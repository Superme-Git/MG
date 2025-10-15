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
		XPTOP_SELECTARG1 = 1, //纹理
		XPTOP_SELECTARG2 = 2, //顶点颜色
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

	//目前系统中只用到了XPRE_COLORBALANCE 和 XPRE_COLORBALANCE_2
	//以后用到一个加一个，减少render初始化时的负担
	enum XPRENDER_EFFECT
	{
		XPRE_NULL = -1,			  // 默认，无效果
		XPRE_COLORBALANCE = 0,    // 色彩平衡..
		XPRE_COLORBALANCE_2,	  // 色彩平衡_2..
		XPRE_ALPHA,				  // alpha信息, 目前用来显示精灵编辑器各打包素材的alpha信息分布
		XPRE_2TEXTUREBLEND,		  // 纹理混合

	//	XPRE_RADIALBLUR,		  // 径向模糊
	//	XPRE_EMBOSSPIC,			  // 浮雕效果..
	//	XPRE_GRAY,				  //灰度图	
		XPRE_DISTORTION,		  //空间扭曲
	//	XPRE_FONTOUTLINE,	//特殊图像(fonttexture里的纹理数据--只有alpha有用的32位图)轮廓提取
							//详见: CBitmapCache::CreateCharBitmap( TCHAR ch)
	//	XPRE_GaussianBlurBlend,		  //先做高斯模糊再做叠加.叠加类似ps里的图层叠加效果

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
//		virtual void SetAlphaBlendEnable(bool f) = 0;
//		virtual bool GetAlphaBlendEnable() const = 0;
//		// 设置和读取alpha混合模式下src混合类型
		virtual void SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend) = 0;
		virtual XPALPHABLEND_TYPE GetAlphaBlendSrcType() const = 0;
		// 设置和读取alpha混合模式下dst混合类型
		virtual void SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend) = 0;
		virtual XPALPHABLEND_TYPE GetAlphaBlendDstType() const = 0;

		// 设置和读取纹理和diffusecolor的混合类型
		virtual void SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop) = 0;
		virtual XPTEXCOLORBLEND_TYPE GetTextureColorBlendType() const = 0;

//		// 是否分开计算RenderTarget的Alpha，这个取决于显卡是否支持，目前比较流行的显卡可能就Intel GMA 500比较麻烦
//		// 目前的公式只能是MAX(src, dest)
//		virtual void SetSeparateAlphaBlend(bool f) = 0;
//		virtual bool GetSeparateAlphaBlend() const = 0;
//
//		// 设置和读取baserenderer的特效类型
//		virtual void SetRenderEffect(XPRENDER_EFFECT re) = 0;
//		virtual XPRENDER_EFFECT GetRenderEffect() const = 0;
//
		// 显示模式
		virtual NuclearDisplayMode GetDisplayMode() const = 0;
//		virtual std::vector<NuclearDisplayMode> EnumDisplayMode() const = 0;
//		// 这个只能由引擎自己来调用，客户端应该调用IEnv的接口
//		virtual bool SetDisplayMode(const NuclearDisplayMode &dmode) = 0;
		


	public:
		// 从内存中加载纹理并返回句柄，pooltype 只是建议值，真正的pooltype请检查 pPicInfo
		// 注意:目前例子系统使用的纹理均为XPPOOL_MANAGED
		virtual PictureHandle LoadPictureFromMem(const void *data, int size, NuclearPictureInfo *pPicInfo = NULL, NuclearTextureFormat texfmt = XPTEXFMT_DEFAULT, NuclearPoolType pooltype = XPPOOL_MANAGED, bool bCache = true, PictureHandle handle = 0, int iScale = 100) = 0;
		// 通过句柄释放纹理
		virtual bool FreePicture(PictureHandle handle) = 0;

		virtual bool LoadCCImageFromMem(cocos2d::CCImage* pImage, NuclearTextureFormat texfmt, const void *data, int size) = 0;
		virtual PictureHandle LoadPictureFromCCImage(cocos2d::CCImage* image, NuclearTextureFormat texfmt, NuclearPictureInfo *pPicInfo = NULL, NuclearPoolType pooltype = XPPOOL_MANAGED, bool bCache = true, PictureHandle handle = 0) = 0;

		// 新建空纹理并返回句柄
		// 目前可能是 8888 或 4444, 使用前请查询纹理格式
		virtual PictureHandle NewPicture(int width, int height, NuclearPictureInfo *pPicInfo =NULL, NuclearTextureFormat texfmt=XPTEXFMT_DEFAULT) = 0;
		// 得到纹理数据
		// 目前可能是 8888 或 4444 或 DXT1-5, 使用前请查询纹理格式, 4444使用16位buffer,其他使用32位buffer, DXT1-5格式的话pRect的大小最好对齐到4
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, const NuclearRect* pRect = NULL) = 0; 
		// 设置纹理数据
		// 目前可能是 8888 或 4444 或 DXT1-5, 使用前请查询纹理格式, 4444使用16位buffer,其他使用32位buffer, DXT1-5格式的话pRect的大小最好对齐到4
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const NuclearRect* pRect = NULL) = 0; 
		// 获取一个纹理信息
		virtual bool GetPictureInfo(PictureHandle handle, NuclearPictureInfo& picinfo) const = 0;
		//pitch 该纹理每行的字节数...
		virtual bool GetPictureInfo(PictureHandle handle, int& pitch, int& width, int& height) const = 0;
		// 通过纹理名获取一个纹理句柄
		virtual bool GetPictureHandle(PictureHandle& handle, const std::wstring& fileuri) const = 0;
		// 检测纹理是否有效
		virtual bool IsTextureExist(PictureHandle handle) = 0;

        virtual bool IsTextureEtc(PictureHandle handle) {return false;}

	public:
		/*
		1. NuclearTCT1Vertex的顶点结构,使用者注意应该给每个顶点坐标 减0.5f
		2. todo-- 把控制渲染状态的参数结构体传进来..方便接口给其他人使用
		*/
		// 用TriangleFan来绘制这些顶点
		virtual bool DrawTriangleFan(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, int nVertexNum/*顶点个数*/) = 0;
		// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, int nVertexNum/*顶点个数*/) = 0;
		// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
		virtual bool DrawParticles(PictureHandle handle, const NuclearUTCT1Vertex* pVertexs, int nVertexNum/*顶点个数*/) = 0;

		// 每个粒子用4个顶点组成的矩形来绘制，粒子数= nVertexNum/4
		// 这里只收集粒子，不绘制..
		virtual bool CollectParticles(PictureHandle handle, XPTEXCOLORBLEND_TYPE colorBlend, PSL_RENDSTATE alphaBlend,
									const NuclearTCT1Vertex* pVertexs, int nVertexNum/*顶点个数*/) = 0;

		virtual bool BatchDrawParticles() = 0; //

	private:
		IBaseRenderer(const IBaseRenderer&);
		IBaseRenderer& operator=(const IBaseRenderer&);
	};



};

#endif