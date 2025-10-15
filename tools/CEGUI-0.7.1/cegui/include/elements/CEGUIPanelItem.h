/***********************************************************************
    filename:   CEGUIPanelItem.h
    purpose:    Interface to base class for panel items
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
#ifndef _CEGUIPanelItem_h_
#define _CEGUIPanelItem_h_

#include "../CEGUIBase.h"
#include "../CEGUIString.h"
#include "../CEGUIColourRect.h"
#include "../CEGUITextUtils.h"

#if defined(_MSC_VER)
#  pragma warning(push)
#  pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
/*!
\brief
    Base class for list box items
*/
class CEGUIEXPORT PanelItem
{
public:

    PanelItem(const String& name,const String& intro,const String& tips, int item_id = 0, bool bAutodelete = false,void* item_data = 0,bool bHide = false);

    virtual ~PanelItem(void) {}

    int    getID(void) const           {return d_ItemID;}
   
	void*   getUserData(void) const     {return d_ItemData;}

	String getItemName(void) const {return d_ItemName;}
   
	bool    isOpen(void) const      {return d_isOpen;}
	bool    isAutoDelete() const     {return d_bAutoDelete;}
	bool    isHide() const { return d_hide;}
  
	// void    setTooltipText(const String& text)      {d_tooltipText = text;}
    void    setID(int item_id)     {d_ItemID = item_id;}

    void    setSortID(int sortid)     {d_SortID = sortid;}

    void    setUserData(void* item_data)    {d_ItemData = item_data;}

	void    setHide(bool bHide) { d_hide = bHide;}

	virtual void toggleIsOpen(void)
	{ d_isOpen = !d_isOpen; }

   virtual void setOwnerWindow(const Window* owner) {d_owner = owner;}

    void    setBackImage(const Image* image)      {d_BackImage = image;}
	void    setLeftIconImage(const Image* icon)      {d_LeftIcon = icon;}
	
	bool isMouseOn() { return d_MouseOn; }
	void SetMouseOnState(bool state) { d_MouseOn=state; }


    virtual Size getSize(void) const;

    virtual void draw(GeometryBuffer* buffer, const Rect& targetRect, float alpha, const Rect* clipper) = 0;

    /*************************************************************************
        Operators
    *************************************************************************/
    /*!
    \brief
        Less-than operator, compares item texts.
    */
    virtual bool    operator<(const PanelItem& rhs) const     {return d_ItemID < rhs.d_ItemID;}
    /*!
    \brief
        Greater-than operator, compares item texts.
    */
    virtual bool    operator>(const PanelItem& rhs) const     {return d_ItemID > rhs.d_ItemID;}

	ColourRect getModulateAlphaColourRect(const ColourRect& cols, float alpha) const;

protected:
    /*!
    \brief
        Return a colour value describing the colour specified by \a col after having its alpha
        component modulated by the value \a alpha.
    */
    colour calculateModulatedAlphaColour(colour col, float alpha) const;


    /*************************************************************************
        Implementation Data
    *************************************************************************/
    String d_ItemName;
	String d_ItemIntro;//说明
	String d_Itemtips;//面板展开时的tips说明
	

	const Image*    d_BackImage; //面板背景图
	const Image*    d_LeftIcon; //面板左边的图标
	
	int  d_ItemID;
	int  d_SortID;
	bool d_isOpen;//是否展开
	bool d_bAutoDelete;
	bool d_hide;//是否隐藏
	bool d_MouseOn;
    void*   d_ItemData;     //!< Pointer to some client code data.  This has no meaning within the GUI system.
    const Window*   d_owner;    //!< Pointer to the window that owns this item.
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#  pragma warning(pop)
#endif

#endif  // end of guard _CEGUIListboxItem_h_
