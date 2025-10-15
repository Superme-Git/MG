#include "../common/nuxptypes.h"
#include "nufxcode.h"

namespace Nuclear
{
	LPCSTR FXCode::s_lpPSColorOps[] =
	{
		"		fResult = fResult*input.c0;								\n"
		,//0
		"		input.c0.xyz = 1.0f;									\n"
		"		fResult = fResult*input.c0;								\n"
		,//1
		"		fResult.xyz = 1.0f;										\n"
		"		fResult = fResult*input.c0;								\n"
		,//2
		"		fResult.xyz = fResult.xyz + input.c0.xyz;				\n"
		"		saturate(fResult.xyz);									\n"
		"		fResult.w = fResult.w * input.c0.w;						\n"
		,//3
		"		fResult.xyz = fResult.xyz - input.c0.xyz;				\n"
		"		saturate(fResult.xyz);									\n"
		"		fResult.w = fResult.w * input.c0.w;						\n"
		,//4
		"		fResult.xyz = fResult.xyz + input.c0.xyz - fResult.xyz * input.c0.xyz;	\n"
		"		saturate(fResult.xyz);									\n"
		"		fResult.w = fResult.w * input.c0.w;						\n"
		,//5
		"		fResult.xyz = fResult.xyz + input.c0.xyz;				\n"
		"		fResult.xyz = fResult.xyz - 0.5f;						\n"
		"		saturate(fResult.xyz);									\n"
		"		fResult.w = fResult.w * input.c0.w;						\n"
		,//6
		"		fResult = fResult*input.c0; 	//D3DTOP_MODULATE2X 	\n"
		"		fResult.xyz = fResult.xyz *2;							\n"
		//7
	};

	//椭圆区域ry = 2.0f/3.0f*rx = 250 , rx = 375
	//过渡区域rx0 = 93.75---375
	LPCSTR FXCode::s_lpPSColorBalanceBegin = 
		"sampler2D g_renderTex;											\n"
		"float g_param[8];												\n"

		"struct PS_INPUT												\n"
		"{																\n"
		"   float2 vpos: VPOS;											\n"
		"	float4 c0: COLOR0;											\n"
		"	float2 TexCoord: TEXCOORD0;									\n"
		"};																\n"

		"struct PS_OUTPUT												\n"
		"{																\n"
		"	float4 cr: COLOR;											\n"
		"};																\n"

		"PS_OUTPUT main( PS_INPUT input )								\n"
		"{																\n"
		"	PS_OUTPUT output = (PS_OUTPUT)0;							\n"
		"	float4 fResult = tex2D(g_renderTex, input.TexCoord);		\n";



	LPCSTR FXCode::s_lpPSColorBalanceEnd = 	

		"	float fx = (input.vpos.x - g_param[6])/1.5f;							\n"
		"	float fy = input.vpos.y - g_param[7];									\n"

		"	float4 fvalue = float4(0.5f, 1.0f, 1.3333f, 250.0f);					\n"
		"	float fdis = sqrt(fx*fx + fy*fy)/fvalue.w;								\n"
		"	float fTemp = 0.25f;													\n"
		"	if(fdis<fTemp)															\n"
		"		fdis = fTemp;														\n"

		"if( fdis < fvalue.y)														\n"
		"{																			\n"			
		"	g_param[0] = fvalue.z*g_param[0]*(fdis-fTemp);							\n"	
		"	g_param[1] = fvalue.z*g_param[1]*(fdis-fTemp);							\n"	
		"	g_param[2] = fvalue.z*g_param[2]*(fdis-fTemp);							\n"	
		"	g_param[3] = fvalue.z*g_param[3]*(fdis-fTemp);							\n"	
		"	g_param[4] = fvalue.z*(g_param[4]-fvalue.y)*(fdis-fTemp) +fvalue.y;		\n"
		"	g_param[5] = fvalue.z*g_param[5]*(fdis-fTemp);							\n"
		"}																			\n"

