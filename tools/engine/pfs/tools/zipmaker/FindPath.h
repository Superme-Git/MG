#pragma once
#include <string>

class CFindPath
{
protected:
	std::wstring	m_basepath;
	virtual bool OnFind( const std::wstring& child, bool bPath) = 0;

public:
	CFindPath( const std::wstring& basepath) : m_basepath( basepath) {}
	bool DoFind()
	{
		std::wstring allpath = m_basepath + L"\\*.*";
		
		WIN32_FIND_DATAW wfd;
		HANDLE	hFind = FindFirstFileW( allpath.c_str(), &wfd);
		if( INVALID_HANDLE_VALUE == hFind)
			return false;
		bool res = false;
		do
		{
			std::wstring temp = wfd.cFileName;
			if( (L"." == temp) || (L".." == temp))
				continue;
			if( OnFind( temp, !!(FILE_ATTRIBUTE_DIRECTORY & wfd.dwFileAttributes)))
			{
				res = true;
				break;
			}
		}while( FindNextFile( hFind, &wfd));
		FindClose( hFind);
		return res;
	}
};