
#include "stdafx.h"
#include "FolderDialog.h"
#include <DLGS.H>
#include <WINUSER.H>

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

IMPLEMENT_DYNAMIC(CFolderDialog, CFileDialog)

WNDPROC CFolderDialog::m_wndProc = NULL;
std::vector<std::wstring> CFolderDialog::m_files;

// Function name	: CFolderDialog::CFolderDialog
// Description	    : Constructor
// Return type		: 
// Argument         : CString* pPath ; represent string where selected folder wil be saved
CFolderDialog::CFolderDialog() : CFileDialog(TRUE, NULL, NULL, OFN_HIDEREADONLY|OFN_ALLOWMULTISELECT)
{
	const int size = 1024;
	m_ofn.lpstrFile = new TCHAR[size];			// 重新定义 lpstrFile 缓冲大小
	memset( m_ofn.lpstrFile, 0, size);			// 初始化定义的缓冲 
	m_ofn.nMaxFile = size;						// 重定义 nMaxFile 
}

CFolderDialog::~CFolderDialog()
{
	delete[] m_ofn.lpstrFile;
}

BEGIN_MESSAGE_MAP(CFolderDialog, CFileDialog)
	//{{AFX_MSG_MAP(CFolderDialog)
		// NOTE - the ClassWizard will add and remove mapping macros here.
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

// Function name	: WindowProcNew
// Description	    : Call this function when user navigate into CFileDialog.
// Return type		: LRESULT
// Argument         : HWND hwnd
// Argument         : UINT message
// Argument         : WPARAM wParam
// Argument         : LPARAM lParam
LRESULT CALLBACK WindowProcNew(HWND hwnd,UINT message, WPARAM wParam, LPARAM lParam)
{
	if( (message == WM_COMMAND && HIWORD(wParam) == BN_CLICKED && LOWORD(wParam) == IDOK))
	{
		CFileDialog* pDlg = (CFileDialog*)CWnd::FromHandle(hwnd);
		if( pDlg)
		{
			CWnd* p = pDlg->GetDlgItem( 0x461);
			if( p)
			{
				CListCtrl* pList = (CListCtrl*)( p->GetWindow( GW_CHILD));
				if( pList)
				{
					TCHAR path[MAX_PATH] = {0};
					GetCurrentDirectory( MAX_PATH, path);
					wcscat_s( path, MAX_PATH, L"\\");
					
					POSITION pos = pList->GetFirstSelectedItemPosition();
					while( pos)
					{
						CString file = path;
						file += pList->GetItemText( pList->GetNextSelectedItem( pos), 0);
						CFolderDialog::m_files.push_back( file.GetString());
					}
				}
			}
			pDlg->CFileDialog::EndDialog(IDOK);
			return NULL;
		}
	}
	return CallWindowProc(CFolderDialog::m_wndProc, hwnd, message, wParam, lParam);
}

// Function name	: CFolderDialog::OnInitDone
// Description	    : For update the wiew of CFileDialog
// Return type		: void 
void CFolderDialog::OnInitDone()
{
	//HideControl(edt1);
	//HideControl(stc3);
	//HideControl(cmb1);
	//HideControl(stc2);
	m_files.clear();
	CWnd* pFD = GetParent();
	//CRect rectCancel; 
	//pFD->GetDlgItem(IDCANCEL)->GetWindowRect(rectCancel);
	//pFD->ScreenToClient(rectCancel);
	//CRect rectOK; pFD->GetDlgItem(IDOK)->GetWindowRect(rectOK);
	//pFD->ScreenToClient(rectOK);
	//pFD->GetDlgItem(IDOK)->SetWindowPos(0,rectCancel.left - rectOK.Width() - 4, rectCancel.top, 0,0, SWP_NOZORDER | SWP_NOSIZE);
	//CRect rectList2; 
	//pFD->GetDlgItem(lst1)->GetWindowRect(rectList2);
	//pFD->ScreenToClient(rectList2);
	//pFD->GetDlgItem(lst1)->SetWindowPos(0,0,0,rectList2.Width(), abs(rectList2.top - (rectCancel.top - 4)), SWP_NOMOVE | SWP_NOZORDER);
	//SetControlText(IDOK, _T("Select"));
	//pFD->SetWindowText(_T("Choose folder"));
	m_wndProc = (WNDPROC)SetWindowLong(pFD->m_hWnd, GWL_WNDPROC, (long)WindowProcNew);
}
