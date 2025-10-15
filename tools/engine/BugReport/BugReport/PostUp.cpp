#include "BugReport.h"
#include <wininet.h>
#include <shlwapi.h>
#include <dbghelp.h>
#include <commctrl.h>
#include "GlobalFunction.h"
#include "../ZipWriter/include/zippublic.h"
#include "Configuration.h"

#pragma comment(lib,"wininet.lib")
#pragma comment(lib,"shlwapi.lib")

//用于拼接post信息的全局字符串
TCHAR g_lpszAgent[] = _TEXT("Bug Reporter 1.0");    //tag1
TCHAR g_lpszPage[MAX_PATH] = _TEXT("bugreport.mhzx.locojoy.com");		//服务器地址，这里交互是个问题，需要由游戏主进程传递
TCHAR g_lpUploadApp[MAX_PATH] = _TEXT("/gameupload/FileSaveServlet");
INTERNET_PORT g_port = 80; // A word.

// charset=utf-8; 
// 为Content-Type添加charset属性，这样，服务器端的javax.servlet.ServletRequest.getCharacterEncoding()可以据此确定form-data的编码。
// 另外，由于历史原因，InfoPart部分的属性名是ANSI编码的，由于没有中文字符，此部分和utf-8兼容，而InfoValue部分则是UTF-16LE编码的。
// 这个问题留在服务器端处理。
// yanghaibo01238 
// 2011-07-12
LPCTSTR g_lpszHeader = _TEXT("Content-Type: multipart/form-data; charset=utf-8; boundary=--7d9271\r\n"); //post包头以及boundary定义
LPCSTR g_lpBoundary = "----7d9271";  //boundary
LPCSTR g_lpEnter = "\r\n";
LPCSTR g_lpDoubleBlank = "--";
LPCSTR g_lpInfoPart = "Content-Disposition: form-data; name=";  //除了文件段以外的part header
LPCSTR g_lpFilePart = "Content-Disposition: form-data; name=\"upload\"; filename=";  //文件段的part header
LPCSTR g_lpFilePartType = "Content-Type: application/octet-stream";

LPCSTR g_szPartNames[PartCount] = 
{
	"\"ExceptionCode\"",
	"\"ExceptionAddress\"",
	"\"Register\"",
	"\"StackInfo\"",
	"\"OSVersion\"",

	"\"Graphic\"",
	"\"Driver\"",
	"\"Descript\"",
	"\"AppVersion\"",
	"\"ComputerName\"",
	"\"UserName\"",
	"\"QQorMSN\"",
	"\"phoneNo\"",
	"\"Email\"",
};

//HANDLE hConnectedEvent,hCompleteEvent;
PartInfo g_szData[PartCount] = {0};

const size_t LEN = 65535;

// 释放内存
void FreeData()
{
	// ExceptionCode,ExceptionAddress,Register,StackInfo,OSVersion共用一段内存
	// 所以只用释放ExceptionCode指向内存
	HeapFree( GetProcessHeap(), 0, g_szData[ExceptionCode].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[Graphic].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[Driver].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[Descript].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[AppVersion].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[ComputerName].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[UserName].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[QQMsn].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[Phone].lpContent);
	HeapFree( GetProcessHeap(), 0, g_szData[EMail].lpContent);

	::DeleteFile( L"crashdump.zip");
}

/*
PostUp_CalcSize 用于计算需要post的buffer总长度,In bytes
返回值：0，获取文件大小失败 other成功
*/
DWORD WINAPI PostUp_CalcSize()
{
	HANDLE hDumpFile;
	DWORD dwFileSize,dwPartNumber,dwTotalSize = 0,dwBoundLen,dwPartHeaderLen;
	
	dwPartNumber = sizeof(g_szPartNames)/sizeof(g_szPartNames[0]); //获取特征字段的个数
	dwBoundLen = (DWORD)strlen(g_lpBoundary);
	dwPartHeaderLen = (DWORD)strlen(g_lpInfoPart);
	dwTotalSize += (dwBoundLen+dwPartHeaderLen+8)*dwPartNumber; //加上所有boundlen,总共数量为dwPartNumber
																//加上所有Content-disp...一个part中包含的\r\n个数
	for(UINT i = 0;i < dwPartNumber;i++)
	{
		dwTotalSize += (DWORD)strlen(g_szPartNames[i]); //加上"字段名"
		dwTotalSize += g_szData[i].dwInfoLen; //每个字段中Data的长度
	}

	//加入文件段的长度
	hDumpFile = CreateFile( g_szDumpFile,FILE_ALL_ACCESS,FILE_SHARE_READ|FILE_SHARE_WRITE,0,OPEN_EXISTING,0,0);
	if(hDumpFile != INVALID_HANDLE_VALUE)
	{
		dwTotalSize += dwBoundLen; 

		dwFileSize = GetFileSize(hDumpFile,0);
		CloseHandle(hDumpFile);

		dwTotalSize += (DWORD)strlen(g_lpFilePart);   //加上FilePart的头长度

		dwTotalSize += (DWORD)strlen( g_ccDumpFile) + 2;

		dwTotalSize += (DWORD)strlen(g_lpFilePartType); //加入filepart的contenttype长度

		dwTotalSize += dwFileSize; //加入文件大小

		dwTotalSize += 2*5; //filepart \r\n的长度
	}

	dwTotalSize += dwBoundLen + (DWORD)strlen(g_lpDoubleBlank); //加上所有boundlen,总共数量为dwPartNumber+2
	dwTotalSize += 2;

	return dwTotalSize;
}

