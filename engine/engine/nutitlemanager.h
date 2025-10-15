#ifdef XP_USE_TOOLS_TITLE_MAN
#ifndef __Nuclear_TitleManager_H
#define __Nuclear_TitleManager_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xref.h"
#include "..\common\xptypes.h"
#include "..\renderer\ifontmanager.h"

namespace Nuclear
{
	struct Title
	{
		IFontManager* pFontMan;
		TextBlockHandle text;
		int width;
		int height;
		Title(IFontManager* pFM) : pFontMan(pFM), text(INVALID_TEXTBLOCK_HANDLE), width(0), height(0) {}
		~Title() { pFontMan->ReleaseTextBlock(text); }
	};

	class TitleManager
	{
	private:
		typedef struct _TitleKey
		{
			std::wstring title;
			int fontType;
			DWORD color;
			bool operator< (const _TitleKey& x) const
			{
				if (fontType<x.fontType)
					return true;
				else if (fontType != x.fontType)
					return false;
				if (color<x.color)
					return true;
				else if (color != x.color)
					return false;
				if (title<x.title)
					return true;
				
				return false;
			}
			_TitleKey() : title(L""), fontType(0), color(0) {}
			_TitleKey(std::wstring t, int f, DWORD c) : title(t), fontType(f), color(c) {}
		} TitleKey;

		typedef std::map<TitleKey, XWeakRef<Title>> TitleMap;

	private:
		TitleMap m_titles;
		IFontManager* m_pFontMan;

	public:
		TitleManager(IFontManager* pFontMan) : m_pFontMan(pFontMan) {}
		XHardRef<Title> GetTitle(const std::wstring &title, int fontType, DWORD color)
		{
			TitleKey key(title, fontType, color);
			TitleMap::iterator iter = m_titles.find(key);
			if (iter != m_titles.end())
			{
				XHardRef<Title> result = iter->second;
				if (result.IsValid())
					return result;
				m_titles.erase(iter);
			}
			Title* pTitle = new Title(m_pFontMan);
			pTitle->text = m_pFontMan->NewText(title.c_str(), fontType, color, color);
			if (pTitle->text == INVALID_TEXTBLOCK_HANDLE)
			{
				delete pTitle;
				return XHardRef<Title>();
			}
			ITextBlock* pTextBlock = m_pFontMan->GetTextBlock(pTitle->text);
			pTitle->width = pTextBlock->GetTextWidth();
			pTitle->height = pTextBlock->GetTextHeight();
			XHardRef<Title> result(pTitle);
			m_titles[key] = result;
			return result;
		}
	};
}

#endif
#endif