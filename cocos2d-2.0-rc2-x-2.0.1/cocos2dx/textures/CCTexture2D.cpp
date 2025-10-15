/****************************************************************************
Copyright (c) 2010-2012 cocos2d-x.org
Copyright (c) 2008      Apple Inc. All Rights Reserved.

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



/*
* Support for RGBA_4_4_4_4 and RGBA_5_5_5_1 was copied from:
* https://devforums.apple.com/message/37855#37855 by a1studmuffin
*/

#include "CCTexture2D.h"
#include "ccConfig.h"
#include "ccMacros.h"
#include "CCConfiguration.h"
#include "platform/platform.h"
#include "platform/CCImage.h"
#include "CCGL.h"
#include "support/ccUtils.h"
#include "platform/CCPlatformMacros.h"
#include "textures/CCTexturePVR.h"
#include "CCDirector.h"
#include "shaders/CCGLProgram.h"
#include "shaders/ccGLStateCache.h"
#include "shaders/CCShaderCache.h"

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

#if CC_ENABLE_CACHE_TEXTURE_DATA
    #include "CCTextureCache.h"
#endif
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
#include "ETCHeader.h"
#endif
NS_CC_BEGIN

//CLASS IMPLEMENTATIONS:

// If the image has alpha, you can create RGBA8 (32-bit) or RGBA4 (16-bit) or RGB5A1 (16-bit)
// Default is: RGBA8888 (32-bit textures)
static CCTexture2DPixelFormat g_defaultAlphaPixelFormat = kCCTexture2DPixelFormat_Default;

// By default PVR images are treated as if they don't have the alpha channel premultiplied
static bool PVRHaveAlphaPremultiplied_ = false;

int CCImage::ms_iTotalPhysMemory = 768 * 1024;
int CCImage::ms_iTotalPhysMemoryLimit = 768 * 1024;
bool CCImage::ms_bNormal = true;

void CCImage::SetTotalPhysMemory(int TotalPhysMemory)
{
	ms_iTotalPhysMemory = TotalPhysMemory;
}
void CCImage::SetTotalPhysMemoryLimit(int TotalPhysMemoryLimit)
{
	ms_iTotalPhysMemoryLimit = TotalPhysMemoryLimit;
}
void CCImage::SetIsNormal(bool bNormal)
{
	ms_bNormal = bNormal;
}
int CCImage::GetTotalPhysMemory()
{
	return ms_iTotalPhysMemory;
}
int CCImage::GetTotalPhysMemoryLimit()
{
	return ms_iTotalPhysMemoryLimit;
}
bool CCImage::IsNormal()
{
	return ms_bNormal;
}

CCTexture2D::CCTexture2D()
: m_bIsEtcTexture(false)
, m_etcAlphaChannel(0)
, m_uPixelsWide(0)
, m_uPixelsHigh(0)
, m_uName(0)
, m_fMaxS(0.0)
, m_fMaxT(0.0)
, m_bHasPremultipliedAlpha(false)
, m_bHasMipmaps(false)
, m_bPVRHaveAlphaPremultiplied(true)
, m_pShaderProgram(NULL)
{
}

CCTexture2D::~CCTexture2D()
{
#if CC_ENABLE_CACHE_TEXTURE_DATA
    VolatileTexture::removeTexture(this);
#endif

    CCLOGINFO("cocos2d: deallocing CCTexture2D %u.", m_uName);
    CC_SAFE_RELEASE(m_pShaderProgram);

    if(m_uName)
    {
        ccGLDeleteTexture(m_uName);
    }
    
    if (m_etcAlphaChannel) {
        ccGLDeleteTexture(m_etcAlphaChannel);
        m_bIsEtcTexture = false;
        m_etcAlphaChannel = 0;
    }
}

CCTexture2DPixelFormat CCTexture2D::getPixelFormat()
{
    return m_ePixelFormat;
}

unsigned int CCTexture2D::getPixelsWide()
{
    return m_uPixelsWide;
}

unsigned int CCTexture2D::getPixelsHigh()
{
    return m_uPixelsHigh;
}

//GLuint CCTexture2D::getName()
//{
//    return m_uName;
//}

CCSize CCTexture2D::getContentSize()
{

    CCSize ret;
    ret.width = m_tContentSize.width / CC_CONTENT_SCALE_FACTOR();
    ret.height = m_tContentSize.height / CC_CONTENT_SCALE_FACTOR();
    
    return ret;
}

const CCSize& CCTexture2D::getContentSizeInPixels()
{
    return m_tContentSize;
}

GLfloat CCTexture2D::getMaxS()
{
    return m_fMaxS;
}

void CCTexture2D::setMaxS(GLfloat maxS)
{
    m_fMaxS = maxS;
}

GLfloat CCTexture2D::getMaxT()
{
    return m_fMaxT;
}

void CCTexture2D::setMaxT(GLfloat maxT)
{
    m_fMaxT = maxT;
}

CCGLProgram* CCTexture2D::getShaderProgram(void)
{
    return m_pShaderProgram;
}

void CCTexture2D::setShaderProgram(CCGLProgram* pShaderProgram)
{
    CC_SAFE_RETAIN(pShaderProgram);
    CC_SAFE_RELEASE(m_pShaderProgram);
    m_pShaderProgram = pShaderProgram;
}

void CCTexture2D::releaseData(void *data)
{
    free(data);
}

