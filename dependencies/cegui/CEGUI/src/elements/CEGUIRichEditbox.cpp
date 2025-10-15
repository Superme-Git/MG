/***********************************************************************
	filename: 	CEGUIRichEditbox.cpp
	created:	6/8/2010
	author:		\u00F2?\u2019?a\u2018

	purpose:	Implementation of the Multi-line edit box base class
*************************************************************************/

#include "elements/CEGUIRichEditbox.h"
#include "elements/CEGUIScrollbar.h"
#include "elements/CEGUIThumb.h"
#include "elements/CEGUICompnentTip.h"
#include "CEGUITextUtils.h"
#include "CEGUIImage.h"
#include "CEGUIExceptions.h"
#include "CEGUICoordConverter.h"
#include "CEGUIWindowManager.h"
#include "elements/CEGUIRichEditboxTextComponent.h"
#include "elements/CEGUIRichEditboxImageComponent.h"
#include "elements/CEGUIRichEditboxBreakLineComponent.h"
#include "elements/CEGUIRichEditboxEmotionComponent.h"


#include "CEGUIXMLAttributes.h"
#include "CEGUIFontManager.h"
#include "CEGUIRichEditbox_xmlHandler.h"
#include "CEGUIPropertyHelper.h"
#include "utils/StringUtil.h"

using namespace CEGUITinyXML;



