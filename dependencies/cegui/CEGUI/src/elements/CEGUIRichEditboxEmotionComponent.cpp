
#include "elements/CEGUIRichEditboxEmotionComponent.h"
#include "CEGUIImageset.h"
#include "CEGUIAnimationInstance.h"
#include "CEGUIAnimationManager.h"
#include "CEGUIExceptions.h"
#include "CEGUIRichEditbox_xmlHandler.h"
#include "CEGUIPropertyHelper.h"

// Start of CEGUI namespace section
namespace CEGUI
{

	const String RichEditboxEmotionComponent::EventNamespace("RichEditboxEmotionComponent");
	const String RichEditboxEmotionComponent::EventFrameChange("EmotionFrameChange");
	const String RichEditboxEmotionComponent::EventEmotionStart("EmotionStart");
	const String RichEditboxEmotionComponent::EventEmotionClick("EmotionClick");

	

//----------------------------------------------------------------------------//
RichEditboxEmotionComponent::RichEditboxEmotionComponent() :
    d_image(0),
	d_animationInstance(0),
	d_size(.0f,.0f),
	d_AnimateID(-1),
	d_CurFrame(0),
	d_ElapseTime(0.0f),
	d_Scale(1.0f,1.0f)
{
	d_Type=RichEditboxComponentType_Emotion;
	/*EmotionStartFunction* pFuc=System::getSingleton().GetEmotionStartFunction();
	if (pFuc)
	{
		RichEditBoxCoponentEventArgs e(this);
		(*pFuc)(e);
	}*/
	subscribeEvent(EventFrameChange,Event::Subscriber(System::getSingleton().GetEmotionFrameChangeFunction()));
	//OnFrameChange();
}

//----------------------------------------------------------------------------//
RichEditboxEmotionComponent::RichEditboxEmotionComponent(
       AnimationInstance* aniInst) :
  d_image(0),
  d_size(0.0f,0.0f),
  d_AnimateID(-1),
  d_CurFrame(0),
  d_ElapseTime(0.0f)

{
    SetAniInstance(aniInst);
}

//RichEditboxEmotionComponent::~RichEditboxEmotionComponent()
//{
//	if (d_animationInstance)
//	{
//		AnimationManager::getSingleton().destroyAnimationInstance(d_animationInstance);
//	}
//	
//}

 RichEditboxEmotionComponent:: RichEditboxEmotionComponent(const RichEditboxEmotionComponent& other)
 {
	 d_CurPosition=other.d_CurPosition;
	 d_Duration=other.d_Duration;
	 d_ElapseTime=other.d_ElapseTime;
	 d_image=other.d_image;

 }

  //! Assignment.
  RichEditboxEmotionComponent& RichEditboxEmotionComponent::operator=(const RichEditboxEmotionComponent& rhs)
  {
	  d_CurPosition=rhs.d_CurPosition;
	  d_Duration=rhs.d_Duration;
	  d_ElapseTime=rhs.d_ElapseTime;
	  d_image=rhs.d_image;

	  return *this;
  }