void* CCTexture2D::keepData(void *data, unsigned int length)
{
    CC_UNUSED_PARAM(length);
    //The texture data mustn't be saved becuase it isn't a mutable texture.
    return data;
}

bool CCTexture2D::hasPremultipliedAlpha()
{
    return m_bHasPremultipliedAlpha;
}

unsigned int ABGR2UIColor(unsigned char A, unsigned char B, unsigned char G, unsigned char R)
{
	unsigned int uiResult = 0;
	uiResult = A << 24;
	uiResult = uiResult | B << 16;
	uiResult = uiResult | G << 8;
	uiResult = uiResult | R;
	return uiResult;
}
void UIColor2ABGR(unsigned int UIColor, unsigned char& A, unsigned char& B, unsigned char& G, unsigned char& R)
{
	R = UIColor & 0xFF;
	G = (UIColor >> 8) & 0xFF;
	B = (UIColor >> 16) & 0xFF;
	A = (UIColor >> 24) & 0xFF;
}
unsigned int RGB2HSV(unsigned int ABGR)
{
	unsigned int Result = ABGR;
	unsigned char bR;
	unsigned char bG;
	unsigned char bB;
	unsigned char bA;
	UIColor2ABGR(Result, bA, bB, bG, bR);

	float fR = bR / 255.0;
	float fG = bG / 255.0;
	float fB = bB / 255.0;
	float fA = bA / 255.0;

	float fMax = 0;
	float fMin = 0;
	float fDelta = 0;

	fMax = fmax(fR, fmax(fG, fB));
	fMin = fmin(fR, fmin(fG, fB));
	fDelta = fMax - fMin;

	float H = 0;
	float S = 0;
	float V = 0;

	V = fMax;		             // Brightness
	if (fDelta == 0)			// No Saturation, so Hue is undefined (achromatic)
	{
		H = 0;
		S = 0;
		Result = ABGR2UIColor(bA, V * 255, S, H);
		return Result;
	}
	S = fDelta / fMax;       // Saturation

	if (fR == fMax) H = (fG - fB) / fDelta / 6;             // color between yellow and magenta
	else if (fG == fMax) H = (2 + (fB - fR) / fDelta) / 6;  // color between cyan and yellow
	else H = (4 + (fR - fG) / fDelta) / 6;                // color between magenta and cyan
	if (H < 0) H += 1;

	Result = ABGR2UIColor(bA, V * 255, S * 255, H * 255);
	return Result;
}

