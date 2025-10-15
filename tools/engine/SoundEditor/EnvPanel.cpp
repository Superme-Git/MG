// EnvPanel.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "EnvPanel.h"
#include "PropView.h"
#include "SoundEditorDoc.h"
#include "operators.h"
#include "AudioFileDialog.h"
#include "..\engine\common\log.h"

// CEnvPanel dialog

IMPLEMENT_DYNAMIC(CEnvPanel, CDialog)

CEnvPanel::CEnvPanel(CWnd* pParent /*=NULL*/)
	: CDialog(CEnvPanel::IDD, pParent), m_pView(NULL), m_bIsInited(false), m_nMax(-1)
	, m_nPriority(0)
	, m_bIsLooping(FALSE)
{
	m_nSoundProp[0] = m_nSoundProp[1] = 100;
	m_nSoundProp[2] = m_nSoundProp[3] = 0;
	m_nBeginTime = 0;
	m_nEndTime = SE_EDITOR_TIME_A_DAY;
	m_nFadeInTime = 0;
	m_nFadeOutTime = 0;
	m_nTransition = 0;
	m_nMinInterval = 0;
	m_nMaxInterval = 0;
}

CEnvPanel::~CEnvPanel()
{
}

void CEnvPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_ADD_SOUND, m_buttonAddSound);
	DDX_Control(pDX, IDC_DEL_SOUND, m_buttonDelSound);
	DDX_Control(pDX, IDC_SOUND_LIST, m_listSound);
	DDX_Control(pDX, IDC_SLIDER_MAX_V, m_silderSoundProp[0]);
	DDX_Control(pDX, IDC_EDIT_MAX_V, m_editorSoundProp[0]);
	DDX_Control(pDX, IDC_SLIDER_MIN_V, m_silderSoundProp[1]);
	DDX_Control(pDX, IDC_EDIT_MIN_V, m_editorSoundProp[1]);
	DDX_Control(pDX, IDC_SLIDER_MAX_F, m_silderSoundProp[2]);
	DDX_Control(pDX, IDC_EDIT_MAX_F, m_editorSoundProp[2]);
	DDX_Control(pDX, IDC_SLIDER_MIN_F, m_silderSoundProp[3]);
	DDX_Control(pDX, IDC_EDIT_MIN_F, m_editorSoundProp[3]);
	DDX_Control(pDX, IDC_EDIT_L, m_editL);
	DDX_Control(pDX, IDC_EDIT_MIN_INTERVAL, m_editMinInterval);
	DDX_Control(pDX, IDC_EDIT_MAX_INTERVAL, m_editMaxInterval);
	DDX_Control(pDX, IDC_COMBO_TYPE, m_comboType);
	DDX_Control(pDX, IDC_BUTTON_ADD_TYPE, m_bottomAddType);
	DDX_Control(pDX, IDC_BUTTON_DEL_TYPE, m_bottomDelType);
	DDX_Control(pDX, IDC_EDIT_PRIORITY, m_editPriority);
	DDX_Control(pDX, IDC_COMBO_SOUND_TYPE, m_comboSoundType);
	DDX_Control(pDX, IDC_CHECK_LOOPING, m_checkLooping);
	DDX_Control(pDX, IDC_EDIT_BEGIN_TIME, m_editBeginTime);
	DDX_Control(pDX, IDC_EDIT_END_TIME, m_editEndTime);
	DDX_Control(pDX, IDC_EDIT_FADE_IN_TIME, m_editFadeInTime);
	DDX_Control(pDX, IDC_EDIT_FADE_OUT_TIME, m_editFadeOutTime);
	DDX_Check(pDX, IDC_CHECK_LOOPING, m_bIsLooping);
	DDX_Text(pDX, IDC_EDIT_MAX_V, m_nSoundProp[0]);
	DDX_Text(pDX, IDC_EDIT_MIN_V, m_nSoundProp[1]);
	DDX_Text(pDX, IDC_EDIT_MAX_F, m_nSoundProp[2]);
	DDX_Text(pDX, IDC_EDIT_MIN_F, m_nSoundProp[3]);
	DDX_Text(pDX, IDC_EDIT_L, m_nTransition);
	DDX_Text(pDX, IDC_EDIT_MIN_INTERVAL, m_nMinInterval);
	DDX_Text(pDX, IDC_EDIT_MAX_INTERVAL, m_nMaxInterval);
	DDX_Text(pDX, IDC_EDIT_PRIORITY, m_nPriority);
	DDX_Text(pDX, IDC_EDIT_BEGIN_TIME, m_nBeginTime);
	DDX_Text(pDX, IDC_EDIT_END_TIME, m_nEndTime);
	DDX_Text(pDX, IDC_EDIT_FADE_IN_TIME, m_nFadeInTime);
	DDX_Text(pDX, IDC_EDIT_FADE_OUT_TIME, m_nFadeOutTime);
}


