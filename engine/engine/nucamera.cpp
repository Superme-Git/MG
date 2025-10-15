#include "../common/nulog.h"
#include "../common/nuworldlogiccoord.h"
#include "../sprite/nusprite.h"
#include "../nuiworld.h"
#include "nucamera.h"

namespace Nuclear
{
	void Camera::updateCameraPos4(int delta)
	{
		if( NULL == m_pAttachedSprite ) return;

		const NuclearLocation locTarget = m_pAttachedSprite->GetLocation();
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
			updateCameraPos3(delta);
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

	void Camera::updateCameraPos3(int delta)
	{
		if( NULL == m_pAttachedSprite ) return;

		const int insensateX = (m_viewport.Width() - 128) / 2;
		const int insensateY = (m_viewport.Height() - 64) / 2;

		const NuclearLocation locTarget = m_pAttachedSprite->GetLocation();

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

	void Camera::updateCameraPos1(int delta)
	{
		if( NULL == m_pAttachedSprite ) return;
		NuclearLocation locTarget = GetLTPosFromCenterPos(m_pAttachedSprite->GetLocation(), m_iMapWidth, m_iMapHeight);

		NuclearLocation locNow;
		locNow.x = m_viewport.left;
		locNow.y = m_viewport.top;

		if( locNow != locTarget )
		{
			SetPosition(locTarget.x, locTarget.y);
		}
	}

	void Camera::updateCameraPos2(int delta)
	{
		static const int MAX_CAMERASTOPMOVE_DIS2 = 256;
		static const int MIN_CAMERASTARTMOVE_DIS2 = 0;
		static const int MAX_CAMERAMOVE_DIS2 = 1024*1024*4;
		static const int MAX_CAMERAMOVE_TIME = 5000;
		static const int MAX_CAMERAMOVE_TIME2 = 10000;
		
		static int m_traceStartTime = 0;
		if( m_pAttachedSprite )
		{			
			NuclearLocation loc = GetLTPosFromCenterPos(m_pAttachedSprite->GetLocation(), m_iMapWidth, m_iMapHeight);
			
			NuclearLocation locnow = m_viewport.Center();

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

	void Camera::updateCameraPos5(int delta)
	{
		mCurDuration += delta;

		NuclearPoint newPos;
		newPos.x = mStartPos.x + mSpeedX * mCurDuration;
		newPos.y = mStartPos.y + mSpeedY * mCurDuration;

		newPos = GetLTPosFromCenterPos(newPos, m_iMapWidth, m_iMapHeight);
		SetPosition(newPos.x, newPos.y);
	}

	void Camera::Update(int now, int delta, bool bIsBattle)
	{
		if (m_ShakeViewportAdj.x != 0 || m_ShakeViewportAdj.y != 0)
		{
			m_viewport -= m_ShakeViewportAdj;
			m_ShakeViewportAdj.x = 0;
			m_ShakeViewportAdj.y = 0;
			m_bDirty = true;
		}
		if (m_pShakeScreenController)
		{
			m_bDirty = true;
			m_ShakeViewportAdj = m_pShakeScreenController->GetShakeScreenAdjust(now, delta);
			m_viewport += m_ShakeViewportAdj;
		}
		//用于镜头跟随
		if (!bIsBattle)
		{
			(this->*m_pUpdateFunc)(delta);
		}
	}

	void Camera::ClearCache()
	{
		for (int l = 0; l < XPPIC_LAYER_COUNT; ++l)
			m_picele_cache[l].clear();
		for (int l = 0; l < XPSORTED_LAYER_COUNT; ++l)
			m_mapmobj_cache[l].clear();
	}

	bool Camera::SetCameraUpdateType(NuclearCameraUpdateType type, void* pParams)
	{
		if (type != m_updateType)
		{
			switch (type)
			{
			case XPCAMERA_HERO_ALWAYS_IN_CENTER:
				m_pUpdateFunc = &Camera::updateCameraPos1;
				break;
			case XPCAMERA_UPDATE_TYPE2:
				m_pUpdateFunc = &Camera::updateCameraPos0;
				break;
			case XPCAMERA_FOLLOW_HERO_WHEN_OUT_OF_INSENCE:
				m_pUpdateFunc = &Camera::updateCameraPos3;
				break;
			case XPCAMERA_FOLLOW_HEROS_DIR_WHEN_OUT_OF_INSENCE:
				m_pUpdateFunc = &Camera::updateCameraPos4;
				break;
			case XPCAMERA_SMOOTH:
				m_pUpdateFunc = &Camera::updateCameraPosSmooth;
				break;
			case XPCAMERA_MOVE_TO_POSITION_IN_DURATION:
				m_pUpdateFunc = &Camera::updateCameraPos5;
				break;
			case XPCAMERA_ACCELERATE:
				m_pUpdateFunc = &Camera::updateCameraPosAccelerate;
				break;
			}
			m_updateType = type;

			setUpdateParams(pParams);
		}

		if (XPCAMERA_ACCELERATE == type)
		{
			mAccelUpdater.reset();
		}

		return true;
	}

	void Camera::setUpdateParams(void* ptr)
	{
		switch (m_updateType)
		{
		case XPCAMERA_MOVE_TO_POSITION_IN_DURATION:
			if (ptr)
			{
				XPCAMERA_MOVE_TO_POSITION_IN_DURATION_PARAMS* pParams = (XPCAMERA_MOVE_TO_POSITION_IN_DURATION_PARAMS*)ptr;
				if (pParams->mDuration <= 0)
				{
					mStartPos = pParams->mTargetPosition;
					mSpeedX = mSpeedY = 0;
				}
				else
				{
					mStartPos = pParams->mStartPosition;
					mSpeedX = float(pParams->mTargetPosition.x - mStartPos.x) / pParams->mDuration;
					mSpeedY = float(pParams->mTargetPosition.y - mStartPos.y) / pParams->mDuration;
				}
				mCurDuration = 0;
			}
			break;
		}
	}

	void Camera::SetShakeScreenController(ShakeScreenController *pController)
	{
		m_pShakeScreenController = pController;
		m_ShakeViewportAdj.x = 0;
		m_ShakeViewportAdj.y = 0;
	}

	void Camera::SetScale(float scale)
	{
		m_fScale = scale;
		NuclearPoint pt = m_viewport.Center();
		int newWidth = static_cast<int>(m_nViewportWidth / m_fScale);
		int newHeight = static_cast<int>(m_nViewportHeight / m_fScale);
		m_viewport.left = pt.x - newWidth / 2;
		m_viewport.top = pt.y - newHeight / 2;
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
		if (m_updateType == XPCAMERA_SMOOTH && m_pAttachedSprite)
		{
			m_smoother.SetFocus(static_cast<float>(m_pAttachedSprite->GetLocation().x),
				static_cast<float>(m_pAttachedSprite->GetLocation().y));
		}
		if (m_pAttachedSprite)
		{
			NuclearLocation locTarget = GetLTPosFromCenterPos(m_pAttachedSprite->GetLocation(), m_iMapWidth, m_iMapHeight);
			m_TgtPos = locTarget.ToFPOINT();
			m_CurPos = m_TgtPos;
		}
	}
	////平滑镜头跟随，镜头中心位置计算更新
	void Camera::updateCameraPosSmooth(int delta)
	{
		if( m_pAttachedSprite )
		{
			float x, y;
			m_smoother.GetCurrentFocus(x, y);
			SetPosition((long)x - m_viewport.Width() / 2,(long)y - m_viewport.Height() / 2);
		}
	}
	////平滑镜头跟随重置
	void Camera::resetSmooth(int width, int height)
	{
		m_smoother.SetViewportExt(static_cast<float>(m_viewport.Width()), static_cast<float>(m_viewport.Height()));
		m_smoother.SetWorldExt(static_cast<float>(width), static_cast<float>(height));
		m_smoother.SetPlayerSpeed( m_pAttachedSprite->GetMoveSpeed() );
		m_smoother.SetFocus(static_cast<float>(m_pAttachedSprite->GetLocation().x),
			static_cast<float>(m_pAttachedSprite->GetLocation().y));
	}
	void Camera::Reset(NuclearLocation loc, int width, int height)
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
			resetSmooth(width, height);
		}
		if (m_pAttachedSprite)
		{
			NuclearLocation locTarget = GetLTPosFromCenterPos(m_pAttachedSprite->GetLocation(), m_iMapWidth, m_iMapHeight);
			m_TgtPos = locTarget.ToFPOINT();
			m_CurPos = m_TgtPos;
		}
	}

	NuclearLocation Camera::GetLTPosFromCenterPos(NuclearLocation loc, int mapWidth, int mapHeight) const
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
				updateCameraPosSmooth(0);
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
								NuclearFPoint target(it->x, it->y);
								FocusTo(m_pAttachedSprite->GetLocation(), NuclearWorldLogicCoord::logic2world(target));
								break;
							}
						}
					}
					else
					{
						astar::Path::reverse_iterator endnode = path.rbegin();
						NuclearFPoint target(endnode->x, endnode->y);
						FocusTo(m_pAttachedSprite->GetLocation(), NuclearWorldLogicCoord::logic2world(target));
					}
				}
			}
		}
	}
	void Camera::SetUseSmooth(bool bSmooth)
	{
		m_bSmooth = bSmooth;
	}
	void Camera::updateCameraPos0(int delta)
	{
		if (NULL == m_pAttachedSprite)
		{
			return;
		}

		NuclearLocation NewPos = m_pAttachedSprite->GetLocation();
		//NewPos.y += m_pAttachedSprite->GetFlyOffsetYCur();

		NuclearLocation locTarget = GetLTPosFromCenterPos(NewPos, m_iMapWidth, m_iMapHeight);

		if (m_bSmooth)
		{
			float fRate = 1.0f;
			if (m_TgtPos.Dis2(locTarget) < 256.0f)
			{
				fRate = 1.0f + (1.0f - m_TgtPos.Dis2(locTarget) / 256.0f);
			}
			m_TgtPos = locTarget.ToFPOINT();
			NuclearFPoint OffPos = m_TgtPos - m_CurPos;
			OffPos = OffPos * 0.001f * delta * fRate;
			NuclearFPoint NewPos = m_CurPos + OffPos;
			m_CurPos = NewPos;

			float halfViewHeight = m_viewport.Height() * 0.5;
			float maxDist = halfViewHeight * 0.8;
			float dist = sqrtf(locTarget.Dis2(m_CurPos));
			if (dist > maxDist)
			{
				NuclearFPoint v = m_CurPos - locTarget;
				v.normalize();
				m_CurPos = locTarget + v * maxDist;
			}
		}
		else
		{
			m_CurPos = locTarget;
		}
		SetPosition((int)m_CurPos.x, (int)m_CurPos.y);
	}

	void Camera::updateCameraPosAccelerate(int delta)
	{
		if (m_pAttachedSprite)
		{
			NuclearLocation locTarget = GetLTPosFromCenterPos(m_pAttachedSprite->GetLocation(), m_iMapWidth, m_iMapHeight);

			if (!mAccelUpdater.isInited())
			{
				float halfViewHeight = m_viewport.Height() * 0.5;
				float maxDist = halfViewHeight * 0.8;
				mAccelUpdater.init(locTarget, m_CurPos, maxDist);
			}

			float dt = delta * 0.001;
			mAccelUpdater.update(dt, locTarget);

			m_CurPos = mAccelUpdater.getFollowPosition();
			SetPosition((int)m_CurPos.x, (int)m_CurPos.y);
		}
	}
}