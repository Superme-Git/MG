#include "elements/CEGUIRichEditboxComponent.h"

namespace CEGUI
{
	const String RichEditboxComponent::EventNamespace("RichEditboxComponent");
	const String RichEditboxComponent::EventMouseButtonDown( "MouseButtonDown");

	const String RichEditboxComponent::EventMouseEnter("MouseEnter");
	const String RichEditboxComponent::EventMouseLeave("MouseLeave");
	const String RichEditboxComponent::EventGuideEnd("GuideEnd");

	std::map<String,String> RichEditboxComponent::d_mapSpecialChar; 
	
	RichEditboxComponent::RichEditboxComponent():
    d_padding(0.0f,0.0f,0.0f,0.0f),
    d_Type(RichEditboxComponentType_None),
    d_bIsMouseOn(false),
	d_UserID(-1),
	d_LineSpace(0.0f),
	d_HasGuide(false)
	{

	}

	RichEditboxComponent::RichEditboxComponent(const RichEditboxComponent& other)
	{
		d_padding=other.d_padding;
		d_Type=other.d_Type;
		d_LineSpace=other.d_LineSpace;
		d_HasGuide=other.d_HasGuide;
		
	}
	RichEditboxComponent& RichEditboxComponent::operator =(const CEGUI::RichEditboxComponent &rhs)
	{
		d_Type=rhs.d_Type;
		d_padding=rhs.d_padding;
		d_LineSpace=rhs.d_LineSpace;
		
		return *this;
	}

	bool  RichEditboxComponent::onMouseButtonDown(MouseEventArgs& e)
	{
		fireEvent(EventMouseButtonDown,e,EventNamespace);
		if (e.button==LeftButton&&d_HasGuide)
		{
			 RichEditBoxCoponentEventArgs e(this);
			fireEvent(EventGuideEnd,e,EventNamespace);
		}
		return false;
	}

	void RichEditboxComponent::DrawSelectFlag(GeometryBuffer* buffer, const Vector2& position,
		const ColourRect* mod_colours,
		const Rect* clip_rect,
		const Image* pSeleImage,
		size_t selectStartIdx,size_t selectEndIdx)
	{
		if (pSeleImage!=NULL)
		{
			Size cpnSize=getPixelSize();
			Size SelectAreaSize(0.0f,0.0f);
			SelectAreaSize.d_width=cpnSize.d_width;
			SelectAreaSize.d_height=cpnSize.d_height;
			pSeleImage->draw(buffer,position,SelectAreaSize,clip_rect,*mod_colours);
		}
	}

	 void RichEditboxComponent::DrawMouseOnFlag(GeometryBuffer* buffer, const Vector2& position,
		const ColourRect* mod_colours,
		const Rect* clip_rect)
	{
		const Image* pSelectImg=System::getSingleton().GetTextBrushImage();
		if (pSelectImg)
		{
			Size cpnSize=getPixelSize();
			Size SelectAreaSize(0.0f,0.0f);
			SelectAreaSize.d_width=cpnSize.d_width;
			SelectAreaSize.d_height=cpnSize.d_height;
			pSelectImg->draw(buffer,position,SelectAreaSize,clip_rect,*mod_colours);
		}

	}

	Point RichEditboxComponent::GetOffsetOfIndex(size_t index)
	{
		Point pt(0.0f,0.0f);
		if (!canSplit()&&index>0)
		{
			pt.d_x= getPixelSize().d_width;
			pt.d_y= getPixelSize().d_height;
		}
		return pt;
	}

	size_t RichEditboxComponent::GetIndexByWidth( float width)
	{
		if (width>getPixelSize().d_width/2.0f)
		{
			return 1;
		}
		return 0;
	}

	 bool RichEditboxComponent::GetNewCaratPos(float width,size_t curIdx,size_t& newCaratIdx)
	 {
		 newCaratIdx=curIdx;
		 return false;
	 }

	 void  RichEditboxComponent::onMouseEnter()
	 { 
		 d_bIsMouseOn=true; 
		 RichEditBoxCoponentEventArgs e(this);
		 fireEvent(EventMouseEnter,e,EventNamespace);
	 }
	 void    RichEditboxComponent::onMouseLeave() 
	 { 
		 d_bIsMouseOn=false; 
		 RichEditBoxCoponentEventArgs e(this);
		 fireEvent(EventMouseLeave,e,EventNamespace);
	 }

	 void RichEditboxComponent::InitSpecialChar() //初始化特殊xml转义字符
	 {
		 d_mapSpecialChar["&"]="&amp;";
		 d_mapSpecialChar["<"]="&gt;";
		 d_mapSpecialChar["\""]="&quot;";
	 }
	void RichEditboxComponent::ReplaceSpecialChar(String& text) //替换特殊xml转义字符
	{
		String newText(text);
		std::map<String,String>::const_iterator it=d_mapSpecialChar.begin();
		for (;it!=d_mapSpecialChar.end();++it)
		{
			String replaceString=it->first;
			String destString=it->second;
			size_t startPos=0;
			size_t pos=newText.find(replaceString,startPos);
			while (pos!=String::npos)
			{
				newText.erase(pos,1);
				newText.insert(pos,destString);

				startPos=pos+2;
				pos=newText.find(replaceString,startPos);
			}
		}
		
		text=newText;
	}


	
}



