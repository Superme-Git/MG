#include "stdafx.h"
#include "..\common\log.h"
#include "statemanager.h"


namespace Nuclear
{
	bool StateManager::Init(IDirect3DDevice9* ifDevice, const D3DCAPS9& d3dCaps)
	{
		if( ifDevice == NULL ) return false;
		m_ifDevice = ifDevice;	

		m_curEffect = XPRE_NULL;
		m_curParamEffect = XPRE_NULL;

		m_reffects[XPRE_COLORBALANCE] = new CBRenderEffect();
		m_reffects[XPRE_COLORBALANCE_2] = new CBRenderEffect_2();
		m_reffects[XPRE_ALPHA] = new ALRenderEffect();
	//	m_reffects[XPRE_RADIALBLUR] = new RBRenderEffect();
	//	m_reffects[XPRE_EMBOSSPIC] = new EPRenderEffect();
	//	m_reffects[XPRE_GRAY] = new GPRenderEffect();
		m_reffects[XPRE_DISTORTION] = new DistortionRenderEffect();
		m_reffects[XPRE_DYE] = new DyeRenderEffect();
	//	m_reffects[XPRE_FONTOUTLINE] = new FontOutlineRenderEffect();
	//	m_reffects[XPRE_GaussianBlurBlend] = new GaussianBlurBlendRenderEffect();

		for(int i=0; i< XPRE_COUNT; ++i)
			m_reffects[i]->Init(d3dCaps.PixelShaderVersion);

		//D3DPMISCCAPS_SEPARATEALPHABLEND
		m_bSeparateAlphaBlendCap = (d3dCaps.PrimitiveMiscCaps & D3DPMISCCAPS_SEPARATEALPHABLEND) != 0;
		
		OnRestore();
		return true;
	}

	void StateManager::Destroy()
	{
		OnRelease();
		for(int i=0; i<XPRE_COUNT; ++i)
			delete m_reffects[i];
		m_curEffect = XPRE_NULL;
		m_curParamEffect = XPRE_NULL;
	}

	void StateManager::OnRelease()
	{
		for(int i=0; i< XPRE_COUNT; ++i)
		{
			if (m_reffects[i]) m_reffects[i]->Release();
		}
		m_curParamEffect = XPRE_NULL;
	}

	XPRENDER_EFFECT StateManager::GetEffect() const
	{
		return m_curEffect;
	}

	bool StateManager::SelEffect(XPRENDER_EFFECT xpre)
	{
		if( xpre < XPRE_NULL || xpre >= XPRE_COUNT ) return false;
		if( xpre == m_curEffect ) return true;

		if( xpre == XPRE_NULL )
		{
			m_ifDevice->SetPixelShader(NULL);
			m_curEffect = xpre;
			return true;
		}
		if( !m_reffects[xpre]->IsEnable() ) return false;

		if( xpre != m_curParamEffect || RenderEffect::GetCurrentColorBlendType() != m_eTextureColorBlendType )
		{
			RenderEffect::SetColorBlendType(m_eTextureColorBlendType);
			m_reffects[xpre]->SetParameter(m_ifDevice);
			m_curParamEffect = xpre;
		}

		HRESULT hr = m_reffects[xpre]->SetPixelShader(m_ifDevice);
		if( FAILED(hr) )
			return false;
		m_curEffect = xpre;
		return true;
	}

