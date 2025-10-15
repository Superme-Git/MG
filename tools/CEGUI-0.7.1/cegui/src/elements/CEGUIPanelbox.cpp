/***********************************************************************
	filename: 	CEGUIPanelbox.cpp
	purpose:	Implementation of Panelbox widget base class
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
#include "elements/CEGUIPanelbox.h"
#include "elements/CEGUIPanelChengJiuItem.h"
#include "elements/CEGUIPanelQiYuanItem.h"
#include "elements/CEGUIScrollbar.h"
#include "elements/CEGUIProgressBar.h"
#include "elements/CEGUIPushButton.h"
#include "elements/CEGUICombobox.h"
#include "CEGUICoordConverter.h"
#include <algorithm>

// Start of CEGUI namespace section
namespace CEGUI
{
const String Panelbox::EventNamespace("Panelbox");
const String Panelbox::WidgetTypeName("CEGUI/Panelbox");


/*************************************************************************
	Constants
*************************************************************************/
// event names
const String Panelbox::EventListContentsChanged( "ListItemsChanged" );
const String Panelbox::EventSelectionChanged( "ItemSelectionChanged" );
const String Panelbox::EventShiftClickItem( "ItemShiftClicked" );

/*************************************************************************
	Constructor for Panelbox base class.
*************************************************************************/
Panelbox::Panelbox(const String& type, const String& name)
	: Window(type, name),
	d_PanelType(CHENG_JIU),
	d_forceVertScroll(true),
	d_lastOpenItem(0),
	d_MouseOnItem(0),
	d_vertScrollbar(0),
	d_progressBar(0),
	d_BackGroundEnable(false)
{
	SetDragMoveEnable(false);
}


Panelbox::Panelbox(const String& type) :
	Window(type)
{
}
const Panelbox& Panelbox::operator=(const Panelbox& t)
{
	Window::operator=(t);
	d_PanelType = t.d_PanelType;
	assert(t.d_listItems.size() == 0);
	d_listItems = t.d_listItems;
	d_lastOpenItem = t.d_lastOpenItem;
	d_MouseOnItem = t.d_MouseOnItem;
	d_BackGroundEnable = t.d_BackGroundEnable;
	d_itemArea = t.d_itemArea;
	d_vertScrollbar = t.d_vertScrollbar;
	d_forceVertScroll = t.d_forceVertScroll;
	d_progressBar = t.d_progressBar;
	d_comboBox = t.d_comboBox;
	d_pushButton = t.d_pushButton;
	return *this;
}
Window* Panelbox::clone(Window* wnd)
{
	Panelbox* retWnd = (Panelbox*)wnd;
	if (retWnd == NULL)
		retWnd = new Panelbox(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == Panelbox::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

/*************************************************************************
	Destructor for Listbox base class.
*************************************************************************/
Panelbox::~Panelbox(void)
{
	resetList_impl();
}


/*************************************************************************
	Initialise the Window based object ready for use.
*************************************************************************/
void Panelbox::initialiseComponents(bool bClone)
{
	if (!bClone)
	{
		// get the component sub-widgets
		d_vertScrollbar = (Scrollbar*)(WindowManager::getSingleton().getWindow(getName() + "__auto_vscrollbar__"));
		d_progressBar = (ProgressBar*)(WindowManager::getSingleton().createWindow("TaharezLook/ProgressBar"));
		d_comboBox = (Combobox*)(WindowManager::getSingleton().createWindow("TaharezLook/Combobox"));
		d_pushButton = (PushButton*)(WindowManager::getSingleton().createWindow("TaharezLook/Button"));

		addChildWindow(d_vertScrollbar);
		addChildWindow(d_progressBar);
		addChildWindow(d_comboBox);
		addChildWindow(d_pushButton);
	}
	d_vertScrollbar->subscribeEvent(Scrollbar::EventScrollPositionChanged, Event::Subscriber(&Panelbox::handle_scrollChange, this));
	if (!bClone)
	{
		d_progressBar->SetFrameEnable(true);
		d_progressBar->setSize(UVector2(UDim(0, 140), UDim(0, 15)));
		d_progressBar->setVisible(false);

		d_comboBox->setSize(UVector2(UDim(0, 100), UDim(0, 100)));
		d_comboBox->setVisible(false);
		d_comboBox->setReadOnly(true);

		d_pushButton->setSize(UVector2(UDim(0, 60), UDim(0, 22)));
		d_pushButton->setVisible(false);

		configureScrollbars();
		performChildWindowLayout();
	}
}


/*************************************************************************
	Return a pointer to the first selected item.
*************************************************************************/
PanelItem* Panelbox::getOpenedItem(void) const
{
	return d_lastOpenItem;
}

/*************************************************************************
	Remove all items from the list.
*************************************************************************/
void Panelbox::resetList(void)
{
	if(d_lastOpenItem)
	{
		d_lastOpenItem->toggleIsOpen();
		d_lastOpenItem = NULL;
	}

	if (resetList_impl())
	{
		WindowEventArgs args(this);
		onListContentsChanged(args);

		d_progressBar->setVisible(false);
		d_pushButton->setVisible(false);
		d_comboBox->setVisible(false);	
	}

}

/*************************************************************************
	Add the given ListboxItem to the list.
*************************************************************************/
void Panelbox::addItem(PanelItem* item)
{
	if (item)
	{
		// establish ownership
		item->setOwnerWindow(this);
		d_listItems.insert(std::upper_bound(d_listItems.begin(), d_listItems.end(), item, &lbi_less), item);
		WindowEventArgs args(this);
		onListContentsChanged(args);
	}

}

void Panelbox::removeItem(const PanelItem* item)
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
			if (item == d_lastOpenItem)
			{
				d_lastOpenItem = 0;
			}

			WindowEventArgs args(this);
			onListContentsChanged(args);
		}
	} 
}

