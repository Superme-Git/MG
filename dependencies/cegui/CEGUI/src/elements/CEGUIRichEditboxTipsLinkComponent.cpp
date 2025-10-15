/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxTipsLinkComponent.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	
	


RichEditboxTipsLinkComponent::~RichEditboxTipsLinkComponent()
{

}

RichEditboxTipsLinkComponent::RichEditboxTipsLinkComponent(const String& text,const String& font_name,
														   int64_t _roleID,int type,const String& key,int baseid,
														   int64_t _shopID, int _counterID, int _bind,
														   int64_t _loseeffecttime):
RichEditboxLinkTextComponent(text,font_name)
,d_ObjectKey(key)
,d_ObjectType(type)
,d_RoleID(_roleID)
,d_ObjBaseID(baseid)
,d_shopID(_shopID)
,d_counterID(_counterID)
,d_Bind(_bind)
,d_loseeffecttime(_loseeffecttime)
{
	d_Type=RichEditboxComponentType_Tips;

}

String RichEditboxTipsLinkComponent::GenerateParseText()
{
	String parseText;
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::TipsLinkElement;
	if (getText().size()>0)
	{
		parseText+=" "+RichEditbox_xmlHandler::TextAttribute;
		parseText+="=";
		parseText+='"';
		parseText+=getText();
		parseText+='"';
	}

	parseText+=" "+RichEditbox_xmlHandler::ObjRoleID;
	parseText+="=\"";
	parseText+=PropertyHelper::int64_tToString(d_RoleID);

	parseText+="\" "+RichEditbox_xmlHandler::ObjType;
	parseText+="=\"";
	parseText+=PropertyHelper::intToString(d_ObjectType);
	
	parseText+="\" "+RichEditbox_xmlHandler::ObjKey;
	parseText+="=\"";
	parseText+=d_ObjectKey;

	parseText+="\" "+RichEditbox_xmlHandler::ObjBaseID;
	parseText+="=\"";
	parseText+=PropertyHelper::intToString(d_ObjBaseID);

	parseText+="\" "+RichEditbox_xmlHandler::ObjShopID;
	parseText+="=\"";
	parseText+=PropertyHelper::int64_tToString(d_shopID);

	parseText+="\" "+RichEditbox_xmlHandler::ObjCounterID;
	parseText+="=\"";
	parseText+=PropertyHelper::intToString(d_counterID);

	parseText+="\" "+RichEditbox_xmlHandler::ObjBind;
	parseText+="=\"";
	parseText+=PropertyHelper::intToString(d_Bind);

	parseText+="\" "+RichEditbox_xmlHandler::ObjLoseEffTime;
	parseText+="=\"";
	parseText+=PropertyHelper::int64_tToString(d_loseeffecttime);

	parseText+="\" "+RichEditbox_xmlHandler::TextColor;
	parseText+="=\"";
	parseText+=PropertyHelper::colourToString(getColours().getColourAtPoint(0.0f,0.0f));

	parseText+="\">";
	parseText+="</";
	parseText+=RichEditbox_xmlHandler::TipsLinkElement;
	parseText+=	">";

	return parseText;
}

bool RichEditboxTipsLinkComponent::onMouseButtonDown(MouseEventArgs& e)
{
	//if (e.button==LeftButton||e.button==RightButton)
	//{
	//	TipsLinkClick* Func = System::getSingleton().GetTipsLinkClickFunc();

	//	if(Func)
	//	{
	//		(*Func)(getText(),d_RoleID,d_ObjectType,d_ObjectKey,d_ObjBaseID,d_shopID,d_counterID,getColours().getColourAtPoint(0.0f,0.0f),d_Bind,d_loseeffecttime);
	//	}
	//}
	return RichEditboxLinkTextComponent::onMouseButtonDown(e);
}

bool RichEditboxTipsLinkComponent::onMouseButtonUp(MouseEventArgs& e)
{
	if (e.button == LeftButton || e.button == RightButton)
	{
		TipsLinkClick* Func = System::getSingleton().GetTipsLinkClickFunc();

		if (Func)
		{
			(*Func)(getText(), d_RoleID, d_ObjectType, d_ObjectKey, d_ObjBaseID, d_shopID, d_counterID, getColours().getColourAtPoint(0.0f, 0.0f), d_Bind, d_loseeffecttime);
		}
	}
	return RichEditboxLinkTextComponent::onMouseButtonUp(e);
}


Point RichEditboxTipsLinkComponent::GetOffsetOfIndex(size_t index)
{
	Point pt(0.0f, 0.0f);
	if (index>0)
	{
		pt.d_x = getPixelSize().d_width;
		pt.d_y = getPixelSize().d_height;
	}
	return pt;
}

bool RichEditboxTipsLinkComponent::canSplit()
{
	return true;
}

RichEditboxTipsLinkComponent* RichEditboxTipsLinkComponent::split(float split_point, bool first_component)
{
	Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

	// This is checked, but should never fail, since if we had no font our
	// extent would be 0 and we would never cause a split to be needed here.
	if (!fnt)
		throw InvalidRequestException("RenderedStringTextComponent::split: "
		"unable to split with no font set.");

	// create 'left' side of split and clone our basic configuration
	RichEditboxTipsLinkComponent* lhs = new RichEditboxTipsLinkComponent(getText()
		, getFont()->getName()
		, d_RoleID
		, d_ObjectType
		, d_ObjectKey
		, d_ObjBaseID
		, d_shopID
		, d_counterID
		, d_Bind
		, d_loseeffecttime);
	
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
	
	return lhs;
}

//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
