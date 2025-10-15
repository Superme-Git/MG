#include "elements/CEGUIItemCellProperties.h"
#include "elements/CEGUIItemCell.h"
#include "CEGUIPropertyHelper.h"
#include <cstdlib>
#include <cstdio>

namespace CEGUI
{
	namespace ItemCellProperties
	{
		String ItemCellStyle::get(const PropertyReceiver* receiver) const
		{
			switch (static_cast<const ItemCell*>(receiver)->GetStyle())
			{
			case ItemCellStyle_IconInside:
				return String("IconInside");
				break;

			case ItemCellStyle_IconExtend:
				return String("IconExtend");
				break;

			default:
				return String("IconInside");
			}
		}

		void ItemCellStyle::set(PropertyReceiver* receiver, const String& value)
		{
			CEGUI::ItemCellStyle style;

			if (value == "IconInside")
			{
				style = ItemCellStyle_IconInside;
			}
			else if (value == "IconExtend")
			{
				style = ItemCellStyle_IconExtend;
			}

			static_cast<ItemCell*>(receiver)->SetStyle(style);
		}

	} //End of  ItemCellProperties namespace section
}// End of  CEGUI namespace section