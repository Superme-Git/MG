#ifndef __Nuclear_SPRITE_H__
#define __Nuclear_SPRITE_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxref.h"
#include "../effect/nueffect.h"
#include "../world/nuentitativeobj.h"
#include "../nuisprite.h"
#include "numovablespriteimp.h"
#include "nurenderablesprite.h"

namespace GM
{
	class GSprite;
}

namespace Nuclear
{
	class Sebind;
    class SpriteEffectNotify;
	typedef std::set<SpriteNotify*> SpriteNotifySet;
	typedef std::map<int, SpriteNotifySet> ActNotifyMap;//一个动作的Map
	typedef std::map<std::wstring, ActNotifyMap> FrameNotifyMap; //所有动作的
	struct NuclearSortBaseRectType;

	class Sprite : public ISprite, public EntitativeObj
	{
		struct EffectInfo
		{
			unsigned int flag;
			int dx;
			int dy;
			NuclearHardRef<Effect> effect;
			EffectInfo() : flag(0) {}
			EffectInfo(unsigned int _flag, int _dx, int _dy, Effect* pEffect) :
				flag(_flag), dx(_dx), dy(_dy), effect(pEffect) {}
			EffectInfo(unsigned int _flag, int _dx, int _dy) :
				flag(_flag), dx(_dx), dy(_dy) {}
		};
		friend class SpriteEffectNotify;
		friend class ComponentSprite;

	protected:
		enum XPSPPropFlag
		{
			XPSPPF_RENDER_SHADOW = 0x1,			// 是否画影子
			XPSPPF_HAS_EFFECT_LAYER = 0x2,		// 是否有特效层
			XPSPPF_SCREEN_COORD = 0x4,			// 是否屏幕坐标
			XPSPPF_HOLD_LAST_FRAME = 0x8,		// DefaultAction是否一直保持最后一帧
			XPSPPF_DEF_ACTION_ASYNC = 0x10,		// DefaultAction是否异步加载
			XPSPPF_HAS_DEF_ASYNC_LOADING = 0x20,// 是否正在异步DefaultAction
			XPSPPF_CUR_ACTION_ASYNC = 0x40,		// 当前Action是否异步加载
			XPSPPF_HAS_CUR_ASYNC_LOADING = 0x80,// 是否正在异步CurAction
			XPSPPF_START_HOLD_LAST_FRAME = 0x100,
			XPSPPF_IS_READING = 0x200,			// 是否正在骑乘
			XPSPPF_HAS_SET_STEP_SOUND = 0x400,	// 是否设了脚步
			XPSPPF_IS_SOLID_MASK = 0x800,		// 是不是先画一遍实的，再画一遍虚的
			XPSPPF_IS_IN_TELEPORT = 0x1000,		// 是否正在瞬间移动
			XPSPPF_IS_USE_SOCKET_TITLE = 0x2000,// title是否绑在挂点上
			XPSPPF_IS_WATER_ERROR = 0x80000000,// 位置错误
		};

		typedef struct
		{
			NuclearPoint loc;
			int tick;
		} TeleportBlurFrame;

		typedef std::list<TeleportBlurFrame> TeleportBlurList;

	public:
		Sprite(EngineBase *pEB, const std::wstring &modelname);
		virtual ~Sprite();
		virtual NuclearSpriteType GetType() { return m_pRenderableSprite ? m_pRenderableSprite->GetType() : XPSPRITETYPE_NULL; }

		virtual void DumpAllActionNames(std::vector<std::wstring> &actions) const
		{
			actions.clear();
			if (m_pRenderableSprite)
				m_pRenderableSprite->DumpAllActionNames(actions);
		}

		void Update3DPos() { if (m_pRenderableSprite) m_pRenderableSprite->Update3DPos(); }
		virtual void GetBase(NuclearSortBaseRectType &base) { if (m_pRenderableSprite) m_pRenderableSprite->GetBase(base); }
		void SetActive(bool active) { if (m_pRenderableSprite) m_pRenderableSprite->SetActive(active); }
		//3D角色是否每帧更新，主角以及队伍中角色应该每帧更新
		virtual bool IsUpdateEveryFrame() const { return m_pRenderableSprite ? m_pRenderableSprite->IsUpdateEveryFrame() : false; }
		virtual void SetUpdateEveryFrame(bool b) { if (m_pRenderableSprite) m_pRenderableSprite->SetUpdateEveryFrame(b); }

		// 设置精灵模型
		virtual bool SetModel(const std::wstring &modelname, bool async);

