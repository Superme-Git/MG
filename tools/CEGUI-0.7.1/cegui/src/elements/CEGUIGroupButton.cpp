/***********************************************************************
filename:   CEGUIGroupButton.cpp
*************************************************************************/

#include "elements/CEGUIGroupButton.h"
#include "CEGUIPropertyHelper.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const String GroupButton::EventNamespace("GroupButton");
	const String GroupButton::WidgetTypeName("CEGUI/GroupButton");


	/*************************************************************************
	Definitions of Properties for this class
	*************************************************************************/
	GroupButtonProperties::Selected GroupButton::d_selectedProperty;
	GroupButtonProperties::GroupID  GroupButton::d_groupIDProperty;
	GroupButtonProperties::StateImageExtendID GroupButton::d_stateImageExtendIDProperty;
	GroupButtonProperties::EnableClickAni GroupButton::d_enableClickAniProperty;


	/*************************************************************************
	Event name constants
	*************************************************************************/
	// generated internally by Window
	const String GroupButton::EventSelectStateChanged( "SelectStateChanged" );


	/*************************************************************************
	Constructor
	*************************************************************************/
	GroupButton::GroupButton(const String& type, const String& name) :
	ButtonBase(type, name),
		d_selected(false),
		d_groupID(0),
		d_TextNormalColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF),
		d_TextHoverColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF),
		d_TextPushedColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF),
		d_TextSelectColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF),
		d_TextDisableColourRect(0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F),
		d_NormalBorderColour(0xFF000000),
		d_HoverBorderColour(0xFF000000),
		d_PushedBorderColour(0xFF000000),
		d_DisableBorderColour(0xFF000000),
		d_SelectBorderColour(0xFF000000),
		d_BorderEnable(true),
		d_StateImageExtendID(0)
	{
		EnableClickAni(false);
		addGroupButtonProperties();
		//setWantsMultiClickEvents(true);
	}


	GroupButton::GroupButton(const String& type) :
		ButtonBase(type)
	{
	}
	const GroupButton& GroupButton::operator=(const GroupButton& t)
	{
		ButtonBase::operator=(t);
		d_selected = t.d_selected;
		d_groupID = t.d_groupID;
		d_TextNormalColourRect = t.d_TextNormalColourRect;
		d_TextHoverColourRect = t.d_TextHoverColourRect;
		d_TextPushedColourRect = t.d_TextPushedColourRect;
		d_TextDisableColourRect = t.d_TextDisableColourRect;
		d_TextSelectColourRect = t.d_TextSelectColourRect;
		d_NormalBorderColour = t.d_NormalBorderColour;
		d_HoverBorderColour = t.d_HoverBorderColour;
		d_PushedBorderColour = t.d_PushedBorderColour;
		d_DisableBorderColour = t.d_DisableBorderColour;
		d_SelectBorderColour = t.d_SelectBorderColour;
		d_BorderEnable = t.d_BorderEnable;
		d_StateImageExtendID = t.d_StateImageExtendID;
		d_EnableClickAni = t.d_EnableClickAni;
		return *this;
	}
	Window* GroupButton::clone(Window* wnd)
	{
		GroupButton* retWnd = (GroupButton*)wnd;
		if (retWnd == NULL)
			retWnd = new GroupButton(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == GroupButton::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

	/*************************************************************************
	Destructor
	*************************************************************************/
	GroupButton::~GroupButton(void)
	{
	}


	/*************************************************************************
	set whether the radio button is selected or not
	*************************************************************************/
	void GroupButton::setSelected(bool select, bool fireEvent)
	{
		if (select != d_selected)
		{
			d_selected = select;
			invalidate();

			// if new state is 'selected', we must de-select any selected radio buttons within our group.
			if (d_selected)
			{
				deselectOtherButtonsInGroup();
				if (fireEvent)
				{
					WindowEventArgs args(this);
					onSelectStateChanged(args);
				}
			}
// 
// 			WindowEventArgs args(this);
// 			onSelectStateChanged(args);
		}

	}


	/*************************************************************************
	set the groupID for this radio button
	*************************************************************************/
	void GroupButton::setGroupID(ulong group)
	{
		d_groupID = group;

		if (d_selected)
		{
			deselectOtherButtonsInGroup();
		}

	}


	/*************************************************************************
	Deselect any selected radio buttons attached to the same parent
	within the same group (but not do not deselect 'this').
	*************************************************************************/
	void GroupButton::deselectOtherButtonsInGroup(void) const
	{
		// nothing to do unless we are attached to another window.
		if (d_parent)
		{
			size_t child_count = d_parent->getChildCount();

			// scan all children
			for (size_t child = 0; child < child_count; ++child)
			{
				// is this child same type as we are?
				if (d_parent->getChildAtIdx(child)->getType().find("GroupButton")!=String::npos &&getType().find("GroupButton")!=String::npos)
				{
					GroupButton* rb = (GroupButton*)d_parent->getChildAtIdx(child);

					// is child same group, selected, but not 'this'?
					if (rb->isSelected() && (rb != this) && (rb->getGroupID() == d_groupID))
					{
						// deselect the group button.
						rb->setSelected(false);
					}

				}

			}

		}

	}


	/*************************************************************************
	event triggered internally when the select state of the button changes.
	*************************************************************************/
	void GroupButton::onSelectStateChanged(WindowEventArgs& e)
	{
		fireEvent(EventSelectStateChanged, e, EventNamespace);
	}


	/*************************************************************************
	Handler called when mouse button gets released
	*************************************************************************/
	void GroupButton::onMouseButtonUp(MouseEventArgs& e)
	{
//		if ((e.button == LeftButton) && isPushed())
//		{
//			Window* sheet = System::getSingleton().getGUISheet();
//
//			if (sheet)
//			{
//				// if mouse was released over this widget
//				// (use mouse position, since e.position has been unprojected)
//				if (this == sheet->getTargetChildAtPosition(
//					MouseCursor::getSingleton().getPosition()))
//				{
//					// select this button & deselect all others in the same group.
//					setSelected(true);
//				}
//
//			}
//
//			++e.handled;
//		}

		// default handling
		ButtonBase::onMouseButtonUp(e);
        
        releaseInput();
        
        // event was handled by us.
		++e.handled;
	}
    
    void GroupButton::onClicked(WindowEventArgs& e)
    {
        setSelected(true);
        
    }


	/*************************************************************************
	Return a pointer to the RadioButton object within the same group as
	this RadioButton, that is currently selected.
	*************************************************************************/
	GroupButton* GroupButton::getSelectedButtonInGroup(void) const
	{
		// Only search we we are a child window
		if (d_parent)
		{
			size_t child_count = d_parent->getChildCount();

			// scan all children
			for (size_t child = 0; child < child_count; ++child)
			{
				// is this child same type as we are?
				if (d_parent->getChildAtIdx(child)->getType().find("GroupButton")!=String::npos &&getType().find("GroupButton")!=String::npos)
				{
					GroupButton* gb = (GroupButton*)d_parent->getChildAtIdx(child);

					// is child same group and selected?
					if (gb->isSelected() && (gb->getGroupID() == d_groupID))
					{
						// return the matching GroupButton pointer (may even be 'this').
						return gb;
					}

				}

			}

		}

		// no selected button attached to this window is in same group
		return 0;
	}

	/*************************************************************************
	Add properties for radio button
	*************************************************************************/
	void GroupButton::addGroupButtonProperties(void)
	{
		addProperty(&d_selectedProperty);
		addProperty(&d_groupIDProperty);
		addProperty(&d_stateImageExtendIDProperty);
		addProperty(&d_enableClickAniProperty);
	}

	void    GroupButton::setStateTextColour(enumGroupButtonState state,const ColourRect& cl)
	{
		switch (state)
		{
		case eNormalState:
			{
				 d_TextNormalColourRect=cl;
			}
			break;
		case eHoverState:
			{
				d_TextHoverColourRect=cl;
			}
			break;
		case ePushedState:
			{
				 d_TextPushedColourRect=cl;
			}
			break;
		case eDisabledState:
			{
				d_TextDisableColourRect=cl;
			}
			break;
		case eSelectState:
			{
				d_TextSelectColourRect=cl;
			}
			break;
		
		}
		

	}

	const ColourRect& GroupButton::GetStateColour()
	{
		enumGroupButtonState state=eNormalState;
		if (isDisabled())
		{
			state=eDisabledState;
		}else if (isHovering())
		{
			state=eHoverState;
		}else if (isPushed())
		{
			state=ePushedState;
		}else if (isSelected())
		{
			state=eSelectState;
		}

		return GetStateColour(state);
	}


	const ColourRect& GroupButton::GetStateColour(enumGroupButtonState state)
	{
		switch (state)
		{
		case eNormalState:
			{
				return d_TextNormalColourRect;
			}
			break;
		case eHoverState:
			{
				if (isSelected() || isPushed())
				{
					return d_TextSelectColourRect;
				}
				return d_TextHoverColourRect;
			}
			break;
		case ePushedState:
			{
				return d_TextPushedColourRect;
			}
			break;
		case eDisabledState:
			{
				return d_TextDisableColourRect;
			}
			break;
		case eSelectState:
			{
				return d_TextSelectColourRect;
			}
			break;
		default:
			return d_TextNormalColourRect;
		}

	}
void    GroupButton::setStateBordcerColour(enumGroupButtonState state,const colour& cl)
{
	switch (state)
	{
	case eNormalState:
		{
			d_NormalBorderColour=cl;
		}
		break;
	case eHoverState:
		{
			d_HoverBorderColour=cl;
		}
		break;
	case ePushedState:
		{
			d_PushedBorderColour=cl;
		}
		break;
	case eDisabledState:
		{
			d_DisableBorderColour=cl;
		}
		break;
	case eSelectState:
		{
			d_SelectBorderColour=cl;
		}
		break;

	}


}

const colour& GroupButton::GetStateBorderColour()
{
	enumGroupButtonState state=eNormalState;
	if (isDisabled())
	{
		state=eDisabledState;
	}else if (isHovering())
	{
		state=eHoverState;
	}else if (isPushed())
	{
		state=ePushedState;
	}else if (isSelected())
	{
		state=eSelectState;
	}

	return GetStateBorderColour(state);
}


const colour& GroupButton::GetStateBorderColour(enumGroupButtonState state)
{
	switch (state)
	{
	case eNormalState:
		{
			return d_NormalBorderColour;
		}
		break;
	case eHoverState:
		{
			if (isSelected() || isPushed())
			{
				return d_SelectBorderColour;
			}
			return d_HoverBorderColour;
		}
		break;
	case ePushedState:
		{
			return d_PushedBorderColour;
		}
		break;
	case eDisabledState:
		{
			return d_DisableBorderColour;
		}
		break;
	case eSelectState:
		{
			return d_SelectBorderColour;
		}
		break;
	default:
		return d_NormalBorderColour;
	}

}
void GroupButton::initialiseComponents(bool bClone)  //设置完外观后设置各种状态的文字颜色
{
	if (!bClone)
	{
		if (isPropertyPresent("NormalTextColour"))
		{
			ColourRect Color = PropertyHelper::stringToColourRect(getProperty("NormalTextColour"));
			d_TextNormalColourRect = Color;
		}

		if (isPropertyPresent("HoverTextColour"))
		{
			ColourRect Color = PropertyHelper::stringToColourRect(getProperty("HoverTextColour"));
			d_TextHoverColourRect = Color;
		}

		if (isPropertyPresent("PushedTextColour"))
		{
			ColourRect Color = PropertyHelper::stringToColourRect(getProperty("PushedTextColour"));
			d_TextPushedColourRect = Color;
		}

		if (isPropertyPresent("DisabledTextColour"))
		{
			ColourRect Color = PropertyHelper::stringToColourRect(getProperty("DisabledTextColour"));
			d_TextDisableColourRect = Color;
		}

		if (isPropertyPresent("SelectedTextColour"))
		{
			ColourRect Color = PropertyHelper::stringToColourRect(getProperty("SelectedTextColour"));
			d_TextSelectColourRect = Color;
		}

		if (isPropertyPresent("NormalBorderColour"))
		{
			colour Color = PropertyHelper::stringToColour(getProperty("NormalBorderColour"));
			d_NormalBorderColour = Color;
		}

		if (isPropertyPresent("HoverBorderColour"))
		{
			colour Color = PropertyHelper::stringToColour(getProperty("HoverBorderColour"));
			d_HoverBorderColour = Color;
		}

		if (isPropertyPresent("PushedBorderColour"))
		{
			colour Color = PropertyHelper::stringToColour(getProperty("PushedBorderColour"));
			d_PushedBorderColour = Color;
		}

		if (isPropertyPresent("DisabledBorderColour"))
		{
			colour Color = PropertyHelper::stringToColour(getProperty("DisabledBorderColour"));
			d_DisableBorderColour = Color;
		}

		if (isPropertyPresent("SelectedBorderColour"))
		{
			colour Color = PropertyHelper::stringToColour(getProperty("SelectedBorderColour"));
			d_SelectBorderColour = Color;
		}
	}
}



} // End of  CEGUI namespace section
