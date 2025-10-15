/***********************************************************************
    filename:   CEGUIGroupBtnTree.h
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
#ifndef _CEGUIGroupBtnTree_h_
#define _CEGUIGroupBtnTree_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "../CEGUIWindowManager.h"
#include "CEGUIGroupBtnItem.h"
#include "CEGUITreeProperties.h"
#include <vector>

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
/*!
 \brief
    EventArgs based class that is used for objects passed to input event
    handlers concerning Tree events.
 */
class CEGUIEXPORT GroupBtnTreeEventArgs : public WindowEventArgs
{
public:
    GroupBtnTreeEventArgs(Window* wnd) : WindowEventArgs(wnd)
        { treeItem = 0; }

    GroupBtnItem *treeItem;
};


/*!
 \brief
    Base class for standard Tree widget.

 \deprecated
    The CEGUI::Tree, CEGUI::TreeItem and any other associated classes are
    deprecated and thier use should be minimised - preferably eliminated -
    where possible.  It is extremely unfortunate that this widget was ever added
    to CEGUI since its design and implementation are poor and do not meet 
    established standards for the CEGUI project.
    \par
    While no alternative currently exists, a superior, replacement tree widget
    will be provided prior to the final removal of the current implementation.
 */
class CEGUIEXPORT GroupBtnTree : public Window
{
    friend class GroupBtnItem;
    typedef	std::vector<GroupBtnItem*>	LBItemList;
    
public:
    //! Namespace for global events
    static const String EventNamespace;
    static const String WidgetTypeName;
    
    /*************************************************************************
        Constants
     *************************************************************************/
    // event names
    //! Event triggered when the contents of the tree is changed.
    static const String EventListContentsChanged;
    //! Event triggered when there is a change to the currently selected item(s).
    static const String EventSelectionChanged;
    static const String EventBranchOpened;
    //! Event triggered when a branch of the tree is closed by the user.
    static const String EventBranchClosed;

	/*************************************************************************
	Properties for GroupBtnTree class
	*************************************************************************/
	static  TreeProperties::MaskImage d_MaskImageProperty;

    //Render the actual tree
    void doTreeRender()
        { populateGeometryBuffer(); }

    //UpdateScrollbars
    void doScrollbars()
        { configureScrollbars(false); }

    /*************************************************************************
        Accessor Methods
     *************************************************************************/
    /*!
     \brief
        Return number of items attached to the tree
     
     \return
        the number of items currently attached to this tree.
     */
    size_t getItemCount(void) const
	{ return d_listItems.size(); }

    /*!
     \brief
        Return a pointer to the first selected item.
     
     \return
        Pointer to a TreeItem based object that is the first selected item in
        the tree.  will return 0 if no item is selected.
     */
    GroupBtnItem* getSelectedItem(void) const;
    
	void SetLastOpenItem(GroupBtnItem* pOpenItem, size_t selectindex = 0);// { d_lastOpenItem = pOpenItem;}

	void SetLastSelectItem(GroupBtnItem* pSelectItem);// { d_lastOpenItem = pOpenItem;}
    
    void setItemRenderArea(Rect& r)
        { d_itemArea = r; }
    
    Scrollbar* getVertScrollbar()
        { return d_vertScrollbar; }

    bool isItemTooltipsEnabled(void) const
        { return d_itemTooltips; }

    /*!
     \brief
        Search the tree for an item with the specified text
     
     \param text
        String object containing the text to be searched for.
     
     \param start_item
        TreeItem where the search is to begin, the search will not include
        \a item.  If \a item is 0, the search will begin from the first item in
        the tree.
     
     \return
        Pointer to the first TreeItem in the tree after \a item that has text
        matching \a text.  If no item matches the criteria 0 is returned.
     
     \exception	InvalidRequestException	thrown if \a item is not attached to
        this tree.
     */
    GroupBtnItem* findFirstItemWithID(uint searchID);
    GroupBtnItem* findNextItemWithID(uint searchID, const GroupBtnItem* start_item);
    GroupBtnItem* findItemWithIDFromList(const LBItemList &itemList, uint searchID,
                                     const GroupBtnItem* start_item,
                                     bool foundStartItem);