// Start of CEGUI namespace section
namespace CEGUI
{
const String RichEditbox::EventNamespace("RichEditbox");
const String RichEditbox::WidgetTypeName("CEGUI/RichEditbox");


//const String TextElement( (utf8*)"T" );   //Text
//const String ImageElement( (utf8*)"I" );
//const String EmoteElement( (utf8*)"E" );
//const String EmoteElement( (utf8*)"E" );
//
//const String AttributeText( (utf8*)"t" );
//const String AttributeColor( (utf8*)"c" );
//const String AttributeColor( (utf8*)"c" );



//const char	NameAttribute[]			= "name";
//const char	ColorAttribute[]		= "color";
//
//const char	NewLineAttribute[]		= "newline";

/*************************************************************************
    MultiLineEditboxWindowRenderer
*************************************************************************/
String RichEditbox::s_strCopyParseText="";

RichEditboxWindowRenderer::RichEditboxWindowRenderer(const String& name) :
    WindowRenderer(name, RichEditbox::EventNamespace)
{
}

//----------------------------------------------------------------------------//
void RichEditboxWindowRenderer::onLookNFeelAssigned()
{
    assert(d_window != 0);

    // ensure window's text has a terminating \n
    /*String text(d_window->getText());
    if (text.empty() || text[text.length() - 1] != '\n')
    {
        text.append(1, '\n');
        d_window->setText(text);
    }*/
}

/*************************************************************************
	TODO:

	Clipboard support
	Undo support
*************************************************************************/
/*************************************************************************
	Static Properties for this class
*************************************************************************/
RichEditboxProperties::ReadOnly				RichEditbox::d_readOnlyProperty;
RichEditboxProperties::WordWrap				RichEditbox::d_wordWrapProperty;
//RichEditboxProperties::CaratIndex				RichEditbox::d_caratIndexProperty;
//RichEditboxProperties::SelectionStart			RichEditbox::d_selectionStartProperty;
//RichEditboxProperties::SelectionLength			RichEditbox::d_selectionLengthProperty;
RichEditboxProperties::MaxTextLength			RichEditbox::d_maxTextLengthProperty;
RichEditboxProperties::SelectionBrushImage     RichEditbox::d_selectionBrushProperty;
RichEditboxProperties::ForceVertScrollbar      RichEditbox::d_forceVertProperty;
RichEditboxProperties::BackGroundEnable     RichEditbox::d_BackGroundEnableProperty;
RichEditboxProperties::LineSpace     RichEditbox::d_LineSpaceProperty;
RichEditboxProperties::TopAfterLoadFont     RichEditbox::d_TopAfterLoadProperty;



/*************************************************************************
	Constants
*************************************************************************/
// event names
const String RichEditbox::EventReadOnlyModeChanged( "ReadOnlyChanged" );
const String RichEditbox::EventWordWrapModeChanged( "WordWrapModeChanged" );
const String RichEditbox::EventMaximumTextLengthChanged( "MaximumTextLengthChanged" );
const String RichEditbox::EventCaratMoved( "CaratMoved" );
const String RichEditbox::EventTextSelectionChanged( "TextSelectionChanged" );
const String RichEditbox::EventEditboxFull( "EditboxFullEvent" );
const String RichEditbox::EventVertScrollbarModeChanged( "VertScrollbarModeChanged" );
const String RichEditbox::EventHorzScrollbarModeChanged( "HorzScrollbarModeChanged" );
const String RichEditbox::EventTextAccepted("TextAccepted");

const String RichEditbox::EventUpKeyDown("UpKeyDown");
const String RichEditbox::EventDownKeyDown("DownKeyDown");



// Static data initialisation
String RichEditbox::d_lineBreakChars("\n");

/*************************************************************************
    Child Widget name suffix constants
*************************************************************************/
const String RichEditbox::VertScrollbarNameSuffix( "__auto_vscrollbar__" );
const String RichEditbox::HorzScrollbarNameSuffix( "__auto_hscrollbar__" );

/*************************************************************************
	Constructor for the MultiLineEditbox base class.
*************************************************************************/
RichEditbox::RichEditbox(const String& type, const String& name) :
	Window(type, name),
	d_readOnly(false),
	d_maxTextLen(String::max_size()),
	d_CurTextLen(0),
	d_caratPos(0,0),
	d_selectionStart(0,0),
	d_selectionEnd(0,0),
	d_dragging(false),
	//d_dragAnchorIdx(0),
	d_wordWrap(true),
	d_widestExtent(0.0f),
	d_forceVertScroll(false),
	d_forceHorzScroll(false),
	d_selectionBrush(0),
	d_ColourRect(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF),
	d_TotalHeight(0.0f),
	d_TextAcceptMode(eTextAcceptMode_Enter),
	d_CurrentLine(0,0),
	d_ParseText(""),
	d_MouseOnComponent(0),
	d_IsLockState(false),
	d_BackGroundEnable(true),
	d_LineSpace(4.0f),
	d_TextBottomEdge(0.0f),
	d_PassMouseClick(false),
	d_ForceHideVerscroll(true),
	d_EmotionScale(1.0f,1.0f),
	d_bEnterKeyIsPess(false),
	d_AutoHorCenter(false),
	d_AutoVertCenter(false),
	d_DrawYOffset(0.0f),
	d_MaxLineNumber(99999),
	d_StartDrawLineIdx(0),
	d_ScrobarLockTime(30.0f),
	d_ScrobarLockElapseTime(0.0f),
	d_ScrobarLockState(false),
	d_ScrobarLockEnable(false),
	d_UserLock(false),
	d_TextBorder(false),
	d_TextYoffset(0.0f),
	d_WaitEmotionNum(false),
	d_AccceptEmotion(false),
	d_SupportEmotion(true),
	d_AutoChangeEmotionSize(false),
	d_MaxLineHeight(0.0f),
    d_IsClickSelectLine(false),
    d_bTopAfterLoadFont(false),
	d_ComponentHeightOffset(0.0f),
	d_isVertCenterfLineText(false)
{
	addRichEditboxProperties();
	
	if (!d_readOnly)
	{
		SetCanEdit(true);
		SetDragMoveEnable(false);
        EnbaleSlide(true);
        EnableDrag(true);
	}else
	{
        d_AccceptEmotion=true;
	}
	
	//setMouseCursor(System::getSingleton().getDefaultMouseCursor());
    // override default and disable text parsing
    d_textParsingEnabled = false;
	d_lines.resize(0);
	d_TempComponents.resize(0);
	d_FormatedComponents.resize(0);
	d_WaitToAddComponents.resize(0);
	d_TempComponents.resize(0);

	d_UpdateCpnList.clear();

	//format();
}


RichEditbox::RichEditbox(const String& type) :
	Window(type)
{
}

const RichEditbox& RichEditbox::operator=(const RichEditbox& t)
{
	Window::operator=(t);
	d_readOnly = t.d_readOnly;
	d_maxTextLen = t.d_maxTextLen;
	d_CurTextLen = t.d_CurTextLen;
	d_caratPos = t.d_caratPos;
	d_selectionStart = t.d_selectionStart;
	d_selectionEnd = t.d_selectionEnd;
	d_dragging = t.d_dragging;
	d_dragAnchorPos = t.d_dragAnchorPos;
	d_wordWrap = t.d_wordWrap;
	d_lines = t.d_lines;
	d_TempComponents = t.d_TempComponents;
	d_FormatedComponents = t.d_FormatedComponents;
	d_WaitToAddComponents = t.d_WaitToAddComponents;
	d_RefreshComponents = t.d_RefreshComponents;
	d_widestExtent = t.d_widestExtent;
	d_TotalHeight = t.d_TotalHeight;
	d_ParseText = t.d_ParseText;
	d_forceVertScroll = t.d_forceVertScroll;
	d_forceHorzScroll = t.d_forceHorzScroll;
	downPosition = t.downPosition;
	d_selectionBrush = t.d_selectionBrush;
	d_CurrentLine = t.d_CurrentLine;
	d_ColourRect = t.d_ColourRect;
	d_TextBottomEdge = t.d_TextBottomEdge;
	d_PassMouseClick = t.d_PassMouseClick;
	d_ScrobarLockTime = t.d_ScrobarLockTime;
	d_ScrobarLockElapseTime = t.d_ScrobarLockElapseTime;
	d_ScrobarLockState = t.d_ScrobarLockState;
	d_ScrobarLockEnable = t.d_ScrobarLockEnable;
	d_TextAcceptMode = t.d_TextAcceptMode;
	d_MouseOnComponent = t.d_MouseOnComponent;
	d_IsLockState = t.d_IsLockState;
	d_UserLock = t.d_UserLock;
	d_BackGroundEnable = t.d_BackGroundEnable;
	d_LineSpace = t.d_LineSpace;
	d_ForceHideVerscroll = t.d_ForceHideVerscroll;
	d_EmotionScale = t.d_EmotionScale;
	d_bEnterKeyIsPess = t.d_bEnterKeyIsPess;
	d_AutoHorCenter = t.d_AutoHorCenter;
	d_AutoVertCenter = t.d_AutoVertCenter;
	d_DrawYOffset = t.d_DrawYOffset;
	d_MaxLineNumber = t.d_MaxLineNumber;
	d_StartDrawLineIdx = t.d_StartDrawLineIdx;
	d_TextBorder = t.d_TextBorder;
	d_TextYoffset = t.d_TextYoffset;
	d_WaitEmotionNum = t.d_WaitEmotionNum;
	d_AccceptEmotion = t.d_AccceptEmotion;
	d_UpdateCpnList = t.d_UpdateCpnList;
	d_SupportEmotion = t.d_SupportEmotion;
	d_AutoChangeEmotionSize = t.d_AutoChangeEmotionSize;
	d_MaxLineHeight = t.d_MaxLineHeight;
	d_IsClickSelectLine = t.d_IsClickSelectLine;
	d_bTopAfterLoadFont = t.d_bTopAfterLoadFont;
	d_ComponentHeightOffset = t.d_ComponentHeightOffset;
	d_isVertCenterfLineText = t.d_isVertCenterfLineText;
	EnableDrag(true); //make it dragable by default
	return *this;
}

Window* RichEditbox::clone(Window* wnd)
{
	RichEditbox* retWnd = (RichEditbox*)wnd;
	if (retWnd == NULL)
		retWnd = new RichEditbox(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == RichEditbox::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

/*************************************************************************
	Destructor for the MultiLineEditbox base class.
*************************************************************************/
RichEditbox::~RichEditbox(void)
{
	Clear();
}


/*************************************************************************
	Initialise the Window based object ready for use.
*************************************************************************/
void RichEditbox::initialiseComponents(bool bClone)
{
	// create the component sub-widgets
	Scrollbar* vertScrollbar = getVertScrollbar();
	Scrollbar* horzScrollbar = getHorzScrollbar();
	if (!bClone)
	{
		horzScrollbar->hide();
		vertScrollbar->hide();
	}
    vertScrollbar->subscribeEvent(Scrollbar::EventScrollPositionChanged, Event::Subscriber(&RichEditbox::handle_scrollChange, this));
    horzScrollbar->subscribeEvent(Scrollbar::EventScrollPositionChanged, Event::Subscriber(&RichEditbox::handle_scrollChange, this));
	horzScrollbar->subscribeEvent(Window::EventShown, Event::Subscriber(&RichEditbox::handle_horscrollVisChange, this));
	if (!bClone)
	{
		performChildWindowLayout();
		formatText();
	}
}


/*************************************************************************
	return true if the edit box has input focus.
*************************************************************************/
bool RichEditbox::hasInputFocus(void) const
{
	return !d_readOnly;
}

/*************************************************************************
	return the current selection start point.
*************************************************************************/
RichEditbox::CaratPos RichEditbox::getSelectionStartPos(void) const
{
	//return (d_selectionStart != d_selectionEnd) ? d_selectionStart : d_caratPos;
	return d_selectionStart;
}


/*************************************************************************
	return the current selection end point.
*************************************************************************/
RichEditbox::CaratPos RichEditbox::getSelectionEndPos(void) const
{
	//return (d_selectionStart != d_selectionEnd) ? d_selectionEnd : d_caratPos;
	return d_selectionEnd;
}


/*************************************************************************
	return the length of the current selection (in code points / characters).
*************************************************************************/
size_t RichEditbox::getSelectionLength(void) 
{
	//return d_selectionEnd - d_selectionStart;
	if (d_selectionStart!=d_selectionEnd)
	{
		if (d_selectionStart.d_ComponentIndex==d_selectionEnd.d_ComponentIndex)
		{
			size_t index=d_selectionStart.d_ComponentIndex;
			if (index<d_FormatedComponents.size())
			{
				size_t startLength=d_FormatedComponents[index]->GetCharNumByIndex(d_selectionStart.d_ComponentOffset);
				size_t endLength=d_FormatedComponents[index]->GetCharNumByIndex(d_selectionEnd.d_ComponentOffset);

				if (endLength>=startLength)
				{
					return endLength-startLength;
				}
			}
		}else
		{
			size_t startIdx=d_selectionStart.d_ComponentIndex;
			if (startIdx<d_FormatedComponents.size()
				&&d_selectionEnd.d_ComponentIndex<d_FormatedComponents.size())
			{
				size_t startLength=d_FormatedComponents[startIdx]->GetCharNumByIndex(d_selectionStart.d_ComponentOffset);
				startLength=d_FormatedComponents[startIdx]->GetCharCount()-startLength;

				size_t endLength=d_FormatedComponents[d_selectionEnd.d_ComponentIndex]->GetCharNumByIndex(d_selectionEnd.d_ComponentOffset);

				size_t beginIdx=startIdx+1;
				size_t endIdx=d_selectionEnd.d_ComponentIndex-1;

				size_t length=startLength;

				for (size_t i=beginIdx;i<=endIdx;++i)
				{
					if (i<d_FormatedComponents.size())
					{
						length+=d_FormatedComponents[i]->GetCharCount();
					}
					
				}
				length+=endLength;

				return length;
			}
			

		}

	}
	return 0;
}


/*************************************************************************
	Specify whether the edit box is read-only.
*************************************************************************/
void RichEditbox::setReadOnly(bool setting)
{
	// if setting is changed
	if (d_readOnly != setting)
	{
		d_readOnly = setting;
		WindowEventArgs args(this);
 		onReadOnlyChanged(args);
		if (d_readOnly)
		{
			SetCanEdit(false);
			SetDragMoveEnable(true);
			d_AccceptEmotion=true;
            EnbaleSlide(true);
            EnableDrag(true);
		}else
		{
			SetCanEdit(true);
			SetDragMoveEnable(false);
			d_AccceptEmotion=false;
            EnbaleSlide(false);
            EnableDrag(false);
		}
	}

}


/*************************************************************************
	Set the current position of the carat.
*************************************************************************/
void RichEditbox::setCaratPos(CaratPos carat_pos)
{
	// make sure new position is valid
	if (d_FormatedComponents.size()<=0)
	{
		d_caratPos.d_ComponentIndex=0;
		d_caratPos.d_ComponentOffset=0;
	}else
	{
		if (carat_pos.d_ComponentIndex >=d_FormatedComponents.size())
		{
			carat_pos.d_ComponentIndex = d_FormatedComponents.size() - 1;
		}
		if (carat_pos.d_ComponentIndex<0)
		{
			carat_pos.d_ComponentIndex=0;
		}

		size_t ComponentMaxIndex=d_FormatedComponents[carat_pos.d_ComponentIndex]->GetMaxIndex();
		if (carat_pos.d_ComponentOffset>ComponentMaxIndex)
		{
			carat_pos.d_ComponentOffset=ComponentMaxIndex;
		}

		if (carat_pos.d_ComponentOffset<0)
		{
			carat_pos.d_ComponentOffset=0;
		}

	}
    

	// if new position is different
	if (d_caratPos != carat_pos)
	{
		d_caratPos = carat_pos;
		invalidate();
		ensureCaratIsVisible();

		// Trigger "carat moved" event
		WindowEventArgs args(this);
		onCaratMoved(args);
	}

}


/*************************************************************************
	Define the current selection for the edit box
*************************************************************************/
void RichEditbox::setSelection(CaratPos start_pos, CaratPos end_pos)
{
	// ensure selection start point is within the valid range
	if (d_FormatedComponents.size()==0)
	{
		start_pos=CaratPos(0,0);
		end_pos=CaratPos(0,0);
		
	}else
	{
		if (start_pos.d_ComponentIndex >= d_FormatedComponents.size())
		{
			start_pos.d_ComponentOffset = d_FormatedComponents.size() - 1;
		}

		if (start_pos.d_ComponentOffset>d_FormatedComponents[start_pos.d_ComponentIndex]->GetMaxIndex())
		{
			start_pos.d_ComponentOffset=d_FormatedComponents[start_pos.d_ComponentIndex]->GetMaxIndex();

		}

		// ensure selection end point is within the valid range
		if (end_pos.d_ComponentIndex >= d_FormatedComponents.size())
		{
			end_pos.d_ComponentOffset = d_FormatedComponents.size() - 1;
		}

		if (end_pos.d_ComponentOffset>d_FormatedComponents[end_pos.d_ComponentIndex]->GetMaxIndex())
		{
			end_pos.d_ComponentOffset=d_FormatedComponents[end_pos.d_ComponentIndex]->GetMaxIndex();

		}

	}
	

	// ensure start is before end
	if (start_pos > end_pos)
	{
		CaratPos tmp = end_pos;
		end_pos = start_pos;
		start_pos = tmp;
	}

	// only change state if values are different.
	if ((start_pos != d_selectionStart) || (end_pos != d_selectionEnd))
	{
		// setup selection
		d_selectionStart = start_pos;
		d_selectionEnd	 = end_pos;

		// Trigger "selection changed" event
		WindowEventArgs args(this);
		onTextSelectionChanged(args);
	}

}


/*************************************************************************
	set the maximum text length for this edit box.
*************************************************************************/
void RichEditbox::setMaxTextLength(size_t max_len)
{
	if (d_maxTextLen != max_len)
	{
		d_maxTextLen = max_len;

		// Trigger max length changed event
		WindowEventArgs args(this);
		onMaximumTextLengthChanged(args);

		// trim string
        /*if (getText().length() > d_maxTextLen)
		{
            String newText = getText();
            newText.resize(d_maxTextLen);
            setText(newText);

			onTextChanged(args);
		}*/

	}

}


/*************************************************************************
	Scroll the view so that the current carat position is visible.
*************************************************************************/
void RichEditbox::ensureCaratIsVisible(void)
{
 //   Scrollbar* vertScrollbar = getVertScrollbar();
 //   Scrollbar* horzScrollbar = getHorzScrollbar();

	//// calculate the location of the carat
	//Font* fnt = getFont();
	//size_t caratLine = getLineNumberFromPos(d_caratPos);

	//if (caratLine < d_lines.size())
	//{
	//	Rect textArea(getTextRenderArea());

	//	size_t caratLineIdx = d_caratPos - d_lines[caratLine].d_startIdx;

	//	float ypos = caratLine * fnt->getLineSpacing();
 //       float xpos = fnt->getTextExtent(getText().substr(d_lines[caratLine].d_startIdx, caratLineIdx));

	//	// adjust position for scroll bars
	//	xpos -= horzScrollbar->getScrollPosition();
	//	ypos -= vertScrollbar->getScrollPosition();

	//	// if carat is above window, scroll up
	//	if (ypos < 0)
	//	{
	//		vertScrollbar->setScrollPosition(vertScrollbar->getScrollPosition() + ypos);
	//	}
	//	// if carat is below the window, scroll down
	//	else if ((ypos += fnt->getLineSpacing()) > textArea.getHeight())
	//	{
	//		vertScrollbar->setScrollPosition(vertScrollbar->getScrollPosition() + (ypos - textArea.getHeight()) + fnt->getLineSpacing());
	//	}

	//	// if carat is left of the window, scroll left
	//	if (xpos < 0)
	//	{
	//		horzScrollbar->setScrollPosition(horzScrollbar->getScrollPosition() + xpos - 50);
	//	}
	//	// if carat is right of the window, scroll right
	//	else if (xpos > textArea.getWidth())
	//	{
	//		horzScrollbar->setScrollPosition(horzScrollbar->getScrollPosition() + (xpos - textArea.getWidth()) + 50);
	//	}

	//}

	

}


/*************************************************************************
	Set whether the text will be word wrapped or not.
*************************************************************************/
void RichEditbox::setWordWrapping(bool setting)
{
	if (setting != d_wordWrap)
	{
		d_wordWrap = setting;
		formatText();

		WindowEventArgs args(this);
		onWordWrapModeChanged(args);
	}

}


/*************************************************************************
	display required integrated scroll bars according to current state
	of the edit box and update their values.
*************************************************************************/
void RichEditbox::configureScrollbars(void)
{
    Scrollbar* vertScrollbar = getVertScrollbar();
    Scrollbar* horzScrollbar = getHorzScrollbar();
	bool bOldVertScrollbarShow=vertScrollbar->isVisible(true);
	bool bOldHorzBarVis=horzScrollbar->isVisible(true);
	float totalHeight	= d_TotalHeight;//(float)d_lines.size() * getFont()->getLineSpacing();
	float widestItem	= d_widestExtent;

	if (d_ForceHideVerscroll)
	{
		vertScrollbar->hide();
	}else
	{
		if ((totalHeight > getTextRenderArea().getHeight()) || d_forceVertScroll)
		{
			vertScrollbar->show();
		}else
		{
			vertScrollbar->hide();
		}

	}

	
	

	horzScrollbar->hide();
	bool bCurVertScrollbarShow=vertScrollbar->isVisible(true);
	bool bCurHozrScrollbarShow=horzScrollbar->isVisible(true);
	if (bCurVertScrollbarShow!=bOldVertScrollbarShow||bOldHorzBarVis!=bCurHozrScrollbarShow)
	{
		EditModeFormat(false);
	}

	//
	// Set up scroll bar values
	//
	Rect renderArea(getTextRenderArea());

	vertScrollbar->setDocumentSize(PixelAligned(d_TotalHeight));
	vertScrollbar->setPageSize(PixelAligned(renderArea.getHeight()));
	vertScrollbar->setStepSize(ceguimax(1.0f, PixelAligned(getFont()->getFontHeight())));
	vertScrollbar->setScrollPosition(PixelAligned(vertScrollbar->getScrollPosition()));

	if (d_windowRenderer != 0)
	{
		RichEditboxWindowRenderer* wr = (RichEditboxWindowRenderer*)d_windowRenderer;
		wr->FixStartDrawLineIdx();
	}

	/*horzScrollbar->setDocumentSize(widestItem);
	horzScrollbar->setPageSize(renderArea.getWidth());
	horzScrollbar->setStepSize(ceguimax(1.0f, renderArea.getWidth() / 10.0f));
	horzScrollbar->setScrollPosition(horzScrollbar->getScrollPosition());*/
}


/*************************************************************************
	Format the text into lines as needed by the current formatting options.
*************************************************************************/
void RichEditbox::formatText(void)
{
     //TODO: ASSAF - todo
	// clear old formatting data
	
	//DestroyAllComponent();
	//d_Components.resize(0);
	d_widestExtent = 0.0f;

	//d_ParseText=GenerateParseText();
	//DestroyAllComponent();
	//CEGUI::RichEditbox_xmlHandler handle(d_ParseText,this);
	format();
	//configureScrollbars();
	//invalidate();
}

void    RichEditbox::DestroyAllComponent()
{
	//d_TempComponents?\u2026??\u201D?d_FormatedComponents\u201D\u2013\u03A9ao?\uFFE1\u00A8\u2264a\u00F7?\u220F\uFFE5delete
	//d_TempComponents delete\u2018?\u00E0?\uFFE1\u00A8\u2018\u00FA\u2018?d_FormatedComponents\u00F7\u2013erase\u03BC\u00F9
	if (d_TempComponents.size()>0)
	{
		ComponentListIt it=d_TempComponents.begin();
		for (;it!=d_TempComponents.end();++it)
		{
			if ((*it)!=NULL)
			{
				DelComponent(*it,true);
			}
		}
	}
	d_TempComponents.clear();

	if (d_FormatedComponents.size()>0)
	{
		ComponentListIt it=d_FormatedComponents.begin();
		for (;it!=d_FormatedComponents.end();++it)
		{
			if ((*it)!=NULL)
			{
				DelComponent(*it,false);
			}
		}
	}
	d_FormatedComponents.clear();
	d_UpdateCpnList.clear();

	ReleaseWaitComponents();
	ReleaseRefreshComponents();
}


//void RichEditbox::FormatByText()
//{
//	/*d_Components.clear();
//	d_widestExtent = 0.0f;
//
//	CEGUI::RichEditbox_xmlHandler handle(d_ParseText,this);
//	format();
//	configureScrollbars();
//	invalidate();*/
//}

//void RichEditbox::AppendParseText(const String& ParseText)
//{
//	if (ParseText.size()>0)
//	{
//		String NewParseText("<content>");
//		//NewParseText+=GenerateParseText(false);
//		NewParseText+=ParseText;
//		NewParseText+=" </content>";
//		//d_ParseText=NewParseText;
//		//FormatByText();
//		
//		std::vector<TextCpnInf> infs;
//		infs.clear();
//		CEGUI::RichEditbox_xmlHandler handle(NewParseText,this,false,"",infs);
//		//format();
//	}
//}

void RichEditbox::AppendParseText(const String& ParseText,bool bCheckShied)
{
	if (!CheckAppendLength(ParseText))
	{
		WindowEventArgs e(this);
		onEditboxFullEvent(e);
		return;
	}
	if (ParseText.size()>0)
	{
		String NewParseText("<content>");
		//NewParseText+=GenerateParseText(false);
		NewParseText+=ParseText;
		NewParseText+=" </content>";
		//d_ParseText=NewParseText;
		//FormatByText();
		CEGUI::RichEditbox_xmlHandler handle(NewParseText,this,bCheckShied);
		if (bCheckShied)
		{
			handle.CheckShied();
		}
		//format();
	}
}

RichEditboxImageComponent* RichEditbox::AppendImage(const Image* pImage, const int& useid,const int& num)
{
	if (NULL!=pImage)
	{
		String set(pImage->getImagesetName());
		String image(pImage->getName());

		RichEditboxImageComponent* pComponent=new RichEditboxImageComponent(set,image,num);
		pComponent->SetUserID(useid);
		AddRefreshComponent(pComponent);
		return pComponent;
	}
	return NULL;
}

RichEditboxImageComponent* RichEditbox::AppendImage(const String& strSet,const String& strName)
{
	RichEditboxImageComponent* pComponent=new RichEditboxImageComponent(strSet,strName,0);
	pComponent->SetUserID(0);
	AddRefreshComponent(pComponent);
	return pComponent;
}

RichEditboxEmotionComponent* RichEditbox::AppendEmotion(int emotionID)
{
	if (emotionID!=-1)
	{
		RichEditboxEmotionComponent* pComponent=new RichEditboxEmotionComponent();
		pComponent->SetScale(d_EmotionScale);
		pComponent->SetAnimateID(emotionID);
		//pComponent->SetParent(this);
		AddRefreshComponent(pComponent);
		return pComponent;
	}
	return NULL;
}

RichEditboxTipsLinkComponent* RichEditbox::AppendTipsLink(const String& Text,const String& fontName,int64_t roleid,
														  int type,const String& key,int baseid, int shopID, int counterID,int bind,int64_t loseeffecttime)
{
	if (type!=0&&!key.empty()&&!Text.empty())
	{
		RichEditboxTipsLinkComponent* pComponent=new RichEditboxTipsLinkComponent(Text,fontName,roleid,type,
			                                                                      key,baseid,shopID,counterID,bind,loseeffecttime);
		
		AddRefreshComponent(pComponent);
		return pComponent;
	}
	return NULL;
}

RichEditboxTipsLinkComponent* RichEditbox::InsertTipsLinkInCarat(const String& Text,const String& fontName,int64_t roleid,
													int type,const String& key,int baseid, int64_t shopID, int counterID
													,int bind,int64_t loseeffecttime,const CEGUI::colour& nameColor)
{
	if (type!=0&&!key.empty()&&!Text.empty())
	{
		RichEditboxTipsLinkComponent* pComponent=new RichEditboxTipsLinkComponent(Text,fontName,roleid,type,
			key,baseid,shopID,counterID,bind,loseeffecttime);
		pComponent->setColours(nameColor);

		if(!InsertComponentInCarat(pComponent))
        {
            delete pComponent;
            pComponent = NULL;
        }
		return pComponent;
	}
	return NULL;

}





/*************************************************************************
	Return the length of the next token in String 'text' starting at
	index 'start_idx'.
*************************************************************************/
size_t RichEditbox::getNextTokenLength(const String& text, size_t start_idx) const
{
	String::size_type pos = text.find_first_of(TextUtils::DefaultWrapDelimiters, start_idx);

	// handle case where no more whitespace exists (so this is last token)
	if (pos == String::npos)
	{
		return (text.length() - start_idx);
	}
	// handle 'delimiter' token cases
	else if ((pos - start_idx) == 0)
	{
		return 1;
	}
	else
	{
		return (pos - start_idx);
	}

}


/*************************************************************************
	Return the text code point index that is rendered closest to screen
	position 'pt'.
*************************************************************************/
RichEditbox::CaratPos RichEditbox::getComponentInfFromPosition(const Point& pt) 
{
	
	
	//calculate final window position to be checked
	CaratPos pos(0,0);
	if (d_lines.size()<=0||d_FormatedComponents.size()<=0)
	{
		return pos;
	}
	Point wndPt = CoordConverter::screenToWindow(*this, pt);

	Rect textArea(getTextRenderArea());

	wndPt.d_x -= textArea.d_left;
	wndPt.d_y -= textArea.d_top;

	// factor in scroll bar values
	wndPt.d_x += getHorzScrollbar()->getScrollPosition();
	wndPt.d_y += getVertScrollbar()->getScrollPosition();

	
	if (!d_readOnly&&!d_wordWrap)
	{
		if (d_windowRenderer != 0)
		{
			RichEditboxWindowRenderer* wr = (RichEditboxWindowRenderer*)d_windowRenderer;
			float textXOffset= wr->GetLastTestOffest();
			wndPt.d_x-=textXOffset;
		}

	}

	size_t lineNumber =0;
	GetLineByPos(lineNumber,wndPt);
	//static_cast<size_t>(wndPt.d_y / getFont()->getLineSpacing());

	if (lineNumber >= d_lines.size())
	{
		lineNumber=0;
		if (d_lines.size()>0)
		{
			lineNumber = d_lines.size() - 1;
		}
		
	}

	size_t LintStartCompnIdx=d_lines[lineNumber].d_startComponentIdx;
	size_t CompnCount=d_lines[lineNumber].d_ComponentCount;
	Size lineSize=GetLineSize(lineNumber);
	if (wndPt.d_x>=lineSize.d_width)
	{
		pos.d_ComponentIndex=LintStartCompnIdx+CompnCount-1;
		pos.d_ComponentOffset=d_FormatedComponents[pos.d_ComponentIndex]->GetMaxIndex();
		return pos;
	}
	float wide=0.0f;
	for (size_t i=LintStartCompnIdx;i<=LintStartCompnIdx+CompnCount-1;i++)
	{
		if (i>d_FormatedComponents.size())
		{
			break;
		}
		Size size=d_FormatedComponents[i]->getPixelSize();
		wide+=size.d_width;
		if (wndPt.d_x<=wide)
		{
			pos.d_ComponentIndex=i;
			pos.d_ComponentOffset=d_FormatedComponents[i]->GetIndexByWidth(wndPt.d_x-(wide-size.d_width));
			
			break;
		}
	}
	return pos;
}

RichEditboxComponent* RichEditbox::GetComponentByPos(const Point& pt) //\u220F????a\u00F7\u221Aa\u00F2\u03BC\u221Acomponent,pt?????a?\u02C9\u00B1\u00ED(?\u02C9?\u2018?\u02C9\u00B1\u00ED)
{
	CaratPos pos(0,0);
	if (d_lines.size()<=0||d_FormatedComponents.size()<=0)
	{
		return NULL;
	}
	Point wndPt = CoordConverter::screenToWindow(*this, pt);

	Rect textArea(getTextRenderArea());

	wndPt.d_x -= textArea.d_left;
	wndPt.d_y -= textArea.d_top;

	// factor in scroll bar values
	wndPt.d_x += getHorzScrollbar()->getScrollPosition();
	wndPt.d_y += getVertScrollbar()->getScrollPosition();

	size_t lineNumber = 0;
	//static_cast<size_t>(wndPt.d_y / getFont()->getLineSpacing());
     
	if (!GetLineByPos(lineNumber,wndPt))
	{
		return NULL;
	}
	

	//if (lineNumber >= d_lines.size())
	//{
	//	/*lineNumber=0;
	//	if (d_lines.size()>0)
	//	{
	//		lineNumber = d_lines.size() - 1;
	//	}*/

	//}

	size_t LintStartCompnIdx=d_lines[lineNumber].d_startComponentIdx;
	size_t CompnCount=d_lines[lineNumber].d_ComponentCount;
	Size lineSize=GetLineSize(lineNumber);
	if (wndPt.d_x>=lineSize.d_width)
	{
		return NULL;
	}
	float wide=0.0f;
	for (size_t i=LintStartCompnIdx;i<=LintStartCompnIdx+CompnCount-1;i++)
	{
		if (i>d_FormatedComponents.size())
		{
			break;
		}
		Size size=d_FormatedComponents[i]->getPixelSize();
		wide+=size.d_width;
		if (wndPt.d_x<=wide)
		{
           return d_FormatedComponents[i];
		}
	}
	return NULL;
}



/*************************************************************************
	Return the line number a given index falls on with the current
	formatting.  Will return last line if index is out of range.
*************************************************************************/
size_t RichEditbox::getLineNumberFromPos(CaratPos index) const
{
	/*size_t lineCount = d_lines.size();

	if (lineCount == 0)
	{
		return 0;
	}
    else if (index >= getText().length() - 1)
	{
		return lineCount - 1;
	}
	else
	{
		size_t indexCount = 0;
		size_t caratLine = 0;

		for (; caratLine < lineCount; ++caratLine)
		{
			indexCount += d_lines[caratLine].d_length;

			if (index < indexCount)
			{
				return caratLine;
			}

		}

	}*/
	return 0;

	throw InvalidRequestException("MultiLineEditbox::getLineNumberFromIndex - Unable to identify a line from the given, invalid, index.");
}



/*************************************************************************
	Clear the current selection setting
*************************************************************************/
void RichEditbox::clearSelection(void)
{
	// perform action only if required.
	//if (getSelectionLength() != 0)
	{
		setSelection(d_caratPos, d_caratPos);
		d_dragAnchorPos=d_caratPos;
	}

}


/*************************************************************************
	Erase the currently selected text.
*************************************************************************/
void RichEditbox::eraseSelectedText(bool modify_text)
{
	//if (getSelectionLength() != 0)
	//{
	//	// setup new carat position and remove selection highlight.
	//	setCaratIndex(getSelectionStartIndex());

	//	// erase the selected characters (if required)
	//	if (modify_text)
	//	{
 //           String newText = getText();
 //           newText.erase(getSelectionStartIndex(), getSelectionLength());
 //           setText(newText);

	//		// trigger notification that text has changed.
	//		WindowEventArgs args(this);
	//		onTextChanged(args);
	//	}

	//	clearSelection();
	//}

}


/*************************************************************************
	Processing for backspace key
*************************************************************************/
void RichEditbox::handleBackspace(void)
{	if (!isReadOnly())
	{/*
		if (getSelectionLength() != 0)
		{
			eraseSelectedText();
		}
		else if (d_caratPos > 0)*/
		if (d_FormatedComponents.size()<=0)
		{
			return;
		}

		
		
		if (d_selectionStart!=d_selectionEnd)
		{
			DeleteSelectText();
		}else if (!isCaratOnAllTop())
		{
            size_t CurrentIndex=d_caratPos.d_ComponentIndex;
			size_t CurrentOffset=d_caratPos.d_ComponentOffset;
			
			bool bIsDelComponent=d_FormatedComponents[CurrentIndex]->HandleBackSpace(CurrentOffset);
			if (bIsDelComponent)
			{
				RichEditboxComponent* pOldComponent=d_FormatedComponents[CurrentIndex];
				if (pOldComponent!=NULL)
				{
					DelComponent(pOldComponent);
				}
				//d_FormatedComponents.erase(d_FormatedComponents.begin()+CurrentIndex);

				size_t newIndex=0;
				if (CurrentIndex>0)
				{
					newIndex=CurrentIndex-1;
				}
				
				size_t newOffset=0;
				if (d_FormatedComponents.size()>0)
				{
					size_t maxComponentOffset=d_FormatedComponents[newIndex]->GetMaxIndex();
					if (maxComponentOffset>0)
					{
						newOffset=maxComponentOffset;
					}
				}
				
				setCaratPos(CaratPos(newIndex,newOffset));
			}else
			{
				d_caratPos.d_ComponentOffset--;
				if (d_caratPos.d_ComponentOffset==0)
				{
					handleCharLeft(0);

				}
			}

			//WindowEventArgs args2(this);
			//onParseTextChanged(args2);
			

			
		}
		clearSelection();
		WindowEventArgs args(this);
		onTextChanged(args);

	}
}


/*************************************************************************
	Processing for Delete key
*************************************************************************/
void RichEditbox::handleDelete(void)
{
	if (!isReadOnly())
	{
		if (d_FormatedComponents.size()<=0)
		{
			return;
		}
		if (d_selectionStart!=d_selectionEnd)
		{
			DeleteSelectText();
		}else
		{
			if (!isCaratOnEnd())
			{
				handleCharRight(0);
			    handleBackspace();
			}
			
		}
		clearSelection();
		WindowEventArgs args(this);
		onTextChanged(args);
	}

}


/*************************************************************************
	Processing to move carat one character left
*************************************************************************/
void RichEditbox::handleCharLeft(uint sysKeys)
{
	static int count=0;
	count++;
	if (count==2)
	{
		int jjj=0;
	}
	size_t CurrenIndex=d_caratPos.d_ComponentIndex;
	size_t CurOffset=d_caratPos.d_ComponentOffset;

	size_t NewIndex=0;
	size_t NewOffset=0;

	
   if (d_FormatedComponents.size()>0)
   {
	   if (CurOffset>0)
	   {
		   NewIndex=CurrenIndex;
		   NewOffset=CurOffset-1;
	   }else
	   {
		   if (CurrenIndex>=1)
		   {
			   NewIndex=CurrenIndex-1;
			   size_t ComponentMaxOffset=d_FormatedComponents[NewIndex]->GetMaxIndex();
			   if (ComponentMaxOffset>=1)
			   {
				   NewOffset=ComponentMaxOffset-1;
			   }else
			   {
				   NewOffset=0;
			   }

		   }else
		   {
			   NewIndex=0;
			   NewOffset=0;
			   
		   }
	   }
   }
	CaratPos NewPos(NewIndex,NewOffset);
	setCaratPos(NewPos);
	if (sysKeys & Shift)
	{
		setSelection(d_dragAnchorPos, d_caratPos);
	}
	else
	{
		clearSelection();
	}

	

}


/*************************************************************************
	Processing to move carat one word left
*************************************************************************/
void RichEditbox::handleWordLeft(uint sysKeys)
{
	/*if (d_caratPos > 0)
	{
        setCaratIndex(TextUtils::getWordStartIdx(getText(), getCaratIndex()));
	}

	if (sysKeys & Shift)
	{
		setSelection(d_caratPos, d_dragAnchorIdx);
	}
	else
	{
		clearSelection();
	}*/

}


/*************************************************************************
	Processing to move carat one character right
*************************************************************************/
void RichEditbox::handleCharRight(uint sysKeys)
{
   /*if (d_caratPos < getText().length() - 1)
	{
		setCaratIndex(d_caratPos + 1);
	}

	if (sysKeys & Shift)
	{
		setSelection(d_caratPos, d_dragAnchorIdx);
	}
	else
	{
		clearSelection();
	}*/

	size_t CurrenIndex=d_caratPos.d_ComponentIndex;
	size_t CurOffset=d_caratPos.d_ComponentOffset;

	size_t NewIndex=0;
	size_t NewOffset=0;

	if (d_FormatedComponents.size()>0)
	{
		size_t ComponentMaxOffset=d_FormatedComponents[CurrenIndex]->GetMaxIndex();
		if (CurOffset<ComponentMaxOffset)
		{
			NewIndex=CurrenIndex;
			NewOffset=CurOffset+1;
		}
		else
		{
			if (CurrenIndex==d_FormatedComponents.size()-1)
			{
				NewIndex=CurrenIndex;
				NewOffset=CurOffset;
			}else
			{
				NewIndex=CurrenIndex+1;
				NewOffset=1;
			}
		}
	}
	
	CaratPos NewPos(NewIndex,NewOffset);
	setCaratPos(NewPos);

	if (sysKeys & Shift)
	{
		setSelection(d_dragAnchorPos,d_caratPos);
	}
	else
	{
		clearSelection();
	}

}


/*************************************************************************
	Processing to move carat one word right
*************************************************************************/
void RichEditbox::handleWordRight(uint sysKeys)
{
   /*if (d_caratPos < getText().length() - 1)
	{
        setCaratIndex(TextUtils::getNextWordStartIdx(getText(), getCaratIndex()));
	}

	if (sysKeys & Shift)
	{
		setSelection(d_caratPos, d_dragAnchorIdx);
	}
	else
	{
		clearSelection();
	}*/

}


/*************************************************************************
	Processing to move carat to the start of the text.
*************************************************************************/
void RichEditbox::handleDocHome(uint sysKeys)
{
	/*if (d_caratPos > 0)
	{
		setCaratIndex(0);
	}

	if (sysKeys & Shift)
	{
		setSelection(d_caratPos, d_dragAnchorIdx);
	}
	else
	{
		clearSelection();
	}*/

}


/*************************************************************************
	Processing to move carat to the end of the text
*************************************************************************/
void RichEditbox::handleDocEnd(uint sysKeys)
{
   /*if (d_caratPos < getText().length() - 1)
	{
       setCaratIndex(getText().length() - 1);
	}

	if (sysKeys & Shift)
	{
		setSelection(d_caratPos, d_dragAnchorIdx);
	}
	else
	{
		clearSelection();
	}*/


}


/*************************************************************************
	Processing to move carat to the start of the current line.
*************************************************************************/
void RichEditbox::handleLineHome(uint sysKeys)
{
	/*size_t line = getLineNumberFromIndex(d_caratPos);

	if (line < d_lines.size())
	{
		size_t lineStartIdx = d_lines[line].d_startIdx;

		if (d_caratPos > lineStartIdx)
		{
			setCaratIndex(lineStartIdx);
		}

		if (sysKeys & Shift)
		{
			setSelection(d_caratPos, d_dragAnchorIdx);
		}
		else
		{
			clearSelection();
		}

	}*/

}


/*************************************************************************
	Processing to move carat to the end of the current line
*************************************************************************/
void RichEditbox::handleLineEnd(uint sysKeys)
{
	/*size_t line = getLineNumberFromIndex(d_caratPos);

	if (line < d_lines.size())
	{
		size_t lineEndIdx = d_lines[line].d_startIdx + d_lines[line].d_length - 1;

		if (d_caratPos < lineEndIdx)
		{
			setCaratIndex(lineEndIdx);
		}

		if (sysKeys & Shift)
		{
			setSelection(d_caratPos, d_dragAnchorIdx);
		}
		else
		{
			clearSelection();
		}

	}*/

}


/*************************************************************************
	Processing to move carat up a line.
*************************************************************************/
void RichEditbox::handleLineUp(uint sysKeys)
{
	/*size_t caratLine = getLineNumberFromIndex(d_caratPos);

	if (caratLine > 0)
	{
        float caratPixelOffset = getFont()->getTextExtent(getText().substr(d_lines[caratLine].d_startIdx, d_caratPos - d_lines[caratLine].d_startIdx));

		--caratLine;

        size_t newLineIndex = getFont()->getCharAtPixel(getText().substr(d_lines[caratLine].d_startIdx, d_lines[caratLine].d_length), caratPixelOffset);

		setCaratIndex(d_lines[caratLine].d_startIdx + newLineIndex);
	}

	if (sysKeys & Shift)
	{
		setSelection(d_caratPos, d_dragAnchorIdx);
	}
	else
	{
		clearSelection();
	}*/

}


/*************************************************************************
	Processing to move carat down a line.
*************************************************************************/
void RichEditbox::handleLineDown(uint sysKeys)
{
	/*size_t caratLine = getLineNumberFromIndex(d_caratPos);

	if ((d_lines.size() > 1) && (caratLine < (d_lines.size() - 1)))
	{
        float caratPixelOffset = getFont()->getTextExtent(getText().substr(d_lines[caratLine].d_startIdx, d_caratPos - d_lines[caratLine].d_startIdx));

		++caratLine;

        size_t newLineIndex = getFont()->getCharAtPixel(getText().substr(d_lines[caratLine].d_startIdx, d_lines[caratLine].d_length), caratPixelOffset);

		setCaratIndex(d_lines[caratLine].d_startIdx + newLineIndex);
	}

	if (sysKeys & Shift)
	{
		setSelection(d_caratPos, d_dragAnchorIdx);
	}
	else
	{
		clearSelection();
	}*/

}


/*************************************************************************
	Processing to insert a new line / paragraph.
*************************************************************************/
void RichEditbox::handleNewLine(uint /*sysKeys*/)
{
	//if (!isReadOnly())
	//{
	//	// erase selected text
	//	eraseSelectedText();

	//	// if there is room
 //      if (getText().length() - 1 < d_maxTextLen)
	//	{
 //           String newText = getText();
 //           newText.insert(getCaratIndex(), 1, 0x0a);
 //           setText(newText);

	//		d_caratPos++;

	//		WindowEventArgs args(this);
	//		onTextChanged(args);
	//	}

	//}

}


/*************************************************************************
    Processing to move caret one page up
*************************************************************************/
void RichEditbox::handlePageUp(uint sysKeys)
{
   /* size_t caratLine = getLineNumberFromIndex(d_caratPos);
    size_t nbLine = static_cast<size_t>(getTextRenderArea().getHeight() / getFont()->getLineSpacing());
    size_t newline = 0;
    if (nbLine < caratLine)
    {
        newline = caratLine - nbLine;
    }
    setCaratIndex(d_lines[newline].d_startIdx);
    if (sysKeys & Shift)
    {
        setSelection(d_caratPos, d_selectionEnd);
    }
    else
    {
        clearSelection();
    }
    ensureCaratIsVisible();*/
}


/*************************************************************************
    Processing to move caret one page down
*************************************************************************/
void RichEditbox::handlePageDown(uint sysKeys)
{
   /* size_t caratLine = getLineNumberFromIndex(d_caratPos);
    size_t nbLine =  static_cast<size_t>(getTextRenderArea().getHeight() / getFont()->getLineSpacing());
    size_t newline = caratLine + nbLine;
    if (d_lines.size() > 0)
    {
        newline = newline < d_lines.size() - 1 ? newline : d_lines.size() -1;
    }
    setCaratIndex(d_lines[newline].d_startIdx + d_lines[newline].d_length - 1);
    if (sysKeys & Shift)
    {
        setSelection(d_selectionStart, d_caratPos);
    }
    else
    {
        clearSelection();
    }
    ensureCaratIsVisible();*/
}


/*************************************************************************
	Handler for when a mouse button is pushed
*************************************************************************/
void RichEditbox::onMouseButtonDown(MouseEventArgs& e)
{
	// base class handling

	Window::onMouseButtonDown(e);
	
	if (d_destructionStarted)
	{
		e.handled++;
		return;
	}
	downPosition = e.position;
	CaratPos NewPos=getComponentInfFromPosition(e.position);
	if (e.button == LeftButton)
	{
		// grab inputs
		if (captureInput())
		{
			// handle mouse down
			
			d_dragging = true;
			
			setCaratPos(NewPos);
			clearSelection();
			
		}

		
	}
	
	bool bClickLink=false;  //bClickLink????\u2211\u00F2\u03BC?a??\u00E0?\uFFE5\u03A9\u201D
	RichEditboxComponent* pClickCpn=GetComponentByPos(e.position);
    
    if (pClickCpn==NULL&&d_IsClickSelectLine)
    {
        pClickCpn=GetLinkTextOnPos(e.position);
    }
    
	if (pClickCpn)
	{
        pClickCpn->onMouseEnter();
		bool bNeedRedraw=pClickCpn->onMouseButtonDown(e);
		if (bNeedRedraw)
		{
			bClickLink=true;
			invalidate();
		}
		
	}
    
	
	
	if (bClickLink)
	{
		++e.handled;
	}else
	{
		if (d_PassMouseClick)
		{
			e.handled=0;
		}

	}

}


/*************************************************************************
	Handler for when mouse button is released
*************************************************************************/
void RichEditbox::onMouseButtonUp(MouseEventArgs& e)
{
	// yangbin--如果滑动了，则返回不触发buttonup
	if (d_recognizerManager->onMouseButtonUp(e)) {
		return;
	}
	

	//RichEditboxComponent* pClickCpn=GetComponentByPos(e.position);
	RichEditboxComponent* pClickCpn = GetComponentByPos(downPosition);
    
    if (pClickCpn==NULL&&d_IsClickSelectLine)
    {
		pClickCpn = GetLinkTextOnPos(downPosition);
    }
	ComponentListIt formatIt = d_FormatedComponents.begin();
	for (; formatIt != d_FormatedComponents.end(); ++formatIt)
	{
		RichEditboxComponent* pCom = *formatIt;
		if (!pCom)
			continue;
	
		if (( pClickCpn && pCom != pClickCpn && pCom->isPushed()) || (pClickCpn == NULL && pCom->isPushed()))
		{
			bool bNeedRedraw = pCom->onMouseButtonUp(e);
			if (bNeedRedraw)
			{
				invalidate();
			}
		}
	}
	if (pClickCpn!=NULL)
	{
		bool bNeedRedraw = pClickCpn->onMouseButtonUp(e);
		if (bNeedRedraw)
		{
			invalidate();
		}
	}

	// base class processing
	Window::onMouseButtonUp(e);
	
	if (e.button == LeftButton)
	{
		releaseInput();
		++e.handled;
		d_dragging=false;
		d_dragAnchorPos=d_caratPos;
	}

}


/*************************************************************************
	Handler for when mouse button is double-clicked
*************************************************************************/
void RichEditbox::onMouseDoubleClicked(MouseEventArgs& e)
{
	// base class processing
	Window::onMouseDoubleClicked(e);

	if (e.button == LeftButton)
	{
  //      d_dragAnchorIdx = TextUtils::getWordStartIdx(getText(), (d_caratPos == getText().length()) ? d_caratPos : d_caratPos + 1);
  //      d_caratPos      = TextUtils::getNextWordStartIdx(getText(), d_caratPos);

		//// perform actual selection operation.
		//setSelection(d_dragAnchorIdx, d_caratPos);

		++e.handled;
	}

}


/*************************************************************************
	Handler for when mouse button is triple-clicked.
*************************************************************************/
void RichEditbox::onMouseTripleClicked(MouseEventArgs& e)
{
	// base class processing
	Window::onMouseTripleClicked(e);

	if (e.button == LeftButton)
	{
		//size_t caratLine = getLineNumberFromIndex(d_caratPos);
		//size_t lineStart = d_lines[caratLine].d_startIdx;

		//// find end of last paragraph
  //      String::size_type paraStart = getText().find_last_of(d_lineBreakChars, lineStart);

		//// if no previous paragraph, selection will start at the beginning.
		//if (paraStart == String::npos)
		//{
		//	paraStart = 0;
		//}

		//// find end of this paragraph
  //      String::size_type paraEnd = getText().find_first_of(d_lineBreakChars, lineStart);

		//// if paragraph has no end, which actually should never happen, fix the
		//// erroneous situation and select up to end at end of text.
		//if (paraEnd == String::npos)
		//{
  //          String newText = getText();
  //          newText.append(1, '\n');
  //          setText(newText);

  //          paraEnd = getText().length() - 1;
		//}

		//// set up selection using new values.
		//d_dragAnchorIdx = paraStart;
		//setCaratIndex(paraEnd);
		//setSelection(d_dragAnchorIdx, d_caratPos);
		++e.handled;
	}

}


/*************************************************************************
	Handler for when mouse moves in the window.
*************************************************************************/
void RichEditbox::onMouseMove(MouseEventArgs& e)
{
	Window::onMouseMove(e);
}
    
bool RichEditbox::onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer)
{
    // base class processing
    Window::onMouseDrag(pRecognizer);
    MouseEventArgs* e = (MouseEventArgs*)pRecognizer->GetEvent();
    
    if (d_dragging&&!isReadOnly())        
    {
		if (d_dragging && e)
        {
            CaratPos curMousePos=getComponentInfFromPosition(e->position);
            setSelection(d_dragAnchorPos,curMousePos);
        }
        
    }else
    {
        Scrollbar* vertScrollbar = getVertScrollbar();
        if (vertScrollbar&&d_readOnly&&d_wordWrap)
        {
            vertScrollbar->onMouseDrag(pRecognizer);
        }
    }
    
    UpdataMouseOnComponent();
    return true;
}


/*************************************************************************
	Handler for when capture is lost.
*************************************************************************/
void RichEditbox::onCaptureLost(WindowEventArgs& e)
{
	d_dragging = false;
	d_bEnterKeyIsPess=false;

	// base class processing
	Window::onCaptureLost(e);
	if (d_MouseOnComponent!=NULL)
	{
		d_MouseOnComponent->onMouseLeave();
		d_MouseOnComponent=NULL;
		invalidate();
	}

	++e.handled;
}


/*************************************************************************
	Handler for when character (printable keys) are typed
*************************************************************************/
void RichEditbox::onCharacter(KeyEventArgs& e)
{
    // NB: We are not calling the base class handler here because it propogates
    // inputs back up the window hierarchy, whereas, as a consumer of key
    // events, we want such propogation to cease with us regardless of whether
    // we actually handle the event.

    // fire event.
    fireEvent(EventCharacterKey, e, Window::EventNamespace);

	// only need to take notice if we have focus
	if (e.handled == 0 && /*hasInputFocus() &&*/ !isReadOnly() &&
        getFont()->isCodepointAvailable(e.codepoint))
	{
		//// if there is room
        //o?\u2264??\u00F3\uFFE5??\u00F7\u2211???
		if (!d_readOnly&&d_CurTextLen>=d_maxTextLen
			&&d_selectionStart==d_selectionEnd)
		{
			onEditboxFullEvent(e);
			return ;
		}
		
		if (true/*getText().length() - 1 < d_maxTextLen*/)
		{
			if (d_selectionStart!=d_selectionEnd)
			{
				DeleteSelectText();
			}
			size_t CurrentIndex=d_caratPos.d_ComponentIndex;
			size_t CurrentOffset=d_caratPos.d_ComponentOffset;
			String InsertString(1,e.codepoint);
            

			if (d_FormatedComponents.size()>0)
			{
				

				RichEditboxComponent* pAddComponent=NULL;
				pAddComponent=d_FormatedComponents[CurrentIndex]->HandleChar(CurrentOffset,InsertString,d_ColourRect);
				if (NULL!=pAddComponent)//?\u221E\u201Ca\u2013\u2013\u2013?\u201C?\u2264?\u2211\u00F7?\u00B1
				{
					RichEditboxTextComponent* pNewTextCopn=new RichEditboxTextComponent;
					pNewTextCopn->setColours(d_ColourRect);
					pNewTextCopn->setText(InsertString);
					pNewTextCopn->setFont(getFont());
					d_FormatedComponents.insert(d_FormatedComponents.begin()+CurrentIndex+1,pNewTextCopn);
					
					if (pAddComponent->GetType()==RichEditboxComponentType_Text)
					{
						RichEditboxTextComponent* pAddTextCpn=(RichEditboxTextComponent*)pAddComponent;
						if (pAddTextCpn!=NULL&&pAddTextCpn->getText().size()>0)
						{
							d_FormatedComponents.insert(d_FormatedComponents.begin()+CurrentIndex+2,pAddComponent);
							setCaratPos(CaratPos(CurrentIndex+1,InsertString.size()));
						}else
						{
							delete pAddTextCpn;
							setCaratPos(CaratPos(CurrentIndex+1,InsertString.size()));
						}
					}else
					{
						d_FormatedComponents.insert(d_FormatedComponents.begin()+CurrentIndex+2,pAddComponent);
						setCaratPos(CaratPos(CurrentIndex+1,InsertString.size()));

					}
					

				}else//?\u221E\u201Ca\u2013\u2013\u2264a\u2013?\u201C?\u2264?\u2211\u00F7?\u00B1
				{
					if (d_FormatedComponents[CurrentIndex]->GetType() != RichEditboxComponentType_Text)
					{
						RichEditboxTextComponent* pNewTextCopn=new RichEditboxTextComponent;
						pNewTextCopn->setColours(d_ColourRect);
						pNewTextCopn->setText(InsertString);
						pNewTextCopn->setFont(getFont());
						if (CurrentOffset==0)  //\u2018?\u2264a?\u2026\u2211\u00F7\u220F\u00F3??o?\u03BC??\u2211?\u00B1\u2018?\u220F\u221A??o??\u221Eo\u201D
						{
							
							d_FormatedComponents.insert(d_FormatedComponents.begin()+CurrentIndex,pNewTextCopn);
							setCaratPos(CaratPos(CurrentIndex,InsertString.size()));
							
						}else
						{
							d_FormatedComponents.insert(d_FormatedComponents.begin()+CurrentIndex+1,pNewTextCopn);
							setCaratPos(CaratPos(CurrentIndex+1,InsertString.size()));
						}
					}
					else
					{
						//
						setCaratPos(CaratPos(CurrentIndex, CurrentOffset + InsertString.size()));
					}
				}
			}else   //?\u00ECo\u201D\u03BC?\u201Ca\u220F????\u00F7?\u00B1
			{
                RichEditboxTextComponent* pNewTextCopn = new RichEditboxTextComponent;
				pNewTextCopn->setColours(d_ColourRect);
				pNewTextCopn->setText(InsertString);
				pNewTextCopn->setFont(getFont());
				d_FormatedComponents.push_back(pNewTextCopn);
				setCaratPos(CaratPos(0,1));
			}

			WindowEventArgs args(this);
			onTextChanged(args);

            ++e.handled;
		}
	   clearSelection();

	}

}


/*************************************************************************
	Handler for when non-printable keys are typed.
*************************************************************************/
void RichEditbox::onKeyDown(KeyEventArgs& e)
{
    // NB: We are not calling the base class handler here because it propogates
    // inputs back up the window hierarchy, whereas, as a consumer of key
    // events, we want such propogation to cease with us regardless of whether
    // we actually handle the event.

    // fire event.
    fireEvent(EventKeyDown, e, Window::EventNamespace);

	if (e.handled == 0 && /*hasInputFocus() &&*/ !isReadOnly())
	{
		WindowEventArgs args(this);
		switch (e.scancode)
		{
		case Key::LeftShift:
		case Key::RightShift:
			//if (d_selectionStart!=d_selectionEnd)
			{
				//d_dragAnchorIdx = getCaratIndex();
				d_selectionStart=getCaratPos();
				d_selectionEnd=getCaratPos();
			}
			break;

		case Key::Backspace:
			handleBackspace();
			break;

		case Key::Delete:
			handleDelete();
			break;

		case Key::Return:
		case Key::NumpadEnter:
			{
				if (!d_bEnterKeyIsPess)
				{
					switch (d_TextAcceptMode)
					{
					case eTextAcceptMode_Enter:
						{
//							if (::GetKeyState(VK_CONTROL) < 0||::GetKeyState(VK_SHIFT) < 0)
//							{
//								if (d_wordWrap)
//								{
//									handleNewLine(e.sysKeys);
//								}
//
//							}else
							{
								onTextAcceptedEvent(args);
							}
						}
						break;
					case eTextAcceptMode_CtrlEnter:
						{
//							if (::GetKeyState(VK_CONTROL) < 0)
//							{
//								onTextAcceptedEvent(args);
//							}else
							{
								if (d_wordWrap)
								{
									handleNewLine(e.sysKeys);
								}
							}
						}
						break;
					case eTextAcceptMode_OnlyEnter:
						{

							if (d_wordWrap)
							{
								handleNewLine(e.sysKeys);
							}
							else
							{
								onTextAcceptedEvent(args);
							}
						}
						break;
					}

				}
				d_bEnterKeyIsPess=true;
				
			}
			break;
		case Key::ArrowLeft:
//			if (::GetKeyState(VK_CONTROL) < 0)
//			{
//				handleWordLeft(e.sysKeys);
//			}
//			else
			{
				handleCharLeft(e.sysKeys);
			}
			break;

		case Key::ArrowRight:
//			if (::GetKeyState(VK_CONTROL) < 0)
//			{
//				handleWordRight(e.sysKeys);
//			}
//			else
			{
				handleCharRight(e.sysKeys);
			}
			break;

		case Key::ArrowUp:
			{
				WindowEventArgs upEvent(this);
				fireEvent(EventUpKeyDown,upEvent,EventNamespace);
				handleLineUp(e.sysKeys);
			}
			break;

		case Key::ArrowDown:
			{
				WindowEventArgs DownEvent(this);
				fireEvent(EventDownKeyDown,DownEvent,EventNamespace);
				handleLineDown(e.sysKeys);
			}
			break;

		case Key::Home:
			if (e.sysKeys & Control)
			{
				handleDocHome(e.sysKeys);
			}
			else
			{
				handleLineHome(e.sysKeys);
			}
			break;

		case Key::End:
			if (e.sysKeys & Control)
			{
				handleDocEnd(e.sysKeys);
			}
			else
			{
				handleLineEnd(e.sysKeys);
			}
			break;

        case Key::PageUp:
            handlePageUp(e.sysKeys);
            break;

        case Key::PageDown:
            handlePageDown(e.sysKeys);
            break;
        default:
            return;
		}

		++e.handled;
	}

}

void RichEditbox::onKeyUp(KeyEventArgs& e)
{
	// NB: We are not calling the base class handler here because it propogates
	// inputs back up the window hierarchy, whereas, as a consumer of key
	// events, we want such propogation to cease with us regardless of whether
	// we actually handle the event.

	// fire event.

	if (e.handled == 0 && /*hasInputFocus() &&*/ !isReadOnly())
	{
		WindowEventArgs args(this);
		switch (e.scancode)
		{
		case Key::Return:
		case Key::NumpadEnter:
			{
				d_bEnterKeyIsPess=false;
			}
		    break;
		}
		++e.handled;
	}

}


/*************************************************************************
	Handler for when text is programmatically changed.
*************************************************************************/
void RichEditbox::onTextChanged(WindowEventArgs& e)
{
    // clear selection
    clearSelection();
    // layout new text
    format();
    ensureCaratIsVisible();
	Window::onTextChanged(e);

    ++e.handled;
}


/*************************************************************************
	Handler for when widget size is changed.
*************************************************************************/
void RichEditbox::onSized(WindowEventArgs& e)
{
	EditModeFormat();
	HandleEnd();
	
	// base class handling
	Window::onSized(e);

	++e.handled;
}


/*************************************************************************
	Handler for mouse wheel changes
*************************************************************************/
void RichEditbox::onMouseWheel(MouseEventArgs& e)
{
	// base class processing.
	Window::onMouseWheel(e);


    Scrollbar* vertScrollbar = getVertScrollbar();
    Scrollbar* horzScrollbar = getHorzScrollbar();
	if (!vertScrollbar->isVisible())
	{
		return;
	}

	if (vertScrollbar->isVisible() && (vertScrollbar->getDocumentSize() > vertScrollbar->getPageSize()))
	{
		vertScrollbar->setScrollPosition(vertScrollbar->getScrollPosition() + vertScrollbar->getStepSize() * -e.wheelChange);
	}
	else if (horzScrollbar->isVisible() && (horzScrollbar->getDocumentSize() > horzScrollbar->getPageSize()))
	{
		horzScrollbar->setScrollPosition(horzScrollbar->getScrollPosition() + horzScrollbar->getStepSize() * -e.wheelChange);
	}

	++e.handled;
}
    
void RichEditbox::onMouseSlide(CEGUI::MouseEventArgs &e)
{
    Scrollbar* vertScrollbar = getVertScrollbar();
    if (vertScrollbar!=NULL) {
        vertScrollbar->onMouseSlide(e);
    }
}
    
    



/*************************************************************************
	Handler called when the read-only state of the edit box changes
*************************************************************************/
void RichEditbox::onReadOnlyChanged(WindowEventArgs& e)
{
	fireEvent(EventReadOnlyModeChanged, e, EventNamespace);
}


/*************************************************************************
	Handler called when the word wrap mode for the the edit box changes
*************************************************************************/
void RichEditbox::onWordWrapModeChanged(WindowEventArgs& e)
{
	fireEvent(EventWordWrapModeChanged, e, EventNamespace);
}


/*************************************************************************
	Handler called when the maximum text length for the edit box changes
*************************************************************************/
void RichEditbox::onMaximumTextLengthChanged(WindowEventArgs& e)
{
	fireEvent(EventMaximumTextLengthChanged, e, EventNamespace);
}


/*************************************************************************
	Handler called when the carat moves.
*************************************************************************/
void RichEditbox::onCaratMoved(WindowEventArgs& e)
{
	invalidate();
	fireEvent(EventCaratMoved, e, EventNamespace);
}


/*************************************************************************
	Handler called when the text selection changes
*************************************************************************/
void RichEditbox::onTextSelectionChanged(WindowEventArgs& e)
{
	invalidate();
	fireEvent(EventTextSelectionChanged, e, EventNamespace);
}


/*************************************************************************
	Handler called when the edit box is full
*************************************************************************/
void RichEditbox::onEditboxFullEvent(WindowEventArgs& e)
{
	fireEvent(EventEditboxFull, e, EventNamespace);
}


/*************************************************************************
	Handler called when the 'always show' setting for the vertical
	scroll bar changes.
*************************************************************************/
void RichEditbox::onVertScrollbarModeChanged(WindowEventArgs& e)
{
	invalidate();
	fireEvent(EventVertScrollbarModeChanged, e, EventNamespace);
}


/*************************************************************************
	Handler called when 'always show' setting for the horizontal scroll
	bar changes.
*************************************************************************/
void RichEditbox::onHorzScrollbarModeChanged(WindowEventArgs& e)
{
	invalidate();
	fireEvent(EventHorzScrollbarModeChanged, e, EventNamespace);
}


/*************************************************************************
	Return whether the text in the edit box will be word-wrapped.
*************************************************************************/
bool RichEditbox::isWordWrapped(void) const
{
	return d_wordWrap;
}


/*************************************************************************
	Add new properties for this class
*************************************************************************/
void RichEditbox::addRichEditboxProperties(void)
{
	addProperty(&d_readOnlyProperty);
	addProperty(&d_wordWrapProperty);
	/*addProperty(&d_caratIndexProperty);
	addProperty(&d_selectionStartProperty);
	addProperty(&d_selectionLengthProperty);*/
	addProperty(&d_maxTextLengthProperty);
	addProperty(&d_selectionBrushProperty);
	addProperty(&d_forceVertProperty);
	addProperty(&d_BackGroundEnableProperty);
	addProperty(&d_LineSpaceProperty);
	addProperty(&d_TopAfterLoadProperty);
}

/*************************************************************************
    Handler for scroll position changes.
*************************************************************************/
bool RichEditbox::handle_scrollChange(const EventArgs&)
{
    // simply trigger a redraw of the Listbox.
	if (d_windowRenderer != 0)
	{
		RichEditboxWindowRenderer* wr = (RichEditboxWindowRenderer*)d_windowRenderer;
		wr->FixStartDrawLineIdx();
	}

	if (d_readOnly&&d_ScrobarLockEnable)
	{
		EventArgs e;
		Scrollbar* vertScrollbar = getVertScrollbar();
		if (vertScrollbar)
		{
			bool bThumbOnEnd=vertScrollbar->isThumbOnEnd();
			if (bThumbOnEnd)
			{
				handleThumbTrackEnd(e);

			}else
			{
				handleThumbTrackStarted(e);

			}
		}
	}
	invalidate();
    return true;
}

bool RichEditbox::handle_horscrollVisChange(const EventArgs& args)
{
	throw InvalidRequestException("handle_horscrollVisError");//?\uFFE5\u03BC\u03A9\uFFE5\u00E0\uFFE5?????\u03BC???\u00F7???\u00F2?\uFFE1\u00A8\u00B1\uFFE1?\u00F9call stack
	return true;
}

/*************************************************************************
    Return a pointer to the vertical scrollbar component widget.
*************************************************************************/
Scrollbar* RichEditbox::getVertScrollbar() const
{
    return static_cast<Scrollbar*>(WindowManager::getSingleton().getWindow(
                                   getName() + VertScrollbarNameSuffix));
}

/*************************************************************************
	Return whether the vertical scroll bar is always shown.
*************************************************************************/
bool RichEditbox::isVertScrollbarAlwaysShown(void) const
{
	return d_forceVertScroll;
}

/*************************************************************************
    Return a pointer to the horizontal scrollbar component widget.
*************************************************************************/
Scrollbar* RichEditbox::getHorzScrollbar() const
{
    return static_cast<Scrollbar*>(WindowManager::getSingleton().getWindow(
                                   getName() + HorzScrollbarNameSuffix));
}

/*************************************************************************
    Get the text rendering area
*************************************************************************/
Rect RichEditbox::getTextRenderArea() const
{
    if (d_windowRenderer != 0)
    {
        RichEditboxWindowRenderer* wr = (RichEditboxWindowRenderer*)d_windowRenderer;
		return wr->getTextRenderArea();
    }
    else
    {
        //return getTextRenderArea_impl();
        throw InvalidRequestException("MultiLineEditbox::getTextRenderArea - This function must be implemented by the window renderer module");
    }
}

const Image* RichEditbox::getSelectionBrushImage() const
{
    return d_selectionBrush;
}

void RichEditbox::setSelectionBrushImage(const Image* image)
{
    d_selectionBrush = image;
    invalidate();
}

/*************************************************************************
	Set whether the vertical scroll bar should always be shown.
*************************************************************************/
void RichEditbox::setShowVertScrollbar(bool setting)
{
	if (d_forceVertScroll != setting)
	{
		d_forceVertScroll = setting;

		configureScrollbars();
		WindowEventArgs args(this);
		onVertScrollbarModeChanged(args);
	}
}


void	RichEditbox::formatTextImpl(const String &textFormat)
{

}

void RichEditbox::format()
{

	if (d_AutoChangeEmotionSize)
	{
		d_EmotionScale.d_x=1.0f;
		d_EmotionScale.d_y=1.0f;

		SetAllEmotionScale(d_EmotionScale);
	}

	if (isReadOnly())
	{
		ReadOnlyFormat();
	}else
	{
		EditModeFormat();
	}

	int emotionLineNum=GetEmotionLineNum();

	Vector2 scale=GetEmotionScaleByLineNum(emotionLineNum,d_lines.size());

	if (d_AutoChangeEmotionSize&&scale!=d_EmotionScale)
	{
		d_EmotionScale=scale;
		SetAllEmotionScale(scale);

		if (isReadOnly())
		{
			ReadOnlyFormat();
		}else
		{
			EditModeFormat();
		}
	}

	if (!isReadOnly())
	{
		d_UpdateCpnList.clear();

		int count=0;
		size_t cpnNum=d_FormatedComponents.size();
		for (size_t i=0;i<cpnNum;++i)
		{
			count+=d_FormatedComponents[i]->GetCharCount();

			if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Emotion)
			{
				d_UpdateCpnList.push_back(i);
			}
		}
		d_CurTextLen=count;
	}

	if (isReadOnly()&&d_windowRenderer != 0)
	{
		RichEditboxWindowRenderer* wr = (RichEditboxWindowRenderer*)d_windowRenderer;
		if (wr!=NULL)
		{
			wr->FixStartDrawLineIdx();
		}
		
	}

}

void RichEditbox::ReadOnlyFormat()
{
	/*if (d_Components.size()<=0)
	{
	return;
	}*/

	if (d_IsLockState)
	{
		return;
	}

	d_MaxLineHeight=0.0f;

	ParseControlText();
	d_TempComponents.resize(0);
	size_t LineCount=d_lines.size();
	size_t LastLineCopnStartIndex=0;
	size_t LastLineCopnCount=0;
	if (LineCount>0)
	{
		LastLineCopnStartIndex=d_lines[LineCount-1].d_startComponentIdx;
		LastLineCopnCount=d_lines[LineCount-1].d_ComponentCount;
		d_lines.pop_back();

		ComponentListIt formatIt=d_FormatedComponents.begin()+LastLineCopnStartIndex;
		for (;formatIt!=d_FormatedComponents.end();++formatIt)
		{
			d_TempComponents.push_back(*formatIt);
		}

		/*ComponentListIt formatIt2=d_FormatedComponents.begin()+LastLineCopnStartIndex;
		for (;formatIt2!=d_FormatedComponents.end();++formatIt2)
		{
			d_FormatedComponents.erase(formatIt2);
			formatIt2--;
		}		*/
		size_t newSize=LastLineCopnStartIndex;
		d_FormatedComponents.resize(newSize);

	}else
	{
		ComponentListIt formatIt=d_FormatedComponents.begin();
		for (;formatIt!=d_FormatedComponents.end();++formatIt)
		{
			d_TempComponents.push_back(*formatIt);
		}
		d_FormatedComponents.clear();
	}
	
	//d_FormatedComponents.clear();

	//d_CurrentLine.d_ComponentCount=0;
	//d_CurrentLine.d_startComponentIdx=LastLineCopnStartIndex;
	
	Rect TextRenderArea=getTextRenderArea();

	
	
	float ClipWidth=TextRenderArea.getWidth();
	if (!d_wordWrap)
	{
		ClipWidth=9999999999999999999999999999.0f;
	}
	float CipHeight=TextRenderArea.getHeight();

	if (ClipWidth<0.0f||CipHeight<0.0f)
	{
		return;
	}
	
	float curLineSpareWide=ClipWidth;  //\u03BC\u00B1?\u221E\u2013\u2013\u03BC?
	size_t curLineStartComponentIndex=LastLineCopnStartIndex;
	size_t curLineComponentCount=0;
	
	//size_t count=d_Components.size();
	//ComponentListIt it=d_Components.begin();
	for (size_t i=0;i<d_TempComponents.size();i++)
	{
		
		if (d_TempComponents[i]->GetType()==RichEditboxComponentType_Break)
		{
			d_FormatedComponents.push_back(d_TempComponents[i]);

			LineInfo line;
			line.d_startComponentIdx=curLineStartComponentIndex;
			line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex;
			if (line.d_ComponentCount==0)
			{
				line.d_ComponentCount=1;
			}
			d_lines.push_back(line);
			curLineStartComponentIndex=d_FormatedComponents.size();
			curLineSpareWide=ClipWidth;
			continue;
		}
		float componentWidth=d_TempComponents[i]->getPixelSize().d_width;
		float componentHeight=d_TempComponents[i]->getPixelSize().d_height;
		if (componentWidth>curLineSpareWide)
		{
			if (d_TempComponents[i]->canSplit())
			{
				RichEditboxComponent* NewComponent=d_TempComponents[i]->split(curLineSpareWide,false);
				
				//d_FormatedComponents.push_back(d_TempComponents[i]);
				if (d_TempComponents[i]->GetCharCount()>0)
				{
					d_FormatedComponents.push_back(d_TempComponents[i]);
				}else
				{
					delete d_TempComponents[i];
					d_TempComponents.erase(d_TempComponents.begin()+i);

					if (i>0)
					{
						i--;
					}else
					{
						//i=0;
						d_FormatedComponents.push_back(NewComponent);
						NewComponent = NULL;

						char tmp[256] = {};
						sprintf(tmp, "RichEditbox split failed: name:%s width:%g\n", getName().c_str(), ClipWidth);
						CEGUI_LOGERR(tmp);
						break;
					}
				}
				
				
				LineInfo line;
				line.d_startComponentIdx=curLineStartComponentIndex;
				line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex;
				d_lines.push_back(line);

				curLineSpareWide=ClipWidth;
				
				curLineStartComponentIndex=d_FormatedComponents.size();
				
				if ((i==d_TempComponents.size()-1)||d_TempComponents.size()==0)
				{
					d_TempComponents.push_back(NewComponent);
				}else
				{
					d_TempComponents.insert(d_TempComponents.begin()+i+1,NewComponent);

				}
				
				
			}else
			{
				if(componentWidth<=ClipWidth)
				{
					d_FormatedComponents.push_back(d_TempComponents[i]);

					LineInfo line;
					line.d_startComponentIdx=curLineStartComponentIndex;
					line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex-1;
					d_lines.push_back(line);
					curLineStartComponentIndex=d_FormatedComponents.size()-1;
					curLineSpareWide=ClipWidth-componentWidth;
					if ( i==d_TempComponents.size()-1)
					{
						LineInfo line;
						line.d_startComponentIdx=curLineStartComponentIndex;
						line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex;
						d_lines.push_back(line);
					}
				}

			}
		}else
		{
			d_FormatedComponents.push_back(d_TempComponents[i]);
			//if (d_lines.size()==0)
			{
				LineInfo line;
				line.d_startComponentIdx=curLineStartComponentIndex;
				line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex;
				if ( i==d_TempComponents.size()-1)
				{
					d_lines.push_back(line);
					curLineStartComponentIndex=d_FormatedComponents.size();
				}
				
			}
			curLineSpareWide=curLineSpareWide-componentWidth;
		}
		
	}
	d_TempComponents.resize(0);

	CheckLineCount();  //o?\u2264??\u00F3\uFFE5?\u03BC?\u2013\u2013??
    
	float ExtentWidth=0.0f;
	float Totalheight=0.0f;
	for (size_t i=0;i<d_lines.size();i++)
	{
		Size LineSize=GetLineSize(i);
		if (LineSize.d_width>ExtentWidth)
		{
			ExtentWidth=LineSize.d_width;
		}

		if (LineSize.d_height>d_MaxLineHeight)
		{
			d_MaxLineHeight=LineSize.d_height;
		}

		d_lines[i].d_YOffset=Totalheight;

		Totalheight+=LineSize.d_height;
	}

	//d_ParseText=GenerateParseText(false);

	d_TotalHeight=Totalheight;
	d_widestExtent=ExtentWidth;

	if (d_AutoHorCenter)
	{
		float renderAreaHeight=TextRenderArea.getHeight();
		d_DrawYOffset=(renderAreaHeight-d_TotalHeight)/2.0f;
	}
	
	configureScrollbars();
	invalidate();

}

void RichEditbox::EditModeFormat(bool bConfigScrollBar)
{
	/*if (d_Components.size()<=0)
	{
	return;
	}*/

	/*if (d_IsLockState)
	{
		return;
	}*/
	
	Rect TextRenderArea=getTextRenderArea();
	if (TextRenderArea.getWidth()<0.0f||TextRenderArea.getHeight()<0.0f)
	{
		return;

	}

	d_MaxLineHeight=0.0f;

	ParseControlText();

	d_TempComponents.resize(0);
	ComponentListIt formatIt=d_FormatedComponents.begin();
	for (;formatIt!=d_FormatedComponents.end();++formatIt)
	{
		d_TempComponents.push_back(*formatIt);
	}
	
	
	
	d_FormatedComponents.clear();
	d_lines.clear();
	d_CurrentLine.d_ComponentCount=0;
	d_CurrentLine.d_startComponentIdx=0;
	
	

	
	
	float ClipWidth=TextRenderArea.getWidth();
	if (!d_wordWrap)
	{
		ClipWidth=9999999999999999999999999999.0f;
	}
	float CipHeight=TextRenderArea.getHeight();

	if (ClipWidth<0.0f||CipHeight<0.0f)
	{
		return;
	}
	
	float curLineSpareWide=ClipWidth;  //\u03BC\u00B1?\u221E\u2013\u2013\u03BC?
	size_t curLineStartComponentIndex=0;
	size_t curLineComponentCount=0;
	
	RichEditboxComponent* pCaraCpn=NULL;
	if (d_caratPos.d_ComponentIndex==0&&d_caratPos.d_ComponentOffset==0)
	{
		pCaraCpn=NULL;
	}else
	{
		if (d_TempComponents.size()>0&&d_caratPos.d_ComponentIndex<d_TempComponents.size())
		{
			pCaraCpn=d_TempComponents[d_caratPos.d_ComponentIndex];
		}
	}
	
	//size_t count=d_Components.size();
	//ComponentListIt it=d_Components.begin();
	for (size_t i=0;i<d_TempComponents.size();i++)
	{
		
		if (d_TempComponents[i]->GetType()==RichEditboxComponentType_Break)
		{
			d_FormatedComponents.push_back(d_TempComponents[i]);

			LineInfo line;
			line.d_startComponentIdx=curLineStartComponentIndex;
			line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex-1;
			if (line.d_ComponentCount==0)
			{
				line.d_ComponentCount=1;
			}
			d_lines.push_back(line);
			curLineStartComponentIndex=d_FormatedComponents.size();
			curLineSpareWide=ClipWidth;
			continue;
		}

		CaratPos newCaratPos(d_caratPos.d_ComponentIndex,d_caratPos.d_ComponentOffset);
		
		bool bIsToCaratCpn=false;
		if (d_TempComponents[i]==pCaraCpn)  //\u201C\u2014?\u2260\u03BC\u03A9\u03BC\u00B1?\u221E\u03C0?\u00B1\u00ED\u00E0?\u2018?\u03BC??\u201D??o??\u00E0
		{
			bIsToCaratCpn=true;
		}
		bool bIsChangeCarat=false;
		size_t newIdx=1;
		
		float componentWidth=d_TempComponents[i]->getPixelSize().d_width;
		float componentHeight=d_TempComponents[i]->getPixelSize().d_height;
		if (componentWidth>curLineSpareWide)
		{
			
			if (d_TempComponents[i]->canSplit())
			{
				
				if (bIsToCaratCpn&&d_wordWrap)
				{
					
					bIsChangeCarat=d_TempComponents[i]->GetNewCaratPos(curLineSpareWide,d_caratPos.d_ComponentOffset,newIdx);
					
					
				}
				RichEditboxComponent* NewComponent=d_TempComponents[i]->split(curLineSpareWide,false);
				if (d_TempComponents[i]->GetCharCount()>0)
				{
					d_FormatedComponents.push_back(d_TempComponents[i]);
				}else
				{
					//todo ?\u2019\u03BC??\u201D\u03A9?\u03BC?\u201C?\u2026?\u03BC\u00F9
					if (d_MouseOnComponent==d_TempComponents[i])
					{
						d_MouseOnComponent=NULL;
					}
					delete d_TempComponents[i];
					d_TempComponents.erase(d_TempComponents.begin()+i);

					if (i>0)
					{
						i--;
					}else
					{
						i=0;
					}
					
				}
				LineInfo line;
				line.d_startComponentIdx=curLineStartComponentIndex;
				line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex;
				d_lines.push_back(line);

				curLineSpareWide=ClipWidth;
				
				curLineStartComponentIndex=d_FormatedComponents.size();
				
				if (i==d_TempComponents.size()-1)
				{
					d_TempComponents.push_back(NewComponent);
				}else
				{
					d_TempComponents.insert(d_TempComponents.begin()+i+1,NewComponent);

				}

				if (bIsToCaratCpn&&d_wordWrap)
				{
					size_t curSize=d_FormatedComponents.size();
					if (bIsChangeCarat)
					{
						newCaratPos.d_ComponentIndex=i+1;
						newCaratPos.d_ComponentOffset=newIdx;
						pCaraCpn=NewComponent;
					}
				}
			}else
			{
				if(componentWidth<=ClipWidth)
				{
					d_FormatedComponents.push_back(d_TempComponents[i]);

					LineInfo line;
					line.d_startComponentIdx=curLineStartComponentIndex;
					line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex-1;
					d_lines.push_back(line);
					curLineStartComponentIndex=d_FormatedComponents.size()-1;
					curLineSpareWide=ClipWidth-componentWidth;

					if ( i==d_TempComponents.size()-1)
					{
						LineInfo line;
						line.d_startComponentIdx=curLineStartComponentIndex;
						line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex;
						d_lines.push_back(line);
					}
				}

				if (bIsToCaratCpn&&d_wordWrap)
				{
					size_t curSize=d_FormatedComponents.size();
					newCaratPos.d_ComponentIndex=curSize>0?(curSize-1):(0);
					newCaratPos.d_ComponentOffset=d_caratPos.d_ComponentOffset;
				}

			}
		}else
		{
			d_FormatedComponents.push_back(d_TempComponents[i]);
			//if (d_lines.size()==0)
			{
				LineInfo line;
				line.d_startComponentIdx=curLineStartComponentIndex;
				line.d_ComponentCount=d_FormatedComponents.size()-curLineStartComponentIndex;
				if ( i==d_TempComponents.size()-1)
				{
					d_lines.push_back(line);
					curLineStartComponentIndex=d_FormatedComponents.size();
				}
				
			}
			curLineSpareWide=curLineSpareWide-componentWidth;

			if (bIsToCaratCpn&&d_wordWrap)
			{
				size_t curSize=d_FormatedComponents.size();
				newCaratPos.d_ComponentIndex=curSize>0?(curSize-1):(0);
				newCaratPos.d_ComponentOffset=d_caratPos.d_ComponentOffset;
			}
		}

		if (bIsToCaratCpn&&d_wordWrap)
		{
			//setCaratPos(newCaratPos);
			d_caratPos=newCaratPos;
			
		}
		
	}

	d_TempComponents.resize(0);

	CheckLineCount();
    
	float ExtentWidth=0.0f;
	float Totalheight=0.0f;
	for (size_t i=0;i<d_lines.size();i++)
	{
		Size LineSize=GetLineSize(i);
		if (LineSize.d_width>ExtentWidth)
		{
			ExtentWidth=LineSize.d_width;
		}

		if (LineSize.d_height>d_MaxLineHeight)
		{
			d_MaxLineHeight=LineSize.d_height;
		}

		d_lines[i].d_YOffset=Totalheight;

		Totalheight+=LineSize.d_height;

	}

	//d_ParseText=GenerateParseText(false);

	d_TotalHeight=Totalheight;
	d_widestExtent=ExtentWidth;
	if (bConfigScrollBar)
	{
		configureScrollbars();
	}
	
	invalidate();
}

Size RichEditbox::GetLineSize(size_t index)
{
	Size size(0.0f,0.0f);
	if (index>=0&&index<d_lines.size())
	{
		float lastWidth=d_lines[index].d_Width;
		float lastHeight=d_lines[index].d_Height;

		if (!d_AutoChangeEmotionSize&&lastWidth>0.0f&&lastHeight>0.0f)
		{
			size.d_height=lastHeight;
			size.d_width=lastWidth;
		}else
		{
			size_t startindex=d_lines[index].d_startComponentIdx;
			size_t count=d_lines[index].d_ComponentCount;


			float CurLineWidth=0.0f;
			float CurLineHeight=0.0f;

			size_t totalCpnCount=d_FormatedComponents.size();
			for(size_t i=startindex;i<startindex+count;i++)
			{
				if (i>=totalCpnCount)
				{
					Logger::getSingleton().logEvent("richeditbox GetLineSize greater len FormatCpnSize",Warnings);
					continue;;
				}

				Size componentsize=d_FormatedComponents[i]->getPixelSize();

				CurLineWidth+=componentsize.d_width;
				if (componentsize.d_height>CurLineHeight)
				{
					CurLineHeight=componentsize.d_height;
				}

			}

			d_lines[index].d_Width=CurLineWidth;
			d_lines[index].d_Height=CurLineHeight;

			size.d_width=CurLineWidth;
			size.d_height=CurLineHeight;

		}
		
	}
	return size;
	
}

Point  RichEditbox::GetCaratDrawPos()
{
	size_t index=d_caratPos.d_ComponentIndex;
	size_t offset=d_caratPos.d_ComponentOffset;

	
	float DrawWidth=0.0f;
	float DrawHeight=0.0f;

	if (d_FormatedComponents.size()>0)
	{
		size_t CaratLineIndex=GetLineIndexByComponent(index);
		if (CaratLineIndex>=0)
		{
			if (CaratLineIndex>=0&&CaratLineIndex<d_lines.size())
			{
				DrawHeight=GetLineVertOffset(CaratLineIndex);
				size_t LineCompnStartIdx=d_lines[CaratLineIndex].d_startComponentIdx;
				for(size_t i=LineCompnStartIdx;i<index;i++)
				{
					DrawWidth+=d_FormatedComponents[i]->getPixelSize().d_width;
				}
				DrawWidth+=d_FormatedComponents[index]->GetOffsetOfIndex(offset).d_x;
				
			}
		}
	}

	return Point(DrawWidth,DrawHeight);
}

float RichEditbox::GetLineVertOffset(size_t index) //?\u00B0\u03BC\u221A\u220F\u221A\u2013\u2013\u03BC??\u2018\u2019?\u220F?\u03BC???\u2026?\u03A9?\u03BC??\u2032\u201C?\u220F???
{
	float height=0.0f;
	if (index>=0&&index<d_lines.size())
	{
		for (size_t i=0;i<index;i++)
		{
			Size linesize=GetLineSize(index);
			height+=linesize.d_height;
		}
		
	}
	return height;
}

size_t RichEditbox::GetLineIndexByComponent(size_t index)
{
	
	if (index>=0&&index<d_FormatedComponents.size())
	{
		for (size_t i=0;i<d_lines.size();i++)
		{
			size_t LineIndexEnd=d_lines[i].d_startComponentIdx+d_lines[i].d_ComponentCount-1;
			if (index>=d_lines[i].d_startComponentIdx&&index<=LineIndexEnd)
			{
				return i;
			}
		}
	}

	return -1;
	
}

String RichEditbox::GenerateParseText(bool bIsGenerateRoot)
{
	String ParseText("");
	if (bIsGenerateRoot)
	{
		ParseText="<content >";
	}
	
	for (size_t i=0;i<d_FormatedComponents.size();i++)
	{
		ParseText+=d_FormatedComponents[i]->GenerateParseText();

	}

	if (bIsGenerateRoot)
	{
		ParseText+="</content>";
	}

	return ParseText;
}

void	RichEditbox::onParseTextChanged(WindowEventArgs& e)
{
	//String parseText=GenerateParseText();
	//d_ParseText=parseText;
	//format();

}

void RichEditbox::onTextAcceptedEvent(WindowEventArgs& e)
{
	if (d_WaitEmotionNum&&!d_readOnly)
	{
		d_AccceptEmotion=true;
		Refresh();
		d_AccceptEmotion=false;
	}
	fireEvent(EventTextAccepted, e, EventNamespace);
}

String RichEditbox::GetPureText()
{
	String PureString("");
	for (size_t i=0;i<d_FormatedComponents.size();i++)
	{
		if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Text)
		{
			RichEditboxTextComponent* pTextComponent=(RichEditboxTextComponent*)d_FormatedComponents[i];
			if (NULL!=pTextComponent)
			{
				PureString+=pTextComponent->getText();
			}
		}
	}

