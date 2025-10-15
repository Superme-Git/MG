#pragma once


// CInPlaceCheckBox

class CInPlaceCheckBox : public CButton
{
	DECLARE_DYNAMIC(CInPlaceCheckBox)

public:
	CInPlaceCheckBox();
	virtual ~CInPlaceCheckBox();

	// Returns the instance of the class
	static CInPlaceCheckBox* GetInstance(); 

	// Deletes the instance of the class
	static void DeleteInstance(); 

	// Creates the Windows combo control and attaches it to the object, if needed and shows the combo ctrl
	BOOL ShowCheckBox(DWORD dwStyle, const CRect& rCellRect, CWnd* pParentWnd, UINT uiResourceID,
		int iRowIndex, int iColumnIndex, CString& strCurSelecetion);

protected:
	DECLARE_MESSAGE_MAP()

	// Singleton instance
	static CInPlaceCheckBox* m_pInPlaceCheckBox;
public:
	afx_msg void OnKillFocus(CWnd* pNewWnd);

	// Index of the item in the list control
	int m_iRowIndex;

	// Index of the subitem in the list control
	int m_iColumnIndex;

};