/*
PostUp_Concat 拼接字符串
参数1： IN PVOID lpOutBuffer 用于POST的缓冲区地址
参数2： IN DWORD dwBufferSize lpOutBuffer的长度
*/
DWORD WINAPI PostUp_Concat(LPSTR lpOutBuffer,DWORD dwBufferSize)
{
	HANDLE hDumpFile;
	DWORD dwPartNumber,dwFileSize,dwBytesRead,dwIndex = 0;
	BOOL bFlag;
	DWORD dwBoundary = (DWORD)strlen(g_lpBoundary);
	DWORD dwEnter = 2;
	DWORD dwInfoPart = (DWORD)strlen(g_lpInfoPart);
	DWORD dwFilePart = (DWORD)strlen(g_lpFilePart);
	DWORD dwFilePartType = (DWORD)strlen(g_lpFilePartType);

	dwPartNumber = sizeof(g_szPartNames)/sizeof(g_szPartNames[0]);

	for(UINT i = 0;i < dwPartNumber;i++)  //拼接特征字段
	{
		RtlCopyMemory(lpOutBuffer,g_lpBoundary,dwBoundary);
		lpOutBuffer += dwBoundary;
		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;
		RtlCopyMemory(lpOutBuffer,g_lpInfoPart,dwInfoPart);
		lpOutBuffer += dwInfoPart;
		RtlCopyMemory(lpOutBuffer,g_szPartNames[i],strlen(g_szPartNames[i]));
		lpOutBuffer += strlen(g_szPartNames[i]);
		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;
		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;
		RtlCopyMemory(lpOutBuffer,g_szData[i].lpContent,g_szData[i].dwInfoLen);
		lpOutBuffer += g_szData[i].dwInfoLen;
		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;
	}

	//文件段

	hDumpFile = CreateFile( g_szDumpFile,FILE_ALL_ACCESS,FILE_SHARE_READ|FILE_SHARE_WRITE,0,OPEN_EXISTING,0,0);
	if(hDumpFile != INVALID_HANDLE_VALUE)
	{
		RtlCopyMemory(lpOutBuffer,g_lpBoundary,dwBoundary);
		lpOutBuffer += dwBoundary;
		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;
		RtlCopyMemory(lpOutBuffer,g_lpFilePart,dwFilePart);
		lpOutBuffer += dwFilePart;

		RtlCopyMemory(lpOutBuffer,"\"",1);
		lpOutBuffer++;
		RtlCopyMemory(lpOutBuffer, g_ccDumpFile,strlen(g_ccDumpFile));
		lpOutBuffer += strlen( g_ccDumpFile);
		RtlCopyMemory(lpOutBuffer,"\"",1);
		lpOutBuffer++;

		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;
		RtlCopyMemory(lpOutBuffer,g_lpFilePartType,dwFilePartType);
		lpOutBuffer += dwFilePartType;
		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;
		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;

		dwFileSize = GetFileSize(hDumpFile,0);
		bFlag = ReadFile(hDumpFile,(LPVOID)lpOutBuffer,dwFileSize,&dwBytesRead,0);
		CloseHandle(hDumpFile);
		if(dwBytesRead != dwFileSize)
		{
			//读取异常
			return -1;
		}

		lpOutBuffer += dwFileSize;
		RtlCopyMemory(lpOutBuffer,g_lpEnter,2);
		lpOutBuffer += 2;
	}

	RtlCopyMemory(lpOutBuffer,g_lpBoundary,dwBoundary);
	lpOutBuffer += dwBoundary;
	RtlCopyMemory(lpOutBuffer,g_lpDoubleBlank,2);
	lpOutBuffer += 2;
	RtlCopyMemory(lpOutBuffer,g_lpEnter,2);

	return 0;
}

