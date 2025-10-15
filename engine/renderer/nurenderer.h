#ifndef __Nuclear_RENDERER_H__
#define __Nuclear_RENDERER_H__

#include "../common/nuxptypes.h"
#include "nuibaserenderer.h"

namespace Nuclear
{	
	// ���ܲ��Բ���
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
		int m_iTotalLoadTimePerFrame;			// ÿ֡���߳��ܹ����ڼ��ص�ʱ�䣨��λ�����룩
		int m_iLoadXmlTimePerFrame;				// ÿ֡���߳����ڼ���Xml�ļ���ʱ�䣨��λ�����룩
		int m_iLoadTextureTimePerFrame;			// ÿ֡���߳����ڼ��������ļ���ʱ�䣨��λ�����룩
		int m_iUpdateTimePerSecond;				// ÿ�����߳�����MainLoop��ʱ�䣨��λ�����룩
		int m_iRenderTimePerSecond;				// ÿ�����߳�����Render��ʱ�䣨��λ�����룩
		int m_iGameTickTimePerSecond;			// ÿ�����߳�����OnTick��ʱ�䣨��λ�����룩
		int m_iLuaTickTimePerSecond;			// ÿ�����߳�����LuaTick��ʱ�䣨��λ�����룩

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
		const NuclearColor*      pColor;	//����NULL��ʱ����ɫֵΪ0xffffffff;
		int                 colorCount;			//ֻ����1����4����pColorΪ�յ�ʱ�����ֵû������
		NuclearTextureBlendMode blend;
		const NuclearFRectt*        pSrcrect;
		bool                bSpecAlpha;
        
        bool                bUseBW;
        float               fGreyPercent;
        float               fRedPercent;

		int					iShaderType;	// ʹ�õ�Shader����,Ĭ��Ϊ0, 0:������Ⱦ,1:HSV
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
		float u0;//0--1����
		float v0;//0--1����
		float ru;//0--1����
		float rv;//0--1����
		NuclearColor color; //������ɫ
		NuclearTextureAddRess TextureMode;
		NuclearColor borderColor; //XPTEXADDRESS_BORDERѰַģʽ�ı���ɫ

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
		const NuclearFRectt *pRect;
		int rectCount;
		const NuclearMatrix3 *pMatrix;
		const NuclearColor *pColor;	//����NULL��ʱ����ɫֵΪ0xffffffff;
		int colorCount;			//ֻ����1����4����pColorΪ�յ�ʱ�����ֵû������
		NuclearTextureBlendMode blend;
		const NuclearFRectt *pSrcrect;
		const NuclearFRectt *pSecSrcRect;	//�ڶ��׶�������������꣬Ĭ��ΪNULL�����secHandle�кϷ�ֵ���������ΪNULL����ʾ�͵�һ����׶ε���������һ��
		bool bSameuv; //Ĭ��true,��ʾ���е�pRect����������ͬ,Ϊfalse���ÿ��pRect��Ӧ��pSrcrect��ȥ����������, pSrcrect����ΪrectCount����
		bool bSecSameuv;	//Ĭ��Ϊtrue����ʾ�ڶ��׶��������е��������궼һ������bSameuv�ĺ�����һ���ģ�
		bool bSpecAlpha;	//�Ƿ�ֿ�����RenderTarget��Alpha��Ĭ��false

