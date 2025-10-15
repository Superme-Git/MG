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
		ReadMapTask *m_pReadMapTask[XPRMTT_COUNT];//分别是地图，maze，声音的xml，脚步声音的ssm

		std::list<Sprite*> m_sprites[XPSL_COUNT];
		EffectList m_effects[XPEL_COUNT]; // 不依赖精灵的动态效果
		IEffectSet m_willBeDeletedEffects;
		IEffectSet m_liveDurEffectSet;	//PlayEffect里面的Effect不在里面
		std::map<ISprite*, int> m_willBeRemoveSprites;//value是状态，0代表删除，1代表移除
		std::map<ISprite*, NuclearSpriteLayer> m_willBeMoveLayer[XPEL_COUNT];//将要被移到别的层的精灵

		EffectList m_topSprEffect;
		EffectList m_bottomSprEffect;
		IEffectSet m_loadingEffects;
		IEffectSet m_removedLoadingEffects;

		NuclearBGSoundMode m_bgsoundMode;
		std::set<XPEnvSoundArea*> m_activeEnvSoundArea;	//环境音区域
		std::set<IImmovableObj*> m_willBeDeletedImmovableObjs;
		std::map<ULONG64, SingleEffectNotify*> m_SignleEffectMap;
		std::wstring m_strMapName;
		std::wstring m_strPrefetchMapName;

		PictureHandle m_hNightEffectRenderTarget;	//昼夜特效临时图片
		PictureHandle m_waterAlphaMask;				//水的特效的alpha遮罩

		int			  m_nAccDelta;					//为了平滑移动精灵，规定了两次Delta之间的差的绝对值的上限，这个是累积值
		int			  m_nLastDelta;
		bool		  m_bIsANewDay;
		bool		  m_bHasCreateBGM;				//是否设置过背景音乐了
		bool		  m_bReseted;
		bool		  m_bDeltaVetFull;				//m_vetDeltas填满没有
		std::vector<int> m_vetDeltas;				//每次的Delta，计算平均值用
		int			  m_totalDeltas;				//最近n次的累积Delta
		size_t		  m_nDeltaPoint;				//m_vetDeltas队尾
		
		float		  m_fSpriteShadowShearX;		//精灵影子的方向参数
		float		  m_fSpriteShadowScalingY;
		eNuclearWorldMode   m_worldMode;

		DWORD m_nGameTime;	//游戏的时间，从0到FIRE_A_DAY_TIME(7200000;//1000 * 60 * 60 * 2;//两个小时)
		float m_nGameTimeDeltaMul;
		XPMazeColors m_mazeColors;

		Nuclear::NuclearColor m_MaskBoxColor;//蒙板盒颜色

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
		NuclearPoint m_PrefetchPt;//预读的目标中心点
		bool m_bDrawWorld;//是否绘制世界（目前只可能在异步加载过程中被设为false）
		bool m_bIsAutoSwitch;//在异步加载地图的时候，加载完了是否自动切换
		bool m_bIsAsync;//是否异步步加载，应该在彻底取消同步加载之后干掉
		bool m_bIsDrawInLoading;//在加载的时候是否Render

		PicResrcMap m_picResrcs;

		bool m_bIsInDestructor;

		float	m_CurveMoveHeight;	//弧线移动的高度（单位像素）
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

		virtual bool SetMapMaze(const void* mazeBuffer, size_t size);//设置通过图数据（编辑器专用）
		virtual bool SetMazeMask(const unsigned char* pData, const NuclearRect &rect);//rect左闭右开上闭下开，格子坐标
		virtual unsigned int GetMazeMask(int x, int y);//逻辑坐标

		virtual void SetMazeColors(const XPMazeColors &colors) { m_mazeColors = colors; }
		virtual const XPMazeColors& GetMazeColors() const { return m_mazeColors; }

		virtual void SetMaskBoxColor(const NuclearColor &color = 0xffffffff) { m_MaskBoxColor = color; }
		virtual const NuclearColor& GetMaskBoxColor() const { return m_MaskBoxColor; }

		virtual bool GetAStartPath(astar::Path& path, const Nuclear::NuclearLocation& start, const Nuclear::NuclearLocation& end); // 输入起始点的逻辑坐标, 返回包含起始点、终点的路径

		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const;

		virtual int GetSortTick() const { return 0; }

		int GetAllSpriteCount() const;
		void GetAllSpriteDetail(std::map<std::wstring, int> &detail) const;

		int GetAllEffectCount() const;
		void GetAllEffectDetail(std::map<std::wstring, int> &detail) const;

		// 同步加载地图的时候是否绘制
		void EnableRenderDuringLoading(bool b) { m_bIsDrawInLoading = b; }
		bool IsRenderDuringLoading() const { return m_bIsDrawInLoading; }

		virtual void SetSpriteShadowParam(const float &shearX, const float &scalingY);
	private:
		int m_iFlyOffsetYCur;//因飞行而引起的Y轴偏移(实际上是模拟Z轴)当前值
		int m_iFlyOffsetYTgt;//因飞行而引起的Y轴偏移(实际上是模拟Z轴)目标值
		int m_iFlyOffsetYStep;//因飞行而引起的Y轴偏移(实际上是模拟Z轴)步伐值

	public:
		virtual void SetFlyOffsetYTgt(int iFlyOffsetYTgt);
		virtual void SetFlyOffsetYCur(int iFlyOffsetYCur);
		virtual int GetFlyOffsetYCur();
		virtual void SetFlyOffsetYStep(int iFlyOffsetYStep);
		virtual int GetFlyOffsetYStep();
		
		virtual void SetScale(float scale);
		virtual float GetScale () const { return m_camera.GetScale(); }

		virtual void SetUseSmooth(bool bSmooth);

		//精灵最上层特效
		void PushSpriteTopEffect(Effect *pEffect){m_topSprEffect.push_back(pEffect);}

		//精灵最下层特效
		void PushSpriteBottomEffect(Effect *pEffect){m_bottomSprEffect.push_back(pEffect);}

		/* 世界模式 */
		virtual void SetWorldMode(eNuclearWorldMode m);
		virtual eNuclearWorldMode GetWorldMode() const { return m_worldMode; }

		virtual WarBackgroundHandle LoadWarBackgound( const std::wstring &name );
		virtual void FreeWarBackgroundHandle (WarBackgroundHandle handle);
		virtual bool SetWarBackground(WarBackgroundHandle handle, NuclearWarBackgroundType type);
		virtual void FreeAllWarBackground();
		
		virtual void SetWarBackgroundEdge(float fEdge) { m_fBattleBackgroundEdge = fEdge; }//这个只有在XPWBT_SCALE的情况下才生效
		virtual float GetWarBackgroundEdge() const { return m_fBattleBackgroundEdge; }

		/* 精灵 */
		virtual ISprite* NewSprite(NuclearSpriteLayer layer, const std::wstring &modelname);
		virtual bool AttachSprite(ISprite *pHostSprite, ISprite *pClientSprite, const std::wstring &hostSocket, const std::wstring &clientSocket, const NuclearVector3 &relpos, unsigned int sign);
		virtual bool DetachSprite(NuclearSpriteLayer layer, ISprite *pHostSprite, ISprite *pClientSprite);
		virtual void DeleteSprite(ISprite* sprite);
		virtual void DeleteAllSprite(NuclearSpriteLayer layer, bool keepAttached = false);
		virtual void MoveSpriteLayer(ISprite *pSprite, NuclearSpriteLayer fromlayer, NuclearSpriteLayer tolayer);

		/* 扔在地图上的物件，如掉落物品、矿物.. */
		virtual IImmovableObj* NewImmovableObj(const std::wstring &objname, int layer/*目前必须为0，只有地表面一层*/, NuclearColor color = 0xffffffff, float freq=1.0f);
		virtual void DeleteImmovableObj(IImmovableObj* immobj);
		virtual void DeleteAllImmovableObj();

		virtual bool SelectObjs(const NuclearLocation &loc, std::vector<ISelectableObj*> &ettobjs);

		// 直接在地图上播放的 effect持续的效果
		virtual IEffect* SetLinkedEffect(const std::wstring &name, Nuclear_EffectLayer layer, const NuclearPoint &pt1, const NuclearPoint &pt2, float time, bool async);
		virtual IEffect* SetEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, bool async);
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, Nuclear_EffectLayer layer, int x, int y, bool async);
		virtual void RemoveEffect(IEffect* pEffect);
		virtual void RemoveEffectEx(IEffect* pEffect);

		//一次性效果, times是播放次数, 一般用来表现一次性伤害或者范围伤害。
		virtual IEffect* PlayEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, int times, bool async, unsigned char soundtype);
		Effect* PlayOnceEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, int times, bool async, unsigned char soundtype);

		const std::list<Sprite*>& GetSprites(NuclearSpriteLayer layer) const { return m_sprites[layer]; }
		bool CheckMapGUID(unsigned int mapguid) const;

		void pauseSceneEffects();
		void resumeSceneEffects();
		virtual void showSceneEffects(bool bshow){ m_bShowEffect = bshow; };
		bool isShowSceneEffects(){ return m_bShowEffect; };

		virtual void AttachCameraTo(ISprite* sprite);
		virtual void SetViewport(const NuclearLocation &loc/* 目的视窗中心，也就是要传送主角到的位置*/, bool syncRsrc/* 是否先加载资源*/);
		virtual void SetViewportLT(int left, int top); // 为编辑器添加，不帮精灵的时候用此函数移动视口；

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
		//下面两个layerid不是同一套体系下的
		void RenderStaticLayer(int layer, int drawTick, bool realRender);
		void RenderSortedLayer(int layer, Canvas *pCanvas, bool realRender);
		//物件和精灵都不透明
		void RenderSortedLayerWithoutAlpha(int layer, Canvas *pCanvas, bool realRender);
		//只绘制物件（包括长得像物件的精灵）
		void RenderSortedLayerElementWithoutSprite(int layer, Canvas *pCanvas, bool realRender);
		//只绘制精灵（不包括长的像物件的精灵）
		void RenderSortedLayerAlphaSpriteWithoutElement(int layer, Canvas *pCanvas, bool realRender);
		//绘制精灵，以及半透明的物件（包括半透明的长得像物件的精灵）
		void RenderSortedLayerNonAlphaSpriteWithAlphaElement(int layer, Canvas *pCanvas, bool realRender);
		void RenderMovingBackground();//移动背景
		void RenderRenderTargetCache();
		inline void RenderDynamicEffect(Nuclear_EffectLayer layer, bool realRender);
		void SetNightEffect(bool bIsInBattle);

		void OnTick(unsigned int now, unsigned int delta, unsigned int realDelta);
		void UpdateSpriteAction(DWORD delta);
		void RenderMaze();
		void RenderMaskBox();//绘制一个颜色蒙板盒

		//设置游戏白天黑夜时间，[0~1)，均匀变化，0代表深夜12点，0.5代表中午12点
		virtual void SetGameTime(float time){m_nGameTime = static_cast<DWORD>(FIRE_A_DAY_TIME * time);}
		virtual void SetGameTimeCycle(int time){m_nGameTimeDeltaMul = FIRE_A_DAY_TIME / static_cast<float>(time);}

		int GetGameTime() const;
		virtual float GetTempGameTime() const { return m_nGameTime / static_cast<float>(FIRE_A_DAY_TIME); }

		virtual bool SetCameraUpdateType(NuclearCameraUpdateType type, void* pParams = NULL) { return m_camera.SetCameraUpdateType(type, pParams); }
		virtual NuclearCameraUpdateType GetCameraUpdateType() const { return m_camera.GetCameraUpdateType(); }
		virtual void OnPlayFrame(ISprite* pSprite, const std::wstring &actionName, int frame){}

		/* 振屏控制器，当设了这个之后，每帧调一次GetShakeScreenAdjust，获取本帧的viewport偏移值 */
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
