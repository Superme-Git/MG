#ifndef __Nuclear_WORLD_H__
#define __Nuclear_WORLD_H__

#include "../engine/nurenderable.h"
#include "../engine/nucamera.h"
#include "../engine/nurendertargetcache.h"
#include "../map/nupmap.h"
#include "../nuiengine.h"
#include "../nuiworld.h"
#include "../nuisprite.h"
#include "nuimmovableobj.h"
#include "nupicresrc.h"
#include "nuport.h"
#include <map>
#include "../soundarea/xpenvsoundarea.h"

#define XPDELTA_COUNT 200
namespace Nuclear
{
	class Effect;
	class ISelectableObj;
	class Engine;
	class ReadMapTask;
	class Map;
    class SingleEffectNotify;
    
	namespace Region
	{
		struct sTriggerObjInfo;
	}
	namespace SubMap
	{
		class PPathMap;
	}

	union TriggerKey
	{
		struct
		{
			int infoVetID;
			Region::sTriggerObjInfo* pTriggerObjInfo;
		};
		ULONG64 id;
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
		friend class Engine;

		Engine *m_pEngine;
		Canvas m_canvas;
		std::list<ImmovableObj*> m_immobjs;

		NuclearWarBackgroundType m_warBackGroundType;
		WarBackgroundHandleMap m_wbhm;
		WarBackgroundMap m_wbm;
		WarBackgroundHandle m_wbhSeed;
		PictureHandle m_warBackGroundPic;
		float m_fBattleBackgroundEdge;

		PictureHandle m_maskPicHandle;
		std::wstring m_maskPicUri;
		int	m_nWarBackGroundPicWidth;
		int	m_nWarBackGroundPicHeight;
		ReadMapTask *m_pReadMapTask[XPRMTT_COUNT];//�ֱ��ǵ�ͼ��maze��������xml���Ų�������ssm

		std::list<Sprite*> m_sprites[XPSL_COUNT];
		EffectList m_effects[XPEL_COUNT]; // ����������Ķ�̬Ч��
		IEffectSet m_willBeDeletedEffects;
		IEffectSet m_liveDurEffectSet;	//PlayEffect�����Effect��������
		std::map<ISprite*, int> m_willBeRemoveSprites;//value��״̬��0����ɾ����1�����Ƴ�
		std::map<ISprite*, NuclearSpriteLayer> m_willBeMoveLayer[XPEL_COUNT];//��Ҫ���Ƶ���Ĳ�ľ���

		EffectList m_topSprEffect;
		EffectList m_bottomSprEffect;
		IEffectSet m_loadingEffects;
		IEffectSet m_removedLoadingEffects;

		NuclearBGSoundMode m_bgsoundMode;
		std::set<XPEnvSoundArea*> m_activeEnvSoundArea;	//����������
		std::set<IImmovableObj*> m_willBeDeletedImmovableObjs;
		std::map<ULONG64, SingleEffectNotify*> m_SignleEffectMap;
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
		eNuclearWorldMode   m_worldMode;

		DWORD m_nGameTime;	//��Ϸ��ʱ�䣬��0��FIRE_A_DAY_TIME(7200000;//1000 * 60 * 60 * 2;//����Сʱ)
		float m_nGameTimeDeltaMul;
		XPMazeColors m_mazeColors;

		Nuclear::NuclearColor m_MaskBoxColor;//�ɰ����ɫ

		SubMap::PPathMap *m_pPathMap;
		Map *m_pMap;
		Map *m_pPrefetchMap;
		Camera m_camera;
		
		RenderTargetCachePool m_poolSurfaceRenderTargets;
		
		Rect2PologyList m_sortlist[XPSL_COUNT];
		std::vector<TopologySortBase> m_sortCache[XPSL_COUNT];
		std::vector<VPic> m_vpiclist[XPSL_COUNT];

		int m_nTotalLoadingCount;
		int m_nLoadingCount;
		NuclearPoint m_PrefetchPt;//Ԥ����Ŀ�����ĵ�
		bool m_bDrawWorld;//�Ƿ�������磨Ŀǰֻ�������첽���ع����б���Ϊfalse��
		bool m_bIsAutoSwitch;//���첽���ص�ͼ��ʱ�򣬼��������Ƿ��Զ��л�
		bool m_bIsAsync;//�Ƿ��첽�����أ�Ӧ���ڳ���ȡ��ͬ������֮��ɵ�
		bool m_bIsDrawInLoading;//�ڼ��ص�ʱ���Ƿ�Render

