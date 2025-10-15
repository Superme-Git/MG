/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditbox_h_
#define _CEGUIRichEditbox_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "../CEGUIFont.h"
#include "CEGUIRichEditboxProperties.h"
#include "CEGUIRichEditboxComponent.h"
#include "../../src/XMLParserModules/TinyXMLParser/ceguitinyxml/tinyxml.h"
#include "elements/CEGUIRichEditboxLinkTextComponent.h"
#include "elements/CEGUIRichEditboxGoToComponent.h"
#include "elements/CEGUIRichEditboxEmotionComponent.h"
#include "elements/CEGUIRichEditboxTipsLinkComponent.h"
#include "elements/CEGUIRichEditboxNameLinkComponent.h"
#include "elements/CEGUIRichEditboxFamilyRecruitLinkComponent.h"
#include "elements/CEGUIRichEditboxImageComponent.h"
#include "elements/CEGUIRichEditboxButtonImageComponent.h"
#include "elements/CEGUIRichEditboxJoinTeamLinkComponent.h"

#include <vector>

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
 struct TipsLinkInf
 {
	 TipsLinkInf(int64_t _roleID,int _type,const String& _key, int64_t _shopID,int _counterID):
     roleID(_roleID),
	 type(_type),
	 key(_key),
	 shopID(_shopID),
	 counterID(_counterID)
	 {
		 
	 }
	 ~TipsLinkInf()
	 {

	 }
	 
	 int64_t roleID;
	 int type;
	 String key;
	 int64_t shopID;
	 int counterID;
	 int64_t loseEffTime;
 };

 struct TextCpnInf //\uFFE5????\u00F7??o?\u2013\u2248?\uFFE0
 {
	  TextCpnInf(size_t _startIdx,size_t _endIdx):
       startIdx(_startIdx),
	   endIdx(_endIdx)
	  {

	  }
	 ~TextCpnInf()
	 {

	 }
     size_t startIdx;
     size_t endIdx;
 };
class CEGUIEXPORT RichEditboxWindowRenderer : public WindowRenderer
{
public:
    /*!
    \brief
        Constructor
    */
	RichEditboxWindowRenderer(){}
    RichEditboxWindowRenderer(const String& name);

    /*!
    \brief
        Return a Rect object describing, in un-clipped pixels, the window relative area
        that the text should be rendered in to.

    \return
        Rect object describing the area of the Window to be used for rendering text.
    */
    virtual Rect getTextRenderArea(void) const = 0;

	virtual void FixStartDrawLineIdx() =0;

	virtual float GetLastTestOffest() = 0; //

	virtual void clone(const WindowRenderer* templateRenderer){ WindowRenderer::clone(templateRenderer); }

protected:
    // base class overrides
    void onLookNFeelAssigned();
};

/*!
\brief
	Base class for the multi-line edit box widget.
*/
enum eTextAcceptMode  //\u2211\uFFE0\u00E0???\u03A9?o?
{ 
	eTextAcceptMode_Enter,           //enter
	eTextAcceptMode_CtrlEnter,  //ctrl+enter
	eTextAcceptMode_OnlyEnter,  //\u00F7a\u201C?\u221E\uFFE5?\u00E0enter??\u2211\uFFE0\u00E0?

	eTextAcceptMode_None
};
class CEGUIEXPORT RichEditbox : public Window
{
public:
	static const String EventNamespace;				//!< Namespace for global events
    static const String WidgetTypeName;             //!< Window factory name

	/*************************************************************************
		Constants
	*************************************************************************/
	// event names
	static const String EventReadOnlyModeChanged;			//!< The read-only mode for the edit box has been changed.
	static const String EventWordWrapModeChanged;			//!< The word wrap mode of the text box has been changed.
	static const String EventMaximumTextLengthChanged;	//!< The maximum allowable string length has been changed.
	static const String EventCaratMoved;					//!< The text carat (insert point) has changed.
	static const String EventTextSelectionChanged;		//!< The current text selection has changed.
	static const String EventEditboxFull;					//!< The number of characters in the edit box has reached the current maximum.
	static const String EventVertScrollbarModeChanged;	//!< Event triggered when the vertical scroll bar 'force' setting changes.
	static const String EventHorzScrollbarModeChanged;	//!< Event triggered when the horizontal scroll bar 'force' setting changes.

	static const String EventUpKeyDown;	//?\u00FA\u2026?o??\u2211\u221E\uFFE5????o?
	static const String EventDownKeyDown;	//?\u00FA\u2026?o??\u2211\u221E\uFFE5????o?

    /*************************************************************************
        Child Widget name suffix constants
    *************************************************************************/
    static const String VertScrollbarNameSuffix;   //!< Widget name suffix for the vertical scrollbar component.
    static const String HorzScrollbarNameSuffix;   //!< Widget name suffix for the horizontal scrollbar component.

    /*************************************************************************
        Implementation struct
    *************************************************************************/
    /*!
    \brief
        struct used to store information about a formatted line within the
        paragraph.
    */
    struct LineInfo
    {
        size_t  d_startComponentIdx;     //\u220F\u221A\u2013\u2013???o\u03BC?component index
        size_t  d_ComponentCount;       //\u220F\u221A\u2013\u2013\u03BC?Component\u220F???
		float   d_Width;                //\u220F\u221A\u2013\u2013\u03BC??\u00EC??
		float   d_Height;               //\u220F\u221A\u2013\u2013\u03BC?\u220F???
		float   d_YOffset;              //\u220F\u221A\u2013\u2013????\u03BC?\u201Ca\u2013\u2013\u03BC?\u220F???
		LineInfo(int startIdx=0,int count=0,float width=0.0f,float height=0.0f,float yoffset=0.0f):
        d_startComponentIdx(startIdx),
		d_ComponentCount(count),
		d_Width(width),
		d_Height(height),
		d_YOffset(yoffset)
		{
			
		}
	};
    typedef std::vector<LineInfo>   LineList;   //!< Type for collection of LineInfos.
	typedef std::vector<RichEditboxComponent*>   ComponentList;   //!< Type for collection of LineInfos.
	typedef std::vector<RichEditboxComponent*>::iterator   ComponentListIt;   //!< Type for collection of LineInfos.

