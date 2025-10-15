#define NOMINMAX
#include "CEGUICocos2DTexture.h"

#include "CEGUIExceptions.h"
#include "CEGUISystem.h"
#include "CEGUIImageCodec.h"
#include <CEGUIDefaultResourceProvider.h>
#include <CCGL.h>
#include <shaders/ccGLStateCache.h>
#include "CEGUIResLoadThread.h"
#include "CEGUIImageset.h"

#if (defined WIN7_32) && (defined _DEBUG)
#include "CEGUIPfsResourceProvider.h"
#endif

#ifdef PUBLISHED_VERSION
#include "Nuclear.h"
//#include "cocos2d_render.h"
#endif

using cocos2d::CCTexture2D;
using cocos2d::CCTextureCache;

namespace CEGUI
{
Cocos2DTexture::Cocos2DTexture(Cocos2DRenderer& owner) :
    d_owner(owner),
    d_texture(0),
    d_size(0, 0),
    d_dataSize(0, 0),
    d_texelScaling(0, 0),
    d_savedSurfaceDescValid(false),
    m_bIsLoading(false),
    m_bIsLoadFromFile(false),
    d_pImageset(NULL)
{
    
}

Cocos2DTexture::Cocos2DTexture(Cocos2DRenderer& owner,
                                   const String& filename,
                                   const String& resourceGroup) :
    d_owner(owner),
    d_texture(0),
    d_size(0, 0),
    d_dataSize(0, 0),
    d_texelScaling(0, 0),
    d_savedSurfaceDescValid(false),
    d_pImageset(NULL),
    d_filename(filename)
{
    m_bIsLoadFromFile = true;
    //loadFromFile(filename, resourceGroup);
}

Cocos2DTexture::Cocos2DTexture(Cocos2DRenderer& owner, const Size& sz) :
    d_owner(owner),
    d_texture(0),
    d_size(0, 0),
    d_dataSize(sz),
    d_texelScaling(0, 0),
    d_savedSurfaceDescValid(false),
    d_pImageset(NULL)
{
	assert("not impelemented!" && false);
    /*Size tex_sz(d_owner.getAdjustedSize(sz));

    HRESULT hr = D3DXCreateTexture(d_owner.getDevice(),
                                   static_cast<UINT>(tex_sz.d_width),
                                   static_cast<UINT>(tex_sz.d_height),
                                   1, 0, D3DFMT_A8R8G8B8, D3DPOOL_MANAGED,
                                   &d_texture);

    if (FAILED(hr))
        throw RendererException(
            "Cocos2DTexture - Failed to create texture of specified size: "
            "D3D Texture creation failed.");

    updateTextureSize();
    updateCachedScaleValues();*/
}

Cocos2DTexture::Cocos2DTexture(Cocos2DRenderer& owner,
                                   cocos2d::CCTexture2D* tex) :
    d_owner(owner),
    d_texture(0),
    d_size(0, 0),
    d_dataSize(0, 0),
    d_texelScaling(0, 0),
    d_savedSurfaceDescValid(false),
    d_pImageset(NULL)
{
    setCocos2DTexture(tex);
    m_bIsLoadFromFile = ((Cocos2DTexture*)tex)->m_bIsLoadFromFile;
}

Cocos2DTexture::~Cocos2DTexture()
{
    cleanupCocos2DTexture();
    if(d_pImageset)
    {
        d_pImageset->clearTexture();
        d_pImageset = NULL;
    }
	else
	{
		ImagesetManager::getSingleton().notifyTextureReleased(this);
	}
}

GLuint Cocos2DTexture::getTextureName() const
{
    return d_texture->getName();
}

String Cocos2DTexture::getFileName() const
{
	return d_filename;
}

const Size& Cocos2DTexture::getSize() const
{
    return d_size;
}

const Size& Cocos2DTexture::getOriginalDataSize() const
{
    return d_dataSize;
}

const Vector2& Cocos2DTexture::getTexelScaling() const
{
    return d_texelScaling;
}
    
void Cocos2DTexture::loadFromFile(const String& filename,
                                  const String& resourceGroup)
{        
#if (defined WIN7_32) && (defined _DEBUG)
	std::wstring strFileName = PFSResourceProvider::GUIStringToWString(filename);
#endif
	// get and check existence of CEGUI::System (needed to access ImageCodec)
    System* sys = System::getSingletonPtr();
    if (!sys)
        CEGUI_THROW(RendererException("OpenGLTexture::loadFromFile - "
                                      "CEGUI::System object has not been created: "
                                      "unable to access ImageCodec."));
    timeval t;
    gettimeofday((struct timeval *)&t,  0);
    
    // CEGUIResLoadThread threadd; // yeqing 2015-10-19 此处代码没有作用，注释掉
#ifdef PUBLISHED_VERSION
	LJFM::LJFMID texFile;
#else
	CEGUI::RawDataContainer texFile;
#endif
    System::getSingleton().getResourceProvider()->loadRawDataContainer(filename, texFile, resourceGroup);
    timeval t2;
    gettimeofday((struct timeval *)&t2,  0);
    
    printf("tick-loadfile:%ld\n", t2.tv_usec - t.tv_usec);
#ifdef PUBLISHED_VERSION
    CodecPrivateData* pData = NULL;
    Texture* res = sys->getImageCodec().load(texFile, this, &pData);
    delete pData;
#else
	Texture* res = sys->getImageCodec().load(texFile, this);
#endif

    if (!res)
        // It's an error
        CEGUI_THROW(RendererException("OpenGLTexture::loadFromFile - " +
                                      sys->getImageCodec().getIdentifierString() +
                                      " failed to load image '" + filename + "'."));
}
    
#ifdef _MSC_VER
#pragma pack(push,1)
#endif
    typedef struct {
        uint32_t version;
        uint32_t flags;
        uint64_t pixelFormat;
        uint32_t colorSpace;
        uint32_t channelType;
        uint32_t height;
        uint32_t width;
        uint32_t depth;
        uint32_t numberOfSurfaces;
        uint32_t numberOfFaces;
        uint32_t numberOfMipmaps;
        uint32_t metadataLength;
#ifdef _MSC_VER
    } ccPVRv3TexHeader;
#pragma pack(pop)
#else
} __attribute__((packed)) ccPVRv3TexHeader;
#endif

