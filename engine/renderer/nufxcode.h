#ifndef __Nuclear_fxcode_H__
#define __Nuclear_fxcode_H__

#include "../common/nuxptypes.h"
#include "nuibaserenderer.h"
#include "../common/nuport.h"
namespace Nuclear
{	

	class FXCode
	{
	private:
		static LPCSTR s_lpPSColorOps[XPTOP_TYPE_COUNT];
		static LPCSTR s_lpPSGrayPic;
		static LPCSTR s_lpPSColorBalanceBegin;
		static LPCSTR s_lpPSColorBalanceEnd;
		static LPCSTR s_lpPSColorBalanceEnd_2;
		static LPCSTR s_lpPSEmbossPic;
		static LPCSTR s_lpPSRadialBlur;

		static LPCSTR s_lpPSDistortion; //伴随某些攻击特效产生
		static LPCSTR s_lpPSOutline;	//提取图像边缘轮廓

		static LPCSTR s_lpPSGaussianBlurBlend;	//r=0.849的高斯模糊之后做混合
						//混合模式参照photoshop中的图层叠加模式,目前实现了其中的几种,比如：柔光、强光等..	

		//alpha图..所有alpha不为0的像素点
		static LPCSTR s_lpPSAlphaPic;

		static LPCSTR s_lpPS2TextureBlend;

	private:
		FXCode(void) {};
	public:
		~FXCode(void) {};

	public:
		static void GetColorBanlanceShaderStr(int colorOpType, std::string &sourceCode);
		static void GetColorBanlanceShaderStr_2(int colorOpType, std::string &sourceCode); //特殊地图使用
		static void GetRadialblurShaderStr(std::string &sourceCode){ sourceCode = s_lpPSRadialBlur;/*径向模糊*/}
		static void GetEmbosspicShaderStr(std::string &sourceCode){ sourceCode = s_lpPSEmbossPic;/*浮雕效果*/}
		static void GetGrayShaderStr(std::string &sourceCode){ sourceCode = s_lpPSGrayPic; }

		static void GetDistortionShaderStr(std::string &sourceCode){ sourceCode = s_lpPSAlphaPic; }
		static void GetOutlineShaderStr(std::string &sourceCode){ sourceCode = s_lpPSDistortion; }

		static void GetGaussianBlurBlendShaderStr(std::string &sourceCode){ sourceCode = s_lpPSOutline; }

		static void GetAlphaPicShaderStr(std::string &sourceCode){ sourceCode = s_lpPSGaussianBlurBlend; }

		static void Get2TextureBlendStr(std::string &sourceCode){ sourceCode = s_lpPS2TextureBlend; }
	};
};

#endif