		DrawBatchPictureParam() : handle(INVALID_PICTURE_HANDLE), secHandle(INVALID_PICTURE_HANDLE), pRect(NULL), rectCount(0), pMatrix(NULL), pColor(NULL), colorCount(0), blend(XPTEXBM_DEFAULT),pSrcrect(NULL), pSecSrcRect(NULL), bSameuv(true), bSecSameuv(true), bSpecAlpha(false)
		{
		}
		DrawBatchPictureParam(const DrawPictureParam &dpp) : handle(dpp.handle), secHandle(INVALID_PICTURE_HANDLE), pRect(dpp.pRect), rectCount(1), pMatrix(dpp.pMatrix), pColor(dpp.pColor), colorCount(dpp.colorCount), blend(dpp.blend), pSrcrect(dpp.pSrcrect), pSecSrcRect(NULL), bSameuv(true), bSecSameuv(true), bSpecAlpha(dpp.bSpecAlpha)
		{
		}
	};
	
	// renderer ����
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
	*	�ӿ���                           *
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

		//���ü��ÿ�
		virtual void SetScissorRect(const NuclearRect* pRect) = 0;
		// ��� *buf == NULL, �� *buf �ɺ����ڷ��䣬 ����ֱ��ʹ�� *buf
		virtual bool PrintScreen(void**buf, int*size,NuclearImageFileFormat filetype=XPIFF_JPG,const NuclearRect* src=NULL) = 0;
		virtual bool GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix = NULL, NuclearRect* pRect = NULL, NuclearPixelFormat fmt = XPPIXELFMT_DEFAULT) = 0;//XPPIXELFMT_DEFAULT�Զ�ѡ8888����4444
		virtual PictureHandle GetBackBuffer() = 0;

		// �����ͼ
		bool DrawLine(float x1, float y1, float x2, float y2, NuclearColor color){return DrawLine(NuclearFRectt(x1, y1, x2, y2), color); }
		virtual bool DrawLine(const NuclearFRectt &line, NuclearColor color) = 0;
		virtual bool DrawLine(const NuclearFRectt *lines, int n, NuclearColor color) = 0;

		virtual bool DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, NuclearColor color) = 0;

		bool DrawBox(float x1, float y1, float x2, float y2, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL){return DrawBox(NuclearFRectt(x1, y1, x2, y2), color, fillmode, pMatrix); }
	
		virtual bool DrawBox(const NuclearFRectt &rect, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL) = 0;
		virtual bool DrawBox(const NuclearFRectt *rects, int n, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL) = 0;

		//fTheta ��λ: �Ƕȣ�����Ϊ0
		virtual bool DrawFan(float x, float y, float r, float fTheta, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, const NuclearMatrix3 *pMatrix = NULL) = 0;
		virtual bool DrawEllipseFan(float x, float y, float rx, float ry, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, float fStartAngle = 0.0f, float fEndAngle = 0.0f, const NuclearMatrix3 *pMatrix = NULL) = 0;//��λ���Ƕ�
		virtual bool DrawEllipse(float x, float y, float rx, float ry, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL, float fAngle = 0.0f) = 0;
		virtual bool DrawCircle(float x, float y, float r, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL){return DrawEllipse(x, y, r, r, color, fillmode);}
		virtual bool DrawPolygon(const std::vector<NuclearPoint> &pts, NuclearColor color, NuclearFillMode fillmode = XPFM_SOLIDFILL) = 0;

		virtual bool DrawTriangles(const std::vector<NuclearTriangle> &triangles, NuclearColor color, NuclearFillMode fillmode = XPFM_WIREFRAME) = 0;

		virtual bool DrawTriangleList(const NuclearTCVertex* pVertexs, int nVertexNum/*�������*/) = 0;

		//���������3��������
		virtual bool DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, NuclearColor color = 0xffffffff) = 0;

		virtual bool DrawEllipsePicture(const DrawEllipsePictureParam& param) = 0;

		// �Ӵ��̼��غͻ���ͼƬ
		// ��windows����·������ͼƬ
		virtual PictureHandle LoadPictureFromNativePath(const std::string &nativepath);
		// ��������Դ·������ͼƬ
		virtual PictureHandle LoadPicture(const std::wstring& filename, NuclearPictureInfo* pPicinfo = NULL) = 0;
		virtual bool GetTextData(const char* pChar, const std::string &font, int size, unsigned char*& pData, int& w, int& h) = 0;
        virtual void CachePicture(PictureHandle pic, const std::wstring &filename) = 0;
        virtual bool CacheInMemory() { return true; }
		virtual bool DrawPicture(const DrawPictureParam& param) = 0;
		virtual bool FreePicture(PictureHandle handle){ return true; }
		virtual bool DrawPicture(PictureHandle picHandle, float left, float top, float right, float bottom) = 0;
		virtual bool DrawBatchPicture(const DrawBatchPictureParam &param) = 0;
		virtual bool DrawPicture(PictureHandle handle, const NuclearFRectt &rect, NuclearColor color) //�򵥽ӿ�
		{
			DrawPictureParam param;
			param.handle = handle;
			param.pRect = &rect;
			param.pColor = &color;
			param.colorCount = 1;
			return DrawPicture(param);
		}

		//���ƴ������Ķ���buffer
		virtual bool DrawIndexVerticeBuffer(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, const WORD* pIndexs, int nVertexNum, int nIndexNum) = 0;

		// Rendertarget�������
		// ���е�RenderTargets���豸���õ�ʱ��ᶪʧ���û���Ҫ�Լ��ָ�
		// ���� RenderTarget
		virtual PictureHandle CreateRenderTarget(int iWidth, int iHeight, NuclearTextureFormat texfmt) = 0;
		// �ͷ� RenderTarget
		virtual bool FreeRenderTarget(PictureHandle hRenderTarget) = 0;
		// ѹ�� RenderTarget
		virtual bool PushRenderTarget(PictureHandle hRenderTarget) = 0;
		// ���� RenderTarget
		virtual bool PopRenderTarget() = 0;
		virtual NuclearDisplayMode GetDisplayMode() const
		{
			NuclearDisplayMode a;
			return a;
		}
		// Ч�����
		virtual bool SelectRenderEffect(XPRENDER_EFFECT xpre) = 0;
		virtual bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam) = 0;
		virtual XPRENDER_EFFECT GetRenderEffect() = 0;

		// �����ı�
		virtual bool DrawText(LPCTSTR text, int nstr, LPRECT rect, NuclearColor color) = 0;
		virtual bool DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, NuclearColor color) = 0;

		// ��ͼ���
		virtual bool IsAutoTestDevice() = 0;
		virtual void SetAutoTestDevice(bool isAuto) = 0;

		/*util for pack tools
		clipmode == 0  ���и�
		clipmode == 1  �и��С��rect �����и���ͼƬ��ԭͼ��λ��
		clipmode == 2  �и�󱣳�ԭͼ������Ϊ����
		*/
		virtual bool SaveTextureToDXSurface(const void *srcdata, int srcsize, NuclearBuffer &destBuffer,NuclearTextureFormat texfmt, NuclearImageFileFormat filefmt, NuclearRect &rect, int clipmode, bool setNonzeroAlpha2One) = 0;
		//����ÿһ֡�����ʹ�����ֶ������˾Ͳ��ڵ���, �����Ҫ���������ɱ߽����������
		//bfalg ��ʱ����..
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, std::vector<NuclearPoint>& outLinePoint, bool bflag = false) = 0;

		//��ָ������NuclearFRectt frctѰ��������frct����������������...
		virtual bool GetPicOutLine(const void *srcdata, int srcsize, NuclearFRectt frct, std::vector<NuclearPoint>& outLinePoint, bool bflag = false) = 0;

		virtual std::wstring GetAdapterDescription() = 0;
