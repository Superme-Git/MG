#include <stdio.h>
#include "agg_basics.h"
#include "agg_rendering_buffer.h"
#include "agg_rasterizer_scanline_aa.h"
#include "agg_scanline_u.h"
#include "agg_renderer_scanline.h"
#include "agg_span_gradient.h"
#include "agg_span_gradient_alpha.h"
#include "agg_span_interpolator_linear.h"
#include "agg_span_allocator.h"
#include "agg_span_converter.h"
#include "agg_ellipse.h"
#include "agg_rounded_rect.h"
#include "agg_pixfmt_rgb.h"
#include "agg_vcgen_stroke.h"
#include "platform/agg_platform_support.h"

#include "ctrl/agg_spline_ctrl.h"

enum flip_y_e { flip_y = true };



#define pix_format agg::pix_format_bgr24
typedef agg::pixfmt_bgr24 pixfmt_type;
typedef pixfmt_type::color_type color_type;
typedef color_type::value_type color_value_type;


class the_application : public agg::platform_support
{
    double m_x[3];
    double m_y[3];
    double m_dx;
    double m_dy;
    int    m_idx;
    agg::spline_ctrl<color_type> m_alpha;


public:
    the_application(agg::pix_format_e format, bool flip_y) :
        agg::platform_support(format, flip_y),
        m_idx(-1),
        m_alpha(2,  2,  200, 30,  6, !flip_y)
    {
        m_x[0] = 257;    m_y[0] = 60;
        m_x[1] = 369;   m_y[1] = 170;
        m_x[2] = 143;   m_y[2] = 310;

        m_alpha.point(0, 0.0,     0.0);
        m_alpha.point(1, 1.0/5.0, 1.0 - 4.0/5.0);
        m_alpha.point(2, 2.0/5.0, 1.0 - 3.0/5.0);
        m_alpha.point(3, 3.0/5.0, 1.0 - 2.0/5.0);
        m_alpha.point(4, 4.0/5.0, 1.0 - 1.0/5.0);
        m_alpha.point(5, 1.0,     1.0);
        m_alpha.update_spline();
        add_ctrl(m_alpha);
    }



    // A simple function to form the gradient color array 
    // consisting of 3 colors, "begin", "middle", "end"
    //---------------------------------------------------
    template<class ColorArrayT>
    static void fill_color_array(ColorArrayT& array, 
                                 color_type begin, 
                                 color_type middle, 
                                 color_type end)
    {
        unsigned i;
        for(i = 0; i < 128; ++i)
        {
            array[i] = begin.gradient(middle, i / 128.0);
        }
        for(; i < 256; ++i)
        {
            array[i] = middle.gradient(end, (i - 128) / 128.0);
        }
    }


	template<class ColorArrayT>
	static void fill_color_array2(ColorArrayT& array, 
		color_type begin,
		color_type end)
	{
		unsigned i;
		for(i = 0; i < 256; ++i)
		{
			array[i] = begin.gradient(end, i/256.0);
		}
	}

	struct stop_point
	{
		double offset;
		color_type color;
	};
	
	template<class ColorArrayT, class StopsArrayT>
	static void fill_color_array_by_stops_array(
		ColorArrayT& array, 
		StopsArrayT& stops,
		unsigned nStops,
		unsigned span)
	{
		color_type current_stop_color;
		color_type next_stop_color;
		unsigned span_stop = 0; 
		unsigned i = 0;
		unsigned last_stop = 0;

		current_stop_color = stops[0].color;

		// loops for all stops
		for (unsigned  s = 0; s < nStops; s ++ )
		{
			next_stop_color = stops[s].color;

			unsigned next_stop = (unsigned)(span * stops[s].offset);

			// range checking
			if ( next_stop < last_stop )
			{
				next_stop = last_stop;
			}
			else if ( next_stop > span )
			{
				next_stop = span;
			}

			unsigned current_span = next_stop-i;
			unsigned span_start = i;
			for ( ; i < next_stop; i ++ )
			{
				array[i] = current_stop_color.gradient(next_stop_color, (i-span_start)*1.0/current_span);
			}
			current_stop_color = next_stop_color;

			last_stop = next_stop;
		}
		
		// last span, maybe 0 length
		next_stop_color = current_stop_color;
		unsigned current_span = span-i;
		unsigned span_start = i;
		for ( ; i < span; i ++ )
		{
			array[i] = current_stop_color.gradient(next_stop_color, (i-span_start)*1.0/current_span);
		}
	}

