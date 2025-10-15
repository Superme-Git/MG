/****************************************************************************
Copyright (c) 2010-2011 cocos2d-x.org
Copyright (c) 2008-2010 Ricardo Quesada
Copyright (c) 2011      Zynga Inc.

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/

#include "CCTextureCache.h"
#include "CCTexture2D.h"
#include "ccMacros.h"
#include "CCDirector.h"
#include "platform/platform.h"
#include "platform/CCFileUtils.h"
#include "platform/CCThread.h"
#include "platform/CCImage.h"
#include "support/ccUtils.h"
#include "CCScheduler.h"
#include "cocoa/CCString.h"
#include <errno.h>
#include <stack>
#include <string>
#include <cctype>
#include <queue>
#include <list>
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
#include <thread>
#include <mutex>
#include <condition_variable>
#else
#include <pthread.h>
#include <semaphore.h>
#endif

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif


using namespace std;

NS_CC_BEGIN

typedef struct _AsyncStruct
{
    std::string            filename;
    CCObject    *target;
    SEL_CallFuncO        selector;
} AsyncStruct;

typedef struct _ImageInfo
{
    AsyncStruct *asyncStruct;
    CCImage        *image;
    CCImage::EImageFormat imageType;
} ImageInfo;
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
static std::thread* s_loadingThread;
static std::mutex   s_asyncStructQueueMutex;
static std::mutex   s_ImageInfoMutex;
struct sem_t {
	sem_t() : _val(0) {}
	volatile int _val;
	std::mutex _mutex;
	std::condition_variable _condition;
};
static sem_t* s_pSem = NULL;
#else
static pthread_t s_loadingThread;

static pthread_mutex_t      s_asyncStructQueueMutex;
static pthread_mutex_t      s_ImageInfoMutex;

static sem_t* s_pSem = NULL;
#endif
static unsigned long s_nAsyncRefCount = 0;
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8

#else
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    #define CC_ASYNC_TEXTURE_CACHE_USE_NAMED_SEMAPHORE 1
#else
    #define CC_ASYNC_TEXTURE_CACHE_USE_NAMED_SEMAPHORE 0
#endif
    

#if CC_ASYNC_TEXTURE_CACHE_USE_NAMED_SEMAPHORE
    #define CC_ASYNC_TEXTURE_CACHE_SEMAPHORE "ccAsync"
#else
    static sem_t s_sem;
#endif
#endif

static bool need_quit = false;

static std::queue<AsyncStruct*>* s_pAsyncStructQueue = NULL;
static std::queue<ImageInfo*>*   s_pImageQueue = NULL;

static CCImage::EImageFormat computeImageFormatType(string& filename)
{
    CCImage::EImageFormat ret = CCImage::kFmtUnKnown;

    if ((std::string::npos != filename.find(".jpg")) || (std::string::npos != filename.find(".jpeg")))
    {
        ret = CCImage::kFmtJpg;
    }
    else if ((std::string::npos != filename.find(".png")) || (std::string::npos != filename.find(".PNG")))
    {
        ret = CCImage::kFmtPng;
    }
    else if ((std::string::npos != filename.find(".tiff")) || (std::string::npos != filename.find(".TIFF")))
    {
        ret = CCImage::kFmtTiff;
    }
    
    return ret;
}

static void* loadImage(void* data)
{
    // create autorelease pool for iOS
    CCThread thread;
    thread.createAutoreleasePool();

    AsyncStruct *pAsyncStruct = NULL;

    while (true)
    {
        // wait for rendering thread to ask for loading if s_pAsyncStructQueue is empty
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		{
			std::unique_lock<std::mutex> lock(s_pSem->_mutex);
			if (s_pSem->_val <= 0) {
				s_pSem->_condition.wait(lock);
			}
			s_pSem->_val--;
		}
#else
		int semWaitRet = sem_wait(s_pSem);
        if( semWaitRet < 0 )
        {
            CCLOG( "CCTextureCache async thread semaphore error: %s\n", strerror( errno ) );
            break;
        }
#endif
        std::queue<AsyncStruct*> *pQueue = s_pAsyncStructQueue;
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		s_asyncStructQueueMutex.lock();
#else
        pthread_mutex_lock(&s_asyncStructQueueMutex);// get async struct from queue
#endif
        if (pQueue->empty())
        {
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
			s_asyncStructQueueMutex.unlock();
#else
            pthread_mutex_unlock(&s_asyncStructQueueMutex);
#endif
            if (need_quit)
                break;
            else
                continue;
        }
        else
        {
            pAsyncStruct = pQueue->front();
            pQueue->pop();
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
			s_asyncStructQueueMutex.unlock();
#else
            pthread_mutex_unlock(&s_asyncStructQueueMutex);
#endif
        }        

        const char *filename = pAsyncStruct->filename.c_str();

        // compute image type
        CCImage::EImageFormat imageType = computeImageFormatType(pAsyncStruct->filename);
        if (imageType == CCImage::kFmtUnKnown)
        {
            CCLOG("unsupportted format %s",filename);
            delete pAsyncStruct;
            
            continue;
        }
        
        // generate image            
        CCImage *pImage = new CCImage();
        if (! pImage->initWithImageFileThreadSafe(filename, imageType))
        {
            delete pImage;
            CCLOG("can not load %s", filename);
            continue;
        }

        // generate image info
        ImageInfo *pImageInfo = new ImageInfo();
        pImageInfo->asyncStruct = pAsyncStruct;
        pImageInfo->image = pImage;
        pImageInfo->imageType = imageType;

        // put the image info into the queue
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		s_ImageInfoMutex.lock();
#else
        pthread_mutex_lock(&s_ImageInfoMutex);
#endif
        s_pImageQueue->push(pImageInfo);
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		s_ImageInfoMutex.unlock();
#else
        pthread_mutex_unlock(&s_ImageInfoMutex);
#endif    
    }
    
    if( s_pSem != NULL )
    {
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		s_pSem->_mutex.unlock();
		delete s_pSem;
#else
    #if CC_ASYNC_TEXTURE_CACHE_USE_NAMED_SEMAPHORE
        sem_unlink(CC_ASYNC_TEXTURE_CACHE_SEMAPHORE);
        sem_close(s_pSem);
    #else
        sem_destroy(s_pSem);
    #endif
#endif
        s_pSem = NULL;
        delete s_pAsyncStructQueue;
        delete s_pImageQueue;
    }
    
    return (void*)0;
}

// implementation CCTextureCache

// TextureCache - Alloc, Init & Dealloc
static CCTextureCache *g_sharedTextureCache = NULL;

CCTextureCache * CCTextureCache::sharedTextureCache()
{
    if (!g_sharedTextureCache)
    {
        g_sharedTextureCache = new CCTextureCache();
    }
    return g_sharedTextureCache;
}

CCTextureCache::CCTextureCache()
{
    CCAssert(g_sharedTextureCache == NULL, "Attempted to allocate a second instance of a singleton.");
    
    m_pTextures = new CCDictionary();
}

CCTextureCache::~CCTextureCache()
{
    CCLOGINFO("cocos2d: deallocing CCTextureCache.");
    need_quit = true;
    if (s_pSem != NULL)
    {
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		{
			std::unique_lock<std::mutex> lock;
			s_pSem->_condition.notify_one();
			s_pSem->_val++;
		}
#else
        sem_post(s_pSem);
#endif
    }
    
    CC_SAFE_RELEASE(m_pTextures);
}

void CCTextureCache::purgeSharedTextureCache()
{
    CC_SAFE_RELEASE_NULL(g_sharedTextureCache);
}

const char* CCTextureCache::description()
{
    return CCString::createWithFormat("<CCTextureCache | Number of textures = %u>", m_pTextures->count())->getCString();
}

CCDictionary* CCTextureCache::snapshotTextures()
{ 
    CCDictionary* pRet = new CCDictionary();
    CCDictElement* pElement = NULL;
    CCDICT_FOREACH(m_pTextures, pElement)
    {
        pRet->setObject(pElement->getObject(), pElement->getStrKey());
    }
    return pRet;
}

void CCTextureCache::addImageAsync(const char *path, CCObject *target, SEL_CallFuncO selector)
{
    CCAssert(path != NULL, "TextureCache: fileimage MUST not be NULL");    

    CCTexture2D *texture = NULL;

    // optimization

    std::string pathKey = path;
    CCFileUtils::sharedFileUtils()->removeSuffixFromFile(pathKey);

    pathKey = CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(pathKey.c_str());
    texture = (CCTexture2D*)m_pTextures->objectForKey(pathKey.c_str());

    std::string fullpath = pathKey;
    if (texture != NULL)
    {
        if (target && selector)
        {
            (target->*selector)(texture);
        }
        
        return;
    }

    // lazy init
    if (s_pSem == NULL)
    {      
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		s_pSem = new sem_t;
#else
#if CC_ASYNC_TEXTURE_CACHE_USE_NAMED_SEMAPHORE
        s_pSem = sem_open(CC_ASYNC_TEXTURE_CACHE_SEMAPHORE, O_CREAT, 0644, 0);
        if( s_pSem == SEM_FAILED )
        {
            CCLOG( "CCTextureCache async thread semaphore init error: %s\n", strerror( errno ) );
            s_pSem = NULL;
            return;
        }
#else
        int semInitRet = sem_init(&s_sem, 0, 0);
        if( semInitRet < 0 )
        {
            CCLOG( "CCTextureCache async thread semaphore init error: %s\n", strerror( errno ) );
            return;
        }
        s_pSem = &s_sem;
#endif
#endif
        s_pAsyncStructQueue = new queue<AsyncStruct*>();
        s_pImageQueue = new queue<ImageInfo*>();        
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		s_loadingThread = new std::thread(loadImage, (void*)NULL);
#else
        pthread_mutex_init(&s_asyncStructQueueMutex, NULL);
        pthread_mutex_init(&s_ImageInfoMutex, NULL);
        pthread_create(&s_loadingThread, NULL, loadImage, NULL);
#endif
        need_quit = false;
    }

    if (0 == s_nAsyncRefCount)
    {
        CCDirector::sharedDirector()->getScheduler()->scheduleSelector(schedule_selector(CCTextureCache::addImageAsyncCallBack), this, 0, false);
    }

    ++s_nAsyncRefCount;

    if (target)
    {
        target->retain();
    }

    // generate async struct
    AsyncStruct *data = new AsyncStruct();
    data->filename = fullpath.c_str();
    data->target = target;
    data->selector = selector;

    // add async struct into queue
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	s_asyncStructQueueMutex.lock();
	s_pAsyncStructQueue->push(data);
	s_asyncStructQueueMutex.unlock();
	{
		std::unique_lock<std::mutex> lock(s_pSem->_mutex);
		s_pSem->_condition.notify_one();
		s_pSem->_val++;
	}
#else
    pthread_mutex_lock(&s_asyncStructQueueMutex);
    s_pAsyncStructQueue->push(data);
    pthread_mutex_unlock(&s_asyncStructQueueMutex);
	sem_post(s_pSem);
#endif
}

void CCTextureCache::addImageAsyncCallBack(float dt)
{
    // the image is generated in loading thread
    std::queue<ImageInfo*> *imagesQueue = s_pImageQueue;
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	s_ImageInfoMutex.lock();
#else
    pthread_mutex_lock(&s_ImageInfoMutex);
#endif
    if (imagesQueue->empty())
    {
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		s_ImageInfoMutex.unlock();
#else
        pthread_mutex_unlock(&s_ImageInfoMutex);
#endif
    }
    else
    {
        ImageInfo *pImageInfo = imagesQueue->front();
        imagesQueue->pop();
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		s_ImageInfoMutex.unlock();
#else
        pthread_mutex_unlock(&s_ImageInfoMutex);
#endif
        AsyncStruct *pAsyncStruct = pImageInfo->asyncStruct;
        CCImage *pImage = pImageInfo->image;

        CCObject *target = pAsyncStruct->target;
        SEL_CallFuncO selector = pAsyncStruct->selector;
        const char* filename = pAsyncStruct->filename.c_str();

        // generate texture in render thread
        CCTexture2D *texture = new CCTexture2D();
#if 0 //TODO: (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
        texture->initWithImage(pImage, kCCResolutioniPhone);
#else
        texture->initWithImage(pImage);
#endif

#if CC_ENABLE_CACHE_TEXTURE_DATA
       // cache the texture file name
       VolatileTexture::addImageTexture(texture, filename, pImageInfo->imageType);
#endif

        // cache the texture
        m_pTextures->setObject(texture, filename);
        texture->autorelease();

        if (target && selector)
        {
            (target->*selector)(texture);
            target->release();
        }        

        pImage->release();
        delete pAsyncStruct;
        delete pImageInfo;

        --s_nAsyncRefCount;
        if (0 == s_nAsyncRefCount)
        {
            CCDirector::sharedDirector()->getScheduler()->unscheduleSelector(schedule_selector(CCTextureCache::addImageAsyncCallBack), this);
        }
    }
}

CCTexture2D * CCTextureCache::addImage(const char * path)
{
    CCAssert(path != NULL, "TextureCache: fileimage MUST not be NULL");

    CCTexture2D * texture = NULL;
    // Split up directory and filename
    // MUTEX:
    // Needed since addImageAsync calls this method from a different thread
    
    //pthread_mutex_lock(m_pDictLock);

    // remove possible -HD suffix to prevent caching the same image twice (issue #1040)
    std::string pathKey = path;
    ccResolutionType resolution = kCCResolutionUnknown;
    CCFileUtils::sharedFileUtils()->removeSuffixFromFile(pathKey);

    pathKey = CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(pathKey.c_str());
    texture = (CCTexture2D*)m_pTextures->objectForKey(pathKey.c_str());

    std::string fullpath = pathKey; // (CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(path));
    if( ! texture ) 
    {
        std::string lowerCase(path);
        for (unsigned int i = 0; i < lowerCase.length(); ++i)
        {
            lowerCase[i] = tolower(lowerCase[i]);
        }
        // all images are handled by UIImage except PVR extension that is handled by our own handler
        do 
        {
            if (std::string::npos != lowerCase.find(".pvr"))
            {
                texture = this->addPVRImage(fullpath.c_str());
            }
            else
            {
                CCImage::EImageFormat eImageFormat = CCImage::kFmtUnKnown;
                if (std::string::npos != lowerCase.find(".png"))
                {
                    eImageFormat = CCImage::kFmtPng;
                }
                else if (std::string::npos != lowerCase.find(".jpg") || std::string::npos != lowerCase.find(".jpeg"))
                {
                    eImageFormat = CCImage::kFmtJpg;
                }
                else if (std::string::npos != lowerCase.find(".tif") || std::string::npos != lowerCase.find(".tiff"))
                {
                    eImageFormat = CCImage::kFmtTiff;
                }else if (std::string::npos != lowerCase.find(".dds"))
                {
                    eImageFormat = CCImage::kFmtDDS;
                }
                
                CCImage image;                
                unsigned long nSize = 0;
                unsigned char* pBuffer = CCFileUtils::sharedFileUtils()->getFileData(fullpath.c_str(), "rb", &nSize);
                CC_BREAK_IF(! image.initWithImageData((void*)pBuffer, nSize, eImageFormat));
                CC_SAFE_DELETE_ARRAY(pBuffer);

                texture = new CCTexture2D();
                texture->initWithImage(&image, resolution);

                if( texture )
                {
#if CC_ENABLE_CACHE_TEXTURE_DATA
                    // cache the texture file name
                    VolatileTexture::addImageTexture(texture, fullpath.c_str(), eImageFormat);
#endif

                    m_pTextures->setObject(texture, pathKey.c_str());
                    texture->release();
                }
                else
                {
                    CCLOG("cocos2d: Couldn't add image:%s in CCTextureCache", path);
                }
            }
        } while (0);
    }

    //pthread_mutex_unlock(m_pDictLock);
    return texture;
}

#ifdef CC_SUPPORT_PVRTC
CCTexture2D* CCTextureCache::addPVRTCImage(const char* path, int bpp, bool hasAlpha, int width)
{
    CCAssert(path != NULL, "TextureCache: fileimage MUST not be nill");
    CCAssert( bpp==2 || bpp==4, "TextureCache: bpp must be either 2 or 4");

    CCTexture2D * texture;

    std::string temp(path);
    CCFileUtils::sharedFileUtils()->removeSuffixFromFile(temp);
    
    if ( (texture = (CCTexture2D*)m_pTextures->objectForKey(temp.c_str())) )
    {
        return texture;
    }
    
    // Split up directory and filename
    std::string fullpath( CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(path) );

    unsigned long nLen = 0;
    unsigned char* pData = CCFileUtils::sharedFileUtils()->getFileData(fullpath.c_str(), "rb", &nLen);

    texture = new CCTexture2D();
    
    if( texture->initWithPVRTCData(pData, 0, bpp, hasAlpha, width,
                                   (bpp==2 ? kCCTexture2DPixelFormat_PVRTC2 : kCCTexture2DPixelFormat_PVRTC4)))
    {
        m_pTextures->setObject(texture, temp.c_str());
        texture->autorelease();
    }
    else
    {
        CCLOG("cocos2d: Couldn't add PVRTCImage:%s in CCTextureCache",path);
    }
    CC_SAFE_DELETE_ARRAY(pData);

    return texture;
}
#endif // CC_SUPPORT_PVRTC

CCTexture2D * CCTextureCache::addPVRImage(const char* path)
{
    CCAssert(path != NULL, "TextureCache: fileimage MUST not be nill");

    CCTexture2D* tex = NULL;
    std::string key(path);
    // remove possible -HD suffix to prevent caching the same image twice (issue #1040)
    CCFileUtils::sharedFileUtils()->removeSuffixFromFile(key);
    
    if( (tex = (CCTexture2D*)m_pTextures->objectForKey(key.c_str())) ) 
    {
        return tex;
    }

    // Split up directory and filename
    std::string fullpath = CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(key.c_str());
    tex = new CCTexture2D();
    if(tex != NULL && tex->initWithPVRFile(fullpath.c_str()) )
    {
#if CC_ENABLE_CACHE_TEXTURE_DATA
        // cache the texture file name
        VolatileTexture::addImageTexture(tex, fullpath.c_str(), CCImage::kFmtRawData);
#endif
        m_pTextures->setObject(tex, key.c_str());
        tex->autorelease();
    }
    else
    {
        CCLOG("cocos2d: Couldn't add PVRImage:%s in CCTextureCache",key.c_str());
        CC_SAFE_DELETE(tex);
    }

    return tex;
}

CCTexture2D* CCTextureCache::addUIImage(CCImage *image, const char *key)
{
    CCAssert(image != NULL, "TextureCache: image MUST not be nill");

    CCTexture2D * texture = NULL;
    // textureForKey() use full path,so the key should be full path
    std::string forKey;
    if (key)
    {
        forKey = CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(key);
    }

    // Don't have to lock here, because addImageAsync() will not 
    // invoke opengl function in loading thread.

    do 
    {
        // If key is nil, then create a new texture each time
        if(key && (texture = (CCTexture2D *)m_pTextures->objectForKey(forKey.c_str())))
        {
            break;
        }

        // prevents overloading the autorelease pool
        texture = new CCTexture2D();
        texture->initWithImage(image, kCCResolutionUnknown);

        if(key && texture)
        {
            m_pTextures->setObject(texture, forKey.c_str());
            texture->autorelease();
        }
        else
        {
            CCLOG("cocos2d: Couldn't add UIImage in CCTextureCache");
        }

    } while (0);

#if CC_ENABLE_CACHE_TEXTURE_DATA
    VolatileTexture::addCCImage(texture, image);
#endif
    
    return texture;
}

// TextureCache - Remove

void CCTextureCache::removeAllTextures()
{
    m_pTextures->removeAllObjects();
}

void CCTextureCache::removeUnusedTextures()
{
    /*
    CCDictElement* pElement = NULL;
    CCDICT_FOREACH(m_pTextures, pElement)
    {
        CCLOG("cocos2d: CCTextureCache: texture: %s", pElement->getStrKey());
        CCTexture2D *value = (CCTexture2D*)pElement->getObject();
        if (value->retainCount() == 1)
        {
            CCLOG("cocos2d: CCTextureCache: removing unused texture: %s", pElement->getStrKey());
            m_pTextures->removeObjectForElememt(pElement);
        }
    }
     */
    
    /** Inter engineer zhuoshi sun finds that this way will get better performance
     */    
    if (m_pTextures->count())
    {   
        // find elements to be removed
        CCDictElement* pElement = NULL;
        list<CCDictElement*> elementToRemove;
        CCDICT_FOREACH(m_pTextures, pElement)
        {
            CCLOG("cocos2d: CCTextureCache: texture: %s", pElement->getStrKey());
            CCTexture2D *value = (CCTexture2D*)pElement->getObject();
            if (value->retainCount() == 1)
            {
                elementToRemove.push_back(pElement);
            }
        }
        
        // remove elements
        for (list<CCDictElement*>::iterator iter = elementToRemove.begin(); iter != elementToRemove.end(); ++iter)
        {
            CCLOG("cocos2d: CCTextureCache: removing unused texture: %s", (*iter)->getStrKey());
            m_pTextures->removeObjectForElememt(*iter);
        }
    }
}

