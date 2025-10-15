// SoundEditorView.h : CSoundEditorView 类的接口
//
#pragma once
#include "MainDlg.h"


class CSoundEditorView : public CView
{
protected: // 仅从序列化创建
	CSoundEditorView();
	DECLARE_DYNCREATE(CSoundEditorView)

// 属性
private:
	
	bool m_bIsInited;
public:
	CSoundEditorDoc* GetDocument() const;
	CMainDlg m_mainDlg;

// 操作
public:

// 重写
public:
	virtual void OnDraw(CDC* pDC);  // 重写以绘制该视图
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

// 实现
public:
	virtual ~CSoundEditorView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
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

#ifndef _DEBUG  // SoundEditorView.cpp 中的调试版本
inline CSoundEditorDoc* CSoundEditorView::GetDocument() const
   { return reinterpret_cast<CSoundEditorDoc*>(m_pDocument); }
#endif