	virtual void on_draw()
	{
		//on_draw1();
		//gradient_draw();
		linear_gradient_draw();
	}



	void linear_gradient_draw()
	{
		typedef agg::renderer_base<pixfmt_type> base_ren_type;

		pixfmt_type pf(rbuf_window());
		base_ren_type ren_base(pf);
		ren_base.clear(agg::rgba(1,1,1));

		agg::scanline_u8 sl;
		agg::rasterizer_scanline_aa<> ras;
		
		agg::rounded_rect rrect;

		
		// Span interpolator. This object is used in all span generators 
		// that operate with transformations during iterating of the spans,
		// for example, image transformers use the interpolator too.
		//-----------------
		typedef agg::span_interpolator_linear<> interpolator_type;


		// Span allocator is an object that allocates memory for 
		// the array of colors that will be used to render the 
		// color spans. One object can be shared between different 
		// span generators.
		//-----------------
		typedef agg::span_allocator<color_type> span_allocator_type;


		// Gradient colors array adaptor
		//-----------------
		typedef agg::pod_auto_array<color_type, 256> gradient_colors_type;

		agg::trans_affine mtx1;
		mtx1 *= agg::trans_affine_translation( 50, 10 );
		mtx1 *= trans_affine_resizing();

		agg::trans_affine   gradient_mtx2;
		gradient_mtx2 *= agg::trans_affine_translation( 50, 10 );
		//gradient_mtx2 *= trans_affine_resizing();
		gradient_mtx2.invert();  
		
		span_allocator_type      span_allocator2;  
		interpolator_type        span_interpolator2(gradient_mtx2);
		gradient_colors_type     color_array2;

		agg::gradient_x gradient_func2x;
		
		//typedef  agg::gradient_x gradient_func_type2;
		//typedef  agg::gradient_repeat_adaptor<agg::gradient_x> gradient_func_type2;   
		typedef  agg::gradient_reflect_adaptor<agg::gradient_x> gradient_func_type2;

		gradient_func_type2	gradient_func2(gradient_func2x);

		stop_point stops2[] = 
		{
			//{ 0.1, agg::rgba(0,    0.19, 0.19), },			
			//{ 0.3, agg::rgba(0.7,  0.7,  0.19),	},		
			//{ 0.8, agg::rgba(0.31, 0,    0)		},
			//{ 0.5, agg::rgba(1,  1, 0.4),	},	
			//{ 0.95, agg::rgba(1,  0.4,  0),	},
			{ 0, agg::rgba(1,  0,  0),	},		
			{ 0.25, agg::rgba(1,  1,  0),	},	
			{ 0.5, agg::rgba(0,  1,  0),	},		
			{ 0.75, agg::rgba(0,  1,  1),	},		
			{ 1, agg::rgba(0,  0,  1),	},		
			
		};

		fill_color_array_by_stops_array(color_array2, stops2, _countof(stops2), color_array2.size() );
		
		rrect.rect( 0,0, 300, 150 );
		rrect.radius( 0 );

		typedef agg::span_gradient<color_type, 
			interpolator_type, 
			gradient_func_type2, 
			gradient_colors_type> span_gradient_type2;

		span_gradient_type2 span_gradient2(span_interpolator2, 
			gradient_func2, color_array2, 0, 500);

		

		agg::conv_transform<agg::rounded_rect, agg::trans_affine> t1(rrect, mtx1);

		ras.add_path( t1 );
		agg::render_scanlines_aa(ras, sl, ren_base, span_allocator2, span_gradient2); 

		//agg::render_scanlines_aa_solid(ras, sl, ren_base, agg::rgba(0, 1, 0));
	}