	return PureString;
}

void RichEditbox::ResetTextColour(ColourRect color)
{
	for (size_t i = 0; i < d_FormatedComponents.size(); i++)
	{
		if (d_FormatedComponents[i]->GetType() == RichEditboxComponentType_Text)
		{
			RichEditboxTextComponent* pTextComponent = (RichEditboxTextComponent*)d_FormatedComponents[i];
			if (NULL != pTextComponent)
			{
				pTextComponent->setColours(color);
			}
		}
	}
}

//String RichEditbox::GetShiedTextInf(std::vector<TextCpnInf>& vecTextCpnInf)  //a\u00F2\u03BC\u221A\u201D\u221A?\uFFE5o?\u2264???\u00B1??\u00F7\u03BC???\u00B1?
//{
//	String PureString("");
//	vecTextCpnInf.clear();
//	int curIdx=0;
//	for (size_t i=0;i<d_FormatedComponents.size();i++)
//	{
//		if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Text)
//		{
//			RichEditboxTextComponent* pTextComponent=(RichEditboxTextComponent*)d_FormatedComponents[i];
//			if (NULL!=pTextComponent)
//			{
//				String cpnText=pTextComponent->getText();
//				PureString+=cpnText;
//
//				TextCpnInf inf(0,0);
//				inf.startIdx=curIdx;
//				inf.endIdx=curIdx+(int)(cpnText.length()-1);
//				vecTextCpnInf.push_back(inf);
//
//				curIdx+=(int)cpnText.length();
//
//			}
//		}
//	}
//
//	return PureString;
//}

