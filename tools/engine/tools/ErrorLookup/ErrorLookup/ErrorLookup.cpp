// ErrorLookup.cpp : Defines the entry point for the application.
//

#include "stdafx.h"
#include "ErrorLookup.h"
#include "ErrorInfo.h"
#include "PFSErrorInfo.h"
#include "PPErrorInfo.h"

// Global Variables:
HINSTANCE g_hInstance;	// current instance

// Forward declarations of functions included in this code module:
INT_PTR CALLBACK	ErrorLookupProc(HWND, UINT, WPARAM, LPARAM);

int APIENTRY _tWinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPTSTR    lpCmdLine,
                     int       nCmdShow)
{
	UNREFERENCED_PARAMETER(hPrevInstance);
	UNREFERENCED_PARAMETER(lpCmdLine);

	// Perform application initialization:
	g_hInstance = hInstance;
	
	DialogBox(hInstance, MAKEINTRESOURCE(IDD_ERROR_LOOKUP), NULL, ErrorLookupProc);

	return TRUE;
}

WNDPROC oldProc = NULL;

// Message handler for IDC_ERROR_CODE control.
INT_PTR CALLBACK ErrorCodeProc(HWND hEdit, UINT message, WPARAM wParam, LPARAM lParam)
{
	// TODO: 
	// 完善此程序，争取做到支持16进制数的解析。
	// 完善此程序，做到Paste方式无法粘贴非法字符。
	switch (message)
	{
	case WM_CHAR:
		{
			if( wParam == L'-' || 
				::iswdigit( (wint_t) wParam ) ||
				::iswcntrl( (wint_t) wParam ) )
			{
				return oldProc( hEdit, message, wParam, lParam ); 
			}
			else
			{
				::MessageBeep(MB_ICONERROR);
				return TRUE;
			}
		}
	}
	return oldProc( hEdit, message, wParam, lParam ); 
}

// Message handler for IDD_ERROR_LOOKUP dialog.
INT_PTR CALLBACK ErrorLookupProc(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	UNREFERENCED_PARAMETER(lParam);
	switch (message)
	{
	case WM_INITDIALOG:
		{
			// 设置对话框图标
			HICON hIconLg = LoadIcon(g_hInstance, MAKEINTRESOURCE(IDI_ERRORLOOKUP));
			HICON hIconSm = LoadIcon(g_hInstance, MAKEINTRESOURCE(IDI_SMALL));
			::SendMessageW( hDlg, WM_SETICON, TRUE, (LPARAM)hIconLg );
			::SendMessageW( hDlg, WM_SETICON, FALSE, (LPARAM)hIconSm );

			// 子类化错误码编辑框控件，简单过滤非法字符。
			HWND hEdit = ::GetDlgItem( hDlg, IDC_ERROR_CODE );
			if( hEdit != NULL )
			{
				oldProc = (WNDPROC)(LONG_PTR)::SetWindowLong( hEdit, GWL_WNDPROC, (LONG)(LONG_PTR)ErrorCodeProc );
			}
			return (INT_PTR)TRUE;
		}
	case WM_COMMAND:
		{
			// 执行查询
			if( LOWORD(wParam) == IDOK )
			{
				BOOL bTranslated = FALSE;
				INT nErrorCodeValue = (INT)::GetDlgItemInt( hDlg, IDC_ERROR_CODE, &bTranslated, TRUE );
				WCHAR szErrorDescription[512] = {0};
				BOOL bNeedFree = FALSE;
				LPWSTR pszErrorDescription = szErrorDescription;
				
				LANGID langId = GetUserDefaultUILanguage();
				int code = 0;
				// 是否作为乐动卓越保护错误码(PP错误码)
				UINT checked = IsDlgButtonChecked(hDlg, IDC_CHECK_PP_CODE );
				if( !checked )
				{
					if ( nErrorCodeValue < 0 && nErrorCodeValue >= -4000 )
					{
						// -1 ~ -4000 PFS 错误码
						code = PFS::GetErrorDescription( langId, nErrorCodeValue, 
							pszErrorDescription, ARRAYSIZE(szErrorDescription) );
						if( code < 0 )
						{
							pszErrorDescription = (LPWSTR)::LocalAlloc( LPTR, 2*(1-code) );
							bNeedFree = TRUE;
							code = PFS::GetErrorDescription( langId, (int)nErrorCodeValue, 
								(LPWSTR)pszErrorDescription, (DWORD)1-code );
						}
					}
					else
					{
						// Windows API错误码
						code = EC::GetErrorDescription( langId, (DWORD)nErrorCodeValue,
							pszErrorDescription, ARRAYSIZE(szErrorDescription) );
						if( code < 0 )
						{
							pszErrorDescription = (LPWSTR)::LocalAlloc( LPTR, 2*(1-code) );
							bNeedFree = TRUE;
							code = EC::GetErrorDescription( langId, (int)nErrorCodeValue, 
								(LPWSTR)pszErrorDescription, (DWORD)1-code );
						}
					}
				}
				else
				{
					// PP错误码
					code = PP::GetErrorDescription(langId, nErrorCodeValue, 
						pszErrorDescription, ARRAYSIZE(szErrorDescription) );
					if( code < 0 )
					{
						pszErrorDescription = (LPWSTR)::LocalAlloc( LPTR, 2*(1-code) );
						bNeedFree = TRUE;
						code = PP::GetErrorDescription( langId, (int)nErrorCodeValue, 
							(LPWSTR)pszErrorDescription, (DWORD)1-code );
					}
				}
				
				if( code <= 0 )
				{
					if( bNeedFree )
					{
						::LocalFree( pszErrorDescription );
						bNeedFree = FALSE;
					}

					// 处理无效错误码的显示问题
					if( PRIMARYLANGID(langId) == LANG_CHINESE )
					{
						wcscpy_s( szErrorDescription, 
							ARRAYSIZE(szErrorDescription), L"<无效的错误码>" );
					}
					else
					{
						wcscpy_s( szErrorDescription,
							ARRAYSIZE(szErrorDescription), L"<Invalid error code>" );
					}

					pszErrorDescription = szErrorDescription;
				}

				// 显示错误消息
				::SetDlgItemTextW( hDlg, IDC_ERROR_DESCRIPTION, pszErrorDescription );
				if( bNeedFree )
				{
					::LocalFree( pszErrorDescription );
					bNeedFree = FALSE;
				}
				return TRUE;
			}
			else if ( LOWORD(wParam) == IDCANCEL)
			{
				// 关闭对话框
				EndDialog(hDlg, LOWORD(wParam));
				return (INT_PTR)TRUE;
			}
		}
		break;
	case WM_DESTROY:
		{
			// 还原子类化窗口控件。
			HWND hEdit = ::GetDlgItem( hDlg, IDC_ERROR_CODE );
			if( hEdit != NULL )
				::SetWindowLong( hEdit, GWL_WNDPROC,(LONG)(LONG_PTR)oldProc );
			break;
		}
	}
	return (INT_PTR)FALSE;
}
