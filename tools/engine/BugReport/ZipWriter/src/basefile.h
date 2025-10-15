#pragma once

#include "zipcommon.h"

namespace ZIP
{
	enum 
	{
		FM_EXCL         = 0x00000001,  /*! 文件必须存在 */
		FM_CREAT        = 0x00000002,  /*! 创建文件 */
		FM_TRUNC        = 0x00000004,  /*! 清除文件内容 */
	};
	typedef unsigned int FILE_MODE;

	enum FILE_ACCESS
	{
		FA_RDONLY       = 0x00000001,	/*! open for reading only */
		FA_WRONLY       = 0x00000002,	/*! open for writing only */
		FA_RDWR         = 0x00000003,	/*! open for reading and writing */
	};

	// 文件是否以只读方式打开
	inline bool IsFileReadOnly( FILE_ACCESS fa ) 
	{
		return (fa & FA_WRONLY) == 0;
	}

	// 文件是否以可写方式打开
	inline bool IsFileWritable( FILE_ACCESS fa )
	{
		return (fa & FA_WRONLY) != 0;
	}
	enum FILE_SEEK_METHOD
	{
		FSM_SET = 0,
		FSM_CUR,
		FSM_END,
	};

	class CBaseFile
	{
	protected:
		CBaseFile() {}
		virtual ~CBaseFile() {}
		virtual void Delete() { /*delete this;*/ }

	public:
		virtual size_t Read( void* data, size_t size) = 0;
		virtual size_t Write( const void* data, size_t size) = 0;
		virtual fsize_t GetSize() = 0;
		virtual fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm) = 0;
		virtual fsize_t GetPos() = 0;
		virtual bool SetEnd() = 0;	

		//
		// 缓冲区data必须至少能够容纳全部文件数据,否则返回false, 
		// GetLastError()返回EC_INSUFFICIENT_BUFFER.
		// 尽管该函数的第二个参数类型为fsize_t, 但对于超过size_t(-1)的文件(4G),
		// 该函数总是返回false, 
		// 且GetLastError()返回EC_FILE_SIZE_TOO_LARGE.
		//
		virtual bool ReadAll( void* data, fsize_t size ) = 0; 

	};

}
