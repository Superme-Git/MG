/***********************************************************************
	filename: 	CEGUIIrregularFigure.h
	purpose:	不规则图形按扭
 ***************************************************************************/
#ifndef _CEGUIIrregularFigure_h_
#define _CEGUIIrregularFigure_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"


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
class CEGUIEXPORT IrregularFigure : public Window
{
public:
	
	static const String EventNamespace;				//!< Namespace for global events
	static const String WidgetTypeName;             //!< Window factory name

	static const String ClickEventName;
	static const int s_iGridPixSize=5; //5*5像素为一个区域
	typedef std::map<int,int> AREAINFMAP; //区域信息map,第一个参数为格子的ID,第二个为区域的ID

	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		Constructor for ButtonBase objects
	*/
	IrregularFigure(const String& type, const String& name);


	/*!
	\brief
		Destructor for ButtonBase objects
	*/
	virtual ~IrregularFigure(void);

	void SetAreaInf(int width,int height,const AREAINFMAP& AreaInfs); //设置区域信息
	void subscribeAreaClickEvent(int areaID, Event::Subscriber subscriber);

	void SetBackImage(const Image* pBackImage);
	void SetHoverImageName(const String& imageSetName,const String& imageName);

	const Image* GetBackImage() { return d_BackImage; }
	const Image* GetHoverImage() { return d_HoverImage; }

	int GetSelectAreaID() { return d_SelectAreaID; }

	bool isLock() { return d_Lock; }
	float GetAngle();

	void SetAngle(float angle){d_Angle = angle;}
	int GetMouseHoverAreaID() { return d_MouseOnAreaID; }

	void SetRotationSpeed(float fSpeed) {d_RatationSpeed=fSpeed; }
	
	bool SetRotationTarget(int targetareaid);

public:
	IrregularFigure(const String& type);
	const IrregularFigure& operator=(const IrregularFigure& t);
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
	virtual void    updateSelf(float elapsed);
	
	virtual bool	testClassName_impl(const String& class_name) const
	{
		if (class_name=="IrregularFigure")	return true;
		return Window::testClassName_impl(class_name);
	}

	int GetAreaIDByScreenPos(const Point& ScreenPt);
	float GetAngleByClickAreaID(int areaID);
private:


	/*************************************************************************
		Implementation Data
	*************************************************************************/
	int	d_MouseOnAreaID;			//鼠标所在的子控件的ID
	int     d_SelectAreaID;
	const Image* d_BackImage;
	const Image* d_HoverImage;
	String  d_HoverImageSetName;
	String  d_HoverImageName;
	AREAINFMAP d_AreaInfMap;
	int	d_AreaWidth;
	int d_AreaHeight;
	bool d_Lock;
	float d_Angle;
	float d_ElapseTime;
	float d_RatationSpeed; //一秒钟转多少度 单位角度
	
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIButtonBase_h_
