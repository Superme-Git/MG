#ifndef __Nuclear_PARTICLEEFFECT_H__
#define __Nuclear_PARTICLEEFFECT_H__

#include "nueffect.h"
#include "../particlesystem/nuiparticlemanager.h"

namespace Nuclear
{
	class ParticleEffect : public IParticleEffect, public Effect, public ParticleLoadingNotify
	{
	//自定义类型、友元、常量
	private:
		friend class EffectManager;
	//数据成员们
	private:
		ParticleSystemHandle m_handle;
		PSParameter	m_parmeter;
		int	m_nPlayOnceTime;	//播放一遍的时间（毫秒）
		NuclearVector4		m_coloration;		//外面设进来的颜色
		NuclearColor			m_vertexColor;
	
	//构造析构
	protected:
		ParticleEffect(EngineBase* pEB);
	public:
		virtual ~ParticleEffect(void);

	//方法们
		virtual void SetScaleType(Nuclear_EffectScaleType t);
		virtual Nuclear_EffectScaleType GetScaleType() const { return m_parmeter.m_psModifyParam.m_scaleType; }
		virtual void OnReady(bool success);
		virtual Nuclear_EffectAssureResResult AssureResource(bool async);
		virtual void Release();
		const PSParameter& GetCreateParmeter() const { return m_parmeter; }
		virtual bool Update(DWORD tickTime);
		virtual int LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode);
		virtual Nuclear_EffectType GetType() const { return XPET_PARTICLE; }

		virtual void SetEffectBindType(Nuclear_EffectBindType t);
		virtual Nuclear_EffectBindType GetEffectBindType() const;

		virtual void SetScreenCrood(bool b);
		virtual bool IsScreenCrood() const { return m_parmeter.m_bScreenCoord; }
		virtual void SetLocation(const NuclearLocation& location);

		//! 渲染
		virtual bool RenderBottom(Canvas* canvas)
		{
			return true;
		}
		virtual bool Render(Canvas* canvas, bool realRender);
		bool CollectRender();//粒子收集以供批绘制

		//! 小于0表示无限循环，并且会设成XPPCM_ALWAY_EMISSION循环，要改为XPPCM_PULSE_EMISSION，得调用SetCycleMode接口
		virtual bool		 SetLoopNum( int loopNum);
		virtual bool		 SetPlayTime( DWORD time); //毫秒

		//设置循环模式（如果是XPPCM_NO_CYCLE就return false）
		virtual bool SetCycleMode(XPParticleEffectCycleMode mode);
		virtual XPParticleEffectCycleMode GetCycleMode() const;

		//设置粒子系统的生命长度（秒），-1代表默认，只在XPPCM_PULSE_EMISSION的时候起作用
		virtual bool SetSysLife(float time);
		virtual float GetSysLife();

		//如果是特效链粒子，那么可以设链的两个端点，type==0代表是起点，type==1代表终点
		//如果不是特效链粒子，那么返回false
		virtual bool SetEmitterLinkPoint(const NuclearFPoint &pt, int type);
		virtual bool GetEmitterLinkPoint(NuclearFPoint &pt, int type);

		virtual bool Play();

		//! 获得播放一次的时长
		virtual int GetPlayOnceTime() const { return m_nPlayOnceTime; }

		virtual bool GetBase(NuclearBase &base);
		virtual void SetVertexColor( NuclearColor color) { m_vertexColor.data = color.data; }
		virtual void SetTransparent( float alpha) { m_vertexColor.a = static_cast<unsigned char>(alpha * 255); }
		
		virtual void Colorate( const NuclearVector4& color) { m_coloration = color; }
		virtual const NuclearVector4& GetColorate() const { return m_coloration; }

		virtual NuclearColor GetVertexColor() const { return m_vertexColor; }
		virtual float GetTransparent() const { return static_cast<float>(m_vertexColor.a) / 255.0f;}

		virtual const IParticleEffect* TryConvertToParticle() const { return this; }
		virtual IParticleEffect* TryConvertToParticle() { return this; }

		virtual const ParticleEffect* TryConvertToParticleEffect() const { return this; }
		virtual ParticleEffect* TryConvertToParticleEffect() { return this; }

		// 粒子系统弹道跟踪时的目标点
		virtual void ModifyObjectPoint(const NuclearFPoint &pt) { m_parmeter.m_psModifyParam.m_pointObject = pt; }

		//////////////////////////////发射器范围改变之后要相应地修改粒子数和发射频率////////////////////////
		// 线发射的线长度
		virtual void ModifyLineLength(float length) { m_parmeter.m_psModifyParam.m_fLineLength = length; }
		// 矩形发射区域的宽高
		virtual void ModifyRectWH(float width, float height)
		{
			m_parmeter.m_psModifyParam.m_fRctWidth = width;
			m_parmeter.m_psModifyParam.m_fRctHeight = height;
		}
		//////////////////////////////发射器范围改变之后要相应地修改粒子数和发射频率////////////////////////

		//粒子盲区设置---进入该区域的粒子就不可见了..一系列相对于发射器中心的矩形区域
		virtual void SetRctBlindAreas(const std::vector<NuclearRect> &vet) { m_parmeter.m_psModifyParam.m_vectorRctBlindArea = vet; }
		//阻挡线..粒子不能越过，如果粒子在可移动方向上没有速度就会表现为停止...
		virtual void SetBlockLines(const std::vector<LINE>& vet)  { m_parmeter.m_psModifyParam.m_vectorBlockLine = vet; }
		//反射线...粒子碰到之后按照光线发射规律改变运动方向
		virtual void SetReflectLines(const std::vector<LINE>& vet)  { m_parmeter.m_psModifyParam.m_vectorReflectLine = vet; }

		// 获得指定粒子特效当前在渲染的粒子数
		virtual int GetNumActiveParticles() const;
		// 获得包围盒等信息..
		virtual PSINFO GetParticleSystemInfo() const;
		// 获得指定粒子特效的创建参数，不能修改
		virtual const PSParameter& GetParticleSystemParameter() const { return m_parmeter; }
		virtual void SetParameterCycle(bool cycle) { m_parmeter.cycle = cycle; }
		virtual void SetParameterTime(float time) { m_parmeter.time = time; }
		// 粒子的可能存活最大时间(秒)
		virtual float GetParticleMaxLife() const;

		//以下针对特殊粒子特效的设置..
		// 设置纹理, 以及纹理的最大u、v坐标(烂显卡上创建没有纹理过滤的非2幂纹理)
		virtual bool SetSpecialPsTexture(PictureHandle ShapesTexture, float fu=1.0f, float fv=1.0f);
		// 获得特殊特效使用的纹理handle (方便外面删除等操作)
		virtual PictureHandle GetSpecialPSTextureHandle() const;
		// 设置特殊特效缩放比例(小图贴大屏幕或者反之..)
		virtual bool SetSpecialPsScale(float fcx=1.0f, float fcy=1.0f);
		// 转场特效静默时间ftime,单位:秒 在这段时间内保持原始图片的状态。
		virtual bool SetSilentTime(float ftime);

		// 转回IEffect
		virtual IEffect* ConvertToIEffect() { return this; }
		virtual const IEffect* ConvertToIEffect() const { return this; }

		virtual void Clone(Effect* p);
	};

}

#endif