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
	typedef std::map<std::wstring, SpriteSoundParamVet> ActionSoundParamMap;//key�Ƕ�����
	typedef std::set<std::wstring> ActionNameSet;

	// �ֲ㾫�飬������ĳһģ��
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
			float worldscale;//��������ű�������������ʵ�ʻ���λ�õ�
			PictureHandle tmpSprite;
			CPOINT location;	//��Ļ���꣨renderTarget���꣩
			CPOINT loc;			//��������
			std::wstring curActionName;
			XPMATRIX3* pMatrix;
			bool bIsEffectlayer;
			bool bIsShadow;	//�Ƿ��ڻ�Ӱ��
			bool bHasRenderTarget;//�Ƿ���������
			bool bSync;//�Ƿ�ͬ������
		} LayerParam;
		typedef struct 
		{
			CPOINT loc;
			int tick;
		} TeleportBlurFrame;

		typedef std::list<TeleportBlurFrame> TeleportBlurList;

	protected:
		std::wstring m_strDefaultActionOutsideName;	//Ĭ�϶����ⲿ����
		std::wstring m_strTmpActionOutsideName;		//��ʱ�������ⲿ����
		std::wstring m_ModelName;					// ģ����
		const Action *m_pDefaultAction;				// Ĭ�϶���
		const Action *m_pTmpAction;					// ��ʱ����
		ActionSoundParamMap *m_pActionMap;			// ��ǰװ��������Map
		std::vector<Component*> m_CurComponents;		// ����
		std::vector<XPCOLOR> m_Colors;				// ������ɫ
		unsigned char m_flagRideLayer;				// ÿ���Ƿ���ˣ�����Ļ���1
		unsigned char m_FlagEffectLayer;			// ÿ���Ƿ���Ч������Ч�Ļ���1
		FRECT m_MaxRectInTex;						// �����RenderTarget����ô���rect������locationΪԭ��ĵ�ǰ֡������
		FRECT m_srcRectInTex;						// �����RenderTarget����ô���rect���ǵ�ǰ֡��������RenderTarget����������
		int	m_nBeginTick;							// ����ʱ���tick������������Ч���aplha
		float m_fEffectAlpha;						// ��Ч���Alpha
		float m_fMinEffectAlpha;					// ��Ч���Alpha����
		float m_fSlopeEffectAlpha;					// ��Ч���Alphaб��
		TeleportBlurList m_blurList;				// ���򿪻��ˡ�����Ӱʱ����Ӱ��λ�ú�ʱ��
		int m_nOldFrame;							// ��һ��Update��֡�����ܻ�����֡
		ActionNameSet m_holdActions;				// Holdס����Դ
		int m_nLayerCount;							// ����
		short m_nSoundPriority;						// �������������ȼ�

	public:
		ComponentSprite(EngineBase *pEB, const std::wstring &modelname);
		virtual ~ComponentSprite();

	public:

		virtual void GetBase(SortBaseRectType &base);

		//��������
		virtual void SetActionSoundPriority(short priority) { m_nSoundPriority = priority; }
		virtual short GetActionSoundPriority() const { return m_nSoundPriority; }

		virtual std::wstring GetModelName() { return m_ModelName; }
		virtual void SetModel(const std::wstring &modelname);
		// ���þ����װ�����
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


		// Ԥȡ�þ���Ķ�����Դ���������Դ��GCʱ�䷶Χ��120�룩��û�б�ʹ�û��߱����£����ľ��飩Ԥȡ����ô���Զ��ͷ�
		virtual XP_PREFETCH_RESULT PrefetchAction(const std::wstring &action_name);

		// ��ס�þ���Ķ�����Դ�����������ReleaseAction����ô����Զ���ͷţ�ֱ�����黻ģ�ͻ��߱�����
		virtual void HoldAction(const std::wstring &action_name);
		// ֻ��Holdס��Action����ҪRelease
		virtual void ReleaseAction(const std::wstring &action_name);

		// Ĭ�϶�����ÿ�����������һ��Ĭ�϶���
		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq, bool bHoldLastFrame);
		virtual const std::wstring &GetDefaultAction() const { return m_strDefaultActionOutsideName; }
		// ��ʱ������ÿ��������0~1���� ��ʱ�����������֮���Զ��л�Ĭ�϶���, action_name �����ַ���ȡ����ʱ����
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
