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

		static LPCSTR s_lpPSDistortion; //����ĳЩ������Ч����
		static LPCSTR s_lpPSOutline;	//��ȡͼ���Ե����
		static LPCSTR s_lpPSDye; //Ⱦɫ		

		static LPCSTR s_lpPSGaussianBlurBlend;	//r=0.849�ĸ�˹ģ��֮�������
						//���ģʽ����photoshop�е�ͼ�����ģʽ,Ŀǰʵ�������еļ���,���磺��⡢ǿ���..	

		//alphaͼ..����alpha��Ϊ0�����ص�
		static LPCSTR s_lpPSAlphaPic;

	private:
		FXCode(void) {};
	public:
		~FXCode(void) {};

	public:
		static void GetColorBanlanceShaderStr(int colorOpType, std::string &sourceCode);
		static void GetColorBanlanceShaderStr_2(int colorOpType, std::string &sourceCode); //�����ͼʹ��
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