		// 返回精灵模型的名字
		virtual const std::wstring& GetModelName() const { return m_modelName; }

		// 设置精灵的装备组件
		virtual bool SetComponent(int scid, const std::wstring& resource, NuclearColor color = 0xffffffff/*XRGB*/, std::wstring wsLayerName = L"");
		virtual bool GetComponent(int scid, std::wstring& resource) { return m_pRenderableSprite && m_pRenderableSprite->GetComponent(scid, resource); }
		virtual void* GetComponent(int scid) { return m_pRenderableSprite->GetComponent(scid); }
		virtual bool GetComponentColor(int scid, NuclearColor &color) { return m_pRenderableSprite && m_pRenderableSprite->GetComponentColor(scid, color); }

		virtual void SetPartParam(int iLayerIndex, int iPartIndex, std::vector<float> vParam) { if (m_pRenderableSprite)  m_pRenderableSprite->SetPartParam(iLayerIndex, iPartIndex, vParam); }
		virtual std::vector<float> GetPartParam(int iLayerIndex, int iPartIndex)
		{
			if (m_pRenderableSprite)
			{
				return m_pRenderableSprite->GetPartParam(iLayerIndex, iPartIndex);
			}
			std::vector<float> vResult;
			return vResult;
		}
		virtual int GetDyePartCount()
		{
			if (m_pRenderableSprite != NULL) return m_pRenderableSprite->GetDyePartCount();
			return 0;
		}
		virtual int GetDyeProjCount(int part)
		{
			if (m_pRenderableSprite != NULL) return m_pRenderableSprite->GetDyeProjCount(part);
			return 0;
		}
		virtual void SetDyePartIndex(int part, int index)
		{
			if (m_pRenderableSprite != NULL)
			{
				if (m_pRenderableSprite->GetDyeEnable() == false)	return;
				int iLayerIndex = GetLayerIndexByName(L"body");
				m_pRenderableSprite->SetDyePartIndex(iLayerIndex, part, index);
			}
		}
		virtual bool GetDyeEnable()
		{
			if (m_pRenderableSprite != NULL) return m_pRenderableSprite->GetDyeEnable();
			return false;
		}

		// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
		virtual NuclearPrefetchResult PrefetchAction(const std::wstring &action_name){ return m_pRenderableSprite ? m_pRenderableSprite->PrefetchAction(action_name) : XPPR_ASYNC_LOADING; }

		// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
		virtual void HoldAction(const std::wstring &action_name) { if (m_pRenderableSprite) m_pRenderableSprite->HoldAction(action_name); }
		// 只有Hold住的Action才需要Release
		virtual void ReleaseAction(const std::wstring &action_name) { if (m_pRenderableSprite) m_pRenderableSprite->ReleaseAction(action_name); }

		virtual void SetHoldLastFrame(bool bHoldLastFrame);

