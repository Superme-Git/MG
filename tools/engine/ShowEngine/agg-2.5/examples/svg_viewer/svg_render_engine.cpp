#include "svg_render_engine.h"
#include <stdio.h>
#include <stdlib.h>
#include "agg_basics.h"
#include "agg_rendering_buffer.h"
#include "agg_rasterizer_scanline_aa.h"
#include "agg_scanline_p.h"
#include "agg_renderer_scanline.h"
#include "agg_pixfmt_rgba.h"
//#include "ctrl/agg_slider_ctrl.h"
#include "agg_svg_parser.h"
#include "agg_span_interpolator_linear.h"
#include "agg_span_gradient.h"
#include "agg_span_allocator.h"
#include "svg_basic.h"

typedef pixfmt_type::color_type color_type;
typedef color_type::value_type color_value_type;


//enum { flip_y = false };

namespace svg
{

	unsigned calc_bpp_for_format(agg::pix_format_e format)
	{
		unsigned bpp = 0;
		using namespace agg;
		switch(format)
		{
		case pix_format_bw:
			bpp = 1;
			break;

		case pix_format_gray8:
			bpp = 8;
			break;

		case pix_format_gray16:
			bpp = 16;
			break;

		case pix_format_rgb565:
		case pix_format_rgb555:
			bpp = 16;
			break;

		case pix_format_rgbAAA:
		case pix_format_bgrAAA:
		case pix_format_rgbBBA:
		case pix_format_bgrABB:
			bpp = 32;
			break;

		case pix_format_rgb24:
		case pix_format_bgr24:
			bpp = 24;
			break;

		case pix_format_rgb48:
		case pix_format_bgr48:
			bpp = 48;
			break;

		case pix_format_bgra32:
		case pix_format_abgr32:
		case pix_format_argb32:
		case pix_format_rgba32:
			bpp = 32;
			break;

		case pix_format_bgra64:
		case pix_format_abgr64:
		case pix_format_argb64:
		case pix_format_rgba64:
			bpp = 64;
			break;
		}

		return bpp;
	}

	svg_render_engine::~svg_render_engine()
	{
		destroy();
	}

	svg_render_engine::svg_render_engine() :
		m_format(agg::pix_format_bgra32),
		m_flip_y(true),
		m_path(),
		//m_expand(5,     5,    256-5, 11,    flip_y),
		//m_gamma (5,     5+15, 256-5, 11+15, flip_y),
		//m_scale (256+5, 5,    512-5, 11,    flip_y),
		//m_rotate(256+5, 5+15, 512-5, 11+15, flip_y),
		m_min_x(0.0),
		m_min_y(0.0),
		m_max_x(0.0),
		m_max_y(0.0),
		m_cx(0.0),
		m_cy(0.0),
		m_bcx(0.0),
		m_bcy(0.0),
		m_dpi(96),
		m_bg_color(0,0,0,0),
		m_need_redraw_flag(false),
		m_buf(NULL)
	{
		m_device_width = 0;
		m_device_height = 0;
		m_image_width = 0;
		m_image_height = 0;

		m_bpp = calc_bpp_for_format(m_format);

		//add_ctrl(m_expand);
		//add_ctrl(m_gamma);
		//add_ctrl(m_scale);
		//add_ctrl(m_rotate);

		////m_expand.label("Expand=%3.2f");
		//m_expand.range(-1, 1.2);
		//m_expand.value(0.0);

		////m_gamma.label("Gamma=%3.2f");
		//m_gamma.range(0.0, 3.0);
		//m_gamma.value(1.0);

		////m_scale.label("Scale=%3.2f");
		////m_scale.num_steps(10);
		////m_scale.range(0.2, 10.0);
		//m_scale.range(0.001, 10.0); // pow2() will be 0.01 to 100.0
		//m_scale.value(1.0);

		////m_rotate.label("Rotate=%3.2f");
		//m_rotate.range(-180.0, 180.0);
		//m_rotate.value(0.0);

		m_active_path_id = -1;
		
		m_mtx.reset();
		m_mtx_zoom.reset();
		m_mtx_move.reset();
		m_mtx_rotate.reset();
	}

