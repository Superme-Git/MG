// ParticleSystemEditorDoc.cpp : CParticleSystemEditorDoc ���ʵ��
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"

#include "ParticleSystemEditorDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CParticleSystemEditorDoc

IMPLEMENT_DYNCREATE(CParticleSystemEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CParticleSystemEditorDoc, CDocument)
END_MESSAGE_MAP()


// CParticleSystemEditorDoc ����/����

CParticleSystemEditorDoc::CParticleSystemEditorDoc()
{
	// TODO: �ڴ����һ���Թ������

}

CParticleSystemEditorDoc::~CParticleSystemEditorDoc()
{
}

BOOL CParticleSystemEditorDoc::OnNewDocument()
{
//	if (!CDocument::OnNewDocument())
//		return FALSE;

	// TODO: �ڴ�������³�ʼ������
	// (SDI �ĵ������ø��ĵ�)
//	theApp.SetCurParticleSysDoc(this);

	return TRUE;
}

BOOL CParticleSystemEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
//	GetParticleParameterView()->SaveParticleSys(lpszPathName);
	return TRUE;
}

BOOL CParticleSystemEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
//	GetParticleParameterView()->OpenParticleSys(lpszPathName);
	return TRUE;
}


// CParticleSystemEditorDoc ���л�

void CParticleSystemEditorDoc::Serialize(CArchive& ar)
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


CParticleSystemEditorView* CParticleSystemEditorDoc::GetParticleSystemEditorView()
{
	CParticleSystemEditorView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CParticleSystemEditorView*>(pView) )
			return r;
	}

	return NULL;
}

CParticleParameterView* CParticleSystemEditorDoc::GetParticleParameterView()
{
	CParticleParameterView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CParticleParameterView*>(pView) )
			return r;
	}

	return NULL;
}

CParticleSysInfoView* CParticleSystemEditorDoc::GetParticleSysInfoView()
{
	CParticleSysInfoView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CParticleSysInfoView*>(pView) )
			return r;
	}

	return NULL;
}

// CParticleSystemEditorDoc ����
