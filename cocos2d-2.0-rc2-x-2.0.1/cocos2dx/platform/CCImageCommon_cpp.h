/****************************************************************************
Copyright (c) 2010 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, CC_FREE of charge, to any person obtaining a copy
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
#if ((CC_TARGET_PLATFORM != CC_PLATFORM_WP8))
#ifndef __CC_PLATFORM_IMAGE_CPP__
#error "CCFileUtilsCommon_cpp.h can only be included for CCFileUtils.cpp in platform/win32(android,...)"
#endif /* __CC_PLATFORM_IMAGE_CPP__ */
#else 
#include "CCPlatformMacros.h"
#endif

#include "CCImage.h"
#include "CCCommon.h"
#include "CCStdC.h"
#include "CCFileUtils.h"
#include <string>
#include <ctype.h>
#include "support/image_support/TGAlib.h"
#include "../support/image_support/OgreDDSCodec.h"
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
#include "win32/png.h"
#include "win32/jpeglib.h"
#include "../../external/tiff/include/win32/tiffio.h"
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
#include "ios/png.h"
#include "ios/jpeglib.h"
#include "../../external/tiff/include/ios/tiffio.h"
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
#include "android/png.h"
#include "android/jpeglib.h"
#include "../../external/tiff/include/android/tiffio.h"
#else
#include "png.h"
#include "jpeglib.h"
#include "tiffio.h"
#endif

//#include "memory/CCMemoryMacros.h"
//#include "TypeDefine.h"
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID) 
#include "decode.h"
#else
#include "webp/decode.h"
#endif

#if (CC_TARGET_PLATFORM != CC_PLATFORM_IOS) 

NS_CC_BEGIN

// on ios, we should use platform/ios/CCImage_ios.mm instead

//////////////////////////////////////////////////////////////////////////
// Impliment CCImage
//////////////////////////////////////////////////////////////////////////

CCImage::CCImage()
: m_nWidth(0)
, m_nHeight(0)
, m_nBitsPerComponent(0)
, m_pData(0)
, m_bHasAlpha(false)
, m_bPreMulti(false)
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
, m_ft(nullptr)
#endif
{
/** gaoyong change for pcmt
*/
#ifdef USE_FREETYPE1
	m_Undefinedcharactercode = false;
	m_pFTLib= NULL;
	m_pFTFace= NULL;
	m_pBackupFTLib = NULL;
	m_pBackupFTFace = NULL;
	m_pCurFTLib = NULL;
	m_pCurFTFace = NULL;
	m_iZoom= 1;
	m_fStrokeWidth= 0.01f;
	m_ucLine_Spacing= 1;
	m_ucRow_Spacing= 1;
	m_bUseStroke= false;
#endif
/* end */
}

CCImage::~CCImage()
{
    CC_SAFE_DELETE_ARRAY(m_pData);
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	CC_SAFE_DELETE(m_ft);
#endif
/** gaoyong change for pcmt
*/
#ifdef USE_FREETYPE1
	destroyFTLib();
	destroyBackupFTLib();
#endif
/* end */
}

bool CCImage::initWithImageFile(const char * strPath, EImageFormat eImgFmt/* = eFmtPng*/)
{
    bool bRet = false;
    unsigned long nSize = 0;
    unsigned char* pBuffer = CCFileUtils::sharedFileUtils()->getFileData(CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(strPath), "rb", &nSize);
    if (pBuffer != NULL && nSize > 0)
    {
        bRet = initWithImageData(pBuffer, nSize, eImgFmt);
    }
    CC_SAFE_DELETE_ARRAY(pBuffer);
    return bRet;
}

bool CCImage::initWithImageFileThreadSafe(const char *fullpath, EImageFormat imageType)
{
    bool bRet = false;
    unsigned long nSize = 0;
    unsigned char *pBuffer = CCFileUtils::sharedFileUtils()->getFileData(fullpath, "rb", &nSize);
    if (pBuffer != NULL && nSize > 0)
    {
        bRet = initWithImageData(pBuffer, nSize, imageType);
    }
    CC_SAFE_DELETE_ARRAY(pBuffer);
    return bRet;
}

