/***********************************************************************
filename: 	CEGUIProgressBarTwoValue.cpp
purpose:	Interface to base class for ProgressBarTwoValue widget
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
#include "elements/CEGUIProgressBarTwoValue.h"


// Start of CEGUI namespace section
namespace CEGUI
{
	const String ProgressBarTwoValue::WidgetTypeName("CEGUI/ProgressBarTwoValue");
	const String ProgressBarTwoValue::EventNamespace("ProgressBarTwoValue");
	const String ProgressBarTwoValue::EventProgressChanged("ProgressChanged");

	ProgressBarTwoValueProperties::CurrentProgress	ProgressBarTwoValue::d_currentProgressProperty;
	ProgressBarTwoValueProperties::StepSize			ProgressBarTwoValue::d_stepSizeProperty;
    ProgressBarTwoValueProperties::FrameEnable			ProgressBarTwoValue::d_FrameEnableProperty;
	/*************************************************************************
	Constructor for ProgressBar class
	*************************************************************************/
	ProgressBarTwoValue::ProgressBarTwoValue(const String& type, const String& name) :
	Window(type, name),
		d_progress(0),
		d_reverseprogress(0),
		d_step(0.01f),
		d_FrameEnable(false)
	{
		//addProgressBarProperties();
	}


	ProgressBarTwoValue::ProgressBarTwoValue(const String& type) :
		Window(type)
	{
	}
	const ProgressBarTwoValue& ProgressBarTwoValue::operator=(const ProgressBarTwoValue& t)
	{
		Window::operator=(t);
		d_progress = t.d_progress;
		d_reverseprogress = t.d_reverseprogress;
		d_step = t.d_step;
		d_FrameEnable = t.d_FrameEnable;
		return *this;
	}
	Window* ProgressBarTwoValue::clone(Window* wnd)
	{
		ProgressBarTwoValue* retWnd = (ProgressBarTwoValue*)wnd;
		if (retWnd == NULL)
			retWnd = new ProgressBarTwoValue(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == ProgressBarTwoValue::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

	/*************************************************************************
	Destructor for ProgressBar
	*************************************************************************/
	ProgressBarTwoValue::~ProgressBarTwoValue(void)
	{
	}


	/*************************************************************************
	set the current progress.	
	*************************************************************************/
	void ProgressBarTwoValue::setProgress(float progress)
	{
		// legal progress rangeis : 0.0f <= progress <= 1.0f
		progress = (progress < 0.0f) ? 0.0f : (progress > 1.0f) ? 1.0f : progress;

		if (progress != d_progress)
		{
			// update progress and fire off event.
			d_progress = progress;
			WindowEventArgs args(this);
			onProgressChanged(args);
			invalidate();
		}

	}
	
	/*************************************************************************
	set the reverse progress.	
	*************************************************************************/
	void ProgressBarTwoValue::setReverseProgress(float reverseprogress)
	{
		// legal progress rangeis : 0.0f <= progress <= 1.0f
		reverseprogress = (reverseprogress < 0.0f) ? 0.0f : (d_progress + reverseprogress > 1.0f) ? 1.0f - d_progress : reverseprogress;

		if (reverseprogress != d_reverseprogress)
		{
			// update progress and fire off event.
			d_reverseprogress = reverseprogress;
			invalidate();
		}

	}

	void  ProgressBarTwoValue::SetFrameEnable(bool bFrameEnable)
	{
		if (bFrameEnable!=d_FrameEnable)
		{
			d_FrameEnable=bFrameEnable;
			invalidate();
		}
		

	}

	void	ProgressBarTwoValue::addProgressBarProperties(void)
	{
		addProperty(&d_stepSizeProperty);
		addProperty(&d_currentProgressProperty);
		addProperty(&d_FrameEnableProperty);

	}

	void ProgressBarTwoValue::onProgressChanged(WindowEventArgs& e)
	{
		invalidate();

		fireEvent(EventProgressChanged, e, EventNamespace);
	}

} // End of  CEGUI namespace section
