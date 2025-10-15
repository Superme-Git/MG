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

//����ƴ��post��Ϣ��ȫ���ַ���
TCHAR g_lpszAgent[] = _TEXT("Bug Reporter 1.0");    //tag1
TCHAR g_lpszPage[MAX_PATH] = _TEXT("bugreport.mhzx.locojoy.com");		//��������ַ�����ｻ���Ǹ����⣬��Ҫ����Ϸ�����̴���
TCHAR g_lpUploadApp[MAX_PATH] = _TEXT("/gameupload/FileSaveServlet");
INTERNET_PORT g_port = 80; // A word.

// charset=utf-8; 
// ΪContent-Type���charset���ԣ��������������˵�javax.servlet.ServletRequest.getCharacterEncoding()���Ծݴ�ȷ��form-data�ı��롣
// ���⣬������ʷԭ��InfoPart���ֵ���������ANSI����ģ�����û�������ַ����˲��ֺ�utf-8���ݣ���InfoValue��������UTF-16LE����ġ�
// ����������ڷ������˴���
// yanghaibo01238 
// 2011-07-12
LPCTSTR g_lpszHeader = _TEXT("Content-Type: multipart/form-data; charset=utf-8; boundary=--7d9271\r\n"); //post��ͷ�Լ�boundary����
LPCSTR g_lpBoundary = "----7d9271";  //boundary
LPCSTR g_lpEnter = "\r\n";
LPCSTR g_lpDoubleBlank = "--";
LPCSTR g_lpInfoPart = "Content-Disposition: form-data; name=";  //�����ļ��������part header
LPCSTR g_lpFilePart = "Content-Disposition: form-data; name=\"upload\"; filename=";  //�ļ��ε�part header
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

