#include "CEGUIPropertyHelper.h"
#include "CEGUIImagesetManager.h"
#include "CEGUICoordConverter.h"
#include "elements/CEGUIIrregularFigure.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const String IrregularFigure::EventNamespace("IrregularFigure");
	const String IrregularFigure::WidgetTypeName("CEGUI/IrregularFigure");
	const String IrregularFigure::ClickEventName( "IrregularFigureClicked" );

	
/*************************************************************************
	Constructor
*************************************************************************/
IrregularFigure::IrregularFigure(const String& type, const String& name) :
	Window(type, name),
    d_MouseOnAreaID(0),
	d_SelectAreaID(0),
	d_BackImage(0),
	d_HoverImage(0),
	d_HoverImageName(""),
	d_HoverImageSetName(""),
	d_Lock(false),
	d_Angle(0.0f),
	d_ElapseTime(0.0f),
	d_RatationSpeed(160.0f),
	d_AreaWidth(0),
	d_AreaHeight(0)
{
	d_AreaInfMap.clear();
	SetDragMoveEnable(false);
}


IrregularFigure::IrregularFigure(const String& type) :
	Window(type)
{
}
const IrregularFigure& IrregularFigure::operator=(const IrregularFigure& t)
{
	Window::operator=(t);
	d_MouseOnAreaID = t.d_MouseOnAreaID;
	d_SelectAreaID = t.d_SelectAreaID;
	d_BackImage = t.d_BackImage;
	d_HoverImage = t.d_HoverImage;
	d_HoverImageSetName = t.d_HoverImageSetName;
	d_HoverImageName = t.d_HoverImageName;
	d_AreaInfMap = t.d_AreaInfMap;
	d_AreaWidth = t.d_AreaWidth;
	d_AreaHeight = t.d_AreaHeight;
	d_Lock = t.d_Lock;
	d_Angle = t.d_Angle;
	d_ElapseTime = t.d_ElapseTime;
	d_RatationSpeed = t.d_RatationSpeed;
	return *this;
}
Window* IrregularFigure::clone(Window* wnd)
{
	IrregularFigure* retWnd = (IrregularFigure*)wnd;
	if (retWnd == NULL)
		retWnd = new IrregularFigure(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == IrregularFigure::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

/*************************************************************************
	Destructor
*************************************************************************/
IrregularFigure::~IrregularFigure(void)
{
}





/*************************************************************************
	Handler for when the mouse moves
*************************************************************************/
void IrregularFigure::onMouseMove(MouseEventArgs& e)
{
	// this is needed to discover whether mouse is in the widget area or not.
	// The same thing used to be done each frame in the rendering method,
	// but in this version the rendering method may not be called every frame
	// so we must discover the internal widget state here - which is actually
	// more efficient anyway.

	// base class processing
	Window::onMouseMove(e);
	int areaID=GetAreaIDByScreenPos(e.position);
	if (areaID!=d_MouseOnAreaID)
	{
		/*if (areaID>0)
		{
			String imageName(d_HoverImageName);
			imageName+=PropertyHelper::intToString(areaID);
			d_HoverImage=&ImagesetManager::getSingleton().get(d_HoverImageSetName).getImage(imageName);
		}else
		{
			d_HoverImage=NULL;
		}*/
		d_MouseOnAreaID=areaID;
		invalidate();
	}
	++e.handled;
}


/*************************************************************************
	Handler for mouse button pressed events
*************************************************************************/
void IrregularFigure::onMouseButtonDown(MouseEventArgs& e)
{
	// default processing
	
	Window::onMouseButtonDown(e);

	if (d_Lock)
	{
		return;
	}
	if (e.button == LeftButton)
	{
		
		int areaID=GetAreaIDByScreenPos(e.position);
		if (areaID>0)
		{
			d_Lock=true;
			d_SelectAreaID=areaID;
			String eventName(ClickEventName);
			eventName+=PropertyHelper::intToString(areaID);
			fireEvent(eventName,e,EventNamespace);
		}
		
		++e.handled;
	}
}

//转至某个区域
bool IrregularFigure::SetRotationTarget(int targetareaid)
{
	if (d_Lock)
	{
		return false;
	}
	d_Lock = true;
	int areaID=targetareaid;
	if (areaID>0)
	{
		d_Lock=true;
		d_SelectAreaID=areaID;
		return true;
		//String eventName(ClickEventName);
		//eventName+=PropertyHelper::intToString(areaID);
		//fireEvent(eventName,e,EventNamespace);
	}
	return false;
}

/*************************************************************************
	Handler for mouse button release events
*************************************************************************/
void IrregularFigure::onMouseButtonUp(MouseEventArgs& e)
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
void IrregularFigure::onCaptureLost(WindowEventArgs& e)
{
	// Default processing
	Window::onCaptureLost(e);

	d_MouseOnAreaID=0;
	d_HoverImage=NULL;

	++e.handled;
}


/*************************************************************************
	Handler for when mouse leaves the widget
*************************************************************************/
void IrregularFigure::onMouseLeaves(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseLeaves(e);
	
	d_MouseOnAreaID=0;
	d_HoverImage=NULL;
	invalidate();

	++e.handled;
}

void IrregularFigure::onMouseEnters(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseEnters(e);

	d_MouseOnAreaID=0;
	d_HoverImage=NULL;
	invalidate();

	++e.handled;
}

void IrregularFigure::SetAreaInf(int width,int height,const AREAINFMAP& AreaInfs) //设置区域信息
{
	d_AreaInfMap.clear();
	AREAINFMAP::const_iterator it=AreaInfs.begin();
	d_AreaWidth = width;
	d_AreaHeight = height;
	for (;it!=AreaInfs.end();++it)
	{
		d_AreaInfMap[it->first]=it->second;
	}
}
void IrregularFigure::subscribeAreaClickEvent(int areaID, Event::Subscriber subscriber)
{
	
	String eventName(ClickEventName);
	eventName+=PropertyHelper::intToString(areaID);
	if (isEventPresent(eventName))
	{
		removeEvent(eventName);
	}
	
	subscribeEvent(eventName,subscriber);

}

void IrregularFigure::SetBackImage(const Image* pBackImage)
{
	if (d_BackImage!=pBackImage)
	{
		d_BackImage=pBackImage;
		invalidate();
	}
}
void IrregularFigure::SetHoverImageName(const String& imageSetName,const String& imageName)
{
	d_HoverImageSetName=imageSetName;
	d_HoverImageName=imageName;

}

int IrregularFigure::GetAreaIDByScreenPos(const Point& ScreenPos)
{
	Point localPos(0.0f,0.0f);
	Point topLeft=GetScreenPos();
	localPos=ScreenPos-topLeft;
	int gridX=static_cast<int>(localPos.d_x/5.0f);
	int gridY=static_cast<int>(localPos.d_y/5.0f);
	int idx=gridY*d_AreaWidth+gridX;
	AREAINFMAP::iterator it=d_AreaInfMap.find(idx);
	if (it!=d_AreaInfMap.end())
	{
		return it->second;
	}
	
	return 0;
}

void   IrregularFigure::updateSelf(float elapsed)
{
	if (d_Lock)
	{
		d_ElapseTime+=elapsed;
		float angle=GetAngleByClickAreaID(d_SelectAreaID);
		if (d_ElapseTime>(abs(angle)/d_RatationSpeed))
		{
			d_Lock=false;
			d_ElapseTime=0.0f;
			d_Angle=d_Angle+angle;
			/*Vector3 rotation(0.0f,0.0f,d_Angle);
			setRotation(rotation);*/

		}
	}
	Window::updateSelf(elapsed);
}

float IrregularFigure::GetAngleByClickAreaID(int areaID)
{
	float scale=3.0f;
	if (areaID>0&&areaID<=3)
	{
		return (360.0f/7.0f)*(float)areaID;

	}else
	{
		if (areaID<=6)
		{
			return (-360.0f/7.0f)*(float)(abs(areaID-6)+1);
		}
	}
	return 0.0f;
	
}

float IrregularFigure::GetAngle()
{
	if (d_Lock)
	{
		float angle=GetAngleByClickAreaID(d_SelectAreaID);
		if (angle>0.0f)
		{
			return d_Angle+d_RatationSpeed*d_ElapseTime;
		}else
		{
			return d_Angle-d_RatationSpeed*d_ElapseTime;
		}
		
	}else
	{
		return d_Angle;
	}
}

} // End of  CEGUI namespace section
