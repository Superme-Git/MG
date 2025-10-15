#ifndef __Nuclear_ComponentSprite_H__
#define __Nuclear_ComponentSprite_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nurenderablesprite.h"
#include "nusprite.h"

namespace Nuclear
{
	class Sprite;
	class Action;
	class Component;
	struct SpriteSoundParam;
	typedef std::vector<SpriteSoundParam> SpriteSoundParamVet;
	typedef std::map<std::wstring, SpriteSoundParamVet> ActionSoundParamMap;//key是动作名
	typedef std::set<std::wstring> ActionNameSet;

	// 分层精灵，隶属于某一模型
	class ComponentSprite : public RenderableSprite
	{	
	private:
		typedef struct  
		{
			Canvas* canvas;
			int ec;
			int linkframe;
			NuclearDirection dir;
			float worldscale;//世界的缩放比例，用来计算实际绘制位置的
			NuclearPoint location;	//屏幕坐标（renderTarget坐标）
			NuclearPoint loc;			//世界坐标
			std::wstring curActionName;
			unsigned char nAlpha;
			bool bIsEffectlayer;
			bool bSync;//是否同步加载
			bool bIsWeaponlayer;
			int iFlyScaleCur;//因飞行而引起的缩放当前值
			int iFlyOffsetYCur;//因飞行而引起的Y轴偏移(实际上是模拟Z轴)当前值
		} LayerParam;

	public:
		std::vector<int> m_vShaderType;				// 使用的Shader类型,默认为0, 0:正常渲染,否则:HSV

		int m_iPartCount;
		std::vector<std::vector<std::vector<float> > > m_vPartParams;// 部件染色参数 Layer<Part<Param> >

	public:
		ComponentSprite(Sprite *pLogicSprite);

		virtual ~ComponentSprite();
		virtual void SetPartParam(int iLayerIndex, int iPartIndex, std::vector<float> vParam);
		virtual std::vector<float> GetPartParam(int iLayerIndex, int iPartIndex);
		void SetDyePartIndex(int level, int part, int index);
		virtual bool GetDyeEnable(){ return m_bDyeEnable > 0; }
		virtual int GetDyePartCount();
		virtual int GetDyeProjCount(int part);
		
		void SetActive(bool active) { m_bActive = active; }
		virtual NuclearSpriteType GetType() { return XPSPRITETYPE_2D; }
		virtual int GetCurrentFrame() const { return 0; }

		virtual void DumpAllActionNames(std::vector<std::wstring> &actions) const;

		virtual void GetBase(NuclearSortBaseRectType &base);

		//动作声音
		virtual void SetActionSoundPriority(short priority) { m_nSoundPriority = priority; }
		virtual short GetActionSoundPriority() const { return m_nSoundPriority; }

		virtual bool SetModel(const std::wstring &modelname, bool async);
		// 设置精灵的装备组件
		virtual bool SetComponent(int sc, const std::wstring& resource, NuclearColor color = 0xffffffff/*XRGB*/);
		virtual bool GetComponent(int scid, std::wstring& resource);
		virtual void* GetComponent(int scid);
		virtual bool GetComponentColor(int scid, NuclearColor &color);

		virtual void X3DRenderCanvas(Sprite *pSprite, Canvas* canvas) { XPASSERT(false && L"ComponentSprite::X3DRenderCanvas"); }
		virtual void GetMaxRect(const NuclearLocation &vpCenter, NuclearFRectt &outrect) { XPASSERT(false && L"ComponentSprite::GetMaxRect"); }

		virtual void SetMinMaxEffectAlpha(float minalpha, float maxalpha)
		{
			XPASSERT(minalpha >= 0.0f && minalpha <= 1.0f);
			XPASSERT(maxalpha >= 0.0f && maxalpha <= 1.0f);
			m_fMinEffectAlpha = minalpha;
			m_fSlopeEffectAlpha = (maxalpha - minalpha) / FIRE_EFFECT_HALF_CYCLE;
		}
		virtual float GetMinEffectAlpha() const { return m_fMinEffectAlpha; }
		virtual float GetMaxEffectAlpha() const { return m_fSlopeEffectAlpha * FIRE_EFFECT_HALF_CYCLE + m_fMinEffectAlpha; }

