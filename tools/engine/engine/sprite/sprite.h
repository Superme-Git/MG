#ifndef __Nuclear_SPRITE_H
#define __Nuclear_SPRITE_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xref.h"
#include "..\effect\effect.h"
#include "..\world\entitativeobj.h"
#include "..\isprite.h"
#include "movablespriteimp.h"

namespace Nuclear
{
	class Sebind;
	const static int NORMAL_HORSE_OFFSET = 25;

	typedef std::set<SpriteNotify*> SpriteNotifySet;
	typedef std::map<int, SpriteNotifySet> ActNotifyMap;//һ��������Map
	typedef std::map<std::wstring, ActNotifyMap> FrameNotifyMap; //���ж�����
	struct SortBaseRectType;

	class Sprite : public ISprite, public EntitativeObj
	{
	public:
		struct EffectInfo
		{
			unsigned int flag;
			int dx;
			int dy;
			XHardRef<Effect> effect;
			EffectInfo() : flag(0) {}
			EffectInfo(unsigned int _flag, int _dx, int _dy, Effect* pEffect) :
			flag(_flag), dx(_dx), dy(_dy), effect(pEffect) {}
		};
		friend class SpriteEffectNotify;
	protected:
		enum XPSPPropFlag
		{
			XPSPPF_RENDER_SHADOW = 0x1,			// �Ƿ�Ӱ��
			XPSPPF_HAS_EFFECT_LAYER = 0x2,		// �Ƿ�����Ч��
			XPSPPF_SCREEN_COORD	= 0x4,			// �Ƿ���Ļ����
			XPSPPF_HOLD_LAST_FRAME = 0x8,		// DefaultAction�Ƿ�һֱ�������һ֡
			XPSPPF_DEF_ACTION_ASYNC = 0x10,		// DefaultAction�Ƿ��첽����
			XPSPPF_HAS_DEF_ASYNC_LOADING = 0x20,// �Ƿ������첽DefaultAction
			XPSPPF_CUR_ACTION_ASYNC = 0x40,		// ��ǰAction�Ƿ��첽����
			XPSPPF_HAS_CUR_ASYNC_LOADING = 0x80,// �Ƿ������첽CurAction
			XPSPPF_START_HOLD_LAST_FRAME = 0x100,
			XPSPPF_IS_READING = 0x200,			// �Ƿ��������
			XPSPPF_HAS_SET_STEP_SOUND = 0x400,	// �Ƿ����˽Ų�
			XPSPPF_USE_RENDER_TARGET = 0x800,	// �Ƿ���RenderTarget
			XPSPPF_IS_COVERD_ALPHA = 0x1000,	// �Ƿ����ϲ�İ�͸����ͼ
			XPSPPF_IS_IN_TELEPORT = 0x2000,		// �Ƿ�����˲���ƶ�
		};

		unsigned int m_nPropFlag;		//XPSPPropFlag�ĸ��ֻ�
		int m_iCurFrame;
		int m_iAniDurationTime;
		int m_iDefaultAniDurationTime;
		int m_iAniStartTime;
		int m_nTeleportTime;
		int m_nTeleportBlurTime;
		float m_fScale;

		std::wstring m_soundActorName;
		std::wstring m_strDefaultActionName;
		std::wstring m_strTmpActionName;
		std::wstring m_strBindFile[2];
		XHardRef<Sebind> m_Sebind[2];
		ActionTypeFlag m_actionTypeFlag;
		ActionTypeFlag m_defaultActionTypeFlag;

		typedef std::list<EffectInfo> EffectInfoList;
		typedef std::map<const IEffect*, EffectInfo> EffectInfoMap;
		typedef std::set<SpriteEffectNotify*> SpriteEffectNotifySet;
		EffectInfoList m_lstTmpEffects;
		EffectInfoMap m_mapDuraEffects;

		FrameNotifyMap m_mapFrameNotifys;

		WATER_SURFACE_TYPE m_surfaceType;
		CPOINT m_effectPos;							// ��Ч�ҵ�

