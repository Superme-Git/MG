#pragma once
#include <string>

const TCHAR SECTION[] = L"setting";
const TCHAR TYPE[] = L"server";			// ��ͨ or ����
const TCHAR URL[] = L"url";				// ������ url

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
// ��Ӧ��VersionInfo�е�type
//
enum UPDATE_MODE
{
	UPDATE_MODE_DEFAULT		= 0, // ȱʡ����
	UPDATE_MODE_DIFFERENTIAL= 0, // �������
	UPDATE_MODE_WHOLE		= 1, // ȫ�����£��㰲װ��ʽ��
	UPDATE_MODE_BACKGROUND	= 2, // ��̨����
	UPDATE_MODE_PATCH		= 3, // ����������
	UPDATE_MODE_REPAIR		= 4, // �޸�ģʽ����(�������汾�뵱ǰ�汾��ͬ)
	UPDATE_MODE_AUTOPATCH	= 100,	// �Զ����ز���������
};

// ���·���������
enum UPDATE_SERVER_TYPE
{
	UPDATE_SERVER_NETCOM	= 0,	// ��ͨ
	UPDATE_SERVER_TELECOM	= 1,	// ����
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

	// �õ�ini�ļ�����ķ���������
	int GetIniFileServerType()
	{
		// ȱʡֵΪ0��Ҫ��Ϊ����ѡ��������Ի���ȱʡ��ʾ��һ��
		return GetPrivateProfileInt( SECTION, TYPE, 0, m_file.c_str());
	}

	// �õ�����������
	int GetServerType()
	{
		// ȱʡֵΪ-1��ҪΪ�������Ƿ�ɹ���ini�ļ���ȡ������������
		int res = GetPrivateProfileInt( SECTION, TYPE, -1, m_file.c_str());
		if( res == -1)
		{
			// û�б�������ϴ�ѡ��ķ��������ͣ����ѡ��һ��
			// ��Ϊ���������� ��ͨ:���� Ϊ 1:2 ���������ѡ�����ҲΪ 1:2
			srand( GetTickCount());
			res = rand() % 300;
			res = ( res >= 100) ? 1 : 0;
		}
		return res;
	}

	// ���÷���������
	void SetServerType( int type)
	{
		std::wostringstream woss;
		woss << type;
		WritePrivateProfileString( SECTION, TYPE, woss.str().c_str(), m_file.c_str());
	}

	// ���÷�����url
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

	// ���ݷ��������ͺ͸������͵õ�Ĭ�ϵ�url
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

	// �õ�������url
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