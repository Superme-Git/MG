// ParticleSystemEditorDoc.cpp : CParticleSystemEditorDoc 类的实现
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


// CParticleSystemEditorDoc 构造/析构

CParticleSystemEditorDoc::CParticleSystemEditorDoc()
{
	// TODO: 在此添加一次性构造代码

}

CParticleSystemEditorDoc::~CParticleSystemEditorDoc()
{
}

BOOL CParticleSystemEditorDoc::OnNewDocument()
{
//	if (!CDocument::OnNewDocument())
//		return FALSE;

	// TODO: 在此添加重新初始化代码
	// (SDI 文档将重用该文档)
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


// CParticleSystemEditorDoc 序列化

void CParticleSystemEditorDoc::Serialize(CArchive& ar)
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

// CParticleSystemEditorDoc 命令
