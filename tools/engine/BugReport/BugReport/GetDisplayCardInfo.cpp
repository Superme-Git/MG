/**********************************************************
���ڴ�ע����л�ȡ��ǰ��װ���Կ��豸�����Լ��汾
**********************************************************/

#include "BugReport.h"
#include <shlwapi.h>
#pragma comment(lib,"shlwapi.lib")
#pragma comment(lib,"version.lib")


#define DRIVER_VERSION_LEN 90   

//ȫ�ֱ���
//TCHAR g_szVideoSubKey[MAX_PATH] = _TEXT("HARDWARE\\DEVICEMAP\\VIDEO");
//TCHAR g_szVideoControlKey[MAX_PATH] = _TEXT("SYSTEM\\CurrentControlSet\\Control\\Video\\");
//TCHAR g_szVideo0[20] = _TEXT("\\Device\\Video0");
LPBYTE g_lpVideo0 = 0;
LPBYTE g_lpDescription = 0;




/*
GetDisplayCardInfo_GetDevice
���ڴ�ע���Hardware��Ϣ�л�ȡ��ʾ�豸��Ӧ��ȫע����ֵ
������OUT PLONG pulError; �����������
����ֵ��
*/
//DWORD WINAPI GetDisplayCardInfo_GetDevice0(PLONG pulError)
//{
//	DWORD dwType = 0;
//	BYTE bTest = 0;
//	DWORD cbSize = 1;
//	HKEY hVideoKey = 0;
//	LONG lResult = 0;
//	lResult = RegOpenKeyEx(HKEY_LOCAL_MACHINE,g_szVideoSubKey,0,KEY_READ,&hVideoKey);
//	if(lResult != ERROR_SUCCESS)
//	{
//		RegCloseKey(hVideoKey);
//		*pulError = lResult;
//		Log(_TEXT("Open Video Subkey Error: %d\r\n"),lResult);
//		return 1; //��video subkey����
//	}
//	lResult = RegQueryValueEx(hVideoKey,g_szVideo0,0,&dwType,&bTest,&cbSize);
//	if(lResult != ERROR_MORE_DATA && lResult != ERROR_SUCCESS)
//	{
//		RegCloseKey(hVideoKey);
//		*pulError = lResult;
//		Log(_TEXT("Read VideoDevice0 to get size Error: %d\r\n"),lResult);
//		return 2; //��һ�ζ�ȡ����
//	}
//	else if(cbSize > 1)
//	{
//		g_lpVideo0 = (LPBYTE)HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,cbSize);
//		lResult = RegQueryValueEx(hVideoKey,g_szVideo0,0,&dwType,g_lpVideo0,&cbSize);
//		if(lResult != ERROR_SUCCESS)
//		{
//			RegCloseKey(hVideoKey);
//			*pulError = lResult;
//			Log(_TEXT("Read VideoDevice0 2nd to get value Error: %d\r\n"),lResult);
//			return 3; //�����ڴ�󣬶�ȡ����
//		}
//	}
//	RegCloseKey(hVideoKey);
//	*pulError = 0;
//	return 0; //��ȡ�ɹ�
//}


