/***********************************************************************
	filename: 	CEGUIScrollbar.cpp
	created:	13/4/2004
	author:		Paul D Turner
	
	purpose:	Implementation of Scrollbar widget base class
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
#include "elements/CEGUIScrollbar.h"
#include "elements/CEGUIThumb.h"
#include "CEGUIWindowManager.h"
#include "CEGUIExceptions.h"
#include <math.h>
#include "gesture/CEGUIPanGestureRecognizer.h"
#include "elements/CEGUIScrollablePane.h"
// Start of CEGUI namespace section
namespace CEGUI
{
const String Scrollbar::EventNamespace("Scrollbar");
const String Scrollbar::WidgetTypeName("CEGUI/Scrollbar");
float Scrollbar::d_DefultAcceleration(100.0f);

/*************************************************************************
    ScrollbarWindowRenderer
*************************************************************************/
ScrollbarWindowRenderer::ScrollbarWindowRenderer(const String& name) :
    WindowRenderer(name, Scrollbar::EventNamespace)
{
}

/*************************************************************************
	Definition of Properties for this class
*************************************************************************/
ScrollbarProperties::DocumentSize	Scrollbar::d_documentSizeProperty;
ScrollbarProperties::PageSize		Scrollbar::d_pageSizeProperty;
ScrollbarProperties::StepSize		Scrollbar::d_stepSizeProperty;
ScrollbarProperties::OverlapSize	Scrollbar::d_overlapSizeProperty;
ScrollbarProperties::ScrollPosition	Scrollbar::d_scrollPositionProperty;


/*************************************************************************
	Event name constants
*************************************************************************/
const String Scrollbar::EventScrollPositionChanged( "ScrollPosChanged" );
const String Scrollbar::EventThumbTrackStarted( "ThumbTrackStarted" );
const String Scrollbar::EventThumbTrackEnded( "ThumbTrackEnded" );
const String Scrollbar::EventScrollConfigChanged( "ScrollConfigChanged" );
const String Scrollbar::EventScrollbarEnd("ScrollbarEnd");
const String Scrollbar::EventSlideStopped("SlideStopped");

/*************************************************************************
    Child Widget name suffix constants
*************************************************************************/
const String Scrollbar::ThumbNameSuffix( "__auto_thumb__" );
const String Scrollbar::IncreaseButtonNameSuffix( "__auto_incbtn__" );
const String Scrollbar::DecreaseButtonNameSuffix( "__auto_decbtn__" );


/*************************************************************************
	Constructor for Scrollbar objects
*************************************************************************/
Scrollbar::Scrollbar(const String& type, const String& name) :
	Window(type, name),
	d_documentSize(1.0f),
	d_pageSize(0.0f),
	d_stepSize(1.0f),
	d_overlapSize(0.0f),
	d_position(0.0f),
    d_velocity(0.0f),
    d_acceleration(0.0f),
    d_Lock(false),
    m_SlideState(StopState),
    m_BackElapseTime(0.0f),
    m_SlideElapseTime(0.0f),
    d_TotalSlideTime(0.0f),
    d_PanGuestureEnable(true),
    d_PanForVert(true),
    d_ClickEnable(true),
    d_parentScrollPane(NULL),
	m_Offset(0.0f),
	d_StopStep(false),
	m_ticktime(0.0f)
{
	addScrollbarProperties();
    EnbaleSlide(true);
    EnableDrag(true);
	//SetNeedUpdate(true); // yangyinyu close!
}

Scrollbar::Scrollbar(const String& type) :
Window(type)
{
}

