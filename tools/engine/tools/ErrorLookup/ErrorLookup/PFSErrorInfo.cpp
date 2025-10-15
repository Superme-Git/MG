#include "StdAfx.h"
#include "PFSErrorInfo.h"

namespace PFS
{
	enum
	{
		EC_SUCCESS		= 0,
		EC_DIR_HAS_ALREADY_MOUNT = -1000,	// Ŀ¼�Ѿ�ӳ�䵽��һ���豸
		EC_INVALID_MOUNT_DEVICE,	// ��Ч��Mount�豸
		EC_INVALID_DEVICE_NAME,		// ��Ч��Mount�豸��
		EC_INVALID_MOUNT_DIR_NAME,	// ��Ч��MountĿ��Ŀ¼��
		EC_FSTYPE_UNEXISTING,		// �ļ�ϵͳ���Ͳ�����
		EC_MOUNT_DIR_UNFOUND,		// �޷��ҵ��ñ�ӳ���Ŀ¼
		EC_MOUNT_FAILED,			// Mount �ļ�ϵͳʧ��
		EC_FS_BUSY_NOW,				// �ļ�ϵͳ���ڱ�ʹ��
		EC_FILE_NOT_MOUNT,			// û���ҵ���Ӧ��ӳ��Ŀ¼
		EC_FILE_NOT_FOUND,			// û���ҵ��ļ�
		EC_FS_READONLY,				// �ļ�ϵͳĿǰֻ��
		EC_FILE_OPEN_MODE,			// �ļ��򿪷�ʽ��֧��
		EC_NATIVE_DEVICE_LOST,		// �޷��ҵ�ԭʼ�ļ��豸
		EC_COPY_DST_EXISTING,		// �ļ�������Ŀ���Ѿ�����
		EC_MAP_UNFOUND,				// �ļ����Ҳ���ӳ��
		EC_FILE_BAD_ZIPDATA,		// zipѹ�������ݽ�ѹ��ʧ��
		EC_FILE_READONLY,			// ��ǰ�ļ�ֻ��
		EC_FILE_URLOPENERROR,		// url�򿪴���
		EC_INVALID_VERSION_INFO,	// ��Ч�İ汾��Ϣ
		EC_INVALID_FILENAME,		// ��Ч���ļ���
		EC_INVALID_DIRNAME,			// ��Ч��Ŀ¼��
		EC_DIR_NOT_FOUND,			// Ŀ¼������(�޷�ɾ��)
		EC_DIR_ALREADY_EXISTING,	// Ŀ¼�Ѵ���(�����ٴδ���)
		EC_DIR_NOT_EMPTY,			// (������ɾ��)�ǿ�Ŀ¼
		EC_ACCESS_DENIED,			// ���ʱ��ܾ�(Ȩ�޲���)
		EC_SHARING_VIOLATION,		// ����Υ��(����ɾ��һ���Ѵ򿪵��ļ�)
		EC_UNMOUNT_FAILED,			// UnMountʧ��
		EC_INVALID_PATCH,			// ��Ч�Ĳ�����
		EC_PATCH_NOT_MATCH,			// �������汾��ƥ�䱾�ؿͻ���
		EC_FILE_NOTOPEN,			// �ļ���δ��
		EC_FILE_WRITEONLY,			// �ļ�ֻ֧��д����
		EC_INSUFFICIENT_BUFFER,		// ���������������������������
		EC_FILESIZE_TOO_LARGE,		// �ļ���С�����ڴ�����������ֵ,�޷���ɴ˲���.
		EC_INVALID_META_FILE,		// ���Ϸ���MetaFile�ļ���ʽ
		EC_UNSUPPORT_META_FILE_VERSION,	// ��Ч��MetaFile�汾��MetaFile�汾̫��

		EC_FILTER_ERROR			= -1100,// һ�����������
		EC_FILTER_INVALID_ORI_DIR,		// ��Ч�Ĺ�����ԭĿ¼
		EC_FILTER_INVALID_NEW_DIR,		// ��Ч�Ĺ�������Ŀ¼
		EC_FILTER_ITEM_NOT_EXISTS,		// �����ڴ˹�����������Ŀ
		
		EC_OPTION_GENERAL_ERROR = -1200,// һ�����Դ���
		EC_OPTION_INVALID_GROUP,		// ��Ч��������
		EC_OPTION_INVALID_NAME,			// ��Ч��������
		EC_OPTION_INVALID_VALUE,		// ��Ч������ֵ
		EC_OPTION_OUT_OF_RANGE,			// ����ֵ����������ķ�Χ
		EC_OPTION_READ_ONLY,			// ����ֻ������֧���޸Ĵ����ԣ�
		EC_OPTION_SYSTEM_ERROR,			// �����޸�/��ȡʱ����ϵͳ����

