//
//  CEGUIIosBuildInParserModule.cpp
//  CEGUI
//
//  Created by MagicStudio on 12-8-23.
//  Copyright 2012年 __MyCompanyName__. All rights reserved.
//

#include "CEGUIIosBuildInParserModule.h"

CEGUI::XMLParser* createParser(void)
{
    return new CEGUI::IosBuildInParser();
}

void destroyParser(CEGUI::XMLParser* parser)
{
    delete parser;
}