bool CCTexture2D::initWithData(const void *data, CCTexture2DPixelFormat pixelFormat, unsigned int pixelsWide, unsigned int pixelsHigh, const CCSize& contentSize, int iScale/* = 100*/)
{
    // XXX: 32 bits or POT textures uses UNPACK of 4 (is this correct ??? )
    if( pixelFormat == kCCTexture2DPixelFormat_RGBA8888 || ( ccNextPOT(pixelsWide)==pixelsWide && ccNextPOT(pixelsHigh)==pixelsHigh) )
    {
        glPixelStorei(GL_UNPACK_ALIGNMENT,4);
    }
    else
    {
        glPixelStorei(GL_UNPACK_ALIGNMENT,1);
    }

    glGenTextures(1, &m_uName);
    ccGLBindTexture2D(m_uName);

    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );

    // Specify OpenGL texture image

	bool bScale = false;

    switch(pixelFormat)
    {
	case kCCTexture2DPixelFormat_RGBA8888:
	{
		if (data != NULL && pixelsWide % 2 == 0 && pixelsHigh % 2 == 0 && iScale != 100)
		{
			bScale = true;
			GLint PixSize = 0;
			PixSize = pixelsWide * pixelsHigh;
			unsigned int* uiB = (unsigned int*)data;
			unsigned int uiCurPix;

			GLint PixSizeNew = 0;
			PixSizeNew = (pixelsWide / 2) * (pixelsHigh / 2);
			GLvoid* pPixBuffer = NULL;
			GLint BufferSize = PixSizeNew * 4;
			pPixBuffer = (GLvoid*)malloc(BufferSize);
			memset(pPixBuffer, 0, BufferSize);
			unsigned int* uiBNew = (unsigned int*)pPixBuffer;
			long long uiCurPixNew;

			for (int i = 0; i < pixelsHigh / 2; i++)
			{
				for (int i2 = 0; i2 < pixelsWide / 2; i2++)
				{
					uiBNew[i * pixelsWide / 2 + i2] = uiB[i2 * 2 + i * 2 * pixelsWide];
					//uiBNew[i * m_nWidth / 2 + i2] = uiCurPixNew;
				}
			}
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)pixelsWide / 2, (GLsizei)pixelsHigh / 2, 0, GL_RGBA, GL_UNSIGNED_BYTE, pPixBuffer);
			CC_SAFE_DELETE_ARRAY(pPixBuffer);

			//for (int i = 0; i < PixSize; i++)
			//{
			//	uiCurPix = uiB[i];
			//	uiCurPix = 0xFF0000FF;//ABGR
			//	//uiCurPix = RGB2HSV(uiCurPix);
			//	uiB[i] = uiCurPix;
			//}
		}
		else
		{
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		}
		//if (iPixelConvert == 1)//OPENGL --> O OPENGLES --> X
		//{
		//	GLvoid* pPixBuffer = NULL;
		//	GLint PixSize = 0;
		//	PixSize = pixelsWide * pixelsHigh;
		//	GLint BufferSize = PixSize * 4;
		//	pPixBuffer = (GLvoid*)malloc(BufferSize);
		//	memset(pPixBuffer, 0, BufferSize);
		//	glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, pPixBuffer);
		//	unsigned int* uiB = (unsigned int*)pPixBuffer;
		//	unsigned int uiCurPix;
		//	for (int i = 0; i < PixSize; i++)
		//	{
		//		uiCurPix = uiB[i];
		//		//uiCurPix = 0xFFFF00FF;//ABGR
		//		uiCurPix = RGB2HSV(uiCurPix);
		//		uiB[i] = uiCurPix;
		//	}
		//	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGBA, GL_UNSIGNED_BYTE, pPixBuffer);
		//	free(pPixBuffer);
		//}
		//if (iPixelConvert == 1)//OPENGL --> O OPENGLES --> O
		//{
		//	GLubyte* pPixBuffer = NULL;
		//	GLint PixSize = 0;
		//	PixSize = pixelsWide * pixelsHigh;
		//	GLint BufferSize = PixSize * 4;
		//	pPixBuffer = (GLubyte*)malloc(BufferSize);
		//	memset(pPixBuffer, 0, BufferSize);
		//	GLint sfb;
		//	GLuint osfb;
		//	glGetIntegerv(GL_FRAMEBUFFER_BINDING, &sfb);
		//	glGenFramebuffers(1, &osfb);
		//	glBindFramebuffer(GL_FRAMEBUFFER, osfb);
		//	glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, m_uName, 0);
		//	GLenum status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		//	if (status != GL_FRAMEBUFFER_COMPLETE)
		//	{
		//	}
		//	glReadPixels(0, 0, pixelsWide, pixelsHigh, GL_RGBA, GL_UNSIGNED_BYTE, pPixBuffer);
		//	unsigned int* uiB = (unsigned int*)pPixBuffer;
		//	unsigned int uiCurPix;
		//	for (int i = 0; i < PixSize; i++)
		//	{
		//		uiCurPix = uiB[i];
		//		//uiCurPix = 0xFFFF00FF;//ABGR
		//		uiCurPix = RGB2HSV(uiCurPix);
		//		uiB[i] = uiCurPix;
		//	}
		//	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGBA, GL_UNSIGNED_BYTE, pPixBuffer);
		//	free(pPixBuffer);
		//	glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, 0, 0);
		//	glBindFramebuffer(GL_FRAMEBUFFER, sfb);
		//	glDeleteFramebuffers(1, &osfb);
		//}
	}
        break;
    case kCCTexture2DPixelFormat_RGB888:
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
        break;
    case kCCTexture2DPixelFormat_RGBA4444:
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGBA, GL_UNSIGNED_SHORT_4_4_4_4, data);
        break;
    case kCCTexture2DPixelFormat_RGB5A1:
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGBA, GL_UNSIGNED_SHORT_5_5_5_1, data);
        break;
    case kCCTexture2DPixelFormat_RGB565:
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, data);
        break;
    case kCCTexture2DPixelFormat_AI88:
        glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE_ALPHA, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE, data);
        break;
    case kCCTexture2DPixelFormat_A8:
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_ALPHA, GL_UNSIGNED_BYTE, data);
        break;
    case kCCTexture2DPixelFormat_I8:
        glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, data);
        break;
    default:
        CCAssert(0, "NSInternalInconsistencyException");

    }

	m_ePixelFormat = pixelFormat;
	m_tContentSize = contentSize;
	m_uPixelsWide = pixelsWide;
	m_uPixelsHigh = pixelsHigh;
	m_fMaxS = contentSize.width / (float)(pixelsWide);
	m_fMaxT = contentSize.height / (float)(pixelsHigh);
    m_bHasPremultipliedAlpha = false;
    m_bHasMipmaps = false;
    m_eResolutionType = kCCResolutionUnknown;
    //setShaderProgram(CCShaderCache::sharedShaderCache()->programForKey(kCCShader_PositionTexture));

	//if (bScale)
	//{
	//	//m_uPixelsWide = pixelsWide / 2;
	//	//m_uPixelsHigh = pixelsHigh / 2;
	//	m_tContentSize = cocos2d::CCSize(pixelsWide / 2, pixelsHigh / 2);
	//	m_fMaxS = contentSize.width / (float)(pixelsWide * 2);
	//	m_fMaxT = contentSize.height / (float)(pixelsHigh * 2);
	//}

    return true;
}


const char* CCTexture2D::description(void)
{
    return CCString::createWithFormat("<CCTexture2D | Name = %u | Dimensions = %u x %u | Coordinates = (%.2f, %.2f)>", m_uName, m_uPixelsWide, m_uPixelsHigh, m_fMaxS, m_fMaxT)->getCString();
}

// implementation CCTexture2D (Image)

bool CCTexture2D::initWithImage(CCImage *uiImage)
{
    return initWithImage(uiImage, kCCResolutionUnknown);
}

