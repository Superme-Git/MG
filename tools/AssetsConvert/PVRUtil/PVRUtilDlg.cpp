// PVRUtilDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "PVRUtil.h"
#include "PVRUtilDlg.h"
#include "control.h"
control g_control;

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define MAX_NAME_SIZE 100

// 用于应用程序“关于”菜单项的 CAboutDlg 对话框

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// 对话框数据
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

// 实现
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()


// CPVRUtilDlg 对话框




CPVRUtilDlg::CPVRUtilDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CPVRUtilDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CPVRUtilDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_STATIC_STATUS, m_progress);
	DDX_Control(pDX, IDC_EDIT2, m_message);
	DDX_Control(pDX, IDC_MIPMAP_TREE, m_Tree);

}

BEGIN_MESSAGE_MAP(CPVRUtilDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDOK, &CPVRUtilDlg::OnBnClickedOk)
	ON_WM_TIMER()
	ON_BN_CLICKED(IDC_CHECK_TO_PVR, &CPVRUtilDlg::OnBnClickedCheckToPvr)
	ON_BN_CLICKED(IDC_CHECK_TO_ATITC, &CPVRUtilDlg::OnBnClickedCheckToAtitc)
	ON_BN_CLICKED(IDC_CHECK_TO_DXT1, &CPVRUtilDlg::OnBnClickedCheckToDxt1)
	ON_BN_CLICKED(IDC_CHECK_TO_ETC1, &CPVRUtilDlg::OnBnClickedCheckToEtc1)
	ON_BN_CLICKED(ID_SELECT_INPUTDIR, &CPVRUtilDlg::OnBnClickedSelectInputdir)
	ON_BN_CLICKED(ID_SELECT_OUTPUTDIR, &CPVRUtilDlg::OnBnClickedSelectOutputdir)
	ON_BN_CLICKED(ID_PROCESSING, &CPVRUtilDlg::OnBnClickedProcessing)
	ON_BN_CLICKED(ID_CLEAR_LOG, &CPVRUtilDlg::OnBnClickedClearLog)
	ON_BN_CLICKED(IDC_CHECK_TO_RGBA8AA, &CPVRUtilDlg::OnBnClickedCheckToRgba8aa)
	ON_BN_CLICKED(IDC_CHECK_TO_RGBA8AAS, &CPVRUtilDlg::OnBnClickedCheckToRgba8aas)
	ON_BN_CLICKED(IDC_CHECK_MERGE_AS, &CPVRUtilDlg::OnBnClickedCheckMergeAs)
	ON_BN_CLICKED(IDC_CHECK_TO_QUALITY_FAST, &CPVRUtilDlg::OnBnClickedCheckToQualityFast)
	ON_BN_CLICKED(IDC_CHECK_TO_QUALITY_NORMAL, &CPVRUtilDlg::OnBnClickedCheckToQualityNormal)
	ON_BN_CLICKED(IDC_CHECK_TO_QUALITY_HIGH, &CPVRUtilDlg::OnBnClickedCheckToQualityHigh)
	ON_BN_CLICKED(IDC_CHECK_TO_QUALITY_BEST, &CPVRUtilDlg::OnBnClickedCheckToQualityBest)
	ON_BN_CLICKED(IDC_CHECK_TO_DXT1_ALPHA, &CPVRUtilDlg::OnBnClickedCheckToDxt1Alpha)
	ON_BN_CLICKED(IDC_CHECK_TO_ATITC_RGB, &CPVRUtilDlg::OnBnClickedCheckToAtitcRgb)
	ON_BN_CLICKED(IDC_CHECK_TO_ATITC_RGBA_I, &CPVRUtilDlg::OnBnClickedCheckToAtitcRgbaI)
	ON_BN_CLICKED(IDC_CHECK_TO_ATITC_RGBA_E, &CPVRUtilDlg::OnBnClickedCheckToAtitcRgbaE)
	ON_BN_CLICKED(IDCANCEL, &CPVRUtilDlg::OnBnClickedCancel)
	ON_BN_CLICKED(IDC_CHECK_OUTPUT_EXT_ORIG, &CPVRUtilDlg::OnBnClickedCheckOutputExtOrig)
	ON_BN_CLICKED(IDC_CHECK_OUTPUT_EXT_PVR, &CPVRUtilDlg::OnBnClickedCheckOutputExtPvr)
	ON_EN_CHANGE(IDC_INPUTDIR, &CPVRUtilDlg::OnEnChangeInputdir)
	ON_EN_CHANGE(IDC_OUTPUTDIR, &CPVRUtilDlg::OnEnChangeOutputdir)
	ON_BN_CLICKED(IDC_CHECK_CONVERT_TO_PVR, &CPVRUtilDlg::OnBnClickedCheckConvertToPvr)
	ON_BN_CLICKED(IDC_CHECK_CONVERT_TO_ATITC, &CPVRUtilDlg::OnBnClickedCheckConvertToAtitc)
	ON_BN_CLICKED(IDC_CHECK_CONVERT_TO_DXT1, &CPVRUtilDlg::OnBnClickedCheckConvertToDxt1)
	ON_BN_CLICKED(IDC_CHECK_CONVERT_TO_ETC1, &CPVRUtilDlg::OnBnClickedCheckConvertToEtc1)
	ON_BN_CLICKED(ID_VIEW_PVR, &CPVRUtilDlg::OnBnClickedViewPvr)
	ON_BN_CLICKED(ID_VIEW_ATITC, &CPVRUtilDlg::OnBnClickedViewAtitc)
	ON_BN_CLICKED(ID_VIEW_DXT1, &CPVRUtilDlg::OnBnClickedViewDxt1)
	ON_BN_CLICKED(ID_VIEW_ETC1, &CPVRUtilDlg::OnBnClickedViewEtc1)
	ON_BN_CLICKED(ID_SAVE_MIPMAP_CHECK, &CPVRUtilDlg::OnBnClickedSaveMipmapCheck)
	ON_NOTIFY(NM_CLICK, IDC_MIPMAP_TREE, &CPVRUtilDlg::OnNMClickMipMapTree)
	ON_BN_CLICKED(IDC_CHECK_RESIZE,&CPVRUtilDlg::OnBnClickedResizeTexture)
