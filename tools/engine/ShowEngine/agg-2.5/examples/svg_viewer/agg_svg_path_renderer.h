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
#ifndef AGG_SVG_PATH_RENDERER_INCLUDED
#define AGG_SVG_PATH_RENDERER_INCLUDED

#include "agg_path_storage.h"
#include "agg_conv_transform.h"
#include "agg_conv_stroke.h"
#include "agg_conv_contour.h"
#include "agg_conv_curve.h"
#include "agg_color_rgba.h"
#include "agg_renderer_scanline.h"
#include "agg_bounding_rect.h"
#include "agg_rasterizer_scanline_aa.h"
#include "agg_svg_path_tokenizer.h"
#include "agg_rasterizer_compound_aa.h"
#include "agg_scanline_u.h"
#include "agg_span_gradient.h"
#include "agg_rounded_rect.h"
#include "agg_span_interpolator_linear.h"
#include "agg_span_allocator.h"

#include <string>
#include <map>
#include <vector>
#include "svg_basic.h"

typedef agg::pixfmt_bgr24 pixfmt_type;
typedef pixfmt_type::color_type color_type;
namespace agg
{



	namespace svg
	{
		//Initial:   inline 
		//Applies to:   'svg', 'g', 'switch', 'a', 'foreignObject', 
		//		graphics elements (including the 'text' element) and 
		//		text sub-elements (i.e., 'tspan', 'tref', 'altGlyph', 'textPath') 
		//Inherited:   no 
		enum display_e
		{
			display_none,
			display_inline,
			display_block,
			//display_list_item,
			//display_run_in,
			//display_compact,
			//display_marker,
			//display_table,
			//display_inline_table,
			//display_table_row_group,
			//display_table_header_group,
			//display_table_footer_group ,
			//display_table_row , 
			//display_table_column_group , 
			//display_table_column ,
			//display_table_cell ,
			//display_table_caption,	
			display_inherit,
		};

		class gradient_polymorphic_wrapper_base
		{
		public:
			virtual int calculate(int x, int y, int) const = 0;
		};

		template<class Adaptor> 
		class gradient_polymorphic_wrapper: public gradient_polymorphic_wrapper_base
		{
		public:
			typedef typename Adaptor::GradientT GradientF;

		public:
			gradient_polymorphic_wrapper() : m_adaptor(m_gradient) {}

			virtual int calculate(int x, int y, int d) const
			{
				return m_adaptor.calculate(x, y, d);
			}

			GradientF m_gradient;

			// refer to:
			// http://www.w3.org/TR/SVG11/pservers.html#LinearGradientSpreadMethodAttribute
			// spreadMethod = "pad | reflect | repeat"
			Adaptor m_adaptor; // pad is default
		};

		template<class VertexSource> class conv_count
		{
		public:
			conv_count(VertexSource& vs) : m_source(&vs), m_count(0), m_count_saved(0) {}

			void count(unsigned n) { m_count = n; m_count_saved = m_count; }
			unsigned count() const { return m_count; }

			void mark(){ m_count_saved = m_count; }
			void restore() { m_count = m_count_saved; }

			void rewind(unsigned path_id) { m_source->rewind(path_id); }
			unsigned vertex(double* x, double* y) 
			{ 
				++m_count; 
				return m_source->vertex(x, y); 
			}

		private:
			VertexSource* m_source;
			unsigned m_count;
			unsigned m_count_saved;
		};

		class span_gradient_opacity_converter
		{
		public:
			span_gradient_opacity_converter(double opacity)
			{
				m_opacity = opacity;
			}
			//--------------------------------------------------------------------
			void prepare() 
			{ 
			}

			//--------------------------------------------------------------------
			void generate(color_type* span, int x, int y, unsigned len)
			{
				for ( unsigned i = 0; i < len; i ++ )
				{
					span[i].opacity(span[i].opacity() * m_opacity);
				}
			}

		public:
			double m_opacity;
		};
		


		//----------------------------------------------------------span_converter
		template<class SpanGenerator, class SpanConverter> class span_converter
		{
		public:
			typedef typename SpanGenerator::color_type color_type;

			span_converter(SpanGenerator& span_gen, SpanConverter& span_cnv) : 
			m_span_gen(&span_gen), m_span_cnv(&span_cnv) {}

			void attach_generator(SpanGenerator& span_gen) { m_span_gen = &span_gen; }
			void attach_converter(SpanConverter& span_cnv) { m_span_cnv = &span_cnv; }

			//--------------------------------------------------------------------
			void prepare() 
			{ 
				m_span_gen->prepare(); 
				m_span_cnv->prepare();
			}

			//--------------------------------------------------------------------
			void generate(color_type* span, int x, int y, unsigned len)
			{
				m_span_gen->generate(span, x, y, len);
				m_span_cnv->generate(span, x, y, len);
			}

		private:
			SpanGenerator* m_span_gen;
			SpanConverter* m_span_cnv;
		};
		
