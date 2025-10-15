/***********************************************************************
     filename:  CEGUIGroupBtnTree.cpp
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
//#include "elements/CEGUITreeItem.h"
#include "CEGUIImageset.h"
#include "elements/CEGUIGroupBtnTree.h"
#include "elements/CEGUIScrollbar.h"
#include "elements/CEGUITooltip.h"
#include "WindowRendererSets/Falagard/FalGroupBtnTree.h"

#include <algorithm>

// Start of CEGUI namespace section
namespace CEGUI
{
const String GroupBtnTree::EventNamespace("GroupBtnTree");
const String GroupBtnTree::WidgetTypeName("CEGUI/GroupBtnTree");

/*************************************************************************
    Constants
*************************************************************************/
// event names
const String GroupBtnTree::EventListContentsChanged( "ListItemsChanged" );
const String GroupBtnTree::EventSelectionChanged( "ItemSelectionChanged" );
const String GroupBtnTree::EventBranchOpened( "BranchOpened" );
const String GroupBtnTree::EventBranchClosed( "BranchClosed" );

TreeProperties::MaskImage GroupBtnTree::d_MaskImageProperty;

/*************************************************************************
    Constructor for GroupBtnTree base class.
*************************************************************************/
GroupBtnTree::GroupBtnTree(const String& type, const String& name) :
    Window(type, name),
    d_itemTooltips(false),
    d_vertScrollbar(0),
    d_lastSelected(0),
	d_rightButtonEnbale(false),
	d_MouseOnItem(0),
	d_lastOpenItem(0),
	d_lastSelectedParentItem(0)
{
    // add new events specific to tree.
    addTreeEvents();
	SetDragMoveEnable(false);
    EnableDrag(true);

	for (int i = 0; i < 2; i++)
	{
		d_maskImage[i] = NULL;
	}

	addGroupBtnTreeProperties();
}

