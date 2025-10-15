/***********************************************************************
filename:  CEGUISpecialTreeItem.cpp
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

#include "elements/CEGUISpecialTree.h"
#include "elements/CEGUISpecialTreeItem.h"
#include "CEGUISystem.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIImageset.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUIWindow.h"
#include "CEGUIImage.h"
#include <algorithm>
#include "CEGUISpecialTreeItem_xmlHandler.h"

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
BasicRenderedStringParser SpecialTreeItem::d_stringParser;

/*************************************************************************
    Constants
*************************************************************************/
const colour SpecialTreeItem::DefaultTextColour = 0xFFFFFFFF;
const colour SpecialTreeItem::DoneTextColour = 0xFF00FF00;
const colour SpecialTreeItem::FailTextColour = 0xFFFF0000;

const int SpecialTreeItem::FailState = 2;
const int SpecialTreeItem::DoneState = 3;
const int SpecialTreeItem::UnDoneState = 4;
/*************************************************************************
    Base class constructor
*************************************************************************/
SpecialTreeItem::SpecialTreeItem(const String& text, uint item_id,const int64_t& item_time,Font* font, void* item_data) :
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
    d_buttonLocation(Rect(0,0,0,0)),
    d_owner(0),
    d_textCols(DefaultTextColour, DefaultTextColour,
               DefaultTextColour, DefaultTextColour),
    d_font(font),
    d_iconImage(0),
    d_isOpen(true),
	d_isOpenInfo(false),
    d_renderedStringValid(false),
	d_time(item_time)
{
	updateTextColour();
    setText(text);
}


/*************************************************************************
Remove all children items when this item  Destructor
*************************************************************************/
SpecialTreeItem::~SpecialTreeItem(void)
{
	// just return false if the list is already empty
	if (getChildrenCompentsCount() == 0)
	{
		return;
	}
	else
	{
		for (size_t i = 0; i < d_ChildrenCompents.size(); ++i)
		{
			delete d_ChildrenCompents[i];
			
		}
		d_ChildrenCompents.clear();
	}
}

/*************************************************************************
    Return a ColourRect object describing the colours in 'cols' after
    having their alpha component modulated by the value 'alpha'.
*************************************************************************/
ColourRect SpecialTreeItem::getModulateAlphaColourRect(const ColourRect& cols,
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
colour SpecialTreeItem::calculateModulatedAlphaColour(colour col, float alpha) const
{
    colour temp(col);
    temp.setAlpha(temp.getAlpha() * alpha);
    return temp;
}

/*************************************************************************
    Return a pointer to the font being used by this TreeItem
*************************************************************************/
Font* SpecialTreeItem::getFont(void) const
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
    Set the font to be used by this TreeItem
*************************************************************************/
void SpecialTreeItem::setFont(const String& font_name)
{
    setFont(&FontManager::getSingleton().get(font_name));
}

//----------------------------------------------------------------------------//
void SpecialTreeItem::setFont(Font* font)
{
    d_font = font;

    d_renderedStringValid = false;
}

//----------------------------------------------------------------------------//

//----------------------------------------------------------------------------//
void SpecialTreeItem::setTime(const int64_t& time)
{
	if(d_time != time)
	{
		d_time = time;
	}
}


void SpecialTreeItem::updateTextColour()
{
// 	if(d_state == FailState)
// 	{
// 		setTextColours(FailTextColour,FailTextColour,FailTextColour,FailTextColour);
// 	}
// 	else if(d_state == DoneState)
// 	{
// 		setTextColours(DoneTextColour,DoneTextColour,DoneTextColour,DoneTextColour);
// 	}
// 	else
// 	{
// 		setTextColours(DefaultTextColour,DefaultTextColour,DefaultTextColour,DefaultTextColour);
// 	}

}
/*************************************************************************
    Return the rendered pixel size of this tree item.
*************************************************************************/
Size SpecialTreeItem::getPixelSize(void) const
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

/*************************************************************************
    Add the given TreeItem to this item's list.
*************************************************************************/
void SpecialTreeItem::addChildCompent(RichEditboxComponent* compent)
{
    if (compent != 0)
    {
       
        d_ChildrenCompents.push_back(compent);           
//         WindowEventArgs args(parentWindow);
//         parentWindow->onListContentsChanged(args);
    }
}

RichEditboxComponent *SpecialTreeItem::getCompentFromIndex(size_t compentIndex)
{
    if (compentIndex > d_ChildrenCompents.size())
        return 0;
    
    return d_ChildrenCompents[compentIndex];
}

void SpecialTreeItem::ParseChildrenCompentText(const String& ParseText)
{
	for (size_t i = 0; i < d_ChildrenCompents.size(); ++i)
	{
		delete d_ChildrenCompents[i];

	}
	d_ChildrenCompents.clear();

	if (ParseText.size()>0)
	{
		String NewParseText("<content>");
		NewParseText+=ParseText;
		NewParseText+=" </content>";
		CEGUI::SpecialTreeItem_xmlHandler handle(NewParseText,this);
		//format();
	}
}

/*************************************************************************
    Draw the tree item in its current state.
*************************************************************************/
void SpecialTreeItem::draw(GeometryBuffer* buffer, const Rect &targetRect, float alpha, const Rect *clipper) const
{
    Rect finalRect(targetRect);

    if (d_iconImage != 0)
    {
        Rect finalPos(finalRect);
        finalPos.setWidth(targetRect.getHeight());
        finalPos.setHeight(targetRect.getHeight());
        d_iconImage->draw(buffer, finalPos, clipper,
                          ColourRect(colour(1,1,1,alpha)));
        finalRect.d_left += targetRect.getHeight();
    }


	Font* font = getFont();

	if (!font)
		return;

	Vector2 draw_pos(finalRect.getPosition());
	font->drawText(buffer, d_textLogical, draw_pos, clipper,d_textCols/*,0.0f,1.0f,1.0f,false,true,ColourRect(0xFF5F4100)*/);

/*
    if (!d_renderedStringValid)
        parseTextString();

    const ColourRect final_colours(
        getModulateAlphaColourRect(ColourRect(0xFFFFFFFF), alpha));

    for (size_t i = 0; i < d_renderedString.getLineCount(); ++i)
    {
        d_renderedString.draw(i, buffer, draw_pos, &final_colours, clipper, 0.0f);
        draw_pos.d_y += d_renderedString.getPixelSize(i).d_height;
    }
	*/
}

/*************************************************************************
Set the colours used for text rendering.
*************************************************************************/
void SpecialTreeItem::setTextColours(colour top_left_colour,
							  colour top_right_colour,
							  colour bottom_left_colour,
							  colour bottom_right_colour)
{
	d_textCols.d_top_left		= top_left_colour;
	d_textCols.d_top_right		= top_right_colour;
	d_textCols.d_bottom_left	= bottom_left_colour;
	d_textCols.d_bottom_right	= bottom_right_colour;

	d_renderedStringValid = false;
}

//----------------------------------------------------------------------------//
void SpecialTreeItem::setText( const String& text )
{
    d_textLogical = text;
    d_bidiDataValid = false;
	d_renderedStringValid = false;
}

//----------------------------------------------------------------------------//
void SpecialTreeItem::parseTextString() const
{
    d_renderedString =
        d_stringParser.parse(getTextVisual(), getFont(), &d_textCols);
    d_renderedStringValid = true;
}

//----------------------------------------------------------------------------//
const String& SpecialTreeItem::getTextVisual() const
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