/*
GetDisplayCardInfo_GetDriverVersion
��ȡ��Ӧ�Կ������ļ��Ĳ�Ʒ�źͰ汾��
������IN lpszFileName�������ļ����ƣ�,OUT lplpMem�����ڴ���ָ�򻺳�����ָ�룬��Ҫheapfree�ͷţ�,���ȹ̶�Ϊ45�ֽ�
����ֵ��
0:�ɹ���1:�ļ��ް汾��Ϣ��2:�����ڴ�ʧ�ܣ�3:��ȡ�汾��Ϣʧ��
*/
DWORD WINAPI GetDisplayCardInfo_GetDriverVersion(LPTSTR lpszFileName,LPVOID *lplpMem)
{
	BOOL bFlag = 0;
	DWORD dwInfoSize = 0;
	UINT uiFixedSize = 0;
	LPVOID lpVerBuff = 0;
	LPVOID lpFixedInfo = 0;
	LPVOID lpOutBuff = 0;
	DWORD dwProductVersionMs = 0;
	DWORD dwProductVersionLs = 0;
	DWORD dwFileVersionMs = 0;
	DWORD dwFileVersionLs = 0;
	dwInfoSize = GetFileVersionInfoSize(lpszFileName,0);
	if(dwInfoSize == 0)
	{
		Log(_TEXT("GetFileVersionInfoSize fileName %s Error: %d\r\n"),lpszFileName,GetLastError());
		return 1;//û�а汾��Ϣ
	}
	lpVerBuff = HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,dwInfoSize);
	if(lpVerBuff == 0)
	{
		Log(_TEXT("HeapAlloc VerBuff Error: %d\r\n"),GetLastError());
		return 2;//�����ڴ�ʧ��
	}
	if(!GetFileVersionInfo(lpszFileName,0,dwInfoSize,lpVerBuff))
	{
		Log(_TEXT("GetFileVersionInfo Error: %d\r\n"),GetLastError());
		return 3; //��ȡ��Ϣʧ��
	}
	VerQueryValue(lpVerBuff,_TEXT("\\"),&lpFixedInfo,&uiFixedSize);
	dwProductVersionLs = ((VS_FIXEDFILEINFO *)lpFixedInfo)->dwProductVersionLS;
	dwProductVersionMs = ((VS_FIXEDFILEINFO *)lpFixedInfo)->dwProductVersionMS;
	dwFileVersionLs = ((VS_FIXEDFILEINFO *)lpFixedInfo)->dwFileVersionLS;
	dwFileVersionMs = ((VS_FIXEDFILEINFO *)lpFixedInfo)->dwFileVersionMS;
	//�����Ϣ
	lpOutBuff = HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,DRIVER_VERSION_LEN);
	if(lpOutBuff == 0)
	{
		Log(_TEXT("HeapAlloc OutBuff Error: %d\r\n"),GetLastError());
		HeapFree(GetProcessHeap(),0,lpVerBuff);
		return 2;
	}
	swprintf_s((wchar_t *)lpOutBuff,DRIVER_VERSION_LEN/2,L"%d.%d.%d.%d",HIWORD(dwProductVersionMs), LOWORD(dwProductVersionMs), HIWORD(dwProductVersionLs), LOWORD(dwProductVersionLs));

	//�ͷ�fileversion�ѿռ�
	HeapFree(GetProcessHeap(),0,lpVerBuff);
	*lplpMem = lpOutBuff;
	return 0;//�ɹ���ȡ
}

