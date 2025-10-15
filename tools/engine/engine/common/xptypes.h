#ifndef __Nuclear_XPTYPES_H
#define __Nuclear_XPTYPES_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <math.h>
#include "xbuffer.h"
#include "xpmaths.h"

namespace Nuclear
{
	// 颜色定义
	//typedef DWORD XPCOLOR; // argb
	union XPCOLOR
	{
		DWORD data;
		struct {
			// todo 大小端
			unsigned char b;
			unsigned char g;
			unsigned char r;
			unsigned char a;
		};
		XPCOLOR() : data(0xFFFFFFFF) {}
		XPCOLOR(DWORD color) : data(color) {}
		XPCOLOR(const XPVECTOR4 &v) : 
			b(static_cast<unsigned char>(v.x * 255)), 
			g(static_cast<unsigned char>(v.y * 255)), 
			r(static_cast<unsigned char>(v.z * 255)), 
			a(static_cast<unsigned char>(v.w * 255)) {}
		XPCOLOR(unsigned char _a, unsigned char _r, unsigned char _g, unsigned char _b) :
		a(_a), r(_r), g(_g), b(_b) {}
		void Reset(const XPVECTOR4 &v)
		{
			b = static_cast<unsigned char>(v.x * 255);
			g = static_cast<unsigned char>(v.y * 255); 
			r = static_cast<unsigned char>(v.z * 255);
			a = static_cast<unsigned char>(v.w * 255);
		}
		XPVECTOR4 ToXPVECTOR4() const
		{
			return XPVECTOR4(static_cast<float>(b) / 255, 
				static_cast<float>(g) / 255, 
				static_cast<float>(r) / 255, 
				static_cast<float>(a) / 255);
		}
		XPCOLOR& operator *= ( const XPVECTOR4& color )
		{
			b = static_cast<unsigned char>(b * color.x);
			g = static_cast<unsigned char>(g * color.y);
			r = static_cast<unsigned char>(r * color.z);
			a = static_cast<unsigned char>(a * color.w);
			return *this;
		}
		XPCOLOR operator * ( const XPVECTOR4& color ) const
		{
			XPCOLOR result = *this;
			return result *= color;
		}

		void Reset(unsigned char _b, unsigned char _g, unsigned char _r, unsigned char _a)
		{
			b = _b; g = _g; r = _r; a = _a;
		}
	};

	const XPCOLOR XPCOLOR_ZERO = 0;
	const XPCOLOR XPCOLOR_FULL = 0xffffffff;
	const XPCOLOR XPCOLOR_RED = 0xffff0000;
	const XPCOLOR XPCOLOR_GREEN = 0xff00ff00;
	const XPCOLOR XPCOLOR_BLUE = 0xff0000ff;
	const XPCOLOR XPCOLOR_BLACK = 0xff000000;
	const XPCOLOR XPCOLOR_WHITE = 0xffffffff;

	#define XPCOLOR_ARGB(a,r,g,b) \
		((XPCOLOR)((((a)&0xff)<<24)|(((r)&0xff)<<16)|(((g)&0xff)<<8)|((b)&0xff)))
	#define XPCOLOR_RGBA(r,g,b,a) XPCOLOR_ARGB(a,r,g,b)
	#define XPCOLOR_RGB(r,g,b)   XPCOLOR_ARGB(0xff,r,g,b)

	const int XP_LIGNT_RANGE = 5000;
	const int XP_DAY_START_TIME =    750000;
	const int XP_NIGHT_START_TIME = 6750000;
	const int XP_A_DAY_TIME = 7200000;//1000 * 60 * 60 * 2;//两个小时

	enum XPMOUSE_TEST_MODE
	{
		XPTM_PIC_RECT = 0,	//用图片的框检测
		XPTM_POLYGON,		//用美术定义的多边形区域检测
		XPTM_ALPHA,			//用Alpha检测（需要Lock资源出来）
	};

