/***********************************************************************
    filename:   CEGUIEditbox.cpp
    created:    13/4/2004
    author:     Paul D Turner

    purpose:    Implementation of Editbox base class widget
*************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2009 Paul D Turner & The CEGUI Development Team
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

#include "elements/CEGUIEditbox.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUITextUtils.h"
#include "CEGUIExceptions.h"
#include "CEGUIFont.h"
#include "CEGUIPCRERegexMatcher.h"
#include "CEGUIBiDiVisualMapping.h"
#include <string.h>

// Start of CEGUI namespace section
namespace CEGUI
{
//----------------------------------------------------------------------------//
EditboxWindowRenderer::EditboxWindowRenderer(const String& name) :
    WindowRenderer(name, Editbox::EventNamespace)
{
}

//----------------------------------------------------------------------------//
EditboxProperties::ReadOnly         Editbox::d_readOnlyProperty;
EditboxProperties::MaskText         Editbox::d_maskTextProperty;
EditboxProperties::MaskCodepoint    Editbox::d_maskCodepointProperty;
EditboxProperties::ValidationString Editbox::d_validationStringProperty;
EditboxProperties::CaratIndex       Editbox::d_caratIndexProperty;
EditboxProperties::SelectionStart   Editbox::d_selectionStartProperty;
EditboxProperties::SelectionLength  Editbox::d_selectionLengthProperty;
EditboxProperties::MaxTextLength    Editbox::d_maxTextLengthProperty;
EditboxProperties::BackGroundEnable    Editbox::d_FrameEnabledProperty;
EditboxProperties::SupportTabKey    Editbox::d_SupportTabKeyProperty;
EditboxProperties::TabOrderID    Editbox::d_TabOrderIDProperty;

//----------------------------------------------------------------------------//
const String Editbox::EventNamespace("Editbox");
const String Editbox::WidgetTypeName("CEGUI/Editbox");
const String Editbox::EventReadOnlyModeChanged("ReadOnlyChanged");
const String Editbox::EventMaskedRenderingModeChanged("MaskRenderChanged");
const String Editbox::EventMaskCodePointChanged("MaskCPChanged");
const String Editbox::EventValidationStringChanged("ValidatorChanged");
const String Editbox::EventMaximumTextLengthChanged("MaxTextLenChanged");
const String Editbox::EventTextInvalidated("TextInvalidated");
const String Editbox::EventInvalidEntryAttempted("InvalidInputAttempt");
const String Editbox::EventCaratMoved("TextCaratMoved");
const String Editbox::EventTextSelectionChanged("TextSelectChanged");
const String Editbox::EventEditboxFull("EditboxFull");
const String Editbox::EventTextAccepted("TextAccepted");

EditboxStringParser Editbox::d_EditboxStringParser;

//----------------------------------------------------------------------------//
Editbox::Editbox(const String& type, const String& name) :
    Window(type, name),
    d_readOnly(false),
    d_maskText(false),
	d_InputPassword(false),
    d_maskCodePoint('*'),
    d_maxTextLen(String::max_size()),
    d_caratPos(0),
    d_selectionStart(0),
    d_selectionEnd(0),
    d_validator(0),
    d_dragging(false),
	d_ShieldSpace(false),
	d_CanInputBySoftBoard(true),
	d_OnlyNumberMode(false),
	d_MaxNumber(99),
	d_CanFirstIsZero(false),
	d_frameEnabled(true),
	d_NormalColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF),
	d_SuppotTabKey(false),
	d_TabOrderID(0),
	d_PlaceHolderStr("")
{
    addEditboxProperties();

	SetCanEdit(true);
	SetDragMoveEnable(false);

    // override default and disable text parsing
    d_textParsingEnabled = false;

#ifdef CEGUI_HAS_PCRE_REGEX
    d_validator = new PCRERegexMatcher;
    // default to accepting all characters
    setValidationString(".*");
#else
    // set copy of validation string to ".*" so getter returns something valid.
    d_validationString = ".*";
#endif
}

Editbox::Editbox(const String& type) :
	Window(type)
{
#ifdef CEGUI_HAS_PCRE_REGEX
	d_validator = new PCRERegexMatcher;
	// default to accepting all characters
	setValidationString(".*");
#endif
}

const Editbox& Editbox::operator=(const Editbox& t)
{
	Window::operator=(t);
	d_readOnly = t.d_readOnly;
	d_maskText = t.d_maskText;
	d_maskCodePoint = t.d_maskCodePoint;
	d_maxTextLen = t.d_maxTextLen;
	d_caratPos = t.d_caratPos;
	d_selectionStart = t.d_selectionStart;
	d_selectionEnd = t.d_selectionEnd;
	d_validationString = t.d_validationString;
	/*
	assert(t.d_validator == NULL);
	d_validator = NULL;
	*/
	d_dragging = t.d_dragging;
	d_dragAnchorIdx = t.d_dragAnchorIdx;
	d_ShieldSpace = t.d_ShieldSpace;
	d_CanInputBySoftBoard = t.d_CanInputBySoftBoard;
	d_InputPassword = t.d_InputPassword;
	d_NormalColourRect = t.d_NormalColourRect;
	d_OnlyNumberMode = t.d_OnlyNumberMode;
	d_MaxNumber = t.d_MaxNumber;
	d_CanFirstIsZero = t.d_CanFirstIsZero;
	d_frameEnabled = t.d_frameEnabled;
	d_SuppotTabKey = t.d_SuppotTabKey;
	d_TabOrderID = t.d_TabOrderID;
	return *this;
}

