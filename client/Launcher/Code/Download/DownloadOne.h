#pragma once
#include "../stdafx.h"
#include "../Utils/StringUtil.h"
#include "../Utils/FileUtil.h"
#include "../WinHttpClient/WinHttpClient.h"
#include "../HttpClient/HttpClient.h"

using namespace std;

class DownloadOne;

typedef void(*DownloadCB_Func)(DownloadOne* pDO);
//void PackOneFile(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strFileName, unsigned int FileIndex, unsigned int FileCount, unsigned int PackIndex)
//{
//	wprintf((L"Pack:" + GetStringUtil().UI2WS(PackIndex + 1) + L"-File:" + GetStringUtil().UI2WS(FileIndex + 1) + L"/" + GetStringUtil().UI2WS(FileCount) + L"-" +
//		(strRootPathName + strParentPathName + strFileName)).c_str());
//	wprintf(L"\n");
//}

class DownloadOne
{
public:
	thread m_tRun;
	int m_iState;
	int m_iResultType;
	wstring m_wsURL;
	wstring m_wsDst;
	int m_iResponseCode;
	vector<char> m_Data;
	void* m_pData;
	DownloadCB_Func m_CBFunc;

	DownloadOne(wstring wsURL, wstring wsDst, DownloadCB_Func CBFunc)
	{
		m_iResultType = 1;
		m_wsURL = wsURL;
		m_wsDst = wsDst;
		m_iState = 0;
		m_iResponseCode = -1;
		m_pData = NULL;
		m_CBFunc = CBFunc;
	}
	DownloadOne(wstring wsURL, void* pData, DownloadCB_Func CBFunc)
	{
		m_iResultType = 2;
		m_wsURL = wsURL;
		m_wsDst = L"";
		m_iState = 0;
		m_iResponseCode = -1;
		m_pData = pData;
		m_CBFunc = CBFunc;
	}
	~DownloadOne()
	{
		m_Data.clear();
		//m_tRun.detach();
	}
	int Run()
	{
		if (m_iState != 0)
		{
			return m_iState;
		}
		m_iState = 1;
		CHttpClient HC;
		int iResult = 0;
		if (m_iResultType == 1)
		{
			HC.Get(StringUtil::WS2S(m_wsURL), &m_Data, &m_iResponseCode);
			if (iResult == 0 && m_iResponseCode == 200)
			{
				CreateDirEx(L"", GetFilePath(m_wsDst, L""));
				std::ofstream FS(StringUtil::WS2S(m_wsDst).c_str(), std::ios_base::binary);
				FS.write(&m_Data[0], m_Data.size());
				FS.close();
				m_pData = &m_Data[0];
				m_iState = 2;
			}
			else
			{
				m_Data.clear();
				m_iState = 3;
			}
		}
		else if (m_iResultType == 2)
		{
			HC.Get(StringUtil::WS2S(m_wsURL), m_pData, &m_iResponseCode);
			if (iResult == 0 && m_iResponseCode == 200)
			{
				m_iState = 2;
			}
			else
			{
				m_iState = 3;
			}
		}
		return iResult;
	}
};