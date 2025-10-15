#pragma once

#include "..\common\xptypes.h"
#include "..\renderer\ibaserenderer.h"
#include "iparticlesyscommon.h"

namespace Nuclear
{

struct BLEND_MODE
{
	TCHAR name[25];
	int mode;
};

const int NUMBLENDINGMODES = 8;
extern const BLEND_MODE g_BlendingModes[8];
//查表powf(fAgeCoef, fvar);//fAgeCoef:0.0f--1.0f,  fvar:0.0f--2.0f粒子系统用
//var分成20份, 生命值分成40份
extern float XP_POWF[11][42];

struct PSTEXTUREINFO
{
	//列大于等于行...
	//纹理数据按行优先填充
	PictureHandle handle;
	int m_nTexWidth; //psl中一个小纹理的宽、高
	int m_nTexHeight;
	int m_nPslTexRow; //小纹理合并之后的大纹理有几行几列..
	int m_nPslTexCol; //列
	int m_nTexNum; //小纹理个数

	PSTEXTUREINFO()
	{
		handle = INVALID_PICTURE_HANDLE;
		m_nTexWidth = 0;
		m_nTexHeight = 0;
		m_nPslTexRow = 0;
		m_nPslTexCol = 0;
		m_nTexNum = 0;
	}
};

struct PSTEXTUREINFOSTRUCT
{
	PSTEXTUREINFO PsTextureInfo;
	DWORD dwTime; //秒
	float dataSize; //M
	IBaseRenderer* pRenderer;

	PSTEXTUREINFOSTRUCT(IBaseRenderer* prender)
	{
		pRenderer = prender;
		dwTime = 0;
		dataSize = 0.0f;
	}

	~PSTEXTUREINFOSTRUCT()
	{
		pRenderer->FreePicture(PsTextureInfo.handle);
	}

private:
	PSTEXTUREINFOSTRUCT(const PSTEXTUREINFOSTRUCT&);
	PSTEXTUREINFOSTRUCT& operator=(const PSTEXTUREINFOSTRUCT&);
};

struct PSPICHANDLESTRUCT //图片名---小纹理
{
	PictureHandle handle;
	DWORD dwTime; //秒
	float dataSize; //M
	IBaseRenderer *pRenderer;

	PSPICHANDLESTRUCT(IBaseRenderer* prender)
	{
		pRenderer = prender;
		handle = INVALID_PICTURE_HANDLE;
		dwTime = 0;
		dataSize = 0.0f;
	}

	~PSPICHANDLESTRUCT()
	{
		pRenderer->FreePicture(handle);
	}

private:
	PSPICHANDLESTRUCT(const PSPICHANDLESTRUCT&);
	PSPICHANDLESTRUCT& operator=(const PSPICHANDLESTRUCT&);
};

struct PATHPOINT 
{
	POINT pt;
	BOOL m_bCurve;
	BOOL m_bLink;

	POINT pre_ControlPt;
	POINT next_ControlPt;

	BOOL bKeyPoint; //为true则是CGeneralParticleSystem::SetKeyPointIndex(int)使用的标记点

	PATHPOINT()
	{
		pt.x = 0;
		pt.y = 0;
		pre_ControlPt.x = 0;
		pre_ControlPt.y = 0;
		next_ControlPt.x = 0;
		next_ControlPt.y = 0;
		m_bCurve = FALSE;
		m_bLink = FALSE;

		bKeyPoint = FALSE;
	}

	~PATHPOINT()
	{
	}
};

//转场特效的分块数据信息 -----------以下-----
struct Vertex
{
	float u;
	float v;
	Vertex()
	{
		u = 0.0f;
		v = 0.0f;
	}
};

typedef std::vector<Vertex> VERTEX_LIST;

struct SHAPE
{
	VERTEX_LIST vertexs; //这个数据粒子系统不需要... 在绘制的时候需要
	Vertex center; //标示每个粒子的位置
	void reset()
	{
		vertexs.clear();
	}
};

typedef std::vector<SHAPE> SHAPE_LIST;
//转场特效的分块数据信息 -----------以上-----


struct PSL
{
	int m_nVersion; //2012-3-1 版本号 有3个参数在版本4里有特殊用途，读psl的时候把版本号记下来方便使用

