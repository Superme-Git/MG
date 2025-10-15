/***********************************************************************
	filename: 	CEGUIPushButton.h
	created:	13/4/2004
	author:		Paul D Turner
	
	purpose:	Interface to base class for PushButton widget
*************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2006 Paul D Turner & The CEGUI Development Team
 *
 *   Permission is hereby granted, free of charge, to any person obtaining
 *   a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including
 *   without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to
 *   permit persons to whom the Software is furnished to do so, subject to
 *   the following conditions:
 *
 *   The above copyright notice and this permission notice shall be
 *   included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *   OTHER DEALINGS IN THE SOFTWARE.
 ***************************************************************************/
#ifndef _CEGUIPushButton_h_
#define _CEGUIPushButton_h_

#include "../CEGUIBase.h"
#include "CEGUIButtonBase.h"
#include "CEGUIPushButtonProperties.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
/*!
\brief
    Base class to provide logic for push button type widgets.
*/

enum PushButtonSizeType
{
	ePushButtonSizeType_Small, //小按扭
	ePushButtonSizeType_Normal, //普通按扭
	ePushButtonSizeType_Big,  //大按扭
	ePushButtonSizeType_Image,//按图片大小
	ePushButtonSizeType_Auto, //自动拉伸
	ePushButtonSizeType_Num //类型个数
};
class CEGUIEXPORT PushButton : public ButtonBase
{
public:
	static const String EventNamespace;				//!< Namespace for global events
    static const String WidgetTypeName;             //!< Window factory name
	

	/*************************************************************************
		Event name constants
	*************************************************************************/
	// generated internally by Window
	static const String EventClicked;					//!< The button was clicked.

	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		Constructor for base PushButton class
	*/
	PushButton(const String& type, const String& name);

	void setText(const String& text, argb_t defcolour =0);
	void SetSizeType(PushButtonSizeType eType);
	PushButtonSizeType GetSizeType(void) const  { return  d_SizeType; }

	void SetAcceptClickButtonType(MouseButton button) { d_AccepClickButtonType=button; }
	void SetPushState(bool b) { d_pushed = b; invalidate(); }

	bool HasFloodLight(){ return d_HasFloodLight; } //是否有流光
	void SetFloodLight(bool bHasFloodLight) { d_HasFloodLight=bHasFloodLight; }
	/*!
	\brief
		Destructor for PushButton class
	*/
	virtual ~PushButton(void);

public:
	PushButton(const String& type);
	virtual Window* clone(Window* wnd);
	const PushButton& operator=(const PushButton&);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return ButtonBase::onRenameTemplatePrefix(sPrefix); }

protected:
	/*************************************************************************
		New Event Handlers
	*************************************************************************/
	/*!
	\brief
		handler invoked internally when the button is clicked.
	*/
	virtual void	onClicked(WindowEventArgs& e);


	/*************************************************************************
		Overridden Event Handlers
	*************************************************************************/
	virtual void	onMouseButtonUp(MouseEventArgs& e);

	virtual void    onMouseButtonDown(MouseEventArgs& e);

	virtual void	onMouseDoubleClicked(MouseEventArgs& e);



	/*************************************************************************
		Implementation Functions
	*************************************************************************/
	/*!
	\brief
		Return whether this window was inherited from the given class name at some point in the inheritance hierarchy.

	\param class_name
		The class name that is to be checked.

	\return
		true if this window was inherited from \a class_name. false if not.
	*/
	virtual bool	testClassName_impl(const String& class_name) const
	{
		if (class_name=="PushButton")	return true;
		return ButtonBase::testClassName_impl(class_name);
	}
private:
	PushButtonSizeType d_SizeType;  //
	static PushButtonProperties::SizeType	d_SizeTypeProperty;
	static PushButtonProperties::EnableClickAni d_enableClickAniProperty;

	void AddPushButtonProperties();
	MouseButton d_AccepClickButtonType;  //左键接受点击还是右键点击
	bool d_HasFloodLight;  //是否有流光效果

};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIPushButton_h_