END_MESSAGE_MAP()


// CPVRUtilDlg 消息处理程序

BOOL CPVRUtilDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// 将“关于...”菜单项添加到系统菜单中。

	// IDM_ABOUTBOX 必须在系统命令范围内。
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	m_Tree.SetBkColor(RGB(255, 255, 255));
	m_Tree.ModifyStyle(0, TVS_LINESATROOT);

	SetIcon(m_hIcon, TRUE);			// SET BIG ICON
	SetIcon(m_hIcon, FALSE);		// SET SMALL ICON

	SetTimer(99, 100, NULL); //wangbin edit old=1000
	m_progress.SetWindowText(L"");

	GetDlgItem(IDOK)->EnableWindow(FALSE);
	GetDlgItem(IDOK)->ShowWindow(FALSE);

	GetDlgItem(IDC_OUTPUTDIR)->ShowWindow(0);
	GetDlgItem(ID_SELECT_OUTPUTDIR)->ShowWindow(0);

	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->ShowWindow(1);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->ShowWindow(0);

	return TRUE;
}

std::string ws2s(const std::wstring& ws)
{
    std::string curLocale = setlocale(LC_ALL, NULL);        // curLocale = "C";
    setlocale(LC_ALL, "chs");
    const wchar_t* _Source = ws.c_str();
    size_t _Dsize = 2 * ws.size() + 1;
    char *_Dest = new char[_Dsize];
    memset(_Dest,0,_Dsize);
    wcstombs(_Dest,_Source,_Dsize);
    std::string result = _Dest;
    delete []_Dest;
    setlocale(LC_ALL, curLocale.c_str());
    return result;
}

std::wstring s2ws(const std::string& s)
{
    setlocale(LC_ALL, "chs"); 
    const char* _Source = s.c_str();
    size_t _Dsize = s.size() + 1;
    wchar_t *_Dest = new wchar_t[_Dsize];
    wmemset(_Dest, 0, _Dsize);
    mbstowcs(_Dest,_Source,_Dsize);
    std::wstring result = _Dest;
    delete []_Dest;
    setlocale(LC_ALL, "C");
    return result;
}

bool myCompare(const std::wstring ws,const char ch[])
{
	int i = 0;
	while(ch[i])
	{
		if(ws[i] != ch[i])
			return false;

	    ++i;
	}

	if((ch[i] == ws[i]) && !i)
	{
		return true;
	}

	return false;
}

void CPVRUtilDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

void CPVRUtilDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this);

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// 使图标在工作区矩形中居中
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// 绘制图标
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

//当用户拖动最小化窗口时系统调用此函数取得光标
//显示。
HCURSOR CPVRUtilDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

int msgindex = 0;
void CPVRUtilDlg::OnBnClickedOk()
{
}

