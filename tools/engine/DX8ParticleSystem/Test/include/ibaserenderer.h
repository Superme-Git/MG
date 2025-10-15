#ifndef __Nuclear_IBASERENDERER_H
#define __Nuclear_IBASERENDERER_H

#if _MSC_VER > 1000
#pragma once
#endif

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
		XPTOP_DEFAULT = 4,
		XPTOP_MODULATE = 4,
		XPTOP_SELECTARG1 = 2, //����
		XPTOP_SELECTARG2 = 3, //������ɫ
		XPTOP_ADD = 7,
		XPTOP_SUBTRACT = 10,
		XPTOP_ADDSMOOTH = 11,
		XPTOP_ADDSIGNED = 8,
		XPTOP_MODULATE2X = 5,
	};

	enum XPRENDER_EFFECT
	{
		XPRE_NULL = -1,			  // Ĭ�ϣ���Ч��
		XPRE_COLORBANLANCE = 0,   // ɫ��ƽ��..
		XPRE_RADIALBLUR,		  // ����ģ��
		XPRE_EMBOSSPIC,			  // ����Ч��..
		XPRE_GRAY,				  //�Ҷ�ͼ	
		XPRE_DISTORTION,		  //�ռ�Ť��
		XPRE_OUTLINE,			  //ͼ��������ȡ
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
		virtual void SetAlphaBlendEnable(bool f) = 0;
		virtual bool GetAlphaBlendEnable() const = 0;
		// ���úͶ�ȡalpha���ģʽ��src�������
		virtual void SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend) = 0;
		virtual XPALPHABLEND_TYPE GetAlphaBlendSrcType() const = 0;
		// ���úͶ�ȡalpha���ģʽ��dst�������
		virtual void SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend) = 0;
		virtual XPALPHABLEND_TYPE GetAlphaBlendDstType() const = 0;

		// ���úͶ�ȡ�����diffusecolor�Ļ������
		virtual void SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop) = 0;
		virtual XPTEXCOLORBLEND_TYPE GetTextureColorBlendType() const = 0;

		// ���úͶ�ȡbaserenderer����Ч����
		virtual void SetRenderEffect(XPRENDER_EFFECT re) = 0;
		virtual XPRENDER_EFFECT GetRenderEffect() const = 0;

	public:
		// ���ڴ��м����������ؾ����pooltype ֻ�ǽ���ֵ��������pooltype���� pPicInfo
		// ע��:Ŀǰ����ϵͳʹ�õ������ΪXPPOOL_MANAGED
		virtual PictureHandle LoadPictureFromMem(const void *data, int size,PictureInfo *pPicInfo=NULL,XPTEXTURE_FORMAT texfmt=XPTEXFMT_DEFAULT,XPPOOL_TYPE pooltype=XPPOOL_MANAGED)=0;
		// ͨ������ͷ�����
		virtual bool FreePicture(PictureHandle handle) = 0;

		// �½����������ؾ��
		// Ŀǰ������ 8888 �� 4444, ʹ��ǰ���ѯ�����ʽ
		virtual PictureHandle NewPicture(int width, int height, PictureInfo *pPicInfo =NULL, XPTEXTURE_FORMAT texfmt=XPTEXFMT_DEFAULT) = 0;
		// �õ���������
		// Ŀǰ������ 8888 �� 4444 �� DXT1-5, ʹ��ǰ���ѯ�����ʽ, 4444ʹ��16λbuffer,����ʹ��32λbuffer, DXT1-5��ʽ�Ļ�pRect�Ĵ�С��ö��뵽4
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, const CRECT* pRect = NULL) = 0; 
		// ������������
		// Ŀǰ������ 8888 �� 4444 �� DXT1-5, ʹ��ǰ���ѯ�����ʽ, 4444ʹ��16λbuffer,����ʹ��32λbuffer, DXT1-5��ʽ�Ļ�pRect�Ĵ�С��ö��뵽4
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect = NULL) = 0; 
		// ��ȡһ��������Ϣ
		virtual bool GetPictureInfo(PictureHandle handle, PictureInfo& picinfo) const = 0;

	public:
		// ��TriangleFan��������Щ����
		virtual bool DrawTriangleFan(PictureHandle handle, const TCT1VERTEX *vertices, int nVertexNum/*�������*/) = 0;
		// ÿ��������4��������ɵľ��������ƣ�������= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const TCT1VERTEX *vertices, int nVertexNum/*�������*/) = 0;
		// ÿ��������4��������ɵľ��������ƣ�������= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const UTCT1VERTEX *vertices, int nVertexNum/*�������*/) = 0;

		// ռ��
	private:
		IBaseRenderer(const IBaseRenderer&);
		IBaseRenderer& operator=(const IBaseRenderer&);
	};



};

#endif