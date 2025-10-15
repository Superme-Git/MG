#include <windows.h>
#include <stdio.h>
#include <tchar.h>
#include <string>
//Assert
#include <assert.h>
//STL
#include <vector>
#include <map>
#include <list>
#include <set>
#include <deque>
#include <hash_map>

#include <d3d9types.h>
#include <stdio.h>
#include <tchar.h>
#include "..\engine\renderer\renderer.h"
#include "..\engine\common\timer.h"
#include "..\engine\common\log.h"
#include "..\engine\common\util.h"
#include "..\engine\common\fileiomanager.h"
#include "../gxwindow/gxw.h"
#include "../gxwindow/projects/myframe.h"
#include "GXWRenderer.h"

#include "..\engine\renderer\videotexture.h"

//#include "..\engine\renderer\radialblur.h"

//#include "vld.h"

#include <float.h>
using namespace Nuclear;

VideoTexture* g_pdshowTexRender = NULL;

FileIOManager *g_pFileIOMan = NULL;
Renderer *pRenderer = NULL;
GXWRENDER::CGXWRenderer* g_pGXWRenderer = NULL;

IParticleManager* g_pParticleManager = NULL;

CMyFrame*					m_frame;
CMyButtonClickNotify		m_notify;

//void *testbuf= NULL;
Nuclear::PictureHandle h1;
Nuclear::PictureHandle h2;
Nuclear::PictureHandle h3;
Nuclear::PictureHandle h4;
Nuclear::PictureHandle h5;

Nuclear::PictureHandle h21;
Nuclear::PictureHandle h22;
Nuclear::PictureHandle h23;
Nuclear::PictureHandle h24;

ParticleSystemHandle g_pshandle1;
ParticleSystemHandle g_pshandle2;
ParticleSystemHandle g_pshandle3;
ParticleSystemHandle g_pshandle4;
ParticleSystemHandle g_pshandle5;
ParticleSystemHandle g_pshandle6;
ParticleSystemHandle g_pshandle7;

class Perf	//先放在这里吧。。。
{
	LARGE_INTEGER s;
	LARGE_INTEGER total;
	int count;
public:	
	Perf()
	{
		total.QuadPart = 0LL;
		count = 0;
	}

	void start()
	{
		QueryPerformanceCounter(&s);
	}

	void stop()
	{
		LARGE_INTEGER e;
		QueryPerformanceCounter(&e);
		total.QuadPart += e.QuadPart - s.QuadPart;
		count++;
	}

	int getCount() const
	{
		return count;
	}

	LARGE_INTEGER getTotal() const
	{
		return total;
	}

	void clean()
	{
		total.QuadPart = 0LL;
		count = 0;
	}
};

BOOL Init(HWND hwnd)
{
	g_pFileIOMan = new FileIOManager();
	g_pFileIOMan->Init(true, true);
	DWORD flag = Nuclear::XPCRF_VERTICALSYNC;
	HRESULT hr = CreateRenderer(&pRenderer, hwnd, Nuclear::XDisplayMode(1024, 768, 32), g_pFileIOMan, flag);
	if( pRenderer == NULL ) return false;
	Nuclear::XDisplayMode mode = pRenderer->GetDisplayMode();
	int iCaptionHeight = GetSystemMetrics(SM_CYCAPTION);
	int iFixedFrameSizeY = GetSystemMetrics(SM_CYFIXEDFRAME);
	int iFixedFrameSizeX = GetSystemMetrics(SM_CXFIXEDFRAME);
	MoveWindow(hwnd, 0, 0, mode.width+2*iFixedFrameSizeX, mode.height+2*iFixedFrameSizeY+iCaptionHeight, true);

	int asdfasf = 0;

	int asdfasfasdfasd = 0;

	pRenderer->InitDShowTextureRenderer(L"c:\\windows\\clock.avi"); //非常好的时钟测试素材！！方便准确定位每一帧！

	g_pdshowTexRender = pRenderer->GetDshowMovieTexRender();

	float faasdfasf = g_pdshowTexRender->GetTotalLength();

	g_pdshowTexRender->SetLoop(true);

	return true;
}

BOOL g_bPlaying = TRUE;

void RenderVideoTexture()
{
	pRenderer->Begin(); //
	FRECT frct(100.0f, 100.0f, 521.0f, 521.0f);

	bool bres = pRenderer->DrawVideoTexture(frct, true, NULL, 0xffffffff);

	FRECT frct1(0.0f, 0.0f, 1024.0f, 768.0f);
	FRECT frct2(60.0f, 80.0f, 800.0f, 600.0f);
	FRECT frct3(300.0f, 300.0f, 1078.0f, 863.0f);
//	pRenderer->DrawPicture(h1, frct1, 0xa100ff00);
//	pRenderer->DrawPicture(h2, frct2, 0x5fff00ff);
//	pRenderer->DrawPicture(h3, frct3, 0xffffffff);

	TCHAR szbuffer[256];
	RECT trect;
	int f1 = (int)(pRenderer->GetFPS()*10);
	swprintf_s(szbuffer, 256, _T("TFPS: %3d.%1d"), f1/10, f1%10);	
	trect.left = 0;
	trect.top = 50;
	trect.right =  500;
	trect.bottom = 300;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xffffffff);
	f1 = (int)(pRenderer->GetLatestFPS()*10);
	swprintf_s(szbuffer, 256, _T("%d, LFPS: %3d.%1d"), g_bPlaying, f1/10, f1%10);
	trect.top += 20;
	trect.bottom += 20;
	pRenderer->DrawText(szbuffer, 15, &trect, 0xffffffff);

	pRenderer->End();
}

