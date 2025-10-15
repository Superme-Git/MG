#ifndef __Nuclear_ComponentSprite_H
#define __Nuclear_ComponentSprite_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "sprite.h"

namespace Nuclear
{
	class Action;
	class Component;
	struct SpriteSoundParam;
	typedef std::vector<SpriteSoundParam> SpriteSoundParamVet;
	typedef std::map<std::wstring, SpriteSoundParamVet> ActionSoundParamMap;//key是动作名
	typedef std::set<std::wstring> ActionNameSet;

	// 分层精灵，隶属于某一模型
	class ComponentSprite : public Sprite
	{	
	private:
		typedef struct  
		{
			Canvas* canvas;
			int ec;
			int linkframe;
			XPDIRECTION dir;
			float waterdepth;
			float water;
			float worldscale;//世界的缩放比例，用来计算实际绘制位置的
			PictureHandle tmpSprite;
			CPOINT location;	//屏幕坐标（renderTarget坐标）
			CPOINT loc;			//世界坐标
			std::wstring curActionName;
			XPMATRIX3* pMatrix;
			bool bIsEffectlayer;
			bool bIsShadow;	//是否在画影子
			bool bHasRenderTarget;//是否画在纹理上
			bool bSync;//是否同步加载
		} LayerParam;
		typedef struct 
		{
			CPOINT loc;
			int tick;
		} TeleportBlurFrame;

		typedef std::list<TeleportBlurFrame> TeleportBlurList;

	protected:
		std::wstring m_strDefaultActionOutsideName;	//默认动作外部名字
		std::wstring m_strTmpActionOutsideName;		//临时动作的外部名字
		std::wstring m_ModelName;					// 模型名
		const Action *m_pDefaultAction;				// 默认动作
		const Action *m_pTmpAction;					// 临时动作
		ActionSoundParamMap *m_pActionMap;			// 当前装备的声音Map
		std::vector<Component*> m_CurComponents;		// 各层
		std::vector<XPCOLOR> m_Colors;				// 各层颜色
		unsigned char m_flagRideLayer;				// 每层是否骑乘，是马的话置1
		unsigned char m_FlagEffectLayer;			// 每层是否特效，是特效的话置1
		FRECT m_MaxRectInTex;						// 如果有RenderTarget，那么这个rect就是以location为原点的当前帧的最大框
		FRECT m_srcRectInTex;						// 如果有RenderTarget，那么这个rect就是当前帧的最大框在RenderTarget的纹理坐标
		int	m_nBeginTick;							// 构造时候的tick，用来计算特效层的aplha
		float m_fEffectAlpha;						// 特效层的Alpha
		float m_fMinEffectAlpha;					// 特效层的Alpha下限
		float m_fSlopeEffectAlpha;					// 特效层的Alpha斜率
		TeleportBlurList m_blurList;				// 当打开击退、冲锋残影时，残影的位置和时间
		int m_nOldFrame;							// 上一次Update的帧，可能会有跳帧
		ActionNameSet m_holdActions;				// Hold住的资源
		int m_nLayerCount;							// 层数
		short m_nSoundPriority;						// 动作声音的优先级

	public:
		ComponentSprite(EngineBase *pEB, const std::wstring &modelname);
		virtual ~ComponentSprite();

	public:

		virtual void GetBase(SortBaseRectType &base);

		//动作声音
		virtual void SetActionSoundPriority(short priority) { m_nSoundPriority = priority; }
		virtual short GetActionSoundPriority() const { return m_nSoundPriority; }

		virtual std::wstring GetModelName() { return m_ModelName; }
		virtual void SetModel(const std::wstring &modelname);
		// 设置精灵的装备组件
		virtual bool SetComponent(int sc, const std::wstring& resource, XPCOLOR color=0xffffffff/*XRGB*/);
		virtual bool GetComponent(int scid, std::wstring& resource);
		virtual bool GetComponentColor(int scid, XPCOLOR &color);

		virtual void SetMinMaxEffectAlpha(float minalpha, float maxalpha)
		{
			XPASSERT(minalpha >= 0.0f && minalpha <= 1.0f);
			XPASSERT(maxalpha >= 0.0f && maxalpha <= 1.0f);
			m_fMinEffectAlpha = minalpha;
			m_fSlopeEffectAlpha = (maxalpha - minalpha) / XP_EFFECT_HALF_CYCLE;
		}
		virtual float GetMinEffectAlpha() const { return m_fMinEffectAlpha; }
		virtual float GetMaxEffectAlpha() const { return m_fSlopeEffectAlpha * XP_EFFECT_HALF_CYCLE + m_fMinEffectAlpha; }

		virtual bool TestViewport(const CRECT &viewport) const;


		// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
		virtual XP_PREFETCH_RESULT PrefetchAction(const std::wstring &action_name);

		// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
		virtual void HoldAction(const std::wstring &action_name);
		// 只有Hold住的Action才需要Release
		virtual void ReleaseAction(const std::wstring &action_name);

		// 默认动作，每个精灵必须有一个默认动作
		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq, bool bHoldLastFrame);
		virtual const std::wstring &GetDefaultAction() const { return m_strDefaultActionOutsideName; }
		// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq);
		virtual const std::wstring &GetCurActionName() const { return m_strTmpActionOutsideName; }

		virtual bool TestPoint(const Location &testloc, int r, float testval);
		virtual void EnableShadow(bool b) { SetPropFlag(XPSPPF_RENDER_SHADOW, b); }
		virtual bool IsEnableShadow() { return TestPropFlag(XPSPPF_RENDER_SHADOW); }

		virtual ActionTypeFlag GetCurrentActionType()
		{
			return m_pTmpAction == NULL ? m_defaultActionTypeFlag : m_actionTypeFlag;
		}

		virtual int GetTotalFrame() const;
		virtual void TeleportWithBlur(const Location &point, int telTime, int blurTime);

	protected:
		void RenderALayer(const LayerParam& layerParam);
		virtual void RenderSprite(Canvas* canvas);
		virtual void UpdateAction();
		virtual void ResetHorseHeight(const std::wstring& ridename);
		virtual void ResetEffectPos();
		virtual void ResetStepAction(const std::wstring& ridename);
		void PlayActionSound(const std::wstring &action_name);
		bool GetActionNames(const std::wstring &action_name, ActionNameSet &actions, const Action* &pAct);
		void ReleaseAllActions();
		inline bool TestDefaultAction() const {	return (m_pDefaultAction == NULL)?false:true; }
		
	};

};

#endif
