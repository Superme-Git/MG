#include "../common/nulog.h"
#include "nustatemanager.h"
#include <CCGL.h>
#include <shaders/ccGLStateCache.h>

namespace
{
    using namespace Nuclear;
    GLenum MapGLBlendType(XPALPHABLEND_TYPE b)
    {
        switch (b) {
            case XPBLEND_ZERO:
                return GL_ZERO;
            case XPBLEND_ONE:
                return GL_ONE;
            case XPBLEND_SRCCOLOR:
                return GL_SRC_COLOR;
            case XPBLEND_INVSRCCOLOR:
                return GL_ONE_MINUS_SRC_COLOR;
            case XPBLEND_SRCALPHA:
                return GL_SRC_ALPHA;
            case XPBLEND_INVSRCALPHA:
                return GL_ONE_MINUS_SRC_ALPHA;
            case XPBLEND_DESTALPHA:
                return GL_DST_ALPHA;
            case XPBLEND_INVDESTALPHA:
                return GL_ONE_MINUS_DST_ALPHA;
            case XPBLEND_DESTCOLOR:
                return GL_DST_COLOR;
            case XPBLEND_INVDESTCOLOR:
                return GL_ONE_MINUS_DST_COLOR;
            default:
                assert(false && "unknown blend type");
                break;
        }
    }
}

namespace Nuclear
{
	bool StateManager::Init()
	{

		m_curEffect = XPRE_NULL;
		m_curParamEffect = XPRE_NULL;
		
		OnRestore();
		return true;
	}

	void StateManager::Destroy()
	{
		OnRelease();
	}

	void StateManager::OnRelease()
	{

	}

	XPRENDER_EFFECT StateManager::GetEffect() const
	{
		return m_curEffect;
	}

	bool StateManager::SelEffect(XPRENDER_EFFECT xpre)
	{
		return true;
	}

	bool StateManager::SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam)
	{

		return true;
	}

	void StateManager::OnRestore()
	{

	}

	void StateManager::EnableSecondTex(bool f)
	{

	}

	void StateManager::SetScissorRect(const NuclearRect* pRect)
	{

	}

	void StateManager::SetAlphaBlendEnable(bool f)
	{

	}

	void StateManager::SetSeparateAlphaBlend(bool f)
	{

	}

	void StateManager::SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend)
	{
		if( srcblend == m_eAlphaBlendSrcType )
			return;
		m_eAlphaBlendSrcType = srcblend;
		glEnable( GL_BLEND );
        cocos2d::ccGLBlendFunc(MapGLBlendType(m_eAlphaBlendSrcType), MapGLBlendType(m_eAlphaBlendDstType));
	}

	void StateManager::SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend)
	{
		if( dstblend == m_eAlphaBlendDstType )
			return;
		m_eAlphaBlendDstType = dstblend;

        cocos2d::ccGLBlendFunc(MapGLBlendType(m_eAlphaBlendSrcType), MapGLBlendType(m_eAlphaBlendDstType));
	}

	void StateManager::SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop)
	{
		
	}

	void StateManager::SetRenderEffect(XPRENDER_EFFECT re)
	{
		SelEffect(re);
	}

	XPRENDER_EFFECT StateManager::GetRenderEffect() const
	{
		return GetEffect();
	}

	void StateManager::OnMMSetRenderState(::DWORD type, ::DWORD value)
	{
	}

	void StateManager::OnMMSetSamplerState(::DWORD sampler, ::DWORD type, ::DWORD value)
	{

	}

	void StateManager::ResumeFrom3DRender()
	{

	}
};