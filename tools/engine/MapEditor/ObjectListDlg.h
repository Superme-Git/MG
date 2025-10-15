#ifndef OBJ_LIST_DLG_H
#define OBJ_LIST_DLG_H

#pragma once

#include "afxcmn.h"
#include <vector>
#include <map>

#include "..\engine\common\xptypes.h"
#include "..\engine\common\recttopologylist.h"
#include "afxwin.h"


class CMapEditorView;

class CObjectListDlg : public CDialog
{
	DECLARE_DYNAMIC(CObjectListDlg)
private:
	void RenewSize();
	int m_nMinHeight;

	int m_nMinWidth;
	std::map<Nuclear::SortBaseId, int> m_SortIdToListIdMap;
	int m_nLayerEditId;
	CListBox m_listObjects;
	CMapEditorView* m_pMapEditorView;
	bool m_bSizing;

public:
	enum { IDD = IDD_OBJ_LIST };
	void TryRemoveObject(Nuclear::SortBaseId sortBaseID);
	void TryAddObject(Nuclear::SortBaseId sortBaseID);
	void UnselectAllItems();
	void TryChangeSelectedObject(Nuclear::SortBaseId sortBaseID, BOOL add = TRUE);
	void ResetLists();
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	virtual ~CObjectListDlg();
	CObjectListDlg(CWnd* pParent = NULL);   

protected:
	virtual BOOL OnInitDialog();
	virtual void OnOK();
	virtual void DoDataExchange(CDataExchange* pDX);    
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnLbnSelchangeObjsListBox();
	DECLARE_MESSAGE_MAP()
};
#endif