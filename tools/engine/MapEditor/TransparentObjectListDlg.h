#pragma once

class CMapEditorView;


class CTransparentObjectListDlg : public CDialog
{
	DECLARE_DYNAMIC(CTransparentObjectListDlg)
private:
	void EditTransObj(int item);
	void RenewSize();
	int m_nMinHeight;

	int m_nMinWidth;
	CMapEditorView* m_pMapEditorView;
	CListCtrl m_listTrans;

public:
	enum { IDD = IDD_DLG_TRANSOBJ_LIST };
	void UnselectAllItems();

	void TryChangeSelectedTransparentObject(unsigned int transID, BOOL add = TRUE);
	void ReNewTree();
	virtual BOOL OnInitDialog();
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	virtual ~CTransparentObjectListDlg();
	CTransparentObjectListDlg(CWnd* pParent = NULL);   

protected:
	afx_msg void OnBnClickedButton2();
	afx_msg void OnBnClickedButton1();
	afx_msg void OnLvnItemchangedList1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclkList1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnBnClickedDeleteObject();
	afx_msg void OnBnClickedNewObject();
	DECLARE_MESSAGE_MAP()

	virtual void OnOK();

	virtual void DoDataExchange(CDataExchange* pDX);    
};