void Cocos2DTexture::loadFromBuffer(const void* buffer, const Size& buffer_size, PixelFormat pixel_format)
    {
        cleanupCocos2DTexture();
        
        if(d_texture != NULL)
        {
            delete d_texture;
        }
        
        cocos2d::CCTexture2DPixelFormat pixfmt;
        switch (pixel_format)
        {
            case PF_RGB:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_RGB888;
                break;
            case PF_RGBA:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_RGBA8888;
                break;
            case PF_PVR2:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_PVRTC2;
                break;
            case PF_PVR4:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_PVRTC4;
                break;
            case PF_ATC_Exp:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_ATC_Explicit;
                break;
            case PF_ATC_Int:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_ATC_Interpolated;
                break;
            case PF_DXT3:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_DXT3;
                break;
            case PF_DXT5:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_DXT5;
                break;
            case PF_ETC:
                pixfmt = cocos2d::kCCTexture2DPixelFormat_ETC;
                break;
            default:
                throw RendererException("Cocos2DTexture::loadFromMemory failed: "
                                        "Invalid PixelFormat value specified.");
        }
        
        d_texture = new CCTexture2D();
        
        bool bRet = false;

        if (pixel_format == PF_PVR2 || pixel_format == PF_PVR4)
        {
            ccPVRv3TexHeader *header = (ccPVRv3TexHeader *)buffer;
            bRet = d_texture->initWithPVRTCData(((unsigned char *)buffer)+(sizeof(ccPVRv3TexHeader) + header->metadataLength) , 0, (pixel_format == PF_PVR2)?2:4, true, buffer_size.d_width, pixfmt);
		}
		else if(pixel_format == PF_ATC_Exp || pixel_format == PF_ATC_Int)
        {
            bRet = d_texture->initWithATCData(((unsigned char *)buffer) + 4*sizeof(unsigned int), 0, 8, true, buffer_size.d_width, buffer_size.d_height, pixfmt);
        }
		else if(pixel_format == PF_DXT3 || pixel_format == PF_DXT5)
        {
            bRet = d_texture->initWithDDSCompressData(((unsigned char*)buffer) + 128, 0, 8, true, buffer_size.d_width, buffer_size.d_height, pixfmt);
        }
		else if(pixel_format == PF_ETC)
        {
            unsigned int* pVal = (unsigned int*)buffer;
            //pVal[7];//alpha size
            //pVal[8];//buffer size
           bRet = d_texture->initWithETCData(((unsigned char *)buffer), 0, 8, true, buffer_size.d_width, buffer_size.d_height, cocos2d::kCCTexture2DPixelFormat_ETC);
        }
		else
        {
			int iScale = 100;
			if (!cocos2d::CCImage::IsNormal())
			{
				iScale = 50;
			}
			bRet = d_texture->initWithData(buffer, pixfmt, buffer_size.d_width, buffer_size.d_height, cocos2d::CCSize(buffer_size.d_width, buffer_size.d_height), iScale);
        }

        if(!bRet)
        {
            delete d_texture;
            d_texture = NULL;
            return;
        }
        
        d_dataSize = buffer_size;
        updateTextureSize();
        updateCachedScaleValues();
    }

