#pragma once

#include "..\common\xptypes.h"
#include "ibaserenderer.h"
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
		static LPCSTR s_lpPSDye; //染色		

		static LPCSTR s_lpPSGaussianBlurBlend;	//r=0.849的高斯模糊之后做混合
						//混合模式参照photoshop中的图层叠加模式,目前实现了其中的几种,比如：柔光、强光等..	

		//alpha图..所有alpha不为0的像素点
		static LPCSTR s_lpPSAlphaPic;

	private:
		FXCode(void) {};
	public:
		~FXCode(void) {};

	public:
		static void GetColorBanlanceShaderStr(int colorOpType, std::string &sourceCode);
		static void GetColorBanlanceShaderStr_2(int colorOpType, std::string &sourceCode); //特殊地图使用
		static void GetRadialblurShaderStr(std::string &sourceCode);
		static void GetEmbosspicShaderStr(std::string &sourceCode);
		static void GetGrayShaderStr(std::string &sourceCode);

		static void GetDistortionShaderStr(std::string &sourceCode);
		static void GetOutlineShaderStr(std::string &sourceCode);
		static void GetDyeShaderStr(std::string &sourceCode);
		
		static void GetGaussianBlurBlendShaderStr(std::string &sourceCode);

		static void GetAlphaPicShaderStr(std::string &sourceCode);
	};

};