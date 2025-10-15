/***********************************************************************
	filename: 	CEGUISillyImageCodec.cpp
	created:	Thu Jun 15 2006
	author:		Tomas Lindquist Olsen

	purpose:	This codec provides SILLY based image loading
*************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2006 Paul D Turner & The CEGUI Development Team
 *
 *   Permission is hereby granted, free of charge, to any person obtaining
 *   a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including
 *   without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to
 *   permit persons to whom the Software is furnished to do so, subject to
 *   the following conditions:
 *
 *   The above copyright notice and this permission notice shall be
 *   included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *   OTHER DEALINGS IN THE SOFTWARE.
 ***************************************************************************/
#include "CEGUIExceptions.h"
#include "CEGUISILLYImageCodec.h"
#include <SILLY.h>
#include "CEGUILogger.h"
#include "CEGUISize.h"
#ifndef FORCEGUIEDITOR
#if !(defined WIN7_32) && !(defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <sys/time.h>
#include "ETCHeader.h"
#endif
#endif

#ifndef FORCEGUIEDITOR
#include <support/image_support/OgreDDSCodec.h>
namespace cocos2d{
    extern bool isCompressed(PixelFormat pf);}
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
	SILLYImageCodec::SILLYImageCodec()
		: ImageCodec("SILLYImageCodec - Official SILLY based image codec")
	{
		d_supportedFormat = "tga jpg png";
		if (! SILLY::SILLYInit())
			throw GenericException("SILLYImageCodec::SILLYImageCodec - Unable to initialize SILLY library");
	}

	SILLYImageCodec::~SILLYImageCodec()
	{
		SILLY::SILLYCleanup();
	}
    
	Texture* SILLYImageCodec::load(const RawDataContainer& data, Texture* result,bool bSyn)
	{
#ifndef FORCEGUIEDITOR
		timeval t;
		gettimeofday((struct timeval *)&t,  0);
#endif
		SILLY::MemoryDataSource md(static_cast<const SILLY::byte*>(data.getDataPtr()), data.getSize());
		SILLY::Image img(md);
		if (!img.loadImageHeader())
		{
			Logger::getSingletonPtr()->logEvent("SILLYImageCodec::load - Invalid image header", Errors);
			return 0;
		}

		SILLY::PixelFormat dstfmt;
		Texture::PixelFormat cefmt;
		switch (img.getSourcePixelFormat())
		{
		case SILLY::PF_RGB:
			dstfmt = SILLY::PF_RGB;
			cefmt = Texture::PF_RGB;
			break;
		case SILLY::PF_RGBA:
		case SILLY::PF_A1B5G5R5:
			dstfmt = SILLY::PF_RGBA;
			cefmt = Texture::PF_RGBA;
			break;
		default:
			Logger::getSingletonPtr()->logEvent("SILLYImageCodec::load - Unsupported pixel format", Errors);
			return 0;
		}

		if (!img.loadImageData(dstfmt, SILLY::PO_TOP_LEFT))
		{ 
			Logger::getSingletonPtr()->logEvent("SILLYImageCodec::load - Invalid image data", Errors);
			return 0;
		}

		result->loadFromMemory(img.getPixelsDataPtr(), Size(img.getWidth(), img.getHeight()), cefmt, bSyn);
#ifndef FORCEGUIEDITOR
		timeval t2;
		gettimeofday((struct timeval *)&t2,  0);
    
		printf("tick:%ld\n", t2.tv_usec - t.tv_usec);
#endif
		return result;
	}

	class CodecPrivateDataSILLY : public CodecPrivateData
    {       
    public:
		CodecPrivateDataSILLY()
			: m_pMd(NULL)
			, mpPixels(NULL)
		{
		}

        virtual ~CodecPrivateDataSILLY()
        {
			if (mpPixels)
			{
				free(mpPixels);
			}
            delete m_pMd;
        }
        virtual void* GetDataPtr()
        {
			return mpPixels;
        }
        virtual int  GetWidth()
        {
			return m_width;
        }
        virtual int  GetHeight()
        {
			return m_height;
        }
        virtual Texture::PixelFormat GetFmt()
        {
            return m_fmt;
        }

        SILLY::MemoryDataSource* m_pMd;
		void* mpPixels;
        Texture::PixelFormat m_fmt;
        int m_width, m_height;
    };
    
    typedef struct _PVRTexHeader
    {
        unsigned int headerLength;
        unsigned int height;
        unsigned int width;
        unsigned int numMipmaps;
        unsigned int flags;
        unsigned int dataLength;
        unsigned int bpp;
        unsigned int bitmaskRed;
        unsigned int bitmaskGreen;
        unsigned int bitmaskBlue;
        unsigned int bitmaskAlpha;
        unsigned int pvrTag;
        unsigned int numSurfs;
    } PVRTexHeader;
    static char gPVRTexIdentifier[5] = "PVR!";
