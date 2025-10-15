//////////////////////////////////////////////////////////////////////////
//
// SvgShowRenderer.cpp
//
// author : yanghaibo
// date :	2008-11-06
//
//////////////////////////////////////////////////////////////////////////
#include "StdAfx.h"
#include "SvgShowRenderer.h"
#include <assert.h>

CSvgShowRenderer::CSvgShowRenderer(void)
{
}

CSvgShowRenderer::~CSvgShowRenderer(void)
{
}

//====================================================================
// ��ʼ��������ؽӿ�
//====================================================================

bool CSvgShowRenderer::ParseData(const void* pShowData, size_t size)
{
	if( pShowData == NULL || size == 0 )
	{
		assert( pShowData != NULL );
		assert( size != 0);
		return false;
	}

	try
	{
		m_svg_render.parse_svg( pShowData, size);
		return true;
	}
	catch (agg::svg::exception e)
	{
		return false;
	}
}

bool CSvgShowRenderer::ParseFile(const wchar_t* pShowFileName)
{
	if( pShowFileName == NULL )
	{
		assert( pShowFileName != NULL );
		return false;
	}

	try
	{
		m_svg_render.parse_svg(pShowFileName);
		return true;
	}
	catch (agg::svg::exception e)
	{
		return false;
	}
}

//====================================================================
// Show������ĳߴ�
// �óߴ粻�ܷ���任���豸��ϢӰ��
// ParseData�ɹ�����Ч
//====================================================================

double CSvgShowRenderer::GetShowWidth() const
{
	return m_svg_render.get_original_image_width();
}

double CSvgShowRenderer::GetShowHeight() const
{
	return m_svg_render.get_original_image_height();
}

void CSvgShowRenderer::SetDeviceSize( unsigned width, unsigned height )
{
	m_svg_render.set_device_dimension( width, height );
}

unsigned CSvgShowRenderer::GetDeviceWidth() const
{	
	return m_svg_render.rbuf_window().width();
}

unsigned CSvgShowRenderer::GetDeviceHeight() const
{
	return m_svg_render.rbuf_window().height();
}

void CSvgShowRenderer::SetDPI(unsigned dpi)
{
	m_svg_render.set_dpi( dpi );
}

unsigned CSvgShowRenderer::GetDPI() const
{
	return m_svg_render.get_dpi();
}

void CSvgShowRenderer::SetBgColor(  unsigned long argb8 )
{
	agg::rgba8 c = agg::argb8_packed( argb8 );
	m_svg_render.set_bg_color( c );
}

unsigned long CSvgShowRenderer::GetBgColor( ) const
{
	agg::rgba8 c =  m_svg_render.get_bg_color();
	unsigned long r = (c.r << 16) | (c.g << 8) |  (c.b)  | (c.a << 24);
	return r;
}

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
bool CSvgShowRenderer::SetAdjustTransAffine(const double* matrix)
{
	try
	{
		m_svg_render.set_adjust_trans_affine(matrix);
		return true;
	}
	catch (agg::svg::exception e)
	{
		return false;
	}
}

//
// summary:
//		ȡ��ԭʼʸ��ͼ�ĵ�������任���������
//
// parameter:
//		[out] matrix - 6����λ���ȵ�double����
//
void CSvgShowRenderer::GetAdjustTransAffine(double* matrix) const
{
	m_svg_render.get_adjust_trans_affine(matrix);
}

void CSvgShowRenderer::Draw()
{
	m_svg_render.on_draw();
}

void CSvgShowRenderer::DrawOnBuffer(
	void* buf, unsigned width, unsigned height, bool bFillWithBgColor, bool bDemultiply  )
{
	agg::rendering_buffer rbuf ( (agg::int8u*)buf, width, height, width*4 );
	m_svg_render.draw_on_buffer( rbuf, bFillWithBgColor, bDemultiply );
}

//====================================================================
// ����任������ؽӿ�
// ÿ��ִ�з���任֮����Ҫ����Drawִ���ػ����
//====================================================================

bool CSvgShowRenderer::Translate( double dx, double dy, bool abs )
{
	return m_svg_render.translate( dx, dy, abs );
}

bool CSvgShowRenderer::Scale( double scale, bool abs )
{
	assert( scale > 0 );
	return m_svg_render.scale(scale, abs);
}

bool CSvgShowRenderer::Rotate( double rad, bool abs )
{
	return m_svg_render.rotate(rad, abs);
}

bool CSvgShowRenderer::OriginalView()
{
	return m_svg_render.original_view();
}

void CSvgShowRenderer::GetTransAffine( double* matrix ) const
{
	if ( matrix == NULL )
	{
		assert( matrix != NULL );
		return ;
	}

	m_svg_render.get_trans_affine( matrix );
}

void CSvgShowRenderer::ApplyTransAffine( const double* matrix )
{
	if ( matrix == NULL )
	{
		assert( matrix != NULL );
		return ;
	}

	m_svg_render.apply_trans_affine( matrix );
}

void CSvgShowRenderer::Transform( double& x, double& y ) const
{
	m_svg_render.transform( x, y );
}

//====================================================================
// ��ǰShow����Ϣ
// ��ǰShow����Ϣ�ܷ���任����Ӱ��
//====================================================================
void CSvgShowRenderer::GetImageBox(
	double* left, double* top, double* right, double* bottom) const
{
	if( !( left != NULL && top != NULL && right != NULL && bottom != NULL ) )
	{
		assert( left != NULL && top != NULL && right != NULL && bottom != NULL );
		return;
	}

	m_svg_render.get_image_box(left, top, right, bottom);
}

const void* CSvgShowRenderer::GetBitmapData() const
{
	const agg::rendering_buffer& rbuf = m_svg_render.rbuf_window();
	return rbuf.buf();
}

size_t CSvgShowRenderer::GetBitmapSize() const
{
	const agg::rendering_buffer& rbuf = m_svg_render.rbuf_window();
	return rbuf.stride_abs() * rbuf.height();
}
