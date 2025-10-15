

#include "stdafx.h"
#include "MapEditor.h"
#include "../engine/map/pmap.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "MaskEditorDlg.h"




IMPLEMENT_DYNAMIC(CMaskEditorDlg, CDialog)

BOOL CMaskEditorDlg::PreTranslateMessage(MSG* pMsg)
{
	if(pMsg->message == WM_KEYDOWN && IsCTRLPressed())
	{
		if (pMsg->wParam == 'Z')
		{
			m_pMapEditorView->UnDo();
			return TRUE;
		} else if (pMsg->wParam == 'Y')
		{
			m_pMapEditorView->ReDo();
			return TRUE;
		}
	}

	return CDialog::PreTranslateMessage(pMsg);
}

void CMaskEditorDlg::OnEnKillfocusEdit2()
{
	std::wstring oldUri = m_MaskParam.picuri;
	UpdateData();
	if (oldUri != m_MaskParam.picuri)
	{
		ModifyParams();
	}
}

void CMaskEditorDlg::OnCbnSelchangeCombo1()
{
	m_MaskParam.blendMode = static_cast<Nuclear::XPTEXTURE_BLENDMODE>(m_comboBlend.GetCurSel());
	ModifyParams();
}

void CMaskEditorDlg::OnBnClickedCheck1()
{
	SetPictureItemState(m_btnNewPicture.GetCheck() == TRUE);
	if (!m_bNewPicture)
	{
		m_MaskParam.picuri = L"";
		m_MaskParam.lastAlpha = m_MaskParam.alpha;
		UpdateData(FALSE);
		UpdateAllParamControls();
	}
	ModifyParams();
}

void CMaskEditorDlg::OnBnClickedButton1()
{
	CFileDialog dlg(TRUE, NULL, NULL, OFN_FILEMUSTEXIST, L"图片文件 (*.jpg, *.png, *.dds, *.tga)|*.jpg;*.png;*.dds;*.tga||");
	CString dir = theApp.GetResPath() + L"\\map";
	dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
	if (dlg.DoModal() == IDOK)
	{
		std::wstring name;
		PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name);
		if (name.substr(0, 5) != L"/map/")
		{
			AfxMessageBox(L"请选择\n" + dir + L"\n下面的文件!");
			return;
		}
		m_MaskParam.picuri = name;
		UpdateData(FALSE);
		ModifyParams();
	}
}

void CMaskEditorDlg::SetPictureItemState(bool isNewPic)
{
	m_bNewPicture = isNewPic;
	m_btnBrowse.EnableWindow(isNewPic);
	m_editPictureUrl.EnableWindow(isNewPic);
	m_sliderLastAlpha.EnableWindow(isNewPic);
	m_editLastAlpha.EnableWindow(isNewPic);
}

void CMaskEditorDlg::Reset()
{
	ResetKeyFrames();
	GetCurrentTimeBarParam();
}

void CMaskEditorDlg::OnShowWindow(BOOL bShow, UINT nStatus)
{
	CDialog::OnShowWindow(bShow, nStatus);
	m_pMapEditorView->SetSettingMask(bShow == TRUE);
}

void CMaskEditorDlg::OnCancel()
{
	m_pMapEditorView->SetSettingMask(false);
	CDialog::OnCancel();
}

void CMaskEditorDlg::OnBnClickedStop()
{
	m_btnStop.EnableWindow(false);
	m_pMapEditorView->SetPlayingTime(false);
}

void CMaskEditorDlg::SetPlay(bool isPlay)
{
	m_btnStop.EnableWindow(isPlay);
}

void CMaskEditorDlg::OnBnClickedPlayQuickly()
{
	m_pMapEditorView->SetGameTimeScaleFactor(50);
	m_pMapEditorView->SetPlayingTime(true);
	m_pMapEditorView->SetGameTime(m_sliderTimeBar.GetPos());
	m_btnStop.EnableWindow(true);
}

void CMaskEditorDlg::OnBnClickedPlay()
{
	m_pMapEditorView->SetGameTimeScaleFactor(0.5);
	m_pMapEditorView->SetPlayingTime(true);
	m_pMapEditorView->SetGameTime(m_sliderTimeBar.GetPos());
	m_btnStop.EnableWindow(true);
}