	void gradient_draw()
	{
		typedef agg::renderer_base<pixfmt_type> base_ren_type;

		pixfmt_type pf(rbuf_window());
		base_ren_type ren_base(pf);
		ren_base.clear(agg::rgba(1,1,1));

		agg::scanline_u8 sl;
		agg::rasterizer_scanline_aa<> ras;
		agg::ellipse ell;
		agg::rounded_rect rrect;

		// Gradient shape function (linear, radial, custom, etc)
		//-----------------
		typedef agg::gradient_conic gradient_func_type;   

		// Span interpolator. This object is used in all span generators 
		// that operate with transformations during iterating of the spans,
		// for example, image transformers use the interpolator too.
		//-----------------
		typedef agg::span_interpolator_linear<> interpolator_type;


		// Span allocator is an object that allocates memory for 
		// the array of colors that will be used to render the 
		// color spans. One object can be shared between different 
		// span generators.
		//-----------------
		typedef agg::span_allocator<color_type> span_allocator_type;


		// Gradient colors array adaptor
		//-----------------
		typedef agg::pod_auto_array<color_type, 256> gradient_colors_type;


		// Finally, the gradient span generator working with the color_type 
		// color type. 
		//-----------------
		typedef agg::span_gradient<color_type, 
			interpolator_type, 
			gradient_func_type, 
			gradient_colors_type> span_gradient_type;

	
		// The gradient objects declarations
		//----------------
		gradient_func_type       gradient_func;                      // The gradient function
		agg::trans_affine        gradient_mtx;                       // Gradient affine transformer
		interpolator_type        span_interpolator(gradient_mtx);    // Span gradient interpolator
		span_allocator_type      span_allocator;                     // Span Allocator
		gradient_colors_type     color_array;                        // The gradient colors


		stop_point stops[] = 
		{
			{ 0.1,  agg::rgba(0.0,  1.0,  0.19) },
			{ 0.3,  agg::rgba(0.3, 0.9, 0.0)	},
			{ 0.6,  agg::rgba(1.0, 1.0, 0.7)	},
			{ 1.0,  agg::rgba(0.1, 0, 0.7)		},

			/*
			{ 0.1, agg::rgba(0,    0.19, 0.19), },			
			{ 0.3, agg::rgba(0.7,  0.7,  0.19),	},		
			{ 0.6, agg::rgba(0.31, 0,    0)		},*/
		};	

		fill_color_array_by_stops_array(color_array, stops, _countof(stops), 256 );


		// Declare the gradient span itself. 
		// The last two arguments are so called "d1" and "d2" 
		// defining two distances in pixels, where the gradient starts
		// and where it ends. The actual meaning of "d1" and "d2" depands
		// on the gradient function.
		//----------------
		span_gradient_type span_gradient(span_interpolator, 
			gradient_func, 
			color_array, 
			0, 255);


		// Finally we can draw a circle.
		//----------------
		gradient_mtx *= agg::trans_affine_scaling(0.45, 0.25);
		gradient_mtx *= agg::trans_affine_rotation(-agg::pi/3.0);
		gradient_mtx *= agg::trans_affine_translation(width()/2, 250);
		//gradient_mtx *= trans_affine_resizing();
		gradient_mtx.invert();
		
		ell.init(width()/2, 250, 100, 50, 100);
		ras.add_path(ell);
		
		// Render the circle with gradient plus alpha-gradient 
		agg::render_scanlines_aa(ras, sl, ren_base, span_allocator, span_gradient);



		agg::trans_affine   gradient_mtx2;
		gradient_mtx2 *= agg::trans_affine_translation(width()/2, height()/4);
		gradient_mtx2.invert();  

		span_allocator_type      span_allocator2;  
		interpolator_type        span_interpolator2(gradient_mtx2);
		gradient_colors_type     color_array2;

		typedef agg::gradient_circle gradient_func_type2;   
		gradient_func_type2		gradient_func2;

		stop_point stops2[] = 
		{
			{ 0.1, agg::rgba(0,    0.19, 0.19), },			
			{ 0.3, agg::rgba(0.7,  0.7,  0.19),	},		
			{ 0.8, agg::rgba(0.31, 0,    0)		},
		};	

		fill_color_array_by_stops_array(color_array2, stops2, _countof(stops2), 256 );

		typedef agg::span_gradient<color_type, 
			interpolator_type, 
			gradient_func_type2, 
			gradient_colors_type> span_gradient_type2;

		span_gradient_type2 span_gradient2(span_interpolator2, 
			gradient_func2, color_array2,  0, 255);

		// rrect.rect( 10, 10, width()-10, height()/2  - 10 );
		rrect.rect( 0,0, 300, 150 );
		rrect.radius( 0 );
 
		double ini_scale = 1.0;
		agg::trans_affine mtx1;
		mtx1 *= agg::trans_affine_scaling(ini_scale, ini_scale);
		mtx1 *= agg::trans_affine_translation( 50, 10 );
		mtx1 *= trans_affine_resizing();

		agg::conv_transform<agg::rounded_rect, agg::trans_affine> t1(rrect, mtx1);

		ras.add_path( t1 );
		agg::render_scanlines_aa(ras, sl, ren_base, span_allocator2, span_gradient2); 
		
		//agg::render_scanlines_aa_solid(ras, sl, ren_base, agg::rgba(0, 1, 0));
	}

