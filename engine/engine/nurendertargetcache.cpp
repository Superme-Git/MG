#include "../renderer/nurenderer.h"
#include "nurendertargetcache.h"

namespace Nuclear
{
	RenderTargetCachePool::RenderTargetCachePool(Renderer *pRenderer) 
		: m_pRenderer(pRenderer), m_nSize(0), m_nWidth(0), m_nHeight(0)
	{
	}

	// ȡ��һ������, todo �����ȶ����Ϊ����������
	RenderTargetCache* RenderTargetCachePool::Get()
	{
        return NULL;
		if (m_nSize > m_renderTargetCaches.size())
		{
            assert(false);
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
        return;
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
        return;
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
        return;
		// Reset ʱ��������ǿգ���ôһ�������豸��ʧʱ���԰ɣ� ���Բ���Ҫ�ͷ�RenderTarget
		int oldSize = (int)m_renderTargetCaches.size();
		if (oldSize < iCount)
		{
			for (int i = oldSize;i<iCount;++i)
			{
				RenderTargetCache *pCache = new RenderTargetCache();
				m_renderTargetCaches.push_back(pCache);
			}
		}
		else
		{
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
		NuclearTextureFormat format = XPTEXFMT_A8R8G8B8;
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