		PicResrcMap m_picResrcs;

		bool m_bIsInDestructor;

		float	m_CurveMoveHeight;	//�����ƶ��ĸ߶ȣ���λ���أ�
		bool	m_bShowEffect;
	public:
		std::set<PicResrc*> m_activePicResrcs;
		std::set<Effect*> m_activeEffects;

		std::set<PicResrc*> m_manualPrefetchPicResrcs;
		std::set<Effect*> m_manualPrefetchEffects;
		
	public:
		World(Engine *pEngine, int viewwidth, int viewheight);
		virtual ~World();

		void OnChangeBGSoundMode(NuclearBGSoundMode mode);
		void ResetBGSound();
		void ClearActiveEnvSoundArea() { m_activeEnvSoundArea.clear(); }
		const std::wstring& GetMapName() const { return m_strMapName; }
		bool GetWaterDepth(const NuclearLocation& pt, unsigned int &nDepth) const;
		virtual bool LoadMap(const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, bool async);
		virtual bool UnloadMap();
		virtual void ImmediatelyAssureRegionResource(const Nuclear::NuclearLocation &loc);

		bool IsMapLoaded()  const { return m_pMap != NULL; }
		bool IsMapFull() const;

		virtual bool GetMapSize(NuclearPoint &size) const;

		virtual bool SetMapMaze(const void* mazeBuffer, size_t size);//����ͨ��ͼ���ݣ��༭��ר�ã�
		virtual bool SetMazeMask(const unsigned char* pData, const NuclearRect &rect);//rect����ҿ��ϱ��¿�����������
		virtual unsigned int GetMazeMask(int x, int y);//�߼�����

		virtual void SetMazeColors(const XPMazeColors &colors) { m_mazeColors = colors; }
		virtual const XPMazeColors& GetMazeColors() const { return m_mazeColors; }

		virtual void SetMaskBoxColor(const NuclearColor &color = 0xffffffff) { m_MaskBoxColor = color; }
		virtual const NuclearColor& GetMaskBoxColor() const { return m_MaskBoxColor; }

		virtual bool GetAStartPath(astar::Path& path, const Nuclear::NuclearLocation& start, const Nuclear::NuclearLocation& end); // ������ʼ����߼�����, ���ذ�����ʼ�㡢�յ��·��

		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const;

		virtual int GetSortTick() const { return 0; }

		int GetAllSpriteCount() const;
		void GetAllSpriteDetail(std::map<std::wstring, int> &detail) const;

		int GetAllEffectCount() const;
		void GetAllEffectDetail(std::map<std::wstring, int> &detail) const;

		// ͬ�����ص�ͼ��ʱ���Ƿ����
		void EnableRenderDuringLoading(bool b) { m_bIsDrawInLoading = b; }
		bool IsRenderDuringLoading() const { return m_bIsDrawInLoading; }

		virtual void SetSpriteShadowParam(const float &shearX, const float &scalingY);
	private:
		int m_iFlyOffsetYCur;//����ж������Y��ƫ��(ʵ������ģ��Z��)��ǰֵ
		int m_iFlyOffsetYTgt;//����ж������Y��ƫ��(ʵ������ģ��Z��)Ŀ��ֵ
		int m_iFlyOffsetYStep;//����ж������Y��ƫ��(ʵ������ģ��Z��)����ֵ

	public:
		virtual void SetFlyOffsetYTgt(int iFlyOffsetYTgt);
		virtual void SetFlyOffsetYCur(int iFlyOffsetYCur);
		virtual int GetFlyOffsetYCur();
		virtual void SetFlyOffsetYStep(int iFlyOffsetYStep);
		virtual int GetFlyOffsetYStep();
		
		virtual void SetScale(float scale);
		virtual float GetScale () const { return m_camera.GetScale(); }

		virtual void SetUseSmooth(bool bSmooth);

		//�������ϲ���Ч
		void PushSpriteTopEffect(Effect *pEffect){m_topSprEffect.push_back(pEffect);}

