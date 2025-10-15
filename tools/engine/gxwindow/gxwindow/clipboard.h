#pragma once

namespace GXWINDOW { 

class CClipBoard 
{
	CClipBoard();
	virtual ~CClipBoard();

	static CClipBoard s_instance;
public:
	static CClipBoard&  GetInstace();
public:
	bool SetString( const std::wstring& str);
	bool GetString( std::wstring& str);
public:
	static std::wstring StringToWString( const std::string& str);
};

} // namespace GXWINDOW { 