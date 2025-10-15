#ifndef _FalRichEditbox_h_
#define _FalRichEditbox_h_

#include "FalModule.h"
#include "../../elements/CEGUIRichEditbox.h"
#include "FalRichEditboxProperties.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
	/*!
	\brief
	MultiLineEditbox class for the FalagardBase module.

	This class requires LookNFeel to be assigned.  The LookNFeel should provide the following:

	States:
	- Enabled    - Rendering for when the editbox is in enabled and is in read-write mode.
	- ReadOnly  - Rendering for when the editbox is in enabled and is in read-only mode.
	- Disabled  - Rendering for when the editbox is disabled.

	NamedAreas:
	TextArea         - area where text, selection, and carat imagery will appear.
	TextAreaHScroll  - TextArea when only horizontal scrollbar is visible.
	TextAreaVScroll  - TextArea when only vertical scrollbar is visible.
	TextAreaHVScroll - TextArea when both horizontal and vertical scrollbar is visible.

	PropertyDefinitions (optional, defaults will be black):
	- NormalTextColour        - property that accesses a colour value to be used to render normal unselected text.
	- SelectedTextColour      - property that accesses a colour value to be used to render selected text.
	- ActiveSelectionColour   - property that accesses a colour value to be used to render active selection highlight.
	- InactiveSelectionColour - property that accesses a colour value to be used to render inactive selection highlight.

	Imagery Sections:
	- Carat

	Child Widgets:
	Scrollbar based widget with name suffix "__auto_vscrollbar__"
	Scrollbar based widget with name suffix "__auto_hscrollbar__"

	*/
	class FALAGARDBASE_API FalagardRichEditbox : public RichEditboxWindowRenderer
	{
	public:
		static const utf8   TypeName[];     //! type name for this widget.
		//! Name of property to use to obtain unselected text rendering colour.
		static const String UnselectedTextColourPropertyName;
		//! Name of property to use to obtain selected text rendering colour.
		static const String SelectedTextColourPropertyName;
		//! Name of property to use to obtain active selection rendering colour.
		static const String ActiveSelectionColourPropertyName;
		//! Name of property to use to obtain inactive selection rendering colour.
		static const String InactiveSelectionColourPropertyName;
		//! The default timeout (in seconds) used when blinking the caret.
		static const float DefaultCaretBlinkTimeout;

		/*!
		\brief
		Constructor
		*/
		FalagardRichEditbox(){}
		FalagardRichEditbox(const String& type);

		// overridden from base classes.
		Rect getTextRenderArea(void) const;
		void render();
		void update(float elapsed);

		//! return whether the blinking caret is enabled.
		bool isCaretBlinkEnabled() const;
		//! return the caret blink timeout period (only used if blink is enabled).
		float getCaretBlinkTimeout() const;
		//! set whether the blinking caret is enabled.
		void setCaretBlinkEnabled(bool enable);
		//! set the caret blink timeout period (only used if blink is enabled).
		void setCaretBlinkTimeout(float seconds);

		void FixStartDrawLineIdx() { d_FixStartDrawLineIdx=false; }

		float GetLastTestOffest() { return d_lastTextOffset; }

		virtual void clone(const WindowRenderer* templateRenderer)
		{
			RichEditboxWindowRenderer::clone(templateRenderer);
			const FalagardRichEditbox* t = dynamic_cast<const FalagardRichEditbox*>(templateRenderer);
			assert(t);
			d_blinkCaret = t->d_blinkCaret;
			d_caretBlinkTimeout = t->d_caretBlinkTimeout;
			d_caretBlinkElapsed = t->d_caretBlinkElapsed;
			d_showCaret = t->d_showCaret;
			d_lastTextOffset = t->d_lastTextOffset;
			d_StartDrawLineIdx = t->d_StartDrawLineIdx;
			d_StartDrawLineYoffset = t->d_StartDrawLineYoffset;
			d_FixStartDrawLineIdx = t->d_FixStartDrawLineIdx;
			d_SelectBrushLeft = t->d_SelectBrushLeft;
			d_SelectBrushMiddle = t->d_SelectBrushMiddle;
			d_SelectBrushRight = t->d_SelectBrushRight;
			d_PushedBrushLeft = t->d_PushedBrushLeft;
			d_PushedBrushMiddle = t->d_PushedBrushMiddle;
			d_PushedBrushRight = t->d_PushedBrushRight;
		}
	protected:
		/*!
		\brief
		Perform rendering of the widget control frame and other 'static' areas.  This
		method should not render the actual text.  Note that the text will be rendered
		to layer 4 and the selection brush to layer 3, other layers can be used for
		rendering imagery behind and infront of the text & selection..

		\return
		Nothing.
		*/
		void cacheEditboxBaseImagery();

		/*!
		\brief
		Render the carat.

		\return
		Nothing
		*/
		void cacheCaratImagery(const Rect& textArea);

		/*!
		\brief
		Render text lines.
		*/
		void cacheTextLines(const Rect& dest_area);
       //重置光标及整个文本的绘制横向偏移，主要是针对单行可编辑,解决输入字过多后面的文字不画的问题
		void ResetCaratDrawPos(const Rect& dest_area);  

		/*!
		\brief
		return the colour to be used for rendering Editbox text oustside of the
		selected region.

		\return
		colour value describing the colour to be used.
		*/
		colour getUnselectedTextColour() const;

		/*!
		\brief
		return the colour to be used for rendering the selection highlight
		when the editbox is active.

		\return
		colour value describing the colour to be used.
		*/
		colour getActiveSelectionColour() const;

		/*!
		\brief
		return the colour to be used for rendering the selection highlight
		when the editbox is inactive.

		\return
		colour value describing the colour to be used.
		*/
		colour getInactiveSelectionColour() const;

		/*!
		\brief
		return the colour to be used for rendering Editbox text falling within
		the selected region.

		\return
		colour value describing the colour to be used.
		*/
		colour getSelectedTextColour() const;

		/*!
		\brief
		Return a colour object fetched from the named property if it exists,
		else a default colour (black).

		\param propertyName
		String object holding the name of the property to be accessed if it
		exists.
		*/
		colour getOptionalPropertyColour(const String& propertyName) const;

		// properties
		static FalagardRichEditboxProperties::BlinkCaret d_blinkCaretProperty;
		static FalagardRichEditboxProperties::BlinkCaretTimeout d_blinkCaretTimeoutProperty;

		//! true if the caret imagery should blink.
		bool d_blinkCaret;
		//! time-out in seconds used for blinking the caret.
		float d_caretBlinkTimeout;
		//! current time elapsed since last caret blink state change.
		float d_caretBlinkElapsed;
		//! true if caret should be shown.
		bool d_showCaret;
		float d_lastTextOffset;
		size_t d_StartDrawLineIdx;
		float d_StartDrawLineYoffset;
		bool d_FixStartDrawLineIdx;
        
        void drawLineBrush(GeometryBuffer* buffer, const Rect& targetRect, const Rect* clipper, const ColourRect& colorRect) const;
        void drawPushedBrush(GeometryBuffer* buffer, const Rect& targetRect, const Rect* clipper, const ColourRect& colorRect) const;
		const Image* d_SelectBrushLeft;
		const Image* d_SelectBrushMiddle;
		const Image* d_SelectBrushRight;
        
        const Image* d_PushedBrushLeft;
		const Image* d_PushedBrushMiddle;
		const Image* d_PushedBrushRight;
	};

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif  // end of guard _FalMultiLineEditbox_h_