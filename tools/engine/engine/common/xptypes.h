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
	// ��ɫ����
	//typedef DWORD XPCOLOR; // argb
	union XPCOLOR
	{
		DWORD data;
		struct {
			// todo ��С��
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
	const int XP_A_DAY_TIME = 7200000;//1000 * 60 * 60 * 2;//����Сʱ

	enum XPMOUSE_TEST_MODE
	{
		XPTM_PIC_RECT = 0,	//��ͼƬ�Ŀ���
		XPTM_POLYGON,		//����������Ķ����������
		XPTM_ALPHA,			//��Alpha��⣨��ҪLock��Դ������
	};

	enum XPEFFECT_SCALE_TYPE
	{
		XPEST_NOSCALE = 0,
		XPEST_ONLY_SCALE_POS,
		XPEST_ALLSCALE,
	};
//	const float XP_LKO_ZOOM = 2.0f; //��ҳ̶ȷŴ� 2��
//	const float XP_LKO_TIME = 5.0f; //�ָ�������ʱ�� 5��
	enum XPWINDOW_STATE
	{
		XPWS_ACTIVE = 0,	//����
		XPWS_INACTIVE,		//�ں��棨û�м��
		XPWS_MINIMIZE,		//��С��
	};

	// ��������ģʽ
	enum XPFILLMODE
	{
		XPFM_WIREFRAME = 0,
		XPFM_SOLIDFILL = 1
	};

	// ������ص�λ��
	enum XPPOOL_TYPE {
		XPPOOL_VIDEOMEM = 0,	// �Դ���
		XPPOOL_MANAGED = 1,		// �Զ�����
		XPPOOL_SYSTEMMEM = 2,   // �ڴ���
	};

	enum XPTEXTUREADDRESS //����Ѱַģʽ
	{
		XPTEXADDRESS_WRAP = 1,
		XPTEXADDRESS_MIRROR = 2,
		XPTEXADDRESS_CLAMP = 3, //Ĭ��ֵ...
		XPTEXADDRESS_BORDER = 4, //Ŀǰ����Ϸ�ͻ��˻����Ͻ�Բ��С��ͼ�õ�
		XPTEXADDRESS_MIRRORONCE = 5,
		XPTEXADDRESS_FORCE_DWORD = 0x7fffffff
	};


	#define XPMAKEFOURCC(ch0, ch1, ch2, ch3)                              \
                ((DWORD)(BYTE)(ch0) | ((DWORD)(BYTE)(ch1) << 8) |   \
                ((DWORD)(BYTE)(ch2) << 16) | ((DWORD)(BYTE)(ch3) << 24 ))

	// ������̴洢��ʽ	
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

	// �����ʽ
	enum XPTEXTURE_FORMAT
	{
		XPTEXFMT_DEFAULT  = 0, // ��renderer������
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

	enum XPPIXEL_FORMAT //���ظ�ʽ..
	{
		XPPIXELFMT_DEFAULT = 0,
		XPPIXELFMT_R5G6B5 = 1,
		XPPIXELFMT_A8R8G8B8 = 2,
		XPPIXELFMT_A4R4G4B4 = 3,
	};

	// ����������
	enum XPTEXTURE_BLENDMODE
	{
		XPTEXBM_DEFAULT = 0,	// Ĭ��
		XPTEXBM_MODULATE = 0,   // alpha���
		XPTEXBM_ADDITIVE = 1,	// ɫ�ʱ���
		XPTEXBM_COPY = 2,
		XPTEXBM_ADDITIVE_ALPHA = 3,//alpha Add
		XPTEXBM_MODULATE2X = 4,   // alpha���
		XPTEXBM_ADDITIVE2X = 5,	// ɫ�ʱ���
		XPTEXBM_MUTIPLY = 6, //��Ƭ����
		XPTEXBM_MUTIPLY_ALPHA = 7, //Alpha��Ƭ���ף���ȷ���÷�Ӧ����������Alpha������ÿ����ɫ������Alpha��������Ҫ��alphaֵ
	};

	// ����İ˸�����
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
	const int XP_EFFECT_CYCLE = 800;	//����
	const int XP_EFFECT_HALF_CYCLE = XP_EFFECT_CYCLE / 2;	//����
//	const float XP_EFFECT_ALPHA_DELTA = (XP_EFFECT_MAX_ALPHA - XP_EFFECT_MIN_ALPHA) / XP_EFFECT_HALF_CYCLE;
	const int XP_EFFECT_SCALE = 2;

	// �������ˮģʽ
	enum WATER_SURFACE_TYPE
	{
		WST_ALWAYS_PLANE = 0,	//��Զ��ˮƽ��
		WST_OBEY_DIR,			//˳�ž���ķ���
		WST_ATHWART_DIR,		//���ž���ķ���
	};

	//��Ч���������ϵ�����
	enum EFFECT_BIND_TYPE
	{
		STATIC_POINT = 0,	//���ڹ̶��㣬���ܶ���Ӱ��
		WEAPON_POINT,		//����������
	};

	enum XP_PREFETCH_RESULT
	{
		XPPR_OK = 0,			//������Դ�����غ���
		XPPR_BASE_ASYNC_OK,		//ֻ���غ��˵�һ֡
		XPPR_ASYNC_LOADING,		//���ڼ�����
	};

	//Title��handle���ɿͻ���UI�߼�����������
	typedef unsigned int EntitativeTitleHandle;
	const EntitativeTitleHandle INVALID_ENTITATIVE_TITLE_HANDLE = 0;

	//ս��״̬������handle
	typedef unsigned int WarBackgroundHandle;
	const WarBackgroundHandle INVALID_WAR_BACKGROUND_HANDLE = 0;

	//title�Ĺҽӵ�
	enum XPTITLE_ALIGN
	{
		XPTA_DEFAULT = 0,
		XPTA_CENTER_BOTTOM = 0,
		XPTA_CENTER_TOP = 1,
	};

	//����ͼ�϶�̬����Ч�Ĳ�Σ�ֻ��4��
	enum XPEFFECT_LAYER
	{
		XPEL_UNDER_SPRITE = 0,	//�ر�֮�ϣ�����֮��
		XPEL_SPRITE,            //������ڵ��㣬������ݵ����Զ��ж��ڵ�
		XPEL_ABOVE_SPRITE,      //����֮��
		XPEL_TOP,				//���ϲ�
	};

	enum XPEFFECT_BIND_TYPE
	{
		XPEBT_NULL = -1,
		XPEBT_ORIGIN = 0,
		XPEBT_EFFECT_POS,
	};

	enum XPEffectState
	{
		XPES_PLAYING,			//! ���ڲ���
		XPES_PAUSE,				//! ��������ͣ״̬��Ŀǰ���ܾͱ༭�������ˣ�
		XPES_STOP,				//! ����ֹͣ״̬
		XPES_EMPTY,				//! ����δ������Դ״̬
	};

	enum XPEffectType
	{
		XPET_ANI = 0,			//! ����ͼ����
		XPET_PARTICLE,			//! ����
		XPET_AUDIO,				//! ����
		XPET_GEFFECT,			//! ��Ч��
		XPET_LISTEFFECT,		//! ��Ч���У���������Ч��
	};

	enum XPEffectAssureResResult
	{
		XPEARR_RESREADY = 0,	//! ��Դ�Ѿ����ã�һ��ֻ��ͬ������ʱ����֣������������ͬ������Դ���ڴ����棬�첽Ҳ����֣�
		XPEARR_LOADING,			//! ��Դ���ڼ���
		XPEARR_LOAD_ERROR,		//! ��Դ���ش���
	};

	enum XPPLAY_MODE
	{
		XPPM_LOOP,				//! ����ѭ�� 
		XPPM_LOOPNUM,			//! ��һ���Ĵ�������ѭ��
		XPPM_TIME,				//! ����ָ��ʱ��
	};

	// ��Ƶ��ʽ
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

	//����ģʽ
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

	// �������ʾģʽ�����Դ�����õ���������
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

	//������������ϰ���mask�Ͷ�Ӧ����ɫ
	struct XPMazeColor
	{
		unsigned int mask;
		XPCOLOR color;
	};
	typedef std::vector<XPMazeColor> XPMazeColors;


	typedef unsigned short ActionTypeFlag;//��������flag

	typedef int PictureHandle;
	const PictureHandle INVALID_PICTURE_HANDLE = 0;
	
	struct XBase
	{
		CPOINT center;
		CPOINT offset[4];				// ������Ϣ	�ݶ�10*4���ֽ�
	};

	struct XPBase//������Ϣ
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
		XPCAMERA_HERO_ALWAYS_IN_CENTER = 0,	// ��򵥵�ģʽ���ӿ�ʼ��������Ϊ����
		XPCAMERA_UPDATE_TYPE2,	// �Ƚϸ��ӣ����Ƚ��ң�������(Ŀǰ������)
		XPCAMERA_FOLLOW_HERO_WHEN_OUT_OF_INSENCE,// ������insence����ʱ�����޸��ӿڣ�����׷�档
		XPCAMERA_FOLLOW_HEROS_DIR_WHEN_OUT_OF_INSENCE,// ������insence����ʱ�����޸��ӿڣ�����׷����ݵ�ǰ���ǵ��ƶ�����׷�档
		XPCAMERA_SMOOTH,		//�������ַ�����baiyu�ṩ
	};

	const int XPFRAMESTAT_TYPE_COUNT = XPFRAMESTAT_IAPP_MSG + 1;

	class IRunnable
	{
	public:
		virtual ~IRunnable() { }

		// ��Ҫ�ڴ˺����н����������������׳��Զ����쳣
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
		std::wstring picuri;	//��Դ·����Ϊ�մ�����һ��ͼ
		XPTEXTURE_BLENDMODE blendMode;
		unsigned char lastAlpha;
		unsigned char alpha;
	};

	struct XPFontEffectParam
	{ //Ŀǰֻ֮�зŴ����С, ���������ټ����ḻ
		FPOINT fMaxScale; //�Ŵ�
		FPOINT fMinScale; //��С
		float fCycle;	  //����: ��

		int nPlayTime;  //! �ܹ����ŵ�ʱ��
		int nLoopNum;	  //! ѭ������

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
		float fCoefr;		//-1----+1 ɫ�ʷ�������
		float fCoefg;
		float fCoefb;
		float fLightness;	//-1----+1 ���ȵ���
		float fContrastk;//0--1����.. ���ƶԱȶȺ�����б�ʺ�y��ؾ�
		float fContrastb;

		float fCentx; //��������λ�� ---���ھ�����Χ�����⴦��
		float fCenty;

	//	float fPicPosx;
	//	float fPicPosy;
	//	float fPicWidth;
	//	float fPicHeight;
	};

	struct XPEmbossParam
	{
		float fSizex;	//һ������Ϊͼ����ߵ�n��(0.2<n<=1)���Ҳ���
		float fSizey;
		int ndir;//8���� 0---7
		int nColorOpType; //������ɫ������Ļ������..
	};

	struct XPRadialBlurParam
	{
		FPOINT BlurCent;	//ģ������  //���Ͻ�(0,0) ���½�(1,1)
		float blurDegree;	//ģ���̶� //0.0---1.0
		int nBlurType;		//0--��״ģ����1--Բ��ģ��..
		int nColorOpType; //������ɫ������Ļ������..
	};

	struct XPGrayParam
	{
		int nType;		//0��Ĭ�ϲ���,��ͨ�Ҷ�ͼЧ����1��r����������������0��2��g...��3��b...��
		int nColorOpType; //������ɫ������Ļ������..
	};

	struct XPAlphaParam
	{
	};

	struct XPDistortionShaderParam //�ռ�Ť����Чshader
	{
		float fdegree; //Ť���̶�0.0f----1.0f //ÿ֡update���ֵ�γɶ���,0�ͻָ�ԭʼ������..
		FPOINT fcent; //Ť������  //���Ͻ�(0,0) ���½�(1,1)
		float fMaxDis; //Ť���뾶 //0.0f----1.0f
		int nType1; //���Ʒ��� //��ѹ�������ŵ�...
		int nType2; //������״
	};

	struct XPDyeShaderParam //Ⱦɫshader
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

	struct XPFontOutlineParam //��ȡ�����ַ�ͼ���Ե����
	{
		XPCOLOR dwOutlineColor; //float4 g_outlineColor
		int nTextureWidth; //float2 g_pixelSize
		int nTextureHeight;
	};

	struct XPGaussianBlurBlendParam //��˹ģ��֮�������,����ps���ģ��֮��ͼ�������ӵ�Ч��
	{
		int nTextureWidth;
		int nTextureHeight;
		int nBlendType; //����ģʽ 0--��� 1---ǿ��
	};

	enum XPDISTORTIONMESHTYPE //����Ť������
	{
		XPDISTORTIONTYPE_HORIZONTAL_1 = 0, //�̶�ˮƽ����һ����
		XPDISTORTIONTYPE_HORIZONTAL_2 = 1,
		XPDISTORTIONTYPE_VERTICAL_1 = 2,	//�̶���ֱ����һ����
		XPDISTORTIONTYPE_VERTICAL_2 = 3,
		XPDISTORTIONTYPE_DIAGONAL = 4,//�̶����ϻ����¶���
	};

	enum XPIMG_BORDERPOS
	{ //img�ļ��ĵ���4�����ʾ...
		enum_left	=	0,
		enum_bottom	=	1,
		enum_right	=	2,
		enum_up		=	3,
	};

	enum XPCOMPUTERCONFIGTYPE
	{ //���������
		XPCOMPUTERCONFIG_L = 0, //����
		XPCOMPUTERCONFIG_M = 1, //����
		XPCOMPUTERCONFIG_H = 2, //����
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