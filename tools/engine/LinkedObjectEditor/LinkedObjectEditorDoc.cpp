// LinkedObjectEditorDoc.cpp : CLinkedObjectEditorDoc ���ʵ��
//

#include "stdafx.h"
#include "LinkedObjectEditor.h"

#include "LinkedObjectEditorDoc.h"

#include "LinkedObjectEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CLinkedObjectEditorDoc

IMPLEMENT_DYNCREATE(CLinkedObjectEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CLinkedObjectEditorDoc, CDocument)
//	ON_COMMAND(ID_FILE_OPEN, &CLinkedObjectEditorDoc::OnFileOpen)
//	ON_COMMAND(ID_FILE_SAVE, &CLinkedObjectEditorDoc::OnFileSave)
//	ON_COMMAND(ID_FILE_SAVE_AS, &CLinkedObjectEditorDoc::OnFileSaveAs)
ON_UPDATE_COMMAND_UI(ID_FILE_SAVE, &CLinkedObjectEditorDoc::OnUpdateFileSave)
END_MESSAGE_MAP()


// CLinkedObjectEditorDoc ����/����

CLinkedObjectEditorDoc::CLinkedObjectEditorDoc()
{
	// TODO: �ڴ����һ���Թ������

}

CLinkedObjectEditorDoc::~CLinkedObjectEditorDoc()
{
}


CLinkedObjectEditorView* CLinkedObjectEditorDoc::GetLinkedObjectEditorView()
{
	CLinkedObjectEditorView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CLinkedObjectEditorView*>(pView) )
			return r;
	}
	OutputDebugString(_T("GetLinkedObjectEditorView return NULL\n"));
	return NULL;
}

CLinkedObjectInfoView* CLinkedObjectEditorDoc::GetLinkedObjectInfoView()
{
	CLinkedObjectInfoView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CLinkedObjectInfoView*>(pView) )
			return r;
	}
	OutputDebugString(_T("GetLinkedObjectInfoView return NULL\n"));
	return NULL;
}

CLinkedObjectStatusView* CLinkedObjectEditorDoc::GetLinkedObjectStatusView()
{
	CLinkedObjectStatusView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CLinkedObjectStatusView*>(pView) )
			return r;
	}
	OutputDebugString(_T("GetLinkedObjectStatusView return NULL\n"));
	return NULL;
}

BOOL CLinkedObjectEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: �ڴ�������³�ʼ������
	// (SDI �ĵ������ø��ĵ�)

	return TRUE;
}




// CLinkedObjectEditorDoc ���л�

void CLinkedObjectEditorDoc::Serialize(CArchive& ar)
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


// CLinkedObjectEditorDoc ���

#ifdef _DEBUG
void CLinkedObjectEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CLinkedObjectEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG


// CLinkedObjectEditorDoc ����
/*
void CLinkedObjectEditorDoc::OnFileOpen()
{
	CString strDefExt = _T("plt");
	CString strFilter = _T("(*.plt)|*.plt||");

	CFileDialog dlg(true, strDefExt, NULL, OFN_HIDEREADONLY | OFN_FILEMUSTEXIST, strFilter, NULL);		
	dlg.DoModal();
	CString filePath = dlg.GetPathName();
	if( filePath.IsEmpty() )
		return;

	OnOpenDocument(filePath);
}

void CLinkedObjectEditorDoc::OnFileSave()
{
	OnSaveDocument(m_strLinkedObjectFilePath);
}

void CLinkedObjectEditorDoc::OnFileSaveAs()
{
CString strDefExt = _T("plt");
CString strFileName = _T("*.plt");
CString strFilter = _T("plt|");

CFileDialog dlg(false, strDefExt, strFileName, OFN_OVERWRITEPROMPT, strFilter, NULL);		
dlg.DoModal();
CString filePath = dlg.GetPathName();
if( filePath.IsEmpty() )
return;

OnSaveDocument(filePath);
}
*/

//����Ҫ�������listCtrl�е�˳��...���ǻ��Ʋ�ι�ϵ..

BOOL CLinkedObjectEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	// TODO: Add your specialized code here and/or call the base class
	m_plantObject.m_ptCentPos = GetLinkedObjectEditorView()->m_ptCentPos;
	for(int i=0; i<4; i++)
		m_plantObject.m_ptBasePos[i] = GetLinkedObjectEditorView()->m_ptBasePos[i];

	m_plantObject.m_vectorLinkedObjectInfo.clear();
	int cnt = GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.GetItemCount();
	for(int i=0; i<cnt; i++)
	{
		DWORD fileID = _wtoi( GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.GetItemText(i, 0) );
		CLinkedObjectEditorView::HashMapLinkedObjectInfo::const_iterator it = GetLinkedObjectEditorView()->m_mapLinkedObjectInfo.find(fileID);
		if(it != GetLinkedObjectEditorView()->m_mapLinkedObjectInfo.end())
		{
			Nuclear::PLANTPICINFO LinkedObjectPicInfo = it->second;
			LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_fSpeed = fabs( LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_fSpeed );
			LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_nSkewSpeedX = abs( LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_nSkewSpeedX );

			m_plantObject.m_vectorLinkedObjectInfo.push_back( LinkedObjectPicInfo );
		}
		else
		{
			MessageBox(0, L"������..CLinkedObjectEditorDoc::OnSaveDocument()\n", 0, 0);
		}
	}

	//����ֲ������Χ��..
	m_plantObject.m_rctMaxBox = GetLinkedObjectEditorView()->ComputeLinkedObjectMaxBox(m_plantObject);
	//����

	m_strLinkedObjectFilePath = lpszPathName;
	m_strLinkedObjectFilePath.MakeLower();

	FILE *f = NULL;
	_tfopen_s(&f, m_strLinkedObjectFilePath, _T("wb"));
	if( f == NULL ) return FALSE;

	BOOL ret = m_plantObject.Save(f) ? TRUE : FALSE;

	fclose(f);
	SetModifiedFlag(false);

	return ret;

	//return CDocument::OnSaveDocument(lpszPathName);
}

BOOL CLinkedObjectEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	m_strLinkedObjectFilePath = lpszPathName;
	FILE *f = NULL;
	_tfopen_s(&f, lpszPathName, _T("rb"));
	if( f == NULL ) return FALSE;

	BOOL ret = m_plantObject.Load(f) ? TRUE : FALSE;
	fclose(f);
	if(!ret)
		return ret;

	GetLinkedObjectEditorView()->SetLinkedObjectData(m_plantObject);
	GetLinkedObjectEditorView()->CreateDTList(m_plantObject);
	GetLinkedObjectInfoView()->SetDragFlag(FALSE);

	UpdateAllViews(NULL);
	SetTitle(m_strLinkedObjectFilePath);
	return ret;
}

void CLinkedObjectEditorDoc::OnUpdateFileSave(CCmdUI *pCmdUI)
{
	BOOL bres = IsModified();
	pCmdUI->Enable(bres);
}
