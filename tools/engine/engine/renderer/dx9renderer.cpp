//#define DRAW_BATCH_PIC_SSE
#include "stdafx.h"
#include <algorithm>
#include "..\common\log.h"
#include "..\common\pngwriter.h"
#include "..\common\fileiomanager.h"
#include "..\..\contrib\libwebp\include\webp\decode.h"
#include "dx9renderer.h"
#include "d3dx9tex.h"
#include "tchar.h"
#include "fonttexture.h"
#include "fontmanager.h"
#include "fxcode.h"


namespace Nuclear
{
	// 初始为空
	void DX9Renderer::Init()
	{
		m_bDeviceLost = false;
		m_adapterid	= D3DADAPTER_DEFAULT;
		m_devtype = D3DDEVTYPE_HAL;
		m_ifD3D = NULL;
		m_ifDevice = NULL;
		m_hwnd = NULL;
		m_pSrcRect = NULL;
		m_pDstRect = NULL;
		m_ifDefaultFont = NULL;
		m_TCVERTEXVB = NULL;
		m_TCVERTEXIB = NULL;
		m_ifTmpFont = NULL;
		m_picHandleSeed = 1;
		m_bTextureCaps_pow2 = false;

		m_dwPixelShaderVersion = 0;
		m_dwVertexShaderVersion = 0;

		m_fUseCursor = false;
		m_iCurCursorHandle = INVALID_PICTURE_HANDLE;
		m_fCurCursorOffsetX = 0;
		m_fCurCursorOffsetY = 0;
		m_pFontMan = NULL;
		m_pParticleMan = NULL;
		m_batchIB = NULL;
		m_batchVB = NULL;
		m_shapeVB = NULL;
		m_particleVB = NULL;
		m_particleUVB = NULL;
		m_particleIB = NULL;
		memset(&m_performance, 0, sizeof(m_performance));

		m_pEllipsePicVertex = NULL; //加个圆心和第一个边界点

		m_iBatchVBOffset = 0;
		m_iBatchIBOffset = 0;
		m_iBatchTCVERTEXVBOffset = 0;
		m_iBatchTCVERTEXIBOffset = 0;

		m_pdshowTexRender = NULL;


		//-Mended 2015
	    iPicWidthDelta	= 20;
		iPicHeightDelta = 15;
		iPicCounter     = 0;
		iIndexOfRect    = 0;
		bStartToRender  = false;
		//-

		//for(int i=0; i<10; i++)
		//	m_Pow2Num[i] = (int)pow((float)2, (float)(i+1));
	}

	void DX9Renderer::ReleaseVideoRenderRes()
	{
		XPSAFE_DELETE(m_pdshowTexRender);
	}

	bool DX9Renderer::InitDShowTextureRenderer(const std::wstring& strVideoFile)
	{
		HRESULT hr = S_OK;

		bool bUseDynamicTextures;
		if( m_d3dCaps.Caps2 & D3DCAPS2_DYNAMICTEXTURES )
			bUseDynamicTextures = true;

		m_pdshowTexRender = new VideoTexture(m_ifDevice); //, m_pFileIOMan);
		if(m_pdshowTexRender == NULL)
			return false;

 		if( !(m_pdshowTexRender->init(strVideoFile, m_bTextureCaps_pow2)) )
		{
			XPSAFE_DELETE(m_pdshowTexRender);
 			return false;
		}
 
		return true;
	}

