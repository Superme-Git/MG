///=============================================================================
/// Product   : svg_viewer
/// Project   : svg_viewer
/// Module    : svg_image.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	svg_image
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-15 Created.
///=============================================================================
#ifndef SVG_IMAGE_H_INCLUDED
#define SVG_IMAGE_H_INCLUDED

#include "../../include/agg_basics.h"
#include "../../include/agg_trans_affine.h"
#include "../../include/agg_rendering_buffer.h"
#include "../../include/agg_color_rgba.h"
#include "agg_svg_path_renderer.h"

namespace agg
{
	namespace svg
	{
		class parser;
	}
}

namespace svg
{
	class svg_image
	{
	public:
		svg_image();

	protected:
		agg::svg::path_renderer m_path;
		std::wstring m_caption;

		unsigned m_image_id; // initialized by user

		unsigned m_image_width;
		unsigned m_image_height;

		// bounding-box
		double m_min_x;
		double m_min_y; 
		double m_max_x; 
		double m_max_y; 
		double m_cx;  // clipping-box center x
		double m_cy;  // clipping-box center y
		double m_bcx; // bounding-box center x
		double m_bcy; // bounding-box center y
		bool m_show_flag;
		bool m_select_flag;
		bool m_bbox_flag; // bounding-box flag
		agg::trans_affine m_mtx_translate, m_mtx_rotate, m_mtx_viewbox;
		agg::trans_affine m_mtx;

		unsigned m_dpi;

		unsigned get_dpi() const { return m_dpi; }

		void inner_init(const agg::svg::parser& p);
		void draw_tracking_box(agg::rendering_buffer& rbuf, const agg::trans_affine& mtx);

	public:
		void set_dpi(unsigned dpi){	m_dpi = dpi; }

		void set_image_id( unsigned image_id )
		{
			m_image_id = image_id;
		}

		unsigned get_image_id( ) const
		{
			return m_image_id;
		}

		// initialize
		bool parse(const wchar_t * fname);

		// 不受show_flag/select_flag控制,不受环境变换控制的绘制函数
		// 不画tracking_box
		void draw(agg::rendering_buffer& rbuf);

		// 受show_flag/select_flag控制,受环境变换控制的绘制函数
		void render(agg::rendering_buffer& rbuf, const agg::trans_affine& mtx);

	public:
		// operation
		void get_trans_affine( double* matrix ) const;
		void set_trans_affine( const double* matrix );

		bool translate(double dx, double dy, bool abs = false);
		bool rotate(double deg, bool abs = false);
		bool rotate_by_rad(double rad, bool abs = false);

		bool reset();
		bool show(bool v)
		{
			bool old = m_show_flag;
			m_show_flag = v;
			return old != v;
		}

		bool select(bool v)
		{
			bool old = m_select_flag;
			m_select_flag = v;
			return old != v;
		}

		bool show_bounding_box( bool v )
		{
			bool old = m_bbox_flag;
			m_bbox_flag = v;
			return old != v;
		}

		bool try_select(unsigned x, unsigned y);

	public:
		// status
		bool visible() const { return m_show_flag; }
		bool selected() const { return m_select_flag; }
		

		// image info
		const wchar_t* caption() const { return m_caption.c_str(); }
		unsigned image_width() const { return m_image_width; }
		unsigned image_height() const { return m_image_height; }

		// 在当前变换下计算image_box参数,可用于计算svg的tracking_box坐标
		void get_image_box(double& left, double& top, double& right, double& bottom) const;

		// 在当前变换下计算bounding_box,可用于计算svg的最小显示纹理
		void bounding_rect(double& left, double& top, double& right, double& bottom);

		// 在当前变换下计算点的变换后的坐标
		//
		// x - [INOUT] 横坐标
		// y - [INOUT] 纵坐标
		//
		void transform( double& x, double& y ) const;
	};

} // namespace svg

#endif // SVG_IMAGE_H_INCLUDED