		"	float4 fDeltacolor;													\n"
		"	fDeltacolor.x = (fvalue.y-fResult.x)*g_param[0];					\n"
		"	fResult.x += fDeltacolor.x;											\n"
		"	fResult.y -= fDeltacolor.x*fvalue.x;								\n"
		"	fResult.z -= fDeltacolor.x*fvalue.x;   								\n"
		"	saturate(fResult.xyz);												\n"

		"	fDeltacolor.y = (fvalue.y-fResult.y)*g_param[1]; 					\n"
		"	fResult.y += fDeltacolor.y;											\n"
		"	fResult.x -= fDeltacolor.y*fvalue.x;								\n"
		"	fResult.z -= fDeltacolor.y*fvalue.x;   								\n"
		"	saturate(fResult.xyz);												\n"

		"	fDeltacolor.z = (fvalue.y-fResult.z)*g_param[2];					\n"
		"	fResult.z += fDeltacolor.z;											\n"
		"	fResult.x -= fDeltacolor.z*fvalue.x;								\n"
		"	fResult.y -= fDeltacolor.z*fvalue.x; 								\n"
		"	saturate(fResult.xyz);												\n"

		"	fResult += float4(g_param[3], g_param[3], g_param[3], 0);			\n"
		"	saturate(fResult.xyz);												\n"
		"	fResult.xyz = g_param[4]*fResult.xyz +g_param[5];					\n"
		"	saturate(fResult.xyz);												\n"

		"	output.cr = fResult;												\n"
		"	return output;														\n"
	"};																			\n";

	//椭圆区域ry = 2.0f/3.0f*rx = 250 , rx = 375
	//过渡区域rx0 = 93.75---375
	//从s_lpPSColorBalanceEnd修改而来的 *_2
	//前者不处理椭圆内区域，后者只处理椭圆内区域。
	//目前游戏中，前者用于普通地图的天黑效果，主角周围区域不处理。
	//后者用于地下宫殿一类的地图，让主角周围区域变亮，其它区域不处理。

	LPCSTR FXCode::s_lpPSColorBalanceEnd_2 = 

		"	float fx = (input.vpos.x - g_param[6])/1.5f;							\n"
		"	float fy = input.vpos.y - g_param[7];									\n"

		"	float4 fvalue = float4(0.5f, 1.0f, -1.3333f, 250.0f);					\n"
		"	float fTemp = 0.25f;													\n"
		"	float fdis = sqrt(fx*fx + fy*fy)/fvalue.w;								\n"
		"	if(fdis<fTemp)															\n"
		"		fdis = fTemp;														\n"
		"	if(fdis>fvalue.y)														\n"
		"		fdis = fvalue.y;													\n"
	
		"	g_param[0] = fvalue.z*g_param[0]*(fdis-fvalue.y);						\n"	
		"	g_param[1] = fvalue.z*g_param[1]*(fdis-fvalue.y);						\n"	
		"	g_param[2] = fvalue.z*g_param[2]*(fdis-fvalue.y);						\n"	
		"	g_param[3] = fvalue.z*g_param[3]*(fdis-fvalue.y);						\n"	
		"	g_param[4] = fvalue.z*(g_param[4]-fvalue.y)*(fdis-fvalue.y) +fvalue.y;	\n"
		"	g_param[5] = fvalue.z*g_param[5]*(fdis-fvalue.y);						\n"

		"	float4 fDeltacolor;													\n"
		"	fDeltacolor.x = (fvalue.y-fResult.x)*g_param[0];					\n"
		"	fResult.x += fDeltacolor.x;											\n"
		"	fResult.y -= fDeltacolor.x*fvalue.x;								\n"
		"	fResult.z -= fDeltacolor.x*fvalue.x;   								\n"
		"	saturate(fResult.xyz);												\n"

