#ifndef __Nuclear_EFFECT_H
#define __Nuclear_EFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\ieffect.h"
#include "..\particlesystem\iparticlesyscommon.h"
#include "..\engine\renderable.h"

//Ҳ����˵�������һ��8�������Ч������ÿ������Ч��˵���ͻ���7*8�����ߣ���ĺ͵��������Ч��û��������
//ȫ�ֵ�λ�ú���ɫ�͸�������һ����

//����Ȼ�һ�¡�������

namespace XMLIO
{
	class CINode;
	class CONode;
}

namespace Nuclear
{
	const static int MAX_EFFECT_RADIUS = 128;

	class EffectManager;
	class ParticleEffect;

	enum
	{
		EFFECT_IO_NORMAL			= 0x00000000,		//! �����汾
		EFFECT_IO_EDIT				= 0x00000001,		//! �༭���汾
		EFFECT_IO_ALONE_AUDIO_FILE	= 0x00000002,		//! �������������ļ�
	};

	//Effect �����Clip��¼����֡����������ʱ������������֡�������п��ܻ���֡��
	//Ҳ����˵����ֻ�޸�fps��ʱ���ܲ���ʱ�䲻�䣬��Ч����֡���ᷢ���仯
	class Effect : public IEffect, public Renderable
	{
	//�Զ������͡���Ԫ������
	private:
		 friend class EffectManager;
		 typedef std::set<IEffectNotify*> NotifySet;
	public:
		
	//������
	protected:
		CRECT			m_relBBox;			//! ��԰�Χ��
		std::wstring	m_name;				//! ��Ч����
		XPPLAY_MODE		m_playMode;			//! ���ŷ�ʽ
		union{
			int			m_nPlayTime;		//! �ܹ����ŵ�ʱ��
			int 		m_nLoopNum;			//! ѭ������
		};
											//  ���m_playMode��XPPM_LOOP����ôm_nLoopNum��-1��
											//  �����XPPM_LOOPNUM����ôӦ����m_nLoopNum
											//  �����XPPM_TIME����ô��m_nTotalTime

		// �й�����ʱ����
		Location		m_pos;				//! λ�ã������͸���֮���λ�ã��п�����GEffect��Ҳ�п��������磩
		FPOINT			m_translate;		//! ������������������ϵ�λ�ã��ܿ��ܻ��ǲ����������ģ�
		FPOINT			m_scale;			//! ����
		float			m_rotationRadian;	//! ��ת
		XPDIRECTION		m_dir;				//! ��Ч�ķ���
		XPEffectState	m_state;			//! ״̬
		int				m_curLoopNum;		//! ��ǰѭ������
		int				m_nCurTime;			//! ��ÿ��update��ʱ���ۻ����������룩
		NotifySet		m_SetPNotifies;		//! �ص�ͨ��

	//������

	protected:
		Effect(EngineBase* pEB) : Renderable(pEB), m_scale(1.0f, 1.0f), m_rotationRadian(0.0f),
			m_state(XPES_EMPTY) {}	//ֻ���� EffectManager������
	public:
		virtual ~Effect() 
		{
			NotifyDelete();
		}

	public:	//�ϲ�����ò���Effect�������Ծ�public��
		virtual XPEffectAssureResResult AssureResource(bool async) = 0;
		virtual void Release() = 0;
		//virtual void Refresh() = 0;	//�༭��ʹ��

		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t) = 0;
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const = 0;

		virtual void AddNotify(IEffectNotify* pNotify) { m_SetPNotifies.insert(pNotify); }
		virtual void RemoveNotify(IEffectNotify* pNotify) { m_SetPNotifies.erase(pNotify); }
		virtual void ClearNotify() { m_SetPNotifies.clear(); }

		virtual void HoldRes() {}

		//effect create������ʱ��ǰʱ���Ǹ��ģ�update��ʱ��������ֵ�ǰʱ��Ϊ��������0��update�����Ҹĵ�ǰʱ��Ϊ0
		virtual bool Update(DWORD tickTime) = 0;	//����false�ʹ����������
		virtual int LoadAttribute(XMLIO::CINode& node) = 0;
		virtual int SaveAttribute(XMLIO::CONode& node) = 0;

		//����״̬����
		virtual bool Play();
		virtual bool Stop();
		virtual bool Pause();
		virtual bool Resume();
		//! ��Ⱦ
		virtual bool Render(Canvas* canvas) = 0;
		//! ��Ⱦǰ�ˣ�ǰ�˺���Ⱦ��Ҳ����˵�ܵ�ס�ˣ�
		virtual void RenderFront(Canvas* canvas) { Render(canvas); }	//�ڷ�GEffect�������棬����ǰ��������ϣ���ô�Ͳ������Render����������ô���Ϳ϶��ܵ�ס��
		//! ��Ⱦ��ˣ��������Ⱦ��Ҳ����˵�ܱ��˵�ס��
		virtual void RenderBack(Canvas* canvas) {}
		