// �ͷ��ڴ�
void FreeData()
{
	// ExceptionCode,ExceptionAddress,Register,StackInfo,OSVersion����һ���ڴ�
	// ����ֻ���ͷ�ExceptionCodeָ���ڴ�
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
PostUp_CalcSize ���ڼ�����Ҫpost��buffer�ܳ���,In bytes
����ֵ��0����ȡ�ļ���Сʧ�� other�ɹ�
*/
DWORD WINAPI PostUp_CalcSize()
{
	HANDLE hDumpFile;
	DWORD dwFileSize,dwPartNumber,dwTotalSize = 0,dwBoundLen,dwPartHeaderLen;
	
	dwPartNumber = sizeof(g_szPartNames)/sizeof(g_szPartNames[0]); //��ȡ�����ֶεĸ���
	dwBoundLen = (DWORD)strlen(g_lpBoundary);
	dwPartHeaderLen = (DWORD)strlen(g_lpInfoPart);
	dwTotalSize += (dwBoundLen+dwPartHeaderLen+8)*dwPartNumber; //��������boundlen,�ܹ�����ΪdwPartNumber
																//��������Content-disp...һ��part�а�����\r\n����
	for(UINT i = 0;i < dwPartNumber;i++)
	{
		dwTotalSize += (DWORD)strlen(g_szPartNames[i]); //����"�ֶ���"
		dwTotalSize += g_szData[i].dwInfoLen; //ÿ���ֶ���Data�ĳ���
	}

	//�����ļ��εĳ���
	hDumpFile = CreateFile( g_szDumpFile,FILE_ALL_ACCESS,FILE_SHARE_READ|FILE_SHARE_WRITE,0,OPEN_EXISTING,0,0);
	if(hDumpFile != INVALID_HANDLE_VALUE)
	{
		dwTotalSize += dwBoundLen; 

		dwFileSize = GetFileSize(hDumpFile,0);
		CloseHandle(hDumpFile);

		dwTotalSize += (DWORD)strlen(g_lpFilePart);   //����FilePart��ͷ����

		dwTotalSize += (DWORD)strlen( g_ccDumpFile) + 2;

		dwTotalSize += (DWORD)strlen(g_lpFilePartType); //����filepart��contenttype����

		dwTotalSize += dwFileSize; //�����ļ���С

		dwTotalSize += 2*5; //filepart \r\n�ĳ���
	}

	dwTotalSize += dwBoundLen + (DWORD)strlen(g_lpDoubleBlank); //��������boundlen,�ܹ�����ΪdwPartNumber+2
	dwTotalSize += 2;

	return dwTotalSize;
}

/*
PostUp_Concat ƴ���ַ���
����1�� IN PVOID lpOutBuffer ����POST�Ļ�������ַ
����2�� IN DWORD dwBufferSize lpOutBuffer�ĳ���
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

	for(UINT i = 0;i < dwPartNumber;i++)  //ƴ�������ֶ�
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

	//�ļ���

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
			//��ȡ�쳣
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

// �õ�Ӧ�ó���汾��
DWORD WINAPI GetAppVersion()
{
	// ����bugreport�����������ڵ�λ�ã�ȷ��gameclient.exe������������������ڵ����Ŀ¼������bugreport.ini�л�ȡ
	// ��������֣��������gameclient.exe�Ļ�����
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

// �õ��û�������Ϣ
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

// �õ��û���д��������Ϣ
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

// ���dump�ļ���Ϣ
static DWORD _GetDumpInfo()
{
	HANDLE hFile;
	HANDLE hFileMapping;
	DWORD dwFileSize;
	LPVOID lpMappingFile;

	//��dump�ļ�map���ڴ�

	hFile = CreateFile( g_szDumpFile,FILE_ALL_ACCESS,FILE_SHARE_READ,0,OPEN_EXISTING,0,0);
	if(hFile == INVALID_HANDLE_VALUE)
	{
		Log(_TEXT("CreateFile Error %s : %d\r\n"), g_szDumpFile,GetLastError());
		return 1;//���ļ�ʧ��
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

	//��ȡ�쳣��Ϣ��ջ��Ϣ
	DWORD dwRet = GetDumpInfo_GetInfo(lpMappingFile,g_szData);
	
	UnmapViewOfFile(lpMappingFile);
	CloseHandle(hFileMapping);
	CloseHandle(hFile);

	return dwRet;
}

// ���dump�ļ���Ϣ, ���ʧ��, ���ؿ�CrushDmp��Ϣ�ʹ�API��õĲ���ϵͳ�汾��Ϣ
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
PostUp_InitInfo =����GetDumpInfo��GetDisplayInfo����ʼ�����ݽṹPartInfo����
*/
DWORD WINAPI PostUp_InitInfo()
{
	DWORD dwError;
	GetDisplayCardInfo_GetVideoInfo(&dwError,g_szData); //��ȡ�Կ���Ϣ
	GetDescriptInfo( g_hDescript, Descript);		// ����û���д��������Ϣ
	GetDescriptInfo( g_hQQMsn, QQMsn);
	GetDescriptInfo( g_hPhone, Phone);
	GetDescriptInfo( g_hEMail, EMail);

	GetAppVersion();		// ���gameclient.exe�İ汾��
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

// ���û���д����Ϣ�����crashdump.zip�ļ�
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
PostUp_MakeCrashDumpZip = ����CrashDump��zip�ļ�
*/
DWORD WINAPI PostUp_MakeCrashDumpZip()
{
	int count = ListView_GetItemCount( g_hFileList);
	if ( count == 0 )
	{
		return 0;
	}

	NotifyStatus( _TEXT("����ѹ���ļ�..."));

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
PostUp_Progress �����ڷ����ڼ�αװ������״̬���߳���ں���
����:1:����ͬ����hevent�������Ҫ������
*/
VOID WINAPI PostUp_Progress(LPVOID lParam)
{
	DWORD dwWait = 0;
	DWORD dwCurPos = 0;
	HANDLE hEvent = (HANDLE)lParam;
	SendMessage(g_hProgressBar,PBM_SETSTEP,5,0); //���õ�������Ϊ5,Ĭ���ܳ���Ϊ100
	do{
		dwCurPos = (DWORD)SendMessage(g_hProgressBar,PBM_GETPOS,0,0);
		if(dwCurPos >= 50)
			SendMessage(g_hProgressBar,PBM_SETSTEP,1,0); //�Ѿ�����%50�������ٶ�
		if(dwCurPos >= 95) //��ôǿ��������������,�۲�װ�ˡ�����
			break;
		SendMessage(g_hProgressBar,PBM_STEPIT,0,0);
		dwWait = WaitForSingleObject(hEvent,1000); //�ȴ�1s�Ƿ����
	}while(dwWait == WAIT_TIMEOUT);

	if(dwWait == WAIT_OBJECT_0) //�¼�����λ
	{
		SendMessage(g_hProgressBar,PBM_SETPOS,100,0);
	}
	else //����95%��û��Ӧ
	{
		Log(_TEXT("Upload Failed or very very slow...Return\r\n"));
		SendMessage(g_hProgressBar,PBM_SETPOS,100,0);
		//SetEvent(hCompleteEvent);//ǿ�ƽ���
	}
	CloseHandle(hEvent); //�ͷž��
}


/*
PostUp_HttpCallBack Http�����Ļص��������᷵�ص�ǰ��״̬
����1��hInternet �����ص��ľ��
����2��dwContext ��ǻص�����������������������ֵ
����3��dwInternetStatus �ص�״̬
����4��lpvStatusInformation �ص�״̬�ľ�����Ϣ
����5��dwStatusInformationLength ������Ϣ�ĳ���
*/
VOID CALLBACK PostUp_HttpCallBack(HINTERNET hInternet,DWORD dwContext,DWORD dwInternetStatus,LPVOID lpvStatusInformation,DWORD dwStatusInformationLength)
{
	static HANDLE hProgressEvent = 0;
	static HANDLE hProgressThread = 0;
	if(dwInternetStatus == INTERNET_STATUS_CONNECTING_TO_SERVER) //�������ӷ�����
	{
		//����static����Ϊ���������ӷ�������
		NotifyStatus(_TEXT("�������ӷ�����"));
	}
	else if(dwInternetStatus == INTERNET_STATUS_CONNECTED_TO_SERVER) //�Ѿ����ӵ�������
	{
		
		hProgressThread = 0; //�����0
		//SetEvent(hConnectedEvent);
		NotifyStatus(_TEXT("�Ѿ����ӵ�������"));
	}
	else if(dwInternetStatus == INTERNET_STATUS_SENDING_REQUEST)  //���ڷ���
	{
		NotifyStatus(_TEXT("���ڷ���..."));
		//��ʼ����������αװ�߳�,ʵ��ʱ���ܻ�ص�������״̬������
		if(hProgressThread == 0)
		{
			hProgressEvent = CreateEvent(0,1,0,_TEXT("progress"));
			hProgressThread = CreateThread(0,0,(LPTHREAD_START_ROUTINE)PostUp_Progress,(LPVOID)hProgressEvent,0,0);//�߳̿��ܡ���
			//��Ѿ��һ�ᡣ��
			WaitForSingleObject(hProgressThread,3000); //������
			CloseHandle(hProgressThread);
		}
	}
	else if(dwInternetStatus == INTERNET_STATUS_REQUEST_COMPLETE ||
			dwInternetStatus == INTERNET_STATUS_CONNECTION_CLOSED) //�ɹ����ͻ��߶Ͽ�����
	{
		//����������
		//SetEvent(hCompleteEvent);
		//SetEvent(hProgressEvent);//��λ�������߳�ͬ���¼���ֱ������
		Log(_TEXT("CompleteEvent is set\r\n"));
		if(dwInternetStatus == INTERNET_STATUS_REQUEST_COMPLETE)
			Log(_TEXT("Request Complete callback routine!\r\n"));
		else
			Log(_TEXT("Connection closed callback routine!\r\n"));
		//CloseHandle(hProgressEvent);�����ͷţ���αװ�߳��ͷ�
	}
}

static BOOL _ConfigureConnectTimeoutValues(HINTERNET hInternet)
{
	// ��ȡInternetȱʡ���õĳ�ʼֵ��Ϊȱʡֵ��
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
	
	// �������bugreport.ini�ļ������ж�ȡ�����ṩ������ֵ��
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

// ���� hRequest �����շ���ص�Timeout��Ϣ��
static BOOL _ConfigureDataTimeoutValues(HINTERNET hRequest)
{
	// ��ȡInternetȱʡ���õĳ�ʼֵ��Ϊȱʡֵ��
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
	
	// �������bugreport.ini�ļ������ж�ȡ�����ṩ������ֵ��
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

	NotifyStatus(_TEXT("�������ӷ�����..."));

	hInternet = InternetOpen(g_lpszAgent,INTERNET_OPEN_TYPE_PRECONFIG,NULL,NULL,0);//INTERNET_FLAG_ASYNC); //����internet handle
	if(hInternet == NULL)
	{
		Log(_TEXT("Internet Open Error: %d\r\n"),GetLastError());
		goto Exit;
	}
	
	// ���� Internet ������ص�Timeout��Ϣ��
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

	// ���� hRequest �����շ���ص�Timeout��Ϣ��
	_ConfigureDataTimeoutValues(hRequest);

	// ���첽�ص��ķ�ʽ��ʵ�ʲ������о��ޣ����ļ�����1M��ʱ�� linux+tomcat ��������������ӳ�ʱ�쳣
	// ���ҿͻ���Ҳ�޷��õ��ϴ����ȣ����Ը�Ϊͬ������ InternetWriteFile ʵ��

	//CallBackPointer = InternetSetStatusCallback(hRequest,(INTERNET_STATUS_CALLBACK)PostUp_HttpCallBack);
	//if(CallBackPointer == INTERNET_INVALID_STATUS_CALLBACK)
	//{
	//	Log(_TEXT("Internet Set CallBack Error: %d\r\n"),GetLastError());
	//	goto Exit;
	//}

	NotifyStatus(_TEXT("���ڷ���..."));

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

	const DWORD MAX_SIZE = 10*1024;		// ÿ�η�10k
	int maxpos = dwTotalSize / MAX_SIZE;	
	PostMessage( g_hProgressBar, PBM_SETRANGE, 0, MAKELPARAM( 0, maxpos+1));
	PostMessage( g_hProgressBar, PBM_SETSTEP, 1, 0); //���õ�������

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
		//�ͷ����ݽṹ�����з���Ķ��ڴ�
		return FALSE;
	}
	lpPostBuffer = HeapAlloc(GetProcessHeap(),HEAP_ZERO_MEMORY,dwTotalSize);
	if(lpPostBuffer == NULL)
	{
		Log(_TEXT("HeapAlloc PostBuffer Error: %d\r\n"),GetLastError());
		//�ͷ����ݽṹ�����з���Ķ��ڴ�
		return FALSE;
	}

	if( PostUp_Concat((LPSTR)lpPostBuffer,dwTotalSize) != 0)
	{
		Log(_TEXT(" PostUp_Concat Error: %d\r\n"),GetLastError());

		//�ͷ����ݽṹ�����з���Ķ��ڴ�
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
PostUp_PostInfo post�߳���ں���
*/
DWORD WINAPI PostUp_PostInfo()
{
	BOOL bSuccess = _PostInfo();
	if( bSuccess )
	{
		NotifyStatus(_TEXT("������ϣ�"));
	}
	else
	{
		NotifyStatus(_TEXT("����ʧ��,�����³���!"));
	}

	PostMessage( g_hDlg, WM_SENDRESULT, bSuccess, 0);

	Log(_TEXT("PostThread return!\r\n"));

	g_hPostThread = 0;

	return 0;
}