// pfsVerifier.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "../..\pfs2.h"
#include "..\..\src\common.h"
#include "..\..\src\brief\brief.h"
#include "..\..\src\utility\md5hash.h"
#include "..\..\src\native\rawfileio.h"
#include <locale.h>
#include <wininet.h>

#include <conio.h>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <ctime>
#include <fstream>
using namespace std;
using namespace PFS;
using namespace PFSX;


LPCWSTR ips[] = 
{
	L"patch2.mhzx.locojoy.com",
	L"patch3.mhzx.locojoy.com",
	L"222.242.104.198",
	L"222.242.104.195",
	L"222.242.104.197",
	L"61.164.121.167",
	L"61.164.121.168",
	L"61.164.121.162",
	L"61.164.121.163",
	L"124.115.19.14",
	L"218.60.11.51",
	L"218.60.11.54",
	L"218.60.11.55",
	L"218.60.11.56",
	L"221.192.143.70",
};

// 读指定目录下的MetaInfo文件
bool ReadMetaInfoFile( std::wstring dir, PFS::CMetaInfoMap& mimFile )
{
	bool bOK = false;

	// 直接使用NativeFile写MetaInfo文件
	PFSX::CNativeFile theFile;
	if( theFile.Open( dir + PFS::CFileMetaInfo::GetMetaFileName(),	PFS::FM_EXCL, PFS::FA_RDONLY ) )
	{
		bOK = PFS::CMetaFileRead::DoRead( theFile, mimFile );
	}

	if( bOK )
	{
		return true;
	}
	else 
	{
		std::wostream& oss = std::wcout;
		oss<<L"错误: 读文件失败 - "<< PFS::CFileMetaInfo::GetMetaFileName();
		//upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );

		return false;
	}
}

// 将MetaInfo文件写到指定目录下
bool WriteMetaInfoFile( std::wstring dir, const PFS::CMetaInfoMap& mimFile )
{
	bool bOK = false;

	// 直接使用NativeFile写MetaInfo文件
	PFSX::CNativeFile theFile;
	if( theFile.Open( dir + PFS::CFileMetaInfo::GetMetaFileName(),
		PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY ) )
	{
		bOK = PFS::CMetaFileWrite::DoWrite( theFile, mimFile );
	}

	if( bOK )
	{
		return true;
	}
	else 
	{
		std::wostream& oss = std::wcout;
		oss<<L"错误: 写文件失败 - "<< PFS::CFileMetaInfo::GetMetaFileName();
		//upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );
		return false;
	}
}

//
// 从url指定的位置下载metainfo文件，并读取内容到rMetaInfoMap对象中
//
bool DownloadMetaInfoFile( std::wstring url, PFS::CMetaInfoMap& rMetaInfoMap )
{
	std::wstring metafile = url + PFS::CFileMetaInfo::GetMetaFileName();
	PFSX::CInetFile inetFile;
	if( !inetFile.Open( metafile ) )
	{
		std::wostream& oss = std::wcout;
		oss<<L"错误: 下载文件失败 - "<< metafile;
		//upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );
		return false;
	}

	PFS::fsize_t szTotal = inetFile.GetSize( );
	if( szTotal == 0 )
	{
		std::wostream& oss = std::wcout;
		oss<<L"错误: 无效文件(空文件) - " << metafile;
		//upd::SetLastError( PFS::FILE_SIZE_ERROR, oss.str() );
		return false;
	}

	// 方案1 - 直接将网络文件作为输入源
	if ( PFS::CMetaFileRead::DoRead( inetFile, rMetaInfoMap ) )
	{
		return true;
	}

	// 尝试方案2 - 将网络文件下载到本地临时文件中
	PFS::CTempFile theFile;
	if( !theFile.Open() )
	{
		std::wostream& oss = std::wcout;
		oss<<L"错误: 无法生成临时文件用于保存" << metafile;
		//upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );

		return false;
	}
	else
	{
		// 重置文件指针
		inetFile.Seek( 0, PFS::FSM_SET );

		PFS::fsize_t szTotalRead = 0;
		size_t szRead = 0;
		BYTE buffer[ 4096 ];
		int nTriedTimes = 0;
		while( szTotalRead < szTotal )
		{
			szRead = inetFile.Read( buffer, 4096  );
			if ( szRead > 0 )
			{
				theFile.Write( buffer, szRead );
				szTotalRead += szRead;
			}
			else
			{
				if( ++ nTriedTimes > 5 )
				{
					Sleep( 1000 );
					break;
				}
			}
		}

		bool bOK = false;
		// 必须重置文件指针到文件头，否则下面的读操作将失败
		if( szTotalRead == szTotal )
		{
			theFile.Seek( 0, PFS::FSM_SET );
			bOK = PFS::CMetaFileRead::DoRead( theFile, rMetaInfoMap );
		}

		if ( bOK )
		{
			return true;
		}
		else
		{
			std::wostream& oss = std::wcout;
			oss<<L"错误: 读文件失败 - " << metafile;
			//upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );
			return false;
		}
	}
}
//
//{
//	L"124.129.16.128",
//		L"124.129.16.127",
//		L"123.234.7.247",
//		L"58.215.98.7",
//		L"58.215.99.7",
//		L"222.242.104.196",
//		L"119.167.253.5",
//		L"124.129.16.144",
//		// L"58.215.100.8",
//};

