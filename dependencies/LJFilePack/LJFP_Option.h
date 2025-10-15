#ifndef LJFP_Option_H
#define LJFP_Option_H

#include "LJFP_Var.h"
#include "LJFP_XML.h"
#include "LJFP_Version.h"
#include "../../common/platform/utils/StringCover.h"
#include "shlwapi.h"
#pragma comment(lib,"shlwapi.lib")

LJFP_NodeEx* g_pOption;
std::wstring g_strOptionFileName;

unsigned int g_Version;
std::wstring g_VersionCaption;
unsigned int g_VersionBase;
std::wstring g_VersionCaptionBase;
unsigned int g_VersionMinimum;
std::wstring g_VersionCaptionMinimum;
unsigned int g_VersionDonotCheck;
unsigned int g_Channel;
std::wstring g_ChannelCaption;
std::wstring g_AppURL;

URLInfoArr m_URLInfoArr;

std::map<std::wstring, std::wstring> g_FilterDirName;
std::map<std::wstring, std::wstring> g_FilterDirNameFull;
std::map<std::wstring, std::wstring> g_FilterFileName;
std::map<std::wstring, std::wstring> g_FilterFileNameFull;
std::map<std::wstring, std::wstring> g_FilterFileType;
std::map<std::wstring, std::wstring> g_UnPackFileName;
std::map<std::wstring, std::wstring> g_UnPackFileNameFull;
std::map<std::wstring, std::wstring> g_UnPackFileType;
std::map<std::wstring, std::wstring> g_UnCompressFileName;
std::map<std::wstring, std::wstring> g_UnCompressFileNameFull;
std::map<std::wstring, std::wstring> g_UnCompressFileType;
std::map<std::wstring, std::wstring> g_UnCodeFileName;
std::map<std::wstring, std::wstring> g_UnCodeFileNameFull;
std::map<std::wstring, std::wstring> g_UnCodeFileType;
std::map<std::wstring, std::wstring> g_Extend;