void Cocos2DTexture::loadFromMemory(const void* buffer,
                                      const Size& buffer_size,
                                      PixelFormat pixel_format, bool async )
{
    if (async) {
        return;
    }
    cleanupCocos2DTexture();

	if(d_texture != NULL)
	{
		delete d_texture;
	}

	cocos2d::CCTexture2DPixelFormat pixfmt;
    switch (pixel_format)
    {
    case PF_RGB:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_RGB888;
        break;
    case PF_RGBA:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_RGBA8888;
        break;
    case PF_PVR2:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_PVRTC2;
        break;
    case PF_PVR4:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_PVRTC4;
        break;
    case PF_ATC_Exp:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_ATC_Explicit;
        break;
    case PF_ATC_Int:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_ATC_Interpolated;
        break;
    case PF_DXT3:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_DXT3;
        break;
    case PF_DXT5:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_DXT5;
        break;
    case PF_ETC:
        pixfmt = cocos2d::kCCTexture2DPixelFormat_ETC;
        break;
    default:
        throw RendererException("Cocos2DTexture::loadFromMemory failed: "
                                "Invalid PixelFormat value specified.");
    }
    
	d_texture = new CCTexture2D();

    if (pixel_format == PF_PVR2 || pixel_format == PF_PVR4)
    {
        d_texture->initWithPVRTCData(((unsigned char *)buffer), 0, (pixel_format == PF_PVR2)?2:4, true, buffer_size.d_width, pixfmt);
	}
	else if(pixel_format == PF_ATC_Exp || pixel_format == PF_ATC_Int)
    {
        d_texture->initWithATCData(((unsigned char *)buffer) + 4*sizeof(unsigned int), 0, 8, true, buffer_size.d_width, buffer_size.d_height, pixfmt);
    }
	else if(pixel_format == PF_DXT3 || pixel_format == PF_DXT5)
    {
        d_texture->initWithDDSCompressData(((unsigned char*)buffer) + 128, 0, 8, true, buffer_size.d_width, buffer_size.d_height, pixfmt);
    }
	else if(pixel_format == PF_ETC)
    {
        unsigned int* pVal = (unsigned int*)buffer;
        //pVal[7];//alpha size
        //pVal[8];//buffer size
        d_texture->initWithETCData(((unsigned char *)buffer), 0, 8, true, buffer_size.d_width, buffer_size.d_height, cocos2d::kCCTexture2DPixelFormat_ETC);
    }
	else
    {
        d_texture->initWithData(buffer, pixfmt, buffer_size.d_width, buffer_size.d_height, cocos2d::CCSize(buffer_size.d_width, buffer_size.d_height));
    }

//#if CC_ENABLE_CACHE_TEXTURE_DATA
//    void* buffer2 = const_cast<void*>(buffer);
//    const cocos2d::CCSize& s = d_texture->getContentSizeInPixels();
//    cocos2d::VolatileTexture::addDataTexture(d_texture, buffer2, pixfmt, s);    
//#endif
	d_dataSize = buffer_size;
	updateTextureSize();
	updateCachedScaleValues();
}

