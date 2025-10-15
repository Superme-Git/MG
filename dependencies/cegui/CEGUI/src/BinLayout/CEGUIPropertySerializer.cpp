//////////////////////////////////////////////////////////////////////////
// File:		CEGUIPropertySerializer.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUIPropertySerializer.h"
#include "BinLayout/CEGUISerializerManager.h"
#include "CEGUIPropertyHelper.h"

namespace CEGUI
{
	namespace BinLayout
	{
		PropertySerializer::PropertySerializer(int propId, int version)
		{
			SerializerManager::getSingleton().addPropertySerializer(propId, version, this);
		}

		void PropertySerializer::stringTo(const String& str, float& dst)
		{
			dst = PropertyHelper::stringToFloat(str);
		}

		void PropertySerializer::stringTo(const String& str, uint& dst)
		{
			dst = PropertyHelper::stringToUint(str);
		}

		void PropertySerializer::stringTo(const String& str, int& dst)
		{
			dst = PropertyHelper::stringToInt(str);
		}

		void PropertySerializer::stringTo(const String& str, int64_t& dst)
		{
			dst = PropertyHelper::stringToInt64(str);
		}

		void PropertySerializer::stringTo(const String& str, bool& dst)
		{
			dst = PropertyHelper::stringToBool(str);
		}

		void PropertySerializer::stringTo(const String& str, Size& dst)
		{
			dst = PropertyHelper::stringToSize(str);
		}

		void PropertySerializer::stringTo(const String& str, Point& dst)
		{
			dst = PropertyHelper::stringToPoint(str);
		}

		void PropertySerializer::stringTo(const String& str, Vector3& dst)
		{
			dst = PropertyHelper::stringToVector3(str);
		}

		void PropertySerializer::stringTo(const String& str, Rect& dst)
		{
			dst = PropertyHelper::stringToRect(str);
		}

		void PropertySerializer::stringTo(const String& str, colour& dst)
		{
			dst = PropertyHelper::stringToColour(str);
		}

		void PropertySerializer::stringTo(const String& str, ColourRect& dst)
		{
			dst = PropertyHelper::stringToColourRect(str);
		}

		void PropertySerializer::stringTo(const String& str, UDim& dst)
		{
			dst = PropertyHelper::stringToUDim(str);
		}

		void PropertySerializer::stringTo(const String& str, UVector2& dst)
		{
			dst = PropertyHelper::stringToUVector2(str);
		}

		void PropertySerializer::stringTo(const String& str, URect& dst)
		{
			dst = PropertyHelper::stringToURect(str);
		}

		void PropertySerializer::stringTo(const String& str, String& dst)
		{
			dst = str;
		}

		String PropertySerializer::toString(float dst)
		{
			return PropertyHelper::floatToString(dst);
		}

		String PropertySerializer::toString(uint dst)
		{
			return PropertyHelper::uintToString(dst);
		}

		String PropertySerializer::toString(int dst)
		{
			return PropertyHelper::intToString(dst);
		}

		String PropertySerializer::toString(int64_t dst)
		{
			return PropertyHelper::int64_tToString(dst);
		}

		String PropertySerializer::toString(bool dst)
		{
			return PropertyHelper::boolToString(dst);
		}

		String PropertySerializer::toString(const Size& dst)
		{
			return PropertyHelper::sizeToString(dst);
		}

		String PropertySerializer::toString(const Point& dst)
		{
			return PropertyHelper::pointToString(dst);
		}

		String PropertySerializer::toString(const Vector3& dst)
		{
			return PropertyHelper::vector3ToString(dst);
		}

		String PropertySerializer::toString(const Rect& dst)
		{
			return PropertyHelper::rectToString(dst);
		}

		String PropertySerializer::toString(const colour& dst)
		{
			return PropertyHelper::colourToString(dst);
		}

		String PropertySerializer::toString(const ColourRect& dst)
		{
			return PropertyHelper::colourRectToString(dst);
		}

		String PropertySerializer::toString(const UDim& dst)
		{
			return PropertyHelper::udimToString(dst);
		}

		String PropertySerializer::toString(const UVector2& dst)
		{
			return PropertyHelper::uvector2ToString(dst);
		}

		String PropertySerializer::toString(const URect& dst)
		{
			return PropertyHelper::urectToString(dst);
		}

		String PropertySerializer::toString(const String& dst)
		{
			return dst;
		}
	}
}