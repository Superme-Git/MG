#ifndef __Nuclear_RENDERER_H__
#define __Nuclear_RENDERER_H__

#include "../common/nuxptypes.h"
#include "nuibaserenderer.h"

namespace Nuclear
{	
	// 性能测试参数
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
		int m_iTotalLoadTimePerFrame;			// 每帧主线程总共用于加载的时间（单位：毫秒）
		int m_iLoadXmlTimePerFrame;				// 每帧主线程用于加载Xml文件的时间（单位：毫秒）
		int m_iLoadTextureTimePerFrame;			// 每帧主线程用于加载纹理文件的时间（单位：毫秒）
		int m_iUpdateTimePerSecond;				// 每秒主线程用于MainLoop的时间（单位：毫秒）
		int m_iRenderTimePerSecond;				// 每秒主线程用于Render的时间（单位：毫秒）
		int m_iGameTickTimePerSecond;			// 每秒主线程用于OnTick的时间（单位：毫秒）
		int m_iLuaTickTimePerSecond;			// 每秒主线程用于LuaTick的时间（单位：毫秒）

		void ClearFrame()
		{
			m_iTotalPixelRenderedPerFrame   = 0;
			m_iDrawPrimitiveCountPerFrame   = 0;
			m_iDrawPrimitiveUPCountPerFrame = 0;
			m_iDrawPictureCountPerFrame     = 0;
			m_iDrawLineCountPerFrame        = 0;
			m_iDrawBoxCountPerFrame         = 0;
			m_iDrawTriangleCountPerFrame    = 0;
			m_iDrawTextCountPerFrame        = 0;
			m_iDrawCircleCountPerFrame      = 0;
			m_iDrawPolygonCountPerFrame     = 0;
			//m_iUpdateTimePerSecond		= 0;
			//m_iRenderTimePerSecond		= 0;
			//m_iGameTickTimePerSecond		= 0;
			// m_iLuaTickTimePerSecond		= 0;
		}
	};

	struct DrawPictureParam
	{
		PictureHandle       handle;
		PictureHandle       handlePart;
		const NuclearFRectt*        pRect;
		const NuclearMatrix3*    pMatrix;
		const NuclearColor*      pColor;	//等于NULL的时候，颜色值为0xffffffff;
		int                 colorCount;			//只能是1或者4，当pColor为空的时候，这个值没有意义
		NuclearTextureBlendMode blend;
		const NuclearFRectt*        pSrcrect;
		bool                bSpecAlpha;
        
        bool                bUseBW;
        float               fGreyPercent;
        float               fRedPercent;

		int					iShaderType;	// 使用的Shader类型,默认为0, 0:正常渲染,1:HSV
		float				pPartParam0[4];
		float				pPartParam1[4];
		float				pPartParam2[4];

		DrawPictureParam() 
		{
			handle = 0;
			handlePart = 0;
			bSpecAlpha  = false;
			pMatrix     = NULL;
			pColor      = NULL;
			colorCount  = 0;
			blend       = XPTEXBM_DEFAULT;
			pSrcrect    = NULL;
            
            bUseBW      = false;
            fGreyPercent= 0.f;
            fRedPercent = 0.f;
			iShaderType = 0;
			pPartParam0[0] = 0;
			pPartParam0[1] = 0;
			pPartParam0[2] = 0;
			pPartParam0[3] = 0;
			pPartParam1[0] = 0;
			pPartParam1[1] = 0;
			pPartParam1[2] = 0;
			pPartParam1[3] = 0;
			pPartParam2[0] = 0;
			pPartParam2[1] = 0;
			pPartParam2[2] = 0;
			pPartParam2[3] = 0;
		}
		DrawPictureParam(const NuclearFRectt* picRect, PictureHandle picHandle) {
			bSpecAlpha = false;
			pMatrix = NULL;
			pColor = NULL;
			colorCount = 0;
			blend = XPTEXBM_DEFAULT;
			pSrcrect = NULL;

			bUseBW = false;
			fGreyPercent = 0.f;
			fRedPercent = 0.f;
			handle = picHandle;
			pRect = picRect;
			iShaderType = 0;
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
		NuclearColor color; //顶点颜色
		NuclearTextureAddRess TextureMode;
		NuclearColor borderColor; //XPTEXADDRESS_BORDER寻址模式的边颜色

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
		const NuclearFRectt *pRect;
		int rectCount;
		const NuclearMatrix3 *pMatrix;
		const NuclearColor *pColor;	//等于NULL的时候，颜色值为0xffffffff;
		int colorCount;			//只能是1或者4，当pColor为空的时候，这个值没有意义
		NuclearTextureBlendMode blend;
		const NuclearFRectt *pSrcrect;
		const NuclearFRectt *pSecSrcRect;	//第二阶段纹理的纹理坐标，默认为NULL，如果secHandle有合法值，但是这个为NULL，表示和第一纹理阶段的纹理坐标一样
		bool bSameuv; //默认true,表示所有的pRect纹理坐标相同,为false则从每个pRect对应的pSrcrect里去找纹理坐标, pSrcrect个数为rectCount个。
		bool bSecSameuv;	//默认为true，表示第二阶段纹理所有的纹理坐标都一样（和bSameuv的含义是一样的）
		bool bSpecAlpha;	//是否分开计算RenderTarget的Alpha，默认false

		DrawBatchPictureParam() : handle(INVALID_PICTURE_HANDLE), secHandle(INVALID_PICTURE_HANDLE), pRect(NULL), rectCount(0), pMatrix(NULL), pColor(NULL), colorCount(0), blend(XPTEXBM_DEFAULT),pSrcrect(NULL), pSecSrcRect(NULL), bSameuv(true), bSecSameuv(true), bSpecAlpha(false)
		{
		}
		DrawBatchPictureParam(const DrawPictureParam &dpp) : handle(dpp.handle), secHandle(INVALID_PICTURE_HANDLE), pRect(dpp.pRect), rectCount(1), pMatrix(dpp.pMatrix), pColor(dpp.pColor), colorCount(dpp.colorCount), blend(dpp.blend), pSrcrect(dpp.pSrcrect), pSecSrcRect(NULL), bSameuv(true), bSecSameuv(true), bSpecAlpha(dpp.bSpecAlpha)
		{
		}
	};
	
	// renderer 类型
	enum XPRENDERER_VERSION
	{
		XPRV_DEFAULT =	0,
		XPRV_DX9	 =	1,
        XPRV_COCOS2D =  2,
	};

	enum
	{
		XPCRF_VERTICALSYNC = 1 << 0,
	};

	enum XPCREATE_RENDERER_RESULT
	{
		XPCRR_NULL_POINTER = -8,
		XPCRR_GET_DIRECT3D = -7,
		XPCRR_CREATE_DEVICE = -6,
		XPCRR_INIT_STATE_MANAGER = -5,
		XPCRR_CREATE_DEPTHSTENCIL_SURFACE = -4,
		XPCRR_CREATE_SECOND_DEPTHSTENCIL_SURFACE = -3,
		XPCRR_SET_DEPTHSTENCIL_SURFACE = -2,
		XPCRR_SHADER_VERSION = -1,
		XPCRR_OK = 0,
	};

	enum XP_DEVICE_STATE
	{
		XPDS_NO_DEVICE,
		XPDS_INTERNAL_ERROR,
		XPDS_LOST_DEVICE,
		XPDS_RESET_ERROR,
		XPDS_OK,
	};

	enum XP_SC_DEPTHSTENCIL_TYPE
	{
		XPSDT_NONE = 0,
		XPSDT_USE_MAIN,
		XPSDT_INDEPENDENCE,
	};

	class VideoTexture;
	class IParticleManager;
	class IFontManager;
	
	/*******************************************
	*	接口类                           *
	*******************************************/
	class Renderer : /*public NuclearFreqCountable,*/ public IBaseRenderer
	{
	public:
		const static float Z;

		Renderer() { }
		virtual ~Renderer() { }
        
        virtual bool OnResetDevice() = 0;
		virtual void OnReloadAllTexture() = 0;
        virtual void ResetAllTextureUName() = 0;
        
		virtual XPCREATE_RENDERER_RESULT Create(const NuclearDisplayMode &dmode, DWORD flags, NuclearMultiSampleType mstype) = 0;
		virtual void Destroy() = 0;

		//设置剪裁框
		virtual void SetScissorRect(const NuclearRect* pRect) = 0;
		// 如果 *buf == NULL, 则 *buf 由函数内分配， 否则直接使用 *buf
		virtual bool PrintScreen(void**buf, int*size,NuclearImageFileFormat filetype=XPIFF_JPG,const NuclearRect* src=NULL) = 0;
		virtual bool GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix = NULL, NuclearRect* pRect = NULL, NuclearPixelFormat fmt = XPPIXELFMT_DEFAULT) = 0;//XPPIXELFMT_DEFAULT自动选8888或者4444
		virtual PictureHandle GetBackBuffer() = 0;

		// 具体绘图
		bool DrawLine(float x1, float y1, float x2, float y2, NuclearColor color){return DrawLine(NuclearFRectt(x1, y1, x2, y2), color); }
		virtual bool DrawLine(const NuclearFRectt &line, NuclearColor color) = 0;
		virtual bool DrawLine(const NuclearFRectt *lines, int n, NuclearColor color) = 0;

		virtual bool DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, NuclearColor color) = 0;

		bool DrawBox(float x1, float y1, float x2, float y2, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL){return DrawBox(NuclearFRectt(x1, y1, x2, y2), color, fillmode, pMatrix); }
	
		virtual bool DrawBox(const NuclearFRectt &rect, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL) = 0;
		virtual bool DrawBox(const NuclearFRectt *rects, int n, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL) = 0;

		//fTheta 单位: 角度，向右为0
		virtual bool DrawFan(float x, float y, float r, float fTheta, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL) = 0;
		virtual bool DrawEllipseFan(float x, float y, float rx, float ry, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, float fStartAngle = 0.0f, float fEndAngle = 0.0f, const NuclearMatrix3 *pMatrix = NULL) = 0;//单位：角度
		virtual bool DrawEllipse(float x, float y, float rx, float ry, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, float fAngle = 0.0f) = 0;
		virtual bool DrawCircle(float x, float y, float r, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL){return DrawEllipse(x, y, r, r, color, fillmode);}
		virtual bool DrawPolygon(const std::vector<NuclearPoint> &pts, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL) = 0;

		virtual bool DrawTriangles(const std::vector<NuclearTriangle> &triangles, NuclearColor color, NuclearFillMode fillmode = XPFM_WIREFRAME) = 0;

		virtual bool DrawTriangleList(const NuclearTCVertex* pVertexs, int nVertexNum/*顶点个数*/) = 0;

		//顶点个数是3的整数倍
		virtual bool DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, NuclearColor color = 0xffffffff) = 0;

		virtual bool DrawEllipsePicture(const DrawEllipsePictureParam& param) = 0;

		// 从磁盘加载和绘制图片
		// 从windows本地路径加载图片
		virtual PictureHandle LoadPictureFromNativePath(const std::string &nativepath);
		// 从引擎资源路径加载图片
		virtual PictureHandle LoadPicture(const std::wstring& filename, NuclearPictureInfo* pPicinfo = NULL) = 0;
		virtual bool GetTextData(const char* pChar, const std::string &font, int size, unsigned char*& pData, int& w, int& h) = 0;
        virtual void CachePicture(PictureHandle pic, const std::wstring &filename) = 0;
        virtual bool CacheInMemory() { return true; }
		virtual bool DrawPicture(const DrawPictureParam& param) = 0;
		virtual bool FreePicture(PictureHandle handle){ return true; }
		virtual bool DrawPicture(PictureHandle picHandle, float left, float top, float right, float bottom) = 0;
		virtual bool DrawBatchPicture(const DrawBatchPictureParam &param) = 0;
		virtual bool DrawPicture(PictureHandle handle, const NuclearFRectt &rect, NuclearColor color) //简单接口
		{
			DrawPictureParam param;
			param.handle = handle;
			param.pRect = &rect;
			param.pColor = &color;
			param.colorCount = 1;
			return DrawPicture(param);
		}

		//绘制带索引的顶点buffer
		virtual bool DrawIndexVerticeBuffer(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, const WORD* pIndexs, int nVertexNum, int nIndexNum) = 0;

		// Rendertarget管理相关
		// 所有的RenderTargets在设备重置的时候会丢失，用户需要自己恢复
		// 创建 RenderTarget
		virtual PictureHandle CreateRenderTarget(int iWidth, int iHeight, NuclearTextureFormat texfmt) = 0;
		// 释放 RenderTarget
		virtual bool FreeRenderTarget(PictureHandle hRenderTarget) = 0;
		// 压入 RenderTarget
		virtual bool PushRenderTarget(PictureHandle hRenderTarget) = 0;
		// 弹出 RenderTarget
		virtual bool PopRenderTarget() = 0;
		virtual NuclearDisplayMode GetDisplayMode() const
		{
			NuclearDisplayMode a;
			return a;
		}
		// 效果相关
		virtual bool SelectRenderEffect(XPRENDER_EFFECT xpre) = 0;
		virtual bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam) = 0;
		virtual XPRENDER_EFFECT GetRenderEffect() = 0;

		// 绘制文本
		virtual bool DrawText(LPCTSTR text, int nstr, LPRECT rect, NuclearColor color) = 0;
		virtual bool DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, NuclearColor color) = 0;

		// 绘图框架
		virtual bool IsAutoTestDevice() = 0;
		virtual void SetAutoTestDevice(bool isAuto) = 0;

		/*util for pack tools
		clipmode == 0  不切割
		clipmode == 1  切割到最小，rect 返回切割后的图片在原图的位置
		clipmode == 2  切割后保持原图中心仍为中心
		*/
		virtual bool SaveTextureToDXSurface(const void *srcdata, int srcsize, NuclearBuffer &destBuffer,NuclearTextureFormat texfmt, NuclearImageFileFormat filefmt, NuclearRect &rect, int clipmode, bool setNonzeroAlpha2One) = 0;
		//对于每一帧，如果使用者手动设置了就不在调用, 否则就要调用来生成边界轮廓多边形
		//bfalg 暂时保留..
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, std::vector<NuclearPoint>& outLinePoint, bool bflag = false) = 0;

		//对指定区域NuclearFRectt frct寻找轮廓，frct的坐标是纹理坐标...
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, NuclearFRectt frct, std::vector<NuclearPoint>& outLinePoint, bool bflag = false) = 0;

		virtual std::wstring GetAdapterDescription() = 0;
