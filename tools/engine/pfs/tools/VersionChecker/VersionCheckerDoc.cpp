// VersionCheckerDoc.cpp : implementation of the CVersionCheckerDoc class
//

#include "stdafx.h"
#include "VersionChecker.h"

#include "VersionCheckerDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CVersionCheckerDoc

IMPLEMENT_DYNCREATE(CVersionCheckerDoc, CDocument)

BEGIN_MESSAGE_MAP(CVersionCheckerDoc, CDocument)
END_MESSAGE_MAP()


// CVersionCheckerDoc construction/destruction

CVersionCheckerDoc::CVersionCheckerDoc()
{
	// TODO: add one-time construction code here

}

CVersionCheckerDoc::~CVersionCheckerDoc()
{
}

BOOL CVersionCheckerDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}

// CVersionCheckerDoc commands

BOOL CVersionCheckerDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	return TRUE;
	//return CDocument::OnSaveDocument(lpszPathName);
}

BOOL CVersionCheckerDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	if (!CDocument::OnOpenDocument(lpszPathName))
		return FALSE;

	return TRUE;
}
