/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxLinkTextComponent.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

#define LinkTextDefaultHoverColor 0xFF00FFFF
#define LinkTextDefaultPushColor  0xFFA9A9A9

// Start of CEGUI namespace section
namespace CEGUI
{

	const String RichEditboxLinkTextComponent::EventNamespace("RichEditboxLinkTextComponent");
	const String RichEditboxLinkTextComponent::EventLeftButtonClick("LeftButtonClick");



	RichEditboxLinkTextComponent::~RichEditboxLinkTextComponent()
	{

	}
//----------------------------------------------------------------------------//
RichEditboxLinkTextComponent::RichEditboxLinkTextComponent() :
	d_HoverColours(LinkTextDefaultHoverColor, LinkTextDefaultHoverColor, LinkTextDefaultHoverColor, LinkTextDefaultHoverColor),
   d_Pushed(false),
   d_PushedColours(LinkTextDefaultPushColor, LinkTextDefaultPushColor, LinkTextDefaultPushColor, LinkTextDefaultPushColor),
   d_ClickEventID(0),
   dataID(0),
   d_UnderLine(false),
   d_TextHorizontalCenter(false),
   d_TextVerticalCenter(false)
{
	d_Type=RichEditboxComponentType_LinkText;
	setColours(0xFFFFFF00);

}

//----------------------------------------------------------------------------//
RichEditboxLinkTextComponent::RichEditboxLinkTextComponent(const String& text) :
d_HoverColours(LinkTextDefaultHoverColor, LinkTextDefaultHoverColor, LinkTextDefaultHoverColor, LinkTextDefaultHoverColor),
d_Pushed(false),
d_PushedColours(LinkTextDefaultPushColor, LinkTextDefaultPushColor, LinkTextDefaultPushColor, LinkTextDefaultPushColor),
d_ClickEventID(0),
dataID(0),
d_UnderLine(false),
d_TextHorizontalCenter(false),
d_TextVerticalCenter(false)
{
	d_Type=RichEditboxComponentType_LinkText;
	setColours(0xFFFFFF00);
	setText(text);
}

//----------------------------------------------------------------------------//
RichEditboxLinkTextComponent::RichEditboxLinkTextComponent(
        const String& text, const String& font_name) :
d_HoverColours(LinkTextDefaultHoverColor, LinkTextDefaultHoverColor, LinkTextDefaultHoverColor, LinkTextDefaultHoverColor),
d_Pushed(false),
d_PushedColours(LinkTextDefaultPushColor, LinkTextDefaultPushColor, LinkTextDefaultPushColor, LinkTextDefaultPushColor),
d_ClickEventID(0),
dataID(0),
d_UnderLine(false),
d_TextHorizontalCenter(false),
d_TextVerticalCenter(false)
{
	d_Type=RichEditboxComponentType_LinkText;
	setColours(0xFFFFFF00);
	setText(text);
	setFont(font_name);
}

//----------------------------------------------------------------------------//
RichEditboxLinkTextComponent::RichEditboxLinkTextComponent(const String& text,
                                                         Font* font) :

d_HoverColours(LinkTextDefaultHoverColor, LinkTextDefaultHoverColor, LinkTextDefaultHoverColor, LinkTextDefaultHoverColor),
d_Pushed(false),
d_PushedColours(LinkTextDefaultPushColor, LinkTextDefaultPushColor, LinkTextDefaultPushColor, LinkTextDefaultPushColor),
d_ClickEventID(0),
dataID(0),
d_UnderLine(false),
d_TextHorizontalCenter(false),
d_TextVerticalCenter(false)
{
	d_Type=RichEditboxComponentType_LinkText;
	setColours(0xFFFFFF00);
	setText(text);
	setFont(font);
}
//----------------------------------------------------------------------------//


//----------------------------------------------------------------------------//


//----------------------------------------------------------------------------//
void RichEditboxLinkTextComponent::setHoverColours(const colour& c)
{
    d_HoverColours.setColours(c);
}

//----------------------------------------------------------------------------//
const ColourRect& RichEditboxLinkTextComponent::getHoverColours() const
{
    return d_HoverColours;
}

//----------------------------------------------------------------------------//
void RichEditboxLinkTextComponent::draw(GeometryBuffer* buffer,
                                       const Vector2& position,
                                       const ColourRect* mod_colours,
                                       const Rect* clip_rect,
                                       const float vertical_space,
                                       const float space_extra,
									   bool bBorder) 
{
    Font* fnt = getFont();
	fnt=fnt ? fnt : System::getSingleton().getDefaultFont();

    if (!fnt)
        return;

    Vector2 final_pos(position);
    float y_scale = 1.0f;
	
    // apply modulative colours if needed.
    ColourRect final_cols(getColours());
//	if (IsMouseOn())
//	{
//		final_cols=getHoverColours();
//	}

	if (d_TextHorizontalCenter)
		final_pos.d_x += (clip_rect->getWidth() - fnt->getTextExtent(getText()))*0.5f;

	if (d_TextVerticalCenter)
	{
		float fontHeight = fnt->getFontHeight();
		float baseline = fnt->getBaseline();
		final_pos.d_y += (fontHeight - baseline)*0.5f;
	}
	else
		final_pos.d_y += vertical_space;

	if (d_Pushed)
	{
//		final_cols=d_PushedColours;
		final_pos.d_x+=2.0f;
		final_pos.d_y+=2.0f;
	}
	
	
    if (mod_colours)
        final_cols *= *mod_colours;

    // draw the text string.
    fnt->drawText(buffer, getText(), final_pos, clip_rect, final_cols,
                  space_extra, 1.0f, y_scale,d_UnderLine,bBorder);
	
}

void RichEditboxLinkTextComponent::DrawSelectFlag(GeometryBuffer* buffer, const Vector2& position,
									 const ColourRect* mod_colours,
									 const Rect* clip_rect,
									 const Image* pSeleImage,
									 size_t selectStartIdx,size_t selectEndIdx)
{
	RichEditboxComponent::DrawSelectFlag(buffer,position,mod_colours,clip_rect,pSeleImage,selectStartIdx,selectStartIdx);
}

Point RichEditboxLinkTextComponent::GetOffsetOfIndex(size_t index)
{
	return RichEditboxComponent::GetOffsetOfIndex(index);
}




//----------------------------------------------------------------------------//
bool RichEditboxLinkTextComponent::canSplit() 
{
    return true;
}

RichEditboxComponent* RichEditboxLinkTextComponent::split(float split_point, bool first_component)
{
	RichEditboxLinkTextComponent* lhs = new RichEditboxLinkTextComponent;
	clone2(lhs,split_point);
	return lhs;
}

//----------------------------------------------------------------------------//
void RichEditboxLinkTextComponent::clone2(RichEditboxLinkTextComponent* lhs, float split_point)
{
	Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

	if (!fnt)
		throw InvalidRequestException("RenderedStringTextComponent::split: "
		"unable to split with no font set.");

	lhs->d_Type = d_Type;
	lhs->d_font = d_font;
	lhs->d_colours = d_colours;

	// calculate the 'best' place to split the text

	size_t cutcount = fnt->getCharAtPixel(d_text, split_point);
	// perform the split.
	lhs->d_text = d_text.substr(cutcount, d_text.length() - cutcount);
	lhs->SetLineSpace(GetLineSpace());
	d_text = d_text.substr(0, cutcount);
	lhs->d_HoverColours = d_HoverColours;
	lhs->d_PushedColours = d_PushedColours;
	lhs->d_Pushed = d_Pushed;
	lhs->d_ClickEventID = d_ClickEventID;
	lhs->dataID = dataID;
	lhs->d_UnderLine = d_UnderLine;
	lhs->d_TextHorizontalCenter = d_TextHorizontalCenter;
	lhs->d_TextVerticalCenter = d_TextHorizontalCenter;
	lhs->SetUserID(GetUserID());

}

String RichEditboxLinkTextComponent::GenerateParseText()
{
	String parseText;
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::TextLinkElement;
	if (getText().size()>0)
	{
		parseText+=" "+RichEditbox_xmlHandler::TextAttribute;
		parseText+="=";
		parseText+='"';
		parseText+=getText();
		parseText+='"';
	}
	parseText+=">";
	parseText+="</";
	parseText+=RichEditbox_xmlHandler::TextLinkElement;
	parseText+=	">";

	return parseText;
}	

RichEditboxComponent* RichEditboxLinkTextComponent::InsertComponent(size_t offset,RichEditboxComponent* pComponent)   //返回true则需生成新的component
{
	return RichEditboxComponent::InsertComponent(offset,pComponent);
}

bool RichEditboxLinkTextComponent::onMouseButtonDown(MouseEventArgs& e)
{
	RichEditboxComponent::onMouseButtonDown(e);
	if (e.button==LeftButton&&!d_Pushed)
	{
		e.handled++;
		d_Pushed = true;
		//fireEvent(EventLeftButtonClick,e,EventNamespace);
		return true;
	}
	return false;
}
bool RichEditboxLinkTextComponent::onMouseButtonUp(MouseEventArgs& e)
{
	RichEditboxComponent::onMouseButtonUp(e);
	if (e.button==LeftButton&&d_Pushed)
	{
		e.handled++;
		fireEvent(EventLeftButtonClick,e,EventNamespace);
		d_Pushed = false;
		return true;
	}
	
	return true;
}

 void RichEditboxLinkTextComponent::onMouseLeave()
 {
     if (d_Pushed)      //reset pushed status
     {
         d_Pushed = false;
     }
	 RichEditboxComponent::onMouseLeave();
	 d_Pushed=false;
 }

 void RichEditboxLinkTextComponent::SetDataID(int  id)
 {
	dataID = id;
 }

 int RichEditboxLinkTextComponent::GetDataID()
 {
	 return dataID;
 }

Size RichEditboxLinkTextComponent::getPixelSize()
{
    Size pixSize=RichEditboxTextComponent::getPixelSize();
//    pixSize.d_height+=4.0f;
    
    return pixSize;
}
    
Size RichEditboxLinkTextComponent::getDrawPixelSize()
{
    
    if (GetType()==RichEditboxComponentType_LinkText)
    {
        Font* fnt = getFont() ? getFont() : System::getSingleton().getDefaultFont();
        
        Size psz(0.0f,0.0f);
        
        if (fnt)
        {
            psz.d_width += fnt->getTextExtent(getText());
            psz.d_height += fnt->getFontHeight();
            
        }
        
        return psz;
        
    }
    
    return getPixelSize();
}

//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
