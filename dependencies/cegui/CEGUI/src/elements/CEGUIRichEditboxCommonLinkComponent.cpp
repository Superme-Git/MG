/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxCommonLinkComponent.h"

#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section

namespace CEGUI
{

RichEditboxCommonLinkComponent::~RichEditboxCommonLinkComponent()
{

}
//----------------------------------------------------------------------------//
RichEditboxCommonLinkComponent::RichEditboxCommonLinkComponent(const String &text, const String &arg) :
RichEditboxLinkTextComponent(text),
arg(arg)
{
	d_Type=RichEditboxComponentType_CommonLink;
}

String RichEditboxCommonLinkComponent::GenerateParseText()
{
	String parseText;
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::CommonLinkElement;
	if (getText().size()>0)
	{
		parseText += " " + RichEditbox_xmlHandler::TextAttribute;
		parseText+="=";
		parseText+='"';
		parseText += getText();
		parseText+='"';

		parseText += " " + RichEditbox_xmlHandler::CommonLinkArg;
		parseText+="=";
		parseText+='"';
		parseText += arg;;
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::ColorAttribute;
		parseText+="=";
		parseText+='"';
		parseText += PropertyHelper::colourToString(getColours().d_top_left);
		parseText+='"';

	}
	parseText+="/>";

	return parseText;
}

Point RichEditboxCommonLinkComponent::GetOffsetOfIndex(size_t index)
{
	Point pt(0.0f, 0.0f);
	if (index>0)
	{
		pt.d_x = getPixelSize().d_width;
		pt.d_y = getPixelSize().d_height;
	}
	return pt;
}

bool RichEditboxCommonLinkComponent::onMouseButtonUp(MouseEventArgs& e)
{
	RichEditboxLinkTextComponent::onMouseButtonUp(e);
	CommonLinkLinkClicked *func = System::getSingleton().GetDefaultCommonLinkLinkFunc();
	if (func)
	{
		(*func)(arg);
	}
	return true;
}

RichEditboxCommonLinkComponent* RichEditboxCommonLinkComponent::split(float split_point, bool first_component)
{
	RichEditboxCommonLinkComponent* lhs = new RichEditboxCommonLinkComponent(getText(),arg);
	clone2(lhs, split_point);
	return lhs;
}


//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
