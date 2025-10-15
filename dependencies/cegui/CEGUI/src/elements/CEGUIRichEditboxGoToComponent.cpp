/***********************************************************************
filename:   CEGUIRichEditboxGoToComponent.cpp
*************************************************************************/

#include "elements/CEGUIRichEditboxGoToComponent.h"
#include "elements/CEGUICompnentTip.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	ColourRect RichEditboxGoToComponent::d_HoverColours(0xffff0000,0xffff0000,0xffff0000,0xffff0000);
	ColourRect RichEditboxGoToComponent::d_PushedColours(0xFFA9A9A9,0xFFA9A9A9,0xFFA9A9A9,0xFFA9A9A9);

	RichEditboxGoToComponent::~RichEditboxGoToComponent()
	{

	}
	//----------------------------------------------------------------------------//
	RichEditboxGoToComponent::RichEditboxGoToComponent() :
	d_font(0),
	d_text(""),
	d_GoToFunction(0),
	d_UnderLine(false),
	d_Pushed(false)
	{
		d_Type=RichEditboxComponentType_GoToText;
		setColours(0xFF00FF00);

	}

	//----------------------------------------------------------------------------//
	RichEditboxGoToComponent::RichEditboxGoToComponent(const String& text) :
	d_text(text),
	d_font(0),
	d_GoToFunction(0),
	d_UnderLine(false),
	d_Pushed(false)
	{
		setColours(0xFF00FF00);
	}

	//----------------------------------------------------------------------------//
	RichEditboxGoToComponent::RichEditboxGoToComponent(
		const String& text, const String& font_name) :
	d_text(text),
	d_font(font_name.empty() ? 0 : &FontManager::getSingleton().get(font_name)),
	d_GoToFunction(0),
	d_UnderLine(false),
	d_Pushed(false)
	{
		setColours(0xFF00FF00);
	}

	//----------------------------------------------------------------------------//
	RichEditboxGoToComponent::RichEditboxGoToComponent(const String& text,
		Font* font) :
	d_text(text),
	d_font(font),
	d_GoToFunction(0),
	d_UnderLine(true),
	d_Pushed(false)
	{
		setColours(0xFF00FF00);
	}

	//----------------------------------------------------------------------------//
	void RichEditboxGoToComponent::setText(const String& text)
	{
		d_text = text;
	}

	//----------------------------------------------------------------------------//
	const String& RichEditboxGoToComponent::getText() const
	{
		return d_text;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxGoToComponent::setFont(Font* font)
	{
		d_font = font;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxGoToComponent::setFont(const String& font_name)
	{
		d_font =
			font_name.empty() ? 0 : &FontManager::getSingleton().get(font_name);
	}

	//----------------------------------------------------------------------------//
	Font* RichEditboxGoToComponent::getFont() const
	{
		return d_font;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxGoToComponent::setColours(const ColourRect& cr)
	{
		d_colours = cr;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxGoToComponent::setColours(const colour& c)
	{
		d_colours.setColours(c);
	}

	//----------------------------------------------------------------------------//
	const ColourRect& RichEditboxGoToComponent::getColours() const
	{
		return d_colours;
	}

	//----------------------------------------------------------------------------//
	void RichEditboxGoToComponent::draw(GeometryBuffer* buffer,
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

		if (d_Pushed)
		{
			//		final_cols=d_PushedColours;
			final_pos.d_x += 2.0f;
			final_pos.d_y += 2.0f;
		}
		// draw the text string.
		
		fnt->drawText(buffer, d_text, final_pos, clip_rect, final_cols,
			space_extra, 1.0f, y_scale,d_UnderLine,bBorder);
	}

	//----------------------------------------------------------------------------//
	Size RichEditboxGoToComponent::getPixelSize() 
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
	bool RichEditboxGoToComponent::canSplit() 
	{
		return false;
	}

	//----------------------------------------------------------------------------//
	RichEditboxComponent* RichEditboxGoToComponent::split(
		float split_point, bool first_component)
	{
		//Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

		//// This is checked, but should never fail, since if we had no font our
		//// extent would be 0 and we would never cause a split to be needed here.
		//if (!fnt)
		//	throw InvalidRequestException("RenderedStringTextComponent::split: "
		//	"unable to split with no font set.");

		//// create 'left' side of split and clone our basic configuration
		//RichEditboxGoToComponent* lhs = new RichEditboxGoToComponent;

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
	size_t RichEditboxGoToComponent::getSpaceCount() 
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

	Point RichEditboxGoToComponent::GetOffsetOfIndex(size_t index)
	{
		return RichEditboxComponent::GetOffsetOfIndex(index);
	}

	size_t RichEditboxGoToComponent::GetIndexByWidth(float width)
	{

		return RichEditboxComponent::GetIndexByWidth(width);

	}

	bool RichEditboxGoToComponent::HandleDelte(size_t offset)
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

	bool RichEditboxGoToComponent::onMouseButtonDown(MouseEventArgs& e)
	{
		RichEditboxComponent::onMouseButtonDown(e);
		if (e.button == LeftButton)
		{	
			d_Pushed = true;
			GoToFunction* gotoFunction = d_GoToFunction ? d_GoToFunction : System::getSingleton().GetDefaultGoToFunction();
			
			if(gotoFunction)
			{
				(*gotoFunction)(d_destPos.mapid, d_destPos.x, d_destPos.y,d_destPos.npckey, d_destPos.chefu, d_destPos.gotodirectly,d_destPos.npcid);
			}
			
			return true;
		}
		return false;
	}
    

	bool RichEditboxGoToComponent::onMouseButtonUp(MouseEventArgs& e)
	{
		RichEditboxComponent::onMouseButtonUp(e);
		if (e.button==LeftButton&&d_Pushed)
		{
			d_Pushed=false;
			return true;
		}

		return true;
	}
    
    bool RichEditboxGoToComponent::onParentClicked()
	{
        GoToFunction* gotoFunction = d_GoToFunction ? d_GoToFunction : System::getSingleton().GetDefaultGoToFunction();
        
        if(gotoFunction)
        {
            (*gotoFunction)(d_destPos.mapid, d_destPos.x, d_destPos.y,d_destPos.npcid,d_destPos.chefu, d_destPos.gotodirectly,d_destPos.npckey);
        }
        
        return true;
	}

	String RichEditboxGoToComponent::GenerateParseText()
	{
		String parseText;
		parseText+="<";
		parseText+=RichEditbox_xmlHandler::GoToElement;
		if (d_text.size()>0)
		{
			parseText+=" "+RichEditbox_xmlHandler::TextAttribute;
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

		String mapid=PropertyHelper::intToString(d_destPos.mapid);
		parseText+=" ";
		parseText+=RichEditbox_xmlHandler::MapAttribute;
		parseText+="=";
		parseText+='"';
		parseText+=mapid;
		parseText+='"';

		String xPos=PropertyHelper::intToString(d_destPos.x);
		parseText+=" ";
		parseText+=RichEditbox_xmlHandler::XPosAttribute;
		parseText+="=";
		parseText+='"';
		parseText+=xPos;
		parseText+='"';

		String yPos=PropertyHelper::intToString(d_destPos.y);
		parseText+=" ";
		parseText+=RichEditbox_xmlHandler::YPosAttribute;
		parseText+="=";
		parseText+='"';
		parseText+=yPos;
		parseText+='"';

		String npcID=PropertyHelper::intToString(d_destPos.npcid);
		parseText+=" ";
		parseText+=RichEditbox_xmlHandler::NpcidAttribute;
		parseText+="=";
		parseText+='"';
		parseText+=npcID;
		parseText+='"';

		parseText+=">";
		parseText+="</";
		parseText+=RichEditbox_xmlHandler::GoToElement;
		parseText+=	">";

		return parseText;
	}
	//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