Window* Editbox::clone(Window* wnd)
{
	Editbox* retWnd = (Editbox*)wnd;
	if (retWnd == NULL)
		retWnd = new Editbox(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == Editbox::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

//----------------------------------------------------------------------------//
Editbox::~Editbox(void)
{
    delete d_validator;
}

//----------------------------------------------------------------------------//
bool Editbox::hasInputFocus(void) const
{
    return !d_readOnly;
}

//----------------------------------------------------------------------------//
bool Editbox::isTextValid(void) const
{
    return isStringValid(getText());
}

//----------------------------------------------------------------------------//
size_t Editbox::getSelectionStartIndex(void) const
{
    return (d_selectionStart != d_selectionEnd) ? d_selectionStart : d_caratPos;
}

//----------------------------------------------------------------------------//
size_t Editbox::getSelectionEndIndex(void) const
{
    return (d_selectionStart != d_selectionEnd) ? d_selectionEnd : d_caratPos;
}

//----------------------------------------------------------------------------//
size_t Editbox::getSelectionLength(void) const
{
    return d_selectionEnd - d_selectionStart;
}

//----------------------------------------------------------------------------//
void Editbox::setReadOnly(bool setting)
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
		}else
		{
			SetCanEdit(true);
		}
    }

}

//----------------------------------------------------------------------------//
void Editbox::setTextMasked(bool setting)
{
    // if setting is changed
    if (d_maskText != setting)
    {
        d_maskText = setting;
        WindowEventArgs args(this);
        onMaskedRenderingModeChanged(args);
    }

}

//----------------------------------------------------------------------------//
void Editbox::setValidationString(const String& validation_string)
{
#ifdef CEGUI_HAS_PCRE_REGEX
    if (validation_string == d_validationString)
        return;

    d_validationString = validation_string;
    d_validator->setRegexString(validation_string);

    // notification
    WindowEventArgs args(this);
    onValidationStringChanged(args);

    // also notify if text is now invalid.
    if (!isTextValid())
    {
        args.handled = 0;
        onTextInvalidatedEvent(args);
    }
#else
    throw InvalidRequestException("Editbox::setValidationString: Unable to set "
        "validation string because CEGUI was compiled without regular "
        "expression support");
#endif
}

