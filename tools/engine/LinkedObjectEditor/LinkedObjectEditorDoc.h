// LinkedObjectEditorDoc.h : CLinkedObjectEditorDoc ��Ľӿ�
//
#pragma once

#include "LinkedObjectEditorView.h"
#include "LinkedObjectInfoView.h"
#include "LinkedObjectStatusView.h"

#include "..\engine\map\plinkedobject.h"

class CLinkedObjectEditorDoc : public CDocument
{
protected: // �������л�����
	CLinkedObjectEditorDoc();
	DECLARE_DYNCREATE(CLinkedObjectEditorDoc)

// ����
private:
	Nuclear::PLinkedObject m_plantObject;

	CString m_strLinkedObjectFilePath;

// ����
public:
	Nuclear::PLinkedObject& GetLinkedObjectObject() { return m_plantObject; }

// ��д
public:
	virtual BOOL OnNewDocument();
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual void Serialize(CArchive& ar);

public:
	CLinkedObjectEditorView* GetLinkedObjectEditorView();
	CLinkedObjectInfoView* GetLinkedObjectInfoView();
	CLinkedObjectStatusView* GetLinkedObjectStatusView();

// ʵ��
public:
	virtual ~CLinkedObjectEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
public:
//	afx_msg void OnFileOpen();
//	afx_msg void OnFileSave();
//	afx_msg void OnFileSaveAs();
	afx_msg void OnUpdateFileSave(CCmdUI *pCmdUI);
};