bool CCImage::initWithImageData(void * pData, 
                                int nDataLen, 
                                EImageFormat eFmt/* = eSrcFmtPng*/, 
                                int nWidth/* = 0*/,
                                int nHeight/* = 0*/,
                                int nBitsPerComponent/* = 8*/,
								int iScale/* = 100*/)
{
    bool bRet = false;
    do 
    {
        CC_BREAK_IF(! pData || nDataLen <= 0);

		if (isWebp(pData, nDataLen))
		{
			eFmt = kFmtWebP;
		}

        if (kFmtPng == eFmt)
        {
			bRet = _initWithPngData(pData, nDataLen, iScale);
            break;
        }
        else if (kFmtJpg == eFmt)
        {
            bRet = _initWithJpgData(pData, nDataLen);
            break;
        }
		else if (kFmtTga == eFmt)
		{
			bRet = _initWithTgaData(pData, nDataLen);
			break;
		}
        else if (kFmtTiff == eFmt)
        {
            bRet = _initWithTiffData(pData, nDataLen);
            break;
        }
        else if (kFmtRawData == eFmt)
        {
            bRet = _initWithRawData(pData, nDataLen, nWidth, nHeight, nBitsPerComponent);
            break;
        }
		else if (kFmtDDS == eFmt)
		{
			bRet = _initWithDDSData(pData, nDataLen);
			break;
		}
		else if(kFmtWebP == eFmt)
		{
			bRet = _initWithWebPData(pData, nDataLen);
			break;
		}
        else
        {
            // if it is a png file buffer.
            if (nDataLen > 8)
            {
                unsigned char* pHead = (unsigned char*)pData;
                if (   pHead[0] == 0x89
                    && pHead[1] == 0x50
                    && pHead[2] == 0x4E
                    && pHead[3] == 0x47
                    && pHead[4] == 0x0D
                    && pHead[5] == 0x0A
                    && pHead[6] == 0x1A
                    && pHead[7] == 0x0A)
                {
                    bRet = _initWithPngData(pData, nDataLen);
                    break;
                }
            }

            // if it is a tiff file buffer.
            if (nDataLen > 2)
            {
                unsigned char* pHead = (unsigned char*)pData;
                if (  (pHead[0] == 0x49 && pHead[1] == 0x49)
                    || (pHead[0] == 0x4d && pHead[1] == 0x4d)
                    )
                {
                    bRet = _initWithTiffData(pData, nDataLen);
                    break;
                }
            }

            // if it is a jpeg file buffer.
            if (nDataLen > 2)
            {
                unsigned char* pHead = (unsigned char*)pData;
                if (   pHead[0] == 0xff
                    && pHead[1] == 0xd8)
                {
                    bRet = _initWithJpgData(pData, nDataLen);
                    break;
                }
            }
        }
    } while (0);
    return bRet;
}

bool CCImage::isWebp(void* data, int nDatalen)
{
	if (nDatalen > 12)
	{
		// RIFFXXXXWEBP
		unsigned char* pHead = (unsigned char*)data;
		if (   pHead[0] == 0x52		// R
			&& pHead[1] == 0x49		// I
			&& pHead[2] == 0x46		// F
			&& pHead[3] == 0x46		// F
			&& pHead[8] == 0x57		// W
			&& pHead[9] == 0x45		// E
			&& pHead[10] == 0x42	// B
			&& pHead[11] == 0x50)	// P
		{
			return true;
		}
	}
	return false;
}

