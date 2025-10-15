#ifndef SVG_BASIC_H_INCLUDED
#define SVG_BASIC_H_INCLUDED

#include "agg_pixfmt_rgb.h"
#include <string.h>
#include <math.h>
#include "agg_math.h"

#include "svg_ascii.h"

typedef agg::pixfmt_bgr24 pixfmt_type;
typedef pixfmt_type::color_type color_type;

namespace agg
{
namespace svg
{
	enum GradientType
	{
		gt_gradient_radial,
		gt_gradient_linear,
	};

	// pad | reflect | repeat
	enum SpreadMethod
	{
		pad, // default value if not specified
		reflect,
		repeat,
	};

	// userSpaceOnUse | objectBoundingBox
	enum GradientUnits
	{
		userSpaceOnUse,
		objectBoundingBox, // default value 
	};


	typedef enum svg_length_unit
	{
		SVG_LENGTH_PERCENTAGE,
		SVG_LENGTH_UNIT_CM,
		SVG_LENGTH_UNIT_EM,
		SVG_LENGTH_UNIT_EX,
		SVG_LENGTH_UNIT_IN,
		SVG_LENGTH_UNIT_MM,
		SVG_LENGTH_UNIT_PC,
		SVG_LENGTH_UNIT_PCT,
		SVG_LENGTH_UNIT_PT,
		SVG_LENGTH_UNIT_PX
	} svg_length_unit_t;


	typedef struct svg_length
	{
		double				value;
		svg_length_unit_t	unit;
	} svg_length_t;

	inline bool _svg_length_init_unit (
		svg_length_t		*length,
		double				value,
		svg_length_unit_t	unit)
	{
		length->value = value;
		length->unit = unit;
		return true;
	}

	inline bool _svg_length_init (svg_length_t *length, double value)
	{
		return _svg_length_init_unit (length, value, SVG_LENGTH_UNIT_PX );
	}	


	inline bool _svg_length_init_from_str (svg_length_t *length, const char *str)
	{
		double value;
		svg_length_unit_t unit;
		const char *unit_str;

		value = _svg_ascii_strtod (str, &unit_str);

		if (unit_str == str)
			return false;

		if (unit_str) {
			if (strcmp (unit_str, "%") == 0)
				unit = SVG_LENGTH_PERCENTAGE;
			if (strcmp (unit_str, "px") == 0)
				unit = SVG_LENGTH_UNIT_PX;
			else if (strcmp (unit_str, "pt") == 0)
				unit = SVG_LENGTH_UNIT_PT;
			else if (strcmp (unit_str, "in") == 0)
				unit = SVG_LENGTH_UNIT_IN;
			else if (strcmp (unit_str, "cm") == 0)
				unit = SVG_LENGTH_UNIT_CM;
			else if (strcmp (unit_str, "mm") == 0)
				unit = SVG_LENGTH_UNIT_MM;
			else if (strcmp (unit_str, "pc") == 0)
				unit = SVG_LENGTH_UNIT_PC;
			else if (strcmp (unit_str, "em") == 0)
				unit = SVG_LENGTH_UNIT_EM;
			else if (strcmp (unit_str, "ex") == 0)
				unit = SVG_LENGTH_UNIT_EX;
			else if (strcmp (unit_str, "%") == 0)
				unit = SVG_LENGTH_UNIT_PCT;
			else {
				unit = SVG_LENGTH_UNIT_PX;
			}
		} else {
			unit = SVG_LENGTH_UNIT_PX;
		}

		return _svg_length_init_unit (length, value, unit);
	}

	inline void _svg_length_deinit (svg_length_t *length)
	{
		length->value = 0.0;
		length->unit = SVG_LENGTH_UNIT_PX;
	}

	inline double _get_dpi()
	{
		return 96.0;
	}

	inline double _svg_length_to_px( svg_length_t *length, unsigned dpi )
	{
		if ( length->value == 0.0 )
		{
			return 0;
		}

		// As in CSS, the em and ex unit identifiers are relative to the current font's font-size 
		// and x-height, respectively.

		// if 90 dpi
		//* "1pt" equals "1.25px" (and therefore 1.25 user units)
		//* "1pc" equals "15px" (and therefore 15 user units)
		//* "1mm" would be "3.543307px" (3.543307 user units)
		//* "1cm" equals "35.43307px" (and therefore 35.43307 user units)
		//* "1in" equals "90px" (and therefore 90 user units)

		if ( length->unit == agg::svg::SVG_LENGTH_UNIT_PX )
		{
			return length->value;
		}
		else if ( length->unit == agg::svg::SVG_LENGTH_UNIT_CM )
		{
			return length->value * 35.43307 * dpi/90;
		}
		else if ( length->unit == agg::svg::SVG_LENGTH_UNIT_MM )
		{
			return length->value * 3.543307* dpi/90;
		}
		else if ( length->unit == agg::svg::SVG_LENGTH_UNIT_PC )
		{
			return length->value * 15 * dpi / 90 ;
		}
		else if ( length->unit == agg::svg::SVG_LENGTH_UNIT_IN )
		{
			return length->value * dpi;
		}
		//	SVG_LENGTH_UNIT_PCT,
		//	SVG_LENGTH_UNIT_PT,
		// TODO: for em ex

		return length->value;
	}

	struct stop_point
	{
		double offset;
		color_type stop_color;
		double stop_opacity;
		stop_point()
		{
			offset = 0.0;
			stop_color = color_type(0,0,0);
			stop_opacity = 1.0;
		}
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

		current_stop_color = stops[0].stop_color;

		// loops for all stops
		for (unsigned  s = 0; s < nStops; s ++ )
		{
			next_stop_color = stops[s].stop_color;

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


	template<class ColorArrayT, class StopsArrayT>
	static void fill_color_array_by_last_stop(
		ColorArrayT& array, 
		StopsArrayT& stops,
		unsigned nStops,
		unsigned span)
	{
		color_type current_stop_color;
		if( nStops > 0  )
			current_stop_color = stops[nStops-1].stop_color;

		for ( unsigned i = 0; i < span; i ++ )
		{
			array[i] = current_stop_color;
		}
	}


	// TODO:
	typedef std::string element_id;
	typedef std::string gradient_id;
	typedef std::string text_data;

	class svg_gradient_element
	{
	public:
		svg_gradient_element()
		{
			spread = pad;
			units = objectBoundingBox;
			trans.reset();
		}

		virtual ~svg_gradient_element()
		{
			trans.reset();
		}

		typedef std::vector<stop_point> StopsArrayT;
		gradient_id		id;
		GradientType	type;
		trans_affine	trans;
		std::string		xlinkref;
		StopsArrayT		stops;
		SpreadMethod	spread;
		GradientUnits	units;
	};

	class svg_gradient_radial : public svg_gradient_element
	{
	public:
		double cx;
		double cy;
		double r;
		double fx;
		double fy;

		svg_gradient_radial()
		{
			cx = 0.5;
			cy = 0.5;
			r = 0.5;
			fx = 0.5;
			fy = 0.5;
			type = gt_gradient_radial;
		}
	};

	class svg_gradient_linear: public svg_gradient_element
	{
	public:
		double x1;
		double y1;
		double x2;
		double y2;

		svg_gradient_linear()
		{
			x1 = 0.0;
			y1 = 0.0;
			x2 = 1.0;
			y2 = 0.0;
			type = gt_gradient_linear;
		}
	};

	typedef std::map< gradient_id, svg_gradient_element*> svg_gradient_elements;

}; // namespace svg

}; // namespace agg

#endif // SVG_BASIC_H_INCLUDED