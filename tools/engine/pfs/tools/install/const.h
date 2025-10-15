#pragma once

enum
{
	READCOPYRIGHT = 0,				//	安装前请仔细阅读最终用户软件许可协议:
	COPYRIGHT,						//	最终用户软件许可协议
	AGREE,							//	同意
	DISAGREE,						//	不同意
	BROWSE,							//	浏览
	PREVSTEP,						//	上一步
	NEXTSTEP,						//	下一步
	END,							//	结束
	ASK,							//	询问
	JUMP,							//	跳过
	CHECK_QUESTION,					//  安装程序从网络下载的过程中可能会导致部分数据丢失，你需要对安装程序数据包做完整性校验吗？	
	CHECK,							//	验证
	CHECKPROGRESS,					//	正验证安装程序数据包的完整性,请稍候...
	CANCEL,							//	取消
	PROGRESS,						//	安装进度
	FINISH,							//	安装结果
	OK = 16,						//	确定

	APPNAME = 20,					//	2DGame安装程序
	NEEDSPACE,						//	请选择安装路径(需要空间:%dMB)
	DISKFREESPACE,					//	磁盘剩余空间(%dMB)
	SPACENOTENOUGH,					//	磁盘剩余空间不够，请选择有足够剩余空间磁盘!
	TOTALPROGRESS,					//	总进度：      	%d%%
	PROGRESSINIT,					//	正在分析文件，请稍候...
	CURRENTFILE,					//	当前处理文件：  %s
	USERSTOP,						//	安装还未完成, 您真的要结束安装吗?
	USERSTOPCHECK,					//	你确信要结束文件的完整性检测吗?
	INSTALLSUCCESS = 29,			//	已经全部安装完毕！
	
	ERRORREASON = 30,				//	安装错误! 原因是: %s
	ERROR_UNKNOWN,					//	未知错误.
	ERROR_USERSTOP,					//	用户终止.
	ERROR_CHECK,					//	安装程序数据包不完整

	ERROR_EC_DIR_HAS_ALREADY_MOUNT = 40,	//	%s 目录已经映射
	ERROR_EC_INVALID_MOUNT_DEVICE,	//	%s 无效的Mount设备
	ERROR_EC_INVALID_DEVICE_NAME,	//	%s 无效的Mount设备名
	ERROR_EC_INVALID_MOUNT_DIR_NAME,//	%s 无效的Mount目标目录名
	ERROR_EC_FSTYPE_UNEXISTING,		//	%s 文件系统类型不存在
	ERROR_EC_MOUNT_FAILED,			//	%s Mount 文件系统失败
	ERROR_EC_MOUNT_DIR_UNFOUNT,		//	%s 映射目录无法找到
	ERROR_EC_FS_BUSY_BOW,			//	%s 文件系统正在被使用
	ERROR_EC_FILE_NOT_MOUNT,		//	%s 映射文件无法找到
	ERROR_EC_FILE_NOT_FOUND,		//	%s 文件无法找到
	ERROR_EC_FS_READONLY,			//	%s 文件系统目前只读
	ERROR_EC_FILE_OPEN_MODE,		//	%s 文件打开方式不支持
	ERROR_EC_NATIVE_DEVICE_LOST,	//	%s 原始文件设备无法找到
	ERROR_EC_COPY_DST_EXISTING,		//	%s 文件拷贝的目标已经存在
	ERROR_EC_MAP_UNFOUND,			//	%s 文件名找不到映射
	ERROR_EC_FILE_BAD_ZIPDATA,		//	%s zip压缩的数据解压缩失败
	ERROR_EC_FILE_READONLY,			//	%s 文件只读
	ERROR_EC_FILE_URLOPENERROR,		//	%s url打开错误
	ERROR_EC_INVALID_VERSION_INFO,	//	%s 无效的版本信息
	ERROR_EC_INVALID_FILENAME,		//	%s 无效的文件名
	ERROR_EC_INVALID_DIRNAME,		//	%s 无效的目录名
	ERROR_EC_DIR_NOT_FOUND,			//	%s 目录不存在(无法删除)
	ERROR_EC_DIR_ALREADY_EXISTING,	//	%s 目录已存在(不能再次创建)
	ERROR_EC_DIR_NOT_EMPTY,			//	%s (不允许删除)非空目录
	ERROR_EC_ACCESS_DENIED,			//	%s 访问被拒绝(权限不足)
	ERROR_EC_SHARING_VIOLATION,		//	%s 访问违例(比如删除一个已打开的文件)
	ERROR_EC_UNMOUNT_FAILED,		//	%s UnMount失败
	ERROR_EC_INVALID_PATCH,			//	%s 无效的补丁包
	ERROR_EC_PATCH_NOT_MATCH,		//	%s 补丁包版本不匹配本地客户端

	ERROR_ECX_ENCODER_UNFOUND = 70,		//	%s 文件编码器没有找到
	ERROR_ECX_DECODER_UNFOUND,		//	%s 文件解码器没有找到
	ERROR_ECX_BADFORMAT,			//	%s 文件格式错误

	ERROR_SYSTEMERROR,				//	%s 文件操作出错，%s

	CANCELVERIFY_INPROGRESS = 80,	// 正在取消验证过程，请稍候...
	CANCELINSTALL_INPROGRESS,		// 正在退出安装程序，请稍候...
	CHECKBOX_CREATELINK,			// 在桌面创建游戏图标
	CHECKBOX_STARTGAME,				// 启动游戏
	AUTO_INSTALL_360,
	AUTO_INSTALL_UNITY3D,
	UNITY3D_VERSION,
	

	ERROR_NOT_SUSPEND_OS64 = 90,	// %s 暂不支持64位操作系统
	ERROR_MD5FILE_UNFOUND = 91,		// 缺少安装数据包(%s)的md5文件(%s.md5)
};