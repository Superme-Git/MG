///=============================================================================
/// Product   : svg_viewer
/// Project   : svg_viewer
/// Module    : svg_developer_engine.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	svg 编辑器引擎
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-15 Created.
///=============================================================================
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "svg_developer_engine.h"
#include "agg_pixfmt_rgba.h"

namespace svg
{	
	typedef agg::pixfmt_bgra32 pixfmt;
	typedef agg::renderer_base<pixfmt> renderer_base;

	svg_developer_engine::svg_developer_engine(void) :
		m_svg_map(),
		m_dpi(96),
		m_bpp(32),
		m_bg_color(0,0,0,0),
		m_need_redraw_flag(false),
		m_active_path_id(-1),
		m_buf(NULL),
		m_device_width(0),
		m_device_height(0),
		m_image_width(0),
		m_image_height(0),
		m_scale(1.0)
	{
	}

	svg_developer_engine::~svg_developer_engine(void)
	{
		destroy();
	}

	void svg_developer_engine::reset()
	{
		m_need_redraw_flag = false;
		m_active_path_id = -1;
		m_scale = 1.0;
		m_mtx.reset();
		m_mtx_zoom.reset();
		m_mtx_move.reset();
		m_mtx_rotate.reset();
	}

	unsigned svg_developer_engine::get_image_view_width() const
	{
		unsigned image_view_width = agg::uround( m_image_width * m_mtx_zoom.scale() );
		return image_view_width;
	}

	unsigned svg_developer_engine::get_image_view_height() const
	{
		unsigned image_view_height = agg::uround( m_image_height * m_mtx_zoom.scale() );
		return image_view_height;
	}

	void svg_developer_engine::set_device_dimension(unsigned width ,unsigned height)
	{
		// 如果是parse之后重新设置device dimension, 此时需要重新init
		if( width > 0 && width != m_device_width || height > 0 && height != m_device_height )
		{
			m_device_width = width;
			m_device_height = height;
			init( m_device_width, m_device_height );
		}
	}

	bool svg_developer_engine::init(unsigned width, unsigned height)
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

	void svg_developer_engine::destroy()
	{
		m_device_width = 0;
		m_device_height = 0;
		if( m_buf != NULL )
		{
			agg::pod_allocator<unsigned char>::deallocate(m_buf,0);
			m_buf = NULL;
		}
	}

	svg_image_ptr  svg_developer_engine::add_svg(const wchar_t* fname, LAYERID layer)
	{
		// parse a new svg
		svg_image_holder _image;
		if( !_image->parse( fname ) )
		{
			// returns a null image
			return svg_image_ptr();
		}

		m_svg_map[layer] = _image;
		{
			unsigned _image_height = _image->image_height();
			unsigned _image_width = _image->image_width();
			if( m_image_height < _image_height )
			{
				m_image_height = _image_height;
			}
			if( m_image_width < _image_width ) 
			{
				m_image_width = _image_width;
			}
		}
		
		return _image;
	}

	void svg_developer_engine::remove_svg(LAYERID layer)
	{
		m_svg_map.erase( layer );
	}

	void svg_developer_engine::clear_svg()
	{
		m_image_width = 0;
		m_image_height = 0;
		m_svg_map.clear();
	}

	svg_image_ptr svg_developer_engine::get_svg(LAYERID layer)
	{
		svg_map_t::iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return svg_image_ptr();
		}

