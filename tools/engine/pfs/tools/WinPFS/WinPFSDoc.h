// WinPFSDoc.h : CWinPFSDoc 类的接口
//
#pragma once

#include "../../src/common.h"
#include "../../src/zip/zipfile.h"
#include "../../src/runzip/runzipfile.h"


class COperatorThread;
class CFileCentralCache;
class CWinPFSDoc : public CDocument
{
protected: // 仅从序列化创建
	CWinPFSDoc();
	DECLARE_DYNCREATE(CWinPFSDoc)

	PFSX::CNativeFile m_nativefile;

// 属性
public:
	std::wstring			m_lastSelPath;
	std::wstring			m_parentPath;
	COperatorThread*		m_thread;

	// 从item字符串得到最后一级目录或文件名
	static LPTSTR GetLastFileName( LPCTSTR item);

// 操作
public:
	void AddDropFilesInPath( const std::wstring& path, CFileCentralCache& fcc);
	void DoDragDrop( const CFileCentralCache& fcc );

// 重写
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

	// 得到parent目录下子目录或子文件列表
	// parent为空串表示为根目录
	// bFolder 是否为目录
	bool GetFolderList( LPCTSTR parent, bool bFolder, CFileCentralCache& childs);

	// 得到parent目录下的子目录列表
	// parent为空串表示为根目录
	// 子目录路径是从根到目录的多级路径名
	bool GetFolderList( LPCTSTR parent, std::set<std::wstring>& childs);

	// 判断parent目录下是否存在子目录
	// parent为空串表示为根目录
	bool HasChildren( LPCTSTR parent );

// 实现
public:
	virtual ~CWinPFSDoc();

	ZIPFILE::CFileEditableReader m_filereader;
	size_t		m_filelen;

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	afx_msg void OnAnalyzepfs();
	afx_msg void OnCheckpfs();
	afx_msg void OnMakeHashTable();
};

