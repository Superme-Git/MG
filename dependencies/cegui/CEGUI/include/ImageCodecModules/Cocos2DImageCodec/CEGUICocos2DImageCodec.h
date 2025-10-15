//  CEGUICocos2DImageCodec.h
//  CEGUI

#ifndef CEGUI_CEGUICocos2DImageCodec_h
#define CEGUI_CEGUICocos2DImageCodec_h
#include <CEGUIImageCodec.h>

#ifdef PUBLISHED_VERSION
#include "../../Nuclear.h"
#endif

#define CEGUICocos2DImageCodec_API

namespace CEGUI {
    class CEGUICocos2DImageCodec_API Cocos2DImageCodec : public ImageCodec
    {
    public:
        Cocos2DImageCodec();
        ~Cocos2DImageCodec();
        
        Texture* load(const RawDataContainer& data, Texture* result);
        
#ifdef PUBLISHED_VERSION
		virtual Texture* load(const LJFM::LJFMID& data, Texture* result);
#endif
    };
}//end of namespace CEGUI


#endif
