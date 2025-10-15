#define NOMINMAX

#include "CEGUICocos2DRenderer.h"

#include <CEGUIRenderingRoot.h>
#include <CEGUIExceptions.h>
#include <CEGUISystem.h>
#include <CEGUIDefaultResourceProvider.h>
#include <CEGUIPfsResourceProvider.h>

#include "CEGUICocos2DTexture.h"
#include "CEGUICocos2DGeometryBuffer.h"
#include "CEGUICocos2DRenderTarget.h"
#include "CEGUICocos2DViewportTarget.h"
#include "CEGUICocos2DTextureTarget.h"
#include "CEGUILoadingTaskManager.h"

#include <algorithm>

#include "CEGUIImageCodec.h"
#include "CEGUIWindow.h"

#define RELEASE_TEXTURE_TIME 100

// Start of CEGUI namespace section
namespace CEGUI
{
	bool g_redraw = false;
	class CLoadFileTask : public ITask
	{
		Cocos2DRenderer* m_pRender;
		Cocos2DTexture*  m_pTexture;
#ifdef PUBLISHED_VERSION
		LJFM::LJFMID  m_texFile;
#else
		RawDataContainer m_texFile;
#endif
		String           m_strFileName;
		String           m_strResourceGroup;
	public:
		CLoadFileTask(Cocos2DRenderer* aPRender, Cocos2DTexture* aPTexture, String filename, String resGroup)
			: ITask(eTPFile)
			, m_pRender(aPRender)
			, m_pTexture(aPTexture)
			, m_strFileName(filename)
			, m_strResourceGroup(resGroup)
		{}

		Cocos2DTexture* GetTexturePtr()
		{
			return m_pTexture;
		}

		String GetFileName()
		{
			return m_strFileName;
		}

#ifdef PUBLISHED_VERSION
		LJFM::LJFMID& GetFileData()
		{
			return m_texFile;
		}
#else
		RawDataContainer& GetFileData()
		{
			return m_texFile;
		}
#endif

		virtual void Run()
		{
			System::getSingleton().getResourceProvider()->loadRawDataContainer(m_strFileName, m_texFile, m_strResourceGroup);

#ifdef PUBLISHED_VERSION
			if (m_texFile.GetSize() == 0 || m_texFile.GetSize() == -1 || !m_texFile.GetData())
			{
				return;
			}
#else
			if (m_texFile.getSize() == 0 || m_texFile.getSize() == -1 || !m_texFile.getSize())
			{
				return;
			}
#endif
			m_pRender->OnFileLoaded(this);
		}
	};

	class CParseImageTask : public ITask
	{
		Cocos2DRenderer*    m_pRender;
		Cocos2DTexture*     m_pTexture;
		CLoadFileTask*      m_pLoadFileTask;
#ifdef PUBLISHED_VERSION
		CodecPrivateData*   m_pCodecData;
#endif
	public:
		CParseImageTask(Cocos2DRenderer* aPRender, Cocos2DTexture* aPTexture, CLoadFileTask* aPTask)
			: ITask(eTPParse)
			, m_pRender(aPRender)
			, m_pTexture(aPTexture)
			, m_pLoadFileTask(aPTask)
#ifdef PUBLISHED_VERSION
			, m_pCodecData(NULL)
#endif
		{}
		virtual ~CParseImageTask()
		{
			delete m_pLoadFileTask;
#ifdef PUBLISHED_VERSION
			delete m_pCodecData;
#endif
		}

#ifdef PUBLISHED_VERSION
		CodecPrivateData* GetParseData()
		{
			return m_pCodecData;
		}
#endif        
		Cocos2DTexture* GetTexturePtr()
		{
			return m_pTexture;
		}

		CLoadFileTask* GetLoadTask()
		{
			return m_pLoadFileTask;
		}