//判断增加指定长度的文本，是否超标.
bool RichEditbox::CheckAppendLength(const String& ParseText)
{
	int charLength = ParseText.GetCharLength();

	const char* s = ParseText.c_str();
	char lastChar = 0;
	bool record = false;
	std::string text;
	while (*s)
	{
		if (*s == '"' || *s == '\'')
		{
			if (record)
			{
				record = false;
			}
			else if (lastChar == 't')
			{
				record = true;
				++s;
				--charLength;
				continue;
			}
		}
		if (record)
			text += *s;
		else
			--charLength;
		if (*s != ' ' && *s != '\t' && *s != '\r' && *s != '\n'&&*s != '=')
			lastChar = *s;
		++s;
	}


	int wideStringLength = s2ws(text).length();
	return wideStringLength + d_CurTextLen <= d_maxTextLen;
}

//richeditbox?\u2018??\u03A9\u201D??
void RichEditbox::AppendBreak()
{
	RichEditboxBreakLineComponent* pNewBreak=new RichEditboxBreakLineComponent;
	AddRefreshComponent(pNewBreak);
	//format();
}

void RichEditbox::AppendText(const String& Text,const ColourRect& color,bool bBorder,const colour& borderColour)
{
	RichEditboxTextComponent* pNewText=new RichEditboxTextComponent();
	pNewText->setText(Text);
	pNewText->setFont(getFont());
	pNewText->setColours(color);
	pNewText->SetBorderEnable(bBorder);
	pNewText->SetBorderColour(borderColour);
	AddRefreshComponent(pNewText);
	//format();	
}