	bool DX9Renderer::DrawVideoTexture(const FRECT& frct, bool bZoom, const XPMATRIX3* pMatrix, XPCOLOR color)
	{
		if(m_pdshowTexRender == NULL)
			return false;

		XPVECTOR2 vecOut;
		XPVECTOR2 thePos[4];
		//左上
		thePos[0].x = frct.left;
		thePos[0].y = frct.top;
		//右上
		thePos[1].x = frct.right;//thePos[0].x + rect.Width();
		thePos[1].y = thePos[0].y;
		//右下
		thePos[2].x = thePos[1].x;
		thePos[2].y = frct.bottom;//thePos[0].y + rect.Height();
		//左下
		thePos[3].x = thePos[0].x;
		thePos[3].y = thePos[2].y;
		if(pMatrix != NULL)
		{
			//m_ifDevice->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR );
			for (int i=0;i<4;++i)
			{
				XPVec3TransformCoord(&vecOut, thePos+i, pMatrix);
				thePos[i] = vecOut;
			}
			//XPTRACE(L"%f, %f, %f, %f\n", posx1, posy1, posx2, posy2);
		}
		
		float fu = 1.0f;
		float fv = 1.0f;
		if(!bZoom) //裁剪
		{
			int nwidth, nheight;
			m_pdshowTexRender->getMovieDimensions(nwidth, nheight);
			fu = frct.Width()/nwidth;
			fv = frct.Height()/nheight;
		}

		//纹理数据是按照bmp格式(倒着的)填充的..
		TCT1VERTEX vertex[] = {
			TCT1VERTEX(thePos[0].x, thePos[0].y, Z, 1.0f, color.data, 0.0f, 1.0f),
			TCT1VERTEX(thePos[1].x, thePos[1].y, Z, 1.0f, color.data, fu, 1.0f),
			TCT1VERTEX(thePos[2].x, thePos[2].y, Z, 1.0f, color.data, fu, 1.0f-fv),
			TCT1VERTEX(thePos[3].x, thePos[3].y, Z, 1.0f, color.data, 0.0f, 1-fv)
		};

		m_ifDevice->SetFVF(TCT1VertexFVF);

		SetAlphaBlendSrcType(XPBLEND_ONE); //XPBLEND_SRCALPHA
		SetAlphaBlendDstType(XPBLEND_ZERO); //XPBLEND_INVSRCALPHA
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_DEFAULT);

		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		m_ifDevice->SetTextureStageState(0, D3DTSS_ALPHAOP, D3DTOP_SELECTARG2); //纹理里面的alpha值都填为0了
		m_pdshowTexRender->setTexture();
		HRESULT hr = m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, 2, vertex, sizeof(TCT1VERTEX));
		m_ifDevice->SetTextureStageState(0, D3DTSS_ALPHAOP, D3DTOP_MODULATE); //恢复默认

		m_stateMan.SelEffect(oldEffect);

		// Check to see if we need to restart the movie
		m_pdshowTexRender->Loop();

	//	if(!m_pdshowTexRender->isPlayingMovie())
	//		m_pdshowTexRender->playMovie();

		return true;
	}

	std::wstring DX9Renderer::GetAdapterDescription()
	{
		return m_adapterDes;
	}

	// 创建
	bool DX9Renderer::Create(HWND hwnd, const XDisplayMode &dmode, DWORD flags) 
	{ 
		// TODO 分析 flags参数
		//
		HRESULT hr;
		// 获得D3D9接口
		m_ifD3D = Direct3DCreate9(D3D_SDK_VERSION);
		if( m_ifD3D == NULL ) return false;
		

		// Look for 'NVIDIA PerfHUD' adapter
		// If it is present, override default settings
		for (UINT Adapter=0;Adapter<m_ifD3D->GetAdapterCount();Adapter++)
		{
			D3DADAPTER_IDENTIFIER9 Identifier;
			HRESULT Res;
			Res = m_ifD3D->GetAdapterIdentifier(Adapter,0,&Identifier);
			if (strstr(Identifier.Description,"PerfHUD") != 0)
			{
				m_adapterid = Adapter;
				m_devtype = D3DDEVTYPE_REF;

				break;
			}
		}

		InitDisplayMode(m_ifD3D, dmode);

		// 设置参数
		D3DCAPS9 caps;
		m_ifD3D->GetDeviceCaps(m_adapterid, m_devtype, &caps);

		DWORD createflags = 0;

	//	http://msdn.microsoft.com/en-us/library/bb219721.aspx#What_is_the_purpose_of_the_D3DCREATE_PUREDEVICE_flag

		if( caps.DevCaps & D3DDEVCAPS_PUREDEVICE )
			createflags |= D3DCREATE_PUREDEVICE;
		else
			XPLOG_ERROR(L"no D3DCREATE_PUREDEVICE");

		if( caps.DevCaps & D3DDEVCAPS_HWTRANSFORMANDLIGHT )
			createflags |= D3DCREATE_HARDWARE_VERTEXPROCESSING;
		else
		{
			createflags |= D3DCREATE_SOFTWARE_VERTEXPROCESSING;
			XPLOG_ERROR(L"D3DCREATE_SOFTWARE_VERTEXPROCESSING");
		}

		m_d3dpp.BackBufferWidth            = m_curDisplayMode.width;
		m_d3dpp.BackBufferHeight           = m_curDisplayMode.height;
		m_d3dpp.BackBufferFormat           = (m_curDisplayMode.bitperpix == 16 ? D3DFMT_R5G6B5 : D3DFMT_A8R8G8B8);
		m_d3dpp.BackBufferCount            = 1;
		m_d3dpp.MultiSampleType            = D3DMULTISAMPLE_NONE;
		m_d3dpp.MultiSampleQuality         = 0;
		m_d3dpp.SwapEffect                 = D3DSWAPEFFECT_DISCARD; 
		m_d3dpp.hDeviceWindow              = hwnd;
		m_d3dpp.Windowed                   = dmode.screenMode != XPSM_REAL_FULL_SCREEN;
		m_d3dpp.EnableAutoDepthStencil     = false; 
		m_d3dpp.AutoDepthStencilFormat     = D3DFMT_D24S8;
		m_d3dpp.Flags                      = 0;
		m_d3dpp.FullScreen_RefreshRateInHz = D3DPRESENT_RATE_DEFAULT;
		//m_d3dpp.PresentationInterval       = (flags & XPCRF_VERTICALSYNC) ? D3DPRESENT_INTERVAL_DEFAULT : D3DPRESENT_INTERVAL_IMMEDIATE;
		m_d3dpp.PresentationInterval       = (flags & XPCRF_VERTICALSYNC) ? D3DPRESENT_INTERVAL_ONE : D3DPRESENT_INTERVAL_IMMEDIATE;
		//m_d3dpp.NumSimultaneousRTs		   = 2;
		

		createflags |= D3DCREATE_MULTITHREADED;

		// 获得D3D9设备接口
		hr = m_ifD3D->CreateDevice(
			m_adapterid, // primary adapter
			m_devtype,         // device type
			hwnd,               // window associated with device
			createflags,                 
			&m_d3dpp,             // present parameters
			&m_ifDevice);            // return created device
		
		D3DADAPTER_IDENTIFIER9 identifier;
		if( SUCCEEDED(m_ifD3D->GetAdapterIdentifier(m_adapterid,0,&identifier)))
		{
			m_adapterDes = CharArrayToUString(identifier.Description, MAX_DEVICE_IDENTIFIER_STRING);
		}
		// 销毁D3D9接口
		//XPSAFE_RELEASE(ifD3D9);
		if( FAILED(hr) ) 
		{
			XPLOG_ERROR(L"  创建渲染器失败，错误码: %d\n", hr);
			return false;
		}

		m_ifDevice->GetDeviceCaps(&m_d3dCaps);
		
		bool bTexturePow2 = ( D3DPTEXTURECAPS_POW2 & m_d3dCaps.TextureCaps ) != 0;
		bool bTextureNonPow2Conditional = ( D3DPTEXTURECAPS_NONPOW2CONDITIONAL & m_d3dCaps.TextureCaps ) != 0;
		m_bTextureCaps_pow2 = bTexturePow2 && (!bTextureNonPow2Conditional);

		if(m_bTextureCaps_pow2)
		{
			XPLOG_INFO(L"  不支持非2幂纹理\n");
		}

		//在这里把条件支持当作支持处理了，使用中应该注意:
		// Device can use non-POW2 textures if:
		//  1) D3DTEXTURE_ADDRESS is set to D3DTADDRESS_CLAMP for this texture's stage
		//  2) D3DRS_WRAP(N) is zero for this texture's coordinates
		//  3) mip mapping is not enabled (use magnification filter only)

		m_dwPixelShaderVersion = m_d3dCaps.PixelShaderVersion;
		m_dwVertexShaderVersion = m_d3dCaps.VertexShaderVersion;

		if( !m_stateMan.Init(m_ifDevice, m_d3dCaps) )
		{
			XPLOG_ERROR(L"  初始化渲染器状态失败!\n");
			return false;
		}

		m_pParticleMan = new ParticleManager(this, m_pFileIOMan);

		m_pParticleMan->SetParticlePath(L"/effect/particle/");

		//todo init fontman
		
		m_pFontMan = new CFontManager();
		m_pFontMan->Init(this, m_pFileIOMan);
		// 初始化默认字体
		InitDefaultFont();
		InitBatchVB();
		m_hwnd = hwnd;

		m_pEllipsePicVertex = new TCT1VERTEX[30+2]; //加个圆心和第一个边界点

		// 调试用
		if(m_pFileIOMan != NULL)
			m_pFileIOMan->GetFileImage(L"/error/error.dds", m_errTexBuf);
		return true; 
	}

	bool DX9Renderer::InitDefaultFont()
	{
		D3DXFONT_DESC df = {
			14, 0, FW_NORMAL, D3DX_DEFAULT, FALSE, GB2312_CHARSET, 0, 0, 0, L"宋体"
		};
		D3DXFONT_DESC df1 = {
			16, 0, FW_NORMAL, D3DX_DEFAULT, FALSE, GB2312_CHARSET, 0, 0, 0, L"方正准圆_GBK"
		};
		BOOL result = SUCCEEDED(D3DXCreateFontIndirect(m_ifDevice, &df1, &m_ifTmpFont));

		return SUCCEEDED(D3DXCreateFontIndirect(m_ifDevice, &df, &m_ifDefaultFont));
	}

	// 销毁
	void DX9Renderer::Destroy() 
	{ 
		if( m_pFontMan )
		{
			m_pFontMan->Release();
			delete m_pFontMan;
		}

		XPSAFE_DELETE(m_pParticleMan);

		// 销毁图库
		int cnt=0;
		int i=0;
		for(PictureMap::iterator it = m_mapPictures.begin(), ie = m_mapPictures.end(); it != ie; ++it)
		{
			cnt = (int)(it->second.m_CTextureInfoVector.size());
			for(i=0; i<cnt; i++)
			{
				IDirect3DTexture9* ifTexture = it->second.m_CTextureInfoVector[i].m_D3dTexture;
				XPSAFE_RELEASE(ifTexture);
			}
		}
		m_mapPictures.clear();

		if( m_pSrcRect) XFree(m_pSrcRect);
		if( m_pDstRect) XFree(m_pDstRect);

		delete []m_pEllipsePicVertex;

		m_stateMan.Destroy();
		DestroyBatchVB();

		// 销毁D3D9设备接口
		XPSAFE_RELEASE(m_ifTmpFont);
		XPSAFE_RELEASE(m_ifDefaultFont);
		XPSAFE_RELEASE(m_ifDevice);
		XPSAFE_RELEASE(m_ifD3D);

		ReleaseVideoRenderRes();
	}

	// 释放加载在显存中的纹理
	void DX9Renderer::ReleaseTexture()
	{
		for(PictureMap::iterator it = m_mapPictures.begin(); it != m_mapPictures.end();)
		{
			if( it->second.m_info.m_pooltype == XPPOOL_VIDEOMEM )
			{
				int cnt = (int)(it->second.m_CTextureInfoVector.size());
				for(int i=0; i<cnt; i++)
				{
#ifdef XP_PERFORMANCE
					m_performance.m_iManagedTextureCount--;
					m_performance.m_iManagedTextureMemory -= it->second.m_CTextureInfoVector[i].mem;
#endif
					IDirect3DTexture9* ifTexture = it->second.m_CTextureInfoVector[i].m_D3dTexture;
					XPSAFE_RELEASE(ifTexture);
				}
				it = m_mapPictures.erase(it);
			}
			else
				++it;			
		}
	}


	// 画线
	bool DX9Renderer::DrawLine(const FRECT &line, XPCOLOR color) 
	{ 
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawLineCountPerFrame++;
#endif
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);
		//
		TCVERTEX vertex[] = {
			TCVERTEX(line.left, line.top, Z, 1.0f, color.data), 
			TCVERTEX(line.right, line.bottom, Z, 1.0f, color.data)
		};

		m_ifDevice->SetFVF( TCVertexFVF);
		HRESULT hr = m_ifDevice->DrawPrimitiveUP(D3DPT_LINELIST, 1, vertex, sizeof(TCVERTEX));
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		//
		m_stateMan.SelEffect(oldEffect);
		return hr == S_OK;
	}

	bool DX9Renderer::DrawLine(const FRECT *lines, int n, XPCOLOR color) 
	{ 
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawLineCountPerFrame += n;
#endif
		TCVERTEX *vertex = (TCVERTEX*)XAlloc(sizeof(TCVERTEX)*2*n);

		if( vertex == NULL ) return false;

		m_ifDevice->SetFVF( TCVertexFVF);
		//m_stateMan.SetState(StateManager::RS_DIFFUSEBLEND);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		for(int i=0; i<n; ++i)
		{
			vertex[2*i].x = lines[i].left;
			vertex[2*i].y = lines[i].top;
			vertex[2*i].z = Z;
			vertex[2*i].rhw = 1.0f;
			vertex[2*i].c = color.data;

			vertex[2*i+1].x = lines[i].right;
			vertex[2*i+1].y = lines[i].bottom;
			vertex[2*i+1].z = Z;
			vertex[2*i+1].rhw = 1.0f;
			vertex[2*i+1].c = color.data;
		}

		HRESULT hr = m_ifDevice->DrawPrimitiveUP(D3DPT_LINELIST, n, vertex, sizeof(TCVERTEX));
#ifdef XP_PERFORMANCE
	m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif

		XFree(vertex);
		m_stateMan.SelEffect(oldEffect);
		return hr == S_OK; 
	}

	// 画填充三角形
	bool DX9Renderer::DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, XPCOLOR color)
	{
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawTriangleCountPerFrame++;
#endif
		TCVERTEX vertex[] =
		{
			TCVERTEX(x1, y1, Z, 1.0f, color.data),
			TCVERTEX(x2, y2, Z, 1.0f, color.data),
			TCVERTEX(x3, y3, Z, 1.0f, color.data),
		};
		m_ifDevice->SetFVF(TCVertexFVF);
		//m_stateMan.SetState(StateManager::RS_DIFFUSEBLEND);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLELIST, 1, vertex, sizeof(TCVERTEX));
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		m_stateMan.SelEffect(oldEffect);
		return true;
	}

	bool DX9Renderer::DrawEllipsePicture(const DrawEllipsePictureParam& param)
	{
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawCircleCountPerFrame++;
#endif

		PictureMap::iterator it = m_mapPictures.find(param.handle);
		if( it == m_mapPictures.end() )
			return false;
		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1)
		{//要求纹理必须是2的幂
			return false;
		}

	//	int nPicWidth = it->second.m_info.m_nPicWidth;		// 原始图片宽度
	//	int nPicHeight = it->second.m_info.m_nPicHeight;    // 原始图片高度
		CTextureInfo texInfo = it->second.m_CTextureInfoVector[0];
		if( FAILED( m_ifDevice->SetTexture(0, texInfo.m_D3dTexture) ))
			return false;

		m_ifDevice->SetSamplerState(0, D3DSAMP_ADDRESSU, param.TextureMode);
		m_ifDevice->SetSamplerState(0, D3DSAMP_ADDRESSV, param.TextureMode);
		if(param.TextureMode == XPTEXADDRESS_BORDER)
		{
			m_ifDevice->SetSamplerState(0, D3DSAMP_BORDERCOLOR, param.borderColor.data);
		}

		int cnt = 30;		
		m_pEllipsePicVertex[0].x = param.x;
		m_pEllipsePicVertex[0].y = param.y;
		m_pEllipsePicVertex[0].u = param.u0; //x/(float)nPicWidth
		m_pEllipsePicVertex[0].v = param.v0; //y/(float)nPicHeight
		m_pEllipsePicVertex[0].z = Z;
		m_pEllipsePicVertex[0].rhw = 1.0f;
		m_pEllipsePicVertex[0].c = param.color.data;

		int nAngle = 360/cnt;
		for(int i=1; i<cnt+2; i++)
		{
			float vertexx = param.rx*XPCOS(i*nAngle);
			float vertexy = param.ry*XPSIN(i*nAngle);

			m_pEllipsePicVertex[i].x = vertexx +param.x;
			m_pEllipsePicVertex[i].y = vertexy +param.y;
			m_pEllipsePicVertex[i].z = Z;
			m_pEllipsePicVertex[i].rhw = 1.0f;
			m_pEllipsePicVertex[i].c = param.color.data;

		//	float du = (m_pEllipsePicVertex[i].x-m_pEllipsePicVertex[0].x)/(float)nPicWidth;
		//	float dv = (m_pEllipsePicVertex[i].y-m_pEllipsePicVertex[0].y)/(float)nPicHeight;

			float du = param.ru * XPCOS(i*nAngle);
			float dv = param.rv * XPSIN(i*nAngle);
			m_pEllipsePicVertex[i].u = param.u0 +du;
			m_pEllipsePicVertex[i].v = param.v0 +dv;
		}

		m_ifDevice->SetFVF(TCT1VertexFVF);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_MODULATE);

		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		//	m_ifDevice->SetRenderState( D3DRS_FILLMODE, D3DFILL_SOLID );

		m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, cnt, m_pEllipsePicVertex, sizeof(TCT1VERTEX));

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		m_stateMan.SelEffect(oldEffect);

		m_ifDevice->SetSamplerState(0, D3DSAMP_ADDRESSU, D3DTADDRESS_CLAMP);
		m_ifDevice->SetSamplerState(0, D3DSAMP_ADDRESSV, D3DTADDRESS_CLAMP);

		return true;
	}

	bool DX9Renderer::DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, XPCOLOR color)
	{
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawCircleCountPerFrame++;
#endif

		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;
		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1)
		{//要求纹理必须是2的幂
			return false;
		}
		int nPicWidth = it->second.m_info.m_nPicWidth;		// 原始图片宽度
		int nPicHeight = it->second.m_info.m_nPicHeight;    // 原始图片高度
		CTextureInfo texInfo = it->second.m_CTextureInfoVector[0];
		if( FAILED( m_ifDevice->SetTexture(0, texInfo.m_D3dTexture) ))
			return false;

		int cnt = 30;		
		m_pEllipsePicVertex[0].x = x;
		m_pEllipsePicVertex[0].y = y;
		m_pEllipsePicVertex[0].u = u0; //x/(float)nPicWidth
		m_pEllipsePicVertex[0].v = v0; //y/(float)nPicHeight
		m_pEllipsePicVertex[0].z = Z;
		m_pEllipsePicVertex[0].rhw = 1.0f;
		m_pEllipsePicVertex[0].c = color.data;

		int nAngle = 360/cnt;
		for(int i=1; i<cnt+2; i++)
		{
			float vertexx = rx*XPCOS(i*nAngle);
			float vertexy = ry*XPSIN(i*nAngle);

			m_pEllipsePicVertex[i].x = vertexx +x;
			m_pEllipsePicVertex[i].y = vertexy +y;
			m_pEllipsePicVertex[i].z = Z;
			m_pEllipsePicVertex[i].rhw = 1.0f;
			m_pEllipsePicVertex[i].c = color.data;

			float du = (m_pEllipsePicVertex[i].x-m_pEllipsePicVertex[0].x)/(float)nPicWidth;
			float dv = (m_pEllipsePicVertex[i].y-m_pEllipsePicVertex[0].y)/(float)nPicHeight;
			m_pEllipsePicVertex[i].u = u0 +du;
			m_pEllipsePicVertex[i].v = v0 +dv;
		}

		m_ifDevice->SetFVF(TCT1VertexFVF);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_MODULATE);

		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

	//	m_ifDevice->SetRenderState( D3DRS_FILLMODE, D3DFILL_SOLID );

		m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, cnt, m_pEllipsePicVertex, sizeof(TCT1VERTEX));

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		m_stateMan.SelEffect(oldEffect);
		return true;
	}

	bool DX9Renderer::DrawTriangles(const std::vector<TRIANGLE> &triangles, XPCOLOR color, XPFILLMODE fillmode)
	{
		if(triangles.size() < 1)
			return false;

		TCVERTEX* vertex = new TCVERTEX[3* triangles.size()];
		std::vector<TRIANGLE>::const_iterator it = triangles.begin(), ie = triangles.end();
		int i;
		for (i = 0; it != ie; ++it, ++i)
		{
			vertex[3*i].x = it->pt0.x;
			vertex[3*i].y = it->pt0.y;
			vertex[3*i].z = Z;
			vertex[3*i].rhw = 1.0f;
			vertex[3*i].c = color.data;

			vertex[3*i+1].x = it->pt1.x;
			vertex[3*i+1].y = it->pt1.y;
			vertex[3*i+1].z = Z;
			vertex[3*i+1].rhw = 1.0f;
			vertex[3*i+1].c = color.data;

			vertex[3*i+2].x = it->pt2.x;
			vertex[3*i+2].y = it->pt2.y;
			vertex[3*i+2].z = Z;
			vertex[3*i+2].rhw = 1.0f;
			vertex[3*i+2].c = color.data;
		}

		m_ifDevice->SetFVF(TCVertexFVF);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		m_ifDevice->SetRenderState(D3DRS_CULLMODE,D3DCULL_NONE);
		m_ifDevice->SetRenderState( D3DRS_FILLMODE, D3DFILL_WIREFRAME );
		m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLELIST, triangles.size(), vertex, sizeof(TCVERTEX));
		m_ifDevice->SetRenderState( D3DRS_FILLMODE, D3DFILL_SOLID );

	//	if(fillmode == XPFM_WIREFRAME)
	//		m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLELIST, triangles.size(), vertex, sizeof(TCVERTEX));
	//	else
	//		m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, pts.size() - 2, vertex, sizeof(TCVERTEX));

		delete[] vertex;
		m_stateMan.SelEffect(oldEffect);
		return true;

	}

	bool DX9Renderer::DrawPolygon(const std::vector<CPOINT> &pts, XPCOLOR color, XPFILLMODE fillmode)
	{
		if (pts.size() < 2)
			return false;
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPolygonCountPerFrame++;
#endif
		TCVERTEX* vertex = new TCVERTEX[pts.size() + 1];
		std::vector<CPOINT>::const_iterator it = pts.begin(), ie = pts.end();
		int i;
		for (i = 0; it != ie; ++it, ++i)
		{
			vertex[i].x = static_cast<float>(it->x);
			vertex[i].y = static_cast<float>(it->y);
			vertex[i].z = Z;
			vertex[i].rhw = 1.0f;
			vertex[i].c = color.data;
		}
		vertex[i] = vertex[0];
		m_ifDevice->SetFVF(TCVertexFVF);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		if(fillmode == XPFM_WIREFRAME)
			m_ifDevice->DrawPrimitiveUP(D3DPT_LINESTRIP, pts.size(), vertex, sizeof(TCVERTEX));
		else
			m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, pts.size() - 2, vertex, sizeof(TCVERTEX));

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		delete[] vertex;
		m_stateMan.SelEffect(oldEffect);
		return true;
	}

	bool DX9Renderer::DrawTriangleList(const TCVERTEX* pVertexs, int nVertexNum/*顶点个数*/)
	{
		if( (nVertexNum < 3) || (nVertexNum%3 != 0) )
			return false;
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPolygonCountPerFrame++;
#endif

		HRESULT hr;

		m_ifDevice->SetFVF(TCVertexFVF);
		m_ifDevice->SetStreamSource(0, m_TCVERTEXVB, 0, sizeof(TCVERTEX));
		m_ifDevice->SetIndices(m_TCVERTEXIB);

	//	SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
	//	SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		TCVERTEX* pVertexBuffer = NULL;
		int nBatchSize = 300; //每次拷贝的顶点数 //一定要是3的倍数
		int nTempVertexNum = nVertexNum;

		while(nTempVertexNum > 0)
		{
			int nsize = nTempVertexNum-nBatchSize >0 ? nBatchSize:nTempVertexNum;

			hr = m_TCVERTEXVB->Lock(m_iBatchTCVERTEXVBOffset *sizeof(TCVERTEX), nsize *sizeof(TCVERTEX), (void**)&pVertexBuffer, 
				m_iBatchTCVERTEXVBOffset==0 ? D3DLOCK_DISCARD : D3DLOCK_NOOVERWRITE);
			if( FAILED(hr) )
			{
				XPLOG_INFO(L"Lock Failed--DrawTriangleList() 部分参数: AvailableTextureMem (%d) MB\n",
					GetAvailableTextureMem());
				return false;
			}

			memcpy(pVertexBuffer, &pVertexs[m_iBatchTCVERTEXVBOffset], nsize*sizeof(TCVERTEX));
			m_TCVERTEXVB->Unlock();

			XPV(m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, m_iBatchTCVERTEXVBOffset, 0, nsize, 0, nsize/3));

			m_iBatchTCVERTEXVBOffset += nsize;
			m_iBatchTCVERTEXVBOffset = m_iBatchTCVERTEXVBOffset >= nVertexNum ? 0:m_iBatchTCVERTEXVBOffset;
			nTempVertexNum -= nsize;

#ifdef XP_PERFORMANCE
			m_performance.m_iDrawTriangleCountPerFrame++;
#endif
		}

		m_stateMan.SelEffect(oldEffect);
		return true;
	}

	//fTheta 单位: 角度 向右为0
	bool DX9Renderer::DrawFan(float x, float y, float r, float fTheta, XPCOLOR color, XPFILLMODE fillmode, const XPMATRIX3 *pMatrix)
	{
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawCircleCountPerFrame++;
#endif

		float fSpin = fTheta * 0.01745f; //角度转弧度PI/180.0f = 0.01745

		int cnt=0;
		cnt = static_cast<int>(r * fSpin / 5);//(弧长除以5)段
		(cnt&1)?(++cnt):cnt;	//确保被划分成了偶数段	

		XPVECTOR2 pt, pt1;
		TCVERTEX* vertex = new TCVERTEX[cnt+3]; //加2个圆心和第一个边界点
		pt.x = x;
		pt.y = y;
		if (pMatrix)
		{
			XPVec3TransformCoord(&pt1, &pt, pMatrix);
			pt = pt1;
		}
		vertex[0].x = pt.x;
		vertex[0].y = pt.y;
		vertex[0].z = Z;
		vertex[0].rhw = 1.0f;
		vertex[0].c = color.data;

		vertex[cnt+2] = vertex[0];
		
		float fangle = fSpin/cnt;	//每段的弧度
		float cosTheta, sinTheta;
		
		for(int i=1, j=-cnt/2 ; i<cnt+2; ++i,++j)
		{
			XPSinCos(j*fangle, &sinTheta, &cosTheta);
			pt.x = r * cosTheta + x;
			pt.y = r * sinTheta + y;
			if (pMatrix)
			{
				XPVec3TransformCoord(&pt1, &pt, pMatrix);
				pt = pt1;
			}
			vertex[i].x = pt.x;
			vertex[i].y = pt.y;
			vertex[i].z = Z;
			vertex[i].rhw = 1.0f;
			vertex[i].c = color.data;
		}

		m_ifDevice->SetFVF(TCVertexFVF);
		//m_stateMan.SetState(StateManager::RS_DIFFUSEBLEND);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		if(fillmode == XPFM_WIREFRAME)
			m_ifDevice->DrawPrimitiveUP(D3DPT_LINESTRIP, cnt+2, vertex, sizeof(TCVERTEX));
		else
			m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, cnt, vertex, sizeof(TCVERTEX));

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		delete[] vertex;
		m_stateMan.SelEffect(oldEffect);
		return true;
	}

	bool DX9Renderer::DrawEllipseFan(float x, float y, float rx, float ry, XPCOLOR color, XPFILLMODE fillmode, float fStartAngle, float fEndAngle, const XPMATRIX3 *pMatrix)
	{
		//单位: 角度
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawCircleCountPerFrame++;
#endif
		while (fStartAngle >= 360) {fStartAngle -= 360;}
		while (fStartAngle < 0) {fStartAngle += 360;}
		while (fEndAngle >= 360) {fEndAngle -= 360;}
		while (fEndAngle < 0) {fEndAngle += 360;}
		float fThe = fEndAngle - fStartAngle;	//圆心角角度
		while (fThe < 0) {fThe += 360;}

		float fStart = TORADIANS(fStartAngle); //角度转弧度
		float fEnd = TORADIANS(fEndAngle); //角度转弧度
		fThe = TORADIANS(fThe);

		int cnt=0;
		float midR = (fabs(rx) + fabs(ry)) / 2;
		cnt = static_cast<int>(midR * fThe / 5);//(近似弧长除以5)段
		//(cnt&1)?(++cnt):cnt;	//确保被划分成了偶数段	
		
		XPVECTOR2 pt, pt1;
		TCVERTEX* vertex = new TCVERTEX[cnt+3]; //加2个圆心和第一个边界点
		pt.x = x;
		pt.y = y;
		if (pMatrix)
		{
			XPVec3TransformCoord(&pt1, &pt, pMatrix);
			pt = pt1;
		}
		vertex[0].x = pt.x;
		vertex[0].y = pt.y;
		vertex[0].z = Z;
		vertex[0].rhw = 1.0f;
		vertex[0].c = color.data;

		vertex[cnt+2] = vertex[0];	//圆心

		float fangle = fThe/cnt;	//每段的弧度
		float cosTheta, sinTheta;

		for(int i=1; i<cnt+2; ++i)
		{
			XPSinCos((i-1)*fangle+fStart, &sinTheta, &cosTheta);
			pt.x = rx * cosTheta + x;
			pt.y = ry * sinTheta + y;
			if (pMatrix)
			{
				XPVec3TransformCoord(&pt1, &pt, pMatrix);
				pt = pt1;
			}
			vertex[i].x = pt.x;
			vertex[i].y = pt.y;
			vertex[i].z = Z;
			vertex[i].rhw = 1.0f;
			vertex[i].c = color.data;
		}

		m_ifDevice->SetFVF(TCVertexFVF);
		//m_stateMan.SetState(StateManager::RS_DIFFUSEBLEND);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		if(fillmode == XPFM_WIREFRAME)
			m_ifDevice->DrawPrimitiveUP(D3DPT_LINESTRIP, cnt+2, vertex, sizeof(TCVERTEX));
		else
			m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, cnt, vertex, sizeof(TCVERTEX));

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		delete[] vertex;
		m_stateMan.SelEffect(oldEffect);
		return true;


	}

	bool DX9Renderer::DrawEllipse(float x, float y, float rx, float ry, XPCOLOR color, XPFILLMODE fillmode, float fAngle)
	{
		//fAngle 单位: 角度
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawCircleCountPerFrame++;
#endif

		float fSpin = fAngle *0.01745f; //角度转弧度PI/180.0f = 0.01745

		int cnt=0;
		float maxR = max(fabs(rx), fabs(ry));
		if(maxR>100)
			cnt = 60;
		else if(maxR<10)
			cnt = 10;
		else
			cnt = 30;

		TCVERTEX* vertex = new TCVERTEX[cnt+2]; //加个圆心和第一个边界点
		vertex[0].x = x;
		vertex[0].y = y;
		vertex[0].z = Z;
		vertex[0].rhw = 1.0f;
		vertex[0].c = color.data;

		float fangle = XP_PI * 2/cnt;
		for(int i=1; i<cnt+2; i++)
		{
			float fx = rx*cos(i*fangle);
			float fy = ry*sin(i*fangle);

			float vertexx = fx*cos(fSpin) - fy*sin(fSpin);
			float vertexy = fx*sin(fSpin) + fy*cos(fSpin);

			vertex[i].x = vertexx +x;
			vertex[i].y = vertexy +y;
			vertex[i].z = Z;
			vertex[i].rhw = 1.0f;
			vertex[i].c = color.data;
		}

		m_ifDevice->SetFVF(TCVertexFVF);
		//m_stateMan.SetState(StateManager::RS_DIFFUSEBLEND);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		if(fillmode == XPFM_WIREFRAME)
			m_ifDevice->DrawPrimitiveUP(D3DPT_LINESTRIP, cnt, vertex+1, sizeof(TCVERTEX));
		else
			m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, cnt, vertex, sizeof(TCVERTEX));

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		delete[] vertex;
		m_stateMan.SelEffect(oldEffect);
		return true;
	}

	// 画框
	bool DX9Renderer::DrawBox(const FRECT &rect, XPCOLOR color, XPFILLMODE fillmode, const XPMATRIX3 *pMatrix)
	{
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawBoxCountPerFrame++;
#endif
		XPVECTOR2 vecOut;
		XPVECTOR2 thePos[4];
		//左上
		thePos[0].x = rect.left;
		thePos[0].y = rect.top;
		//右上
		thePos[1].x = rect.right;//thePos[0].x + rect.Width();
		thePos[1].y = thePos[0].y;
		//右下
		thePos[2].x = thePos[1].x;
		thePos[2].y = rect.bottom;//thePos[0].y + rect.Height();
		//左下
		thePos[3].x = thePos[0].x;
		thePos[3].y = thePos[2].y;
		if (pMatrix)
		{
			//m_ifDevice->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR );
			for (int i=0;i<4;++i)
			{
				XPVec3TransformCoord(&vecOut, thePos+i, pMatrix);
				thePos[i] = vecOut;
			}
			//XPTRACE(L"%f, %f, %f, %f\n", posx1, posy1, posx2, posy2);
		}
		TCVERTEX vertex[] = {
			TCVERTEX(thePos[0].x, thePos[0].y, Z, 1.0f, color.data),
			TCVERTEX(thePos[1].x, thePos[1].y, Z, 1.0f, color.data),
			TCVERTEX(thePos[2].x, thePos[2].y, Z, 1.0f, color.data),
			TCVERTEX(thePos[3].x, thePos[3].y, Z, 1.0f, color.data),
			TCVERTEX(thePos[0].x, thePos[0].y, Z, 1.0f, color.data)
		};
		m_ifDevice->SetFVF( TCVertexFVF);
		//m_stateMan.SetState(StateManager::RS_DIFFUSEBLEND);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		SetSeparateAlphaBlend(false);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		if( fillmode == XPFM_SOLIDFILL )// 填充
		{
			HRESULT hr = m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, 2, vertex, sizeof(TCVERTEX));
#ifdef XP_PERFORMANCE
	m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		}
		else if( fillmode == XPFM_WIREFRAME )// 线框
		{
			m_ifDevice->DrawPrimitiveUP(D3DPT_LINESTRIP, 4, vertex, sizeof(TCVERTEX));
#ifdef XP_PERFORMANCE
	m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		}
		else
		{
			m_stateMan.SelEffect(oldEffect);
			return false;
		}
		//
		m_stateMan.SelEffect(oldEffect);
		return true;
	}

	bool DX9Renderer::DrawBox(const FRECT *rects, int n, XPCOLOR color, XPFILLMODE fillmode, const XPMATRIX3 *pMatrix)
	{
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawBoxCountPerFrame += n;
#endif

		TCVERTEX *vertex = (TCVERTEX*)XAlloc(sizeof(TCVERTEX)*4*n);

		if( vertex == NULL ) return false;
		else
		{
			XPVECTOR2 vecOut;
			XPVECTOR2 thePos[4];
			
			for(int i=0; i<n; ++i)
			{
				//左上
				thePos[0].x = rects[i].left;
				thePos[0].y = rects[i].top;
				//右上
				thePos[1].x = rects[i].right;//thePos[0].x + rect.Width();
				thePos[1].y = thePos[0].y;
				//右下
				thePos[2].x = thePos[1].x;
				thePos[2].y = rects[i].bottom;//thePos[0].y + rect.Height();
				//左下
				thePos[3].x = thePos[0].x;
				thePos[3].y = thePos[2].y;
				if (pMatrix)
				{
					//m_ifDevice->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR );
					for (int i=0;i<4;++i)
					{
						XPVec3TransformCoord(&vecOut, thePos+i, pMatrix);
						thePos[i] = vecOut;
					}
					//XPTRACE(L"%f, %f, %f, %f\n", posx1, posy1, posx2, posy2);
				}
				for (int j=0; j<4; ++j)
				{
					vertex[4*i+j].x = thePos[j].x;
					vertex[4*i+j].y = thePos[j].y;
					vertex[4*i+j].z = Z;
					vertex[4*i+j].rhw = 1.0f;
					vertex[4*i+j].c = color.data;
				}
			}
		}

		m_ifDevice->SetFVF( TCVertexFVF);
		//m_stateMan.SetState(StateManager::RS_DIFFUSEBLEND);
		SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
		SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
		SetSeparateAlphaBlend(false);
		SetTextureColorBlendType(XPTOP_SELECTARG2);
		m_ifDevice->SetTexture(0, NULL);
		XPRENDER_EFFECT oldEffect = m_stateMan.GetEffect();
		m_stateMan.SelEffect(XPRE_NULL);

		HRESULT hr;
		if( fillmode == XPFM_SOLIDFILL )// 填充
		{
			WORD *indexData = (WORD*)XAlloc(6*n*sizeof(WORD));
			if( indexData )
			{
				for(int i=0; i<n; ++i)
				{
					indexData[6*i] = i*4;
					indexData[6*i+1] = i*4+1;
					indexData[6*i+2] = i*4+2;
					indexData[6*i+3] = i*4+2;
					indexData[6*i+4] = i*4+3;
					indexData[6*i+5] = i*4;

				}
				hr = m_ifDevice->DrawIndexedPrimitiveUP(D3DPT_TRIANGLELIST,0,n*4,2*n,indexData,D3DFMT_INDEX16,vertex, sizeof(TCVERTEX));
#ifdef XP_PERFORMANCE
				m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
				XFree(indexData);
			}

		}
		else if( fillmode == XPFM_WIREFRAME )// 线框
		{
			//
			WORD *indexData = (WORD*)XAlloc(8*n*sizeof(WORD));
			if( indexData )
			{
				for(int i=0; i<n; ++i)
				{
					indexData[8*i] = i*4;
					indexData[8*i+1] = i*4+1;
					indexData[8*i+2] = i*4+1;
					indexData[8*i+3] = i*4+2;
					indexData[8*i+4] = i*4+2;
					indexData[8*i+5] = i*4+3;
					indexData[8*i+6] = i*4+3;
					indexData[8*i+7] = i*4;
				}
				hr = m_ifDevice->DrawIndexedPrimitiveUP(D3DPT_LINELIST,0,n*4,4*n,indexData,D3DFMT_INDEX16,vertex, sizeof(TCVERTEX));
#ifdef XP_PERFORMANCE
				m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
				XFree(indexData);
			}
		
		}

		XFree(vertex);
		m_stateMan.SelEffect(oldEffect);
		return hr == S_OK;

	}

	PictureHandle DX9Renderer::NewPicture(int width, int height, PictureInfo *pPicInfo, XPTEXTURE_FORMAT texfmt) // 目前可能是 8888 或 4444, 使用前请查询纹理格式
	{
		if( texfmt != XPTEXFMT_DEFAULT )
		{
			if( !IsTexfmtSupport(texfmt) )
				texfmt = XPTEXFMT_DEFAULT;
		}

		if( texfmt == XPTEXFMT_DEFAULT ) // 根据data本身和当前颜色模式决定
		{
			texfmt = IsMode16() ? XPTEXFMT_A4R4G4B4 : XPTEXFMT_A8R8G8B8;
		}

		CTextureInfo TextureInfo;
		CPicInfo PicInfo;

		if(!m_bTextureCaps_pow2) //
		{	//支持非2幂
			TextureInfo.rect.left = 0;
			TextureInfo.rect.top = 0;
			TextureInfo.rect.right = width;
			TextureInfo.rect.bottom = height;
			if( 0 != GenTexture(NULL, texfmt, XPPOOL_MANAGED, TextureInfo) ) 
				return INVALID_PICTURE_HANDLE;

			PicInfo.m_info.m_nPicWidth = width;
			PicInfo.m_info.m_nPicHeight = height;
			PicInfo.m_info.m_pooltype = XPPOOL_MANAGED;
			PicInfo.m_info.m_texfmt = texfmt;

#ifdef XP_PERFORMANCE
			m_performance.m_iManagedTextureCount++;
			m_performance.m_iManagedTextureMemory += TextureInfo.mem;
#endif
			if( pPicInfo )
				*pPicInfo = PicInfo.m_info;

			PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
			m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));

		}else{
			if( texfmt != XPTEXFMT_A8R8G8B8 && texfmt != XPTEXFMT_A4R4G4B4 ) return INVALID_PICTURE_HANDLE;
			RECT srcRect;
			srcRect.left = srcRect.top = 0;
			srcRect.right = width;
			srcRect.bottom = height;
			std::vector<CRECT> dstRects;
			PicInfo.m_info.m_nPicHeight = height;
			PicInfo.m_info.m_nPicWidth = width;
			PicInfo.m_info.m_pooltype = XPPOOL_MANAGED;
			PicInfo.m_info.m_texfmt = texfmt;
			if( pPicInfo )
				*pPicInfo = PicInfo.m_info;
			PartitionRectToPower2(srcRect, dstRects);
			for(int i=0; i<(int)dstRects.size();++i)
			{
				CTextureInfo TextureInfo;
				TextureInfo.rect = dstRects[i];

				if( 0 != GenTexture(NULL, texfmt, XPPOOL_MANAGED, TextureInfo) )
					return INVALID_PICTURE_HANDLE;

				PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
#ifdef XP_PERFORMANCE
				m_performance.m_iManagedTextureCount++;
				m_performance.m_iManagedTextureMemory += TextureInfo.mem;
#endif
			}

			m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));
	
		}

		return m_picHandleSeed++;
	}

	// 目前可能是 8888 或 4444 或 DXT1-5, 使用前请查询纹理格式, 4444使用16位buffer,其他使用32位buffer, DXT1-5格式的话pRect的大小最好对齐到4
	bool DX9Renderer::GetPictureData(PictureHandle handle, void *data, int size, const CRECT* pRect)
	{
		if(m_ifDevice==NULL || data==NULL) return false;
		
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() ) return false;

		CRECT rect(0,0,it->second.m_info.m_nPicWidth,it->second.m_info.m_nPicHeight);
		if( pRect )
		{
			if( pRect->left > pRect->right || pRect->top > pRect->bottom
				|| pRect->left < 0  || pRect->top < 0
				|| pRect->right > rect.right || pRect->bottom > rect.bottom 
				)
				return false;
			rect = *pRect;
		}

		//todo 压缩纹理的读写，考虑以后自己写
		if( it->second.m_info.m_texfmt != XPTEXFMT_A4R4G4B4 && it->second.m_info.m_texfmt != XPTEXFMT_A8R8G8B8 ) 
		{
			HRESULT hr;
			// 此时创建纹理的逻辑保证了图片没有分割，只有一张
			if( size < 4 * rect.Width() * rect.Height() )
				return false;
			LPDIRECT3DTEXTURE9 ifTex = NULL;
			if( FAILED(D3DXCreateTexture(
				m_ifDevice,
				rect.Width(), rect.Height(),
				1, // 指定共生成几级mipmap 0 create a complete mipmap chain 1...
				0,
				D3DFMT_A8R8G8B8,
				D3DPOOL_SYSTEMMEM, &ifTex)))
				return false;

			IDirect3DSurface9 * pDestSurface = NULL, *pSrcSurface = NULL;
			XPV(ifTex->GetSurfaceLevel(0,&pDestSurface));
			XPV(it->second.m_CTextureInfoVector[0].m_D3dTexture->GetSurfaceLevel(0,&pSrcSurface));
			bool r = false;
			if( SUCCEEDED(D3DXLoadSurfaceFromSurface(pDestSurface, NULL, NULL, pSrcSurface, NULL, &rect, D3DX_DEFAULT, 0)))
			{
				//XPTRACE(L"LoadSurface Ok\n");
				D3DLOCKED_RECT lockedRect;
				XPV(ifTex->LockRect(0, &lockedRect, NULL, D3DLOCK_READONLY));			

				for(int i=0; i<rect.Height(); i++)
				{
					DWORD *desData = (DWORD*)data + i*rect.Width();
					DWORD *imageData = (DWORD*)((BYTE*)lockedRect.pBits + i*lockedRect.Pitch);
					memcpy(desData, imageData, 4 * rect.Width());
				}
				XPV(ifTex->UnlockRect(0));
				r = true;
			}
			XPSAFE_RELEASE(pDestSurface);
			XPSAFE_RELEASE(pSrcSurface);
			XPSAFE_RELEASE(ifTex);
			return r;
		}

		bool tex16 = it->second.m_info.m_texfmt == XPTEXFMT_A4R4G4B4;
		
		if( size < ( tex16 ? 2 : 4) * rect.Width() * rect.Height() )
			return false;
		

		int n = (int)(it->second.m_CTextureInfoVector.size());

		for(int i=0; i<n; i++)
		{
			CTextureInfo texInfo = it->second.m_CTextureInfoVector[i];
			D3DSURFACE_DESC textureDesc; 
			texInfo.m_D3dTexture->GetLevelDesc(0, &textureDesc);
			if( textureDesc.Format != (D3DFORMAT)it->second.m_info.m_texfmt )
				return false;

			D3DLOCKED_RECT lockedRect;
			CRECT res;
			if( rect.IsCross(texInfo.rect, res))
			{
				RECT srcrect;
				srcrect.left = res.left - texInfo.rect.left;
				srcrect.top = res.top - texInfo.rect.top;
				srcrect.right = res.right - texInfo.rect.left;
				srcrect.bottom = res.bottom - texInfo.rect.top;
				texInfo.m_D3dTexture->LockRect(0, &lockedRect, &srcrect, D3DLOCK_READONLY);			
				if( tex16 )
				{
					for(int i=0; i<res.Height(); i++)
					{
						WORD *desData = (WORD*)data + (i+res.top-rect.top)*rect.Width() + res.left-rect.left;
						WORD *imageData = (WORD*)((BYTE*)lockedRect.pBits + i*lockedRect.Pitch);
						memcpy(desData, imageData, 2 * res.Width());
					}
				}
				else
				{
					for(int i=0; i<res.Height(); i++)
					{
						DWORD *desData = (DWORD*)data + (i+res.top-rect.top)*rect.Width() + res.left-rect.left;
						DWORD *imageData = (DWORD*)((BYTE*)lockedRect.pBits + i*lockedRect.Pitch);
						memcpy(desData, imageData, 4 * res.Width());
					}
				}
				texInfo.m_D3dTexture->UnlockRect(0);
			}
		}
		return true;
	}

	// 目前可能是 8888 或 4444 或 DXT1-5, 使用前请查询纹理格式, 4444使用16位buffer,其他使用32位buffer, DXT1-5格式的话pRect的大小最好对齐到4
	bool DX9Renderer::SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect)
	{
		if(m_ifDevice==NULL || data==NULL) return false;

		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() ) return false;

		CRECT rect(0,0,it->second.m_info.m_nPicWidth,it->second.m_info.m_nPicHeight);
		if( pRect )
		{
			if( pRect->left > pRect->right || pRect->top > pRect->bottom
				|| pRect->left < 0  || pRect->top < 0
				|| pRect->right > rect.right || pRect->bottom > rect.bottom 
				)
				return false;
			rect = *pRect;
		}

		//todo 压缩纹理的读写，考虑以后自己写
		if( it->second.m_info.m_texfmt != XPTEXFMT_A4R4G4B4 && it->second.m_info.m_texfmt != XPTEXFMT_A8R8G8B8 ) 
		{
			HRESULT hr;
			// 此时创建纹理的逻辑保证了图片没有分割，只有一张
			if( size < 4 * rect.Width() * rect.Height() )
				return false;
			LPDIRECT3DTEXTURE9 ifTex = NULL;
			if( FAILED(D3DXCreateTexture(
				m_ifDevice,
				rect.Width(), rect.Height(),
				1, // 指定共生成几级mipmap 0 create a complete mipmap chain 1...
				0,
				D3DFMT_A8R8G8B8,
				D3DPOOL_SYSTEMMEM, &ifTex)))
				return false;

			D3DLOCKED_RECT lockedRect;
			XPV(ifTex->LockRect(0, &lockedRect, NULL, 0));			

			for(int i=0; i<rect.Height(); i++)
			{
				DWORD *imageData = (DWORD*)data + i*rect.Width();
				DWORD *desData = (DWORD*)((BYTE*)lockedRect.pBits + i*lockedRect.Pitch);
				memcpy(desData, imageData, 4 * rect.Width());
			}
			XPV(ifTex->UnlockRect(0));

			IDirect3DSurface9 * pDestSurface = NULL, *pSrcSurface = NULL;
			XPV(ifTex->GetSurfaceLevel(0,&pSrcSurface));
			XPV(it->second.m_CTextureInfoVector[0].m_D3dTexture->GetSurfaceLevel(0,&pDestSurface));
			bool r = false;
			if( SUCCEEDED(D3DXLoadSurfaceFromSurface(pDestSurface, NULL, &rect, pSrcSurface, NULL, NULL, D3DX_DEFAULT, 0)))
			{
				//XPTRACE(L"LoadSurface Ok\n");
				
				r = true;
			}
			XPSAFE_RELEASE(pDestSurface);
			XPSAFE_RELEASE(pSrcSurface);
			XPSAFE_RELEASE(ifTex);
			return r;
		}

		bool tex16 = it->second.m_info.m_texfmt == XPTEXFMT_A4R4G4B4;
		
		if( size < ( tex16 ? 2 : 4) * rect.Width() * rect.Height() )
			return false;

		int n = (int)(it->second.m_CTextureInfoVector.size());

		for(int i=0; i<n; i++)
		{
			CTextureInfo texInfo = it->second.m_CTextureInfoVector[i];
			D3DSURFACE_DESC textureDesc; 
			texInfo.m_D3dTexture->GetLevelDesc(0, &textureDesc);
			if( textureDesc.Format != (D3DFORMAT)it->second.m_info.m_texfmt )
				return false;

			D3DLOCKED_RECT lockedRect;
			CRECT res;
			if( rect.IsCross(texInfo.rect, res))
			{
				RECT srcrect;
				srcrect.left = res.left - texInfo.rect.left;
				srcrect.top = res.top - texInfo.rect.top;
				srcrect.right = res.right - texInfo.rect.left;
				srcrect.bottom = res.bottom - texInfo.rect.top;
				texInfo.m_D3dTexture->LockRect(0, &lockedRect, &srcrect, 0);		
				if( tex16 )
				{
					for(int i=0; i<res.Height(); i++)
					{
						WORD *desData = (WORD*)data + (i+res.top-rect.top)*rect.Width() + res.left-rect.left;
						WORD *imageData = (WORD*)((BYTE*)lockedRect.pBits + i*lockedRect.Pitch);
						memcpy(imageData, desData, 2 * res.Width());
					}
				}
				else
				{
					for(int i=0; i<res.Height(); i++)
					{
						DWORD *desData = (DWORD*)data + (i+res.top-rect.top)*rect.Width() + res.left-rect.left;
						DWORD *imageData = (DWORD*)((BYTE*)lockedRect.pBits + i*lockedRect.Pitch);
						memcpy(imageData, desData, 4 * res.Width());
					}
				}
				texInfo.m_D3dTexture->UnlockRect(0);
			}
		}
		return true;
	}

	//	装载图片
	PictureHandle DX9Renderer::LoadPicture(const std::wstring& filename) // return pichandle
	{
		XBuffer os;
		if( m_pFileIOMan->GetFileImage(filename, os))
		{
			return LoadPictureFromMem(os.constbegin(), (int)os.size());
		}
		return INVALID_PICTURE_HANDLE;
	}

	struct DDS_PIXELFORMAT {
		DWORD dwSize;
		DWORD dwFlags;
		DWORD dwFourCC;
		DWORD dwRGBBitCount;
		DWORD dwRBitMask;
		DWORD dwGBitMask;
		DWORD dwBBitMask;
		DWORD dwABitMask;
	};

	typedef struct {
		DWORD           dwSize;
		DWORD           dwFlags;
		DWORD           dwHeight;
		DWORD           dwWidth;
		DWORD           dwPitchOrLinearSize;
		DWORD           dwDepth;
		DWORD           dwMipMapCount;
		DWORD           dwReserved1[11];
		DDS_PIXELFORMAT ddspf;
		DWORD           dwCaps;
		DWORD           dwCaps2;
		DWORD           dwCaps3;
		DWORD           dwCaps4;
		DWORD           dwReserved2;
	} DDS_HEADER;


	bool DX9Renderer::GetCreateTextureFormatFromFileInMemory(const void *data, int size, XPIMAGE_INFO& info)
	{
		if (size < 2)
			return false;
		const unsigned short* pData = (const unsigned short*)data;
		switch (*pData)
		{
		case 0xD8FF://jpg
			info.ImageFileFormat = XPIFF_JPG;
			info.Format = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
			break;
		case 0x5089://png
			{
				if (size < 0x20)
					return false;
				info.ImageFileFormat = XPIFF_PNG;
				const unsigned char *pByte = (const unsigned char*)data;
				pByte += 0x18;
				info.Format = (IsMode16() || (*pByte <= 4))?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
			}
			break;
		case 0x4D42://bmp
			if (size < 0xF)
				return false;
			info.ImageFileFormat = XPIFF_BMP;
			pData += 0xE;
			info.Format = (IsMode16() || (*pData <= 16))?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
			break;
		case 0x4444://DDS
			{
				if (size < 0x62)
					return false;
				info.ImageFileFormat = XPIFF_DDS;
				const DDS_HEADER *pHeader = (const DDS_HEADER *)(pData + 2);
				if (pHeader->ddspf.dwFlags & DDPF_FOURCC)
				{
					info.Format = (XPTEXTURE_FORMAT)pHeader->ddspf.dwFourCC;
				} else if (pHeader->ddspf.dwFlags & DDPF_RGB)
				{
					if (pHeader->ddspf.dwRGBBitCount == 16)
					{
						info.Format = (pHeader->ddspf.dwABitMask == 0)?XPTEXFMT_R5G6B5:XPTEXFMT_A4R4G4B4;
					} else {
						info.Format = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
					}
				} else {
					info.Format = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
				}
			}
			break;
		case 0://tga
			info.ImageFileFormat = XPIFF_TGA;
			info.Format = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8; //todo;细分..
			break;
		case 0x4952://WEBP
			{
				if (size < 12)
					return false;
				else
				{
					// RIFFXXXXWEBP
					unsigned char* pHead = (unsigned char*)pData;
					if (   pHead[0] != 0x52		// R
						|| pHead[1] != 0x49		// I
						|| pHead[2] != 0x46		// F
						|| pHead[3] != 0x46		// F
						|| pHead[8] != 0x57		// W
						|| pHead[9] != 0x45		// E
						|| pHead[10] != 0x42	// B
						|| pHead[11] != 0x50)	// P
					{
						return false;
					}
					info.ImageFileFormat = XPIFF_WEBP;
					info.Format = XPTEXFMT_A8R8G8B8;
				}

			}
			break;
		default:
			return false;
		}
		return true;

	}

	PictureHandle DX9Renderer::CreateTextureFromWebPFileInMemory(const void *data, int size, PictureInfo *pPicInfo, XPTEXTURE_FORMAT texfmt)
	{
		int nWidth=0;
		int nHeight=0;
		uint8_t* pData = WebPDecodeBGRA((const uint8_t*)data,size,&nWidth,&nHeight);
		size_t imageDataSize = nWidth * nHeight * 4;
		PictureHandle handle = NewPicture(nWidth,nHeight,pPicInfo,texfmt);
		SetPictureData(handle,pData,imageDataSize);
		delete [] pData;
		return handle;
	}
	//	从内存装载图片
	PictureHandle DX9Renderer::LoadPictureFromMem(const void *data, int size, PictureInfo *pPicInfo, XPTEXTURE_FORMAT texfmt, XPPOOL_TYPE pooltype)
	{
		//		m_bTextureCaps_pow2 = true; //测试用..

		// 调试用
		{
			if( data == NULL || size <= 0 )
			{
				data = m_errTexBuf.constbegin();
				size = (int)m_errTexBuf.size();
			}
		}
		if( texfmt != XPTEXFMT_DEFAULT )
		{
			if( !IsTexfmtSupport(texfmt) )
				texfmt = XPTEXFMT_DEFAULT;
		}

		XPIMAGE_FILEFORMAT filefmt = XPIFF_FORCE_DWORD;
		if( texfmt == XPTEXFMT_DEFAULT ) // 根据data本身和当前颜色模式决定
		{
			XPIMAGE_INFO fileinfo;
			if(!GetCreateTextureFormatFromFileInMemory(data, size, fileinfo))
				return INVALID_PICTURE_HANDLE;
			
			texfmt = fileinfo.Format;
			filefmt = fileinfo.ImageFileFormat;
			if( fileinfo.ImageFileFormat == D3DXIFF_DDS ) // dds
			{
				if( m_bTextureCaps_pow2 || !IsTexfmtSupport((XPTEXTURE_FORMAT)fileinfo.Format) )
					texfmt = XPTEXFMT_A4R4G4B4; // 090327 whf, 直接使用16来节省存储？
			}
		}

		if( filefmt == XPIFF_WEBP )
		{
			PictureHandle pHandle = CreateTextureFromWebPFileInMemory(data, size, pPicInfo,texfmt);
			if( pHandle == NULL )	return INVALID_PICTURE_HANDLE;
			return pHandle;
		}
		
		CTextureInfo TextureInfo;
		CPicInfo PicInfo;
		bool bret = false;
		D3DXIMAGE_INFO pSrcInfo;

		HRESULT hr;
		int iImageWidth = 0;
		int iImageHeight = 0;

		if(!m_bTextureCaps_pow2)
		{
			//支持非2幂
			hr = D3DXCreateTextureFromFileInMemoryEx(
				m_ifDevice, data, size,
				D3DX_DEFAULT_NONPOW2, 
				D3DX_DEFAULT_NONPOW2, 
				1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
				0,
				(D3DFORMAT)texfmt,
				(D3DPOOL)pooltype, 
				D3DX_FILTER_NONE,
				D3DX_FILTER_NONE,
				0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);

//  			D3DLOCKED_RECT lockedRect;
//  			XPV(TextureInfo.m_D3dTexture->LockRect(0, &lockedRect, NULL, 0));
//  			int pitch = lockedRect.Pitch;
//  			XPV(TextureInfo.m_D3dTexture->UnlockRect(0));

			if( pooltype == XPPOOL_VIDEOMEM && hr == D3DERR_OUTOFVIDEOMEMORY )
			{
				pooltype = XPPOOL_MANAGED;
				
				XPLOG_INFO(L"pooltype == XPPOOL_VIDEOMEM && hr == D3DERR_OUTOFVIDEOMEMORY \n");

				hr = D3DXCreateTextureFromFileInMemoryEx(
				m_ifDevice, data, size,
				D3DX_DEFAULT_NONPOW2, 
				D3DX_DEFAULT_NONPOW2, 
				1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
				0,
				(D3DFORMAT)texfmt,
				(D3DPOOL)pooltype, 
				D3DX_FILTER_NONE,
				D3DX_FILTER_NONE,
				0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);
			}

			if( FAILED(hr) ) return INVALID_PICTURE_HANDLE;

			//XPLOG_INFO("level is %d\n", TextureInfo.m_D3dTexture->GetLevelCount());
			D3DSURFACE_DESC desc;
			TextureInfo.m_D3dTexture->GetLevelDesc(0, &desc);

			iImageWidth = pSrcInfo.Width;
			iImageHeight = pSrcInfo.Height;
			TextureInfo.rect.left = 0;
			TextureInfo.rect.top = 0;
			TextureInfo.rect.right = iImageWidth;
			TextureInfo.rect.bottom = iImageHeight;
#ifdef XP_PERFORMANCE
			switch( texfmt )
			{
			case XPTEXFMT_A4R4G4B4:
				TextureInfo.mem = 2 * desc.Width * desc.Height;
				break;
			case XPTEXFMT_A8R8G8B8:
				TextureInfo.mem = 4 * desc.Width * desc.Height;
				break;
			case XPTEXFMT_DXT2:
			case XPTEXFMT_DXT3:
			case XPTEXFMT_DXT4:
			case XPTEXFMT_DXT5:
				TextureInfo.mem = desc.Width * desc.Height;
				break;
			case XPTEXFMT_DXT1:
				TextureInfo.mem = desc.Width * desc.Height / 2;
				break;
			default:
				break;
			}
#endif
			PicInfo.m_info.m_nPicWidth = iImageWidth;
			PicInfo.m_info.m_nPicHeight = iImageHeight;
			PicInfo.m_info.m_pooltype = pooltype;
			PicInfo.m_info.m_texfmt = texfmt;

			if( pPicInfo )
			{
				*pPicInfo = PicInfo.m_info;
			}

			PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
			m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));
			
