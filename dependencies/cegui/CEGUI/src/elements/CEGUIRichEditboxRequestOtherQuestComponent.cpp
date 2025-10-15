/***********************************************************************
filename:   CEGUIRichEditboxTextComponent.cpp
*************************************************************************/

#include "elements/CEGUIRichEditboxRequestOtherQuestComponent.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{

	RichEditboxRequestOtherQuestComponent::RichEditboxRequestOtherQuestComponent(const String& text, int64_t questID)
		: RichEditboxLinkTextComponent(text)
		, _targetQuestID(questID)
	{
		d_Type = RichEditboxComponentType_OpenDialog;
	}

	bool RichEditboxRequestOtherQuestComponent::onMouseButtonUp(MouseEventArgs& e)
	{
		if (e.button == LeftButton || e.button == RightButton)
		{
			RequestOtherQuest *func = CEGUI::System::getSingleton().GetDefaultOtherQuest();
			if (func)
			{
				(*func)(_targetQuestID, d_text);
			}
		}
		return RichEditboxLinkTextComponent::onMouseButtonUp(e);
	}

	RichEditboxRequestOtherQuestComponent* RichEditboxRequestOtherQuestComponent::split(float split_point, bool first_component)
	{
		RichEditboxRequestOtherQuestComponent* lhs = new RichEditboxRequestOtherQuestComponent(getText(),_targetQuestID);
		clone2(lhs, split_point);
		return lhs;
	}
} // End of  CEGUI namespace section
