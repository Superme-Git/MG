


#include "stdafx.h"
#include "SpriteEditor.h"
#include "PackDlg.h"




IMPLEMENT_DYNAMIC(CPackDlg, CDialog)

void CPackDlg::GetAnimationFilename(std::wstring &name)
{
	name = m_AnimationFilename.GetString();
}

void CPackDlg::OnCbnSelchangeComboTimeMode()
{
	UpdateData();
	m_TimeMode = m_ComboTimeMode.GetCurSel();
	if (m_nFrame != 0)
	{
		if (m_TimeMode == 0)	
		{
			m_PackTime *= m_nFrame;
		} else {
			m_PackTime /= m_nFrame;
		}
		UpdateData(FALSE);
	}
}

void CPackDlg::OnBnClickedChkAdframe()
{
	UpdateData();
	GetDlgItem(IDC_EDT_FRAMECOUNT)->EnableWindow(!m_fAutoDetectFrameCount);
}

void CPackDlg::OnBnClickedButton2()
{
	SetDlgItemText(IDC_EDT_PACKDSTPATH, L"");
}

void CPackDlg::OnBnClickedBtnSelpackdstdir()
{
	BROWSEINFO   bInfo;  
	ZeroMemory(&bInfo,   sizeof(bInfo));  
	bInfo.hwndOwner   =   NULL;  
	TCHAR   tchPath[MAX_PATH];  
	bInfo.lpszTitle   =   _T("ѡ�����Ŀ¼:   ");  
	bInfo.ulFlags   =   BIF_RETURNONLYFSDIRS | BIF_DONTGOBELOWDOMAIN;



	LPITEMIDLIST   lpDlist;  

	lpDlist   =   SHBrowseForFolder(&bInfo)   ;   
	if(lpDlist == NULL)  
		return;


	SHGetPathFromIDList(lpDlist,   tchPath);

	SetDlgItemText(IDC_EDT_PACKDSTPATH, tchPath);

}

void CPackDlg::OnOK()
{
	UpdateData();

	int cursel = m_ComboTexFormat.GetCurSel();
	if( cursel < 0 ) return;
	CString str;
	m_ComboTexFormat.GetLBText(cursel, str);
	m_TexFormat = m_TexFormatMap[std::wstring(str)];

	cursel = m_ComboFileFormat.GetCurSel();
	if( cursel < 0 ) return;
	m_ComboFileFormat.GetLBText(cursel, str);
	m_FileFormat = m_FileFormatMap[std::wstring(str)];

	cursel = m_ComboBlend.GetCurSel();
	if( cursel < 0 ) return;
	m_ComboBlend.GetLBText(cursel, str);
	m_Blend = m_BlendMap[std::wstring(str)];

	cursel = m_ComboDirMode.GetCurSel();
	if( cursel < 0 ) return;
	m_ComboDirMode.GetLBText(cursel, str);
	m_DirMode = m_DirModeMap[std::wstring(str)];

	GetDlgItemText(IDC_EDT_PACKDSTPATH, str);
	m_DstPath = str;
	GetDlgItemText(IDC_EDT_PARTPATH, str);
	m_PartPath = str;

	if( m_fNew )
	{
		if( !m_fAutoDetectFrameCount)
		{
			if( m_nFrame < 1 || m_nFrame > 128 )
			{
				AfxMessageBox(L"֡�����ò���ȷ��Ӧ���� 1 �� 128 ֮��");
				return;
			}
		}
	}
	m_SystemLevel = m_comboSysLevel.GetCurSel();

	CDialog::OnOK();
}

