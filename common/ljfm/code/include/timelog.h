#pragma once
#ifdef OUT_SYNC_FILEIO_TIME_LOG___

namespace LJFM { class COutLog; class LJFMID; }

namespace LJFMX
{
	namespace helper
	{
		class CLogScoped
		{
			LJFM::COutLog*	m_outlog;
			std::wstring	m_filename;
			std::wstring	m_funcname;
			DWORD			m_dwTimeStart;
			LJFM::LJFMID&		m_data;

			static unsigned long long	m_allSize;
			static DWORD				m_dwLastTime;
			static unsigned long long	m_LastSize;
		public:
			CLogScoped(const wchar_t* funcname, const wchar_t* filename, LJFM::LJFMID& data);
			CLogScoped(const wchar_t* funcname, const wchar_t* filename);
			~CLogScoped();
		};
	}
}

#else

namespace LJFMX
{
	namespace helper
	{
		class CLogScoped
		{
		public:
			CLogScoped(const wchar_t*, const wchar_t*, LJFM::LJFMID&) { }
			CLogScoped(const wchar_t*, const wchar_t*) { }
			~CLogScoped() { }
		};
	}
}

#endif