		//============================================================================
		// Basic path attributes
		struct path_attributes
		{
			unsigned     index;
			rgba8        fill_color;
			rgba8        stroke_color;
			bool         fill_flag;
			bool		 text_flag;		// this path is a text element
			unsigned     gradient_flag; // 0, gradient_none, 1, gradient_linear, 2, gradient_radial
			element_id	 id;			// if the element is a definition or a pattern 
			gradient_id  grad_id;		// url(#SVGID_%d_)
			bool         stroke_flag;
			bool         even_odd_flag;
			bool		 pattern_flag;
			bool		 defs_flag;

			line_join_e  line_join;
			line_cap_e   line_cap;
			double       miter_limit;
			double       stroke_width;
			trans_affine transform;
			trans_affine transform_adjusting;
			char		font_family[256];
			double       font_size;
			display_e	display_mode;
			text_data	text_value; // valid when text_flag is true.
			double		text_x;
			double		text_y;

			// Empty constructor
			path_attributes() :
				index(0),
				fill_color(rgba(0,0,0)),
				stroke_color(rgba(0,0,0)),
				fill_flag(true), // 根据svg规范，fill属性缺省为black
				//fill_flag(false),
				text_flag(false),
				gradient_flag(0),
				stroke_flag(false), // 根据svg规范，stroke属性缺省为none
				even_odd_flag(false),
				pattern_flag(false),
				defs_flag(false),
				line_join(miter_join),
				line_cap(butt_cap),
				miter_limit(4.0),
				stroke_width(1.0),
				font_size(10),
				display_mode(display_inline),
				transform(),
				transform_adjusting(),
				id(),
				grad_id(),
				text_value(),
				text_x(0),
				text_y(0)
			{
				font_family[0] = 0;
			}

			// Copy constructor
			path_attributes(const path_attributes& attr) :
				index(attr.index),
				fill_color(attr.fill_color),
				stroke_color(attr.stroke_color),
				fill_flag(attr.fill_flag),
				text_flag(attr.text_flag),
				gradient_flag(attr.gradient_flag),
				grad_id(attr.grad_id),
				id(attr.id),
				stroke_flag(attr.stroke_flag),
				even_odd_flag(attr.even_odd_flag),
				pattern_flag(attr.pattern_flag),
				defs_flag(attr.defs_flag),
				line_join(attr.line_join),
				line_cap(attr.line_cap),
				miter_limit(attr.miter_limit),
				stroke_width(attr.stroke_width),
				font_size(attr.font_size),
				display_mode(attr.display_mode),
				transform(attr.transform),
				transform_adjusting(attr.transform_adjusting),
				text_value(attr.text_value),
				text_x(attr.text_x),
				text_y(attr.text_y)
			{
				memcpy( font_family, attr.font_family, sizeof(font_family) );
			}

			// Copy constructor with new index value
			path_attributes(const path_attributes& attr, unsigned idx) :
				index(idx),
				fill_color(attr.fill_color),
				stroke_color(attr.stroke_color),
				fill_flag(attr.fill_flag),
				text_flag(attr.text_flag),
				gradient_flag(attr.gradient_flag),
				grad_id(attr.grad_id),
				id(attr.id),
				stroke_flag(attr.stroke_flag),
				even_odd_flag(attr.even_odd_flag),
				pattern_flag(attr.pattern_flag),
				defs_flag(attr.defs_flag),
				line_join(attr.line_join),
				line_cap(attr.line_cap),
				miter_limit(attr.miter_limit),
				stroke_width(attr.stroke_width),
				font_size(attr.font_size),
				display_mode(attr.display_mode),
				transform(attr.transform),
				transform_adjusting(attr.transform_adjusting),
				text_value(attr.text_value),
				text_x(attr.text_x),
				text_y(attr.text_y)
			{
				memcpy( font_family, attr.font_family, sizeof(font_family) );
			}

			bool zoom( double cx, double cy, double scale )
			{
				transform_adjusting *= agg::trans_affine_translation(-cx,-cy);
				transform_adjusting *= agg::trans_affine_scaling(scale);
				transform_adjusting *= agg::trans_affine_translation(cx,cy);

				return true;
			}

			bool zoom_in( double cx, double cy )
			{
				zoom( cx, cy, 2.0 );
				return true;
			}

			bool zoom_out( double cx, double cy )
			{
				zoom( cx, cy, 0.5 );
				return true;
			}

			bool move( double tx, double ty ) 
			{
				transform_adjusting.translate(tx, ty);
				return true;
			}

			bool rotate( double cx, double cy, double rad )
			{
				transform_adjusting *= agg::trans_affine_translation(-cx,-cy);
				transform_adjusting *= agg::trans_affine_rotation(rad);
				transform_adjusting *= agg::trans_affine_translation(cx,cy);
				return true;
			}

			bool rotate_cw( double cx, double cy )
			{
				rotate( cx, cy, agg::deg2rad(5) );
				return true;
			}

			bool rotate_ccw( double cx, double cy )
			{
				rotate( cx, cy, agg::deg2rad(-5) );
				return true;
			}
		};
		