PanelItem* Panelbox::findFirstItemWithID(uint searchID)
{
	size_t itemCount = d_listItems.size();

	for (size_t index = 0; index < itemCount; ++index)
	{
			// Already found the startItem, now looking for the actual text.
		if (d_listItems[index]->getID() == searchID)
			return d_listItems[index];	
	}

	return 0;
}

// 
// /*************************************************************************
// 	Causes the list box to update it's internal state after changes have
// 	been made to one or more attached ListboxItem objects.
// *************************************************************************/
// void Panelbox::handleUpdatedItemData(void)
// { 
//     resortList();
// 	configureScrollbars();
// 	invalidate();
// }


/*************************************************************************
Perform the actual rendering for this Window.
*************************************************************************/
void Panelbox::populateGeometryBuffer()
{
	// Render list items
// 	Vector2  itemPos;
// 
// 	itemPos.d_x = d_itemArea.d_left;
// 	itemPos.d_y = d_itemArea.d_top - d_vertScrollbar->getScrollPosition();

	drawItemList(d_geometry,getEffectiveAlpha());
}


void Panelbox::drawItemList(GeometryBuffer* geometry, float alpha)
{
	if (d_listItems.empty())
		return;

	Vector2  itemPos;
	itemPos.d_x = d_itemArea.d_left;
	itemPos.d_y = d_itemArea.d_top - d_vertScrollbar->getScrollPosition();

	Size     itemSize;
	Rect     itemClipper, itemRect;
	size_t   itemCount = d_listItems.size();
	bool     itemIsVisible;

	for (size_t i = 0; i < itemCount; ++i)
	{
		if(d_listItems[i]->isHide() == true)
			continue;

		itemSize.d_height = d_listItems[i]->getSize().d_height;
		itemSize.d_width = d_itemArea.getWidth();

		// calculate destination area for this item.
		itemRect.d_left = itemPos.d_x;
		itemRect.d_top  = itemPos.d_y;
		itemRect.setSize(itemSize);

		itemClipper = itemRect.getIntersection(d_itemArea);
		
		if (itemClipper.getHeight() > 0)
		{

			itemIsVisible = true;
			d_listItems[i]->draw(geometry, itemRect, alpha, &itemClipper);

			// Process this item's list if it has items in it.
			if (d_listItems[i]->isOpen())
			{
				if(d_PanelType == CHENG_JIU)
				{
					ResetProgressBarPos(d_listItems[i],itemRect);
				}
				else if(d_PanelType == CHENG_WEI)
				{
					ResetPushBtnPos(itemRect);
				}
				else if(d_PanelType == QI_YUAN)
				{
					ResetComboboxPos(d_listItems[i],itemRect);
				}
			}

		}
		else if(d_listItems[i]->isOpen())
		{
			if(d_PanelType == CHENG_JIU)
			{
				d_progressBar->setVisible(false);
			}
			else if(d_PanelType == CHENG_WEI)
			{
				d_pushButton->setVisible(false);
			}
			else if(d_PanelType == QI_YUAN)
			{
				d_pushButton->setVisible(false);
				d_comboBox->setVisible(false);
			}
		}
		// update position ready for next item
		itemPos.d_y += itemSize.d_height;
		
	}
}