void CMaskEditorDlg::OnLbnSelchangeList1()
{
	m_sliderTimeBar.SetPos(static_cast<int>(m_listKeyFrames.GetItemData(m_listKeyFrames.GetCurSel())));
	GetCurrentTimeBarParam();
}

void CMaskEditorDlg::OnBnClickedAddRemoveFrame()
{
	int nTime = m_pMapEditorView->GetGameTime();
	CAction *pOperator = new CAction(CAction::AT_MASK_PARAM, m_pMapEditorView->GetLayerEditId());
	CAction::CMaskParamActionAtom* pOper = pOperator->GetMaskParamActionData();
	pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = m_MaskParam;
	pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_nTime = nTime;
	pOper->oper[CAction::CActionAtom::AFTER_OPERATOR] = m_MaskParam;
	pOper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_nTime = nTime;
	if (m_bInKeyFrame)
	{
		m_pMapEditorView->GetDocument()->GetToolsMap().RemoveAMask(nTime);
		pOper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_nTime = -1;
	} else {
		m_pMapEditorView->GetDocument()->GetToolsMap().AddAMask(nTime, m_MaskParam);
		pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_nTime = -1;
	}
	m_pMapEditorView->DoAction(pOperator);
	ResetKeyFrames();
	GetCurrentTimeBarParam();
}

void CMaskEditorDlg::ModifyParams()
{
	Nuclear::PMap &map = m_pMapEditorView->GetDocument()->GetToolsMap();
	const Nuclear::SubMap::MaskParamMap &paramMap = map.GetMaskParams();
	int nTime = m_pMapEditorView->GetGameTime();
	Nuclear::SubMap::MaskParamMap::const_iterator iter = paramMap.find(nTime);
	if (iter == paramMap.end())
		return;
	const Nuclear::XPMaskParam &mask = iter->second;
	if (mask.alpha != m_MaskParam.alpha || mask.lastAlpha != m_MaskParam.lastAlpha
		|| mask.picuri != m_MaskParam.picuri || mask.blendMode != m_MaskParam.blendMode)
	{
		CAction *pOperator = new CAction(CAction::AT_MASK_PARAM, m_pMapEditorView->GetLayerEditId());
		CAction::CMaskParamActionAtom* pOper = pOperator->GetMaskParamActionData();
		pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = mask;
		pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_nTime = nTime;
		pOper->oper[CAction::CActionAtom::AFTER_OPERATOR] = m_MaskParam;
		pOper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_nTime = nTime;
		map.ModifyAMask(nTime, nTime, m_MaskParam);
		m_pMapEditorView->DoAction(pOperator);
	}

}

void CMaskEditorDlg::UpdateParams()
{
	m_sliderTimeBar.SetPos(m_pMapEditorView->GetGameTime());
	GetCurrentTimeBarParam();
}

BOOL CMaskEditorDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_sliderLastAlpha.SetRange(0, 255, TRUE);
	m_sliderAlpha.SetRange(0, 255, TRUE);
	m_sliderTimeBar.SetRange(0, Nuclear::XP_A_DAY_TIME, TRUE);
	m_btnStop.EnableWindow(false);


	m_comboBlend.InsertString(0, L"Alpha混合");
	m_comboBlend.InsertString(1, L"Add");
	m_comboBlend.InsertString(2, L"不透明");
	m_comboBlend.InsertString(3, L"Alpha Add");
	m_comboBlend.InsertString(4, L"2倍Alpha混合");
	m_comboBlend.InsertString(5, L"2倍Add");
	m_comboBlend.InsertString(6, L"正片叠底");
	m_comboBlend.InsertString(7, L"Alpha正片叠底");

	UpdateAllParamControls();
	ResetKeyFrames();
	GetCurrentTimeBarParam();

	return TRUE;
}

