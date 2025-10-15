/***********************************************************************
    filename:   CEGUIBasicRenderedStringParser.h
    created:    28/05/2009
    author:     Paul Turner
 *************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2009 Paul D Turner & The CEGUI Development Team
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
#ifndef _CEGUIEditboxStringParser_h_
#define _CEGUIEditboxStringParser_h_

#include "CEGUIBasicRenderedStringParser.h"

#if defined(_MSC_VER)
#   pragma warning(push)
#   pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
/*!
\brief
    Basic RenderedStringParser class that offers support for the following tags:
    - 'colour' value is a CEGUI colour property value.
    - 'font' value is the name of a font.
    - 'image' value is a CEGUI image property value.
    - 'window' value is the name of a window.
    - 'vert-alignment' value is either top, bottom, centre or stretch.
    - 'padding' value is a CEGUI Rect property value.
    - 'top-padding' value is a float.
    - 'bottom-padding' value is a float.
    - 'left-padding' value is a float.
    - 'right-padding' value is a float.
    - 'image-size' value is a CEGUI size property value.
    - 'image-width' value is a float.
    - 'image-height' value is a float.
    - 'aspect-lock' value is a boolean (NB: this currently has no effect).
*/
class CEGUIEXPORT EditboxStringParser : public BasicRenderedStringParser
{
public:
    // Strings for supported tags
  

    //! Constructor.
    EditboxStringParser();
    /*!
    \brief
        Initialising constructor.

    \param initial_font
        Reference to a String holding the name of the initial font to be used.

    \param initial_colours
        Reference to a ColourRect describing the initial colours to be used.
    */
    EditboxStringParser(const String& initial_font,
                              const ColourRect& initial_colours);
    //! Destructor.
    virtual ~EditboxStringParser();

  
 
    // implement required interface from RenderedStringParser
    bool parse(const String& input_string,String& out_String);

protected:
    
    bool processControlString( const String& ctrl_str);

};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#   pragma warning(pop)
#endif

#endif // end of guard _CEGUIBasicRenderedStringParser_h_
