//----------------------------------------------------------------------------
// Anti-Grain Geometry - Version 2.3
// Copyright (C) 2002-2005 Maxim Shemanarev (http://www.antigrain.com)
//
// Permission to copy, use, modify, sell and distribute this software 
// is granted provided this copyright notice appears in all copies. 
// This software is provided "as is" without express or implied
// warranty, and with no claim as to its suitability for any purpose.
//
//----------------------------------------------------------------------------
// Contact: mcseem@antigrain.com
//          mcseemagg@yahoo.com
//          http://www.antigrain.com
//----------------------------------------------------------------------------
//
// SVG path renderer.
//
//----------------------------------------------------------------------------

#include <stdio.h>
#include "agg_svg_path_renderer.h"
#include "agg_ellipse.h"
#include "agg_gsv_text.h"

namespace agg
{
namespace svg
{

    //------------------------------------------------------------------------
    path_renderer::path_renderer() :
        m_curved(m_storage),
        m_curved_count(m_curved),

        m_curved_stroked(m_curved_count),
        m_curved_stroked_trans(m_curved_stroked, m_transform),

        m_curved_trans(m_curved_count, m_transform),
        m_curved_trans_contour(m_curved_trans)
    {
        m_curved_trans_contour.auto_detect_orientation(false);
		m_current_gradient = NULL;
    }
	
	path_renderer::~path_renderer()
	{
		remove_all();
	}

    //------------------------------------------------------------------------
    void path_renderer::remove_all()
    {
        m_storage.remove_all();
        m_attr_storage.remove_all();
        m_attr_stack.remove_all();
        m_transform.reset();
		m_current_gradient = NULL;

		svg_gradient_elements::iterator itr = m_gradients.begin();
		for( ; itr != m_gradients.end(); ++ itr )
		{
			delete (*itr).second;
		}

		
		m_gradients.clear();
    }


	void path_renderer::begin_pattern()
	{		
		push_attr();
		path_attributes& attr = cur_attr();
		attr.pattern_flag = true;
	}

	void path_renderer::end_pattern()
	{
		pop_attr();
	}

	void path_renderer::begin_defs()
	{
		push_attr();
		path_attributes& attr = cur_attr();
		attr.defs_flag = true;		
	}

	void path_renderer::end_defs()
	{
		pop_attr();
	}

    //------------------------------------------------------------------------
    void path_renderer::begin_path()
    {
        push_attr();
		unsigned idx = m_storage.start_new_path();
		m_attr_storage.add(path_attributes(cur_attr(), idx));
    }

    //------------------------------------------------------------------------
    void path_renderer::end_path()
    {
        if(m_attr_storage.size() == 0) 
        {
            throw exception("end_path : The path was not begun");
        }
		// 因为这里要pop_attr,所以,直接使用引用修改cur_attr()没有关系
		// path_attributes attr = cur_attr(); 
		// by yanghaibo
		path_attributes& attr = cur_attr(); 
		unsigned idx = m_attr_storage[m_attr_storage.size() - 1].index;
        attr.index = idx;
        m_attr_storage[m_attr_storage.size() - 1] = attr;
        pop_attr();
    }

	void path_renderer::circle(double cx, double cy, double r)
	{
		//
		// bezier_arc is not a closed path, even if we specify the sweep angle is 2pi
		//

		bezier_arc a(cx, cy, r, r, 0, pi2 );
		path_attributes attr = cur_attr();
		m_storage.concat_path(a,attr.index);
		
		/*m_storage.start_new_path();
		m_storage.join_path(a,attr.index);
		m_storage.vline_to(cy);
		m_storage.end_poly(path_flags_close);*/
	}

	void path_renderer::ellipse_(double cx, double cy, double rx, double ry)
	{
		bezier_arc a(cx, cy, rx, ry, 0, pi2 );
		path_attributes attr = cur_attr();
		m_storage.concat_path(a,attr.index);

		//m_storage.start_new_path();
		//m_storage.join_path(a,attr.index);
		//m_storage.vline_to(cy);
		//m_storage.end_poly(path_flags_close);
	}

