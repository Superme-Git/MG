//////////////////////////////////////////////////////////////////////////
// File:		CEGUIStream.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUIStream.h"
#include "CEGUIcolour.h"
#include "CEGUIColourRect.h"
#include "CEGUIRect.h"
#include "CEGUIString.h"
#include "CEGUIVertex.h"

namespace CEGUI
{
	namespace BinLayout
	{
#define TMP_BUFFER_SIZE 10240

		Stream& Stream::operator >> (std::string& val)
		{
			int readSize = 0;
			readT(readSize);

			if (readSize > 0)
			{
				if (readSize < TMP_BUFFER_SIZE)
				{
					char tmpBuffer[TMP_BUFFER_SIZE] = { 0 };
					read(tmpBuffer, readSize);
					val = tmpBuffer;
				}
				else
				{
					char* tmpBuffer = new char[readSize + 1];

					read(tmpBuffer, readSize);
					tmpBuffer[readSize] = '\0';
					val = tmpBuffer;

					delete[] tmpBuffer;
					tmpBuffer = NULL;
				}
			}

			return *this;
		}

		Stream& Stream::operator << (const std::string& val)
		{
			int writeSize = val.size();
			writeT(writeSize);

			if (writeSize > 0)
			{
				write(val.c_str(), writeSize);
			}

			return *this;
		}

		Stream& Stream::operator >> (colour& val)
		{
			float r, g, b, a;
			*this >> r >> g >> b >> a;

			val.set(r, g, b, a);

			return *this;
		}

		Stream& Stream::operator << (const colour& val)
		{
			float r = val.getRed();
			float g = val.getGreen();
			float b = val.getBlue();
			float a = val.getAlpha();
			*this << r << g << b << a;

			return *this;
		}

		Stream& Stream::operator >> (ColourRect& val)
		{
			*this >> val.d_top_left >> val.d_top_right >> val.d_bottom_left >> val.d_bottom_right;
			return *this;
		}

		Stream& Stream::operator << (const ColourRect& val)
		{
			*this << val.d_top_left << val.d_top_right << val.d_bottom_left << val.d_bottom_right;
			return *this;
		}

		Stream& Stream::operator >> (Rect& val)
		{
			*this >> val.d_left >> val.d_top >> val.d_right >> val.d_bottom;
			return *this;
		}

		Stream& Stream::operator << (const Rect& val)
		{
			*this << val.d_left << val.d_top << val.d_right << val.d_bottom;
			return *this;
		}

		Stream& Stream::operator >> (Size& val)
		{
			*this >> val.d_width >> val.d_height;
			return *this;
		}

		Stream& Stream::operator << (const Size& val)
		{
			*this << val.d_width << val.d_height;
			return *this;
		}

		Stream& Stream::operator >> (String& val)
		{
			std::string str;
			*this >> str;  // ¶Á³öÀ´µÄÊÇ utf8 ±àÂëµÄ×Ö·û´®

			val = (utf8*)str.c_str();  // °´ utf8 ×Ö·û´®¸³¸ø val

			return *this;
		}

		Stream& Stream::operator << (const String& val)
		{
			// Ð´Èë utf8 ±àÂëµÄ×Ö·û´®
			std::string str = val.c_str();
			*this << str;
			return *this;
		}

		Stream& Stream::operator >> (UDim& val)
		{
			*this >> val.d_scale >> val.d_offset;
			return *this;
		}

		Stream& Stream::operator << (const UDim& val)
		{
			*this << val.d_scale << val.d_offset;
			return *this;
		}

		Stream& Stream::operator >> (UVector2& val)
		{
			*this >> val.d_x >> val.d_y;
			return *this;
		}

		Stream& Stream::operator << (const UVector2& val)
		{
			*this << val.d_x << val.d_y;
			return *this;
		}

		Stream& Stream::operator >> (URect& val)
		{
			*this >> val.d_min >> val.d_max;
			return *this;
		}

		Stream& Stream::operator << (const URect& val)
		{
			*this << val.d_min << val.d_max;
			return *this;
		}

		Stream& Stream::operator >> (UBox& val)
		{
			*this >> val.d_top >> val.d_left >> val.d_bottom >> val.d_right;
			return *this;
		}

		Stream& Stream::operator << (const UBox& val)
		{
			*this << val.d_top << val.d_left << val.d_bottom << val.d_right;
			return *this;
		}

		Stream& Stream::operator >> (Vector2& val)
		{
			*this >> val.d_x >> val.d_y;
			return *this;
		}

		Stream& Stream::operator << (const Vector2& val)
		{
			*this << val.d_x << val.d_y;
			return *this;
		}

		Stream& Stream::operator >> (Vector3& val)
		{
			*this >> val.d_x >> val.d_y >> val.d_z;
			return *this;
		}

		Stream& Stream::operator << (const Vector3& val)
		{
			*this << val.d_x << val.d_y << val.d_z;
			return *this;
		}

		Stream& Stream::operator >> (Vertex& val)
		{
			*this >> val.position >> val.tex_coords >> val.colour_val;
			return *this;
		}

		Stream& Stream::operator << (const Vertex& val)
		{
			*this << val.position << val.tex_coords << val.colour_val;
			return *this;
		}
	}
}