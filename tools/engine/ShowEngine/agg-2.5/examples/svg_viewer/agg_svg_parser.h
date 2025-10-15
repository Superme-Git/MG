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
// SVG parser.
//
//----------------------------------------------------------------------------

#ifndef AGG_SVG_PARSER_INCLUDED
#define AGG_SVG_PARSER_INCLUDED

#include "agg_svg_path_tokenizer.h"
#include "agg_svg_path_renderer.h"

namespace agg
{
namespace svg
{

	class transform_parser
	{
	public:
		transform_parser(trans_affine& transformer)
			: the_transformer(transformer)
		{
		}

		void parse(const char* str);
		unsigned parse_matrix(const char* str);
		unsigned parse_translate(const char* str);
		unsigned parse_rotate(const char* str);
		unsigned parse_scale(const char* str);
		unsigned parse_skew_x(const char* str);
		unsigned parse_skew_y(const char* str);

		trans_affine& the_transformer;
	};

	class gradient_parser
	{
	public:
		gradient_parser( svg_gradient_element* element )
		{
			the_element = element;
		}

		bool parse(const char ** attr, int i );

	protected:
		svg_gradient_element* the_element;
	};

	class style_parser
	{
	public:
		style_parser()
			: m_attr_name(new char[128]),
			m_attr_value(new char[1024]),
			m_attr_name_len(127),
			m_attr_value_len(1023)
		{

		}

		~style_parser()
		{
			delete [] m_attr_value;
			delete [] m_attr_name;
		}

		void parse_style(const char* str);

		virtual bool parse_attr(const char* name, const char* value)
		{
			return false;
		}

		bool parse_name_value(const char* nv_start, const char* nv_end);

		void copy_name(const char* start, const char* end);

		void copy_value(const char* start, const char* end);

	private:
		char*          m_attr_name;
		char*          m_attr_value;
		unsigned       m_attr_name_len;
		unsigned       m_attr_value_len;
	};

	class stop_parser : public style_parser
	{
		stop_point * the_element;
	public:
		stop_parser(stop_point * element)
		{
			the_element = element;
		}

		void parse(const char** attr);

		virtual bool parse_attr(const char* name, const char* value);	
	};

    class parser : public style_parser
    {
        enum buf_size_e { buf_size = BUFSIZ };
    public:

        ~parser();
        parser(path_renderer& path);

        void parse(const char* fname);
		void parse(const wchar_t* fname);
		void parse(const void* data, size_t size);


        const char* title() const { return m_title; }

    private:
        // XML event handlers
        static void start_element(void* data, const char* el, const char** attr);
        static void end_element(void* data, const char* el);
        static void content(void* data, const char* s, int len);

		void parse_viewbox(const char* str);
		void parse_svg_attr(const char** attr);
        void parse_attr(const char** attr);
        void parse_path(const char** attr);
        void parse_poly(const char** attr, bool close_flag);
        void parse_rect(const char** attr);
		void parse_ellipse(const char** attr);
		void parse_circle(const char** attr);
        void parse_line(const char** attr);
		void parse_text(const char** attr);
        void parse_transform(const char* str);
		void parse_linearGradient(const char** attr);
		void parse_radialGradient(const char** attr);
		void parse_stop(const char** attr);

		virtual bool parse_attr(const char* name, const char* value);

	public:
		svg_length_t get_width() const { return m_width; }
		svg_length_t get_height() const { return m_height; }
		rect_d get_viewbox() const { return m_viewbox; }

    private:
        path_renderer& m_path;
        path_tokenizer m_tokenizer;
		svg_gradient_element::StopsArrayT m_stops;
        char*          m_buf;
        char*          m_title;
        unsigned       m_title_len;
        bool           m_title_flag;
        bool           m_path_flag;
		bool           m_defs_flag;
		bool           m_text_flag;
		char*          m_text_content;
		unsigned       m_text_buf_len;
		unsigned       m_text_len;

		char*          m_attr_name;
        char*          m_attr_value;
        unsigned       m_attr_name_len;
        unsigned       m_attr_value_len;
		svg_length_t   m_width;
		svg_length_t   m_height;
		double			m_last_x;
		double			m_last_y;

		rect_d			m_viewbox;
    };

}
}

#endif
