#ifndef __Nuclear_WORLD_H
#define __Nuclear_WORLD_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\sound\streamsound.h"
#include "..\engine\renderable.h"
#include "..\engine\camera.h"
#include "..\engine\rendertargetcache.h"
#include "..\map\pmap.h"
#include "..\iengine.h"
#include "..\iworld.h"
#include "..\isprite.h"
#include "immovableobj.h"
#include "picresrc.h"

#define XPDELTA_COUNT 200
namespace Nuclear
{
	class XPEnvSoundArea;
	class XPBkgSoundArea;
	class Effect;
	class ISelectableObj;
	class Engine;
	class ReadMapTask;
	namespace Region
	{
		struct TriggerObjInfo;
	}
	namespace SubMap
	{
		class PPathMap;
	}

	union TriggerKey
	{
		struct {
			int infoVetID;
			Region::TriggerObjInfo* pTriggerObjInfo;
		};
		unsigned __int64 id;
	};

	enum XPSPRITE_LAYER
	{
		XPSL_MIDDLE = 0, //�в㾫��
		XPSL_BATTLE, //ս������
		XPSL_FLY,	//���о���
	};
	enum XPSPRITE_LAYER_COUNT
	{
		XPSL_COUNT = XPSL_FLY + 1,
	};

	class World : public IWorld, public SpriteNotify
	{
		typedef struct _WarBackgroundInfo
		{
			std::wstring name;
			PictureHandle picHandle;
			int width;
			int height;
		} WarBackgroundInfo;
		typedef std::map<std::wstring, WarBackgroundHandle> WarBackgroundHandleMap;
		typedef std::map<WarBackgroundHandle, WarBackgroundInfo> WarBackgroundMap;
		typedef std::list<Effect*> EffectList;
		enum XPReadMapTaskType
		{
			XPRMTT_MAP = 0,
			XPRMTT_MAZE,
			XPRMTT_SOUND,
			XPRMTT_STEP_SOUND,
		};
		enum XPReadMapTaskTypeCount
		{
			XPRMTT_COUNT = XPRMTT_STEP_SOUND + 1,
		};

		friend class SingleEffectNotify;
		friend class WorldEffectnotify;
		friend class ReadMapTask;

		//struct LoadMapEffectParm
		//{
		//	XPLOADMAP_EFFECT loadingMapEffect;
		//	int nBeginTick;
		//	int nViewportWidth;
		//	int nViewportHeight;
		//	bool bIsDrawProtagonist;
		//	//�������ݿ��ܻ���effect���������Ӷ����������
		//	//Location protagonistLoc;
		//	//PictureHandle transPicHandle;
		//	//SubMap::TransObjInfo transInfo;
		//};

		Engine *m_pEngine;
		Canvas m_canvas;
		std::list<ImmovableObj*> m_immobjs;
		WarBackgroundHandleMap m_wbhm;
		WarBackgroundMap m_wbm;
		WarBackgroundHandle m_wbhSeed;
		PictureHandle m_warBackGroundPic;
		PictureHandle m_maskPicHandle;
		std::wstring m_maskPicUri;
		int	m_nWarBackGroundPicWidth;
		int	m_nWarBackGroundPicHeight;
		ReadMapTask *m_pReadMapTask[XPRMTT_COUNT];//�ֱ��ǵ�ͼ��maze��������xml���Ų�������ssm

		//��ͨ״̬
		std::list<Sprite*> m_sprites;
		EffectList m_effects[4]; // ����������Ķ�̬Ч��
		IEffectSet m_willBeDeletedEffects;
		IEffectSet m_liveDurEffectSet;	//PlayEffect�����Effect��������
		std::set<ISprite*> m_willBeDeletedSprites;
		//ս��״̬
		std::list<Sprite*> m_warSprites;
		EffectList m_warEffects[3]; // ����������Ķ�̬Ч��
		IEffectSet m_warWillBeDeletedEffects;
		IEffectSet m_warLiveDurEffectSet;	//PlayEffect�����Effect��������
		std::set<ISprite*> m_warWillBeDeletedSprites;

		EffectList m_topSprEffect;

		IEffectSet m_loadingEffects;
		IEffectSet m_removedLoadingEffects;

		//����Ļ��棬�����Ȳ����ˣ��о�û��̫���Ҫ��
		//std::list<Sprite*> m_spritesCache;