void RichEditbox::Clear() 
{ 
	if (d_IsLockState)
	{
		return;
	}
	DestroyAllComponent();
	/*d_FormatedComponents.clear();
	d_FormatedComponents.resize(0);*/
	d_lines.resize(0);
	//format();
	setCaratPos(0,0);
	d_selectionStart.Reset();
	d_selectionEnd.Reset();
	d_CurTextLen = 0;
}

void RichEditbox::HandleEnd()
{
	 Scrollbar* vertScrollbar = getVertScrollbar();
	 if (vertScrollbar&&d_readOnly&&d_wordWrap)
	 {
		 float max_pos = ceguimax((vertScrollbar->getDocumentSize() -vertScrollbar->getPageSize()), 0.0f);
		 vertScrollbar->setScrollPosition(max_pos);
	 }
}

void RichEditbox::HandleTop()
{
	Scrollbar* vertScrollbar = getVertScrollbar();
	if (vertScrollbar&&d_readOnly&&d_wordWrap)
	{
		vertScrollbar->setScrollPosition(0.0f);
	}
}

Size RichEditbox::GetExtendSize() //a\u00F2\u03BC\u221A
{
	Size ExtentSize(0.0f,0.0f);
    /*
	for (size_t index=0;index<d_lines.size();index++)
	{
		Size linesize=GetLineSize(index);
		ExtentSize.d_height+=linesize.d_height;
		if (ExtentSize.d_width<linesize.d_width)
		{
			ExtentSize.d_width=linesize.d_width;
		}
		
	}
    */
	ExtentSize.d_height=d_TotalHeight;
	ExtentSize.d_width=d_widestExtent;
	return ExtentSize;
}

