#ifndef __Nuclear_RenderableSprite_H__
#define __Nuclear_RenderableSprite_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxptypes.h"
#include "../engine/nurenderable.h"

namespace Nuclear
{
	struct NuclearSortBaseRectType;
	class Sprite;
	class C3DEffect;

	struct RenderSpriteOutParam
	{
		PictureHandle handle;
		NuclearFRectt srcRect;
		NuclearFRectt destRect;
		NuclearPoint pos;//������ͼƬ����
	};

	class RenderableSprite
	{
	protected:
		virtual ~RenderableSprite(void) {}	//���ܴ�����ɾ��

	public:
		RenderableSprite(void) {}	//���ܴ�����ɾ���Ľ���ǣ��Ժ��ͨ��һ������������new��		

		virtual NuclearSpriteType GetType() = 0;
		virtual void GetBase(NuclearSortBaseRectType &base) = 0;
		virtual void UpdateAction(DWORD ticktime) = 0;
		virtual void Update3DPos() {}
		virtual void SetActive(bool active) {}
		virtual bool IsUpdateEveryFrame() const { return false; }
		virtual void SetUpdateEveryFrame(bool b) {}
		virtual bool TestViewport(const NuclearRect &viewport) const = 0;

		//����ı����Ч
		virtual void Render3DFrontEffect() {}
		virtual void RenderSprite(BOOL renderAttachedEffect, Canvas* canvas, NuclearFRectt *pOutrect, bool isAlpha, bool bShake, bool realRender) = 0;
		virtual void RenderChildSpriteEffectFront() {}
		virtual void RenderChildSpriteEffectBack() {}

		virtual void SetScale(float fScale) {}
		// ���þ���ģ��
		virtual bool SetModel(const std::wstring &modelname, bool async) = 0;

		// ���þ����װ�����
		virtual bool SetComponent(int scid, const std::wstring& resource, NuclearColor color = 0xffffffff/*XRGB*/) = 0;
		virtual bool GetComponent(int scid, std::wstring& resource) = 0;
		virtual void* GetComponent(int scid) = 0;
		virtual bool GetComponentColor(int scid, NuclearColor &color) = 0;

		virtual void OnSetAlpha(unsigned char alpha) {}

		// Ԥȡ�þ���Ķ�����Դ���������Դ��GCʱ�䷶Χ��120�룩��û�б�ʹ�û��߱����£����ľ��飩Ԥȡ����ô���Զ��ͷ�
		virtual NuclearPrefetchResult PrefetchAction(const std::wstring &action_name) = 0;

		// ��ס�þ���Ķ�����Դ�����������ReleaseAction����ô����Զ���ͷţ�ֱ�����黻ģ�ͻ��߱�����
		virtual void HoldAction(const std::wstring &action_name) = 0;
		// ֻ��Holdס��Action����ҪRelease
		virtual void ReleaseAction(const std::wstring &action_name) = 0;

		virtual void SetHoldLastFrame(bool bHoldLastFrame) = 0;

		// Ĭ�϶�����ÿ�����������һ��Ĭ�϶���
		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame) = 0;
		// ��ʱ������ÿ��������0~1���� ��ʱ�����������֮���Զ��л�Ĭ�϶���, action_name �����ַ���ȡ����ʱ����
		// ����true������Ĳ����ˣ�����false��������ʵ����һ��������Ŀǰ�Ļ�û�����غ���
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime) = 0;
		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time) = 0;

		// ��ǰ�����Ĳ�����֡����������UpdateAction��ʱ����£���UpdateActionǰΪ��һ֡�����ݣ�UpdateAction��Ϊ��֡�����ݣ�
		virtual int GetCurrentFrame() const = 0;
		// ��ǰ��������֡��
		virtual int GetTotalFrame() const = 0;

		// �����ݶ���Ϊ��ʰȡ׼���Ľӿ�, rΪ���뾶
		virtual bool TestPoint(const NuclearLocation &testloc, int r, float testval) = 0;
		virtual void GetMaxRect(const NuclearLocation &vpCenter, NuclearFRectt &outrect) = 0;
		virtual void X3DRenderCanvas(Sprite *pSprite, Canvas* canvas) = 0;
		//������ֻ�ܸ�3D�����3D��Ч��
		virtual bool SetDurativeEffect(NuclearHardRef<C3DEffect> pEffect, const NuclearPoint& relpos, const std::wstring &hostname, bool async) = 0;
		virtual void PlayEffect(C3DEffect *pEffect, const NuclearPoint& relpos, int times, const std::wstring &hostname, bool async) = 0;
		virtual bool RemoveEffect(IEffect *pEffect) { return false; }
		virtual bool ChangeEffectOffset(IEffect* pEffect, const NuclearPoint &relpos) { return false; }

		// �����Ƿ��ѯSegmpak
		virtual void SetLookupSegmpak(bool b) {}

		// ������״̬�ı���֮��
		virtual void OnHighlight(bool isHighlight) {}

		virtual void OnSetVisible() {}

		virtual void DumpAllActionNames(std::vector<std::wstring> &actions) const = 0;
		virtual void Release() { delete this; }
        
        virtual void SetComponentAniReleaseFlag(Nuclear::NuclearPaniReleaseFlag flag) = 0;

		virtual void SetPartParam(int iLayerIndex, int iPartIndex, std::vector<float> vParam) = 0;
		virtual std::vector<float> GetPartParam(int iLayerIndex, int iPartIndex) = 0;
		virtual void SetDyePartIndex(int level, int part, int index) = 0;
		virtual bool GetDyeEnable() = 0;
		virtual int GetDyePartCount() = 0;
		virtual int GetDyeProjCount(int part) = 0;
	};

}


#endif