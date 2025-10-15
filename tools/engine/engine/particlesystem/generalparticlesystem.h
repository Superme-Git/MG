#pragma once

#include "particlesystem.h"
#include "iparticlemanager.h"

namespace Nuclear
{
	class CGeneralParticleSystem :	public CParticleSystem
	{
	public:
		CGeneralParticleSystem(const XHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer,
			const std::vector<POINT>& vectorFullPathPoint, const std::wstring& pslName);

		virtual ~CGeneralParticleSystem(void);

	public:
		virtual bool Render(bool bUpdate = false, bool bEndScene = true); 
		virtual bool Update(float fTimeDelta); //允许外面更新粒子再调用render()
		
		void SetParticleSysResouce(const XHardRef<PSTEXTUREINFOSTRUCT>& pRefPSTextureInfo);
		void SetPicHandleRefResource(const std::vector<XHardRef<PSPICHANDLESTRUCT>>& vectorD3DTexture);

		virtual void Reset(void);

		virtual void ResetVerticesVector();
		void ResizeParticleArry(int nSysMaxParticles, int nEmissionRate = -1); //编辑器和文字背景特效需要用到

		void SetLoadedResFlag(bool bLoadedRes);
		bool GetLoadedResFlag();
		bool ReleaseResource();

		bool SetKeyPointIndex(int nIndex); //关键帧路径，当前使用到哪个关键点

		void SetParticleLoadingNotify(ParticleLoadingNotify* pNotify) { m_pLoadingNotify = pNotify; }
		ParticleLoadingNotify* GetParticleLoadingNotify() const { return m_pLoadingNotify; }

	public://编辑器专用...
		XHardRef<PSTEXTUREINFOSTRUCT> GetPSTextureInfo() { return m_pRefPSTextureInfo; } //返回之后修改..
		void SetPSLPathPointVector(const std::wstring& strPathFileName);
		bool IsLoadFaild() const { return m_bLoadFaild; }
		void SetLoadFaild() { m_bLoadFaild = true; }
		int m_restTexRes;//异步的时候，还省下多少资源没有加载，只有异步的时候用

	private:
//		template<class VERTEX>
//		bool Render(PictureHandle handle);

		//出于效率考虑--把std::vector<VERTEX> vertices;移到类成员变量里,取消了上面的模板函数
		bool Render_TCT(PictureHandle handle, bool bEndScene = true); //TCT1VERTEX
		bool Render_UTCT(PictureHandle handle); //UTCT1VERTEX

		void InitParticlePos(CParticle* part);
		void InitParticleSpeed(CParticle* part, const POINT& objPoint);
		bool CreateNewParticles(float fTimeDelta); //update里..产生新粒子
		bool LoadFullPathPoint(); //---不同路径采样精度下会要修改粒子的一些速度参数
		void ComputePathAngle();//计算m_vectorPathAngle;
		void SetPathVector(const std::vector<POINT>& vectorFullPathPoint);

		void InitTexturePicSize(); //计算 m_fparticleSize 用来精确判断粒子是否跑出了视口范围...

	private:
		XHardRef<PSTEXTUREINFOSTRUCT> m_pRefPSTextureInfo; //普通粒子特效用的经过拼接后的大纹理
		
		std::vector<XHardRef<PSPICHANDLESTRUCT>> m_vectorD3DTexture; //该粒子特效用到的小纹理图
		//记录的目的是使小纹理的引用计数加一、在delete粒子特效或者releaseResource的时候减一，达到合理管理资源的目的
	
		int m_nKeyPointIndex; //大于等于-1的数， -1 表示使用完整路径，否则就使用这个值。

		std::vector<FPOINT> m_vectorFullPathPoint; //路径点---矢量字符路径和bezier曲线路径---在构造函数里面再来初始化这个vector
		std::vector<float> m_vectorPathAngle; //align to path 专用
		std::list<int> m_listPrePathIndex; //粒子沿着路径运动中死了,新产生的粒子接着走

		//m_psParameter.m_psModifyParam.m_bModifyLineEmitter == TRUE的情况，标记新产生粒子的位置
		int m_nLinePosIndex; //按有向线顺序发射

		int m_nPosIndex; //标示新产生粒子的位置 PSLTYPE_CUSTOMPATH
		float m_fparticleSize; //用来判断粒子是否出了屏幕
		//对于很大的粒子，如何准确判断粒子是否出屏幕了.. 需要考虑粒子纹理的宽高和放大倍数.

		// 以下两个参数与m_ppsl里的数很可能不同，如果被外部程序根据发射器范围修改了...
		int m_nCurSysMaxParticles; //当前系统的允许最大粒子数，绘制时候的判断依据						
		int m_nCurEmissionRate; //当前系统发射器的发射频率..创建新粒子时候的判断依据

		std::vector<TCT1VERTEX> m_vertices_TCT;
		std::vector<UTCT1VERTEX> m_vertices_UTCT;
		
		bool m_bLoadedRes; //是否已经把纹理资源加载进来了..m_vertices_TCT空间是否分配好了
		bool m_bLoadFaild; //是否加载失败……只会在异步并且还有文件没有加载完的时候出现

		ParticleLoadingNotify* m_pLoadingNotify;//异步加载完成（成功or失败）的通知
	};

};