RichEditboxLinkTextComponent* RichEditbox::AppendLinkText(const String& Text,const ColourRect& colours)
{
	RichEditboxLinkTextComponent* pNewLinkText=new RichEditboxLinkTextComponent();
	pNewLinkText->setText(Text);
	pNewLinkText->setFont(getFont());
	pNewLinkText->setColours(colours);
	AddRefreshComponent(pNewLinkText);
	//format();
	//d_ParseText=GenerateParseText();
	
	return pNewLinkText;

}

RichEditboxGoToComponent* RichEditbox::AppendGoToText(const String& Text,const ColourRect& colours)
{
	RichEditboxGoToComponent* pGoToComponent=new RichEditboxGoToComponent();
	pGoToComponent->setText(Text);
	pGoToComponent->setColours(colours);
	pGoToComponent->setFont(getFont());
	AddRefreshComponent(pGoToComponent);
	return pGoToComponent;

}

bool RichEditbox::InsertComponentInCarat(RichEditboxComponent* pComponent, bool checkMaxLength)
{
	//o?\u2264??\u00F3\uFFE5??\u00F7\u2211???
	size_t selectLength=getSelectionLength();
	if (d_selectionStart!=d_selectionEnd)
	{
		DeleteSelectText();
		d_CurTextLen-=selectLength;
	}
	bool bIsEditFull=false;
	if (!d_readOnly&&pComponent!=NULL && checkMaxLength)
	{
		int textLen=pComponent->GetCharCount();
		if (textLen+d_CurTextLen>d_maxTextLen)
		{
			
			if (pComponent->GetType()==RichEditboxComponentType_Text&&d_CurTextLen<d_maxTextLen)
			{
				RichEditboxTextComponent* pTextCpn=(RichEditboxTextComponent*)pComponent;
				size_t cout=textLen+d_CurTextLen-d_maxTextLen;
				String newText=pTextCpn->getText().substr(0,cout);
				pTextCpn->setText(newText);
				bIsEditFull=true;
			}else
			{
				delete pComponent;
				WindowEventArgs args(this);
				onEditboxFullEvent(args);
				return false;
			}
			
			
		}
	}
	if (pComponent!=NULL)
	{
		if (d_FormatedComponents.size()>0)
		{
			size_t CurCaratIndex=d_caratPos.d_ComponentIndex;
			size_t CurCaratOffset=d_caratPos.d_ComponentOffset;
			RichEditboxComponent* pAddComponent=d_FormatedComponents[CurCaratIndex]->InsertComponent(CurCaratOffset,pComponent);
			if (NULL!=pAddComponent)
			{
				d_FormatedComponents.insert(d_FormatedComponents.begin()+CurCaratIndex+1,pComponent);
				d_FormatedComponents.insert(d_FormatedComponents.begin()+CurCaratIndex+2,pAddComponent);
				setCaratPos(CaratPos(CurCaratIndex+1,pComponent->GetMaxIndex()));
			}else
			{
				
				if (pComponent->canSplit()&&d_FormatedComponents[CurCaratIndex]->canSplit())
				{
					setCaratPos(CurCaratIndex,pComponent->GetMaxIndex()+CurCaratOffset);
					delete pAddComponent;
                    pAddComponent = NULL;
				}
                else
				{
					
					d_FormatedComponents.insert(d_FormatedComponents.begin()+CurCaratIndex+1,pComponent);
					setCaratPos(CurCaratIndex+1,pComponent->GetMaxIndex());

				}
				
			}
		}else
		{
			d_FormatedComponents.push_back(pComponent);
			setCaratPos(0,pComponent->GetMaxIndex());
		}

		WindowEventArgs args(this);
		onTextChanged(args);
	}
	if (bIsEditFull)
	{
		WindowEventArgs args(this);
		onEditboxFullEvent(args);
	}

    return true;
}

RichEditboxLinkTextComponent* RichEditbox::InsertLinkTextInCarat(const String& Text,const ColourRect& colours)
{
	if (Text.size()<=0)
	{
		return NULL;
	}

	if (d_CurTextLen>=d_maxTextLen)
	{
		WindowEventArgs e(this);
		onEditboxFullEvent(e);
		return NULL;
	}


	RichEditboxLinkTextComponent* pNewLinkText=new RichEditboxLinkTextComponent();
	pNewLinkText->setText(Text);
	pNewLinkText->setFont(getFont());
	pNewLinkText->setColours(colours);

    if(!InsertComponentInCarat(pNewLinkText))
    {
        delete pNewLinkText;
        pNewLinkText = NULL;
    }
	return pNewLinkText;

}

RichEditboxEmotionComponent* RichEditbox::InserEmotionInCarat(int emotionID)
{
	if (emotionID!=-1)
	{
		if (d_CurTextLen>=d_maxTextLen)
		{
			WindowEventArgs e(this);
			onEditboxFullEvent(e);
			return NULL;
		}

		RichEditboxEmotionComponent* pComponent=new RichEditboxEmotionComponent();
		pComponent->SetScale(d_EmotionScale);
		pComponent->SetAnimateID(emotionID);
		//pComponent->SetParent(this);
        if(!InsertComponentInCarat(pComponent))
        {
            delete pComponent;
            pComponent = NULL;
        }
        
		return pComponent;
	}
	return NULL;

}
RichEditboxGoToComponent* RichEditbox::InsertGoToTextInCarat(const String& Text,const ColourRect& colours)
{
	if (d_CurTextLen>=d_maxTextLen)
	{
		WindowEventArgs e(this);
		onEditboxFullEvent(e);
		return NULL;
	}

	RichEditboxGoToComponent* pGoToComponent=new RichEditboxGoToComponent();
	pGoToComponent->setText(Text);
	pGoToComponent->setColours(colours);
	pGoToComponent->setFont(getFont());

    if(!InsertComponentInCarat(pGoToComponent))
    {
        delete pGoToComponent;
        pGoToComponent = NULL;
    }
    
	return pGoToComponent;

}
void RichEditbox::InsertTextInCarat(const String& Text,const ColourRect& colours)
{
	if (Text.size()<=0)
	{
		return;
	}

	size_t selectLenth=getSelectionLength();

	String insertString(Text);

	if (d_CurTextLen-selectLenth+Text.length()>d_maxTextLen)
	{
		
		WindowEventArgs e(this);
		onEditboxFullEvent(e);
		if (selectLenth==0&&d_CurTextLen>=d_maxTextLen)
		{
			return;
		}else
		{
			//\uFFE5??\u00EC\u2018\u03A9\u03A9\u00E1?\u00F7\u2211?\uFFE5?
			insertString=ParseTextOutofBound(insertString, d_maxTextLen-d_CurTextLen+selectLenth);
			//insertString=Text.substr(0,d_maxTextLen-d_CurTextLen+selectLenth);
		}
	}

	RichEditboxTextComponent* pNewText=new RichEditboxTextComponent();
	pNewText->setText(insertString);
	pNewText->setFont(getFont());
	pNewText->setColours(colours);

    if(!InsertComponentInCarat(pNewText, false))
    {
        delete pNewText;
        pNewText = NULL;
    }
}

bool RichEditbox::isNumber(utf32 num) //??\u2211\u00F2?????\u00F7?\u00F7\u2211?
{
	if (num>=48&&num<=57)
	{
		return true;
	}
	return false;
}

//\uFFE5??\u00EC\u2018\u03A9\u03A9\u00E1?\u00F7\u2211?\uFFE5?
String RichEditbox::ParseTextOutofBound(const String& srcStr, int boundLen)
{
	if (srcStr.size() <= (size_t)boundLen)
	{
		return srcStr;
	}

	int realStrLen = 0;		//\u201D\u2026\u201D?\u00B1\u00EC?\u00E8\u2211???\u03BC\u00B1\u201D?\u201Ca\u220F??\u00F7\u2211?,\u03C0???\u00E0\u2014\u2014\u221E?\u00F7\u2211?\uFFE5?\u00F7\u2013\u00B1\u00EC?\u00E8\u2211?\u220F???o\u221E???\u00EA?\u00F7\u2211?
	int realStrLenEx = 0;
	for (size_t i = 0; i<srcStr.size();)
	{
		utf32 curChar = srcStr[i];
		if (curChar != '#')
		{
			realStrLen++;
			i++;
			if (realStrLen == boundLen)
			{
				return srcStr.substr(0,i);	
			}			
		}
		else
		{
			int sharpID = i;
			//\u00B1\u00E8?\u02D9\u00F7?\u222B?\u03BC????\u00F7
			String curEmotionIDStr = "";
			int curEmotionID = -1;
			int startID = i+1;
			for(size_t j=startID; j<srcStr.size(); j++)
			{
				if (isNumber(srcStr[j]))
				{
					curEmotionIDStr.push_back(srcStr[j]);
					int newEmotionID = PropertyHelper::stringToInt(curEmotionIDStr);
					if (newEmotionID <= System::getSingleton().GetEmotionNum())
					{
						curEmotionID = newEmotionID;
						i++;
					}
					else
					{
						break;
					}
				}
				else
				{
					break;
				}
			}
			//\u2264\u00E8?\uFFE5??\u2211\u00F2\u201D\u2013?\u2026\u201D\u221A\u00B1\u00EC?\u00E8
			realStrLen++;
			i++;
			if (realStrLen == boundLen)
			{
				return srcStr.substr(0,i);	
			}			
		}
	}

	return srcStr;

	//String newStr = srcStr.substr(0,boundLen);	

	//utf32 endChar=srcStr[boundLen-1];
	//utf32 endNextChar=srcStr[boundLen];

	////?\u00F3\u222B?\u201Ca\u220F??\u00F7\u2211?\u2211????\u00F7?\u201C\u2264a??\u00B1\u00EC?\u00E8??\u03C0?#
	//if (!isNumber(endChar) && endChar != '#')
	//{
	//	return newStr;
	//}

	////\u2019??\u2018#?\u2018\u220F?\u220F??\u00E8????\u2211\u00F7??	
	//size_t curIdx = newStr.rfind("#");	//?\u00B0\u03BC\u221A?\u00F3\u222B?\u201Ca\u220F?
	//if (curIdx == String::npos)
	//{
	//	return newStr;
	//}

	//\u2014\u221E\u2019\u201C\u03BC\u03A9\u00B1\u00EC?\u00E8\u2211?,?\u00FA\u222B?\u00E0\u2014\u00E0?.	
	//int operLen = std::min(int(srcStr.size()), GetMaxEmotionLen());
	//int emotionID = PropertyHelper::stringToInt(numStr);;

	//return "";
}

void RichEditbox::InsertImageInCarat(const Image* pImage)
{
	if (pImage==NULL)
	{
		return;
	}
	RichEditboxImageComponent* pNewImage=new RichEditboxImageComponent(pImage);

    if(!InsertComponentInCarat(pNewImage))
    {
        delete pNewImage;
        pNewImage = NULL;
    }
}

void  RichEditbox::UpdataMouseOnComponent()
{
	CEGUI::Point ptMouse=MouseCursor::getSingleton().getPosition();
	RichEditboxComponent* pMouseOnCpn=GetComponentByPos(ptMouse);
	/*if (d_FormatedComponents.size()>0)
	{
		pMouseOnCpn=d_FormatedComponents[pos.d_ComponentIndex];
	}*/
	if (pMouseOnCpn!=d_MouseOnComponent)
	{
		if (d_MouseOnComponent!=NULL)
		{
			d_MouseOnComponent->onMouseLeave();
			invalidate();
		}
		d_MouseOnComponent=pMouseOnCpn;
		if (d_MouseOnComponent!=NULL)
		{
			d_MouseOnComponent->onMouseEnter();
			invalidate();
		}
	}
    Scrollbar* vertScrollbar = getVertScrollbar();
    if (vertScrollbar) {
        printf("after %f\n", vertScrollbar->getScrollPosition());
    }
}

void  RichEditbox::onMouseLeaves(MouseEventArgs& e)
{
	Window::onMouseLeaves(e);

	d_bEnterKeyIsPess=false;

	if (d_MouseOnComponent!=NULL)
	{
		d_MouseOnComponent->onMouseLeave();
		d_MouseOnComponent=NULL;
		invalidate();
	}


}

void RichEditbox::DelComponent(RichEditboxComponent* pComponent,bool bEraseFromList)
{
	if (pComponent!=NULL)
	{
		if (d_MouseOnComponent==pComponent)
		{
			d_MouseOnComponent=NULL;
		}

		delete pComponent;
        
		
		if (bEraseFromList)
		{
			ComponentList::iterator it=d_FormatedComponents.begin();
			for (;it!=d_FormatedComponents.end();++it)
			{
				if (*it==pComponent)
				{
					d_FormatedComponents.erase(it);
					break;
				}
			}
		}
	}
}

void RichEditbox::SetLockState(bool bIsLock)
{
	if (d_readOnly)
	{
		if (d_IsLockState&&!bIsLock)
		{
			d_IsLockState=bIsLock;
			AddWaitComponentToBox();
		}
		d_IsLockState=bIsLock;
		
	}
}

void    RichEditbox::AddWaitComponentToBox()  //?\u00ECo\u201D\uFFE5??\u00ECo\u201D\u03BC???o?
{
	if (d_WaitToAddComponents.size()>0)
	{
		ComponentList::iterator it=d_WaitToAddComponents.begin();
		for (;it!=d_WaitToAddComponents.end();++it)
		{
			d_FormatedComponents.push_back(*it);
		}
		d_WaitToAddComponents.resize(0);
		format();
		HandleEnd();
		
	}
}

void    RichEditbox::ReleaseWaitComponents()
{
	if (!d_WaitToAddComponents.empty())
	{
		ComponentList::iterator it=d_WaitToAddComponents.begin();
		for (;it!=d_WaitToAddComponents.end();++it)
		{
			delete (*it);
		}
		d_WaitToAddComponents.resize(0);
	}
}

void    RichEditbox::ReleaseRefreshComponents()
{
	if (!d_RefreshComponents.empty())
	{
		ComponentList::iterator it=d_RefreshComponents.begin();
		for (;it!=d_RefreshComponents.end();++it)
		{
			delete (*it);
		}
		d_RefreshComponents.resize(0);
	}
}

void  RichEditbox::AddComponent(RichEditboxComponent* pComponent)
{ 
	if (pComponent!=NULL)
	{
		pComponent->SetLineSpace(d_LineSpace);
		if (d_IsLockState)
		{
			d_WaitToAddComponents.push_back(pComponent);
		}else
		{
			d_FormatedComponents.push_back(pComponent);  
		}
	
	}
}

bool RichEditbox::GetLineByPos(size_t& LineIndex,const Point& pt) //\u220F????a\u00F7\u221Aa\u00F2\u03BC\u221A\u03BC?o\u220F\u2013\u2013,pt?????a?\u02C9\u00B1\u00ED(?\u02C9?\u2018?\u02C9\u00B1\u00ED)
{
	if (d_lines.size()<=0)
	{
		return false;
	}
	float height=0.0f;
	for (size_t i=0;i<d_lines.size();++i)
	{
		Size linesize=GetLineSize(i);
		if (pt.d_y<=height+linesize.d_height)
		{
			LineIndex=i;
			return true;
		}
		height+=linesize.d_height;
	}
	return false;
}

void  RichEditbox::AddRefreshComponent(RichEditboxComponent* pComponent)   //\u2018?o\u201D\u03BC\u03A9???\u00B1\u03BC??\u201D??o??\u201D?\u2013\u00F7\u2013\uFFE1\u00A8\u03BC?refresh?\u00B1\u2264\u2248a\u00B7format
{
	if (pComponent!=NULL)
	{
		pComponent->SetLineSpace(d_LineSpace);
		if (d_IsLockState)
		{
			d_WaitToAddComponents.push_back(pComponent);
		}else
		{
			d_RefreshComponents.push_back(pComponent);
		}
	}
}

void RichEditbox::Refresh()  //\u2018?o\u201D??o??\u00B1\u201Ca??\u201C?\u03BC?\u201D\u221A
{
	if (d_RefreshComponents.size()>0)
	{
		ComponentList::iterator it=d_RefreshComponents.begin();
		for (;it!=d_RefreshComponents.end();++it)
		{
			d_FormatedComponents.push_back(*it);
		}
		
		d_RefreshComponents.resize(0);
	}
	format();
	if (!d_IsLockState)
	{
		HandleEnd();
	}
	
}

void RichEditbox::SetCaratEnd()  //\u221E\u2014\u03C0?\u00B1\u00ED\u201C????\u00F3\u222B?
{
	CaratPos pos(0,0);
	
	if (d_FormatedComponents.size()>0)
	{
		size_t index=d_FormatedComponents.size()-1;
		pos.d_ComponentIndex=index;
		pos.d_ComponentOffset=d_FormatedComponents[index]->GetMaxIndex();
	}
	setCaratPos(pos);
}

