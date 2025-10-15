//////////////////////////////////////////////////////////////////////////
//
// LauncherConfig.h
//
// ������Ϣ������
//
// author: yanghaibo
// date : 2008-11-11
//
#pragma once

class CLauncherConfig;

extern CLauncherConfig* LauncherConfig();

class CLauncherConfig
{
	friend CLauncherConfig* LauncherConfig();
	CLauncherConfig(void);
	~CLauncherConfig(void);

public:	
	BSTR GetValue( BSTR queryString );

protected:
	BOOL Init(CComPtr<IXMLDOMNode>& rXmlRootNode);
};

//
// ��ȡ���г��������ļ���
//
LPCTSTR GetApplicationFileName();

//
// ��ȡ��ִ�г������ڵ�Ŀ¼
//
LPCTSTR GetModuleDir();

// Patcher ����λ��install directory ��bin/binDĿ¼��
// ��ˣ�InstallDirΪModuleDir�ĸ�Ŀ¼
LPCTSTR GetInstallDir();

// ��ȡLauncher���ڵ��ļ�������
// ���磺launcher, launcher_tiyan
// launcher Ϊȱʡֵ
LPCTSTR GetLauncherFolder();

// ����Launcher���ڵ��ļ�������
// ���磺launcher, launcher_tiyan
// ���������launcher,���������д��ݸò�����patcher����
// patcher�������SetLauncherFolder()��¼�ò���ֵ
void SetLauncherFolder( LPCTSTR szFolder );

//
// ��ȡ������Ϸ�ͻ��˳���Ŀ¼
//
CString GetGameClientDir();

//
// ��ȡPP��Ŀ¼
//
CString GetPerfectProtectorDir();

//
// ��ȡ������Ϸ�ͻ��˳����·��
//
CString GetGameClientPath();

//
// ��ȡRepair.exe����İ�װ·��
//
CString GetRepairPath();

//
// ��ȡ������Ϸ�ͻ������������·��
//
CString GetBootstrapPath();

//
// ��ñ�����Ϣҳ���ļ����ڵ�Ŀ¼
//
CString GetLauncherLocalPagePath();

// 
// ��ȡlauncher�ϵ�Webҳ���URL·��
// �����������ҳ�治���ڣ���������ҳ���ַ.
//
CString GetLauncherWebPageUrl();

//
// ��ȡ������Դ������Url
//
CString GetLauncherOnlineResourceUrl();

//
// ��ȡLauncher�û�������ԴĿ¼
//
CString GetLauncherUIResourceDir();

//
// ��ȡUI����Ĳ��������ļ�
//
CString GetLauncherUILayoutFile();

//
// ��ȡlauncher����ҳ���·��
//
CString GetLauncherOnlineUrl();

//
// ��ȡlauncher��������ҳ���·��
//
CString GetLauncherOfflineUrl();

//
// ��ȡ "ѡ�������" UI����Ĳ��������ļ�
//
CString GetSelectServerDlgUILayoutFile();

//
// ��ȡ "ѡ��������б�" UI����Ĳ��������ļ�
//
CString GetSelectServerListUILayoutFile();

//
// ��ȡ "�ֶ����ظ��°�" ��url
//
CString GetOnlineDownloadPageUrl();

//
// ��ȡ�ı���Դ�������ļ� - launcher, patcher, repair
//
CString GetLauncherTextResourceFile();
CString GetPatcherTextResourceFile();
CString GetRepairTextResourceFile();

//
// ��ȡ��̨���µ�Ƶ��(�Է���Ϊ��λ��ȱʡֵ1Сʱ)
//
DWORD GetBackgroundUpdateFrequency();