	struct CaratPos
	{
		size_t d_ComponentIndex; //\u03C0?\u00B1\u00ED\u00E0?\u2018?\u03BC??\u201D??o?index
		size_t d_ComponentOffset; //\u03C0?\u00B1\u00ED\u00E0?\u2018?\u03BC??\u201D??o???\u03BC??\u2032\u201C?

		CaratPos(size_t index=0,size_t offset=0)
		{
			d_ComponentIndex=index;
			d_ComponentOffset=offset;
		}
		bool operator==(const CaratPos& pos) const
		{
			if (d_ComponentIndex==pos.d_ComponentIndex
				&& d_ComponentOffset==pos.d_ComponentOffset)
			{
				return true;
			}
			return false;
		}

		bool operator<(const CaratPos& pos) const
		{
			if (d_ComponentIndex<pos.d_ComponentIndex)
			{
				return true;
			}else if( d_ComponentIndex==pos.d_ComponentIndex )
			{
				if (d_ComponentOffset<pos.d_ComponentOffset)
				{
					return true;
				}
			}
			return false;
		}

		bool operator>(const CaratPos& pos) const
		{
			if (d_ComponentIndex>pos.d_ComponentIndex)
			{
				return true;
			}else if( d_ComponentIndex==pos.d_ComponentIndex )
			{
				if (d_ComponentOffset>pos.d_ComponentOffset)
				{
					return true;
				}
			}
			return false;
		}

		bool operator!=(const CaratPos& pos) const
		{
			if (d_ComponentIndex!=pos.d_ComponentIndex
				|| d_ComponentOffset!=pos.d_ComponentOffset)
			{
				return true;
			}
			return false;
		}

		void Reset()
		{
			d_ComponentIndex=0;
			d_ComponentOffset=0;
		}
	};

	/*************************************************************************
		Accessor Functions
	*************************************************************************/
	/*!
	\brief
		return true if the edit box has input focus.

	\return
		- true if the edit box has keyboard input focus.
		- false if the edit box does not have keyboard input focus.
	*/
	bool	hasInputFocus(void) const;


	/*!
	\brief
		return true if the edit box is read-only.

	\return
		- true if the edit box is read only and can't be edited by the user.
		- false if the edit box is not read only and may be edited by the user.
	*/
	bool	isReadOnly(void) const		{return d_readOnly;}


	/*!
	\brief
		return the current position of the carat.

	\return
		Index of the insert carat relative to the start of the text.
	*/
	CaratPos	getCaratPos(void) const		{return d_caratPos;}


	/*!
	\brief
		return the current selection start point.

	\return
		Index of the selection start point relative to the start of the text.  If no selection is defined this function returns
		the position of the carat.
	*/
	CaratPos	getSelectionStartPos(void) const;


	/*!
	\brief
		return the current selection end point.

	\return
		Index of the selection end point relative to the start of the text.  If no selection is defined this function returns
		the position of the carat.
	*/
	CaratPos	getSelectionEndPos(void) const;

	
	/*!
	\brief
		return the length of the current selection (in code points / characters).

	\return
		Number of code points (or characters) contained within the currently defined selection.
	*/
	size_t	getSelectionLength(void);


	/*!
	\brief
		return the maximum text length set for this edit box.

	\return
		The maximum number of code points (characters) that can be entered into this edit box.
	*/
	size_t	getMaxTextLength(void) const		{return d_maxTextLen;}


	/*!
	\brief
		Return whether the text in the edit box will be word-wrapped.

	\return
		- true if the text will be word-wrapped at the edges of the widget frame.
		- false if text will not be word-wrapped (a scroll bar will be used to access long text lines).
	*/
	bool	isWordWrapped(void) const;


    /*!
    \brief
        Return a pointer to the vertical scrollbar component widget for this
        MultiLineEditbox.

    \return
        Pointer to a Scrollbar object.

    \exception UnknownObjectException
        Thrown if the vertical Scrollbar component does not exist.
    */
    Scrollbar* getVertScrollbar() const;

    /*!
	\brief
		Return whether the vertical scroll bar is always shown.

	\return
		- true if the scroll bar will always be shown even if it is not required.
		- false if the scroll bar will only be shown when it is required.
	*/
	bool	isVertScrollbarAlwaysShown(void) const;

    /*!
    \brief
        Return a pointer to the horizontal scrollbar component widget for this
        MultiLineEditbox.

    \return
        Pointer to a Scrollbar object.

    \exception UnknownObjectException
        Thrown if the horizontal Scrollbar component does not exist.
    */
    Scrollbar* getHorzScrollbar() const;


    /*!
    \brief
        Return a Rect object describing, in un-clipped pixels, the window relative area
        that the text should be rendered in to.

    \return
        Rect object describing the area of the Window to be used for rendering text.
    */
    Rect    getTextRenderArea(void) const;

    // get d_lines
    const LineList& getFormattedLines(void) const   {return d_lines;}

	ComponentList& getFormattedComponents(void)    {return d_FormatedComponents;}

    /*!
    \brief
        Return the line number a given index falls on with the current formatting.  Will return last line
        if index is out of range.
    */
    size_t  getLineNumberFromPos(CaratPos pos) const;

	/*************************************************************************
		Manipulators
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


	/*!
	\brief
		Specify whether the edit box is read-only.

	\param setting
		- true if the edit box is read only and can't be edited by the user.
		- false if the edit box is not read only and may be edited by the user.

	\return
		Nothing.
	*/
	void	setReadOnly(bool setting);


