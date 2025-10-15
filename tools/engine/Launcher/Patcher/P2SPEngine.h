#include "i_p2sp_download.h"

class P2SPEngine
{
	static HMODULE s_hHandle;

private:
	P2SPEngine();
	P2SPEngine(const P2SPEngine& );

public:
	/// ���¡���������DLL�������ظ���Ϸ��������Ϣ
	static bool dll_p2spStart();

	/// ���á���ȡ���ز���
	static bool dll_p2spSetting(const SettingItemT* setting); 
	static bool dll_p2spGetSetting(SettingItemT* setting); 

	/// ��ӵ����ļ���������
	static bool dll_p2spAddDownload(const char* filemd5);

	/// ��ʼ����
	static bool dll_p2spStartDownload(const char* filemd5);

	/// ֹͣ��������
	static bool dll_p2spStopDownload(const char* filemd5);

	/// ɾ����������
	static bool dll_p2spDeleteDownload(const char* filemd5);

	/// ��ȡ��������
	static bool dll_p2spGetTaskInfomation(TaskInfomationT* info);

	/// ֹͣ�����������񣬲�ж��DLL
	static bool dll_p2spStop();
};