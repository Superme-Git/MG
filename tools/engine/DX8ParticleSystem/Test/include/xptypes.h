#ifndef __Nuclear_XPTYPES_H
#define __Nuclear_XPTYPES_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <vector>
#include <windows.h>
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
	const int XP_DAY_START_TIME =    900000;
	const int XP_NIGHT_START_TIME = 6300000;
	const int XP_A_DAY_TIME = 7200000;//1000 * 60 * 60 * 2;//两个小时

//	const float XP_LKO_ZOOM = 2.0f; //最激烈程度放大 2倍
//	const float XP_LKO_TIME = 5.0f; //恢复正常需时间 5秒

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
		XPIFF_XPD = XPMAKEFOURCC('X', 'P', 'D', ' '),
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
		XPTEXBM_COPY = 2
	};

	// 精灵的八个方向
	enum XPDIRECTION
	{
		XPDIR_TOP,
		XPDIR_TOPRIGHT,
		XPDIR_RIGHT,
		XPDIR_BOTTOMRIGHT,
		XPDIR_BOTTOM,
		XPDIR_BOTTOMLEFT,
		XPDIR_LEFT,
		XPDIR_TOPLEFT,
		XPDIR_COUNT
	};

	//特效绑在人身上的类型
	enum EFFECT_BIND_TYPE
	{
		STATIC_POINT = 0,	//绑在固定点，不受动作影响
		WEAPON_POINT,		//绑在武器上
	};

	//往地图上动态扔特效的层次，只有4层
	enum XPEFFECT_LAYER
	{
		XPEL_UNDER_SPRITE = 0,	//地表之上，精灵之下
		XPEL_SPRITE,            //精灵和遮挡层，引擎根据底座自动判断遮挡
		XPEL_ABOVE_SPRITE,      //精灵之上
		XPEL_TOP,				//最上层
	};

	// 音频格式
	enum XPSOUND_TYPE
	{
		XPSOUNDTYPE_WAVE,
		XPSOUNDTYPE_OGG
	};

	// 引擎的显示模式，可以从引擎得到或者设置
	struct XDisplayMode
	{
		int width;
		int height;
		int bitperpix; // 16 or 32 
		XDisplayMode() : width(800), height(600), bitperpix(32) { }
		XDisplayMode(int w, int h, int bpp = 32) :width(w), height(h), bitperpix(bpp) { }
		bool operator==(const XDisplayMode &df) const { 
			return width == df.width && height == df.height && bitperpix == df.bitperpix;
		}
	};

	enum XPCOLOROPTYPE
	{
		XPCOLOROP_DEFAULT = 0,
		XPCOLOROP_MODULATE = 0,
		XPCOLOROP_SELECTARG1 = 1, //纹理
		XPCOLOROP_SELECTARG2 = 2, //顶点颜色
		XPCOLOROP_ADD = 3,
		XPCOLOROP_SUBTRACT = 4,
		XPCOLOROP_ADDSMOOTH = 5,
		XPCOLOROP_ADDSIGNED = 6,
		XPCOLOROP_MODULATE2X = 7,
	};

	enum {
		XPCOLOR_OP_TYPE_COUNT = XPCOLOROP_MODULATE2X + 1
	};

	enum XPALPHABLENDTYPE
	{
		XPAB_DEFAULT = 0,
		XPAB_ALPHA = 0,
		XPAB_LIGHTING,
		XPAB_ADD,
		XPAB_NULL,
	};

	// 临时的字体参数类型
	struct Font
	{	 
		int size;
		int type;
		int style;
		XPCOLOR color; // argb
	};

	typedef int PictureHandle;
	const PictureHandle INVALID_PICTURE_HANDLE = 0;
	
	struct XBase
	{
		CPOINT center;
		CPOINT offset[4];				// 底座信息	暂定10*4个字节
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

	struct XPColorBalanceParam
	{
		float fCoefr;		//-1----+1 色彩分量调节
		float fCoefg;
		float fCoefb;
		float fLightness;	//-1----+1 亮度调节
		float fContrastk;//0--1坐标.. 控制对比度函数的斜率和y轴截距
		float fContrastb;
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

	struct XPDistortionShaderParam //空间扭曲特效shader
	{
		float fdegree; //扭曲程度0.0f----1.0f //每帧update这个值形成动画,0就恢复原始样子了..
		FPOINT fcent; //扭曲中心  //左上角(0,0) 右下角(1,1)
		float fMaxDis; //扭曲半径 //0.0f----1.0f
		int nType1; //控制方向 //挤压还是阔张等...
		int nType2; //控制形状
	};

	struct XPOutlineParam //提取图像边缘轮廓
	{
		XPCOLOR dwOutlineColor; //float4 g_outlineColor
		int nTextureWidth; //float2 g_pixelSize
		int nTextureHeight;
	};

	const int XPGRIDSIZE = 16; //扭曲网格的大小...
	struct XPDistortionParam //做旗子、布等的飘动...
	{
		int nType; //类型..
		float fCycNum; //控制正余弦的函数周期
//		float fAmplitude;  //振幅..
		std::vector<FPOINT> fAmplitudes;
		int nRowIndex;
		int nColIndex;
		float fPos; //根据这个值来确定函数位置.. y = a*sin(cyc*x + fPos)
		//每次update-----fPos += fDetaTime*2*PI/fSpeed; --------fpos -= 2*PI;
		XPDistortionParam(int Type=0, float CycNum=0.2f, float Amplitude=1.0f, int RowIndex=0, int ColIndex=0, float Pos=0.0f)
		{
			nType = Type;
			fCycNum = CycNum; //用来确定函数周期 (0.0f------2.0f)
		//	fAmplitude = Amplitude; //与网格宽度相乘确定振幅 (0.0f--------5.0f)
			fAmplitudes.clear();
			fAmplitudes.push_back(FPOINT(0.0F, Amplitude));
			fAmplitudes.push_back(FPOINT(1.0F, Amplitude));
			nRowIndex = RowIndex;
			nColIndex = ColIndex;
			fPos = Pos;
		}
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

}


#endif