	int m_nAbsrtactPsl; //默认为FALSE ---TRUE是抽象psl..不带纹理信息
	std::wstring m_strVtxsObjectPath; //抽象粒子特效对应的vertex文件名
	int m_nDirMotion; //抽象粒子特效--是否锁定运动方向
	int m_nPSConfigReq;
	int m_nPosType;	  //点、圆、矩形、直线段、自定义路径
	int m_nParticlePathInitPosType; //开头、随机、上次死亡位置
	int m_nParticleSizeRandType;
	int m_nMoveType;
	int m_nPathOrder; //是否按路径方向产生粒子
	int m_nUVmirrorh; //纹理水平镜像
	int m_nUVmirrorv; //纹理垂直镜像
	FPOINT m_point0;
	FPOINT m_point1;  //1线发射时的另外一个端点
	FPOINT m_pointEmitterSpinCent; //发射器旋转中心..相对于特效挂接点的相对坐标
	FPOINT m_pointParticleHole; //粒子影响点..黑洞
	int m_nSysPosRadiusX;
	int m_nSysPosRadiusY;
	CRECT m_rct;
	// 路径文件名...
	std::wstring m_strFullPathFile;
	float m_fPathZoom; //路径缩放比例..可能一个路径文件供多个psl使用..仅仅是大小不一样..
	float m_fPathSpin; //路径旋转角度..使用方法同上
	int m_nPathMirrorType; //路径镜像类型...使用方法同上
	int m_nNumPointFullPath; //为以后改变了路径采样精度时推算粒子的运动速度准备...
	//默认-1 则不改变运动速度，但是设置路径采样频率为8.0f/dis

	int m_nParFillMode;				//false 不填充 -- true 填充
	int m_nEmitterType;				//false 不连续 -- true 连续
	int m_nSingleParticleAni;		//false 单粒子动画...
	int m_nParticleAniTextureIndex1;//单粒子动画是否从美术素材的第一帧开始
	int m_nAniPlaySpeed;			//0---50 default: 10	
	float m_fAniSpeedRand;			//0---10 default: 0
	int m_nParticleColorChange;		//false 是否颜色渐变
	int m_nParticleColorRand;		//色彩随机...一种很奇怪的bug色彩效果..
	int m_nTrackFx;					//是否弹道跟踪

	int m_nSysMaxParticles;			//0-2000

	float m_fSysLife;				//0--1000 ---100.0f
	int m_nEmissionRate;			//0--1000
	int m_nEmitterSpeed;			//0--1000
	float m_fParticleLifeMin;		//0--1000 ---100.0f
	float m_fParticleLifeMax;		//0--1000 ---100.0f
	float m_fEmissionDirection;		//0--3600 --360.0f
	float m_fEmissionSpread;		//0--3600 --360.0f
	float m_fSpeedMin;				//0--10000 --1000.0f
	float m_fSpeedMax;				//0--10000 --1000.0f
	float m_fGravity;			//-1000--1000 --100.0f
	float m_fGravityRand;			//0--50 --5.0f
	float m_fRadialAcc;			//-200--200 --20.0f
	float m_fRadialAccRand;			//0--50 --5.0f
	float m_fTangAcc;			//-200--200 --20.0f
	float m_fTangAccRand;			//0--50 --5.0f
	float m_fParSizeStrtX;			//0---100.0f
	float m_fParSizeStrtY;			//0---100.0f
	float m_fParSizeEndX;			//0---100.0f
	float m_fParSizeEndY;			//0---100.0f
	float m_fParSizeVar;			//0--100 --1.0f
	float m_fParSpinStrt;			//0--3600 --360.0f
	float m_fParSpinEnd;			//0--3600 --360.0f
	float m_fParSpinVar;			//0--100 --1.0f

	float m_fRotateCentX;			//-1.0---+1.0f (-0.5f---+0.5f表示图像的左上到右下..)
	float m_fRotateCentY;			//(-0.5--+0.5f)外的值表示旋转中心在图像外..