		ECX_ENCODER_UNFOUND		= -2000,// �ļ�������û���ҵ�
		ECX_DECODER_UNFOUND,			// �ļ�������û���ҵ�
		ECX_BADFORMAT,					// ������ļ���ʽ
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
			L"�����ɹ���ɡ�",
		},

		{
			EC_DIR_HAS_ALREADY_MOUNT /*= -1000*/,
			L"The directory has already been mount to pfs system.",
			L"Ŀ¼�Ѿ�ӳ�䵽��һ���豸��",
		},
		
		{
			EC_INVALID_MOUNT_DEVICE,
			L"Invalid mount device.",
			L"��Ч�Ĺ����豸��",
		},
		
		{
			EC_INVALID_DEVICE_NAME,
			L"Invalid mount device name.",
			L"��Ч�Ĺ����豸����",
		},
		
		{
			EC_INVALID_MOUNT_DIR_NAME, 
			L"Invalid mount directory name.",
			L"��Ч�Ĺ���Ŀ��Ŀ¼����",
		},
		
		{
			EC_FSTYPE_UNEXISTING,
			L"Invalid PFS file system type.",
			L"�����ڵ�PFS�ļ�ϵͳ���͡�",
		},
		
		{
			EC_MOUNT_DIR_UNFOUND,
			L"Can not find specified pfs mount directory.",
			L"�޷��ҵ�ָ���ı�ӳ���PFS����Ŀ¼��",
		},
		
		{
			EC_MOUNT_FAILED,
			L"Mount pfs device failed.",
			L"����PFS�豸ʧ�ܡ�",
		},
		
		{
			EC_FS_BUSY_NOW,
			L"The PFS file system is busy now.",
			L"PFS�ļ�ϵͳ���ڱ�ʹ�á�",
		},
		
		{
			EC_FILE_NOT_MOUNT,
			L"Can not find the specified file in pfs system.",
			L"û���ҵ�PFS·����Ӧ��ӳ��Ŀ¼��",
		},
		
		{
			EC_FILE_NOT_FOUND,
			L"Can not find the file.",
			L"�ļ�δ�ҵ���",
		},
		
		{
			EC_FS_READONLY,
			L"Can not perform the writting operation on the readonly file system.",
			L"ֻ����PFS�ļ�ϵͳ��������ִ���޸Ĳ�����",
		},
		
		{
			EC_FILE_OPEN_MODE,
			L"Invalid file open mode.",
			L"��֧��ָ�����ļ��򿪷�ʽ��",
		},
		
		{
			EC_NATIVE_DEVICE_LOST,
			L"Can not find the device in the file system underlying the operation system(OS).",
			L"�ڲ���ϵͳ���ļ�ϵͳ���޷��ҵ�ԭʼ�ļ���",
		},
		
		{
			EC_COPY_DST_EXISTING,
			L"The destination file already exists when copy one to it.",
			L"�ļ�������Ŀ���Ѿ����ڡ�",
		},
		
		{
			EC_MAP_UNFOUND,
			L"Can not find the file system based on the file path.",
			L"�ļ����Ҳ���ӳ�䡣",
		},
		
		{
			EC_FILE_BAD_ZIPDATA,
			L"Corrupt zip data in the file.",
			L"zipѹ�������ݽ�ѹ��ʧ�ܡ�",
		},
		
		{
			EC_FILE_READONLY,
			L"Can not perform writing operation on the readonly file.",
			L"ֻ�����ļ�������д������",			
		},
		
		{
			EC_FILE_URLOPENERROR,
			L"Can not open the file by the URL.",
			L"�޷���ͳһ��Դ��λ��(URL)��ָ����ļ���",
		},
		
		{
			EC_INVALID_VERSION_INFO,
			L"Invalid version info.",
			L"��Ч�İ汾��Ϣ��",
		},
		
		{
			EC_INVALID_FILENAME,
			L"Invalid file name.",
			L"��Ч���ļ�����",
		},
		
		{
			EC_INVALID_DIRNAME,
			L"Invalid directory name.",
			L"��Ч��Ŀ¼����",
		},
		
		{
			EC_DIR_NOT_FOUND,
			L"Can not find the directory.",
			L"Ŀ¼�����ڣ��޷�ɾ����",
		},
		
		{
			EC_DIR_ALREADY_EXISTING,
			L"The directory has already been existing when trying to create a new one.",
			L"Ŀ¼�Ѵ��ڣ������ظ�������",
		},
		
		{
			EC_DIR_NOT_EMPTY,			// (������ɾ��)�ǿ�Ŀ¼
			L"Can not remove the directory when it contains sub-nodes(directories or files).",
			L"������ɾ���ǿ�Ŀ¼��",
		},
		