const Scrollbar& Scrollbar::operator=(const Scrollbar& t)
{
	Window::operator=(t);
	d_documentSize = t.d_documentSize;
	d_pageSize = t.d_pageSize;
	d_stepSize = t.d_stepSize;
	d_overlapSize = t.d_overlapSize;
	d_position = t.d_position;
	d_velocity = t.d_velocity;
	d_acceleration = t.d_acceleration;
	d_Lock = t.d_Lock;
	d_TotalSlideTime = t.d_TotalSlideTime;
	d_ClickEnable = t.d_ClickEnable;
	assert(!d_parentScrollPane);
	m_SlideState = t.m_SlideState;
	m_BackElapseTime = t.m_BackElapseTime;
	m_SlideElapseTime = t.m_SlideElapseTime;
	m_SlideDstPos = t.m_SlideDstPos;
	d_PanGuestureEnable = t.d_PanGuestureEnable;
	d_PanForVert = t.d_PanForVert;
	m_Offset = t.m_Offset;
	d_StopStep = t.d_StopStep;
	m_ticktime = t.m_ticktime;
	return *this;
}

Window* Scrollbar::clone(Window* wnd)
{
	Scrollbar* retWnd = (Scrollbar*)wnd;
	if (retWnd == NULL)
		retWnd = new Scrollbar(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == Scrollbar::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}


/*************************************************************************
	Destructor for Scrollbar objects
*************************************************************************/
Scrollbar::~Scrollbar(void)
{
}


/*************************************************************************
	Initialises the Scrollbar object ready for use.
*************************************************************************/
void Scrollbar::initialiseComponents(bool bClone)
{
	// Set up thumb
	Thumb* thumb = getThumb();
	thumb->subscribeEvent(Thumb::EventThumbPositionChanged, Event::Subscriber(&CEGUI::Scrollbar::handleThumbMoved, this));
	thumb->subscribeEvent(Thumb::EventThumbTrackStarted, Event::Subscriber(&CEGUI::Scrollbar::handleThumbTrackStarted, this));
	thumb->subscribeEvent(Thumb::EventThumbTrackEnded, Event::Subscriber(&CEGUI::Scrollbar::handleThumbTrackEnded, this));
	this->subscribeEvent(EventScrollPositionChanged, Event::Subscriber(&CEGUI::Scrollbar::handleScrollPositionChanged, this));
	// set up Increase button
	PushButton* increase = getIncreaseButton();
	increase->subscribeEvent(PushButton::EventMouseButtonDown, Event::Subscriber(&CEGUI::Scrollbar::handleIncreaseClicked, this));

	// set up Decrease button
	PushButton* decrease = getDecreaseButton();
	decrease->subscribeEvent(PushButton::EventMouseButtonDown, Event::Subscriber(&CEGUI::Scrollbar::handleDecreaseClicked, this));

	// do initial layout
	if (!bClone)
	{
		performChildWindowLayout();
	}
}


/*************************************************************************
	Set the size of the document or data.
*************************************************************************/
void Scrollbar::setDocumentSize(float document_size)
{
	if (d_documentSize != document_size)
	{
		d_documentSize = document_size;
		updateThumb();

		WindowEventArgs args(this);
		onScrollConfigChanged(args);
	}

}


/*************************************************************************
	Set the page size for this scroll bar.
*************************************************************************/
void Scrollbar::setPageSize(float page_size)
{
	if (d_pageSize != page_size)
	{
		d_pageSize = page_size;
		updateThumb();

		WindowEventArgs args(this);
		onScrollConfigChanged(args);
	}

}


/*************************************************************************
	Set the step size for this scroll bar.
*************************************************************************/
void Scrollbar::setStepSize(float step_size)
{
	if (d_stepSize != step_size)
	{
		d_stepSize = step_size;

		WindowEventArgs args(this);
		onScrollConfigChanged(args);
	}

}


/*************************************************************************
	Set the overlap size for this scroll bar.
*************************************************************************/
void Scrollbar::setOverlapSize(float overlap_size)
{
	if (d_overlapSize != overlap_size)
	{
		d_overlapSize = overlap_size;

		WindowEventArgs args(this);
		onScrollConfigChanged(args);
	}

}


/*************************************************************************
	Set the current position of scroll bar within the document.
*************************************************************************/
void Scrollbar::setScrollPosition(float position,bool checkPos)
{
	float old_pos = d_position;

	// max position is (docSize - pageSize), but must be at least 0 (in case doc size is very small)
	float max_pos = ceguimax((d_documentSize - d_pageSize), 0.0f);

	// limit position to valid range:  0 <= position <= max_pos
	if (checkPos)
    {
        d_position = (position >= 0) ? ((position <= max_pos) ? position : max_pos) : 0.0f;
	}
	else
	{
		d_position = position;
	}
    
	updateThumb();

	// notification if required
	if (d_position != old_pos)
	{
		WindowEventArgs args(this);
		onScrollPositionChanged(args);
	}

}


/*************************************************************************
	Handler triggered when the scroll position changes
*************************************************************************/
void Scrollbar::onScrollPositionChanged(WindowEventArgs& e)
{
	fireEvent(EventScrollPositionChanged, e, EventNamespace);
}


/*************************************************************************
	Handler triggered when the user begins to drag the scroll bar thumb. 	
*************************************************************************/
void Scrollbar::onThumbTrackStarted(WindowEventArgs& e)
{
	fireEvent(EventThumbTrackStarted, e, EventNamespace);
}


/*************************************************************************
	Handler triggered when the scroll bar thumb is released
*************************************************************************/
void Scrollbar::onThumbTrackEnded(WindowEventArgs& e)
{
	fireEvent(EventThumbTrackEnded, e, EventNamespace);
}


/*************************************************************************
	Handler triggered when the scroll bar data configuration changes
*************************************************************************/
void Scrollbar::onScrollConfigChanged(WindowEventArgs& e)
{
	performChildWindowLayout();
	fireEvent(EventScrollConfigChanged, e, EventNamespace);
}
    
/*************************************************************************
    滑动结束触发的事件(最开始和最后结束)
 *************************************************************************/
void Scrollbar::onScrollbarEnd(WindowEventArgs& e)
{
    fireEvent(EventScrollbarEnd, e, EventNamespace);
}



/*************************************************************************
	Handler for when mouse button is clicked within the container
*************************************************************************/
void Scrollbar::onMouseButtonDown(MouseEventArgs& e)
{
    if(!d_ClickEnable) return;
	// base class processing
	Window::onMouseButtonDown(e);

	if (e.button == LeftButton)
	{
		float adj = getAdjustDirectionFromPoint(e.position);

		// adjust scroll bar position in whichever direction as required.
		if (adj != 0)
		{
			setScrollPosition(d_position + ((d_pageSize - d_overlapSize) * adj));
		}

		++e.handled;
	}

	d_StopStep = false;
}


/*************************************************************************
	Handler for scroll wheel changes
*************************************************************************/
void Scrollbar::onMouseWheel(MouseEventArgs& e)
{
	// base class processing
	Window::onMouseWheel(e);

	// scroll by e.wheelChange * stepSize
	setScrollPosition(d_position + d_stepSize * -e.wheelChange);

	// ensure the message does not go to our parent.
	++e.handled;
}
    
void Scrollbar::onMouseSlide(MouseEventArgs& e)
{
//    Window::onMouseSlide(e);
 
    /*float v=350.0f;
    float a=350.0f;
    if (e.clickCount==2||e.clickCount==4) {
        d_velocity=v;
        d_acceleration=0.0f-a;
    }else
    {
        d_velocity=0.0f-v;
        d_acceleration=a;
    }
    Slide();*/
}
    
bool Scrollbar::onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer)
{
    Window::onMouseDrag(pRecognizer);
    
    if (!d_PanGuestureEnable)
    {
        return true;
    }
    
	if (m_SlideState == SlideState)
	{
		Stop();
	}

    if (pRecognizer->GetState()!=Gesture::GestureRecognizerStateEnded)
    {
        const MouseEventArgs* e = (MouseEventArgs*)pRecognizer->GetEvent();
		if (!e) return false;
        float curPos=getScrollPosition();
        if (d_PanForVert)
        {
            curPos -= (e->moveDelta.d_y / getWeakenRatio(curPos));
        }else
        {
            curPos -= (e->moveDelta.d_x / getWeakenRatio(curPos));
        }
        
		////////////////////////////
		float lastspeed = m_Offset;
		m_Offset = (curPos - getScrollPosition());

		if (m_Offset * lastspeed < 0) //反向走
		{
			m_ticktime = 0;
			d_StopStep = true;
			m_Offset = 0.0f;
		}
		///////////////////////////

        setScrollPosition(curPos,false);
        return true;
    }

    Gesture::CEGUIPanGestureRecognizer* pPanRecognizer = (Gesture::CEGUIPanGestureRecognizer*)pRecognizer;
    float v_y = -pPanRecognizer->velocityInView().d_y;
    float v_x = -pPanRecognizer->velocityInView().d_x;
    if (!d_PanForVert)
    {
        v_y = -pPanRecognizer->velocityInView().d_x;
        v_x = -pPanRecognizer->velocityInView().d_y;
    }
    
    float curPos=getScrollPosition();
    v_y /= getWeakenRatio(curPos);
    d_velocity=v_y;
    Slide();

    return true;
}


