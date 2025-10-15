//  CEGUIIosBuildInParserModule.h
//  CEGUI

#ifndef CEGUI_CEGUIIosBuildInParserModule_h
#define CEGUI_CEGUIIosBuildInParserModule_h

#include "CEGUIIosBuildInParser.h"

extern "C" CEGUIIosBuildInParser_API CEGUI::XMLParser* createParser(void);
extern "C" CEGUIIosBuildInParser_API void destroyParser(CEGUI::XMLParser* parser);

#endif
