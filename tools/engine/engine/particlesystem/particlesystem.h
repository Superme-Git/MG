#pragma once

#include <MinMax.h>
#include "Particle.h"
#include "CommonFuncs.h"

#include "PslPathObject.h"
#include "PSVertexsObject.h"
#include "..\renderer\ibaserenderer.h"
#include "particlearray.h"
#include "..\common\xref.h"
#include "..\common\log.h"


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
		CParticleSystem(const XHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer, const std::wstring& pslName);
		
		virtual ~CParticleSystem(void);

	public:
		virtual bool Update(float fTimeDelta) = 0; //����������������ٵ���render()
		virtual bool Render(bool bUpdate = false, bool bEndScene = true) = 0; //bUpdate..true���ʾ������render֮ǰ�Ѿ�������	
		
		virtual void ResetVerticesVector() = 0;

		virtual void Reset(void);

	public:
		int GetNumActiveParticles(void) const { return m_particleArray.GetNumUsedElements(); }
		float GetSysLife() { return m_fRealSysLife; }//�õ���һ���ʱ��... //return m_ppsl->m_fSysLife;
		void SetSysLife(float ftime) { m_fRealSysLife = ftime; }

		XPEFFECT_BIND_TYPE GetEffectBindType() { return (XPEFFECT_BIND_TYPE)(m_ppsl->m_nSkillEffectSpritePoint); } //��Ч�ҵ�����...

		XHardRef<PSL> GetPSL()const { return m_ppsl; }
		
		PSParameter& GetPSParameter() { return m_psParameter; }
		const PSParameter& GetPSParameter()const { return m_psParameter; }

		std::wstring GetPslName()const { return m_strPslName; }

		void SetViewPort(const CRECT& ViewPort);
		void SetStopFlag(bool bStop) { m_bStop = bStop; }
		void AddPOINTdxdy(const FPOINT& fpt) { m_POINTdxdy += fpt; }
		void SetParticleAngle(float fParticleAngle) { m_fParticleAngle += fParticleAngle; }
		void InsertParticleNotify(IParticleNotify* pParticleNotify) { m_vectNotifys.push_back(pParticleNotify); }
		void RemoveParticleNotify(IParticleNotify* pParticleNotify) { m_vectNotifys.erase(remove(m_vectNotifys.begin(), m_vectNotifys.end(), pParticleNotify)); }

		bool SavePSL(LPCWSTR strFilename);//����psl//�༭��ר��...
		bool SavePSLEx(LPCWSTR strFilename);//����psl//�༭��ר��...

		static bool SavePSLToFile(std::wstring FN, const XHardRef<PSL>& ppsl);//����psl//�༭��ר��...
		static bool SavePSLToStream(std::ofstream& FS, const XHardRef<PSL>& ppsl);//����psl//�༭��ר��...

	protected:
		void InitParticleAlphaColor(CParticle* part);

	private:
		void SetSysAge(float fSysAge) { m_fSysAge = fSysAge; }
		void SetEmitterProperty();

	public:
		//m_pointCent0 ʼ�ձ�������洫�����������--Ϊ������ȷ����PSLMOVETYPE_FOLLOWSPRITE�������͵���Ч
		FPOINT m_pointCent0; //���ֵʼ�ձ�����������--  ����Ч���ڵ���������

	protected:
		PSParameter m_psParameter;
		XHardRef<PSL> m_ppsl; //��Ӧ�Ĵ����ļ��ṹ
		CRecyclingArray<CParticle> m_particleArray;
		IBaseRenderer *m_pRenderer;
		bool m_bStop;
		float m_fLastTime;

		CRECT m_preViewPort;
		CRECT m_curViewPort;

		std::wstring m_strPslName; //�ͷ���Դ��ʱ����Ҫ�õ�(������ϵͳ��ʹ�õ������ļ���)
		std::vector<IParticleNotify*> m_vectNotifys;

		float m_fSysAge; //����..��m_ppsl->m_fSysLifeֵ�Ƚ�
		float m_fRealSysLife; //����������˷�PSDEFAULTTIMEʱ����ó������ֵ���������psl.fsyslife

		float m_fNumNewPartsExcess; //С�������ۼӱ���
		int m_nNumReLifePartsExcess; //����������

		float m_fGravity;
		float m_fRadialAcc;
		float m_fTangAcc;
		FPOINT m_ViewPortdxdy;
		FPOINT m_POINTdxdy; //m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE
		float m_fParticleAngle; //m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE

	private:
		std::wstring m_strSysPath;

	private:
		CParticleSystem(const CParticleSystem&);
		CParticleSystem& operator=(const CParticleSystem&);
	};

};