void TestDrawPicture1()
{
	static int cnt=0;
	pRenderer->Begin(0xffffffff);
	
	FRECT frct1(0.0f, 0.0f, 1024.0f, 768.0f);
	FRECT frct2(60.0f, 80.0f, 800.0f, 600.0f);
	FRECT frct3(100.0f, 100.0f, 878.0f, 663.0f);
	FRECT frct4(100.0f, 20.0f, 612.0f, 532.0f);
	FRECT frct5(400.0f, 200.0f, 524.0f, 436.0f);
	
	XPColorBalanceParam timeEffectParam;
	timeEffectParam.fCoefr = 0.9f;
	timeEffectParam.fCoefg = 0.9f;
	timeEffectParam.fCoefb = 0.9f;
	timeEffectParam.fLightness = 0.0f;
	timeEffectParam.fContrastk = 1.0f;
	timeEffectParam.fContrastb = 0.0f;
// 
  	timeEffectParam.fCentx = 400.0f;
  	timeEffectParam.fCenty = 400.0f;
	
	//XPGrayParam psParam;
	//psParam.nType = 0;
	//psParam.nColorOpType = 0;

	//pRenderer->SelectRenderEffect(XPRE_COLORBALANCE);//XPRE_COLORBALANCE

//	pRenderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam);
//	timeEffectParam.fCoefr += 0.00001f;
//	timeEffectParam.fCoefr = timeEffectParam.fCoefr>1.0f ? 1.0f:timeEffectParam.fCoefr;

	for(int i=0; i<1; i++)
	{
	//	timeEffectParam.fCoefg += cnt/5000.0f;
		//pRenderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam); //psParam
		pRenderer->DrawPicture(h1, frct1, 0xffffffff);

//  	timeEffectParam.fCoefg += cnt/1000.0f;
//  	pRenderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam);
//  	pRenderer->DrawPicture(h2, frct2, 0xffff00ff);
//  
// 		timeEffectParam.fCoefg -= cnt/1000.0f;
// 		pRenderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam);
// 		pRenderer->DrawPicture(h3, frct3, 0xffffffff);
// 
// 		timeEffectParam.fCoefg += cnt/1000.0f;
// 		pRenderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam);
// 		pRenderer->DrawPicture(h4, frct4, 0xffffffff);
// 
// 		timeEffectParam.fCoefg += cnt/1000.0f;
// 		pRenderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam);
// 		pRenderer->DrawPicture(h5, frct5, 0xffffffff);
	}

	TCHAR szbuffer[256];
	RECT trect;
	int f1 = (int)(pRenderer->GetFPS()*10);
	swprintf_s(szbuffer, 256, _T("TFPS: %3d.%1d"), f1/10, f1%10);	
	trect.left = 0;
	trect.top = 50;
	trect.right =  500;
	trect.bottom = 300;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xffffffff);
	f1 = (int)(pRenderer->GetLatestFPS()*10);
	swprintf_s(szbuffer, 256, _T("LFPS: %3d.%1d"), f1/10, f1%10);
	trect.top += 20;
	trect.bottom += 20;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xffffffff);

	pRenderer->End();
	cnt++;
	if(cnt>90)
		cnt=-100;
}

void TestShaderEffect()
{
	int N = 1;
	double a[3][3];    /* 高斯模板 */
	double r = 7.5;                     /* 高斯半径：[ 0.1, 250] */
	double A=1/(2*3.1415926*r*r);
	int i,j;
	for(i=-1*N; i<=N; i++)
		for(j=-1*N; j<=N; j++)
			a[i+N][j+N] = A*exp((-1)*(i*i+j*j)/ (2*r*r));


	PictureInfo picinfo;

	pRenderer->Begin();
	pRenderer->GetPictureInfo(h21, picinfo);

	float x1,y1,x2,y2;
	x1 = 0.f;
	x2 = x1+ (float)(picinfo.m_nPicWidth);
	y1 = 0.f;
	y2 = y1+ (float)(picinfo.m_nPicHeight);
	FRECT r0(x1, y1, x2, y2);

	FRECT srcRct(0.0f, 0.0f, 1.0f, 1.0f);

	DrawPictureParam paramPic;
	paramPic.handle = h21;
	paramPic.pRect = &r0;
	paramPic.blend = Nuclear::XPTEXBM_COPY;
	paramPic.pSrcrect = &srcRct;

	XPGaussianBlurBlendParam BlurParam;
	BlurParam.nBlendType = 0;
	BlurParam.nTextureWidth = picinfo.m_nPicWidth;
	BlurParam.nTextureHeight = picinfo.m_nPicHeight;

	static int s_cnt=0;
	if(s_cnt%4000 < 3000)
	{
	//	if(s_cnt%4000 < 1000)
	//	{
	//		BlurParam.nBlendType = 1;
	//	}
//		pRenderer->SelectRenderEffect(XPRE_GaussianBlurBlend);
//		pRenderer->SetShaderParam(XPRE_GaussianBlurBlend, &BlurParam);
	}
	else
	{
		pRenderer->SelectRenderEffect(XPRE_NULL);
	}
	
	pRenderer->DrawBatchPicture(paramPic);
	
	s_cnt++;
	
	TCHAR szbuffer[256];
	RECT trect;
	int f1 = (int)(pRenderer->GetFPS()*10);
	swprintf_s(szbuffer, 256, L"TFPS: %3d.%1d", f1/10, f1%10);	
	trect.left = 0;
	trect.top = 50;
	trect.right =  500;
	trect.bottom = 300;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xff00ff00);
	f1 = (int)(pRenderer->GetLatestFPS()*10);
	swprintf_s(szbuffer, 256, L"LFPS: %3d.%1d", f1/10, f1%10);
	trect.top += 20;
	trect.bottom += 20;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xffff0000);

	pRenderer->End();
}

float test()
{
	float sum = 0.0f;
	for(int i=0; i<300000; i++)
	{
		sum += sin(i/57.3f) *cos(i/10.8f);
		sum += cos(i/30.8f)/5.3f;
		sum += powf(0.9, i) *30.9f;
	}

	return sum;
}

