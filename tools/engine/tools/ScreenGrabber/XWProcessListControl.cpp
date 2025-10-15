#include "StdAfx.h"
#include "XWProcessListControl.h"
#include "VersionInfo.h"
#include "ProcessInfo.h"
#include <Tlhelp32.h>

CXWProcessListControl::CXWProcessListControl(void)
{
}

CXWProcessListControl::~CXWProcessListControl(void)
{
}

//
// ������Ϣ·��/���غ���(������,���ظ�����Ϣ)
//
BOOL CXWProcessListControl::OnWndMsg( UINT message, WPARAM wParam, LPARAM lParam, LRESULT* pResult )
{
	if ( message == WM_LBUTTONDBLCLK )
	{
		UpdateView();
		*pResult = 1;
		return TRUE;
	}

	return __super::OnWndMsg(message, wParam, lParam, pResult );
}

enum COLUMNID
{
	COL_PID = 0,
	COL_PARENT_PID = 1,
	COL_THREAD_COUNT,
	COL_EXE_NAME,			//
	COL_CREATION_TIME,		//
	COL_FILE_DESCRIPTION,	//
	COL_MODULE_FILE_PATH,	//
	COL_FILE_VERSION,		//
	COL_PRODUCT_VERSION,	//
};

LPWSTR rgColumnNames[] = 
{
	L"PID",
	L"P-PID",
	L"�߳���",
	L"ӳ������",
	L"����ʱ��",
	L"����",
	L"ӳ��·��",
	L"�ļ��汾",
	L"��Ʒ�汾",
};

BOOL CXWProcessListControl::InitData()
{
	HWND hList = GetSafeHwnd();

	_ASSERT( ::IsWindow(hList) );

	LVCOLUMN lvColumn = {0};
	
	int nRet, iCol;
	lvColumn.mask = LVCF_TEXT | LVCF_WIDTH | LVCF_FMT;
	lvColumn.cchTextMax = _MAX_PATH;
	lvColumn.fmt = LVCFMT_LEFT;
	lvColumn.cx = 100; 
	for( iCol = 0; iCol < _countof(rgColumnNames); iCol ++ )
	{
		lvColumn.pszText = rgColumnNames[iCol];
		nRet = ListView_InsertColumn( hList, iCol, &lvColumn );
	}

	for( iCol = 0; iCol < _countof(rgColumnNames); iCol ++ )
	{
		ListView_SetColumnWidth( hList, iCol, LVSCW_AUTOSIZE_USEHEADER );
		int nWidth = ListView_GetColumnWidth( hList, iCol );
		ListView_SetColumnWidth( hList, iCol, nWidth );
	}

	return TRUE;
}

int CXWProcessListControl::OnCreate( LPCREATESTRUCT lpCreateStruct )
{
	if( __super::OnCreate(lpCreateStruct) != 0 )
	{
		return -1;
	}
	HWND hList = GetSafeHwnd();

	_ASSERT( ::IsWindow(hList) );

	// CreateWindowʱ����LVS_REPORT�������ã�ֻ����������
	LONG lStyle = GetWindowLong( hList, GWL_STYLE );
	SetWindowLong(hList, GWL_STYLE, lStyle | LVS_REPORT );
	ListView_SetView( hList, LV_VIEW_DETAILS );
	ListView_SetExtendedListViewStyle( hList,
		LVS_EX_GRIDLINES | LVS_EX_FULLROWSELECT | LVS_EX_SUBITEMIMAGES ); 

	InitData();

	UpdateView();

	return 0;
}