	/*!
	\brief
		Set the current position of the carat.

	\param carat_pos
		New index for the insert carat relative to the start of the text.  If the value specified is greater than the
		number of characters in the edit box, the carat is positioned at the end of the text.

	\return
		Nothing.
	*/
	void	setCaratPos(CaratPos carat_pos);
	void setCaratPos(size_t index,size_t offset){ setCaratPos(CaratPos(index,offset)); }


	/*!
	\brief
		Define the current selection for the edit box

	\param start_pos
		Index of the starting point for the selection.  If this value is greater than the number of characters in the edit box, the
		selection start will be set to the end of the text.

	\param end_pos
		Index of the ending point for the selection.  If this value is greater than the number of characters in the edit box, the
		selection start will be set to the end of the text.

	\return
		Nothing.
	*/
	void	setSelection(CaratPos start_pos, CaratPos end_pos);
	

	/*!
	\brief
		set the maximum text length for this edit box.

	\param max_len
		The maximum number of code points (characters) that can be entered into this Editbox.

	\return
		Nothing.
	*/
	void	setMaxTextLength(size_t max_len);


	/*!
	\brief
		Scroll the view so that the current carat position is visible.
	*/
	void	ensureCaratIsVisible(void);


	/*!
	\brief
		Set whether the text will be word wrapped or not.

	\param setting
		- true if the text should word-wrap at the edges of the text box.
		- false if the text should not wrap, but a scroll bar should be used.

	\return
		Nothing.
	*/
	void	setWordWrapping(bool setting);

    /*!
	\brief
		Set whether the vertical scroll bar should always be shown.

	\param setting
		true if the vertical scroll bar should be shown even when it is not required.  false if the vertical
		scroll bar should only be shown when it is required.

	\return
		Nothing.
	*/
	void	setShowVertScrollbar(bool setting);

    // selection brush image property support
    void setSelectionBrushImage(const Image* image);
    const Image* getSelectionBrushImage() const;

	Size    GetLineSize(size_t index);
	Point  GetCaratDrawPos();

	void  AddComponent(RichEditboxComponent* pComponent);

	void  AddRefreshComponent(RichEditboxComponent* pComponent);   //\u2018?o\u201D\u03BC\u03A9???\u00B1\u03BC??\u201D??o??\u201D?\u2013\u00F7\u2013\uFFE1\u00A8\u03BC?refresh?\u00B1\u2264\u2248a\u00B7format

	const ColourRect& GetColourRect() { return d_ColourRect; }
	String GetColourString();
	void SetColourRect(ColourRect Color);
	void SetColourRect(colour color) { SetColourRect(ColourRect(color)); }

	eTextAcceptMode GetTextAcceptMode() { return d_TextAcceptMode; }
	void SetTextAcceptMode(eTextAcceptMode mode) { d_TextAcceptMode=mode; }
	static const String EventTextAccepted;

	const String GetParseText() { return d_ParseText; }
	
	String GetPureText();

	void ResetTextColour(ColourRect color);

	//String GetShiedTextInf(std::vector<TextCpnInf>& vecTextCpnInf);  //a\u00F2\u03BC\u221A\u201D\u221A?\uFFE5o?\u2264???\u00B1??\u00F7\u03BC???\u00B1?


	String GenerateParseText(bool bIsGenerateRoot=true); //\u220F???formatcomponent\u2026\u02D9\u2265\u2026\u03A9???\u03BC?xml??o?,bIsGenerateRoot????\u2211\u00F2\u2026\u02D9\u2265\u2026xml\u03BC?\u220F?\u2018?\u00E0?

	void Clear();

	//?\u00ECo\u201D\u201C?\u03A9???\u03BC???\u00B1?
	//bCheckShied ??\u2211\u00F2\u201C??\u00EAaa??\u00B1??\u00F7
	void AppendParseText(const String& ParseText,bool bCheckShied=false);

	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		Constructor for the MultiLineEditbox base class.
	*/
	RichEditbox(const String& type, const String& name);


	/*!
	\brief
		Destructor for the MultiLineEditbox base class.
	*/
	virtual ~RichEditbox(void);


public:
	RichEditbox(const String& type);
	const RichEditbox& operator=(const RichEditbox& t);
	virtual Window* clone(Window* wnd);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }


protected:
	/*************************************************************************
		Implementation Methods (abstract)
	*************************************************************************/
	/*!
	\brief
		Return a Rect object describing, in un-clipped pixels, the window relative area
		that the text should be rendered in to.

	\return
		Rect object describing the area of the Window to be used for rendering text.
	*/
	//virtual	Rect	getTextRenderArea_impl(void) const		= 0;


	/*************************************************************************
		Implementation Methods
	*************************************************************************/
	/*!
	\brief
		Format the text into lines as needed by the current formatting options.
	*/
	void	formatText(void);


	/*!
	\brief
		Return the length of the next token in String \a text starting at index \a start_idx.

	\note
		Any single whitespace character is one token, any group of other characters is a token.
	
	\return
		The code point length of the token.
	*/
	size_t	getNextTokenLength(const String& text, size_t start_idx) const;


    /*!
	\brief
		display required integrated scroll bars according to current state of the edit box and update their values.
	*/
	void	configureScrollbars(void);


	/*!
	\brief
		Return the text code point index that is rendered closest to screen position \a pt.

	\param pt
		Point object describing a position on the screen in pixels.

	\return
		Code point index into the text that is rendered closest to screen position \a pt.
	*/
	CaratPos	getComponentInfFromPosition(const Point& pt) ;  //\uFFE5\u201D?\u2265\u03BC?a\u00F2\u03BC\u221A\u220F\u221A\u03BC??\u2018\u201D?\u03BC?componet indexo\u221Eoffset


	/*!
	\brief
		Clear the current selection setting
	*/
	void	clearSelection(void);


	/*!
	\brief
		Erase the currently selected text.

	\param modify_text
		when true, the actual text will be modified.  When false, everything is done except erasing the characters.
	*/
	void	eraseSelectedText(bool modify_text = true);


	/*!
	\brief
		Processing for backspace key
	*/
	void	handleBackspace(void);


	/*!
	\brief
		Processing for Delete key
	*/
	void	handleDelete(void);


	/*!
	\brief
		Processing to move carat one character left
	*/
	void	handleCharLeft(uint sysKeys);


	/*!
	\brief
		Processing to move carat one word left
	*/
	void	handleWordLeft(uint sysKeys);


	/*!
	\brief
		Processing to move carat one character right
	*/
	void	handleCharRight(uint sysKeys);


	/*!
	\brief
		Processing to move carat one word right
	*/
	void	handleWordRight(uint sysKeys);


	/*!
	\brief
		Processing to move carat to the start of the text.
	*/
	void	handleDocHome(uint sysKeys);


	/*!
	\brief
		Processing to move carat to the end of the text
	*/
	void	handleDocEnd(uint sysKeys);


	/*!
	\brief
		Processing to move carat to the start of the current line.
	*/
	void	handleLineHome(uint sysKeys);


	/*!
	\brief
		Processing to move carat to the end of the current line
	*/
	void	handleLineEnd(uint sysKeys);


	/*!
	\brief
		Processing to move carat up a line.
	*/
	void	handleLineUp(uint sysKeys);


	/*!
	\brief
		Processing to move carat down a line.
	*/
	void	handleLineDown(uint sysKeys);


	/*!
	\brief
		Processing to insert a new line / paragraph.
	*/
	void	handleNewLine(uint sysKeys);


    /*!
    \brief
        Processing to move caret one page up
    */
    void    handlePageUp(uint sysKeys);


    /*!
    \brief
        Processing to move caret one page down
    */
    void    handlePageDown(uint sysKeys);


	/*!
	\brief
		Return whether this window was inherited from the given class name at some point in the inheritance hierarchy.

	\param class_name
		The class name that is to be checked.

	\return
		true if this window was inherited from \a class_name. false if not.
	*/
	virtual bool	testClassName_impl(const String& class_name) const
	{
		if ((class_name=="RichEditbox") ||
			(class_name=="RichEditbox"))
		{
			return true;
		}

		return Window::testClassName_impl(class_name);
	}

	/*!
	\brief
	   Internal handler that is triggered when the user interacts with the scrollbars.
    */
    bool handle_scrollChange(const EventArgs& args);

    // validate window renderer
    virtual bool validateWindowRenderer(const String& name) const
    {
        return (name == EventNamespace);
    }

	/*************************************************************************
		New event handlers
	*************************************************************************/
	/*!
	\brief
		Handler called when the read-only state of the edit box changes
	*/
	void	onReadOnlyChanged(WindowEventArgs& e);


	/*!
	\brief
		Handler called when the word wrap mode for the the edit box changes
	*/
	void	onWordWrapModeChanged(WindowEventArgs& e);


	/*!
	\brief
		Handler called when the maximum text length for the edit box changes
	*/
	void	onMaximumTextLengthChanged(WindowEventArgs& e);


	/*!
	\brief
		Handler called when the carat moves.
	*/
	void	onCaratMoved(WindowEventArgs& e);


	/*!
	\brief
		Handler called when the text selection changes
	*/
	void	onTextSelectionChanged(WindowEventArgs& e);


	/*!
	\brief
		Handler called when the edit box is full
	*/
	void	onEditboxFullEvent(WindowEventArgs& e);


	/*!
	\brief
		Handler called when the 'always show' setting for the vertical scroll bar changes.
	*/
	void	onVertScrollbarModeChanged(WindowEventArgs& e);


	/*!
	\brief
		Handler called when 'always show' setting for the horizontal scroll bar changes.
	*/
	void	onHorzScrollbarModeChanged(WindowEventArgs& e);

	void    processElement(CEGUITinyXML::TiXmlElement *pElement);
	void    elementStart(const String &element, const XMLAttributes &attributes);
	void    elementEnd(const String &element);



	void	formatTextImpl(const String &textFormat);

	void    format();  //\u220F???d_components\u220F\u00F2?\u03A9a?

	void    ReadOnlyFormat(); //\u2264a?\u2026\u00B1?o\u2260?\uFFE1?\u03A9?\u00B1\u201D\u221A\u03BC?format,\u2018???\u220F\u00F2?\u03A9a?

	void    EditModeFormat(bool bConfigScrollBar=true);  //\u00B1?o\u2260?\uFFE1?\u03A9?\u00B1\u201D\u221A\u03BC?format,o\uFFE5?\u00B1\u220F\u00F2?\u03A9a?

	void    DestroyAllComponent();

	bool InsertComponentInCarat(RichEditboxComponent* pComponent, bool checkMaxLength = true);

	/*************************************************************************
		Overridden event handlers
	*************************************************************************/
	virtual	void	onMouseButtonDown(MouseEventArgs& e);
	virtual void	onMouseButtonUp(MouseEventArgs& e);
	virtual	void	onMouseDoubleClicked(MouseEventArgs& e);
	virtual	void	onMouseTripleClicked(MouseEventArgs& e);
	virtual void	onMouseMove(MouseEventArgs& e);
	virtual void    onMouseLeaves(MouseEventArgs& e);
	virtual void	onCaptureLost(WindowEventArgs& e);
	virtual void	onCharacter(KeyEventArgs& e);
	virtual void	onKeyDown(KeyEventArgs& e);
	virtual void	onKeyUp(KeyEventArgs& e);

	virtual void	onTextChanged(WindowEventArgs& e);
	virtual void	onSized(WindowEventArgs& e);
	virtual	void	onMouseWheel(MouseEventArgs& e);
    virtual	void	onMouseSlide(MouseEventArgs& e);
    virtual	bool	onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer);

	virtual void onTextAcceptedEvent(WindowEventArgs& e);

	bool handleThumbTrackStarted(const EventArgs& e);
	bool handleThumbTrackEnd(const EventArgs& e);

	void HandlePasteFromClipBord(); //\u221E\uFFE5ctrl+V?\u00B1\uFFE5\u201Do\u00F9?\u2013\u221E?\u220F\uFFE5\u00F7?
	void HandleCopyToClipBord();  //\u221E\uFFE5ctrl+C?\u00B1\u03A9???\u00E0?\u2014\u00B0??o?\u03BC?\uFFE5???\u00B1?\u00F7?o\u00F9?\u2013\u221E?
	

	//virtual void update(float timeElapsed){};


	/*************************************************************************
		Implementation data
	*************************************************************************/
	bool	d_readOnly;			//!< true if the edit box is in read-only mode
	size_t	d_maxTextLen;		//!< Maximum number of characters for this Editbox.
	size_t  d_CurTextLen;     //\u03BC\u00B1?\u221E?\u00F7\u2211?\u2265\u00A7??
	CaratPos	d_caratPos;			//!< Position of the carat / insert-point.
	CaratPos	d_selectionStart;	//!< Start of selection area.
	CaratPos	d_selectionEnd;		//!< End of selection area.
	bool	d_dragging;			//!< true when a selection is being dragged.
	CaratPos	d_dragAnchorPos;	//!< Selection index for drag selection anchor point.

	static String d_lineBreakChars;	//!< Holds what we consider to be line break characters.
	bool		d_wordWrap;			//!< true when formatting uses word-wrapping.
	LineList	d_lines;			//!< Holds the lines for the current formatting.
	ComponentList d_TempComponents;     //?\u201D??o??\u2013\u00B1\u00EC
	ComponentList d_FormatedComponents;     //\u201C\u2014?\u2260?\u2260\u03C0?\u220F\u00F2?\u03A9a???o??\u2013\u00B1\u00EC

	ComponentList d_WaitToAddComponents;  //\uFFE5?\u2018?o\u201D\u03BC??\u201D??o?

	ComponentList d_RefreshComponents;   //???\u00B1??o?,refresh\u2264\u2248\u00E0\uFFE0\u2013?

	float		d_widestExtent;		//!< Holds the extent of the widest line as calculated in the last formatting pass.
	float       d_TotalHeight;

	String      d_ParseText;    //\u201D\u221A?\uFFE5\u03A9???\u2026\u02D9\u2265\u2026??o?\u03BC???\u00B1?,\u2264a\uFFE5\u02C9/content?\u2211,\u03A9????\u00B1?\u2018??o\u201D

	// component widget settings
	bool	d_forceVertScroll;		//!< true if vertical scrollbar should always be displayed
	bool	d_forceHorzScroll;		//!< true if horizontal scrollbar should always be displayed

	Point	downPosition;  // downPosition// 按下点

	// images
	const Image*	d_selectionBrush;	//!< Image to use as the selection brush (should be set by derived class).

	LineInfo d_CurrentLine;

	float GetLineVertOffset(size_t index); //?\u00B0\u03BC\u221A\u220F\u221A\u2013\u2013\u03BC??\u2018\u2019?\u220F?\u03BC???\u2026?\u03A9?\u03BC??\u2032\u201C?\u220F???
	size_t GetLineIndexByComponent(size_t index); //a\u00F2\u03BC\u221Acomponent\u00E0?\u2018?\u03BC?\u2013\u2013,\u2211\u03BCa?-1 ????\u2264a\uFFE5\u00EA\u2018?
	//String GenerateParseText(bool bIsGenerateRoot=true); //\u220F???formatcomponent\u2026\u02D9\u2265\u2026\u03A9???\u03BC?xml??o?,bIsGenerateRoot????\u2211\u00F2\u2026\u02D9\u2265\u2026xml\u03BC?\u220F?\u2018?\u00E0?
	void	onParseTextChanged(WindowEventArgs& e);

	ColourRect d_ColourRect;

	float d_TextBottomEdge;   //???\u00F7?\u02C9\u201D\u00FA?\u2032??\u03BC?\u2264?\u03BC?\u220F???

	bool d_PassMouseClick;  //??\u2211\u00F2\u2013?\u201C?\u03C0??\u00E0\u2211??\u2026\u03BC?a?\u221E\uFFE5\u2248\u00A7\u03BC?a??\u00B1\u03BC????\uFFE0\uFFE1\u00A8\u00F7?\u201C??????\u00E0\u03BC?a???\u00F7\u221A????\uFFE5\u221E???\u00B1?\u2026????

	
	String GenerateAreaPureText(CaratPos startPos,CaratPos endPos); //\u220F????\u02C9\u201D\u00FA\u2026\u02D9\u2265\u2026\uFFE5???\u00B1?\u2013\u2248?\uFFE0
	bool isPosValid(CaratPos pos); //\u03C0?\u00B1\u00ED?a\u00F7\u221A\u2013\u2248?\uFFE0??\u2211\u00F2\u201D\u2013\u2013?

	//\u03C0?????\u00E0\u02C9????\u03C0?