		XPBGSoundMode m_bgsoundMode;
		std::set<XPEnvSoundArea*> m_activeEnvSoundArea;	//����������
		XPBkgSoundArea* m_pLastArea;	//����������
		std::set<IImmovableObj*> m_willBeDeletedImmovableObjs;
		std::map<unsigned __int64, SingleEffectNotify*> m_SignleEffectMap;
		std::wstring m_strMapName;
		std::wstring m_strPrefetchMapName;

		PictureHandle m_hNightEffectRenderTarget;	//��ҹ��Ч��ʱͼƬ
		PictureHandle m_waterAlphaMask;				//ˮ����Ч��alpha����

		int			  m_nAccDelta;					//Ϊ��ƽ���ƶ����飬�涨������Delta֮��Ĳ�ľ���ֵ�����ޣ�������ۻ�ֵ
		int			  m_nLastDelta;
		bool		  m_bIsANewDay;
		bool		  m_bHasCreateBGM;				//�Ƿ����ù�����������
		bool		  m_bReseted;
		bool		  m_bDeltaVetFull;				//m_vetDeltas����û��
		std::vector<int> m_vetDeltas;				//ÿ�ε�Delta������ƽ��ֵ��
		int			  m_totalDeltas;				//���n�ε��ۻ�Delta
		size_t		  m_nDeltaPoint;				//m_vetDeltas��β
		
		float		  m_fSpriteShadowShearX;		//����Ӱ�ӵķ������
		float		  m_fSpriteShadowScalingY;
		XPWorldMode   m_worldMode;

		DWORD m_nGameTime;	//��Ϸ��ʱ�䣬��0��XP_A_DAY_TIME(7200000;//1000 * 60 * 60 * 2;//����Сʱ)
		float m_nGameTimeDeltaMul;
		

		XPMazeColors m_mazeColors;

		SubMap::PPathMap *m_pPathMap;
		Map *m_pMap;
		Map *m_pPrefetchMap;
		Camera m_camera;
		//
		RenderTargetCachePool m_poolSurfaceRenderTargets;
		//
		RectTopologyList m_sortlist[XPSL_COUNT];
		std::vector<TopologySortBase> m_sortCache; // todo

		int m_nTotalLoadingCount;
		int m_nLoadingCount;
		CPOINT m_PrefetchPt;//Ԥ����Ŀ�����ĵ�
		bool m_bDrawWorld;//�Ƿ�������磨Ŀǰֻ�������첽���ع����б���Ϊfalse��
		bool m_bIsAutoSwitch;//���첽���ص�ͼ��ʱ�򣬼��������Ƿ��Զ��л�
		bool m_bIsAsync;//�Ƿ��첽�����أ�Ӧ���ڳ���ȡ��ͬ������֮��ɵ�
		bool m_bIsDrawInLoading;//�ڼ��ص�ʱ���Ƿ�Render
		//LoadMapEffectParm m_LoadMapEffectParam;

		//SortMap XXX
		//stdext::hash_map<SortBaseID,int> m_sortMap;
		std::map<SortBaseId,int> m_sortMap;

		PicResrcMap m_picResrcs;

		bool m_bIsInDestructor;

	public:
		std::set<PicResrc*> m_activePicResrcs;
		std::set<Effect*> m_activeEffects;

		std::set<PicResrc*> m_manualPrefetchPicResrcs;
		std::set<Effect*> m_manualPrefetchEffects;


	private:
		void LocalSort(std::vector<TopologySortBase> &v);
		//ȥ������Ч������������Ժ�blame��ʱ����Կ������ĸ��汾
		//void InitLoadmapJumpEffict(unsigned int transID);
		//void DrawLoadmapJumpEffect(int pro);
		void UpdateDistorts(int delta);
		void UpdateTrees(int delta);
		void SimpleUpdateTrees(int delta, int updatetime, bool randomlight);
		void NormalUpdateTrees(int delta, int updatetime, bool randomlight);
		void UpdateTriggers(int delta);
		void UpdateEffects(int delta);
		static int GetSurfaceCacheCountByView(const CRECT &viewrect);
		void DeleteSprite();
		void DeleteWarSprite();
		void DeleteEffect();
		void DeleteWarEffect();
		void DeleteImmovableObj();
		void UpdateBGMArea(const CPOINT &pt);
		void PrefetchMapNotify(int count);
		bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, int pro, bool async);	// ���ص�ͼ
		