	float m_fParAngleStrt;			//0--3600 --360.0f
	float m_fParAngleEnd;			//0--3600 --360.0f
	float m_fParAngleVar;			//0--100 --1.0f

	int m_nParAlphaStrt;			//0--255
	int m_nParAlphaEnd;				//0--255
	float m_fParAlphaVar;			//0--100 --1.0f
	int m_nParAlphaMid;				//允许超过255..单关键帧模拟双关键帧
	float m_fParAlphaMidTime;

	float m_fSizeRandom; //0-1000 --10.0f
	float m_fSpinRandom; //0-3600 -- 360.0f
	float m_fAngleRandom; //0-3600 -- 360.0f
	float m_fAlignToMotionAngle; //-3600-3600 -- -360.0f--360.0f
	int m_nAlignToMotion; //粒子发射方向与路径方向绑定？
	int m_nAlignToPath; //粒子方向与路径方向绑定--2009-03-13
	int m_nMotionToPath; //粒子沿着路径移动-2009-03-26 （发射方向和加速度参数无效）
	//由初始发射速度决定..只要大小不考虑方向..

	int m_nAlignToEmitter; //粒子方向与发射器旋转角度相关 默认值为0，表示两者无关。2010-03-15

	int m_nAlignToHole; //吸引粒子向着这个方向运动..方向也旋转了向着...

	int m_nParticleRandAngle; //2009-4-6 粒子出现的角度随机，在粒子的整个生命过程中不再改变.. 版本1--2
							  //2009-5-31 -------------------根据m_fSpinRandom的值来不断改变. .版本3
	int m_nParticleRandAngleSelf; //自旋随机 09-9-15增加的-----根据m_fAngleRandom的值来不断改变
	//用来实现很多爬在地上的带透视效果的树叶等.. //SpecialParticleSystem没有实现该参数

	int m_nParColorStrtR;			//0--255
	int m_nParColorStrtG;			//0--255
	int m_nParColorStrtB;			//0--255
	int m_nParColorEndR;			//0--255
	int m_nParColorEndG;			//0--255
	int m_nParColorEndB;			//0--255
	float m_fParColorVar;			//0--100 --1.0f
	float m_fPSLzoom;				//0--500 --5.0f //默认值1.0f
	float m_fEmitterSpin;			//发射器旋转角度 单位：角度
 
	int m_nBlendMode; //0-7 8种模式 --default: 0
	int m_nDestBlendMode; //3种 详见PSL_RENDSTATE

	std::vector<std::wstring> m_vectorTexturePath; //多纹理支持..

	int m_nRowNum; //2012-3-1 psl版本4中增加..以下三个参数 //美术把多纹理事先拼在一张大图上了
	int m_nColNum;
	int m_nTextureNum;

	CRECT m_rctMaxBox;

	//2010-1-25 标示放在界面的psl特效的位置坐标
	// 对于非界面特效，这个坐标应该是(0,0)
	int m_nInterfacePsl; //默认为FALSE ---TRUE则表示该特效为界面特效,坐标需要加下面的值..
	FPOINT m_ptInterfacialCenter; //相对于界面中心的相对坐标（水平右x+，竖直下y+）

	int m_nSkillEffectSpritePoint; //BOOL值 是否使用精灵的特效挂点.. 默认false

