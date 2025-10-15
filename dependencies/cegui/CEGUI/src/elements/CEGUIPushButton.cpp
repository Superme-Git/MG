/***********************************************************************
	filename: 	CEGUIPushButton.cpp
	created:	13/4/2004
	author:		Paul D Turner

	purpose:	Implementation of PushButton widget base class
*************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2006 Paul D Turner & The CEGUI Development Team
 *
 *   Permission is hereby granted, free of charge, to any person obtaining
 *   a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including
 *   without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to
 *   permit persons to whom the Software is furnished to do so, subject to
 *   the following conditions:
 *
 *   The above copyright notice and this permission notice shall be
 *   included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *   OTHER DEALINGS IN THE SOFTWARE.
 ***************************************************************************/
#include "elements/CEGUIPushButton.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIImage.h"






// Start of CEGUI namespace section
namespace CEGUI
{

/*************************************************************************
	constants
*************************************************************************/
// event strings
const String PushButton::EventNamespace("PushButton");
const String PushButton::WidgetTypeName("CEGUI/PushButton");
const String PushButton::EventClicked( "Clicked" );

const ColourRect DefaultButtonColour=ColourRect(ButtonTextTopColour,ButtonTextTopColour,ButtonTextBottomColour,ButtonTextBottomColour);
const colour DefaultButtonBoderColour=colour(ButtonBorderColor);

PushButtonProperties::SizeType PushButton::d_SizeTypeProperty;
PushButtonProperties::EnableClickAni PushButton::d_enableClickAniProperty;


/*************************************************************************
	Constructor
*************************************************************************/
PushButton::PushButton(const String& type, const String& name) :
	ButtonBase(type, name),
	d_AccepClickButtonType(LeftButton)
	
{
	d_HasFloodLight = false;
	d_SizeType=ePushButtonSizeType_Auto;
	AddPushButtonProperties();
}

PushButton::PushButton(const String& type):
	ButtonBase(type)
{
}

Window* PushButton::clone(Window* wnd)
{
	PushButton* retWnd = (PushButton*)wnd;
	if (retWnd == NULL)
		retWnd = new PushButton(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == PushButton::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

const PushButton& PushButton::operator=(const PushButton& t)
{
	ButtonBase::operator=(t);
	d_AccepClickButtonType = t.d_AccepClickButtonType;
	d_SizeType = t.d_SizeType;
	d_SizeTypeProperty = t.d_SizeTypeProperty;
	d_HasFloodLight = t.d_HasFloodLight;
	return *this;
}

/*************************************************************************
	Destructor
*************************************************************************/
PushButton::~PushButton(void)
{
}


/*************************************************************************
	handler invoked internally when the button is clicked.
*************************************************************************/
void PushButton::onClicked(WindowEventArgs& e)
{
	fireEvent(EventClicked, e, EventNamespace);
}

void   PushButton::onMouseButtonDown(MouseEventArgs& e)
{
	
	//ButtonBase::onMouseButtonDown(e);
	if (e.button!=d_AccepClickButtonType)
	{
		return;
	}
	
	ButtonBase::onMouseButtonDown(e);

//	ButtonBase::OnPlayClickSound();

	if (e.button == d_AccepClickButtonType)
	{
		if (captureInput())
		{
			d_pushed = true;
			updateInternalState(e.position);
			invalidate();
		}

		// event was handled by us.
		++e.handled;
	}
}


/*************************************************************************
	Handler for mouse button release events
*************************************************************************/
void PushButton::onMouseButtonUp(MouseEventArgs& e)
{
	if (e.button!=d_AccepClickButtonType)
	{
		return;
	}
    
    bool bIsPushed = isPushed();
    
    ButtonBase::onMouseButtonUp(e);

	if (/*(e.button == d_AccepClickButtonType) && */bIsPushed && !HasChangedUpMsg())
	{
		//Window* sheet = System::getSingleton().getGUISheet();

		//if (sheet)
		{
			// if mouse was released over this widget
            // (use position from mouse, as e.position has been unprojected)
			//if (this == sheet->getTargetChildAtPosition(
             //                       MouseCursor::getSingleton().getPosition()))
			{
				// fire event
//				WindowEventArgs args(this);
//				onClicked(args);
			}

		}

		++e.handled;
	}
    
	// default handling
	//ButtonBase::onMouseButtonUp(e);
	

//	if (e.button == d_AccepClickButtonType)
	{
		releaseInput();

		if (!isInChatOutWnd())
		{
			System::getSingleton().UpdateKeyBoardTargetWnd();
		}
		
		// event was handled by us.
		++e.handled;
	}
}

void PushButton::onMouseDoubleClicked(MouseEventArgs& e)
{
	onMouseButtonDown(e);
}

void PushButton::setText(const String& text, argb_t defcolour )
{
	ButtonBase::setText(text,defcolour);
	/*if (text.find("[border")==CEGUI::String::npos)
	{
		String newText("[border=\'");
		newText+=PropertyHelper::colourToString(DefaultButtonBoderColour);
		newText+="\']";
		newText+=text;
		setText(newText);
	}
	if (text.find("[colour")==CEGUI::String::npos&&text.find("[colrect")==CEGUI::String::npos)
	{
		String newText("[colrect=\'");
		newText+=PropertyHelper::colourRectToString(DefaultButtonColour);
		newText+="\']";
		newText+=text;
		setText(newText);
	}*/
}

void PushButton::SetSizeType(PushButtonSizeType eType)
{
	if (eType!=d_SizeType)
	{
		d_SizeType=eType;
	     
		UVector2 ButtonSize(cegui_absdim(0),cegui_absdim(0)); 
		ButtonSize.d_y.d_scale=0.0f;
		ButtonSize.d_y.d_offset=Button_Height;
		switch (d_SizeType)
		{
		case ePushButtonSizeType_Small:
			{
				ButtonSize.d_x.d_scale=0;
				ButtonSize.d_x.d_offset=Button_Width_Small;
				setMaxSize(ButtonSize);
				setMinSize(ButtonSize);
			}
			break;
		case ePushButtonSizeType_Normal:
			{
				ButtonSize.d_x.d_scale=0;
				ButtonSize.d_x.d_offset=Button_Width_Normal;
				setMaxSize(ButtonSize);
				setMinSize(ButtonSize);
			}
			break;
		case ePushButtonSizeType_Big:
			{
				ButtonSize.d_x.d_scale=0;
				ButtonSize.d_x.d_offset=Button_Width_Big;
				setMaxSize(ButtonSize);
				setMinSize(ButtonSize);
			}
			break;
		case ePushButtonSizeType_Image:
			{
				if (isPropertyPresent("NormalImage"))
				{
					String strImage=getProperty("NormalImage");
					const Image* pImage=PropertyHelper::stringToImage(strImage);
					if (pImage)
					{
						ButtonSize.d_x.d_scale=0;
						ButtonSize.d_x.d_offset=pImage->getWidth();

						ButtonSize.d_y.d_scale=0;
						ButtonSize.d_y.d_offset=pImage->getHeight();
						setMaxSize(ButtonSize);
						setMinSize(ButtonSize);
					}
				}else
				{
					SetSizeType(ePushButtonSizeType_Auto);
				}
			}
			break;
		case ePushButtonSizeType_Auto:
			{
				ButtonSize.d_x.d_scale=1.0;
				ButtonSize.d_x.d_offset=0;
				ButtonSize.d_y.d_scale=1.0;
				ButtonSize.d_y.d_offset=0;
				setMaxSize(ButtonSize);

				ButtonSize.d_x.d_scale=0.0;
				ButtonSize.d_x.d_offset=0;
				ButtonSize.d_y.d_scale=0.0;
				ButtonSize.d_y.d_offset=0;
				setMinSize(ButtonSize);
			}
			break;
		default:
			{
				SetSizeType(ePushButtonSizeType_Auto);
			}
		}
		
	}
	
}

void PushButton::AddPushButtonProperties()
{
	addProperty(&d_SizeTypeProperty);
	addProperty(&d_enableClickAniProperty);
}





} // End of  CEGUI namespace section