		//�������²���Ч
		void PushSpriteBottomEffect(Effect *pEffect){m_bottomSprEffect.push_back(pEffect);}

		/* ����ģʽ */
		virtual void SetWorldMode(eNuclearWorldMode m);
		virtual eNuclearWorldMode GetWorldMode() const { return m_worldMode; }

		virtual WarBackgroundHandle LoadWarBackgound( const std::wstring &name );
		virtual void FreeWarBackgroundHandle (WarBackgroundHandle handle);
		virtual bool SetWarBackground(WarBackgroundHandle handle, NuclearWarBackgroundType type);
		virtual void FreeAllWarBackground();
		
		virtual void SetWarBackgroundEdge(float fEdge) { m_fBattleBackgroundEdge = fEdge; }//���ֻ����XPWBT_SCALE������²���Ч
		virtual float GetWarBackgroundEdge() const { return m_fBattleBackgroundEdge; }

		/* ���� */
		virtual ISprite* NewSprite(NuclearSpriteLayer layer, const std::wstring &modelname);
		virtual bool AttachSprite(ISprite *pHostSprite, ISprite *pClientSprite, const std::wstring &hostSocket, const std::wstring &clientSocket, const NuclearVector3 &relpos, unsigned int sign);
		virtual bool DetachSprite(NuclearSpriteLayer layer, ISprite *pHostSprite, ISprite *pClientSprite);
		virtual void DeleteSprite(ISprite* sprite);
		virtual void DeleteAllSprite(NuclearSpriteLayer layer, bool keepAttached = false);
		virtual void MoveSpriteLayer(ISprite *pSprite, NuclearSpriteLayer fromlayer, NuclearSpriteLayer tolayer);

		/* ���ڵ�ͼ�ϵ�������������Ʒ������.. */
		virtual IImmovableObj* NewImmovableObj(const std::wstring &objname, int layer/*Ŀǰ����Ϊ0��ֻ�еر���һ��*/, NuclearColor color = 0xffffffff, float freq=1.0f);
		virtual void DeleteImmovableObj(IImmovableObj* immobj);
		virtual void DeleteAllImmovableObj();

		virtual bool SelectObjs(const NuclearLocation &loc, std::vector<ISelectableObj*> &ettobjs);

