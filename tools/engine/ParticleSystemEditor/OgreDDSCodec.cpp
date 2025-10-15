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
*/

//#include "OgreStableHeaders.h"

//#include "OgreRoot.h"
//#include "OgreRenderSystem.h"
#include "stdafx.h"
#include "OgreDDSCodec.h"
//#include "OgreImage.h"
//#include "OgreException.h"

//#include "OgreLogManager.h"
//#include "OgreStringConverter.h"
#include "OgreBitwise.h"
#include <algorithm>

using namespace Ogre;

namespace cocos2d {
	typedef float Real;
	
	class ColourValue
	{
	public:
		static const ColourValue ZERO;

		explicit ColourValue( float red = 1.0f,
			float green = 1.0f,
			float blue = 1.0f,
			float alpha = 1.0f ) : r(red), g(green), b(blue), a(alpha)
		{ }

		// arithmetic operations
		inline ColourValue operator + ( const ColourValue& rkVector ) const
		{
			ColourValue kSum;

			kSum.r = r + rkVector.r;
			kSum.g = g + rkVector.g;
			kSum.b = b + rkVector.b;
			kSum.a = a + rkVector.a;

			return kSum;
		}

		inline friend ColourValue operator * (const float fScalar, const ColourValue& rkVector )
		{
			ColourValue kProd;

			kProd.r = fScalar * rkVector.r;
			kProd.g = fScalar * rkVector.g;
			kProd.b = fScalar * rkVector.b;
			kProd.a = fScalar * rkVector.a;

			return kProd;
		}

		inline ColourValue operator / (const float fScalar ) const
		{
			ASSERT( fScalar != 0.0 );

			ColourValue kDiv;

			float fInv = 1.0f / fScalar;
			kDiv.r = r * fInv;
			kDiv.g = g * fInv;
			kDiv.b = b * fInv;
			kDiv.a = a * fInv;

			return kDiv;
		}

		float r,g,b,a;
	};

    class ByteColourValue
    {
    public:
        unsigned char r,g,b,a;
    };
    
	const ColourValue ColourValue::ZERO = ColourValue(0.0,0.0,0.0,0.0);

	enum ImageFlags
	{
		IF_COMPRESSED = 0x00000001,
		IF_CUBEMAP    = 0x00000002,
		IF_3D_TEXTURE = 0x00000004
	};

	enum PixelComponentType
	{
		PCT_BYTE = 0,    /// Byte per component (8 bit fixed 0.0..1.0)
		PCT_SHORT = 1,   /// Short per component (16 bit fixed 0.0..1.0))
		PCT_FLOAT16 = 2, /// 16 bit float per component
		PCT_FLOAT32 = 3, /// 32 bit float per component
		PCT_COUNT = 4    /// Number of pixel types
	};
	//-----------------------------------------------------------------------
    /**
    * A record that describes a pixel format in detail.
    */
    struct PixelFormatDescription {
        /* Name of the format, as in the enum */
        const char *name;
        /* Number of bytes one element (colour value) takes. */
        unsigned char elemBytes;
        /* Pixel format flags, see enum PixelFormatFlags for the bit field
        * definitions
        */
        unsigned int flags;
        /** Component type
         */
        PixelComponentType componentType;
        /** Component count
         */
        unsigned char componentCount;
        /* Number of bits for red(or luminance), green, blue, alpha
        */
        unsigned char rbits,gbits,bbits,abits; /*, ibits, dbits, ... */

