/***********************************************************************
     filename:  CEGUISpeicalTree.cpp
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
#include "CEGUIExceptions.h"
#include "CEGUIWindowManager.h"
#include "CEGUICoordConverter.h"
#include "CEGUIWindowManager.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "elements/CEGUISpecialTreeItem.h"
#include "elements/CEGUISpecialTree.h"
#include "elements/CEGUIScrollbar.h"
#include "WindowRendererSets/Falagard/FalSpecialTree.h"

#include <algorithm>

// Start of CEGUI namespace section
namespace CEGUI
{

const String SpecialTree::EventNamespace("SpecialTree");
const String SpecialTree::WidgetTypeName("CEGUI/SpecialTree");


/*************************************************************************
    Constants
*************************************************************************/
// event names
const String SpecialTree::EventListContentsChanged( "ListItemsChanged" );
const String SpecialTree::EventVertScrollbarModeChanged( "VertScrollModeChanged" );
const String SpecialTree::EventHorzScrollbarModeChanged( "HorzScrollModeChanged" );
const String SpecialTree::EventBranchOpened( "BranchOpened" );
const String SpecialTree::EventBranchClosed( "BranchClosed" );
const String SpecialTree::EventOpenItemInfo( "OpenInfo" );
const String SpecialTree::EventCloseItemInfo( "CloseInfo" );

const float SpecialTree::CompentHeight = 20.0f;
const float SpecialTree::LeftOffect = 16.0f;
const float SpecialTree::buttonWH = 14.0f;

/*************************************************************************
    Constructor for Tree base class.
*************************************************************************/
SpecialTree::SpecialTree(const String& type, const String& name) :
    Window(type, name),
    d_sorted(true),
    d_forceVertScroll(false),
    d_forceHorzScroll(false),
    d_vertScrollbar(0),
    //d_horzScrollbar(0),
	d_OpenInfoItem(0),
    d_openButtonImagery(0),
    d_closeButtonImagery(0),
	d_OpenInfoImagery(0),
	d_CloseInfoImagery(0),
	d_LastMouseOnComment(0)
{
    // add new events specific to tree.
    addTreeEvents();
	d_listItems.clear();
	d_itemArea = Rect(0.0f,0.0f,0.0f,0.0f);
	SetDragMoveEnable(false);
    EnableDrag(true);

}

SpecialTree::SpecialTree(const String& type) :
	Window(type)
{
}

const SpecialTree& SpecialTree::operator=(const SpecialTree& t)
{
	Window::operator=(t);
	d_sorted = t.d_sorted;
	d_forceVertScroll = t.d_forceVertScroll;
	d_forceHorzScroll = t.d_forceHorzScroll;
	d_vertScrollbar = t.d_vertScrollbar;
	d_listItems = t.d_listItems;
	d_OpenInfoItem = t.d_OpenInfoItem;
	d_openButtonImagery = t.d_openButtonImagery;
	d_closeButtonImagery = t.d_closeButtonImagery;
	d_OpenInfoImagery = t.d_OpenInfoImagery;
	d_CloseInfoImagery = t.d_CloseInfoImagery;
	d_LastMouseOnComment = t.d_LastMouseOnComment;
	d_itemArea = t.d_itemArea;
	return *this;
}

