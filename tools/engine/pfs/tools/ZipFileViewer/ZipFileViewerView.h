// ZipFileViewerView.h : CZipFileViewerView ��Ľӿ�
//


#pragma once


class CZipFileViewerView : public CEditView
{
protected: // �������л�����
	CZipFileViewerView();
	DECLARE_DYNCREATE(CZipFileViewerView)

// ����
public:
	CZipFileViewerDoc* GetDocument() const;

// ����
public:

// ��д
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

// ʵ��
public:
	virtual ~CZipFileViewerView();

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual void OnInitialUpdate();
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
};

inline CZipFileViewerDoc* CZipFileViewerView::GetDocument() const
   { return reinterpret_cast<CZipFileViewerDoc*>(m_pDocument); }

