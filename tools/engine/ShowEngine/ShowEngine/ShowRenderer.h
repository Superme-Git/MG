//////////////////////////////////////////////////////////////////////////
//
// ShowRenderer.h
//
// author : yanghaibo
// date :	2008-11-06 initial version
//			2009-07-06 add SetAdjustTransAffine()/GetAdjustTransAffine()
//
//////////////////////////////////////////////////////////////////////////

#pragma once

#ifdef SHOWENGINE_EXPORTS
#define SHOWENGINE_API __declspec(dllexport)
#else
#define SHOWENGINE_API __declspec(dllimport)
#endif

class SHOWENGINE_API IShowRenderer
{
public:
	//====================================================================
	// ��ʼ��������ؽӿ�
	//====================================================================

	//
	// summary:
	//		����Show���ڴ�����
	// 
	// parameter:
	//		[in] pShowData - Show���ڴ�����
	//		[in] size - Show���ڴ����ݴ�С�����ֽڼ�
	//
	// returns:
	//		��ȷ����������true��ʧ�ܣ�����false��
	//
	virtual bool ParseData(const void* pShowData, size_t size) = 0;

	//
	// summary:
	//		����ָ����Show�ļ�
	//
	// parameter:
	//		pShowFileName - show ���ļ���
	//
	// returns:
	//		��ȷ����������true��ʧ�ܣ�����false��
	//
	virtual bool ParseFile(const wchar_t* pShowFileName) = 0;

	//====================================================================
	// Show������ĳߴ�
	// �óߴ粻�ܷ���任���豸��ϢӰ��
	// ParseData�ɹ�����Ч
	//====================================================================

	//
	// summary: Show�Ŀ��
	//
	virtual double GetShowWidth() const = 0;
	
	//
	// summary: Show�ĸ߶�
	//
	virtual double GetShowHeight() const = 0;

	//
	// summary: 
	//		������ʾ�豸��Ϣ
	//		��δ���ã���ʹ��Show������Ĵ�Сȷ���豸��С
	//
	// parameter:
	//		[in] width - �豸���
	//		[in] height - �豸�߶�
	//
	virtual void SetDeviceSize( unsigned width, unsigned height ) = 0;

	//
	// summary: 
	//		��ȡ��ǰ���õ���ʾ�豸���
	//
	virtual unsigned GetDeviceWidth() const = 0;

	//
	// summary: 
	//		��ȡ��ǰ���õ���ʾ�豸�߶�
	//
	virtual unsigned GetDeviceHeight() const = 0;
	
	//
	// summary:
	//		������ʾ�豸��DPI��Ϣ
	//
	// parameter:
	//		[in] dpi - һ��Ϊ 96 ���� 120. Windows�£��ɸ������º���ȡ��ȱʡֵ
	//		GetDeviceCaps(GetDC(NULL), LOGPIXELSX).
	//
	virtual void SetDPI(unsigned dpi) = 0;

	//
	// summary:
	//		��ȡ��ǰ�����豸��DPI
	//
	// returns:
	//		���ص�ǰ���ƻ��������õ�DPI�����ڲ�ʹ�õ�ȱʡDPI
	//
	virtual unsigned GetDPI() const = 0;

	//
	// summary:
	//		����ͼ���32λ����ɫ
	// 
	// remarks:
	//		32λ����ɫ����ģʽ��A8R8G8B8, �Ӹߵ�����ɵ�4�ֽ���ɫֵ��
	//		����A8λ��Ϊ͸������Ϣ�����Ϊ0����ͼ�񱳾���ȫ͸�������Ϊ255������͸����
	//
	virtual void SetBgColor(  unsigned long argb8 ) = 0;

	//
	// summary:
	//		��ȡ��ǰȱʡ�ı���ɫ�����û����õı���ɫ��
	// 
	// remarks:
	//		����û�δ������SetBgColor()���ñ���ɫ����ôȱʡΪ0��
	//
	// returns:
	//		32λ����ɫֵ
	//
	virtual unsigned long GetBgColor( ) const = 0;

	//
	// summary:
	//		����ԭʼʸ��ͼ�ĵ�������任���������
	//		�þ���������ڵ���ԭʼʸ��ͼ����ˣ�����Ӧ���ڱ༭����任֮ǰ��
	//		�༭����任ָ����Translate()/Scale()/Rotate()/ApplyTransAffine().
	//		����ͬһ��ʸ��ͼ���������任�������Ӧ��ֻ������һ�Ρ�
	//
	// parameter:
	//		[in] matrix - 6����λ���ȵ�double����
	//
	// returns:
	//		���óɹ�����true,��Ч��������false.
	//
	virtual bool SetAdjustTransAffine(const double* matrix) = 0;

	//
	// summary:
	//		ȡ��ԭʼʸ��ͼ�ĵ�������任���������
	//
	// parameter:
	//		[out] matrix - 6����λ���ȵ�double����
	//
	virtual void GetAdjustTransAffine(double* matrix) const = 0;

	//
	// summary:
	//		����Show
	//
	virtual void Draw() = 0;