#define CC_HOST_IS_BIG_ENDIAN (bool)(*(unsigned short *)"\0\xff" < 0x100)
#define CC_SWAP32(i)  ((i & 0x000000ff) << 24 | (i & 0x0000ff00) << 8 | (i & 0x00ff0000) >> 8 | (i & 0xff000000) >> 24)
#define CC_SWAP16(i)  ((i & 0x00ff) << 8 | (i &0xff00) >> 8)
#define CC_SWAP_INT32_LITTLE_TO_HOST(i) ((CC_HOST_IS_BIG_ENDIAN == true)? CC_SWAP32(i) : (i) )
    
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

    struct AndroidHeader
    {
        unsigned int sig;
        unsigned int width;
        unsigned int height;
        unsigned int format;
    };
    
#ifndef DWORD
#define DWORD unsigned int
#endif
    struct DDS_PIXELFORMAT {
		DWORD dwSize;
		DWORD dwFlags;
		DWORD dwFourCC;
		DWORD dwRGBBitCount;
		DWORD dwRBitMask;
		DWORD dwGBitMask;
		DWORD dwBBitMask;
		DWORD dwABitMask;
	};
    
	typedef struct {
		DWORD           dwSize;
		DWORD           dwFlags;
		DWORD           dwHeight;
		DWORD           dwWidth;
		DWORD           dwPitchOrLinearSize;
		DWORD           dwDepth;
		DWORD           dwMipMapCount;
		DWORD           dwReserved1[11];
		DDS_PIXELFORMAT ddspf;
		DWORD           dwCaps;
		DWORD           dwCaps2;
		DWORD           dwCaps3;
		DWORD           dwCaps4;
		DWORD           dwReserved2;
	} DDS_HEADER;

