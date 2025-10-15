#include "stdafx.h"
#include "..\common\log.h"
#include "rendereffect.h"
#include "fxcode.h"


namespace Nuclear
{
	XPTEXCOLORBLEND_TYPE RenderEffect::s_currentColorBlendType = XPTOP_SELECTARG1;

	RenderEffect::RenderEffect(void)
	{
		for (int i=0;i<XPTOP_TYPE_COUNT;++i)
		{
			m_bEnable[i] = false;
			m_pPSshader[i] = NULL;
			m_pConstTablePS[i] = NULL;
		}
	}

	RenderEffect::~RenderEffect(void)
	{
	}

	void RenderEffect::Release()
	{
		for (int i=0;i<XPTOP_TYPE_COUNT;++i)
		{
			if (m_bEnable[i])
			{
				XPSAFE_RELEASE(m_pPSshader[i]);
				XPSAFE_RELEASE(m_pConstTablePS[i]);
			}
		}
	}

	bool RenderEffect::InitShader(IDirect3DDevice9* ifDevice)
	{
		bool bResult = true;
		std::string sourceCode;
		LPD3DXBUFFER shaderPS = NULL;
		for(int i=0; i<XPTOP_TYPE_COUNT; ++i)
		{
			if(!m_bEnable[i])
			{
				bResult = false;
				continue;
			}
			shaderPS = NULL;

			GetShaderStr(i, sourceCode);
			
			LPD3DXBUFFER pErrorMsgs = NULL;
			HRESULT hr = D3DXCompileShader(sourceCode.c_str(), sourceCode.size(), NULL, NULL, "main", "ps_3_0",
				D3DXSHADER_ENABLE_BACKWARDS_COMPATIBILITY | D3DXSHADER_OPTIMIZATION_LEVEL3 | D3DXSHADER_SKIPVALIDATION, 
				&shaderPS, &pErrorMsgs, m_pConstTablePS + i);
				// | D3DXSHADER_USE_LEGACY_D3DX9_31_DLL //老版本编译...
			if(pErrorMsgs != NULL)
			{
				LPSTR strErr = (LPSTR)( pErrorMsgs->GetBufferPointer() );
				DWORD dwSize = pErrorMsgs->GetBufferSize();

				std::wstring errStr = CharArrayToUString(strErr, dwSize);
				XPLOG_ERROR(errStr.c_str());

			//	FILE* fp = fopen("c:\\aaa.txt", "w");
			//	fprintf(fp, strErr);
			//	fclose(fp);

				pErrorMsgs->Release();				
			}

			if( FAILED(hr) )
			{
				m_bEnable[i] = false;
				bResult = false;

// 				int a=-1;
// 
// 				if(hr == D3DERR_INVALIDCALL)
// 					a=0;
// 				if(hr == D3DXERR_INVALIDDATA)
// 					a=1;
// 				if(hr == E_OUTOFMEMORY)
// 					a=2;

				continue;
			}

			hr = ifDevice->CreatePixelShader((DWORD*)shaderPS->GetBufferPointer(), m_pPSshader + i);
			XPSAFE_RELEASE(shaderPS);

			if( FAILED(hr) )
			{
				m_bEnable[i] = false;
				bResult = false;
				continue;
			}
			//pConstTablePS->SetDefaults(ifDevice); // ?????????????todo
		}

		return bResult;
	}

	////////////////////////////////////////////////////////////////

	TBRenderEffect::TBRenderEffect()
	{
		param.fcoef = 0.5f; //两重纹理的混合比例...
	}

