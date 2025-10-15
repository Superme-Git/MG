// ShopEditorDoc.h : interface of the CShopEditorDoc class
//
#pragma once
#include "ShopItemManager.h"


// 清除文档内容之前的通告标志
#define HINT_BEFORE_DELETE_CONTENTS  0x0001

class CShopEditorDoc : public CDocument
{
protected: // create from serialization only
	CShopEditorDoc();
	DECLARE_DYNCREATE(CShopEditorDoc)

	

// Attributes
public:

	CShopItemManager	m_manager;

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// Implementation
public:
	virtual ~CShopEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual void DeleteContents(); // delete doc items etc
	afx_msg void OnUpdateFileSave(CCmdUI *pCmdUI);
};


