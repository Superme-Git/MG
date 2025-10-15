#ifndef __Nuclear_IWORLD_H__
#define __Nuclear_IWORLD_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "common/nuxptypes.h"
#include "nuieffect.h"
#include "nuisprite.h"
#include "nuiimmovableobj.h"


namespace Nuclear
{
	class NuclearStepSoundMap;

	//地图跳转效果
	enum XPLOADMAP_EFFECT
	{
		XPLOADMAP_NOEFFECT,
		//XPLOADMAP_JUMP
	};

	// Load地图参数
	struct XPLoadmapParam
	{
		NuclearLocation objLoc;	//跳转目标位置
		virtual XPLOADMAP_EFFECT GetType() const
		{
			return XPLOADMAP_NOEFFECT;
		}
		virtual ~XPLoadmapParam() {}
	};


	inline Nuclear_EffectLayer transformWarLayer2NewLayer(Nuclear_EffectLayer oldWarLayer)
	{
		switch (oldWarLayer)
		{
		case XPEL_UNDER_SPRITE:
			return XPEL_BATTLE_LOW;
			break;
		case XPEL_SPRITE:
			return XPEL_BATTLE_MID;
			break;
		case XPEL_ABOVE_SPRITE:
			return XPEL_BATTLE_HEIGHT;
			break;
		}
		return oldWarLayer;
	}

	class ShakeScreenController
	{
	public:
		virtual NuclearPoint GetShakeScreenAdjust(int now, int delta) = 0;
	};

	// 游戏世界
	class IWorld
	{
	public:
		IWorld() {}
		virtual ~IWorld() {  }