//----------------------------------------------------------------------------//
void Editbox::setCaratIndex(size_t carat_pos)
{
    // make sure new position is valid
    if (carat_pos > getText().length())
        carat_pos = getText().length();

    // if new position is different
    if (d_caratPos != carat_pos)
    {
        d_caratPos = carat_pos;

        // Trigger "carat moved" event
        WindowEventArgs args(this);
        onCaratMoved(args);
    }

}

//----------------------------------------------------------------------------//
void Editbox::setSelection(size_t start_pos, size_t end_pos)
{
	//设定密码时不能选中
	if(d_InputPassword)
		return;

    // ensure selection start point is within the valid range
    if (start_pos > getText().length())
        start_pos = getText().length();

    // ensure selection end point is within the valid range
    if (end_pos > getText().length())
        end_pos = getText().length();

    // ensure start is before end
    if (start_pos > end_pos)
    {
        size_t tmp = end_pos;
        end_pos = start_pos;
        start_pos = tmp;
    }

    // only change state if values are different.
    if ((start_pos != d_selectionStart) || (end_pos != d_selectionEnd))
    {
        // setup selection
        d_selectionStart = start_pos;
        d_selectionEnd  = end_pos;

        // Trigger "selection changed" event
        WindowEventArgs args(this);
        onTextSelectionChanged(args);
    }

}

void Editbox::SelectAll()
{
	if (getText().length()>0)
	{
	}
	
	setSelection(0,getText().length());
}


//----------------------------------------------------------------------------//
void Editbox::setMaskCodePoint(utf32 code_point)
{
    if (code_point != d_maskCodePoint)
    {
        d_maskCodePoint = code_point;

        // Trigger "mask code point changed" event
        WindowEventArgs args(this);
        onMaskCodePointChanged(args);
    }

}

//----------------------------------------------------------------------------//
void Editbox::setMaxTextLength(size_t max_len)
{
    if (d_maxTextLen != max_len)
    {
        d_maxTextLen = max_len;

        // Trigger max length changed event
        WindowEventArgs args(this);
        onMaximumTextLengthChanged(args);

        // trim string
        if (getText().length() > d_maxTextLen)
        {
            String newText = getText();
            newText.resize(d_maxTextLen);
            setText(newText);
            onTextChanged(args);

            // see if new text is valid
            if (!isTextValid())
            {
                // Trigger Text is invalid event.
                onTextInvalidatedEvent(args);
            }

        }

    }

}

//----------------------------------------------------------------------------//
void Editbox::clearSelection(void)
{
    // perform action only if required.
    if (getSelectionLength() != 0)
        setSelection(0, 0);
}

//----------------------------------------------------------------------------//
void Editbox::eraseSelectedText(bool modify_text)
{
    if (getSelectionLength() != 0)
    {
        // setup new carat position and remove selection highlight.
        setCaratIndex(d_selectionStart);
        clearSelection();

        // erase the selected characters (if required)
        if (modify_text)
        {
            String newText = getText();
            newText.erase(getSelectionStartIndex(), getSelectionLength());
            setText(newText);

            // trigger notification that text has changed.
            WindowEventArgs args(this);
            onTextChanged(args);
        }

    }

}

//----------------------------------------------------------------------------//
bool Editbox::isStringValid(const String& str) const
{
    return d_validator ? d_validator->matchRegex(str) : true;
}

//----------------------------------------------------------------------------//
void Editbox::onMouseButtonDown(MouseEventArgs& e)
{
    // base class handling
    Window::onMouseButtonDown(e);

    if (e.button == LeftButton)
    {
        // grab inputs
        if (captureInput())
        {
            // handle mouse down
            clearSelection();
			//输入密码时，光标不能随便插入
			if(d_InputPassword)
			{
				setCaratIndex(getText().length());
			}
			else
			{
				d_dragging = true;
				d_dragAnchorIdx = getTextIndexFromPosition(e.position);
#ifdef CEGUI_BIDI_SUPPORT
				if (d_bidiVisualMapping->getV2lMapping().size() > d_dragAnchorIdx)
					d_dragAnchorIdx =
					d_bidiVisualMapping->getV2lMapping()[d_dragAnchorIdx];
#endif
				setCaratIndex(d_dragAnchorIdx);
			}
          
        }

        ++e.handled;
    }

}

