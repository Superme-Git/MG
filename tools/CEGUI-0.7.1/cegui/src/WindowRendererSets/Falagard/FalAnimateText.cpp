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
#include "FalAnimateText.h"
#include "CEGUIFont.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUILeftAlignedRenderedString.h"
#include "CEGUIRenderedStringWordWrapper.h"
// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardAnimateText::TypeName[] = "Falagard/AnimateText";

    FalagardAnimateText::FalagardAnimateText(const String& type) : FalagardStaticText(type)
    , m_lastDrawText("")
//    ,	d_formattedRenderedString(NULL)

    {
    }

	FalagardAnimateText::~FalagardAnimateText()
	{
//		if(d_formattedRenderedString)
//	 {
//		 delete d_formattedRenderedString;
//	 }

	}

    void FalagardAnimateText::render()
    {
        AnimateText* w = (AnimateText*)d_window;
        String text = w->GetDrawText();
        if (text != m_lastDrawText) {
            d_formatValid = false;
            m_lastDrawText = text;
        }
        FalagardStaticText::render();
//		const WidgetLookFeel& wlf = getLookNFeel();
//		Rect rect=wlf.getNamedArea("AnimateTextRenderingArea").getArea().getPixelRect(*w);
//		
//		
//		String text=w->GetDrawText();
//		Font* pFont=w->getFont();
//		if (!text.empty()&&pFont)
//		{
//			if (NULL!=d_formattedRenderedString)
//			{
//				delete d_formattedRenderedString;
//				d_formattedRenderedString=NULL;
//			}
//			
//			if (!d_formattedRenderedString)
//			{
//				d_formattedRenderedString =new RenderedStringWordWrapper
//					<LeftAlignedRenderedString>(d_window->getRenderedString());
//				d_window->getRenderedString();
//				d_formattedRenderedString->format(rect.getSize());
//			
//			}
//			if (d_formattedRenderedString)
//			{
//				ColourRect colorRect(0xFFFFFFFF);
//				colorRect.modulateAlpha(d_window->getEffectiveAlpha());
//				// cache the text for rendering.
//				d_formattedRenderedString->draw(d_window->getGeometryBuffer(),
//					rect.getPosition(),
//					&colorRect, &rect);
//			}
//			
//		}
    }

} // End of  CEGUI namespace section
