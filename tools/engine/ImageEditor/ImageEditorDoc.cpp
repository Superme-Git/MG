// ImageEditorDoc.cpp : CImageEditorDoc 类的实现
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


// CImageEditorDoc 构造/析构

CImageEditorDoc::CImageEditorDoc()
{
	// TODO: 在此添加一次性构造代码
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
	// TODO: 在此添加重新初始化代码
	// (SDI 文档将重用该文档)

	return TRUE;
}

// CImageEditorDoc 序列化

void CImageEditorDoc::Serialize(CArchive& ar)
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

// CImageEditorDoc 诊断

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

// CImageEditorDoc 命令

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
//		CString strInfo = _T("是否保存当前文件的修改？");
// 	}

	SetModifiedFlag(false);

	m_strImgFilePath = lpszPathName;
	FILE *f = NULL;
	_tfopen_s(&f, lpszPathName, _T("rb"));
	if( f == NULL ) return FALSE;

	m_imgObject.ResetImgObject(); //打开老版本文件..提示保存成新版本文件时候的bug..
	//比如：某些参数如mask信息在老版本中没有..那么就会导致打开老版本保存新版本时候一直重复覆盖以前的参数..
	BOOL ret = m_imgObject.Load(f) ? TRUE : FALSE;
	fclose(f);
	if(!ret)
		return ret;

	SetPicInit(false);

	if(m_imgObject.GetSourceVersion() < PImg::NEWVER) //3以内的
		SetModifiedFlag(true); //打开的是老版本文件..关闭时提示保存..为新文件。 字节序调整..

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
	swprintf(strFilter, L"文件(*%s)|*%s||", ImageSet_ExtName, ImageSet_ExtName);

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
	swprintf(strFilter, L"文件(*%s)|*%s||", ImageSet_ExtName, ImageSet_ExtName);

	CFileDialog dlg(true, strDefExt, NULL, OFN_HIDEREADONLY | OFN_FILEMUSTEXIST, strFilter, NULL);		
	dlg.DoModal();
	CString filePath = dlg.GetPathName();
	if( filePath.IsEmpty() )
		return;

	OnOpenDocument(filePath);
}

void CImageEditorDoc::SetTitle(LPCTSTR lpszTitle)
{
	// TODO: 在此添加专用代码和/或调用基类

	CDocument::SetTitle(lpszTitle);
}

void CImageEditorDoc::OnFileClose()
{
	if(IsModified())
	{
		CString strInfo = _T("是否保存当前文件的修改？");
		int res = MessageBox(NULL, strInfo, _T("提示"), MB_ICONQUESTION|MB_YESNOCANCEL);
		if(res == IDYES) //IDYES
		{
			OnSaveDocument(m_strImgFilePath);
		}
		else if(res == IDCANCEL)
		{
			return;
		}
		else
		{// res == IDNO 继续后面的处理...
		}
	}
	CDocument::OnFileClose();
}


void CImageEditorDoc::OnCloseDocument()
{
	// TODO: 在此添加专用代码和/或调用基类

	CDocument::OnCloseDocument();
}

void CImageEditorDoc::OnSetbkcolor()
{
	CColorDialog dlg;
	if( IDOK != dlg.DoModal() )
		return;

	COLORREF color = dlg.GetColor(); //BGR --> ARGB (alpha值为255, 画纯色背景用)
	m_dwBKcolor = (0xffffffff & 0xff000000) | (color & 0x000000ff)<<16 | (color & 0x0000ff00) | (color & 0x00ff0000)>>16;
	GetImageEditorView()->Invalidate(false);
}

void CImageEditorDoc::OnUpdateFileSave(CCmdUI *pCmdUI)
{
	BOOL bres = IsModified();
	pCmdUI->Enable(bres);
}