		virtual XPEffectState GetPlayState() const { return m_state; }
		const std::wstring &GetName() const { return m_name; }

		virtual void SetScreenCrood(bool b) = 0;
		virtual bool IsScreenCrood() const = 0;

		virtual const CRECT& GetRelBouningBox() const { return m_relBBox; }

		virtual XPDIRECTION GetDirection() const { return m_dir; }
		Location GetLocation() const { return m_pos; }
		const FPOINT&	GetTranslation() const { return m_translate; }
		virtual const FPOINT&	GetScale() const { return m_scale; }
		virtual float GetRotationRadian() const { return m_rotationRadian; }
		virtual XPCOLOR GetVertexColor() const = 0;
		virtual float GetTransparent() const = 0;
		virtual const XPVECTOR4& GetColorate() const = 0;
		
		virtual void SetDirection( XPDIRECTION direction ) { m_dir = direction; }
		void SetLocation(const Location& location) { m_pos = location; }
		void SetLocation(int x, int y) { m_pos.x = x; m_pos.y = y;}
		void SetTranslation(const FPOINT& trans) { m_translate = trans; }
		virtual void SetScale( float scale)	{ m_scale.x = m_scale.y = scale; }
		virtual void SetScale( float scaleX, float scaleY) { m_scale.x = scaleX; m_scale.y = scaleY; }
		virtual void SetScale( const FPOINT& scale) { m_scale = scale; }
		virtual void SetRotationRadian( float radian) { m_rotationRadian = radian; }
		virtual void SetVertexColor( XPCOLOR color) = 0;
		virtual void SetTransparent( float alpha) = 0;
		virtual void Colorate( const XPVECTOR4& color) = 0;
		
		//void SetScreenCrood( bool screen) { }

		virtual XPEffectType GetType() const = 0;
		virtual XPPLAY_MODE	 GetPlayMode() const { return m_playMode; }
		virtual int			 GetLoopNum() const 
		{ 
			if ( m_playMode == XPPM_LOOPNUM)
				return m_nLoopNum;
			else if ( m_playMode == XPPM_LOOP)
				return -1;
			else
				return 0;
		}
		virtual int			 GetPlayTime() const 
		{ 
			if ( m_playMode == XPPM_LOOPNUM)
				return GetPlayOnceTime() * m_nLoopNum;
			else if ( m_playMode == XPPM_LOOP)
				return -1;
			else
				return m_nPlayTime;
		}
		//! С��0��ʾ����ѭ��
		virtual bool		 SetLoopNum( int loopNum);
		virtual bool		 SetPlayTime( DWORD time); //����
		//! �����ϵͳ�еļ���
		//virtual int		GetSystemLevel() const { return m_systemLevel; }
		//! ����ϵͳ���� 0 ���� 1 �еȿ��� 2 ����, Ĭ��Ϊ������
		//virtual void	SetSystemLevel( int level) { m_systemLevel = level; }

		virtual int			 GetPlayOnceTime() const = 0; //����
		virtual bool		 GetBase(XPBase &base) = 0;

		virtual const IParticleEffect* TryConvertToParticle() const { return NULL; }
		virtual IParticleEffect* TryConvertToParticle() { return NULL; }

		virtual const ParticleEffect* TryConvertToParticleEffect() const { return NULL; }
		virtual ParticleEffect* TryConvertToParticleEffect() { return NULL; }

		// �����Ч���������������������ͺ����ȼ���ע�⣺Ҫ���������ų���֮ǰ���ã�������Ч
		virtual void SetSoundType(unsigned char type) {}
		virtual unsigned char GetSoundType() const { return 0; }
		virtual void SetSoundPriority(short priority) {}
		virtual short GetSoundPriority() const { return 0; }
		virtual void RemoveAudio() {} //�����Ч����������
	protected:
		bool IsInfitLoop() const { return m_playMode == XPPM_LOOP; }
		bool IsLoopNum() const { return m_playMode == XPPM_LOOPNUM; }
		bool IsPlayTime() const { return m_playMode == XPPM_TIME; }
		
		void NotifyAsyncLoadResult(bool succ);
		void NotifyStop();
		void NotifyEnd();
		void NotifyDelete();
	};

};

#endif