class LJFP_Option
{
public:
	static int MakeDefaultOption(LJFP_NodeEx*& pNode)
	{
		LJFP_NodeEx* pNode0;
		LJFP_NodeEx* pNode1;
		LJFP_NodeEx* pNode2;
		LJFP_NodeEx* pNode3;
		LJFP_NodeEx* pNode4;
		if (!pNode)
		{
			pNode = new LJFP_NodeEx(L"Root");
		}
		pNode0 = pNode;
		{
			pNode1 = pNode0->AddNode(L"Version", L"");
			pNode1->AddAttr(L"Count", L"2");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"IOS");
				{
					pNode3 = pNode2->AddNode(L"VersionInfo", L"");
					pNode3->AddAttr(L"VersionDonotCheck", L"0");
					pNode3->AddAttr(L"VersionCaption", L"0.0.1");
					pNode3->AddAttr(L"VersionCaptionBase", L"0.0.1");
					pNode3->AddAttr(L"VersionCaptionMinimum", L"0.0.1");
				}
			}
			{
				pNode2 = pNode1->AddNode(L"1", L"");
				pNode2->AddAttr(L"Description", L"Android");
				{
					pNode3 = pNode2->AddNode(L"VersionInfo", L"");
					pNode3->AddAttr(L"VersionDonotCheck", L"0");
					pNode3->AddAttr(L"VersionCaption", L"0.0.1");
					pNode3->AddAttr(L"VersionCaptionBase", L"0.0.1");
					pNode3->AddAttr(L"VersionCaptionMinimum", L"0.0.1");
				}
			}
		}
		{
			pNode1 = pNode0->AddNode(L"Update", L"");
			pNode1->AddAttr(L"Count", L"2");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"IOS");
				{
					pNode3 = pNode2->AddNode(L"URLInfo", L"");
					pNode3->AddAttr(L"Count", L"1");
					pNode3->AddAttr(L"AppURL", L"http://store.apple.com/123/456/789/0");
					{
						pNode4 = pNode3->AddNode(L"0", L"");
						pNode4->AddAttr(L"URL", L"http://192.168.29.164:8090/ios/");
						pNode4->AddAttr(L"System", L"ios");
						pNode4->AddAttr(L"Network", L"lan");
					}
				}
			}
			{
				pNode2 = pNode1->AddNode(L"1", L"");
				pNode2->AddAttr(L"Description", L"Android");
				{
					pNode3 = pNode2->AddNode(L"URLInfo", L"");
					pNode3->AddAttr(L"Count", L"1");
					pNode3->AddAttr(L"AppURL", L"http://store.google.com/123/456/789/0");
					{
						pNode4 = pNode3->AddNode(L"0", L"");
						pNode4->AddAttr(L"URL", L"http://192.168.29.164:8090/android/");
						pNode4->AddAttr(L"System", L"android");
						pNode4->AddAttr(L"Network", L"lan");
					}
				}
			}
		}
		{
			pNode1 = pNode0->AddNode(L"Channel", L"");
			pNode1->AddAttr(L"Count", L"2");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"IOS_Locojoy");
				{
					pNode3 = pNode2->AddNode(L"ChannelInfo", L"");
					pNode3->AddAttr(L"Channel", L"0");
					pNode3->AddAttr(L"ChannelCaption", L"IOS_Locojoy");
				}
			}
			{
				pNode2 = pNode1->AddNode(L"1", L"");
				pNode2->AddAttr(L"Description", L"Android_Locojoy");
				{
					pNode3 = pNode2->AddNode(L"ChannelInfo", L"");
					pNode3->AddAttr(L"Channel", L"0");
					pNode3->AddAttr(L"ChannelCaption", L"Android_Locojoy");
				}
			}
		}
		{
			pNode1 = pNode0->AddNode(L"Extend", L"");
			pNode1->AddAttr(L"Count", L"2");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"IOS");
				{
					pNode3 = pNode2->AddNode(L"ExtendInfo", L"");
					pNode3->AddAttr(L"Test1", L"0");
					pNode3->AddAttr(L"Test2", L"0.0.1");
				}
			}
			{
				pNode2 = pNode1->AddNode(L"1", L"");
				pNode2->AddAttr(L"Description", L"Android");
				{
					pNode3 = pNode2->AddNode(L"ExtendInfo", L"");
					pNode3->AddAttr(L"Test1", L"0");
					pNode3->AddAttr(L"Test2", L"0.0.1");
				}
			}
		}
		{
			pNode1 = pNode0->AddNode(L"IO", L"");
			pNode1->AddAttr(L"Count", L"4");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"Some texts");
				pNode2->AddAttr(L"FindPath", L"Root/");
				pNode2->AddAttr(L"OutputPath", L"IOS_Pack/");
				pNode2->AddAttr(L"OutputType", L"Pack");
			}
			{
				pNode2 = pNode1->AddNode(L"1", L"");
				pNode2->AddAttr(L"Description", L"Some texts");
				pNode2->AddAttr(L"FindPath", L"Root/");
				pNode2->AddAttr(L"OutputPath", L"IOS_File/");
				pNode2->AddAttr(L"OutputType", L"File");
			}
			{
				pNode2 = pNode1->AddNode(L"2", L"");
				pNode2->AddAttr(L"Description", L"Some texts");
				pNode2->AddAttr(L"FindPath", L"Root/");
				pNode2->AddAttr(L"OutputPath", L"Android_Pack/");
				pNode2->AddAttr(L"OutputType", L"Pack");
			}
			{
				pNode2 = pNode1->AddNode(L"3", L"");
				pNode2->AddAttr(L"Description", L"Some texts");
				pNode2->AddAttr(L"FindPath", L"Root/");
				pNode2->AddAttr(L"OutputPath", L"Android_File/");
				pNode2->AddAttr(L"OutputType", L"File");
			}
		}
		{
			pNode1 = pNode0->AddNode(L"Filter", L"");
			pNode1->AddAttr(L"Count", L"1");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"Some texts");
				{
					pNode3 = pNode2->AddNode(L"FilterFileType", L"4");
					pNode3->AddAttr(L"0", L"db");
					pNode3->AddAttr(L"1", L"ilk");
					pNode3->AddAttr(L"2", L"pdb");
					pNode3->AddAttr(L"3", L"exe");
				}
				{
					pNode3 = pNode2->AddNode(L"FilterFileName", L"0");
				}
				{
					pNode3 = pNode2->AddNode(L"FilterFileNameFull", L"0");
				}
				{
					pNode3 = pNode2->AddNode(L"FilterDirName", L"0");
				}
				{
					pNode3 = pNode2->AddNode(L"FilterDirNameFull", L"1");
					pNode3->AddAttr(L"0", L"config/autoconfig");
				}
			}
		}
		{
			pNode1 = pNode0->AddNode(L"Pack", L"");
			pNode1->AddAttr(L"Count", L"1");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"Some texts");
				pNode2->AddAttr(L"MaxSize", L"52428800");//1024 * 1024 * 50
				{
					pNode3 = pNode2->AddNode(L"UnPackFileType", L"4");
					pNode3->AddAttr(L"0", L"ogg");
					pNode3->AddAttr(L"1", L"mp3");
					pNode3->AddAttr(L"2", L"mp4");
					pNode3->AddAttr(L"3", L"ini");
				}
				{
					pNode3 = pNode2->AddNode(L"UnPackFileName", L"0");
				}
				{
					pNode3 = pNode2->AddNode(L"UnPackFileNameFull", L"3");
					pNode3->AddAttr(L"0", L"cfg/mount_android.xml");
					pNode3->AddAttr(L"1", L"cfg/mount_ios.xml");
					pNode3->AddAttr(L"2", L"cfg/mount_win.xml");
				}
			}
		}
		{
			pNode1 = pNode0->AddNode(L"Compress", L"");
			pNode1->AddAttr(L"Count", L"1");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"Some texts");
				{
					pNode3 = pNode2->AddNode(L"UnCompressFileType", L"5");
					pNode3->AddAttr(L"0", L"ogg");
					pNode3->AddAttr(L"1", L"mp3");
					pNode3->AddAttr(L"2", L"mp4");
					pNode3->AddAttr(L"3", L"ini");
					pNode3->AddAttr(L"4", L"png");
				}
				{
					pNode3 = pNode2->AddNode(L"UnCompressFileName", L"0");
				}
				{
					pNode3 = pNode2->AddNode(L"UnCompressFileNameFull", L"3");
					pNode3->AddAttr(L"0", L"cfg/mount_android.xml");
					pNode3->AddAttr(L"1", L"cfg/mount_ios.xml");
					pNode3->AddAttr(L"2", L"cfg/mount_win.xml");
				}
			}
		}
		{
			pNode1 = pNode0->AddNode(L"Code", L"");
			pNode1->AddAttr(L"Count", L"1");
			{
				pNode2 = pNode1->AddNode(L"0", L"");
				pNode2->AddAttr(L"Description", L"Some texts");
				{
					pNode3 = pNode2->AddNode(L"UnCodeFileType", L"4");
					pNode3->AddAttr(L"0", L"ogg");
					pNode3->AddAttr(L"1", L"mp3");
					pNode3->AddAttr(L"2", L"mp4");
					pNode3->AddAttr(L"3", L"ini");
				}
				{
					pNode3 = pNode2->AddNode(L"UnCodeFileName", L"0");
				}
				{
					pNode3 = pNode2->AddNode(L"UnCodeFileNameFull", L"3");
					pNode3->AddAttr(L"0", L"cfg/mount_android.xml");
					pNode3->AddAttr(L"1", L"cfg/mount_ios.xml");
					pNode3->AddAttr(L"2", L"cfg/mount_win.xml");
				}
			}
		}
		return 0;
	}
	static int CheckOption(LJFP_NodeEx* pNode)
	{
		pNode;
		return 0;
	}
	static int SaveOption(std::wstring strFileName, LJFP_NodeEx* pNode)
	{
		int iResult = pNode->SaveToXMLFile(strFileName, pNode);
		if (iResult != 0)
		{
			return iResult;
		}
		return 0;
	}
	static int LoadOption(std::wstring strFileName, LJFP_NodeEx*& pNode)
	{
		g_strOptionFileName = strFileName;
		BOOL bResult = PathFileExistsW(strFileName.c_str());
		if (!bResult)
		{
			MakeDefaultOption(pNode);
			SaveOption(strFileName, pNode);
			return -1;
		}
		int iResult = pNode->LoadFromXMLFile(strFileName, pNode);
		if (iResult != 0)
		{
			MakeDefaultOption(pNode);
			SaveOption(strFileName, pNode);
			return -2;
		}
		if (CheckOption(pNode) != 0)
		{
			MakeDefaultOption(pNode);
			SaveOption(strFileName, pNode);
			return -3;
		}
		
		return 0;
	}
	static int LoadOption(std::wstring strFileName)
	{
		if (g_pOption)
		{
			delete g_pOption;
		}
		return LoadOption(strFileName, g_pOption);
	}
	static int InitOptionOne(std::wstring Node1Name, std::wstring Node2Name, std::wstring Node3Name, std::map<std::wstring, std::wstring>& AttrMap)
	{
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		LJFP_NodeEx* pNode3 = NULL;
		unsigned int AttrCount = 0;
		pNode1 = g_pOption->FindNode(Node1Name);
		if (!pNode1)
		{
			return -1;
		}
		pNode2 = pNode1->FindNode(Node2Name);
		if (!pNode1)
		{
			return -1;
		}
		pNode3 = pNode2->FindNode(Node3Name);
		if (!pNode3)
		{
			return -1;
		}
		AttrCount = GetStringUtil().WS2UI(pNode3->m_Text);
		for (unsigned int i = 0; i < AttrCount; i++)
		{
			std::wstring AttrValue = pNode3->FindAttrValue(GetStringUtil().UI2WS(i));
			std::transform(AttrValue.begin(), AttrValue.end(), AttrValue.begin(), ::tolower);
			AttrMap[AttrValue] = L"";
		}
		return 0;
	}
	static int InitVersion(std::wstring OptionIndexVersion)
	{
		LJFP_NodeEx* pNode1 = g_pOption->FindNode(L"Version");
		if (!pNode1)
		{
			return -1;
		}
		else
		{
			LJFP_NodeEx* pNode2 = pNode1->FindNode(OptionIndexVersion);
			if (!pNode2)
			{
				return -1;
			}
			else
			{
				LJFP_NodeEx* pNode3 = pNode2->FindNode(L"VersionInfo");
				if (!pNode3)
				{
					return -1;
				}
				g_VersionCaption = pNode3->FindAttrValue(L"VersionCaption");
				if (g_VersionCaption == L"")
				{
					return -1;
				}
				g_VersionCaptionBase = pNode3->FindAttrValue(L"VersionCaptionBase");
				if (g_VersionCaptionBase == L"")
				{
					return -1;
				}
				g_VersionCaptionMinimum = pNode3->FindAttrValue(L"VersionCaptionMinimum");
				if (g_VersionCaptionMinimum == L"")
				{
					return -1;
				}
				LJFP_Version V;
				g_Version = V.VersionCaption2Version(g_VersionCaption);
				g_VersionBase = V.VersionCaption2Version(g_VersionCaptionBase);
				g_VersionMinimum = V.VersionCaption2Version(g_VersionCaptionMinimum);
				if (g_Version == 0 || g_VersionBase == 0 || g_VersionMinimum == 0)
				{
					return -1;
				}
				g_VersionDonotCheck = GetStringUtil().WS2UI(pNode3->FindAttrValue(L"VersionDonotCheck"));
			}
		}
		return 0;
	}
	static int InitUpdate(std::wstring OptionIndexUpdate)
	{
		LJFP_NodeEx* pNode1;
		LJFP_NodeEx* pNode2;
		LJFP_NodeEx* pNode3;
		LJFP_NodeEx* pNode4;

		pNode1 = g_pOption->FindNode(L"Update");
		if (!pNode1)
		{
			return -1;
		}
		else
		{
			pNode2 = pNode1->FindNode(OptionIndexUpdate);
			if (!pNode2)
			{
				return -1;
			}
			else
			{
				pNode3 = pNode2->FindNode(L"URLInfo");
				if (!pNode3)
				{
					return -1;
				}
				else
				{
					g_AppURL = pNode3->FindAttrValue(L"AppURL");
					std::wstring wsURLCount = pNode3->FindAttrValue(L"Count");
					if (wsURLCount == L"")
					{
						return -1;
					}
					unsigned int URLCount = 0;
					URLCount = GetStringUtil().WS2UI(wsURLCount);
					for (unsigned int i = 0; i < URLCount; i++)
					{
						pNode4 = pNode3->FindNode(GetStringUtil().UI2WS(i));
						if (!pNode4)
						{
							return -1;
						}
						URLInfo UI;
						UI.m_URL = pNode4->FindAttrValue(L"URL");
						UI.m_System = pNode4->FindAttrValue(L"System");
						UI.m_Network = pNode4->FindAttrValue(L"Network");
						//std::transform(UI.m_URL.begin(), UI.m_URL.end(), UI.m_URL.begin(), ::tolower);
						std::transform(UI.m_System.begin(), UI.m_System.end(), UI.m_System.begin(), ::tolower);
						std::transform(UI.m_Network.begin(), UI.m_Network.end(), UI.m_Network.begin(), ::tolower);
						m_URLInfoArr.push_back(UI);
					}
				}
			}
		}
		return 0;
	}
	static int InitChannel(std::wstring OptionIndexChannel)
	{
		LJFP_NodeEx* pNode1 = g_pOption->FindNode(L"Channel");
		if (!pNode1)
		{
			return -1;
		}
		else
		{
			LJFP_NodeEx* pNode2 = pNode1->FindNode(OptionIndexChannel);
			if (!pNode2)
			{
				return -1;
			}
			else
			{
				LJFP_NodeEx* pNode3 = pNode2->FindNode(L"ChannelInfo");
				if (!pNode3)
				{
					return -1;
				}
				g_ChannelCaption = pNode3->FindAttrValue(L"ChannelCaption");
				if (g_ChannelCaption == L"")
				{
					return -1;
				}
				std::wstring wsChannel = pNode3->FindAttrValue(L"Channel");
				if (wsChannel == L"")
				{
					return -1;
				}
				g_Channel = GetStringUtil().WS2UI(wsChannel);
			}
		}
		return 0;
	}
	static int InitExtend(std::wstring OptionIndexExtend)
	{
		g_Extend.clear();
		LJFP_NodeEx* pNode1 = g_pOption->FindNode(L"Extend");
		if (!pNode1)
		{
			return -1;
		}
		else
		{
			LJFP_NodeEx* pNode2 = pNode1->FindNode(OptionIndexExtend);
			if (!pNode2)
			{
				return -1;
			}
			else
			{
				LJFP_NodeEx* pNode3 = pNode2->FindNode(L"ExtendInfo");
				if (!pNode3)
				{
					return -1;
				}
				for (unsigned int i = 0; i < pNode3->GetAttrCount(); i++)
				{
					LJFP_Attr* pAttr = pNode3->GetAttr(i);
					g_Extend[pAttr->m_Key] = pAttr->m_Value;
				}
			}
		}
		return 0;
	}
	static int InitOption(std::wstring OptionIndexVersion, std::wstring OptionIndexUpdate, std::wstring OptionIndexChannel, std::wstring OptionIndexExtend, std::wstring OptionIndexFilter, std::wstring OptionIndexPack, std::wstring OptionIndexCompress, std::wstring OptionIndexCode)
	{
		if (InitVersion(OptionIndexVersion) != 0){ return -99; }
		if (InitUpdate(OptionIndexUpdate) != 0){ return -98; }
		if (InitChannel(OptionIndexChannel) != 0){ return -97; }
		if (InitExtend(OptionIndexExtend) != 0){ return -96; }
		if (InitOptionOne(L"Filter", OptionIndexFilter, L"FilterDirName", g_FilterDirName) != 0){ return -1; }
		if (InitOptionOne(L"Filter", OptionIndexFilter, L"FilterDirNameFull", g_FilterDirNameFull) != 0){ return -2; }
		if (InitOptionOne(L"Filter", OptionIndexFilter, L"FilterFileName", g_FilterFileName) != 0){ return -3; }
		if (InitOptionOne(L"Filter", OptionIndexFilter, L"FilterFileNameFull", g_FilterFileNameFull) != 0){ return -4; }
		if (InitOptionOne(L"Filter", OptionIndexFilter, L"FilterFileType", g_FilterFileType) != 0){ return -5; }
		if (InitOptionOne(L"Pack", OptionIndexPack, L"UnPackFileName", g_UnPackFileName) != 0){ return -6; }
		if (InitOptionOne(L"Pack", OptionIndexPack, L"UnPackFileNameFull", g_UnPackFileNameFull) != 0){ return -7; }
		if (InitOptionOne(L"Pack", OptionIndexPack, L"UnPackFileType", g_UnPackFileType) != 0){ return -8; }
		if (InitOptionOne(L"Compress", OptionIndexCompress, L"UnCompressFileName", g_UnCompressFileName) != 0){ return -9; }
		if (InitOptionOne(L"Compress", OptionIndexCompress, L"UnCompressFileNameFull", g_UnCompressFileNameFull) != 0){ return -10; }
		if (InitOptionOne(L"Compress", OptionIndexCompress, L"UnCompressFileType", g_UnCompressFileType) != 0){ return -11; }
		if (InitOptionOne(L"Code", OptionIndexCode, L"UnCodeFileName", g_UnCodeFileName) != 0){ return -12; }
		if (InitOptionOne(L"Code", OptionIndexCode, L"UnCodeFileNameFull", g_UnCodeFileNameFull) != 0){ return -13; }
		if (InitOptionOne(L"Code", OptionIndexCode, L"UnCodeFileType", g_UnCodeFileType) != 0){ return -14; }
		return 0;
	}
	static int IsMapMember(std::wstring strName, std::map<std::wstring, std::wstring>& AttrMap)
	{
		if (AttrMap.find(strName) != AttrMap.end())
		{
			return 1;
		}
		return 0;
	}
	static int IsFilterDir(std::wstring strParentPathName, std::wstring strDirName)
	{
		if (IsMapMember(strDirName, g_FilterDirName) != 0 || IsMapMember(strParentPathName + strDirName, g_FilterDirNameFull) != 0)
		{
			return 1;
		}
		return 0;
	}
	static int IsFilterFile(std::wstring strParentPathName, std::wstring strFileName)
	{
		std::wstring strFileNameExt = GetFileUtil().GetFileNameExt(strFileName);
		if (IsMapMember(strFileName, g_FilterFileName) != 0 || 
			IsMapMember(strParentPathName + strFileName, g_FilterFileNameFull) != 0 ||
			IsMapMember(strFileNameExt, g_FilterFileType) != 0)
		{
			return 1;
		}
		return 0;
	}
	static int IsPackFile(std::wstring strParentPathName, std::wstring strFileName)
	{
		std::wstring strFileNameExt = GetFileUtil().GetFileNameExt(strFileName);
		if (IsMapMember(strFileName, g_UnPackFileName) != 0 ||
			IsMapMember(strParentPathName + strFileName, g_UnPackFileNameFull) != 0 ||
			IsMapMember(strFileNameExt, g_UnPackFileType) != 0)
		{
			return 0;
		}
		return 1;
	}
	static int IsCompressFile(std::wstring strParentPathName, std::wstring strFileName)
	{
		std::wstring strFileNameExt = GetFileUtil().GetFileNameExt(strFileName);
		if (IsMapMember(strFileName, g_UnCompressFileName) != 0 ||
			IsMapMember(strParentPathName + strFileName, g_UnCompressFileNameFull) != 0 ||
			IsMapMember(strFileNameExt, g_UnCompressFileType) != 0)
		{
			return 0;
		}
		return 1;
	}
	static int IsCodeFile(std::wstring strParentPathName, std::wstring strFileName)
	{
		std::wstring strFileNameExt = GetFileUtil().GetFileNameExt(strFileName);
		if (IsMapMember(strFileName, g_UnCodeFileName) != 0 ||
			IsMapMember(strParentPathName + strFileName, g_UnCodeFileNameFull) != 0 ||
			IsMapMember(strFileNameExt, g_UnCodeFileType) != 0)
		{
			return 0;
		}
		return 1;
	}
	static std::wstring GetFindPath(std::wstring OptionIndexIO)
	{
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		pNode1 = g_pOption->FindNode(L"IO");
		if (!pNode1)
		{
			return L"";
		}
		pNode2 = pNode1->FindNode(OptionIndexIO);
		if (!pNode2)
		{
			return L"";
		}
		return pNode2->FindAttrValue(L"FindPath");
	}
	static std::wstring GetOutputPath(std::wstring OptionIndexIO)
	{
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		pNode1 = g_pOption->FindNode(L"IO");
		if (!pNode1)
		{
			return L"";
		}
		pNode2 = pNode1->FindNode(OptionIndexIO);
		if (!pNode2)
		{
			return L"";
		}
		return pNode2->FindAttrValue(L"OutputPath");
	}
	static bool GetOutputType(std::wstring OptionIndexIO)
	{
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		pNode1 = g_pOption->FindNode(L"IO");
		if (!pNode1)
		{
			return L"";
		}
		pNode2 = pNode1->FindNode(OptionIndexIO);
		if (!pNode2)
		{
			return L"";
		}
		std::wstring wsOutputType = pNode2->FindAttrValue(L"OutputType");
		if (wsOutputType.compare(L"Pack") == 0)
		{
			return true;
		}
		return false;
	}
	static unsigned int GetPackMaxSize(std::wstring OptionIndexPack)
	{
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		pNode1 = g_pOption->FindNode(L"Pack");
		if (!pNode1)
		{
			return 0;
		}
		pNode2 = pNode1->FindNode(OptionIndexPack);
		if (!pNode2)
		{
			return 0;
		}
		std::wstring sMaxSize = pNode2->FindAttrValue(L"MaxSize");
		if (sMaxSize != L"")
		{
			unsigned int MaxSize = GetStringUtil().WS2UI(sMaxSize);
			return MaxSize;
		}
		return 0;
	}
	static std::wstring GetVersionCaption()
	{
		return g_VersionCaption;
	}
	static unsigned int GetVersion()
	{
		return g_Version;
	}
	static std::wstring GetVersionCaptionBase()
	{
		return g_VersionCaptionBase;
	}
	static unsigned int GetVersionBase()
	{
		return g_VersionBase;
	}
	static std::wstring GetVersionCaptionMinimum()
	{
		return g_VersionCaptionMinimum;
	}
	static unsigned int GetVersionMinimum()
	{
		return g_VersionMinimum;
	}
	void SetVersionDonotCheck(unsigned int VersionDonotCheck)
	{
		g_VersionDonotCheck = VersionDonotCheck;
	}
	unsigned int GetVersionDonotCheck()
	{
		return g_VersionDonotCheck;
	}
	void SetChannel(unsigned int uiChannel)
	{
		g_Channel = uiChannel;
	}
	unsigned int GetChannel()
	{
		return g_Channel;
	}
	void SetChannelCaption(std::wstring ChannelCaption)
	{
		g_ChannelCaption = ChannelCaption;
	}
	std::wstring GetChannelCaption()
	{
		return g_ChannelCaption;
	}
	void SetAppURL(std::wstring AppURL)
	{
		g_AppURL = AppURL;
	}
	std::wstring GetAppURL()
	{
		return g_AppURL;
	}
	unsigned int GetExtendCount()
	{
		return g_Extend.size();
	}
	std::wstring GetExtendKey(unsigned int uiIndex)
	{
		if (uiIndex >= GetExtendCount())
		{
			return L"";
		}
		std::map<std::wstring, std::wstring>::iterator it = g_Extend.begin();
		for (unsigned int i = 0; i < uiIndex; i++)
		{
			it++;
		}
		return it->first;
	}
	std::wstring GetExtendValue(unsigned int uiIndex)
	{
		if (uiIndex >= GetExtendCount())
		{
			return L"";
		}
		std::map<std::wstring, std::wstring>::iterator it = g_Extend.begin();
		for (unsigned int i = 0; i < uiIndex; i++)
		{
			it++;
		}
		return it->second;
	}
	bool GetExtendKeyAndValue(unsigned int uiIndex, std::wstring& wsKey, std::wstring& wsValue)
	{
		if (uiIndex >= GetExtendCount())
		{
			return false;
		}
		std::map<std::wstring, std::wstring>::iterator it = g_Extend.begin();
		for (unsigned int i = 0; i < uiIndex; i++)
		{
			it++;
		}
		wsKey = it->first;
		wsValue = it->second;
		return true;
	}
	std::map<std::wstring, std::wstring> GetExtendMap()
	{
		return g_Extend;
	}
	static URLInfoArr GetUpdateURLInfoArr()
	{
		return m_URLInfoArr;
	}
	static int ReleaseOption()
	{
		if (g_pOption)
		{
			delete g_pOption;
			g_pOption = NULL;
		}
		return 0;
	}
};
LJFP_Option GetOption()
{
	LJFP_Option Result;
	return Result;
}
#endif //LJFP_Option_H