bool CCTexture2D::initWithImage(CCImage * uiImage, ccResolutionType resolution)
{
    if (uiImage == NULL)
    {
        CCLOG("cocos2d: CCTexture2D. Can't create Texture. UIImage is nil");
        this->release();
        return false;
    }
    
    unsigned int imageWidth = uiImage->getWidth();
    unsigned int imageHeight = uiImage->getHeight();
    
    CCConfiguration *conf = CCConfiguration::sharedConfiguration();

    unsigned maxTextureSize = conf->getMaxTextureSize();
    if (imageWidth > maxTextureSize || imageHeight > maxTextureSize) 
    {
        CCLOG("cocos2d: WARNING: Image (%u x %u) is bigger than the supported %u x %u", imageWidth, imageHeight, maxTextureSize, maxTextureSize);
        this->release();
        return false;
    }

    m_eResolutionType = resolution;

    // always load premultiplied images
    return initPremultipliedATextureWithImage(uiImage, imageWidth, imageHeight);
}
bool CCTexture2D::initPremultipliedATextureWithImage(CCImage *image, unsigned int width, unsigned int height)
{
    unsigned char*            tempData = image->getData();
    unsigned int*             inPixel32 = NULL;
    unsigned char*            inPixel8 = NULL;
    unsigned short*           outPixel16 = NULL;
    bool                      hasAlpha = image->hasAlpha();
    CCSize                    imageSize = CCSizeMake((float)(image->getWidth()), (float)(image->getHeight()));
    CCTexture2DPixelFormat    pixelFormat;
    size_t                    bpp = image->getBitsPerComponent();

    // compute pixel format
    if(hasAlpha)
    {
        pixelFormat = g_defaultAlphaPixelFormat;
    }
    else
    {
        if (bpp >= 8)
        {
            pixelFormat = kCCTexture2DPixelFormat_RGB888;
        }
        else 
        {
            pixelFormat = kCCTexture2DPixelFormat_RGB565;
        }
        
    }
    
    // Repack the pixel data into the right format
    unsigned int length = width * height;
    
    if (pixelFormat == kCCTexture2DPixelFormat_RGB565)
    {
        if (hasAlpha)
        {
            // Convert "RRRRRRRRRGGGGGGGGBBBBBBBBAAAAAAAA" to "RRRRRGGGGGGBBBBB"
            
            tempData = new unsigned char[width * height * 2];
            outPixel16 = (unsigned short*)tempData;
            inPixel32 = (unsigned int*)image->getData();
            
            for(unsigned int i = 0; i < length; ++i, ++inPixel32)
            {
                *outPixel16++ = 
                ((((*inPixel32 >>  0) & 0xFF) >> 3) << 11) |  // R
                ((((*inPixel32 >>  8) & 0xFF) >> 2) << 5)  |  // G
                ((((*inPixel32 >> 16) & 0xFF) >> 3) << 0);    // B
            }
        }
        else 
        {
            // Convert "RRRRRRRRRGGGGGGGGBBBBBBBB" to "RRRRRGGGGGGBBBBB"
            
            tempData = new unsigned char[width * height * 2];
            outPixel16 = (unsigned short*)tempData;
            inPixel8 = (unsigned char*)image->getData();
            
            for(unsigned int i = 0; i < length; ++i)
            {
                *outPixel16++ = 
                (((*inPixel8++ & 0xFF) >> 3) << 11) |  // R
                (((*inPixel8++ & 0xFF) >> 2) << 5)  |  // G
                (((*inPixel8++ & 0xFF) >> 3) << 0);    // B
            }
        }    
    }
    else if (pixelFormat == kCCTexture2DPixelFormat_RGBA4444)
    {
        // Convert "RRRRRRRRRGGGGGGGGBBBBBBBBAAAAAAAA" to "RRRRGGGGBBBBAAAA"
        
        inPixel32 = (unsigned int*)image->getData();  
        tempData = new unsigned char[width * height * 2];
        outPixel16 = (unsigned short*)tempData;
        
        for(unsigned int i = 0; i < length; ++i, ++inPixel32)
        {
            *outPixel16++ = 
            ((((*inPixel32 >> 0) & 0xFF) >> 4) << 12) | // R
            ((((*inPixel32 >> 8) & 0xFF) >> 4) <<  8) | // G
            ((((*inPixel32 >> 16) & 0xFF) >> 4) << 4) | // B
            ((((*inPixel32 >> 24) & 0xFF) >> 4) << 0);  // A
        }
    }
    else if (pixelFormat == kCCTexture2DPixelFormat_RGB5A1)
    {
        // Convert "RRRRRRRRRGGGGGGGGBBBBBBBBAAAAAAAA" to "RRRRRGGGGGBBBBBA"
        inPixel32 = (unsigned int*)image->getData();   
        tempData = new unsigned char[width * height * 2];
        outPixel16 = (unsigned short*)tempData;
        
        for(unsigned int i = 0; i < length; ++i, ++inPixel32)
        {
            *outPixel16++ = 
            ((((*inPixel32 >> 0) & 0xFF) >> 3) << 11) | // R
            ((((*inPixel32 >> 8) & 0xFF) >> 3) <<  6) | // G
            ((((*inPixel32 >> 16) & 0xFF) >> 3) << 1) | // B
            ((((*inPixel32 >> 24) & 0xFF) >> 7) << 0);  // A
        }
    }
    else if (pixelFormat == kCCTexture2DPixelFormat_A8)
    {
        // Convert "RRRRRRRRRGGGGGGGGBBBBBBBBAAAAAAAA" to "AAAAAAAA"
        inPixel32 = (unsigned int*)image->getData();
        tempData = new unsigned char[width * height];
        unsigned char *outPixel8 = tempData;
        
        for(unsigned int i = 0; i < length; ++i, ++inPixel32)
        {
            *outPixel8++ = (*inPixel32 >> 24) & 0xFF;  // A
        }
    }
    
    if (hasAlpha && pixelFormat == kCCTexture2DPixelFormat_RGB888)
    {
        // Convert "RRRRRRRRRGGGGGGGGBBBBBBBBAAAAAAAA" to "RRRRRRRRGGGGGGGGBBBBBBBB"
        inPixel32 = (unsigned int*)image->getData();
        tempData = new unsigned char[width * height * 3];
        unsigned char *outPixel8 = tempData;
        
        for(unsigned int i = 0; i < length; ++i, ++inPixel32)
        {
            *outPixel8++ = (*inPixel32 >> 0) & 0xFF; // R
            *outPixel8++ = (*inPixel32 >> 8) & 0xFF; // G
            *outPixel8++ = (*inPixel32 >> 16) & 0xFF; // B
        }
    }
    
    initWithData(tempData, pixelFormat, width, height, imageSize);
    
    if (tempData != image->getData())
    {
        delete [] tempData;
    }

    m_bHasPremultipliedAlpha = image->isPremultipliedAlpha();
    return true;
}

