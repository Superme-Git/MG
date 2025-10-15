/***********************************************************************
	filename: 	CEGUIIrregularFigure.h
	purpose:	������ͼ�ΰ�Ť
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
	static const int s_iGridPixSize=5; //5*5����Ϊһ������
	typedef std::map<int,int> AREAINFMAP; //������Ϣmap,��һ������Ϊ���ӵ�ID,�ڶ���Ϊ�����ID
	typedef std::map<int,bool> AREAENABLESTATEMAP; //����enablemap
	typedef std::vector<const Image*> DRAWIMAGES;  //Ҫ����ͼƬ��
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

	void SetAreaInf(int width,int height,const AREAINFMAP& AreaInfs); //����������Ϣ
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
	void UpdataState(); //����״̬

	static IrregularButtonProperties::ImageSetName d_ImageSetNameProperty;
	static IrregularButtonProperties::ImageName d_ImageNameProperty;

private:


	/*************************************************************************
		Implementation Data
	*************************************************************************/
	int	d_MouseOnAreaID;			//������ڵ��ӿؼ���ID
	int     d_SelectAreaID;
	String  d_ImageSetName;
	String  d_ImageName;
	const Image* d_Image;
	AREAINFMAP d_AreaInfMap;
	AREAENABLESTATEMAP d_AreaEnableStateMap;
	DRAWIMAGES d_DrawImages;
	int d_AreaWidth;
	int d_AreaHeight;
	bool d_Pushed;  //�ͷ��ڰ���
	
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIButtonBase_h_
