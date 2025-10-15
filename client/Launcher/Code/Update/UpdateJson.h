#ifndef UpdateUtil_H
#define UpdateUtil_H

#include "JsonUtil.h"
#include "../Utils/StringUtil.h"
#include "../Download/DownloadManager.h"

typedef void(*SetDownloadSite_Func)(std::wstring DownloadSite, std::wstring AppSite);

extern std::vector<char> ms_data;

static SetDownloadSite_Func m_SetDownloadSite_Func = NULL;

static std::wstring ms_wsChannelName;

class UpdateJson
{
public:
	
	static void SetCB_SetDownloadSite_Func(SetDownloadSite_Func ASetDownloadSite_Func)
	{
		m_SetDownloadSite_Func = ASetDownloadSite_Func;
	}
	static void onGetUpdateJson(DownloadOne* pDO)
	{
		int rcode = pDO->m_iResponseCode;
		std::string jsonStr;
		bool isSuccess = false;
		if (rcode == 200)
		{
			isSuccess = true;
			std::vector<char>* data = (std::vector<char>*)pDO->m_pData;
			std::vector<char>::iterator it;
			for (it = data->begin(); it != data->end(); it++)
			{
				jsonStr += *it;
			}
		}

		std::string sDownloadSite;
		std::string sAppSite;

		if (isSuccess)
		{
			Json* root = Json_create(jsonStr.c_str());
			if (root)
			{
				Json* gameConfig = Json_getItem(root, "GameConfigInfo");
				if (gameConfig && gameConfig->type == Json_Object)
				{
					Json* _pathchUrl = Json_getItem(gameConfig, "PatchUrl");
					if (_pathchUrl && _pathchUrl->type == Json_String)
					{
						sDownloadSite = _pathchUrl->valueString;
					}

					Json* _updateUrl = Json_getItem(gameConfig, "UpdateUrl");
					if (_updateUrl && _updateUrl->type == Json_String)
					{
						sAppSite = _updateUrl->valueString;
					}
				}

				//Json_dispose(root);
			}

		}
		else //Á¬½ÓÊ§°Ü
		{

		}
		if (m_SetDownloadSite_Func)
		{
			std::wstring wsDownloadSite = StringUtil::S2WS(sDownloadSite);
			std::wstring wsAppSite = StringUtil::S2WS(sAppSite);
			m_SetDownloadSite_Func(wsDownloadSite, wsAppSite);
		}
	}

	static void RequestUpdateJson(std::wstring wsURL, std::wstring wsVersionCaption, std::wstring wsChannelCaption)
	{
		std::wstring wsURLFull = wsURL + wsVersionCaption + L"/" + wsChannelCaption + L"/index.html";
		std::string strUrl = StringUtil::WS2S(wsURLFull);
		
		//"http://59.151.73.76:50000/1000/0.0.1/100/index.html"
		//"http://192.168.32.2:8080/6688/0.0.1/100/index.html"

		GetDownloadManager()->AddTask(wsURLFull, L"index.html", &ms_data, UpdateJson::onGetUpdateJson);
	}


	static void SetChannelNameInt(int iChannelName)
	{
		ms_wsChannelName = NumTowstring<int>(iChannelName);
	}

	static void SetChannelNameWStr(std::wstring wsChannelName)
	{
		ms_wsChannelName = wsChannelName;
	}
	static int GetChannelNameInt()
	{
		return WStrToNum<int>(GetChannelNameWStr());
	}
	static std::wstring GetChannelNameWStr()
	{
		ms_wsChannelName = L"100";
		return ms_wsChannelName;
	}
	static void SetChannelNameStr(std::string sChannelName)
	{
		SetChannelNameWStr(StringUtil::S2WS(sChannelName));
	}
	static std::string GetChannelNameStr()
	{
		return StringUtil::WS2S(GetChannelNameWStr());
	}
	static void ExitApp()
	{
		exit(0);
	}
};

#endif