void CCTextureCache::removeTexture(CCTexture2D* texture)
{
    if( ! texture )
    {
        return;
    }

    CCArray* keys = m_pTextures->allKeysForObject(texture);
    m_pTextures->removeObjectsForKeys(keys);
}

void CCTextureCache::removeTextureForKey(const char *textureKeyName)
{
    if (textureKeyName == NULL)
    {
        return;
    }

    string fullPath = CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(textureKeyName);
    m_pTextures->removeObjectForKey(fullPath.c_str());
}

CCTexture2D* CCTextureCache::textureForKey(const char* key)
{
    return (CCTexture2D*)m_pTextures->objectForKey(CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(key));
}

void CCTextureCache::reloadAllTextures(CCFileProvidor* pProvidor/* = NULL*/)
{
#if CC_ENABLE_CACHE_TEXTURE_DATA
    VolatileTexture::reloadAllTextures(pProvidor);
#endif
}

void CCTextureCache::dumpCachedTextureInfo()
{
    unsigned int count = 0;
    unsigned int totalBytes = 0;

    CCDictElement* pElement = NULL;
    CCDICT_FOREACH(m_pTextures, pElement)
    {
        CCTexture2D* tex = (CCTexture2D*)pElement->getObject();
        unsigned int bpp = tex->bitsPerPixelForFormat();
        // Each texture takes up width * height * bytesPerPixel bytes.
        unsigned int bytes = tex->getPixelsWide() * tex->getPixelsHigh() * bpp / 8;
        totalBytes += bytes;
        count++;
        CCLOG("cocos2d: \"%s\" rc=%lu id=%lu %lu x %lu @ %ld bpp => %lu KB",
               pElement->getStrKey(),
               (long)tex->retainCount(),
               (long)tex->getName(),
               (long)tex->getPixelsWide(),
               (long)tex->getPixelsHigh(),
               (long)bpp,
               (long)bytes / 1024);
    }

    CCLOG("cocos2d: CCTextureCache dumpDebugInfo: %ld textures, for %lu KB (%.2f MB)", (long)count, (long)totalBytes / 1024, totalBytes / (1024.0f*1024.0f));
}

