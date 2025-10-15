#pragma once
#include "afxcmn.h"
#include "LayerCtrl.h"


class CMapEditorView;
class CBackgroundListDlg : public CDialog
{
	DECLARE_DYNAMIC(CBackgroundListDlg)
public:
	
	enum { IDD = IDD_DLG_BACKGROUND };

private:
	CLayerCtrl m_BackgroundInfoList;
	CMapEditorView *m_pMapEditorView;

public:
	virtual ~CBackgroundListDlg();
	CBackgroundListDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()

public:
	afx_msg LRESULT OnLvnEnddraggingList(WPARAM wParam, LPARAM lParam);
	afx_msg void OnLvnItemchangedList1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonDelete();
	afx_msg void OnBnClickedButtonEdit();
	afx_msg void OnBnClickedButtonAdd();
	virtual BOOL OnInitDialog();
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	void RenewList();
};