bool CCImage::_initWithJpgData(void * data, int nSize, int iScale/* = 100*/)
{
    /* these are standard libjpeg structures for reading(decompression) */
    struct jpeg_decompress_struct cinfo;
    struct jpeg_error_mgr jerr;
    /* libjpeg data structure for storing one row, that is, scanline of an image */
    JSAMPROW row_pointer[1] = {0};
    unsigned long location = 0;
    unsigned int i = 0;

    bool bRet = false;
    do 
    {
        /* here we set up the standard libjpeg error handler */
        cinfo.err = jpeg_std_error( &jerr );

        /* setup decompression process and source, then read JPEG header */
        jpeg_create_decompress( &cinfo );

        jpeg_mem_src( &cinfo, (unsigned char *) data, nSize );

        /* reading the image header which contains image information */
        jpeg_read_header( &cinfo, true );

        // we only support RGB or grayscale
        if (cinfo.jpeg_color_space != JCS_RGB)
        {
            if (cinfo.jpeg_color_space == JCS_GRAYSCALE || cinfo.jpeg_color_space == JCS_YCbCr)
            {
                cinfo.out_color_space = JCS_RGB;
            }
        }
        else
        {
            break;
        }

        /* Start decompression jpeg here */
        jpeg_start_decompress( &cinfo );

        /* init image info */
        m_nWidth  = (short)(cinfo.image_width);
        m_nHeight = (short)(cinfo.image_height);
        m_bHasAlpha = false;
        m_bPreMulti = false;
        m_nBitsPerComponent = 8;
        row_pointer[0] = new unsigned char[cinfo.output_width*cinfo.output_components];
        CC_BREAK_IF(! row_pointer[0]);

		m_pData = new unsigned char[cinfo.output_width*cinfo.output_height*cinfo.output_components];
        CC_BREAK_IF(! m_pData);

        /* now actually read the jpeg into the raw buffer */
        /* read one scan line at a time */
        while( cinfo.output_scanline < cinfo.image_height )
        {
            jpeg_read_scanlines( &cinfo, row_pointer, 1 );
            for( i=0; i<cinfo.image_width*cinfo.output_components;i++) 
            {
                m_pData[location++] = row_pointer[0][i];
            }
        }

        jpeg_finish_decompress( &cinfo );
        jpeg_destroy_decompress( &cinfo );
        /* wrap up decompression, destroy objects, CC_FREE pointers and close open files */        
        bRet = true;
    } while (0);

    CC_SAFE_DELETE_ARRAY(row_pointer[0]);

	if (!CCImage::IsNormal() && m_nWidth % 2 == 0 && m_nHeight % 2 == 0/* && iScale != 100*/)
	{
		int PixSize = 0;
		PixSize = m_nWidth * m_nHeight;
		CharRGB* uiB = (CharRGB*)m_pData;
		CharRGB uiCurPix;

		int PixSizeNew = 0;
		PixSizeNew = (m_nWidth / 2) * (m_nHeight / 2);
		unsigned char* pPixBuffer = NULL;
		int BufferSize = PixSizeNew * 3;
		pPixBuffer = (unsigned char*)malloc(BufferSize);
		memset(pPixBuffer, 0, BufferSize);
		CharRGB* uiBNew = (CharRGB*)pPixBuffer;
		long long uiCurPixNew;

		for (int i = 0; i < m_nHeight / 2; i++)
		{
			for (int i2 = 0; i2 < m_nWidth / 2; i2++)
			{
				uiBNew[i * m_nWidth / 2 + i2] = uiB[i2 * 2 + i * 2 * m_nWidth];
			}
		}

		CC_SAFE_DELETE_ARRAY(m_pData);
		m_pData = (unsigned char*)pPixBuffer;

		m_nWidth /= 2;
		m_nHeight /= 2;

	}

    return bRet;
}

static tmsize_t _tiffReadProc(thandle_t fd, void* buf, tmsize_t size)
{
    tImageSource* isource = (tImageSource*)fd;
    uint8* ma;
    uint64 mb;
    unsigned long n;
    unsigned long o;
    tmsize_t p;
    ma=(uint8*)buf;
    mb=size;
    p=0;
    while (mb>0)
    {
        n=0x80000000UL;
        if ((uint64)n>mb)
            n=(unsigned long)mb;


        if((int)(isource->offset + n) <= isource->size)
        {
            memcpy(ma, isource->data+isource->offset, n);
            isource->offset += n;
            o = n;
        }
        else
        {
            return 0;
        }

        ma+=o;
        mb-=o;
        p+=o;
        if (o!=n)
        {
            break;
        }
    }
    return p;
}

static tmsize_t _tiffWriteProc(thandle_t fd, void* buf, tmsize_t size)
{
    CC_UNUSED_PARAM(fd);
    CC_UNUSED_PARAM(buf);
    CC_UNUSED_PARAM(size);
    return 0;
}


static uint64 _tiffSeekProc(thandle_t fd, uint64 off, int whence)
{
    tImageSource* isource = (tImageSource*)fd;
    uint64 ret = -1;
    do 
    {
        if (whence == SEEK_SET)
        {
            CC_BREAK_IF(off > isource->size-1);
            ret = isource->offset = (uint32)off;
        }
        else if (whence == SEEK_CUR)
        {
            CC_BREAK_IF(isource->offset + off > isource->size-1);
            ret = isource->offset += (uint32)off;
        }
        else if (whence == SEEK_END)
        {
            CC_BREAK_IF(off > isource->size-1);
            ret = isource->offset = (uint32)(isource->size-1 - off);
        }
        else
        {
            CC_BREAK_IF(off > isource->size-1);
            ret = isource->offset = (uint32)off;
        }
    } while (0);

    return ret;
}

static uint64 _tiffSizeProc(thandle_t fd)
{
    tImageSource* pImageSrc = (tImageSource*)fd;
    return pImageSrc->size;
}

static int _tiffCloseProc(thandle_t fd)
{
    CC_UNUSED_PARAM(fd);
    return 0;
}