        /* Masks and shifts as used by packers/unpackers */
        unsigned int rmask, gmask, bmask, amask;
        unsigned char rshift, gshift, bshift, ashift;
    };
    //-----------------------------------------------------------------------
    /** Pixel format database */
    PixelFormatDescription _pixelFormats[PF_COUNT] = {
	//-----------------------------------------------------------------------
        {"PF_UNKNOWN",
        /* Bytes per element */
        0,
        /* Flags */
        0,
        /* Component type and count */
        PCT_BYTE, 0,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_L8",
        /* Bytes per element */
        1,
        /* Flags */
        PFF_LUMINANCE | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 1,
        /* rbits, gbits, bbits, abits */
        8, 0, 0, 0,
        /* Masks and shifts */
        0xFF, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_L16",
        /* Bytes per element */
        2,
        /* Flags */
        PFF_LUMINANCE | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_SHORT, 1,
        /* rbits, gbits, bbits, abits */
        16, 0, 0, 0,
        /* Masks and shifts */
        0xFFFF, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_A8",
        /* Bytes per element */
        1,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 1,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 8,
        /* Masks and shifts */
        0, 0, 0, 0xFF, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_A4L4",
        /* Bytes per element */
        1,
        /* Flags */
        PFF_HASALPHA | PFF_LUMINANCE | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 2,
        /* rbits, gbits, bbits, abits */
        4, 0, 0, 4,
        /* Masks and shifts */
        0x0F, 0, 0, 0xF0, 0, 0, 0, 4
        },
	//-----------------------------------------------------------------------
        {"PF_BYTE_LA",
        /* Bytes per element */
        2,
        /* Flags */
        PFF_HASALPHA | PFF_LUMINANCE,
        /* Component type and count */
        PCT_BYTE, 2,
        /* rbits, gbits, bbits, abits */
        8, 0, 0, 8,
        /* Masks and shifts */
        0,0,0,0,0,0,0,0
        },
	//-----------------------------------------------------------------------
        {"PF_R5G6B5",
        /* Bytes per element */
        2,
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        5, 6, 5, 0,
        /* Masks and shifts */
        0xF800, 0x07E0, 0x001F, 0,
        11, 5, 0, 0
        },
	//-----------------------------------------------------------------------
		{"PF_B5G6R5",
        /* Bytes per element */
        2,
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        5, 6, 5, 0,
        /* Masks and shifts */
        0x001F, 0x07E0, 0xF800, 0,
        0, 5, 11, 0
        },
	//-----------------------------------------------------------------------
        {"PF_A4R4G4B4",
        /* Bytes per element */
        2,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        4, 4, 4, 4,
        /* Masks and shifts */
        0x0F00, 0x00F0, 0x000F, 0xF000,
        8, 4, 0, 12
        },
	//-----------------------------------------------------------------------
        {"PF_A1R5G5B5",
        /* Bytes per element */
        2,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        5, 5, 5, 1,
        /* Masks and shifts */
        0x7C00, 0x03E0, 0x001F, 0x8000,
        10, 5, 0, 15,
        },
	//-----------------------------------------------------------------------
        {"PF_R8G8B8",
        /* Bytes per element */
        3,  // 24 bit integer -- special
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        8, 8, 8, 0,
        /* Masks and shifts */
        0xFF0000, 0x00FF00, 0x0000FF, 0,
        16, 8, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_B8G8R8",
        /* Bytes per element */
        3,  // 24 bit integer -- special
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        8, 8, 8, 0,
        /* Masks and shifts */
        0x0000FF, 0x00FF00, 0xFF0000, 0,
        0, 8, 16, 0
        },
	//-----------------------------------------------------------------------
        {"PF_A8R8G8B8",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        8, 8, 8, 8,
        /* Masks and shifts */
        0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000,
        16, 8, 0, 24
        },
	//-----------------------------------------------------------------------
        {"PF_A8B8G8R8",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        8, 8, 8, 8,
        /* Masks and shifts */
        0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000,
        0, 8, 16, 24,
        },
	//-----------------------------------------------------------------------
        {"PF_B8G8R8A8",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        8, 8, 8, 8,
        /* Masks and shifts */
        0x0000FF00, 0x00FF0000, 0xFF000000, 0x000000FF,
        8, 16, 24, 0
        },
	//-----------------------------------------------------------------------
        {"PF_A2R10G10B10",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        10, 10, 10, 2,
        /* Masks and shifts */
        0x3FF00000, 0x000FFC00, 0x000003FF, 0xC0000000,
        20, 10, 0, 30
        },
	//-----------------------------------------------------------------------
        {"PF_A2B10G10R10",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        10, 10, 10, 2,
        /* Masks and shifts */
        0x000003FF, 0x000FFC00, 0x3FF00000, 0xC0000000,
        0, 10, 20, 30
        },
	//-----------------------------------------------------------------------
        {"PF_DXT1",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED | PFF_HASALPHA,
        /* Component type and count */
        PCT_BYTE, 3, // No alpha
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_DXT2",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED | PFF_HASALPHA,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_DXT3",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED | PFF_HASALPHA,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_DXT4",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED | PFF_HASALPHA,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_DXT5",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED | PFF_HASALPHA,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_FLOAT16_RGB",
        /* Bytes per element */
        6,
        /* Flags */
        PFF_FLOAT,
        /* Component type and count */
        PCT_FLOAT16, 3,
        /* rbits, gbits, bbits, abits */
        16, 16, 16, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_FLOAT16_RGBA",
        /* Bytes per element */
        8,
        /* Flags */
        PFF_FLOAT | PFF_HASALPHA,
        /* Component type and count */
        PCT_FLOAT16, 4,
        /* rbits, gbits, bbits, abits */
        16, 16, 16, 16,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_FLOAT32_RGB",
        /* Bytes per element */
        12,
        /* Flags */
        PFF_FLOAT,
        /* Component type and count */
        PCT_FLOAT32, 3,
        /* rbits, gbits, bbits, abits */
        32, 32, 32, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_FLOAT32_RGBA",
        /* Bytes per element */
        16,
        /* Flags */
        PFF_FLOAT | PFF_HASALPHA,
        /* Component type and count */
        PCT_FLOAT32, 4,
        /* rbits, gbits, bbits, abits */
        32, 32, 32, 32,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_X8R8G8B8",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        8, 8, 8, 0,
        /* Masks and shifts */
        0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000,
        16, 8, 0, 24
        },
	//-----------------------------------------------------------------------
        {"PF_X8B8G8R8",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        8, 8, 8, 0,
        /* Masks and shifts */
        0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000,
        0, 8, 16, 24
        },
	//-----------------------------------------------------------------------
        {"PF_R8G8B8A8",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_HASALPHA | PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        8, 8, 8, 8,
        /* Masks and shifts */
        0xFF000000, 0x00FF0000, 0x0000FF00, 0x000000FF,
        24, 16, 8, 0
        },
	//-----------------------------------------------------------------------
		{"PF_DEPTH",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_DEPTH,
        /* Component type and count */
        PCT_FLOAT32, 1, // ?
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
		0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
		{"PF_SHORT_RGBA",
		/* Bytes per element */
        8,
        /* Flags */
        PFF_HASALPHA,
        /* Component type and count */
        PCT_SHORT, 4,
        /* rbits, gbits, bbits, abits */
        16, 16, 16, 16,
        /* Masks and shifts */
		0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_R3G3B2",
        /* Bytes per element */
        1,
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        3, 3, 2, 0,
        /* Masks and shifts */
        0xE0, 0x1C, 0x03, 0,
        5, 2, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_FLOAT16_R",
        /* Bytes per element */
        2,
        /* Flags */
        PFF_FLOAT,
        /* Component type and count */
        PCT_FLOAT16, 1,
        /* rbits, gbits, bbits, abits */
        16, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_FLOAT32_R",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_FLOAT,
        /* Component type and count */
        PCT_FLOAT32, 1,
        /* rbits, gbits, bbits, abits */
        32, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_SHORT_GR",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_SHORT, 2,
        /* rbits, gbits, bbits, abits */
        16, 16, 0, 0,
        /* Masks and shifts */
        0x0000FFFF, 0xFFFF0000, 0, 0, 
		0, 16, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_FLOAT16_GR",
        /* Bytes per element */
        4,
        /* Flags */
        PFF_FLOAT,
        /* Component type and count */
        PCT_FLOAT16, 2,
        /* rbits, gbits, bbits, abits */
        16, 16, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
        {"PF_FLOAT32_GR",
        /* Bytes per element */
        8,
        /* Flags */
        PFF_FLOAT,
        /* Component type and count */
        PCT_FLOAT32, 2,
        /* rbits, gbits, bbits, abits */
        32, 32, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
	//-----------------------------------------------------------------------
		{"PF_SHORT_RGB",
		/* Bytes per element */
        6,
        /* Flags */
        0,
        /* Component type and count */
        PCT_SHORT, 3,
        /* rbits, gbits, bbits, abits */
        16, 16, 16, 0,
        /* Masks and shifts */
		0, 0, 0, 0, 0, 0, 0, 0
        },
    //-----------------------------------------------------------------------
		{"PF_PVRTC_RGB2",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
    //-----------------------------------------------------------------------
		{"PF_PVRTC_RGBA2",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED | PFF_HASALPHA,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
    //-----------------------------------------------------------------------
		{"PF_PVRTC_RGB4",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED,
        /* Component type and count */
        PCT_BYTE, 3,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
    //-----------------------------------------------------------------------
		{"PF_PVRTC_RGBA4",
        /* Bytes per element */
        0,
        /* Flags */
        PFF_COMPRESSED | PFF_HASALPHA,
        /* Component type and count */
        PCT_BYTE, 4,
        /* rbits, gbits, bbits, abits */
        0, 0, 0, 0,
        /* Masks and shifts */
        0, 0, 0, 0, 0, 0, 0, 0
        },
    //-----------------------------------------------------------------------
        {"PF_R8",
        /* Bytes per element */
        1,
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 1,
        /* rbits, gbits, bbits, abits */
        8, 0, 0, 0,
        /* Masks and shifts */
        0xFF0000, 0, 0, 0,
        0, 0, 0, 0
        },
    //-----------------------------------------------------------------------
        {"PF_RG8",
        /* Bytes per element */
        2,
        /* Flags */
        PFF_NATIVEENDIAN,
        /* Component type and count */
        PCT_BYTE, 2,
        /* rbits, gbits, bbits, abits */
        8, 8, 0, 0,
        /* Masks and shifts */
        0xFF0000, 0x00FF00, 0, 0,
        8, 0, 0, 0
        },

    };

	static inline const PixelFormatDescription &getDescriptionFor(const PixelFormat fmt)
	{
		const int ord = (int)fmt;
		ASSERT(ord>=0 && ord<PF_COUNT);

		return _pixelFormats[ord];
	}

	bool isCompressed(PixelFormat pf)
	{
		const int ord = (int)pf;
		ASSERT(ord>=0 && ord<PF_COUNT);

		return (_pixelFormats[ord].flags & PFF_COMPRESSED) > 0;
	}

	size_t getNumElemBytes(PixelFormat format)
	{
		const int ord = (int)format;
		ASSERT(ord>=0 && ord<PF_COUNT);

		return _pixelFormats[ord].elemBytes;
	}
	size_t getNumElemBits( PixelFormat format )
	{
		return getDescriptionFor(format).elemBytes * 8;
	}

	void getBitMasks(PixelFormat format, unsigned int rgba[4])
	{
		const PixelFormatDescription &des = getDescriptionFor(format);
		rgba[0] = des.rmask;
		rgba[1] = des.gmask;
		rgba[2] = des.bmask;
		rgba[3] = des.amask;
	}
	void getBitDepths(PixelFormat format, int rgba[4])
	{
		const PixelFormatDescription &des = getDescriptionFor(format);
		rgba[0] = des.rbits;
		rgba[1] = des.gbits;
		rgba[2] = des.bbits;
		rgba[3] = des.abits;
	}

	size_t getMemorySize(size_t width, size_t height, size_t depth, PixelFormat format)
	{
		if(isCompressed(format))
		{
			switch(format)
			{
				// DXT formats work by dividing the image into 4x4 blocks, then encoding each
				// 4x4 block with a certain number of bytes. 
			case PF_DXT1:
				return ((width+3)/4)*((height+3)/4)*8 * depth;
			case PF_DXT2:
			case PF_DXT3:
			case PF_DXT4:
			case PF_DXT5:
				return ((width+3)/4)*((height+3)/4)*16 * depth;

				// Size calculations from the PVRTC OpenGL extension spec
				// http://www.khronos.org/registry/gles/extensions/IMG/IMG_texture_compression_pvrtc.txt
				// Basically, 32 bytes is the minimum texture size.  Smaller textures are padded up to 32 bytes
			case PF_PVRTC_RGB2:
			case PF_PVRTC_RGBA2:
				ASSERT(depth == 1);
				return (std::max<int>((int)width, 16) * std::max<int>((int)height, 8) * 2 + 7) / 8;
			case PF_PVRTC_RGB4:
			case PF_PVRTC_RGBA4:
				ASSERT(depth == 1);
				return (std::max<int>((int)width, 8) * std::max<int>((int)height, 8) * 4 + 7) / 8;
			default:
				ASSERT(false && "Invalid compressed pixel format PixelUtil::getMemorySize");
			}
		}
		else
		{
			const int ord = (int)format;
			ASSERT(ord>=0 && ord<PF_COUNT);

			return width*height*depth*_pixelFormats[ord].elemBytes;
		}
		return 0;
	}

	size_t calculateSize(size_t mipmaps, size_t faces, size_t width, size_t height, size_t depth, 
		PixelFormat format)
	{
		size_t size = 0;
		for(size_t mip=0; mip<=mipmaps; ++mip)
		{
			size += getMemorySize(width, height, depth, format)*faces; 
			if(width!=1) width /= 2;
			if(height!=1) height /= 2;
			if(depth!=1) depth /= 2;
		}
		return size;
	}

	//////////////////////////////////////////////////////////////////////////
	//-----------------------------------------------------------------------
    /*************************************************************************
    * Pixel packing/unpacking utilities
    */
    
    //-----------------------------------------------------------------------
    void packColour(const float r, const float g, const float b, const float a, const PixelFormat pf,  void* dest)
    {
        // Catch-it-all here
        const PixelFormatDescription &des = getDescriptionFor(pf);
        if(des.flags & PFF_NATIVEENDIAN) {
            // Do the packing
            //std::cerr << dest << " " << r << " " << g <<  " " << b << " " << a << std::endl;
            const unsigned int value = ((Bitwise::floatToFixed(r, des.rbits)<<des.rshift) & des.rmask) |
                ((Bitwise::floatToFixed(g, des.gbits)<<des.gshift) & des.gmask) |
                ((Bitwise::floatToFixed(b, des.bbits)<<des.bshift) & des.bmask) |
                ((Bitwise::floatToFixed(a, des.abits)<<des.ashift) & des.amask);
            // And write to memory
            Bitwise::intWrite(dest, des.elemBytes, value);
        } else {
            switch(pf)
            {
            case PF_FLOAT32_R:
                ((float*)dest)[0] = r;
                break;
			case PF_FLOAT32_GR:
				((float*)dest)[0] = g;
				((float*)dest)[1] = r;
				break;
            case PF_FLOAT32_RGB:
                ((float*)dest)[0] = r;
                ((float*)dest)[1] = g;
                ((float*)dest)[2] = b;
                break;
            case PF_FLOAT32_RGBA:
                ((float*)dest)[0] = r;
                ((float*)dest)[1] = g;
                ((float*)dest)[2] = b;
                ((float*)dest)[3] = a;
                break;
            case PF_FLOAT16_R:
                ((unsigned short*)dest)[0] = Bitwise::floatToHalf(r);
                break;
			case PF_FLOAT16_GR:
				((unsigned short*)dest)[0] = Bitwise::floatToHalf(g);
				((unsigned short*)dest)[1] = Bitwise::floatToHalf(r);
				break;
            case PF_FLOAT16_RGB:
                ((unsigned short*)dest)[0] = Bitwise::floatToHalf(r);
                ((unsigned short*)dest)[1] = Bitwise::floatToHalf(g);
                ((unsigned short*)dest)[2] = Bitwise::floatToHalf(b);
                break;
            case PF_FLOAT16_RGBA:
                ((unsigned short*)dest)[0] = Bitwise::floatToHalf(r);
                ((unsigned short*)dest)[1] = Bitwise::floatToHalf(g);
                ((unsigned short*)dest)[2] = Bitwise::floatToHalf(b);
                ((unsigned short*)dest)[3] = Bitwise::floatToHalf(a);
                break;
            case PF_SHORT_RGB:
				((unsigned short*)dest)[0] = (unsigned short)Bitwise::floatToFixed(r, 16);
                ((unsigned short*)dest)[1] = (unsigned short)Bitwise::floatToFixed(g, 16);
                ((unsigned short*)dest)[2] = (unsigned short)Bitwise::floatToFixed(b, 16);
                break;
			case PF_SHORT_RGBA:
				((unsigned short*)dest)[0] = (unsigned short)Bitwise::floatToFixed(r, 16);
                ((unsigned short*)dest)[1] = (unsigned short)Bitwise::floatToFixed(g, 16);
                ((unsigned short*)dest)[2] = (unsigned short)Bitwise::floatToFixed(b, 16);
                ((unsigned short*)dest)[3] = (unsigned short)Bitwise::floatToFixed(a, 16);
				break;
			case PF_BYTE_LA:
				((unsigned char*)dest)[0] = (unsigned char)Bitwise::floatToFixed(r, 8);
                ((unsigned char*)dest)[1] = (unsigned char)Bitwise::floatToFixed(a, 8);
				break;
            default:
                // Not yet supported
               /* OGRE_EXCEPT(
                    Exception::ERR_NOT_IMPLEMENTED,
                    "pack to "+getFormatName(pf)+" not implemented",
                    "PixelUtil::packColour");*/
				ASSERT(false && "PixelUtil::packColour not implemented format");
                break;
            }
        }
    }

	
	//-----------------------------------------------------------------------
	void packColour(const unsigned char r, const unsigned char g, const unsigned char b, const unsigned char a, const PixelFormat pf,  void* dest)
	{
		const PixelFormatDescription &des = getDescriptionFor(pf);
		if(des.flags & PFF_NATIVEENDIAN) {
			// Shortcut for integer formats packing
			unsigned int value = ((Bitwise::fixedToFixed(r, 8, des.rbits)<<des.rshift) & des.rmask) |
				((Bitwise::fixedToFixed(g, 8, des.gbits)<<des.gshift) & des.gmask) |
				((Bitwise::fixedToFixed(b, 8, des.bbits)<<des.bshift) & des.bmask) |
				((Bitwise::fixedToFixed(a, 8, des.abits)<<des.ashift) & des.amask);
			// And write to memory
			Bitwise::intWrite(dest, des.elemBytes, value);
		} else {
			// Convert to float
			packColour((float)r/255.0f,(float)g/255.0f,(float)b/255.0f,(float)a/255.0f, pf, dest);
		}
	}
	
	void packColour(const ColourValue &colour, const PixelFormat pf,  void* dest)
	{
		packColour(colour.r, colour.g, colour.b, colour.a, pf, dest);
	}
    
    void packColourByte(const ByteColourValue &colour, const PixelFormat pf,  void* dest)
	{
		
	}

	//-----------------------------------------------------------------------
	void unpackColour(float *r, float *g, float *b, float *a,
		PixelFormat pf,  const void* src)
	{
		const PixelFormatDescription &des = getDescriptionFor(pf);
		if(des.flags & PFF_NATIVEENDIAN) {
			// Shortcut for integer formats unpacking
			const unsigned int value = Bitwise::intRead(src, des.elemBytes);
			if(des.flags & PFF_LUMINANCE)
			{
				// Luminance format -- only rbits used
				*r = *g = *b = Bitwise::fixedToFloat(
					(value & des.rmask)>>des.rshift, des.rbits);
			}
			else
			{
				*r = Bitwise::fixedToFloat((value & des.rmask)>>des.rshift, des.rbits);
				*g = Bitwise::fixedToFloat((value & des.gmask)>>des.gshift, des.gbits);
				*b = Bitwise::fixedToFloat((value & des.bmask)>>des.bshift, des.bbits);
			}
			if(des.flags & PFF_HASALPHA)
			{
				*a = Bitwise::fixedToFloat((value & des.amask)>>des.ashift, des.abits);
			}
			else
			{
				*a = 1.0f; // No alpha, default a component to full
			}
		} else {
			switch(pf)
			{
			case PF_FLOAT32_R:
				*r = *g = *b = ((float*)src)[0];
				*a = 1.0f;
				break;
			case PF_FLOAT32_GR:
				*g = ((float*)src)[0];
				*r = *b = ((float*)src)[1];
				*a = 1.0f;
				break;
			case PF_FLOAT32_RGB:
				*r = ((float*)src)[0];
				*g = ((float*)src)[1];
				*b = ((float*)src)[2];
				*a = 1.0f;
				break;
			case PF_FLOAT32_RGBA:
				*r = ((float*)src)[0];
				*g = ((float*)src)[1];
				*b = ((float*)src)[2];
				*a = ((float*)src)[3];
				break;
			case PF_FLOAT16_R:
				*r = *g = *b = Bitwise::halfToFloat(((unsigned short*)src)[0]);
				*a = 1.0f;
				break;
			case PF_FLOAT16_GR:
				*g = Bitwise::halfToFloat(((unsigned short*)src)[0]);
				*r = *b = Bitwise::halfToFloat(((unsigned short*)src)[1]);
				*a = 1.0f;
				break;
			case PF_FLOAT16_RGB:
				*r = Bitwise::halfToFloat(((unsigned short*)src)[0]);
				*g = Bitwise::halfToFloat(((unsigned short*)src)[1]);
				*b = Bitwise::halfToFloat(((unsigned short*)src)[2]);
				*a = 1.0f;
				break;
			case PF_FLOAT16_RGBA:
				*r = Bitwise::halfToFloat(((unsigned short*)src)[0]);
				*g = Bitwise::halfToFloat(((unsigned short*)src)[1]);
				*b = Bitwise::halfToFloat(((unsigned short*)src)[2]);
				*a = Bitwise::halfToFloat(((unsigned short*)src)[3]);
				break;
			case PF_SHORT_RGB:
				*r = Bitwise::fixedToFloat(((unsigned short*)src)[0], 16);
				*g = Bitwise::fixedToFloat(((unsigned short*)src)[1], 16);
				*b = Bitwise::fixedToFloat(((unsigned short*)src)[2], 16);
				*a = 1.0f;
				break;
			case PF_SHORT_RGBA:
				*r = Bitwise::fixedToFloat(((unsigned short*)src)[0], 16);
				*g = Bitwise::fixedToFloat(((unsigned short*)src)[1], 16);
				*b = Bitwise::fixedToFloat(((unsigned short*)src)[2], 16);
				*a = Bitwise::fixedToFloat(((unsigned short*)src)[3], 16);
				break;
			case PF_BYTE_LA:
				*r = *g = *b = Bitwise::fixedToFloat(((unsigned char*)src)[0], 8);
				*a = Bitwise::fixedToFloat(((unsigned char*)src)[1], 8);
				break;
			default:
				// Not yet supported
				/*OGRE_EXCEPT(Exception::ERR_NOT_IMPLEMENTED,
					"unpack from "+getFormatName(pf)+" not implemented",
					"PixelUtil::unpackColour");*/
				ASSERT(false && "unpackColour not support");
				break;
			}
		}
	}
	//-----------------------------------------------------------------------
	void unpackColour(ColourValue *colour, PixelFormat pf,  const void* src)
	{
		unpackColour(&colour->r, &colour->g, &colour->b, &colour->a, pf, src);
	}
    
    //-----------------------------------------------------------------------
    void unpackColour(ByteColourValue *colour, PixelFormat pf,  const void* src)
	{
		ASSERT(pf == PF_R5G6B5);

#pragma pack(1)
        union color_t{
            struct  {
                unsigned short b:5;
                unsigned short g:6;
                unsigned short r:5;
            };
            unsigned short value;
        };
#pragma pack()
        color_t v;
        v.value = *((unsigned short*)src);        

        colour->r = v.r<<3;
        colour->g = v.g<<2;
        colour->b = v.b<<3;

	}

	//-----------------------------------------------------------------------
	void unpackColour(unsigned char *r, unsigned char *g, unsigned char *b, unsigned char *a, PixelFormat pf,  const void* src)
	{
		const PixelFormatDescription &des = getDescriptionFor(pf);
		if(des.flags & PFF_NATIVEENDIAN) {
			// Shortcut for integer formats unpacking
			const unsigned int value = Bitwise::intRead(src, des.elemBytes);
			if(des.flags & PFF_LUMINANCE)
			{
				// Luminance format -- only rbits used
				*r = *g = *b = (unsigned char)Bitwise::fixedToFixed(
					(value & des.rmask)>>des.rshift, des.rbits, 8);
			}
			else
			{
				*r = (unsigned char)Bitwise::fixedToFixed((value & des.rmask)>>des.rshift, des.rbits, 8);
				*g = (unsigned char)Bitwise::fixedToFixed((value & des.gmask)>>des.gshift, des.gbits, 8);
				*b = (unsigned char)Bitwise::fixedToFixed((value & des.bmask)>>des.bshift, des.bbits, 8);
			}
			if(des.flags & PFF_HASALPHA)
			{
				*a = (unsigned char)Bitwise::fixedToFixed((value & des.amask)>>des.ashift, des.abits, 8);
			}
			else
			{
				*a = 255; // No alpha, default a component to full
			}
		} else {
			// Do the operation with the more generic floating point
			float rr = 0, gg = 0, bb = 0, aa = 0;
			unpackColour(&rr,&gg,&bb,&aa, pf, src);
			*r = (unsigned char)Bitwise::floatToFixed(rr, 8);
			*g = (unsigned char)Bitwise::floatToFixed(gg, 8);
			*b = (unsigned char)Bitwise::floatToFixed(bb, 8);
			*a = (unsigned char)Bitwise::floatToFixed(aa, 8);
		}
	}
	// Internal DDS structure definitions
#define FOURCC(c0, c1, c2, c3) (c0 | (c1 << 8) | (c2 << 16) | (c3 << 24))
	
#ifdef WIN32
#pragma pack (push, 1)
#else
#pragma pack (1)
#endif

	// Nested structure
	struct DDSPixelFormat
	{
		unsigned int size;
		unsigned int flags;
		unsigned int fourCC;
		unsigned int rgbBits;
		unsigned int redMask;
		unsigned int greenMask;
		unsigned int blueMask;
		unsigned int alphaMask;
	};
	
	// Nested structure
	struct DDSCaps
	{
		unsigned int caps1;
		unsigned int caps2;
		unsigned int reserved[2];
	};
	// Main header, note preceded by 'DDS '
	struct DDSHeader
	{
		unsigned int size;		
		unsigned int flags;
		unsigned int height;
		unsigned int width;
		unsigned int sizeOrPitch;
		unsigned int depth;
		unsigned int mipMapCount;
		unsigned int reserved1[11];
		DDSPixelFormat pixelFormat;
		DDSCaps caps;
		unsigned int reserved2;
	};

	// An 8-byte DXT colour block, represents a 4x4 texel area. Used by all DXT formats
	struct DXTColourBlock
	{
		// 2 colour ranges
		unsigned short colour_0;
		unsigned short colour_1;
		// 16 2-bit indexes, each byte here is one row
		unsigned char indexRow[4];
	};
	// An 8-byte DXT explicit alpha block, represents a 4x4 texel area. Used by DXT2/3
	struct DXTExplicitAlphaBlock
	{
		// 16 4-bit values, each 16-bit value is one row
		unsigned short alphaRow[4];
	};
	// An 8-byte DXT interpolated alpha block, represents a 4x4 texel area. Used by DXT4/5
	struct DXTInterpolatedAlphaBlock
	{
		// 2 alpha ranges
		unsigned char alpha_0;
		unsigned char alpha_1;
		// 16 3-bit indexes. Unfortunately 3 bits doesn't map too well to row bytes
		// so just stored raw
		unsigned char indexes[6];
	};
	
#ifdef WIN32
#pragma pack (pop)
#else
#pragma pack ()
#endif

	const unsigned int DDS_MAGIC = FOURCC('D', 'D', 'S', ' ');
	const unsigned int DDS_PIXELFORMAT_SIZE = 8 * sizeof(unsigned int);
	const unsigned int DDS_CAPS_SIZE = 4 * sizeof(unsigned int);
	const unsigned int DDS_HEADER_SIZE = 19 * sizeof(unsigned int) + DDS_PIXELFORMAT_SIZE + DDS_CAPS_SIZE;

	const unsigned int DDSD_CAPS = 0x00000001;
	const unsigned int DDSD_HEIGHT = 0x00000002;
	const unsigned int DDSD_WIDTH = 0x00000004;
	const unsigned int DDSD_PITCH = 0x00000008;
	const unsigned int DDSD_PIXELFORMAT = 0x00001000;
	const unsigned int DDSD_MIPMAPCOUNT = 0x00020000;
	const unsigned int DDSD_LINEARSIZE = 0x00080000;
	const unsigned int DDSD_DEPTH = 0x00800000;
	const unsigned int DDPF_ALPHAPIXELS = 0x00000001;
	const unsigned int DDPF_FOURCC = 0x00000004;
	const unsigned int DDPF_RGB = 0x00000040;
	const unsigned int DDSCAPS_COMPLEX = 0x00000008;
	const unsigned int DDSCAPS_TEXTURE = 0x00001000;
	const unsigned int DDSCAPS_MIPMAP = 0x00400000;
	const unsigned int DDSCAPS2_CUBEMAP = 0x00000200;
	const unsigned int DDSCAPS2_CUBEMAP_POSITIVEX = 0x00000400;
	const unsigned int DDSCAPS2_CUBEMAP_NEGATIVEX = 0x00000800;
	const unsigned int DDSCAPS2_CUBEMAP_POSITIVEY = 0x00001000;
	const unsigned int DDSCAPS2_CUBEMAP_NEGATIVEY = 0x00002000;
	const unsigned int DDSCAPS2_CUBEMAP_POSITIVEZ = 0x00004000;
	const unsigned int DDSCAPS2_CUBEMAP_NEGATIVEZ = 0x00008000;
	const unsigned int DDSCAPS2_VOLUME = 0x00200000;

	// Special FourCC codes
	const unsigned int D3DFMT_R16F			= 111;
	const unsigned int D3DFMT_G16R16F			= 112;
	const unsigned int D3DFMT_A16B16G16R16F	= 113;
	const unsigned int D3DFMT_R32F            = 114;
	const unsigned int D3DFMT_G32R32F         = 115;
	const unsigned int D3DFMT_A32B32G32R32F   = 116;


	//---------------------------------------------------------------------
	DDSCodec* DDSCodec::msInstance = 0;
	//---------------------------------------------------------------------
	
    DDSCodec::DDSCodec()
    { 
    }
    //---------------------------------------------------------------------
    /*DataStreamPtr DDSCodec::code(MemoryDataStreamPtr& input, Codec::CodecDataPtr& pData) const
    {        
		OGRE_EXCEPT(Exception::ERR_NOT_IMPLEMENTED,
			"DDS encoding not supported",
			"DDSCodec::code" ) ;
    }*/
    //---------------------------------------------------------------------
    void DDSCodec::codeToFile(LPCTSTR outFileName, CDDSImage* pData) const
    {
		// Unwrap codecDataPtr - data is cleaned by calling function
		CDDSImage* imgData = pData;  


	//	// Check size for cube map faces
		bool isCubeMap = (imgData->size == 
			calculateSize(imgData->num_mipmaps, 6, imgData->width, 
			imgData->height, imgData->depth, imgData->format));

		// Establish texture attributes
		bool isVolume = (imgData->depth > 1);		
		bool isFloat32r = (imgData->format == PF_FLOAT32_R);
		bool hasAlpha = false;
		bool notImplemented = false;
		//String notImplementedString = "";

		// Check for all the 'not implemented' conditions
		if (imgData->num_mipmaps != 0)
		{
			// No mip map functionality yet
			notImplemented = true;
			//notImplementedString += " mipmaps";
		}

		if ((isVolume == true)&&(imgData->width != imgData->height))
		{
			// Square textures only
			notImplemented = true;
			//notImplementedString += " non square textures";
		}

		unsigned int size = 1;
		while (size < imgData->width)
		{
			size <<= 1;
		}
		if (size != imgData->width)
		{
			// Power two textures only
			notImplemented = true;
			//notImplementedString += " non power two textures";
		}

		switch(imgData->format)
		{
		case PF_A8R8G8B8:
		case PF_X8R8G8B8:
		case PF_R8G8B8:
		case PF_FLOAT32_R:
			break;
		default:
			// No crazy FOURCC or 565 et al. file formats at this stage
			notImplemented = true;
			//notImplementedString = " unsupported pixel format";
			break;
		}		



		// Except if any 'not implemented' conditions were met
		if (notImplemented)
		{
			//OGRE_EXCEPT(Exception::ERR_NOT_IMPLEMENTED,
			//	"DDS encoding for" + notImplementedString + " not supported",
			//	"DDSCodec::codeToFile" ) ;
		}
		else
		{
			// Build header and write to disk

			// Variables for some DDS header flags
			unsigned int ddsHeaderFlags = 0;			
			unsigned int ddsHeaderRgbBits = 0;
			unsigned int ddsHeaderSizeOrPitch = 0;
			unsigned int ddsHeaderCaps1 = 0;
			unsigned int ddsHeaderCaps2 = 0;
			unsigned int ddsMagic = DDS_MAGIC;

			// Initalise the header flags
			ddsHeaderFlags = (isVolume) ? DDSD_CAPS|DDSD_WIDTH|DDSD_HEIGHT|DDSD_DEPTH|DDSD_PIXELFORMAT :
				DDSD_CAPS|DDSD_WIDTH|DDSD_HEIGHT|DDSD_PIXELFORMAT;	

			// Initalise the rgbBits flags
			switch(imgData->format)
			{
			case PF_A8R8G8B8:
				ddsHeaderRgbBits = 8 * 4;
				hasAlpha = true;
				break;
			case PF_X8R8G8B8:
				ddsHeaderRgbBits = 8 * 4;
				break;
			case PF_R8G8B8:
				ddsHeaderRgbBits = 8 * 3;
				break;
			case PF_FLOAT32_R:
				ddsHeaderRgbBits = 32;
				break;
			default:
				ddsHeaderRgbBits = 0;
				break;
			}

			// Initalise the SizeOrPitch flags (power two textures for now)
			ddsHeaderSizeOrPitch = ddsHeaderRgbBits * imgData->width;

			// Initalise the caps flags
			ddsHeaderCaps1 = (isVolume||isCubeMap) ? DDSCAPS_COMPLEX|DDSCAPS_TEXTURE : DDSCAPS_TEXTURE;
			if (isVolume)
			{
				ddsHeaderCaps2 = DDSCAPS2_VOLUME;
			}
			else if (isCubeMap)
			{
				ddsHeaderCaps2 = DDSCAPS2_CUBEMAP|
					DDSCAPS2_CUBEMAP_POSITIVEX|DDSCAPS2_CUBEMAP_NEGATIVEX|
					DDSCAPS2_CUBEMAP_POSITIVEY|DDSCAPS2_CUBEMAP_NEGATIVEY|
					DDSCAPS2_CUBEMAP_POSITIVEZ|DDSCAPS2_CUBEMAP_NEGATIVEZ;
			}

			// Populate the DDS header information
			DDSHeader ddsHeader;
			ddsHeader.size = DDS_HEADER_SIZE;
			ddsHeader.flags = ddsHeaderFlags;		
			ddsHeader.width = (unsigned int)imgData->width;
			ddsHeader.height = (unsigned int)imgData->height;
			ddsHeader.depth = (unsigned int)(isVolume ? imgData->depth : 0);
			ddsHeader.depth = (unsigned int)(isCubeMap ? 6 : ddsHeader.depth);
			ddsHeader.mipMapCount = 0;
			ddsHeader.sizeOrPitch = ddsHeaderSizeOrPitch;
			for (unsigned int reserved1=0; reserved1<11; reserved1++) // XXX nasty constant 11
			{
				ddsHeader.reserved1[reserved1] = 0;
			}
			ddsHeader.reserved2 = 0;

			ddsHeader.pixelFormat.size = DDS_PIXELFORMAT_SIZE;
			ddsHeader.pixelFormat.flags = (hasAlpha) ? DDPF_RGB|DDPF_ALPHAPIXELS : DDPF_RGB;
			ddsHeader.pixelFormat.flags = (isFloat32r) ? DDPF_FOURCC : ddsHeader.pixelFormat.flags;
			ddsHeader.pixelFormat.fourCC = (isFloat32r) ? D3DFMT_R32F : 0;
			ddsHeader.pixelFormat.rgbBits = ddsHeaderRgbBits;

			ddsHeader.pixelFormat.alphaMask = (hasAlpha)   ? 0xFF000000 : 0x00000000;
			ddsHeader.pixelFormat.alphaMask = (isFloat32r) ? 0x00000000 : ddsHeader.pixelFormat.alphaMask;
			ddsHeader.pixelFormat.redMask   = (isFloat32r) ? 0xFFFFFFFF :0x00FF0000;
			ddsHeader.pixelFormat.greenMask = (isFloat32r) ? 0x00000000 :0x0000FF00;
			ddsHeader.pixelFormat.blueMask  = (isFloat32r) ? 0x00000000 :0x000000FF;

			ddsHeader.caps.caps1 = ddsHeaderCaps1;
			ddsHeader.caps.caps2 = ddsHeaderCaps2;
			ddsHeader.caps.reserved[0] = 0;
			ddsHeader.caps.reserved[1] = 0;

			// Swap endian
			flipEndian(&ddsMagic, sizeof(unsigned int), 1);
			flipEndian(&ddsHeader, 4, sizeof(DDSHeader) / 4);

			// Write the file 		

			CFile cf;
			if (!cf.Open(outFileName, CFile::modeCreate|CFile::modeWrite))
			{
				AfxMessageBox(_T("open fail."));
			}

			int len = imgData->size;
			cf.Write((const char *)&ddsMagic, sizeof(unsigned int));
			cf.Write((const char *)&ddsHeader, DDS_HEADER_SIZE);
			cf.Write(imgData->GetBuffer(), imgData->size);
			cf.Close();

			//std::ofstream of;
			//of.open(outFileName.c_str(), std::ios_base::binary|std::ios_base::out);
			//of.write((const char *)&ddsMagic, sizeof(unsigned int));
			//of.write((const char *)&ddsHeader, DDS_HEADER_SIZE);
			// XXX flipEndian on each pixel chunk written unless isFloat32r ?
			//of.write((const char *)input->getPtr(), (unsigned int)imgData->size);
			//of.close();
		}
	}
	//---------------------------------------------------------------------
	PixelFormat DDSCodec::convertFourCCFormat(unsigned int fourcc) const
	{
		// convert dxt pixel format
		switch(fourcc)
		{
		case FOURCC('D','X','T','1'):
			return PF_DXT1;
		case FOURCC('D','X','T','2'):
			return PF_DXT2;
		case FOURCC('D','X','T','3'):
			return PF_DXT3;
		case FOURCC('D','X','T','4'):
			return PF_DXT4;
		case FOURCC('D','X','T','5'):
			return PF_DXT5;
		case D3DFMT_R16F:
			return PF_FLOAT16_R;
		case D3DFMT_G16R16F:
			return PF_FLOAT16_GR;
		case D3DFMT_A16B16G16R16F:
			return PF_FLOAT16_RGBA;
		case D3DFMT_R32F:
			return PF_FLOAT32_R;
		case D3DFMT_G32R32F:
			return PF_FLOAT32_GR;
		case D3DFMT_A32B32G32R32F:
			return PF_FLOAT32_RGBA;
		// We could support 3Dc here, but only ATI cards support it, not nVidia
		default:
			ASSERT (false &&
				"Unsupported FourCC format found in DDS file DDSCodec::decode");
		};

	}
	//---------------------------------------------------------------------
	PixelFormat DDSCodec::convertPixelFormat(unsigned int rgbBits, unsigned int rMask, 
		unsigned int gMask, unsigned int bMask, unsigned int aMask) const
	{
		// General search through pixel formats
		for (int i = PF_UNKNOWN + 1; i < PF_COUNT; ++i)
		{
			PixelFormat pf = static_cast<PixelFormat>(i);
			if (getNumElemBits(pf) == rgbBits)
			{
				unsigned int testMasks[4];
				getBitMasks(pf, testMasks);
				int testBits[4];
				getBitDepths(pf, testBits);
				if (testMasks[0] == rMask && testMasks[1] == gMask &&
					testMasks[2] == bMask && 
					// for alpha, deal with 'X8' formats by checking bit counts
					(testMasks[3] == aMask || (aMask == 0 && testBits[3] == 0)))
				{
					return pf;
				}
			}

		}

		ASSERT( false && "Cannot determine pixel format DDSCodec::convertPixelFormat");

	}
	//---------------------------------------------------------------------
	void DDSCodec::unpackDXTColour(PixelFormat pf, const DXTColourBlock& block, 
		ColourValue* pCol) const
	{
		// Note - we assume all values have already been endian swapped

		// Colour lookup table
		ColourValue derivedColours[4];

		if (pf == PF_DXT1 && block.colour_0 <= block.colour_1)
		{
			// 1-bit alpha
			unpackColour(&(derivedColours[0]), PF_R5G6B5, &(block.colour_0));
			unpackColour(&(derivedColours[1]), PF_R5G6B5, &(block.colour_1));
			// one intermediate colour, half way between the other two
			derivedColours[2] = (derivedColours[0] + derivedColours[1]) / 2;
			// transparent colour
			derivedColours[3] = ColourValue::ZERO;
		}
		else
		{
			unpackColour(&(derivedColours[0]), PF_R5G6B5, &(block.colour_0));
			unpackColour(&(derivedColours[1]), PF_R5G6B5, &(block.colour_1));
			// first interpolated colour, 1/3 of the way along
			derivedColours[2] = (2 * derivedColours[0] + derivedColours[1]) / 3;
			// second interpolated colour, 2/3 of the way along
			derivedColours[3] = (derivedColours[0] + 2 * derivedColours[1]) / 3;
		}

		// Process 4x4 block of texels
		for (size_t row = 0; row < 4; ++row)
		{
			for (size_t x = 0; x < 4; ++x)
			{
				// LSB come first
				unsigned char colIdx = static_cast<unsigned char>(block.indexRow[row] >> (x * 2) & 0x3);
				if (pf == PF_DXT1)
				{
					// Overwrite entire colour
					pCol[(row * 4) + x] = derivedColours[colIdx];
				}
				else
				{
					// alpha has already been read (alpha precedes colour)
					ColourValue& col = pCol[(row * 4) + x];
					col.r = derivedColours[colIdx].r;
					col.g = derivedColours[colIdx].g;
					col.b = derivedColours[colIdx].b;
				}
			}

		}


	}
    
    //---------------------------------------------------------------------
	void DDSCodec::unpackDXTColourByte(PixelFormat pf, const DXTColourBlock& block,
                                   ByteColourValue* pCol) const
	{
		// Note - we assume all values have already been endian swapped
        
		// Colour lookup table
		ByteColourValue derivedColours[4];
        
		if (pf == PF_DXT1 )
		{
            unpackColour(&(derivedColours[0]), PF_R5G6B5, &(block.colour_0));
            unpackColour(&(derivedColours[1]), PF_R5G6B5, &(block.colour_1));
            if (block.colour_0 < block.colour_1) {
                // 1-bit alpha                
                // one intermediate colour, half way between the other two
                derivedColours[2].r = (derivedColours[0].r + derivedColours[1].r) >> 1;
                derivedColours[2].g = (derivedColours[0].g + derivedColours[1].g) >> 1;
                derivedColours[2].b = (derivedColours[0].b + derivedColours[1].b) >> 1;
                derivedColours[2].a = (derivedColours[0].a + derivedColours[1].a) >> 1;
                // transparent colour
                derivedColours[3].r = derivedColours[3].g = derivedColours[3].b = derivedColours[3].a = 0;
            }else
            {
                const static unsigned int magic = 0xAAAAAAAB;//search this for info
                derivedColours[0].a = 0xff;
                derivedColours[1].a = 0xff;
                derivedColours[2].r = (int64_t(2*derivedColours[0].r + derivedColours[1].r)*magic)>>33 ;
                derivedColours[2].g = (int64_t(2*derivedColours[0].g + derivedColours[1].g)*magic)>>33;
                derivedColours[2].b = (int64_t(2*derivedColours[0].b + derivedColours[1].b)*magic)>>33;
                derivedColours[2].a = 0xFF;
                // second interpolated colour, 2/3 of the way along
                //			derivedColours[3] = (derivedColours[0] + 2 * derivedColours[1]) / 3;
                derivedColours[3].r = (int64_t(derivedColours[0].r + 2*derivedColours[1].r)*magic)>>33;
                derivedColours[3].g = (int64_t(derivedColours[0].g + 2*derivedColours[1].g)*magic)>>33;
                derivedColours[3].b = (int64_t(derivedColours[0].b + 2*derivedColours[1].b)*magic)>>33;
                derivedColours[3].a = 0xFF;
            }
			
		}
		else
		{
			unpackColour(&(derivedColours[0]), PF_R5G6B5, &(block.colour_0));
			unpackColour(&(derivedColours[1]), PF_R5G6B5, &(block.colour_1));
			// first interpolated colour, 1/3 of the way along
//			derivedColours[2] = (2 * derivedColours[0] + derivedColours[1]) / 3;
            const static unsigned int magic = 0xAAAAAAAB;//search this for info
            derivedColours[2].r = (int64_t(2*derivedColours[0].r + derivedColours[1].r)*magic)>>33 ;
            derivedColours[2].g = (int64_t(2*derivedColours[0].g + derivedColours[1].g)*magic)>>33;
            derivedColours[2].b = (int64_t(2*derivedColours[0].b + derivedColours[1].b)*magic)>>33;
            derivedColours[2].a = (int64_t(2*derivedColours[0].a + derivedColours[1].a)*magic)>>33;
			// second interpolated colour, 2/3 of the way along
//			derivedColours[3] = (derivedColours[0] + 2 * derivedColours[1]) / 3;
            derivedColours[3].r = (int64_t(derivedColours[0].r + 2*derivedColours[1].r)*magic)>>33;
            derivedColours[3].g = (int64_t(derivedColours[0].g + 2*derivedColours[1].g)*magic)>>33;
            derivedColours[3].b = (int64_t(derivedColours[0].b + 2*derivedColours[1].b)*magic)>>33;
            derivedColours[3].a = (int64_t(derivedColours[0].a + 2*derivedColours[1].a)*magic)>>33;
		}
        
		// Process 4x4 block of texels
		for (size_t row = 0; row < 4; ++row)
		{
			for (size_t x = 0; x < 4; ++x)
			{
				// LSB come first
				unsigned char colIdx = static_cast<unsigned char>(block.indexRow[row] >> (x * 2) & 0x3);
				if (pf == PF_DXT1)
				{
					// Overwrite entire colour
					pCol[(row * 4) + x] = derivedColours[colIdx];
				}
				else
				{
					// alpha has already been read (alpha precedes colour)
					ByteColourValue& col = pCol[(row * 4) + x];
					col.r = derivedColours[colIdx].r;
					col.g = derivedColours[colIdx].g;
					col.b = derivedColours[colIdx].b;
				}
			}
            
		}
        
        
	}
    
	//---------------------------------------------------------------------
	void DDSCodec::unpackDXTAlpha(
		const DXTExplicitAlphaBlock& block, ColourValue* pCol) const
	{
		// Note - we assume all values have already been endian swapped
		
		// This is an explicit alpha block, 4 bits per pixel, LSB first
		for (size_t row = 0; row < 4; ++row)
		{
			for (size_t x = 0; x < 4; ++x)
			{
				// Shift and mask off to 4 bits
				unsigned char val = static_cast<unsigned char>(block.alphaRow[row] >> (x * 4) & 0xF);
				// Convert to [0,1]
				pCol->a = (Real)val / (Real)0xF;
				pCol++;
				
			}
			
		}

	}
    
    //---------------------------------------------------------------------
	void DDSCodec::unpackDXTAlphaByte(
                                  const DXTExplicitAlphaBlock& block, ByteColourValue* pCol) const
	{
		// Note - we assume all values have already been endian swapped
		
		// This is an explicit alpha block, 4 bits per pixel, LSB first
		for (size_t row = 0; row < 4; ++row)
		{
			for (size_t x = 0; x < 4; ++x)
			{
				// Shift and mask off to 4 bits
				unsigned char val = static_cast<unsigned char>(block.alphaRow[row] >> (x * 4) & 0xF);
				// Convert to [0,1]
				pCol->a = val*17;
				pCol++;
				
			}
			
		}
        
	}
    
	//---------------------------------------------------------------------
	void DDSCodec::unpackDXTAlpha(
		const DXTInterpolatedAlphaBlock& block, ColourValue* pCol) const
	{
		// 8 derived alpha values to be indexed
		Real derivedAlphas[8];

		// Explicit extremes
		derivedAlphas[0] = block.alpha_0 / (Real)0xFF;
		derivedAlphas[1] = block.alpha_1 / (Real)0xFF;
		
		
		if (block.alpha_0 <= block.alpha_1)
		{
			// 4 interpolated alphas, plus zero and one			
			// full range including extremes at [0] and [5]
			// we want to fill in [1] through [4] at weights ranging
			// from 1/5 to 4/5
			Real denom = 1.0f / 5.0f;
			for (size_t i = 0; i < 4; ++i) 
			{
				Real factor0 = (4 - i) * denom;
				Real factor1 = (i + 1) * denom;
				derivedAlphas[i + 2] = 
					(factor0 * derivedAlphas[0]) + (factor1 * derivedAlphas[1]);
			}
			derivedAlphas[6] = 0.0f;
			derivedAlphas[7] = 1.0f;

		}
		else
		{
			// 6 interpolated alphas
			// full range including extremes at [0] and [7]
			// we want to fill in [1] through [6] at weights ranging
			// from 1/7 to 6/7
			Real denom = 1.0f / 7.0f;
			for (size_t i = 0; i < 6; ++i) 
			{
				Real factor0 = (6 - i) * denom;
				Real factor1 = (i + 1) * denom;
				derivedAlphas[i + 2] = 
					(factor0 * derivedAlphas[0]) + (factor1 * derivedAlphas[1]);
			}
			
		}

		// Ok, now we've built the reference values, process the indexes
		for (size_t i = 0; i < 16; ++i)
		{
			size_t baseByte = (i * 3) / 8;
			size_t baseBit = (i * 3) % 8;
			unsigned char bits = static_cast<unsigned char>(block.indexes[baseByte] >> baseBit & 0x7);
			// do we need to stitch in next byte too?
			if (baseBit > 5)
			{
				unsigned char extraBits = static_cast<unsigned char>(
					(block.indexes[baseByte+1] << (8 - baseBit)) & 0xFF);
				bits |= extraBits & 0x7;
			}
			pCol[i].a = derivedAlphas[bits];

		}

	}
    
    //---------------------------------------------------------------------
	void DDSCodec::unpackDXTAlphaByte(
                                  const DXTInterpolatedAlphaBlock& block, ByteColourValue* pCol) const
	{
		// 8 derived alpha values to be indexed
		unsigned char derivedAlphas[8];
        
		// Explicit extremes
		derivedAlphas[0] = block.alpha_0;
		derivedAlphas[1] = block.alpha_1;
		
		
		if (block.alpha_0 <= block.alpha_1)
		{
			// 4 interpolated alphas, plus zero and one
			// full range including extremes at [0] and [5]
			// we want to fill in [1] through [4] at weights ranging
			// from 1/5 to 4/5
			Real denom = 1.0f / 5.0f;
			for (size_t i = 0; i < 4; ++i)
			{
				Real factor0 = (4 - i) * denom;
				Real factor1 = (i + 1) * denom;
				derivedAlphas[i + 2] =
                (factor0 * derivedAlphas[0]) + (factor1 * derivedAlphas[1]);
			}
			derivedAlphas[6] = 0;
			derivedAlphas[7] = 0xFF;
            
		}
		else
		{
			// 6 interpolated alphas
			// full range including extremes at [0] and [7]
			// we want to fill in [1] through [6] at weights ranging
			// from 1/7 to 6/7
			Real denom = 1.0f / 7.0f;
			for (size_t i = 0; i < 6; ++i)
			{
				Real factor0 = (6 - i) * denom;
				Real factor1 = (i + 1) * denom;
				derivedAlphas[i + 2] =
                (factor0 * derivedAlphas[0]) + (factor1 * derivedAlphas[1]);
			}
			
		}
        
		// Ok, now we've built the reference values, process the indexes
		for (size_t i = 0; i < 16; ++i)
		{
			size_t baseByte = (i * 3) / 8;
			size_t baseBit = (i * 3) % 8;
			unsigned char bits = static_cast<unsigned char>(block.indexes[baseByte] >> baseBit & 0x7);
			// do we need to stitch in next byte too?
			if (baseBit > 5)
			{
				unsigned char extraBits = static_cast<unsigned char>(
                                                                     (block.indexes[baseByte+1] << (8 - baseBit)) & 0xFF);
				bits |= extraBits & 0x7;
			}
			pCol[i].a = derivedAlphas[bits];
            
		}
        
	}
    
    //---------------------------------------------------------------------
    bool DDSCodec::decode(MemStream& astream, CDDSImage& output) const
    {
		MemStream* stream = &astream;
		// Read 4 character code
		unsigned int fileType;
		stream->read(&fileType, sizeof(unsigned int));
		flipEndian(&fileType, sizeof(unsigned int), 1);
		
		if (FOURCC('D', 'D', 'S', ' ') != fileType)
		{
			return false;
			/*
			OGRE_EXCEPT(Exception::ERR_INVALIDPARAMS, 
				"This is not a DDS file!", "DDSCodec::decode");*/
		}
		
		// Read header in full
		DDSHeader header;
		stream->read(&header, sizeof(DDSHeader));

		// Endian flip if required, all 32-bit values
		flipEndian(&header, 4, sizeof(DDSHeader) / 4);

		// Check some sizes
		if (header.size != DDS_HEADER_SIZE)
		{
			return false;
			/*OGRE_EXCEPT(Exception::ERR_INVALIDPARAMS, 
				"DDS header size mismatch!", "DDSCodec::decode");*/
		}
		if (header.pixelFormat.size != DDS_PIXELFORMAT_SIZE)
		{
			return false;
			/*OGRE_EXCEPT(Exception::ERR_INVALIDPARAMS, 
				"DDS header size mismatch!", "DDSCodec::decode");*/
		}

		CDDSImage* imgData = &output;

		imgData->depth = 1; // (deal with volume later)
		imgData->width = header.width;
		imgData->height = header.height;
		size_t numFaces = 1; // assume one face until we know otherwise

		if (header.caps.caps1 & DDSCAPS_MIPMAP)
		{
	        imgData->num_mipmaps = static_cast<unsigned short>(header.mipMapCount - 1);
		}
		else
		{
			imgData->num_mipmaps = 0;
		}
		imgData->flags = 0;

		bool decompressDXT = false;
		// Figure out basic image type
		if (header.caps.caps2 & DDSCAPS2_CUBEMAP)
		{
			imgData->flags |= IF_CUBEMAP;
			numFaces = 6;
		}
		else if (header.caps.caps2 & DDSCAPS2_VOLUME)
		{
			imgData->flags |= IF_3D_TEXTURE;
			imgData->depth = header.depth;
		}
		// Pixel format
		PixelFormat sourceFormat = PF_UNKNOWN;

		if (header.pixelFormat.flags & DDPF_FOURCC)
		{
			sourceFormat = convertFourCCFormat(header.pixelFormat.fourCC);
		}
		else
		{
			sourceFormat = convertPixelFormat(header.pixelFormat.rgbBits, 
				header.pixelFormat.redMask, header.pixelFormat.greenMask, 
				header.pixelFormat.blueMask, 
				header.pixelFormat.flags & DDPF_ALPHAPIXELS ? 
				header.pixelFormat.alphaMask : 0);
		}

		if (isCompressed(sourceFormat))
		{			
			// We'll need to decompress
			decompressDXT = true;
			// Convert format
			switch (sourceFormat)
			{
			case PF_DXT1:
				// source can be either 565 or 5551 depending on whether alpha present
				// unfortunately you have to read a block to figure out which
				// Note that we upgrade to 32-bit pixel formats here, even 
				// though the source is 16-bit; this is because the interpolated
				// values will benefit from the 32-bit results, and the source
				// from which the 16-bit samples are calculated may have been
				// 32-bit so can benefit from this.
				DXTColourBlock block;
				stream->read(&block, sizeof(DXTColourBlock));
				flipEndian(&(block.colour_0), sizeof(unsigned short), 1);
				flipEndian(&(block.colour_1), sizeof(unsigned short), 1);
				// skip back since we'll need to read this again
				stream->skip(0 - (long)sizeof(DXTColourBlock));
				// colour_0 <= colour_1 means transparency in DXT1
				if (block.colour_0 < block.colour_1)
				{
					imgData->format = PF_BYTE_RGBA;
				}
				else
				{
					imgData->format = PF_BYTE_RGB;
				}
				break;
			case PF_DXT2:
			case PF_DXT3:
			case PF_DXT4:
			case PF_DXT5:
				// full alpha present, formats vary only in encoding 
				imgData->format = PF_BYTE_RGBA;
				break;
            default:
                // all other cases need no special format handling
                break;
			}			
		}
		else // not compressed
		{
			// Don't test against DDPF_RGB since greyscale DDS doesn't set this
			// just derive any other kind of format
			imgData->format = sourceFormat;
		}

		// Calculate total size from number of mipmaps, faces and size
		imgData->size = calculateSize(imgData->num_mipmaps, numFaces, 
			imgData->width, imgData->height, imgData->depth, imgData->format);

		if (PF_A4R4G4B4 == sourceFormat)
		{
			imgData->size *= 2;
		}
		// Bind output buffer
		//output.bind(OGRE_NEW MemoryDataStream(imgData->size));

		
		// Now deal with the data
		void* destPtr = output.AllocBuffer(imgData->size);

		// all mips for a face, then each face
		for(size_t i = 0; i < numFaces; ++i)
		{   
			size_t width = imgData->width;
			size_t height = imgData->height;
			size_t depth = imgData->depth;

			for(size_t mip = 0; mip <= imgData->num_mipmaps; ++mip)
			{
				size_t dstPitch = width * getNumElemBytes(imgData->format);

				if (isCompressed(sourceFormat))
				{					
					DXTColourBlock* col = NULL;
					DXTInterpolatedAlphaBlock* iAlpha = NULL;
					DXTExplicitAlphaBlock* eAlpha = NULL;
					// 4x4 block of decompressed colour
//					ColourValue tempColours[16];
                    ByteColourValue tempByteColours[16];
					size_t destBpp = getNumElemBytes(imgData->format);
					size_t sx = std::min<size_t>(width, (size_t)4);
					size_t sy = std::min<size_t>(height, (size_t)4);
					size_t destPitchMinus4 = dstPitch - destBpp * sx;
					// slices are done individually
					for(size_t z = 0; z < depth; ++z)
					{
						// 4x4 blocks in x/y
						for (size_t y = 0; y < height; y += 4)
						{
							for (size_t x = 0; x < width; x += 4)
							{
								size_t sx = std::min<size_t>(width - x, (size_t)4);
								size_t sy = std::min<size_t>(height - y, (size_t)4);


								if (sourceFormat == PF_DXT2 || 
									sourceFormat == PF_DXT3)
								{
									// explicit alpha
									stream->xread((void**)&eAlpha, sizeof(DXTExplicitAlphaBlock));
									flipEndian((*eAlpha).alphaRow, sizeof(unsigned short), 4);
									unpackDXTAlphaByte((*eAlpha), tempByteColours) ;
								}
								else if (sourceFormat == PF_DXT4 || 
									sourceFormat == PF_DXT5)
								{
									// interpolated alpha
									stream->xread((void**)&iAlpha, sizeof(DXTInterpolatedAlphaBlock));
									flipEndian(&((*iAlpha).alpha_0), sizeof(unsigned short), 1);
									flipEndian(&((*iAlpha).alpha_1), sizeof(unsigned short), 1);
									unpackDXTAlphaByte((*iAlpha), tempByteColours) ;
								}
								// always read colour
								stream->xread((void**)&col, sizeof(DXTColourBlock));
								flipEndian(&((*col).colour_0), sizeof(unsigned short), 1);
								flipEndian(&((*col).colour_1), sizeof(unsigned short), 1);
								unpackDXTColourByte(sourceFormat, *col, tempByteColours);

								// write 4x4 block to uncompressed version
								for (size_t by = 0; by < sy; ++by)
								{
									for (size_t bx = 0; bx < sx; ++bx)
									{
										ASSERT(destPtr < output.GetBuffer() + output.size);
//										packColour(tempColours[by*4+bx],imgData->format, destPtr);
                                        unsigned char* pClr = (unsigned char*)destPtr;
                                        pClr[0] = tempByteColours[by*4+bx].r;
                                        pClr[1] = tempByteColours[by*4+bx].g;
                                        pClr[2] = tempByteColours[by*4+bx].b;
                                        if (imgData->format == PF_BYTE_RGBA) {
                                            pClr[3] = tempByteColours[by*4+bx].a;
                                        }
                                        
										destPtr = static_cast<void*>(static_cast<unsigned char*>(destPtr) + destBpp);										
									}
									// advance to next row
									destPtr = static_cast<void*>(static_cast<unsigned char*>(destPtr) + destPitchMinus4);
								}
								// next block. Our dest pointer is 4 lines down
								// from where it started
								if (x + 4 >= width)
								{									
									// Jump back to the start of the line
									destPtr = static_cast<void*>(static_cast<unsigned char*>(destPtr) - destPitchMinus4);									
								}
								else
								{
									// Jump back up 4 rows and 4 pixels to the
									// right to be at the next block to the right
									destPtr = static_cast<void*>(static_cast<unsigned char*>(destPtr) - dstPitch * sy + destBpp * sx);									
								}
							}
						}
					}
				}
				else
				{
					// Final data - trim incoming pitch
					size_t srcPitch;
					if (header.flags & DDSD_PITCH)
					{
						srcPitch = header.sizeOrPitch / 
							std::max<size_t>((size_t)1, mip * 2);
					}
					else
					{
						// assume same as final pitch
						srcPitch = dstPitch;
					}
					ASSERT (dstPitch <= srcPitch);
					long srcAdvance = static_cast<long>(srcPitch) - static_cast<long>(dstPitch);

					int bytesPP = 0;
					switch (sourceFormat)
					{
					case PF_A4R4G4B4:
						bytesPP = 2;
						break;
					case PF_A8R8G8B8:
						bytesPP = 4;
						break;
					default:
						bytesPP = 3;
					}
					unsigned char* buf = new unsigned char[dstPitch];
					for (size_t z = 0; z < imgData->depth; ++z)
					{
						for (size_t y = 0; y < height; ++y)
						{
							stream->read(buf, dstPitch);

							
							for (int i = 0; i < dstPitch; i+=bytesPP)
							{
								unsigned char* pDst = (unsigned char*)destPtr;
								
								if (PF_A8R8G8B8 == sourceFormat)
								{
									pDst[i] = buf[i + 2];
									pDst[i + 1] = buf[i + 1];
									pDst[i + 2] = buf[i];
									pDst[i + 3] = buf[i + 3];//buf[i ];
									//pDst[i] = 0;//red
									//pDst[i + 1] = 0;//green
									//pDst[i + 2] = 0;//blue
									//pDst[i + 3] = 0xFF;
								}else if (PF_A4R4G4B4 == sourceFormat)
								{
									pDst[i*2] = (buf[i + 1] & 0xF)<<4;
									pDst[i*2 + 1] = (buf[i]&0xF0);
									pDst[i*2 + 2] = (buf[i]&0xF)<<4;
									pDst[i*2 + 3] = (buf[i + 1] & 0xF0);
//                                    pDst[i*2] = 0xFF;   //red
//									pDst[i*2 + 1] = 0; //green
//                                    
//									pDst[i*2 + 2] = 0; //blue
//									pDst[i*2 + 3] = 0; //alpha
								}else
								{
									pDst[i] = buf[i + 2];
									pDst[i + 1] = buf[i + 1];
									pDst[i + 2] = buf[i];
								}
								
							}
							if (srcAdvance > 0)
								stream->skip(srcAdvance);

							destPtr = static_cast<void*>(static_cast<unsigned char*>(destPtr) + (PF_A4R4G4B4 == sourceFormat? dstPitch*2:dstPitch));
						}
					}
					delete []buf;
				}

				
				/// Next mip
				if(width!=1) width /= 2;
				if(height!=1) height /= 2;
				if(depth!=1) depth /= 2;
			}
		}

		/*DecodeResult ret;
		ret.first = output;
		ret.second = CodecDataPtr(imgData);*/
		return true;
    }
    //---------------------------------------------------------------------    
     
    void DDSCodec::flipEndian(void * pData, size_t size, size_t count) const
    {
#if OGRE_ENDIAN == OGRE_ENDIAN_BIG
		for(unsigned int index = 0; index < count; index++)
        {
            flipEndian((void *)((long)pData + (index * size)), size);
        }
#endif
    }
    //---------------------------------------------------------------------    
    void DDSCodec::flipEndian(void * pData, size_t size) const
    {
#if OGRE_ENDIAN == OGRE_ENDIAN_BIG
        char swapByte;
        for(unsigned int byteIndex = 0; byteIndex < size/2; byteIndex++)
        {
            swapByte = *(char *)((long)pData + byteIndex);
            *(char *)((long)pData + byteIndex) = *(char *)((long)pData + size - byteIndex - 1);
            *(char *)((long)pData + size - byteIndex - 1) = swapByte;
        }
#endif
    }
	//---------------------------------------------------------------------
	/*CCString DDSCodec::magicNumberToFileExt(const char *magicNumberPtr, size_t maxbytes) const
	{
		if (maxbytes >= sizeof(unsigned int))
		{
			unsigned int fileType;
			memcpy(&fileType, magicNumberPtr, sizeof(unsigned int));
			flipEndian(&fileType, sizeof(unsigned int), 1);

			if (DDS_MAGIC == fileType)
			{
				return CCString("dds");
			}
		}

		return "";
	}*/
	
}

