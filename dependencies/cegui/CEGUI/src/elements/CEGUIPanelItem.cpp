/***********************************************************************
	filename:   CEGUIPanelItem.cpp
	purpose:	Implementation of base class for list items
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
#ifdef HAVE_CONFIG_H
#   include "config.h"
#endif

#include "elements/CEGUIPanelItem.h"
#include "CEGUISystem.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIImageset.h"
#include "CEGUIWindow.h"
#include "elements/CEGUIListbox.h"

// Start of CEGUI namespace section
namespace CEGUI
{

/*************************************************************************
	Base class constructor
*************************************************************************/
PanelItem::PanelItem(const String& name,const String& intro,const String& tips, int item_id,bool bAutodelete, void* item_data,bool bHide) :
d_ItemName(name),
d_ItemIntro(intro),
d_Itemtips(tips),
d_ItemID(item_id),
d_bAutoDelete(bAutodelete),
d_ItemData(item_data),
d_isOpen(false),
d_MouseOn(false),
d_hide(bHide),
d_BackImage(0),
d_LeftIcon(0),
d_owner(0),
d_SortID(0)
{
  
}

Size PanelItem::getSize() const
{
	if(d_BackImage && !d_hide)
		return d_BackImage->getSize();
	return Size(0,0);
}
/*************************************************************************
	Return a ColourRect object describing the colours in 'cols' after
	having their alpha component modulated by the value 'alpha'.
*************************************************************************/
ColourRect PanelItem::getModulateAlphaColourRect(const ColourRect& cols, float alpha) const
{
	return ColourRect
		(
			calculateModulatedAlphaColour(cols.d_top_left, alpha),
			calculateModulatedAlphaColour(cols.d_top_right, alpha),
			calculateModulatedAlphaColour(cols.d_bottom_left, alpha),
			calculateModulatedAlphaColour(cols.d_bottom_right, alpha)
		);
}


/*************************************************************************
	Return a colour value describing the colour specified by 'col' after
	having its alpha component modulated by the value 'alpha'.
*************************************************************************/
colour PanelItem::calculateModulatedAlphaColour(colour col, float alpha) const
{
	colour temp(col);
	temp.setAlpha(temp.getAlpha() * alpha);
	return temp;
}

//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
