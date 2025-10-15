#ifndef __Nuclear_particlesystem_H__
#define __Nuclear_particlesystem_H__
#include "nuparticle.h"
#include "nucommonfuncs.h"

#include "nupslpathobject.h"
#include "nupsvertexsobject.h"
#include "../renderer/nuibaserenderer.h"
#include "nuparticlearray.h"
#include "../common/nuxref.h"
#include "../common/nulog.h"


namespace Nuclear
{
	class InitStaticVar
	{
	public:
		InitStaticVar(void);
		~InitStaticVar(void);
	};

	class CParticleSystem
	{
	public:
		CParticleSystem(const NuclearHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer, const std::wstring& pslName);		
		virtual ~CParticleSystem(void);

	public:
		virtual bool Update(float fTimeDelta) = 0; //允许外面更新粒子再调用render()
		virtual bool Render(bool bUpdate = false, bool bEndScene = true) = 0; //bUpdate..true则表示粒子在render之前已经更新了	
		
		virtual void ResetVerticesVector() = 0;

		virtual void Reset(void);
        
        virtual bool IsTextureEtc(){return false;}

		int GetNumActiveParticles(void) const { return m_particleArray.GetNumUsedElements(); }
		float GetSysLife() { return m_fRealSysLife; }//得到播一遍的时间... //return m_ppsl->m_fSysLife;
		void SetSysLife(float ftime) { m_fRealSysLife = ftime; }

		Nuclear_EffectBindType GetEffectBindType() { return (Nuclear_EffectBindType)(m_ppsl->m_nSkillEffectSpritePoint); } //特效挂点类型...

		NuclearHardRef<PSL> GetPSL()const { return m_ppsl; }
		
		PSParameter& GetPSParameter() { return m_psParameter; }
		const PSParameter& GetPSParameter()const { return m_psParameter; }

		std::wstring GetPslName()const { return m_strPslName; }

		void SetViewPort(const NuclearRect& ViewPort);
		void SetStopFlag(bool bStop) { m_bStop = bStop; }
		void AddPOINTdxdy(const NuclearFPoint& fpt) { m_POINTdxdy += fpt; }
		void SetParticleAngle(float fParticleAngle) { m_fParticleAngle += fParticleAngle; }
		void InsertParticleNotify(IParticleNotify* pParticleNotify) { m_vectNotifys.push_back(pParticleNotify); }
		void RemoveParticleNotify(IParticleNotify* pParticleNotify) { m_vectNotifys.erase(remove(m_vectNotifys.begin(), m_vectNotifys.end(), pParticleNotify)); }

	protected:
		void InitParticleAlphaColor(CParticle* part);

	private:
		void SetSysAge(float fSysAge) { m_fSysAge = fSysAge; }
		void SetEmitterProperty();

	public:
		//m_pointCent0 始终保存的外面传入的世界坐标--为的是正确处理PSLMOVETYPE_FOLLOWSPRITE这种类型的特效
		NuclearFPoint m_pointCent0; //这个值始终保持世界坐标--  该特效所在的世界坐标

		bool m_bLookupSegmPak; //微端相关, CreateParticleSystem()之前需要先检查该特效资源在哪...

	protected:
		PSParameter m_psParameter;
		NuclearHardRef<PSL> m_ppsl; //对应的磁盘文件结构
		CRecyclingArray<CParticle> m_particleArray;
		IBaseRenderer *m_pRenderer;
		bool m_bStop;
		float m_fLastTime;

		NuclearRect m_preViewPort;
		NuclearRect m_curViewPort;

		std::wstring m_strPslName; //释放资源的时候需要用到(该粒子系统所使用的psl文件名)
		std::vector<IParticleNotify*> m_vectNotifys;

		float m_fSysAge; //年龄..与m_ppsl->m_fSysLife值比较
		float m_fRealSysLife; //如果程序传入了非PSDEFAULTTIME时间就用程序传入的值，否则就是psl.fsyslife

		float m_fNumNewPartsExcess; //小数部分累加保存
		int m_nNumReLifePartsExcess; //复活粒子数

		float m_fGravity;
		float m_fRadialAcc;
		float m_fTangAcc;
		NuclearFPoint m_ViewPortdxdy;
		NuclearFPoint m_POINTdxdy; //m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE
		float m_fParticleAngle; //m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE

	private:
		std::wstring m_strSysPath;

		CParticleSystem(const CParticleSystem&);
		CParticleSystem& operator=(const CParticleSystem&);
	};
};

#endif