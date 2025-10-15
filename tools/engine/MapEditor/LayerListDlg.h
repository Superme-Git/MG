#pragma once
#include "LayerCtrl.h"
#include "afxcmn.h"
#include "afxwin.h"



class CMapEditorView;

class CLayerListDlg : public CDialog
{
	DECLARE_DYNAMIC(CLayerListDlg)
private:
	bool m_bSizing;
	CMapEditorView* m_pMapEditorView;
	CButton m_btnAddLayer;
	CButton m_btnDeleteLayer;
	CLayerCtrl m_listGroundLayers;
	CLayerCtrl m_listSmallLayers;
	CTabCtrl m_Tab;

private:
	void RenewSize();
	void ResetTabMode();

protected:
	virtual void OnOK() {};
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonDeleteLayer();
	afx_msg void OnBnClickedButtonNewLayer();
	afx_msg LRESULT OnLvnEndDraggingList(WPARAM wParam, LPARAM lParam); 
	afx_msg void OnLvnItemChangedList2(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnItemChangedList1(NMHDR *pNMHDR, LRESULT *pResult);

	enum { IDD = IDD_DLG_LAYER_LIST };

	void ReNewTree();


	void SetLayerChecked(int layer, bool isChecked);
	void SetLayerName(int layer, CString name);
	void SelectLayer(Nuclear::SubMap::GroundLayers layerID);
	void SelectLayer(int layerID);	
	void SetLayerState(int layer, UINT nState);
	UINT GetLayerState(int layer);
	void DeleteLayer(int layer);
	void NewLayer(CString name);
	virtual BOOL OnInitDialog();
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	virtual ~CLayerListDlg();
	CLayerListDlg(CWnd* pParent = NULL);   

	DECLARE_MESSAGE_MAP()
};