void CPVRUtilDlg::OnTimer(UINT_PTR nIDEvent)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
//	this->SetWindowText(g_control.GetWindowText().c_str());
//	int count = g_control.GetMessageCount();
	CString wndTxt;
	m_message.GetWindowText(wndTxt);
	bool bChange = false;
	bool bPvrMsgChange = false, bATITCMsgChange = false, bDXT1CMsgChange = false, bETC1MsgChange = false;
	CString pvrMsgs;
	CString atitcMsgs;
	CString dxt1Msgs;
	CString etc1Msgs;
	if( g_control.m_convert2 & control::CT_PVR )
	{
		GetDlgItem(IDC_EDIT_PVR_MSG)->GetWindowText(pvrMsgs);
		std::wstring pvr_msg = g_control.FetchPvrFirstMessage();

		if( pvr_msg != L"" )
		{
			pvrMsgs += pvr_msg.c_str();
			pvrMsgs += L"\r\n";
			bPvrMsgChange = true;		
		}

	}

	if( g_control.m_convert2 & control::CT_ATITC )
	{
		GetDlgItem(IDC_EDIT_ATITC_MSG2)->GetWindowText(atitcMsgs);
		std::wstring atitc_msg = g_control.FetchATITCFirstMessage();

		if( atitc_msg != L"" )
		{
			atitcMsgs += atitc_msg.c_str();
			atitcMsgs += L"\r\n";
			bATITCMsgChange = true;		
		}

	}

	if( g_control.m_convert2 & control::CT_DXT1 )
	{
		GetDlgItem(IDC_EDIT_DXT1_MSG)->GetWindowText(dxt1Msgs);
		std::wstring dxt1_msg = g_control.FetchDXT1FirstMessage();

		if( dxt1_msg != L"" )
		{
			dxt1Msgs += dxt1_msg.c_str();
			dxt1Msgs += L"\r\n";
			bDXT1CMsgChange = true;		
		}

	}

	if( g_control.m_convert2 & control::CT_ETC1 )
	{
		GetDlgItem(IDC_EDIT_ETC1_MSG)->GetWindowText(etc1Msgs);
		std::wstring etc1_msg = g_control.FetchETC1FirstMessage();

		if( etc1_msg != L"" )
		{
			etc1Msgs += etc1_msg.c_str();
			etc1Msgs += L"\r\n";
			bETC1MsgChange = true;		
		}

	}

	if( bPvrMsgChange )
	{
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->SetWindowText( pvrMsgs );
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->LineScroll(static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->GetLineCount());
	}

	if( bATITCMsgChange )
	{
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->SetWindowText( atitcMsgs );
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->LineScroll(static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->GetLineCount());
	}

	if( bDXT1CMsgChange )
	{
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->SetWindowText( dxt1Msgs );
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->LineScroll(static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->GetLineCount());
	}

	if( bETC1MsgChange )
	{
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->SetWindowText( etc1Msgs );
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->LineScroll(static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->GetLineCount());
	}

	if( g_control.m_pvr_convert_status == control::ENone
		&& g_control.m_atitc_convert_status == control::ENone
		&& g_control.m_dxt1_convert_status == control::ENone
		&& g_control.m_etc1_convert_status == control::ENone
		/*&& g_control.GetMessageCount() == 0*/ )
	{
		GetDlgItem( ID_PROCESSING )->EnableWindow(TRUE);
		GetDlgItem( IDC_INPUTDIR )->EnableWindow(TRUE);
	}

	CDialog::OnTimer(nIDEvent);
}


void CPVRUtilDlg::OnBnClickedCheckToPvr()
{
	if( IsDlgButtonChecked(IDC_CHECK_TO_PVR) )
	{
		g_control.m_targetFormat = control::TF_PVR;

		CheckDlgButton(IDC_CHECK_TO_ATITC, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGB, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGBA_I, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGBA_E, false);

		CheckDlgButton(IDC_CHECK_TO_DXT1, false);
		CheckDlgButton( IDC_CHECK_TO_DXT1_ALPHA, FALSE );

		CheckDlgButton( IDC_CHECK_TO_ETC1, false );
		CheckDlgButton( IDC_CHECK_TO_RGBA8AAS, false );
		CheckDlgButton(IDC_CHECK_MERGE_AS, FALSE);
		CheckDlgButton(IDC_CHECK_TO_RGBA8AA, FALSE);

	}

	else
	{
		g_control.m_targetFormat = control::TF_None;
	}
}


void CPVRUtilDlg::OnBnClickedCheckToAtitc()
{
	if( IsDlgButtonChecked(IDC_CHECK_TO_ATITC) )
	{
		g_control.m_targetFormat = control::TF_ATITC;

		CheckDlgButton(IDC_CHECK_TO_PVR, false);
		CheckDlgButton( IDC_CHECK_TO_QUALITY_FAST, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_NORMAL, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_HIGH, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_BEST, FALSE );

		CheckDlgButton(IDC_CHECK_TO_DXT1, false);
		CheckDlgButton( IDC_CHECK_TO_DXT1_ALPHA, FALSE );

		CheckDlgButton( IDC_CHECK_TO_ETC1, false );
		CheckDlgButton( IDC_CHECK_TO_RGBA8AAS, false );
		CheckDlgButton(IDC_CHECK_MERGE_AS, FALSE);
		CheckDlgButton(IDC_CHECK_TO_RGBA8AA, FALSE);
	}

	else
	{
		g_control.m_targetFormat = control::TF_None;
	}
}