/*************************************************************************
	handler function for when thumb moves.
*************************************************************************/
bool Scrollbar::handleThumbMoved(const EventArgs&)
{
	// adjust scroll bar position as required.
	setScrollPosition(getValueFromThumb());

	return true;
}


/*************************************************************************
	handler function for when the increase button is clicked.
*************************************************************************/
bool Scrollbar::handleIncreaseClicked(const EventArgs& e)
{
	if (((const MouseEventArgs&)e).button == LeftButton)
	{
		// adjust scroll bar position as required.
		setScrollPosition(d_position + d_stepSize);

		return true;
	}
	else
	{
		return false;
	}

}


/*************************************************************************
	handler function for when the decrease button is clicked.
*************************************************************************/
bool Scrollbar::handleDecreaseClicked(const EventArgs& e)
{
	if (((const MouseEventArgs&)e).button == LeftButton)
	{
		// adjust scroll bar position as required.
		setScrollPosition(d_position - d_stepSize);

		return true;
	}
	else
	{
		return false;
	}
}


/*************************************************************************
	handler function for when thumb tracking begins
*************************************************************************/
bool Scrollbar::handleThumbTrackStarted(const EventArgs&)
{
	// simply trigger our own version of this event
	WindowEventArgs args(this);
	onThumbTrackStarted(args);

	return true;
}


