/***********************************************************************
filename: 	CEGUIRichEditbox.h
purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxGoToComponent_h_
#define _CEGUIRichEditboxGoToComponent_h_

#include "CEGUIRichEditboxComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	struct DestPos
	{
		int mapid;
		int x;
		int y;
		int npcid;
        int64_t npckey;
		int chefu;
        bool gotodirectly;
		DestPos()
		{
			mapid = 0;
			x = 0;
			y = 0;
			npcid = 0;
            npckey = 0;
			chefu = 0;
            gotodirectly = false;
		}

	};

	class CEGUIEXPORT RichEditboxGoToComponent:public RichEditboxComponent 
	{
	public:

		struct DestPos
		{
			int mapid;
			int x;
			int y;
			int npcid;
            int64_t npckey;
			int chefu;
            bool gotodirectly;
			DestPos()
			{
				mapid = 0;
				x = 0;
				y = 0;
				npcid = 0;
                npckey = 0;
				chefu = 0;
                gotodirectly = false;
			}

			void operator=(const DestPos& rhs)
			{
				mapid = rhs.mapid;
				x = rhs.x;
				y = rhs.y;
				npcid = rhs.npcid;
				chefu = rhs.chefu;
                gotodirectly = rhs.gotodirectly;
			}

		};

		//! Destructor.
		virtual ~RichEditboxGoToComponent();



		RichEditboxGoToComponent();
		RichEditboxGoToComponent(const String& text);
		RichEditboxGoToComponent(const String& text, const String& font_name);
		RichEditboxGoToComponent(const String& text, Font* font);

		static const String EventNamespace;

		static const String EventLeftButtonClick;			//!< The read-only mode for the edit box has been changed.

		//! Set the text to be rendered by this component.
		void setText(const String& text);
		//! return the text that will be rendered by this component
		const String& getText() const;
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

		void setGoToFunction(GoToFunction* func) { d_GoToFunction = func;}

        void setDestPos(int mapid, int xpos, int ypos,int npcid, int chefu = 0, bool gotodirectly=true, int64_t npckey = 0){ d_destPos.mapid = mapid; d_destPos.x = xpos; d_destPos.y = ypos;d_destPos.npcid = npcid;
            d_destPos.npckey = npckey; d_destPos.chefu = chefu; d_destPos.gotodirectly = gotodirectly; }
		DestPos  getDestPos() const{ return d_destPos;}
		int  getDestMapID() { return d_destPos.mapid;}
		int  getDestXPos() { return d_destPos.x;}
		int  getDestYPos() { return d_destPos.y;}
		int  getDestNpcid() { return d_destPos.npcid;}
		int  getDestCheFu() { return d_destPos.chefu;}
        bool  getGotoDirectly() { return d_destPos.gotodirectly;}
	
		//! draw the component.
		void draw(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect,
			const float vertical_space,
			const float space_extra,
			bool bBorder=false)  ;

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

		void SetUnderLine(bool bUnderLine) { d_UnderLine=bUnderLine; } 
		bool isPushed() { return d_Pushed; }
		bool isTextType() { return true; }
		
		bool 	onMouseButtonDown(MouseEventArgs& e);
		bool    onMouseButtonUp(MouseEventArgs& e);

            
        bool onParentClicked(); // add for task trace dialog
	
	private:
		String d_text;
		//! Font to use for text rendering, 0 for system default.
		Font* d_font;
		//! ColourRect object describing the colours to use when rendering.
		ColourRect d_colours;
		static ColourRect d_HoverColours;
		static ColourRect d_PushedColours;
		DestPos	d_destPos;	

		bool d_UnderLine;  //是否有下划线,默认有

		bool d_Pushed;

		GoToFunction* d_GoToFunction;//寻路链接函数
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