		//-----------------------------------------------------------bounding_rect
		template<class VertexSource, class GetId, class Display, class CoordT>
		bool svg_bounding_rect(VertexSource& vs, GetId& gi, Display& display,
			unsigned start, unsigned num, 
			CoordT* x1, CoordT* y1, CoordT* x2, CoordT* y2)
		{
			unsigned i;
			double x;
			double y;
			bool first = true;

			*x1 = CoordT(1);
			*y1 = CoordT(1);
			*x2 = CoordT(0);
			*y2 = CoordT(0);

			for(i = 0; i < num; i++)
			{
				if( !display(start + i) )
				{
					continue;
				}

				vs.rewind(gi[start + i]);
				unsigned cmd;
				while(!is_stop(cmd = vs.vertex(&x, &y)))
				{
					if(is_vertex(cmd))
					{
						if(first)
						{
							*x1 = CoordT(x);
							*y1 = CoordT(y);
							*x2 = CoordT(x);
							*y2 = CoordT(y);
							first = false;
						}
						else
						{
							if(CoordT(x) < *x1) *x1 = CoordT(x);
							if(CoordT(y) < *y1) *y1 = CoordT(y);
							if(CoordT(x) > *x2) *x2 = CoordT(x);
							if(CoordT(y) > *y2) *y2 = CoordT(y);
						}
					}
				}
			}
			return *x1 <= *x2 && *y1 <= *y2;
		}

		//============================================================================
		// Path container and renderer. 
		class path_renderer
		{
		public:
			typedef pod_bvector<path_attributes>   attr_storage;

			typedef conv_curve<path_storage>       curved;
			typedef conv_count<curved>             curved_count;

			typedef conv_stroke<curved_count>      curved_stroked;
			typedef conv_transform<curved_stroked> curved_stroked_trans;

			typedef conv_transform<curved_count>   curved_trans;
			typedef conv_contour<curved_trans>     curved_trans_contour;

			path_renderer();
			~path_renderer();

			agg::trans_affine& path_transform( int path_id )
			{
				path_attributes& attr = m_attr_storage[path_id];
				return attr.transform_adjusting;
			}

			void remove_all();

			// Use these functions as follows:
			// begin_path() when the XML tag <path> comes ("start_element" handler)
			// parse_path() on "d=" tag attribute
			// end_path() when parsing of the entire tag is done.
			void begin_path();
			void parse_path(path_tokenizer& tok);
			void end_path();

			// a circle element
			void circle(double cx, double cy, double r);

			// an ellipse element
			void ellipse_(double cx, double cy, double rx, double ry);

			//void ell_arc(double cx, double cy, double rx, double ry, double start_angle, double sweep_angle);
			void ell_arc_to(double x, double y, bool rel=false);

			// The following functions are essentially a "reflection" of
			// the respective SVG path commands.
			void move_to(double x, double y, bool rel=false);   // M, m
			void line_to(double x,  double y, bool rel=false);  // L, l
			void hline_to(double x, bool rel=false);            // H, h
			void vline_to(double y, bool rel=false);            // V, v
			void curve3(double x1, double y1,                   // Q, q
						double x,  double y, bool rel=false);
			void curve3(double x, double y, bool rel=false);    // T, t
			void curve4(double x1, double y1,                   // C, c
						double x2, double y2, 
						double x,  double y, bool rel=false);
			void curve4(double x2, double y2,                   // S, s
						double x,  double y, bool rel=false);
			void arc_to(double rx, double ry, double angle, 
						bool large_arc_flag, bool sweep_flag, 
						double x, double y, bool rel=false);	// A, a
			void close_subpath();                               // Z, z

			void text( double x,  double y, const char* data );

	//        template<class VertexSource> 
	//        void add_path(VertexSource& vs, 
	//                      unsigned path_id = 0, 
	//                      bool solid_path = true)
	//        {
	//            m_storage.add_path(vs, path_id, solid_path);
	//        }


			unsigned vertex_count() const { return m_curved_count.count(); }
	        

			// Call these functions on <g> tag (start_element, end_element respectively)
			void push_attr();
			void pop_attr();

			void push_gradient_elemment(svg_gradient_element* element);
			svg_gradient_element* curr_gradient_element() { return m_current_gradient; }

			// Attribute setting functions.
			void set_element_id( const char* id );
			void fill(const rgba8& f);
			void stroke(const rgba8& s);
			void even_odd(bool flag);
			void stroke_width(double w);
			void begin_pattern();
			void end_pattern();
			void begin_defs();
			void end_defs();
			void fill_none();
			void stroke_none();
			void gradient_fill(gradient_id grad_id);
			void fill_opacity(double op);
			void stroke_opacity(double op);
			void line_join(line_join_e join);
			void line_cap(line_cap_e cap);
			void miter_limit(double ml);
			trans_affine& transform();

			void font_family(const char* fname);
			void font_size(double fsize);

			void display_mode(display_e d);

			// Make all polygons CCW-oriented
			void arrange_orientations()
			{
				m_storage.arrange_orientations_all_paths(path_flags_ccw);
			}

			// Expand all polygons 
			void expand(double value)
			{
				m_curved_trans_contour.width(value);
			}

			unsigned operator [](unsigned idx)
			{
				m_transform = m_attr_storage[idx].transform;
				return m_attr_storage[idx].index;
			}

