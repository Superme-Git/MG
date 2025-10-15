//  cocos2d_type.h
//  engine

#ifndef __Nuclear_cocos2d_type_H__
#define __Nuclear_cocos2d_type_H__
#include "nuxptypes.h"
#include <cocos2d.h>

namespace Nuclear
{
    cocos2d::CCTexture2DPixelFormat GetCCPixelFormatFromXP(NuclearPixelFormat aPF);
    cocos2d::CCTexture2DPixelFormat GetCCPixelFormatFromXP(NuclearTextureFormat aPTF);
    
}// namespace Nuclear

#endif
