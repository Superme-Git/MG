/***********************************************************************
	filename: 	CEGUIListboxTextItem.cpp
	created:	12/6/2004
	author:		Paul D Turner
	
	purpose:	Implementation of List box text items
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
#include "elements/CEGUIListboxTextItem.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUIWindow.h"
#include "CEGUIImage.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIPropertyHelper.h"

// Start of CEGUI namespace section
namespace CEGUI
{
//----------------------------------------------------------------------------//
BasicRenderedStringParser ListboxTextItem::d_stringParser;

/*************************************************************************
	Constants
*************************************************************************/
const colour	ListboxTextItem::DefaultTextColour		= 0xFFFFFFFF;


/*************************************************************************
	Constructor
*************************************************************************/
ListboxTextItem::ListboxTextItem(const String& text, uint item_id, void* item_data, colour textcoulor,bool disabled, bool auto_delete) :
	ListboxItem(text, item_id, item_data, disabled, auto_delete),
	d_textCols(textcoulor, textcoulor, textcoulor, textcoulor),
	d_font(0),
    d_renderedStringValid(false),
	d_TextHorFormat(eListBoxTextItemHorFormat_Left),
	d_ConnectID(0),
	d_MouseOnBursh(0),
	d_SelectBrushLeft(NULL),
	d_SelectBrushMiddle(NULL),
	d_SelectBrushRight(NULL),
	d_StaticImageWidth(-1.f),
	d_StaticImageHeight(-1.f)
{
	 //setSelectionBrushImage("component", "ListBoxItemMouseOn");
	 //d_MouseOnBursh=&ImagesetManager::getSingleton().get("component").getImage("ListBoxItemMouseOn");
	 //start by wjf 2/17/2016 --delete image
	//d_SelectBrushLeft = &ImagesetManager::getSingleton().get("component5").getImage("SelectionBrushLeft");
	//d_SelectBrushMiddle = &ImagesetManager::getSingleton().get("component5").getImage("SelectionBrushMiddle");
	//d_SelectBrushRight = &ImagesetManager::getSingleton().get("component5").getImage("SelectionBrushRight");
	d_SelectBrushLeft = ImagesetManager::getSingleton().getImage("common", "common_biaoshi_cc");
	d_SelectBrushMiddle = ImagesetManager::getSingleton().getImage("common", "common_biaoshi_cc");
	d_SelectBrushRight = ImagesetManager::getSingleton().getImage("common", "common_biaoshi_cc");
	 //end by wjf 2/17/2016 --delete image

	 //SetTextHorFormat(eListBoxTextItemHorFormat_Center);

	 d_StaticImage = NULL;
}

/*************************************************************************
	Return a pointer to the font being used by this ListboxTextItem
*************************************************************************/
Font* ListboxTextItem::getFont(void) const
{
	// prefer out own font
	if (d_font)
	{
		return d_font;
	}
	// try our owner window's font setting (may be null if owner uses no existant default font)
	else if (d_owner)
	{
		return d_owner->getFont();
	}
	// no owner, just use the default (which may be NULL anyway)
	else
	{
		return System::getSingleton().getDefaultFont();
	}

}


/*************************************************************************
	Set the font to be used by this ListboxTextItem
*************************************************************************/
void ListboxTextItem::setFont(const String& font_name)
{
	setFont(&FontManager::getSingleton().get(font_name));
}

//----------------------------------------------------------------------------//
void ListboxTextItem::setFont(Font* font)
{
    d_font = font;

    d_renderedStringValid = false;
}


/*************************************************************************
	Return the rendered pixel size of this list box item.
*************************************************************************/
Size ListboxTextItem::getPixelSize(void) const
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

	if (sz.d_height>0.5f)
	{
		sz.d_height+=getTopHeight()+getBottomHeight();
	}
	

    return sz;
}


