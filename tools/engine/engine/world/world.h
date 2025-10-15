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
		XPSL_MIDDLE = 0, //中层精灵
		XPSL_BATTLE, //战斗精灵
		XPSL_FLY,	//飞行精灵
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
		//	//以下内容可能会因effect的种类增加而变成联合体
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
		ReadMapTask *m_pReadMapTask[XPRMTT_COUNT];//分别是地图，maze，声音的xml，脚步声音的ssm

		//普通状态
		std::list<Sprite*> m_sprites;
		EffectList m_effects[4]; // 不依赖精灵的动态效果
		IEffectSet m_willBeDeletedEffects;
		IEffectSet m_liveDurEffectSet;	//PlayEffect里面的Effect不在里面
		std::set<ISprite*> m_willBeDeletedSprites;
		//战争状态
		std::list<Sprite*> m_warSprites;
		EffectList m_warEffects[3]; // 不依赖精灵的动态效果
		IEffectSet m_warWillBeDeletedEffects;
		IEffectSet m_warLiveDurEffectSet;	//PlayEffect里面的Effect不在里面
		std::set<ISprite*> m_warWillBeDeletedSprites;

		EffectList m_topSprEffect;

		IEffectSet m_loadingEffects;
		IEffectSet m_removedLoadingEffects;

		//精灵的缓存，还是先不做了，感觉没有太大必要啊
		//std::list<Sprite*> m_spritesCache;

		XPBGSoundMode m_bgsoundMode;
		std::set<XPEnvSoundArea*> m_activeEnvSoundArea;	//环境音区域
		XPBkgSoundArea* m_pLastArea;	//背景音区域
		std::set<IImmovableObj*> m_willBeDeletedImmovableObjs;
		std::map<unsigned __int64, SingleEffectNotify*> m_SignleEffectMap;
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
		XPWorldMode   m_worldMode;

		DWORD m_nGameTime;	//游戏的时间，从0到XP_A_DAY_TIME(7200000;//1000 * 60 * 60 * 2;//两个小时)
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
		CPOINT m_PrefetchPt;//预读的目标中心点
		bool m_bDrawWorld;//是否绘制世界（目前只可能在异步加载过程中被设为false）
		bool m_bIsAutoSwitch;//在异步加载地图的时候，加载完了是否自动切换
		bool m_bIsAsync;//是否异步步加载，应该在彻底取消同步加载之后干掉
		bool m_bIsDrawInLoading;//在加载的时候是否Render
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
		//去掉传送效果，留着这个以后blame的时候可以看到是哪个版本
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
		bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, int pro, bool async);	// 加载地图
		
	public:
		World(Engine *pEngine, int viewwidth, int viewheight);
		virtual ~World();

	public:
		void OnChangeBGSoundMode(XPBGSoundMode mode);
		void ResetBGSound();
		void ClearActiveEnvSoundArea() { m_activeEnvSoundArea.clear(); }
		const std::wstring& GetMapName() const { return m_strMapName; }
		unsigned int GetWaterDepth(const Location& pt) const;
		virtual bool LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, bool async)	// 加载地图
		{
			return LoadMap(mapname, mazename, param, 0, async);
		}
		virtual bool UnloadMap();				// 卸载地图

		bool IsMapLoaded() const { return m_pMap != NULL; }

		virtual bool GetMapSize(CPOINT &size) const;

		virtual bool SetMapMaze(const void* mazeBuffer, size_t size);//设置通过图数据（编辑器专用）
		virtual bool SetMazeMask(const unsigned char* pData, const CRECT &rect);//rect左闭右开上闭下开，格子坐标
		virtual unsigned int GetMazeMask(int x, int y);//逻辑坐标

		virtual void SetMazeColors(const XPMazeColors &colors) { m_mazeColors = colors; }
		virtual const XPMazeColors& GetMazeColors() const { return m_mazeColors; }

		virtual bool SetSoundAreas(XMLIO::CINode& root, PStepSoundMap* pSSM);	//设置声音地图（编辑器专用）

		virtual bool GetAStartPath(astar::Path& path, const Nuclear::Location& start, const Nuclear::Location& end); // 输入起始点的逻辑坐标, 返回包含起始点、终点的路径

		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const 
		{ 
			shearX = m_fSpriteShadowShearX; 
			scalingY = m_fSpriteShadowScalingY; 
		}

		int GetAllSpriteCount() const;
		void GetAllSpriteDetail(std::map<std::wstring, int> &detail) const;

		int GetAllEffectCount() const;
		void GetAllEffectDetail(std::map<std::wstring, int> &detail) const;

		// 同步加载地图的时候是否绘制
		void EnableRenderDuringLoading(bool b) { m_bIsDrawInLoading = b; }
		bool IsRenderDuringLoading() const { return m_bIsDrawInLoading; }

		virtual void SetSpriteShadowParam(const float &shearX, const float &scalingY)
		{ 
			m_fSpriteShadowShearX = shearX; 
			m_fSpriteShadowScalingY = scalingY; 
		}

		/* 飞行 */
		virtual void SetScale(float scale) 
		{ 
			m_camera.SetScale(scale); 
			m_poolSurfaceRenderTargets.SetSize(GetSurfaceCacheCountByView(m_camera.GetViewport()));
		}
		virtual float GetScale () const { return m_camera.GetScale(); }

		//精灵最上层特效
		void PushSpriteTopEffect(Effect *pEffect) { m_topSprEffect.push_back(pEffect); }

		/* 世界模式 */
		virtual void SetWorldMode(XPWorldMode m) { m_worldMode = m; m_sortCache.clear();}
		virtual XPWorldMode GetWorldMode() const { return m_worldMode; }

		//应该是pfs路径，基本图片格式，dds、png之类的。如果没有会同步读取资源
		virtual WarBackgroundHandle LoadWarBackgound( const std::wstring &name );
		virtual void FreeWarBackgroundHandle (WarBackgroundHandle handle);
		virtual bool SetWarBackground(WarBackgroundHandle handle);
		virtual void FreeAllWarBackground();

		/* 普通状态的精灵 */
		virtual ISprite* NewSprite(const std::wstring &modelname);
		//virtual ISprite* CloneSprite(const ISprite* spr);
		virtual void DeleteSprite(ISprite* sprite);
		virtual void DeleteAllSprite(bool keepAttached = false);
		size_t GetSpriteCount() { return m_sprites.size(); }

		/* 战斗状态的精灵 */
		virtual ISprite* NewWarSprite(const std::wstring &modelname);
		virtual void DeleteWarSprite(ISprite* sprite);
		virtual void DeleteAllWarSprite();
		size_t GetWarSpriteCount() { return m_warSprites.size(); }

		/* 扔在地图上的物件，如掉落物品、矿物.. */
		virtual IImmovableObj* NewImmovableObj(const std::wstring &objname, int layer/*目前必须为0，只有地表面一层*/, XPCOLOR color = 0xffffffff, float freq=1.0f);
		virtual void DeleteImmovableObj(IImmovableObj* immobj);
		virtual void DeleteAllImmovableObj();

		virtual bool SelectObjs(const Location &loc, std::vector<ISelectableObj*> &ettobjs);

		// 直接在地图上播放的 effect
		//持续的效果
		virtual IEffect* SetLinkedEffect(const std::wstring &name, XPEFFECT_LAYER layer, const CPOINT &pt1, const CPOINT &pt2, float time, bool async);
		virtual IEffect* SetEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async);
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async);
		virtual void RemoveEffect(IEffect* pEffect);

		//一次性效果, times是播放次数, 一般用来表现一次性伤害或者范围伤害。
		virtual void PlayEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype)
		{
			PlayOnceEffect(name, layer, x, y, times, async, soundtype);
		}
		Effect* PlayOnceEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype);


		//战斗的效果。。。。。
		//持续的效果
		virtual IEffect* SetWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async);
		virtual IEffect* SetWarContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async);
		virtual void RemoveWarEffect(IEffect* pEffect);

		//一次性效果, times是播放次数, 一般用来表现一次性伤害或者范围伤害。
		virtual void PlayWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype)
		{
			PlayOnceWarEffect(name, layer, x, y, times, async, soundtype);
		}
		Effect* PlayOnceWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype);
		//战斗的效果结束。。。。。

		const std::list<Sprite*>& GetSprites() const { return m_sprites; }
		bool CheckMapGUID(unsigned int mapguid) const;

		virtual void   AttachCameraTo(ISprite* sprite);
		virtual void SetViewport(const Location &loc/* 目的视窗中心，也就是要传送主角到的位置*/, bool syncRsrc/* 是否先加载资源*/);
		virtual void SetViewportLT(int left, int top); // 为编辑器添加，不帮精灵的时候用此函数移动视口；

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

		//设置游戏白天黑夜时间，[0~1)，均匀变化，0代表深夜12点，0.5代表中午12点
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
