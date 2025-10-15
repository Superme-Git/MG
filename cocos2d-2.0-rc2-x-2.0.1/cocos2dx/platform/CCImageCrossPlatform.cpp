//////////////////////////////////////////////////////////////////////////
// File:		CCImage.cpp
// Author:	杨春雷
// Date:		2016/03/16
//////////////////////////////////////////////////////////////////////////

#include "CCImage.h"
#include "CCStdC.h"
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
#include "win32/png.h"
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
#include "ios/png.h"
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
#include "android/png.h"
#else
#include "png.h"
#endif
#include <vector>

namespace cocos2d
{
	static void pngReadCallback(png_structp png_ptr, png_bytep data, png_size_t length)
	{
		tImageSource* isource = (tImageSource*)png_get_io_ptr(png_ptr);

		if ((int)(isource->offset + length) <= isource->size)
		{
			memcpy(data, isource->data + isource->offset, length);
			isource->offset += length;
		}
		else
		{
			png_error(png_ptr, "pngReaderCallback failed");
		}
	}

	bool CCImage::_initWithPngData(void * pData, int nDatalen, int iScale/* = 100*/)
	{
		// length of bytes to check if it is a valid png file
		// length of bytes to check if it is a valid png file
#define PNGSIGSIZE  8
		bool bRet = false;
		png_byte        header[PNGSIGSIZE] = { 0 };
		png_structp     png_ptr = 0;
		png_infop       info_ptr = 0;

		do
		{
			// png header len is 8 bytes
			CC_BREAK_IF(nDatalen < PNGSIGSIZE);

			// check the data is png or not
			memcpy(header, pData, PNGSIGSIZE);
			CC_BREAK_IF(png_sig_cmp(header, 0, PNGSIGSIZE));

			// init png_struct
			png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, 0, 0, 0);
			CC_BREAK_IF(!png_ptr);

			// init png_info
			info_ptr = png_create_info_struct(png_ptr);
			CC_BREAK_IF(!info_ptr);

#if (CC_TARGET_PLATFORM != CC_PLATFORM_BADA && CC_TARGET_PLATFORM != CC_PLATFORM_NACL)
			CC_BREAK_IF(setjmp(png_jmpbuf(png_ptr)));
#endif

			// set the read call back function
			tImageSource imageSource;
			imageSource.data = (unsigned char*)pData;
			imageSource.size = nDatalen;
			imageSource.offset = 0;
			png_set_read_fn(png_ptr, &imageSource, pngReadCallback);

			// read png header info

			// read png file info
			png_read_info(png_ptr, info_ptr);

			m_nWidth = png_get_image_width(png_ptr, info_ptr);
			m_nHeight = png_get_image_height(png_ptr, info_ptr);

			m_nBitsPerComponent = png_get_bit_depth(png_ptr, info_ptr);
			png_uint_32 color_type = png_get_color_type(png_ptr, info_ptr);

			//CCLOG("color type %u", color_type);

			// force palette images to be expanded to 24-bit RGB
			// it may include alpha channel
			if (color_type == PNG_COLOR_TYPE_PALETTE)
			{
				png_set_palette_to_rgb(png_ptr);
			}
			// low-bit-depth grayscale images are to be expanded to 8 bits
			if (color_type == PNG_COLOR_TYPE_GRAY && m_nBitsPerComponent < 8)
			{
				png_set_expand_gray_1_2_4_to_8(png_ptr);
			}
			// expand any tRNS chunk data into a full alpha channel
			if (png_get_valid(png_ptr, info_ptr, PNG_INFO_tRNS))
			{
				png_set_tRNS_to_alpha(png_ptr);
			}
			// reduce images with 16-bit samples to 8 bits
			if (m_nBitsPerComponent == 16)
			{
				png_set_strip_16(png_ptr);
			}
			// expand grayscale images to RGB
			if (color_type == PNG_COLOR_TYPE_GRAY || color_type == PNG_COLOR_TYPE_GRAY_ALPHA)
			{
				png_set_gray_to_rgb(png_ptr);
			}

			// read png data
			// m_nBitsPerComponent will always be 8
			m_nBitsPerComponent = 8;
			png_uint_32 rowbytes;
			png_bytep* row_pointers = (png_bytep*)malloc(sizeof(png_bytep) * m_nHeight);

			png_read_update_info(png_ptr, info_ptr);

			rowbytes = png_get_rowbytes(png_ptr, info_ptr);

			m_pData = new unsigned char[rowbytes * m_nHeight];
			CC_BREAK_IF(!m_pData);

			for (unsigned short i = 0; i < m_nHeight; ++i)
			{
				row_pointers[i] = m_pData + i*rowbytes;
			}
			png_read_image(png_ptr, row_pointers);

			png_read_end(png_ptr, NULL);

			png_uint_32 channel = rowbytes / m_nWidth;
			if (channel == 4)
			{
				m_bHasAlpha = true;
				m_bPreMulti = false;  // 指定颜色是否预乘 alpha。如果选择不预乘 alpha，记得修改某些 PixelShader，如 Ball.psh 等

				if (m_bPreMulti)
				{
					unsigned int *tmp = (unsigned int *)m_pData;
					for (unsigned short i = 0; i < m_nHeight; i++)
					{
						for (unsigned int j = 0; j < rowbytes; j += 4)
						{
							*tmp++ = CC_RGB_PREMULTIPLY_ALPHA(row_pointers[i][j], row_pointers[i][j + 1],
								row_pointers[i][j + 2], row_pointers[i][j + 3]);
						}
					}
				}

				if (!CCImage::IsNormal() && m_nWidth % 2 == 0 && m_nHeight % 2 == 0/* && iScale != 100*/)
				{
					int PixSize = 0;
					PixSize = m_nWidth * m_nHeight;
					CharRGBA* uiB = (CharRGBA*)m_pData;

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
			}
			else
			{
				m_nWidth = m_nWidth;
				m_nHeight = m_nHeight;
			}

			CC_SAFE_FREE(row_pointers);

			bRet = true;
		} while (0);

		if (png_ptr)
		{
			png_destroy_read_struct(&png_ptr, (info_ptr) ? &info_ptr : 0, 0);
		}
		return bRet;
	}

