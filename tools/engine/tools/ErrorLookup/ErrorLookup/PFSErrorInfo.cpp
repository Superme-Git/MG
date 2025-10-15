#include "StdAfx.h"
#include "PFSErrorInfo.h"

namespace PFS
{
	enum
	{
		EC_SUCCESS		= 0,
		EC_DIR_HAS_ALREADY_MOUNT = -1000,	// 目录已经映射到了一个设备
		EC_INVALID_MOUNT_DEVICE,	// 无效的Mount设备
		EC_INVALID_DEVICE_NAME,		// 无效的Mount设备名
		EC_INVALID_MOUNT_DIR_NAME,	// 无效的Mount目标目录名
		EC_FSTYPE_UNEXISTING,		// 文件系统类型不存在
		EC_MOUNT_DIR_UNFOUND,		// 无法找到该被映射的目录
		EC_MOUNT_FAILED,			// Mount 文件系统失败
		EC_FS_BUSY_NOW,				// 文件系统正在被使用
		EC_FILE_NOT_MOUNT,			// 没有找到相应的映射目录
		EC_FILE_NOT_FOUND,			// 没有找到文件
		EC_FS_READONLY,				// 文件系统目前只读
		EC_FILE_OPEN_MODE,			// 文件打开方式不支持
		EC_NATIVE_DEVICE_LOST,		// 无法找到原始文件设备
		EC_COPY_DST_EXISTING,		// 文件拷贝的目标已经存在
		EC_MAP_UNFOUND,				// 文件名找不到映射
		EC_FILE_BAD_ZIPDATA,		// zip压缩的数据解压缩失败
		EC_FILE_READONLY,			// 当前文件只读
		EC_FILE_URLOPENERROR,		// url打开错误
		EC_INVALID_VERSION_INFO,	// 无效的版本信息
		EC_INVALID_FILENAME,		// 无效的文件名
		EC_INVALID_DIRNAME,			// 无效的目录名
		EC_DIR_NOT_FOUND,			// 目录不存在(无法删除)
		EC_DIR_ALREADY_EXISTING,	// 目录已存在(不能再次创建)
		EC_DIR_NOT_EMPTY,			// (不允许删除)非空目录
		EC_ACCESS_DENIED,			// 访问被拒绝(权限不足)
		EC_SHARING_VIOLATION,		// 访问违例(比如删除一个已打开的文件)
		EC_UNMOUNT_FAILED,			// UnMount失败
		EC_INVALID_PATCH,			// 无效的补丁包
		EC_PATCH_NOT_MATCH,			// 补丁包版本不匹配本地客户端
		EC_FILE_NOTOPEN,			// 文件尚未打开
		EC_FILE_WRITEONLY,			// 文件只支持写操作
		EC_INSUFFICIENT_BUFFER,		// 缓冲区不足以容纳所请求的数据
		EC_FILESIZE_TOO_LARGE,		// 文件大小超过内存所允许的最大值,无法完成此操作.
		EC_INVALID_META_FILE,		// 不合法的MetaFile文件格式
		EC_UNSUPPORT_META_FILE_VERSION,	// 无效的MetaFile版本或MetaFile版本太高

		EC_FILTER_ERROR			= -1100,// 一般过滤器错误
		EC_FILTER_INVALID_ORI_DIR,		// 无效的过滤器原目录
		EC_FILTER_INVALID_NEW_DIR,		// 无效的过滤器新目录
		EC_FILTER_ITEM_NOT_EXISTS,		// 不存在此过滤器配置项目
		
		EC_OPTION_GENERAL_ERROR = -1200,// 一般属性错误
		EC_OPTION_INVALID_GROUP,		// 无效的属性组
		EC_OPTION_INVALID_NAME,			// 无效的属性名
		EC_OPTION_INVALID_VALUE,		// 无效的属性值
		EC_OPTION_OUT_OF_RANGE,			// 属性值超出可允许的范围
		EC_OPTION_READ_ONLY,			// 属性只读（不支持修改此属性）
		EC_OPTION_SYSTEM_ERROR,			// 属性修改/读取时发生系统错误

		ECX_ENCODER_UNFOUND		= -2000,// 文件编码器没有找到
		ECX_DECODER_UNFOUND,			// 文件解码器没有找到
		ECX_BADFORMAT,					// 错误的文件格式
	};

	struct ErrorMessageDefinition
	{
		int code;
		LPCWSTR message_1033;
		LPCWSTR message_2052;
	};

