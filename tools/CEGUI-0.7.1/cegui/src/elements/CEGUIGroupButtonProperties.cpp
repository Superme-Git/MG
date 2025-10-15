/***********************************************************************
filename: 	CEGUIGroupButtonProperties.cpp
*************************************************************************/
#include "elements/CEGUIGroupButtonProperties.h"
#include "elements/CEGUIGroupButton.h"
#include "CEGUIPropertyHelper.h"

// Start of CEGUI namespace section
namespace CEGUI
{

	// Start of GroupButtonProperties namespace section
	namespace GroupButtonProperties
	{
		String Selected::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::boolToString(static_cast<const GroupButton*>(receiver)->isSelected());
		}


		void Selected::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<GroupButton*>(receiver)->setSelected(PropertyHelper::stringToBool(value));
		}


		String GroupID::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::uintToString(static_cast<const GroupButton*>(receiver)->getGroupID());
		}


		void GroupID::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<GroupButton*>(receiver)->setGroupID(PropertyHelper::stringToUint(value));
		}


		String StateImageExtendID::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::intToString(static_cast<const GroupButton*>(receiver)->GetStateImageExtendID());
		}


		void StateImageExtendID::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<GroupButton*>(receiver)->SetStateImageExtendID(PropertyHelper::stringToInt(value));
		}


		String EnableClickAni::get(const PropertyReceiver* receiver) const
		{
			return PropertyHelper::boolToString(static_cast<const GroupButton*>(receiver)->isClickAniEnable());
		}


		void EnableClickAni::set(PropertyReceiver* receiver, const String& value)
		{
			static_cast<GroupButton*>(receiver)->EnableClickAni(PropertyHelper::stringToBool(value));
		}

	} // End of  RadioButtonProperties namespace section

} // End of  CEGUI namespace section
