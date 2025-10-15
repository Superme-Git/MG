#pragma once
#include "afxcmn.h"



class CMapEditorView;

class CTriggerListDlg : public CDialog
{
	DECLARE_DYNAMIC(CTriggerListDlg)
private:
	std::map<unsigned int, HTREEITEM> m_ItemMap;
	CTreeCtrl m_treeTriggers;
	CMapEditorView* m_pMapEditorView;

public:
	enum { IDD = IDD_DLG_TRIGGER_LIST };
	virtual ~CTriggerListDlg();

	CTriggerListDlg(CWnd* pParent = NULL);   

private:
	
	void ListRemoveTrigger(Nuclear::SubMap::TriggerID id);

public:
	bool HasSelectedTrigger() const
	{
		return (m_treeTriggers.GetSelectedItem() != NULL);
	}
	Nuclear::SubMap::TriggerInfoID GetCurInfoID() const;
	void UnselectAllItems();

	void TryChangeSelectedTrigger(Nuclear::SubMap::TriggerID id);
	void ReNewTree();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnNMClickTree1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButton2();
	afx_msg void OnBnClickedButtonDeleteLayer();
	afx_msg void OnBnClickedButton1();
	afx_msg void OnBnClickedButtonNewLayer();
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);

	virtual BOOL OnInitDialog();
};
