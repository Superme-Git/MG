/***********************************************************************
     filename:  CEGUIGroupBtnItem.cpp
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

#include "elements/CEGUITree.h"
#include "elements/CEGUIGroupBtnItem.h"
#include "CEGUISystem.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIImageset.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUIWindow.h"
#include "CEGUIImage.h"
#include <algorithm>

#if defined (CEGUI_USE_FRIBIDI)
    #include "CEGUIFribidiVisualMapping.h"
#elif defined (CEGUI_USE_MINIBIDI)
    #include "CEGUIMinibidiVisualMapping.h"
#else
    #include "CEGUIBiDiVisualMapping.h"
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
//----------------------------------------------------------------------------//
BasicRenderedStringParser GroupBtnItem::d_stringParser;

/*************************************************************************
    Constants
*************************************************************************/
const colour GroupBtnItem::DefaultSelectionColour = 0xFF50321A;// 0xFFFFFFFF;
const colour GroupBtnItem::DefaultTextColour = 0xFF50321A;// 0xFF693F00;
const colour GroupBtnItem::DefaultHoveringColour = 0xFF50321A;// 0xFFFFFFFF;

/*************************************************************************
    Base class constructor
*************************************************************************/
GroupBtnItem::GroupBtnItem(const String& text, uint item_id,colour boardcol, void* item_data) :
#ifndef CEGUI_BIDI_SUPPORT
    d_bidiVisualMapping(0),
#elif defined (CEGUI_USE_FRIBIDI)
    d_bidiVisualMapping(new FribidiVisualMapping),
#elif defined (CEGUI_USE_MINIBIDI)
    d_bidiVisualMapping(new MinibidiVisualMapping),
#else
    #error "BIDI Configuration is inconsistant, check your config!"
#endif
    d_bidiDataValid(false),
    d_itemID(item_id),
    d_itemData(item_data),
    d_selected(false),
	d_disabled(false),
    d_owner(0),
	d_ownerItem(0),
    d_selectCols(DefaultSelectionColour, DefaultSelectionColour,
                 DefaultSelectionColour, DefaultSelectionColour),
    //d_selectBrush(0),
    d_textCols(DefaultTextColour, DefaultTextColour,
               DefaultTextColour, DefaultTextColour),
    d_hoverCols(DefaultHoveringColour, DefaultHoveringColour,
			   DefaultHoveringColour, DefaultHoveringColour),
    d_boardCol(boardcol),
    d_font(0),
    d_isOpen(false),
    d_renderedStringValid(false),
	d_formattedRenderedString(0),
	d_MouseOn(false),
	d_NormalImage(0),
	d_HoverImage(0),
	d_SelectionImage(0),
	d_OpenImage(0),
	d_OrnamentImage(0)
{
    setText(text);
	
	if (ImagesetManager::getSingleton().isDefined("common"))
    {
        const Imageset& imageSet=ImagesetManager::getSingleton().get("common");
        if (imageSet.isImageDefined("treehuang1"))
        {
             d_NormalImage = &imageSet.getImage("treehuang1");
        }
        
        if (imageSet.isImageDefined("treelan1"))
        {
            d_HoverImage = &imageSet.getImage("treelan1");
        }
        
        if (imageSet.isImageDefined("treelan1"))
        {
            d_SelectionImage = &imageSet.getImage("treelan1");
            
        }
        
        if (imageSet.isImageDefined("treelan1"))
        {
            d_OpenImage = &imageSet.getImage("treelan1");
            
        }
    }

	d_OrnamentPosition.d_x = 0.0f;
	d_OrnamentPosition.d_y = 0.0f;

//    d_NormalImage = &ImagesetManager::getSingleton().get("MainControl3").getImage("TrackNormal");
//	d_HoverImage=&ImagesetManager::getSingleton().get("MainControl3").getImage("TrackPushed");
//	d_SelectionImage=&ImagesetManager::getSingleton().get("MainControl3").getImage("BlueFirgureBtnSelected");
//	d_OpenImage=&ImagesetManager::getSingleton().get("MainControl3").getImage("BlueFirgureBtnSelected");
}


