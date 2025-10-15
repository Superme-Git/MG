#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\worldlogiccoord.h"
#include "..\sprite\sprite.h"
#include "camera.h"

namespace Nuclear
{
	//void Camera::UpdateCameraPos5(DWORD now)
	//{
	//}

	void Camera::UpdateCameraPos4()
	{
		if( NULL == m_pAttachedSprite ) return;

		const Location locTarget = m_pAttachedSprite->GetLocation();
		if (m_attachLastLocation.x < 0) // 初始化
			m_attachLastLocation = locTarget;

		// 角在地图边缘时，此时无法保持移动方向一致。
		const int halfWidth = m_viewport.Width() / 2;
		const int halfHeight = m_viewport.Height() / 2;
		if (locTarget.x < halfWidth || locTarget.x > m_iMapWidth - halfWidth
			|| locTarget.y < halfHeight || locTarget.y > m_iMapHeight - halfHeight
			|| m_attachLastLocation.x < halfWidth || m_attachLastLocation.x > m_iMapWidth - halfWidth
			|| m_attachLastLocation.y < halfHeight || m_attachLastLocation.y > m_iMapHeight - halfHeight
			)
		{
			m_attachLastLocation = locTarget;
			UpdateCameraPos3();
			return;
		}

		// 如果主角非敏感区，考虑移动方向，修改viewport。
		// 为了实现方便，并不把主角定位到insensate区域的边缘，而是内部附近。
		const int insensateX = halfWidth - 128 / 2;
		const int insensateY = halfHeight - 64 / 2;
		// deflate
		if (locTarget.x < m_viewport.left + insensateX || locTarget.x > m_viewport.right - insensateX
			|| locTarget.y < m_viewport.top + insensateY || locTarget.y > m_viewport.bottom - insensateY)
		{
			int viewLeft = m_viewport.left + locTarget.x - m_attachLastLocation.x;
			int viewTop = m_viewport.top + locTarget.y - m_attachLastLocation.y;
			// 地图边缘修正
			if (viewLeft < 0) viewLeft = 0;
			else if (viewLeft > m_iMapWidth - m_viewport.Width()) viewLeft = m_iMapWidth - m_viewport.Width();
			if (viewTop < 0) viewTop = 0;
			else if (viewTop > m_iMapWidth - m_viewport.Height()) viewTop = m_iMapHeight - m_viewport.Height();
			// 放到 SetPosition 内部判断？应该是为了那个m_bDirty
			if (viewLeft != m_viewport.left || viewTop != m_viewport.top) 
				SetPosition(viewLeft, viewTop);
		}
		m_attachLastLocation = locTarget;
	}

	void Camera::ClearCache() 
	{
		for(int l=0; l < PIC_LAYER_COUNT; ++l)
			m_picele_cache[l].clear();
		m_mapmobj_cache.clear();
	}

	void Camera::UpdateCameraPos3()
	{
		if( NULL == m_pAttachedSprite ) return;

		const int insensateX = (m_viewport.Width() - 128) / 2;
		const int insensateY = (m_viewport.Height() - 64) / 2;

		const Location locTarget = m_pAttachedSprite->GetLocation();

		// 只看主角是否离开非敏感区，不考虑移动方向，主角移动方向和摄像机移动可能出现不一致。
		int viewLeft = m_viewport.left;
		if (locTarget.x < m_viewport.left + insensateX) // 区域左方
			viewLeft = locTarget.x - insensateX;
		else if (locTarget.x > m_viewport.right - insensateX) // 区域右方
			viewLeft = locTarget.x + insensateX - m_viewport.Width();
		int viewTop = m_viewport.top;
		if (locTarget.y < m_viewport.top + insensateY) // 区域上方
			viewTop = locTarget.y - insensateY;
		else if (locTarget.y > m_viewport.bottom - insensateY)  // 区域下方
			viewTop = locTarget.y + insensateY - m_viewport.Height();

		// 地图边缘修正
		if (viewLeft < 0) viewLeft = 0;
		else if (viewLeft > m_iMapWidth - m_viewport.Width()) viewLeft = m_iMapWidth - m_viewport.Width();
		if (viewTop < 0) viewTop = 0;
		else if (viewTop > m_iMapWidth - m_viewport.Height()) viewTop = m_iMapHeight - m_viewport.Height();

		// 放到 SetPosition 内部判断？应该是为了那个m_bDirty
		if (viewLeft != m_viewport.left || viewTop != m_viewport.top) 
			SetPosition(viewLeft, viewTop);
	}

	void Camera::UpdateCameraPos1()
	{
		if( NULL == m_pAttachedSprite ) return;
		Location locTarget = GetLTPosFromCenterPos(m_pAttachedSprite->GetLocation(), m_iMapWidth, m_iMapHeight);

		Location locNow;
		locNow.x = m_viewport.left;
		locNow.y = m_viewport.top;

		if( locNow != locTarget )
		{
			SetPosition(locTarget.x, locTarget.y);
		}
	}

