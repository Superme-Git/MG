#pragma once

namespace ZIPFILE
{
	class CFileCentralRead;
};

namespace PFS
{
	class CBaseFile;
	class CZipTempFileContainer;
	class CzipTempFileManager;
	
	class CZipTempFile : public CBaseFile
	{
	protected:
		CZipTempFileContainer&	m_container;
		fsize_t					m_startpos;
		fsize_t					m_offset;
		fsize_t					m_size;
		bool					m_closed;

	public:
		CZipTempFile( CZipTempFileContainer& container, fsize_t startpos) 
			: m_container( container), m_startpos( startpos), m_offset( 0), m_size( 0), m_closed( false) {}

		virtual size_t Read( void* data, size_t size);
		virtual size_t Write( const void* data, size_t size);
		virtual fsize_t GetSize()
		{
			return m_size;
		}
		virtual fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm);
		virtual fsize_t GetPos()
		{
			return m_offset;
		}
		virtual bool SetEnd()
		{
			m_size = m_offset;
			return true;
		}
		virtual CImageData GetImage()
		{
			fsize_t old = m_offset;
			Seek( 0, PFS::FSM_SET);
			CImageData image = BaseFileToImageData( *this);
			Seek( old, PFS::FSM_SET);
			return image;
		}

		virtual bool ReadAll(void* data, fsize_t size );

	public:
		bool ReOpen();
		void Close() { m_closed = true;}
		bool IsClosed() { return m_closed; }
		CZipTempFileContainer& GetContainer() { return m_container; }

	protected:
		virtual void Delete()
		{
			// 重写父类的Delete函数，不执行delete this。
			m_closed = true;
		}
	};

	class CZipTempFileContainer
	{
		friend class CZipTempFile;
		friend class CZipTempFileManager;

		typedef std::map<std::wstring, CZipTempFile*>	CZipTempFileMap;
		CZipTempFileMap			m_mapTempFile;
		CZipTempFile*			m_curwritefile;		// 当前正在写的文件

		CTempFile				m_file;
		
		CZipTempFile* GetTempFile( const std::wstring& filename);
		CZipTempFile* CreateTempFile( const std::wstring& filename);

		void Delete() { delete this;}

	public:
		CZipTempFileContainer( ) : m_curwritefile( NULL) {}
		~CZipTempFileContainer();

		void DetachCentral( const std::wstring& filename);
		static void CloseFile( CZipTempFile*& rpFile );
		
		// 返回仍然打开的文件数目
		size_t Commit();
		bool IsEmpty() const
		{
			return m_mapTempFile.empty();
		}
	};

	// zip临时文件管理器，
	// 为了避免zip系统写操作时创建太多临时文件
	class CZipTempFileManager
	{
		typedef std::vector< CZipTempFileContainer*>	CZipTempFileContainerVector;
		CZipTempFileContainerVector	m_containers;
		CZipTempFileContainer* m_curcontainer;

		CZipTempFileContainer* AddContainer();

		static bool WriteFileData( CZipTempFile* pSrc, CZipTempFile* pDes);
		static bool WriteFileData( ZIPFILE::CFileCentralRead* centralread, CZipTempFile* pDes);

	public:
		CZipTempFileManager() : m_curcontainer( NULL) {}
		~CZipTempFileManager();

		// 返回仍然打开的文件数目
		size_t Commit();

		void Destroy();
		
		CZipTempFile* GetTempFile( const std::wstring& filename);
		
		CZipTempFile* CreateTempFile( const std::wstring& filename, ZIPFILE::CFileCentralRead* centralread);
	};
};