#ifndef __Nuclear_C3DSprite_H
#define __Nuclear_C3DSprite_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xref.h"
#include "..\isprite.h"
#include "..\ieffect.h"
#include "renderablesprite.h"

namespace GM
{
	class GSprite;
	class ISprite;
}

namespace Nuclear
{
	class Sprite;
	class Canvas;
	class C3DSpriteNotify;
	class C3DEffect;
	class C3DSprite : public RenderableSprite, public IEffectNotify
	{
		friend class C3DSpriteNotify;
	public:
		C3DSprite(Sprite* pLogicSprite);
		~C3DSprite(void);

	public:
		virtual GM::GSprite* GetGSprite() { return m_pGSprite; }
		virtual XPSPRITE_TYPE GetType() { return XPSPRITETYPE_3D; }

		virtual void GetBase(SortBaseRectType &base);

		virtual void UpdateAction(DWORD ticktime);
		virtual void Update3DPos();
		virtual bool TestViewport(const NuclearRect &viewport) const { return true; }
		virtual void SetActive(bool active);
		virtual bool IsUpdateEveryFrame() const { return m_bAlwaysUpdate; }
		virtual void SetUpdateEveryFrame(bool b);
		
		// 设置精灵模型
		virtual bool SetModel(const std::wstring &modelname, bool async);
		
		// 设置精灵的装备组件
		virtual bool SetComponent(int scid, const std::wstring& resource, uFireCOLOR color=0xffffffff/*XRGB*/);


		virtual bool GetComponent(int scid, std::wstring& resource);
		virtual bool GetComponentColor(int scid, uFireCOLOR &color);

		// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
		virtual XP_PREFETCH_RESULT PrefetchAction(const std::wstring &action_name) { return XPPR_OK; }
		
		// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
		virtual void HoldAction(const std::wstring &action_name) { return; }
		// 只有Hold住的Action才需要Release
		virtual void ReleaseAction(const std::wstring &action_name) { return; }

		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame);
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime);

		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time);
		
	//	virtual bool  IsMoving() const { return m_pMovableImp->IsMoving(); }
		virtual int GetCurrentFrame() const { return 0; }

		// 当前动作的总帧数
		virtual int GetTotalFrame() const { return 0; }
		//

		//动作声音
		virtual void SetActionSoundPriority(short priority) { return; }
		virtual short GetActionSoundPriority() const { return 0; }
		
		// 名字暂定，为了拾取准备的接口, r为检测半径
		virtual bool TestPoint(const Location &testloc, int r, float testval) { return true; }
		virtual void SetScale(float fScale);

		virtual bool AttachSprite(const std::wstring &hostSocket, const std::wstring &clientSocket, Sprite* pSprite, const NuclearVector3 &relpos, int sign);
		virtual bool DetachSprite(Sprite* pSprite);
		virtual void SetSpriteIncline(float incline) { m_fIncline = incline; }

		//以下俩只能给3D精灵和3D特效用
		virtual bool SetDurativeEffect(XHardRef<C3DEffect> pEffect, const CPOINT& relpos, const std::wstring &hostname, bool async);
		virtual void PlayEffect(C3DEffect *pEffect, const CPOINT& relpos, int times, const std::wstring &hostname, bool async);
		virtual bool MoveDurativeEffectToOtherSprite(IEffect *pEffect, Sprite *pSprite, const CPOINT& relpos, const std::wstring &hostname);
		virtual bool RemoveEffect(IEffect *pEffect);
		virtual bool ChangeEffectOffset(IEffect* pEffect, const CPOINT &relpos);

		virtual bool GetSocketPos(const std::wstring &socket, CPOINT &pos) const;

		// 3D模型的转向速度
		virtual void SetAutoRotationTimePerPI(float fTimePerPi);
		virtual float GetAutoRotationTimePerPI() const { return m_fAutoRotationTimePerPI; }

		// 设Alpha
		virtual void OnSetAlpha(unsigned char alpha);

		void On3DSpriteLoaded(GM::ISprite* sprite, bool succeed);
		void On3DSpriteComponentPrefetch(bool succeeded, const std::wstring& meshName, int partId);
		// Notify
		virtual void RegisterEventNotify(SpriteEventNotify* pNotify)
		{
			m_notifies.insert(pNotify);
		}

		virtual void DeregisterEventNotify(SpriteEventNotify* pNotify)
		{
			m_notifies.erase(pNotify);
		}

		virtual void OnSetVisible();

	public:
		inline void ProcessEvent(const std::vector<int>& eventIDs);
		void OnActionEnd();
		//精灵的别的特效
		virtual void Render3DFrontEffect();
		virtual void RenderSprite(BOOL renderAttachedEffect, Canvas* canvas, FRECT *pOutrect, bool isAlpha);
		virtual void X3DRenderCanvas(Sprite *pSprite, Canvas* canvas);
		void SetHead();
		virtual void GetMaxRect(const Location &vpCenter, FRECT &outrect);
		virtual void OnHighlight(bool isHighlight);
		virtual void OnStop(IEffect *pEffect);
		virtual void RenderChildSpriteEffectFront();
		virtual void RenderChildSpriteEffectBack();

		virtual void DumpAllActionNames(std::vector<std::wstring> &actions) const;
		virtual void Release() 
		{ 
			if (m_nRestNotifyCount)
			{
				m_bIsDeleted = true;
				ReleaseAll();
			} else {
				delete this; 
			}
		}


	private:
		void ReleaseAll();
		void ReleaseChildren();
		void RebindRes();
		void ResetTitlePos();
		C3DSprite *GetRoot() { return m_pFather?m_pFather->GetRoot():this; }
		inline bool UpdateState() const;
		inline void PushPopUpdateQueue(bool oldState);
		inline int GetActionTimeByNameAndSpeed(const std::wstring& act_name, float speed);

	private:
		struct C3DEffectStruct
		{
			XHardRef<C3DEffect> pEffect;
			std::wstring hostname;
			CPOINT relPos;
		};
		struct AttachSpriteSockets
		{
			std::wstring hostname;
			std::wstring clientname;
			NuclearVector3 relpos;
		};
		struct ComponentParam
		{
			std::wstring name;
			uFireCOLOR color;
		};

		typedef std::map<IEffect*, C3DEffectStruct> C3DEffectMap;
		typedef std::map<Sprite*, AttachSpriteSockets> ChildrenSpriteMap;
		typedef std::map<int, ComponentParam> ComponentMap;
		typedef std::set<SpriteEventNotify*> NotifySet;

	private:
		C3DSpriteNotify* m_pNotify;		// 注册进GM::GSprite的Notify
		Sprite* m_pLogicSprite;			// 精灵的逻辑对象
		GM::GSprite* m_pGSprite;		// 精灵的绘制对象
		float m_fIncline;				// 倾斜角（视角）
		float m_fDefaultActionSpeed;	// default action的速度
		float m_fTmpActionSpeed;		// tmpAction的速度
		float m_fAutoRotationTimePerPI;	// 转向的速度
		NuclearFPoint m_fptFaceTo;				// 朝向

		C3DSprite* m_pFather;			// 如果被Attach了的话。。。。
		ChildrenSpriteMap m_mapChildren;// 如果Attach了别人
		C3DSprite* m_pHead;				// Title以谁为准
		C3DEffectMap m_att3DEffect;		// 绑在精灵身上的3DEffect
		ComponentMap m_components;		// 装备名字们
		std::list<IEffect*> m_deledEffect;
		NotifySet m_notifies;			// 通告们

		bool m_bIsLoading;				// 是否正在异步加载中
		bool m_bIsActive;				// 是否Update
		bool m_bAlwaysUpdate;			// 是否永远Update
		bool m_bIsVisible;				// 是否可见
		int m_nUpdateTick;
		int m_nAccumulateDelta;			// 在不需要更新的时候积累时间
		int m_nCurActionTime;			// 当前动作需要的时间，用来给最小化、不显示的时候手动调用OnActionEnd回调，
										// 但是如果动作播了一半再最小化，积累的时间会从0开始算，而不是从播放点开始算，这点误差应该能忍吧
		bool m_bIsDeleted;				// 要删除了，但是回调还没有过来呢，等着；
		char m_nRestNotifyCount;		// 还剩下的回调;

		
	};

}

#endif