void RichEditbox::updateSelf(float elapsed)
{
	Window::updateSelf(elapsed);

	bool bNeedReDraw=false;

	if (isVisible())
	{
		size_t cpnTotal=d_FormatedComponents.size();
		for (size_t i=0;i<d_UpdateCpnList.size();i++)
		{
			size_t idx=d_UpdateCpnList[i];
			if (idx>=0&&idx<cpnTotal)
			{
				if (d_FormatedComponents[idx]->updata(elapsed))
				{
					bNeedReDraw=true;
				}
			}
		}
	}
	
	if (d_ScrobarLockState)
	{
		d_ScrobarLockElapseTime+=elapsed;
		if (d_ScrobarLockElapseTime>d_ScrobarLockTime)
		{
			OnThumbLockEnd();
		}
	}
    if (g_bIsTextLoading) {
        EditModeFormat(true);
        if (d_bTopAfterLoadFont)
            HandleTop();
        else
            HandleEnd();
    }
	if (bNeedReDraw || g_bIsTextLoading)
	{
		invalidate();
	}
}

void RichEditbox::ParseTipsLinkInf(std::vector<TipsLinkInf>& vecTipsInf)
{
	
	size_t count=0;
	for (size_t i=0;i<d_FormatedComponents.size();++i)
	{
		if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Tips)
		{
			RichEditboxTipsLinkComponent* pTipsCpn=(RichEditboxTipsLinkComponent*)d_FormatedComponents[i];
			if (pTipsCpn)
			{
				//TipsLinkInf Inf(0,0);
				if (count>vecTipsInf.size())
				{
					return;
				}
				vecTipsInf[count].roleID=pTipsCpn->GetRoleID();
				vecTipsInf[count].key=pTipsCpn->GetObjectKey();
				vecTipsInf[count].type=pTipsCpn->GetObjectType();
				vecTipsInf[count].shopID=pTipsCpn->GetShopID();
				vecTipsInf[count].counterID=pTipsCpn->GetCounterID();
				vecTipsInf[count].loseEffTime=pTipsCpn->GetLoseEffectTime();
				
				count++;
			}
		}
	}
}

int RichEditbox::GetTipsLinkCount()  //a\u00F2\u03BC\u221A\u03BC\u00B1?\u221E\u03BC?tips?\uFFE5\u03A9\u201D\u03BC?\u220F???
{
	int count=0;
	for (size_t i=0;i<d_FormatedComponents.size();++i)
	{
		if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Tips
			||d_FormatedComponents[i]->GetType()==RichEditboxComponentType_GoToText
			||d_FormatedComponents[i]->GetType()==RichEditboxComponentType_NameLink)
		{
			count++;
		}
	}
	return count;

}

int RichEditbox::GetCharCount() //a\u00F2\u03BC\u221A?\u00F7\u2211???
{
	/*int count=0;
	for (size_t i=0;i<d_FormatedComponents.size();++i)
	{
		count+=d_FormatedComponents[i]->GetCharCount();
	}
	return count;*/
	return static_cast<int>(d_CurTextLen);

}
int RichEditbox::GetLineCount() //a\u00F2\u03BC\u221A\u2013\u2013??
{
	return (int)d_lines.size();

}

bool RichEditbox::isCaratOnAllTop() //\u03BC\u00B1?\u221E\u03C0?\u00B1\u00ED??\u2264a??\u2018?\u2019?\u220F??\u00FA\u03BC??\u00F3?\u221E?\u00E0
{
	if (d_caratPos.d_ComponentIndex==0&&d_caratPos.d_ComponentOffset==0)
	{
		return true;
	}
	return false;
}

//a\u00F2\u03BC\u221A?\u2265??o?a\u2260\u2014\u00B0\u2018\u00F2\u00B1\u00ED\u00F7??\u00B1\u03BC??\uFFE5?\u00A8\uFFE1\u00A8
//startDrawOffset ???oa\u2260\u03BC??\u2032\u201C?
//EndDrawOffset \u03A9\u00B7?\u02C9a\u2260\u03BC??\u2032\u201C?
bool RichEditbox::GetSelectDrawPos(size_t cpnIdx,size_t& startDrawOffset,size_t& EndDrawOffset)
{
	if (cpnIdx>=d_FormatedComponents.size())
	{
		return false;
	}
	if (d_selectionStart==d_selectionEnd)
	{
		return false;
	}
	
	CaratPos SelStartPos=getSelectionStartPos();
	CaratPos SelectEndPos=getSelectionEndPos();
	startDrawOffset=0;
	EndDrawOffset=0;
	if (cpnIdx>=SelStartPos.d_ComponentIndex&&cpnIdx<=SelectEndPos.d_ComponentIndex)
	{
		if (d_FormatedComponents[cpnIdx]->canSplit())  //?\u2026\u2264?\u2211\u00F7a\u03C0\u03BC\u221A\u2014\u00B0
		{
			if (cpnIdx!=SelStartPos.d_ComponentIndex&&cpnIdx!=SelectEndPos.d_ComponentIndex)//\u2018?\u2014\u00B0\u2018\u00F2???o\u222B?\u03A9\u00B7?\u02C9???o\u00F7\u2013o\u2030,?\u2032a\u2260
			{
				EndDrawOffset=d_FormatedComponents[cpnIdx]->GetMaxIndex();
				
			}else if (cpnIdx==SelStartPos.d_ComponentIndex) //\u2018????o\u2014\u00B0\u2018\u00F2??
			{
				if (SelStartPos.d_ComponentIndex!=SelectEndPos.d_ComponentIndex)
				{
					startDrawOffset=SelStartPos.d_ComponentOffset;
					EndDrawOffset=d_FormatedComponents[cpnIdx]->GetMaxIndex();
				}else
				{
					startDrawOffset=SelStartPos.d_ComponentOffset;
					EndDrawOffset=SelectEndPos.d_ComponentOffset;

				}
				
			}else //\u2018?\u03A9\u00B7?\u02C9\u2014\u00B0\u2018\u00F2??
			{
				if (SelStartPos.d_ComponentIndex!=SelectEndPos.d_ComponentIndex)
				{
					startDrawOffset=0;
					EndDrawOffset=SelectEndPos.d_ComponentOffset;

				}else
				{
					startDrawOffset=SelStartPos.d_ComponentOffset;
					EndDrawOffset=SelectEndPos.d_ComponentOffset;

				}
			}

		}else //\u2264a?\u2026\u2264?\u2211\u00F7?\u2032a\u2260
		{
			EndDrawOffset=d_FormatedComponents[cpnIdx]->GetMaxIndex();

		}
		return true;
	}
	return false;
}

void RichEditbox::DeleteSelectText()  //\u2026?\u2265?\u2014\u00B0\u00F7\u2013\u03BC??\u02C9\u201D\u00FA
{
	CaratPos selStartPos=getSelectionStartPos();
	CaratPos selEndPos=getSelectionEndPos();

	
	if (selStartPos.d_ComponentIndex!=selEndPos.d_ComponentIndex)//???\u2264??o?\u2264a?\u00A8
	{
		//??\u2026?\u2265??\u2264??o?
		if (!d_FormatedComponents[selEndPos.d_ComponentIndex]->canSplit()) //?\u201D??o?\u2264a??\u2264?\u2211\u00F7\uFFE1\u00A8?\u2032\u2026?
		{
			DelComponent(d_FormatedComponents[selEndPos.d_ComponentIndex]);
		}else//?\u201D??o???\u2264?\u2211\u00F7
		{
			size_t maxOffsetEnd=d_FormatedComponents[selEndPos.d_ComponentIndex]->GetMaxIndex();
			if (selEndPos.d_ComponentOffset<maxOffsetEnd)
			{
				float offset=d_FormatedComponents[selEndPos.d_ComponentIndex]->GetOffsetOfIndex(selEndPos.d_ComponentOffset).d_x;
				RichEditboxComponent* pNewCpn=d_FormatedComponents[selEndPos.d_ComponentIndex]->split(offset,true);
				DelComponent(d_FormatedComponents[selEndPos.d_ComponentIndex]);
				//d_FormatedComponents.erase(d_FormatedComponents.begin()+selEndPos.d_ComponentIndex);
				if (pNewCpn)
				{
					d_FormatedComponents.insert(d_FormatedComponents.begin()+selEndPos.d_ComponentIndex,pNewCpn);
				}

			}else
			{
				DelComponent(d_FormatedComponents[selEndPos.d_ComponentIndex]);
				//d_FormatedComponents.erase(d_FormatedComponents.begin()+selEndPos.d_ComponentIndex);


			}

		}

		//\u2026?\u2265?\u00F7\u2013o\u2030\u03BC??\u201D??o?
		if (selEndPos.d_ComponentIndex>0)
		{
			for (size_t i=selEndPos.d_ComponentIndex-1;i>selStartPos.d_ComponentIndex;--i)
			{
				DelComponent(d_FormatedComponents[i]);
			}
		}

		//\u2026?\u2265?????o?
		if (!d_FormatedComponents[selStartPos.d_ComponentIndex]->canSplit()) //?\u201D??o?\u2264a??\u2264?\u2211\u00F7\uFFE1\u00A8?\u2032\u2026?
		{
			DelComponent(d_FormatedComponents[selStartPos.d_ComponentIndex]);
			if (selStartPos.d_ComponentIndex>0&&d_FormatedComponents.size()>0)
			{
				setCaratPos(selStartPos.d_ComponentIndex-1,d_FormatedComponents[selStartPos.d_ComponentIndex-1]->GetMaxIndex());
			}else
			{
				setCaratPos(0,0);
			}
		}else//?\u201D??o???\u2264?\u2211\u00F7
		{
			if (selStartPos.d_ComponentOffset>0)
			{
				float offset=d_FormatedComponents[selStartPos.d_ComponentIndex]->GetOffsetOfIndex(selStartPos.d_ComponentOffset).d_x;
				d_FormatedComponents[selStartPos.d_ComponentIndex]->split(offset,true);
				setCaratPos(selStartPos.d_ComponentIndex,selStartPos.d_ComponentOffset-1);

			}else
			{
				DelComponent(d_FormatedComponents[selStartPos.d_ComponentIndex]);
				if (selStartPos.d_ComponentIndex>0&&d_FormatedComponents.size()>0)
				{
					setCaratPos(selStartPos.d_ComponentIndex-1,d_FormatedComponents[selStartPos.d_ComponentIndex-1]->GetMaxIndex());
				}else
				{
					setCaratPos(0,0);
				}

			}

		}

	}else //???\u2264??o????\u00A8
	{
		if (!d_FormatedComponents[selStartPos.d_ComponentIndex]->canSplit()) //?\u201D??o?\u2264a??\u2264?\u2211\u00F7\uFFE1\u00A8?\u2032\u2026?
		{
			DelComponent(d_FormatedComponents[selStartPos.d_ComponentIndex]);
			if (selStartPos.d_ComponentIndex>0&&d_FormatedComponents.size()>0)
			{
				setCaratPos(selStartPos.d_ComponentIndex-1,d_FormatedComponents[selStartPos.d_ComponentIndex-1]->GetMaxIndex());
			}else
			{
				setCaratPos(0,0);
			}
		}else//?\u201D??o???\u2264?\u2211\u00F7
		{
			size_t maxOffsetStart=d_FormatedComponents[selStartPos.d_ComponentIndex]->GetMaxIndex();
			if (selStartPos.d_ComponentOffset==0&&selEndPos.d_ComponentOffset==maxOffsetStart)
			{
				DelComponent(d_FormatedComponents[selEndPos.d_ComponentIndex]);
				if (selStartPos.d_ComponentIndex>0&&d_FormatedComponents.size()>0)
				{
					setCaratPos(selStartPos.d_ComponentIndex-1,d_FormatedComponents[selStartPos.d_ComponentIndex-1]->GetMaxIndex());
				}else
				{
					setCaratPos(0,0);
				}

			}else
			{
				
				RichEditboxTextComponent* pTextCpn=(RichEditboxTextComponent*)d_FormatedComponents[selStartPos.d_ComponentIndex];
				if (pTextCpn)
				{
					const String strText=pTextCpn->getText();
					size_t length=selEndPos.d_ComponentOffset-selStartPos.d_ComponentOffset;
					String LeftText=strText.substr(0,selStartPos.d_ComponentOffset);
					String newText=LeftText+strText.substr(selStartPos.d_ComponentOffset+length,strText.size()-length-selStartPos.d_ComponentOffset);
					pTextCpn->setText(newText);
					setCaratPos(selStartPos.d_ComponentIndex,LeftText.size());
				}
				

			}

		}

	}
}

void RichEditbox::HandlePasteFromClipBord() //\u221E\uFFE5ctrl+V?\u00B1\uFFE5\u201Do\u00F9?\u2013\u221E?\u220F\uFFE5\u00F7?
{
	
	OnPasteFromClipBord* pFunc=System::getSingleton().GetPasteFromClipBordFunc();
	if (pFunc!=NULL)
	{
		String text("");
		pFunc(text);
		if (text.size()>0)
		{
			InsertTextInCarat(text);
		}
	}

}
void RichEditbox::HandleCopyToClipBord()  //\u221E\uFFE5ctrl+C?\u00B1\u03A9???\u00E0?\u2014\u00B0??o?\u03BC?\uFFE5???\u00B1?\u00F7?o\u00F9?\u2013\u221E?
{
	OnCopyToClipBord* pFunc=System::getSingleton().GetCopyToClipBordFunc();
	if (pFunc!=NULL)
	{
		String strCopy=GenerateSelectPureText();
		(*pFunc)(strCopy);
	}
}

String RichEditbox::GenerateSelectPureText() //\u2026\u02D9\u2265\u2026\u2014\u00B0\u2018\u00F2?\u02C9\u201D\u00FA\u03BC?\uFFE5???\u00B1?\u2013\u2248?\uFFE0
{
	return GenerateAreaPureText(d_selectionStart,d_selectionEnd);

}

String RichEditbox::GenerateAreaPureText(CaratPos startPos,CaratPos endPos) //\u220F????\u02C9\u201D\u00FA\u2026\u02D9\u2265\u2026\uFFE5???\u00B1?\u2013\u2248?\uFFE0
{
	
	if (!IsEmpty()&&isPosValid(startPos)&&isPosValid(endPos))
	{
		String strPureText("");
		if (startPos.d_ComponentIndex==endPos.d_ComponentIndex)
		{
			strPureText=d_FormatedComponents[startPos.d_ComponentIndex]->GeneratePureText(startPos.d_ComponentOffset,endPos.d_ComponentOffset);
		}else
		{
			size_t startPosMaxIndex=d_FormatedComponents[startPos.d_ComponentIndex]->GetMaxIndex();
			strPureText+=d_FormatedComponents[startPos.d_ComponentIndex]->GeneratePureText(startPos.d_ComponentOffset,startPosMaxIndex);

			for (size_t i=startPos.d_ComponentIndex+1;i<endPos.d_ComponentIndex;++i)
			{
				startPosMaxIndex=d_FormatedComponents[i]->GetMaxIndex();
				strPureText+=d_FormatedComponents[i]->GeneratePureText(0,startPosMaxIndex);
			}

			
			strPureText+=d_FormatedComponents[endPos.d_ComponentIndex]->GeneratePureText(0,endPos.d_ComponentOffset);
		}

		return strPureText;
	}
	return "";

}

String RichEditbox::GenerateAllPureText()
{
    CaratPos old = getCaratPos();
    CaratPos posStart(0,0);
    SetCaratEnd();
    CaratPos posEnd = getCaratPos();
    String str = GenerateAreaPureText(posStart, posEnd);
    setCaratPos(old);
    return str;
}
    
bool RichEditbox::isPosValid(CaratPos pos) //\u03C0?\u00B1\u00ED?a\u00F7\u221A\u2013\u2248?\uFFE0??\u2211\u00F2\u201D\u2013\u2013?
{
	if (d_FormatedComponents.size()>0)
	{
		if (pos.d_ComponentIndex<d_FormatedComponents.size())
		{
			if (pos.d_ComponentOffset<=d_FormatedComponents[pos.d_ComponentIndex]->GetMaxIndex())
			{
				return true;
			}
		}
	}else
	{
		if (pos.d_ComponentIndex==0&&pos.d_ComponentOffset==0)
		{
			return true;
		}
	}

	return false;
	
}

bool RichEditbox::isCaratOnEnd() //\u03BC\u00B1?\u221E\u03C0?\u00B1\u00ED??\u2211\u00F2\u2018??\u00F3\u222B?
{
	if (d_FormatedComponents.size()==0)
	{
		return true;
	}
	if (d_caratPos.d_ComponentIndex==d_FormatedComponents.size()-1)
	{
		if (d_caratPos.d_ComponentOffset==d_FormatedComponents[d_caratPos.d_ComponentIndex]->GetMaxIndex())
		{
			return true;
		}
	}
	return false;
}

bool RichEditbox::isOnlySpace() //??\u2211\u00F2\u00F7a\u201D\u2013?\u2019?\u00F7\u2211?
{
	if (d_FormatedComponents.size()>0)
	{
		for (size_t i=0;i<d_FormatedComponents.size();++i)
		{
			if (!d_FormatedComponents[i]->isOnlySpace())
			{
				return false;
				break;
			}
		}

	}
	return true;
}

RichEditboxNameLinkComponent* RichEditbox::InsertNameLinkInCarat(int64_t roleID,const String& roleName,int colorType)
{
	RichEditboxNameLinkComponent* pComponent=new RichEditboxNameLinkComponent(roleID,roleName);
	pComponent->setFont(getFont());
	pComponent->SetNameColorType(colorType);

    if(!InsertComponentInCarat(pComponent))
    {
        delete pComponent;
        pComponent = NULL;
    }
	return pComponent;

}
RichEditboxNameLinkComponent* RichEditbox::AppendNameLink(int64_t roleID,const String& roleName)
{
	RichEditboxNameLinkComponent* pComponent=new RichEditboxNameLinkComponent(roleID,roleName);
	pComponent->setFont(getFont());
	AddRefreshComponent(pComponent);
	return pComponent;
}

RichEditboxFamilyRecruitLinkComponent* RichEditbox::InsertFamilyRecruitInCarat(int familyID,const String& familyName,
																  int64_t leaderID,const String& leader,int rank,int level,const String& familyAim)
{
	RichEditboxFamilyRecruitLinkComponent* pCopn=new RichEditboxFamilyRecruitLinkComponent(familyID,familyName,leaderID,leader,rank,level,familyAim);
	pCopn->setFont(getFont());

    if(!InsertComponentInCarat(pCopn))
    {
        delete pCopn;
        pCopn = NULL;
    }
	return pCopn;
}

RichEditboxFamilyRecruitLinkComponent* RichEditbox::AppendFamilyRecruit(int familyID,const String& familyName,
														   int64_t leaderID,const String& leader,int rank,int level,const String& familyAim)
{
	RichEditboxFamilyRecruitLinkComponent* pComponent=new RichEditboxFamilyRecruitLinkComponent(familyID,familyName,leaderID,leader,rank,level,familyAim);
	pComponent->setFont(getFont());
	AddRefreshComponent(pComponent);
	return pComponent;
}

