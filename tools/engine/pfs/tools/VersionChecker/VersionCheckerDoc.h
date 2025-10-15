// VersionCheckerDoc.h : interface of the CVersionCheckerDoc class
//


#pragma once


class CVersionCheckerDoc : public CDocument
{
protected: // create from serialization only
	CVersionCheckerDoc();
	DECLARE_DYNCREATE(CVersionCheckerDoc)

// Attributes
public:

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();

	virtual void SetModifiedFlag(BOOL bModified = TRUE)
	{
	}

// Implementation
public:
	virtual ~CVersionCheckerDoc();

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
};


