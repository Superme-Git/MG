/***********************************************************************
filename: 	CEGUIRichEditboxHttpComponent.h
purpose:	连接网页的控件
*************************************************************************/

#ifndef _CEGUIRichEditboxHttpComponent_h_
#define _CEGUIRichEditboxHttpComponent_h_

#include "CEGUIRichEditboxComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	
	class CEGUIEXPORT RichEditboxHttpComponent:public RichEditboxComponent 
	{
	public:

		//! Destructor.
		virtual ~RichEditboxHttpComponent();



		RichEditboxHttpComponent();
		RichEditboxHttpComponent(const String& text,const String& httpaddress);
		RichEditboxHttpComponent(const String& text,const String& httpaddress,const String& font_name);
		RichEditboxHttpComponent(const String& text,const String& httpaddress,Font* font);

		static const String EventNamespace;

		static const String EventLeftButtonClick;			//!< The read-only mode for the edit box has been changed.

		//! Set the text to be rendered by this component.
		void setText(const String& text);
		//! return the text that will be rendered by this component
		const String& getText() const;

		//! Set the text to be rendered by this component.
		void setHttpAddress(const String& address);
		//! return the text that will be rendered by this component
		const String& getHttpAddress() const;

		//! set the font to use when rendering the text.
		void setFont(Font* font);
		//! set the font to use when rendering the text.
		void setFont(const String& font_name);
		//! return the font set to be used.  If 0 the default font will be used.
		Font* getFont() const;
		//! Set the colour values used when rendering this component.
		void setColours(const ColourRect& cr);
		//! Set the colour values used when rendering this component.
		void setColours(const colour& c);
		//! return the ColourRect object used when drawing this component.
		const ColourRect& getColours() const;

		//void setGoToFunction(GoToFunction* func) { d_GoToFunction = func;}

		//! draw the component.
		void draw(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect,
			const float vertical_space,
			const float space_extra,
			bool bBorder=false)  ;

		virtual void DrawSelectFlag(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect,
			const Image* pSeleImage=NULL,
			size_t selectStartIdx=0,size_t selectEndIdx=0);

		//! return the pixel size of the rendered component.
		 Size getPixelSize();

		//! return whether the component can be split
		bool canSplit()  ;

		RichEditboxComponent* split(float split_point,bool first_component);

		//! clone this component.
		RichEditboxComponent* clone() { return NULL;}

		//! return the total number of spacing characters in the string.
		size_t getSpaceCount() ;
		Point GetOffsetOfIndex(size_t index);
		size_t GetMaxIndex() { return 1; }

		size_t GetIndexByWidth(float with);
		bool HandleDelte(size_t offset);
		
		String GenerateParseText();
		String GeneratePureText(size_t startPos,size_t endPos) { return getText(); }

		bool isPushed() { return d_Pushed; }
		bool isTextType() { return true; }
		
		bool	onMouseButtonDown(MouseEventArgs& e);
		bool    onMouseButtonUp(MouseEventArgs& e);
		void    onMouseLeave();
		void    onMouseEnter();
	
	private:
		String d_text;
		String d_httpAddress;
		//! Font to use for text rendering, 0 for system default.
		Font* d_font;
		//! ColourRect object describing the colours to use when rendering.
		ColourRect d_colours;
		static ColourRect d_HoverColours;
		static ColourRect d_PushedColours;
		bool d_Pushed;

		//GoToFunction* d_GoToFunction;//寻路链接函数
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