		virtual void Run()
		{
			System* sys = System::getSingletonPtr();
			if (!sys)
				CEGUI_THROW(RendererException("OpenGLTexture::loadFromFile - "
				"CEGUI::System object has not been created: "
				"unable to access ImageCodec."));

#ifdef PUBLISHED_VERSION
			Texture* res = sys->getImageCodec().load(m_pLoadFileTask->GetFileData(), m_pTexture, &m_pCodecData, true);
#else
			Texture* res = sys->getImageCodec().load(m_pLoadFileTask->GetFileData(), m_pTexture);
#endif

			if (!res)
				return;
			//                CEGUI_THROW(RendererException("OpenGLTexture::loadFromFile - " +
			//                                              sys->getImageCodec().getIdentifierString() +
			//                                              " failed to load image '" + m_pLoadFileTask->GetFileName() + "'."));

			//tell render that this image is loaded;
			m_pRender->OnImageParsed(this);
		}
	};

	void Cocos2DRenderer::CheckLoadingTexture(Cocos2DTexture* aPTexture)
	{
		{//sync loaded finished
			core::CMutex::Scoped lockLoaded(m_mutexLoadedTextures);
			std::map<Cocos2DTexture*, STextureLoadItem>::iterator itLoaded = m_mapLoadedTexture.find(aPTexture);
			if (itLoaded != m_mapLoadedTexture.end())
			{
				//tex = iterLoaded->first;
				CParseImageTask* pTask = (CParseImageTask*)itLoaded->second.pTask;

#ifdef PUBLISHED_VERSION
				CEGUI::Size sz(pTask->GetParseData()->GetWidth(), pTask->GetParseData()->GetHeight());
				if (pTask->GetParseData()->GetFmt() == Texture::PF_PVR2 || pTask->GetParseData()->GetFmt() == Texture::PF_PVR4 || pTask->GetParseData()->GetFmt() == Texture::PF_ETC)
				{
					aPTexture->loadFromBuffer(pTask->GetLoadTask()->GetFileData().GetData(), sz, pTask->GetParseData()->GetFmt());
				}
				else if (pTask->GetParseData()->GetFmt() == Texture::PF_ATC_Exp || pTask->GetParseData()->GetFmt() == Texture::PF_ATC_Int)
				{
					aPTexture->loadFromBuffer(pTask->GetLoadTask()->GetFileData().GetData(), sz, pTask->GetParseData()->GetFmt());
				}
				else if (pTask->GetParseData()->GetFmt() == Texture::PF_DXT3 || pTask->GetParseData()->GetFmt() == Texture::PF_DXT5)
				{
					aPTexture->loadFromBuffer(pTask->GetLoadTask()->GetFileData().GetData(), sz, pTask->GetParseData()->GetFmt());
				}
				else
				{
					aPTexture->loadFromBuffer(pTask->GetParseData()->GetDataPtr(), sz, pTask->GetParseData()->GetFmt());
				}
#endif                
				aPTexture->m_bIsLoading = false;
				g_redraw = true;
				TextureList::iterator iLd = std::find(d_loadingTextures.begin(), d_loadingTextures.end(), aPTexture);
				if (iLd != d_loadingTextures.end()) {
					d_loadingTextures.erase(iLd);
				}
				delete pTask;
				m_mapLoadedTexture.erase(itLoaded);
			}
		}
	}

	void Cocos2DRenderer::OnFileLoaded(ITask* aPTask)
	{
		ITask* pTask = new CParseImageTask(this, ((CLoadFileTask*)aPTask)->GetTexturePtr(), (CLoadFileTask*)aPTask);
		if (!CCEGUITaskManager::GetInstancePtr()->QueueTask(pTask))
		{
			delete pTask;
		}
	}