    virtual void on_draw1()
    {
        typedef agg::renderer_base<pixfmt_type> base_ren_type;

        pixfmt_type pf(rbuf_window());
        base_ren_type ren_base(pf);
        ren_base.clear(agg::rgba(1,1,1));

        agg::scanline_u8 sl;
        agg::rasterizer_scanline_aa<> ras;

        // Draw some background
        agg::ellipse ell;
        srand(1234);
        unsigned i;
        unsigned w = unsigned(width());
        unsigned h = unsigned(height());
        for(i = 0; i < 100; i++)
        {
            ell.init(rand() % w, rand() % h, rand() % 60 + 5, rand() % 60 + 5, 50);
            ras.add_path(ell);
            agg::render_scanlines_aa_solid(ras, sl, ren_base, 
                                           agg::rgba(rand() / double(RAND_MAX), 
                                                     rand() / double(RAND_MAX), 
                                                     rand() / double(RAND_MAX), 
                                                     rand() / double(RAND_MAX) / 2.0));
        }
		
        double parallelogram[6];
        parallelogram[0] = m_x[0];
        parallelogram[1] = m_y[0];
        parallelogram[2] = m_x[1];
        parallelogram[3] = m_y[1];
        parallelogram[4] = m_x[2];
        parallelogram[5] = m_y[2];


        // Gradient shape function (linear, radial, custom, etc)
        //-----------------
        typedef agg::gradient_circle gradient_func_type;   

        // Alpha gradient shape function (linear, radial, custom, etc)
        //-----------------
        typedef agg::gradient_xy  gradient_alpha_func_type;

        // Span interpolator. This object is used in all span generators 
        // that operate with transformations during iterating of the spans,
        // for example, image transformers use the interpolator too.
        //-----------------
        typedef agg::span_interpolator_linear<> interpolator_type;


        // Span allocator is an object that allocates memory for 
        // the array of colors that will be used to render the 
        // color spans. One object can be shared between different 
        // span generators.
        //-----------------
        typedef agg::span_allocator<color_type> span_allocator_type;


        // Gradient colors array adaptor
        //-----------------
        typedef agg::pod_auto_array<color_type, 256> gradient_colors_type;


        // Finally, the gradient span generator working with the color_type 
        // color type. 
        //-----------------
        typedef agg::span_gradient<color_type, 
                                   interpolator_type, 
                                   gradient_func_type, 
                                   gradient_colors_type> span_gradient_type;


        // Gradient alpha array adaptor
        //-----------------
        typedef agg::pod_auto_array<color_value_type, 256> gradient_alpha_type;

        // The alpha gradient span converter working with the color_type 
        // color type. 
        //-----------------
        typedef agg::span_gradient_alpha<color_type, 
                                         interpolator_type, 
                                         gradient_alpha_func_type, 
                                         gradient_alpha_type> span_gradient_alpha_type;


        // Span converter type
        //-----------------
        typedef agg::span_converter<span_gradient_type,
                                    span_gradient_alpha_type> span_conv_type;

        // The gradient objects declarations
        //----------------
        gradient_func_type       gradient_func;                      // The gradient function
        gradient_alpha_func_type alpha_func;                         // The gradient function
        agg::trans_affine        gradient_mtx;                       // Gradient affine transformer
        agg::trans_affine        alpha_mtx;                          // Alpha affine transformer
        interpolator_type        span_interpolator(gradient_mtx);    // Span gradient interpolator
        interpolator_type        span_interpolator_alpha(alpha_mtx); // Span alpha interpolator
        span_allocator_type      span_allocator;                     // Span Allocator
        gradient_colors_type     color_array;                        // The gradient colors

        // Declare the gradient span itself. 
        // The last two arguments are so called "d1" and "d2" 
        // defining two distances in pixels, where the gradient starts
        // and where it ends. The actual meaning of "d1" and "d2" depands
        // on the gradient function.
        //----------------
        span_gradient_type span_gradient(span_interpolator, 
                                         gradient_func, 
                                         color_array, 
                                         0, 150);

        // Declare the gradient span itself. 
        // The last two arguments are so called "d1" and "d2" 
        // defining two distances in pixels, where the gradient starts
        // and where it ends. The actual meaning of "d1" and "d2" depands
        // on the gradient function.
        //----------------
        gradient_alpha_type alpha_array;
        span_gradient_alpha_type span_gradient_alpha(span_interpolator_alpha, 
                                                     alpha_func, 
                                                     alpha_array, 
                                                     0, 100);

        // Span converter declaration
        span_conv_type span_conv(span_gradient, span_gradient_alpha);
		

        // Finally we can draw a circle.
        //----------------
        gradient_mtx *= agg::trans_affine_scaling(0.75, 1.2);
		gradient_mtx *= agg::trans_affine_rotation(-agg::pi/3.0);
        gradient_mtx *= agg::trans_affine_translation(width()/2, height()/2);
        gradient_mtx.invert();
        alpha_mtx.parl_to_rect(parallelogram, -100, -100, 100, 100);

        fill_color_array(color_array, 
                         agg::rgba(0,    0.19, 0.19), 
                         agg::rgba(0.7,  0.7,  0.19),
                         agg::rgba(0.31, 0,    0));

		// the following codes performs same behavior as above fill_color_array()

		//stop_point stops[] = 
		//{	
		//	{ 0.0, agg::rgba(0,    0.19, 0.19), },			
		//	{ 0.5, agg::rgba(0.7,  0.7,  0.19),	},
		//	{ 1.0, agg::rgba(0.31, 0,    0)		},
		//	
		//};
		//fill_color_array_by_stops_array(color_array, stops, _countof(stops), 256 );
		
        // Fill Alpha array
        //----------------
        for(i = 0; i < 256; i++)
        {
            alpha_array[i] = color_value_type(m_alpha.value(i / 255.0) * double(color_type::base_mask));
        }

        ell.init(width()/2, height()/2, 180, 130, 100);
        ras.add_path(ell);

        // Render the circle with gradient plus alpha-gradient 
        agg::render_scanlines_aa(ras, sl, ren_base, span_allocator, span_conv);

        // Draw the control points and the parallelogram
        //-----------------
		agg::rgba color_pnt(0, 0.4, 0.4, 0.31);
		ell.init(m_x[0], m_y[0], 5, 5, 20);
		ras.add_path(ell);
		agg::render_scanlines_aa_solid(ras, sl, ren_base, color_pnt);
		ell.init(m_x[1], m_y[1], 5, 5, 20);
		ras.add_path(ell);
		agg::render_scanlines_aa_solid(ras, sl, ren_base, color_pnt);
		ell.init(m_x[2], m_y[2], 5, 5, 20);
		ras.add_path(ell);
		agg::render_scanlines_aa_solid(ras, sl, ren_base, color_pnt);

        agg::vcgen_stroke stroke;
        stroke.add_vertex(m_x[0], m_y[0], agg::path_cmd_move_to);
        stroke.add_vertex(m_x[1], m_y[1], agg::path_cmd_line_to);
        stroke.add_vertex(m_x[2], m_y[2], agg::path_cmd_line_to);
        stroke.add_vertex(m_x[0]+m_x[2]-m_x[1], m_y[0]+m_y[2]-m_y[1], agg::path_cmd_line_to);
        stroke.add_vertex(0, 0, agg::path_cmd_end_poly | agg::path_flags_close);
        ras.add_path(stroke);
        agg::render_scanlines_aa_solid(ras, sl, ren_base, agg::rgba(0, 0, 0));


        agg::render_ctrl(ras, sl, ren_base, m_alpha);
    }


