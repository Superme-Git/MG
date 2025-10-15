// PVRUtilDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "PVRUtil.h"
#include "PVRUtilDlg.h"
#include "control.h"
control g_control;

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// ����Ӧ�ó��򡰹��ڡ��˵���� CAboutDlg �Ի���

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// �Ի�������
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

// ʵ��
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


// CPVRUtilDlg �Ի���




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
	ON_BN_CLICKED(IDC_CHECK_GEN_MIPMAPS, &CPVRUtilDlg::OnBnClickedCheckGenMipmaps)
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
END_MESSAGE_MAP()


// CPVRUtilDlg ��Ϣ�������

BOOL CPVRUtilDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// ��������...���˵�����ӵ�ϵͳ�˵��С�

	// IDM_ABOUTBOX ������ϵͳ���Χ�ڡ�
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

	// ���ô˶Ի����ͼ�ꡣ��Ӧ�ó��������ڲ��ǶԻ���ʱ����ܽ��Զ�
	//  ִ�д˲���
	SetIcon(m_hIcon, TRUE);			// ���ô�ͼ��
	SetIcon(m_hIcon, FALSE);		// ����Сͼ��

	// TODO: �ڴ���Ӷ���ĳ�ʼ������

	SetTimer(99, 1000, NULL);
	m_progress.SetWindowText(L"");

	GetDlgItem(IDOK)->EnableWindow(FALSE);

	return TRUE;  // ���ǽ��������õ��ؼ������򷵻� TRUE
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

// �����Ի��������С����ť������Ҫ����Ĵ���
//  �����Ƹ�ͼ�ꡣ����ʹ���ĵ�/��ͼģ�͵� MFC Ӧ�ó���
//  �⽫�ɿ���Զ���ɡ�

void CPVRUtilDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // ���ڻ��Ƶ��豸������

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// ʹͼ���ڹ����������о���
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// ����ͼ��
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

//���û��϶���С������ʱϵͳ���ô˺���ȡ�ù��
//��ʾ��
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
	// TODO: �ڴ������Ϣ�����������/�����Ĭ��ֵ
	this->SetWindowText(g_control.GetWindowText().c_str());
//	int count = g_control.GetMessageCount();
	CString wndTxt;
	m_message.GetWindowText(wndTxt);
	bool bChange = false;
//	for (int i = msgindex; i < count; ++i)
//	{
//		control::message msg = g_control.GetMessage(i);
		control::message msg = g_control.FetchFirstMessage();

		wndTxt += msg.msg.c_str();
		wndTxt += L"\r\n";
		bChange = true;
//	}
	// racing ... 
//	g_control.ClearMessages();
	if (bChange && msg.msg != L"")
	{
m_message.SetWindowText(wndTxt);
m_message.LineScroll(m_message.GetLineCount());
	}

	if( g_control.m_status == control::ENone && g_control.GetMessageCount() == 0 )
	{
		GetDlgItem( ID_PROCESSING )->EnableWindow(TRUE);
		GetDlgItem( IDC_INPUTDIR )->EnableWindow(TRUE);
		GetDlgItem( IDC_OUTPUTDIR )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_GEN_MIPMAPS )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_TO_PVR )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_TO_ETC1 )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_TO_DXT1 )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_TO_ATITC )->EnableWindow(TRUE);
		GetDlgItem( IDC_INPUTDIR )->EnableWindow(TRUE);
		GetDlgItem( IDC_OUTPUTDIR )->EnableWindow(TRUE);
		GetDlgItem( ID_SELECT_INPUTDIR )->EnableWindow(TRUE);
		GetDlgItem( ID_SELECT_OUTPUTDIR )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_TO_QUALITY_FAST )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_TO_QUALITY_NORMAL )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_TO_QUALITY_HIGH )->EnableWindow(TRUE);
		GetDlgItem( IDC_CHECK_TO_QUALITY_BEST )->EnableWindow(TRUE);
		GetDlgItem(IDC_CHECK_TO_ATITC_RGB)->EnableWindow(TRUE);
		GetDlgItem(IDC_CHECK_TO_ATITC_RGBA_E)->EnableWindow(TRUE);
		GetDlgItem(IDC_CHECK_TO_ATITC_RGBA_I)->EnableWindow(TRUE);
		GetDlgItem(IDC_CHECK_TO_DXT1_ALPHA)->EnableWindow(TRUE);
		GetDlgItem(IDC_CHECK_TO_RGBA8AA)->EnableWindow(TRUE);
		GetDlgItem(IDC_CHECK_TO_RGBA8AAS)->EnableWindow(TRUE);
		GetDlgItem(IDC_CHECK_MERGE_AS)->EnableWindow(TRUE);
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


void CPVRUtilDlg::OnBnClickedCheckGenMipmaps()
{
	g_control.m_genMipmaps = (BOOL)IsDlgButtonChecked(IDC_CHECK_GEN_MIPMAPS);
}


