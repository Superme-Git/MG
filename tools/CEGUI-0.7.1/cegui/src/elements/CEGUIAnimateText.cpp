
#include "elements/CEGUIAnimateText.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const String AnimateText::EventNamespace("AnimateText");
	const String AnimateText::WidgetTypeName("CEGUI/AnimateText");
	const String AnimateText::ClickEventName( "AnimateTextClicked" );


/*************************************************************************
	Constructor
*************************************************************************/
AnimateText::AnimateText(const String& type, const String& name) :
	Window(type, name),
	d_TextEnd(false),
	d_TextAniTime(0.0f),
	d_TextEndTime(0.0f),
	d_DrawText(""),
	d_AniSpeed(16)
{

}

AnimateText::AnimateText(const String& type) :
	Window(type)
{
}

const AnimateText& AnimateText::operator=(const AnimateText& t)
{
	Window::operator=(t);
	d_TextAniTime = t.d_TextAniTime;
	d_TextEndTime = t.d_TextEndTime;
	d_TextEnd = t.d_TextEnd;
	d_DrawText = t.d_DrawText;
	d_AniSpeed = t.d_AniSpeed;
	return *this;
}

Window* AnimateText::clone(Window* wnd)
{
	AnimateText* retWnd = (AnimateText*)wnd;
	if (retWnd == NULL)
		retWnd = new AnimateText(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == AnimateText::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

/*************************************************************************
	Destructor
*************************************************************************/
AnimateText::~AnimateText(void)
{

}

const RenderedString& AnimateText::getRenderedString() const
{
	if (!d_renderedStringValid)
	{
		d_renderedString = getRenderedStringParser().parse(
			d_DrawText, getFont(), 0);
		d_renderedStringValid = true;
	}

	return d_renderedString;
}


/*************************************************************************
	Handler for mouse button pressed events
*************************************************************************/
void AnimateText::onMouseButtonDown(MouseEventArgs& e)
{
	// default processing
	
	Window::onMouseButtonDown(e);

	if (e.button == LeftButton)
	{
		fireEvent(ClickEventName,e,EventNamespace);

		++e.handled;
	}

}

void AnimateText::updateSelf(float elapse)
{
	String text=getText();
	String newDrawText(d_DrawText);
	if (text.size()<=0)
	{
		return;
	}
	if (d_TextEnd)
	{
		d_TextEndTime+=elapse;
		
	}else
	{
		d_TextAniTime+=elapse;
		size_t showCharNum=0;
		showCharNum=(size_t)(d_TextAniTime*(float)d_AniSpeed);
		if (showCharNum>=9999)
		{
			//Logger::getSingleton().logEvent("AniTextShowCharNum overLoad",Errors);
			return;
		}
		if (showCharNum>=text.size())
		{
			d_TextEnd=true;
			d_TextAniTime=0.0f;
			d_TextEndTime=0.0f;
			newDrawText=text;
		}else 
		{
			newDrawText=text.substr(0,showCharNum);
		}

	}
	if (newDrawText!=d_DrawText)
	{
		d_DrawText=newDrawText;
		d_renderedStringValid=false;
		invalidate();
	}
}

void AnimateText::ShowAllText() //显示所有
{
	d_TextEnd=true;
	d_TextAniTime=0.0f;
	d_TextEndTime=0.0f;
	d_DrawText=getText();
	d_renderedStringValid=false;

	invalidate();

}
void AnimateText::Reset() //从新开始播
{
	d_TextEnd=false;
	d_TextAniTime=0.0f;
	d_TextEndTime=0.0f;
	d_DrawText="";
	d_renderedStringValid=false;

	invalidate();
}

    void AnimateText::setText(const String& text, argb_t defcolour,bool triggerEvent) {
        if (getText() != text)
            Reset();
        Window::setText(text, defcolour, triggerEvent);
    }
/*************************************************************************
	Handler for mouse button release events
*************************************************************************/


} // End of  CEGUI namespace section
