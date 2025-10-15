// SoundEditorDoc.cpp : CSoundEditorDoc 类的实现
//

#include "stdafx.h"
#include "SoundApp.h"
#include "SoundEditor.h"

#include "SoundEditorDoc.h"

#include "xmlio\xmlio.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CSoundEditorDoc

IMPLEMENT_DYNCREATE(CSoundEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CSoundEditorDoc, CDocument)
END_MESSAGE_MAP()


// CSoundEditorDoc 构造/析构

CSoundEditorDoc::CSoundEditorDoc() : m_editState(EDIT_STATE_MAZE), m_pSelectedShape(NULL),
m_nSelectedPt(-1), m_bIsIngoreDragging(false),
m_bTestingDrawBkgArea(false), m_nUpdateViewSign(UVS_ALL)
{

}

CSoundEditorDoc::~CSoundEditorDoc()
{
	FreeAll();
}

BOOL CSoundEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	FreeAll();

	return TRUE;
}




// CSoundEditorDoc 序列化

void CSoundEditorDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: 在此添加存储代码
	}
	else
	{
		// TODO: 在此添加加载代码
	}
}


// CSoundEditorDoc 诊断

#ifdef _DEBUG
void CSoundEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CSoundEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

// CSoundEditorDoc 命令
BOOL CSoundEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	FreeAll();
	XMLIO::CFileReader fr;
	int retcode = fr.OpenNativeFile(lpszPathName);
	if (retcode != XMLIO::EC_SUCCESS)
	{
		if (retcode == ERROR_FILE_NOT_FOUND)
		{
			XMLIO::CFileWriter fw;
			XMLIO::CONode root;
			fw.GetRootNode(root);
			SaveAsXML(root);
			return XMLIO::EC_SUCCESS == fw.SaveNative(lpszPathName);
		}
		return FALSE;
	}
	XMLIO::CINode root;
	fr.GetRootNode(root);
	XMLIO::CNodeList rootnl;
	root.GetChildren(rootnl);
	XMLIO::CNodeList::iterator rnlIt = rootnl.begin();
	for (;rnlIt!=rootnl.end();++rnlIt)
	{
		if (rnlIt->GetType() != XMLIO::NT_ELEMENT)
			continue;
		if (rnlIt->GetName() == L"env")	//环境音
		{
			
		} else if (rnlIt->GetName() == L"bgs")	//背景音
		{

		} else {
			ASSERT(false);
		}
	}

	fr.CloseFile();
	return TRUE;
}

BOOL CSoundEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	XMLIO::CFileWriter fw;
	XMLIO::CONode root;
	fw.GetRootNode(root);
	SaveAsXML(root);
	int retcode = fw.SaveNative(lpszPathName);
	if (retcode == XMLIO::EC_SUCCESS)
	{
		SetModifiedFlag(FALSE);
		return TRUE;
	}
	return FALSE;
}

void CSoundEditorDoc::FreeAll()
{
	if (m_pSelectedShape)
		delete m_pSelectedShape;
	m_pSelectedShape = NULL;	
}

void CSoundEditorDoc::SaveAsXML(XMLIO::CONode &root)
{

}


void CSoundEditorDoc::SetMapSize(const Nuclear::CPOINT &size) 
{ 
	m_mapSize = size;
}
