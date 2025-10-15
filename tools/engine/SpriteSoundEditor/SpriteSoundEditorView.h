// SpriteSoundEditorView.h : CSpriteSoundEditorView ��Ľӿ�
//


#pragma once


class CSpriteSoundEditorView : public CView
{
protected: // �������л�����
	CSpriteSoundEditorView();
	DECLARE_DYNCREATE(CSpriteSoundEditorView)

// ����
private:
	bool m_bIsInited;

// ����
private:
	void MainLoop();

public:
	CSpriteSoundEditorDoc* GetDocument() const;


public:

// ��д
public:
	virtual void OnDraw(CDC* pDC);  // ��д�Ի��Ƹ���ͼ
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:

// ʵ��
public:
	virtual ~CSpriteSoundEditorView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnTimer(UINT_PTR nIDEvent);
};

#ifndef _DEBUG  // SpriteSoundEditorView.cpp �еĵ��԰汾
inline CSpriteSoundEditorDoc* CSpriteSoundEditorView::GetDocument() const
   { return reinterpret_cast<CSpriteSoundEditorDoc*>(m_pDocument); }
#endif