BEGIN_MESSAGE_MAP(CEnvPanel, CDialog)
	ON_BN_CLICKED(IDC_ADD_SOUND, &CEnvPanel::OnBnClickedAddSound)
	ON_BN_CLICKED(IDC_DEL_SOUND, &CEnvPanel::OnBnClickedDelSound)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_EN_KILLFOCUS(IDC_EDIT_L, &CEnvPanel::OnEnKillfocusEditL)
	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_MAX_V, IDC_EDIT_MIN_F, &CEnvPanel::OnEnKillfocusEditSoundProp)
	ON_WM_HSCROLL()
	ON_LBN_SELCHANGE(IDC_SOUND_LIST, &CEnvPanel::OnLbnSelchangeSoundList)
	ON_BN_CLICKED(IDC_BUTTON_ADD_TYPE, &CEnvPanel::OnBnClickedButtonAddType)
	ON_BN_CLICKED(IDC_BUTTON_DEL_TYPE, &CEnvPanel::OnBnClickedButtonDelType)
	ON_WM_LBUTTONDOWN()
	ON_CBN_SELCHANGE(IDC_COMBO_TYPE, &CEnvPanel::OnCbnSelchangeComboType)
	ON_EN_KILLFOCUS(IDC_EDIT_MIN_INTERVAL, &CEnvPanel::OnEnKillfocusEditMinInterval)
	ON_EN_KILLFOCUS(IDC_EDIT_MAX_INTERVAL, &CEnvPanel::OnEnKillfocusEditMaxInterval)
	ON_NOTIFY_RANGE(NM_RELEASEDCAPTURE, IDC_SLIDER_MAX_V, IDC_SLIDER_MIN_F, &CEnvPanel::OnNMReleasedcaptureSliderSoundProp)
	ON_BN_CLICKED(IDC_BUTTON_DELETE_CUTS, &CEnvPanel::OnBnClickedButtonDeleteCuts)
	ON_CONTROL_RANGE(BN_CLICKED, IDC_RADIO_POLYGON_NONE, IDC_RADIO_POLYGON_CUT, &CEnvPanel::OnBnClickedRadioPolygonRadio)
	ON_EN_KILLFOCUS(IDC_EDIT_PRIORITY, &CEnvPanel::OnEnKillfocusEditPriority)
	ON_CBN_SELCHANGE(IDC_COMBO_SOUND_TYPE, &CEnvPanel::OnCbnSelchangeComboSoundType)
	ON_EN_KILLFOCUS(IDC_EDIT_BEGIN_TIME, &CEnvPanel::OnEnKillfocusEditBeginTime)
	ON_EN_KILLFOCUS(IDC_EDIT_END_TIME, &CEnvPanel::OnEnKillfocusEditEndTime)
	ON_EN_KILLFOCUS(IDC_EDIT_FADE_IN_TIME, &CEnvPanel::OnEnKillfocusEditFadeInTime)
	ON_EN_KILLFOCUS(IDC_EDIT_FADE_OUT_TIME, &CEnvPanel::OnEnKillfocusEditFadeOutTime)
	ON_BN_CLICKED(IDC_CHECK_LOOPING, &CEnvPanel::OnBnClickedCheckLooping)
END_MESSAGE_MAP()

void CEnvPanel::EnableSoundCtrls(BOOL b)
{
	m_bottomDelType.EnableWindow(b);
	m_buttonAddSound.EnableWindow(b);
	m_buttonDelSound.EnableWindow(b);
	m_listSound.EnableWindow(b);
	m_editMinInterval.EnableWindow(b);
	m_editMaxInterval.EnableWindow(b);
	m_editPriority.EnableWindow(b);
}

void CEnvPanel::ResetTimeCtrls()
{
	m_editBeginTime.EnableWindow(FALSE);
	m_editEndTime.EnableWindow(FALSE);
	m_editFadeInTime.EnableWindow(FALSE);
	m_editFadeOutTime.EnableWindow(FALSE);
	if (m_comboSoundType.IsWindowEnabled())
	{
		switch (m_comboSoundType.GetCurSel())
		{
		case 2:
			m_editFadeInTime.EnableWindow(TRUE);
			m_editFadeOutTime.EnableWindow(TRUE);
			//木有break
		case 1:
			m_editBeginTime.EnableWindow(TRUE);
			m_editEndTime.EnableWindow(TRUE);
			break;
		case 0:
			break;
		}

	}
}

void CEnvPanel::EnableSoundPropCtrls(BOOL b)
{
	for (int i=0;i<4;++i)
	{
		m_silderSoundProp[i].EnableWindow(b);
		m_editorSoundProp[i].EnableWindow(b);
	}
	m_comboSoundType.EnableWindow(b);
	m_checkLooping.EnableWindow(b);
	ResetTimeCtrls();
}

