#ifndef __Nuclear_DX9RENDERER_H
#define __Nuclear_DX9RENDERER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "renderer.h"

#include <DxErr.h>
#include "..\particlesystem\particlemanager.h"
#include "fontmanager.h"
#include "statemanager.h"
#include "d3dx9core.h"
#include "igetd3ddevice9.h"
#include "VideoTexture.h"

namespace Nuclear
{

	class DX9Renderer : public Renderer, public IGetD3DDevice9
	{	
		struct CTextureInfo
		{
			CRECT rect;
			IDirect3DTexture9* m_D3dTexture;

#ifdef XP_PERFORMANCE
			int mem;
#endif

			CTextureInfo(){
				rect.left = 0;
				rect.top = 0;
				rect.right = 0;
				rect.bottom = 0;
				m_D3dTexture = NULL;
#ifdef XP_PERFORMANCE
				mem = 0;
#endif
			}
		};

		struct CPicInfo 
		{
			PictureInfo m_info;
			std::vector<CTextureInfo> m_CTextureInfoVector;
		};

		struct XPIMAGE_INFO
		{
			XPIMAGE_FILEFORMAT ImageFileFormat;
			XPTEXTURE_FORMAT Format;
		};

		struct CXBufferPS 
		{
			LPDIRECT3DPIXELSHADER9 PSshader;
			LPD3DXCONSTANTTABLE pConstTablePS;
			CXBufferPS()
			{
				PSshader = NULL;
				pConstTablePS = NULL;
			}
		};

	public:
		static const DWORD TCVertexFVF = D3DFVF_XYZRHW | D3DFVF_DIFFUSE;
		static const DWORD TCT1VertexFVF = D3DFVF_XYZRHW | D3DFVF_DIFFUSE | D3DFVF_TEX2;
		static const DWORD UTCT1VertexFVF = D3DFVF_XYZ | D3DFVF_DIFFUSE | D3DFVF_TEX1;
		// Renderer �ӿ�ʵ��
	public:
		DX9Renderer(CFileIOManager *pFileIOMan) : m_pFileIOMan(pFileIOMan), m_bAutoTestDevice(true) { Init(); }
		virtual ~DX9Renderer() { }

		// ��������
		virtual bool Create(HWND hwnd, const XDisplayMode &dmode, DWORD flags);
		virtual void Destroy();

		// ��ʾģʽ
		virtual XDisplayMode GetDisplayMode() const;
		virtual bool SetDisplayMode(const XDisplayMode &dmode);
		virtual std::vector<XDisplayMode> EnumDisplayMode() const;

		// �ı�Դ��Ŀ�ľ�������
		virtual void SetSourceRect(const CRECT* pSrcRect);
		virtual void SetDestRect(const CRECT* pDstRect);

		virtual bool PrintScreen(void**buf, int*size,XPIMAGE_FILEFORMAT filetype=XPIFF_JPG,const CRECT* src=NULL);
		virtual bool GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix = NULL, CRECT* pRect = NULL, 
			XPPIXEL_FORMAT fmt = XPPIXELFMT_DEFAULT);//XPPIXELFMT_DEFAULT�Զ�ѡ8888����4444
		virtual PictureHandle GetBackBuffer();

		// �����ͼ
		virtual bool DrawLine(const FRECT &line, XPCOLOR color);
		virtual bool DrawLine(const FRECT *lines, int n, XPCOLOR color);

