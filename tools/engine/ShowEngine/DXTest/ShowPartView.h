#pragma once
#include "ShowPartLoader.h"
#include "DXTestView.h"
// CShowPartView view
#include "ComboListCtrl/ComboListCtrl.h"

class CShowPartView : public CTreeView
{
	DECLARE_DYNCREATE(CShowPartView)

protected:
	CShowPartView();           // protected constructor used by dynamic creation
	virtual ~CShowPartView();

	CShowPartLoader		m_loader;
	HTREEITEM			m_hManRoot;
	HTREEITEM			m_hWomenRoot;
	

public:
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif
	
	CDXTestView*		m_pDXTestView;

protected:
	DECLARE_MESSAGE_MAP()

	void InsertTypeItem( HTREEITEM hParent, long sex);
	void InsertItemsToType( HTREEITEM hParent, LPARAM lParam);

	bool TypeHasChild( long typeID, long sex);

	// �Ƿ������岿��item
	bool IsPartItem( HTREEITEM hItem);

	// ��װ
	void ChangePart( const ShowPartItem& part, CLayerOperatorType& lot);
	// �л���ʾ
	void ShowPart( const ShowPartItem& part, bool bShow, CLayerOperatorType& lot);

	// ����һ��
	HTREEITEM InsertOneItem( LPCTSTR name, bool hasChild, LPARAM lParam, int image, HTREEITEM hParent);

public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnTvnItemexpanding(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTvnSelchanged(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMClick(NMHDR *pNMHDR, LRESULT *pResult);
	virtual void OnInitialUpdate();
};


