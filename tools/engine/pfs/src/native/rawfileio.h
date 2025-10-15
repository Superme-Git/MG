#pragma once

#include <algorithm>

namespace PFS {

class CFileIOException 
{
	int				m_nLastError;
#ifdef _DEBUG
	std::wstring	m_Filename;
#endif
public:
	CFileIOException( int error) : m_nLastError( error) { OutLog(); }
#ifdef _DEBUG	
	CFileIOException( int error, const std::wstring& filename) : m_nLastError( error), m_Filename( filename) { OutLog(); }
#else
	CFileIOException( int error, const std::wstring& filename) : m_nLastError( error) { OutLog(); }
#endif	
	~CFileIOException() {}
private:
	void OutLog()
	{
#ifdef _DEBUG
		CEnv::GetOutLogInstance()->Print( L"CFileIOException filename = %s code = %d\n", m_Filename.c_str(), m_nLastError);
#endif
	}
public:
	int GetLastError() const { return m_nLastError; }
#ifdef _DEBUG	
	const std::wstring& GetFileName() const { return m_Filename; }
#endif	
};

};

#ifdef _MSC_VER

// For windows APIs and some type-defined types, such as DWORD.
// Note: we should not omit this inclusion, or else it can 
// cause rawfileio.h can not be included alone.
#include <windows.h> 

#if _MSC_VER == 1310
#define _wcslwr_s( s, l)	_wcslwr( s)
#endif

namespace PFS {

class CFileImageHandle;

class CFileIO
{
	friend class CFileImageHandle;

	HANDLE			m_file;
#ifdef _DEBUG
	std::wstring	m_filename;
#endif
private:
	void ThrowFileIOException( int error)
	{
#ifdef _DEBUG		
		throw CFileIOException( error, m_filename);
#else
		throw CFileIOException( error);
#endif
	}

public:
	CFileIO( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa) 
	{
#ifdef _DEBUG		
		m_filename = filename;
#endif
		DWORD fileaccess, filecreate;
		switch ( fm) 
		{
		case FM_EXCL:  
			filecreate = OPEN_EXISTING;
			break;
		case FM_CREAT:
			filecreate = OPEN_ALWAYS;
			break;
		case FM_TRUNC:
		case FM_TRUNC | FM_EXCL:  
			filecreate = TRUNCATE_EXISTING;
			break;
		case FM_CREAT | FM_EXCL:
		case FM_CREAT | FM_TRUNC | FM_EXCL:
			filecreate = CREATE_NEW;
			break;
		case FM_CREAT | FM_TRUNC:
			filecreate = CREATE_ALWAYS;
			break;
		default:
			filecreate = OPEN_EXISTING;
			break;;
		}

		switch( fa) 
		{
		case FA_RDONLY:
			fileaccess = GENERIC_READ;
			break;
		case FA_WRONLY:  
			fileaccess = GENERIC_WRITE;
			break;
		case FA_RDWR:  
			fileaccess = GENERIC_READ | GENERIC_WRITE;
			break;
		default:   
			fileaccess = GENERIC_READ;
			break;
		}

		if( FM_CREAT == ( fm & FM_CREAT))
			CheckCreateParentDirectory( filename);
		m_file = ::CreateFileW( filename.c_str(), 
					fileaccess, 
					(fileaccess == GENERIC_READ) ? FILE_SHARE_READ : 0, 
					NULL, 
					filecreate, 
					0, 
					NULL);
		if( INVALID_HANDLE_VALUE == m_file)
			ThrowFileIOException( ::GetLastError());
	}
	~CFileIO() { assert( INVALID_HANDLE_VALUE != m_file); CloseHandle( m_file); }
public:
	static bool CheckCreateDirectory( const std::wstring& directory )
	{
		std::wstring dir = directory; 
		std::replace(dir.begin(), dir.end(), L'/', L'\\');
		std::wstring::size_type	posoffset = 0;
		while( std::wstring::size_type s = dir.find( L'\\', posoffset))
		{
			std::wstring substring = dir.substr( 0, s);
			if( !IsDirectoryExisting( substring))
			{
				if( !CreateDirectoryW( substring.c_str(), NULL) )
				{
					PFS::CEnv::GetOutLogInstance()->Print(
						L"ERROR: CheckCreateDirectory(path:%s)(%d)\n", substring.c_str(), ::GetLastError() );

					return false;
				}
			}
			posoffset = s + 1;
			if( std::wstring::npos == s)
			{
				break;
			}
		}

		return true;
	}

