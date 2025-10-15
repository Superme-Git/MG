#ifndef __Nuclear_STATEMANAGER_H
#define __Nuclear_STATEMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "rendereffect.h"
#include "ibaserenderer.h"

namespace Nuclear
{

class StateManager
{
private:
	// 私有成员
	IDirect3DDevice9*	m_ifDevice;
	//int m_curState;

	RenderEffect* m_reffects[XPRE_COUNT];
	XPRENDER_EFFECT m_curEffect;
	XPRENDER_EFFECT m_curParamEffect;

private:
	bool	m_fAlphaBlendEnable;
	bool	m_fIsSecondTexEnable;
	bool	m_bSeparateAlphaBlend;//Alpha和颜色分开计算
	bool	m_bSeparateAlphaBlendCap;
	XPALPHABLEND_TYPE	m_eAlphaBlendSrcType;
	XPALPHABLEND_TYPE	m_eAlphaBlendDstType;
	XPTEXCOLORBLEND_TYPE	m_eTextureColorBlendType;
	XPRENDER_EFFECT	m_eRenderEffect;
	// 构造析构
public:
	StateManager() : m_ifDevice(NULL) 
	{ 
		for (int i=0;i<XPRE_COUNT;++i)
		{
			m_reffects[i] = NULL;
		}
		m_bSeparateAlphaBlend = false;
		m_bSeparateAlphaBlendCap = false;

	}
	~StateManager() { }

	// 操作
	bool Init(IDirect3DDevice9*, const D3DCAPS9& d3dCaps);
	void Destroy();

	void OnRelease();
	void OnRestore();

	bool SelEffect(XPRENDER_EFFECT xpre);
	XPRENDER_EFFECT GetEffect() const;
	bool SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam);
	bool SetShaderTexture(XPRENDER_EFFECT xpre, const char* name, const void* pParam);
	//void SetColorOpType(XPCOLOROPTYPE nColorOpType);

	//
	//void SetAlphaBlendType(XPALPHABLENDTYPE nAlphaBlendType);

public:
	void SetAlphaBlendEnable(bool f);
	bool GetAlphaBlendEnable() const;

	void SetSeparateAlphaBlend(bool f);
	bool GetSeparateAlphaBlend() const;

	void SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend);
	XPALPHABLEND_TYPE GetAlphaBlendSrcType() const;
	void SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend);
	XPALPHABLEND_TYPE GetAlphaBlendDstType() const;


	void SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop);
	XPTEXCOLORBLEND_TYPE GetTextureColorBlendType() const;

	void EnableSecondTex(bool f);
	bool IsSecondTexEnable() const { return m_fIsSecondTexEnable; }

	void SetRenderEffect(XPRENDER_EFFECT re);
	XPRENDER_EFFECT GetRenderEffect() const;

	// 占坑
private:
	StateManager(const StateManager&);
	StateManager& operator=(const StateManager&);
};



};

#endif