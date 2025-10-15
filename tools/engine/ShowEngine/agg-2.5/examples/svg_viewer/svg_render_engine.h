#ifndef SVG_RENDER_ENGINE_INCLUDED
#define SVG_RENDER_ENGINE_INCLUDED

#include "../../include/platform/agg_platform_support.h"
#include "../../include/agg_color_rgba.h"
//#include "../../include/ctrl/agg_slider_ctrl.h"
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
	class svg_render_engine
	{
	private:
		agg::pix_format_e	m_format;
		unsigned			m_bpp;
		bool				m_flip_y;
		unsigned char*		m_buf;
		agg::rendering_buffer m_rbuf_window;
		//agg::slider_ctrl<agg::rgba8> m_expand;
		//agg::slider_ctrl<agg::rgba8> m_gamma;
		//agg::slider_ctrl<agg::rgba8> m_scale;
		//agg::slider_ctrl<agg::rgba8> m_rotate;

	private:
		unsigned m_device_width;
		unsigned m_device_height;
		unsigned m_dpi;
		agg::rgba8 m_bg_color; // background color
		
	protected:
		agg::svg::path_renderer m_path;

		// svg image dimension
		double m_image_width;
		double m_image_height;
		
		// svg bounding-box
		double m_min_x;
		double m_min_y; 
		double m_max_x; 
		double m_max_y; 
		double m_cx;  // clipping-box center x
		double m_cy;  // clipping-box center y
		double m_bcx; // bounding-box center x
		double m_bcy; // bounding-box center y

		int m_active_path_id;
		bool m_need_redraw_flag;

		double m_scale;
		agg::trans_affine m_mtx_adjust; // 原始调整仿射变换矩阵
		agg::trans_affine m_mtx_viewbox;
		agg::trans_affine m_mtx_zoom;
		agg::trans_affine m_mtx_move;
		agg::trans_affine m_mtx_rotate;
		agg::trans_affine m_mtx;
	
	protected:
		virtual void reset();
		virtual void inner_init(const agg::svg::parser& p);
		virtual bool init(unsigned width, unsigned height);
		virtual void destroy();

	protected:
		void zoom();
		bool zoom(double cx, double cy, double scale, bool abs);
		bool rotate(double cx, double cy, double deg, bool abs);
		bool rotate_by_rad(double cx, double cy, double rad, bool abs);

	public:
		svg_render_engine();
		~svg_render_engine();

		//--------------------------------------------------------------------
		// The very same parameters that were used in the constructor
		agg::pix_format_e format() const { return m_format; }
		bool flip_y() const { return m_flip_y; }
		unsigned bpp() const { return m_bpp; }

		//////////////////////////////////////////////////////////////////////////
		//
		// init and attributes-getter methods
		//
	protected:
		// rendering buffer 
		agg::rendering_buffer& rbuf_window()          { return m_rbuf_window; } 
	public:
		// rendering buffer 
		const agg::rendering_buffer& rbuf_window() const { return m_rbuf_window; } 

		// device dimension(same as rbuf_window().with() and rbuf_window().height())
		void       set_device_dimension(unsigned width, unsigned height);
		unsigned   device_width()  const { return m_device_width; }
		unsigned   device_height() const { return m_device_height; }
		
		// DPI - dot per inch
		void       set_dpi(unsigned dpi){	m_dpi = dpi; }
		unsigned   get_dpi() const { return m_dpi; }

		// background color
		void       set_bg_color( agg::rgba8 v );
		agg::rgba8 get_bg_color() const;

		// set/get adjust matrix
		void get_adjust_trans_affine( double* matrix ) const;
		void set_adjust_trans_affine( const double* matrix );

	public:
		void parse_svg(const wchar_t* fname);
		void parse_svg(const void* data, size_t size);
	
	public:
		bool is_need_redraw(){return m_need_redraw_flag;}

	protected:
		void force_redraw(){m_need_redraw_flag = true;}
		void redraw_finished(){m_need_redraw_flag = false;}

		//////////////////////////////////////////////////////////////////////////
		//
		// transformation methods
		//
	public:
		//////////////////////////////////////////////////////////////////////////
		//
		// if abs is true, perform absolute transformations, 
		// else perform relative transformations.
		//
		bool translate(double dx, double dy, bool abs = false);
		bool scale(double _scale, bool abs = false);
		bool rotate(double _rad, bool abs = false);

		//////////////////////////////////////////////////////////////////////////
		//
		// relative transformations
		//
		bool zoom_in();
		bool zoom_in(double cx, double cy);
		bool zoom_out();
		bool zoom_out(double cx, double cy);

		// rotate picture by clipping box center
		bool rotate_cw();
		bool rotate_ccw();

		// rotate object by bounding box center
		// currently, there's only one object for test
		bool rotate_cw_object();
		bool rotate_ccw_object();
		
		void get_trans_affine( double* matrix ) const;
		void apply_trans_affine( const double* matrix );

		virtual bool original_view();

	public:
		void on_draw();
		void draw_on_buffer(
			agg::rendering_buffer& rbuf, bool bFillWithBgColor, bool bDemultiply );

	public:
		//////////////////////////////////////////////////////////////////////////
		//
		// image info
		//
		double get_original_image_width() const { return m_image_width; }
		double get_original_image_height() const { return m_image_height; }

		void get_image_box(
			double*left, double*top, double* right, double* bottom ) const;

		//////////////////////////////////////////////////////////////////////////
		//
		// 在当前变换下计算点的变换后的坐标
		//
		// x - [INOUT] 横坐标
		// y - [INOUT] 纵坐标
		//
		void transform( double& x, double& y ) const;


	};

	// svg_render_engine_ex - 支持用户操作事件
	class svg_render_engine_ex : public svg_render_engine
	{
	protected:
		double	m_x;
		double	m_y;
		double	m_dx;
		double	m_dy;
		int		m_hx;// for hit and drag
		int		m_hy; 
		bool	m_drag_flag;
		bool	m_hit_flag; // hit result

	public:
		svg_render_engine_ex();

	public:
		int hit_test( int x, int y );
		bool original_view();

	protected:
		virtual void reset();
	
	public:
		virtual void on_resize(int cx, int cy);
		virtual void on_mouse_button_down(int x, int y, unsigned flags);
		virtual void on_mouse_move(int x, int y, unsigned flags);
		virtual void on_mouse_button_up(int x, int y, unsigned flags);
		virtual void on_key(int x, int y, unsigned key, unsigned flags);
	};
};

#endif // SVG_RENDER_ENGINE_INCLUDED