	void path_renderer::ell_arc_to(double x, double y, bool rel)
	{
		if(rel) m_storage.rel_to_abs(&x, &y);
		m_storage.ell_arc_to(x, y);
	}

    //------------------------------------------------------------------------
    void path_renderer::move_to(double x, double y, bool rel)          // M, m
    {
        if(rel) m_storage.rel_to_abs(&x, &y);
        m_storage.move_to(x, y);
    }

    //------------------------------------------------------------------------
    void path_renderer::line_to(double x,  double y, bool rel)         // L, l
    {
        if(rel) m_storage.rel_to_abs(&x, &y);
        m_storage.line_to(x, y);
    }

    //------------------------------------------------------------------------
    void path_renderer::hline_to(double x, bool rel)                   // H, h
    {
        double x2 = 0.0;
        double y2 = 0.0;
        if(m_storage.total_vertices())
        {
            m_storage.vertex(m_storage.total_vertices() - 1, &x2, &y2);
            if(rel) x += x2;
            m_storage.line_to(x, y2);
        }
    }

    //------------------------------------------------------------------------
    void path_renderer::vline_to(double y, bool rel)                   // V, v
    {
        double x2 = 0.0;
        double y2 = 0.0;
        if(m_storage.total_vertices())
        {
            m_storage.vertex(m_storage.total_vertices() - 1, &x2, &y2);
            if(rel) y += y2;
            m_storage.line_to(x2, y);
        }
    }

    //------------------------------------------------------------------------
    void path_renderer::curve3(double x1, double y1,                   // Q, q
                               double x,  double y, bool rel)
    {
        if(rel) 
        {
            m_storage.rel_to_abs(&x1, &y1);
            m_storage.rel_to_abs(&x,  &y);
        }
        m_storage.curve3(x1, y1, x, y);
    }

    //------------------------------------------------------------------------
    void path_renderer::curve3(double x, double y, bool rel)           // T, t
    {
//        throw exception("curve3(x, y) : NOT IMPLEMENTED YET");
        if(rel) 
        {
            m_storage.curve3_rel(x, y);
        } else 
        {
            m_storage.curve3(x, y);
        }
    }

    //------------------------------------------------------------------------
    void path_renderer::curve4(double x1, double y1,                   // C, c
                               double x2, double y2, 
                               double x,  double y, bool rel)
    {
        if(rel) 
        {
            m_storage.rel_to_abs(&x1, &y1);
            m_storage.rel_to_abs(&x2, &y2);
            m_storage.rel_to_abs(&x,  &y);
        }
        m_storage.curve4(x1, y1, x2, y2, x, y);
    }

    //------------------------------------------------------------------------
    void path_renderer::curve4(double x2, double y2,                   // S, s
                               double x,  double y, bool rel)
    {
        //throw exception("curve4(x2, y2, x, y) : NOT IMPLEMENTED YET");
        if(rel) 
        {
            m_storage.curve4_rel(x2, y2, x, y);
        } else 
        {
            m_storage.curve4(x2, y2, x, y);
        }
    }


	//------------------------------------------------------------------------
	void path_renderer::arc_to(double rx, double ry, double angle, 
		bool large_arc_flag, bool sweep_flag, 
		double x, double y, bool rel)	// A, a
	{
		double rad = deg2rad(angle);
		if(rel) 
		{
			m_storage.arc_rel(rx,ry,rad,large_arc_flag,sweep_flag, x, y);
		}
		else
		{
			m_storage.arc_to(rx,ry,rad,large_arc_flag,sweep_flag, x, y);
		}
	}

    //------------------------------------------------------------------------
    void path_renderer::close_subpath()
    {
        m_storage.end_poly(path_flags_close);
    }

	//------------------------------------------------------------------------
	void path_renderer::font_family(const char* fname)
	{
		strcpy_s( cur_attr().font_family, sizeof(((path_attributes*)0)->font_family), fname );
	}

	//------------------------------------------------------------------------
	void path_renderer::font_size(double fsize)
	{
		cur_attr().font_size = fsize;
	}

