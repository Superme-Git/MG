//  CEGUICocos2DImageCodecModule.h
//  CEGUI

#ifndef CEGUI_CEGUICocos2DImageCodecModule_h
#define CEGUI_CEGUICocos2DImageCodecModule_h

#include "CEGUICocos2DImageCodec.h"

extern "C" CEGUICocos2DImageCodec_API CEGUI::ImageCodec* createImageCodec(void);
extern "C" CEGUICocos2DImageCodec_API void destroyImageCodec(CEGUI::ImageCodec* iamgeCodec);

#endif