			struct Display
			{
				attr_storage& m_attr_storage;
				Display(attr_storage& attr)
					: m_attr_storage(attr)
				{
				}
				bool operator () (unsigned idx )
				{
					const path_attributes& attr = m_attr_storage[idx];
					if ( attr.defs_flag || attr.pattern_flag || attr.display_mode == display_none )
					{
						return false;
					}

					return true;
				}
			};

			bool bounding_rect(double* x1, double* y1, double* x2, double* y2)
			{
				agg::conv_transform<agg::path_storage> trans(m_storage, m_transform);
				return svg_bounding_rect(trans, *this, Display(m_attr_storage), 0, m_attr_storage.size(), x1, y1, x2, y2);
			}

			bool bounding_rect(double* x1, double* y1, double* x2, double* y2, const trans_affine& mtx)
			{
				agg::conv_transform<agg::path_storage> trans(m_storage, m_transform);
				agg::conv_transform<agg::conv_transform<agg::path_storage>> trans_final(trans, mtx);
				return svg_bounding_rect(trans_final, *this, Display(m_attr_storage), 0, m_attr_storage.size(), x1, y1, x2, y2);
			}

// 下面的编译指令用于解决模板函数修改后编译器生成的代码没有更新的问题
#pragma component( minrebuild, off )

			// Rendering. One can specify two additional parameters: 
			// trans_affine and opacity. They can be used to transform the whole
			// image and/or to make it translucent.
			template<class Rasterizer, class Scanline, class Renderer> 
			void render(Rasterizer& ras, 
						Scanline& sl,
						Renderer& ren, 
						const trans_affine& mtx, 
						const rect_i& cb,
						double opacity=1.0)
			{
				unsigned i;

				ras.clip_box(cb.x1, cb.y1, cb.x2, cb.y2);
				m_curved_count.count(0);

				for(i = 0; i < m_attr_storage.size(); i++)
				{
					const path_attributes& attr = m_attr_storage[i];
					m_transform = attr.transform;
					m_transform *= mtx;
					double scl = m_transform.scale();
					//m_curved.approximation_method(curve_inc);
					m_curved.approximation_scale(scl);
					m_curved.angle_tolerance(0.0);

					rgba8 color;

					if(attr.fill_flag)
					{
						ras.reset();
						ras.filling_rule(attr.even_odd_flag ? fill_even_odd : fill_non_zero);
						if(fabs(m_curved_trans_contour.width()) < 0.0001)
						{
							ras.add_path(m_curved_trans, attr.index);
						}
						else
						{
							m_curved_trans_contour.miter_limit(attr.miter_limit);
							ras.add_path(m_curved_trans_contour, attr.index);
						}

						color = attr.fill_color;
						color.opacity(color.opacity() * opacity);
						ren.color(color);
						agg::render_scanlines(ras, sl, ren);
					}

					if(attr.stroke_flag)
					{
						m_curved_stroked.width(attr.stroke_width);
						//m_curved_stroked.line_join((attr.line_join == miter_join) ? miter_join_round : attr.line_join);
						m_curved_stroked.line_join(attr.line_join);
						m_curved_stroked.line_cap(attr.line_cap);
						m_curved_stroked.miter_limit(attr.miter_limit);
						m_curved_stroked.inner_join(inner_round);
						m_curved_stroked.approximation_scale(scl);

						// If the *visual* line width is considerable we 
						// turn on processing of curve cusps.
						//---------------------
						if(attr.stroke_width * scl > 1.0)
						{
							m_curved.angle_tolerance(0.2);
						}
						ras.reset();
						ras.filling_rule(fill_non_zero);
						ras.add_path(m_curved_stroked_trans, attr.index);
						color = attr.stroke_color;
						color.opacity(color.opacity() * opacity);
						ren.color(color);
						agg::render_scanlines(ras, sl, ren);
					}
				}
			}

			template<class Rasterizer, class BaseRenderer> 
			void render_text(
				Rasterizer& ras,
				BaseRenderer& ren_base,
				const path_attributes& attr)
			{
				// TODO: 渲染文字的功能需继续研究
				ras.reset();
				agg::gsv_text t;

				double logicsize = attr.font_size;
				//// MulDiv(PointSize, GetDeviceCaps(hDC, LOGPIXELSY), 72);

				// 96DPI or 120 DPI
				double PointSize =logicsize* 72 / 96;
				t.size(PointSize);
				t.flip(true);
				t.start_point(attr.text_x, attr.text_y);
				t.text(attr.text_value.c_str());
				agg::gsv_text_outline<> txt_outline( t, m_transform);
				//typedef agg::conv_stroke<agg::gsv_text> conv_stroked_text;
				//typedef agg::conv_transform<conv_stroked_text> curved_stroked_trans_text;
				//conv_stroked_text pt(t);
				//pt.width(PointSize/10.0);
				//curved_stroked_trans_text cst_text(pt, m_transform);
				//ras.add_path( cst_text );
				txt_outline.width(PointSize/10.0);
				ras.add_path( txt_outline );
				agg::scanline_u8 sl_u;
				agg::render_scanlines_aa_solid(ras, sl_u, ren_base, attr.stroke_color ); 
			}

