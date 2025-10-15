#include "elements/CEGUIItemTableProperties.h"
#include "elements/CEGUIItemTable.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIExceptions.h"
#include <cstdlib>
#include <cstdio>


// Start of CEGUI namespace section
namespace CEGUI
{

	// Start of CheckboxProperties namespace section
	namespace ItemTableProperties
	{
		String RowCount::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString((int)static_cast<const ItemTable*>(receiver)->GetRowCount());
		}


		void RowCount::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<ItemTable*>(receiver)->SetRowCount((size_t)PropertyHelper::stringToInt(value));
		}

		String ColCount::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString((int)static_cast< const ItemTable*>(receiver)->GetColCount());
		}


		void ColCount::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<ItemTable*>(receiver)->SetColCount((size_t)PropertyHelper::stringToInt(value));
		}

		String CellWidth::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString((int)static_cast<const ItemTable*>(receiver)->GetCellWidth());
		}


		void CellWidth::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<ItemTable*>(receiver)->SetCellWidth(PropertyHelper::stringToInt(value));
		}

		String CellHeight::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString((int)static_cast<const ItemTable*>(receiver)->GetCellHeight());
		}


		void CellHeight::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<ItemTable*>(receiver)->SetCellHeight(PropertyHelper::stringToInt(value));
		}

		String SpaceX::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString((int)static_cast<const ItemTable*>(receiver)->GetSpaceX());
		}


		void SpaceX::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<ItemTable*>(receiver)->SetSpaceX(PropertyHelper::stringToInt(value));
		}

		String SpaceY::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString((int)static_cast<const ItemTable*>(receiver)->GetSpaceY());
		}


		void SpaceY::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<ItemTable*>(receiver)->SetSpaceY(PropertyHelper::stringToInt(value));
		}

		String StartX::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString((int)static_cast<const ItemTable*>(receiver)->GetStartX());
		}

		void StartX::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<ItemTable*>(receiver)->SetStartX(PropertyHelper::stringToInt(value));
		}

		String StartY::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString((int)static_cast<const ItemTable*>(receiver)->GetStartY());
		}

		void StartY::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<ItemTable*>(receiver)->SetStartY(PropertyHelper::stringToInt(value));
		}

		String ItemCellStyle::get(const PropertyReceiver* receiver) const
		{
			switch (static_cast<const ItemTable*>(receiver)->GetItemCellStyle())
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

			static_cast<ItemTable*>(receiver)->SetItemCellStyle(style);
		}

	} // End of  CheckboxProperties namespace section

} // End of  CEGUI namespace section