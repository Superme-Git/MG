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

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include "agg_svg_parser.h"

#define XML_STATIC
#include "expat.h"

namespace agg
{
namespace svg
{
    struct named_color
    {
        char  name[22];
        int8u r, g, b, a;
    };

    named_color colors[] = 
    {
        { "aliceblue",240,248,255, 255 },
        { "antiquewhite",250,235,215, 255 },
        { "aqua",0,255,255, 255 },
        { "aquamarine",127,255,212, 255 },
        { "azure",240,255,255, 255 },
        { "beige",245,245,220, 255 },
        { "bisque",255,228,196, 255 },
        { "black",0,0,0, 255 },
        { "blanchedalmond",255,235,205, 255 },
        { "blue",0,0,255, 255 },
        { "blueviolet",138,43,226, 255 },
        { "brown",165,42,42, 255 },
        { "burlywood",222,184,135, 255 },
        { "cadetblue",95,158,160, 255 },
        { "chartreuse",127,255,0, 255 },
        { "chocolate",210,105,30, 255 },
        { "coral",255,127,80, 255 },
        { "cornflowerblue",100,149,237, 255 },
        { "cornsilk",255,248,220, 255 },
        { "crimson",220,20,60, 255 },
        { "cyan",0,255,255, 255 },
        { "darkblue",0,0,139, 255 },
        { "darkcyan",0,139,139, 255 },
        { "darkgoldenrod",184,134,11, 255 },
        { "darkgray",169,169,169, 255 },
        { "darkgreen",0,100,0, 255 },
        { "darkgrey",169,169,169, 255 },
        { "darkkhaki",189,183,107, 255 },
        { "darkmagenta",139,0,139, 255 },
        { "darkolivegreen",85,107,47, 255 },
        { "darkorange",255,140,0, 255 },
        { "darkorchid",153,50,204, 255 },
        { "darkred",139,0,0, 255 },
        { "darksalmon",233,150,122, 255 },
        { "darkseagreen",143,188,143, 255 },
        { "darkslateblue",72,61,139, 255 },
        { "darkslategray",47,79,79, 255 },
        { "darkslategrey",47,79,79, 255 },
        { "darkturquoise",0,206,209, 255 },
        { "darkviolet",148,0,211, 255 },
        { "deeppink",255,20,147, 255 },
        { "deepskyblue",0,191,255, 255 },
        { "dimgray",105,105,105, 255 },
        { "dimgrey",105,105,105, 255 },
        { "dodgerblue",30,144,255, 255 },
        { "firebrick",178,34,34, 255 },
        { "floralwhite",255,250,240, 255 },
        { "forestgreen",34,139,34, 255 },
        { "fuchsia",255,0,255, 255 },
        { "gainsboro",220,220,220, 255 },
        { "ghostwhite",248,248,255, 255 },
        { "gold",255,215,0, 255 },
        { "goldenrod",218,165,32, 255 },
        { "gray",128,128,128, 255 },
        { "green",0,128,0, 255 },
        { "greenyellow",173,255,47, 255 },
        { "grey",128,128,128, 255 },
        { "honeydew",240,255,240, 255 },
        { "hotpink",255,105,180, 255 },
        { "indianred",205,92,92, 255 },
        { "indigo",75,0,130, 255 },
        { "ivory",255,255,240, 255 },
        { "khaki",240,230,140, 255 },
        { "lavender",230,230,250, 255 },
        { "lavenderblush",255,240,245, 255 },
        { "lawngreen",124,252,0, 255 },
        { "lemonchiffon",255,250,205, 255 },
        { "lightblue",173,216,230, 255 },
        { "lightcoral",240,128,128, 255 },
        { "lightcyan",224,255,255, 255 },
        { "lightgoldenrodyellow",250,250,210, 255 },
        { "lightgray",211,211,211, 255 },
        { "lightgreen",144,238,144, 255 },
        { "lightgrey",211,211,211, 255 },
        { "lightpink",255,182,193, 255 },
        { "lightsalmon",255,160,122, 255 },
        { "lightseagreen",32,178,170, 255 },
        { "lightskyblue",135,206,250, 255 },
        { "lightslategray",119,136,153, 255 },
        { "lightslategrey",119,136,153, 255 },
        { "lightsteelblue",176,196,222, 255 },
        { "lightyellow",255,255,224, 255 },
        { "lime",0,255,0, 255 },
        { "limegreen",50,205,50, 255 },
        { "linen",250,240,230, 255 },
        { "magenta",255,0,255, 255 },
        { "maroon",128,0,0, 255 },
        { "mediumaquamarine",102,205,170, 255 },
        { "mediumblue",0,0,205, 255 },
        { "mediumorchid",186,85,211, 255 },
        { "mediumpurple",147,112,219, 255 },
        { "mediumseagreen",60,179,113, 255 },
        { "mediumslateblue",123,104,238, 255 },
        { "mediumspringgreen",0,250,154, 255 },
        { "mediumturquoise",72,209,204, 255 },
        { "mediumvioletred",199,21,133, 255 },
        { "midnightblue",25,25,112, 255 },
        { "mintcream",245,255,250, 255 },
        { "mistyrose",255,228,225, 255 },
        { "moccasin",255,228,181, 255 },
        { "navajowhite",255,222,173, 255 },
        { "navy",0,0,128, 255 },
        { "oldlace",253,245,230, 255 },
        { "olive",128,128,0, 255 },
        { "olivedrab",107,142,35, 255 },
        { "orange",255,165,0, 255 },
        { "orangered",255,69,0, 255 },
        { "orchid",218,112,214, 255 },
        { "palegoldenrod",238,232,170, 255 },
        { "palegreen",152,251,152, 255 },
        { "paleturquoise",175,238,238, 255 },
        { "palevioletred",219,112,147, 255 },
        { "papayawhip",255,239,213, 255 },
        { "peachpuff",255,218,185, 255 },
        { "peru",205,133,63, 255 },
        { "pink",255,192,203, 255 },
        { "plum",221,160,221, 255 },
        { "powderblue",176,224,230, 255 },
        { "purple",128,0,128, 255 },
        { "red",255,0,0, 255 },
        { "rosybrown",188,143,143, 255 },
        { "royalblue",65,105,225, 255 },
        { "saddlebrown",139,69,19, 255 },
        { "salmon",250,128,114, 255 },
        { "sandybrown",244,164,96, 255 },
        { "seagreen",46,139,87, 255 },
        { "seashell",255,245,238, 255 },
        { "sienna",160,82,45, 255 },
        { "silver",192,192,192, 255 },
        { "skyblue",135,206,235, 255 },
        { "slateblue",106,90,205, 255 },
        { "slategray",112,128,144, 255 },
        { "slategrey",112,128,144, 255 },
        { "snow",255,250,250, 255 },
        { "springgreen",0,255,127, 255 },
        { "steelblue",70,130,180, 255 },
        { "tan",210,180,140, 255 },
        { "teal",0,128,128, 255 },
        { "thistle",216,191,216, 255 },
        { "tomato",255,99,71, 255 },
        { "turquoise",64,224,208, 255 },
        { "violet",238,130,238, 255 },
        { "wheat",245,222,179, 255 },
        { "white",255,255,255, 255 },
        { "whitesmoke",245,245,245, 255 },
        { "yellow",255,255,0, 255 },
        { "yellowgreen",154,205,50, 255 },
        { "zzzzzzzzzzz",0,0,0, 0 }
    }; 