void CPVRUtilDlg::OnBnClickedCheckToDxt1()
{
	if( IsDlgButtonChecked(IDC_CHECK_TO_DXT1) )
	{
		g_control.m_targetFormat = control::TF_DXT1;

		CheckDlgButton(IDC_CHECK_TO_ATITC, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGB, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGBA_I, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGBA_E, false);

		CheckDlgButton(IDC_CHECK_TO_PVR, false);
		CheckDlgButton( IDC_CHECK_TO_QUALITY_FAST, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_NORMAL, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_HIGH, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_BEST, FALSE );

		CheckDlgButton( IDC_CHECK_TO_ETC1, false );
		CheckDlgButton( IDC_CHECK_TO_RGBA8AAS, false );
		CheckDlgButton(IDC_CHECK_MERGE_AS, FALSE);
		CheckDlgButton(IDC_CHECK_TO_RGBA8AA, FALSE);
	}

	else
	{
		g_control.m_targetFormat = control::TF_None;
	}
}


void CPVRUtilDlg::OnBnClickedCheckToEtc1()
{
	if( IsDlgButtonChecked(IDC_CHECK_TO_ETC1) )
	{
		g_control.m_targetFormat = control::TF_ETC1_RGB8;

		CheckDlgButton( IDC_CHECK_TO_RGBA8AAS, false );
		CheckDlgButton(IDC_CHECK_MERGE_AS, FALSE);
		CheckDlgButton(IDC_CHECK_TO_RGBA8AA, FALSE);

		CheckDlgButton(IDC_CHECK_TO_ATITC, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGB, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGBA_I, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGBA_E, false);

		CheckDlgButton(IDC_CHECK_TO_PVR, false);
		CheckDlgButton( IDC_CHECK_TO_QUALITY_FAST, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_NORMAL, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_HIGH, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_BEST, FALSE );

		CheckDlgButton(IDC_CHECK_TO_DXT1, false);
		CheckDlgButton( IDC_CHECK_TO_DXT1_ALPHA, FALSE );

	}

	else
	{
		g_control.m_targetFormat = control::TF_None;
	}
}

void CPVRUtilDlg::OnBnClickedSelectInputdir()
{
	BROWSEINFO bi;
	wchar_t Buffer[MAX_PATH];

	bi.hwndOwner = NULL;
	bi.pidlRoot = NULL;
	bi.pszDisplayName = Buffer;//此参数如为NULL则不能显示对话框
	bi.lpszTitle = L"选择路径";
	bi.ulFlags = BIF_RETURNONLYFSDIRS;
	bi.lpfn = NULL;
	bi.iImage = 0;

	LPITEMIDLIST pIDList = SHBrowseForFolder(&bi);//调用显示选择对话框
	if(pIDList)
	{
		SHGetPathFromIDList(pIDList, Buffer);
		//取得文件夹路径到Buffer里
		g_control.m_inputDir = Buffer;//将路径保存在一个CString对象里
	}

	SetDlgItemText( IDC_INPUTDIR, CString(g_control.m_inputDir.c_str()) );

	//OnCreateMipmapTree();
}


void CPVRUtilDlg::OnBnClickedSelectOutputdir()
{
	BROWSEINFO bi;
	wchar_t Buffer[MAX_PATH];

	bi.hwndOwner = NULL;
	bi.pidlRoot = NULL;
	bi.pszDisplayName = Buffer;
	bi.lpszTitle = L"选择路径";
	bi.ulFlags = BIF_RETURNONLYFSDIRS;
	bi.lpfn = NULL;
	bi.iImage = 0;

	LPITEMIDLIST pIDList = SHBrowseForFolder(&bi);
	if(pIDList)
	{
		SHGetPathFromIDList(pIDList, Buffer);
		g_control.m_outputDir = Buffer;
	}

	SetDlgItemText( IDC_OUTPUTDIR, CString(g_control.m_outputDir.c_str()) );
}


void CPVRUtilDlg::OnBnClickedProcessing()
{
	if( g_control.m_inputDir == L"" )
	{
		MessageBox(L"Select Input dir", L"Info");
		return ;
	}

	if( g_control.m_convert2 & control::CT_PVR == 0
		&& g_control.m_convert2 & control::CT_ATITC == 0 
		&& g_control.m_convert2 & control::CT_DXT1 == 0
		&& g_control.m_convert2 & control::CT_ETC1 == 0 )
	{
		MessageBox(L"convert to", L"Info");
		return ;
	}

	GetDlgItem( ID_PROCESSING )->EnableWindow(FALSE);
	GetDlgItem( IDC_INPUTDIR )->EnableWindow(FALSE);
//	GetDlgItem( IDC_OUTPUTDIR )->EnableWindow(FALSE);

	g_control.m_terminate_converting = false;

/*
	if( g_control.m_convert2 & control::CT_PVR )
	{
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->SetWindowText( L"Preparing pvr Assets ..." );
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->LineScroll(static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->GetLineCount());
	}

	if( g_control.m_convert2 & control::CT_ATITC )
	{
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->SetWindowText( L"Preparing atitc Assets ..." );
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->LineScroll(static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->GetLineCount());
	}

	if( g_control.m_convert2 & control::CT_DXT1 )
	{
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->SetWindowText( L"Preparing dxt1 Assets ..." );
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->LineScroll(static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->GetLineCount());
	}

	if( g_control.m_convert2 & control::CT_ETC1 )
	{
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->SetWindowText( L"Preparing etc1 Assets ..." );
		static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->LineScroll(static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->GetLineCount());
	}*/

	g_control.begin_convert();
	
	return ;
}


