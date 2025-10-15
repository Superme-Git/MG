#ifndef __NUCLEAR_PNGWRITER
#define __NUCLEAR_PNGWRITER

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\..\contrib\lpng1234\png.h"
#include "..\common\xbuffer.h"
#include "..\..\contrib\libwebp\include\webp\encode.h"

namespace Nuclear
{
	bool Write32BitPNGWithPitch(XBuffer &buffer, unsigned char* pBits, bool bNeedAlpha, int nWidth, int nHeight, int nPitch);
	bool Write32BitWebPNGWithPitch(XBuffer &buffer, unsigned char* pBits, bool bNeedAlpha, float fQuality, int nWidth, int nHeight, int nPitch);
}

#endif