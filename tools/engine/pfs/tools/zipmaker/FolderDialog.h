#pragma once
#include <vector>
#include <string>

// 可以选择多个文件或目录的对话框

class CFolderDialog : public CFileDialog
{
	DECLARE_DYNAMIC(CFolderDialog)

public:
	static WNDPROC m_wndProc;
	virtual void OnInitDone( );
	CFolderDialog();
	~CFolderDialog();

	static std::vector<std::wstring> m_files;

protected:
	//{{AFX_MSG(CFolderDialog)
		// NOTE - the ClassWizard will add and remove member functions here.
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Developer Studio will insert additional declarations immediately before the previous line.

