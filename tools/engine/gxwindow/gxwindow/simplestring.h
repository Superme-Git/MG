#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { 

class CSimpleString : public std::wstring
{
public:
	CSimpleString() {}
	CSimpleString( const wchar_t* text) : std::wstring( text) {}
	virtual ~CSimpleString() {}

};

class CAnsiString : public std::string
{
public:
	CAnsiString() {}
	virtual ~CAnsiString() {}
};

} // namespace INLINECLASS { 

} // namespace GXWINDOW { 



