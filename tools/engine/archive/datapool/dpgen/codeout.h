#pragma once

#include <stdio.h>
#include <stdarg.h>
#include <sstream>

namespace CODEOUT {

class CCodeOut
{
public:
	CCodeOut() {}
	virtual ~CCodeOut() {}
public:
	virtual void Print( const wchar_t* szInfo, ...) const
	{
		va_list		vl;
		va_start( vl,  szInfo);
		VPrint( szInfo, vl);
		va_end( vl);
	}
	virtual void VPrint( const wchar_t* szInfo, va_list vl) const = 0;
};

class CMemBufferCodeOut : public CCodeOut
{
	enum
	{
		BUFFER_SIZE	= 2048,
	};
	mutable wchar_t					m_Buffer[BUFFER_SIZE];
public:
	CMemBufferCodeOut() {}
	virtual ~CMemBufferCodeOut() {}
protected:
	virtual void OnPrint( const wchar_t* szText) const = 0;
public:
	virtual void VPrint( const wchar_t* szInfo, va_list vl) const
	{
		vswprintf_s( m_Buffer, BUFFER_SIZE, szInfo, vl);
		OnPrint( m_Buffer);
	}
};

class CFileCodeOut : public CMemBufferCodeOut
{
	FILE*	m_pFile;
public:
	CFileCodeOut( const char* szFilename) { fopen_s( &m_pFile, szFilename, "w"); }
	CFileCodeOut( const wchar_t* szFilename) { _wfopen_s( &m_pFile, szFilename, L"w"); }
	virtual ~CFileCodeOut() { fclose( m_pFile); }
private:
	const std::string& GetAnsiString( const wchar_t* szText, std::string& buffer) const
	{
		UINT	acp = GetACP();
		int nResult = WideCharToMultiByte( acp, 0, szText, -1, NULL, NULL, NULL, NULL);
		if( 0 == nResult)
			return buffer;
		buffer.resize( nResult + 1);

		nResult = WideCharToMultiByte( acp, 0, szText, -1, &buffer[0], nResult, NULL, NULL);
		buffer[nResult] = 0;
		return buffer;
	}
public:
	virtual void OnPrint( const wchar_t* szText) const
	{
		std::string		buffer;
		fprintf( m_pFile, "%s", GetAnsiString( szText, buffer).c_str());
	}
};

class CStringCodeOut : public CMemBufferCodeOut
{
	mutable std::wstringstream		m_oss;
public:
	CStringCodeOut() {}
	virtual ~CStringCodeOut() {}
public:
	virtual void OnPrint( const wchar_t* szText) const
	{
		m_oss << szText;   
	}
	std::wstring GetString() const
	{
		return m_oss.str();
	}
};

} // namespace CODEOUT {