	void Cocos2DRenderer::OnImageParsed(ITask* aPTask)
	{
		{
			core::CMutex::Scoped lockLoading(m_mutexLoadingTextures);
			m_mapLoadingTexture.erase(((CParseImageTask*)aPTask)->GetLoadTask()->GetFileName());
		}
		{
			core::CMutex::Scoped lockLoaded(m_mutexLoadedTextures);
			STextureLoadItem item;
			item.pTask = aPTask;
			m_mapLoadedTexture.insert(std::make_pair(((CParseImageTask*)aPTask)->GetTexturePtr(), item));
		}
	}
	extern int g_loadingTextCount;
	void Cocos2DRenderer::OnFrameEnd()
	{
		//check if loaded texture is been used
		{
			core::CMutex::Scoped mutexLoaded(m_mutexLoadedTextures);

			bool bRedraw = false;
			std::map<Cocos2DTexture*, STextureLoadItem>::iterator iter = m_mapLoadedTexture.begin();
			for (; iter != m_mapLoadedTexture.end();)
			{
				if (iter->second.iCheckCount == RELEASE_TEXTURE_TIME)
				{
					TextureList::iterator iLd = std::find(d_loadingTextures.begin(), d_loadingTextures.end(), iter->first);
					if (iLd != d_loadingTextures.end())
					{
						d_loadingTextures.erase(iLd);
					}
					delete iter->first;
					delete iter->second.pTask;
					m_mapLoadedTexture.erase(iter++);
				}
				else
				{
					iter->second.iCheckCount++;
					++iter;
				}
				bRedraw = true;
			}
			if (bRedraw)
			{
				System::getSingleton().getGUISheet()->invalidate(true);
			}
		}

		if (g_redraw)
		{
			System::getSingleton().getGUISheet()->invalidate(true);
			g_redraw = false;
		}

		static bool bLastLoadingState = false;
		g_bIsTextLoading = (!CCEGUITaskManager::GetInstancePtr()->LoadingFontEmpty()) || (g_loadingTextCount != 0);
		if (g_bIsTextLoading != bLastLoadingState)
		{
			if (g_bIsTextLoading == false)
			{
				bLastLoadingState = false;
				g_bIsTextLoading = true;
			}
			else
			{
				bLastLoadingState = g_bIsTextLoading;
			}
		}
	}

	//----------------------------------------------------------------------------//
	String Cocos2DRenderer::d_rendererID(
		"CEGUI::Cocos2DRenderer - Cocos2D renderer "
		"module.");

	//----------------------------------------------------------------------------//
	static const float s_identityMatrix[16] =
	{
		1.0, 0.0, 0.0, 0.0,
		0.0, 1.0, 0.0, 0.0,
		0.0, 0.0, 1.0, 0.0,
		0.0, 0.0, 0.0, 1.0
	};

	//----------------------------------------------------------------------------//
	Cocos2DRenderer& Cocos2DRenderer::bootstrapSystem(cocos2d::CCLayer* parent, const char* logFile)
	{
		if (System::getSingletonPtr())
			CEGUI_THROW(InvalidRequestException("OpenGLRenderer::bootstrapSystem: "
			"CEGUI::System object is already initialised."));

		Cocos2DRenderer& renderer(create());
		renderer.d_pParent = parent;

		//renderer.d_pDebugTexture = &dynamic_cast<Cocos2DTexture&>(renderer.createTexture("logo.png", "imagesets"));
		return renderer;
	}

	void Cocos2DRenderer::destroySystem()
	{
		System* sys;
		if (!(sys = System::getSingletonPtr()))
			CEGUI_THROW(InvalidRequestException("OpenGLRenderer::destroySystem: "
			"CEGUI::System object is not created or was already destroyed."));

		Cocos2DRenderer* renderer = static_cast<Cocos2DRenderer*>(sys->getRenderer());
#ifdef PUBLISHED_VERSION
		PFSResourceProvider* rp = static_cast<PFSResourceProvider*>(sys->getResourceProvider());
#else
		DefaultResourceProvider* rp = static_cast<DefaultResourceProvider*>(sys->getResourceProvider());
#endif

		System::destroy();
		delete rp;
		destroy(*renderer);
	}

	//----------------------------------------------------------------------------//
	Cocos2DRenderer& Cocos2DRenderer::create()
	{
		return *new Cocos2DRenderer();
	}

	void Cocos2DRenderer::destroy(Cocos2DRenderer& renderer)
	{
		delete &renderer;
	}