    //------------------------------------------------------------------------
    parser::~parser()
    {
        delete [] m_buf;
        delete [] m_title;
		if ( m_text_content != NULL )
		{
			delete[] m_text_content;
			m_text_content = NULL;
		}
    }

    //------------------------------------------------------------------------
    parser::parser(path_renderer& path) :
        m_path(path),
        m_tokenizer(),
        m_buf(new char[buf_size]),
        m_title(new char[256]),
		m_text_content(0),
		m_text_buf_len(0),
		m_text_len(0),
        m_title_len(0),
		m_defs_flag(false),
		m_text_flag(false),
        m_title_flag(false),
        m_path_flag(false),
		m_last_x(0.0),
		m_last_y(0.0)
    {
        m_title[0] = 0;
		//_svg_length_init( &m_width, 0.0 );
		
		m_width.unit = SVG_LENGTH_PERCENTAGE;
		m_width.value = 100;

		//_svg_length_init( &m_height, 0.0 );

		m_height.unit = SVG_LENGTH_PERCENTAGE;
		m_height.value = 100;

		m_viewbox.init( 0,0,-1,-1 );// not a valid viewbox

    }

	//------------------------------------------------------------------------
	void parser::parse(const wchar_t* fname)
	{
		char msg[1024];
		XML_Parser p = XML_ParserCreate(NULL);
		if(p == 0) 
		{
			throw exception("Couldn't allocate memory for parser");
		}

		XML_SetUserData(p, this);
		XML_SetElementHandler(p, start_element, end_element);
		XML_SetCharacterDataHandler(p, content);

		FILE* fd = NULL;
		errno_t err = _wfopen_s(&fd, fname, L"r");
		if(err != 0 || fd == 0)
		{
			size_t result;
			char buffer[1024];
			wcstombs_s( &result, buffer, 1024, fname,  _TRUNCATE );
			sprintf_s(msg, 1024, "Couldn't open file %s", buffer);

			XML_ParserFree(p);

			throw exception(msg);
		}

		bool done = false;
		do
		{
			size_t len = fread(m_buf, 1, buf_size, fd);
			done = len < buf_size;
			if(!XML_Parse(p, m_buf, len, done))
			{
				sprintf_s(msg, sizeof(msg), 
					"%s at line %d\n",
					XML_ErrorString(XML_GetErrorCode(p)),
					XML_GetCurrentLineNumber(p));

				fclose(fd);
				XML_ParserFree(p);

				throw exception(msg);
			}
		}
		while(!done);
		fclose(fd);
		XML_ParserFree(p);

		char* ts = m_title;
		while(*ts)
		{
			if(*ts < ' ') *ts = ' ';
			++ts;
		}
	}
    
	//------------------------------------------------------------------------
    void parser::parse(const char* fname)
    {
        char msg[1024];
	    XML_Parser p = XML_ParserCreate(NULL);
	    if(p == 0) 
	    {
		    throw exception("Couldn't allocate memory for parser");
	    }

        XML_SetUserData(p, this);
	    XML_SetElementHandler(p, start_element, end_element);
	    XML_SetCharacterDataHandler(p, content);

		FILE* fd = NULL;
        errno_t err = fopen_s(&fd, fname, "r");
        if(err != 0 || fd == 0)
        {
            sprintf_s(msg, sizeof(msg), "Couldn't open file %s", fname);

			XML_ParserFree(p);

		    throw exception(msg);
        }

        bool done = false;
        do
        {
            size_t len = fread(m_buf, 1, buf_size, fd);
            done = len < buf_size;
            if(!XML_Parse(p, m_buf, len, done))
            {
                sprintf_s(msg, sizeof(msg), 
                    "%s at line %d\n",
                    XML_ErrorString(XML_GetErrorCode(p)),
                    XML_GetCurrentLineNumber(p));
				fclose(fd);
				XML_ParserFree(p);
                throw exception(msg);
            }
        }
        while(!done);
        fclose(fd);
        XML_ParserFree(p);

        char* ts = m_title;
        while(*ts)
        {
            if(*ts < ' ') *ts = ' ';
            ++ts;
        }
    }

