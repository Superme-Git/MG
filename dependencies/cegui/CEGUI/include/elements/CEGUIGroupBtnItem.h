/***********************************************************************
    filename:  CEGUIGroupBtnItem.h
    created:   2001.07.11
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
#ifndef _CEGUIGroupBtnItem_h_
#define _CEGUIGroupBtnItem_h_

#include "../CEGUIBase.h"
#include "../CEGUIString.h"
#include "../CEGUIColourRect.h"
#include "../CEGUIBasicRenderedStringParser.h"
#include "../CEGUICentredRenderedString.h"

#if defined(_MSC_VER)
#   pragma warning(push)
#   pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
/*!
 \brief
    Base class for tree items

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
class CEGUIEXPORT GroupBtnItem
{
public:
    typedef std::vector<GroupBtnItem*>  LBItemList;

    /*************************************************************************
        Constants
     *************************************************************************/
    //! Default text colour.
    static const colour DefaultTextColour;
    //! Default selection brush colour.
    static const colour DefaultSelectionColour;
	//! Default text colour.
	static const colour DefaultHoveringColour;

    /*************************************************************************
        Construction and Destruction
     *************************************************************************/
    /*!
     \brief
        base class constructor
     */
	GroupBtnItem(const String& text, uint item_id = 0, colour boardcol = 0xFF015885, void* item_data = 0);

    /*!
     \brief
        base class destructor
     */
    virtual ~GroupBtnItem(void);

    /*************************************************************************
        Accessors
     *************************************************************************/
    /*!
     \brief
         Return a pointer to the font being used by this TreeItem
        
         This method will try a number of places to find a font to be used.  If
         no font can be found, NULL is returned.
     
     \return
        Font to be used for rendering this item
     */
    Font* getFont(void) const;

    /*!
     \brief
        Return the current colours used for text rendering.
     
     \return
        ColourRect object describing the currently set colours
     */
    ColourRect getTextColours(void) const
    { return d_textCols; }

    /*************************************************************************
        Manipulator methods
     *************************************************************************/
    /*!
     \brief
        Set the font to be used by this TreeItem
     
     \param font
        Font to be used for rendering this item
     
     \return
        Nothing
     */
    void setFont(Font* font);

    /*!
     \brief
        Set the font to be used by this TreeItem
     
     \param font_name
        String object containing the name of the Font to be used for rendering
        this item
     
     \return
        Nothing
     */
    void setFont(const String& font_name);

    /*!
     \brief
        Set the colours used for text rendering.
     
     \param cols
        ColourRect object describing the colours to be used.
     
     \return
        Nothing.
     */
    void setTextColours(const ColourRect& cols)
    { d_textCols = cols; }

    void setBoardColor(colour boardColor)
    {d_boardCol = boardColor;}
    /*!
     \brief
        Set the colours used for text rendering.
     
     \param top_left_colour
        Colour (as ARGB value) to be applied to the top-left corner of each text
        glyph rendered.
     
     \param top_right_colour
        Colour (as ARGB value) to be applied to the top-right corner of each
        text glyph rendered.
     
     \param bottom_left_colour
        Colour (as ARGB value) to be applied to the bottom-left corner of each
        text glyph rendered.
     
     \param bottom_right_colour
        Colour (as ARGB value) to be applied to the bottom-right corner of each
        text glyph rendered.
     
     \return
        Nothing.
     */
    void setTextColours(colour top_left_colour, colour top_right_colour,
                        colour bottom_left_colour, colour bottom_right_colour);

    /*!
     \brief
        Set the colours used for text rendering.
     
     \param col
        colour value to be used when rendering.
     
     \return
        Nothing.
     */
    void setTextColours(colour col)
    { setTextColours(col, col, col, col); }

	
	void setHoverColours(const ColourRect& cols)
	{ d_hoverCols = cols; }

	void setHoverColours(colour top_left_colour, colour top_right_colour,
		colour bottom_left_colour, colour bottom_right_colour) { }

	void setHoverColours(colour col)
	{ setHoverColours(col, col, col, col); }

    /*!
     \brief
        return the text string set for this tree item.
     
        Note that even if the item does not render text, the text string can
        still be useful, since it is used for sorting tree items.
     
     \return
        String object containing the current text for the tree item.
     */
    const String& getText() const {return d_textLogical;}

    //! return text string with \e visual ordering of glyphs.
    const String& getTextVisual() const;

    /*!
    \brief
        Return the text string currently set to be used as the tooltip text for
        this item.

    \return
        String object containing the current tooltip text as sued by this item.
    */
    const String& getTooltipText(void) const
    { return d_tooltipText; }

    /*!
     \brief
        Return the current ID assigned to this tree item.
     
        Note that the system does not make use of this value, client code can
        assign any meaning it wishes to the ID.
     
     \return
        ID code currently assigned to this tree item
     */
    uint getID(void) const
    { return d_itemID; }

    /*!
     \brief
        Return the pointer to any client assigned user data attached to this
        tree item.
     
        Note that the system does not make use of this data, client code can
        assign any meaning it wishes to the attached data.
     
     \return
        Pointer to the currently assigned user data.
     */
    void* getUserData(void) const
    { return d_itemData; }

    /*!
     \brief
        return whether this item is selected.
     
     \return
        - true if the item is selected.
        - false if the item is not selected.
     */
    bool isSelected(void) const
    { return d_selected; }

    /*!
     \brief
        return whether this item is disabled.
     
     \return
        - true if the item is disabled.
        - false if the item is enabled.
     */
    bool isDisabled(void) const
    { return d_disabled; }

    /*!
     \brief
        Get the owner window for this TreeItem.
     
        The owner of a TreeItem is typically set by the tree widget when an
        item is added or inserted.
     
     \return
        Ponter to the window that is considered the owner of this TreeItem.
     */
    const Window* getOwnerWindow(void)
    { return d_owner; }

    /*!
     \brief
        Return the current colours used for selection highlighting.
     
     \return
        ColourRect object describing the currently set colours.
     */
    ColourRect getSelectionColours(void) const
    { return d_selectCols; }


    /*!
     \brief
        Return the current selection highlighting brush.
     
     \return
        Pointer to the Image object currently used for selection highlighting.
     */
    const Image* getSelectionImage(void) const
    { return d_SelectionImage; }


    /*************************************************************************
        Manipulators
     *************************************************************************/
    /*!
     \brief
        set the text string for this tree item.
     
        Note that even if the item does not render text, the text string can
        still be useful, since it is used for sorting tree items.
     
     \param text
        String object containing the text to set for the tree item.
     
     \return
        Nothing.
     */
    void setText(const String& text);

    /*!
    \brief
        Set the tooltip text to be used for this item.

    \param text
        String object holding the text to be used in the tooltip displayed for
        this item.

    \return
        Nothing.
    */
    void setTooltipText(const String& text)
    { d_tooltipText = text; }

    /*!
     \brief
        Set the ID assigned to this tree item.
     
        Note that the system does not make use of this value, client code can
        assign any meaning it wishes to the ID.
     
     \param item_id
        ID code to be assigned to this tree item
     
     \return
        Nothing.
     */
    void setID(uint item_id)
    { d_itemID = item_id; }

    /*!
     \brief
         Set the client assigned user data attached to this lis box item.
        
         Note that the system does not make use of this data, client code can
         assign any meaning it wishes to the attached data.
     
     \param item_data
        Pointer to the user data to attach to this tree item.
     
     \return
        Nothing.
     */
    void setUserData(void* item_data)
    { d_itemData = item_data; }

    /*!
     \brief
        Set the selected state for the item.
     
     \param setting
        - true if the item is selected.
        - false if the item is not selected.

     \return
        Nothing.
     */
    void setSelected(bool setting);//
    //{ d_selected = setting; }

    /*!
     \brief
        Set the disabled state for the item.
     
     \param setting
        - true if the item should be disabled.
        - false if the item should be enabled.
     
     \return
        Nothing.
     */
    void setDisabled(bool setting)
    { d_disabled = setting; }

    /*!
     \brief
         Set the owner window for this TreeItem.  This is called by the tree
         widget when an item is added or inserted.
     
     \param owner
        Ponter to the window that should be considered the owner of this
        TreeItem.
     
     \return
        Nothing
     */
    void setOwnerWindow(const Window* owner)
    { 
		d_owner = owner;
		if (owner)
			setFont(CEGUI::String("simhei-16"));
	}

	void setOwernItem(GroupBtnItem* owernitem)
	{
		d_ownerItem = owernitem;
		if (owernitem)
			setFont(CEGUI::String("simhei-14"));
	}

    /*!
     \brief
        Set the colours used for selection highlighting.
     
     \param cols
        ColourRect object describing the colours to be used.
     
     \return
        Nothing.
     */
    void setSelectionColours(const ColourRect& cols)
    { d_selectCols = cols; }


    /*!
     \brief
        Set the colours used for selection highlighting.
     
     \param top_left_colour
        Colour (as ARGB value) to be applied to the top-left corner of the
        selection area.
     
     \param top_right_colour
        Colour (as ARGB value) to be applied to the top-right corner of the
        selection area.
     
     \param bottom_left_colour
        Colour (as ARGB value) to be applied to the bottom-left corner of the
        selection area.
     
     \param bottom_right_colour
        Colour (as ARGB value) to be applied to the bottom-right corner of the
        selection area.
     
     \return
        Nothing.
     */
    void setSelectionColours(colour top_left_colour,
                             colour top_right_colour,
                             colour bottom_left_colour,
                             colour bottom_right_colour);

    /*!
     \brief
        Set the colours used for selection highlighting.
     
     \param col
        colour value to be used when rendering.
     
     \return
        Nothing.
     */
    void setSelectionColours(colour col)
    { setSelectionColours(col, col, col, col); }


    /*!
     \brief
        Set the selection highlighting brush image.
     
     \param image
        Pointer to the Image object to be used for selection highlighting.
     
     \return
        Nothing.
     */
    void setSelectionImage(const Image* image)
    { d_SelectionImage = image; }

    void setSelectionImage(const String& imageset, const String& image);


	void seNormalImage(const Image* image)
	{ d_NormalImage = image; }

	void seNormalImage(const String& imageset, const String& image);


	void setHoverImage(const Image* image)
	{ d_HoverImage = image; }

	void setHoverImage(const String& imageset, const String& image);


	void setOpenImage(const Image* image)
	{ d_OpenImage = image; }

	void setOpenImage(const String& imageset, const String& image);

	void setOrnamentImage(const Image* image)
	{ d_OrnamentImage = image; }

	void setOrnamentImage(const String& imageset, const String& image);

	void setOrnamentPosition(float x, float y);

    bool getIsOpen(void)
    { return d_isOpen; }

    void toggleIsOpen(void)
    { d_isOpen = !d_isOpen; }

	GroupBtnItem *getParentItem() { return d_ownerItem;}
    GroupBtnItem *getTreeItemFromIndex(size_t itemIndex);

    size_t getItemCount(void) const
    { return d_listItems.size(); }

    LBItemList &getItemList(void)
    { return d_listItems; }

    void addItem(GroupBtnItem* item);
	GroupBtnItem* addItem(const String& text, uint item_id = 0, colour boardcol = 0xFF015885, void* item_data = 0);
    void removeItem(const GroupBtnItem* item);

	void RemoveChildrenItems();

    /*************************************************************************
        Abstract portion of interface
     *************************************************************************/
    /*!
     \brief
        Return the rendered pixel size of this tree item.
     
     \return
        Size object describing the size of the tree item in pixels.
     */
    virtual Size getPixelSize(void) const;
	Size getItemSize() const;

    /*!
     \brief
        Draw the tree item in its current state
     
     \param position
        Vector2 object describing the upper-left corner of area that should be
        rendered in to for the draw operation.
     
     \param alpha
        Alpha value to be used when rendering the item (between 0.0f and 1.0f).
     
     \param clipper
        Rect object describing the clipping rectangle for the draw operation.
     
     \return
        Nothing.
     */
    virtual void draw(GeometryBuffer* buffer, const Rect& targetRect, float alpha, const Rect* clipper) const;

	virtual void drawText(GeometryBuffer* buffer, const Rect& targetRect, const ColourRect& color, const Rect* clipper) const;

	bool isMouseOn() { return d_MouseOn; }
	void SetMouseOnState(bool state) { d_MouseOn=state; }

