//////////////////////////////////////////////////////////////////////////
// File:		CEGUIPropertyIds.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/03/02
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUIPropertyIds.h"
#include "CEGUIExceptions.h"

namespace CEGUI
{
	namespace BinLayout
	{
		String PropertyIdUtil::getPropNameById(int id)
		{
			if (0 <= id && id < PI_COUNT)
			{
				return PROP_NAMES[id];
			}
			else
			{
				throw InvalidRequestException(String("Invalid id: ") + id);
				return "";
			}
		}

		int PropertyIdUtil::getPropIdByName(const String& name)
		{
			for (int i = 0; i < PI_COUNT; ++i)
			{
				if (name == PROP_NAMES[i])
				{
					return i;
				}
			}

			return PI_PropertyDefinition;
		}
	}
}