#ifndef __Nuclear_particle_H__
#define __Nuclear_particle_H__
#include "nucommonfuncs.h"

namespace Nuclear
{
	struct CParticle
	{
		CParticle()
		{
			m_fSizeX = 0.0f; 
			m_fSizeY = 0.0f; 
			m_fLifetime = 0.0f; 
		
			m_fAge = 0.0f; 
			m_fFullScreenTime = 0.0f;

			m_fSpin = 0.0f; 
			m_fAngle = 0.0f; 
			m_fParSizeStrtX = 0.0f;	
			m_fParSizeStrtY = 0.0f;	
			m_fParSizeEndX = 0.0f;		
			m_fParSizeEndY = 0.0f;		
			m_fParSizeVar = 0.0f;
			m_nStrTextureIndex = 0;
			m_nCurTextureIndex = 0;
			m_nStrtPathIndex = 0;
			m_nCurPathIndex = 0;
			m_fPathMotionSpeed = 0.0f;
			m_fAniSpeed = 10.0f;

			m_fParAlphaMidTime = 0.5f;
		}


		float m_fLifetime;  //生命 --不随时间而变，由发射器决定。
		float m_fSizeX;		//大小 --与m_vPos一起决定四边形的位置
		float m_fSizeY;		//大小 --与m_vPos一起决定四边形的位置
		float m_fSpin;		//旋转角度
		float m_fAngle;		//自旋角度

		NuclearColor m_color;
		NuclearVector3 m_vPos; //粒子中心点位置 --根据中心点位置计算四个顶点坐标
		NuclearVector3 m_vDir; //速度(矢量)

		//只在CParticleSystem.Update()中循环调用...
		inline bool Update(float fTimeDelta)
		{
			//生命
			m_fAge += fTimeDelta;

			if( fTimeDelta>PSUPDATECRITICALTIME )
				fTimeDelta = 0.0f;

			//超过生命期的...
			if( m_fAge+m_fFullScreenTime > m_fLifetime)
				return false; //死亡

			//更新粒子
			float coef = 1.0f - m_fAge/m_fLifetime;

			float coefAlpha = GetPowfLerpValue(coef, m_fAlphaVar);
			float fAlpha = 0.0f;
			//单mid值模拟双关键帧midAlpha最大可能是10.0f...特效朱迪的想法..
			if(1.0f-coef < m_fParAlphaMidTime)
				fAlpha = coefAlpha*(m_colorStrt.a - m_nAlphaMid) + m_nAlphaMid;
			else
				fAlpha = coefAlpha*(m_nAlphaMid - m_colorEnd.a) + m_colorEnd.a;

			fAlpha = fAlpha>255.0f ? 255.0f:fAlpha;
			m_color.a = (unsigned char)(fAlpha);

			float coefCR = GetPowfLerpValue(coef, m_fColorVar);

			m_color.r = (unsigned char)( coefCR*(m_colorStrt.r - m_colorEnd.r) +m_colorEnd.r);
			m_color.g = (unsigned char)( coefCR*(m_colorStrt.g - m_colorEnd.g) +m_colorEnd.g);
			m_color.b = (unsigned char)( coefCR*(m_colorStrt.b - m_colorEnd.b) +m_colorEnd.b);

			float coefSZ = GetPowfLerpValue(coef, m_fParSizeVar);

			m_fSizeX = coefSZ*(m_fParSizeStrtX - m_fParSizeEndX) + m_fParSizeEndX;
			m_fSizeY = coefSZ*(m_fParSizeStrtY - m_fParSizeEndY) + m_fParSizeEndY;

			float coefSP = GetPowfLerpValue(coef, m_fSpinVar);

			m_fSpin = coefSP*(m_fSpinStrt - m_fSpinEnd) + m_fSpinEnd;

			float coefAn = GetPowfLerpValue(coef, m_fAngleVar);

			m_fAngle = coefAn*(m_fAngleStrt - m_fAngleEnd) + m_fAngleEnd;
			return true; //活着的粒子
		}

		float m_fParSizeStrtX;			//0---100.0f
		float m_fParSizeStrtY;			//0---100.0f
		float m_fParSizeEndX;			//0---100.0f
		float m_fParSizeEndY;			//0---100.0f
		float m_fParSizeVar;			//0---1.0f

		float m_fColorVar;
		NuclearColor m_colorStrt;
		NuclearColor m_colorEnd;

		float m_fAlphaVar;
		int m_nAlphaMid; //alpha中值，可以大于255，用一个关键帧来模拟出2个关键帧
		float m_fParAlphaMidTime;
	
		float m_fSpinStrt;
		float m_fSpinEnd;
		float m_fSpinVar;

		float m_fAngleStrt;
		float m_fAngleEnd;
		float m_fAngleVar;

		float m_fAge; //对于全屏特效，这个值常会被强制修改掉...
		float m_fFullScreenTime; //全屏特效中被强制减少了的粒子年龄 ---一个粒子死了这个值就要修改为0

		NuclearVector3 m_vDir0; //初始发射速度(矢量)
		float m_fAngle0; //这个是...初始发射角度

		NuclearVector3 m_vPos0; //粒子中心点初始位置 --有了这个备份能方便粒子循环时候的处理 09-6-4

		int m_nStrTextureIndex; //纹理索引
		int m_nCurTextureIndex; //当前纹理索引..为实现单粒子动画加入的..,粒子新产生的时候分配... （粒子系统多纹理支持...） --更新实现单粒子动画..
		float m_fAniSpeed; //每个粒子的变换频率都可能不相同

		float m_fPathMotionSpeed; //由初始发射速度决定..只要大小不考虑方向、加速度..
		int m_nStrtPathIndex;
		int m_nCurPathIndex; //当前粒子在路径（仅限于用户编辑的路径..不包括矢量字符路径）中的哪个位置...,用于实现粒子沿着路径运动...，记录当前粒子运动到了哪个点..
	};
};

#endif