	enum XPEFFECT_SCALE_TYPE
	{
		XPEST_NOSCALE = 0,
		XPEST_ONLY_SCALE_POS,
		XPEST_ALLSCALE,
	};
//	const float XP_LKO_ZOOM = 2.0f; //最激烈程度放大 2倍
//	const float XP_LKO_TIME = 5.0f; //恢复正常需时间 5秒
	enum XPWINDOW_STATE
	{
		XPWS_ACTIVE = 0,	//激活
		XPWS_INACTIVE,		//在后面（没有激活）
		XPWS_MINIMIZE,		//最小化
	};

	// 多边形填充模式
	enum XPFILLMODE
	{
		XPFM_WIREFRAME = 0,
		XPFM_SOLIDFILL = 1
	};

	// 纹理加载的位置
	enum XPPOOL_TYPE {
		XPPOOL_VIDEOMEM = 0,	// 显存中
		XPPOOL_MANAGED = 1,		// 自动管理
		XPPOOL_SYSTEMMEM = 2,   // 内存中
	};

	enum XPTEXTUREADDRESS //纹理寻址模式
	{
		XPTEXADDRESS_WRAP = 1,
		XPTEXADDRESS_MIRROR = 2,
		XPTEXADDRESS_CLAMP = 3, //默认值...
		XPTEXADDRESS_BORDER = 4, //目前在游戏客户端画右上角圆形小地图用到
		XPTEXADDRESS_MIRRORONCE = 5,
		XPTEXADDRESS_FORCE_DWORD = 0x7fffffff
	};


	#define XPMAKEFOURCC(ch0, ch1, ch2, ch3)                              \
                ((DWORD)(BYTE)(ch0) | ((DWORD)(BYTE)(ch1) << 8) |   \
                ((DWORD)(BYTE)(ch2) << 16) | ((DWORD)(BYTE)(ch3) << 24 ))

	// 纹理磁盘存储格式	
	enum XPIMAGE_FILEFORMAT 
	{
		XPIFF_BMP = 0,
		XPIFF_JPG = 1,
		XPIFF_TGA = 2,
		XPIFF_PNG = 3,
		XPIFF_DDS = 4,
		XPIFF_PPM = 5,
		XPIFF_DIB = 6,
		XPIFF_HDR = 7,
		XPIFF_PFM = 8,
		XPIFF_WEBP = 9,
		XPIFF_FORCE_DWORD = 0x7fffffff
	};

	// 纹理格式
	enum XPTEXTURE_FORMAT
	{
		XPTEXFMT_DEFAULT  = 0, // 由renderer来决定
		XPTEXFMT_A8R8G8B8 = 21,
		XPTEXFMT_A4R4G4B4 = 26,
		XPTEXFMT_R5G6B5 = 23,
		XPTEXFMT_DXT1 = XPMAKEFOURCC('D', 'X', 'T', '1'),
		XPTEXFMT_DXT2 = XPMAKEFOURCC('D', 'X', 'T', '2'),
		XPTEXFMT_DXT3 = XPMAKEFOURCC('D', 'X', 'T', '3'),
		XPTEXFMT_DXT4 = XPMAKEFOURCC('D', 'X', 'T', '4'),
		XPTEXFMT_DXT5 = XPMAKEFOURCC('D', 'X', 'T', '5'),
		XPTEXFMT_R3G3B2               = 27,
	};

	enum XPPIXEL_FORMAT //像素格式..
	{
		XPPIXELFMT_DEFAULT = 0,
		XPPIXELFMT_R5G6B5 = 1,
		XPPIXELFMT_A8R8G8B8 = 2,
		XPPIXELFMT_A4R4G4B4 = 3,
	};

	// 纹理混合类型
	enum XPTEXTURE_BLENDMODE
	{
		XPTEXBM_DEFAULT = 0,	// 默认
		XPTEXBM_MODULATE = 0,   // alpha混合
		XPTEXBM_ADDITIVE = 1,	// 色彩饱和
		XPTEXBM_COPY = 2,
		XPTEXBM_ADDITIVE_ALPHA = 3,//alpha Add
		XPTEXBM_MODULATE2X = 4,   // alpha混合
		XPTEXBM_ADDITIVE2X = 5,	// 色彩饱和
		XPTEXBM_MUTIPLY = 6, //正片叠底
		XPTEXBM_MUTIPLY_ALPHA = 7, //Alpha正片叠底，正确的用法应该是纹理不带Alpha，顶点每个颜色（包括Alpha）分量都要是alpha值
	};

