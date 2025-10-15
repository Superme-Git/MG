#include "CEGUIPropertyHelper.h"
#include "CEGUIImagesetManager.h"
#include "CEGUICoordConverter.h"
#include "elements/CEGUIAnimationButton.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const String AnimationButton::EventNamespace("AnimationButton");
	const String AnimationButton::WidgetTypeName("CEGUI/AnimationButton");
	const String AnimationButton::ClickEventName( "AnimationButtonClicked" );

	AnimationButtonProperties::ImageSetName	AnimationButton::d_ImageSetNameProperty;
	AnimationButtonProperties::ImageName	AnimationButton::d_ImageNameProperty;
	AnimationButtonProperties::TotalFrame	AnimationButton::d_TotalFrameProperty;
	AnimationButtonProperties::AnimationSpeed	AnimationButton::d_AniSpeedProperty;

	
/*************************************************************************
	Constructor
*************************************************************************/
AnimationButton::AnimationButton(const String& type, const String& name) :
	Window(type, name),
		d_MouseOn(false),
		d_NormalImage(NULL),
		d_ImageSetName("CartoonBtn"),
		d_ImageName(""),
		d_AnimationSpeed(6),
		d_ElapseTime(0.0f),
		d_TotalFrame(2)
{
	SetDragMoveEnable(false);
	addProperty(&d_ImageSetNameProperty);
	addProperty(&d_ImageNameProperty);
	addProperty(&d_TotalFrameProperty);
	addProperty(&d_AniSpeedProperty);
}


AnimationButton::AnimationButton(const String& type) :
	Window(type)
{
}
const AnimationButton& AnimationButton::operator=(const AnimationButton& t)
{
	Window::operator=(t);
	d_NormalImage = t.d_NormalImage;
	d_ImageSetName = t.d_ImageSetName;
	d_ImageName = t.d_ImageName;
	d_MouseOn = t.d_MouseOn;
	d_AnimationSpeed = t.d_AnimationSpeed;
	d_TotalFrame = t.d_TotalFrame;
	d_ElapseTime = t.d_ElapseTime;
	return *this;
}
Window* AnimationButton::clone(Window* wnd)
{
	AnimationButton* retWnd = (AnimationButton*)wnd;
	if (retWnd == NULL)
		retWnd = new AnimationButton(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == AnimationButton::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

/*************************************************************************
	Destructor
*************************************************************************/
AnimationButton::~AnimationButton(void)
{
}





///*************************************************************************
//	Handler for when the mouse moves
//*************************************************************************/
//void AnimationButton::onMouseMove(MouseEventArgs& e)
//{
//	// this is needed to discover whether mouse is in the widget area or not.
//	// The same thing used to be done each frame in the rendering method,
//	// but in this version the rendering method may not be called every frame
//	// so we must discover the internal widget state here - which is actually
//	// more efficient anyway.
//
//	// base class processing
//	Window::onMouseMove(e);
//	
//	++e.handled;
//}


/*************************************************************************
	Handler for mouse button pressed events
*************************************************************************/
void AnimationButton::onMouseButtonDown(MouseEventArgs& e)
{
	// default processing
	
	Window::onMouseButtonDown(e);

	if (e.button == LeftButton)
	{
		if (isSoundEable())
		{
			String soundRes=GetSoundResource();
			if (soundRes.empty())
			{
				System::getSingleton().PlayUISound(UIClickSound);
			}else
			{
				System::getSingleton().PlayUISound(soundRes);
			}
		}
		String eventName(ClickEventName);
		fireEvent(eventName,e,EventNamespace);

	}
	++e.handled;

}


/*************************************************************************
	Handler for mouse button release events
*************************************************************************/
void AnimationButton::onMouseButtonUp(MouseEventArgs& e)
{
	// default processing
	Window::onMouseButtonUp(e);

	if (e.button == LeftButton)
	{
		releaseInput();

		// event was handled by us.
		++e.handled;
	}

}

/*************************************************************************
	Handler for when mouse capture is lost
*************************************************************************/
void AnimationButton::onCaptureLost(WindowEventArgs& e)
{
	// Default processing
	Window::onCaptureLost(e);

	d_MouseOn=false;
	invalidate();
	++e.handled;
}


/*************************************************************************
	Handler for when mouse leaves the widget
*************************************************************************/
void AnimationButton::onMouseLeaves(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseLeaves(e);
	d_MouseOn=false;
	invalidate();

	++e.handled;
}

void AnimationButton::onMouseEnters(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseEnters(e);
	d_MouseOn=true;
	invalidate();

	++e.handled;
}



void   AnimationButton::updateSelf(float elapsed)
{

	d_ElapseTime+=elapsed;
		
	Window::updateSelf(elapsed);
	invalidate();
}

void AnimationButton::SetImageSetName(const String& name)
{
	if (d_ImageSetName!=name )
	{
		d_ImageSetName=name;
		invalidate();
	}
}
void AnimationButton::SetImageName(const String& name)
{
	if (d_ImageName!=name)
	{
		if (ImagesetManager::getSingleton().isDefined(d_ImageSetName))
		{
			const Imageset* pSet=&ImagesetManager::getSingleton().get(d_ImageSetName);
			String firstImageName(name);
			firstImageName+="1";
			if (pSet->isImageDefined(firstImageName))
			{
				d_ImageName=name;
				d_NormalImage=&pSet->getImage(firstImageName);
			}
		}
	}
}

void AnimationButton::SetTotalFrame(int totalFrame)
{
	if (totalFrame!=d_TotalFrame)
	{
		d_TotalFrame=totalFrame;
	}
}
int AnimationButton::GetCurFrame()
{
	float duration=1.0f/d_AnimationSpeed;
	int round=(int)(d_ElapseTime/duration);
	int frame=static_cast<int>((d_ElapseTime-round*duration)/duration*d_TotalFrame)+1;
	if (frame<1)
	{
		frame=1;
	}
	if (frame>d_TotalFrame)
	{
		frame=d_TotalFrame;
	}
	return frame;

}



} // End of  CEGUI namespace section
