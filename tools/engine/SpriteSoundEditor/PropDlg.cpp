// PropDlg.cpp : implementation file
//

#include "stdafx.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\sprite\spritemanager.h"
#include "SpriteSoundEditor.h"
#include "SpriteSoundEditorDoc.h"
#include "PropView.h"
#include "PropDlg.h"
#include "AudioFileDialog.h"


// CPropDlg dialog

IMPLEMENT_DYNAMIC(CPropDlg, CDialog)

CPropDlg::CPropDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CPropDlg::IDD, pParent), m_pView(NULL), m_bIsInited(false)
	, m_nVolume(100)
	, m_nFreq(0)
	, m_nComboKeySel(0)
	, m_nComboModel(0)
{
	m_strLayerDeses[0] = L"";
	m_strLayerDeses[1] = L"";
	m_strLayerDeses[2] = L"";
	m_strLayerDeses[3] = L"";
	m_strLayerDeses[4] = L"";
	m_strLayerDeses[5] = L"";
	m_strLayerDeses[6] = L"";
	m_strLayerDeses[7] = L"";
}

CPropDlg::~CPropDlg()
{
}

void CPropDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_COMBO_MODEL, m_comboModel);
	DDX_Control(pDX, IDC_COMBO_KEY_COMP, m_comboKeyComp);
	DDX_Control(pDX, IDC_COMBO_ACTION, m_comboAction);
	DDX_Control(pDX, IDC_COMBO1, m_comboSound);
	DDX_Control(pDX, IDC_SLIDER_V, m_sliderVolume);
	DDX_Control(pDX, IDC_SLIDER_F, m_sliderFreq);
	DDX_Control(pDX, IDC_COMBO_LAYER0, m_comboLayers[0]);
	DDX_Control(pDX, IDC_COMBO_LAYER1, m_comboLayers[1]);
	DDX_Control(pDX, IDC_COMBO_LAYER2, m_comboLayers[2]);
	DDX_Control(pDX, IDC_COMBO_LAYER3, m_comboLayers[3]);
	DDX_Control(pDX, IDC_COMBO_LAYER4, m_comboLayers[4]);
	DDX_Control(pDX, IDC_COMBO_LAYER5, m_comboLayers[5]);
	DDX_Control(pDX, IDC_COMBO_LAYER6, m_comboLayers[6]);
	DDX_Control(pDX, IDC_COMBO_LAYER7, m_comboLayers[7]);
	DDX_Text(pDX, IDC_EDIT_V, m_nVolume);
	DDX_Text(pDX, IDC_EDIT_F, m_nFreq);
	DDX_Text(pDX, IDC_LAYER_DES0, m_strLayerDeses[0]);
	DDX_Text(pDX, IDC_LAYER_DES1, m_strLayerDeses[1]);
	DDX_Text(pDX, IDC_LAYER_DES2, m_strLayerDeses[2]);
	DDX_Text(pDX, IDC_LAYER_DES3, m_strLayerDeses[3]);
	DDX_Text(pDX, IDC_LAYER_DES4, m_strLayerDeses[4]);
	DDX_Text(pDX, IDC_LAYER_DES5, m_strLayerDeses[5]);
	DDX_Text(pDX, IDC_LAYER_DES6, m_strLayerDeses[6]);
	DDX_Text(pDX, IDC_LAYER_DES7, m_strLayerDeses[7]);
}

void CPropDlg::ResetCont()
{
	if (!m_pView)
		return;
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	Nuclear::SpriteManager *pSprMan = pDoc->GetSpriteManager();
	if (!m_bIsInited)
	{
		m_bIsInited = true;
		m_comboModel.ResetContent();
		std::vector<std::wstring> names;
		pSprMan->DumpModelNames(names);
		std::vector<std::wstring>::iterator mIt = names.begin(), mIe = names.end();
		int i = 0;
		for (;mIt!=mIe;++mIt, ++i)
		{
			m_comboModel.InsertString(i, mIt->c_str());
		}
		m_comboModel.SetCurSel(0);
		m_nComboModel = 0;
		CheckRadioButton(IDC_RADIO_DIR0, IDC_RADIO_DIR7, IDC_RADIO_DIR0);
		OnChangeModel();
	}
}

