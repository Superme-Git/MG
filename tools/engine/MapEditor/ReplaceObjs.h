#ifndef REPLACE_OBJS_H
#define REPLACE_OBJS_H
#pragma once

#include "ToolsMap.h"
#include "..\engine\map\pimg.h"
#include "..\engine\map\plinkedobject.h"

class CReplaceObjs
{

private:
	typedef struct _MapInfo
	{
		std::wstring m_Uri;
		CToolsMap* m_pMap;
	} MapInfo;
	typedef std::vector<MapInfo> MapInfoArray;
	typedef std::map<std::wstring, std::wstring> ReplaceMap;
	typedef std::map<std::wstring, Nuclear::PImg> ImgMap;
	typedef std::map<std::wstring, Nuclear::PLinkedObject> LinkedObjectMap;


	ReplaceMap m_LinkedObjectToImgReplaceMap;
	ReplaceMap m_ImgToLinkedObjectReplaceMap;
	MapInfoArray m_MapInfos;
	ImgMap m_ImgMap;
	LinkedObjectMap m_LinkedObjectMap;


public:
	~CReplaceObjs(void);
	CReplaceObjs(void);

private:
	void DoReplace();
	void ReleaseMapInfos();
	void ResetMapInfos();
	void LoadMapInfoFromXML(const std::wstring &name);

public:
	void Do(const std::wstring &name);
};
#endif