// implementation CCTexture2D (Text)
bool CCTexture2D::initWithString(const char *text, const char *fontName, float fontSize)
{
    return initWithString(text, CCSizeMake(0,0), kCCTextAlignmentCenter, kCCVerticalTextAlignmentTop, fontName, fontSize);
}

bool CCTexture2D::initWithString(const char *text, const CCSize& dimensions, CCTextAlignment hAlignment, CCVerticalTextAlignment vAlignment, const char *fontName, float fontSize)
{
#if CC_ENABLE_CACHE_TEXTURE_DATA
    // cache the texture data
    VolatileTexture::addStringTexture(this, text, dimensions, hAlignment, vAlignment, fontName, fontSize);
#endif

    CCImage image;

    CCImage::ETextAlign eAlign;

    if (kCCVerticalTextAlignmentTop == vAlignment)
    {
        eAlign = (kCCTextAlignmentCenter == hAlignment) ? CCImage::kAlignTop
            : (kCCTextAlignmentLeft == hAlignment) ? CCImage::kAlignTopLeft : CCImage::kAlignTopRight;
    }
    else if (kCCVerticalTextAlignmentCenter == vAlignment)
    {
        eAlign = (kCCTextAlignmentCenter == hAlignment) ? CCImage::kAlignCenter
            : (kCCTextAlignmentLeft == hAlignment) ? CCImage::kAlignLeft : CCImage::kAlignRight;
    }
    else if (kCCVerticalTextAlignmentBottom == vAlignment)
    {
        eAlign = (kCCTextAlignmentCenter == hAlignment) ? CCImage::kAlignBottom
            : (kCCTextAlignmentLeft == hAlignment) ? CCImage::kAlignBottomLeft : CCImage::kAlignBottomRight;
    }
    else
    {
        CCAssert(false, "Not supported alignment format!");
    }
    
    if (!image.initWithString(text, (int)dimensions.width, (int)dimensions.height, eAlign, fontName, (int)fontSize))
    {
        return false;
    }

    return initWithImage(&image);
}


// implementation CCTexture2D (Drawing)

