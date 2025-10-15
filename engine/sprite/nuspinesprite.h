#ifndef __Nuclear_SpineSprite_H__
#define __Nuclear_SpineSprite_H__

#if _MSC_VER > 1000
#pragma once
#endif

//#if ((defined WIN32) || (defined ANDROID))
#include <stddef.h>
//#endif
#include "nusprite.h"
#include <spine/spine-cocos2dx.h>
#include "engine/nuspinemanager.h"

namespace Nuclear
{
	class SpineSprite : public Sprite, public SpineLoadingNotify
	{
	public:
		SpineSprite(EngineBase *pEB, const std::wstring &modelname);
		virtual ~SpineSprite();

		// 设置精灵模型
		virtual bool SetModel(const std::wstring &modelname, bool async);

		void SetActive(bool active) { m_bActive = active; }

		virtual void* GetComponent(int scid) { return 0; }

		// 设置精灵的装备组件
		virtual bool SetComponent(int scid, const std::wstring& resource, NuclearColor color = 0xffffffff/*XRGB*/, int iShaderType = 0, std::wstring wsLayerName = L"") { return false; }
		virtual bool GetComponent(int scid, std::wstring& resource) { return false; }

		// 默认动作，每个精灵必须有一个默认动作
		virtual bool SetDefaultAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame);

		// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
		// 返回true代表真的播放了，返回false代表播的其实是上一个动作，目前的还没有下载好呢
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime);

		// 单位是秒，如果没有这个动作，返回false
		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time);

		virtual void RegisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame) {}
		virtual void DeregisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame) {}

		virtual void SetScale(float scale);

		virtual void SetRideName(const std::wstring& name) {}

		virtual int GetLayerCount() { return 0; }
		virtual int GetLayerIndexByName(std::wstring wsName) { return 0; }
		virtual std::wstring GetLayerNameByIndex(int iIndex) { return L""; }
		virtual bool IsPartLayer(int iIndex) { return false; }

		virtual void Update(DWORD ticktime);
		virtual void UpdateAction(DWORD ticktime);
		virtual void RenderSpriteOnly(bool realRender)
		{
			if (m_pSkelAnim && realRender && (m_nVisible & XP_VIS_VIS) && (m_nVisible & XP_VIS_ENTITY))
			{
				NuclearRect vp = m_pEB->GetViewport();
				m_pSkelAnim->draw(vp.left, vp.top);
			}
		}
		virtual bool TestPoint(const NuclearLocation &testloc, int r, float testval)
		{
			return true;
		}

		virtual void SetSkelAnim(spine::SkeletonAnimation* pSkelAnim)
		{
			if (pSkelAnim){
				m_pSkelAnim = pSkelAnim;
				SetScale(m_fScale);
			}
		}

		virtual bool TestViewport(const NuclearRect &viewport) const;

		void Reset();

		virtual void OnLoaded(const std::wstring& dir, SpineRes* spineRes);

		//virtual void GetBase(NuclearSortBaseRectType &base);

		virtual void* GetUserData() const { return mpUserData; }
		virtual void SetUserData(void* pData) { mpUserData = pData; }
		void SetIsUISprite(bool b);

	private:

		virtual void renderEntity(Canvas* canvas, bool isAlpha, bool realRender);

		virtual void release();

	private:

		spine::SkeletonAnimation* m_pSkelAnim;
		int m_iAniStartTime;
		int m_iAniDurationTime;
		float m_fScaleForTotalTime;

		bool  m_bActive;

		void* mpUserData;
		bool  m_isUISprite;	//是否是UI
	};

};

#endif
