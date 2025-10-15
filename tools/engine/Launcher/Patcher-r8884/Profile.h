#pragma once
#include <string>

const TCHAR SECTION[] = L"setting";
const TCHAR TYPE[] = L"server";			// 网通 or 电信
const TCHAR URL[] = L"url";				// 服务器 url

#ifdef VERSION_TIYAN
const TCHAR DEFURL_NETCOM_NORMAL[] = L"http://patch2.mhzx.locojoy.com/mhzx/";
const TCHAR DEFURL_NETCOM_BACKGROUND[] = L"http://patch4.mhzx.locojoy.com/mhzx2/";
const TCHAR DEFURL_NETCOM_AUTOPATCH[] = L"http://patch100.mhzx.locojoy.com/mhzx3/";

const TCHAR DEFURL_TELECOM_NORMAL[] = L"http://patch3.mhzx.locojoy.com/mhzx/";
const TCHAR DEFURL_TELECOM_BACKGROUND[] = L"http://patch5.mhzx.locojoy.com/mhzx2/";
const TCHAR DEFURL_TELECOM_AUTOPATCH[] = L"http://patch100.mhzx.locojoy.com/mhzx3/";
#else
// TODO:
const TCHAR DEFURL_NETCOM_NORMAL[] = L"http://patch2.mhzx.locojoy.com/mhzx/";
const TCHAR DEFURL_NETCOM_BACKGROUND[] = L"http://patch4.mhzx.locojoy.com/mhzx2/";
const TCHAR DEFURL_NETCOM_AUTOPATCH[] = L"http://patch100.mhzx.locojoy.com/mhzx3/";
const TCHAR DEFURL_TELECOM_NORMAL[] = L"http://patch3.mhzx.locojoy.com/mhzx/";
const TCHAR DEFURL_TELECOM_BACKGROUND[] = L"http://patch5.mhzx.locojoy.com/mhzx2/";
const TCHAR DEFURL_TELECOM_AUTOPATCH[] = L"http://patch100.mhzx.locojoy.com/mhzx3/";
#endif

//
// 对应于VersionInfo中的type
//
enum UPDATE_MODE
{
	UPDATE_MODE_DEFAULT		= 0, // 缺省更新
	UPDATE_MODE_DIFFERENTIAL= 0, // 差异更新
	UPDATE_MODE_WHOLE		= 1, // 全部更新（零安装方式）
	UPDATE_MODE_BACKGROUND	= 2, // 后台更新
	UPDATE_MODE_PATCH		= 3, // 补丁包更新
	UPDATE_MODE_REPAIR		= 4, // 修复模式更新(补丁包版本与当前版本相同)
	UPDATE_MODE_AUTOPATCH	= 100,	// 自动下载补丁包更新
};

// 更新服务期类型
enum UPDATE_SERVER_TYPE
{
	UPDATE_SERVER_NETCOM	= 0,	// 网通
	UPDATE_SERVER_TELECOM	= 1,	// 电信
};

class CProfile
{
	std::wstring m_file;

public:
	bool SetFileName( LPCTSTR pszFile)
	{
		if( pszFile)
		{
			m_file = pszFile;
			return true;
		}
		return false;
	}

	// 得到ini文件保存的服务器类型
	int GetIniFileServerType()
	{
		// 缺省值为0主要是为了让选择服务器对话框缺省显示第一项
		return GetPrivateProfileInt( SECTION, TYPE, 0, m_file.c_str());
	}

	// 得到服务器类型
	int GetServerType()
	{
		// 缺省值为-1主要为了区分是否成功从ini文件读取到服务器类型
		int res = GetPrivateProfileInt( SECTION, TYPE, -1, m_file.c_str());
		if( res == -1)
		{
			// 没有保存玩家上次选择的服务器类型，随机选择一个
			// 因为服务器数量 网通:电信 为 1:2 ，所以随机选择概率也为 1:2
			srand( GetTickCount());
			res = rand() % 300;
			res = ( res >= 100) ? 1 : 0;
		}
		return res;
	}

	// 设置服务器类型
	void SetServerType( int type)
	{
		std::wostringstream woss;
		woss << type;
		WritePrivateProfileString( SECTION, TYPE, woss.str().c_str(), m_file.c_str());
	}

	// 设置服务器url
	void SetServerUrl( const std::wstring& url, int UpdateMode)
	{
		std::wostringstream woss;
		woss << URL;
		if( UpdateMode != UPDATE_MODE_DEFAULT)
		{
			woss << UpdateMode;
		}
		WritePrivateProfileString( SECTION, woss.str().c_str(), url.c_str(), m_file.c_str());
	}

	// 根据服务器类型和更新类型得到默认的url
	bool GetDefaultUrl( std::wstring& url, int type, int UpdateMode)
	{
		if( type == UPDATE_SERVER_NETCOM )
		{
			switch( UpdateMode)
			{
			case UPDATE_MODE_BACKGROUND:
				url = DEFURL_NETCOM_BACKGROUND;
				break;
			case UPDATE_MODE_AUTOPATCH:
				url = DEFURL_NETCOM_AUTOPATCH;
				break;
			default:
				url = DEFURL_NETCOM_NORMAL;
				break;
			}
		}
		else
		{
			switch( UpdateMode)
			{
			case UPDATE_MODE_BACKGROUND:
				url = DEFURL_TELECOM_BACKGROUND;
				break;
			case UPDATE_MODE_AUTOPATCH:
				url = DEFURL_TELECOM_AUTOPATCH;
				break;
			default:
				url = DEFURL_TELECOM_NORMAL;
				break;
			}
		}
		return true;
	}

	// 得到服务器url
	bool GetServerUrl( std::wstring& url, int UpdateMode)
	{
		std::wostringstream woss;
		woss << URL;
		if( UpdateMode != UPDATE_MODE_DEFAULT)
		{
			woss << UpdateMode;
		}

		std::wstring defaultUrl;
		const size_t len = 1024;
		url.resize( len);
		DWORD res = GetPrivateProfileString( SECTION, woss.str().c_str(), defaultUrl.c_str(), 
			(LPWSTR)url.c_str(), len, m_file.c_str());
		url.resize( res);
		return true;
	}

	static CProfile& GetInstance()
	{
		static CProfile s_instance;
		return s_instance;
	}
};