
#include "elements/CEGUIRichEditboxImageComponent.h"
#include "elements/CEGUICompnentTip.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIImageset.h"
#include "CEGUIExceptions.h"
#include "CEGUIRichEditbox_xmlHandler.h"
#include "CEGUIPropertyHelper.h"

// Start of CEGUI namespace section
namespace CEGUI
{

	const Vector2 RichEditboxImageComponent::sTextOffset = Vector2(40.0f,40.0f);
//----------------------------------------------------------------------------//
RichEditboxImageComponent::RichEditboxImageComponent() :
    d_image(0),
    d_colours(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF),
    d_size(0, 0),
	d_xPos(0.0f),
	d_yPos(0.0f),
	d_specialID(0),
	d_ImageScale(1.0f)
{
	d_Type=RichEditboxComponentType_Image;

}

//----------------------------------------------------------------------------//
RichEditboxImageComponent::RichEditboxImageComponent(
        const String& imageset, const String& image,const int& specialid) :
    d_colours(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF),
    d_size(0, 0),
	d_xPos(0.0f),
	d_yPos(0.0f),
	d_specialID(specialid),
	d_ImageScale(1.0f)
{
    setImage(imageset, image);
	d_Type=RichEditboxComponentType_Image;
}

//----------------------------------------------------------------------------//
RichEditboxImageComponent::RichEditboxImageComponent(const Image* image,const int& specialid) :
    d_image(image),
    d_colours(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF),
    d_size(0, 0),
	d_xPos(0.0f),
	d_yPos(0.0f),
	d_specialID(specialid),
	d_ImageScale(1.0f)
{
	d_Type=RichEditboxComponentType_Image;
}

//----------------------------------------------------------------------------//
void RichEditboxImageComponent::setImage(const String& imageset,
                                            const String& image)
{
    if (!imageset.empty() && !image.empty())
    {
		if (!ImagesetManager::getSingleton().isDefined(imageset))
		{
			d_image = 0;
			CEGUI_LOGERR(L"imageset not found: " + imageset);
			return;
		}
		Imageset& is = ImagesetManager::getSingleton().get(imageset);
		if (!is.isImageDefined(image))
		{
			d_image = 0;
			CEGUI_LOGERR(imageset + L" not found: " + image);
			return;
		}

		try
		{
			d_image = &is.getImage(image);
		}
		catch (const CEGUI::UnknownObjectException& uoe)
		{
			//图片资源没有找到
			d_image = 0;
			std::wostringstream strsteam;
			strsteam << uoe.getMessage().c_str();
			//MessageBoxW(NULL,strsteam.str().c_str(),L"",0);		

		}
       // d_image = &is.getImage(image);
    }
    else
    {
        d_image = 0;
    }
}

//----------------------------------------------------------------------------//
void RichEditboxImageComponent::setImage(const Image* image)
{
    d_image = image;
}

//----------------------------------------------------------------------------//
const Image* RichEditboxImageComponent::getImage() const
{
    return d_image;
}

//----------------------------------------------------------------------------//
void RichEditboxImageComponent::setColours(const ColourRect& cr)
{
    d_colours = cr;
}

//----------------------------------------------------------------------------//
void RichEditboxImageComponent::setColours(const colour& c)
{
    d_colours.setColours(c);
}

//----------------------------------------------------------------------------//
const ColourRect& RichEditboxImageComponent::getColours() const
{
    return d_colours;
}

void RichEditboxImageComponent::onMouseEnter()
{
	//d_specialID 代表任务物品的数量,值为零代表不是任务道具的图片，不需要显示tips
	if(d_specialID > 100)
	{
		ShowCompnentTips* pFunc=System::getSingleton().GetCompnentTips();
		if (pFunc)
		{
			(*pFunc)(this);
		}
		
	}
	else if(d_specialID > 0)
	{
		ShowItemTips* showTips = System::getSingleton().GetDefaultShowItemTips();
		if(showTips)
		{
			(*showTips)((int)GetUserID(),d_xPos,d_yPos);
		}
	}
	//d_bIsMouseOn=true;
	RichEditboxComponent::onMouseEnter();
}

void RichEditboxImageComponent::onMouseLeave()
{
	if(d_specialID > 0 && d_specialID < 100)
	{
		ShowItemTips* showTips = System::getSingleton().GetDefaultShowItemTips();
		if(showTips)
		{
			(*showTips)(0,0.0f,0.0f);
		}
	}
	else if(d_specialID > 100)
	{
		CompnentTip* pTip=System::getSingleton().getDefaultCompnenttip();
		if (pTip)
		{
			pTip->SetTargetCompnent(0);
		}
	}
	
	RichEditboxComponent::onMouseLeave();
}
//----------------------------------------------------------------------------//
void RichEditboxImageComponent::draw(GeometryBuffer* buffer,
                                        const Vector2& position,
                                        const ColourRect* mod_colours,
                                        const Rect* clip_rect,
                                        const float vertical_space,
                                        const float space_extra,
										bool bBorder) 
{
    if (!d_image)
        return;

    CEGUI::Rect dest(position.d_x, position.d_y, 0, 0);
    float y_scale = 1.0f;

    // handle formatting options
    
    Size sz(d_image->getSize());
    if (d_size.d_width != 0.0)
        sz.d_width = d_size.d_width;
    if (d_size.d_height != 0.0)
        sz.d_height = d_size.d_height;
	
	sz.d_height*=d_ImageScale;
	sz.d_width*=d_ImageScale;
    
	d_xPos = position.d_x;
	d_yPos = position.d_y;

    sz.d_height *= y_scale;
    dest.setSize(sz);

    // apply padding to position
    dest.offset(d_padding.getPosition());

	dest.offset(CEGUI::Point(0.0f,vertical_space));

    // apply modulative colours if needed.
    ColourRect final_cols(d_colours);
    if (mod_colours)
        final_cols *= *mod_colours;

    // draw the image.
	if(d_specialID > 0 && d_specialID <100)//代表任务物品的数量,画格子
	{
		const Image* pBackimage=System::getSingleton().GetCellImage();

		CEGUI::Rect backdest;
		backdest.d_left = dest.d_left - 6.5f;
		backdest.d_top = dest.d_top - 6.5f;
		backdest.d_right = dest.d_right + 6.5f;
		backdest.d_bottom = dest.d_bottom + 6.5f;
		pBackimage->draw(buffer, backdest, clip_rect, final_cols);
	}
    d_image->draw(buffer, dest, clip_rect, final_cols);
	if(d_specialID > 1 && d_specialID <100)
	{
		Vector2 final_pos(position);
		final_pos.d_x += sTextOffset.d_x;
		final_pos.d_y += sTextOffset.d_y;

		String text = CEGUI::PropertyHelper::intToString(d_specialID);
		 Font* fnt = System::getSingleton().getDefaultFont();
		 fnt->drawText(buffer, text, final_pos, clip_rect, final_cols,
			 0, 1.0f, y_scale);
	}
}

//----------------------------------------------------------------------------//
Size RichEditboxImageComponent::getPixelSize() 
{
    Size sz(0, 0);

    if (d_image)
    {
        sz = d_image->getSize();
        if (d_size.d_width != 0.0)
            sz.d_width = d_size.d_width;
        if (d_size.d_height != 0.0)
            sz.d_height = d_size.d_height;

		sz.d_width*=d_ImageScale;
		sz.d_height*=d_ImageScale;

        sz.d_width += (d_padding.d_left + d_padding.d_right);
        sz.d_height += (d_padding.d_top + d_padding.d_bottom)+GetLineSpace();
    }

    return sz;
}

//----------------------------------------------------------------------------//
bool RichEditboxImageComponent::canSplit() 
{
    return false;
}

//----------------------------------------------------------------------------//
RichEditboxComponent* RichEditboxImageComponent::split(
    float /*split_point*/, bool /*first_component*/)
{
    throw InvalidRequestException("RenderedStringImageComponent::split: this "
                                  "component does not support being split.");
}

//----------------------------------------------------------------------------//
RichEditboxComponent* RichEditboxImageComponent::clone() 
{
    return new RichEditboxImageComponent(*this);
}

//----------------------------------------------------------------------------//
size_t RichEditboxImageComponent::getSpaceCount() 
{
    // images do not have spaces.
    return 0;
}

//----------------------------------------------------------------------------//
void RichEditboxImageComponent::setSize(const Size& sz)
{
    d_size = sz;
}

//----------------------------------------------------------------------------//
const Size& RichEditboxImageComponent::getSize() const
{
    return d_size;
}

String RichEditboxImageComponent::GenerateParseText()
{
	String parseText("");
	if (NULL==d_image)
	{
		return parseText;
	}
	
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::ImageElement+" ";
	
	
	parseText+=RichEditbox_xmlHandler::ImageSetNameAttribute+"=\"";
	parseText+=d_image->getImagesetName();

	parseText+="\" ";

	parseText+=RichEditbox_xmlHandler::ImageAttribute+"=\"";
	parseText+=d_image->getName();
	
	parseText+="\"></";
	parseText+=RichEditbox_xmlHandler::ImageElement;
	parseText+=">";

	return parseText;
}

//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
