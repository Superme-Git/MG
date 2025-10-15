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

	// ��������Ľṹ
	struct VPic
	{
		FRECT rect;
		FRECT srcrect;
		SortBaseRectType base; // for sort
		Renderable *pRenderable; // ������������ݽṹ��pRenderable == NULL ��ʾimgͼƬ
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

	struct GroundCachePic//�ر��cache
	{
		FRECT objRect;
		PictureHandle handle;
		XPCOLOR color[4];
	};

	class Camera  // �۲촰�ڣ�����ӿ�
	{		
		friend class World;

		std::vector<GroundCachePic> m_ground_picele_cache[PIC_GROUND_LAYER_COUNT]; // �ر������viewcache
		std::vector<CachePic> m_picele_cache[PIC_LAYER_COUNT]; // �����viewcache���������м��6
		std::vector<VPic> m_mapmobj_cache; // ��ͼ����в��viewcache, �в���Ҫ��spriteһ������
		//std::vector<Effect*> m_effect_update_cache;//����в��effect update cache��
		void (Camera::*m_pUpdateFunc)();
		XPCAMERA_UPDATE_TYPE m_updateType;
		Sprite* m_pAttachedSprite;

		bool m_bDirty;
		bool m_bTracing;
		Location m_locTracingTarget;

		CRECT m_viewport;
		int m_iMapWidth;
		int m_iMapHeight;
		int m_nViewportWidth;//����ǰ�Ĵ�С
		int m_nViewportHeight;//����ǰ�Ĵ�С
		float m_fScale;
		///////////////��ͷƽ�����////////////////////
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
		void Reset(Location loc, int width, int height);//loc����������

		void Update();

		void SetScale(float scale);
		float GetScale() const { return m_fScale; }

		void SetPosition(int l, int t);
		void SetSize(int w, int h);
	
		Location GetLTPosFromCenterPos(Location loc, int mapWidth, int mapHeight) const;

		bool SetCameraUpdateType(XPCAMERA_UPDATE_TYPE type);
		XPCAMERA_UPDATE_TYPE GetCameraUpdateType() const { return m_updateType; }

	private:
		void UpdateCameraPos1(); // ��򵥵�ģʽ���ӿ�ʼ��������Ϊ����
		void UpdateCameraPos2(); // �Ƚϸ��ӣ����Ƚ��ң�������

		// ������insence����ʱ�����޸��ӿڣ�����׷�档
		// �����������С��һ����ʱ��Ч��ͬUpdateCameraPos1
		void UpdateCameraPos3();

		// ������insence����ʱ�����޸��ӿڣ�����׷����ݵ�ǰ���ǵ��ƶ�����׷�档
		// �����������С��һ����ʱ��Ч��ͬUpdateCameraPos1
		Location m_attachLastLocation;
		void UpdateCameraPos4();
	};

}

#endif