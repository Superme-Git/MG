// DownloadThread.cpp : implementation file
//

#include "stdafx.h"
#include "DownloadThread.h"
#include "DownloadFileList.h"
#include "GlobalNotification.h"
#include <sstream>
#include <string>
#include <vector>
#include "InetIO.h"
#include "GlobalNotification.h"

// CDownloadThread

IMPLEMENT_DYNCREATE(CDownloadThread, CWinThread)

CDownloadThread::CDownloadThread()
{
}

CDownloadThread::~CDownloadThread()
{
}

BOOL CDownloadThread::InitInstance()
{
	return TRUE;
}

int CDownloadThread::ExitInstance()
{
	return CWinThread::ExitInstance();
}

BEGIN_MESSAGE_MAP(CDownloadThread, CWinThread)
END_MESSAGE_MAP()

bool DownloadToBlob( LPCWSTR lpszFileName, std::vector<unsigned char>& blob )
{
	PFS::CInetIO inetFile;
	if( !inetFile.Open( lpszFileName) )
	{
		return false;
	}

	DWORD dwSize = inetFile.GetSize();
	blob.resize(dwSize);
	DWORD dwRead = 0;
	DWORD dwReadTotal = 0;
	do 
	{
		dwReadTotal += dwRead = inetFile.Read( &blob[dwReadTotal], dwSize-dwReadTotal );

	} while (  dwRead > 0 && dwReadTotal < dwSize );

	return dwReadTotal == dwSize;
}

// running and idle processing
int CDownloadThread::Run()
{
	std::wstring downloadinfo;
	while( true )
	{
		// list中如果没有数据则会阻塞
		
		DOWNLOAD::ItemData itemData;
		std::wstring& filename = itemData.second;

		if( !DOWNLOAD::GetInstance<DOWNLOAD::ItemData>().Pop( itemData ))
		{
			break;
		}

		if( filename.empty())
		{
			break;
		}

		Data blob;
		blob.iItemId = itemData.first;
		blob.strItemName = itemData.second; // must copy it
		GlobalNotifyDataFetch( blob );

		try
		{
			if( !DownloadToBlob( filename.c_str(), blob.vData ) )
			{
				downloadinfo = filename + L" 下载失败！";
				GlobalNotify( ::GetLastError(), downloadinfo.c_str() );

				blob.iItemId = itemData.first;
				blob.strItemName = itemData.second; // must copy it
				GlobalNotifyDataLost( blob );

				continue;
			}
		}
		catch (PFS::CInetIOException& ex )
		{
			if( ex.GetDetail()[0] != NULL )
			{
				GlobalNotify( ex.GetErrorCode(), L"%1 下载失败!\r\n%2", filename.c_str(), ex.GetDetail() );
			}
			else
			{
				GlobalNotify( ex.GetErrorCode(), L"%1 下载失败!", filename.c_str() );
			}

			blob.iItemId = itemData.first;
			blob.strItemName = itemData.second; // must copy it
			GlobalNotifyDataLost( blob );

			continue;
		}

		blob.iItemId = itemData.first;
		blob.strItemName = itemData.second; // must copy it
		GlobalNotifyDataReady( blob );
		
		GlobalNotify( L"%1 下载完毕！", filename.c_str() );
	}

	return 0;
}