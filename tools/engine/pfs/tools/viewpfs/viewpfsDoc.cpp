// viewpfsDoc.cpp : implementation of the CviewpfsDoc class
//

#include "stdafx.h"
#include "viewpfs.h"
#include "viewpfsDoc.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CviewpfsDoc

IMPLEMENT_DYNCREATE(CviewpfsDoc, CDocument)

BEGIN_MESSAGE_MAP(CviewpfsDoc, CDocument)
END_MESSAGE_MAP()

// CviewpfsDoc construction/destruction

CviewpfsDoc::CviewpfsDoc()
{
}

CviewpfsDoc::~CviewpfsDoc()
{
}

BOOL CviewpfsDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// (SDI documents will reuse this document)
	m_filereader.GetFileCentralMap().clear();
	//((ZIPFILE::CRunFileReader::CRunFileCentralVector&)m_runfilereader.GetFileCentralVector()).clear();

	return TRUE;
}

// CviewpfsDoc serialization

void CviewpfsDoc::Serialize(CArchive& ar)
{
}

// CviewpfsDoc commands

BOOL CviewpfsDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	if (!CDocument::OnOpenDocument(lpszPathName))
		return FALSE;

	m_filereader.GetFileCentralMap().clear();
	// ((ZIPFILE::CRunFileReader::CRunFileCentralVector&)m_runfilereader.GetFileCentralVector()).clear();

	PFSX::CNativeFile nativefile;
	if( !nativefile.Open( lpszPathName, PFS::FM_EXCL, PFS::FA_RDONLY))
		return FALSE;
	
	try
	{
		m_filereader.SetFileIO( nativefile);
		// m_runfilereader.SetFileIO( nativefile);
	}
	catch (ZIPFILE::CZipFileBadFormat&)
	{
		AfxMessageBox( L"不是有效的pfs文件！");
		return FALSE;
	}

	return TRUE;
}
