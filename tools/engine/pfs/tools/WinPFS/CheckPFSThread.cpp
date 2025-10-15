// CheckPFSThread.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "CheckPFSThread.h"
#include "../../pfsutility.h"
#include "ThreadStateList.h"
// CCheckPFSThread

IMPLEMENT_DYNCREATE(CCheckPFSThread, CWinThread)

CCheckPFSThread::CCheckPFSThread()
: m_doc( NULL)
, m_wnd( NULL)
, m_stop( false)
{
}

CCheckPFSThread::~CCheckPFSThread()
{
}

BOOL CCheckPFSThread::InitInstance()
{
	// TODO:  perform and per-thread initialization here
	return TRUE;
}

int CCheckPFSThread::ExitInstance()
{
	// TODO:  perform any per-thread cleanup here
	return CWinThread::ExitInstance();
}

BEGIN_MESSAGE_MAP(CCheckPFSThread, CWinThread)
END_MESSAGE_MAP()


// CCheckPFSThread message handlers

int CCheckPFSThread::Run()
{
	if( NULL == m_doc)
	{
		return 0;
	}

	const ZIPFILE::CFileCentral* fmFC = m_doc->m_filereader.GetFileCentral( L".files.meta");
	if( fmFC)
	{
		ZIPFILE::CFileCentralRead fcr( m_doc->m_nativefile, *fmFC);
		PFS::CImageData imageData = fcr.GetImage();
		PFS::CImageFile imageFile(imageData);
		PFS::CMetaFileRead::DoRead( imageFile, m_mim);
	}

	int count = 0;

	CThreadStateList::Item cr;
	ZIPFILE::CFileReader::CFileCentralMap& fcm = m_doc->m_filereader.GetFileCentralMap();
	for( ZIPFILE::CFileReader::CFileCentralMap::iterator i=fcm.begin(), e=fcm.end(); i!=e; ++i)
	{
		if( m_stop)
		{
			break;
		}

		const ZIPFILE::CFileCentral& fc = i->second;
		if( fc.IsDirectory())
		{
			continue;
		}

		cr.m_name = i->first;
		int crcResult, md5Result;
		CheckOneFile( fc, crcResult, md5Result );
		cr.m_state = MAKELONG( crcResult, md5Result);

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

	if( m_wnd && !m_stop)
	{
		m_wnd->PostMessage( WM_THREADSTATE, 0, END);
	}
	return 0;
}

void CCheckPFSThread::CheckOneFile( const ZIPFILE::CFileCentral& fc, int& crcResult, int& md5Result)
{
	crcResult = EQUAL;
	md5Result = EQUAL;

	std::wstring temp;
	temp = L"/" + fc.m_name;

	PFSX::TSID encoder = 0;
	PFS::FILE_DATA_HASH md5Src = 0xFFFFFFFFFFFFFFFF;
	PFS::CMetaInfoMap::iterator iFind = m_mim.find( temp);
	if( iFind != m_mim.end())
	{
		md5Src = iFind->second.GetFileHash();
		encoder = PFSX::GetEncoderTypeFromFilePfsxAttributes( iFind->second.GetFilePfsxAttributes());
	}
	else
	{
		md5Result = NOFOUND;
	}

	ZIPFILE::helper::CCRC32 crc32;
	PFS::helper::CMD5Hash	md5;

	ZIPFILE::CFileCentralRead fcr( m_doc->m_nativefile, fc);
	
	PFS::CImageData all = fcr.GetImage();

	crc32.Update( all.GetData(), (size_t)all.GetSize() );
	if( crc32.GetValue() != fc.m_uiCRC32)
	{
		crcResult = NOEQUAL;
	}

	if( NOFOUND != md5Result)
	{
		md5.Update( all.GetData(), (size_t)all.GetSize() );
	
		PFS::CImageFile image( all);
		PFS::FILE_DATA_HASH digest = 0;
		PFS::helper::CMD5FileHashMethod method;
		method.MakeFileHash( image, digest);

		if( digest != md5Src)
		{
			md5Result = NOEQUAL;
			
			const PFSX::TSID ZpdID = PFSX::MakeTypeIDByName( 'Z', 'P', 'D');
			const PFSX::TSID ZipID = PFSX::MakeTypeIDByName( 'Z', 'I', 'P');
			if( ZpdID == encoder || ZipID == encoder)
			{
				// 如果encoder为压缩过，将其解压再求一次md5
				image.Seek( 0, PFS::FSM_SET);
				PFSX::CZipReadFile zrf( image, 0);
				method.MakeFileHash( zrf, digest);

				if( digest == md5Src)
				{
					md5Result = EQUAL;
				}
			}
		}
	}
	
}