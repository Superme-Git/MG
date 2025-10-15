/****************************************************************************
Copyright (c) 2010 cocos2d-x.org

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
#import "CCImage.h"
#import "CCFileUtils.h"
#import <string>

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

// FontLabel support
#import "FontLabel/FontManager.h"
#import "FontLabel/FontLabelStringDrawing.h"
#include "support/image_support/OgreDDSCodec.h"
#include <WebP/decode.h>

#define CC_RGB_PREMULTIPLY_ALPHA(vr, vg, vb, va) \
(unsigned)(((unsigned)((unsigned char)(vr) * ((unsigned char)(va) + 1)) >> 8) | \
((unsigned)((unsigned char)(vg) * ((unsigned char)(va) + 1) >> 8) << 8) | \
((unsigned)((unsigned char)(vb) * ((unsigned char)(va) + 1) >> 8) << 16) | \
((unsigned)(unsigned char)(va) << 24))

typedef struct
{
    unsigned int height;
    unsigned int width;
    int         bitsPerComponent;
    bool        hasAlpha;
    bool        isPremultipliedAlpha;
    unsigned char*  data;
} tImageInfo;

static bool _initWithImage(CGImageRef cgImage, tImageInfo *pImageinfo)
{
    if(cgImage == NULL) 
    {
        return false;
    }
    
    // get image info
    
    pImageinfo->width = CGImageGetWidth(cgImage);
    pImageinfo->height = CGImageGetHeight(cgImage);
    
    CGImageAlphaInfo info = CGImageGetAlphaInfo(cgImage);
    pImageinfo->hasAlpha = ((info == kCGImageAlphaNoneSkipLast)
                            || (info == kCGImageAlphaPremultipliedLast)
                            || (info == kCGImageAlphaPremultipliedFirst) 
                            || (info == kCGImageAlphaLast) 
                            || (info == kCGImageAlphaFirst))?YES:NO;
    
    CGColorSpaceRef colorSpace = CGImageGetColorSpace(cgImage);
    if (colorSpace)
    {
        if (pImageinfo->hasAlpha)
        {
            if(info == kCGImageAlphaLast)
            {
                info = kCGImageAlphaPremultipliedLast;
            }
            else if(info == kCGImageAlphaFirst)
            {
                info = kCGImageAlphaPremultipliedFirst;
            }
            pImageinfo->isPremultipliedAlpha = true;
        }
        else 
        {
            info = kCGImageAlphaNoneSkipLast;
            pImageinfo->isPremultipliedAlpha = false;
        }
    }
    else
    {
        return false;
    }
    
    // change to RGBA8888
    pImageinfo->hasAlpha = true;
    pImageinfo->bitsPerComponent = 8;
    pImageinfo->data = new unsigned char[pImageinfo->width * pImageinfo->height * 4];
    colorSpace = CGColorSpaceCreateDeviceRGB();
    CGContextRef context = CGBitmapContextCreate(pImageinfo->data, 
                                                 pImageinfo->width, 
                                                 pImageinfo->height,
                                                 8, 
                                                 4 * pImageinfo->width, 
                                                 colorSpace, 
                                                 info | kCGBitmapByteOrder32Big);
    
    CGContextClearRect(context, CGRectMake(0, 0, pImageinfo->width, pImageinfo->height));
    //CGContextTranslateCTM(context, 0, 0);
    CGContextDrawImage(context, CGRectMake(0, 0, pImageinfo->width, pImageinfo->height), cgImage);
    
    CGContextRelease(context);
    
    return true;
}

static bool _initWithFile(const char* path, tImageInfo *pImageinfo)
{
    CGImageRef                CGImage;    
    UIImage                    *jpg;
    UIImage                    *png;
    bool            ret;
    
    // convert jpg to png before loading the texture
    
    NSString *fullPath = [NSString stringWithUTF8String:path];
    jpg = [[UIImage alloc] initWithContentsOfFile: fullPath];
    png = [[UIImage alloc] initWithData:UIImagePNGRepresentation(jpg)];
    CGImage = png.CGImage;    
    
    ret = _initWithImage(CGImage, pImageinfo);
    
    [png release];
    [jpg release];
    
    return ret;
}


static bool _initWithData(void * pBuffer, int length, tImageInfo *pImageinfo)
{
    bool ret = false;
    
    if (pBuffer) 
    {
        CGImageRef CGImage;
        NSData *data;
        
        data = [NSData dataWithBytes:pBuffer length:length];
        CGImage = [[UIImage imageWithData:data] CGImage];
        
        ret = _initWithImage(CGImage, pImageinfo);
    }
    
    return ret;
}

static bool _isValidFontName(const char *fontName)
{
    bool ret = false;
    
    NSString *fontNameNS = [NSString stringWithUTF8String:fontName];
    
    for (NSString *familiName in [UIFont familyNames]) 
    {
        if ([familiName isEqualToString:fontNameNS]) 
        {
            ret = true;
            goto out;
        }
        
        for(NSString *font in [UIFont fontNamesForFamilyName: familiName])
        {
            if ([font isEqualToString: fontNameNS])
            {
                ret = true;
                goto out;
            }
        }
    }
    
    out:
    return ret;
}

static CGSize _calculateStringSizeWithFontOrZFont(NSString *str, id font, CGSize *constrainSize, bool isZfont)
{
    NSArray *listItems = [str componentsSeparatedByString: @"\n"];
    CGSize dim = CGSizeZero;
    CGSize textRect = CGSizeZero;
    textRect.width = constrainSize->width > 0 ? constrainSize->width
                                              : 0x7fffffff;
    textRect.height = constrainSize->height > 0 ? constrainSize->height
                                              : 0x7fffffff;
    
    
    for (NSString *s in listItems)
    {
        CGSize tmp;
        if (isZfont)
        {
            tmp = [FontLabelStringDrawingHelper sizeWithZFont:s zfont:font constrainedToSize:textRect];
        }
        else
        {
           tmp = [s sizeWithFont:font constrainedToSize:textRect]; 
        }
        
        if (tmp.width > dim.width)
        {
           dim.width = tmp.width; 
        }
        
        dim.height += tmp.height;
    }
    
    return dim;
}

// refer CCImage::ETextAlign
#define ALIGN_TOP    1
#define ALIGN_CENTER 3
#define ALIGN_BOTTOM 2

static bool _initWithString(const char * pText, cocos2d::CCImage::ETextAlign eAlign, const char * pFontName, int nSize, tImageInfo* pInfo)
{
    bool bRet = false;
    do 
    {
        CC_BREAK_IF(! pText || ! pInfo);
        
        NSString * str  = [NSString stringWithUTF8String:pText];
        NSString * fntName = [NSString stringWithUTF8String:pFontName];
        CGSize dim, constrainSize;
        constrainSize.width = pInfo->width;
        constrainSize.height = pInfo->height;
        
        // create the font   
        id font;
        font = [UIFont fontWithName:fntName size:nSize];  
        if (font)
        {
                dim = _calculateStringSizeWithFontOrZFont(str, font, &constrainSize, false);
        }      
        
        if (! font)
        {
                font = [[FontManager sharedManager] zFontWithName:fntName pointSize:nSize];
                if (font)
                {
                    dim =_calculateStringSizeWithFontOrZFont(str, font, &constrainSize, true);
                }  
        }

        if (! font)
        {
                fntName = _isValidFontName(pFontName) ? fntName : @"MarkerFelt-Wide";
                font = [UIFont fontWithName:fntName size:nSize];
                
                if (! font) 
                {
                        font = [UIFont systemFontOfSize:nSize];
                }
                
                if (font)
                {
                    dim = _calculateStringSizeWithFontOrZFont(str, font, &constrainSize, false);
                }  
        }

        CC_BREAK_IF(! font);
        
        // compute start point
        int startH = 0;
        if (constrainSize.height > dim.height)
        {
            // vertical alignment
            unsigned int vAlignment = (eAlign >> 4) & 0x0F;
            if (vAlignment == ALIGN_TOP)
            {
                startH = 0;
            }
            else if (vAlignment == ALIGN_CENTER)
            {
                startH = (constrainSize.height - dim.height) / 2;
            }
            else 
            {
                startH = constrainSize.height - dim.height;
            }
        }
        
        // adjust text rect
        if (constrainSize.width > 0 && constrainSize.width > dim.width)
        {
            dim.width = constrainSize.width;
        }
        if (constrainSize.height > 0 && constrainSize.height > dim.height)
        {
            dim.height = constrainSize.height;
        }         
        
        unsigned char* data = new unsigned char[(int)(dim.width * dim.height * 4)];
        memset(data, 0, (int)(dim.width * dim.height * 4));
        
        // draw text
        CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();    
        CGContextRef context = CGBitmapContextCreate(data, dim.width, dim.height, 8, dim.width * 4, colorSpace, kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);
        CGColorSpaceRelease(colorSpace);
        
        if (! context)
        {
            delete[] data;
            break;
        }
        
        CGContextSetRGBFillColor(context, 1, 1, 1, 1);
        CGContextTranslateCTM(context, 0.0f, dim.height);
        CGContextScaleCTM(context, 1.0f, -1.0f); //NOTE: NSString draws in UIKit referential i.e. renders upside-down compared to CGBitmapContext referential
        UIGraphicsPushContext(context);
        
        // measure text size with specified font and determine the rectangle to draw text in
        unsigned uHoriFlag = eAlign & 0x0f;
        UITextAlignment align = (2 == uHoriFlag) ? UITextAlignmentRight
                                : (3 == uHoriFlag) ? UITextAlignmentCenter
                                : UITextAlignmentLeft;
        
        // normal fonts
        if( [font isKindOfClass:[UIFont class] ] )
        {
            [str drawInRect:CGRectMake(0, startH, dim.width, dim.height) withFont:font lineBreakMode:UILineBreakModeWordWrap alignment:align];
        }
        else // ZFont class 
        {
            [FontLabelStringDrawingHelper drawInRect:str rect:CGRectMake(0, startH, dim.width, dim.height) withZFont:font lineBreakMode:UILineBreakModeWordWrap alignment:align];
        }
        
        UIGraphicsPopContext();
        
        CGContextRelease(context);
               
        // output params
        pInfo->data = data;
        pInfo->hasAlpha = true;
        pInfo->isPremultipliedAlpha = true;
        pInfo->bitsPerComponent = 8;
        pInfo->width = dim.width;
        pInfo->height = dim.height;
        bRet = true;
    } while (0);

    return bRet;
}

NS_CC_BEGIN

CCImage::CCImage()
: m_nWidth(0)
, m_nHeight(0)
, m_nBitsPerComponent(0)
, m_pData(0)
, m_bHasAlpha(false)
, m_bPreMulti(false)
{
    
}

CCImage::~CCImage()
{
    CC_SAFE_DELETE_ARRAY(m_pData);
}

bool CCImage::initWithImageFile(const char * strPath, EImageFormat eImgFmt/* = eFmtPng*/)
{
	bool bRet = false;
    unsigned long nSize = 0;
    unsigned char* pBuffer = CCFileUtils::sharedFileUtils()->getFileData(
				CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(strPath),
				"rb",
				&nSize);
				
    if (pBuffer != NULL && nSize > 0)
    {
        bRet = initWithImageData(pBuffer, nSize, eImgFmt);
    }
    CC_SAFE_DELETE_ARRAY(pBuffer);
    return bRet;
}