void Cocos2DTexture::updataFromMemory(const void* buffer,
									  const Size& buffer_size,
									  const Rect& srcRect,
									  PixelFormat pixel_format)
{
	if (!d_texture)
	{
		uchar* argbBuffer = (uchar*)malloc(sizeof(argb_t)*buffer_size.d_width*buffer_size.d_height);
		int index = 0;
		for (int i = 0; i < buffer_size.d_height; ++i)
		{
			for (int j = 0; j < buffer_size.d_width; ++j)
			{
				argbBuffer[index+0] = 0xff;
				argbBuffer[index+1] = 0xff;
				argbBuffer[index+2] = 0xff;
				argbBuffer[index+3] = ((uchar*)buffer)[index/4];
				index+=4;
			}
		}
		loadFromMemory(argbBuffer,buffer_size,pixel_format, false);
		free(argbBuffer);
		return;
	}
    
    glPixelStorei(GL_UNPACK_ALIGNMENT,4);  
	cocos2d::ccGLActiveTexture(GL_TEXTURE0);
    cocos2d::ccGLBindTexture2D(d_texture->getName());
    //glBindTexture(GL_TEXTURE_2D, d_texture->getName());
    
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );
    
    //copy buffer content
    unsigned char* buf = NULL;
    size_t iStride = 0;
    switch (pixel_format) {
        case Texture::PF_RGBA:
            iStride = 4;
            break;
        case Texture::PF_RGB:
            iStride = 3;
            break;
    }
    buf = new unsigned char[(size_t)(srcRect.getWidth() * srcRect.getHeight() * iStride)];
    memset(buf, 0, srcRect.getWidth() * srcRect.getHeight() * iStride);
    
    size_t iRowSize = iStride * (size_t)srcRect.getWidth();
    for (int row = srcRect.d_top; row < srcRect.d_bottom; row ++) 
	{
		unsigned char* srcPos = ((unsigned char*)buffer) + (size_t)(srcRect.d_left) + (size_t)(row * buffer_size.d_width);
		unsigned char* dstPos = buf + (size_t)((row - srcRect.d_top) * iRowSize);
		for (int col = 0; col < iRowSize; col+=iStride)
		{
			dstPos[col] = 0xff;
			dstPos[col + 1] = 0xff;
			dstPos[col + 2] = 0xff;
			dstPos[col + 3] = srcPos[col/iStride];
		}
        //memcpy(buf + (size_t)((row - srcRect.d_top) * iRowSize), ((unsigned char*)buffer) + (size_t)(srcRect.d_left*iStride) + (size_t)(row * buffer_size.d_width * iStride), iRowSize);
    }
	//show buf
	/*
	std::stringstream ss;
	OutputDebugStringA("cur texture buf: \n");
	for (uint i = 0; i < (size_t)srcRect.getHeight(); i++) {
		ss.str("");
		for (uint j = 0; j < (size_t)srcRect.getWidth(); j++) {
			char d = buf[(i*(size_t)srcRect.getWidth() + j + 1)*iStride - 1];
			if (d > 0) {
				ss << "* ";
			} else {
				ss << ". ";
			}
		}
		ss << "\n";
		OutputDebugStringA(ss.str().c_str());
	}*/
    // Specify OpenGL texture image
    
    switch(pixel_format)
    {
        case PF_RGBA:
            glTexSubImage2D(GL_TEXTURE_2D, 0, (size_t)srcRect.d_left, (size_t)srcRect.d_top, (size_t)srcRect.getWidth(), (size_t)srcRect.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buf);
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
            break;
        case PF_RGB:
            glTexSubImage2D(GL_TEXTURE_2D, 0, (size_t)srcRect.d_left, (size_t)srcRect.d_top, (size_t)srcRect.getWidth(), (size_t)srcRect.getHeight(), GL_RGB, GL_UNSIGNED_BYTE, buf);
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, (GLsizei)pixelsWide, (GLsizei)pixelsHigh, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
            break;
       
        default:
            throw RendererException("Cocos2DTexture::updateFromMemory failed: "
                                    "Invalid PixelFormat value specified.");            
    }
    delete[] buf;
}

