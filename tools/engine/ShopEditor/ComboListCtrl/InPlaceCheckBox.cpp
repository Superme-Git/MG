// InPlaceCheckBox.cpp : implementation file
//

#include "stdafx.h"
#include "InPlaceCheckBox.h"


CInPlaceCheckBox* CInPlaceCheckBox::m_pInPlaceCheckBox = NULL;
// CInPlaceCheckBox

LPCTSTR g_Check[] = {
	_T("V"),
	_T("X")
};

IMPLEMENT_DYNAMIC(CInPlaceCheckBox, CButton)

CInPlaceCheckBox::CInPlaceCheckBox()
{

}

CInPlaceCheckBox::~CInPlaceCheckBox()
{
}


BEGIN_MESSAGE_MAP(CInPlaceCheckBox, CButton)
	ON_WM_KILLFOCUS()
END_MESSAGE_MAP()



// CInPlaceCheckBox message handlers


// Returns the instance of the class
CInPlaceCheckBox* CInPlaceCheckBox::GetInstance()
{
	if( NULL == m_pInPlaceCheckBox)
	{
		m_pInPlaceCheckBox = new CInPlaceCheckBox;
	}
	return m_pInPlaceCheckBox;
}

// Deletes the instance of the class
void CInPlaceCheckBox::DeleteInstance()
{
	delete m_pInPlaceCheckBox;
	m_pInPlaceCheckBox = NULL;
}

// Creates the Windows combo control and attaches it to the object, if needed and shows the combo ctrl
BOOL CInPlaceCheckBox::ShowCheckBox(DWORD dwStyle, const CRect& rCellRect, CWnd* pParentWnd, UINT uiResourceID,
				   int iRowIndex, int iColumnIndex, CString& strCurSelecetion)
{
	m_iRowIndex = iRowIndex;
	m_iColumnIndex = iColumnIndex;

	BOOL bRetVal = FALSE;
	if (NULL == m_pInPlaceCheckBox->m_hWnd) 
	{
		bRetVal = m_pInPlaceCheckBox->Create( L"", dwStyle, rCellRect, pParentWnd, uiResourceID);
	}
	if( strCurSelecetion.CompareNoCase( g_Check[0]) == 0)
	{
		m_pInPlaceCheckBox->SetCheck( BST_CHECKED);
	}
	else
	{
		m_pInPlaceCheckBox->SetCheck( BST_UNCHECKED);
	}
	SetFocus();
	return bRetVal;
}


void CInPlaceCheckBox::OnKillFocus(CWnd* pNewWnd)
{
	CButton::OnKillFocus(pNewWnd);

	BOOL bCheck = GetCheck();

	// TODO: Add your message handler code here
	LV_DISPINFO dispinfo;
	dispinfo.hdr.hwndFrom = GetParent()->m_hWnd;
	dispinfo.hdr.idFrom = GetDlgCtrlID();
	dispinfo.hdr.code = LVN_ENDLABELEDIT;

	dispinfo.item.mask = LVIF_TEXT;
	dispinfo.item.iItem = m_iRowIndex;
	dispinfo.item.iSubItem = m_iColumnIndex;
	dispinfo.item.pszText = (LPWSTR)g_Check[bCheck];
	dispinfo.item.cchTextMax = 1;

	GetParent()->SendMessage(WM_NOTIFY, GetParent()->GetDlgCtrlID(), (LPARAM)&dispinfo);

	PostMessage(WM_CLOSE);
}