	//----------------------------------------------------------------------------//
	RenderingRoot& Cocos2DRenderer::getDefaultRenderingRoot()
	{
		return *d_defaultRoot;
	}

	//----------------------------------------------------------------------------//
	GeometryBuffer& Cocos2DRenderer::createGeometryBuffer()
	{
		Cocos2DGeometryBuffer* b = new Cocos2DGeometryBuffer();
		d_geometryBuffers.push_back(b);
		return *b;
	}

	void Cocos2DRenderer::destroyGeometryBuffer(const GeometryBuffer* buffer)
	{
		GeometryBufferList::iterator i = std::find(d_geometryBuffers.begin(),
			d_geometryBuffers.end(),
			buffer);

		if (d_geometryBuffers.end() != i)
		{
			d_geometryBuffers.erase(i);

			// ycl destroyGeometryBuffer 之前必须确保该 GeometryBuffer 已经从 RenderQueue 中移除了
			getDefaultRenderingRoot().removeGeometryBuffer(RQ_BASE, buffer);

			delete buffer;
		}
	}

	void Cocos2DRenderer::destroyAllGeometryBuffers()
	{
		while (!d_geometryBuffers.empty())
			destroyGeometryBuffer(*d_geometryBuffers.begin());
	}

	//----------------------------------------------------------------------------//
	TextureTarget* Cocos2DRenderer::createTextureTarget()
	{
		TextureTarget* t = new Cocos2DTextureTarget(*this);
		d_textureTargets.push_back(t);
		return t;
	}

	void Cocos2DRenderer::destroyTextureTarget(TextureTarget* target)
	{
		TextureTargetList::iterator i = std::find(d_textureTargets.begin(),
			d_textureTargets.end(),
			target);

		if (d_textureTargets.end() != i)
		{
			d_textureTargets.erase(i);
			delete target;
		}
	}

	void Cocos2DRenderer::destroyAllTextureTargets()
	{
		while (!d_textureTargets.empty())
			destroyTextureTarget(*d_textureTargets.begin());
	}

	//----------------------------------------------------------------------------//
	//Texture ops
	Texture& Cocos2DRenderer::createTexture()
	{
		Cocos2DTexture* tex = new Cocos2DTexture(*this);
		d_textures.push_back(tex);
		return *tex;
	}

	Texture& Cocos2DRenderer::createTexture(const String& filename, const String& resourceGroup, float priority, bool synload)
	{
#if (defined WIN7_32) && (defined _DEBUG)
		std::wstring strFileName = PFSResourceProvider::GUIStringToWString(filename);
#endif

#ifdef PUBLISHED_VERSION
		if (synload) // 异步加载.
#endif
		{
			core::CMutex::Scoped lockLoading(m_mutexLoadingTextures);
			std::map<String, Cocos2DTexture*>::iterator iterLoaded = m_mapLoadingTexture.find(filename);
			if (iterLoaded == m_mapLoadingTexture.end())
			{
				Cocos2DTexture* pTex = new Cocos2DTexture(*this, filename, resourceGroup);
				ITask* pTask = new CLoadFileTask(this, pTex, filename, resourceGroup);
				pTask->SetPriority(priority);
				d_textures.push_back(pTex);
				d_loadingTextures.push_back(pTex);
				pTex->m_bIsLoading = true;
				m_mapLoadingTexture.insert(std::make_pair(filename, pTex));
				if (!CCEGUITaskManager::GetInstancePtr()->QueueTask(pTask))
				{
					delete pTask;
				}
				return *pTex;
			}
			else
			{
				return *iterLoaded->second;
			}
		}
#ifdef PUBLISHED_VERSION
		else // 同步加载.
		{
			Cocos2DTexture* pTex = new Cocos2DTexture(*this, filename, resourceGroup);
			LJFM::LJFMID texFile;
			System::getSingleton().getResourceProvider()->loadRawDataContainer(filename, texFile, resourceGroup);

			CodecPrivateData* pCodecData = NULL;
			Texture* res = System::getSingleton().getImageCodec().load(texFile, pTex, &pCodecData, false);
			pTex->m_bIsLoading = false;
			pTex->m_bIsLoadFromFile = false;
			d_textures.push_back(pTex);
			delete pCodecData;
			return *pTex;
		}
#endif
	}