void CMaskEditorDlg::GetCurrentTimeBarParam()
{
	const Nuclear::SubMap::MaskParamMap &maskMap = m_pMapEditorView->GetDocument()->GetToolsMap().GetMaskParams();
	Nuclear::SubMap::MaskParamMap::const_iterator iter = maskMap.begin();
	int nTime = m_sliderTimeBar.GetPos();
	m_pMapEditorView->SetGameTime(nTime);
	if (iter == maskMap.end())
	{
		m_MaskParam.alpha = 0;
		m_MaskParam.lastAlpha = 0;
		m_MaskParam.blendMode = Nuclear::XPTEXBM_MUTIPLY_ALPHA;
		m_MaskParam.picuri = L"";
		m_CurPictureName = L"";
		UpdateData(FALSE);
		UpdateAllParamControls();
		m_listKeyFrames.SetCurSel(-1);
		SetItemState(false, false);
		return;
	}
	int listPos = 0;
	Nuclear::SubMap::MaskParamMap::const_iterator preIter = maskMap.end(), preUriIter = maskMap.end();
	preIter--;
	for (;iter!=maskMap.end();iter++,listPos++)
	{
		if (nTime<=iter->first)
			break;
		preIter = iter;
		if (iter->second.picuri != L"")
		{
			preUriIter = iter;
		}
	}

	if (iter == maskMap.end())
	{
		iter = maskMap.begin();
		listPos = 0;
	}
	int length = iter->first - preIter->first;
	if ((length == 0) || (nTime == iter->first))
	{
		m_MaskParam = iter->second;
		if (m_MaskParam.picuri == L"")
		{
			if (preUriIter != maskMap.end())
			{
				m_CurPictureName = preUriIter->second.picuri;
			} else {
				m_CurPictureName = L"";
			}
		} else {
			m_CurPictureName = m_MaskParam.picuri;
		}
		UpdateData(FALSE);
		UpdateAllParamControls();
		m_listKeyFrames.SetCurSel(listPos);
		if (nTime == iter->first)
		{
			SetItemState(true, iter->second.picuri != L"");
		} else {
			SetItemState(false, false);
		}
		return;
	}
	if (length < 0)
		length += Nuclear::XP_A_DAY_TIME;
	int nPos = nTime - preIter->first;
	if (nPos < 0)
		nPos += Nuclear::XP_A_DAY_TIME;
	if (preUriIter != maskMap.end())
	{
		m_CurPictureName = preUriIter->second.picuri;
	} else {
		m_CurPictureName = L"";
	}
	m_MaskParam.alpha = preIter->second.alpha + (iter->second.lastAlpha - preIter->second.alpha) * nPos / length;
	m_MaskParam.lastAlpha = m_MaskParam.alpha;
	m_MaskParam.blendMode = preIter->second.blendMode;
	m_MaskParam.picuri = L"";
	m_listKeyFrames.SetCurSel(-1);
	UpdateData(FALSE);
	UpdateAllParamControls();
	SetItemState(false, false);
	return;
}

void CMaskEditorDlg::SetItemState(bool isKeyFrame, bool isNewPic)
{
	m_sliderLastAlpha.EnableWindow(isKeyFrame);
	m_sliderAlpha.EnableWindow(isKeyFrame);
	m_comboBlend.EnableWindow(isKeyFrame);
	m_editPictureUrl.EnableWindow(isKeyFrame);
	m_btnNewPicture.EnableWindow(isKeyFrame);
	m_btnBrowse.EnableWindow(isKeyFrame);
	m_editLastAlpha.EnableWindow(isKeyFrame);

	GetDlgItem(IDC_EDIT1)->EnableWindow(isKeyFrame);
	GetDlgItem(IDC_EDIT_COEF_G)->EnableWindow(isKeyFrame);

	m_btnAddDelFrame.SetWindowText(isKeyFrame?_T("删除关键帧"):_T("添加关键帧"));
	m_bInKeyFrame = isKeyFrame;
	SetPictureItemState(isNewPic);
}