protected:
    /*************************************************************************
        Implementation methods
     *************************************************************************/
    /*!
     \brief
        Return a ColourRect object describing the colours in \a cols after
        having their alpha component modulated by the value \a alpha.
     */
    ColourRect getModulateAlphaColourRect(const ColourRect& cols,
                                          float alpha) const;

    /*!
     \brief
         Return a colour value describing the colour specified by \a col after
         having its alpha component modulated by the value \a alpha.
     */
    colour calculateModulatedAlphaColour(colour col, float alpha) const;

    //! parse the text visual string into a RenderString representation.
    void parseTextString() const;

    /*************************************************************************
        Implementation Data
     *************************************************************************/
    //! Text for this tree item.  If not rendered, still used for sorting.
    String               d_textLogical;            //!< text rendered by this component.
    //! pointer to bidirection support object
    BiDiVisualMapping* d_bidiVisualMapping;
    //! whether bidi visual mapping has been updated since last text change.
    mutable bool d_bidiDataValid;
    //! Text for the individual tooltip of this item.
    String d_tooltipText;
    //! ID code assigned by client code.
    uint d_itemID;
    //! Pointer to some client code data.
    void* d_itemData;
    //! true if item is selected.  false if item is not selected.
    bool d_selected;
    //! true if item is disabled.  false if item is not disabled.
    bool d_disabled;
   
    //! Pointer to the window that owns this item.
    const Window* d_owner;
	//! Pointer to the GroupBtnItem that owns this item.
	GroupBtnItem* d_ownerItem;

    //! Colours used for selection highlighting.
    ColourRect d_selectCols;
    //! Colours used for rendering the text.
    ColourRect d_textCols;
	//! Colours used for hovering.
	ColourRect d_hoverCols;
	colour	   d_boardCol;//Ãè±ßÑÕÉ«
    //! Font used for rendering text.
    Font* d_font;
    //! list of items in this item's tree branch.
    LBItemList d_listItems;
    //! true if the this item's tree branch is opened.
    bool d_isOpen;
    //! Parser used to produce a final RenderedString from the standard String.
    static BasicRenderedStringParser d_stringParser;
    //! RenderedString drawn by this item.
	mutable CentredRenderedString* d_formattedRenderedString;
   //! RenderedString drawn by this item.
	mutable RenderedString  d_renderedString;
    //! boolean used to track when item state changes (and needs re-parse)
    mutable bool d_renderedStringValid;
	bool d_MouseOn;
	
	
	const Image* d_NormalImage;
	const Image* d_HoverImage;
	const Image* d_SelectionImage;
	const Image* d_OpenImage;

	const Image* d_OrnamentImage; //some small sign image. by liugeng
	Point d_OrnamentPosition;
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#   pragma warning(pop)
#endif

#endif   // end of guard _CEGUITreeItem_h_