void Panelbox::ResetProgressBarPos(PanelItem* pItem,Rect& itemPos)
{
	PanelChengJiuItem* pChengJiuItem = static_cast<PanelChengJiuItem*>(pItem);
	if(pChengJiuItem->IsShowProgress() && pChengJiuItem->IsFinish() == false)
	{

		Rect Loc(itemPos);
		Loc.d_left += 112.0f;
		Loc.d_right = Loc.d_left  + 140.0f;
		Loc.d_top += 45.0f;
		Loc.d_bottom = Loc.d_top + 15;

// 		d_progressBar->setProgress(float(pChengJiuItem->GetCurGrade())/pChengJiuItem->GetTotalGrade());
// 		std::wstringstream namestream;
// 		namestream<<pChengJiuItem->GetCurGrade()<<L"/"<<pChengJiuItem->GetTotalGrade();
// 		d_progressBar->setText(namestream.str());

		d_progressBar->setPosition(UVector2(UDim(0,Loc.d_left),UDim(0,Loc.d_top)));
		d_progressBar->setVisible(true);
	}
	else
	{
		d_progressBar->setVisible(false);
	}
	
	//d_progressBar->setVisible(true);
}

void Panelbox::ResetComboboxPos(PanelItem* pItem,Rect& itemPos)
{
	PanelQiYuanItem* pQiYuanItem = static_cast<PanelQiYuanItem*>(pItem);
	if(pQiYuanItem->getWishState() == 1 || pQiYuanItem->getWishState() == 2)
	{
		d_comboBox->setVisible(false);

		Rect Loc(itemPos);
		d_comboBox->setVisible(false);

		Loc.d_left += 280.0f;
		Loc.d_right = Loc.d_left  + 70.0f;
		Loc.d_top += 67.0f;
		Loc.d_bottom = Loc.d_top + 30.0f;

		d_pushButton->setPosition(UVector2(UDim(0,Loc.d_left),UDim(0,Loc.d_top)));
		d_pushButton->setVisible(true);
		
	}
	else if(pQiYuanItem->getWishState() == 0)//¿ÉÆíÔ¸
	{
		Rect Loc(itemPos);

		Loc.d_left += 52.0f;
		Loc.d_right = Loc.d_left  + 80.0f;

		Loc.d_top += 42.0f;
		Loc.d_bottom = Loc.d_top + 100;

		d_comboBox->setPosition(UVector2(UDim(0,Loc.d_left),UDim(0,Loc.d_top)));
		d_comboBox->setVisible(true);

		Loc.d_left += 260.0f;
		Loc.d_right = Loc.d_left  + 60.0f;
		Loc.d_top += 25.0f;
		Loc.d_bottom = Loc.d_top + 30.0f;

		d_pushButton->setPosition(UVector2(UDim(0,Loc.d_left),UDim(0,Loc.d_top)));
		d_pushButton->setVisible(true);
	}
	else
	{
		d_pushButton->setVisible(false);
		d_comboBox->setVisible(false);
	}

	
}

