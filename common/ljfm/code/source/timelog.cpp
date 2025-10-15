#include "../include/common.h"
#include "timelog.h"

#ifdef OUT_SYNC_FILEIO_TIME_LOG___

namespace LJFMX
{
	namespace helper
	{
		unsigned long long CLogScoped::m_allSize = 0;
		unsigned long long CLogScoped::m_LastSize = 0;
		DWORD CLogScoped::m_dwLastTime = 0;

		CLogScoped::CLogScoped(const wchar_t* funcname, const wchar_t* filename, LJFM::LJFMID& data) : m_funcname(funcname), m_filename(filename), m_data(data)
		{
			m_outlog = LJFM::LJFMOpen::GetOutLogInstance();
			m_dwTimeStart = timeGetTime();
		}

		CLogScoped::CLogScoped(const wchar_t* funcname, const wchar_t* filename) : m_funcname(funcname), m_filename(filename), m_data(LJFM::LJFMID())
		{
			m_outlog = LJFM::LJFMOpen::GetOutLogInstance();
			m_dwTimeStart = timeGetTime();
		}
		CLogScoped::~CLogScoped()
		{
			DWORD dwNow = timeGetTime() - m_dwTimeStart;
			if (dwNow > 2)
				m_outlog->Print(L"%s end %s %d %d", m_funcname.c_str(), m_filename.c_str(), m_data.GetSize(), dwNow);
		}
	}
}

#endif
