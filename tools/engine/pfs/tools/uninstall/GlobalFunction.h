#pragma once
#include <string>

class CGlobalFunction
{
	CGlobalFunction();
public:
	// 得到可执行文件目录及名称
	// path返回目录，appname返回文件名
	static bool ParseAppString( std::wstring& path, std::wstring& appname)
	{
		path.resize( MAX_PATH);
		DWORD len = ::GetModuleFileName( NULL, const_cast<LPWSTR>( path.c_str()), MAX_PATH);
		if( 0 == len)
			return false;
		path.resize( len);
		std::wstring::size_type pos = path.find_last_of( L"\\")+1;
		if( 0 == pos)
			return false;
		appname = path.substr( pos);
		path.erase( pos);
		return true;
	}

	// 删除目录，将目录里文件及子目录也删除
	static bool DeletePath( std::wstring path)
	{
		path.resize( path.size() + 1, 0 );
		SHFILEOPSTRUCT FileOp = {0};
		FileOp.pFrom = path.c_str();
		FileOp.wFunc = FO_DELETE;
		FileOp.fFlags = FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_SILENT;
		int iRet = ::SHFileOperation( &FileOp);
		if ( iRet != ERROR_SUCCESS )
		{
			return false;
		}
		return true;
	}
};