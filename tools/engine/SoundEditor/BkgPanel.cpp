// BkgPanel.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "BkgPanel.h"
#include "PropView.h"
#include "SoundEditorDoc.h"
#include "operators.h"
#include "AudioFileDialog.h"

// CBkgPanel dialog

IMPLEMENT_DYNAMIC(CBkgPanel, CDialog)

CBkgPanel::CBkgPanel(CWnd* pParent /*=NULL*/)
	: CDialog(CBkgPanel::IDD, pParent), m_pView(NULL), m_bIsInited(false)
	, m_nVol(0)
	, m_nFadeIn(0)
	, m_nFadeOut(0)
{

}

CBkgPanel::~CBkgPanel()
{
}

void CBkgPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_SOUND_LIST, m_listSound);
	DDX_Control(pDX, IDC_BUTTON_UP, m_ctrlUp);
	DDX_Control(pDX, IDC_BUTTON_DOWN, m_ctrlDown);
	DDX_Control(pDX, IDC_ADD_SOUND, m_buttonAddSound);
	DDX_Control(pDX, IDC_DEL_SOUND, m_buttonDelSound);
	DDX_Control(pDX, IDC_STATIC_VOL, m_staticVol);
	DDX_Control(pDX, IDC_SLIDER_VOL, m_silderVol);
	DDX_Control(pDX, IDC_EDIT_VOL, m_editVol);
	DDX_Control(pDX, IDC_EDIT_MIN_INTERVAL, m_editMinInterval);
	DDX_Control(pDX, IDC_EDIT_MAX_INTERVAL, m_editMaxInterval);
	DDX_Control(pDX, IDC_CHECK1, m_checkWaitPrevArea);
	DDX_Control(pDX, IDC_PLAY_MODE_STATIC, m_staticPlayMode);
	DDX_Control(pDX, IDC_COMBO_PLAY_MODE, m_comboPlayMode);
	DDX_Control(pDX, IDC_STATIC_FADE_IN, m_staticFadeIn);
	DDX_Control(pDX, IDC_EDIT1, m_editFadeIn);
	DDX_Control(pDX, IDC_STATIC_FADE_OUT, m_staticFadeOut);
	DDX_Control(pDX, IDC_EDIT2, m_editFadeOut);
	DDX_Text(pDX, IDC_EDIT_VOL, m_nVol);
	DDX_Text(pDX, IDC_EDIT_MIN_INTERVAL, m_nMinInterval);
	DDX_Text(pDX, IDC_EDIT_MAX_INTERVAL, m_nMaxInterval);
	DDX_Text(pDX, IDC_EDIT1, m_nFadeIn);
	DDX_Text(pDX, IDC_EDIT2, m_nFadeOut);
	DDV_MinMaxInt(pDX, m_nFadeIn, 0, 65535);
	DDV_MinMaxInt(pDX, m_nFadeOut, 0, 65535);
}