#if CC_ENABLE_CACHE_TEXTURE_DATA

std::list<VolatileTexture*> VolatileTexture::textures;
bool VolatileTexture::isReloading = false;

VolatileTexture::VolatileTexture(CCTexture2D *t)
: texture(t)
, m_eCashedImageType(kInvalid)
, m_pTextureData(NULL)
, m_PixelFormat(kTexture2DPixelFormat_RGBA8888)
, m_strFileName("")
, m_FmtImage(CCImage::kFmtPng)
, m_alignment(kCCTextAlignmentCenter)
, m_vAlignment(kCCVerticalTextAlignmentCenter)
, m_strFontName("")
, m_strText("")
, uiImage(NULL)
, m_fFontSize(0.0f)
{
    m_size = CCSizeMake(0, 0);
    textures.push_back(this);
}

VolatileTexture::~VolatileTexture()
{
    textures.remove(this);
    CC_SAFE_RELEASE(uiImage);
    
    if((m_eCashedImageType==kAtcImage||m_eCashedImageType==kPVRTCImage||m_eCashedImageType==kDDSTCImage
        ||m_eCashedImageType==kETCImage)
       &&m_pTextureData!=NULL
       )
    {
        delete [] m_pTextureData;
        m_pTextureData=NULL;
        
//        LOGD("release atc texture cache \n");
    }

}

