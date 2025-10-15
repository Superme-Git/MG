/***********************************************************************
	filename: 	CEGUIItemTableProperties.h
*************************************************************************/

#ifndef _CEGUIItemCellProperties_h_
#define _CEGUIItemCellProperties_h_

#include "../CEGUIProperty.h"

// Start of CEGUI namespace section
namespace CEGUI
{

namespace ItemCellProperties
{
	class ItemCellStyle : public Property
	{
	public:
		ItemCellStyle() : Property(
			"ItemCellStyle",
			"Property to get/set the ItemTable ItemCellStyle",
			"IconInside")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};


}
} // End of  CEGUI namespace section

#endif	