bool CCImage::initWithImageFileThreadSafe(const char *fullpath, EImageFormat imageType)
{
    /*
     * CCFileUtils::fullPathFromRelativePath() is not thread-safe, it use autorelease().
     */
    bool bRet = false;
    unsigned long nSize = 0;
    unsigned char* pBuffer = CCFileUtils::sharedFileUtils()->getFileData(fullpath, "rb", &nSize);
    if (pBuffer != NULL && nSize > 0)
    {
        bRet = initWithImageData(pBuffer, nSize, imageType);
    }
    CC_SAFE_DELETE_ARRAY(pBuffer);
    return bRet;
}

bool CCImage::initWithImageData(void * pData, 
                                int nDataLen, 
                                EImageFormat eFmt,
                                int nWidth,
                                int nHeight,
                                int nBitsPerComponent,
                                int iScale)
{
    bool bRet = false;
    tImageInfo info = {0};
    do 
    {
        CC_BREAK_IF(! pData || nDataLen <= 0);
        if (eFmt == kFmtRawData)
        {
            bRet = _initWithRawData(pData, nDataLen, nWidth, nHeight, nBitsPerComponent);
        }else if(eFmt == kFmtDDS)
        {
            bRet = _initWithDDSData(pData, nDataLen);
        }else if (eFmt == kFmtWebP)
        {
            bRet = _initWithWebPData(pData, nDataLen);
        }
        else if(eFmt == kFmtPng)
        {
            bRet = _initWithPngData(pData, nDataLen);
        }
        else // init with png or jpg file data
        {
            bRet = _initWithData(pData, nDataLen, &info);
            if (bRet)
            {
                m_nHeight = (short)info.height;
                m_nWidth = (short)info.width;
                m_nBitsPerComponent = info.bitsPerComponent;
                m_bHasAlpha = info.hasAlpha;
                m_bPreMulti = info.isPremultipliedAlpha;
                m_pData = info.data;
            }
        }
    } while (0);
    
    return bRet;
}