	static bool CheckCreateParentDirectory( const std::wstring& filename)
	{
		std::wstring fn = filename; 
		std::replace(fn.begin(), fn.end(), L'/', L'\\');
		std::wstring::size_type	posoffset = 0;
		std::wstring::size_type s = 0;
		while( (s = fn.find( L'\\', posoffset)) != std::wstring::npos )
		{
			std::wstring substring = fn.substr( 0, s);
			if( !IsDirectoryExisting( substring))
			{
				if( !CreateDirectoryW( substring.c_str(), NULL) )
				{
					PFS::CEnv::GetOutLogInstance()->Print(
						L"ERROR: CheckCreateParentDirectory(path:%s)(%d)\n", substring.c_str(), ::GetLastError() );
					return false;
				}
			}
			posoffset = s + 1;
		}

		return true;
	}
	
	static bool IsDirectoryExisting( const std::wstring& dir)
	{
		DWORD	dwAttribute = GetFileAttributesW( dir.c_str());
		if( INVALID_FILE_ATTRIBUTES == dwAttribute)
			return false;
		return FILE_ATTRIBUTE_DIRECTORY == ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
	}

	static bool IsFileExisting( const std::wstring& filename )
	{
		DWORD	dwAttribute = GetFileAttributesW( filename.c_str());
		if( INVALID_FILE_ATTRIBUTES == dwAttribute)
			return false;
		return FILE_ATTRIBUTE_DIRECTORY != ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
	}

	
#ifdef DeleteFile
#pragma push_macro( "DeleteFile" )
#undef DeleteFile
#endif // DeleteFile

	static int DeleteFile( const std::wstring& filename)
	{
		if( ::DeleteFileW( filename.c_str()))
			return 0;
		else
			return ::GetLastError();
	}

	static int DeleteFileW( const std::wstring& filename)
	{
		return DeleteFile( filename );
	}

#pragma pop_macro( "DeleteFile" )	

#ifdef RemoveDirectory
#pragma push_macro( "RemoveDirectory" )
#undef RemoveDirectory
#endif // RemoveDirectory

	// 
	// 删除Windows目录。
	//
	// directory - 被删除的目录名。
	// bFailIfNotEmpty - true: 如果目录非空，删除失败;
	//                   false: 如果目录非空，递归删除该目录下所有子目录和子文件。
	//
	static int RemoveDirectory( const std::wstring& directory, bool bFailIfNotEmpty = false)
	{
		if( bFailIfNotEmpty )
		{
			if( ::RemoveDirectoryW( directory.c_str() ) )
				return 0;
			else
				return ::GetLastError();
		}
		else
		{
			// 我们必须保证字符串最后有2个0结尾
			// 字符串不能含有 "/"
			// 字符串不能以 "\" 结尾
			std::wstring strFoler = directory; // copy it then can be change it.
			std::replace( strFoler.begin(), strFoler.end(), L'/', L'\\');
			std::wstring::size_type pos = strFoler.find_last_not_of( L"\\");
			if( pos != strFoler.size()-1)
			{
				strFoler.erase( pos+1);
			}
			strFoler.resize( strFoler.size() + 1, 0 );
			
			SHFILEOPSTRUCTW FileOp = {0};
			FileOp.pFrom = strFoler.c_str();
			FileOp.wFunc = FO_DELETE;
			FileOp.fFlags = FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_SILENT;
			return ::SHFileOperationW(&FileOp);
		}
	}