#ifdef XP_PERFORMANCE
	m_performance.m_iManagedTextureCount++;
	m_performance.m_iManagedTextureMemory += TextureInfo.mem;
#endif

		}else{
			//XPRINT_PICTURE(L"pow2, load picture, size is %d, pooltype = %d\n", size, pooltype);
			IDirect3DTexture9* pSrcTex = NULL;

			HRESULT hr = D3DXCreateTextureFromFileInMemoryEx(
				m_ifDevice, data, size,
				D3DX_DEFAULT,
				D3DX_DEFAULT,
				1,
				0,
				(D3DFORMAT)texfmt,
				D3DPOOL_SYSTEMMEM, 
				D3DX_FILTER_NONE, 
				D3DX_FILTER_NONE, 
				0, &pSrcInfo, NULL, &pSrcTex); 

			if(FAILED(hr)) return INVALID_PICTURE_HANDLE;
			//XPRINT_PICTURE(L"pow2, load picture, create system ok\n");

			D3DSURFACE_DESC textureDesc; 
			pSrcTex->GetLevelDesc(0, &textureDesc);
			if( textureDesc.Format != (D3DFORMAT)texfmt )
				return INVALID_PICTURE_HANDLE;

			iImageWidth = pSrcInfo.Width;
			iImageHeight = pSrcInfo.Height;			

			RECT srcRect;
			srcRect.left = srcRect.top = 0;
			srcRect.right = pSrcInfo.Width;
			srcRect.bottom = pSrcInfo.Height;
			std::vector<CRECT> dstRects;

			PartitionRectToPower2(srcRect, dstRects);

			bool outofvideomem = false;
			int genindex = -1;

			//XPRINT_PICTURE(L"pow2, before split texture, count is %d\n", (int)dstRects.size());

			for(int i=0; i<(int)dstRects.size();++i)
			{				
				CTextureInfo TextureInfo;
				TextureInfo.rect = dstRects[i];

				//XPRINT_PICTURE(L"\tpow2, before gen index =%d\n", i);
				int r = GenTexture(pSrcTex, texfmt, pooltype, TextureInfo);
				//XPRINT_PICTURE(L"\tpow2, index =%d, r = %d\n", i, r);
				if( r == 1 )
				{				
					XPLOG_INFO(L"error---outofvideomem \n");

					outofvideomem = true;
					genindex = i;
					break;
				}

				if( 0 !=  r)
				{
					//XPRINT_PICTURE(L"\tload picture, gen failed, index =%d, r = %d\n", i, r);
					return INVALID_PICTURE_HANDLE;
				}

				PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
#ifdef XP_PERFORMANCE
				m_performance.m_iManagedTextureCount++;
				m_performance.m_iManagedTextureMemory += TextureInfo.mem;
#endif
			}

			if( outofvideomem )
			{
				//
				//XPRINT_PICTURE(L"pow2, out of video memory\n");
				//
				for(int i=0; i< genindex; ++i)
				{
#ifdef XP_PERFORMANCE
					m_performance.m_iManagedTextureCount--;
					m_performance.m_iManagedTextureMemory -= PicInfo.m_CTextureInfoVector[i].mem;
#endif
					XPSAFE_RELEASE(PicInfo.m_CTextureInfoVector[i].m_D3dTexture);
				}
				PicInfo.m_CTextureInfoVector.clear();
				//
				pooltype = XPPOOL_MANAGED;
				for(int i=0; i<(int)dstRects.size();++i)
				{				
					CTextureInfo TextureInfo;
					TextureInfo.rect = dstRects[i];

					int r = GenTexture(pSrcTex, texfmt, pooltype, TextureInfo);
					if( 0 !=  r)
					{
						return INVALID_PICTURE_HANDLE;
					}

					PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
#ifdef XP_PERFORMANCE
					m_performance.m_iManagedTextureCount++;
					m_performance.m_iManagedTextureMemory += TextureInfo.mem;
#endif
				}
			}

			
			PicInfo.m_info.m_nPicHeight = pSrcInfo.Height;
			PicInfo.m_info.m_nPicWidth = pSrcInfo.Width;
			PicInfo.m_info.m_pooltype = pooltype;
			PicInfo.m_info.m_texfmt = texfmt;

			if( pPicInfo )
			{
				*pPicInfo = PicInfo.m_info;
			}
			m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));
	
			pSrcTex->Release();
		}
		return m_picHandleSeed++;
	}

	//	根据 pichandle 批量贴图
