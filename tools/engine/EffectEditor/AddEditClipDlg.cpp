


#include "stdafx.h"
#include "EffectEditor.h"
#include "AddEditClipDlg.h"
#include "EffectEditorDoc.h"
#include "AudioFileDialog.h"
#include "..\engine\common\pfsutil.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CAddEditClipDlg, CDialog)

void CAddEditClipDlg::OnPaint()
{
	CPaintDC dc(this); 
	if (m_bUseDefColor)
	{
		CBrush brushGround(m_color);
		dc.FillRect(&m_rectColor, &brushGround);
	}
}

void CAddEditClipDlg::OnBnClickedCheckColor()
{
	m_bUseDefColor = !m_bUseDefColor;
	m_ColorButton.ShowWindow(m_bUseDefColor?SW_SHOW:SW_HIDE);
	InvalidateRect(&m_rectColor);
}

void CAddEditClipDlg::OnCbnSelendokComboPlaymode()
{
	switch (m_IDCComboPlayMode.GetCurSel())
	{
	case 0:
		m_nPartType = Nuclear::XPPM_LOOP;
		m_PlayTime.ShowWindow(SW_HIDE);
		break;
	case 1:
		m_nPartType = Nuclear::XPPM_LOOPNUM;
		m_PlayTime.ShowWindow(SW_SHOW);
		break;
	case 2:
		m_nPartType = Nuclear::XPPM_TIME;
		m_PlayTime.ShowWindow(SW_SHOW);
		break;
	}
}
void CAddEditClipDlg::OnBnClickedColorButton()
{
	CColorDialog dlg(m_color);
	if (dlg.DoModal() == IDOK)
	{
		m_color = dlg.GetColor();
		UpdateData(FALSE);
		Invalidate();
	}
}

void CAddEditClipDlg::OnCbnSelendokComboEffecttype()
{
	switch (m_EffectType.GetCurSel())
	{
	case 0:
		m_nType = Nuclear::XPET_ANI;
		m_ColorButton.ShowWindow(SW_HIDE);
		m_checkColor.ShowWindow(SW_SHOW);
		break;
	case 1:
		m_nType = Nuclear::XPET_PARTICLE;
		m_ColorButton.ShowWindow(m_bUseDefColor?SW_SHOW:SW_HIDE);
		m_checkColor.ShowWindow(SW_SHOW);
		break;
	case 2:
		m_nType = Nuclear::XPET_AUDIO;
		m_checkColor.ShowWindow(SW_HIDE);
		m_ColorButton.ShowWindow(SW_HIDE);
		break;
	case 3:
		m_nType = Nuclear::XPET_GEFFECT;
		m_ColorButton.ShowWindow(SW_HIDE);
		m_checkColor.ShowWindow(SW_HIDE);
		break;
	}
	m_IDCComboPlayMode.ShowWindow(SW_SHOW);
	m_IDCComboPlayMode.SetCurSel(1);
	m_PlayTime.ShowWindow(SW_SHOW);
	m_nPartType = Nuclear::XPPM_LOOPNUM;
	m_nPlayTime = 1;
	m_strFilepath = L"";
	UpdateData(FALSE);
	Invalidate();
}

void CAddEditClipDlg::OnBnClickedOk()
{
	UpdateData(TRUE);
	CString err = L"发现如下错误：";
	bool hasErr = false;
	if (m_strFilepath == L"")
	{
		err += L"\n  请输入或者选择文件";
		hasErr = true;
	}
	if (m_nStartFrame < 0)
	{
		err += L"\n  起始帧必须大于等于0";
		hasErr = true;
	}
	if (m_nType == Nuclear::XPET_GEFFECT && m_nPartType == Nuclear::XPPM_TIME)
	{
		err += L"\n  傀儡特效不能设置播放时间，请选择‘循环播放’或者‘播放次数’";
		hasErr = true;
	}
	if (m_nType == Nuclear::XPET_AUDIO && m_nPartType == Nuclear::XPPM_TIME)
	{
		err += L"\n  声音不能设置播放时间，请选择‘循环播放’或者‘播放次数’";
		hasErr = true;
	}
	if (hasErr)
	{
		AfxMessageBox(err);
	} else {
		OnOK();
	}
}

