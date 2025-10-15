/***********************************************************************
	filename: 	CEGUIAnimateText.h
	purpose:	滚动文本
 ***************************************************************************/
#ifndef _CEGUIAnimateText_h_
#define _CEGUIAnimateText_h_

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
class CEGUIEXPORT AnimateText : public Window
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
	AnimateText(const String& type, const String& name);


	/*!
	\brief
		Destructor for ButtonBase objects
	*/
	virtual ~AnimateText(void);


	/*************************************************************************
		Overridden event handlers
	*************************************************************************/
	virtual void	onMouseButtonDown(MouseEventArgs& e);
	virtual void    updateSelf(float elapsed);

	virtual bool	testClassName_impl(const String& class_name) const
	{
		if (class_name=="AnimateText")	return true;
		return Window::testClassName_impl(class_name);
	}

	void ShowAllText(); //显示所有
	void Reset(); //从新开始播

	void SetTextSpeed(int speed) { d_AniSpeed=speed; }

	const String& GetDrawText() { return d_DrawText; }

	float GetTextEndTime() { return d_TextEndTime; }

	const RenderedString& getRenderedString() const;

	bool isTextEnd() { return d_TextEnd; }
	virtual void setText(const String& text, argb_t defcolour = 0,bool triggerEvent = true);

public:

	AnimateText(const String& type);
	const AnimateText& operator=(const AnimateText& t);
	virtual Window* clone(Window* wnd);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

private:

	/*************************************************************************
		Implementation Data
	*************************************************************************/
	
	float d_TextAniTime; //文字动画持续的时间
	float d_TextEndTime; //文字结束动画已持续时间
	bool  d_TextEnd;
	String d_DrawText;
	int d_AniSpeed;  //文字动画的速度，一秒出现几个字
	
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIButtonBase_h_
