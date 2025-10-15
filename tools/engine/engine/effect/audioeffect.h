#ifndef __Nuclear_AUDIOEFFECT_H
#define __Nuclear_AUDIOEFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif
#include "effect.h"
#include "..\sound\directsoundmanager.h"

namespace Nuclear
{

	class AudioEffect :
		public Effect, public ISoundNotify
	{
	//自定义类型、友元、常量
	private:
		friend class EffectManager;

	public:
		enum SOUND_PROP
		{
			SP_MAX_VOL = 0,
			SP_MIN_VOL,
			SP_MAX_FREQ,
			SP_MIN_FREQ,
		};
		enum 
		{
			SOUND_PROP_COUNT = SP_MIN_FREQ + 1,
		};
	//数据成员们
	private:
		SoundHandle m_handle;
		unsigned char m_nSoundProp[SOUND_PROP_COUNT];//maxV minV maxF minF
		short m_priority;		//声音优先级
		unsigned char m_type;	//声音类型
		bool m_bNoPlay;

	public:
		AudioEffect(EngineBase* pEB);
		virtual ~AudioEffect(void);

		virtual void SetScreenCrood(bool b) {}
		virtual bool IsScreenCrood() const { return false; }
		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t) {  }
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const { return XPEST_NOSCALE; }
		virtual XPEffectAssureResResult AssureResource(bool async);
		virtual void Release() { m_state = XPES_EMPTY; }
		virtual bool Update(DWORD tickTime);
		virtual int LoadAttribute(XMLIO::CINode& node);
		virtual int SaveAttribute(XMLIO::CONode& node);
		virtual bool Render(Canvas* canvas);
		virtual XPCOLOR GetVertexColor() const { return 0; }
		virtual float GetTransparent() const { return 0.0f; }
		virtual const XPVECTOR4& GetColorate() const;
		virtual void SetVertexColor( XPCOLOR color) {}
		virtual void SetTransparent( float alpha) {}
		virtual void Colorate( const XPVECTOR4& color) {}
		virtual XPEffectType GetType() const { return XPET_AUDIO; }
		virtual int	GetPlayOnceTime() const; //毫秒
		virtual bool GetBase(XPBase &base) { return true; }
		virtual void OnSoundPlayEnd(SoundHandle handle) override;
		virtual bool Stop();
		void SetSoundProp(SOUND_PROP type, unsigned char value) { m_nSoundProp[type] = value; }
		unsigned char GetSoundProp(SOUND_PROP type) const { return m_nSoundProp[type]; }
		virtual void SetEffectBindType(XPEFFECT_BIND_TYPE t) {}
		virtual XPEFFECT_BIND_TYPE GetEffectBindType() const { return XPEBT_NULL; }

		// 如果特效里面有声音，声音的类型和优先级。注意：要在声音播放出来之前设置，否则无效
		virtual void SetSoundType(unsigned char type) { m_type = type; }
		virtual unsigned char GetSoundType() const { return m_type; }
		virtual void SetSoundPriority(short priority) { m_priority = priority; }
		virtual short GetSoundPriority() const { return m_priority; }
		virtual void RemoveAudio() { m_bNoPlay = true; } //这个特效不播放声音
	};

}
#endif	//__Nuclear_AUDIOEFFECT_H