void CPVRUtilDlg::OnBnClickedSelectInputdir()
{
	BROWSEINFO bi;
	wchar_t Buffer[MAX_PATH];

	bi.hwndOwner = NULL;
	bi.pidlRoot = NULL;
	bi.pszDisplayName = Buffer;//�˲�����ΪNULL������ʾ�Ի���
	bi.lpszTitle = L"ѡ��·��";
	bi.ulFlags = BIF_RETURNONLYFSDIRS;
	bi.lpfn = NULL;
	bi.iImage = 0;

	LPITEMIDLIST pIDList = SHBrowseForFolder(&bi);//������ʾѡ��Ի���
	if(pIDList)
	{
		SHGetPathFromIDList(pIDList, Buffer);
		//ȡ���ļ���·����Buffer��
		g_control.m_inputDir = Buffer;//��·��������һ��CString������
	}

	SetDlgItemText( IDC_INPUTDIR, CString(g_control.m_inputDir.c_str()) );

}


void CPVRUtilDlg::OnBnClickedSelectOutputdir()
{
	BROWSEINFO bi;
	wchar_t Buffer[MAX_PATH];

	bi.hwndOwner = NULL;
	bi.pidlRoot = NULL;
	bi.pszDisplayName = Buffer;
	bi.lpszTitle = L"ѡ��·��";
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
	if( g_control.m_inputDir == L"" 
		|| g_control.m_outputDir == L""
		|| g_control.m_targetFormat == control::TF_None)
	{
		::MessageBox(NULL, L"set input | output dir and target format ", L"Error", MB_OK);
		return ;
	}

	if( g_control.m_targetFormat == control::TF_PVR )
	{
		if( !g_control.m_bPvrQualityFast && !g_control.m_bPvrQualityNormal 
			&& !g_control.m_bPvrQualityHigh && !g_control.m_bPvrQualityBest )
		{
			::MessageBox( NULL, L"plz: select pvr quality:[fast/normal/high/best]", L"Error", MB_OK );
			return ;
		}
	}

	if( g_control.m_targetFormat == control::TF_ATITC )
	{
		if( !g_control.m_atitc_rgb && !g_control.m_atitc_rgba_interpolated && !g_control.m_atitc_rgba_explicit)
		{
			::MessageBox( NULL, L"plz: select atitc rgba format:[rgb/rgb I/rgb E]", L"Error", MB_OK );
			return ;
		}
	}

	if( g_control.m_targetFormat == control::TF_None )
	{
		::MessageBox( NULL, L"plz: select convert target format", L"Error", MB_OK );
		return ;
	}

	if( g_control.m_output_ext != control::OE_ORIG && g_control.m_output_ext != control::OE_PVR )
	{
		::MessageBox( NULL, L"plz: select output file ext name[.pvr/.org]", L"Error", MB_OK );
		return ;
	}

	GetDlgItem( ID_PROCESSING )->EnableWindow(FALSE);
	GetDlgItem( IDC_INPUTDIR )->EnableWindow(FALSE);
	GetDlgItem( IDC_OUTPUTDIR )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_GEN_MIPMAPS )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_TO_PVR )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_TO_ETC1 )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_TO_DXT1 )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_TO_ATITC )->EnableWindow(FALSE);
	GetDlgItem( IDC_INPUTDIR )->EnableWindow(FALSE);
	GetDlgItem( IDC_OUTPUTDIR )->EnableWindow(FALSE);
	GetDlgItem( ID_SELECT_INPUTDIR )->EnableWindow(FALSE);
	GetDlgItem( ID_SELECT_OUTPUTDIR )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_TO_QUALITY_FAST )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_TO_QUALITY_NORMAL )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_TO_QUALITY_HIGH )->EnableWindow(FALSE);
	GetDlgItem( IDC_CHECK_TO_QUALITY_BEST )->EnableWindow(FALSE);
	GetDlgItem(IDC_CHECK_TO_ATITC_RGB)->EnableWindow(FALSE);
	GetDlgItem(IDC_CHECK_TO_ATITC_RGBA_E)->EnableWindow(FALSE);
	GetDlgItem(IDC_CHECK_TO_ATITC_RGBA_I)->EnableWindow(FALSE);
	GetDlgItem(IDC_CHECK_TO_DXT1_ALPHA)->EnableWindow(FALSE);
	GetDlgItem(IDC_CHECK_TO_RGBA8AA)->EnableWindow(FALSE);
	GetDlgItem(IDC_CHECK_TO_RGBA8AAS)->EnableWindow(FALSE);
	GetDlgItem(IDC_CHECK_MERGE_AS)->EnableWindow(FALSE);

	g_control.begin_convert();
	
	return ;
}


void CPVRUtilDlg::OnBnClickedClearLog()
{

	GetDlgItem(IDC_EDIT2)->SetWindowText(L"");
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

	if (g_control.m_status == control::EProcessing)
	{
		MessageBox(L"���ڴ���", L"info");
		return;
	}

	CDialog::OnCancel();
}


void CPVRUtilDlg::OnBnClickedCheckOutputExtOrig()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
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
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	if((bool)IsDlgButtonChecked(IDC_CHECK_OUTPUT_EXT_ORIG))
	{
		g_control.m_output_ext = control::OE_ORIG;
	}

}
