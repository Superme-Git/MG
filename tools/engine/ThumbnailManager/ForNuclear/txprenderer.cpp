#include <windows.h>
#include <stdio.h>
#include <tchar.h>
#include <string>
//Assert
#include <assert.h>
//STL
#include <vector>
#include <map>
#include <list>
#include <set>
#include <deque>
#include <hash_map>

#include "txprenderer.h"

namespace TMan
{
	//��fmt�ĸ�ʽ����һ������
	PictureHandle TXPRenderer::NewPicture(int width, int height, TPOOL_FORMAT fmt)
	{
		if (!m_pRenderer)
			return INVALID_PICTURE_HANDLE;
		return m_pRenderer->NewPicture(width, height, NULL, fmt==TPF_8888?Nuclear::XPTEXFMT_A8R8G8B8:Nuclear::XPTEXFMT_A4R4G4B4);
	}

	//���ڴ��ȡͼƬ����ȡ�������fmtָ���ĸ�ʽ
	PictureHandle TXPRenderer::LoadPicture(const void* data, int size, TPOOL_FORMAT fmt)
	{
		if (!m_pRenderer)
			return INVALID_PICTURE_HANDLE;
		return m_pRenderer->LoadPictureFromMem(data, size, NULL, fmt==TPF_8888?Nuclear::XPTEXFMT_A8R8G8B8:Nuclear::XPTEXFMT_A4R4G4B4);
	}

	//���pRect���������data�ĸ�ʽ���handle��Ӧ�������ʽ����һ��
	bool TXPRenderer::SetPictureData(PictureHandle handle, const void *data, int size, const CRECT* pRect)
	{
		if (!m_pRenderer)
			return false;
		if (pRect)
		{
			Nuclear::CRECT rect(pRect->left, pRect->top, pRect->right, pRect->bottom);
			return m_pRenderer->SetPictureData(handle, data, size, &rect);
		} else {
			return m_pRenderer->SetPictureData(handle, data, size, NULL);
		}
		
	}

	//��fmt��ʽ��ȡpRect������������ͼƬ������fmt��ʽ�ģ�ת����Ż�ȡ
	bool TXPRenderer::GetPictureData(PictureHandle handle, void *data, int size, TPOOL_FORMAT fmt, const CRECT* pRect)
	{
		if (!m_pRenderer)
			return false;
		Nuclear::XPTEXTURE_FORMAT xpfmt = fmt==TPF_8888?Nuclear::XPTEXFMT_A8R8G8B8:Nuclear::XPTEXFMT_A4R4G4B4;
		if (pRect)
		{
			Nuclear::CRECT rect(pRect->left, pRect->top, pRect->right, pRect->bottom);
			return m_pRenderer->GetSpeFormatPictureData(handle, data, size, xpfmt, &rect);
		} else {
			return m_pRenderer->GetSpeFormatPictureData(handle, data, size, xpfmt, NULL);
		}
		
	}

	//���ͼƬ���
	bool TXPRenderer::GetPictureInfo(PictureHandle handle, int &width, int &height) const
	{
		if (!m_pRenderer)
			return false;
		Nuclear::PictureInfo picinfo;
		if (m_pRenderer->GetPictureInfo(handle, picinfo))
		{
			width = picinfo.m_nPicWidth;
			height = picinfo.m_nPicHeight;
			return true;
		}
		return false;
	}
	//�ͷ�ͼƬ
	bool TXPRenderer::ReleasePicture(PictureHandle handle)
	{
		if (!m_pRenderer)
			return false;
		return m_pRenderer->FreePicture(handle);
	}
	//����ͼƬ
	bool TXPRenderer::DrawPicture(PictureHandle handle, const FRECT &rect, const FRECT *pSrcRect, unsigned int color)
	{
		if (!m_pRenderer)
			return false;
		Nuclear::DrawBatchPictureParam param;
		Nuclear::XPCOLOR xpcolor = color;
		Nuclear::FRECT drect(rect.left, rect.top, rect.right, rect.bottom);
		Nuclear::FRECT srect;
		param.handle = handle;
		param.colorCount = 1;
		param.pColor = &xpcolor;
		param.pRect = &drect;
		param.rectCount = 1;
		if (pSrcRect)
		{
			srect.left = pSrcRect->left;
			srect.top = pSrcRect->top;
			srect.right = pSrcRect->right;
			srect.bottom = pSrcRect->bottom;
			param.pSrcrect = &srect;
		}
		return m_pRenderer->DrawBatchPicture(param);
	}
}