		virtual bool TestViewport(const NuclearRect &viewport) const;

		// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
		virtual NuclearPrefetchResult PrefetchAction(const std::wstring &action_name);

		// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
		virtual void HoldAction(const std::wstring &action_name);
		// 只有Hold住的Action才需要Release
		virtual void ReleaseAction(const std::wstring &action_name);

		virtual void SetHoldLastFrame(bool bHoldLastFrame);

		// 默认动作，每个精灵必须有一个默认动作
		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq, bool bHoldLastFrame);
		// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq);
		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time);

		virtual bool TestPoint(const NuclearLocation &testloc, int r, float testval);

		virtual ActionTypeFlag GetCurrentActionType(){ return m_pTmpAction == NULL ? m_pLogicSprite->m_defaultActionTypeFlag : m_pLogicSprite->m_actionTypeFlag; }

		virtual int GetTotalFrame() const;
		virtual bool SetDurativeEffect(NuclearHardRef<C3DEffect> pEffect, const NuclearPoint& relpos, const std::wstring &hostname, bool async);
		virtual void PlayEffect(C3DEffect *pEffect, const NuclearPoint& relpos, int times, const std::wstring &hostname, bool async) { XPASSERT(false && L"ComponentSprite::PlayEffect"); }

		// 设置是否查询Segmpak
		virtual void SetLookupSegmpak(bool b) { m_bLookupSegmpak = b; }

		virtual void SetComponentAniReleaseFlag(Nuclear::NuclearPaniReleaseFlag flag);

	protected:
		NuclearHardRef<XAni> LoadLayer(const LayerParam& layerParam, int iLayerIndex, bool bSync);
		void RenderALayer(const LayerParam& layerParam, NuclearFRectt &maxRect, bool realRender);
		
		//精灵的别的特效
		virtual void RenderSprite(BOOL renderAttachedEffect, Canvas* canvas, NuclearFRectt *pOutrect, bool isAlpha, bool bShake, bool realRender);
		virtual void UpdateAction(DWORD ticktime);
		virtual void ResetHorseHeight(const std::wstring& ridename);
		virtual void ResetEffectPos();
		bool GetActionNames(const std::wstring &action_name, ActionNameSet &actions, const Action* &pAct);
		void ReleaseAllActions();
		inline bool TestDefaultAction1() const { return (m_pDefaultAction == NULL) ? false : true; }
		void updateShake(LayerParam& param, bool bShake);
		
	protected:
		Sprite *m_pLogicSprite;						// 精灵的逻辑对象
		std::wstring m_strDefaultActionRefName;		// 默认动作名字
		std::wstring m_strTmpActionRefName;			// 临时动作的名字
		std::wstring m_strCurrentActionRefName;		// 当前动作的名字
		const Action *m_pDefaultAction;				// 默认动作
		const Action *m_pTmpAction;					// 临时动作
		ActionSoundParamMap *m_pActionMap;			// 当前装备的声音Map
		std::vector<Component*> m_curComponent;		// 各层
		std::vector<NuclearColor> m_colors;				// 各层颜色
		unsigned char m_flagRideLayer;				// 每层是否骑乘，是马的话置1
		unsigned char m_flagEffectLayer;			// 每层是否特效，是特效的话置1
		int	m_nBeginTick;							// 构造时候的tick，用来计算特效层的aplha
		float m_fEffectAlpha;						// 特效层的Alpha
		float m_fMinEffectAlpha;					// 特效层的Alpha下限
		float m_fSlopeEffectAlpha;					// 特效层的Alpha斜率

		int m_nOldFrame;							// 上一次Update的帧，可能会有跳帧
		ActionNameSet m_holdActions;				// Hold住的资源
		int m_nLayerCount;							// 层数
		short m_nSoundPriority;						// 动作声音的优先级
		bool m_bLookupSegmpak;						// 是否查询子包

		unsigned int m_nStartShakeTime;				// 开始抖动时刻点
		NuclearPoint		 m_OrgLocation;					// 开始抖动时的位置
		NuclearPoint		 m_NextLocation;				// 抖动的目标位置
		int			 m_ShakeCount;					// 抖动次数

		unsigned char		 m_bDyeEnable;					// 是否可以染色 >0 可以染色 >1有分块图
		bool  m_bActive;
	};

};

#endif