	bool StateManager::SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam)
	{
		if( xpre <= XPRE_NULL || xpre >= XPRE_COUNT ) return false;
		m_reffects[xpre]->ChangeShaderParam(pParam);
		if( xpre == m_curEffect )
		{
			m_reffects[xpre]->SetParameter(m_ifDevice);
			m_curParamEffect = xpre;
		}
		return true;
	}
	bool StateManager::SetShaderTexture(XPRENDER_EFFECT xpre, const char* name, const void* pParam)
	{
		m_reffects[xpre]->SetTexture(m_ifDevice, name, pParam);
		return true;
	}
	void StateManager::OnRestore()
	{
		m_ifDevice->SetRenderState(D3DRS_CULLMODE,D3DCULL_NONE);
		m_ifDevice->SetRenderState( D3DRS_FILLMODE, D3DFILL_SOLID );

		m_ifDevice->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR );
		m_ifDevice->SetSamplerState(0, D3DSAMP_MINFILTER, D3DTEXF_LINEAR );

		m_fAlphaBlendEnable = true;
		m_ifDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, m_fAlphaBlendEnable);
		m_eAlphaBlendSrcType = XPBLEND_SRCALPHA;
		m_ifDevice->SetRenderState( D3DRS_SRCBLEND, m_eAlphaBlendSrcType);
		m_eAlphaBlendDstType = XPBLEND_INVSRCALPHA;
		m_ifDevice->SetRenderState( D3DRS_DESTBLEND, m_eAlphaBlendDstType);

		if (m_bSeparateAlphaBlendCap)
		{
			m_ifDevice->SetRenderState(D3DRS_SEPARATEALPHABLENDENABLE, m_bSeparateAlphaBlend);
			m_ifDevice->SetRenderState(D3DRS_SRCBLENDALPHA, D3DBLEND_ONE);
			m_ifDevice->SetRenderState(D3DRS_DESTBLENDALPHA, D3DBLEND_ONE);
			m_ifDevice->SetRenderState(D3DRS_BLENDOPALPHA, D3DBLENDOP_MAX);
		}

		m_ifDevice->SetRenderState(D3DRS_ALPHATESTENABLE, TRUE);
		m_ifDevice->SetRenderState(D3DRS_ALPHAREF, (DWORD)0x00000001); 
		m_ifDevice->SetRenderState(D3DRS_ALPHAFUNC, D3DCMP_GREATEREQUAL);


		m_eTextureColorBlendType = XPTOP_MODULATE;
		m_ifDevice->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_MODULATE);
		m_ifDevice->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
		m_ifDevice->SetTextureStageState(0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);

		m_ifDevice->SetTextureStageState(0, D3DTSS_ALPHAOP, D3DTOP_MODULATE);
		m_ifDevice->SetTextureStageState(0, D3DTSS_ALPHAARG1, D3DTA_TEXTURE);
		m_ifDevice->SetTextureStageState(0, D3DTSS_ALPHAARG2, D3DTA_DIFFUSE);

		m_fIsSecondTexEnable = false;
		//目前，1层纹理的ColorOP和AlphaOP只能是乘
		m_ifDevice->SetTextureStageState(1, D3DTSS_TEXCOORDINDEX, 1);
		m_ifDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_DISABLE);
		m_ifDevice->SetTextureStageState(1, D3DTSS_COLORARG1, D3DTA_TEXTURE);
		m_ifDevice->SetTextureStageState(1, D3DTSS_COLORARG2, D3DTA_CURRENT);

		m_ifDevice->SetTextureStageState(1, D3DTSS_ALPHAOP, D3DTOP_DISABLE);
		m_ifDevice->SetTextureStageState(1, D3DTSS_ALPHAARG1, D3DTA_TEXTURE);
		m_ifDevice->SetTextureStageState(1, D3DTSS_ALPHAARG2, D3DTA_CURRENT);

		//D3DPTEXTURECAPS_NONPOW2CONDITIONAL的条件之一，同时粒子系统中也用到了..
		m_ifDevice->SetSamplerState( 0, D3DSAMP_ADDRESSU, D3DTADDRESS_CLAMP);
		m_ifDevice->SetSamplerState( 0, D3DSAMP_ADDRESSV, D3DTADDRESS_CLAMP);

	//	m_ifDevice->SetRenderState(D3DRS_WRAP0, 0); // 默认值..
		
		// TODO 如果将来各状态之间的差别比较大，可使用一组IDirect3DStateBlock9成员来记录
		// 相应的，也可以开放定制IDirect3DStateBlock9的接口

		for(int i=0; i<XPRE_COUNT; ++i)
		{
			m_reffects[i]->InitShader(m_ifDevice);			
		}

		if( m_curEffect != XPRE_NULL && !m_reffects[m_curEffect]->IsEnable() )
		{
			SelEffect(XPRE_NULL);
		}
	}

	void StateManager::EnableSecondTex(bool f)
	{
		if (m_fIsSecondTexEnable == f)
			return;
		m_fIsSecondTexEnable = f;
		DWORD op = m_fIsSecondTexEnable?D3DTOP_MODULATE:D3DTOP_DISABLE;
		m_ifDevice->SetTextureStageState(1, D3DTSS_COLOROP, op);
		m_ifDevice->SetTextureStageState(1, D3DTSS_ALPHAOP, op);
	}

	void StateManager::SetAlphaBlendEnable(bool f)
	{
		if( f == m_fAlphaBlendEnable )
			return;
		m_fAlphaBlendEnable = f;
		m_ifDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, m_fAlphaBlendEnable);
	}

	bool StateManager::GetAlphaBlendEnable() const
	{
		return m_fAlphaBlendEnable;
	}

	void StateManager::SetSeparateAlphaBlend(bool f)
	{
		if( f == m_bSeparateAlphaBlend )
			return;
		m_bSeparateAlphaBlend = f;
		if (m_bSeparateAlphaBlendCap)
			m_ifDevice->SetRenderState(D3DRS_SEPARATEALPHABLENDENABLE, m_bSeparateAlphaBlend);
	}

	bool StateManager::GetSeparateAlphaBlend() const
	{
		return m_bSeparateAlphaBlend;
	}

	void StateManager::SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend)
	{
		if( srcblend == m_eAlphaBlendSrcType )
			return;
		m_eAlphaBlendSrcType = srcblend;
		m_ifDevice->SetRenderState( D3DRS_SRCBLEND, m_eAlphaBlendSrcType);
	}
		
	XPALPHABLEND_TYPE StateManager::GetAlphaBlendSrcType() const
	{
		return m_eAlphaBlendSrcType;
	}

	void StateManager::SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend)
	{
		if( dstblend == m_eAlphaBlendDstType )
			return;
		m_eAlphaBlendDstType = dstblend;
		m_ifDevice->SetRenderState( D3DRS_DESTBLEND, m_eAlphaBlendDstType);
	}
		
	XPALPHABLEND_TYPE StateManager::GetAlphaBlendDstType() const
	{
		return m_eAlphaBlendDstType;
	}

	void StateManager::SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop)
	{
		static _D3DTEXTUREOP d3dParam[XPTOP_TYPE_COUNT] = 
		{
			D3DTOP_MODULATE,
			D3DTOP_SELECTARG1, 
			D3DTOP_SELECTARG2,
			D3DTOP_ADD,
			D3DTOP_SUBTRACT,
			D3DTOP_ADDSMOOTH,
			D3DTOP_ADDSIGNED,
			D3DTOP_MODULATE2X,
		};

		if( colorop != m_eTextureColorBlendType )
		{
			m_eTextureColorBlendType = colorop;
			m_ifDevice->SetTextureStageState(0, D3DTSS_COLOROP, d3dParam[m_eTextureColorBlendType]);
		}

		if( m_curEffect <= XPRE_NULL || m_curEffect >= XPRE_COUNT ) 
			return;
		if (RenderEffect::GetCurrentColorBlendType() == colorop)
			return;
		RenderEffect::SetColorBlendType(colorop);
		m_reffects[m_curEffect]->SetPixelShader(m_ifDevice);
		m_reffects[m_curEffect]->SetParameter(m_ifDevice);		
		
	}

	XPTEXCOLORBLEND_TYPE StateManager::GetTextureColorBlendType() const
	{
		return m_eTextureColorBlendType;
	}

	void StateManager::SetRenderEffect(XPRENDER_EFFECT re)
	{
		//todo
		SelEffect(re);
	}

	XPRENDER_EFFECT StateManager::GetRenderEffect() const
	{
		return GetEffect(); //todo
		//return m_eRenderEffect;
	}
};