// 得到应用程序版本号
DWORD WINAPI GetAppVersion()
{
	// 根据bugreport程序自身所在的位置，确定gameclient.exe程序或者其他程序所在的相对目录，并从bugreport.ini中获取
	// 程序的名字（如果不是gameclient.exe的话）。
	wchar_t	szPath[MAX_PATH] = {0};
	GetModuleFileNameW( NULL, szPath, MAX_PATH);
	wchar_t* szFind = wcsrchr( szPath, L'\\');
	if( NULL != szFind)
	{	
		(++szFind)[0] = 0;
	}

	wchar_t	szClientName[MAX_PATH] = {L"gameclient.exe"};	
	GetOptionValueUnicodeString(L"bugreport", L"game-client-name", szClientName, MAX_PATH);	
	wcscat_s( szPath, MAX_PATH, szClientName );

	DWORD cbSize = 6;
	wchar_t buffer[MAX_PATH] = {0};
	DWORD res = helper::GetFileVersion( szPath, buffer, MAX_PATH);
	if( res > 0)
	{
		cbSize = (DWORD)res*2;
		g_szData[AppVersion].dwInfoLen = cbSize;
		g_szData[AppVersion].lpContent = (LPWSTR)HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,cbSize);
		RtlCopyMemory( g_szData[AppVersion].lpContent, buffer, cbSize);
	}
	else
	{
		g_szData[AppVersion].dwInfoLen = cbSize;
		g_szData[AppVersion].lpContent = (LPWSTR)HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,cbSize);
		RtlCopyMemory( g_szData[AppVersion].lpContent, L"N/A", cbSize);
	}

	return cbSize;
}

// 得到用户机器信息
DWORD WINAPI GetComputerInfo()
{
	wchar_t buffer[MAX_PATH] = {0};
	DWORD res = helper::GetComputerName( buffer, MAX_PATH);
	if( res > 0)
	{
		DWORD cbSize = (DWORD)res*2;
		g_szData[ComputerName].dwInfoLen = cbSize;
		g_szData[ComputerName].lpContent = (LPWSTR)HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,cbSize);
		RtlCopyMemory( g_szData[ComputerName].lpContent, buffer, cbSize);
	}

	res = helper::GetComputerUserName( buffer, MAX_PATH);
	if( res > 0)
	{
		DWORD cbSize = (DWORD)res*2;
		g_szData[UserName].dwInfoLen = cbSize;
		g_szData[UserName].lpContent = (LPWSTR)HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,cbSize);
		RtlCopyMemory( g_szData[UserName].lpContent, buffer, cbSize);
	}
	return 0;
}

// 得到用户填写的描述信息
DWORD WINAPI GetDescriptInfo( HWND hEdit, ePartIndex index)
{
	wchar_t buffer[MAX_PATH] = { 0 };
	DWORD cbSize = 6;
	LRESULT res = SendMessageW( hEdit, WM_GETTEXT, MAX_PATH, (LPARAM)buffer);
	if( res > 0)
	{
		cbSize = (DWORD)res*2;
		g_szData[index].dwInfoLen = cbSize;
		g_szData[index].lpContent = (LPWSTR)HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,cbSize);
		RtlCopyMemory( g_szData[index].lpContent, buffer, cbSize);
	}
	else
	{
		g_szData[index].dwInfoLen = cbSize;
		g_szData[index].lpContent = (LPWSTR)HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,cbSize);
		RtlCopyMemory( g_szData[index].lpContent, L"N/A", cbSize);
	}

	return cbSize;
}

// 获得dump文件信息
static DWORD _GetDumpInfo()
{
	HANDLE hFile;
	HANDLE hFileMapping;
	DWORD dwFileSize;
	LPVOID lpMappingFile;

	//将dump文件map进内存

	hFile = CreateFile( g_szDumpFile,FILE_ALL_ACCESS,FILE_SHARE_READ,0,OPEN_EXISTING,0,0);
	if(hFile == INVALID_HANDLE_VALUE)
	{
		Log(_TEXT("CreateFile Error %s : %d\r\n"), g_szDumpFile,GetLastError());
		return 1;//打开文件失败
	}
	dwFileSize = GetFileSize(hFile,0);
	hFileMapping = CreateFileMapping(hFile,0,PAGE_READONLY,0,dwFileSize,0);
	if(hFileMapping == NULL ||hFileMapping == INVALID_HANDLE_VALUE)
	{
		Log(_TEXT("CreateFileMapping Error: %d\r\n"),GetLastError());
		CloseHandle(hFile);
		return 2;
	}
	lpMappingFile = MapViewOfFile(hFileMapping,FILE_MAP_READ,0,0,dwFileSize);
	if( NULL == lpMappingFile)
	{
		Log(_TEXT("MapViewOfFile Error: %d\r\n"),GetLastError());
		CloseHandle(hFileMapping);
		CloseHandle(hFile);
		return 3;
	}

	//获取异常信息和栈信息
	DWORD dwRet = GetDumpInfo_GetInfo(lpMappingFile,g_szData);
	
	UnmapViewOfFile(lpMappingFile);
	CloseHandle(hFileMapping);
	CloseHandle(hFile);

	return dwRet;
}

