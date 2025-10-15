//  cocos2d_render.h
//  engine

#ifndef __Nuclear_cocos2d_render_H__
#define __Nuclear_cocos2d_render_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nurenderer.h"

#include "../particlesystem/nuparticlemanager.h"
#include "nufontmanager.h"
#include "nustatemanager.h"
#include "nuigetd3ddevice9.h"

namespace Nuclear
{
    class NuclearFileIOManager;
    
    class Cocos2dRenderTarget : public cocos2d::CCTexture2D
    {
    public:
        Cocos2dRenderTarget()
        {
            m_pRenderTexture = NULL;
        }
        virtual ~Cocos2dRenderTarget()
        {
            if (m_pRenderTexture)
			{
                //delete m_pRenderTexture;
                m_pRenderTexture->release();
                m_pRenderTexture = NULL;
            }
        }
        
        class MyRenderTexture : public cocos2d::CCRenderTexture
        {
            public:
            cocos2d::CCTexture2D* GetTexture(){ return m_pTexture; }
        };
        
        static Cocos2dRenderTarget* CreateRenderTarget(int aIWidth, int aIHeight, NuclearTextureFormat aTFormat);
        
        cocos2d::CCRenderTexture* getRT() { return m_pRenderTexture; }

    private:
        MyRenderTexture*    m_pRenderTexture;        
    };
    
	class Cocos2dRenderer : public Renderer
	{	
	public:
		struct CTextureInfo
		{
			NuclearRect rect;
            cocos2d::CCTexture2D* m_pTexture;
			std::wstring fileuri;
            
#ifdef XP_PERFORMANCE
			int mem;
#endif            
			CTextureInfo()
			{
				rect.left = 0;
				rect.top = 0;
				rect.right = 0;
				rect.bottom = 0;
				m_pTexture = NULL;
#ifdef XP_PERFORMANCE
				mem = 0;
#endif
			}

			~CTextureInfo()
			{
				if (m_pTexture)
				{
					//delete m_pTexture;
					m_pTexture = NULL;
				}
			}
		};
        
		struct CPicInfo 
		{
			NuclearPictureInfo m_info;
			std::vector<CTextureInfo> m_CTextureInfoVector;
		};

	private:
		struct XPIMAGE_INFO
		{
			NuclearImageFileFormat ImageFileFormat;
			NuclearTextureFormat Format;
		};
        
		struct CXBufferPS 
		{
			int PSshader;
			int pConstTablePS;
			CXBufferPS()
			{
				PSshader = 0;
				pConstTablePS = 0;
			}
		};
	protected:
		struct RenderTargetData
		{
            Cocos2dRenderTarget* pRT;
			int width;
			int height;
		};
        
		typedef std::vector<RenderTargetData> RenderTargetStack;
        
        RenderTargetStack   m_renderTargetStack;

	public:
		Cocos2dRenderer(NuclearFileIOManager *pFileIOMan) : m_pFileIOMan(pFileIOMan), m_bAutoTestDevice(true) { initial(); }
		virtual ~Cocos2dRenderer();
        bool IsMode16() { return false; }

		virtual XPCREATE_RENDERER_RESULT Create(const NuclearDisplayMode &dmode, DWORD flags, NuclearMultiSampleType mstype);
		virtual void Destroy();

		virtual bool IsTextureExist(PictureHandle handle);
        virtual bool IsTextureEtc(PictureHandle handle);

		virtual NuclearDisplayMode GetDisplayMode() const ;
        
		virtual void SetScissorRect(const NuclearRect* pRect);
        
		virtual bool PrintScreen(void**buf, int*size,NuclearImageFileFormat filetype=XPIFF_JPG,const NuclearRect* src=NULL);
		virtual bool GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix = NULL, NuclearRect* pRect = NULL,  NuclearPixelFormat fmt = XPPIXELFMT_DEFAULT);
		virtual PictureHandle GetBackBuffer();
        