BOOL CAddEditClipDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_rectColor.left = 90;
	m_rectColor.right = m_rectColor.left + 80;
	m_rectColor.top = 146;
	m_rectColor.bottom = m_rectColor.top + 20;
	CString layerName;
	int i;
	for (i=0;i<Nuclear::GEffect::MAX_CLIP_LAYER;++i)
	{
		layerName.Format(L"%d", i);
		m_LayerCtrl.AddString(layerName);
	}
	m_EffectType.AddString(L"序列图");
	m_EffectType.AddString(L"粒子");
	m_EffectType.AddString(L"声音");
	m_EffectType.AddString(L"傀儡特效");
	m_IDCComboPlayMode.AddString(L"循环播放");
	m_IDCComboPlayMode.AddString(L"播放次数");
	m_IDCComboPlayMode.AddString(L"时间(毫秒)");
	m_IDCComboPlayMode.SetCurSel(1);
	m_nPartType = Nuclear::XPPM_LOOPNUM;
	UpdateData(FALSE);
	m_ColorButton.ShowWindow(SW_HIDE);
	m_checkColor.ShowWindow(SW_SHOW);
	m_bUseDefColor = false;
	m_checkColor.SetCheck(m_bUseDefColor);
	switch (m_nType)
	{
	case Nuclear::XPET_ANI:
		m_EffectType.SetCurSel(0);
		break;
	case Nuclear::XPET_PARTICLE:
		m_EffectType.SetCurSel(1);
		break;
	case Nuclear::XPET_AUDIO:
		m_EffectType.SetCurSel(2);
		break;
	case Nuclear::XPET_GEFFECT:
		m_EffectType.SetCurSel(3);
		break;
	default:
		break;
	}
	m_LayerCtrl.SetCurSel(m_layer);

	return TRUE;  

}
void CAddEditClipDlg::OnBnClickedBrouse()
{
	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	std::wstring strWorkDir = workdir;
	int npos = (int)strWorkDir.rfind('\\');
	strWorkDir = std::wstring(strWorkDir, 0, npos);
	CString wdir = strWorkDir.c_str();

	bool ok = false;
	switch (m_nType)
	{
	case Nuclear::XPET_ANI:
		{
			WCHAR extFilter[256];
			swprintf(extFilter, L"序列图文件 (*%s)|*%s||", SFI_ExtName, SFI_ExtName);
			CFileDialog dlg(TRUE, NULL, NULL, OFN_FILEMUSTEXIST, extFilter);			
			CString dir = wdir + L"\\res\\effect\\animation";
			dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
			if (dlg.DoModal() == IDOK)
			{
				std::wstring name;
				PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name);
				m_strFilepath = name.data();
				m_strFilepath.Delete(m_strFilepath.GetLength() - 4, 4);
				ok = true;
			}
		}
		break;
	case Nuclear::XPET_PARTICLE:
		{
			CFileDialog dlg(TRUE, NULL, NULL, OFN_FILEMUSTEXIST, L"粒子文件 (*.ptc)|*.ptc||");
			CString dir = wdir + L"\\res\\effect\\particle\\" + FNExt_ParticleWS;
			dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
			if (dlg.DoModal() == IDOK)
			{
				std::wstring name;
				PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name);
				m_strFilepath = name.data();
				m_strFilepath.Delete(m_strFilepath.GetLength() - 4, 4);
				ok = true;
			}
		}
		break;
	case Nuclear::XPET_AUDIO:
		{
			CAudioFileDialog dlg(theApp.GetEngine()->GetSoundManager(), TRUE, NULL, NULL, OFN_FILEMUSTEXIST, L"声音文件 (*.*|*.*||)");
			CString dir = wdir + L"\\res\\sound";
			dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
			if (dlg.DoModal() == IDOK)
			{
				std::wstring name;
				PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name);
				m_strFilepath = name.data();
				ok = true;
			}
		}
		break;
	case Nuclear::XPET_GEFFECT:
		{
			WCHAR extFilter[256];
			swprintf(extFilter, L"特效文件(*%s)|*%s||", Effect_ExtName, Effect_ExtName);
			CFileDialog dlg(TRUE, NULL, NULL, OFN_FILEMUSTEXIST, extFilter);
			CString dir = wdir + L"\\res\\effect\\geffect";
			dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
			if (dlg.DoModal() == IDOK)
			{
				std::wstring name;
				PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name);
				m_strFilepath = name.data();
				m_strFilepath.Delete(m_strFilepath.GetLength() - 9, 9);
				ok = true;
			}
		}
		break;
	default:
		break;
	}
	if ( ok )
	{
		if (m_nType != Nuclear::XPET_AUDIO)
		{

			m_strFilepath = m_strFilepath.Mid(8);
		}
		UpdateData(FALSE);
	}
}

BEGIN_MESSAGE_MAP(CAddEditClipDlg, CDialog)
	ON_BN_CLICKED(IDC_BROUSE, &CAddEditClipDlg::OnBnClickedBrouse)
	ON_BN_CLICKED(IDOK, &CAddEditClipDlg::OnBnClickedOk)
	ON_CBN_SELENDOK(IDC_COMBO_EFFECTTYPE, &CAddEditClipDlg::OnCbnSelendokComboEffecttype)
	ON_BN_CLICKED(IDC_COLOR_BUTTON, &CAddEditClipDlg::OnBnClickedColorButton)
	ON_CBN_SELENDOK(IDC_COMBO_PLAYMODE, &CAddEditClipDlg::OnCbnSelendokComboPlaymode)
	ON_BN_CLICKED(IDC_CHECK_COLOR, &CAddEditClipDlg::OnBnClickedCheckColor)
	ON_WM_PAINT()
END_MESSAGE_MAP()




void CAddEditClipDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_FILE_PATH, m_strFilepath);
	DDX_Text(pDX, IDC_COMBO_LAYER, m_layer);
	DDX_Text(pDX, IDC_START_FRAME, m_nStartFrame);
	DDX_Text(pDX, IDC_TIME, m_nPlayTime);
	DDX_Control(pDX, IDC_COMBO_LAYER, m_LayerCtrl);
	DDX_Control(pDX, IDC_COMBO_EFFECTTYPE, m_EffectType);
	DDX_Control(pDX, IDC_COLOR_BUTTON, m_ColorButton);
	DDX_Control(pDX, IDC_COMBO_PLAYMODE, m_IDCComboPlayMode);
	DDX_Control(pDX, IDC_TIME, m_PlayTime);
	DDX_Control(pDX, IDC_CHECK_COLOR, m_checkColor);
}


CAddEditClipDlg::~CAddEditClipDlg()
{
}

CAddEditClipDlg::CAddEditClipDlg(CWnd* pParent /*=NULL*/)
: CDialog(CAddEditClipDlg::IDD, pParent)
, m_strFilepath(_T(""))
, m_layer(0)
, m_nStartFrame(0)
, m_nType(Nuclear::XPET_ANI)
, m_nPlayTime(1)
, m_color(0x00FFFFFF)
{

}

