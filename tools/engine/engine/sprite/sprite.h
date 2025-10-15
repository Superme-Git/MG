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
	typedef std::map<int, SpriteNotifySet> ActNotifyMap;//一个动作的Map
	typedef std::map<std::wstring, ActNotifyMap> FrameNotifyMap; //所有动作的
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
			XPSPPF_RENDER_SHADOW = 0x1,			// 是否画影子
			XPSPPF_HAS_EFFECT_LAYER = 0x2,		// 是否有特效层
			XPSPPF_SCREEN_COORD	= 0x4,			// 是否屏幕坐标
			XPSPPF_HOLD_LAST_FRAME = 0x8,		// DefaultAction是否一直保持最后一帧
			XPSPPF_DEF_ACTION_ASYNC = 0x10,		// DefaultAction是否异步加载
			XPSPPF_HAS_DEF_ASYNC_LOADING = 0x20,// 是否正在异步DefaultAction
			XPSPPF_CUR_ACTION_ASYNC = 0x40,		// 当前Action是否异步加载
			XPSPPF_HAS_CUR_ASYNC_LOADING = 0x80,// 是否正在异步CurAction
			XPSPPF_START_HOLD_LAST_FRAME = 0x100,
			XPSPPF_IS_READING = 0x200,			// 是否正在骑乘
			XPSPPF_HAS_SET_STEP_SOUND = 0x400,	// 是否设了脚步
			XPSPPF_USE_RENDER_TARGET = 0x800,	// 是否用RenderTarget
			XPSPPF_IS_COVERD_ALPHA = 0x1000,	// 是否画最上层的半透明贴图
			XPSPPF_IS_IN_TELEPORT = 0x2000,		// 是否正在瞬间移动
		};

		unsigned int m_nPropFlag;		//XPSPPropFlag的各种或
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
		CPOINT m_effectPos;							// 特效挂点

		int m_nHorseHeight;							// 马的高度
		unsigned char m_nAlpha;						// Alpha
		
		
		short	m_nTitlePos;

		
		int m_nTeleportStartTime;					// 瞬间移动开始时间
		Location m_TeleportObj;						// 瞬间移动的目标
		Location m_TeleportStart;					// 瞬间移动的起点
		SpriteEffectNotifySet m_sprEffNotifys;
		std::map<IEffect*, SpriteEffectNotify*> m_sprEffNotMap;


	private:
		MovableSpriteImp *m_pMovableImp;

	public:
		Sprite(EngineBase *pEB);
		virtual ~Sprite();

		virtual void GetBase(SortBaseRectType &base) = 0;

		// 注册/取消Notify
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

		//编辑器使用
		virtual void ClearAllNotify()
		{
			SetPropFlag(XPSPPF_HAS_SET_STEP_SOUND, false);
			m_mapFrameNotifys.clear();
		}

		virtual const std::wstring &GetSoundActorName() const { return m_soundActorName; }

		virtual void UseRenderTarget(bool b) { SetPropFlag(XPSPPF_USE_RENDER_TARGET, b); }
		virtual bool IsUseRenderTarget() const { return TestPropFlag(XPSPPF_USE_RENDER_TARGET); }

		// 是否高亮
		virtual void SetHighlight(bool b) { EntitativeObj::SetHighlight(b); }
		virtual bool IsHighlight() const { return EntitativeObj::IsHighlight(); }

		// 检查能否选择ani的时候的模式
		virtual void SetMouseTestMode(XPMOUSE_TEST_MODE m) { EntitativeObj::SetMouseTestMode(m); }
		virtual XPMOUSE_TEST_MODE GetMouseTestMode() const { return EntitativeObj::GetMouseTestMode(); }

		// 设置是否可见
		virtual void SetVisible(bool v) { EntitativeObj::SetVisible(v); }
		virtual bool IsVisiable() const { return EntitativeObj::IsVisiable(); }

		// 设置是否可见实体（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetEntityVisible(bool v) { EntitativeObj::SetEntityVisible(v); }
		virtual bool IsEntityVisible() const { return EntitativeObj::IsEntityVisible(); }

		// 设置是否可见名字（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetTitleVisible(bool v) { EntitativeObj::SetTitleVisible(v); }
		virtual bool IsTitleVisible() const { return EntitativeObj::IsTitleVisible(); }

		// 这个精灵被遮挡的时候是否需要半透明，如果是，那么它会完全挡住在它后面的精灵，所以长得像建筑的精灵应该设为false）
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

		// 设置Alpha
		virtual void SetAlpha(unsigned char a) { m_nAlpha = a; }
		virtual unsigned char GetAlpha() { return m_nAlpha; }
		// 精灵特效层（如果有的话）的Alpha上下限[0~1]
		virtual void SetMinMaxEffectAlpha(float minalpha, float maxalpha) {}
		virtual float GetMinEffectAlpha() const { return 0.0f; }
		virtual float GetMaxEffectAlpha() const { return 1.0f; }

		virtual void SetScreenCoord(bool b) { SetPropFlag(XPSPPF_SCREEN_COORD, b); }
		virtual bool GetScreenCoord() { return TestPropFlag(XPSPPF_SCREEN_COORD); }


		virtual void SetRideName(const std::wstring& name);

		//设置绑定文件，当设置成L""的时候为取消，只能是0或者1
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
		virtual Location GetTitleLocation() const { return m_pMovableImp->getLocation()-Location(0, m_nTitlePos+(TestPropFlag(XPSPPF_IS_READING)?m_nHorseHeight+NORMAL_HORSE_OFFSET:0)); }//骑乘的时候，title固定向上偏移25个像素
		virtual void ResetEffectPos() { m_effectPos.x = m_effectPos.y = 0;}
		virtual void ResetStepAction(const std::wstring& ridename) { m_soundActorName = L""; }
		inline bool TestPropFlag(XPSPPropFlag flag) const { return (m_nPropFlag & flag) != 0; }
		inline void SetPropFlag(XPSPPropFlag flag, bool b) { if (b) m_nPropFlag |= flag; else m_nPropFlag &= ~flag; }

	public:

		virtual void UpdateAction() = 0;
		///////////////////////////////////////////////////////////////////////
		// 实现 ISprite 接口

		// 精灵支持4方向还是8方向，其中，4方向是左上 右上 右下 左下
		virtual void SetOnly4Dir(bool b) { m_pMovableImp->SetOnly4Dir(b); }
		virtual bool IsOnly4Dir() const { return m_pMovableImp->IsOnly4Dir(); }

		virtual void SetLocation(const Location& location) { m_pMovableImp->setLocation(location); }
		virtual void SetDirection( XPDIRECTION direction ) { m_pMovableImp->setDirection(direction); }
		virtual void SetDirection(int tx, int ty)          { m_pMovableImp->SetDirection(tx, ty); }

		// 获得被击特效挂点的位置（相对像素坐标）
		virtual const CPOINT& GetEffectPos() const { return m_effectPos; }

		virtual bool TestViewport(const CRECT &viewport) const = 0;

		virtual Location GetLocation() const               { return m_pMovableImp->getLocation(); }
		virtual XPDIRECTION GetDirection() const           { return m_pMovableImp->getDirection(); }

		virtual Location GetLogicLocation() const                { return m_pMovableImp->getLogicLocation(); }
		//这几个目前还没用上
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

		virtual void  MoveTo(astar::Path & trail) { m_pMovableImp->MoveTo(trail); } // 逻辑坐标 
		virtual void  MoveTo(int targetX, int targetY, int range, const CSIZE * size) // 世界坐标
		{
			m_pMovableImp->MoveTo(targetX, targetY, range, size);
		}

		// 获取当前path的一个拷贝, 在使用MoveTo后调用可得到即将要走的路径(逻辑坐标)
		virtual void GetPath(astar::Path & path) { this->m_pMovableImp->GetPath( path); }

		virtual void KeepMoveTrail(bool keep) { m_pMovableImp->KeepMoveTrail(keep); }
		virtual bool IsKeepMoveTrail() const        { return m_pMovableImp->IsKeepMoveTrail(); }
		virtual void SetMoveMask(unsigned int mask) { m_pMovableImp->SetMoveMask(mask); }
		virtual bool TakeMoveTrail(astar::Path & trail /*out*/) // 逻辑坐标
		{
			return m_pMovableImp->TakeMoveTrail(trail);
		}

		virtual bool InAttackRange( const Nuclear::Location & target, int range, unsigned int mask) // 世界坐标
		{
			return m_pMovableImp->InAttackRange(target, range, mask);
		}

		// 当前动作的播到哪帧（此数据在UndateAction的时候更新，在UndateAction前为上一帧的数据，UndateAction后为本帧的数据）
		virtual int GetCurrentFrame() const { return m_iCurFrame; }

		// 移动嘀嗒。参数为上一次嘀嗒依赖的时间差。by world
		void tick(int now, int deltaTime);
	};

};

#endif
