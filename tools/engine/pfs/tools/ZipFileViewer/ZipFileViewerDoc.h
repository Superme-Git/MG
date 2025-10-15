// ZipFileViewerDoc.h : CZipFileViewerDoc 类的接口
//


#pragma once


class CZipFileViewerDoc : public CDocument
{
protected: // 仅从序列化创建
	CZipFileViewerDoc();
	DECLARE_DYNCREATE(CZipFileViewerDoc)

// 属性
public:

// 操作
public:

// 重写
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// 实现
public:
	virtual ~CZipFileViewerDoc();
protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
};