		virtual bool DrawLine(const NuclearFRectt &line, NuclearColor color);
		virtual bool DrawLine(const NuclearFRectt *lines, int n, NuclearColor color);
        
		virtual bool DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, NuclearColor color);
        
		virtual bool DrawBox(const NuclearFRectt &rect, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL);
		virtual bool DrawBox(const NuclearFRectt *rects, int n, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL);
		virtual bool DrawEllipse(float x, float y, float rx, float ry, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, float fAngle = 0.0f);
		virtual bool DrawFan(float x, float y, float r, float fTheta, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL) ;
		virtual bool DrawEllipseFan(float x, float y, float rx, float ry, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, float fStartAngle = 0.0f, float fEndAngle = 0.0f, const NuclearMatrix3 *pMatrix = NULL);
		virtual bool DrawPolygon(const std::vector<NuclearPoint> &pts, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL);
        
		virtual bool DrawTriangles(const std::vector<NuclearTriangle> &triangles, NuclearColor color, NuclearFillMode fillmode = XPFM_WIREFRAME);
        
		virtual bool DrawTriangleList(const NuclearTCVertex* pVertexs, int nVertexNum);
        
		virtual bool DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, NuclearColor color = 0xffffffff);
        
		virtual bool DrawEllipsePicture(const DrawEllipsePictureParam& param);
        
		virtual bool DrawPicture(const DrawPictureParam& param);

		virtual bool DrawPicture(PictureHandle picHandle, float left, float top, float right, float bottom);
        
		virtual bool SelectRenderEffect(XPRENDER_EFFECT xpre);
		virtual bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam);
		virtual XPRENDER_EFFECT GetRenderEffect();
        
		virtual PictureHandle CreateRenderTarget(int iWidth, int iHeight, NuclearTextureFormat texfmt);

		virtual bool FreeRenderTarget(PictureHandle hRenderTarget);

		virtual bool PushRenderTarget(PictureHandle hRenderTarget);

		virtual bool PopRenderTarget();
        
		virtual bool DrawBatchPicture(const DrawBatchPictureParam &param);
        
		virtual bool DrawIndexVerticeBuffer(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, const WORD* pIndexs, int nVertexNum, int nIndexNum);
        
		virtual PictureHandle LoadPicture(const std::wstring& filename, NuclearPictureInfo* pPicinfo = NULL);
        virtual void CachePicture(PictureHandle pic, const std::wstring &filename);
		virtual PictureHandle LoadPictureFromMem(const void *data, int size, NuclearPictureInfo *pPicInfo = NULL, NuclearTextureFormat texfmt = XPTEXFMT_DEFAULT, NuclearPoolType pooltype = XPPOOL_MANAGED, bool bCache = true, PictureHandle handle = 0, int iScale = 100);
		virtual bool GetTextData(const char* pChar, const std::string &font, int size, unsigned char*& pData, int& w, int& h);

		virtual bool LoadCCImageFromMem(cocos2d::CCImage* pImage, NuclearTextureFormat texfmt, const void *data, int size);
		virtual PictureHandle LoadPictureFromCCImage(cocos2d::CCImage* image, NuclearTextureFormat texfmt, NuclearPictureInfo *pPicInfo = NULL, NuclearPoolType pooltype = XPPOOL_MANAGED, bool bCache = true, PictureHandle handle = 0);

		virtual PictureHandle NewPicture(int width, int height, NuclearPictureInfo *pPicInfo =NULL, NuclearTextureFormat texfmt=XPTEXFMT_DEFAULT);
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, const NuclearRect* pRect = NULL);
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const NuclearRect* pRect = NULL);
		virtual bool FreePicture(PictureHandle handle);
        
		// »æÖÆÎÄ±¾
		virtual bool DrawText(LPCTSTR text, int nstr, LPRECT rect, NuclearColor color);
		virtual bool DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, NuclearColor color);
        
		virtual bool IsAutoTestDevice() { return m_bAutoTestDevice; }
		virtual void SetAutoTestDevice(bool isAuto) { m_bAutoTestDevice = isAuto; }
        
	public:
		virtual void SetAlphaBlendEnable(bool f);
		virtual bool GetAlphaBlendEnable() const;
		virtual void SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend);
		virtual XPALPHABLEND_TYPE GetAlphaBlendSrcType() const;
		virtual void SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend);
		virtual XPALPHABLEND_TYPE GetAlphaBlendDstType() const;
        
		virtual void SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop);
		virtual XPTEXCOLORBLEND_TYPE GetTextureColorBlendType() const;
        
		virtual void SetSeparateAlphaBlend(bool f);
		virtual bool GetSeparateAlphaBlend() const;
        
		virtual void SetRenderEffect(XPRENDER_EFFECT re);
		virtual XPRENDER_EFFECT GetRenderEffect() const;
        
		virtual bool DrawTriangleFan(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, int nVertexNum/*??\u03BC?\u220F???*/);

		virtual bool DrawParticles(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, int nVertexNum/*??\u03BC?\u220F???*/);

		virtual bool DrawParticles(PictureHandle handle, const NuclearUTCT1Vertex* pVertexs, int nVertexNum/*??\u03BC?\u220F???*/);
        
		virtual bool CollectParticles(PictureHandle handle, XPTEXCOLORBLEND_TYPE colorBlend, PSL_RENDSTATE alphaBlend,
                                      const NuclearTCT1Vertex* pVertexs, int nVertexNum/*??\u03BC?\u220F???*/);
        
		virtual bool BatchDrawParticles();

	public:

		virtual bool GetPictureInfo(PictureHandle handle, NuclearPictureInfo& picinfo) const;
		virtual bool GetPictureInfo(PictureHandle handle, int& pitch, int& width, int& height) const;
		virtual bool GetPictureHandle(PictureHandle& handle, const std::wstring& fileuri) const;

		// util for pack tools
		virtual bool SaveTextureToFile(PictureHandle handle, NuclearTextureFormat texfmt, NuclearImageFileFormat filefmt, NuclearBuffer &outbuffer);
		virtual bool SaveTextureToDXSurface(const void *srcdata, int srcsize, NuclearBuffer &destBuffer, NuclearTextureFormat texfmt, NuclearImageFileFormat filefmt, NuclearRect &rect, int clipmode, bool setNonzeroAlpha2One);
		virtual bool SplitTexture(PictureHandle handle, NuclearImageFileFormat filefmt, NuclearRect &rect, std::vector<std::pair<NuclearRect,NuclearBuffer> > &dstpics, int &row, int &col, int width = 512, int height = 512);
        
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, std::vector<NuclearPoint>& outLinePoint, bool bflag = false);
        
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, NuclearFRectt frct, std::vector<NuclearPoint>& outLinePoint, bool bflag = false);
        
		virtual std::wstring GetAdapterDescription();
        
