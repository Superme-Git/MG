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
		//��fmt�ĸ�ʽ����һ������
		virtual PictureHandle NewPicture(int width, int height, TPOOL_FORMAT fmt);
		//���ڴ��ȡͼƬ����ȡ�������fmtָ���ĸ�ʽ
		virtual PictureHandle LoadPicture(const void* data, int size, TPOOL_FORMAT fmt);
		//���pRect���������data�ĸ�ʽ���handle��Ӧ�������ʽ����һ��
		virtual bool SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect = NULL); 
		//��fmt��ʽ��ȡpRect������������ͼƬ������fmt��ʽ�ģ�ת����Ż�ȡ
		virtual bool GetPictureData(PictureHandle handle, void *data, int size, TPOOL_FORMAT fmt, const CRECT* pRect = NULL);
		//���ͼƬ���
		virtual bool GetPictureInfo(PictureHandle handle, int &width, int &height) const;
		//�ͷ�ͼƬ
		virtual bool ReleasePicture(PictureHandle handle);
		//����ͼƬ
		virtual bool DrawPicture(PictureHandle handle, const FRECT &rect, const FRECT *srcRect, unsigned int color);
	};

}

#endif //#ifndef TXPRENDERER_H