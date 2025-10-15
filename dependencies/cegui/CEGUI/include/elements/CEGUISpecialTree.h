/***********************************************************************
    filename:   CEGUISpecialTree.h
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
#ifndef _CEGUISpecialTree_h_
#define _CEGUISpecialTree_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "../CEGUIWindowManager.h"
#include "CEGUISpecialTreeItem.h"
#include "elements/CEGUIRichEditboxGoToComponent.h"
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
class CEGUIEXPORT SpecialTreeEventArgs : public WindowEventArgs
{
public:
    SpecialTreeEventArgs(Window* wnd) : WindowEventArgs(wnd)
        { treeItem = 0; }

    SpecialTreeItem *treeItem;
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
class CEGUIEXPORT SpecialTree : public Window
{
    friend class SpecialTreeItem;
    typedef	std::vector<SpecialTreeItem*>	LBSpecialItemList;
    
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
    //! Event triggered when the sort mode setting changes.
    static const String EventSortModeChanged;
    //! Event triggered when the multi-select mode setting changes.
    static const String EventMultiselectModeChanged;
    //! Event triggered when the vertical scroll bar 'force' setting changes.
    static const String EventVertScrollbarModeChanged;
    //! Event triggered when the horizontal scroll bar 'force' setting changes.
    static const String EventHorzScrollbarModeChanged;
    //! Event triggered when a branch of the tree is opened by the user.
    static const String EventBranchOpened;
    //! Event triggered when a branch of the tree is closed by the user.
    static const String EventBranchClosed;

	static const String EventOpenItemInfo;//打开任务详细信息
	static const String EventCloseItemInfo;//关闭任务详细信息
	
	static const float CompentHeight;

	static const float LeftOffect;

	static const float buttonWH;//按钮的宽高;
    //Render the actual tree
    void doTreeRender()
        { populateGeometryBuffer(); }

    //UpdateScrollbars
    void doScrollbars()
        { configureScrollbars(); }

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
        return whether tree sorting is enabled
     
     \return
        - true if the tree is sorted
        - false if the tree is not sorted
     */
    bool isSortEnabled(void) const
        { return d_sorted; }
    
    void setItemRenderArea(Rect& r)
        { d_itemArea = r; }
    
    Scrollbar* getVertScrollbar()
        { return d_vertScrollbar; }

//     Scrollbar* getHorzScrollbar()
//         { return d_horzScrollbar; }
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
    SpecialTreeItem* findItemWithIDFromList(uint searchID);

    /*!
     \brief
        Return whether the specified TreeItem is in the tree
     
     \return
        - true if TreeItem \a item is in the tree
        - false if TreeItem \a item is not in the tree.
     */
    bool isTreeItemInList(const SpecialTreeItem* item) const;

    /*!
     \brief
        Return whether the vertical scroll bar is always shown.
     
     \return
         - true if the scroll bar will always be shown even if it is not required.
         - false if the scroll bar will only be shown when it is required.
     */
    bool isVertScrollbarAlwaysShown(void) const;

    /*!
     \brief
        Return whether the horizontal scroll bar is always shown.
     
     \return
         - true if the scroll bar will always be shown even if it is not required.
         - false if the scroll bar will only be shown when it is required.
     */
    bool isHorzScrollbarAlwaysShown(void) const;

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
    void addItem(SpecialTreeItem* item);
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
    void removeItem(const SpecialTreeItem* item);

    /*!
     \brief
        Set whether the tree should be sorted.
     
     \param setting
        - true if the tree should be sorted
        - false if the tree should not be sorted.
     
     \return
        Nothing.
     */
    void setSortingEnabled(bool setting);

	void reSort(); //重新排序

	void ClearLastMouseOnCompent(){ if(d_LastMouseOnComment){ d_LastMouseOnComment->onMouseLeave();d_LastMouseOnComment = NULL;}}

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

	SpecialTreeItem* getItemAtPoint(const Point& pt, int* itemindex) const;
    
    RichEditboxComponent *getCompentAtPoint(const Point& pt, int& itemindex) const; //参数itemindex表示第itemindex个Item节点
	RichEditboxComponent *getFirstGoToCompent();
	/*!
	\brief
	Return the sum of all item heights
	*/
	float getTotalItemsHeight(void) const;

	/*!
	\brief
	Return the width of the widest item
	*/
	float getWidestItemWidth(void) const;
	
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
	Rect getFirstGoToCompentPos(void) const;
	/*************************************************************************
     Construction and Destruction
     *************************************************************************/
    /*!
     \brief
        Constructor for Tree base class.
     */
    SpecialTree(const String& type, const String& name);

    /*!
     \brief
        Destructor for Tree base class.
     */
    virtual ~SpecialTree(void);