/*************************************************************************
	handler function for when thumb tracking begins
*************************************************************************/
bool Scrollbar::handleThumbTrackEnded(const EventArgs&)
{
	// simply trigger our own version of this event
	WindowEventArgs args(this);
	onThumbTrackEnded(args);

	return true;
}

bool Scrollbar::handleScrollPositionChanged(const EventArgs& e)
{
	invalidate();
	return true;
}

/*************************************************************************
	Add scroll bar properties
*************************************************************************/
void Scrollbar::addScrollbarProperties(void)
{
	addProperty(&d_documentSizeProperty);
	addProperty(&d_pageSizeProperty);
	addProperty(&d_stepSizeProperty);
	addProperty(&d_overlapSizeProperty);
	addProperty(&d_scrollPositionProperty);

    // we ban all these properties from xml for auto windows
    if (isAutoWindow())
	{
#ifdef FORCEGUIEDITOR
        banPropertyFromXML(&d_documentSizeProperty);
        banPropertyFromXML(&d_pageSizeProperty);
        banPropertyFromXML(&d_stepSizeProperty);
        banPropertyFromXML(&d_overlapSizeProperty);
        banPropertyFromXML(&d_scrollPositionProperty);

        // scrollbars tend to have their visibility toggled alot, so we ban that as well
        banPropertyFromXML(&d_visibleProperty);
#endif
    }
}