/*************************************************************************
Remove all children items when this item  Destructor
*************************************************************************/
GroupBtnItem::~GroupBtnItem(void)
{
	if(d_formattedRenderedString)
	{
		delete d_formattedRenderedString;
	}

	// just return false if the list is already empty
	if (getItemCount() == 0)
	{
		return;
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

		// clear out the listItem.
		d_listItems.clear();
	}
}

void GroupBtnItem::RemoveChildrenItems()
{
	for (size_t i = 0; i < getItemCount(); ++i)
	{
		delete d_listItems[i];
	}

	// clear out the listItem.
	d_listItems.clear();
}
/*************************************************************************
    Set the normal image.
*************************************************************************/
void GroupBtnItem::seNormalImage(const String& imageset,
                                      const String& image)
{
    seNormalImage(
        ImagesetManager::getSingleton().getImage(imageset, image));
}

/*************************************************************************
Set the hover image.
*************************************************************************/
void GroupBtnItem::setHoverImage(const String& imageset,
									 const String& image)
{
	setHoverImage(
		ImagesetManager::getSingleton().getImage(imageset, image));
}

/*************************************************************************
Set the selection highlighting brush image.
*************************************************************************/
void GroupBtnItem::setSelectionImage(const String& imageset,
									 const String& image)
{
	setSelectionImage(
		ImagesetManager::getSingleton().getImage(imageset, image));
}

/*************************************************************************
Set the Open image.
*************************************************************************/
void GroupBtnItem::setOpenImage(const String& imageset,
								 const String& image)
{
	setOpenImage(
		ImagesetManager::getSingleton().getImage(imageset, image));
}

/*************************************************************************
Set the Ornament image.
*************************************************************************/
void GroupBtnItem::setOrnamentImage(const String& imageset,
									const String& image)
{
	setOrnamentImage(
		ImagesetManager::getSingleton().getImage(imageset, image));
}

void GroupBtnItem::setOrnamentPosition(float x, float y)
{
	d_OrnamentPosition.d_x = x;
	d_OrnamentPosition.d_y = y;
}

/*************************************************************************
    Return a ColourRect object describing the colours in 'cols' after
    having their alpha component modulated by the value 'alpha'.
*************************************************************************/
ColourRect GroupBtnItem::getModulateAlphaColourRect(const ColourRect& cols,
                                                float alpha) const
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
colour GroupBtnItem::calculateModulatedAlphaColour(colour col, float alpha) const
{
    colour temp(col);
    temp.setAlpha(temp.getAlpha() * alpha);
    return temp;
}

/*************************************************************************
    Return a pointer to the font being used by this GroupBtnItem
*************************************************************************/
Font* GroupBtnItem::getFont(void) const
{
    // prefer out own font
    if (d_font != 0)
        return d_font;
    // try our owner window's font setting
    // (may be null if owner uses non existant default font)
    else if (d_owner != 0)
        return d_owner->getFont();
    // no owner, just use the default (which may be NULL anyway)
    else
        return System::getSingleton().getDefaultFont();   
}

/*************************************************************************
    Set the font to be used by this GroupBtnItem
*************************************************************************/
void GroupBtnItem::setFont(const String& font_name)
{
    setFont(&FontManager::getSingleton().get(font_name));
}

//----------------------------------------------------------------------------//
void GroupBtnItem::setFont(Font* font)
{
    d_font = font;

    d_renderedStringValid = false;
}

//----------------------------------------------------------------------------//

/*************************************************************************
    Return the rendered pixel size of this tree item.
*************************************************************************/
Size GroupBtnItem::getPixelSize(void) const
{
    Font* fnt = getFont();

    if (!fnt)
        return Size(0, 0);

    if (!d_renderedStringValid)
        parseTextString();

    Size sz(0.0f, 0.0f);

    for (size_t i = 0; i < d_renderedString.getLineCount(); ++i)
    {
        const Size line_sz(d_renderedString.getPixelSize(i));
        sz.d_height += line_sz.d_height;

        if (line_sz.d_width > sz.d_width)
            sz.d_width = line_sz.d_width;
    }

    return sz;
}


