#ifndef __Nuclear_IBASERENDERER_H
#define __Nuclear_IBASERENDERER_H

#if _MSC_VER > 1000
#pragma once
#endif
#include "../common/nuxptypes.h"

namespace Nuclear
{
	enum XPALPHABLEND_TYPE
	{
		XPBLEND_ZERO = 1,
		XPBLEND_ONE = 2,
		XPBLEND_SRCCOLOR = 3,
		XPBLEND_INVSRCCOLOR = 4,
		XPBLEND_SRCALPHA = 5,
		XPBLEND_INVSRCALPHA = 6,
		XPBLEND_DESTALPHA = 7,
		XPBLEND_INVDESTALPHA = 8,
		XPBLEND_DESTCOLOR = 9,
		XPBLEND_INVDESTCOLOR = 10,
		XPBLEND_SRCALPHASAT = 11,
		XPBLEND_BOTHSRCALPHA = 12,
		XPBLEND_BOTHINVSRCALPHA = 13,
		XPBLEND_BLENDFACTOR = 14,
		XPBLEND_INVBLENDFACTOR = 15,
	};

	enum XPTEXCOLORBLEND_TYPE
	{
		XPTOP_DEFAULT = 0,
		XPTOP_MODULATE = 0,
		XPTOP_SELECTARG1 = 1, //����
		XPTOP_SELECTARG2 = 2, //������ɫ
		XPTOP_ADD = 3,
		XPTOP_SUBTRACT = 4,
		XPTOP_ADDSMOOTH = 5,
		XPTOP_ADDSIGNED = 6,
		XPTOP_MODULATE2X = 7,
	};

	enum PSL_RENDSTATE
	{
		PSLRENDSTATE_ALPHA = 6,		 //D3DBLEND_INVSRCALPHA
		PSLRENDSTATE_LIGHTING = 2,   //ALPHA---ONE
		PSLRENDSTATE_ADD  = 3,		 //ONE---ONE 
	};

	enum {
		XPTOP_TYPE_COUNT = XPTOP_MODULATE2X + 1
	};

	//Ŀǰϵͳ��ֻ�õ���XPRE_COLORBALANCE �� XPRE_COLORBALANCE_2
	//�Ժ��õ�һ����һ��������render��ʼ��ʱ�ĸ���
	enum XPRENDER_EFFECT
	{
		XPRE_NULL = -1,			  // Ĭ�ϣ���Ч��
		XPRE_COLORBALANCE = 0,    // ɫ��ƽ��..
		XPRE_COLORBALANCE_2,	  // ɫ��ƽ��_2..
		XPRE_ALPHA,				  // alpha��Ϣ, Ŀǰ������ʾ����༭��������زĵ�alpha��Ϣ�ֲ�
		XPRE_2TEXTUREBLEND,		  // ������

	//	XPRE_RADIALBLUR,		  // ����ģ��
	//	XPRE_EMBOSSPIC,			  // ����Ч��..
	//	XPRE_GRAY,				  //�Ҷ�ͼ	
		XPRE_DISTORTION,		  //�ռ�Ť��
	//	XPRE_FONTOUTLINE,	//����ͼ��(fonttexture�����������--ֻ��alpha���õ�32λͼ)������ȡ
							//���: CBitmapCache::CreateCharBitmap( TCHAR ch)
	//	XPRE_GaussianBlurBlend,		  //������˹ģ����������.��������ps���ͼ�����Ч��

		XPRE_COUNT
	};

	// ���������Ϊ������ϵͳ�����ֱ����Ϊ�������õģ����Խӿڷ�ӳ��Ӧ��������ϵͳ����С����
	class IBaseRenderer
	{	
	public:
		IBaseRenderer() {};
		virtual ~IBaseRenderer() { }

	public:
		// ���úͶ�ȡalpha��Ͽ���
//		virtual void SetAlphaBlendEnable(bool f) = 0;
//		virtual bool GetAlphaBlendEnable() const = 0;
//		// ���úͶ�ȡalpha���ģʽ��src�������
		virtual void SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend) = 0;
		virtual XPALPHABLEND_TYPE GetAlphaBlendSrcType() const = 0;
		// ���úͶ�ȡalpha���ģʽ��dst�������
		virtual void SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend) = 0;
		virtual XPALPHABLEND_TYPE GetAlphaBlendDstType() const = 0;

		// ���úͶ�ȡ�����diffusecolor�Ļ������
		virtual void SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop) = 0;
		virtual XPTEXCOLORBLEND_TYPE GetTextureColorBlendType() const = 0;