	svg_render_engine_ex::svg_render_engine_ex() :
		m_x(0.0),
		m_y(0.0),
		m_dx(0.0),
		m_dy(0.0),
		m_hx(0),
		m_hy(0),
		m_drag_flag(false),
		m_hit_flag(false)
	{
	}

	bool svg_render_engine::zoom_in()
	{
		if ( m_active_path_id != -1 )
		{
			agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox; 
			m_path.zoom_in_path( mtx, m_active_path_id );
		}
		else
		{
			double cur_value = m_scale;
			if ( cur_value > 256.0 )
				return false;
			
			cur_value *= 2.0;
			m_scale = cur_value;

			zoom();
		}

		return true;
	}

	bool svg_render_engine::zoom_in(double cx, double cy)
	{
		if ( m_active_path_id != -1 )
		{
			agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox; 
			m_path.zoom_in_path( mtx, m_active_path_id );
		}
		else
		{

			double cur_value = m_scale;
			if ( cur_value > 256.0 )
				return false;

			cur_value *= 2.0;
			m_scale = cur_value;

			zoom( cx, cy, m_scale, true );
		}

		return true;
	}

	bool svg_render_engine::zoom_out()
	{
		if ( m_active_path_id != -1 )
		{
			agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox; 
			m_path.zoom_out_path( mtx, m_active_path_id );
		}
		else
		{
			double cur_value = m_scale;
			if ( cur_value < 0.001 )
			{
				return false;
			}

			cur_value /= 2.0;
			m_scale = cur_value;
		
			zoom();
		}
		
		return true;
	}

	bool svg_render_engine::zoom_out(double cx, double cy)
	{
		if ( m_active_path_id != -1 )
		{
			agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox; 
			m_path.zoom_out_path( mtx, m_active_path_id );
		}
		else
		{			
			double cur_value = m_scale;
			if ( cur_value < 0.001 )
			{
				return false;
			}

			cur_value /= 2.0;
			m_scale = cur_value;

			zoom( cx, cy, m_scale, true );
		}

		return true;
	}

	bool svg_render_engine::rotate(double cx, double cy, double deg, bool abs)
	{
		return rotate_by_rad( cx, cy, agg::deg2rad(deg), abs );
	}

	bool svg_render_engine::rotate_by_rad(double cx, double cy, double rad, bool abs)
	{
		// 为了提高效率，这里使用乘运算
		// m_mtx *= agg::trans_affine_translation( -cx, -cy );
		// 等价于:
		// m_mtx /= agg::trans_affine_translation( cx, cy );
		m_mtx *= agg::trans_affine_translation( -cx, -cy );
		m_mtx /= m_mtx_rotate;
		if( abs )
		{
			m_mtx_rotate.reset();
		}
		m_mtx_rotate.rotate( rad );
		m_mtx *= m_mtx_rotate;
		m_mtx *= agg::trans_affine_translation( cx, cy );

		return true;
	}

	bool svg_render_engine::rotate_cw()
	{
		if ( m_active_path_id != -1 )
		{
			agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox; 
			return m_path.rotate_cw_path( mtx, m_active_path_id );
		}
		else
		{
			return rotate(m_cx, m_cy, 5, false);
		}
	}

	bool svg_render_engine::rotate_ccw()
	{
		if ( m_active_path_id != -1 )
		{
			agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox; 
			return m_path.rotate_ccw_path( mtx, m_active_path_id );
		}
		else
		{
			return rotate(m_cx, m_cy, -5, false);
		}
	}

	bool svg_render_engine::rotate_cw_object()
	{
		double bcx = m_bcx;
		double bcy = m_bcy;

		// get the new center point.
		m_mtx.transform(&bcx, &bcy);

		return rotate( bcx, bcy, 5, false );
	}

	bool svg_render_engine::rotate_ccw_object()
	{
		double bcx = m_bcx;
		double bcy = m_bcy;

		// get the new center point.
		m_mtx.transform(&bcx, &bcy);
		
		return rotate( bcx, bcy, -5, false );
	}

