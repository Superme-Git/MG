/***********************************************************************
    filename:   FalToggleButton.cpp
    created:    Sat Jan 14 2006
    author:     Tomas Lindquist Olsen <tomas@famolsen.dk>
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
#include "FalToggleButton.h"
#include "CEGUIPropertyHelper.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "elements/CEGUIGroupButton.h"
#include "CEGUICentredRenderedString.h"


// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardToggleButton::TypeName[] = "Falagard/ToggleButton";

    FalagardToggleButton::FalagardToggleButton(const String& type) :
		 WindowRenderer(type),	  
		d_formattedRenderedString(NULL)
	
    {
    }

	 FalagardToggleButton::~FalagardToggleButton()
	 {
		 if(d_formattedRenderedString)
		 {
			 delete d_formattedRenderedString;
		 }
		
	 }

    String FalagardToggleButton::actualStateName(const String& name) const
    {
		//std::string str(name.c_str());
    	bool selected = PropertyHelper::stringToBool(d_window->getProperty("Selected"));
        return selected ? "Selected"+name : name;
    }

	void FalagardToggleButton::clone(const WindowRenderer* templateRenderer)
	{
		WindowRenderer::clone(templateRenderer);
		d_formattedRenderedString = NULL;//这东西都是动态创建的，设置成空就可以了.
	}

	void FalagardToggleButton::render()
	{
		ButtonBase* w = (ButtonBase*)d_window;
		const WidgetLookFeel& wlf = getLookNFeel();

		bool norm = false;
		String state;

		if (w->isDisabled())
		{
			state = "Disabled";
		}
		else if (w->isPushed())
		{
			state ="Pushed" ;
		}
//		else if (w->isHovering())
//		{
//			state = "Hover";
//		}
		else
		{
			state = "Normal";
			norm = true;
		}

		if (!norm && !wlf.isStateImageryPresent(state))
		{
			state = "Normal";
		}

		wlf.getStateImagery(actualStateName(state)).render(*w);

		if(d_window->getType().find("GroupButton")!=String::npos)
		{
			GroupButton* groupbtn = (GroupButton*)d_window;
			const Rect clipper(getTextRenderArea());

			if (NULL!=d_formattedRenderedString)
			{
				delete d_formattedRenderedString;
				d_formattedRenderedString=NULL;
			}
			
			if (!d_formattedRenderedString)
			{
				d_formattedRenderedString =new CentredRenderedString(d_window->getRenderedString());
				d_window->getRenderedString();
				d_formattedRenderedString->format(clipper.getSize());
				/*colour border(0xFF015387);
				if (groupbtn->isSelected())
				{
					border.setARGB(0xFF03568B);
				}

				d_formattedRenderedString->SetBorderInf(true,border);*/
			}
            
            if (g_bIsTextLoading) {
                d_window->getRenderedString();
                d_formattedRenderedString->format(clipper.getSize());
            }            
            
			if (d_formattedRenderedString)
			{

				Rect absarea(clipper);
				float textHeight = d_formattedRenderedString->getVerticalExtent();
				absarea.d_top += PixelAligned((absarea.getHeight() - textHeight) * 0.5f);
				/*ColourRect colorRect(0xFFBFF8FC,0xFFBFF8FC,0xFF0397D5,0xFF0397D5);
				if (groupbtn->isSelected())
				{
					colorRect=ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFF42C8FF,0xFF42C8FF);
				}*/

				if (groupbtn->isBorderEnable())
				{
					colour boderColour=groupbtn->GetStateBorderColour();
					d_formattedRenderedString->SetBorderInf(true,boderColour);
				}
				

				ColourRect colorRect(groupbtn->GetStateColour());

				colorRect.modulateAlpha(d_window->getEffectiveAlpha());

				// cache the text for rendering.
				d_formattedRenderedString->draw(d_window->getGeometryBuffer(),
					absarea.getPosition(),
					&colorRect, &clipper);
			}
		}
		
	}


	Rect FalagardToggleButton::getTextRenderArea(void) const
	{
		const WidgetLookFeel& wlf = getLookNFeel();

		String area_name("TextRenderArea");

		// if either of the scrollbars are visible, we might want to use a special rendering area

		if (wlf.isNamedAreaDefined(area_name))
		{
			return wlf.getNamedArea(area_name).getArea().getPixelRect(*d_window);
		}

		// default to plain WithFrameTextRenderArea

		return Rect(0.0f,0.0f,0.0f,0.0f);



	}
} // End of  CEGUI namespace section
