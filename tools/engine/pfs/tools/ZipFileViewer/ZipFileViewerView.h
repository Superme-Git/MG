// ZipFileViewerView.h : CZipFileViewerView 类的接口
//


#pragma once


class CZipFileViewerView : public CEditView
{
protected: // 仅从序列化创建
	CZipFileViewerView();
	DECLARE_DYNCREATE(CZipFileViewerView)

// 属性
public:
	CZipFileViewerDoc* GetDocument() const;

// 操作
public:

// 重写
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

// 实现
public:
	virtual ~CZipFileViewerView();

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual void OnInitialUpdate();
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
};

inline CZipFileViewerDoc* CZipFileViewerView::GetDocument() const
   { return reinterpret_cast<CZipFileViewerDoc*>(m_pDocument); }

