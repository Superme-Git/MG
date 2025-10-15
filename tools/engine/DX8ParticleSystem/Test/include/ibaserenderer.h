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
		XPTOP_SELECTARG1 = 2, //纹理
		XPTOP_SELECTARG2 = 3, //顶点颜色
		XPTOP_ADD = 7,
		XPTOP_SUBTRACT = 10,
		XPTOP_ADDSMOOTH = 11,
		XPTOP_ADDSIGNED = 8,
		XPTOP_MODULATE2X = 5,
	};

	enum XPRENDER_EFFECT
	{
		XPRE_NULL = -1,			  // 默认，无效果
		XPRE_COLORBANLANCE = 0,   // 色彩平衡..
		XPRE_RADIALBLUR,		  // 径向模糊
		XPRE_EMBOSSPIC,			  // 浮雕效果..
		XPRE_GRAY,				  //灰度图	
		XPRE_DISTORTION,		  //空间扭曲
		XPRE_OUTLINE,			  //图像轮廓提取
		XPRE_COUNT
	};

	// 这个基类是为了粒子系统抽出来直接作为适配器用的，所以接口反映的应该是粒子系统的最小需求
	class IBaseRenderer
	{	

	public:
		IBaseRenderer() {};
		virtual ~IBaseRenderer() { }

	public:
		// 设置和读取alpha混合开关
		virtual void SetAlphaBlendEnable(bool f) = 0;
		virtual bool GetAlphaBlendEnable() const = 0;
		// 设置和读取alpha混合模式下src混合类型
		virtual void SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend) = 0;
		virtual XPALPHABLEND_TYPE GetAlphaBlendSrcType() const = 0;
		// 设置和读取alpha混合模式下dst混合类型
		virtual void SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend) = 0;
		virtual XPALPHABLEND_TYPE GetAlphaBlendDstType() const = 0;

		// 设置和读取纹理和diffusecolor的混合类型
		virtual void SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop) = 0;
		virtual XPTEXCOLORBLEND_TYPE GetTextureColorBlendType() const = 0;

		// 设置和读取baserenderer的特效类型
		virtual void SetRenderEffect(XPRENDER_EFFECT re) = 0;
		virtual XPRENDER_EFFECT GetRenderEffect() const = 0;

	public:
		// 从内存中加载纹理并返回句柄，pooltype 只是建议值，真正的pooltype请检查 pPicInfo
		// 注意:目前例子系统使用的纹理均为XPPOOL_MANAGED
		virtual PictureHandle LoadPictureFromMem(const void *data, int size,PictureInfo *pPicInfo=NULL,XPTEXTURE_FORMAT texfmt=XPTEXFMT_DEFAULT,XPPOOL_TYPE pooltype=XPPOOL_MANAGED)=0;
		// 通过句柄释放纹理
		virtual bool FreePicture(PictureHandle handle) = 0;

		// 新建空纹理并返回句柄
		// 目前可能是 8888 或 4444, 使用前请查询纹理格式
		virtual PictureHandle NewPicture(int width, int height, PictureInfo *pPicInfo =NULL, XPTEXTURE_FORMAT texfmt=XPTEXFMT_DEFAULT) = 0;
		// 得到纹理数据
		// 目前可能是 8888 或 4444 或 DXT1-5, 使用前请查询纹理格式, 4444使用16位buffer,其他使用32位buffer, DXT1-5格式的话pRect的大小最好对齐到4
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, const CRECT* pRect = NULL) = 0; 
		// 设置纹理数据
		// 目前可能是 8888 或 4444 或 DXT1-5, 使用前请查询纹理格式, 4444使用16位buffer,其他使用32位buffer, DXT1-5格式的话pRect的大小最好对齐到4
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect = NULL) = 0; 
		// 获取一个纹理信息
		virtual bool GetPictureInfo(PictureHandle handle, PictureInfo& picinfo) const = 0;

	public:
		// 用TriangleFan来绘制这些顶点
		virtual bool DrawTriangleFan(PictureHandle handle, const TCT1VERTEX *vertices, int nVertexNum/*顶点个数*/) = 0;
		// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const TCT1VERTEX *vertices, int nVertexNum/*顶点个数*/) = 0;
		// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const UTCT1VERTEX *vertices, int nVertexNum/*顶点个数*/) = 0;

		// 占坑
	private:
		IBaseRenderer(const IBaseRenderer&);
		IBaseRenderer& operator=(const IBaseRenderer&);
	};



};

#endif