/***********************************************************************
    filename:   CEGUIScrollablePane.cpp
    created:    1/3/2005
    author:     Paul D Turner
 *************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2009 Paul D Turner & The CEGUI Development Team
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
#include "elements/CEGUIScrollablePane.h"
#include "elements/CEGUIScrolledContainer.h"
#include "elements/CEGUIScrollbar.h"
#include "elements/CEGUIPushButton.h"
#include "CEGUIWindowManager.h"
#include "CEGUIExceptions.h"
#include "gesture/CEGUIPanGestureRecognizer.h"
#include <math.h>

// Start of CEGUI namespace section
namespace CEGUI
{
//----------------------------------------------------------------------------//
const String ScrollablePane::WidgetTypeName("CEGUI/ScrollablePane");
const String ScrollablePane::EventNamespace("ScrollablePane");
const String ScrollablePane::EventContentPaneChanged("ContentPaneChanged");
const String ScrollablePane::EventVertScrollbarModeChanged("VertScrollbarModeChanged");
const String ScrollablePane::EventHorzScrollbarModeChanged("HorzScrollbarModeChanged");
const String ScrollablePane::EventAutoSizeSettingChanged("AutoSizeSettingChanged");
const String ScrollablePane::EventContentPaneScrolled("ContentPaneScrolled");
const String ScrollablePane::VertScrollbarNameSuffix( "__auto_vscrollbar__" );
const String ScrollablePane::HorzScrollbarNameSuffix( "__auto_hscrollbar__" );
const String ScrollablePane::ScrolledContainerNameSuffix( "__auto_container__" );
const String ScrollablePane::EventPrePage("PrePage");
const String ScrollablePane::EventNextPage("NextPage");
const String ScrollablePane::EventScrollPageChanged("ScrollPageChanged");
const String ScrollablePane::EventCellEnterView("CellEnter");
const String ScrollablePane::EventCellLeaveView("CellLeave");
    
//----------------------------------------------------------------------------//
ScrollablePaneProperties::ForceHorzScrollbar   ScrollablePane::d_horzScrollbarProperty;
ScrollablePaneProperties::ForceVertScrollbar   ScrollablePane::d_vertScrollbarProperty;
ScrollablePaneProperties::ContentPaneAutoSized ScrollablePane::d_autoSizedProperty;
ScrollablePaneProperties::ContentArea          ScrollablePane::d_contentAreaProperty;
ScrollablePaneProperties::HorzStepSize         ScrollablePane::d_horzStepProperty;
ScrollablePaneProperties::HorzOverlapSize      ScrollablePane::d_horzOverlapProperty;
ScrollablePaneProperties::HorzScrollPosition   ScrollablePane::d_horzScrollPositionProperty;
ScrollablePaneProperties::VertStepSize         ScrollablePane::d_vertStepProperty;
ScrollablePaneProperties::VertOverlapSize      ScrollablePane::d_vertOverlapProperty;
ScrollablePaneProperties::VertScrollPosition   ScrollablePane::d_vertScrollPositionProperty;
ScrollablePaneProperties::PaneScrollMode       ScrollablePane::d_paneScrollModeProperty;

//----------------------------------------------------------------------------//
ScrollablePaneWindowRenderer::ScrollablePaneWindowRenderer(const String& name) :
WindowRenderer(name, ScrollablePane::EventNamespace)
{
}

//----------------------------------------------------------------------------//
ScrollablePane::ScrollablePane(const String& type, const String& name) :
    Window(type, name),
    d_forceVertScroll(false),
    d_forceHorzScroll(false),
    d_contentRect(0, 0, 0, 0),
    d_vertStep(0.1f),
    d_vertOverlap(0.01f),
    d_horzStep(0.1f),
    d_horzOverlap(0.01f),
    d_vertScrollbar(NULL),
    d_horzScrollbar(NULL),
    d_contentPane(NULL),
    d_EnableVertScrollBar(true),
    d_EnableHorzScrollBar(false),
	d_EnableScrollBarDrag(true),
    d_PaneScrollMode(eNoPageScroll),
    d_endPos(0.0f)
{
    addScrollablePaneProperties();
    
    // create scrolled container widget
    ScrolledContainer* container = static_cast<ScrolledContainer*>(
        WindowManager::getSingleton().createWindow(
            ScrolledContainer::WidgetTypeName,
            d_name + ScrolledContainerNameSuffix));

    if (container)
    {
		// add scrolled container widget as child
		addChildWindow(container);
        container->subscriberEventDrag(Event::Subscriber(&ScrollablePane::HandleMouseDrag, this));
    }
	
    EnbaleSlide(true);
    EnableDrag(true);
    
}

ScrollablePane::ScrollablePane(const String& type) :
Window(type)
{
}

void ScrollablePane::setLookNFeel(const String& look, bool bClone)
{
	Window::setLookNFeel(look, bClone);
	if (bClone)
	{
		d_contentChangedConn->disconnect();
		d_autoSizeChangedConn->disconnect();
		d_lostForceConn->disconnect();

		ScrolledContainer* container = getScrolledContainer();

		const char* s = container->getName().c_str();

		d_contentChangedConn = container->subscribeEvent(
			ScrolledContainer::EventContentChanged,
			Event::Subscriber(&ScrollablePane::handleContentAreaChange, this));

		d_autoSizeChangedConn = container->subscribeEvent(
			ScrolledContainer::EventAutoSizeSettingChanged,
			Event::Subscriber(&ScrollablePane::handleAutoSizePaneChanged, this));

		d_lostForceConn = container->subscribeEvent(
			ScrolledContainer::EventDeactivated,
			Event::Subscriber(&ScrollablePane::handleLostForce, this));

		for (size_t i = 0; i < this->getChildCount(); ++i)
		{
			Window* wnd = this->getChildAtIdx(i);
			wnd->subscriberEventDrag(Event::Subscriber(&ScrollablePane::HandleMouseDrag, this));
			wnd->subscribeEvent(Window::EventMouseButtonDown, Event::Subscriber(&ScrollablePane::HandleChildMouseButtonDown, this));

			//
			EnableAllChildDrag(wnd);
		}
	}
}

bool ScrollablePane::onRenameTemplatePrefix(const String& sPrefix)
{
	return Window::onRenameTemplatePrefix(sPrefix);
}

const ScrollablePane& ScrollablePane::operator=(const ScrollablePane& t)
{
	Window::operator=(t);
	d_forceVertScroll = t.d_forceVertScroll;
	d_forceHorzScroll = t.d_forceHorzScroll;
	d_contentRect = t.d_contentRect;
	d_vertStep = t.d_vertStep;
	d_vertOverlap = t.d_vertOverlap;
	d_horzStep = t.d_horzStep;
	d_horzOverlap = t.d_horzOverlap;
	d_contentChangedConn = t.d_contentChangedConn;
	d_autoSizeChangedConn = t.d_autoSizeChangedConn;
	d_lostForceConn = t.d_lostForceConn;
	assert(!t.d_vertScrollbar);
	assert(!t.d_horzScrollbar);
	assert(!t.d_contentPane);
	d_EnableVertScrollBar = t.d_EnableVertScrollBar;
	d_EnableHorzScrollBar = t.d_EnableHorzScrollBar;
	d_EnableScrollBarDrag = t.d_EnableScrollBarDrag;
	d_PaneScrollMode = t.d_PaneScrollMode;
	d_endPos = t.d_endPos;
	return *this;
}

Window* ScrollablePane::clone(Window* wnd)
{
	ScrollablePane* retWnd = (ScrollablePane*)wnd;
	if (retWnd == NULL)
		retWnd = new ScrollablePane(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == ScrollablePane::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

//----------------------------------------------------------------------------//
ScrollablePane::~ScrollablePane(void)
{
    d_contentChangedConn->disconnect();
    d_autoSizeChangedConn->disconnect();
	d_lostForceConn->disconnect();
}

//----------------------------------------------------------------------------//
const ScrolledContainer* ScrollablePane::getContentPane(void) const
{
    return getScrolledContainer();
}

//----------------------------------------------------------------------------//
bool ScrollablePane::isVertScrollbarAlwaysShown(void) const
{
    return d_forceVertScroll;
}

//----------------------------------------------------------------------------//
void ScrollablePane::setShowVertScrollbar(bool setting)
{
    if (d_forceVertScroll != setting)
    {
        d_forceVertScroll = setting;
        
        configureScrollbars();
        WindowEventArgs args(this);
        onVertScrollbarModeChanged(args);
    }
}

//----------------------------------------------------------------------------//
bool ScrollablePane::isHorzScrollbarAlwaysShown(void) const
{
    return d_forceHorzScroll;
}

//----------------------------------------------------------------------------//
void ScrollablePane::setShowHorzScrollbar(bool setting)
{
    if (d_forceHorzScroll != setting)
    {
        d_forceHorzScroll = setting;
        
        configureScrollbars();
        WindowEventArgs args(this);
        onHorzScrollbarModeChanged(args);
    }
}

//----------------------------------------------------------------------------//
bool ScrollablePane::isContentPaneAutoSized(void) const
{
    return getScrolledContainer()->isContentPaneAutoSized();
}

//----------------------------------------------------------------------------//
void ScrollablePane::setContentPaneAutoSized(bool setting)
{
    getScrolledContainer()->setContentPaneAutoSized(setting);
}

//----------------------------------------------------------------------------//
const Rect& ScrollablePane::getContentPaneArea(void) const
{
    return getScrolledContainer()->getContentArea();
}

//----------------------------------------------------------------------------//
void ScrollablePane::setContentPaneArea(const Rect& area)
{
    getScrolledContainer()->setContentArea(area);
}

//----------------------------------------------------------------------------//
float ScrollablePane::getHorizontalStepSize(void) const
{
    return d_horzStep;
}

//----------------------------------------------------------------------------//
void ScrollablePane::setHorizontalStepSize(float step)
{
    d_horzStep = step;
    configureScrollbars();
}

//----------------------------------------------------------------------------//
float ScrollablePane::getHorizontalOverlapSize(void) const
{
    return d_horzOverlap;
}

//----------------------------------------------------------------------------//
void ScrollablePane::setHorizontalOverlapSize(float overlap)
{
    d_horzOverlap = overlap;
    configureScrollbars();
}

//----------------------------------------------------------------------------//
float ScrollablePane::getHorizontalScrollPosition(void) const
{
    Scrollbar* horzScrollbar = getHorzScrollbar();
    float docSz = horzScrollbar->getDocumentSize();
    return (docSz != 0) ? horzScrollbar->getScrollPosition() / docSz : 0.0f;
}

//----------------------------------------------------------------------------//
void ScrollablePane::setHorizontalScrollPosition(float position)
{
    Scrollbar* horzScrollbar = getHorzScrollbar();
    horzScrollbar->setScrollPosition(
                        horzScrollbar->getDocumentSize() * position);
}

//----------------------------------------------------------------------------//
float ScrollablePane::getVerticalStepSize(void) const
{
    return d_vertStep;
}

//----------------------------------------------------------------------------//
void ScrollablePane::setVerticalStepSize(float step)
{
    d_vertStep = step;
    configureScrollbars();
}

//----------------------------------------------------------------------------//
float ScrollablePane::getVerticalOverlapSize(void) const
{
    return d_vertOverlap;
}

//----------------------------------------------------------------------------//
void ScrollablePane::setVerticalOverlapSize(float overlap)
{
    d_vertOverlap = overlap;
    configureScrollbars();
}

//----------------------------------------------------------------------------//
float ScrollablePane::getVerticalScrollPosition(void) const
{
    Scrollbar* vertScrollbar = getVertScrollbar();
    float docSz = vertScrollbar->getDocumentSize();
    return (docSz != 0) ? vertScrollbar->getScrollPosition() / docSz : 0.0f;
}

//----------------------------------------------------------------------------//
void ScrollablePane::setVerticalScrollPosition(float position)
{
    Scrollbar* vertScrollbar = getVertScrollbar();
    vertScrollbar->setScrollPosition(
                        vertScrollbar->getDocumentSize() * position);
}

//----------------------------------------------------------------------------//
void ScrollablePane::initialiseComponents(bool bClone)
{
    // get horizontal scrollbar
    Scrollbar* horzScrollbar = getHorzScrollbar();
    
    // get vertical scrollbar
    Scrollbar* vertScrollbar = getVertScrollbar();
    
    // get scrolled container widget
    ScrolledContainer* container = getScrolledContainer();
    
    // do a bit of initialisation
	if (!bClone)
	{
		horzScrollbar->setAlwaysOnTop(true);
		vertScrollbar->setAlwaysOnTop(true);
		// container pane is always same size as this parent pane,
		// scrolling is actually implemented via positioning and clipping tricks.
		container->setSize(UVector2(cegui_reldim(1.0f), cegui_reldim(1.0f)));
	}
    
    if (horzScrollbar)
    {
        if(d_PaneScrollMode == eSinglePageScroll || d_PaneScrollMode == eMultiPageScroll)
        {
            horzScrollbar->subscribeEvent(
                                          Scrollbar::EventSlideStopped,
                                          Event::Subscriber(&ScrollablePane::onScrollPageChanged, this));
        }
		else
		{
			horzScrollbar->subscribeEvent(
				Scrollbar::EventScrollPositionChanged,
				Event::Subscriber(&ScrollablePane::handleScrollChange, this));
		}
        
        horzScrollbar->SetParentScrollPane(this);
    }
    
    if (vertScrollbar)
    {
        if(d_PaneScrollMode == eSinglePageScroll || d_PaneScrollMode == eMultiPageScroll)
        {
            vertScrollbar->subscribeEvent(
                                          Scrollbar::EventSlideStopped,
                                          Event::Subscriber(&ScrollablePane::onScrollPageChanged, this));
        }
        vertScrollbar->subscribeEvent(
            Scrollbar::EventScrollPositionChanged,
            Event::Subscriber(&ScrollablePane::handleScrollChange, this));
        vertScrollbar->SetParentScrollPane(this);
    }

    d_contentChangedConn = container->subscribeEvent(
            ScrolledContainer::EventContentChanged,
            Event::Subscriber(&ScrollablePane::handleContentAreaChange, this));

    d_autoSizeChangedConn = container->subscribeEvent(
            ScrolledContainer::EventAutoSizeSettingChanged,
            Event::Subscriber(&ScrollablePane::handleAutoSizePaneChanged, this));

    // 捆绑失去焦点函数.
    d_lostForceConn = container->subscribeEvent(
		ScrolledContainer::EventDeactivated,
            Event::Subscriber(&ScrollablePane::handleLostForce, this));
    
    // finalise setup
	if (!bClone)
	{
		configureScrollbars();
	}
}

//----------------------------------------------------------------------------//
void ScrollablePane::configureScrollbars(void)
{
    // controls should all be valid by this stage
    Scrollbar* vertScrollbar = getVertScrollbar();
    Scrollbar* horzScrollbar = getHorzScrollbar();
    
    // enable required scrollbars
    vertScrollbar->setVisible(isVertScrollbarNeeded());
    horzScrollbar->setVisible(isHorzScrollbarNeeded());
    
    // Check if the addition of the horizontal scrollbar means we
    // now also need the vertical bar.
    if (horzScrollbar->isVisible())
    {
        vertScrollbar->setVisible(isVertScrollbarNeeded());
    }
    
    performChildWindowLayout();
    
    // get viewable area
    Rect viewableArea(getViewableArea());
    
    // set up vertical scroll bar values
    vertScrollbar->setDocumentSize(fabsf(d_contentRect.getHeight()));
    vertScrollbar->setPageSize(viewableArea.getHeight());
    vertScrollbar->setStepSize(ceguimax(1.0f, viewableArea.getHeight() * d_vertStep));
    vertScrollbar->setOverlapSize(ceguimax(1.0f, viewableArea.getHeight() * d_vertOverlap));
    vertScrollbar->setScrollPosition(vertScrollbar->getScrollPosition());
    
    
    
    // set up horizontal scroll bar values
    horzScrollbar->setDocumentSize(fabsf(d_contentRect.getWidth()));
    horzScrollbar->setPageSize(viewableArea.getWidth());
    horzScrollbar->setStepSize(ceguimax(1.0f, viewableArea.getWidth() * d_horzStep));
    horzScrollbar->setOverlapSize(ceguimax(1.0f, viewableArea.getWidth() * d_horzOverlap));
    horzScrollbar->setScrollPosition(horzScrollbar->getScrollPosition());
    
    UpdateChildsShow();

}

//----------------------------------------------------------------------------//
bool ScrollablePane::isHorzScrollbarNeeded(void) const
{
//    return ((fabs(d_contentRect.getWidth()) > getViewableArea().getWidth()) ||
//            d_forceHorzScroll);
    return  false;
}

//----------------------------------------------------------------------------//
bool ScrollablePane::isVertScrollbarNeeded(void) const
{
//    return ((fabs(d_contentRect.getHeight()) > getViewableArea().getHeight()) ||
//            d_forceVertScroll);
    return false;
}

void ScrollablePane::UpdateChildsViewState(bool clearLastDraw)
{
	if (!isVisible(false))
		return;

	struct Rect
	{
		float top;
		float bottom;
		Rect(Point pos, Size sz)
			:top(pos.d_y),
			bottom(top + sz.d_height)
		{
		}
	};

	const ScrolledContainer* Container = getContentPane();

	Window* parentWnd = getParent();
	Rect pr(GetScreenPos(), getPixelSize());

	std::map<String, Window*> newCells;
	for (size_t i = 0; i < Container->getChildCount(); ++i)
	{
		Window* cellWnd = Container->getChildAtIdx(i);
		if (!cellWnd->isAutoWindow() && cellWnd->isVisible(true))
		{
			Rect cr(cellWnd->GetScreenPos(), cellWnd->getPixelSize());
			if (cr.top>pr.top&&cr.top < pr.bottom)
			{
				newCells.insert(std::pair<String, Window*>(cellWnd->getName(), cellWnd));
			}
			else if (cr.bottom>pr.top&&cr.bottom < pr.bottom)
			{
				newCells.insert(std::pair<String, Window*>(cellWnd->getName(), cellWnd));
			}
		}
	}

	WindowManager& winMgr = WindowManager::getSingleton();
	/*
	std::set<String>::iterator itOldCells = d_DrawCells.begin();
	while (itOldCells != d_DrawCells.end())
	{
		if (newCells.find(*itOldCells) == newCells.end())
		{
			Window* cellWnd = winMgr.getWindow(*itOldCells);
			if (cellWnd && cellWnd->getChildCount() == 1)
			{
				WindowEventArgs args(cellWnd);
				fireEvent(EventCellLeaveView, args);
			}
		}
		++itOldCells;
	}
	*/
	ScrolledContainer* container = this->getScrolledContainer();
	for (size_t i = 0; i < container->getChildCount(); ++i)
	{
		Window* cellSpaceWnd = container->getChildAtIdx(i);
		if (cellSpaceWnd->isVisible(true) && newCells.find(cellSpaceWnd->getName()) == newCells.end())
		{
			WindowEventArgs args(cellSpaceWnd);
			fireEvent(EventCellLeaveView, args);
		}
	}

	std::map<String, Window*>::iterator itNewCells=newCells.begin();
	while (itNewCells != newCells.end())
	{
		Window* cellWnd = itNewCells->second;
		if (cellWnd->getChildCount() == 0)
		{
			WindowEventArgs args(itNewCells->second);
			fireEvent(EventCellEnterView, args);
		}
		++itNewCells;
	}

	d_DrawCells.clear();
	itNewCells = newCells.begin();
	while (itNewCells != newCells.end())
	{
		d_DrawCells.insert(itNewCells->first);
		++itNewCells;
	}

	invalidate();
	notifyScreenAreaChanged();
}