static int _tiffMapProc(thandle_t fd, void** pbase, toff_t* psize)
{
    CC_UNUSED_PARAM(fd);
    CC_UNUSED_PARAM(pbase);
    CC_UNUSED_PARAM(psize);
    return 0;
}

static void _tiffUnmapProc(thandle_t fd, void* base, toff_t size)
{
    CC_UNUSED_PARAM(fd);
    CC_UNUSED_PARAM(base);
    CC_UNUSED_PARAM(size);
}

bool CCImage::_initWithTiffData(void* pData, int nDataLen, int iScale/* = 100*/)
{
    bool bRet = false;
    do 
    {
        // set the read call back function
        tImageSource imageSource;
        imageSource.data    = (unsigned char*)pData;
        imageSource.size    = nDataLen;
        imageSource.offset  = 0;

        TIFF* tif = TIFFClientOpen("file.tif", "r", (thandle_t)&imageSource, 
            _tiffReadProc, _tiffWriteProc,
            _tiffSeekProc, _tiffCloseProc, _tiffSizeProc,
            _tiffMapProc,
            _tiffUnmapProc);

        CC_BREAK_IF(NULL == tif);

        uint32 w = 0, h = 0;
        uint16 bitsPerSample = 0, samplePerPixel = 0, planarConfig = 0;
        size_t npixels = 0;
        
        TIFFGetField(tif, TIFFTAG_IMAGEWIDTH, &w);
        TIFFGetField(tif, TIFFTAG_IMAGELENGTH, &h);
        TIFFGetField(tif, TIFFTAG_BITSPERSAMPLE, &bitsPerSample);
        TIFFGetField(tif, TIFFTAG_SAMPLESPERPIXEL, &samplePerPixel);
        TIFFGetField(tif, TIFFTAG_PLANARCONFIG, &planarConfig);

        npixels = w * h;
        
        m_bHasAlpha = true;
        m_nWidth = w;
        m_nHeight = h;
        m_nBitsPerComponent = 8;

        m_pData = new unsigned char[npixels * sizeof (uint32)];

        uint32* raster = (uint32*) _TIFFmalloc(npixels * sizeof (uint32));
        if (raster != NULL) 
        {
           if (TIFFReadRGBAImageOriented(tif, w, h, raster, ORIENTATION_TOPLEFT, 0))
           {
                unsigned char* src = (unsigned char*)raster;
                unsigned int* tmp = (unsigned int*)m_pData;

                /* the raster data is pre-multiplied by the alpha component 
                   after invoking TIFFReadRGBAImageOriented
                for(int j = 0; j < m_nWidth * m_nHeight * 4; j += 4)
                {
                    *tmp++ = CC_RGB_PREMULTIPLY_APLHA( src[j], src[j + 1], 
                        src[j + 2], src[j + 3] );
                }
                */
                m_bPreMulti = true;

               memcpy(m_pData, raster, npixels*sizeof (uint32));
           }

          _TIFFfree(raster);
        }
        

        TIFFClose(tif);

        bRet = true;
    } while (0);
    return bRet;
}