	void path_renderer::display_mode(display_e d)
	{
		// 注：父结点的display_none模式需要传递给子结点
		if ( cur_attr().display_mode != display_none )
		{
			cur_attr().display_mode = d;
		}
	}

	//------------------------------------------------------------------------
	void path_renderer::text( double x,  double y, const char* data )
	{
		begin_path();

		path_attributes& attr = cur_attr();
		attr.stroke_flag = true;
		attr.fill_flag = false;
		//attr.text_flag = true;
		attr.text_x = x;
		attr.text_y = y;
		//attr.stroke_color = agg::rgba8(0,0,255,255);
		//attr.text_value.assign( data );

		agg::gsv_text t;

		double logicsize = cur_attr().font_size;
		// MulDiv(PointSize, GetDeviceCaps(hDC, LOGPIXELSY), 72);

		// 96DPI or 120 DPI
		double PointSize = logicsize* 72 / 96;
		t.size(PointSize);
		t.flip(true);
		t.start_point(x, y);
		t.text(data);
		attr.stroke_width = PointSize/10.0;
		m_storage.concat_path( t );

		//agg::conv_stroke<agg::gsv_text> pt(t);
		//pt.width(PointSize/10.0);
		//m_storage.concat_path( pt );

		end_path();
	}

    //------------------------------------------------------------------------
    path_attributes& path_renderer::cur_attr()
    {
        if(m_attr_stack.size() == 0)
        {
            throw exception("cur_attr : Attribute stack is empty");
        }
        
		//return m_attr_stack[m_attr_stack.size() - 1];
		// by yanghaibo
		return m_attr_stack.last();
    }

    //------------------------------------------------------------------------
    void path_renderer::push_attr()
    {
        m_attr_stack.add(m_attr_stack.size() ? 
                         m_attr_stack[m_attr_stack.size() - 1] :
                         path_attributes());
    }

    //------------------------------------------------------------------------
    void path_renderer::pop_attr()
    {
        if(m_attr_stack.size() == 0)
        {
            throw exception("pop_attr : Attribute stack is empty");
        }
        m_attr_stack.remove_last();
    }

	//------------------------------------------------------------------------
	void path_renderer::push_gradient_elemment(svg_gradient_element* element)
	{
		m_gradients.insert(std::make_pair( element->id, element) );
		m_current_gradient = element; // save the last element
	}

	//------------------------------------------------------------------------
	void path_renderer::set_element_id( const char* id )
	{
		path_attributes& attr = cur_attr();
		attr.id = id;
	}

    //------------------------------------------------------------------------
    void path_renderer::fill(const rgba8& f)
    {
        path_attributes& attr = cur_attr();

		double opacity = attr.fill_color.opacity();

		// yanghaibo
		if( opacity < 1.0 )
		{
			rgba8 f2 = f;
			f2.opacity( f.opacity() * opacity ); // combine the opacity
			attr.fill_color = f2;
		}
		else
		{
			attr.fill_color = f;
		}
        attr.fill_flag = true;
    }

    //------------------------------------------------------------------------
    void path_renderer::stroke(const rgba8& s)
    {
        path_attributes& attr = cur_attr();
		double opacity = attr.stroke_color.opacity();

		// yanghaibo
		if( opacity < 1.0 )
		{
			rgba8 s2 = s;
			s2.opacity( s.opacity() * opacity ); // combine the opacity
			attr.stroke_color = s2;
		}
		else
		{
	        attr.stroke_color = s;
		}
        attr.stroke_flag = true;
    }

    //------------------------------------------------------------------------
    void path_renderer::even_odd(bool flag)
    {
        cur_attr().even_odd_flag = flag;
    }
    
    //------------------------------------------------------------------------
    void path_renderer::stroke_width(double w)
    {
        cur_attr().stroke_width = w;
    }

    //------------------------------------------------------------------------
    void path_renderer::fill_none()
    {
        cur_attr().fill_flag = false;
    }

	void path_renderer::gradient_fill(gradient_id grad_id)
	{
		cur_attr().grad_id = grad_id;
	}

    //------------------------------------------------------------------------
    void path_renderer::stroke_none()
    {
        cur_attr().stroke_flag = false;
    }

