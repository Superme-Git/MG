// WinPFSDoc.h : CWinPFSDoc ��Ľӿ�
//
#pragma once

#include "../../src/common.h"
#include "../../src/zip/zipfile.h"
#include "../../src/runzip/runzipfile.h"


class COperatorThread;
class CFileCentralCache;
class CWinPFSDoc : public CDocument
{
protected: // �������л�����
	CWinPFSDoc();
	DECLARE_DYNCREATE(CWinPFSDoc)

	PFSX::CNativeFile m_nativefile;

// ����
public:
	std::wstring			m_lastSelPath;
	std::wstring			m_parentPath;
	COperatorThread*		m_thread;

	// ��item�ַ����õ����һ��Ŀ¼���ļ���
	static LPTSTR GetLastFileName( LPCTSTR item);

// ����
public:
	void AddDropFilesInPath( const std::wstring& path, CFileCentralCache& fcc);
	void DoDragDrop( const CFileCentralCache& fcc );

// ��д
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

	// �õ�parentĿ¼����Ŀ¼�����ļ��б�
	// parentΪ�մ���ʾΪ��Ŀ¼
	// bFolder �Ƿ�ΪĿ¼
	bool GetFolderList( LPCTSTR parent, bool bFolder, CFileCentralCache& childs);

	// �õ�parentĿ¼�µ���Ŀ¼�б�
	// parentΪ�մ���ʾΪ��Ŀ¼
	// ��Ŀ¼·���ǴӸ���Ŀ¼�Ķ༶·����
	bool GetFolderList( LPCTSTR parent, std::set<std::wstring>& childs);

	// �ж�parentĿ¼���Ƿ������Ŀ¼
	// parentΪ�մ���ʾΪ��Ŀ¼
	bool HasChildren( LPCTSTR parent );

// ʵ��
public:
	virtual ~CWinPFSDoc();

	ZIPFILE::CFileEditableReader m_filereader;
	size_t		m_filelen;

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	afx_msg void OnAnalyzepfs();
	afx_msg void OnCheckpfs();
	afx_msg void OnMakeHashTable();
};

