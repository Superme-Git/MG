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
		NuclearPoint pos;//可能在图片外面
	};

	class RenderableSprite
	{
	protected:
		virtual ~RenderableSprite(void) {}	//不能从外面删除

	public:
		RenderableSprite(void) {}	//不能从外面删除的结果是，以后得通过一个工厂方法来new了		

		virtual NuclearSpriteType GetType() = 0;
		virtual void GetBase(NuclearSortBaseRectType &base) = 0;
		virtual void UpdateAction(DWORD ticktime) = 0;
		virtual void Update3DPos() {}
		virtual void SetActive(bool active) {}
		virtual bool IsUpdateEveryFrame() const { return false; }
		virtual void SetUpdateEveryFrame(bool b) {}
		virtual bool TestViewport(const NuclearRect &viewport) const = 0;

		//精灵的别的特效
		virtual void Render3DFrontEffect() {}
		virtual void RenderSprite(BOOL renderAttachedEffect, Canvas* canvas, NuclearFRectt *pOutrect, bool isAlpha, bool bShake, bool realRender) = 0;
		virtual void RenderChildSpriteEffectFront() {}
		virtual void RenderChildSpriteEffectBack() {}

		virtual void SetScale(float fScale) {}
		// 设置精灵模型
		virtual bool SetModel(const std::wstring &modelname, bool async) = 0;

		// 设置精灵的装备组件
		virtual bool SetComponent(int scid, const std::wstring& resource, NuclearColor color = 0xffffffff/*XRGB*/) = 0;
		virtual bool GetComponent(int scid, std::wstring& resource) = 0;
		virtual void* GetComponent(int scid) = 0;
		virtual bool GetComponentColor(int scid, NuclearColor &color) = 0;

		virtual void OnSetAlpha(unsigned char alpha) {}

		// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
		virtual NuclearPrefetchResult PrefetchAction(const std::wstring &action_name) = 0;

		// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
		virtual void HoldAction(const std::wstring &action_name) = 0;
		// 只有Hold住的Action才需要Release
		virtual void ReleaseAction(const std::wstring &action_name) = 0;

		virtual void SetHoldLastFrame(bool bHoldLastFrame) = 0;

		// 默认动作，每个精灵必须有一个默认动作
		virtual bool SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame) = 0;
		// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
		// 返回true代表真的播放了，返回false代表播的其实是上一个动作，目前的还没有下载好呢
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime) = 0;
		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time) = 0;

		// 当前动作的播到哪帧（此数据在UpdateAction的时候更新，在UpdateAction前为上一帧的数据，UpdateAction后为本帧的数据）
		virtual int GetCurrentFrame() const = 0;
		// 当前动作的总帧数
		virtual int GetTotalFrame() const = 0;

		// 名字暂定，为了拾取准备的接口, r为检测半径
		virtual bool TestPoint(const NuclearLocation &testloc, int r, float testval) = 0;
		virtual void GetMaxRect(const NuclearLocation &vpCenter, NuclearFRectt &outrect) = 0;
		virtual void X3DRenderCanvas(Sprite *pSprite, Canvas* canvas) = 0;
		//以下仨只能给3D精灵和3D特效用
		virtual bool SetDurativeEffect(NuclearHardRef<C3DEffect> pEffect, const NuclearPoint& relpos, const std::wstring &hostname, bool async) = 0;
		virtual void PlayEffect(C3DEffect *pEffect, const NuclearPoint& relpos, int times, const std::wstring &hostname, bool async) = 0;
		virtual bool RemoveEffect(IEffect *pEffect) { return false; }
		virtual bool ChangeEffectOffset(IEffect* pEffect, const NuclearPoint &relpos) { return false; }

		// 设置是否查询Segmpak
		virtual void SetLookupSegmpak(bool b) {}

		// 当高亮状态改变了之后
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