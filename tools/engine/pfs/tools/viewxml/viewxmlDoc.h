// viewxmlDoc.h : interface of the CviewxmlDoc class
//
#pragma once
#include "../../pfs2.h"
#include "../../../xmlio/xmlio.h"

class CviewxmlDoc : public CDocument
{
protected: // create from serialization only
	CviewxmlDoc();
	DECLARE_DYNCREATE(CviewxmlDoc)

// Attributes
public:

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();

// Implementation
public:
	virtual ~CviewxmlDoc();

	BOOL m_bOpen;
	XMLIO::CFileReader	m_reader;

protected:
	void WriteNode( const XMLIO::CINode& nodeSrc, XMLIO::CONode& nodeDes);

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()

	
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	afx_msg void OnUpdateFileSave(CCmdUI *pCmdUI);
};


