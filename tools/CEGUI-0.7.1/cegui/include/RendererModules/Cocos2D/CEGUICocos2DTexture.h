#ifndef _CEGUICocos2DTexture_h_
#define _CEGUICocos2DTexture_h_

#include "../../CEGUIBase.h"
#include "../../CEGUIRenderer.h"
#include "../../CEGUITexture.h"
#include "CEGUICocos2DRenderer.h"

#include <CCGL.h>
#include <textures/CCTexture2D.h>

namespace CEGUI
{

class COCOS2D_GUIRENDERER_API Cocos2DTexture : public Texture
{
public:
    //void setDirect3D9Texture(LPDIRECT3DTEXTURE9 tex);
    //LPDIRECT3DTEXTURE9 getDirect3D9Texture() const;

    bool m_bIsLoadFromFile;
    bool m_bIsLoading;
    bool isEtc(){return d_texture->isEtcTexture();}
    GLuint  getAlphaName(){ return d_texture->getAlphaName(); }
	GLuint	getTextureName() const;
	String	getFileName() const;
	void	setOriginalDataSize(const Size& sz);
    void	preD3DReset();
    void	postD3DReset();

	//implements Texture Interface
    virtual const Size&		getSize() const;
    virtual const Size&		getOriginalDataSize() const;
    virtual const Vector2&	getTexelScaling() const;
    virtual void			loadFromFile(const String& filename, 
		const String& resourceGroup);
    virtual void			loadFromMemory(const void* buffer, 
		const Size& buffer_size, PixelFormat pixel_format, bool async);
    virtual void loadFromBuffer(const void* buffer, const Size& buffer_size, PixelFormat pixel_format);
	virtual void			updataFromMemory(const void* buffer, const Size& buffer_size,
		const Rect& rect,PixelFormat pixel_format);
    virtual void			saveToMemory(void* buffer);
    virtual ~Cocos2DTexture();

    virtual void setImageSet(const Imageset* pImageset){d_pImageset = const_cast<Imageset*>(pImageset);}
protected:
    friend Texture& Cocos2DRenderer::createTexture(void);
	friend Texture& Cocos2DRenderer::createTexture(const String&, const String&, float priority, bool synload);
    friend Texture& Cocos2DRenderer::createTexture(const Size&);
	friend Texture& Cocos2DRenderer::createTexture(cocos2d::CCTexture2D *pTexture);
	friend void Cocos2DRenderer::destroyTexture(Texture* texture);
    friend void Cocos2DRenderer::OnFrameEnd();

    
    Cocos2DTexture(Cocos2DRenderer& owner);
    Cocos2DTexture(Cocos2DRenderer& owner, const String& filename,
                     const String& resourceGroup);
    Cocos2DTexture(Cocos2DRenderer& owner, const Size& sz);
    Cocos2DTexture(Cocos2DRenderer& owner, cocos2d::CCTexture2D* tex);
    

	void cleanupCocos2DTexture();
    void updateCachedScaleValues();
    void updateTextureSize();
	void setCocos2DTexture(cocos2d::CCTexture2D* tex);

    Cocos2DRenderer& d_owner;
    cocos2d::CCTexture2D* d_texture;
    //! Size of the texture.
    Size d_size;
    //! original pixel of size data loaded into texture
    Size d_dataSize;
    //! cached pixel to texel mapping scale values.
    Vector2 d_texelScaling;
    //! holds info about the texture surface before we released it for reset.
    //D3DSURFACE_DESC d_savedSurfaceDesc;
    //! true when d_savedSurfaceDesc is valid and texture can be restored.
    bool d_savedSurfaceDescValid;
    Imageset* d_pImageset;
    String d_filename;
};

} // End of  CEGUI namespace section


#endif // end of guard _CEGUICocos2DTexture_h_