void VolatileTexture::addImageTexture(CCTexture2D *tt, const char* imageFileName, CCImage::EImageFormat format)
{
    if (isReloading)
    {
        return;
    }

    VolatileTexture *vt = findVolotileTexture(tt);

    vt->m_eCashedImageType = kImageFile;
    vt->m_strFileName = imageFileName;
    vt->m_FmtImage    = format;
    vt->m_PixelFormat = tt->getPixelFormat();
}

void VolatileTexture::addCCImage(CCTexture2D *tt, CCImage *image)
{
    VolatileTexture *vt = findVolotileTexture(tt);
    image->retain();
    vt->uiImage = image;
    vt->m_eCashedImageType = kImage;
}

VolatileTexture* VolatileTexture::findVolotileTexture(CCTexture2D *tt)
{
    VolatileTexture *vt = 0;
    std::list<VolatileTexture *>::iterator i = textures.begin();
    while (i != textures.end())
    {
        VolatileTexture *v = *i++;
        if (v->texture == tt) 
        {
            vt = v;
            break;
        }
    }
    
    if (! vt)
    {
        vt = new VolatileTexture(tt);
    }
    
    return vt;
}

void VolatileTexture::addDataTexture(CCTexture2D *tt, void* data, CCTexture2DPixelFormat pixelFormat, const CCSize& contentSize)
{
    if (isReloading)
    {
        return;
    }

    VolatileTexture *vt = findVolotileTexture(tt);

    vt->m_eCashedImageType = kImageData;
    vt->m_pTextureData = data;
    vt->m_PixelFormat = pixelFormat;
    vt->m_TextureSize = contentSize;
}

