

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

/// ������Ŀ��־
enum P2P_SETTING_ITEM {
	P2P_SETTING_ITEM_MAX_DOWNLOAD_NUM	= 0x1 << 0, //�������������������Ĭ����5
	P2P_SETTING_ITEM_MAX_DOWNLOAD_SPEED = 0x1 << 1,	//������������ٶȣ�����������Ĭ��0�������ƣ�
	P2P_SETTING_ITEM_MAX_UPLOAD_SPEED	= 0x1 << 2, //��������ϴ��ٶȣ�����������Ĭ��0�������ƣ�
	P2P_SETTING_ITEM_FILE_SAVE_PATH		= 0x1 << 3, //�������ر���·����Ĭ��Ϊ[ϵͳ�̷�\PWRD]

	//����ٶ�С��P2P_SETTING_ITEM_TASK_TIMEOUT_SPEED������P2P_SETTING_ITEM_TASK_TIMEOUTʱ�䣬��������ʧ��
	P2P_SETTING_ITEM_TASK_TIMEOUT		= 0x1 << 4, 
	P2P_SETTING_ITEM_TASK_TIMEOUT_SPEED = 0x1 << 5, 
};
/// �������־
#define P2P_SETTING_ITEM_ALL P2P_SETTING_ITEM_MAX_DOWNLOAD_NUM | P2P_SETTING_ITEM_MAX_DOWNLOAD_SPEED \
	| P2P_SETTING_ITEM_MAX_UPLOAD_SPEED | P2P_SETTING_ITEM_FILE_SAVE_PATH | P2P_SETTING_ITEM_TASK_TIMEOUT \
	| P2P_SETTING_ITEM_TASK_TIMEOUT_SPEED 

/// ���ò����ṹ
struct SettingItemT {
	UINT	mask;					//P2P_SETTING_ITEM ���������
	UINT	maxDownloadNum;			//�������������
	UINT	maxDownloadSpeed;		//��������ٶ�(�ֽ�)
	UINT	maxUploadSpeed;			//����ϴ��ٶ�(�ֽ�)
	UINT	taskTimeout;			//��ʱʱ��(��)	   Ĭ��Ϊ300
	UINT	taskTimeoutSpeed;		//��ʱʱ�ٶ�(�ֽ�) Ĭ��Ϊ10240
	WCHAR	savePath[MAX_PATH + 1];	//����·��
};

/// ����״̬
enum P2P_TASK_STATE {
    P2P_TASK_STATE_NOREADY,     //δ׼����
	P2P_TASK_STATE_READY,		//׼��
	P2P_TASK_STATE_DOWNLOADING,	//����
	P2P_TASK_QUEUING,			//�Ŷ�
	P2P_TASK_STATE_STOP,		//ֹͣ
	P2P_TASK_STATE_COMPLETE,	//���
	P2P_TASK_STATE_ERROR,		//����
};

/// ������Ϣ
struct TaskInfomationT {
	CHAR				md5[33];		//MD5��
	P2P_TASK_STATE		state;			//����״̬����P2P_TASK_STATE����	
	USHORT				err;			//�����룬��TaskErrorCode����
	UINT				dspeed;			//�����ٶ� (�ֽ�)
	UINT				uspeed;			//�ϴ��ٶ� (�ֽ�)
	UINT64				size;			//�ļ���С (�ֽ�)
	UINT64				complateSize;	//��ɴ�С (�ֽ�)
	WCHAR	savePath[MAX_PATH + 1];		//����·��
};

/// �����뺬��
enum TaskErrorCode {
	P2PE_OK,

	P2PE_TASK_ALREADY_EXISTS,	//�����Ѿ�����
	P2PE_DOWNLOAD_TASK_SEED,	//������������ʧ��
	P2PE_PARSE_TASK_SEED,		//�������ӽ���ʧ��
	P2PE_CHECK_TASK_SEED,		//�������Ӳ���У��ʧ��

	P2PE_STARTTASK_INEXISTENT,	//���񲻴���
	P2PE_STARTTASK_DISKFULL,	//ʣ����̿ռ䲻��
	P2PE_STARTTASK_QUEUING,		//���������Ŷ�

	P2PE_HTTPDOWNLOAD_PARSEURL,			//����URLʧ��
	P2PE_HTTPDOWNLOAD_RESOLVEHOST,		//����HOSTʧ��
	P2PE_HTTPDOWNLOAD_CONNECTSERVER,	//���ӷ�����ʧ��
	P2PE_HTTPDOWNLOAD_REQUEST,			//��������ʧ��
	P2PE_HTTPDOWNLOAD_READSTATUSLINE,	//��ȡHTTP����ͷʧ��
	P2PE_HTTPDOWNLOAD_UNSUPPORTEPARTIAL,	//��֧�ֶϵ�
	P2PE_HTTPDOWNLOAD_READHEADERS,			//��ȡHTTP����ͷʧ��
	P2PE_HTTPDOWNLOAD_READDATA,				//��ȡ����ʧ��

	P2PE_HTTPDOWNLOAD_TRY_TIMES_OVERRUN,	//�������̫��
	P2PE_STARTTASK_CREATE_FILE_ERROR,		//��������ʧ��

	P2PE_TIMEOUT = 1000,					//�ٶȲ��������õ���Сֵ���ҳ���ʱ�䳬������ֵ��P2P_SETTING_ITEM_TASK_TIMEOUT��
};

EXTERN_C_BEGIN
	/// ���¡���������DLL�������ظ���Ϸ��������Ϣ
	P2SP_ENGINE_API bool p2spStart(int gameID);

	/// ���á���ȡ���ز���
	P2SP_ENGINE_API bool p2spSetting(const SettingItemT* setting); 
	P2SP_ENGINE_API bool p2spGetSetting(SettingItemT* setting); 
	
	/// ��ӵ����ļ���������
	P2SP_ENGINE_API bool p2spAddDownload(const char* filemd5);

	/// ��ʼ����
	P2SP_ENGINE_API bool p2spStartDownload(const char* filemd5);

	/// ֹͣ��������
	P2SP_ENGINE_API bool p2spStopDownload(const char* filemd5);

	/// ɾ����������
	P2SP_ENGINE_API bool p2spDeleteDownload(const char* filemd5);

	/// ��ȡ��������
	P2SP_ENGINE_API bool p2spGetTaskInfomation(TaskInfomationT* info);

	/// ֹͣ�����������񣬲�ж��DLL
	P2SP_ENGINE_API bool p2spStop();
EXTERN_C_END

#endif //__I_P2SP_DOWNLOAD_H__