	// 精灵的八个方向
	enum XPDIRECTION
	{
		XPDIR_TOP = 0,
		XPDIR_TOPRIGHT,
		XPDIR_RIGHT,
		XPDIR_BOTTOMRIGHT,
		XPDIR_BOTTOM,
		XPDIR_BOTTOMLEFT,
		XPDIR_LEFT,
		XPDIR_TOPLEFT,
	};
	enum
	{
		XPDIR_COUNT = XPDIR_TOPLEFT + 1
	};

//	const float XP_EFFECT_MAX_ALPHA = 0.8f;
//	const float XP_EFFECT_MIN_ALPHA = 0.2f;
	const int XP_EFFECT_CYCLE = 800;	//毫秒
	const int XP_EFFECT_HALF_CYCLE = XP_EFFECT_CYCLE / 2;	//毫秒
//	const float XP_EFFECT_ALPHA_DELTA = (XP_EFFECT_MAX_ALPHA - XP_EFFECT_MIN_ALPHA) / XP_EFFECT_HALF_CYCLE;
	const int XP_EFFECT_SCALE = 2;

	// 精灵的涉水模式
	enum WATER_SURFACE_TYPE
	{
		WST_ALWAYS_PLANE = 0,	//永远是水平的
		WST_OBEY_DIR,			//顺着精灵的方向
		WST_ATHWART_DIR,		//逆着精灵的方法
	};

	//特效绑在人身上的类型
	enum EFFECT_BIND_TYPE
	{
		STATIC_POINT = 0,	//绑在固定点，不受动作影响
		WEAPON_POINT,		//绑在武器上
	};

	enum XP_PREFETCH_RESULT
	{
		XPPR_OK = 0,			//所有资源都加载好了
		XPPR_BASE_ASYNC_OK,		//只加载好了第一帧
		XPPR_ASYNC_LOADING,		//正在加载中
	};

	//Title的handle，由客户端UI逻辑层管理和生成
	typedef unsigned int EntitativeTitleHandle;
	const EntitativeTitleHandle INVALID_ENTITATIVE_TITLE_HANDLE = 0;

	//战斗状态背景的handle
	typedef unsigned int WarBackgroundHandle;
	const WarBackgroundHandle INVALID_WAR_BACKGROUND_HANDLE = 0;

	//title的挂接点
	enum XPTITLE_ALIGN
	{
		XPTA_DEFAULT = 0,
		XPTA_CENTER_BOTTOM = 0,
		XPTA_CENTER_TOP = 1,
	};

	//往地图上动态扔特效的层次，只有4层
	enum XPEFFECT_LAYER
	{
		XPEL_UNDER_SPRITE = 0,	//地表之上，精灵之下
		XPEL_SPRITE,            //精灵和遮挡层，引擎根据底座自动判断遮挡
		XPEL_ABOVE_SPRITE,      //精灵之上
		XPEL_TOP,				//最上层
	};

	enum XPEFFECT_BIND_TYPE
	{
		XPEBT_NULL = -1,
		XPEBT_ORIGIN = 0,
		XPEBT_EFFECT_POS,
	};

	enum XPEffectState
	{
		XPES_PLAYING,			//! 正在播放
		XPES_PAUSE,				//! 正处于暂停状态（目前可能就编辑器会用了）
		XPES_STOP,				//! 处于停止状态
		XPES_EMPTY,				//! 处于未加载资源状态
	};

	enum XPEffectType
	{
		XPET_ANI = 0,			//! 序列图动画
		XPET_PARTICLE,			//! 粒子
		XPET_AUDIO,				//! 声音
		XPET_GEFFECT,			//! 特效组
		XPET_LISTEFFECT,		//! 特效序列，给持续特效用
	};

