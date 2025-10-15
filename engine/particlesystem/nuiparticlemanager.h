#ifndef __Nuclear_IPARTICLEMANAGER_H__
#define __Nuclear_IPARTICLEMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuiparticlesyscommon.h"

namespace Nuclear
{
	class ParticleLoadingNotify
	{
	public:
		virtual void OnReady(bool success) = 0;
	};

	struct PSL;

	class IParticleManager
	{	
	public:
		IParticleManager() { }
		virtual ~IParticleManager() { }

		// 更新viewport,当游戏视口发生变化时调用
		virtual void OnViewportUpdate(const NuclearRect &viewport) = 0;
		// 通过psl文件名(可以不带扩展名)创建一个粒子特效
		// bLoadRes为true, 表示创建特效的时候需要把纹理资源一起读入
		// bLookupSegmPak...创建特效前要先查一下这个psl的状态, 如果它是在主包, 那么创建的时候, 这个值是false;
		// 如果它在子包, 那么就是true; 如果它还没有下载下来, 就不能创建这个特效;
		virtual ParticleSystemHandle CreateParticleSystem(const std::wstring& pslName, const PSParameter& param, bool bLoadRes, bool bLookupSegmPak) = 0;
		// 删除一个粒子系统(对应资源的引用计数减一)
		virtual bool DeleteParticleSystem(ParticleSystemHandle handle) = 0;
	
		// 释放一个粒子系统的纹理资源(对应资源的引用计数减一)、还有为其分配的粒子存储空间
		virtual bool ReleaseParticleSystemRes(ParticleSystemHandle handle) = 0;

		// 重置粒子系统系统参数, todo？
		virtual bool ResetParticleSystem(ParticleSystemHandle handle) = 0;
		// 修改粒子系统参数, 比如位置等...
		virtual bool ModifyParticleSystem(ParticleSystemHandle handle, const PSModifyParam& param) = 0;
		// bUpdated为true表示render之前已经调用过update了, 返回false表示非循环特效一遍播完..
		virtual bool RenderParticleSystem(ParticleSystemHandle handle, bool bUpdated = false, bool bEndScene = true) = 0;
		// 渲染指定层的所有粒子特效
		virtual bool RenderParticleSystemByLayer(int layer) = 0;
		// 获得当前加载的所有粒子特效数量
		virtual int GetParticleSystemCount() const = 0;
		// 获得当前渲染的所有粒子数
		virtual int GetTotalNumActiveParticles() const = 0;
		// 获得指定粒子特效当前在渲染的粒子数
		virtual int GetNumActiveParticles(ParticleSystemHandle handle) const = 0;
		// 获得包围盒等信息..
		virtual PSINFO GetParticleSystemInfo(ParticleSystemHandle handle) const = 0;
		// 获得指定粒子特效的创建参数 //只允许通过ModifyParticleSystem()修改PSParameter
		virtual const PSParameter* GetParticleSystemParameter(ParticleSystemHandle handle) const = 0;
		// 获得粒子系统系统生命(从psl中获取)
		virtual float GetSysLife(ParticleSystemHandle handle) const = 0;
		// 粒子的可能存活最大时间(秒)
		virtual float GetParticleMaxLife(ParticleSystemHandle handle) const = 0;
		//在创建了特效但是播放之前调用修改...
		virtual bool ModifyParticleSystem(ParticleSystemHandle handle, const PSParameter& param) = 0;
		// 每帧调用, 在这里更新粒子的位置颜色等状态. fTimeDelta单位：秒
		virtual bool UpdateParticleSystem(ParticleSystemHandle handle, float fTimeDelta) = 0;
	
		// 技能特效是否使用精灵上设置的挂点(从psl中获取)（如果不使用,默认特效挂在精灵的脚上）
		virtual Nuclear_EffectBindType GetEffectBindType(ParticleSystemHandle handle) const = 0;

		// 检查纹理资源是否已经加载进来了... 默认false... 表示同步读取资源
		virtual Nuclear_EffectAssureResResult EnsureParticleSystemResouce(ParticleSystemHandle handle, bool async = false, ParticleLoadingNotify* pNotify = NULL) = 0;

		//以下针对特殊粒子特效的设置..
		// 设置纹理, 以及纹理的最大u、v坐标(烂显卡上创建没有纹理过滤的非2幂纹理)
		virtual bool SetSpecialPsTexture(ParticleSystemHandle handle, PictureHandle ShapesTexture, float fu=1.0f, float fv=1.0f) = 0;
		// 获得特殊特效使用的纹理handle (方便外面删除等操作)
		virtual PictureHandle GetSpecialPSTextureHandle(ParticleSystemHandle handle) const = 0;
		// 设置特殊特效缩放比例(小图贴大屏幕或者反之..)
		virtual bool SetSpecialPsScale( ParticleSystemHandle handle, float fcx=1.0f, float fcy=1.0f) = 0;
		// 转场特效静默时间ftime,单位:秒 在这段时间内保持原始图片的状态。
		virtual bool SetSilentTime(ParticleSystemHandle handle, float ftime) = 0;

		//最多允许粒子系统缓存nMaxSize兆的资源, 超标之后..最近一次使用是fTime分钟前的就释放掉
		virtual void GarbageCollect() = 0;
		//fMaxSize单位M，nTime单位分钟
		virtual void SetGCParam(float fMaxSize, float fTime) = 0;
		// 设置系统路径...通过这个参数在创建粒子系统的时候才能找到正确的资源路径
		virtual void SetParticlePath(const std::wstring& pslPath) = 0;
		// 获得出错信息. 目前仅仅在创建特效的时候对各种资源加载错误有简单信息记录
		virtual PSL_ERRORTYPE GetLastError() const = 0;
		// 设置机子配置--//高2 中1 低0
		virtual void SetSystemConfig(NuclearComputerConfigType nParam) = 0;
		// 暂停特效渲染
		virtual void SetStopFlag(ParticleSystemHandle handle, bool bStop) = 0;
		// 插入notify
		virtual bool InsertParticleNotify(ParticleSystemHandle handle, IParticleNotify* pParticleNotify) = 0;
		// 移除notify
		virtual bool RemoveParticleNotify(ParticleSystemHandle handle, IParticleNotify* pParticleNotify) = 0;
	
		virtual void SetLogPath(const std::wstring& logPath) = 0;

		virtual bool SetKeyPointIndex(ParticleSystemHandle handle, int nIndex) = 0;

		virtual bool OnResetDevice() = 0;

		virtual bool LoadPSLFromMem(const NuclearBuffer& data, PSL* ppsl) = 0;

		virtual bool Enabled() = 0;
		virtual void SetEnabled(bool flag) = 0;

	private:
		IParticleManager(const IParticleManager&);
		IParticleManager& operator=(const IParticleManager&);
	};

}

#endif