// LPCWSTR lpszFile = L"cfg/data/shop.xml";

// 保存网络文件到临时文件中(重试机制)
static bool _CopyInetFile( PFS::CBaseFile& source, PFS::CBaseFile& dest )
{
	PFS::CBaseFile*	fsrc = &source;
	PFS::CBaseFile*	fdst = &dest;
	const int	nBufferSize = 4096;
	unsigned char byteBuffer[nBufferSize];
	bool		bResult = true;
	size_t		s = 0;
	int			retryCount = 0;
	
	PFS::fsize_t total = fsrc->GetSize();

	for(;total > 0;)
	{
		while( s = fsrc->Read( byteBuffer, nBufferSize ) )
		{
			if( PFS::FILE_SIZE_ERROR == s )
				return false;
			s = fdst->Write( byteBuffer, s);
			if( PFS::FILE_SIZE_ERROR == s )
				return false;
			total -= s;
		}

		if( total != 0 )
		{
			retryCount ++;
			if( retryCount <= 10 )
				Sleep( 1000 );
			else
				break;
		}
	}

	return total == 0;
}

static LPCTSTR _GetAgentName()
{
	static TCHAR AgentName[MAX_PATH];
	if ( AgentName[0] == NULL )
	{
		GetModuleFileName( NULL, AgentName, MAX_PATH);
		PathStripPath( AgentName );//Leaves the file name only
	}
	return AgentName;
}

