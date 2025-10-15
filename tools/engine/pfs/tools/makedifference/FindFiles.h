#pragma once

class CFindFiles
{
protected:
	std::wstring		m_strBasePath;
	
	void OnFoundFile( const std::wstring& strPath, WIN32_FIND_DATAW& wfd)
	{
		if( L'.' == wfd.cFileName[0])
			return;

		_wcslwr_s( wfd.cFileName, MAX_PATH );
		std::wstring	strTemp( wfd.cFileName );

		if( FILE_ATTRIBUTE_DIRECTORY == ( FILE_ATTRIBUTE_DIRECTORY & wfd.dwFileAttributes))
		{
			std::wstring	strCurrentPath = strPath + strTemp + L"\\";
			OnOneFoundPath( strCurrentPath);
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
public:
	CFindFiles( const std::wstring& strBasePath) : m_strBasePath( strBasePath) {}
	virtual ~CFindFiles() {}

protected:
	virtual void OnOneFoundPath( const std::wstring& pathname) = 0;
	virtual void OnOneFoundFile( const std::wstring& filename) = 0;

public:
	virtual void DoFind() 
	{
		OnOneFoundPath( L"\\");
		SeachFiles( L"\\"); 
	}
	const std::wstring& GetBasePath() const { return m_strBasePath; }
};