// 获得dump文件信息, 如果失败, 返回空CrushDmp信息和从API获得的操作系统版本信息
DWORD WINAPI GetDumpInfo()
{
	DWORD dwRet = 1;

	if( g_szDumpFile[0] != 0 )
	{
		dwRet = _GetDumpInfo();
		if ( dwRet != 0 )
		{
			Log(_TEXT("_GetDumpInfo Error!\r\n"));
		}
	}

	if ( dwRet != 0 )
	{
		GetDumpInfo_DummyInfo( g_szData);
	}

	return 0;
}

/*
PostUp_InitInfo =调用GetDumpInfo和GetDisplayInfo来初始化数据结构PartInfo数组
*/
DWORD WINAPI PostUp_InitInfo()
{
	DWORD dwError;
	GetDisplayCardInfo_GetVideoInfo(&dwError,g_szData); //获取显卡信息
	GetDescriptInfo( g_hDescript, Descript);		// 获得用户填写的描述信息
	GetDescriptInfo( g_hQQMsn, QQMsn);
	GetDescriptInfo( g_hPhone, Phone);
	GetDescriptInfo( g_hEMail, EMail);

	GetAppVersion();		// 获得gameclient.exe的版本号
	GetComputerInfo();
	return 0;
}

void FillUserInfoToBuffer( wchar_t* buffer, size_t& count, HWND hEdit, const wchar_t* attr)
{
	wchar_t text[MAX_PATH] = { 0 };
	LRESULT res = SendMessageW( hEdit, WM_GETTEXT, MAX_PATH, (LPARAM)text);
	if( res > 0)
	{
		res = swprintf_s( buffer, LEN - count, L"%s : %s\r\n", attr, text);
	}
	else
	{
		res = swprintf_s( buffer, LEN - count, L"%s : N/A\r\n", attr);
	}
	count += res;
}

// 将用户填写的信息打包到crashdump.zip文件
void PostUp_FillUserInfo( ZIP::CFileWriter& fw)
{
	WCHAR buffer[LEN] = {0xFEFF, 0};

	size_t count = 1;
	FillUserInfoToBuffer( buffer+count, count, g_hDescript, L"Descript");
	FillUserInfoToBuffer( buffer+count, count, g_hQQMsn, L"QQMsn");
	FillUserInfoToBuffer( buffer+count, count, g_hPhone, L"Phone");
	FillUserInfoToBuffer( buffer+count, count, g_hEMail, L"EMail");

	if( fw.WriteNextCentral( L"userinfo.txt" ))
	{
		if( !fw.WriteFileData( buffer, count*sizeof( WCHAR), true) )
		{
			Log(_TEXT("Zip WriteFileData userinfo.txt Error! (%d)\r\n"), ::GetLastError());
		}
	}
}

/*
PostUp_MakeCrashDumpZip = 生成CrashDump的zip文件
*/
DWORD WINAPI PostUp_MakeCrashDumpZip()
{
	int count = ListView_GetItemCount( g_hFileList);
	if ( count == 0 )
	{
		return 0;
	}

	NotifyStatus( _TEXT("正在压缩文件..."));

	ZIP::CNativeFile nf;
	if( !nf.Open( L"crashdump.zip", ZIP::FM_CREAT|ZIP::FM_TRUNC, ZIP::FA_RDWR))
	{
		Log(_TEXT("Create crashdump.zip  Error! Return Now(%d)\r\n"), ::GetLastError());
		return -1;
	}

	ZIP::CFileWriter fw;
	fw.SetFileIO( nf);

	char buffer[LEN] = {0};
	wchar_t nameBuffer[MAX_PATH] = {0};
	for( int i=0; i<count; i++)
	{
		ListView_GetItemText( g_hFileList, i, 0, nameBuffer, MAX_PATH);
		ZIP::CNativeFile nfChild;
		if( !nfChild.Open( nameBuffer, ZIP::FM_EXCL, ZIP::FA_RDONLY))
		{
			Log(_TEXT("Open file %s Error! Continue with error code %d.\r\n"), nameBuffer, ::GetLastError());
			continue;
		}

		wchar_t* filename = wcsrchr( nameBuffer, L'\\');
		if( filename)
		{
			filename ++;
		}
		else
		{
			filename = nameBuffer;
		}
		
		if( !fw.WriteNextCentral( filename ) )
		{
			Log(_TEXT("WriteNextCentral of %s Error! Returns %d.\r\n"), filename, ::GetLastError());
			return -1;
		}

		if( !fw.WriteFileData( nfChild ) )
		{
			Log(_TEXT("Read WriteFileData of %s Error! Returns %d.\r\n"), filename, ::GetLastError());
			return -1;
		}
	}

	PostUp_FillUserInfo( fw);

	fw.CloseFile();

	wcscpy_s( g_szDumpFile, L"crashdump.zip");
	strcpy_s( g_ccDumpFile, "crashdump.zip");
	
	return 0;
}

