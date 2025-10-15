/***********************************************************************
    filename:  CEGUISpecialTreeItem.h
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
#ifndef _CEGUISpecialTreeItem_h_
#define _CEGUISpecialTreeItem_h_

#include "../CEGUIBase.h"
#include "../CEGUIString.h"
#include "../CEGUIColourRect.h"
#include "../CEGUIBasicRenderedStringParser.h"
#include "CEGUIRichEditboxComponent.h"

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
class CEGUIEXPORT SpecialTreeItem
{
public:
    typedef std::vector<RichEditboxComponent*>  ChildrenCompentList;
    /*************************************************************************
        Constants
     *************************************************************************/
    //! Default text colour.
    static const colour DefaultTextColour;
	static const colour DoneTextColour; //已完成任务对应的颜色
	static const colour FailTextColour;//任务失败对应的颜色

	static const int FailState;
	static const int DoneState;
	static const int UnDoneState;
    /*************************************************************************
        Construction and Destruction
     *************************************************************************/
    /*!
     \brief
        base class constructor
     */
    SpecialTreeItem(const String& text, uint item_id = 0, const int64_t& item_time = 0,Font* font= 0,void* item_data = 0);

    /*!
     \brief
        base class destructor
     */
    virtual ~SpecialTreeItem(void);

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
        return whether this item will be automatically deleted when it is
        removed from the tree or when the the tree it is attached to is
        destroyed.
     
     \return
         - true if the item object will be deleted by the system when it is
           removed from the tree, or when the tree it is attached to is
           destroyed.
        - false if client code must destroy the item after it is removed from
          the tree.
     */
    bool isAutoDeleted(void) const
    { return d_autoDelete; }

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
         Set whether this item will be automatically deleted when it is removed
         from the tree, or when the tree it is attached to is destroyed.
     
     \param setting
         - true if the item object should be deleted by the system when the it
           is removed from the tree, or when the tree it is attached to is
           destroyed.
        - false if client code will destroy the item after it is removed from
          the tree.
     
     \return
        Nothing.
     */

	void setTime(const int64_t& time); //设置时间，用来排序
	//{ d_state = state;}

	void updateTextColour();//根据状态设置文字颜色

    void setAutoDeleted(bool setting)
    { d_autoDelete = setting; }

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
    { d_owner = owner; }

    /*!
     \brief
        Tell the treeItem where its button is located.
        Calculated and set in Tree.cpp.
     
     \param buttonOffset
        Location of the button in screenspace.
     */
    void setButtonLocation(Rect &buttonOffset)
    { d_buttonLocation = buttonOffset; }

    Rect &getButtonLocation(void)
    { return d_buttonLocation; }

    bool getIsOpen(void)
    { return d_isOpen; }

    void toggleIsOpen(void)
    { d_isOpen = !d_isOpen; }

	void setInfoBtnLocation(Rect &buttonOffset)
	{ d_InfoBtnLocation = buttonOffset; }

	Rect &getInfoBtnLocation(void)
	{ return d_InfoBtnLocation; }

	bool getIsOpenInfo(void)
	{ return d_isOpenInfo; }

	void toggleIsOpenInfo(void)
	{ d_isOpenInfo = !d_isOpenInfo; }

    RichEditboxComponent *getCompentFromIndex(size_t compentIndex);

    size_t getChildrenCompentsCount(void) const
    { return d_ChildrenCompents.size(); }

    ChildrenCompentList &getChildrenCompentList(void)
    { return d_ChildrenCompents; }

    void addChildCompent(RichEditboxComponent* compent);

    void setIcon(const Image &theIcon)
    { d_iconImage = (Image *) & theIcon; }

	void clearIcon(){ d_iconImage = 0;}

	void ParseChildrenCompentText(const String& ParseText); //解析子节点的组件


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

    /*************************************************************************
        Operators
     *************************************************************************/
    /*!
     \brief
        Less-than operator, compares item texts.
     */
    virtual bool operator<(const SpecialTreeItem& rhs) const
    { return d_time < rhs.d_time; }

    /*!
     \brief
        Greater-than operator, compares item texts.
     */
    virtual bool operator>(const SpecialTreeItem& rhs) const
    { return d_time > rhs.d_time; }

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
    //! ID code assigned by client code.
    uint d_itemID;
    //! Pointer to some client code data.
    void* d_itemData;
    //! true if the system will destroy this item, false if client code will.
    bool d_autoDelete;
    //! Location of the 'expand' button for the item.
    Rect d_buttonLocation;
	//! Location of the 'info' button for the item.
	Rect d_InfoBtnLocation;

    //! Pointer to the window that owns this item.
    const Window* d_owner;
    //! Colours used for rendering the text.
    ColourRect d_textCols;
    //! Font used for rendering text.
    Font* d_font;
    //! Image for the icon to be displayed with this TreeItem.
    Image* d_iconImage;
    //! list of items in this item's tree branch.
    ChildrenCompentList d_ChildrenCompents;
    //! true if the this item's tree branch is opened.
    bool d_isOpen;

	bool d_isOpenInfo;//是否打开任务详细界面
    //! Parser used to produce a final RenderedString from the standard String.
    static BasicRenderedStringParser d_stringParser;
    //! RenderedString drawn by this item.
    mutable RenderedString  d_renderedString;
    //! boolean used to track when item state changes (and needs re-parse)
    mutable bool d_renderedStringValid;

	int64_t d_time; //状态
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#   pragma warning(pop)
#endif

#endif   // end of guard _CEGUITreeItem_h_