// bFastMode 快速模式，只测试服务器文件是否存在
// 针对 不压缩 和 xml 文件验证文件长度
void testNewServer( int count, _TCHAR* ips[], const _TCHAR* pszDestinationDir, bool bFastMode )
{
	std::wstring baseurl, url, result;
	wchar_t buffer[256] = {0};

	CInetFile fileZiped;
	CTempFile tempFile;
	if( !tempFile.Open() )
	{
		wcout << L"Create temp file failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;
		return;
	}

	const PFSX::TSID X20ID = PFSX::MakeTypeIDByName( 'X', '2', 'O');

	for ( int i = 0; i < count; i ++ )
	{
		std::wstring dir = pszDestinationDir;
		if ( dir.at(dir.size()-1) != L'\\' )
		{
			dir += L'\\';
		}

		dir += ips[i];

		CFileIO::CheckCreateDirectory( dir );

		std::wofstream oss;
		oss.open( (dir + L"\\test.log").c_str(), std::ios_base::out | std::ios_base::trunc |  std::ios_base::ate );
		if( !oss.is_open() )
		{
			wcout << L"open " <<  dir <<  L"\\test.log failed." << endl;
			continue;
		}

		try
		{
			oss.imbue(std::locale(".936"));
		}
		catch (std::runtime_error e)
		{
			oss << L"oss.imbue(std::locale(\".936\")) throws an runtime error - " <<  e.what() << endl;
			wcout << L"oss.imbue(std::locale(\".936\")) throws an runtime error - " <<  e.what() << endl;
		}

		wcout << ips[i] << std::endl;
		oss << ips[i] << std::endl;


		baseurl = L"http://";
		baseurl += ips[i];
		//baseurl += L"/mhzx";

		//wcout << L"downloading " << baseurl << std::endl;
		PFS::CMetaInfoMap fileList;
		if( !DownloadMetaInfoFile( baseurl + L"/mhzx", fileList ) )
		{
			wcout << baseurl << L" failed!" << endl;
			oss << baseurl << L" failed!" << endl;
			continue;
		}


		// 优先使用注册表配置
		HINTERNET hInternet = InternetOpen(_GetAgentName(), 
			INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
		if ( hInternet == NULL )
		{
			oss << L"InternetOpen " << baseurl << L" failed GLE: " << GetLastError() << std::endl;
			wcout << L"InternetOpen " << baseurl << L" failed GLE: " << GetLastError() << std::endl;
			continue;
		}

		HINTERNET hConnect = InternetConnect(
			hInternet,
			ips[i],
			INTERNET_DEFAULT_HTTP_PORT, // INTERNET_INVALID_PORT_NUMBER, // INTERNET_DEFAULT_HTTP_PORT,
			TEXT(""), // LPCTSTR lpszUsername,
			TEXT(""), // LPCTSTR lpszPassword,
			INTERNET_SERVICE_HTTP,
			0,
			0);

		if( hConnect == NULL)
		{
			oss << L"InternetConnect " << baseurl << L" failed GLE: " << GetLastError() << std::endl;
			wcout << L"InternetConnect " << baseurl << L" failed GLE: " << GetLastError() << std::endl;
			continue;
		}

		
		std::time_t current = std::time( NULL );
		_wctime_s(buffer, _countof(buffer), &current);
		oss << L"----------------START---------------------\n";
		oss <<  buffer ; // buffer 中包含换行符
		oss << L"------------------------------------------\n";

		wcout << L"----------------START---------------------\n";
		wcout <<  buffer ; // buffer 中包含换行符
		wcout << L"------------------------------------------\n";

		fileList.erase( L"/.version.meta");
		
		int retryTimes = 0;
		while( retryTimes++ < 10)
		{

			PFS::CMetaInfoMap failedList;		// 出错的文件列表
			int count = 0;

			PFS::CMetaInfoMap::const_iterator itr = fileList.begin();
			PFS::CMetaInfoMap::const_iterator itrE = fileList.end();
			oss << L"total: " << fileList.size() << endl;
			wcout << L"total: " << fileList.size() << endl;
			//itr = fileList.lower_bound( std::wstring(L"/cfg/") );
			//itr = fileList.lower_bound( std::wstring(L"/map/map_058/") );
			for ( ; itr != itrE; ++ itr )
			{
				//url = baseurl + itr->first + L'?';
				url = L"/mhzx";
				url += itr->first;
				result = dir + itr->first;
				count ++;
				if( fileZiped.Open( hConnect, url ) )
				{
					if( bFastMode)
					{
						const PFS::CMetaInfo& mi = itr->second;
						if ( 0 == mi.GetTSID() || X20ID == mi.GetTSID())
						{
							if( fileZiped.GetSize() == itr->second.GetFileSize())
							{
								if ( count % 100 == 0 )
								{
									oss << count << endl;
									wcout << L".";
								}
							}
							else
							{
								oss << L"\ncheck file size " << url <<  L"failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;
								wcout << L"\ncheck file size " << url <<  L"failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;

								failedList.insert( *itr);
							}
						}
						else
						{
							if ( count % 100 == 0 )
							{
								oss << count << endl;
								wcout << L".";
							}
						}
					}
					else
					{
						tempFile.Seek(0, PFS::FSM_SET );
						tempFile.SetEnd();

						//oss << L"net file size: " << fileZiped.GetSize();
						if( !_CopyInetFile( fileZiped, tempFile ) )
						{
							continue;
						}

						tempFile.Seek(0, PFS::FSM_SET );

						CNativeFile file;
						if( file.Open( result, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR ) )
						{
							//PFS::FILEPFSXATTRIB attr = itr->second.GetFilePfsxAttributes();
							//TSID decoderId = GetDecoderTypeFromFilePfsxAttributes( attr);
							//if( !PFSX::DecodeFile(&fileZiped, &file, decoderId ) )
							if( !PFSX::DecodeFileEn ( tempFile, file, itr->second ) )
							{
								oss << L"\ndecode " << url <<  L"failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;
								wcout << L"\ndecode " << url <<  L"failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;

								failedList.insert( *itr);
							}
							else
							{
								if ( count % 100 == 0 )
								{
									oss << count << endl;
									wcout << L".";
								}
							}
						}
						else
						{
							oss << L"\ndownload " << url;
							oss <<  L" create local file failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;
							wcout << L"\ndownload " << url;
							wcout <<  L" create local file failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;

							failedList.insert( *itr);
						}
					}

					fileZiped.Close();
				}
				else
				{
					oss << L"\ndownload " << url << L" failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;
					wcout << L"\ndownload " << url << L" failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;

					failedList.insert( *itr);
				}

				if ( count % 10000 == 0 )
				{
					oss << endl << count << endl;
					wcout << endl << count << endl;
				}
			}

			if( failedList.empty())
			{
				break;
			}
			else
			{
				oss << L"\ncheck " << retryTimes << L" times! find " << failedList.size() << L" error, check error file again!" << std::endl;
				wcout << L"\ncheck " << retryTimes << L" times! find " << failedList.size() << L" error, check error file again!" << std::endl;

				fileList.swap( failedList);
				failedList.clear();
			}
		}
		

		{
			std::time_t current = std::time( NULL );
			_wctime_s(buffer, _countof(buffer), &current);
			oss << L"----------------END---------------------\n";
			oss << buffer ; // buffer 中包含换行符
			oss << L"------------------------------------------\n";
			wcout << L"----------------END---------------------\n";
			wcout << buffer ; // buffer 中包含换行符
			wcout << L"------------------------------------------\n";
		}

		InternetCloseHandle( hConnect );
		InternetCloseHandle( hInternet );
	}
}

void testServer( int argc, _TCHAR* argv[] )
{
	std::wstring url, result;
	CInetFile fileZiped;

	for ( int i = 0; i < _countof(ips); i ++ )
	{	
		for ( int c = 1; c < argc; c ++ )
		{
			url = L"http://";
			url += ips[i];
			url += L"/mhzx/";
			url += argv[c];
			url += L"?";
			result = L"E:\\test\\bin101\\";
			result += ips[i];
			result += L"\\";
			result += argv[c];
			wcout << ips[i] << std::endl;

			wcout << L"downloading " << url << std::endl;

			if( fileZiped.Open( url ) )
			{
				wcout << L"net file size: " << fileZiped.GetSize();

				CNativeFile file;
				if( file.Open( result, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR ) )
				{
					TSID tsid = PFSX::MakeTypeIDByName('Z','I','P') ;
					if( wcscmp(argv[c],L"patcher/patcher.exe") == 0 ||  
						wcscmp(argv[c],L"patcher/repair.exe") == 0 ||
						wcscmp(argv[c],L"patcher/inetutils.dll") == 0 ||
						wcscmp(argv[c],L".version.meta") == 0 )
					{
						tsid = 0;
					}
					
					if( PFSX::DecodeFile( &fileZiped, &file, tsid) )
					{
						wcout << L" local file size: " << file.GetSize();
						file.SetEnd();
						file.Seek( 0, PFS::FSM_SET );
						::PFS::helper::CMD5FileHashMethod hash;
						FILE_DATA_HASH hashValue = 0;
						hash.MakeFileHash( file, hashValue);
						wcout << L" hash: " << hashValue << std::endl;
						continue;
					}
					else
					{
						wcout <<  L" decode file failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;
					}
				}
				else
				{
					wcout <<  L" create local file failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;
				}
			}
			else
			{
				wcout << L" download file failed! GLE: " << GetLastError() << L" pfs:" << PFS::CEnv::GetLastError() << std::endl;
			}
		}
	}
	
}


_TCHAR* ipsss[] = 
{
	L"124.129.16.128",
	L"124.129.16.127",
	L"123.234.7.247",
	L"58.215.98.7",
	L"58.215.99.7",
	L"222.242.104.196",
	L"119.167.253.5",
	L"124.129.16.144",
	L"61.164.121.166",
	L"61.164.121.165",
	L"61.164.121.164",
	// L"58.215.100.8",
};

int _tmain(int argc, _TCHAR* argv[])
{
	setlocale(LC_ALL, "chs.936" );

	if ( argc <= 3 )
	{
		wcout<< L"Specify the check mode ( fast or normal) and destination directory (like D:\\temp\\ ) and server ip to download from - like these: \n" ;
		for ( int i = 0; i < _countof(ipsss); i ++ )
		{
			wcout << setw(2) << i+1 << L": " << ipsss[i] << endl;
		}
		
		wcout<< L"\nPress any key to quit...";
		_getch();

		return 0;
	}

	--argc, ++argv;
	bool bFastMode = ( 0 == wcscmp( argv[0], L"-fast"));
	--argc, ++argv;
	const _TCHAR* pszDestinationDir = argv[0];
	std::wostream& oss = wcout;

	try
	{
		oss.imbue(std::locale(".936"));
	}
	catch (std::runtime_error e)
	{
		oss << L"oss.imbue(std::locale(\".936\")) throws an runtime error - " <<  e.what() << endl;
	}

	// 输出时间信息
	wchar_t buffer[256] = {0};
	std::time_t current = std::time( NULL );
	_wctime_s(buffer, _countof(buffer), &current);
	oss << L"----------------START---------------------\n";
	oss <<  buffer ; // buffer 中包含换行符
	oss << L"------------------------------------------\n";
	
	testNewServer( --argc, ++argv, pszDestinationDir, bFastMode);
	
	//testNewServer( 1, ipsss+4);
	current = std::time( NULL );
	_wctime_s(buffer, _countof(buffer), &current);
	
	oss << L"\n\n-----------------DONE--------------------\n";
	oss << buffer;
	oss << L"\n------------------------------------------\n\n";

	wcout<< L"\nPress any key to quit...";
	_getch();

	return 0;
}

