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
		//��fmt�ĸ�ʽ����һ������
		virtual PictureHandle NewPicture(int width, int height, TPOOL_FORMAT fmt) = 0;
		//���ڴ��ȡͼƬ��������fmtָ���ĸ�ʽ
		virtual PictureHandle LoadPicture(const void* data, int size, TPOOL_FORMAT fmt) = 0;
		//���pRect���������data�ĸ�ʽ���handle��Ӧ�������ʽ����һ��
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect = 0) = 0; 
		//��fmt��ʽ��ȡpRect������������ͼƬ������fmt��ʽ�ģ�ת����Ż�ȡ
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, TPOOL_FORMAT fmt, const CRECT* pRect = 0) = 0; 
		//���ͼƬ���
		virtual bool GetPictureInfo(PictureHandle handle, int &width, int &height) const = 0;
		//�ͷ�ͼƬ
		virtual bool ReleasePicture(PictureHandle handle) = 0;
		//����ͼƬ
		virtual bool DrawPicture(PictureHandle handle, const FRECT &rect, const FRECT *srcRect, unsigned int color) = 0;
	};
}

#endif //#ifndef TRENDERER_H