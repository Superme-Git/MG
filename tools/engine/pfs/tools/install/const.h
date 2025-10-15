#pragma once

enum
{
	READCOPYRIGHT = 0,				//	��װǰ����ϸ�Ķ������û�������Э��:
	COPYRIGHT,						//	�����û�������Э��
	AGREE,							//	ͬ��
	DISAGREE,						//	��ͬ��
	BROWSE,							//	���
	PREVSTEP,						//	��һ��
	NEXTSTEP,						//	��һ��
	END,							//	����
	ASK,							//	ѯ��
	JUMP,							//	����
	CHECK_QUESTION,					//  ��װ������������صĹ����п��ܻᵼ�²������ݶ�ʧ������Ҫ�԰�װ�������ݰ���������У����	
	CHECK,							//	��֤
	CHECKPROGRESS,					//	����֤��װ�������ݰ���������,���Ժ�...
	CANCEL,							//	ȡ��
	PROGRESS,						//	��װ����
	FINISH,							//	��װ���
	OK = 16,						//	ȷ��

	APPNAME = 20,					//	2DGame��װ����
	NEEDSPACE,						//	��ѡ��װ·��(��Ҫ�ռ�:%dMB)
	DISKFREESPACE,					//	����ʣ��ռ�(%dMB)
	SPACENOTENOUGH,					//	����ʣ��ռ䲻������ѡ�����㹻ʣ��ռ����!
	TOTALPROGRESS,					//	�ܽ��ȣ�      	%d%%
	PROGRESSINIT,					//	���ڷ����ļ������Ժ�...
	CURRENTFILE,					//	��ǰ�����ļ���  %s
	USERSTOP,						//	��װ��δ���, �����Ҫ������װ��?
	USERSTOPCHECK,					//	��ȷ��Ҫ�����ļ��������Լ����?
	INSTALLSUCCESS = 29,			//	�Ѿ�ȫ����װ��ϣ�
	
	ERRORREASON = 30,				//	��װ����! ԭ����: %s
	ERROR_UNKNOWN,					//	δ֪����.
	ERROR_USERSTOP,					//	�û���ֹ.
	ERROR_CHECK,					//	��װ�������ݰ�������

	ERROR_EC_DIR_HAS_ALREADY_MOUNT = 40,	//	%s Ŀ¼�Ѿ�ӳ��
	ERROR_EC_INVALID_MOUNT_DEVICE,	//	%s ��Ч��Mount�豸
	ERROR_EC_INVALID_DEVICE_NAME,	//	%s ��Ч��Mount�豸��
	ERROR_EC_INVALID_MOUNT_DIR_NAME,//	%s ��Ч��MountĿ��Ŀ¼��
	ERROR_EC_FSTYPE_UNEXISTING,		//	%s �ļ�ϵͳ���Ͳ�����
	ERROR_EC_MOUNT_FAILED,			//	%s Mount �ļ�ϵͳʧ��
	ERROR_EC_MOUNT_DIR_UNFOUNT,		//	%s ӳ��Ŀ¼�޷��ҵ�
	ERROR_EC_FS_BUSY_BOW,			//	%s �ļ�ϵͳ���ڱ�ʹ��
	ERROR_EC_FILE_NOT_MOUNT,		//	%s ӳ���ļ��޷��ҵ�
	ERROR_EC_FILE_NOT_FOUND,		//	%s �ļ��޷��ҵ�
	ERROR_EC_FS_READONLY,			//	%s �ļ�ϵͳĿǰֻ��
	ERROR_EC_FILE_OPEN_MODE,		//	%s �ļ��򿪷�ʽ��֧��
	ERROR_EC_NATIVE_DEVICE_LOST,	//	%s ԭʼ�ļ��豸�޷��ҵ�
	ERROR_EC_COPY_DST_EXISTING,		//	%s �ļ�������Ŀ���Ѿ�����
	ERROR_EC_MAP_UNFOUND,			//	%s �ļ����Ҳ���ӳ��
	ERROR_EC_FILE_BAD_ZIPDATA,		//	%s zipѹ�������ݽ�ѹ��ʧ��
	ERROR_EC_FILE_READONLY,			//	%s �ļ�ֻ��
	ERROR_EC_FILE_URLOPENERROR,		//	%s url�򿪴���
	ERROR_EC_INVALID_VERSION_INFO,	//	%s ��Ч�İ汾��Ϣ
	ERROR_EC_INVALID_FILENAME,		//	%s ��Ч���ļ���
	ERROR_EC_INVALID_DIRNAME,		//	%s ��Ч��Ŀ¼��
	ERROR_EC_DIR_NOT_FOUND,			//	%s Ŀ¼������(�޷�ɾ��)
	ERROR_EC_DIR_ALREADY_EXISTING,	//	%s Ŀ¼�Ѵ���(�����ٴδ���)
	ERROR_EC_DIR_NOT_EMPTY,			//	%s (������ɾ��)�ǿ�Ŀ¼
	ERROR_EC_ACCESS_DENIED,			//	%s ���ʱ��ܾ�(Ȩ�޲���)
	ERROR_EC_SHARING_VIOLATION,		//	%s ����Υ��(����ɾ��һ���Ѵ򿪵��ļ�)
	ERROR_EC_UNMOUNT_FAILED,		//	%s UnMountʧ��
	ERROR_EC_INVALID_PATCH,			//	%s ��Ч�Ĳ�����
	ERROR_EC_PATCH_NOT_MATCH,		//	%s �������汾��ƥ�䱾�ؿͻ���

	ERROR_ECX_ENCODER_UNFOUND = 70,		//	%s �ļ�������û���ҵ�
	ERROR_ECX_DECODER_UNFOUND,		//	%s �ļ�������û���ҵ�
	ERROR_ECX_BADFORMAT,			//	%s �ļ���ʽ����

	ERROR_SYSTEMERROR,				//	%s �ļ���������%s

	CANCELVERIFY_INPROGRESS = 80,	// ����ȡ����֤���̣����Ժ�...
	CANCELINSTALL_INPROGRESS,		// �����˳���װ�������Ժ�...
	CHECKBOX_CREATELINK,			// �����洴����Ϸͼ��
	CHECKBOX_STARTGAME,				// ������Ϸ
	AUTO_INSTALL_360,
	AUTO_INSTALL_UNITY3D,
	UNITY3D_VERSION,
	

	ERROR_NOT_SUSPEND_OS64 = 90,	// %s �ݲ�֧��64λ����ϵͳ
	ERROR_MD5FILE_UNFOUND = 91,		// ȱ�ٰ�װ���ݰ�(%s)��md5�ļ�(%s.md5)
};