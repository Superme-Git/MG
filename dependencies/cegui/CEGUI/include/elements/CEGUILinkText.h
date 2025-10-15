/***********************************************************************
	filename: 	CEGUILinkText.h
	purpose:	不规则图形按扭
 ***************************************************************************/
#ifndef _CEGUILinkText_h_
#define _CEGUILinkText_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "CEGUILinkTextProperties.h"


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
class CEGUIEXPORT LinkText : public Window
{
public:
	
	static const String EventNamespace;				//!< Namespace for global events
	static const String WidgetTypeName;             //!< Window factory name

	static const String ClickEventName;
	
	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		Constructor for ButtonBase objects
	*/
	LinkText(const String& type, const String& name);


	/*!
	\brief
		Destructor for ButtonBase objects
	*/
	virtual ~LinkText(void);

	bool isMouseOn(){ return d_MouseOn;}
	bool isPushed() { return d_Pushed; }
	int GetUserID()const { return d_UserID; } 
	void SetUserID(int id) { d_UserID=id; }

	void SetClickLinkToWordBook(bool bLinkToWordBook){d_LinkToWordBook = bLinkToWordBook;}

public:
	LinkText(const String& type);
	const LinkText& operator=(const LinkText& t);
	virtual Window* clone(Window* wnd);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

protected:
	/*************************************************************************
		Overridden event handlers
	*************************************************************************/
	virtual void	onMouseButtonDown(MouseEventArgs& e);
	virtual void	onMouseButtonUp(MouseEventArgs& e);
	virtual void	onCaptureLost(WindowEventArgs& e);
	virtual void    onMouseEnters(MouseEventArgs& e);
	virtual void	onMouseLeaves(MouseEventArgs& e);
	
	virtual bool	testClassName_impl(const String& class_name) const
	{
		if (class_name=="LinkText")	return true;
		return Window::testClassName_impl(class_name);
	}

	static LinkTextProperties::UserID d_UserIDProperty;
	
private:


	/*************************************************************************
		Implementation Data
	*************************************************************************/
	
	bool d_MouseOn; //是否mouse经过
	bool d_Pushed;  //是否在按下
	int d_UserID;   //用户设置的ID
	bool d_LinkToWordBook;//点击后是否连接到世界之书
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIButtonBase_h_
