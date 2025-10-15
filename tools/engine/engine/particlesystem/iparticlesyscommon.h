#pragma once
#include "..\common\xptypes.h"

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

	struct LINE
	{
		CPOINT pt0;
		CPOINT pt1;
		LINE(CPOINT point0 = CPOINT(0, 0), CPOINT point1 = CPOINT(0, 0))
		{
			pt0 = point0;
			pt1 = point1;
		}
	};

	struct PSModifyParam 
	{
		//m_pointCent是使用世界坐标还是屏幕坐标要根据创建参数中m_bScreenCoord来确定
		//m_pointCent  外面传入的是世界坐标，内部使用时已经转为屏幕坐标了。
		FPOINT m_pointCent; //0 点发射--1线、2圆、3矩形 4自定义路径做中心
		
		float m_fEmitterSpinAngle; //发射器旋转角度--弧度为单位---顺时针为正---线、矩形、自定义路径形状
							   //旋转中心从粒子中获得

		FPOINT m_pointObject; //弹道跟踪时的目标点
		float m_fEmissionScale; //粒子放大倍数 --与psl文件中的m_fpslzoom相乘
				
		//发射器范围改变之后内部相应地修改了粒子数和发射频率
		float m_fLineLength;//线发射的线长度 //目前的应用:文字背景特效..2009-10-15
		//只考虑水平方向的线...
	//	float m_fLineDeltaDis;//线相对于中心点的偏移距离---左上负、右下正
		float m_fRctWidth;//矩形的长和宽
		float m_fRctHeight;

		//粒子盲区设置---进入该区域的粒子就不可见了..一系列相对于发射器中心的矩形区域
		std::vector<CRECT> m_vectorRctBlindArea;
		//阻挡线..粒子不能越过，如果粒子在可移动方向上没有速度就会表现为停止...
		std::vector<LINE> m_vectorBlockLine;
		 //反射线...粒子碰到之后按照光线发射规律改变运动方向
		std::vector<LINE> m_vectorReflectLine;
		
		//以上坐标值都是相对于m_pointCent的坐标

		XPEFFECT_SCALE_TYPE m_scaleType;
		float m_fParticleSizeCoef;
		float m_fParticlePosCoef;

		//指定两个目标点，在连线上产生粒子.. 绘制闪电链...
		FPOINT m_fpoint0;
		FPOINT m_fpoint1;
		float m_fLineEmitterRotate; //线性发射器的旋转角度，在这里是用来旋转粒子坐标的...弧度
		//

		PSModifyParam()
		{
			m_pointCent = FPOINT(400, 400);
			m_fEmitterSpinAngle = 0.0f;
			m_pointObject = FPOINT(200, 200);

			m_fEmissionScale = 1.0f;

			m_fLineLength = -1.0f; //这三个参数如果小于0，则不改变美术资源参数
			m_fRctWidth = -1.0f;
			m_fRctHeight = -1.0f;
			m_vectorRctBlindArea.clear();
			m_vectorBlockLine.clear();
			m_vectorReflectLine.clear();

			m_scaleType = XPEST_NOSCALE;
			m_fParticleSizeCoef = 1.0f;
			m_fParticlePosCoef = 1.0f;

			m_fpoint0 = FPOINT(100, 100);
			m_fpoint1 = FPOINT(300, 300);
			m_fLineEmitterRotate = 0.0f;
		}
	};

	struct PSParameter
	{
		PSModifyParam m_psModifyParam;
		//还有字符参数..？
		DWORD color;
		float time; //ps播放时间..
		int layer;
		bool m_bScreenCoord; //是否屏幕坐标。默认false，表示为世界坐标。
		//目前界面特效和聊天窗口的文字背景特效置true，其它均为false
		bool cycle;
		bool autorender;//层渲染时为false则不绘制..

		bool m_bVertexRHWflag; //默认为true,为false则表示顶点结构为D3DFVF_XYZ …… 需要经过D3D顶点坐标变换过程

		BOOL m_bModifyLineEmitter; //修改线发射器标记，置为true则修改粒子中对应参数
		FPOINT m_ptLineEmitterStrt; //特效链 2011-10-28
		FPOINT m_ptLineEmitterEnd;

		PSParameter()
		{
			m_bScreenCoord = false;
			m_bVertexRHWflag = true;
			color = PSDEFAULTCOLOR; //使用粒子颜色
			time = PSDEFAULTTIME; //使用粒子时间
			layer = 0;
			cycle = false;		//是否循环
			autorender = false;

			m_bModifyLineEmitter = false;
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
		PARTICLESIZERAND_XY = 0, //xy均 等值 随机
		PARTICLESIZERAND_X = 1, //只随机x
		PARTICLESIZERAND_Y = 2, //只随机y
		PARTICLESIZERAND_ZOOM = 3, //xy均 等比 随机
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

	enum PSL_CONFIGREQ //粒子对机子的配置要求..
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
		int m_nPSLConfigReq; //指定粒子的优先级
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
		PSLSHAPETYPE_2PIC = 4, //分成2块图片沿着分割线方向相向运动...
	};

	//enum PSL_LOADRESFLAG
	//{
	//	PSLLOADRESFLAG_UNKNOWERROR = -1, //未知错误
	//	PSLLOADRESFLAG_READFILEERROR = 0,   //读文件错误
	//	PSLLOADRESFLAG_SUCC = 1,    //成功
	//	PSLLOADRESFLAG_LOADING = 2, //正在读取
	//};

}