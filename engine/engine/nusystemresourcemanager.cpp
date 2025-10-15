#include "../renderer/nurenderer.h"
#include "nuenginebase.h"
#include "nusystemresourcemanager.h"

namespace Nuclear
{
	SystemResourceManager::SystemResourceManager(Renderer *pRenderer)
		: m_iCPUCount(1), m_i64CPUFrequency(0), m_itmp64CPUFrequency(0), m_iSystemTimerPeriod(100000), m_iAvailTextureMemory(0)
		, m_pRenderer(pRenderer), m_CPUFrequencyValid(false),m_bInitedSound(false)
	{
#if (defined WIN7_32)
		LARGE_INTEGER tmpFreq;
		QueryPerformanceFrequency(&tmpFreq);
		m_itmp64CPUFrequency = tmpFreq.QuadPart;
#endif
	}

	SystemResourceManager::~SystemResourceManager()
	{
	}

	void SystemResourceManager::Init()
	{
#if (defined WIN7_32)
		SYSTEM_INFO SystemInfo;
		::GetSystemInfo(&SystemInfo);
		m_iCPUCount = (int)SystemInfo.dwNumberOfProcessors;

		OSVERSIONINFO OSVersion = { sizeof(OSVersion) };
		::GetVersionEx(&OSVersion);
		m_WindowsVersionInfo.m_iPlatformID = (int)OSVersion.dwPlatformId;
		m_WindowsVersionInfo.m_iMajorVersion = (int)OSVersion.dwMajorVersion;
		m_WindowsVersionInfo.m_iMinorVersion = (int)OSVersion.dwMinorVersion;
		m_WindowsVersionInfo.m_iBuildNumber = (int)OSVersion.dwBuildNumber;

		m_WindowsVersionInfo.m_strInfo = L"UnknownOS";
		if( m_WindowsVersionInfo.m_iPlatformID == VER_PLATFORM_WIN32_WINDOWS)
		{
			if( m_WindowsVersionInfo.m_iMajorVersion == 4 && m_WindowsVersionInfo.m_iMinorVersion == 0 )
				m_WindowsVersionInfo.m_strInfo = L"Win95   ";
			else if( m_WindowsVersionInfo.m_iMajorVersion == 4 && m_WindowsVersionInfo.m_iMinorVersion == 10 )
				m_WindowsVersionInfo.m_strInfo = L"Win98   ";
			else if( m_WindowsVersionInfo.m_iMajorVersion == 4 && m_WindowsVersionInfo.m_iMinorVersion == 90 )
				m_WindowsVersionInfo.m_strInfo = L"WinMe   ";
		}
		else if( m_WindowsVersionInfo.m_iPlatformID == VER_PLATFORM_WIN32_NT )
		{
			if( m_WindowsVersionInfo.m_iMajorVersion == 3 && m_WindowsVersionInfo.m_iMinorVersion == 51 )
				m_WindowsVersionInfo.m_strInfo = L"WinNT3.5";
			else if( m_WindowsVersionInfo.m_iMajorVersion == 4 && m_WindowsVersionInfo.m_iMinorVersion == 0 )
				m_WindowsVersionInfo.m_strInfo = L"WinNT4.0";
			else if( m_WindowsVersionInfo.m_iMajorVersion == 5 && m_WindowsVersionInfo.m_iMinorVersion == 0 )
				m_WindowsVersionInfo.m_strInfo = L"Win2000";
			else if( m_WindowsVersionInfo.m_iMajorVersion == 5 && m_WindowsVersionInfo.m_iMinorVersion == 1 )
				m_WindowsVersionInfo.m_strInfo = L"WinXP  ";
			else if( m_WindowsVersionInfo.m_iMajorVersion == 5 && m_WindowsVersionInfo.m_iMinorVersion == 2 )
				m_WindowsVersionInfo.m_strInfo = L"Win2003";
			else if( m_WindowsVersionInfo.m_iMajorVersion == 6 && m_WindowsVersionInfo.m_iMinorVersion == 0 )
				m_WindowsVersionInfo.m_strInfo = L"Vista  ";
			else if( m_WindowsVersionInfo.m_iMajorVersion == 6 && m_WindowsVersionInfo.m_iMinorVersion == 1 )
				m_WindowsVersionInfo.m_strInfo = L"Win7  ";
		}
		wchar_t buffer[32];
		wsprintf(buffer, L" %d.%d(%d)", m_WindowsVersionInfo.m_iMajorVersion, m_WindowsVersionInfo.m_iMinorVersion, m_WindowsVersionInfo.m_iBuildNumber);
		m_WindowsVersionInfo.m_strInfo += buffer;
		
		if( m_WindowsVersionInfo.m_iPlatformID > VER_PLATFORM_WIN32_WINDOWS )
		{
			DWORD dwTimeAdjustment;
			DWORD dwTimeIncrement;
			BOOL bTimeAdjustmentDisabled;
			if( GetSystemTimeAdjustment(&dwTimeAdjustment, &dwTimeIncrement, &bTimeAdjustmentDisabled) == TRUE )
			{
				m_iSystemTimerPeriod = (int)dwTimeIncrement;
			}
		}
		else
		{
			if( m_iCPUCount > 1 )
				m_iSystemTimerPeriod = 156250;	// 这个是测我自己双核机器上的
			else
				m_iSystemTimerPeriod = 100000;  // 这个还没测，大概值
		}
		
		Update();
#endif
	}