#ifdef XP_PERFORMANCE
		virtual RenderPerformance& GetPerformance();
#endif
        
		virtual int GetAvailableTextureMem();
        
		virtual bool IsTextureCapsNoPow2();
        
		virtual void GetAvailableMultiSampleType(bool window, bool isMode16, std::vector<NuclearMultiSampleType> &types);
		virtual NuclearMultiSampleType GetCurrentMultiSampleType() const { return m_xpMultisampleType; }
        
		virtual void SetRestoreTask(INuclearRunnable *pTask);

		virtual void SetLostTask(INuclearRunnable *pTask);
        
		virtual IParticleManager* GetParticleManager() { return m_pParticleMan; }
		virtual IFontManager* GetFontManager() { return m_pFontMan; }
        
		virtual bool SaveTextureToFile(PictureHandle handle, NuclearImageFileFormat filefmt, const std::wstring &path);
        
		virtual void DumpAllPicture(const std::wstring &path);
        
        virtual PictureHandle LoadPictureFromNativePath(const std::string &nativepath);

		virtual NuclearPictureInfo GetPictureSizeInfo(PictureHandle handle);

		virtual const void* GetTexInfo(PictureHandle handle);
		
	protected:

		bool				m_bUseLJFM;

		NuclearTCT1Vertex*		m_pEllipsePicVertex;
		unsigned int		m_adapterid;
		
		NuclearFileIOManager*		m_pFileIOMan;
		
		StateManager		m_stateMan;
		ParticleManager*	m_pParticleMan;
		CFontManager*		m_pFontMan;
		
		DWORD				m_dwPixelShaderVersion;
		DWORD				m_dwVertexShaderVersion;
        
		bool				m_bTextureCaps_pow2;
		bool				m_bDeviceLost;
		bool				m_fUseCursor;
		bool				m_bAutoTestDevice;
        
		PictureHandle		m_iCurCursorHandle;
		int					m_fCurCursorOffsetX;
		int					m_fCurCursorOffsetY;
        
		// batch render
		enum { BATCH_IB_COUNT = 24576 };

		enum { BATCH_VB_COUNT = 8192};

		int m_iBatchVBOffset;
		int m_iBatchIBOffset;
        
		enum { MAX_PARTICLE_NUM = 6000 };

        GLuint          m_particleVB;
        GLuint          m_particleUVB;
        GLuint          m_particleIB;
        GLushort*       m_pIndices;
        
		enum { MAX_TCVERTEX_NUM = 300 };

		int m_iBatchTCVERTEXVBOffset;
		int m_iBatchTCVERTEXIBOffset;
        
		std::wstring		m_adapterDes;
