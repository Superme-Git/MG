#ifndef __Nuclear_SYSTEMRESOURCEMANAGER_H
#define __Nuclear_SYSTEMRESOURCEMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{

class EngineBase;

// 管理系统相关的资源、信息
// (与显示设备相关的转发Renderer的信息)
class SystemResourceManager
{
public:
	// Windows 版本信息
	// 各成员意义与 OSVERSIONINFO 相同, m_strInfo 是给人看的
	struct WindowsVersionInfo
	{
		int m_iPlatformID;
		int m_iMajorVersion;
		int m_iMinorVersion;
		int m_iBuildNumber;
		std::wstring m_strInfo;
	};

	// 内存使用信息
	struct MemoryStatus
	{
		int m_iTotalPhys;			// 物理内存总量   ,单位K
		int m_iAvailPhys;			// 物理内存余量   ,单位K
		int m_iMemoryLoad;			// 物理内存使用率，单位%
		int m_iTotalVirtual;		// 物理内存总量   ,单位K
		int m_iAvailVirtual;		// 物理内存余量   ,单位K
	};
	
public:
	SystemResourceManager(EngineBase *pEB);
	~SystemResourceManager();

public:
	// 初始化，之后才可用使用其他成员接口
	void Init();

	// 更新动态系统资源当前值
	void Update();

	// 定时更新资源值
	void OnTimer(int iPeriod/*ms*/);

	/*
	/  取得静态系统资源值
	*/

	// 逻辑 CPU 个数
	int GetCPUCount() const;	

	// Windows 版本信息
	void GetWindowsVersionInfo(WindowsVersionInfo&) const;

	// 系统时钟周期，单位 100ns
	int GetSystemTimerPeriod() const;
	
	//显卡是否支持非2幂纹理，true表示支持非2幂纹理
	bool IsTextureCapsNoPow2() const;

	/*
	/  取得动态系统资源当前值
	*/

	// 主频，单位 M
	// @实时计算
	int GetCPUFrequency() const;

	bool IsCPUFrequencyValid() const { return m_CPUFrequencyValid; }

	// 内存使用信息
	// @未缓存
	void GetMemoryStatus(MemoryStatus&) const;

	// 可用纹理显存,单位 M
	// @缓存,OnTimer更新
	int GetAvailTextureMemory() const;

	// 声音
	void SetSoundInited(bool ok) { m_bInitedSound = ok; }
	bool IsSoundInited() const { return m_bInitedSound; }

	std::wstring GetCurDisplayDevice() { return m_CurDisplayDevice.DeviceString; }

private:
	int m_iCPUCount;							// 逻辑 CPU 个数
	__int64 m_i64CPUFrequency;					// 主频(算出来的)
	__int64 m_itmp64CPUFrequency;				// 主频(Get出来的)
	WindowsVersionInfo m_WindowsVersionInfo;	// Windows 版本信息
	int m_iSystemTimerPeriod;					// 系统时钟周期(貌似应该是切换线程的最小间隔时间)
	int m_iAvailTextureMemory;					// 可用纹理显存，单位 M
	bool m_CPUFrequencyValid;					// 主频的计算稳定下来没有
	bool m_bInitedSound;						// 声音设备初始化是否成功

	DISPLAY_DEVICE m_CurDisplayDevice; //显卡信息摘要

private:
	EngineBase *m_pEB;					
	
	// 占坑
private:
	SystemResourceManager(const SystemResourceManager&);
	SystemResourceManager& operator=(const SystemResourceManager&);
};

};

#endif
