#ifndef __Nuclear_STATEMANAGER_H__
#define __Nuclear_STATEMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuibaserenderer.h"
#include "../common/nuxpmaths.h"

namespace Nuclear
{

	class StateManager
	{
	private:
		XPRENDER_EFFECT m_curEffect;
		XPRENDER_EFFECT m_curParamEffect;

		enum XPChangeSign
		{
			XP0_D3DSAMP_MAGFILTER = 1,			//XP认为是D3DTEXF_LINEAR
			XP0_D3DSAMP_MINFILTER = 1 << 1,		//XP认为是D3DTEXF_LINEAR
			XP0_D3DSAMP_MIPFILTER = 1 << 2,		//XP认为是D3DTEXF_NONE
			XP1_D3DSAMP_MAGFILTER = 1 << 3,		//XP认为是D3DTEXF_LINEAR
			XP1_D3DSAMP_MINFILTER = 1 << 4,		//XP认为是D3DTEXF_LINEAR
			XP1_D3DSAMP_MIPFILTER = 1 << 5,		//XP认为是D3DTEXF_NONE
			XP0_D3DSAMP_ADDRESSU = 1 << 6,		//XP认为是D3DTADDRESS_CLAMP 1层纹理改成什么样子目前不需要关心
			XP0_D3DSAMP_ADDRESSV = 1 << 7,		//XP认为是D3DTADDRESS_CLAMP 1层纹理改成什么样子目前不需要关心
			XP_D3DRS_FILLMODE = 1 << 8,			//XP认为是D3DFILL_SOLID
			XP_D3DRS_CULLMODE = 1 << 9,			//XP认为是D3DCULL_NONE
			XP_D3DRS_ZENABLE = 1 << 10,			//XP认为是D3DZB_FALSE
			XP_D3DRS_ZWRITEENABLE = 1 << 11,	//XP认为是FALSE
			XP_D3DRS_ALPHATESTENABLE = 1 << 12,	//XP认为是TRUE
			XP_D3DRS_ALPHAFUNC = 1 << 13,		//XP认为是D3DCMP_GREATEREQUAL
			XP_D3DRS_ALPHAREF = 1 << 14,		//XP认为是(DWORD)0x00000001
			XP_D3DRS_BLENDOP = 1 << 15,			//XP认为是D3DBLENDOP_ADD
			XP_D3DRS_BLENDOPALPHA = 1 << 16,	//XP认为是D3DBLENDOP_MAX
			XP_D3DRS_SRCBLENDALPHA = 1 << 17,	//XP认为是D3DBLEND_ONE
			XP_D3DRS_DESTBLENDALPHA = 1 << 18,	//XP认为是D3DBLEND_ONE
		};

		bool	m_bAlphaBlendEnable;
		bool	m_bIsSecondTexEnable;
		bool	m_bSeparateAlphaBlend;//Alpha和颜色分开计算
		bool	m_bSeparateAlphaBlendCap;
		bool	m_bIsMultiSampleTypeEnable;	//是否抗锯齿
		XPALPHABLEND_TYPE	m_eAlphaBlendSrcType;
		XPALPHABLEND_TYPE	m_eAlphaBlendDstType;
		XPTEXCOLORBLEND_TYPE	m_eTextureColorBlendType;
		XPRENDER_EFFECT	m_eRenderEffect;
		DWORD m_dwChangeFlag;

	public:
		StateManager() : 
		m_dwChangeFlag(0)
		{ 
			m_bAlphaBlendEnable = true;
			m_bSeparateAlphaBlend = false;
			m_bSeparateAlphaBlendCap = false;
			m_bIsMultiSampleTypeEnable = true;
			m_eAlphaBlendSrcType = XPBLEND_SRCALPHA;
			m_eAlphaBlendDstType = XPBLEND_INVSRCALPHA;
		}
		~StateManager() { }

		bool Init();
		void Destroy();

		void OnRelease();
		void OnRestore();

		bool SelEffect(XPRENDER_EFFECT xpre);
		XPRENDER_EFFECT GetEffect() const;
		bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam);

	public:
		bool IsMultiSampleTypeEnable() const { return m_bIsMultiSampleTypeEnable; }

		void SetAlphaBlendEnable(bool f);
		bool GetAlphaBlendEnable() const { return m_bAlphaBlendEnable; }

		void SetSeparateAlphaBlend(bool f);
		bool GetSeparateAlphaBlend() const { return m_bSeparateAlphaBlend; }

		void SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend);
		XPALPHABLEND_TYPE GetAlphaBlendSrcType() const { return m_eAlphaBlendSrcType; }
		void SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend);
		XPALPHABLEND_TYPE GetAlphaBlendDstType() const { return m_eAlphaBlendDstType; }

		void SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop);
		XPTEXCOLORBLEND_TYPE GetTextureColorBlendType() const { return m_eTextureColorBlendType; }

		void EnableSecondTex(bool f);
		bool IsSecondTexEnable() const { return m_bIsSecondTexEnable; }

		void SetScissorRect(const NuclearRect* pRect);

		void SetRenderEffect(XPRENDER_EFFECT re);
		XPRENDER_EFFECT GetRenderEffect() const;

		virtual void OnMMSetRenderState(::DWORD type, ::DWORD value);
		virtual void OnMMSetSamplerState(::DWORD sampler, ::DWORD type, ::DWORD value);
		void ResumeFrom3DRender();

	private:
		StateManager(const StateManager&);
		StateManager& operator=(const StateManager&);
	};

}

#endif