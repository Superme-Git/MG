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
	typedef std::map<std::wstring, SpriteSoundParamVet> ActionSoundParamMap;//key�Ƕ�����
	typedef std::set<std::wstring> ActionNameSet;

	// �ֲ㾫�飬������ĳһģ��
	class ComponentSprite : public RenderableSprite
	{	
	private:
		typedef struct  
		{
			Canvas* canvas;
			int ec;
			int linkframe;
			NuclearDirection dir;
			float worldscale;//��������ű�������������ʵ�ʻ���λ�õ�
			NuclearPoint location;	//��Ļ���꣨renderTarget���꣩
			NuclearPoint loc;			//��������
			std::wstring curActionName;
			unsigned char nAlpha;
			bool bIsEffectlayer;
			bool bSync;//�Ƿ�ͬ������
			bool bIsWeaponlayer;
			int iFlyScaleCur;//����ж���������ŵ�ǰֵ
			int iFlyOffsetYCur;//����ж������Y��ƫ��(ʵ������ģ��Z��)��ǰֵ
		} LayerParam;

	public:
		std::vector<int> m_vShaderType;				// ʹ�õ�Shader����,Ĭ��Ϊ0, 0:������Ⱦ,����:HSV

		int m_iPartCount;
		std::vector<std::vector<std::vector<float> > > m_vPartParams;// ����Ⱦɫ���� Layer<Part<Param> >

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

		//��������
		virtual void SetActionSoundPriority(short priority) { m_nSoundPriority = priority; }
		virtual short GetActionSoundPriority() const { return m_nSoundPriority; }

		virtual bool SetModel(const std::wstring &modelname, bool async);
		// ���þ����װ�����
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

		// Ԥȡ�þ���Ķ�����Դ���������Դ��GCʱ�䷶Χ��120�룩��û�б�ʹ�û��߱����£����ľ��飩Ԥȡ����ô���Զ��ͷ�
		virtual NuclearPrefetchResult PrefetchAction(const std::wstring &action_name);

		// ��ס�þ���Ķ�����Դ�����������ReleaseAction����ô����Զ���ͷţ�ֱ�����黻ģ�ͻ��߱�����
		virtual void HoldAction(const std::wstring &action_name);
		// ֻ��Holdס��Action����ҪRelease
		virtual void ReleaseAction(const std::wstring &action_name);

		virtual void SetHoldLastFrame(bool bHoldLastFrame);

		// Ĭ�϶�����ÿ�����������һ��Ĭ�϶���
		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq, bool bHoldLastFrame);
		// ��ʱ������ÿ��������0~1���� ��ʱ�����������֮���Զ��л�Ĭ�϶���, action_name �����ַ���ȡ����ʱ����
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq);
		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time);

		virtual bool TestPoint(const NuclearLocation &testloc, int r, float testval);

		virtual ActionTypeFlag GetCurrentActionType(){ return m_pTmpAction == NULL ? m_pLogicSprite->m_defaultActionTypeFlag : m_pLogicSprite->m_actionTypeFlag; }

		virtual int GetTotalFrame() const;
		virtual bool SetDurativeEffect(NuclearHardRef<C3DEffect> pEffect, const NuclearPoint& relpos, const std::wstring &hostname, bool async);
		virtual void PlayEffect(C3DEffect *pEffect, const NuclearPoint& relpos, int times, const std::wstring &hostname, bool async) { XPASSERT(false && L"ComponentSprite::PlayEffect"); }

		// �����Ƿ��ѯSegmpak
		virtual void SetLookupSegmpak(bool b) { m_bLookupSegmpak = b; }

		virtual void SetComponentAniReleaseFlag(Nuclear::NuclearPaniReleaseFlag flag);

	protected:
		NuclearHardRef<XAni> LoadLayer(const LayerParam& layerParam, int iLayerIndex, bool bSync);
		void RenderALayer(const LayerParam& layerParam, NuclearFRectt &maxRect, bool realRender);
		
		//����ı����Ч
		virtual void RenderSprite(BOOL renderAttachedEffect, Canvas* canvas, NuclearFRectt *pOutrect, bool isAlpha, bool bShake, bool realRender);
		virtual void UpdateAction(DWORD ticktime);
		virtual void ResetHorseHeight(const std::wstring& ridename);
		virtual void ResetEffectPos();
		bool GetActionNames(const std::wstring &action_name, ActionNameSet &actions, const Action* &pAct);
		void ReleaseAllActions();
		inline bool TestDefaultAction1() const { return (m_pDefaultAction == NULL) ? false : true; }
		void updateShake(LayerParam& param, bool bShake);
		
	protected:
		Sprite *m_pLogicSprite;						// ������߼�����
		std::wstring m_strDefaultActionRefName;		// Ĭ�϶�������
		std::wstring m_strTmpActionRefName;			// ��ʱ����������
		std::wstring m_strCurrentActionRefName;		// ��ǰ����������
		const Action *m_pDefaultAction;				// Ĭ�϶���
		const Action *m_pTmpAction;					// ��ʱ����
		ActionSoundParamMap *m_pActionMap;			// ��ǰװ��������Map
		std::vector<Component*> m_curComponent;		// ����
		std::vector<NuclearColor> m_colors;				// ������ɫ
		unsigned char m_flagRideLayer;				// ÿ���Ƿ���ˣ�����Ļ���1
		unsigned char m_flagEffectLayer;			// ÿ���Ƿ���Ч������Ч�Ļ���1
		int	m_nBeginTick;							// ����ʱ���tick������������Ч���aplha
		float m_fEffectAlpha;						// ��Ч���Alpha
		float m_fMinEffectAlpha;					// ��Ч���Alpha����
		float m_fSlopeEffectAlpha;					// ��Ч���Alphaб��

		int m_nOldFrame;							// ��һ��Update��֡�����ܻ�����֡
		ActionNameSet m_holdActions;				// Holdס����Դ
		int m_nLayerCount;							// ����
		short m_nSoundPriority;						// �������������ȼ�
		bool m_bLookupSegmpak;						// �Ƿ��ѯ�Ӱ�

		unsigned int m_nStartShakeTime;				// ��ʼ����ʱ�̵�
		NuclearPoint		 m_OrgLocation;					// ��ʼ����ʱ��λ��
		NuclearPoint		 m_NextLocation;				// ������Ŀ��λ��
		int			 m_ShakeCount;					// ��������

		unsigned char		 m_bDyeEnable;					// �Ƿ����Ⱦɫ >0 ����Ⱦɫ >1�зֿ�ͼ
		bool  m_bActive;
	};

};

#endif