	//------------------------------------------------------------------------
	void parser::parse(const void* data, size_t size)
	{
		char msg[1024];
		XML_Parser p = XML_ParserCreate(NULL);
		if(p == 0) 
		{
			throw exception("Couldn't allocate memory for parser");
		}

		XML_SetUserData(p, this);
		XML_SetElementHandler(p, start_element, end_element);
		XML_SetCharacterDataHandler(p, content);

		const char* data_ptr = (const char*)data;
		size_t size_left = size;
		size_t len = 0;
		bool done = false;

#ifndef min
#define min(a,b) (((a) < (b)) ? (a) : (b))
#endif
		do
		{
			len = min( buf_size, size_left );
			memcpy_s(m_buf, buf_size, data_ptr , len);
			done = len < buf_size;
			if(!XML_Parse(p, m_buf, len, done))
			{
				sprintf_s(msg, sizeof(msg), 
					"%s at line %d\n",
					XML_ErrorString(XML_GetErrorCode(p)),
					XML_GetCurrentLineNumber(p));
				throw exception(msg);
			}

			size_left -= len;
			data_ptr += len;

		}
		while(!done);
		
		XML_ParserFree(p);

		char* ts = m_title;
		while(*ts)
		{
			if(*ts < ' ') *ts = ' ';
			++ts;
		}
	}

    //------------------------------------------------------------------------
    void parser::start_element(void* data, const char* el, const char** attr)
    {
        parser& self = *(parser*)data;

		if (strcmp(el, "svg") == 0)
		{
			//width="586" height="652" viewBox="0 0 586 652"
			self.m_path.push_attr();
			self.parse_svg_attr(attr);
		}
        else if(strcmp(el, "title") == 0)
        {
            self.m_title_flag = true;
        }
		else if (strcmp(el, "text") == 0 )
		{
			self.m_text_flag = true;
			self.parse_text(attr);
		}
		else if ( strcmp( el, "pattern") == 0 )
		{
			self.m_path.begin_pattern();
			self.parse_attr(attr);
		}
		else if ( strcmp( el, "defs") == 0 )
		{
			self.m_path.begin_defs();
			self.parse_attr(attr);
		}
        else
        if(strcmp(el, "g") == 0)
        {
			self.m_path.push_attr();
			self.parse_attr(attr);
        }
        else
        if(strcmp(el, "path") == 0)
        {
            if(self.m_path_flag)
            {
                throw exception("start_element: Nested path");
            }
            self.m_path.begin_path();
            self.parse_path(attr);
            self.m_path.end_path();
            self.m_path_flag = true;
        }
        else
        if(strcmp(el, "rect") == 0) 
        {
            self.parse_rect(attr);
		}
		else
		if(strcmp(el, "ellipse") == 0) 
		{
			self.parse_ellipse(attr);
		}
		else
		if(strcmp(el, "circle") == 0) 
		{
			self.parse_circle(attr);
		}
		else
        if(strcmp(el, "line") == 0) 
        {
            self.parse_line(attr);
        }
        else
        if(strcmp(el, "polyline") == 0) 
        {
            self.parse_poly(attr, false);
        }
        else
        if(strcmp(el, "polygon") == 0) 
        {
            self.parse_poly(attr, true);
        }
		else
		if ( strcmp( el, "linearGradient") == 0 )
		{
			self.parse_linearGradient(attr);
		}
		else
		if ( strcmp(el, "radialGradient") == 0 )
		{
			self.parse_radialGradient(attr);
		}
		else 
		if ( strcmp(el, "stop") == 0 )
		{
			self.parse_stop(attr);
		}
		else 
		if ( strcmp(el, "use") == 0 )
		{
			// TODO:
		}

		//else
        //if(strcmp(el, "<OTHER_ELEMENTS>") == 0) 
        //{
        //}
        // . . .
    } 


    //------------------------------------------------------------------------
    void parser::end_element(void* data, const char* el)
    {
        parser& self = *(parser*)data;

		if(strcmp(el, "svg") == 0)
		{
			self.m_path.pop_attr();
		}
		else if(strcmp(el, "title") == 0)
        {
            self.m_title_flag = false;
        }
		else if (strcmp(el, "text") == 0 )
		{
			self.m_text_flag = false;
		}
		else if ( strcmp( el, "pattern") == 0 )
		{
			self.m_path.end_pattern();
		}
		else if ( strcmp( el, "defs") == 0 )
		{
			self.m_path.end_defs();
		}
        else
        if(strcmp(el, "g") == 0)
        {
            self.m_path.pop_attr();
        }
        else
        if(strcmp(el, "path") == 0)
        {
            self.m_path_flag = false;
        }
		else 
		if( strcmp (el, "linearGradient") == 0 ||
			strcmp (el, "radialGradient") == 0 )
		{
			svg_gradient_element* cur_gradient = self.m_path.curr_gradient_element();
			if ( cur_gradient != NULL )
			{
				cur_gradient->stops.swap( self.m_stops );
			}
		}
        //else
        //if(strcmp(el, "<OTHER_ELEMENTS>") == 0) 
        //{
        //}
        // . . .
    }


