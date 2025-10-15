// EditMetaFileDoc.h : interface of the CEditMetaFileDoc class
//


#pragma once
#include "../../src/common.h"

class CEditMetaFileDoc : public CDocument
{
protected: // create from serialization only
	CEditMetaFileDoc();
	DECLARE_DYNCREATE(CEditMetaFileDoc)

// Attributes
public:
	PFS::CMetaInfoMap		m_map;

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// Implementation
public:
	virtual ~CEditMetaFileDoc();
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
public:
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	afx_msg void OnUpdateFileSave(CCmdUI *pCmdUI);
};


