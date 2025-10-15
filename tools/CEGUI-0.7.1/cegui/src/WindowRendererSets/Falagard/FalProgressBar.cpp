/***********************************************************************
    filename:   FalProgressBar.cpp
    created:    Sat Jul 2 2005
    author:     Paul D Turner <paul@cegui.org.uk>
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
#include "FalProgressBar.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "elements/CEGUIProgressBar.h"
#include "CEGUIGeometryBuffer.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardProgressBar::TypeName[] = "Falagard/ProgressBar";
    FalagardProgressBarProperties::VerticalProgress FalagardProgressBar::d_verticalProperty;
    FalagardProgressBarProperties::ReversedProgress FalagardProgressBar::d_reversedProperty;


    FalagardProgressBar::FalagardProgressBar(const String& type) :
        WindowRenderer(type, "ProgressBar"),
        d_vertical(false),
        d_reversed(false)
    {
        registerProperty(&d_verticalProperty);
        registerProperty(&d_reversedProperty);
    }

    void FalagardProgressBar::render()
    {
        const StateImagery* imagery;

        // get WidgetLookFeel for the assigned look.
        const WidgetLookFeel& wlf = getLookNFeel();
        // try and get imagery for our current state
		
		
        //imagery = &wlf.getStateImagery(d_window->isDisabled() ? "Disabled" : "Enabled");
        //// peform the rendering operation.
        //imagery->render(*d_window);
		
		 ProgressBar* w = (ProgressBar*)d_window;

		 if (w->isFrameEnable())
		 {
			  imagery = &wlf.getStateImagery("FrameBack");
			 imagery->render(*w);
		 }
		 
		

        // get imagery for actual progress rendering
		
// 		 if(d_window->isDisabled() && w->getBarType() == ProgressBar::RedBar)
// 		 {
// 			imagery = &wlf.getStateImagery("DisabledRedProgress");
// 		 }
// 		 else if(d_window->isDisabled() == false && w->getBarType() == ProgressBar::RedBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("EnabledRedProgress");
// 		 }
// 
// 		 else if(d_window->isDisabled() && w->getBarType() == ProgressBar::GreenBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("DisabledGreenProgress");
// 		 }
// 		 else if(d_window->isDisabled() == false && w->getBarType() == ProgressBar::GreenBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("EnabledGreenProgress");
// 		 }
// 
// 		 else if(d_window->isDisabled() && w->getBarType() == ProgressBar::BuleBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("DisabledBuleProgress");
// 		 }
// 		 else if(d_window->isDisabled() == false && w->getBarType() == ProgressBar::BuleBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("EnabledBuleProgress");
// 		 }
// 		 else if(d_window->isDisabled() && w->getBarType() == ProgressBar::YellowBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("DisabledYellowProgress");
// 		 }
// 		 else if(d_window->isDisabled() == false && w->getBarType() == ProgressBar::YellowBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("EnabledYellowProgress");
// 		 }
// 		 else if(d_window->isDisabled() && w->getBarType() == ProgressBar::ColorfulBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("EnabledColorfulProgress");
// 		 }
// 		 else if(d_window->isDisabled() == false && w->getBarType() == ProgressBar::ColorfulBar)
// 		 {
// 			 imagery = &wlf.getStateImagery("EnabledColorfulProgress");
// 		 }
// 		 if(d_window->isDisabled() )
// 		 {
// 			 imagery = &wlf.getStateImagery("Disabled");
// 		 }else
		 {
			  imagery = &wlf.getStateImagery("Enabled");
		 }
		 
       
        // get target rect for this imagery
       
		String AreaName("");
		//if (w->isFrameEnable())
		{
			AreaName="ProgressAreaFrameEnable";
		}
		//else
		{
			//AreaName="ProgressAreaFrameDisable";
		}
		
		 Rect progressRect(wlf.getNamedArea(AreaName).getArea().getPixelRect(*d_window));

        // calculate a clipper according to the current progress.
        Rect progressClipper(progressRect);

       
        if (d_vertical)
        {
            float height = progressClipper.getHeight() * w->getProgress();

            if (d_reversed)
            {
                progressClipper.setHeight(height);
            }
            else
            {
                progressClipper.d_top = progressClipper.d_bottom - height;
            }
        }
        else
        {
            float width = progressClipper.getWidth() * w->getProgress();

            if (d_reversed)
            {
                progressClipper.d_left = progressClipper.d_right - width;
            }
            else
            {
                progressClipper.setWidth(width);
            }
        }

        // peform the rendering operation.
        imagery->render(*d_window, progressRect, 0, &progressClipper);
		//imagery->render(*d_window, progressClipper, 0, &progressClipper);	// by liugeng. don't clip, scale it as tile		
		
		imagery = &wlf.getStateImagery("LabelText");
		imagery->render(*d_window, progressRect, 0, &progressRect);
    }

    bool FalagardProgressBar::isVertical() const
    {
        return d_vertical;
    }

    bool FalagardProgressBar::isReversed() const
    {
        return d_reversed;
    }

    void FalagardProgressBar::setVertical(bool setting)
    {
        d_vertical = setting;
    }

    void FalagardProgressBar::setReversed(bool setting)
    {
        d_reversed = setting;
    }

} // End of  CEGUI namespace section