bool CCImage::_initWithWebPData(void* pData, int nDataLen, int iScale/* = 100*/)
{
#if 0
	int width, height;
	int err = WebPGetInfo((const uint8_t*)pData, nDataLen, &width, &height);
	if(0 != err)
	{
		int stride = width * 4;
		size_t imageDataSize = stride * height;
		m_pData = CC_NEW unsigned char[imageDataSize];

		if(WebPDecodeRGBAInto((const uint8_t*)pData, nDataLen, m_pData, imageDataSize, stride))
		{
			m_nWidth = width;
			m_nHeight = height;
			m_nBitsPerComponent = 8;
			m_bHasAlpha = true;
			m_bPreMulti = false;

			return true;
		}
		else
		{
			CC_SAFE_DELETE_ARRAY(m_pData);
		}
	}
#else
	bool ret = false;

	do
	{
		WebPDecoderConfig config;
		if (WebPInitDecoderConfig(&config) == 0) break;
		if (WebPGetFeatures(static_cast<const uint8_t*>(pData), nDataLen, &config.input) != VP8_STATUS_OK) break;
		if (config.input.width == 0 || config.input.height == 0) break;

		//webp doesn't have premultipliedAlpha
		m_bPreMulti = false;
		m_nBitsPerComponent = 8;
		m_bHasAlpha = (config.input.has_alpha != 0);
		m_nWidth    = config.input.width;
		m_nHeight   = config.input.height;

		size_t imageDataSize = m_bHasAlpha ? m_nWidth * m_nHeight * 4 : m_nWidth * m_nHeight * 3;
		m_pData = new unsigned char[imageDataSize];

		config.output.colorspace = m_bHasAlpha ? MODE_RGBA : MODE_RGB;
		config.output.u.RGBA.rgba = static_cast<uint8_t*>(m_pData);
		config.output.u.RGBA.stride = m_bHasAlpha ? m_nWidth * 4 : m_nWidth * 3;
		config.output.u.RGBA.size = imageDataSize;
		config.output.is_external_memory = 1;

		if (WebPDecode(static_cast<const uint8_t*>(pData), nDataLen, &config) != VP8_STATUS_OK)
		{
			CC_SAFE_DELETE_ARRAY(m_pData);
			break;
		}

		if (m_bHasAlpha) 
		{
			m_bPreMulti = false;  // 指定颜色是否预乘 alpha。如果选择不预乘 alpha，记得修改某些 PixelShader，如 Ball.psh 等

			if(m_bPreMulti)
			{
				unsigned int *tmp = (unsigned int *)m_pData;
				for(unsigned int i = 0; i < m_nHeight; i++)
				{
					for(unsigned int j = 0; j < (unsigned int)m_nWidth*4; j += 4)
					{
						int k = i*m_nWidth*4+j;
						*tmp++ = CC_RGB_PREMULTIPLY_ALPHA( m_pData[k], m_pData[k+1], m_pData[k+2], m_pData[k+3] );
					}
				}
			}

			if (!CCImage::IsNormal() && m_nWidth % 2 == 0 && m_nHeight % 2 == 0/* && iScale != 100*/)
			{
				int PixSize = 0;
				PixSize = m_nWidth * m_nHeight;
				CharRGBA* uiB = (CharRGBA*)m_pData;
				CharRGBA uiCurPix;

				int PixSizeNew = 0;
				PixSizeNew = (m_nWidth / 2) * (m_nHeight / 2);
				unsigned char* pPixBuffer = NULL;
				int BufferSize = PixSizeNew * 4;
				pPixBuffer = (unsigned char*)malloc(BufferSize);
				memset(pPixBuffer, 0, BufferSize);
				CharRGBA* uiBNew = (CharRGBA*)pPixBuffer;
				//CharRGBA uiCurPixNew;

				for (int i = 0; i < m_nHeight / 2; i++)
				{
					for (int i2 = 0; i2 < m_nWidth / 2; i2++)
					{
						uiBNew[i * m_nWidth / 2 + i2] = uiB[i2 * 2 + i * 2 * m_nWidth];
						//uiBNew[i * m_nWidth / 2 + i2] = uiCurPixNew;
					}
				}

				CC_SAFE_DELETE_ARRAY(m_pData);
				m_pData = (unsigned char*)pPixBuffer;

				m_nWidth /= 2;
				m_nHeight /= 2;

			}

		}
		else
		{
			if (!CCImage::IsNormal() && m_nWidth % 2 == 0 && m_nHeight % 2 == 0/* && iScale != 100*/)
			{
				int PixSize = 0;
				PixSize = m_nWidth * m_nHeight;
				CharRGB* uiB = (CharRGB*)m_pData;
				CharRGB uiCurPix;

				int PixSizeNew = 0;
				PixSizeNew = (m_nWidth / 2) * (m_nHeight / 2);
				unsigned char* pPixBuffer = NULL;
				int BufferSize = PixSizeNew * 3;
				pPixBuffer = (unsigned char*)malloc(BufferSize);
				memset(pPixBuffer, 0, BufferSize);
				CharRGB* uiBNew = (CharRGB*)pPixBuffer;
				long long uiCurPixNew;

				for (int i = 0; i < m_nHeight / 2; i++)
				{
					for (int i2 = 0; i2 < m_nWidth / 2; i2++)
					{
						uiBNew[i * m_nWidth / 2 + i2] = uiB[i2 * 2 + i * 2 * m_nWidth];
					}
				}

				CC_SAFE_DELETE_ARRAY(m_pData);
				m_pData = (unsigned char*)pPixBuffer;

				m_nWidth /= 2;
				m_nHeight /= 2;

			}
		}

		ret = true;

	} while (0);

	return ret;
#endif//

	return false;
}

bool CCImage::_initWithRawData(void * pData, int nDatalen, int nWidth, int nHeight, int nBitsPerComponent)
{
    bool bRet = false;
    do 
    {
        CC_BREAK_IF(0 == nWidth || 0 == nHeight);

        m_nBitsPerComponent = nBitsPerComponent;
        m_nHeight   = (short)nHeight;
        m_nWidth    = (short)nWidth;
        m_bHasAlpha = true;

        // only RGBA8888 surported
        int nBytesPerComponent = 4;
        int nSize = nHeight * nWidth * nBytesPerComponent;
        m_pData = new unsigned char[nSize];
        CC_BREAK_IF(! m_pData);
        memcpy(m_pData, pData, nSize);

        bRet = true;
    } while (0);
    return bRet;
}

