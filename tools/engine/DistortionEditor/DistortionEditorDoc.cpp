// DistortionEditorDoc.cpp : CDistortionEditorDoc ���ʵ��
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


// CDistortionEditorDoc ����/����

CDistortionEditorDoc::CDistortionEditorDoc()
{
	// TODO: �ڴ����һ���Թ������

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

	// TODO: �ڴ�������³�ʼ������
	// (SDI �ĵ������ø��ĵ�)

	return TRUE;
}




// CDistortionEditorDoc ���л�

void CDistortionEditorDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: �ڴ���Ӵ洢����
	}
	else
	{
		// TODO: �ڴ���Ӽ��ش���
	}
}


// CDistortionEditorDoc ���

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
//	return CDocument::OnSaveDocument(lpszPathName); ����Ҫ��� ...
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