		return itr->second;
	}

	const svg_image_ptr svg_developer_engine::get_svg(LAYERID layer) const
	{
		svg_map_t::const_iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return svg_image_ptr();
		}

		return itr->second;
	}

	// returns true, visible, false, invisible
	bool svg_developer_engine::is_svg_visible( LAYERID layer ) const
	{
		svg_map_t::const_iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return false;
		}
		
		const svg_image_ptr& _image = itr->second;
		return _image->visible();
	}

	// returns true, selected, false, un-selected
	bool svg_developer_engine::is_svg_selected( LAYERID layer ) const
	{
		svg_map_t::const_iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return false;
		}

		const svg_image_ptr& _image = itr->second;
		return _image->selected();
	}

	// v == true, show it, v == false, hide it
	bool svg_developer_engine::show_svg( LAYERID layer, bool v )
	{
		svg_map_t::iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return false;
		}

		svg_image_ptr& _image = itr->second;
		return _image->show(v);
	}

	// v == true, select it, v == false, deselect it
	bool svg_developer_engine::select_svg( LAYERID layer, bool v )
	{
		svg_map_t::iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return false;
		}

		svg_image_ptr& _image = itr->second;
		return _image->select(v);
	}
	
	// 根据给定的坐标,尝试选中一个svg,返回svg对象的智能指针(如果没有任何svg被选中,返回null)
	svg_image_ptr svg_developer_engine::try_select_svg(unsigned x, unsigned y)
	{
		double fx = x, fy = y;
		m_mtx.inverse_transform(&fx, &fy);
		x = agg::uround(fx);
		y = agg::uround(fy);
		svg_map_t::reverse_iterator itr = m_svg_map.rbegin();
		for ( ; itr != m_svg_map.rend(); ++ itr )
		{
			if( itr->second->try_select(x, y) )
			{
				return itr->second;
			}
		}

		return svg_image_ptr();
	}

	bool svg_developer_engine::change_select_status_all( bool new_status )
	{
		bool bAnyChanged = false;
		svg_map_t::iterator itr = m_svg_map.begin();
		for ( ; itr != m_svg_map.end(); ++ itr )
		{
			bAnyChanged = itr->second->select(new_status) || bAnyChanged;
			bAnyChanged = itr->second->show_bounding_box(new_status) || bAnyChanged;
		}

		return bAnyChanged;
	}

	bool svg_developer_engine::translate_svg(LAYERID layer, double dx, double dy, bool abs)
	{
		svg_map_t::iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return false;
		}

		svg_image_ptr& _image = itr->second;
		m_mtx_zoom.inverse_transform(&dx,&dy);
		return _image->translate( dx, dy, abs );
	}

	bool svg_developer_engine::translate_svg(svg_image_ptr image_ptr, double dx, double dy, bool abs)
	{
		if( image_ptr == NULL )
		{
			return false;
		}

		m_mtx_zoom.inverse_transform(&dx,&dy);
		return image_ptr->translate( dx, dy, abs );
	}

	bool svg_developer_engine::rotate_svg_by_rad(LAYERID layer, double rad, bool abs)
	{
		svg_map_t::iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return false;
		}

		svg_image_ptr& _image = itr->second;
		return _image->rotate_by_rad( rad, abs );
	}

	bool svg_developer_engine::rotate_svg(LAYERID layer, double deg, bool abs)
	{
		return rotate_svg_by_rad( layer, agg::deg2rad(deg), abs);
	}

	bool svg_developer_engine::original_view_svg(LAYERID layer)
	{
		svg_map_t::iterator itr = m_svg_map.find( layer );
		if( itr == m_svg_map.end() )
		{
			return false;
		}

		svg_image_ptr& _image = itr->second;
		return _image->reset( );
	}

	bool svg_developer_engine::translate_svg_selected(double dx, double dy, bool abs)
	{
		bool bAnyMoved = false;
		svg_map_t::iterator itr = m_svg_map.begin();
		for ( ; itr != m_svg_map.end(); ++ itr )
		{
			if( itr->second->selected() )
			{
				bAnyMoved = itr->second->translate(dx,dy,abs) || bAnyMoved;
			}
		}
		return bAnyMoved;
	}

	void svg_developer_engine::draw_on_buffer(
		agg::rendering_buffer& rbuf, bool bFillWithBgColor, bool bDemultiply )
	{
		pixfmt pixf(rbuf);
		renderer_base rb(pixf);

		// fill the buffer with the background color;
		if( bFillWithBgColor )
			rb.clear(m_bg_color) ;

		agg::trans_affine mtx = m_mtx;

		svg_map_t::iterator itr = m_svg_map.begin();
		for ( ; itr != m_svg_map.end(); ++ itr )
		{
			itr->second->render(rbuf, mtx);
		}
		
		if ( bDemultiply )
		{
			pixf.demultiply();
		}	
	}

	void svg_developer_engine::on_draw()
	{
		draw_on_buffer( m_rbuf_window, true, false );
		//draw_on_buffer( m_rbuf_window, true, true );
	}

	bool svg_developer_engine::zoom(double cx, double cy, double scale, bool abs)
	{
		if( scale <= 0 )
		{
			return false;
		}

		// scaling transform
		// 指定中心点的放大缩小，中心点将不变
		m_mtx *= agg::trans_affine_translation( -cx, -cy );
		m_mtx /= m_mtx_zoom;

		if( abs )
		{
			m_mtx_zoom.reset();
		}

		m_mtx_zoom *= agg::trans_affine_scaling(scale);
		m_mtx *= m_mtx_zoom;
		m_mtx *= agg::trans_affine_translation(cx, cy);

		return true;
	}

	void svg_developer_engine::zoom()
	{
		// scaling by current state value
		double m_cx, m_cy;
		m_cx = m_device_width>>1;
		m_cy = m_device_height>>1;
		zoom( m_cx, m_cy, m_scale, true );
	}

	bool svg_developer_engine::zoom_in()
	{
		double cur_value = m_scale;
		if ( cur_value > 256.0 )
			return false;

		cur_value *= 2.0;
		m_scale = cur_value;

		zoom();
		
		return true;
	}

	//bool svg_developer_engine::zoom_in(double cx, double cy)
	//{
	//	double cur_value = m_scale;
	//	if ( cur_value > 256.0 )
	//		return false;

	//	cur_value *= 2.0;
	//	m_scale = cur_value;
	//	zoom( cx, cy, m_scale, true );
	//	
	//	return true;
	//}

	bool svg_developer_engine::zoom_out()
	{	
		double cur_value = m_scale;
		if ( cur_value < 0.001 )
		{
			return false;
		}

		cur_value /= 2.0;
		m_scale = cur_value;

		zoom();

		return true;
	}

	//bool svg_developer_engine::zoom_out(double cx, double cy)
	//{
	//	double cur_value = m_scale;
	//	if ( cur_value < 0.001 )
	//	{
	//		return false;
	//	}

	//	cur_value /= 2.0;
	//	m_scale = cur_value;
	//	zoom( cx, cy, m_scale, true );
	//
	//	return true;
	//}

	bool svg_developer_engine::original_view()
	{	
		m_scale = 1.0;
		m_mtx.reset();
		m_mtx_zoom.reset();
		m_mtx_move.reset();
		m_mtx_rotate.reset();
	
		return true;
	}

	bool svg_developer_engine::translate(double dx, double dy, bool abs )
	{
		// translation transform
		m_mtx /= m_mtx_move;

		if( abs )
		{
			m_mtx_move.reset();
		}

		m_mtx_move *= agg::trans_affine_translation(dx, dy);
		m_mtx *= m_mtx_move;
	
		return true;
	}

	//bool svg_developer_engine::rotate_by_rad(double cx, double cy, double rad, bool abs)
	//{
	//	// 为了提高效率，这里使用乘运算
	//	// m_mtx *= agg::trans_affine_translation( -cx, -cy );
	//	// 等价于:
	//	// m_mtx /= agg::trans_affine_translation( cx, cy );
	//	m_mtx *= agg::trans_affine_translation( -cx, -cy );
	//	m_mtx /= m_mtx_rotate;
	//	if( abs )
	//	{
	//		m_mtx_rotate.reset();
	//	}
	//	m_mtx_rotate.rotate( rad );
	//	m_mtx *= m_mtx_rotate;
	//	m_mtx *= agg::trans_affine_translation( cx, cy );

	//	return true;
	//}

	//bool svg_developer_engine::rotate(double cx, double cy, double deg, bool abs)
	//{
	//	return rotate_by_rad( cx, cy, agg::deg2rad(deg), abs );
	//}

} // namespace svg

