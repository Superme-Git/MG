// ImageEditorDoc.cpp : CImageEditorDoc ���ʵ��
//

#include "stdafx.h"
#include "ImageEditor.h"
#include "ImageEditorDoc.h"

#include "..\engine/common\log.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

using namespace Nuclear;

// CImageEditorDoc

IMPLEMENT_DYNCREATE(CImageEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CImageEditorDoc, CDocument)
	ON_COMMAND(ID_FILE_SAVE, &CImageEditorDoc::OnFileSave)
	ON_COMMAND(ID_FILE_SAVE_AS, &CImageEditorDoc::OnFileSaveAs)
	ON_COMMAND(ID_FILE_OPEN, &CImageEditorDoc::OnFileOpen)
	ON_COMMAND(ID_FILE_CLOSE, &CImageEditorDoc::OnFileClose)

	ON_COMMAND(ID_SETBKCOLOR, &CImageEditorDoc::OnSetbkcolor)
	
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVE, &CImageEditorDoc::OnUpdateFileSave)
END_MESSAGE_MAP()


// CImageEditorDoc ����/����

CImageEditorDoc::CImageEditorDoc()
{
	// TODO: �ڴ����һ���Թ������
	m_bPicInit = false;
	m_strImgFilePath = _T("");
	m_dwBKcolor = 0xfff0a080;//ARGB
}

CImageEditorDoc::~CImageEditorDoc()
{
}

CImageEditorView* CImageEditorDoc::GetImageEditorView()
{
	CImageEditorView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CImageEditorView*>(pView) )
			return r;
	}
	XPALARM(L"GetImageEditorView return NULL");
	return NULL;
}

CImageInfoView* CImageEditorDoc::GetImageInfoView()
{
	CImageInfoView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CImageInfoView*>(pView) )
			return r;
	}
	XPALARM(L"GetImageInfoView return NULL");
	return NULL;
}

CImageStatusView* CImageEditorDoc::GetImageStatusView()
{
	CImageStatusView *r;
	POSITION pos = GetFirstViewPosition();
	while (pos != NULL)
	{
		CView* pView = (CView*)GetNextView(pos);
		if( r = dynamic_cast<CImageStatusView*>(pView) )
			return r;
	}
	XPALARM(L"GetImageStatusView return NULL");
	return NULL;
}

BOOL CImageEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	CString str = GetTitle();
	// TODO: �ڴ�������³�ʼ������
	// (SDI �ĵ������ø��ĵ�)

	return TRUE;
}

// CImageEditorDoc ���л�

void CImageEditorDoc::Serialize(CArchive& ar)
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

// CImageEditorDoc ���

#ifdef _DEBUG
void CImageEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CImageEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

// CImageEditorDoc ����

BOOL CImageEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	// TODO: Add your specialized code here and/or call the base class

	m_strImgFilePath = lpszPathName;

	m_strImgFilePath.MakeLower();

	FILE *f = NULL;
	_tfopen_s(&f, m_strImgFilePath, _T("wb"));
	if( f == NULL ) return FALSE;

	BOOL ret = m_imgObject.Save(f) ? TRUE : FALSE;

	fclose(f);
	SetModifiedFlag(false);

	return ret;

	//return CDocument::OnSaveDocument(lpszPathName);
}