	void Camera::UpdateCameraPos2()
	{
		static const int MAX_CAMERASTOPMOVE_DIS2 = 256;
		//static const int MIN_CAMERASTARTMOVE_DIS2 = 4096;
		static const int MIN_CAMERASTARTMOVE_DIS2 = 0;
		static const int MAX_CAMERAMOVE_DIS2 = 1024*1024*4;
		static const int MAX_CAMERAMOVE_TIME = 5000;
		static const int MAX_CAMERAMOVE_TIME2 = 10000;
		
		//static Location m_locTracingTarget;
		static int m_traceStartTime = 0;

		if( m_pAttachedSprite )
		{
			
			Location loc = GetLTPosFromCenterPos(m_pAttachedSprite->GetLocation(), m_iMapWidth, m_iMapHeight);
			
			Location locnow = m_viewport.Center();

			bool continuetracing = false;
			int dis2 = locnow.Dis2(m_locTracingTarget);			
			if( dis2 < MAX_CAMERASTOPMOVE_DIS2 )
			{
				m_locTracingTarget = loc;
				m_bTracing = false;
				return;
			}
			else if( dis2 < MIN_CAMERASTARTMOVE_DIS2 ) 
			{
				if( m_bTracing )
					continuetracing = true;
				else
				{
					m_locTracingTarget = loc;
					return;
				}
			}
			else if( dis2 > MAX_CAMERAMOVE_DIS2 )
			{
				SetPosition(loc.x - m_viewport.Width()/ 2, loc.y - m_viewport.Height() / 2);
				m_bTracing = false;
			}
			else // 尝试平滑
			{
				continuetracing = true;
				if( !m_bTracing )
				{
					m_locTracingTarget = loc;
					m_bTracing = true;
					m_traceStartTime = (int)GetMilliSeconds();
				}
				
			}

			if( continuetracing ) // 计算位置 时间是 MAX_CAMERAMOVE_TIME
			{
				int maxtime = MAX_CAMERAMOVE_TIME2;
				if( loc != m_locTracingTarget ) //todo 修正
				{
					maxtime = MAX_CAMERAMOVE_TIME;
				}

				int d = (int)GetMilliSeconds() - m_traceStartTime;
				float r = (float)d/maxtime;
				if( r > 1.0f || r < 0.0f )
				{
					XPTRACE(L"r = %f\n", r);
				}
				// 尝试线性先
				int x = locnow.x + (int)((loc.x - locnow.x) * r);
				int y = locnow.y + (int)((loc.y - locnow.y) * r);
				SetPosition(x - m_viewport.Width()/ 2, y - m_viewport.Height() / 2);
			}
			m_locTracingTarget = loc;
		}
	}

	bool Camera::SetCameraUpdateType(XPCAMERA_UPDATE_TYPE type)
	{
		if (type != m_updateType)
		{
			switch (type)
			{
			case XPCAMERA_HERO_ALWAYS_IN_CENTER:
				m_pUpdateFunc = &Camera::UpdateCameraPos1;
				break;
			case XPCAMERA_UPDATE_TYPE2:
				m_pUpdateFunc = &Camera::UpdateCameraPos2;
				break;
			case XPCAMERA_FOLLOW_HERO_WHEN_OUT_OF_INSENCE:
				m_pUpdateFunc = &Camera::UpdateCameraPos3;
				break;
			case XPCAMERA_FOLLOW_HEROS_DIR_WHEN_OUT_OF_INSENCE:
				m_pUpdateFunc = &Camera::UpdateCameraPos4;
				break;
			case XPCAMERA_SMOOTH:
				m_pUpdateFunc = &Camera::UpdateCameraPosSmooth;
				break;
			}
			m_updateType = type;
		}
		return true;
	}

	void Camera::Update()
	{
		(this->*m_pUpdateFunc)();
	}

