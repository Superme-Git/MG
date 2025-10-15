/**********************************************************
用于从注册表中获取当前安装的显卡设备驱动以及版本
**********************************************************/

#include "BugReport.h"
#include <shlwapi.h>
#pragma comment(lib,"shlwapi.lib")
#pragma comment(lib,"version.lib")


#define DRIVER_VERSION_LEN 90   

//全局变量
//TCHAR g_szVideoSubKey[MAX_PATH] = _TEXT("HARDWARE\\DEVICEMAP\\VIDEO");
//TCHAR g_szVideoControlKey[MAX_PATH] = _TEXT("SYSTEM\\CurrentControlSet\\Control\\Video\\");
//TCHAR g_szVideo0[20] = _TEXT("\\Device\\Video0");
LPBYTE g_lpVideo0 = 0;
LPBYTE g_lpDescription = 0;




/*
GetDisplayCardInfo_GetDevice
用于从注册表Hardware信息中获取显示设备对应的全注册表键值
参数：OUT PLONG pulError; 输出具体错误号
返回值：
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
//		return 1; //打开video subkey出错
//	}
//	lResult = RegQueryValueEx(hVideoKey,g_szVideo0,0,&dwType,&bTest,&cbSize);
//	if(lResult != ERROR_MORE_DATA && lResult != ERROR_SUCCESS)
//	{
//		RegCloseKey(hVideoKey);
//		*pulError = lResult;
//		Log(_TEXT("Read VideoDevice0 to get size Error: %d\r\n"),lResult);
//		return 2; //第一次读取出错
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
//			return 3; //分配内存后，读取错误
//		}
//	}
//	RegCloseKey(hVideoKey);
//	*pulError = 0;
//	return 0; //读取成功
//}


/*
GetDisplayCardInfo_GetDriverVersion
获取对应显卡驱动文件的产品号和版本号
参数：IN lpszFileName（驱动文件名称）,OUT lplpMem（用于传出指向缓冲区的指针，需要heapfree释放）,长度固定为45字节
返回值：
0:成功，1:文件无版本信息，2:分配内存失败，3:获取版本信息失败
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
		return 1;//没有版本信息
	}
	lpVerBuff = HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,dwInfoSize);
	if(lpVerBuff == 0)
	{
		Log(_TEXT("HeapAlloc VerBuff Error: %d\r\n"),GetLastError());
		return 2;//分配内存失败
	}
	if(!GetFileVersionInfo(lpszFileName,0,dwInfoSize,lpVerBuff))
	{
		Log(_TEXT("GetFileVersionInfo Error: %d\r\n"),GetLastError());
		return 3; //获取信息失败
	}
	VerQueryValue(lpVerBuff,_TEXT("\\"),&lpFixedInfo,&uiFixedSize);
	dwProductVersionLs = ((VS_FIXEDFILEINFO *)lpFixedInfo)->dwProductVersionLS;
	dwProductVersionMs = ((VS_FIXEDFILEINFO *)lpFixedInfo)->dwProductVersionMS;
	dwFileVersionLs = ((VS_FIXEDFILEINFO *)lpFixedInfo)->dwFileVersionLS;
	dwFileVersionMs = ((VS_FIXEDFILEINFO *)lpFixedInfo)->dwFileVersionMS;
	//输出信息
	lpOutBuff = HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,DRIVER_VERSION_LEN);
	if(lpOutBuff == 0)
	{
		Log(_TEXT("HeapAlloc OutBuff Error: %d\r\n"),GetLastError());
		HeapFree(GetProcessHeap(),0,lpVerBuff);
		return 2;
	}
	swprintf_s((wchar_t *)lpOutBuff,DRIVER_VERSION_LEN/2,L"%d.%d.%d.%d",HIWORD(dwProductVersionMs), LOWORD(dwProductVersionMs), HIWORD(dwProductVersionLs), LOWORD(dwProductVersionLs));

	//释放fileversion堆空间
	HeapFree(GetProcessHeap(),0,lpVerBuff);
	*lplpMem = lpOutBuff;
	return 0;//成功获取
}

/*
GetDisplayCardInfo_GetVideoInfo 供上传接口调用获取显卡信息
参数1：pdwGviError 输出错误代码
参数2：pPartArray 需要进行初始化的数据结构地址
返回值：0 成功；1 GetDevice0出错；2 Device0中没有{；3 打开xxxx\0000失败；4 查询Description失败；
5 分配内存失败； 6，7读取注册表出错
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

//***************新加代码，修改获取显示设备的方式
	DISPLAY_DEVICE stDevice = {0};
	DWORD dwDeviceIndex = 0;
	BOOL bMainDeviceFound = 0;
	while(1)
	{
		stDevice.cb = sizeof(DISPLAY_DEVICE);
		if(0 == EnumDisplayDevices(0,dwDeviceIndex,&stDevice,0))
			break;   //遍历的index不存在
		if( (stDevice.StateFlags & DISPLAY_DEVICE_ATTACHED_TO_DESKTOP) != 0)  //找到了当前用于显示的主设备
		{
			bMainDeviceFound = TRUE;
			dwDeviceIndex++;
			break;
		}
		ZeroMemory(&stDevice,sizeof(DISPLAY_DEVICE));
		dwDeviceIndex++;
	}
	if(!bMainDeviceFound) //没找到主设备
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
		return 5; //分配内存失败
	}
	lstrcpy((LPWSTR)g_lpDescription,stDevice.DeviceString);

	// 将写显卡信息提前，因为后面读注册表的操作可能失败
	dwVideoCard =  (DWORD)(wcslen((LPWSTR)g_lpDescription)*2);
	pPartArray[Graphic].dwInfoLen = dwVideoCard;
	pPartArray[Graphic].lpContent = (LPWSTR)g_lpDescription;
	Log(_TEXT("Graphic set into Array\r\n"));


	//获取到主设备相关信息，生成对应的注册表路径

	//注册表键值不再从Hardware中生成
	//dwGdReturn = GetDisplayCardInfo_GetDevice0(&lGdError);
	//if(dwGdReturn != 0)
	//{
	//	Log(_TEXT("GetDevice0 Error\r\n"));
	//	*pdwGviError = dwGdReturn;
	//	return 1; //执行getdevice0失败
	//}
	
	lpszRandomKey = StrRChr((LPCTSTR)(stDevice.DeviceKey),0,_TEXT('{'));
	if(lpszRandomKey == 0)
	{
		Log(_TEXT("StrRChr for { Error,String is %ws \r\n"),stDevice.DeviceKey);
		return 2; //查找{失败
	}
	lstrcat(szVideoControlKey,lpszRandomKey);
	lResult = RegOpenKeyEx(HKEY_LOCAL_MACHINE,szVideoControlKey,0,KEY_READ,&hVideoKey);
	if(lResult != ERROR_SUCCESS)
	{
		Log(_TEXT("Open szVideoControlKey %s Error: %d\r\n"),szVideoControlKey,lResult);
		*pdwGviError = lResult;
		return 3; //打开\\xxxxx\\0000失败
	}
	//lResult = RegQueryValueExW(hVideoKey,L"Device Description",0,0,&bTest,&cbSize);
	//if(lResult != ERROR_MORE_DATA && lResult != ERROR_SUCCESS)
	//{
	//	Log(_TEXT("Query Description for size Error: %d\r\n"),lResult);
	//	RegCloseKey(hVideoKey);
	//	*pdwGviError = lResult;
	//	return 4; //QueryValue失败
	//}
//_GetDeviceDescription:
	
	//********************************************************
	//修正版本之后不需要从注册表里读DeviceDescription这个键值
	//**********************************************************

	//lResult = RegQueryValueExW(hVideoKey,L"Device Description",0,0,g_lpDescription,&cbSize); //这里如果是unicode需要转
	//if(lResult != ERROR_SUCCESS)
	//{
	//	Log(_TEXT("QueryValue Description Error: %d"),lResult);
	//	RegCloseKey(hVideoKey);
	//	*pdwGviError = lResult;
	//	return 6; //分配内存后读取失败
	//}
	
	lResult = RegQueryValueEx(hVideoKey,_TEXT("InstalledDisplayDrivers"),0,0,(LPBYTE)szDriverName,&dwBuffSize);
	if(lResult != ERROR_SUCCESS)
	{
		Log(_TEXT("QueryValue InstalledDisplayDrivers Error: %d"),lResult);
		RegCloseKey(hVideoKey);
		*pdwGviError = lResult;
		return 7; //读取Installed失败
	}

	lstrcat(szDriverName,_TEXT(".dll"));

	dwGdvReturn = GetDisplayCardInfo_GetDriverVersion(szDriverName,&lpVersionInfo);

	pPartArray[Driver].lpContent = (LPWSTR)lpVersionInfo;
	pPartArray[Driver].dwInfoLen = (DWORD)(wcslen((LPWSTR)lpVersionInfo)*2);
	Log(_TEXT("Driver set into Array\r\n"));

	//获取版本信息失败，仅构造显卡设备信息
	//获取成功，开始构建整个显卡信息缓冲区

	RegCloseKey(hVideoKey);
	return 0;
}
