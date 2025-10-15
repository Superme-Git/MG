#ifndef __Nuclear_IPARTICLEMANAGER_H__
#define __Nuclear_IPARTICLEMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuiparticlesyscommon.h"

namespace Nuclear
{
	class ParticleLoadingNotify
	{
	public:
		virtual void OnReady(bool success) = 0;
	};

	struct PSL;

	class IParticleManager
	{	
	public:
		IParticleManager() { }
		virtual ~IParticleManager() { }

		// ����viewport,����Ϸ�ӿڷ����仯ʱ����
		virtual void OnViewportUpdate(const NuclearRect &viewport) = 0;
		// ͨ��psl�ļ���(���Բ�����չ��)����һ��������Ч
		// bLoadResΪtrue, ��ʾ������Ч��ʱ����Ҫ��������Դһ�����
		// bLookupSegmPak...������ЧǰҪ�Ȳ�һ�����psl��״̬, �������������, ��ô������ʱ��, ���ֵ��false;
		// ��������Ӱ�, ��ô����true; �������û����������, �Ͳ��ܴ��������Ч;
		virtual ParticleSystemHandle CreateParticleSystem(const std::wstring& pslName, const PSParameter& param, bool bLoadRes, bool bLookupSegmPak) = 0;
		// ɾ��һ������ϵͳ(��Ӧ��Դ�����ü�����һ)
		virtual bool DeleteParticleSystem(ParticleSystemHandle handle) = 0;
	
		// �ͷ�һ������ϵͳ��������Դ(��Ӧ��Դ�����ü�����һ)������Ϊ���������Ӵ洢�ռ�
		virtual bool ReleaseParticleSystemRes(ParticleSystemHandle handle) = 0;

		// ��������ϵͳϵͳ����, todo��
		virtual bool ResetParticleSystem(ParticleSystemHandle handle) = 0;
		// �޸�����ϵͳ����, ����λ�õ�...
		virtual bool ModifyParticleSystem(ParticleSystemHandle handle, const PSModifyParam& param) = 0;
		// bUpdatedΪtrue��ʾrender֮ǰ�Ѿ����ù�update��, ����false��ʾ��ѭ����Чһ�鲥��..
		virtual bool RenderParticleSystem(ParticleSystemHandle handle, bool bUpdated = false, bool bEndScene = true) = 0;
		// ��Ⱦָ���������������Ч
		virtual bool RenderParticleSystemByLayer(int layer) = 0;
		// ��õ�ǰ���ص�����������Ч����
		virtual int GetParticleSystemCount() const = 0;
		// ��õ�ǰ��Ⱦ������������
		virtual int GetTotalNumActiveParticles() const = 0;
		// ���ָ��������Ч��ǰ����Ⱦ��������
		virtual int GetNumActiveParticles(ParticleSystemHandle handle) const = 0;
		// ��ð�Χ�е���Ϣ..
		virtual PSINFO GetParticleSystemInfo(ParticleSystemHandle handle) const = 0;
		// ���ָ��������Ч�Ĵ������� //ֻ����ͨ��ModifyParticleSystem()�޸�PSParameter
		virtual const PSParameter* GetParticleSystemParameter(ParticleSystemHandle handle) const = 0;
		// �������ϵͳϵͳ����(��psl�л�ȡ)
		virtual float GetSysLife(ParticleSystemHandle handle) const = 0;
		// ���ӵĿ��ܴ�����ʱ��(��)
		virtual float GetParticleMaxLife(ParticleSystemHandle handle) const = 0;
		//�ڴ�������Ч���ǲ���֮ǰ�����޸�...
		virtual bool ModifyParticleSystem(ParticleSystemHandle handle, const PSParameter& param) = 0;
		// ÿ֡����, ������������ӵ�λ����ɫ��״̬. fTimeDelta��λ����
		virtual bool UpdateParticleSystem(ParticleSystemHandle handle, float fTimeDelta) = 0;
	
		// ������Ч�Ƿ�ʹ�þ��������õĹҵ�(��psl�л�ȡ)�������ʹ��,Ĭ����Ч���ھ���Ľ��ϣ�
		virtual Nuclear_EffectBindType GetEffectBindType(ParticleSystemHandle handle) const = 0;

		// ���������Դ�Ƿ��Ѿ����ؽ�����... Ĭ��false... ��ʾͬ����ȡ��Դ
		virtual Nuclear_EffectAssureResResult EnsureParticleSystemResouce(ParticleSystemHandle handle, bool async = false, ParticleLoadingNotify* pNotify = NULL) = 0;

		//�����������������Ч������..
		// ��������, �Լ���������u��v����(���Կ��ϴ���û��������˵ķ�2������)
		virtual bool SetSpecialPsTexture(ParticleSystemHandle handle, PictureHandle ShapesTexture, float fu=1.0f, float fv=1.0f) = 0;
		// ���������Чʹ�õ�����handle (��������ɾ���Ȳ���)
		virtual PictureHandle GetSpecialPSTextureHandle(ParticleSystemHandle handle) const = 0;
		// ����������Ч���ű���(Сͼ������Ļ���߷�֮..)
		virtual bool SetSpecialPsScale( ParticleSystemHandle handle, float fcx=1.0f, float fcy=1.0f) = 0;
		// ת����Ч��Ĭʱ��ftime,��λ:�� �����ʱ���ڱ���ԭʼͼƬ��״̬��
		virtual bool SetSilentTime(ParticleSystemHandle handle, float ftime) = 0;

		//�����������ϵͳ����nMaxSize�׵���Դ, ����֮��..���һ��ʹ����fTime����ǰ�ľ��ͷŵ�
		virtual void GarbageCollect() = 0;
		//fMaxSize��λM��nTime��λ����
		virtual void SetGCParam(float fMaxSize, float fTime) = 0;
		// ����ϵͳ·��...ͨ����������ڴ�������ϵͳ��ʱ������ҵ���ȷ����Դ·��
		virtual void SetParticlePath(const std::wstring& pslPath) = 0;
		// ��ó�����Ϣ. Ŀǰ�����ڴ�����Ч��ʱ��Ը�����Դ���ش����м���Ϣ��¼
		virtual PSL_ERRORTYPE GetLastError() const = 0;
		// ���û�������--//��2 ��1 ��0
		virtual void SetSystemConfig(NuclearComputerConfigType nParam) = 0;
		// ��ͣ��Ч��Ⱦ
		virtual void SetStopFlag(ParticleSystemHandle handle, bool bStop) = 0;
		// ����notify
		virtual bool InsertParticleNotify(ParticleSystemHandle handle, IParticleNotify* pParticleNotify) = 0;
		// �Ƴ�notify
		virtual bool RemoveParticleNotify(ParticleSystemHandle handle, IParticleNotify* pParticleNotify) = 0;
	
		virtual void SetLogPath(const std::wstring& logPath) = 0;

		virtual bool SetKeyPointIndex(ParticleSystemHandle handle, int nIndex) = 0;

		virtual bool OnResetDevice() = 0;

		virtual bool LoadPSLFromMem(const NuclearBuffer& data, PSL* ppsl) = 0;

		virtual bool Enabled() = 0;
		virtual void SetEnabled(bool flag) = 0;

	private:
		IParticleManager(const IParticleManager&);
		IParticleManager& operator=(const IParticleManager&);
	};

}

#endif