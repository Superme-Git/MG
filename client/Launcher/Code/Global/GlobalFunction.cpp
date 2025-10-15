#include "GlobalFunction.h"
#include "../UIControl/DUIWindowManager.h"
#include "../UIControl/DUILabel.h"
#include "../UIControl/DUIButton.h"
#include "../UIControl/DUIProgressBar.h"
#include "../LauncherData.h"

using namespace std;

void DoEvents()
{
	MSG msg;
	//while (GetMessage(&msg, NULL, 0, 0, PM_REMOVE))
	while (PeekMessage(&msg,NULL,0,0,PM_REMOVE))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}
	DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
	if (!pMainWindow)
	{
		exit(0);
	}
}
void DoEventsUI()
{
	GetDUIWindowManager()->Run();
}
void GlobalNotifyEnd()
{
	DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
	if (!pMainWindow)
	{
		return;
	}
	DUIButton* pButtonGameStart = (DUIButton*)pMainWindow->FindItem(L"ButtonGameStart");
	if (!pButtonGameStart)
	{
		return;
	}
	pButtonGameStart->SetEnable(1);
	pButtonGameStart->DrawMeBefor(pMainWindow->GetBGDC());
	DoEvents();
	DoEventsUI();
}
void GlobalNotifyFail()
{
	DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
	if (!pMainWindow)
	{
		return;
	}
	pMainWindow->CloseMe();
	DoEvents();
	DoEventsUI();
}
void GlobalNotifyStep(int step)
{
	DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
	if (!pMainWindow)
	{
		return;
	}
	DUILabel* pLabelPercent = (DUILabel*)pMainWindow->FindItem(L"LabelPercent");
	if (!pLabelPercent)
	{
		return;
	}
	pLabelPercent->SetText(GetStringUtil().UI2WS(step) + L"%");
	pLabelPercent->DrawMeBefor(pMainWindow->GetBGDC());
	DUIProgressBar* pProgressBarPrecent = (DUIProgressBar*)pMainWindow->FindItem(L"ProgressBarPercent");
	if (!pProgressBarPrecent)
	{
		return;
	}
	pProgressBarPrecent->SetValueCur(step);
	pProgressBarPrecent->DrawMeBefor(pMainWindow->GetBGDC());
	DoEvents();
	DoEventsUI();
}
std::wstring GlobalNotifyGetStringByKey(std::wstring wsKey)
{
	return LauncherData::FindStringByKey(wsKey);
}
void GlobalNotifyMsg(const std::wstring& msg)
{
	DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
	if (!pMainWindow)
	{
		return;
	}
	DUIBase* pUITemp = pMainWindow->FindItem(L"LabelMsg");
	if (!pUITemp)
	{
		return;
	}
	DUILabel* pLabelMsg = (DUILabel*)pUITemp;
	pLabelMsg->SetText(msg);
	pLabelMsg->DrawMeBefor(pMainWindow->GetBGDC());
	DoEvents();
	DoEventsUI();
}
void GlobalNotifyMsgByKey(const std::wstring& key)
{
	std::wstring wsValue = GlobalNotifyGetStringByKey(key);
	GlobalNotifyMsg(wsValue);
}
void GlobalNotifyLocalVersion(const std::wstring& localVersion)
{
	DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
	if (!pMainWindow)
	{
		return;
	}
	DUIBase* pUITemp = pMainWindow->FindItem(L"LabelCurVer");
	if (!pUITemp)
	{
		return;
	}
	DUILabel* pCurVer = (DUILabel*)pUITemp;
	pCurVer->SetText(GlobalNotifyGetStringByKey(L"CurVer") + localVersion);
	pCurVer->DrawMeBefor(pMainWindow->GetBGDC());
	DoEvents();
	DoEventsUI();
}
void GlobalNotifyNewVersion(const std::wstring& newVersion)
{
	DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
	if (!pMainWindow)
	{
		return;
	}
	DUIBase* pUITemp = pMainWindow->FindItem(L"LabelNewVer");
	if (!pUITemp)
	{
		return;
	}
	DUILabel* pNewVer = (DUILabel*)pUITemp;
	pNewVer->SetText(GlobalNotifyGetStringByKey(L"NewVer") + newVersion);
	pNewVer->DrawMeBefor(pMainWindow->GetBGDC());
	DoEvents();
	DoEventsUI();
}
int GlobalNotifyShowForm(int FormType, int DownloadSize, std::wstring AppURL)
{
	DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
	if (!pMainWindow)
	{
		return -1;
	}
	HWND MainWindowHandle = pMainWindow->GetBGHandle();

	wstring wsTitle = GlobalNotifyGetStringByKey(L"upformstrtitle") + L"   " + GlobalNotifyGetStringByKey(L"upformstrwifi");
	wstring wsText;
	
	if (FormType == 1)
	{
		wsText = GlobalNotifyGetStringByKey(L"upformstr11");
	}
	else if (FormType == 2)
	{
		wsText = GlobalNotifyGetStringByKey(L"upformstr21");
	}
	else if (FormType == 3)
	{
		wsText = GlobalNotifyGetStringByKey(L"upformstr31");
	}
	else if (FormType == 4)
	{
		wstring wsS1 = GlobalNotifyGetStringByKey(L"upformstr41");
		wstring wsS2;
		if (DownloadSize >= 1024 * 1024)
		{
			wsS2 = GetStringUtil().UI2WS(DownloadSize / 1024 / 1024) + L"MB";
		}
		else if (DownloadSize >= 1024)
		{
			wsS2 = GetStringUtil().UI2WS(DownloadSize / 1024) + L"KB";
		}
		else
		{
			wsS2 = GetStringUtil().UI2WS(DownloadSize) + L"B";
		}
		wstring wsS3 = GlobalNotifyGetStringByKey(L"upformstr42");
		wsText = wsS1 + wsS2 + wsS3;
	}
	else if (FormType == 5)
	{
		wsText = GlobalNotifyGetStringByKey(L"upformstr51");
	}
	else if (FormType == 21)
	{
		wsText = GlobalNotifyGetStringByKey(L"upformstr211");
	}
	else if (FormType == 22)
	{
		wstring wsS1 = GlobalNotifyGetStringByKey(L"upformstr221");
		wstring wsS2;
		if (DownloadSize >= 1024 * 1024)
		{
			wsS2 = GetStringUtil().UI2WS(DownloadSize / 1024 / 1024) + L"MB";
		}
		else if (DownloadSize >= 1024)
		{
			wsS2 = GetStringUtil().UI2WS(DownloadSize / 1024) + L"KB";
		}
		else
		{
			wsS2 = GetStringUtil().UI2WS(DownloadSize) + L"B";
		}
		wstring wsS3 = GlobalNotifyGetStringByKey(L"upformstr222");
		wsText = wsS1 + wsS2 + wsS3;
	}
	else if (FormType == 23)
	{
		wsText = GlobalNotifyGetStringByKey(L"upformstr231");
	}

	int iResult = MessageBox(MainWindowHandle, wsText.c_str(), wsTitle.c_str(), MB_OKCANCEL);
	if (iResult == IDOK)
	{
		if (FormType == 1)
		{
			return 2;
		}
		else if (FormType == 2)
		{
			return -1;
		}
		else if (FormType == 3)
		{
			return 4;
		}
		else if (FormType == 4)
		{
			return 6;
		}
		else if (FormType == 5)
		{
			return 6;
		}
		else if (FormType == 21)
		{
			return 21;
		}
		else if (FormType == 22)
		{
			return 23;
		}
		else if (FormType == 23)
		{
			return 23;
		}
	}
	else if (iResult == IDCANCEL)
	{
		return -1;
	}
	return -1;
}