bool CCImage::saveToFile(const char *pszFilePath, bool bIsToRGB)
{
    bool bRet = false;

    do 
    {
        CC_BREAK_IF(NULL == pszFilePath);

        std::string strFilePath(pszFilePath);
        CC_BREAK_IF(strFilePath.size() <= 4);

		std::string strLowerCasePath(strFilePath);
        for (unsigned int i = 0; i < strLowerCasePath.length(); ++i)
        {
            strLowerCasePath[i] = tolower(strFilePath[i]);
        }

		if (std::string::npos != strLowerCasePath.find(".png"))
        {
            CC_BREAK_IF(!_saveImageToPNG(pszFilePath, bIsToRGB));
        }
		else if (std::string::npos != strLowerCasePath.find(".jpg"))
        {
            CC_BREAK_IF(!_saveImageToJPG(pszFilePath));
        }
        else
        {
            break;
        }

        bRet = true;
    } while (0);

    return bRet;
}

bool CCImage::_saveImageToPNG(const char * pszFilePath, bool bIsToRGB)
{
    bool bRet = false;
    do 
    {
        CC_BREAK_IF(NULL == pszFilePath);

        FILE *fp;
        png_structp png_ptr;
        png_infop info_ptr;
        png_colorp palette;
        png_bytep *row_pointers;

        fp = fopen(pszFilePath, "wb");
        CC_BREAK_IF(NULL == fp);

        png_ptr = png_create_write_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);

        if (NULL == png_ptr)
        {
            fclose(fp);
            break;
        }

        info_ptr = png_create_info_struct(png_ptr);
        if (NULL == info_ptr)
        {
            fclose(fp);
            png_destroy_write_struct(&png_ptr, NULL);
            break;
        }
#if (CC_TARGET_PLATFORM != CC_PLATFORM_BADA)
        if (setjmp(png_jmpbuf(png_ptr)))
        {
            fclose(fp);
            png_destroy_write_struct(&png_ptr, &info_ptr);
            break;
        }
#endif
        png_init_io(png_ptr, fp);

        if (!bIsToRGB && m_bHasAlpha)
        {
            png_set_IHDR(png_ptr, info_ptr, m_nWidth, m_nHeight, 8, PNG_COLOR_TYPE_RGB_ALPHA,
                PNG_INTERLACE_NONE, PNG_COMPRESSION_TYPE_BASE, PNG_FILTER_TYPE_BASE);
        } 
        else
        {
            png_set_IHDR(png_ptr, info_ptr, m_nWidth, m_nHeight, 8, PNG_COLOR_TYPE_RGB,
                PNG_INTERLACE_NONE, PNG_COMPRESSION_TYPE_BASE, PNG_FILTER_TYPE_BASE);
        }

        palette = (png_colorp)png_malloc(png_ptr, PNG_MAX_PALETTE_LENGTH * sizeof (png_color));
        png_set_PLTE(png_ptr, info_ptr, palette, PNG_MAX_PALETTE_LENGTH);

        png_write_info(png_ptr, info_ptr);

        png_set_packing(png_ptr);

        row_pointers = (png_bytep *)malloc(m_nHeight * sizeof(png_bytep));
        if(row_pointers == NULL)
        {
            fclose(fp);
            png_destroy_write_struct(&png_ptr, &info_ptr);
            break;
        }

        if (!m_bHasAlpha)
        {
            for (int i = 0; i < (int)m_nHeight; i++)
            {
                row_pointers[i] = (png_bytep)m_pData + i * m_nWidth * 3;
            }

            png_write_image(png_ptr, row_pointers);

            free(row_pointers);
            row_pointers = NULL;
        }
        else
        {
            if (bIsToRGB)
            {
                unsigned char *pTempData = new unsigned char[m_nWidth * m_nHeight * 3];
                if (NULL == pTempData)
                {
                    fclose(fp);
                    png_destroy_write_struct(&png_ptr, &info_ptr);
                    break;
                }

                for (int i = 0; i < m_nHeight; ++i)
                {
                    for (int j = 0; j < m_nWidth; ++j)
                    {
                        pTempData[(i * m_nWidth + j) * 3] = m_pData[(i * m_nWidth + j) * 4];
                        pTempData[(i * m_nWidth + j) * 3 + 1] = m_pData[(i * m_nWidth + j) * 4 + 1];
                        pTempData[(i * m_nWidth + j) * 3 + 2] = m_pData[(i * m_nWidth + j) * 4 + 2];
                    }
                }

                for (int i = 0; i < (int)m_nHeight; i++)
                {
                    row_pointers[i] = (png_bytep)pTempData + i * m_nWidth * 3;
                }

                png_write_image(png_ptr, row_pointers);

                free(row_pointers);
                row_pointers = NULL;

                CC_SAFE_DELETE_ARRAY(pTempData);
            } 
            else
            {
                for (int i = 0; i < (int)m_nHeight; i++)
                {
                    row_pointers[i] = (png_bytep)m_pData + i * m_nWidth * 4;
                }

                png_write_image(png_ptr, row_pointers);

                free(row_pointers);
                row_pointers = NULL;
            }
        }

        png_write_end(png_ptr, info_ptr);

        png_free(png_ptr, palette);
        palette = NULL;

        png_destroy_write_struct(&png_ptr, &info_ptr);

        fclose(fp);

        bRet = true;
    } while (0);
    return bRet;
}

