#ifndef LauncherData_H
#define LauncherData_H
#include "stdafx.h"
#include "Utils/SystemUtil.h"
#include "Utils/FileUtil.h"
#include "LJFP/LJFP_StringUtil.h"
#include "LJFP/LJFP_XML.h"

using namespace std;

extern map<wstring, wstring> g_StringMap;

namespace LauncherData
{
	static int InitData()
	{
		wstring wsBinReleasePath = L"bin/release/";
		wstring wsLauncherDataFileName = wsBinReleasePath + L"LauncherData.xml";
		LJFP_NodeEx* pNode0 = NULL;
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		int iResult = 0;	
		iResult = pNode0->LoadFromXMLFile(wsLauncherDataFileName, pNode0);
		if (iResult != 0)
		{
			return iResult;
		}
		pNode1 = pNode0->FindNode(L"StringMap");
		unsigned int uiValue = GetStringUtil().WS2UI(pNode1->FindAttrValue(L"Count"));
		for (int i = 0; i < uiValue; i++)
		{
			pNode2 = pNode1->FindNode(GetStringUtil().UI2WS(i));
			wstring wsName = pNode2->FindAttrValue(L"Name");
			wstring wsValue = pNode2->FindAttrValue(L"Value");
			g_StringMap[wsName] = wsValue;
		}
		delete pNode0;
		return 0;
	}
	static wstring FindStringByKey(wstring wsKey)
	{
		return g_StringMap[wsKey];
	}
		

}


#endif