		"	fDeltacolor.y = (fvalue.y-fResult.y)*g_param[1]; 					\n"
		"	fResult.y += fDeltacolor.y;											\n"
		"	fResult.x -= fDeltacolor.y*fvalue.x;								\n"
		"	fResult.z -= fDeltacolor.y*fvalue.x;   								\n"
		"	saturate(fResult.xyz);												\n"

		"	fDeltacolor.z = (fvalue.y-fResult.z)*g_param[2];					\n"
		"	fResult.z += fDeltacolor.z;											\n"
		"	fResult.x -= fDeltacolor.z*fvalue.x;								\n"
		"	fResult.y -= fDeltacolor.z*fvalue.x; 								\n"
		"	saturate(fResult.xyz);												\n"

		"	fResult += float4(g_param[3], g_param[3], g_param[3], 0);			\n"
		"	saturate(fResult.xyz);												\n"
		"	fResult.xyz = g_param[4]*fResult.xyz +g_param[5];					\n"
		"	saturate(fResult.xyz);												\n"

		"	output.cr = fResult;												\n"
		"	return output;														\n"
		"};																		\n";


	LPCSTR FXCode::s_lpPSGrayPic = 
		"sampler2D Texture0;												\n"
		"int g_nType;														\n"
		"float4 main( float2 texCoord  : TEXCOORD0, float2 vpos:VPOS ) : COLOR	\n"
		"{																	\n"
		"	float4 cr;														\n"
		"	cr = tex2D( Texture0, texCoord );								\n"
		"	float fline = vpos.x+vpos.y;				\n"
		"	float4 fValue = float4(200, 400, 600, 800);	\n"

		"	if(fline > fValue.x)						\n"
		"		g_nType = 1;							\n"
		"	if(fline > fValue.y)						\n"
		"		g_nType = 2;							\n"
		"	if(fline > fValue.z)						\n"
		"		g_nType = 3;							\n"

		"	if(fline > fValue.w)						\n"
		"		g_nType = 4;							\n"

		"	float fx = vpos.x-500;						\n"
		"	float fy = vpos.y-400;						\n"
		"	float fdis = sqrt(fx*fx + fy*fy);			\n"
		"	if(fdis<300)			\n"
		"		g_nType = 5;		\n"
		"	if(fdis<200)			\n"
		"		g_nType = 6;		\n"
		"	if(g_nType==0)													\n"
		"		cr = mul(cr.xyz, float3(0.3, 0.59, 0.11));					\n"
		"	if(g_nType==1)													\n"
		"	{																\n"
		"		cr.y = 0;													\n"
		"		cr.z = 0;													\n"
		"	}																\n"
		"	if(g_nType==2)													\n"
		"	{																\n"
		"		cr.x = 0;													\n"
		"		cr.z = 0;													\n"
		"	}																\n"
		"	if(g_nType==3)													\n"
		"	{																\n"
		"		cr.x = 0;													\n"
		"		cr.y = 0;													\n"
		"	}																\n"
		"	if(g_nType==4)													\n"
		"	{																\n"
		"		cr.x = 0;													\n"
		"	}																\n"
		"	if(g_nType==5)													\n"
		"	{																\n"
		"		cr.y = 0;													\n"
		"	}																\n"
		"	if(g_nType==6)													\n"
		"	{																\n"
		"		cr.z = 0;													\n"
		"	}																\n"
		"	return cr;														\n"
		"};																	\n";


	LPCSTR FXCode::s_lpPSEmbossPic = 
		"sampler2D g_renderTex;										\n"	
		"float g_param[4];											\n"	

		"struct PS_INPUT											\n"	
		"{															\n"	
		"	float2 TexCoord: TEXCOORD;								\n"	
		"};															\n"	

		"struct PS_OUTPUT											\n"	
		"{															\n"	
		"	float4 cr: COLOR;										\n"	
		"};															\n"	

		"PS_OUTPUT main( PS_INPUT input )							\n"	
		"{															\n"	
		"	PS_OUTPUT output = (PS_OUTPUT)0;						\n"	
		"	float4 fResult = tex2D(g_renderTex, input.TexCoord);	\n"	

