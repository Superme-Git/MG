#include "stdafx.h"
#include "..\renderer\renderer.h"
#include "rendertargetcache.h"

namespace Nuclear
{

	RenderTargetCachePool::RenderTargetCachePool(Renderer *pRenderer) 
		: m_pRenderer(pRenderer), m_nSize(0), m_nWidth(0), m_nHeight(0)
	{
	}

	// ȡ��һ������, todo �����ȶ����Ϊ����������
	RenderTargetCache* RenderTargetCachePool::Get()
	{
		if (m_nSize > m_renderTargetCaches.size())
		{
			RenderTargetCache *pCache = new RenderTargetCache();
			pCache->m_hRenderTarget = m_pRenderer->CreateRenderTarget(m_nWidth, m_nHeight, 
				m_pRenderer->GetDisplayMode().bitperpix == 32? XPTEXFMT_A8R8G8B8 : XPTEXFMT_A4R4G4B4);
			m_renderTargetCaches.push_back(pCache);
			pCache->m_bRendered = false;
			pCache->m_bUsed = true;
			return pCache;
		}
		RTCList::iterator it = m_renderTargetCaches.begin(), ie = m_renderTargetCaches.end();
		for(;it!=ie;++it)
		{
			if( !(*it)->m_bUsed )
			{
				(*it)->m_bRendered = false;
				(*it)->m_bUsed = true;
				return *it;
			}
		}
		return NULL;
	}
	// ����һ������
	void RenderTargetCachePool::Release(RenderTargetCache* pCache)
	{
		if (m_nSize < m_renderTargetCaches.size())
		{
			RTCList::iterator it = m_renderTargetCaches.begin(), ie = m_renderTargetCaches.end();
			for(;it!=ie;++it)
			{
				if( *it == pCache )
				{
					m_pRenderer->FreeRenderTarget(pCache->m_hRenderTarget);
					m_renderTargetCaches.erase(it);
					delete pCache;
					break;
				}
			}
		} else {
			pCache->m_bUsed = false;
		}
	}

	// ���ػ��л���ͼʱ���������ж�����ʹ��
	// ���ô˷���ǰӦȷ���ⲿ������RenderTargetCacheָ�������
	void RenderTargetCachePool::ReleaseAll()
	{
		RTCList::iterator it = m_renderTargetCaches.begin(), ie = m_renderTargetCaches.end();
		for(;it!=ie;++it)
		{
			(*it)->m_bUsed = false;
		}
	}

	// ��ʼ�����豸��ʧʱ������RenderTarget��Դ
	// ���ô˷���ǰӦȷ���ⲿ������RenderTargetCacheָ�������
	void RenderTargetCachePool::Reset(int iCount, int iWidth, int iHeight)
	{
		// Reset ʱ��������ǿգ���ôһ�������豸��ʧʱ���԰ɣ� ���Բ���Ҫ�ͷ�RenderTarget
		//m_renderTargetCaches.clear();
		//m_renderTargetCaches.resize(iCount);
		int oldSize = (int)m_renderTargetCaches.size();
		if (oldSize < iCount)
		{
			for (int i = oldSize;i<iCount;++i)
			{
				RenderTargetCache *pCache = new RenderTargetCache();
				m_renderTargetCaches.push_back(pCache);
			}
		} else {
			for (int i=iCount;i<oldSize;++i)
			{
				delete *m_renderTargetCaches.rbegin();
				m_renderTargetCaches.pop_back();
			}
		}
		m_nSize = iCount;
		m_nWidth = iWidth;
		m_nHeight = iHeight;
		RTCList::iterator it = m_renderTargetCaches.begin(), ie = m_renderTargetCaches.end();
		XPTEXTURE_FORMAT format = m_pRenderer->GetDisplayMode().bitperpix == 32? XPTEXFMT_A8R8G8B8 : XPTEXFMT_A4R4G4B4;
		for(;it!=ie;++it)
		{	
			(*it)->m_hRenderTarget = m_pRenderer->CreateRenderTarget(iWidth, iHeight, format);
			(*it)->m_bRendered = false;
			(*it)->m_bUsed = false;
			assert((*it)->m_hRenderTarget != INVALID_PICTURE_HANDLE);
		}
	}

	// ������
	void RenderTargetCachePool::Destroy()
	{
		RTCList::iterator it = m_renderTargetCaches.begin(), ie = m_renderTargetCaches.end();
		for(;it!=ie;++it)
		{
			m_pRenderer->FreeRenderTarget((*it)->m_hRenderTarget);
			delete (*it);
		}
		m_renderTargetCaches.clear();
	}

}
