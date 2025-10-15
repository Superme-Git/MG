#include "../common.h"
#include "runzip.h"
#include "../base/packetfilesystem.h"
// #include <conio.h> // for _getch()
namespace PFS
{
	class CRunZipBaseFile : public ZIPFILE::CZipSystemReadFile
	{
		CRunZipFS&					m_zfs;
	public:
		CRunZipBaseFile( CRunZipFS& zfs, const ZIPFILE::CRunFileCentral& central) 
			: CZipSystemReadFile( zfs.m_zipfile, central, zfs.m_ImageHandle), m_zfs( zfs) {}

		virtual ~CRunZipBaseFile() 
		{ 
			m_zfs.OnFileClose( this); 
		}
		virtual size_t Write( const void* data, size_t size) 
		{
			return FILE_SIZE_ERROR;
		}
		virtual fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm) 
		{
			try
			{
				return CZipSystemReadFile::Seek( pos, fsm);
			}
			catch( ZIPFILE::CZipReadError&)
			{
			}
			return FILE_SIZE_ERROR;
		}
		virtual bool SetEnd()
		{
			return false;
		}
	};

	class CRunZipFileImageHandle : public CFileImageHandle
	{
	private:
		class CMutex
		{
		protected:
			HANDLE m_hMutex;

		public:
			CMutex( LPCWSTR lpName )
			{
				// ע�������mutexNameΪ�����lpName������ǰ׺
				// ԭ����lpName�Ѿ����������ļ�ӳ������Windows�������ں˶���������
				// �������������Ȳ�����.
				std::wstring mutexName(L"pfsmutex_");
				mutexName += lpName;

				//////////////////////////////////////////////////////////////////////////
				// 
				// ����ע�͵���_putws(), _getch() �ȴ��������ڿ���̵���ʹ�õ�,
				// ��Ҫʱ���Դ�ע�ͽ��б�Ҫ�ĵ���
				//
				//////////////////////////////////////////////////////////////////////////

				//_putws(L"Press any key to try to lock ...");
				//_getch();
				m_hMutex = CreateMutexW( NULL, TRUE, mutexName.c_str() );
				DWORD dwError = ::GetLastError();
				if( m_hMutex == NULL )
				{
					CEnv::GetOutLogInstance()->Print(L"CreateMutex(%s) failed (%d)\n",
						mutexName.c_str(), dwError );
					return;
				}

				if( dwError == ERROR_ALREADY_EXISTS )
				{
					//_putws(L"Already locked by another process! Press any key to wait...");
					//_getch();
					WaitForSingleObject( m_hMutex, INFINITE );
					ReleaseMutex( m_hMutex ); // Release the lock immediately!
					CloseHandle( m_hMutex );
					m_hMutex = NULL;
				}
				else if ( dwError == ERROR_SUCCESS )
				{
					//_putws(L"locked! Press any key to continue ...");
					//_getch();
				}
				else
				{
					CEnv::GetOutLogInstance()->Print(L"CreateMutex(%s). unexpected error code (%d)\n",
						mutexName.c_str(), dwError );
					assert(  dwError == ERROR_SUCCESS );
					m_hMutex = NULL;
				}
			}

			~CMutex()
			{
				if( m_hMutex != NULL )
				{
					//_putws(L"Press any key to unlock ...");
					//_getch();
					ReleaseMutex( m_hMutex );
					//_putws(L"unlocked! Press any key to quit...");
					//_getch();
					CloseHandle( m_hMutex );
					m_hMutex = NULL;
				}
			}
		};

	public:
		CRunZipFileImageHandle ( 
			const wchar_t* name,
			size_t size, 
			CRunZipFS* pRunZipFS,
			const ZIPFILE::CRunFileCentral* central)
			// : CFileImageHandle() - �������Զ�ʹ��ȱʡ���캯��
		{
			HANDLE image = CreateFileMapping( INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, size, name);
			if( NULL == image)
			{
#ifdef _DEBUG
				throw CFileIOException( ::GetLastError(), name);
#else
				throw CFileIOException( ::GetLastError());
#endif
			}

			// �жϸոմ�����FilesMapping�����Ǳ��Ҵ������Ǳ�������
			m_bNew = ERROR_ALREADY_EXISTS != ::GetLastError();
			m_image = new CCountableHandle( image);
			m_size = size;
			
			{	// ����̻�������������
				CMutex _autoLock( name );
				if( m_bNew )
				{
					// ���FileMapping�����Ǳ����״δ���,���ʼ����
					CFileWritableImage	fwi( *this );
					// ��Ϊ����ʹ����this,����ǰ����뱣֤m_image,m_size�ѱ���ʼ��
					CRunZipBaseFile rzbf( *pRunZipFS, *central);
					rzbf.ReadAll( fwi.GetData(), size);
				}
			}
		}
	};

	
	CRunZipFS::CRunZipFS( void*)
		: CPFSBase( FST_RUNZIP), m_basefile( NULL) 
	{}

	CRunZipFS::CRunZipFS() 
		: m_basefile( NULL) 
	{}

	CRunZipFS::~CRunZipFS()
	{
		m_cachemap.clear();
		if( m_basefile)
		{
			CEnv::CloseFile( m_basefile);
			m_basefile = NULL;
		}
		if( m_BasePFS)
			m_BasePFS->RemoveRefMountFSDevive( m_strBasePath);
	}

	void CRunZipFS::MakeImageHandle()
	{
		if( PFSX::IGetFileIO* rawfile = dynamic_cast<PFSX::IGetFileIO*>( m_basefile))
			m_ImageHandle.Initialize( rawfile->GetRawFileIO());
	}

	void CRunZipFS::DumpInfos( COutLog& outlog) const
	{
		outlog.Print( L"\tCRunZipFS = { m_strBasePath = '%s' m_cachemap.size() = %d }\n", m_strBasePath.c_str(), m_cachemap.size());

#ifdef OUTLOG_DUMP_INFOS_WITH_FILE_NAME
		outlog.Print( L"\t\t{ m_lastlostfilename = '%s' hash1 = %d(0x%08x) hash2 = %d(0x%08x) }\n", m_lastlostfilename.c_str(), m_lastlostfilehash.hash1, m_lastlostfilehash.hash1, m_lastlostfilehash.hash2, m_lastlostfilehash.hash2);
#endif

		CPFSBase::DumpInfos( outlog);

		outlog.Print( L"\tm_zipfile = {\n");
		m_zipfile.DumpInfos( outlog);
		outlog.Print( L"}\n");
	}

	int CRunZipFS::Initialize( const std::wstring& device, MOUNT_TYPE mt)
	{
		if( device.empty() )
		{
			assert( !device.empty() );
			return EC_INVALID_MOUNT_DEVICE;
		}

		if( !IsMountReadOnly( mt))
			return EC_FS_READONLY;
		
		m_deviceName = device;
		m_MountType = mt;

		try
		{
			std::wstring newname = device;
			CPFSBase* base = CPacketFileSystem::GetInstance().FindPFSBase( newname);
			if( base)
			{
				assert( CheckDirFileStringFormat( newname));
				int nRes = base->OpenFile( newname, FM_EXCL, FA_RDONLY, m_basefile);
				if (EC_SUCCESS != nRes)
					return nRes;
				m_BasePFS = base;
				m_strBasePath = newname;
				base->AddRefMountFSDevive( newname);
			}
			else
			{
				PFSX::CNativeFile* rawfile = new PFSX::CNativeFile();
				if( !rawfile->Open( device, FM_EXCL, FA_RDONLY))
				{
					delete rawfile;
					return CEnv::GetLastError();
				}
				m_basefile = rawfile;
			}
			m_zipfile.SetFileIO( *m_basefile);

			MakeImageHandle();
		}
		catch( CFileIOException& fio)
		{
			return fio.GetLastError();
		}
		catch( ZIPFILE::CZipFileBadFormat&)
		{
			return EC_INVALID_MOUNT_DEVICE;
		}

		// �����豸·��
		CalcNativeDevicePath();
		InitMetaInfoFromMetaFile();

		return EC_SUCCESS;
	}

	int CRunZipFS::OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file)
	{
		if( (FM_EXCL != fm) || ( FA_RDONLY != fa))
		{
			return EC_FILE_READONLY;
		}
		std::wstring name( filename);
		//std::transform( name.begin(), name.end(), name.begin(), ::towlower);
		name.erase( 0, name.find_first_not_of( L'/'));
		const ZIPFILE::CRunFileCentral* central = m_zipfile.GetFileCentral( name);
		if( !central)
		{
#ifdef OUTLOG_DUMP_INFOS_WITH_FILE_NAME
			m_lastlostfilename = name;
			m_lastlostfilehash.hash1 = ZIPFILE::CHash::Hash_ELF( (const char*)name.c_str(), name.size()*sizeof( wchar_t));
			m_lastlostfilehash.hash2 = ZIPFILE::CHash::Hash_JS( (const char*)name.c_str(), name.size()*sizeof( wchar_t));
#endif
			return EC_FILE_NOT_FOUND;
		}

		if( IsMountCacheData(GetMountType()) )
		{
			HashPair key( central->GetHash1(), central->GetHash2());

			// ��m_cachemap��������
			CMutex::CScoped lock( m_cs ); 

			CImageCacheMap::iterator it = m_cachemap.find( key);
			if( it != m_cachemap.end())
			{
				CImageData& data = it->second;
				file = new CImageFile( data);
				return EC_SUCCESS;
			}

			if ( central->IsRawData() )
			{
				// ���⴦��RawData�����,������ν���ڴ�copy
				CRunZipBaseFile	tempfile( *this, *central);
				PFS::CImageData data = tempfile.GetImage();
				m_cachemap.insert( std::make_pair( key, data));
				file = new CImageFile( data);
				return EC_SUCCESS;
			}

			try
			{
				std::wstring fullPath = GetNativeFilePath(filename);
				size_t sizeall = central->GetOrginalSize();
				CRunZipFileImageHandle fih( fullPath.c_str(), sizeall, this, central );
				CImageData data( new CRawImageData( new CFileImage( fih)));
				m_cachemap.insert( std::make_pair( key, data));
				file = new CImageFile( data);
				return EC_SUCCESS;
			}
			catch( CFileIOException& fioe ) 
			{
				CEnv::GetOutLogInstance()->Print( L"CRunZipFS::OpenFile(%s) exception occurred(%d).\n", filename.c_str(), fioe.GetLastError() );
			}
		}

		// ���ַ�ʽ����RawData����������Ż�,���ڷ�RawData�����Ҳ��ͨ��ReadAll��ʽ����
		CRunZipBaseFile	tempfile( *this, *central );		
		PFS::CImageData imageData = tempfile.GetImage();
		file = new CImageFile( imageData );

		return EC_SUCCESS;
	}

	bool CRunZipFS::IsFileExisting( const std::wstring& filename)
	{
		if( filename.empty() )
		{
			return false;
		}

		std::wstring zipFileName = &filename[1]; // zipFileName���� '/' ��ͷ
		return m_zipfile.GetFileCentral( zipFileName ) != NULL;
	}

	bool CRunZipFS::IsDirectoryExisting( const std::wstring& path )
	{
		if ( path.empty() )
		{
			return false;
		}

		std::wstring zipDir = &path[1]; 
		zipDir += L"/";
		return m_zipfile.GetFileCentral( zipDir ) != NULL;
	}
	
	void CRunZipFS::InitMetaInfoFromMetaFile()
	{ 
		if( (GetMountType() & MT_RUNTIME) == 0 )
		{
			CBaseFile* file = NULL;
			OpenFile( CPFSBase::GetMetaFileName(), FM_EXCL, FA_RDONLY, file);
			if( file)
			{
				CMetaInfoMap& mim = CPFSBase::GetMetaInfoMap();
				CMetaFileRead::DoRead( *file, mim);
				CEnv::CloseFile( file);
			}
		}
	}
}