//----------------------------------------------------------------------------//
void Cocos2DTexture::saveToMemory(void* buffer)
{
    // TODO:
    throw RendererException("Cocos2DTexture::saveToMemory - Unimplemented!");
}

//----------------------------------------------------------------------------//
void Cocos2DTexture::cleanupCocos2DTexture()
{
    if (d_texture)
    {
        d_texture->release();
        d_texture = 0;
    }
}

//----------------------------------------------------------------------------//
void Cocos2DTexture::updateCachedScaleValues()
{
    //
    // calculate what to use for x scale
    //
    const float orgW = d_dataSize.d_width;
    const float texW = d_size.d_width;

    // if texture and original data width are the same, scale is based
    // on the original size.
    // if texture is wider (and source data was not stretched), scale
    // is based on the size of the resulting texture.
    d_texelScaling.d_x = 1.0f / ((orgW == texW) ? orgW : texW);

    //
    // calculate what to use for y scale
    //
    const float orgH = d_dataSize.d_height;
    const float texH = d_size.d_height;

    // if texture and original data height are the same, scale is based
    // on the original size.
    // if texture is taller (and source data was not stretched), scale
    // is based on the size of the resulting texture.
    d_texelScaling.d_y = 1.0f / ((orgH == texH) ? orgH : texH);
}

void Cocos2DTexture::updateTextureSize()
{
	if (d_texture)
	{
		d_size.d_width = d_texture->getPixelsWide();
		d_size.d_height = d_texture->getPixelsHigh();
	}
    // use the original size if query failed.
    // NB: This should probably be an exception.
    else
        d_size = d_dataSize;
}

//----------------------------------------------------------------------------//
void Cocos2DTexture::setOriginalDataSize(const Size& sz)
{
    d_dataSize = sz;
    updateCachedScaleValues();
}

void Cocos2DTexture::setCocos2DTexture(cocos2d::CCTexture2D* tex)
{
	if (d_texture != tex)
	{
		cleanupCocos2DTexture();
		d_dataSize.d_width = d_dataSize.d_height = 0;

		d_texture = tex;
		if (d_texture)
			d_texture->retain();
	}

	updateTextureSize();
	d_dataSize = d_size;
	updateCachedScaleValues();
}

//----------------------------------------------------------------------------//
void Cocos2DTexture::preD3DReset()
{
    // if already saved surface info, or we have no texture, do nothing
    if (d_savedSurfaceDescValid || !d_texture)
        return;

    // get info about our texture
    //d_texture->GetLevelDesc(0, &d_savedSurfaceDesc);

    // if texture is managed, we have nothing more to do
    //if (d_savedSurfaceDesc.Pool == D3DPOOL_MANAGED)
    //    return;

    // otherwise release texture.
    //d_texture->Release();
    //d_texture = 0;
    //d_savedSurfaceDescValid = true;
}

//----------------------------------------------------------------------------//
void Cocos2DTexture::postD3DReset()
{
    // if texture has no saved surface info, we do nothing.
    if (!d_savedSurfaceDescValid)
        return;

    // otherwise, create a new texture using saved details.
    //d_owner.getDevice()->
     //   CreateTexture(d_savedSurfaceDesc.Width,
      //                d_savedSurfaceDesc.Height,
      //                1, d_savedSurfaceDesc.Usage, d_savedSurfaceDesc.Format,
      //                d_savedSurfaceDesc.Pool, &d_texture, 0);

    d_savedSurfaceDescValid = false;
}

//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
