// AllSoundListPanel.cpp : implementation file
//

#include "stdafx.h"
#include <assert.h>
#include "SoundEditor.h"
#include "AllSoundListPanel.h"


// AllSoundListPanel dialog

IMPLEMENT_DYNAMIC(AllSoundListPanel, CDialog)

AllSoundListPanel::AllSoundListPanel(CWnd* pParent /*=NULL*/)
	: CDialog(AllSoundListPanel::IDD, pParent), m_pPanel(NULL), m_bIsInited(false)
{

}

AllSoundListPanel::~AllSoundListPanel()
{
}

void AllSoundListPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_ALL_SOUND_LIST, m_soundList);
}

void AllSoundListPanel::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rct;
		GetClientRect(&rct);
		m_soundList.MoveWindow(&rct);
	}
}

BEGIN_MESSAGE_MAP(AllSoundListPanel, CDialog)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_LBN_SELCHANGE(IDC_ALL_SOUND_LIST, &AllSoundListPanel::OnLbnSelchangeAllSoundList)
END_MESSAGE_MAP()


// AllSoundListPanel message handlers

BOOL AllSoundListPanel::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_bIsInited = true;
	ResetSize();

	return FALSE;
}

void AllSoundListPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

void AllSoundListPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void AllSoundListPanel::OnLbnSelchangeAllSoundList()
{
	if (m_pPanel)
	{
		m_pPanel->SelectASound(m_soundList.GetItemData(m_soundList.GetCurSel()));
	}
}

void AllSoundListPanel::InsertSound(UINT nID, DWORD_PTR handle, const std::wstring &str)
{
	assert(nID <= (UINT)m_soundList.GetCount());
	m_soundList.InsertString(nID, str.c_str());
	m_soundList.SetItemData(nID, handle);
	ListMap::iterator it, ie = m_listMap.end();
	for (it=m_listMap.begin();it!=ie;++it)
	{
		if (it->second >= nID)
			++(it->second);
	}
	m_listMap[handle] = nID;
}

UINT AllSoundListPanel::RemoveSound(DWORD_PTR handle)
{
	ListMap::iterator it = m_listMap.find(handle), ie = m_listMap.end();
	UINT result = 0;
	if (it != ie)
	{
		result = it->second;
		m_listMap.erase(it);
		for (it=m_listMap.begin();it!=ie;++it)
		{
			if (it->second > result)
				--(it->second);
		}
		m_soundList.DeleteString(result);
	}
	return result;
}

void AllSoundListPanel::RemoveSounds(UINT nBeginID, UINT nEndID)
{
	if (nEndID > nBeginID)
	{
		ListMap::iterator it = m_listMap.begin(), ie = m_listMap.end();
		for (;it!=ie;)
		{
			if (it->second >= nEndID)
			{
				--(it->second);
				++it;
				continue;
			}
			if (it->second < nBeginID)
			{
				++it;
				continue;
			}
			m_listMap.erase(it++);
		}
		for (UINT i=nEndID-1;i>=nBeginID;--i)
		{
			m_soundList.DeleteString(i);
		}
	}
}

void AllSoundListPanel::ClearAll()
{
	m_listMap.clear();
	m_soundList.ResetContent();
}