BOOL CPackDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_FileFormatMap[std::wstring(L"dds")] = Nuclear::XPIFF_DDS;
	m_FileFormatMap[std::wstring(L"png")] = Nuclear::XPIFF_PNG;
	m_FileFormatMap[std::wstring(L"webp")] = Nuclear::XPIFF_WEBP;

	m_TexFormatMap[std::wstring(L"A8R8G8B8")] = Nuclear::XPTEXFMT_A8R8G8B8;
	m_TexFormatMap[std::wstring(L"A4R4G4B4")] = Nuclear::XPTEXFMT_A4R4G4B4;
	m_TexFormatMap[std::wstring(L"DXT1")] = Nuclear::XPTEXFMT_DXT1;
	m_TexFormatMap[std::wstring(L"DXT2")] = Nuclear::XPTEXFMT_DXT2;
	m_TexFormatMap[std::wstring(L"DXT3")] = Nuclear::XPTEXFMT_DXT3;
	m_TexFormatMap[std::wstring(L"DXT4")] = Nuclear::XPTEXFMT_DXT4;
	m_TexFormatMap[std::wstring(L"DXT5")] = Nuclear::XPTEXFMT_DXT5;
	m_TexFormatMap[std::wstring(L"R3G3B2")] = Nuclear::XPTEXFMT_R3G3B2;

	m_BlendMap[std::wstring(L"Alpha���")] = Nuclear::XPTEXBM_DEFAULT;
	m_BlendMap[std::wstring(L"ɫ�ʱ���")] = Nuclear::XPTEXBM_ADDITIVE;
	m_BlendMap[std::wstring(L"Alpha Add")] = Nuclear::XPTEXBM_ADDITIVE_ALPHA;

	m_DirModeMap[std::wstring(L"�˷���")] = Nuclear::XPANIDM_8;
	m_DirModeMap[std::wstring(L"�ķ���")] = Nuclear::XPANIDM_4;
	m_DirModeMap[std::wstring(L"������")] = Nuclear::XPANIDM_3;
	m_DirModeMap[std::wstring(L"�����ַ���")] = Nuclear::XPANIDM_8USE1;
	m_DirModeMap[std::wstring(L"���˷���")] = Nuclear::XPANIDM_8FROM5;
	m_DirModeMap[std::wstring(L"һ��˷���")] = Nuclear::XPANIDM_8FROM1;
	m_DirModeMap[std::wstring(L"������")] = Nuclear::XPANIDM_2;
	m_DirModeMap[std::wstring(L"���±�������")] = Nuclear::XPANIDM_B2FROM1;
	m_DirModeMap[std::wstring(L"����������������ķ���")] = Nuclear::XPANIDM_4FROM2;

	std::wstring filetp[3] = {
		L"dds",L"png",L"webp"
	};
	for(int i=0; i<3; ++i)
	{
		m_ComboFileFormat.AddString(filetp[i].c_str());
		if( m_FileFormatMap[filetp[i]] == m_FileFormat )
			m_ComboFileFormat.SetCurSel(i);
	}

	std::wstring texfmts[8] = {
		L"DXT5",L"A8R8G8B8",L"A4R4G4B4",L"DXT1",L"DXT2",L"DXT3",L"DXT4",L"R3G3B2"
	};

	for(int i=0; i<8; ++i)
	{
		m_ComboTexFormat.AddString(texfmts[i].c_str());
		if( m_TexFormatMap[texfmts[i]] == m_TexFormat )
			m_ComboTexFormat.SetCurSel(i);
	}

	std::wstring blends[3] = {
		L"Alpha���",L"ɫ�ʱ���", L"Alpha Add"
	};

	for(int i=0; i<3; ++i)
	{
		m_ComboBlend.AddString(blends[i].c_str());
		if( m_BlendMap[blends[i]] == m_Blend )
			m_ComboBlend.SetCurSel(i);
	}

	std::wstring dirmodes[Nuclear::MAX_DIRMODE-1] = {
		L"�����ַ���",L"�˷���",L"�ķ���",L"������",L"���˷���",L"һ��˷���",L"������",L"���±�������",L"����������������ķ���"
	};

	for(int i=0; i<Nuclear::MAX_DIRMODE-1; ++i)
	{
		m_ComboDirMode.AddString(dirmodes[i].c_str());
		if( m_DirModeMap[dirmodes[i]] == m_DirMode )
			m_ComboDirMode.SetCurSel(i);
	}

	SetDlgItemText(IDC_EDT_PACKDSTPATH, m_DstPath.c_str());

	m_ComboTimeMode.AddString(L"��ʱ��");
	m_ComboTimeMode.AddString(L"ƽ��ÿ֡ʱ��");
	m_TimeMode = 0;
	m_ComboTimeMode.SetCurSel(m_TimeMode);


	if( !m_fNew )
		GetDlgItem(IDC_CHK_ADFRAME)->EnableWindow(FALSE);

	GetDlgItem(IDC_EDT_FRAMECOUNT)->EnableWindow(!m_fAutoDetectFrameCount);
	m_comboSysLevel.InsertString(0, L"0 ����");
	m_comboSysLevel.InsertString(1, L"1 �е�");
	m_comboSysLevel.InsertString(2, L"2 ����");
	m_comboSysLevel.SetCurSel(m_SystemLevel);

	return TRUE;  

}