			template<class Rasterizer, class BaseRenderer> 
			void render_gradient_element(
				Rasterizer& ras,
				BaseRenderer& ren_base,
				const rect_d& bounding_box,
				double scl,
				const path_attributes& attr)
			{
				agg::scanline_u8 sl_u;
				rect_i cb = ren_base.clip_box();

				svg_gradient_element* gradient_elem = NULL;
				svg_gradient_elements::const_iterator itr =  m_gradients.find(attr.grad_id);
				if ( itr != m_gradients.end() )
				{
					gradient_elem = (*itr).second;
				}

				if ( gradient_elem == NULL )
				{
					// an exception!
					return;
				}

				//ras.reset();
				ras.filling_rule(attr.even_odd_flag ? fill_even_odd : fill_non_zero);
				if(fabs(m_curved_trans_contour.width()) < 0.0001)
				{
					ras.add_path(m_curved_trans, attr.index);
				}
				else
				{
					m_curved_trans_contour.miter_limit(attr.miter_limit);
					ras.add_path(m_curved_trans_contour, attr.index);
				}

				typedef agg::span_interpolator_linear_subdiv<> interpolator_type;
				typedef agg::span_allocator<color_type> span_allocator_type;
				typedef agg::pod_array<color_type> gradient_colors_type;

				trans_affine mtx2;

				double d1 = 0, d2 = 1;
				int color_arry_size = 1;

				span_allocator_type      span_allocator;  
				gradient_colors_type     color_array;


				// gradient_radial_focus
				gradient_polymorphic_wrapper<gradient_pad_adaptor<agg::gradient_radial_focus>>		gr_radial_pad;
				gradient_polymorphic_wrapper<gradient_reflect_adaptor<agg::gradient_radial_focus>>	gr_radial_reflect;
				gradient_polymorphic_wrapper<gradient_repeat_adaptor<agg::gradient_radial_focus>>	gr_radial_repeat;

				// TODO: gradient_ellipse
				gradient_polymorphic_wrapper<gradient_pad_adaptor<agg::gradient_ellipse>>			gr_ellipse_pad;
				gradient_polymorphic_wrapper<gradient_reflect_adaptor<agg::gradient_ellipse>>		gr_ellipse_reflect;
				gradient_polymorphic_wrapper<gradient_repeat_adaptor<agg::gradient_ellipse>>		gr_ellipse_repeat;

				gradient_polymorphic_wrapper<gradient_pad_adaptor<agg::gradient_x>>					gr_linear_pad;
				gradient_polymorphic_wrapper<gradient_reflect_adaptor<agg::gradient_x>>				gr_linear_reflect;
				gradient_polymorphic_wrapper<gradient_repeat_adaptor<agg::gradient_x>>				gr_linear_repeat;

				gradient_polymorphic_wrapper_base* gr_ptr = NULL;
				gradient_polymorphic_wrapper_base* gr_radial = NULL;
				gradient_polymorphic_wrapper_base* gr_linear = NULL;

				if( gradient_elem->type == gt_gradient_radial )
				{
					svg_gradient_radial* radial = (svg_gradient_radial*)gradient_elem;

					if( radial->units == userSpaceOnUse )
					{
						if( radial->r == 0.5 )
						{
							radial->r = labs(cb.x2 + cb.x1) * 0.5;
						}

						if ( radial->cx == 0.5 && radial->cy == 0.5 ) // 精确
						{
							radial->cx = (cb.x2 + cb.x1)*0.5;
							radial->cy = (cb.y2 + cb.y1)*0.5;
						}

						if ( radial->fx == 0.5 && radial->fy == 0.5 ) // 精确
						{
							radial->fx = (cb.x2 + cb.x1)*0.5;
							radial->fy = (cb.y2 + cb.y1)*0.5;
						}

						// if r == 0, this is an empty gradient fill element
						d2 = (int)radial->r;

						if ( gradient_elem->spread == pad )
						{
							gr_radial = &gr_radial_pad;
							gr_radial_pad.m_gradient.init( radial->r, radial->fx-radial->cx, radial->fy-radial->cy );

						}
						else if ( gradient_elem->spread == reflect  )
						{
							gr_radial = &gr_radial_reflect;
							gr_radial_reflect.m_gradient.init( radial->r, radial->fx-radial->cx, radial->fy-radial->cy );
						}
						else
						{
							gr_radial = &gr_radial_repeat;
							gr_radial_repeat.m_gradient.init( radial->r, radial->fx-radial->cx, radial->fy-radial->cy );
						}

						mtx2 *= agg::trans_affine_translation(radial->cx, radial->cy);
					}
					else // default objectBoundingBox - default
					{
						// TODO:
						double rx = (bounding_box.x2 - bounding_box.x1)*0.5;
						double ry = (bounding_box.y2 - bounding_box.y1)*0.5;
						double fx = (bounding_box.x2 + bounding_box.x1)*0.5;
						double fy = (bounding_box.y2 + bounding_box.y1)*0.5;

						if ( rx > ry )
						{
							d2 = rx;
						}
						else
						{
							d2 = ry;
						}

						if ( gradient_elem->spread == pad )
						{
							gr_radial = &gr_ellipse_pad;
							gr_ellipse_pad.m_gradient.init( rx, ry, fx, fy );

						}
						else if ( gradient_elem->spread == reflect  )
						{
							gr_radial = &gr_ellipse_reflect;
							gr_ellipse_reflect.m_gradient.init( rx, ry, fx, fy  );
						}
						else
						{
							gr_radial = &gr_ellipse_repeat;
							gr_ellipse_repeat.m_gradient.init( rx, ry, fx, fy  );
						}

						mtx2 *= agg::trans_affine_translation(fx, fy);
					}

					gr_ptr = gr_radial;

				}
				else if (gradient_elem->type == gt_gradient_linear)
				{
					if ( gradient_elem->spread == pad )
					{
						gr_linear = &gr_linear_pad;
					}
					else if ( gradient_elem->spread == reflect  )
					{
						gr_linear = &gr_linear_reflect;
					}
					else
					{
						gr_linear = &gr_linear_repeat;
					}

					svg_gradient_linear* linear = (svg_gradient_linear*)gradient_elem;
					if( linear->units == userSpaceOnUse )
					{
						if ( linear->x2 == 1.0 && linear->x1 == 0.0 &&
							linear->y2 == 0.0 && linear->y1 == 0.0 )
						{
							linear->x1 = cb.x1;
							linear->x2 = cb.x2;
							d2 = labs(cb.x2 - cb.x1)+1;
						}
						else
						{
							if ( linear->x2 == 0.0 && linear->x1 == 0.0 )
							{
								d2 = labs(cb.y2 - cb.y1)+1;
							}
							else
							{
								/*
								double s = atan2( linear->y2-linear->y1, linear->x2 - linear->x1 );
								mtx2*=agg::trans_affine_rotation(s);
								mtx2*=agg::trans_affine_translation(linear->x1,linear->y1);
								*/

								// same as above
								mtx2*=trans_affine_line_segment(linear->x1,linear->y1,linear->x2,linear->y2, 0.0);

								d2 = calc_distance(linear->x1,linear->y1, linear->x2, linear->y2);
							}
						}
					}
					else // objectBoundingBox - default
					{
						// just now, we don't fully support it yet
						//
						// they are fractions or percentages of the bounding box of the element

						mtx2 *= agg::trans_affine_translation(bounding_box.x1, 0/*bounding_box.y1*/);

						double width = (bounding_box.x2 - bounding_box.x1);
						d2 = width * (linear->x2-linear->x1);
					}

					// empty gradient fill element
					if ( linear->x2 == linear->x1 && linear->y2 == linear->y1 )
					{
						d2 = 0;
					}

					gr_ptr = gr_linear;
				}

				if ( gr_ptr == NULL )
				{
					return;
				}

				mtx2 *= gradient_elem->trans;
				mtx2 *= m_transform;
				mtx2.invert();
				color_arry_size = (int)(d2 * scl + 0.5);

				if ( d2 == 0 )
				{
					d2 = 1;
				}

				interpolator_type	span_interpolator(mtx2);

				typedef gradient_polymorphic_wrapper_base gradient_func_type;   
				gradient_func_type&	gradient_func = *gr_ptr;

				if ( color_arry_size == 0 )
				{
					color_array.resize(1);
					fill_color_array_by_last_stop(color_array, gradient_elem->stops,
						gradient_elem->stops.size(), color_array.size() );
				}
				else
				{
					color_array.resize(color_arry_size);
					fill_color_array_by_stops_array(color_array, gradient_elem->stops,
						gradient_elem->stops.size(), color_array.size() );
				}

				typedef agg::span_gradient<color_type, 
					interpolator_type, 
					gradient_func_type, 
					gradient_colors_type> span_gradient_type;

				span_gradient_type span_gen(span_interpolator, 
					gradient_func, color_array,  d1, d2);

				agg::rgba8 color = attr.fill_color;
				span_allocator_type alloc;
				if( color.opacity() < 1.0 )
				{
					span_gradient_opacity_converter opacity_conv( color.opacity() );
					span_converter<span_gradient_type, span_gradient_opacity_converter> 
						span_gen_conv(span_gen, opacity_conv);

					agg::render_scanlines_aa(ras, sl_u, ren_base, alloc, span_gen_conv); 
				}
				else
				{	
					agg::render_scanlines_aa(ras, sl_u, ren_base, alloc, span_gen); 
				}
			}

