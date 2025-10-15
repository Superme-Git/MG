#include "stdafx.h"
#ifdef XP_USE_TOOLS_TITLE_MAN
#include "..\common\log.h"
#include "toolstitlemanager.h"
#include "titlemanager.h"

namespace Nuclear
{
	ToolsTitleManager::ToolsTitleManager(IEngine* pEngine) : m_handleSeed(INVALID_ENTITATIVE_TITLE_HANDLE), 
		m_pRenderer(NULL), m_pEngine(pEngine), m_pTitleMan(NULL)
	{
		m_pRenderer = pEngine->GetRenderer();
		m_pTitleMan = new TitleManager(m_pRenderer->GetFontManager());
	}

	ToolsTitleManager::~ToolsTitleManager(void)
	{
		delete m_pTitleMan;
	}

	void ToolsTitleManager::RenderTitle(EntitativeTitleHandle title, const CPOINT &pos)
	{
		ToolsTitleMap::iterator iter = m_titles.find(title);
		if (iter != m_titles.end())
		{
			IFontManager* pFontMan = m_pRenderer->GetFontManager();
			ToolsTitle &title = iter->second;
			CRECT rect;
			for(int i=0; i<TOOLS_TITLE_COUNT; ++i)
			{
				if (!title.title[i].IsValid())
					continue;

				pFontMan->DrawText(title.title[i]->text, 
					pos.x - static_cast<float>(title.title[i]->width) / 2, 
					pos.y - static_cast<float>((i+1)*20), NULL);
			}
		}
	}

	bool ToolsTitleManager::TestTitleAlpha(EntitativeTitleHandle title, const Location &testLoc)
	{
		ToolsTitleMap::iterator iter = m_titles.find(title);
		if (iter != m_titles.end())
		{
			ToolsTitle &title = iter->second;
			CRECT rect = m_pEngine->GetWorld()->GetViewport();
			Location pos = testLoc;
			pos.x -= rect.left;
			pos.y -= rect.top;
			for(int i=0; i<TOOLS_TITLE_COUNT; ++i)
			{
				if (!title.title[i].IsValid())
					continue;
			
				rect.left = pos.x-title.title[i]->width/2;
				rect.top = pos.y+i*20;
				rect.right = rect.left+title.title[i]->width;
				rect.bottom = rect.top+title.title[i]->height;
				if (rect.PtInRect(pos))
				{
					//XPTRACE(L"loc: %d, %d, ok\n", testLoc.x, testLoc.y);
					//XPTRACE(L"%s\n", title.title[0].title.c_str());
					return true;
				}
			}
		}
		return false;
	}

	EntitativeTitleHandle ToolsTitleManager::CreateTitle()
	{
		m_titles[++m_handleSeed] = ToolsTitle();
		return m_handleSeed;
	}

	void ToolsTitleManager::ReleaseTitle(EntitativeTitleHandle handle)
	{
		m_titles.erase(handle);
	}

	void ToolsTitleManager::SetTitle(EntitativeTitleHandle handle, int slot, const std::wstring &title, int fontType, XPCOLOR color)
	{
		XPASSERT(slot >= 0 && slot < TOOLS_TITLE_COUNT);
		ToolsTitleMap::iterator iter = m_titles.find(handle);
		if (iter != m_titles.end())
		{
			ToolsTitle &ttitle = iter->second;
			ttitle.title[slot] = m_pTitleMan->GetTitle(title, fontType, color.data);
		}

	}

	void ToolsTitleManager::UnsetTitle(EntitativeTitleHandle handle, int slot)
	{
		ToolsTitleMap::iterator iter = m_titles.find(handle);
		if (iter != m_titles.end())
		{
			ToolsTitle &title = iter->second;
			title.title[slot] = XHardRef<Title>();
		}
	}

	bool ToolsTitleManager::GetTitleSize(EntitativeTitleHandle handle, CPOINT &size)
	{
		ToolsTitleMap::iterator iter = m_titles.find(handle);
		size.x = 0;
		size.y = 20 * TOOLS_TITLE_COUNT;
		if (iter != m_titles.end())
		{
			ToolsTitle &title = iter->second;
			for(int i=0; i<TOOLS_TITLE_COUNT; ++i)
			{
				if (!title.title[i].IsValid())
					continue;

				if (size.x < title.title[i]->width)
					size.x = title.title[i]->width;
			}
			return true;
		}
		return false;		
	}
}
#endif