	void Camera::SetScale(float scale)
	{
		m_fScale = scale;
		CPOINT pt = m_viewport.Center();
		int newWidth = static_cast<int>(m_nViewportWidth / m_fScale);
		int newHeight = static_cast<int>(m_nViewportHeight / m_fScale);
		m_viewport.left = pt.x - newWidth / 2;
		m_viewport.top = pt.x - newHeight / 2;
		m_viewport.right = m_viewport.left + newWidth;
		m_viewport.bottom = m_viewport.top + newHeight;
		m_bDirty = true;
		if (m_updateType == XPCAMERA_SMOOTH)
		{
			m_smoother.SetViewportExt((float)newWidth, (float)newHeight);
		}
	}
	void Camera::Attach(Sprite* sprite) 	
	{ 
		m_pAttachedSprite = sprite; 
		//以下add by baiyu
		if (m_updateType == XPCAMERA_SMOOTH && m_pAttachedSprite)
		{
			m_smoother.SetFocus(static_cast<float>(m_pAttachedSprite->GetLocation().x),
				static_cast<float>(m_pAttachedSprite->GetLocation().y));
		}
	}
	////平滑镜头跟随，镜头中心位置计算更新~	add by baiyu
	void Camera::UpdateCameraPosSmooth()
	{
		if( m_pAttachedSprite )
		{
			float x, y;
			m_smoother.GetCurrentFocus(x, y);
			SetPosition((long)x - m_viewport.Width() / 2,(long)y - m_viewport.Height() / 2);
		}
	}
	////平滑镜头跟随重置~	add by baiyu
	void Camera::ResetSmooth(int width, int height)
	{
		m_smoother.SetViewportExt(static_cast<float>(m_viewport.Width()), static_cast<float>(m_viewport.Height()));
		m_smoother.SetWorldExt(static_cast<float>(width), static_cast<float>(height));
		m_smoother.SetPlayerSpeed( m_pAttachedSprite->GetMoveSpeed() );
		m_smoother.SetFocus(static_cast<float>(m_pAttachedSprite->GetLocation().x),
			static_cast<float>(m_pAttachedSprite->GetLocation().y));
	}
	void Camera::Reset(Location loc, int width, int height)
	{
		m_iMapWidth = width;
		m_iMapHeight = height;
		m_bTracing = false;
		if (loc.x > width)
			loc.x = width;
		if (loc.y > height)
			loc.y = height;
		if (loc.x < 0)
			loc.x = 0;
		if (loc.y < 0)
			loc.y = 0;
		if( m_pAttachedSprite )
		{
			m_pAttachedSprite->SetLocation(loc);
		}
		loc = GetLTPosFromCenterPos(loc, m_iMapWidth, m_iMapHeight);
		SetPosition(loc.x, loc.y);
		//平滑镜头跟随
		if (m_updateType == XPCAMERA_SMOOTH)
		{
			ResetSmooth(width, height);
		}
		
	}

	Location Camera::GetLTPosFromCenterPos(Location loc, int mapWidth, int mapHeight) const
	{
		if( mapWidth )
		{
			if( loc.x + m_viewport.Width()/2 > mapWidth )
				loc.x = mapWidth - m_viewport.Width()/2;
			if( loc.x < m_viewport.Width()/2 )
				loc.x = m_viewport.Width()/2;
		}
		if( mapHeight )
		{
			if( loc.y + m_viewport.Height()/2 > mapHeight )
				loc.y = mapHeight - m_viewport.Height()/2;
			if( loc.y < m_viewport.Height()/2 )
				loc.y = m_viewport.Height()/2;
		}
		loc.x -= m_viewport.Width() / 2;
		loc.y -= m_viewport.Height() / 2;
		return loc;
	}

	void Camera::SetPosition(int l, int t) 
	{ 
		m_viewport.right = l + m_viewport.Width(); 
		m_viewport.bottom =t + m_viewport.Height();
		m_viewport.left = l; 
		m_viewport.top = t; 
		//XPTRACE(L"SetPosition %d,%d, dirty = true\n", l, t);
		m_bDirty = true;
	}

	void Camera::SetSize(int w, int h) 
	{ 
		if( w != m_nViewportWidth || h != m_nViewportHeight )
		{
			m_nViewportWidth = w;
			m_nViewportHeight = h;
			m_viewport.right = m_viewport.left + static_cast<int>(w / m_fScale);
			m_viewport.bottom = m_viewport.top + static_cast<int>(h / m_fScale);
			m_locTracingTarget.x = 0;
			m_locTracingTarget.y = 0;
			m_bDirty = true;
			if (m_updateType == XPCAMERA_SMOOTH)
			{
				m_smoother.SetViewportExt((float)w, (float)h);
				UpdateCameraPosSmooth();
			}
		}
	}

	void Camera::tick(int delta)
	{
		if (m_updateType == XPCAMERA_SMOOTH)
		{
			m_smoother.Update( delta/1000.0f );
			if (m_pAttachedSprite)
			{
				astar::Path path;
				m_pAttachedSprite->GetPath(path);
				if (!path.empty())
				{
					if (path.size() >= 3 )
					{
						int i=0;
						for (astar::Path::iterator it=path.begin();it!=path.end();it++,i++)
						{
							if (i == 2)
							{
								FPOINT target(it->x, it->y);
								FocusTo(m_pAttachedSprite->GetLocation(), 
									WorldLogicCoord::logic2world(target));
								break;
							}
						}
					}
					else
					{
						astar::Path::reverse_iterator endnode = path.rbegin();
						FPOINT target(endnode->x, endnode->y);
						FocusTo(m_pAttachedSprite->GetLocation(), 
							WorldLogicCoord::logic2world(target));
					}
				}
			}
		}
	}
}