		virtual bool DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, XPCOLOR color);

		virtual bool DrawBox(const FRECT &rect, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, const XPMATRIX3 *pMatrix = NULL);
		virtual bool DrawBox(const FRECT *rects, int n, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, const XPMATRIX3 *pMatrix = NULL);
		virtual bool DrawEllipse(float x, float y, float rx, float ry, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, float fAngle = 0.0f);
		virtual bool DrawFan(float x, float y, float r, float fTheta, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, const XPMATRIX3 *pMatrix = NULL) ;
		virtual bool DrawEllipseFan(float x, float y, float rx, float ry, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, float fStartAngle = 0.0f, float fEndAngle = 0.0f, const XPMATRIX3 *pMatrix = NULL);
		virtual bool DrawPolygon(const std::vector<CPOINT> &pts, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL);

		virtual bool DrawTriangles(const std::vector<TRIANGLE> &triangles, XPCOLOR color, XPFILLMODE fillmode = XPFM_WIREFRAME);

		virtual bool DrawTriangleList(const TCVERTEX* pVertexs, int nVertexNum/*�������*/);

		virtual bool DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, XPCOLOR color = 0xffffffff);

		virtual bool DrawEllipsePicture(const DrawEllipsePictureParam& param);

		virtual bool DrawPicture(const DrawPictureParam& param);

		virtual bool SelectRenderEffect(XPRENDER_EFFECT xpre);
		virtual bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam);
		virtual bool SetShaderTexture(XPRENDER_EFFECT xpre, const char* name, PictureHandle pic);
		virtual XPRENDER_EFFECT GetRenderEffect();

		// Rendertarget�������
		// ���е�RenderTargets���豸���õ�ʱ��ᶪʧ���û���Ҫ�Լ��ָ�
		// ���� RenderTarget
		virtual PictureHandle CreateRenderTarget(int iWidth, int iHeight, XPTEXTURE_FORMAT texfmt);
		// �ͷ� RenderTarget
		virtual bool FreeRenderTarget(PictureHandle hRenderTarget);
		// ѹ�� RenderTarget
		virtual bool PushRenderTarget(PictureHandle hRenderTarget);
		// ���� RenderTarget
		virtual bool PopRenderTarget();

		// �о� batch �Ż��ؼ������ϲ㣬���Ըɴ������ʽ�ӿ�
		virtual bool DrawBatchPicture(const DrawBatchPictureParam &param);

		//���ƴ������Ķ���buffer
		virtual bool DrawIndexVerticeBuffer(PictureHandle handle, const TCT1VERTEX* pVertexs, const WORD* pIndexs, int nVertexNum, int nIndexNum);

		PictureHandle ClonePicture(PictureHandle handle, double scale, bool bEqualScale, int& srcW, int& srcH, int& dstW, int& dstH);

		// ע��ͼƬ 	
		virtual PictureHandle LoadPicture(const std::wstring& filename); // return pichandle
		virtual PictureHandle LoadPictureFromMem(const void *data, int size,PictureInfo *pPicInfo=NULL,XPTEXTURE_FORMAT texfmt=XPTEXFMT_DEFAULT,XPPOOL_TYPE pooltype=XPPOOL_MANAGED);

		virtual PictureHandle NewPicture(int width, int height, PictureInfo *pPicInfo =NULL, XPTEXTURE_FORMAT texfmt=XPTEXFMT_DEFAULT); // Ŀǰ������ 8888 �� 4444, ʹ��ǰ���ѯ�����ʽ
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, const CRECT* pRect = NULL); // Ŀǰ������ 8888 �� 4444, ʹ��ǰ���ѯ�����ʽ
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect = NULL); // Ŀǰ������ 8888 �� 4444, ʹ��ǰ���ѯ�����ʽ
		virtual bool FreePicture(PictureHandle handle);

		PictureHandle CreateTextureFromWebPFileInMemory(const void *data, int size, PictureInfo *pPicInfo, XPTEXTURE_FORMAT texfmt);
		// �����ı�
		virtual bool DrawText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color);
		virtual bool DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color);

		// ���
		virtual bool ShowCursor(bool f);
		virtual bool SetCursorPosition(int x, int y);
		virtual bool SetCursorPicture(PictureHandle handle, int offsetx = 0, int offsety = 0);
		virtual bool GetCurrentCursor(PictureHandle &handle, int& x, int& y);

		// ��ͼ���
		virtual bool Begin(XPCOLOR color = 0xc090);
		virtual bool End();
		virtual bool Clear(XPCOLOR color);
		virtual XP_DEVICE_STATE TestDevice();
		virtual bool JustBegin();
		virtual bool JustEnd();

		virtual bool IsAutoTestDevice() { return m_bAutoTestDevice; }
		virtual void SetAutoTestDevice(bool isAuto) { m_bAutoTestDevice = isAuto; }


		// from IBaseRenderer
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

		// ��TriangleFan��������Щ����
		virtual bool DrawTriangleFan(PictureHandle handle, const TCT1VERTEX* pVertexs, int nVertexNum/*�������*/);
		// ÿ��������4��������ɵľ��������ƣ�������= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const TCT1VERTEX* pVertexs, int nVertexNum/*�������*/);
		// ÿ��������4��������ɵľ��������ƣ�������= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const UTCT1VERTEX* pVertexs, int nVertexNum/*�������*/);

		// ÿ��������4��������ɵľ��������ƣ�������= nVertexNum/4
		// ����ֻ�ռ����ӣ�������..
		virtual bool CollectParticles(PictureHandle handle, XPTEXCOLORBLEND_TYPE colorBlend, PSL_RENDSTATE alphaBlend,
			const TCT1VERTEX* pVertexs, int nVertexNum/*�������*/);

		virtual bool BatchDrawParticles(); //

		// form IGetD3DDevice9
		virtual IDirect3DDevice9* GetDevice() const { return m_ifDevice; }
	public:
		// ��ȡһ��������Ϣ
		virtual bool GetPictureInfo(PictureHandle handle, PictureInfo& picinfo) const;
		virtual bool GetPictureInfo(PictureHandle handle, int& pitch, int& width, int& height) const;

		// util for pack tools
		virtual bool SaveTextureToFile(PictureHandle handle, XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, XBuffer &outbuffer);
		
		//-Mended 2015-Version 1
		int iPicWidthDelta ;
		int iPicHeightDelta;
		int iPicCounter;
		CRECT cFirstPicRect;
		//-

		//-Mended 2015-Version 2
		typedef std::vector<CRECT> CRectSet;
		CRectSet vCRectSet;
		int iIndexOfRect;
		bool bStartToRender;
		//-

		virtual bool SaveTextureToDXSurface(const void *srcdata, int srcsize, XBuffer &destBuffer,
			XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum,int det = 4);
		
		//-Mended 2015
		virtual CRECT CalPicRect(const void *srcdata, int srcsize, XBuffer &destBuffer,
			XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum);
		virtual void DrawPicClip(const void *srcdata, int srcsize, XBuffer &destBuffer,
			XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum);


		virtual bool SplitTexture(const void *srcdata, int srcsize,XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, 
			CRECT &rect, std::vector<std::pair<CRECT,XBuffer> > &dstpics, int &row, int &col, int width = 512, int height = 512);

		//frct ��handle������ĳһ�飬�������ꡣ(0.0f, 0.0f, 1.0f, 1.0f)��ʾ��������
	//	virtual bool SaveTextureToXBuffer(const void *srcdata, int srcsize, Nuclear::FRECT frct, XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, XBuffer &outbuffer);

		//����ÿһ֡�����ʹ�����ֶ������˾Ͳ��ڵ���, �����Ҫ���������ɱ߽����������
		//bfalg ��ʱ����..
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, std::vector<CPOINT>& outLinePoint, bool bflag = false);

		//��ָ������FRECT frctѰ��������frct����������������...
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, FRECT frct, std::vector<CPOINT>& outLinePoint, bool bflag = false);

		virtual std::wstring GetAdapterDescription();

		// performance
		virtual RenderPerformance GetPerformance();

		// ����ֵ
		virtual int GetAvailableTextureMem();

		//�Ƿ�֧�ַ�2������
		virtual bool IsTextureCapsNoPow2();

		// �豸��ʧ�ָ�ʱ�Ļص�
		virtual void SetRestoreTask(IRunnable *pTask);
		// �豸��ʧʱ�Ļص�
		virtual void SetLostTask(IRunnable *pTask);
		

		virtual IParticleManager* GetParticleManager() { return m_pParticleMan; }
		virtual IFontManager* GetFontManager() { return m_pFontMan; }

		virtual bool InitDShowTextureRenderer(const std::wstring& strVideoFile);
		virtual bool DrawVideoTexture(const FRECT& frct, bool bZoom = true, const XPMATRIX3* pMatrix = NULL, XPCOLOR color=0xffffffff);
		virtual void ReleaseVideoRenderRes();

		virtual VideoTexture* GetDshowMovieTexRender() { return m_pdshowTexRender; } //return NULL

		virtual bool SaveTextureToFile(PictureHandle handle, XPIMAGE_FILEFORMAT filefmt, const std::wstring &path);

		//Ŀǰֻ֧��8888
		virtual bool ClearAlpha(PictureHandle handle);

		//��ɱ����Debug�ã�path����ΪwindowsĿ¼����\��β
		virtual void DumpAllPicture(const std::wstring &path);

		//2015 Yao
		void ResizePic(int &value);
		bool Is2Power(int value);

		//todo
		//�ӿڰ���ogreMovieTexture.txt���淶����..manager...
		//ͬʱ���Ŷ����Ƶ..
		// ��Ա
	protected:
		VideoTexture* m_pdshowTexRender; //DshowMovieTexRender ---����
		
	protected:
		TCT1VERTEX*			m_pEllipsePicVertex;
		UINT				m_adapterid;
		D3DDEVTYPE			m_devtype;	
		D3DPRESENT_PARAMETERS m_d3dpp;
		CFileIOManager*		m_pFileIOMan;
		IDirect3D9*			m_ifD3D;
		IDirect3DDevice9*	m_ifDevice;
		HWND				m_hwnd;			// ����Ҳ����
		CRECT*				m_pSrcRect;
		CRECT*				m_pDstRect;
		D3DCAPS9			m_d3dCaps;
		bool				m_bDeviceLost;
		StateManager		m_stateMan;
		ParticleManager*	m_pParticleMan;
		CFontManager*		m_pFontMan;
		bool				m_bTextureCaps_pow2;
		DWORD				m_dwPixelShaderVersion;
		DWORD				m_dwVertexShaderVersion;

		bool				m_fUseCursor;
		PictureHandle		m_iCurCursorHandle;
		int					m_fCurCursorOffsetX;
		int					m_fCurCursorOffsetY;

		bool				m_bAutoTestDevice;

		// batch render
		enum { BATCH_IB_COUNT = 24576 }; //�����Կ�ֻ֧��WORD���������ֵ65535.
		//�߼��ϣ�IB��VB��������Сû���κι�ϵ
		//6144�պù���512*512��DistortionObjEffect���һ��������ȡ2����С..
		enum { BATCH_VB_COUNT = 8192};
		IDirect3DVertexBuffer9* m_batchVB;
		IDirect3DIndexBuffer9* m_batchIB;
		//	int m_iBatchOffset;
		int m_iBatchVBOffset;
		int m_iBatchIBOffset;

		IDirect3DVertexBuffer9* m_shapeVB;

		enum { MAX_PARTICLE_NUM = 6000 };
		IDirect3DVertexBuffer9* m_particleVB;
		IDirect3DVertexBuffer9* m_particleUVB;
		IDirect3DIndexBuffer9* m_particleIB;

		enum { MAX_TCVERTEX_NUM = 300 };
		IDirect3DVertexBuffer9* m_TCVERTEXVB;
		IDirect3DIndexBuffer9* m_TCVERTEXIB;
		int m_iBatchTCVERTEXVBOffset;
		int m_iBatchTCVERTEXIBOffset;

		std::wstring		m_adapterDes;
		RenderPerformance	m_performance;
		XBuffer				m_errTexBuf; // �������ͼƬ��������

		std::vector<IRunnable*> m_restoreTasks;
		std::vector<IRunnable*> m_lostTasks;

	protected:
		XDisplayMode m_curDisplayMode;
		std::vector<XDisplayMode> m_displayModes;
		std::set<XPTEXTURE_FORMAT> m_supportTexFmts[2]; // 0 : 16, 1 : 32
	private:
		// ͼƬ����
		PictureHandle m_picHandleSeed;
		//typedef stdext::hash_map<PictureHandle, CPicInfo> PictureMap;
		typedef std::map<PictureHandle, CPicInfo> PictureMap;
		PictureMap m_mapPictures;						// renderer���������ͼƬ

		// rendertarget ����
		typedef std::vector<IDirect3DSurface9*> RenderTargetStack;
		RenderTargetStack m_stkRenderTargets;			// ջ�е�RenderTargets
		std::set<PictureHandle> m_setRenderTargets;		// ����Create������RenderTargets���������е�Ԫ��һ����m_picmap��key set��

		// pixel shader ����
		typedef std::map<std::wstring, CXBufferPS> PixelShaderMap;
		PixelShaderMap m_pixelShaderMap; //�õ���ʱ���������ң��Ҳ�����load����

		//
		std::map<ULONG64, std::vector<TCT1VERTEX>> m_mapParticles;

	private:
		bool DrawCurParticles(ULONG64 param, const std::vector<TCT1VERTEX>& particles);

		ID3DXFont*			m_ifDefaultFont;
		ID3DXFont*			m_ifTmpFont;
		bool InitDefaultFont();
		bool InitDisplayMode(IDirect3D9* ifD3D9, const XDisplayMode &dmode);

		bool OnLostDevice();
		bool OnResetDevice();

		void ReleaseTexture();
		void ReleaseRenderTargets();

		//�������ֻ�Ƿ�������ļ�Ӧ����ʲô������������������������ļ����������������ʽ��ע��dataҪ�����ļ�ͷ
		bool GetCreateTextureFormatFromFileInMemory(const void *data, int size, XPIMAGE_INFO& info);

		const CPicInfo* GetPicInfo(PictureHandle handle) const;

		bool IsMode16() const { return m_curDisplayMode.bitperpix == 16; }
		bool IsTexfmtSupport(XPTEXTURE_FORMAT texfmt) const { return m_supportTexFmts[IsMode16()?0:1].find(texfmt) != m_supportTexFmts[IsMode16()?0:1].end(); }

		// todo return 0 ��ʾ�ɹ��� return 1 ��ʾ outofvideomemory, return -1 ��ʾ����ʧ��
		int GenTexture(IDirect3DTexture9* pSrcTex, XPTEXTURE_FORMAT texfmt, XPPOOL_TYPE pooltype, CTextureInfo& TextureInfo);
		// reset �豸�� newmode == NULL ��ʾʹ�õ�ǰ����
		bool ResetD3D(const XDisplayMode *newmode = NULL);
		bool InitBatchVB();
		void DestroyBatchVB();
	protected:
		void Init();
		// ռ��
	private:
		DX9Renderer(const DX9Renderer&);
		DX9Renderer& operator=(const DX9Renderer&);
	};
};

#endif