void CCTexture2D::drawAtPoint(const CCPoint& point)
{
    GLfloat    coordinates[] = {    
        0.0f,    m_fMaxT,
        m_fMaxS,m_fMaxT,
        0.0f,    0.0f,
        m_fMaxS,0.0f };

    GLfloat    width = (GLfloat)m_uPixelsWide * m_fMaxS,
        height = (GLfloat)m_uPixelsHigh * m_fMaxT;

    GLfloat        vertices[] = {    
        point.x,            point.y,
        width + point.x,    point.y,
        point.x,            height  + point.y,
        width + point.x,    height  + point.y };

    ccGLEnableVertexAttribs( kCCVertexAttribFlag_Position | kCCVertexAttribFlag_TexCoords );
    m_pShaderProgram->use();
    m_pShaderProgram->setUniformForModelViewProjectionMatrix();

    ccGLBindTexture2D( m_uName );


    glVertexAttribPointer(kCCVertexAttrib_Position, 2, GL_FLOAT, GL_FALSE, 0, vertices);
    glVertexAttribPointer(kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE, 0, coordinates);

    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

void CCTexture2D::drawInRect(const CCRect& rect)
{
    GLfloat    coordinates[] = {    
        0.0f,    m_fMaxT,
        m_fMaxS,m_fMaxT,
        0.0f,    0.0f,
        m_fMaxS,0.0f };

    GLfloat    vertices[] = {    rect.origin.x,        rect.origin.y,                            /*0.0f,*/
        rect.origin.x + rect.size.width,        rect.origin.y,                            /*0.0f,*/
        rect.origin.x,                            rect.origin.y + rect.size.height,        /*0.0f,*/
        rect.origin.x + rect.size.width,        rect.origin.y + rect.size.height,        /*0.0f*/ };

    ccGLEnableVertexAttribs( kCCVertexAttribFlag_Position | kCCVertexAttribFlag_TexCoords );
    m_pShaderProgram->use();
    m_pShaderProgram->setUniformForModelViewProjectionMatrix();

    ccGLBindTexture2D( m_uName );

    glVertexAttribPointer(kCCVertexAttrib_Position, 2, GL_FLOAT, GL_FALSE, 0, vertices);
    glVertexAttribPointer(kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE, 0, coordinates);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

#ifdef CC_SUPPORT_PVRTC
// implementation CCTexture2D (PVRTC);    
bool CCTexture2D::initWithPVRTCData(const void *data, int level, int bpp, bool hasAlpha, int length, CCTexture2DPixelFormat pixelFormat)
{
    if( !(CCConfiguration::sharedConfiguration()->supportsPVRTC()) )
    {
        CCLOG("cocos2d: WARNING: PVRTC images is not supported.");
        this->release();
        return false;
    }

    glGenTextures(1, &m_uName);
    glBindTexture(GL_TEXTURE_2D, m_uName);

    this->setAntiAliasTexParameters();

    GLenum format = 0;
    GLsizei size = length * length * bpp / 8;

#if( CC_PLATFORM_IOS == CC_TARGET_PLATFORM)
	if(hasAlpha) {
	    format = (bpp == 4) ? GL_COMPRESSED_RGBA_PVRTC_4BPPV1_IMG : GL_COMPRESSED_RGBA_PVRTC_2BPPV1_IMG;
	} else {
	    format = (bpp == 4) ? GL_COMPRESSED_RGB_PVRTC_4BPPV1_IMG : GL_COMPRESSED_RGB_PVRTC_2BPPV1_IMG;
	}
#endif

	
    if(size < 32) {
        size = 32;
    }
    glCompressedTexImage2D(GL_TEXTURE_2D, level, format, length, length, 0, size, data);

    m_tContentSize = CCSizeMake((float)(length), (float)(length));
    m_uPixelsWide = length;
    m_uPixelsHigh = length;
    m_fMaxS = 1.0f;
    m_fMaxT = 1.0f;
    m_bHasPremultipliedAlpha = PVRHaveAlphaPremultiplied_;
    m_ePixelFormat = pixelFormat;

    return true;
}


#endif // CC_SUPPORT_PVRTC

bool CCTexture2D::initWithDDSCompressData(const void *data, int level, int bpp, bool hasAlpha, int width, int height, CCTexture2DPixelFormat pixelFormat)
{
    if ( !(CCConfiguration::sharedConfiguration()->supportsNVS3TC()) ){
        CCLOG("cocos2d: WARNING: NVS3TC is not supported.");
        this->release();
        return false;
    }
    glGenTextures(1, &m_uName);
	cocos2d::ccGLBindTexture2D(m_uName);
        
    this->setAntiAliasTexParameters();
        
    GLenum format;
    GLsizei size = width * height * bpp / 8;
        
    //formats
    //RGB_S3TC_DXT1     0x83F0
    //RGBA_S3TC_DXT1    0x83F1
    //RGBA_S3TC_DXT3    0x83F2
    //RGBA_S3TC_DXT5    0x83F3
        
    switch (pixelFormat)
    {
        case kCCTexture2DPixelFormat_DXT1:
            format = 0x83F0;
            break;
        case kCCTexture2DPixelFormat_DXT1A:
            format = 0x83F1;
            break;
        case kCCTexture2DPixelFormat_DXT3:
            format = 0x83F2;
            break;
        case kCCTexture2DPixelFormat_DXT5:
            format = 0x83F3;
            break;
        default:
        {
            CCLOG("unsported dds format.");
            this->release();
            return false;
        }
    }
    
    if (kCCTexture2DPixelFormat_DXT1A == pixelFormat || kCCTexture2DPixelFormat_DXT1 == pixelFormat) {
        size = ((width + 3)/4) * ((height + 3)/4) * 8;
	}
	else{
		size = ((width + 3) / 4) * ((height + 3) / 4) * 16;
	}

//  LOGD("----pars:level:%d, format:%d, width:%d, height:%d, size:%d, error:%d", level, format, width, height, size, glGetError());
    glCompressedTexImage2D(GL_TEXTURE_2D, level, format, width, height, 0, size, data);
//  LOGD("----error:%d", glGetError());
    m_tContentSize = CCSizeMake((float)(width), (float)(height));
    m_uPixelsWide = width;
    m_uPixelsHigh = height;
    m_fMaxS = 1.0f;
    m_fMaxT = 1.0f;
    m_bHasPremultipliedAlpha = PVRHaveAlphaPremultiplied_;
    m_ePixelFormat = pixelFormat;

	if (data != NULL && pixelFormat == kCCTexture2DPixelFormat_DXT3 && pixelFormat == kCCTexture2DPixelFormat_DXT5 && CCImage::IsNormal() && m_uPixelsWide % 2 == 0 && m_uPixelsHigh % 2 == 0/* && iScale != 100*/)
	{
		GLint PixSize = 0;
		PixSize = m_uPixelsWide * m_uPixelsHigh;
		CharRGBA* uiB = (CharRGBA*)data;
		CharRGBA uiCurPix;

		GLint PixSizeNew = 0;
		PixSizeNew = (m_uPixelsWide / 2) * (m_uPixelsHigh / 2);
		GLvoid* pPixBuffer = NULL;
		GLint BufferSize = PixSizeNew * 4;
		pPixBuffer = (GLvoid*)malloc(BufferSize);
		memset(pPixBuffer, 0, BufferSize);
		CharRGBA* uiBNew = (CharRGBA*)pPixBuffer;

		for (int i = 0; i < m_uPixelsHigh / 2; i++)
		{
			for (int i2 = 0; i2 < m_uPixelsWide / 2; i2++)
			{
				uiBNew[i * m_uPixelsWide / 2 + i2] = uiB[i2 * 2 + i * 2 * m_uPixelsWide];
				//uiBNew[i * m_nWidth / 2 + i2] = uiCurPixNew;
			}
		}

		m_uPixelsWide /= 2;
		m_uPixelsHigh /= 2;

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)m_uPixelsWide, (GLsizei)m_uPixelsHigh, 0, GL_RGBA, GL_UNSIGNED_BYTE, pPixBuffer);
		CC_SAFE_DELETE_ARRAY(pPixBuffer);

	}
        
    return true;
}

 bool CCTexture2D::initWithATCData(const void *data, int level, int bpp, bool hasAlpha, int width, int height, CCTexture2DPixelFormat pixelFormat)
 {
     if( !(CCConfiguration::sharedConfiguration()->supportsATC()) )
     {
     CCLOG("cocos2d: WARNING: ATC images is not supported.");
     this->release();
     return false;
     }
     
     glGenTextures(1, &m_uName);
     glBindTexture(GL_TEXTURE_2D, m_uName);
     
     this->setAntiAliasTexParameters();
     
     GLenum format;
     GLsizei size = width * height * bpp / 8;
//     if(hasAlpha) {
//     format = (bpp == 4) ? GL_COMPRESSED_RGBA_PVRTC_4BPPV1_IMG : GL_COMPRESSED_RGBA_PVRTC_2BPPV1_IMG;
//     } else {
//     format = (bpp == 4) ? GL_COMPRESSED_RGB_PVRTC_4BPPV1_IMG : GL_COMPRESSED_RGB_PVRTC_2BPPV1_IMG;
//     }
     
     if (pixelFormat == kCCTexture2DPixelFormat_ATC_Explicit) {
         format = 0x8C93;
     }else if (kCCTexture2DPixelFormat_ATC_Interpolated == pixelFormat)
     {
         format = 0x87EE;
     }else
     {
         CCLOG("unsupported atc format");
     }
     if(size < 32) {
     size = 32;
     }
     glCompressedTexImage2D(GL_TEXTURE_2D, level, format, width, height, 0, size, data);
     
     m_tContentSize = CCSizeMake((float)(width), (float)(height));
     m_uPixelsWide = width;
     m_uPixelsHigh = height;
     m_fMaxS = 1.0f;
     m_fMaxT = 1.0f;
     m_bHasPremultipliedAlpha = PVRHaveAlphaPremultiplied_;
     m_ePixelFormat = pixelFormat;
 
 return true;
 }
 

