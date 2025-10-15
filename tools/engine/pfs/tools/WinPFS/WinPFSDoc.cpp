// WinPFSDoc.cpp : CWinPFSDoc ���ʵ��
//

#include "stdafx.h"
#include "WinPFS.h"
#include "WinPFSDoc.h"
#include "FileCentralCache.h"
#include "AnalyzePFSDlg.h"
#include "CheckPFSDlg.h"
#include "MakeHashTableDlg.h"
#include "OperatorThread.h"
#include "DropDataSource.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CWinPFSDoc

IMPLEMENT_DYNCREATE(CWinPFSDoc, CDocument)

BEGIN_MESSAGE_MAP(CWinPFSDoc, CDocument)
	ON_COMMAND(ID_ANALYZEPFS, &OnAnalyzepfs)
	ON_COMMAND(ID_CHECKPFS, &CWinPFSDoc::OnCheckpfs)
	ON_COMMAND(ID_MAKEHASHTABLE, &CWinPFSDoc::OnMakeHashTable)
END_MESSAGE_MAP()


// CWinPFSDoc ����/����

CWinPFSDoc::CWinPFSDoc()
{
	m_thread = (COperatorThread*)AfxBeginThread( RUNTIME_CLASS(COperatorThread),
		THREAD_PRIORITY_NORMAL, 0, CREATE_SUSPENDED, NULL);
	if( m_thread)
	{
		m_thread->SetDocument( this );
		m_thread->ResumeThread();
	}
	else
	{
		AfxMessageBox( L"CDocument ���� operator �߳�ʧ�ܣ�");
	}
}

CWinPFSDoc::~CWinPFSDoc()
{
	if( m_thread)
	{
		HANDLE hThread = m_thread->m_hThread;
		if( WAIT_TIMEOUT == WaitForSingleObject( hThread, 0))
		{
			COperatorList::Item item;
			item.m_type = COperatorList::EXIT;
			// ��COperatorThread�߳��˳�
			COperatorList::GetInstance().PushFront( item);
			WaitForSingleObject( hThread, INFINITE);
		}
	}
}

BOOL CWinPFSDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: �ڴ�������³�ʼ������
	// (SDI �ĵ������ø��ĵ�)
	m_filereader.GetFileCentralMap().clear();

	return TRUE;
}

// CWinPFSDoc ���л�

void CWinPFSDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: �ڴ���Ӵ洢����
	}
	else
	{
		// TODO: �ڴ���Ӽ��ش���
	}
}

// CWinPFSDoc ����

BOOL CWinPFSDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	if (!CDocument::OnOpenDocument(lpszPathName))
		return FALSE;

	m_filereader.GetFileCentralMap().clear();

	m_nativefile.Close();

	if( !m_nativefile.Open( lpszPathName, PFS::FM_EXCL, PFS::FA_RDONLY))
		return FALSE;

	try
	{
		m_filereader.SetFileIO( m_nativefile);
		m_filelen = (size_t)m_nativefile.GetSize();
	}
	catch (ZIPFILE::CZipFileBadFormat&)
	{
		AfxMessageBox( L"������Ч��pfs�ļ���");
		return FALSE;
	}

	return TRUE;
}

BOOL CWinPFSDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	// TODO: Add your specialized code here and/or call the base class
	return TRUE;
}

// �ж�parentĿ¼���Ƿ������Ŀ¼
bool CWinPFSDoc::HasChildren( LPCTSTR parent )
{
	assert( parent);

	ZIPFILE::CFileReader::CFileCentralMap& fcm = m_filereader.GetFileCentralMap();

	if( parent[0] == 0 && fcm.size() != 0 )
	{
		return true;
	}

	ZIPFILE::CFileReader::CFileCentralMap::const_iterator i = fcm.begin();
	if( parent[0] )
	{
		i = fcm.lower_bound( parent);
		if( i != fcm.end() && (*i).first == parent )
		{
			i++;	// ����ҵ�����������
		}
	}

	size_t len = _tcslen( parent);
	for( ZIPFILE::CFileReader::CFileCentralMap::const_iterator e=fcm.end(); i!=e; i++)
	{
		const ZIPFILE::CFileCentral& fc = i->second;
		const std::wstring& folder = i->first;

		size_t n = 0;
		if( parent[0])
		{
			n = len;
			if( folder.size() <= n || (_tcsncmp( folder.c_str(), parent, n) != 0))
			{
				// ��Ϊ fcm �ǰ����ַ�������ģ�������ֲ����� parent ��ͷ���ļ������˳�ѭ��
				break;
			}
		}

		std::wstring::size_type pos = folder.find( L'/', n);
		if( pos != std::wstring::npos )
		{
			return true;
		}
	}

	return false;
}

