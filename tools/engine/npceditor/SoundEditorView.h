// SoundEditorView.h : CSoundEditorView ��Ľӿ�
//
#pragma once
#include "MainDlg.h"


class CSoundEditorView : public CView
{
protected: // �������л�����
	CSoundEditorView();
	DECLARE_DYNCREATE(CSoundEditorView)

// ����
private:
	
	bool m_bIsInited;
public:
	CSoundEditorDoc* GetDocument() const;
	CMainDlg m_mainDlg;

// ����
public:

// ��д
public:
	virtual void OnDraw(CDC* pDC);  // ��д�Ի��Ƹ���ͼ
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

// ʵ��
public:
	virtual ~CSoundEditorView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnFileOpen();
protected:
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
	afx_msg void OnTool(UINT nID);
	afx_msg void OnUpdateTool(CCmdUI* pCmdUI);
	afx_msg void OnKeyUp(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnDrawGird();
	afx_msg void OnUpdateDrawGird(CCmdUI *pCmdUI);
public:
	afx_msg void OnDrawBlock();
	afx_msg void OnUpdateDrawBlock(CCmdUI *pCmdUI);
	afx_msg void OnSavemaze();
	afx_msg void OnAllScreenBrush();
	afx_msg void OnUpdateAllScreenBrush(CCmdUI *pCmdUI);
};

#ifndef _DEBUG  // SoundEditorView.cpp �еĵ��԰汾
inline CSoundEditorDoc* CSoundEditorView::GetDocument() const
   { return reinterpret_cast<CSoundEditorDoc*>(m_pDocument); }
#endif

