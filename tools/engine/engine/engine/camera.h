#ifndef __Nuclear_CAMERA_H
#define __Nuclear_CAMERA_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\mapconst.h"
#include "..\common\xptypes.h"
#include "..\common\recttopologylist.h"
#include "camerasmoother.h"

namespace Nuclear
{
	class Sprite;
	class Renderable;

	// 用于排序的结构
	struct VPic
	{
		FRECT rect;
		FRECT srcrect;
		SortBaseRectType base; // for sort
		Renderable *pRenderable; // 简单起见公用数据结构，pRenderable == NULL 表示img图片
		void *pObjPic;
		PictureHandle handle;
		SortBaseRectType GetRect() const { return base; }
	};

	struct CachePic
	{
		FRECT objRect;
		FRECT srcRect;
		PictureHandle handle;
		XPCOLOR color;
	};

	struct GroundCachePic//地表的cache
	{
		FRECT objRect;
		PictureHandle handle;
		XPCOLOR color[4];
	};

	class Camera  // 观察窗口，玩家视口
	{		
		friend class World;

		std::vector<GroundCachePic> m_ground_picele_cache[PIC_GROUND_LAYER_COUNT]; // 地表物件层viewcache
		std::vector<CachePic> m_picele_cache[PIC_LAYER_COUNT]; // 物件层viewcache，不包括中间层6
		std::vector<VPic> m_mapmobj_cache; // 地图物件中层的viewcache, 中层需要和sprite一起排序
		//std::vector<Effect*> m_effect_update_cache;//物件中层的effect update cache。
		void (Camera::*m_pUpdateFunc)();
		XPCAMERA_UPDATE_TYPE m_updateType;
		Sprite* m_pAttachedSprite;

		bool m_bDirty;
		bool m_bTracing;
		Location m_locTracingTarget;

		CRECT m_viewport;
		int m_iMapWidth;
		int m_iMapHeight;
		int m_nViewportWidth;//缩放前的大小
		int m_nViewportHeight;//缩放前的大小
		float m_fScale;
		///////////////镜头平滑相关////////////////////
		CCameraSmoother		m_smoother;
	private:
		void ResetSmooth(int width, int height);
		void UpdateCameraPosSmooth();
		void tick(int delta);
	public:
		void FocusTo(FPOINT ptCur,FPOINT ptTarget)
		{
			m_smoother.FocusTo(ptCur.x, ptCur.y, ptTarget.x, ptTarget.y);
		}
		Camera()
			: m_bDirty(true), m_pAttachedSprite(NULL), m_bTracing(false), m_iMapWidth(0), m_iMapHeight(0)
			, m_attachLastLocation(-1, -1), m_nViewportWidth(0), m_nViewportHeight(0), m_fScale(1.0f)
		{
			m_pUpdateFunc = &Camera::UpdateCameraPos1;
			m_updateType = XPCAMERA_HERO_ALWAYS_IN_CENTER;
		}
		~Camera() { }

		CRECT GetViewport() { return m_viewport; }
		const CRECT& GetConstViewport() const { return m_viewport; }

		void ClearCache();
		void Attach(Sprite* sprite);
		void Reset(Location loc, int width, int height);//loc是中心坐标

		void Update();

		void SetScale(float scale);
		float GetScale() const { return m_fScale; }

		void SetPosition(int l, int t);
		void SetSize(int w, int h);
	
		Location GetLTPosFromCenterPos(Location loc, int mapWidth, int mapHeight) const;

		bool SetCameraUpdateType(XPCAMERA_UPDATE_TYPE type);
		XPCAMERA_UPDATE_TYPE GetCameraUpdateType() const { return m_updateType; }

	private:
		void UpdateCameraPos1(); // 最简单的模式，视口始终以主角为中心
		void UpdateCameraPos2(); // 比较复杂，还比较乱，待整理

		// 主角在insence区域时，不修改视口，否则追随。
		// 当这个区域缩小到一个点时，效果同UpdateCameraPos1
		void UpdateCameraPos3();

		// 主角在insence区域时，不修改视口，否则追随根据当前主角的移动方向追随。
		// 当这个区域缩小到一个点时，效果同UpdateCameraPos1
		Location m_attachLastLocation;
		void UpdateCameraPos4();
	};

}

#endif