void CBkgPanel::ResetSize() 
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		rect.DeflateRect(5,5,5,5);
		CRect tmpRect = rect;
		tmpRect.bottom = rect.top + 20;
		tmpRect.right = rect.right - 55;
		tmpRect.left = tmpRect.right - 50;
		m_editMinInterval.MoveWindow(&tmpRect);
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = rect.right;
		m_editMaxInterval.MoveWindow(&tmpRect);
		tmpRect.top = tmpRect.bottom + 8;
		tmpRect.bottom = tmpRect.top + 17;
		tmpRect.left = rect.left;
		tmpRect.right = tmpRect.left + 33;
		m_staticFadeIn.MoveWindow(&tmpRect);
		tmpRect.top -= 3;
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = tmpRect.left + 50;
		m_editFadeIn.MoveWindow(&tmpRect);
		tmpRect.top += 3;
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = tmpRect.left + 33;
		m_staticFadeOut.MoveWindow(&tmpRect);
		tmpRect.top -= 3;
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = tmpRect.left + 50;
		m_editFadeOut.MoveWindow(&tmpRect);
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = tmpRect.left + 80;
		m_checkWaitPrevArea.MoveWindow(&tmpRect);
		tmpRect.top = tmpRect.bottom + 8;
		tmpRect.bottom = tmpRect.top + 18;
		tmpRect.left = rect.left;
		tmpRect.right = tmpRect.left + 60;
		m_staticPlayMode.MoveWindow(&tmpRect);
		tmpRect.top -= 3;
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = rect.right;
		m_comboPlayMode.MoveWindow(&tmpRect);
		tmpRect.top = tmpRect.bottom + 5;
		tmpRect.bottom = tmpRect.top + 100;
		tmpRect.right = rect.right - 30;
		tmpRect.left = rect.left;
		m_listSound.MoveWindow(&tmpRect);
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = rect.right;
		tmpRect.bottom = tmpRect.top + 20;
		m_ctrlUp.MoveWindow(&tmpRect);
		tmpRect.bottom = tmpRect.top + 100;
		tmpRect.top = tmpRect.bottom - 20;
		m_ctrlDown.MoveWindow(&tmpRect);
		tmpRect.top = tmpRect.bottom + 5;
		tmpRect.left = rect.left;
		tmpRect.right = tmpRect.left + 80;
		tmpRect.bottom = tmpRect.top + 20;
		m_buttonAddSound.MoveWindow(&tmpRect);
		tmpRect.right = rect.right;
		tmpRect.left = tmpRect.right - 80;
		m_buttonDelSound.MoveWindow(&tmpRect);
		tmpRect.left = rect.left;
		tmpRect.top = tmpRect.bottom + 5;
		tmpRect.right = tmpRect.left + 30;
		tmpRect.bottom = tmpRect.top + 20;
		m_staticVol.MoveWindow(&tmpRect);
		tmpRect.right = rect.right;
		tmpRect.left = tmpRect.right - 40;
		m_editVol.MoveWindow(&tmpRect);
		tmpRect.right = tmpRect.left - 5;
		tmpRect.left = rect.left + 35;
		m_silderVol.MoveWindow(&tmpRect);
		tmpRect.left = rect.left;
		tmpRect.right = rect.right;
		tmpRect.top = tmpRect.bottom + 105;
		tmpRect.bottom = rect.bottom;
		m_AllSoundListPanel.MoveWindow(&tmpRect);
		m_AllSoundListPanel.BringWindowToTop();
	}
}

void CBkgPanel::EnableSoundPropCtrls(BOOL b)
{
	m_editVol.EnableWindow(b);
	m_silderVol.EnableWindow(b);
}

void CBkgPanel::ResetSoundList()
{
	m_listSound.ResetContent();
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	CToolsBkgSoundArea *pArea = pDoc->m_pSelectedBkgArea?pDoc->m_pSelectedBkgArea:&pDoc->m_defaultBkgArea;
	m_nMaxInterval = pArea->GetMaxInterval();
	m_nMinInterval = pArea->GetMinInterval();
	m_nFadeIn = pArea->GetFadeInTime();
	m_nFadeOut = pArea->GetFadeOutTime();
	m_checkWaitPrevArea.SetCheck(!pArea->IsWaitPrevAreaPlayEnd());
	m_comboPlayMode.SetCurSel(pArea->GetPlayMode());
	std::vector<std::wstring> slist;
	pArea->GetSoundList(slist);
	std::vector<std::wstring>::iterator it = slist.begin(), ie = slist.end();
	int i=0, check;
	for (;it!=ie;++it,++i)
	{
		m_listSound.InsertString(i, it->c_str());
		switch (pArea->GetSoundType(i))
		{
		case Nuclear::XPSA_DAY:
			check = 1;
			break;
		case Nuclear::XPSA_NIGHT:
			check = 2;
			break;
		case Nuclear::XPSA_BOTH:
			check = 0;
			break;
		}
		m_listSound.SetCheck(i, check);
	}
	if (i==0)	//没有东西
	{
		EnableSoundPropCtrls(FALSE);
	} else {
		EnableSoundPropCtrls(TRUE);
		if (pDoc->GetUpdateSource() == US_UNDOREDO && pDoc->m_pCurOperator &&
			pDoc->m_pCurOperator->GetTabEditType() == EDIT_STATE_BKG_MUSIC)
		{
			int soundID = pDoc->m_pCurOperator->GetSoundID(pDoc->m_nCurOperatorType);
			m_listSound.SetCurSel(soundID>0?soundID:0);
		} else {
			m_listSound.SetCurSel(0);
		}
	}
	UpdateData(FALSE);
	ResetSoundProp();
}
void CBkgPanel::ResetSoundProp()
{
	if (m_listSound.GetCount() != 0)
	{
		EnableSoundPropCtrls(TRUE);
		CSoundEditorDoc* pDoc = m_pView->GetDocument();
		CToolsBkgSoundArea *pArea = pDoc->m_pSelectedBkgArea;
		if (!pArea)
		{
			pArea = &(pDoc->m_defaultBkgArea);
		}
		int soundID = m_listSound.GetCurSel();
		m_nVol = Vol255to100(pArea->GetVol(soundID));
		m_silderVol.SetPos(m_nVol);
		UpdateData(FALSE);
	} else {
		EnableSoundPropCtrls(FALSE);
	}
}