/*
PostUp_Progress 用于在发送期间伪装进度条状态的线程入口函数
参数:1:用于同步的hevent，最后需要减计数
*/
VOID WINAPI PostUp_Progress(LPVOID lParam)
{
	DWORD dwWait = 0;
	DWORD dwCurPos = 0;
	HANDLE hEvent = (HANDLE)lParam;
	SendMessage(g_hProgressBar,PBM_SETSTEP,5,0); //设置单步步长为5,默认总长度为100
	do{
		dwCurPos = (DWORD)SendMessage(g_hProgressBar,PBM_GETPOS,0,0);
		if(dwCurPos >= 50)
			SendMessage(g_hProgressBar,PBM_SETSTEP,1,0); //已经超过%50，放慢速度
		if(dwCurPos >= 95) //这么强悍。。慢到家了,咱不装了。。。
			break;
		SendMessage(g_hProgressBar,PBM_STEPIT,0,0);
		dwWait = WaitForSingleObject(hEvent,1000); //等待1s是否完成
	}while(dwWait == WAIT_TIMEOUT);

	if(dwWait == WAIT_OBJECT_0) //事件被置位
	{
		SendMessage(g_hProgressBar,PBM_SETPOS,100,0);
	}
	else //到了95%都没反应
	{
		Log(_TEXT("Upload Failed or very very slow...Return\r\n"));
		SendMessage(g_hProgressBar,PBM_SETPOS,100,0);
		//SetEvent(hCompleteEvent);//强制结束
	}
	CloseHandle(hEvent); //释放句柄
}


/*
PostUp_HttpCallBack Http操作的回调函数，会返回当前的状态
参数1：hInternet 发生回调的句柄
参数2：dwContext 标记回调发生具体所属操作的特征值
参数3：dwInternetStatus 回调状态
参数4：lpvStatusInformation 回调状态的具体信息
参数5：dwStatusInformationLength 具体信息的长度
*/
VOID CALLBACK PostUp_HttpCallBack(HINTERNET hInternet,DWORD dwContext,DWORD dwInternetStatus,LPVOID lpvStatusInformation,DWORD dwStatusInformationLength)
{
	static HANDLE hProgressEvent = 0;
	static HANDLE hProgressThread = 0;
	if(dwInternetStatus == INTERNET_STATUS_CONNECTING_TO_SERVER) //尝试连接服务器
	{
		//更新static内容为“正在连接服务器”
		NotifyStatus(_TEXT("正在连接服务器"));
	}
	else if(dwInternetStatus == INTERNET_STATUS_CONNECTED_TO_SERVER) //已经连接到服务器
	{
		
		hProgressThread = 0; //句柄置0
		//SetEvent(hConnectedEvent);
		NotifyStatus(_TEXT("已经连接到服务器"));
	}
	else if(dwInternetStatus == INTERNET_STATUS_SENDING_REQUEST)  //正在发送
	{
		NotifyStatus(_TEXT("正在发送..."));
		//开始创建进度条伪装线程,实测时可能会回调多次这个状态。。。
		if(hProgressThread == 0)
		{
			hProgressEvent = CreateEvent(0,1,0,_TEXT("progress"));
			hProgressThread = CreateThread(0,0,(LPTHREAD_START_ROUTINE)PostUp_Progress,(LPVOID)hProgressEvent,0,0);//线程开跑。。
			//让丫跑一会。。
			WaitForSingleObject(hProgressThread,3000); //跑三秒
			CloseHandle(hProgressThread);
		}
	}
	else if(dwInternetStatus == INTERNET_STATUS_REQUEST_COMPLETE ||
			dwInternetStatus == INTERNET_STATUS_CONNECTION_CLOSED) //成功发送或者断开连接
	{
		//进度条走满
		//SetEvent(hCompleteEvent);
		//SetEvent(hProgressEvent);//置位进度条线程同步事件，直接走满
		Log(_TEXT("CompleteEvent is set\r\n"));
		if(dwInternetStatus == INTERNET_STATUS_REQUEST_COMPLETE)
			Log(_TEXT("Request Complete callback routine!\r\n"));
		else
			Log(_TEXT("Connection closed callback routine!\r\n"));
		//CloseHandle(hProgressEvent);不用释放，由伪装线程释放
	}
}

