#include "StdAfx.h"
#include "PPErrorInfo.h"

namespace PP
{
	static struct dummy
	{
		static LPCWSTR GetString( LPCWSTR /*key*/, LPCWSTR value ) 
		{
			return value;
		}
	} _ini;

	// 1		��ǰʹ�õĲ���ϵͳ�汾��֧��
	// 3		��ȴ���Ϸ��ȫ�����Ժ��ٳ���
	// 4��11	��ȷ����Ϸû�б�ɱ����������ǽ��ֹ����ȷ�������ڹ���Ա�ʺ���������Ϸ
	// 7		��ȴ���Ϸ��ȫ�����Ժ��ٳ���
	// 126		���ڲ���ϵͳ��δ��ɵĲ��������Զ����£��������Щ����������������ٳ��Դ���Ϸ
	// 10		�����̼��ʧ��,���Launcher����
	// 23		��ʱ���������ر�������Ϸ�ͻ��ˣ����³���
	// 24		�࿪
	// 25		pp_data0�ļ��Ƿ�
	// 26		pp_data0�ļ�������
	// 1450		ϵͳ�ڴ治�㣬��ע�������������Ժ��ٳ���!
	// ����ֵ	��Ϸ��ʼ��ʧ�ܣ���ʱ�ɵ���һ���Ի�������Ҹ����Ƿ���pp_005.dat��
	static LPCWSTR GetMessage( int ErrorCode )
	{
		static LPCWSTR ErrorMessage[] = 
		{
			/*0 */L"��֧�������ڵ�ǰ����ϵͳ�¡�",
			/*1 */L"�ոմ򿪵���Ϸ�ͻ��˻�û������ϣ���ȴ���ȫ���������ԡ�",
			/*2 */L"��ȷ����Ϸû�б�ɱ����������ǽ��ֹ����ȷ�������Լ��������Ա�ʻ����������Ϸ��",
			/*3 */L"���ڹرյ���Ϸ�ͻ��˻�û��ȫ�˳�����ȴ���ȫ�˳������ԡ�",
			/*4 */L"�������ϵͳ�Ƿ���δ��ɵĲ��������Զ����£�ȷ�������Щ���������������֮���ٳ��Դ���Ϸ��",
			/*5 */L"��������ݷ�ʽ\"ħ������\"��{��ϷĿ¼}/launcher/launcher.exe����!",
			/*6 */L"������Ӧʱ�����, ��ر�������Ϸ�ͻ��˺����³���������",
			/*7 */L"ͬʱ�򿪵���Ϸ�ͻ����Ѵ����ޡ�",
			/*8 */L"�����ļ����������볢�ԶԿͻ��˳�����а汾��֤��",
			/*9 */L"ȱ�ٱ�Ҫ�ĳ����ļ����볢�ԶԿͻ��˳�����а汾��֤��",
			/*10 */L"ϵͳ��Դ���㣬��ر�����ռ����Դ�϶�ĳ����ע��/���������Ժ��ٳ��ԡ�",
			/*11 */L"ϵͳԭ������Ϸ��ʼ��ʧ�ܡ�",
		};
		switch( ErrorCode )
		{
		case 1: return _ini.GetString(L"msg0", ErrorMessage[0]);
		case 3: return _ini.GetString(L"msg1", ErrorMessage[1]);
		case 4: case 11: return _ini.GetString(L"msg2", ErrorMessage[2]);
		case 7: return _ini.GetString(L"msg3", ErrorMessage[3]);
		case 126: return _ini.GetString(L"msg4", ErrorMessage[4]);
		case 10: return _ini.GetString(L"msg5", ErrorMessage[5]);
		case 23: return _ini.GetString(L"msg6", ErrorMessage[6]);
		case 24: return _ini.GetString(L"msg7", ErrorMessage[7]);
		case 25:return _ini.GetString(L"msg8", ErrorMessage[8]);
		case 26:return _ini.GetString(L"msg9", ErrorMessage[9]);
		case 1450: return _ini.GetString(L"msg10", ErrorMessage[10]);
		default: return _ini.GetString(L"msg11", ErrorMessage[11]);
		}
	}

	int GetErrorDescription( LANGID /*langId*/, int ErrorCode, LPWSTR lpBuffer, DWORD nSize )
	{
		// TODO: 
		// langId ��δʵ�֡�
		LPCWSTR pszMessage = GetMessage( ErrorCode );
		int nLen = (int)wcslen( pszMessage );
		if( nLen < (int)nSize )
		{
			wcscpy_s( lpBuffer, nSize, pszMessage );
			return nLen;
		}
		else
		{
			return -nLen;
		}
	}

} // namespace PP