    //------------------------------------------------------------------------
    void path_renderer::fill_opacity(double op)
    {
        cur_attr().fill_color.opacity(op);
    }

    //------------------------------------------------------------------------
    void path_renderer::stroke_opacity(double op)
    {
        cur_attr().stroke_color.opacity(op);
    }

    //------------------------------------------------------------------------
    void path_renderer::line_join(line_join_e join)
    {
        cur_attr().line_join = join;
    }

    //------------------------------------------------------------------------
    void path_renderer::line_cap(line_cap_e cap)
    {
        cur_attr().line_cap = cap;
    }

    //------------------------------------------------------------------------
    void path_renderer::miter_limit(double ml)
    {
        cur_attr().miter_limit = ml;
    }

    //------------------------------------------------------------------------
    trans_affine& path_renderer::transform()
    {
        return cur_attr().transform;
    }

    //------------------------------------------------------------------------
	void path_renderer::parse_path(path_tokenizer& tok)
	{
		while(tok.next())
		{
			double arg[10];
			char cmd = tok.last_command();
			unsigned i;
			switch(cmd)
			{
			case 'M': case 'm':
				arg[0] = tok.last_number();
				arg[1] = tok.next(cmd);
				move_to(arg[0], arg[1], cmd == 'm');
				break;

			case 'L': case 'l':
				arg[0] = tok.last_number();
				arg[1] = tok.next(cmd);
				line_to(arg[0], arg[1], cmd == 'l');
				break;

			case 'V': case 'v':
				vline_to(tok.last_number(), cmd == 'v');
				break;

			case 'H': case 'h':
				hline_to(tok.last_number(), cmd == 'h');
				break;

			case 'Q': case 'q':
				arg[0] = tok.last_number();
				for(i = 1; i < 4; i++)
				{
					arg[i] = tok.next(cmd);
				}
				curve3(arg[0], arg[1], arg[2], arg[3], cmd == 'q');
				break;

			case 'T': case 't':
				arg[0] = tok.last_number();
				arg[1] = tok.next(cmd);
				curve3(arg[0], arg[1], cmd == 't');
				break;

			case 'C': case 'c':
				arg[0] = tok.last_number();
				for(i = 1; i < 6; i++)
				{
					arg[i] = tok.next(cmd);
				}
				curve4(arg[0], arg[1], arg[2], arg[3], arg[4], arg[5], cmd == 'c');
				break;

			case 'S': case 's':
				arg[0] = tok.last_number();
				for(i = 1; i < 4; i++)
				{
					arg[i] = tok.next(cmd);
				}
				curve4(arg[0], arg[1], arg[2], arg[3], cmd == 's');
				break;

			case 'A': case 'a':
				arg[0] = tok.last_number();
				for(i = 1; i < 7; i++)
				{
					arg[i] = tok.next(cmd);
				}
				arc_to( arg[0], arg[1], arg[2], (bool)(arg[3]!=0), (bool)(arg[4]!=0), arg[5], arg[6], cmd == 'a');
				break;
			case 'Z': case 'z':
				{
					close_subpath();
				}
				break;

			default:
				{
					char buf[100];
					sprintf_s(buf, 100, "parse_path: Invalid Command %c", cmd);
					throw exception(buf);
				}
			}
		}
	}

	int path_renderer::hit_test_in_box(const trans_affine& mtx, int x, int y, rect_i clipbox)
	{
		// check whether the point(x,y) is in the clipbox.
		if ( !clipbox.hit_test(x, y) )
		{
			return -1;
		}

		return hit_test(mtx, x, y);
	}

