///=============================================================================
/// Product   : svg_viewer
/// Project   : svg_viewer
/// Module    : svg_image.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	svg_viewer
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-15 Created.
///=============================================================================
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "agg_basics.h"
#include "agg_trans_viewport.h"
#include "agg_rasterizer_scanline_aa.h"
#include "agg_scanline_p.h"
#include "agg_pixfmt_rgba.h"
#include "agg_conv_dash.h"
#include "agg_vcgen_markers_term.h"
#include "agg_svg_parser.h"
#include "svg_basic.h"
#include "svg_image.h"

namespace svg
{	
	typedef agg::pixfmt_bgra32 pixfmt;
	typedef agg::renderer_base<pixfmt> renderer_base;

	//typedef agg::comp_op_adaptor_clip_to_dst_rgba<agg::rgba8, agg::order_bgra> blend_adaptor;
	typedef agg::comp_op_adaptor_rgba<agg::rgba8, agg::order_bgra> blend_adaptor;

	typedef agg::pixfmt_custom_blend_rgba<blend_adaptor, agg::rendering_buffer> invert_fmt;

	typedef agg::renderer_base<invert_fmt> renderer_invert;
	const int TRACKER_WIDTH = 1;

	// initialize
	svg_image::svg_image()
	{
		m_image_id = 0;
		m_image_width = 0;
		m_image_height = 0;

		// bounding-box
		m_min_x = 0;
		m_min_y = 0; 
		m_max_x = 0; 
		m_max_y = 0; 
		m_cx = 0;  // clipping-box center x
		m_cy = 0;  // clipping-box center y
		m_bcx = 0; // bounding-box center x
		m_bcy = 0; // bounding-box center y
		m_show_flag = true;
		m_select_flag = false;
		m_bbox_flag = false;
		m_dpi = 96; // TODO:
	}

	bool svg_image::parse(const wchar_t * fname)
	{
		// parse a new svg
		reset();
		try
		{
			agg::svg::parser p(m_path);
			p.parse(fname);

			inner_init(p);
		}
		catch (agg::svg::exception e)
		{
			puts( e.msg() );
			return false;
		}

		return true;
	}

