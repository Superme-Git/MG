// ImageEditorDoc.h : CImageEditorDoc 类的接口
//

#pragma once

#include "..\engine\map\pimg.h"
#include "ImageEditorView.h"
#include "ImageInfoView.h"
#include "ImageStatusView.h"

using Nuclear::PImg;

class CImageEditorDoc : public CDocument
{
protected: // 仅从序列化创建
	CImageEditorDoc();
	DECLARE_DYNCREATE(CImageEditorDoc)

private:
	PImg m_imgObject;
	bool m_bPicInit;
	CString m_strImgFilePath;
	DWORD m_dwBKcolor;

public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);
	CImageEditorView* GetImageEditorView();
	CImageInfoView* GetImageInfoView();
	CImageStatusView* GetImageStatusView();

// 实现
public:
	virtual ~CImageEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()

public:
	PImg& GetImgObject() { return m_imgObject; }
	bool IsPicInit() const { return m_bPicInit; }
	void SetPicInit(bool f) { m_bPicInit = f; }
	DWORD GetBackGroundColor() { return m_dwBKcolor; }

public:
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	afx_msg void OnFileSave();
	afx_msg void OnFileSaveAs();
	afx_msg void OnFileOpen();
	virtual void SetTitle(LPCTSTR lpszTitle);
	afx_msg void OnFileClose();
	virtual void OnCloseDocument();

private:
	afx_msg void OnSetbkcolor();

	afx_msg void OnUpdateFileSave(CCmdUI *pCmdUI);
};