void Panelbox::ResetPushBtnPos(Rect& itemPos)
{
	Rect Loc(itemPos);
	Loc.d_left += 290.0f;
	Loc.d_right = Loc.d_left  + 60.0f;
	Loc.d_top += 70.0f;
	Loc.d_bottom = Loc.d_top + 30;

	d_pushButton->setPosition(UVector2(UDim(0,Loc.d_left),UDim(0,Loc.d_top)));
	d_pushButton->setVisible(true);
	
}
/*************************************************************************
	display required integrated scroll bars according to current state
	of the list box and update their values.
*************************************************************************/
void Panelbox::configureScrollbars(void)
{
	float totalHeight	= getTotalItemsHeight();

	// First show or hide the scroll bars as needed (or requested)
	//
	// show or hide vertical scroll bar as required (or as specified by option)
	if (totalHeight > getListRenderArea().getHeight() || d_forceVertScroll)
	{
		d_vertScrollbar->show();
	}
	else
	{
		d_vertScrollbar->hide();
	}
	//
	// Set up scroll bar values
	//
	Rect renderArea(getListRenderArea());

	d_vertScrollbar->setDocumentSize(totalHeight);
	d_vertScrollbar->setPageSize(renderArea.getHeight());
	d_vertScrollbar->setStepSize(ceguimax(1.0f, 48.0f)/*renderArea.getHeight() / 10.0f)*/);
	d_vertScrollbar->setScrollPosition(d_vertScrollbar->getScrollPosition());
}

/*************************************************************************
	Return the sum of all item heights
*************************************************************************/
float Panelbox::getTotalItemsHeight(void) const
{
	float height = 0;

	for (size_t i = 0; i < getItemCount(); ++i)
	{
		height += d_listItems[i]->getSize().d_height;
	}

	return height;
}


/*************************************************************************
	Return the ListboxItem under the given window local pixel co-ordinate.
*************************************************************************/
PanelItem* Panelbox::getItemAtPoint(const Point& pt) const
{
	Rect renderArea(getListRenderArea());

	// point must be within the rendering area of the Panelbox.
	if (renderArea.isPointInRect(pt))
	{
		float y = renderArea.d_top - getVertScrollbar()->getScrollPosition();

		// test if point is above first item
		if (pt.d_y >= y)
		{
			for (size_t i = 0; i < getItemCount(); ++i)
			{
				if(d_listItems[i]->isHide() == true)
					continue;

				y += d_listItems[i]->getSize().d_height;

				if (pt.d_y < y)
				{
					return d_listItems[i];
				}

			}
		}
	}

	return 0;
}


/*************************************************************************
	Handler called internally when the list contents are changed
*************************************************************************/
void Panelbox::onListContentsChanged(WindowEventArgs& e)
{
	configureScrollbars();
	invalidate();
	fireEvent(EventListContentsChanged, e, EventNamespace);
}

/*************************************************************************
	Handler for when mouse button is pressed
*************************************************************************/
void Panelbox::onMouseButtonDown(MouseEventArgs& e)
{
	// base class processing
	bool modified = false;

	Point localPos(CoordConverter::screenToWindow(*this, e.position));

	PanelItem* item = getItemAtPoint(localPos);

	//if (::GetKeyState(VK_SHIFT) < 0  && item)
	//{
	//	PanelboxEventArgs args(this);
	//	args.panelItem = item; 
	//	onShiftClickItem(args);
	//}
	//else 
    if (item)
	{
		System::getSingleton().PlayUISound(UIClickSound);
		
		modified = true;
		
		item->toggleIsOpen();
		if (item->isOpen())
		{
			if(d_lastOpenItem)
			{
				d_lastOpenItem->toggleIsOpen();
			}
			d_lastOpenItem = item;

			PanelboxEventArgs args(this);
			args.panelItem = item;
			onSelectionChanged(args);

			//d_progressBar->setVisible(true);
		}
		else
		{
			if(d_lastOpenItem)
			{
				//d_lastOpenItem->toggleIsOpen();
				d_lastOpenItem = NULL;	
			}
			
			
			PanelboxEventArgs args(this);
			args.panelItem = NULL; 
			onSelectionChanged(args);

			if(d_PanelType != CHENG_JIU)
			{
				d_pushButton->setVisible(false);
				d_comboBox->setVisible(false);
			}
			else
			{
				d_progressBar->setVisible(false);
			}
		}

		// Update the item screen locations, needed to update the scrollbars.
		//	populateGeometryBuffer();

		// Opened or closed a tree branch, so must update scrollbars.
		configureScrollbars();
		invalidate();
	}

	Window::onMouseButtonDown(e);  
	++e.handled;
}