//bool DX9Renderer::DrawBatchPicture(PictureHandle handle, const FRECT* rects, int count, XPCOLOR color, XPTEXTURE_BLENDMODE blend, const FRECT &srcrect)
	bool DX9Renderer::DrawBatchPicture(const DrawBatchPictureParam &param)
	{
		if(param.handle == INVALID_PICTURE_HANDLE)
			return false;

		if(m_ifDevice==NULL || param.pRect == NULL || param.rectCount <= 0 || param.rectCount * 4 > BATCH_VB_COUNT )
			return false;
		//	StateSaver saver(m_ifDevice);
		PictureMap::iterator it = m_mapPictures.find(param.handle);
		PictureMap::iterator secIt = m_mapPictures.find(param.secHandle);
		if( it == m_mapPictures.end() )
			return false;
		//bool hasSecTex = (secIt != m_mapPictures.end());
		bool hasSecTex = (param.secHandle != INVALID_PICTURE_HANDLE);
		if (hasSecTex)	//第二张纹理只能是2的幂
		{
			XPASSERT(secIt->second.m_CTextureInfoVector.size() == 1);
		}
		int a = (int)(it->second.m_CTextureInfoVector.size()); 
		//因为不是2的幂而显卡又要求才切分的..
		//大于512的切分是成了新的磁盘文件..只有一个texture
		switch (param.blend)
		{
		case XPTEXBM_MODULATE:   // alpha混合
			SetTextureColorBlendType(XPTOP_DEFAULT);
			SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
			break;
		case XPTEXBM_ADDITIVE:	// 色彩饱和
			SetTextureColorBlendType(XPTOP_DEFAULT);
			SetAlphaBlendSrcType(XPBLEND_ONE);
			SetAlphaBlendDstType(XPBLEND_ONE);
			break;
		case XPTEXBM_COPY:
			SetTextureColorBlendType(XPTOP_DEFAULT);
			SetAlphaBlendSrcType(XPBLEND_ONE);
			SetAlphaBlendDstType(XPBLEND_ZERO);
			break;
		case XPTEXBM_ADDITIVE_ALPHA:
			SetTextureColorBlendType(XPTOP_DEFAULT);
			SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			SetAlphaBlendDstType(XPBLEND_ONE);
			break;
		case XPTEXBM_MODULATE2X:
			SetTextureColorBlendType(XPTOP_MODULATE2X);
			SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
			break;
		case XPTEXBM_ADDITIVE2X:
			SetTextureColorBlendType(XPTOP_MODULATE2X);
			SetAlphaBlendSrcType(XPBLEND_ONE);
			SetAlphaBlendDstType(XPBLEND_ONE);
			break;
		case XPTEXBM_MUTIPLY:
			SetTextureColorBlendType(XPTOP_DEFAULT);
			SetAlphaBlendSrcType(XPBLEND_DESTCOLOR);
			SetAlphaBlendDstType(XPBLEND_ZERO);
			break;
		case XPTEXBM_MUTIPLY_ALPHA:
			SetTextureColorBlendType(XPTOP_DEFAULT);
			SetAlphaBlendSrcType(XPBLEND_DESTCOLOR);
			SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
			break;
		}
		SetSeparateAlphaBlend(param.bSpecAlpha);
		m_stateMan.EnableSecondTex(hasSecTex);

		D3DCOLOR color[4];
		if (param.pColor == NULL)
		{
			color[0] = color[1] = color[2] = color[3] = 0xFFFFFFFF;
		} else {
			if (param.colorCount < 4)
			{
				color[0] = color[1] = color[2] = color[3] = param.pColor->data;
			} else {
				color[0] = param.pColor[0].data;
				color[1] = param.pColor[1].data;
				color[2] = param.pColor[2].data;
				color[3] = param.pColor[3].data;
			}
		}

		XPVECTOR2 vecOut;
		XPVECTOR2 thePos[4];
#ifdef DRAW_BATCH_PIC_SSE
		XPVECTOR2 thePos2[4];
#endif
		FRECT srcrect(.0f, .0f, 1.0f, 1.0f);
		for(int i=0; i<a; i++)
		{
			volatile int nPicWidth = it->second.m_info.m_nPicWidth;		// 原始图片宽度
			volatile int nPicHeight = it->second.m_info.m_nPicHeight;    // 原始图片高度
			CTextureInfo texInfo = it->second.m_CTextureInfoVector[i];

			volatile float texLeft = (float)texInfo.rect.left;	// 分片在原始图片中的位置
			volatile float texTop = (float)texInfo.rect.top;     // 分片在原始图片中的位置
			volatile float texW = (float)texInfo.rect.Width();   // 分片的原始宽度
			volatile float texH = (float)texInfo.rect.Height();  // 分片的原始高度 

			D3DSURFACE_DESC textureDesc; 
			texInfo.m_D3dTexture->GetLevelDesc(0, &textureDesc); //纹理的实际宽高可以在创建纹理的时候就缓存起来的..todo
			volatile float texRealW = (float)textureDesc.Width;	// 分片纹理的实际宽度
			volatile float texRealH = (float)textureDesc.Height; // 分片纹理的实际高度

			//绘制....

			//根据约束条件下取出来的纹理的大小再来确定画框的的大小...
			//7-24日
			////////////////////////////////////////
			float lx = texLeft/nPicWidth;				// 分片的纹理坐标
			float ty = texTop/nPicHeight;				//
			float rx = (texLeft+texW)/nPicWidth;		//
			float by = (texTop+texH)/nPicHeight;		//
			float rx2 = (texLeft + texRealW)/nPicWidth;
			float by2 = (texTop + texRealH)/nPicHeight;

			HRESULT hr;
			TCT1VERTEX *vertex = NULL;
			WORD *index = NULL;

			if( (m_iBatchVBOffset + param.rectCount*4) > BATCH_VB_COUNT-1 )
				m_iBatchVBOffset = 0;

			hr = m_batchVB->Lock(sizeof(TCT1VERTEX) * m_iBatchVBOffset, 4 * sizeof(TCT1VERTEX) * param.rectCount, (void**)&vertex,
					m_iBatchVBOffset == 0 ?D3DLOCK_DISCARD :D3DLOCK_NOOVERWRITE);

		//	hr = m_batchVB->Lock(0, 0, (void**)&vertex, D3DLOCK_DISCARD);

			if(FAILED(hr))
			{
				XPLOG_INFO(L"Lock Failed--DrawBatchPicture() 部分参数:m_iBatchVBOffset(%d), rectCount(%d), m_iBatchIBOffset(%d)\n",
					m_iBatchVBOffset, param.rectCount, m_iBatchIBOffset);

				XPLOG_INFO(L"Lock Failed--DrawBatchPicture() 部分参数: param.blend(%d), textureCnt(%d), hasSecTex(%d)\n",
					(int)(param.blend), a, (int)hasSecTex);
				
				XPLOG_INFO(L"Lock Failed--DrawBatchPicture() 部分参数: AvailableTextureMem (%d) MB\n",
							GetAvailableTextureMem());

				return false;
			}

			if( (m_iBatchIBOffset + param.rectCount*6) > BATCH_IB_COUNT-1 )
				m_iBatchIBOffset = 0;

			hr = m_batchIB->Lock(sizeof(WORD) * m_iBatchIBOffset, 6 * sizeof(WORD) * param.rectCount, (void**)&index, 
				m_iBatchIBOffset == 0 ?D3DLOCK_DISCARD :D3DLOCK_NOOVERWRITE);
			
		//	hr = m_batchIB->Lock(0, 0, (void**)&index, D3DLOCK_DISCARD);

			if( FAILED(hr) )
			{
				XPV(m_batchVB->Unlock());

				XPLOG_INFO(L"Lock Failed--DrawBatchPicture() 部分参数: AvailableTextureMem (%d) MB\n",
					GetAvailableTextureMem());
				
				return false;
			}

			bool bUVfind = false; //用来标记uv坐标是否已经计算好了，避免重复计算
			bool bSUVfind = false; //用来标记第二纹理的uv坐标是否已经计算好了，避免重复计算
			bool bUVflag = false; //用来标记当前纹理块与需要绘制的区域是否没有交集。true表示无交集。
			float u1, v1, u2, v2, du, dv;
			float su1=0.0f, sv1=0.0f, su2=0.0f, sv2=0.0f;
			for(int j=0; j<param.rectCount; ++j)			
			{
				if(param.bSameuv)
				{
					if(!bUVfind)
					{
						if(param.pSrcrect)
						{
							srcrect = *param.pSrcrect;
						}
						// 确定分片与 srcrect的交集
						u1 = max(srcrect.left, lx);
						u2 = min(srcrect.right, rx);
						v1 = max(srcrect.top, ty);
						v2 = min(srcrect.bottom, by);
						if( (u1>u2)||(v1>v2) )
						{
							bUVflag = true;
							break;	//没有交集...
						}
						du = u2-u1;
						dv = v2-v1;

						bUVfind = true;
					}
				}
				else
				{
					srcrect = param.pSrcrect[j];

					// 确定分片与 srcrect的交集
					u1 = max(srcrect.left, lx);
					u2 = min(srcrect.right, rx);
					v1 = max(srcrect.top, ty);
					v2 = min(srcrect.bottom, by);
					if( (u1>u2)||(v1>v2) )
						continue;	//没有交集...

					du = u2-u1;
					dv = v2-v1;
				}
				if (hasSecTex)
				{
					if (param.pSecSrcRect)
					{
						if (param.bSecSameuv)
						{
							if (!bSUVfind)
							{
								su1 = param.pSecSrcRect->left + param.pSecSrcRect->Width() * (u1-srcrect.left) / srcrect.Width();
								su2 = param.pSecSrcRect->left + param.pSecSrcRect->Width() * (u2-srcrect.left) / srcrect.Width();
								sv1 = param.pSecSrcRect->top + param.pSecSrcRect->Height() * (v1-srcrect.top) / srcrect.Height();
								sv2 = param.pSecSrcRect->top + param.pSecSrcRect->Height() * (v2-srcrect.top) / srcrect.Height();
								bSUVfind = true;
							}
						} else {
							const FRECT* pSecRect = param.pSecSrcRect + j;
							su1 = pSecRect->left + pSecRect->Width() * (u1-srcrect.left) / srcrect.Width();
							su2 = pSecRect->left + pSecRect->Width() * (u2-srcrect.left) / srcrect.Width();
							sv1 = pSecRect->top + pSecRect->Height() * (v1-srcrect.top) / srcrect.Height();
							sv2 = pSecRect->top + pSecRect->Height() * (v2-srcrect.top) / srcrect.Height();
						}
					} else {
						su1 = u1;
						su2 = u2;
						sv1 = v1;
						sv2 = v2;
					}
				}

				float rctW = param.pRect[j].Width();	// 实际要绘制到的顶点坐标
				float rctH = param.pRect[j].Height(); // 实际要绘制到的顶点坐标

				//在当前纹理中的位置... 关于0.5问题.. Directly Mapping Texels to Pixels 

				//左上
				thePos[0].x = param.pRect[j].left + param.pRect[j].Width()*(u1-srcrect.left)/srcrect.Width()  -0.5f;
				thePos[0].y = param.pRect[j].top  + param.pRect[j].Height()*(v1-srcrect.top)/srcrect.Height() -0.5f;
				//右上
				thePos[1].x = thePos[0].x + rctW*du/srcrect.Width();
				thePos[1].y = thePos[0].y;
				//右下
				thePos[2].x = thePos[1].x;
				thePos[2].y = thePos[0].y + rctH*dv/srcrect.Height();
				//左下
				thePos[3].x = thePos[0].x;
				thePos[3].y = thePos[2].y;

				float tu1 = (u1-lx)/(rx2-lx);
				float tv1 = (v1-ty)/(by2-ty);
				float tu2 = (u2-lx)/(rx2-lx);
				float tv2 = (v2-ty)/(by2-ty);
                
				///////矩阵变换
				if (param.pMatrix)
				{
					//m_ifDevice->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR );
#ifndef DRAW_BATCH_PIC_SSE
					for (int i=0;i<4;++i)
					{
						XPVec3TransformCoord(&vecOut, thePos+i, param.pMatrix);
						thePos[i] = vecOut;
					}
					//XPTRACE(L"%f, %f, %f, %f\n", posx1, posy1, posx2, posy2);
#else
					thePos2[0].x = thePos[0].x;
					thePos2[0].y = thePos[1].x;
					thePos2[1].x = thePos[2].x;
					thePos2[1].y = thePos[3].x;
					thePos2[2].x = thePos[0].y;
					thePos2[2].y = thePos[1].y;
					thePos2[3].x = thePos[2].y;
					thePos2[3].y = thePos[3].y;
					__asm
					{
						MOVUPS XMM0, thePos2
						MOVUPS XMM1, thePos2[16]

						MOV EBX, param
						MOV ECX, [EBX]param.pMatrix

						MOVSS XMM2, DWORD PTR [ECX]
						MOVSS XMM3, DWORD PTR [ECX + 4]
						MOVSS XMM4, DWORD PTR [ECX + 8]
						MOVSS XMM5, DWORD PTR [ECX + 12]
						MOVSS XMM6, DWORD PTR [ECX + 16]
						MOVSS XMM7, DWORD PTR [ECX + 20]

						SHUFPS XMM2, XMM2, 00H
						SHUFPS XMM3, XMM3, 00H
						SHUFPS XMM4, XMM4, 00H
						SHUFPS XMM5, XMM5, 00H
						SHUFPS XMM6, XMM6, 00H
						SHUFPS XMM7, XMM7, 00H

						MULPS XMM2, XMM0
						MULPS XMM3, XMM0
						MULPS XMM4, XMM0
						MULPS XMM5, XMM1
						MULPS XMM6, XMM1
						MULPS XMM7, XMM1

						ADDPS XMM5, XMM2
						ADDPS XMM6, XMM3
						ADDPS XMM7, XMM4

						MOVSS XMM2, DWORD PTR [ECX + 24]
						MOVSS XMM3, DWORD PTR [ECX + 28]
						MOVSS XMM4, DWORD PTR [ECX + 32]

						SHUFPS XMM2, XMM2, 00H
						SHUFPS XMM3, XMM3, 00H
						SHUFPS XMM4, XMM4, 00H

						ADDPS XMM5, XMM2
						ADDPS XMM6, XMM3
						ADDPS XMM7, XMM4
						DIVPS XMM5, XMM7
						DIVPS XMM6, XMM7

						MOVUPS thePos2, XMM5
						MOVUPS thePos2[16], XMM6
					}
					thePos[0].x = thePos2[0].x;
					thePos[1].x = thePos2[0].y;
					thePos[2].x = thePos2[1].x;
					thePos[3].x = thePos2[1].y;
					thePos[0].y = thePos2[2].x;
					thePos[1].y = thePos2[2].y;
					thePos[2].y = thePos2[3].x;
					thePos[3].y = thePos2[3].y;
#endif
				}
				////////////////////////////////////
				/*printf("%f %f %f %f %f %f %f %f\n", thePos2[0].x, thePos2[2].x, thePos2[0].y, thePos2[2].y, thePos2[1].x, thePos2[3].x, thePos2[1].y, thePos2[3].y );
				printf("%f %f %f %f %f %f %f %f\n", thePos[0].x, thePos[0].y, thePos[1].x, thePos[1].y, thePos[2].x, thePos[2].y, thePos[3].x, thePos[3].y );*/
				//int vboff = (m_iBatchOffset+j)*4;
				int vboff = j*4;
				for (int k=0;k<4;++k)
				{
					vertex[vboff+k].x = thePos[k].x;
					vertex[vboff+k].y = thePos[k].y;
					vertex[vboff+k].z = Z;
					vertex[vboff+k].rhw = 1.0f;
					vertex[vboff+k].c = (D3DCOLOR)color[k];
				}

				vertex[vboff+0].u = tu1; vertex[vboff+0].v = tv1;
				vertex[vboff+1].u = tu2; vertex[j*4+1].v = tv1;
				vertex[vboff+2].u = tu2; vertex[j*4+2].v = tv2;
				vertex[vboff+3].u = tu1; vertex[vboff+3].v = tv2;

				vertex[vboff+0].u2 = su1; vertex[vboff+0].v2 = sv1;
				vertex[vboff+1].u2 = su2; vertex[j*4+1].v2 = sv1;
				vertex[vboff+2].u2 = su2; vertex[j*4+2].v2 = sv2;
				vertex[vboff+3].u2 = su1; vertex[vboff+3].v2 = sv2;
				

				int iboff = j*6;
				index[iboff+0] = j*4+0;
				index[iboff+1] = j*4+1;
				index[iboff+2] = j*4+2;
				index[iboff+3] = j*4+2;
				index[iboff+4] = j*4+3;
				index[iboff+5] = j*4+0;
#ifdef XP_PERFORMANCE

				m_performance.m_iTotalPixelRenderedPerFrame += (int)((rctW*du/srcrect.Width())*(rctH*dv/srcrect.Height()));
#endif
			}

			XPV(m_batchIB->Unlock());
			XPV(m_batchVB->Unlock());

			if(bUVflag)
				continue; //当前纹理块与需要绘制的区域没有交集

			if( FAILED( m_ifDevice->SetTexture(0, texInfo.m_D3dTexture) )) 
			{
				m_stateMan.EnableSecondTex(false);
				return false;
			}
			if (hasSecTex)
			{
				m_ifDevice->SetTexture(1, secIt->second.m_CTextureInfoVector[0].m_D3dTexture);
			}
			m_ifDevice->SetFVF( TCT1VertexFVF );
			XPV(m_ifDevice->SetStreamSource(0, m_batchVB, 0, sizeof(TCT1VERTEX)));
			XPV(m_ifDevice->SetIndices(m_batchIB));

			hr = m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, m_iBatchVBOffset, 0/*m_iBatchOffset*4*/,
				param.rectCount*4, m_iBatchIBOffset, param.rectCount*2);

			if(FAILED(hr))
			{
				XPLOG_INFO(L"  DrawBatchPicture() 部分参数: m_iBatchVBOffset(%d), rectCount(%d), m_iBatchIBOffset(%d)\n",
							m_iBatchVBOffset, param.rectCount, m_iBatchIBOffset);

				XPLOG_INFO(L"  DrawBatchPicture() 部分参数: param.blend(%d), textureCnt(%d), hasSecTex(%d)\n",
							(int)(param.blend), a, (int)hasSecTex);
				return false;
			}

			if (hasSecTex)
			{
				m_ifDevice->SetTexture(1, NULL);
			}

			m_iBatchVBOffset += param.rectCount*4;
			m_iBatchIBOffset += param.rectCount*6;
#ifdef XP_PERFORMANCE
			m_performance.m_iDrawPrimitiveCountPerFrame++;
			m_performance.m_iDrawPictureCountPerFrame += param.rectCount;
#endif
		}
		m_stateMan.EnableSecondTex(false);
		return true;
	}

	bool DX9Renderer::DrawPicture(const DrawPictureParam& param)
	{
		// todo 测试，只要不是批量绘制，使用DrawXXXUP和DrawXXX 没什么效率差别， 因为顶点太少了
		// 先不用这个
		return DrawBatchPicture(param);
		//
		if(m_ifDevice==NULL)
			return false;

		//	StateSaver saver(m_ifDevice);
		PictureMap::iterator it = m_mapPictures.find(param.handle);
		if( it == m_mapPictures.end() )
			return false;
		int a = (int)(it->second.m_CTextureInfoVector.size());
		switch (param.blend)
		{
		case XPTEXBM_MODULATE:   // alpha混合
			SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
			break;
		case XPTEXBM_ADDITIVE:	// 色彩饱和
			SetAlphaBlendSrcType(XPBLEND_ONE);
			SetAlphaBlendDstType(XPBLEND_ONE);
			break;
		case XPTEXBM_COPY:
			SetAlphaBlendSrcType(XPBLEND_ONE);
			SetAlphaBlendDstType(XPBLEND_ZERO);
			break;
		case XPTEXBM_ADDITIVE_ALPHA:
			SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			SetAlphaBlendDstType(XPBLEND_ONE);
			break;
		case XPTEXBM_MUTIPLY:
			SetAlphaBlendSrcType(XPBLEND_DESTCOLOR);
			SetAlphaBlendDstType(XPBLEND_ZERO);
			break;
		}
		SetTextureColorBlendType(XPTOP_DEFAULT);
		XPVECTOR2 vecOut;
		XPVECTOR2 thePos[4];
		for(int i=0; i<a; i++)
		{
			int nPicWidth = it->second.m_info.m_nPicWidth;		// 原始图片宽度
			int nPicHeight = it->second.m_info.m_nPicHeight;    // 原始图片高度
			CTextureInfo texInfo = it->second.m_CTextureInfoVector[i];
			if( FAILED( m_ifDevice->SetTexture(0, texInfo.m_D3dTexture) )) return false;

			float texLeft = (float)texInfo.rect.left;	// 分片在原始图片中的位置
			float texTop = (float)texInfo.rect.top;     // 分片在原始图片中的位置
			float texW = (float)texInfo.rect.Width();   // 分片的原始宽度
			float texH = (float)texInfo.rect.Height();  // 分片的原始高度 

			D3DSURFACE_DESC textureDesc; 
			texInfo.m_D3dTexture->GetLevelDesc(0, &textureDesc);

			float texRealW = (float)textureDesc.Width;	// 分片纹理的实际宽度
			float texRealH = (float)textureDesc.Height; // 分片纹理的实际高度

			//绘制....

			//根据约束条件下取出来的纹理的大小再来确定画框的的大小...
			//7-24日
			////////////////////////////////////////
			float lx = texLeft/nPicWidth;				// 分片的纹理坐标
			float ty = texTop/nPicHeight;				//
			float rx = (texLeft+texW)/nPicWidth;		//
			float by = (texTop+texH)/nPicHeight;		//
			float rx2 = (texLeft + texRealW)/nPicWidth;
			float by2 = (texTop + texRealH)/nPicHeight;

			// 确定分片与 srcrect的交集
			FRECT srcrect(.0f, .0f, 1.0f, 1.0f);
			if (param.pSrcrect)
			{
				srcrect = *param.pSrcrect;
			}
			float u1 = max(srcrect.left, lx);
			float u2 = min(srcrect.right, rx);
			float v1 = max(srcrect.top, ty);
			float v2 = min(srcrect.bottom, by);
			if( (u1>u2)||(v1>v2) )
				continue;	//没有交集...
			float du = u2-u1;
			float dv = v2-v1;
			float rctW = param.pRect->Width();	// 实际要绘制到的顶点坐标
			float rctH = param.pRect->Height(); // 实际要绘制到的顶点坐标

			//在当前纹理中的位置... 关于0.5问题.. Directly Mapping Texels to Pixels 
			//x1 y1是左上
			thePos[0].x = param.pRect->left + param.pRect->Width()*(u1-srcrect.left)/srcrect.Width() -0.5f;
			thePos[0].y = param.pRect->top + param.pRect->Height()*(v1-srcrect.top)/srcrect.Height() -0.5f;
			//x2 y2是左下
			thePos[1].x = thePos[0].x;
			thePos[1].y = thePos[0].y + rctH*dv/srcrect.Height() ;//+0.5f
			//x3 y3是右下
			thePos[2].x = thePos[0].x + rctW*du/srcrect.Width() ;//+0.5f
			thePos[2].y = thePos[1].y;
			//x4 y4是右上
			thePos[3].x = thePos[2].x;
			thePos[3].y = thePos[0].y;

			float tu1 = (u1-lx)/(rx2-lx) ;//+ 0.5f/nPicWidth; // 
			float tv1 = (v1-ty)/(by2-ty) ;//+ 0.5f/nPicHeight;
			float tu2 = (u2-lx)/(rx2-lx) ;//+ 0.5f/nPicWidth;
			float tv2 = (v2-ty)/(by2-ty) ;//+ 0.5f/nPicHeight;

			///////矩阵变换
			if (param.pMatrix)
			{
				//m_ifDevice->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR );
				for (int i=0;i<4;++i)
				{
					XPVec3TransformCoord(&vecOut, thePos+i, param.pMatrix);
					thePos[i] = vecOut;
				}
				//XPTRACE(L"%f, %f, %f, %f\n", posx1, posy1, posx2, posy2);
			}

			////////////////////////////////////
			m_ifDevice->SetFVF( TCT1VertexFVF );

			D3DCOLOR color[4];
			if (param.pColor == NULL)
			{
				color[0] = color[1] = color[2] = color[3] = 0xFFFFFFFF;
			} else {
				if (param.colorCount < 4)
				{
					color[0] = color[1] = color[2] = color[3] = param.pColor->data;
				} else {
					for (int i=0;i<4;i++)
					{
						color[i] = param.pColor[i].data;
					}
				}
			}
			
			TCT1VERTEX vertex[] = {
				TCT1VERTEX(thePos[0].x, thePos[0].y, Z, 1.0f, color[0], tu1, tv1 ),
				TCT1VERTEX(thePos[1].x, thePos[1].y, Z, 1.0f, color[3], tu1, tv2 ),
				TCT1VERTEX(thePos[2].x, thePos[2].y, Z, 1.0f, color[2], tu2, tv2 ),
				TCT1VERTEX(thePos[3].x, thePos[3].y, Z, 1.0f, color[1], tu2, tv1 )
			};						
			m_ifDevice->DrawPrimitiveUP(D3DPT_TRIANGLEFAN, 2, vertex, sizeof(TCT1VERTEX));
#ifdef XP_PERFORMANCE
			m_performance.m_iDrawPrimitiveUPCountPerFrame++;
			m_performance.m_iDrawPictureCountPerFrame++;
			m_performance.m_iTotalPixelRenderedPerFrame += (int)((rctW*du/srcrect.Width())*(rctH*dv/srcrect.Height()));
#endif
		}
		return true;
	}

	bool DX9Renderer::SelectRenderEffect(XPRENDER_EFFECT xpre)
	{
		return m_stateMan.SelEffect(xpre);
	}

	XPRENDER_EFFECT DX9Renderer::GetRenderEffect()
	{
		return m_stateMan.GetEffect();
	}

	bool DX9Renderer::SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam)
	{
		return m_stateMan.SetShaderParam(xpre, pParam);
	}
	bool DX9Renderer::SetShaderTexture(XPRENDER_EFFECT xpre, const char* name, PictureHandle pic)
	{
		const CPicInfo* pInfo = GetPicInfo(pic);
		if(pInfo==NULL)
			return m_stateMan.SetShaderTexture(xpre, name, NULL);
		LPDIRECT3DTEXTURE9 pTex = pInfo->m_CTextureInfoVector[0].m_D3dTexture;
		return m_stateMan.SetShaderTexture(xpre, name, pTex);
	}
	// 释放图片
	bool DX9Renderer::FreePicture(PictureHandle handle)
	{
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
		{
			XPLOG_ERROR(L" FreePicture(%d) Failed.\n", handle);
			return false;
		}
		int cnt=0;
		int i=0;
		cnt = (int)(it->second.m_CTextureInfoVector.size());
		for(i=0; i<cnt; i++)
		{
			IDirect3DTexture9* ifTexture = it->second.m_CTextureInfoVector[i].m_D3dTexture;
			XPSAFE_RELEASE(ifTexture);
#ifdef XP_PERFORMANCE
			m_performance.m_iManagedTextureCount--;
			m_performance.m_iManagedTextureMemory -= it->second.m_CTextureInfoVector[i].mem;
#endif
		}

		m_mapPictures.erase(it);

		return true;
	}


	// 清屏
	bool DX9Renderer::Clear(XPCOLOR color)
	{
		if( m_ifDevice == NULL ) return false;
		return SUCCEEDED(m_ifDevice->Clear(0, NULL, D3DCLEAR_TARGET, color.data, 1.0f, 0));
	}

	XP_DEVICE_STATE DX9Renderer::TestDevice()
	{
		if (!m_bDeviceLost)
			return XPDS_OK;
		if(m_ifDevice == NULL) return XPDS_NO_DEVICE;
		HRESULT hr = m_ifDevice->TestCooperativeLevel();
		switch( hr )
		{
		case D3DERR_DEVICELOST:
			return XPDS_LOST_DEVICE;
			break;
		case D3DERR_DEVICENOTRESET:
			if ( !ResetD3D(NULL) )
				return XPDS_RESET_ERROR;
			m_bDeviceLost = false;
			break;
		case D3DERR_DRIVERINTERNALERROR:
			return XPDS_INTERNAL_ERROR;
		default:
			break;
		}
		return XPDS_OK;
	}

	bool DX9Renderer::JustBegin()
	{
		return SUCCEEDED(m_ifDevice->BeginScene());
	}

	bool DX9Renderer::JustEnd()
	{
		return SUCCEEDED(m_ifDevice->EndScene());
	}

	// 开始绘制
	bool DX9Renderer::Begin(XPCOLOR color)
	{
		if(m_ifDevice == NULL) return false;
		if (m_bAutoTestDevice)
		{
			while( m_bDeviceLost )
			{
				switch (TestDevice())
				{
				case XPDS_LOST_DEVICE:
					Sleep(100);
					break;
				case XPDS_OK:
					break;
				default:
					return false;
				}
			}
		}
		//hr = (HRESULT)Clear(D3DCOLOR_ARGB(125,0,200,155));
		if( !Clear(color.data) )
			return false;
		m_iBatchVBOffset = 0;
		m_iBatchIBOffset = 0;
		return SUCCEEDED(m_ifDevice->BeginScene());
	}

	// 结束绘制
	bool DX9Renderer::End()
	{
		// performance
#ifdef XP_PERFORMANCE
		m_performance.ClearFrame();
#endif
		if( m_ifDevice == NULL ) return false;

		HRESULT hr = m_ifDevice->EndScene();
		if( FAILED(hr) ) 
			return false;

		// TODO test swap effect
		//考虑一下要不要Reset
		assert(m_stkRenderTargets.empty());
		hr = m_ifDevice->Present(m_pSrcRect, m_pDstRect, NULL, NULL);
		bool r = SUCCEEDED(hr);

		if( D3DERR_DEVICELOST == hr)
		{
			m_bDeviceLost = true;
			r = true;
		}
		return Renderer::End() && r;
	}

	// 绘制文本

	bool DX9Renderer::DrawText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color)
	{
		
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawTextCountPerFrame++;
#endif
		
		return 0 != m_ifDefaultFont->DrawText(0, text, nstr, rect,DT_TOP | DT_LEFT, color.data);
	}

	bool DX9Renderer::DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, XPCOLOR color)
	{

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawTextCountPerFrame++;
#endif
		return 0 != m_ifTmpFont->DrawText(0, text, nstr, rect,DT_TOP | DT_CENTER, color.data);
	}

	int DX9Renderer::GenTexture(IDirect3DTexture9* pSrcTex, XPTEXTURE_FORMAT texfmt, XPPOOL_TYPE pooltype, CTextureInfo& TextureInfo)
	{
		HRESULT hr;
		RECT srcRCT = TextureInfo.rect;
		hr = D3DXCreateTexture(
			m_ifDevice,
			TextureInfo.rect.Width(),
			TextureInfo.rect.Height(),
			1, // 指定共生成几级mipmap 0 create a complete mipmap chain 1...
			pooltype == XPPOOL_VIDEOMEM ? D3DUSAGE_DYNAMIC : 0,
			(D3DFORMAT)texfmt,
			(D3DPOOL)pooltype, &TextureInfo.m_D3dTexture); //
				
		if( pooltype == XPPOOL_VIDEOMEM && hr == D3DERR_OUTOFVIDEOMEMORY )
		{
			XPLOG_INFO(L"pooltype == XPPOOL_VIDEOMEM && hr == D3DERR_OUTOFVIDEOMEMORY \n");
			return 1;
		}

		if(FAILED(hr))
		{
			XPLOG_INFO(L"  GenTexture() 部分参数: TextureInfoWidth(%d), TextureInfoHeight(%d), texfmt(%d), pooltype(%d)\n",
						   TextureInfo.rect.Width(), TextureInfo.rect.Height(), (int)texfmt, (int)pooltype);
			
			void* pMsgBuf;
			::FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM,
							NULL, hr, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
							(LPWSTR)&pMsgBuf, 0, NULL );

			XPLOG_INFO(L"D3DXCreateTexture Failed--GenTexture() 部分参数: error info(%s), AvailableTextureMem (%d) MB\n",
							(LPWSTR)&pMsgBuf, GetAvailableTextureMem());

			LocalFree(pMsgBuf);

			return -1;
		}

		D3DSURFACE_DESC textureDesc; 
		TextureInfo.m_D3dTexture->GetLevelDesc(0, &textureDesc);
		if( textureDesc.Format != (D3DFORMAT)texfmt )
			return -2;
		/*
		if( textureDesc.Width != TextureInfo.rect.Width()
			|| textureDesc.Width != TextureInfo.rect.Width() )
		{
			XPLOG_INFO(L"  GenTexture, (%d,%d) to (%d,%d)\n"
				, TextureInfo.rect.Width()
				, TextureInfo.rect.Height()
				, textureDesc.Width
				, textureDesc.Height);
		}
		*/
#ifdef XP_PERFORMANCE
		int npix = textureDesc.Width * textureDesc.Height;
		switch( texfmt )
		{
		case XPTEXFMT_A4R4G4B4:
			TextureInfo.mem = 2 * npix;
			break;
		case XPTEXFMT_A8R8G8B8:
			TextureInfo.mem = 4 * npix;
			break;
		case XPTEXFMT_DXT2:
		case XPTEXFMT_DXT3:
		case XPTEXFMT_DXT4:
		case XPTEXFMT_DXT5:
			TextureInfo.mem = npix;
			break;
		case XPTEXFMT_DXT1:
			TextureInfo.mem = npix / 2;
			break;
		default:
			break;
		}
