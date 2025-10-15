//////////////////////////////////////////////////////////////////////////
//
// update_test.cpp
//
// 用于测试与客户端更新相关的功能和逻辑
//
// author : yanghaibo
// date : 2008-11-24
//
#define  WIN32_LEAN_AND_MEAN
#include <Windows.h>
#include <ShellApi.h>
#include <iostream>
#include "..\src\common.h"
#include <atlbase.h>
#include "../../xmlio/msdom.h"
#include <strsafe.h>


namespace PFSX
{
	// 使用Translate方式copy文件
	bool TranslateFile( const std::wstring& srcFileName, const std::wstring& dstFileName );
} 

using namespace PFS;

namespace UPDATETEST
{
	//<data>
	//	<f n="/cfg"/>
	//	<f n="/effect"/>
	//	<f n="/map"/>
	//	<f n="/pak"/>
	//	<f n="/role"/>
	//	<f n="/sound"/>
	//	<f n="/ui"/>
	//</data>

	class CSetupMetaFile
	{
		std::vector<std::wstring>&		m_metas;
	public:
		CSetupMetaFile( std::vector<std::wstring>& metas) : m_metas( metas) {}
		~CSetupMetaFile() {}

		bool Read( PFS::CBaseFile& file)
		{
			MSXMLDOM::CReader reader;

			if( PFS::EC_SUCCESS != reader.OpenFile( file) )
			{
				return false;
			}

			MSXMLDOM::CReadNode root, child;
			reader.GetRootNode(root);
			size_t count = root.GetChildrenCount();
			std::wstring strValue;
			for( size_t i=0; i<count; i++)
			{
				root.GetChildAt( i, child);
				child.GetAttribute( L"n", strValue );
				m_metas.push_back( strValue );
			}
			return true;
		}
	};

#define RES_ROOT  L"/.cache"
#define RUN_ROOT  L"/.run"

	class CSetupMountManager
	{
	public:
		~CSetupMountManager();

		bool MountPFS(const std::wstring& strRunRoot);
		void CommitPFS();
		void UnmountPFS();

		static CSetupMountManager& GetInstance()
		{
			static CSetupMountManager _instance;
			return _instance;
		}
		
	protected:
		CSetupMountManager() {};
		std::vector<std::wstring> m_mountPoints;
	};
	
	CSetupMountManager::~CSetupMountManager()
	{
		UnmountPFS();
	}

	void CSetupMountManager::CommitPFS()
	{
		std::vector<std::wstring>::const_iterator itr = m_mountPoints.begin();
		for( ; itr != m_mountPoints.end(); ++ itr )
		{
			CEnv::Commit( *itr );
		}
	}

	void CSetupMountManager::UnmountPFS()
	{
		std::vector<std::wstring>::const_iterator itr = m_mountPoints.begin();
		for( ; itr != m_mountPoints.end(); ++ itr )
		{
			CEnv::Unmount( *itr, false );
		}

		m_mountPoints.clear();
	}

	bool CSetupMountManager::MountPFS(const std::wstring& strRunRoot)
	{
		std::vector<std::wstring> mountPoints;
		CSetupMetaFile metaFile(mountPoints);
		CBaseFile* file = CEnv::OpenFile( strRunRoot + L"/.setup.meta", FM_EXCL, FA_RDONLY );
		if( file != NULL && metaFile.Read(*file) )
		{
			bool bRet = false;
			std::vector<std::wstring>::const_iterator itr = mountPoints.begin();
			for( ; itr != mountPoints.end(); ++ itr )
			{
				std::wstring pfsdir = strRunRoot + *itr;
				bRet = CEnv::Mount( pfsdir, pfsdir + L".pfs", FST_ZIP, MT_WRITABLE );
				if ( !bRet )
				{
					std::wclog<<L"\nERROR: Mount("<< pfsdir << L"," << pfsdir + L".pfs" << L") failed!" << std::endl;
					break;
				}
				else
				{
					m_mountPoints.push_back( pfsdir );
				}
			}

			CEnv::CloseFile( file );

			return bRet;
		}

		return false;
	}