void CPVRUtilDlg::OnBnClickedClearLog()
{
	GetDlgItem(IDC_EDIT2)->SetWindowText(L"");

	if( GetDlgItem(IDC_EDIT_PVR_MSG)->IsWindowVisible() )
	{
		GetDlgItem(IDC_EDIT_PVR_MSG)->SetWindowText( L"" );
	}

	else if( GetDlgItem(IDC_EDIT_ATITC_MSG2)->IsWindowVisible() )
	{
		GetDlgItem(IDC_EDIT_ATITC_MSG2)->SetWindowText( L"" );
	}

	else if( GetDlgItem(IDC_EDIT_DXT1_MSG)->IsWindowVisible() )
	{
		GetDlgItem(IDC_EDIT_DXT1_MSG)->SetWindowText( L"" );
	}

	else if( GetDlgItem(IDC_EDIT_ETC1_MSG)->IsWindowVisible() )
	{
		GetDlgItem(IDC_EDIT_ETC1_MSG)->SetWindowText( L"" );
	}
}


void CPVRUtilDlg::OnBnClickedCheckToRgba8aa()
{
	if( IsDlgButtonChecked(IDC_CHECK_TO_RGBA8AA) )
	{
		g_control.m_targetFormat = control::TF_ETC1_RGBA8_AA;

		CheckDlgButton(IDC_CHECK_TO_ATITC, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGB, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGBA_I, false);
		CheckDlgButton(IDC_CHECK_TO_ATITC_RGBA_E, false);

		CheckDlgButton(IDC_CHECK_TO_PVR, false);
		CheckDlgButton( IDC_CHECK_TO_QUALITY_FAST, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_NORMAL, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_HIGH, FALSE );
		CheckDlgButton( IDC_CHECK_TO_QUALITY_BEST, FALSE );

		CheckDlgButton(IDC_CHECK_TO_DXT1, false);
		CheckDlgButton( IDC_CHECK_TO_DXT1_ALPHA, FALSE );

		CheckDlgButton( IDC_CHECK_TO_ETC1, false );
		CheckDlgButton( IDC_CHECK_TO_RGBA8AAS, false );
		CheckDlgButton(IDC_CHECK_MERGE_AS, FALSE);
	}

	else
	{
		g_control.m_targetFormat = control::TF_None;
	}	
}


void CPVRUtilDlg::OnBnClickedCheckToRgba8aas()
{
	if( IsDlgButtonChecked(IDC_CHECK_TO_RGBA8AAS) )
	{
		g_control.m_targetFormat = control::TF_ETC1_RGBA8_AS;

		CheckDlgButton(IDC_CHECK_TO_ATITC, false);
		CheckDlgButton(IDC_CHECK_TO_PVR, false);
		CheckDlgButton(IDC_CHECK_TO_DXT1, false);
		CheckDlgButton( IDC_CHECK_TO_ETC1, false );
		CheckDlgButton( IDC_CHECK_TO_RGBA8AA, false );
	}

	else
	{
		g_control.m_targetFormat = control::TF_None;
	}	
}


void CPVRUtilDlg::OnBnClickedCheckMergeAs()
{
	g_control.m_mergeEtc = (bool)IsDlgButtonChecked(IDC_CHECK_MERGE_AS);
}


void CPVRUtilDlg::OnBnClickedCheckToQualityFast()
{
	g_control.m_bPvrQualityFast = (bool)IsDlgButtonChecked(IDC_CHECK_TO_QUALITY_FAST);

	if( g_control.m_bPvrQualityFast )
	{
		g_control.m_bPvrQualityNormal = false;
		CheckDlgButton(IDC_CHECK_TO_QUALITY_NORMAL, false);

		g_control.m_bPvrQualityBest = false;
		CheckDlgButton( IDC_CHECK_TO_QUALITY_BEST, false );

		g_control.m_bPvrQualityHigh = false;
		CheckDlgButton( IDC_CHECK_TO_QUALITY_HIGH, false );
	}
}