	enum XPEffectAssureResResult
	{
		XPEARR_RESREADY = 0,	//! 资源已经可用（一般只在同步加载时候出现，但是如果经有同样的资源在内存里面，异步也会出现）
		XPEARR_LOADING,			//! 资源正在加载
		XPEARR_LOAD_ERROR,		//! 资源加载错误
	};

	enum XPPLAY_MODE
	{
		XPPM_LOOP,				//! 无限循环 
		XPPM_LOOPNUM,			//! 以一定的次数进行循环
		XPPM_TIME,				//! 播放指定时间
	};

	// 音频格式
	enum XPSOUND_TYPE
	{
		XPSOUNDTYPE_WAVE,
		XPSOUNDTYPE_OGG
	};

	enum XPBGSoundMode
	{
		XPBGSM_AUTO,
		XPBGSM_CUSTOM,
		XPBGSM_BATTLE,
	};

	enum CHECK_SOUND_STATE
	{
		CSS_CHECK_OK = 0,
		CSS_LOADING,
		CSS_FAILD
	};

	enum XPFADE_SIGN
	{
		XPFS_NORMAL = 0,
		XPFS_FADE_IN,
		XPFS_FADE_OUT,
		XPFS_MUTE,
	};

	class SoundWrap;
	typedef SoundWrap* SoundHandle;
	const SoundHandle INVALID_SOUND_HANDLE = 0;
	typedef std::set<SoundHandle> SoundSet;

	typedef unsigned int EngineSpriteHandle;
	const EngineSpriteHandle INVALID_ENGINE_SPRITE_HANDLE = 0;

	//世界模式
	enum XPWorldMode
	{
		XPWM_NORMAL = 0,
		XPWM_SIMPLE_BATTLE,
		XPWM_WORLD_BATTLE,
		XPWM_FLY,
	};

	enum XPScreenMode
	{
		XPSM_WINDOW = 0,
		XPSM_FADE_FULL_SCREEN,
		XPSM_REAL_FULL_SCREEN,
	};

	// 引擎的显示模式，可以从引擎得到或者设置
	struct XDisplayMode
	{
		int width;
		int height;
		int bitperpix; // 16 or 32 
		XPScreenMode screenMode;
		XDisplayMode() : width(800), height(600), bitperpix(32), screenMode(XPSM_WINDOW) { }
		XDisplayMode(int w, int h, int bpp = 32, XPScreenMode sm = XPSM_WINDOW) :width(w), 
			height(h), bitperpix(bpp), screenMode(sm) { }
		bool operator==(const XDisplayMode &df) const { 
			return width == df.width && height == df.height && bitperpix == df.bitperpix && screenMode == df.screenMode;
		}
		bool equalExceptScreenMode(const XDisplayMode &df) const {
			return width == df.width && height == df.height && bitperpix == df.bitperpix;
		}
	};

	//引擎里面绘制障碍的mask和对应的颜色
	struct XPMazeColor
	{
		unsigned int mask;
		XPCOLOR color;
	};
	typedef std::vector<XPMazeColor> XPMazeColors;


	typedef unsigned short ActionTypeFlag;//动作类型flag

	typedef int PictureHandle;
	const PictureHandle INVALID_PICTURE_HANDLE = 0;
	
	struct XBase
	{
		CPOINT center;
		CPOINT offset[4];				// 底座信息	暂定10*4个字节
	};

	struct XPBase//底座信息
	{
		CPOINT left;
		CPOINT right;
		XPBase() : left(-10, 0), right(10, 0) {}
	};

	struct PictureInfo 
	{
		int m_nPicWidth;
		int m_nPicHeight;
		XPTEXTURE_FORMAT m_texfmt;
		XPPOOL_TYPE m_pooltype;
		//
	};

	typedef int TextBlockHandle;
	const TextBlockHandle INVALID_TEXTBLOCK_HANDLE = 0;

	typedef int CursorHandle;
	const CursorHandle INVALID_CURSOR_HANDLE = 0;

	typedef HCURSOR XPCursorHandle;