		int m_nHorseHeight;							// ��ĸ߶�
		unsigned char m_nAlpha;						// Alpha
		
		
		short	m_nTitlePos;

		
		int m_nTeleportStartTime;					// ˲���ƶ���ʼʱ��
		Location m_TeleportObj;						// ˲���ƶ���Ŀ��
		Location m_TeleportStart;					// ˲���ƶ������
		SpriteEffectNotifySet m_sprEffNotifys;
		std::map<IEffect*, SpriteEffectNotify*> m_sprEffNotMap;


	private:
		MovableSpriteImp *m_pMovableImp;

	public:
		Sprite(EngineBase *pEB);
		virtual ~Sprite();

		virtual void GetBase(SortBaseRectType &base) = 0;

		// ע��/ȡ��Notify
		virtual void RegisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame)
		{
			m_mapFrameNotifys[actionName][frame].insert(pNotify);
		}
		virtual void DeregisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame)
		{
			ActNotifyMap &anm = m_mapFrameNotifys[actionName];
			SpriteNotifySet &sns = anm[frame];
			sns.erase(pNotify);
			if (sns.size() == 0)
			{
				anm.erase(frame);
				if (anm.size() == 0)
				{
					m_mapFrameNotifys.erase(actionName);
				}
			}
		}

		virtual void EnableStepSound(bool b);
		virtual bool IsStepSoundEnable() const { return TestPropFlag(XPSPPF_HAS_SET_STEP_SOUND); }

		virtual void SetScale(float scale);
		virtual float GetScale () const { return m_fScale; }

		//�༭��ʹ��
		virtual void ClearAllNotify()
		{
			SetPropFlag(XPSPPF_HAS_SET_STEP_SOUND, false);
			m_mapFrameNotifys.clear();
		}

		virtual const std::wstring &GetSoundActorName() const { return m_soundActorName; }

		virtual void UseRenderTarget(bool b) { SetPropFlag(XPSPPF_USE_RENDER_TARGET, b); }
		virtual bool IsUseRenderTarget() const { return TestPropFlag(XPSPPF_USE_RENDER_TARGET); }

		// �Ƿ����
		virtual void SetHighlight(bool b) { EntitativeObj::SetHighlight(b); }
		virtual bool IsHighlight() const { return EntitativeObj::IsHighlight(); }

		// ����ܷ�ѡ��ani��ʱ���ģʽ
		virtual void SetMouseTestMode(XPMOUSE_TEST_MODE m) { EntitativeObj::SetMouseTestMode(m); }
		virtual XPMOUSE_TEST_MODE GetMouseTestMode() const { return EntitativeObj::GetMouseTestMode(); }

		// �����Ƿ�ɼ�
		virtual void SetVisible(bool v) { EntitativeObj::SetVisible(v); }
		virtual bool IsVisiable() const { return EntitativeObj::IsVisiable(); }

		// �����Ƿ�ɼ�ʵ�壨���SetVisible(false)�˵Ļ������������ô�趼���ɼ���
		virtual void SetEntityVisible(bool v) { EntitativeObj::SetEntityVisible(v); }
		virtual bool IsEntityVisible() const { return EntitativeObj::IsEntityVisible(); }

		// �����Ƿ�ɼ����֣����SetVisible(false)�˵Ļ������������ô�趼���ɼ���
		virtual void SetTitleVisible(bool v) { EntitativeObj::SetTitleVisible(v); }
		virtual bool IsTitleVisible() const { return EntitativeObj::IsTitleVisible(); }

		// ������鱻�ڵ���ʱ���Ƿ���Ҫ��͸��������ǣ���ô������ȫ��ס��������ľ��飬���Գ��������ľ���Ӧ����Ϊfalse��
		virtual bool IsCoveredAlpha() const { return TestPropFlag(XPSPPF_IS_COVERD_ALPHA); }
		virtual void SetCoveredAlpha(bool b) { SetPropFlag(XPSPPF_IS_COVERD_ALPHA, b); }
		
		// title
		virtual void SetTitle(EntitativeTitleHandle handle, XPTITLE_ALIGN align = XPTA_DEFAULT) { EntitativeObj::SetTitle(handle, align); }
		virtual EntitativeTitleHandle GetTitle() { return EntitativeObj::GetTitle(); }
		virtual EntitativeTitleHandle UnsetTitle()  { return EntitativeObj::UnsetTitle(); }
		virtual void SetTitleSize(int width, int height) { EntitativeObj::SetTitleSize(width, height); }
		virtual int GetTitleWidth() const { return EntitativeObj::GetTitleWidth(); }
		virtual int GetTitleHeight() const { return EntitativeObj::GetTitleHeight(); }

		virtual int GetBubbleItemOffset() const
		{
			return m_nTitlePos+(m_titleAlign==XPTA_CENTER_BOTTOM?GetTitleHeight():0)+(TestPropFlag(XPSPPF_IS_READING)?NORMAL_HORSE_OFFSET+m_nHorseHeight:0);
		}

		// ����Alpha
		virtual void SetAlpha(unsigned char a) { m_nAlpha = a; }
		virtual unsigned char GetAlpha() { return m_nAlpha; }
		// ������Ч�㣨����еĻ�����Alpha������[0~1]
		virtual void SetMinMaxEffectAlpha(float minalpha, float maxalpha) {}
		virtual float GetMinEffectAlpha() const { return 0.0f; }
		virtual float GetMaxEffectAlpha() const { return 1.0f; }

		virtual void SetScreenCoord(bool b) { SetPropFlag(XPSPPF_SCREEN_COORD, b); }
		virtual bool GetScreenCoord() { return TestPropFlag(XPSPPF_SCREEN_COORD); }


		virtual void SetRideName(const std::wstring& name);

		//���ð��ļ��������ó�L""��ʱ��Ϊȡ����ֻ����0����1
		virtual bool SetBindFile(int id, const std::wstring &bindFile);
		virtual std::wstring GetBindFile(int id) {
			if (id < 0 || id > 1)
				return L"";
			return m_strBindFile[id-1]; 
		}
		virtual IEffect* SetDurativeEffect(const std::wstring &effect_name, const CPOINT& relpos, unsigned int flag);
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const CPOINT& relpos, unsigned int flag);
		virtual void RemoveDurativeEffect(IEffect* pEffect);
		virtual void PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype);

		virtual void Tick(DWORD ticktime);

		virtual ActionTypeFlag GetCurrentActionType() { return m_actionTypeFlag; }

		int GetCurFrame() { return m_iCurFrame; }
	private:
	
		void UpdateEffect(DWORD tickTime);
		void RenderEffectFront(Canvas* canvas);
		void RenderEffectBack(Canvas* canvas);
		
		virtual void RenderSprite(Canvas* canvas) = 0;
	public:

		int GetAllEffectCount() const
		{
			return (int)(m_mapDuraEffects.size() + m_lstTmpEffects.size());
		}

		void GetAllEffectDetail(std::map<std::wstring, int> &detail) const
		{
			for(EffectInfoMap::const_iterator it = m_mapDuraEffects.begin(); it != m_mapDuraEffects.end(); ++it)
			{
				detail[it->second.effect->GetName()] += 1;
			}
			for(std::list<EffectInfo>::const_iterator it = m_lstTmpEffects.begin(); it != m_lstTmpEffects.end(); ++it)
			{
				detail[it->effect->GetName()] += 1;
			}
		}
		
		

	private:		
		virtual void RenderEntity(Canvas* canvas)
		{
			m_pMovableImp->RenderPath();
			RenderEffectBack(canvas);
			RenderSprite(canvas);		
			RenderEffectFront(canvas);
		}
		virtual void ResetHorseHeight(const std::wstring& ridename) { m_nHorseHeight = 0; }
	protected:
		virtual Location GetTitleLocation() const { return m_pMovableImp->getLocation()-Location(0, m_nTitlePos+(TestPropFlag(XPSPPF_IS_READING)?m_nHorseHeight+NORMAL_HORSE_OFFSET:0)); }//��˵�ʱ��title�̶�����ƫ��25������
		virtual void ResetEffectPos() { m_effectPos.x = m_effectPos.y = 0;}
		virtual void ResetStepAction(const std::wstring& ridename) { m_soundActorName = L""; }
		inline bool TestPropFlag(XPSPPropFlag flag) const { return (m_nPropFlag & flag) != 0; }
		inline void SetPropFlag(XPSPPropFlag flag, bool b) { if (b) m_nPropFlag |= flag; else m_nPropFlag &= ~flag; }

	public:

		virtual void UpdateAction() = 0;
		///////////////////////////////////////////////////////////////////////
		// ʵ�� ISprite �ӿ�

		// ����֧��4������8�������У�4���������� ���� ���� ����
		virtual void SetOnly4Dir(bool b) { m_pMovableImp->SetOnly4Dir(b); }
		virtual bool IsOnly4Dir() const { return m_pMovableImp->IsOnly4Dir(); }

		virtual void SetLocation(const Location& location) { m_pMovableImp->setLocation(location); }
		virtual void SetDirection( XPDIRECTION direction ) { m_pMovableImp->setDirection(direction); }
		virtual void SetDirection(int tx, int ty)          { m_pMovableImp->SetDirection(tx, ty); }

		// ��ñ�����Ч�ҵ��λ�ã�����������꣩
		virtual const CPOINT& GetEffectPos() const { return m_effectPos; }

		virtual bool TestViewport(const CRECT &viewport) const = 0;

		virtual Location GetLocation() const               { return m_pMovableImp->getLocation(); }
		virtual XPDIRECTION GetDirection() const           { return m_pMovableImp->getDirection(); }

		virtual Location GetLogicLocation() const                { return m_pMovableImp->getLogicLocation(); }
		//�⼸��Ŀǰ��û����
		//virtual void SetLogicLocation(float x, float y)	         { m_pMovableImp->setLogicLocation( x, y); }
		//virtual void SetLogicLocation(const Location & location) { m_pMovableImp->setLogicLocation( location); }
		//virtual void SetLogicDirection(int tx, int ty)           { m_pMovableImp->setLogicDirection( tx, ty); }
		virtual void SetLocationDirection(const astar::Point & point)
		{
			m_pMovableImp->SetLocationDirection(point);
		}

		virtual void  SetMoveSpeed(float speed) { m_pMovableImp->SetMoveSpeed(speed); }
		virtual float GetMoveSpeed() const      { return m_pMovableImp->GetMoveSpeed(); }
		virtual void  StopMove() { return m_pMovableImp->StopMove(); }
		virtual bool  IsMoving() const          { return m_pMovableImp->IsMoving(); }
		virtual void SetMoveSuspended(bool b)   { m_pMovableImp->SetMoveSuspended(b); }
		virtual bool IsMoveSuspended() const    { return m_pMovableImp->IsMoveSuspended(); }

		virtual void  MoveTo(astar::Path & trail) { m_pMovableImp->MoveTo(trail); } // �߼����� 
		virtual void  MoveTo(int targetX, int targetY, int range, const CSIZE * size) // ��������
		{
			m_pMovableImp->MoveTo(targetX, targetY, range, size);
		}

		// ��ȡ��ǰpath��һ������, ��ʹ��MoveTo����ÿɵõ�����Ҫ�ߵ�·��(�߼�����)
		virtual void GetPath(astar::Path & path) { this->m_pMovableImp->GetPath( path); }

		virtual void KeepMoveTrail(bool keep) { m_pMovableImp->KeepMoveTrail(keep); }
		virtual bool IsKeepMoveTrail() const        { return m_pMovableImp->IsKeepMoveTrail(); }
		virtual void SetMoveMask(unsigned int mask) { m_pMovableImp->SetMoveMask(mask); }
		virtual bool TakeMoveTrail(astar::Path & trail /*out*/) // �߼�����
		{
			return m_pMovableImp->TakeMoveTrail(trail);
		}

		virtual bool InAttackRange( const Nuclear::Location & target, int range, unsigned int mask) // ��������
		{
			return m_pMovableImp->InAttackRange(target, range, mask);
		}

		// ��ǰ�����Ĳ�����֡����������UndateAction��ʱ����£���UndateActionǰΪ��һ֡�����ݣ�UndateAction��Ϊ��֡�����ݣ�
		virtual int GetCurrentFrame() const { return m_iCurFrame; }

		// �ƶ���ડ�����Ϊ��һ�����������ʱ��by world
		void tick(int now, int deltaTime);
	};

};

#endif
