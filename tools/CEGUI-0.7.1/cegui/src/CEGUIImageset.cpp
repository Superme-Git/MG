/***********************************************************************
	filename: 	CEGUIImageset.cpp
	created:	21/2/2004
	author:		Paul D Turner
	
	purpose:	Implements the Imageset class
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
#include "CEGUIImageset.h"
#include "CEGUIExceptions.h"
#include "CEGUITexture.h"
#include "CEGUIRenderer.h"
#include "CEGUISystem.h"
#include "CEGUIImageset_xmlHandler.h"
#include "CEGUILogger.h"
#include "CEGUIDataContainer.h"
#include "CEGUIXMLParser.h"
#include "CEGUIXMLSerializer.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIGeometryBuffer.h"
#include "CEGUIVertex.h"
#ifndef FORCEGUIEDITOR
#include "CEGUICocos2DTexture.h"
#endif
#include <iostream>
#include <cmath>
#ifndef M_PI
#define M_PI        3.14159265358979323846264338327950288   /* pi             */
#endif

#if (defined WIN7_32) && (defined _DEBUG)
#include <CEGUIPfsResourceProvider.h>
#endif

// Start of CEGUI namespace section
namespace CEGUI
{

/*************************************************************************
	Definition of static /const data for Imageset (and sub-classes)
*************************************************************************/
// Declared in Imageset
String Imageset::d_defaultResourceGroup;


/*************************************************************************
	constructor
*************************************************************************/
Imageset::Imageset(const String& name, Texture& texture) :
	d_name(name),
	d_texture(&texture),
	d_textureFilename(""),
	d_AutoLoadTexture(true),
	d_SynLoadTexture(true),
    d_AutoReleaseTime(0.0f),
    d_LastRenderToNowFrame(0),
    d_AutoReleaseFrameNum(300)
{
	if (!d_texture)
	{
		throw NullObjectException("Imageset::Imageset - Texture object supplied for Imageset creation must be valid.");
	}
	// defaults for scaling options
	d_autoScale = false;
	setNativeResolution(Size(DefaultNativeHorzRes, DefaultNativeVertRes));
}


Imageset::Imageset(const String& name, const String& filename, const String& resourceGroup) :
    d_name(name),
	d_texture(0),
	d_AutoLoadTexture(true),
	d_SynLoadTexture(true),
    d_AutoReleaseTime(0.0f),
    d_LastRenderToNowFrame(0),
    d_AutoReleaseFrameNum(300)
{
    // try to load the image file using the renderer
    

    // store texture filename
    d_textureFilename = filename;
    // TODO: Should we store the resource group too?

    // initialse the auto-scaling for this Imageset
    d_autoScale = false;
	setNativeResolution(Size(DefaultNativeHorzRes, DefaultNativeVertRes));

    // define the default image for this Imageset
   /* defineImage(
        "full_image",
        Rect(0, 0,
             d_texture->getOriginalDataSize().d_width,
             d_texture->getOriginalDataSize().d_height),
        Point(0, 0)
    );*/
}


/*************************************************************************
	destructor
*************************************************************************/
Imageset::~Imageset(void)
{
	unload();
}

void Imageset::clearTexture()
{
	d_texture = NULL;
}

/*************************************************************************
	Set texture for use by this imageset object
*************************************************************************/
void Imageset::setTexture(Texture* texture)
{
	if (!texture)
	{
		throw NullObjectException("Imageset::setTexture - Texture object supplied for Imageset creation must be valid.");
	}

	d_texture = texture;
}


/*************************************************************************
	return the Image object for the named image
*************************************************************************/
const Image& Imageset::getImage(const String& name) const
{
	ImageRegistry::const_iterator	pos = d_images.find(name);
	
	//std::string n(name.c_str());
	if (pos == d_images.end())
	{
		CEGUI::Logger::getSingleton().logEvent("Imageset::getImage - The Image named '" + name + "' could not be found in Imageset '" + d_name + "'.", CEGUI::Warnings);
		if (d_name != "back" || name != "black")
		{
			return ImagesetManager::getSingleton().get("back").getImage("blank");
		}
		throw	UnknownObjectException("Imageset::getImage - The Image named '" + name + "' could not be found in Imageset '" + d_name + "'.");
	}

	return pos->second;
}


/*************************************************************************
	defines a new Image.
*************************************************************************/
void Imageset::defineImage(const String& name, const Rect& image_rect, const Point& render_offset, const float rotation)
{
	if (isImageDefined(name))
	{
		throw AlreadyExistsException("Imageset::defineImage - An image with the name '" + name + "' already exists in Imageset '" + d_name + "'.");
	}

	// get scaling factors
	float hscale = d_autoScale ? d_horzScaling : 1.0f;
	float vscale = d_autoScale ? d_vertScaling : 1.0f;

	// add the Image definition
	d_images[name] = Image(this, name, image_rect, render_offset, hscale, vscale, rotation);

	CEGUI_LOGINSANE("Image '" + name + "' has been defined for Imageset '" + d_name + "'.")
}


/*************************************************************************
    Queues an area of the associated Texture the be drawn on the screen.
    Low-level routine not normally used!
*************************************************************************/
void Imageset::draw(GeometryBuffer* buffer, const Rect& source_rect,
    const Rect& dest_rect, const Rect* clip_rect,const ColourRect& colours,
    QuadSplitMode quad_split_mode, const float rotation) const
{
#ifndef FORCEGUIEDITOR
	if (NULL == d_texture&&!d_textureFilename.empty())
    {
        d_texture=&System::getSingleton().getRenderer()->createTexture(d_textureFilename,d_defaultResourceGroup , dest_rect.d_left,this->d_SynLoadTexture);
        if(d_texture)
        {
            Cocos2DTexture* pcctexture = dynamic_cast<Cocos2DTexture*>(d_texture);
            if(pcctexture)
                pcctexture->setImageSet(this);

			d_LastRenderToNowFrame = 0;
        }
		//d_texture =;
    }

	if (d_texture==NULL)
	{
		return;
	}	
    
	// get the rect area that we will actually draw to (i.e. perform clipping)
    Rect final_rect(clip_rect ? dest_rect.getIntersection(*clip_rect) : dest_rect );

    // check if rect was totally clipped
    if ((final_rect.getWidth() == 0) || (final_rect.getHeight() == 0))
        return;

    // Fix bug #45
    // Obtain correct scale values from the texture
    
    float tex_Scale=1.0f;
    
#if defined(ANDROID)
    if(!d_textureFilename.empty())
        tex_Scale=System::getSingleton().GetUIImageScale();
#endif
    
#ifdef _DEBUG
    tex_Scale=1.0f;
#endif
    const float x_scale = tex_Scale*d_texture->getTexelScaling().d_x;
    const float y_scale = tex_Scale*d_texture->getTexelScaling().d_y;

    float tex_per_pix_x = source_rect.getWidth() / dest_rect.getWidth();
    float tex_per_pix_y = source_rect.getHeight() / dest_rect.getHeight();

    // calculate final, clipped, texture co-ordinates
    Rect  tex_rect((source_rect.d_left + ((final_rect.d_left - dest_rect.d_left) * tex_per_pix_x)) * x_scale,
        (source_rect.d_top + ((final_rect.d_top - dest_rect.d_top) * tex_per_pix_y)) * y_scale,
        (source_rect.d_right + ((final_rect.d_right - dest_rect.d_right) * tex_per_pix_x)) * x_scale,
        (source_rect.d_bottom + ((final_rect.d_bottom - dest_rect.d_bottom) * tex_per_pix_y)) * y_scale);

	CEGUI::Rect tempRect = final_rect;

    Vertex vbuffer[6];

    if (rotation < 0.5 && rotation > -0.5)
    {
        if ((abs(final_rect.d_top) - abs(int(final_rect.d_top)) >= 0.4) &&
            (abs(final_rect.d_bottom) - abs(int(final_rect.d_bottom)) >= 0.4))
        {
            int result1 = PixelAligned(final_rect.d_bottom) - PixelAligned(final_rect.d_top);
            int result2 = PixelAligned(final_rect.d_bottom-0.1) - PixelAligned(final_rect.d_top-0.1);
            int result3 = PixelAligned(final_rect.d_bottom+0.1) - PixelAligned(final_rect.d_top+0.1);
            if ((result2 == result3) && (result1 != result2))
            {
                final_rect.d_left	= PixelAligned(final_rect.d_left);
                final_rect.d_right	= PixelAligned(final_rect.d_right);
                final_rect.d_top	= PixelAligned(final_rect.d_top-0.1);
                final_rect.d_bottom	= PixelAligned(final_rect.d_bottom-0.1);
            }
            else
            {
                final_rect.d_left	= PixelAligned(final_rect.d_left);
                final_rect.d_right	= PixelAligned(final_rect.d_right);
                final_rect.d_top	= PixelAligned(final_rect.d_top);
                final_rect.d_bottom	= PixelAligned(final_rect.d_bottom);
            }
        }
        else
        {
            final_rect.d_left	= PixelAligned(final_rect.d_left);
            final_rect.d_right	= PixelAligned(final_rect.d_right);
            final_rect.d_top	= PixelAligned(final_rect.d_top);
            final_rect.d_bottom	= PixelAligned(final_rect.d_bottom);
        }
        
        // vertex 0
        vbuffer[0].position   = Vector3(final_rect.d_left, final_rect.d_top, 0.0f);
        vbuffer[0].colour_val = colours.d_top_left;
        vbuffer[0].tex_coords = Vector2(tex_rect.d_left, tex_rect.d_top);

        // vertex 1
        vbuffer[1].position   = Vector3(final_rect.d_left, final_rect.d_bottom, 0.0f);
        vbuffer[1].colour_val = colours.d_bottom_left;
        vbuffer[1].tex_coords = Vector2(tex_rect.d_left, tex_rect.d_bottom);

        // vertex 2
        vbuffer[2].position.d_x   = final_rect.d_right;
        vbuffer[2].position.d_z   = 0.0f;
        vbuffer[2].colour_val     = colours.d_bottom_right;
        vbuffer[2].tex_coords.d_x = tex_rect.d_right;

        // top-left to bottom-right diagonal
        if (quad_split_mode == TopLeftToBottomRight)
        {
            vbuffer[2].position.d_y   = final_rect.d_bottom;
            vbuffer[2].tex_coords.d_y = tex_rect.d_bottom;
        }
        // bottom-left to top-right diagonal
        else
        {
            vbuffer[2].position.d_y   = final_rect.d_top;
            vbuffer[2].tex_coords.d_y = tex_rect.d_top;
        }

        // vertex 3
        vbuffer[3].position   = Vector3(final_rect.d_right, final_rect.d_top, 0.0f);
        vbuffer[3].colour_val = colours.d_top_right;
        vbuffer[3].tex_coords = Vector2(tex_rect.d_right, tex_rect.d_top);

        // vertex 4
        vbuffer[4].position.d_x   = final_rect.d_left;
        vbuffer[4].position.d_z   = 0.0f;
        vbuffer[4].colour_val     = colours.d_top_left;
        vbuffer[4].tex_coords.d_x = tex_rect.d_left;

        // top-left to bottom-right diagonal
        if (quad_split_mode == TopLeftToBottomRight)
        {
            vbuffer[4].position.d_y   = final_rect.d_top;
            vbuffer[4].tex_coords.d_y = tex_rect.d_top;
        }
        // bottom-left to top-right diagonal
        else
        {
            vbuffer[4].position.d_y   = final_rect.d_bottom;
            vbuffer[4].tex_coords.d_y = tex_rect.d_bottom;
        }

        // vertex 5
        vbuffer[5].position = Vector3(final_rect.d_right, final_rect.d_bottom, 0.0f);
        vbuffer[5].colour_val= colours.d_bottom_right;
        vbuffer[5].tex_coords = Vector2(tex_rect.d_right, tex_rect.d_bottom);
    }
    else    //with rotation
    {
		
        float arc = rotation *  M_PI / 180;
        Vector2 vec[4], vecRotated[4];
        vec[0] = Vector2(final_rect.d_left, final_rect.d_top) - Vector2((final_rect.d_left + final_rect.d_right) / 2, (final_rect.d_top + final_rect.d_bottom) / 2);
        vec[1] = Vector2(final_rect.d_right, final_rect.d_top) - Vector2((final_rect.d_left + final_rect.d_right) / 2, (final_rect.d_top + final_rect.d_bottom) / 2);
        vec[2] = Vector2(final_rect.d_right, final_rect.d_bottom) - Vector2((final_rect.d_left + final_rect.d_right) / 2, (final_rect.d_top + final_rect.d_bottom) / 2);
        vec[3] = Vector2(final_rect.d_left, final_rect.d_bottom) - Vector2((final_rect.d_left + final_rect.d_right) / 2, (final_rect.d_top + final_rect.d_bottom) / 2);
        vecRotated[0] = Vector2(vec[0].d_x * cos(arc) + vec[0].d_y * sin(arc), - vec[0].d_x * sin(arc) + vec[0].d_y * cos(arc));
        vecRotated[1] = Vector2(vec[1].d_x * cos(arc) + vec[1].d_y * sin(arc), - vec[1].d_x * sin(arc) + vec[1].d_y * cos(arc));
        vecRotated[2] = Vector2(vec[2].d_x * cos(arc) + vec[2].d_y * sin(arc), - vec[2].d_x * sin(arc) + vec[2].d_y * cos(arc));
        vecRotated[3] = Vector2(vec[3].d_x * cos(arc) + vec[3].d_y * sin(arc), - vec[3].d_x * sin(arc) + vec[3].d_y * cos(arc));
        vec[0] = vecRotated[0] + Vector2((final_rect.d_left + final_rect.d_right) / 2, (final_rect.d_top + final_rect.d_bottom) / 2);
        vec[1] = vecRotated[1] + Vector2((final_rect.d_left + final_rect.d_right) / 2, (final_rect.d_top + final_rect.d_bottom) / 2);
        vec[2] = vecRotated[2] + Vector2((final_rect.d_left + final_rect.d_right) / 2, (final_rect.d_top + final_rect.d_bottom) / 2);
        vec[3] = vecRotated[3] + Vector2((final_rect.d_left + final_rect.d_right) / 2, (final_rect.d_top + final_rect.d_bottom) / 2);
        vec[0].d_x = PixelAligned(vec[0].d_x);
        vec[0].d_y = PixelAligned(vec[0].d_y);
        vec[1].d_x = PixelAligned(vec[1].d_x);
        vec[1].d_y = PixelAligned(vec[1].d_y);
        vec[2].d_x = PixelAligned(vec[2].d_x);
        vec[2].d_y = PixelAligned(vec[2].d_y);
        vec[3].d_x = PixelAligned(vec[3].d_x);
        vec[3].d_y = PixelAligned(vec[3].d_y);
        
        // vertex 0
        vbuffer[0].position   = Vector3(vec[0].d_x, vec[0].d_y, 0.0f);
        vbuffer[0].colour_val = colours.d_top_left;
        vbuffer[0].tex_coords = Vector2(tex_rect.d_left, tex_rect.d_top);
        
        // vertex 1
        vbuffer[1].position   = Vector3(vec[3].d_x, vec[3].d_y, 0.0f);
        vbuffer[1].colour_val = colours.d_bottom_left;
        vbuffer[1].tex_coords = Vector2(tex_rect.d_left, tex_rect.d_bottom);
        
        // vertex 2
        vbuffer[2].colour_val     = colours.d_bottom_right;
        vbuffer[2].tex_coords.d_x = tex_rect.d_right;
        
        // top-left to bottom-right diagonal
        if (quad_split_mode == TopLeftToBottomRight)
        {
            vbuffer[2].position = Vector3(vec[2].d_x, vec[2].d_y, 0.0f);
            vbuffer[2].tex_coords.d_y = tex_rect.d_bottom;
        }
        // bottom-left to top-right diagonal
        else
        {
            vbuffer[2].position = Vector3(vec[1].d_x, vec[1].d_y, 0.0f);
            vbuffer[2].tex_coords.d_y = tex_rect.d_top;
        }
        
        // vertex 3
        vbuffer[3].position   = Vector3(vec[1].d_x, vec[1].d_y, 0.0f);
        vbuffer[3].colour_val = colours.d_top_right;
        vbuffer[3].tex_coords = Vector2(tex_rect.d_right, tex_rect.d_top);
        
        // vertex 4
        vbuffer[4].colour_val     = colours.d_top_left;
        vbuffer[4].tex_coords.d_x = tex_rect.d_left;
        
        // top-left to bottom-right diagonal
        if (quad_split_mode == TopLeftToBottomRight)
        {
            vbuffer[4].position = Vector3(vec[0].d_x, vec[0].d_y, 0.0f);
            vbuffer[4].tex_coords.d_y = tex_rect.d_top;
        }
        // bottom-left to top-right diagonal
        else
        {
            vbuffer[4].position = Vector3(vec[3].d_x, vec[3].d_y, 0.0f);
            vbuffer[4].tex_coords.d_y = tex_rect.d_bottom;
        }
        
        // vertex 5
        vbuffer[5].position = Vector3(vec[2].d_x, vec[2].d_y, 0.0f);
        vbuffer[5].colour_val= colours.d_bottom_right;
        vbuffer[5].tex_coords = Vector2(tex_rect.d_right, tex_rect.d_bottom);
    }

    // TODO: Remove cast when GeometryBuffer gets it's APIs fixed!
    buffer->setActiveTexture((Texture*)d_texture);
    buffer->appendGeometry(vbuffer, 6);
#else
	// get the rect area that we will actually draw to (i.e. perform clipping)
	Rect final_rect(clip_rect ? dest_rect.getIntersection(*clip_rect) : dest_rect);

	// check if rect was totally clipped
	if ((final_rect.getWidth() == 0) || (final_rect.getHeight() == 0))
		return;

	// Fix bug #45
	// Obtain correct scale values from the texture
	const float x_scale = d_texture->getTexelScaling().d_x;
	const float y_scale = d_texture->getTexelScaling().d_y;

	float tex_per_pix_x = source_rect.getWidth() / dest_rect.getWidth();
	float tex_per_pix_y = source_rect.getHeight() / dest_rect.getHeight();

	// calculate final, clipped, texture co-ordinates
	Rect  tex_rect((source_rect.d_left + ((final_rect.d_left - dest_rect.d_left) * tex_per_pix_x)) * x_scale,
		(source_rect.d_top + ((final_rect.d_top - dest_rect.d_top) * tex_per_pix_y)) * y_scale,
		(source_rect.d_right + ((final_rect.d_right - dest_rect.d_right) * tex_per_pix_x)) * x_scale,
		(source_rect.d_bottom + ((final_rect.d_bottom - dest_rect.d_bottom) * tex_per_pix_y)) * y_scale);

	final_rect.d_left = PixelAligned(final_rect.d_left);
	final_rect.d_right = PixelAligned(final_rect.d_right);
	final_rect.d_top = PixelAligned(final_rect.d_top);
	final_rect.d_bottom = PixelAligned(final_rect.d_bottom);

	Vertex vbuffer[6];

	// vertex 0
	vbuffer[0].position = Vector3(final_rect.d_left, final_rect.d_top, 0.0f);
	vbuffer[0].colour_val = colours.d_top_left;
	vbuffer[0].tex_coords = Vector2(tex_rect.d_left, tex_rect.d_top);

	// vertex 1
	vbuffer[1].position = Vector3(final_rect.d_left, final_rect.d_bottom, 0.0f);
	vbuffer[1].colour_val = colours.d_bottom_left;
	vbuffer[1].tex_coords = Vector2(tex_rect.d_left, tex_rect.d_bottom);

	// vertex 2
	vbuffer[2].position.d_x = final_rect.d_right;
	vbuffer[2].position.d_z = 0.0f;
	vbuffer[2].colour_val = colours.d_bottom_right;
	vbuffer[2].tex_coords.d_x = tex_rect.d_right;

	// top-left to bottom-right diagonal
	if (quad_split_mode == TopLeftToBottomRight)
	{
		vbuffer[2].position.d_y = final_rect.d_bottom;
		vbuffer[2].tex_coords.d_y = tex_rect.d_bottom;
	}
	// bottom-left to top-right diagonal
	else
	{
		vbuffer[2].position.d_y = final_rect.d_top;
		vbuffer[2].tex_coords.d_y = tex_rect.d_top;
	}

	// vertex 3
	vbuffer[3].position = Vector3(final_rect.d_right, final_rect.d_top, 0.0f);
	vbuffer[3].colour_val = colours.d_top_right;
	vbuffer[3].tex_coords = Vector2(tex_rect.d_right, tex_rect.d_top);

	// vertex 4
	vbuffer[4].position.d_x = final_rect.d_left;
	vbuffer[4].position.d_z = 0.0f;
	vbuffer[4].colour_val = colours.d_top_left;
	vbuffer[4].tex_coords.d_x = tex_rect.d_left;

	// top-left to bottom-right diagonal
	if (quad_split_mode == TopLeftToBottomRight)
	{
		vbuffer[4].position.d_y = final_rect.d_top;
		vbuffer[4].tex_coords.d_y = tex_rect.d_top;
	}
	// bottom-left to top-right diagonal
	else
	{
		vbuffer[4].position.d_y = final_rect.d_bottom;
		vbuffer[4].tex_coords.d_y = tex_rect.d_bottom;
	}

	// vertex 5
	vbuffer[5].position = Vector3(final_rect.d_right, final_rect.d_bottom, 0.0f);
	vbuffer[5].colour_val = colours.d_bottom_right;
	vbuffer[5].tex_coords = Vector2(tex_rect.d_right, tex_rect.d_bottom);

	// TODO: Remove cast when GeometryBuffer gets it's APIs fixed!
	buffer->setActiveTexture((Texture*)d_texture);
	buffer->appendGeometry(vbuffer, 6);
#endif
}

/*************************************************************************
	Unload all data, leaving Imageset in a clean (but unusable) state
*************************************************************************/
void Imageset::unload(void)
{
	undefineAllImages();

	// cleanup texture
	if (d_texture!=NULL)
	{
		System::getSingleton().getRenderer()->destroyTexture(d_texture);
	}
	
	
	d_texture = 0;
}


/*************************************************************************
	Sets the scaling factor for all Images that are a part of this Imageset.
*************************************************************************/
void Imageset::updateImageScalingFactors(void)
{
	float hscale, vscale;

	if (d_autoScale)
	{
		hscale = d_horzScaling;
		vscale = d_vertScaling;
	}
	else
	{
		hscale = vscale = 1.0f;
	}

	ImageRegistry::iterator pos = d_images.begin(), end = d_images.end();
	for(; pos != end; ++pos)
	{
		pos->second.setHorzScaling(hscale);
		pos->second.setVertScaling(vscale);
	}

}


/*************************************************************************
	Enable or disable auto-scaling for this Imageset.
*************************************************************************/
void Imageset::setAutoScalingEnabled(bool setting)
{
	if (setting != d_autoScale)
	{
		d_autoScale = setting;
		updateImageScalingFactors();
	}

}


/*************************************************************************
	Set the native resolution for this Imageset
*************************************************************************/
void Imageset::setNativeResolution(const Size& size)
{
	d_nativeHorzRes = size.d_width;
	d_nativeVertRes = size.d_height;

	// re-calculate scaling factors & notify images as required
	notifyDisplaySizeChanged(
       System::getSingleton().getRenderer()->getDisplaySize());
}


/*************************************************************************
	Notify the Imageset of the current (usually new) display resolution.
*************************************************************************/
void Imageset::notifyDisplaySizeChanged(const Size& size)
{
	d_horzScaling = size.d_width / d_nativeHorzRes;
	d_vertScaling = size.d_height / d_nativeVertRes;

	if (d_autoScale)
	{
		updateImageScalingFactors();
	}

}

void Imageset::writeXMLToStream(XMLSerializer& xml_stream) const
{
    // output Imageset tag
    xml_stream.openTag("Imageset")
        .attribute("Name", d_name)
        .attribute("Imagefile", d_textureFilename);

    if (d_nativeHorzRes != DefaultNativeHorzRes)
        xml_stream.attribute("NativeHorzRes", 
          PropertyHelper::uintToString(static_cast<uint>(d_nativeHorzRes)));
    if (d_nativeVertRes != DefaultNativeVertRes)
        xml_stream.attribute("NativeVertRes", 
          PropertyHelper::uintToString(static_cast<uint>(d_nativeVertRes)));

    if (d_autoScale)
        xml_stream.attribute("AutoScaled", "true");

    // output images
    ImageIterator image = getIterator();
    while (!image.isAtEnd())
    {
        image.getCurrentValue().writeXMLToStream(xml_stream);
        ++image;
    }

    // output closing tag
    xml_stream.closeTag();
}


/*************************************************************************
	Return an iterator object that can be used to iterate over the Image
	objects in the Imageset.
*************************************************************************/
Imageset::ImageIterator Imageset::getIterator(void) const
{
	return ImageIterator(d_images.begin(), d_images.end());
}


void Imageset::undefineImage(const String& name)
{
	d_images.erase(name);

	CEGUI_LOGINSANE("Image '" + name + "' has been removed from Imageset '" + d_name + "'.")
}


void Imageset::undefineAllImages(void)
{
	d_images.clear();

	CEGUI_LOGINSANE("All images have been removed from Imageset '" + d_name + "'.")
}

void Imageset::UpdateTextureState() //\u220F?\u2013????\u00EC\u03BC?\u2030\u00F7???\uFFE5?\u00A8\uFFE1\u00A8\u00B1?\u00F7\u00B0\u221Aa\u201D\u2013\u2030\u00F7??\u03BC?\u2018\u00FAa\u00B7\u00B1a\u220F\u2026\u03BC\u00F9
{
	if (d_texture!=NULL&&!d_textureFilename.empty())
	{
        bool bRender=System::getSingleton().getRenderer()->isTextureRender(*d_texture);

		if (!bRender)
		{
			d_LastRenderToNowFrame++;
			if (d_LastRenderToNowFrame >= d_AutoReleaseFrameNum)
			{
				printf("imageset release Tetxure:%s \n", d_textureFilename.c_str());
				System::getSingleton().getRenderer()->ReleaseTexture(d_texture);
				d_texture = NULL;
				d_LastRenderToNowFrame = 0;
			}

		}
		else
		{
			d_LastRenderToNowFrame = 0;
		}
	}
}
    
void Imageset::cleanUpTextureState() //\u220F?\u2013????\u00EC\u03BC?\u2030\u00F7???\uFFE5?\u00A8\uFFE1\u00A8\u00B1?\u00F7\u00B0\u221Aa\u201D\u2013\u2030\u00F7??\u03BC?\u2018\u00FAa\u00B7\u00B1a\u220F\u2026\u03BC\u00F9
{
    if (d_texture!=NULL&&!d_textureFilename.empty())
    {
        bool bRender=System::getSingleton().getRenderer()->isTextureRender(*d_texture);
        if (!bRender)
        {
            printf("imageset release Tetxure:%s \n", d_textureFilename.c_str());
            System::getSingleton().getRenderer()->ReleaseTexture(d_texture);
            d_texture=NULL;
        }
    }
}
    
const String& Imageset::GetFirstImageName()const  //\u83B7\u5F97\u7B2C\u4E00\u4E2Aimage\u7684\u540D\u5B57
{
    if (d_images.size()>0)
    {
        ImageRegistry::const_iterator pos = d_images.begin();
        return pos->second.getName();
    }
    
	static String sEmpty;
	return sEmpty;
    
}
const String& Imageset::GetEndImageName()const    //\u83B7\u5F97\u6700\u540E\u4E00\u4E2A\u7EB9\u7406\u7684\u540D\u5B57
{
    if (d_images.size()>0)
    {
        ImageRegistry::const_iterator pos = d_images.end();
        pos--;
        return pos->second.getName();
    }
    
	static String sEmpty;
	return sEmpty;
}
    
} // End of  CEGUI namespace section
