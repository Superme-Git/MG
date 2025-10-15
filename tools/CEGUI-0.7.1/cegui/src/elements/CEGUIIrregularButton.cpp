#include "CEGUIPropertyHelper.h"
#include "CEGUIImagesetManager.h"
#include "CEGUICoordConverter.h"
#include "elements/CEGUIIrregularButton.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const String IrregularButton::EventNamespace("IrregularButton");
	const String IrregularButton::WidgetTypeName("CEGUI/IrregularButton");
	const String IrregularButton::ClickEventName( "IrregularButtonClicked" );

	IrregularButtonProperties::ImageSetName IrregularButton::d_ImageSetNameProperty;
	IrregularButtonProperties::ImageName IrregularButton::d_ImageNameProperty;
	
/*************************************************************************
	Constructor
*************************************************************************/
IrregularButton::IrregularButton(const String& type, const String& name) :
	Window(type, name),
    d_MouseOnAreaID(0),
	d_SelectAreaID(0),
	d_AreaHeight(0),
	d_AreaWidth(0),
	d_Pushed(false),
	d_ImageSetName(""),
	d_ImageName(""),
	d_Image(NULL)
	
{
	d_AreaInfMap.clear();
	d_DrawImages.clear();
	d_AreaEnableStateMap.clear();
	SetDragMoveEnable(false);
	addProperty(&d_ImageSetNameProperty);
	addProperty(&d_ImageNameProperty);
}


IrregularButton::IrregularButton(const String& type) :
	Window(type)
{
}
const IrregularButton& IrregularButton::operator=(const IrregularButton& t)
{
	Window::operator=(t);
	d_MouseOnAreaID = t.d_MouseOnAreaID;
	d_SelectAreaID = t.d_SelectAreaID;
	d_ImageSetName = t.d_ImageSetName;
	d_ImageName = t.d_ImageName;
	d_Image = t.d_Image;
	d_AreaInfMap = t.d_AreaInfMap;
	d_AreaEnableStateMap = t.d_AreaEnableStateMap;
	d_DrawImages = t.d_DrawImages;
	d_AreaWidth = t.d_AreaWidth;
	d_AreaHeight = t.d_AreaHeight;
	d_Pushed = t.d_Pushed;
	return *this;
}
Window* IrregularButton::clone(Window* wnd)
{
	IrregularButton* retWnd = (IrregularButton*)wnd;
	if (retWnd == NULL)
		retWnd = new IrregularButton(d_type);
#ifdef _DEBUG
	const char* type = d_type.c_str();
	assert(d_type == IrregularButton::WidgetTypeName);
#endif
	*retWnd = *this;
	return retWnd;
}

/*************************************************************************
	Destructor
*************************************************************************/
IrregularButton::~IrregularButton(void)
{
}





/*************************************************************************
	Handler for when the mouse moves
*************************************************************************/
void IrregularButton::onMouseMove(MouseEventArgs& e)
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
		d_Pushed=false;
		UpdataState();

		invalidate();
	}
	++e.handled;
}


/*************************************************************************
	Handler for mouse button pressed events
*************************************************************************/
void IrregularButton::onMouseButtonDown(MouseEventArgs& e)
{
	// default processing
	
	Window::onMouseButtonDown(e);

	if (e.button == LeftButton)
	{
		
		int areaID=GetAreaIDByScreenPos(e.position);
		if (areaID>0)
		{
			if (!isAreaEnable(areaID))
			{
				return;
			}
			d_Pushed=true;
			System::getSingleton().PlayUISound(UIClickSound);
			d_SelectAreaID=areaID;
			UpdataState();
			String eventName(ClickEventName);
			eventName+=PropertyHelper::intToString(areaID);
			fireEvent(eventName,e,EventNamespace);
			
		}
		
		++e.handled;
	}

}

/*************************************************************************
	Handler for mouse button release events
*************************************************************************/
void IrregularButton::onMouseButtonUp(MouseEventArgs& e)
{
	// default processing
	Window::onMouseButtonUp(e);

	if (e.button == LeftButton)
	{
		d_Pushed=false;
		UpdataState();

		releaseInput();

		// event was handled by us.
		++e.handled;
	}

}

/*************************************************************************
	Handler for when mouse capture is lost
*************************************************************************/
void IrregularButton::onCaptureLost(WindowEventArgs& e)
{
	// Default processing
	Window::onCaptureLost(e);

	d_MouseOnAreaID=0;
	d_Pushed=false;
	UpdataState();


	++e.handled;
}