		"	float g_fSizex = g_param[0];								\n"	
		"	float g_fSizey = g_param[1];								\n"	
		"	float g_offsetx = g_param[2];								\n"	
		"	float g_offsety = g_param[3];								\n"	

		"	float2 delta = float2(g_fSizex * g_offsetx, g_fSizey * g_offsety);		\n"	
		"	fResult -= tex2D(g_renderTex, input.TexCoord + delta );					\n"	
		"	output.cr = fResult;//+0.5f												\n"
		"	return output;															\n"
		"};																			\n";

	LPCSTR FXCode::s_lpPSRadialBlur = 
		"sampler2D g_renderTex;											\n"
		"float g_param[3];												\n"	
		"//float g_fBlurCenterx;										\n"
		"//float g_fBlurCentery;										\n"
		"//float g_fBlurDegree;											\n"

		"struct PS_INPUT												\n"
		"{																\n"
		"	float2 TexCoord: TEXCOORD0;									\n"
		"};																\n"

		"struct PS_OUTPUT												\n"
		"{																\n"
		"	float4 cr: COLOR;											\n"
		"};																\n"

		"PS_OUTPUT main( PS_INPUT input )								\n"
		"{																\n"
		"	PS_OUTPUT output = (PS_OUTPUT)0;							\n"

		"	float g_fBlurCenterx = g_param[0];							\n"
		"	float g_fBlurCentery = g_param[1];							\n"
		"	float g_fBlurDegree = g_param[2];							\n"

		"	if(g_fBlurDegree<0.001f)									\n"
		"	{															\n"
		"		output.cr = tex2D(g_renderTex, input.TexCoord);			\n"
		"	}															\n"
		"	else														\n"
		"	{															\n"
		"		float2 A = float2(g_fBlurCenterx, g_fBlurCentery);		\n"
		"		float2 fxfy = A - input.TexCoord;						\n"
		"		float fRad = length(fxfy);      						\n"
		"		float2 fSinCos = fxfy/fRad;								\n"
		"		int count = 0;											\n"
		"		float4 fSumResult = float4(0,0,0,0);					\n"

		"		float i=0;												\n"
		"		float fblurdis_test = 0.04f;							\n"
		"		if(fRad<0.02)											\n"
		"		{														\n"
		"			fblurdis_test = 0.012;								\n"
		"			for(i=-0.5*fblurdis_test; i<0.5*fblurdis_test; i+= 0.005f) //fBlurDis 	\n"
		"			{																		\n"
		"				float2 kxy = fSinCos *i;											\n"
		"				float2 xy = input.TexCoord - kxy;									\n"
		"				//saturate(xy);														\n"
		"				fSumResult += tex2D(g_renderTex, xy);								\n"
		"				count++;															\n"
		"			}      																	\n"
		"		}																			\n"
		"		else																		\n"
		"		{																			\n"
		"			for(i=-0.5*fblurdis_test; i<0.5*fblurdis_test; i+= 0.003f) //fBlurDis 	\n"
		"			{																		\n"
		"				float2 kxy = fSinCos *i;											\n"
		"				float2 xy = input.TexCoord - kxy;									\n"
		"				//saturate(xy);														\n"
		"				fSumResult += tex2D(g_renderTex, xy);								\n"
		"				count++;															\n"
		"			}      																	\n"
		"		}																			\n"
		"		output.cr = fSumResult/count;												\n"	
		"	}																				\n"

		"	return output;																	\n"
		"};																					\n";

	//扭曲特效
	//	float fdegree = 0.6f; //扭曲程度----每帧衰减这个值
	//	float2 fcent = float2(0.5f, 0.5f); //扭曲中心
	//	float fMaxDis = 0.6f; //扭曲范围
	//	int nType1 = 1; //控制方向
	//	int nType2 = 1; //控制形状
	//如上参数,按照注释顺序存放在g_param---g_nType中