#if (defined PUBLISHED_VERSION) && !(defined FORCEGUIEDITOR)
Texture* SILLYImageCodec::load(const LJFM::LJFMID& data, Texture* result, CodecPrivateData** aPrivate,bool bSyn)
{
    CodecPrivateDataSILLY* aPPrivate = new CodecPrivateDataSILLY;
    *aPrivate = aPPrivate;

	aPPrivate->mpPixels = NULL;
    aPPrivate->m_pMd = NULL;
    
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
    unsigned char* etcheader_ptr = (unsigned char*)data.GetData();
    if(data.GetSize()>=3 && etcheader_ptr[0]=='P' && etcheader_ptr[1]=='K' && etcheader_ptr[2]=='M')
    {
    	MaliSDKCEGUI::ETCHeader etcheader = MaliSDKCEGUI::ETCHeader((unsigned char*)data.GetData());
        aPPrivate->m_fmt = Texture::PF_ETC;
        aPPrivate->m_width = etcheader.getWidth();
        aPPrivate->m_height = etcheader.getHeight();
        result->loadFromMemory(data.GetData(),
                               Size(aPPrivate->m_width, aPPrivate->m_height), aPPrivate->m_fmt, bSyn);
        return result;
    }
#endif
    
    ccPVRv3TexHeader *header = (ccPVRv3TexHeader *)data.GetData();
    if(!header)
    {
        Logger::getSingletonPtr()->logEvent("SILLYImageCodec::load - Invalid PVRTexHeader image header", Errors);
        return 0;
    }
    
    //Make sure that tag is in correct formatting

    if (CC_SWAP_INT32_BIG_TO_HOST(header->version) == 0x50565203)
    {
        aPPrivate->m_fmt = Texture::PF_PVR4;

        aPPrivate->m_width = CC_SWAP_INT32_LITTLE_TO_HOST(header->width);
        aPPrivate->m_height = CC_SWAP_INT32_LITTLE_TO_HOST(header->height);
        result->loadFromMemory(((unsigned char*)data.GetData())+(sizeof(ccPVRv3TexHeader) + header->metadataLength),
                               Size(aPPrivate->m_width, aPPrivate->m_height), aPPrivate->m_fmt, bSyn);
        
        return result;
    }
    
    //check atc
    AndroidHeader* ah = (AndroidHeader*)data.GetData();
    if (ah->sig == 0x11111111) {
        aPPrivate->m_fmt = Texture::PF_ATC_Exp;
        aPPrivate->m_width = ah->width;
        aPPrivate->m_height = ah->height;
        result->loadFromMemory(data.GetData(),
                               Size(aPPrivate->m_width, aPPrivate->m_height), aPPrivate->m_fmt, bSyn);
        
        return result;
    }
#define FOURCC(c0, c1, c2, c3) (c0 | (c1 << 8) | (c2 << 16) | (c3 << 24))
    DWORD * pSig = (DWORD*)data.GetData();
    if ((*pSig) == FOURCC('D', 'D', 'S', ' ')) {
        DDS_HEADER *pHeader = (DDS_HEADER*)( ((unsigned char*)data.GetData()) + 4);
        cocos2d::PixelFormat pf = cocos2d::PF_UNKNOWN;
        cocos2d::DDSCodec ddscodec;
        if (pHeader->ddspf.dwFlags & 0x00000004) {
            pf = ddscodec.convertFourCCFormat(pHeader->ddspf.dwFourCC);
        }else
        {
            pf = ddscodec.convertPixelFormat(pHeader->ddspf.dwRGBBitCount,
                                             pHeader->ddspf.dwRBitMask,
                                             pHeader->ddspf.dwGBitMask,
                                             pHeader->ddspf.dwBBitMask,
                                             pHeader->ddspf.dwFlags & 0x00000001 ?
                                             pHeader->ddspf.dwABitMask : 0);
        }
        if (cocos2d::isCompressed(pf)) {
            if (pf == cocos2d::PF_DXT2 || pf == cocos2d::PF_DXT3)
            {
                aPPrivate->m_fmt = Texture::PF_DXT3;
            }else if(pf == cocos2d::PF_DXT4 ||  pf == cocos2d::PF_DXT5)
            {
                aPPrivate->m_fmt = Texture::PF_DXT5;
            }else
            {
                //error
                Logger::getSingletonPtr()->logEvent("SILLYImageCodec::load - Invalid dds format", Errors);
                return 0;
            }
            
            aPPrivate->m_width = pHeader->dwWidth;
            aPPrivate->m_height = pHeader->dwHeight;
            result->loadFromMemory(data.GetData(),
                                   Size(aPPrivate->m_width, aPPrivate->m_height), aPPrivate->m_fmt, bSyn);
            return result;
        }
    }
    
    
    aPPrivate->m_pMd = new SILLY::MemoryDataSource(static_cast<const SILLY::byte*>(data.GetData()), data.GetSize());
	SILLY::Image img(*(aPPrivate->m_pMd));
    
    timeval t;
    gettimeofday((struct timeval *)&t,  0);
//	SILLY::MemoryDataSource md(static_cast<const SILLY::byte*>(data.GetData()), data.GetSize());
//	SILLY::Image img(md);
//  SILLY::MemoryDataSource& md = *aPPrivate->m_pMd;
    
	if (!img.loadImageHeader())
	{
		Logger::getSingletonPtr()->logEvent("SILLYImageCodec::load - Invalid image header", Errors);
		return 0;
	}

	SILLY::PixelFormat dstfmt;
	Texture::PixelFormat cefmt;
	switch (img.getSourcePixelFormat())
	{
	case SILLY::PF_RGB:
		dstfmt = SILLY::PF_RGB;
		cefmt = Texture::PF_RGB;
		break;
	case SILLY::PF_RGBA:
	case SILLY::PF_A1B5G5R5:
		dstfmt = SILLY::PF_RGBA;
		cefmt = Texture::PF_RGBA;
		break;
	default:
		Logger::getSingletonPtr()->logEvent("SILLYImageCodec::load - Unsupported pixel format", Errors);
		return 0;
	}

    aPPrivate->m_fmt = cefmt;
	aPPrivate->m_width = img.getWidth();
	aPPrivate->m_height = img.getHeight();
    
	if (!img.loadImageData(dstfmt, SILLY::PO_TOP_LEFT))
	{ 
		Logger::getSingletonPtr()->logEvent("SILLYImageCodec::load - Invalid image data", Errors);
		return 0;
	}

	result->loadFromMemory(img.getPixelsDataPtr(),
		Size(img.getWidth(), img.getHeight()), cefmt, bSyn);

	if (bSyn)
	{
		size_t pixelsDataSize = img.getPixelsDataSize();
		aPPrivate->mpPixels = malloc(pixelsDataSize);
		if (aPPrivate->mpPixels)
		{
			memcpy(aPPrivate->mpPixels, img.getPixelsDataPtr(), pixelsDataSize);
		}
	}
    
    timeval t2;
    gettimeofday((struct timeval *)&t2,  0);
    
    printf("tick:%ld\n", t2.tv_usec - t.tv_usec);
	return result;
}
#endif

} // End of CEGUI namespace section 
