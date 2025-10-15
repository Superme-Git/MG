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
		
		// ���þ���ģ��
		virtual bool SetModel(const std::wstring &modelname, bool async);
		
		// ���þ����װ�����
		virtual bool SetComponent(int scid, const std::wstring& resource, uFireCOLOR color=0xffffffff/*XRGB*/);


		virtual bool GetComponent(int scid, std::wstring& resource);
		virtual bool GetComponentColor(int scid, uFireCOLOR &color);

		// Ԥȡ�þ���Ķ�����Դ���������Դ��GCʱ�䷶Χ��120�룩��û�б�ʹ�û��߱����£����ľ��飩Ԥȡ����ô���Զ��ͷ�
		virtual XP_PREFETCH_RESULT PrefetchAction(const std::wstring &action_name) { return XPPR_OK; }
		
		// ��ס�þ���Ķ�����Դ�����������ReleaseAction����ô����Զ���ͷţ�ֱ�����黻ģ�ͻ��߱�����
		virtual void HoldAction(const std::wstring &action_name) { return; }
		// ֻ��Holdס��Action����ҪRelease
		virtual void ReleaseAction(const std::wstring &action_name) { return; }

		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame);
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime);

		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time);
		
	//	virtual bool  IsMoving() const { return m_pMovableImp->IsMoving(); }
		virtual int GetCurrentFrame() const { return 0; }

		// ��ǰ��������֡��
		virtual int GetTotalFrame() const { return 0; }
		//

		//��������
		virtual void SetActionSoundPriority(short priority) { return; }
		virtual short GetActionSoundPriority() const { return 0; }
		
		// �����ݶ���Ϊ��ʰȡ׼���Ľӿ�, rΪ���뾶
		virtual bool TestPoint(const Location &testloc, int r, float testval) { return true; }
		virtual void SetScale(float fScale);

		virtual bool AttachSprite(const std::wstring &hostSocket, const std::wstring &clientSocket, Sprite* pSprite, const NuclearVector3 &relpos, int sign);
		virtual bool DetachSprite(Sprite* pSprite);
		virtual void SetSpriteIncline(float incline) { m_fIncline = incline; }

		//������ֻ�ܸ�3D�����3D��Ч��
		virtual bool SetDurativeEffect(XHardRef<C3DEffect> pEffect, const CPOINT& relpos, const std::wstring &hostname, bool async);
		virtual void PlayEffect(C3DEffect *pEffect, const CPOINT& relpos, int times, const std::wstring &hostname, bool async);
		virtual bool MoveDurativeEffectToOtherSprite(IEffect *pEffect, Sprite *pSprite, const CPOINT& relpos, const std::wstring &hostname);
		virtual bool RemoveEffect(IEffect *pEffect);
		virtual bool ChangeEffectOffset(IEffect* pEffect, const CPOINT &relpos);

		virtual bool GetSocketPos(const std::wstring &socket, CPOINT &pos) const;

		// 3Dģ�͵�ת���ٶ�
		virtual void SetAutoRotationTimePerPI(float fTimePerPi);
		virtual float GetAutoRotationTimePerPI() const { return m_fAutoRotationTimePerPI; }

		// ��Alpha
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
		//����ı����Ч
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
		C3DSpriteNotify* m_pNotify;		// ע���GM::GSprite��Notify
		Sprite* m_pLogicSprite;			// ������߼�����
		GM::GSprite* m_pGSprite;		// ����Ļ��ƶ���
		float m_fIncline;				// ��б�ǣ��ӽǣ�
		float m_fDefaultActionSpeed;	// default action���ٶ�
		float m_fTmpActionSpeed;		// tmpAction���ٶ�
		float m_fAutoRotationTimePerPI;	// ת����ٶ�
		NuclearFPoint m_fptFaceTo;				// ����

		C3DSprite* m_pFather;			// �����Attach�˵Ļ���������
		ChildrenSpriteMap m_mapChildren;// ���Attach�˱���
		C3DSprite* m_pHead;				// Title��˭Ϊ׼
		C3DEffectMap m_att3DEffect;		// ���ھ������ϵ�3DEffect
		ComponentMap m_components;		// װ��������
		std::list<IEffect*> m_deledEffect;
		NotifySet m_notifies;			// ͨ����

		bool m_bIsLoading;				// �Ƿ������첽������
		bool m_bIsActive;				// �Ƿ�Update
		bool m_bAlwaysUpdate;			// �Ƿ���ԶUpdate
		bool m_bIsVisible;				// �Ƿ�ɼ�
		int m_nUpdateTick;
		int m_nAccumulateDelta;			// �ڲ���Ҫ���µ�ʱ�����ʱ��
		int m_nCurActionTime;			// ��ǰ������Ҫ��ʱ�䣬��������С��������ʾ��ʱ���ֶ�����OnActionEnd�ص���
										// ���������������һ������С�������۵�ʱ����0��ʼ�㣬�����ǴӲ��ŵ㿪ʼ�㣬������Ӧ�����̰�
		bool m_bIsDeleted;				// Ҫɾ���ˣ����ǻص���û�й����أ����ţ�
		char m_nRestNotifyCount;		// ��ʣ�µĻص�;

		
	};

}

#endif