//----------------------------------------------------------------------------//
void Editbox::onMouseButtonUp(MouseEventArgs& e)
{
    // base class processing
    Window::onMouseButtonUp(e);

    if (e.button == LeftButton)
    {
        releaseInput();
        ++e.handled;
    }

}

//----------------------------------------------------------------------------//
void Editbox::onMouseDoubleClicked(MouseEventArgs& e)
{
    // base class processing
    Window::onMouseDoubleClicked(e);
	
    if (e.button == LeftButton)
    {
		if(d_InputPassword)
			return;

        // if masked, set up to select all
        if (isTextMasked())
        {
            d_dragAnchorIdx = 0;
            setCaratIndex(getText().length());
        }
        // not masked, so select the word that was double-clicked.
        else
        {
            d_dragAnchorIdx = TextUtils::getWordStartIdx(getText(),
                (d_caratPos == getText().length()) ? d_caratPos :
                                                     d_caratPos + 1);
            d_caratPos = TextUtils::getNextWordStartIdx(getText(), d_caratPos);
        }

        // perform actual selection operation.
        setSelection(d_dragAnchorIdx, d_caratPos);

        ++e.handled;
    }

}

//----------------------------------------------------------------------------//
void Editbox::onMouseTripleClicked(MouseEventArgs& e)
{
    // base class processing
    Window::onMouseTripleClicked(e);

    if (e.button == LeftButton)
    {
		if(d_InputPassword)
			return;

        d_dragAnchorIdx = 0;
        setCaratIndex(getText().length());
        setSelection(d_dragAnchorIdx, d_caratPos);
        ++e.handled;
    }

}

//----------------------------------------------------------------------------//
void Editbox::onMouseMove(MouseEventArgs& e)
{
    // base class processing
    Window::onMouseMove(e);

    if (d_dragging)
    {
        size_t anchorIdx = getTextIndexFromPosition(e.position);
#ifdef CEGUI_BIDI_SUPPORT
        if (d_bidiVisualMapping->getV2lMapping().size() > anchorIdx)
            anchorIdx = d_bidiVisualMapping->getV2lMapping()[anchorIdx];
#endif
        setCaratIndex(anchorIdx);

        setSelection(d_caratPos, d_dragAnchorIdx);
    }

    ++e.handled;
}

//----------------------------------------------------------------------------//
void Editbox::onCaptureLost(WindowEventArgs& e)
{
    d_dragging = false;

    // base class processing
    Window::onCaptureLost(e);

    ++e.handled;
}

//----------------------------------------------------------------------------//
void Editbox::onCharacter(KeyEventArgs& e)
{
    // NB: We are not calling the base class handler here because it propogates
    // inputs back up the window hierarchy, whereas, as a consumer of key
    // events, we want such propogation to cease with us regardless of whether
    // we actually handle the event.

	if(d_ShieldSpace && e.codepoint == 32)
		return;

	if(IsCanInputBySoftBoard() == false)
		return;

    // fire event.
    fireEvent(EventCharacterKey, e, Window::EventNamespace);

    // only need to take notice if we have focus
    if (e.handled == 0 && /*hasInputFocus()*/!isReadOnly() &&
        getFont()->isCodepointAvailable(e.codepoint))
    {
        // backup current text
        String tmp(getText());
        tmp.erase(getSelectionStartIndex(), getSelectionLength());

        // if there is room
        if (tmp.length() < d_maxTextLen)  // 一个汉字算两个字符
        {
            tmp.insert(getSelectionStartIndex(), 1, e.codepoint);

            if (isStringValid(tmp))
            {
                // erase selection using mode that does not modify getText()
                // (we just want to update state)
                eraseSelectedText(false);

                // advance carat (done first so we can "do stuff" in event
                // handlers!)
                d_caratPos++;

                // set text to the newly modified string
                setText(tmp);

                // char was accepted into the Editbox - mark event as handled.
                ++e.handled;
            }
            else
            {
                // Trigger invalid modification attempted event.
                WindowEventArgs args(this);
                onInvalidEntryAttempted(args);
            }

        }
        else
        {
            // Trigger text box full event
            WindowEventArgs args(this);
            onEditboxFullEvent(args);
        }

    }

    // event was (possibly) not handled
}