void CMaskEditorDlg::ResetKeyFrames()
{
	const Nuclear::SubMap::MaskParamMap &maskMap = m_pMapEditorView->GetDocument()->GetToolsMap().GetMaskParams();
	Nuclear::SubMap::MaskParamMap::const_iterator iter = maskMap.begin();
	m_listKeyFrames.ResetContent();
	CString name;
	int nID = 0;
	for (;iter!=maskMap.end();iter++,nID++)
	{
		name.Format(L"%d", iter->first);
		m_listKeyFrames.InsertString(nID, name);
		m_listKeyFrames.SetItemData(nID, iter->first);
	}
}

BOOL CMaskEditorDlg::Create(CMapEditorView* pMapEditorView)
{
	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

void CMaskEditorDlg::UpdateAllParamControls()
{
	m_sliderLastAlpha.SetPos(m_MaskParam.lastAlpha);
	m_sliderAlpha.SetPos(m_MaskParam.alpha);
	m_btnNewPicture.SetCheck(m_MaskParam.picuri != L"");
	m_comboBlend.SetCurSel((int)m_MaskParam.blendMode);

}

void CMaskEditorDlg::OnEnKillfocusEditForSlider(UINT nID)
{
	if (UpdateData(TRUE))
	{
		switch(nID)
		{
		case IDC_EDIT_COEF_R:
			m_sliderLastAlpha.SetPos(m_MaskParam.lastAlpha);
			break;
		case IDC_EDIT_COEF_G:
			m_sliderAlpha.SetPos(m_MaskParam.alpha);
			if (!m_bNewPicture)
			{
				m_MaskParam.lastAlpha = m_MaskParam.alpha;
				m_sliderLastAlpha.SetPos(m_MaskParam.alpha);
			}
			break;
		}
		ModifyParams();
	}
}

void CMaskEditorDlg::OnEnKillfocusEdit1()
{
	int oldTime = m_pMapEditorView->GetGameTime();
	UpdateData(TRUE);
	int newTime = m_pMapEditorView->GetGameTime();
	if (oldTime != newTime)
	{
		CAction *pOperator = new CAction(CAction::AT_MASK_PARAM, m_pMapEditorView->GetLayerEditId());
		CAction::CMaskParamActionAtom* pOper = pOperator->GetMaskParamActionData();
		pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = m_MaskParam;
		pOper->oper[CAction::CActionAtom::BEFORE_OPERATOR].m_nTime = oldTime;
		m_pMapEditorView->GetDocument()->GetToolsMap().ModifyAMask(oldTime, newTime, m_MaskParam);
		pOper->oper[CAction::CActionAtom::AFTER_OPERATOR] = m_MaskParam;
		pOper->oper[CAction::CActionAtom::AFTER_OPERATOR].m_nTime = newTime;
		m_pMapEditorView->DoAction(pOperator);
		m_sliderTimeBar.SetPos(newTime);
		GetCurrentTimeBarParam();
		ResetKeyFrames();
		m_pMapEditorView->GetDocument()->SetModifiedFlag();
	}
}

void CMaskEditorDlg::OnSliderReleasedCapture(UINT nID, NMHDR *pNMHDR, LRESULT *pResult)
{
	ModifyParams();
	*pResult = 0;
}

void CMaskEditorDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	switch (pScrollBar->GetDlgCtrlID())
	{
	case IDC_SLIDER1:
		GetCurrentTimeBarParam();
		break;
	case IDC_SLIDER_COEF_R:
		m_MaskParam.lastAlpha = m_sliderLastAlpha.GetPos();
		break;
	case IDC_SLIDER_COEF_G:
		m_MaskParam.alpha = m_sliderAlpha.GetPos();
		if (!m_bNewPicture)
		{
			m_MaskParam.lastAlpha = m_MaskParam.alpha;
			m_sliderLastAlpha.SetPos(m_MaskParam.alpha);
		}
		break;
	}
	UpdateData(FALSE);
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

BEGIN_MESSAGE_MAP(CMaskEditorDlg, CDialog)
	ON_BN_CLICKED(IDC_PLAY, &CMaskEditorDlg::OnBnClickedPlay)
	ON_WM_HSCROLL()
	ON_NOTIFY_RANGE(NM_RELEASEDCAPTURE, IDC_SLIDER_COEF_R, IDC_SLIDER_COEF_G, &CMaskEditorDlg::OnSliderReleasedCapture)
	ON_EN_KILLFOCUS(IDC_EDIT1, &CMaskEditorDlg::OnEnKillfocusEdit1)
	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_COEF_R, IDC_EDIT_COEF_G, &CMaskEditorDlg::OnEnKillfocusEditForSlider)
	ON_BN_CLICKED(IDC_ADD_REMOVE_FRAME, &CMaskEditorDlg::OnBnClickedAddRemoveFrame)
	ON_LBN_SELCHANGE(IDC_LIST1, &CMaskEditorDlg::OnLbnSelchangeList1)
	ON_BN_CLICKED(IDC_PLAY_QUICKLY, &CMaskEditorDlg::OnBnClickedPlayQuickly)
	ON_BN_CLICKED(IDC_STOP, &CMaskEditorDlg::OnBnClickedStop)
	ON_WM_SHOWWINDOW()
	ON_BN_CLICKED(IDC_BUTTON1, &CMaskEditorDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_CHECK1, &CMaskEditorDlg::OnBnClickedCheck1)
	ON_CBN_SELCHANGE(IDC_COMBO1, &CMaskEditorDlg::OnCbnSelchangeCombo1)
	ON_EN_KILLFOCUS(IDC_EDIT2, &CMaskEditorDlg::OnEnKillfocusEdit2)
