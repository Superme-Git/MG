#pragma once

//#define OUT_SYNC_FILEIO_TIME_LOG___

#ifdef OUT_SYNC_FILEIO_TIME_LOG___

namespace PFS { class COutLog; class CImageData; }

namespace PFSX {
namespace helper {

class CLogScoped
{
	PFS::COutLog*	m_outlog;
	std::wstring	m_filename;
	std::wstring	m_funcname;
	DWORD			m_dwTimeStart;
	PFS::CImageData&		m_data;

	static unsigned long long	m_allSize;
	static DWORD				m_dwLastTime;
	static unsigned long long	m_LastSize;
public:
	CLogScoped( const wchar_t* funcname, const wchar_t* filename, PFS::CImageData& data);
	CLogScoped( const wchar_t* funcname, const wchar_t* filename);
	~CLogScoped();
};


} // namespace helper {
} // namespace PFSX {


#else

namespace PFSX {
namespace helper {

class CLogScoped
{
public:
	CLogScoped( const wchar_t*, const wchar_t*, PFS::CImageData&) { }
	CLogScoped( const wchar_t*, const wchar_t*) { }
	~CLogScoped() { }
};

} // namespace helper {
} // namespace PFSX {

#endif // OUT_SYNC_FILEIO_TIME_LOG___
