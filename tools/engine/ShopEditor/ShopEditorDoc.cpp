// ShopEditorDoc.cpp : implementation of the CShopEditorDoc class
//

#include "stdafx.h"
#include "ShopEditor.h"
#include "ShopEditorDoc.h"
#include "MainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CShopEditorDoc

IMPLEMENT_DYNCREATE(CShopEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CShopEditorDoc, CDocument)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVE, &CShopEditorDoc::OnUpdateFileSave)
END_MESSAGE_MAP()


// CShopEditorDoc construction/destruction

CShopEditorDoc::CShopEditorDoc()
{
	// TODO: add one-time construction code here

}

CShopEditorDoc::~CShopEditorDoc()
{
}

BOOL CShopEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}




// CShopEditorDoc serialization

void CShopEditorDoc::Serialize(CArchive& ar)
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


// CShopEditorDoc diagnostics

#ifdef _DEBUG
void CShopEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CShopEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG


// CShopEditorDoc commands

BOOL CShopEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	//if (!CDocument::OnOpenDocument(lpszPathName))
	//	return FALSE;
	bool res = m_manager.Load( lpszPathName);
	if( res )
	{
		SetModifiedFlag( FALSE);
	}
	return res;
}

// 文件名只能保存为shop.xml
bool CheckFileName( LPCTSTR lpszPathName)
{
	static wchar_t file[] = L"\\shop.xml";
	size_t filelen = wcslen( file);
	size_t len = wcslen( lpszPathName);
	if( len <= filelen)
	{
		return false;
	}
	lpszPathName += ( len - filelen);
	return ( 0 == wcscmp( lpszPathName, file));
}

BOOL CShopEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	// TODO: Add your specialized code here and/or call the base class
	if( !CheckFileName( lpszPathName))
	{
		AfxMessageBox( L"文件名只能保存为“shop.xml”");
		return FALSE;
	}
	
	bool res = m_manager.Save( lpszPathName);
	if( res )
	{
		SetModifiedFlag( FALSE);
	}
	return res;
}

void CShopEditorDoc::DeleteContents() // delete doc items etc
{
	UpdateAllViews(NULL, HINT_BEFORE_DELETE_CONTENTS, NULL );

	m_manager.Close();
}

void CShopEditorDoc::OnUpdateFileSave(CCmdUI *pCmdUI)
{
	// TODO: Add your command update UI handler code here
	pCmdUI->Enable( m_bModified);
}
