//
//  CEGUICocos2DImageCodecModule.cpp
//  CEGUI
//
//  Created by MagicStudio on 12-8-23.
//  Copyright 2012年 __MyCompanyName__. All rights reserved.
//

#include "CEGUICocos2DImageCodecModule.h"

CEGUI::ImageCodec* createImageCodec(void)
{
    return  new CEGUI::Cocos2DImageCodec();
}

void destroyImageCodec(CEGUI::ImageCodec* imageCodec)
{
    delete imageCodec;
}