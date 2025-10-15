#ifndef __Nuclear_XPTYPES_H__
#define __Nuclear_XPTYPES_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <math.h>
#include "nuxbuffer.h"
#include "nuxpmaths.h"

namespace Nuclear
{
#ifndef WIN32
    enum Messages
	{
        WM_LBUTTONDOWN,
        WM_LBUTTONUP,
        WM_RBUTTONDOWN,
        WM_RBUTTONUP,
		WM_MOUSEMOVE,
        WM_LONGPRESS,
        WM_CLICK,
        WM_DOUBLECLICK,
        WM_SLIDE,
        WM_DRAG,
    };
#else
	enum Messages
	{
        WM_LONGPRESS,
        WM_CLICK,
        WM_DOUBLECLICK,
        WM_SLIDE,
        WM_DRAG,
    };
#endif
#ifdef XPP_IOS
	//typedef DWORD NuclearColor; // argb
	union NuclearColor
	{
		DWORD data;
		struct
		{
			unsigned char r;
			unsigned char g;
			unsigned char b;
			unsigned char a;
		};
		NuclearColor() : data(0xFFFFFFFF) {}
		NuclearColor(DWORD color) : data(color) {}
		NuclearColor(const NuclearVector4 &v) : 
        b(static_cast<unsigned char>(v.x * 255)), 
        g(static_cast<unsigned char>(v.y * 255)), 
        r(static_cast<unsigned char>(v.z * 255)), 
        a(static_cast<unsigned char>(v.w * 255)) {}
		NuclearColor(unsigned char _a, unsigned char _r, unsigned char _g, unsigned char _b) :
		a(_a), r(_r), g(_g), b(_b) {}
		void Reset(const NuclearVector4 &v)
		{
			b = static_cast<unsigned char>(v.x * 255);
			g = static_cast<unsigned char>(v.y * 255); 
			r = static_cast<unsigned char>(v.z * 255);
			a = static_cast<unsigned char>(v.w * 255);
		}
		NuclearVector4 ToXPVECTOR4() const
		{
			return NuclearVector4(static_cast<float>(b) / 255, 
                             static_cast<float>(g) / 255, 
                             static_cast<float>(r) / 255, 
                             static_cast<float>(a) / 255);
		}
		NuclearColor& operator *= ( const NuclearVector4& color )
		{
			b = static_cast<unsigned char>(b * color.x);
			g = static_cast<unsigned char>(g * color.y);
			r = static_cast<unsigned char>(r * color.z);
			a = static_cast<unsigned char>(a * color.w);
			return *this;
		}
		NuclearColor operator * ( const NuclearVector4& color ) const
		{
			NuclearColor result = *this;
			return result *= color;
		}
        
		void Reset(unsigned char _b, unsigned char _g, unsigned char _r, unsigned char _a)
		{
			b = _b; g = _g; r = _r; a = _a;
		}
        
	};
    
	const NuclearColor XPCOLOR_ZERO = 0;
	const NuclearColor XPCOLOR_FULL = 0xffffffff;
	const NuclearColor XPCOLOR_RED = 0xff0000ff;
	const NuclearColor XPCOLOR_GREEN = 0xff00ff00;
	const NuclearColor XPCOLOR_BLUE = 0xffff0000;
	const NuclearColor XPCOLOR_BLACK = 0xff000000;
	const NuclearColor XPCOLOR_WHITE = 0xffffffff;
    
#define XPCOLOR_ARGB(a,r,g,b) \
((NuclearColor)((((a)&0xff)<<24)|(((b)&0xff)<<16)|(((g)&0xff)<<8)|((r)&0xff)))
#define XPCOLOR_RGBA(r,g,b,a) XPCOLOR_ARGB(a,r,g,b)
#define XPCOLOR_RGB(r,g,b)   XPCOLOR_ARGB(0xff,r,g,b)
    
#elif XPP_WIN
    // 颜色定义
	//typedef DWORD NuclearColor; // argb
	union NuclearColor
	{
		DWORD data;
		struct
		{
			unsigned char b;
			unsigned char g;
			unsigned char r;
			unsigned char a;
		};
		NuclearColor() : data(0xFFFFFFFF) {}
		NuclearColor(DWORD color) : data(color) {}
		NuclearColor(const NuclearVector4 &v) : 
        b(static_cast<unsigned char>(v.x * 255)), 
        g(static_cast<unsigned char>(v.y * 255)), 
        r(static_cast<unsigned char>(v.z * 255)), 
        a(static_cast<unsigned char>(v.w * 255)) {}
		NuclearColor(unsigned char _a, unsigned char _r, unsigned char _g, unsigned char _b) :
		a(_a), r(_r), g(_g), b(_b) {}
		void Reset(const NuclearVector4 &v)
		{
			b = static_cast<unsigned char>(v.x * 255);
			g = static_cast<unsigned char>(v.y * 255); 
			r = static_cast<unsigned char>(v.z * 255);
			a = static_cast<unsigned char>(v.w * 255);
		}
		NuclearVector4 ToXPVECTOR4() const
		{
			return NuclearVector4(static_cast<float>(b) / 255, 
                             static_cast<float>(g) / 255, 
                             static_cast<float>(r) / 255, 
                             static_cast<float>(a) / 255);
		}
		NuclearColor& operator *= ( const NuclearVector4& color )
		{
			b = static_cast<unsigned char>(b * color.x);
			g = static_cast<unsigned char>(g * color.y);
			r = static_cast<unsigned char>(r * color.z);
			a = static_cast<unsigned char>(a * color.w);
			return *this;
		}
		NuclearColor operator * ( const NuclearVector4& color ) const
		{
			NuclearColor result = *this;
			return result *= color;
		}
        
