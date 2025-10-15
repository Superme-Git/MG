// OperatorThread.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "OperatorThread.h"
#include "ThreadStateList.h"
#include "FileCentralCache.h"
// COperatorThread

IMPLEMENT_DYNCREATE(COperatorThread, CWinThread)

enum { BUFFERSIZE = 40960 };

COperatorThread::COperatorThread()
: m_doc( NULL)
, m_pWnd( NULL)
{
	m_buffer.Resize( BUFFERSIZE);
}

COperatorThread::~COperatorThread()
{
}

BOOL COperatorThread::InitInstance()
{
	return TRUE;
}

int COperatorThread::ExitInstance()
{
	return CWinThread::ExitInstance();
}

BEGIN_MESSAGE_MAP(COperatorThread, CWinThread)
END_MESSAGE_MAP()


// running and idle processing
int COperatorThread::Run()
{
	while( true)
	{
		COperatorList::Item item;
		if( COperatorList::GetInstance().Pop( item))
		{
			switch( item.m_type)
			{
			case COperatorList::EXIT:
				return 0;
			case COperatorList::INFLATE:
			case COperatorList::INFLATEEND:
				InflateOneFile( item);
				OnEndInflateOneFile( item);
				break;
			}
		}
	}
	return 0;
}

// 解压文件
// src为pfs文件中文件路径
// des为windows文件路径
bool COperatorThread::InflateOneFile( const COperatorList::Item& item)
{
	const std::wstring& src = item.m_src;
	const std::wstring& des = item.m_des;

	if( src.empty() || des.empty())
	{
		return false;
	}

	if( item.m_isPath)
	{
		::CreateDirectory( des.c_str() , NULL);
		return true;
	}

	if( m_pWnd && ::IsWindow( m_pWnd->GetSafeHwnd()))
	{
		CThreadStateList::Item item;
		item.m_name = L"正在解压文件：";
		item.m_name += src;
		CThreadStateList::GetInstance().Push( item);
		m_pWnd->PostMessage( WM_THREADSTATE);
	}

	ZIPFILE::CFileEditableReader& fer = m_doc->m_filereader;
	const ZIPFILE::CFileCentral* fc = fer.GetFileCentral( src);
	if( NULL == fc)
	{
		return false;
	}

	PFSX::CNativeFile nf;
	if( !nf.Open( des, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
	{
		return false;
	}

	if( m_pWnd && ::IsWindow( m_pWnd->GetSafeHwnd()))
	{
		m_pWnd->PostMessage( WM_THREADPROGRESS, fc->GetOrginalSize(), INIT);
	}

	ZIPFILE::CFileCentralRead fcr( fer.GetFileIO(), *fc);
	while( size_t read = fcr.Read( m_buffer, BUFFERSIZE))
	{
		if( PFS::FILE_SIZE_ERROR == read)
		{
			return false;
		}
		m_buffer.Resize( read);

		if( PFS::FILE_SIZE_ERROR == nf.Write( m_buffer.GetData(), m_buffer.GetSize()))
		{
			return false;
		}

		if( m_pWnd && ::IsWindow( m_pWnd->GetSafeHwnd()))
		{
			m_pWnd->PostMessage( WM_THREADPROGRESS, read, PROGRESS);
		}
	}
	return true;
}

// 解压文件完成后调用
void COperatorThread::OnEndInflateOneFile( const COperatorList::Item& item)
{
	switch( item.m_type)
	{
	case COperatorList::INFLATE:
		InflateChildPath( item);
		break;
	case COperatorList::INFLATEEND:
		if( !InflateChildPath( item))
		{
			if( m_pWnd && ::IsWindow( m_pWnd->GetSafeHwnd()))
			{
				m_pWnd->PostMessage( WM_THREADSTATE, 0, END);
			}
		}
		break;
	}
}

// 解压子目录
bool COperatorThread::InflateChildPath( const COperatorList::Item& item)
{
	if( item.m_des.empty())
	{
		return false;
	}

	if( !item.m_isPath)
	{
		return false;
	}

	CFileCentralCache fcc;
	m_doc->GetFolderList( item.m_src.c_str(), false, fcc);
	if( fcc.empty())
	{
		return false;
	}

	size_t parentPathLen = item.m_src.size();
	for( CFileCentralCache::iterator i=fcc.begin(), e=fcc.end(); i!=e; )
	{
		ZIPFILE::CFileCentral* fc = (ZIPFILE::CFileCentral*)*i;
		COperatorList::Item child;
		child.m_src = fc->m_name;
		child.m_isPath = fc->IsDirectory();
		child.m_des = item.m_des;
		child.m_des += ( fc->m_name.c_str() + parentPathLen);
		++i;
		if( COperatorList::INFLATEEND == item.m_type)
		{
			child.m_type = ( i!=e ? COperatorList::INFLATE : COperatorList::INFLATEEND);
			COperatorList::GetInstance().Push( child);
		}
		else
		{
			child.m_type = COperatorList::INFLATE;
			COperatorList::GetInstance().PushFront( child);
		}
		
	}
	return true;
}