			template<class Rasterizer, class BaseRenderer> 
			void render2(
				Rasterizer& ras,
				BaseRenderer& ren_base,
				const trans_affine& mtx, 
				int active_path_id = -1,
				bool draw_bounding_box = false)
			{
				typedef agg::renderer_scanline_aa_solid<BaseRenderer> renderer_aa_solid;
				//typedef agg::renderer_scanline_bin_solid<BaseRenderer> renderer_bin_solid;
				renderer_aa_solid ren_solid(ren_base);
				rect_i cb = ren_base.clip_box();
				double opacity = 1.0;

				// 根据AGG 的文档，对不同的图形元素使用不同的scanline方法，可稍微提高渲染速度
				agg::scanline_p8 sl_p;
				ras.clip_box(cb.x1, cb.y1, cb.x2+1, cb.y2+1);
				m_curved_count.count(0);
			
				unsigned i;
				for(i = 0; i < m_attr_storage.size(); i++)
				{
					const path_attributes& attr = m_attr_storage[i];
					if ( attr.defs_flag || attr.pattern_flag)
					{
						continue;
					}

					if ( attr.display_mode == display_none )
					{
						// 注：为了便于美术制作并测试svg，现在暂时忽略display属性的处理
						// 按照svg规范，对于display为none的结点需要放弃渲染
						// 下面的continue暂时注释掉.
						// [1/15/2009 yanghaibo]
						//
						// don't render it
						//continue;
					}

					m_transform = attr.transform;
					if( active_path_id < 0 )
					{
						m_transform *= attr.transform_adjusting;
					}
					m_transform *= mtx;
					if( active_path_id >= 0 )
					{
						m_transform *= attr.transform_adjusting;
					}
					
					rect_d bounding_box;
					if( !bounding_rect_single(m_curved, attr.index,
						&bounding_box.x1, &bounding_box.y1,
						&bounding_box.x2, &bounding_box.y2) )
					{
						bounding_box.x1 = ras.min_x();
						bounding_box.y1 = ras.min_y();
						bounding_box.x2 = ras.max_x();
						bounding_box.y2 = ras.max_y();
					}

					// 注，这个过程会同时对m_curved_count进行不必要的计数
					// 因此需要保存一次然后再恢复。
					//m_curved_count.mark();
					//rect_d bounding_box_trans;
					//if( !bounding_rect_single(m_curved_trans, attr.index, 
					//	&bounding_box_trans.x1, &bounding_box_trans.y1,
					//	&bounding_box_trans.x2, &bounding_box_trans.y2) )
					//{
					//	bounding_box_trans.x1 = ras.min_x();
					//	bounding_box_trans.y1 = ras.min_y();
					//	bounding_box_trans.x2 = ras.max_x();
					//	bounding_box_trans.y2 = ras.max_y();
					//}
					//m_curved_count.restore();

					double scl = m_transform.scale();

					double dd0 = calc_distance( bounding_box.x1, bounding_box.y1,
						bounding_box.x2, bounding_box.y1); // original size
					if ( dd0 != 0 )
					{
						double x1 = bounding_box.x1;
						double y1 = bounding_box.y1;
						double x2 = bounding_box.x2;
						double y2 = bounding_box.y1;
						m_transform.transform(&x1, &y1);
						m_transform.transform(&x2, &y2);
						double dd1 = calc_distance( x1, y1, x2, y2 );	// size after transformed
						double scl1 = dd1 / dd0;
						if( scl < scl1 )
						{
							scl = scl1;
						}
					}

					//m_curved.approximation_method(curve_inc);
					m_curved.approximation_scale(scl);
					m_curved.angle_tolerance(0.0);

					rgba8 color;
					
					//if(attr.text_flag)
					//{
					//render_text( ras, ren_base, attr );
					//continue;
					//}

					if( attr.fill_flag)
					{
						ras.reset();
						ras.filling_rule(attr.even_odd_flag ? fill_even_odd : fill_non_zero);
						if(fabs(m_curved_trans_contour.width()) < 0.0001)
						{
							ras.add_path(m_curved_trans, attr.index);
						}
						else
						{
							m_curved_trans_contour.miter_limit(attr.miter_limit);
							ras.add_path(m_curved_trans_contour, attr.index);
						}

						color = attr.fill_color;
						color.opacity(color.opacity() * opacity);
						ren_solid.color(color);
						
						agg::render_scanlines(ras, sl_p, ren_solid);
					}
					else if(!attr.grad_id.empty())
					{
						render_gradient_element( ras, ren_base, bounding_box, scl, attr);
					}

					if( attr.stroke_flag)
					{
						m_curved_stroked.width(attr.stroke_width);
						//m_curved_stroked.line_join((attr.line_join == miter_join) ? miter_join_round : attr.line_join);
						m_curved_stroked.line_join(attr.line_join);
						m_curved_stroked.line_cap(attr.line_cap);
						m_curved_stroked.miter_limit(attr.miter_limit);
						m_curved_stroked.inner_join(inner_round);
						m_curved_stroked.approximation_scale(scl);

						// If the *visual* line width is considerable we 
						// turn on processing of curve cusps.
						//---------------------
						if(attr.stroke_width * scl > 1.0)
						{
							m_curved.angle_tolerance(0.2);
						}
						ras.reset();
						ras.filling_rule(fill_non_zero);
						ras.add_path(m_curved_stroked_trans, attr.index);
						color = attr.stroke_color;
						color.opacity(color.opacity() * opacity);
						ren_solid.color(color);
						agg::render_scanlines(ras, sl_p, ren_solid);
					}
				}

				if( active_path_id >= 0 )
				{
					show_tracker_box_for_path( ras, ren_base, mtx, active_path_id );
				}

				if ( draw_bounding_box )
				{
					show_tracker_box_for_path (ras, ren_base, mtx, -1 );
				}
			}