void CPVRUtilDlg::OnBnClickedCheckToQualityNormal()
{
	g_control.m_bPvrQualityNormal = (bool)IsDlgButtonChecked(IDC_CHECK_TO_QUALITY_NORMAL);

	if( g_control.m_bPvrQualityNormal )
	{
		g_control.m_bPvrQualityFast = false;
		CheckDlgButton(IDC_CHECK_TO_QUALITY_FAST, false);

		g_control.m_bPvrQualityBest = false;
		CheckDlgButton( IDC_CHECK_TO_QUALITY_BEST, false );

		g_control.m_bPvrQualityHigh = false;
		CheckDlgButton( IDC_CHECK_TO_QUALITY_HIGH, false );
	}
}


void CPVRUtilDlg::OnBnClickedCheckToQualityHigh()
{
	g_control.m_bPvrQualityHigh = (bool)IsDlgButtonChecked(IDC_CHECK_TO_QUALITY_HIGH);

	if( g_control.m_bPvrQualityHigh )
	{
		g_control.m_bPvrQualityFast = false;
		CheckDlgButton(IDC_CHECK_TO_QUALITY_FAST, false);

		g_control.m_bPvrQualityBest = false;
		CheckDlgButton( IDC_CHECK_TO_QUALITY_BEST, false );

		g_control.m_bPvrQualityNormal = false;
		CheckDlgButton( IDC_CHECK_TO_QUALITY_NORMAL, false );
	}
}


void CPVRUtilDlg::OnBnClickedCheckToQualityBest()
{
	g_control.m_bPvrQualityBest = (bool)IsDlgButtonChecked(IDC_CHECK_TO_QUALITY_BEST);

	if( g_control.m_bPvrQualityBest )
	{
		g_control.m_bPvrQualityFast = false;
		CheckDlgButton(IDC_CHECK_TO_QUALITY_FAST, false);

		g_control.m_bPvrQualityHigh = false;
		CheckDlgButton( IDC_CHECK_TO_QUALITY_HIGH, false );

		g_control.m_bPvrQualityNormal = false;
		CheckDlgButton( IDC_CHECK_TO_QUALITY_NORMAL, false );
	}
}


void CPVRUtilDlg::OnBnClickedCheckToDxt1Alpha()
{
	g_control.m_dxt1Alpha = (bool)IsDlgButtonChecked(IDC_CHECK_TO_DXT1_ALPHA);	
}


void CPVRUtilDlg::OnBnClickedCheckToAtitcRgb()
{
	g_control.m_atitc_rgb = (bool)IsDlgButtonChecked(IDC_CHECK_TO_ATITC_RGB);

	if( g_control.m_atitc_rgb )
	{
		g_control.m_atitc_rgba_explicit = false;
		CheckDlgButton( IDC_CHECK_TO_ATITC_RGBA_E, false );

		g_control.m_atitc_rgba_interpolated= false;
		CheckDlgButton( IDC_CHECK_TO_ATITC_RGBA_I, false );
	}
}


void CPVRUtilDlg::OnBnClickedCheckToAtitcRgbaI()
{
	g_control.m_atitc_rgba_interpolated = (bool)IsDlgButtonChecked(IDC_CHECK_TO_ATITC_RGBA_I);

	if( g_control.m_atitc_rgba_interpolated )
	{
		g_control.m_atitc_rgb = false;
		CheckDlgButton( IDC_CHECK_TO_ATITC_RGB, false );

		g_control.m_atitc_rgba_explicit= false;
		CheckDlgButton( IDC_CHECK_TO_ATITC_RGBA_E, false );
	}
}


void CPVRUtilDlg::OnBnClickedCheckToAtitcRgbaE()
{
	g_control.m_atitc_rgba_explicit = (bool)IsDlgButtonChecked(IDC_CHECK_TO_ATITC_RGBA_E);

	if( g_control.m_atitc_rgba_explicit )
	{
		g_control.m_atitc_rgb = false;
		CheckDlgButton( IDC_CHECK_TO_ATITC_RGB, false );

		g_control.m_atitc_rgba_interpolated = false;
		CheckDlgButton( IDC_CHECK_TO_ATITC_RGBA_I, false );
	}
}


void CPVRUtilDlg::OnBnClickedCancel()
{

	if ( g_control.m_pvr_convert_status == control::EProcessing 
		|| g_control.m_atitc_convert_status == control::EProcessing 
		|| g_control.m_dxt1_convert_status == control::EProcessing 
		|| g_control.m_etc1_convert_status == control::EProcessing )
	{
		MessageBox(L"正在处理", L"info");
		return;
	}

	CDialog::OnCancel();
}


void CPVRUtilDlg::OnBnClickedCheckOutputExtOrig()
{
	// TODO: 在此添加控件通知处理程序代码
	if((bool)IsDlgButtonChecked(IDC_CHECK_OUTPUT_EXT_PVR))
	{
		g_control.m_output_ext = control::OE_PVR;
	}
	
	else
	{
		g_control.m_output_ext = control::OE_ORIG;
	}
}


