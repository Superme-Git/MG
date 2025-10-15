/***********************************************************************
    filename:   CEGUIDirect3D9Renderer.cpp
    created:    Mon Feb 9 2009
    author:     Paul D Turner
*************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2009 Paul D Turner & The CEGUI Development Team
 *
 *   Permission is hereby granted, free of charge, to any person obtaining
 *   a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including
 *   without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to
 *   permit persons to whom the Software is furnished to do so, subject to
 *   the following conditions:
 *
 *   The above copyright notice and this permission notice shall be
 *   included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *   OTHER DEALINGS IN THE SOFTWARE.
 ***************************************************************************/
#define NOMINMAX
#include "CEGUIDirect3D9Renderer.h"
#include "CEGUIDirect3D9Texture.h"
#include "CEGUIDirect3D9GeometryBuffer.h"
#include "CEGUIDirect3D9RenderTarget.h"
#include "CEGUIRenderingRoot.h"
#include "CEGUIExceptions.h"
#include "CEGUIDirect3D9ViewportTarget.h"
#include "CEGUIDirect3D9TextureTarget.h"
#include "CEGUISystem.h"

#include <algorithm>

// Start of CEGUI namespace section
namespace CEGUI
{
//----------------------------------------------------------------------------//
String Direct3D9Renderer::d_rendererID(
"CEGUI::Direct3D9Renderer - Official Direct3D 9 based 2nd generation renderer "
"module.");

//----------------------------------------------------------------------------//
static const D3DMATRIX s_identityMatrix =
{
    1.0, 0.0, 0.0, 0.0,
    0.0, 1.0, 0.0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
};

//----------------------------------------------------------------------------//
Direct3D9Renderer& Direct3D9Renderer::create(LPDIRECT3DDEVICE9 device)
{
    return *new Direct3D9Renderer(device);
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::destroy(Direct3D9Renderer& renderer)
{
    delete &renderer;
}

//----------------------------------------------------------------------------//
RenderingRoot& Direct3D9Renderer::getDefaultRenderingRoot()
{
    return *d_defaultRoot;
}

//----------------------------------------------------------------------------//
GeometryBuffer& Direct3D9Renderer::createGeometryBuffer()
{
    Direct3D9GeometryBuffer* b = new Direct3D9GeometryBuffer(d_device);
    d_geometryBuffers.push_back(b);
    return *b;
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::destroyGeometryBuffer(const GeometryBuffer& buffer)
{
    GeometryBufferList::iterator i = std::find(d_geometryBuffers.begin(),
                                     d_geometryBuffers.end(),
                                     &buffer);

    if (d_geometryBuffers.end() != i)
    {
        d_geometryBuffers.erase(i);
        delete &buffer;
    }
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::destroyAllGeometryBuffers()
{
    while (!d_geometryBuffers.empty())
        destroyGeometryBuffer(**d_geometryBuffers.begin());
}

//----------------------------------------------------------------------------//
TextureTarget* Direct3D9Renderer::createTextureTarget()
{
    TextureTarget* t = new Direct3D9TextureTarget(*this);
    d_textureTargets.push_back(t);
    return t;
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::destroyTextureTarget(TextureTarget* target)
{
    TextureTargetList::iterator i = std::find(d_textureTargets.begin(),
                                    d_textureTargets.end(),
                                    target);

    if (d_textureTargets.end() != i)
    {
        d_textureTargets.erase(i);
        delete target;
    }
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::destroyAllTextureTargets()
{
    while (!d_textureTargets.empty())
        destroyTextureTarget(*d_textureTargets.begin());
}

//----------------------------------------------------------------------------//
Texture& Direct3D9Renderer::createTexture()
{
    Direct3D9Texture* tex = new Direct3D9Texture(*this);
    d_textures.push_back(tex);
    return *tex;
}

//----------------------------------------------------------------------------//
Texture& Direct3D9Renderer::createTexture(const String& filename, const String& resourceGroup, float priority)
{
    Direct3D9Texture* tex = new Direct3D9Texture(*this, filename, resourceGroup);
    d_textures.push_back(tex);
    return *tex;
}

//----------------------------------------------------------------------------//
Texture& Direct3D9Renderer::createTexture(const Size& size)
{
    Direct3D9Texture* tex = new Direct3D9Texture(*this, size);
    d_textures.push_back(tex);
    return *tex;
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::destroyTexture(Texture& texture)
{
    TextureList::iterator i = std::find(d_textures.begin(),
                                        d_textures.end(),
                                        &texture);

    if (d_textures.end() != i)
    {
        d_textures.erase(i);
        delete &static_cast<Direct3D9Texture&>(texture);
    }
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::destroyAllTextures()
{
    while (!d_textures.empty())
        destroyTexture(**d_textures.begin());
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::beginRendering()
{
    SaveXPRenderState();

	d_device->SetFVF(D3DFVF_XYZ | D3DFVF_DIFFUSE | D3DFVF_TEX1);
    // no shaders initially
	
   d_device->SetVertexShader(0);
   d_device->SetPixelShader(0);

    //// set device states
    d_device->SetRenderState(D3DRS_LIGHTING, FALSE);
    d_device->SetRenderState(D3DRS_FOGENABLE, FALSE);
    d_device->SetRenderState(D3DRS_ZENABLE, D3DZB_FALSE);
   // d_device->SetRenderState(D3DRS_ALPHATESTENABLE, true);
    d_device->SetRenderState(D3DRS_CULLMODE, D3DCULL_NONE);
    d_device->SetRenderState(D3DRS_FILLMODE, D3DFILL_SOLID);
    d_device->SetRenderState(D3DRS_SCISSORTESTENABLE, TRUE); //影响小胖渲染的地方
    d_device->SetRenderState(D3DRS_ZWRITEENABLE, FALSE);

    // setup texture addressing settings
    d_device->SetSamplerState(0, D3DSAMP_ADDRESSU, D3DTADDRESS_CLAMP);
    d_device->SetSamplerState(0, D3DSAMP_ADDRESSV, D3DTADDRESS_CLAMP);

    // setup colour calculations
    d_device->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
    d_device->SetTextureStageState(0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
    d_device->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_MODULATE);

    // setup alpha calculations
    d_device->SetTextureStageState(0, D3DTSS_ALPHAARG1, D3DTA_TEXTURE);
    d_device->SetTextureStageState(0, D3DTSS_ALPHAARG2, D3DTA_DIFFUSE);
    d_device->SetTextureStageState(0, D3DTSS_ALPHAOP, D3DTOP_MODULATE);

    // setup filtering
	if (d_PointMode)
	{
		d_device->SetSamplerState(0, D3DSAMP_MINFILTER, D3DTEXF_POINT);
		d_device->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_POINT);
	}else
	{
		d_device->SetSamplerState(0, D3DSAMP_MINFILTER, D3DTEXF_LINEAR);
		d_device->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR);

	}
    

    // disable texture stages we do not need.
    d_device->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_DISABLE);

    // setup scene alpha blending
    d_device->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
    d_device->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
    d_device->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);

	if (d_SeparateAlphaBlendCap)
	{
		/*d_device->SetRenderState(D3DRS_SEPARATEALPHABLENDENABLE, TRUE);
		d_device->SetRenderState(D3DRS_SRCBLENDALPHA, D3DBLEND_ONE);
		d_device->SetRenderState(D3DRS_DESTBLENDALPHA, D3DBLEND_ONE);
		d_device->SetRenderState(D3DRS_BLENDOPALPHA, D3DBLENDOP_MAX);*/
	}
	
    // set view matrix back to identity.
    d_device->SetTransform(D3DTS_VIEW, &s_identityMatrix);
	
}

void Direct3D9Renderer::InitRenderStateType()
{
	//renderstate
	d_mapRenderState[D3DRS_LIGHTING]=0;
	d_mapRenderState[D3DRS_FOGENABLE]=0;
	d_mapRenderState[D3DRS_ZENABLE]=0;
	d_mapRenderState[D3DRS_ALPHATESTENABLE]=0;
	d_mapRenderState[D3DRS_CULLMODE]=0;
	d_mapRenderState[D3DRS_FILLMODE]=0;
	d_mapRenderState[D3DRS_SCISSORTESTENABLE]=0;
	d_mapRenderState[D3DRS_ZWRITEENABLE]=0;
	d_mapRenderState[D3DRS_ALPHABLENDENABLE]=0;
	d_mapRenderState[D3DRS_SRCBLEND]=0;
	d_mapRenderState[D3DRS_DESTBLEND]=0;

	d_mapRenderState[D3DRS_SEPARATEALPHABLENDENABLE]=0;
	d_mapRenderState[D3DRS_SRCBLENDALPHA]=0;
	d_mapRenderState[D3DRS_DESTBLENDALPHA]=0;
	d_mapRenderState[D3DRS_BLENDOPALPHA]=0;

	//texture 0stage state
	d_mapTextureStage0State[D3DTSS_COLORARG1]=0;
	d_mapTextureStage0State[D3DTSS_COLORARG2]=0;
	d_mapTextureStage0State[D3DTSS_COLOROP]=0;
	d_mapTextureStage0State[D3DTSS_ALPHAARG1]=0;
	d_mapTextureStage0State[D3DTSS_ALPHAARG2]=0;
	d_mapTextureStage0State[D3DTSS_ALPHAOP]=0;

	//texture 1stage state
	d_mapTextureStage1State[D3DTSS_COLOROP]=0;
	
	//SampleState
	d_mapSampleState[D3DSAMP_MINFILTER]=0;
	d_mapSampleState[D3DSAMP_MAGFILTER]=0;
	d_mapSampleState[D3DSAMP_ADDRESSU]=0;
	d_mapSampleState[D3DSAMP_ADDRESSV]=0;

}
//void Direct3D9Renderer::SaveRenderState()
//{
//	
//	/*d_device->GetFVF(&d_OldFVF);
//	d_device->GetPixelShader(&d_oldPixShader);
//	d_device->GetVertexShader(&d_oldVertextShader);
//
//	RenderStateMap::iterator renderStateIt=d_mapRenderState.begin();
//	for (;renderStateIt!=d_mapRenderState.end();++renderStateIt)
//	{
//		DWORD renderstatevalue=NULL;
//		d_device->GetRenderState(renderStateIt->first,&renderstatevalue);
//		d_mapRenderState[renderStateIt->first]=renderstatevalue;
//	}
//
//	TextureStatgeStateType::iterator TextureStage0StateIt=d_mapTextureStage0State.begin();
//	for (;TextureStage0StateIt!=d_mapTextureStage0State.end();++TextureStage0StateIt)
//	{
//		DWORD textureState0=NULL;
//		d_device->GetTextureStageState(0,TextureStage0StateIt->first,&textureState0);
//		d_mapTextureStage0State[TextureStage0StateIt->first]=textureState0;
//	}
//
//	TextureStatgeStateType::iterator TextureStage1StateIt=d_mapTextureStage1State.begin();
//	for (;TextureStage1StateIt!=d_mapTextureStage1State.end();++TextureStage1StateIt)
//	{
//		DWORD textureState1=NULL;
//		d_device->GetTextureStageState(1,TextureStage1StateIt->first,&textureState1);
//		d_mapTextureStage1State[TextureStage1StateIt->first]=textureState1;
//	}
//
//	SampleStateMap::iterator sampleSateIt=d_mapSampleState.begin();
//	for (;sampleSateIt!=d_mapSampleState.end();++sampleSateIt)
//	{
//		DWORD sampleStateValue=NULL;
//		d_device->GetSamplerState(0,sampleSateIt->first,&sampleStateValue);
//		d_mapSampleState[sampleSateIt->first]=sampleStateValue;
//	}*/
//
//}


void Direct3D9Renderer::SaveXPRenderState()
{
	/*if (d_XPStateBlock)
	{
		d_XPStateBlock->Release();
		d_XPStateBlock=NULL;
	}
	d_device->CreateStateBlock(D3DSBT_ALL,&d_XPStateBlock);*/

	d_device->GetFVF(&d_OldFVF);
    d_device->GetPixelShader(&d_oldPixShader);
	d_device->GetVertexShader(&d_oldVertextShader);
}
void Direct3D9Renderer::SaveUIRenderState()
{
	/*if (d_UISatteBlock)
	{
		d_UISatteBlock->Release();
		d_UISatteBlock=NULL;
	}
	d_device->CreateStateBlock(D3DSBT_ALL,&d_UISatteBlock);*/
}
void Direct3D9Renderer::RestoreXPRenderState()
{
	/*if (d_XPStateBlock)
	{
		d_XPStateBlock->Apply();
	}*/
	d_device->SetFVF(d_OldFVF);
	d_device->SetVertexShader(d_oldVertextShader);
	d_device->SetPixelShader(d_oldPixShader);
	d_device->SetRenderState(D3DRS_SCISSORTESTENABLE, false); //影响小胖渲染的地方
	d_device->SetSamplerState(0, D3DSAMP_MINFILTER, D3DTEXF_LINEAR);
	d_device->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR);

	OnRestoreXPRenderState* pFunc=System::getSingleton().GetRestoreXPStateFunc();
	if (pFunc)
	{
		(*pFunc)();
	}
	
}
void Direct3D9Renderer::RestorUIRenderState()
{
	/*if (d_UISatteBlock)
	{
	   d_UISatteBlock->Apply();
	}*/
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::endRendering()
{
	//d_device->SetRenderState(D3DRS_SCISSORTESTENABLE, FALSE); //影响小胖渲染的地方
	/*d_device->SetRenderState(D3DRS_SEPARATEALPHABLENDENABLE, FALSE);

	d_device->SetRenderState(D3DRS_SRCBLENDALPHA, D3DBLEND_SRCALPHA);
	d_device->SetRenderState(D3DRS_DESTBLENDALPHA, D3DBLEND_INVSRCALPHA);
	d_device->SetRenderState(D3DRS_BLENDOPALPHA, D3DBLENDOP_ADD);*/
	/*d_device->SetRenderState(D3DRS_SRCBLEN, D3DBLEND_SRCALPHA);
	d_device->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);*/
	//d_device->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
	//d_device->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);

	//SaveUIRenderState();
	RestoreXPRenderState();
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::setDisplaySize(const Size& sz)
{
    if (sz != d_displaySize)
    {
        d_displaySize = sz;

        // FIXME: This is probably not the right thing to do in all cases.
        Rect area(d_defaultTarget->getArea());
        area.setSize(sz);
        d_defaultTarget->setArea(area);
    }
}

//----------------------------------------------------------------------------//
const Size& Direct3D9Renderer::getDisplaySize() const
{
    return d_displaySize;
}

//----------------------------------------------------------------------------//
const Vector2& Direct3D9Renderer::getDisplayDPI() const
{
    return d_displayDPI;
}

//----------------------------------------------------------------------------//
uint Direct3D9Renderer::getMaxTextureSize() const
{
    return d_maxTextureSize;
}

//----------------------------------------------------------------------------//
const String& Direct3D9Renderer::getIdentifierString() const
{
    return d_rendererID;
}

//----------------------------------------------------------------------------//
Direct3D9Renderer::Direct3D9Renderer(LPDIRECT3DDEVICE9 device) :
    d_device(device),
    d_displaySize(getViewportSize()),
    d_displayDPI(96, 96),
    d_defaultRoot(0),
    d_defaultTarget(0),
	d_SeparateAlphaBlendCap(false),
	d_OldFVF(NULL),
	d_oldPixShader(0),
	d_oldVertextShader(0),
	d_XPStateBlock(NULL),
	d_UISatteBlock(NULL),
	d_PointMode(true)
{
    D3DCAPS9 caps;
    device->GetDeviceCaps(&caps);

    if (!caps.RasterCaps && D3DPRASTERCAPS_SCISSORTEST)
        throw RendererException("Direct3D9Renderer: Hardware does not support "
                                "D3DPRASTERCAPS_SCISSORTEST.  Unable to proceed.");

    d_maxTextureSize = ceguimin(caps.MaxTextureHeight, caps.MaxTextureWidth);

    d_supportNonSquareTex = !(caps.TextureCaps & D3DPTEXTURECAPS_SQUAREONLY);

    d_supportNPOTTex = !(caps.TextureCaps & D3DPTEXTURECAPS_POW2) ||
                       (caps.TextureCaps & D3DPTEXTURECAPS_NONPOW2CONDITIONAL);
	d_SeparateAlphaBlendCap=(caps.PrimitiveMiscCaps & D3DPMISCCAPS_SEPARATEALPHABLEND) != 0;

    d_defaultTarget = new Direct3D9ViewportTarget(*this);
    d_defaultRoot = new RenderingRoot(*d_defaultTarget);

	InitRenderStateType();
}

//----------------------------------------------------------------------------//
Direct3D9Renderer::~Direct3D9Renderer()
{
    destroyAllGeometryBuffers();
    destroyAllTextureTargets();
    destroyAllTextures();

    delete d_defaultRoot;
    delete d_defaultTarget;
	if (d_XPStateBlock)
	{
		d_XPStateBlock->Release();
		d_XPStateBlock=NULL;
	}
	if (d_UISatteBlock)
	{
		d_UISatteBlock->Release();
		d_UISatteBlock=NULL;
	}
}

//----------------------------------------------------------------------------//
Size Direct3D9Renderer::getViewportSize()
{
    D3DVIEWPORT9 vp;

    if (FAILED(d_device->GetViewport(&vp)))
        throw RendererException("Direct3D9Renderer::getViewportSize - Unable "
                                "to access required view port information from "
                                "Direct3DDevice9.");
    else
        return Size(static_cast<float>(vp.Width),
                    static_cast<float>(vp.Height));
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::preD3DReset()
{
    // perform pre-reset on texture targets
    TextureTargetList::iterator target_iterator = d_textureTargets.begin();
    for (; target_iterator != d_textureTargets.end(); ++target_iterator)
        static_cast<Direct3D9TextureTarget*>(*target_iterator)->preD3DReset();

    // perform pre-reset on textures
    TextureList::iterator texture_iterator = d_textures.begin();
    for (; texture_iterator != d_textures.end(); ++texture_iterator)
        (*texture_iterator)->preD3DReset();

	if (d_UISatteBlock)
	{
		d_UISatteBlock->Release();
		d_UISatteBlock=NULL;
	}

	if (d_XPStateBlock)
	{
		d_XPStateBlock->Release();
		d_XPStateBlock=NULL;
	}
}

//----------------------------------------------------------------------------//
void Direct3D9Renderer::postD3DReset()
{
    // perform post-reset on textures
    TextureList::iterator texture_iterator = d_textures.begin();
    for (; texture_iterator != d_textures.end(); ++texture_iterator)
        (*texture_iterator)->postD3DReset();

    // perform post-reset on texture targets
    TextureTargetList::iterator target_iterator = d_textureTargets.begin();
    for (; target_iterator != d_textureTargets.end(); ++target_iterator)
        static_cast<Direct3D9TextureTarget*>(*target_iterator)->postD3DReset();

    // notify system about the (possibly) new viewport size.
    System::getSingleton().notifyDisplaySizeChanged(getViewportSize());
}

//----------------------------------------------------------------------------//
LPDIRECT3DDEVICE9 Direct3D9Renderer::getDevice() const
{
    return d_device;
}

//----------------------------------------------------------------------------//
Texture& Direct3D9Renderer::createTexture(LPDIRECT3DTEXTURE9 texture)
{
    Direct3D9Texture* tex = new Direct3D9Texture(*this, texture);
    d_textures.push_back(tex);
    return *tex;
}

//----------------------------------------------------------------------------//
bool Direct3D9Renderer::supportsNonSquareTexture()
{
    return d_supportNonSquareTex;
}

//----------------------------------------------------------------------------//
bool Direct3D9Renderer::supportsNPOTTextures()
{
    return d_supportNPOTTex;
}

//----------------------------------------------------------------------------//
Size Direct3D9Renderer::getAdjustedSize(const Size& sz)
{
    Size s(sz);

    if (!d_supportNPOTTex)
    {
        s.d_width  = getSizeNextPOT(sz.d_width);
        s.d_height = getSizeNextPOT(sz.d_height);
    }
    if (!d_supportNonSquareTex)
        s.d_width = s.d_height =
                        ceguimax(s.d_width, s.d_height);

    return s;
}

//----------------------------------------------------------------------------//
float Direct3D9Renderer::getSizeNextPOT(float sz) const
{
    uint size = static_cast<uint>(sz);

    // if not power of 2
    if ((size & (size - 1)) || !size)
    {
        int log = 0;

        // get integer log of 'size' to base 2
        while (size >>= 1)
            ++log;

        // use log to calculate value to use as size.
        size = (2 << log);
    }

    return static_cast<float>(size);
}
void Direct3D9Renderer::Reset()
{
	d_defaultRoot->Reset();
}


void Direct3D9Renderer::ResetRenderTextures()  //清空渲染过的纹理
{
	d_RenderTextures.clear();

}
void Direct3D9Renderer::MarkRenderTexture(Texture* pTexture)
{
	RenderTextureList::iterator it=std::find(d_RenderTextures.begin(),d_RenderTextures.end(),pTexture);
	if (it==d_RenderTextures.end())
	{
		d_RenderTextures.push_back(static_cast<Direct3D9Texture*>(pTexture));

	}
	

}
bool Direct3D9Renderer::isTextureRender(Texture& texture)
{

	RenderTextureList::iterator it=std::find(d_RenderTextures.begin(),d_RenderTextures.end(),&texture);
	if (it!=d_RenderTextures.end())
	{
		return true;
	}
	return false;
}
void Direct3D9Renderer::ReleaseTexture(Texture& pTexture)
{
	destroyTexture(pTexture);


	RenderTextureList::iterator it=std::find(d_RenderTextures.begin(),d_RenderTextures.end(),&pTexture);
	if (it!=d_RenderTextures.end())
	{
		d_RenderTextures.erase(it);
	}

}

void Direct3D9Renderer::MarkRenderTexture(LPDIRECT3DTEXTURE9 pD3D9Texture)
{
	TextureList::iterator it=d_textures.begin();
	for (;it!=d_textures.end();++it)
	{
		//Direct3D9Texture* pTexture=(*it);
		if ((*it)->getDirect3D9Texture()==pD3D9Texture)
		{
			MarkRenderTexture((Texture*)(*it));
			break;
		}
	}
	
}

void Direct3D9Renderer::SetPointMode(bool b)
{
	d_PointMode=b;
	

}

bool Direct3D9Renderer::isTextureValid(LPDIRECT3DTEXTURE9 pD3D9Texture)
{
	TextureList::iterator it=d_textures.begin();
	for (;it!=d_textures.end();++it)
	{
		//Direct3D9Texture* pTexture=(*it);
		if ((*it)->getDirect3D9Texture()==pD3D9Texture)
		{
			return true;
		}
	}
	return false;
}



//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
