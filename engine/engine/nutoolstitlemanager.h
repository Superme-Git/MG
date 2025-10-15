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

		//�ڻ���ʵ�壨Sprite��Immovableobj����ʱ���������������
		//�߼��Ǳ߿����ڵ��������ʱ������ϻ��ƣ����ʱ������м�㣬
		//Ҳ����������������ռ���֮���ڻ��������棬����������UI֮ǰ����
		//posΪ��Ļ��������
		virtual void RenderTitle(EntitativeTitleHandle title, const CPOINT &pos);

		//���Location�������������꣬��ʰȡ����ʱ�򣬵����ּ���������Title�ķ�Χ�ڵ�ʱ��
		//�ͻ�������������һ����⡣��Ȼ�����Ǳ��뾫ȷ������
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