public:
	void SetSrollBarLockTime(float time);
	void SetScrollBarLockEnable(bool bEnable) { d_ScrobarLockEnable=bEnable; }
	void OnThumbLockEnd();

private:
	float d_ScrobarLockTime;
	float d_ScrobarLockElapseTime;
	bool  d_ScrobarLockState;
	bool  d_ScrobarLockEnable; //??\u2211\u00F2\u2018?\u2013\u00EC\u03C0????????o?????\u00B1?\u2018??\u00E0\u02C9??

private:
	/*************************************************************************
		Static Properties for this class
	*************************************************************************/
	static RichEditboxProperties::ReadOnly					d_readOnlyProperty;
	static RichEditboxProperties::WordWrap					d_wordWrapProperty;
	/*static RichEditboxProperties::CaratIndex				d_caratIndexProperty;
	static RichEditboxProperties::SelectionStart			d_selectionStartProperty;
	static RichEditboxProperties::SelectionLength			d_selectionLengthProperty;*/
	static RichEditboxProperties::MaxTextLength			d_maxTextLengthProperty;
    static RichEditboxProperties::SelectionBrushImage      d_selectionBrushProperty;
    static RichEditboxProperties::ForceVertScrollbar	    d_forceVertProperty;
	static RichEditboxProperties::BackGroundEnable	    d_BackGroundEnableProperty;
	static RichEditboxProperties::LineSpace             d_LineSpaceProperty;
    static RichEditboxProperties::TopAfterLoadFont	    d_TopAfterLoadProperty;


	/*************************************************************************
		Private methods
	*************************************************************************/
	void	addRichEditboxProperties(void);
	eTextAcceptMode    d_TextAcceptMode;
	RichEditboxComponent* d_MouseOnComponent;        //mouse \u00E0?\u2018?\u03BC?component
	void    UpdataMouseOnComponent();

	void    DelComponent(RichEditboxComponent* pComponent,bool eraseFromList=true); //eraseFromList??\u2211\u00F2\u00F7\u2013\u2030\u00F7???\u2013\u00B1\u00EC\u00F7\u2013\u2026?\u2265?

	bool    d_IsLockState;       //??\u2211\u00F2??\u00E0\u02C9???\uFFE5?\u00A8\uFFE1\u00A8\u00E0\u02C9???\uFFE5?\u00A8??\u2264a\u00E0\uFFE0\u2013?,?\u00E1\u03C0?readonly\u2019?\u220F??\uFFE5?\u00A8????\u201D\u221A\uFFE1\u00A8?\u2026\u2013\uFFE5\u2018\u00FA\u2264a????\u201D\u221A
	bool    d_UserLock;      //??\u2211\u00F2??\u201D\u221Aa?\u00E0\u02C9??
	void    AddWaitComponentToBox();  //?\u00ECo\u201D\uFFE5??\u00ECo\u201D\u03BC???o?
	void    ReleaseWaitComponents();
	void    ReleaseRefreshComponents();
	bool    CheckAppendLength(const String& ParseText);//判断增加指定长度的文本，是否超标.
	
	bool GetLineByPos(size_t& LineIndex,const Point& pt); //\u220F????a\u00F7\u221Aa\u00F2\u03BC\u221A\u03BC?o\u220F\u2013\u2013,pt?????a?\u02C9\u00B1\u00ED(?\u02C9?\u2018?\u02C9\u00B1\u00ED)

	bool d_BackGroundEnable;  //??\u2211\u00F2a\u2260\u00B1\u2265?\u221E?o
	float d_LineSpace; //\u2013\u2013o\u2030??

	bool d_ForceHideVerscroll;  //??\u00F7?\u201C?\u2264?\uFFE5\u03C0\u00F7\u00B1\u03C0?????
	Vector2 d_EmotionScale;  //\u00B1\u00EC?\u00E8\u00E0?\u2211\u2248?\u03BC?? ?\u00A8???\u03BC??1.0,1.0
	
	bool d_bEnterKeyIsPess;
	bool d_AutoHorCenter;  //?\u2018?????\u00FA???\u00F7?\u201D\u00F7\u2013
	bool d_AutoVertCenter;  //?\u2018??\u222B\u00B7?\u00FA???\u00F7?\u201D\u00F7\u2013
	float d_DrawYOffset;
	size_t d_MaxLineNumber; //?\u00F3\uFFE5?\u2013\u2013??
	size_t d_StartDrawLineIdx; //???oa\u2260\u03BC?\u2013\u2013\u00E0?\u201C?
	bool  d_TextBorder; //???\u00F7??\u2211\u00F2\u221A?\u00B1?
	float d_TextYoffset; //???\u00F7???\u00FA?\u2032\u201C?
	bool  d_WaitEmotionNum; //??\u2211\u00F2??\u2018?\u03BC?\uFFE5?\u00B1\u00EC?\u00E8\u03BC????\u00F7?\u2030??
	bool  d_AccceptEmotion;  //a?\u2265\u03BC?\u2211??\u00B1\u00EC?\u00E8?\u2030??

	std::vector<size_t> d_UpdateCpnList; //\u2013?\u201C?\u221A?\u201Ca\u00F7\u00B0?o\u220F?\u2013?\u03BC??\u201D??o?\uFFE1\u00A8\u00B1??\u00E1\u00B1\u00EC?\u00E8

	bool d_SupportEmotion; //??\u2211\u00F2\u00F7?\u2265\u00F7\u00B1\u00EC?\u00E8
	bool d_AutoChangeEmotionSize; //??\u2211\u00F2\u00F7?\u2265\u00F7\u00B1\u00EC?\u00E8\u220F???\u2013\u2013??\u00E0?\u2211\u2248

	float d_MaxLineHeight; //?\u00F3\u220F?\u03BC???\u201Ca\u2013\u2013\u03BC?\u220F???
    
    bool d_IsClickSelectLine; //\u70B9\u51FB\u9009\u62E9\u884C

    bool d_bTopAfterLoadFont;

	float d_ComponentHeightOffset; //背景图标高度间距
	
	bool d_isVertCenterfLineText; //添加richeditbox 文字是否居中判断