		void Reset(unsigned char _b, unsigned char _g, unsigned char _r, unsigned char _a)
		{
			b = _b; g = _g; r = _r; a = _a;
		}
        
	};
    
	const NuclearColor XPCOLOR_ZERO = 0;
	const NuclearColor XPCOLOR_FULL = 0xffffffff;
	const NuclearColor XPCOLOR_RED = 0xffff0000;
	const NuclearColor XPCOLOR_GREEN = 0xff00ff00;
	const NuclearColor XPCOLOR_BLUE = 0xff0000ff;
	const NuclearColor XPCOLOR_BLACK = 0xff000000;
	const NuclearColor XPCOLOR_WHITE = 0xffffffff;
    
	#define XPCOLOR_ARGB(a,r,g,b) \
	((NuclearColor)((((a)&0xff)<<24)|(((r)&0xff)<<16)|(((g)&0xff)<<8)|((b)&0xff)))
	#define XPCOLOR_RGBA(r,g,b,a) XPCOLOR_ARGB(a,r,g,b)
	#define XPCOLOR_RGB(r,g,b)   XPCOLOR_ARGB(0xff,r,g,b)
	#else
	#error "cur platform is not supported!"
	#endif

	//不需要LocalSort了，排序优化得挺不错的
	const int FIRE_SORT_FREQUENCE = 1;		//每多少帧全排序一次

	const int FIRE_LIGNT_RANGE = 5000;
	const int FIRE_DAY_START_TIME = 750000;
	const int FIRE_NIGHT_START_TIME = 6750000;
	const int FIRE_A_DAY_TIME = 7200000;//1000 * 60 * 60 * 2;//两个小时
	const int FIRE_MID_DAY = FIRE_DAY_START_TIME + (FIRE_NIGHT_START_TIME - FIRE_DAY_START_TIME) / 2;
	const int FIRE_MID_NIGHT = (FIRE_NIGHT_START_TIME + (FIRE_DAY_START_TIME + FIRE_A_DAY_TIME - FIRE_NIGHT_START_TIME) / 2) % FIRE_A_DAY_TIME;

	enum Nuclear_ResState
	{
		XPRS_UNAVAILABLE = -1,
		XPRS_AVAILABLE_NEED_LOOKUP = 0,
		XPRS_AVAILABLE_NEED_NOT_LOOKUP,
	};

	enum Nuclear_Mouse_TestMode
	{
		XPTM_PIC_RECT = 0,	//用图片的框检测
		XPTM_POLYGON,		//用美术定义的多边形区域检测
		XPTM_ALPHA,			//用Alpha检测（需要Lock资源出来）
	};

	enum Nuclear_EffectScaleType
	{
		XPEST_NOSCALE = 0,
		XPEST_ONLY_SCALE_POS,
		XPEST_ALLSCALE,
	};
	enum NuclearWindowState
	{
		XPWS_ACTIVE = 0,	//激活
		XPWS_INACTIVE,		//在后面（没有激活）
		XPWS_MINIMIZE,		//最小化
	};