	void svg_image::inner_init(const agg::svg::parser& p )
	{
		m_path.bounding_rect(&m_min_x, &m_min_y, &m_max_x, &m_max_y);

		m_bcx = (m_max_x + m_min_x) * 0.5;
		m_bcy = (m_max_y + m_min_y) * 0.5;

		// 计算参考值(TODO:)
		double width = fabs( m_max_x + m_min_x );
		double height = fabs( m_max_y + m_min_y );

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

		m_image_width  = agg::uround( width );
		m_image_height = agg::uround( height );

		if( p.title() != NULL )
		{
			size_t result = 0;
			wchar_t buffer[1024];
			errno_t err = mbstowcs_s( &result, buffer, p.title(), _TRUNCATE );
			if( err != -1 && err != EINVAL )
			{
				m_caption.assign(buffer);
			}
		}

		agg::rect_d viewbox = p.get_viewbox();
		m_mtx_viewbox.reset();
		if ( viewbox.is_valid() )
		{
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

		reset();
	}

	void svg_image::draw_tracking_box(agg::rendering_buffer& rbuf, const agg::trans_affine& mtx)
	{
		double x1=0, x2=0, y1=0, y2=0;
		get_image_box( x1, y1, x2, y2 );
		agg::rasterizer_scanline_aa<agg::rasterizer_sl_no_clip> ras;
		agg::rounded_rect rc(x1+TRACKER_WIDTH,y1+TRACKER_WIDTH,
			x2-TRACKER_WIDTH,y2-TRACKER_WIDTH,0);

		typedef agg::conv_dash<agg::rounded_rect, agg::vcgen_markers_term > dashed_rect;
		dashed_rect d_rect(rc);
		d_rect.add_dash(5.0, 2.0);
		d_rect.add_dash(2.0, 2.0);
		d_rect.dash_start(0.0);

		agg::conv_stroke<dashed_rect> rc_stroke(d_rect);
		rc_stroke.width(1);
		
		agg::conv_transform<agg::conv_stroke<dashed_rect>,agg::trans_affine> 
			rc_stroke_trans(rc_stroke, mtx);
		ras.add_path(rc_stroke_trans);

		//invert_fmt ifmt(rbuf, agg::comp_op_invert);
		invert_fmt ifmt(rbuf, agg::comp_op_src_over);
		renderer_invert inv_blender(ifmt);
		agg::scanline_u8 sl;
		agg::render_scanlines_aa_solid(ras,sl,inv_blender, agg::rgba8(255,0,0,255));
	}

	void svg_image::draw(agg::rendering_buffer& rbuf)
	{
		// 不受show_flag/select_flag控制,不受环境变换控制的绘制函数
		agg::trans_affine mtx_final = m_mtx;
		mtx_final *= m_mtx_viewbox;
		pixfmt pixf(rbuf);
		renderer_base rb(pixf);
		agg::rasterizer_scanline_aa<agg::rasterizer_sl_clip_dbl> ras;
		m_path.expand(0);
		m_path.render2(ras, rb, mtx_final, -1, false );
	}

	void svg_image::render(agg::rendering_buffer& rbuf, const agg::trans_affine& mtx)
	{
		if( m_show_flag )
		{
			agg::trans_affine mtx_final = m_mtx;
			mtx_final *= m_mtx_viewbox;
			mtx_final *= mtx;
			pixfmt pixf(rbuf);
			renderer_base rb(pixf);
			agg::rasterizer_scanline_aa<agg::rasterizer_sl_clip_dbl> ras;
			m_path.expand(0);
			//m_path.render2(ras, rb, mtx_final, -1, m_select_flag );

			m_path.render2(ras, rb, mtx_final, -1, m_bbox_flag );
			if ( !m_bbox_flag && m_select_flag )
			{
				// 绘制tracker矩形
				draw_tracking_box(rbuf, mtx);
			}
		}
	}

	void svg_image::get_trans_affine( double* matrix ) const
	{
		m_mtx.store_to( matrix );
	}

	void svg_image::set_trans_affine( const double* matrix )
	{
		reset();
		m_mtx.load_from( matrix );
		m_mtx_translate.translate( matrix[4], matrix[5] );
	}

	bool svg_image::translate(double dx, double dy, bool abs)
	{
		m_mtx *= m_mtx_viewbox;
		if( abs )
		{
			m_mtx /= m_mtx_translate;
			m_mtx_translate.reset();
		}
		m_mtx_translate.translate(dx,dy);
		m_mtx.translate(dx,dy);
		m_mtx /= m_mtx_viewbox;

		return true;
	}

	bool svg_image::rotate(double deg, bool abs)
	{
		return rotate_by_rad( agg::deg2rad(deg), abs );
	}

	bool svg_image::rotate_by_rad(double rad, bool abs)
	{
		// 图像中心点
		//double cx = (m_image_width+1)>>1;
		//double cy = (m_image_height+1)>>1;

		// 使用bounding_box center
		double cx = m_bcx;
		double cy = m_bcy;
		transform(cx,cy);

		m_mtx *= m_mtx_viewbox;
		m_mtx *= agg::trans_affine_translation( -cx, -cy );
		if ( abs )
		{
			m_mtx /= m_mtx_rotate;
			m_mtx_rotate = agg::trans_affine_rotation(rad);
		}
		else
		{
			m_mtx_rotate.rotate(rad);
		}
		m_mtx.rotate(rad);
		m_mtx *= agg::trans_affine_translation( cx, cy );
		m_mtx /= m_mtx_viewbox;
		return true;
	}

	bool svg_image::reset()
	{
		m_mtx_translate.reset();
		m_mtx_rotate.reset();
		m_mtx.reset();

		return true;
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

	void svg_image::get_image_box(
		double& left, double& top, double& right, double& bottom) const
	{
		agg::trans_affine mtx = m_mtx;
		
		// 计算实际出现在(image_width,image_height)上的可见点坐标
		double view_height= m_image_height;
		double view_width = m_image_width;
		m_mtx_viewbox.inverse_transform( &view_width, &view_height );

		double l0 = 0, b0 = view_height;
		double t0 = 0, r0 = view_width;
		double l1 = 0, b1 = view_height;
		double t1 = 0, r1 = view_width;

		mtx.transform( &l0, &t0);
		mtx.transform( &r0, &b0);
		mtx.transform( &l1, &b1);
		mtx.transform( &r1, &t1);

		// 这里其实是两个正规化(规则化)的矩形的UNION运算
		left = min4( l0, l1, r0, r1 );
		right = max4( l0, l1, r0, r1 );
		top = min4( t0, t1, b0, b1 );
		bottom  = max4( t0, t1, b0, b1 );

		// 在viewbox中还原坐标
		m_mtx_viewbox.transform(&left,&top);
		m_mtx_viewbox.transform(&right,&bottom);
	}

	bool svg_image::try_select(unsigned x, unsigned y)
	{
		if ( !m_show_flag )
		{
			return false;
		}

		agg::trans_affine mtx_final = m_mtx;
		mtx_final *= m_mtx_viewbox;
		
		if( m_path.hit_test( mtx_final, x, y ) != -1 )
		{
			m_select_flag = true;
			m_bbox_flag = true; 
			return true;
		}

		return false;
	}

	// 在当前变换下计算bounding_box,可用于计算svg的最小显示纹理
	void svg_image::bounding_rect(double& left, double& top, double& right, double& bottom)
	{
		m_path.bounding_rect(&left, &top, &right, &bottom);
	}

	void svg_image::transform( double& x, double& y ) const
	{
		m_mtx_viewbox.inverse_transform( &x, &y );
		m_mtx.transform( &x, &y );
		m_mtx_viewbox.transform( &x, &y );
	}

} // namespace svg
