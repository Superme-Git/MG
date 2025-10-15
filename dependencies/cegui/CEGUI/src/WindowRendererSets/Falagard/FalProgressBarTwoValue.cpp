/***********************************************************************
filename:   FalProgressBarTwoValue.cpp
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
#include "FalProgressBarTwoValue.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "elements/CEGUIProgressBarTwoValue.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const utf8 FalagardProgressBarTwoValue::TypeName[] = "Falagard/ProgressBarTwoValue";

	FalagardProgressBarTwoValue::FalagardProgressBarTwoValue(const String& type) :
	WindowRenderer(type)
	{
		
	}

	void FalagardProgressBarTwoValue::render()
	{
		const StateImagery* imagery;
		// get WidgetLookFeel for the assigned look.
		const WidgetLookFeel& wlf = getLookNFeel();
		// try and get imagery for our current state
		// imagery = &wlf.getStateImagery(d_window->isDisabled() ? "Disabled" : "Enabled");
		// peform the rendering operation.
		//imagery->render(*d_window);

		ProgressBarTwoValue* w = (ProgressBarTwoValue*)d_window;
		// get imagery for actual progress rendering	

		if (w->isFrameEnable())
		{
			imagery = &wlf.getStateImagery("Frame");

			imagery->render(*w);
		}

		//imagery = &wlf.getStateImagery("EnabledRedProgress");
		// get target rect for this imagery
		String AreaName("");
		if (w->isFrameEnable())
		{
			AreaName="ProgressAreaFrameEnable";
		}else
		{
			AreaName="ProgressAreaFrameDisable";
		}

		String imageryName("");
		if (w->isFrameEnable())
		{
			imageryName="EnabledRedProgress_WithFrame";
		}else
		{
			imageryName="EnabledRedProgress_NoFrame";
		}

		imagery=&wlf.getStateImagery(imageryName);
		

		Rect progressRect(wlf.getNamedArea(AreaName).getArea().getPixelRect(*d_window));
		// calculate a clipper according to the current progress.
		Rect progressClipper(progressRect);
		float width = progressClipper.getWidth() * w->getProgress();
		progressClipper.setWidth(width);

		// peform the rendering operation.
	
		imagery->render(*d_window, progressRect, 0, &progressClipper);
		
		float reversevalue = w->getReverseProgress();
		if(reversevalue > 0.00001f)
		{
			// calculate a clipper according to the reverse progress.
			Rect reverseProgressClipper(progressRect);
			float reversewidth = reverseProgressClipper.getWidth() * reversevalue;
			reverseProgressClipper.d_left = reverseProgressClipper.d_right - reversewidth;	

			Rect middleRect(progressRect);
			middleRect.d_left = progressClipper.d_right;
			middleRect.d_right = reverseProgressClipper.d_left;
			
			imagery = &wlf.getStateImagery("DisabledRedProgress");
			imagery->render(*d_window, progressRect, 0, &middleRect);

			if (w->isFrameEnable())
			{
				imageryName="EnabledWhiteProgress";
			}else
			{
				imageryName="EnabledWhiteProgress_NoFrame";
			}
			imagery = &wlf.getStateImagery(imageryName);
			imagery->render(*d_window, progressRect, 0, &reverseProgressClipper);
		}
		
		
		imagery = &wlf.getStateImagery("LabelText");
		imagery->render(*d_window, progressRect, 0, &progressRect);
	}

} // End of  CEGUI namespace section