/*************************************************************************
    Return a pointer to the 'increase' PushButtoncomponent widget for this
    Scrollbar.
*************************************************************************/
PushButton* Scrollbar::getIncreaseButton() const
{
    return static_cast<PushButton*>(WindowManager::getSingleton().getWindow(
                                    getName() + IncreaseButtonNameSuffix));
}


/*************************************************************************
    Return a pointer to the 'decrease' PushButton component widget for this
    Scrollbar.
*************************************************************************/
PushButton* Scrollbar::getDecreaseButton() const
{
    return static_cast<PushButton*>(WindowManager::getSingleton().getWindow(
                                    getName() + DecreaseButtonNameSuffix));
}


/*************************************************************************
    Return a pointer to the Thumb component widget for this Scrollbar.
*************************************************************************/
Thumb* Scrollbar::getThumb() const
{
    return static_cast<Thumb*>(WindowManager::getSingleton().getWindow(
                               getName() + ThumbNameSuffix));
}

/*************************************************************************
    update the size and location of the thumb to properly represent the
    current state of the scroll bar
*************************************************************************/
void Scrollbar::updateThumb(void)
{
    if (d_windowRenderer != 0)
    {
        ScrollbarWindowRenderer* wr = (ScrollbarWindowRenderer*)d_windowRenderer;
        wr->updateThumb();
    }
    else
    {
        //updateThumb_impl();
        throw InvalidRequestException("Scrollbar::updateThumb - This function must be implemented by the window renderer module");
    }
}

/*************************************************************************
    return value that best represents current scroll bar position given
    the current location of the thumb.
*************************************************************************/
float Scrollbar::getValueFromThumb(void) const
{
    if (d_windowRenderer != 0)
    {
        ScrollbarWindowRenderer* wr = (ScrollbarWindowRenderer*)d_windowRenderer;
        return wr->getValueFromThumb();
    }
    else
    {
        //return getValueFromThumb_impl();
        throw InvalidRequestException("Scrollbar::getValueFromThumb - This function must be implemented by the window renderer module");
    }
}

/*************************************************************************
    Given window location 'pt', return a value indicating what change
    should be made to the scroll bar.
*************************************************************************/
float Scrollbar::getAdjustDirectionFromPoint(const Point& pt) const
{
    if (d_windowRenderer != 0)
    {
        ScrollbarWindowRenderer* wr = (ScrollbarWindowRenderer*)d_windowRenderer;
        return wr->getAdjustDirectionFromPoint(pt);
    }
    else
    {
        //return getAdjustDirectionFromPoint_impl(pt);
        throw InvalidRequestException("Scrollbar::getAdjustDirectionFromPoint - This function must be implemented by the window renderer module");
    }
}

bool    Scrollbar::isThumbOnEnd()
{
	if (d_windowRenderer != 0)
	{
		ScrollbarWindowRenderer* wr = (ScrollbarWindowRenderer*)d_windowRenderer;
		return wr->isThumbOnEnd();
	}
	return false;
}
    


void Scrollbar::Free()
{
    d_Lock=false;
    
}
    
bool  Scrollbar::isTheSameDir()
{
    return  false;
}
bool  Scrollbar::isInertiaEnd()
{
    
    float curPos=getScrollPosition();
    float totalSize=getDocumentSize();
    float pageSize=getPageSize();
    if (d_acceleration>0.0f)
    {
        if (curPos>0.0f
            &&abs(d_velocity)<50.0f
            )
        {
            return true;
        }else if(abs(curPos)<60.0f&&d_velocity>0.0f)
        {
            return true;
        }
    }else
    {
        if (curPos+pageSize<totalSize
            &&abs(d_velocity)<50.0f
            )
        {
            return true;
        }else if(abs(curPos+pageSize-totalSize)<60.0f)
        {
            if (d_velocity<0.0f) {
                return true;
            }
            return true;
        }
        
    }
    return  false;
}

