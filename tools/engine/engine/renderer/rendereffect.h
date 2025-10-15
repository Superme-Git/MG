#pragma once

#include <d3d9.h>
#include <d3dx9.h>
#include "..\common\xptypes.h"
#include "ibaserenderer.h"

namespace Nuclear
{

	class RenderEffect
	{
	protected:
		bool m_bEnable[XPTOP_TYPE_COUNT];
		LPDIRECT3DPIXELSHADER9 m_pPSshader[XPTOP_TYPE_COUNT];
		LPD3DXCONSTANTTABLE m_pConstTablePS[XPTOP_TYPE_COUNT];
		static XPTEXCOLORBLEND_TYPE s_currentColorBlendType;

	public:
		RenderEffect(void);
		virtual ~RenderEffect(void);


	private:
		virtual void GetShaderStr(int colorOpType, std::string& code) = 0;

	public:
		bool IsEnable() { return m_bEnable[s_currentColorBlendType]; };
		void Release();
		HRESULT SetPixelShader(IDirect3DDevice9* ifDevice) { return ifDevice->SetPixelShader(m_pPSshader[s_currentColorBlendType]); }
		bool InitShader(IDirect3DDevice9* ifDevice);
		virtual void Init(DWORD psShaderVer) 
		{ 
			for (int i=0;i<XPTOP_TYPE_COUNT;++i)
			{
				m_bEnable[i] = psShaderVer >= D3DPS_VERSION(3,0);
			}
		}
		virtual void SetParameter(IDirect3DDevice9* ifDevice) = 0;
		virtual void ChangeShaderParam(const void* pParam) = 0;
		virtual void SetTexture(IDirect3DDevice9* ifDevice, const char* name, const void* pParam){};
		static XPTEXCOLORBLEND_TYPE GetCurrentColorBlendType() { return s_currentColorBlendType; }
		static void SetColorBlendType(XPTEXCOLORBLEND_TYPE type) 
		{
			//在这里应该是选择用不同的Shader，设SetPixelShader和Parameter留给外面做
			s_currentColorBlendType = type;
		}

	};

	/////////////////////////////////////////

	class CBRenderEffect : public RenderEffect 
	{ 
	public:
		XPColorBalanceParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		CBRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPColorBalanceParam*>(pParam);
		}
	};

	/////////////////////////////////////////

	class CBRenderEffect_2 : public RenderEffect 
	{ 
	public:
		XPColorBalanceParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		CBRenderEffect_2();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPColorBalanceParam*>(pParam);
		}
	};

	//////////////////////////////////////////

	class RBRenderEffect : public RenderEffect
	{
	public:
		XPRadialBlurParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		RBRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPRadialBlurParam*>(pParam);
		}
	};

	/////////////////////////////////////////////////////

	class EPRenderEffect : public RenderEffect 
	{ 
	public:
		XPEmbossParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		EPRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPEmbossParam*>(pParam);
		}
	};

	////////////////////////////

	class GPRenderEffect : public RenderEffect
	{
	public:
		XPGrayParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		GPRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPGrayParam*>(pParam);
		}
	};

	/////////////////////////////////////////

	class ALRenderEffect : public RenderEffect
	{
	public:
		XPAlphaParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		ALRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPAlphaParam*>(pParam);
		}
	};

	/////////////////////////////////////////

	class DistortionRenderEffect : public RenderEffect 
	{ 
	public:
		XPDistortionShaderParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		DistortionRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPDistortionShaderParam*>(pParam);
		}
	};

	/////////////////////////////////////////

	class DyeRenderEffect : public RenderEffect 
	{ 
	public:
		XPDyeShaderParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		DyeRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void SetTexture(IDirect3DDevice9* ifDevice, const char* name, const void* pParam);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPDyeShaderParam*>(pParam);
		}
	};
	/////////////////////////////////////////

	class FontOutlineRenderEffect : public RenderEffect 
	{ 
	public:
		XPFontOutlineParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		FontOutlineRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPFontOutlineParam*>(pParam);
		}
	};

	//////////////////////////////////////////

	class GaussianBlurBlendRenderEffect : public RenderEffect 
	{ 
	public:
		XPGaussianBlurBlendParam param;
	private:
		virtual void GetShaderStr(int colorOpType, std::string& code);
	public:
		GaussianBlurBlendRenderEffect();
		virtual void SetParameter(IDirect3DDevice9* ifDevice);
		virtual void ChangeShaderParam(const void* pParam)
		{
			param = *static_cast<const XPGaussianBlurBlendParam*>(pParam);
		}
	};

	//////////////////////////////////////////

};