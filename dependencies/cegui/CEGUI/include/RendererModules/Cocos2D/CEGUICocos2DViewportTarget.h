#ifndef _CEGUICocos2DViewportTarget_h_
#define _CEGUICocos2DViewportTarget_h_

#include "CEGUICocos2DRenderTarget.h"
#include "../../CEGUIRect.h"

namespace CEGUI
{

class COCOS2D_GUIRENDERER_API Cocos2DViewportTarget : 
    public Cocos2DRenderTarget
{
public:
    Cocos2DViewportTarget(Cocos2DRenderer& owner);
    Cocos2DViewportTarget(Cocos2DRenderer& owner, const Rect& area);
    bool isImageryCache() const;
};

} // End of  CEGUI namespace section

#endif  // end of guard _CEGUICocos2DViewportTarget_h_
