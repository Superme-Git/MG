/***********************************************************************
    filename:   FalButton.cpp
    created:    Wed Jun 22 2005
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
#include "FalLinkText.h"
#include "CEGUIFont.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUIImage.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardLinkText::TypeName[] = "Falagard/LinkText";

    FalagardLinkText::FalagardLinkText(const String& type) :
        WindowRenderer(type)
    {
    }

    void FalagardLinkText::render()
    {
        LinkText* w = (LinkText*)d_window;
		const WidgetLookFeel& wlf = getLookNFeel();
		Rect rect=wlf.getNamedArea("LinkTextRenderingArea").getArea().getPixelRect(*w);
		
		bool bEnable=!w->isDisabled();
		bool bMouseHover=w->isMouseOn();
		bool bPushed=w->isPushed();

		String text=w->getText();
		Font* pFont=w->getFont();
		if (!text.empty()&&pFont)
		{
			ColourRect colour(0xFFFFFF00);
			CEGUI::Point pt(rect.getPosition());
			if (!bEnable)
			{
				colour.setColours(0xFF7F7F7F);
			}else if (bPushed)
			{
				pt.d_x+=1.0f;
				pt.d_y+=1.0f;
				colour.setColours(0xFFA9A9A9);
			}else if (bMouseHover)
			{
				/*pt.d_x+=1.0f;
				pt.d_y+=1.0f;*/
				colour.setColours(0xFFFF0000);
			}
			pFont->drawText(w->getGeometryBuffer(),text,pt,&rect,colour,0.0f,1.0f,1.0f,true);
		}
    }

} // End of  CEGUI namespace section