//----------------------------------------------------------------------------//
void ScrollablePane::updateContainerPosition(void)
{
    // basePos is the position represented by the scrollbars
    // (these are negated so pane is scrolled in the correct directions)
    UVector2 basePos(cegui_absdim(-getHorzScrollbar()->getScrollPosition()),
                     cegui_absdim(-getVertScrollbar()->getScrollPosition()));
    
    // this bias is the absolute position that 0 on the scrollbars represent.
    // Allows the pane to function correctly with negatively positioned content.
    UVector2 bias(cegui_absdim(d_contentRect.d_left),
                  cegui_absdim(d_contentRect.d_top));
    
    // set the new container pane position to be what the scrollbars request
    // minus any bias generated by the location of the content.
    getScrolledContainer()->setPosition(basePos - bias);
    
    UpdateChildsShow();

    // UpdateChildsShow里面有很多BUG.所以另搞了一套系统。
	UpdateChildsViewState();
}

void ScrollablePane::setPaneScrollMode(int b)
{
    d_PaneScrollMode=(enumPageScrollMode)b;
    if (d_horzScrollbar)
    {
        d_horzScrollbar->removeEvent(Scrollbar::EventSlideStopped);
        if(d_PaneScrollMode == eSinglePageScroll || d_PaneScrollMode == eMultiPageScroll)
        {
            d_horzScrollbar->subscribeEvent(
                                          Scrollbar::EventSlideStopped,
                                          Event::Subscriber(&ScrollablePane::onScrollPageChanged, this));
        }
    }
    
    if (d_vertScrollbar)
    {
         d_vertScrollbar->removeEvent(Scrollbar::EventSlideStopped);
        if(d_PaneScrollMode == eSinglePageScroll || d_PaneScrollMode == eMultiPageScroll)
        {
            d_vertScrollbar->subscribeEvent(
                                          Scrollbar::EventSlideStopped,
                                          Event::Subscriber(&ScrollablePane::onScrollPageChanged, this));
        }
    }

}
    
    
void ScrollablePane::UpdateChildsShow()
{
    Scrollbar* vertScrollbar = getVertScrollbar();
    
    if (d_EnableHorzScrollBar)
    {
        vertScrollbar=getHorzScrollbar();
    }
    
    if (vertScrollbar) {
        
        float vertScrollPos = vertScrollbar->getScrollPosition();
        //暂时提的需求
//        if (vertScrollPos < 0) {
//            vertScrollbar->setScrollPosition(0.f);
//        }
        
        vertScrollPos = vertScrollbar->getScrollPosition();
        
        //vertScrollbar->onMouseDrag(pRecognizer);
        float vertDocSize = vertScrollbar->getDocumentSize();
        float vertPageSize = vertScrollbar->getPageSize();
        
        
        
        
        int count = getScrolledContainer()->getChildCount();
        CEGUI::Window* pScrolledWnd = getScrolledContainer();
        Size parentSize=pScrolledWnd->getPixelSize();
        for (size_t i=0; i<(size_t)count; i++) {
            Window* pWnd = pScrolledWnd->getChildAtIdx(i);
            //Window* pWnd = *it;
            if (pWnd != NULL) {
                float xPos = pWnd->getPosition().d_x.d_scale*parentSize.d_width+pWnd->getPosition().d_x.d_offset;
                float yPos = pWnd->getPosition().d_y.d_scale*parentSize.d_height+pWnd->getPosition().d_y.d_offset;
                
                
                //printf("name:%s, yPos:%f vertPos:%f\n", pWnd->getName().c_str(), yPos.d_offset, vertScrollPos.d_offset);
                
                float wndwidth = pWnd->getPixelSize().d_width;
                float wndheight = pWnd->getPixelSize().d_height;
                
                bool bWndShow=true;
                
                float testPos=yPos;
                float testSize=wndheight;
                
                if (d_EnableHorzScrollBar) {
                    testPos=xPos;
                    testSize=wndwidth;
                }
                if (testPos + testSize < vertScrollPos || testPos > vertScrollPos + vertPageSize/*vertPageSize.d_offset - vertScrollPos.d_offset || yPos.d_offset < - vertScrollPos.d_offset*/) {
                    //pScrolledWnd->removeWindowFromDrawList(pWnd);
                    //pWnd->setVisible(false);
                    pWnd->setDrawIt(false);
                }
                else{
                    //pWnd->setVisible(true);
                    pWnd->setDrawIt(true);
                }
                
            }
        }
        this->invalidate();
        //printf("count:%d\n", count);
    }
    
    
    
    
    //    UDim horzDocSize = cegui_absdim(horzScrollbar->getDocumentSize());
    //    UDim horzPageSize = cegui_absdim(horzScrollbar->getPageSize());
    //    UDim horzScrollPos = cegui_absdim(horzScrollbar->getScrollPosition());
    
}
    
