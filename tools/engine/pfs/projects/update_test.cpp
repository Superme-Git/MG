//////////////////////////////////////////////////////////////////////////
//
// update_test.cpp
//
// ���ڲ�����ͻ��˸�����صĹ��ܺ��߼�
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
	// ʹ��Translate��ʽcopy�ļ�
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

	// ��strInputDirָ����Ŀ¼�е��ļ���ʹ��Translate��ʽ�򿪲����浽strOutputDirָ����Ŀ¼��
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
				// ����FileMetaInfo��Ϣ
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
	//		�Ƚ�����MetaInfo�Ƿ�һ��
	//		����ֻ�Ƚ��ļ���С��Hashֵ
	//
	bool IsEqualMetaInfo(const CMetaInfo& rLeft,const CMetaInfo& rRight)
	{
		return rLeft.GetFileSize() == rRight.GetFileSize() &&
			rLeft.GetFileHash() == rRight.GetFileHash();
	}

	// 
	// summary:
	//		��Server�˵�MetaInfo��Ϣ���û������汾��MetaInfo���в��컯�ж�
	//		����ȷ����Ҫ���µ��б�����
	//
	// parameters:
	//		[in] rServer - Server�˵�MetaInfo��Ϣ
	//		[in] rLocal - �û�������MetaInfo��Ϣ
	//		[out] rUpdate - ������������Ҫ���µ�MetaInfo��Ϣ
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

			// ���������ͬ���ж�MetaInfo�Ƿ�һ��������һ���ļӵ������б���
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

	// ��ָ��Ŀ¼�µ�MetaInfo�ļ�
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

	// ��MetaInfo�ļ�д��ָ��Ŀ¼��
	bool WriteMetaInfoFile( std::wstring dir, const CMetaInfoMap& mimFile )
	{
		bool bOK = false;
		
		// ֱ��ʹ��NativeFileдMetaInfo�ļ�
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
	// ��urlָ����λ������metainfo�ļ�������ȡ���ݵ�rMetaInfoMap������
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

		// ����1 - ֱ�ӽ������ļ���Ϊ����Դ
		if ( CMetaFileRead::DoRead( inetFile, rMetaInfoMap ) )
		{
			return true;
		}
		
		// ���Է���2 - �������ļ����ص�������ʱ�ļ���
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
			// ���������ļ�ָ�뵽�ļ�ͷ����������Ķ�������ʧ��
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

	// ɾ��ָ����Ŀ¼�Լ�Ŀ¼�µ������ļ�
	bool DeleteCacheFolder( std::wstring strFolder )
	{
		// ���Ǳ��뱣֤�ַ��������2��0��β
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
			// ɾ����ʱĿ¼
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
		// ���ر�������Ŀ¼�µ�MetaInfo
		b = ReadMetaInfoFile( rundir, mimLocal );
		if( !b )
		{
			return false;
		}

		std::wstring url;
		// �Ӹ��ڵ���ȷ���ļ����ص�URL
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
			 
		// ���ش�������������Ѿ����ص���������Ŀ¼�µ�MetaInfo
		// ���������µİ汾��Ϣ
		b = DownloadMetaInfoFile( url, mimServer );
		if( !b )
		{
			return false;
		}

		//
		// ����2��MetaInfoMap�Ĳ
		//
		// ע�⣬����û�п���mimUpdate���Ƿ���Ҫɾ���Ѿ�������Ҫ���ļ��Ĳ���
		//
		SubtractMetaInfoMap( mimServer, mimLocal, mimUpdate );

		b = mimUpdate.size() > 0;
		if( b )
		{
			// "/."Ϊ��Ŀ¼��Ϣ�����к���Download URL�ȹؼ���Ϣ��
			// ����������Ҫ�嵽Update��Ϣ�ļ���
			CMetaInfoMap::const_iterator itr = mimServer.find( L"/."); 
			if( itr != mimServer.end() )
			{
				CMetaInfoMap::_Pairib inserted = mimUpdate.insert( *itr );
				if( inserted.first->second.GetFileDownloadURI().size() == 0 )
				{
					// ע�⣬�����WebServer�Դ�Сд���У������Ҫ������Сд��ȷ��URL
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
	// ����MetaInfo����ȽϺ�MetaInfo��Ϣ�Ķ�д
	//
	void TestUpdate()
	{
		std::wclog.imbue(std::locale("chs"));
		//std::wcout.imbue(std::locale("chs"));

		std::wstring runDir = L"D:\\2DGame"; // ��Ϸ��װĿ¼
		
		CMetaInfoMap mimServer, mimUpdate;
		
		//
		// �ک�����Mount�ļ�ϵͳ
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
		// ��һ��������°汾
		//
		bool b = CheckNewVersion( runDir, mimServer, mimUpdate );
		if( !b )
		{
			return;
		}

		//
		// �ڶ����������°汾�ļ�����ʱĿ¼��
		//
		std::wstring cacheDir( L"D:\\2DGame.cache" );

		// �����ļ�
		CDownloadFiles downloader( mimUpdate, cacheDir );
		if( !downloader.Download() )
		{
			return ;
		}

		// �����������浽��ʱĿ¼�У�����ִ��ת��
		WriteMetaInfoFile( cacheDir, mimUpdate );

		//
		// ������������ʱĿ¼�е��ļ����Ƶ���Ϸ��װĿ¼��
		//

		// �����µ�MetaInfo Copy��Ŀ��Ŀ¼��
		// �������Ա�֤Ŀ��Ŀ¼ͬʱ������
		//WriteMetaInfoFile( runDir, mimServer );

		// ����Ҫ���µĳ����ļ� Copy����ʱĿ¼������Ŀ¼��
		TranslateFilesInFolder( cacheDir, runDir );

		// ���෴˳��unmount�ļ�ϵͳ
		CSetupMountManager::GetInstance().UnmountPFS();
		CEnv::Unmount( RUN_ROOT );
	}

	void Test()
	{
		std::wclog.imbue(std::locale("chs"));

		std::wstring runDir = L"E:\\2DGame"; // ��Ϸ��װĿ¼
		
		CMetaInfoMap mimServer, mimUpdate;
		
		//
		// �ک�����Mount�ļ�ϵͳ
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

		// ����Ҫ���µĳ����ļ� Copy����ʱĿ¼������Ŀ¼��
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


		// ���෴˳��unmount�ļ�ϵͳ
		CSetupMountManager::GetInstance().UnmountPFS();
		CEnv::Unmount( RUN_ROOT );
	}

}; // namespace UPDATETEST