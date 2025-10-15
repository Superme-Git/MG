
#include "elements/CEGUIRichEditboxImageComponent.h"
#include "elements/CEGUIRichEditboxButtonImageComponent.h"
#include "elements/CEGUICompnentTip.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIImageset.h"
#include "CEGUIExceptions.h"
#include "CEGUIRichEditbox_xmlHandler.h"
#include "CEGUIPropertyHelper.h"

// Start of CEGUI namespace section
namespace CEGUI
{

   const String RichEditboxButtonImageComponent::EventLeftButtonClick("LeftButtonClick");

//----------------------------------------------------------------------------//
RichEditboxButtonImageComponent::RichEditboxButtonImageComponent() :
	RichEditboxImageComponent(),
	d_HoverImage(0),
	d_PushedImage(0),
	dataID(0),
	d_Pushed(false)
{
	d_Type=RichEditboxComponentType_ButtonImage;

}

//----------------------------------------------------------------------------//
RichEditboxButtonImageComponent::RichEditboxButtonImageComponent(
        const String& nimageset, const String& nimage,const String& himageset, const String& himage,const String& pimageset, const String& pimage,const int& id) :
   RichEditboxImageComponent(nimageset,nimage),
   dataID(id),
   d_Pushed(false)
{
    setHoverImage(himageset, himage);
	setPushedImage(pimageset,pimage);
	d_Type=RichEditboxComponentType_ButtonImage;

}

//----------------------------------------------------------------------------//
RichEditboxButtonImageComponent::RichEditboxButtonImageComponent(const Image* image,const Image* himage,const Image* pimage,const int& id) :
   RichEditboxImageComponent(image),
   dataID(id),
   d_Pushed(false)
{
	setHoverImage(himage);
	setPushedImage(pimage);
	d_Type=RichEditboxComponentType_ButtonImage;

}

//----------------------------------------------------------------------------//
void RichEditboxButtonImageComponent::setHoverImage(const String& imageset,
                                            const String& image)
{
    if (!imageset.empty() && !image.empty())
    {
		d_HoverImage = ImagesetManager::getSingleton().getImage(imageset, image);
    }
    else
    {
        d_HoverImage = 0;
    }
}

//----------------------------------------------------------------------------//
void RichEditboxButtonImageComponent::setHoverImage(const Image* image)
{
    d_HoverImage = image;
}

//----------------------------------------------------------------------------//
const Image* RichEditboxButtonImageComponent::getHoverImage() const
{
    return d_HoverImage;
}

//----------------------------------------------------------------------------//
void RichEditboxButtonImageComponent::setPushedImage(const String& imageset,
													const String& image)
{
	if (!imageset.empty() && !image.empty())
	{
		d_PushedImage = ImagesetManager::getSingleton().getImage(imageset, image);
	}
	else
	{
		d_PushedImage = 0;
	}
}

//----------------------------------------------------------------------------//
void RichEditboxButtonImageComponent::setPushedImage(const Image* image)
{
	d_PushedImage = image;
}

//----------------------------------------------------------------------------//
const Image* RichEditboxButtonImageComponent::getPushedImage() const
{
	return d_PushedImage;
}


bool RichEditboxButtonImageComponent::onMouseButtonDown(MouseEventArgs& e)
{
	RichEditboxComponent::onMouseButtonDown(e);
	if (e.button==LeftButton&&!d_Pushed && dataID <= 11)
	{
		OnChangelImageClick* pFunc=System::getSingleton().GetChangelClickFunc();
		if (pFunc)
		{
			(*pFunc)(dataID);
		}

		d_Pushed=true;
		//fireEvent(EventLeftButtonClick,e,EventNamespace);
	}
	return true;
}

bool RichEditboxButtonImageComponent::onMouseButtonUp(MouseEventArgs& e)
{
	RichEditboxComponent::onMouseButtonUp(e);
	if (e.button==LeftButton&&d_Pushed)
	{
		//fireEvent(EventLeftButtonClick,e,EventNamespace);
		d_Pushed=false;
		return true;
	}

	return true;
}

void RichEditboxButtonImageComponent::onMouseLeave()
{
	CompnentTip* pTip=System::getSingleton().getDefaultCompnenttip();
	if (pTip)
	{
		pTip->SetTargetCompnent(0);
	}
	RichEditboxComponent::onMouseLeave();
	d_Pushed=false;
}


void RichEditboxButtonImageComponent::onMouseEnter()
{
	ShowCompnentTips* pFunc=System::getSingleton().GetCompnentTips();
	if (pFunc)
	{
		(*pFunc)(this);
	}

	RichEditboxComponent::onMouseEnter();
}

//----------------------------------------------------------------------------//
void RichEditboxButtonImageComponent::draw(GeometryBuffer* buffer,
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
    
	d_xPos = position.d_x;

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
	if (IsMouseOn() && d_HoverImage)
	{
		d_HoverImage->draw(buffer, dest, clip_rect, final_cols);
	}
	else if(d_Pushed && d_PushedImage)
	{
		d_PushedImage->draw(buffer, dest, clip_rect, final_cols);
	}
	else
	{
		d_image->draw(buffer, dest, clip_rect, final_cols);
	}
 
}


String RichEditboxButtonImageComponent::GenerateParseText()
{
	String parseText("");
	if (NULL==d_image || NULL==d_HoverImage || NULL==d_PushedImage)
	{
		return parseText;
	}
	
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::ButtonImageElement+" ";
	
	
	parseText+=RichEditbox_xmlHandler::ImageSetNameAttribute+"=\"";
	parseText+=d_image->getImagesetName();

	parseText+="\" ";

	parseText+=RichEditbox_xmlHandler::ImageAttribute+"=\"";
	parseText+=d_image->getName()+" ";

	parseText+=RichEditbox_xmlHandler::HImageSetNameAttribute+"=\"";
	parseText+=d_HoverImage->getImagesetName();

	parseText+="\" ";

	parseText+=RichEditbox_xmlHandler::HImageAttribute+"=\"";
	parseText+=d_HoverImage->getName()+" ";

	parseText+=RichEditbox_xmlHandler::PImageSetNameAttribute+"=\"";
	parseText+=d_PushedImage->getImagesetName();

	parseText+="\" ";

	parseText+=RichEditbox_xmlHandler::PImageAttribute+"=\"";
	parseText+=d_PushedImage->getName();

// 	parseText+=RichEditbox_xmlHandler::ObjType+"=\"";
// 	parseText+=intToString(dataID);

	parseText+="\"></";
	parseText+=RichEditbox_xmlHandler::ButtonImageElement;
	parseText+=">";

	return parseText;
}

//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