void CPropDlg::OnChangeModel()
{
	if (!m_pView)
		return;
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	if (!pDoc->SaveModified())
	{
		m_comboModel.SetCurSel(m_nComboModel);
		return;
	}
	m_nComboModel = m_comboModel.GetCurSel();
	CString strName;
	m_comboModel.GetWindowText(strName);
	std::wstring modelName = strName.GetBuffer();
	strName.ReleaseBuffer();
	pDoc->SetModel(modelName);
	m_comboKeyComp.ResetContent();
	m_comboAction.ResetContent();
	int i = 0;
	for (;i<8;++i)
	{
		m_strLayerDeses[i] = L"";
		m_comboLayers[i].ResetContent();
		m_comboLayers[i].ShowWindow(SW_HIDE);
	}
	if (modelName.size() > 0)
	{
		Nuclear::SpriteManager *pSprMan = pDoc->GetSpriteManager();
		std::vector<std::wstring> names;
		pSprMan->DumpAction(modelName, names);
		std::vector<std::wstring>::iterator it = names.begin(), ie = names.end();
		for (i=0;it!=ie;++it,++i)
		{
			m_comboAction.InsertString(i, it->c_str());
		}
		int layerCount = pSprMan->GetLayerCount(modelName);
		int j = 0;
		for (i=0;i<layerCount;++i)
		{
			m_strLayerDeses[i] = pSprMan->GetLayerDes(modelName, i).c_str();
			m_comboKeyComp.InsertString(i, m_strLayerDeses[i]);
			names.clear();
			pSprMan->DumpComponent(modelName, i, names);
			m_comboLayers[i].InsertString(0, L"");
			for (j=1,it=names.begin(),ie=names.end();it!=ie;++it,++j)
			{
				m_comboLayers[i].InsertString(j, it->c_str());
			}
			m_comboLayers[i].ShowWindow(SW_SHOW);
			m_comboLayers[i].SetCurSel(names.size() > 1?1:0);
			OnChangeComponent(i);
		}
		m_comboKeyComp.InsertString(0, L"无");
		const Nuclear::ModelSoundParam &param = pDoc->GetCurmodelParam();
		m_nComboKeySel = param.keyComponentID + 1;
		m_comboKeyComp.SetCurSel(m_nComboKeySel);
		m_comboAction.SetCurSel(0);
		OnChangeAction();
		UpdateData(FALSE);
	}
}

void CPropDlg::OnChangeAction()
{
	if (!m_pView)
		return;
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	CString strName;
	m_comboAction.GetWindowText(strName);
	std::wstring actionName = strName.GetBuffer();
	strName.ReleaseBuffer();
	pDoc->SetAction(actionName);
	ResetSoundList();
}

void CPropDlg::OnChangeComponent(int i)
{
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	CString strName;
	m_comboLayers[i].GetWindowText(strName);
	std::wstring component = strName.GetBuffer();
	strName.ReleaseBuffer();
	pDoc->SetComponent(i, component);
	if (i == m_comboKeyComp.GetCurSel() - 1)//关键部件变了
	{
		ResetSoundList();
	}
}

void CPropDlg::OnChangeSound()
{
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	int id = m_comboSound.GetCurSel();
	pDoc->SetCurSound(id);
	m_nFreq = FreqToShow(pDoc->GetCurSoundFreq());
	m_nVolume = Vol255to100(pDoc->GetCurSoundVolume());
	m_sliderFreq.SetPos(m_nFreq);
	m_sliderVolume.SetPos(m_nVolume);
	UpdateData(FALSE);
}

void CPropDlg::EnableCtrls(BOOL enable)
{
	m_comboSound.EnableWindow(enable);
	m_sliderFreq.EnableWindow(enable);
	m_sliderVolume.EnableWindow(enable);
	GetDlgItem(IDC_EDIT_V)->EnableWindow(enable);
	GetDlgItem(IDC_EDIT_F)->EnableWindow(enable);
	GetDlgItem(IDC_BUTTON_DEL)->EnableWindow(enable);
}

void CPropDlg::ResetSoundList()
{
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	const Nuclear::PActionSoundParamMap pMap = pDoc->GetCurParamMap();
	m_comboSound.ResetContent();
	if ((!pMap) || (pDoc->GetCurAction().size() == 0))
	{
		EnableCtrls(FALSE);
		return;
	}
	Nuclear::ActionSoundParamMap::const_iterator it = pMap->find(pDoc->GetCurAction());
	if (it == pMap->end() || it->second.size() == 0)
	{
		EnableCtrls(FALSE);
		return;
	}
	EnableCtrls(TRUE);
	Nuclear::SpriteSoundParamVet::const_iterator sIt = it->second.begin(), sIe = it->second.end();
	int i = 0;
	for (;sIt!=sIe;++sIt,++i)
	{
		m_comboSound.InsertString(i, sIt->soundname.c_str());
	}
	m_comboSound.SetCurSel(pDoc->GetCurSound()<i?pDoc->GetCurSound():0);
	OnChangeSound();
}

