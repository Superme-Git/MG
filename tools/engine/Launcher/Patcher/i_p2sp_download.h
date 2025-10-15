

#ifndef __I_P2SP_DOWNLOAD_H__
#define __I_P2SP_DOWNLOAD_H__

#include <Windows.h>

#ifdef P2SP_ENGINE_DLL
#	ifdef P2SP_ENGINE_EXPORT
#		define P2SP_ENGINE_API    __declspec(dllexport)
#	else
#		define P2SP_ENGINE_API    __declspec(dllimport)
#	endif //P2SP_ENGINE_LIB_EXPORT
#	define EXTERN_C_BEGIN	extern "C" {
#	define EXTERN_C_END };
#else
#	define P2SP_ENGINE_API
#	define EXTERN_C_BEGIN
#	define EXTERN_C_END
#endif //P2SP_ENGINE_DLL

/// 设置项目标志
enum P2P_SETTING_ITEM {
	P2P_SETTING_ITEM_MAX_DOWNLOAD_NUM	= 0x1 << 0, //设置最大下载任务数，默认是5
	P2P_SETTING_ITEM_MAX_DOWNLOAD_SPEED = 0x1 << 1,	//设置最大下载速度，所有任务共享，默认0（不限制）
	P2P_SETTING_ITEM_MAX_UPLOAD_SPEED	= 0x1 << 2, //设置最大上传速度，所有任务共享，默认0（不限制）
	P2P_SETTING_ITEM_FILE_SAVE_PATH		= 0x1 << 3, //设置下载保存路径，默认为[系统盘符\PWRD]

	//如果速度小于P2P_SETTING_ITEM_TASK_TIMEOUT_SPEED并持续P2P_SETTING_ITEM_TASK_TIMEOUT时间，返回下载失败
	P2P_SETTING_ITEM_TASK_TIMEOUT		= 0x1 << 4, 
	P2P_SETTING_ITEM_TASK_TIMEOUT_SPEED = 0x1 << 5, 
};
/// 所有项标志
#define P2P_SETTING_ITEM_ALL P2P_SETTING_ITEM_MAX_DOWNLOAD_NUM | P2P_SETTING_ITEM_MAX_DOWNLOAD_SPEED \
	| P2P_SETTING_ITEM_MAX_UPLOAD_SPEED | P2P_SETTING_ITEM_FILE_SAVE_PATH | P2P_SETTING_ITEM_TASK_TIMEOUT \
	| P2P_SETTING_ITEM_TASK_TIMEOUT_SPEED 

/// 设置参数结构
struct SettingItemT {
	UINT	mask;					//P2P_SETTING_ITEM 设置项组合
	UINT	maxDownloadNum;			//最大下载任务数
	UINT	maxDownloadSpeed;		//最大下载速度(字节)
	UINT	maxUploadSpeed;			//最大上传速度(字节)
	UINT	taskTimeout;			//超时时间(秒)	   默认为300
	UINT	taskTimeoutSpeed;		//超时时速度(字节) 默认为10240
	WCHAR	savePath[MAX_PATH + 1];	//保存路径
};

/// 任务状态
enum P2P_TASK_STATE {
    P2P_TASK_STATE_NOREADY,     //未准备好
	P2P_TASK_STATE_READY,		//准备
	P2P_TASK_STATE_DOWNLOADING,	//下载
	P2P_TASK_QUEUING,			//排队
	P2P_TASK_STATE_STOP,		//停止
	P2P_TASK_STATE_COMPLETE,	//完成
	P2P_TASK_STATE_ERROR,		//错误
};

/// 任务信息
struct TaskInfomationT {
	CHAR				md5[33];		//MD5码
	P2P_TASK_STATE		state;			//任务状态，见P2P_TASK_STATE定义	
	USHORT				err;			//错误码，见TaskErrorCode定义
	UINT				dspeed;			//下载速度 (字节)
	UINT				uspeed;			//上传速度 (字节)
	UINT64				size;			//文件大小 (字节)
	UINT64				complateSize;	//完成大小 (字节)
	WCHAR	savePath[MAX_PATH + 1];		//完整路径
};

/// 错误码含义
enum TaskErrorCode {
	P2PE_OK,

	P2PE_TASK_ALREADY_EXISTS,	//任务已经存在
	P2PE_DOWNLOAD_TASK_SEED,	//任务种子下载失败
	P2PE_PARSE_TASK_SEED,		//任务种子解析失败
	P2PE_CHECK_TASK_SEED,		//任务种子参数校验失败

	P2PE_STARTTASK_INEXISTENT,	//任务不存在
	P2PE_STARTTASK_DISKFULL,	//剩余磁盘空间不足
	P2PE_STARTTASK_QUEUING,		//任务正在排队

	P2PE_HTTPDOWNLOAD_PARSEURL,			//解析URL失败
	P2PE_HTTPDOWNLOAD_RESOLVEHOST,		//解析HOST失败
	P2PE_HTTPDOWNLOAD_CONNECTSERVER,	//连接服务器失败
	P2PE_HTTPDOWNLOAD_REQUEST,			//发送请求失败
	P2PE_HTTPDOWNLOAD_READSTATUSLINE,	//读取HTTP返回头失败
	P2PE_HTTPDOWNLOAD_UNSUPPORTEPARTIAL,	//不支持断点
	P2PE_HTTPDOWNLOAD_READHEADERS,			//读取HTTP返回头失败
	P2PE_HTTPDOWNLOAD_READDATA,				//读取内容失败

	P2PE_HTTPDOWNLOAD_TRY_TIMES_OVERRUN,	//错误次数太多
	P2PE_STARTTASK_CREATE_FILE_ERROR,		//创建任务失败

	P2PE_TIMEOUT = 1000,					//速度不满足设置的最小值，且持续时间超过限制值（P2P_SETTING_ITEM_TASK_TIMEOUT）
};

EXTERN_C_BEGIN
	/// 更新、加载下载DLL，并加载该游戏的任务信息
	P2SP_ENGINE_API bool p2spStart(int gameID);

	/// 设置、获取下载参数
	P2SP_ENGINE_API bool p2spSetting(const SettingItemT* setting); 
	P2SP_ENGINE_API bool p2spGetSetting(SettingItemT* setting); 
	
	/// 添加单个文件下载任务
	P2SP_ENGINE_API bool p2spAddDownload(const char* filemd5);

	/// 开始下载
	P2SP_ENGINE_API bool p2spStartDownload(const char* filemd5);

	/// 停止下载任务
	P2SP_ENGINE_API bool p2spStopDownload(const char* filemd5);

	/// 删除下载任务
	P2SP_ENGINE_API bool p2spDeleteDownload(const char* filemd5);

	/// 获取任务的相关
	P2SP_ENGINE_API bool p2spGetTaskInfomation(TaskInfomationT* info);

	/// 停止所有下载任务，并卸载DLL
	P2SP_ENGINE_API bool p2spStop();
EXTERN_C_END

#endif //__I_P2SP_DOWNLOAD_H__