/***********************************************************************
    filename:   FalStaticImage.cpp
    created:    Tue Jul 5 2005
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
#include "FalStaticImage.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardStaticImage::TypeName[] = "Falagard/StaticImage";
    
    FalagardStaticImageProperties::Image    FalagardStaticImage::d_imageProperty;
	 FalagardStaticImageProperties::ImageSizeEnable    FalagardStaticImage::d_ImageSizeEnableProperty;

    FalagardStaticImage::FalagardStaticImage(const String& type) :
        FalagardStatic(type),
        d_image(0),
		d_ImageSizeEnable(true)
    {
        registerProperty(&d_imageProperty);
		registerProperty(&d_ImageSizeEnableProperty);
    }

    void FalagardStaticImage::render()
    {
        // base class rendering
        FalagardStatic::render();

        // render image if there is one
        if (d_image!=0)
        {
            // get WidgetLookFeel for the assigned look.
            const WidgetLookFeel& wlf = getLookNFeel();
            String imagery_name = (!d_frameEnabled && wlf.isStateImageryPresent("NoFrameImage")) ? "NoFrameImage" : "WithFrameImage";
			Rect renderrect;

			renderrect = Rect(wlf.getNamedArea("ImageArea").getArea().getPixelRect(*d_window));

			Rect Clipper(renderrect);

			float width = Clipper.getWidth() * d_window->GetWidthClip();

			Clipper.setWidth(width);

            wlf.getStateImagery(imagery_name).render(*d_window,renderrect,0,&Clipper);

        }
    }

    void FalagardStaticImage::setImage(const Image* img)
    {
        d_image = img;

#ifdef _EDITOR_
		if (d_image&&!d_ImageSizeEnable)
		{
			UVector2 wndSize(cegui_absdim(d_image->getWidth()),cegui_absdim(d_image->getHeight()));
			d_window->setMaxSize(wndSize);
			d_window->setMinSize(wndSize);
		}
#endif

        d_window->invalidate();
    }

	void FalagardStaticImage::SetImageSizeEnable( bool bEnable )
	{
		if (bEnable!=d_ImageSizeEnable)
		{
			d_ImageSizeEnable=bEnable;
			float frameWidth=16.0f;
			float frameHeight=13.0f;
			if (!d_frameEnabled)
			{
				frameWidth=0.0f;
				frameHeight=0.0f;
			}
			
			if (d_ImageSizeEnable)
			{
				UVector2 maxSize(cegui_reldim(1.0f),cegui_reldim(1.0f));
				UVector2 minSize(cegui_reldim(0.0f),cegui_reldim(0.0f));
				d_window->setMaxSize(maxSize);
				d_window->setMinSize(minSize);
			}else
			{
				if (d_image&&d_window)
				{
					UVector2 wndSize(cegui_absdim(d_image->getWidth()+frameWidth),cegui_absdim(d_image->getHeight()+frameHeight));
					d_window->setMaxSize(wndSize);
					d_window->setMinSize(wndSize);
				}
				
			}
			d_window->invalidate();
			
		}
		
	}

} // End of  CEGUI namespace section
