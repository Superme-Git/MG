/***********************************************************************
	filename: 	CEGUIPanelbox.h
	purpose:	Interface to base class for Listbox widget
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
#ifndef _CEGUIPanelbox_h_
#define _CEGUIPanelbox_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "CEGUIListboxProperties.h"
#include "CEGUIPanelItem.h"
#include <vector>


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{

	class CEGUIEXPORT PanelboxEventArgs : public WindowEventArgs
	{
	public:
		PanelboxEventArgs(Window* wnd) : WindowEventArgs(wnd)
		{ panelItem = 0; }

		PanelItem * panelItem;
	};
/*!
\brief
	Base class for standard Listbox widget.
*/
class CEGUIEXPORT Panelbox : public Window
{
public:

	enum PanelType
	{
		CHENG_JIU = 1,//成就
		CHENG_WEI = 2,//称谓、勋章
		QI_YUAN = 3,//祈愿
	};
	static const String EventNamespace;				//!< Namespace for global events
    static const String WidgetTypeName;             //!< Window factory name

	/*************************************************************************
		Constants
	*************************************************************************/
	// event names
	static const String EventListContentsChanged;			//!< Event triggered when the contents of the list is changed.
	static const String EventSelectionChanged;			//!< Event triggered when there is a change to the currently selected item(s).
	static const String EventShiftClickItem;
	/*************************************************************************
		Accessor Methods
	*************************************************************************/
	/*!
	\brief
		Return number of items attached to the list box

	\return
		the number of items currently attached to this list box.
	*/
	size_t	getItemCount(void) const		{return d_listItems.size();}

	PanelItem*	getOpenedItem(void) const;

    PanelItem* findFirstItemWithID(uint searchID);

	/*************************************************************************
		Manipulator Methods
	*************************************************************************/
	/*!
	\brief
		Initialise the Window based object ready for use.

	\note
		This must be called for every window created.  Normally this is handled automatically by the WindowFactory for each Window type.

	\return
		Nothing
	*/
	virtual void	initialiseComponents(bool bClone);
	
	void	setPanelType(PanelType type) { d_PanelType = type;}
	void	resetList(void);
	void	addItem(PanelItem* item);
	void    removeItem(const PanelItem* item);


	virtual	Rect getListRenderArea(void) const
	{ return d_itemArea; }

	void setItemRenderArea(Rect& r)
	{ d_itemArea = r; }

	Scrollbar* getVertScrollbar() const { return d_vertScrollbar; }
	PushButton* getPushButton() const { return d_pushButton;}
	Combobox*   getComboBox() const { return d_comboBox;}
	ProgressBar*   getProgressBar() const { return d_progressBar;}

    float   getTotalItemsHeight(void) const;

	//UpdateScrollbars
	void doScrollbars()
	{ configureScrollbars(); }

	//Render the actual tree
	void doPanelRender()
	{ populateGeometryBuffer(); }

	Panelbox(const String& type, const String& name);
	virtual ~Panelbox(void);


public:
	Panelbox(const String& type);
	const Panelbox& operator=(const Panelbox& t);
	virtual Window* clone(Window* wnd);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

protected:
	
	

	// overridden from Window base class.
	virtual void populateGeometryBuffer();

	void drawItemList(GeometryBuffer* buffer, float alpha);
	void ResetProgressBarPos(PanelItem* pItem,Rect& itemPos);
	void ResetComboboxPos(PanelItem* pItem,Rect& itemPos);
	void ResetPushBtnPos(Rect& itemPos);

	/*************************************************************************
		Implementation Functions
	*************************************************************************/
	/*!
	\brief
		display required integrated scroll bars according to current state of the list box and update their values.
	*/
	void	configureScrollbars(void);

public:
	/*!
	\brief
		Return the ListboxItem under the given window local pixel co-ordinate.

	\return
		ListboxItem that is under window pixel co-ordinate \a pt, or NULL if no
		item is under that position.
	*/
	PanelItem*	getItemAtPoint(const Point& pt) const;
	bool isBackGroundEnable() { return d_BackGroundEnable; }
	void SetBackGroundEnable(bool bBackGroundEnable) { d_BackGroundEnable=bBackGroundEnable; }
	void setShowVertScrollbar(bool setting);
	void resortList();

	void ClearLastOpenItem() { d_MouseOnItem = 0;d_lastOpenItem = 0;}
protected:
	bool	resetList_impl(void);

	virtual bool	testClassName_impl(const String& class_name) const
	{
		if (class_name=="Panelbox")	return true;
		return Window::testClassName_impl(class_name);
	}

    bool handle_scrollChange(const EventArgs& args);


//     // validate window renderer
//     virtual bool validateWindowRenderer(const String& name) const
//     {
//         return (name == "Panelbox");
//     }

    

	/*************************************************************************
		New event handlers
	*************************************************************************/
	/*!
	\brief
		Handler called internally when the list contents are changed
	*/
	virtual	void	onListContentsChanged(WindowEventArgs& e);
	void			onSelectionChanged(PanelboxEventArgs& e);
	void			onShiftClickItem(PanelboxEventArgs& e);

	/*************************************************************************
		Overridden Event handlers
	*************************************************************************/
	virtual void	onMouseButtonDown(MouseEventArgs& e);
	virtual	void	onMouseWheel(MouseEventArgs& e);
	virtual void    onMouseMove(MouseEventArgs& e);
	virtual void    onMouseLeaves(MouseEventArgs& e);

	/*************************************************************************
		Implementation Data
	*************************************************************************/
	typedef	std::vector<PanelItem*>	LBItemList;
	
	PanelType  d_PanelType;
	LBItemList	d_listItems;		//!< list of items in the list box.
	PanelItem*	d_lastOpenItem;	//!< holds pointer to the last selected item (used in range selections)
	PanelItem* d_MouseOnItem;
	bool    d_BackGroundEnable;
	Rect	d_itemArea;
	
	Scrollbar* d_vertScrollbar;
	bool d_forceVertScroll;

	ProgressBar* d_progressBar;//成就选项的进度条
	Combobox*    d_comboBox;//祈愿选项的祈愿金额下拉条
	PushButton*  d_pushButton;//祈愿选项的祈愿按钮

    friend class PanelboxWindowRenderer;

};


/*!
\brief
	Helper function used in sorting to compare two list box item text strings
	via the ListboxItem pointers and return if \a a is less than \a b.
*/
bool lbi_less(const PanelItem* a, const PanelItem* b);


/*!
\brief
	Helper function used in sorting to compare two list box item text strings
	via the ListboxItem pointers and return if \a a is greater than \a b.
*/
bool lbi_greater(const PanelItem* a, const PanelItem* b);

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIListbox_h_