/*************************************************************************
	Handler for mouse wheel changes
*************************************************************************/
void Panelbox::onMouseWheel(MouseEventArgs& e)
{
	// base class processing.
	Window::onMouseWheel(e);

    Scrollbar* vertScrollbar = getVertScrollbar();
 
	if (vertScrollbar->isVisible() && (vertScrollbar->getDocumentSize() > vertScrollbar->getPageSize()))
	{
		vertScrollbar->setScrollPosition(vertScrollbar->getScrollPosition() + vertScrollbar->getStepSize() * -e.wheelChange);
	}
	++e.handled;
}

/*************************************************************************
Handler for mouse movement
*************************************************************************/
void Panelbox::onMouseMove(MouseEventArgs& e)
{
	Point posi(CoordConverter::screenToWindow(*this, e.position));
	//      Point posi = relativeToAbsolute(CoordConverter::screenToWindow(*this, e.position));
	PanelItem* item = getItemAtPoint(posi);
	if (item!=d_MouseOnItem)
	{
		if (d_MouseOnItem)
		{
			d_MouseOnItem->SetMouseOnState(false);
		}
		d_MouseOnItem=item;
		if (d_MouseOnItem)
		{
			d_MouseOnItem->SetMouseOnState(true);
		}
		invalidate();
	}
	Window::onMouseMove(e);
}

void Panelbox::onMouseLeaves(MouseEventArgs& e)
{
	if (d_MouseOnItem)
	{
		d_MouseOnItem->SetMouseOnState(false);
		d_MouseOnItem=NULL;
		invalidate();
	}

}


void Panelbox::onSelectionChanged(PanelboxEventArgs& e)
{
	//invalidate();
	fireEvent(EventSelectionChanged, e, EventNamespace);
}

void Panelbox::onShiftClickItem(PanelboxEventArgs& e)
{
	//invalidate();
	fireEvent(EventShiftClickItem, e, EventNamespace);
}

/*************************************************************************
Handler for scroll position changes.
*************************************************************************/
bool Panelbox::handle_scrollChange(const EventArgs&)
{
	// simply trigger a redraw of the Listbox.
	invalidate();
	return true;

}
/*************************************************************************
	Remove all items from the list.
*************************************************************************/
bool Panelbox::resetList_impl(void)
{
	// just return false if the list is already empty
	if (getItemCount() == 0)
	{
		return false;
	}
	// we have items to be removed and possible deleted
	else
	{
		d_listItems.clear();
		d_lastOpenItem = 0;
		return true;
	}

}

void Panelbox::setShowVertScrollbar(bool setting)
{
	if (d_forceVertScroll != setting)
	{
		d_forceVertScroll = setting;
		configureScrollbars();
	}
}

/*************************************************************************
    Function to resort the list data.
*************************************************************************/
void Panelbox::resortList()
{
    std::sort(d_listItems.begin(), d_listItems.end(), &lbi_less);
}

//////////////////////////////////////////////////////////////////////////
/*************************************************************************
	Functions used for predicates in std algorithms
*************************************************************************/
//////////////////////////////////////////////////////////////////////////
/*************************************************************************
	used for < comparisons between ListboxItem pointers
*************************************************************************/
bool lbi_less(const PanelItem* a, const PanelItem* b)
{
	return *a < *b;
}

/*************************************************************************
	used for > comparisons between ListboxItem pointers
*************************************************************************/
bool lbi_greater(const PanelItem* a, const PanelItem* b)
{
	return *a > *b;
}


} // End of  CEGUI namespace section