    //------------------------------------------------------------------------
	void parser::content(void* data, const char* s, int len)
    {
        parser& self = *(parser*)data;

        // m_title_flag signals that the <title> tag is being parsed now.
        // The following code concatenates the pieces of content of the <title> tag.
        if(self.m_title_flag)
        {
            if(len + self.m_title_len > 255) len = 255 - self.m_title_len;
            if(len > 0) 
            {
                memcpy(self.m_title + self.m_title_len, s, len);
                self.m_title_len += len;
                self.m_title[self.m_title_len] = 0;
            }
        }

		if(self.m_text_flag)
		{
			if ( self.m_text_content != NULL )
			{
				if ( self.m_text_buf_len < (unsigned)len )
				{
					delete[] self.m_text_content;
					self.m_text_content = NULL;
				}
			}

			if ( self.m_text_content == NULL )
			{
				self.m_text_buf_len = len + 1;
				if ( self.m_text_buf_len < 256 )
				{
					self.m_text_buf_len = 256;
				}

				self.m_text_content = new char[self.m_text_buf_len];
			}

			if ( len > 0)
			{
				memcpy_s( self.m_text_content, self.m_text_buf_len, s, len );
			}
			self.m_text_content[len] = 0;
			self.m_text_len = len;

			double x = self.m_last_x;
			double y = self.m_last_y;
			self.m_path.text( x,  y, self.m_text_content );
			self.m_last_x = 0.0;
			self.m_last_y = 0.0;
		}
    }


    //------------------------------------------------------------------------
    void parser::parse_attr(const char** attr)
    {
        int i;
        for(i = 0; attr[i]; i += 2)
        {
            if(strcmp(attr[i], "style") == 0)
            {
                parse_style(attr[i + 1]);
            }
            else
            {
                parse_attr(attr[i], attr[i + 1]);
            }
        }
    }

    //-------------------------------------------------------------
    void parser::parse_path(const char** attr)
    {
        int i;

        for(i = 0; attr[i]; i += 2)
        {
            // The <path> tag can consist of the path itself ("d=") 
            // as well as of other parameters like "style=", "transform=", etc.
            // In the last case we simply rely on the function of parsing 
            // attributes (see 'else' branch).
            if(strcmp(attr[i], "d") == 0)
            {
                m_tokenizer.set_path_str(attr[i + 1]);
                m_path.parse_path(m_tokenizer);
            }
            else
            {
                // Create a temporary single pair "name-value" in order
                // to avoid multiple calls for the same attribute.
                const char* tmp[4];
                tmp[0] = attr[i];
                tmp[1] = attr[i + 1];
                tmp[2] = 0;
                tmp[3] = 0;
                parse_attr(tmp);
            }
        }
    }


    //-------------------------------------------------------------
    int cmp_color(const void* p1, const void* p2)
    {
        return strcmp(((named_color*)p1)->name, ((named_color*)p2)->name);
    }

    //-------------------------------------------------------------
    rgba8 parse_color(const char* str)
    {
        while(*str == ' ') ++str;
        unsigned c = 0;
        if(*str == '#')
        {
			size_t hexlen = strlen(str+1) ;
			if ( hexlen == 3 )
			{
				char rrggbb[7];
				rrggbb[0] = str[1];
				rrggbb[1] = str[1];
				rrggbb[2] = str[2];
				rrggbb[3] = str[2];
				rrggbb[4] = str[3];
				rrggbb[5] = str[3];
				rrggbb[6] = 0;
				sscanf_s(rrggbb, "%x", &c);
			}
			else if ( hexlen == 6 )
			{
				sscanf_s(str+1, "%x", &c);
			}
			
            return rgb8_packed(c);
        }
		else if (strncmp("rgb(", str, 4) == 0)
		{
			unsigned r = 0;
			unsigned g = 0;
			unsigned b = 0;
			sscanf_s(str, "rgb(%u,%u,%u)", &r,&g,&b);

			return rgba8(r,g,b);
		}
        else
        {
            named_color c;
            unsigned len = strlen(str);
            if(len > sizeof(c.name) - 1)
            {
                throw exception("parse_color: Invalid color name '%s'", str);
            }
            strcpy_s(c.name, sizeof(c.name), str);
            const void* p = bsearch(&c, 
                                    colors, 
                                    sizeof(colors) / sizeof(colors[0]), 
                                    sizeof(colors[0]), 
                                    cmp_color);
            if(p == 0)
            {
                throw exception("parse_color: Invalid color name '%s'", str);
				return rgba8();
            }
            const named_color* pc = (const named_color*)p;
            return rgba8(pc->r, pc->g, pc->b, pc->a);
        }
    }

	static bool is_numeric(char c)
	{
		return strchr("0123456789+-.eE", c) != 0;
	}

    double parse_double(const char* str)
    {
        while(*str == ' ') ++str;

		bool bPercentage = false;
		const char* c = str;
		while ( *c )
		{
			if ( !is_numeric( *c ) )
			{
				if ( *c == '%' )
				{
					bPercentage = true;
				}
				break;
			}
			c ++;
		}

		if (bPercentage )
		{
			return atof(str)/100.0;
		}
		else
		{
			return atof(str);
		}
    }

	bool _svg_element_parse_view_box (const char	*view_box_str,
		double	*x,
		double	*y,
		double	*width,
		double	*height)
	{
		const char *s;
		const char *end;

		s = view_box_str;
		*x = _svg_ascii_strtod (s, &end);
		if (end == s)
			return false;

		s = end;
		_svg_str_skip_space_or_char (&s, ',');
		*y = _svg_ascii_strtod (s, &end);
		if (end == s)
			return false;

		s = end;
		_svg_str_skip_space_or_char (&s, ',');
		*width = _svg_ascii_strtod (s, &end);
		if (end == s)
			return false;

		s = end;
		_svg_str_skip_space_or_char (&s, ',');
		*height = _svg_ascii_strtod (s, &end);
		if (end == s)
			return false;

		return true;
	}

