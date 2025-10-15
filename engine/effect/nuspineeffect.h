#ifndef __Nuclear_SPINEEFFECT_H__
#define __Nuclear_SPINEEFFECT_H__

#include "nueffect.h"
#include <spine/spine-cocos2dx.h>
#include "engine/nuspinemanager.h"

namespace Nuclear
{
	class EngineBase;
	class SpineEffectReadTask;
	class SpineEffect : public Effect, public SpineLoadingNotify
	{
	public:
		SpineEffect(EngineBase* pEB, const std::wstring &name);
		virtual ~SpineEffect(void);

		virtual bool Update(DWORD tickTime);

		virtual void Release();

		virtual Nuclear_EffectAssureResResult AssureResource(bool async);

		virtual void SetScaleType(Nuclear_EffectScaleType t) { }
		Nuclear_EffectScaleType GetScaleType() const { return XPEST_NOSCALE; }

		virtual int LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode) { return 0; }

		virtual void SetScreenCrood(bool b) { }
		virtual bool IsScreenCrood() const { return false; }

		virtual void SetVertexColor(NuclearColor color) { }
		virtual NuclearColor GetVertexColor() const { return NuclearColor(0xFFFFFFFF); }
		virtual float GetTransparent() const { return m_alpha; }
		virtual void SetTransparent(float alpha) { m_alpha = alpha; }

		virtual void SetLocation(const NuclearLocation& location);
		virtual void SetScale(float scaleX, float scaleY);
		virtual void SetRotationRadian(float radian);

		virtual void SetPlayMode(NuclearPlayMode mode);
		virtual bool SetLoopNum(int loopNum);

		virtual void SetEffectBindType(Nuclear_EffectBindType t) { }
		virtual Nuclear_EffectBindType GetEffectBindType() const { return XPEBT_NULL; }

		virtual const NuclearVector4& GetColorate() const  { static NuclearVector4 color(1.0f, 1.0f, 1.0f, 1.0f); return color; }

		virtual int	 GetPlayOnceTime() const { return m_totalTime; } //毫秒
		virtual void SetPlayOnceTime(int totalTime) { m_totalTime = totalTime; }

		virtual void Colorate(const NuclearVector4& color) { }
		virtual Nuclear_EffectType GetType() const { return XPET_SPINE; }

		virtual bool GetBase(NuclearBase &base) { return false; }

		virtual const char* GetDefaultActName() { return m_defaultActName.c_str(); }
		virtual void SetDefaultActName(const char* szActName);

		void SetIsUIEffect(bool b);
		bool IsUIEffect() { return m_isUIEffect; }

		virtual bool RenderBottom(Canvas* canvas) { return true; }
		bool Render(Canvas* canvas, bool realRender);


		virtual void SetSkelAnim(spine::SkeletonAnimation* pSkelAnim)
		{
			if (pSkelAnim){
				m_pSkelAnim = pSkelAnim;
				SetScale(m_scale.x, m_scale.y);
			}
		}

		void Reset();

		virtual void OnLoaded(const std::wstring& dir, SpineRes* spineRes);

	private:

		bool loadEffect(const std::wstring &name, bool async);

	private:

		std::wstring			  m_spinename;	//spine特效的名字

		std::string				  m_defaultActName;

		spine::SkeletonAnimation* m_pSkelAnim;
		SpineEffectReadTask* m_pTask;

		float			m_alpha;		//! 透明度

		int				m_totalTime;	//! 播放时间

		bool			m_isUIEffect;	//是否是UI特效
	};
}

#endif