bool CCTexture2D::initWithPVRFile(const char* file)
{
    bool bRet = false;
    // nothing to do with CCObject::init
    
    CCTexturePVR *pvr = new CCTexturePVR;
    bRet = pvr->initWithContentsOfFile(file);
        
    if (bRet)
    {
        pvr->setRetainName(true); // don't dealloc texture on release
        
        m_uName = pvr->getName();
        m_fMaxS = 1.0f;
        m_fMaxT = 1.0f;
        m_uPixelsWide = pvr->getWidth();
        m_uPixelsHigh = pvr->getHeight();
        m_tContentSize = CCSizeMake((float)m_uPixelsWide, (float)m_uPixelsHigh);
        m_bHasPremultipliedAlpha = PVRHaveAlphaPremultiplied_;
        m_ePixelFormat = pvr->getFormat();
        m_bHasMipmaps = pvr->getNumberOfMipmaps() > 1;       

        pvr->release();
    }
    else
    {
        CCLOG("cocos2d: Couldn't load PVR image %s", file);
    }

    return bRet;
}

bool CCTexture2D::initWithETCData(const void *data, int level, int bpp, bool hasAlpha, int width, int height, CCTexture2DPixelFormat pixelFormat)
{
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
    if( !(CCConfiguration::sharedConfiguration()->supportsETC1()))
    {
        CCLOG("cocos2d: WARNING: PVRTC images is not supported.");
        this->release();
        return false;
    }
//    LOGD("--------------------------------------etc");
    m_bIsEtcTexture = true;
    
//rgb part
    glGenTextures(1, &m_uName);
    glBindTexture(GL_TEXTURE_2D, m_uName);
    
    this->setAntiAliasTexParameters();
    
    GLenum format;
//    GLsizei size = width * height * bpp / 8;
    format = 0x8D64;
    unsigned char* pVal = (unsigned char*)data;
    MaliSDK::ETCHeader etcheader = MaliSDK::ETCHeader(pVal);
    glCompressedTexImage2D(GL_TEXTURE_2D, level, format, etcheader.getWidth(), etcheader.getHeight(), 0, (etcheader.getPaddedWidth() * etcheader.getPaddedHeight()) >> 1, pVal + 16);
    
//alpha part
    if (CCConfiguration::sharedConfiguration()->getMaxTextureUnits() >= 2) {
        glPixelStorei(GL_UNPACK_ALIGNMENT,1);
        
        glGenTextures(1, &m_etcAlphaChannel);
        ccGLBindTexture2D(m_etcAlphaChannel);
        
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );
        
        unsigned char* pAlphaVal = (unsigned char*)pVal + 16 + ((etcheader.getPaddedWidth() * etcheader.getPaddedHeight()) >> 1);
        MaliSDK::ETCHeader etcAlphaHeader = MaliSDK::ETCHeader(pAlphaVal);
        
        glCompressedTexImage2D(GL_TEXTURE_2D, level, format, etcAlphaHeader.getWidth(), etcAlphaHeader.getHeight(), 0, (etcAlphaHeader.getPaddedWidth() * etcAlphaHeader.getPaddedHeight()) >> 1, pAlphaVal + 16);
    }
    
    m_tContentSize = CCSizeMake((float)(etcheader.getWidth()), (float)(etcheader.getHeight()));
    m_uPixelsWide = etcheader.getWidth();
    m_uPixelsHigh = etcheader.getHeight();
    m_fMaxS = 1.0f;
    m_fMaxT = 1.0f;
    m_bHasPremultipliedAlpha = PVRHaveAlphaPremultiplied_;
    m_ePixelFormat = pixelFormat;