  RichEditboxEmotionComponent* RichEditboxEmotionComponent::operator=(const RichEditboxEmotionComponent* rhs)
  {
	  d_CurPosition=rhs->d_CurPosition;
	  d_Duration=rhs->d_Duration;
	  d_ElapseTime=rhs->d_ElapseTime;
	  d_image=rhs->d_image;

	  return this;
  }


//----------------------------------------------------------------------------//
void RichEditboxEmotionComponent::SetAniInstance(AnimationInstance* aniInst)
{
	/*d_animationInstance=aniInst;
	if (d_animationInstance)
	{
		d_animationInstance->setTarget(this);
		d_animationInstance->start();
	}*/
}

RichEditboxEmotionComponent::RichEditboxEmotionComponent(int emotionID):
d_image(0),
d_animationInstance(0),
d_size(.0f,.0f),
d_AnimateID(-1)
{
	SetAnimateID(emotionID);
}

void RichEditboxEmotionComponent::SetAnimateID(int aniID)
{
	if (aniID!=d_AnimateID)
	{
		d_AnimateID=aniID;
		OnFrameChange();
	}
	
}


//----------------------------------------------------------------------------//
void RichEditboxEmotionComponent::draw(GeometryBuffer* buffer,
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
	sz.d_width*=d_Scale.d_x;
	sz.d_height*=d_Scale.d_y;
   
    
    sz.d_height *= y_scale;
    dest.setSize(sz);

    // apply padding to position
    dest.offset(d_padding.getPosition());

	dest.offset(CEGUI::Point(0.0f,vertical_space));

    // apply modulative colours if needed.
    ColourRect final_cols(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
    if (mod_colours)
        final_cols *= *mod_colours;

    // draw the image.
    d_image->draw(buffer, dest, clip_rect, final_cols);
}

//----------------------------------------------------------------------------//
Size RichEditboxEmotionComponent::getPixelSize() 
{
    Size sz(0, 0);

    if (d_image)
    {
        sz = d_image->getSize();
        if (d_size.d_width != 0.0)
            sz.d_width = d_size.d_width;
        if (d_size.d_height != 0.0)
            sz.d_height = d_size.d_height;
        sz.d_width += (d_padding.d_left + d_padding.d_right);
        sz.d_height += (d_padding.d_top + d_padding.d_bottom)+GetLineSpace();
    }

    sz.d_width*=d_Scale.d_x;
	sz.d_height*=d_Scale.d_y;
	return sz;
}

//----------------------------------------------------------------------------//
bool RichEditboxEmotionComponent::canSplit() 
{
    return false;
}

//----------------------------------------------------------------------------//
RichEditboxComponent* RichEditboxEmotionComponent::split(
    float /*split_point*/, bool /*first_component*/)
{
    throw InvalidRequestException("RenderedStringImageComponent::split: this "
                                  "component does not support being split.");
}

//----------------------------------------------------------------------------//
RichEditboxComponent* RichEditboxEmotionComponent::clone() 
{
    return new RichEditboxEmotionComponent(*this);
}

//----------------------------------------------------------------------------//
size_t RichEditboxEmotionComponent::getSpaceCount() 
{
    // images do not have spaces.
    return 0;
}

//----------------------------------------------------------------------------//
void RichEditboxEmotionComponent::setSize(const Size& sz)
{
    d_size = sz;
}

//----------------------------------------------------------------------------//
const Size& RichEditboxEmotionComponent::getSize() const
{
    return d_size;
}

String RichEditboxEmotionComponent::GenerateParseText()
{
	String parseText("");
	if (d_AnimateID==-1)
	{
		return parseText;
	}
	
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::EmotionElement+" ";
	
	
	parseText+=RichEditbox_xmlHandler::EmotionID+"=\"";
	parseText+=PropertyHelper::intToString(d_AnimateID);
	parseText+="\"></";

	parseText+=RichEditbox_xmlHandler::EmotionElement;
	parseText+=">";

	return parseText;
}

void RichEditboxEmotionComponent::SetFrame(int frame)
{
	if (d_AnimateID!=-1)
	{
		d_CurFrame=frame;
		OnFrameChange();
	}
}

//String	AniFrameProperty::get(const PropertyReceiver* receiver) const
//{
//	 return PropertyHelper::intToString(static_cast<const RichEditboxEmotionComponent*>(receiver)->GetFrame());
//
//}
//void	AniFrameProperty::set(PropertyReceiver* receiver, const String& value)
//{
//	static_cast<RichEditboxEmotionComponent*>(receiver)->SetFrame(PropertyHelper::stringToInt(value));
//
//}

bool RichEditboxEmotionComponent::updata(float fElapse)
{
	const Image* oldImage=d_image;
	d_ElapseTime+=fElapse;
	SetFrame(0);
	if (d_image!=oldImage)
	{
		return true;
	}
	return false;

}

bool RichEditboxEmotionComponent::onMouseButtonDown(MouseEventArgs& e)
{
	//if (e.button=LeftButton)
	{
		RichEditBoxCoponentEventArgs e(this);
	    fireEvent(EventEmotionClick,e,EventNamespace);
	}
	
	return RichEditboxComponent::onMouseButtonDown(e);

}
void RichEditboxEmotionComponent::onMouseEnter()
{
	RichEditboxComponent::onMouseEnter();

}
void RichEditboxEmotionComponent::onMouseLeave()
{
	RichEditboxComponent::onMouseLeave();

}

void RichEditboxEmotionComponent::OnFrameChange()
{
	RichEditBoxCoponentEventArgs e(this);
	fireEvent(EventFrameChange,e,EventNamespace);
}

String RichEditboxEmotionComponent::GeneratePureText(size_t startPos,size_t endPos)
{
	int maxEmotionID=System::getSingleton().GetEmotionNum();
	if (d_AnimateID>=0&&d_AnimateID<=maxEmotionID)
	{
		String text("#");
		text+=PropertyHelper::intToString(d_AnimateID);
		return text;

	}
	return "";
}




//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