		// 默认动作，每个精灵必须有一个默认动作
		virtual bool SetDefaultAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame);
		virtual const std::wstring &GetDefaultAction() const { return m_strDefaultActionName; }
		// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
		// 返回true代表真的播放了，返回false代表播的其实是上一个动作，目前的还没有下载好呢
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime);
		virtual const std::wstring &GetCurActionName() const { return m_strTmpActionName; }
		void PlayDefaultAction();

		// 单位是秒，如果没有这个动作，返回false
		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time) { return m_pRenderableSprite && m_pRenderableSprite->GetActionTimeByName(action_name, time); }

		// 当前动作的播到哪帧（此数据在UpdateAction的时候更新，在UpdateAction前为上一帧的数据，UpdateAction后为本帧的数据）
		int GetCurFrame() { return m_iCurFrame; }

		// 当前动作的总帧数
		virtual int GetTotalFrame() const { return m_pRenderableSprite ? m_pRenderableSprite->GetTotalFrame() : 0; }

		virtual void EnableShadow(bool b) { SetPropFlag(XPSPPF_RENDER_SHADOW, b); }
		virtual bool IsEnableShadow() { return TestPropFlag(XPSPPF_RENDER_SHADOW); }

		// 精灵瞬移拖残影
		//telTime是移动的时间，blurTime是残影持续的时间，总的时间是telTime+blurTime，在telTime之后就已经移动到位
		virtual void TeleportWithBlur(const NuclearLocation &point, int telTime, int blurTime);
		virtual void StopTeleport(const NuclearLocation &point);

		//动作声音
		virtual void SetActionSoundPriority(short priority) { } 
		virtual short GetActionSoundPriority() const { return 0; }

		// 名字暂定，为了拾取准备的接口, r为检测半径
		virtual bool TestPoint(const NuclearLocation &testloc, int r, float testval)
		{
			if (TestTitleAlpha(testloc, r)) return true;
			return m_pRenderableSprite && m_pRenderableSprite->TestPoint(testloc, r, testval);
		}

		// 注册/取消Notify
		virtual void RegisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame){m_mapFrameNotifys[actionName][frame].insert(pNotify);}
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

		// 注册渲染名称的回调函数
		virtual void RegisterDrawNameCB(DrawName_CallBack drawNameCB) { mDrawNameCB = drawNameCB; }
		virtual void DeregisterDrawNameCB() { mDrawNameCB = NULL; }

		virtual void EnableStepSound(bool b);
		virtual bool IsStepSoundEnable() const { return TestPropFlag(XPSPPF_HAS_SET_STEP_SOUND); }

		virtual void SetScale(float scale);
		virtual float GetScale () const { return m_fScale; }
        virtual void SetComponentAniReleaseFlag(Nuclear::NuclearPaniReleaseFlag flag);

		// 获取 XModel 中的缩放值
		virtual float GetModelScale() const;

		//编辑器使用
		virtual void ClearAllNotify()
		{
			SetPropFlag(XPSPPF_HAS_SET_STEP_SOUND, false);
			m_mapFrameNotifys.clear();
		}

		virtual const std::wstring &GetSoundActorName() const { return m_soundActorName; }

		// 是否高亮
		virtual void SetHighlight(bool b) 
		{
			EntitativeObj::SetHighlight(b); 
			if (m_pRenderableSprite) 
				m_pRenderableSprite->OnHighlight(b); 
		}
		virtual bool IsHighlight() const { return EntitativeObj::IsHighlight(); }

		// 检查能否选择ani的时候的模式
		virtual void SetMouseTestMode(Nuclear_Mouse_TestMode m) { EntitativeObj::SetMouseTestMode(m); }
		virtual Nuclear_Mouse_TestMode GetMouseTestMode() const { return EntitativeObj::GetMouseTestMode(); }

		// 设置是否可见
		virtual void SetVisible(bool v) 
		{ 
			EntitativeObj::SetVisible(v); 
			if (m_pRenderableSprite)
				m_pRenderableSprite->OnSetVisible();
		}

		virtual bool IsVisiable() const { return EntitativeObj::IsVisiable(); }

		// 设置是否可见实体（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetEntityVisible(bool v) 
		{ 
			EntitativeObj::SetEntityVisible(v); 
			if (m_pRenderableSprite)
				m_pRenderableSprite->OnSetVisible();
		}
		virtual bool IsEntityVisible() const { return EntitativeObj::IsEntityVisible(); }

		// 设置是否可见名字（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetTitleVisible(bool v) { EntitativeObj::SetTitleVisible(v); }
		virtual bool IsTitleVisible() const { return EntitativeObj::IsTitleVisible(); }

		//是否实体，例如建筑。开启这个会导致精灵绘制两遍，有一定的性能下降
		virtual bool IsSolidMask() const { return TestPropFlag(XPSPPF_IS_SOLID_MASK); }
		virtual void SetSolidMask(bool b) { SetPropFlag(XPSPPF_IS_SOLID_MASK, b); }

		// title
		virtual void SetTitle(EntitativeTitleHandle handle, NuclearTitleAlign align = XPTA_DEFAULT) { EntitativeObj::SetTitle(handle, align); }
		virtual EntitativeTitleHandle GetTitle() { return EntitativeObj::GetTitle(); }
		virtual EntitativeTitleHandle UnsetTitle()  { return EntitativeObj::UnsetTitle(); }
		virtual void SetTitleSize(int width, int height) { EntitativeObj::SetTitleSize(width, height); }
		virtual int GetTitleWidth() const { return EntitativeObj::GetTitleWidth(); }
		virtual int GetTitleHeight() const { return EntitativeObj::GetTitleHeight(); }

		virtual int GetBubbleItemOffset() const
		{
			int iResult = m_nTitlePos;
			iResult += (m_titleAlign == XPTA_CENTER_BOTTOM ? GetTitleHeight() : 0);
			iResult += (TestPropFlag(XPSPPF_IS_READING) ? m_nHorseHeight : 0);
			iResult += m_iFlyOffsetYCur;
			iResult *= m_iFlyScaleCur / 10000.0f;
			return iResult;
		}

		// 设置Alpha
		virtual void SetAlpha(unsigned char a)
		{ 
			m_nAlpha = a; 
			if (m_pRenderableSprite)
				m_pRenderableSprite->OnSetAlpha(a);
		}
		virtual unsigned char GetAlpha() const { return m_nAlpha; }
		// 精灵特效层（如果有的话）的Alpha上下限[0~1]
		virtual void SetMinMaxEffectAlpha(float minalpha, float maxalpha) {}
		virtual float GetMinEffectAlpha() const { return 0.0f; }
		virtual float GetMaxEffectAlpha() const { return 1.0f; }

		virtual void SetScreenCoord(bool b) { SetPropFlag(XPSPPF_SCREEN_COORD, b); }
		virtual bool GetScreenCoord() { return TestPropFlag(XPSPPF_SCREEN_COORD); }

		virtual void SetRideName(const std::wstring& name);

		virtual int GetLayerCount();
		virtual int GetLayerIndexByName(std::wstring wsName);
		virtual std::wstring GetLayerNameByIndex(int iIndex);
		virtual bool IsPartLayer(int iIndex);

		//设置绑定文件，当设置成L""的时候为取消，只能是0或者1
		virtual bool SetBindFile(int id, const std::wstring &bindFile);
		virtual std::wstring GetBindFile(int id)
		{
			if (id < 0 || id > 1) return L"";
			return m_strBindFile[id-1]; 
		}
		virtual IEffect* SetDurativeEffect(const std::wstring &effect_name, const NuclearPoint& relpos, unsigned int flag);
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const NuclearPoint& relpos, unsigned int flag);
		virtual void RemoveDurativeEffect(IEffect* pEffect);
		virtual IEffect* PlayEffect(const std::wstring &effect_name, const NuclearPoint& relpos, int times, const std::wstring &hostname, bool async);
		virtual IEffect* PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype, int TimeMode = 0, int iRot = 0);//0:LoopNum,1:Time

		virtual bool ChangeEffectOffset(IEffect* pEffect, const NuclearPoint &relpos);

		virtual void Update(DWORD ticktime);
		virtual void UpdateAction(DWORD ticktime);	// yeqing 2015-11-07

		virtual ActionTypeFlag GetCurrentActionType() { return m_actionTypeFlag; }

	public:

		int GetAllEffectCount() const { return (int)(m_mapDuraEffects.size() + m_lstTmpEffects.size()); }

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

		virtual void RenderBeforeMid(bool realRender)
		{ 
			if ((m_nVisible & XP_VIS_VIS) && (m_nVisible & XP_VIS_ENTITY))//是需要画实体
			{
				renderEntity(NULL, false, realRender);
			}
		}

		//当半透明方案是XPSTT_SPRITE_TRANSLUCENT的时候，用来绘制半透明精灵
		virtual void RenderSpriteOnly(bool realRender)
		{
			//不是实体
			if (/*!TestPropFlag(XPSPPF_IS_SOLID_MASK) &&*/ m_pRenderableSprite &&
				(m_nVisible & XP_VIS_VIS) && (m_nVisible & XP_VIS_ENTITY))
			{
				m_pRenderableSprite->RenderSprite(FALSE, NULL, NULL, false, m_bShaking, realRender);
			}
		}

	protected:

		virtual void UpdateEffect(DWORD tickTime);
		virtual void RenderEffectFront(Canvas* canvas, bool realRender);
		virtual void RenderEffectBack(Canvas* canvas, bool realRender);

		void UpdateEffectPosWithDir(EffectInfo& info);

	private:
		
		virtual void RenderEntityBottom(Canvas* canvas);
		virtual void renderEntity(Canvas* canvas, bool isAlpha, bool realRender);
		virtual void RenderEntity(Canvas* canvas, bool realRender)
		{
			if (TestPropFlag(XPSPPF_IS_SOLID_MASK))	//画了实体了
			{
				//画虚的了
				renderEntity(canvas, true, realRender);
			}
			else
			{
				renderEntity(canvas, false, realRender);
			}
		}
		virtual void ResetHorseHeight(const std::wstring& ridename) { m_nHorseHeight = 0; }

		virtual void removeEffect(IEffect* effect);

	protected:
		virtual NuclearLocation GetTitleLocation() const { return m_pMovableImp->getLocation() - NuclearLocation(0, (m_nTitlePos + (TestPropFlag(Sprite::XPSPPF_IS_READING) ? m_nHorseHeight : 0)) * m_fScale); }
		virtual void ResetEffectPos() { m_effectPos.x = m_effectPos.y = 0;}
		inline bool TestPropFlag(XPSPPropFlag flag) const { return (m_nPropFlag & flag) != 0; }
		inline void SetPropFlag(XPSPPropFlag flag, bool b) { if (b) m_nPropFlag |= flag; else m_nPropFlag &= ~flag; }

	public:
		using ISprite::SetDirection;

		// 如果是L""，代表在原来的在原点基础上偏移的方法
		virtual void SetTitleBindSocket(const std::wstring &socket)
		{
			SetPropFlag(XPSPPF_IS_USE_SOCKET_TITLE, socket.size() > 0);
			m_titleSocket = socket;
		}
		virtual std::wstring GetTitleBindSocket() const { return m_titleSocket; }

		// 精灵支持的方向，有8，4，2，1四种方向
		virtual void SetTurnDir(int iDir) { m_pMovableImp->SetTurnDir(iDir); }// 默认是8方向
		virtual int  GetTurnDir() const { return m_pMovableImp->GetTurnDir(); }

		virtual void SetLocation(const NuclearLocation& location) { m_pMovableImp->setLocation(location); }
		virtual void SetDirection( NuclearDirection direction ) { m_pMovableImp->setDirection(direction); }
		virtual void SetDirection(int tx, int ty)          { m_pMovableImp->SetDirection(tx, ty); }

		// 获得被击特效挂点的位置（相对像素坐标）
		virtual const NuclearPoint& GetEffectPos() const { return m_effectPos; }

		virtual bool TestViewport(const NuclearRect &viewport) const 
		{
			if (viewport.PtInRect(GetLocation()))
				return true;
			return m_pRenderableSprite && m_pRenderableSprite->TestViewport(viewport);
		}

		virtual NuclearLocation GetLocation() const               { return m_pMovableImp->getLocation(); }
		virtual NuclearDirection GetDirection() const           { return m_pMovableImp->getDirection(); }
		virtual NuclearFPoint GetVectorDirection() const		   { return m_pMovableImp->getVectorDirection(); }

		virtual NuclearLocation GetLogicLocation() const                { return m_pMovableImp->getLogicLocation(); }
		virtual void	 SetLocationDirection(const astar::Point & point) { m_pMovableImp->SetLocationDirection(point); }

		virtual void  SetMoveSpeed(float speed) { m_pMovableImp->SetMoveSpeed(speed); }
		virtual float GetMoveSpeed() const      { return m_pMovableImp->GetMoveSpeed(); }
		virtual void  StopMove() { return m_pMovableImp->StopMove(); }
		virtual bool  IsMoving() const          { return m_pMovableImp->IsMoving(); }
		virtual void SetMoveSuspended(bool b)   { m_pMovableImp->SetMoveSuspended(b); }
		virtual bool IsMoveSuspended() const    { return m_pMovableImp->IsMoveSuspended(); }

		virtual void  MoveTo(astar::Path & trail) { m_pMovableImp->MoveTo(trail); } // 逻辑坐标 
		virtual void  MoveTo(int targetX, int targetY, int range, const CSIZE * size, bool straight = false) // 世界坐标
		{
			m_pMovableImp->MoveTo(targetX, targetY, range, size, straight);
		}

		virtual void  StartShake();	// 开始抖动 
		virtual void  StopShake();	// 结束抖动
		
		virtual void SetCurveMove(bool b);//移动中是否需要走弧形

		// 获取当前path的一个拷贝, 在使用MoveTo后调用可得到即将要走的路径(逻辑坐标)
		virtual void GetPath(astar::Path & path) { this->m_pMovableImp->GetPath( path); }

		virtual void KeepMoveTrail(bool keep) { m_pMovableImp->KeepMoveTrail(keep); }
		virtual bool IsKeepMoveTrail() const        { return m_pMovableImp->IsKeepMoveTrail(); }
		virtual void SetMoveMask(unsigned int mask) { m_pMovableImp->SetMoveMask(mask); }
		virtual bool TakeMoveTrail(astar::Path & trail /*out*/)  { return m_pMovableImp->TakeMoveTrail(trail); }// 逻辑坐标

		virtual bool InAttackRange( const Nuclear::NuclearLocation & target, int range, unsigned int mask) {return m_pMovableImp->InAttackRange(target, range, mask);}// 世界坐标

		// 当前动作的播到哪帧（此数据在UndateAction的时候更新，在UndateAction前为上一帧的数据，UndateAction后为本帧的数据）
		virtual int GetCurrentFrame() const { return m_iCurFrame; }

		// 移动嘀嗒。参数为上一次嘀嗒依赖的时间差。by world
		void tick(int now, int deltaTime);

		void IncreaseUpdateTick() { ++m_nUpdateTick; }
		int GetUpdateTick() const { return m_nUpdateTick; }
		void ResetUpdateTick() { m_nUpdateTick = 0; }

	protected:
		unsigned int m_nPropFlag;		//XPSPPropFlag的各种或
		///////这个要搬到componentSprite里面
		int m_iCurFrame;
		int m_iAniDurationTime;
		int m_iDefaultAniDurationTime;
		int m_iAniStartTime;
		int m_nHorseHeight;							// 马的高度
		float m_fHorseScale;							// 马的缩放
		short	m_nTitlePos;
		FrameNotifyMap m_mapFrameNotifys;
		//////////////////////////////////////////

		int m_nTeleportTime;
		int m_nTeleportBlurTime;
		float m_fSetScale;  // 外界设置的缩放值
		float m_fScale;  // 最终的缩放值

		std::wstring m_titleSocket;
		std::wstring m_modelName;		//模型名
		std::wstring m_soundActorName;
		std::wstring m_strDefaultActionName;
		std::wstring m_strTmpActionName;
		std::wstring m_strBindFile[2];
		NuclearHardRef<Sebind> m_Sebind[2];
		ActionTypeFlag m_actionTypeFlag;
		ActionTypeFlag m_defaultActionTypeFlag;

		typedef std::list<EffectInfo> EffectInfoList;
		typedef std::map<const IEffect*, EffectInfo> EffectInfoMap;
		typedef std::set<SpriteEffectNotify*> SpriteEffectNotifySet;
		EffectInfoList m_lstTmpEffects;
		EffectInfoMap m_mapDuraEffects;

		NuclearWaterSurfaceType m_surfaceType;
		NuclearPoint m_effectPos;							// 特效挂点

		unsigned char m_nUpdateTick;
		unsigned char m_nAlpha;						// Alpha
		int m_nTeleportStartTime;					// 瞬间移动开始时间
		NuclearLocation m_TeleportObj;						// 瞬间移动的目标
		NuclearLocation m_TeleportStart;					// 瞬间移动的起点
		TeleportBlurList m_blurList;				// 当打开击退、冲锋残影时，残影的位置和时间
		SpriteEffectNotifySet m_sprEffNotifys;
		std::map<IEffect*, SpriteEffectNotify*> m_sprEffNotMap;

		MovableSpriteImp *m_pMovableImp;
		RenderableSprite *m_pRenderableSprite;

		DrawName_CallBack	mDrawNameCB;

		bool m_bShaking;	// 是否进行抖动

		bool m_bRideAniIsReady;

		int m_iFlyScaleCur;//因飞行而引起的缩放当前值
		int m_iFlyScaleTgt;//因飞行而引起的缩放目标值
		int m_iFlyScaleStep;//因为飞行而引起的缩放的步伐值
		int m_iFlyOffsetYCur;//因飞行而引起的Y轴偏移(实际上是模拟Z轴)当前值
		int m_iFlyOffsetYTgt;//因飞行而引起的Y轴偏移(实际上是模拟Z轴)目标值
		int m_iFlyOffsetYStep;//因飞行而引起的Y轴偏移(实际上是模拟Z轴)步伐值
		int m_iUseWorldScale;//1使用世界缩放,0不使用

		void* mpUserData;

	public:

		virtual void SetFlyScaleTgt(int iFlyScaleTgt);
		virtual void SetFlyScaleCur(int iFlyScaleCur);
		virtual int GetFlyScaleCur();
		virtual void SetFlyScaleStep(int iFlyScaleStep);
		virtual int GetFlyScaleStep();
		virtual void SetFlyOffsetYTgt(int iFlyOffsetYTgt);
		virtual void SetFlyOffsetYCur(int iFlyOffsetYCur);
		virtual int GetFlyOffsetYCur();
		virtual void SetFlyOffsetYStep(int iFlyOffsetYStep);
		virtual int GetFlyOffsetYStep();

		virtual void SetUseWorldScale(int iUseWorldScale);
		virtual int GetUseWorldScale();

		virtual void* GetUserData() const { return mpUserData; }
		virtual void SetUserData(void* pData) { mpUserData = pData; }
	};

};

#endif