void TestDrawBatchPicture()
{
	static int aaaaa = 1;
	static Perf perf1;
	static Perf perf2;
	static Perf perf3;
	static Perf perf4;
	Perf perf11;
	Perf perf21;
	Perf perf31;
	perf2.start();
	perf21.start();
	pRenderer->Begin(0xffff0000);

	FRECT rect;
	rect = FRECT(0, 0, 1024, 768);

	PictureInfo picinfo;
	pRenderer->GetPictureInfo(h1, picinfo);

	float x1,y1,x2,y2;
	x1 = 10.f;
	x2 = x1+ (float)(picinfo.m_nPicWidth);
	y1 = 10.f;
	y2 = y1+ (float)(picinfo.m_nPicHeight);
	FRECT r0(x1, y1, x2, y2);

	FRECT srcRct(0.0f, 0.0f, 1.0f, 1.0f);
	DrawBatchPictureParam paramPic;
	XPMATRIX3 m;
	FPOINT pt(512, 768/2);
	XPMatrixRotationPoint(&m, &pt, 3.14159f/4);
	paramPic.pMatrix = &m;
	paramPic.handle = h1;
	paramPic.pRect = &rect;
	paramPic.rectCount = 1;
	XPCOLOR color = 0xffffffff;
	paramPic.pColor = &color;
	paramPic.colorCount = 1;
	paramPic.blend = Nuclear::XPTEXBM_DEFAULT;
	paramPic.pSrcrect = &srcRct;

	pRenderer->SelectRenderEffect(XPRE_NULL);


	//int tickStrt = (int)GetMilliSeconds();
	float ab = (float)(rand()) / RAND_MAX + 1;
	for(int i=0; i<100; i++)
	{
		XPMatrixRotationPoint(&m, &pt, i*3.14159f/50 * ab);
		perf3.start();
		perf31.start();
		pRenderer->DrawBatchPicture(paramPic);	
		perf3.stop();
		perf31.stop();
	}
	perf21.stop();

	//Sleep(100);
	/*int tickEnd = (int)GetMilliSeconds();
	int nDeltaTime = tickEnd - tickStrt;*/

	TCHAR szbuffer[256];
//	wsprintf(szbuffer, _T("deltaTime: %5d"), nDeltaTime);	
	RECT trect;
// 	trect.left = 0;
// 	trect.top =0;
// 	trect.right =  500;
// 	trect.bottom = 300;
// 	pRenderer->DrawText(szbuffer, 16, &trect, 0xff00ff00);

	int f1 = (int)(pRenderer->GetFPS()*10);
	swprintf_s(szbuffer, 256, L"TFPS: %3d.%1d", f1/10, f1%10);	
	trect.left = 0;
	trect.top = 50;
	trect.right =  500;
	trect.bottom = 300;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xff00ff00);
	f1 = (int)(pRenderer->GetLatestFPS()*10);
	swprintf_s(szbuffer, 256, L"LFPS: %3d.%1d", f1/10, f1%10);
	trect.top += 20;
	trect.bottom += 20;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xffff0000);

	perf4.start();
	float aaaaaaaaaaaa = test();
	perf4.stop();
	perf1.start();
	perf11.start();
	pRenderer->End();
	perf1.stop();
	perf11.stop();

	perf2.stop();


	/*tickEnd = (int)GetMilliSeconds();
	int nTempDeltaTime = tickEnd - tickStrt;*/
	//int aaaa = 0;

	static int a;
	a = perf1.getTotal().QuadPart/perf1.getCount();
	static int b;
	b = perf2.getTotal().QuadPart/perf2.getCount();
	static int c;
	c = perf3.getTotal().QuadPart/perf3.getCount();
	static int d;
	d = perf4.getTotal().QuadPart/perf4.getCount();
	static int a1;
	a1 = perf11.getTotal().QuadPart/perf11.getCount();
	static int b1;
	b1 = perf21.getTotal().QuadPart/perf21.getCount();
	static int c1;
	c1 = perf31.getTotal().QuadPart/perf31.getCount();
//	XPTRACE(L"%d, %d, %d,    %d, %d, %d\n", a, b, c, a1, b1, c1);
//	XPTRACE(L"%d, %d, %.6f\n", b, d, aaaaaaaaaaaa);
	if (aaaaa == 1)
	{
		aaaaa = 0;
		perf1.clean();
		perf2.clean();
		perf3.clean();
		perf4.clean();
	}
}


void TestDrawEllipsePicture()
{
	pRenderer->Begin(0xffff0000);

	DrawEllipsePictureParam param;
	param.handle = h1;
	param.x = 512.0f;
	param.y = 384.0f;
	param.rx = 200.0f;
	param.ry = 100.0f;
	param.u0 = 0.25f;
	param.v0 = 0.5f;
	param.ru = 0.5f;
	param.rv = 0.75f;
	param.color = 0xffffffff;
	param.TextureMode = XPTEXADDRESS_BORDER;
	param.borderColor = 0xff000000;

	pRenderer->DrawEllipsePicture(param);

	pRenderer->End();

}