#endif
    return true;
}

void CCTexture2D::PVRImagesHavePremultipliedAlpha(bool haveAlphaPremultiplied)
{
    PVRHaveAlphaPremultiplied_ = haveAlphaPremultiplied;
}

    
//
// Use to apply MIN/MAG filter
//
// implementation CCTexture2D (GLFilter)

void CCTexture2D::generateMipmap()
{
    CCAssert( m_uPixelsWide == ccNextPOT(m_uPixelsWide) && m_uPixelsHigh == ccNextPOT(m_uPixelsHigh), "Mimpap texture only works in POT textures");
    ccGLBindTexture2D( m_uName );
    glGenerateMipmap(GL_TEXTURE_2D);
    m_bHasMipmaps = true;
}

bool CCTexture2D::hasMipmaps()
{
    return m_bHasMipmaps;
}

void CCTexture2D::setTexParameters(ccTexParams *texParams)
{
    CCAssert( (m_uPixelsWide == ccNextPOT(m_uPixelsWide) || texParams->wrapS == GL_CLAMP_TO_EDGE) &&
        (m_uPixelsHigh == ccNextPOT(m_uPixelsHigh) || texParams->wrapT == GL_CLAMP_TO_EDGE),
        "GL_CLAMP_TO_EDGE should be used in NPOT dimensions");

    ccGLBindTexture2D( m_uName );
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texParams->minFilter );
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, texParams->magFilter );
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, texParams->wrapS );
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, texParams->wrapT );
}

void CCTexture2D::setAliasTexParameters()
{
    ccGLBindTexture2D( m_uName );

    if( ! m_bHasMipmaps )
    {
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    }
    else
    {
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST );
    }

    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
}

void CCTexture2D::setAntiAliasTexParameters()
{
    ccGLBindTexture2D( m_uName );

    if( ! m_bHasMipmaps )
    {
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    }
    else
    {
        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST );
    }

    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
}

const char* CCTexture2D::stringForFormat()
{
	switch (m_ePixelFormat) 
	{
		case kCCTexture2DPixelFormat_RGBA8888:
			return  "RGBA8888";

		case kCCTexture2DPixelFormat_RGB888:
			return  "RGB888";

		case kCCTexture2DPixelFormat_RGB565:
			return  "RGB565";

		case kCCTexture2DPixelFormat_RGBA4444:
			return  "RGBA4444";

		case kCCTexture2DPixelFormat_RGB5A1:
			return  "RGB5A1";

		case kCCTexture2DPixelFormat_AI88:
			return  "AI88";

		case kCCTexture2DPixelFormat_A8:
			return  "A8";

		case kCCTexture2DPixelFormat_I8:
			return  "I8";

		case kCCTexture2DPixelFormat_PVRTC4:
			return  "PVRTC4";

		case kCCTexture2DPixelFormat_PVRTC2:
			return  "PVRTC2";

		default:
			CCAssert(false , "unrecognised pixel format");
			CCLOG("stringForFormat: %ld, cannot give useful result", (long)m_ePixelFormat);
			break;
	}

	return  NULL;
}


//
// Texture options for images that contains alpha
//
// implementation CCTexture2D (PixelFormat)

void CCTexture2D::setDefaultAlphaPixelFormat(CCTexture2DPixelFormat format)
{
    g_defaultAlphaPixelFormat = format;
}


CCTexture2DPixelFormat CCTexture2D::defaultAlphaPixelFormat()
{
    return g_defaultAlphaPixelFormat;
}

unsigned int CCTexture2D::bitsPerPixelForFormat(CCTexture2DPixelFormat format)
{
	unsigned int ret=0;

	switch (format) {
		case kCCTexture2DPixelFormat_RGBA8888:
			ret = 32;
			break;
		case kCCTexture2DPixelFormat_RGB888:
			// It is 32 and not 24, since its internal representation uses 32 bits.
			ret = 32;
			break;
		case kCCTexture2DPixelFormat_RGB565:
			ret = 16;
			break;
		case kCCTexture2DPixelFormat_RGBA4444:
			ret = 16;
			break;
		case kCCTexture2DPixelFormat_RGB5A1:
			ret = 16;
			break;
		case kCCTexture2DPixelFormat_AI88:
			ret = 16;
			break;
		case kCCTexture2DPixelFormat_A8:
			ret = 8;
			break;
		case kCCTexture2DPixelFormat_I8:
			ret = 8;
			break;
		case kCCTexture2DPixelFormat_PVRTC4:
			ret = 4;
			break;
		case kCCTexture2DPixelFormat_PVRTC2:
			ret = 2;
			break;
		default:
			ret = -1;
			CCAssert(false , "unrecognised pixel format");
			CCLOG("bitsPerPixelForFormat: %ld, cannot give useful result", (long)format);
			break;
	}
	return ret;
}

unsigned int CCTexture2D::bitsPerPixelForFormat()
{
	return this->bitsPerPixelForFormat(m_ePixelFormat);
}


NS_CC_END