	public:
		World(Engine *pEngine, int viewwidth, int viewheight);
		virtual ~World();

	public:
		void OnChangeBGSoundMode(XPBGSoundMode mode);
		void ResetBGSound();
		void ClearActiveEnvSoundArea() { m_activeEnvSoundArea.clear(); }
		const std::wstring& GetMapName() const { return m_strMapName; }
		unsigned int GetWaterDepth(const Location& pt) const;
		virtual bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, bool async)	// ���ص�ͼ
		{
			return LoadMap(mapname, mazename, param, 0, async);
		}
		virtual bool UnloadMap();				// ж�ص�ͼ

		bool IsMapLoaded() const { return m_pMap != NULL; }

		virtual bool GetMapSize(CPOINT &size) const;

		virtual bool SetMapMaze(const void* mazeBuffer, size_t size);//����ͨ��ͼ���ݣ��༭��ר�ã�
		virtual bool SetMazeMask(const unsigned char* pData, const CRECT &rect);//rect����ҿ��ϱ��¿�����������
		virtual unsigned int GetMazeMask(int x, int y);//�߼�����

		virtual void SetMazeColors(const XPMazeColors &colors) { m_mazeColors = colors; }
		virtual const XPMazeColors& GetMazeColors() const { return m_mazeColors; }

		virtual bool SetSoundAreas(XMLIO::CINode& root, PStepSoundMap* pSSM);	//����������ͼ���༭��ר�ã�

		virtual bool GetAStartPath(astar::Path& path, const Nuclear::Location& start, const Nuclear::Location& end); // ������ʼ����߼�����, ���ذ�����ʼ�㡢�յ��·��

		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const 
		{ 
			shearX = m_fSpriteShadowShearX; 
			scalingY = m_fSpriteShadowScalingY; 
		}

		int GetAllSpriteCount() const;
		void GetAllSpriteDetail(std::map<std::wstring, int> &detail) const;

		int GetAllEffectCount() const;
		void GetAllEffectDetail(std::map<std::wstring, int> &detail) const;

		// ͬ�����ص�ͼ��ʱ���Ƿ����
		void EnableRenderDuringLoading(bool b) { m_bIsDrawInLoading = b; }
		bool IsRenderDuringLoading() const { return m_bIsDrawInLoading; }

		virtual void SetSpriteShadowParam(const float &shearX, const float &scalingY)
		{ 
			m_fSpriteShadowShearX = shearX; 
			m_fSpriteShadowScalingY = scalingY; 
		}

		/* ���� */
		virtual void SetScale(float scale) 
		{ 
			m_camera.SetScale(scale); 
			m_poolSurfaceRenderTargets.SetSize(GetSurfaceCacheCountByView(m_camera.GetViewport()));
		}
		virtual float GetScale () const { return m_camera.GetScale(); }

		//�������ϲ���Ч
		void PushSpriteTopEffect(Effect *pEffect) { m_topSprEffect.push_back(pEffect); }

		/* ����ģʽ */
		virtual void SetWorldMode(XPWorldMode m) { m_worldMode = m; m_sortCache.clear();}
		virtual XPWorldMode GetWorldMode() const { return m_worldMode; }

		//Ӧ����pfs·��������ͼƬ��ʽ��dds��png֮��ġ����û�л�ͬ����ȡ��Դ
		virtual WarBackgroundHandle LoadWarBackgound( const std::wstring &name );
		virtual void FreeWarBackgroundHandle (WarBackgroundHandle handle);
		virtual bool SetWarBackground(WarBackgroundHandle handle);
		virtual void FreeAllWarBackground();

		/* ��ͨ״̬�ľ��� */
		virtual ISprite* NewSprite(const std::wstring &modelname);
		//virtual ISprite* CloneSprite(const ISprite* spr);
		virtual void DeleteSprite(ISprite* sprite);
		virtual void DeleteAllSprite(bool keepAttached = false);
		size_t GetSpriteCount() { return m_sprites.size(); }

		/* ս��״̬�ľ��� */
		virtual ISprite* NewWarSprite(const std::wstring &modelname);
		virtual void DeleteWarSprite(ISprite* sprite);
		virtual void DeleteAllWarSprite();
		size_t GetWarSpriteCount() { return m_warSprites.size(); }

		/* ���ڵ�ͼ�ϵ�������������Ʒ������.. */
		virtual IImmovableObj* NewImmovableObj(const std::wstring &objname, int layer/*Ŀǰ����Ϊ0��ֻ�еر���һ��*/, XPCOLOR color = 0xffffffff, float freq=1.0f);
		virtual void DeleteImmovableObj(IImmovableObj* immobj);
		virtual void DeleteAllImmovableObj();

		virtual bool SelectObjs(const Location &loc, std::vector<ISelectableObj*> &ettobjs);

		// ֱ���ڵ�ͼ�ϲ��ŵ� effect
		//������Ч��
		virtual IEffect* SetLinkedEffect(const std::wstring &name, XPEFFECT_LAYER layer, const CPOINT &pt1, const CPOINT &pt2, float time, bool async);
		virtual IEffect* SetEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async);
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async);
		virtual void RemoveEffect(IEffect* pEffect);

		//һ����Ч��, times�ǲ��Ŵ���, һ����������һ�����˺����߷�Χ�˺���
		virtual void PlayEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype)
		{
			PlayOnceEffect(name, layer, x, y, times, async, soundtype);
		}
		Effect* PlayOnceEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype);


		//ս����Ч������������
		//������Ч��
		virtual IEffect* SetWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async);
		virtual IEffect* SetWarContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async);
		virtual void RemoveWarEffect(IEffect* pEffect);

		//һ����Ч��, times�ǲ��Ŵ���, һ����������һ�����˺����߷�Χ�˺���
		virtual void PlayWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype)
		{
			PlayOnceWarEffect(name, layer, x, y, times, async, soundtype);
		}
		Effect* PlayOnceWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype);
		//ս����Ч����������������

		const std::list<Sprite*>& GetSprites() const { return m_sprites; }
		bool CheckMapGUID(unsigned int mapguid) const;

		virtual void   AttachCameraTo(ISprite* sprite);
		virtual void SetViewport(const Location &loc/* Ŀ���Ӵ����ģ�Ҳ����Ҫ�������ǵ���λ��*/, bool syncRsrc/* �Ƿ��ȼ�����Դ*/);
		virtual void SetViewportLT(int left, int top); // Ϊ�༭����ӣ����ﾫ���ʱ���ô˺����ƶ��ӿڣ�

		virtual bool PrefetchMapRes(const std::wstring &mapname, const std::wstring &mazename, const CPOINT &center, bool autoSwitch);
		virtual void CancelPrefetch();

		void OnRendererRestore();

		//XPLOADMAP_EFFECT GetLoadMapEffect() { return m_LoadMapEffectParam.loadingMapEffect; }
		bool NotifyLoadmapProgress(int pro);
		void NotifyALoadItemFin(unsigned char beginPro, unsigned char endPro);
		void AddTotalLoadingCount() { ++m_nTotalLoadingCount; }
		
		bool Init();
		void SetViewSize(int width, int height);
		virtual CRECT GetViewport();

		void OnTimer(int iPeriod);
		void UpdateBeforeRender(int delta);
		void Render(int now, bool UseCavas);
		void DrawInfo();

		void OnTick(int now, int delta);
		void RenderMaze();

		//������Ϸ�����ҹʱ�䣬[0~1)�����ȱ仯��0������ҹ12�㣬0.5��������12��
		virtual void SetGameTime(float time)
		{
			m_nGameTime = static_cast<DWORD>(XP_A_DAY_TIME * time);
		}

		virtual void SetGameTimeCycle(int time)
		{
			m_nGameTimeDeltaMul = XP_A_DAY_TIME / static_cast<float>(time);
		}

		inline int GetGameTime() const { return m_nGameTime; }
		virtual float GetTempGameTime() const { return m_nGameTime / static_cast<float>(XP_A_DAY_TIME); }

		bool GetLightPos(CPOINT &pos);

		virtual bool SetCameraUpdateType(XPCAMERA_UPDATE_TYPE type) { return m_camera.SetCameraUpdateType(type); }
		virtual XPCAMERA_UPDATE_TYPE GetCameraUpdateType() const { return m_camera.GetCameraUpdateType(); }
		virtual void OnPlayFrame(ISprite* pSprite, const std::wstring &actionName, int frame);
	};
}

#endif