void VolatileTexture::addStringTexture(CCTexture2D *tt, const char* text, const CCSize& dimensions, CCTextAlignment alignment, 
                                       CCVerticalTextAlignment vAlignment, const char *fontName, float fontSize)
{
    if (isReloading)
    {
        return;
    }

    VolatileTexture *vt = findVolotileTexture(tt);

    vt->m_eCashedImageType = kString;
    vt->m_size        = dimensions;
    vt->m_strFontName = fontName;
    vt->m_alignment   = alignment;
    vt->m_vAlignment  = vAlignment;
    vt->m_fFontSize   = fontSize;
    vt->m_strText     = text;
}

void VolatileTexture::removeTexture(CCTexture2D *t) 
{

    std::list<VolatileTexture *>::iterator i = textures.begin();
    while (i != textures.end())
    {
        VolatileTexture *vt = *i++;
        if (vt->texture == t) 
        {
            delete vt;
            break;
        }
    }
}

void VolatileTexture::reloadAllTextures(CCFileProvidor* pProvidor/* = NULL*/)
{
    isReloading = true;

    CCLOG("reload all texture");
    std::list<VolatileTexture *>::iterator iter = textures.begin();

    while (iter != textures.end())
    {
        VolatileTexture *vt = *iter++;

        switch (vt->m_eCashedImageType)
        {
        case kImageFile:
            {
                CCImage image;
                std::string lowerCase(vt->m_strFileName.c_str());
                for (unsigned int i = 0; i < lowerCase.length(); ++i)
                {
                    lowerCase[i] = tolower(lowerCase[i]);
                }

                if (std::string::npos != lowerCase.find(".pvr")) 
                {
                    CCTexture2DPixelFormat oldPixelFormat = CCTexture2D::defaultAlphaPixelFormat();
                    CCTexture2D::setDefaultAlphaPixelFormat(vt->m_PixelFormat);

                    vt->texture->initWithPVRFile(vt->m_strFileName.c_str());
                    CCTexture2D::setDefaultAlphaPixelFormat(oldPixelFormat);
                } 
                else 
                {
					continue;
                    LOGD("info!!!, in VolatileTexture::reloadAllTextures pProvidor should not null\n");
                    if (pProvidor == NULL) {
                        LOGD("error!!!, in VolatileTexture::reloadAllTextures pProvidor should not null\n");
                    }
                    CCFileProvidor::CCFileImage fileImg;
                    if (pProvidor &&
                        pProvidor->LoadFile(fileImg, vt->m_strFileName.c_str()) &&
                        fileImg.pData != NULL &&
                        image.initWithImageData(fileImg.pData, fileImg.iSize, vt->m_FmtImage))
                    {
                        CCTexture2DPixelFormat oldPixelFormat = CCTexture2D::defaultAlphaPixelFormat();
                        CCTexture2D::setDefaultAlphaPixelFormat(vt->m_PixelFormat);
                        vt->texture->initWithImage(&image);
                        CCTexture2D::setDefaultAlphaPixelFormat(oldPixelFormat);
                    }
                }
            }
            break;
        case kImageData:
            {
                vt->texture->initWithData(vt->m_pTextureData, 
                                          vt->m_PixelFormat, 
                                          vt->m_TextureSize.width, 
                                          vt->m_TextureSize.height, 
                                          vt->m_TextureSize);
            }
            break;
        case kString:
            {
                vt->texture->initWithString(vt->m_strText.c_str(),
                    vt->m_size,
                    vt->m_alignment,
                    vt->m_vAlignment,
                    vt->m_strFontName.c_str(),
                    vt->m_fFontSize);
            }
            break;
        case kImage:
            {
                vt->texture->initWithImage(vt->uiImage,
                                           kCCResolutionUnknown);
            }
            break;
        case kAtcImage:
            {
                vt->texture->initWithATCData(((unsigned char *)vt->m_pTextureData) + 4*sizeof(unsigned int), 0, 8, true, (int)vt->m_TextureSize.width,(int) vt->m_TextureSize.height,cocos2d::kCCTexture2DPixelFormat_ATC_Explicit);
            }
            break;
        case kPVRTCImage:
            {
#ifdef CC_SUPPORT_PVRTC 
                int width=(int)vt->m_TextureSize.width;
                vt->texture->initWithPVRTCData(((unsigned char *)vt->m_pTextureData) + 13*sizeof(unsigned int), 0, 4, true, CC_SWAP_INT32_LITTLE_TO_HOST(width),cocos2d::kCCTexture2DPixelFormat_PVRTC4);
#else
		CCAssert(false);
#endif
               
            }
            break;
        case kDDSTCImage:
            {
                vt->texture->initWithDDSCompressData(((unsigned char*)vt->m_pTextureData) + sizeof(DDS_HEADER) + 4, 0, 8, true, (int)vt->m_TextureSize.width,(int) vt->m_TextureSize.height, vt->m_PixelFormat);
            }
            break;
        case kETCImage:
            {
                
                int dataSize=(int)vt->m_size.width;
                int bitmaskRedSize=(int)vt->m_size.height;
                int width=(int)vt->m_TextureSize.width;
                int height=(int) vt->m_TextureSize.height;
                
                vt->texture->initWithETCData(((unsigned char*)vt->m_pTextureData),
                                             0, 8, true, CC_SWAP_INT32_LITTLE_TO_HOST(width), CC_SWAP_INT32_LITTLE_TO_HOST(height), cocos2d::kCCTexture2DPixelFormat_ETC);
            }
            break;

        default:
            break;
        }
    }

    isReloading = false;
}