/*************************************************************************
	Draw the list box item in its current state.
*************************************************************************/
void ListboxTextItem::draw(GeometryBuffer* buffer, const Rect& targetRect,
                           float alpha, const Rect* clipper,bool bDrawSelBrush) const
{
    
	if ((d_selected||d_MouseOn)&&bDrawSelBrush )
	{
		
		Rect DrawTargetRect(targetRect);
		DrawTargetRect.d_top=targetRect.d_top;
		DrawTargetRect.d_bottom=DrawTargetRect.d_top+29.0f;
		DrawTargetRect.d_left-=1.f;
		DrawTargetRect.d_right+=1.0f;

		Rect clipRect(targetRect);
		if (clipper)
		{
			clipRect=*clipper;
			clipRect.d_left-=1.f;
			clipRect.d_top-=0.0f;
			clipRect.d_bottom+=1.0f;
			clipRect.d_right+=1.0f;
		}
		
		drawSelectBrush(buffer, DrawTargetRect, &clipRect,alpha);

	}

	if (d_StaticImage != NULL) // 画图片 by changhao
	{
		float w, h = 0.f;
		if (d_StaticImageWidth == -1.f && d_StaticImageHeight == -1.f)
		{
			w = std::min(d_StaticImage->getWidth(), targetRect.getWidth());
			h = std::min(d_StaticImage->getHeight(), targetRect.getHeight());
		}
		else
		{
			w = d_StaticImageWidth;
			h = d_StaticImageHeight;
		}

		CEGUI::Point pos;
		pos.d_x = std::max((targetRect.getWidth() - w) * 0.5f, 0.0f) + targetRect.d_left;
		pos.d_y = std::max((targetRect.getHeight() - h) * 0.5f, 0.0f) + targetRect.d_top;

		Rect DrawTargetRect;
		DrawTargetRect.setPosition(pos);
		DrawTargetRect.setWidth(w);
		DrawTargetRect.setHeight(h);

		d_StaticImage->draw(buffer, DrawTargetRect, clipper, getModulateAlphaColourRect(ColourRect(0xFFFFFFFF), alpha));
	}
        
	/*if (d_MouseOn&&d_MouseOnBursh&&bDrawSelBrush)
	{
		ColourRect color(0xffffffff);
		d_MouseOnBursh->draw(buffer, targetRect, clipper,
			getModulateAlphaColourRect(color, alpha));
	}
	*/

    Font* font = getFont();

    if (!font)
        return;

    Vector2 draw_pos(targetRect.getPosition());
    draw_pos.d_y += getTopHeight();

	switch (d_TextHorFormat)
	{
	case eListBoxTextItemHorFormat_Left:      //靠左
		{
			draw_pos.d_x+=0.0f;

		}
		break;
	case eListBoxTextItemHorFormat_Center:  //居中
		{
			Size textSize=getPixelSize();
			draw_pos.d_x+=(targetRect.getWidth()-textSize.d_width)/2.0f;

		}
		break;
	case eListBoxTextItemHorFormat_Right:  //靠右
		{
			Size textSize=getPixelSize();
			draw_pos.d_x+=textSize.d_width;
		}
		break;
	}
	
	

    draw_pos.d_y += PixelAligned(
        (font->getLineSpacing() - font->getFontHeight()) * 0.5f);

    if (!d_renderedStringValid)
        parseTextString();

	ColourRect colorRect(0xFFFFFFFF);
	//if (!bDrawSelBrush)                 //预定bDrawSelBrush为false则为multiColumn的
	//{
	//	if (!d_selected)
	//	{
	//		colorRect.setColours(colour(0xFF12C4FF));
	//	}
	//	
	//}
	

    const ColourRect final_colours(
        getModulateAlphaColourRect(colorRect, alpha));

    for (size_t i = 0; i < d_renderedString.getLineCount(); ++i)
    {
        d_renderedString.draw(i, buffer, draw_pos, &final_colours, clipper, 0.0f);
        draw_pos.d_y += d_renderedString.getPixelSize(i).d_height;
    }
}

void ListboxTextItem::drawSelectBrush(GeometryBuffer* buffer, const Rect& targetRect, const Rect* clipper, float alpha) const
{
	if (d_SelectBrushLeft&&d_SelectBrushMiddle&&d_SelectBrushRight)
	{
		float leftWidth=d_SelectBrushLeft->getWidth();
		float RightWidth=d_SelectBrushRight->getWidth();
		float middleDrawWidth=targetRect.getWidth()-leftWidth-RightWidth;
		
		Rect LeftDrawRect(targetRect);
		LeftDrawRect.d_right=LeftDrawRect.d_left+leftWidth;
		d_SelectBrushLeft->draw(buffer,LeftDrawRect,clipper,getModulateAlphaColourRect(ColourRect(0xFFFFFFFF), alpha));

		Rect MiddleDrawRect(targetRect);
		MiddleDrawRect.d_left=LeftDrawRect.d_right;
		MiddleDrawRect.d_right=MiddleDrawRect.d_left+middleDrawWidth;
		d_SelectBrushMiddle->draw(buffer,MiddleDrawRect,clipper,getModulateAlphaColourRect(ColourRect(0xFFFFFFFF), alpha));

		Rect RightDrawRect(targetRect);
		RightDrawRect.d_left=targetRect.d_right-RightWidth;
		RightDrawRect.d_right=targetRect.d_right;
		d_SelectBrushRight->draw(buffer,RightDrawRect,clipper,getModulateAlphaColourRect(ColourRect(0xFFFFFFFF), alpha));
	}
	
	//d_selectBrush->draw(buffer, targetRect, clipper,
	//	getModulateAlphaColourRect(d_selectCols, alpha));

}


/*************************************************************************
	Set the colours used for text rendering.	
*************************************************************************/
void ListboxTextItem::setTextColours(colour top_left_colour,
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
void ListboxTextItem::setText(const String& text)
{
    ListboxItem::setText(text);

    d_renderedStringValid = false;
}

//----------------------------------------------------------------------------//
void ListboxTextItem::parseTextString() const
{
    d_renderedString =
        d_stringParser.parse(getTextVisual(), getFont(), &d_textCols);
    d_renderedStringValid = true;
}

void ListboxTextItem::setOwnerWindow(const Window* owner,bool ownerIsMultiCloumn)
{
	ListboxItem::setOwnerWindow(owner);
	if (ownerIsMultiCloumn)
	{
		SetTextHorFormat(eListBoxTextItemHorFormat_Center);
	}
	
}

void ListboxTextItem::setStaticImage(const String& name)
{
	d_StaticImage = (Image*)CEGUI::PropertyHelper::stringToImage(name);
}


void ListboxTextItem::setStaticImageWidthAndHeight(float w /*= -1*/, float h /*= -1*/)
{
	this->d_StaticImageWidth = w;
	this->d_StaticImageHeight = h;
}

} // End of  CEGUI namespace section