void RenderParticles()
{
	/*
	int tickStrt = (int)GetMilliSeconds();
	float fdata = 0.0f;
	float ftemp = 2.0f;
	float fvalue = 1.23f;
	for(int i=0; i<100000000; i++)
	{
		fdata = ftemp*2.0f *fvalue;
	}
	int deltaTime = (int)GetMilliSeconds() - tickStrt;

	XPTRACE( L"use time %d ms\n", deltaTime );
*/

	pRenderer->Begin();

// 	g_pParticleManager->UpdateParticleSystem(g_pshandle1, 0.01f);
// 	g_pParticleManager->UpdateParticleSystem(g_pshandle2, 0.01f);
// 	g_pParticleManager->UpdateParticleSystem(g_pshandle3, 0.01f);
// 	g_pParticleManager->UpdateParticleSystem(g_pshandle4, 0.01f);
// 	g_pParticleManager->UpdateParticleSystem(g_pshandle5, 0.01f);
// 	g_pParticleManager->UpdateParticleSystem(g_pshandle6, 0.01f);
// 	g_pParticleManager->UpdateParticleSystem(g_pshandle7, 0.01f);

 	g_pParticleManager->RenderParticleSystem(g_pshandle1);
//  g_pParticleManager->RenderParticleSystem(g_pshandle2);
//  g_pParticleManager->RenderParticleSystem(g_pshandle3);
// 	g_pParticleManager->RenderParticleSystem(g_pshandle4);
// 	g_pParticleManager->RenderParticleSystem(g_pshandle5);
//	g_pParticleManager->RenderParticleSystem(g_pshandle6);
//	g_pParticleManager->RenderParticleSystem(g_pshandle7);

	RECT trect;
	trect.left = 500;
	trect.top = 100;
	trect.right = 680;
	trect.bottom = 400;

	TCHAR szbuffer[256];
	int f1 = (int)(pRenderer->GetFPS()*10);
	swprintf_s(szbuffer, 256, L"TFPS: %3d.%1d", f1/10, f1%10);	
	trect.left = 0;
	trect.top =0;
	trect.right =  500;
	trect.bottom = 300;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xff00ff00);
	f1 = (int)(pRenderer->GetLatestFPS()*10);
	swprintf_s(szbuffer, 256, L"LFPS: %3d.%1d", f1/10, f1%10);
	trect.top += 20;
	trect.bottom += 20;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xffff0000);

	int nTotalNum = g_pParticleManager->GetTotalNumActiveParticles();
	swprintf_s(szbuffer, 256, L"TotalActiveParticles: %10d", nTotalNum);
	trect.top += 20;
	trect.bottom += 20;
	pRenderer->DrawText(szbuffer, 32, &trect, 0xffff0000);

	pRenderer->End();
}

int g_cnt = 0;

float fdeltaTime = 0.0f; //波动特效测试...
float SumDeltatime = 0.0f;

