/***********************************************************************
	filename:   CEGUIFont.cpp
	created:    21/2/2004
	author:     Paul D Turner

	purpose:    Implements FreeTypeFont class
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
#include "CEGUIFreeTypeFont.h"
#include "CEGUIExceptions.h"
#include "CEGUITexture.h"
#include "CEGUIImageset.h"
#include "CEGUIImagesetManager.h"
#include "CEGUILogger.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIFont_xmlHandler.h"
#include <cmath>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <algorithm>

#include "CEGUIPfsResourceProvider.h"
#include "../../../../common/platform/utils/FileUtil.h"
#include "../../../../common/platform/utils/StringUtil.h"
#include "CEGUILoadingTaskManager.h"
//#include "../../../../dependencies/LJXML/Include/LJXML.hpp"

#ifndef FORCEGUIEDITOR
#include <platform/CCImage.h>
#endif
#ifdef _MSC_VER
#define snprintf _snprintf
#endif

namespace CEGUI
{
	//----------------------------------------------------------------------------//
	// Pixels to put between glyphs
#define INTER_GLYPH_PAD_SPACE 2
	// A multiplication coefficient to convert FT_Pos values into normal floats
#define FT_POS_COEF  (1.0/64.0)

	//----------------------------------------------------------------------------//
	// Font objects usage count
	static int ft_usage_count = 0;
	// A handle to the FreeType library
	static FT_Library ft_lib;

	//----------------------------------------------------------------------------//
#undef __FTERRORS_H__
#define FT_ERRORDEF( e, v, s ) s,
#define FT_ERROR_START_LIST static const char* ft_errors[] = {
#define FT_ERROR_END_LIST 0};
#include FT_ERRORS_H

	struct FontBufferInfo
	{
		unsigned char* buffer; //缓冲区
		int numberOfReferences;// 引用次数
	};
	static std::map<std::wstring, FontBufferInfo> sa_FontBuff; //key = font name.

	String FreeTypeFont::s_defaultFontFile;
	int FreeTypeFont::s_defaultFontType;
	void FreeTypeFont::SetDefaultFont(String& fontFile, int fontType)
	{
		s_defaultFontFile = fontFile;
		s_defaultFontType = fontType;
	}
	//----------------------------------------------------------------------------//
	FreeTypeFont::FreeTypeFont(const String& font_name, const float point_size,
		const bool anti_aliased, const String& font_filename,
		const String& resource_group, const bool auto_scaled,
		const float native_horz_res,
		const float native_vert_res) :
		Font(font_name, Font_xmlHandler::FontTypeFreeType, font_filename, resource_group, auto_scaled, native_horz_res, native_vert_res),
		d_ptSize(point_size),
		d_antiAliased(anti_aliased),
		d_fontFace(0),
		d_defFace(NULL),
		m_Words(false),
		hzmem_buffer(0),
		m_FontBuff(0),
		m_nHZX(INTER_GLYPH_PAD_SPACE),
		m_nHZY(INTER_GLYPH_PAD_SPACE),
		m_nHZYB(INTER_GLYPH_PAD_SPACE)
	{
		d_hz_map.clear();
		if (!ft_usage_count++){
			FT_Init_FreeType(&ft_lib);
		}
		addFreeTypeFontProperties();

		updateFont();

		char tmp[50];
		snprintf(tmp, sizeof(tmp), "Succsessfully loaded %d glyphs", static_cast<int>(d_cp_map.size()));
		Logger::getSingleton().logEvent(tmp, Informative);
	}

	//----------------------------------------------------------------------------//
	FreeTypeFont::~FreeTypeFont()
	{
		free();

		if (!--ft_usage_count)
			FT_Done_FreeType(ft_lib);
	}

	//----------------------------------------------------------------------------//
	uint FreeTypeFont::getTextureSize(CodepointMap::const_iterator s,
		CodepointMap::const_iterator e) const
	{
		uint texsize = 32; // start with 32x32
		uint max_texsize = System::getSingleton().getRenderer()->getMaxTextureSize();
		uint glyph_count = 0;

		// Compute approximatively the optimal texture size for font
		while (texsize < max_texsize)
		{
			uint x = INTER_GLYPH_PAD_SPACE, y = INTER_GLYPH_PAD_SPACE;
			uint yb = INTER_GLYPH_PAD_SPACE;
			for (CodepointMap::const_iterator c = s; c != e; ++c)
			{
				// skip glyphs that are already rendered
				if (c->second.getImage())
					continue;

				// load glyph metrics (don't render)
				if (FT_Load_Char(d_fontFace, c->first,
					FT_LOAD_DEFAULT | FT_LOAD_FORCE_AUTOHINT))
					continue;

				uint glyph_w = int(ceil(d_fontFace->glyph->metrics.width * FT_POS_COEF)) +
					INTER_GLYPH_PAD_SPACE;
				uint glyph_h = int(ceil(d_fontFace->glyph->metrics.height * FT_POS_COEF)) +
					INTER_GLYPH_PAD_SPACE;

				x += glyph_w;
				if (x > texsize)
				{
					x = INTER_GLYPH_PAD_SPACE;
					y = yb;
				}
				uint yy = y + glyph_h;
				if (yy > texsize)
					goto too_small;

				if (yy > yb)
					yb = yy;

				++glyph_count;
			}
			// Okay, the texture size is enough for holding our glyphs
			break;

		too_small:
			texsize *= 2;
		}

		return glyph_count ? texsize : 0;
	}

	//----------------------------------------------------------------------------//
	void FreeTypeFont::rasterise(utf32 start_codepoint, utf32 end_codepoint)
	{
		CodepointMap::const_iterator s = d_cp_map.lower_bound(start_codepoint);
		if (s == d_cp_map.end())
			return;

		CodepointMap::const_iterator orig_s = s;
		CodepointMap::const_iterator e = d_cp_map.upper_bound(end_codepoint);
		while (true)
		{
			// Create a new Imageset for glyphs
			uint texsize = getTextureSize(s, e);
			// If all glyphs were already rendered, do nothing
			if (!texsize)
				break;

			Imageset& is = ImagesetManager::getSingleton().create(
				d_name + "_auto_glyph_images_" + int(s->first),
				System::getSingleton().getRenderer()->createTexture());
			d_glyphImages.push_back(&is);

			// Create a memory buffer where we will render our glyphs
			uchar *mem_buffer = new uchar[texsize * texsize];
			for (int i = 0; i < texsize * texsize; i++)
				mem_buffer[i] = 0;

			// Go ahead, line by line, top-left to bottom-right
			uint x = INTER_GLYPH_PAD_SPACE, y = INTER_GLYPH_PAD_SPACE;
			uint yb = INTER_GLYPH_PAD_SPACE;

			// Set to true when we finish rendering all glyphs we were asked to
			bool finished = false;
			// Set to false when we reach d_cp_map.end() and we start going backward
			bool forward = true;

			/* To conserve texture space we will render more glyphs than asked,
			 * but never less than asked. First we render all glyphs from s to e
			 * and after that we render glyphs until we reach d_cp_map.end(),
			 * and if there's still free texture space we will go backward
			 * from s until we hit d_cp_map.begin().
			 */
			while (s != d_cp_map.end())
			{
				// Check if we finished rendering all the required glyphs
				finished |= (s == e);

				// Check if glyph already rendered
				if (!s->second.getImage())
				{
					// Render the glyph
					if (FT_Load_Char(d_fontFace, s->first, FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT |
						(d_antiAliased ? FT_LOAD_TARGET_NORMAL : FT_LOAD_TARGET_MONO)))
					{
						std::stringstream err;
						err << "Font::loadFreetypeGlyph - Failed to load glyph for codepoint: ";
						err << static_cast<unsigned int>(s->first);
						err << ".  Will use an empty image for this glyph!";
						Logger::getSingleton().logEvent(err.str(), Errors);

						// Create a 'null' image for this glyph so we do not seg later
						Rect area(0, 0, 0, 0);
						Point offset(0, 0);
						String name;
						name += s->first;
						is.defineImage(name, area, offset);
						((FontGlyph &)s->second).setImage(&is.getImage(name));
					}
					else
					{
						uint glyph_w = d_fontFace->glyph->bitmap.width + INTER_GLYPH_PAD_SPACE;
						uint glyph_h = d_fontFace->glyph->bitmap.rows + INTER_GLYPH_PAD_SPACE;

						// Check if glyph right margin does not exceed texture size
						uint x_next = x + glyph_w;
						if (x_next > texsize)
						{
							x = INTER_GLYPH_PAD_SPACE;
							x_next = x + glyph_w;
							y = yb;
						}

						// Check if glyph bottom margine does not exceed texture size
						uint y_bot = y + glyph_h;
						if (y_bot > texsize)
							break;

						// Copy rendered glyph to memory buffer in RGBA format
						drawGlyphToBuffer(mem_buffer + (y * texsize) + x, texsize, d_fontFace);

						// Create a new image in the imageset
						Rect area(static_cast<float>(x),
							static_cast<float>(y),
							static_cast<float>(x + glyph_w - INTER_GLYPH_PAD_SPACE),
							static_cast<float>(y + glyph_h - INTER_GLYPH_PAD_SPACE));

						Point offset(d_fontFace->glyph->metrics.horiBearingX * static_cast<float>(FT_POS_COEF),
							-d_fontFace->glyph->metrics.horiBearingY * static_cast<float>(FT_POS_COEF));

						String name;
						name += s->first;
						is.defineImage(name, area, offset);
						((FontGlyph &)s->second).setImage(&is.getImage(name));

						// Advance to next position
						x = x_next;
						if (y_bot > yb)
						{
							yb = y_bot;
						}
					}
				}

				// Go to next glyph, if we are going forward
				if (forward)
					if (++s == d_cp_map.end())
					{
						finished = true;
						forward = false;
						s = orig_s;
					}
				// Go to previous glyph, if we are going backward
				if (!forward)
					if ((s == d_cp_map.begin()) || (--s == d_cp_map.begin()))
						break;
			}

			// Copy our memory buffer into the texture and free it
			float fSize = static_cast<float>(texsize);
			uchar* argbBuffer = (uchar*)malloc(sizeof(argb_t)*texsize*texsize);
			int index = 0;
			for (int i = 0; i < texsize; ++i)
			{
				for (int j = 0; j < texsize; ++j)
				{
					argbBuffer[index + 0] = 0xff;
					argbBuffer[index + 1] = 0xff;
					argbBuffer[index + 2] = 0xff;
					argbBuffer[index + 3] = ((uchar*)mem_buffer)[index / 4];
					index += 4;
				}
			}
			is.getTexture()->loadFromMemory(argbBuffer, Size(fSize, fSize), Texture::PF_RGBA, false);
			::free(argbBuffer);
			delete[] mem_buffer;
			if (finished)
				break;
		}
	}

	//----------------------------------------------------------------------------//
	void FreeTypeFont::drawGlyphToBuffer(uchar* buffer, uint buf_width, FT_Face face) const
	{
		FT_Bitmap *glyph_bitmap = &face->glyph->bitmap;

		for (int i = 0; i < glyph_bitmap->rows; ++i)
		{
			uchar *src = glyph_bitmap->buffer + (i * glyph_bitmap->pitch);
			switch (glyph_bitmap->pixel_mode)
			{
			case FT_PIXEL_MODE_GRAY:
			{
				uchar *dst = reinterpret_cast<uchar*>(buffer);
				for (int j = 0; j < glyph_bitmap->width; ++j)
				{
					// RGBA
					*dst++ = *src++;
				}
			}
			break;

			case FT_PIXEL_MODE_MONO:
				for (int j = 0; j < glyph_bitmap->width; ++j)
					buffer[j] = (src[j / 8] & (0x80 >> (j & 7))) ? 0xFF : 0x00;
				break;

			default:
				throw InvalidRequestException("Font::drawGlyphToBuffer: "
					"The glyph could not be drawn because the pixel mode is "
					"unsupported.");
				break;
			}

			buffer += buf_width;
		}
	}

	//----------------------------------------------------------------------------//
	void FreeTypeFont::free()
	{
		if (!d_fontFace)
			return;

		d_cp_map.clear();
		d_hz_map.clear();
		if (hzmem_buffer)
		{
			delete[] hzmem_buffer;
			hzmem_buffer = 0;
		}

		if (m_FontBuff)
		{
			std::map<std::wstring, FontBufferInfo>::iterator it = sa_FontBuff.begin();
			while (it != sa_FontBuff.end())
			{
				if (it->second.buffer == m_FontBuff)
				{
					--it->second.numberOfReferences;
					if (!it->second.numberOfReferences)
					{
						delete[] it->second.buffer;
						sa_FontBuff.erase(it);
						break;
					}
				}
				++it;
			}
			m_FontBuff = 0;
		}

		for (size_t i = 0; i < d_glyphImages.size(); i++)
			ImagesetManager::getSingleton().destroy(d_glyphImages[i]->getName());
		d_glyphImages.clear();

		FT_Done_Face(d_fontFace);
		d_fontFace = 0;
		FT_Done_Face(d_defFace);
		d_defFace = NULL;
		m_Words = false;
#ifndef PUBLISHED_VERSION
		System::getSingleton().getResourceProvider()->unloadRawDataContainer(d_fontData);
#endif
	}

	static unsigned long free_type_io(FT_Stream       stream,
		unsigned long   offset,
		unsigned char*  buffer,
		unsigned long   count)
	{
		static core::CMutex* pFontMutex = NULL;
		if (pFontMutex == NULL) {
			pFontMutex = new core::CMutex;
		}
		pFontMutex->Lock();
		assert(stream->pathname.pointer);

		LJFM::LJFMF* pFile = (LJFM::LJFMF*)stream->pathname.pointer;
		pFile->Seek(offset, LJFM::FSM_SET);
		if (buffer)
		{
			size_t rd = pFile->Read(buffer, count);
		}
		pFontMutex->UNLock();
		return count;
	}

	static void free_type_close(FT_Stream  stream)
	{
		assert(stream->pathname.pointer);
		LJFM::LJFMF* pFile = (LJFM::LJFMF*)stream->pathname.pointer;
		pFile->Close();
		delete pFile;	// yeqing 2015-10-20
		return;
	}

	int _wtoi(const wchar_t* str)
	{
		int ret = 0;
		std::string str_s = ws2s(str);
		sscanf(str_s.c_str(), "%d", &ret);
		return ret;
	}

	//----------------------------------------------------------------------------//
	void FreeTypeFont::updateFont()
	{
		free();

		int face_index = 0;
		int language = 0;
		String final_filename;

#ifndef ANDROID
		final_filename = "/ui/fonts/" + d_filename;
#else
		final_filename = "/ui/fonts/" + d_filename;
#endif

		LJFM::LJFMF* pFile = new LJFM::LJFMF();
		std::wstring finalName = s2ws(final_filename.c_str());
		if (!pFile->Open(finalName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
		{
			delete pFile;
			return;
		}

		std::map<std::wstring, FontBufferInfo>::iterator it = sa_FontBuff.find(finalName);
		size_t size = static_cast<size_t>(pFile->GetSize());
		if (it == sa_FontBuff.end())
		{
			m_FontBuff = new unsigned char[size];
			pFile->ReadAll(m_FontBuff, size);

			FontBufferInfo fbi;
			fbi.buffer = m_FontBuff;
			fbi.numberOfReferences = 1;
			sa_FontBuff.insert(std::pair<std::wstring, FontBufferInfo>(finalName, fbi));
		}
		else
		{
			m_FontBuff = it->second.buffer;
			++it->second.numberOfReferences;
		}
		pFile->Close();

		d_openFontArg.flags = FT_OPEN_STREAM;
		d_openFontArg.memory_base = NULL;
		d_openFontArg.memory_size = 0;
		d_openFontArg.pathname = NULL;

		d_fontStream.pathname.pointer = (void*)pFile;
		d_fontStream.base = NULL;
		d_fontStream.size = size;
		d_fontStream.pos = 0;
		d_fontStream.memory = NULL;
		d_fontStream.cursor = NULL;
		d_fontStream.limit = NULL;

		d_fontStream.read = free_type_io;
		d_fontStream.close = free_type_close;
		d_openFontArg.stream = &d_fontStream;

		FT_Error error = FT_New_Memory_Face(ft_lib, m_FontBuff, size, 0, &d_fontFace);
		if (error != 0)
		{
			it = sa_FontBuff.find(finalName);
			sa_FontBuff.erase(it);

			delete[] m_FontBuff;
			m_FontBuff = 0;
			throw GenericException("FreeTypeFont::updateFont: Failed to create "
				"face from font file '" + d_filename + "' error was: " +
				((error < FT_Err_Max) ? ft_errors[error] : "unknown error"));
		}

		delete pFile;

		// check that default Unicode character map is available
		if (!d_fontFace->charmap)
		{
			FT_Done_Face(d_fontFace);
			d_fontFace = 0;
			throw GenericException("FreeTypeFont::updateFont: "
				"The font '" + d_name + "' does not have a Unicode charmap, and "
				"cannot be used.");
		}

		uint horzdpi = static_cast<uint>(System::getSingleton().getRenderer()->getDisplayDPI().d_x);
		uint vertdpi = static_cast<uint>(System::getSingleton().getRenderer()->getDisplayDPI().d_y);

		float hps = d_ptSize * 64 * 132.0f / horzdpi;
		float vps = d_ptSize * 64 * 132.0f / vertdpi;
		if (d_autoScale)
		{
			hps *= d_horzScaling;
			vps *= d_vertScaling;
		}

		if (FT_Set_Char_Size(d_fontFace, FT_F26Dot6(hps), FT_F26Dot6(vps), horzdpi, vertdpi))
		{
			// For bitmap fonts we can render only at specific point sizes.
			// Try to find nearest point size and use it, if that is possible
			float ptSize_72 = (d_ptSize * 72.0f) / vertdpi;
			float best_delta = 99999;
			float best_size = 0;
			for (int i = 0; i < d_fontFace->num_fixed_sizes; i++)
			{
				float size = d_fontFace->available_sizes[i].size * float(FT_POS_COEF);
				float delta = fabs(size - ptSize_72);
				if (delta < best_delta)
				{
					best_delta = delta;
					best_size = size;
				}
			}

			if ((best_size <= 0) || FT_Set_Char_Size(d_fontFace, 0, FT_F26Dot6(best_size * 64), 0, 0))
			{
				char size[20];
				snprintf(size, sizeof(size), "%g", d_ptSize);
				throw GenericException("FreeTypeFont::load - The font '" + d_name + "' cannot be rasterised at a size of " + size + " points, and cannot be used.");
			}
		}

		if (d_fontFace->face_flags & FT_FACE_FLAG_SCALABLE)
		{
			//float x_scale = d_fontFace->size->metrics.x_scale * FT_POS_COEF * (1.0/65536.0);
			float y_scale = d_fontFace->size->metrics.y_scale * float(FT_POS_COEF) * (1.0f / 65536.0f);
			d_ascender = d_fontFace->ascender * y_scale;
			d_descender = d_fontFace->descender * y_scale;
			d_height = d_fontFace->height * y_scale;
		}
		else
		{
			d_ascender = d_fontFace->size->metrics.ascender * float(FT_POS_COEF);
			d_descender = d_fontFace->size->metrics.descender * float(FT_POS_COEF);
			d_height = d_fontFace->size->metrics.height * float(FT_POS_COEF);
		}

		// Create an empty FontGlyph structure for every glyph of the font
		FT_UInt gindex;
		FT_ULong codepoint = FT_Get_First_Char(d_fontFace, &gindex);
		FT_ULong max_codepoint = codepoint;
		float numberAdv = 0.0f;
		while (codepoint <= 256)
		{
			/* if (max_codepoint < codepoint)
				 max_codepoint = codepoint;*/

			// load-up required glyph metrics (don't render)
			if (FT_Load_Char(d_fontFace, codepoint, FT_LOAD_DEFAULT | FT_LOAD_FORCE_AUTOHINT))
				continue; // glyph error

			float adv = d_fontFace->glyph->metrics.horiAdvance * float(FT_POS_COEF);

			if (codepoint == 48)
			{
				numberAdv = adv;
			}

			if (codepoint > 48 && codepoint <= 57)
			{
				adv = numberAdv;
			}

			if (codepoint > 0 && codepoint < 0x80 && FREETYPE_MAJOR == 2 && FREETYPE_MINOR == 3 && FREETYPE_PATCH == 9)
			{
				adv = adv / 2;
			}

			// create a new empty FontGlyph with given character code
			d_cp_map[codepoint] = FontGlyph(adv);

			// proceed to next glyph
			//codepoint = FT_Get_Next_Char(d_fontFace, codepoint, &gindex);
			codepoint++;
		}
#ifndef ANDROID
		//    const FontGlyph* pGlyph9=getGlyphData(57);
		//    if(pGlyph9)
		//    {
		//        d_MaxHzWidth=pGlyph9->getRenderedAdvance(1.0f)*2.0f;
		//    }
#endif
		setMaxCodepoint(65535);
	}

	//----------------------------------------------------------------------------//
	void FreeTypeFont::writeXMLToStream_impl(XMLSerializer& xml_stream) const
	{
		xml_stream.attribute(Font_xmlHandler::FontSizeAttribute,
			PropertyHelper::floatToString(d_ptSize));
		if (!d_antiAliased)
			xml_stream.attribute(Font_xmlHandler::FontAntiAliasedAttribute, "False");
	}

	//----------------------------------------------------------------------------//
	float FreeTypeFont::getPointSize() const
	{
		return d_ptSize;
	}

	//----------------------------------------------------------------------------//
	bool FreeTypeFont::isAntiAliased() const
	{
		return d_antiAliased;
	}

	//----------------------------------------------------------------------------//
	void FreeTypeFont::setPointSize(const float point_size)
	{
		if (point_size == d_ptSize)
			return;

		d_ptSize = point_size;
		updateFont();
	}

	//----------------------------------------------------------------------------//
	void FreeTypeFont::setAntiAliased(const bool anti_alaised)
	{
		if (anti_alaised == d_antiAliased)
			return;

		d_antiAliased = anti_alaised;
		updateFont();
	}

	// amount of bits in a uint   
#define BITS_PER_UINT   (sizeof (uint) * 8)   
	// must be a power of two   
#define GLYPHS_PER_PAGE 256   


	const FontGlyph * FreeTypeFont::getGlyphData(utf32 codepoint)
	{
		if (codepoint < 256)  //忙藛鈭偮甭Ｂ∶欌�溌�欌増艗鈭喡棵屸垜鈮堚�澛⑴捚掆埆脮鈼娒封垜藲
		{
			Font* pNumberFont = GetNumberFont();
			if (pNumberFont && pNumberFont->d_cp_map.size() > 0 && !m_isNeedReaload)
			{
				return pNumberFont->getGlyphData(codepoint);
			}

			if (!m_Words || m_isNeedReaload)
			{
				rasterise(codepoint & ~(GLYPHS_PER_PAGE - 1), codepoint | (GLYPHS_PER_PAGE - 1));

				m_Words = true;
				m_isNeedReaload = false;
			}

			CodepointMap::const_iterator pos = d_cp_map.find(codepoint);

			return (pos != d_cp_map.end()) ? &pos->second : 0;
		}

		else //艙鈥樎犆︹埆鈭棅梅驴锟?
		{
			CodepointMap::const_iterator pos;

			pos = d_hz_map.find(codepoint);

			if (pos != d_hz_map.end() && !m_isNeedReaload)
			{
				return (pos != d_hz_map.end()) ? &pos->second : 0;
			}
			else
			{
				rasteriseHZ(codepoint);
				m_isNeedReaload = false;
				pos = d_hz_map.find(codepoint);

				return (pos != d_hz_map.end()) ? &pos->second : 0;
			}
		}
	}

	class CLoadGlyphTask : public ITask
	{
		FreeTypeFont*     m_pFont;
		utf32             m_codepoint;
	public:
		CLoadGlyphTask(FreeTypeFont* aPFont, utf32 aCodePoint)
			: ITask(eTPFont)
			, m_pFont(aPFont)
			, m_codepoint(aCodePoint)
		{
			assert(aPFont);
		}

		virtual void Run()
		{
			m_pFont->LoadGlyph(m_codepoint);
		}
	};

	void FreeTypeFont::LoadGlyph(utf32 codepoint)
	{
		uint texsize = 512;
		Rect copyRect;
		if (FT_Load_Char(d_fontFace, codepoint, FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT | (d_antiAliased ? FT_LOAD_TARGET_NORMAL : FT_LOAD_TARGET_MONO)))
		{
			std::stringstream err;
			err << "Font::loadFreetypeGlyph - Failed to load glyph for codepoint: ";
			err << static_cast<unsigned int> (codepoint);
			err << ".  Will use an empty image for this glyph!";

			Logger::getSingleton().logEvent(err.str(), Errors);
			// Create a 'null' image for this glyph so we do not seg later

			Rect area(0, 0, 0, 0);
			Point offset(0, 0);
			String name;
			name += codepoint;

			m_loadingPageLock.Lock();
			if (m_asyncLoadingPages.empty())
			{
				m_asyncLoadingPages.push_back(new FontPage);
			}

			FontPage::Grid grid;
			grid.codepoint = codepoint;
			grid.area = area;
			grid.offset = offset;
			grid.name = name;
			grid.adv = d_fontFace->glyph->metrics.horiAdvance * float(FT_POS_COEF);
			grid.buffer = NULL;
			m_asyncLoadingPages.back()->m_lTexts.push_back(grid);
			m_loadingPageLock.UNLock();
		}
		else
		{
			uint glyph_w = d_fontFace->glyph->bitmap.width + INTER_GLYPH_PAD_SPACE;
			uint glyph_h = d_fontFace->glyph->bitmap.rows + INTER_GLYPH_PAD_SPACE;

			// Check if glyph right margin does not exceed texture size

			uint x_next = m_nHZX + glyph_w;
			if (x_next > texsize)
			{
				m_nHZX = INTER_GLYPH_PAD_SPACE;
				x_next = m_nHZX + glyph_w;
				m_nHZY = m_nHZYB;
			}

			m_loadingPageLock.Lock();
			// Check if glyph bottom margine does not exceed texture size
			if (m_asyncLoadingPages.empty())
			{
				m_asyncLoadingPages.push_back(new FontPage);
				m_asyncLoadingPages.back()->m_pBuffer = new uchar[texsize * texsize];
				for (int i = 0; i < texsize * texsize; i++)
					m_asyncLoadingPages.back()->m_pBuffer[i] = 0x00;
			}

			FontPage* pPage = m_asyncLoadingPages.back();
			uint y_bot = m_nHZY + glyph_h;

			if (y_bot > texsize)
			{
				if (!m_asyncLoadingPages.empty())
				{
					m_asyncLoadingPages.back()->m_bFull = true;
					m_asyncLoadingPages.push_back(new FontPage);
				}
				else
				{
					m_asyncLoadingPages.push_back(new FontPage);
				}

				pPage = m_asyncLoadingPages.back();
				assert(pPage->m_pBuffer == NULL);

				pPage->m_pBuffer = new uchar[texsize * texsize];
				for (int i = 0; i < texsize * texsize; i++)
					pPage->m_pBuffer[i] = 0x0;

				m_nHZX = m_nHZY = m_nHZYB = INTER_GLYPH_PAD_SPACE;
				y_bot = m_nHZY + glyph_h;
			}

			// Copy rendered glyph to memory buffer in RGBA format

			drawGlyphToBuffer(pPage->m_pBuffer + (m_nHZY * texsize) + m_nHZX, texsize, d_fontFace);

			FT_Bitmap *glyph_bitmap = &d_fontFace->glyph->bitmap;
			copyRect.d_left = static_cast<float>(m_nHZX);
			copyRect.d_right = copyRect.d_left + glyph_bitmap->width;

			copyRect.d_top = static_cast<float>(m_nHZY);
			copyRect.d_bottom = copyRect.d_top + glyph_bitmap->rows;

			// Create a new image in the imageset        
			Rect area(static_cast<float>(m_nHZX),
				static_cast<float>(m_nHZY),
				static_cast<float>(m_nHZX + glyph_w - INTER_GLYPH_PAD_SPACE),
				static_cast<float>(m_nHZY + glyph_h - INTER_GLYPH_PAD_SPACE));

			Point offset(d_fontFace->glyph->metrics.horiBearingX * static_cast<float>(FT_POS_COEF),
				-d_fontFace->glyph->metrics.horiBearingY * static_cast<float>(FT_POS_COEF));

			String name;
			name += codepoint;

			FontPage::Grid grid;
			grid.codepoint = codepoint;
			grid.area = area;
			grid.offset = offset;
			grid.name = name;
			grid.copyRect = copyRect;
			grid.adv = d_fontFace->glyph->metrics.horiAdvance * float(FT_POS_COEF);
			grid.buffer = pPage->m_pBuffer;
			pPage->m_lTexts.push_back(grid);

			m_nHZX = x_next;

			if (y_bot > m_nHZYB)
			{
				m_nHZYB = y_bot;
			}
			m_loadingPageLock.UNLock();
		}
	}

	int g_loadingTextCount = 0;

	void FreeTypeFont::rasteriseHZ(utf32 codepoint)
	{
		uint texsize = 512;
		Rect copyRect;

		if (d_async)
		{//寮傛鍔犺浇
			//loaded glyphes
			m_loadingPageLock.Lock();
			bool bNeedLoad = true;
			float fSize = static_cast<float>(texsize);
			for (std::list<FontPage*>::iterator page = m_asyncLoadingPages.begin(); page != m_asyncLoadingPages.end(); ++page) {
				if ((*page)->m_pImageset == NULL) {
					(*page)->m_pImageset = &(ImagesetManager::getSingleton().create(d_name + "_auto_glyph_images_" + int(codepoint), System::getSingleton().getRenderer()->createTexture()));
					d_glyphImages.push_back((*page)->m_pImageset);
				}

				assert((*page)->m_pImageset);
				assert((*page)->m_pBuffer);
				for (std::list<FontPage::Grid>::iterator grid = (*page)->m_lTexts.begin(); grid != (*page)->m_lTexts.end(); ++grid) {
					if (grid->codepoint == codepoint) {
						bNeedLoad = false;
					}
					CodepointMap::const_iterator hzInter = d_hz_map.find(grid->codepoint);
					if (hzInter != d_hz_map.end()) {
						assert(false);
						continue;
					}
					d_hz_map[grid->codepoint] = FontGlyph(0.0f);
					hzInter = d_hz_map.find(grid->codepoint);

					(*page)->m_pImageset->defineImage(grid->name, grid->area, grid->offset);
					((FontGlyph &)hzInter->second).setImage(&(*page)->m_pImageset->getImage(grid->name));
					d_hz_map[grid->codepoint].setAdvance(grid->adv);

					if (grid->buffer != NULL) {
						(*page)->m_pImageset->getTexture()->updataFromMemory(grid->buffer, CEGUI::Size(fSize, fSize), grid->copyRect, Texture::PF_RGBA);
					}
					g_loadingTextCount--;
				}
				(*page)->m_lTexts.clear();
			}

			while (!m_asyncLoadingPages.empty())
			{
				if (m_asyncLoadingPages.front()->m_bFull)
				{
					delete m_asyncLoadingPages.front()->m_pBuffer;
					delete m_asyncLoadingPages.front();
					m_asyncLoadingPages.pop_front();
				}
				else
				{
					break;
				}
			}

			if (bNeedLoad && m_loadingCodepoints.find(codepoint) == m_loadingCodepoints.end())
			{
				//add load task
				ITask* pTask = new CLoadGlyphTask(this, codepoint);
				if (!CCEGUITaskManager::GetInstancePtr()->QueueTask(pTask))
				{
					delete pTask;
					return;
				}
				g_loadingTextCount++;
				m_loadingCodepoints.insert(codepoint);
			}
			m_loadingPageLock.UNLock();
			return;
		}

		if (d_hz_map.size() == 0)
		{
			hzImageset = &(ImagesetManager::getSingleton().create(d_name + "_auto_glyph_images_" + int(codepoint), System::getSingleton().getRenderer()->createTexture()));
			d_glyphImages.push_back(hzImageset);
			assert(!hzmem_buffer);
			hzmem_buffer = new uchar[texsize * texsize];
			for (int i = 0; i < texsize * texsize; i++)
				hzmem_buffer[i] = 0x0;

			m_nHZX = m_nHZY = m_nHZYB = INTER_GLYPH_PAD_SPACE;
		}

		d_hz_map[codepoint] = FontGlyph(0.0f);

		CodepointMap::const_iterator hzInter = d_hz_map.find(codepoint);

		if (!hzInter->second.getImage())
		{
			if (FT_Load_Char(d_fontFace, hzInter->first, FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT | (d_antiAliased ? FT_LOAD_TARGET_NORMAL : FT_LOAD_TARGET_MONO)))
			{
				std::stringstream err;

				err << "Font::loadFreetypeGlyph - Failed to load glyph for codepoint: ";

				err << static_cast<unsigned int> (hzInter->first);

				err << ".  Will use an empty image for this glyph!";

				Logger::getSingleton().logEvent(err.str(), Errors);

				// Create a 'null' image for this glyph so we do not seg later 
				Rect area(0, 0, 0, 0);
				Point offset(0, 0);
				String name;
				name += hzInter->first;
				hzImageset->defineImage(name, area, offset);
				((FontGlyph &)hzInter->second).setImage(&hzImageset->getImage(name));
				float adv = d_fontFace->glyph->metrics.horiAdvance * float(FT_POS_COEF);
				d_hz_map[codepoint].setAdvance(adv);
			}
			else
			{
#ifndef FORCEGUIEDITOR
				FT_Face pTempFace = d_fontFace;
				int copyType = 1;
				bool bSuccess = true;
				FT_GlyphSlot slot = pTempFace->glyph;
				FT_Bitmap* bitmap = &slot->bitmap;
				int w = slot->metrics.horiAdvance >> 6;
				int h = w;
				uint glyph_w = bitmap->width + INTER_GLYPH_PAD_SPACE;
				uint glyph_h = bitmap->rows + INTER_GLYPH_PAD_SPACE;
				unsigned char* pData = bitmap->buffer;
				Point offset(slot->metrics.horiBearingX * static_cast<float>(FT_POS_COEF), -slot->metrics.horiBearingY * static_cast<float>(FT_POS_COEF));
				if (d_fontFace->glyph->bitmap.buffer == NULL && s_defaultFontFile.length() > 0)
				{
					if (s_defaultFontType == 1)
					{
						if (d_defFace == NULL)
						{
							FT_New_Face(ft_lib, s_defaultFontFile.c_str(), 0, &d_defFace);

							uint horzdpi = static_cast<uint>(System::getSingleton().getRenderer()->getDisplayDPI().d_x);
							uint vertdpi = static_cast<uint>(System::getSingleton().getRenderer()->getDisplayDPI().d_y);
							float hps = d_ptSize * 64 * 132.0f / horzdpi;
							float vps = d_ptSize * 64 * 132.0f / vertdpi;
							if (d_autoScale)
							{
								hps *= d_horzScaling;
								vps *= d_vertScaling;
							}
							FT_Set_Char_Size(d_defFace, FT_F26Dot6(hps), FT_F26Dot6(vps), horzdpi, vertdpi);
						}
						//FT_Set_Pixel_Sizes(ft_defFace, 0, 22);
						//FT_Activate_Size(ft_defSize);
						if (FT_Load_Char(d_defFace, hzInter->first, FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT | (d_antiAliased ? FT_LOAD_TARGET_NORMAL : FT_LOAD_TARGET_MONO)))
							bSuccess = false;
						pTempFace = d_defFace;
						slot = pTempFace->glyph;
						bitmap = &slot->bitmap;
						w = slot->metrics.horiAdvance >> 6;
						h = w;
						glyph_w = bitmap->width + INTER_GLYPH_PAD_SPACE;
						glyph_h = bitmap->rows + INTER_GLYPH_PAD_SPACE;
						pData = bitmap->buffer;
						offset = Point(slot->metrics.horiBearingX * static_cast<float>(FT_POS_COEF), -slot->metrics.horiBearingY * static_cast<float>(FT_POS_COEF));
					}
					else if (s_defaultFontType == 2)
					{
						std::wstring wws;
						wws = hzInter->first;
						std::string tmp = SHARE_Wstring2String(wws);
						GetTextData(tmp.c_str(), s_defaultFontFile, w, pData, glyph_w, glyph_h);
						glyph_w = glyph_w + INTER_GLYPH_PAD_SPACE;
						glyph_h = glyph_h + INTER_GLYPH_PAD_SPACE;
						//posX = (glyph_w - w) / 2;
						//posY = (glyph_h - w) / 2;
						//offset = Point(slot->metrics.horiBearingX * static_cast<float>(FT_POS_COEF), -slot->metrics.horiBearingY * static_cast<float>(FT_POS_COEF));
						offset = Point(INTER_GLYPH_PAD_SPACE, -w + INTER_GLYPH_PAD_SPACE * 2);
						copyType = 2;
					}
					else
						bSuccess = false;
				}
				if (bSuccess == false)
				{
					// Create a 'null' image for this glyph so we do not seg later 
					Rect area(0, 0, 0, 0);
					Point offset(0, 0);
					String name;
					name += hzInter->first;
					hzImageset->defineImage(name, area, offset);
					((FontGlyph &)hzInter->second).setImage(&hzImageset->getImage(name));
					float adv = d_fontFace->glyph->metrics.horiAdvance * float(FT_POS_COEF);
					d_hz_map[codepoint].setAdvance(adv);
				}
				else
				{
					// Check if glyph right margin does not exceed texture size   
					uint x_next = m_nHZX + glyph_w;

					if (x_next > texsize)
					{
						m_nHZX = INTER_GLYPH_PAD_SPACE;

						x_next = m_nHZX + glyph_w;

						m_nHZY = m_nHZYB;
					}

					// Check if glyph bottom margine does not exceed texture size  
					uint y_bot = m_nHZY + glyph_h;

					if (y_bot > texsize)
					{
						hzImageset = &(ImagesetManager::getSingleton().create(d_name + "_auto_glyph_images_" + int(codepoint), System::getSingleton().getRenderer()->createTexture()));
						d_glyphImages.push_back(hzImageset);

						if (hzmem_buffer)
						{
							delete[] hzmem_buffer;
						}
						hzmem_buffer = new uchar[texsize * texsize];
						for (int i = 0; i < texsize * texsize; i++)
							hzmem_buffer[i] = 0;

						m_nHZX = m_nHZY = m_nHZYB = INTER_GLYPH_PAD_SPACE;
						y_bot = m_nHZY + glyph_h;
					}

					// Copy rendered glyph to memory buffer in RGBA format  
					if (copyType == 1)
						drawGlyphToBuffer(hzmem_buffer + (m_nHZY * texsize) + m_nHZX, texsize, pTempFace);
					else if (copyType == 2)
					{
						int bmpW = glyph_w - INTER_GLYPH_PAD_SPACE;
						int bmpH = glyph_h - INTER_GLYPH_PAD_SPACE;
						int posX = (bmpW - w) / 2;
						int posY = (bmpH - h) / 2;
						uchar* pBase = hzmem_buffer + (m_nHZY * texsize) + m_nHZX;
						for (int i = 0; i < h; i++)
						{
							uchar* pSave = pBase;
							int ri = i + posY;
							if (ri < 0 || ri >= bmpH)	continue;
							for (int j = 0; j < w; j++)
							{
								int rj = j + posX;
								if (rj < 0 || rj >= bmpW)	continue;
								pSave[i* w + j] = *((unsigned int*)pData + (ri)* w + rj);
							}
							pBase += texsize - w;
						}
						if (pData != NULL)
						{
							delete[] pData;
							pData = NULL;
						}
					}

					copyRect.d_left = static_cast<float>(m_nHZX);
					copyRect.d_right = copyRect.d_left + glyph_w - INTER_GLYPH_PAD_SPACE;

					copyRect.d_top = static_cast<float>(m_nHZY);
					copyRect.d_bottom = copyRect.d_top + glyph_h - INTER_GLYPH_PAD_SPACE;

					// Create a new image in the imageset 
					Rect area(static_cast<float>(m_nHZX),
						static_cast<float>(m_nHZY),
						static_cast<float>(m_nHZX + glyph_w - INTER_GLYPH_PAD_SPACE),
						static_cast<float>(m_nHZY + glyph_h - INTER_GLYPH_PAD_SPACE));

					String name;
					name += hzInter->first;
					hzImageset->defineImage(name, area, offset);

					((FontGlyph &)hzInter->second).setImage(&hzImageset->getImage(name));
					float adv = pTempFace->glyph->metrics.horiAdvance * float(FT_POS_COEF);
					((FontGlyph &)hzInter->second).setAdvance(adv);

					m_nHZX = x_next;

					if (y_bot > m_nHZYB)
					{
						m_nHZYB = y_bot;
					}
				}
#else
				FT_Face pTempFace = d_fontFace;
				if (d_fontFace->glyph->bitmap.buffer == NULL && s_defaultFontFile.length() > 0)
				{
					if (d_defFace == NULL)
					{
						FT_New_Face(ft_lib, s_defaultFontFile.c_str(), 0, &d_defFace);

						uint horzdpi = static_cast<uint>(System::getSingleton().getRenderer()->getDisplayDPI().d_x);
						uint vertdpi = static_cast<uint>(System::getSingleton().getRenderer()->getDisplayDPI().d_y);
						float hps = d_ptSize * 64 * 132.0f / horzdpi;
						float vps = d_ptSize * 64 * 132.0f / vertdpi;
						if (d_autoScale)
						{
							hps *= d_horzScaling;
							vps *= d_vertScaling;
						}
						FT_Set_Char_Size(d_defFace, FT_F26Dot6(hps), FT_F26Dot6(vps), horzdpi, vertdpi);
					}
					//FT_Set_Pixel_Sizes(ft_defFace, 0, 22);
					//FT_Activate_Size(ft_defSize);
					if (FT_Load_Char(d_defFace, hzInter->first, FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT | (d_antiAliased ? FT_LOAD_TARGET_NORMAL : FT_LOAD_TARGET_MONO)))
						return;
					pTempFace = d_defFace;
				}
				uint glyph_w = pTempFace->glyph->bitmap.width + INTER_GLYPH_PAD_SPACE;

				uint glyph_h = pTempFace->glyph->bitmap.rows + INTER_GLYPH_PAD_SPACE;

				// Check if glyph right margin does not exceed texture size   

				uint x_next = m_nHZX + glyph_w;

				if (x_next > texsize)
				{
					m_nHZX = INTER_GLYPH_PAD_SPACE;

					x_next = m_nHZX + glyph_w;

					m_nHZY = m_nHZYB;
				}

				// Check if glyph bottom margine does not exceed texture size   

				uint y_bot = m_nHZY + glyph_h;

				if (y_bot > texsize)
				{
					hzImageset = &(ImagesetManager::getSingleton().create(d_name + "_auto_glyph_images_" + int(codepoint), System::getSingleton().getRenderer()->createTexture()));
					d_glyphImages.push_back(hzImageset);

					if (hzmem_buffer)
					{
						delete[] hzmem_buffer;
					}
					hzmem_buffer = new uchar[texsize * texsize];
					for (int i = 0; i < texsize * texsize; i++)
						hzmem_buffer[i]= 0xff;

					m_nHZX = m_nHZY = m_nHZYB = INTER_GLYPH_PAD_SPACE;
					y_bot = m_nHZY + glyph_h;
				}

				// Copy rendered glyph to memory buffer in RGBA format   

				drawGlyphToBuffer(hzmem_buffer + (m_nHZY * texsize) + m_nHZX, texsize, pTempFace);

				FT_Bitmap *glyph_bitmap = &pTempFace->glyph->bitmap;
				copyRect.d_left = static_cast<float>(m_nHZX);
				copyRect.d_right = copyRect.d_left + glyph_bitmap->width;

				copyRect.d_top = static_cast<float>(m_nHZY);
				copyRect.d_bottom = copyRect.d_top + glyph_bitmap->rows;

				// Create a new image in the imageset 
				Rect area(static_cast<float>(m_nHZX),

					static_cast<float>(m_nHZY),

					static_cast<float>(m_nHZX + glyph_w - INTER_GLYPH_PAD_SPACE),

					static_cast<float>(m_nHZY + glyph_h - INTER_GLYPH_PAD_SPACE));

				Point offset(pTempFace->glyph->metrics.horiBearingX * static_cast<float>(FT_POS_COEF), -pTempFace->glyph->metrics.horiBearingY * static_cast<float>(FT_POS_COEF));

				String name;

				name += hzInter->first;

				hzImageset->defineImage(name, area, offset);

				((FontGlyph &)hzInter->second).setImage(&hzImageset->getImage(name));
				float adv = pTempFace->glyph->metrics.horiAdvance * float(FT_POS_COEF);
				((FontGlyph &)hzInter->second).setAdvance(adv);

				m_nHZX = x_next;

				if (y_bot > m_nHZYB)
				{
					m_nHZYB = y_bot;
				}
#endif
			}
		}

		// Copy our memory buffer into the texture and free it 

		float fSize = static_cast<float>(texsize);

		if (hzImageset->getTexture())
			hzImageset->getTexture()->updataFromMemory(hzmem_buffer, CEGUI::Size(fSize, fSize), copyRect, Texture::PF_RGBA);
	}

#ifndef FORCEGUIEDITOR
	bool FreeTypeFont::GetTextData(const char* pChar, const String &font, int size, unsigned char*& pData, uint& w, uint& h)
	{
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
		cocos2d::CCImage* image = new cocos2d::CCImage();
		image->initWithString(pChar, size, size, cocos2d::CCImage::kAlignCenter, font.c_str(), size);
		w = image->getWidth();
		h = image->getHeight();
		pData = new unsigned char[w*h*sizeof(unsigned int)];
		memcpy(pData, image->getData(), image->getDataLen()*sizeof(unsigned int));
		delete image;
#else
		cocos2d::CCImage image;
		image.initWithString(pChar, size, size, cocos2d::CCImage::kAlignCenter, font.c_str(), size);
		w = image.getWidth();
		h = image.getHeight();
		pData = new unsigned char[w*h*sizeof(unsigned int)];
		memcpy(pData, image.getData(), image.getDataLen()*sizeof(unsigned int));
#endif
		return true;
	}
#endif
	//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