    virtual void on_mouse_button_down(int x, int y, unsigned flags)
    {
        unsigned i;
        if(flags & agg::mouse_left)
        {
            for (i = 0; i < 3; i++)
            {
                if(sqrt( (x-m_x[i]) * (x-m_x[i]) + (y-m_y[i]) * (y-m_y[i]) ) < 10.0)
                {
                    m_dx = x - m_x[i];
                    m_dy = y - m_y[i];
                    m_idx = i;
                    break;
                }
            }
            if(i == 3)
            {
                if(agg::point_in_triangle(m_x[0], m_y[0], 
                                          m_x[1], m_y[1],
                                          m_x[2], m_y[2],
                                          x, y))
                {
                    m_dx = x - m_x[0];
                    m_dy = y - m_y[0];
                    m_idx = 3;
                }

            }
        }
    }


    virtual void on_mouse_move(int x, int y, unsigned flags)
    {
        if(flags & agg::mouse_left)
        {
            if(m_idx == 3)
            {
                double dx = x - m_dx;
                double dy = y - m_dy;
                m_x[1] -= m_x[0] - dx;
                m_y[1] -= m_y[0] - dy;
                m_x[2] -= m_x[0] - dx;
                m_y[2] -= m_y[0] - dy;
                m_x[0] = dx;
                m_y[0] = dy;
                force_redraw();
                return;
            }

            if(m_idx >= 0)
            {
                m_x[m_idx] = x - m_dx;
                m_y[m_idx] = y - m_dy;
                force_redraw();
            }
        }
        else
        {
            on_mouse_button_up(x, y, flags);
        }
    }

    virtual void on_mouse_button_up(int x, int y, unsigned flags)
    {
        m_idx = -1;
    }

    
    virtual void on_key(int x, int y, unsigned key, unsigned flags)
    {
        double dx = 0;
        double dy = 0;
        switch(key)
        {
        case agg::key_left:  dx = -0.1; break;
        case agg::key_right: dx =  0.1; break;
        case agg::key_up:    dy =  0.1; break;
        case agg::key_down:  dy = -0.1; break;
        }
        m_x[0] += dx;
        m_y[0] += dy;
        m_x[1] += dx;
        m_y[1] += dy;
        force_redraw();
    }


};


int agg_main(int argc, char* argv[])
{
    the_application app(pix_format, flip_y);
    app.caption("AGG Example. Alpha channel gradient");

    if(app.init(400, 320, agg::window_resize))
    {
        return app.run();
    }
    return 1;
}