VOID Render()
{
	/*
	if( testbuf == NULL )
	{
		testbuf = malloc(800*600*4);
		memset(testbuf,0,800*600*4);
		
		pRenderer->BeginRenderToMemory(800,600);
		XPV(pRenderer->Begin());
		XPV(pRenderer->DrawLine(0.f, 0.f, 200.f, 200.f, 0))
		
		
		XPV(pRenderer->End());
		pRenderer->EndRenderToMemory(testbuf,800*600*4);
		
	}
	*/
	pRenderer->Begin();

	CRECT srcrect;
	srcrect.left = 100;
	srcrect.top = 30;
	srcrect.right = 612;
	srcrect.bottom = 542;

	int iWidth = srcrect.Width();
	int iHeight = srcrect.Height();

	PictureHandle handle = h1;//在BOOL Init(HWND hwnd)中加载的

	Nuclear::FPOINT lt(200, 0);
	Nuclear::FPOINT rt(400, 200);
	Nuclear::FPOINT rb(200, 400);
	Nuclear::FPOINT lb(0, 200);

//	bool bresult = pRenderer->DrawPicture(handle, lt, rt, rb, lb);
	//XPCOLOR color[4] = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xff000000};
	//bool bresult = pRenderer->DrawPicture(handle, srcrect.ToFRECT(), color);

	g_cnt++;
	if(g_cnt>=700*5)
		g_cnt = 0;
	
//	for(int i=0; i<100; i++)
		//pRenderer->RenderGrayPic(handle, srcrect.ToFRECT(), g_cnt/500);
//		pRenderer->DrawPicture(handle, srcrect.ToFRECT());
//	pRenderer->RenderEmbossPic(handle, srcrect.ToFRECT(), 500, 500, g_cnt/500);
//	pRenderer->RenderRadialBlur(handle, srcrect.ToFRECT(), FPOINT(0.5f, 0.5f), 0.5f, 0);
	FRECT srcrect_2;
	srcrect_2 = FRECT(10.0f,10.0f, 522.0f, 522.0f);

	PictureInfo picinfo;
	pRenderer->GetPictureInfo(h1, picinfo);

	float x1,y1,x2,y2;
	x1 = 10.f;
	x2 = x1+ (float)(picinfo.m_nPicWidth);
	y1 = 10.f;
	y2 = y1+ (float)(picinfo.m_nPicHeight);
	
	FRECT r0(x1, y1, x2, y2);
	FRECT rte;

	DrawPictureParam paramPic21;
	paramPic21.handle = h21;
	paramPic21.pRect = &srcrect_2;
	XPCOLOR color21 = 0xffffffff;
	paramPic21.pColor = &color21;
	paramPic21.colorCount = 1;
	paramPic21.blend = Nuclear::XPTEXBM_DEFAULT;

	DrawPictureParam paramPic;
	paramPic.handle = h1;
	paramPic.pRect = &r0;
	XPCOLOR color = 0xf0f02f5f;
	paramPic.pColor = &color;
	paramPic.colorCount = 1;
	paramPic.blend = Nuclear::XPTEXBM_DEFAULT;

// 	float fSpeed = 2.0f;
// 	distortionParam.fCycNum = 0.9f;
// 	distortionParam.fPos += fdeltaTime*2*XP_PI/fSpeed;
// 	int count = (int)(distortionParam.fPos/(2.0f*XP_PI));
// 	distortionParam.fPos -= count*2.0f*XP_PI;//------控制函数移动

	int tickStrt = (int)GetMilliSeconds();
//	paramPic.pDistortionParam = &distortionParam;

// 	XPDistortionShaderParam param;
// 	param.fdegree = -1.0f*SumDeltatime/60.0f +0.5f; //扭曲程度0.0f----1.0f
// 	param.fcent = FPOINT(0.5f, 0.5f); //扭曲中心  //左上角(0,0) 右下角(1,1)
// 	param.fMaxDis = 0.6f; //扭曲半径 //0.0f----1.0f
// 	param.nType1 = 1; //控制方向 //挤压还是阔张等...
// 	param.nType2 = 1; //控制形状
// 	pRenderer->SelectRenderEffect(XPRE_DISTORTION);
// 	pRenderer->SetShaderParam(XPRE_DISTORTION, &param);
// 	pRenderer->DrawPicture(paramPic);

	pRenderer->SelectRenderEffect(XPRE_NULL);
	pRenderer->DrawPicture(paramPic21);

	XPFontOutlineParam OutlineParam;
	OutlineParam.dwOutlineColor = XPCOLOR_RED;
	OutlineParam.nTextureWidth = picinfo.m_nPicWidth;
	OutlineParam.nTextureHeight = picinfo.m_nPicHeight;
//	pRenderer->SelectRenderEffect(XPRE_FONTOUTLINE);
//	pRenderer->SetShaderParam(XPRE_FONTOUTLINE, &OutlineParam);
	pRenderer->DrawPicture(paramPic);

// 	XPEmbossParam EmbossParam;
// 	EmbossParam.fSizex = picinfo.m_nPicWidth;
// 	EmbossParam.fSizey = picinfo.m_nPicHeight;
// 	EmbossParam.ndir = 0;
// 	pRenderer->SelectRenderEffect(XPRE_EMBOSSPIC);
// 	pRenderer->SetShaderParam(XPRE_EMBOSSPIC, &EmbossParam);
// 	pRenderer->DrawPicture(paramPic);

//	float u0 = fabs( cos(SumDeltatime) );
//	float v0 = fabs( sin(SumDeltatime) );
//	bool bres = pRenderer->DrawEllipsePicture(h1, 200, 200, 150, 100, u0, v0);

	int tickEnd = (int)GetMilliSeconds();
	fdeltaTime = (tickEnd-tickStrt)/1.0f;
	SumDeltatime += fdeltaTime;
	if(SumDeltatime > 30.0f)
	{
		SumDeltatime = 0.0f;
	}
//	SumDeltatime = (float)( (int)(SumDeltatime)%3 );

	//r0.Assign(10.f+x1,10.f+y1,10.f+x2/2,10.f+y2/2);
	//rte.Assign(0.f,0.f,.5f,.5f);
	//paramPic.pSrcrect = &rte;
	//pRenderer->DrawPicture(paramPic);

	//r0.Assign(10.f+x2/2,10.f+y1,10.f+x2,10.f+y2/2);
	//rte.Assign(.5f,0.f,1.f,.5f);
	//pRenderer->DrawPicture(paramPic);

	//r0.Assign(10.f+x1,10.f+y2/2,10.f+x2/2,10.f+y2);
	//rte.Assign(.0f,0.5f,.5f,1.f);
	//pRenderer->DrawPicture(paramPic);

	//r0.Assign(10.f+x2/2,10.f+y2/2,10.f+x2,10.f+y2);
	//rte.Assign(.5f,0.5f,1.f,1.f);
	//pRenderer->DrawPicture(paramPic);

	//
	//rte.Assign(0.f,0.f,1.f,1.f);

	//paramPic.handle = h21;
	//r0.Assign(10.f+x1,10.f+y1,10.f+x2/2,10.f+y2/2);
	//pRenderer->DrawPicture(paramPic);

	//paramPic.handle = h22;
	//r0.Assign(10.f+x2/2,10.f+y1,10.f+x2,10.f+y2/2);
	//pRenderer->DrawPicture(paramPic);

	//paramPic.handle = h23;
	//r0.Assign(10.f+x1,10.f+y2/2,10.f+x2/2,10.f+y2);
	//pRenderer->DrawPicture(paramPic);

	//paramPic.handle = h24;
	//r0.Assign(10.f+x2/2,10.f+y2/2,10.f+x2,10.f+y2);
	//pRenderer->DrawPicture(paramPic);


//	int tickStart = (int)GetMilliSeconds();
//	for(int i=0; i<1000; i++)
//	{
//		pRenderer->DrawPicture(paramPic);
//		pRenderer->RenderColorBalance(handle, srcrect_2.ToFRECT(), param);
//	}

//	int tickEnd = (int)GetMilliSeconds();
//	XPTRACE( L"TestRender()--RenderColorBalance, use time %d ms\n", tickEnd- tickStart );

//	for(int i=0; i<100; i++)
//		pRenderer->RenderColorContrast(handle, srcrect_2.ToFRECT(), 0.5f);
//	pRenderer->FreePicture(handle);
/*
	int pixelByte;
	void *buf = NULL;
//	int size;

	BYTE* srcBuff = NULL;
	BYTE* desBuff = NULL;
//	srcBuff = new BYTE[pixelByte*iWidth*iHeight];
																	//, XPPIXELFMT_A8R8G8B8
	if(pRenderer->GetBackBufferData(&buf, &size, &pixelByte, &srcrect))//PrintScreen(&buf, &size, XPIFF_BMP, &srcrect)
	{
		srcBuff = (BYTE*)buf;
	//	memcpy(srcBuff, (BYTE*)buf +sizeof(BITMAPFILEHEADER) +sizeof(BITMAPINFOHEADER), pixelByte*512*512 );
		desBuff = new BYTE[pixelByte*iWidth*iHeight];

		IMAGESTRUCT SrcPara;
		SrcPara.m_nWidth = iWidth;
		SrcPara.m_nHeight = iHeight;
		SrcPara.m_nPitch = pixelByte*iWidth;
		SrcPara.m_enumPixelFormat = XPPIXELFMT_A8R8G8B8; //XPPIXELFMT_R5G6B5
		SrcPara.m_pBuf = srcBuff;

		IMAGESTRUCT DesPara;
		DesPara.m_nWidth = iWidth;
		DesPara.m_nHeight = iHeight;
		DesPara.m_nPitch = pixelByte*iWidth;
		DesPara.m_enumPixelFormat = XPPIXELFMT_A8R8G8B8; //XPPIXELFMT_A8R8G8B8
		DesPara.m_pBuf = desBuff;

		EFFECTSTRUCT imgEffect;
		imgEffect.m_ptBlurCenter.x = iWidth/2;
		imgEffect.m_ptBlurCenter.y = iHeight/2;
		imgEffect.m_nBlurMode = XPZOOMBLUR;
		imgEffect.m_fDegree = 0.2f;

		CRadialBlur radialBlur;
		radialBlur.RadialBlurEffect(&SrcPara, imgEffect, &DesPara);

		BITMAPFILEHEADER bfh;//文件标题结构
		BITMAPINFOHEADER bih;//信息标题结构
		int BytesPerRow;
		//每行中数据字节长度为4的倍数
		BytesPerRow=(iWidth*pixelByte+3) & (~3);

		bfh.bfType = (0X4d42);//位图文件标志，19778是“BM”的数值
		bfh.bfSize = sizeof(bfh)+sizeof(bih)+ BytesPerRow*iHeight;
		bfh.bfReserved1 = 0;
		bfh.bfReserved2 = 0;
		bfh.bfOffBits = sizeof(bfh)+sizeof(bih);

		bih.biSize = sizeof(bih);
		bih.biWidth = iWidth;
		bih.biHeight = iHeight;
		bih.biPlanes = 1;
		bih.biBitCount = pixelByte *8;

	//	bih.biCompression = BI_RGB;
		bih.biCompression = BI_BITFIELDS;
		bfh.bfSize += 12; //增加三个DWORD值，分别代表RGB三分量的掩码
		bfh.bfOffBits += 12;

		bih.biSizeImage = 0;
		bih.biXPelsPerMeter = 0x0000DEC4;
		bih.biYPelsPerMeter = 0x0000DEC4;
		bih.biClrUsed = 0;
		bih.biClrImportant = 0;

		DWORD RedMask;
		DWORD GreenMask;
		DWORD BlueMask;
		RedMask = 0xF800;
		GreenMask = 0x07E0;
		BlueMask = 0x001F;

  		FILE *f= FOpen(L"E:\\testPic\\333test_121212121212112.bmp", L"wb"); //rgb565文件保存
  		if(f)
  		{
 			FWrite(f, &bfh, sizeof(bfh));
			FWrite(f, &bih, sizeof(bih));
			FWrite(f, &RedMask, sizeof(DWORD));
			FWrite(f, &GreenMask, sizeof(DWORD));
			FWrite(f, &BlueMask, sizeof(DWORD));

 			FWrite(f, DesPara.m_pBuf, pixelByte*iWidth*iHeight);
  			fclose(f);
  		}
		XFree(buf);

 		PictureHandle handle1 = pRenderer->NewPicture(iWidth, iHeight, NULL, XPTEXFMT_A8R8G8B8);
 		bool bres1 = pRenderer->SetPictureData(handle1, DesPara.m_pBuf, pixelByte*iWidth*iHeight);
 		pRenderer->DrawPicture(handle1, Nuclear::FRECT(0, 0, (float)iWidth, (float)iHeight));
 		pRenderer->FreePicture(handle1);

		delete []desBuff;
	}
*/

//	pRenderer->DrawLine(0.f, 0.f, 200.f, 200.f, 0);
	//XPV(pRenderer->DrawLine(100.f, 100.f, 300.f, 100.f, 0))
	//XPV(pRenderer->DrawLine(100.f, 200.f, 300.f, 200.f, 0))
	//XPV(pRenderer->DrawLine(100.f, 300.f, 300.f, 300.f, 0))
	//XPV(pRenderer->DrawLine(100.f, 400.f, 300.f, 400.f, 0xff00ff00))
//	XPV(pRenderer->DrawBox(100.f, 100.f, 300.f, 300.f, 0xff00ff00));

//	pRenderer->DrawCircle(100, 100, 50, 0xffff0000, Nuclear::XPFM_SOLIDFILL);
//	pRenderer->DrawCircle(200, 200, 80, 0xffff0000, Nuclear::XPFM_WIREFRAME);


	/*
	static bool test = false;
	if( !test )
	{
		Nuclear::FileBuffer fb(L"d:\\0.tga");
		if(fb.Size())
		{
			void *buf= malloc(fb.Size());
			int bufsize = fb.Size();
			RECT rect;
			bool r = pRenderer->SaveTextureToDXSurface(fb.Begin(), fb.Size(),buf,bufsize, D3DFMT_DXT5,rect);
			if( !r ) MessageBox(0,0,L"failed",0);
			else
			{
				
				FILE *fo = FOpen(L"d:\\x.dds",L"wb");
				if( fo )
				{
					FWrite(fo,buf,bufsize);
					fclose(fo);
				}
				
				int npid = pRenderer->LoadPictureFromMem(buf,bufsize);
				pRenderer->DrawPictureByID(npid,0.f,0.f,100.f,100.f);
				pRenderer->FreePicture(npid);
			}
			free(buf);
		}
		test = true;
	}
	*/
	/*
	static void *testbuf = NULL;
	if( NULL == testbuf )
		testbuf = malloc(400*400*4);
	memset(testbuf,-1,400*400*4);
	
	if( testbuf)
	{
		Nuclear::PictureHandle newPic = pRenderer->NewPicture32(400,400);
		Nuclear::PictureHandle newPic2 = pRenderer->NewPicture32(200,200);
		//int newPic2 = pRenderer->NewPicture32(800,600);
		if( newPic != Nuclear::INVALID_PICTURE_HANDLE )
		{
			DWORD * data = (DWORD*)testbuf;
			RECT rect;
			rect.left = 300;
			rect.top = 300;
			rect.right = 400;
			rect.bottom = 400;

			RECT rect2;
			rect2.left = 0;
			rect2.top = 0;
			rect2.right = 100;
			rect2.bottom = 100;
			RECT rect3;
			rect3.left = 50;
			rect3.top = 50;
			rect3.right = 350;
			rect3.bottom = 350;
			RECT rect4;
			rect4.left = 0;
			rect4.top = 0;
			rect4.right = 300;
			rect4.bottom = 300;
			pRenderer->SetPictureData32(newPic, data, 100*100*4, &rect);
			pRenderer->SetPictureData32(newPic, data, 100*100*4, &rect2);

			//pRenderer->DrawPicture(newPic,100.f,100.f,500.f,500.f,0xffffffff);
			memset(testbuf,0,400*400*4);
			pRenderer->GetPictureData32(newPic, data, 300*300*4, &rect3);
			pRenderer->SetPictureData32(newPic, data, 300*300*4, &rect4);
			pRenderer->DrawPicture(newPic,100.f,100.f,500.f,500.f,0xffffffff);
			//pRenderer->GetPictureData32(newPic, data, 800*600*4);
			//pRenderer->SetPictureData32(newPic2, data+400*600+400, 100*100*4, &rect2);
			//pRenderer->DrawPicture(newPic2,300.f,100.f,400.f,200.f,0xffffffff);
			//pRenderer->FreePicture(newPic);
			//pRenderer->FreePicture(newPic2);
		}
	}
	*/
	/*
	FRECT *rects = new FRECT[20*20];
	for(int i=0; i<20; ++i)for(int j=0;j<20;++j)
	{
		FRECT &tr = rects[j*20+i];
		tr.left = 0+i*20.f;
		tr.right =0+i*20.f +18.f;
		tr.top = 0+j*20.f;
		tr.bottom = 0+j*20.f+16.f;
		//XPV(pRenderer->DrawLine(tr, 0xff0000ff))
		//XPV(pRenderer->DrawPicture(1,tr));
		//XPV(pRenderer->DrawBatchPicture(1,&tr,1));
	}

	//XPV(pRenderer->DrawBox(rects, 40*40, 0xff0000ff,0));
//	pRenderer->DrawBatchPicture(1,rects,20*20);

	//delete[] rects;
	*/
//	XPV(pRenderer->DrawPicture(_T("E:\\3.bmp"), 10, 210, 210, 410, 0xffffffff))
	//XPV(pRenderer->DrawPicture(_T("f:\\1.bmp"), 150.f, 100.f, 250.f, 500.f, 0xffffff))
	//XPV(pRenderer->DrawPicture(_T("f:\\1.bmp"), 550.f, 100.f, 850.f, 500.f, 0xffffff))
	RECT trect;
	trect.left = 500;

	trect.top = 100;
	trect.right = 680;
	trect.bottom = 400;

//	pRenderer->DrawBox(100.f, 3.f, 700.f, 6.f, 0xff00ff00);
//	pRenderer->DrawPicture(_T("E:\\12.bmp"), 100, 10, 700, 510);

//	PictureHandle handle = pRenderer->LoadPicture(_T("E:\\333test.png"));
//	bool bres = pRenderer->DrawPicture(handle, 100, 30, 612, 542, FPOINT(256.0f, 256.0f), 0.2f);
//	pRenderer->FreePicture(handle);

//	pRenderer->DrawText(_T("拉丝机法律上的发生地方"), 10, &trect, 0xff00ffff);
		
	TCHAR szbuffer[256];
	int f1 = (int)(pRenderer->GetFPS()*10);
	swprintf_s(szbuffer, 256, L"TFPS: %3d.%1d", f1/10, f1%10);	
	trect.left = 0;
	trect.top =0;
	trect.right =  500;
	trect.bottom = 300;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xff00ff00);
	f1 = (int)(pRenderer->GetLatestFPS()*10);
	swprintf_s(szbuffer, 256, L"LFPS: %3d.%1d", f1/10, f1%10);
	trect.top += 20;
	trect.bottom += 20;
	pRenderer->DrawText(szbuffer, 11, &trect, 0xff00ff00);

	GXWINDOW::GXWUpdate();

	//XPV(pRenderer->DrawPicture(_T("f:\\black800600.bmp"), 0, 0, 800,600, 0))
	//XPV(pRenderer->DrawPicture(_T("f:\\black800600.bmp"), 0-0.5f, 0-0.5f, 800-0.5f,600-0.5f, 0))
	/*
	void *buf;
	int size;
	RECT srcrect;
	srcrect.left = srcrect.top = 0;
	srcrect.right = srcrect.bottom = 128;
	if(pRenderer->PrintScreen(&buf, &size,D3DXIFF_JPG, &srcrect))
	{
		FILE *f= FOpen(L"d:\\test.jpg",L"wb");
		if( f)
		{
			FWrite(f,buf,size);
			fclose(f);
		}
		XFree(buf);
	}
	*/
	/*
	XPV(pRenderer->ShowCursor(true))
	static bool f2=false;
	if( !f2)
	{		
		XPV(pRenderer->SetCursorPicture(1,false))
		f2=true;
	}
	*/
	//XPV(pRenderer->SetCursorPosition(0,0))

	pRenderer->End();
}