	// 抗锯齿参数
	enum NuclearMultiSampleType
	{
		XPMULTISAMPLE_NONE            =  0,
		//XPMULTISAMPLE_NONMASKABLE     =  1,	//先不提供
		XPMULTISAMPLE_2_SAMPLES       =  2,
		XPMULTISAMPLE_3_SAMPLES       =  3,
		XPMULTISAMPLE_4_SAMPLES       =  4,
		XPMULTISAMPLE_5_SAMPLES       =  5,
		XPMULTISAMPLE_6_SAMPLES       =  6,
		XPMULTISAMPLE_7_SAMPLES       =  7,
		XPMULTISAMPLE_8_SAMPLES       =  8,
		XPMULTISAMPLE_9_SAMPLES       =  9,
		XPMULTISAMPLE_10_SAMPLES      = 10,
		XPMULTISAMPLE_11_SAMPLES      = 11,
		XPMULTISAMPLE_12_SAMPLES      = 12,
		XPMULTISAMPLE_13_SAMPLES      = 13,
		XPMULTISAMPLE_14_SAMPLES      = 14,
		XPMULTISAMPLE_15_SAMPLES      = 15,
		XPMULTISAMPLE_16_SAMPLES      = 16,
	};

	// 纹理加载的位置
	enum NuclearPoolType
	{
		XPPOOL_VIDEOMEM = 0,	// 显存中
		XPPOOL_MANAGED = 1,		// 自动管理
		XPPOOL_SYSTEMMEM = 2,   // 内存中
	};

