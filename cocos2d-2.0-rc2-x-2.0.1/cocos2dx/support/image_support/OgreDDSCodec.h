/*
-----------------------------------------------------------------------------
This source file is part of OGRE
(Object-oriented Graphics Rendering Engine)
For the latest info, see http://www.ogre3d.org/

Copyright (c) 2000-2012 Torus Knot Software Ltd

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
-----------------------------------------------------------------------------
from ogre 1.8.0
*/
#ifndef __OgreDDSCodec_H__
#define __OgreDDSCodec_H__

#include "cocos2d.h"
//#include "OgreImageCodec.h"
namespace cocos2d {
#define OGRE_ENDIAN_BIG 0
#define OGRE_ENDIAN_LITTLE 1
#define OGRE_ENDIAN OGRE_ENDIAN_LITTLE
	
	enum PixelFormat
	{
		/// Unknown pixel format.
		PF_UNKNOWN = 0,
		/// 8-bit pixel format, all bits luminance.
		PF_L8 = 1,
		PF_BYTE_L = PF_L8,
		/// 16-bit pixel format, all bits luminance.
		PF_L16 = 2,
		PF_SHORT_L = PF_L16,
		/// 8-bit pixel format, all bits alpha.
		PF_A8 = 3,
		PF_BYTE_A = PF_A8,
		/// 8-bit pixel format, 4 bits alpha, 4 bits luminance.
		PF_A4L4 = 4,
		/// 2 byte pixel format, 1 byte luminance, 1 byte alpha
		PF_BYTE_LA = 5,
		/// 16-bit pixel format, 5 bits red, 6 bits green, 5 bits blue.
		PF_R5G6B5 = 6,
		/// 16-bit pixel format, 5 bits red, 6 bits green, 5 bits blue.
		PF_B5G6R5 = 7,
		/// 8-bit pixel format, 2 bits blue, 3 bits green, 3 bits red.
		PF_R3G3B2 = 31,
		/// 16-bit pixel format, 4 bits for alpha, red, green and blue.
		PF_A4R4G4B4 = 8,
		/// 16-bit pixel format, 5 bits for blue, green, red and 1 for alpha.
		PF_A1R5G5B5 = 9,
		/// 24-bit pixel format, 8 bits for red, green and blue.
		PF_R8G8B8 = 10,
		/// 24-bit pixel format, 8 bits for blue, green and red.
		PF_B8G8R8 = 11,
		/// 32-bit pixel format, 8 bits for alpha, red, green and blue.
		PF_A8R8G8B8 = 12,
		/// 32-bit pixel format, 8 bits for blue, green, red and alpha.
		PF_A8B8G8R8 = 13,
		/// 32-bit pixel format, 8 bits for blue, green, red and alpha.
		PF_B8G8R8A8 = 14,
		/// 32-bit pixel format, 8 bits for red, green, blue and alpha.
		PF_R8G8B8A8 = 28,
		/// 32-bit pixel format, 8 bits for red, 8 bits for green, 8 bits for blue
		/// like PF_A8R8G8B8, but alpha will get discarded
		PF_X8R8G8B8 = 26,
		/// 32-bit pixel format, 8 bits for blue, 8 bits for green, 8 bits for red
		/// like PF_A8B8G8R8, but alpha will get discarded
		PF_X8B8G8R8 = 27,
#if OGRE_ENDIAN == OGRE_ENDIAN_BIG
		/// 3 byte pixel format, 1 byte for red, 1 byte for green, 1 byte for blue
		PF_BYTE_RGB = PF_R8G8B8,
		/// 3 byte pixel format, 1 byte for blue, 1 byte for green, 1 byte for red
		PF_BYTE_BGR = PF_B8G8R8,
		/// 4 byte pixel format, 1 byte for blue, 1 byte for green, 1 byte for red and one byte for alpha
		PF_BYTE_BGRA = PF_B8G8R8A8,
		/// 4 byte pixel format, 1 byte for red, 1 byte for green, 1 byte for blue, and one byte for alpha
		PF_BYTE_RGBA = PF_R8G8B8A8,
#else
		/// 3 byte pixel format, 1 byte for red, 1 byte for green, 1 byte for blue
		PF_BYTE_RGB = PF_B8G8R8,
		/// 3 byte pixel format, 1 byte for blue, 1 byte for green, 1 byte for red
		PF_BYTE_BGR = PF_R8G8B8,
		/// 4 byte pixel format, 1 byte for blue, 1 byte for green, 1 byte for red and one byte for alpha
		PF_BYTE_BGRA = PF_A8R8G8B8,
		/// 4 byte pixel format, 1 byte for red, 1 byte for green, 1 byte for blue, and one byte for alpha
		PF_BYTE_RGBA = PF_A8B8G8R8,
#endif        
		/// 32-bit pixel format, 2 bits for alpha, 10 bits for red, green and blue.
		PF_A2R10G10B10 = 15,
		/// 32-bit pixel format, 10 bits for blue, green and red, 2 bits for alpha.
		PF_A2B10G10R10 = 16,
		/// DDS (DirectDraw Surface) DXT1 format
		PF_DXT1 = 17,
		/// DDS (DirectDraw Surface) DXT2 format
		PF_DXT2 = 18,
		/// DDS (DirectDraw Surface) DXT3 format
		PF_DXT3 = 19,
		/// DDS (DirectDraw Surface) DXT4 format
		PF_DXT4 = 20,
		/// DDS (DirectDraw Surface) DXT5 format
		PF_DXT5 = 21,
		// 16-bit pixel format, 16 bits (float) for red
		PF_FLOAT16_R = 32,
		// 48-bit pixel format, 16 bits (float) for red, 16 bits (float) for green, 16 bits (float) for blue
		PF_FLOAT16_RGB = 22,
		// 64-bit pixel format, 16 bits (float) for red, 16 bits (float) for green, 16 bits (float) for blue, 16 bits (float) for alpha
		PF_FLOAT16_RGBA = 23,
		// 32-bit pixel format, 32 bits (float) for red
		PF_FLOAT32_R = 33,
		// 96-bit pixel format, 32 bits (float) for red, 32 bits (float) for green, 32 bits (float) for blue
		PF_FLOAT32_RGB = 24,
		// 128-bit pixel format, 32 bits (float) for red, 32 bits (float) for green, 32 bits (float) for blue, 32 bits (float) for alpha
		PF_FLOAT32_RGBA = 25,
		// 32-bit, 2-channel s10e5 floating point pixel format, 16-bit green, 16-bit red
		PF_FLOAT16_GR = 35,
		// 64-bit, 2-channel floating point pixel format, 32-bit green, 32-bit red
		PF_FLOAT32_GR = 36,
		// Depth texture format
		PF_DEPTH = 29,
		// 64-bit pixel format, 16 bits for red, green, blue and alpha
		PF_SHORT_RGBA = 30,
		// 32-bit pixel format, 16-bit green, 16-bit red
		PF_SHORT_GR = 34,
		// 48-bit pixel format, 16 bits for red, green and blue
		PF_SHORT_RGB = 37,
		/// PVRTC (PowerVR) RGB 2 bpp
		PF_PVRTC_RGB2 = 38,
		/// PVRTC (PowerVR) RGBA 2 bpp
		PF_PVRTC_RGBA2 = 39,
		/// PVRTC (PowerVR) RGB 4 bpp
		PF_PVRTC_RGB4 = 40,
		/// PVRTC (PowerVR) RGBA 4 bpp
		PF_PVRTC_RGBA4 = 41,
		/// 8-bit pixel format, all bits red.
		PF_R8 = 42,
		/// 16-bit pixel format, 8 bits red, 8 bits green.
		PF_RG8 = 43,
		// Number of pixel formats currently defined
		PF_COUNT = 44
	};