	static int RemoveDirectoryW( const std::wstring& directory, bool bFailIfNotEmpty = false)
	{
		return RemoveDirectory( directory, bFailIfNotEmpty );
	}

#pragma pop_macro( "RemoveDirectory" )

public:
	size_t Read( void* data, size_t need)
	{
		assert( INVALID_HANDLE_VALUE != m_file);

		DWORD	dwReaded;
		BOOL b = ReadFile( m_file, data, (DWORD)need, &dwReaded, NULL);
		if( !b)
			ThrowFileIOException( ::GetLastError());
		return dwReaded;
	}
	size_t Write( const void* data, size_t out)
	{
		assert( INVALID_HANDLE_VALUE != m_file);

		DWORD	dwWrited;
		BOOL	b = WriteFile( m_file, data, (DWORD)out, &dwWrited, NULL);
		if( !b)
			ThrowFileIOException( ::GetLastError());
		return dwWrited;
	}
	fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm)
	{
		assert( INVALID_HANDLE_VALUE != m_file);

		DWORD		dwMethod;
		switch( fsm)
		{
		case FSM_SET:
			dwMethod = FILE_BEGIN;
			break;
		case FSM_CUR:
			dwMethod = FILE_CURRENT;
			break;
		case FSM_END:
			dwMethod = FILE_END;
			break;
		default:
			return 0;
		}
		LARGE_INTEGER MoveTo, NewValue;
		MoveTo.QuadPart = pos;

		if( SetFilePointerEx( m_file, MoveTo, &NewValue, dwMethod))
			return NewValue.LowPart;
		else
			return -1;
	}
	fsize_t GetPos()
	{
		DWORD	dwResult = SetFilePointer( m_file, 0, NULL, FILE_CURRENT);
		if( INVALID_SET_FILE_POINTER == dwResult)
			ThrowFileIOException( ::GetLastError());
		return dwResult;
	}
	fsize_t GetSize()
	{
		assert( INVALID_HANDLE_VALUE != m_file);

		DWORD dwSize = GetFileSize( m_file, NULL);
		if( INVALID_FILE_SIZE == dwSize)
			ThrowFileIOException( ::GetLastError());
		return dwSize;
	}
	bool SetEnd()
	{
		bool bRes = SetEndOfFile( m_file) ? true : false;
		if( !bRes)
			ThrowFileIOException( ::GetLastError());
		return bRes;
	}
};

class CFileImageHandle
{
protected:
	class CCountableHandle
	{
		HANDLE			m_handle;
		volatile LONG	m_counter;
	public:
		CCountableHandle( HANDLE handle) : m_handle( handle), m_counter( 1) {}
		~CCountableHandle() { assert( 0 == m_counter); CloseHandle( m_handle); }
	public:
		void AddRef();
		void Release();
		HANDLE GetHandle() { return m_handle; } 
	};
	CCountableHandle*	m_image;
	fsize_t				m_size;
	bool				m_bNew;
public:
	CFileImageHandle() : m_image( NULL), m_size( 0), m_bNew( false) {}
	CFileImageHandle( CFileIO& fio);
	CFileImageHandle( const wchar_t* name, size_t size);

	~CFileImageHandle() 
	{ 
		Destroy();
	}

	CFileImageHandle( const CFileImageHandle& src) : m_image( src.m_image), m_size( src.m_size)
	{	
		if( m_image)
			m_image->AddRef();
	}

	CFileImageHandle& operator=( const CFileImageHandle& src) 
	{
		if( this == &src)
			return *this;
		if( m_image)
			m_image->Release();
		m_size = src.m_size;
		m_image = src.m_image;
		if( m_image)
			m_image->AddRef();
		return *this;
	}
public:
	bool Initialize( CFileIO& fio);

	// 允许使用方显式释放文件映像句柄
	// 参见CZipFS的使用场景
	void Destroy()
	{
		if( m_image) 
		{
			m_image->Release(); 
			m_image = NULL;
		}
		m_size = 0;
	}

