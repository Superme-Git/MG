#ifndef __Nuclear_RENDERTARGETCACHE_H__
#define __Nuclear_RENDERTARGETCACHE_H__

#include "../common/nuxptypes.h"
#include <list>

namespace Nuclear
{
	// 用于cache多层地表等地图上的纯静态图片
	// cache 大小不会动态变化，只能通过Reset接口调整
	struct RenderTargetCache
	{
		PictureHandle m_hRenderTarget;
		bool m_bRendered;
		bool m_bUsed;
		RenderTargetCache(PictureHandle hRenderTarget = INVALID_PICTURE_HANDLE) 
			: m_hRenderTarget(hRenderTarget), m_bRendered(false), m_bUsed(false) 
		{ 
		}			
	};

	class Renderer;
	class RenderTargetCachePool
	{
	public:
		RenderTargetCachePool(Renderer *pRenderer);

		// 取得一个对象, todo 功能稳定后改为返回弱引用
		RenderTargetCache* Get();
		// 返还一个对象
		void Release(RenderTargetCache* pCache);

		// 加载或切换地图时，设置所有对象不再使用
		// 调用此方法前应确认外部不再有RenderTargetCache指针的引用
		void ReleaseAll();

		// 初始化或设备丢失时，重置RenderTarget资源
		// 调用此方法前应确认外部不再有RenderTargetCache指针的引用
		void Reset(int iCount, int iWidth, int iHeight);
		void SetSize(int size) { m_nSize = size; }

		void Destroy();

	private:
		typedef std::list<RenderTargetCache*> RTCList;
		Renderer *m_pRenderer;
		RTCList m_renderTargetCaches;
		size_t m_nSize;//可能会和list的size不一致，但会趋向一直
		int m_nWidth;
		int m_nHeight;
	};

}

#endif