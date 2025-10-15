#include "i_p2sp_download.h"

class P2SPEngine
{
	static HMODULE s_hHandle;

private:
	P2SPEngine();
	P2SPEngine(const P2SPEngine& );

public:
	/// 更新、加载下载DLL，并加载该游戏的任务信息
	static bool dll_p2spStart();

	/// 设置、获取下载参数
	static bool dll_p2spSetting(const SettingItemT* setting); 
	static bool dll_p2spGetSetting(SettingItemT* setting); 

	/// 添加单个文件下载任务
	static bool dll_p2spAddDownload(const char* filemd5);

	/// 开始下载
	static bool dll_p2spStartDownload(const char* filemd5);

	/// 停止下载任务
	static bool dll_p2spStopDownload(const char* filemd5);

	/// 删除下载任务
	static bool dll_p2spDeleteDownload(const char* filemd5);

	/// 获取任务的相关
	static bool dll_p2spGetTaskInfomation(TaskInfomationT* info);

	/// 停止所有下载任务，并卸载DLL
	static bool dll_p2spStop();
};