	// 将strInputDir指定的目录中的文件，使用Translate方式打开并保存到strOutputDir指定的目录中
	bool TranslateFilesInFolder( const std::wstring& strInputDir, 
		const std::wstring& strOutputDir/*, CWnd* pMessageWnd */)
	{
		bool bRet = false;
		CMetaInfoMap::const_iterator itr;

		bRet = CEnv::Mount( RES_ROOT, strInputDir, FST_NATIVE, MT_READONLY );
		if ( !bRet )
		{
			std::wclog<<L"ERROR: Mount("<< RES_ROOT << L"," << strInputDir << L") failed!" << std::endl;
			return false;
		}

		//if( pMessageWnd != NULL )
		//	pMessageWnd->PostMessage( WM_DOWNLOAD_STEP );

		CMetaInfoMap paths ;
		if( !CEnv::GetMetaInfos(RES_ROOT, paths ) )
		{
			std::wclog<<L"ERROR: GetMetaInfos("<< RES_ROOT << L") failed!" << std::endl;
			goto __EXIT;
		}
		/*if( pMessageWnd != NULL )
			pMessageWnd->PostMessage( WM_DOWNLOAD_STEP );*/

		bRet = false;
	
		std::wclog<<L"INFO: "<< paths.size()-1 << L" files need to copy:" << std::endl;

		size_t count = 0;
		for( itr = paths.begin(); itr != paths.end(); ++ itr )
		{
			const std::wstring& path =  (*itr).first;
			if ( path == L"/." )
			{
				continue;
			}

			if( PFSX::TranslateFile( RES_ROOT + path , RUN_ROOT + path ) )
			{
				// 更新FileMetaInfo信息
				if( !CEnv::SetFileMetaInfo(RUN_ROOT + path,  (*itr).second ) )
				{
					std::wclog<<L"\nERROR: SetFileMetaInfo("<< path << L") failed!" << std::endl;
					goto __EXIT;
				}
				else
				{
					count ++;
					std::wclog<<L".";
				}
			}
			else
			{
				std::wclog<<L"\nERROR: CopyFileEx("<< path << L") failed!" << std::endl;
				goto __EXIT;
			}
			
			/*if( pMessageWnd != NULL )
				pMessageWnd->PostMessage( WM_DOWNLOAD_STEP );
			*/
		}

		bRet = true;

__EXIT:
		std::wclog<<L"\nINFO: "<< count << L" files copied!" << std::endl;

		CEnv::Unmount( RES_ROOT );

		//if( pMessageWnd != NULL )
		//	pMessageWnd->PostMessage( WM_DOWNLOAD_STEP );
		
		return bRet;
	}

	bool CopyToNativeFile(CBaseFile& rSrcFile, std::wstring strDstFileName )
	{
		PFSX::CNativeFile dstFile;
		if( !dstFile.Open ( strDstFileName, FM_CREAT|FM_TRUNC, FA_WRONLY ))
		{
			std::wclog<<L"ERROR: Open("<< strDstFileName << L") failed!" << std::endl;
			return false;
		}

		unsigned char buffer[8192];
		size_t read = 0;
		while( (read = rSrcFile.Read( buffer, 8192 ) ) != 0 )
		{
			if( dstFile.Write( buffer, read ) == 0 )
			{
				std::wclog<<L"ERROR: Copy src file to "<< strDstFileName << L" failed!" << std::endl;
				return false;
			}
		}

		return true;
	}

	//
	// summary:
	//		比较两个MetaInfo是否不一样
	//		这里只比较文件大小和Hash值
	//
	bool IsEqualMetaInfo(const CMetaInfo& rLeft,const CMetaInfo& rRight)
	{
		return rLeft.GetFileSize() == rRight.GetFileSize() &&
			rLeft.GetFileHash() == rRight.GetFileHash();
	}

	// 
	// summary:
	//		对Server端的MetaInfo信息和用户本机版本的MetaInfo进行差异化判断
	//		用于确定需要更新的列表内容
	//
	// parameters:
	//		[in] rServer - Server端的MetaInfo信息
	//		[in] rLocal - 用户本机的MetaInfo信息
	//		[out] rUpdate - 计算结果，包含要更新的MetaInfo信息
	//
	void SubtractMetaInfoMap(const CMetaInfoMap& rServer,
							 const CMetaInfoMap& rLocal,
							 CMetaInfoMap& rUpdate )
	{
		CMetaInfoMap::const_iterator itrS = rServer.begin();
		CMetaInfoMap::const_iterator itrL = rLocal.begin();
		CMetaInfoMap::key_compare comparator;
		for ( ; itrS != rServer.end() &&  itrL != rLocal.end();  ++ itrL )
		{
			const std::wstring& rName2 = itrL->first;

			for( ; itrS != rServer.end() && comparator( itrS->first, rName2 ); ++ itrS )
			{
				rUpdate.insert( *itrS );
			}

			if ( itrS == rServer.end() )
			{
				break;
			}

			// 如果名字相同，判断MetaInfo是否一样，将不一样的加到更新列表中
			if (  itrS->first.compare( rName2 ) == 0 ) 
			{
				if ( !IsEqualMetaInfo( itrS->second, itrL->second) )
				{
					rUpdate.insert( *itrS );
				}

				++ itrS;
			}

		}

		for( ; itrS != rServer.end() ; ++ itrS )
		{
			rUpdate.insert( *itrS );
		}
	}