void Editbox::InsertStringInCarat(const String& text)
{
	if ( hasInputFocus() && !isReadOnly() &&
		text.length()>0)
	{
		// backup current text
		String tmp(getText());
		tmp.erase(getSelectionStartIndex(), getSelectionLength());

		// if there is room
		if (tmp.length() < d_maxTextLen)
		{
			String insertString(text);
			if (tmp.length()+text.length()>d_maxTextLen)
			{
				WindowEventArgs args(this);
				onEditboxFullEvent(args);
				insertString=insertString.substr(0,d_maxTextLen-tmp.length());
			}
			tmp.insert(getSelectionStartIndex(), insertString);

			if (true/*isStringValid(tmp)*/) // yeqing show valid string anyway
			{
				// erase selection using mode that does not modify getText()
				// (we just want to update state)
				eraseSelectedText(false);

				// advance carat (done first so we can "do stuff" in event
				// handlers!)
				d_caratPos+=insertString.length();

				// set text to the newly modified string
				setText(tmp);

				// char was accepted into the Editbox - mark event as handled.
			}
			else
			{
				// Trigger invalid modification attempted event.
				WindowEventArgs args(this);
				onInvalidEntryAttempted(args);
			}

		}
		else
		{
			// Trigger text box full event
			WindowEventArgs args(this);
			onEditboxFullEvent(args);
		}

	}
	
}


//----------------------------------------------------------------------------//
void Editbox::onKeyDown(KeyEventArgs& e)
{
    // NB: We are not calling the base class handler here because it propogates
    // inputs back up the window hierarchy, whereas, as a consumer of key
    // events, we want such propogation to cease with us regardless of whether
    // we actually handle the event.

    // fire event.

	//当前状态如果为软件盘输入，不屏蔽退格和Delete操作
	if(IsCanInputBySoftBoard() == false && e.scancode != Key::Backspace && e.scancode != Key::Delete)
		return;

    fireEvent(EventKeyDown, e, Window::EventNamespace);
	
    if (e.handled == 0 &&/* hasInputFocus()*/!isReadOnly())
    {
        WindowEventArgs args(this);
        switch (e.scancode)
        {
        case Key::LeftShift:
        case Key::RightShift:
            if (getSelectionLength() == 0)
                d_dragAnchorIdx = d_caratPos;
            break;

        case Key::Backspace:
            handleBackspace();
            break;

        case Key::Delete:
            handleDelete();
            break;

        //case Key::Tab
        case Key::Return:
        case Key::NumpadEnter:
            // Fire 'input accepted' event
            onTextAcceptedEvent(args);
            break;

        case Key::ArrowLeft:
            if (e.sysKeys & Control)
                handleWordLeft(e.sysKeys);
            else
                handleCharLeft(e.sysKeys);
            break;

        case Key::ArrowRight:
            if (e.sysKeys & Control)
                handleWordRight(e.sysKeys);
            else
                handleCharRight(e.sysKeys);
            break;

        case Key::Home:
            handleHome(e.sysKeys);
            break;

        case Key::End:
            handleEnd(e.sysKeys);
            break;
		case Key::Space:
			break;
			//复制
		case Key::C:
			//if (::GetKeyState(VK_CONTROL) < 0)
			//{
			//	HandleCopyToClipBoard();
			//}
			break;
			//粘贴
		case Key::V:
			//if (::GetKeyState(VK_CONTROL) < 0)
			//{
			//	HandlePasteFromClipBoard();
			//}
			break;
		case Key::Tab:
			{
				HandleTab();
			}
			break;
            // default case is now to leave event as (possibly) unhandled.
        default:
            return;
        }

        ++e.handled;
    }

}