	LPCSTR FXCode::s_lpPSDistortion = 
		"sampler2D g_renderTex;										\n"	
		"float g_param[4];											\n"	
		"int g_nType[2];											\n"	

		"struct PS_INPUT											\n"	
		"{															\n"	
		"	float4 c0: COLOR0;										\n"	
		"	float2 TexCoord: TEXCOORD;								\n"	
		"};															\n"	

		"struct PS_OUTPUT											\n"	
		"{															\n"	
		"	float4 cr: COLOR;										\n"	
		"};															\n"	

		"PS_OUTPUT main( PS_INPUT input )							\n"	
		"{															\n"	
		"	PS_OUTPUT output = (PS_OUTPUT)0;						\n"	
		"	float4 fResult = tex2D(g_renderTex, input.TexCoord);	\n"	

		"	float fdegree = g_param[0];									\n"	
		"	float2 fcent = float2(g_param[1], g_param[2]); //扭曲中心	\n"	
		"	float fMaxDis = g_param[3];									\n"	
		"	int nType1 = g_nType[0]; //控制方向							\n"	
		"	int nType2 = g_nType[1]; //控制形状							\n"	

		"	float fpai = 3.1415926f; //常数								\n"	

		"	float dis = distance(input.TexCoord, float2(g_param[1], g_param[2])); \n"	

		"	if(dis > fMaxDis)											\n"	
		"	{															\n"	
		"	}															\n"	
		"	else														\n"	
		"	{															\n"	
		"		float2 texpos;											\n"	
		"		int nCoef = 0;											\n"	
		"		if(nType1 == 1)											\n"	
		"			nCoef = 1;											\n"	
		"		if(nType1 == 2)											\n"	
		"			nCoef = -1;											\n"	

		"		float offsetx = 0.0f;									\n"	
		"		float offsety = 0.0f;									\n"	

		"		if(nType2 == 1)											\n"	
		"		{														\n"	
		"			offsetx = sin( 2.0f*fpai*input.TexCoord.x );		\n"	
		"			offsety = sin( 2.0f*fpai*input.TexCoord.y );		\n"	
		"		}														\n"	
		"		if(nType2 == 2)											\n"	
		"		{														\n"	
		"			offsetx = sin( 2.0f*fpai*input.TexCoord.y );		\n"	
		"			offsety = cos( 2.0f*fpai*input.TexCoord.x );		\n"	
		"		}														\n"	

		"		texpos.x = input.TexCoord.x+ nCoef* pow((fMaxDis-dis)*fdegree, 2.0f)*offsetx;  \n"	  
		"		texpos.y = input.TexCoord.y+ nCoef* pow((fMaxDis-dis)*fdegree, 2.0f)*offsety;  \n"	

		"		clamp(texpos, 0.0f, 1.0f);											\n"	
		"		fResult = tex2D(g_renderTex, texpos);								\n"	
		"	}																		\n"	
		"	fResult.w = fResult.w * input.c0.w;										\n"	
		"	output.cr = fResult;													\n"	
		"	return output;															\n"
		"};																			\n";

		//pixelsize要准确传入所创建的纹理的宽高。(可通过GetPictureInfo()获得)
		LPCSTR FXCode::s_lpPSOutline = 
			"sampler2D g_renderTex;										\n"	
			"struct PS_INPUT											\n"	
			"{															\n"	
			"	float4 c0: COLOR0;										\n"	
			"	float2 TexCoord: TEXCOORD0;								\n"	
			"};															\n"	

			"struct PS_OUTPUT											\n"	
			"{															\n"	
			"	float4 cr: COLOR;										\n"	
			"};															\n"	

			"float g_param[6];											\n"
			"//float g_samplesIndex[16];								\n"

			"PS_OUTPUT main(PS_INPUT input)									\n"	
			"{																\n"	
			"	PS_OUTPUT output = (PS_OUTPUT) 0;							\n"	

