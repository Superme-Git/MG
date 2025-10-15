/***********************************************************************
filename: 	CEGUIGroupButtonProperties.h
*************************************************************************/

#ifndef _CEGUIGroupButtonProperties_h_
#define _CEGUIGroupButtonProperties_h_

#include "../CEGUIProperty.h"


// Start of CEGUI namespace section
namespace CEGUI
{

	// Start of GroupButtonProperties namespace section
	/*!
	\brief
	Namespace containing all classes that make up the properties interface for the GroupButton class
	*/
	namespace GroupButtonProperties
	{
		/*!
		\brief
		Property to access the selected state of the GroupButton.

		\par Usage:
		- Name: Selected
		- Format: "[text]".

		\par Where [Text] is:
		- "True" to indicate the group button is selected.
		- "False" to indicate the group button is not selected.
		*/
		class Selected : public Property
		{
		public:
			Selected() : Property(
				"Selected",
				"Property to get/set the selected state of the GroupButton.  Value is either \"True\" or \"False\".",
				"False")
			{}

			String	get(const PropertyReceiver* receiver) const;
			void	set(PropertyReceiver* receiver, const String& value);
		};


		/*!
		\brief
		Property to access the group button group ID.

		\par Usage:
		- Name: GroupID
		- Format: "[uint]".

		\par Where:
		- [uint] is any unsigned integer value.
		*/
		class GroupID : public Property
		{
		public:
			GroupID() : Property(
				"GroupID",
				"Property to get/set the radio button group ID.  Value is an unsigned integer number.",
				"0")
			{}

			String	get(const PropertyReceiver* receiver) const;
			void	set(PropertyReceiver* receiver, const String& value);
		};

		/*!
		\brief
		Property to access the state image extend ID.

		\par Usage:
		- Name: StateImageExtendID
		- Format: "[int]".

		\par Where:
		- [uint] is any integer value.
		*/
		class StateImageExtendID : public Property
		{
		public:
			StateImageExtendID() : Property(
				"StateImageExtendID",
				"Property to get/set the state image extend ID.  Value is an integer number.",
				"0")
			{}

			String	get(const PropertyReceiver* receiver) const;
			void	set(PropertyReceiver* receiver, const String& value);
		};


		/*!
		\brief
		Property to access the group button EnableClickAni.

		\par Usage:
		- Name: EnableClickAni
		- Format: "[bool]".

		\par Where:
		- [bool] is bool value.
		*/
		class EnableClickAni : public Property
		{
		public:
			EnableClickAni() : Property(
				"EnableClickAni",
				"Property to get/set the EnableClickAni of the GroupButton.  Value is either \"True\" or \"False\".",
				"0")
			{}

			String	get(const PropertyReceiver* receiver) const;
			void	set(PropertyReceiver* receiver, const String& value);
		};

	} // End of  GroupButtonProperties namespace section

} // End of  CEGUI namespace section


#endif	// end of guard _CEGUIGroupButtonProperties_h_