static BOOL _ConfigureConnectTimeoutValues(HINTERNET hInternet)
{
	// 获取Internet缺省配置的初始值作为缺省值。
	DWORD dwConnectTimeout = 10000; // 10s
	DWORD dwBufferLength = sizeof(DWORD);
	if( !InternetQueryOptionW(hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwConnectTimeout, &dwBufferLength) )
	{
		DWORD dwError = ::GetLastError();
		Log(_TEXT("InternetQueryOptionW(INTERNET_OPTION_CONNECT_TIMEOUT, %u): ErrorCode: %d.\r\n"), dwConnectTimeout, dwError );
		dwConnectTimeout = 10000; 
	}
	else
	{
		Log(_TEXT("Internet API provides values: \r\n"));
		Log(_TEXT("dwConnectTimeout: %u\r\n"), dwConnectTimeout );
	}
	
	// 如果存在bugreport.ini文件，从中读取可能提供的配置值。
	static LPCWSTR file = GetConfigurationFilePath();	
	if( ::PathFileExistsW(file) )
	{
		if( GetConfigure_ConnectTimeout(dwConnectTimeout) )
		{
			Log(_TEXT("%s provides value: \r\n"), file);
			Log(_TEXT("dwConnectTimeout: %u\r\n"), dwConnectTimeout );
		}
	}

	DWORD dwError;
	BOOL bRet = InternetSetOptionW( hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwConnectTimeout, sizeof(DWORD) );
	if( !bRet )
	{
		dwError = ::GetLastError();
		Log(_TEXT("InternetSetOptionW(INTERNET_OPTION_CONNECT_TIMEOUT, %u): %d.\r\n"), dwConnectTimeout, dwError );
	}

	return TRUE;
}

// 配置 hRequest 数据收发相关的Timeout信息。
static BOOL _ConfigureDataTimeoutValues(HINTERNET hRequest)
{
	// 获取Internet缺省配置的初始值作为缺省值。
	DWORD dwSendTimeout = 60000; // 60s
	DWORD dwReceiveTimeout = 60000; // 60s
	DWORD dwError;
	DWORD dwBufferLength = sizeof(DWORD);
	BOOL bRet1 = InternetQueryOptionW( hRequest, INTERNET_OPTION_SEND_TIMEOUT, &dwSendTimeout, &dwBufferLength );
	if( !bRet1 )
	{
		dwError = ::GetLastError();
		Log(_TEXT("InternetQueryOptionW(INTERNET_OPTION_SEND_TIMEOUT, %u): ErrorCode: %d.\r\n"), dwSendTimeout, dwError );
		dwSendTimeout = 60000;
	}

	dwBufferLength = sizeof(DWORD);
	BOOL bRet2 = InternetQueryOptionW( hRequest, INTERNET_OPTION_RECEIVE_TIMEOUT, &dwReceiveTimeout, &dwBufferLength );
	if( !bRet2 )
	{
		dwError = ::GetLastError();
		Log(_TEXT("InternetQueryOptionW(INTERNET_OPTION_RECEIVE_TIMEOUT, %u): ErrorCode: %d.\r\n"), dwReceiveTimeout, dwError );
		dwReceiveTimeout = 60000;
	}

	if( bRet1 || bRet2 )
	{
		Log(_TEXT("Internet API provides values: \r\n"));

		if( bRet1 )
			Log(_TEXT("dwSendTimeout: %u\r\n"), dwSendTimeout );
		if( bRet2 )
			Log(_TEXT("dwReceiveTimeout: %u\r\n"), dwReceiveTimeout );
	}
	
	// 如果存在bugreport.ini文件，从中读取可能提供的配置值。
	static LPCWSTR file = GetConfigurationFilePath();	
	if( ::PathFileExistsW(file) )
	{
		if( GetConfigure_SendTimeout(dwSendTimeout ) )
		{
			Log(_TEXT("%s provides values: \r\n"), file);
			Log(_TEXT("dwSendTimeout: %u\r\n"), dwSendTimeout );
			if( GetConfigure_ReceiveTimeout(dwReceiveTimeout) )
			{
				Log(_TEXT("dwReceiveTimeout: %u\r\n"), dwReceiveTimeout );
			}
		}
		else
		{
			if( GetConfigure_ReceiveTimeout(dwReceiveTimeout) )
			{
				Log(_TEXT("%s provides value: \r\n"), file);
				Log(_TEXT("dwReceiveTimeout: %u\r\n"), dwReceiveTimeout );
			}
		}		
	}

	BOOL bRet = InternetSetOptionW( hRequest, INTERNET_OPTION_SEND_TIMEOUT, &dwSendTimeout, sizeof(DWORD) );
	if( !bRet )
	{
		dwError = ::GetLastError();
		Log(_TEXT("InternetSetOptionW(INTERNET_OPTION_SEND_TIMEOUT, %u): %d.\r\n"), dwSendTimeout, dwError );
	}

	bRet = InternetSetOptionW( hRequest, INTERNET_OPTION_RECEIVE_TIMEOUT, &dwReceiveTimeout, sizeof(DWORD) );
	if( !bRet )
	{
		dwError = ::GetLastError();
		Log(_TEXT("InternetSetOptionW(INTERNET_OPTION_RECEIVE_TIMEOUT, %u): %d.\r\n"), dwReceiveTimeout, dwError );
	}

	return TRUE;
}

