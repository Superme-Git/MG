// DistortionEditorDoc.cpp : CDistortionEditorDoc 类的实现
//

#include "stdafx.h"
#include "DistortionEditor.h"

#include "DistortionEditorDoc.h"

#include "DistortionEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CDistortionEditorDoc

IMPLEMENT_DYNCREATE(CDistortionEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CDistortionEditorDoc, CDocument)

END_MESSAGE_MAP()


// CDistortionEditorDoc 构造/析构

CDistortionEditorDoc::CDistortionEditorDoc()
{
	// TODO: 在此添加一次性构造代码

}

CDistortionEditorDoc::~CDistortionEditorDoc()
{
}


CDistortionEditorView* CDistortionEditorDoc::GetDistortionEditorView()
{
	CDistortionEditorView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CDistortionEditorView*>(pView) )
			return r;
	}
	OutputDebugString(_T("GetDistortionEditorView return NULL\n"));
	return NULL;
}

CDistortionInfoView* CDistortionEditorDoc::GetDistortionInfoView()
{
	CDistortionInfoView *r;
	POSITION pos = GetFirstViewPosition();
	while(pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CDistortionInfoView*>(pView) )
			return r;
	}
	OutputDebugString(_T("GetDistortionInfoView return NULL\n"));
	return NULL;
}

CDistortionStatusView* CDistortionEditorDoc::GetDistortionStatusView()
{
	CDistortionStatusView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CDistortionStatusView*>(pView) )
			return r;
	}
	OutputDebugString(_T("GetDistortionStatusView return NULL\n"));
	return NULL;
}

BOOL CDistortionEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: 在此添加重新初始化代码
	// (SDI 文档将重用该文档)

	return TRUE;
}




// CDistortionEditorDoc 序列化

void CDistortionEditorDoc::Serialize(CArchive& ar)
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


// CDistortionEditorDoc 诊断

#ifdef _DEBUG
void CDistortionEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CDistortionEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

BOOL CDistortionEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	FILE *f = NULL;

	m_strDistortionFilePath = lpszPathName;
	m_strDistortionFilePath.MakeLower();

	_tfopen_s(&f, m_strDistortionFilePath, _T("wb"));
	if( f == NULL ) return FALSE;

	if(m_distortionObject.m_distortionObj.strLightFile != L"")
	{
		m_distortionObject.m_bLightFlag = true;
	}
	BOOL ret = m_distortionObject.Save(f) ? TRUE : FALSE;

	fclose(f);
	SetModifiedFlag(false);

	return true;
//	return CDocument::OnSaveDocument(lpszPathName); 不能要这个 ...
}

BOOL CDistortionEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	m_strDistortionFilePath = lpszPathName;
	FILE *f = NULL;
	_tfopen_s(&f, lpszPathName, _T("rb"));
	if( f == NULL ) return FALSE;

	BOOL ret = false;
	ret = m_distortionObject.Load(f) ? TRUE : FALSE;
	fclose(f);
	if(!ret)
		return ret;

	UpdateAllViews(NULL);
	SetTitle(m_strDistortionFilePath);

	GetDistortionEditorView()->m_bLoadFile = true;
	GetDistortionInfoView()->Updatectrl();

	return ret;
}