#ifdef XP_PERFORMANCE
		RenderPerformance	m_performance;
#endif
		NuclearBuffer				m_errTexBuf;
        
		std::vector<INuclearRunnable*> m_restoreTasks;
		std::vector<INuclearRunnable*> m_lostTasks;
        
		float				m_fViewportScale;
		NuclearMultiSampleType  m_xpMultisampleType;
        
	protected:
		std::vector<NuclearDisplayMode> m_displayModes;
		std::set<NuclearTextureFormat> m_supportTexFmts[2]; // 0 : 16, 1 : 32

	private:
		PictureHandle m_picHandleSeed;
		typedef std::map<PictureHandle, CPicInfo> PictureMap;
		PictureMap m_mapPictures;
        std::map<PictureHandle, Cocos2dRenderTarget*> m_mapRenderTargets;
		std::set<PictureHandle> m_setRenderTargets;
        
		typedef std::map<std::wstring, CXBufferPS> PixelShaderMap;
		PixelShaderMap m_pixelShaderMap;
        
		std::map<ULONG64, std::vector<NuclearTCT1Vertex> > m_mapParticles;
        
    public:
        bool OnResetDevice();
		virtual void ResetAllTextureUName();
        virtual void OnReloadAllTexture();
        
	private:
		bool DrawCurParticles(ULONG64 param, const std::vector<NuclearTCT1Vertex>& particles);

		bool InitDefaultFont();
        
		bool OnLostDevice();
         
		bool GetCreateTextureFormatFromFileInMemory(const void *data, int size, XPIMAGE_INFO& info);
        
		const CPicInfo* GetPicInfo(PictureHandle handle) const;
        
		bool IsTexfmtSupport(NuclearTextureFormat texfmt) const {  return true; }
        
		int GenTexture(NuclearTextureFormat texfmt, NuclearPoolType pooltype, CTextureInfo& TextureInfo);

		bool ResetD3D(const NuclearDisplayMode *newmode = NULL);
		bool InitBatchVB();
		void DestroyBatchVB();

	protected:
		void initial();

		PictureHandle checkPicture(const std::wstring &fileuri);

	private:
		Cocos2dRenderer(const Cocos2dRenderer&);
		Cocos2dRenderer& operator=(const Cocos2dRenderer&);
	};
};

#endif