bool CCImage::_initWithDDSData(void* pData, int nSize, int iScale)
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
    
	switch(image.format)
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

bool CCImage::_initWithRawData(void *pData, int nDatalen, int nWidth, int nHeight, int nBitsPerComponent)
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

bool CCImage::_initWithJpgData(void *pData, int nDatalen, int iScale)
{
    assert(0);
	return false;
}

// ycl 使用 png 库加�?png 图像

bool CCImage::_initWithWebPData(void *pData, int nDataLen, int iScale)
{
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
        m_bHasAlpha = config.input.has_alpha;
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
            m_bPreMulti = false;  // 指定颜色是否预乘 alpha。如果选择不预�?alpha，记得修改某�?PixelShader，如 Ball.psh �?
            
            if(m_bPreMulti)
            {
                unsigned int *tmp = (unsigned int *)m_pData;
                for(unsigned int i = 0; i < m_nHeight; i++)
                {
                    for(unsigned int j = 0; j < m_nWidth*4; j += 4)
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
}

bool CCImage::_saveImageToPNG(const char *pszFilePath, bool bIsToRGB)
{
    assert(0);
	return false;
}

bool CCImage::_saveImageToJPG(const char *pszFilePath)
{
    assert(0);
	return false;
}

bool CCImage::initWithString(
             const char * pText, 
             int         nWidth /* = 0 */, 
             int         nHeight /* = 0 */,
             ETextAlign eAlignMask /* = kAlignCenter */,
             const char * pFontName /* = nil */,
             int         nSize /* = 0 */)
{
    tImageInfo info = {0};
    info.width = nWidth;
    info.height = nHeight;
      
    if (! _initWithString(pText, eAlignMask, pFontName, nSize, &info))
    {
        return false;
    }
    m_nHeight = (short)info.height;
    m_nWidth = (short)info.width;
    m_nBitsPerComponent = info.bitsPerComponent;
    m_bHasAlpha = info.hasAlpha;
    m_bPreMulti = info.isPremultipliedAlpha;
    m_pData = info.data;

    return true;
}

bool CCImage::saveToFile(const char *pszFilePath, bool bIsToRGB)
{
    bool saveToPNG = false;
    bool needToCopyPixels = false;
    std::string filePath(pszFilePath);
    if (std::string::npos != filePath.find(".png"))
    {
        saveToPNG = true;
    }
        
    int bitsPerComponent = 8;            
    int bitsPerPixel = m_bHasAlpha ? 32 : 24;
    if ((! saveToPNG) || bIsToRGB)
    {
        bitsPerPixel = 24;
    }            
    
    int bytesPerRow    = (bitsPerPixel/8) * m_nWidth;
    int myDataLength = bytesPerRow * m_nHeight;
    
    unsigned char *pixels    = m_pData;
    
    // The data has alpha channel, and want to save it with an RGB png file,
    // or want to save as jpg,  remove the alpha channel.
    if ((saveToPNG && m_bHasAlpha && bIsToRGB)
       || (! saveToPNG))
    {
        pixels = new unsigned char[myDataLength];
        
        for (int i = 0; i < m_nHeight; ++i)
        {
            for (int j = 0; j < m_nWidth; ++j)
            {
                pixels[(i * m_nWidth + j) * 3] = m_pData[(i * m_nWidth + j) * 4];
                pixels[(i * m_nWidth + j) * 3 + 1] = m_pData[(i * m_nWidth + j) * 4 + 1];
                pixels[(i * m_nWidth + j) * 3 + 2] = m_pData[(i * m_nWidth + j) * 4 + 2];
            }
        }
        
        needToCopyPixels = true;
    }
        
    // make data provider with data.
    CGBitmapInfo bitmapInfo = kCGBitmapByteOrderDefault;
    if (saveToPNG && m_bHasAlpha && (! bIsToRGB))
    {
        bitmapInfo |= kCGImageAlphaPremultipliedLast;
    }
    CGDataProviderRef provider        = CGDataProviderCreateWithData(NULL, pixels, myDataLength, NULL);
    CGColorSpaceRef colorSpaceRef    = CGColorSpaceCreateDeviceRGB();
    CGImageRef iref                    = CGImageCreate(m_nWidth, m_nHeight,
                                                        bitsPerComponent, bitsPerPixel, bytesPerRow,
                                                        colorSpaceRef, bitmapInfo, provider,
                                                        NULL, false,
                                                        kCGRenderingIntentDefault);
        
    UIImage* image                    = [[UIImage alloc] initWithCGImage:iref];
        
    CGImageRelease(iref);    
    CGColorSpaceRelease(colorSpaceRef);
    CGDataProviderRelease(provider);
    
    NSData *data;
                
    if (saveToPNG)
    {
        data = UIImagePNGRepresentation(image);
    }
    else
    {
        data = UIImageJPEGRepresentation(image, 1.0f);
    }
    
    [data writeToFile:[NSString stringWithUTF8String:pszFilePath] atomically:YES];
        
    [image release];
        
    if (needToCopyPixels)
    {
        delete [] pixels;
    }
    
    return true;
}

NS_CC_END