	void SystemResourceManager::OnTimer(int iPeriod/*ms*/)
	{
#if (defined WIN7_32)
		static int iTick = 0;
		static __int64 i64CPUCounter;
		if (!m_CPUFrequencyValid)
		{
			iTick++;
			if( iTick == 1 )
			{
				i64CPUCounter = rdtsc();
			}
			else if( iTick > 1  && iTick < 100)
			{
				__int64 i64CPUFrequency = (rdtsc() - i64CPUCounter) * (1000/ iPeriod);
				m_i64CPUFrequency = ((iTick-2)*m_i64CPUFrequency + i64CPUFrequency)/(iTick-1);
				i64CPUCounter = rdtsc();
			}
			else
			{
				m_CPUFrequencyValid = true;
			}

		}		
		Update();
#endif
	}

	// 逻辑 CPU 个数
	int SystemResourceManager::GetCPUCount() const
	{
		return m_iCPUCount;
	}

	// 主频，单位 M
	int SystemResourceManager::GetCPUFrequency() const
	{
#if (defined WIN7_32)
		return (int)((IsCPUFrequencyValid()?m_i64CPUFrequency:m_itmp64CPUFrequency)/1000000);
#else
		return 0;
#endif
	}

	// Windows 版本信息
	void SystemResourceManager::GetWindowsVersionInfo(SystemResourceManager::WindowsVersionInfo &info) const
	{
		info = m_WindowsVersionInfo;
	}

	// 取得系统时钟周期，单位 100纳秒
	int SystemResourceManager::GetSystemTimerPeriod() const
	{
#if (defined WIN7_32)
		return m_iSystemTimerPeriod;
#else
		return 0;
#endif
	}

	// 内存使用信息
	void SystemResourceManager::GetMemoryStatus(SystemResourceManager::MemoryStatus &status) const
	{
#if (defined WIN7_32)
		MEMORYSTATUSEX memstatus = { sizeof(memstatus) };
		::GlobalMemoryStatusEx(&memstatus);

		status.m_iTotalPhys = (int)(memstatus.ullTotalPhys / 1024);
		status.m_iAvailPhys = (int)(memstatus.ullAvailPhys / 1024);
		status.m_iMemoryLoad = (int)memstatus.dwMemoryLoad;
		status.m_iTotalVirtual = (int)(memstatus.ullTotalVirtual / 1024);
		status.m_iAvailVirtual = (int)(memstatus.ullAvailVirtual / 1024);
#endif
	}

	// 更新动态系统资源当前值
	void SystemResourceManager::Update()
	{
#if (defined WIN7_32)
		// 更新可用纹理显存值
		m_iAvailTextureMemory = m_pRenderer->GetAvailableTextureMem();
#endif
	}

	// 可用纹理显存,单位 M
	// @缓存,250ms更新一次
	int SystemResourceManager::GetAvailTextureMemory() const
	{
#if (defined WIN7_32)
		return m_iAvailTextureMemory;
#else
		return 0;
#endif
	}

	bool SystemResourceManager::IsTextureCapsNoPow2() const
	{
#if (defined WIN7_32)
		return m_pRenderer->IsTextureCapsNoPow2();
#else
		return false;
#endif
	}

};