	static ErrorMessageDefinition defs[]  = 
	{
		{
			EC_SUCCESS,
			L"The operation completed successfully.",
			L"操作成功完成。",
		},

		{
			EC_DIR_HAS_ALREADY_MOUNT /*= -1000*/,
			L"The directory has already been mount to pfs system.",
			L"目录已经映射到了一个设备。",
		},
		
		{
			EC_INVALID_MOUNT_DEVICE,
			L"Invalid mount device.",
			L"无效的挂载设备。",
		},
		
		{
			EC_INVALID_DEVICE_NAME,
			L"Invalid mount device name.",
			L"无效的挂载设备名。",
		},
		
		{
			EC_INVALID_MOUNT_DIR_NAME, 
			L"Invalid mount directory name.",
			L"无效的挂载目标目录名。",
		},
		
		{
			EC_FSTYPE_UNEXISTING,
			L"Invalid PFS file system type.",
			L"不存在的PFS文件系统类型。",
		},
		
		{
			EC_MOUNT_DIR_UNFOUND,
			L"Can not find specified pfs mount directory.",
			L"无法找到指定的被映射的PFS挂载目录。",
		},
		
		{
			EC_MOUNT_FAILED,
			L"Mount pfs device failed.",
			L"挂载PFS设备失败。",
		},
		
		{
			EC_FS_BUSY_NOW,
			L"The PFS file system is busy now.",
			L"PFS文件系统正在被使用。",
		},
		
		{
			EC_FILE_NOT_MOUNT,
			L"Can not find the specified file in pfs system.",
			L"没有找到PFS路径对应的映射目录。",
		},
		
		{
			EC_FILE_NOT_FOUND,
			L"Can not find the file.",
			L"文件未找到。",
		},
		
		{
			EC_FS_READONLY,
			L"Can not perform the writting operation on the readonly file system.",
			L"只读的PFS文件系统，不允许执行修改操作。",
		},
		
		{
			EC_FILE_OPEN_MODE,
			L"Invalid file open mode.",
			L"不支持指定的文件打开方式。",
		},
		
		{
			EC_NATIVE_DEVICE_LOST,
			L"Can not find the device in the file system underlying the operation system(OS).",
			L"在操作系统的文件系统中无法找到原始文件。",
		},
		
		{
			EC_COPY_DST_EXISTING,
			L"The destination file already exists when copy one to it.",
			L"文件拷贝的目标已经存在。",
		},
		
		{
			EC_MAP_UNFOUND,
			L"Can not find the file system based on the file path.",
			L"文件名找不到映射。",
		},
		
		{
			EC_FILE_BAD_ZIPDATA,
			L"Corrupt zip data in the file.",
			L"zip压缩的数据解压缩失败。",
		},
		
		{
			EC_FILE_READONLY,
			L"Can not perform writing operation on the readonly file.",
			L"只读的文件不允许写操作。",			
		},
		
		{
			EC_FILE_URLOPENERROR,
			L"Can not open the file by the URL.",
			L"无法打开统一资源定位符(URL)所指向的文件。",
		},
		
		{
			EC_INVALID_VERSION_INFO,
			L"Invalid version info.",
			L"无效的版本信息。",
		},
		
		{
			EC_INVALID_FILENAME,
			L"Invalid file name.",
			L"无效的文件名。",
		},
		
		{
			EC_INVALID_DIRNAME,
			L"Invalid directory name.",
			L"无效的目录名。",
		},
		
		{
			EC_DIR_NOT_FOUND,
			L"Can not find the directory.",
			L"目录不存在，无法删除。",
		},
		
		{
			EC_DIR_ALREADY_EXISTING,
			L"The directory has already been existing when trying to create a new one.",
			L"目录已存在，不能重复创建。",
		},
		
		{
			EC_DIR_NOT_EMPTY,			// (不允许删除)非空目录
			L"Can not remove the directory when it contains sub-nodes(directories or files).",
			L"不允许删除非空目录。",
		},
		
		{
			EC_ACCESS_DENIED,
			L"Access is denied.",
			L"访问被拒绝(权限不足)。",
		},
		
		{
			EC_SHARING_VIOLATION,// 访问违例(比如删除一个已打开的文件)
			L"The process cannot access the file because it is being used by another process.",
			L"文件被其它进程独占，本进程无法访问。",
		},
		
		{
			EC_UNMOUNT_FAILED,
			L"Unmount PFS file system failed.",
			L"卸载PFS文件系统失败。",
		},
		
		{
			EC_INVALID_PATCH,
			L"Invalid patch.",
			L"无效的补丁包。",
		},
		
		{
			EC_PATCH_NOT_MATCH,
			L"The patch does not match the client based on the version.",
			L"补丁包版本与本地客户端不匹配。",
		},
		
		{
			EC_FILE_NOTOPEN,
			L"The file is not open.",
			L"文件尚未打开。",
		},
		
		{
			EC_FILE_WRITEONLY,
			L"The file is write-only.",
			L"文件只支持写操作。",
		},
		
		{
			EC_INSUFFICIENT_BUFFER,
			L"The data area passed to the call is too small.",
			L"缓冲区不足以容纳所请求的数据。",
		},
		
		{
			EC_FILESIZE_TOO_LARGE,
			L"The file size exceeds the upper limit of the memory size.",
			L"文件大小超过内存所允许的最大值,无法完成此操作。",
		},
		
		{
			EC_INVALID_META_FILE,
			L"Invalid meta file data.",
			L"不合法的元文件格式。",
		},
		
		{
			EC_UNSUPPORT_META_FILE_VERSION,
			L"Invalid meta file version or the file version is too high to support.",
			L"无效的元文件版本或元文件版本太高。",
		},
		
		{		
			EC_FILTER_ERROR/*	= -1100*/,
			L"General filter operation error.",
			L"一般过滤器错误。",
		},
		
		{
			EC_FILTER_INVALID_ORI_DIR,
			L"Invalid filter original directory.",
			L"无效的过滤器原目录",
		},
		
		{
			EC_FILTER_INVALID_NEW_DIR,
			L"Invalid filter new directory.",
			L"无效的过滤器新目录。",
		},
		
		{
			EC_FILTER_ITEM_NOT_EXISTS,
			L"Can not find the filter item.",
			L"不存在此过滤器配置项目。",
		},
		
		{
			EC_OPTION_GENERAL_ERROR/* = -1200*/,
			L"General option operation error.",
			L"一般选项错误。",
		},
		
		{
			EC_OPTION_INVALID_GROUP,
			L"Invalid option group.",
			L"无效的选项属性组。",
		},
		
		{
			EC_OPTION_INVALID_NAME,
			L"Invalid option name.",
			L"无效的选项属性名。",
		},
		
		{
			EC_OPTION_INVALID_VALUE,
			L"Invalid option value.",
			L"无效的选项属性值。",
		},
		
		{
			EC_OPTION_OUT_OF_RANGE,
			L"Option value is out of range.",
			L"选项的属性值超出可允许的范围。",
		},
		
		{
			EC_OPTION_READ_ONLY,
			L"The option is readonly.",
			L"属性只读（不支持修改此属性）。",
		},
		
		{
			EC_OPTION_SYSTEM_ERROR,
			L"System error occurred when access the option data.",
			L"属性修改/读取时发生系统错误。",
		},
		
		{
			ECX_ENCODER_UNFOUND		/*= -2000*/, 
			L"Invalid encoder.",
			L"没有找到文件编码器。",
		},
		
		{
			ECX_DECODER_UNFOUND,
			L"Invalid decoder.",
			L"没有找到文件解码器。",
		},
		
		{
			ECX_BADFORMAT,
			L"File data is corrupt or with unrecognized file format.",
			L"无效的文件数据或不支持的文件格式。",
		},
	};