	/**
     * Flags defining some on/off properties of pixel formats
     */
    enum PixelFormatFlags {
        // This format has an alpha channel
        PFF_HASALPHA        = 0x00000001,      
        // This format is compressed. This invalidates the values in elemBytes,
        // elemBits and the bit counts as these might not be fixed in a compressed format.
        PFF_COMPRESSED    = 0x00000002,
        // This is a floating point format
        PFF_FLOAT           = 0x00000004,         
        // This is a depth format (for depth textures)
        PFF_DEPTH           = 0x00000008,
        // Format is in native endian. Generally true for the 16, 24 and 32 bits
        // formats which can be represented as machine integers.
        PFF_NATIVEENDIAN    = 0x00000010,
        // This is an intensity format instead of a RGB one. The luminance
        // replaces R,G and B. (but not A)
        PFF_LUMINANCE       = 0x00000020
    };


	class CDDSImage
	{
		unsigned char*	m_pBuffer;
		//size_t			m_size;
	public:
		CDDSImage(): m_pBuffer(NULL), size(0){}
		~CDDSImage(){ 
			if (m_pBuffer)
			{
				delete m_pBuffer;
				m_pBuffer = NULL;
			}
		}

		/*size_t	GetSize(){ return size; }
		void	SetSize(size_t sz) { m_size = sz; }*/

