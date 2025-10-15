#define NOMINMAX
#include "CEGUICocos2DViewportTarget.h"
#include "CEGUIRenderQueue.h"
#include "CEGUIGeometryBuffer.h"
#include "CEGUIExceptions.h"

#include <CCGL.h>


namespace CEGUI
{
Cocos2DViewportTarget::Cocos2DViewportTarget(Cocos2DRenderer& owner) :
    Cocos2DRenderTarget(owner)
{
    // initialise renderer size    
	GLint vp[4];
	glGetIntegerv(GL_VIEWPORT, vp);
    Rect area(
        Point(static_cast<float>(vp[0]), static_cast<float>(vp[1])),
        Size(static_cast<float>(vp[2]), static_cast<float>(vp[3]))
    );

    setArea(area);
}

Cocos2DViewportTarget::Cocos2DViewportTarget(Cocos2DRenderer& owner, const Rect& area) :
        Cocos2DRenderTarget(owner)
{
    setArea(area);
}

bool Cocos2DViewportTarget::isImageryCache() const
{
    return false;
}

} // End of  CEGUI namespace section
