//
//  CEGUICocos2DImageCodec.cpp
//  CEGUI
//
//  Created by MagicStudio on 12-8-23.
//  Copyright 2012年 __MyCompanyName__. All rights reserved.
//

#include "CEGUICocos2DImageCodec.h"
#include <CEGUISize.h>
#include <cocos2d.h>

#ifdef ANDROID
#include <fcntl.h>
#include <errno.h>
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGD
#endif

namespace CEGUI {
    Cocos2DImageCodec::Cocos2DImageCodec()
    :CEGUI::ImageCodec("Cocos2DImageCodec - LocoJoy image codec")
    {
        
    }
    
    Cocos2DImageCodec::~Cocos2DImageCodec()
    {
        
    }
    
    Texture* Cocos2DImageCodec::load(const RawDataContainer& data, Texture* result)
    {
        cocos2d::CCImage image;                
        if(image.initWithImageData((void*)data.getDataPtr(), data.getSize(), cocos2d::CCImage::kFmtPng))
        {
            CEGUI::Texture::PixelFormat pixel_format;
            if(image.hasAlpha())
            {
                pixel_format = CEGUI::Texture::PF_RGBA;
            }else
            {
                pixel_format = CEGUI::Texture::PF_RGB;
            }
            result->loadFromMemory(image.getData(), CEGUI::Size((float)image.getWidth(), (float)image.getHeight()), pixel_format);
            
            return result;
        }
           
      
        return NULL;
    }

    
#ifdef PUBLISHED_VERSION
    Texture* Cocos2DImageCodec::load(const PFS::CImageData& data, Texture* result)
    {
        cocos2d::CCImage image;
        
        if(image.initWithImageData((void*)data.GetData(), data.GetSize(), cocos2d::CCImage::kFmtUnKnown))
        {
            CEGUI::Texture::PixelFormat pixel_format;
            if(image.hasAlpha())
            {
                pixel_format = CEGUI::Texture::PF_RGBA;
            }else
            {
                pixel_format = CEGUI::Texture::PF_RGB;
            }
            result->loadFromMemory(image.getData(), CEGUI::Size((float)image.getWidth(), (float)image.getHeight()), pixel_format);
            
            return result;
        }
        
        
        return NULL;
    }
#endif
    
}//end of namespace CEGUI
