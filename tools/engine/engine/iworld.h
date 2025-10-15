#ifndef __Nuclear_IWORLD_H
#define __Nuclear_IWORLD_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "common\xptypes.h"
#include "ieffect.h"
#include "isprite.h"
#include "iimmovableobj.h"
namespace XMLIO
{
	class CINode;
}
namespace Nuclear
{
	class PStepSoundMap;
	enum {
		XPEFFECT_WAR_LAYER_COUNT = XPEL_TOP,
		XPEFFECT_LAYER_COUNT,
	};

	//地图跳转效果
	enum XPLOADMAP_EFFECT
	{
		XPLOADMAP_NOEFFECT,
		//XPLOADMAP_JUMP
	};

	// Load地图参数
	struct XPLoadmapParam
	{
		Location objLoc;	//跳转目标位置
		virtual XPLOADMAP_EFFECT GetType() const
		{
			return XPLOADMAP_NOEFFECT;
		}
		virtual ~XPLoadmapParam() {}
	};

	//// Load地图跳跃效果参数
	//struct XPLoadmapJumpParam : public XPLoadmapParam
	//{
	//	unsigned int transID;
	//	virtual XPLOADMAP_EFFECT GetType() const
	//	{
	//		return XPLOADMAP_JUMP;
	//	}
	//	virtual ~XPLoadmapJumpParam() {}
	//};

	// 游戏世界

	class IWorld
	{
		// 构造析构
	public:
		IWorld() {}
		virtual ~IWorld() {  }

