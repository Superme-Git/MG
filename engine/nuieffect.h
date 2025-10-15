#ifndef __Nuclear_IEFFECT_H__
#define __Nuclear_IEFFECT_H__

#if _MSC_VER > 1000
#pragma once
#endif
#include <set>
#include "common/nuxptypes.h"
#include "particlesystem/nuiparticlesyscommon.h"

namespace Nuclear
{
	class IEffect;
	typedef std::set<IEffect*> IEffectSet;

	class IEffectNotify
	{
	public:
		virtual void OnStop(IEffect *pEffect) {}
		virtual void OnEnd(IEffect *pEffect) {}
		virtual void OnDelete(IEffect *pEffect) {}	//如果客户手动在world或者在sprite身上remove的特效，这个回调不会调出来
		virtual void OnAsyncLoaded(IEffect *pEffect, bool succeeded) {}//有可能加载失败（文件错误，或者找不到资源）
		virtual ~IEffectNotify() {}
	};
	class IParticleEffect;

	class IEffect
	{
	public:
		IEffect(){ m_isSpine = false; }
		virtual ~IEffect() { }

		bool m_isSpine;

		//播放状态控制
		virtual bool Play() = 0;
		virtual bool Stop() = 0;
		virtual bool Pause() = 0;
		virtual bool Resume() = 0;

		// 设置位置。location 世界像素坐标。
		virtual void SetLocation(const NuclearLocation& location) = 0;
		// 得到位置.
		virtual NuclearLocation GetLocation() const = 0;

		// 设置和得到方向
		virtual void SetDirection( NuclearDirection direction ) = 0;
		virtual NuclearDirection GetDirection() const = 0;

		// 物体像素座标
		virtual void SetDirection(const NuclearFPoint &target) = 0;
		void SetDirection(float targetx, float targety) { SetDirection(NuclearFPoint(targetx, targety)); }		

		// 旋转
		virtual void SetRotationRadian( float radian) = 0;
		virtual float GetRotationRadian() const = 0;

		// 缩放
		virtual void SetScale( float scaleX, float scaleY) = 0;
		void SetScale( float scale)	{ SetScale(scale, scale); }
		void SetScale( const NuclearFPoint& scale) { SetScale(scale.x, scale.y); }
		virtual const NuclearFPoint& GetScale() const = 0;

		// 颜色
		virtual void SetVertexColor( NuclearColor color) = 0;
		virtual NuclearColor GetVertexColor() const = 0;

		// 获得包围盒
		virtual const NuclearRect& GetRelBouningBox() const = 0;

		// 特效挂点
		virtual void SetEffectBindType(Nuclear_EffectBindType t) = 0;
		virtual Nuclear_EffectBindType GetEffectBindType() const = 0;

		// 如果特效里面有声音，声音的类型和优先级。注意：要在声音播放出来之前设置，否则无效
		virtual void SetSoundType(unsigned char type) = 0;
		virtual unsigned char GetSoundType() const = 0;
		virtual void SetSoundPriority(short priority) = 0;
		virtual short GetSoundPriority() const = 0;

		// 获得状态
		virtual Nuclear_EffectState GetPlayState() const = 0;

		virtual const IParticleEffect* TryConvertToParticle() const = 0;
		virtual IParticleEffect* TryConvertToParticle() = 0;

		// Notify
		virtual void AddNotify(IEffectNotify* pNotify) = 0;
		virtual void RemoveNotify(IEffectNotify* pNotify) = 0;
		virtual void ClearNotify() = 0;//这个在引擎内部使用，引擎外不要调用

		// Update，正常情况下是不需要调用的，如果你不知道该不该调用，那么就不该调用。。。- -b
		// 只有在某些特殊的情况下，引擎没有统一Update特效的时候（例如同步Loading）才需要调用
		virtual bool Update(DWORD tickTime) = 0;	//返回false就代表播放完毕了

		// 仅用于骨骼动画
		virtual void SetDefaultActName(const char* szActName) {}
	};
	enum XPParticleEffectCycleMode
	{
		XPPCM_NO_CYCLE = 0,			//这个值不能Set，如果不是循环模式，调用GetCycleMode会返回这个值
		XPPCM_ALWAY_EMISSION = -1,	//发射器永远在发射
		XPPCM_PULSE_EMISSION = -2,	//发射器过了设定的生命周期之后会停止发射，等粒子死亡之后再重新发射
	};

	class IParticleEffect
	{
	public:
		virtual ~IParticleEffect() {}
		// 粒子系统弹道跟踪时的目标点
		virtual void ModifyObjectPoint(const NuclearFPoint &pt) = 0;
		
		//////////////////////////////发射器范围改变之后要相应地修改粒子数和发射频率////////////////////////
		// 线发射的线长度
		virtual void ModifyLineLength(float length) = 0;
		// 矩形发射区域的宽高
		virtual void ModifyRectWH(float width, float height) = 0;
		//////////////////////////////发射器范围改变之后要相应地修改粒子数和发射频率////////////////////////

		//粒子盲区设置---进入该区域的粒子就不可见了..一系列相对于发射器中心的矩形区域
		virtual void SetRctBlindAreas(const std::vector<NuclearRect> &vet) = 0;

		//设置循环模式（如果是XPPCM_NO_CYCLE就return false），此方法会重置SysLife
		virtual bool SetCycleMode(XPParticleEffectCycleMode mode) = 0;
		virtual XPParticleEffectCycleMode GetCycleMode() const = 0;

		//设置粒子系统的生命长度（秒），-1代表默认，只在XPPCM_PULSE_EMISSION的时候起作用
		virtual bool SetSysLife(float time) = 0;
		virtual float GetSysLife() = 0;

		//如果是特效链粒子，那么可以设链的两个端点，type==0代表是起点，type==1代表终点
		//如果不是特效链粒子，那么返回false
		virtual bool SetEmitterLinkPoint(const NuclearFPoint &pt, int type) = 0;
		virtual bool GetEmitterLinkPoint(NuclearFPoint &pt, int type) = 0;

		// 获得指定粒子特效当前在渲染的粒子数
		virtual int GetNumActiveParticles() const = 0;
		// 粒子的可能存活最大时间(秒)
		virtual float GetParticleMaxLife() const = 0;

		//以下针对特殊粒子特效的设置..
		// 设置纹理, 以及纹理的最大u、v坐标(烂显卡上创建没有纹理过滤的非2幂纹理)
		virtual bool SetSpecialPsTexture(PictureHandle ShapesTexture, float fu=1.0f, float fv=1.0f) = 0;
		// 获得特殊特效使用的纹理handle (方便外面删除等操作)
		virtual PictureHandle GetSpecialPSTextureHandle() const = 0;
		// 设置特殊特效缩放比例(小图贴大屏幕或者反之..)
		virtual bool SetSpecialPsScale(float fcx=1.0f, float fcy=1.0f) = 0;
		// 转场特效静默时间ftime,单位:秒 在这段时间内保持原始图片的状态。
		virtual bool SetSilentTime(float ftime) = 0;

		// 转回IEffect
		virtual IEffect* ConvertToIEffect() = 0;
		virtual const IEffect* ConvertToIEffect() const = 0;
	};

}

#endif