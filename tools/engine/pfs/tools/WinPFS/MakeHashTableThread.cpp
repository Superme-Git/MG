// MakeHashTableThread.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "MakeHashTableThread.h"
#include "../../pfs2.h"
#include "ThreadStateList.h"
#include "../../../xmlio/xmlio.h"
#include "../../stringhelper.h"
// CMakeHashTableThread

IMPLEMENT_DYNCREATE(CMakeHashTableThread, CWinThread)

CMakeHashTableThread::CMakeHashTableThread()
: m_stop( false)
{
}

CMakeHashTableThread::~CMakeHashTableThread()
{
}

BOOL CMakeHashTableThread::InitInstance()
{
	// TODO:  perform and per-thread initialization here
	return TRUE;
}

int CMakeHashTableThread::ExitInstance()
{
	// TODO:  perform any per-thread cleanup here
	return CWinThread::ExitInstance();
}

BEGIN_MESSAGE_MAP(CMakeHashTableThread, CWinThread)
END_MESSAGE_MAP()


// CMakeHashTableThread message handlers
// running and idle processing
int CMakeHashTableThread::Run()
{
	if( NULL == m_doc)
	{
		return 0;
	}

	bool res = true;

	try
	{

		PFS::CMetaInfoMap mim;
		const ZIPFILE::CFileCentral* fmFC = m_doc->m_filereader.GetFileCentral( L".files.meta");
		if( NULL == fmFC)
		{
			throw 0;
		}

		// 读 .files.meta 文件
		ZIPFILE::CFileCentralRead fcr( m_doc->m_nativefile, *fmFC);
		PFS::CImageData imageData = fcr.GetImage();
		PFS::CImageFile imageFile(imageData);
		if( !PFS::CMetaFileRead::DoRead( imageFile, mim))
		{
			throw 0;
		}

		mim.erase( L"/.files.meta");
		mim.erase( L"/.setup.meta");
		mim.erase( L"/.version.meta");

		// 写xml
		XMLIO::CFileWriter writer;
		XMLIO::CONode rootDes;
		if( !writer.GetRootNode( rootDes))
		{
			throw 0;
		}

		int count = 0;

		wchar_t buffer[1024] = {0};
		CThreadStateList::Item cr;
		for( PFS::CMetaInfoMap::iterator i=mim.begin(), e=mim.end(); i!=e; ++i)
		{
			if( m_stop)
			{
				break;
			}

			XMLIO::CONode child;
			if( !rootDes.CreateSubElement( L"child", child))
			{
				throw 0;
			}

			const PFS::CMetaInfo& mi = i->second;
			cr.m_name = m_mountName + i->first;

			if( m_outputFileName)
			{
				child.SetAttribute( L"name", cr.m_name.c_str());
			}
			PFS::FILE_DATA_HASH nameHash = PFS::CEnv::MakeStringHash( cr.m_name);
			wsprintf( buffer, L"%I64d", nameHash);
			child.SetAttribute( L"nameHash", buffer);
			wsprintf( buffer, L"%I64d", mi.GetFileHash());
			child.SetAttribute( L"dataHash", buffer);

			CThreadStateList::GetInstance().Push( cr);
			if( m_wnd )
			{
				m_wnd->PostMessage( WM_THREADSTATE);

				if( ++count > 50)
				{
					count = 0;
					Sleep( 1);	// 让 m_wnd 有机会处理消息
				}
			}
		}

		if( XMLIO::EC_SUCCESS != writer.SaveNative( m_saveFileName))
		{
			throw 0;
		}
	}
	catch( ...)
	{
		res = false;
	}

	if( m_wnd && !m_stop)
	{
		m_wnd->PostMessage( WM_THREADSTATE, res, END);
	}
	return 0;
}