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
	// ���ܲ��Բ�����release������ʵ��
	struct RenderPerformance
	{
		int m_iManagedTextureCount;				// d3d����� texture �ĸ���
		int m_iManagedTextureMemory;			// d3d����� texture ��ռ���ڴ�, ��λ�ֽ�

		int m_iTotalPixelRenderedPerFrame;		// ÿ֡�ۻ��������������� ��λ������
		int m_iDrawPrimitiveCountPerFrame;		// ÿ֡ DrawPrimitive �����ĵ��ô���
		int m_iDrawPrimitiveUPCountPerFrame;	// ÿ֡ DrawPrimitiveUP �����ĵ��ô���
		int m_iDrawPictureCountPerFrame;		// ÿ֡ DrawPicture �����ĵ��ô���
		int m_iDrawLineCountPerFrame;			// ÿ֡ DrawLine �����ĵ��ô���
		int m_iDrawTriangleCountPerFrame;		// ÿ֡ DrawTriangle �����ĵ��ô���
		int m_iDrawBoxCountPerFrame;			// ÿ֡ DrawBox �����ĵ��ô���
		int m_iDrawTextCountPerFrame;			// ÿ֡ DrawText �����ĵ��ô���
		int m_iDrawCircleCountPerFrame;			// ÿ֡ DrawCircle �����ĵ��ô���
		int m_iDrawPolygonCountPerFrame;		// ÿ֡ DrawPolygon �����ĵ��ô���

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
		const XPCOLOR *pColor;	//����NULL��ʱ����ɫֵΪ0xffffffff;
		int colorCount;			//ֻ����1����4����pColorΪ�յ�ʱ�����ֵû������
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
		float u0;//0--1����
		float v0;//0--1����
		float ru;//0--1����
		float rv;//0--1����
		XPCOLOR color; //������ɫ
		XPTEXTUREADDRESS TextureMode;
		XPCOLOR borderColor; //XPTEXADDRESS_BORDERѰַģʽ�ı���ɫ

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
		PictureHandle secHandle;	//����׶ε�����Ŀǰֻ�����˷�
		const FRECT *pRect;
		int rectCount;
		const XPMATRIX3 *pMatrix;
		const XPCOLOR *pColor;	//����NULL��ʱ����ɫֵΪ0xffffffff;
		int colorCount;			//ֻ����1����4����pColorΪ�յ�ʱ�����ֵû������
		XPTEXTURE_BLENDMODE blend;
		const FRECT *pSrcrect;
		const FRECT *pSecSrcRect;	//�ڶ��׶�������������꣬Ĭ��ΪNULL�����secHandle�кϷ�ֵ���������ΪNULL����ʾ�͵�һ����׶ε���������һ��
		bool bSameuv; //Ĭ��true,��ʾ���е�pRect����������ͬ��
					  //Ϊfalse���ÿ��pRect��Ӧ��pSrcrect��ȥ����������, pSrcrect����ΪrectCount����
		bool bSecSameuv;	//Ĭ��Ϊtrue����ʾ�ڶ��׶��������е��������궼һ������bSameuv�ĺ�����һ���ģ�
		bool bSpecAlpha;	//�Ƿ�ֿ�����RenderTarget��Alpha��Ĭ��false

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
	
	// renderer ����
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
	*	�ӿ���                           *
	*******************************************/

	// renderer �ӿ�
	class Renderer : public FreqCountable, public IBaseRenderer
	{
	public:
		const static float Z;
		// ��������
		Renderer() { }
		virtual ~Renderer() { }

		virtual VideoTexture* GetDshowMovieTexRender() = 0;
		virtual bool InitDShowTextureRenderer(const std::wstring& strVideoFile) = 0;
		//����Ƶ����ָ�����������bzoomΪtrue�������ź����ȫ�����ݣ���֮��ü���1:1���ơ�
		virtual bool DrawVideoTexture(const FRECT& frct, bool bZoom = true, const XPMATRIX3* pMatrix = NULL, XPCOLOR color=0xffffffff) = 0;
		virtual void ReleaseVideoRenderRes() = 0; //����֮���ͷ�...
		// ��������
		virtual bool Create(HWND hwnd, const XDisplayMode &dmode, DWORD flags) = 0;
		virtual void Destroy() = 0;

		// �ı�Դ��Ŀ�ľ�������
		virtual void SetSourceRect(const CRECT* pSrcRect) { }
		virtual void SetDestRect(const CRECT* pDstRect) { }

		// ��� *buf == NULL, �� *buf �ɺ����ڷ��䣬 ����ֱ��ʹ�� *buf
		virtual bool PrintScreen(void**buf, int*size,XPIMAGE_FILEFORMAT filetype=XPIFF_JPG,const CRECT* src=NULL) = 0;
		virtual bool GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix = NULL, CRECT* pRect = NULL, 
			XPPIXEL_FORMAT fmt = XPPIXELFMT_DEFAULT) = 0;//XPPIXELFMT_DEFAULT�Զ�ѡ8888����4444
		virtual PictureHandle GetBackBuffer() = 0;

		// �����ͼ
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

		//fTheta ��λ: �Ƕȣ�����Ϊ0
		virtual bool DrawFan(float x, float y, float r, float fTheta, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, const XPMATRIX3 *pMatrix = NULL) = 0;
		virtual bool DrawEllipseFan(float x, float y, float rx, float ry, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, float fStartAngle = 0.0f, float fEndAngle = 0.0f, const XPMATRIX3 *pMatrix = NULL) = 0;//��λ���Ƕ�
		virtual bool DrawEllipse(float x, float y, float rx, float ry, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL, float fAngle = 0.0f) = 0;
		virtual bool DrawCircle(float x, float y, float r, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL)
		{
			return DrawEllipse(x, y, r, r, color, fillmode);
		}
		virtual bool DrawPolygon(const std::vector<CPOINT> &pts, XPCOLOR color, XPFILLMODE fillmode = XPFM_SOLIDFILL) = 0;

		virtual bool DrawTriangles(const std::vector<TRIANGLE> &triangles, XPCOLOR color, XPFILLMODE fillmode = XPFM_WIREFRAME) = 0;

		virtual bool DrawTriangleList(const TCVERTEX* pVertexs, int nVertexNum/*�������*/) = 0;
		//���������3��������

		virtual bool DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, XPCOLOR color = 0xffffffff) = 0;

		virtual bool DrawEllipsePicture(const DrawEllipsePictureParam& param) = 0;

		virtual PictureHandle ClonePicture(PictureHandle handle, bool bEqualScale)
		{
			int srcW, srcH, dstW, dstH;
			return ClonePicture(handle, 1.0f, bEqualScale, srcW, srcH, dstW, dstH);
		}
		virtual PictureHandle ClonePicture(PictureHandle handle, double scale, bool bEqualScale, int& srcW, int& srcH, int& dstW, int& dstH) = 0;
		// �Ӵ��̼��غͻ���ͼƬ
		// ��windows����·������ͼƬ
		virtual PictureHandle LoadPictureFromNativePath(const std::wstring &nativepath);
		// ��������Դ·������ͼƬ
		virtual PictureHandle LoadPicture(const std::wstring &filename) = 0;
		virtual bool DrawPicture(const DrawPictureParam& param) = 0;
		virtual bool DrawBatchPicture(const DrawBatchPictureParam &param) = 0;
		virtual bool DrawPicture(PictureHandle handle, const FRECT &rect, XPCOLOR color) //�򵥽ӿ�
		{
			DrawPictureParam param;
			param.handle = handle;
			param.pRect = &rect;
			param.pColor = &color;
			param.colorCount = 1;
			return DrawPicture(param);
		}

		//���ƴ������Ķ���buffer
		virtual bool DrawIndexVerticeBuffer(PictureHandle handle, const TCT1VERTEX* pVertexs, const WORD* pIndexs,
												int nVertexNum, int nIndexNum) = 0;

		// Rendertarget�������
		// ���е�RenderTargets���豸���õ�ʱ��ᶪʧ���û���Ҫ�Լ��ָ�
		// ���� RenderTarget
		virtual PictureHandle CreateRenderTarget(int iWidth, int iHeight, XPTEXTURE_FORMAT texfmt) = 0;
		// �ͷ� RenderTarget
		virtual bool FreeRenderTarget(PictureHandle hRenderTarget) = 0;
		// ѹ�� RenderTarget
		virtual bool PushRenderTarget(PictureHandle hRenderTarget) = 0;
		// ���� RenderTarget
		virtual bool PopRenderTarget() = 0;
		
		// Ч�����
		virtual bool SelectRenderEffect(XPRENDER_EFFECT xpre) = 0;
		virtual bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam) = 0;
		virtual bool SetShaderTexture(XPRENDER_EFFECT xpre, const char* name, PictureHandle pic) = 0;
		virtual XPRENDER_EFFECT GetRenderEffect() = 0;


		// �����ı�
		virtual bool DrawText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color) = 0;
		virtual bool DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color) = 0;

		// ���
		virtual bool ShowCursor(bool f) = 0;
		virtual bool SetCursorPosition(int x, int y) = 0;
		virtual bool SetCursorPicture(PictureHandle handle, int offsetx = 0, int offsety = 0) = 0;
		virtual bool GetCurrentCursor(PictureHandle &handle, int& x, int& y) = 0;

		// ��ͼ���
		virtual bool Begin(XPCOLOR color = 0xc090) = 0;
		virtual bool End();
		virtual bool Clear(XPCOLOR color) = 0;
		virtual XP_DEVICE_STATE TestDevice() = 0;
		virtual bool IsAutoTestDevice() = 0;
		virtual void SetAutoTestDevice(bool isAuto) = 0;
		virtual bool JustBegin() = 0;
		virtual bool JustEnd() = 0;


		// FPSͳ��
		virtual void ResetFPSCounter();
		virtual float GetFPS() const;
		virtual float GetLatestFPS() const;

		// util for pack tools
		/*
		clipmode == 0  ���и�
		clipmode == 1  �и��С��rect �����и���ͼƬ��ԭͼ��λ��
		clipmode == 2  �и�󱣳�ԭͼ������Ϊ����
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

		//����ÿһ֡�����ʹ�����ֶ������˾Ͳ��ڵ���, �����Ҫ���������ɱ߽����������
		//bfalg ��ʱ����..
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, std::vector<CPOINT>& outLinePoint, bool bflag = false) = 0;

		//��ָ������FRECT frctѰ��������frct����������������...
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, FRECT frct, std::vector<CPOINT>& outLinePoint, bool bflag = false) = 0;

		virtual std::wstring GetAdapterDescription() = 0;

		// performance
		virtual RenderPerformance GetPerformance() = 0;

		// �豸��ʧ�ָ�ʱ�Ļص�, ֻ����Renderer�̵߳��ã�����Ҫ��֤renderer����ǰpTaskָ��һֱ��Ч
		virtual void SetRestoreTask(IRunnable *pTask) { }
		// �豸��ʧʱ�Ļص�, ֻ����Renderer�̵߳��ã�����Ҫ��֤renderer����ǰpTaskָ��һֱ��Ч
		virtual void SetLostTask(IRunnable *pTask) { }

		// ���������Դ����ֵ����λ�� M
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

		//Ŀǰֻ֧��8888
		virtual bool ClearAlpha(PictureHandle handle) = 0;
		//frct ��handle������ĳһ�飬�������ꡣ(0.0f, 0.0f, 1.0f, 1.0f)��ʾ��������
	//	virtual bool SaveTextureToXBuffer(const void *srcdata, int srcsize, Nuclear::FRECT frct, XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, XBuffer &outbuffer) = 0;

		//��ɱ����Debug�ã�path����ΪwindowsĿ¼����\��β
		virtual void DumpAllPicture(const std::wstring &path) = 0;
	};

	/*******************************************
	*	С����غ���                           *
	*******************************************/

	class CFileIOManager;
	// ��������
	bool CreateRenderer(Renderer **ppr, HWND hwnd, const XDisplayMode &dmode, CFileIOManager *pFileIOMan, DWORD flags = 0, XPRENDERER_VERSION rv = XPRV_DEFAULT);
	// ���ٺ���
	void DestroyRenderer(Renderer *r);
};

#endif