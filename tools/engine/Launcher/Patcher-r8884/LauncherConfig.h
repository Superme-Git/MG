//////////////////////////////////////////////////////////////////////////
//
// LauncherConfig.h
//
// 配置信息管理器
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
// 获取运行程序自身文件名
//
LPCTSTR GetApplicationFileName();

//
// 获取可执行程序所在的目录
//
LPCTSTR GetModuleDir();

// Patcher 程序位于install directory 的bin/binD目录下
// 因此，InstallDir为ModuleDir的父目录
LPCTSTR GetInstallDir();

// 获取Launcher所在的文件夹名字
// 比如：launcher, launcher_tiyan
// launcher 为缺省值
LPCTSTR GetLauncherFolder();

// 设置Launcher所在的文件夹名字
// 比如：launcher, launcher_tiyan
// 对于体验版launcher,它从命令行传递该参数给patcher程序
// patcher程序调用SetLauncherFolder()记录该参数值
void SetLauncherFolder( LPCTSTR szFolder );

//
// 获取修仙游戏客户端程序目录
//
CString GetGameClientDir();

//
// 获取PP的目录
//
CString GetPerfectProtectorDir();

//
// 获取修仙游戏客户端程序的路径
//
CString GetGameClientPath();

//
// 获取Repair.exe程序的安装路径
//
CString GetRepairPath();

//
// 获取修仙游戏客户端启动程序的路径
//
CString GetBootstrapPath();

//
// 获得本地信息页面文件所在的目录
//
CString GetLauncherLocalPagePath();

// 
// 获取launcher上的Web页面的URL路径
// 如果本地离线页面不存在，返回在线页面地址.
//
CString GetLauncherWebPageUrl();

//
// 获取在线资源的下载Url
//
CString GetLauncherOnlineResourceUrl();

//
// 获取Launcher用户界面资源目录
//
CString GetLauncherUIResourceDir();

//
// 获取UI界面的布局配置文件
//
CString GetLauncherUILayoutFile();

//
// 获取launcher在线页面的路径
//
CString GetLauncherOnlineUrl();

//
// 获取launcher本地离线页面的路径
//
CString GetLauncherOfflineUrl();

//
// 获取 "选择服务器" UI界面的布局配置文件
//
CString GetSelectServerDlgUILayoutFile();

//
// 获取 "选择服务器列表" UI界面的布局配置文件
//
CString GetSelectServerListUILayoutFile();

//
// 获取 "手动下载更新包" 的url
//
CString GetOnlineDownloadPageUrl();

//
// 获取文本资源的配置文件 - launcher, patcher, repair
//
CString GetLauncherTextResourceFile();
CString GetPatcherTextResourceFile();
CString GetRepairTextResourceFile();

//
// 获取后台更新的频率(以分钟为单位，缺省值1小时)
//
DWORD GetBackgroundUpdateFrequency();
