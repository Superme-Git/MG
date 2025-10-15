//////////////////////////////////////////////////////////////////////////
//
// SvgShowRenderer.h
//
// author : yanghaibo
// date :	2008-11-06
//
//////////////////////////////////////////////////////////////////////////
#pragma once

#include "ShowRenderer.h"

#include "..\agg-2.5\examples\svg_viewer\svg_render_engine.h"

class CSvgShowRenderer : public IShowRenderer
{
public:
	CSvgShowRenderer(void);
	~CSvgShowRenderer(void);


	//====================================================================
	// ��ʼ��������ؽӿ�
	//====================================================================

	//
	// summary:
	//		����Show���ڴ����ݣ�type
	// 
	// parameter:
	//		[in] pShowData - Show���ڴ�����
	//		[in] size - Show���ڴ����ݴ�С�����ֽڼ�
	//
	// returns:
	//		��ȷ����������true��ʧ�ܣ�����false��
	//
	virtual bool ParseData(const void* pShowData, size_t size);

	//
	// summary:
	//		����ָ����Show�ļ�
	//
	// parameter:
	//		pShowFileName - show ���ļ��������ļ�����׺ȷ��ͼ������
	//
	// returns:
	//		��ȷ����������true��ʧ�ܣ�����false��
	//
	virtual bool ParseFile(const wchar_t* pShowFileName);

	//====================================================================
	// Show������ĳߴ�
	// �óߴ粻�ܷ���任���豸��ϢӰ��
	// ParseData�ɹ�����Ч
	//====================================================================

	//
	// summary: Show�Ŀ��
	//
	virtual double GetShowWidth() const;

	//
	// summary: Show�ĸ߶�
	//
	virtual double GetShowHeight() const;

	//
	// summary: 
	//		������ʾ�豸��Ϣ
	//		��δ���ã���ʹ��Show������Ĵ�Сȷ���豸��С
	//
	// parameter:
	//		[in] width - �豸���
	//		[in] height - �豸�߶�
	//
	virtual void SetDeviceSize( unsigned width, unsigned height );

	//
	// summary: 
	//		��ȡ��ǰ���õ���ʾ�豸���
	//
	virtual unsigned GetDeviceWidth() const;

	//
	// summary: 
	//		��ȡ��ǰ���õ���ʾ�豸�߶�
	//
	virtual unsigned GetDeviceHeight() const;

	//
	// summary:
	//		������ʾ�豸��DPI��Ϣ
	//
	// parameter:
	//		[in] dpi - һ��Ϊ 96 ���� 120. Windows�£��ɸ������º���ȡ��ȱʡֵ
	//		GetDeviceCaps(GetDC(NULL), LOGPIXELSX).
	//
	virtual void SetDPI(unsigned dpi);

	//
	// summary:
	//		��ȡ��ǰ�����豸��DPI
	//
	// returns:
	//		���ص�ǰ���ƻ��������õ�DPI�����ڲ�ʹ�õ�ȱʡDPI
	//
	virtual unsigned GetDPI() const;

	//
	// summary:
	//		����ͼ���32λ����ɫ
	// 
	// remarks:
	//		32λ����ɫ����ģʽ��A8R8G8B8, �Ӹߵ�����ɵ�4�ֽ���ɫֵ��
	//		����A8λ��Ϊ͸������Ϣ�����Ϊ0����ͼ�񱳾���ȫ͸�������Ϊ255������͸����
	//
	virtual void SetBgColor(  unsigned long argb8 );

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
	virtual unsigned long GetBgColor( ) const;
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
	virtual bool SetAdjustTransAffine(const double* matrix);

	//
	// summary:
	//		ȡ��ԭʼʸ��ͼ�ĵ�������任���������
	//
	// parameter:
	//		[out] matrix - 6����λ���ȵ�double����
	//
	virtual void GetAdjustTransAffine(double* matrix) const;

	//
	// summary:
	//		����Show
	//
	virtual void Draw();

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
	virtual void DrawOnBuffer( void* buf, unsigned width, unsigned height, bool bFillWithBgColor, bool bDemultiply );

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
	virtual bool Translate( double dx, double dy, bool abs );

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
	virtual bool Scale( double scale, bool abs );

	//
	// summary:	��ת
	//
	// parameter: 
	//		rad - ��ת�ĽǶȣ��Ի��ȼƣ�
	//		��ֵ˳ʱ����ת����ֵ��ʱ����ת
	//		abs - true, ������ת, false ����ڵ�ǰ״̬��ת
	//
	virtual bool Rotate( double rad, bool abs );

	//
	// summary: ���÷���任��ʹͼ��ص���ʼ״̬
	//
	virtual bool OriginalView();

	//
	// summary: ��ȡ��ǰͼ�����ۻ��ķ���任����
	// 
	// parameter:
	//		[out] matrix - ����任�ľ���������飬6����λ��
	//
	virtual void GetTransAffine( double* matrix ) const;

	//
	// summary: �ڵ�ǰͼ����Ӧ�÷���任
	// 
	// parameter:
	//		[in] matrix - ����任�ľ���������飬6����λ��
	//
	virtual void ApplyTransAffine( const double* matrix );

	//
	// summary: �ڵ�ǰͼ���Ϸ���任һ��ָ����
	// 
	// parameter:
	//		[inout] x - ����任�ĺ�����
	//		[inout] y - ����任��������
	//
	virtual void Transform( double& x, double& y ) const;

	//
	// summary:	Show�ı߽�λ������
	//		��ǰShow�ı߽������ܷ���任����Ӱ�졣
	//		�ر�˵��������Ѿ���ͼ��Ӧ������ת�任��
	//		��ImageBox������תǰ��ImageBox��BoundingBox��
	//		����������ImagePixel��BoundingBox��
	//
	virtual void GetImageBox(
		double* left, double* top, double* right, double* bottom) const;

	//
	// summary:
	//		��ȡShow��ͼ������
	//		bgra32 format
	//
	virtual const void* GetBitmapData() const;

	//
	// summary:
	//		��ȡShow��ͼ�����ݴ�С�����ֽڼ�
	//
	virtual size_t GetBitmapSize() const;

	// 
	// summary:
	//		����ShowRenderer����
	//
	virtual void Destroy();

protected:
	svg::svg_render_engine m_svg_render;
};

//////////////////////////////////////////////////////////////////////////
//
// inline ����ʵ��
//
inline void CSvgShowRenderer::Destroy()
{
	delete this;
}
