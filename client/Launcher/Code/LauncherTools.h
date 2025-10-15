#ifndef LauncherTools_H
#define LauncherTools_H
#include "stdafx.h"
#include "Utils/SystemUtil.h"
#include "Utils/FileUtil.h"
#include "LJFP/LJFP_StringUtil.h"
#include "Update/UpdateBin.h"

using namespace std;

static int gs_iDesignMode = 0;

namespace LauncherTools
{
	static int MakeRgnFile(wstring wsImageFileName, wstring wsRgnFileName, unsigned int uiColorKey)
	{
		int iResult = 0;
		HRGN hRgnResult;
		iResult = CreateRgnFromImageFile(wsImageFileName, hRgnResult, uiColorKey);
		if (iResult != 0)
		{
			return -1;
		}
		iResult = SaveRgnToFile(hRgnResult, wsRgnFileName);
		if (iResult != 0)
		{
			MessageBox(0, L"区域生成失败", L"", MB_OK);
			return -1;
		}
		MessageBox(0, L"区域生成成功", L"", MB_OK);
		return 0;
	}
	static int MakeBinUpdate(wstring wsFileListName, wstring wsDstPathName)
	{
		if (ExistFile(wsFileListName) == false)
		{
			return -1;
		}
		int iResult = 0;
		UpdateBinFileList UBFL;
		iResult = UBFL.LoadFromXMLFile(wsFileListName);
		if (iResult != 0)
		{
			return -1;
		}
		wstring wsSrcFileName = GetFileName(wsFileListName);
		CreateDirEx(L"", wsDstPathName);
		iResult = UBFL.SaveToBinFile(wsDstPathName + wsSrcFileName + L".bin");
		if (iResult != 0)
		{
			MessageBox(0, L"资源生成失败", L"", MB_OK);
			return -1;
		}
		MessageBox(0, L"资源生成成功", L"", MB_OK);
		return 0;
	}
	static int CheckCmdLine(wstring wsCmdLine)
	{
		int iResult = 0;
		std::vector<std::wstring> ResultStr;
		std::size_t ResultCount = GetStringUtil().SplitStrW(wsCmdLine, L" ", ResultStr);
		std::wstring wsCurCmd;
		for (std::size_t i = 0; i < ResultCount; i++)
		{
			wsCurCmd = ResultStr[i];
			if (wsCurCmd.find(L"designmode") == 0)
			{
				gs_iDesignMode = 1;
				MessageBox(0, wsCmdLine.c_str(), L"CmdLine", MB_OK);
			}
			else if(wsCurCmd.find(L"makergnfile:") == 0)
			{
				std::wstring wsCmdValue = L"makergnfile:";
				std::wstring wsParamValue = wsCurCmd.substr(wsCmdValue.size());
				std::wstring wsImageFileName;
				std::wstring wsRgnFileName;
				std::wstring wsColorKeyR;
				std::wstring wsColorKeyG;
				std::wstring wsColorKeyB;
				unsigned int uiColorKeyR;
				unsigned int uiColorKeyG;
				unsigned int uiColorKeyB;
				unsigned int uiColorKeyRGB;
				std::vector<std::wstring> ParamList;
				GetStringUtil().SplitStrW(wsParamValue, L"|", ParamList);
				if (ParamList.size() == 5)
				{
					wsImageFileName = ParamList[0];
					wsRgnFileName = ParamList[1];
					wsColorKeyR = ParamList[2];
					wsColorKeyG = ParamList[3];
					wsColorKeyB = ParamList[4];
					uiColorKeyR = GetStringUtil().WS2UI(wsColorKeyR);
					uiColorKeyG = GetStringUtil().WS2UI(wsColorKeyG);
					uiColorKeyB = GetStringUtil().WS2UI(wsColorKeyB);
					uiColorKeyRGB = RGB(uiColorKeyR, uiColorKeyG, uiColorKeyB);
					return MakeRgnFile(wsImageFileName, wsRgnFileName, uiColorKeyRGB);
				}
				else
				{
					wprintf(L"参数不足\n");
					return -1;
				}
			}
			else if (wsCurCmd.find(L"makebinupdate:") == 0)
			{
				std::wstring wsCmdValue = L"makebinupdate:";
				std::wstring wsParamValue = wsCurCmd.substr(wsCmdValue.size());
				wstring wsFileListName;
				wstring wsDstPathName;
				std::vector<std::wstring> ParamList;
				GetStringUtil().SplitStrW(wsParamValue, L"|", ParamList);
				if (ParamList.size() == 2)
				{
					wsFileListName = ParamList[0];
					wsDstPathName = ParamList[1];
					return MakeBinUpdate(wsFileListName, wsDstPathName);
				}
				else
				{
					wprintf(L"参数不足\n");
					return -1;
				}
			}
		}
		return 1;
	}

}



#endif