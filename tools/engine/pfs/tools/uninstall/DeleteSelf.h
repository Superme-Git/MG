#pragma once
// 删除运行程序本身，先用写批处理文件实现

#include <string>
#include "../../src/common.h"
#include "../../src/zip/zipfile.h"
#include "GlobalFunction.h"

typedef char* LPCHAR;
const LPCHAR g_batfile[4] = {
":try\r\n",
"del %s\r\n",
"if exist %s goto try\r\n",
"del %0"};

class CDeleteSelf
{
	std::wstring		m_path;
	std::string			m_appname;
	bool GetAppPath()
	{
		std::wstring appname;
		if( !CGlobalFunction::ParseAppString( m_path, appname))
			return false;
		return ZIPFILE::CStr::WideToMutilByte( appname, m_appname);
	}
	bool CreateBatFile()
	{
		m_path += L"delete.bat";
		PFSX::CNativeFile file;
		if( !file.Open( m_path, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
			return false;
		file.Write( g_batfile[0], strlen( g_batfile[0]));
		char buffer[MAX_PATH] = {0};
		sprintf_s( buffer, g_batfile[1], m_appname.c_str());
		file.Write( buffer, strlen( buffer));
		sprintf_s( buffer, g_batfile[2], m_appname.c_str());
		file.Write( buffer, strlen( buffer));
		file.Write( g_batfile[3], strlen( g_batfile[3]));
		return true;
	}

public:
	bool Do()
	{
		if( !GetAppPath())
			return false;
		if( !CreateBatFile())
			return false;
		return (int)::ShellExecute( NULL, L"open", m_path.c_str(), NULL, NULL, SW_HIDE) > 32;
	}
};