		unsigned char*	GetBuffer(){ return m_pBuffer; }
		void			SetBuffer(unsigned char* aPBuf){ assert(!m_pBuffer); m_pBuffer = aPBuf; }

		unsigned int	depth, width, height;
		unsigned short	num_mipmaps;
		unsigned int	flags;
		PixelFormat		format;
		size_t			size;

		unsigned char*	AllocBuffer(size_t sz){ assert(!m_pBuffer); m_pBuffer = new unsigned char[sz]; return m_pBuffer;}
	};

	class MemStream{
		unsigned char* m_pBuf;
		unsigned char* m_pCur;
		size_t			m_sz;
	public:
		MemStream(unsigned char* aPBuf, size_t sz) : m_pBuf(aPBuf), m_pCur(aPBuf), m_sz(sz){}
		void read(void* aPBuf, size_t sz)
		{
			assert(aPBuf != NULL && sz >= 0);
			if (sz == 0) return;

			memcpy(aPBuf, m_pCur, sz);
			m_pCur += sz;
		}
        
        void xread(void** aPBuf, size_t sz)
        {
            assert(aPBuf != NULL && sz >= 0);
			if (sz == 0) return;
            
			*aPBuf = m_pCur;
			m_pCur += sz;
        }
        
		void skip(int cnt)
		{
			assert (m_pBuf <= m_pCur + cnt);
			m_pCur += cnt;
		}
	};

	// Forward declarations
    class ByteColourValue;
	class ColourValue;
	struct DXTColourBlock;
	struct DXTExplicitAlphaBlock;
	struct DXTInterpolatedAlphaBlock;

    /** Codec specialized in loading DDS (Direct Draw Surface) images.
	@remarks
		We implement our own codec here since we need to be able to keep DXT
		data compressed if the card supports it.
    */
    class CC_DLL DDSCodec
    {
    private:
    	void flipEndian(void * pData, size_t size, size_t count) const;
	    void flipEndian(void * pData, size_t size) const;

        void unpackDXTColourByte(PixelFormat pf, const DXTColourBlock& block, ByteColourValue* pCol) const;
		/// Unpack DXT alphas into array of 16 colour values
		void unpackDXTAlphaByte(const DXTExplicitAlphaBlock& block, ByteColourValue* pCol) const;
		/// Unpack DXT alphas into array of 16 colour values
		void unpackDXTAlphaByte(const DXTInterpolatedAlphaBlock& block, ByteColourValue* pCol) const;
        
		/// Unpack DXT colours into array of 16 colour values
		void unpackDXTColour(PixelFormat pf, const DXTColourBlock& block, ColourValue* pCol) const;
		/// Unpack DXT alphas into array of 16 colour values
		void unpackDXTAlpha(const DXTExplicitAlphaBlock& block, ColourValue* pCol) const;
		/// Unpack DXT alphas into array of 16 colour values
		void unpackDXTAlpha(const DXTInterpolatedAlphaBlock& block, ColourValue* pCol) const;

		/// Single registered codec instance
		static DDSCodec* msInstance;
	public:
        DDSCodec();
        virtual ~DDSCodec() { }

        PixelFormat convertFourCCFormat(unsigned int fourcc) const;
		PixelFormat convertPixelFormat(unsigned int rgbBits, unsigned int rMask,
                                       unsigned int gMask, unsigned int bMask, unsigned int aMask) const;
        /// @copydoc Codec::code
        //DataStreamPtr code(MemoryDataStreamPtr& input, CodecDataPtr& pData) const;
        /// @copydoc Codec::codeToFile
        //void codeToFile(MemoryDataStreamPtr& input, const String& outFileName, CodecDataPtr& pData) const;
        /// @copydoc Codec::decode
        bool decode(MemStream& input, CDDSImage& output) const;
		/// @copydoc Codec::magicNumberToFileExt
		//CCString magicNumberToFileExt(const char *magicNumberPtr, size_t maxbytes) const;
    };
	/** @} */
	/** @} */

} // namespace

#endif

