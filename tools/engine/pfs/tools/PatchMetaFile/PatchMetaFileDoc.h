// PatchMetaFileDoc.h : interface of the CPatchMetaFileDoc class
//
#pragma once
#include "../../src/common.h"
#include "../../patchmetafile.h"
#include "../../versionmetafile.h"

class CPatchMetaFileDoc : public CDocument
{
protected: // create from serialization only
	CPatchMetaFileDoc();
	DECLARE_DYNCREATE(CPatchMetaFileDoc)

// Attributes
public:

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// Implementation
public:
	virtual ~CPatchMetaFileDoc();

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
	
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	afx_msg void OnUpdateFileSave(CCmdUI *pCmdUI);

	PFSX::CPatchMetaFile		m_pmf;
	PFSX::CVersionInfo			m_version;		// 保存最新的version
};