	enum XPCONSOLE_INFO
	{
		XPCSINFO_FPS = 1 << 0,
		XPCSINFO_COORDINATE = 1 << 1,
		XPCSINFO_RENDERINFO = 1 << 2,
		XPCSINFO_SYSINFO = 1 << 3,
		XPCSINFO_WORLDINFO = 1 << 4,
		XPCSINFO_FRAMESTAT = 1 << 5,
		XPCSINFO_ALL = 0xfFfFfFfF
	};

	enum XPFRAMESTAT_TYPE
	{
		XPFRAMESTAT_FRAME_TIME = 0,
		XPFRAMESTAT_CAMERA_DIS,
		XPFRAMESTAT_FILE_IO,
		XPFRAMESTAT_TASK_TIME,
		XPFRAMESTAT_IAPP_MSG,
	};

	enum XPCAMERA_UPDATE_TYPE
	{
		XPCAMERA_HERO_ALWAYS_IN_CENTER = 0,	// 最简单的模式，视口始终以主角为中心
		XPCAMERA_UPDATE_TYPE2,	// 比较复杂，还比较乱，待整理(目前有问题)
		XPCAMERA_FOLLOW_HERO_WHEN_OUT_OF_INSENCE,// 主角在insence区域时，不修改视口，否则追随。
		XPCAMERA_FOLLOW_HEROS_DIR_WHEN_OUT_OF_INSENCE,// 主角在insence区域时，不修改视口，否则追随根据当前主角的移动方向追随。
		XPCAMERA_SMOOTH,		//梦诛那种方法，baiyu提供
	};

	const int XPFRAMESTAT_TYPE_COUNT = XPFRAMESTAT_IAPP_MSG + 1;

	class IRunnable
	{
	public:
		virtual ~IRunnable() { }

		// 不要在此函数中进行阻塞操作或者抛出自定义异常
		virtual void Run() = 0;
	};

	class ITimer
	{
	public:
		virtual void OnTimer() = 0; 
		virtual ~ITimer() { }
	};

	struct XPMaskParam
	{
		std::wstring picuri;	//资源路径，为空代表上一张图
		XPTEXTURE_BLENDMODE blendMode;
		unsigned char lastAlpha;
		unsigned char alpha;
	};

	struct XPFontEffectParam
	{ //目前之只有放大和缩小, 有需求了再继续丰富
		FPOINT fMaxScale; //放大
		FPOINT fMinScale; //缩小
		float fCycle;	  //周期: 秒

		int nPlayTime;  //! 总共播放的时间
		int nLoopNum;	  //! 循环次数

		FRECT* pFrctSrc;

		XPFontEffectParam()
		{
			fMaxScale = FPOINT(5.0f, 5.0f);
			fMinScale = FPOINT(0.1f, 0.1f);
			fCycle = 5.0f;

			nPlayTime = -1;
			nLoopNum = 1;

			pFrctSrc = NULL;
		}
	};

	struct XPColorBalanceParam
	{
		float fCoefr;		//-1----+1 色彩分量调节
		float fCoefg;
		float fCoefb;
		float fLightness;	//-1----+1 亮度调节
		float fContrastk;//0--1坐标.. 控制对比度函数的斜率和y轴截距
		float fContrastb;

		float fCentx; //精灵所在位置 ---用于精灵周围的特殊处理
		float fCenty;

	//	float fPicPosx;
	//	float fPicPosy;
	//	float fPicWidth;
	//	float fPicHeight;
	};

	struct XPEmbossParam
	{
		float fSizex;	//一般设置为图像宽、高的n倍(0.2<n<=1)左右波动
		float fSizey;
		int ndir;//8方向 0---7
		int nColorOpType; //顶点颜色与纹理的混合类型..
	};

	struct XPRadialBlurParam
	{
		FPOINT BlurCent;	//模糊中心  //左上角(0,0) 右下角(1,1)
		float blurDegree;	//模糊程度 //0.0---1.0
		int nBlurType;		//0--线状模糊、1--圆行模糊..
		int nColorOpType; //顶点颜色与纹理的混合类型..
	};

