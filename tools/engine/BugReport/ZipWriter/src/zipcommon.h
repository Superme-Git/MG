#pragma once

namespace ZIP
{

	enum
	{
		EC_SUCCESS		= 0,
		EC_FILE_NOT_FOUND,			// 没有找到文件
		EC_FILE_OPEN_MODE,			// 文件打开方式不支持
		EC_FILE_NOT_OPEN,			// 文件尚未打开
		EC_FILE_READ_ONLY,			// 当前文件只读
		EC_FILE_WRITE_ONLY,			// 文件只支持写操作
		EC_INVALID_FILE_NAME,		// 无效的文件名
		EC_INVALID_DIR_NAME,		// 无效的目录名
		EC_COPY_DST_EXISTING,		// 文件拷贝的目标已经存在
		EC_DIR_NOT_FOUND,			// 目录不存在(无法删除)
		EC_DIR_ALREADY_EXISTING,	// 目录已存在(不能再次创建)
		EC_DIR_NOT_EMPTY,			// (不允许删除)非空目录
		EC_ACCESS_DENIED,			// 访问被拒绝(权限不足)
		EC_SHARING_VIOLATION,		// 访问违例(比如删除一个已打开的文件)
		EC_INSUFFICIENT_BUFFER,		// 缓冲区不足以容纳所请求的数据
		EC_FILE_BAD_ZIPDATA,		// zip压缩的数据解压缩失败
		EC_FILE_SIZE_TOO_LARGE,		// 文件大小超过内存所允许的最大值,无法完成此操作.
	};

	typedef unsigned long long	fsize_t; // 64bit的文件大小和文件指针位置
	typedef long long			foff_t;	 // 64bit的文件指针相对偏移量
	#define MAX_MEMORY_DATA_SIZE  ((size_t)(-1)) // 内存中的数据上限
	enum { FILE_SIZE_ERROR = -1, };

	class CZipFileBadFormat
	{
	
	};
	
	class CZipWriteError
	{
	
	};

	class CUncopyableObject
	{
	private:
		CUncopyableObject( const CUncopyableObject&){};
		CUncopyableObject& operator=( const CUncopyableObject&){return *this;};
	protected:
		CUncopyableObject(){};
		virtual ~CUncopyableObject(){};
	};
}