	Texture& Cocos2DRenderer::createTexture(const Size& size)
	{
		Cocos2DTexture* tex = new Cocos2DTexture(*this, size);
		d_textures.push_back(tex);
		return *tex;
	}

	Texture& Cocos2DRenderer::createTexture(cocos2d::CCTexture2D* pTexture)
	{
		Cocos2DTexture* tex = new Cocos2DTexture(*this, pTexture);
		d_textures.push_back(tex);
		return *tex;
	}

	void Cocos2DRenderer::destroyTexture(Texture* texture)
	{
		TextureList::iterator it = std::find(d_textures.begin(),
			d_textures.end(),
			texture);

		if (d_textures.end() != it)
		{
			d_textures.erase(it);
		}

		TextureList::iterator iLd = std::find(d_loadingTextures.begin(), d_loadingTextures.end(), texture);
		if (iLd == d_loadingTextures.end())
		{
#if (defined WIN7_32) && (defined _DEBUG)
			std::wstring strName;
			Cocos2DTexture* cocosTex = dynamic_cast<Cocos2DTexture*>(texture);
			if (cocosTex)
			{
				strName = PFSResourceProvider::GUIStringToWString(cocosTex->getFileName());
			}
#endif
			delete texture;
		}
	}

	void Cocos2DRenderer::destroyAllTextures()
	{
		TextureList::iterator it = d_loadingTextures.begin();
		while (it != d_loadingTextures.end())
		{
			delete *it;
			++it;
		}
		d_loadingTextures.clear();

		d_textures.clear();
	}