    /*!
     \brief
        Return whether the specified TreeItem is in the tree
     
     \return
        - true if TreeItem \a item is in the tree
        - false if TreeItem \a item is not in the tree.
     */
    bool isTreeItemInList(const GroupBtnItem* item) const;

   

    /*************************************************************************
        Manipulator Methods
     *************************************************************************/
    /*!
     \brief
        Initialise the Window based object ready for use.
     
     \note
        This must be called for every window created.  Normally this is handled
        automatically by the WindowFactory for each Window type.

     \return
        Nothing
     */
    virtual void initialise(void);

    /*!
     \brief
        Remove all items from the tree.
     
        Note that this will cause 'AutoDelete' items to be deleted.
     */
    void resetList(void);

    /*!
     \brief
        Add the given TreeItem to the tree.
     
     \param item
         Pointer to the TreeItem to be added to the tree.  Note that it is the
         passed object that is added to the tree, a copy is not made.  If this
         parameter is NULL, nothing happens.
     
     \return
        Nothing.
     */
	GroupBtnItem* addItem(const String& text, uint item_id = 0, colour boardcol = 0xFF015885, void* item_data = 0);
    void addItem(GroupBtnItem* item);

    /*!
     \brief
        Insert an item into the tree after a specified item already in the
        tree.
     
        Note that if the tree is sorted, the item may not end up in the
        requested position.
     
     \param item
         Pointer to the TreeItem to be inserted.  Note that it is the passed
         object that is added to the tree, a copy is not made.  If this
         parameter is 0, nothing happens.
     
     \param position
         Pointer to a TreeItem that \a item is to be inserted after.  If this
         parameter is 0, the item is inserted at the start of the tree.
     
     \return
        Nothing.
     
     \exception InvalidRequestException	thrown if no TreeItem \a position is
        attached to this tree.
     */
    void insertItem(GroupBtnItem* item, const GroupBtnItem* position);

    /*!
     \brief
        Removes the given item from the tree.  If the item is has the auto
        delete state set, the item will be deleted.
     
     \param item
        Pointer to the TreeItem that is to be removed.  If \a item is not
        attached to this tree then nothing will happen.
     
     \return
        Nothing.
     */
    void removeItem(const GroupBtnItem* item);

    /*!
     \brief
        Clear the selected state for all items.
     
     \return
        Nothing.
     */
    void clearAllSelections(void);
    bool clearAllSelectionsFromList(const LBItemList &itemList);

    /*!
     \brief
        Set whether the vertical scroll bar should always be shown.
     
     \param setting
         - true if the vertical scroll bar should be shown even when it is not
           required.
         - false if the vertical scroll bar should only be shown when it is
           required.

     \return
        Nothing.
     */
    void setShowVertScrollbar(bool setting);

    /*!
     \brief
        Set whether the horizontal scroll bar should always be shown.
     
     \param setting
         - true if the horizontal scroll bar should be shown even when it is not
           required.
         - false if the horizontal scroll bar should only be shown when it is
           required.

     \return
        Nothing.
     */
    void setShowHorzScrollbar(bool setting);
    
    void setItemTooltipsEnabled(bool setting);

    /*!
     \brief
        Set the select state of an attached TreeItem.
     
        This is the recommended way of selecting and deselecting items attached
        to a tree as it respects the multi-select mode setting.  It is
        possible to modify the setting on TreeItems directly, but that approach
        does not respect the settings of the tree.
     
     \param item
        The TreeItem to be affected.
        This item must be attached to the tree.
     
     \param state
        - true to select the item.
        - false to de-select the item.
     
     \return
        Nothing.
     
     \exception	InvalidRequestException	thrown if \a item is not attached to
        this tree.
     */
    void setItemSelectState(GroupBtnItem* item, bool state);