bool CCImage::_saveImageToJPG(const char * pszFilePath)
{
    bool bRet = false;
    do 
    {
        CC_BREAK_IF(NULL == pszFilePath);

        struct jpeg_compress_struct cinfo;
        struct jpeg_error_mgr jerr;
        FILE * outfile;                 /* target file */
        JSAMPROW row_pointer[1];        /* pointer to JSAMPLE row[s] */
        int     row_stride;          /* physical row width in image buffer */

        cinfo.err = jpeg_std_error(&jerr);
        /* Now we can initialize the JPEG compression object. */
        jpeg_create_compress(&cinfo);

        CC_BREAK_IF((outfile = fopen(pszFilePath, "wb")) == NULL);
        
        jpeg_stdio_dest(&cinfo, outfile);

        cinfo.image_width = m_nWidth;    /* image width and height, in pixels */
        cinfo.image_height = m_nHeight;
        cinfo.input_components = 3;       /* # of color components per pixel */
        cinfo.in_color_space = JCS_RGB;       /* colorspace of input image */

        jpeg_set_defaults(&cinfo);

        jpeg_start_compress(&cinfo, TRUE);

        row_stride = m_nWidth * 3; /* JSAMPLEs per row in image_buffer */

        if (m_bHasAlpha)
        {
            unsigned char *pTempData = new unsigned char[m_nWidth * m_nHeight * 3];
            if (NULL == pTempData)
            {
                jpeg_finish_compress(&cinfo);
                jpeg_destroy_compress(&cinfo);
                fclose(outfile);
                break;
            }

            for (int i = 0; i < m_nHeight; ++i)
            {
                for (int j = 0; j < m_nWidth; ++j)

                {
                    pTempData[(i * m_nWidth + j) * 3] = m_pData[(i * m_nWidth + j) * 4];
                    pTempData[(i * m_nWidth + j) * 3 + 1] = m_pData[(i * m_nWidth + j) * 4 + 1];
                    pTempData[(i * m_nWidth + j) * 3 + 2] = m_pData[(i * m_nWidth + j) * 4 + 2];
                }
            }

            while (cinfo.next_scanline < cinfo.image_height) {
                row_pointer[0] = & pTempData[cinfo.next_scanline * row_stride];
                (void) jpeg_write_scanlines(&cinfo, row_pointer, 1);
            }

            CC_SAFE_DELETE_ARRAY(pTempData);
        } 
        else
        {
            while (cinfo.next_scanline < cinfo.image_height) {
                row_pointer[0] = & m_pData[cinfo.next_scanline * row_stride];
                (void) jpeg_write_scanlines(&cinfo, row_pointer, 1);
            }
        }

        jpeg_finish_compress(&cinfo);
        fclose(outfile);
        jpeg_destroy_compress(&cinfo);
        
        bRet = true;
    } while (0);
    return bRet;
}
bool CCImage::_initWithTgaData(void* pData, int nSize, int iScale/* = 100*/)
{
	tImageTGA* tgaInfo = NULL;
	tgaInfo = tgaLoad((unsigned char*)pData, nSize);
	if (tgaInfo->status != TGA_OK)
	{
		CCAssert(0, "CCImage cannot load TGA file");
		return false;
	}

	m_nWidth = tgaInfo->width;
	m_nHeight = tgaInfo->height;
	m_bHasAlpha = true;

	m_nBitsPerComponent = 8;

	// mode equals the number of image components
	int mode = tgaInfo->pixelDepth / 8;
	// total is the number of unsigned chars to read
	int total = tgaInfo->height * tgaInfo->width * mode;

	m_pData = new unsigned char[total];

	unsigned char* pSrc = tgaInfo->imageData;

	memcpy(m_pData, pSrc, total);

	tgaDestroy(tgaInfo);

	return true;
}

