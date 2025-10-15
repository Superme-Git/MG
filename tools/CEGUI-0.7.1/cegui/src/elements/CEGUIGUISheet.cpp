/***********************************************************************
	filename: 	CEGUIGUISheet.cpp
	created:	28/8/2004
	author:		Paul D Turner
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
#include "elements/CEGUIGUISheet.h"


// Start of CEGUI namespace section
namespace CEGUI
{
/*************************************************************************
	Constants
*************************************************************************/
// type name for this widget
const String GUISheet::WidgetTypeName( "DefaultWindow" );
const float GUISheet::s_fMinOffset(30.0f);
/*************************************************************************
    Constructor
*************************************************************************/
GUISheet::GUISheet(const String& type, const String& name) :
    Window(type,name)
{
	d_name = name;
    UVector2 sz(cegui_reldim(1.0f), cegui_reldim(1.0f));
    setMaxSize(sz);
    setSize(sz);
	d_dragMovable = false;
}

GUISheet::GUISheet(const String& type) :
	Window(type)
{
}

const GUISheet& GUISheet::operator=(const GUISheet& t)
{
	Window::operator=(t);
	d_dragMovable = t.d_dragMovable;
	return *this;
}

Window* GUISheet::clone(Window* wnd)
{
	GUISheet* retWnd = (GUISheet*)wnd;
	if (retWnd == NULL)
		retWnd = new GUISheet(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == "DefaultWindow");
#endif
	*retWnd = *this;
	return retWnd;
}

//----------------------------------------------------------------------------//
void GUISheet::onMouseMove(MouseEventArgs& e)
{
    // always call the base class handler
    Window::onMouseMove(e);
    updateMouseEventHandled(e);
}

//----------------------------------------------------------------------------//
void GUISheet::onMouseWheel(MouseEventArgs& e)
{
    // always call the base class handler
    Window::onMouseWheel(e);
    updateMouseEventHandled(e);
}

//----------------------------------------------------------------------------//
void GUISheet::onMouseButtonDown(MouseEventArgs& e)
{
    // always call the base class handler
    Window::onMouseButtonDown(e);
    updateMouseEventHandled(e);
}

//----------------------------------------------------------------------------//
void GUISheet::onMouseButtonUp(MouseEventArgs& e)
{
    // always call the base class handler
    Window::onMouseButtonUp(e);
    updateMouseEventHandled(e);
}

//----------------------------------------------------------------------------//
void GUISheet::onMouseClicked(MouseEventArgs& e)
{
    // always call the base class handler
    Window::onMouseClicked(e);
    // only adjust the handled state if event was directly injected
    if (!System::getSingleton().isMouseClickEventGenerationEnabled())
        updateMouseEventHandled(e);
}

//----------------------------------------------------------------------------//
void GUISheet::onMouseDoubleClicked(MouseEventArgs& e)
{
    // always call the base class handler
    Window::onMouseDoubleClicked(e);
    // only adjust the handled state if event was directly injected
    //if (!System::getSingleton().isMouseClickEventGenerationEnabled())
        updateMouseEventHandled(e);
}

//----------------------------------------------------------------------------//
void GUISheet::onMouseTripleClicked(MouseEventArgs& e)
{
    // always call the base class handler
    Window::onMouseTripleClicked(e);
    // only adjust the handled state if event was directly injected
    if (!System::getSingleton().isMouseClickEventGenerationEnabled())
        updateMouseEventHandled(e);
}

//----------------------------------------------------------------------------//
void GUISheet::updateMouseEventHandled(MouseEventArgs& e) const
{
    // by default, if we are a root window (no parent) with pass-though enabled
    // we do /not/ mark mouse events as handled.
    if (!d_parent && e.handled && d_mousePassThroughEnabled)
        --e.handled;
}

//----------------------------------------------------------------------------//
bool GUISheet::moveToFront_impl(bool wasClicked)
{
    const bool took_action = Window::moveToFront_impl(wasClicked);

    if (!d_parent && d_mousePassThroughEnabled)
        return false;
    else
        return took_action;
}

void GUISheet::onDragMove(const Vector2& delta)
{
	if (d_dragMovable && d_parent && d_parent->getType() == "DefaultWindow" )
	{
		static_cast< CEGUI::DefaultWindow*>(d_parent)->offsetPixelPosition(delta);
	}
	else if(d_dragMovable == false && d_parent)
	{
		if (isHandleDragMove())
		{
			offsetPixelPosition(delta);
		}else
		{
			d_parent->onDragMove(delta);

		}
		
	}
}
/*************************************************************************
Set whether this FrameWindow can be moved by dragging the title bar.	
*************************************************************************/
void GUISheet::setDragMovingEnabled(bool setting)
{
	if (d_dragMovable != setting)
	{
		d_dragMovable = setting;
	}

}

/*************************************************************************
Move the window by the pixel offsets specified in 'offset'.	
*************************************************************************/
void GUISheet::offsetPixelPosition(const Vector2& offset)
{
	UVector2 uOffset(cegui_absdim(PixelAligned(offset.d_x)),
		cegui_absdim(PixelAligned(offset.d_y)));

	setPosition(d_area.getPosition() + uOffset);
	CheckLeftTopPos();
}

void GUISheet::CheckLeftTopPos()  //¼ì²â×óÉÏ½ÇÀëÆÁÄ»±ßÔµµÄ¾àÀë
{
	if (getParent()&&getParent()==System::getSingleton().getGUISheet())
	{
		Point ptTopLeft=GetScreenPos();
		Point ptRightBottm;
		ptRightBottm.d_x=ptTopLeft.d_x+getPixelSize().d_width;
		ptRightBottm.d_y=ptTopLeft.d_y+getPixelSize().d_height;
		Size ScreenSize=System::getSingleton().getGUISheet()->getPixelSize();

		bool bChange=false;
		if (ptTopLeft.d_x>(ScreenSize.d_width-s_fMinOffset))
		{
			ptTopLeft.d_x=ScreenSize.d_width-s_fMinOffset;
			bChange=true;
		}
		if (ptTopLeft.d_y>(ScreenSize.d_height-s_fMinOffset))
		{
			ptTopLeft.d_y=ScreenSize.d_height-s_fMinOffset;
			bChange=true;
		}

		if (ptRightBottm.d_x<s_fMinOffset)
		{
			ptTopLeft.d_x=ptTopLeft.d_x+(s_fMinOffset-ptRightBottm.d_x);
			bChange=true;
		}

		if (ptRightBottm.d_y<s_fMinOffset)
		{
			ptTopLeft.d_y=ptTopLeft.d_y+(s_fMinOffset-ptRightBottm.d_y);
			bChange=true;
		}

		if (bChange)
		{
			UVector2 newLeftTop(cegui_absdim(ptTopLeft.d_x),cegui_absdim(ptTopLeft.d_y));
			setPosition(newLeftTop);
		}
	}


}
//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
