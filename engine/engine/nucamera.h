#ifndef __Nuclear_CAMERA_H__
#define __Nuclear_CAMERA_H__

#include "../common/numapconst.h"
#include "../common/nuxptypes.h"
#include "../common/nurecttopologylist.h"
#include "nucamerasmoother.h"
#include "nuAccelerateUpdater.h"

namespace Nuclear
{
	class Sprite;
	class Renderable;
	class ShakeScreenController;

	// 用于排序的结构
	struct VPic
	{
		NuclearFRectt rect;
		NuclearFRectt srcrect;
		NuclearSortBaseRectType base; // for sort
		Renderable *pRenderable; // 简单起见公用数据结构，pRenderable == NULL 表示img图片
		void *pObjPic;
		PictureHandle handle;
		VPic() : pRenderable(0), pObjPic(0), handle(0) {}
		NuclearSortBaseRectType GetRect() const { return base; }
	};

	struct CachePic
	{
		NuclearFRectt objRect;
		NuclearFRectt srcRect;
		PictureHandle handle;
		NuclearColor color;
		CachePic() : handle(0) {}
	};

	struct GroundCachePic//地表的cache
	{
		NuclearFRectt objRect;
		PictureHandle handle;
		NuclearColor color[4];
		GroundCachePic() : handle(0) {}
	};

	class Camera  // 观察窗口，玩家视口
	{		
		friend class World;

	public:
		void FocusTo(NuclearFPoint ptCur,NuclearFPoint ptTarget)
		{
			m_smoother.FocusTo(ptCur.x, ptCur.y, ptTarget.x, ptTarget.y);
		}
		Camera()
			: m_bDirty(true), m_pAttachedSprite(NULL), m_bTracing(false), m_iMapWidth(0), m_iMapHeight(0)
			, m_attachLastLocation(-1, -1), m_nViewportWidth(0), m_nViewportHeight(0), m_fScale(1.0f),
			m_pShakeScreenController(NULL), m_bSmooth(true)
		{
			m_pUpdateFunc = &Camera::updateCameraPos1;
			m_updateType = XPCAMERA_HERO_ALWAYS_IN_CENTER;
		}
		~Camera() { }

		void Update(int now, int delta, bool bIsBattle);

		const NuclearRect& GetViewport() const { return m_viewport; }
		const NuclearRect& GetConstViewport() const { return m_viewport; }

		const NuclearPoint& GetShakeViewportAdj() const { return m_ShakeViewportAdj; }

		void ClearCache();
		void Attach(Sprite* sprite);
		void Reset(NuclearLocation loc, int width, int height);//loc是中心坐标

		void SetScale(float scale);
		float GetScale() const { return m_fScale; }

		void SetPosition(int l, int t);
		void SetSize(int w, int h);
	
		NuclearLocation GetLTPosFromCenterPos(NuclearLocation loc, int mapWidth, int mapHeight) const;

		bool SetCameraUpdateType(NuclearCameraUpdateType type, void* pParams = NULL);
		NuclearCameraUpdateType GetCameraUpdateType() const { return m_updateType; }

		virtual void SetShakeScreenController(ShakeScreenController *pController);
		virtual ShakeScreenController* GetShakeScreenController() const { return m_pShakeScreenController; }

		void SetUseSmooth(bool bSmooth);

	private:
		bool m_bSmooth;
		

		void updateCameraPos0(int delta);
		void updateCameraPos1(int delta); // 最简单的模式，视口始终以主角为中心
		void updateCameraPos2(int delta); // 比较复杂，还比较乱，待整理
		void updateCameraPos3(int delta); // 主角在insence区域时，不修改视口，否则追随,当这个区域缩小到一个点时，效果同updateCameraPos1。
		void updateCameraPos4(int delta); // 主角在insence区域时，不修改视口，否则追随根据当前主角的移动方向追随,当这个区域缩小到一个点时，效果同updateCameraPos1
		void updateCameraPos5(int delta);
		void updateCameraPosAccelerate(int delta);

		void setUpdateParams(void* ptr);

		void resetSmooth(int width, int height);
		void updateCameraPosSmooth(int delta);
		void tick(int delta);

		NuclearLocation	m_attachLastLocation;
		NuclearFPoint		m_CurPos;
		NuclearFPoint		m_TgtPos;

	private:
		std::vector<GroundCachePic> m_ground_picele_cache[XPPIC_GROUND_LAYER_COUNT]; // 地表物件层viewcache
		std::vector<CachePic> m_picele_cache[XPPIC_LAYER_COUNT]; // 物件层viewcache，不包括中间层
		//地图物件中层的viewcache, 中层需要和sprite一起排序，只有两层有地图物件，XPSL_FLY和XPSL_BATTLE都是只有精灵的，或者以后会有动态特效（不是在地图编辑器里面放的特效）
		std::vector<VPic> m_mapmobj_cache[XPSORTED_LAYER_COUNT];
		void (Camera::*m_pUpdateFunc)(int);
		NuclearCameraUpdateType m_updateType;
		Sprite* m_pAttachedSprite;
		ShakeScreenController *m_pShakeScreenController;
		NuclearPoint m_ShakeViewportAdj;

		bool m_bDirty;
		bool m_bTracing;
		NuclearLocation m_locTracingTarget;

		NuclearRect m_viewport;
		int m_iMapWidth;
		int m_iMapHeight;
		int m_nViewportWidth;//缩放前的大小
		int m_nViewportHeight;//缩放前的大小
		float m_fScale;
		///////////////镜头平滑相关////////////////////
		CameraSmoother		m_smoother;

		// XPCAMERA_MOVE_TO_POSITION_IN_DURATION 参数
		NuclearPoint mStartPos;
		float mSpeedX, mSpeedY;
		int mCurDuration;

		AccelerateUpdater mAccelUpdater;
	};

}

#endif