/***********************************************************************
	filename: 	CEGUIFreeTypeFont.h
	created:	3/6/2006
	author:		Andrew Zabolotny

	purpose:    Implementation of the Font class via the FreeType library
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
#ifndef _CEGUIFreeTypeFont_h_
#define _CEGUIFreeTypeFont_h_

#include "CEGUIFont.h"
#include "CEGUIImage.h"
#include "CEGUIDataContainer.h"
#include "Nuclear.h"
#include "../../../../common/ljfm/code/include/ljfmimage.h"
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#define generic GenericFromFreeTypeLibrary
#define internal InternalFromFreeTypeLibrary
#endif
#include <ft2build.h>
#include <freetype/ftglyph.h>

#include <platform/mutex.h>
#include FT_FREETYPE_H
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#undef generic
#undef internal
#endif
// Start of CEGUI namespace section
namespace CEGUI
{
/*!
\brief
    Implementation of the Font class interface using the FreeType library.

    This implementation tries to provide maximal support for any kind of
    fonts supported by FreeType. It has been tested on outline font formats
    like TTF and PS as well as on bitmap font formats like PCF and FON.

    Glyphs are rendered dynamically on demand, so a large font with lots
    of glyphs won't slow application startup time.
*/
class FreeTypeFont : public Font
{
private:
    struct FontPage{
        struct Grid{
            utf32 codepoint;
            
            String  name;
            Rect    area;
            Point   offset;
            float   adv;
            
            Rect    copyRect;
            uchar* buffer;
        };
        Imageset*   m_pImageset;
        uchar*     m_pBuffer;
        bool        m_bFull;
        std::list<Grid> m_lTexts;
        
        FontPage(){
            m_pImageset = NULL;
            m_pBuffer = NULL;
            m_bFull = false;
        }
    };
    core::CMutex        m_loadingPageLock;
    std::list<FontPage*> m_asyncLoadingPages;
    std::set<utf32>     m_loadingCodepoints;

public:
    /*!
    \brief
        Constructor for FreeTypeFont based fonts.

    \param font_name
        The name that the font will use within the CEGUI system.

    \param point_size
        Specifies the point size that the font is to be rendered at.

    \param anti_aliased
        Specifies whether the font should be rendered using anti aliasing.

    \param font_filename
        The filename of an font file that will be used as the source for
        glyph images for this font.

    \param resource_group
        The resource group identifier to use when loading the font file
        specified by \a font_filename.

    \param auto_scaled
        Specifies whether the font imagery should be automatically scaled to
        maintain the same physical size (which is calculated by using the
        native resolution setting).

    \param native_horz_res
        The horizontal native resolution value.  This is only significant when
        auto scaling is enabled.

    \param native_vert_res
        The vertical native resolution value.  This is only significant when
        auto scaling is enabled.
    */
    FreeTypeFont(const String& font_name, const float point_size,
                 const bool anti_aliased, const String& font_filename,
                 const String& resource_group = "",
                 const bool auto_scaled = false,
                 const float native_horz_res = 640.0f,
                 const float native_vert_res = 480.0f);

    //! Destructor.
    ~FreeTypeFont();

    //! return the point size of the freetype font.
    float getPointSize() const;

    //! return whether the freetype font is rendered anti-aliased.
    bool isAntiAliased() const;

    //! return the point size of the freetype font.
    void setPointSize(const float point_size);

    //! return whether the freetype font is rendered anti-aliased.
    void setAntiAliased(const bool anti_alaised);
    
    void updateFont();
    
    void LoadGlyph(utf32 codepoint);

#ifndef FORCEGUIEDITOR
	bool GetTextData(const char* pChar, const String& font, int size, unsigned char*& pData, uint& w, uint& h);
#endif // !FORCEGUIEDITOR

public:
	static void SetDefaultFont(String& fontFile, int fontType);
private:
	static String	s_defaultFontFile;
	static int s_defaultFontType;
protected:
    /*!
    \brief
        Copy the current glyph data into \a buffer, which has a width of
        \a buf_width pixels (not bytes).

    \param buffer
        Memory buffer large enough to receive the imagery for the currently
        loaded glyph.

    \param buf_width
        Width of \a buffer in pixels (where each pixel is a argb_t).

    \return
        Nothing.
    */
	void drawGlyphToBuffer(uchar* buffer, uint buf_width, FT_Face face) const;

    /*!
    \brief
        Return the required texture size required to store imagery for the
        glyphs from s to e
    \param s
        The first glyph in set
    \param e
        The last glyph in set
    */
    uint getTextureSize(CodepointMap::const_iterator s,
                        CodepointMap::const_iterator e) const;

	const FontGlyph * getGlyphData (utf32 codepoint);   

    //! Register all properties of this class.
    void addFreeTypeFontProperties();
    //! Free all allocated font data.
    void free();

    // overrides of functions in Font base class.
    void rasterise(utf32 start_codepoint, utf32 end_codepoint) ;
	void rasteriseHZ(utf32 codepoint);
    void writeXMLToStream_impl (XMLSerializer& xml_stream) const;

    //! Point size of font.
    float d_ptSize;
    //! True if the font should be rendered as anti-alaised by freeType.
    bool d_antiAliased;
    
    FT_Open_Args d_openFontArg;
    FT_StreamRec d_fontStream;
    //! FreeType-specific font handle
	FT_Face d_fontFace;
	FT_Face	 d_defFace;
    //! Font file data
#ifdef PUBLISHED_VERSION
	LJFM::LJFMID d_fontData;
#else
    RawDataContainer d_fontData;
#endif
    //! Type definition for ImagesetVector.
    typedef std::vector<Imageset*> ImagesetVector;
    //! Imagesets that holds the glyphs for this font.
    mutable ImagesetVector d_glyphImages;
	CodepointMap d_hz_map;
	bool m_Words;
	uint  m_nHZX ;
	uint  m_nHZY; 
	uint  m_nHZYB; 
	Imageset* hzImageset;
	uchar* hzmem_buffer;

	unsigned char* m_FontBuff;
};

} // End of  CEGUI namespace section

#endif	// end of guard _CEGUIFreeTypeFont_h_