	public:
		bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param){return LoadMap(mapname, mazename, param, false);}
		virtual bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, bool async) = 0;	
		virtual bool UnloadMap() = 0;
		virtual void ImmediatelyAssureRegionResource(const Nuclear::NuclearLocation &loc) = 0;

		virtual bool GetMapSize(NuclearPoint &size) const = 0;

		/* 振屏控制器，当设了这个之后，每帧调一次GetShakeScreenAdjust，获取本帧的viewport偏移值 */
		virtual void SetShakeScreenController(ShakeScreenController *pController) = 0;
		virtual ShakeScreenController* GetShakeScreenController() const = 0;

		/* 飞行 */
		virtual void SetFlyOffsetYTgt(int iFlyOffsetYTgt) = 0;
		virtual void SetFlyOffsetYCur(int iFlyOffsetYCur) = 0;
		virtual int GetFlyOffsetYCur() = 0;
		virtual void SetFlyOffsetYStep(int iFlyOffsetYStep) = 0;
		virtual int GetFlyOffsetYStep() = 0;
		virtual void SetScale(float scale) = 0;
		virtual float GetScale () const = 0;
		virtual void SetUseSmooth(bool bSmooth) = 0;

		/* 障碍 */
		virtual bool SetMapMaze(const void* mazeBuffer, size_t size) = 0;
		virtual bool SetMazeMask(const unsigned char* pData, const NuclearRect &rect) = 0;//rect左闭右开上闭下开，格子坐标
		virtual unsigned int GetMazeMask(int x, int y) = 0;//逻辑坐标

		/* 哪种障碍用什么颜色 */
		virtual void SetMazeColors(const XPMazeColors &colors) = 0;
		virtual const XPMazeColors& GetMazeColors() const = 0;

		virtual void SetMaskBoxColor(const NuclearColor &color = 0xffffffff) = 0;
		virtual const NuclearColor& GetMaskBoxColor() const = 0;

		/* 外部需要的地图A*方法 */
		virtual bool GetAStartPath(astar::Path& path, const Nuclear::NuclearLocation& start, const Nuclear::NuclearLocation& end) = 0; // 输入起始点的逻辑坐标, 返回包含起始点、终点的路径

		/* 世界模式 */
		virtual void SetWorldMode(eNuclearWorldMode m) = 0;
		virtual eNuclearWorldMode GetWorldMode() const = 0;

		virtual WarBackgroundHandle LoadWarBackgound( const std::wstring &name ) = 0;
		virtual void FreeWarBackgroundHandle (WarBackgroundHandle handle) = 0;
		virtual bool SetWarBackground(WarBackgroundHandle handle, NuclearWarBackgroundType type) = 0;
        bool SetWarBackground(WarBackgroundHandle handle) { return SetWarBackground(handle, XPWBT_CENTER); }
		virtual void FreeAllWarBackground() = 0;
		virtual void SetWarBackgroundEdge(float fEdge) = 0;//这个只有在XPWBT_SCALE的情况下才生效
		virtual float GetWarBackgroundEdge() const = 0;

		// 获取当前的SortTick，当这个值 % XP_SORT_FREQUENCE == 0时，会全排序（当查到的时候就已经排序完了。。。。）
		virtual int GetSortTick() const = 0;

		/* 精灵 */
		virtual ISprite* NewSprite(NuclearSpriteLayer layer, const std::wstring &modelname) = 0;
		virtual bool AttachSprite(ISprite *pHostSprite, ISprite *pClientSprite, const std::wstring &hostSocket, const std::wstring &clientSocket, const NuclearVector3 &relpos, unsigned int sign) = 0;
        bool AttachSprite(ISprite *pHostSprite, ISprite *pClientSprite, const std::wstring &hostSocket, const std::wstring &clientSocket, unsigned int sign){return AttachSprite(pHostSprite, pClientSprite, hostSocket, clientSocket, NuclearVector3(0.0f, 0.0f, 0.0f), sign);}
		virtual bool DetachSprite(NuclearSpriteLayer layer, ISprite *pHostSprite, ISprite *pClientSprite) = 0;
		virtual void DeleteSprite(ISprite* sprite) = 0;
		virtual void DeleteAllSprite(NuclearSpriteLayer layer, bool keepAttached = false) = 0; // keepAttached 是否保留主角。
		virtual void MoveSpriteLayer(ISprite *pSprite, NuclearSpriteLayer fromlayer, NuclearSpriteLayer tolayer) = 0;

        ISprite* NewSprite(const std::wstring &modelname){return NewSprite(XPSL_MIDDLE1, modelname);}
        bool DetachSprite(ISprite *pHostSprite, ISprite *pClientSprite){return DetachSprite(XPSL_MIDDLE1, pHostSprite, pClientSprite);}
		void DeleteAllSprite(bool keepAttached = false){DeleteAllSprite(XPSL_MIDDLE1, keepAttached);}

		/* 战斗状态的精灵 */
		ISprite* NewWarSprite(const std::wstring &modelname){return NewSprite(XPSL_BATTLE, modelname);}
		bool AttachWarSprite(ISprite *pHostSprite, ISprite *pClientSprite, const std::wstring &hostSocket, const std::wstring &clientSocket, unsigned int sign){return AttachSprite(pHostSprite, pClientSprite, hostSocket, clientSocket, NuclearVector3(0.0f, 0.0f, 0.0f), sign);}
		bool DetachWarSprite(ISprite *pHostSprite, ISprite *pClientSprite){return DetachSprite(XPSL_BATTLE, pHostSprite, pClientSprite);}
		void DeleteWarSprite(ISprite* sprite){DeleteSprite(sprite);}
		void DeleteAllWarSprite(){DeleteAllSprite(XPSL_BATTLE, false);}

		//精灵的影子的参数
		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const = 0;
		virtual void SetSpriteShadowParam(const float &shearX, const float &scalingY) = 0;

		/* 扔在地图上的物件，如掉落物品、矿物.. */
		virtual IImmovableObj* NewImmovableObj(const std::wstring &objname, int layer/*目前必须为0，只有地表面一层*/, NuclearColor color = 0xffffffff, float freq=1.0f) = 0;
		virtual void DeleteImmovableObj(IImmovableObj* immobj) = 0;
		virtual void DeleteAllImmovableObj() = 0;
		
		virtual bool SelectObjs(const NuclearLocation &loc, std::vector<ISelectableObj*> &ettobjs) = 0;

		//直接在地图上播放的 effect 持续的效果
		virtual IEffect* SetLinkedEffect(const std::wstring &name, Nuclear_EffectLayer layer, const NuclearPoint &pt1, const NuclearPoint &pt2, float time, bool async) = 0;// time < 0代表循环，time > 0代表发射器会在time秒之后停止发射，等所有粒子死了之后再重新开始。这个只能是粒子特效
		virtual IEffect* SetEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, bool async) = 0;
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, Nuclear_EffectLayer layer, int x, int y, bool async) = 0;
		virtual void RemoveEffect(IEffect* pEffect) = 0;
		virtual void RemoveEffectEx(IEffect* pEffect) = 0;

		//一次性效果, times是播放次数, 一般用来表现一次性伤害或者范围伤害,如果特效有声音，soundtype就是声音的类型
		virtual IEffect* PlayEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, int times, bool async, unsigned char soundtype) = 0;
		
		//战斗的效果。。。。。layer只有3层，不支持XPEL_TOP,持续的效果
		IEffect* SetWarEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, bool async) { return SetEffect(name, transformWarLayer2NewLayer(layer), x, y, async); }

		IEffect* SetWarContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, Nuclear_EffectLayer layer, int x, int y, bool async){return SetContinueEffect(headEffect, midEffect, endEffect, continueTime, transformWarLayer2NewLayer(layer), x, y, async);}

		void RemoveWarEffect(IEffect* pEffect) { RemoveEffect(pEffect); }

		virtual void pauseSceneEffects(){}
		virtual void resumeSceneEffects(){}
		virtual void showSceneEffects(bool bshow){}

		virtual void AttachCameraTo(ISprite* sprite) = 0;
		virtual const NuclearRect& GetViewport() const = 0; //返回camera的left, top, right, bottom
		virtual void SetViewport(const NuclearLocation &loc/* 目的视窗中心，也就是要传送主角到的位置*/, bool syncRsrc/* 是否先加载资源*/) = 0;
		virtual void SetViewportLT(int left, int top) = 0; // 为编辑器添加，不绑精灵的时候用此函数移动视口；


		virtual void SetGameTime(float time) = 0;	//设置游戏白天黑夜时间，[0~1)，均匀变化，0代表深夜12点，0.5代表中午12点
		virtual float GetTempGameTime() const = 0;		//获取游戏白天黑夜时间，[0~1)，均匀变化，0代表深夜12点，0.5代表中午12点
		virtual void SetGameTimeCycle(int time) = 0;	//设置游戏逻辑时间一天（24小时）的长度，单位毫秒

		virtual bool SetCameraUpdateType(NuclearCameraUpdateType type, void* pParams = NULL) = 0;
		virtual NuclearCameraUpdateType GetCameraUpdateType() const = 0;
		virtual bool PrefetchMapRes(const std::wstring &mapname, const std::wstring &mazename, const NuclearPoint &center, bool autoSwitch) = 0;
		void PrefetchMapRes(const NuclearPoint &center){PrefetchMapRes(L"", L"", center, false);}
		virtual void CancelPrefetch() = 0;

	private:
		IWorld(const IWorld&);
		IWorld& operator=(const IWorld&);

	};
}


#endif