#pragma once


#include <list>

namespace Nuclear
{
	typedef int ParticleSystemHandle;
	const ParticleSystemHandle INVALID_PS_HANDLE = 0;

	const DWORD PSDEFAULTCOLOR = 0xff000001;
	const float	PSDEFAULTTIME = -1.0f;
	const float PSUPDATECRITICALTIME = 0.2f;

	class IParticleNotify
	{
	public:
		//virtual void OnStart() {}
		virtual void OnStop() {}
		virtual void OnEnd() {}
		virtual ~IParticleNotify() {}
	};

	struct PSModifyParam 
	{
		//以下坐标是使用世界坐标还是屏幕坐标要根据创建参数中m_bScreenCoord来确定

		FPOINT m_pointCent; //0 点发射--1线、2圆、3矩形 4自定义路径做中心
		
		float m_fEmitterSpinAngle; //发射器旋转角度--弧度为单位---顺时针为正---线、矩形、自定义路径形状
							   //旋转中心从PSL中获得

		FPOINT m_pointObject; //弹道跟踪时的目标点

		float m_fEmissionScale; //psl放大倍数 --与psl文件中的m_fpslzoom相乘
		FPOINT m_point0; //1 线发射的两个点..
		FPOINT m_point1; //
		float m_fEmissionAngle; //线发射时的发射方向..
		CRECT m_rctEmission; //3 矩形区域发射位置..
		
		PSModifyParam()
		{
			m_pointCent = FPOINT(400, 400);
			m_fEmitterSpinAngle = 0.0f;
			m_pointObject = FPOINT(200, 200);

			m_fEmissionScale = 1.0f;
			m_point0 = FPOINT(0.0f, 0.0f);
			m_point1 = FPOINT(800.0f, 0.0f);
			m_fEmissionAngle = 0.0f;
			m_rctEmission = CRECT(100, 100, 300, 300);
		}
	};

	struct PSParameter
	{
		PSModifyParam m_psModifyParam;
		//还有字符参数..？
		DWORD color;
		float time; //ps播放时间..
		int layer;
		bool m_bScreenCoord; //以下坐标是否屏幕坐标。默认false，表示以下坐标均为世界坐标。界面特效置true，其它均为false
		bool cycle;
		bool autorender;//层渲染时为false则不绘制..

		bool m_bVertexRHWflag; //默认为true,为false则表示顶点结构为D3DFVF_XYZ …… 需要经过D3D顶点坐标变换过程

		PSParameter()
		{
			m_bScreenCoord = false;
			m_bVertexRHWflag = true;
			color = PSDEFAULTCOLOR; //使用psl颜色
			time = PSDEFAULTTIME; //使用psl时间
			layer = 0;
			cycle = false;		//是否循环
			autorender = false;
		}
	};

	enum PSL_MOVETYPE
	{
		PSLMOVETYPE_DEFAULT = 0,
		PSLMOVETYPE_FOLLOWSPRITE = 1,
		PSLMOVETYPE_FULLSCREEN = 2,
	};

	enum PSL_PARTICLEPATHPOSTYPE
	{//有自定义路径的粒子特效初始产生位置
		PARTICLEPATHPOS_START = 0, //路径头
		PARTICLEPATHPOS_RANDOM = 1, //从上次死亡粒子位置列表里取
		PARTICLEPATHPOS_PREPOS = 2, //随机
	};

	enum PSL_PARTICLESIZERANDTYPE
	{//sizeRand的使用方式..
		PARTICLESIZERAND_XY = 0, //xy均随机
		PARTICLESIZERAND_X = 1, //只随机x
		PARTICLESIZERAND_Y = 2, //只随机y
	};

	enum PSL_EMISSIONTYPE
	{
		PSLTYPE_POINT = 0,
		PSLTYPE_LINE = 1,
		PSLTYPE_CIRCLE = 2,
		PSLTYPE_RECT = 3,
		PSLTYPE_CUSTOMPATH = 4,
	};

	enum PSL_PATHMIRRORTYPE
	{
		PATHMIRRORTYPE_NONE = 0, //不镜像
		PATHMIRRORTYPE_LR = 1,   //左右镜像
		PATHMIRRORTYPE_TB = 2,   //上下镜像
		PATHMIRRORTYPE_LRTB = 3, //关于原点对称 --中心对称
	};

	enum PSL_CONFIGREQ //psl对机子的配置要求..
	{
		PSLCONFIGREQ_L = 0, //低配
		PSLCONFIGREQ_M = 1, //中配
		PSLCONFIGREQ_H = 2, //高配
	};

	struct PSINFO
	{
		CRECT m_rctMaxBox; //粒子特效包围盒。
		FPOINT m_pointCent; //意义同PSParameter中的m_pointCent
		int m_moveType; //PSL_MOVETYPE
		int m_emisType; //PSL_EMISSIONTYPE
		int m_nPSLConfigReq; //指定psl的优先级
		bool m_bCycle;

		PSINFO()
		{
			m_rctMaxBox = CRECT(0, 0, 200, 200);
			m_pointCent = FPOINT(400, 400);
			m_moveType = PSLMOVETYPE_DEFAULT;
			m_emisType = PSLTYPE_POINT;
			m_bCycle = false;
			m_nPSLConfigReq = PSLCONFIGREQ_L;
		}
	};

	enum PSL_ERRORTYPE
	{
		PSLLOADERROR_SUCC = 100, //未定义
		PSLLOADERROR_FILE = 5, //特效文件打开出错..
		PSLLOADERROR_TEXTURE = 6, //特效所使用纹理加载出错
		PSLLOADERROR_PATH = 7, //特效所使用的路径文件加载出错
		PSLLOADERROR_SHAPELIST = 8, //转场特效所使用的前景图分块数据文件加载出错
	};

	enum PSL_ABSRTACTSHAPETYPE
	{
		PSLSHAPETYPE_VORONOI = 1,
		PSLSHAPETYPE_RECTANGLE = 2,
		PSLSHAPETYPE_TRIANGLE = 3,
	};

}