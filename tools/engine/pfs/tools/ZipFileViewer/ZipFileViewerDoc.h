// ZipFileViewerDoc.h : CZipFileViewerDoc ��Ľӿ�
//


#pragma once


class CZipFileViewerDoc : public CDocument
{
protected: // �������л�����
	CZipFileViewerDoc();
	DECLARE_DYNCREATE(CZipFileViewerDoc)

// ����
public:

// ����
public:

// ��д
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// ʵ��
public:
	virtual ~CZipFileViewerDoc();
protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
};