Window* SpecialTree::clone(Window* wnd)
{
	SpecialTree* retWnd = (SpecialTree*)wnd;
	if (retWnd == NULL)
		retWnd = new SpecialTree(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == SpecialTree::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

void SpecialTree::setLookNFeel(const String& look, bool bClone)
{
    Window::setLookNFeel( look, bClone );
    initialise();
}

/*************************************************************************
    Destructor for Tree base class.
*************************************************************************/
SpecialTree::~SpecialTree(void)
{
    resetList_impl();
}

/*************************************************************************
    Initialise the Window based object ready for use.
*************************************************************************/
void SpecialTree::initialise(void)
{
    // get WidgetLookFeel for the assigned look.
    const WidgetLookFeel &wlf = WidgetLookManager::getSingleton().getWidgetLook(d_lookName);
    const ImagerySection &tempOpenImagery = wlf.getImagerySection("OpenTreeButton");
    const ImagerySection &tempCloseImagery = wlf.getImagerySection("CloseTreeButton");
	const ImagerySection &tempOpenInfoImagery = wlf.getImagerySection("OpenInfoButton");
	const ImagerySection &tempCloseInfoImagery = wlf.getImagerySection("CloseInfoButton");
    d_openButtonImagery = (ImagerySection *)&tempOpenImagery;
    d_closeButtonImagery = (ImagerySection *)&tempCloseImagery;
	d_OpenInfoImagery = (ImagerySection *)&tempOpenInfoImagery;
	d_CloseInfoImagery = (ImagerySection *)&tempCloseInfoImagery;
    
    // create the component sub-widgets
    d_vertScrollbar = createVertScrollbar(getName() + "__auto_vscrollbar__");
    //d_horzScrollbar = createHorzScrollbar(getName() + "__auto_hscrollbar__");
    
    addChildWindow(d_vertScrollbar);
    //addChildWindow(d_horzScrollbar);
    
    d_vertScrollbar->subscribeEvent(Scrollbar::EventScrollPositionChanged, Event::Subscriber(&SpecialTree::handle_scrollChange, this));
    //d_horzScrollbar->subscribeEvent(Scrollbar::EventScrollPositionChanged, Event::Subscriber(&SpecialTree::handle_scrollChange, this));

	d_vertScrollbar->hide();
	//d_horzScrollbar->hide();
    
    configureScrollbars();
    performChildWindowLayout();
}

SpecialTreeItem* SpecialTree::findItemWithIDFromList(uint searchID)
{
    size_t itemCount = d_listItems.size();
    
    for (size_t index = 0; index < itemCount; ++index)
    {    
		if (d_listItems[index]->getID() == searchID)
			return d_listItems[index];
    }
    
    return 0;
}

/*************************************************************************
    Return whether the specified TreeItem is in the List
*************************************************************************/
bool SpecialTree::isTreeItemInList(const SpecialTreeItem* item) const
{
	if(item == 0)
		return false;

	size_t itemCount = d_listItems.size();

	for (size_t index = 0; index < itemCount; ++index)
	{    
		if (d_listItems[index]->getID() == item->getID())
			return true;
	}

	return false;
}

/*************************************************************************
    Remove all items from the list.
*************************************************************************/
void SpecialTree::resetList(void)
{
    if (resetList_impl())
    {
        WindowEventArgs args(this);
        onListContentsChanged(args);
    }
}

/*************************************************************************
    Add the given TreeItem to the list.
*************************************************************************/
void SpecialTree::addItem(SpecialTreeItem* item)
{
    if (item != 0)
    {
		if(item->getChildrenCompentsCount() == 0)
			return;

        // establish ownership
        item->setOwnerWindow(this);
        
        // if sorting is enabled, re-sort the list
        if (isSortEnabled())
        {
            d_listItems.insert(std::upper_bound(d_listItems.begin(), d_listItems.end(), item, &lbi_greater), item);
        }
        // not sorted, just stick it on the end.
        else
        {
			//新加的元素在最前面
			d_listItems.insert(d_listItems.begin(),item);
            //d_listItems.push_back(item);
        }
        
        WindowEventArgs args(this);
        onListContentsChanged(args);
    }
}

/*************************************************************************
    Removes the given item from the tree.
*************************************************************************/
void SpecialTree::removeItem(const SpecialTreeItem* item)
{
    if (item != 0)
    {
        LBSpecialItemList::iterator pos = std::find(d_listItems.begin(), d_listItems.end(), item);
        
        // if item is in the list
        if (pos != d_listItems.end())
        {
            // disown item
            (*pos)->setOwnerWindow(0);            
            // remove item
			if(d_LastMouseOnComment)
			{
				d_LastMouseOnComment->onMouseLeave();
				d_LastMouseOnComment = NULL;
			}
            d_listItems.erase(pos);
			
            delete item;         
            WindowEventArgs args(this);
            onListContentsChanged(args);
        }
    }
}

/*************************************************************************
    Set whether the list should be sorted.
*************************************************************************/
void SpecialTree::setSortingEnabled(bool setting)
{
    // only react if the setting will change
    if (d_sorted != setting)
    {
        d_sorted = setting;
        
        // if we are enabling sorting, we need to sort the list
        if (d_sorted)
        {
            std::sort(d_listItems.begin(), d_listItems.end(), &lbi_greater);
        }
    }
}


void SpecialTree::reSort()
{
	std::sort(d_listItems.begin(), d_listItems.end(), &lbi_greater);
	invalidate();

}

/*************************************************************************
    Set whether the vertical scroll bar should always be shown.
*************************************************************************/
void SpecialTree::setShowVertScrollbar(bool setting)
{
    if (d_forceVertScroll != setting)
    {
        d_forceVertScroll = setting;
        
        configureScrollbars();
        WindowEventArgs args(this);
        onVertScrollbarModeChanged(args);
    }
}

/*************************************************************************
    Set whether the horizontal scroll bar should always be shown.
*************************************************************************/
void SpecialTree::setShowHorzScrollbar(bool setting)
{
    if (d_forceHorzScroll != setting)
    {
        d_forceHorzScroll = setting;
        
        configureScrollbars();
        WindowEventArgs args(this);
        onHorzScrollbarModeChanged(args);
    }
}
/*************************************************************************
    Causes the tree to update it's internal state after changes have
    been made to one or more attached TreeItem objects.
*************************************************************************/
void SpecialTree::handleUpdatedItemData(void)
{
    //configureScrollbars();
    invalidate();
}

/*************************************************************************
    Perform the actual rendering for this Window.
*************************************************************************/
void SpecialTree::populateGeometryBuffer()
{
    // get the derived class to render general stuff before we handle the items
    cacheTreeBaseImagery();
    
    // Render list items
    Vector2  itemPos;
    float    widest = getWidestItemWidth();
    
    // calculate position of area we have to render into
    Rect itemsArea(getTreeRenderArea());
	if (d_windowRenderer)
	{
		FalagardSpecialTree* TreeRender=(FalagardSpecialTree*)d_windowRenderer;
		if (TreeRender)
		{
			itemsArea=TreeRender->getTreeRenderArea();
		}

	}
    //Rect itemsArea(0,0,500,500);
    
    // set up some initial positional details for items
    itemPos.d_x = itemsArea.d_left;// - d_horzScrollbar->getScrollPosition();
    itemPos.d_y = itemsArea.d_top - d_vertScrollbar->getScrollPosition();
    
    drawItemList(itemsArea, widest, itemPos, d_geometry, getEffectiveAlpha());
}

// Recursive!
void SpecialTree::drawItemList(Rect& itemsArea, float widest, Vector2& itemPos, GeometryBuffer* buffer, float alpha)
{
    if (d_listItems.empty())
        return;
   
	//Rect  renderArea(getTreeRenderArea());
	//if (d_windowRenderer)
	//{
	//	FalagardSpecialTree* TreeRender=(FalagardSpecialTree*)d_windowRenderer;
	//	if (TreeRender)
	//	{
	//		renderArea=TreeRender->getTreeRenderArea();
	//	}

	//}
    // loop through the items
    Size     itemSize;
    Rect     itemClipper, itemRect;
    size_t   itemCount = d_listItems.size();
	itemPos.d_y += 1;

    for (size_t i = 0; i < itemCount; ++i)
    {
        itemSize.d_height = d_listItems[i]->getPixelSize().d_height + 2;
        
        // allow item to have full width of box if this is wider than items
        itemSize.d_width = ceguimax(itemsArea.getWidth(), widest);
        
        // calculate destination area for this item.
        itemRect.d_left = itemPos.d_x;
        itemRect.d_top  = itemPos.d_y;
        itemRect.setSize(itemSize);
        itemClipper = itemRect.getIntersection(itemsArea);
        itemRect.d_left += buttonWH + 4.0f;     // start text past open/close buttons
     
        // Process this item's list if it has items in it.
        if (d_listItems[i]->getChildrenCompentsCount() > 0)
        {
			d_listItems[i]->draw(buffer, itemRect, alpha, &itemClipper);

            Rect buttonRenderRect;
            buttonRenderRect.d_left = itemPos.d_x;
            buttonRenderRect.d_right = buttonRenderRect.d_left + buttonWH;
            buttonRenderRect.d_top = itemPos.d_y;
            buttonRenderRect.d_bottom = buttonRenderRect.d_top + buttonWH;
            d_listItems[i]->setButtonLocation(buttonRenderRect);
            
            if (d_listItems[i]->getIsOpen())
            {
                // Draw the Close button 
                d_closeButtonImagery->render(*this, buttonRenderRect, 0, &itemClipper);      
                // update position ready for next item
                itemPos.d_y += itemSize.d_height;
               // itemPos.d_x += buttonWH + LeftOffect;		
				float DrawWidth = itemRect.d_left;//0.0f;
				
				SpecialTreeItem::ChildrenCompentList& compentlist = d_listItems[i]->getChildrenCompentList();
				for(size_t index = 0; index < compentlist.size(); index++)
				{
					 Size componentsize=compentlist[index]->getPixelSize();/*d_FormatComponents[i].getPixelSize()*/;

					 ColourRect final_cols(0xFFFFFFFF);
					 final_cols.modulateAlpha(alpha);

					 Point pos(DrawWidth,itemPos.d_y);
					 compentlist[index]->draw(buffer,
						 pos,
						 &final_cols, &itemsArea,0.0f,0.0f,false);
					 DrawWidth+=componentsize.d_width;
				}
				itemPos.d_y += CompentHeight;
                //itemPos.d_x = 0;
            }
            else
            {
               d_openButtonImagery->render(*this, buttonRenderRect, 0, &itemClipper);           
                // update position ready for next item
                itemPos.d_y += itemSize.d_height;
            }
			//详细信息按钮
			buttonRenderRect.d_left = itemsArea.d_right - 19;
			buttonRenderRect.d_right = itemsArea.d_right;
			//buttonRenderRect.setWidth(19);
			d_listItems[i]->setInfoBtnLocation(buttonRenderRect);
			if(d_listItems[i]->getIsOpenInfo())
			{
				d_CloseInfoImagery->render(*this, buttonRenderRect, 0, &itemClipper); 
			}
			else
			{
				d_OpenInfoImagery->render(*this, buttonRenderRect, 0, &itemClipper); 
			}	
        }
//         else
//         {
//             // update position ready for next item
//             itemPos.d_y += itemSize.d_height;
//         }
    }
    
    // Successfully drew all items, so vertical scrollbar not needed.
    //   setShowVertScrollbar(false);
}

#define HORIZONTAL_STEP_SIZE_DIVISOR   20.0f

/*************************************************************************
    display required integrated scroll bars according to current state
    of the tree and update their values.
*************************************************************************/
void SpecialTree::configureScrollbars(void)
{
    Rect  renderArea(getTreeRenderArea());
    if(renderArea.getWidth() < 1.0f || renderArea.getHeight()<1.0f)
		return;
    
    //This is becuase CEGUI IS GAY! and fires events before the item is initialized
    if(!d_vertScrollbar)
        d_vertScrollbar = createVertScrollbar(getName() + "__auto_vscrollbar__");
//     if(!d_horzScrollbar)
//         d_horzScrollbar = createHorzScrollbar(getName() + "__auto_hscrollbar__");
    
    float totalHeight = getTotalItemsHeight();
    float widestItem  = getWidestItemWidth();// + LeftOffect;

	bool bOldVertBarVis=d_vertScrollbar->isVisible(true);
	
    
    //
    // First show or hide the scroll bars as needed (or requested)
    //
    // show or hide vertical scroll bar as required (or as specified by option)
//    if ((totalHeight > renderArea.getHeight()) || d_forceVertScroll)
//    {
//        d_vertScrollbar->show();
//       // renderArea.d_right -= d_vertScrollbar->getWidth().d_offset + d_vertScrollbar->getXPosition().d_offset;
//       /* renderArea.d_right -= d_vertScrollbar->getAbsoluteWidth() + d_vertScrollbar->getAbsoluteXPosition();
//          show or hide horizontal scroll bar as required (or as specified by option)
//      
//        d_horzScrollbar->hide();
//        d_horzScrollbar->setScrollPosition(0);
//		*/
//       
//    }
//    else
    {
		d_vertScrollbar->hide();
		d_vertScrollbar->setScrollPosition(0);
        // show or hide horizontal scroll bar as required (or as specified by option)
        /*if ((widestItem > renderArea.getWidth()) || d_forceHorzScroll)
        {
            d_horzScrollbar->show();
            renderArea.d_bottom -= d_horzScrollbar->getHeight().d_offset;
            //renderArea.d_bottom -= d_vertScrollbar->getHeight().d_offset;
            
            // show or hide vertical scroll bar as required (or as specified by option)
            if ((totalHeight > renderArea.getHeight()) || d_forceVertScroll)
            {
                d_vertScrollbar->show();
                //            renderArea.d_right -= d_vertScrollbar->getAbsoluteWidth();
                renderArea.d_right -= d_vertScrollbar->getWidth().d_offset;
            }
            else
            {
                d_vertScrollbar->hide();
                d_vertScrollbar->setScrollPosition(0);
            }
        }
        else
        {
            d_vertScrollbar->hide();
            d_vertScrollbar->setScrollPosition(0);
           // d_horzScrollbar->hide();
            //d_horzScrollbar->setScrollPosition(0);
        }
		*/
    }

	bool bCurVertBarVis=d_vertScrollbar->isVisible(true);


	if (bOldVertBarVis!=bCurVertBarVis)
	{
		configureScrollbars();
	}
    
    //
    // Set up scroll bar values
    //
    
    float itemHeight;
//     if (d_listItems.size() > 0)
//         itemHeight = d_listItems[0]->getPixelSize().d_height + 2;
//     else
        itemHeight = 13.0f;
    
    d_vertScrollbar->setDocumentSize(PixelAligned(totalHeight));
    d_vertScrollbar->setPageSize(PixelAligned(renderArea.getHeight()));
    d_vertScrollbar->setStepSize(ceguimax(1.0f, PixelAligned(renderArea.getHeight() / itemHeight)));
    d_vertScrollbar->setScrollPosition(PixelAligned(d_vertScrollbar->getScrollPosition()));
    
    /*d_horzScrollbar->setDocumentSize(widestItem + d_vertScrollbar->getWidth().d_offset);
    //   d_horzScrollbar->setDocumentSize(widestItem + d_vertScrollbar->getAbsoluteWidth());
    d_horzScrollbar->setPageSize(renderArea.getWidth());
    d_horzScrollbar->setStepSize(ceguimax(1.0f, renderArea.getWidth() / HORIZONTAL_STEP_SIZE_DIVISOR));
    d_horzScrollbar->setScrollPosition(d_horzScrollbar->getScrollPosition());
	*/
}

/*************************************************************************
    Return the sum of all item heights
*************************************************************************/
float SpecialTree::getTotalItemsHeight(void) const
{
    float heightSum = 0;
	bool  bHaveCloseItem = false;
    
	size_t itemCount = d_listItems.size();
	for (size_t index = 0; index < itemCount; ++index)
	{
		heightSum += d_listItems[index]->getPixelSize().d_height + 2;
		if (d_listItems[index]->getIsOpen() && (d_listItems[index]->getChildrenCompentsCount() > 0))
			heightSum += CompentHeight;
		else if(!d_listItems[index]->getIsOpen() && !bHaveCloseItem)
			bHaveCloseItem = true;
	}

	if(bHaveCloseItem)
		heightSum+=7.0f;

    return heightSum;
}

/*************************************************************************
    Return the width of the widest item, including any white space to the left
    due to indenting.
*************************************************************************/
float SpecialTree::getWidestItemWidth(void) const
{
    float widest = 0;
	size_t itemCount = d_listItems.size();
	for (size_t i = 0; i < itemCount; ++i)
	{	
		float thisWidth = LeftOffect;
		if (d_listItems[i]->getIsOpen() && (d_listItems[i]->getChildrenCompentsCount() > 0))
		{	
			SpecialTreeItem::ChildrenCompentList& compentlist = d_listItems[i]->getChildrenCompentList();
			for(size_t index = 0; index < compentlist.size(); index++)
			{	
				Size componentsize=compentlist[index]->getPixelSize();/*d_FormatComponents[i].getPixelSize()*/;
				thisWidth+=componentsize.d_width;
			}
		}
		if (thisWidth > widest)
			widest = thisWidth;
	}

	return widest;
   
}

/*************************************************************************
    Return the TreeItem under the given window local pixel co-ordinate.
*************************************************************************/
SpecialTreeItem* SpecialTree::getItemAtPoint(const Point& pt, int* itemindex) const
{
    Rect renderArea(getTreeRenderArea());
    
    // point must be within the rendering area of the Tree.
    if (renderArea.isPointInRect(pt))
    {
        float y = renderArea.d_top - d_vertScrollbar->getScrollPosition();
        
        // test if point is above first item
        if (pt.d_y >= y)
		{
			size_t itemCount = d_listItems.size();

			for (size_t i = 0; i < itemCount; ++i)
			{
				y += d_listItems[i]->getPixelSize().d_height + 2;
				if (pt.d_y < y)
					return d_listItems[i];

				if (d_listItems[i]->getIsOpen())
				{
					y += CompentHeight;
					if (pt.d_y < y)
					{
						*itemindex =(int)i;
						return 0;
					}
				}
			}
		}
           
    }
    
    return 0;
}


/*************************************************************************
Return the TreeItem under the given window local pixel co-ordinate.
*************************************************************************/
RichEditboxComponent* SpecialTree::getCompentAtPoint(const Point& pt,int& itemindex) const
{
	if(itemindex > (int)d_listItems.size())
		return 0;

	if(d_listItems[itemindex]->getIsOpen())
	{
		Rect renderArea(getTreeRenderArea());
		float x = renderArea.d_left;// - d_horzScrollbar->getScrollPosition();

		float xoffset = LeftOffect;//子节点距离最左边的偏移量			
		float DrawWidth=0.0f;
		SpecialTreeItem::ChildrenCompentList& compentlist = d_listItems[itemindex]->getChildrenCompentList();
		for(size_t i = 0; i < compentlist.size(); i++)
		{
			Size componentsize=compentlist[i]->getPixelSize();/*d_FormatComponents[i].getPixelSize()*/;
			DrawWidth+=componentsize.d_width;
			if((pt.d_x -x ) > xoffset && (pt.d_x -x) < (xoffset + DrawWidth))
				return compentlist[i];
		}
	}

	return 0;
}

RichEditboxComponent* SpecialTree::getFirstGoToCompent()
{
	size_t itemCount = d_listItems.size();
	for (size_t i = 0; i < itemCount; ++i)
	{
		if (d_listItems[i]->getIsOpen())
		{
			SpecialTreeItem::ChildrenCompentList& compentlist = d_listItems[i]->getChildrenCompentList();
			for(size_t i = 0; i < compentlist.size(); i++)
			{
				if(compentlist[i]->GetType() == RichEditboxComponentType_GoToText)
				{
					return compentlist[i];
				}
			}	
		}
	}

	return NULL;
}

Rect SpecialTree::getFirstGoToCompentPos(void) const
{
	Rect pos(0.0f,0.0f,0.0f,0.0f);
	if (d_listItems.size()<=0)
	{
		return pos;
	}

	size_t itemCount = d_listItems.size();
	size_t i=0;
	{
		if (d_listItems[i]->getIsOpen())
		{
			pos.d_top = CompentHeight;
			pos.d_bottom = pos.d_top+CompentHeight;

			Rect renderArea(getTreeRenderArea());
			float x = renderArea.d_left;// - d_horzScrollbar->getScrollPosition();
			pos.d_left += x+ LeftOffect;

			SpecialTreeItem::ChildrenCompentList& compentlist = d_listItems[i]->getChildrenCompentList();
			for(size_t i = 0; i < compentlist.size(); i++)
			{
				Size componentsize=compentlist[i]->getPixelSize();

				if(compentlist[i]->GetType() != RichEditboxComponentType_GoToText)
				{
					pos.d_left += componentsize.d_width;
				}
				else
				{
					pos.d_right = pos.d_left + componentsize.d_width;
					return pos;
				}
				
			}	
		}
	}
	return pos;
}
/*************************************************************************
    Add tree specific events
*************************************************************************/
void SpecialTree::addTreeEvents(void)
{
    addEvent(EventListContentsChanged);
    addEvent(EventVertScrollbarModeChanged);
    addEvent(EventHorzScrollbarModeChanged);
    addEvent(EventBranchOpened);
    addEvent(EventBranchClosed);
	addEvent(EventOpenItemInfo);
	addEvent(EventCloseItemInfo);
}

/*************************************************************************
    Handler called internally when the list contents are changed
*************************************************************************/
void SpecialTree::onListContentsChanged(WindowEventArgs& e)
{
    //configureScrollbars();
    invalidate();
    fireEvent(EventListContentsChanged, e, EventNamespace);
}
/*************************************************************************
    Handler called internally when the forced display of the vertical
    scroll bar setting changes.
*************************************************************************/
void SpecialTree::onVertScrollbarModeChanged(WindowEventArgs& e)
{
    invalidate();
    fireEvent(EventVertScrollbarModeChanged, e, EventNamespace);
}

/*************************************************************************
    Handler called internally when the forced display of the horizontal
    scroll bar setting changes.
*************************************************************************/
void SpecialTree::onHorzScrollbarModeChanged(WindowEventArgs& e)
{
    invalidate();
    fireEvent(EventHorzScrollbarModeChanged, e, EventNamespace);
}

/*************************************************************************
    Handler called internally when the forced display of the horizontal
    scroll bar setting changes.
*************************************************************************/
void SpecialTree::onBranchOpened(SpecialTreeEventArgs& e)
{
    invalidate();
    fireEvent(EventBranchOpened, e, EventNamespace);
}

/*************************************************************************
    Handler called internally when the forced display of the horizontal
    scroll bar setting changes.
*************************************************************************/
void SpecialTree::onBranchClosed(SpecialTreeEventArgs& e)
{
    invalidate();
    fireEvent(EventBranchClosed, e, EventNamespace);
}

/*************************************************************************
Handler called internally when the forced display of the horizontal
scroll bar setting changes.
*************************************************************************/
void SpecialTree::onOpenItemInfo(SpecialTreeEventArgs& e)
{
	invalidate();
	fireEvent(EventOpenItemInfo, e, EventNamespace);
}

/*************************************************************************
Handler called internally when the forced display of the horizontal
scroll bar setting changes.
*************************************************************************/
void SpecialTree::onCloseItemInfo(SpecialTreeEventArgs& e)
{
	invalidate();
	fireEvent(EventCloseItemInfo, e, EventNamespace);
}


/*************************************************************************
    Handler for when we are sized
*************************************************************************/
void SpecialTree::onSized(WindowEventArgs& e)
{
    // base class handling
    Window::onSized(e);
    
    //configureScrollbars();
    
    ++e.handled;
}

void SpecialTree::onMouseEnters(MouseEventArgs& e)
{
	Window::onMouseEnters(e);
}

void SpecialTree::onMouseLeaves(MouseEventArgs& e)
{
	Window::onMouseLeaves(e);
	if(d_LastMouseOnComment)
	{
		d_LastMouseOnComment->onMouseLeave();
		invalidate();
		d_LastMouseOnComment = NULL;
	}
	
}

/*************************************************************************
    Handler for when mouse button is pressed
*************************************************************************/
void SpecialTree::onMouseButtonDown(MouseEventArgs& e)
{
    // base class processing
    // populateGeometryBuffer();
    
    if (e.button == LeftButton)
    {   
        Point localPos(CoordConverter::screenToWindow(*this, e.position));
		int itemindex = -1;
        SpecialTreeItem* item = getItemAtPoint(localPos,&itemindex);
        
        if (item != 0)
        {
            SpecialTreeEventArgs args(this);
            args.treeItem = item;
            Rect buttonLocation = item->getButtonLocation();
            if (((localPos.d_x >= buttonLocation.d_left) && (localPos.d_x <= buttonLocation.d_right) &&
                (localPos.d_y >= buttonLocation.d_top) && (localPos.d_y <= buttonLocation.d_bottom)))// || item->getChildrenCompentsCount()>0)
            {
                item->toggleIsOpen();
                if (item->getIsOpen())
                {
                    onBranchOpened(args);
                }
                else
                {
                    onBranchClosed(args);
                }
                
                // Update the item screen locations, needed to update the scrollbars.
                populateGeometryBuffer();
                
                // Opened or closed a tree branch, so must update scrollbars.
                //configureScrollbars();
				++e.handled;
            }
			buttonLocation = item->getInfoBtnLocation();
			if (((localPos.d_x >= buttonLocation.d_left) && (localPos.d_x <= buttonLocation.d_right) &&
				(localPos.d_y >= buttonLocation.d_top) && (localPos.d_y <= buttonLocation.d_bottom)))// || item->getChildrenCompentsCount()>0)
			{
				if (item->getIsOpenInfo() == false)
				{
					onOpenItemInfo(args);
				}
				else
				{
					onCloseItemInfo(args);
				}
				++e.handled;
			}

        } 
		else if(itemindex >=0)
		{
			RichEditboxComponent* pCompent = getCompentAtPoint(localPos,itemindex);
			if(pCompent)
			{
				pCompent->onMouseButtonDown(e);
				++e.handled;
			}
		}
       // ++e.handled;
    }
	Window::CheckGuideEnd(e.button);
}

/*************************************************************************
    Handler for mouse wheel changes
*************************************************************************/
void SpecialTree::onMouseWheel(MouseEventArgs& e)
{
    // base class processing.
    Window::onMouseWheel(e);
    
    if (d_vertScrollbar->isVisible() && (d_vertScrollbar->getDocumentSize() > d_vertScrollbar->getPageSize()))
        d_vertScrollbar->setScrollPosition(d_vertScrollbar->getScrollPosition() + d_vertScrollbar->getStepSize() * -e.wheelChange);
//     else if (d_horzScrollbar->isVisible() && (d_horzScrollbar->getDocumentSize() > d_horzScrollbar->getPageSize()))
//         d_horzScrollbar->setScrollPosition(d_horzScrollbar->getScrollPosition() + d_horzScrollbar->getStepSize() * -e.wheelChange);
    
    ++e.handled;
}

/*************************************************************************
    Handler for mouse movement
*************************************************************************/
void SpecialTree::onMouseMove(MouseEventArgs& e)
{
    Window::onMouseMove(e);
	Point localPos(CoordConverter::screenToWindow(*this, e.position));
	int itemindex = -1;
	SpecialTreeItem* item = getItemAtPoint(localPos,&itemindex);
	
	//LOG_OutLog_Info(L"鼠标（%u,%u）,（%u,%u）划过任务追踪\n",e.position.d_x,e.position.d_y,localPos.d_x,localPos.d_y);
	
	if(item == NULL && itemindex >=0)
	{
 		RichEditboxComponent* pCompent = getCompentAtPoint(localPos,itemindex);
 		if(pCompent && (pCompent->GetType() == RichEditboxComponentType_GoToText ||  pCompent->GetType() == RichEditboxComponentType_Image)&& d_LastMouseOnComment != pCompent)
 		{
   			if(d_LastMouseOnComment)
   			{
   				d_LastMouseOnComment->onMouseLeave();
   			}
 			pCompent->onMouseEnter();
 			invalidate();
			d_LastMouseOnComment = pCompent;
 		}
 		else if(d_LastMouseOnComment && d_LastMouseOnComment != pCompent)
 		{
 			d_LastMouseOnComment->onMouseLeave();
 			invalidate();
 			d_LastMouseOnComment = NULL;
 		}
	}
	else if(d_LastMouseOnComment)
	{
		d_LastMouseOnComment->onMouseLeave();
		invalidate();
		d_LastMouseOnComment = NULL;
	}
}


/*************************************************************************
    Return whether the vertical scroll bar is always shown.
*************************************************************************/
bool SpecialTree::isVertScrollbarAlwaysShown(void) const
{
    return d_forceVertScroll;
}

/*************************************************************************
    Return whether the horizontal scroll bar is always shown.
*************************************************************************/
bool SpecialTree::isHorzScrollbarAlwaysShown(void) const
{
    return d_forceHorzScroll;
}


/*************************************************************************
    Remove all items from the list.
*************************************************************************/
bool SpecialTree::resetList_impl(void)
{
    // just return false if the list is already empty
    if (getItemCount() == 0)
    {
        return false;
    }
    // we have items to be removed and possible deleted
    else
    {
        // delete any items we are supposed to
        for (size_t i = 0; i < getItemCount(); ++i)
        {           
            // clean up this item.
            delete d_listItems[i];       
        }
        
        // clear out the list.
        d_listItems.clear();
        return true;
    }
}

/*************************************************************************
    Handler for scroll position changes.
*************************************************************************/
bool SpecialTree::handle_scrollChange(const EventArgs&)
{
    // simply trigger a redraw of the Tree.
    invalidate();
    return true;
}

bool SpecialTree::onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer)
{
 //   Window::onMouseSlide(e);
    Scrollbar* vertScrollbar = getVertScrollbar();
    if (vertScrollbar) {
        vertScrollbar->onMouseDrag(pRecognizer);
    }
	return true;
}


//////////////////////////////////////////////////////////////////////////
/*************************************************************************
    Functions used for predicates in std algorithms
*************************************************************************/
//////////////////////////////////////////////////////////////////////////
/*************************************************************************
    used for < comparisons between TreeItem pointers
*************************************************************************/
bool lbi_less(const SpecialTreeItem* a, const SpecialTreeItem* b)
{
    return *a < *b;
}

/*************************************************************************
    used for > comparisons between TreeItem pointers
*************************************************************************/
bool lbi_greater(const SpecialTreeItem* a, const SpecialTreeItem* b)
{
    return *a > *b;
}

} // End of  CEGUI namespace section
