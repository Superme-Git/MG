#ifndef TRENDERER_H
#define TRENDERER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "ttypes.h"

namespace TMan
{
	class TRenderer
	{
	public:
		//以fmt的格式创建一块纹理
		virtual PictureHandle NewPicture(int width, int height, TPOOL_FORMAT fmt) = 0;
		//从内存读取图片，必须是fmt指定的格式
		virtual PictureHandle LoadPicture(const void* data, int size, TPOOL_FORMAT fmt) = 0;
		//填充pRect区域的纹理，data的格式会和handle对应的纹理格式保持一致
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect = 0) = 0; 
		//以fmt格式获取pRect区域的纹理，如果图片本身不是fmt格式的，转换后才获取
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, TPOOL_FORMAT fmt, const CRECT* pRect = 0) = 0; 
		//获得图片宽高
		virtual bool GetPictureInfo(PictureHandle handle, int &width, int &height) const = 0;
		//释放图片
		virtual bool ReleasePicture(PictureHandle handle) = 0;
		//绘制图片
		virtual bool DrawPicture(PictureHandle handle, const FRECT &rect, const FRECT *srcRect, unsigned int color) = 0;
	};
}

#endif //#ifndef TRENDERER_H