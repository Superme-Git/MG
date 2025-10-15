#ifndef __Nuclear_PARTICLEEFFECT_H
#define __Nuclear_PARTICLEEFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "effect.h"
#include "..\particlesystem\iparticlemanager.h"

namespace Nuclear
{
	class ParticleEffect : public IParticleEffect, public Effect, public ParticleLoadingNotify
	{
	//�Զ������͡���Ԫ������
	private:
		friend class EffectManager;

	//���ݳ�Ա��
	private:
		ParticleSystemHandle m_handle;
		PSParameter	m_parmeter;
		int	m_nPlayOnceTime;	//����һ���ʱ�䣨���룩
	
	//��������
	protected:
		ParticleEffect(EngineBase* pEB);
	public:
		virtual ~ParticleEffect(void);

	//������
		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t);
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const { return m_parmeter.m_psModifyParam.m_scaleType; }
		virtual void OnReady(bool success);
		virtual XPEffectAssureResResult AssureResource(bool async);
		virtual void Release();
		const PSParameter& GetCreateParmeter() const { return m_parmeter; }
		virtual bool Update(DWORD tickTime);
		virtual int LoadAttribute(XMLIO::CINode& node);
		virtual int SaveAttribute(XMLIO::CONode& node);
		virtual XPEffectType GetType() const { return XPET_PARTICLE; }

		virtual void SetEffectBindType(XPEFFECT_BIND_TYPE t);
		virtual XPEFFECT_BIND_TYPE GetEffectBindType() const;

		virtual void SetScreenCrood(bool b);
		virtual bool IsScreenCrood() const { return m_parmeter.m_bScreenCoord; }
		virtual void SetLocation(const Location& location);

		//! ��Ⱦ
		virtual bool Render(Canvas* canvas);
		bool CollectRender();//�����ռ��Թ�������

		//! С��0��ʾ����ѭ�������һ����XPPCM_ALWAY_EMISSIONѭ����Ҫ��ΪXPPCM_PULSE_EMISSION���õ���SetCycleMode�ӿ�
		virtual bool		 SetLoopNum( int loopNum);
		virtual bool		 SetPlayTime( DWORD time); //����

		//����ѭ��ģʽ�������XPPCM_NO_CYCLE��return false��
		virtual bool SetCycleMode(XPParticleEffectCycleMode mode);
		virtual XPParticleEffectCycleMode GetCycleMode() const;

		//��������ϵͳ���������ȣ��룩��-1����Ĭ�ϣ�ֻ��XPPCM_PULSE_EMISSION��ʱ��������
		virtual bool SetSysLife(float time);
		virtual float GetSysLife();

		//�������Ч�����ӣ���ô���������������˵㣬type==0��������㣬type==1�����յ�
		//���������Ч�����ӣ���ô����false
		virtual bool SetEmitterLinkPoint(const FPOINT &pt, int type);
		virtual bool GetEmitterLinkPoint(FPOINT &pt, int type);

		virtual bool Play();

		//! ��ò���һ�ε�ʱ��
		virtual int GetPlayOnceTime() const { return m_nPlayOnceTime; }

		virtual bool GetBase(XPBase &base);
		virtual void SetVertexColor( XPCOLOR color);
		virtual void SetTransparent( float alpha);
		//��֧��������ɫ
		virtual void Colorate( const XPVECTOR4& color) {};

		virtual XPCOLOR GetVertexColor() const { return m_parmeter.color; }
		virtual float GetTransparent() const 
		{ 
			XPCOLOR color(m_parmeter.color);
			return static_cast<float>(color.a) / 255.0f;
		}

		virtual const XPVECTOR4& GetColorate() const 
		{ 
			static XPVECTOR4 color(1.0f, 1.0f, 1.0f, 1.0f); 
			return color; 
		}

		virtual const IParticleEffect* TryConvertToParticle() const { return this; }
		virtual IParticleEffect* TryConvertToParticle() { return this; }

		virtual const ParticleEffect* TryConvertToParticleEffect() const { return this; }
		virtual ParticleEffect* TryConvertToParticleEffect() { return this; }

		// ����ϵͳ��������ʱ��Ŀ���
		virtual void ModifyObjectPoint(const FPOINT &pt) { m_parmeter.m_psModifyParam.m_pointObject = pt; }

		//////////////////////////////��������Χ�ı�֮��Ҫ��Ӧ���޸��������ͷ���Ƶ��////////////////////////
		// �߷�����߳���
		virtual void ModifyLineLength(float length) { m_parmeter.m_psModifyParam.m_fLineLength = length; }
		// ���η�������Ŀ��
		virtual void ModifyRectWH(float width, float height)
		{
			m_parmeter.m_psModifyParam.m_fRctWidth = width;
			m_parmeter.m_psModifyParam.m_fRctHeight = height;
		}
		//////////////////////////////��������Χ�ı�֮��Ҫ��Ӧ���޸��������ͷ���Ƶ��////////////////////////

		//����ä������---�������������ӾͲ��ɼ���..һϵ������ڷ��������ĵľ�������
		virtual void SetRctBlindAreas(const std::vector<CRECT> &vet) { m_parmeter.m_psModifyParam.m_vectorRctBlindArea = vet; }
		//�赲��..���Ӳ���Խ������������ڿ��ƶ�������û���ٶȾͻ����Ϊֹͣ...
		virtual void SetBlockLines(const std::vector<LINE>& vet)  { m_parmeter.m_psModifyParam.m_vectorBlockLine = vet; }
		//������...��������֮���չ��߷�����ɸı��˶�����
		virtual void SetReflectLines(const std::vector<LINE>& vet)  { m_parmeter.m_psModifyParam.m_vectorReflectLine = vet; }

		// ���ָ��������Ч��ǰ����Ⱦ��������
		virtual int GetNumActiveParticles() const;
		// ��ð�Χ�е���Ϣ..
		virtual PSINFO GetParticleSystemInfo() const;
		// ���ָ��������Ч�Ĵ��������������޸�
		virtual const PSParameter& GetParticleSystemParameter() const { return m_parmeter; }
		// ���ӵĿ��ܴ�����ʱ��(��)
		virtual float GetParticleMaxLife() const;

		//�����������������Ч������..
		// ��������, �Լ���������u��v����(���Կ��ϴ���û��������˵ķ�2������)
		virtual bool SetSpecialPsTexture(PictureHandle ShapesTexture, float fu=1.0f, float fv=1.0f);
		// ���������Чʹ�õ�����handle (��������ɾ���Ȳ���)
		virtual PictureHandle GetSpecialPSTextureHandle() const;
		// ����������Ч���ű���(Сͼ������Ļ���߷�֮..)
		virtual bool SetSpecialPsScale(float fcx=1.0f, float fcy=1.0f);
		// ת����Ч��Ĭʱ��ftime,��λ:�� �����ʱ���ڱ���ԭʼͼƬ��״̬��
		virtual bool SetSilentTime(float ftime);

		// ת��IEffect
		virtual IEffect* ConvertToIEffect() { return this; }
		virtual const IEffect* ConvertToIEffect() const { return this; }

	};

}

#endif