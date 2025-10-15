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

		static LPCSTR s_lpPSDistortion; //����ĳЩ������Ч����
		static LPCSTR s_lpPSOutline;	//��ȡͼ���Ե����

		static LPCSTR s_lpPSGaussianBlurBlend;	//r=0.849�ĸ�˹ģ��֮�������
						//���ģʽ����photoshop�е�ͼ�����ģʽ,Ŀǰʵ�������еļ���,���磺��⡢ǿ���..	

		//alphaͼ..����alpha��Ϊ0�����ص�
		static LPCSTR s_lpPSAlphaPic;

		static LPCSTR s_lpPS2TextureBlend;

	private:
		FXCode(void) {};
	public:
		~FXCode(void) {};

	public:
		static void GetColorBanlanceShaderStr(int colorOpType, std::string &sourceCode);
		static void GetColorBanlanceShaderStr_2(int colorOpType, std::string &sourceCode); //�����ͼʹ��
		static void GetRadialblurShaderStr(std::string &sourceCode){ sourceCode = s_lpPSRadialBlur;/*����ģ��*/}
		static void GetEmbosspicShaderStr(std::string &sourceCode){ sourceCode = s_lpPSEmbossPic;/*����Ч��*/}
		static void GetGrayShaderStr(std::string &sourceCode){ sourceCode = s_lpPSGrayPic; }

		static void GetDistortionShaderStr(std::string &sourceCode){ sourceCode = s_lpPSAlphaPic; }
		static void GetOutlineShaderStr(std::string &sourceCode){ sourceCode = s_lpPSDistortion; }

		static void GetGaussianBlurBlendShaderStr(std::string &sourceCode){ sourceCode = s_lpPSOutline; }

		static void GetAlphaPicShaderStr(std::string &sourceCode){ sourceCode = s_lpPSGaussianBlurBlend; }

		static void Get2TextureBlendStr(std::string &sourceCode){ sourceCode = s_lpPS2TextureBlend; }
	};
};

#endif