///=============================================================================
/// Product   : svg_viewer
/// Project   : svg_viewer
/// Module    : svg_developer_engine.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	svg 编辑器引擎
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-15 Created.
///=============================================================================

#ifndef SVG_DEVELOPER_ENGINE_INCLUDED
#define SVG_DEVELOPER_ENGINE_INCLUDED

#include "../../include/agg_basics.h"
#include "../../include/agg_trans_affine.h"
#include "../../include/agg_rendering_buffer.h"
#include "../../include/agg_color_rgba.h"
#include "agg_svg_path_renderer.h"
#include "svg_smart_object.h"
#include "svg_image.h"
#include <map>

namespace svg
{
	typedef smart_ptr < smart_object< svg_image > >			 svg_image_ptr;
	typedef smart_object_holder< smart_object< svg_image > > svg_image_holder;

	typedef unsigned LAYERID;

	class svg_developer_engine
	{
	public:
		svg_developer_engine(void);
		~svg_developer_engine(void);

		unsigned			m_dpi;
		unsigned			m_bpp;
		unsigned char*		m_buf;
		agg::rendering_buffer m_rbuf_window;

		typedef std::map<LAYERID, svg_image_holder> svg_map_t;
		svg_map_t m_svg_map;

		unsigned m_device_width;
		unsigned m_device_height;
		// svg image dimension
		
		unsigned m_image_width;
		unsigned m_image_height;

		agg::rgba8 m_bg_color; // background color

		int m_active_path_id;
		bool m_need_redraw_flag;

		double m_scale;
		agg::trans_affine m_mtx_zoom;
		agg::trans_affine m_mtx_move;
		agg::trans_affine m_mtx_rotate;
		agg::trans_affine m_mtx;

		//std::wstring m_caption;
		// DPI - dot per inch
		void       set_dpi(unsigned dpi){	m_dpi = dpi; }
		unsigned   get_dpi() const { return m_dpi; }

		// background color
		void set_bg_color( agg::rgba8 v ) { m_bg_color = v; }
		agg::rgba8 get_bg_color() const { return m_bg_color; }

	protected:
		virtual void reset();
		virtual bool init(unsigned width, unsigned height);
		virtual void destroy();

	protected:
		void zoom();
		bool zoom(double cx, double cy, double scale, bool abs);

	public:
		// add new svg, and specifies the layer
		svg_image_ptr add_svg(const wchar_t* fname, LAYERID layer);

		// remove the svg by layer
		void remove_svg(LAYERID layer);
		
		// clear all svg objects
		void clear_svg();

		// if new status is true, select all svg images
		// else deselect all svg images
		bool change_select_status_all( bool new_status );

		// returns the internal svg_image object smart pointer(maybe null).
		svg_image_ptr get_svg(LAYERID layer);
		const svg_image_ptr get_svg(LAYERID layer) const;

		// image view info
		unsigned get_image_view_width() const;
		unsigned get_image_view_height() const;

		bool is_need_redraw(){return m_need_redraw_flag;}
		
		// rendering buffer
		const agg::rendering_buffer& rbuf_window() const{ return m_rbuf_window; } 

		void get_trans_affine( double* matrix ) const;
		void apply_trans_affine( const double* matrix );
		void       set_device_dimension(unsigned width, unsigned height);
		unsigned   device_width()  const { return m_device_width; }
		unsigned   device_height() const { return m_device_height; }
		void on_draw();
		void draw_on_buffer( agg::rendering_buffer& rbuf, bool bFillWithBgColor, bool bDemultiply );

		// returns true, visible, false, invisible
		bool is_svg_visible( LAYERID layer ) const; 

		bool is_svg_selected( LAYERID layer ) const; 

		// if no such image, returns false, else returns true
		// v == true, show it, v == false, hide it
		bool show_svg( LAYERID layer, bool v ); 

		// if no such image, returns false, else returns true
		// v == true, select it, v == false, deselect it
		bool select_svg( LAYERID layer, bool v );

		bool translate_svg(LAYERID layer, double dx, double dy, bool abs = false);

		bool translate_svg(svg_image_ptr image_ptr, double dx, double dy, bool abs = false);

		//bool rotate_svg_by_rad(LAYERID layer, double cx, double cy, double rad, bool abs);
		bool rotate_svg_by_rad(LAYERID layer, double rad, bool abs);
		//bool rotate_svg(LAYERID layer, double cx, double cy, double deg, bool abs);
		bool rotate_svg(LAYERID layer, double deg, bool abs);
		bool original_view_svg(LAYERID layer);
		// translate all selected svg images
		bool translate_svg_selected(double dx, double dy, bool abs = false);

		// 根据给定的坐标,尝试选中一个svg,返回svg对象的智能指针(如果没有任何svg被选中,返回null)
		svg_image_ptr try_select_svg(unsigned x, unsigned y);

	public:
		// relative transformations
		bool zoom_in();
		//bool zoom_in(double cx, double cy);
		bool zoom_out();
		//bool zoom_out(double cx, double cy);
		bool original_view();

		bool translate(double dx, double dy, bool abs = false);

		//bool rotate_by_rad(double cx, double cy, double rad, bool abs);
		//bool rotate(double cx, double cy, double deg, bool abs);
	};

} // namespace svg

#endif // SVG_DEVELOPER_ENGINE_INCLUDED