void VolatileTexture::addAtcImage(CCTexture2D *tt, const void* data,int size)
{
    if (isReloading)
    {
        return;
    }
    
    VolatileTexture *vt = findVolotileTexture(tt);
    
    vt->m_eCashedImageType = kAtcImage;
    
    
    unsigned char* pTempData = new unsigned char[size];
    
    memcpy(pTempData, data, size);
    
    vt->m_pTextureData = pTempData;
    
    vt->m_TextureSize= tt->getContentSizeInPixels();
    
}

void VolatileTexture::addPVRTCImage(CCTexture2D *tt, const void* data,int size)
{
    if (isReloading)
    {
        return;
    }
    
    VolatileTexture *vt = findVolotileTexture(tt);
    
    vt->m_eCashedImageType = kPVRTCImage;
    
    
    unsigned char* pTempData = new unsigned char[size];
    
    memcpy(pTempData, data, size);
    
    vt->m_pTextureData = pTempData;
    
    vt->m_TextureSize= tt->getContentSizeInPixels();
    
}

void VolatileTexture::addDDSTCImage(CCTexture2D *tt, const void* data,int size,CCTexture2DPixelFormat pixelFormat)
{
    if (isReloading)
    {
        return;
    }
    
    VolatileTexture *vt = findVolotileTexture(tt);
    
    vt->m_eCashedImageType = kDDSTCImage;
    
    
    unsigned char* pTempData = new unsigned char[size];
    
    memcpy(pTempData, data, size);
    
    vt->m_pTextureData = pTempData;
    
    vt->m_TextureSize= tt->getContentSizeInPixels();
    vt->m_PixelFormat=pixelFormat;
    
}

void VolatileTexture::addETCImage(CCTexture2D *tt, const void* data,int size,int bitMaskRedSize)
{
    if (isReloading)
    {
        return;
    }
    
    VolatileTexture *vt = findVolotileTexture(tt);
    
    vt->m_eCashedImageType = kETCImage;
    
    
    unsigned char* pTempData = new unsigned char[size];
    
    memcpy(pTempData, data, size);
    
    vt->m_pTextureData = pTempData;
    
    vt->m_TextureSize= tt->getContentSizeInPixels();
    vt->m_size.width=(float)size;
    vt->m_size.height=(float)bitMaskRedSize;
}

#endif // CC_ENABLE_CACHE_TEXTURE_DATA

NS_CC_END

