/***********************************************************************
	filename: 	CEGUICheckboxProperties.cpp
	created:	9/7/2004
	author:		Paul D Turner
	
	purpose:	Implements properties for the Checkbox class
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
#include "elements/CEGUIPushButtonProperties.h"
#include "elements/CEGUIPushButton.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIExceptions.h"
#include <cstdlib>
#include <cstdio>


// Start of CEGUI namespace section
namespace CEGUI
{

// Start of CheckboxProperties namespace section
namespace PushButtonProperties
{
	String SizeType::get(const PropertyReceiver* receiver) const
	{
		//const PushButton* pBtn=(const PushButton*)(receiver);
		PushButtonSizeType sizeType=static_cast<const PushButton*>(receiver)->GetSizeType();
		switch (sizeType)
		{
		case ePushButtonSizeType_Small:
			{
				return "Small";
			}
			break;
		case ePushButtonSizeType_Normal:
			{
				return "Normal";
			}
			break;
		case ePushButtonSizeType_Big:
			{
				return "Big";
			}
			break;
		case ePushButtonSizeType_Image:
			{
				return "Image";
			}
			break;
		case ePushButtonSizeType_Auto:
			{
				return "Auto";
			}
			break;
		default:
			{
				return "Auto";
			}
		}
		
		
	}


	void SizeType::set(PropertyReceiver* receiver, const String& value)
	{
		PushButtonSizeType eType=ePushButtonSizeType_Auto;
		if (value=="Small")
		{
			eType= ePushButtonSizeType_Small;
		}else if (value=="Normal")
		{
			eType= ePushButtonSizeType_Normal;
		}else if (value=="Big")
		{
			eType= ePushButtonSizeType_Big;
		}
		else if (value=="Image")
		{
			eType= ePushButtonSizeType_Image;
		}else if (value=="Auto")
		{
			eType= ePushButtonSizeType_Auto;
		}
		
		static_cast<PushButton*>(receiver)->SetSizeType(eType);
	}


	String EnableClickAni::get(const PropertyReceiver* receiver) const
	{
		return PropertyHelper::boolToString(static_cast<const PushButton*>(receiver)->isClickAniEnable());
	}


	void EnableClickAni::set(PropertyReceiver* receiver, const String& value)
	{
		static_cast<PushButton*>(receiver)->EnableClickAni(PropertyHelper::stringToBool(value));
	}

} // End of  CheckboxProperties namespace section

} // End of  CEGUI namespace section
