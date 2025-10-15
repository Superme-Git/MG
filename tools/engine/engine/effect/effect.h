#ifndef __Nuclear_EFFECT_H
#define __Nuclear_EFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\ieffect.h"
#include "..\particlesystem\iparticlesyscommon.h"
#include "..\engine\renderable.h"

//也就是说，如果是一个8方向的特效，对于每个子特效来说，就会有7*8条曲线，别的和单方向的特效就没有区别了
//全局的位置和颜色就各个方向都一样。

//这个先缓一下…………

namespace XMLIO
{
	class CINode;
	class CONode;
}

namespace Nuclear
{
	const static int MAX_EFFECT_RADIUS = 128;

	class EffectManager;
	class ParticleEffect;

	enum
	{
		EFFECT_IO_NORMAL			= 0x00000000,		//! 正常版本
		EFFECT_IO_EDIT				= 0x00000001,		//! 编辑器版本
		EFFECT_IO_ALONE_AUDIO_FILE	= 0x00000002,		//! 单独保存声音文件
	};

	//Effect 里面的Clip记录的是帧，但是是以时间驱动而不是帧驱动，有可能会跳帧。
	//也就是说，在只修改fps的时候，总播放时间不变，特效的总帧数会发生变化
	class Effect : public IEffect, public Renderable
	{
	//自定义类型、友元、常量
	private:
		 friend class EffectManager;
		 typedef std::set<IEffectNotify*> NotifySet;
	public:
		
	//属性们
	protected:
		CRECT			m_relBBox;			//! 相对包围盒
		std::wstring	m_name;				//! 特效名字
		XPPLAY_MODE		m_playMode;			//! 播放方式
		union{
			int			m_nPlayTime;		//! 总共播放的时间
			int 		m_nLoopNum;			//! 循环次数
		};
											//  如果m_playMode是XPPM_LOOP，那么m_nLoopNum是-1，
											//  如果是XPPM_LOOPNUM，那么应该用m_nLoopNum
											//  如果是XPPM_TIME，那么用m_nTotalTime

		// 有关运行时变量
		Location		m_pos;				//! 位置，是它和父亲之间的位置（有可能是GEffect，也有可能是世界）
		FPOINT			m_translate;		//! 这个是它父亲在世界上的位置（很可能会是层层积累下来的）
		FPOINT			m_scale;			//! 缩放
		float			m_rotationRadian;	//! 旋转
		XPDIRECTION		m_dir;				//! 特效的方向
		XPEffectState	m_state;			//! 状态
		int				m_curLoopNum;		//! 当前循环次数
		int				m_nCurTime;			//! 由每次update的时候累积起来（毫秒）
		NotifySet		m_SetPNotifies;		//! 回调通告

	//方法们

	protected:
		Effect(EngineBase* pEB) : Renderable(pEB), m_scale(1.0f, 1.0f), m_rotationRadian(0.0f),
			m_state(XPES_EMPTY) {}	//只能由 EffectManager来构造
	public:
		virtual ~Effect() 
		{
			NotifyDelete();
		}

	public:	//上层根本拿不到Effect对象，所以就public吧
		virtual XPEffectAssureResResult AssureResource(bool async) = 0;
		virtual void Release() = 0;
		//virtual void Refresh() = 0;	//编辑器使用

		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t) = 0;
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const = 0;

		virtual void AddNotify(IEffectNotify* pNotify) { m_SetPNotifies.insert(pNotify); }
		virtual void RemoveNotify(IEffectNotify* pNotify) { m_SetPNotifies.erase(pNotify); }
		virtual void ClearNotify() { m_SetPNotifies.clear(); }

		virtual void HoldRes() {}

		//effect create出来的时候当前时间是负的，update的时候如果发现当前时间为负，就用0来update，并且改当前时间为0
		virtual bool Update(DWORD tickTime) = 0;	//返回false就代表播放完毕了
		virtual int LoadAttribute(XMLIO::CINode& node) = 0;
		virtual int SaveAttribute(XMLIO::CONode& node) = 0;

		//播放状态控制
		virtual bool Play();
		virtual bool Stop();
		virtual bool Pause();
		virtual bool Resume();
		//! 渲染
		virtual bool Render(Canvas* canvas) = 0;
		//! 渲染前端（前端后渲染，也就是说能挡住人）
		virtual void RenderFront(Canvas* canvas) { Render(canvas); }	//在非GEffect的类里面，如果是绑定在人身上（那么就不会调用Render方法），那么他就肯定能挡住人
		//! 渲染后端（后端先渲染，也就是说能被人挡住）
		virtual void RenderBack(Canvas* canvas) {}
		
