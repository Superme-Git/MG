#ifndef __Nuclear_RENDERER_H
#define __Nuclear_RENDERER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\freqcountable.h"
#include "..\common\xptypes.h"
#include "ibaserenderer.h"

namespace Nuclear
{	
	// 性能测试参数，release版无需实现
	struct RenderPerformance
	{
		int m_iManagedTextureCount;				// d3d管理的 texture 的个数
		int m_iManagedTextureMemory;			// d3d管理的 texture 所占总内存, 单位字节

		int m_iTotalPixelRenderedPerFrame;		// 每帧累积绘制纹理的总面积 单位像素数
		int m_iDrawPrimitiveCountPerFrame;		// 每帧 DrawPrimitive 函数的调用次数
		int m_iDrawPrimitiveUPCountPerFrame;	// 每帧 DrawPrimitiveUP 函数的调用次数
		int m_iDrawPictureCountPerFrame;		// 每帧 DrawPicture 函数的调用次数
		int m_iDrawLineCountPerFrame;			// 每帧 DrawLine 函数的调用次数
		int m_iDrawTriangleCountPerFrame;		// 每帧 DrawTriangle 函数的调用次数
		int m_iDrawBoxCountPerFrame;			// 每帧 DrawBox 函数的调用次数
		int m_iDrawTextCountPerFrame;			// 每帧 DrawText 函数的调用次数
		int m_iDrawCircleCountPerFrame;			// 每帧 DrawCircle 函数的调用次数
		int m_iDrawPolygonCountPerFrame;		// 每帧 DrawPolygon 函数的调用次数

		void ClearFrame()
		{
			m_iTotalPixelRenderedPerFrame = 0;
			m_iDrawPrimitiveCountPerFrame = 0;
			m_iDrawPrimitiveUPCountPerFrame = 0;
			m_iDrawPictureCountPerFrame = 0;
			m_iDrawLineCountPerFrame = 0;
			m_iDrawBoxCountPerFrame = 0;
			m_iDrawTriangleCountPerFrame = 0;
			m_iDrawTextCountPerFrame = 0;
			m_iDrawCircleCountPerFrame = 0;
			m_iDrawPolygonCountPerFrame = 0;
		}
	};

	struct DrawPictureParam
	{
		PictureHandle handle;
		const FRECT *pRect;
		const XPMATRIX3 *pMatrix;
		const XPCOLOR *pColor;	//等于NULL的时候，颜色值为0xffffffff;
		int colorCount;			//只能是1或者4，当pColor为空的时候，这个值没有意义
		XPTEXTURE_BLENDMODE blend;
		const FRECT *pSrcrect;
		bool bSpecAlpha;

		DrawPictureParam() 
		{
			bSpecAlpha = false;
			pMatrix = NULL;
			pColor = NULL;
			colorCount = 0;
			blend = XPTEXBM_DEFAULT;
			pSrcrect = NULL;
		}
	};

	struct DrawEllipsePictureParam
	{
		PictureHandle handle;
		float x;
		float y;
		float rx;
		float ry;
		float u0;//0--1坐标
		float v0;//0--1坐标
		float ru;//0--1坐标
		float rv;//0--1坐标
		XPCOLOR color; //顶点颜色
		XPTEXTUREADDRESS TextureMode;
		XPCOLOR borderColor; //XPTEXADDRESS_BORDER寻址模式的边颜色

		DrawEllipsePictureParam() 
		{
			handle = INVALID_PICTURE_HANDLE;
			x = 0.0f;
			y = 0.0f;
			rx = 0.0f;
			ry = 0.0f;
			u0 = 0.0f;
			v0 = 0.0f;
			ru = 0.0f;
			rv = 0.0f;
			color = 0xffffffff;
			TextureMode = XPTEXADDRESS_BORDER;
			borderColor = 0xff000000;
		}
	};