    /*!
     \brief
        Set the LookNFeel that shoule be used for this window.
     
     \param look
        String object holding the name of the look to be assigned to the window.
     
     \return
        Nothing.
     
     \exception UnknownObjectException
        thrown if the look'n'feel specified by \a look does not exist.
     
     \note
        Once a look'n'feel has been assigned it is locked - as in cannot be
        changed.
     */
	virtual void setLookNFeel(const String& look, bool bClone);
    
    /*!
     \brief
        Causes the tree to update it's internal state after changes have
        been made to one or more attached TreeItem objects.
     
        Client code must call this whenever it has made any changes to TreeItem
        objects already attached to the tree.  If you are just adding items,
        or removed items to update them prior to re-adding them, there is no
        need to call this method.
     
     \return
        Nothing.
     */
    void handleUpdatedItemData(void);

    /*!
     \brief
        Ensure the item at the specified index is visible within the tree.
     
     \param item
        Pointer to the TreeItem to be made visible in the tree.
     
     \return
        Nothing.
     
     \exception	InvalidRequestException	thrown if \a item is not attached to
        this tree.
     */
    void ensureItemIsVisible(const GroupBtnItem* item);

	/*
	 count the height from top to item
	 */
	float getHeightToItem(const GroupBtnItem* item);

	GroupBtnItem* getItemAtPoint(const Point& pt) const;
    
    
    /*************************************************************************
     Construction and Destruction
     *************************************************************************/
    /*!
     \brief
        Constructor for Tree base class.
     */
    GroupBtnTree(const String& type, const String& name);

    /*!
     \brief
        Destructor for Tree base class.
     */
    virtual ~GroupBtnTree(void);

	// 设置右键点击是否相应消息
	void SetRightButtonEnbale(bool bEnbale);

	void SetMaskImage(int index, const String& imagesetName, const String& imageName);
	const Image* GetMaskImage(int index) const;
	void ClearMaskImage(int index);
	void ClearAllMaskImage();

public:
	GroupBtnTree(const String& type);
	const GroupBtnTree& operator=(const GroupBtnTree& t);
	virtual Window* clone(Window* wnd);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }
	bool onSetTemplateLookNFeel();