#ifdef XP_PERFORMANCE
		virtual RenderPerformance& GetPerformance() = 0;
#endif
		// �豸��ʧ�ָ�ʱ�Ļص�, ֻ����Renderer�̵߳��ã�����Ҫ��֤renderer����ǰpTaskָ��һֱ��Ч
		virtual void SetRestoreTask(INuclearRunnable *pTask) { }
		// �豸��ʧʱ�Ļص�, ֻ����Renderer�̵߳��ã�����Ҫ��֤renderer����ǰpTaskָ��һֱ��Ч
		virtual void SetLostTask(INuclearRunnable *pTask) { }

		// ���������Դ����ֵ����λ�� M
		virtual int GetAvailableTextureMem() = 0;
		virtual bool IsTextureCapsNoPow2() = 0;

		//�����Կ�֧�ֵĿ����ģʽ
		//window: �Ƿ񴰿�ģʽ�� isMode16: �Ƿ�16λɫ����������types����
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

		//��ɱ����Debug�ã�path����ΪwindowsĿ¼����\��β
		virtual void DumpAllPicture(const std::wstring &path) = 0;

		// ��ȡһ�������С��Ϣ yanji 20150821 for lua
		virtual NuclearPictureInfo GetPictureSizeInfo(PictureHandle handle) = 0;
		virtual const void* GetTexInfo(PictureHandle handle) = 0;
	};

	class NuclearFileIOManager;

	// ����/���ٺ���
	XPCREATE_RENDERER_RESULT CreateRenderer(Renderer **ppr, const NuclearDisplayMode &dmode, NuclearFileIOManager *pFileIOMan, DWORD flags = 0, XPRENDERER_VERSION rv = XPRV_DEFAULT, NuclearMultiSampleType mstype = XPMULTISAMPLE_NONE);
	void DestroyRenderer(Renderer *r);

};

#endif