			"	float4 outlineColor = float4(g_param[0], g_param[1], g_param[2], g_param[3]); \n"	
			"	float2 pixelsize = float2(g_param[4], g_param[5]);			\n"

			"	float2 samplesIndex[8] =									\n"	
			"	{															\n"	
			"		-1.0f, -1.0f,											\n"	
			"		0.0f,  -1.0f,											\n"	
			"		1.0f,  -1.0f,											\n"	
			"		1.0f,  0.0f,											\n"	
			"		1.0f,  1.0f,											\n"	
			"		0.0f,  1.0f,											\n"	
			"		-1.0f, 1.0f,											\n"	
			"		-1.0f, 0.0f,											\n"	
			"	};															\n"	

			"	float4 fResult = tex2D(g_renderTex, input.TexCoord);		\n"
			"	float4 fResult_temp = fResult;								\n"

			"	fResult = -9.0f* fResult.w;									\n"
			"	float4 temp = 0.0f;											\n"
			"	for(int i=0; i<8; i++){										\n"
			"		temp = tex2D(g_renderTex, input.TexCoord+ pixelsize* samplesIndex[i]);\n"
			"		fResult += temp.w;										\n"	
			"	}															\n"
			"	clamp(fResult, 0.0f, 1.0f);									\n"
			"	fResult *= outlineColor;									\n"

			"//	temp = fResult_temp.w * float4(0.0f, 1.0f, 0.0f, 0.0f);		\n"
			"//	fResult += temp;											\n"

			"//	fResult.w = fResult_temp.w;									\n"

			"	output.cr = fResult;										\n"	
			"	return output;												\n"	
			"};																\n";
		
		//输入参数--纹理像素size: g_param[2]、叠加模式g_nBlendType..0柔光 1强光

		LPCSTR FXCode::s_lpPSGaussianBlurBlend = 
			"sampler2D g_renderTex;										\n"	
			"struct PS_INPUT											\n"	
			"{															\n"	
			"	float4 c0: COLOR0;										\n"	
			"	float2 TexCoord: TEXCOORD0;								\n"	
			"};															\n"	

			"struct PS_OUTPUT											\n"	
			"{															\n"	
			"	float4 cr: COLOR;										\n"	
			"};															\n"	

			"float g_param[2];											\n"
			"int g_nBlendType;											\n"

			"PS_OUTPUT main(PS_INPUT input)									\n"	
			"{																\n"	
			"	PS_OUTPUT output = (PS_OUTPUT) 0;							\n"	
			"	float2 pixelsize = float2(g_param[0], g_param[1]);			\n"


			"	float2 samplesIndex[9] =									\n"	
			"	{															\n"	
			"		-1.0f,  -1.0f,											\n"	
			"		 0.0f,  -1.0f,											\n"	
			"		 1.0f,  -1.0f,											\n"	
			"		-1.0f,   0.0f,											\n"	
			"		 0.0f,   0.0f,											\n"	
			"		 1.0f,   0.0f,											\n"	
			"		-1.0f,   1.0f,											\n"	
			"		 0.0f,   1.0f,											\n"	
			"		 1.0f,   1.0f,											\n"	
			"	};															\n"	

			"float gaussianValue[9] = {0.062467, 0.125000, 0.062467,		\n"
			"						   0.125000, 0.250131, 0.125000,		\n"
			"						   0.062467, 0.125000, 0.062467,};		\n"

			"	float4 colorA = 0;											\n"
			"	for(int i=0; i<9; i++){										\n"
			"		colorA += gaussianValue[i]*tex2D(g_renderTex, input.TexCoord+ pixelsize* samplesIndex[i]);\n"		
			"	}																		\n"

			"	float4 colorC = colorA;													\n"
			"	float4 colorB = tex2D(g_renderTex, input.TexCoord);						\n"

