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

	// ��������Ľṹ
	struct VPic
	{
		NuclearFRectt rect;
		NuclearFRectt srcrect;
		NuclearSortBaseRectType base; // for sort
		Renderable *pRenderable; // ������������ݽṹ��pRenderable == NULL ��ʾimgͼƬ
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

	struct GroundCachePic//�ر��cache
	{
		NuclearFRectt objRect;
		PictureHandle handle;
		NuclearColor color[4];
		GroundCachePic() : handle(0) {}
	};

	class Camera  // �۲촰�ڣ�����ӿ�
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
		void Reset(NuclearLocation loc, int width, int height);//loc����������

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
		void updateCameraPos1(int delta); // ��򵥵�ģʽ���ӿ�ʼ��������Ϊ����
		void updateCameraPos2(int delta); // �Ƚϸ��ӣ����Ƚ��ң�������
		void updateCameraPos3(int delta); // ������insence����ʱ�����޸��ӿڣ�����׷��,�����������С��һ����ʱ��Ч��ͬupdateCameraPos1��
		void updateCameraPos4(int delta); // ������insence����ʱ�����޸��ӿڣ�����׷����ݵ�ǰ���ǵ��ƶ�����׷��,�����������С��һ����ʱ��Ч��ͬupdateCameraPos1
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
		std::vector<GroundCachePic> m_ground_picele_cache[XPPIC_GROUND_LAYER_COUNT]; // �ر������viewcache
		std::vector<CachePic> m_picele_cache[XPPIC_LAYER_COUNT]; // �����viewcache���������м��
		//��ͼ����в��viewcache, �в���Ҫ��spriteһ������ֻ�������е�ͼ�����XPSL_FLY��XPSL_BATTLE����ֻ�о���ģ������Ժ���ж�̬��Ч�������ڵ�ͼ�༭������ŵ���Ч��
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
		int m_nViewportWidth;//����ǰ�Ĵ�С
		int m_nViewportHeight;//����ǰ�Ĵ�С
		float m_fScale;
		///////////////��ͷƽ�����////////////////////
		CameraSmoother		m_smoother;

		// XPCAMERA_MOVE_TO_POSITION_IN_DURATION ����
		NuclearPoint mStartPos;
		float mSpeedX, mSpeedY;
		int mCurDuration;

		AccelerateUpdater mAccelUpdater;
	};

}

#endif