	//----------------------------------------------------------------------------//
	void Cocos2DRenderer::beginRendering()
	{
		kmGLMatrixMode(KM_GL_PROJECTION);
		kmGLPushMatrix();
		kmGLMatrixMode(KM_GL_MODELVIEW);
		kmGLPushMatrix();

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glEnable(GL_BLEND);
		cocos2d::ccGLBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		d_stateManager.d_ScissorEnabled = glIsEnabled(GL_SCISSOR_TEST);
		glGetIntegerv(GL_SCISSOR_BOX, (GLint*)&d_stateManager.d_ScissorBox[0]);

		glEnable(GL_SCISSOR_TEST);
		cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColor);
		m_program = cocos2d::CCShaderCache::sharedShaderCache()->programForKey(kCCShader_PositionTextureColor);
		ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_PosColorTex);
	}

	void Cocos2DRenderer::InitRenderStateType()
	{
	}

	void Cocos2DRenderer::SaveXPRenderState()
	{
	}
	void Cocos2DRenderer::SaveUIRenderState()
	{
	}
	void Cocos2DRenderer::RestoreXPRenderState()
	{
	}

	void Cocos2DRenderer::RestorUIRenderState()
	{
	}

	void Cocos2DRenderer::endRendering()
	{
		kmGLMatrixMode(KM_GL_PROJECTION);
		kmGLPopMatrix();
		kmGLMatrixMode(KM_GL_MODELVIEW);
		kmGLPopMatrix();
		glEnable(GL_BLEND);
		cocos2d::ccGLBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		//glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

		if (!d_stateManager.d_ScissorEnabled) {
			glDisable(GL_SCISSOR_TEST);
		}
		glScissor(d_stateManager.d_ScissorBox[0], d_stateManager.d_ScissorBox[1],
			d_stateManager.d_ScissorBox[2], d_stateManager.d_ScissorBox[3]);
		cocos2d::CCShaderCache::sharedShaderCache()->popShader();
		return;
	}

	//----------------------------------------------------------------------------//
	void Cocos2DRenderer::setDisplaySize(const Size& sz)
	{
		if (sz != d_displaySize)
		{
			d_displaySize = sz;

			// FIXME: This is probably not the right thing to do in all cases.
			Rect area(d_defaultTarget->getArea());
			area.setSize(sz);
			d_defaultTarget->setArea(area);
		}
	}

	const Size& Cocos2DRenderer::getDisplaySize() const
	{
		return d_displaySize;
	}

	const Vector2& Cocos2DRenderer::getDisplayDPI() const
	{
		return d_displayDPI;
	}

	uint Cocos2DRenderer::getMaxTextureSize() const
	{
		return d_maxTextureSize;
	}

	const String& Cocos2DRenderer::getIdentifierString() const
	{
		return d_rendererID;
	}

	//----------------------------------------------------------------------------//
	Cocos2DRenderer::Cocos2DRenderer() :
		d_displaySize(getViewportSize()),
		d_displayDPI(384, 384),
		d_defaultRoot(0),
		d_defaultTarget(0),
		d_SeparateAlphaBlendCap(false),
		m_program(NULL)
	{
		GLint max_tex_size;
		glGetIntegerv(GL_MAX_TEXTURE_SIZE, &max_tex_size);
		d_maxTextureSize = max_tex_size;

		/* d_supportNonSquareTex = !(caps.TextureCaps & D3DPTEXTURECAPS_SQUAREONLY);

		 d_supportNPOTTex = !(caps.TextureCaps & D3DPTEXTURECAPS_POW2) ||
		 (caps.TextureCaps & D3DPTEXTURECAPS_NONPOW2CONDITIONAL);
		 d_SeparateAlphaBlendCap=(caps.PrimitiveMiscCaps & D3DPMISCCAPS_SEPARATEALPHABLEND) != 0;*/

		d_supportNonSquareTex = false;
		d_supportNPOTTex = cocos2d::CCConfiguration::sharedConfiguration()->supportsNPOT();

		d_SeparateAlphaBlendCap = false;

		d_defaultTarget = new Cocos2DViewportTarget(*this);
		d_defaultRoot = new RenderingRoot(*d_defaultTarget);

		//InitRenderStateType();
	}

	Cocos2DRenderer::~Cocos2DRenderer()
	{
		destroyAllGeometryBuffers();
		destroyAllTextureTargets();
		destroyAllTextures();

		delete d_defaultRoot;
		delete d_defaultTarget;
	}

	Size Cocos2DRenderer::getViewportSize()
	{
		GLint vp[4];
		glGetIntegerv(GL_VIEWPORT, vp);
		return Size(static_cast<float>(vp[2]), static_cast<float>(vp[3]));
	}

	//----------------------------------------------------------------------------//
	void Cocos2DRenderer::preD3DReset()
	{
		// perform pre-reset on texture targets
		//   TextureTargetList::iterator target_iterator = d_textureTargets.begin();
		//   for (; target_iterator != d_textureTargets.end(); ++target_iterator)
		//       static_cast<Direct3D9TextureTarget*>(*target_iterator)->preD3DReset();

		//   // perform pre-reset on textures
		//   TextureList::iterator texture_iterator = d_textures.begin();
		//   for (; texture_iterator != d_textures.end(); ++texture_iterator)
		//       (*texture_iterator)->preD3DReset();

		//if (d_UISatteBlock)
		//{
		//	d_UISatteBlock->Release();
		//	d_UISatteBlock=NULL;
		//}

		//if (d_XPStateBlock)
		//{
		//	d_XPStateBlock->Release();
		//	d_XPStateBlock=NULL;
		//}
	}

	void Cocos2DRenderer::postD3DReset()
	{
		// perform post-reset on textures
		//TextureList::iterator texture_iterator = d_textures.begin();
		//for (; texture_iterator != d_textures.end(); ++texture_iterator)
		//    (*texture_iterator)->postD3DReset();

		//// perform post-reset on texture targets
		//TextureTargetList::iterator target_iterator = d_textureTargets.begin();
		//for (; target_iterator != d_textureTargets.end(); ++target_iterator)
		//    static_cast<Direct3D9TextureTarget*>(*target_iterator)->postD3DReset();

		//// notify system about the (possibly) new viewport size.
		//System::getSingleton().notifyDisplaySizeChanged(getViewportSize());
	}

	//----------------------------------------------------------------------------//
	//Texture& Cocos2DRenderer::createTexture(LPDIRECT3DTEXTURE9 texture)
	//{
	//    Cocos2DTexture* tex = new Cocos2DTexture(*this, texture);
	//    d_textures.push_back(tex);
	//    return *tex;
	//}

	//----------------------------------------------------------------------------//
	bool Cocos2DRenderer::supportsNonSquareTexture()
	{
		return d_supportNonSquareTex;
	}

	//----------------------------------------------------------------------------//
	bool Cocos2DRenderer::supportsNPOTTextures()
	{
		return d_supportNPOTTex;
	}

	//----------------------------------------------------------------------------//
	Size Cocos2DRenderer::getAdjustedSize(const Size& sz)
	{
		Size s(sz);

		if (!d_supportNPOTTex)
		{
			s.d_width = getSizeNextPOT(sz.d_width);
			s.d_height = getSizeNextPOT(sz.d_height);
		}
		if (!d_supportNonSquareTex)
			s.d_width = s.d_height =
			ceguimax(s.d_width, s.d_height);

		return s;
	}

	float Cocos2DRenderer::getSizeNextPOT(float sz) const
	{
		uint size = static_cast<uint>(sz);

		// if not power of 2
		if ((size & (size - 1)) || !size)
		{
			int log = 0;

			// get integer log of 'size' to base 2
			while (size >>= 1)
				++log;

			// use log to calculate value to use as size.
			size = (2 << log);
		}

		return static_cast<float>(size);
	}

	void Cocos2DRenderer::Reset()
	{
		//d_defaultRoot->Reset();
	}

	void Cocos2DRenderer::ResetRenderTextures()  //???\u2019\u2030\u00F7??\u03C0?\u03BC????\u00EC
	{
		d_RenderTextures.clear();
	}

	void Cocos2DRenderer::MarkRenderTexture(Texture* pTexture)
	{
		RenderTextureList::iterator it = std::find(d_RenderTextures.begin(),
			d_RenderTextures.end(), pTexture);
		if (it == d_RenderTextures.end())
		{
			d_RenderTextures.push_back(static_cast<Cocos2DTexture*>(pTexture));
		}
	}

	bool Cocos2DRenderer::isTextureRender(Texture& texture)
	{
		RenderTextureList::iterator it = std::find(d_RenderTextures.begin(), d_RenderTextures.end(), &texture);
		if (it != d_RenderTextures.end())
		{
			return true;
		}
		return false;
	}

	void Cocos2DRenderer::ReleaseTexture(Texture* texture)
	{
		RenderTextureList::iterator it = std::find(d_RenderTextures.begin(), d_RenderTextures.end(), texture);
		if (it != d_RenderTextures.end())
		{
			d_RenderTextures.erase(it);
		}
		destroyTexture(texture);
	}

	bool Cocos2DRenderer::isTextureValid(Cocos2DTexture* pCocos2DTexture)
	{
		TextureList::iterator it = std::find(d_textures.begin(), d_textures.end(),
			pCocos2DTexture);
		return it != d_textures.end();

		//TextureList::iterator it=d_textures.begin();
		//for (;it!=d_textures.end();++it)
		//{
		//	//Direct3D9Texture* pTexture=(*it);
		//	if ((*it) == pCocos2DTexture)
		//	{
		//		return true;
		//	}
		//}
		//return false;
	}

	void Cocos2DRenderer::MarkRenderTexture(Cocos2DTexture* pCocos2DTexture)
	{
		if (isTextureValid(pCocos2DTexture))
		{
			MarkRenderTexture((Texture*)(pCocos2DTexture));
		}
	}

	void Cocos2DRenderer::SetPointMode(bool b)
	{
		//	d_PointMode=b;
	}


} // End of  CEGUI namespace section