END_MESSAGE_MAP()

void CMaskEditorDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	int nTime = 0;
	CString tmpUri;
	if ((!pDX->m_bSaveAndValidate) && m_pMapEditorView)
	{
		nTime = m_pMapEditorView->GetGameTime();
		tmpUri = m_MaskParam.picuri.c_str();
	}
	DDX_Text(pDX, IDC_EDIT1, nTime);
	DDX_Text(pDX, IDC_EDIT2, tmpUri);
	if (pDX->m_bSaveAndValidate && m_pMapEditorView)
	{
		if (nTime >= 0 && nTime < Nuclear::XP_A_DAY_TIME)
		{
			m_pMapEditorView->SetGameTime(nTime);
		}
		m_MaskParam.picuri = tmpUri.GetBuffer();
		tmpUri.ReleaseBuffer();
	}
	DDV_MinMaxInt(pDX, nTime, 0, Nuclear::XP_A_DAY_TIME);
	DDX_Control(pDX, IDC_LIST1, m_listKeyFrames);
	DDX_Control(pDX, IDC_CHECK1, m_btnNewPicture);
	DDX_Control(pDX, IDC_EDIT2, m_editPictureUrl);
	DDX_Control(pDX, IDC_BUTTON1, m_btnBrowse);
	DDX_Control(pDX, IDC_COMBO1, m_comboBlend);
	DDX_Control(pDX, IDC_SLIDER_COEF_R, m_sliderLastAlpha);
	DDX_Control(pDX, IDC_SLIDER_COEF_G, m_sliderAlpha);
	DDX_Control(pDX, IDC_EDIT_COEF_R, m_editLastAlpha);
	DDX_Control(pDX, IDC_SLIDER1, m_sliderTimeBar);
	DDX_Control(pDX, IDC_STOP, m_btnStop);
	DDX_Text(pDX, IDC_EDIT_COEF_R, m_MaskParam.lastAlpha);
	DDX_Text(pDX, IDC_EDIT_COEF_G, m_MaskParam.alpha);
	DDX_Control(pDX, IDC_ADD_REMOVE_FRAME, m_btnAddDelFrame);
}

CMaskEditorDlg::~CMaskEditorDlg()
{
}

CMaskEditorDlg::CMaskEditorDlg(CWnd* pParent /*=NULL*/)
: CDialog(CMaskEditorDlg::IDD, pParent)
, m_pMapEditorView(NULL)
, m_bInKeyFrame(false)
, m_bNewPicture(false)
{
	m_MaskParam.alpha = 0;
	m_MaskParam.lastAlpha = 0;
	m_MaskParam.blendMode = Nuclear::XPTEXBM_MUTIPLY_ALPHA;
	m_MaskParam.picuri = L"";
}

