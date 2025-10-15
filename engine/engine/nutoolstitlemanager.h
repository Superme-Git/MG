#ifdef XP_USE_TOOLS_TITLE_MAN

#ifndef __Nuclear_TOOLS_TITLE_MANAGER_H
#define __Nuclear_TOOLS_TITLE_MANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif
#include <map>
#include "..\renderer\renderer.h"
#include "..\iengine.h"
#include "..\ientitativetitlemanager.h"
#include "titlemanager.h"

namespace Nuclear
{
	enum 
	{
		TOOLS_TITLE_COUNT = 3,
	};
	struct ToolsTitle 
	{
		XHardRef<Title> title[TOOLS_TITLE_COUNT];
	};

	typedef std::map<EntitativeTitleHandle, ToolsTitle> ToolsTitleMap;

	class ToolsTitleManager :
		public IEntitativeTitleManager
	{
	private:
		EntitativeTitleHandle m_handleSeed;
		ToolsTitleMap m_titles;
		Renderer* m_pRenderer;
		IEngine* m_pEngine;
		TitleManager* m_pTitleMan;

	public:
		ToolsTitleManager(IEngine* pEngine);
		virtual ~ToolsTitleManager(void);

		//在绘制实体（Sprite、Immovableobj）的时候会调用这个方法。
		//逻辑那边可以在调用这个的时候就马上绘制，这个时候就在中间层，
		//也可以在这里仅仅做收集，之后在绘制完引擎，但是在所有UI之前绘制
		//pos为屏幕像素坐标
		virtual void RenderTitle(EntitativeTitleHandle title, const CPOINT &pos);

		//这个Location是世界像素坐标，在拾取检测的时候，当发现检测点落在了Title的范围内的时候，
		//就会调用这个方法进一步检测。当然，不是必须精确到像素
		virtual bool TestTitleAlpha(EntitativeTitleHandle title, const Location &testLoc);

		EntitativeTitleHandle CreateTitle();
		void ReleaseTitle(EntitativeTitleHandle handle);
		void SetTitle(EntitativeTitleHandle handle, int slot, const std::wstring &title, int fontType, uFireCOLOR color);
		void UnsetTitle(EntitativeTitleHandle handle, int slot);
		bool GetTitleSize(EntitativeTitleHandle handle, CPOINT &size);
		void ClearAllRes() { m_titles.clear(); }
		
	};

}

#endif
#endif