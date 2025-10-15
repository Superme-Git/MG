#ifndef LauncherUI_H
#define LauncherUI_H
#include "stdafx.h"
#include "shellapi.h"
#include "UIControl/DUIWindowManager.h"
#include "UIControl/DUIImage.h"
#include "UIControl/DUILabel.h"
#include "UIControl/DUILabelLink.h"
#include "UIControl/DUIButton.h"
#include "UIControl/DUIProgressBar.h"
#include "UIControl/DUIWebBrowser.h"

#include "Global/GlobalFunction.h"

static HINSTANCE gs_hInstance = 0;

namespace LauncherUI
{
	static void GameMin(DUIBase* pUI, int iX, int iY)
	{
		if (pUI->m_Type != DUIType_Button)
		{
			return;
		}
		DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
		if (!pMainWindow)
		{
			return;
		}
		ShowWindow(pMainWindow->GetBGHandle(), SW_MINIMIZE);
	}
	static void GameExit(DUIBase* pUI, int iX, int iY)
	{
		if (pUI->m_Type != DUIType_Button)
		{
			return;
		}
		DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
		if (!pMainWindow)
		{
			return;
		}
		wstring wsTitle = GlobalNotifyGetStringByKey(L"gameexittitle");
		wstring wsText = GlobalNotifyGetStringByKey(L"gameexittext");
		int iResult = MessageBox(pMainWindow->GetBGHandle(), wsText.c_str(), wsTitle.c_str(), MB_OKCANCEL);
		if (iResult == IDOK)
		{
			pMainWindow->m_bOver = true;
			//pMainWindow->CloseMe();
		}
	}
	static void GameExitForce(DUIBase* pUI, int iX, int iY)
	{
		if (pUI->m_Type != DUIType_Button)
		{
			return;
		}
		DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
		if (!pMainWindow)
		{
			return;
		}
		pMainWindow->m_bOver = true;
		DUIButton* pButtonGameStart = (DUIButton*)pUI;
		if (!pButtonGameStart)
		{
			return;
		}
		pButtonGameStart->SetEnable(0);
		//pMainWindow->CloseMe();
	}
	static void GameStart(DUIBase* pUI, int iX, int iY)
	{
		if (pUI->m_Type != DUIType_Button)
		{
			return;
		}
		wstring wsFileName = GetRootPath() + L"bin/release/MT3.exe";
		wstring wsFilePath = GetRootPath() + L"bin/release/";
		ShellExecute(NULL, L"open", wsFileName.c_str(), NULL, wsFilePath.c_str(), SW_SHOW);
		GameExitForce(pUI, iX, iY);
	}
	static int InitUIBase(LJFP_NodeEx* pNode, DUIBase* pParent, DUIBase*& pResult);
	static int InitUIItems(LJFP_NodeEx* pNode, DUIBase* pParent)
	{
		int iResult = 0;
		LJFP_NodeEx* pNodeItems = pNode->FindNode(L"Items");
		if (pNodeItems)
		{
			unsigned int uiCount = GetStringUtil().WS2UI(pNodeItems->FindAttrValue(L"Count"));
			for (int i = 0; i < uiCount; i++)
			{
				LJFP_NodeEx* pNodeCur = pNodeItems->FindNode(GetStringUtil().UI2WS(i));
				DUIBase* pResult = NULL;
				iResult = InitUIBase(pNodeCur, pParent, pResult);
				if (iResult != 0)
				{
					return -1;
				}
			}
		}
		return 0;
	}
	static int InitUIWindow(LJFP_NodeEx* pNode, DUIBase* pParent, DUIWindow*& pResult)
	{
		pResult = NULL;
		wstring wsName = pNode->FindAttrValue(L"Name");
		wstring wsClassName = pNode->FindAttrValue(L"ClassName");
		wstring wsTitle = pNode->FindAttrValue(L"Title");
		int iX = GetStringUtil().WS2I(pNode->FindAttrValue(L"X"));
		int iY = GetStringUtil().WS2I(pNode->FindAttrValue(L"Y"));
		unsigned int uiWidth = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Width"));
		unsigned int uiHeight = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Height"));
		wstring wsRgnFileName = pNode->FindAttrValue(L"RgnFileName");
		int iMainWindow = GetStringUtil().WS2I(pNode->FindAttrValue(L"MainWindow"));
		GetDUIWindowManager()->RegisterClass(gs_hInstance, wsClassName.c_str(), IDR_EXE_ICON, IDR_EXE_ICON, GetDUIWindowManager()->WndProc);
		pResult = GetDUIWindowManager()->AddItem(pParent, wsName, gs_hInstance, wsClassName, wsTitle, WS_POPUP | WS_VISIBLE, uiWidth, uiHeight, L"bin/release/LauncherImageBG.rgn", 1, iX, iY, iMainWindow);
		if (!pResult)
		{
			return -1;
		}
		return InitUIItems(pNode, pResult);
	}
	static int InitUILabel(LJFP_NodeEx* pNode, DUIBase* pParent, DUILabel*& pResult)
	{
		pResult = NULL;
		wstring wsName = pNode->FindAttrValue(L"Name");
		wstring wsText = pNode->FindAttrValue(L"Text");
		unsigned int uiTextColorR = GetStringUtil().WS2UI(pNode->FindAttrValue(L"TextColorR"));
		unsigned int uiTextColorG = GetStringUtil().WS2UI(pNode->FindAttrValue(L"TextColorG"));
		unsigned int uiTextColorB = GetStringUtil().WS2UI(pNode->FindAttrValue(L"TextColorB"));
		unsigned int uiTextColorRGB = RGB(uiTextColorR, uiTextColorG, uiTextColorB);
		wstring wsFontName = pNode->FindAttrValue(L"FontName");
		int iFontHeight = GetStringUtil().WS2I(pNode->FindAttrValue(L"FontHeight"));
		int iX = GetStringUtil().WS2I(pNode->FindAttrValue(L"X"));
		int iY = GetStringUtil().WS2I(pNode->FindAttrValue(L"Y"));
		unsigned int uiWidth = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Width"));
		unsigned int uiHeight = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Height"));
		pResult = new DUILabel(pParent, wsName);
		pResult->Init(1, 0, uiTextColorRGB, wsText, iX, iY, uiWidth, uiHeight, wsFontName, iFontHeight);
		if (!pResult)
		{
			return -1;
		}
		return InitUIItems(pNode, pResult);
	}
	static int InitUILabelLink(LJFP_NodeEx* pNode, DUIBase* pParent, DUILabelLink*& pResult)
	{
		pResult = NULL;
		wstring wsName = pNode->FindAttrValue(L"Name");
		wstring wsText = pNode->FindAttrValue(L"Text");
		wstring wsURL = pNode->FindAttrValue(L"URL");
		unsigned int uiTextColorR = GetStringUtil().WS2UI(pNode->FindAttrValue(L"TextColorR"));
		unsigned int uiTextColorG = GetStringUtil().WS2UI(pNode->FindAttrValue(L"TextColorG"));
		unsigned int uiTextColorB = GetStringUtil().WS2UI(pNode->FindAttrValue(L"TextColorB"));
		unsigned int uiTextColorRGB = RGB(uiTextColorR, uiTextColorG, uiTextColorB);
		wstring wsFontName = pNode->FindAttrValue(L"FontName");
		int iFontHeight = GetStringUtil().WS2I(pNode->FindAttrValue(L"FontHeight"));
		int iX = GetStringUtil().WS2I(pNode->FindAttrValue(L"X"));
		int iY = GetStringUtil().WS2I(pNode->FindAttrValue(L"Y"));
		unsigned int uiWidth = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Width"));
		unsigned int uiHeight = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Height"));
		wstring wsRandomStr = pNode->FindAttrValue(L"RandomStr");
		pResult = new DUILabelLink(pParent, wsName);
		pResult->Init(1, 0, uiTextColorRGB, wsText, wsURL, iX, iY, uiWidth, uiHeight, wsFontName, iFontHeight, wsRandomStr);
		if (!pResult)
		{
			return -1;
		}
		return InitUIItems(pNode, pResult);
	}
	static int InitUIImage(LJFP_NodeEx* pNode, DUIBase* pParent, DUIImage*& pResult)
	{
		pResult = NULL;
		wstring wsName = pNode->FindAttrValue(L"Name");
		wstring wsFileName = pNode->FindAttrValue(L"FileName");
		unsigned int uiColorKeyR = GetStringUtil().WS2UI(pNode->FindAttrValue(L"ColorKeyR"));
		unsigned int uiColorKeyG = GetStringUtil().WS2UI(pNode->FindAttrValue(L"ColorKeyG"));
		unsigned int uiColorKeyB = GetStringUtil().WS2UI(pNode->FindAttrValue(L"ColorKeyB"));
		unsigned int uiColorKeyRGB = RGB(uiColorKeyR, uiColorKeyG, uiColorKeyB);
		int iX = GetStringUtil().WS2I(pNode->FindAttrValue(L"X"));
		int iY = GetStringUtil().WS2I(pNode->FindAttrValue(L"Y"));
		unsigned int uiWidth = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Width"));
		unsigned int uiHeight = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Height"));
		pResult = new DUIImage(pParent, wsName);
		pResult->Init(wsFileName, iX, iY, uiWidth, uiHeight, uiColorKeyRGB);
		if (!pResult)
		{
			return -1;
		}
		return InitUIItems(pNode, pResult);
	}
	static int InitUIButton(LJFP_NodeEx* pNode, DUIBase* pParent, DUIButton*& pResult)
	{
		pResult = NULL;
		wstring wsName = pNode->FindAttrValue(L"Name");
		int iX = GetStringUtil().WS2I(pNode->FindAttrValue(L"X"));
		int iY = GetStringUtil().WS2I(pNode->FindAttrValue(L"Y"));
		unsigned int uiWidth = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Width"));
		unsigned int uiHeight = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Height"));
		int iEnable = GetStringUtil().WS2I(pNode->FindAttrValue(L"Enable"));
		wstring wsFunc = pNode->FindAttrValue(L"Func");
		pResult = new DUIButton(pParent, wsName);
		pResult->Init(iX, iY, uiWidth, uiHeight, iEnable);
		if (!pResult)
		{
			return -1;
		}
		if (wsFunc == L"GameStart")
		{
			pResult->m_LBDownCB_Func = GameStart;
		}
		else if (wsFunc == L"GameMin")
		{
			pResult->m_LBDownCB_Func = GameMin;
		}
		else if (wsFunc == L"GameExit")
		{
			pResult->m_LBDownCB_Func = GameExit;
		}
		int iResult = InitUIItems(pNode, pResult);
		if (iResult != 0)
		{
			return -1;
		}
		pResult->ReLayer();
		return 0;
	}
	static int InitUIProgressBar(LJFP_NodeEx* pNode, DUIBase* pParent, DUIProgressBar*& pResult)
	{
		pResult = NULL;
		wstring wsName = pNode->FindAttrValue(L"Name");
		int iX = GetStringUtil().WS2I(pNode->FindAttrValue(L"X"));
		int iY = GetStringUtil().WS2I(pNode->FindAttrValue(L"Y"));
		unsigned int uiWidth = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Width"));
		unsigned int uiHeight = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Height"));
		unsigned int uiValueMax = GetStringUtil().WS2UI(pNode->FindAttrValue(L"ValueMax"));
		unsigned int uiValueMin = GetStringUtil().WS2UI(pNode->FindAttrValue(L"ValueMin"));
		unsigned int uiValueCur = GetStringUtil().WS2UI(pNode->FindAttrValue(L"ValueCur"));
		pResult = new DUIProgressBar(pParent, wsName);
		pResult->Init(iX, iY, uiWidth, uiHeight, uiValueMax, uiValueMin, uiValueCur);
		if (!pResult)
		{
			return -1;
		}
		int iResult = InitUIItems(pNode, pResult);
		if (iResult != 0)
		{
			return -1;
		}
		pResult->ReLayer();
		return 0;
	}
	static int InitUIWebBrowser(LJFP_NodeEx* pNode, DUIBase* pParent, DUIWebBrowser*& pResult)
	{
		pResult = NULL;
		wstring wsName = pNode->FindAttrValue(L"Name");
		int iX = GetStringUtil().WS2I(pNode->FindAttrValue(L"X"));
		int iY = GetStringUtil().WS2I(pNode->FindAttrValue(L"Y"));
		unsigned int uiWidth = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Width"));
		unsigned int uiHeight = GetStringUtil().WS2UI(pNode->FindAttrValue(L"Height"));
		wstring wsURL = pNode->FindAttrValue(L"URL");
		wstring wsRandomStr = pNode->FindAttrValue(L"RandomStr");
		pResult = new DUIWebBrowser(pParent, wsName);
		pResult->Init(iX, iY, uiWidth, uiHeight, wsURL, gs_hInstance, wsRandomStr);
		if (!pResult)
		{
			return -1;
		}
		//GetDUIWindowManager()->FindMainWindow()->AddWebBrowserParentStatic(pResult->m_hWndText);
		return 0;
	}
	static int InitUIBase(LJFP_NodeEx* pNode, DUIBase* pParent, DUIBase*& pResult)
	{
		if (pNode->FindAttrValue(L"Type") == L"Window")
		{
			return InitUIWindow(pNode, pParent, (DUIWindow*&)pResult);
		}
		else if (pNode->FindAttrValue(L"Type") == L"Label")
		{
			return InitUILabel(pNode, pParent, (DUILabel*&)pResult);
		}
		else if (pNode->FindAttrValue(L"Type") == L"LabelLink")
		{
			return InitUILabelLink(pNode, pParent, (DUILabelLink*&)pResult);
		}
		else if (pNode->FindAttrValue(L"Type") == L"Image")
		{
			return InitUIImage(pNode, pParent, (DUIImage*&)pResult);
		}
		else if (pNode->FindAttrValue(L"Type") == L"Button")
		{
			return InitUIButton(pNode, pParent, (DUIButton*&)pResult);
		}
		else if (pNode->FindAttrValue(L"Type") == L"ProgressBar")
		{
			return InitUIProgressBar(pNode, pParent, (DUIProgressBar*&)pResult);
		}
		else if (pNode->FindAttrValue(L"Type") == L"WebBrowser")
		{
			return InitUIWebBrowser(pNode, pParent, (DUIWebBrowser*&)pResult);
		}
		else
		{
			return -1;
		}
		return 0;
	}
	static int InitUI()
	{
		wstring wsBinReleasePath = L"bin/release/";
		wstring wsLauncherDataFileName = wsBinReleasePath + L"LauncherData.xml";
		LJFP_NodeEx* pNode0 = NULL;
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		LJFP_NodeEx* pNode3 = NULL;
		LJFP_NodeEx* pNode4 = NULL;
		int iResult = 0;
		iResult = pNode0->LoadFromXMLFile(wsLauncherDataFileName, pNode0);
		if (iResult != 0)
		{
			return iResult;
		}
		pNode1 = pNode0->FindNode(L"UI");
		if (!pNode1){ return -1; }
		iResult = InitUIItems(pNode1, NULL);
		if (iResult != 0)
		{
			return iResult;
		}
		delete pNode0;
		DUIWindow* pMainWindow = GetDUIWindowManager()->FindMainWindow();
		if (!pMainWindow)
		{
			return -1;
		}
		pMainWindow->Redraw();

		//GlobalNotifyLocalVersion(L"1.2.3");
		//GlobalNotifyNewVersion(L"1.2.5");
		//GlobalNotifyMsgByKey(L"upmgrstr31");
		//GlobalNotifyStep(33);
		//GlobalNotifyEnd();

		return 0;
	}
	static int UnInitUI()
	{
		ReleaseDUIWindowManager();

		return 0;
	}

};

#endif