	struct DrawBatchPictureParam
	{
		PictureHandle handle;
		PictureHandle secHandle;	//这个阶段的纹理目前只能做乘法
		const FRECT *pRect;
		int rectCount;
		const XPMATRIX3 *pMatrix;
		const XPCOLOR *pColor;	//等于NULL的时候，颜色值为0xffffffff;
		int colorCount;			//只能是1或者4，当pColor为空的时候，这个值没有意义
		XPTEXTURE_BLENDMODE blend;
		const FRECT *pSrcrect;
		const FRECT *pSecSrcRect;	//第二阶段纹理的纹理坐标，默认为NULL，如果secHandle有合法值，但是这个为NULL，表示和第一纹理阶段的纹理坐标一样
		bool bSameuv; //默认true,表示所有的pRect纹理坐标相同。
					  //为false则从每个pRect对应的pSrcrect里去找纹理坐标, pSrcrect个数为rectCount个。
		bool bSecSameuv;	//默认为true，表示第二阶段纹理所有的纹理坐标都一样（和bSameuv的含义是一样的）
		bool bSpecAlpha;	//是否分开计算RenderTarget的Alpha，默认false

		DrawBatchPictureParam() : handle(INVALID_PICTURE_HANDLE), secHandle(INVALID_PICTURE_HANDLE), 
			pRect(NULL), rectCount(0), pMatrix(NULL), pColor(NULL), colorCount(0), blend(XPTEXBM_DEFAULT), 
			pSrcrect(NULL), pSecSrcRect(NULL), bSameuv(true), bSecSameuv(true), bSpecAlpha(false)
		{
		}
		DrawBatchPictureParam(const DrawPictureParam &dpp) : handle(dpp.handle), secHandle(INVALID_PICTURE_HANDLE), 
			pRect(dpp.pRect), rectCount(1), pMatrix(dpp.pMatrix), pColor(dpp.pColor), colorCount(dpp.colorCount), blend(dpp.blend), 
			pSrcrect(dpp.pSrcrect), pSecSrcRect(NULL), bSameuv(true), bSecSameuv(true), bSpecAlpha(dpp.bSpecAlpha)
		{
		}
	};
	
	// renderer 类型
	enum XPRENDERER_VERSION
	{
		XPRV_DEFAULT =		0,
		XPRV_DX9	 =	1
	};

	enum
	{
		XPCRF_VERTICALSYNC = 1 << 0,
	};

	enum XP_DEVICE_STATE
	{
		XPDS_NO_DEVICE,
		XPDS_INTERNAL_ERROR,
		XPDS_LOST_DEVICE,
		XPDS_RESET_ERROR,
		XPDS_OK,
	};

	class VideoTexture;
	class IParticleManager;
	class IFontManager;
	
	/*******************************************
	*	接口类                           *
	*******************************************/

	// renderer 接口
	class Renderer : public FreqCountable, public IBaseRenderer
	{
	public:
		const static float Z;
		// 构造析构
		Renderer() { }
		virtual ~Renderer() { }

		virtual VideoTexture* GetDshowMovieTexRender() = 0;
		virtual bool InitDShowTextureRenderer(const std::wstring& strVideoFile) = 0;
		//把视频画在指定的区域，如果bzoom为true，则缩放后绘制全部内容，反之则裁剪后1:1绘制。
		virtual bool DrawVideoTexture(const FRECT& frct, bool bZoom = true, const XPMATRIX3* pMatrix = NULL, XPCOLOR color=0xffffffff) = 0;
		virtual void ReleaseVideoRenderRes() = 0; //用完之后释放...
		// 创建销毁
		virtual bool Create(HWND hwnd, const XDisplayMode &dmode, DWORD flags) = 0;
		virtual void Destroy() = 0;

		// 改变源和目的矩形区域
		virtual void SetSourceRect(const CRECT* pSrcRect) { }
		virtual void SetDestRect(const CRECT* pDstRect) { }

		// 如果 *buf == NULL, 则 *buf 由函数内分配， 否则直接使用 *buf
		virtual bool PrintScreen(void**buf, int*size,XPIMAGE_FILEFORMAT filetype=XPIFF_JPG,const CRECT* src=NULL) = 0;
		virtual bool GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix = NULL, CRECT* pRect = NULL, 
			XPPIXEL_FORMAT fmt = XPPIXELFMT_DEFAULT) = 0;//XPPIXELFMT_DEFAULT自动选8888或者4444
		virtual PictureHandle GetBackBuffer() = 0;

		// 具体绘图
		bool DrawLine(float x1, float y1, float x2, float y2, XPCOLOR color)
		{
			return DrawLine(FRECT(x1, y1, x2, y2), color); 
		}
		virtual bool DrawLine(const FRECT &line, XPCOLOR color) = 0;
		virtual bool DrawLine(const FRECT *lines, int n, XPCOLOR color) = 0;