		virtual XPEffectState GetPlayState() const { return m_state; }
		const std::wstring &GetName() const { return m_name; }

		virtual void SetScreenCrood(bool b) = 0;
		virtual bool IsScreenCrood() const = 0;

		virtual const CRECT& GetRelBouningBox() const { return m_relBBox; }

		virtual XPDIRECTION GetDirection() const { return m_dir; }
		Location GetLocation() const { return m_pos; }
		const FPOINT&	GetTranslation() const { return m_translate; }
		virtual const FPOINT&	GetScale() const { return m_scale; }
		virtual float GetRotationRadian() const { return m_rotationRadian; }
		virtual XPCOLOR GetVertexColor() const = 0;
		virtual float GetTransparent() const = 0;
		virtual const XPVECTOR4& GetColorate() const = 0;
		
		virtual void SetDirection( XPDIRECTION direction ) { m_dir = direction; }
		void SetLocation(const Location& location) { m_pos = location; }
		void SetLocation(int x, int y) { m_pos.x = x; m_pos.y = y;}
		void SetTranslation(const FPOINT& trans) { m_translate = trans; }
		virtual void SetScale( float scale)	{ m_scale.x = m_scale.y = scale; }
		virtual void SetScale( float scaleX, float scaleY) { m_scale.x = scaleX; m_scale.y = scaleY; }
		virtual void SetScale( const FPOINT& scale) { m_scale = scale; }
		virtual void SetRotationRadian( float radian) { m_rotationRadian = radian; }
		virtual void SetVertexColor( XPCOLOR color) = 0;
		virtual void SetTransparent( float alpha) = 0;
		virtual void Colorate( const XPVECTOR4& color) = 0;
		
		//void SetScreenCrood( bool screen) { }

		virtual XPEffectType GetType() const = 0;
		virtual XPPLAY_MODE	 GetPlayMode() const { return m_playMode; }
		virtual int			 GetLoopNum() const 
		{ 
			if ( m_playMode == XPPM_LOOPNUM)
				return m_nLoopNum;
			else if ( m_playMode == XPPM_LOOP)
				return -1;
			else
				return 0;
		}
		virtual int			 GetPlayTime() const 
		{ 
			if ( m_playMode == XPPM_LOOPNUM)
				return GetPlayOnceTime() * m_nLoopNum;
			else if ( m_playMode == XPPM_LOOP)
				return -1;
			else
				return m_nPlayTime;
		}
		//! 小于0表示无限循环
		virtual bool		 SetLoopNum( int loopNum);
		virtual bool		 SetPlayTime( DWORD time); //毫秒
		//! 获得在系统中的级别
		//virtual int		GetSystemLevel() const { return m_systemLevel; }
		//! 设置系统级别 0 基本 1 中等可无 2 华丽, 默认为基本级
		//virtual void	SetSystemLevel( int level) { m_systemLevel = level; }

		virtual int			 GetPlayOnceTime() const = 0; //毫秒
		virtual bool		 GetBase(XPBase &base) = 0;

		virtual const IParticleEffect* TryConvertToParticle() const { return NULL; }
		virtual IParticleEffect* TryConvertToParticle() { return NULL; }

		virtual const ParticleEffect* TryConvertToParticleEffect() const { return NULL; }
		virtual ParticleEffect* TryConvertToParticleEffect() { return NULL; }

		// 如果特效里面有声音，声音的类型和优先级。注意：要在声音播放出来之前设置，否则无效
		virtual void SetSoundType(unsigned char type) {}
		virtual unsigned char GetSoundType() const { return 0; }
		virtual void SetSoundPriority(short priority) {}
		virtual short GetSoundPriority() const { return 0; }
		virtual void RemoveAudio() {} //这个特效不播放声音
	protected:
		bool IsInfitLoop() const { return m_playMode == XPPM_LOOP; }
		bool IsLoopNum() const { return m_playMode == XPPM_LOOPNUM; }
		bool IsPlayTime() const { return m_playMode == XPPM_TIME; }
		
		void NotifyAsyncLoadResult(bool succ);
		void NotifyStop();
		void NotifyEnd();
		void NotifyDelete();
	};

};

#endif