Size GroupBtnItem::getItemSize() const
{
	Size size(0.0f,0.0f);
    if (d_NormalImage)
    {
        return d_NormalImage->getSize();
    }
    return size;
}
/*************************************************************************
    Add the given GroupBtnItem to this item's list.
*************************************************************************/
GroupBtnItem* GroupBtnItem::addItem(const String& text, uint item_id,colour boardcol, void* item_data ) {
    GroupBtnItem* newItem = new GroupBtnItem(text, item_id, boardcol, item_data);
    addItem(newItem);
    return newItem;
}
void GroupBtnItem::addItem(GroupBtnItem* item)
{
    if (item != 0)
    {
       // Tree* parentWindow = (Tree*)getOwnerWindow();
        
        // establish ownership
       // item->setOwnerWindow(parentWindow);
		item->setOwernItem(this);
      
        d_listItems.push_back(item);
          
       // WindowEventArgs args(parentWindow);
       // parentWindow->onListContentsChanged(args);
    }
}

/*************************************************************************
    Remove the given GroupBtnItem from this item's list.
*************************************************************************/
void GroupBtnItem::removeItem(const GroupBtnItem* item) 
{ 
    if (item)
    {
        Tree* parentWindow = (Tree*)getOwnerWindow();

        LBItemList::iterator pos = std::find(d_listItems.begin(),
                                             d_listItems.end(),
                                             item);
        if (pos != d_listItems.end())
        {
            (*pos)->setOwnerWindow(0);
            d_listItems.erase(pos);

//             if (item == parentWindow->d_lastSelected)
//                 parentWindow->d_lastSelected = 0;
//              delete item;
// 
//             WindowEventArgs args(parentWindow);
//             parentWindow->onListContentsChanged(args);
        }
    }
}

void GroupBtnItem::setSelected(bool setting)
{
	d_selected = setting;
// 	if(d_selected)
// 	{
// 		if(getParentItem() && getParentItem()->isSelected()==false)
// 		{
// 			getParentItem()->setSelected(true);
// 		}
// 	}
	
}

GroupBtnItem *GroupBtnItem::getTreeItemFromIndex(size_t itemIndex)
{
    if (itemIndex > d_listItems.size())
        return 0;
    
    return d_listItems[itemIndex];
}

/*************************************************************************
    Draw the tree item in its current state.
*************************************************************************/
void GroupBtnItem::draw(GeometryBuffer* buffer, const Rect &targetRect,
                    float alpha, const Rect *clipper) const
{
	ColourRect final_colours;
    ColourRect image_colours;
    Rect finalRect(targetRect);
	Size imagesize = d_NormalImage->getSize();

	finalRect.d_left += (targetRect.getWidth()- imagesize.d_width)/2;
	
	finalRect.setWidth(imagesize.d_width);
	finalRect.setHeight(imagesize.d_height);
	
    image_colours = getModulateAlphaColourRect(ColourRect(0XFFFFFFFF), alpha);
    
	if(d_selected && d_SelectionImage)
	{
		 final_colours = getModulateAlphaColourRect(d_selectCols, alpha);
		 d_SelectionImage->draw(buffer,finalRect,clipper,image_colours);
	}
	else if(d_MouseOn && d_HoverImage)
	{
		final_colours = getModulateAlphaColourRect(d_hoverCols, alpha);
		d_HoverImage->draw(buffer,finalRect,clipper,image_colours);
	}
   
	else if(d_isOpen && d_OpenImage)
	{
		final_colours = getModulateAlphaColourRect(d_selectCols, alpha);
		d_OpenImage->draw(buffer,finalRect,clipper,image_colours);
	}
	else if(d_NormalImage)
	{
		final_colours = getModulateAlphaColourRect(d_textCols, alpha);
		d_NormalImage->draw(buffer,finalRect,clipper,image_colours);
	}

	drawText(buffer,targetRect,final_colours,clipper);
   
	if (d_OrnamentImage)
	{
		imagesize = d_OrnamentImage->getSize();
		Rect rect(d_OrnamentPosition + finalRect.getPosition(), imagesize);
		d_OrnamentImage->draw(buffer, rect, clipper, image_colours);
	}
}

