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
	const int XP_A_DAY_TIME = 7200000;//1000 * 60 * 60 * 2;//����Сʱ

//	const float XP_LKO_ZOOM = 2.0f; //��ҳ̶ȷŴ� 2��
//	const float XP_LKO_TIME = 5.0f; //�ָ�������ʱ�� 5��

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
		XPIFF_XPD = XPMAKEFOURCC('X', 'P', 'D', ' '),
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
		XPTEXBM_COPY = 2
	};

	// ����İ˸�����
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

	//��Ч���������ϵ�����
	enum EFFECT_BIND_TYPE
	{
		STATIC_POINT = 0,	//���ڹ̶��㣬���ܶ���Ӱ��
		WEAPON_POINT,		//����������
	};

	//����ͼ�϶�̬����Ч�Ĳ�Σ�ֻ��4��
	enum XPEFFECT_LAYER
	{
		XPEL_UNDER_SPRITE = 0,	//�ر�֮�ϣ�����֮��
		XPEL_SPRITE,            //������ڵ��㣬������ݵ����Զ��ж��ڵ�
		XPEL_ABOVE_SPRITE,      //����֮��
		XPEL_TOP,				//���ϲ�
	};

	// ��Ƶ��ʽ
	enum XPSOUND_TYPE
	{
		XPSOUNDTYPE_WAVE,
		XPSOUNDTYPE_OGG
	};

	// �������ʾģʽ�����Դ�����õ���������
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
		XPCOLOROP_SELECTARG1 = 1, //����
		XPCOLOROP_SELECTARG2 = 2, //������ɫ
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

	// ��ʱ�������������
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
		CPOINT offset[4];				// ������Ϣ	�ݶ�10*4���ֽ�
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

		// ��Ҫ�ڴ˺����н����������������׳��Զ����쳣
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
		float fCoefr;		//-1----+1 ɫ�ʷ�������
		float fCoefg;
		float fCoefb;
		float fLightness;	//-1----+1 ���ȵ���
		float fContrastk;//0--1����.. ���ƶԱȶȺ�����б�ʺ�y��ؾ�
		float fContrastb;
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

	struct XPDistortionShaderParam //�ռ�Ť����Чshader
	{
		float fdegree; //Ť���̶�0.0f----1.0f //ÿ֡update���ֵ�γɶ���,0�ͻָ�ԭʼ������..
		FPOINT fcent; //Ť������  //���Ͻ�(0,0) ���½�(1,1)
		float fMaxDis; //Ť���뾶 //0.0f----1.0f
		int nType1; //���Ʒ��� //��ѹ�������ŵ�...
		int nType2; //������״
	};

	struct XPOutlineParam //��ȡͼ���Ե����
	{
		XPCOLOR dwOutlineColor; //float4 g_outlineColor
		int nTextureWidth; //float2 g_pixelSize
		int nTextureHeight;
	};

	const int XPGRIDSIZE = 16; //Ť������Ĵ�С...
	struct XPDistortionParam //�����ӡ����ȵ�Ʈ��...
	{
		int nType; //����..
		float fCycNum; //���������ҵĺ�������
//		float fAmplitude;  //���..
		std::vector<FPOINT> fAmplitudes;
		int nRowIndex;
		int nColIndex;
		float fPos; //�������ֵ��ȷ������λ��.. y = a*sin(cyc*x + fPos)
		//ÿ��update-----fPos += fDetaTime*2*PI/fSpeed; --------fpos -= 2*PI;
		XPDistortionParam(int Type=0, float CycNum=0.2f, float Amplitude=1.0f, int RowIndex=0, int ColIndex=0, float Pos=0.0f)
		{
			nType = Type;
			fCycNum = CycNum; //����ȷ���������� (0.0f------2.0f)
		//	fAmplitude = Amplitude; //�����������ȷ����� (0.0f--------5.0f)
			fAmplitudes.clear();
			fAmplitudes.push_back(FPOINT(0.0F, Amplitude));
			fAmplitudes.push_back(FPOINT(1.0F, Amplitude));
			nRowIndex = RowIndex;
			nColIndex = ColIndex;
			fPos = Pos;
		}
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

}


#endif