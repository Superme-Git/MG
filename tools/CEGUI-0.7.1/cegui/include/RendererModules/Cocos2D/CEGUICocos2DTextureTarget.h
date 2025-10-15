#ifndef _CEGUICocos2DTextureTarget_h_
#define _CEGUICocos2DTextureTarget_h_

#include "CEGUICocos2DRenderTarget.h"
#include "../../CEGUITextureTarget.h"
#include "../../CEGUIRect.h"

#include <misc_nodes/CCRenderTexture.h>

#if defined(_MSC_VER)
#   pragma warning(push)
#   pragma warning(disable : 4250)
#   pragma warning(disable : 4251)
#endif

namespace CEGUI
{
class Cocos2DTexture;
class COCOS2D_GUIRENDERER_API Cocos2DTextureTarget : public Cocos2DRenderTarget,
                                                         public TextureTarget
{
public:
    Cocos2DTextureTarget(Cocos2DRenderer& owner);
    virtual ~Cocos2DTextureTarget();

    void preD3DReset();
    void postD3DReset();

    // overrides from Cocos2DRenderTarget
    virtual void activate();
    virtual void deactivate();
    // implementation of RenderTarget interface
    virtual bool isImageryCache() const;
    // implementation of TextureTarget interface
    virtual void clear();
    virtual Texture& getTexture() const;
    virtual void declareRenderSize(const Size& sz);
    virtual bool isRenderingInverted() const;

	virtual bool saveToFile(const String& filename);

protected:
    //! default size of created texture objects
    static const float DEFAULT_SIZE;

    //! allocate and set up the texture used for rendering.
    void initialiseRenderTexture();
    //! clean up the texture used for rendering.
    void cleanupRenderTexture();
    //! resize the texture
    void resizeRenderTexture();

	cocos2d::CCRenderTexture*	d_surface;
	Cocos2DTexture*				d_texture;
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#   pragma warning(pop)
#endif

#endif  // end of guard _CEGUICocos2DTextureTarget_h_