//		// �Ƿ�ֿ�����RenderTarget��Alpha�����ȡ�����Կ��Ƿ�֧�֣�Ŀǰ�Ƚ����е��Կ����ܾ�Intel GMA 500�Ƚ��鷳
//		// Ŀǰ�Ĺ�ʽֻ����MAX(src, dest)
//		virtual void SetSeparateAlphaBlend(bool f) = 0;
//		virtual bool GetSeparateAlphaBlend() const = 0;
//
//		// ���úͶ�ȡbaserenderer����Ч����
//		virtual void SetRenderEffect(XPRENDER_EFFECT re) = 0;
//		virtual XPRENDER_EFFECT GetRenderEffect() const = 0;
//
		// ��ʾģʽ
		virtual NuclearDisplayMode GetDisplayMode() const = 0;
//		virtual std::vector<NuclearDisplayMode> EnumDisplayMode() const = 0;
//		// ���ֻ���������Լ������ã��ͻ���Ӧ�õ���IEnv�Ľӿ�
//		virtual bool SetDisplayMode(const NuclearDisplayMode &dmode) = 0;
		


	public:
		// ���ڴ��м����������ؾ����pooltype ֻ�ǽ���ֵ��������pooltype���� pPicInfo
		// ע��:Ŀǰ����ϵͳʹ�õ������ΪXPPOOL_MANAGED
		virtual PictureHandle LoadPictureFromMem(const void *data, int size, NuclearPictureInfo *pPicInfo = NULL, NuclearTextureFormat texfmt = XPTEXFMT_DEFAULT, NuclearPoolType pooltype = XPPOOL_MANAGED, bool bCache = true, PictureHandle handle = 0, int iScale = 100) = 0;
		// ͨ������ͷ�����
		virtual bool FreePicture(PictureHandle handle) = 0;

		virtual bool LoadCCImageFromMem(cocos2d::CCImage* pImage, NuclearTextureFormat texfmt, const void *data, int size) = 0;
		virtual PictureHandle LoadPictureFromCCImage(cocos2d::CCImage* image, NuclearTextureFormat texfmt, NuclearPictureInfo *pPicInfo = NULL, NuclearPoolType pooltype = XPPOOL_MANAGED, bool bCache = true, PictureHandle handle = 0) = 0;

		// �½����������ؾ��
		// Ŀǰ������ 8888 �� 4444, ʹ��ǰ���ѯ�����ʽ
		virtual PictureHandle NewPicture(int width, int height, NuclearPictureInfo *pPicInfo =NULL, NuclearTextureFormat texfmt=XPTEXFMT_DEFAULT) = 0;
		// �õ���������
		// Ŀǰ������ 8888 �� 4444 �� DXT1-5, ʹ��ǰ���ѯ�����ʽ, 4444ʹ��16λbuffer,����ʹ��32λbuffer, DXT1-5��ʽ�Ļ�pRect�Ĵ�С��ö��뵽4
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, const NuclearRect* pRect = NULL) = 0; 
		// ������������
		// Ŀǰ������ 8888 �� 4444 �� DXT1-5, ʹ��ǰ���ѯ�����ʽ, 4444ʹ��16λbuffer,����ʹ��32λbuffer, DXT1-5��ʽ�Ļ�pRect�Ĵ�С��ö��뵽4
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const NuclearRect* pRect = NULL) = 0; 
		// ��ȡһ��������Ϣ
		virtual bool GetPictureInfo(PictureHandle handle, NuclearPictureInfo& picinfo) const = 0;
		//pitch ������ÿ�е��ֽ���...
		virtual bool GetPictureInfo(PictureHandle handle, int& pitch, int& width, int& height) const = 0;
		// ͨ����������ȡһ��������
		virtual bool GetPictureHandle(PictureHandle& handle, const std::wstring& fileuri) const = 0;
		// ��������Ƿ���Ч
		virtual bool IsTextureExist(PictureHandle handle) = 0;

        virtual bool IsTextureEtc(PictureHandle handle) {return false;}

	public:
		/*
		1. NuclearTCT1Vertex�Ķ���ṹ,ʹ����ע��Ӧ�ø�ÿ���������� ��0.5f
		2. todo-- �ѿ�����Ⱦ״̬�Ĳ����ṹ�崫����..����ӿڸ�������ʹ��
		*/
		// ��TriangleFan��������Щ����
		virtual bool DrawTriangleFan(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, int nVertexNum/*�������*/) = 0;
		// ÿ��������4��������ɵľ��������ƣ�������= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, int nVertexNum/*�������*/) = 0;
		// ÿ��������4��������ɵľ��������ƣ�������= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const NuclearUTCT1Vertex* pVertexs, int nVertexNum/*�������*/) = 0;

		// ÿ��������4��������ɵľ��������ƣ�������= nVertexNum/4
		// ����ֻ�ռ����ӣ�������..
		virtual bool CollectParticles(PictureHandle handle, XPTEXCOLORBLEND_TYPE colorBlend, PSL_RENDSTATE alphaBlend,
									const NuclearTCT1Vertex* pVertexs, int nVertexNum/*�������*/) = 0;

		virtual bool BatchDrawParticles() = 0; //

	private:
		IBaseRenderer(const IBaseRenderer&);
		IBaseRenderer& operator=(const IBaseRenderer&);
	};



};

#endif