	void TBRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::Get2TextureBlendStr(code);
	}

	void TBRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
		D3DXHANDLE fparamHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_fcoef");
		m_pConstTablePS[s_currentColorBlendType]->SetFloat(ifDevice, fparamHandle, param.fcoef);
	}
	////////////////////////////////////////////////////////////////

	CBRenderEffect::CBRenderEffect()
	{
		param.fCoefr = 0.2f; //-1----+1 色彩分量调节
		param.fCoefg = 0.0f;
		param.fCoefb = 0.0f;
		param.fLightness = 0.0f; //-1----+1 亮度调节
		param.fContrastk = 1.0f;//0--1坐标.. 控制对比度函数的斜率和y轴截距
		param.fContrastb = 0.0f;

		param.fCentx = 500.0f;
		param.fCenty = 400.0f;
	}

	void CBRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetColorBanlanceShaderStr(colorOpType, code);
	}

	void CBRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
		D3DXHANDLE fparamHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_param");
		float fParam[8];
		fParam[0] = param.fCoefr;
		fParam[1] = param.fCoefg;
		fParam[2] = param.fCoefb;
		fParam[3] = param.fLightness;
		fParam[4] = param.fContrastk;
		fParam[5] = param.fContrastb;
		fParam[6] = param.fCentx;
		fParam[7] = param.fCenty;
	
		m_pConstTablePS[s_currentColorBlendType]->SetFloatArray(ifDevice, fparamHandle, fParam, 8);
	}
	////////////////////////////////////////////////////////////////

	CBRenderEffect_2::CBRenderEffect_2()
	{
		param.fCoefr = 0.2f; //-1----+1 色彩分量调节
		param.fCoefg = 0.0f;
		param.fCoefb = 0.0f;
		param.fLightness = 0.0f; //-1----+1 亮度调节
		param.fContrastk = 1.0f;//0--1坐标.. 控制对比度函数的斜率和y轴截距
		param.fContrastb = 0.0f;

		param.fCentx = 500.0f;
		param.fCenty = 400.0f;
	}

	void CBRenderEffect_2::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetColorBanlanceShaderStr_2(colorOpType, code);
	}

	void CBRenderEffect_2::SetParameter(IDirect3DDevice9* ifDevice)
	{
		D3DXHANDLE fparamHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_param");
		float fParam[8];
		fParam[0] = param.fCoefr;
		fParam[1] = param.fCoefg;
		fParam[2] = param.fCoefb;
		fParam[3] = param.fLightness;
		fParam[4] = param.fContrastk;
		fParam[5] = param.fContrastb;
		fParam[6] = param.fCentx;
		fParam[7] = param.fCenty;
	
		m_pConstTablePS[s_currentColorBlendType]->SetFloatArray(ifDevice, fparamHandle, fParam, 8);
	}

	/////////////////////////////////////////////////////////////////

	RBRenderEffect::RBRenderEffect()
	{
		param.BlurCent = NuclearFPoint(0.2f, 0.3f);
		param.blurDegree = 0.3f;
		param.nBlurType = 0;
	}

	void RBRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetRadialblurShaderStr(code);
	}

	void RBRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
		D3DXHANDLE fparamHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_param");
		float fParam[3];
		fParam[0] = param.BlurCent.x;
		fParam[1] = param.BlurCent.y;
		fParam[2] = param.blurDegree;
		m_pConstTablePS[s_currentColorBlendType]->SetFloatArray(ifDevice, fparamHandle, fParam, 3);
	}

	/////////////////////////////////////////////////////////////////

	EPRenderEffect::EPRenderEffect()
	{
		param.fSizex = 800; //一般设置为图像宽、高的n倍(0.2<n<=1)左右波动
		param.fSizey = 600;
		param.ndir = 0;//8方向 0---7
	}

	void EPRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetEmbosspicShaderStr(code);
	}

	void EPRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
		float fSizex = 1.0f/param.fSizex;
		float fSizey = 1.0f/param.fSizey;

		int noffsetxy[2];
		switch(param.ndir)
		{
		case 0:
			noffsetxy[0] = -1;
			noffsetxy[1] = -1;
			break;
		case 1:
			noffsetxy[0] = 0;
			noffsetxy[1] = -1;
			break;
		case 2:
			noffsetxy[0] = 1;
			noffsetxy[1] = -1;
			break;
		case 3:
			noffsetxy[0] = 1;
			noffsetxy[1] = 0;
			break;
		case 4:
			noffsetxy[0] = 1;
			noffsetxy[1] = 1;
			break;
		case 5:
			noffsetxy[0] = 0;
			noffsetxy[1] = 1;
			break;
		case 6:
			noffsetxy[0] = -1;
			noffsetxy[1] = 1;
			break;
		case 7:
			noffsetxy[0] = -1;
			noffsetxy[1] = 0;
			break;
		default:
			break;
		}

		float fPram[4];

		fPram[0] = fSizex;
		fPram[1] = fSizey;
		fPram[2] = (float)noffsetxy[0];
		fPram[3] = (float)noffsetxy[1];

		D3DXHANDLE fparamHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_param");

		m_pConstTablePS[s_currentColorBlendType]->SetFloatArray(ifDevice, fparamHandle, fPram, 4);
	}

	/////////////////////////////////////////////////////////

	GPRenderEffect::GPRenderEffect()
	{
		param.nType = 0; //0，默认参数,普通灰度图效果；1，r保留，其它分量置0；2，g...；3，b...
	}

	void GPRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetGrayShaderStr(code);
	}

	void GPRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
		D3DXHANDLE nTypeHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_nType");
		m_pConstTablePS[s_currentColorBlendType]->SetInt(ifDevice, nTypeHandle, param.nType);
	}

	/////////////////////////////////////////////////////////////////

	ALRenderEffect::ALRenderEffect()
	{
	}

	void ALRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetAlphaPicShaderStr(code);
	}

	void ALRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
	}

	/////////////////////////////////////////////////////////////////

	DistortionRenderEffect::DistortionRenderEffect()
	{
		param.fdegree = 0.5f; //扭曲程度0.0f----1.0f
		param.fcent = NuclearFPoint(0.5f, 0.5f); //扭曲中心  //左上角(0,0) 右下角(1,1)
		param.fMaxDis = 0.5f; //扭曲半径 //0.0f----1.0f
		param.nType1 = 1; //控制方向 //挤压还是阔张等...
		param.nType2 = 1; //控制形状
	}

	void DistortionRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetDistortionShaderStr(code);
	}

	void DistortionRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
		float fPram[4];
		int nType[2];
		fPram[0] = param.fdegree;
		fPram[1] = param.fcent.x;
		fPram[2] = param.fcent.y;
		fPram[3] = param.fMaxDis;
		nType[0] = param.nType1;
		nType[1] = param.nType2;

		D3DXHANDLE fparamHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_param");
		D3DXHANDLE nTypeHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_nType");

		m_pConstTablePS[s_currentColorBlendType]->SetFloatArray(ifDevice, fparamHandle, fPram, 4);
		m_pConstTablePS[s_currentColorBlendType]->SetIntArray(ifDevice, nTypeHandle, nType, 2);
	}

	/////////////////////////////////////////////////////////

	FontOutlineRenderEffect::FontOutlineRenderEffect()
	{
		param.dwOutlineColor = XPCOLOR_FULL; //轮廓颜色
		param.nTextureWidth = 512; //纹理宽高
		param.nTextureHeight = 512;
	}

	void FontOutlineRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetOutlineShaderStr(code);
	}

	void FontOutlineRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
		float outlineparam[6]; //rgba //sizex,sizey
		outlineparam[0] = param.dwOutlineColor.r/255.0f;
		outlineparam[1] = param.dwOutlineColor.g/255.0f;
		outlineparam[2] = param.dwOutlineColor.b/255.0f;
		outlineparam[3] = param.dwOutlineColor.a/255.0f;		
		outlineparam[4] = 1.0f/param.nTextureWidth;
		outlineparam[5] = 1.0f/param.nTextureHeight;

	//	float samplesIndex[16] = {-1.0f,-1.0f, 0.0f,-1.0f, 1.0f,-1.0f, 1.0f,0.0f, 1.0f,1.0f, 0.0f,1.0f, -1.0f,1.0f, -1.0f, 0.0f};									

		D3DXHANDLE fparamHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_param");
		m_pConstTablePS[s_currentColorBlendType]->SetFloatArray(ifDevice, fparamHandle, outlineparam, 6);

	//	D3DXHANDLE fsamplesIndexHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_samplesIndex");
	//	m_pConstTablePS[s_currentColorBlendType]->SetFloatArray(ifDevice, fsamplesIndexHandle, samplesIndex, 16);
	}

	/////////////////////////////////////////////////////////
	GaussianBlurBlendRenderEffect::GaussianBlurBlendRenderEffect()
	{
		param.nBlendType = 0; //
		param.nTextureWidth = 512; //纹理宽高
		param.nTextureHeight = 512;
	}

	void GaussianBlurBlendRenderEffect::GetShaderStr(int colorOpType, std::string& code)
	{
		FXCode::GetGaussianBlurBlendShaderStr(code);
	}

	void GaussianBlurBlendRenderEffect::SetParameter(IDirect3DDevice9* ifDevice)
	{
		float pixelSize[2];		
		pixelSize[0] = 1.0f/param.nTextureWidth;
		pixelSize[1] = 1.0f/param.nTextureHeight;

		D3DXHANDLE fparamHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_param");		
		m_pConstTablePS[s_currentColorBlendType]->SetFloatArray(ifDevice, fparamHandle, pixelSize, 2);
		D3DXHANDLE nBlendTypeHandle = m_pConstTablePS[s_currentColorBlendType]->GetConstantByName(NULL, "g_nBlendType");
		m_pConstTablePS[s_currentColorBlendType]->SetInt(ifDevice, nBlendTypeHandle, param.nBlendType);
	}
};