protected:
	void addGroupBtnTreeProperties(void);

    /*************************************************************************
        Abstract Implementation Functions (must be provided by derived class)
     *************************************************************************/
    /*!
     \brief
        Return a Rect object describing, in un-clipped pixels, the window
        relative area that is to be used for rendering tree items.
     
     \return
        Rect object describing the area of the Window to be used for rendering
        tree items.
     */
    virtual	Rect getTreeRenderArea(void) const
        { return d_itemArea; }
    
    /*!
     \brief
        create and return a pointer to a Scrollbar widget for use as vertical
        scroll bar.
     
     \param name
        String holding the name to be given to the created widget component.
     
     \return
        Pointer to a Scrollbar to be used for scrolling the tree vertically.
     */
    virtual Scrollbar* createVertScrollbar(const String& name) const
        { return (Scrollbar*)(WindowManager::getSingleton().getWindow(name)); }
  /*!
     \brief
         Perform caching of the widget control frame and other 'static' areas.
         This method should not render the actual items.  Note that the items
         are typically rendered to layer 3, other layers can be used for
         rendering imagery behind and infront of the items.
     
     \return
        Nothing.
     */
    virtual	void cacheTreeBaseImagery()
    {}
    
    /*************************************************************************
        Implementation Functions
     *************************************************************************/
    /*!
     \brief
        Checks if a tree item is visible (searches sub-items)
    */
    bool containsOpenItemRecursive(const LBItemList& itemList, GroupBtnItem* item);

    /*!
     \brief
        Add tree specific events
     */
    void addTreeEvents(void);
    
    
    /*!
     \brief
        display required integrated scroll bars according to current state of
        the tree and update their values.
     */
    void configureScrollbars(bool checkpos = true);
    
    
    /*!
     \brief
        Return the sum of all item heights
     */
    float getTotalItemsHeight(void) const;
    void getTotalItemsInListHeight(const LBItemList &itemList,
                                    float *heightSum) const;
    /*!
     \brief
        Clear the selected state for all items (implementation)
     
     \return
        - true if treeItem was found in the search.
        - false if it was not.
     */
    bool getHeightToItemInList(const LBItemList &itemList,
                               const GroupBtnItem *treeItem,
                               int itemDepth,
                               float *height) const;


    /*!
     \brief
        Return the TreeItem under the given window local pixel co-ordinate.
     
     \return
         TreeItem that is under window pixel co-ordinate \a pt, or 0 if no
         item is under that position.
     */
  
    GroupBtnItem* getItemFromListAtPoint(const LBItemList &itemList, float *bottomY,
                                     const Point& pt) const;

    /*!
     \brief
        Remove all items from the tree.
     
     \note
        Note that this will cause 'AutoDelete' items to be deleted.
     
     \return
         - true if the tree contents were changed.
         - false if the tree contents were not changed (tree already empty).
     */
    bool resetList_impl(void);

    /*!
     \brief
        Return whether this window was inherited from the given class name at
        some point in the inheritance heirarchy.
     
     \param class_name
        The class name that is to be checked.
     
     \return
        true if this window was inherited from \a class_name. false if not.
     */
    virtual bool testClassName_impl(const String& class_name) const
    {
        if (class_name==(const utf8*)"GroupBtnTree")
            return true;

        return Window::testClassName_impl(class_name);
    }
    
    /*!
     \brief
        Internal handler that is triggered when the user interacts with the
        scrollbars.
     */
    bool handle_scrollChange(const EventArgs& args);
    
     // overridden from Window base class.
     virtual void populateGeometryBuffer();
 
     void drawItemList(LBItemList& itemList, Rect& itemsArea, float widest,
                       Vector2& itemPos, GeometryBuffer* geometry, float alpha, bool bFirst = true);
    
    /*************************************************************************
        New event handlers
     *************************************************************************/
    /*!
     \brief
        Handler called internally when the tree contents are changed
     */
    virtual	void onListContentsChanged(WindowEventArgs& e);

    /*!
     \brief
        Handler called internally when the currently selected item or items
        changes.
     */
    virtual	void onSelectionChanged(GroupBtnTreeEventArgs& e);
    
    /*!
     \brief
        Handler called internally when the user opens a branch of the tree.
     */
    virtual	void onBranchOpened(GroupBtnTreeEventArgs& e);
    
    /*!
     \brief
        Handler called internally when the user closes a branch of the tree.
     */
    virtual	void onBranchClosed(GroupBtnTreeEventArgs& e);
    
    /*************************************************************************
        Overridden Event handlers
     *************************************************************************/
    virtual void onSized(WindowEventArgs& e);
	virtual void onMouseClicked(MouseEventArgs& e);
    virtual	void onMouseWheel(MouseEventArgs& e);
    virtual void onMouseMove(MouseEventArgs& e);
    virtual	bool onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer);
	virtual void onMouseButtonDown(MouseEventArgs& e);
	virtual void onMouseButtonUp(MouseEventArgs& e);
	virtual void onMouseLeaves(MouseEventArgs& e);

	void OnItemOpend(GroupBtnItem* pOpenItem);

	void setMaskImage(int index, const Image* image);

    /*************************************************************************
        Implementation Data
     *************************************************************************/
    //! true if each item should have an individual tooltip
    bool d_itemTooltips;
	//! 右键相应onSelectionChanged
	bool d_rightButtonEnbale;
    //! vertical scroll-bar widget
    Scrollbar* d_vertScrollbar;
    //! list of items in the tree.
    LBItemList d_listItems;
    //! holds pointer to the last selected item (used in range selections)
    GroupBtnItem* d_lastSelected;
	GroupBtnItem* d_lastOpenItem;//上次展开的节点
	GroupBtnItem* d_lastSelectedParentItem;//选中的大节点，当有选中的子节点时，大节点收缩后，大节点被选中
	GroupBtnItem* d_MouseOnItem;
	const Image*  d_maskImage[2];    //遮罩图
	
private:
    Rect d_itemArea;
};

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUITree_h_
