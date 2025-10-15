#ifndef __Nuclear_IEFFECT_H
#define __Nuclear_IEFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif
#include <set>
#include "common\xptypes.h"
#include "particlesystem\iparticlesyscommon.h"

namespace Nuclear
{
	class IEffect;
	typedef std::set<IEffect*> IEffectSet;

	class IEffectNotify
	{
	public:
		virtual void OnStop(IEffect *pEffect) {}
		virtual void OnEnd(IEffect *pEffect) {}
		virtual void OnDelete(IEffect *pEffect) {}	//����ͻ��ֶ���world������sprite����remove����Ч������ص����������
		virtual void OnAsyncLoaded(IEffect *pEffect, bool succeeded) {}//�п��ܼ���ʧ�ܣ��ļ����󣬻����Ҳ�����Դ��
		virtual ~IEffectNotify() {}
	};
	class IParticleEffect;

	class IEffect
	{
	public:
		IEffect(){}
		virtual ~IEffect() { }

		//����״̬����
		virtual bool Play() = 0;
		virtual bool Stop() = 0;
		virtual bool Pause() = 0;
		virtual bool Resume() = 0;

		// ����λ�á�location �����������ꡣ
		virtual void SetLocation(const Location& location) = 0;
		// �õ�λ��.
		virtual Location GetLocation() const = 0;

		// ���ú͵õ�����
		virtual void SetDirection( XPDIRECTION direction ) = 0;
		virtual XPDIRECTION GetDirection() const = 0;

		// ��ת
		virtual void SetRotationRadian( float radian) = 0;
		virtual float GetRotationRadian() const = 0;

		// ����
		virtual void SetScale( float scale)	= 0;
		virtual void SetScale( float scaleX, float scaleY) = 0;
		virtual void SetScale( const FPOINT& scale) = 0;
		virtual const FPOINT& GetScale() const = 0;

		// ��ɫ
		virtual void SetVertexColor( XPCOLOR color) = 0;
		virtual XPCOLOR GetVertexColor() const = 0;

		// ��ð�Χ��
		virtual const CRECT& GetRelBouningBox() const = 0;

		// ��Ч�ҵ�
		virtual void SetEffectBindType(XPEFFECT_BIND_TYPE t) = 0;
		virtual XPEFFECT_BIND_TYPE GetEffectBindType() const = 0;

		// �����Ч���������������������ͺ����ȼ���ע�⣺Ҫ���������ų���֮ǰ���ã�������Ч
		virtual void SetSoundType(unsigned char type) = 0;
		virtual unsigned char GetSoundType() const = 0;
		virtual void SetSoundPriority(short priority) = 0;
		virtual short GetSoundPriority() const = 0;

		// ���״̬
		virtual XPEffectState GetPlayState() const = 0;

		virtual const IParticleEffect* TryConvertToParticle() const = 0;
		virtual IParticleEffect* TryConvertToParticle() = 0;

		// Notify
		virtual void AddNotify(IEffectNotify* pNotify) = 0;
		virtual void RemoveNotify(IEffectNotify* pNotify) = 0;
		virtual void ClearNotify() = 0;//����������ڲ�ʹ�ã������ⲻҪ����

		// Update������������ǲ���Ҫ���õģ�����㲻֪���ò��õ��ã���ô�Ͳ��õ��á�����- -b
		// ֻ����ĳЩ���������£�����û��ͳһUpdate��Ч��ʱ������ͬ��Loading������Ҫ����
		virtual bool Update(DWORD tickTime) = 0;	//����false�ʹ����������
	};
	enum XPParticleEffectCycleMode
	{
		XPPCM_NO_CYCLE = 0,			//���ֵ����Set���������ѭ��ģʽ������GetCycleMode�᷵�����ֵ
		XPPCM_ALWAY_EMISSION = -1,	//��������Զ�ڷ���
		XPPCM_PULSE_EMISSION = -2,	//�����������趨����������֮���ֹͣ���䣬����������֮�������·���
	};

	class IParticleEffect
	{
	public:
		// ����ϵͳ��������ʱ��Ŀ���
		virtual void ModifyObjectPoint(const FPOINT &pt) = 0;
		
		//////////////////////////////��������Χ�ı�֮��Ҫ��Ӧ���޸��������ͷ���Ƶ��////////////////////////
		// �߷�����߳���
		virtual void ModifyLineLength(float length) = 0;
		// ���η�������Ŀ��
		virtual void ModifyRectWH(float width, float height) = 0;
		//////////////////////////////��������Χ�ı�֮��Ҫ��Ӧ���޸��������ͷ���Ƶ��////////////////////////

		//����ä������---�������������ӾͲ��ɼ���..һϵ������ڷ��������ĵľ�������
		virtual void SetRctBlindAreas(const std::vector<CRECT> &vet) = 0;
		//�赲��..���Ӳ���Խ������������ڿ��ƶ�������û���ٶȾͻ����Ϊֹͣ...
		virtual void SetBlockLines(const std::vector<LINE>& vet) = 0;
		//������...��������֮���չ��߷�����ɸı��˶�����
		virtual void SetReflectLines(const std::vector<LINE>& vet) = 0;

		//����ѭ��ģʽ�������XPPCM_NO_CYCLE��return false�����˷���������SysLife
		virtual bool SetCycleMode(XPParticleEffectCycleMode mode) = 0;
		virtual XPParticleEffectCycleMode GetCycleMode() const = 0;

		//��������ϵͳ���������ȣ��룩��-1����Ĭ�ϣ�ֻ��XPPCM_PULSE_EMISSION��ʱ��������
		virtual bool SetSysLife(float time) = 0;
		virtual float GetSysLife() = 0;

		//�������Ч�����ӣ���ô���������������˵㣬type==0��������㣬type==1�����յ�
		//���������Ч�����ӣ���ô����false
		virtual bool SetEmitterLinkPoint(const FPOINT &pt, int type) = 0;
		virtual bool GetEmitterLinkPoint(FPOINT &pt, int type) = 0;

		// ���ָ��������Ч��ǰ����Ⱦ��������
		virtual int GetNumActiveParticles() const = 0;
		// ��ð�Χ�е���Ϣ..
		virtual PSINFO GetParticleSystemInfo() const = 0;
		// ���ָ��������Ч�Ĵ��������������޸�
		virtual const PSParameter &GetParticleSystemParameter() const = 0;
		// ���ӵĿ��ܴ�����ʱ��(��)
		virtual float GetParticleMaxLife() const = 0;


		//�����������������Ч������..
		// ��������, �Լ���������u��v����(���Կ��ϴ���û��������˵ķ�2������)
		virtual bool SetSpecialPsTexture(PictureHandle ShapesTexture, float fu=1.0f, float fv=1.0f) = 0;
		// ���������Чʹ�õ�����handle (��������ɾ���Ȳ���)
		virtual PictureHandle GetSpecialPSTextureHandle() const = 0;
		// ����������Ч���ű���(Сͼ������Ļ���߷�֮..)
		virtual bool SetSpecialPsScale(float fcx=1.0f, float fcy=1.0f) = 0;
		// ת����Ч��Ĭʱ��ftime,��λ:�� �����ʱ���ڱ���ԭʼͼƬ��״̬��
		virtual bool SetSilentTime(float ftime) = 0;

		// ת��IEffect
		virtual IEffect* ConvertToIEffect() = 0;
		virtual const IEffect* ConvertToIEffect() const = 0;
	};


// 	class IFontEffect
// 	{
// 	public:
// 		IFontEffect() {}
// 		virtual ~IFontEffect() {}
// 
// 	};


}

#endif