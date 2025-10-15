#ifndef __Nuclear_SYSTEMRESOURCEMANAGER_H__
#define __Nuclear_SYSTEMRESOURCEMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{

	class Renderer;

	// ����ϵͳ��ص���Դ����Ϣ
	// (����ʾ�豸��ص�ת��Renderer����Ϣ)
	class SystemResourceManager
	{
	public:
		// Windows �汾��Ϣ
		// ����Ա������ OSVERSIONINFO ��ͬ, m_strInfo �Ǹ��˿���
		struct WindowsVersionInfo
		{
			int m_iPlatformID;
			int m_iMajorVersion;
			int m_iMinorVersion;
			int m_iBuildNumber;
			std::wstring m_strInfo;
		};

		// �ڴ�ʹ����Ϣ
		struct MemoryStatus
		{
			int m_iTotalPhys;			// �����ڴ�����   ,��λK
			int m_iAvailPhys;			// �����ڴ�����   ,��λK
			int m_iMemoryLoad;			// �����ڴ�ʹ���ʣ���λ%
			int m_iTotalVirtual;		// �����ڴ�����   ,��λK
			int m_iAvailVirtual;		// �����ڴ�����   ,��λK
		};
	
	public:
		//��EngineBase�ع����֮ǰ������ֻҪrenderer��
		SystemResourceManager(Renderer *pRenderer);
		~SystemResourceManager();

	public:
		// ��ʼ����֮��ſ���ʹ��������Ա�ӿ�
		void Init();

		// ���¶�̬ϵͳ��Դ��ǰֵ
		void Update();

		// ��ʱ������Դֵ
		void OnTimer(int iPeriod/*ms*/);

		//  ȡ�þ�̬ϵͳ��Դֵ,�߼� CPU ����
		int GetCPUCount() const;	

		// Windows �汾��Ϣ
		void GetWindowsVersionInfo(WindowsVersionInfo&) const;

		// ϵͳʱ�����ڣ���λ 100ns
		int GetSystemTimerPeriod() const;
	
		//�Կ��Ƿ�֧�ַ�2������true��ʾ֧�ַ�2������
		bool IsTextureCapsNoPow2() const;

		//  ȡ�ö�̬ϵͳ��Դ��ǰֵ, ��Ƶ����λ M @ʵʱ����
		int GetCPUFrequency() const;

		bool IsCPUFrequencyValid() const { return m_CPUFrequencyValid; }

		// �ڴ�ʹ����Ϣ
		// @δ����
		void GetMemoryStatus(MemoryStatus&) const;

		// ���������Դ�,��λ M
		// @����,OnTimer����
		int GetAvailTextureMemory() const;

		// ����
		void SetSoundInited(bool ok) { m_bInitedSound = ok; }
		bool IsSoundInited() const { return m_bInitedSound; }
	
	private:
		int m_iCPUCount;							// �߼� CPU ����
		int64 m_i64CPUFrequency;					// ��Ƶ(�������)
		int64 m_itmp64CPUFrequency;					// ��Ƶ(Get������)
		WindowsVersionInfo m_WindowsVersionInfo;	// Windows �汾��Ϣ
		int m_iSystemTimerPeriod;					// ϵͳʱ������(ò��Ӧ�����л��̵߳���С���ʱ��)
		int m_iAvailTextureMemory;					// ���������Դ棬��λ M
		bool m_CPUFrequencyValid;					// ��Ƶ�ļ����ȶ�����û��
		bool m_bInitedSound;						// �����豸��ʼ���Ƿ�ɹ�

		Renderer *m_pRenderer;					
	
	private:
		SystemResourceManager(const SystemResourceManager&);
		SystemResourceManager& operator=(const SystemResourceManager&);
	};

}

#endif
