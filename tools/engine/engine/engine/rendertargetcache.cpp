#include "stdafx.h"
#include "..\renderer\renderer.h"
#include "rendertargetcache.h"

namespace Nuclear
{

	RenderTargetCachePool::RenderTargetCachePool(Renderer *pRenderer) 
		: m_pRenderer(pRenderer), m_nSize(0), m_nWidth(0), m_nHeight(0)
	{
	}

	// 取得一个对象, todo 功能稳定后改为返回弱引用
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
	// 返还一个对象
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

	// 加载或切换地图时，设置所有对象不再使用
	// 调用此方法前应确认外部不再有RenderTargetCache指针的引用
	void RenderTargetCachePool::ReleaseAll()
	{
		RTCList::iterator it = m_renderTargetCaches.begin(), ie = m_renderTargetCaches.end();
		for(;it!=ie;++it)
		{
			(*it)->m_bUsed = false;
		}
	}

	// 初始化或设备丢失时，重置RenderTarget资源
	// 调用此方法前应确认外部不再有RenderTargetCache指针的引用
	void RenderTargetCachePool::Reset(int iCount, int iWidth, int iHeight)
	{
		// Reset 时如果容器非空，那么一定是在设备丢失时，对吧？ 所以不需要释放RenderTarget
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

	// 结束语
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
