// EditMetaFileDoc.cpp : implementation of the CEditMetaFileDoc class
//

#include "stdafx.h"
#include "EditMetaFile.h"

#include "EditMetaFileDoc.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CEditMetaFileDoc

IMPLEMENT_DYNCREATE(CEditMetaFileDoc, CDocument)

BEGIN_MESSAGE_MAP(CEditMetaFileDoc, CDocument)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVE, &CEditMetaFileDoc::OnUpdateFileSave)
END_MESSAGE_MAP()


// CEditMetaFileDoc construction/destruction

CEditMetaFileDoc::CEditMetaFileDoc()
{
	// TODO: add one-time construction code here

}

CEditMetaFileDoc::~CEditMetaFileDoc()
{
}

BOOL CEditMetaFileDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}




// CEditMetaFileDoc serialization

void CEditMetaFileDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}


// CEditMetaFileDoc diagnostics

#ifdef _DEBUG
void CEditMetaFileDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CEditMetaFileDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG


// CEditMetaFileDoc commands

BOOL CEditMetaFileDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	if (!CDocument::OnOpenDocument(lpszPathName))
		return FALSE;

	// TODO:  Add your specialized creation code here
	m_map.clear();
	PFSX::CNativeFile file;
	if( !file.Open( lpszPathName, PFS::FM_EXCL, PFS::FA_RDONLY))
		return FALSE;
	if( !PFS::CMetaFileRead::DoRead( file, m_map))
	{
		// 不再支持 XML MetaFile 的读取
		//file.Seek( 0, PFS::FSM_SET);
		//m_map.clear();
		//if( !XMLMETAFILE::CMetaFileRead::DoRead( file, m_map))
		return FALSE;
	}
	return TRUE;
}

BOOL CEditMetaFileDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	// TODO: Add your specialized code here and/or call the base class
	PFSX::CNativeFile file;
	if( !file.Open( lpszPathName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
		return FALSE;
	if( !PFS::CMetaFileWrite::DoWrite( file, m_map))
		return FALSE;
	SetModifiedFlag( FALSE);
	return TRUE;
	//return CDocument::OnSaveDocument(lpszPathName);
}

void CEditMetaFileDoc::OnUpdateFileSave(CCmdUI *pCmdUI)
{
	// TODO: Add your command update UI handler code here
	pCmdUI->Enable( m_bModified);
}
