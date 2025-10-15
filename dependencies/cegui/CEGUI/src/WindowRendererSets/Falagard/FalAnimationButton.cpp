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
#include "FalAnimationButton.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUIImage.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIPropertyHelper.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardAnimationButton::TypeName[] = "Falagard/AnimationButton";

    FalagardAnimationButton::FalagardAnimationButton(const String& type) :
        WindowRenderer(type)
    {
    }

    void FalagardAnimationButton::render()
    {
        AnimationButton* w = (AnimationButton*)d_window;
		const WidgetLookFeel& wlf = getLookNFeel();
		Rect rect=wlf.getNamedArea("AniBtnRenderingArea").getArea().getPixelRect(*w);
		Rect imageRect(rect);
		
		if (w->isMouseOn())
		{
			
			
			const Image* pNormalImage=w->GetNormalImage();
			if (pNormalImage)
			{
				imageRect.setSize(pNormalImage->getSize());
			    pNormalImage->draw(w->getGeometryBuffer(),imageRect,&imageRect);
			}
		}else
		{
			int curFrame=w->GetCurFrame();
			String ImageSetName(w->GetImageSetName());
			String ImageName(w->GetImageName());

			if (ImagesetManager::getSingleton().isDefined(ImageSetName))
			{
				const Imageset* pSet=&ImagesetManager::getSingleton().get(ImageSetName);
				String strCurImageName(ImageName);
				strCurImageName+=PropertyHelper::intToString(curFrame);
				if (pSet->isImageDefined(strCurImageName))
				{
					const Image* pCurImage=&pSet->getImage(strCurImageName);
					imageRect.setSize(pCurImage->getSize());
					pCurImage->draw(w->getGeometryBuffer(),imageRect,&imageRect);
				}
			}

		}

    }

} // End of  CEGUI namespace section
