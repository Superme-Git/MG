
#include "elements/CEGUILinkText.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const String LinkText::EventNamespace("LinkText");
	const String LinkText::WidgetTypeName("CEGUI/LinkText");
	const String LinkText::ClickEventName( "LinkTextClicked" );

	LinkTextProperties::UserID LinkText::d_UserIDProperty;

/*************************************************************************
	Constructor
*************************************************************************/
LinkText::LinkText(const String& type, const String& name) :
	Window(type, name),
	d_Pushed(false),
	d_MouseOn(false),
	d_LinkToWordBook(true),
	d_UserID(0)
{
	SetDragMoveEnable(false);
	addProperty(&d_UserIDProperty);
}


LinkText::LinkText(const String& type) :
	Window(type)
{
}
const LinkText& LinkText::operator=(const LinkText& t)
{
	Window::operator=(t);
	d_MouseOn = t.d_MouseOn;
	d_Pushed = t.d_Pushed;
	d_UserID = t.d_UserID;
	d_LinkToWordBook = t.d_LinkToWordBook;
	return *this;
}
Window* LinkText::clone(Window* wnd)
{
	LinkText* retWnd = (LinkText*)wnd;
	if (retWnd == NULL)
		retWnd = new LinkText(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == LinkText::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

/*************************************************************************
	Destructor
*************************************************************************/
LinkText::~LinkText(void)
{

}


/*************************************************************************
	Handler for mouse button pressed events
*************************************************************************/
void LinkText::onMouseButtonDown(MouseEventArgs& e)
{
	// default processing
	
	Window::onMouseButtonDown(e);

	if (e.button == LeftButton)
	{
		
		d_Pushed=true;

		System::getSingleton().PlayUISound(UIClickSound);
		fireEvent(ClickEventName,e,EventNamespace);

		invalidate();
		
		++e.handled;
	}

}

/*************************************************************************
	Handler for mouse button release events
*************************************************************************/
void LinkText::onMouseButtonUp(MouseEventArgs& e)
{
	// default processing
	Window::onMouseButtonUp(e);

	if (e.button == LeftButton)
	{
		if(d_UserID > 0 && d_LinkToWordBook)
		{
			OnLinkTextClick* pFunc=System::getSingleton().GetLinkTextClickFunc();
			if (pFunc)
			{
				(*pFunc)(d_UserID);
			}
		}
		
		d_Pushed=false;
		releaseInput();
		invalidate();
		
		// event was handled by us.
		++e.handled;
	}

}

/*************************************************************************
	Handler for when mouse capture is lost
*************************************************************************/
void LinkText::onCaptureLost(WindowEventArgs& e)
{
	// Default processing
	Window::onCaptureLost(e);

	d_Pushed=false;
	d_MouseOn=false;
	invalidate();

	++e.handled;
}


/*************************************************************************
	Handler for when mouse leaves the widget
*************************************************************************/
void LinkText::onMouseLeaves(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseLeaves(e);
	
	d_Pushed=false;
	d_MouseOn=false;
	invalidate();

	++e.handled;
}

void LinkText::onMouseEnters(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseEnters(e);
	d_Pushed=false;
	d_MouseOn=true;

	invalidate();

	++e.handled;
}

} // End of  CEGUI namespace section
