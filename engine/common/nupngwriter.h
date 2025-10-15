#ifndef __NUCLEAR_PNGWRITER__
#define __NUCLEAR_PNGWRITER__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxbuffer.h"

namespace Nuclear
{
	bool Write32BitPNGWithPitch(NuclearBuffer &buffer, unsigned char* pBits, bool bNeedAlpha, int nWidth, int nHeight, int nPitch);
}

#endif