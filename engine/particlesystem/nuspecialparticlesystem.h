#ifndef __Nuclear_specialparticlesystem_H__
#define __Nuclear_specialparticlesystem_H__

#include "nuparticlesystem.h"

namespace Nuclear
{
	class CSpecialParticleSystem :	public CParticleSystem
	{
	public:
		CSpecialParticleSystem(const NuclearHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer, const std::wstring& pslName);

		~CSpecialParticleSystem(void);

		virtual bool Render(bool bUpdate = false, bool bEndScene = true); 

		virtual bool Update(float fTimeDelta); //允许外面更新粒子再调用render()

		virtual void Reset(void);

		virtual void ResetVerticesVector() { }

		virtual bool SetShapePsTexture(PictureHandle, float fu=1.0f, float fv=1.0f); //烂显卡上创建的非2幂纹理
		virtual void SetShapePsScale(float fcx=1.0f, float fcy=1.0f); //绘制区域缩放比例
		virtual void SetShapePsVertexs(const SHAPE_LIST& shapeList);
		virtual void SetSilentTime(float ftime); //转场特效在这段时间内停留在第一帧
		virtual PictureHandle GetSpecialPSTextureHandle()const { return m_pD3dTextureShapes; }

	private:
		bool CreateNewParticles(float fTimeDelta); //update里..产生新粒子
		void InitParticlePos(CParticle* part)const;

	private:
		//以下是抽象粒子效果特有的---
		//抽象粒子顶点数据
		SHAPE_LIST m_shapeList; //初始数据...update只修改particles里面的数据..	
		PictureHandle m_pD3dTextureShapes; //抽象粒子特效用的纹理... ----从particleManager里面传过来的..这里面不做管理！
		int m_nShapeTextureWidth; //计算爆炸中心、通过纹理坐标计算顶点值、计算每块的开始结束纹理坐标...
		int m_nShapeTextureHeight;
		float m_fmaxu; //纹理有效数据区域..
		float m_fmaxv;
		float m_fShapePsScalex; //缩放比例
		float m_fShapePsScaley;
		float m_fSilentTime; //在这段时间内停留在第一帧
		float m_fSilentAge;  //已经停留的时间

		bool m_bDir; //运动方向 -- 两个粒子的运动方向刚好相反

	};

}

#endif