void CPVRUtilDlg::OnBnClickedCheckOutputExtPvr()
{
	// TODO: 在此添加控件通知处理程序代码
	if((bool)IsDlgButtonChecked(IDC_CHECK_OUTPUT_EXT_ORIG))
	{
		g_control.m_output_ext = control::OE_ORIG;
	}

}


void CPVRUtilDlg::OnEnChangeInputdir()
{
	CString input_dir;
	GetDlgItemText(IDC_INPUTDIR, input_dir );
	g_control.m_inputDir = input_dir.GetBuffer();
	OnCreateMipmapTree();
}


void CPVRUtilDlg::OnEnChangeOutputdir()
{
	CString output_dir;
	GetDlgItemText(IDC_OUTPUTDIR, output_dir);

	g_control.m_outputDir = output_dir.GetBuffer();
}


void CPVRUtilDlg::OnBnClickedCheckConvertToPvr()
{
	if((bool)IsDlgButtonChecked(IDC_CHECK_CONVERT_TO_PVR))
	{
		g_control.m_convert2 |= control::CT_PVR;
	}
	else
	{
		g_control.m_convert2 &= ~control::CT_PVR;
	}
}


void CPVRUtilDlg::OnBnClickedCheckConvertToAtitc()
{
	if((bool)IsDlgButtonChecked(IDC_CHECK_CONVERT_TO_ATITC))
	{
		g_control.m_convert2 |= control::CT_ATITC;
	}
	else
	{
		g_control.m_convert2 &= ~control::CT_ATITC;
	}
}


void CPVRUtilDlg::OnBnClickedCheckConvertToDxt1()
{
	if((bool)IsDlgButtonChecked(IDC_CHECK_CONVERT_TO_DXT1))
	{
		g_control.m_convert2 |= control::CT_DXT1;
	}
	else
	{
		g_control.m_convert2 &= ~control::CT_DXT1;
	}
}


void CPVRUtilDlg::OnBnClickedCheckConvertToEtc1()
{
	if((bool)IsDlgButtonChecked(IDC_CHECK_CONVERT_TO_ETC1))
	{
		g_control.m_convert2 |= control::CT_ETC1;
		GetDlgItem( IDC_EDIT_PVR_MSG )->ShowWindow(1);

	}
	else
	{
		g_control.m_convert2 &= ~control::CT_ETC1;
			GetDlgItem( IDC_EDIT_PVR_MSG )->ShowWindow(0);
	}
}


void CPVRUtilDlg::OnBnClickedViewPvr()
{
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->ShowWindow(1);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->ShowWindow(0);
}


void CPVRUtilDlg::OnBnClickedViewAtitc()
{
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->ShowWindow(1);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->ShowWindow(0);
}


void CPVRUtilDlg::OnBnClickedViewDxt1()
{
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->ShowWindow(1);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->ShowWindow(0);
}


void CPVRUtilDlg::OnBnClickedViewEtc1()
{
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_PVR_MSG))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ATITC_MSG2))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_DXT1_MSG))->ShowWindow(0);
	static_cast<CEdit*>(GetDlgItem(IDC_EDIT_ETC1_MSG))->ShowWindow(1);
}

void CPVRUtilDlg::OnCreateMipmapTree()
{
	if(g_control.m_inputDir.empty())
		return;

	m_Tree.DeleteAllItems();
	dir in_dir(g_control.m_inputDir);
	std::vector<std::wstring> filters;
	filters.push_back(L"png");
	filters.push_back(L"jpg");
	filters.push_back(L"dds");
	filters.push_back(L"tga");
	filters.push_back(L"bmp");

	std::vector<dir::item*>::iterator it = g_control.m_in_dir_items.begin();
	for(;it != g_control.m_in_dir_items.end();++it)
	{
		dir::item* temp = *it;
		delete temp;
	}
	g_control.m_in_dir_items.clear();

	in_dir.CreateMipMapTree(g_control.m_in_dir_items,m_Tree,TVI_ROOT,filters);
	OnChangeMipmapTreeCheckFromLastSave();
	if(m_Tree.GetRootItem())
		OnRefleshTreeNodeChecking(m_Tree.GetRootItem());
}

void CPVRUtilDlg::OnNMClickMipMapTree(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	*pResult = 0;

	CPoint pt;
	UINT uFlag;
	HTREEITEM hCurrentItem;
	GetCursorPos(&pt);
	m_Tree.ScreenToClient(&pt);
	hCurrentItem = m_Tree.HitTest(pt, &uFlag);
	if(uFlag & TVHT_ONITEMSTATEICON)
	{
		// 点击了Check框
		m_Tree.SelectItem(hCurrentItem);

		OnTreeNodeChecking(hCurrentItem);
	}
}

