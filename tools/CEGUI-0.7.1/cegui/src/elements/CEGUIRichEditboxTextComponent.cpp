/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxTextComponent.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"
#include "elements/CEGUIRichEditboxEmotionComponent.h"


// Start of CEGUI namespace section
namespace CEGUI
{

	RichEditboxTextComponent::~RichEditboxTextComponent()
	{

	}
//----------------------------------------------------------------------------//
RichEditboxTextComponent::RichEditboxTextComponent() :
    d_font(0),
    d_colours(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF),
	d_text(""),
	d_Border(false),
	d_BorderColour(0xFF000000)
	
{
	d_Type=RichEditboxComponentType_Text;

}

//----------------------------------------------------------------------------//
RichEditboxTextComponent::RichEditboxTextComponent(const String& text) :
    d_text(text),
    d_font(0),
	d_colours(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF),
	d_Border(false),
	d_BorderColour(0xFF000000)
{
}

//----------------------------------------------------------------------------//
RichEditboxTextComponent::RichEditboxTextComponent(
        const String& text, const String& font_name) :
    d_text(text),
    d_font(font_name.empty() ? 0 : &FontManager::getSingleton().get(font_name)),
	d_colours(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF),
	d_Border(false),
	d_BorderColour(0xFF000000)
{
}

//----------------------------------------------------------------------------//
RichEditboxTextComponent::RichEditboxTextComponent(const String& text,
                                                         Font* font) :
    d_text(text),
    d_font(font),
	d_colours(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF),
	d_Border(false),
	d_BorderColour(0xFF000000)
{
}

//----------------------------------------------------------------------------//
void RichEditboxTextComponent::setText(const String& text)
{
    d_text = text;
}

//----------------------------------------------------------------------------//
const String& RichEditboxTextComponent::getText() const
{
    return d_text;
}

//----------------------------------------------------------------------------//
void RichEditboxTextComponent::setFont(Font* font)
{
    d_font = font;
}

//----------------------------------------------------------------------------//
void RichEditboxTextComponent::setFont(const String& font_name)
{
    d_font =
        font_name.empty() ? 0 : &FontManager::getSingleton().get(font_name);
}

//----------------------------------------------------------------------------//
Font* RichEditboxTextComponent::getFont() const
{
    return d_font;
}

//----------------------------------------------------------------------------//
void RichEditboxTextComponent::setColours(const ColourRect& cr)
{
    d_colours = cr;
}

//----------------------------------------------------------------------------//
void RichEditboxTextComponent::setColours(const colour& c)
{
    d_colours.setColours(c);
}

//----------------------------------------------------------------------------//
const ColourRect& RichEditboxTextComponent::getColours() const
{
    return d_colours;
}

//----------------------------------------------------------------------------//
void RichEditboxTextComponent::draw(GeometryBuffer* buffer,
                                       const Vector2& position,
                                       const ColourRect* mod_colours,
                                       const Rect* clip_rect,
                                       const float vertical_space,
                                       const float space_extra,
									   bool bBorder) 
{
    Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

    if (!fnt)
        return;
	
    Vector2 final_pos(position);
	final_pos.d_y+=vertical_space;
    float y_scale = 1.0f;

    // apply modulative colours if needed.
    ColourRect final_cols(d_colours);
    if (mod_colours)
        final_cols *= *mod_colours;

  bool textBorder=bBorder||d_Border;   
	// draw the text string.
    fnt->drawText(buffer, d_text, final_pos, clip_rect, final_cols,
                  space_extra, 1.0f, y_scale,false,textBorder,d_BorderColour);
	

	
	
}

void RichEditboxTextComponent::DrawSelectFlag(GeometryBuffer* buffer, const Vector2& position,
					const ColourRect* mod_colours,
					const Rect* clip_rect,
					const Image* pSeleImage,
					size_t selectStartIdx,size_t selectEndIdx)
{
	if (pSeleImage!=NULL)
	{
		
		
		
		String selectText=d_text.substr(selectStartIdx,selectEndIdx-selectStartIdx);
		String beforeSelectText=d_text.substr(0,selectStartIdx);
		float selectWidth=d_font->getTextExtent(selectText);
		float beforeselectWidth=d_font->getTextExtent(beforeSelectText);

		Point SelectDrawPos(0.0f,0.0f);
		SelectDrawPos.d_x=position.d_x+beforeselectWidth;
		SelectDrawPos.d_y=position.d_y;
		Size SelectAreaSize(0.0f,0.0f);
		SelectAreaSize.d_width=selectWidth;
		SelectAreaSize.d_height=d_font->getFontHeight(1.0f);
		pSeleImage->draw(buffer,SelectDrawPos,SelectAreaSize,clip_rect,*mod_colours);
	}
}

//----------------------------------------------------------------------------//
Size RichEditboxTextComponent::getPixelSize() 
{
    Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

    Size psz(0.0f,0.0f);

    if (fnt)
    {
        psz.d_width += fnt->getTextExtent(d_text);
        psz.d_height += fnt->getFontHeight()+GetLineSpace();

    }

    return psz;
}

//----------------------------------------------------------------------------//
bool RichEditboxTextComponent::canSplit() 
{
    return true;
}

//----------------------------------------------------------------------------//
RichEditboxComponent* RichEditboxTextComponent::split(float split_point,bool first_component)
{
    Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

    // This is checked, but should never fail, since if we had no font our
    // extent would be 0 and we would never cause a split to be needed here.
    if (!fnt)
        throw InvalidRequestException("RenderedStringTextComponent::split: "
                                      "unable to split with no font set.");

    // create 'left' side of split and clone our basic configuration
    RichEditboxTextComponent* lhs = new RichEditboxTextComponent;
   
    lhs->d_font = d_font;
    lhs->d_colours = d_colours;

    // calculate the 'best' place to split the text
    
	size_t cutcount = fnt->getCharAtPixel(d_text, split_point);
    // perform the split.
    lhs->d_text = d_text.substr(cutcount, d_text.length()-cutcount);
	lhs->SetLineSpace(GetLineSpace());

	if (lhs->d_text.size()==0)
	{
		int k=0;

	}

    
    
    d_text = d_text.substr(0,cutcount);

    return lhs;
}



//----------------------------------------------------------------------------//
RichEditboxComponent* RichEditboxTextComponent::clone() 
{
    RichEditboxTextComponent* c = new RichEditboxTextComponent(*this);
    return c;
}

//----------------------------------------------------------------------------//
size_t RichEditboxTextComponent::getSpaceCount() 
{
    // TODO: The value calculated here is a good candidate for caching.

    size_t space_count = 0;

    // Count the number of spaces in this component.
    // NB: here I'm not countng tabs since those are really intended to be
    // something other than just a bigger space.
    const size_t char_count = d_text.length();
    for (size_t c = 0; c < char_count; ++c)
        if (d_text[c] == ' ') // TODO: There are other space characters!
            ++space_count;

    return space_count;
}

Point RichEditboxTextComponent::GetOffsetOfIndex(size_t index)
{
	Point pt(0.0f,0.0f);
	if (index<=d_text.size()&&index>0)
	{
		Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();
		if (fnt)
		{
			String newString=d_text.substr(0,index);
			pt.d_x=fnt->getTextExtent(newString);
			pt.d_y=fnt->getFontHeight();
		}
	}
	return pt;
}

size_t RichEditboxTextComponent::GetIndexByWidth(float width)
{
	
	if (!canSplit())
	{
		return RichEditboxComponent::GetIndexByWidth(width);
	}

	if (d_text.size()>0)
	{
		 Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();
		 if (NULL!=fnt)
		 {
			     size_t charIndex= fnt->getCharAtPixel(d_text,width);
			
				 float selectCharWidth=fnt->getTextExtent(d_text.substr(charIndex,1)); //选择的那个字的宽度
				 float FirstToSelectWidth=fnt->getTextExtent(d_text.substr(0,charIndex)); //从开始到选择的那个字的宽度
				 if (width>(FirstToSelectWidth-selectCharWidth)/2.0f)
				 {
					 return charIndex+1;
				 }else
				 {
					 return charIndex;
				 }
			
		 }
		 
	}
	
	return 0;

}

bool RichEditboxTextComponent::HandleBackSpace(size_t offset)
{
	if (d_text.size()==1)
	{
		return true;
	}
	if (offset==0)
	{
		offset=1;

	}
	if (offset<=d_text.size())
	{
		d_text.erase(offset-1,1);

	}
	return false;
	
}

RichEditboxComponent* RichEditboxTextComponent::HandleChar(size_t offset,const String& InsertText, const ColourRect& ColorRect)
{
	if (InsertText.size()<=0)
	{
		return NULL;
	}
	if (offset>d_text.size())
	{
		return NULL;
	}

	
	
	if (ColorRect.getColourAtPoint(0.0f,0.0f)!=d_colours.getColourAtPoint(0.0f,0.0f))
	{
		RichEditboxTextComponent* lhs = new RichEditboxTextComponent;
		lhs->d_text=d_text.substr(offset,d_text.size()-offset);
		lhs->d_colours=d_colours;
		lhs->d_font=d_font;

		d_text=d_text.substr(0,offset);

		return lhs;
	}else
	{
		
		
		d_text.insert(offset,InsertText);
	}
	return NULL;
}

RichEditboxComponent* RichEditboxTextComponent::InsertComponent(size_t offset,RichEditboxComponent* pComponent)   //返回true则需生成新的component
{
	if (pComponent==NULL||offset>d_text.size())
	{
		return NULL;
	}
	if (pComponent->GetType()==RichEditboxComponentType_Text)
	{
		RichEditboxTextComponent* pTextComponent=(RichEditboxTextComponent*)pComponent;
		if (pTextComponent!=NULL)
		{
			return HandleChar(offset,pTextComponent->getText(),pTextComponent->getColours());

		}else
		{
			return NULL;
		}
		
	}else
	{
		if (offset>=d_text.size())
		{
			return NULL;
		}
		RichEditboxTextComponent* lhs = new RichEditboxTextComponent;
		lhs->d_text=d_text.substr(offset,d_text.size()-offset);
		lhs->d_colours=d_colours;
		lhs->d_font=d_font;

		d_text=d_text.substr(0,offset);
		return lhs;
	}
}


String RichEditboxTextComponent::GenerateParseText()
{
	String parseText;
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::TextElement;
	if (d_text.size()>0)
	{
		/*String newText(d_text);
		RichEditboxComponent::ReplaceSpecialChar(newText);*/
		String newText=XMLSerializer::convertEntityInText(d_text);
		parseText+=" "+RichEditbox_xmlHandler::TextAttribute;
		parseText+="=";
		parseText+='"';
		parseText+=newText;
		parseText+='"';


	}

	String colorText=PropertyHelper::colourToString(d_colours.getColourAtPoint(0.0f,0.0f));
	parseText+=" ";
	parseText+=RichEditbox_xmlHandler::ColorAttribute;
	parseText+="=";
	parseText+='"';
	parseText+=colorText;
	parseText+='"';
	
	parseText+=">";
	parseText+="</";
	parseText+=RichEditbox_xmlHandler::TextElement;
	parseText+=	">";

	return parseText;
}

int RichEditboxTextComponent::GetCharCount()
{

	//return d_text.length();
	int count=(int)d_text.length();

	return count;
}

String RichEditboxTextComponent::GeneratePureText(size_t startPos,size_t endPos)
{
	if (startPos>=0&&endPos<=d_text.size())
	{
		String strText=d_text.substr(startPos,endPos-startPos);
		return strText;
	}
	return "";
}

bool RichEditboxTextComponent::GetNewCaratPos(float width,size_t curIdx,size_t& newCaratIdx)
{
	Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

	// This is checked, but should never fail, since if we had no font our
	// extent would be 0 and we would never cause a split to be needed here.
	if (!fnt)
		throw InvalidRequestException("RenderedStringTextComponent::split: "
		"unable to split with no font set.");

	// create 'left' side of split and clone our basic configuration
	size_t cutcount=fnt->getCharAtPixel(d_text,width);
	if (cutcount==0)
	{
		newCaratIdx=curIdx;
		return true;

	}else
	{
		if (curIdx>cutcount)
		{
			newCaratIdx=curIdx-cutcount;
			return true;
		}
	}
	
	return false;

}

bool RichEditboxTextComponent::isOnlySpace() //只否只有空字符
{
	if (canSplit()&&d_text.size()>0)
	{
		if (d_text.find_first_not_of(" ")==String::npos)
		{
			return true;
		}
	}
	return false;

}

RichEditboxComponent* RichEditboxTextComponent::ParseControlText(bool bIsCaratOn,size_t curCpnIdx,size_t curCaratIndex,size_t curCaratOffet,
									   size_t& newCaratIndex,size_t& newCaratOffset,bool& bChange,String& newText,
									   bool& bWaitEmotionNum,bool bAcceptEmotion) //解析字符串 解析#等快捷输入信息
{
	bChange=false;
	//yangbin --不解析表情的快捷输入方式，为了使用系统输入控件
	return NULL;

	if (!isOnlySpace())
	{
		
		size_t startIdx=0;
		size_t endIdx=0;
		int maxEmoitonNum=System::getSingleton().GetEmotionNum();
		if (GetFirstEmotionInf(startIdx,endIdx,bWaitEmotionNum,bAcceptEmotion))
		{
			
			bChange=true;
			String numStr=d_text.substr(startIdx+1,endIdx-startIdx);
			int emodtionID = PropertyHelper::stringToInt(numStr);

			if (!System::getSingleton().HasEmotionKey(emodtionID))
			{
				return NULL;
			}
			
			newText=d_text.substr(endIdx+1,String::npos);
			d_text.erase(startIdx,String::npos);
			
			if (emodtionID>=0&&emodtionID<=maxEmoitonNum)
			{
				//todo 检测有没有这个表情
				RichEditboxEmotionComponent* pNewEmotion=new RichEditboxEmotionComponent;
				
				pNewEmotion->SetAnimateID(emodtionID);

				if (bIsCaratOn)
				{
					//如果当前的文字已经被清空，当前控件是要被删掉的
					if (d_text.empty())
					{
						newCaratIndex=curCpnIdx;
						newCaratOffset=1;
					}else
					{
						newCaratIndex=curCpnIdx+1;
						newCaratOffset=1;
					}
					if (!newText.empty())
					{
						newCaratIndex++;
						newCaratOffset=newText.length();
					}

				}else
				{
					//如果小于当前控件
					if (curCpnIdx<curCaratIndex)
					{

						if (d_text.empty())
						{
							newCaratIndex=curCaratIndex;
						}else
						{
							newCaratIndex=curCaratIndex+1;
						}
						
						if (!newText.empty())
						{
							newCaratIndex++;
						}
						newCaratOffset=curCaratOffet;


					}else
					{
						newCaratIndex=curCaratIndex;
						newCaratOffset=curCaratOffet;
					}
				}
				return pNewEmotion;
			}
		}
		
	}
	return NULL;
}

bool RichEditboxTextComponent::isNumber(utf32 num) //是否是数字字符
{
	if (num>=48&&num<=57)
	{
		return true;
	}
	return false;
}


bool RichEditboxTextComponent::GetFirstEmotionInf(size_t& startIdx, size_t& endIdx,bool& bWaitNum,bool bAcceptEmotion) //得到第一个表情的#号位置及表情对应数字的最后一位对应的位置
{
	bWaitNum=false;
	size_t curIdx=d_text.find("#");
	// #存在而且不是最后一个字符
	while(curIdx!=String::npos&&(curIdx!=d_text.size()-1))
	{
		
		utf32 nextChar=d_text[curIdx+1];
		//如果这个字符是数字
		if (isNumber(nextChar))
		{
			startIdx=curIdx; //记录#位置
			
			//寻到末尾或是最后一个数字

			String strNumber("");
			strNumber+=nextChar;
			size_t startSearchIdx=curIdx+2;
			size_t endSearchIdx=d_text.size();
			
			endIdx=curIdx+1;

			for (size_t i=startSearchIdx;i<endSearchIdx;++i)
			{
				utf32 curTextChar=d_text[i];
				if (!isNumber(curTextChar))
				{
					break;
				}
				endIdx=i;
				strNumber+=curTextChar;
			}

			int maxEmotionIdx=System::getSingleton().GetEmotionNum();
			int curEmotionIdx=PropertyHelper::stringToInt(strNumber);

			if (curEmotionIdx>maxEmotionIdx)
			{
				size_t len=endIdx-startIdx;
				for (size_t k=len;k>=1;--k)
				{
					String strTest=d_text.substr(startIdx+1,k);
					int testEmotionIdx=PropertyHelper::stringToInt(strTest);
					if (testEmotionIdx<=maxEmotionIdx)
					{
						endIdx=startIdx+k;
						break;
					}
				}
			}else if (endIdx==d_text.size()-1) //如果是最后一个数字字符处在最后且可能再输入一位会有新表情也不解析
			{
				if (curEmotionIdx<=maxEmotionIdx/10)
				{
					bWaitNum=true;
					if (bAcceptEmotion)
					{
						bWaitNum=false;
						return true;
					}
					return false;
				}
				
			}
			
			return true;
		}
		curIdx=d_text.find("#",curIdx+1);
	}

	return false;

}





//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
