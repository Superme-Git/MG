#pragma once

class CCodeOut
{
public:
	CCodeOut() {}
	virtual ~CCodeOut() {}
public:
	virtual void Print( const char* szInfo, ...) const
	{
		va_list		vl;
		va_start( vl,  szInfo);
		VPrint( szInfo, vl);
		va_end( vl);
	}
	virtual void VPrint( const char* szInfo, va_list vl) const = 0;
};

class CFileCodeOut : public CCodeOut
{
	FILE*	m_pFile;
public:
	CFileCodeOut( const char* szFilename) { fopen_s( &m_pFile, szFilename, "w"); }
	virtual ~CFileCodeOut() { fclose( m_pFile); }
public:
	virtual void VPrint( const char* szInfo, va_list vl) const
	{
		vfprintf( m_pFile, szInfo, vl);
	}
};

class CStringCodeOut : public CCodeOut
{
	enum
	{
		BUFFER_SIZE	= 8388608, // 1K * 2
	};

	mutable char*					m_Buffer/*[BUFFER_SIZE]*/;
	mutable std::ostringstream		m_oss;
public:
	CStringCodeOut() { m_Buffer = new char[ BUFFER_SIZE];}
	virtual ~CStringCodeOut() { delete m_Buffer; }
public:
	virtual void VPrint( const char* szInfo, va_list vl) const
	{
		vsprintf_s( m_Buffer, BUFFER_SIZE, szInfo, vl);
		m_oss << m_Buffer;   
	}
	std::string GetString() const
	{
		return m_oss.str();
	}
};