RichEditboxComponent* RichEditbox::GetFirstLinkTextCpn()  //\u2211\u03BCa?\u03BC?\u201Ca\u220F??\u2026\u03BC?a?\u03BC??\uFFE5\u03A9\u201D
{
	for (size_t i=0;i<d_FormatedComponents.size();i++)
	{
		RichEditboxComponentType type=d_FormatedComponents[i]->GetType();
		if (type==RichEditboxComponentType_LinkText||type==RichEditboxComponentType_GoToText)
		{
			return d_FormatedComponents[i];
		}

	}
	return NULL;

}
    
RichEditboxGoToComponent* RichEditbox::GetFirstGotoLinkCpn()
{
    for (size_t i=0;i<d_FormatedComponents.size();i++)
	{
		RichEditboxComponentType type=d_FormatedComponents[i]->GetType();
		if (type==RichEditboxComponentType_GoToText)
		{
			return (RichEditboxGoToComponent*)d_FormatedComponents[i];
		}
        
	}
	return NULL;
}
    
void RichEditbox::OnFirstGotoLinkClick()
{
        for (size_t i=0;i<d_FormatedComponents.size();i++)
        {
            RichEditboxComponentType type=d_FormatedComponents[i]->GetType();
            if (type==RichEditboxComponentType_GoToText)
            {
                 RichEditboxGoToComponent* gotoclick = (RichEditboxGoToComponent*)d_FormatedComponents[i];
                if(gotoclick)
                {
                    gotoclick->onParentClicked();
                }
            }
        }
}
    
    
Point RichEditbox::GetCpnScreenPos(RichEditboxComponent* pCpn) //\u03BC\u221A\u03BC\u03A9?\u2265\u201Ca\u220F??\u201D??o?\u03BC????a?\u02C9\u00B1\u00ED
{
	size_t lineIndex=GetCpnLineIndex(pCpn);
	Point pt=GetLineScreenPos(lineIndex);
	size_t startIndex=d_lines[lineIndex].d_startComponentIdx;
	size_t cpnCount=d_lines[lineIndex].d_ComponentCount;
	for (size_t i=startIndex;i<startIndex+cpnCount;++i)
	{
		if (d_FormatedComponents[i]==pCpn)
		{
			break;
			
		}
		pt.d_x+=d_FormatedComponents[i]->getPixelSize().d_width;
	}
	return pt;
}

size_t RichEditbox::GetCpnLineIndex(RichEditboxComponent* pCpn)  //?\u2265\u220F?Cpn\u2018?\u03BC?o\u220F\u2013\u2013
{
	size_t index=0;
	for (size_t k=0;k<d_FormatedComponents.size();k++)
	{
		if (d_FormatedComponents[k]==pCpn)
		{
			index=k;
		}
	}

	size_t lineIndex=0;
	for (size_t i=0;i<d_lines.size();++i)
	{
		if (index>=d_lines[i].d_startComponentIdx
			&&index<(d_lines[i].d_startComponentIdx+d_lines[i].d_ComponentCount))
		{
			return i;
		}
	}
	return lineIndex;
}

Point RichEditbox::GetLineScreenPos(size_t index)  //\u2013\u2013\u03BC????a?\u02C9\u00B1\u00ED
{
	Point pt(0.0f,0.0f);
	if (index>=d_lines.size())
	{
		return pt;
	}

	float height=0.0f;
	for (size_t i=0;i<index;++i)
	{
		Size lineSize=GetLineSize(i);
		height+=lineSize.d_height;
	}
	pt.d_x=GetScreenPos().d_x;
	pt.d_y=GetScreenPos().d_y+height;

	pt.d_x -= getHorzScrollbar()->getScrollPosition();
	pt.d_y -= getVertScrollbar()->getScrollPosition();
	return pt;
}

void RichEditbox::ParseControlText() //\u03A9????\u00F7\u2211?\uFFE5? \u03A9???#\u03BC???\u03A9??\u2030??\u2013\u2248?\uFFE0
{
	//return;  //\u2018??\u00B1?\uFFE0??\u03BC\u00F9 

	for (size_t i=0;i<d_FormatedComponents.size();++i)
	{
		if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Text)
		{
			RichEditboxTextComponent* pTextCpn=(RichEditboxTextComponent*)d_FormatedComponents[i];
			if (pTextCpn)
			{
				bool bIsCaratOnCpn=(i==d_caratPos.d_ComponentIndex)?true:false;
				size_t newCaratIdx=d_caratPos.d_ComponentIndex;
				size_t newCaratOffset=d_caratPos.d_ComponentOffset;
				bool bChange=false;
				String newText("");
				bool bWaitForEmotionNum=false;
				RichEditboxComponent* pNewCpn=pTextCpn->ParseControlText(bIsCaratOnCpn,i,d_caratPos.d_ComponentIndex,
					d_caratPos.d_ComponentOffset,newCaratIdx,newCaratOffset,bChange,newText,bWaitForEmotionNum,d_AccceptEmotion);

				d_WaitEmotionNum=bWaitForEmotionNum; //??\u2211\u00F2??\u2018?\u03BC?\uFFE5?\u00B1\u00EC?\u00E8?\u2030??

				if (!bChange)
				{
					continue;
				}

				if (pNewCpn&&pNewCpn->GetType()==RichEditboxComponentType_Emotion)
				{
					RichEditboxEmotionComponent* pEmotionCpn=(RichEditboxEmotionComponent*)pNewCpn;
					pEmotionCpn->SetScale(d_EmotionScale);
				}
				size_t inserIdx=i;
				
				RichEditboxTextComponent* pNewTextCpn=NULL;

				if (!newText.empty())
				{
					pNewTextCpn=new RichEditboxTextComponent;
					pNewTextCpn->setText(newText);
					pNewTextCpn->setFont(pTextCpn->getFont());
					pNewTextCpn->setColours(pTextCpn->getColours());
				}
				if (pTextCpn->getText().empty())
				{
					DelComponent(pTextCpn,true);
					//d_FormatedComponents.erase(d_FormatedComponents.begin()+i);
					inserIdx=i>0?(i-1):0;
					
				}
				if (pNewCpn)
				{
					size_t idx=inserIdx+1;
					if (idx>=d_FormatedComponents.size())
					{
						d_FormatedComponents.push_back(pNewCpn);
					}else
					{
						d_FormatedComponents.insert(d_FormatedComponents.begin()+idx,pNewCpn);
					}
					
				}

				if (pNewTextCpn)
				{

					size_t idx=inserIdx+2;
					if (idx>=d_FormatedComponents.size())
					{
						d_FormatedComponents.push_back(pNewTextCpn);
					}else
					{
						d_FormatedComponents.insert(d_FormatedComponents.begin()+idx,pNewTextCpn);
					}
				}
				
				d_caratPos.d_ComponentIndex=newCaratIdx;
				d_caratPos.d_ComponentOffset=newCaratOffset;
				i=inserIdx;
				
			}
		}
	}

}

int RichEditbox::GetTipsNameIndex(const String& name) //a\u00F2\u03BC\u221A?\u00A8\u221A?tips???\u2211\u03BC?\u220F???
{
	int count=0;
	String noNumberName(name);
	EraseNumber(noNumberName);
	for (size_t i=0;i<d_FormatedComponents.size();++i)
	{
		if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Tips)
		{
			RichEditboxTipsLinkComponent* pTipsCpn=(RichEditboxTipsLinkComponent* )d_FormatedComponents[i];
			const String& curName=pTipsCpn->getText();

			String noNumberCurName(curName);
			EraseNumber(noNumberCurName);

			if (noNumberCurName==noNumberName)
			{
				count++;
			}
			
		}
	}
	return count;

}

void RichEditbox::EraseNumber(String& name) //??\u03BC\u00F9??\u00B1?\u00F7\u2013\u03BC????\u00F7
{
	size_t length=name.size();
	if (length<=0)
	{
		return;
	}
	length--;
    while(length>=0)
	{
		utf32 c=name[length];
		if ((c>=48&&c<=57)
			||c=='['
			||c==']'
			)
		{
			name.erase(length,1);
		}
		if (length==0)
		{
			break;
		}
		length--;

	}
	
}


void RichEditbox::CheckLineCount()
{
	//if (d_readOnly)
	{
		size_t curLineNumber=d_lines.size();
		if (curLineNumber>d_MaxLineNumber)
		{
			size_t cutLineCount=curLineNumber-d_MaxLineNumber;
			if (cutLineCount-1>=d_lines.size())
			{
				Logger::getSingleton().logEvent("richeditbox cutlLineCount greater len lineSize",Warnings);
				return;
			}
			size_t lastCpnIdx=d_lines[cutLineCount-1].d_startComponentIdx+d_lines[cutLineCount-1].d_ComponentCount-1;

			if (lastCpnIdx>=d_FormatedComponents.size())
			{
				Logger::getSingleton().logEvent("richeditbox lastCpnIdx greater len FormatCpnSize",Warnings);
				return;
			}

			for (size_t i=0;i<cutLineCount;++i)
			{
				
				d_lines.erase(d_lines.begin());
			}

			for (size_t k=0;k<=lastCpnIdx;++k)
			{
				DelComponent(*d_FormatedComponents.begin());	
			}

			for (size_t j=0;j<d_lines.size();++j)
			{
				d_lines[j].d_startComponentIdx-=lastCpnIdx+1;
			}
		}
	}
}

void RichEditbox::SetSrollBarLockTime(float time)
{
	d_ScrobarLockEnable=true;
	d_ScrobarLockTime=time;
	/*Scrollbar* vertScrollbar = getVertScrollbar();
	if (vertScrollbar)
	{
		Thumb* pThumb=vertScrollbar->getThumb();
		if (pThumb)
		{
			pThumb->subscribeEvent(Thumb::EventThumbTrackStarted,Event::Subscriber(&CEGUI::RichEditbox::handleThumbTrackStarted, this));
			pThumb->subscribeEvent(Thumb::EventThumbTrackEnded,Event::Subscriber(&CEGUI::RichEditbox::handleThumbTrackEnd, this));

		}
	}*/
}

bool RichEditbox::handleThumbTrackStarted(const EventArgs& e)
{
	if (d_ScrobarLockEnable)
	{
		d_ScrobarLockState=true;
		d_ScrobarLockElapseTime=0.0f;
		SetLockState(true);
	}
	
	return true;

}

bool RichEditbox::handleThumbTrackEnd(const EventArgs& e)
{
	Scrollbar* vertScrollbar = getVertScrollbar();
	if (vertScrollbar)
	{
		bool bThumbOnEnd=vertScrollbar->isThumbOnEnd();
		if (bThumbOnEnd)
		{
			d_ScrobarLockState=false;
			d_ScrobarLockElapseTime=0.0f;
			if (!d_UserLock)
			{
				SetLockState(false);
			}
		}
	}

	return true;

}
void RichEditbox::OnThumbLockEnd()
{
	d_ScrobarLockState=false;
	d_ScrobarLockElapseTime=0.0f;
	if (!d_UserLock)
	{
		SetLockState(false);
	}
	
}

void RichEditbox::SetBackGroundEnable(bool enable)
{ 
	if (enable!=d_BackGroundEnable)
	{
		d_BackGroundEnable=enable;
		invalidate();
	}
	 
}
void RichEditbox::SetColourRect(ColourRect Color) 
{ 
	//if (d_ColourRect!=Color)
	{
		if (!d_readOnly&&d_WaitEmotionNum)
		{
			d_AccceptEmotion=true;
			Refresh();
			d_AccceptEmotion=false;
		}
	}
	d_ColourRect=Color; 
}

String RichEditbox::GetColourString()
{
	return PropertyHelper::colourToString(d_ColourRect.d_bottom_right);
}

int RichEditbox::GetEmotionLineNum() //\u03BC\u221A\u03BC\u03A9\u201D\u2013??\u2026?\u2013\u2013\u201D\u2013\u00B1\u00EC?\u00E8
{
	int num=0;
	size_t lineNum=d_lines.size();
	for (size_t index=0;index<lineNum;++index)
	{
		size_t startindex=d_lines[index].d_startComponentIdx;
		size_t count=d_lines[index].d_ComponentCount;

		size_t totalCpnCount=d_FormatedComponents.size();
		for(size_t i=startindex;i<startindex+count;i++)
		{
			if (i>=totalCpnCount)
			{
				Logger::getSingleton().logEvent("richeditbox GetLineSize greater len FormatCpnSize",Warnings);
				break;
			}

			bool isEmotion=d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Emotion;

			if (isEmotion)
			{
				num++;
				break;
			}
		}

	}
	
	return num;
}

Vector2 RichEditbox::GetEmotionScaleByLineNum(int emotionLineNum,size_t totalLineNum ) //\u220F???\u2013\u2013??\u03BC\u221A\u03BC\u03A9\u00B1\u00EC?\u00E8\u03BC?\u2019?\u2211\u2248?\u03BC??
{

	Vector2 scale(1.0f,1.0f);

	if (emotionLineNum==2&&totalLineNum>=3)
	{
		scale.d_x=27.0f/32.0f;
		scale.d_y=27.0f/32.0f;
	}else if (emotionLineNum==3)
	{
		scale.d_x=24.0f/32.0f;
		scale.d_y=24.0f/32.0f;
	}

	return scale;

}

void RichEditbox::SetAllEmotionScale(Vector2 scale)
{
	size_t totalCpnCount=d_FormatedComponents.size();
	for(size_t i=0;i<totalCpnCount;i++)
	{
		
		bool isEmotion=d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Emotion;

		if (isEmotion)
		{
			RichEditboxEmotionComponent* pEmotion=(RichEditboxEmotionComponent*)d_FormatedComponents[i];
			if (pEmotion!=NULL)
			{
				pEmotion->SetScale(scale);
			}
			
		}
	}

	totalCpnCount=d_WaitToAddComponents.size();
	for(size_t i=0;i<totalCpnCount;i++)
	{

		bool isEmotion=d_WaitToAddComponents[i]->GetType()==RichEditboxComponentType_Emotion;

		if (isEmotion)
		{
			RichEditboxEmotionComponent* pEmotion=(RichEditboxEmotionComponent*)d_WaitToAddComponents[i];
			if (pEmotion!=NULL)
			{
				pEmotion->SetScale(scale);
			}

		}
	}

	totalCpnCount=d_RefreshComponents.size();
	for(size_t i=0;i<totalCpnCount;i++)
	{

		bool isEmotion=d_RefreshComponents[i]->GetType()==RichEditboxComponentType_Emotion;

		if (isEmotion)
		{
			RichEditboxEmotionComponent* pEmotion=(RichEditboxEmotionComponent*)d_RefreshComponents[i];
			if (pEmotion!=NULL)
			{
				pEmotion->SetScale(scale);
			}

		}
	}

}

int RichEditbox::GetEmotionNum()
{
	int num=0;
	size_t totalCpnCount=d_FormatedComponents.size();
	for(size_t i=0;i<totalCpnCount;i++)
	{

		bool isEmotion=d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Emotion;

		if (isEmotion)
		{
			num++;

		}
	}

	return num;

}

void RichEditbox::RefreshUpdateCpnList(size_t startIdx,size_t endIdx) //\u00E0\uFFE0\u2013?\u2264a?\u2026\u00B1?o\u2260?\uFFE5?\u00A8??\u03BC?\u2013?\u201C?\u221A?\u00F7\u00B0?o\u220F?\u2013?\u03BC???o?\uFFE1\u00A8\u00B1??\u00E1\u00B1\u00EC?\u00E8
{
	if (!isReadOnly())
	{
		return;
	}

	d_UpdateCpnList.clear();

	size_t cpnNum=d_FormatedComponents.size();

	if (cpnNum==0)
	{
		return;
	}

	if (endIdx<startIdx)
	{
		return;
	}

	if (startIdx<0)
	{
		return;
	}

	if (endIdx>=cpnNum)
	{
		return;
	}

	for (size_t i=startIdx;i<=endIdx;++i)
	{

		if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Emotion)
		{
			d_UpdateCpnList.push_back(i);
		}
	}

}

void RichEditbox::InsertText(const String& strText)
{
    if (!d_readOnly) {
		InsertTextInCarat(strText, d_ColourRect);
    }
    
}
    
void RichEditbox::GetAllEmotionID(std::vector<int>& emotionIds)//\u83B7\u5F97\u6240\u6709\u7684\u8868\u60C5
{
    emotionIds.clear();
    size_t totalCpnCount=d_FormatedComponents.size();
    for(size_t i=0;i<totalCpnCount;i++)
    {
        bool isEmotion=d_FormatedComponents[i]->GetType()==RichEditboxComponentType_Emotion;
        if (isEmotion)
        {
            RichEditboxEmotionComponent* pCpn=(RichEditboxEmotionComponent*)d_FormatedComponents[i];
            if (pCpn)
            {
                emotionIds.push_back(pCpn->GetAniID());
            }
        }
    }
}

RichEditboxComponent* RichEditbox::GetLinkTextOnPos(const Point& pt)
{
    if (d_lines.size()<=0||d_FormatedComponents.size()<=0)
	{
		return NULL;
	}
	Point wndPt = CoordConverter::screenToWindow(*this, pt);
    
	Rect textArea(getTextRenderArea());
    
	wndPt.d_x -= textArea.d_left;
	wndPt.d_y -= textArea.d_top;
    
	// factor in scroll bar values
	wndPt.d_x += getHorzScrollbar()->getScrollPosition();
	wndPt.d_y += getVertScrollbar()->getScrollPosition();
    
	size_t lineNumber = 0;
	//static_cast<size_t>(wndPt.d_y / getFont()->getLineSpacing());
    
	if (!GetLineByPos(lineNumber,wndPt))
	{
		return NULL;
	}
	
      
	size_t LintStartCompnIdx=d_lines[lineNumber].d_startComponentIdx;
	size_t CompnCount=d_lines[lineNumber].d_ComponentCount;
	Size lineSize=GetLineSize(lineNumber);
    
//	if (wndPt.d_x>=lineSize.d_width)
//	{
//		return NULL;
//	}
	
	for (size_t i=LintStartCompnIdx;i<=LintStartCompnIdx+CompnCount-1;i++)
	{
		if (i>d_FormatedComponents.size())
		{
			break;
		}
		
		
		if (d_FormatedComponents[i]->GetType()==RichEditboxComponentType_LinkText)
		{
            return d_FormatedComponents[i];
		}
	}
    
    return NULL;
}

bool RichEditbox::isPushedLine(size_t lineIdx)
{
    if (d_lines.size()<=0)
    {
        return  false;
    }
    
    if (lineIdx>=d_lines.size())
    {
        return false;
    }
    size_t LintStartCompnIdx=d_lines[lineIdx].d_startComponentIdx;
	size_t CompnCount=d_lines[lineIdx].d_ComponentCount;
	
	for (size_t i=LintStartCompnIdx;i<=LintStartCompnIdx+CompnCount-1;i++)
	{
		if (i>d_FormatedComponents.size())
		{
			break;
		}
		
		
		if (d_FormatedComponents[i]->isPushed())
		{
            return true;
		}
	}

    return false;
}
    
    
} // End of  CEGUI namespace section