BOOL CImageEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
//	if( IsModified()){ 
//		CString strInfo = _T("�Ƿ񱣴浱ǰ�ļ����޸ģ�");
// 	}

	SetModifiedFlag(false);

	m_strImgFilePath = lpszPathName;
	FILE *f = NULL;
	_tfopen_s(&f, lpszPathName, _T("rb"));
	if( f == NULL ) return FALSE;

	m_imgObject.ResetImgObject(); //���ϰ汾�ļ�..��ʾ������°汾�ļ�ʱ���bug..
	//���磺ĳЩ������mask��Ϣ���ϰ汾��û��..��ô�ͻᵼ�´��ϰ汾�����°汾ʱ��һֱ�ظ�������ǰ�Ĳ���..
	BOOL ret = m_imgObject.Load(f) ? TRUE : FALSE;
	fclose(f);
	if(!ret)
		return ret;

	SetPicInit(false);

	if(m_imgObject.GetSourceVersion() < PImg::NEWVER) //3���ڵ�
		SetModifiedFlag(true); //�򿪵����ϰ汾�ļ�..�ر�ʱ��ʾ����..Ϊ���ļ��� �ֽ������..

	GetImageEditorView()->SetViewSize(m_imgObject.m_nFileWidth, m_imgObject.m_nFileWidth);

	//
	GetImageEditorView()->InitBorderCentPos();

	GetImageInfoView()->UpdateControlState(false);

	UpdateAllViews(NULL);

	SetTitle(m_strImgFilePath);

	return ret;
}

void CImageEditorDoc::OnFileSave()
{
	OnSaveDocument(m_strImgFilePath);
}

void CImageEditorDoc::OnFileSaveAs()
{
	CString strDefExt = ImageSet_ExtName;
	CString strFileName = ImageSet_ExtName;
	WCHAR strFilter[256];
	swprintf(strFilter, L"�ļ�(*%s)|*%s||", ImageSet_ExtName, ImageSet_ExtName);

	CFileDialog dlg(false, strDefExt, strFileName, OFN_OVERWRITEPROMPT, strFilter, NULL);		
	dlg.DoModal();
	CString filePath = dlg.GetPathName();
	if( filePath.IsEmpty() )
		return;

	OnSaveDocument(filePath);
}

void CImageEditorDoc::OnFileOpen()
{
	CString strDefExt = ImageSet_ExtName;
	WCHAR strFilter[256];
	swprintf(strFilter, L"�ļ�(*%s)|*%s||", ImageSet_ExtName, ImageSet_ExtName);

	CFileDialog dlg(true, strDefExt, NULL, OFN_HIDEREADONLY | OFN_FILEMUSTEXIST, strFilter, NULL);		
	dlg.DoModal();
	CString filePath = dlg.GetPathName();
	if( filePath.IsEmpty() )
		return;

	OnOpenDocument(filePath);
}

void CImageEditorDoc::SetTitle(LPCTSTR lpszTitle)
{
	// TODO: �ڴ����ר�ô����/����û���

	CDocument::SetTitle(lpszTitle);
}

void CImageEditorDoc::OnFileClose()
{
	if(IsModified())
	{
		CString strInfo = _T("�Ƿ񱣴浱ǰ�ļ����޸ģ�");
		int res = MessageBox(NULL, strInfo, _T("��ʾ"), MB_ICONQUESTION|MB_YESNOCANCEL);
		if(res == IDYES) //IDYES
		{
			OnSaveDocument(m_strImgFilePath);
		}
		else if(res == IDCANCEL)
		{
			return;
		}
		else
		{// res == IDNO ��������Ĵ���...
		}
	}
	CDocument::OnFileClose();
}


void CImageEditorDoc::OnCloseDocument()
{
	// TODO: �ڴ����ר�ô����/����û���

	CDocument::OnCloseDocument();
}

void CImageEditorDoc::OnSetbkcolor()
{
	CColorDialog dlg;
	if( IDOK != dlg.DoModal() )
		return;

	COLORREF color = dlg.GetColor(); //BGR --> ARGB (alphaֵΪ255, ����ɫ������)
	m_dwBKcolor = (0xffffffff & 0xff000000) | (color & 0x000000ff)<<16 | (color & 0x0000ff00) | (color & 0x00ff0000)>>16;
	GetImageEditorView()->Invalidate(false);
}

void CImageEditorDoc::OnUpdateFileSave(CCmdUI *pCmdUI)
{
	BOOL bres = IsModified();
	pCmdUI->Enable(bres);
}
