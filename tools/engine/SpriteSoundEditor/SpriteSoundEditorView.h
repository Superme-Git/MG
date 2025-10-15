// SpriteSoundEditorView.h : CSpriteSoundEditorView 类的接口
//


#pragma once


class CSpriteSoundEditorView : public CView
{
protected: // 仅从序列化创建
	CSpriteSoundEditorView();
	DECLARE_DYNCREATE(CSpriteSoundEditorView)

// 属性
private:
	bool m_bIsInited;

// 操作
private:
	void MainLoop();

public:
	CSpriteSoundEditorDoc* GetDocument() const;


public:

// 重写
public:
	virtual void OnDraw(CDC* pDC);  // 重写以绘制该视图
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:

// 实现
public:
	virtual ~CSpriteSoundEditorView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnTimer(UINT_PTR nIDEvent);
};

#ifndef _DEBUG  // SpriteSoundEditorView.cpp 中的调试版本
inline CSpriteSoundEditorDoc* CSpriteSoundEditorView::GetDocument() const
   { return reinterpret_cast<CSpriteSoundEditorDoc*>(m_pDocument); }
#endif

