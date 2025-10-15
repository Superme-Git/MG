/***********************************************************************
    filename:   RichEditboxAnswerQuestionLinkComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxAnswerquestionLinkComponent.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	RichEditboxAnswerQuestionLinkComponent::RichEditboxAnswerQuestionLinkComponent(const String& text, int questionid, const String& name, int roleid, int type)
		: RichEditboxLinkTextComponent(text)
		, _questionid(questionid)
		, _name(name)
		, _roleid(roleid)
		, _type(type)
	{
		d_Type = RichEditboxComponentType_AnswerQuestion;
	}

	bool RichEditboxAnswerQuestionLinkComponent::onMouseButtonUp(MouseEventArgs& e)
	{
		if (e.button == LeftButton || e.button == RightButton)
		{
			AnswerQuestionLinkClicked *func = System::getSingleton().GetDefaultAnswerQuestionTeamLinkFunc();
			if (func)
			{
				(*func)(_questionid, _name, _roleid, _type);
			}
		}
		return RichEditboxLinkTextComponent::onMouseButtonUp(e);
	}

	RichEditboxAnswerQuestionLinkComponent* RichEditboxAnswerQuestionLinkComponent::split(float split_point, bool first_component)
	{
		RichEditboxAnswerQuestionLinkComponent* lhs = new RichEditboxAnswerQuestionLinkComponent(getText(), _questionid, _name, _roleid, _type);
		RichEditboxLinkTextComponent::clone2(lhs, split_point);
		return lhs;
	}

} // End of  CEGUI namespace section