	void svg_render_engine::reset()
	{
		m_min_x = (0.0);
		m_min_y = (0.0);
		m_max_x = (0.0);
		m_max_y = (0.0);
		m_bcx  = (0.0);
		m_bcy  = (0.0);

		m_cx = (0.0);
		m_cy = (0.0);

		m_image_width = 0;
		m_image_height = 0;

		m_active_path_id = -1;
		
		m_mtx_adjust.reset();
		m_mtx.reset();
		m_mtx_zoom.reset();
		m_mtx_move.reset();
		m_mtx_rotate.reset();

		//m_expand.range(-1, 1.2);
		//m_expand.value(0.0);

		//m_gamma.range(0.0, 3.0);
		//m_gamma.value(1.0);

		//m_scale.range(0.001, 10.0); // pow2() will be 0.01 to 100.0
		//m_scale.value(1.0);

		//m_rotate.range(-180.0, 180.0);
		//m_rotate.value(0.0);

		// do not reset device width and device height
	}

	void svg_render_engine::set_device_dimension(unsigned width ,unsigned height)
	{
		// 如果是parse之后重新设置device dimension, 此时需要重新init
		if( width > 0 && width != m_device_width || height > 0 && height != m_device_height )
		{
			m_device_width = width;
			m_device_height = height;
			init( m_device_width, m_device_height/*, agg::no_window*/ );

			m_cx = m_device_width * 0.5;
			m_cy = m_device_height * 0.5;
		}
	}

	bool svg_render_engine::init(unsigned width, unsigned height)
	{
		destroy();
		m_buf = agg::pod_allocator<unsigned char>::allocate(width*height*m_bpp/8);
		if( m_buf != NULL )
		{
			m_rbuf_window.attach(m_buf,width,height,width*m_bpp/8);
			return true;
		}
		return false;
	}

	void svg_render_engine::destroy()
	{
		if( m_buf != NULL )
		{
			agg::pod_allocator<unsigned char>::deallocate(m_buf,0);
			m_buf = NULL;
		}
	}

	void svg_render_engine::inner_init(const agg::svg::parser& p)
	{
		m_path.bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);

		m_bcx = (m_max_x + m_min_x) * 0.5;
		m_bcy = (m_max_y + m_min_y) * 0.5;

		double width;
		double height;
		if( m_device_width != 0 )
		{
			width = m_device_width;
		}
		else
		{
			width = fabs(m_max_x + m_min_x);
		}

		if ( m_device_height != 0 )
		{
			height = m_device_height;
		}
		else
		{
			height = fabs( m_max_y + m_min_y);
		}

		agg::svg::svg_length_t width_v = p.get_width();
		agg::svg::svg_length_t height_v = p.get_height();

		if( width_v.unit == agg::svg::SVG_LENGTH_PERCENTAGE )
		{
			width = width_v.value * width / 100;
		}
		else
		{
			width = _svg_length_to_px(&width_v, get_dpi() );
		}

		if (height_v.unit == agg::svg::SVG_LENGTH_PERCENTAGE )
		{
			height = height_v.value * height / 100;
		}
		else
		{
			height = _svg_length_to_px(&height_v, get_dpi() );
		}

		m_image_width = width;
		m_image_height = height;

		if( m_device_width == 0 )
		{
			m_device_width = (unsigned)width;
		}

		if( m_device_height == 0 )
		{
			m_device_height = (unsigned)height;
		}

		init( m_device_width, m_device_height/*, agg::no_window */);

		//caption(p.title());

		agg::rect_d viewbox = p.get_viewbox();
		m_mtx_viewbox.reset();
		if ( viewbox.is_valid() )
		{
			// stretch:
			//
			//m_mtx_viewbox *= agg::trans_affine_scaling( 
			//	(viewbox.x2 - viewbox.x1) / width, 
			//	(viewbox.y2 - viewbox.y1) / height );
			//m_mtx_viewbox *= agg::trans_affine_translation(viewbox.x1, viewbox.y1);
			//m_mtx_viewbox.invert();

			// keep ratio:
			agg::trans_viewport vp;
			vp.preserve_aspect_ratio(0.5, 0.5, agg::aspect_ratio_meet);
			vp.device_viewport(0, 0, width, height);
			vp.world_viewport(viewbox.x1, viewbox.y1, viewbox.x2, viewbox.y2);
			if ( vp.is_valid() )
			{
				m_mtx_viewbox = vp.to_affine();
			}
		}
		else if ( !m_mtx_viewbox.is_identity() )
		{
			throw agg::svg::exception("not a valid viewbox");
		}