	bool CCImage::scaleToRect(int width, int height, bool bSmooth)
	{
		if (m_pData == NULL)	return false;

		if (width > m_nWidth || height > m_nHeight)
		{
			return false;  // 只支持缩小
		}

		float srcRatio = (float)m_nWidth / m_nHeight;
		float dstRatio = (float)width / height;

		float invScale = srcRatio > dstRatio ? ((float)m_nHeight / height) : ((float)m_nWidth / width);
		int sampleCount = invScale;
		bool bSubPixel = false;
		if (invScale > sampleCount)
		{
			++sampleCount;
			bSubPixel = true;
		}

		if (!bSmooth)
		{
			bSubPixel = false;
		}

		float fSrcXOffset = (m_nWidth - width * invScale) / 2.f;
		float fSrcYOffset = (m_nHeight - height * invScale) / 2.f;

		int pixelSize = m_bHasAlpha ? 4 : 3;
		unsigned char* pPixelBuffer = new unsigned char[width * height * pixelSize];
		if (pPixelBuffer)
		{
			memset(pPixelBuffer, 0, width * height * pixelSize);

			for (int r = 0; r < height; ++r)
			{
				float fSrcYStart = fSrcYOffset + r * invScale;
				float fSrcYEnd = fSrcYStart + invScale;

				for (int c = 0; c < width; ++c)
				{
					unsigned char* pDstPixel = pPixelBuffer + (r * width + c) * pixelSize;

					std::vector<CharRGBA> rgbaPixels;
					std::vector<CharRGB> rgbPixels;

					float fSrcXStart = fSrcXOffset + c * invScale;
					float fSrcXEnd = fSrcXStart + invScale;

					float leftBorderSampleFactor = 1 - (fSrcXStart - (int)fSrcXStart);
					float topBorderSampleFactor = 1 - (fSrcYStart - (int)fSrcYStart);
					float rightBorderSampleFactor = fSrcXEnd - (int)fSrcXEnd;
					float bottomBorderSampleFactor = fSrcYEnd - (int)fSrcYEnd;

					int iSrcXStart = fSrcXStart;
					int iSrcYStart = fSrcYStart;
					int iSrcXEnd = fSrcXEnd;
					int iSrcYEnd = fSrcYEnd;
					float realSampleCount = 0;
					for (int srcR = iSrcYStart; srcR <= iSrcYEnd; ++srcR)
					{
						if (!bSmooth && realSampleCount > 0)
						{
							break;
						}

						for (int srcC = iSrcXStart; srcC <= iSrcXEnd; ++srcC)
						{
							if (!bSmooth && realSampleCount > 0)
							{
								break;
							}

							const unsigned char* pSamplePixel = getPixel(srcC, srcR, pixelSize);
							if (pSamplePixel)
							{
								CharRGBA rgbaColor;
								CharRGB rgbColor;

								if (m_bHasAlpha)
								{
									rgbaColor = *(const CharRGBA*)pSamplePixel;
								}
								else
								{
									rgbColor = *(const CharRGB*)pSamplePixel;
								}

								if (bSubPixel)
								{
									float sampleFactor = 1;
									if (srcR == iSrcYStart)
									{
										if (srcC == iSrcXStart)
										{
											sampleFactor = leftBorderSampleFactor * topBorderSampleFactor;
										}
										else if (srcC == iSrcXEnd)
										{
											sampleFactor = rightBorderSampleFactor * topBorderSampleFactor;
										}
										else
										{
											sampleFactor = topBorderSampleFactor;
										}
									}
									else if (srcR == iSrcYEnd)
									{
										if (srcC == iSrcXStart)
										{
											sampleFactor = leftBorderSampleFactor * bottomBorderSampleFactor;
										}
										else if (srcC == iSrcXEnd)
										{
											sampleFactor = rightBorderSampleFactor * bottomBorderSampleFactor;
										}
										else
										{
											sampleFactor = bottomBorderSampleFactor;
										}
									}
									else if (srcC == iSrcXStart)
									{
										sampleFactor = leftBorderSampleFactor;
									}
									else if (srcC == iSrcXEnd)
									{
										sampleFactor = rightBorderSampleFactor;
									}

									if (m_bHasAlpha)
									{
										rgbaColor = rgbaColor * sampleFactor;
									}
									else
									{
										rgbColor = rgbColor * sampleFactor;
									}

									realSampleCount += sampleFactor;
								}
								else
								{
									realSampleCount += 1;
								}

								if (m_bHasAlpha)
								{
									rgbaPixels.push_back(rgbaColor);
								}
								else
								{
									rgbPixels.push_back(rgbColor);
								}
							}
						}
					}

					if (realSampleCount > 0)
					{
						float fR = 0;
						float fG = 0;
						float fB = 0;
						float fA = 0;

						if (m_bHasAlpha)
						{
							for (std::vector<CharRGBA>::iterator it = rgbaPixels.begin(); it != rgbaPixels.end(); ++it)
							{
								const CharRGBA& rgbaColor = *it;
								fR += rgbaColor.R;
								fG += rgbaColor.G;
								fB += rgbaColor.B;
								fA += rgbaColor.A;
							}
						}
						else
						{
							for (std::vector<CharRGB>::iterator it = rgbPixels.begin(); it != rgbPixels.end(); ++it)
							{
								const CharRGB& rgbColor = *it;
								fR += rgbColor.R;
								fG += rgbColor.G;
								fB += rgbColor.B;
							}
						}

						if (realSampleCount != 1)
						{
							fR /= realSampleCount;
							fG /= realSampleCount;
							fB /= realSampleCount;
							fA /= realSampleCount;
						}

						if (m_bHasAlpha)
						{
							CharRGBA& dstColor = *(CharRGBA*)pDstPixel;
							dstColor.R = fR;
							dstColor.G = fG;
							dstColor.B = fB;
							dstColor.A = fA;
						}
						else
						{
							CharRGB& dstColor = *(CharRGB*)pDstPixel;
							dstColor.R = fR;
							dstColor.G = fG;
							dstColor.B = fB;
						}
					}
				}
			}

			CC_SAFE_DELETE_ARRAY(m_pData);
			m_pData = pPixelBuffer;
			m_nWidth = width;
			m_nHeight = height;

			return true;
		}

		return false;
	}

	const unsigned char* CCImage::getPixel(int x, int y, int pixelSize) const
	{
		if (0 <= x && x < m_nWidth && 0 <= y && y < m_nHeight)
		{
			int offset = (y * m_nWidth + x) * pixelSize;
			return m_pData + offset;
		}
		return NULL;
	}
}