#pragma once

namespace ZIP
{

	enum
	{
		EC_SUCCESS		= 0,
		EC_FILE_NOT_FOUND,			// û���ҵ��ļ�
		EC_FILE_OPEN_MODE,			// �ļ��򿪷�ʽ��֧��
		EC_FILE_NOT_OPEN,			// �ļ���δ��
		EC_FILE_READ_ONLY,			// ��ǰ�ļ�ֻ��
		EC_FILE_WRITE_ONLY,			// �ļ�ֻ֧��д����
		EC_INVALID_FILE_NAME,		// ��Ч���ļ���
		EC_INVALID_DIR_NAME,		// ��Ч��Ŀ¼��
		EC_COPY_DST_EXISTING,		// �ļ�������Ŀ���Ѿ�����
		EC_DIR_NOT_FOUND,			// Ŀ¼������(�޷�ɾ��)
		EC_DIR_ALREADY_EXISTING,	// Ŀ¼�Ѵ���(�����ٴδ���)
		EC_DIR_NOT_EMPTY,			// (������ɾ��)�ǿ�Ŀ¼
		EC_ACCESS_DENIED,			// ���ʱ��ܾ�(Ȩ�޲���)
		EC_SHARING_VIOLATION,		// ����Υ��(����ɾ��һ���Ѵ򿪵��ļ�)
		EC_INSUFFICIENT_BUFFER,		// ���������������������������
		EC_FILE_BAD_ZIPDATA,		// zipѹ�������ݽ�ѹ��ʧ��
		EC_FILE_SIZE_TOO_LARGE,		// �ļ���С�����ڴ�����������ֵ,�޷���ɴ˲���.
	};

	typedef unsigned long long	fsize_t; // 64bit���ļ���С���ļ�ָ��λ��
	typedef long long			foff_t;	 // 64bit���ļ�ָ�����ƫ����
	#define MAX_MEMORY_DATA_SIZE  ((size_t)(-1)) // �ڴ��е���������
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