/*
GetDisplayCardInfo_GetVideoInfo ���ϴ��ӿڵ��û�ȡ�Կ���Ϣ
����1��pdwGviError ����������
����2��pPartArray ��Ҫ���г�ʼ�������ݽṹ��ַ
����ֵ��0 �ɹ���1 GetDevice0����2 Device0��û��{��3 ��xxxx\0000ʧ�ܣ�4 ��ѯDescriptionʧ�ܣ�
5 �����ڴ�ʧ�ܣ� 6��7��ȡע������
*/
DWORD WINAPI GetDisplayCardInfo_GetVideoInfo(LPDWORD pdwGviError,PPartInfo pPartArray)
{
	HKEY hVideoKey = 0;
	
	BOOL bVersion = 0;
	DWORD dwGdReturn = 0;
	LONG lGdError = 0;
	DWORD dwGdvReturn = 0;
	LPTSTR lpszRandomKey = 0;
	LONG lResult = 0;

	BYTE bTest = 0;
	DWORD cbSize = 1;
	TCHAR szVideoControlKey[MAX_PATH] = _TEXT("SYSTEM\\CurrentControlSet\\Control\\Video\\");
	TCHAR szDriverName[MAX_PATH] = {0};
	DWORD dwBuffSize = sizeof(szDriverName);
	
	LPVOID lpVersionInfo = 0;
	DWORD dwVideoCard = 0;
	DWORD dwDriverName = 0;
	DWORD dwTotalSize = 0;
	LPBYTE lpOutBuff = 0;

//***************�¼Ӵ��룬�޸Ļ�ȡ��ʾ�豸�ķ�ʽ
	DISPLAY_DEVICE stDevice = {0};
	DWORD dwDeviceIndex = 0;
	BOOL bMainDeviceFound = 0;
	while(1)
	{
		stDevice.cb = sizeof(DISPLAY_DEVICE);
		if(0 == EnumDisplayDevices(0,dwDeviceIndex,&stDevice,0))
			break;   //������index������
		if( (stDevice.StateFlags & DISPLAY_DEVICE_ATTACHED_TO_DESKTOP) != 0)  //�ҵ��˵�ǰ������ʾ�����豸
		{
			bMainDeviceFound = TRUE;
			dwDeviceIndex++;
			break;
		}
		ZeroMemory(&stDevice,sizeof(DISPLAY_DEVICE));
		dwDeviceIndex++;
	}
	if(!bMainDeviceFound) //û�ҵ����豸
	{
		Log(_TEXT("Can't get main display device\r\n"));
		return 1;
	}

	cbSize = (lstrlen(stDevice.DeviceString)+1)*2;
	g_lpDescription = (LPBYTE)HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,cbSize);
	if(g_lpDescription == 0)
	{
		Log(_TEXT("HeapAlloc Description Error: %d"),GetLastError());
		RegCloseKey(hVideoKey);
		return 5; //�����ڴ�ʧ��
	}
	lstrcpy((LPWSTR)g_lpDescription,stDevice.DeviceString);

	// ��д�Կ���Ϣ��ǰ����Ϊ�����ע���Ĳ�������ʧ��
	dwVideoCard =  (DWORD)(wcslen((LPWSTR)g_lpDescription)*2);
	pPartArray[Graphic].dwInfoLen = dwVideoCard;
	pPartArray[Graphic].lpContent = (LPWSTR)g_lpDescription;
	Log(_TEXT("Graphic set into Array\r\n"));


	//��ȡ�����豸�����Ϣ�����ɶ�Ӧ��ע���·��

	//ע����ֵ���ٴ�Hardware������
	//dwGdReturn = GetDisplayCardInfo_GetDevice0(&lGdError);
	//if(dwGdReturn != 0)
	//{
	//	Log(_TEXT("GetDevice0 Error\r\n"));
	//	*pdwGviError = dwGdReturn;
	//	return 1; //ִ��getdevice0ʧ��
	//}
	
	lpszRandomKey = StrRChr((LPCTSTR)(stDevice.DeviceKey),0,_TEXT('{'));
	if(lpszRandomKey == 0)
	{
		Log(_TEXT("StrRChr for { Error,String is %ws \r\n"),stDevice.DeviceKey);
		return 2; //����{ʧ��
	}
	lstrcat(szVideoControlKey,lpszRandomKey);
	lResult = RegOpenKeyEx(HKEY_LOCAL_MACHINE,szVideoControlKey,0,KEY_READ,&hVideoKey);
	if(lResult != ERROR_SUCCESS)
	{
		Log(_TEXT("Open szVideoControlKey %s Error: %d\r\n"),szVideoControlKey,lResult);
		*pdwGviError = lResult;
		return 3; //��\\xxxxx\\0000ʧ��
	}
	//lResult = RegQueryValueExW(hVideoKey,L"Device Description",0,0,&bTest,&cbSize);
	//if(lResult != ERROR_MORE_DATA && lResult != ERROR_SUCCESS)
	//{
	//	Log(_TEXT("Query Description for size Error: %d\r\n"),lResult);
	//	RegCloseKey(hVideoKey);
	//	*pdwGviError = lResult;
	//	return 4; //QueryValueʧ��
	//}
//_GetDeviceDescription:
	
	//********************************************************
	//�����汾֮����Ҫ��ע������DeviceDescription�����ֵ
	//**********************************************************

	//lResult = RegQueryValueExW(hVideoKey,L"Device Description",0,0,g_lpDescription,&cbSize); //���������unicode��Ҫת
	//if(lResult != ERROR_SUCCESS)
	//{
	//	Log(_TEXT("QueryValue Description Error: %d"),lResult);
	//	RegCloseKey(hVideoKey);
	//	*pdwGviError = lResult;
	//	return 6; //�����ڴ���ȡʧ��
	//}
	
	lResult = RegQueryValueEx(hVideoKey,_TEXT("InstalledDisplayDrivers"),0,0,(LPBYTE)szDriverName,&dwBuffSize);
	if(lResult != ERROR_SUCCESS)
	{
		Log(_TEXT("QueryValue InstalledDisplayDrivers Error: %d"),lResult);
		RegCloseKey(hVideoKey);
		*pdwGviError = lResult;
		return 7; //��ȡInstalledʧ��
	}

	lstrcat(szDriverName,_TEXT(".dll"));

	dwGdvReturn = GetDisplayCardInfo_GetDriverVersion(szDriverName,&lpVersionInfo);

	pPartArray[Driver].lpContent = (LPWSTR)lpVersionInfo;
	pPartArray[Driver].dwInfoLen = (DWORD)(wcslen((LPWSTR)lpVersionInfo)*2);
	Log(_TEXT("Driver set into Array\r\n"));

	//��ȡ�汾��Ϣʧ�ܣ��������Կ��豸��Ϣ
	//��ȡ�ɹ�����ʼ���������Կ���Ϣ������

	RegCloseKey(hVideoKey);
	return 0;
}