			template<class Rasterizer, class BaseRenderer> 
			void show_tracker_box_for_path(
				Rasterizer& ras,
				BaseRenderer& ren_base, 
				const trans_affine& mtx, 
				int path_id)
			{
				rect_d bounding_box;
				if ( path_id == -1 )
				{
					if( !bounding_rect( &bounding_box.x1, &bounding_box.y1, &bounding_box.x2, &bounding_box.y2, mtx ) )
					{
						return ;
					}
					//mtx.transform( &bounding_box.x1, &bounding_box.y1 );
					//mtx.transform( &bounding_box.x2, &bounding_box.y2 );
				}
				else
				{
					if( !path_bounding_box( mtx, path_id, bounding_box) )
					{
						return ;
					}
				}

				if ( bounding_box.is_valid() )
				{
					//rect_i cb = ren_base.clip_box();
					//ras.clip_box(cb.x1, cb.y1, cb.x2, cb.y2);

					agg::scanline_p8 sl;

					agg::rounded_rect rr(bounding_box.x1-1, bounding_box.y1-1, bounding_box.x2+1, bounding_box.y2+1, 0 );

					agg::conv_stroke<agg::rounded_rect> conv_rr( rr );
					conv_rr.width(1);
					ras.add_path( conv_rr );
					
					// 显示tracker不使用aa_solid模式,而直接使用bin_solid(不做半像素处理)
					// 下面的2个方案均可以达到此目的(颜色不同,以区别实现方案):

					// 方案1:
					//render_scanlines_bin_solid( ras, sl, ren_base, agg::rgba8(0,0,255,255) );
					render_scanlines_bin_solid( ras, sl, ren_base, agg::rgba8(255,0,0,255) );
					
					// 方案2:
					//typedef agg::renderer_scanline_bin_solid<BaseRenderer> renderer_bin_solid;
					//renderer_bin_solid ren_bin_solid(ren_base);
					//ren_bin_solid.color(agg::rgba8(255,0,0,255 ) );
					//render_scanlines( ras, sl, ren_bin_solid );
					
				}
			}
#pragma component( minrebuild, on )
			// 
			// if there are more than one paths to be test, 
			// we will navigate them in the reverse order.
			// if no any path was hit, returns -1, else returns the index.
			// clipbox - the clipbox to check the point(x,y)
			//
			int hit_test(const trans_affine& mtx, int x, int y);
			int hit_test_in_box(const trans_affine& mtx, int x, int y, rect_i clipbox);

