/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxTextComponent_h_
#define _CEGUIRichEditboxTextComponent_h_

#include "CEGUIRichEditboxComponent.h"
//#include "../CEGUIWindow.h"
//#include "../CEGUIFont.h"
//#include "CEGUIRichEditboxProperties.h"
//
//#include <vector>


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxTextComponent:public RichEditboxComponent 
	{
	public:
		//! Destructor.
		virtual ~RichEditboxTextComponent();


		RichEditboxTextComponent();
		RichEditboxTextComponent(const String& text);
		RichEditboxTextComponent(const String& text, const String& font_name);
		RichEditboxTextComponent(const String& text, Font* font);

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
		 virtual Size getPixelSize();

		//! return whether the component can be split
		 bool canSplit()  ;

		 RichEditboxComponent* split(float split_point,
			bool first_component) ;

		//! clone this component.
		 RichEditboxComponent* clone() ;

		//! return the total number of spacing characters in the string.
		 size_t getSpaceCount() ;
		 Point GetOffsetOfIndex(size_t index);
		 size_t GetMaxIndex() { return d_text.size(); }

		 size_t GetIndexByWidth(float with);
		 bool HandleBackSpace(size_t offset);
		 RichEditboxComponent* HandleChar(size_t offset,const String& InsetText,const ColourRect& color=ColourRect());
		 RichEditboxComponent* InsertComponent(size_t offset,RichEditboxComponent* pComponent);   //返回true则需生成新的component

		 String GenerateParseText();
		 virtual String GeneratePureText(size_t startPos,size_t endPos);

		 virtual int GetCharCount();

		 virtual size_t GetCharNumByIndex(size_t index) { return index; }

		 //获得该控件拆分后新的光标位置，curidx::当前位置 newcaratIdx新的光标位置 返回true则表明新光标是在拆分后的新控件里
		 virtual bool GetNewCaratPos(float width,size_t curIdx,size_t& newCaratIdx);

		 virtual bool isOnlySpace(); //只否只有空字符

		 virtual bool isTextType() { return true; }

		 RichEditboxComponent* ParseControlText(bool bIsCaratOn,size_t curCpnIdx,size_t curCaratIndex,size_t curCaratOffet,
			                                    size_t& newCaratIndex,size_t& newCaratOffset,bool& bChange,String& newText,
												bool& bWaitEmotionNum,bool bAcceptEmotion); //解析字符串 解析#等快捷输入信息
        
		 //得到最后一个表情的#号位置及表情对应数字的最后一位对应的位置
		 //startIdx : 最后一个表情的#号位置
		 //endIdx   :   对应数字的最后一位对应的位置
		 //返回false则不存在表情转义
		 bool GetFirstEmotionInf(size_t& startIdx, size_t& endIdx,bool& bWaitNum,bool bAcceptEmotion); 
		 bool isNumber(utf32 num); //是否是数字字符
		 
		 void SetBorderEnable(bool bBorder) { d_Border=bBorder; }
		 void SetBorderColour(const colour& borderColour) { d_BorderColour=borderColour; }
	 
		

    protected:
		String d_text;
		//! Font to use for text rendering, 0 for system default.
		Font* d_font;
		//! ColourRect object describing the colours to use when rendering.
		ColourRect d_colours;
		bool  d_Border;
		colour d_BorderColour;
		
	private:

	


	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
