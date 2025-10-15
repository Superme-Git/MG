// DlgPSANIparam.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "DlgPSANIparam.h"


// CDlgPSANIparam 对话框

IMPLEMENT_DYNAMIC(CDlgPSANIparam, CDialog)

CDlgPSANIparam::CDlgPSANIparam(CWnd* pParent /*=NULL*/)
	: CDialog(CDlgPSANIparam::IDD, pParent)
	, m_nFramePerSecond(20)
	, m_nRadioPrintScreenArea(FALSE)
	, m_strFilePath(_T(""))
{
	m_nFileFormat = Nuclear::XPIFF_JPG;
}

CDlgPSANIparam::~CDlgPSANIparam()
{
}

void CDlgPSANIparam::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_psAniSpeed, m_nFramePerSecond);
	DDV_MinMaxInt(pDX, m_nFramePerSecond, 0, 20);
	DDX_Control(pDX, IDC_COMBO_PicFileFormat, m_comboPicFileFormat);
	DDX_Radio(pDX, IDC_RADIO1, m_nRadioPrintScreenArea);
	DDX_Text(pDX, IDC_EDIT_FILEPATH, m_strFilePath);
}


BEGIN_MESSAGE_MAP(CDlgPSANIparam, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgPSANIparam::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CDlgPSANIparam::OnBnClickedCancel)
	ON_EN_KILLFOCUS(IDC_EDIT_psAniSpeed, &CDlgPSANIparam::OnEnKillfocusEditpsanispeed)
	ON_CBN_SELCHANGE(IDC_COMBO_PicFileFormat, &CDlgPSANIparam::OnCbnSelchangeComboPicfileformat)
	ON_BN_CLICKED(IDC_RADIO1, &CDlgPSANIparam::OnBnClickedRadio1)
	ON_BN_CLICKED(IDC_RADIO2, &CDlgPSANIparam::OnBnClickedRadio2)
	ON_BN_CLICKED(IDC_BUTTON_SETFILEPATH, &CDlgPSANIparam::OnBnClickedButtonSetfilepath)
END_MESSAGE_MAP()


// CDlgPSANIparam 消息处理程序

BOOL CDlgPSANIparam::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化

	m_comboPicFileFormat.InsertString(0, L"bmp");
	m_comboPicFileFormat.InsertString(1, L"jpg");
	m_comboPicFileFormat.InsertString(2, L"tga 暂不支持"); //不支持
	m_comboPicFileFormat.InsertString(3, L"png 暂不支持"); //不支持
	m_comboPicFileFormat.InsertString(4, L"dds");
	m_comboPicFileFormat.SetCurSel(1);

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CDlgPSANIparam::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
	OnOK();
}

void CDlgPSANIparam::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
	OnCancel();
}

void CDlgPSANIparam::OnEnKillfocusEditpsanispeed()
{
	UpdateData(true);	
	m_nFramePerSecond = m_nFramePerSecond<0 ? 0:m_nFramePerSecond;
	m_nFramePerSecond = m_nFramePerSecond>20 ? 20:m_nFramePerSecond;
	UpdateData(false);
}

void CDlgPSANIparam::OnCbnSelchangeComboPicfileformat()
{
	m_nFileFormat = (Nuclear::XPIMAGE_FILEFORMAT)( m_comboPicFileFormat.GetCurSel() );	
}

void CDlgPSANIparam::OnBnClickedRadio1()
{
//	UpdateData(true);
//	int aafa = m_nRadioPrintScreenArea;
}

void CDlgPSANIparam::OnBnClickedRadio2()
{
//	UpdateData(true);
//	int aafa = m_nRadioPrintScreenArea;
}

void CDlgPSANIparam::OnBnClickedButtonSetfilepath()
{
/*
	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, NULL, NULL, dwFlags, NULL, NULL);

	dlg.DoModal();

	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
	filePath = dlg.GetPathName();
	int nPos = filePath.ReverseFind('\\');	
	m_strFilePath = filePath.Left(nPos); //指定文件所在目录...

	UpdateData(false);
*/
	BROWSEINFO bInfo;  
	ZeroMemory(&bInfo, sizeof(bInfo));  
	bInfo.hwndOwner = m_hWnd;  
	TCHAR tchPath[255];  
	bInfo.lpszTitle = L"所选择路径: ";  
	bInfo.ulFlags = BIF_RETURNONLYFSDIRS;          

	LPITEMIDLIST lpDlist;  
	//用来保存返回信息的IDList，使用SHGetPathFromIDList函数转换为字符串  
	lpDlist = SHBrowseForFolder(&bInfo);   //显示选择对话框  
	if(lpDlist != NULL)
	{
		SHGetPathFromIDList(lpDlist, tchPath); //把项目标识列表转化成目录
		m_strFilePath = tchPath;
	}
	UpdateData(false);
}