void GroupBtnItem::drawText(GeometryBuffer* buffer, const Rect& targetRect, const ColourRect& color, const Rect* clipper) const
{
	if (!d_renderedStringValid)
		parseTextString();

	if (!d_formattedRenderedString)
	{
		d_formattedRenderedString =new CentredRenderedString(d_renderedString);
		d_formattedRenderedString->format(clipper->getSize());
		
		//comment on 2016.4.6.
		//d_formattedRenderedString->SetBorderInf((d_selected || d_isOpen || d_MouseOn), d_boardCol);

		//if(d_boardCol> 0)
		//{
		//	//border.setARGB(0xFF03568B);
		//	d_formattedRenderedString->SetBorderInf(true,d_boardCol);
		//}
	}
    if (g_bIsTextLoading)
    d_formattedRenderedString->format(clipper->getSize());
    
    //if(d_boardCol> 0)
    //{
    //    //border.setARGB(0xFF03568B);
    //    d_formattedRenderedString->SetBorderInf(true,d_boardCol);
    //}

	//comment on 2016.4.6.
	//d_formattedRenderedString->SetBorderInf((d_selected || d_isOpen || d_MouseOn), d_boardCol);

	if (d_formattedRenderedString)
	{

		Rect absarea(targetRect);
		float textHeight = d_formattedRenderedString->getVerticalExtent();
		absarea.d_top += PixelAligned((absarea.getHeight() - textHeight) * 0.5f);
		
		//colorRect.modulateAlpha(d_window->getEffectiveAlpha());
		// cache the text for rendering.
		d_formattedRenderedString->draw(buffer,
			absarea.getPosition(),
			&color, clipper);
	}
}
/*************************************************************************
    Set the colours used for selection highlighting.
*************************************************************************/
void GroupBtnItem::setSelectionColours(colour top_left_colour,
                                   colour top_right_colour,
                                   colour bottom_left_colour,
                                   colour bottom_right_colour)
{
    d_selectCols.d_top_left		= top_left_colour;
    d_selectCols.d_top_right	= top_right_colour;
    d_selectCols.d_bottom_left	= bottom_left_colour;
    d_selectCols.d_bottom_right	= bottom_right_colour;
}

/*************************************************************************
    Set the colours used for text rendering.
*************************************************************************/
void GroupBtnItem::setTextColours(colour top_left_colour,
                              colour top_right_colour,
                              colour bottom_left_colour,
                              colour bottom_right_colour)
{
    d_textCols.d_top_left		= top_left_colour;
    d_textCols.d_top_right		= top_right_colour;
    d_textCols.d_bottom_left	= bottom_left_colour;
    d_textCols.d_bottom_right	= bottom_right_colour;


}

//----------------------------------------------------------------------------//
void GroupBtnItem::setText( const String& text )
{
    d_textLogical = text;
    d_bidiDataValid = false;
	d_renderedStringValid = false;
	if(d_formattedRenderedString)
	{
		delete d_formattedRenderedString;
		d_formattedRenderedString = NULL;
	}
}

//----------------------------------------------------------------------------//
void GroupBtnItem::parseTextString() const
{
    ColourRect color(0xFFFFFFFF);
    d_renderedString =
		d_stringParser.parse(getTextVisual(), getFont(), &color);
    d_renderedStringValid = true;
}

//---------------------------------------------------------------------------- //
const String& GroupBtnItem::getTextVisual() const
{
    // no bidi support
    if (!d_bidiVisualMapping)
        return d_textLogical;

    if (!d_bidiDataValid)
    {
        d_bidiVisualMapping->updateVisual(d_textLogical);
        d_bidiDataValid = true;
    }

    return d_bidiVisualMapping->getTextVisual();
}

//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
