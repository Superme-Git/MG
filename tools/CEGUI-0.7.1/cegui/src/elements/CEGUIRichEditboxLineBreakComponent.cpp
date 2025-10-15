/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxBreakLineComponent.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{

	RichEditboxBreakLineComponent::~RichEditboxBreakLineComponent()
	{

	}
//----------------------------------------------------------------------------//
RichEditboxBreakLineComponent::RichEditboxBreakLineComponent() 
{
	d_Type=RichEditboxComponentType_Break;

}

String RichEditboxBreakLineComponent::GenerateParseText()
{
	String ParseText("");
	ParseText+="<"+RichEditbox_xmlHandler::BreakElement+"></" + RichEditbox_xmlHandler::BreakElement+">";
	return ParseText;

}


} // End of  CEGUI namespace section