			// retrieve the bounding box for the path by the path_id
			bool path_bounding_box(const trans_affine& mtx, int path_id, rect_d& bounding_box);

			bool zoom_in_path(agg::trans_affine& mtx, int path_id)
			{
				agg::rect_d bounding_box;
				double cx = 0.0, cy = 0.0;
				if ( path_bounding_box(mtx, path_id, bounding_box ) )
				{
					cx = (bounding_box.x1+bounding_box.x2)*0.5;
					cy = (bounding_box.y1+bounding_box.y2)*0.5;
				}

				return m_attr_storage[path_id].zoom_in( cx, cy );
			}

			bool zoom_out_path(agg::trans_affine& mtx, int path_id)
			{
				agg::rect_d bounding_box;
				double cx = 0.0, cy = 0.0;
				if ( path_bounding_box(mtx, path_id, bounding_box ) )
				{
					cx = (bounding_box.x1+bounding_box.x2)*0.5;
					cy = (bounding_box.y1+bounding_box.y2)*0.5;
				}

				return m_attr_storage[path_id].zoom_out( cx, cy );
			}

			bool move_path(int path_id, double tx, double ty ) 
			{
				return m_attr_storage[path_id].move( tx, ty );
			}

			bool rotate_cw_path(agg::trans_affine& mtx, int path_id)
			{
				agg::rect_d bounding_box;
				double cx = 0.0, cy = 0.0;
				if ( path_bounding_box(mtx, path_id, bounding_box ) )
				{
					cx = (bounding_box.x1+bounding_box.x2)*0.5;
					cy = (bounding_box.y1+bounding_box.y2)*0.5;
				}

				return m_attr_storage[path_id].rotate_cw( cx, cy );
			}

			bool rotate_ccw_path(agg::trans_affine& mtx, int path_id)
			{
				agg::rect_d bounding_box;
				double cx = 0.0, cy = 0.0;
				if ( path_bounding_box(mtx, path_id, bounding_box ) )
				{
					cx = (bounding_box.x1+bounding_box.x2)*0.5;
					cy = (bounding_box.y1+bounding_box.y2)*0.5;
				}

				return m_attr_storage[path_id].rotate_ccw( cx, cy );
			}

		private:
			path_attributes& cur_attr();

			path_storage   m_storage;
			attr_storage   m_attr_storage;
			attr_storage   m_attr_stack;
			svg_gradient_elements m_gradients;
			svg_gradient_element* m_current_gradient;
			trans_affine   m_transform;

			curved                       m_curved;
			curved_count                 m_curved_count;

			curved_stroked               m_curved_stroked;
			curved_stroked_trans         m_curved_stroked_trans;

			curved_trans                 m_curved_trans;
			curved_trans_contour         m_curved_trans_contour;
		};

	}
}

#endif
