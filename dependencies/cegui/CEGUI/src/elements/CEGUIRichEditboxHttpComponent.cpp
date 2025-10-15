/***********************************************************************
filename:   CEGUIRichEditboxHttpComponent.cpp
*************************************************************************/

#include "elements/CEGUIRichEditboxHttpComponent.h"
#include "elements/CEGUICompnentTip.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	ColourRect RichEditboxHttpComponent::d_HoverColours(0xffff0000,0xffff0000,0xffff0000,0xffff0000);
	ColourRect RichEditboxHttpComponent::d_PushedColours(0xFFA9A9A9,0xFFA9A9A9,0xFFA9A9A9,0xFFA9A9A9);

	RichEditboxHttpComponent::~RichEditboxHttpComponent()
	{

	}
	//----------------------------------------------------------------------------//
	RichEditboxHttpComponent::RichEditboxHttpComponent() :
	d_font(0),
	d_text(""),
	d_httpAddress(""),
	d_Pushed(false)
	{
		d_Type=RichEditboxComponentType_HttpAddress;
		setColours(0xFF00FF00);

	}

	//----------------------------------------------------------------------------//
	RichEditboxHttpComponent::RichEditboxHttpComponent(const String& text,const String& httpaddress) :
	d_text(text),
	d_httpAddress(httpaddress),
	d_font(0),
	d_Pushed(false)
	{
		d_Type=RichEditboxComponentType_HttpAddress;
		setColours(0xFF00FF00);
	}

	//----------------------------------------------------------------------------//
	RichEditboxHttpComponent::RichEditboxHttpComponent(
		const String& text,const String& httpaddress, const String& font_name) :
	d_text(text),
	d_httpAddress(httpaddress),
	d_font(font_name.empty() ? 0 : &FontManager::getSingleton().get(font_name)),
	d_Pushed(false)
	{
		d_Type=RichEditboxComponentType_HttpAddress;
		setColours(0xFF00FF00);
	}

	//----------------------------------------------------------------------------//
	RichEditboxHttpComponent::RichEditboxHttpComponent(const String& text,const String& httpaddress,
		Font* font) :
	d_text(text),
	d_httpAddress(httpaddress),
	d_font(font),
	d_Pushed(false)
	{
		d_Type=RichEditboxComponentType_HttpAddress;
		setColours(0xFF00FF00);
	}

	//----------------------------------------------------------------------------//
	void RichEditboxHttpComponent::setText(const String& text)
	{
		d_text = text;
	}

	//----------------------------------------------------------------------------//
	const String& RichEditboxHttpComponent::getText() const
	{
		return d_text;
	}

	void RichEditboxHttpComponent::setHttpAddress(const String& address)
	{
		d_httpAddress = address;
	}

	//----------------------------------------------------------------------------//
	const String& RichEditboxHttpComponent::getHttpAddress() const
	{
		return d_httpAddress;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxHttpComponent::setFont(Font* font)
	{
		d_font = font;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxHttpComponent::setFont(const String& font_name)
	{
		d_font =
			font_name.empty() ? 0 : &FontManager::getSingleton().get(font_name);
	}

	//----------------------------------------------------------------------------//
	Font* RichEditboxHttpComponent::getFont() const
	{
		return d_font;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxHttpComponent::setColours(const ColourRect& cr)
	{
		d_colours = cr;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxHttpComponent::setColours(const colour& c)
	{
		d_colours.setColours(c);
	}

	//----------------------------------------------------------------------------//
	const ColourRect& RichEditboxHttpComponent::getColours() const
	{
		return d_colours;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxHttpComponent::draw(GeometryBuffer* buffer,
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
		if (d_bIsMouseOn)
		{
			final_cols=d_HoverColours;
		}
		if (d_Pushed)
		{
			final_cols=d_PushedColours;
			final_pos.d_x+=1.0f;
			final_pos.d_y+=1.0f;
		}
		if (mod_colours)
			final_cols *= *mod_colours;

		// draw the text string.
		
		fnt->drawText(buffer, d_text, final_pos, clip_rect, final_cols,
			space_extra, 1.0f, y_scale,true,bBorder);
	}

	void RichEditboxHttpComponent::DrawSelectFlag(GeometryBuffer* buffer, const Vector2& position,
		const ColourRect* mod_colours,
		const Rect* clip_rect,
		const Image* pSeleImage,
		size_t selectStartIdx,size_t selectEndIdx)
	{
		RichEditboxComponent::DrawSelectFlag(buffer,position,mod_colours,clip_rect,pSeleImage,selectStartIdx,selectStartIdx);
	}

	//----------------------------------------------------------------------------//
	Size RichEditboxHttpComponent::getPixelSize() 
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
	bool RichEditboxHttpComponent::canSplit() 
	{
		return false;
	}

	//----------------------------------------------------------------------------//
	RichEditboxComponent* RichEditboxHttpComponent::split(
		float split_point, bool first_component)
	{
		//Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

		//// This is checked, but should never fail, since if we had no font our
		//// extent would be 0 and we would never cause a split to be needed here.
		//if (!fnt)
		//	throw InvalidRequestException("RenderedStringTextComponent::split: "
		//	"unable to split with no font set.");

		//// create 'left' side of split and clone our basic configuration
		//RichEditboxHttpComponent* lhs = new RichEditboxGoToComponent;

		//lhs->d_font = d_font;
		//lhs->d_colours = d_colours;
		//lhs->d_destPos = d_destPos;

		//// calculate the 'best' place to split the text

		//size_t cutcount=d_font->getCharAtPixel(d_text,split_point);
		//// perform the split.
		//lhs->d_text = d_text.substr(cutcount, d_text.length()-cutcount);



		//d_text = d_text.substr(0,cutcount);

		//return lhs;
		return NULL;
	}

	//----------------------------------------------------------------------------//
	size_t RichEditboxHttpComponent::getSpaceCount() 
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

	Point RichEditboxHttpComponent::GetOffsetOfIndex(size_t index)
	{
		return RichEditboxComponent::GetOffsetOfIndex(index);
	}

	size_t RichEditboxHttpComponent::GetIndexByWidth(float width)
	{

		return RichEditboxComponent::GetIndexByWidth(width);

	}

	bool RichEditboxHttpComponent::HandleDelte(size_t offset)
	{
		if (d_text.size()==1)
		{
			return true;
		}
		if (offset<d_text.size())
		{
			d_text.erase(offset,1);

		}
		return false;

	}

	bool RichEditboxHttpComponent::onMouseButtonDown(MouseEventArgs& e)
	{
		RichEditboxComponent::onMouseButtonDown(e);
		if (e.button == LeftButton)
		{	
			d_Pushed = true;
			LinkHttpFunction* LinkFunction = System::getSingleton().GetDefaultLinkHttpFunction();
			
			if(LinkFunction)
			{
				(*LinkFunction)(d_httpAddress);
			}
			
			return true;
		}
		return false;
	}

	bool RichEditboxHttpComponent::onMouseButtonUp(MouseEventArgs& e)
	{
		RichEditboxComponent::onMouseButtonUp(e);
		if (e.button==LeftButton&&d_Pushed)
		{
			d_Pushed=false;
			return true;
		}

		return true;
	}

	void RichEditboxHttpComponent::onMouseEnter()
	{
// 		ShowCompnentTips* pFunc=System::getSingleton().GetCompnentTips();
// 		if (pFunc)
// 		{
// 			(*pFunc)(this);
// 		}
		d_bIsMouseOn=true; 
		//RichEditboxComponent::onMouseEnter();
	}

	void RichEditboxHttpComponent::onMouseLeave()
	{
// 		CompnentTip* pTip=System::getSingleton().getDefaultCompnenttip();
// 		if (pTip)
// 		{
// 			pTip->SetTargetCompnent(0);
// 		}
		 d_bIsMouseOn=false; 
		//RichEditboxComponent::onMouseLeave();
		d_Pushed=false;
	}

	String RichEditboxHttpComponent::GenerateParseText()
	{
		String parseText;
		parseText+="<";
		parseText+=RichEditbox_xmlHandler::LinkHttpElement;
		if (d_text.size()>0)
		{
			parseText+=" "+RichEditbox_xmlHandler::TextAttribute;
			parseText+="=";
			parseText+='"';
			parseText+=d_text;
			parseText+='"';

		}

		if (d_httpAddress.size()>0)
		{
			parseText+=" "+RichEditbox_xmlHandler::HttpAddress;
			parseText+="=";
			parseText+='"';
			parseText+=d_text;
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
		parseText+=RichEditbox_xmlHandler::LinkHttpElement;
		parseText+=	">";

		return parseText;
	}
	//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