		virtual bool DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, XPCOLOR color) = 0;

		bool DrawBox(float x1, float y1, float x2, float y2, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, const XPMATRIX3 *pMatrix = NULL)
		{
			return DrawBox(FRECT(x1, y1, x2, y2), color, fillmode, pMatrix); 
		}
	
		virtual bool DrawBox(const FRECT &rect, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, const XPMATRIX3 *pMatrix = NULL) = 0;
		virtual bool DrawBox(const FRECT *rects, int n, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, const XPMATRIX3 *pMatrix = NULL) = 0;

		//fTheta 单位: 角度，向右为0
		virtual bool DrawFan(float x, float y, float r, float fTheta, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, const XPMATRIX3 *pMatrix = NULL) = 0;
		virtual bool DrawEllipseFan(float x, float y, float rx, float ry, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, float fStartAngle = 0.0f, float fEndAngle = 0.0f, const XPMATRIX3 *pMatrix = NULL) = 0;//单位：角度
		virtual bool DrawEllipse(float x, float y, float rx, float ry, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, float fAngle = 0.0f) = 0;
		virtual bool DrawCircle(float x, float y, float r, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL)
		{
			return DrawEllipse(x, y, r, r, color, fillmode);
		}
		virtual bool DrawPolygon(const std::vector<CPOINT> &pts, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL) = 0;

		virtual bool DrawTriangles(const std::vector<TRIANGLE> &triangles, XPCOLOR color, XPFILLMODE fillmode = XPFM_WIREFRAME) = 0;

		virtual bool DrawTriangleList(const TCVERTEX* pVertexs, int nVertexNum/*顶点个数*/) = 0;
		//顶点个数是3的整数倍

		virtual bool DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, XPCOLOR color = 0xffffffff) = 0;

		virtual bool DrawEllipsePicture(const DrawEllipsePictureParam& param) = 0;

		virtual PictureHandle ClonePicture(PictureHandle handle, bool bEqualScale)
		{
			int srcW, srcH, dstW, dstH;
			return ClonePicture(handle, 1.0f, bEqualScale, srcW, srcH, dstW, dstH);
		}
		virtual PictureHandle ClonePicture(PictureHandle handle, double scale, bool bEqualScale, int& srcW, int& srcH, int& dstW, int& dstH) = 0;
		// 从磁盘加载和绘制图片
		// 从windows本地路径加载图片
		virtual PictureHandle LoadPictureFromNativePath(const std::wstring &nativepath);
		// 从引擎资源路径加载图片
		virtual PictureHandle LoadPicture(const std::wstring &filename) = 0;
		virtual bool DrawPicture(const DrawPictureParam& param) = 0;
		virtual bool DrawBatchPicture(const DrawBatchPictureParam &param) = 0;
		virtual bool DrawPicture(PictureHandle handle, const FRECT &rect, XPCOLOR color) //简单接口
		{
			DrawPictureParam param;
			param.handle = handle;
			param.pRect = &rect;
			param.pColor = &color;
			param.colorCount = 1;
			return DrawPicture(param);
		}

		//绘制带索引的顶点buffer
		virtual bool DrawIndexVerticeBuffer(PictureHandle handle, const TCT1VERTEX* pVertexs, const WORD* pIndexs,
												int nVertexNum, int nIndexNum) = 0;

		// Rendertarget管理相关
		// 所有的RenderTargets在设备重置的时候会丢失，用户需要自己恢复
		// 创建 RenderTarget
		virtual PictureHandle CreateRenderTarget(int iWidth, int iHeight, XPTEXTURE_FORMAT texfmt) = 0;
		// 释放 RenderTarget
		virtual bool FreeRenderTarget(PictureHandle hRenderTarget) = 0;
		// 压入 RenderTarget
		virtual bool PushRenderTarget(PictureHandle hRenderTarget) = 0;
		// 弹出 RenderTarget
		virtual bool PopRenderTarget() = 0;
		
		// 效果相关
		virtual bool SelectRenderEffect(XPRENDER_EFFECT xpre) = 0;
		virtual bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam) = 0;
		virtual bool SetShaderTexture(XPRENDER_EFFECT xpre, const char* name, PictureHandle pic) = 0;
		virtual XPRENDER_EFFECT GetRenderEffect() = 0;


		// 绘制文本
		virtual bool DrawText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color) = 0;
		virtual bool DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color) = 0;

		// 光标
		virtual bool ShowCursor(bool f) = 0;
		virtual bool SetCursorPosition(int x, int y) = 0;
		virtual bool SetCursorPicture(PictureHandle handle, int offsetx = 0, int offsety = 0) = 0;
		virtual bool GetCurrentCursor(PictureHandle &handle, int& x, int& y) = 0;

		// 绘图框架
		virtual bool Begin(XPCOLOR color = 0xc090) = 0;
		virtual bool End();
		virtual bool Clear(XPCOLOR color) = 0;
		virtual XP_DEVICE_STATE TestDevice() = 0;
		virtual bool IsAutoTestDevice() = 0;
		virtual void SetAutoTestDevice(bool isAuto) = 0;
		virtual bool JustBegin() = 0;
		virtual bool JustEnd() = 0;


		// FPS统计
		virtual void ResetFPSCounter();
		virtual float GetFPS() const;
		virtual float GetLatestFPS() const;

		// util for pack tools
		/*
		clipmode == 0  不切割
		clipmode == 1  切割到最小，rect 返回切割后的图片在原图的位置
		clipmode == 2  切割后保持原图中心仍为中心
		*/

		//-Mended 2015
		virtual CRECT CalPicRect(const void *srcdata, int srcsize, XBuffer &destBuffer,
			XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum) = 0;
		virtual void DrawPicClip(const void *srcdata, int srcsize, XBuffer &destBuffer,
			XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum) = 0;

		virtual bool SaveTextureToDXSurface(const void *srcdata, int srcsize, XBuffer &destBuffer,
			XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum,int det = 4) = 0;
		virtual bool SplitTexture(const void *srcdata, int srcsize,XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, 
			CRECT &rect, std::vector<std::pair<CRECT,XBuffer> > &dstpics, int &row, int &col, int width = 512, int height = 512) =0;

		//对于每一帧，如果使用者手动设置了就不在调用, 否则就要调用来生成边界轮廓多边形
		//bfalg 暂时保留..
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, std::vector<CPOINT>& outLinePoint, bool bflag = false) = 0;

		//对指定区域FRECT frct寻找轮廓，frct的坐标是纹理坐标...
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, FRECT frct, std::vector<CPOINT>& outLinePoint, bool bflag = false) = 0;

		virtual std::wstring GetAdapterDescription() = 0;

		// performance
		virtual RenderPerformance GetPerformance() = 0;

		// 设备丢失恢复时的回调, 只能在Renderer线程调用，而且要保证renderer销毁前pTask指针一直有效
		virtual void SetRestoreTask(IRunnable *pTask) { }
		// 设备丢失时的回调, 只能在Renderer线程调用，而且要保证renderer销毁前pTask指针一直有效
		virtual void SetLostTask(IRunnable *pTask) { }

		// 可用纹理显存估计值，单位是 M
		virtual int GetAvailableTextureMem() = 0;
		virtual bool IsTextureCapsNoPow2() = 0;

		virtual IParticleManager* GetParticleManager() = 0;
		virtual IFontManager *GetFontManager() = 0;			

		bool SaveTextureToFile(PictureHandle handle, const std::wstring &path)
		{
			return SaveTextureToFile(handle, XPIFF_DDS, path);
		}
		virtual bool SaveTextureToFile(PictureHandle handle, XPIMAGE_FILEFORMAT filefmt, const std::wstring &path) = 0;
		virtual bool SaveTextureToFile(PictureHandle handle, XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, XBuffer &outbuffer) = 0;

		//目前只支持8888
		virtual bool ClearAlpha(PictureHandle handle) = 0;
		//frct 是handle的其中某一块，纹理坐标。(0.0f, 0.0f, 1.0f, 1.0f)表示整块纹理
	//	virtual bool SaveTextureToXBuffer(const void *srcdata, int srcsize, Nuclear::FRECT frct, XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, XBuffer &outbuffer) = 0;

		//大杀器，Debug用，path必须为windows目录，以\结尾
		virtual void DumpAllPicture(const std::wstring &path) = 0;
	};

	/*******************************************
	*	小胖相关函数                           *
	*******************************************/

	class CFileIOManager;
	// 创建函数
	bool CreateRenderer(Renderer **ppr, HWND hwnd, const XDisplayMode &dmode, CFileIOManager *pFileIOMan, DWORD flags = 0, XPRENDERER_VERSION rv = XPRV_DEFAULT);
	// 销毁函数
	void DestroyRenderer(Renderer *r);
};

#endif