#endif

		//XPRINT_PICTURE(L"\t\tin gentex, before copy\n");
		if( pSrcTex )
		{
//			D3DSURFACE_DESC textureSrc; 
//			pSrcTex->GetLevelDesc(0, &textureSrc);

			D3DLOCKED_RECT lockedSrcRect;
			pSrcTex->LockRect(0, &lockedSrcRect, 0, 0); 

			D3DLOCKED_RECT lockedDesRect;
			if( FAILED(TextureInfo.m_D3dTexture->LockRect(0, &lockedDesRect, 0, 0)))
				return -3;
			else
			{
				if( texfmt == XPTEXFMT_A4R4G4B4 ) // 16
				{
					for(int i=0; i<(TextureInfo.rect.bottom-TextureInfo.rect.top); i++)
					{
						WORD* imageSrcData = (WORD*)((BYTE*)lockedSrcRect.pBits + (i+TextureInfo.rect.top)*lockedSrcRect.Pitch) +TextureInfo.rect.left;
						WORD* imageDesData = (WORD*)((BYTE*)lockedDesRect.pBits + i*lockedDesRect.Pitch);
						memcpy(imageDesData, imageSrcData, 2*(TextureInfo.rect.right-TextureInfo.rect.left));
					}
				}
				else
				{
					for(int i=0; i<(TextureInfo.rect.bottom-TextureInfo.rect.top); i++)
					{
						DWORD* imageSrcData = (DWORD*)((BYTE*)lockedSrcRect.pBits + (i+TextureInfo.rect.top)*lockedSrcRect.Pitch) +TextureInfo.rect.left;
						DWORD* imageDesData = (DWORD*)((BYTE*)lockedDesRect.pBits + i*lockedDesRect.Pitch);
						memcpy(imageDesData, imageSrcData, 4*(TextureInfo.rect.right-TextureInfo.rect.left));
					}
				}
				TextureInfo.m_D3dTexture->UnlockRect(0);
			}
			pSrcTex->UnlockRect(0);
		}
		//XPRINT_PICTURE(L"\t\tin gentex, after copy\n");

		return 0;
	}

	XDisplayMode DX9Renderer::GetDisplayMode() const
	{
		return m_curDisplayMode;
	}

	bool DX9Renderer::OnLostDevice()
	{
		for(std::vector<IRunnable*>::const_iterator it = m_lostTasks.begin(), ie = m_lostTasks.end(); it != ie; ++it)
			(*it)->Run();
		if(m_pdshowTexRender != NULL)
			m_pdshowTexRender->ReleaseVideoTexture();
		ReleaseRenderTargets();
		ReleaseTexture();
		m_ifDefaultFont->OnLostDevice();
		m_ifTmpFont->OnLostDevice();
		DestroyBatchVB();
		m_stateMan.OnRelease();

		return true;
		//return DestroyResource();
	}

	// 设备丢失恢复时的回调
	void DX9Renderer::SetRestoreTask(IRunnable *pTask)
	{
		m_restoreTasks.push_back(pTask);
	}

	// 设备丢失时的回调
	void DX9Renderer::SetLostTask(IRunnable *pTask)
	{
		m_lostTasks.push_back(pTask);
	}

	bool DX9Renderer::OnResetDevice()
	{
		HWND hWnd = GetDesktopWindow();
		HDC hdc = GetDC(hWnd);
		m_curDisplayMode.bitperpix = GetDeviceCaps(hdc, BITSPIXEL);
		ReleaseDC(hWnd, hdc);
		m_stateMan.OnRestore();
		InitBatchVB();
		ShowCursor(m_fUseCursor);
		SetCursorPicture(m_iCurCursorHandle, m_fCurCursorOffsetX, m_fCurCursorOffsetY);
		m_ifTmpFont->OnResetDevice();
		m_ifDefaultFont->OnResetDevice();

 		if(m_pdshowTexRender != NULL)
 		{
 			m_pdshowTexRender->Restore(); //重建资源
 		}
		m_pParticleMan->OnResetDevice();
		//Render内部的资源都恢复了之后再弄task
		for(std::vector<IRunnable*>::const_iterator it = m_restoreTasks.begin(), ie = m_restoreTasks.end(); it != ie; ++it)
			(*it)->Run();
		return true;
	}

	
	bool DX9Renderer::ResetD3D(const XDisplayMode *newmode)
	{
		if (newmode != NULL && newmode->equalExceptScreenMode(m_curDisplayMode) &&
			newmode->screenMode != XPSM_REAL_FULL_SCREEN && 
			m_curDisplayMode.screenMode != XPSM_REAL_FULL_SCREEN)
		{
			m_curDisplayMode = *newmode;
			return true;
		}
		if( ! OnLostDevice() )
			return false;

		if( newmode != NULL )
		{
			D3DPRESENT_PARAMETERS d3dpp = m_d3dpp;
			d3dpp.BackBufferFormat = (newmode->bitperpix == 16 ? D3DFMT_R5G6B5 : D3DFMT_A8R8G8B8);
			d3dpp.BackBufferWidth= newmode->width;
			d3dpp.BackBufferHeight= newmode->height;
			d3dpp.Windowed = newmode->screenMode != XPSM_REAL_FULL_SCREEN;

			if( FAILED( m_ifDevice->Reset(&d3dpp) ) )
				return false;

			m_d3dpp = d3dpp;
			m_curDisplayMode = *newmode;
		}
		else
		{
			if( FAILED( m_ifDevice->Reset(&m_d3dpp) ) )
				return false;
		}

		if( ! OnResetDevice() )
			return false;

		return true;

	}

	bool DX9Renderer::SetDisplayMode(const XDisplayMode &dmode)
	{
		if( dmode == m_curDisplayMode) return true;
		if( std::find(m_displayModes.begin(), m_displayModes.end(), dmode) == m_displayModes.end()) 
			return false;

		return ResetD3D(&dmode);
	}

	std::vector<XDisplayMode> DX9Renderer::EnumDisplayMode() const
	{
		return m_displayModes;
	}

	bool DX9Renderer::InitDisplayMode(IDirect3D9* ifD3D9, const XDisplayMode &dmode)
	{

		D3DDISPLAYMODE mode;
		if( SUCCEEDED( ifD3D9->CheckDeviceType(m_adapterid, m_devtype, D3DFMT_R5G6B5, D3DFMT_R5G6B5, TRUE)))
		{
			if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5,D3DUSAGE_RENDERTARGET,D3DRTYPE_SURFACE,D3DFMT_R5G6B5))
				&& SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5,0,D3DRTYPE_TEXTURE, D3DFMT_A4R4G4B4)))
			{
				//todo
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5, 0, D3DRTYPE_TEXTURE, D3DFMT_A4R4G4B4)))
					m_supportTexFmts[0].insert((XPTEXTURE_FORMAT)D3DFMT_A4R4G4B4);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5, 0, D3DRTYPE_TEXTURE, D3DFMT_A8R8G8B8)))
					m_supportTexFmts[0].insert((XPTEXTURE_FORMAT)D3DFMT_A8R8G8B8);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT1)))
					m_supportTexFmts[0].insert((XPTEXTURE_FORMAT)D3DFMT_DXT1);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT2)))
					m_supportTexFmts[0].insert((XPTEXTURE_FORMAT)D3DFMT_DXT2);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT3)))
					m_supportTexFmts[0].insert((XPTEXTURE_FORMAT)D3DFMT_DXT3);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT4)))
					m_supportTexFmts[0].insert((XPTEXTURE_FORMAT)D3DFMT_DXT4);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_R5G6B5, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT5)))
					m_supportTexFmts[0].insert((XPTEXTURE_FORMAT)D3DFMT_DXT5);

				//
				UINT nMode = ifD3D9->GetAdapterModeCount(m_adapterid, D3DFMT_R5G6B5);
				for(UINT i = 0; i < nMode; ++i)
				{
					ifD3D9->EnumAdapterModes(m_adapterid, D3DFMT_R5G6B5, i, &mode);
					m_displayModes.push_back(XDisplayMode(mode.Width, mode.Height, 16, XPSM_WINDOW));
					m_displayModes.push_back(XDisplayMode(mode.Width, mode.Height, 16, XPSM_FADE_FULL_SCREEN));
					m_displayModes.push_back(XDisplayMode(mode.Width, mode.Height, 16, XPSM_REAL_FULL_SCREEN));
				}
			}
		}

		if( SUCCEEDED( ifD3D9->CheckDeviceType(m_adapterid, m_devtype, D3DFMT_X8R8G8B8, D3DFMT_A8R8G8B8, TRUE)))
		{
			if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_X8R8G8B8,D3DUSAGE_RENDERTARGET,D3DRTYPE_SURFACE,D3DFMT_A8R8G8B8)))
			{
				//todo
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_X8R8G8B8, 0, D3DRTYPE_TEXTURE, D3DFMT_A4R4G4B4)))
					m_supportTexFmts[1].insert((XPTEXTURE_FORMAT)D3DFMT_A4R4G4B4);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_X8R8G8B8, 0, D3DRTYPE_TEXTURE, D3DFMT_A8R8G8B8)))
					m_supportTexFmts[1].insert((XPTEXTURE_FORMAT)D3DFMT_A8R8G8B8);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_X8R8G8B8, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT1)))
					m_supportTexFmts[1].insert((XPTEXTURE_FORMAT)D3DFMT_DXT1);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_X8R8G8B8, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT2)))
					m_supportTexFmts[1].insert((XPTEXTURE_FORMAT)D3DFMT_DXT2);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_X8R8G8B8, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT3)))
					m_supportTexFmts[1].insert((XPTEXTURE_FORMAT)D3DFMT_DXT3);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_X8R8G8B8, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT4)))
					m_supportTexFmts[1].insert((XPTEXTURE_FORMAT)D3DFMT_DXT4);
				if( SUCCEEDED( ifD3D9->CheckDeviceFormat(m_adapterid, m_devtype, D3DFMT_X8R8G8B8, 0, D3DRTYPE_TEXTURE, D3DFMT_DXT5)))
					m_supportTexFmts[1].insert((XPTEXTURE_FORMAT)D3DFMT_DXT5);

				UINT nMode = ifD3D9->GetAdapterModeCount(m_adapterid, D3DFMT_X8R8G8B8);
				for(UINT i = 0; i < nMode; ++i)
				{
					ifD3D9->EnumAdapterModes(m_adapterid, D3DFMT_X8R8G8B8, i, &mode);
					mode.Format = D3DFMT_A8R8G8B8;
					m_displayModes.push_back(XDisplayMode(mode.Width, mode.Height, 32, XPSM_WINDOW));
					m_displayModes.push_back(XDisplayMode(mode.Width, mode.Height, 32, XPSM_FADE_FULL_SCREEN));
					m_displayModes.push_back(XDisplayMode(mode.Width, mode.Height, 32, XPSM_REAL_FULL_SCREEN));
				}
			}
		}

		//
		if( m_displayModes.empty() ) return false;

		if( m_displayModes.end() == std::find(m_displayModes.begin(), m_displayModes.end(), dmode))
			m_curDisplayMode = XDisplayMode(800, 600, 32); // todo 假设一定存在
		else
			m_curDisplayMode = dmode;
		return true;
	}

	// 改变源和目的矩形区域
	void DX9Renderer::SetSourceRect(const CRECT* pSrcRect)
	{	
		if( pSrcRect )
		{
			if(pSrcRect->bottom <=0 || pSrcRect->right <=0)
			{
				return; //Direct3D9: (ERROR) :Unable to present with invalid source RECT
			}

			if( NULL == m_pSrcRect ) 
				m_pSrcRect = (CRECT*)XAlloc(sizeof(CRECT));
			*m_pSrcRect= *pSrcRect;
		}
		else
		{
			if( m_pSrcRect )
				XFree(m_pSrcRect);
			m_pSrcRect = NULL;
		}
	}

	void DX9Renderer::SetDestRect(const CRECT* pDstRect)
	{
		if( pDstRect )
		{
			if(pDstRect->bottom <=0 || pDstRect->right <=0)
			{
				return; //Direct3D9: (ERROR) :Unable to present with invalid destination RECT
			}

			if( NULL == m_pDstRect ) 
				m_pDstRect = (CRECT*)XAlloc(sizeof(CRECT));
			*m_pDstRect= *pDstRect;
		}
		else
		{
			if( m_pDstRect )
				XFree(m_pDstRect);
			m_pDstRect = NULL;
		}
	}

	//2015 Yao
	void DX9Renderer::ResizePic(int &value)
	{
		if(value <= 16)
		{
			value = 16;
		}
		else if(value > 16 && value <= 32)
		{
			value = 32;
		}
		else if(value > 32 && value <= 64)
		{
			value = 64;
		}
		else if(value > 64 && value <= 128)
		{
			value = 128;
		}
		else if(value > 128 && value <= 256)
		{
			value = 256;
		}
		else if(value > 256 && value <= 512)
		{
			value = 512;
		}
		else if(value > 512 && value <= 1024)
		{
			value = 1024;
		}
		else if(value > 1024)
		{
			value = 2048;
		}
	}

	bool DX9Renderer::Is2Power(int value)
	{
		if(value == 8 || value == 16 || value == 32 || value == 64 || value == 128 || value == 256 || value == 512 || value == 1024 || value == 2048)
		{
			return true;
		}
		return false;
	}


	PictureHandle DX9Renderer::ClonePicture(PictureHandle handle, double scale, bool bEqualScale, int& srcW, int& srcH, int& dstW, int& dstH)
	{
		if(m_ifDevice==NULL)
			return INVALID_PICTURE_HANDLE;

		PictureMap::iterator it = m_mapPictures.find(handle);
		if ( it == m_mapPictures.end() )
			return INVALID_PICTURE_HANDLE;

		int tmpWidth = 0, tmpHeight = 0;
		PictureHandle result = INVALID_PICTURE_HANDLE;
		if (!bEqualScale)
		{
			result = NewPicture(static_cast<int>(it->second.m_info.m_nPicWidth * scale / 4) * 4, 
				static_cast<int>(it->second.m_info.m_nPicHeight * scale / 4) * 4, NULL, it->second.m_info.m_texfmt);
		}
		else
		{
			int tmpWidth = it->second.m_info.m_nPicWidth;
			int tmpHeight = it->second.m_info.m_nPicHeight;
			srcW = tmpWidth;
			srcH = tmpHeight;

			if (Is2Power(tmpWidth) && Is2Power(tmpHeight))
				return INVALID_PICTURE_HANDLE;

			ResizePic(tmpWidth);
			ResizePic(tmpHeight);
			dstW = tmpWidth;
			dstH = tmpHeight;
			result = NewPicture(tmpWidth,tmpHeight, NULL, it->second.m_info.m_texfmt);
		}
		
		if (result == INVALID_PICTURE_HANDLE)
			return result;

		PictureMap::iterator newit = m_mapPictures.find(result);

		IDirect3DSurface9 *pSurface = NULL, *pNewSurface = NULL;
		std::vector<CTextureInfo>::iterator tIt = it->second.m_CTextureInfoVector.begin(), tIe = it->second.m_CTextureInfoVector.end(),
			nIt = newit->second.m_CTextureInfoVector.begin();

		for (;tIt!=tIe;++tIt, ++nIt)
		{
			tIt->m_D3dTexture->GetSurfaceLevel(0, &pSurface);
			nIt->m_D3dTexture->GetSurfaceLevel(0, &pNewSurface);
			XPASSERT(pSurface);
			XPASSERT(pNewSurface);

			if (bEqualScale)
			{
				Nuclear::CRECT rect;
				rect.top = 0;
				rect.bottom = it->second.m_info.m_nPicHeight;
				rect.left = 0;
				rect.right = it->second.m_info.m_nPicWidth;

				D3DXLoadSurfaceFromSurface(pNewSurface, NULL, &rect, pSurface, NULL, NULL, D3DX_DEFAULT, 0);
			}
			else
			{
				D3DXLoadSurfaceFromSurface(pNewSurface, NULL, NULL, pSurface, NULL, NULL, D3DX_DEFAULT, 0);
			}
			
			XPSAFE_RELEASE(pSurface);
			XPSAFE_RELEASE(pNewSurface);
		}
		return result;
	}

	PictureHandle DX9Renderer::GetBackBuffer()
	{		
		HRESULT hr;
		D3DSURFACE_DESC surfaceDesc;
		IDirect3DSurface9 *pBackBuffer;
		IDirect3DSurface9 *pBackBufferCopy;

		XPV(m_ifDevice->GetBackBuffer(0, 0,D3DBACKBUFFER_TYPE_MONO, &pBackBuffer));
		XPV(pBackBuffer->GetDesc(&surfaceDesc));
		PictureHandle result = NewPicture(surfaceDesc.Width, surfaceDesc.Height, NULL, (XPTEXTURE_FORMAT)surfaceDesc.Format);
		if (result == INVALID_PICTURE_HANDLE)
			return result;
		PictureMap::iterator it = m_mapPictures.find(result);
		XPASSERT(it != m_mapPictures.end());
		XPASSERT(it->second.m_CTextureInfoVector.size() == 1);
		IDirect3DTexture9* pTexture = it->second.m_CTextureInfoVector[0].m_D3dTexture;
		pTexture->GetSurfaceLevel(0, &pBackBufferCopy);
		XPV(D3DXLoadSurfaceFromSurface(pBackBufferCopy, NULL, NULL, pBackBuffer, NULL, NULL, D3DX_FILTER_NONE, 0));
		XPSAFE_RELEASE(pBackBufferCopy);
		XPSAFE_RELEASE(pBackBuffer);
		return result;
	}

	bool DX9Renderer::GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix, CRECT* pRect, XPPIXEL_FORMAT fmt)
	{
		int tickStart = (int)GetMilliSeconds();
		if( pBuf == NULL )
			return false;
		bool localalloc = ((*pBuf) == NULL);
		HRESULT hr;
		D3DSURFACE_DESC surfaceDesc;
		IDirect3DSurface9 *pBackBuffer;
		IDirect3DSurface9 *pBackBufferCopy;
		IDirect3DSurface9 *pBackBufferTemp;
		D3DFORMAT format;

		XPV(m_ifDevice->GetBackBuffer(0, 0,D3DBACKBUFFER_TYPE_MONO, &pBackBuffer));
		XPV(pBackBuffer->GetDesc(&surfaceDesc));
		switch (fmt)
		{
		case XPPIXELFMT_DEFAULT:
			format = surfaceDesc.Format == D3DFMT_R5G6B5?D3DFMT_A4R4G4B4:D3DFMT_A8R8G8B8;
			break;
		case XPPIXELFMT_A8R8G8B8:
			format = D3DFMT_A8R8G8B8;
			break;
		case XPPIXELFMT_A4R4G4B4:
			format = D3DFMT_A4R4G4B4;
			break;
		case XPPIXELFMT_R5G6B5:
			format = D3DFMT_R5G6B5;
			break;
		default:
			return false;
		}
		XPV(m_ifDevice->CreateOffscreenPlainSurface(surfaceDesc.Width, surfaceDesc.Height, surfaceDesc.Format, 
			D3DPOOL_SYSTEMMEM, &pBackBufferTemp, NULL));

		XPV(m_ifDevice->GetRenderTargetData(pBackBuffer, pBackBufferTemp));
		XPSAFE_RELEASE(pBackBuffer);

		XPV(m_ifDevice->CreateOffscreenPlainSurface(surfaceDesc.Width, surfaceDesc.Height, format, 
			D3DPOOL_SYSTEMMEM, &pBackBufferCopy, NULL));

		XPV(D3DXLoadSurfaceFromSurface(pBackBufferCopy, NULL, NULL, pBackBufferTemp, NULL, NULL, D3DX_FILTER_NONE, 0));

		
		XPSAFE_RELEASE(pBackBufferTemp);

		D3DLOCKED_RECT lockedRect;
		pBackBufferCopy->LockRect(&lockedRect, pRect, 0);
		int bufferSize, width, height, bytePerPix, pitch;
		//不会有其他格式了吧。。。。。。。。
		switch (surfaceDesc.Format)
		{
		case D3DFMT_A8R8G8B8:
		case D3DFMT_X8R8G8B8:
			bytePerPix = 4;
			break;
		case D3DFMT_R5G6B5:
		case D3DFMT_A4R4G4B4:
			bytePerPix = 2;
			break;
		default:
			return false;
		}
		if (pBytePerPix)
		{
			*pBytePerPix = bytePerPix;
		}
		if (pRect)
		{
			width = pRect->Width();
			height = pRect->Height();
		} else {
			width = surfaceDesc.Width;
			height= surfaceDesc.Height;
		}
		pitch = width * bytePerPix;
		bufferSize = height * pitch;
		if (pSize)
		{
			*pSize = bufferSize;
		}
		if( localalloc )
			*pBuf = XAlloc(bufferSize);
		BYTE* imageSrcData = static_cast<BYTE*>(lockedRect.pBits);
		BYTE* imageDesData = static_cast<BYTE*>(*pBuf);
		for (int i=0;i<height;i++)
		{
			memcpy(imageDesData, imageSrcData, pitch);
			imageSrcData += lockedRect.Pitch;
			imageDesData += pitch;
		}
		pBackBufferCopy->UnlockRect();
		XPSAFE_RELEASE(pBackBufferCopy);
		int tickEnd = (int)GetMilliSeconds();
		XPTRACE(L"GetBackBufferData, use time %d ms\n", tickEnd- tickStart);
		return hr == S_OK;
	}

	bool DX9Renderer::PrintScreen(void** pbuf, int*size,XPIMAGE_FILEFORMAT filetype,const CRECT* src)
	{
//		int tickStart = (int)GetMilliSeconds();
		if( pbuf == NULL )
			return false;
		bool localalloc = ((*pbuf) == NULL);
		HRESULT hr;
		D3DSURFACE_DESC surfaceDesc;
		IDirect3DSurface9 *pBackBuffer;
		IDirect3DSurface9 *pBackBufferTemp;
		
		
		XPV(m_ifDevice->GetBackBuffer(0, 0,D3DBACKBUFFER_TYPE_MONO, &pBackBuffer));
		XPV(pBackBuffer->GetDesc(&surfaceDesc));
		XPV(m_ifDevice->CreateOffscreenPlainSurface(surfaceDesc.Width, surfaceDesc.Height, surfaceDesc.Format, 
			D3DPOOL_SYSTEMMEM, &pBackBufferTemp, NULL));
		XPV(m_ifDevice->GetRenderTargetData(pBackBuffer, pBackBufferTemp));
		XPSAFE_RELEASE(pBackBuffer);


		LPD3DXBUFFER buffer = NULL;
		XPV(D3DXSaveSurfaceToFileInMemory(&buffer, (D3DXIMAGE_FILEFORMAT)filetype, pBackBufferTemp, NULL, src));

		*size = buffer->GetBufferSize();
		if( localalloc )
			*pbuf = XAlloc(*size);
		memcpy(*pbuf, buffer->GetBufferPointer(), *size);
		XPSAFE_RELEASE(buffer);
		XPSAFE_RELEASE(pBackBufferTemp);
		
//		int tickEnd = (int)GetMilliSeconds();
//		XPTRACE(L"PrintScreen, use time %d ms\n", tickEnd- tickStart);
		return hr == S_OK;
	}
	
	const DX9Renderer::CPicInfo* DX9Renderer::GetPicInfo(PictureHandle handle) const
	{
		PictureMap::const_iterator it = m_mapPictures.find(handle);
		if ( m_mapPictures.end() != it)
			return &(it->second);

		return NULL;
	}
	
	bool DX9Renderer::GetPictureInfo(PictureHandle handle, PictureInfo& picinfo) const
	{
		PictureMap::const_iterator it = m_mapPictures.find(handle);
		if ( m_mapPictures.end() != it)
		{
			picinfo = it->second.m_info;
			return true;
		}
		return false;
	}

	bool DX9Renderer::GetPictureInfo(PictureHandle handle, int& pitch, int& width, int& height) const
	{
		PictureMap::const_iterator it = m_mapPictures.find(handle);
		if (m_mapPictures.end() != it)
		{
			width = it->second.m_info.m_nPicWidth;
			height = it->second.m_info.m_nPicHeight;

			XPTEXTURE_FORMAT texfmt = it->second.m_info.m_texfmt;
			switch( texfmt )
			{
			case XPTEXFMT_A4R4G4B4:
				pitch = (2 * width +3)&~3;
				break;
			case XPTEXFMT_A8R8G8B8:
				pitch = 4 * width;
				break;
			case XPTEXFMT_DXT2:
			case XPTEXFMT_DXT3:
			case XPTEXFMT_DXT4:
			case XPTEXFMT_DXT5:
				pitch = 4* ( (width+3)&~3 );
				break;
			case XPTEXFMT_DXT1:
				pitch = 2* ( (width+3)&~3 );
				break;
			default:
				pitch = 0;
				break;
			}

			return true;
		}
		return false;
	}

	// 光标

	bool DX9Renderer::GetCurrentCursor(PictureHandle &handle, int& x, int& y)
	{
		handle = m_iCurCursorHandle;
		x = m_fCurCursorOffsetX;
		y = m_fCurCursorOffsetY;
		return m_fUseCursor;
	}

	bool DX9Renderer::ShowCursor(bool f)
	{
		if( m_ifDevice == NULL ) return false;
		m_fUseCursor = f;
		m_ifDevice->ShowCursor(f?TRUE:FALSE);
		return true;
	}

	bool DX9Renderer::SetCursorPosition(int x, int y)
	{
		if( m_ifDevice == NULL ) return false;
		RECT rect;
		GetWindowRect(m_hwnd,&rect);
		int iCaptionHeight = GetSystemMetrics(SM_CYCAPTION);
		int iFixedFrameSizeY = GetSystemMetrics(SM_CYFIXEDFRAME) + GetSystemMetrics(SM_CYBORDER);
		int iFixedFrameSizeX = GetSystemMetrics(SM_CXFIXEDFRAME) + GetSystemMetrics(SM_CXBORDER);
		m_ifDevice->SetCursorPosition(x+rect.left+iFixedFrameSizeX, y+rect.top+iCaptionHeight+iFixedFrameSizeY, 0);
		//m_ifDevice->SetCursorPosition(x, y, 0);
		return true;
	}

	bool DX9Renderer::SetCursorPicture(PictureHandle handle, int offsetx, int offsety)
	{
		if( m_ifDevice == NULL ) return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;		

		m_iCurCursorHandle = handle;
		m_fCurCursorOffsetX = offsetx;
		m_fCurCursorOffsetY = offsety;
		IDirect3DTexture9 *tex = it->second.m_CTextureInfoVector[0].m_D3dTexture;
		IDirect3DSurface9 *psur = NULL;
		tex->GetSurfaceLevel(0, &psur);
		bool r = SUCCEEDED(m_ifDevice->SetCursorProperties(m_fCurCursorOffsetX, m_fCurCursorOffsetY, psur));
		XPSAFE_RELEASE(psur);
		return r;
	}

	bool DX9Renderer::GetPicOutLine(const void *srcdata, int srcsize, std::vector<CPOINT>& outLinePoint, bool bflag)
	{
		FRECT frct(0.0f, 0.0f, 1.0f, 1.0f);
		return GetPicOutLine(srcdata, srcsize, frct, outLinePoint, bflag);
/*
		if( m_ifDevice == NULL ) return false;
		if( m_bTextureCaps_pow2 ) return false;
		CTextureInfo TextureInfo;
		D3DXIMAGE_INFO pSrcInfo;

		HRESULT hr;

		hr = D3DXCreateTextureFromFileInMemoryEx(
			m_ifDevice, srcdata, srcsize,
			D3DX_DEFAULT_NONPOW2, 
			D3DX_DEFAULT_NONPOW2, 
			1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
			0,
			//(D3DFORMAT)dxfmt,
			D3DFMT_A8R8G8B8,
			D3DPOOL_SYSTEMMEM,
			D3DX_FILTER_NONE,
			D3DX_FILTER_NONE,
			0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);

		if( FAILED(hr) )
			return false;

		int nFailedBorder = 0; //没有找到轮廓点的方向数
		int cx = 4;
		int cy = 4;
		DWORD dwThreColor = 0x7f000000; //alpha值 127
		int nThreValue = 8; //在cx*cy的区域内满足条件的像素值超过nThreValue个就认为是有效目标点
		int nPicWidth = pSrcInfo.Width;
		int nPicHeight = pSrcInfo.Height;
		int nType = nPicWidth>=nPicHeight ? 1:2;
		outLinePoint.clear();
		//左上---左中---左上---下中---右下---右中---右上---上中
		//以上8个方向如果有2个方向没找到轮廓点就退出..把图片矩形框赋值给outLinePoint
		//

		IDirect3DSurface9 * pSurface;
		XPV(TextureInfo.m_D3dTexture->GetSurfaceLevel(0, &pSurface));
		D3DLOCKED_RECT lockedRect;
		XPV(pSurface->LockRect(&lockedRect, NULL, 0));
		
		//先把影子过滤掉, 特征r=g=b
		{
			for(int i=0; i< (int)pSrcInfo.Height; i++)
			{
				for(int j=0; j<(int)pSrcInfo.Width;++j)
				{
					if( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0xff000000) < dwThreColor )
					{
						(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) = 0;
					} 
					else
					{
						BYTE r = ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0x00ff0000) >>16;
						BYTE g = ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0x0000ff00) >>8;
						BYTE b = ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0x000000ff) ;
						if(r==g && g==b)
						{
							(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) = 0;
						}
					}
				}
			}
		}

		//
		
		//左上
		bool bfind = false;
		float fk = (float)(nPicHeight)/nPicWidth;
		if(nType == 1)
		{
			for(int i=0; i<nPicWidth-cx; i++)
			{
				int nValue = 0;
				int j = (int)( (fk*i)>(nPicHeight-1) ? (nPicHeight-1):(fk*i) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int y = (j+n)>(nPicHeight-1) ? (nPicHeight-1):(j+n);

						if( ((((DWORD*)(y*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					i++; //没有找到就跳一个再看看
			}
		}
		else
		{
			for(int j=0; j<nPicHeight-cy; j++)
			{
				int nValue = 0;
				int i = (int)( (1.0f/fk *j)>(nPicWidth-1) ? (nPicWidth-1):(1.0f/fk *j) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int x = (i+m)>(nPicWidth-1) ? (nPicWidth-1):(i+m);
						if( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[x])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					j++;
			}
		}
		if( !bfind ) // 该方向查找失败
			nFailedBorder++;

		//左中
		bfind = false;
		for(int i=0; i<nPicWidth-cx; i++)
		{
			int nValue = 0;
			int j = nPicHeight/2;

			for(int m=0; m<cx; m++)
			{
				for(int n=0; n<cy; n++)
				{
					int y = (j+n)>(nPicHeight-1) ? (nPicHeight-1):(j+n);
					if( ((((DWORD*)(y*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0xff000000) > dwThreColor )
					{
						nValue++;
					}
				}
			}

			if( nValue>nThreValue )
			{
				bfind = true;
				outLinePoint.push_back( CPOINT(i,j) );
				break;
			}
			else
				i++;
		}
		if( !bfind ) // 该方向查找失败
			nFailedBorder++;

		//左下
		bfind = false;
		fk = (-1.0f* nPicHeight)/nPicWidth;
		if(nType == 1)
		{
			for(int i=0; i<nPicWidth-cx; i++)
			{
				int nValue = 0;
				int j = (int)( (fk*i +nPicHeight)>nPicHeight ? nPicHeight:(fk*i +nPicHeight) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int y = (j-n)<0 ? 0:(j-n);
						if( ((((DWORD*)(y*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					i++;
			}
		}
		else
		{
			for(int j=nPicHeight-1; j>=cy; j--)
			{
				int nValue = 0;
				int i = (int)( (1.0f/fk *(j-nPicHeight))>nPicWidth ? nPicWidth:(1.0f/fk *(j-nPicHeight)) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int x = (i+m)>(nPicWidth-1) ? (nPicWidth-1):(i+m);
						if( ((((DWORD*)((j-n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[x])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					j--;
			}
		}
		if( !bfind ) // 该方向查找失败
			nFailedBorder++;

		//下中
		bfind = false;
		for(int j=nPicHeight-1; j>=cy; j--)
		{
			int nValue = 0;
			int i = nPicWidth/2;
		
			for(int m=0; m<cx; m++)
			{
				for(int n=0; n<cy; n++)
				{
					int x = (i+m)>(nPicWidth-1) ? (nPicWidth-1):(i+m);
					if( ((((DWORD*)((j-n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[x])&0xff000000) > dwThreColor )
					{
						nValue++;
					}
				}
			}

			if( nValue>nThreValue )
			{
				bfind = true;
				outLinePoint.push_back( CPOINT(i,j) );
				break;
			}
			else
				j--;
		}
		if( !bfind ) // 该方向查找失败
			nFailedBorder++;

		//右下
		bfind = false;
		fk = (float)(nPicHeight)/nPicWidth;
		if(nType == 1) 
		{
			for(int i=nPicWidth-1; i>=cx; i--)
			{
				int nValue = 0;
				int j = (int)( (fk*i)>nPicHeight ? nPicHeight:(fk*i) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int y = (j-n)<0 ? 0:(j-n);
						if( ((((DWORD*)(y*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i-m])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					i--;
			}
		}
		else
		{
			for(int j=nPicHeight-1; j>=cy; j--)
			{
				int nValue = 0;
				int i = (int)( (1.0f/fk *j)>nPicWidth ? nPicWidth:(1.0f/fk *j) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int x = (i-m)<0 ? 0:(i-m);
						if( ((((DWORD*)((j-n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[x])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					j--;
			}
		}
		if( !bfind ) // 该方向查找失败
			nFailedBorder++;

		//右中
		bfind = false;
		for(int i=nPicWidth-1; i>=cx; i--)
		{
			int nValue = 0;
			int j = nPicHeight/2;

			for(int m=0; m<cx; m++)
			{
				for(int n=0; n<cy; n++)
				{
					int y = (j+n)>(nPicHeight-1) ? (nPicHeight-1):(j+n);
					if( ((((DWORD*)(y*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i-m])&0xff000000) > dwThreColor )
					{
						nValue++;
					}
				}
			}

			if( nValue>nThreValue )
			{
				bfind = true;
				outLinePoint.push_back( CPOINT(i,j) );
				break;
			}
			else
				i--;
		}
		if( !bfind ) // 该方向查找失败
			nFailedBorder++;

		//右上
		bfind = false;
		fk = (-1.0f* nPicHeight)/nPicWidth;
		if(nType == 1)
		{
			for(int i=nPicWidth-1; i>=cx; i--)
			{
				int nValue = 0;
				int j = (int)( (fk*i +nPicHeight)>nPicHeight ? nPicHeight:(fk*i +nPicHeight) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int y = (j+n)>(nPicHeight-1) ? (nPicHeight-1):(j+n);
						if( ((((DWORD*)(y*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i-m])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					i--;
			}
		}
		else
		{
			for(int j=0; j<nPicHeight-cy; j++)
			{
				int nValue = 0;
				int i = (int)( (1.0f/fk *(j-nPicHeight))>nPicWidth ? nPicWidth:(1.0f/fk *(j-nPicHeight)) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int x = (i-m)<0 ? 0:(i-m);
						if( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[x])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					j++;
			}
		}
		if( !bfind ) // 该方向查找失败
			nFailedBorder++;

		//上中
		bfind = false;
		for(int j=0; j<nPicHeight-cy; j++)
		{
			int nValue = 0;
			int i = nPicWidth/2;

			for(int m=0; m<cx; m++)
			{
				for(int n=0; n<cy; n++)
				{
					int x = (i+m)>(nPicWidth-1) ? (nPicWidth-1):(i+m);
					if( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[x])&0xff000000) > dwThreColor )
					{
						nValue++;
					}
				}
			}

			if( nValue>nThreValue )
			{
				bfind = true;
				outLinePoint.push_back( CPOINT(i,j) );
				break;
			}
			else
				j++; //跳一个点，加快搜索速度
		}
		if( !bfind ) // 该方向查找失败
			nFailedBorder++;

		pSurface->UnlockRect();

		//

		XPSAFE_RELEASE(pSurface);
		XPSAFE_RELEASE(TextureInfo.m_D3dTexture);

		if(nFailedBorder>1)
		{//找不到边界的方向超过1个。。就把轮廓置为图片的矩形框
			outLinePoint.clear();
			outLinePoint.push_back( CPOINT(0, 0) );
			outLinePoint.push_back( CPOINT(0, nPicHeight) );
			outLinePoint.push_back( CPOINT(nPicWidth, nPicHeight) );
			outLinePoint.push_back( CPOINT(nPicWidth, 0) );
		}

		return true;
*/
	}

	bool DX9Renderer::GetPicOutLine(const void *srcdata, int srcsize, FRECT frct, std::vector<CPOINT>& outLinePoint, bool bflag)
	{
		if( m_ifDevice == NULL ) return false;
		if( m_bTextureCaps_pow2 ) return false;
		CTextureInfo TextureInfo;
		D3DXIMAGE_INFO pSrcInfo;

		HRESULT hr;

		hr = D3DXCreateTextureFromFileInMemoryEx(
			m_ifDevice, srcdata, srcsize,
			D3DX_DEFAULT_NONPOW2, 
			D3DX_DEFAULT_NONPOW2, 
			1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
			0,
			//(D3DFORMAT)dxfmt,
			D3DFMT_A8R8G8B8,
			D3DPOOL_SYSTEMMEM,
			D3DX_FILTER_NONE,
			D3DX_FILTER_NONE,
			0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);

		if( FAILED(hr) )
			return false;

		int nFailedBorder = 0; //没有找到轮廓点的方向数
		int cx = 4;
		int cy = 4;
		DWORD dwThreColor = 0;//0x5f000000; //alpha值 95
		int nThreValue = 4; //在cx*cy的区域内满足条件的像素值超过nThreValue个就认为是有效目标点

		CRECT picRect; //在大图片的这块区域寻找轮廓
		picRect.left = (int)(frct.left * pSrcInfo.Width);
		picRect.top = (int)(frct.top * pSrcInfo.Height);
		picRect.right = (int)(frct.right * pSrcInfo.Width);
		picRect.bottom = (int)(frct.bottom * pSrcInfo.Height);
		int x0 = picRect.left;
		int y0 = picRect.top;

		int nPicWidth = picRect.Width();
		int nPicHeight = picRect.Height();
		int nType = nPicWidth>=nPicHeight ? 1:2;

		outLinePoint.clear();
		//左上---左中---左上---下中---右下---右中---右上---上中
		//以上8个方向如果有2个方向没找到轮廓点就退出..把图片矩形框赋值给outLinePoint
		//

		IDirect3DSurface9 * pSurface;
		XPV(TextureInfo.m_D3dTexture->GetSurfaceLevel(0, &pSurface));
		D3DLOCKED_RECT lockedRect;
		XPV(pSurface->LockRect(&lockedRect, NULL, 0));
/*
		//先把影子过滤掉, 特征r=g=b
		{
			for(int i=picRect.top; i<picRect.bottom; i++)
			{
				for(int j=picRect.left; j<picRect.right;++j)
				{
					if( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0xff000000) < dwThreColor )
					{
						(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) = 0;
					} 
					else
					{
						BYTE r = (BYTE)( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0x00ff0000) >>16 );
						BYTE g = (BYTE)( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0x0000ff00) >>8 );
						BYTE b = (BYTE)( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0x000000ff) );
						if(r==g && g==b)
						{
							(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) = 0;
						}
					}
				}
			}
		}*/

		//

		//左上
		bool bfind = false;
		float fk = (float)(nPicHeight)/nPicWidth;
		if(nType == 1)
		{
			for(int i=0; i<nPicWidth-cx; i++)
			{
				int nValue = 0;
				int j = (int)( (fk*i)>(nPicHeight-1) ? (nPicHeight-1):(fk*i) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int y = (j+n)>(nPicHeight-1) ? (nPicHeight-1):(j+n);

						int picX = i+m+picRect.left;
						int picY = y+picRect.top;

						if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					i++; //没有找到就跳一个再看看,目的是减少运算
			}
		}
		else
		{
			for(int j=0; j<nPicHeight-cy; j++)
			{
				int nValue = 0;
				int i = (int)( (1.0f/fk *j)>(nPicWidth-1) ? (nPicWidth-1):(1.0f/fk *j) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int x = (i+m)>(nPicWidth-1) ? (nPicWidth-1):(i+m);

						int picX = x+picRect.left;
						int picY = j+n+picRect.top;

						if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					j++;
			}
		}
		if( !bfind ) // 该方向查找失败
		{
			outLinePoint.push_back( CPOINT(0,0) );
			nFailedBorder++;
		}

		//左中
		bfind = false;
		for(int i=0; i<nPicWidth-cx; i++)
		{
			int nValue = 0;
			int j = nPicHeight/2;

			for(int m=0; m<cx; m++)
			{
				for(int n=0; n<cy; n++)
				{
					int y = (j+n)>(nPicHeight-1) ? (nPicHeight-1):(j+n);

					int picX = i+m+picRect.left;
					int picY = y+picRect.top;

					if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
					{
						nValue++;
					}
				}
			}

			if( nValue>nThreValue )
			{
				bfind = true;
				outLinePoint.push_back( CPOINT(i,j) );
				break;
			}
			else
				i++;
		}
		if( !bfind ) // 该方向查找失败
		{
			outLinePoint.push_back( CPOINT(0, nPicHeight/2) );
			nFailedBorder++;
		}

		//左下
		bfind = false;
		fk = (-1.0f* nPicHeight)/nPicWidth;
		if(nType == 1)
		{
			for(int i=0; i<nPicWidth-cx; i++)
			{
				int nValue = 0;
				int j = (int)( (fk*i +nPicHeight)>(nPicHeight-1) ? (nPicHeight-1):(fk*i +nPicHeight) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int y = (j-n)<0 ? 0:(j-n);

						int picX = i+m+picRect.left;
						int picY = y+picRect.top;

						if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					i++;
			}
		}
		else
		{
			for(int j=nPicHeight-1; j>=cy; j--)
			{
				int nValue = 0;
				int i = (int)( (1.0f/fk *(j-nPicHeight))>(nPicWidth-1) ? (nPicWidth-1):(1.0f/fk *(j-nPicHeight)) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int x = (i+m)>(nPicWidth-1) ? (nPicWidth-1):(i+m);

						int picX = x+picRect.left;
						int picY = j-n+picRect.top;

						if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					j--;
			}
		}
		if( !bfind ) // 该方向查找失败
		{
			outLinePoint.push_back( CPOINT(0, nPicHeight-1) );
			nFailedBorder++;
		}

		//下中
		bfind = false;
		for(int j=nPicHeight-1; j>=cy; j--)
		{
			int nValue = 0;
			int i = nPicWidth/2;

			for(int m=0; m<cx; m++)
			{
				for(int n=0; n<cy; n++)
				{
					int x = (i+m)>(nPicWidth-1) ? (nPicWidth-1):(i+m);

					int picX = x+picRect.left;
					int picY = j-n+picRect.top;

					if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
					{
						nValue++;
					}
				}
			}

			if( nValue>nThreValue )
			{
				bfind = true;
				outLinePoint.push_back( CPOINT(i,j) );
				break;
			}
			else
				j--;
		}
		if( !bfind ) // 该方向查找失败
		{
			outLinePoint.push_back( CPOINT(nPicWidth/2, nPicHeight-1) );
			nFailedBorder++;
		}

		//右下
		bfind = false;
		fk = (float)(nPicHeight)/nPicWidth;
		if(nType == 1) 
		{
			for(int i=nPicWidth-1; i>=cx; i--)
			{
				int nValue = 0;
				int j = (int)( (fk*i)>(nPicHeight-1) ? (nPicHeight-1):(fk*i) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int y = (j-n)<0 ? 0:(j-n);

						int picX = i-m+picRect.left;
						int picY = y+picRect.top;

						if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					i--;
			}
		}
		else
		{
			for(int j=nPicHeight-1; j>=cy; j--)
			{
				int nValue = 0;
				int i = (int)( (1.0f/fk *j)>(nPicWidth-1) ? (nPicWidth-1):(1.0f/fk *j) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int x = (i-m)<0 ? 0:(i-m);

						int picX = x+picRect.left;
						int picY = j-n+picRect.top;

						if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					j--;
			}
		}
		if( !bfind ) // 该方向查找失败
		{
			outLinePoint.push_back( CPOINT(nPicWidth-1, nPicHeight-1) );
			nFailedBorder++;
		}

		//右中
		bfind = false;
		for(int i=nPicWidth-1; i>=cx; i--)
		{
			int nValue = 0;
			int j = nPicHeight/2;

			for(int m=0; m<cx; m++)
			{
				for(int n=0; n<cy; n++)
				{
					int y = (j+n)>(nPicHeight-1) ? (nPicHeight-1):(j+n);

					int picX = i-m+picRect.left;
					int picY = y+picRect.top;

					if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
					{
						nValue++;
					}
				}
			}

			if( nValue>nThreValue )
			{
				bfind = true;
				outLinePoint.push_back( CPOINT(i,j) );
				break;
			}
			else
				i--;
		}
		if( !bfind ) // 该方向查找失败
		{
			outLinePoint.push_back( CPOINT(nPicWidth-1, nPicHeight/2) );
			nFailedBorder++;
		}

		//右上
		bfind = false;
		fk = (-1.0f* nPicHeight)/nPicWidth;
		if(nType == 1)
		{
			for(int i=nPicWidth-1; i>=cx; i--)
			{
				int nValue = 0;
				int j = (int)( (fk*i +nPicHeight)>(nPicHeight-1) ? (nPicHeight-1):(fk*i +nPicHeight) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int y = (j+n)>(nPicHeight-1) ? (nPicHeight-1):(j+n);

						int picX = i-m+picRect.left;
						int picY = y+picRect.top;

						if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					i--;
			}
		}
		else
		{
			for(int j=0; j<nPicHeight-cy; j++)
			{
				int nValue = 0;
				int i = (int)( (1.0f/fk *(j-nPicHeight))>(nPicWidth-1) ? (nPicWidth-1):(1.0f/fk *(j-nPicHeight)) );

				for(int m=0; m<cx; m++)
				{
					for(int n=0; n<cy; n++)
					{
						int x = (i-m)<0 ? 0:(i-m);

						int picX = x+picRect.left;
						int picY = j+n+picRect.top;

						if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
						{
							nValue++;
						}
					}
				}

				if( nValue>nThreValue )
				{
					bfind = true;
					outLinePoint.push_back( CPOINT(i,j) );
					break;
				}
				else
					j++;
			}
		}
		if( !bfind ) // 该方向查找失败
		{
			outLinePoint.push_back( CPOINT(nPicWidth-1, 0) );
			nFailedBorder++;
		}

		//上中
		bfind = false;
		for(int j=0; j<nPicHeight-cy; j++)
		{
			int nValue = 0;
			int i = nPicWidth/2;

			for(int m=0; m<cx; m++)
			{
				for(int n=0; n<cy; n++)
				{
					int x = (i+m)>(nPicWidth-1) ? (nPicWidth-1):(i+m);

					int picX = x+picRect.left;
					int picY = j+n+picRect.top;

					if( ((((DWORD*)(picY*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[picX])&0xff000000) > dwThreColor )
					{
						nValue++;
					}
				}
			}

			if( nValue>nThreValue )
			{
				bfind = true;
				outLinePoint.push_back( CPOINT(i,j) );
				break;
			}
			else
				j++; //跳一个点，加快搜索速度
		}
		if( !bfind ) // 该方向查找失败
		{
			outLinePoint.push_back( CPOINT(nPicWidth/2, 0) );
			nFailedBorder++;
		}

		pSurface->UnlockRect();

		//

		XPSAFE_RELEASE(pSurface);
		XPSAFE_RELEASE(TextureInfo.m_D3dTexture);

		if(nFailedBorder>5)
		{//找不到边界的方向超过1个。。就把轮廓置为图片的矩形框
			outLinePoint.clear();
			outLinePoint.push_back( CPOINT(0, 0) );
			outLinePoint.push_back( CPOINT(0, nPicHeight-1) );
			outLinePoint.push_back( CPOINT(nPicWidth-1, nPicHeight-1) );
			outLinePoint.push_back( CPOINT(nPicWidth-1, 0) );
		}

		return true;
	}

	bool DX9Renderer::ClearAlpha(PictureHandle handle)
	{
		if( m_ifDevice == NULL ) return false;
		if( m_bTextureCaps_pow2 ) return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if ( m_mapPictures.end() == it) return false;
		PictureInfo &info = it->second.m_info;
		IDirect3DSurface9 * pSurface;
		HRESULT hr;
		XPV(it->second.m_CTextureInfoVector.front().m_D3dTexture->GetSurfaceLevel(0, &pSurface));

		{
			D3DLOCKED_RECT lockedRect;

			XPV(pSurface->LockRect(&lockedRect, NULL, 0));
			for(int i=0; i< (int)info.m_nPicHeight; i++)
			{
				for(int j=0; j<(int)info.m_nPicWidth;++j)
				{
					if( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0xff000000) == 0 )
					{
						(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) = 0;
					} 
				}
			}
			XPV(pSurface->UnlockRect());
		}
		return true;
	}

	//-Mended 2015
	void DX9Renderer::DrawPicClip(const void *srcdata, int srcsize, XBuffer &destBuffer,
		XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum)
	{
		if( m_ifDevice == NULL ) return;
		if( m_bTextureCaps_pow2 ) return;
		CTextureInfo TextureInfo;
		D3DXIMAGE_INFO pSrcInfo;

		HRESULT hr;

		hr = D3DXCreateTextureFromFileInMemoryEx(
			m_ifDevice, srcdata, srcsize,
			D3DX_DEFAULT_NONPOW2, 
			D3DX_DEFAULT_NONPOW2, 
			1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
			0,
			//(D3DFORMAT)dxfmt,
			D3DFMT_A8R8G8B8,
			D3DPOOL_SYSTEMMEM,
			D3DX_FILTER_NONE,
			D3DX_FILTER_NONE,
			0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);

		if( FAILED(hr) ) return;

		IDirect3DSurface9 * pSurface;
		XPV(TextureInfo.m_D3dTexture->GetSurfaceLevel(0, &pSurface));

		// todo 尝试打扫alpha为0的rgb值
		{
			D3DLOCKED_RECT lockedRect;

			XPV(pSurface->LockRect(&lockedRect, NULL, 0));
			
			for(int i=0; i< (int)pSrcInfo.Height; i++)
			{
				for(int j=0; j<(int)pSrcInfo.Width;++j)
				{
					if( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0xff000000) == 0 )
					{
						(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) = 0;
					} 
					else if (setNonzeroAlpha2One)
					{
						DWORD dwColor = ((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j];
						if( (dwColor&0xff000000)>0x00000000 ) //alpha值大于0的就置为255
						{
							(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) |= 0xff000000;
						}
					}
					else
					{
					}
				}
			}
			XPV(pSurface->UnlockRect());
		}
		
		LPD3DXBUFFER d3dbuffer=NULL;

		if( filefmt == XPIFF_PNG && texfmt == XPTEXFMT_A8R8G8B8 )
		{
			D3DLOCKED_RECT lockedRect;
			XPV(pSurface->LockRect(&lockedRect, &rect, 0));
			XBuffer outbuf;
			if( Write32BitPNGWithPitch(outbuf, (unsigned char*)lockedRect.pBits, true, rect.right-rect.left, rect.bottom-rect.top, lockedRect.Pitch))
			{
				destBuffer.resize(outbuf.size());
				memcpy(destBuffer.normalbegin(), outbuf.constbegin(), destBuffer.size());
			}
			XPV(pSurface->UnlockRect());
		}
		else
		{
			if( SUCCEEDED(D3DXSaveSurfaceToFileInMemory( &d3dbuffer,(D3DXIMAGE_FILEFORMAT)filefmt,pSurface,NULL,&rect)))
			{
				destBuffer.resize(d3dbuffer->GetBufferSize());
				memcpy(destBuffer.normalbegin(), d3dbuffer->GetBufferPointer(), destBuffer.size());
			}
		}

		XPSAFE_RELEASE(pSurface);
		XPSAFE_RELEASE(d3dbuffer);
		XPSAFE_RELEASE(TextureInfo.m_D3dTexture);
		
		return;
	}
    //-

    bool DX9Renderer::SaveTextureToDXSurface(const void *srcdata, int srcsize, XBuffer &destBuffer,
		XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum,int det)
	{
		if( m_ifDevice == NULL ) return false;
		if( m_bTextureCaps_pow2 ) return false;
		CTextureInfo TextureInfo;
		D3DXIMAGE_INFO pSrcInfo;

		HRESULT hr;

		hr = D3DXCreateTextureFromFileInMemoryEx(
			m_ifDevice, srcdata, srcsize,
			D3DX_DEFAULT_NONPOW2, 
			D3DX_DEFAULT_NONPOW2, 
			1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
			0,
			//(D3DFORMAT)dxfmt,
			D3DFMT_A8R8G8B8,
			D3DPOOL_SYSTEMMEM,
			D3DX_FILTER_NONE,
			D3DX_FILTER_NONE,
			0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);



		if( FAILED(hr) ) return false;

		IDirect3DSurface9 * pSurface;
		XPV(TextureInfo.m_D3dTexture->GetSurfaceLevel(0, &pSurface));

		// todo 尝试打扫alpha为0的rgb值
		{
			D3DLOCKED_RECT lockedRect;

			XPV(pSurface->LockRect(&lockedRect, NULL, 0));
			
			for(int i=0; i< (int)pSrcInfo.Height; i++)
			{
				for(int j=0; j<(int)pSrcInfo.Width;++j)
				{
					if( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0xff000000) == 0 )
					{
						(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) = 0;
					} 
					else if (setNonzeroAlpha2One)
					{
						DWORD dwColor = ((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j];
						if( (dwColor&0xff000000)>0x00000000 ) //alpha值大于0的就置为255
						{
							(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) |= 0xff000000;
						}
					}
					else
					{
					}
				}
			}
			XPV(pSurface->UnlockRect());
		}
		
		LPD3DXBUFFER d3dbuffer=NULL;
		//XPV(D3DXCreateBuffer(dstsize, &d3dbuffer))
		
		//bool r = SUCCEEDED(D3DXSaveTextureToFileInMemory( &d3dbuffer, D3DXIFF_DDS, TextureInfo.m_D3dTexture,NULL));
		//
		rect.top	= 0;
		rect.left	= 0;
		rect.bottom = pSrcInfo.Height;
		rect.right	= pSrcInfo.Width;

		//
		if( clipmode==1 )
		{
			bool find = false;
			D3DLOCKED_RECT lockedRect;
			XPV(pSurface->LockRect(&lockedRect, NULL, 0));

			int cx = 1;
			int cy = 1;
			DWORD dwThreColor = 0x0A000000; //alpha值 10
			
			BYTE threAlpha = 0;

			int nThreValue = 0; //在cx*cy的区域内满足条件的像素值超过nThreValue个就认为是有效目标点

			for(int i=0; i<(int)pSrcInfo.Height-cy; ++i)
			{
				for(int j=0; j<(int)pSrcInfo.Width-cx; ++j)
				{
					int nValue = 0;
					for(int m=0; m<cy; m++)
					{
						for(int n=0; n<cx; n++)
						{
							BYTE a = (BYTE)( ((((DWORD*)((i+m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0xff000000) >>24 );
						//	BYTE r = (BYTE)( ((((DWORD*)((i+m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x00ff0000) >>16 );
						//	BYTE g = (BYTE)( ((((DWORD*)((i+m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x0000ff00) >>8 );
						//	BYTE b = (BYTE)( ((((DWORD*)((i+m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x000000ff) );

							if( a>threAlpha ) // && (r+g+b)>10 
							{
								nValue++;
							}
						}
					}
					if( nValue>nThreValue )
					{
						find = true;
						rect.top = i;
						break;
					}
				}
				if( find ) break;
			}

			if( !find ) // 全0
			{
				rect.left = 0;
				rect.top = 0;
				rect.right = 1;
				rect.bottom = 1;
			}
			else
			{
				find = false;
				for(; rect.bottom >rect.top +cy;--rect.bottom)
				{
					for(int j=0; j<(int)pSrcInfo.Width -cx;++j)
					{
						int nValue = 0;
						for(int m=0; m<cy; m++)
						{
							for(int n=0; n<cx; n++)
							{
								BYTE a = (BYTE)( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0xff000000) >>24 );
							//	BYTE r = (BYTE)( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x00ff0000) >>16 );
							//	BYTE g = (BYTE)( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x0000ff00) >>8 );
							//	BYTE b = (BYTE)( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x000000ff) );

								if( a>threAlpha ) // && (r+g+b)>10 
								//if( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0xff000000) > dwThreColor )
								{
									nValue++;
								}
							}
						}

						if( nValue>nThreValue )
						{
							find = true;
							break;
						}
					}
					if( find ) break;
				}
				// 计算左右
				for(int i=0; i<(int)pSrcInfo.Width -cx; ++i)
				{
					find = false;
					for(int j=rect.top; j<(int)rect.bottom -cy;++j)
					{
						int nValue = 0;
						for(int m=0; m<cx; m++)
						{
							for(int n=0; n<cy; n++)
							{
								BYTE a = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0xff000000) >>24 );
							//	BYTE r = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0x00ff0000) >>16 );
							//	BYTE g = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0x0000ff00) >>8 );
							//	BYTE b = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0x000000ff) );

								if( a>threAlpha ) // && (r+g+b)>10 
							//	if( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0xff000000) > dwThreColor )
								{
									nValue++;
								}
							}
						}

						if( nValue>nThreValue )
						{
							find = true;
							rect.left = i;
							break;
						}
					}
					if( find ) break;
				}
				find = false;
				for(; rect.right>rect.left +cx; --rect.right)
				{
					for(int j=rect.top; j<(int)rect.bottom -cy;++j)
					{
						int nValue = 0;
						for(int m=0; m<cx; m++)
						{
							for(int n=0; n<cy; n++)
							{
								BYTE a = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0xff000000) >>24 );
							//	BYTE r = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0x00ff0000) >>16 );
							//	BYTE g = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0x0000ff00) >>8 );
							//	BYTE b = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0x000000ff) );

								if( a>threAlpha ) // && (r+g+b)>10 
							//	if( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0xff000000) > dwThreColor )
								{
									nValue++;
								}
							}
						}

						if( nValue>nThreValue )
						{
							find = true;
							break;
						}
					}
					if( find ) break;
				}
			}
			pSurface->UnlockRect();
		}
		//
		
		if( texfmt != XPTEXFMT_A8R8G8B8 )
		{
			// todo D3DXLoadSurfaceFromSurface?
			XPSAFE_RELEASE(d3dbuffer);
		
			XPV(D3DXSaveTextureToFileInMemory( &d3dbuffer, (D3DXIMAGE_FILEFORMAT)filefmt, TextureInfo.m_D3dTexture,NULL));
			XPSAFE_RELEASE(pSurface);
			XPSAFE_RELEASE(TextureInfo.m_D3dTexture);			
			
			//
			D3DXCreateTextureFromFileInMemoryEx(
			m_ifDevice, d3dbuffer->GetBufferPointer(), d3dbuffer->GetBufferSize(),
			D3DX_DEFAULT_NONPOW2, 
			D3DX_DEFAULT_NONPOW2, 
			1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
			0,
			(D3DFORMAT)texfmt,
			D3DPOOL_SYSTEMMEM,
			D3DX_FILTER_NONE,
			D3DX_FILTER_NONE,
			0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);
			XPV(TextureInfo.m_D3dTexture->GetSurfaceLevel(0,&pSurface));
			XPSAFE_RELEASE(d3dbuffer);			
		}
		//

		int iLeftValue  = rect.left % det;
		if( rect.left % det )
		{
			rect.left	 = rect.left/det * det;
		}
		
		int iTopValue	= rect.top % det;
		if( rect.top % det )
		{		
			rect.top	 = rect.top/det * det;
		}

		int iRightValue  = rect.right % det;
		if( rect.right % det ) 
		{
			rect.right	 = rect.right/det*det+det;  
		}
		/*todo*/
		if( rect.right && rect.right > (int)pSrcInfo.Width )
		{
			rect.right  -= det;
		}

		int iBottomValue  = rect.bottom % det;
		if( rect.bottom % det ) 
		{
			rect.bottom  = rect.bottom/det*det+det;
		}
		/*todo*/

		bool r = false;
		
		if( filefmt == XPIFF_PNG && texfmt == XPTEXFMT_A8R8G8B8 )
		{
			D3DLOCKED_RECT lockedRect;
			XPV(pSurface->LockRect(&lockedRect, &rect, 0));
			XBuffer outbuf;
			if( Write32BitPNGWithPitch(outbuf, (unsigned char*)lockedRect.pBits, true, rect.right-rect.left, rect.bottom-rect.top, lockedRect.Pitch))
			{
				destBuffer.resize(outbuf.size());
				memcpy(destBuffer.normalbegin(), outbuf.constbegin(), destBuffer.size());
				r = true;
			}
			XPV(pSurface->UnlockRect());
		}
		else
		{
			if( SUCCEEDED(D3DXSaveSurfaceToFileInMemory( &d3dbuffer,(D3DXIMAGE_FILEFORMAT)filefmt,pSurface,NULL,&rect)))
			{
				destBuffer.resize(d3dbuffer->GetBufferSize());
				memcpy(destBuffer.normalbegin(), d3dbuffer->GetBufferPointer(), destBuffer.size());
				r = true;
			}
		}

		XPSAFE_RELEASE(pSurface);
		XPSAFE_RELEASE(d3dbuffer);
		XPSAFE_RELEASE(TextureInfo.m_D3dTexture);
	
		//
		return r;
	}


    //-Mended
	CRECT DX9Renderer::CalPicRect(const void *srcdata, int srcsize, XBuffer &destBuffer,
		XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, CRECT &rect, int clipmode, bool setNonzeroAlpha2One,int iRectNum)
	{
		if( m_ifDevice == NULL )  return rect;
		if( m_bTextureCaps_pow2 ) return rect;

		CTextureInfo TextureInfo;
		D3DXIMAGE_INFO pSrcInfo;

		HRESULT hr;

		hr = D3DXCreateTextureFromFileInMemoryEx(
			m_ifDevice, srcdata, srcsize,
			D3DX_DEFAULT_NONPOW2, 
			D3DX_DEFAULT_NONPOW2, 
			1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
			0,
			//(D3DFORMAT)dxfmt,
			D3DFMT_A8R8G8B8,
			D3DPOOL_SYSTEMMEM,
			D3DX_FILTER_NONE,
			D3DX_FILTER_NONE,
			0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);

		if( FAILED(hr) ) return rect;

		IDirect3DSurface9 * pSurface;
		XPV(TextureInfo.m_D3dTexture->GetSurfaceLevel(0, &pSurface));

		// todo 尝试打扫alpha为0的rgb值
		{
			D3DLOCKED_RECT lockedRect;

			XPV(pSurface->LockRect(&lockedRect, NULL, 0));
			
			for(int i=0; i< (int)pSrcInfo.Height; i++)
			{
				for(int j=0; j<(int)pSrcInfo.Width;++j)
				{
					if( ((((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j])&0xff000000) == 0 )
					{
						(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) = 0;
					} 
					else if (setNonzeroAlpha2One)
					{
						DWORD dwColor = ((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j];
						if( (dwColor&0xff000000)>0x00000000 ) //alpha值大于0的就置为255
						{
							(((DWORD*)(i*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j]) |= 0xff000000;
						}
					}
					else
					{
					}
				}
			}
			XPV(pSurface->UnlockRect());
		}
		
		LPD3DXBUFFER d3dbuffer=NULL;
		//XPV(D3DXCreateBuffer(dstsize, &d3dbuffer))
		
		//bool r = SUCCEEDED(D3DXSaveTextureToFileInMemory( &d3dbuffer, D3DXIFF_DDS, TextureInfo.m_D3dTexture,NULL));
		//
		rect.top	= 0;
		rect.left	= 0;
		rect.bottom = pSrcInfo.Height;
		rect.right	= pSrcInfo.Width;

		//
		if( clipmode==1 )
		{
			bool find = false;
			D3DLOCKED_RECT lockedRect;
			XPV(pSurface->LockRect(&lockedRect, NULL, 0));

			int cx = 4;
			int cy = 4;
			DWORD dwThreColor = 0x0A000000; //alpha值 10
			
			BYTE threAlpha = 10;

			int nThreValue = 6; //在cx*cy的区域内满足条件的像素值超过nThreValue个就认为是有效目标点

			for(int i=0; i<(int)pSrcInfo.Height-cy; ++i)
			{
				for(int j=0; j<(int)pSrcInfo.Width-cx; ++j)
				{
					int nValue = 0;
					for(int m=0; m<cy; m++)
					{
						for(int n=0; n<cx; n++)
						{
							BYTE a = (BYTE)( ((((DWORD*)((i+m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0xff000000) >>24 );
						//	BYTE r = (BYTE)( ((((DWORD*)((i+m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x00ff0000) >>16 );
						//	BYTE g = (BYTE)( ((((DWORD*)((i+m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x0000ff00) >>8 );
						//	BYTE b = (BYTE)( ((((DWORD*)((i+m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x000000ff) );

							if( a>threAlpha ) // && (r+g+b)>10 
							{
								nValue++;
							}
						}
					}
					if( nValue>nThreValue )
					{
						find = true;
						rect.top = i;
						break;
					}
				}
				if( find ) break;
			}

			if( !find ) // 全0
			{
				rect.left = 0;
				rect.top = 0;
				rect.right = 1;
				rect.bottom = 1;
			}
			else
			{
				find = false;
				for(; rect.bottom >rect.top +cy;--rect.bottom)
				{
					for(int j=0; j<(int)pSrcInfo.Width -cx;++j)
					{
						int nValue = 0;
						for(int m=0; m<cy; m++)
						{
							for(int n=0; n<cx; n++)
							{
								BYTE a = (BYTE)( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0xff000000) >>24 );
							//	BYTE r = (BYTE)( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x00ff0000) >>16 );
							//	BYTE g = (BYTE)( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x0000ff00) >>8 );
							//	BYTE b = (BYTE)( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0x000000ff) );

								if( a>threAlpha ) // && (r+g+b)>10 
								//if( ((((DWORD*)((rect.bottom-1-m)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[j+n])&0xff000000) > dwThreColor )
								{
									nValue++;
								}
							}
						}

						if( nValue>nThreValue )
						{
							find = true;
							break;
						}
					}
					if( find ) break;
				}
				// 计算左右
				for(int i=0; i<(int)pSrcInfo.Width -cx; ++i)
				{
					find = false;
					for(int j=rect.top; j<(int)rect.bottom -cy;++j)
					{
						int nValue = 0;
						for(int m=0; m<cx; m++)
						{
							for(int n=0; n<cy; n++)
							{
								BYTE a = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0xff000000) >>24 );
							//	BYTE r = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0x00ff0000) >>16 );
							//	BYTE g = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0x0000ff00) >>8 );
							//	BYTE b = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0x000000ff) );

								if( a>threAlpha ) // && (r+g+b)>10 
							//	if( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[i+m])&0xff000000) > dwThreColor )
								{
									nValue++;
								}
							}
						}

						if( nValue>nThreValue )
						{
							find = true;
							rect.left = i;
							break;
						}
					}
					if( find ) break;
				}
				find = false;
				for(; rect.right>rect.left +cx; --rect.right)
				{
					for(int j=rect.top; j<(int)rect.bottom -cy;++j)
					{
						int nValue = 0;
						for(int m=0; m<cx; m++)
						{
							for(int n=0; n<cy; n++)
							{
								BYTE a = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0xff000000) >>24 );
							//	BYTE r = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0x00ff0000) >>16 );
							//	BYTE g = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0x0000ff00) >>8 );
							//	BYTE b = (BYTE)( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0x000000ff) );

								if( a>threAlpha ) // && (r+g+b)>10 
							//	if( ((((DWORD*)((j+n)*lockedRect.Pitch+(BYTE*)lockedRect.pBits))[rect.right-1-m])&0xff000000) > dwThreColor )
								{
									nValue++;
								}
							}
						}

						if( nValue>nThreValue )
						{
							find = true;
							break;
						}
					}
					if( find ) break;
				}
			}
			pSurface->UnlockRect();
		}
		//
		
		if( texfmt != XPTEXFMT_A8R8G8B8 )
		{
			// todo D3DXLoadSurfaceFromSurface?
			XPSAFE_RELEASE(d3dbuffer);
		
			XPV(D3DXSaveTextureToFileInMemory( &d3dbuffer, (D3DXIMAGE_FILEFORMAT)filefmt, TextureInfo.m_D3dTexture,NULL));
			XPSAFE_RELEASE(pSurface);
			XPSAFE_RELEASE(TextureInfo.m_D3dTexture);			
			
			//
			D3DXCreateTextureFromFileInMemoryEx(
			m_ifDevice, d3dbuffer->GetBufferPointer(), d3dbuffer->GetBufferSize(),
			D3DX_DEFAULT_NONPOW2, 
			D3DX_DEFAULT_NONPOW2, 
			1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
			0,
			(D3DFORMAT)texfmt,
			D3DPOOL_SYSTEMMEM,
			D3DX_FILTER_NONE,
			D3DX_FILTER_NONE,
			0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);
			XPV(TextureInfo.m_D3dTexture->GetSurfaceLevel(0,&pSurface));
			XPSAFE_RELEASE(d3dbuffer);			
		}
		//

		int iLeftValue  = rect.left % 4;
		if( rect.left % 4 )
		{
			rect.left	 = rect.left/4 * 4;
		}
		
		int iTopValue	= rect.top % 4;
		if( rect.top % 4 )
		{		
			rect.top	 = rect.top/4 * 4;
		}

		int iRightValue  = rect.right % 4;
		if( rect.right % 4 ) 
		{
			rect.right	 = rect.right/4*4+4;  
		}
		/*todo*/
		if( rect.right && rect.right > (int)pSrcInfo.Width )
		{
			rect.right  -= 4;
		}

		int iBottomValue  = rect.bottom % 4;
		if( rect.bottom % 4 ) 
		{
			rect.bottom  = rect.bottom/4*4+4;
		}
		/*todo*/
		
		XPSAFE_RELEASE(pSurface);
		XPSAFE_RELEASE(d3dbuffer);
		XPSAFE_RELEASE(TextureInfo.m_D3dTexture);

		return rect;
		/*vCRectSet.push_back(rect);
		if(iIndexOfRect != (iRectNum-1))
		{
			iIndexOfRect++;
	
		}else 
		{
		
			CRECT cMaxRect;
			CRectSet::iterator it = vCRectSet.begin();
			CRectSet::iterator ie = vCRectSet.end();
            int iRectCounter = 0;

			/*for(;it!=ie;++it)
			{
				cMaxRect = vCRectSet[iRectCounter];
				LONG tmpRectArea_top	=	it->top;
				LONG tmpRectArea_bottom =	it->bottom;
				LONG tmpRectArea_left	=	it->left;
				LONG tmpRectArea_right	=	it->right;

				if(cMaxRect.top <= tmpRectArea_top)
				{
					cMaxRect.top	= tmpRectArea_top;
				}

				if(cMaxRect.bottom <= tmpRectArea_bottom)
				{
					cMaxRect.bottom = tmpRectArea_bottom;
				}

				if(cMaxRect.left <= tmpRectArea_left)
				{
					cMaxRect.left	= tmpRectArea_left;
				}

				if(cMaxRect.right <= tmpRectArea_right)
				{
					cMaxRect.right	= tmpRectArea_right;
				}

				++iRectCounter;
			}

			rect = cMaxRect;
		}*/

	}

	bool DX9Renderer::SaveTextureToFile(PictureHandle handle, XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, XBuffer &outbuffer)
	{
		if( handle == INVALID_PICTURE_HANDLE ) 
			return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;
		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;
		LPD3DXBUFFER d3dbuffer = NULL;
		//pTexture = it->second.m_CTextureInfoVector[0].m_D3dTexture;
		HRESULT hr;
		if (it->second.m_info.m_texfmt == texfmt)
		{
			hr = D3DXSaveTextureToFileInMemory(&d3dbuffer, (D3DXIMAGE_FILEFORMAT)filefmt, 
				it->second.m_CTextureInfoVector[0].m_D3dTexture, NULL);
			if (FAILED(hr))
				return false;
		} else {
			LPDIRECT3DTEXTURE9 ifTex = NULL;
			if( FAILED(D3DXCreateTexture(
				m_ifDevice,
				it->second.m_info.m_nPicWidth, it->second.m_info.m_nPicHeight, 
				1, // 指定共生成几级mipmap 0 create a complete mipmap chain 1...
				0,
				(D3DFORMAT)texfmt,
				D3DPOOL_SYSTEMMEM, &ifTex)))
				return false;

			IDirect3DSurface9 * pDestSurface = NULL, *pSrcSurface = NULL;
			XPV(ifTex->GetSurfaceLevel(0, &pDestSurface));
			XPV(it->second.m_CTextureInfoVector[0].m_D3dTexture->GetSurfaceLevel(0, &pSrcSurface));
			bool r = false;
			hr = D3DXLoadSurfaceFromSurface(pDestSurface, NULL, NULL, pSrcSurface, NULL, NULL, D3DX_DEFAULT, 0);
			XPSAFE_RELEASE(pSrcSurface);
			if( SUCCEEDED(hr))
			{
				hr = D3DXSaveSurfaceToFileInMemory(&d3dbuffer, (D3DXIMAGE_FILEFORMAT)filefmt,
					pDestSurface, NULL, NULL);
			}
			XPSAFE_RELEASE(pDestSurface);
			XPSAFE_RELEASE(ifTex);
			if (FAILED(hr))
				return false;
		}
		if( SUCCEEDED(hr))
		{
			outbuffer.resize(d3dbuffer->GetBufferSize());
			memcpy(outbuffer.normalbegin(), d3dbuffer->GetBufferPointer(), outbuffer.size());
			XPSAFE_RELEASE(d3dbuffer);
			return true;
		}
		XPSAFE_RELEASE(d3dbuffer);
		return false;
	}

	//frct 是handle的其中某一块，纹理坐标。(0.0f, 0.0f, 1.0f, 1.0f)表示整块纹理
/*	bool DX9Renderer::SaveTextureToXBuffer(const void *srcdata, int srcsize, Nuclear::FRECT frct, XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, XBuffer& outbuffer)
	{
		if( m_ifDevice == NULL ) return false;
		if( m_bTextureCaps_pow2 ) return false;
		if(srcdata == NULL) return false;
	
		HRESULT hr;	

		PictureInfo picInfo;
		PictureHandle handle = LoadPictureFromMem(srcdata, srcsize, &picInfo, XPTEXFMT_A8R8G8B8, XPPOOL_SYSTEMMEM);

		CRECT Rct;
		Rct.left = (int)(frct.left * picInfo.m_nPicWidth);
		Rct.top = (int)(frct.top * picInfo.m_nPicHeight);
		Rct.right = (int)(frct.right * picInfo.m_nPicWidth);
		Rct.bottom = (int)(frct.bottom * picInfo.m_nPicWidth);

		XBuffer dataBuf;
		dataBuf.resize(Rct.Width() * Rct.Height() * 4);
		bool bres = GetPictureData(handle, dataBuf.normalbegin(), dataBuf.size(), &Rct); 

		PictureHandle handle2 = LoadPictureFromMem(dataBuf.constbegin(), dataBuf.size(), &picInfo, texfmt, XPPOOL_SYSTEMMEM);

		bres = SaveTextureToFile(handle2, texfmt, filefmt, outbuffer);

		return true;
	}
*/

	bool DX9Renderer::SaveTextureToFile(PictureHandle handle, XPIMAGE_FILEFORMAT filefmt, const std::wstring &path)
	{
		if( handle == INVALID_PICTURE_HANDLE ) 
			return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;

		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;

		HRESULT hr;
		hr = D3DXSaveTextureToFile(path.c_str(), (D3DXIMAGE_FILEFORMAT)filefmt, it->second.m_CTextureInfoVector[0].m_D3dTexture, NULL);
		if( FAILED(hr) )
			return false;
		return true;
	}

	void DX9Renderer::DumpAllPicture(const std::wstring &path)
	{
		PictureMap::iterator it = m_mapPictures.begin(), ie = m_mapPictures.end();
		std::vector<CTextureInfo>::iterator texIt, texIe;
		int i;
		wchar_t filename[MAX_PATH];
		std::wstring f = path + L"%d_%d.dds";
		const wchar_t *format = f.c_str();
	//	IDirect3DSurface9* pSurfaceLevel = NULL;
	//	HRESULT hr;
		for (;it!=ie;++it)
		{
			texIt = it->second.m_CTextureInfoVector.begin();
			texIe = it->second.m_CTextureInfoVector.end();
			i = 0;
			for (;texIt!=texIe;++i,++texIt)
			{
				swprintf_s(filename, MAX_PATH, format, it->first, i);
	//			XPV(texIt->m_D3dTexture->GetSurfaceLevel(0, &pSurfaceLevel));
	//			D3DXSaveSurfaceToFile(filename, D3DXIFF_DDS, pSurfaceLevel, NULL, NULL);
	//			XPSAFE_RELEASE(pSurfaceLevel);

				D3DXSaveTextureToFile(filename, D3DXIFF_DDS, texIt->m_D3dTexture, NULL);
			}
		}
	}

	bool DX9Renderer::SplitTexture(const void *srcdata, int srcsize,XPTEXTURE_FORMAT texfmt, XPIMAGE_FILEFORMAT filefmt, 
			CRECT &rect, std::vector<std::pair<CRECT,XBuffer> > &dstpics, int &row, int &col, int width, int height)
	{
		if( m_ifDevice == NULL ) return false;
		if( m_bTextureCaps_pow2 ) return false;
		CTextureInfo TextureInfo;
		D3DXIMAGE_INFO pSrcInfo;

		HRESULT hr;

		hr = D3DXCreateTextureFromFileInMemoryEx(
			m_ifDevice, srcdata, srcsize,
			D3DX_DEFAULT_NONPOW2, 
			D3DX_DEFAULT_NONPOW2, 
			1, //zero or D3DX_DEFAULT, a complete mipmap chain is created
			0,
			(D3DFORMAT)texfmt,
			D3DPOOL_SYSTEMMEM,
			D3DX_FILTER_NONE,
			D3DX_FILTER_NONE,
			0, &pSrcInfo, NULL, &TextureInfo.m_D3dTexture);

		if( FAILED(hr) ) return false;
		
		
		IDirect3DSurface9 * pSurface;
		XPV(TextureInfo.m_D3dTexture->GetSurfaceLevel(0,&pSurface));
		//
		rect.left = 0;
		rect.top = 0;
		rect.right = pSrcInfo.Width;
		rect.bottom = pSrcInfo.Height;

		rect.right -= (rect.right - rect.left )%4;
		rect.bottom -= (rect.bottom - rect.top )%4;
		//
		row = 0; //返回行数
		col = 0; //返回列数
		for(int i=0; i < rect.bottom; i += height)
		{
			row++;
			col = 0;
			for(int j=0; j< rect.right; j+= width)
			{
				col++;
				LPD3DXBUFFER d3dbuffer=NULL;
				RECT tmprect;
				tmprect.left = j;
				tmprect.top = i;
				tmprect.right = tmprect.left + width;
				if( tmprect.right > rect.right ) tmprect.right = rect.right;
				
				tmprect.bottom = tmprect.top + height;
				if( tmprect.bottom > rect.bottom ) tmprect.bottom = rect.bottom;
				
				if( SUCCEEDED(D3DXSaveSurfaceToFileInMemory( &d3dbuffer,(D3DXIMAGE_FILEFORMAT)filefmt,pSurface,NULL,&tmprect)))
				{
					dstpics.push_back(std::make_pair(CRECT(tmprect), 
						XBuffer(d3dbuffer->GetBufferPointer(), d3dbuffer->GetBufferSize())));
					XPSAFE_RELEASE(d3dbuffer);
				}
				else
				{
					XPSAFE_RELEASE(d3dbuffer);
					return false;
				}
				
			}
		}
		
		XPSAFE_RELEASE(pSurface);
		XPSAFE_RELEASE(TextureInfo.m_D3dTexture);
		return true;
	}


	RenderPerformance DX9Renderer::GetPerformance()
	{
		return m_performance;
	}

	int DX9Renderer::GetAvailableTextureMem()
	{
		if( m_ifDevice )
			return m_ifDevice->GetAvailableTextureMem() / 1024 / 1024;
		return 0;
	}


	bool DX9Renderer::IsTextureCapsNoPow2()
	{
		if(m_ifDevice)
			return !m_bTextureCaps_pow2;
		return false;
	}

	bool DX9Renderer::InitBatchVB()
	{
		HRESULT hr = 0;
		XPV(m_ifDevice->CreateVertexBuffer(BATCH_VB_COUNT*sizeof(TCT1VERTEX), D3DUSAGE_DYNAMIC | D3DUSAGE_WRITEONLY,
			TCT1VertexFVF, D3DPOOL_DEFAULT ,&m_batchVB,NULL));
		XPV(m_ifDevice->CreateIndexBuffer(BATCH_IB_COUNT*sizeof(WORD), D3DUSAGE_DYNAMIC | D3DUSAGE_WRITEONLY,D3DFMT_INDEX16,
			D3DPOOL_DEFAULT ,&m_batchIB,NULL));		

		XPV(m_ifDevice->CreateVertexBuffer(10*sizeof(TCT1VERTEX), D3DUSAGE_DYNAMIC | D3DUSAGE_WRITEONLY,
			TCT1VertexFVF, D3DPOOL_DEFAULT ,&m_shapeVB,NULL));

		XPV(m_ifDevice->CreateVertexBuffer(4*MAX_PARTICLE_NUM*sizeof(TCT1VERTEX), D3DUSAGE_DYNAMIC | D3DUSAGE_WRITEONLY,
			TCT1VertexFVF, D3DPOOL_DEFAULT ,&m_particleVB,NULL));
		XPV(m_ifDevice->CreateVertexBuffer(4*MAX_PARTICLE_NUM*sizeof(UTCT1VERTEX), D3DUSAGE_DYNAMIC | D3DUSAGE_WRITEONLY,
			UTCT1VertexFVF, D3DPOOL_DEFAULT ,&m_particleUVB,NULL));
		XPV(m_ifDevice->CreateIndexBuffer(6*MAX_PARTICLE_NUM*sizeof(WORD), D3DUSAGE_WRITEONLY,D3DFMT_INDEX16,
					D3DPOOL_DEFAULT ,&m_particleIB,NULL));	//初始化的时候写好的数据buffer不应该用动态的D3DUSAGE_DYNAMIC | 

		WORD *pIndices = NULL;
		hr = m_particleIB->Lock(0, 0, (void**)&pIndices, 0); //D3DLOCK_DISCARD
		if( FAILED(hr) )
		{
			XPLOG_INFO(L"Lock Failed--InitBatchVB() 部分参数: AvailableTextureMem (%d) MB\n",
				GetAvailableTextureMem());
			return false;
		}

		WORD n = 0;
		for(int i=0; i<MAX_PARTICLE_NUM; i++) //最多MAX_PARTICLE_NUM个粒子
		{
			//顶点顺序为顺时针... 与D3D的D3DRS_CULLMODE默认值对应...
			*pIndices++ = n;
			*pIndices++ = n+1;
			*pIndices++ = n+2;
			*pIndices++ = n+2;
			*pIndices++ = n+3;
			*pIndices++ = n;
			n += 4;
		}
		m_particleIB->Unlock();


		XPV(m_ifDevice->CreateVertexBuffer(MAX_TCVERTEX_NUM*sizeof(TCVERTEX), D3DUSAGE_DYNAMIC | D3DUSAGE_WRITEONLY,
											TCVertexFVF, D3DPOOL_DEFAULT ,&m_TCVERTEXVB,NULL));

		XPV(m_ifDevice->CreateIndexBuffer(MAX_TCVERTEX_NUM*sizeof(WORD), D3DUSAGE_DYNAMIC | D3DUSAGE_WRITEONLY,D3DFMT_INDEX16,
											D3DPOOL_DEFAULT ,&m_TCVERTEXIB,NULL));

		WORD *pTCVERTEXIndices = NULL;
		hr = m_TCVERTEXIB->Lock(0, 0, (void**)&pTCVERTEXIndices, 0); //D3DLOCK_DISCARD
		if( FAILED(hr) )
		{
			XPLOG_INFO(L"Lock Failed--InitBatchVB() 部分参数: AvailableTextureMem (%d) MB\n",
				GetAvailableTextureMem());
			return false;
		}

		for(int i=0; i<MAX_TCVERTEX_NUM/3; i++) //最多100个三角形
		{
			//顶点顺序为顺时针... 与D3D的D3DRS_CULLMODE默认值对应...
			*pTCVERTEXIndices++ = 3*i;
			*pTCVERTEXIndices++ = 3*i+1;
			*pTCVERTEXIndices++ = 3*i+2;
		}
		m_TCVERTEXIB->Unlock();

		return hr == S_OK;
	}

	void DX9Renderer::DestroyBatchVB()
	{
		XPSAFE_RELEASE(m_batchIB);
		XPSAFE_RELEASE(m_batchVB);
		XPSAFE_RELEASE(m_shapeVB);
		XPSAFE_RELEASE(m_particleVB);
		XPSAFE_RELEASE(m_particleUVB);
		XPSAFE_RELEASE(m_particleIB);

		XPSAFE_RELEASE(m_TCVERTEXVB);
		XPSAFE_RELEASE(m_TCVERTEXIB);
	}

	//创建新的屏幕大小的Texture
	PictureHandle DX9Renderer::CreateRenderTarget(int iWidth, int iHeight, XPTEXTURE_FORMAT texfmt)
	{
		if( texfmt != XPTEXFMT_DEFAULT )
		{
			if( !IsTexfmtSupport(texfmt) )
				texfmt = XPTEXFMT_DEFAULT;
		}

		
		if( texfmt == XPTEXFMT_DEFAULT ) // 根据data本身和当前颜色模式决定
		{
			texfmt = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
		}

		CTextureInfo TextureInfo;
		CPicInfo PicInfo;
		bool bret = false;

		HRESULT hr;
		hr = D3DXCreateTexture(m_ifDevice, iWidth, iHeight, 1, D3DUSAGE_RENDERTARGET, 
								(D3DFORMAT)texfmt, D3DPOOL_DEFAULT, &TextureInfo.m_D3dTexture);
		if( FAILED(hr) ) 
		{
			XPLOG_INFO(L"  CreateRenderTarget() 部分参数: iWidth(%d), iHeight(%d), texfmt(%d)\n",
						iWidth, iHeight, (int)texfmt);
			
			void* pMsgBuf;
			::FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM,
							NULL, hr, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
							(LPWSTR)&pMsgBuf, 0, NULL );

			XPLOG_INFO(L"CreateRenderTarget Failed--GenTexture() 部分参数: error info:(%s), AvailableTextureMem (%d) MB\n",
							(LPWSTR)&pMsgBuf, GetAvailableTextureMem());

			LocalFree(pMsgBuf);

			return INVALID_PICTURE_HANDLE;
		}

		D3DSURFACE_DESC desc;
		TextureInfo.m_D3dTexture->GetLevelDesc(0, &desc);
		TextureInfo.rect.left = 0;
		TextureInfo.rect.top = 0;
		TextureInfo.rect.right = iWidth;
		TextureInfo.rect.bottom = iHeight;
#ifdef XP_PERFORMANCE
		switch( texfmt )
		{
		case XPTEXFMT_A4R4G4B4:
			TextureInfo.mem = 2 * desc.Width * desc.Height;
			break;
		case XPTEXFMT_A8R8G8B8:
			TextureInfo.mem = 4 * desc.Width * desc.Height;
			break;
		}
#endif
		PicInfo.m_info.m_nPicWidth = iWidth;
		PicInfo.m_info.m_nPicHeight = iHeight;
		PicInfo.m_info.m_pooltype = static_cast<XPPOOL_TYPE>(D3DPOOL_DEFAULT);
		PicInfo.m_info.m_texfmt = texfmt;
		PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
		m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));
		m_setRenderTargets.insert(m_picHandleSeed);

#ifdef XP_PERFORMANCE
		m_performance.m_iManagedTextureCount++;
		m_performance.m_iManagedTextureMemory += TextureInfo.mem;
#endif
		return m_picHandleSeed++;
	}

	bool DX9Renderer::FreeRenderTarget(PictureHandle hRenderTarget)
	{
		if( m_setRenderTargets.end() == m_setRenderTargets.find(hRenderTarget) )
			return false;
		m_setRenderTargets.erase(hRenderTarget);
		return FreePicture(hRenderTarget);
	}

	bool DX9Renderer::PushRenderTarget(PictureHandle hRenderTarget)
	{
		if(m_ifDevice==NULL)
			return false;

		if( m_setRenderTargets.end() == m_setRenderTargets.find(hRenderTarget) )
			return false;

		PictureMap::iterator it = m_mapPictures.find(hRenderTarget);
		assert( it != m_mapPictures.end() );
	
		IDirect3DSurface9* pSurface;
		m_ifDevice->GetRenderTarget(0, &pSurface);
		m_stkRenderTargets.push_back(pSurface);
		it->second.m_CTextureInfoVector[0].m_D3dTexture->GetSurfaceLevel(0, &pSurface);
		bool hr = SUCCEEDED(m_ifDevice->SetRenderTarget(0, pSurface));
		XPSAFE_RELEASE(pSurface);
		return hr;
	}

	bool DX9Renderer::PopRenderTarget()
	{
		if(m_ifDevice==NULL)
			return false;
		if(m_stkRenderTargets.empty())
			return false;
		IDirect3DSurface9 *pSurface = m_stkRenderTargets.back();
		m_stkRenderTargets.pop_back();
		bool hr = SUCCEEDED(m_ifDevice->SetRenderTarget(0, pSurface));
		XPSAFE_RELEASE(pSurface);
		return hr;
	}

	void DX9Renderer::ReleaseRenderTargets()
	{
		m_setRenderTargets.clear();
		if(m_ifDevice==NULL)
			return;
		if(m_stkRenderTargets.empty())
			return;
		IDirect3DSurface9 *pSurface = m_stkRenderTargets.front(); // backbuffer
		m_ifDevice->SetRenderTarget(0, pSurface);
		RenderTargetStack::reverse_iterator iter = m_stkRenderTargets.rbegin();
		for (;iter!=m_stkRenderTargets.rend();iter++)
		{
			XPSAFE_RELEASE(*iter);
		}
		m_stkRenderTargets.clear();
	}

	// from IBaseRenderer
	
	void DX9Renderer::SetAlphaBlendEnable(bool f)
	{
		m_stateMan.SetAlphaBlendEnable(f);
	}

	bool DX9Renderer::GetAlphaBlendEnable() const
	{
		return m_stateMan.GetAlphaBlendEnable();
	}

	void DX9Renderer::SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend)
	{
		m_stateMan.SetAlphaBlendSrcType(srcblend);
	}
		
	XPALPHABLEND_TYPE DX9Renderer::GetAlphaBlendSrcType() const
	{
		return m_stateMan.GetAlphaBlendSrcType();
	}

	void DX9Renderer::SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend)
	{
		m_stateMan.SetAlphaBlendDstType(dstblend);
	}
		
	XPALPHABLEND_TYPE DX9Renderer::GetAlphaBlendDstType() const
	{
		return m_stateMan.GetAlphaBlendDstType();
	}

	void DX9Renderer::SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop)
	{
		m_stateMan.SetTextureColorBlendType(colorop);
	}

	XPTEXCOLORBLEND_TYPE DX9Renderer::GetTextureColorBlendType() const
	{
		return m_stateMan.GetTextureColorBlendType();
	}

	void DX9Renderer::SetSeparateAlphaBlend(bool f)
	{
		m_stateMan.SetSeparateAlphaBlend(f);
	}

	bool DX9Renderer::GetSeparateAlphaBlend() const
	{
		return m_stateMan.GetSeparateAlphaBlend();
	}

	void DX9Renderer::SetRenderEffect(XPRENDER_EFFECT re)
	{
		m_stateMan.SetRenderEffect(re);
	}

	XPRENDER_EFFECT DX9Renderer::GetRenderEffect() const
	{
		return m_stateMan.GetRenderEffect();
	}

	
	
	

	// 用TriangleFan来绘制这些顶点
	bool DX9Renderer::DrawTriangleFan(PictureHandle handle, const TCT1VERTEX* pVertexs, int nVertexNum/*顶点个数*/)
	{ 
		//按逆时针排列的一个封闭多边形顶点
		if( handle == INVALID_PICTURE_HANDLE || pVertexs == NULL || nVertexNum < 1 ) 
			return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;

		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;

		m_ifDevice->SetFVF(TCT1VertexFVF);

		m_ifDevice->SetStreamSource(0, m_shapeVB, 0, sizeof(TCT1VERTEX));
		m_ifDevice->SetTexture(0, it->second.m_CTextureInfoVector[0].m_D3dTexture);

		HRESULT hr;
		TCT1VERTEX *pVertexBuffer = NULL;
		hr = m_shapeVB->Lock(0, nVertexNum * sizeof(TCT1VERTEX), (void**)&pVertexBuffer, D3DLOCK_DISCARD);
		if( FAILED(hr) )
		{
			XPLOG_INFO(L"Lock Failed--DrawTriangleFan() 部分参数: AvailableTextureMem (%d) MB\n",
				GetAvailableTextureMem());
			return false;
		}
		memcpy(pVertexBuffer, pVertexs, nVertexNum*sizeof(TCT1VERTEX));				
		m_shapeVB->Unlock();

		XPV(m_ifDevice->DrawPrimitive(D3DPT_TRIANGLEFAN, 0, nVertexNum-2));					
	#ifdef XP_PERFORMANCE
			m_performance.m_iDrawPrimitiveCountPerFrame++;
			//m_performance.m_iDrawPictureCountPerFrame += todo?;
			//m_performance.m_iTotalPixelRenderedPerFrame += todo?;
#endif
		return true;
	}

	// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
	bool DX9Renderer::DrawParticles(PictureHandle handle, const TCT1VERTEX* pVertexs, int nVertexNum/*顶点个数*/)
	{
		if( handle == INVALID_PICTURE_HANDLE || pVertexs == NULL || nVertexNum < 1 ) 
			return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;

		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;

		HRESULT hr;

		m_ifDevice->SetFVF(TCT1VertexFVF);

		m_ifDevice->SetStreamSource(0, m_particleVB, 0, sizeof(TCT1VERTEX));
		m_ifDevice->SetIndices(m_particleIB);
		m_ifDevice->SetTexture(0, it->second.m_CTextureInfoVector[0].m_D3dTexture);

		TCT1VERTEX* pVertexBuffer = NULL;
		hr = m_particleVB->Lock(0, nVertexNum * sizeof(TCT1VERTEX), (void**)&pVertexBuffer, D3DLOCK_DISCARD);
		if( FAILED(hr) )
		{
			XPLOG_INFO(L"  DrawParticles(TCT1VERTEX) 部分参数: nVertexNum(%d)\n", nVertexNum);
			XPLOG_INFO(L"Lock Failed--DrawParticles() 部分参数: AvailableTextureMem (%d) MB\n", GetAvailableTextureMem());

			return false;
		}

		memcpy(pVertexBuffer, pVertexs, nVertexNum*sizeof(TCT1VERTEX));				
		m_particleVB->Unlock();
		hr = m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, 0, 0, nVertexNum, 0, nVertexNum/2);
		if( FAILED(hr) )
			return false;

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveCountPerFrame++;
		m_performance.m_iDrawPictureCountPerFrame += nVertexNum/4;
		//m_performance.m_iTotalPixelRenderedPerFrame += todo?;
#endif

		/*
		int nBatchOffset = 0;
		int nBatchSize = 512; //每次拷贝的顶点数 //一定要是4的倍数..因为绘制一个粒子需要4个顶点
		int nTempVertexNum = nVertexNum;

		while(nTempVertexNum > 0)
		{
			int nsize = nTempVertexNum-nBatchSize >0 ? nBatchSize:nTempVertexNum;

			hr = m_particleVB->Lock(nBatchOffset *sizeof(TCT1VERTEX), nsize *sizeof(TCT1VERTEX), (void**)&pVertexBuffer, 
				nBatchOffset==0 ? D3DLOCK_DISCARD : D3DLOCK_NOOVERWRITE);
			if( FAILED(hr) )
			{
				XPLOG_INFO(L"  DrawParticles(TCT1VERTEX) 部分参数: nBatchOffset(%d), nsize(%d)\n",
					nBatchOffset, nsize);
				XPLOG_INFO(L"Lock Failed--DrawParticles() 部分参数: AvailableTextureMem (%d) MB\n",
					GetAvailableTextureMem());
				return false;
			}

			memcpy(pVertexBuffer, &pVertexs[nBatchOffset], nsize*sizeof(TCT1VERTEX));
			m_particleVB->Unlock();

			hr = m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, nBatchOffset, 0, nsize, 0, nsize/2);
																							//nBatchOffset*6/4
			if( FAILED(hr) )
			{
				XPLOG_INFO(L"  DrawParticles(TCT1VERTEX) 部分参数: nBatchOffset(%d), nsize(%d)\n",
							nBatchOffset, nsize);
				return false;
			}

			nBatchOffset += nsize;
			nBatchOffset = nBatchOffset >= nVertexNum ? 0:nBatchOffset;
			nTempVertexNum -= nsize;
#ifdef XP_PERFORMANCE
			m_performance.m_iDrawPrimitiveCountPerFrame++;
			m_performance.m_iDrawPictureCountPerFrame += nsize/4;
			//m_performance.m_iTotalPixelRenderedPerFrame += todo?;
#endif
		}
		*/
		return true;
	}

	// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
	bool DX9Renderer::DrawParticles(PictureHandle handle, const UTCT1VERTEX* pVertexs, int nVertexNum/*顶点个数*/)
	{
		if( handle == INVALID_PICTURE_HANDLE || pVertexs == NULL || nVertexNum < 1 ) 
			return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;

		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;

		HRESULT hr;

		m_ifDevice->SetFVF(UTCT1VertexFVF);

		m_ifDevice->SetStreamSource(0, m_particleUVB, 0, sizeof(UTCT1VERTEX));
		m_ifDevice->SetIndices(m_particleIB);
		m_ifDevice->SetTexture(0, it->second.m_CTextureInfoVector[0].m_D3dTexture);
		
		UTCT1VERTEX* pVertexBuffer = NULL;

		int nBatchOffset = 0;
		int nBatchSize = 512; //每次拷贝的顶点数 //一定要是4的倍数..因为绘制一个粒子需要4个顶点
		int nTempVertexNum = nVertexNum;

		while(nTempVertexNum > 0)
		{
			int nsize = nTempVertexNum-nBatchSize >0 ? nBatchSize:nTempVertexNum;

			hr = m_particleUVB->Lock(nBatchOffset *sizeof(UTCT1VERTEX), nsize *sizeof(UTCT1VERTEX), (void**)&pVertexBuffer, 
				nBatchOffset==0 ? D3DLOCK_DISCARD : D3DLOCK_NOOVERWRITE);
			if( FAILED(hr) )
			{
				XPLOG_INFO(L"Lock Failed--DrawParticles() 部分参数: AvailableTextureMem (%d) MB\n",
					GetAvailableTextureMem());
				return false;
			}

			memcpy(pVertexBuffer, &pVertexs[nBatchOffset], nsize*sizeof(UTCT1VERTEX));
			m_particleUVB->Unlock();

			hr = m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, nBatchOffset, 0, nsize, 0, nsize/2);
			if( FAILED(hr) )
			{
				XPLOG_INFO(L"  DrawParticles(UTCT1VERTEX) 部分参数: nBatchOffset(%d), nsize(%d)\n",
							nBatchOffset, nsize);
				return false;
			}

			nBatchOffset += nsize;
			nBatchOffset = nBatchOffset >= nVertexNum ? 0:nBatchOffset;
			nTempVertexNum -= nsize;
#ifdef XP_PERFORMANCE
			m_performance.m_iDrawPrimitiveCountPerFrame++;
			m_performance.m_iDrawPictureCountPerFrame += nsize/4;
			//m_performance.m_iTotalPixelRenderedPerFrame += todo?;
#endif

		}

// 		UTCT1VERTEX* pVertexBuffer = NULL;
// 		XPV(m_particleUVB->Lock(0, nVertexNum * sizeof(UTCT1VERTEX), (void**)&pVertexBuffer, D3DLOCK_DISCARD));
// 		memcpy(pVertexBuffer, pVertexs, nVertexNum*sizeof(UTCT1VERTEX));				
// 		m_particleUVB->Unlock();
// 
// 		XPV(m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, 0, 0, nVertexNum, 0, nVertexNum/2));


		return true;
	}

	bool DX9Renderer::CollectParticles(PictureHandle handle, XPTEXCOLORBLEND_TYPE colorBlend, PSL_RENDSTATE alphaBlend,
		const TCT1VERTEX* pVertexs, int nVertexNum/*顶点个数*/)
	{
		//收集粒子不绘制
		ULONG64 ULong64Key = handle;
		ULong64Key = ULong64Key<<16 | colorBlend<<8 | alphaBlend;

		std::map<ULONG64, std::vector<TCT1VERTEX>>::iterator it = m_mapParticles.find(ULong64Key);
		if( it == m_mapParticles.end() )
		{ //一次最多加进2000个粒子来
			std::vector<TCT1VERTEX> particles;
			particles.resize(nVertexNum);
			memcpy(&particles[0], pVertexs, nVertexNum * sizeof(TCT1VERTEX));
			m_mapParticles.insert(std::make_pair(ULong64Key, particles));
		}
		else
		{//如果超过MAX_PARTICLE_NUM个粒子就要在这里先绘制掉...

			int cnt = (int)(it->second.size());
			if( cnt+nVertexNum >= 4*MAX_PARTICLE_NUM )
			{
				DrawCurParticles(it->first, it->second);
				it->second.clear();
			}
			else
			{
				for(int i=0; i<nVertexNum; i++)
				{
					it->second.push_back( *(pVertexs+i) );
				}
			}
		}

		return true;
	}

	bool DX9Renderer::DrawCurParticles(ULONG64 param, const std::vector<TCT1VERTEX>& particles)
	{
		PictureHandle handle = (int)(param>>16 & 0xffffffff);
		XPTEXCOLORBLEND_TYPE colorBlend = (XPTEXCOLORBLEND_TYPE)(param>>8 & 0xff);
		PSL_RENDSTATE alphaBlend = (PSL_RENDSTATE)(param & 0xff);

		SetTextureColorBlendType(colorBlend);
		XPRENDER_EFFECT oldefct = GetRenderEffect();
		switch(alphaBlend)
		{
		case PSLRENDSTATE_ALPHA:
			SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
			break;
		case PSLRENDSTATE_LIGHTING:
			SetRenderEffect(XPRE_NULL);
			SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
			SetAlphaBlendDstType(XPBLEND_ONE);
			break;
		case PSLRENDSTATE_ADD:
			SetRenderEffect(XPRE_NULL);
			SetAlphaBlendSrcType(XPBLEND_ONE);
			SetAlphaBlendDstType(XPBLEND_ONE);
			break;
		default:
			break;
		}

		int cnt = (int)(particles.size());

		if( handle == INVALID_PICTURE_HANDLE || cnt < 1 ) 
			return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;

		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;

		HRESULT hr;

		m_ifDevice->SetFVF(TCT1VertexFVF);

		m_ifDevice->SetStreamSource(0, m_particleVB, 0, sizeof(TCT1VERTEX));
		m_ifDevice->SetIndices(m_particleIB);
		m_ifDevice->SetTexture(0, it->second.m_CTextureInfoVector[0].m_D3dTexture);

		TCT1VERTEX* pVertexBuffer = NULL; //if > 2000...
		hr = m_particleVB->Lock(0, cnt * sizeof(TCT1VERTEX), (void**)&pVertexBuffer, D3DLOCK_DISCARD);
		if( FAILED(hr) )
		{
			XPLOG_INFO(L"  BatchDrawParticles(TCT1VERTEX) 部分参数: nVertexNum(%d)\n", cnt);
			XPLOG_INFO(L"Lock Failed--BatchDrawParticles() 部分参数: AvailableTextureMem (%d) MB\n", GetAvailableTextureMem());

			return false;
		}

		memcpy(pVertexBuffer, &particles[0], cnt*sizeof(TCT1VERTEX));				
		m_particleVB->Unlock();

		hr = m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, 0, 0, cnt, 0, cnt/2);

		SetRenderEffect(oldefct);
		if( FAILED(hr) )
			return false;
		return true;
	}

	bool DX9Renderer::BatchDrawParticles()
	{
		std::map<ULONG64, std::vector<TCT1VERTEX>>::iterator itb = m_mapParticles.begin();
		std::map<ULONG64, std::vector<TCT1VERTEX>>::iterator ite = m_mapParticles.end();
		for(; itb != ite; ++itb)
		{
			DrawCurParticles(itb->first, itb->second);
		}
		m_mapParticles.clear();

		return true;
	}

	bool DX9Renderer::DrawIndexVerticeBuffer(PictureHandle handle, const TCT1VERTEX* pVertexs, const WORD* pIndexs,
													int nVertexNum, int nIndexNum)
	{
		if( handle == INVALID_PICTURE_HANDLE || pVertexs == NULL || nVertexNum < 1 ) 
			return false;

		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;

		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;
		//在不支持非2幂纹理的显卡上不能绘制...

		HRESULT hr;

		m_ifDevice->SetFVF(TCT1VertexFVF);

		m_ifDevice->SetStreamSource(0, m_batchVB, 0, sizeof(TCT1VERTEX));
		m_ifDevice->SetIndices(m_batchIB);
		m_ifDevice->SetTexture(0, it->second.m_CTextureInfoVector[0].m_D3dTexture);

		TCT1VERTEX* pVertexBuffer = NULL;
 		if( (m_iBatchVBOffset + nVertexNum) > BATCH_VB_COUNT-1 )
 			m_iBatchVBOffset = 0;
 		hr = m_batchVB->Lock(sizeof(TCT1VERTEX) * m_iBatchVBOffset, sizeof(TCT1VERTEX) * nVertexNum, (void**)&pVertexBuffer,
			m_iBatchVBOffset == 0 ?D3DLOCK_DISCARD :D3DLOCK_NOOVERWRITE);
		if( FAILED(hr) )
		{
			XPLOG_INFO(L"Lock Failed--DrawIndexVerticeBuffer() 部分参数: AvailableTextureMem (%d) MB\n",
				GetAvailableTextureMem());
			return false;
		}

 		memcpy(pVertexBuffer, pVertexs, nVertexNum*sizeof(TCT1VERTEX));	

		m_batchVB->Unlock();

		WORD* pIndexBuffer = NULL;

		if( (m_iBatchIBOffset + nIndexNum) > BATCH_IB_COUNT-1 )
			m_iBatchIBOffset = 0;
 		hr = m_batchIB->Lock(sizeof(WORD) * m_iBatchIBOffset, nIndexNum * sizeof(WORD), (void**)&pIndexBuffer,
			m_iBatchIBOffset == 0 ?D3DLOCK_DISCARD :D3DLOCK_NOOVERWRITE);
		if( FAILED(hr) )
		{
			XPLOG_INFO(L"Lock Failed--DrawIndexVerticeBuffer() 部分参数: AvailableTextureMem (%d) MB\n",
				GetAvailableTextureMem());
			return false;
		}

 		memcpy(pIndexBuffer, pIndexs, nIndexNum*sizeof(WORD));			
		
		m_batchIB->Unlock();

	//	m_ifDevice->SetRenderState( D3DRS_FILLMODE, D3DFILL_WIREFRAME );

	//	XPV(m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, 0, 0, nVertexNum, 0, nIndexNum/3)); //nIndexNum/3
													//绘制所有三角形.. 每3个索引值对应一个三角形

		hr = m_ifDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, m_iBatchVBOffset, 0, nVertexNum, m_iBatchIBOffset, nIndexNum/3);
		if( FAILED(hr) )
		{
			XPLOG_INFO(L"  DrawIndexVerticeBuffer() 部分参数: m_iBatchVBOffset(%d), nVertexNum(%d), m_iBatchIBOffset(%d), nIndexNum(%d)\n",
						m_iBatchVBOffset, nVertexNum, m_iBatchIBOffset, nIndexNum);
			return false;
		}
						
		m_iBatchVBOffset += nVertexNum;
		m_iBatchIBOffset += nIndexNum;

#ifdef XP_PERFORMANCE
		m_performance.m_iDrawPrimitiveCountPerFrame++;
		m_performance.m_iDrawPictureCountPerFrame += nVertexNum/4;
		//m_performance.m_iTotalPixelRenderedPerFrame += todo?;
#endif

		return true;
	}

};


