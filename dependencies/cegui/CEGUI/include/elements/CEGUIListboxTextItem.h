/***********************************************************************
	filename: 	CEGUIListboxTextItem.h
	created:	12/6/2004
	author:		Paul D Turner
	
	purpose:	Interface for list box text items
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
#ifndef _CEGUIListboxTextItem_h_
#define _CEGUIListboxTextItem_h_
#include "CEGUIListboxItem.h"
#include "../CEGUIBasicRenderedStringParser.h"

// Start of CEGUI namespace section
namespace CEGUI
{
/*!
\brief
	Class used for textual items in a list box.
*/

enum ListBoxTextItemHorFormat  //文字靠齐方式
{
	eListBoxTextItemHorFormat_Left,    //靠左
	eListBoxTextItemHorFormat_Center,  //居中
	eListBoxTextItemHorFormat_Right    //靠右
};
class CEGUIEXPORT ListboxTextItem : public ListboxItem
{
public:
	/*************************************************************************
		Constants
	*************************************************************************/
	static const colour	DefaultTextColour;			//!< Default text colour.


	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		base class constructor
	*/
	ListboxTextItem(const String& text, uint item_id = 0, void* item_data = 0, colour textcoulor = 0xFFFFFFFF, bool disabled = false, bool auto_delete = true);


	/*!
	\brief
		base class destructor
	*/
	virtual ~ListboxTextItem(void) {}


	/*************************************************************************
		Accessor methods
	*************************************************************************/
	/*!
	\brief
		Return a pointer to the font being used by this ListboxTextItem

		This method will try a number of places to find a font to be used.  If no font can be
		found, NULL is returned.

	\return
		Font to be used for rendering this item
	*/
	Font*	getFont(void) const;


	/*!
	\brief
		Return the current colours used for text rendering.

	\return
		ColourRect object describing the currently set colours
	*/
	ColourRect	getTextColours(void) const		{return d_textCols;}

	int64_t getConnectID() { return d_ConnectID;}

	void setConnectID(int64_t connectid) { d_ConnectID = connectid;}
	/*************************************************************************
		Manipulator methods
	*************************************************************************/
	/*!
	\brief
		Set the font to be used by this ListboxTextItem

	\param font
		Font to be used for rendering this item

	\return
		Nothing
	*/
    void setFont(Font* font);


	/*!
	\brief
		Set the font to be used by this ListboxTextItem

	\param font_name
		String object containing the name of the Font to be used for rendering this item

	\return
		Nothing
	*/
	void	setFont(const String& font_name);


	/*!
	\brief
		Set the colours used for text rendering.

	\param cols
		ColourRect object describing the colours to be used.

	\return
		Nothing.
	*/
	void	setTextColours(const ColourRect& cols)			{d_textCols = cols;}


	/*!
	\brief
		Set the colours used for text rendering.

	\param top_left_colour
		Colour (as ARGB value) to be applied to the top-left corner of each text glyph rendered.

	\param top_right_colour
		Colour (as ARGB value) to be applied to the top-right corner of each text glyph rendered.

	\param bottom_left_colour
		Colour (as ARGB value) to be applied to the bottom-left corner of each text glyph rendered.

	\param bottom_right_colour
		Colour (as ARGB value) to be applied to the bottom-right corner of each text glyph rendered.

	\return 
		Nothing.
	*/
	void	setTextColours(colour top_left_colour, colour top_right_colour, colour bottom_left_colour, colour bottom_right_colour);


	/*!
	\brief
		Set the colours used for text rendering.

	\param col
		colour value to be used when rendering.

	\return
		Nothing.
	*/
	void	setTextColours(colour col)		{setTextColours(col, col, col, col);}

    // base class overrides
    void setText(const String& text);


	/*************************************************************************
		Required implementations of pure virtuals from the base class.
	*************************************************************************/
    Size getPixelSize(void) const;
    void draw(GeometryBuffer* buffer, const Rect& targetRect, float alpha, const Rect* clipper,bool bDrawSelBrush=true) const;

	void drawSelectBrush(GeometryBuffer* buffer, const Rect& targetRect, const Rect* clipper, float alpha) const;

	void SetTextHorFormat(ListBoxTextItemHorFormat HorFormat) { d_TextHorFormat=HorFormat; }

	bool isTextItem() { return true; }

	void setOwnerWindow(const Window* owner,bool ownerIsMultiCloumn=false);
    
    float getTopHeight() const { return 13.0f; } //space above text
    float getBottomHeight() const { return 13.0f; } //space below text

	void setStaticImage(const String& name);
	//w 自定义宽度
	//h 自定义高度
	//注 当且仅当 w 和 h 等于-1 时 根据图片尺寸自适应尺寸
	void setStaticImageWidthAndHeight(float w = -1, float h = -1);

protected:
    void parseTextString() const;

	/*************************************************************************
		Implementation Data
	*************************************************************************/
	ColourRect	d_textCols;			//!< Colours used for rendering the text.
	Font*		d_font;				//!< Font used for rendering text.
    //! Parser used to produce a final RenderedString from the standard String.
    static BasicRenderedStringParser d_stringParser;
    //! RenderedString drawn by this item.
    mutable RenderedString  d_renderedString;
    //! boolean used to track when item state changes (and needs re-parse)
    mutable bool d_renderedStringValid;
	ListBoxTextItemHorFormat d_TextHorFormat; //文字对齐方式

	const Image* d_MouseOnBursh; //鼠标在上面的格子刷

	int64_t d_ConnectID;
	const Image* d_SelectBrushLeft;
	const Image* d_SelectBrushMiddle;
	const Image* d_SelectBrushRight;

	Image* d_StaticImage;
	float d_StaticImageWidth;
	float d_StaticImageHeight;
};

} // End of  CEGUI namespace section


#endif	// end of guard _CEGUIListboxTextItem_h_
