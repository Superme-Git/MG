#ifndef NUCLEAR_COMMON_3DENGINE_H
#define NUCLEAR_COMMON_3DENGINE_H

#include "../../NuclearRSI/NuclearRSI/export/MXPRSI.h"
#include "../inc/GGlobal.h"
#include "../inc/GInterface.h"
#include "../inc/GSprite.h"
#include "../inc/GEffect.h"
#include "../inc/GNuclear.h"
#include "../inc/GFileInput.h"
#include "../inc/GResParser.h"

#ifndef max
#define max(a,b)            (((a) > (b)) ? (a) : (b))
#endif

#ifndef min
#define min(a,b)            (((a) < (b)) ? (a) : (b))
#endif

namespace Nuclear
{
	const float XP3DOBJ_THICKNESS = 80.0f;
	const int XP3DOBJ_MAX_HEIGHT = 300;
}

#endif