void Scrollbar::EndInertia()
{
    d_acceleration=0.0f;
    d_velocity=0.0f;
    d_Lock=false;
    float curPos=getScrollPosition();
    setScrollPosition(curPos,true);
}


void Scrollbar::updateSelf(float elapsed)
{
    Window::updateSelf(elapsed);
    //float curPos=getScrollPosition();
    float totalSize=getDocumentSize();
    float pageSize=getPageSize();
    float maxPos = ceguimax(0.0f, totalSize - pageSize);
    switch (m_SlideState)
    {
        case SlideState:
        {
            m_SlideElapseTime+=elapsed;
            float t = m_SlideElapseTime;
            float b = m_SlideStartPos;
            float c = m_SlideDstPos - m_SlideStartPos;
            float d = d_TotalSlideTime;
            float newPos = c*((t=t/d-1)*t*t + 1) + b;
            if((newPos > m_SlideStartPos && newPos >= m_SlideDstPos)
                || (newPos < m_SlideStartPos && newPos <= m_SlideDstPos))
            {
                newPos = m_SlideDstPos;
            }
            setScrollPosition(newPos,false);
            if(m_SlideElapseTime >= d_TotalSlideTime)
            {
                setScrollPosition(m_SlideDstPos);
                Back();
            }
            else if (newPos < 0 || newPos > maxPos)
            {
                Back();
            }
            break;
        }
        case BackState:
        {
            m_BackElapseTime+=elapsed;
            if(m_BackElapseTime >= 0.3f)
            {
                if(m_SlideStartPos < 0)
                    setScrollPosition(0.0f,false);
                else if(m_SlideStartPos > maxPos)
                    setScrollPosition(maxPos,false);
                Stop();
                WindowEventArgs args(this);
                onScrollbarEnd(args);
            }
            else
            {
                float t = m_BackElapseTime;
                float b = m_SlideStartPos;
                float c = 0 - m_SlideStartPos;
                if(m_SlideStartPos > 0)
                    c = maxPos - m_SlideStartPos;
                float d = 0.3f;
                float newPos = c*((t=t/d-1)*t*t + 1) + b;
                setScrollPosition(newPos,false);
            }
            break;
        }
            
        default:
            break;
    }
    
	//////////////////////刚反向滑动时不产生惯性
	m_ticktime += elapsed;
	if (m_ticktime > 1.5f && d_StopStep == true)
	{
		m_ticktime = 0;
		d_StopStep = false;
	}
	///////////////////
           
}
    