static BOOL _SendData(DWORD dwTotalSize, LPVOID lpPostBuffer)
{
	BOOL bSuccess = FALSE;
	HINTERNET hInternet;
	HINTERNET hHttp;
	HINTERNET hRequest;
	DWORD dwContext = 100;
	INTERNET_BUFFERS iBuffers ={0};

	//INTERNET_STATUS_CALLBACK CallBackPointer = 0;
	//hCompleteEvent = CreateEvent(0,0,0,0);
	//hConnectedEvent = CreateEvent(0,0,0,0);

	NotifyStatus(_TEXT("正在连接服务器..."));

	hInternet = InternetOpen(g_lpszAgent,INTERNET_OPEN_TYPE_PRECONFIG,NULL,NULL,0);//INTERNET_FLAG_ASYNC); //创建internet handle
	if(hInternet == NULL)
	{
		Log(_TEXT("Internet Open Error: %d\r\n"),GetLastError());
		goto Exit;
	}
	
	// 配置 Internet 连接相关的Timeout信息。
	_ConfigureConnectTimeoutValues(hInternet);

	INTERNET_PORT port = g_port;
	GetConfigure_HttpServerPort( port );	
	LPCWSTR pszPage = GetConfigure_HttpServerName(g_lpszPage, _countof(g_lpszPage) );

	Log(_TEXT("Internet Address: %s:%hu\r\n"), pszPage, port);

	hHttp = InternetConnect(hInternet,pszPage,port,0,0,INTERNET_SERVICE_HTTP,0,0);
	if(hHttp == NULL)
	{
		Log(_TEXT("Internet Connect Error: %d\r\n"),GetLastError());
		goto Exit;
	}

	LPCWSTR pszUploadApp = GetConfigure_HttpServerApplication(g_lpUploadApp, _countof(g_lpUploadApp) );
	Log(_TEXT("Internet Application: %s\r\n"), pszUploadApp );

	hRequest = HttpOpenRequest(hHttp,
		_TEXT("POST"),
		pszUploadApp,
		0,0,0,INTERNET_FLAG_RELOAD|INTERNET_FLAG_NO_CACHE_WRITE,(DWORD_PTR)&dwContext);

	if(hRequest == NULL)
	{	
		Log(_TEXT("Internet Open Request Error: %d\r\n"),GetLastError());
		goto Exit;
	}

	// 配置 hRequest 数据收发相关的Timeout信息。
	_ConfigureDataTimeoutValues(hRequest);

	// 用异步回调的方式在实际测试中有局限，当文件大于1M的时候 linux+tomcat 服务器会出现连接超时异常
	// 而且客户端也无法得到上传进度，所以改为同步函数 InternetWriteFile 实现

	//CallBackPointer = InternetSetStatusCallback(hRequest,(INTERNET_STATUS_CALLBACK)PostUp_HttpCallBack);
	//if(CallBackPointer == INTERNET_INVALID_STATUS_CALLBACK)
	//{
	//	Log(_TEXT("Internet Set CallBack Error: %d\r\n"),GetLastError());
	//	goto Exit;
	//}

	NotifyStatus(_TEXT("正在发送..."));

	iBuffers.dwStructSize = sizeof(INTERNET_BUFFERS);
	iBuffers.lpvBuffer = 0;//lpPostBuffer;
	iBuffers.dwBufferLength = 0;//dwTotalSize;
	iBuffers.dwBufferTotal = dwTotalSize;
	iBuffers.lpcszHeader = g_lpszHeader;
	iBuffers.dwHeadersLength = lstrlen(g_lpszHeader);

	if( !HttpSendRequestEx(hRequest,&iBuffers,0,0,0))
	{
		Log(_TEXT("HttpSendRequestEx error: %d\r\n"),GetLastError());
		goto Exit;
	}

	const DWORD MAX_SIZE = 10*1024;		// 每次发10k
	int maxpos = dwTotalSize / MAX_SIZE;	
	PostMessage( g_hProgressBar, PBM_SETRANGE, 0, MAKELPARAM( 0, maxpos+1));
	PostMessage( g_hProgressBar, PBM_SETSTEP, 1, 0); //设置单步步长

	DWORD dwTotalSend = 0, dwCurSend = 0;
	char* buffer = (char*)lpPostBuffer;
	while( true)
	{
		dwCurSend = dwTotalSize - dwTotalSend;
		if( (int)dwCurSend <= 0)
		{
			break;
		}
		if( dwCurSend > MAX_SIZE)
		{
			dwCurSend = MAX_SIZE; 
		}

		DWORD dwWritten = 0;
		if( !InternetWriteFile( hRequest, buffer, dwCurSend, &dwWritten))
		{
			Log(_TEXT("InternetWriteFile error: %d\r\n"),GetLastError());
			goto Exit;
		}

		dwTotalSend += dwCurSend;
		buffer += dwCurSend;

		PostMessage( g_hProgressBar, PBM_STEPIT, 0, 0);
	}

	if( !HttpEndRequest( hRequest, 0, 0, 0) )
	{
		Log(_TEXT("HttpEndRequest error: %d\r\n"),GetLastError());
		goto Exit;
	}

	bSuccess = TRUE;

Exit:
	if(hRequest)
		InternetCloseHandle(hRequest);
	if(hHttp)
		InternetCloseHandle(hHttp);
	if(hInternet)
		InternetCloseHandle(hInternet);

	return bSuccess;
}