	// 读指定目录下的MetaInfo文件
	bool ReadMetaInfoFile( std::wstring dir, CMetaInfoMap& mimFile )
	{
		bool bOK = CEnv::GetMetaInfosEx(RUN_ROOT, mimFile);
		if ( bOK )
		{
			return true;
		}
		else 
		{
			std::wclog<<L"ERROR! Get local meta info file failed! " << dir << std::endl;
			return false;
		}
	}

	// 将MetaInfo文件写到指定目录下
	bool WriteMetaInfoFile( std::wstring dir, const CMetaInfoMap& mimFile )
	{
		bool bOK = false;
		
		// 直接使用NativeFile写MetaInfo文件
		PFSX::CNativeFile theFile;
		if( theFile.Open( dir + CFileMetaInfo::GetMetaFileName(),
			FM_CREAT|FM_TRUNC, FA_WRONLY ) )
		{
			bOK = CMetaFileWrite::DoWrite( theFile, mimFile );
		}

		if( bOK )
		{
			return true;
		}
		else 
		{
			std::wclog<<L"ERROR: Write meta info file failed!"<<std::endl;
			return false;
		}
	}

	//
	// 从url指定的位置下载metainfo文件，并读取内容到rMetaInfoMap对象中
	//
	bool DownloadMetaInfoFile( std::wstring url, CMetaInfoMap& rMetaInfoMap )
	{
		PFSX::CInetFile inetFile;
		if( !inetFile.Open( url + CFileMetaInfo::GetMetaFileName() ) )
		{
			std::wclog<<L"ERROR: Download meta info file failed!"<< url + CFileMetaInfo::GetMetaFileName()<< std::endl;
			return false;
		}

		fsize_t szTotal = inetFile.GetSize( );
		if( szTotal == 0 )
		{
			std::wclog<<L"ERROR: Empty meta info file?" << url + CFileMetaInfo::GetMetaFileName()<<std::endl;
			return false;
		}

		// 方案1 - 直接将网络文件作为输入源
		if ( CMetaFileRead::DoRead( inetFile, rMetaInfoMap ) )
		{
			return true;
		}
		
		// 尝试方案2 - 将网络文件下载到本地临时文件中
		CTempFile theFile;
		if( !theFile.Open() )
		{
			std::wclog<<L"ERROR: Create temp file for meta info failed!"<<std::endl;
			return false;
		}
		else
		{
			size_t szTotalRead = 0;
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
				theFile.Seek( 0, FSM_SET );

				bOK = CMetaFileRead::DoRead( theFile, rMetaInfoMap );
			}

			if ( bOK )
			{
				return true;
			}
			else
			{
				std::wclog<<L"ERROR: Download meta info file to temp file failed!"<<std::endl;
				return false;
			}
		}
	}

	// 删除指定的目录以及目录下的所有文件
	bool DeleteCacheFolder( std::wstring strFolder )
	{
		// 我们必须保证字符串最后有2个0结尾
		strFolder.resize( strFolder.size() + 1, 0 );
		SHFILEOPSTRUCT FileOp = {0};
		FileOp.pFrom = strFolder.c_str();
		FileOp.wFunc = FO_DELETE;
		FileOp.fFlags = FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_SILENT;
		int iRet = SHFileOperation(&FileOp);
		if ( iRet != ERROR_SUCCESS )
		{
			std::wclog<<L"ERROR: Delete temp files folder failed! Folder: " <<  strFolder << L"ErrorCode:"<< iRet <<std::endl;
			return false;
		}
		return true;
	}

	class CDownloadFiles
	{
	protected:
		// contains root url
		const CMetaInfoMap& m_rmimDownload;
		std::wstring m_strBaseUrl;
		std::wstring m_strDir;
		
	public:
		CDownloadFiles(const CMetaInfoMap& rmim, std::wstring strDir)
			: m_rmimDownload(rmim)
			, m_strDir(strDir)
		{
		}

		~CDownloadFiles()
		{
			// 删除临时目录
			DeleteCacheFolder( m_strDir );
		}

		bool Download( );

	protected:
		bool DownloadOneFile( const std::wstring& fileName, const CMetaInfo& rMetaInfo );
	};

	bool CDownloadFiles::DownloadOneFile(const std::wstring& fileName, const CMetaInfo& rMetaInfo )
	{
		std::wstring strRelUrl = rMetaInfo.GetFileDownloadURI();
		if( strRelUrl.size() == 0 )
		{
			strRelUrl = fileName;
		}

		std::wstring strFullUrl = m_strBaseUrl + strRelUrl;
		std::wstring strPath = m_strDir + fileName;
		
		PFSX::CInetFile inetFile;
		if( !inetFile.Open( strFullUrl ) )
		{
			int LastError = CEnv::GetLastError();
			std::wclog<<L"ERROR: Failed to download file: " << strFullUrl<< L" ErrorCode:" << LastError << std::endl;
			//return false;
			return true;
		}

		fsize_t szTotal = inetFile.GetSize( );
		if( szTotal == 0 )
		{
			std::wclog<<L"ERROR: Empty file? " << strFullUrl << std::endl;
			return false;
		}
		
		return CopyToNativeFile( inetFile, strPath );
	}

	bool CDownloadFiles::Download( )
	{
		CMetaInfoMap::const_iterator baseUrlItr = m_rmimDownload.find( L"/.");
	
		if( baseUrlItr != m_rmimDownload.end() )
		{
			m_strBaseUrl = baseUrlItr->second.GetFileDownloadURI();
			if ( m_strBaseUrl.size() == 0 )
			{
				std::wclog<<L"ERROR: No base url for download files!"<<std::endl;
				return false;
			}
		}

		std::wclog<<L"INFO: Start Downloading:" << std::endl;

		CMetaInfoMap::const_iterator itr = m_rmimDownload.begin();
		for ( ; itr != m_rmimDownload.end(); ++ itr )
		{
			if ( itr != baseUrlItr ) // skip the base url 
			{
				if( !DownloadOneFile( itr->first, itr->second ) )
				{
					std::wclog<<L"\nERROR: Downloading file failed!" << itr->first << std::endl;
					return false;
				}
				else
				{
					std::wclog<<L".";
				}
			}
		}

		std::wclog<<L"\nINFO: Downloading Done!" << std::endl;
		return true;
	}

	bool HasNewVersion(std::wstring rundir)
	{
		return true;
	}

	bool CheckNewVersion( 
		std::wstring rundir, 
		CMetaInfoMap& mimServer, 
		CMetaInfoMap& mimUpdate )
	{
		CMetaInfoMap mimLocal;
		bool b = true;
		// 加载本地运行目录下的MetaInfo
		b = ReadMetaInfoFile( rundir, mimLocal );
		if( !b )
		{
			return false;
		}

		std::wstring url;
		// 从根节点中确定文件下载的URL
		CMetaInfoMap::const_iterator itr = mimLocal.find( L"/."); 
		if( itr != mimLocal.end() )
		{
			url = itr->second.GetFileDownloadURI();
		}
		else
		{
			std::wclog<<L"ERROR: No base url in local metainfo file!"<<std::endl;
			return false;
		}
			 
		// 加载从网络服务器上已经下载到本地运行目录下的MetaInfo
		// 它含有最新的版本信息
		b = DownloadMetaInfoFile( url, mimServer );
		if( !b )
		{
			return false;
		}

		//
		// 计算2个MetaInfoMap的差集
		//
		// 注意，这里没有考虑mimUpdate中是否考虑要删除已经不再需要的文件的操作
		//
		SubtractMetaInfoMap( mimServer, mimLocal, mimUpdate );

		b = mimUpdate.size() > 0;
		if( b )
		{
			// "/."为根目录信息，其中含有Download URL等关键信息，
			// 无论怎样都要插到Update信息文件中
			CMetaInfoMap::const_iterator itr = mimServer.find( L"/."); 
			if( itr != mimServer.end() )
			{
				CMetaInfoMap::_Pairib inserted = mimUpdate.insert( *itr );
				if( inserted.first->second.GetFileDownloadURI().size() == 0 )
				{
					// 注意，下面的WebServer对大小写敏感，因此需要给出大小写正确的URL
					inserted.first->second.SetFileDownloadURI( url );
				}
			}
		}
		else
		{
			std::wclog<<L"INFO: No need update any files!" << std::endl;
		}

		return b;
	}
	
	//
	// 测试MetaInfo差异比较和MetaInfo信息的读写
	//
	void TestUpdate()
	{
		std::wclog.imbue(std::locale("chs"));
		//std::wcout.imbue(std::locale("chs"));

		std::wstring runDir = L"D:\\2DGame"; // 游戏安装目录
		
		CMetaInfoMap mimServer, mimUpdate;
		
		//
		// 第〇步：Mount文件系统
		//
		bool bOK;
		bOK = CEnv::Mount( RUN_ROOT, runDir, FST_NATIVE, MT_WRITABLE );
		if ( !bOK )
		{
			return;
		}

		bOK = CSetupMountManager::GetInstance().MountPFS( RUN_ROOT );
		if ( !bOK )
		{
			CEnv::Unmount( RUN_ROOT );
			return;
		}

		//
		// 第一步：检查新版本
		//
		bool b = CheckNewVersion( runDir, mimServer, mimUpdate );
		if( !b )
		{
			return;
		}

		//
		// 第二步：下载新版本文件到临时目录中
		//
		std::wstring cacheDir( L"D:\\2DGame.cache" );

		// 下载文件
		CDownloadFiles downloader( mimUpdate, cacheDir );
		if( !downloader.Download() )
		{
			return ;
		}

		// 将差异结果保存到临时目录中，用于执行转换
		WriteMetaInfoFile( cacheDir, mimUpdate );

		//
		// 第三步：将临时目录中的文件复制到游戏安装目录中
		//

		// 将最新的MetaInfo Copy到目标目录下
		// 这样可以保证目标目录同时被创建
		//WriteMetaInfoFile( runDir, mimServer );

		// 将需要更新的程序文件 Copy到临时目录到运行目录中
		TranslateFilesInFolder( cacheDir, runDir );

		// 按相反顺序unmount文件系统
		CSetupMountManager::GetInstance().UnmountPFS();
		CEnv::Unmount( RUN_ROOT );
	}

	void Test()
	{
		std::wclog.imbue(std::locale("chs"));

		std::wstring runDir = L"E:\\2DGame"; // 游戏安装目录
		
		CMetaInfoMap mimServer, mimUpdate;
		
		//
		// 第〇步：Mount文件系统
		//
		bool bOK;
		bOK = CEnv::Mount( RUN_ROOT, runDir, FST_NATIVE, MT_WRITABLE );
		if ( !bOK )
		{
			return;
		}

		bOK = CSetupMountManager::GetInstance().MountPFS( RUN_ROOT );
		if ( !bOK )
		{
			CEnv::Unmount( RUN_ROOT );
			return;
		}

		// 将需要更新的程序文件 Copy到临时目录到运行目录中
		// UpdateFilesInFolder0( runDir );
		CFile file; 
		if( file.Open( RUN_ROOT L"/cfg/actiontext.xml", FM_CREAT, FA_RDWR ) )
		{
			file.Write(L"actiontext==33-actiontext.xml", 30 );
			file.Close();
		}
		else
		{
			std::wclog<<L"open /cfg/actiontext.xml faild." << CEnv::GetLastError() << std::endl;
		}
		
		CSetupMountManager::GetInstance().CommitPFS();
 
		// Close file after commit, you should got such warning message in the console:
		//		WARN: After committing the ZipFS, there is still 1 file open.
		//
		// file.Close();
		//

		if( !CFile::RemoveFile(RUN_ROOT  L"/cfg/actiontext.xml") )
		{
			std::wclog<<L"RemoveFile /cfg/actiontext.xml faild." << CEnv::GetLastError() << std::endl;
		}

		if( file.Open(RUN_ROOT  L"/cfg/menpaiinfo.xml", FM_CREAT, FA_RDWR ) )
		{
			file.Write(L"=-=-=-=-=0123456789-=-=-=-menpaiinfo.xml", 30 );
			file.Close();
		}
		else
		{
			std::wclog<<L"open /cfg/menpaiinfo.xml faild." << CEnv::GetLastError() << std::endl;
		}
		
		CSetupMountManager::GetInstance().CommitPFS();

		
		if( file.Open(RUN_ROOT L"/cfg/actiontext.xml", FM_CREAT, FA_RDWR ) )
		{
			file.Write(L"/cfg/a2ctiontext.xml", 20 );
			file.Close();
		}
		else
		{
			std::wclog<<L"open /cfg/actiontext.xml faild." << CEnv::GetLastError() << std::endl;
		}

		if( file.Open(RUN_ROOT  L"/cfg/menpaiinfo.xml", FM_CREAT, FA_RDWR ) )
		{
			file.Write(L"987654321-=-=-=-menpaiinfo.xml", 10 );
			file.Close();
		}
		else
		{
			std::wclog<<L"open /cfg/menpaiinfo.xml faild." << CEnv::GetLastError() << std::endl;
		}
		
		CSetupMountManager::GetInstance().CommitPFS();


		// 按相反顺序unmount文件系统
		CSetupMountManager::GetInstance().UnmountPFS();
		CEnv::Unmount( RUN_ROOT );
	}

}; // namespace UPDATETEST