	//
	// summary:
	//		��ָ�����ڴ��л���Show
	//
	// parameter:
	//		[in] buf - ��������A8R8G8B8��ʽ�������ݵ��ڴ��׵�ַָ��
	//		[in] width - �ڴ�ͼ�����ؿ��
	//		[in] height - �ڴ�ͼ�����ظ߶�
	//		[in] bFillWithBgColor - �ڻ���ͼ��ǰ�Ƿ�����ʹ�ñ���ɫ�������
	//		[in] bDemultiply - �Ƿ���ڴ����ؽ���alpha�ָ�����
	// 
	// remarks:
	//		���ڶ����Ⱦ������˵����һ����Ҫָ��bFillWithBgColor��ʹ�ñ���ɫ���ͼ��
	//		��δָ�������÷������ȶ�buf��0��
	//		�������ͼ���뱳��ͼ����alpha���ӣ���buf�е�ͼ�񾭶�ε���֮��
	//		���һ�ε�DrawOnBuffer����һ����Ҫ����Demultiply���㣬�Ա��뱳����������.
	//
	//		���⣬������Ⱦģʽ�����ⲿ�趨��DeviceSize��Ӱ��.
	//		�����ִ��OnDraw(), GetBitmapData()���᷵�ؾ�DrawOnBuffer()��Ⱦ��ͼ��
	//		GetBitmapSize()��һ�����ھ�DrawOnBuffer()��Ⱦ��ͼ���buffer��С������width��height��DeviceSize��ȫ��ͬ.
	//
	virtual void DrawOnBuffer( void* buf, unsigned width, unsigned height, bool bFillWithBgColor, bool bDemultiply ) = 0;

	//====================================================================
	// ����任������ؽӿ�
	// ÿ��ִ�з���任֮����Ҫ����Drawִ���ػ����
	//====================================================================

	//
	// summary:	ƽ��
	//
	// parameter:
	//		dx - x ����ƽ��
	//		dy - y ����ƽ��
	//		abs - true, ����ƽ��, false ����ڵ�ǰ״̬ƽ��
	//
	virtual bool Translate( double dx, double dy, bool abs ) = 0;
	
	//
	// summary:	����
	//
	// parameter: 
	//		scale - �Ŵ���
	//		abs - true, ���ԷŴ�, false ����ڵ�ǰ״̬�Ŵ�
	//
	//		1.0���ϵ����ݣ�ͼ�񽫱��Ŵ�
	//		0~1.0�����ڵ����ݣ�ͼ�񽫱���С
	//		0(��0)����������Ч����������false.
	//
	virtual bool Scale( double scale, bool abs ) = 0;

	//
	// summary:	��ת
	//
	// parameter: 
	//		rad - ��ת�ĽǶȣ��Ի��ȼƣ�
	//		��ֵ˳ʱ����ת����ֵ��ʱ����ת
	//		abs - true, ������ת, false ����ڵ�ǰ״̬��ת
	// 
	virtual bool Rotate( double rad, bool abs ) = 0;


	//
	// summary: ���÷���任��ʹͼ��ص���ʼ״̬
	//
	virtual bool OriginalView() = 0;

	//
	// summary: ��ȡ��ǰͼ�����ۻ��ķ���任����
	// 
	// parameter:
	//		[out] matrix - ����任�ľ���������飬6����λ��
	//
	virtual void GetTransAffine( double* matrix ) const = 0;

	//
	// summary: �ڵ�ǰͼ����Ӧ�÷���任
	// 
	// parameter:
	//		[in] matrix - ����任�ľ���������飬6����λ��
	//
	virtual void ApplyTransAffine( const double* matrix ) = 0;

	//
	// summary: �ڵ�ǰͼ���Ϸ���任һ��ָ����
	// 
	// parameter:
	//		[inout] x - ����任�ĺ�����
	//		[inout] y - ����任��������
	//
	virtual void Transform( double& x, double& y ) const = 0;

	//
	// summary:	Show�ı߽�λ������
	//		��ǰShow�ı߽������ܷ���任����Ӱ�졣
	//		�ر�˵��������Ѿ���ͼ��Ӧ������ת�任��
	//		��ImageBox������תǰ��ImageBox��BoundingBox��
	//		����������ImagePixel��BoundingBox��
	//
	virtual void GetImageBox(
		double* left, double* top, double* right, double* bottom) const = 0;  
	
	//
	// summary:
	//		��ȡShow��ͼ������
	//		bgra32 format
	//
	virtual const void* GetBitmapData() const = 0;

	//
	// summary:
	//		��ȡShow��ͼ�����ݴ�С�����ֽڼ�
	//
	virtual size_t GetBitmapSize() const = 0;

	// 
	// summary:
	//		����ShowRenderer����
	//
	virtual void Destroy() = 0;
};

//
// summary: ShowRenderer�����Ͳ���
//
#define	ST_SVG  0		// ��ǰΨһ��֧�ֵ�Show������
//#define	ST_SVGZ	1	// ѹ����SVGͼ�����ݣ���δ֧��
//#define	ST_FLASH  1	// FLASHͼ�����ݣ���δ֧��
#define	ST_DEFAULT  0	// ȱʡ������Ϣ

//
// summary:
//		����typeָ�������ʹ���ShowRenderer����
//
// parameter:
//		ShowRenderer������
//
// returns:
//		ShowRenderer����ӿڣ�����NULL��ʾ��֧�ָ����ͻ����ڴ治�㵼�´�������ʧ��
//
SHOWENGINE_API IShowRenderer* CreateShowRenderer( int type = ST_DEFAULT );
