#ifndef _CEGUICocos2DRenderer_h_
#define _CEGUICocos2DRenderer_h_

#include "../../CEGUIBase.h"
#include "../../CEGUIRenderer.h"
#include "../../CEGUISize.h"
#include "../../CEGUIVector.h"

#include <cocos2d.h>

#include <vector>
#include <map>

#include <platform/mutex.h>

#if (defined( __WIN32__ ) || defined( _WIN32 )) && !defined(CEGUI_STATIC)
#   ifdef CEGUICOCOS2DRENDER_EXPORTS
#       define COCOS2D_GUIRENDERER_API __declspec(dllexport)
#   else
#       define COCOS2D_GUIRENDERER_API __declspec(dllimport)
#   endif
#else
#   define DIRECT3D9_GUIRENDERER_API
#endif
#   define COCOS2D_GUIRENDERER_API

#if defined(_MSC_VER)
#   pragma warning(push)
#   pragma warning(disable : 4251)
#endif

#define CheckSuccess()	assert(glGetError() == 0)
#define CC0(func)\
	func();\
	CheckSuccess()

#define CC1(func, arg)\
	func(arg);\
	CheckSuccess()

namespace CEGUI
{
class Cocos2DTexture;
class Cocos2DGeometryBuffer;
class ITask;
    
class COCOS2D_GUIRENDERER_API Cocos2DRenderer : public Renderer
{
    struct StateManager{
      
        bool    d_ScissorEnabled;
        int     d_ScissorBox[4];
    };
public:
	static Cocos2DRenderer& bootstrapSystem(cocos2d::CCLayer* parent, const char* logFile);
	static void destroySystem();
    static Cocos2DRenderer& create();
    static void destroy(Cocos2DRenderer& renderer);

    struct STextureLoadItem
    {
        ITask*  pTask;
        int iCheckCount;
        STextureLoadItem()
        {
            pTask   = NULL;
            iCheckCount = 0;
        }
    };
    cocos2d::CCGLProgram* m_program;
private:
    std::map<String, Cocos2DTexture*>   m_mapLoadingTexture;
    std::map<Cocos2DTexture*, STextureLoadItem>   m_mapLoadedTexture;
    
    core::CMutex m_mutexLoadingTextures;
    core::CMutex m_mutexLoadedTextures;
public:
    void OnFileLoaded(ITask* aPTask);
    void OnImageParsed(ITask* aPTask);
    void CheckLoadingTexture(Cocos2DTexture* aPTexture);
    
    void preD3DReset();

    void postD3DReset();

    bool supportsNonSquareTexture();

    bool supportsNPOTTextures();

    Size getAdjustedSize(const Size& sz);

    // implement Renderer interface
    virtual RenderingRoot&		getDefaultRenderingRoot();
    virtual GeometryBuffer&		createGeometryBuffer();

    virtual void				destroyGeometryBuffer(const GeometryBuffer* buffer);
    virtual void				destroyAllGeometryBuffers();
    
	virtual TextureTarget*		createTextureTarget();
    virtual void				destroyTextureTarget(TextureTarget* target);
    virtual void				destroyAllTextureTargets();
    
	virtual Texture&			createTexture();
	virtual Texture&			createTexture(const String& filename, const String& resourceGroup, float priority, bool synload);
    virtual Texture&			createTexture(const Size& size);
    virtual void				destroyTexture(Texture* texture);
    virtual void				destroyAllTextures();
    
	virtual void				beginRendering();
    virtual void				endRendering();
    virtual void				setDisplaySize(const Size& sz);
    virtual const Size&			getDisplaySize() const;
    virtual const Vector2&		getDisplayDPI() const;
    virtual uint				getMaxTextureSize() const;
    virtual const String&		getIdentifierString() const;

	Texture&					createTexture(cocos2d::CCTexture2D* pTexture);	
	
	void DisableSeparateAlphaBlend();

	void InitRenderStateType();
	void SaveXPRenderState();
	void SaveUIRenderState();
	void RestoreXPRenderState();
	void RestorUIRenderState();
	void Reset();

	//\u52A8\u6001\u52A0\u8F7D\u56FE\u7247\u76F8\u5173
	void ResetRenderTextures();  //\u6E05\u7A7A\u6E32\u67D3\u8FC7\u7684\u7EB9\u7406
	void MarkRenderTexture(Texture* pTexture);
	bool isTextureRender(Texture& texture);
	void ReleaseTexture(Texture* texture);

	void MarkRenderTexture(Cocos2DTexture* pCocos2DTexture);
	bool isTextureValid(Cocos2DTexture* pCocos2DTexture);

	void SetPointMode(bool b);
    
    virtual void OnFrameEnd();

	Cocos2DTexture*		d_pDebugTexture;
    cocos2d::CCLayer*   d_pParent;
private:
    Cocos2DRenderer();
    virtual ~Cocos2DRenderer();

    Size getViewportSize();
    //! returns next power of 2 size if \a size is not power of 2
    float getSizeNextPOT(float sz) const;

    static String d_rendererID;
    Size d_displaySize;
    Vector2 d_displayDPI;
    RenderingRoot* d_defaultRoot;
    RenderTarget* d_defaultTarget;

    typedef std::vector<TextureTarget*> TextureTargetList;
    TextureTargetList d_textureTargets;

    typedef std::vector<Cocos2DGeometryBuffer*> GeometryBufferList;
    GeometryBufferList d_geometryBuffers;

    typedef std::vector<Cocos2DTexture*> TextureList;
    TextureList d_textures;
    TextureList d_loadingTextures;

    uint d_maxTextureSize;
    bool d_supportNPOTTex;
    bool d_supportNonSquareTex;
	bool d_SeparateAlphaBlendCap;

	typedef std::vector<Cocos2DTexture*> RenderTextureList;
	RenderTextureList d_RenderTextures;
    StateManager    d_stateManager;	

	/*typedef std::map<D3DRENDERSTATETYPE,DWORD> RenderStateMap;
	RenderStateMap d_mapRenderState;

	typedef std::map<D3DTEXTURESTAGESTATETYPE,DWORD> TextureStatgeStateType;
	TextureStatgeStateType d_mapTextureStage0State;
	TextureStatgeStateType d_mapTextureStage1State;

	typedef std::map<D3DSAMPLERSTATETYPE,DWORD> SampleStateMap;
	SampleStateMap d_mapSampleState;
	DWORD d_OldFVF;
    IDirect3DPixelShader9*  d_oldPixShader;
	IDirect3DVertexShader9* d_oldVertextShader;
	IDirect3DStateBlock9* d_XPStateBlock;
	IDirect3DStateBlock9* d_UISatteBlock;
	bool d_PointMode;*/
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#   pragma warning(pop)
#endif

#endif // end of guard _CEGUICocos2DRenderer_h_