GroupBtnTree::GroupBtnTree(const String& type) :
	Window(type)
{
	addTreeEvents();
	SetDragMoveEnable(false);
	EnableDrag(true);
}
const GroupBtnTree& GroupBtnTree::operator=(const GroupBtnTree& t)
{
	Window::operator=(t);
	d_itemTooltips = t.d_itemTooltips;
	d_rightButtonEnbale = t.d_rightButtonEnbale;
	d_vertScrollbar = t.d_vertScrollbar;
	assert(t.d_listItems.size() == 0);
	d_listItems = t.d_listItems;
	assert(t.d_lastSelected == NULL);
	d_lastSelected = NULL;
	assert(t.d_lastOpenItem == NULL);
	d_lastOpenItem = NULL;
	assert(t.d_lastSelectedParentItem == NULL);
	d_lastSelectedParentItem = t.d_lastSelectedParentItem;
	assert(t.d_MouseOnItem == NULL);
	d_MouseOnItem = NULL;
	for (int i = 0; i < 2; i++)
	{
		d_maskImage[i] = t.d_maskImage[i];
	}
	d_itemArea = d_itemArea;
	return *this;
}
Window* GroupBtnTree::clone(Window* wnd)
{
	GroupBtnTree* retWnd = (GroupBtnTree*)wnd;
	if (retWnd == NULL)
		retWnd = new GroupBtnTree(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == GroupBtnTree::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

void GroupBtnTree::setLookNFeel(const String& look, bool bClone)
{
    Window::setLookNFeel( look, bClone );
    initialise();
}

bool GroupBtnTree::onSetTemplateLookNFeel()
{
	if (false == Window::onSetTemplateLookNFeel())
		return false;

	return true;
}

/*************************************************************************
    Destructor for GroupBtnTree base class.
*************************************************************************/
GroupBtnTree::~GroupBtnTree(void)
{
    resetList_impl();
}

/*************************************************************************
    Initialise the Window based object ready for use.
*************************************************************************/
void GroupBtnTree::initialise(void)
{
    // get WidgetLookFeel for the assigned look.
    const WidgetLookFeel &wlf = WidgetLookManager::getSingleton().getWidgetLook(d_lookName);
   
    // create the component sub-widgets
    d_vertScrollbar = createVertScrollbar(getName() + "__auto_vscrollbar__");
 
	d_vertScrollbar->hide();
    
    addChildWindow(d_vertScrollbar);
     
    d_vertScrollbar->subscribeEvent(Scrollbar::EventScrollPositionChanged, Event::Subscriber(&GroupBtnTree::handle_scrollChange, this));
    
    configureScrollbars();
    performChildWindowLayout();
}


/*************************************************************************
    Return a pointer to the first selected item.
*************************************************************************/
GroupBtnItem* GroupBtnTree::getSelectedItem(void) const
{
	if(d_lastSelected)
	{
		return d_lastSelected;
	}
    return 0;
}

// Recursive!
GroupBtnItem* GroupBtnTree::findItemWithIDFromList(const LBItemList &itemList, uint searchID, const GroupBtnItem* startItem, bool foundStartItem)
{
    size_t itemCount = itemList.size();
    
    for (size_t index = 0; index < itemCount; ++index)
    {
        if (foundStartItem == true)
        {
            // Already found the startItem, now looking for the actual text.
            if (itemList[index]->getID() == searchID)
                return itemList[index];
        }
        else
        {
            // Still looking for startItem.  Is this it?
            if (itemList[index] == startItem)
                foundStartItem = true;
        }
        
        if (itemList[index]->getItemCount() > 0)
        {
            // Search the current item's itemList regardless if it's open or not.
            GroupBtnItem *foundSelectedTree;
            foundSelectedTree = findItemWithIDFromList(itemList[index]->getItemList(), searchID, startItem, foundStartItem);
            if (foundSelectedTree != 0)
                return foundSelectedTree;
        }
    }
    
    return 0;
}

GroupBtnItem* GroupBtnTree::findNextItemWithID(uint searchID, const GroupBtnItem* start_item)
{
    if (start_item == 0)
        return findItemWithIDFromList(d_listItems, searchID, 0, true);
    else
        return findItemWithIDFromList(d_listItems, searchID, start_item, false);
}

GroupBtnItem* GroupBtnTree::findFirstItemWithID(uint searchID)
{
    return findItemWithIDFromList(d_listItems, searchID, 0, true);
}

/*************************************************************************
    Return whether the specified TreeItem is in the List
*************************************************************************/
bool GroupBtnTree::isTreeItemInList(const GroupBtnItem* item) const
{
    return std::find(d_listItems.begin(), d_listItems.end(), item) != d_listItems.end();
}

/*************************************************************************
    Remove all items from the list.
*************************************************************************/
void GroupBtnTree::resetList(void)
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
void GroupBtnTree::addItem(GroupBtnItem* item)
{
    if (item != 0)
    {
        // establish ownership
        item->setOwnerWindow(this);
        d_listItems.push_back(item);
          
        WindowEventArgs args(this);
        onListContentsChanged(args);
    }
}
    
GroupBtnItem* GroupBtnTree::addItem(const String& text, uint item_id,colour boardcol, void* item_data) {
    GroupBtnItem* newItem = new GroupBtnItem(text, item_id, boardcol, item_data);
    addItem(newItem);
    return newItem;
}

/*************************************************************************
    Insert an item into the tree after a specified item already in
    the list.
*************************************************************************/
void GroupBtnTree::insertItem(GroupBtnItem* item, const GroupBtnItem* position)
{
    
    // establish ownership
    item->setOwnerWindow(this);
    
    // if position is NULL begin insert at begining, else insert after item 'position'
    LBItemList::iterator ins_pos;
    
    if (position == 0)
    {
        ins_pos = d_listItems.begin();
    }
    else
    {
        ins_pos = std::find(d_listItems.begin(), d_listItems.end(), position);
        
        // throw if item 'position' is not in the list
        if (ins_pos == d_listItems.end())
        {
            throw InvalidRequestException((utf8*)"GroupBtnTree::insertItem - the specified TreeItem for parameter 'position' is not attached to this GroupBtnTree.");
        }
    }
    
    d_listItems.insert(ins_pos, item);
    
    WindowEventArgs args(this);
    onListContentsChanged(args);
    
}

/*************************************************************************
    Removes the given item from the tree.
*************************************************************************/
void GroupBtnTree::removeItem(const GroupBtnItem* item)
{
    if (item != 0)
    {
        LBItemList::iterator pos = std::find(d_listItems.begin(), d_listItems.end(), item);
        
        // if item is in the list
        if (pos != d_listItems.end())
        {
            // disown item
            (*pos)->setOwnerWindow(0);
            
            // remove item
            d_listItems.erase(pos);
            
            // if item was the last selected item, reset that to NULL
            if (item == d_lastSelected)
            {
                d_lastSelected = 0;
            }

			// if item was the last open item, reset lastOpenItem and lastSelected. by liugeng
            if (item == d_lastOpenItem)
            {
				d_lastOpenItem = 0;
				d_lastSelected = 0;
            }
           
			// clean up this item.
			delete item;
            
            
            WindowEventArgs args(this);
            onListContentsChanged(args);
        }
    }
}

/*************************************************************************
    Clear the selected state for all items.
*************************************************************************/
void GroupBtnTree::clearAllSelections(void)
{
   
}

bool GroupBtnTree::clearAllSelectionsFromList(const LBItemList &itemList)
{
	return false;
}

void GroupBtnTree::setShowVertScrollbar(bool setting)
{

}

void GroupBtnTree::setItemTooltipsEnabled(bool setting)
{
    d_itemTooltips = setting;
}

/*************************************************************************
    Set the select state of an attached TreeItem.
*************************************************************************/
void GroupBtnTree::setItemSelectState(GroupBtnItem* item, bool state)
{
    if (containsOpenItemRecursive(d_listItems, item))
    {	
		if(d_lastSelected != item)
		{
			if(d_lastSelected)
			{
				d_lastSelected->setSelected(false);
				if(d_lastSelected->getParentItem() && d_lastSelected->getParentItem() != item->getParentItem())
				{
					d_lastSelected->getParentItem()->setSelected(false);
				}
			}

			item->setSelected(true);
			d_lastSelected = item;

			GroupBtnTreeEventArgs args(this);
			args.treeItem = item;

			onSelectionChanged(args);
			populateGeometryBuffer();
		}
    }
    else
    {
        throw InvalidRequestException("GroupBtnTree::setItemSelectState - the "
            "specified TreeItem is not attached to this GroupBtnTree or not visible.");
    }
}

//----------------------------------------------------------------------------//
void GroupBtnTree::addGroupBtnTreeProperties()
{
	addProperty(&d_MaskImageProperty);
}

//----------------------------------------------------------------------------//
bool GroupBtnTree::containsOpenItemRecursive(const LBItemList& itemList, GroupBtnItem* item)
{
    size_t itemCount = itemList.size();
    for (size_t index = 0; index < itemCount; ++index)
    {
        if (itemList[index] == item)
            return true;

        if (itemList[index]->getItemCount() > 0)
        {
            if (itemList[index]->getIsOpen())
            {
                if (containsOpenItemRecursive(itemList[index]->getItemList(), item))
                    return true;
            }
        }
    }

    return false;
}

/*************************************************************************
    Causes the tree to update it's internal state after changes have
    been made to one or more attached TreeItem objects.
*************************************************************************/
void GroupBtnTree::handleUpdatedItemData(void)
{
    configureScrollbars();
    invalidate();
}

/*************************************************************************
    Perform the actual rendering for this Window.
*************************************************************************/
void GroupBtnTree::populateGeometryBuffer()
{
    // get the derived class to render general stuff before we handle the items
    cacheTreeBaseImagery();
    
    // Render list items
    Vector2  itemPos;  
    // calculate position of area we have to render into
    //Rect itemsArea(getTreeRenderArea());
    //Rect itemsArea(0,0,500,500);
    
    // set up some initial positional details for items
    itemPos.d_x = d_itemArea.d_left;
    itemPos.d_y = d_itemArea.d_top - d_vertScrollbar->getScrollPosition();
    
    drawItemList(d_listItems, d_itemArea, d_itemArea.getWidth(), itemPos, d_geometry, getEffectiveAlpha());
}

// Recursive!
void GroupBtnTree::drawItemList(LBItemList& itemList, Rect& itemsArea, float widest,
				Vector2& itemPos, GeometryBuffer* geometry, float alpha, bool bFirst)
{
    if (itemList.empty())
        return;

    itemPos.d_y += 1;
    // loop through the items
    Size     itemSize;
    Rect     itemClipper, itemRect;
    size_t   itemCount = itemList.size();
    bool     itemIsVisible;
	
    for (size_t i = 0; i < itemCount; ++i)
    {
		GroupBtnItem* groupBtnItem = itemList[i];
        itemSize.d_height = itemList[i]->getItemSize().d_height + 2;
        
        // allow item to have full width of box if this is wider than items
        itemSize.d_width = widest;
        
        // calculate destination area for this item.
        itemRect.d_left = itemPos.d_x;
        itemRect.d_top  = itemPos.d_y;
        itemRect.setSize(itemSize);
        itemClipper = itemRect.getIntersection(itemsArea);
      
        if (itemClipper.getHeight() > 0)
        {
            itemIsVisible = true;
            itemList[i]->draw(geometry, itemRect, alpha, &itemClipper);
        }
        else
        {
            itemIsVisible = false;
        }
        
        // Process this item's list if it has items in it.
        if (itemList[i]->getItemCount() > 0)
        {    
            if (itemList[i]->getIsOpen())
            {
                // update position ready for next item
                itemPos.d_y += itemSize.d_height;
                
				itemPos.d_x += 2;
				
                drawItemList(itemList[i]->getItemList(), itemsArea, widest, itemPos, geometry, alpha, false);
				itemPos.d_x -= 2;
				
            }
            else
            {
                // update position ready for next item
                itemPos.d_y += itemSize.d_height;
            }
        }
        else
        {
            // update position ready for next item
            itemPos.d_y += itemSize.d_height;
        }

		if (bFirst)
		{
			Rect realItemRect(itemRect);
			Size itemImageSize = itemList[i]->getItemSize();
			realItemRect.d_left += 0.5f*(itemRect.getWidth() - itemImageSize.d_width);
			realItemRect.setWidth(itemImageSize.d_width);
			realItemRect.setHeight(itemImageSize.d_height);

			//渲染遮罩图
			if (!itemList[i]->getIsOpen())//收缩状态
			{
				const Image* pImage = d_maskImage[0];
				if (pImage != NULL)
				{
					Size imageSize = pImage->getSize();
					Rect maskRect(realItemRect);
					maskRect.d_left = maskRect.d_right - imageSize.d_width - 20;
					maskRect.d_top = 0.5f*(maskRect.d_top + maskRect.d_bottom - imageSize.d_height);

					maskRect.setSize(imageSize);
					pImage->draw(getGeometryBuffer(), maskRect.getPosition(), maskRect.getSize(), &itemClipper);
				}
			}
			else//展开状态
			{
				const Image* pImage = d_maskImage[1];
				if (pImage)
				{
					Size imageSize = pImage->getSize();
					Rect maskRect(realItemRect);
					maskRect.d_left = maskRect.d_right - imageSize.d_width - 20;
					maskRect.d_top = 0.5f*(maskRect.d_top + maskRect.d_bottom - imageSize.d_height);

					maskRect.setSize(imageSize);
					pImage->draw(getGeometryBuffer(), maskRect.getPosition(), maskRect.getSize(), &itemClipper);
				}
			}
		}

    }//itemCount
    
    // Successfully drew all items, so vertical scrollbar not needed.
    //   setShowVertScrollbar(false);
}

#define HORIZONTAL_STEP_SIZE_DIVISOR   20.0f

/*************************************************************************
    display required integrated scroll bars according to current state
    of the tree and update their values.
*************************************************************************/
void GroupBtnTree::configureScrollbars(bool checkpos)
{
    Rect  renderArea(getTreeRenderArea());
	if (d_windowRenderer)
	{
		FalagardGroupBtnTree* TreeRender=(FalagardGroupBtnTree*)d_windowRenderer;
		if (TreeRender)
		{
			renderArea=TreeRender->getTreeRenderArea();
		}
		
	}
	
	if(renderArea.getWidth() < 1.0f || renderArea.getHeight()<1.0f)
		return;
    
    //This is becuase CEGUI IS GAY! and fires events before the item is initialized
    if(!d_vertScrollbar)
	{
		d_vertScrollbar = createVertScrollbar(getName() + "__auto_vscrollbar__");
		d_vertScrollbar->hide();
	}
    
    float totalHeight = getTotalItemsHeight();    
    //
    // First show or hide the scroll bars as needed (or requested)
    //
    // show or hide vertical scroll bar as required (or as specified by option)
	bool bOldVertBarVis=d_vertScrollbar->isVisible(true);
//    if ((totalHeight > renderArea.getHeight()))
//    {
//        d_vertScrollbar->show();
//       // renderArea.d_right -= d_vertScrollbar->getWidth().d_offset + d_vertScrollbar->getXPosition().d_offset;
//        //      renderArea.d_right -= d_vertScrollbar->getAbsoluteWidth() + d_vertScrollbar->getAbsoluteXPosition();
//        // show or hide horizontal scroll bar as required (or as specified by option)
//    }
//    else
//    {
//        d_vertScrollbar->hide();
//        d_vertScrollbar->setScrollPosition(0);     
//    }

	bool bCurVertBarVis=d_vertScrollbar->isVisible(true);

	if (bOldVertBarVis!=bCurVertBarVis)
	{
		configureScrollbars();
	}
    //
    // Set up scroll bar values
    //
    
    float itemHeight;
    if (d_listItems.size() > 0)
        itemHeight = d_listItems[0]->getItemSize().d_height + 2;
    else
        itemHeight = 10;

	totalHeight = getTotalItemsHeight();
    
    d_vertScrollbar->setDocumentSize(totalHeight);
    d_vertScrollbar->setPageSize(renderArea.getHeight());
    d_vertScrollbar->setStepSize(ceguimax(1.0f, renderArea.getHeight() / itemHeight));
    d_vertScrollbar->setScrollPosition(d_vertScrollbar->getScrollPosition(), checkpos);
}

/*************************************************************************
    Return the sum of all item heights
*************************************************************************/
float GroupBtnTree::getTotalItemsHeight(void) const
{
    float heightSum = 0;
    
    getTotalItemsInListHeight(d_listItems, &heightSum);
    return heightSum;
}

// Recursive!
void GroupBtnTree::getTotalItemsInListHeight(const LBItemList &itemList, float *heightSum) const
{
    size_t itemCount = itemList.size();
    for (size_t index = 0; index < itemCount; ++index)
    {
        *heightSum += itemList[index]->getItemSize().d_height +2;
        if (itemList[index]->getIsOpen() && (itemList[index]->getItemCount() > 0))
            getTotalItemsInListHeight(itemList[index]->getItemList(), heightSum);
    }
}

/*************************************************************************
    Return the TreeItem under the given window local pixel co-ordinate.
*************************************************************************/
GroupBtnItem* GroupBtnTree::getItemAtPoint(const Point& pt) const
{
    Rect renderArea(getTreeRenderArea());
    
    // point must be within the rendering area of the GroupBtnTree.
    if (renderArea.isPointInRect(pt))
    {
        float y = renderArea.d_top - d_vertScrollbar->getScrollPosition();
        y+=1;
        // test if point is above first item
        if (pt.d_y >= y)
            return getItemFromListAtPoint(d_listItems, &y, pt);
    }
    
    return 0;
}

// Recursive!
GroupBtnItem* GroupBtnTree::getItemFromListAtPoint(const LBItemList &itemList, float *bottomY, const Point& pt) const
{
    size_t itemCount = itemList.size();
    
    for (size_t i = 0; i < itemCount; ++i)
    {
        *bottomY += itemList[i]->getItemSize().d_height + 2;
        if (pt.d_y < *bottomY)
            return itemList[i];
        
        if (itemList[i]->getItemCount() > 0)
        {
            if (itemList[i]->getIsOpen())
            {
                GroupBtnItem *foundPointedAtTree;
                foundPointedAtTree = getItemFromListAtPoint(itemList[i]->getItemList(), bottomY, pt);
                if (foundPointedAtTree != 0)
                    return foundPointedAtTree;
            }
        }
    }
    
    return 0;
}

/*************************************************************************
    Add tree specific events
*************************************************************************/
void GroupBtnTree::addTreeEvents(void)
{
    addEvent(EventListContentsChanged);
    addEvent(EventSelectionChanged);
    addEvent(EventBranchOpened);
    addEvent(EventBranchClosed);
}

/*************************************************************************
    Handler called internally when the list contents are changed
*************************************************************************/
void GroupBtnTree::onListContentsChanged(WindowEventArgs& e)
{
    configureScrollbars();
    invalidate();
    fireEvent(EventListContentsChanged, e, EventNamespace);
}

/*************************************************************************
    Handler called internally when the currently selected item or items
    changes.
*************************************************************************/
void GroupBtnTree::onSelectionChanged(GroupBtnTreeEventArgs& e)
{
    invalidate();
    fireEvent(EventSelectionChanged, e, EventNamespace);
}


/*************************************************************************
    Handler called internally when the forced display of the horizontal
    scroll bar setting changes.
*************************************************************************/
void GroupBtnTree::onBranchOpened(GroupBtnTreeEventArgs& e)
{
    invalidate();
    fireEvent(EventBranchOpened, e, EventNamespace);
}

/*************************************************************************
    Handler called internally when the forced display of the horizontal
    scroll bar setting changes.
*************************************************************************/
void GroupBtnTree::onBranchClosed(GroupBtnTreeEventArgs& e)
{
    invalidate();
    fireEvent(EventBranchClosed, e, EventNamespace);
}

/*************************************************************************
    Handler for when we are sized
*************************************************************************/
void GroupBtnTree::onSized(WindowEventArgs& e)
{
    // base class handling
    Window::onSized(e);
    
    configureScrollbars();
    
    ++e.handled;
}

/*************************************************************************
    Handler for when mouse button is pressed
*************************************************************************/
void GroupBtnTree::onMouseClicked(MouseEventArgs& e)
{
    // base class processing
    // populateGeometryBuffer();
	Window::onMouseClicked(e);
    
    if (e.button == LeftButton || d_rightButtonEnbale)
    {
        bool modified = false;
        
        Point localPos(CoordConverter::screenToWindow(*this, e.position));
        //      Point localPos(screenToWindow(e.position));
        
        GroupBtnItem* item = getItemAtPoint(localPos);
        
        if (item != 0 && !item->isDisabled())
        {
            modified = true;
            GroupBtnTreeEventArgs args(this);
            args.treeItem = item;

			if (isSoundEable())
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
          
			//点击的是大节点，有展开、收缩两种状态,
            if (item->getItemCount()>0)//
            {
                item->toggleIsOpen();
				//节点被展开，展开时有一些处理，要默认选中第一个子节点
                if (item->getIsOpen())
                {
					OnItemOpend(item);
                    onBranchOpened(args);
                }
                else //节点被收起，如果有子节点被选中，本节点也要选中
                {
					if(d_lastSelected && d_lastSelected->getParentItem() == item)
					{
						item->setSelected(true);
						d_lastSelectedParentItem = item;
					}
                    onBranchClosed(args);
					d_lastOpenItem = 0;//清空保存的展开节点
                }
                
                // Update the item screen locations, needed to update the scrollbars.
                //	populateGeometryBuffer();
                
                // Opened or closed a tree branch, so must update scrollbars.
                configureScrollbars();
				//populateGeometryBuffer();
            }
			//点击的是子节点，子节点被选中，
            else if(d_lastSelected != item)
            {
				if(item->getParentItem() == NULL && d_lastOpenItem)//是父节点，但是没有子节点
				{
					d_lastOpenItem->toggleIsOpen();
					d_lastOpenItem = NULL;
				}

				if(d_lastSelected)
				{
					d_lastSelected->setSelected(false);
				}
				
				if(d_lastSelectedParentItem)
				{
					d_lastSelectedParentItem->setSelected(false);
					d_lastSelectedParentItem = 0;
				}
                item->setSelected(true);
                d_lastSelected = item;
                onSelectionChanged(args);
				populateGeometryBuffer();
            }
        }
        ++e.handled;
    }
}

void GroupBtnTree::onMouseButtonDown(MouseEventArgs& e)
{
	Window::onMouseButtonDown(e);
	if (e.button == LeftButton || d_rightButtonEnbale)
	{
		d_vertScrollbar->Stop(); //stop scroll. by liugeng
		Point localPos(CoordConverter::screenToWindow(*this, e.position));
		GroupBtnItem* item = getItemAtPoint(localPos);
		if (item != 0 && !item->isDisabled())
		{
			d_MouseOnItem = item;
			d_MouseOnItem->SetMouseOnState(true);
		}
		++e.handled;
	}
}

void GroupBtnTree::onMouseButtonUp(MouseEventArgs& e)
{
	Window::onMouseButtonUp(e);

	if (d_MouseOnItem)
	{
		d_MouseOnItem->SetMouseOnState(false);
		d_MouseOnItem = NULL;
		invalidate();
	}

	// event was handled by us.
	++e.handled;
}

void GroupBtnTree::OnItemOpend(CEGUI::GroupBtnItem *pOpenItem)
{
	if(d_lastOpenItem)
	{
		d_lastOpenItem->toggleIsOpen();
	}		
	d_lastOpenItem = pOpenItem;//将本次展开的节点保存起来

	//本节点没有选中的子节点时，第一个节点置为选中
	if(d_lastSelectedParentItem != pOpenItem)
	{
		GroupBtnItem *firstItemInList = pOpenItem->getTreeItemFromIndex(0);
		if(d_lastSelected != firstItemInList)
		{
			if(d_lastSelected)
			{
				d_lastSelected->setSelected(false);
				if(d_lastSelectedParentItem)
				{
					d_lastSelectedParentItem->setSelected(false);
					d_lastSelectedParentItem = 0;
				}
			}

			firstItemInList->setSelected(true);
			d_lastSelected = firstItemInList;
			GroupBtnTreeEventArgs args(this);
			args.treeItem = firstItemInList;
			onSelectionChanged(args);
		}
	}
	

	GroupBtnItem *lastItemInList = pOpenItem->getTreeItemFromIndex(pOpenItem->getItemCount() - 1);
	ensureItemIsVisible(lastItemInList);
	ensureItemIsVisible(pOpenItem);
}

void GroupBtnTree::SetLastOpenItem(CEGUI::GroupBtnItem *pOpenItem,size_t selectindex)
{
	if(d_lastOpenItem && d_lastOpenItem->getIsOpen())
	{
		d_lastOpenItem->toggleIsOpen();
	}
	if (!pOpenItem)
	{
		return;
	}
    if (!pOpenItem->getIsOpen())
        pOpenItem->toggleIsOpen();
	d_lastOpenItem = pOpenItem;//将本次展开的节点保存起来
	//本节点没有选中的子节点时，第一个节点置为选中
	if (d_lastSelected && d_lastSelected->getParentItem() == pOpenItem)
	{
		return;
	}
	if(d_lastSelectedParentItem != pOpenItem && pOpenItem->getItemList().size() > 0)
	{
		GroupBtnItem *firstItemInList = pOpenItem->getTreeItemFromIndex(selectindex);
		if(d_lastSelected != firstItemInList)
		{
			if(d_lastSelected)
			{
				d_lastSelected->setSelected(false);
				if(d_lastSelectedParentItem)
				{
					d_lastSelectedParentItem->setSelected(false);
					d_lastSelectedParentItem = 0;
				}
			}

			firstItemInList->setSelected(true);
			d_lastSelected = firstItemInList;
			GroupBtnTreeEventArgs args(this);
			args.treeItem = firstItemInList;
			onSelectionChanged(args);
		}
	}
}

void GroupBtnTree::SetLastSelectItem(GroupBtnItem* pSelectItem)
{
	if(d_lastSelected)
	{
		d_lastSelected->setSelected(false);
	}
	d_lastSelected = pSelectItem;
	d_lastSelected->setSelected(true);
}
/*************************************************************************
    Handler for mouse wheel changes
*************************************************************************/
void GroupBtnTree::onMouseWheel(MouseEventArgs& e)
{
    // base class processing.
    Window::onMouseWheel(e);
    
    if (d_vertScrollbar->isVisible() && (d_vertScrollbar->getDocumentSize() > d_vertScrollbar->getPageSize()))
        d_vertScrollbar->setScrollPosition(d_vertScrollbar->getScrollPosition() + d_vertScrollbar->getStepSize() * -e.wheelChange);
    
    ++e.handled;
}

/*************************************************************************
    Handler for mouse movement
*************************************************************************/
void GroupBtnTree::onMouseMove(MouseEventArgs& e)
{
	//Point posi(CoordConverter::screenToWindow(*this, e.position));
	////      Point posi = relativeToAbsolute(CoordConverter::screenToWindow(*this, e.position));
	//GroupBtnItem* item = getItemAtPoint(posi);
	//if (item!=d_MouseOnItem)
	//{
	//	if (d_MouseOnItem)
	//	{
	//		d_MouseOnItem->SetMouseOnState(false);
	//	}
	//	d_MouseOnItem=item;
	//	if (d_MouseOnItem)
	//	{
	//		d_MouseOnItem->SetMouseOnState(true);
	//	}
	//	 invalidate();
	//}
	//if (d_itemTooltips)
 //   {
 //       static GroupBtnItem* lastItem = 0;
 //       
 //      
 //       if (item != lastItem)
 //       {
 //           if (item != 0)
 //           {
 //               setTooltipText(item->getTooltipText());
 //           }
 //           else
 //           {
 //               setTooltipText("");
 //           }
 //           lastItem = item;
 //       }

	//	
	//	
 //       // must check the result from getTooltip(), as the tooltip object could
 //       // be 0 at any time for various reasons.
 //       Tooltip* tooltip = getTooltip();
 //       
 //       if (tooltip)
 //       {
 //           if (tooltip->getTargetWindow() != this)
 //               tooltip->setTargetWindow(this);
 //           else
 //               tooltip->positionSelf();
 //       }
 //   }
    
    Window::onMouseMove(e);
}

void GroupBtnTree::onMouseLeaves(MouseEventArgs& e)
{
    Window::onMouseLeaves(e);
    if (d_MouseOnItem)
	{
		d_MouseOnItem->SetMouseOnState(false);
		d_MouseOnItem=NULL;
		 invalidate();
	}
	
}

// Recursive!
bool GroupBtnTree::getHeightToItemInList(const LBItemList &itemList, const GroupBtnItem *treeItem, int itemDepth, float *height) const
{
    size_t itemCount = itemList.size();
    for (size_t index = 0; index < itemCount; ++index)
    {
        if (treeItem == itemList[index])
            return true;
        
        *height += itemList[index]->getItemSize().d_height + 2;
        
        if (itemList[index]->getIsOpen() && (itemList[index]->getItemCount() > 0))
        {
            if (getHeightToItemInList(itemList[index]->getItemList(), treeItem, itemDepth + 1, height))
                return true;
        }
    }
    
    return false;
}

/*************************************************************************
    Ensure the specified item is visible within the tree.
*************************************************************************/
void GroupBtnTree::ensureItemIsVisible(const GroupBtnItem *treeItem)
{
    // TODO: finish this (make it work!)
    
    if (!treeItem)
        return;
    
    float bottom;
    float top = 0;
    
    if (!getHeightToItemInList(d_listItems, treeItem, 0, &top))
        return;  // treeItem wasn't found by getHeightToItemInList
    
    // calculate height to bottom of item
    bottom = top + treeItem->getItemSize().d_height;
    
    // account for current scrollbar value
    float currPos = d_vertScrollbar->getScrollPosition();
    top      -= currPos;
    bottom   -= currPos;
    
    
#if 0
    // handle simple "scroll to the bottom" case
    if (item_index >= getItemCount())
    {
        d_vertScrollbar->setScrollPosition(d_vertScrollbar->getDocumentSize() - d_vertScrollbar->getPageSize());
    }
    else
    {
        float bottom;
        float listHeight = getTreeRenderArea().getHeight();
        float top = 0;
        
        // get height to top of item
        size_t i;
        for (i = 0; i < item_index; ++i)
        {
            top += d_listItems[i]->getItemSize().d_height;
        }
        
        // calculate height to bottom of item
        bottom = top + d_listItems[i]->getItemSize().d_height;
        
        // account for current scrollbar value
        float currPos = d_vertScrollbar->getScrollPosition();
        top      -= currPos;
        bottom   -= currPos;
        
        // if top is above the view area, or if item is too big to fit
        if ((top < 0.0f) || ((bottom - top) > listHeight))
        {
            // scroll top of item to top of box.
            d_vertScrollbar->setScrollPosition(currPos + top);
        }
        // if bottom is below the view area
        else if (bottom >= listHeight)
        {
            // position bottom of item at the bottom of the list
            d_vertScrollbar->setScrollPosition(currPos + bottom - listHeight);
        }
        
        // Item is already fully visible - nothing more to do.
    }
#endif
}

float GroupBtnTree::getHeightToItem(const GroupBtnItem* item)
{
	float top = 0;
	getHeightToItemInList(d_listItems, item, 0, &top);
	return top;
}

/*************************************************************************
    Remove all items from the list.
*************************************************************************/
bool GroupBtnTree::resetList_impl(void)
{
	for (int i = 0; i < 2; i++)
	{
		d_maskImage[i] = NULL;
	}

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
        d_lastSelected = 0;
		d_lastOpenItem = 0;
		d_lastSelectedParentItem = 0;
		d_MouseOnItem=0;
        return true;
    }
}

/*************************************************************************
    Handler for scroll position changes.
*************************************************************************/
bool GroupBtnTree::handle_scrollChange(const EventArgs&)
{
    // simply trigger a redraw of the GroupBtnTree.
    configureScrollbars(false);
    invalidate();
    return true;
}

void GroupBtnTree::SetRightButtonEnbale(bool bEnbale)
{
	d_rightButtonEnbale = bEnbale;
}

void GroupBtnTree::setMaskImage(int index, const Image* image)
{
	if (index >= 0 && index < 2)
	{
		if (d_maskImage[index] != image)
		{
			d_maskImage[index] = image;
			invalidate(false);
		}
	}
}

void GroupBtnTree::SetMaskImage(int index, const String& imagesetName, const String& imageName)
{
	const CEGUI::Image* pImage = ImagesetManager::getSingleton().getImage(imagesetName, imageName);
	if (pImage)
	{
		setMaskImage(index, pImage);
	}
}

const Image* GroupBtnTree::GetMaskImage(int index) const
{
	if (index >= 0 && index < 2)
		return d_maskImage[index];
	return NULL;
}

void GroupBtnTree::ClearMaskImage(int index)
{
	if (index >= 0 && index < 2)
	{
		d_maskImage[index] = NULL;
	}
}

void GroupBtnTree::ClearAllMaskImage()
{
	for (int i = 0; i < 2; i++)
	{
		d_maskImage[i] = NULL;
	}
}

bool GroupBtnTree::onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer)
{
    Window::onMouseDrag(pRecognizer);
    Scrollbar* vertScrollbar = getVertScrollbar();
    if (vertScrollbar) {
        vertScrollbar->onMouseDrag(pRecognizer);
    }
    return true;
}


} // End of  CEGUI namespace section