public:
	SpecialTree(const String& type);
	const SpecialTree& operator=(const SpecialTree& t);
	virtual Window* clone(Window* wnd);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return SpecialTree::onRenameTemplatePrefix(sPrefix); }


protected:
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
        create and return a pointer to a Scrollbar widget for use as horizontal
        scroll bar.
     
     \param name
        String holding the name to be given to the created widget component.
     
     \return
        Pointer to a Scrollbar to be used for scrolling the tree horizontally.
     */
    virtual Scrollbar* createHorzScrollbar(const String& name) const
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
    void configureScrollbars(void);
    
   
   
 

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
        if (class_name==(const utf8*)"SpecialTree")
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
 
     void drawItemList(Rect& itemsArea, float widest, Vector2& itemPos, GeometryBuffer* buffer, float alpha);
    
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
        Handler called internally when the forced display of the vertical scroll
        bar setting changes.
     */
    virtual	void onVertScrollbarModeChanged(WindowEventArgs& e);

    /*!
     \brief
        Handler called internally when the forced display of the horizontal
        scroll bar setting changes.
     */
    virtual	void onHorzScrollbarModeChanged(WindowEventArgs& e);
    
    /*!
     \brief
        Handler called internally when the user opens a branch of the tree.
     */
    virtual	void onBranchOpened(SpecialTreeEventArgs& e);
    
    /*!
     \brief
        Handler called internally when the user closes a branch of the tree.
     */
    virtual	void onBranchClosed(SpecialTreeEventArgs& e);

	/*!
	\brief
	Handler called internally when the user opens a branch of the tree.
	*/
	virtual	void onOpenItemInfo(SpecialTreeEventArgs& e);

	/*!
	\brief
	Handler called internally when the user closes a branch of the tree.
	*/
	virtual	void onCloseItemInfo(SpecialTreeEventArgs& e);

	 
    /*************************************************************************
        Overridden Event handlers
     *************************************************************************/
    virtual void onSized(WindowEventArgs& e);
    virtual void onMouseButtonDown(MouseEventArgs& e);
    virtual	void onMouseWheel(MouseEventArgs& e);
    virtual void onMouseMove(MouseEventArgs& e);
	virtual void onMouseEnters(MouseEventArgs& e);
	virtual void onMouseLeaves(MouseEventArgs& e);
    virtual	bool onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer);

    /*************************************************************************
        Implementation Data
     *************************************************************************/
    //! true if tree is sorted
    bool d_sorted;
    //! true if vertical scrollbar should always be displayed
    bool d_forceVertScroll;
    //! true if horizontal scrollbar should always be displayed
    bool d_forceHorzScroll;
    //! vertical scroll-bar widget
    Scrollbar* d_vertScrollbar;
    //! horizontal scroll-bar widget
   // Scrollbar* d_horzScrollbar;
    //! list of items in the tree.
    LBSpecialItemList d_listItems;
	SpecialTreeItem* d_OpenInfoItem;
    ImagerySection* d_openButtonImagery;
    ImagerySection* d_closeButtonImagery;
	ImagerySection* d_OpenInfoImagery;
	ImagerySection* d_CloseInfoImagery;
	RichEditboxComponent* d_LastMouseOnComment;
private:
    Rect d_itemArea;
};

/*!
 \brief
    Helper function used in sorting to compare two tree item text strings
    via the TreeItem pointers and return if \a a is less than \a b.
 */
bool lbi_less(const SpecialTreeItem* a, const SpecialTreeItem* b);


/*!
 \brief
    Helper function used in sorting to compare two tree item text strings
    via the TreeItem pointers and return if \a a is greater than \a b.
 */
bool lbi_greater(const SpecialTreeItem* a, const SpecialTreeItem* b);

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUITree_h_
