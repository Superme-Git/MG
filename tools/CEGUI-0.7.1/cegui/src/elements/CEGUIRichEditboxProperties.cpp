/***********************************************************************
	filename: 	CEGUIRichEditboxProperties.cpp
	purpose:	Implements properties for the MultiLineEditbox class
*************************************************************************/

#include "elements/CEGUIRichEditboxProperties.h"
#include "elements/CEGUIRichEditbox.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIExceptions.h"
#include "CEGUIFont.h"


// Start of CEGUI namespace section
namespace CEGUI
{

// Start of MultiLineEditboxProperties namespace section
namespace RichEditboxProperties
{
String ReadOnly::get(const PropertyReceiver* receiver) const
{
	return PropertyHelper::boolToString(static_cast<const RichEditbox*>(receiver)->isReadOnly());
}


void ReadOnly::set(PropertyReceiver* receiver, const String& value)
{
	static_cast<RichEditbox*>(receiver)->setReadOnly(PropertyHelper::stringToBool(value));
}


String WordWrap::get(const PropertyReceiver* receiver) const
{
	return PropertyHelper::boolToString(static_cast<const RichEditbox*>(receiver)->isWordWrapped());
}


void WordWrap::set(PropertyReceiver* receiver, const String& value)
{
	static_cast<RichEditbox*>(receiver)->setWordWrapping(PropertyHelper::stringToBool(value));
}

String MaxTextLength::get(const PropertyReceiver* receiver) const
{
	return PropertyHelper::uintToString(static_cast<uint>(static_cast<const RichEditbox*>(receiver)->getMaxTextLength()));
}


void MaxTextLength::set(PropertyReceiver* receiver, const String& value)
{
	static_cast<RichEditbox*>(receiver)->setMaxTextLength(PropertyHelper::stringToUint(value));
}


String SelectionBrushImage::get(const PropertyReceiver* receiver) const
{
    const Image* img = static_cast<const RichEditbox*>(receiver)->getSelectionBrushImage();
    return img ? PropertyHelper::imageToString(img) : String("");
}

void SelectionBrushImage::set(PropertyReceiver* receiver, const String &value)
{
    static_cast<RichEditbox*>(receiver)->setSelectionBrushImage(PropertyHelper::stringToImage(value));
}

String	ForceVertScrollbar::get(const PropertyReceiver* receiver) const
{
	return PropertyHelper::boolToString(static_cast<const RichEditbox*>(receiver)->isVertScrollbarAlwaysShown());
}

void	ForceVertScrollbar::set(PropertyReceiver* receiver, const String& value)
{
	static_cast<RichEditbox*>(receiver)->setShowVertScrollbar(PropertyHelper::stringToBool(value));
}

void BackGroundEnable::set(PropertyReceiver* receiver, const String& value)
{
	static_cast<RichEditbox*>(receiver)->SetBackGroundEnable(PropertyHelper::stringToBool(value));
}

String	BackGroundEnable::get(const PropertyReceiver* receiver) const
{
	return PropertyHelper::boolToString(static_cast<const RichEditbox*>(receiver)->IsBackGroundEnable());
}

void LineSpace::set(PropertyReceiver* receiver, const String& value)
{
	static_cast<RichEditbox*>(receiver)->SetLineSpace(PropertyHelper::stringToFloat(value));
}

String	LineSpace::get(const PropertyReceiver* receiver) const
{
	return PropertyHelper::floatToString(static_cast<const RichEditbox*>(receiver)->GetLineSpace());
}

void TopAfterLoadFont::set(PropertyReceiver* receiver, const String& value)
{
    static_cast<RichEditbox*>(receiver)->setTopAfterLoadFont(PropertyHelper::stringToBool(value));
}

String	TopAfterLoadFont::get(const PropertyReceiver* receiver) const
{
    return PropertyHelper::boolToString(static_cast<const RichEditbox*>(receiver)->getTopAfterLoadFont());
}
    
} // End of  MultiLineEditboxProperties namespace section

} // End of  CEGUI namespace section
