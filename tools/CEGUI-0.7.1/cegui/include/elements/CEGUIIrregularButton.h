/***********************************************************************
	filename: 	CEGUIIrregularFigure.h
	purpose:	不规则图形按扭
 ***************************************************************************/
#ifndef _CEGUIIrregularButton_h_
#define _CEGUIIrregularButton_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "CEGUIIrregularButtonProperties.h"


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{

/*!
\brief
	Base class for all the 'button' type widgets (push button, radio button, check-box, etc)
*/
class CEGUIEXPORT IrregularButton : public Window
{
public:
	
	static const String EventNamespace;				//!< Namespace for global events
	static const String WidgetTypeName;             //!< Window factory name

	static const String ClickEventName;
	static const int s_iGridPixSize=5; //5*5像素为一个区域
	typedef std::map<int,int> AREAINFMAP; //区域信息map,第一个参数为格子的ID,第二个为区域的ID
	typedef std::map<int,bool> AREAENABLESTATEMAP; //区域enablemap
	typedef std::vector<const Image*> DRAWIMAGES;  //要画的图片集
	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		Constructor for ButtonBase objects
	*/
	IrregularButton(const String& type, const String& name);


	/*!
	\brief
		Destructor for ButtonBase objects
	*/
	virtual ~IrregularButton(void);

	void SetAreaInf(int width,int height,const AREAINFMAP& AreaInfs); //设置区域信息
	void subscribeAreaClickEvent(int areaID, Event::Subscriber subscriber);

	void SetImageSetName(const String& imageSetName);
	void SetImageName(const String& imageName);

	const String& GetImageSetName() const  { return d_ImageSetName; }
	const String& GetImageName() const { return d_ImageName; }


	int GetSelectAreaID() { return d_SelectAreaID; }
	int GetMouseHoverAreaID() { return d_MouseOnAreaID; }

	const Image* GetImage() { return d_Image; }

	void SetAreaEnableState(int areaID,bool state);
	bool isAreaEnable(int areaID);

	const DRAWIMAGES& GetDrawImages() { return d_DrawImages; }

public:
	IrregularButton(const String& type);
	const IrregularButton& operator=(const IrregularButton& t);
	virtual Window* clone(Window* wnd);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

protected:
	/*************************************************************************
		Overridden event handlers
	*************************************************************************/
	virtual void	onMouseMove(MouseEventArgs& e);
	virtual void	onMouseButtonDown(MouseEventArgs& e);
	virtual void	onMouseButtonUp(MouseEventArgs& e);
	virtual void	onCaptureLost(WindowEventArgs& e);
	virtual void    onMouseEnters(MouseEventArgs& e);
	virtual void	onMouseLeaves(MouseEventArgs& e);
	
	virtual bool	testClassName_impl(const String& class_name) const
	{
		if (class_name=="IrregularButton")	return true;
		return Window::testClassName_impl(class_name);
	}

	int GetAreaIDByScreenPos(const Point& ScreenPt);
	void UpdataState(); //更新状态

	static IrregularButtonProperties::ImageSetName d_ImageSetNameProperty;
	static IrregularButtonProperties::ImageName d_ImageNameProperty;

private:


	/*************************************************************************
		Implementation Data
	*************************************************************************/
	int	d_MouseOnAreaID;			//鼠标所在的子控件的ID
	int     d_SelectAreaID;
	String  d_ImageSetName;
	String  d_ImageName;
	const Image* d_Image;
	AREAINFMAP d_AreaInfMap;
	AREAENABLESTATEMAP d_AreaEnableStateMap;
	DRAWIMAGES d_DrawImages;
	int d_AreaWidth;
	int d_AreaHeight;
	bool d_Pushed;  //就否在按下
	
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIButtonBase_h_
