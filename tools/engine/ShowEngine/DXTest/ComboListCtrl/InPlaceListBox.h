#pragma once


// CInPlaceCheckBox

class CInPlaceListBox : public CListCtrl
{
	DECLARE_DYNAMIC(CInPlaceListBox)

public:
	CInPlaceListBox();
	virtual ~CInPlaceListBox();

	// Returns the instance of the class
	static CInPlaceListBox* GetInstance(); 

	// Deletes the instance of the class
	static void DeleteInstance(); 

	// Creates the Windows combo control and attaches it to the object, if needed and shows the combo ctrl
	BOOL ShowListBox(DWORD dwStyle, const CRect& rCellRect, CWnd* pParentWnd, UINT uiResourceID,
		int iRowIndex, int iColumnIndex, CStringList& slDropDownList, CString& strCurSelecetion);

protected:
	DECLARE_MESSAGE_MAP()

	// Singleton instance
	static CInPlaceListBox* m_pInPlaceListBox;
public:
	afx_msg void OnKillFocus(CWnd* pNewWnd);

	// Index of the item in the list control
	int m_iRowIndex;

	// Index of the subitem in the list control
	int m_iColumnIndex;

	// List of items to be shown in the drop down
	CStringList m_DropDownList;
	CString		m_strCurSelecetion;

	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
};