	PSL()
	{
		m_nAbsrtactPsl = FALSE;
		m_strVtxsObjectPath = L"";
		m_nDirMotion = FALSE;
		m_nPSConfigReq = PSLCONFIGREQ_L;
		m_nPosType = 0;
		m_nParticlePathInitPosType = PARTICLEPATHPOS_START;
		m_nMoveType = PSLMOVETYPE_DEFAULT;
		m_nParticleSizeRandType = PARTICLESIZERAND_XY;
		m_nEmitterType = 1;
		m_nSingleParticleAni = 0;
		m_nParticleAniTextureIndex1 = 0;
		m_nAniPlaySpeed = 10;
		m_fAniSpeedRand = 0.0f;
		m_nParFillMode = 0;
		m_nParticleColorChange = 0;
		m_nParticleColorRand = 0;
		m_nTrackFx = 0;

		m_nPathOrder = 0;
		m_nUVmirrorh = 0;
		m_nUVmirrorv = 0;

		m_point0 = FPOINT(0, 0);
		m_point1 = FPOINT(0, 0);
		m_pointEmitterSpinCent = FPOINT();
		m_pointParticleHole = FPOINT();
		m_nSysPosRadiusX = 100;
		m_nSysPosRadiusY = 100;
		m_rct = CRECT(-50, -50, 50, 50);//矩形发射器的相对坐标
		m_strFullPathFile = L"";
		m_fPathZoom = 1.0f;
		m_fPathSpin = 0.0f;
		m_nPathMirrorType = PATHMIRRORTYPE_NONE;
		m_nNumPointFullPath = -1;

		m_fSysLife = 5.0f;
		m_nEmissionRate = 100;
		m_nEmitterSpeed = 100;
		m_fParticleLifeMin = 1.0f;
		m_fParticleLifeMax = 5.0f;
		m_fEmissionDirection = 0.0f;
		m_fEmissionSpread = 0.0f;
		m_fSpeedMin = 0.0f;
		m_fSpeedMax = 100.0f;
		m_fGravity = 0.0f;
		m_fGravityRand = 0.0f;
		m_fRadialAcc = 0.0f;
		m_fRadialAccRand = 0.0f;
		m_fTangAcc = 0.0f;
		m_fTangAccRand = 0.0f;
		m_fParSizeStrtX = 1.0f;
		m_fParSizeStrtY = 1.0f;
		m_fParSizeEndX = 1.0f;
		m_fParSizeEndY = 1.0f;
		m_fParSizeVar = 0.5f;
		m_fParSpinStrt = 0.0f;
		m_fParSpinEnd = 0.0f;
		m_fParSpinVar = 0.5f;
		
		m_fRotateCentX = 0.0f;
		m_fRotateCentY = 0.0f;

		m_fParAngleStrt = 0.0f;
		m_fParAngleEnd = 0.0f;
		m_fParAngleVar = 0.5f;
		m_nParAlphaStrt = 255;
		m_nParAlphaEnd = 0;
		m_fParAlphaVar = 0.5f;
		m_nParAlphaMid = 128;
		m_fParAlphaMidTime = 0.5f;

		m_nSysMaxParticles = 1000;

		m_fSizeRandom = 0.0f; //0-1000 --10.0f
		m_fSpinRandom = 0.0f; //0-3600 -- 360.0f
		m_fAngleRandom = 0.0f; //0-3600 -- 360.0f
		m_fAlignToMotionAngle = 0.0f;
		m_nAlignToMotion = 0;
		m_nAlignToPath = 0;
		m_nMotionToPath = 0;
		m_nAlignToEmitter = 0;
		m_nAlignToHole = 0;
		m_nParticleRandAngle = 0;
		m_nParticleRandAngleSelf = 0;

		m_nParColorStrtR = 255;
		m_nParColorStrtG = 255;
		m_nParColorStrtB = 0;
		m_nParColorEndR = 255;
		m_nParColorEndG = 0;
		m_nParColorEndB = 0;
		m_fParColorVar = 0.5f;
		m_fPSLzoom = 1.0f;
		m_fEmitterSpin = 0.0f;

		m_nBlendMode = g_BlendingModes[0].mode;
		m_nDestBlendMode = PSLRENDSTATE_ALPHA;

		std::wstring pTempTexPath = L"pirerreE.tga";
		m_vectorTexturePath.clear();
		m_vectorTexturePath.push_back(pTempTexPath);
		
		m_nRowNum = 1;
		m_nColNum = 1;
		m_nTextureNum = 1;

		m_rctMaxBox = CRECT(-400, -400, 400, 400);

		m_nInterfacePsl = FALSE;
		m_ptInterfacialCenter = FPOINT(0, 0);

		m_nSkillEffectSpritePoint = FALSE;
	}

};

typedef int PSLHandle;
const PSLHandle INVALID_PSL_HANDLE = 0;

};