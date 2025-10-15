// LinkedObjectEditorDoc.h : CLinkedObjectEditorDoc 类的接口
//
#pragma once

#include "LinkedObjectEditorView.h"
#include "LinkedObjectInfoView.h"
#include "LinkedObjectStatusView.h"

#include "..\engine\map\plinkedobject.h"

class CLinkedObjectEditorDoc : public CDocument
{
protected: // 仅从序列化创建
	CLinkedObjectEditorDoc();
	DECLARE_DYNCREATE(CLinkedObjectEditorDoc)

// 属性
private:
	Nuclear::PLinkedObject m_plantObject;

	CString m_strLinkedObjectFilePath;

// 操作
public:
	Nuclear::PLinkedObject& GetLinkedObjectObject() { return m_plantObject; }

// 重写
public:
	virtual BOOL OnNewDocument();
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual void Serialize(CArchive& ar);

public:
	CLinkedObjectEditorView* GetLinkedObjectEditorView();
	CLinkedObjectInfoView* GetLinkedObjectInfoView();
	CLinkedObjectStatusView* GetLinkedObjectStatusView();

// 实现
public:
	virtual ~CLinkedObjectEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
//	afx_msg void OnFileOpen();
//	afx_msg void OnFileSave();
//	afx_msg void OnFileSaveAs();
	afx_msg void OnUpdateFileSave(CCmdUI *pCmdUI);
};