public:
	//richeditbox?\u2018??\u03A9\u201D??
	bool handle_horscrollVisChange(const EventArgs& args);
	void Refresh();  //\u2018?o\u201D??o??\u00B1\u201Ca??\u201C?\u03BC?\u201D\u221A
	void AppendBreak();
	void AppendText(const String& Text,const ColourRect& colours=ColourRect(colour(0xffffffff)),bool bBorder=false,const colour& borderColour=colour(0xFF000000));
	RichEditboxLinkTextComponent* AppendLinkText(const String& Text,const ColourRect& colours=ColourRect(colour(0xffffff00)));
	RichEditboxGoToComponent* AppendGoToText(const String& Text,const ColourRect& colours=ColourRect(colour(0xff00ff00)));

	RichEditboxLinkTextComponent* InsertLinkTextInCarat(const String& Text,const ColourRect& colours=ColourRect(colour(0xffffff00)));
	void InsertTextInCarat(const String& Text,const ColourRect& colours=ColourRect(colour(0xffffffff)));
	void InsertImageInCarat(const Image* pImage);
	RichEditboxEmotionComponent* InserEmotionInCarat(int emotionID);
	RichEditboxGoToComponent* InsertGoToTextInCarat(const String& Text,const ColourRect& colours=ColourRect(colour(0xff00ff00)));

	RichEditboxImageComponent* AppendImage(const Image* pImage, const int& useid = 0,const int& num = 0);
	RichEditboxImageComponent* AppendImage(const String& strSet,const String& strName);
	bool IsEmpty() { return d_FormatedComponents.size()?false:true; }
	void HandleEnd(); 
	void HandleTop();

	RichEditboxEmotionComponent* AppendEmotion(int emotionID);
	RichEditboxTipsLinkComponent* AppendTipsLink(const String& Text,const String& fontName,int64_t roleid,
		                                          int type,const String& key,int baseid, int shopID, int counterID,int bind,int64_t loseeffecttime);
	RichEditboxTipsLinkComponent* InsertTipsLinkInCarat(const String& Text,const String& fontName,int64_t roleid,
		int type,const String& key,int baseid, int64_t shopID, int counterID,int bind=0,int64_t loseeffecttime=0,const CEGUI::colour& nameColor=colour(0xFFFFFF00));	
	
	RichEditboxNameLinkComponent* InsertNameLinkInCarat(int64_t roleID,const String& roleName,int colorType=0);
	RichEditboxNameLinkComponent* AppendNameLink(int64_t roleID,const String& roleName);

	RichEditboxFamilyRecruitLinkComponent* InsertFamilyRecruitInCarat(int familyID,const String& familyName,
		int64_t leaderID,const String& leader,int rank,int level,const String& familyAim);

	RichEditboxFamilyRecruitLinkComponent* AppendFamilyRecruit(int familyID,const String& familyName,
		int64_t leaderID,const String& leader,int rank,int level,const String& familyAim);

	Size GetExtendSize(); //a\u00F2\u03BC\u221A
	RichEditboxComponent* GetComponentByPos(const Point& pt); //\u220F????a\u00F7\u221Aa\u00F2\u03BC\u221Acomponent,pt?????a?\u02C9\u00B1\u00ED(?\u02C9?\u2018?\u02C9\u00B1\u00ED)

	void SetCaratEnd();  //\u221E\u2014\u03C0?\u00B1\u00ED\u201C????\u00F3\u222B?

	void SetLockState(bool bIsLock);
	bool GetLockState() { return d_IsLockState; }
	void SetUserLock(bool bUserLock) { d_UserLock=bUserLock; }
	bool IsBackGroundEnable()const{ return d_BackGroundEnable; }
    void SetBackGroundEnable(bool enable);
	void SetTextBottomEdge(float offset) { d_TextBottomEdge=offset; }
	float GetTextBottomEdge() { return d_TextBottomEdge; }

	void SetLineSpace(float fSpace)  { d_LineSpace=fSpace; }
	float GetLineSpace() const { return d_LineSpace; }
	void SetPassMouseClick(bool b)  { d_PassMouseClick=b; }
	void SetForceHideVerscroll(bool b)  { d_ForceHideVerscroll=b; }
	void updateSelf(float elapsed);

	void SetComponentHeightOffset(float offset){ d_ComponentHeightOffset = offset; }
	float GetComponentHeightOffset(){ return d_ComponentHeightOffset; }

	void ParseTipsLinkInf(std::vector<TipsLinkInf>& vecTipsInf);
	int GetTipsLinkCount();  //a\u00F2\u03BC\u221A\u03BC\u00B1?\u221E\u03BC?tips?\uFFE5\u03A9\u201D\u03BC?\u220F???
	int GetTipsNameIndex(const String& name); //a\u00F2\u03BC\u221A?\u00A8\u221A?tips???\u2211\u03BC?\u220F???
	void EraseNumber(String& name); //??\u03BC\u00F9??\u00B1?\u00F7\u2013\u03BC????\u00F7
	int GetCharCount(); //a\u00F2\u03BC\u221A?\u00F7\u2211??? \u222B\u222B?\u00F7\u00E0??\u03A9\u220F??\u00F7\u2211?
	int GetLineCount(); //a\u00F2\u03BC\u221A\u2013\u2013??
	bool isCaratOnAllTop(); //\u03BC\u00B1?\u221E\u03C0?\u00B1\u00ED??\u2264a??\u2018?\u2019?\u220F??\u00FA\u03BC??\u00F3?\u221E?\u00E0

    //a\u00F2\u03BC\u221A?\u2265??o?a\u2260\u2014\u00B0\u2018\u00F2\u00B1\u00ED\u00F7??\u00B1\u03BC??\uFFE5?\u00A8\uFFE1\u00A8
	//startDrawOffset ???oa\u2260\u03BC??\u2032\u201C?
	//EndDrawOffset \u03A9\u00B7?\u02C9a\u2260\u03BC??\u2032\u201C?
	bool GetSelectDrawPos(size_t cpnIdx,size_t& startDrawOffset,size_t& EndDrawOffset);
	void DeleteSelectText();  //\u2026?\u2265?\u2014\u00B0\u00F7\u2013\u03BC??\u02C9\u201D\u00FA
	
	String GenerateSelectPureText(); //\u2026\u02D9\u2265\u2026\u2014\u00B0\u2018\u00F2?\u02C9\u201D\u00FA\u03BC?\uFFE5???\u00B1?\u2013\u2248?\uFFE0
	static String s_strCopyParseText;  //ctrl_c\u220F\uFFE5\u00F7?\u2265??\uFFE5\u03BC?\u03A9?????\u00B1?
	void SetEmotionScale(Vector2 scale) { d_EmotionScale=scale; }
	Vector2 GetEmotionScale() { return d_EmotionScale; }
	bool isOnlySpace(); //??\u2211\u00F2\u00F7a\u201D\u2013?\u2019?\u00F7\u2211?
	Point GetLineScreenPos(size_t index);  //\u2013\u2013\u03BC????a?\u02C9\u00B1\u00ED
	size_t GetCpnLineIndex(RichEditboxComponent* pCpn);  //?\u2265\u220F?Cpn\u2018?\u03BC?o\u220F\u2013\u2013
	RichEditboxComponent* GetFirstLinkTextCpn();  //\u2211\u03BCa?\u03BC?\u201Ca\u220F??\u2026\u03BC?a?\u03BC??\uFFE5\u03A9\u201D
    RichEditboxGoToComponent* GetFirstGotoLinkCpn();
    void OnFirstGotoLinkClick();
	Point GetCpnScreenPos(RichEditboxComponent* pCpn); //\u03BC\u221A\u03BC\u03A9?\u2265\u201Ca\u220F??\u201D??o?\u03BC????a?\u02C9\u00B1\u00ED
	void SetHoriAutoCenter(bool bHoriCenter) { d_AutoHorCenter=bHoriCenter; }
	bool isHoriAutoCenter() { return d_AutoHorCenter; }
	void SetVertAutoCenter(bool bVertCenter) { d_AutoVertCenter=bVertCenter; }
	bool isVertAutoCenter() { return d_AutoVertCenter; }
	float GetDrawYOffset() { return d_DrawYOffset; }
	bool isCaratOnEnd(); //\u03BC\u00B1?\u221E\u03C0?\u00B1\u00ED??\u2211\u00F2\u2018??\u00F3\u222B?
	void ParseControlText(); //\u03A9????\u00F7\u2211?\uFFE5? \u03A9???#\u03BC???\u03A9??\u2030??\u2013\u2248?\uFFE0
	void CheckLineCount();
	void SetMaxLineNumber(size_t number){ d_MaxLineNumber=number; }
	size_t GetStartDrawLineIndx() { return d_StartDrawLineIdx; }
	void SetTextBorder(bool bBorder) { d_TextBorder=bBorder; } //\u2026?\u00F7\u221A???\u00F7??\u2211\u00F2\u221A?\u00B1?
	bool isTextBorder() { return d_TextBorder; } //???\u00F7??\u2211\u00F2\u221A?\u00B1?  
	float GetTextYOffset() { return d_TextYoffset;} 
	void SetTextYOffset(float offset) { d_TextYoffset=offset; }

	bool isSupportEmotion() { return d_SupportEmotion; }              //??\u2211\u00F2\u00F7?\u2265\u00F7\u00B1\u00EC?\u00E8?\u2030??
	void SetSupportEmotion(bool bEnbale) { d_SupportEmotion=bEnbale; }  //\u2026?\u00F7\u221A??\u2211\u00F2\u00F7?\u2265\u00F7\u00B1\u00EC?\u00E8?\u2030??

	void EnableAutoChangeEmotionSize(bool bEnable) { d_AutoChangeEmotionSize=bEnable; }
	int GetEmotionLineNum(); //\u03BC\u221A\u03BC\u03A9\u201D\u2013??\u2026?\u2013\u2013\u201D\u2013\u00B1\u00EC?\u00E8
	Vector2 GetEmotionScaleByLineNum(int emotionLineNum,size_t totalLineNum); //\u220F???\u2013\u2013??\u03BC\u221A\u03BC\u03A9\u00B1\u00EC?\u00E8\u03BC?\u2019?\u2211\u2248?\u03BC??
	void SetAllEmotionScale(Vector2 scale);
	int GetEmotionNum();

	String ParseTextOutofBound(const String& srcStr, int boundLen);		//\uFFE5??\u00EC\u2018\u03A9\u03A9\u00E1?\u00F7\u2211?\uFFE5?
	bool isNumber(utf32 num);											//??\u2211\u00F2?????\u00F7?\u00F7\u2211?

	size_t GetFormatCpnNum() { return d_FormatedComponents.size(); }

	float GetMaxLineHeight() { return d_MaxLineHeight; } //\u03BC\u221A\u03BC\u03A9?\u00F3\u220F?\u03BC???\u201Ca\u2013\u2013\u03BC?\u220F???

	void RefreshUpdateCpnList(size_t startIdx,size_t endIdx); //\u00E0\uFFE0\u2013?\u2264a?\u2026\u00B1?o\u2260?\uFFE5?\u00A8??\u03BC?\u2013?\u201C?\u221A?\u00F7\u00B0?o\u220F?\u2013?\u03BC???o?\uFFE1\u00A8\u00B1??\u00E1\u00B1\u00EC?\u00E8
    
    virtual void InsertText(const String& strText);
        
    void GetAllEmotionID(std::vector<int>& emotionIds);//\u83B7\u5F97\u6240\u6709\u7684\u8868\u60C5
        
    bool isClickSelectLine() { return d_IsClickSelectLine; }
    
    void EnableClickSelectLine(bool b) { d_IsClickSelectLine=b; }
    
    RichEditboxComponent* GetLinkTextOnPos(const Point& pt);
    
    bool isPushedLine(size_t lineIdx);
    
    void setTopAfterLoadFont(bool b){d_bTopAfterLoadFont = b;}

    bool getTopAfterLoadFont() const{return d_bTopAfterLoadFont;}
        
    String GenerateAllPureText();
	
	void setTextLineVertCenter(bool b){ d_isVertCenterfLineText = b; }//添加文字是否居中
	bool getTextLineVertCenter(){ return d_isVertCenterfLineText; }
    };
    
} // End of  CEGUI namespace section
    
#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIMultiLineEditbox_h_