void CPVRUtilDlg::OnTreeNodeChecking( HTREEITEM hItem )
{
	BOOL Checked = m_Tree.GetCheck(hItem);
	dir::item* pItemData = reinterpret_cast<dir::item*>(m_Tree.GetItemData(hItem));
	if(pItemData)
	{
		pItemData->isCreateMipMap = !(bool)Checked;
	}
	
	// 勾选所有子节点控制
	if (m_Tree.ItemHasChildren(hItem))
	{
		if(Checked == TRUE)
		{
			OnChildrenNodeCheck(hItem,FALSE);
		}
		else
		{
			OnChildrenNodeCheck(hItem,TRUE);
		}
	}
	g_control.m_in_dir_items;
}

void CPVRUtilDlg::OnChildrenNodeCheck( HTREEITEM hItem,BOOL check )
{
	HTREEITEM hChildItem = m_Tree.GetChildItem(hItem);
	
	while(hChildItem)
	{
		m_Tree.SetCheck(hChildItem, check);
		dir::item* pItemData = reinterpret_cast<dir::item*>(m_Tree.GetItemData(hChildItem));
		if(pItemData)
		{
			pItemData->isCreateMipMap = (bool)check;
		}
		OnChildrenNodeCheck(hChildItem,check);
		hChildItem = m_Tree.GetNextItem(hChildItem, TVGN_NEXT);
	}
}

void CPVRUtilDlg::OnBnClickedSaveMipmapCheck()
{
	if(g_control.m_inputDir.empty())
	{
		MessageBox(_T("请先打开一个目录...."));
		return;
	}

	FILE *pFile = NULL;
	std::string pName;
	for(size_t i = 0; i < g_control.m_inputDir.size();++i)
	{
		if(g_control.m_inputDir[i] == ':')
		{
			pName += '#';
		}
		else if(g_control.m_inputDir[i] == '\\')
		{
			pName += '$';
		}
		else
		{
			pName += g_control.m_inputDir[i];
		}	
	}

	fopen_s(&pFile,pName.c_str(), "wb");

	for(int a = 0;a < g_control.m_in_dir_items.size(); ++a)
	{
		if(g_control.m_in_dir_items[a]->isCreateMipMap)
		{
			std::string str = ws2s(g_control.m_in_dir_items[a]->fileName);
			fprintf(pFile, "%s\r\n",str.c_str());
		}
	}

	fclose(pFile);
	MessageBox(_T("保存成功！"));
}

std::wstring MyCharToString(char ch[])
{
	std::wstring temp;
	int i = 0;
	while(ch[i] != L'\n')
	{
		temp += ch[i++];
	}
	return temp;
}

void CPVRUtilDlg::OnChangeMipmapTreeCheckFromLastSave()
{
	FILE *pFile = NULL;
	std::string pName;
	for(size_t i = 0; i < g_control.m_inputDir.size();++i)
	{
		if(g_control.m_inputDir[i] == ':')
		{
			pName += '#';
		}
		else if(g_control.m_inputDir[i] == '\\')
		{
			pName += '$';
		}
		else
		{
			pName += g_control.m_inputDir[i];
		}	
	}

	if(!fopen_s(&pFile,pName.c_str(), "r"))
	{
		char str[MAX_NAME_SIZE];
		size_t Itsize = g_control.m_in_dir_items.size();
		std::wstring ss,ss1;
		const wchar_t* ss2;
		while(fgets(str,MAX_NAME_SIZE,pFile))
		{	
			for(size_t j = 0; j < Itsize; j++)
			{
				ss = MyCharToString(str);
			    ss1 = g_control.m_in_dir_items[j]->fileName;
				if(ss == ss1)
				{
					g_control.m_in_dir_items[j]->isCreateMipMap = true;
					break;
				}
			}
		}
		fclose(pFile);
	}
	
}

void CPVRUtilDlg::OnRefleshTreeNodeChecking( HTREEITEM hItem )
{
	dir::item* pItemData = reinterpret_cast<dir::item*>(m_Tree.GetItemData(hItem));
	if(pItemData && pItemData->isCreateMipMap)
	{
		m_Tree.SetCheck(hItem,TRUE);
		HTREEITEM hParentItem = m_Tree.GetParentItem(hItem);
		while(hParentItem)
		{
			m_Tree.SetCheck(hParentItem,TRUE);
			hParentItem = m_Tree.GetParentItem(hParentItem);
		}	
	}

	HTREEITEM hChildItem = m_Tree.GetChildItem(hItem);
	if(hChildItem)
	{
		OnRefleshTreeNodeChecking(hChildItem);
	}

	hChildItem = m_Tree.GetNextItem(hItem, TVGN_NEXT);
	if(hChildItem)
	{
		OnRefleshTreeNodeChecking(hChildItem);
	}
}

void CPVRUtilDlg::OnBnClickedResizeTexture()
{
	if((bool)IsDlgButtonChecked(IDC_CHECK_RESIZE))
	{
		g_control.rescale = 0.5f;
	}
	else
	{
		g_control.rescale = 1.0f;
	}
}