	//-------------------------------------------------------------
	void parser::parse_viewbox(const char* str)
	{
		// parse viewbox
		double x=0, y=0, width = 0, height = 0;
		if( _svg_element_parse_view_box( str, &x, &y, &width, &height ) )
		{
			m_viewbox.x1 = x;
			m_viewbox.y1 = y;
			m_viewbox.x2 = x + width;
			m_viewbox.y2 = y + height;
		}
	}

	//-------------------------------------------------------------
	void parser::parse_svg_attr(const char** attr)
	{
		double x = 0.0;
		double y = 0.0;
		double w = 0.0;
		double h = 0.0;


		for(int i = 0; attr[i]; i += 2)
		{
			if(!parse_attr(attr[i], attr[i + 1]))
			{
				if(strcmp(attr[i], "viewBox") == 0)
				{
					parse_viewbox( attr[i + 1] );
				}
				else if(strcmp(attr[i], "x") == 0)      x = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "y") == 0)      y = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "width") == 0)
				{
					//w = parse_double(attr[i + 1]);
					_svg_length_init_from_str(&m_width, attr[i + 1]);
				}
				else if(strcmp(attr[i], "height") == 0) 
				{
					//h = parse_double(attr[i + 1]);
					_svg_length_init_from_str(&m_height, attr[i + 1]);
				}
			}
		}

		// save the last result
		//m_width = w;
		//m_height = h;
	}

    //-------------------------------------------------------------
    bool parser::parse_attr(const char* name, const char* value)
    {
		if(strcmp(name, "id") == 0)
		{
			 m_path.set_element_id( value );
		}
		else if(strcmp(name, "style") == 0)
        {
            parse_style(value);
        }
        else
        if(strcmp(name, "fill") == 0)
        {
            if(strcmp(value, "none") == 0)
            {
                m_path.fill_none();
            }
			else if ( strncmp( value, "url", 3 ) == 0 )
			{
				// reserved code
				// yanghaibo
				m_path.fill_none();

				//gradient fill id
				//static int id = 1;
				//sscanf_s( value, "url(#SVGID_%d_)", &id);

				// SVGID_%d_
				std::string id = value + 5;
				id = id.substr(0,id.length()-1); 
				m_path.gradient_fill( id );
			}
            else
            {
                m_path.fill(parse_color(value));
            }
        }
		else
		if(strcmp(name, "fill-rule") == 0)
		{	
			if(strcmp( value, "evenodd" ) == 0 )
			{
				m_path.even_odd( true );
			}
			else
			{
				m_path.even_odd( false );
			}
		}
        else
        if(strcmp(name, "fill-opacity") == 0)
        {
            m_path.fill_opacity(parse_double(value));
        }
		else
		if(strcmp(name, "opacity") == 0)
		{
			// yanghaibo
			double opacity = parse_double(value);

			// both opacique
			m_path.fill_opacity(opacity);
			m_path.stroke_opacity(opacity);
		}
        else
        if(strcmp(name, "stroke") == 0)
        {
            if(strcmp(value, "none") == 0)
            {
                m_path.stroke_none();
            }
			else if ( strncmp( value, "url", 3 ) == 0 )
			{
				// yanghaibo
				// TODO: ²»Ö§³Öpattern stroke!
				//
			}
            else
            {
                m_path.stroke(parse_color(value));
            }
        }
        else
        if(strcmp(name, "stroke-width") == 0)
        {
            m_path.stroke_width(parse_double(value));
        }
        else
        if(strcmp(name, "stroke-linecap") == 0)
        {
            if(strcmp(value, "butt") == 0)        m_path.line_cap(butt_cap);
            else if(strcmp(value, "round") == 0)  m_path.line_cap(round_cap);
            else if(strcmp(value, "square") == 0) m_path.line_cap(square_cap);
        }
        else
        if(strcmp(name, "stroke-linejoin") == 0)
        {
            if(strcmp(value, "miter") == 0)      m_path.line_join(miter_join);
            else if(strcmp(value, "round") == 0) m_path.line_join(round_join);
            else if(strcmp(value, "bevel") == 0) m_path.line_join(bevel_join);
        }
        else
        if(strcmp(name, "stroke-miterlimit") == 0)
        {
            m_path.miter_limit(parse_double(value));
        }
        else
        if(strcmp(name, "stroke-opacity") == 0)
        {
            m_path.stroke_opacity(parse_double(value));
        }
        else
        if(strcmp(name, "transform") == 0)
        {
			transform_parser tp( m_path.transform() );
            tp.parse(value);
        }
		else if(strcmp(name, "font-family") == 0)
		{
			m_path.font_family( value );

		}
		else if(strcmp(name, "font-size") == 0)
		{
			m_path.font_size( parse_double(value) );
		}
		else if ( strcmp(name, "display") == 0)
		{
			if(strcmp(value, "none") == 0)
			{
				m_path.display_mode(display_none);
			}
			else if(strcmp(value, "inline") == 0)  
			{
				m_path.display_mode(display_inline);
			}
			else if(strcmp(value, "block") == 0)  
			{
				m_path.display_mode(display_block);
			}
			else if(strcmp(value, "inherit") == 0)
			{
				m_path.display_mode(display_inherit);
			}
			else 
			{
				// unsupported yet!
			}
		}
        //else
        //if(strcmp(el, "<OTHER_ATTRIBUTES>") == 0) 
        //{
        //}
        // . . .
        else
        {
            return false;
        }
        return true;
    }



    //-------------------------------------------------------------
    void style_parser::copy_name(const char* start, const char* end)
    {
        unsigned len = unsigned(end - start);
        if(m_attr_name_len == 0 || len > m_attr_name_len)
        {
            delete [] m_attr_name;
            m_attr_name = new char[len + 1];
            m_attr_name_len = len;
        }
        if(len) memcpy(m_attr_name, start, len);
        m_attr_name[len] = 0;
    }

    //-------------------------------------------------------------
    void style_parser::copy_value(const char* start, const char* end)
    {
        unsigned len = unsigned(end - start);
        if(m_attr_value_len == 0 || len > m_attr_value_len)
        {
            delete [] m_attr_value;
            m_attr_value = new char[len + 1];
            m_attr_value_len = len;
        }
        if(len) memcpy(m_attr_value, start, len);
        m_attr_value[len] = 0;
    }


    //-------------------------------------------------------------
    bool style_parser::parse_name_value(const char* nv_start, const char* nv_end)
    {
        const char* str = nv_start;
        while(str < nv_end && *str != ':') ++str;

        const char* val = str;

        // Right Trim
        while(str > nv_start && 
            (*str == ':' || isspace(*str))) --str;
        ++str;

        copy_name(nv_start, str);

        while(val < nv_end && (*val == ':' || isspace(*val))) ++val;
        
        copy_value(val, nv_end);
        return parse_attr(m_attr_name, m_attr_value);
    }



    //-------------------------------------------------------------
    void style_parser::parse_style(const char* str)
    {
        while(*str)
        {
            // Left Trim
            while(*str && isspace(*str)) ++str;
            const char* nv_start = str;
            while(*str && *str != ';') ++str;
            const char* nv_end = str;

            // Right Trim
            while(nv_end > nv_start && 
                (*nv_end == ';' || isspace(*nv_end))) --nv_end;
            ++nv_end;

            parse_name_value(nv_start, nv_end);
            if(*str) ++str;
        }

    }


    //-------------------------------------------------------------
    void parser::parse_rect(const char** attr)
    {
        int i;
        double x = 0.0;
        double y = 0.0;
        double w = 0.0;
        double h = 0.0;
		double rx = 0.0;
		double ry = 0.0;
		bool brx = false;
		bool bry = false;
        m_path.begin_path();

        for(i = 0; attr[i]; i += 2)
        {
            if(!parse_attr(attr[i], attr[i + 1]))
            {
                if(strcmp(attr[i], "x") == 0)      x = parse_double(attr[i + 1]);
                else if(strcmp(attr[i], "y") == 0)      y = parse_double(attr[i + 1]);
                else if(strcmp(attr[i], "width") == 0)  w = parse_double(attr[i + 1]);
                else if(strcmp(attr[i], "height") == 0) h = parse_double(attr[i + 1]);
                else if(strcmp(attr[i], "rx") == 0)
				{
					brx = true; 
					rx = parse_double(attr[i + 1]);
				}
				else if(strcmp(attr[i], "ry") == 0)
				{
					bry = true;
					ry = parse_double(attr[i + 1]);
				}
            }
        }

        if(w != 0.0 && h != 0.0)
        {
            if(w < 0.0) throw exception("parse_rect: Invalid width: %f", w);
            if(h < 0.0) throw exception("parse_rect: Invalid height: %f", h);
			if(rx < 0.0) throw exception("parse_rect: Invalid x radii: %f", rx);
			if(ry < 0.0) throw exception("parse_rect: Invalid y radii: %f", ry);
			
			if(brx)
			{
				if ( !bry )
				{
					ry = rx;
				}
			}

			if(bry)
			{
				if( !brx )
				{
					rx = ry;
				}
			}

			if ( rx * 2.0 > w)
			{
				rx = w/2.0;
			}

			if ( ry * 2.0 > h)
			{
				ry = h/2.0;
			}
			
			if ( rx == 0.0 || ry == 0.0 )
			{
				m_path.move_to(x,     y);
				m_path.line_to(x + w, y);
				m_path.line_to(x + w, y + h);
				m_path.line_to(x,     y + h);
				m_path.close_subpath();
			}
			else
			{
				m_path.move_to(x + rx, y);
				
				m_path.hline_to(x+w-rx);

				m_path.ell_arc_to(rx, ry, true);

				m_path.vline_to(y+h-ry);

				m_path.ell_arc_to(-rx, ry, true);

				m_path.hline_to(x+rx);

				m_path.ell_arc_to(-rx,-ry, true);

				m_path.vline_to(y+ry);
				
				m_path.ell_arc_to(rx,-ry, true);

				m_path.close_subpath();
			}
        }

        m_path.end_path();
    }

	void parser::parse_ellipse(const char** attr)
	{
		int i;
		double cx = 0.0;
		double cy = 0.0;
		double rx = 0.0;
		double ry = 0.0;

		m_path.begin_path();

		for(i = 0; attr[i]; i += 2)
		{
			if(!parse_attr(attr[i], attr[i + 1]))
			{
				if(strcmp(attr[i], "cx") == 0) cx = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "cy") == 0) cy = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "rx") == 0) rx = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "ry") == 0) ry = parse_double(attr[i + 1]);
			}
		}

		if(rx < 0.0) throw exception("parse_ellipse: Invalid ellipse x radius: %f", rx);
		if(ry < 0.0) throw exception("parse_ellipse: Invalid ellipse y radius: %f", ry);

		if(rx != 0.0 && ry != 0.0 )
		{
			m_path.ellipse_(cx, cy, rx, ry);
		}

		m_path.end_path();
	}
	//-------------------------------------------------------------
	void parser::parse_circle(const char** attr)
	{
		int i;
		double cx = 0.0;
		double cy = 0.0;
		double r = 0.0;
 
		m_path.begin_path();

		for(i = 0; attr[i]; i += 2)
		{
			if(!parse_attr(attr[i], attr[i + 1]))
			{
				if(strcmp(attr[i], "cx") == 0) cx = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "cy") == 0) cy = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "r") == 0)  r = parse_double(attr[i + 1]);
			}
		}

		if(r != 0.0)
		{
			if(r < 0.0) throw exception("parse_circle: Invalid circle radius: %f", r);
			m_path.circle(cx, cy, r);
		}

		 m_path.end_path();
	}


    //-------------------------------------------------------------
    void parser::parse_line(const char** attr)
    {
        int i;
        double x1 = 0.0;
        double y1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;

        m_path.begin_path();

		// default stroke attribute
		//m_path.stroke( rgba8(0,0,0) ); 
		
        for(i = 0; attr[i]; i += 2)
        {
            if(!parse_attr(attr[i], attr[i + 1]))
            {
                if(strcmp(attr[i], "x1") == 0) x1 = parse_double(attr[i + 1]);
                else if(strcmp(attr[i], "y1") == 0) y1 = parse_double(attr[i + 1]);
                else if(strcmp(attr[i], "x2") == 0) x2 = parse_double(attr[i + 1]);
                else if(strcmp(attr[i], "y2") == 0) y2 = parse_double(attr[i + 1]);
            }
        }


        m_path.move_to(x1, y1);
        m_path.line_to(x2, y2);
        m_path.end_path();
    }


    //-------------------------------------------------------------
    void parser::parse_poly(const char** attr, bool close_flag)
    {
        int i;
        double x = 0.0;
        double y = 0.0;

        m_path.begin_path();

        for(i = 0; attr[i]; i += 2)
        {
            if(!parse_attr(attr[i], attr[i + 1]))
            {
                if(strcmp(attr[i], "points") == 0) 
                {
                    m_tokenizer.set_path_str(attr[i + 1]);
                    if(!m_tokenizer.next())
                    {
                        throw exception("parse_poly: Too few coordinates");
                    }
                    x = m_tokenizer.last_number();
                    if(!m_tokenizer.next())
                    {
                        throw exception("parse_poly: Too few coordinates");
                    }
                    y = m_tokenizer.last_number();
                    m_path.move_to(x, y);
                    while(m_tokenizer.next())
                    {
                        x = m_tokenizer.last_number();
                        if(!m_tokenizer.next())
                        {
                            throw exception("parse_poly: Odd number of coordinates");
                        }
                        y = m_tokenizer.last_number();
                        m_path.line_to(x, y);
                    }
                }
            }
        }
        if(close_flag) 
        {
            m_path.close_subpath();
        }
        m_path.end_path();
    }


	void parser::parse_text(const char** attr)
	{
		int i;
		double x = 0.0;
		double y = 0.0;
	
		for(i = 0; attr[i]; i += 2)
		{
			if(!parse_attr(attr[i], attr[i + 1]))
			{
				if(strcmp(attr[i], "x") == 0) x = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "y") == 0) y = parse_double(attr[i + 1]);
			}
		}

		m_last_x = x;
		m_last_y = y;
	}

	bool gradient_parser::parse(const char ** attr, int i )
	{
		if(strcmp(attr[i], "id") == 0) 
		{
			the_element->id = attr[i + 1];
			return true;
		}
		else if(strcmp(attr[i], "gradientUnits") == 0) 
		{
			if( strcmp( attr[i + 1], "userSpaceOnUse" ) == 0  )
			{
				the_element->units = userSpaceOnUse;
			}
			else if( strcmp( attr[i + 1], "objectBoundingBox" ) == 0  )
			{
				the_element->units = objectBoundingBox;
			}
			return true;
		}
		else if(strcmp(attr[i], "gradientTransform") == 0) 
		{
			transform_parser tp( the_element->trans );
			tp.parse(attr[i+1]);
			return true;
		}
		else if(strcmp(attr[i], "spreadMethod") == 0) 
		{
			if( strcmp( attr[i + 1], "pad" ) == 0  )
			{
				the_element->spread = pad;
			}
			else if( strcmp( attr[i + 1], "reflect" ) == 0  )
			{
				the_element->spread = reflect;
			}
			else if( strcmp( attr[i + 1], "repeat" ) == 0  )
			{
				the_element->spread = repeat;
			}
			return true;
		}
		else if(strcmp(attr[i], "xlink:href") == 0) 
		{
			the_element->xlinkref = attr[i + 1];
			return true;
		}

		return false;
	}

	void parser::parse_linearGradient(const char** attr)
	{
		int i;
		
		svg_gradient_linear* element = new svg_gradient_linear();
		gradient_parser base_parser( element );

		for(i = 0; attr[i]; i += 2)
		{
			if( !base_parser.parse( attr, i ) )
			{
				if(strcmp(attr[i], "x1") == 0) element->x1 = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "y1") == 0) element->y1 = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "x2") == 0) element->x2 = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "y2") == 0) element->y2 = parse_double(attr[i + 1]);
			}
		}

		m_path.push_gradient_elemment( element );
	}

	void parser::parse_radialGradient(const char** attr)
	{
		int i;
		
		svg_gradient_radial* element = new svg_gradient_radial();
		gradient_parser base_parser( element );

		bool bfx = false, bfy = false;
		for(i = 0; attr[i]; i += 2)
		{
			if( !base_parser.parse( attr, i ) )
			{
				if(strcmp(attr[i], "cx") == 0) element->cx = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "cy") == 0) element->cy = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "r") == 0) element->r = parse_double(attr[i + 1]);
				else if(strcmp(attr[i], "fx") == 0) 
				{
					bfx = true;
					element->fx = parse_double(attr[i + 1]);
				}
				else if(strcmp(attr[i], "fy") == 0) 
				{
					element->fy = parse_double(attr[i + 1]);
					bfy = true;
				}
			}
		}

		if ( !bfx )
		{
			element->fx = element->cx;
		}

		if( !bfy )
		{
			element->fy = element->cy;
		}

		m_path.push_gradient_elemment( element );
	}

	void parser::parse_stop(const char** attr)
	{
		stop_point stop;
		stop_parser sub_parser(&stop);
		sub_parser.parse( attr );
		m_stops.push_back(stop);
	}

	bool stop_parser::parse_attr(const char* name, const char* value)
	{
		if(strcmp(name, "stop-color") == 0)
		{
			the_element->stop_color = parse_color(value);
			return true;
		}
		else if(strcmp(name, "stop-opacity") == 0)
		{
			the_element->stop_opacity = parse_double( value );
			return true;
		}
		return false;
	}

	void stop_parser::parse(const char** attr)
	{
		// offset 
		// stop-color
		// stop-opacity
		int i;
		double offset = 0.0;
		for(i = 0; attr[i]; i += 2)
		{	
			if(strcmp(attr[i], "offset") == 0) 
			{
				the_element->offset = parse_double(attr[i + 1]);
			}
			else if(strcmp(attr[i], "stop-color") == 0) 
			{
				the_element->stop_color = parse_color(attr[i + 1]);
			}
			else if(strcmp(attr[i], "stop-opacity") == 0) 
			{
				the_element->stop_opacity = parse_double( attr[i + 1] );
			}
			else if(strcmp(attr[i], "style") == 0) 
			{
				parse_style(attr[i + 1]);
			}
		}
	}

    //-------------------------------------------------------------


	//-------------------------------------------------------------
	void transform_parser::parse(const char* str)
	{
		while(*str)
		{
			if(islower(*str))
			{
				if(strncmp(str, "matrix", 6) == 0)    str += parse_matrix(str);    else 
				if(strncmp(str, "translate", 9) == 0) str += parse_translate(str); else 
				if(strncmp(str, "rotate", 6) == 0)    str += parse_rotate(str);    else 
				if(strncmp(str, "scale", 5) == 0)     str += parse_scale(str);     else 
				if(strncmp(str, "skewX", 5) == 0)     str += parse_skew_x(str);    else 
				if(strncmp(str, "skewY", 5) == 0)     str += parse_skew_y(str);    else
				{
					++str;
				}
			}
			else
			{
				++str;
			}
		}
	}


	
    //-------------------------------------------------------------
    static unsigned parse_transform_args(const char* str, 
                                         double* args, 
                                         unsigned max_na, 
                                         unsigned* na)
    {
        *na = 0;
        const char* ptr = str;
        while(*ptr && *ptr != '(') ++ptr;
        if(*ptr == 0)
        {
            throw exception("parse_transform_args: Invalid syntax");
        }
        const char* end = ptr;
        while(*end && *end != ')') ++end;
        if(*end == 0)
        {
            throw exception("parse_transform_args: Invalid syntax");
        }

        while(ptr < end)
        {
            if(is_numeric(*ptr))
            {
                if(*na >= max_na)
                {
                    throw exception("parse_transform_args: Too many arguments");
                }
                args[(*na)++] = atof(ptr);
                while(ptr < end && is_numeric(*ptr)) ++ptr;
            }
            else
            {
                ++ptr;
            }
        }
        return unsigned(end - str);
    }

    //-------------------------------------------------------------
    unsigned transform_parser::parse_matrix(const char* str)
    {
        double args[6];
        unsigned na = 0;
        unsigned len = parse_transform_args(str, args, 6, &na);
        if(na != 6)
        {
            throw exception("parse_matrix: Invalid number of arguments");
        }
        the_transformer.premultiply(trans_affine(args[0], args[1], args[2], args[3], args[4], args[5]));
        return len;
    }

    //-------------------------------------------------------------
    unsigned transform_parser::parse_translate(const char* str)
    {
        double args[2];
        unsigned na = 0;
        unsigned len = parse_transform_args(str, args, 2, &na);
        if(na == 1) args[1] = 0.0;
        the_transformer.premultiply(trans_affine_translation(args[0], args[1]));
        return len;
    }

    //-------------------------------------------------------------
    unsigned transform_parser::parse_rotate(const char* str)
    {
        double args[3];
        unsigned na = 0;
        unsigned len = parse_transform_args(str, args, 3, &na);
        if(na == 1) 
        {
            the_transformer.premultiply(trans_affine_rotation(deg2rad(args[0])));
        }
        else if(na == 3)
        {
            trans_affine t = trans_affine_translation(-args[1], -args[2]);
            t *= trans_affine_rotation(deg2rad(args[0]));
            t *= trans_affine_translation(args[1], args[2]);
            the_transformer.premultiply(t);
        }
        else
        {
            throw exception("parse_rotate: Invalid number of arguments");
        }
        return len;
    }

    //-------------------------------------------------------------
    unsigned transform_parser::parse_scale(const char* str)
    {
        double args[2];
        unsigned na = 0;
        unsigned len = parse_transform_args(str, args, 2, &na);
        if(na == 1) args[1] = args[0];
        the_transformer.premultiply(trans_affine_scaling(args[0], args[1]));
        return len;
    }

    //-------------------------------------------------------------
    unsigned transform_parser::parse_skew_x(const char* str)
    {
        double arg;
        unsigned na = 0;
        unsigned len = parse_transform_args(str, &arg, 1, &na);
        the_transformer.premultiply(trans_affine_skewing(deg2rad(arg), 0.0));
        return len;
    }

    //-------------------------------------------------------------
    unsigned transform_parser::parse_skew_y(const char* str)
    {
        double arg;
        unsigned na = 0;
        unsigned len = parse_transform_args(str, &arg, 1, &na);
        the_transformer.premultiply(trans_affine_skewing(0.0, deg2rad(arg)));
        return len;
    }

}
}