//----------------------------------------------------------------------------//
void Editbox::handleBackspace(void)
{
    if (!isReadOnly())
    {
        String tmp(getText());

        if (getSelectionLength() != 0)
        {
            tmp.erase(getSelectionStartIndex(), getSelectionLength());

            if (isStringValid(tmp))
            {
                // erase selection using mode that does not modify getText()
                // (we just want to update state)
                eraseSelectedText(false);

                // set text to the newly modified string
                setText(tmp);
            }
            else
            {
                // Trigger invalid modification attempted event.
                WindowEventArgs args(this);
                onInvalidEntryAttempted(args);
            }

        }
        else if (getCaratIndex() > 0)
        {
            tmp.erase(d_caratPos - 1, 1);

            if (isStringValid(tmp))
            {
                setCaratIndex(d_caratPos - 1);

                // set text to the newly modified string
                setText(tmp);
            }
            else
            {
                // Trigger invalid modification attempted event.
                WindowEventArgs args(this);
                onInvalidEntryAttempted(args);
            }

        }

    }

}

//----------------------------------------------------------------------------//
void Editbox::handleDelete(void)
{
    if (!isReadOnly())
    {
        String tmp(getText());

        if (getSelectionLength() != 0)
        {
            tmp.erase(getSelectionStartIndex(), getSelectionLength());

            if (isStringValid(tmp))
            {
                // erase selection using mode that does not modify getText()
                // (we just want to update state)
                eraseSelectedText(false);

                // set text to the newly modified string
                setText(tmp);
            }
            else
            {
                // Trigger invalid modification attempted event.
                WindowEventArgs args(this);
                onInvalidEntryAttempted(args);
            }

        }
        else if (getCaratIndex() < tmp.length())
        {
            tmp.erase(d_caratPos, 1);

            if (isStringValid(tmp))
            {
                // set text to the newly modified string
                setText(tmp);
            }
            else
            {
                // Trigger invalid modification attempted event.
                WindowEventArgs args(this);
                onInvalidEntryAttempted(args);
            }

        }

    }

}

//----------------------------------------------------------------------------//
void Editbox::handleCharLeft(uint sysKeys)
{
	if(d_InputPassword)
		return;

    if (d_caratPos > 0)
        setCaratIndex(d_caratPos - 1);

    if (sysKeys & Shift)
        setSelection(d_caratPos, d_dragAnchorIdx);
    else
        clearSelection();
}

//----------------------------------------------------------------------------//
void Editbox::handleWordLeft(uint sysKeys)
{
	if(d_InputPassword)
		return;

    if (d_caratPos > 0)
        setCaratIndex(TextUtils::getWordStartIdx(getText(), d_caratPos));

    if (sysKeys & Shift)
        setSelection(d_caratPos, d_dragAnchorIdx);
    else
        clearSelection();
}

//----------------------------------------------------------------------------//
void Editbox::handleCharRight(uint sysKeys)
{
	if(d_InputPassword)
		return;

    if (d_caratPos < getText().length())
        setCaratIndex(d_caratPos + 1);

    if (sysKeys & Shift)
        setSelection(d_caratPos, d_dragAnchorIdx);
    else
        clearSelection();
}

