
#ifndef __Nuclear_RENDERTARGETCACHE_H
#define __Nuclear_RENDERTARGETCACHE_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"

namespace Nuclear
{

	// ����cache���ر�ȵ�ͼ�ϵĴ���̬ͼƬ
	// cache ��С���ᶯ̬�仯��ֻ��ͨ��Reset�ӿڵ���
	struct RenderTargetCache
	{
		PictureHandle m_hRenderTarget;
		bool m_bRendered;
		bool m_bUsed;
		//bool bEmpty;
		RenderTargetCache(PictureHandle hRenderTarget = INVALID_PICTURE_HANDLE) 
			: m_hRenderTarget(hRenderTarget), m_bRendered(false), m_bUsed(false) 
		{ 
		}			
	};

	class Renderer;
	class RenderTargetCachePool
	{
		typedef std::list<RenderTargetCache*> RTCList;
		Renderer *m_pRenderer;
		RTCList m_renderTargetCaches;
		size_t m_nSize;//���ܻ��list��size��һ�£���������һֱ
		int m_nWidth;
		int m_nHeight;

	public:
		RenderTargetCachePool(Renderer *pRenderer);

		// ȡ��һ������, todo �����ȶ����Ϊ����������
		RenderTargetCache* Get();
		// ����һ������
		void Release(RenderTargetCache* pCache);

		// ���ػ��л���ͼʱ���������ж�����ʹ��
		// ���ô˷���ǰӦȷ���ⲿ������RenderTargetCacheָ�������
		void ReleaseAll();

		// ��ʼ�����豸��ʧʱ������RenderTarget��Դ
		// ���ô˷���ǰӦȷ���ⲿ������RenderTargetCacheָ�������
		void Reset(int iCount, int iWidth, int iHeight);
		void SetSize(int size) { m_nSize = size; }

		// ������
		void Destroy();

	};

}

#endif