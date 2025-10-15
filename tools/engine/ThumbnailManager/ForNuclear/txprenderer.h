#ifndef TXPRENDERER_H
#define TXPRENDERER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\thumbnailmanager\trenderer.h"
#include "..\..\engine\renderer\renderer.h"

namespace TMan
{
	class TXPRenderer :
		public TRenderer
	{
	private:
		Nuclear::Renderer* m_pRenderer;

	public:
		TXPRenderer(Nuclear::Renderer* pRenderer) : m_pRenderer(pRenderer) { }
		virtual ~TXPRenderer(void) {}

	public:
		//以fmt的格式创建一块纹理
		virtual PictureHandle NewPicture(int width, int height, TPOOL_FORMAT fmt);
		//从内存读取图片，读取后必须是fmt指定的格式
		virtual PictureHandle LoadPicture(const void* data, int size, TPOOL_FORMAT fmt);
		//填充pRect区域的纹理，data的格式会和handle对应的纹理格式保持一致
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect = NULL); 
		//以fmt格式获取pRect区域的纹理，如果图片本身不是fmt格式的，转换后才获取
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, TPOOL_FORMAT fmt, const CRECT* pRect = NULL);
		//获得图片宽高
		virtual bool GetPictureInfo(PictureHandle handle, int &width, int &height) const;
		//释放图片
		virtual bool ReleasePicture(PictureHandle handle);
		//绘制图片
		virtual bool DrawPicture(PictureHandle handle, const FRECT &rect, const FRECT *srcRect, unsigned int color);
	};

}

#endif //#ifndef TXPRENDERER_H