	HANDLE GetHandle() const
	{
		if( m_image)
			return m_image->GetHandle();
		else
			return NULL;
	}

	fsize_t GetSize() const
	{
		return m_size;
	}

	bool IsNew() const
	{
		return m_bNew;
	}
};

class CFileWritableImage
{
	void*	m_data;
public:
	CFileWritableImage( CFileImageHandle& fih)
	{
		HANDLE hImage = fih.GetHandle();
		if( NULL == hImage)
			throw CFileIOException( ERROR_INVALID_PARAMETER);
		m_data = MapViewOfFileEx( hImage, FILE_MAP_WRITE, 0, 0, 0, NULL);
		if( NULL == m_data)
			throw CFileIOException( ::GetLastError());
	}
	~CFileWritableImage()
	{
		assert( m_data);
		UnmapViewOfFile( m_data);
	}
public:
	void* GetData()
	{
		return m_data;
	}
};

class CAbstactFileImage : public CUncopyableObject
{
protected:
	CFileImageHandle	m_image;
	void*				m_data;
	fsize_t				m_size;
public:
	CAbstactFileImage( CFileImageHandle& fih) : m_image( fih), m_data( NULL), m_size( m_image.GetSize()) {}
	virtual ~CAbstactFileImage() {}
public:
	virtual const void* GetData() const { return m_data; }
	virtual fsize_t GetSize() const { return m_size; }
};

class CFileImage : public CAbstactFileImage
{
public:
	CFileImage( CFileImageHandle& fih);
	virtual ~CFileImage();
};

class CPieceFileImage : public CAbstactFileImage
{
	void*					m_base;
public:
	CPieceFileImage( CFileImageHandle& fih, fsize_t offset, size_t size);
	virtual ~CPieceFileImage();
};

class CRawImageData : public CAbstactImageImplement
{
	CAbstactFileImage*	m_image;
public:
	CRawImageData( CAbstactFileImage* fi) : m_image( fi) {}
	virtual ~CRawImageData() { delete m_image; }
public:
	virtual const void* GetData() const { assert( m_image); return m_image->GetData(); }
	virtual fsize_t GetSize() const { assert( m_image); return m_image->GetSize(); }
};

class CFindFiles
{
	std::wstring		m_strBasePath;
public:
	CFindFiles( const std::wstring& strBasePath) : m_strBasePath( strBasePath) {}
	virtual ~CFindFiles() {}
private:
	void OnFoundFile( const std::wstring& strPath, WIN32_FIND_DATAW& wfd)
	{
		if( L'.' == wfd.cFileName[0])
			return;

		_wcslwr_s( wfd.cFileName, MAX_PATH );
		std::wstring	strTemp( wfd.cFileName );

		if( FILE_ATTRIBUTE_DIRECTORY == ( FILE_ATTRIBUTE_DIRECTORY & wfd.dwFileAttributes))
		{
			std::wstring	strCurrentPath = strPath + strTemp + L"/";
			SeachFiles( strCurrentPath);
		}
		else
		{
			std::wstring	strCurrentFile = strPath + strTemp;
			OnOneFoundFile( strCurrentFile);
		}
	}
	void SeachFiles( const std::wstring& strPath)
	{
		std::wstring		strFindPath = m_strBasePath + strPath + L"\\*.*";

		WIN32_FIND_DATAW	wfd;

		HANDLE	hFind = FindFirstFileW( strFindPath.c_str(), &wfd);
		if( INVALID_HANDLE_VALUE == hFind)
			return;
		OnFoundFile( strPath, wfd);
		while( FindNextFileW( hFind, &wfd))
			OnFoundFile( strPath, wfd);
		FindClose( hFind);
	}
protected:
	virtual void OnOneFoundFile( const std::wstring& filename) = 0;
public:
	virtual void DoFind() { SeachFiles( L"/"); }
	const std::wstring& GetBasePath() const { return m_strBasePath; }
};

#else
#endif




} // namespace PFS {