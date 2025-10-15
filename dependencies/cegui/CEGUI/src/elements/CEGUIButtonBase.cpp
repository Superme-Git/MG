/***********************************************************************
	filename: 	CEGUIButtonBase.cpp
	created:	13/4/2004
	author:		Paul D Turner
	
	purpose:	Implementation of ButtonBase widget
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
#include "elements/CEGUIButtonBase.h"
#include "CEGUIMouseCursor.h"


// Start of CEGUI namespace section
namespace CEGUI
{

	const float  s_fLeftButtonClickTime = 0.25f;	//鼠标按键的时间间隔

/*************************************************************************
	Constructor
*************************************************************************/
ButtonBase::ButtonBase(const String& type, const String& name) :
	Window(type, name),
	d_pushed(false),
	d_hovering(false),
	d_MouseLeaveReleaseInput(true),
    m_fClickAniElapse(0.0f),
    m_ClickAniState(ButtonClickAniState_None),
    d_MouseUpMsgHandle(false),
    d_EnableClickAni(true),
	m_fClickedElapse(0.5f),
	m_bLBtnDown(false)
{
	SetDragMoveEnable(false);
}

ButtonBase::ButtonBase(const String& type)	:
	Window(type)
{
}

Window* ButtonBase::clone(ButtonBase* wnd)
{
	assert(0);
	return NULL;
}

const ButtonBase& ButtonBase::operator=(const ButtonBase& t)
{
	Window::operator=(t);
	this->d_pushed = t.d_pushed;
	this->d_hovering = t.d_hovering;
	this->d_MouseLeaveReleaseInput = t.d_MouseLeaveReleaseInput;
	this->m_fClickAniElapse = t.m_fClickAniElapse;
	this->m_ClickAniState = t.m_ClickAniState;
	this->d_MouseUpMsgHandle = t.d_MouseUpMsgHandle;
	this->d_EnableClickAni = t.d_EnableClickAni;
	return *this;
}

/*************************************************************************
	Destructor
*************************************************************************/
ButtonBase::~ButtonBase(void)
{
}


/*************************************************************************
	Update the internal state of the Widget
*************************************************************************/
void ButtonBase::updateInternalState(const Point& mouse_pos)
{
    // This code is rewritten and has a slightly different behaviour
    // it is no longer fully "correct", as overlapping windows will not be
    // considered if the widget is currently captured.
    // On the other hand it's alot faster, so I believe it's a worthy
    // tradeoff

 	bool oldstate = d_hovering;

	// assume not hovering 
	d_hovering = false;

	// if input is captured, but not by 'this', then we never hover highlight
	const Window* capture_wnd = getCaptureWindow();
	if (capture_wnd == 0)
	{
	    System* sys = System::getSingletonPtr();
	    if (sys->getWindowContainingMouse() == this && isHit(mouse_pos))
	    {
	        d_hovering = true;
	    }
    }
    else if (capture_wnd == this && isHit(mouse_pos))
    {
        d_hovering = true;
    }

	if (d_MouseLeaveReleaseInput&&!isHit(mouse_pos))
	{
		releaseInput();
	}
	
    

	// if state has changed, trigger a re-draw
	if (oldstate != d_hovering)
	{
		invalidate();
	}

}


/*************************************************************************
	Handler for when the mouse moves
*************************************************************************/
void ButtonBase::onMouseMove(MouseEventArgs& e)
{
	// this is needed to discover whether mouse is in the widget area or not.
	// The same thing used to be done each frame in the rendering method,
	// but in this version the rendering method may not be called every frame
	// so we must discover the internal widget state here - which is actually
	// more efficient anyway.

	// base class processing
	Window::onMouseMove(e);

	updateInternalState(e.position);
	++e.handled;
}


/*************************************************************************
	Handler for mouse button pressed events
*************************************************************************/
void ButtonBase::onMouseButtonDown(MouseEventArgs& e)
{
	// default processing
	Window::onMouseButtonDown(e);

	if (e.button == LeftButton)
	{
		if (m_fClickedElapse < s_fLeftButtonClickTime)
			return;

		m_bLBtnDown = true;
		m_fClickedElapse = 0.0f;

        OnPlayClickSound();

		if (System::getSingleton().getWindowContainingMouse()==this)
		{
			if (captureInput())
			{
				d_pushed = true;
                
				if (d_EnableClickAni && m_ClickAniState != ButtonClickAniState_None)
				{
					m_ClickAniState = ButtonClickAniState_None;
					m_fClickAniElapse = 0.0f;
					SetClickAniScale(1.0f);

					if (d_MouseUpMsgHandle)
					{
						d_MouseUpMsgHandle = false;
						WindowEventArgs args(this);
						onClicked(args);

					}
				}

                if (d_EnableClickAni&&m_ClickAniState==ButtonClickAniState_None)
                {
                    m_ClickAniState=ButtonClickAniState_BeginTouch_ZooIn;
                    m_fClickAniElapse=0.0f;
                }

				
               
				
                updateInternalState(e.position);
				invalidate();
			}
		}
		
		// event was handled by us.
		++e.handled;
	}
}

//void ButtonBase::CheckGuideEnd(MouseButton button)
//{
//	if (HasGuide())
//	{
//		if (button==LeftButton)
//		{
//			SetGuideState(false);
//			WindowEventArgs e(this);
//			fireEvent(Window::EventGuideEnd,e,Window::EventNamespace);
//		}
//	}
//}


