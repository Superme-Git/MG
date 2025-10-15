#pragma once

#include "zipcommon.h"

namespace ZIP
{
	enum 
	{
		FM_EXCL         = 0x00000001,  /*! �ļ�������� */
		FM_CREAT        = 0x00000002,  /*! �����ļ� */
		FM_TRUNC        = 0x00000004,  /*! ����ļ����� */
	};
	typedef unsigned int FILE_MODE;

	enum FILE_ACCESS
	{
		FA_RDONLY       = 0x00000001,	/*! open for reading only */
		FA_WRONLY       = 0x00000002,	/*! open for writing only */
		FA_RDWR         = 0x00000003,	/*! open for reading and writing */
	};

	// �ļ��Ƿ���ֻ����ʽ��
	inline bool IsFileReadOnly( FILE_ACCESS fa ) 
	{
		return (fa & FA_WRONLY) == 0;
	}

	// �ļ��Ƿ��Կ�д��ʽ��
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
		// ������data���������ܹ�����ȫ���ļ�����,���򷵻�false, 
		// GetLastError()����EC_INSUFFICIENT_BUFFER.
		// ���ܸú����ĵڶ�����������Ϊfsize_t, �����ڳ���size_t(-1)���ļ�(4G),
		// �ú������Ƿ���false, 
		// ��GetLastError()����EC_FILE_SIZE_TOO_LARGE.
		//
		virtual bool ReadAll( void* data, fsize_t size ) = 0; 

	};

}
