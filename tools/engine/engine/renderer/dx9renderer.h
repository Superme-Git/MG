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
		// Renderer 接口实现
	public:
		DX9Renderer(CFileIOManager *pFileIOMan) : m_pFileIOMan(pFileIOMan), m_bAutoTestDevice(true) { Init(); }
		virtual ~DX9Renderer() { }

		// 创建销毁
		virtual bool Create(HWND hwnd, const XDisplayMode &dmode, DWORD flags);
		virtual void Destroy();

		// 显示模式
		virtual XDisplayMode GetDisplayMode() const;
		virtual bool SetDisplayMode(const XDisplayMode &dmode);
		virtual std::vector<XDisplayMode> EnumDisplayMode() const;

		// 改变源和目的矩形区域
		virtual void SetSourceRect(const CRECT* pSrcRect);
		virtual void SetDestRect(const CRECT* pDstRect);

		virtual bool PrintScreen(void**buf, int*size,XPIMAGE_FILEFORMAT filetype=XPIFF_JPG,const CRECT* src=NULL);
		virtual bool GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix = NULL, CRECT* pRect = NULL, 
			XPPIXEL_FORMAT fmt = XPPIXELFMT_DEFAULT);//XPPIXELFMT_DEFAULT自动选8888或者4444
		virtual PictureHandle GetBackBuffer();

		// 具体绘图
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

		virtual bool DrawTriangleList(const TCVERTEX* pVertexs, int nVertexNum/*顶点个数*/);

		virtual bool DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, XPCOLOR color = 0xffffffff);

		virtual bool DrawEllipsePicture(const DrawEllipsePictureParam& param);

		virtual bool DrawPicture(const DrawPictureParam& param);

		virtual bool SelectRenderEffect(XPRENDER_EFFECT xpre);
		virtual bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam);
		virtual bool SetShaderTexture(XPRENDER_EFFECT xpre, const char* name, PictureHandle pic);
		virtual XPRENDER_EFFECT GetRenderEffect();

		// Rendertarget管理相关
		// 所有的RenderTargets在设备重置的时候会丢失，用户需要自己恢复
		// 创建 RenderTarget
		virtual PictureHandle CreateRenderTarget(int iWidth, int iHeight, XPTEXTURE_FORMAT texfmt);
		// 释放 RenderTarget
		virtual bool FreeRenderTarget(PictureHandle hRenderTarget);
		// 压入 RenderTarget
		virtual bool PushRenderTarget(PictureHandle hRenderTarget);
		// 弹出 RenderTarget
		virtual bool PopRenderTarget();

		// 感觉 batch 优化关键还在上层，所以干脆给出显式接口
		virtual bool DrawBatchPicture(const DrawBatchPictureParam &param);

		//绘制带索引的顶点buffer
		virtual bool DrawIndexVerticeBuffer(PictureHandle handle, const TCT1VERTEX* pVertexs, const WORD* pIndexs, int nVertexNum, int nIndexNum);

		PictureHandle ClonePicture(PictureHandle handle, double scale, bool bEqualScale, int& srcW, int& srcH, int& dstW, int& dstH);

		// 注册图片 	
		virtual PictureHandle LoadPicture(const std::wstring& filename); // return pichandle
		virtual PictureHandle LoadPictureFromMem(const void *data, int size,PictureInfo *pPicInfo=NULL,XPTEXTURE_FORMAT texfmt=XPTEXFMT_DEFAULT,XPPOOL_TYPE pooltype=XPPOOL_MANAGED);

		virtual PictureHandle NewPicture(int width, int height, PictureInfo *pPicInfo =NULL, XPTEXTURE_FORMAT texfmt=XPTEXFMT_DEFAULT); // 目前可能是 8888 或 4444, 使用前请查询纹理格式
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, const CRECT* pRect = NULL); // 目前可能是 8888 或 4444, 使用前请查询纹理格式
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect = NULL); // 目前可能是 8888 或 4444, 使用前请查询纹理格式
		virtual bool FreePicture(PictureHandle handle);

		PictureHandle CreateTextureFromWebPFileInMemory(const void *data, int size, PictureInfo *pPicInfo, XPTEXTURE_FORMAT texfmt);
		// 绘制文本
		virtual bool DrawText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color);
		virtual bool DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color);

		// 光标
		virtual bool ShowCursor(bool f);
		virtual bool SetCursorPosition(int x, int y);
		virtual bool SetCursorPicture(PictureHandle handle, int offsetx = 0, int offsety = 0);
		virtual bool GetCurrentCursor(PictureHandle &handle, int& x, int& y);

		// 绘图框架
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

		// 用TriangleFan来绘制这些顶点
		virtual bool DrawTriangleFan(PictureHandle handle, const TCT1VERTEX* pVertexs, int nVertexNum/*顶点个数*/);
		// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const TCT1VERTEX* pVertexs, int nVertexNum/*顶点个数*/);
		// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const UTCT1VERTEX* pVertexs, int nVertexNum/*顶点个数*/);

		// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
		// 这里只收集粒子，不绘制..
		virtual bool CollectParticles(PictureHandle handle, XPTEXCOLORBLEND_TYPE colorBlend, PSL_RENDSTATE alphaBlend,
			const TCT1VERTEX* pVertexs, int nVertexNum/*顶点个数*/);

		virtual bool BatchDrawParticles(); //

		// form IGetD3DDevice9
		virtual IDirect3DDevice9* GetDevice() const { return m_ifDevice; }
	public:
		// 获取一个纹理信息
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

		//frct 是handle的其中某一块，纹理坐标。(0.0f, 0.0f, 1.0f, 1.0f)表示整块纹理
	//	virtual bool SaveTextureToXBuffer(const void *srcdata, int srcsize, Nuclear::FRECT frct, XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, XBuffer &outbuffer);

		//对于每一帧，如果使用者手动设置了就不在调用, 否则就要调用来生成边界轮廓多边形
		//bfalg 暂时保留..
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, std::vector<CPOINT>& outLinePoint, bool bflag = false);

		//对指定区域FRECT frct寻找轮廓，frct的坐标是纹理坐标...
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, FRECT frct, std::vector<CPOINT>& outLinePoint, bool bflag = false);

		virtual std::wstring GetAdapterDescription();

		// performance
		virtual RenderPerformance GetPerformance();

		// 估计值
		virtual int GetAvailableTextureMem();

		//是否支持非2幂纹理
		virtual bool IsTextureCapsNoPow2();

		// 设备丢失恢复时的回调
		virtual void SetRestoreTask(IRunnable *pTask);
		// 设备丢失时的回调
		virtual void SetLostTask(IRunnable *pTask);
		

		virtual IParticleManager* GetParticleManager() { return m_pParticleMan; }
		virtual IFontManager* GetFontManager() { return m_pFontMan; }

		virtual bool InitDShowTextureRenderer(const std::wstring& strVideoFile);
		virtual bool DrawVideoTexture(const FRECT& frct, bool bZoom = true, const XPMATRIX3* pMatrix = NULL, XPCOLOR color=0xffffffff);
		virtual void ReleaseVideoRenderRes();

		virtual VideoTexture* GetDshowMovieTexRender() { return m_pdshowTexRender; } //return NULL

		virtual bool SaveTextureToFile(PictureHandle handle, XPIMAGE_FILEFORMAT filefmt, const std::wstring &path);

		//目前只支持8888
		virtual bool ClearAlpha(PictureHandle handle);

		//大杀器，Debug用，path必须为windows目录，以\结尾
		virtual void DumpAllPicture(const std::wstring &path);

		//2015 Yao
		void ResizePic(int &value);
		bool Is2Power(int value);

		//todo
		//接口按照ogreMovieTexture.txt来规范定义..manager...
		//同时播放多个视频..
		// 成员
	protected:
		VideoTexture* m_pdshowTexRender; //DshowMovieTexRender ---改名
		
	protected:
		TCT1VERTEX*			m_pEllipsePicVertex;
		UINT				m_adapterid;
		D3DDEVTYPE			m_devtype;	
		D3DPRESENT_PARAMETERS m_d3dpp;
		CFileIOManager*		m_pFileIOMan;
		IDirect3D9*			m_ifD3D;
		IDirect3DDevice9*	m_ifDevice;
		HWND				m_hwnd;			// 不记也可以
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
		enum { BATCH_IB_COUNT = 24576 }; //早期显卡只支持WORD索引，最大值65535.
		//逻辑上，IB与VB的数量大小没有任何关系
		//6144刚好够画512*512的DistortionObjEffect物件一个，这里取2倍大小..
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
		XBuffer				m_errTexBuf; // 错误替代图片，调试用

		std::vector<IRunnable*> m_restoreTasks;
		std::vector<IRunnable*> m_lostTasks;

	protected:
		XDisplayMode m_curDisplayMode;
		std::vector<XDisplayMode> m_displayModes;
		std::set<XPTEXTURE_FORMAT> m_supportTexFmts[2]; // 0 : 16, 1 : 32
	private:
		// 图片数据
		PictureHandle m_picHandleSeed;
		//typedef stdext::hash_map<PictureHandle, CPicInfo> PictureMap;
		typedef std::map<PictureHandle, CPicInfo> PictureMap;
		PictureMap m_mapPictures;						// renderer管理的所有图片

		// rendertarget 数据
		typedef std::vector<IDirect3DSurface9*> RenderTargetStack;
		RenderTargetStack m_stkRenderTargets;			// 栈中的RenderTargets
		std::set<PictureHandle> m_setRenderTargets;		// 所有Create出来的RenderTargets，此容器中的元素一定在m_picmap的key set中

		// pixel shader 数据
		typedef std::map<std::wstring, CXBufferPS> PixelShaderMap;
		PixelShaderMap m_pixelShaderMap; //用到的时候来这里找，找不到则load进来

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

		//这个方法只是返回这个文件应该用什么方法来创建纹理，而不是这个文件本身真正的纹理格式。注意data要包含文件头
		bool GetCreateTextureFormatFromFileInMemory(const void *data, int size, XPIMAGE_INFO& info);

		const CPicInfo* GetPicInfo(PictureHandle handle) const;

		bool IsMode16() const { return m_curDisplayMode.bitperpix == 16; }
		bool IsTexfmtSupport(XPTEXTURE_FORMAT texfmt) const { return m_supportTexFmts[IsMode16()?0:1].find(texfmt) != m_supportTexFmts[IsMode16()?0:1].end(); }

		// todo return 0 表示成功， return 1 表示 outofvideomemory, return -1 表示其他失败
		int GenTexture(IDirect3DTexture9* pSrcTex, XPTEXTURE_FORMAT texfmt, XPPOOL_TYPE pooltype, CTextureInfo& TextureInfo);
		// reset 设备， newmode == NULL 表示使用当前设置
		bool ResetD3D(const XDisplayMode *newmode = NULL);
		bool InitBatchVB();
		void DestroyBatchVB();
	protected:
		void Init();
		// 占坑
	private:
		DX9Renderer(const DX9Renderer&);
		DX9Renderer& operator=(const DX9Renderer&);
	};
};

#endif