//----------------------------------------------------------------------------//
bool ScrollablePane::onScrollPageChanged(const EventArgs&)
{
    WindowEventArgs args(this);
    fireEvent(EventScrollPageChanged, args, EventNamespace);
    return true;
}
    

//----------------------------------------------------------------------------//
void ScrollablePane::onContentPaneChanged(WindowEventArgs& e)
{
    fireEvent(EventContentPaneChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void ScrollablePane::onVertScrollbarModeChanged(WindowEventArgs& e)
{
    fireEvent(EventVertScrollbarModeChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void ScrollablePane::onHorzScrollbarModeChanged(WindowEventArgs& e)
{
    fireEvent(EventHorzScrollbarModeChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void ScrollablePane::onAutoSizeSettingChanged(WindowEventArgs& e)
{
    fireEvent(EventAutoSizeSettingChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void ScrollablePane::onContentPaneScrolled(WindowEventArgs& e)
{
    updateContainerPosition();
    fireEvent(EventContentPaneScrolled, e, EventNamespace);
}

//----------------------------------------------------------------------------//
bool ScrollablePane::handleScrollChange(const EventArgs&)
{
    WindowEventArgs args(this);
    onContentPaneScrolled(args);
    return true;
}

//----------------------------------------------------------------------------//
bool ScrollablePane::handleContentAreaChange(const EventArgs&)
{
	if (!getWindowRenderer())
		return true;

    Scrollbar* vertScrollbar = getVertScrollbar();
    Scrollbar* horzScrollbar = getHorzScrollbar();
    
    // get updated extents of the content
    Rect contentArea(getScrolledContainer()->getContentArea());
    
    // calculate any change on the top and left edges.
    float xChange = contentArea.d_left - d_contentRect.d_left;
    float yChange = contentArea.d_top - d_contentRect.d_top;
    
    // store new content extents information
    d_contentRect = contentArea;
    
    configureScrollbars();
    
    // update scrollbar positions (which causes container pane to be moved as needed).
    horzScrollbar->setScrollPosition(horzScrollbar->getScrollPosition() - xChange);
    vertScrollbar->setScrollPosition(vertScrollbar->getScrollPosition() - yChange);
    
    // this call may already have been made if the scroll positions changed.  The call
    // is required here for cases where the top/left 'bias' has changed; in which
    // case the scroll position notification may or may not have been fired.
    if (xChange || yChange)
        updateContainerPosition();
    
    // fire event
    WindowEventArgs args(this);
    onContentPaneChanged(args);
    
    return true;
}

//----------------------------------------------------------------------------//
bool ScrollablePane::handleAutoSizePaneChanged(const EventArgs&)
{
    // just forward event to client.
    WindowEventArgs args(this);
    fireEvent(EventAutoSizeSettingChanged, args, EventNamespace);
	return args.handled>0?true:false;
}

//
bool ScrollablePane::handleLostForce(const EventArgs& e)
{
	ActivationEventArgs* aea = (ActivationEventArgs*)&e;
	for (size_t i = 0; i < aea->window->getChildCount(); ++i)
	{
		Window* childWnd = aea->window->getChildAtIdx(i);
		PushButton* childBtn = dynamic_cast<PushButton*>(childWnd);
		if (childBtn&&childBtn->isLeftBtnDown())
		{
			MouseEventArgs args(childWnd);
			args.button = MouseButton::LeftButton;
			args.clickCount = 1;
			args.handled = 0;
			args.position.d_x = -1;
			args.position.d_y = -1;
			childWnd->onLostForce(args);
		}
	}

	return true;
}

//----------------------------------------------------------------------------//
void ScrollablePane::addChild_impl(Window* wnd)
{
    // null is not a valid window pointer!
    assert(wnd != 0);
    
    // See if this is an internally generated window
    // (will have AutoWidgetNameSuffix in the name)
    if (wnd->getName().find(AutoWidgetNameSuffix) != String::npos)
    {
        // This is an internal widget, so should be added normally.
        Window::addChild_impl(wnd);
    }
    // this is a client window/widget, so should be added to the pane container.
    else
    {
        // container should always be valid by the time we're adding client
        // controls
        getScrolledContainer()->addChildWindow(wnd);
    }
    
    wnd->subscriberEventDrag(Event::Subscriber(&ScrollablePane::HandleMouseDragChild, this));
    wnd->subscribeEvent(Window::EventMouseButtonDown, Event::Subscriber(&ScrollablePane::HandleChildMouseButtonDown, this));
    EnableAllChildDrag(wnd);
}
    
void ScrollablePane::EnableAllChildDrag(CEGUI::Window *pwindow) {
    if (pwindow == NULL)
        return;
    const Window* pContainer=getContentPane();
    for (size_t i = 0; i < pwindow->getChildCount(); i++) {
        Window* pChild = pwindow->getChildAtIdx(i);
        if (pChild->isAutoWindow()&&pChild!=pContainer) {
            continue;
        }
        pChild->subscriberEventDrag(Event::Subscriber(&ScrollablePane::HandleMouseDrag, this));
        pChild->subscribeEvent(Window::EventMouseButtonDown, Event::Subscriber(&ScrollablePane::HandleChildMouseButtonDown, this));
        EnableAllChildDrag(pChild);
    }
}

void ScrollablePane::EnableChildDrag(CEGUI::Window *pwindow) {
	if (pwindow == NULL)
		return;
	pwindow->subscriberEventDrag(Event::Subscriber(&ScrollablePane::HandleMouseDrag, this));
	pwindow->subscribeEvent(Window::EventMouseButtonDown, Event::Subscriber(&ScrollablePane::HandleChildMouseButtonDown, this));
	EnableAllChildDrag(pwindow);
}

//----------------------------------------------------------------------------//
void ScrollablePane::removeChild_impl(Window* wnd)
{
    // null is not a valid window pointer!
    assert(wnd != 0);
    
    // See if this is an internally generated window
    // (will have AutoWidgetNameSuffix in the name)
    if (wnd->getName().find(AutoWidgetNameSuffix) != String::npos)
    {
        // This is an internal widget, so should be removed normally.
        Window::removeChild_impl(wnd);
    }
    // this is a client window/widget, so should be removed from the pane
    // container.
    else
    {
        // container should always be valid by the time we're handling client
        // controls
        getScrolledContainer()->removeChildWindow(wnd);
    }
}

//----------------------------------------------------------------------------//
void ScrollablePane::onSized(WindowEventArgs& e)
{
    Window::onSized(e);
    configureScrollbars();
    updateContainerPosition();
    
    ++e.handled;
}

//----------------------------------------------------------------------------//
void ScrollablePane::onMouseWheel(MouseEventArgs& e)
{
    // base class processing.
    Window::onMouseWheel(e);
    
    Scrollbar* vertScrollbar = getVertScrollbar();
    Scrollbar* horzScrollbar = getHorzScrollbar();
    
    if (vertScrollbar->isVisible() &&
        (vertScrollbar->getDocumentSize() > vertScrollbar->getPageSize()))
    {
        vertScrollbar->setScrollPosition(vertScrollbar->getScrollPosition() +
                            vertScrollbar->getStepSize() * -e.wheelChange);
    }
    else if (horzScrollbar->isVisible() &&
             (horzScrollbar->getDocumentSize() > horzScrollbar->getPageSize()))
    {
        horzScrollbar->setScrollPosition(horzScrollbar->getScrollPosition() +
                            horzScrollbar->getStepSize() * -e.wheelChange);
    }
    
    ++e.handled;
}
    
void ScrollablePane::onMouseSlide(MouseEventArgs& e)
{
//    Window::onMouseSlide(e);
    Scrollbar* vertScrollbar = getVertScrollbar();
    if (vertScrollbar) {
        vertScrollbar->onMouseSlide(e);
    }
}

bool ScrollablePane::HandleMouseDragChild(const EventArgs& e)
{
	const GestureEventArgs& ge = (const GestureEventArgs&)e;
	Gesture::CEGUIPanGestureRecognizer* pRecognizer = dynamic_cast<Gesture::CEGUIPanGestureRecognizer*>(ge.d_Recognizer);
	if (pRecognizer&&pRecognizer->getPushWndName() == getContentPane()->getName())
	{
		return true;
	}

	return ScrollablePane::HandleMouseDrag(e);
}

bool ScrollablePane::HandleMouseDrag(const EventArgs& e)
{
    if (!ExistNonAutoChildren())
    {
        return true;
    }
	if (!d_EnableScrollBarDrag)
	{
		MouseEventArgs args(this);
		//CEGUI::Window::onMouseMove(args);
		CEGUI::Window::onMouseButtonUp(args);
		return true;
	}
	
    const GestureEventArgs& ge = (const GestureEventArgs&)e;
    Gesture::CEGUIGestureRecognizer* pRecognizer = ge.d_Recognizer;
    Window::onMouseDrag(pRecognizer);

//    if (pRecognizer)
//    {
//        Gesture::CEGUIPanGestureRecognizer* pPan=(Gesture::CEGUIPanGestureRecognizer*)pRecognizer;
//        Gesture::UIPanGestureRecognizerDirection dir=pPan->GetPanDirection();
//        
//        if (d_EnableHorzScrollBar
//            &&dir!=Gesture::UIPanGestureRecognizerDirectionLeft
//            &&dir!=Gesture::UIPanGestureRecognizerDirectionRight
//            )
//        {
//            return true;
//        }
//    }
    
    Scrollbar* pScrollbar = getVertScrollbar();
    if (d_EnableHorzScrollBar)
    {
        pScrollbar=getHorzScrollbar();
    }
    if (pScrollbar) {
        bool r = pScrollbar->onMouseDrag(pRecognizer);
        //if(!r)
        //    return false;
        if (pRecognizer->GetState() == Gesture::GestureRecognizerStateEnded)
        {
            if (pScrollbar->getScrollPosition() < -20.0) {
                fireEvent(EventPrePage, const_cast<CEGUI::EventArgs&>(e), EventNamespace);
            }
            else if (pScrollbar->getPageSize() < pScrollbar->getDocumentSize())
            {
                if ((pScrollbar->getScrollPosition() - (pScrollbar->getDocumentSize() - pScrollbar->getPageSize())) > 20.0) {
                    fireEvent(EventNextPage, const_cast<CEGUI::EventArgs&>(e), EventNamespace);
                }
            }
        }
    }
    // pRecognizer->fireGestureEvent();
    const MouseEventArgs* mouseArgs = (MouseEventArgs*)pRecognizer->GetEvent();
	if (mouseArgs && mouseArgs->window) {
        mouseArgs->window->onMouseDrag(pRecognizer);
    }

    return true;
}

//----------------------------------------------------------------------------//
void ScrollablePane::addScrollablePaneProperties(void)
{
    addProperty(&d_horzScrollbarProperty);
    addProperty(&d_vertScrollbarProperty);
    addProperty(&d_autoSizedProperty);
    addProperty(&d_contentAreaProperty);
    addProperty(&d_horzStepProperty);
    addProperty(&d_horzOverlapProperty);
    addProperty(&d_horzScrollPositionProperty);
    addProperty(&d_vertStepProperty);
    addProperty(&d_vertOverlapProperty);
    addProperty(&d_vertScrollPositionProperty);
    addProperty(&d_paneScrollModeProperty);
}

//----------------------------------------------------------------------------//
Scrollbar* ScrollablePane::getVertScrollbar() const
{
    if(d_vertScrollbar != NULL)
        return d_vertScrollbar;
    d_vertScrollbar = static_cast<Scrollbar*>(
        WindowManager::getSingleton().getWindow(
            getName() + VertScrollbarNameSuffix));
    return d_vertScrollbar;
}

//----------------------------------------------------------------------------//
Scrollbar* ScrollablePane::getHorzScrollbar() const
{
    if(d_horzScrollbar != NULL)
        return d_horzScrollbar;
    d_horzScrollbar = static_cast<Scrollbar*>(
        WindowManager::getSingleton().getWindow(
            getName() + HorzScrollbarNameSuffix));
    return d_horzScrollbar;
}

//----------------------------------------------------------------------------//
ScrolledContainer* ScrollablePane::getScrolledContainer() const
{
    if(d_contentPane != NULL)
        return d_contentPane;
    d_contentPane =  static_cast<ScrolledContainer*>(
        WindowManager::getSingleton().getWindow(
            getName() + ScrolledContainerNameSuffix));
    return d_contentPane;
}

//----------------------------------------------------------------------------//
Rect ScrollablePane::getViewableArea() const
{
    if (!d_windowRenderer)
        throw InvalidRequestException("ScrollablePane::getViewableArea: "
            "This function must be implemented by the window renderer module");
    
    ScrollablePaneWindowRenderer* wr =
        static_cast<ScrollablePaneWindowRenderer*>(d_windowRenderer);
    return wr->getViewableArea();
}

//----------------------------------------------------------------------------//
void ScrollablePane::destroy(void)
{
    // detach from events on content pane
	/*
    d_contentChangedConn->disconnect();
    d_autoSizeChangedConn->disconnect();
	d_lostForceConn->disconnect();
    */
    // now do the cleanup
    Window::destroy();
}

bool ScrollablePane::HandleChildMouseButtonDown(const CEGUI::EventArgs &e) {
	if (!d_EnableScrollBarDrag)
	{
		MouseEventArgs args(this);
		CEGUI::Window::onMouseButtonDown(args);
		return true;
	}
    Scrollbar* pScrollbar = getVertScrollbar();
    
    if(d_EnableHorzScrollBar)
    {
        pScrollbar=getHorzScrollbar();
    }
    
    if (pScrollbar) {
		if (d_PaneScrollMode != eMultiPageScroll)
		{
			pScrollbar->Stop();
			return true;
		}

		if(pScrollbar->getSlidState() == StopState)
		{
			return true;
		}
		int count = getScrolledContainer()->getChildCount();
		CEGUI::Window* pScrolledWnd = getScrolledContainer();
		Size parentSize=pScrolledWnd->getPixelSize();
		float scrollPos = pScrollbar->getScrollPosition();
		for (int i=0; i<count; i++) {
			Window* pWnd = pScrolledWnd->getChildAtIdx(i);
			if(pWnd == NULL)
			{
				break;
			}

			float testPos = pWnd->getPosition().d_y.d_scale*parentSize.d_height+pWnd->getPosition().d_y.d_offset;
			float testSize = pWnd->getPixelSize().d_height;

			if (d_EnableHorzScrollBar) {
				testPos=pWnd->getPosition().d_x.d_scale*parentSize.d_width+pWnd->getPosition().d_x.d_offset;
				testSize=pWnd->getPixelSize().d_width;
			}

			if (scrollPos <= testSize / 2 ) {
				pScrollbar->setScrollPosition(testPos);
				break;
			}

			if (testPos+testSize > scrollPos)
			{
				if ((testPos + testSize - scrollPos) < testSize / 2)
				{
					pScrollbar->setScrollPosition(testPos + testSize);
				}
				else
				{
					pScrollbar->setScrollPosition(testPos);
				}
				break;
			}

		}
        pScrollbar->Stop();
    }
    return false;
}
    
void ScrollablePane::onMouseButtonDown(CEGUI::MouseEventArgs &e)
{
    Scrollbar* pScrollbar = getVertScrollbar();
    
    if(d_EnableHorzScrollBar)
    {
        pScrollbar=getHorzScrollbar();
        
    }
    
    
    if (pScrollbar) {
        pScrollbar->Stop();
    }
    CEGUI::Window::onMouseButtonDown(e);
}

void ScrollablePane::EnableScrollDrag(bool bEnbale)
{
	d_EnableScrollBarDrag=bEnbale;
}

void ScrollablePane::EnableVertScrollBar(bool bEnbale)
{
    
    d_EnableVertScrollBar=bEnbale;
    EnableHorzScrollBar(!bEnbale);
}

void ScrollablePane::EnableHorzScrollBar(bool bEnbale)
{
    d_EnableHorzScrollBar=bEnbale;
    if (bEnbale)
    {
        Scrollbar* horzScrollbar = getHorzScrollbar();
        if (horzScrollbar)
        {
            horzScrollbar->SetPanForVert(false);
        }
    }
    
}
    
bool ScrollablePane::ExistNonAutoChildren()
{
    if (getContentPane())
    {
        size_t childNum=getContentPane()->getChildCount();
        if (childNum>0)
        {
            return true;
        }
    }
        
    return false;
}
    
void ScrollablePane::cleanupNonAutoChildren()
{
    ScrolledContainer* pPane=getScrolledContainer();
    if (pPane!=NULL) {
        pPane->cleanupNonAutoChildren();
    }
}


void ScrollablePane::updateSelf(float elapsed)
{
    Window::updateSelf(elapsed);
}

void ScrollablePane::StartPageScroll(float orignial_v)
{
    
}
    
void ScrollablePane::amendSlideDesPos(float& curPos, float& desPos, float& slideTime, float& v)
{
    Scrollbar* scrollbar = getVertScrollbar();
    if (d_EnableHorzScrollBar)
        scrollbar=getHorzScrollbar();
    if (!scrollbar)
        return;
    float scrollPos = 0.0f;
    if (d_PaneScrollMode == eNoPageScroll)
        return;
    else if (d_PaneScrollMode == eSinglePageScroll)
    {
        slideTime = 0.3f;
        scrollPos = curPos;
    }
    else if (d_PaneScrollMode == eMultiPageScroll)
    {
        scrollPos = desPos;
    }
	
    int count = getScrolledContainer()->getChildCount();
    CEGUI::Window* pScrolledWnd = getScrolledContainer();
    Size parentSize=pScrolledWnd->getPixelSize();
    for (size_t i=0; i<(size_t)count; i++) {
        Window* pWnd = pScrolledWnd->getChildAtIdx(i);
        
        if (pWnd != NULL) {

            float testPos = pWnd->getPosition().d_y.d_scale*parentSize.d_height+pWnd->getPosition().d_y.d_offset;
            float testSize = pWnd->getPixelSize().d_height;
            
            if (d_EnableHorzScrollBar) {
                testPos=pWnd->getPosition().d_x.d_scale*parentSize.d_width+pWnd->getPosition().d_x.d_offset;
                testSize=pWnd->getPixelSize().d_width;
            }
            if (testPos+testSize > scrollPos)
            {
                d_endPos = testPos;
                if (d_PaneScrollMode == eMultiPageScroll)
                {
					if ((testPos + testSize - scrollPos) < testSize / 2)
					{
						d_endPos += testSize;
					}
					//if(v > 0)
					//    d_endPos += testSize;
                    break;
                }
                if ((testPos + testSize - scrollPos) < testSize / 2)
                {
                    d_endPos += testSize;
                    if(v < -30)//50
                    {
                        d_endPos -= testSize;
                    }
                }
                else
                {
                    if(v > 30)
                    {
                        d_endPos += testSize;
                    }
                }
                break;
            }
            
        }
    }
    desPos = d_endPos;
    
    return;
}
    
//----------------------------------------------------------------------------//
    
} // End of  CEGUI namespace section