		// ֱ���ڵ�ͼ�ϲ��ŵ� effect������Ч��
		virtual IEffect* SetLinkedEffect(const std::wstring &name, Nuclear_EffectLayer layer, const NuclearPoint &pt1, const NuclearPoint &pt2, float time, bool async);
		virtual IEffect* SetEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, bool async);
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, Nuclear_EffectLayer layer, int x, int y, bool async);
		virtual void RemoveEffect(IEffect* pEffect);
		virtual void RemoveEffectEx(IEffect* pEffect);

		//һ����Ч��, times�ǲ��Ŵ���, һ����������һ�����˺����߷�Χ�˺���
		virtual IEffect* PlayEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, int times, bool async, unsigned char soundtype);
		Effect* PlayOnceEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, int times, bool async, unsigned char soundtype);

		const std::list<Sprite*>& GetSprites(NuclearSpriteLayer layer) const { return m_sprites[layer]; }
		bool CheckMapGUID(unsigned int mapguid) const;

		void pauseSceneEffects();
		void resumeSceneEffects();
		virtual void showSceneEffects(bool bshow){ m_bShowEffect = bshow; };
		bool isShowSceneEffects(){ return m_bShowEffect; };

		virtual void AttachCameraTo(ISprite* sprite);
		virtual void SetViewport(const NuclearLocation &loc/* Ŀ���Ӵ����ģ�Ҳ����Ҫ�������ǵ���λ��*/, bool syncRsrc/* �Ƿ��ȼ�����Դ*/);
		virtual void SetViewportLT(int left, int top); // Ϊ�༭����ӣ����ﾫ���ʱ���ô˺����ƶ��ӿڣ�

		virtual bool PrefetchMapRes(const std::wstring &mapname, const std::wstring &mazename, const NuclearPoint &center, bool autoSwitch);
		virtual void CancelPrefetch();

		void OnRendererRestore();
		void FreeAllPicReses();

		bool NotifyLoadmapProgress(int pro);
		void NotifyALoadItemFin(unsigned char beginPro, unsigned char endPro);
		void AddTotalLoadingCount() { ++m_nTotalLoadingCount; }
		
		bool Init();
		void SetViewSize(int width, int height);
		virtual const NuclearRect& GetViewport() const;

		void OnTimer(int iPeriod);
		void UpdateBeforeRender(int delta);
		void SortMid();
		void Render(int now, bool UseCavas, bool realRender);
        void RenderCaptureWorld();
		//��������layerid����ͬһ����ϵ�µ�
		void RenderStaticLayer(int layer, int drawTick, bool realRender);
		void RenderSortedLayer(int layer, Canvas *pCanvas, bool realRender);
		//����;��鶼��͸��
		void RenderSortedLayerWithoutAlpha(int layer, Canvas *pCanvas, bool realRender);
		//ֻ�����������������������ľ��飩
		void RenderSortedLayerElementWithoutSprite(int layer, Canvas *pCanvas, bool realRender);
		//ֻ���ƾ��飨����������������ľ��飩
		void RenderSortedLayerAlphaSpriteWithoutElement(int layer, Canvas *pCanvas, bool realRender);
		//���ƾ��飬�Լ���͸���������������͸���ĳ���������ľ��飩
		void RenderSortedLayerNonAlphaSpriteWithAlphaElement(int layer, Canvas *pCanvas, bool realRender);
		void RenderMovingBackground();//�ƶ�����
		void RenderRenderTargetCache();
		inline void RenderDynamicEffect(Nuclear_EffectLayer layer, bool realRender);
		void SetNightEffect(bool bIsInBattle);

		void OnTick(unsigned int now, unsigned int delta, unsigned int realDelta);
		void UpdateSpriteAction(DWORD delta);
		void RenderMaze();
		void RenderMaskBox();//����һ����ɫ�ɰ��

		//������Ϸ�����ҹʱ�䣬[0~1)�����ȱ仯��0������ҹ12�㣬0.5��������12��
		virtual void SetGameTime(float time){m_nGameTime = static_cast<DWORD>(FIRE_A_DAY_TIME * time);}
		virtual void SetGameTimeCycle(int time){m_nGameTimeDeltaMul = FIRE_A_DAY_TIME / static_cast<float>(time);}

		int GetGameTime() const;
		virtual float GetTempGameTime() const { return m_nGameTime / static_cast<float>(FIRE_A_DAY_TIME); }

		virtual bool SetCameraUpdateType(NuclearCameraUpdateType type, void* pParams = NULL) { return m_camera.SetCameraUpdateType(type, pParams); }
		virtual NuclearCameraUpdateType GetCameraUpdateType() const { return m_camera.GetCameraUpdateType(); }
		virtual void OnPlayFrame(ISprite* pSprite, const std::wstring &actionName, int frame){}

		/* ���������������������֮��ÿ֡��һ��GetShakeScreenAdjust����ȡ��֡��viewportƫ��ֵ */
		virtual void SetShakeScreenController(ShakeScreenController *pController) { m_camera.SetShakeScreenController(pController); }
		virtual ShakeScreenController* GetShakeScreenController() const { return m_camera.GetShakeScreenController(); }

		void  SetCurveMoveHeight(float height)   { m_CurveMoveHeight = height; }
		float GetCurveMoveHeight() const  { return m_CurveMoveHeight; }

	private:
		void LocalSort(std::vector<TopologySortBase> &v, int l);
		void UpdateWaterRenderArea();
		void UpdateSurfaceCacheLayersWhenNotUseSurfaceCache(bool &picloading);
		void UpdateStaticLayers(int startlayer, bool &picloading);
		void UpdateDynamicLayers(bool &picloading);
		void UpdateDistorts(int delta);
		void UpdateTrees(int delta);
		void SimpleUpdateTrees(int delta, int updatetime, bool randomlight);
		void NormalUpdateTrees(int delta, int updatetime, bool randomlight);
		void UpdateTriggers(int delta);
		void UpdateEffects(int delta);

		static int GetSurfaceCacheCountByView(const NuclearRect &viewrect);
		void DeleteSprite();
		void DeleteEffect();
		void DeleteImmovableObj();
		void UpdateBGMArea(const NuclearPoint &pt);
		void PrefetchMapNotify(int count);
		bool LoadMap(const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, int pro, bool async);

	};
}

#endif