BEGIN_MESSAGE_MAP(CPropDlg, CDialog)
	ON_CONTROL_RANGE(BN_CLICKED, IDC_RADIO_DIR0, IDC_RADIO_DIR7, &CPropDlg::OnBnClickedRadioDir)
	ON_CBN_SELCHANGE(IDC_COMBO_MODEL, &CPropDlg::OnCbnSelchangeComboModel)
	ON_CBN_SELCHANGE(IDC_COMBO_KEY_COMP, &CPropDlg::OnCbnSelchangeComboKeyComp)
	ON_CBN_SELCHANGE(IDC_COMBO_ACTION, &CPropDlg::OnCbnSelchangeComboAction)
	ON_CONTROL_RANGE(CBN_SELCHANGE, IDC_COMBO_LAYER0, IDC_COMBO_LAYER7,  &CPropDlg::OnCbnSelchangeComboLayer)
	ON_BN_CLICKED(IDC_BUTTON_ADD, &CPropDlg::OnBnClickedButtonAdd)
	ON_BN_CLICKED(IDC_BUTTON_DEL, &CPropDlg::OnBnClickedButtonDel)
	ON_CBN_SELCHANGE(IDC_COMBO1, &CPropDlg::OnCbnSelchangeCombo1)
	ON_BN_CLICKED(IDC_SAVE, &CPropDlg::OnBnClickedSave)
	ON_WM_HSCROLL()
	ON_EN_KILLFOCUS(IDC_EDIT_F, &CPropDlg::OnEnKillfocusEditF)
	ON_EN_KILLFOCUS(IDC_EDIT_V, &CPropDlg::OnEnKillfocusEditV)
	ON_BN_CLICKED(IDC_BUTTON_COPY, &CPropDlg::OnBnClickedButtonCopy)
	ON_BN_CLICKED(IDC_BUTTON_PASTE, &CPropDlg::OnBnClickedButtonPaste)
END_MESSAGE_MAP()


// CPropDlg message handlers

BOOL CPropDlg::Create(UINT nIDTemplate, CWnd* pParentWnd, CPropView* pView)
{
	m_pView = pView;
	return CDialog::Create(nIDTemplate, pParentWnd);
}

void CPropDlg::OnBnClickedRadioDir(UINT nID)
{
	UINT dir = nID - IDC_RADIO_DIR0;
	XPASSERT(dir >= 0);
	XPASSERT(dir < 8);
	if (!m_pView)
		return;
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	pDoc->SetDirection(static_cast<Nuclear::XPDIRECTION>(dir));
}

void CPropDlg::OnCbnSelchangeComboModel()
{
	OnChangeModel();
}

void CPropDlg::OnCbnSelchangeComboKeyComp()
{
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	const Nuclear::ModelSoundParam &param = pDoc->GetCurmodelParam();
	int i = m_comboKeyComp.GetCurSel() - 1;//“无”应该是-1
	if (param.keyComponentID != i)
	{
		if (param.soundmap.size() > 1 || (param.soundmap.size() > 0 && param.soundmap.begin()->second != NULL))
		{
			if (AfxMessageBox(L"改变关键部件会清空此模型的所有声音设置，是否继续？", MB_YESNO) == IDNO)
			{
				m_comboKeyComp.SetCurSel(m_nComboKeySel);
				return;
			}
		}
	}
	m_nComboKeySel = i + 1;
	pDoc->SetKeyLayer(i);
	if ( i > 0)
	{
		OnChangeComponent(i);//在这里也会调用ResetSoundList();，所以不必专门调了
	} else {
		ResetSoundList();
	}	
}

void CPropDlg::OnCbnSelchangeComboAction()
{
	OnChangeAction();
}

void CPropDlg::OnCbnSelchangeComboLayer(UINT nID)
{
	nID -= IDC_COMBO_LAYER0;
	XPASSERT(nID >= 0);
	XPASSERT(nID < 8);
	if (!m_pView)
		return;
	OnChangeComponent((int)nID);
}