// �õ�parentĿ¼����Ŀ¼�б�
// parentΪ�մ���ʾΪ��Ŀ¼
bool CWinPFSDoc::GetFolderList( LPCTSTR parent, std::set<std::wstring>& childs)
{
	assert( parent);

	ZIPFILE::CFileReader::CFileCentralMap& fcm = m_filereader.GetFileCentralMap();

	ZIPFILE::CFileReader::CFileCentralMap::const_iterator i = fcm.begin();
	if( parent[0])
	{
		i = fcm.lower_bound( parent);
		if( i != fcm.end() && (*i).first == parent )
		{
			i++;	// ����ҵ�����������
		}
	}

	size_t len = _tcslen( parent);
	for( ZIPFILE::CFileReader::CFileCentralMap::const_iterator e=fcm.end(); i!=e; i++)
	{
		const ZIPFILE::CFileCentral& fc = i->second;
		const std::wstring& folder = i->first;

		size_t n = 0;
		if( parent[0])
		{
			n = len;
			if( folder.size() <= n || (_tcsncmp( folder.c_str(), parent, n) != 0))
			{
				// ��Ϊ fcm �ǰ����ַ�������ģ�������ֲ����� parent ��ͷ���ļ������˳�ѭ��
				break;
			}
		}
		std::wstring::size_type pos = folder.find( L'/', n);
		if( pos != std::wstring::npos )
		{
			childs.insert( folder.substr( 0,  pos + 1 ) );
		}
	}

	return true;
}

// �õ�parentĿ¼����Ŀ¼�б�
// parentΪ�մ���ʾΪ��Ŀ¼
bool CWinPFSDoc::GetFolderList( LPCTSTR parent, bool bFolder, CFileCentralCache& childs)
{
	assert( parent);

	ZIPFILE::CFileReader::CFileCentralMap& fcm = m_filereader.GetFileCentralMap();

	ZIPFILE::CFileReader::CFileCentralMap::const_iterator i = fcm.begin();
	if( parent[0])
	{
		i = fcm.lower_bound( parent);
		if( i != fcm.end() && (*i).first == parent )
		{
			i++;	// ����ҵ�����������
		}
	}

	size_t len = _tcslen( parent);
	for( ZIPFILE::CFileReader::CFileCentralMap::const_iterator e=fcm.end(); i!=e; i++)
	{
		const ZIPFILE::CFileCentral& fc = i->second;
		const std::wstring& folder = i->first;

		size_t n = 0;
		if( parent[0])
		{
			n = len;
			if( folder.size() <= n || (_tcsncmp( folder.c_str(), parent, n) != 0))
			{
				// ��Ϊ fcm �ǰ����ַ�������ģ�������ֲ����� parent ��ͷ���ļ������˳�ѭ��
				break;
			}
		}
		std::wstring::size_type pos = folder.find( L'/', n);
		if( pos == folder.size()-1 )
		{
			childs.push_back( (DWORD_PTR)&fc);
		}
		else if( pos == std::wstring::npos)
		{
			if( !bFolder)
			{
				childs.push_back( (DWORD_PTR)&fc);
			}
		}
	}
	return true;
}

// ��item�ַ����õ����һ��Ŀ¼���ļ���
LPTSTR CWinPFSDoc::GetLastFileName( LPCTSTR item)
{
	static wchar_t name[MAX_PATH] = {0};
	name[0] = 0;

	size_t len = _tcslen( item);
	if( len > 0)
	{
		LPCTSTR start = item;
		LPCTSTR end = start + (len-1);
		if( end[0] == L'/')
		{
			end--;
		}
		start = end;
		while( start != item)
		{
			if( start[0] == L'/')
			{
				start++;
				break;
			}
			start--;
		}
		_tcsncpy_s( name, start, end-start+1);
	}
	return name;
}

void CWinPFSDoc::OnAnalyzepfs()
{
	// TODO: Add your command handler code here
	CAnalyzePFSDlg dlg;

	AfxGetApp()->BeginWaitCursor();
	dlg.SetDocument( this);
	AfxGetApp()->EndWaitCursor();

	dlg.DoModal();
}

