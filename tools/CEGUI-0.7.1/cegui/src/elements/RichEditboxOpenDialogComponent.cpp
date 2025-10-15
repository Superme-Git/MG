/***********************************************************************
filename:   CEGUIRichEditboxTextComponent.cpp
*************************************************************************/

#include "elements/CEGUIRichEditboxOpenDialogComponent.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{

	RichEditboxOpenDialogComponent::RichEditboxOpenDialogComponent(const String& text, int64_t dialogID)
		: RichEditboxLinkTextComponent(text)
		, _targetDialogID(dialogID)
	{
		d_Type = RichEditboxComponentType_OpenDialog;
	}

	bool RichEditboxOpenDialogComponent::onMouseButtonUp(MouseEventArgs& e)
	{
		if (e.button == LeftButton || e.button == RightButton)
		{
			OpenDialog *func = System::getSingleton().GetDefaultOpenDialog();
			if (func)
			{
				(*func)(_targetDialogID);
			}
		}
		return RichEditboxLinkTextComponent::onMouseButtonUp(e);
	}

	RichEditboxOpenDialogComponent* RichEditboxOpenDialogComponent::split(float split_point, bool first_component)
	{
		RichEditboxOpenDialogComponent* lhs = new RichEditboxOpenDialogComponent(getText(),_targetDialogID);
		clone2(lhs, split_point);
		return lhs;
	}

} // End of  CEGUI namespace section