static BOOL _PrepareInfo(DWORD& dwTotalSize, LPVOID& lpPostBuffer)
{
	dwTotalSize = 0;
	lpPostBuffer = NULL;

	dwTotalSize = PostUp_CalcSize();
	if( 0 == dwTotalSize)
	{
		Log(_TEXT(" PostUp_CalcSize Error: %d\r\n"),GetLastError());
		//释放数据结构数组中分配的堆内存
		return FALSE;
	}
	lpPostBuffer = HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,dwTotalSize);
	if(lpPostBuffer == NULL)
	{
		Log(_TEXT("HeapAlloc PostBuffer Error: %d\r\n"),GetLastError());
		//释放数据结构数组中分配的堆内存
		return FALSE;
	}

	if( PostUp_Concat((LPSTR)lpPostBuffer,dwTotalSize) != 0)
	{
		Log(_TEXT(" PostUp_Concat Error: %d\r\n"),GetLastError());

		//释放数据结构数组中分配的堆内存
		return FALSE;
	}

	return TRUE;
}

static BOOL _PostInfo()
{
	GetDumpInfo();
	PostUp_InitInfo();

	if( PostUp_MakeCrashDumpZip() != 0)
	{
		return FALSE;
	}

	DWORD dwTotalSize;
	LPVOID lpPostBuffer = 0;
	BOOL bSuccess = _PrepareInfo(dwTotalSize, lpPostBuffer);
	FreeData();
	if ( !bSuccess )
	{
		return FALSE;
	}
	
	bSuccess = _SendData( dwTotalSize, lpPostBuffer );

	HeapFree(GetProcessHeap(),0,lpPostBuffer);

	return bSuccess;
}

/*
PostUp_PostInfo post线程入口函数
*/
DWORD WINAPI PostUp_PostInfo()
{
	BOOL bSuccess = _PostInfo();
	if( bSuccess )
	{
		NotifyStatus(_TEXT("发送完毕！"));
	}
	else
	{
		NotifyStatus(_TEXT("发送失败,请重新尝试!"));
	}

	PostMessage( g_hDlg, WM_SENDRESULT, bSuccess, 0);

	Log(_TEXT("PostThread return!\r\n"));

	g_hPostThread = 0;

	return 0;
}