	int path_renderer::hit_test(const trans_affine& mtx, int x, int y)
	{
		// use the no-clipped rasterizer
		agg::rasterizer_scanline_aa<agg::rasterizer_sl_no_clip> ras;
		
		// use m_curved directly but not use m_curved_count
		agg::conv_transform<curved> curved_trans(m_curved, m_transform);
		agg::conv_stroke<curved>	curved_stroke(m_curved);
		agg::conv_transform<agg::conv_stroke<curved>> curved_stroked_trans(curved_stroke, m_transform);

		int i = m_attr_storage.size()-1;
		for(; i >=0 ; --i )
		{
			const path_attributes& attr = m_attr_storage[i];
			if ( attr.defs_flag || attr.pattern_flag)
			{
				continue;
			}

			if(attr.display_mode == svg::display_none )
			{
				continue;
			}

			m_transform = attr.transform;
			m_transform *= mtx;
			m_transform *= attr.transform_adjusting;

			double scl = m_transform.scale();

			m_curved.approximation_scale(scl);
			m_curved.angle_tolerance(0.0);
	
			ras.reset();

			// take grad fill same as a solid fill 
			if(attr.fill_flag || !attr.grad_id.empty() )
			{
				ras.filling_rule(attr.even_odd_flag ? fill_even_odd : fill_non_zero);
				ras.add_path(curved_trans, attr.index);
				if( ras.hit_test( x, y ) )
				{
					return i;
				}
			}

			if(attr.stroke_flag)
			{
				curved_stroke.width(attr.stroke_width);
				curved_stroke.line_join(attr.line_join);
				curved_stroke.line_cap(attr.line_cap);
				curved_stroke.miter_limit(attr.miter_limit);
				curved_stroke.inner_join(inner_round);
				curved_stroke.approximation_scale(scl);

				// If the *visual* line width is considerable we 
				// turn on processing of curve cusps.
				//---------------------
				if(attr.stroke_width * scl > 1.0)
				{
					m_curved.angle_tolerance(0.2);
				}
				
				ras.filling_rule(fill_non_zero);
				
				// if we can hit stroke path, we can use the following method to replace above one
				ras.add_path(m_curved_trans, attr.index);

				/*if( ras.hit_test( x, y ) )
				{
					return i;
				}*/

				ras.add_path(curved_stroked_trans, attr.index);

				// if we can hit stroke path, we can use the following method to replace above one
				//ras.add_path(m_curved_trans, attr.index);
				
				if( ras.hit_test( x, y ) )
				{
					return i;
				}

			}
		}

		return -1;
	}

	// retrieve the bounding box for the path by the path_id
	bool path_renderer::path_bounding_box(
		const trans_affine& mtx, int path_id, rect_d& bounding_box)
	{
		bounding_box.init(0,0,-1,-1);

		if ( path_id < 0 || (size_t)path_id > m_attr_storage.size() )
		{
			return false;
		}

		const path_attributes& attr = m_attr_storage[path_id];
		if ( attr.defs_flag || attr.pattern_flag)
		{
			return false;
		}

		if(attr.display_mode == svg::display_none )
		{
			return false;
		}

		m_transform = attr.transform;
		m_transform *= mtx;
		m_transform *= attr.transform_adjusting;
		
		if( attr.stroke_flag )
		{
			agg::conv_stroke<curved>	curved_stroke(m_curved);
			agg::conv_transform<agg::conv_stroke<curved>> curved_stroked_trans(curved_stroke, m_transform);

			curved_stroke.width(attr.stroke_width);
			curved_stroke.line_join(attr.line_join);
			curved_stroke.line_cap(attr.line_cap);
			curved_stroke.miter_limit(attr.miter_limit);
			curved_stroke.inner_join(inner_round);
			double scl = m_transform.scale();
			curved_stroke.approximation_scale(scl);

			// If the *visual* line width is considerable we 
			// turn on processing of curve cusps.
			//---------------------
			if(attr.stroke_width * scl > 1.0)
			{
				m_curved.angle_tolerance(0.2);
			}

			return bounding_rect_single(curved_stroked_trans, attr.index,
				&bounding_box.x1, &bounding_box.y1,
				&bounding_box.x2, &bounding_box.y2);
		}
		
		if(attr.fill_flag || !attr.grad_id.empty() )
		{
			agg::conv_transform<curved> curved_trans(m_curved, m_transform);

			return bounding_rect_single(curved_trans, attr.index,
				&bounding_box.x1, &bounding_box.y1,
				&bounding_box.x2, &bounding_box.y2);
		}

		return false;
	}

}
}