void CWinPFSDoc::OnCheckpfs()
{
	// TODO: Add your command handler code here
	CCheckPFSDlg dlg;
	dlg.SetDocument( this);
	dlg.DoModal();
}

void CWinPFSDoc::OnMakeHashTable()
{
	CMakeHashTableDlg dlg;
	dlg.SetDocument( this);
	dlg.DoModal();
};


// �ݹ�õ�pathĿ¼��������Ŀ¼�����ļ����ϱ��浽fcc��
void CWinPFSDoc::AddDropFilesInPath( const std::wstring& path, CFileCentralCache& fcc)
{
	CFileCentralCache fccPath;
	GetFolderList( path.c_str(), false, fccPath);
	fcc.insert( fcc.end(), fccPath.begin(), fccPath.end());
	
	for( CFileCentralCache::iterator i=fccPath.begin(), e=fccPath.end(); i!=e; ++i)
	{
		ZIPFILE::CFileCentral* fc = (ZIPFILE::CFileCentral*)(*i);
		if( fc->IsDirectory())
		{
			AddDropFilesInPath( fc->m_name, fcc);
		}
	}
}

void CWinPFSDoc::DoDragDrop( const CFileCentralCache& fcc )
{
	CDropDataSource	dds;
	dds.SetDocument( this );

	UINT uFileCount = fcc.size();
	UINT uBuffSize = sizeof( FILEGROUPDESCRIPTOR) + (uFileCount-1) * sizeof(FILEDESCRIPTOR);
	HGLOBAL hFileDescriptor = GlobalAlloc ( GHND | GMEM_SHARE, uBuffSize );
	if(hFileDescriptor)
	{
		FILEGROUPDESCRIPTOR* pGroupDescriptor = (FILEGROUPDESCRIPTOR*)GlobalLock( hFileDescriptor );
		if(pGroupDescriptor)
		{
			FILEDESCRIPTOR* pFileDescriptorArray = 
				(FILEDESCRIPTOR*)((LPBYTE)pGroupDescriptor + sizeof(UINT));
			pGroupDescriptor->cItems = uFileCount;
			
			int index = 0;
			std::wstring temp;
			for( CFileCentralCache::const_iterator i=fcc.begin(), e=fcc.end(); i!=e; ++i)
			{
				ZIPFILE::CFileCentral* fc = (ZIPFILE::CFileCentral*)(*i);
				const std::wstring& file = fc->m_name;
				
				ZeroMemory( &pFileDescriptorArray[index], sizeof(FILEDESCRIPTOR));
				lstrcpy ( pFileDescriptorArray[index].cFileName, file.c_str());

				dds.GetFile().push_back( file);

				pFileDescriptorArray[index].dwFlags = FD_ATTRIBUTES|FD_FILESIZE|FD_CREATETIME|FD_PROGRESSUI;
				pFileDescriptorArray[index].nFileSizeLow = fc->GetOrginalSize();
				pFileDescriptorArray[index].nFileSizeHigh = 0;
				CoDosDateTimeToFileTime( fc->m_usModifyDate, fc->m_usModifyTime, 
					&pFileDescriptorArray[index].ftCreationTime);
				
				if( fc->IsDirectory())
				{
					pFileDescriptorArray[index].dwFileAttributes = FILE_ATTRIBUTE_DIRECTORY;
				}
				else
				{
					pFileDescriptorArray[index].dwFileAttributes = FILE_ATTRIBUTE_NORMAL;
				}
				index++;
			}
		}
		else
		{
			GlobalFree ( hFileDescriptor );
		}
	}
	GlobalUnlock ( hFileDescriptor );	

	FORMATETC etcDescriptor = { RegisterClipboardFormat(CFSTR_FILEDESCRIPTOR), 
		NULL, DVASPECT_CONTENT, -1, TYMED_HGLOBAL };
	dds.CacheGlobalData ( RegisterClipboardFormat(CFSTR_FILEDESCRIPTOR), 
		hFileDescriptor, &etcDescriptor );

	FORMATETC etcContents = { RegisterClipboardFormat(CFSTR_FILECONTENTS), 
			NULL, DVASPECT_CONTENT, -1, TYMED_HGLOBAL };
	dds.DelayRenderFileData( RegisterClipboardFormat(CFSTR_FILECONTENTS), &etcContents);

	DROPEFFECT dwEffect = dds.DoDragDrop ( DROPEFFECT_COPY | DROPEFFECT_MOVE );

	if(dwEffect == DROPEFFECT_NONE )
	{
		GlobalFree( hFileDescriptor );
	}
}