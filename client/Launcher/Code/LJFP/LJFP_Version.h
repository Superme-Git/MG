#ifndef LJFP_Version_H
#define LJFP_Version_H

#include "LJFP_XML.h"

struct URLInfo
{
	std::wstring m_URL;
	std::wstring m_System;
	std::wstring m_Network;
};
typedef std::vector<URLInfo> URLInfoArr;

class LJFP_Version
{
	unsigned int m_uiVersion;
	unsigned int m_uiVersionBase;
	unsigned int m_uiVersionMinimum;
	std::wstring m_VersionCaption;
	std::wstring m_VersionCaptionBase;
	std::wstring m_VersionCaptionMinimum;
	unsigned int m_VersionDonotCheck;
	unsigned int m_uiChannel;
	std::wstring m_ChannelCaption;
	
	std::wstring m_AppURL;

	URLInfoArr m_URLInfoArr;
	std::vector<std::wstring> m_URLArr;
	std::vector<std::wstring> m_SystemArr;
	std::vector<std::wstring> m_NetworkArr;

	std::map<std::wstring, std::wstring> m_ExtendMap;

public:
	LJFP_Version()
	{
		m_uiVersion = 0;
		m_uiVersionBase = 0;
		m_uiVersionMinimum = 0;
		m_VersionDonotCheck = 0;
		m_uiChannel = 0;
	}
	static unsigned int VersionCaption2Version(std::wstring VersionCaption)
	{
		std::vector<std::wstring> ResultStr;
		std::size_t ResultCount = LJXMLStringHelper::SplitStrW(VersionCaption, L".", ResultStr);
		if (ResultCount != 3)
		{
			return 0;
		}
		int Version1 = GetStringUtil().WS2I(ResultStr[0]);
		if (Version1 > 255 || Version1 < 0)
		{
			return 0;
		}
		Version1 = Version1 << 24;
		int Version2 = GetStringUtil().WS2I(ResultStr[1]);
		if (Version2 > 4095 || Version2 < 0)
		{
			return 0;
		}
		Version2 = Version2 << 12;
		int Version3 = GetStringUtil().WS2I(ResultStr[2]);
		if (Version3 > 4095 || Version3 < 0)
		{
			return 0;
		}
		unsigned int uiResult = Version1 + Version2 + Version3;
		return uiResult;
	}
	static std::wstring Version2VersionCaption(unsigned int Version, int ResultType = 0)//0:11.222.3333,1:11,2:222,3:3333
	{
		unsigned int Version1 = Version >> 24;
		if (Version1 > 255 || Version1 < 0)
		{
			return L"";
		}
		unsigned int Version2 = (Version >> 12) - (Version1 << 12);
		if (Version2 > 4095 || Version2 < 0)
		{
			return L"";
		}
		unsigned int Version3 = Version - (Version1 << 24) - (Version2 << 12);
		if (Version3 > 4095 || Version3 < 0)
		{
			return L"";
		}
		std::wstring wsVersion1 = GetStringUtil().UI2WS(Version1);
		std::wstring wsVersion2 = GetStringUtil().UI2WS(Version2);
		std::wstring wsVersion3 = GetStringUtil().UI2WS(Version3);
		if (ResultType == 0)
		{
			return  wsVersion1 + L"." + wsVersion2 + L"." + wsVersion3;
		} 
		else if (ResultType == 1)
		{
			return  wsVersion1;
		}
		else if (ResultType == 2)
		{
			return  wsVersion2;
		}
		else if (ResultType == 3)
		{
			return  wsVersion3;
		}
		return  wsVersion1 + L"." + wsVersion2 + L"." + wsVersion3;
		
	}
	unsigned int GetVersion()
	{
		return m_uiVersion;
	}
	unsigned int GetVersionBase()
	{
		return m_uiVersionBase;
	}
	unsigned int GetVersionMinimum()
	{
		return m_uiVersionMinimum;
	}
	void SetVersionCaption(std::wstring VersionCaption)
	{
		m_VersionCaption = VersionCaption;
		m_uiVersion = VersionCaption2Version(m_VersionCaption);
	}
	std::wstring GetVersionCaption()
	{
		return m_VersionCaption;
	}
	void SetVersionCaptionBase(std::wstring VersionCaptionBase)
	{
		m_VersionCaptionBase = VersionCaptionBase;
		m_uiVersionBase = VersionCaption2Version(m_VersionCaptionBase);
	}
	std::wstring GetVersionCaptionBase()
	{
		return m_VersionCaptionBase;
	}
	void SetVersionCaptionMinimum(std::wstring VersionCaptionMinimum)
	{
		m_VersionCaptionMinimum = VersionCaptionMinimum;
		m_uiVersionMinimum = VersionCaption2Version(m_VersionCaptionMinimum);
	}
	std::wstring GetVersionCaptionMinimum()
	{
		return m_VersionCaptionMinimum;
	}
	void SetVersionDonotCheck(unsigned int VersionDonotCheck)
	{
		m_VersionDonotCheck = VersionDonotCheck;
	}
	unsigned int GetVersionDonotCheck()
	{
		return m_VersionDonotCheck;
	}
	void SetChannel(unsigned int uiChannel)
	{
		m_uiChannel = uiChannel;
	}
	unsigned int GetChannel()
	{
		return m_uiChannel;
	}
	void SetChannelCaption(std::wstring ChannelCaption)
	{
		m_ChannelCaption = ChannelCaption;
	}
	std::wstring GetChannelCaption()
	{
		return m_ChannelCaption;
	}
	void SetAppURL(std::wstring AppURL)
	{
		m_AppURL = AppURL;
	}
	std::wstring GetAppURL()
	{
		return m_AppURL;
	}
	void SetURLInfoArr(URLInfoArr UIArr)
	{
		m_URLInfoArr.resize(UIArr.size());
		m_URLArr.resize(UIArr.size());
		m_SystemArr.resize(UIArr.size());
		m_NetworkArr.resize(UIArr.size());
		for (unsigned int i = 0; i < m_URLInfoArr.size(); i++)
		{
			m_URLInfoArr[i].m_URL = UIArr[i].m_URL;
			m_URLInfoArr[i].m_System = UIArr[i].m_System;
			m_URLInfoArr[i].m_Network = UIArr[i].m_Network;
			m_URLArr[i] = UIArr[i].m_URL;
			m_SystemArr[i] = UIArr[i].m_System;
			m_NetworkArr[i] = UIArr[i].m_Network;
		}
	}
	URLInfoArr GetURLInfoArr()
	{
		return m_URLInfoArr;
	}
	std::vector<std::wstring> GetURLInfoArr(std::wstring strInfoType)
	{
		if (strInfoType == L"URL")
		{
			return m_URLArr;
		}
		else if (strInfoType == L"System")
		{
			return m_SystemArr;
		}
		else if (strInfoType == L"Network")
		{
			return m_NetworkArr;
		}
		return m_URLArr;
	}
	std::vector<std::wstring> GetSystemArr()
	{
		return m_URLArr;
	}
	std::vector<std::wstring> GetNetworkArr()
	{
		return m_URLArr;
	}
	void SetExtendMap(std::map<std::wstring, std::wstring> Extend)
	{
		m_ExtendMap.clear();
		std::map<std::wstring, std::wstring>::iterator it = Extend.begin();
		while (it != Extend.end())
		{
			m_ExtendMap[it->first] = it->second;
			it++;
		}
	}
	std::map<std::wstring, std::wstring> GetExtendMap()
	{
		return m_ExtendMap;
	}
	unsigned int GetExtendCount()
	{
		return m_ExtendMap.size();
	}
	std::wstring FindExtendValue(std::wstring wsKey)
	{
		std::map<std::wstring, std::wstring>::iterator it = m_ExtendMap.find(wsKey);
		if (it != m_ExtendMap.end())
		{
			return it->second;
		}
		return L"";
	}
	bool GetExtendKeyAndValue(unsigned int uiIndex, std::wstring& wsKey, std::wstring& wsValue)
	{
		if (uiIndex >= GetExtendCount())
		{
			return false;
		}
		std::map<std::wstring, std::wstring>::iterator it = m_ExtendMap.begin();
		for (unsigned int i = 0; i < uiIndex; i++)
		{
			it++;
		}
		wsKey = it->first;
		wsValue = it->second;
		return true;
	}
	void CloneExtendMap(std::map<std::wstring, std::wstring>& ExtendMap)
	{
		ExtendMap.clear();
		std::map<std::wstring, std::wstring>::iterator it = m_ExtendMap.begin();
		while (it != m_ExtendMap.end())
		{
			ExtendMap[it->first] = it->second;
			it++;
		}
	}
	int LoadFromXMLFile(std::wstring strFileName)
	{
		return LoadFromFileEx(strFileName, 0);
	}
	int SaveToXMLFile(std::wstring strFileName)
	{
		return SaveToFileEx(strFileName, 0);
	}
	int LoadFromFile(std::wstring strFileName)
	{
		return LoadFromFileEx(strFileName, 1);
	}
	int SaveToFile(std::wstring strFileName)
	{
		return SaveToFileEx(strFileName, 1);
	}
	int LoadFromFileEx(std::wstring strFileName, int FileType)
	{
		LJFP_NodeEx* pNode0 = NULL;
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		int iResult = 0;
		if (FileType == 0)
		{
			iResult = pNode0->LoadFromXMLFile(strFileName, pNode0);
		}
		else if (FileType == 1)
		{
			iResult = pNode0->LoadFromFile(strFileName, (LJFP_Node*&)pNode0);
		}
		else
		{
			return -1;
		}
		if (iResult != 0)
		{
			return iResult;
		}
		{
			pNode1 = pNode0->FindNode(L"VersionInfo");
			std::wstring VersionCaption = pNode1->FindAttrValue(L"VersionCaption");
			SetVersionCaption(VersionCaption);
			std::wstring VersionCaptionBase = pNode1->FindAttrValue(L"VersionCaptionBase");
			SetVersionCaptionBase(VersionCaptionBase);
			std::wstring VersionCaptionMinimum = pNode1->FindAttrValue(L"VersionCaptionMinimum");
			SetVersionCaptionMinimum(VersionCaptionMinimum);
			std::wstring wsVersionDonotCheck = pNode1->FindAttrValue(L"VersionDonotCheck");
			if (wsVersionDonotCheck != L"")
			{
				m_VersionDonotCheck = GetStringUtil().WS2UI(wsVersionDonotCheck);
			}
			else
			{
				m_VersionDonotCheck = 0;
			}
		}
		{
			pNode1 = pNode0->FindNode(L"URLInfo");
			std::wstring AppURL = pNode1->FindAttrValue(L"AppURL");
			SetAppURL(AppURL);
			std::wstring wsURLCount = pNode1->FindAttrValue(L"Count");
			if (wsURLCount == L"")
			{
				return -1;
			}
			unsigned int URLCount = 0;
			URLCount = GetStringUtil().WS2UI(wsURLCount);
			m_URLInfoArr.resize(URLCount);
			m_URLArr.resize(URLCount);
			m_SystemArr.resize(URLCount);
			m_NetworkArr.resize(URLCount);
			for (unsigned int i = 0; i < URLCount; i++)
			{
				pNode2 = pNode1->FindNode(GetStringUtil().UI2WS(i));
				if (!pNode2)
				{
					return -1;
				}
				URLInfo UI;
				UI.m_URL = pNode2->FindAttrValue(L"URL");
				UI.m_System = pNode2->FindAttrValue(L"System");
				UI.m_Network = pNode2->FindAttrValue(L"Network");
				//std::transform(UI.m_URL.begin(), UI.m_URL.end(), UI.m_URL.begin(), ::tolower);
				std::transform(UI.m_System.begin(), UI.m_System.end(), UI.m_System.begin(), ::tolower);
				std::transform(UI.m_Network.begin(), UI.m_Network.end(), UI.m_Network.begin(), ::tolower);
				m_URLInfoArr[i] = UI;
				m_URLArr[i] = UI.m_URL;
				m_SystemArr[i] = UI.m_System;
				m_NetworkArr[i] = UI.m_Network;
			}
		}
		{
			pNode1 = pNode0->FindNode(L"ChannelInfo");
			m_ChannelCaption = pNode1->FindAttrValue(L"ChannelCaption");
			std::wstring wsChannel = pNode1->FindAttrValue(L"Channel");
			if (wsChannel != L"")
			{
				m_uiChannel = GetStringUtil().WS2UI(wsChannel);
			}
			else
			{
				m_uiChannel = 0;
			}
		}
		{
			pNode1 = pNode0->FindNode(L"ExtendInfo");
			m_ExtendMap.clear();
			for (unsigned int i = 0; i < pNode1->GetAttrCount(); i++)
			{
				LJFP_Attr* pAttr = pNode1->GetAttr(i);
				m_ExtendMap[pAttr->m_Key] = pAttr->m_Value;
			}
		}
		delete pNode0;
		return 0;
	}
	int SaveToFileEx(std::wstring strFileName, int FileType)
	{
		LJFP_NodeEx* pNode0 = new LJFP_NodeEx(L"Root", L"");
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		{
			pNode1 = pNode0->AddNode(L"VersionInfo", L"");
			pNode1->AddAttr(L"VersionCaption", m_VersionCaption);
			pNode1->AddAttr(L"VersionCaptionBase", m_VersionCaptionBase);
			pNode1->AddAttr(L"VersionCaptionMinimum", m_VersionCaptionMinimum);
			//pNode1->AddAttr(L"VersionNumber", GetStringUtil().UI2WS(m_uiVersion));
			//pNode1->AddAttr(L"VersionNumberBase", GetStringUtil().UI2WS(m_uiVersionBase));
			//pNode1->AddAttr(L"VersionNumberMinimum", GetStringUtil().UI2WS(m_uiVersionMinimum));
			if (m_VersionDonotCheck > 0)
			{
				pNode1->AddAttr(L"VersionDonotCheck", GetStringUtil().UI2WS(m_VersionDonotCheck));
			}
		}
		{
			pNode1 = pNode0->AddNode(L"URLInfo", L"");
			pNode1->AddAttr(L"AppURL", m_AppURL);
			pNode1->AddAttr(L"Count", GetStringUtil().UI2WS(m_URLInfoArr.size()));
			{
				for (unsigned int i = 0; i < m_URLInfoArr.size(); i++)
				{
					pNode2 = pNode1->AddNode(GetStringUtil().UI2WS(i), L"");
					pNode2->AddAttr(L"URL", m_URLInfoArr[i].m_URL);
					pNode2->AddAttr(L"System", m_URLInfoArr[i].m_System);
					pNode2->AddAttr(L"Network", m_URLInfoArr[i].m_Network);
				}
			}
		}
		{
			pNode1 = pNode0->AddNode(L"ChannelInfo", L"");
			pNode1->AddAttr(L"Channel", GetStringUtil().UI2WS(m_uiChannel));
			pNode1->AddAttr(L"ChannelCaption", m_ChannelCaption);
		}
		{
			pNode1 = pNode0->AddNode(L"ExtendInfo", L"");
			std::map<std::wstring, std::wstring>::iterator it = m_ExtendMap.begin();
			while (it != m_ExtendMap.end())
			{
				pNode1->AddAttr(it->first, it->second);
				it++;
			}
		}
		if (FileType == 0)
		{
			pNode0->SaveToXMLFile(strFileName, pNode0);
		}
		else if (FileType == 1)
		{
			pNode0->SaveToFile(strFileName, pNode0);
		}
		delete pNode0;
		return 0;
	}
};

#endif //LJFP_Version_H