/*************************************************************************
	Handler for mouse button release events
*************************************************************************/
void ButtonBase::onMouseButtonUp(MouseEventArgs& e)
{
	// default processing
	
    bool bPushed=d_pushed;
    Window::onMouseButtonUp(e);

	if (e.button == LeftButton&&!HasChangedUpMsg() && m_bLBtnDown)
	{
		if (!d_EnableClickAni)
        {
            if (bPushed)
            {
                WindowEventArgs args(this);
                onClicked(args);
                
            }
            releaseInput();
            return;
        }
        
        
        releaseInput();
        if (d_EnableClickAni&&m_ClickAniState!=ButtonClickAniState_None)
        {
            d_MouseUpMsgHandle=true;
            m_ClickAniState=ButtonClickAniState_TouchEnd_ZoomOut;
            m_fClickAniElapse=0.0f;
        }
        
        
		m_bLBtnDown = false;

		// event was handled by us.
		++e.handled;
	}

}

/*************************************************************************
	Handler for when mouse capture is lost
*************************************************************************/
void ButtonBase::onCaptureLost(WindowEventArgs& e)
{
	// Default processing
	Window::onCaptureLost(e);

	d_pushed = false;
	
    if (m_ClickAniState!=ButtonClickAniState_None&&d_EnableClickAni)
    {
        m_ClickAniState=ButtonClickAniState_TouchEnd_ZoomOut;
        m_fClickAniElapse=0.0f;
    }
	updateInternalState(
        getUnprojectedPosition(MouseCursor::getSingletonPtr()->getPosition()));

	d_hovering = false; //reset hovering state
	invalidate();

	// event was handled by us.
	++e.handled;
}


/*************************************************************************
	Handler for when mouse leaves the widget
*************************************************************************/
void ButtonBase::onMouseLeaves(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseLeaves(e);

	if (d_MouseLeaveReleaseInput)
	{
		releaseInput();
	}
	
	d_hovering = false;
	invalidate();

	++e.handled;
}

void ButtonBase::setText(const String& text, argb_t defcolour )
{
	Window::setText(text,defcolour);
}

bool ButtonBase::isDrawFlash() // 是否要画次烁效果
{
	if (isHovering()||isPushed()||isDisabled())
	{
		return false;
	}

	return true;
}

void ButtonBase::OnPlayClickSound() //点击时播放点击的声音
{
	if (isSoundEable() && &System::getSingleton())
	{
		String soundRes=GetSoundResource();
		if (soundRes.empty())
		{
			System::getSingleton().PlayUISound(UIClickSound);
		}
		else
		{
			System::getSingleton().PlayUISound(soundRes);
		}
	}
}

// add by liugeng. make children do animate with button
void ButtonBase::setChildrenClickAniScale(float scale)
{
	int num = getChildCount();
	for (int i = 0; i < num; i++)
	{
		Window *child = getChildAtIdx(i);
		if (child && child->GetClickAniScale() != scale)
		{
			child->SetClickAniScale(scale);
		}
	}
}
    
void ButtonBase::updateSelf(float elapsed)
{
	m_fClickedElapse += elapsed;
    Window::updateSelf(elapsed);
    
    if (!d_EnableClickAni)
    {
        return;
    }
    if (isDisabled()||m_ClickAniState==ButtonClickAniState_None) {
        return;
    }
    
    float aniTime=0.16f;
    float MinScale=0.9f;
    float MaxScale=1.2f;
    //float EndZoomOutTime=0.12f;
    //float EndZoomInTime=0.1f;
	float EndZoomOutTime=0.0f;
	float EndZoomInTime=0.0f;
    m_fClickAniElapse+=elapsed;
    switch (m_ClickAniState) {
        case ButtonClickAniState_BeginTouch_ZooIn:
        {
            if (m_fClickAniElapse>aniTime) {
                m_ClickAniState=ButtonClickAniState_Pushed;
                m_fClickAniElapse=0.0f;
                SetClickAniScale(MinScale);
            }else
                SetClickAniScale(1.0f-(m_fClickAniElapse/aniTime)*(1.0f-MinScale));

			setChildrenClickAniScale(GetClickAniScale());
        }
        break;
        case ButtonClickAniState_TouchEnd_ZoomOut:
        {
            if (m_fClickAniElapse>EndZoomOutTime) {
                m_ClickAniState=ButtonClickAniState_TouchEnd_ZoomIn;
                m_fClickAniElapse=0.0f;
                SetClickAniScale(MaxScale);
                
               
            }else
                SetClickAniScale(MinScale+(m_fClickAniElapse/EndZoomOutTime)*(MaxScale-MinScale));

			setChildrenClickAniScale(GetClickAniScale());
        }
        break;
        case ButtonClickAniState_TouchEnd_ZoomIn:
        {
            if (m_fClickAniElapse>EndZoomInTime) {
                m_ClickAniState=ButtonClickAniState_None;
                m_fClickAniElapse=0.0f;
                SetClickAniScale(1.0f);
                
                if (d_MouseUpMsgHandle)
                {
                    d_MouseUpMsgHandle=false;
                    WindowEventArgs args(this);
                    onClicked(args);
                    
                }
            }else
                SetClickAniScale(MaxScale-(m_fClickAniElapse/EndZoomInTime)*(MaxScale-1.0f));

			setChildrenClickAniScale(GetClickAniScale());
        }
        break;

            
        default:
            break;
    }

}
    
void  ButtonBase::onClicked(WindowEventArgs& e)
{
}




} // End of  CEGUI namespace section