void CEnvPanel::ShowPolygonProp(BOOL b)
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

void CEnvPanel::ResetCont()
{
	if (m_bIsInited)
	{
		CSoundEditorDoc* pDoc = m_pView->GetDocument();
		static int sign = UVS_ENV_SOUND_AREA_SELECTED | UVS_TAB | UVS_LOADMAP | UVS_ENV_SOUND_AREA_ADD_DELETED;
		if (pDoc->m_nUpdateViewSign & sign)
		{
			m_comboType.ResetContent();
			if (pDoc->m_pSelectedEnvArea)
			{
				int typeCount = pDoc->m_pSelectedEnvArea->GetTypeCount();
				EnableSoundCtrls(typeCount != 0);
				m_comboType.EnableWindow(TRUE);
				m_bottomAddType.EnableWindow(TRUE);
				m_nMax = typeCount - 1;
				for (int i=0;i<typeCount;++i)
				{
					CString name;
					name.Format(L"类型 %d", i);
					m_comboType.AddString(name);
				}
				if (pDoc->GetUpdateSource() == US_UNDOREDO && pDoc->m_pCurOperator && 
					pDoc->m_pCurOperator->GetTabEditType() == EDIT_STATE_ENV_SOUND)
				{
					int typeID = pDoc->m_pCurOperator->GetTypeID(pDoc->m_nCurOperatorType);
					m_comboType.SetCurSel(typeID>0?typeID:0);
				} else {
					m_comboType.SetCurSel(0);
				}
				m_editL.EnableWindow(TRUE);
				m_nTransition = pDoc->m_pSelectedEnvArea->GetTransition();
				UpdateData(FALSE);
				ShowPolygonProp(pDoc->m_pSelectedEnvArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON);
			} else {
				m_nMax = -1;
				EnableSoundCtrls(FALSE);
				m_editL.EnableWindow(FALSE);
				m_comboType.EnableWindow(FALSE);
				m_bottomAddType.EnableWindow(FALSE);
				ShowPolygonProp(FALSE);
			}
			ResetSoundList();
			if (pDoc->m_nUpdateViewSign & UVS_LOADMAP)
			{
				m_AllSoundListPanel.ClearAll();
				const EnvAreaCont& cont = pDoc->GetEnvArea();
				std::vector<TmpInfo> infoVet;
				infoVet.resize(pDoc->GetEnvSoundCount());
				EnvAreaCont::const_iterator envIt = cont.begin(), envIe = cont.end();
				int i = 0, count = 0, soundID;
				for (;envIt!=envIe;++envIt)
				{
					const CToolsEnvSoundArea *pArea = *envIt;
					i=pArea->GetBeginID();
					int typeCount, typeID;
					typeCount = pArea->GetTypeCount();
					for (typeID=0;typeID<typeCount;++typeID)
					{
						std::vector<std::wstring> slist;
						pArea->GetSoundList(typeID, slist);
						std::vector<std::wstring>::iterator soundIt = slist.begin(), soundIe = slist.end();
						for (soundID=0;soundIt!=soundIe;++soundIt,++soundID,++i, ++count)
						{
							infoVet[i].handle = pArea->GetHandle(typeID, soundID);
							infoVet[i].str = *soundIt;
						}
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
				pDoc->m_pCurOperator->GetTabEditType() == EDIT_STATE_ENV_SOUND)
			{
				switch (pDoc->m_pCurOperator->GetType())
				{
				case CAction::ADD_DEL_ENV_AREA:
					{
						AddDelEnvArea* pOper = dynamic_cast<AddDelEnvArea*>(pDoc->m_pCurOperator);
						bool isAdd = pOper->IsCurrentAdd(pDoc->m_nCurOperatorType);
						int typeCount, typeID, soundID;
						typeCount = pOper->pArea->GetTypeCount();

						for (typeID=0;typeID<typeCount;++typeID)
						{
							std::vector<std::wstring> slist;
							pOper->pArea->GetSoundList(typeID, slist);
							std::vector<std::wstring>::iterator soundIt = slist.begin(), soundIe = slist.end();
							for (soundID=0;soundIt!=soundIe;++soundIt,++soundID)
							{
								DWORD_PTR handle = pOper->pArea->GetHandle(typeID, soundID);
								if (isAdd)
									m_AllSoundListPanel.InsertSound(pOper->allSoundListIDMap[handle], handle, *soundIt);
								else
									m_AllSoundListPanel.RemoveSound(handle);
							}
						}
					}
					break;
				case CAction::ADD_DEL_ENV_SOUND:
					{
						AddDelEnvSound* pOper = dynamic_cast<AddDelEnvSound*>(pDoc->m_pCurOperator);
						if (pOper->IsCurrentAdd(pDoc->m_nCurOperatorType))
						{
							m_AllSoundListPanel.InsertSound(pOper->nAllSoundListID, pOper->handle, pOper->param.soundName);
						} else {
							m_AllSoundListPanel.RemoveSound(pOper->handle);
						}
					}
					break;
				case CAction::ADD_DEL_ENV_TYPE:
					{
						AddDelEnvType* pOper = dynamic_cast<AddDelEnvType*>(pDoc->m_pCurOperator);
						if (pOper->IsCurrentAdd(pDoc->m_nCurOperatorType)) //add
						{
							int soundID;
							std::vector<std::wstring> slist;
							pOper->pArea->GetSoundList(pOper->typeID, slist);
							std::vector<std::wstring>::iterator soundIt = slist.begin(), soundIe = slist.end();
							for (soundID=0;soundIt!=soundIe;++soundIt,++soundID)
							{
								DWORD_PTR handle = pOper->pArea->GetHandle(pOper->typeID, soundID);
								m_AllSoundListPanel.InsertSound(pOper->allSoundListIDMap[handle], handle, *soundIt);
							}
						} else {
							CToolsSoundArea::HandleVet::iterator iter = pOper->soundHandles.begin(), 
								iterEnd = pOper->soundHandles.end();
							for (;iter!=iterEnd;++iter)
							{
								m_AllSoundListPanel.RemoveSound(*iter);
							}

						}
					}
					break;
				default:
					break;
				}
			}
			if (pDoc->m_nUpdateViewSign & UVS_ENV_SOUND_AREA_ADD_DELETED)
			{
				if (pDoc->m_pSelectedEnvArea) // add
				{
					int typeCount, typeID, soundID, panelSoundID;
					typeCount = pDoc->m_pSelectedEnvArea->GetTypeCount();
					panelSoundID = pDoc->m_pSelectedEnvArea->GetBeginID();
					AddDelEnvArea *pOper = dynamic_cast<AddDelEnvArea*>(pDoc->m_pCurOperator);
					XPASSERT(pOper);
					for (typeID=0;typeID<typeCount;++typeID)
					{
						std::vector<std::wstring> slist;
						pDoc->m_pSelectedEnvArea->GetSoundList(typeID, slist);
						std::vector<std::wstring>::iterator soundIt = slist.begin(), soundIe = slist.end();
						for (soundID=0;soundIt!=soundIe;++soundIt,++soundID,++panelSoundID)
						{
							DWORD_PTR handle = pDoc->m_pSelectedEnvArea->GetHandle(typeID, soundID);
							m_AllSoundListPanel.InsertSound(panelSoundID, handle, *soundIt);
							pOper->allSoundListIDMap[handle] = panelSoundID;
						}
					}
				} else {	//delete
					CToolsSoundArea::HandleVet::const_iterator iter = pDoc->GetDeletedHandleVet().begin(), 
						iterEnd = pDoc->GetDeletedHandleVet().end();
					AddDelEnvArea *pOper = dynamic_cast<AddDelEnvArea*>(pDoc->m_pCurOperator);
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
}

void CEnvPanel::ResetSoundList()
{
	m_listSound.ResetContent();
	if (m_comboType.GetCount() != 0)
	{
		EnableSoundCtrls(TRUE);
		int typeId = m_comboType.GetCurSel();
		CSoundEditorDoc* pDoc = m_pView->GetDocument();
		std::vector<std::wstring> slist;
		pDoc->m_pSelectedEnvArea->GetSoundList(typeId, slist);
		std::vector<std::wstring>::iterator it = slist.begin(), ie = slist.end();
		int i=0;
		for (;it!=ie;++it,++i)
		{
			m_listSound.InsertString(i, it->c_str());
		}
		if (i==0)	//没有东西
		{
			EnableSoundPropCtrls(FALSE);
		} else {
			EnableSoundPropCtrls(TRUE);
			if (pDoc->GetUpdateSource() == US_UNDOREDO && pDoc->m_pCurOperator && 
				pDoc->m_pCurOperator->GetTabEditType() == EDIT_STATE_ENV_SOUND)
			{
				int soundID = pDoc->m_pCurOperator->GetSoundID(pDoc->m_nCurOperatorType);
				m_listSound.SetCurSel(soundID>0?soundID:0);
			} else {
				m_listSound.SetCurSel(0);
			}
		}
		m_nMinInterval = pDoc->m_pSelectedEnvArea->GetMinInterval(typeId);
		m_nMaxInterval = pDoc->m_pSelectedEnvArea->GetMaxInterval(typeId);
		m_nPriority = pDoc->m_pSelectedEnvArea->GetPriority(typeId);
		UpdateData(FALSE);
	} else {
		EnableSoundCtrls(FALSE);
	}
	ResetSoundProp();
}

void CEnvPanel::ResetSoundProp()
{
	if (m_listSound.GetCount() != 0)
	{
		CToolsEnvSoundArea *pArea = m_pView->GetDocument()->m_pSelectedEnvArea;
		XPASSERT(pArea);
		int typeId = m_comboType.GetCurSel();
		int soundID = m_listSound.GetCurSel();
		m_comboSoundType.SetCurSel(pArea->GetSoundType(typeId, soundID) - 1);
		EnableSoundPropCtrls(TRUE);
		m_nSoundProp[0] = Vol255to100(pArea->GetMaxVol(typeId, soundID));
		m_nSoundProp[1] = Vol255to100(pArea->GetMinVol(typeId, soundID));
		m_nSoundProp[2] = FreqToShow(pArea->GetMaxFreq(typeId, soundID));
		m_nSoundProp[3] = FreqToShow(pArea->GetMinFreq(typeId, soundID));
		for (int i=0;i<4;++i)
		{
			m_silderSoundProp[i].SetPos(m_nSoundProp[i]);
		}
		m_nBeginTime = GameTime2EditorTime(pArea->GetTime(typeId, soundID, Nuclear::XPEST_BEGIN_TIME));
		m_nEndTime = GameTime2EditorTime(pArea->GetTime(typeId, soundID, Nuclear::XPEST_END_TIME));
		m_nFadeInTime = GameTime2RealTime(pArea->GetTime(typeId, soundID, Nuclear::XPEST_BEGIN_NORMAL_TIME)) - EditorTime2RealTime(m_nBeginTime);
		if (m_nFadeInTime < 0)
			m_nFadeInTime += GameTime2RealTime(Nuclear::XP_A_DAY_TIME);
		m_nFadeOutTime = EditorTime2RealTime(m_nEndTime) - GameTime2RealTime(pArea->GetTime(typeId, soundID, Nuclear::XPEST_FADE_OUT_TIME));
		if (m_nFadeOutTime < 0)
			m_nFadeOutTime += GameTime2RealTime(Nuclear::XP_A_DAY_TIME);
		m_bIsLooping = pArea->IsLooping(typeId, soundID);
		UpdateData(FALSE);
	} else {
		EnableSoundPropCtrls(FALSE);
	}
}

void CEnvPanel::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		rect.DeflateRect(5,5,5,5);
		CRect tmpRect = rect;
		tmpRect.bottom = tmpRect.top + 20;
		tmpRect.left = tmpRect.right - 35;
		m_bottomDelType.MoveWindow(&tmpRect, FALSE);
		tmpRect.right = tmpRect.left - 5;
		tmpRect.left = tmpRect.right - 35;
		m_bottomAddType.MoveWindow(&tmpRect, FALSE);
		tmpRect.right = tmpRect.left - 5;
		tmpRect.left = rect.left;
		m_comboType.MoveWindow(&tmpRect, FALSE);
		tmpRect.right = rect.right - 55;
		tmpRect.left = tmpRect.right - 50;
		tmpRect.top = tmpRect.bottom + 5;
		tmpRect.bottom = tmpRect.top + 20;
		m_editMinInterval.MoveWindow(&tmpRect, FALSE);
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = rect.right;
		m_editMaxInterval.MoveWindow(&tmpRect, FALSE);
		tmpRect.right = rect.right;
		tmpRect.left = rect.right - 50;
		tmpRect.top = tmpRect.bottom + 5;
		tmpRect.bottom = tmpRect.top + 20;
		m_editPriority.MoveWindow(&tmpRect, FALSE);
		tmpRect.left = rect.left;
		tmpRect.top = tmpRect.bottom + 5;
		tmpRect.bottom = tmpRect.top + 70;
		m_listSound.MoveWindow(&tmpRect, FALSE);
		tmpRect.top = tmpRect.bottom + 10;
		tmpRect.bottom = tmpRect.top + 20;
		tmpRect.right = tmpRect.left + 80;
		m_buttonAddSound.MoveWindow(&tmpRect, FALSE);
		tmpRect.right = rect.right;
		tmpRect.left = tmpRect.right - 80;
		m_buttonDelSound.MoveWindow(&tmpRect, FALSE);
		tmpRect.right = rect.right;
		tmpRect.left = tmpRect.right - 40;
		tmpRect.top = tmpRect.bottom + 10;
		tmpRect.bottom = tmpRect.top + 20;
		CRect tmpRect2 = tmpRect;
		tmpRect2.right = tmpRect.left - 5;
		tmpRect2.left = rect.left + 60;
		tmpRect2.top -= 1;
		tmpRect2.bottom += 1;
		for (int i=0;i<4;++i)
		{
			m_editorSoundProp[i].MoveWindow(&tmpRect, FALSE);
			m_silderSoundProp[i].MoveWindow(&tmpRect2, FALSE);
			tmpRect.top = tmpRect2.top = tmpRect.bottom + 10;
			tmpRect.bottom = tmpRect2.bottom = tmpRect.top + 20;
			tmpRect2.top -= 1;
			tmpRect2.bottom += 1;
		}
		tmpRect.left = 70;
		tmpRect.right = tmpRect.left + 75;
		m_comboSoundType.MoveWindow(&tmpRect);
		tmpRect.left = tmpRect.right + 5;
		tmpRect.right = tmpRect.left + 80;
		m_checkLooping.MoveWindow(&tmpRect);
		tmpRect = rect;
		tmpRect.top = tmpRect2.bottom + 200;
		m_AllSoundListPanel.MoveWindow(&tmpRect);
		m_AllSoundListPanel.BringWindowToTop();
	}
}

void CEnvPanel::SaveSoundProp(int nID)
{
	XPASSERT (nID >= 0);
	XPASSERT (nID < 4);
	int typeID = m_comboType.GetCurSel();
	int soundID = m_listSound.GetCurSel();
	unsigned char value = static_cast<unsigned char>(m_nSoundProp[nID]);
	switch(nID)
	{
	case 0:
		m_pView->GetDocument()->SetSelectedAreaMaxVol(typeID, soundID, Vol100to255(value));
		break;
	case 1:
		m_pView->GetDocument()->SetSelectedAreaMinVol(typeID, soundID, Vol100to255(value));
		break;
	case 2:
		m_pView->GetDocument()->SetSelectedAreaMaxFreq(typeID, soundID, FreqToSave(value));
		break;
	case 3:
		m_pView->GetDocument()->SetSelectedAreaMinFreq(typeID, soundID, FreqToSave(value));
		break;
	default:
		XPASSERT(FALSE);
		break;
	}
}

// CEnvPanel message handlers

BOOL CEnvPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_pView = static_cast<CPropView*>(GetParent());
	for (int i=0;i<2;++i)
	{
		m_silderSoundProp[i].SetRange(0, 100, FALSE);
		m_silderSoundProp[i].SetPos(m_nSoundProp[i]);
	}
	for (int i=2;i<4;++i)
	{
		m_silderSoundProp[i].SetRange(-128, 127, FALSE);
		m_silderSoundProp[i].SetPos(m_nSoundProp[i]);
	}
	m_AllSoundListPanel.Create(m_AllSoundListPanel.IDD, this);
	m_AllSoundListPanel.SetPanel(this);
	m_AllSoundListPanel.ShowWindow(SW_SHOW);
	m_comboSoundType.InsertString(0, L"普通音效");
	m_comboSoundType.InsertString(1, L"时间音效");
	m_comboSoundType.InsertString(2, L"淡入淡出音效");
	m_comboSoundType.SetCurSel(0);
	m_bIsInited = true;
	ResetCont();
	ResetSize();

	return FALSE;
}

void CEnvPanel::OnBnClickedAddSound()
{
	CSoundEditorDoc *pDoc = m_pView->GetDocument();
	if (!pDoc)
		return;
	XPASSERT(pDoc->GetEngineBase());
	wchar_t olddir[MAX_PATH];
	GetCurrentDirectory(MAX_PATH, olddir);
	CAudioFileDialog dlg(pDoc->GetEngineBase()->GetSoundManager(), 
		TRUE, NULL, NULL, OFN_FILEMUSTEXIST, L"声音文件(*.*|*.*||)");
	CString dir = pDoc->m_lastEnvOpenUrl.IsEmpty()?theApp.GetExePath() + L"\\res\\sound":m_pView->GetDocument()->m_lastEnvOpenUrl;
	dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
	if (dlg.DoModal() == IDOK)
	{
		pDoc->m_lastEnvOpenUrl = dlg.GetPathName();
		std::wstring name;
		if (PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name))
		{
			SoundInfo info;
			int soundID = pDoc->AddSound2SelectedArea(m_comboType.GetCurSel(), name, info);
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

void CEnvPanel::OnBnClickedDelSound()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	int soundID = m_listSound.GetCurSel();
	DWORD_PTR handle = pDoc->m_pSelectedEnvArea->GetHandle(m_comboType.GetCurSel(), soundID);
	UINT nID = m_AllSoundListPanel.RemoveSound(handle);
	pDoc->DeleteSelectedAreaSound(m_comboType.GetCurSel(), soundID, nID);
	
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

void CEnvPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

void CEnvPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CEnvPanel::OnEnKillfocusEditL()
{
	UpdateData();
	m_pView->GetDocument()->SetSelectedAreaTransition(m_nTransition);
}

void CEnvPanel::OnEnKillfocusEditSoundProp(UINT nID)
{
	nID -= IDC_EDIT_MAX_V;
	int minprop = nID<2?0:-128;
	int maxprop = nID<2?100:127;
	int oldProp = m_nSoundProp[nID];
	UpdateData(TRUE);
	if (m_nSoundProp[nID] >=minprop && m_nSoundProp[nID] <= maxprop)
	{
		m_silderSoundProp[nID].SetPos(m_nSoundProp[nID]);
		SaveSoundProp(nID);
	} else {
		m_nSoundProp[nID] = oldProp;
		UpdateData(FALSE);
	}
}

void CEnvPanel::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	int nID = pScrollBar->GetDlgCtrlID() - IDC_SLIDER_MAX_V;
	if (nID >= 0 && nID < 4)
	{
		int newPos = m_silderSoundProp[nID].GetPos();
		m_nSoundProp[nID] = newPos;
		UpdateData(FALSE);
	}
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CEnvPanel::OnLbnSelchangeSoundList()
{
	ResetSoundProp();
}

void CEnvPanel::OnBnClickedButtonAddType()
{
	int soundType = m_pView->GetDocument()->AddSoundType2SelectedArea();
	CString name;
	name.Format(L"类型 %d", ++m_nMax);
	m_comboType.AddString(name);
	m_comboType.SetCurSel(soundType);
	ResetSoundList();
}

void CEnvPanel::OnBnClickedButtonDelType()
{
	int soundType = m_comboType.GetCurSel();
	CSoundEditorDoc *pDoc = m_pView->GetDocument();
	AddDelEnvType* pOper = NULL;
	pDoc->DeleteSoundTypeFromSelectedArea(soundType, pOper);
	m_comboType.DeleteString(soundType);
	if (soundType >= m_comboType.GetCount())
	{
		soundType = m_comboType.GetCount() - 1;
	}
	if (soundType >= 0)
	{
		m_comboType.SetCurSel(soundType);
	}
	CToolsSoundArea::HandleVet::iterator iter = pOper->soundHandles.begin(), 
		iterEnd = pOper->soundHandles.end();
	for (;iter!=iterEnd;++iter)
	{
		UINT id = m_AllSoundListPanel.RemoveSound(*iter);
		pOper->allSoundListIDMap[*iter] = id;
	}
	ResetSoundList();

}

void CEnvPanel::OnLButtonDown(UINT nFlags, CPoint point)
{
	GetDlgItem(IDC_STATIC_MAX_V)->SetFocus();
	CDialog::OnLButtonDown(nFlags, point);
}

void CEnvPanel::OnCbnSelchangeComboType()
{
	ResetSoundList();
}

void CEnvPanel::OnEnKillfocusEditMinInterval()
{
	UpdateData();
	m_pView->GetDocument()->SetSelectedAreaMinInterval(m_comboType.GetCurSel(), m_nMinInterval);
}

void CEnvPanel::OnEnKillfocusEditMaxInterval()
{
	UpdateData();
	m_pView->GetDocument()->SetSelectedAreaMaxInterval(m_comboType.GetCurSel(), m_nMaxInterval);
}

void CEnvPanel::OnNMReleasedcaptureSliderSoundProp(UINT nID, NMHDR *pNMHDR, LRESULT *pResult)
{
	SaveSoundProp(nID - IDC_SLIDER_MAX_V);
	*pResult = 0;
}

void CEnvPanel::OnBnClickedButtonDeleteCuts()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	XPASSERT(pDoc->m_pSelectedEnvArea);
	pDoc->ClearPolygonCuts(pDoc->m_pSelectedEnvArea);
}

void CEnvPanel::OnBnClickedRadioPolygonRadio(UINT nID)
{
	m_pView->GetDocument()->m_nPolygonEditState = nID - IDC_RADIO_POLYGON_NONE;
	m_pView->GetDocument()->m_PolygonMouseState = PMS_NORMAL;
}

void CEnvPanel::SelectASound(DWORD_PTR handle)
{
	m_pView->GetDocument()->SelectAndMoveToAnEnvArea(handle);
}
void CEnvPanel::OnEnKillfocusEditPriority()
{
	UpdateData();
	m_pView->GetDocument()->SetSelectedAreaPriority(m_comboType.GetCurSel(), m_nPriority);
}

void CEnvPanel::OnCbnSelchangeComboSoundType()
{
	ResetTimeCtrls();
	int type = m_comboSoundType.GetCurSel();
	switch (type)
	{
	case 0:
		m_nBeginTime = 0;
		m_nEndTime = SE_EDITOR_TIME_A_DAY;
	case 1:
		m_nFadeInTime = 0;
		m_nFadeOutTime = 0;
		break;
	case 2:
		break;
	}
	UpdateData(FALSE);
	m_pView->GetDocument()->SetSelectedAreaSoundType(m_comboType.GetCurSel(), 
		m_listSound.GetCurSel(), static_cast<unsigned char>(type + 1));
}

void CEnvPanel::CheckTimeValid()
{
	if (m_nFadeInTime > 0 || m_nFadeOutTime > 0)
	{
		int nTimeDuring = m_nEndTime - m_nBeginTime;
		if (nTimeDuring < 0)
			nTimeDuring += SE_EDITOR_TIME_A_DAY;
		CString errMsg = L"发生如下错误，请调整：\n";
		bool hasErr = false;
		if (RealTime2EditorTime(m_nFadeInTime) > nTimeDuring)
		{
			hasErr = true;
			errMsg += L"  淡入时间比播放持续时间还长！\n";
		}
		if (RealTime2EditorTime(m_nFadeOutTime) > nTimeDuring)
		{
			hasErr = true;
			errMsg += L"  淡出时间比播放持续时间还长！\n";
		}
		if (hasErr)
		{
			AfxMessageBox(errMsg.GetBuffer());
			return;
		}
		//编辑器进度条时间
		int nFadeInTime = (m_nBeginTime + RealTime2EditorTime(m_nFadeInTime)) % SE_EDITOR_TIME_A_DAY;
		int nFadeOutTime = m_nEndTime - RealTime2EditorTime(m_nFadeOutTime);
		if (nFadeOutTime < 0)
			nFadeOutTime += SE_EDITOR_TIME_A_DAY;
		
		if (m_nBeginTime <= m_nEndTime)
		{
			if (nFadeInTime > nFadeOutTime) 
			{
				hasErr = true;
				errMsg += L"  淡入结束时间在开始淡出时间之后！\n    请减少淡入或淡出时间或者延后声音结束时间";
			}
		} else {
			if (nFadeOutTime >= m_nBeginTime)
			{
				if (nFadeInTime < m_nBeginTime || nFadeInTime > nFadeOutTime)
				{
					hasErr = true;
					errMsg += L"  淡入结束时间在开始淡出时间之后！\n    请减少淡入或淡出时间或者延后声音结束时间";
				}
			} else {
				if (nFadeInTime <= m_nEndTime && nFadeInTime > nFadeOutTime)
				{
					hasErr = true;
					errMsg += L"  淡入结束时间在开始淡出时间之后！\n    请减少淡入或淡出时间或者延后声音结束时间";
				}
			}
		}
		if (hasErr)
			AfxMessageBox(errMsg.GetBuffer());
	}
}

void CEnvPanel::OnEnKillfocusEditBeginTime()
{
	UpdateData();
	int typeID = m_comboType.GetCurSel();
	int soundID = m_listSound.GetCurSel();
	m_pView->GetDocument()->SetSelectedAreaTime(typeID, soundID, 
		Nuclear::XPEST_BEGIN_TIME, EditorTime2GameTime(m_nBeginTime));
	m_pView->GetDocument()->SetSelectedAreaTime(typeID, soundID, Nuclear::XPEST_BEGIN_NORMAL_TIME, 
		(EditorTime2GameTime(m_nBeginTime) + RealTime2GameTime(m_nFadeInTime)) % Nuclear::XP_A_DAY_TIME);
	CheckTimeValid();
}

void CEnvPanel::OnEnKillfocusEditEndTime()
{
	UpdateData();
	int typeID = m_comboType.GetCurSel();
	int soundID = m_listSound.GetCurSel();
	m_pView->GetDocument()->SetSelectedAreaTime(typeID, soundID, 
		Nuclear::XPEST_END_TIME, EditorTime2GameTime(m_nEndTime));
	int tmpTime = EditorTime2GameTime(m_nEndTime) - RealTime2GameTime(m_nFadeOutTime);
	if (tmpTime < 0) 
		tmpTime += Nuclear::XP_A_DAY_TIME;
	m_pView->GetDocument()->SetSelectedAreaTime(typeID, soundID, Nuclear::XPEST_FADE_OUT_TIME, tmpTime);
	CheckTimeValid();
}

void CEnvPanel::OnEnKillfocusEditFadeInTime()
{
	UpdateData();
	m_pView->GetDocument()->SetSelectedAreaTime(m_comboType.GetCurSel(), 
		m_listSound.GetCurSel(), Nuclear::XPEST_BEGIN_NORMAL_TIME, 
		(EditorTime2GameTime(m_nBeginTime) + RealTime2GameTime(m_nFadeInTime)) % Nuclear::XP_A_DAY_TIME);
	CheckTimeValid();
}

void CEnvPanel::OnEnKillfocusEditFadeOutTime()
{
	UpdateData();
	int tmpTime = EditorTime2GameTime(m_nEndTime) - RealTime2GameTime(m_nFadeOutTime);
	if (tmpTime < 0) 
		tmpTime += Nuclear::XP_A_DAY_TIME;
	m_pView->GetDocument()->SetSelectedAreaTime(m_comboType.GetCurSel(), 
		m_listSound.GetCurSel(), Nuclear::XPEST_FADE_OUT_TIME, tmpTime);
	CheckTimeValid();
}

void CEnvPanel::OnBnClickedCheckLooping()
{
	UpdateData();
	m_pView->GetDocument()->SetSelectedAreaLooping(m_comboType.GetCurSel(), m_listSound.GetCurSel(), m_bIsLooping==TRUE);
}