			"	if(g_nBlendType == 0)													\n"
			"	{																		\n"
			"		if(colorA.x <= 0.5f)												\n"
			"			colorC.x = (2.0f*colorA.x - 1.0f)*(colorB.x - colorB.x*colorB.x) +colorB.x;	\n"
			"		else																\n"
			"			colorC.x = (2.0f*colorA.x - 1.0f)*(sqrt(colorB.x) - colorB.x) +colorB.x;	\n"

			"		if(colorA.y <= 0.5f)												\n"
			"			colorC.y = (2.0f*colorA.y - 1.0f)*(colorB.y - colorB.y*colorB.y) +colorB.y;	\n"
			"		else																\n"
			"			colorC.y = (2.0f*colorA.y - 1.0f)*(sqrt(colorB.y) - colorB.y) +colorB.y;	\n"

			"		if(colorA.z <= 0.5f)												\n"
			"			colorC.z = (2.0f*colorA.z - 1.0f)*(colorB.z - colorB.z*colorB.z) +colorB.z;	\n"
			"		else																\n"
			"			colorC.z = (2.0f*colorA.z - 1.0f)*(sqrt(colorB.z) - colorB.z) +colorB.z;	\n"

			"	}																		\n"

			"	if(g_nBlendType == 1)													\n"
			"	{																		\n"
			"		if(colorA.y <= 0.5f)												\n"
			"			colorC = 2.0f*colorA*colorB;									\n"
			"		else																\n"
			"			colorC = 1.0f - 2.0f*(1.0f - colorA)*(1.0f - colorB);			\n"
			"	}																		\n"

			"	output.cr = colorC;														\n"	
			"	return output;															\n"	
			"};																			\n";


		LPCSTR FXCode::s_lpPSAlphaPic = 
			"sampler2D Texture0;												\n"
			"float4 main( float2 texCoord  : TEXCOORD0) : COLOR	\n"
			"{																	\n"
			"	float4 cr;														\n"
			"	cr = tex2D( Texture0, texCoord );								\n"
			
			"	if(cr.w > 0.0f)													\n"
			"	{																\n"
			"		cr.r = 1.0f;												\n"
			"		cr.w = 1.0f;												\n"
			"	}																\n"
			"	return cr;														\n"
			"};																	\n";


	LPCSTR FXCode::s_lpPS2TextureBlend = 
		"sampler2D g_renderTex1;									\n"	
		"sampler2D g_renderTex2;									\n"	
		"float g_fcoef;												\n"	

		"struct PS_INPUT											\n"	
		"{															\n"	
		"	float2 TexCoord0: TEXCOORD0;							\n"	
		"	float2 TexCoord1: TEXCOORD1;							\n"	
		"};															\n"	

		"struct PS_OUTPUT											\n"	
		"{															\n"	
		"	float4 cr: COLOR;										\n"	
		"};															\n"	

		"PS_OUTPUT main( PS_INPUT input )							\n"	
		"{															\n"	
		"	PS_OUTPUT output = (PS_OUTPUT)0;						\n"	
		"	float4 fResult1 = tex2D(g_renderTex1, input.TexCoord0);	\n"	
		"	float4 fResult2 = tex2D(g_renderTex2, input.TexCoord1);	\n"	

		"	output.cr = fResult1 *g_fcoef + fResult2 *(1.0f-g_fcoef);	\n"
		"	return output;											\n"
		"};															\n";

	void FXCode::GetColorBanlanceShaderStr(int colorOpType, std::string &sourceCode)
	{
		sourceCode = s_lpPSColorBalanceBegin;
		sourceCode += s_lpPSColorOps[colorOpType];
		sourceCode += s_lpPSColorBalanceEnd;
	}

	void FXCode::GetColorBanlanceShaderStr_2(int colorOpType, std::string &sourceCode)
	{
		sourceCode = s_lpPSColorBalanceBegin;
		sourceCode += s_lpPSColorOps[colorOpType];
		sourceCode += s_lpPSColorBalanceEnd_2;
	}

};