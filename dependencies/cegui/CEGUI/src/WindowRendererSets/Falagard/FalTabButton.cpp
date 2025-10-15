/***********************************************************************
    filename:   FalTabTabButton.cpp
    created:    Fri Jul 8 2005
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
#include "FalTabButton.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "elements/CEGUITabButton.h"
#include "elements/CEGUITabControl.h"
#include "CEGUICentredRenderedString.h"
#include "CEGUIGeometryBuffer.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardTabButton::TypeName[] = "Falagard/TabButton";

    FalagardTabButton::FalagardTabButton(const String& type) :
        WindowRenderer(type, "TabButton"),
			d_formattedRenderedString(0)
    {
    }

	FalagardTabButton::~FalagardTabButton()
	{
		if(d_formattedRenderedString)
		{
			 delete d_formattedRenderedString;
		}

	}

    void FalagardTabButton::render()
    {
        TabButton* w = (TabButton*)d_window;
        // get WidgetLookFeel for the assigned look.
        const WidgetLookFeel& wlf = getLookNFeel();

		TabControl* tc = static_cast<TabControl*>(w->getParent()->getParent());

        String state;
		String prefix;
		if(tc->getTabPanePosition() == TabControl::Top)
		{
			prefix = "Top";
		}
		else if(tc->getTabPanePosition() == TabControl::Bottom)
		{
			prefix = "Bottom";
		}
		else if(tc->getTabPanePosition() == TabControl::Left)
		{
			prefix = "Left";
		}
		

		if (w->isDisabled())
		    state = "Disabled";
		else if (w->isSelected())
		    state = "Selected";
		else if (w->isPushed())
		    state = "Pushed";
		else if (w->isHovering())
		    state = "Hover";
		else
		    state = "Normal";

        if (!wlf.isStateImageryPresent(prefix + state))
        {
            state = "Normal";
			if (!wlf.isStateImageryPresent(prefix + state))
				prefix = "";
        }

        wlf.getStateImagery(prefix + state).render(*w);
		
		const Rect clipper(getTextRenderArea());
		if (!d_formattedRenderedString)
		{
			d_formattedRenderedString =new CentredRenderedString(d_window->getRenderedString());
			d_window->getRenderedString();
			d_formattedRenderedString->format(clipper.getSize());
			colour border(0xFF015885);
			/*if (w->isSelected())
			{
				border.setARGB(0xFF03568B);
			}*/
			
			//d_formattedRenderedString->SetBorderInf(true,border);
		}
        
        if (g_bIsTextLoading) {
            d_window->getRenderedString();
            d_formattedRenderedString->format(clipper.getSize());
			colour border(0xFF015885);
            /*if (w->isSelected())
            {
                border.setARGB(0xFF03568B);
            }*/
            
            //d_formattedRenderedString->SetBorderInf(true,border);
        }
        
		if (d_formattedRenderedString)
		{
			if (w->isSelected())
			{
				d_formattedRenderedString->SetBorderInf(true, colour(0xFF015885));
			}
			else
			{
				d_formattedRenderedString->SetBorderInf(false, colour(0xFFFFFFFF));
			}

			
			
			Rect absarea(clipper);
			float textHeight = d_formattedRenderedString->getVerticalExtent();
			absarea.d_top += PixelAligned((absarea.getHeight() - textHeight) * 0.5f)+2.0f;
			ColourRect colorRect(0xFF693F00, 0xFF693F00, 0xFF693F00, 0xFF693F00); 
			if (w->isSelected())
			{
				colorRect = ColourRect(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
			}
			
			colorRect.modulateAlpha(d_window->getEffectiveAlpha());
			// cache the text for rendering.

			d_formattedRenderedString->draw(d_window->getGeometryBuffer(),
				absarea.getPosition(),
				&colorRect, &clipper);
		}
    }
	Rect FalagardTabButton::getTextRenderArea(void) const
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
