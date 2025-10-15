// PatchMetaFileDoc.cpp : implementation of the CPatchMetaFileDoc class
//

#include "stdafx.h"
#include "PatchMetaFile.h"

#include "PatchMetaFileDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CPatchMetaFileDoc

IMPLEMENT_DYNCREATE(CPatchMetaFileDoc, CDocument)

BEGIN_MESSAGE_MAP(CPatchMetaFileDoc, CDocument)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVE, &CPatchMetaFileDoc::OnUpdateFileSave)
END_MESSAGE_MAP()

// CPatchMetaFileDoc construction/destruction

CPatchMetaFileDoc::CPatchMetaFileDoc()
{
	// TODO: add one-time construction code here
}

CPatchMetaFileDoc::~CPatchMetaFileDoc()
{
}

BOOL CPatchMetaFileDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)
	m_pmf.Close();

	return TRUE;
}

// CPatchMetaFileDoc serialization

void CPatchMetaFileDoc::Serialize(CArchive& ar)
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

// CPatchMetaFileDoc commands

BOOL CPatchMetaFileDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	if (!CDocument::OnOpenDocument(lpszPathName))
		return FALSE;

	PFSX::CNativeFile file;
	if( !file.Open( lpszPathName, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		AfxMessageBox( L"打开文件失败！");
		return FALSE;
	}

	if( !m_pmf.LoadFrom( file))
	{
		AfxMessageBox( L"不是有效的 PatchMeta 文件！");
		return FALSE;
	}

	return TRUE;
}

BOOL CPatchMetaFileDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	PFSX::CNativeFile file;
	if( !file.Open( lpszPathName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
	{
		AfxMessageBox( L"保存失败！");
		return FALSE;
	}

	if( !m_pmf.Write( file))
	{
		AfxMessageBox( L"保存失败！");
		return FALSE;
	}

	SetModifiedFlag( FALSE);

	if( m_version.m_Version != 0 && m_version.IsValid() )
	{
		if( IDYES == AfxMessageBox( L"最新的版本文件有修改，要保存版本文件么？", MB_YESNO))
		{
			wchar_t szFilter[] = L"version meta file|*.meta|all files|*.*|";
			CFileDialog dlgFile( FALSE, L"", L".version.meta", OFN_HIDEREADONLY|OFN_OVERWRITEPROMPT, szFilter);
			if( dlgFile.DoModal() == IDOK)
			{
				if( !PFSX::WriteVersionMetaFile( dlgFile.GetPathName().GetString(), m_version))
				{
					AfxMessageBox( L"保存版本文件失败！");
				}
			}
		}
	}

	return TRUE;
}

void CPatchMetaFileDoc::OnUpdateFileSave(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bModified);
}