void CBkgPanel::ShowPolygonProp(BOOL b)
{
	GetDlgItem(IDC_RADIO_POLYGON_NONE)->ShowWindow(b?SW_SHOW:SW_HIDE);
	GetDlgItem(IDC_RADIO_POLYGON_BASE)->ShowWindow(b?SW_SHOW:SW_HIDE);
	GetDlgItem(IDC_RADIO_POLYGON_CUT)->ShowWindow(b?SW_SHOW:SW_HIDE);
	GetDlgItem(IDC_BUTTON_DELETE_CUTS)->ShowWindow(b?SW_SHOW:SW_HIDE);
	if (b)
	{
		CheckRadioButton(IDC_RADIO_POLYGON_NONE, IDC_RADIO_POLYGON_CUT, IDC_RADIO_POLYGON_NONE);
		m_pView->GetDocument()->m_nPolygonEditState = 0;
		m_pView->GetDocument()->m_PolygonMouseState = PMS_NORMAL;
	}
}

typedef struct _TmpInfo
{
	DWORD_PTR handle;
	std::wstring str;
} TmpInfo;

void CBkgPanel::ResetCont()
{
	if (m_bIsInited)
	{
		CSoundEditorDoc* pDoc = m_pView->GetDocument();
		static int sign = UVS_BKG_SOUND_AREA_SELECTED | UVS_TAB | UVS_LOADMAP | UVS_BKG_SOUND_AREA_ADD_DELETED;
		if (pDoc->m_nUpdateViewSign & sign)
		{
			if (pDoc->m_pSelectedBkgArea)
			{
				ShowPolygonProp(pDoc->m_pSelectedBkgArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON);
			} else {
				ShowPolygonProp(FALSE);
			}
			ResetSoundList();
			if (pDoc->m_nUpdateViewSign & UVS_LOADMAP)
			{
				m_AllSoundListPanel.ClearAll();
				const BkgAreaCont& cont = pDoc->GetBkgArea();
				std::vector<TmpInfo> infoVet;
				infoVet.resize(pDoc->GetBkgSoundCount());
				BkgAreaCont::const_iterator bkgIt = cont.begin(), bkgIe = cont.end();
				int i = 0, count = 0, soundID;
				const CToolsBkgSoundArea *pArea = &pDoc->m_defaultBkgArea;
				i=pArea->GetBeginID();
				std::vector<std::wstring> slist;
				pArea->GetSoundList(slist);
				std::vector<std::wstring>::iterator soundIt = slist.begin(), soundIe = slist.end();
				for (soundID=0;soundIt!=soundIe;++soundIt,++soundID,++i, ++count)
				{
					infoVet[i].handle = pArea->GetHandle(soundID);
					infoVet[i].str = *soundIt;
				}
				assert(i==pArea->GetEndID());
				for (;bkgIt!=bkgIe;++bkgIt)
				{
					const CToolsBkgSoundArea *pArea = *bkgIt;
					i=pArea->GetBeginID();
					std::vector<std::wstring> slist;
					pArea->GetSoundList(slist);
					std::vector<std::wstring>::iterator soundIt = slist.begin(), soundIe = slist.end();
					for (soundID=0;soundIt!=soundIe;++soundIt,++soundID,++i, ++count)
					{
						infoVet[i].handle = pArea->GetHandle(soundID);
						infoVet[i].str = *soundIt;
					}
					assert(i==pArea->GetEndID());
				}
				assert(count == (int)infoVet.size());
				std::vector<TmpInfo>::iterator infoIt = infoVet.begin(), infoIe = infoVet.end();
				for (i=0;infoIt!=infoIe;++infoIt,++i)
				{
					m_AllSoundListPanel.InsertSound(i, infoIt->handle, infoIt->str);
				}
			}
			if (pDoc->GetUpdateSource() == US_UNDOREDO && pDoc->m_pCurOperator &&
				pDoc->m_pCurOperator->GetTabEditType() == EDIT_STATE_BKG_MUSIC)
			{
				switch (pDoc->m_pCurOperator->GetType())
				{
				case CAction::ADD_DEL_BKG_AREA:
					{
						AddDelBkgArea* pOper = dynamic_cast<AddDelBkgArea*>(pDoc->m_pCurOperator);
						bool isAdd = pOper->IsCurrentAdd(pDoc->m_nCurOperatorType);
						int i=pOper->pArea->GetBeginID();
						int soundID;
						std::vector<std::wstring> slist;
						pOper->pArea->GetSoundList(slist);
						std::vector<std::wstring>::iterator soundIt = slist.begin(), soundIe = slist.end();
						for (soundID=0;soundIt!=soundIe;++soundIt,++soundID,++i)
						{
							DWORD_PTR handle = pOper->pArea->GetHandle(soundID);
							if (isAdd)
								m_AllSoundListPanel.InsertSound(pOper->allSoundListIDMap[handle], handle, *soundIt);
							else
								m_AllSoundListPanel.RemoveSound(handle);
						}
					}
					break;
				case CAction::ADD_DEL_BKG_SOUND:
					{
						AddDelBkgSound* pOper = dynamic_cast<AddDelBkgSound*>(pDoc->m_pCurOperator);
						if (pOper->IsCurrentAdd(pDoc->m_nCurOperatorType))
						{
							m_AllSoundListPanel.InsertSound(pOper->nAllSoundListID, pOper->handle, pOper->param.soundName);
						} else {
							m_AllSoundListPanel.RemoveSound(pOper->handle);
						}
					}
					break;
				default:
					break;
				}
			}
			if (pDoc->m_nUpdateViewSign & UVS_BKG_SOUND_AREA_ADD_DELETED)
			{
				CToolsSoundArea::HandleVet::const_iterator iter = pDoc->GetDeletedHandleVet().begin(), 
					iterEnd = pDoc->GetDeletedHandleVet().end();
				AddDelBkgArea *pOper = dynamic_cast<AddDelBkgArea*>(pDoc->m_pCurOperator);
				for (;iter!=iterEnd;++iter)
				{
					UINT id = m_AllSoundListPanel.RemoveSound(*iter);
					if (pOper)
						pOper->allSoundListIDMap[*iter] = id;
				}
			}
		}
	}
}