BOOL CXWProcessListControl::UpdateView()
{
	HANDLE hProcessSnap = NULL;
	PROCESSENTRY32 pe32 = {0};
	HANDLE hProcessParent = NULL;
	DWORD dwParentProcessID = 0;

	hProcessSnap = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);

	if (hProcessSnap == INVALID_HANDLE_VALUE)
		return FALSE;

	pe32.dwSize = sizeof(pe32);

	if (!Process32First(hProcessSnap, &pe32))
	{
		CloseHandle(hProcessSnap);
		return FALSE;
	}

	LockWindowUpdate();

	ListView_DeleteAllItems(GetSafeHwnd());

	HWND hList = GetSafeHwnd();

	LVITEM lvItem = {0};
	lvItem.mask = LVIF_TEXT;
	lvItem.cchTextMax = 100;
	lvItem.iItem = 0;

	SYSTEMTIME currentTime;
	GetSystemTime(&currentTime);

	int iItem;
	WCHAR Buffer[MAX_PATH] = {0};
	WCHAR Path[MAX_PATH] = {0};
	do
	{
		StringCchPrintfW( Buffer, MAX_PATH, L"%d", pe32.th32ProcessID );
		lvItem.pszText = Buffer;
		iItem = ListView_InsertItem( hList, &lvItem ); // PID
		StringCchPrintfW( Buffer, MAX_PATH, L"%d", pe32.th32ParentProcessID );
		ListView_SetItemText( hList, iItem, COL_PARENT_PID, Buffer ); // P-PID
		ListView_SetItemText( hList, iItem, COL_EXE_NAME, pe32.szExeFile ); // ӳ������

		if( pe32.th32ProcessID > 4 )
		{
			CProcessInfo _processInfo;
			if( _processInfo.Open( pe32.th32ProcessID ) )
			{
				SYSTEMTIME systemTime = {0};
				if( _processInfo.GetCreationLocalSystemTime( &systemTime ) )
				{
					if( currentTime.wDay != systemTime.wDay ||
						currentTime.wMonth != systemTime.wMonth ||
						currentTime.wYear != systemTime.wYear )
					{
						StringCchPrintfW( Buffer, MAX_PATH, L"%04hu-%02hu-%02hu %02hu:%02hu:%02hu",
							currentTime.wYear, systemTime.wMonth, systemTime.wDay,
							systemTime.wHour, systemTime.wMinute, systemTime.wSecond );
					}
					else
					{
						StringCchPrintfW( Buffer, MAX_PATH, L"%02hu:%02hu:%02hu",
							systemTime.wHour, systemTime.wMinute, systemTime.wSecond );
					}

					ListView_SetItemText( hList, iItem, COL_CREATION_TIME, Buffer ); 
				}

				if( _processInfo.GetExePathName( Path, MAX_PATH) )
				{
					ListView_SetItemText( hList, iItem, COL_MODULE_FILE_PATH, Path ); // "ӳ��·��"
					CVersionInfo _versionInfo;
					if( _versionInfo.Init( Path ) )
					{
						BOOL bRet = _versionInfo.GetFileDescription( Buffer, MAX_PATH );
						if( bRet )
						{
							ListView_SetItemText( hList, iItem, COL_FILE_DESCRIPTION, Buffer ); // "����"
						}
						VERSION_TYPE fileVersion, productVersion;
						bRet = _versionInfo.GetModuleVersions( &fileVersion, &productVersion);
						if ( bRet )
						{
							StringCchPrintfW( Buffer, MAX_PATH, L"%hu.%hu.%hu.%hu",
								fileVersion.wVersion[0],
								fileVersion.wVersion[1],
								fileVersion.wVersion[2],
								fileVersion.wVersion[3]);
							ListView_SetItemText( hList, iItem, COL_FILE_VERSION, Buffer ); // "�ļ��汾"
							StringCchPrintfW( Buffer, MAX_PATH, L"%hu.%hu.%hu.%hu",
								productVersion.wVersion[0],
								productVersion.wVersion[1],
								productVersion.wVersion[2],
								productVersion.wVersion[3]);
							ListView_SetItemText( hList, iItem, COL_PRODUCT_VERSION, Buffer ); // "��Ʒ�汾"
						}
					}
				}
				else
				{
					DWORD dwCount = _processInfo.GetImageFileName( Path, MAX_PATH );
					if( dwCount != 0 )
					{
						ListView_SetItemText( hList, iItem, COL_MODULE_FILE_PATH, Path ); // "ӳ��·��"
					}
				}
			}
			else
			{
				// ���Ȩ�޲��㣬��ô�죿
				StringCchPrintfW( Buffer, MAX_PATH, L"GetModulePath(%d) Failed:(%d)",
					pe32.th32ProcessID, GetLastError() );
				ListView_SetItemText( hList, iItem, COL_MODULE_FILE_PATH, Buffer ); // "ӳ��·��"
			}
		}

		StringCchPrintfW( Buffer, MAX_PATH, L"%d", pe32.cntThreads );
		ListView_SetItemText( hList, iItem, COL_THREAD_COUNT, Buffer); // �߳���

		// �汾
		lvItem.iItem = iItem ++ ;

	} while (Process32Next(hProcessSnap, &pe32));

	CloseHandle(hProcessSnap);

	
	// �Զ������п�ȡAUTOSIZE��ʽ��AUTOSIZE_USEHEADER�����Զ������п�ʽ�еĽϴ�ֵ��
	HWND hHeader = ListView_GetHeader(hList);
	int nColumnCount = Header_GetItemCount(hHeader);
	for(int iCol = 0; iCol < nColumnCount; iCol ++ )
	{
		ListView_SetColumnWidth( hList, iCol, LVSCW_AUTOSIZE );
		int nWidth1 = ListView_GetColumnWidth( hList, iCol );

		ListView_SetColumnWidth( hList, iCol, LVSCW_AUTOSIZE_USEHEADER );
		int nWidth2 = ListView_GetColumnWidth( hList, iCol );

		ListView_SetColumnWidth( hList, iCol, max(nWidth1, nWidth2) );
	}

	UnlockWindowUpdate();

	UpdateWindow();

	return TRUE;
}