		{
			EC_ACCESS_DENIED,
			L"Access is denied.",
			L"���ʱ��ܾ�(Ȩ�޲���)��",
		},
		
		{
			EC_SHARING_VIOLATION,// ����Υ��(����ɾ��һ���Ѵ򿪵��ļ�)
			L"The process cannot access the file because it is being used by another process.",
			L"�ļ����������̶�ռ���������޷����ʡ�",
		},
		
		{
			EC_UNMOUNT_FAILED,
			L"Unmount PFS file system failed.",
			L"ж��PFS�ļ�ϵͳʧ�ܡ�",
		},
		
		{
			EC_INVALID_PATCH,
			L"Invalid patch.",
			L"��Ч�Ĳ�������",
		},
		
		{
			EC_PATCH_NOT_MATCH,
			L"The patch does not match the client based on the version.",
			L"�������汾�뱾�ؿͻ��˲�ƥ�䡣",
		},
		
		{
			EC_FILE_NOTOPEN,
			L"The file is not open.",
			L"�ļ���δ�򿪡�",
		},
		
		{
			EC_FILE_WRITEONLY,
			L"The file is write-only.",
			L"�ļ�ֻ֧��д������",
		},
		
		{
			EC_INSUFFICIENT_BUFFER,
			L"The data area passed to the call is too small.",
			L"��������������������������ݡ�",
		},
		
		{
			EC_FILESIZE_TOO_LARGE,
			L"The file size exceeds the upper limit of the memory size.",
			L"�ļ���С�����ڴ�����������ֵ,�޷���ɴ˲�����",
		},
		
		{
			EC_INVALID_META_FILE,
			L"Invalid meta file data.",
			L"���Ϸ���Ԫ�ļ���ʽ��",
		},
		
		{
			EC_UNSUPPORT_META_FILE_VERSION,
			L"Invalid meta file version or the file version is too high to support.",
			L"��Ч��Ԫ�ļ��汾��Ԫ�ļ��汾̫�ߡ�",
		},
		
		{		
			EC_FILTER_ERROR/*	= -1100*/,
			L"General filter operation error.",
			L"һ�����������",
		},
		
		{
			EC_FILTER_INVALID_ORI_DIR,
			L"Invalid filter original directory.",
			L"��Ч�Ĺ�����ԭĿ¼",
		},
		
		{
			EC_FILTER_INVALID_NEW_DIR,
			L"Invalid filter new directory.",
			L"��Ч�Ĺ�������Ŀ¼��",
		},
		
		{
			EC_FILTER_ITEM_NOT_EXISTS,
			L"Can not find the filter item.",
			L"�����ڴ˹�����������Ŀ��",
		},
		
		{
			EC_OPTION_GENERAL_ERROR/* = -1200*/,
			L"General option operation error.",
			L"һ��ѡ�����",
		},
		
		{
			EC_OPTION_INVALID_GROUP,
			L"Invalid option group.",
			L"��Ч��ѡ�������顣",
		},
		
		{
			EC_OPTION_INVALID_NAME,
			L"Invalid option name.",
			L"��Ч��ѡ����������",
		},
		
		{
			EC_OPTION_INVALID_VALUE,
			L"Invalid option value.",
			L"��Ч��ѡ������ֵ��",
		},
		
		{
			EC_OPTION_OUT_OF_RANGE,
			L"Option value is out of range.",
			L"ѡ�������ֵ����������ķ�Χ��",
		},
		
		{
			EC_OPTION_READ_ONLY,
			L"The option is readonly.",
			L"����ֻ������֧���޸Ĵ����ԣ���",
		},
		
		{
			EC_OPTION_SYSTEM_ERROR,
			L"System error occurred when access the option data.",
			L"�����޸�/��ȡʱ����ϵͳ����",
		},
		
		{
			ECX_ENCODER_UNFOUND		/*= -2000*/, 
			L"Invalid encoder.",
			L"û���ҵ��ļ���������",
		},
		
		{
			ECX_DECODER_UNFOUND,
			L"Invalid decoder.",
			L"û���ҵ��ļ���������",
		},
		
		{
			ECX_BADFORMAT,
			L"File data is corrupt or with unrecognized file format.",
			L"��Ч���ļ����ݻ�֧�ֵ��ļ���ʽ��",
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

		return 0; // ����PFS������Ĵ����롣
	}
	
	int GetErrorDescription( LANGID langId, int nErrorCode, 
		LPWSTR lpBuffer, DWORD nSize )
	{
		int code = _GetErrorDescription( langId, 
			nErrorCode, lpBuffer, nSize );

		return code;		
	}
	

}// namespace PFS