BEGIN_MESSAGE_MAP(CBkgPanel, CDialog)
	ON_CLBN_CHKCHANGE(IDC_SOUND_LIST, &CBkgPanel::OnLbnCheckChangeSoundList)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_BN_CLICKED(IDC_ADD_SOUND, &CBkgPanel::OnBnClickedAddSound)
	ON_CONTROL_RANGE(BN_CLICKED, IDC_RADIO_POLYGON_NONE, IDC_RADIO_POLYGON_CUT, &CBkgPanel::OnBnClickedRadioPolygonRadio)
	ON_BN_CLICKED(IDC_DEL_SOUND, &CBkgPanel::OnBnClickedDelSound)
	ON_BN_CLICKED(IDC_BUTTON_UP, &CBkgPanel::OnBnClickedButtonUp)
	ON_BN_CLICKED(IDC_BUTTON_DOWN, &CBkgPanel::OnBnClickedButtonDown)
	ON_LBN_SELCHANGE(IDC_SOUND_LIST, &CBkgPanel::OnLbnSelchangeSoundList)
	ON_WM_HSCROLL()
	ON_EN_KILLFOCUS(IDC_EDIT_VOL, &CBkgPanel::OnEnKillfocusEditVol)
	ON_NOTIFY(NM_RELEASEDCAPTURE, IDC_SLIDER_VOL, &CBkgPanel::OnNMReleasedcaptureSliderVol)
	ON_BN_CLICKED(IDC_BUTTON_DELETE_CUTS, &CBkgPanel::OnBnClickedButtonDeleteCuts)
	ON_EN_KILLFOCUS(IDC_EDIT_MAX_INTERVAL, &CBkgPanel::OnEnKillfocusEditMaxInterval)
	ON_EN_KILLFOCUS(IDC_EDIT_MIN_INTERVAL, &CBkgPanel::OnEnKillfocusEditMinInterval)
	ON_BN_CLICKED(IDC_CHECK1, &CBkgPanel::OnBnClickedCheck1)
	ON_CBN_SELCHANGE(IDC_COMBO_PLAY_MODE, &CBkgPanel::OnCbnSelchangeComboPlayMode)
	ON_EN_KILLFOCUS(IDC_EDIT1, &CBkgPanel::OnEnKillfocusEdit1)
	ON_EN_KILLFOCUS(IDC_EDIT2, &CBkgPanel::OnEnKillfocusEdit2)