VOID Clean()
{
//	m_frame->Hide();
//	delete m_frame;

//	GXWINDOW::GXWUninitialize();

//	delete g_pGXWRenderer;

	XPLOG_INFO(L"fps is %8.3f\n", pRenderer->GetFPS());
	DestroyRenderer(pRenderer);
	g_pFileIOMan->Destroy();
	delete g_pFileIOMan;
	g_pFileIOMan = NULL;
}

LRESULT WINAPI MsgProc( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
{
	if( GXWINDOW::GXWMessage( hWnd, msg, wParam, lParam))
		return 0;

    switch( msg )
    {
	case WM_TIMER:
		if( wParam == 1 )
			TimerOB::OnSourceTimer();
		break;
    case WM_DESTROY:
		Clean();
		PostQuitMessage(0);
		break;

	case WM_KEYDOWN:
		switch( wParam)
		{
		case VK_F1:
			pRenderer->ReleaseVideoRenderRes();

// 			if(g_pdshowTexRender != NULL)
// 			{
// 				g_pdshowTexRender->pauseMovie();
// 			}
			/*
			{

				FRECT frct1(0.0f, 0.0f, 1024.0f, 768.0f);
				FRECT frct2(60.0f, 80.0f, 800.0f, 600.0f);
				FRECT frct3(100.0f, 100.0f, 878.0f, 663.0f);
				FRECT frct4(100.0f, 20.0f, 612.0f, 532.0f);
				FRECT frct5(400.0f, 200.0f, 524.0f, 436.0f);

				XPColorBalanceParam timeEffectParam;
				timeEffectParam.fCoefr = 0.0f;
				timeEffectParam.fCoefg = 0.9f;
				timeEffectParam.fCoefb = 0.9f;
				timeEffectParam.fLightness = 0.0f;
				timeEffectParam.fContrastk = 1.0f;
				timeEffectParam.fContrastb = 0.0f;
				// 
				timeEffectParam.fCentx = 400.0f;
				timeEffectParam.fCenty = 400.0f;

				pRenderer->SelectRenderEffect(XPRE_COLORBALANCE);//XPRE_COLORBALANCE
				pRenderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam); //psParam
			}
			*/
			break;

		case VK_F2:
			g_pdshowTexRender->HoldLastFrame();
		//	g_pdshowTexRender->pauseMovie();

	//		g_pdshowTexRender->playMovie();
			/*
			{
				FRECT frct1(0.0f, 0.0f, 1024.0f, 768.0f);
				FRECT frct2(60.0f, 80.0f, 800.0f, 600.0f);
				FRECT frct3(100.0f, 100.0f, 878.0f, 663.0f);
				FRECT frct4(100.0f, 20.0f, 612.0f, 532.0f);
				FRECT frct5(400.0f, 200.0f, 524.0f, 436.0f);

				XPColorBalanceParam timeEffectParam;
				timeEffectParam.fCoefr = 0.0f;
				timeEffectParam.fCoefg = 0.9f;
				timeEffectParam.fCoefb = 0.9f;
				timeEffectParam.fLightness = 0.0f;
				timeEffectParam.fContrastk = 1.0f;
				timeEffectParam.fContrastb = 0.0f;
				// 
				timeEffectParam.fCentx = 400.0f;
				timeEffectParam.fCenty = 400.0f;

				pRenderer->SelectRenderEffect(XPRE_COLORBALANCE_2);//XPRE_COLORBALANCE
				pRenderer->SetShaderParam(XPRE_COLORBALANCE_2, &timeEffectParam); //psParam
			}
			*/
			break;

		case VK_F3:

			g_pdshowTexRender->rePlayMovie();
			break;

		case VK_F4:
			{
				bool bres = g_pdshowTexRender->isPlayingMovie();
				g_bPlaying = bres;
				if(bres)
					XPTRACE(L"正在播放...\n");
				else
					XPTRACE(L"没有播放...\n");
			}
			break;

		case VK_F5:
			g_pdshowTexRender->SetPosition(5.0f);
	//		g_pdshowTexRender->SetSpeed(2.0f);
			break;

		case VK_F6:
	//		g_pdshowTexRender->SetSpeed(0.5f);
			break;

		case VK_F7:
	//		g_pdshowTexRender->SetSpeed(1.0f);
			break;

		default:
			break;
		}
		break;

	default:
		break;
    }

    return DefWindowProc( hWnd, msg, wParam, lParam );
}

INT WINAPI WinMain( HINSTANCE hInst, HINSTANCE, LPSTR, INT )
{    
    WNDCLASSEX wc = { sizeof(WNDCLASSEX), CS_CLASSDC, MsgProc, 0L, 0L,
                      GetModuleHandle(NULL), NULL, NULL, NULL, NULL,
                      _T("TestRenderer"), NULL };
    RegisterClassEx( &wc );

    
    HWND hWnd = CreateWindow( _T("TestRenderer"), _T("TestRenderer"),
                              WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX, 0, 0, 1280, 1024,
                              GetDesktopWindow(), NULL, wc.hInstance, NULL );

	SetTimer(hWnd, 1, 1000, NULL);
	
	if( Init( hWnd ) )
	{		
		ShowWindow( hWnd, SW_SHOWDEFAULT );
		UpdateWindow( hWnd );

	//	GXWINDOW::GXWInitialize( hWnd, 800, 600);
	//	g_pGXWRenderer = new GXWRENDER::CGXWRenderer( pRenderer);
	//	GXWINDOW::GXWSetDefaultRenderer( g_pGXWRenderer);
//		GXWRENDER::CGXWRenderer::InitializeImages( pRenderer);

	//	int nIndex = pRenderer->LoadPictureFromNativePath( L"RoleInfo.bmp");
	//	m_frame = new CMyFrame;
	//	m_frame->Show();
	//	m_notify.SetHwnd( hWnd);

		
		MSG msg;
		ZeroMemory( &msg, sizeof(msg) );
		while( msg.message!=WM_QUIT )
		{
			if( PeekMessage( &msg, NULL, 0U, 0U, PM_REMOVE ) )
			{
				TranslateMessage( &msg );
				DispatchMessage( &msg );
			}
			else
			{
			//	Render();
			//	RenderParticles();
			//	TestDrawBatchPicture();
			//	TestShaderEffect();
			//	TestDrawEllipsePicture();
			//	TestDrawPicture1();
				RenderVideoTexture(); //g_pdshowTexRender
			}
		}
	}

    UnregisterClass( _T("TestRenderer"), wc.hInstance );
    return 0;
}