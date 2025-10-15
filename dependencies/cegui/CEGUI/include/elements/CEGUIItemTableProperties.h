/***********************************************************************
	filename: 	CEGUIItemTableProperties.h
*************************************************************************/

#ifndef _CEGUIItemTableProperties_h_
#define _CEGUIItemTableProperties_h_

#include "../CEGUIProperty.h"

// Start of CEGUI namespace section
namespace CEGUI
{

namespace ItemTableProperties
{
	class RowCount : public Property
	{
	public:
		RowCount() : Property(
			"RowCount",
			"Property to get/set the ItemTable RowCount",
			"0")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};

	class ColCount : public Property
	{
	public:
		ColCount() : Property(
			"ColCount",
			"Property to get/set the ItemTable ColCount",
			"0")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};

	class CellWidth : public Property
	{
	public:
		CellWidth() : Property(
			"CellWidth",
			"Property to get/set the ItemTable CellWidth",
			"77")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};

	class CellHeight : public Property
	{
	public:
		CellHeight() : Property(
			"CellHeight",
			"Property to get/set the ItemTable CellHeight",
			"77")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};

	class SpaceX : public Property
	{
	public:
		SpaceX() : Property(
			"SpaceX",
			"Property to get/set the ItemTable SpaceX",
			"2")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};

	class SpaceY : public Property
	{
	public:
		SpaceY() : Property(
			"SpaceY",
			"Property to get/set the ItemTable SpaceY",
			"2")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};

	class StartX : public Property
	{
	public:
		StartX() : Property(
			"StartX",
			"Property to get/set the ItemTable StartX",
			"0")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};

	class StartY : public Property
	{
	public:
		StartY() : Property(
			"StartY",
			"Property to get/set the ItemTable StartY",
			"0")
		{}

		String  get(const PropertyReceiver* receiver) const;
		void    set(PropertyReceiver* receiver, const String& value);
	};

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
} // End of  DragContainerProperties namespace section
} // End of  CEGUI namespace section

#endif	