END_MESSAGE_MAP()


// CBkgPanel message handlers

BOOL CBkgPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_listSound.SetCheckStyle(BS_AUTO3STATE);
	m_pView = static_cast<CPropView*>(GetParent());
	m_silderVol.SetRange(0, 100, FALSE);
	m_silderVol.SetPos(m_nVol);
	m_AllSoundListPanel.Create(m_AllSoundListPanel.IDD, this);
	m_AllSoundListPanel.SetPanel(this);
	m_AllSoundListPanel.ShowWindow(SW_SHOW);
	m_comboPlayMode.InsertString(0, L"循环（除第一首）");
	m_comboPlayMode.InsertString(1, L"随机（可重复）");
	m_comboPlayMode.InsertString(2, L"随机（不重复）");
	m_comboPlayMode.InsertString(3, L"顺序（从头开始）");
	m_comboPlayMode.InsertString(4, L"顺序（当前）");
	m_comboPlayMode.InsertString(5, L"序列（无缝）");
	m_bIsInited = true;
	ResetCont();
	ResetSize();

	return FALSE;
}

void CBkgPanel::OnLbnCheckChangeSoundList()
{
	if (m_listSound.GetCount() != 0)
	{
		int soundID = m_listSound.GetCurSel();
		Nuclear::XPSA_SOUND_TYPE type;
		switch (m_listSound.GetCheck(soundID))
		{
		case 0:
			type = Nuclear::XPSA_BOTH;
			break;
		case 1:
			type = Nuclear::XPSA_DAY;
			break;
		case 2:
			type = Nuclear::XPSA_NIGHT;
			break;
		}
		m_pView->GetDocument()->SetSelectedAreaSoundType(soundID, type);

	}
}

void CBkgPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

void CBkgPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CBkgPanel::OnBnClickedRadioPolygonRadio(UINT nID)
{
	m_pView->GetDocument()->m_nPolygonEditState = nID - IDC_RADIO_POLYGON_NONE;
	m_pView->GetDocument()->m_PolygonMouseState = PMS_NORMAL;
}

void CBkgPanel::OnBnClickedAddSound()
{
	CSoundEditorDoc *pDoc = m_pView->GetDocument();
	if (!pDoc)
		return;
	XPASSERT(pDoc->GetEngineBase());
	wchar_t olddir[MAX_PATH];
	GetCurrentDirectory(MAX_PATH, olddir);
	CAudioFileDialog dlg(pDoc->GetEngineBase()->GetSoundManager(), 
		TRUE, NULL, NULL, OFN_FILEMUSTEXIST, L"声音文件 (*.*|*.*||)");
	CString dir = m_pView->GetDocument()->m_lastBkgOpenUrl.IsEmpty()?theApp.GetExePath() + L"\\res\\sound":m_pView->GetDocument()->m_lastBkgOpenUrl;
	dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
	if (dlg.DoModal() == IDOK)
	{
		m_pView->GetDocument()->m_lastBkgOpenUrl = dlg.GetPathName();
		std::wstring name;
		if (PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name))
		{
			SoundInfo info;
			int soundID = m_pView->GetDocument()->AddSound2SelectedArea(name, info);
			m_listSound.AddString(name.c_str());
			m_listSound.SetCurSel(soundID);
			m_AllSoundListPanel.InsertSound(info.nID, info.handle, name);
			ResetSoundProp();
		} else {
			AfxMessageBox(L"请选择正确路径（res\\sound）下面的声音文件！");
		}
	}
	SetCurrentDirectory(olddir);
}