	static int _GetErrorDescription( LANGID langId, int nErrorCode, 
		LPWSTR lpBuffer, DWORD nSize )
	{
		if( PRIMARYLANGID(langId) == LANG_CHINESE )
		{
			for( int i = 0; i < ARRAYSIZE(defs); i ++ )
			{
				if( nErrorCode == defs[i].code )
				{
					int nLen = (int)wcslen(defs[i].message_2052);
					if( nLen < (int) nSize )
					{
						wcscpy_s(lpBuffer, nSize, defs[i].message_2052 );
						return nLen;
					}
					else
					{
						return -nLen;
					}
				}
			}
		}
		else // if( PRIMARYLANGID(langId) == LANG_ENGLISH )
		{
			for( int i = 0; i < ARRAYSIZE(defs); i ++ )
			{
				if( nErrorCode == defs[i].code )
				{
					int nLen = (int)wcslen(defs[i].message_1033 );
					if( nLen < (int)nSize )
					{
						wcscpy_s(lpBuffer, nSize, defs[i].message_1033 );
						return nLen;
					}
					else
					{
						return -nLen;
					}
				}
			}
		}

		return 0; // 不是PFS所定义的错误码。
	}
	
	int GetErrorDescription( LANGID langId, int nErrorCode, 
		LPWSTR lpBuffer, DWORD nSize )
	{
		int code = _GetErrorDescription( langId, 
			nErrorCode, lpBuffer, nSize );

		return code;		
	}
	

}// namespace PFS