		// 接口
	public:
		__declspec(deprecated) bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param)
		{
			return LoadMap(mapname, mazename, param, false);
		}
		virtual bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, bool async) = 0;	
		virtual bool UnloadMap() = 0;				// 卸载地图

		virtual bool GetMapSize(CPOINT &size) const = 0;

		/* 飞行 */
		virtual void SetScale(float scale) = 0;
		virtual float GetScale () const = 0;

		/* 障碍 */
		virtual bool SetMapMaze(const void* mazeBuffer, size_t size) = 0;
		virtual bool SetMazeMask(const unsigned char* pData, const CRECT &rect) = 0;//rect左闭右开上闭下开，格子坐标
		virtual unsigned int GetMazeMask(int x, int y) = 0;//逻辑坐标

		/* 哪种障碍用什么颜色 */
		virtual void SetMazeColors(const XPMazeColors &colors) = 0;
		virtual const XPMazeColors& GetMazeColors() const = 0;

		/* 声音地图（只限SoundEditor使用） */
		virtual bool SetSoundAreas(XMLIO::CINode& root, PStepSoundMap* pSSM) = 0;

		/* 外部需要的地图A*方法 */
		virtual bool GetAStartPath(astar::Path& path, const Nuclear::Location& start, const Nuclear::Location& end) = 0; // 输入起始点的逻辑坐标, 返回包含起始点、终点的路径

		/* 战斗状态 */
		__declspec(deprecated) void SetWarMode(bool m) { SetWorldMode(m?XPWM_SIMPLE_BATTLE:XPWM_NORMAL); }
		__declspec(deprecated) bool IsInWarMode() const { return ((GetWorldMode() == XPWM_SIMPLE_BATTLE) || (GetWorldMode() == XPWM_WORLD_BATTLE)); }

		/* 世界模式 */
		virtual void SetWorldMode(XPWorldMode m) = 0;
		virtual XPWorldMode GetWorldMode() const = 0;

		//应该是pfs路径，基本图片格式，dds、png之类的。如果没有会同步读取资源
		virtual WarBackgroundHandle LoadWarBackgound( const std::wstring &name ) = 0;
		virtual void FreeWarBackgroundHandle (WarBackgroundHandle handle) = 0;
		virtual bool SetWarBackground(WarBackgroundHandle handle) = 0;
		virtual void FreeAllWarBackground() = 0;

		/* 普通状态的精灵 */
		virtual ISprite* NewSprite(const std::wstring &modelname) = 0;
		//virtual ISprite* CloneSprite(const ISprite* spr) = 0;
		virtual void DeleteSprite(ISprite* sprite) = 0;
		virtual void DeleteAllSprite(bool keepAttached = false) = 0; // keepAttached 是否保留主角。

		/* 战斗状态的精灵 */
		virtual ISprite* NewWarSprite(const std::wstring &modelname) = 0;
		virtual void DeleteWarSprite(ISprite* sprite) = 0;
		virtual void DeleteAllWarSprite() = 0;

		//精灵的影子的参数
		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const = 0;
		virtual void SetSpriteShadowParam(const float &shearX, const float &scalingY) = 0;

		/* 扔在地图上的物件，如掉落物品、矿物.. */
		virtual IImmovableObj* NewImmovableObj(const std::wstring &objname, int layer/*目前必须为0，只有地表面一层*/, XPCOLOR color = 0xffffffff, float freq=1.0f) = 0;
		virtual void DeleteImmovableObj(IImmovableObj* immobj) = 0;
		virtual void DeleteAllImmovableObj() = 0;

		//这个方法在OnBeforeRender里面调用无效，因为这个时候缓存已经清了
		virtual bool SelectObjs(const Location &loc, std::vector<ISelectableObj*> &ettobjs) = 0;

		/* 直接在地图上播放的 effect */
		//持续的效果
		virtual IEffect* SetLinkedEffect(const std::wstring &name, XPEFFECT_LAYER layer, const CPOINT &pt1, const CPOINT &pt2, float time, bool async) = 0;// time < 0代表循环，time > 0代表发射器会在time秒之后停止发射，等所有粒子死了之后再重新开始。这个只能是粒子特效
		virtual IEffect* SetEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async) = 0;
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async) = 0;
		virtual void RemoveEffect(IEffect* pEffect) = 0;

		//一次性效果, times是播放次数, 一般用来表现一次性伤害或者范围伤害。
		//layer 由策划
		//a) 地表之上，精灵之下。                      // 如：地面燃烧，炸裂
		//b) 精灵和遮挡层，引擎根据底座自动判断遮挡。  // 如：突起的冰柱
		//c) 精灵之上。                                // 如：闪电，小范围
		//d) 最上层。                                  // 如：暴风雪，大范围
		// 如果特效有声音，soundtype就是声音的类型
		virtual void PlayEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype) = 0;
		
		//战斗的效果。。。。。layer只有3层，不支持XPEL_TOP
		//持续的效果
		virtual IEffect* SetWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async) = 0;
		virtual IEffect* SetWarContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async) = 0;
		virtual void RemoveWarEffect(IEffect* pEffect) = 0;

		//一次性效果, times是播放次数, 一般用来表现一次性伤害或者范围伤害。
		// 如果特效有声音，soundtype就是声音的类型
		virtual void PlayWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype) = 0;
		//战斗的效果结束。。。。。


		

		virtual void AttachCameraTo(ISprite* sprite) = 0;
		virtual CRECT GetViewport() = 0; //返回camera的left, top, right, bottom
		virtual void SetViewport(const Location &loc/* 目的视窗中心，也就是要传送主角到的位置*/, bool syncRsrc/* 是否先加载资源*/) = 0;
		virtual void SetViewportLT(int left, int top) = 0; // 为编辑器添加，不绑精灵的时候用此函数移动视口；


		virtual void SetGameTime(float time) = 0;	//设置游戏白天黑夜时间，[0~1)，均匀变化，0代表深夜12点，0.5代表中午12点
		virtual float GetTempGameTime() const = 0;		//获取游戏白天黑夜时间，[0~1)，均匀变化，0代表深夜12点，0.5代表中午12点
		virtual void SetGameTimeCycle(int time) = 0;	//设置游戏逻辑时间一天（24小时）的长度，单位毫秒

		//virtual void FocusTo(Location ptCurrent,Location ptTarget) = 0;
		virtual bool SetCameraUpdateType(XPCAMERA_UPDATE_TYPE type) = 0;
		virtual XPCAMERA_UPDATE_TYPE GetCameraUpdateType() const = 0;

		//mapname如果是L""，那么就是本地图，center是预加载的中心点
		//调用了PrefetchMapRes方法之后，只要没有跳过地图，
		//这块区域的资源就会以异步的方式加载进来，并且一直不释放，
		//除非PrefetchMapRes了另外一块区域，或者CancelPrefetch，或者跳了地图
		virtual bool PrefetchMapRes(const std::wstring &mapname, const std::wstring &mazename, const CPOINT &center, bool autoSwitch) = 0;
		__declspec(deprecated) void PrefetchMapRes(const CPOINT &center)
		{
			PrefetchMapRes(L"", L"", center, false);
		}
		virtual void CancelPrefetch() = 0;

		// 占坑
	private:
		IWorld(const IWorld&);
		IWorld& operator=(const IWorld&);

	};


}


#endif