BEGIN_MESSAGE_MAP(CPackDlg, CDialog)
	ON_BN_CLICKED(IDC_BTN_SELPACKDSTDIR, &CPackDlg::OnBnClickedBtnSelpackdstdir)
	ON_BN_CLICKED(IDC_BUTTON2, &CPackDlg::OnBnClickedButton2)
	ON_BN_CLICKED(IDC_CHK_ADFRAME, &CPackDlg::OnBnClickedChkAdframe)
	ON_CBN_SELCHANGE(IDC_COMBO_TIME_MODE, &CPackDlg::OnCbnSelchangeComboTimeMode)
	ON_BN_CLICKED(IDC_BTN_SELPARTPATH, &CPackDlg::OnBnClickedBtnSelpartpath)
END_MESSAGE_MAP()




void CPackDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_CMB_TEXFMT, m_ComboTexFormat);
	DDX_Text(pDX, IDC_EDT_CENTERX, m_CenterX);
	DDX_Text(pDX, IDC_EDT_CENTERY, m_CenterY);
	DDX_Text(pDX, IDC_EDT_PACKTIME, m_PackTime);
	DDV_MinMaxInt(pDX, m_PackTime, 0, 256000);
	DDX_Control(pDX, IDC_CMB_BLEND, m_ComboBlend);
	DDX_Control(pDX, IDC_CMB_DIRMODE, m_ComboDirMode);
	DDX_Text(pDX, IDC_EDT_REGIONCOUNT, m_nRegion);
	DDX_Text(pDX, IDC_EDT_FRAMECOUNT, m_nFrame);
	DDX_Check(pDX, IDC_CHK_ADREGION, m_fAutoDetectRegionCount);
	DDX_Check(pDX, IDC_CHK_ADFRAME, m_fAutoDetectFrameCount);
	DDX_Check(pDX, IDC_CHECK_BIND_TYPE, m_bBindType);
	DDX_Control(pDX, IDC_COMBO_TIME_MODE, m_ComboTimeMode);
	DDX_Control(pDX, IDC_COMBO_SYS_LEVEL, m_comboSysLevel);

	DDX_Text(pDX,IDC_ANIMFILE_NAME, m_AnimationFilename);
	DDX_Control(pDX, IDC_CMB_TEXFMT2, m_ComboFileFormat);
}



CPackDlg::~CPackDlg()
{
}

CPackDlg::CPackDlg(CWnd* pParent )
: CDialog(CPackDlg::IDD, pParent)
, m_TimeMode(0)
, m_PackTime(1000)
, m_CenterX(0)
, m_CenterY(0)
, m_nRegion(0)
, m_nFrame(0)
, m_fAutoDetectRegionCount(TRUE)
, m_fAutoDetectFrameCount(TRUE)
, m_fNew(true)
, m_bBindType(FALSE)
, m_SystemLevel(0)
{

}


void CPackDlg::OnBnClickedBtnSelpartpath()
{
	BROWSEINFO   bInfo;  
	ZeroMemory(&bInfo,   sizeof(bInfo));  
	bInfo.hwndOwner   =   NULL;  
	TCHAR   tchPath[MAX_PATH];  
	bInfo.lpszTitle   =   _T("ѡ�����Ŀ¼:   ");  
	bInfo.ulFlags   =   BIF_RETURNONLYFSDIRS | BIF_DONTGOBELOWDOMAIN;



	LPITEMIDLIST   lpDlist;  

	lpDlist   =   SHBrowseForFolder(&bInfo)   ;   
	if(lpDlist == NULL)  
		return;


	SHGetPathFromIDList(lpDlist,   tchPath);

	SetDlgItemText(IDC_EDT_PARTPATH, tchPath);
}