void CBkgPanel::OnBnClickedDelSound()
{
	int soundID = m_listSound.GetCurSel();
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	CToolsBkgSoundArea* pArea = pDoc->m_pSelectedBkgArea?pDoc->m_pSelectedBkgArea:(&(pDoc->m_defaultBkgArea));
	DWORD_PTR handle = pArea->GetHandle(soundID);
	UINT nID = m_AllSoundListPanel.RemoveSound(handle);
	pDoc->DeleteSelectedAreaSound(soundID, nID);
	
	m_listSound.DeleteString(soundID);
	if (soundID > m_listSound.GetCount() - 1)
	{
		soundID = m_listSound.GetCount() - 1;
	}
	if (soundID >= 0)
	{
		m_listSound.SetCurSel(soundID);
	}
	ResetSoundProp();
}

void CBkgPanel::OnBnClickedButtonUp()
{
	// TODO: Add your control notification handler code here
}

void CBkgPanel::OnBnClickedButtonDown()
{
	// TODO: Add your control notification handler code here
}

void CBkgPanel::OnLbnSelchangeSoundList()
{
	ResetSoundProp();
}

void CBkgPanel::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	if (pScrollBar->GetDlgCtrlID() == IDC_SLIDER_VOL)
	{
		m_nVol = m_silderVol.GetPos();
		UpdateData(FALSE);
	}
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CBkgPanel::OnEnKillfocusEditVol()
{
	int oldVol = m_nVol;
	UpdateData(TRUE);
	if (m_nVol >=0 && m_nVol <= 100)
	{
		m_silderVol.SetPos(m_nVol);
		m_pView->GetDocument()->SetSelectedAreaVol(m_listSound.GetCurSel(), Vol100to255(m_nVol));
	} else {
		m_nVol = oldVol;
		UpdateData(FALSE);
	}
}

void CBkgPanel::OnNMReleasedcaptureSliderVol(NMHDR *pNMHDR, LRESULT *pResult)
{
	m_pView->GetDocument()->SetSelectedAreaVol(m_listSound.GetCurSel(), Vol100to255(m_nVol));
	*pResult = 0;
}

void CBkgPanel::OnBnClickedButtonDeleteCuts()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	CToolsBkgSoundArea* pArea = pDoc->m_pSelectedBkgArea?pDoc->m_pSelectedBkgArea:(&(pDoc->m_defaultBkgArea));
	pDoc->ClearPolygonCuts(pArea);
}

void CBkgPanel::SelectASound(DWORD_PTR handle)
{
	m_pView->GetDocument()->SelectAndMoveToAnBkgArea(handle);
}
void CBkgPanel::OnEnKillfocusEditMaxInterval()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	UpdateData();
	pDoc->SetSelectedAreaMaxInterval(m_nMaxInterval);
}

void CBkgPanel::OnEnKillfocusEditMinInterval()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	UpdateData();
	pDoc->SetSelectedAreaMinInterval(m_nMinInterval);
}

void CBkgPanel::OnBnClickedCheck1()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	pDoc->SetSelectedAreaWaitPrevArea(m_checkWaitPrevArea.GetCheck() == 0);
}

void CBkgPanel::OnCbnSelchangeComboPlayMode()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	pDoc->SetSelectedAreaPlayMode(static_cast<Nuclear::XPBGM_PLAY_TYPE>(m_comboPlayMode.GetCurSel()));
}

void CBkgPanel::OnEnKillfocusEdit1()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	UpdateData();
	pDoc->SetSelectedAreaFadeIn(m_nFadeIn);
}

void CBkgPanel::OnEnKillfocusEdit2()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	UpdateData();
	pDoc->SetSelectedAreaFadeOut(m_nFadeOut);
}
