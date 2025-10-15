/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxLinkTextComponent_h_
#define _CEGUIRichEditboxLinkTextComponent_h_

#include "CEGUIRichEditboxTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxLinkTextComponent:public RichEditboxTextComponent 
	{
		friend class RichEditboxTipsLinkComponent;
	public:
		//! Destructor.
		virtual ~RichEditboxLinkTextComponent();


		
		RichEditboxLinkTextComponent();
		RichEditboxLinkTextComponent(const String& text);
		RichEditboxLinkTextComponent(const String& text, const String& font_name);
		RichEditboxLinkTextComponent(const String& text, Font* font);

		static const String EventNamespace;

		static const String EventLeftButtonClick;			//!< The read-only mode for the edit box has been changed.

		//! Set the text to be rendered by this component.
		

		//! draw the component.
		 void draw(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect,
			const float vertical_space,
			const float space_extra,
			bool bBorder=false);


		 virtual void DrawSelectFlag(GeometryBuffer* buffer, const Vector2& position,
			 const ColourRect* mod_colours,
			 const Rect* clip_rect,
			 const Image* pSeleImage=NULL,
			 size_t selectStartIdx=0,size_t selectEndIdx=0);

		//! return the pixel size of the rendered component.
		// Size getPixelSize();

		//! return whether the component can be split
		 bool canSplit();

		 RichEditboxComponent* split(float split_point, bool first_component);
		 
		void setHoverColours(const colour& c);
		
		 const ColourRect& getHoverColours()const;

		 Point GetOffsetOfIndex(size_t index);
		 size_t GetMaxIndex(){return 1;}
		 size_t GetIndexByWidth( float width){return 0;}
		 //bool HandleDelte(size_t offset){ return true; }  //����true����ɾ����ǰ��component
		 RichEditboxComponent* HandleChar(size_t offset,const String& InsetText,const ColourRect& ColorRect=ColourRect()){ return NULL; }   //����true���������µ�textcomponent
		 RichEditboxComponent* InsertComponent(size_t offset,RichEditboxComponent* pComponent);   //����true���������µ�component
		 bool HandleBackSpace(size_t offset){ return RichEditboxComponent::HandleBackSpace(offset); }
		 virtual String GenerateParseText();

		 virtual bool onMouseButtonDown(MouseEventArgs& e);
		 virtual bool onMouseButtonUp(MouseEventArgs& e);

		 void onMouseLeave();
		 
		 void SetClickEventID(const int64_t& id) { d_ClickEventID = id;}
		 int64_t GetClickEventID() { return d_ClickEventID;}

		 void SetDataID(int id);

		
		 int GetDataID();

		 virtual bool isPushed() { return d_Pushed; }

		 void EnableUnderLine(bool b) { d_UnderLine=b; } //�����Ƿ��»���

		 String GeneratePureText(size_t startPos,size_t endPos) { return getText(); }

		 virtual size_t GetCharNumByIndex(size_t index) { return 1; }
        
        virtual Size getPixelSize();
        
        virtual Size getDrawPixelSize();
        
		void setTextHorizontalCenter(bool center) { d_TextHorizontalCenter = center; }
		void setTextVerticalCenter(bool center){ d_TextVerticalCenter = center; }
 
	protected:
		
		ColourRect d_HoverColours;
		ColourRect d_PushedColours;
		bool d_Pushed;
		int64_t  d_ClickEventID;   //��������¼���ID,�ڸ�������richeditbox�в���
		int      dataID;
		bool     d_UnderLine; //�Ƿ��»���
		bool	d_TextHorizontalCenter;	//�Ƿ����־���
		bool	d_TextVerticalCenter;//�Ƿ����¾���

		void clone2(RichEditboxLinkTextComponent* rlh,float split_point);

	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