void Scrollbar::Slide()
{
    m_SlideStartPos=getScrollPosition();
    float totalSize=getDocumentSize();
    float pageSize=getPageSize();
    float maxPos = ceguimax(0.0f, totalSize - pageSize);
    if(m_SlideStartPos <= 0 || m_SlideStartPos >= maxPos)
    {
        Back();
        return;
    }
    
    m_SlideDstPos = m_SlideStartPos;
    d_TotalSlideTime = 3.0f;
    m_SlideElapseTime = 0.0f;
    float doorV = 20;//30
    float minV = 100;
    float maxV = 1500;
    if(d_velocity > doorV)
    {
        if(d_velocity < minV)
        {
            d_velocity = minV;
            d_TotalSlideTime = 1.5f;
        }
        else if(d_velocity > maxV)
            d_velocity = maxV;
    }
    else if(d_velocity < -doorV)
    {
        if(d_velocity > -minV)
        {
            d_velocity = -minV;
            d_TotalSlideTime = 1.5f;
        }
        else if(d_velocity < -maxV)
            d_velocity = -maxV;
    }
    else
    {
		d_TotalSlideTime = 0.75f;
        if(d_parentScrollPane != NULL)
        {//special parent scroll pane
            CEGUI::ScrollablePane* pane = dynamic_cast<CEGUI::ScrollablePane*>(d_parentScrollPane);
            pane->amendSlideDesPos(m_SlideStartPos, m_SlideDstPos, d_TotalSlideTime, d_velocity);
        }
        float c = m_SlideDstPos - m_SlideStartPos;
        if(fabsf(c) < 0.01)
            Back();
        else
            m_SlideState=SlideState;
        return;
    }

    float offset = d_velocity * 0.5f;//max slip offset = v＊t
    
	if(fabs(d_velocity) < 500)//300
		d_TotalSlideTime *= 0.5f;
	else if(fabs(d_velocity) < 1000)//300
		d_TotalSlideTime *= 0.6f;
	else if(fabs(d_velocity) < 1500)//300
		d_TotalSlideTime *= 0.7f;
	else if(fabs(d_velocity) < 2000)//300
		d_TotalSlideTime *= 0.8f;

	if (d_StopStep)
		m_SlideDstPos = m_SlideStartPos;
	else
		m_SlideDstPos = m_SlideStartPos + offset;

    if(m_SlideDstPos < 0)
    {
        d_TotalSlideTime *= fabsf(m_SlideStartPos/offset);
        m_SlideDstPos = 0;
    }
    else if(m_SlideDstPos > maxPos)
    {
        d_TotalSlideTime *= fabsf((maxPos - m_SlideStartPos)/offset);
        m_SlideDstPos = maxPos;
    }
    
    if(d_parentScrollPane != NULL)
    {
        CEGUI::ScrollablePane* pane = dynamic_cast<CEGUI::ScrollablePane*>(d_parentScrollPane);
        pane->amendSlideDesPos(m_SlideStartPos, m_SlideDstPos, d_TotalSlideTime, d_velocity);
    }
    m_SlideState=SlideState;
//    printf("SlideStartPos:%f \n",m_SlideStartPos);
//    printf("SlideDstPos:%f \n",m_SlideDstPos);
//    printf("d_TotalSlideTime:%f \n",d_TotalSlideTime);
//    printf("velocity:%f \n",d_velocity);
}

/*void Scrollbar::Slide()
{
    m_SlideState=SlideState;
    m_SlideElapseTime=0.0f;
    if (abs(d_velocity)<60.0f) {
        Back();
    }
 
}*/

void Scrollbar::Back()
{
    float curPos=getScrollPosition();
    float totalSize=getDocumentSize();
    float pageSize=getPageSize();
    if (/*curPos>0.0f&&*/curPos+pageSize<totalSize) {
        Stop();
        return;
    }
    m_SlideState=BackState;
    m_BackElapseTime=0.0f;
    m_SlideElapseTime=0.0f;
    d_TotalSlideTime=0.0f;
    m_SlideStartPos = curPos;
	m_Offset = 0.0f;
}
    
void Scrollbar::Stop()
{
    m_SlideState=StopState;
    m_BackElapseTime=0.0f;
    d_velocity=0.0f;
    d_acceleration=0.0f;
    m_SlideElapseTime=0.0f;
    d_TotalSlideTime=0.0f;
	m_Offset = 0.0f;
    float curPos=getScrollPosition();
    setScrollPosition(curPos,true);
    WindowEventArgs args(this);
    fireEvent(EventSlideStopped, args, EventNamespace);
}

void Scrollbar::Lock()
{
    m_SlideState=LockState;
    m_BackElapseTime=0.0f;
    d_velocity=0.0f;
    d_acceleration=0.0f;
    m_SlideElapseTime=0.0f;
    d_TotalSlideTime=0.0f;
	m_Offset = 0.0f;
}


float Scrollbar::getWeakenRatio(float pos)
{
	
    float max_pos = ceguimax((d_documentSize - d_pageSize), 0.0f);
    float pageSize = d_pageSize < d_documentSize ? d_pageSize : d_documentSize;
    float result;
    
    if (pos < 0)
    {
        result = ((-pos) * 3 + pageSize) / pageSize;
    }
    else
    {
        result = ((pos - max_pos) * 3 + pageSize) / pageSize;
    }
    return result > 1.0f ? result * result * result : 1.0f;
}
    

} // End of  CEGUI namespace section