bool CCImage::_initWithDDSData(void* pData, int nSize, int iScale/* = 100*/)
{
	DDSCodec ddsCodec;
	CDDSImage image;
	MemStream input((unsigned char*)pData, nSize);

	if (!ddsCodec.decode(input, image))
	{
		//log something
		return false;
	}

	m_nWidth = image.width;
	m_nHeight = image.height;

	switch (image.format)
	{
	case PF_A4R4G4B4:
	case PF_A8R8G8B8:
	case PF_A8B8G8R8:
		m_bHasAlpha = true;
		break;
	case PF_B8G8R8:
		m_bHasAlpha = false;
		break;
	}

	m_nBitsPerComponent = 8;
	m_pData = new unsigned char[image.size];

	unsigned char* pSrc = image.GetBuffer();
	/*for (int i = 0; i < image.size; i+=3)
	{
	m_pData[i] = pSrc[i+2];
	m_pData[i+1] = pSrc[i+1];
	m_pData[i+2] = pSrc[i];
	}*/

	if (m_bHasAlpha)
	{
		if (!CCImage::IsNormal() && m_nWidth % 2 == 0 && m_nHeight % 2 == 0/* && iScale != 100*/)
		{
			int PixSize = 0;
			PixSize = m_nWidth * m_nHeight;
			CharRGBA* uiB = (CharRGBA*)image.GetBuffer();
			CharRGBA uiCurPix;

			int PixSizeNew = 0;
			PixSizeNew = (m_nWidth / 2) * (m_nHeight / 2);
			unsigned char* pPixBuffer = NULL;
			int BufferSize = PixSizeNew * 4;
			pPixBuffer = (unsigned char*)malloc(BufferSize);
			memset(pPixBuffer, 0, BufferSize);
			CharRGBA* uiBNew = (CharRGBA*)pPixBuffer;

			for (int i = 0; i < m_nHeight / 2; i++)
			{
				for (int i2 = 0; i2 < m_nWidth / 2; i2++)
				{
					uiBNew[i * m_nWidth / 2 + i2] = uiB[i2 * 2 + i * 2 * m_nWidth];
				}
			}

			CC_SAFE_DELETE_ARRAY(m_pData);
			m_pData = (unsigned char*)pPixBuffer;

			m_nWidth /= 2;
			m_nHeight /= 2;

		}
		else
		{
			memcpy(m_pData, image.GetBuffer(), image.size);
		}
	} 
	else
	{
		if (!CCImage::IsNormal() && m_nWidth % 2 == 0 && m_nHeight % 2 == 0/* && iScale != 100*/)
		{
			int PixSize = 0;
			PixSize = m_nWidth * m_nHeight;
			CharRGB* uiB = (CharRGB*)image.GetBuffer();
			CharRGB uiCurPix;

			int PixSizeNew = 0;
			PixSizeNew = (m_nWidth / 2) * (m_nHeight / 2);
			unsigned char* pPixBuffer = NULL;
			int BufferSize = PixSizeNew * 3;
			pPixBuffer = (unsigned char*)malloc(BufferSize);
			memset(pPixBuffer, 0, BufferSize);
			CharRGB* uiBNew = (CharRGB*)pPixBuffer;
			long long uiCurPixNew;

			for (int i = 0; i < m_nHeight / 2; i++)
			{
				for (int i2 = 0; i2 < m_nWidth / 2; i2++)
				{
					uiBNew[i * m_nWidth / 2 + i2] = uiB[i2 * 2 + i * 2 * m_nWidth];
				}
			}

			CC_SAFE_DELETE_ARRAY(m_pData);
			m_pData = (unsigned char*)pPixBuffer;

			m_nWidth /= 2;
			m_nHeight /= 2;

		}
		else
		{
			memcpy(m_pData, image.GetBuffer(), image.size);
		}
	}

	//memcpy(m_pData, image.GetBuffer(), image.size);

	return true;
}
NS_CC_END

#endif // (CC_TARGET_PLATFORM != TARGET_OS_IPHONE)
/* ios/CCImage_ios.mm uses "mm" as the extension, 
   so we cannot inclue it in this CCImage.cpp.
   It makes a little difference on ios */