	enum NuclearTextureAddRess //纹理寻址模式
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
	enum NuclearImageFileFormat 
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
        XPIFF_PVR2 = 9,
        XPIFF_PVR4 = 10,
		XPIFF_ATC = 11,
		XPIFF_WEBP = 12,
		XPIFF_FORCE_DWORD = 0x7fffffff
	};

	// 纹理格式
	enum NuclearTextureFormat
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
	};

	enum NuclearPixelFormat //像素格式..
	{
		XPPIXELFMT_DEFAULT = 0,
		XPPIXELFMT_R5G6B5 = 1,
		XPPIXELFMT_A8R8G8B8 = 2,
		XPPIXELFMT_A4R4G4B4 = 3,
	};

	// 纹理混合类型
	enum NuclearTextureBlendMode
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
	enum NuclearDirection
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

	//向下向右为正
	template<class T>
	NuclearDirection TransVector2XPDirection(const T &direction)
	{
		// 根据 form 到 to 与 X 轴的夹角判断方向。
		// 不使用 sqrt 计算，这样会丢失符号，返回时根据 dx 或者 dy 的符号再判断一次。

		// cos(角度) * cos(角度)
		float cos2 = direction.x * direction.x / (float)(direction.x * direction.x + direction.y * direction.y);

		if (cos2 > 0.8535534) // abs(角度) < 22.5. 0.8535534 = cos(22.5) * cos(22.5)
			return direction.x > 0 ? XPDIR_RIGHT : XPDIR_LEFT;

		if (cos2 > 0.1464466) // abs(角度) < 67.5. 0.1464466 = cos(67.5) * cos(67.5)
			return direction.x > 0
			? ( direction.y > 0 ? XPDIR_BOTTOMRIGHT : XPDIR_TOPRIGHT)
			: ( direction.y > 0 ? XPDIR_BOTTOMLEFT : XPDIR_TOPLEFT);

		return direction.y > 0 ? XPDIR_BOTTOM : XPDIR_TOP;
	}

	// 这里得到的方向为世界坐标方向
	inline NuclearFPoint TransXPDirection2Vector(NuclearDirection direction)
	{
		switch (direction)
		{
		case XPDIR_TOP:         return NuclearFPoint(0.0f, -1.0f);
		case XPDIR_TOPRIGHT:    return NuclearFPoint(1.0f, -1.0f);
		case XPDIR_RIGHT:       return NuclearFPoint(1.0f, 0.0f);
		case XPDIR_BOTTOMRIGHT: return NuclearFPoint(1.0f, 1.0f);
		case XPDIR_BOTTOM:      return NuclearFPoint(0.0f, 1.0f);
		case XPDIR_BOTTOMLEFT:  return NuclearFPoint(-1.0f, 1.0f);
		case XPDIR_LEFT:        return NuclearFPoint(-1.0f, 0.0f);
		case XPDIR_TOPLEFT:     return NuclearFPoint(-1.0f, -1.0f);
		default:                return NuclearFPoint(0.0f, 0.0f); // no direction
		}
	}

	inline float TransXPDirection2Radian(NuclearDirection direction)
	{
		const float PI = 3.1415926f;
		static const float radianArr[8] =
		{
			-270 * PI / 180,
			-225 * PI / 180,
			-PI,
			-135 * PI / 180,
			-90 * PI / 180,
			-45 * PI / 180,
			0,
			45 * PI / 180,
		};
		return radianArr[direction];
	}

	const int FIRE_EFFECT_CYCLE = 800;	//毫秒
	const int FIRE_EFFECT_HALF_CYCLE = FIRE_EFFECT_CYCLE / 2;	//毫秒
	const int FIRE_EFFECT_SCALE = 2;

	// 精灵的涉水模式
	enum NuclearWaterSurfaceType
	{
		WST_ALWAYS_PLANE = 0,	//永远是水平的
		WST_OBEY_DIR,			//顺着精灵的方向
		WST_ATHWART_DIR,		//逆着精灵的方法
	};

	//特效绑在人身上的类型
	enum NuclearEffectBindType
	{
		STATIC_POINT = 0,	//绑在固定点，不受动作影响
		WEAPON_POINT,		//绑在武器上
	};

	enum NuclearPrefetchResult
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
	enum NuclearWarBackgroundType
	{
		XPWBT_CENTER = 0,
		XPWBT_SCALE,
	};

	//title的挂接点
	enum NuclearTitleAlign
	{
		XPTA_DEFAULT = 0,
		XPTA_CENTER_BOTTOM = 0,
		XPTA_CENTER_TOP = 1,
	};

	//精灵的挂接标志位
	enum NuclearSpriteAttachSign
	{
		XPSAS_TITLE = 1,		//名字就在它上面
	};

	enum NuclearSpriteLayer
	{
		XPSL_MIDDLE1 = 0, //中1层精灵
		XPSL_MIDDLE2,	//中2层精灵
		XPSL_FLY,	//飞行精灵
		XPSL_BATTLE, //战斗精灵
	};

	enum NuclearSpriteLayerCount
	{
		XPSL_WORLD_SPRITE_COUNT = XPSL_FLY + 1,
		XPSL_COUNT = XPSL_BATTLE + 1,
	};

	//动态扔特效的层次
	enum Nuclear_EffectLayer
	{
		XPEL_UNDER_SPRITE = 0,	//以下4个在未来某个时间内会删掉
		XPEL_SPRITE = 1,
		XPEL_ABOVE_SPRITE = 2,
		XPEL_TOP = 4,
		///////////////////////////////////////////////////////
		XPEL_NORMAL_UNDER_SPRITE = 0,		//场景里面，地表之上，精灵之下
		XPEL_NORMAL_SPRITE_1 = 1,           //场景里面，精灵和中1层，引擎根据底座自动判断遮挡
		XPEL_NORMAL_ABOVE_SPRITE = 2,       //场景里面，中1精灵之上
		XPEL_NORMAL_SPRITE_2 = 3,			//场景里面，精灵和中2层，引擎根据底座自动判断遮挡
		XPEL_NORMAL_TOP = 4,				//场景里面，最上层
		XPEL_BATTLE_LOW = 5,				//战斗里面，精灵之下
		XPEL_BATTLE_MID = 6,				//战斗里面，精灵层
		XPEL_BATTLE_HEIGHT = 7,				//战斗里面，精灵之上
	};

	enum Nuclear_EffectLayerCount
	{
		XPEL_WORLD_COUNT = XPEL_NORMAL_TOP + 1,
		XPEL_COUNT = XPEL_BATTLE_HEIGHT + 1,
	};

	enum Nuclear_EffectBindType
	{
		XPEBT_NULL = -1,
		XPEBT_ORIGIN = 0,
		XPEBT_EFFECT_POS,
	};

	enum Nuclear_EffectState
	{
		XPES_PLAYING,			//! 正在播放
		XPES_PAUSE,				//! 正处于暂停状态（目前可能就编辑器会用了）
		XPES_STOP,				//! 处于停止状态
		XPES_EMPTY,				//! 处于未加载资源状态
	};

	enum Nuclear_EffectType
	{
		XPET_ANI = 0,			//! 序列图动画
		XPET_PARTICLE,			//! 粒子
		XPET_AUDIO,				//! 声音
		XPET_GEFFECT,			//! 特效组
		XPET_LISTEFFECT,		//! 特效序列，给持续特效用
		XPET_SPINE,				//! Spine特效

		XPET_3D,				//! 3D特效
	};

	enum Nuclear_EffectAssureResResult
	{
		XPEARR_RESREADY = 0,	//! 资源已经可用（一般只在同步加载时候出现，但是如果经有同样的资源在内存里面，异步也会出现）
		XPEARR_LOADING,			//! 资源正在加载
		XPEARR_LOAD_ERROR,		//! 资源加载错误
	};

	enum NuclearPlayMode
	{
		XPPM_LOOP,				//! 无限循环 
		XPPM_LOOPNUM,			//! 以一定的次数进行循环
		XPPM_TIME,				//! 播放指定时间
	};

	// 音频格式
	enum NuclearSoundType
	{
		XPSOUNDTYPE_WAVE,
		XPSOUNDTYPE_OGG
	};

	enum NuclearBGSoundMode
	{
		XPBGSM_AUTO,
		XPBGSM_CUSTOM,
		XPBGSM_BATTLE,
	};

	enum NuclearCheckSoundState
	{
		CSS_CHECK_OK = 0,
		CSS_LOADING,
		CSS_FAILD
	};

	typedef unsigned int SoundHandle;
	const SoundHandle INVALID_SOUND_HANDLE = 0;
	typedef std::set<SoundHandle> SoundSet;

	typedef unsigned int EngineSpriteHandle;
	const EngineSpriteHandle INVALID_ENGINE_SPRITE_HANDLE = 0;

	enum NuclearSpriteType
	{
		XPSPRITETYPE_NULL = -1,
		XPSPRITETYPE_2D = 0,
		XPSPRITETYPE_3D = 1,
		XPSPRITETYPE_SPINE = 2,
	};

	//世界模式
	enum eNuclearWorldMode
	{
		XPWM_NORMAL = 0,
		XPWM_SIMPLE_BATTLE,
		XPWM_WORLD_BATTLE,
		XPWM_FLY,
	};

	enum NuclearScreenMode
	{
		XPSM_WINDOW = 0,
		XPSM_FADE_FULL_SCREEN,
		XPSM_REAL_FULL_SCREEN,
	};

	// 引擎的显示模式，可以从引擎得到或者设置
	struct NuclearDisplayMode
	{
		int width;
		int height;
		int bitperpix; // 16 or 32 
		NuclearScreenMode screenMode;
		NuclearDisplayMode() : width(800), height(600), bitperpix(32), screenMode(XPSM_WINDOW) { }
		NuclearDisplayMode(int w, int h, int bpp = 32, NuclearScreenMode sm = XPSM_WINDOW) :width(w), 
			height(h), bitperpix(bpp), screenMode(sm) { }
		bool operator==(const NuclearDisplayMode &df) const { 
			return width == df.width && height == df.height && bitperpix == df.bitperpix && screenMode == df.screenMode;
		}
		bool equalExceptScreenMode(const NuclearDisplayMode &df) const {
			return width == df.width && height == df.height && bitperpix == df.bitperpix;
		}
	};

	//引擎里面绘制障碍的mask和对应的颜色
	struct NuclearMazeColor
	{
		unsigned int mask;
		NuclearColor color;
	};
	typedef std::vector<NuclearMazeColor> XPMazeColors;


	typedef unsigned short ActionTypeFlag;//动作类型flag

	typedef int PictureHandle;
	const PictureHandle INVALID_PICTURE_HANDLE = 0;
	
	struct NuclearXBase
	{
		NuclearPoint center;
		NuclearPoint offset[4];				// 底座信息	暂定10*4个字节
	};

	struct NuclearBase//底座信息
	{
		NuclearPoint left;
		NuclearPoint right;
		NuclearBase() : left(-10, 0), right(10, 0) {}
	};

	struct NuclearPictureInfo 
	{
		int m_nPicWidth;
		int m_nPicHeight;
		NuclearTextureFormat m_texfmt;
		NuclearPoolType m_pooltype;
		std::wstring fileuri;
	};

	typedef int TextBlockHandle;
	const TextBlockHandle INVALID_TEXTBLOCK_HANDLE = 0;

	typedef int CursorHandle;
	const CursorHandle INVALID_CURSOR_HANDLE = 0;

	//遮罩半透明方案
	enum NuclearSpriteTranslucentType
	{
		XPSTT_NONE = 0,								//不透明
		XPSTT_ELEMENT_TRANSLUCENT,					//遮罩半透明
		XPSTT_SPRITE_TRANSLUCENT,					//精灵半透明
	};

	enum NuclearConsoleInfo
	{
		XPCSINFO_FPS = 1 << 0,
		XPCSINFO_COORDINATE = 1 << 1,
		XPCSINFO_RENDERINFO = 1 << 2,
		XPCSINFO_SYSINFO = 1 << 3,
		XPCSINFO_WORLDINFO = 1 << 4,
		XPCSINFO_FRAMESTAT = 1 << 5,
		XPCSINFO_ALL = 0xfFfFfFfF
	};

	enum NuclearFrameStatType
	{
		XPFRAMESTAT_FRAME_TIME = 0,
		XPFRAMESTAT_CAMERA_DIS,
		XPFRAMESTAT_FILE_IO,
		XPFRAMESTAT_TASK_TIME,
		XPFRAMESTAT_IAPP_MSG,
	};

	enum NuclearCameraUpdateType
	{
		XPCAMERA_HERO_ALWAYS_IN_CENTER = 0,	// 最简单的模式，视口始终以主角为中心
		XPCAMERA_UPDATE_TYPE2,	// 比较复杂，还比较乱，待整理(目前有问题)
		XPCAMERA_FOLLOW_HERO_WHEN_OUT_OF_INSENCE,// 主角在insence区域时，不修改视口，否则追随。
		XPCAMERA_FOLLOW_HEROS_DIR_WHEN_OUT_OF_INSENCE,// 主角在insence区域时，不修改视口，否则追随根据当前主角的移动方向追随。
		XPCAMERA_SMOOTH,
		XPCAMERA_MOVE_TO_POSITION_IN_DURATION,  // 在一定时间内移动镜头到指定位置
		XPCAMERA_ACCELERATE,  // 加速运动：目标远离时，加速追赶，接近目标时减速。
	};

	class XPCAMERA_MOVE_TO_POSITION_IN_DURATION_PARAMS
	{
	public:
		NuclearPoint mStartPosition, mTargetPosition;
		int mDuration;

	public:
		XPCAMERA_MOVE_TO_POSITION_IN_DURATION_PARAMS() : mDuration(0){}
	};

	const int XPFRAMESTAT_TYPE_COUNT = XPFRAMESTAT_IAPP_MSG + 1;

	class INuclearRunnable
	{
	public:
		virtual ~INuclearRunnable() { }

		// 不要在此函数中进行阻塞操作或者抛出自定义异常
		virtual void Run() = 0;
	};

	class INuclearTimer
	{
	public:
        int m_iTimerID;
        INuclearTimer() {m_iTimerID=0;}
		virtual void OnTimer() = 0; 
		virtual ~INuclearTimer() { }
	};

	struct sNuclearMaskParam
	{
		std::wstring picuri;	//资源路径，为空代表上一张图
		NuclearTextureBlendMode blendMode;
		unsigned char lastAlpha;
		unsigned char alpha;
	};

	struct sNuclearFontEffectParam
	{
		NuclearFPoint fMaxScale; //放大
		NuclearFPoint fMinScale; //缩小
		float fCycle;	  //周期: 秒

		int nPlayTime;  //! 总共播放的时间
		int nLoopNum;	  //! 循环次数

		NuclearFRectt* pFrctSrc;

		sNuclearFontEffectParam()
		{
			fMaxScale = NuclearFPoint(5.0f, 5.0f);
			fMinScale = NuclearFPoint(0.1f, 0.1f);
			fCycle = 5.0f;

			nPlayTime = -1;
			nLoopNum = 1;

			pFrctSrc = NULL;
		}
	};

	struct sNuclearTextureBlendParam 
	{
		float fcoef; //混合比例
	};

	struct sNuclearColorBalanceParam
	{
		float fCoefr;		//-1----+1 色彩分量调节
		float fCoefg;
		float fCoefb;
		float fLightness;	//-1----+1 亮度调节
		float fContrastk;//0--1坐标.. 控制对比度函数的斜率和y轴截距
		float fContrastb;

		float fCentx; //精灵所在位置 ---用于精灵周围的特殊处理
		float fCenty;
	};

	struct sNuclearEmbossParam
	{
		float fSizex;	//一般设置为图像宽、高的n倍(0.2<n<=1)左右波动
		float fSizey;
		int ndir;//8方向 0---7
		int nColorOpType; //顶点颜色与纹理的混合类型..
	};

	struct sNuclearRadialBlurParam
	{
		NuclearFPoint BlurCent;	//模糊中心  //左上角(0,0) 右下角(1,1)
		float blurDegree;	//模糊程度 //0.0---1.0
		int nBlurType;		//0--线状模糊、1--圆行模糊..
		int nColorOpType; //顶点颜色与纹理的混合类型..
	};

	struct sNuclearGrayParam
	{
		int nType;		//0，默认参数,普通灰度图效果；1，r保留，其它分量置0；2，g...；3，b...；
		int nColorOpType; //顶点颜色与纹理的混合类型..
	};

	struct sNuclearDistortionShaderParam //空间扭曲特效shader
	{
		float fdegree; //扭曲程度0.0f----1.0f //每帧update这个值形成动画,0就恢复原始样子了..
		NuclearFPoint fcent; //扭曲中心  //左上角(0,0) 右下角(1,1)
		float fMaxDis; //扭曲半径 //0.0f----1.0f
		int nType1; //控制方向 //挤压还是阔张等...
		int nType2; //控制形状
	};

	struct sNuclearFontOutlineParam //提取特殊字符图像边缘轮廓
	{
		NuclearColor dwOutlineColor; //float4 g_outlineColor
		int nTextureWidth; //float2 g_pixelSize
		int nTextureHeight;
	};

	struct sNuclearGaussianBlurBlendParam //高斯模糊之后做混合,类似ps里的模糊之后图层柔光叠加等效果
	{
		int nTextureWidth;
		int nTextureHeight;
		int nBlendType; //叠加模式 0--柔光 1---强光
	};

	enum NuclearDistortionMeshType //网格扭曲类型
	{
		XPDISTORTIONTYPE_HORIZONTAL_1 = 0, //固定水平方向一条边
		XPDISTORTIONTYPE_HORIZONTAL_2 = 1,
		XPDISTORTIONTYPE_VERTICAL_1 = 2,	//固定垂直方向一条边
		XPDISTORTIONTYPE_VERTICAL_2 = 3,
		XPDISTORTIONTYPE_DIAGONAL = 4,//固定左上或右下顶点
	};

	enum NuclearImgBorderPos
	{ //img文件的底座4个点标示...
		enum_left	=	0,
		enum_bottom	=	1,
		enum_right	=	2,
		enum_up		=	3,
	};

	enum NuclearComputerConfigType
	{ //计算机配置
		XPCOMPUTERCONFIG_L = 0, //低配
		XPCOMPUTERCONFIG_M = 1, //中配
		XPCOMPUTERCONFIG_H = 2, //高配
	};

	struct NuclearUTCT1Vertex
	{
		float x,y,z;
		DWORD c;
		float u,v;
		NuclearUTCT1Vertex(float _x=0.0f, float _y=0.0f, float _z=0.0f, DWORD _c=0xFFFFFFFF, float _u=0.0f, float _v=0.0f) : 
		x(_x), y(_y), z(_z), c(_c), u(_u), v(_v) {}
	};

	struct NuclearTCT1Vertex
	{
		float x,y,z;
		float rhw;
		DWORD c;
		float u,v;
		float u2,v2;
		NuclearTCT1Vertex(float _x=0.0f, float _y=0.0f, float _z=0.0f, float _rhw=1.0f, DWORD _c=0xFFFFFFFF, float _u=0.0f, float _v=0.0f,float _u2=0.0f, float _v2=0.0f) : 
		x(_x), y(_y), z(_z), rhw(_rhw), c(_c), u(_u), v(_v), u2(_u2), v2(_v2) {}
	};

	struct NuclearTCVertex
	{
		float x,y,z;
		float rhw;
		DWORD c;
		NuclearTCVertex(float _x=0.0f, float _y=0.0f, float _z=0.0f, float _rhw=1.0f, DWORD _c=0xFFFFFFFF) : x(_x), y(_y), z(_z), rhw(_rhw), c(_c) {}
	};
    
    enum NuclearPaniReleaseFlag
    {
        XPANI_RELEASE_WAIT_GC,
		XPANI_RELEASE_IMMEDIATE_GC,
    };

}


#endif