// DownloadThread.cpp : implementation file
//

#include "stdafx.h"
#include "CheckClient.h"
#include "DownloadThread.h"
#include "DownloadFileList.h"
#include "../Patcher/GlobalNotification.h"
#include "CheckThread.h"
#include "../Patcher/TextManager.h"
#include "../Patcher/TextID.h"

// CDownloadThread

IMPLEMENT_DYNCREATE(CDownloadThread, CWinThread)

CDownloadThread::CDownloadThread()
: m_downloaderr( 0)
{
}

CDownloadThread::~CDownloadThread()
{
}

BOOL CDownloadThread::InitInstance()
{
	// TODO:  perform and per-thread initialization here
	return TRUE;
}

int CDownloadThread::ExitInstance()
{
	// TODO:  perform any per-thread cleanup here
	return CWinThread::ExitInstance();
}

BEGIN_MESSAGE_MAP(CDownloadThread, CWinThread)
END_MESSAGE_MAP()


// running and idle processing
int CDownloadThread::Run()
{
	while( true)
	{
		// list�����û�������������
		CDownloadFileList::CMetaInfoPair mipair;
		if( !CDownloadFileList::GetInstance().Pop( mipair))
		{
			break;
		}
		const std::wstring& filename = mipair.first;
		if( filename.empty())
		{
			break;
		}
		
		if( CCheckThread::s_checkend)
		{
			// ˢ�����ؽ�����
			GlobalNotifyStep( filename);
		}
		//downloadinfo = DOWNLOADING + filename;
		//GlobalNotify( downloadinfo.c_str());

		std::wstring strDstPath = RES_ROOT + filename;
		std::wstring strSrcPath = BRF_ROOT + filename;
		// ���metainfo�Ա�brief��OpenFile�ɹ�
		PFS::CEnv::AddFileMetaInfo( strSrcPath, mipair.second);

		// ����ʧ������3��
		int retryTimes = 0;
		while( retryTimes++ < 3)
		{
			if( PFS::CEnv::FileCopy( strSrcPath, strDstPath, false))
			{
				break;
			}
		}
		if( retryTimes < 3)
		{
			GlobalNotify( GlobalFormatText( TIP_DOWNLOAD_FILE_COMPLETE, filename.c_str()).c_str() );
		}
		else
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_COPY_FILE, strSrcPath.c_str(), RES_ROOT) );
			m_downloaderr++;

			GlobalNotify( GlobalFormatText( ERR_DOWNLOAD_FILE2, filename.c_str()).c_str() );
		}
	}

	GlobalNotify( GlobalGetText( TIP_ALL_FILES_DOWNLOAD_COMPLETE).c_str() );
	return 0;
}