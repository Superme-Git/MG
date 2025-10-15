//  cocos2d_type.cpp
//  engine

#include <iostream>

#include "nucocos2d_type.h"

namespace Nuclear
{
	cocos2d::CCTexture2DPixelFormat GetCCPixelFormatFromXP(NuclearPixelFormat aPF)
    {
		switch (aPF)
		{
            case XPPIXELFMT_DEFAULT:
                assert("unknown pixel format XPPIXELFMT_DEFAULT" && false);
                break;
            case XPPIXELFMT_R5G6B5:
                return cocos2d::kCCTexture2DPixelFormat_RGB565;
            case XPPIXELFMT_A8R8G8B8:
                return cocos2d::kCCTexture2DPixelFormat_RGBA8888;
            case XPPIXELFMT_A4R4G4B4:
                return cocos2d::kCCTexture2DPixelFormat_RGBA4444;
        }
        assert("unknown pixel format" && false);
    }
    
	cocos2d::CCTexture2DPixelFormat GetCCPixelFormatFromXP(NuclearTextureFormat aPTF)
    {
		switch (aPTF)
		{
            case XPTEXFMT_DEFAULT:
                assert("unknown pixel format XPTEXFMT_DEFAULT" && false);
                break;
            case XPTEXFMT_R5G6B5:
                return cocos2d::kCCTexture2DPixelFormat_RGB565;
            case XPTEXFMT_A8R8G8B8:
                return cocos2d::kCCTexture2DPixelFormat_RGBA8888;
            case XPTEXFMT_A4R4G4B4:
                return cocos2d::kCCTexture2DPixelFormat_RGBA4444;
            default:
                assert("unknown pixel format" && false);
        }        
    }
}//namespace Nuclear