/*************************************************************************
	Handler for when mouse leaves the widget
*************************************************************************/
void IrregularButton::onMouseLeaves(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseLeaves(e);
	
	d_Pushed=false;
	d_MouseOnAreaID=0;
	UpdataState();

	invalidate();

	++e.handled;
}

void IrregularButton::onMouseEnters(MouseEventArgs& e)
{
	// deafult processing
	Window::onMouseEnters(e);

	d_MouseOnAreaID=0;
	d_Pushed=false;
	UpdataState();

	invalidate();

	++e.handled;
}

void IrregularButton::SetAreaInf(int width,int height,const AREAINFMAP& AreaInfs) //设置区域信息
{
	d_AreaInfMap.clear();
	AREAINFMAP::const_iterator it=AreaInfs.begin();
	d_AreaWidth = width;
	d_AreaHeight = height;
	for (;it!=AreaInfs.end();++it)
	{
		d_AreaInfMap[it->first]=it->second;
		if (it->second>0)
		{
			d_AreaEnableStateMap[it->second]=true;
		}
		
	}
	UpdataState();
}
void IrregularButton::subscribeAreaClickEvent(int areaID, Event::Subscriber subscriber)
{
	
	String eventName(ClickEventName);
	eventName+=PropertyHelper::intToString(areaID);
	if (isEventPresent(eventName))
	{
		removeEvent(eventName);
	}
	
	subscribeEvent(eventName,subscriber);

}




int IrregularButton::GetAreaIDByScreenPos(const Point& ScreenPos)
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
void IrregularButton::UpdataState() //更新状态
{
	/*const Image* pNewImage=NULL;
	if (!d_ImageSetName.empty()&&!d_ImageName.empty())
	{
		String imageName(d_ImageName);
		if (d_MouseOnAreaID>0)
		{
			imageName+="Area";
			imageName+=PropertyHelper::int64_tToString(d_MouseOnAreaID);
			if (d_Pushed)
			{
				imageName+="Pushed";
			}else
			{
				imageName+="Hover";
			}
		}else
		{
			imageName+="Normal";
		}

		if (ImagesetManager::getSingleton().isDefined(d_ImageSetName))
		{
			const Imageset* pSet=&ImagesetManager::getSingleton().get(d_ImageSetName);
			
			if (pSet->isImageDefined(imageName))
			{
				pNewImage=&pSet->getImage(imageName);
			}
		}
	}else
	{
		pNewImage=NULL;
	}

	if (pNewImage!=d_Image)
	{
		d_Image=pNewImage;
		invalidate();
	}*/
	d_DrawImages.clear();
	if (!d_ImageSetName.empty()&&!d_ImageName.empty())
	{
		AREAENABLESTATEMAP::iterator it=d_AreaEnableStateMap.begin();
		for (;it!=d_AreaEnableStateMap.end();++it)
		{
			int areaID=it->first;
			bool EnableState=it->second;
			String imageName(d_ImageName);
			imageName+="Area";
			imageName+=PropertyHelper::intToString(areaID);
			if (EnableState)
			{
				if (d_MouseOnAreaID>0&&d_MouseOnAreaID==areaID)
				{
					if (d_Pushed)
					{
						imageName+="Pushed";
					}else
					{
						imageName+="Hover";
					}
				}else
				{
					imageName+="Normal";
				}
			}else{
				imageName+="Disable";
			}

			if (ImagesetManager::getSingleton().isDefined(d_ImageSetName))
			{
				const Imageset* pSet=&ImagesetManager::getSingleton().get(d_ImageSetName);

				if (pSet->isImageDefined(imageName))
				{
					const Image* pImage=&pSet->getImage(imageName);
					d_DrawImages.push_back(pImage);
				}
			}
			
		}
	}
	invalidate();
	
}

void IrregularButton::SetImageSetName(const String& imageSetName)
{ 
	d_ImageSetName=imageSetName; 
	UpdataState();
}
void IrregularButton::SetImageName(const String& imageName) 
{ 
	d_ImageName=imageName;
	UpdataState();
}

void IrregularButton::SetAreaEnableState(int areaID,bool state)
{
	d_AreaEnableStateMap[areaID]=state;
	UpdataState();
}

bool IrregularButton::isAreaEnable(int areaID)
{
	AREAENABLESTATEMAP::iterator it=d_AreaEnableStateMap.find(areaID);
	if (it!=d_AreaEnableStateMap.end())
	{
		return it->second;
	}
	return false;

}









} // End of  CEGUI namespace section