#ifdef XP_PERFORMANCE
		virtual RenderPerformance& GetPerformance() = 0;
#endif
		// 设备丢失恢复时的回调, 只能在Renderer线程调用，而且要保证renderer销毁前pTask指针一直有效
		virtual void SetRestoreTask(INuclearRunnable *pTask) { }
		// 设备丢失时的回调, 只能在Renderer线程调用，而且要保证renderer销毁前pTask指针一直有效
		virtual void SetLostTask(INuclearRunnable *pTask) { }

		// 可用纹理显存估计值，单位是 M
		virtual int GetAvailableTextureMem() = 0;
		virtual bool IsTextureCapsNoPow2() = 0;

		//测试显卡支持的抗锯齿模式
		//window: 是否窗口模式； isMode16: 是否16位色，结果输出到types里面
		virtual void GetAvailableMultiSampleType(bool window, bool isMode16, std::vector<NuclearMultiSampleType> &types) = 0;
		virtual NuclearMultiSampleType GetCurrentMultiSampleType() const = 0;

		virtual IParticleManager* GetParticleManager() = 0;
		virtual IFontManager *GetFontManager() = 0;			

		bool SaveTextureToFile(PictureHandle handle, const std::wstring &path)
		{
			return SaveTextureToFile(handle, XPIFF_DDS, path);
		}
		virtual bool SaveTextureToFile(PictureHandle handle, NuclearImageFileFormat filefmt, const std::wstring &path) = 0;
		virtual bool SaveTextureToFile(PictureHandle handle, NuclearTextureFormat texfmt, NuclearImageFileFormat filefmt, NuclearBuffer &outbuffer) = 0;

		//大杀器，Debug用，path必须为windows目录，以\结尾
		virtual void DumpAllPicture(const std::wstring &path) = 0;

		// 获取一个纹理大小信息 yanji 20150821 for lua
		virtual NuclearPictureInfo GetPictureSizeInfo(PictureHandle handle) = 0;
		virtual const void* GetTexInfo(PictureHandle handle) = 0;
	};

	class NuclearFileIOManager;

	// 创建/销毁函数
	XPCREATE_RENDERER_RESULT CreateRenderer(Renderer **ppr, const NuclearDisplayMode &dmode, NuclearFileIOManager *pFileIOMan, DWORD flags = 0, XPRENDERER_VERSION rv = XPRV_DEFAULT, NuclearMultiSampleType mstype = XPMULTISAMPLE_NONE);
	void DestroyRenderer(Renderer *r);

};

#endif