/***********************************************************************
	filename: 	CEGUIRichEditBoxProperties.h
	purpose:	Interface to properties for the MultiLineEditbox class
*************************************************************************/

#ifndef _CEGUIRichEditboxProperties_h_
#define _CEGUIRichEditboxProperties_h_

#include "../CEGUIProperty.h"


// Start of CEGUI namespace section
namespace CEGUI
{

// Start of MultiLineEditboxProperties namespace section
/*!
\brief
	Namespace containing all classes that make up the properties interface for the MultiLineEditbox class
*/
namespace RichEditboxProperties
{
/*!
\brief
	Property to access the read-only setting of the edit box.

	\par Usage:
		- Name: ReadOnly
		- Format: "[text]"

	\par Where [Text] is:
		- "True" to indicate the edit box is read-only.
		- "False" to indicate the edit box is not read-only (text may be edited by user).
*/
class ReadOnly : public Property
{
public:
	ReadOnly() : Property(
		"ReadOnly",
		"Property to get/set the read-only setting for the edit box.  Value is either \"True\" or \"False\".",
		"False")
	{}

	String	get(const PropertyReceiver* receiver) const;
	void	set(PropertyReceiver* receiver, const String& value);
};


/*!
\brief
	Property to access the word-wrap setting of the edit box.

	\par Usage:
		- Name: WordWrap
		- Format: "[text]"

	\par Where [Text] is:
		- "True" to indicate the text should be word-wrapped.
		- "False" to indicate the text should not be word-wrapped.
*/
class WordWrap : public Property
{
public:
	WordWrap() : Property(
		"WordWrap",
		"Property to get/set the word-wrap setting of the edit box.  Value is either \"True\" or \"False\".",
		"True")
	{}

	String	get(const PropertyReceiver* receiver) const;
	void	set(PropertyReceiver* receiver, const String& value);
};


class MaxTextLength : public Property
{
public:
	MaxTextLength() : Property(
		"MaxTextLength",
		"Property to get/set the the maximum allowed text length (as a count of code points).  Value is \"[uint]\".",
		"1073741824")
	{}

	String	get(const PropertyReceiver* receiver) const;
	void	set(PropertyReceiver* receiver, const String& value);
};


/*!
\brief
Property to access the selection brush image

\par Usage:
    - Name: SelectionBrushImage
    - Format: "set:<imageset> image:<imagename>".

*/
class SelectionBrushImage : public Property
{
public:
    SelectionBrushImage() : Property(
        "SelectionBrushImage",
        "Property to get/set the selection brush image for the editbox.  Value should be \"set:[imageset name] image:[image name]\".",
        "")
    {}

    String   get(const PropertyReceiver* receiver) const;
    void   set(PropertyReceiver* receiver, const String& value);
};

/*!
\brief
	Property to access the 'always show' setting for the vertical scroll bar of the box.

	\par Usage:
		- Name: ForceVertScrollbar
		- Format: "[text]"

	\par Where [Text] is:
		- "True" to indicate that the vertical scroll bar will always be shown.
		- "False" to indicate that the vertical scroll bar will only be shown when it is needed.
*/
class ForceVertScrollbar : public Property
{
public:
	ForceVertScrollbar() : Property(
		"ForceVertScrollbar",
		"Property to get/set the 'always show' setting for the vertical scroll bar of the list box.  Value is either \"True\" or \"False\".",
		"False")
	{}

	String	get(const PropertyReceiver* receiver) const;
	void	set(PropertyReceiver* receiver, const String& value);
};

class BackGroundEnable : public Property
{
public:
	BackGroundEnable() : Property(
		"BackGroundEnable",
		"Property to get/set the 'always show' setting for the vertical scroll bar of the list box.  Value is either \"True\" or \"False\".",
		"True")
	{}

	String	get(const PropertyReceiver* receiver) const;
	void	set(PropertyReceiver* receiver, const String& value);
};

class LineSpace : public Property
{
public:
	LineSpace() : Property(
		"LineSpace",
		"Property to get/set the 'always show' setting for the vertical scroll bar of the list box.  Value is either \"True\" or \"False\".",
		"1.0")
	{}

	String	get(const PropertyReceiver* receiver) const;
	void	set(PropertyReceiver* receiver, const String& value);
};
    
/*!
 \brief
 Property to access the top-after-load-font setting of the edit box.
 
 \par Usage:
 - Name: TopAfterLoadFont
 - Format: "[text]"
 
 \par Where [Text] is:
 - "True" to indicate the edit box will handletop after load font.
 - "False" to indicate the edit box will not handletop after load font.
 */
class TopAfterLoadFont : public Property
{
public:
    TopAfterLoadFont() : Property(
                          "TopAfterLoadFont",
                          "Property to get/set the top-after-load-font setting for the edit box.  Value is either \"True\" or \"False\".",
                          "False")
    {}
    
    String	get(const PropertyReceiver* receiver) const;
    void	set(PropertyReceiver* receiver, const String& value);
};

} // End of  MultiLineEditboxProperties namespace section

} // End of  CEGUI namespace section


#endif	// end of guard _CEGUIMultiLineEditboxProperties_h_
