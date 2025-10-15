// InPlaceCheckBox.cpp : implementation file
//

#include "stdafx.h"
#include "InPlaceListBox.h"


CInPlaceListBox* CInPlaceListBox::m_pInPlaceListBox = NULL;
// CInPlaceCheckBox


IMPLEMENT_DYNAMIC(CInPlaceListBox, CListCtrl)

CInPlaceListBox::CInPlaceListBox()
{

}

CInPlaceListBox::~CInPlaceListBox()
{
}


BEGIN_MESSAGE_MAP(CInPlaceListBox, CListCtrl)
	ON_WM_KILLFOCUS()
	ON_WM_CREATE()
END_MESSAGE_MAP()



// CInPlaceCheckBox message handlers


// Returns the instance of the class
CInPlaceListBox* CInPlaceListBox::GetInstance()
{
	if( NULL == m_pInPlaceListBox)
	{
		m_pInPlaceListBox = new CInPlaceListBox;
	}
	return m_pInPlaceListBox;
}

// Deletes the instance of the class
void CInPlaceListBox::DeleteInstance()
{
	delete m_pInPlaceListBox;
	m_pInPlaceListBox = NULL;
}

// Creates the Windows combo control and attaches it to the object, if needed and shows the combo ctrl
BOOL CInPlaceListBox::ShowListBox(DWORD dwStyle, const CRect& rCellRect, CWnd* pParentWnd, UINT uiResourceID,
				   int iRowIndex, int iColumnIndex, CStringList& slDropDownList, CString& strCurSelecetion)
{
	m_iRowIndex = iRowIndex;
	m_iColumnIndex = iColumnIndex;
	
	m_strCurSelecetion = strCurSelecetion;

	m_DropDownList.RemoveAll(); 
	m_DropDownList.AddTail( &slDropDownList);

	BOOL bRetVal = FALSE;
	if (NULL == m_pInPlaceListBox->m_hWnd) 
	{
		bRetVal = m_pInPlaceListBox->Create( LVS_REPORT|LVS_NOCOLUMNHEADER|WS_VISIBLE|WS_BORDER, 
			rCellRect, pParentWnd, uiResourceID);
	}
	
	return bRetVal;
}


void CInPlaceListBox::OnKillFocus(CWnd* pNewWnd)
{
	CListCtrl::OnKillFocus(pNewWnd);

	CString str;
	for( int i=0; i<GetItemCount(); i++)
	{
		if( GetCheck( i))
		{
			if( !str.IsEmpty())
			{
				str += L";";
			}
			str += GetItemText( i, 0);
		}
	}

	// TODO: Add your message handler code here
	LV_DISPINFO dispinfo;
	dispinfo.hdr.hwndFrom = GetParent()->m_hWnd;
	dispinfo.hdr.idFrom = GetDlgCtrlID();
	dispinfo.hdr.code = LVN_ENDLABELEDIT;

	dispinfo.item.mask = LVIF_TEXT;
	dispinfo.item.iItem = m_iRowIndex;
	dispinfo.item.iSubItem = m_iColumnIndex;
	dispinfo.item.pszText = str.GetBuffer();
	dispinfo.item.cchTextMax = str.GetLength();

	GetParent()->SendMessage(WM_NOTIFY, GetParent()->GetDlgCtrlID(), (LPARAM)&dispinfo);

	PostMessage(WM_CLOSE);
}

int CInPlaceListBox::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CListCtrl::OnCreate(lpCreateStruct) == -1)
		return -1;

	// TODO:  Add your specialized creation code here
	// Set the proper font
	SetExtendedStyle( LVS_EX_CHECKBOXES|LVS_EX_FULLROWSELECT );

	InsertColumn( 0, L"coloumn", LVCFMT_LEFT, lpCreateStruct->cx);

	CFont* pFont = GetParent()->GetFont();
	SetFont(pFont);

	DeleteAllItems();
	if( !m_DropDownList.IsEmpty())
	{
		for (POSITION Pos_ = m_DropDownList.GetHeadPosition(); Pos_ != NULL;)
		{
			CString& str = m_DropDownList.GetNext(Pos_);
			int index = InsertItem( GetItemCount(), str);
			LPCTSTR psz = wcsstr( m_strCurSelecetion, str);
			if( psz )
			{
				psz += str.GetLength();
				if( psz[0] == 0 || psz[0] == L';')
				{
					SetCheck( index );
				}
			}
		}

		CRect rcItem;
		GetItemRect( 0, &rcItem, LVIR_BOUNDS);
		int height = GetItemCount()*rcItem.Height();
		if( height < lpCreateStruct->cy)
		{
			SetWindowPos( NULL, 0, 0, lpCreateStruct->cx, height, SWP_NOMOVE);
		}
	}

	SetFocus();

	return 0;
}
