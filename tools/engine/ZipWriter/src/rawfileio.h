#pragma once

#include <algorithm>
#include <string>

namespace ZIP {

	class CFileIOException 
	{
		int				m_nLastError;
	#ifdef _DEBUG
		std::wstring	m_Filename;
	#endif
	public:
		CFileIOException( int error) 
			: m_nLastError( error)
		{
		}
	#ifdef _DEBUG	
		CFileIOException( int error, const std::wstring& filename)
			: m_nLastError( error), m_Filename( filename)
		{
		
		}
	#else
		CFileIOException( int error, const std::wstring& filename)
			: m_nLastError( error)
		{
		
		}
	#endif	
		~CFileIOException()
		{
		
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

namespace ZIP {

	class CFileIO
	{
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
						//PFS::CEnv::GetOutLogInstance()->Print(
						//	L"ERROR: CheckCreateDirectory(path:%s)(%d)\n", substring.c_str(), ::GetLastError() );

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
						/*PFS::CEnv::GetOutLogInstance()->Print(
							L"ERROR: CheckCreateParentDirectory(path:%s)(%d)\n", substring.c_str(), ::GetLastError() );*/
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

		static bool IsFileExisting( const std::wstring& filename)
		{
			DWORD	dwAttribute = GetFileAttributesW( filename.c_str());
			if( INVALID_FILE_ATTRIBUTES == dwAttribute)
				return false;
			return FILE_ATTRIBUTE_DIRECTORY != ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
		}

		static int DeleteFile( const std::wstring& filename)
		{
			if( ::DeleteFileW( filename.c_str()))
				return 0;
			else
				return ::GetLastError();
		}

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

#else
#endif

} // namespace ZIP {