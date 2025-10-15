#pragma once
#include <string>


class CFileException 
{
public:
	int				m_nLastError;
#ifdef _DEBUG
	std::wstring	m_Filename;
#endif
public:
	CFileException( int error = 0) : m_nLastError( error) { OutLog(); }
#ifdef _DEBUG	
	CFileException( int error, const std::wstring& filename) 
		: m_nLastError( error), m_Filename( filename) { OutLog(); }
#else
	CFileException( int error, const std::wstring& filename) 
		: m_nLastError( error) { OutLog(); }
#endif	
	~CFileException() {}
private:
	void OutLog()
	{
#ifdef _DEBUG
		//( L"CFileException filename = %s code = %d\n", m_Filename.c_str(), m_nLastError);
#endif
	}
public:
	int GetLastError() const { return m_nLastError; }
#ifdef _DEBUG	
	const std::wstring& GetFileName() const { return m_Filename; }
#endif	
};

class CFile
{
public:
	HANDLE			m_hFile; // raw file handle

protected:
#ifdef _DEBUG
	std::wstring	m_filename;
#endif

	void ThrowFileIOException( int error)
	{
#ifdef _DEBUG		
		throw CFileException( error, m_filename);
#else
		throw CFileException( error);
#endif
	}

public:
	enum 
	{
		FM_EXCL         = 0x00000001,  /*! 文件必须存在 */
		FM_CREAT        = 0x00000002,  /*! 创建文件 */
		FM_TRUNC        = 0x00000004,  /*! 清除文件内容 */
	};
	typedef unsigned int FILE_MODE;

	enum FILE_ACCESS
	{
		FA_RDONLY       = 0x00000001,	/*! open for reading only */
		FA_WRONLY       = 0x00000002,	/*! open for writing only */
		FA_RDWR         = 0x00000003,	/*! open for reading and writing */
	};

	enum { FILE_SIZE_ERROR = -1, };
	enum FILE_SEEK_METHOD
	{
		FSM_SET = 0,
		FSM_CUR,
		FSM_END,
	};


	CFile(void);
	~CFile(void);

	CFile( LPCWSTR szFileName, FILE_MODE fm, FILE_ACCESS fa);
	bool Open( LPCWSTR szFileName, FILE_MODE fm, FILE_ACCESS fa, CFileException* pException = NULL);

	static bool CheckCreateParentDirectory( LPCWSTR szFileName );
	static bool CheckCreateDirectory( LPCWSTR szDirectory );
	static bool IsDirectoryExisting( LPCWSTR szDirectory );
	static bool IsFileExisting( LPCWSTR szFileName);
	static int DeleteFile( LPCWSTR szFileName);

public:
	size_t Read( void* data, size_t need);
	size_t Write( const void* data, size_t out);
	size_t Seek( long pos, FILE_SEEK_METHOD fsm);

	size_t GetPos();
	size_t GetSize();
	bool SetEnd();
	bool Flush();

	void Close();
};