void CPropDlg::OnBnClickedButtonAdd()
{
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	if (pDoc->GetCurAction() == L"")
	{
		AfxMessageBox(L"请先选择一个动作");
		return;
	}
	const Nuclear::ModelSoundParam &param = pDoc->GetCurmodelParam();
	wchar_t olddir[MAX_PATH];
	GetCurrentDirectory(MAX_PATH, olddir);
	CAudioFileDialog dlg(pDoc->GetSoundManager(), TRUE, NULL, NULL, OFN_FILEMUSTEXIST, L"声音文件(*.*|*.*||)");
	CString dir = pDoc->GetPath() + L"\\res\\sound";
	dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
	if (dlg.DoModal() == IDOK)
	{
		std::wstring name;
		PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name);
		if (pDoc->AddASound(name) == ASE_OK)
		{
			int nId = m_comboSound.GetCount();
			m_comboSound.InsertString(nId, name.c_str());
			m_comboSound.SetCurSel(nId);
			OnChangeSound();
			EnableCtrls(TRUE);
		} else {
			AfxMessageBox(L"添加声音失败!");
		}
	}
	SetCurrentDirectory(olddir);
}

void CPropDlg::OnBnClickedButtonDel()
{
	m_pView->GetDocument()->DelASound();
	ResetSoundList();
}

BOOL CPropDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_sliderFreq.SetRange(-128, 127);
	m_sliderFreq.SetPos(-128);
	m_sliderFreq.SetPos(0);
	m_sliderVolume.SetRange(0, 100);
	m_sliderVolume.SetPos(0);
	m_sliderVolume.SetPos(100);

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CPropDlg::OnCbnSelchangeCombo1()
{
	OnChangeSound();
}

void CPropDlg::OnBnClickedSave()
{
	m_pView->GetDocument()->Save();
}

void CPropDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	switch (pScrollBar->GetDlgCtrlID())
	{
	case IDC_SLIDER_V:
		m_nVolume = m_sliderVolume.GetPos();
		UpdateData(FALSE);
		m_pView->GetDocument()->SetSoundVol(Vol100to255(m_nVolume));
		break;
	case IDC_SLIDER_F:
		m_nFreq = m_sliderFreq.GetPos();
		UpdateData(FALSE);
		m_pView->GetDocument()->SetSoundFreq(FreqToSave(m_nFreq));
		break;
	}
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CPropDlg::OnEnKillfocusEditF()
{
	int oldVolume = m_nVolume;
	UpdateData(TRUE);
	if (m_nVolume < 0 || m_nVolume > 100)
	{
		m_nVolume = oldVolume;
		UpdateData(FALSE);
		return;
	}
	m_sliderVolume.SetPos(m_nVolume);
	m_pView->GetDocument()->SetSoundVol(Vol100to255(m_nVolume));
}

void CPropDlg::OnEnKillfocusEditV()
{
	int oldFreq = m_nFreq;
	UpdateData(TRUE);
	if (m_nFreq < 0 || m_nFreq > 100)
	{
		m_nFreq = oldFreq;
		UpdateData(FALSE);
		return;
	}
	m_sliderFreq.SetPos(m_nFreq);
	m_pView->GetDocument()->SetSoundFreq(FreqToSave(m_nFreq));
}

void CPropDlg::OnBnClickedButtonCopy()
{
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	const Nuclear::PActionSoundParamMap pMap = pDoc->GetCurParamMap();
	if ((!pMap) || (pDoc->GetCurAction().size() == 0))
	{
		m_copyedSounds.clear();
		return;
	}
	Nuclear::ActionSoundParamMap::const_iterator it = pMap->find(pDoc->GetCurAction());
	if (it == pMap->end() || it->second.size() == 0)
	{
		m_copyedSounds.clear();
		return;
	}
	m_copyedSounds = it->second;
}

void CPropDlg::OnBnClickedButtonPaste()
{
	CSpriteSoundEditorDoc *pDoc = m_pView->GetDocument();
	if (pDoc->GetCurAction() == L"")
	{
		AfxMessageBox(L"请先选择一个动作");
		return;
	}
	const Nuclear::ModelSoundParam &param = pDoc->GetCurmodelParam();
	Nuclear::SpriteSoundParamVet::iterator it = m_copyedSounds.begin(), ie = m_copyedSounds.end();
	int succCount = 0;
	int oldID = m_comboSound.GetCurSel();
	for (;it!=ie;++it)
	{
		if (pDoc->AddASound(it->soundname) == ASE_OK)
		{
			int nId = m_comboSound.GetCount();
			m_comboSound.InsertString(nId, it->soundname.c_str());
			pDoc->SetCurSound(nId);
			pDoc->SetSoundVol(it->vol);
			pDoc->SetSoundFreq(it->freq);
			++succCount;
		}
	}
	if (succCount > 0)
	{
		if (oldID < 0)
			oldID = 0;
		m_comboSound.SetCurSel(oldID);
		OnChangeSound();
		EnableCtrls(TRUE);
	}
}