		original_view();
	}

	bool svg_render_engine::original_view()
	{
		if( m_active_path_id >= 0 )
		{
			m_path.path_transform(m_active_path_id).reset();
		}
		else
		{
			m_scale = 1.0;
			m_bcx = (m_max_x + m_min_x) * 0.5;
			m_bcy = (m_max_y + m_min_y) * 0.5;
			m_cx = m_device_width * 0.5;
			m_cy = m_device_height * 0.5;
			m_mtx.reset();
			m_mtx_zoom.reset();
			m_mtx_move.reset();
			m_mtx_rotate.reset();
		}

		return true;
	}
	void svg_render_engine::parse_svg(const wchar_t* fname)
	{		
		reset();

		m_path.remove_all();
		agg::svg::parser p(m_path);
		p.parse(fname);

		inner_init(p);
	}

	void svg_render_engine::parse_svg(const void* data, size_t size)
	{		
		reset();

		m_path.remove_all();
		agg::svg::parser p(m_path);
		p.parse(data, size);

		inner_init(p);
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// background color
	//
	void svg_render_engine::set_bg_color( agg::rgba8 v )
	{
		m_bg_color = v;
	}

	agg::rgba8 svg_render_engine::get_bg_color() const
	{
		return m_bg_color;
	}

	void svg_render_engine::draw_on_buffer(
		agg::rendering_buffer& rbuf, bool bFillWithBgColor, bool bDemultiply )
	{
		typedef agg::pixfmt_bgra32 pixfmt;

		typedef agg::renderer_base<pixfmt> renderer_base;
		typedef agg::renderer_scanline_aa_solid<renderer_base> renderer_aa_solid;
		typedef agg::renderer_scanline_bin_solid<renderer_base> renderer_bin_solid;

		pixfmt pixf(rbuf);
		renderer_base rb(pixf);
		renderer_aa_solid ren(rb);
		//renderer_bin_solid ren_bin_solid(rb);

		// fill the buffer with the background color;
		if( bFillWithBgColor )
			rb.clear(m_bg_color) ;

		// clear the background and set the alpha with 0.
		//rb.clear(agg::rgba(0,0,0,0));

		// set the background color with white and set the alpha with 1.
		//rb.clear(agg::rgba(1,1,1,1));

		agg::rasterizer_scanline_aa<agg::rasterizer_sl_clip_dbl> ras;
		//agg::scanline_p8 sl_p;
		//agg::scanline_u8 sl;
		//ras.gamma(agg::gamma_power(m_gamma.value()));

		//m_path.expand(m_expand.value());
		m_path.expand(0); // 这个expand()调用不可少
		agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox;
		//start_timer();		
		//m_path.render(ras,sl,ren, mtx, rb.clip_box(), 1.0);
		m_path.render2(ras, rb, mtx, m_active_path_id);
		//double tm = elapsed_time();

		if ( bDemultiply )
		{
			pixf.demultiply();
		}

		/*
		unsigned vertex_count = m_path.vertex_count();
		ras.gamma(agg::gamma_none());
		agg::render_ctrl(ras, sl, rb, m_expand);
		agg::render_ctrl(ras, sl, rb, m_gamma);
		agg::render_ctrl(ras, sl, rb, m_scale);
		agg::render_ctrl(ras, sl, rb, m_rotate);

		char buf[128]; 
		agg::gsv_text t;
		t.size(10.0);
		t.flip(true);

		agg::conv_stroke<agg::gsv_text> pt(t);
		pt.width(1.5);

		sprintf(buf, "Vertices=%d Time=%.3f ms", vertex_count, tm);

		t.start_point(10.0, 40.0);
		t.text(buf);

		ras.add_path(pt);
		ren.color(agg::rgba(0,0,0));
		agg::render_scanlines(ras, sl, ren);
		*/
	}

	void svg_render_engine::on_draw()
	{
		// 注: 现在的on_draw()函数被ShowEngine.dll使用,它要求单次绘制都必须执行premultiply操作.
		// 因此,现在先这样实现on_draw()函数.
		draw_on_buffer( rbuf_window(), true, true );
		redraw_finished();
	}

	bool svg_render_engine::translate(double dx, double dy, bool abs)
	{
		// translation transform
		m_mtx *= m_mtx_viewbox;

		m_mtx /= m_mtx_move;
		
		if( abs )
		{
			m_mtx_move.reset();
		}

		m_mtx_move *= agg::trans_affine_translation(dx, dy);
		m_mtx *= m_mtx_move;
		m_mtx /= m_mtx_viewbox;

		return true;
	}
	bool svg_render_engine::scale(double _scale, bool abs)
	{
		return zoom( m_cx, m_cy, _scale, abs );
	}

	bool svg_render_engine::rotate(double _rad, bool abs)
	{
		return rotate_by_rad(m_cx, m_cy, _rad, abs);
	}

	bool svg_render_engine::zoom(double cx, double cy, double scale, bool abs)
	{
		if( scale <= 0 )
		{
			return false;
		}

		// scaling transform
		// 指定中心点的放大缩小，中心点将不变
		m_mtx *= m_mtx_viewbox;

		m_mtx *= agg::trans_affine_translation( -cx, -cy );
		m_mtx /= m_mtx_zoom;

		if( abs )
		{
			m_mtx_zoom.reset();
		}

		m_mtx_zoom *= agg::trans_affine_scaling(scale);
		m_mtx *= m_mtx_zoom;
		m_mtx *= agg::trans_affine_translation(cx, cy);

		m_mtx /= m_mtx_viewbox;

		return true;
	}

	void svg_render_engine::zoom()
	{
		// scaling by current state value
		zoom( m_cx, m_cy, m_scale, true );
	}

	void svg_render_engine::get_trans_affine( double* matrix ) const
	{
		m_mtx.store_to(matrix);
	}

	void svg_render_engine::apply_trans_affine(  const double* matrix )
	{
		agg::trans_affine mtx(matrix);
		if( !mtx.is_valid() )
		{
			throw agg::svg::exception("not a valid matrix");
			return;
		}

		m_mtx.load_from( matrix );
		m_mtx_zoom.reset();
		m_mtx_move.reset();
		m_mtx_rotate.reset();
	}

	void svg_render_engine::set_adjust_trans_affine(const double* matrix ) 
	{
		// 用临时变量验证该参数是否有效
		agg::trans_affine mtx(matrix);
		if( !mtx.is_valid() )
		{
			throw agg::svg::exception("not a valid matrix");
			return;
		}
		
		m_mtx_adjust.load_from(matrix);
	}

	void svg_render_engine::get_adjust_trans_affine( double* matrix ) const
	{
		m_mtx_adjust.store_to(matrix);
	}

	inline double min4( double v1, double v2, double v3, double v4 )
	{
		if( v2 < v1 ) v1 = v2;
		if ( v3 < v1 ) v1 = v3;
		if ( v4 < v1 ) v1 = v4;
		return v1;
	}

	inline double max4( double v1, double v2, double v3, double v4 )
	{
		if( v2 > v1 ) v1 = v2;
		if ( v3 > v1 ) v1 = v3;
		if ( v4 > v1 ) v1 = v4;
		return v1;
	}

	void svg_render_engine::get_image_box(
		double*left, double*top, double* right, double* bottom ) const
	{
		agg::trans_affine mtx = m_mtx; 
		mtx *=  m_mtx_viewbox;

		double l0 = 0, b0 = m_image_height;
		double t0 = 0, r0 = m_image_width;
		double l1 = 0, b1 = m_image_height;
		double t1 = 0, r1 = m_image_width;

		mtx.transform( &l0, &t0);
		mtx.transform( &r0, &b0);
		mtx.transform( &l1, &b1);
		mtx.transform( &r1, &t1);

		// 这里其实是两个正规化(规则化)的矩形的UNION运算
		*left = min4( l0, l1, r0, r1 );
		*right = max4( l0, l1, r0, r1 );
		*top = min4( t0, t1, b0, b1 );
		*bottom  = max4( t0, t1, b0, b1 );
	}

	void svg_render_engine::transform( double& x, double& y ) const
	{
		m_mtx.transform( &x, &y );
		m_mtx_viewbox.transform( &x, &y );
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// svg_render_engine_ex - implementations
	//
	//////////////////////////////////////////////////////////////////////////

	bool svg_render_engine_ex::original_view()
	{
		svg_render_engine::original_view();

		if( !m_hit_flag )
		{
			m_x = (0.0);
			m_y = (0.0);
			m_dx = (0.0);
			m_dy = (0.0);
			m_hx = 0;
			m_hy = 0;
		}

		return true;
	}

	void svg_render_engine_ex::reset()
	{
		svg_render_engine::reset();

		m_x = (0.0);
		m_y = (0.0);
		m_dx = (0.0);
		m_dy = (0.0);
		m_hx = 0;
		m_hy = 0;
		m_drag_flag = false;
		m_hit_flag = false;
	}

	int svg_render_engine_ex::hit_test( int x, int y )
	{
		agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox;
		const agg::rendering_buffer& rbuf = rbuf_window();
		agg::rect_i clipbox(0,0,rbuf.width(), rbuf.height());
		return m_path.hit_test_in_box(mtx, x, y, clipbox);
	}

	void svg_render_engine_ex::on_resize(int cx, int cy)
	{
	}

	void svg_render_engine_ex::on_mouse_button_down(int x, int y, unsigned flags)
	{
		const agg::rendering_buffer& rbuf = rbuf_window();
		agg::rect_i clipbox(0,0,rbuf.width(), rbuf.height());
		m_drag_flag = clipbox.hit_test(x,y);
		if( !m_drag_flag )
		{
			return ;
		}

		m_dx = x - m_x;
		m_dy = y - m_y;
		m_hx = x;
		m_hy = y;

		int hit_path_id = hit_test( x, y );
		if( m_active_path_id != -1  )
		{
			agg::trans_affine mtx = m_mtx_adjust * m_mtx * m_mtx_viewbox;
			agg::rect_d bounding_box;
			if ( m_path.path_bounding_box(mtx, m_active_path_id, bounding_box ) )
			{
				if( bounding_box.hit_test( x, y ) )
				{
					if( hit_path_id < m_active_path_id )
					{
						// don't change hitting status
						return ;
					}
				}
			}
		}
		
		if( hit_path_id != m_active_path_id )
		{
			m_active_path_id = hit_path_id;
			m_hit_flag = m_active_path_id >= 0;
			force_redraw();
		}
	}

	void svg_render_engine_ex::on_mouse_move(int x, int y, unsigned flags)
	{
		if(flags == 0)
		{
			m_drag_flag = false;
		}
		else if(m_drag_flag)
		{
			if(m_active_path_id >= 0 )
			{
				int _tx = x - m_hx;
				int _ty = y - m_hy;

				// move a single path
				m_path.move_path( m_active_path_id, _tx, _ty);

				m_hx = x;
				m_hy = y;
			}
			else
			{
				// move all picture
				m_x = x - m_dx;
				m_y = y - m_dy;

				translate( m_x, m_y, true );
			}

			force_redraw();
		}
	}

	void svg_render_engine_ex::on_mouse_button_up(int x, int y, unsigned flags)
	{
		m_drag_flag = false;
	}

	void svg_render_engine_ex::on_key(int x, int y, unsigned key, unsigned flags)
	{
		//if(key == ' ')
		//{
			//agg::trans_affine mtx;
			//mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * -0.5, (m_min_y + m_max_y) * -0.5);
			//mtx *= agg::trans_affine_scaling(m_scale.value());
			//mtx *= agg::trans_affine_rotation(agg::deg2rad(m_rotate.value()));
			//mtx *= agg::trans_affine_translation((m_min_x + m_max_x) * 0.5, (m_min_y + m_max_y) * 0.5);
			//mtx *= agg::trans_affine_translation(m_x, m_y);

			//double m[6];
			//mtx.store_to(m);

			//char buf[128];
			//sprintf(buf, "%3.3f, %3.3f, %3.3f, %3.3f, %3.3f, %3.3f",
			//	m[0], m[1], m[2], m[3], m[4], m[5]);

			//message(buf);
			//FILE* fd = fopen(full_file_name("transform.txt"), "a");
			//fprintf(fd, "%s\n", buf);
			//fclose(fd);
		//}
	}
}