	struct XPGrayParam
	{
		int nType;		//0，默认参数,普通灰度图效果；1，r保留，其它分量置0；2，g...；3，b...；
		int nColorOpType; //顶点颜色与纹理的混合类型..
	};

	struct XPAlphaParam
	{
	};

	struct XPDistortionShaderParam //空间扭曲特效shader
	{
		float fdegree; //扭曲程度0.0f----1.0f //每帧update这个值形成动画,0就恢复原始样子了..
		FPOINT fcent; //扭曲中心  //左上角(0,0) 右下角(1,1)
		float fMaxDis; //扭曲半径 //0.0f----1.0f
		int nType1; //控制方向 //挤压还是阔张等...
		int nType2; //控制形状
	};

	struct XPDyeShaderParam //染色shader
	{
		float fParam0[4];
		float fParam1[4];
		float fParam2[4];
		XPDyeShaderParam(){ 
			fParam0[0] = 0;
			fParam0[1] = 0;
			fParam0[2] = 0;
			fParam0[3] = 0;
			fParam1[0] = 2;
			fParam1[1] = 3;
			fParam1[2] = 4;
			fParam1[3] = 0.5;
			fParam2[0] = 2;
			fParam2[1] = 3;
			fParam2[2] = 4;
			fParam2[3] = 0.5;
		}
	};

	struct XPFontOutlineParam //提取特殊字符图像边缘轮廓
	{
		XPCOLOR dwOutlineColor; //float4 g_outlineColor
		int nTextureWidth; //float2 g_pixelSize
		int nTextureHeight;
	};

	struct XPGaussianBlurBlendParam //高斯模糊之后做混合,类似ps里的模糊之后图层柔光叠加等效果
	{
		int nTextureWidth;
		int nTextureHeight;
		int nBlendType; //叠加模式 0--柔光 1---强光
	};

	enum XPDISTORTIONMESHTYPE //网格扭曲类型
	{
		XPDISTORTIONTYPE_HORIZONTAL_1 = 0, //固定水平方向一条边
		XPDISTORTIONTYPE_HORIZONTAL_2 = 1,
		XPDISTORTIONTYPE_VERTICAL_1 = 2,	//固定垂直方向一条边
		XPDISTORTIONTYPE_VERTICAL_2 = 3,
		XPDISTORTIONTYPE_DIAGONAL = 4,//固定左上或右下顶点
	};

	enum XPIMG_BORDERPOS
	{ //img文件的底座4个点标示...
		enum_left	=	0,
		enum_bottom	=	1,
		enum_right	=	2,
		enum_up		=	3,
	};

	enum XPCOMPUTERCONFIGTYPE
	{ //计算机配置
		XPCOMPUTERCONFIG_L = 0, //低配
		XPCOMPUTERCONFIG_M = 1, //中配
		XPCOMPUTERCONFIG_H = 2, //高配
	};

	struct UTCT1VERTEX
	{
		float x,y,z;
		DWORD c;
		float u,v;
		UTCT1VERTEX(float _x=0.0f, float _y=0.0f, float _z=0.0f, DWORD _c=0xFFFFFFFF, float _u=0.0f, float _v=0.0f) : 
		x(_x), y(_y), z(_z), c(_c), u(_u), v(_v) {}
	};

	struct TCT1VERTEX
	{
		float x,y,z;
		float rhw;
		DWORD c;
		float u,v;
		float u2,v2;
		TCT1VERTEX(float _x=0.0f, float _y=0.0f, float _z=0.0f, float _rhw=1.0f, DWORD _c=0xFFFFFFFF, float _u=0.0f, float _v=0.0f,float _u2=0.0f, float _v2=0.0f) : 
		x(_x), y(_y), z(_z), rhw(_rhw), c(_c), u(_u), v(_v), u2(_u2), v2(_v2) {}
	};

	struct TCVERTEX
	{
		float x,y,z;
		float rhw;
		DWORD c;
		TCVERTEX(float _x=0.0f, float _y=0.0f, float _z=0.0f, float _rhw=1.0f, DWORD _c=0xFFFFFFFF) : x(_x), y(_y), z(_z), rhw(_rhw), c(_c) {}
	};

}


#endif