//----------------------------------------------------------------------------//
void Editbox::handleWordRight(uint sysKeys)
{
	if(d_InputPassword)
		return;

    if (d_caratPos < getText().length())
        setCaratIndex(TextUtils::getNextWordStartIdx(getText(), d_caratPos));

    if (sysKeys & Shift)
        setSelection(d_caratPos, d_dragAnchorIdx);
    else
        clearSelection();
}

//----------------------------------------------------------------------------//
void Editbox::handleHome(uint sysKeys)
{
    if (d_caratPos > 0)
        setCaratIndex(0);

    if (sysKeys & Shift)
        setSelection(d_caratPos, d_dragAnchorIdx);
    else
        clearSelection();
}

//----------------------------------------------------------------------------//
void Editbox::handleEnd(uint sysKeys)
{
    if (d_caratPos < getText().length())
        setCaratIndex(getText().length());

    if (sysKeys & Shift)
        setSelection(d_caratPos, d_dragAnchorIdx);
    else
        clearSelection();
}

//----------------------------------------------------------------------------//
void Editbox::onReadOnlyChanged(WindowEventArgs& e)
{
    invalidate();
    fireEvent(EventReadOnlyModeChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onMaskedRenderingModeChanged(WindowEventArgs& e)
{
    invalidate();
    fireEvent(EventMaskedRenderingModeChanged , e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onMaskCodePointChanged(WindowEventArgs& e)
{
    // if we are in masked mode, trigger a GUI redraw.
    if (isTextMasked())
        invalidate();

    fireEvent(EventMaskCodePointChanged , e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onValidationStringChanged(WindowEventArgs& e)
{
    fireEvent(EventValidationStringChanged , e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onMaximumTextLengthChanged(WindowEventArgs& e)
{
    fireEvent(EventMaximumTextLengthChanged , e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onTextInvalidatedEvent(WindowEventArgs& e)
{
    fireEvent(EventTextInvalidated, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onInvalidEntryAttempted(WindowEventArgs& e)
{
    fireEvent(EventInvalidEntryAttempted , e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onCaratMoved(WindowEventArgs& e)
{
    invalidate();
    fireEvent(EventCaratMoved , e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onTextSelectionChanged(WindowEventArgs& e)
{
    invalidate();
    fireEvent(EventTextSelectionChanged , e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onEditboxFullEvent(WindowEventArgs& e)
{
    fireEvent(EventEditboxFull, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Editbox::onTextAcceptedEvent(WindowEventArgs& e)
{
    fireEvent(EventTextAccepted, e, EventNamespace);
}

void Editbox::onShown(WindowEventArgs& e)
{
	 setCaratIndex(getText().length());
	Window::onShown(e);
}

void Editbox::activate()
{
	Window::activate();
	setCaratIndex(getText().length());
}

//----------------------------------------------------------------------------//
void Editbox::onTextChanged(WindowEventArgs& e)
{
    // base class processing

	if (!isTextParsingEnabled())
	{
		String newText("");
		bool bChange=d_EditboxStringParser.parse(getText(),newText);

		if (bChange)
		{
			setText(newText,0,false);
		}
	}
	if (d_OnlyNumberMode)
	{
		if (!getText().empty())
		{
			int64_t curNumber=PropertyHelper::stringToInt64(getText());
			if (curNumber>d_MaxNumber&&d_MaxNumber>=0)
			{
				setText(PropertyHelper::int64_tToString(d_MaxNumber));
			}else
			{
				if (!d_CanFirstIsZero)
				{
					setText(PropertyHelper::int64_tToString(curNumber));
				}

			}

		}
		
	}

	
    Window::onTextChanged(e);

    // clear selection
    clearSelection();

    // make sure carat is within the text
    if (d_caratPos > getText().length())
        setCaratIndex(getText().length());

    ++e.handled;
}

//----------------------------------------------------------------------------//
void Editbox::addEditboxProperties(void)
{
	addProperty(&d_readOnlyProperty);
	addProperty(&d_maskTextProperty);
	addProperty(&d_maskCodepointProperty);
	addProperty(&d_validationStringProperty);
	addProperty(&d_caratIndexProperty);
	addProperty(&d_selectionStartProperty);
	addProperty(&d_selectionLengthProperty);
	addProperty(&d_maxTextLengthProperty);
	addProperty(&d_FrameEnabledProperty);
	addProperty(&d_SupportTabKeyProperty);
	addProperty(&d_TabOrderIDProperty);
}

//----------------------------------------------------------------------------//
size_t Editbox::getTextIndexFromPosition(const Point& pt) const
{
    if (d_windowRenderer != 0)
    {
        EditboxWindowRenderer* wr = (EditboxWindowRenderer*)d_windowRenderer;
        return wr->getTextIndexFromPosition(pt);
    }
    else
    {
        throw InvalidRequestException("Editbox::getTextIndexFromPosition: "
            "This function must be implemented by the window renderer");
    }
}

//----------------------------------------------------------------------------//
size_t Editbox::getCaratIndex(void) const
{
#ifdef CEGUI_BIDI_SUPPORT
    size_t caratPos = d_caratPos;
    if (d_bidiVisualMapping->getL2vMapping().size() > caratPos)
        caratPos = d_bidiVisualMapping->getL2vMapping()[caratPos];
#endif

   return d_caratPos;
}

void Editbox::SetOnlyNumberMode(bool bOnlyNumber,int64_t max_Number,bool bCanFirstIsZero)
{
	d_OnlyNumberMode=bOnlyNumber;
	if (bOnlyNumber)
	{
		setValidationString("\\d*");
	}else
	{
		setValidationString(".*");
	}

	d_MaxNumber=max_Number;
	d_CanFirstIsZero=bCanFirstIsZero;
}

void Editbox::HandleCopyToClipBoard()
{
	//火星文密码，不能复制
	if(d_maskText)
		return;

	String srcText(getText());
	String copyText=srcText.substr(getSelectionStartIndex(),getSelectionLength());
	if (copyText.size()>0)
	{
		OnCopyToClipBord* pFunc=System::getSingleton().GetCopyToClipBordFunc();
		if (pFunc)
		{
			(*pFunc)(copyText);
		}
	}
}

String Editbox::GetSelectText()
{
	String srcText(getText());
	String selectText=srcText.substr(getSelectionStartIndex(),getSelectionLength());

	return selectText;

}


void Editbox::HandlePasteFromClipBoard() //从剪切板粘贴
{
	OnPasteFromClipBord* pFunc=System::getSingleton().GetPasteFromClipBordFunc();
	if (pFunc)
	{
		String srcText("");
		(*pFunc)(srcText);
		if (srcText.size()>0)
		{
			InsertStringInCarat(srcText);
		}
	}
	
}

void Editbox::SetNormalColourRect(argb_t color)
{
	colour cl(color);
	SetNormalColourRect(cl);

}
void Editbox::SetNormalColourRect(const ColourRect& clRect)
{
	d_NormalColourRect=clRect;
	invalidate();
	
}
void Editbox::SetNormalColourRect(const colour& col)
{
	ColourRect clRect(col);
	SetNormalColourRect(clRect);
}

void Editbox::SetFrameEnabled(bool bFrameEnable) 
{ 
	if (d_frameEnabled!=bFrameEnable)
	{	
		d_frameEnabled = bFrameEnable;
		invalidate();
	}
}

void Editbox::HandleTab()
{
	if (!d_readOnly&&d_SuppotTabKey)
	{
		Editbox* pNextEditBox=FindNextInputBox();
		if (pNextEditBox)
		{
			pNextEditBox->activate();
		}
	}

}

Editbox* Editbox::FindNextInputBox()
{
	if (d_parent)
	{
		return d_parent->GetNextEditBox(d_TabOrderID);
	}

	return NULL;

}
    
void Editbox::InsertText(const String& strText)
{
   
    InsertStringInCarat(strText);
   
}




//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
