#include "stdafx.h"
#include <hash_set>
#include "..\common\xptypes.h"
#include "..\common\log.h"
#include "..\common\recttopologylist.h"
#include "..\common\mapconst.h"
#include "..\common\xptimeuselog.h"
#include "..\particlesystem\iparticlemanager.h"
#include "..\effect\effectmanager.h"
#include "..\effect\particleeffect.h"
#include "..\map\ppathmap.h"
#include "..\map\pstepsoundmap.h"
#include "..\sprite\spritemanager.h"
#include "..\sprite\componentsprite.h"
#include "..\sound\directsoundmanager.h"
#include "..\soundarea\xpbkgsoundarea.h"
#include "..\soundarea\xpbkgvolarea.h"
#include "..\soundarea\xpenvsoundarea.h"
#include "..\soundarea\stepsoundtype.h"
#include "..\engine\configmanager.h"
#include "..\engine\engine.h"
#include "lkotree.h"
#include "distort.h"
#include "map.h"
#include "world.h"

class Perf	//先放在这里吧。。。
{
	LARGE_INTEGER s;
	LARGE_INTEGER total;
	int count;
public:	
	Perf()
	{
		total.QuadPart = 0LL;
		count = 0;
	}

	void start()
	{
		QueryPerformanceCounter(&s);
	}

	void stop()
	{
		LARGE_INTEGER e;
		QueryPerformanceCounter(&e);
		total.QuadPart += e.QuadPart - s.QuadPart;
		count++;
	}

	int getCount() const
	{
		return count;
	}

	LARGE_INTEGER getTotal() const
	{
		return total;
	}
};

namespace Nuclear
{
	extern XPTimeUseLog g_timeuserlog;
	class SingleEffectNotify : public IEffectNotify
	{
	public:
		TriggerKey triggerKey;
		static World* pWorld;
		virtual void OnStop(IEffect *pEffect) override
		{
			pWorld->m_SignleEffectMap.erase(triggerKey.id);
			((Effect*)pEffect)->RemoveNotify(this);
			delete this;
		}
		virtual void OnDelete(IEffect *pEffect) override
		{
			pWorld->m_SignleEffectMap.erase(triggerKey.id);
			//pEffect->RemoveNotify(this);都要删除了，就不用了
			delete this;
		}
	};

	World* SingleEffectNotify::pWorld = NULL;

	class WorldEffectnotify : public IEffectNotify, public EffectGC
	{
	public:
		unsigned char layer;//如果大于等于XPEFFECT_LAYER_COUNT，那么就是战斗里面的
		static World* pWorld;
		virtual void OnDelete(IEffect* pEffect) override
		{
			//pEffect->RemoveNotify(this);都要删除了，就不用了
			if (pWorld->m_loadingEffects.erase(pEffect) == 0 && (!pWorld->m_bIsInDestructor))
			{
				pWorld->m_removedLoadingEffects.erase(pEffect);
			}
			delete this;
		}
		virtual void OnAsyncLoaded(IEffect* pEffect, bool succeeded) override
		{
			IEffectSet::iterator it = pWorld->m_loadingEffects.find(pEffect);
			Effect *tpEffect = (Effect*)pEffect;
			tpEffect->RemoveNotify(this);
			if (it == pWorld->m_loadingEffects.end())
			{
				//加载完之后发现其实已经不需要了。。。。
				RemoveEffect(pEffect);
				pWorld->m_removedLoadingEffects.erase(pEffect);
			} else {
				if (succeeded)
				{
					tpEffect->Play();
					if (layer >= XPEFFECT_LAYER_COUNT)
					{
						pWorld->m_warEffects[layer - XPEFFECT_LAYER_COUNT].push_back(tpEffect);
					} else {
						pWorld->m_effects[layer].push_back(tpEffect);
					}
				} else {
					if (layer >= XPEFFECT_LAYER_COUNT)
					{
						if (pWorld->m_warLiveDurEffectSet.find(pEffect) == pWorld->m_warLiveDurEffectSet.end())//是播放一次的特效，外面不会有effect的指针
							RemoveEffect(pEffect);
					} else {
						if (pWorld->m_liveDurEffectSet.find(pEffect) == pWorld->m_liveDurEffectSet.end())//是播放一次的特效，外面不会有effect的指针
							RemoveEffect(pEffect);
					}
				}
				pWorld->m_loadingEffects.erase(it);
			}
			delete this;
		}

	};

	World* WorldEffectnotify::pWorld = NULL;
	class ReadMapTask : public CFileIOManager::AsyncReadTask
	{
		World *m_pWorld;
		int m_tick;
		std::wstring m_name;	//如果是地图，就是mapname，如果是maze，就是mazename
	public:
		bool ready;
		const std::wstring &GetName() const {  return m_name; }
		XBuffer &GetData() { return m_data; }
		ReadMapTask(const std::wstring &filename, const std::wstring &name, 
			CFileIOManager *pFileIOMan, World *pWorld)
			: CFileIOManager::AsyncReadTask(pFileIOMan, filename, 0), m_pWorld(pWorld), m_name(name),
			ready(false)
		{
			m_tick = GetMilliSeconds();
		}

		virtual void OnReady()
		{
			int tick = GetMilliSeconds();
			if( m_data.size() == 0 )
			{
				XPLOG_ERROR(L"load map failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load map failed!, path is %s\n", m_filename.c_str());
			}
			else
				m_pFileIOMan->OnReady(tick-m_tick, m_data.size());
			ready = true;
			int count = 0;
			for (int i=0;i<World::XPRMTT_COUNT;++i)
			{
				if (m_pWorld->m_pReadMapTask[i]->ready)
				{
					++count;
				}
			}
			m_pWorld->PrefetchMapNotify(count);//在Notify里面删掉ReadMapTask
		}
		virtual void OnDiscard()
		{
			delete this;
		}
		virtual ~ReadMapTask() { }
	};		

	World::World(Engine *pEngine, int  viewwidth, int viewheight)
		: m_poolSurfaceRenderTargets(pEngine->GetRenderer()), m_waterAlphaMask(INVALID_PICTURE_HANDLE), m_nGameTimeDeltaMul(10),
		m_nAccDelta(0), m_nLastDelta(0), m_bReseted(true), m_totalDeltas(0), m_nDeltaPoint(0), m_bDeltaVetFull(false), 
		m_bHasCreateBGM(false), m_pLastArea(NULL), m_worldMode(XPWM_NORMAL), m_bIsInDestructor(false)
	{
		m_bgsoundMode = pEngine->GetBGSoundMode();
		m_wbhSeed = INVALID_WAR_BACKGROUND_HANDLE;
		m_warBackGroundPic = INVALID_PICTURE_HANDLE;
		m_maskPicHandle = INVALID_PICTURE_HANDLE;
		m_nWarBackGroundPicWidth = 0;
		m_nWarBackGroundPicHeight = 0;
		m_fSpriteShadowShearX = 0.7f;
		m_fSpriteShadowScalingY = 0.7f;
		m_vetDeltas.resize(XPDELTA_COUNT, 0);
		m_strMapName = L"";
		m_strPrefetchMapName = L"";
		m_bDrawWorld = true;
		m_bIsAsync = false;
		m_bIsAutoSwitch = false;
		m_bIsDrawInLoading = true;
		m_pEngine = pEngine;
		m_pPathMap = NULL;
		m_pMap = NULL;
		m_pPrefetchMap = NULL;
		m_camera.SetSize(viewwidth, viewheight);
		//m_bIsLostDevice = false;
		//如果以后放进union里面了，这个要修改
		//m_LoadMapEffectParam.transPicHandle = INVALID_PICTURE_HANDLE;
		m_hNightEffectRenderTarget = INVALID_PICTURE_HANDLE;
		if( m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget() )
		{
			const XDisplayMode& mode = m_pEngine->GetRenderer()->GetDisplayMode();
			m_hNightEffectRenderTarget = m_pEngine->GetRenderer()->CreateRenderTarget(mode.width,
				mode.height, mode.bitperpix==32?XPTEXFMT_A8R8G8B8:XPTEXFMT_A4R4G4B4);
		}
		SingleEffectNotify::pWorld = this;
		WorldEffectnotify::pWorld = this;
		for (int i=0;i<XPRMTT_COUNT;++i)
		{
			m_pReadMapTask[i] = NULL;
		}
		//默认障碍的颜色
		XPMazeColor mc;
		mc.mask = 0x3;
		mc.color.data = 0x3fff0000;
		m_mazeColors.push_back(mc);
		mc.mask = 0x1;
		mc.color.data = 0x3f04fd16;
		m_mazeColors.push_back(mc);
	}

	World::~World()
	{
		m_bIsInDestructor = true;
		if (m_pEngine->GetBGSoundMode() == XPBGSM_AUTO && m_pLastArea)
		{
			m_pLastArea->LeaveMeAndEnterNew(NULL);
		}
		FreeAllWarBackground();
		for(std::list<ImmovableObj*>::iterator it = m_immobjs.begin(), ie = m_immobjs.end();
			it != ie; ++it)
			delete *it;
		for (int layer=0;layer<XPEFFECT_LAYER_COUNT;++layer)
			for(EffectList::iterator it = m_effects[layer].begin(), 
				ie = m_effects[layer].end(); it != ie; ++it)
				delete *it;
		for(std::list<Sprite*>::iterator it = m_sprites.begin(), ie = m_sprites.end();
			it != ie; ++it)
			delete *it;
		for (int layer=0;layer<XPEFFECT_WAR_LAYER_COUNT;++layer)
			for(EffectList::iterator it = m_warEffects[layer].begin(), 
				ie = m_warEffects[layer].end(); it != ie; ++it)
				delete *it;
		for(std::list<Sprite*>::iterator it = m_warSprites.begin(), ie = m_warSprites.end();
			it != ie; ++it)
			delete *it;
		for (std::map<unsigned __int64, SingleEffectNotify*>::iterator it =m_SignleEffectMap.begin(), 
			ie = m_SignleEffectMap.end(); it != ie; ++it)
			delete it->second;
		for (IEffectSet::iterator it = m_removedLoadingEffects.begin(), 
			ie = m_removedLoadingEffects.end(); it != ie; ++it)
		{
			delete *it;
		}
		for (IEffectSet::iterator it = m_loadingEffects.begin(), 
			ie = m_loadingEffects.end(); it != ie; ++it)
		{
			delete *it;
		}
		m_poolSurfaceRenderTargets.Destroy();
		if( m_hNightEffectRenderTarget != INVALID_PICTURE_HANDLE )
			m_pEngine->GetRenderer()->FreeRenderTarget(m_hNightEffectRenderTarget);
		if (m_pPrefetchMap)
			delete m_pPrefetchMap;
		m_pPrefetchMap = NULL;
		delete m_pMap;
		delete m_pPathMap;
		for (int i=0;i<XPRMTT_COUNT;++i)
		{
			if (m_pReadMapTask[i])
			{
				delete m_pReadMapTask[i];
			}
		}
		
	}

	WarBackgroundHandle World::LoadWarBackgound( const std::wstring &name )
	{
		WarBackgroundHandleMap::iterator it = m_wbhm.find(name);
		if (it != m_wbhm.end())
			return it->second;
		WarBackgroundInfo info;
		Renderer* pRenderer = m_pEngine->GetRenderer();
		info.picHandle = pRenderer->LoadPicture(name);
		if (info.picHandle == INVALID_PICTURE_HANDLE)
			return INVALID_WAR_BACKGROUND_HANDLE;
		info.name = name;
		PictureInfo pinfo;
		if (pRenderer->GetPictureInfo(info.picHandle, pinfo))
		{
			info.width = pinfo.m_nPicWidth;
			info.height = pinfo.m_nPicHeight;
			m_wbhm[name] = ++m_wbhSeed;
			m_wbm[m_wbhSeed] = info;
			return m_wbhSeed;
		} else {
			pRenderer->FreePicture(info.picHandle);
			return INVALID_WAR_BACKGROUND_HANDLE;
		}
		
	}

	void World::FreeWarBackgroundHandle (WarBackgroundHandle handle)
	{
		WarBackgroundMap::iterator it = m_wbm.find(handle);
		if (it == m_wbm.end())
			return;
		m_wbhm.erase(it->second.name);
		m_pEngine->GetRenderer()->FreePicture(it->second.picHandle);
		m_wbm.erase(it);
	}

	bool World::SetWarBackground(WarBackgroundHandle handle)
	{
		WarBackgroundMap::iterator it = m_wbm.find(handle);
		if (it == m_wbm.end())
		{
			m_warBackGroundPic = INVALID_PICTURE_HANDLE;
			m_nWarBackGroundPicWidth = 0;
			m_nWarBackGroundPicHeight = 0;
			return false;
		}
		m_warBackGroundPic = it->second.picHandle;
		m_nWarBackGroundPicWidth = it->second.width;
		m_nWarBackGroundPicHeight = it->second.height;
		return true;
	}

	void World::FreeAllWarBackground()
	{
		WarBackgroundMap::iterator it = m_wbm.begin(), ie = m_wbm.end();
		Renderer* pRenderer = m_pEngine->GetRenderer();
		for (;it!=ie;++it)
		{
			pRenderer->FreePicture(it->second.picHandle);
		}
		m_wbm.clear();
		m_wbhm.clear();
	}

	bool World::CheckMapGUID(unsigned int mapguid) const
	{
		return (m_pMap == NULL ? false : (m_pMap->GetGUID() == mapguid)) ? true : (m_pPrefetchMap == NULL ? false : (m_pPrefetchMap->GetGUID() == mapguid));
	}

	ISprite* World::NewSprite(const std::wstring &modelname)
	{	
		Sprite *pSprite =  new ComponentSprite(m_pEngine, modelname);
		m_sprites.push_back(pSprite);
		return pSprite;
	}

	ISprite* World::NewWarSprite(const std::wstring &modelname)
	{	
		Sprite *pSprite =  new ComponentSprite(m_pEngine, modelname);
		m_warSprites.push_back(pSprite);
		return pSprite;
	}

	bool World::SelectObjs(const Location &selloc, std::vector<ISelectableObj*> &ettobjs)
	{
		ettobjs.clear();
		std::set<EntitativeObj*> tset;
		for(std::vector<DrawElement>::const_reverse_iterator it = m_canvas.m_elements.rbegin(),ie = m_canvas.m_elements.rend(); it != ie; ++it)
		{
			CRECT itRect = it->m_pos;
			if (!itRect.PtInRect(CPOINT(selloc.x,selloc.y))) continue;
			EntitativeObj* pEtobj = dynamic_cast<EntitativeObj*>(it->m_pr);
			if( tset.find(pEtobj) == tset.end())
			{
				tset.insert(pEtobj);
				if( pEtobj && pEtobj->TestPoint(selloc, 2, 0.8f))
				{
					ettobjs.push_back(pEtobj);
				}
			}
		}
		return !ettobjs.empty();
	}

	//普通特效
	//持续的效果

	IEffect* World::SetLinkedEffect(const std::wstring &name, XPEFFECT_LAYER layer, const CPOINT &pt1, const CPOINT &pt2, float time, bool async)
	{
		ParticleEffect *tpEffect = m_pEngine->GetEffectManager()->CreateLinkedEffect(name, pt1, pt2, time);
		if (tpEffect)
		{
			m_liveDurEffectSet.insert(tpEffect);
			if( tpEffect )
			{
				m_liveDurEffectSet.insert(tpEffect);
				switch (tpEffect->AssureResource(async))
				{
				case XPEARR_RESREADY:
					tpEffect->Play();
					m_effects[layer].push_back(tpEffect);
					break;
				case XPEARR_LOADING:
					{
						m_loadingEffects.insert(tpEffect);
						WorldEffectnotify* pNotify = new WorldEffectnotify();
						pNotify->layer = layer;
						tpEffect->AddNotify(pNotify);
					}
					break;
				case XPEARR_LOAD_ERROR:
					delete tpEffect;
					m_liveDurEffectSet.erase(tpEffect);
					return NULL;
					break;
				}
				tpEffect->SetDirection(XPDIR_TOP);//和资源无关
				tpEffect->SetScaleType(XPEST_ALLSCALE);
			}
		}
		return tpEffect;
	}

	IEffect* World::SetEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async)
	{
		Effect *tpEffect = m_pEngine->GetEffectManager()->CreateEffect(name, true);
		if( tpEffect )
		{
			m_liveDurEffectSet.insert(tpEffect);
			switch (tpEffect->AssureResource(async))
			{
			case XPEARR_RESREADY:
				tpEffect->Play();
				m_effects[layer].push_back(tpEffect);
				break;
			case XPEARR_LOADING:
				{
					m_loadingEffects.insert(tpEffect);
					WorldEffectnotify* pNotify = new WorldEffectnotify();
					pNotify->layer = layer;
					tpEffect->AddNotify(pNotify);
				}
				break;
			case XPEARR_LOAD_ERROR:
				delete tpEffect;
				m_liveDurEffectSet.erase(tpEffect);
				return NULL;
				break;
			}
			tpEffect->SetDirection(XPDIR_TOP);//和资源无关
			tpEffect->SetLocation(Location(x, y));//和资源无关
			tpEffect->SetScaleType(XPEST_ALLSCALE);
			IParticleEffect *pPEffect = tpEffect->TryConvertToParticle();
			if (pPEffect)
			{
				pPEffect->SetCycleMode(XPPCM_PULSE_EMISSION);
			}
		}
		return tpEffect;
	}

	IEffect* World::SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async)
	{
		Effect* tpEffect = m_pEngine->GetEffectManager()->CreateListEffect(headEffect, midEffect, endEffect, continueTime, true, async, false);
		if( tpEffect )
		{
			m_liveDurEffectSet.insert(tpEffect);
			if (tpEffect->GetPlayState() == XPES_EMPTY)
			{
				m_loadingEffects.insert(tpEffect);
				WorldEffectnotify* pNotify = new WorldEffectnotify();
				pNotify->layer = layer;
				tpEffect->AddNotify(pNotify);
			} else {
				tpEffect->Play();
				m_effects[layer].push_back(tpEffect);
			}
			tpEffect->SetDirection(XPDIR_TOP);
			tpEffect->SetLocation(Location(x, y));
			tpEffect->SetScaleType(XPEST_ALLSCALE);
		}
		return tpEffect;
	}

	void World::DeleteEffect()
	{
		EffectList::iterator it, ie;
		IEffectSet::iterator sie = m_willBeDeletedEffects.end();
		for (int layer=0;layer<XPEFFECT_LAYER_COUNT;++layer)
		{
			it = m_effects[layer].begin();
			ie = m_effects[layer].end();
			for (;it!=ie;)
			{
				if (m_willBeDeletedEffects.find(*it) != sie)
				{
					delete (*it);
					it = m_effects[layer].erase(it);
				} else {
					++it;
				}
			}
		}
		m_willBeDeletedEffects.clear();
	}

	void World::RemoveEffect(IEffect* pEffect)
	{
		IEffectSet::iterator it = m_liveDurEffectSet.find(pEffect);
		if (it == m_liveDurEffectSet.end())
			return;
		m_liveDurEffectSet.erase(it);
		it = m_loadingEffects.find(pEffect);
		if (it == m_loadingEffects.end())
		{
			m_willBeDeletedEffects.insert(pEffect);
			pEffect->ClearNotify();
			pEffect->Stop();
		} else {
			m_loadingEffects.erase(pEffect);
			m_removedLoadingEffects.insert(pEffect);
		}
	}

	//一次性效果, times是播放次数, 一般用来表现一次性伤害或者范围伤害。
	Effect* World::PlayOnceEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype)
	{
		Effect *tpEffect = m_pEngine->GetEffectManager()->CreateEffect(name, false);
		if( tpEffect )
		{
			tpEffect->SetSoundType(soundtype);
			switch (tpEffect->AssureResource(async))
			{
			case XPEARR_RESREADY:
				tpEffect->Play();
				m_effects[layer].push_back(tpEffect);
				break;
			case XPEARR_LOADING:
				{
					m_loadingEffects.insert(tpEffect);
					WorldEffectnotify* pNotify = new WorldEffectnotify();
					pNotify->layer = layer;
					tpEffect->AddNotify(pNotify);
				}
				break;
			case XPEARR_LOAD_ERROR:
				delete tpEffect;
				return NULL;
				break;
			}
			tpEffect->SetScaleType(XPEST_ALLSCALE);
			tpEffect->SetLoopNum(times);//只有粒子特效有自己的实现，其它的特效的这个方法和资源无关，粒子的psl还是同步的，只有图片资源是异步
			tpEffect->SetDirection(XPDIR_TOP);//和资源无关
			tpEffect->SetLocation(Location(x, y));//和资源无关
		}
		return tpEffect;
	}

	//战斗状态特效
	IEffect* World::SetWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, bool async)
	{
		XPASSERT(layer < XPEFFECT_WAR_LAYER_COUNT);
		Effect *tpEffect = m_pEngine->GetEffectManager()->CreateEffect(name, true);
		if( tpEffect )
		{
			m_warLiveDurEffectSet.insert(tpEffect);
			switch (tpEffect->AssureResource(async))
			{
			case XPEARR_RESREADY:
				tpEffect->Play();
				m_warEffects[layer].push_back(tpEffect);
				break;
			case XPEARR_LOADING:
				{
					m_loadingEffects.insert(tpEffect);
					WorldEffectnotify* pNotify = new WorldEffectnotify();
					pNotify->layer = XPEFFECT_LAYER_COUNT + layer;
					tpEffect->AddNotify(pNotify);
				}
				break;
			case XPEARR_LOAD_ERROR:
				m_warLiveDurEffectSet.erase(tpEffect);
				delete tpEffect;
				return NULL;
				break;
			}
			tpEffect->SetScaleType(XPEST_ALLSCALE);
			tpEffect->SetDirection(XPDIR_TOP);//和资源无关
			tpEffect->SetLocation(Location(x, y));//和资源无关
			IParticleEffect *pPEffect = tpEffect->TryConvertToParticle();
			if (pPEffect)
			{
				pPEffect->SetCycleMode(XPPCM_PULSE_EMISSION);
			}
		}
		return tpEffect;
	}

	IEffect* World::SetWarContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, XPEFFECT_LAYER layer, int x, int y, bool async)
	{
		XPASSERT(layer < XPEFFECT_WAR_LAYER_COUNT);
		Effect* tpEffect = m_pEngine->GetEffectManager()->CreateListEffect(headEffect, midEffect, endEffect, continueTime, true, async, false);
		if( tpEffect )
		{
			m_warLiveDurEffectSet.insert(tpEffect);
			if (tpEffect->GetPlayState() == XPES_EMPTY)
			{
				m_loadingEffects.insert(tpEffect);
				WorldEffectnotify* pNotify = new WorldEffectnotify();
				pNotify->layer = XPEFFECT_LAYER_COUNT + layer;
				tpEffect->AddNotify(pNotify);
			} else {
				tpEffect->Play();
				m_warEffects[layer].push_back(tpEffect);
			}
			tpEffect->SetScaleType(XPEST_ALLSCALE);
			tpEffect->SetDirection(XPDIR_TOP);
			tpEffect->SetLocation(Location(x, y));
		}
		return tpEffect;
	}

	void World::DeleteWarEffect()
	{
		EffectList::iterator it, ie;
		IEffectSet::iterator sie = m_warWillBeDeletedEffects.end();
		for (int layer=0;layer<XPEFFECT_WAR_LAYER_COUNT;++layer)
		{
			it = m_warEffects[layer].begin();
			ie = m_warEffects[layer].end();
			for (;it!=ie;)
			{
				if (m_warWillBeDeletedEffects.find(*it) != sie)
				{
					delete (*it);
					it = m_warEffects[layer].erase(it);
				} else {
					++it;
				}
			}
		}
		m_warWillBeDeletedEffects.clear();		
	}

	void World::RemoveWarEffect(IEffect* pEffect)
	{
		IEffectSet::iterator it = m_warLiveDurEffectSet.find(pEffect);
		if (it == m_warLiveDurEffectSet.end())
			return;
		m_warLiveDurEffectSet.erase(it);
		it = m_loadingEffects.find(pEffect);
		if (it == m_loadingEffects.end())
		{
			m_warWillBeDeletedEffects.insert(pEffect);
			pEffect->ClearNotify();
			pEffect->Stop();
		} else {
			m_loadingEffects.erase(pEffect);
			m_removedLoadingEffects.insert(pEffect);
		}
	}

	//一次性效果, times是播放次数, 一般用来表现一次性伤害或者范围伤害。
	Effect* World::PlayOnceWarEffect(const std::wstring &name, XPEFFECT_LAYER layer, int x, int y, int times, bool async, unsigned char soundtype)
	{
		XPASSERT(layer < XPEFFECT_WAR_LAYER_COUNT);
		Effect *tpEffect = m_pEngine->GetEffectManager()->CreateEffect(name, false);
		if( tpEffect )
		{
			tpEffect->SetSoundType(soundtype);
			switch (tpEffect->AssureResource(async))
			{
			case XPEARR_RESREADY:
				tpEffect->Play();
				m_warEffects[layer].push_back(tpEffect);
				break;
			case XPEARR_LOADING:
				{
					m_loadingEffects.insert(tpEffect);
					WorldEffectnotify* pNotify = new WorldEffectnotify();
					pNotify->layer = XPEFFECT_LAYER_COUNT + layer;
					tpEffect->AddNotify(pNotify);
				}
				break;
			case XPEARR_LOAD_ERROR:
				delete tpEffect;
				return NULL;
				break;
			}
			tpEffect->SetScaleType(XPEST_ALLSCALE);
			tpEffect->SetLoopNum(times);//只有粒子特效有自己的实现，其它的特效的这个方法和资源无关，粒子的psl还是同步的，只有图片资源是异步
			tpEffect->SetDirection(XPDIR_TOP);//和资源无关
			tpEffect->SetLocation(Location(x, y));//和资源无关
		}
		return tpEffect;
	}

	//普通精灵
	void World::DeleteSprite()
	{
		std::list<Sprite*>::iterator it = m_sprites.begin(), ie=m_sprites.end();
		std::set<ISprite*>::iterator sie = m_willBeDeletedSprites.end();
		if (m_willBeDeletedSprites.find(m_camera.m_pAttachedSprite) != sie)
		{
			m_camera.Attach(NULL);
		}
		
		for (;it!=ie;)
		{
			if (m_willBeDeletedSprites.find(*it) != sie)
			{
				delete (*it);
				it = m_sprites.erase(it);
			} else {
				++it;
			}
		}
		m_willBeDeletedSprites.clear();
	}

	void World::DeleteSprite(ISprite* sprite)
	{
		m_willBeDeletedSprites.insert(sprite);
	}

	void World::DeleteAllSprite(bool keepAttached)
	{
		m_willBeDeletedSprites.insert(m_sprites.begin(), m_sprites.end());
		if (keepAttached)
			m_willBeDeletedSprites.erase(m_camera.m_pAttachedSprite);
	}

	//战斗精灵
	void World::DeleteWarSprite()
	{
		std::list<Sprite*>::iterator it = m_warSprites.begin(), ie=m_warSprites.end();
		std::set<ISprite*>::iterator sie = m_warWillBeDeletedSprites.end();
		for (;it!=ie;)
		{
			if (m_warWillBeDeletedSprites.find(*it) != sie)
			{
				delete (*it);
				it = m_warSprites.erase(it);
			} else {
				++it;
			}
		}
		m_warWillBeDeletedSprites.clear();
	}

	void World::DeleteWarSprite(ISprite* sprite)
	{
		m_warWillBeDeletedSprites.insert(sprite);
	}

	void World::DeleteAllWarSprite()
	{
		m_warWillBeDeletedSprites.insert(m_warSprites.begin(), m_warSprites.end());
	}

	void World::OnTimer(int iPeriod)
	{
		if( m_pMap  && m_pEngine->GetConfigManager()->IsDynamicMapLoading() )
		{
			//XPTRACE(L"assure resource\n");
			m_pMap->AssureRegionResource(m_camera.GetViewport(), true, false, false, 0);
		}
	}

	bool World::NotifyLoadmapProgress(int pro)
	{
		if( m_pEngine->GetApp()->LoadingMapNotify(pro) ) 
		{
			if (!m_bIsAsync && m_bIsDrawInLoading)
			{
				static DWORD lastDraw = 0;
				DWORD now = GetTickCount();
				if (now - lastDraw > 50 || pro == 100)
				{
					m_canvas.Clear();
					m_pEngine->Render(false);
					lastDraw = now;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	void World::NotifyALoadItemFin(unsigned char beginPro, unsigned char endPro)
	{
		if (m_nTotalLoadingCount)
		{
			++m_nLoadingCount;
			NotifyLoadmapProgress(m_nLoadingCount * (endPro - beginPro) / m_nTotalLoadingCount + beginPro);
			if (m_bIsAutoSwitch && m_nTotalLoadingCount == m_nLoadingCount)
			{
				if (m_pPrefetchMap)
				{
					XPLoadmapParam param;
					param.objLoc = m_PrefetchPt;
					LoadMap(m_strPrefetchMapName, L"", &param, endPro, false);
				} else {
					m_bDrawWorld = true;
					m_bIsAutoSwitch = false;
					m_bIsAsync = false;
					SetViewport(m_PrefetchPt, false);
					CancelPrefetch();
				}
			}
		}
	}

	bool World::SetMapMaze(const void* mazeBuffer, size_t size)
	{
		return m_pMap->SetMapMaze(mazeBuffer, size);
	}

	//设置声音地图（编辑器专用）
	bool World::SetSoundAreas(XMLIO::CINode& root, PStepSoundMap* pSSM)
	{
		if (!m_pMap)
			return false;
		m_pLastArea = NULL;
		m_activeEnvSoundArea.clear();
		m_pMap->FreeSoundMap();
		if (m_pMap->LoadSoundAreas(root, pSSM))
		{
			m_bHasCreateBGM = false;
			return true;
		}
		return false;
	}

	bool World::GetMapSize(CPOINT &size) const
	{
		if (!m_pMap)
			return false;
		size.x = m_pMap->m_width;
		size.y = m_pMap->m_height;
		return true;
	}

	bool World::LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, int pro, bool async)	// 加载地图
	{
		m_bReseted = true;
		if (param == NULL)
			return false;
		XPLOG_INFO(L"  开始加载地图: %s\n", mapname.c_str());
		bool switchPrefectchMap = ( mapname == m_strPrefetchMapName && m_pPrefetchMap);
		m_bDrawWorld = false;
		m_bIsAsync = async;
		if (!switchPrefectchMap)
		{
			CancelPrefetch();
			if (async)
			{
				XPLOG_INFO(L"  异步加载地图: %s\n", mapname.c_str());
				return PrefetchMapRes(mapname, mazename, param->objLoc, true);
			}
		}
		NotifyLoadmapProgress(1 * (100 - pro) / 100 + pro);

		if( m_pMap && !UnloadMap() ) 
		{
			m_bDrawWorld = true;
			return false;
		}
		NotifyLoadmapProgress(5 * (100 - pro) / 100 + pro);
		if (!switchPrefectchMap)
		{
			m_pMap = new Map(this, m_pEngine, &m_picResrcs);
			if(!m_pMap->Load(mapname, mazename, m_pPathMap))
			{
				delete m_pMap;
				m_pMap = NULL;
				m_bDrawWorld = true;
				return false;
			}
		} else {
			m_pMap = m_pPrefetchMap;
			m_strMapName = m_strPrefetchMapName;
			m_pPrefetchMap = NULL;
			m_strPrefetchMapName = L"";
			m_manualPrefetchEffects.clear();
			m_manualPrefetchPicResrcs.clear();
		}
		m_pMap->UseMapMaze();
		
		if (m_pMap->HasWater())
		{
			//有水
			if (m_waterAlphaMask == INVALID_PICTURE_HANDLE)
			{
				m_waterAlphaMask = m_pEngine->GetRenderer()->LoadPicture(L"/map/tiles/tilemask.png");
			}
			for (int i=0;i<SubMap::PGround::WATER_EFFECT_COUNT;++i)
			{
				m_pMap->m_waterEffects[i].SetAlphaMask(m_waterAlphaMask);
			}
		} else {
			//没有水
			if (m_waterAlphaMask != INVALID_PICTURE_HANDLE)
			{
				m_pEngine->GetRenderer()->FreePicture(m_waterAlphaMask);
				m_waterAlphaMask = INVALID_PICTURE_HANDLE;
			}
		}
		

		if (!NotifyLoadmapProgress(21 * (100 - pro) / 100 + pro))
		{
			m_bDrawWorld = true;
			return false;
		}

		m_camera.Reset(param->objLoc, m_pMap->m_width, m_pMap->m_height);
		m_pMap->AssureRegionResource(m_camera.GetViewport(), false, true, false, 22 * (100 - pro) / 100 + pro);

		NotifyLoadmapProgress(99 * (100 - pro) / 100 + pro);
		
		m_strMapName = mapname;
		m_bHasCreateBGM = false;
		XPLOG_INFO(L"  加载地图成功: %s\n", mapname.c_str());
		NotifyLoadmapProgress(100);
		m_bDrawWorld = true;
		return true;

	}

	bool World::UnloadMap()			// 卸载地图
	{
		if (!m_pMap)
			return true;
		DirectSoundManager* pDSM = m_pEngine->GetSoundManager();
		pDSM->StopTypeSound(m_pEngine->GetConfigManager()->GetEnvSoundType());
		if (m_pEngine->GetBGSoundMode() == XPBGSM_AUTO && m_pLastArea)
		{
			m_pLastArea->FadeOut(m_pEngine->GetConfigManager()->GetUnloadMapBGSoundFadeOutTime());
		}
		m_strMapName = L"";
		//清除已经调了Remove，收集好了，但是来不及删除的东西
		DeleteSprite();
		DeleteWarSprite();
		DeleteEffect();
		DeleteWarEffect();
		DeleteImmovableObj();
		m_activeEnvSoundArea.clear();
		m_removedLoadingEffects.insert(m_loadingEffects.begin(), m_loadingEffects.end());
		m_loadingEffects.clear();
		//清除剩下的Effect
		EffectList::iterator eit, eie;
		for (int layer=0;layer<XPEFFECT_LAYER_COUNT;++layer)
		{
			eit = m_effects[layer].begin();
			eie = m_effects[layer].end();
			for (;eit!=eie;++eit)
			{
				(*eit)->Stop();
				delete (*eit);
			}
			m_effects[layer].clear();
		}
		m_liveDurEffectSet.clear();
		for (int layer=0;layer<XPEFFECT_WAR_LAYER_COUNT;++layer)
		{
			eit = m_warEffects[layer].begin();
			eie = m_warEffects[layer].end();
			for (; eit != eie; ++eit)
			{
				(*eit)->Stop();
				delete (*eit);
			}
			m_warEffects[layer].clear();
		}
		m_warLiveDurEffectSet.clear();
		//清除剩下的ImmovableObj
		std::list<ImmovableObj*>::iterator iit = m_immobjs.begin(), iie=m_immobjs.end();
		for (;iit!=iie;++iit)
		{
			delete (*iit);
		}
		m_immobjs.clear();
		//不清除Sprite


		if (m_pPrefetchMap == NULL)
		{
			m_activeEffects.clear();
			m_activePicResrcs.clear();
			m_manualPrefetchEffects.clear();
			m_manualPrefetchPicResrcs.clear();
		} else {
			m_activeEffects = m_manualPrefetchEffects;
			m_activePicResrcs = m_manualPrefetchPicResrcs;
		}
		
		delete m_pMap;
		m_pMap = NULL;
		m_camera.ClearCache();
		m_sortCache.clear();
		m_poolSurfaceRenderTargets.ReleaseAll();
		m_pLastArea = NULL;
		return true;
	}
		
	bool World::Init()
	{
		m_poolSurfaceRenderTargets.Reset(GetSurfaceCacheCountByView(m_camera.GetViewport()),Map::REGION_WIDTH,Map::REGION_HEIGHT);
		m_pPathMap = new SubMap::PPathMap(m_pEngine->GetFileIOManager());
		m_bIsANewDay = false;
		SetGameTime(0);
		return m_pPathMap->Load();
	}

	void World::SetViewSize(int width, int height)
	{
		m_camera.SetSize(width, height);
	}

	void World::AttachCameraTo(ISprite* sprite)
	{
		if (sprite == NULL)
			m_camera.Attach(NULL);
		else {
			std::list<Sprite*>::iterator iter = m_sprites.begin();
			for(;iter!=m_sprites.end();iter++)
			{
				if (*iter == sprite)
				{
					m_camera.Attach(*iter);
					break;
				}
			}
		}
		
	}

	CRECT World::GetViewport(){
		return m_camera.GetViewport();
	}

	void World::UpdateDistorts(int delta)
	{
		int updateTime = m_pEngine->GetTick();
		static int lasttime = updateTime;
		if (lasttime == updateTime)
			updateTime -= 1;
		lasttime = updateTime;
		float fDelta = static_cast<float>(delta) / 1000.0f;
		float scale = m_pEngine->GetWorldScale();
		//更新Distorts
		CRECT vp = m_pEngine->GetViewport();
		for (int i=0;i<MAP_LAYER_COUNT-1;++i)
		{
			for(std::vector<Map::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), 
				ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for (std::vector<Distort*>::const_iterator disIter = (*it)->m_vpDistorts[i].begin(),
					disIE = (*it)->m_vpDistorts[i].end(); disIter!=disIE;++disIter)
				{
					Distort* pDistort = *disIter;
					if (pDistort->lastUpdateTick != updateTime)
					{
						pDistort->setZoom(scale);
						pDistort->Update(fDelta);
						pDistort->lastUpdateTick = updateTime;
					}
				}
			}
		}
	}

	void World::SimpleUpdateTrees(int delta, int updatetime, bool randomlight)
	{
		CRECT vp = m_pEngine->GetViewport();
		float scale = m_pEngine->GetWorldScale();
		for (int i=0;i<MAP_LAYER_COUNT-1;++i)
		{
			for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), 
				ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for (std::vector<LkoTree*>::const_iterator treeIter = (*it)->m_vpLkoTrees[i].begin(),
					treeIE = (*it)->m_vpLkoTrees[i].end(); treeIter!=treeIE;++treeIter)
				{
					if ((*treeIter)->lastUpdateTick != updatetime)
					{
						(*treeIter)->SimpleUpdate(delta, vp.left, vp.top, scale);
						if (randomlight)
						{
							(*treeIter)->RandomBrightTime();
						}
						(*treeIter)->lastUpdateTick = updatetime;
					}
				}
			}
		}
	}

	void World::NormalUpdateTrees(int delta, int updatetime, bool randomlight)
	{
		CRECT vp = m_pEngine->GetViewport();
		float scale = m_pEngine->GetWorldScale();
		for (int i=0;i<MAP_LAYER_COUNT-1;++i)
		{
			for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), 
				ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for (std::vector<LkoTree*>::const_iterator treeIter = (*it)->m_vpLkoTrees[i].begin(),
					treeIE = (*it)->m_vpLkoTrees[i].end(); treeIter!=treeIE;++treeIter)
				{
					if ((*treeIter)->lastUpdateTick != updatetime)
					{
						(*treeIter)->Update(delta, vp.left, vp.top, scale);
						if (randomlight)
						{
							(*treeIter)->RandomBrightTime();
						}
						(*treeIter)->lastUpdateTick = updatetime;
					}
				}
			}
		}
	}

	void World::UpdateTrees(int delta)
	{
		int updateTime = m_pEngine->GetTick();
		static int lasttime = updateTime;
		if (lasttime == updateTime)
			updateTime -= 1;
		lasttime = updateTime;
		//更新动态物件
		DWORD nightTime = XP_NIGHT_START_TIME - XP_LIGNT_RANGE;
		DWORD dayTime = XP_DAY_START_TIME + XP_LIGNT_RANGE;
		if (nightTime < 0)
			nightTime += XP_A_DAY_TIME;
		if (dayTime > XP_A_DAY_TIME)
			dayTime -= XP_A_DAY_TIME;
		bool updateNewLightTime = false;
		if (nightTime < dayTime)
		{
			if ((!m_bIsANewDay) && (m_nGameTime > dayTime || 
				m_nGameTime < nightTime))
			{
				m_bIsANewDay = true;
				updateNewLightTime = true;
			}
			if (m_bIsANewDay && m_nGameTime > nightTime && 
				m_nGameTime < dayTime)
			{
				m_bIsANewDay = false;
			}
		} else {
			if ((!m_bIsANewDay) && m_nGameTime < nightTime && 
				m_nGameTime > dayTime)
			{
				m_bIsANewDay = true;
				updateNewLightTime = true;
			}
			if (m_bIsANewDay && (m_nGameTime > nightTime || 
				m_nGameTime < dayTime))
			{
				m_bIsANewDay = false;
			}
		}
		m_pEngine->GetConfigManager()->IsEnableLinkedObjs()?
			NormalUpdateTrees(delta, updateTime, updateNewLightTime):SimpleUpdateTrees(delta, updateTime, updateNewLightTime);
	}

	void World::UpdateTriggers(int delta)
	{
		static __int64 updateTick = 0;
		++updateTick;
		std::vector<Region::TriggerObjInfo*>::const_iterator triggerIter, triggerIE;
		RegionMap::TriggerInfoVet::iterator triggerInfoIter, triggerInfoIE;
		Region::TriggerObjInfo* pInfo;
		//bool clearState;
		CRECT vp = m_pEngine->GetViewport();
		CRECT objRect;
		int effectCount = 0, i = 0;
		int s;	//触发特效有效面积
		int tmp;	//用来记录一个tile的面积和触发间隔的积
		float f;
		Location loc;
		std::vector<ActionTypeFlag> actionFlags;
		std::vector<ActionTypeFlag>::iterator afIt, afIe;
		TriggerKey key;
		for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), 
			ie = m_pMap->m_curRegions.end(); it != ie; ++it)
		{
			triggerIter = (*it)->m_vpTriggerObjInfo.begin();
			triggerIE = (*it)->m_vpTriggerObjInfo.end();
			for (;triggerIter!=triggerIE;++triggerIter)
			{
				key.pTriggerObjInfo = pInfo = *triggerIter;
				key.infoVetID = 0;
				if (pInfo->updateTick == updateTick)
					continue;
				pInfo->updateTick = updateTick;
				if (!IsRectCross(vp, pInfo->rect))
					continue;
				//clearState = (updateTick - m_pMap->m_TriggerInfos[pInfo->infoID].updateTick > 1);
				//m_pMap->m_TriggerInfos[pInfo->infoID].updateTick = updateTick;
				triggerInfoIter = m_pMap->m_TriggerInfos[pInfo->infoID].begin();
				triggerInfoIE = m_pMap->m_TriggerInfos[pInfo->infoID].end();
				//触发特效和屏幕的交集区域
				objRect.left = vp.left>pInfo->rect.left?vp.left:pInfo->rect.left;
				objRect.top = vp.top>pInfo->rect.top?vp.top:pInfo->rect.top;
				objRect.right = vp.right<pInfo->rect.right?vp.right:pInfo->rect.right;
				objRect.bottom = vp.bottom<pInfo->rect.bottom?vp.bottom:pInfo->rect.bottom;
				s = objRect.Width() * objRect.Height();
				if (s == 0)
					continue;
				actionFlags.clear();
				for (;triggerInfoIter!=triggerInfoIE;++triggerInfoIter, ++key.infoVetID)
				{
					//if (clearState)
					//{
					//	triggerInfoIter->effectTime = 0;
					//	triggerInfoIter->triggerTime = 0;
					//}
					if (triggerInfoIter->triggerTypeFlag & Nuclear::SubMap::TRIGGER_TIME)
					{
						//时间触发判断条件
						//只要 1 begin > time 
						//     2 time >= end
						//     3 end >= begin 三个条件里面满足任意两个就认为不满足时间触发条件条件
						if ( ((triggerInfoIter->beginTime > m_nGameTime) && (m_nGameTime >= triggerInfoIter->endTime)) ||
							 ((triggerInfoIter->beginTime > m_nGameTime) && (triggerInfoIter->endTime >= triggerInfoIter->beginTime)) ||
							 ((m_nGameTime >= triggerInfoIter->endTime) && (triggerInfoIter->endTime >= triggerInfoIter->beginTime)))
							 continue;
					}
					if (triggerInfoIter->triggerTypeFlag & Nuclear::SubMap::TRIGGER_SPRITE)
					{
						actionFlags.push_back(triggerInfoIter->actionsFlag);
					} else if (triggerInfoIter->triggerTypeFlag & Nuclear::SubMap::TRIGGER_NONRANDOM_AND_SINGLE) {
						if (m_SignleEffectMap.find(key.id) == m_SignleEffectMap.end())	//没有被触发
						{
							triggerInfoIter->triggerTime += delta;
							if ((triggerInfoIter->maxDelta > 0) && (triggerInfoIter->effectTime >= triggerInfoIter->maxDelta))
							{
								loc = pInfo->rect.Center();
								Effect* pEffect = PlayOnceEffect(triggerInfoIter->effname, triggerInfoIter->layer, loc.x, loc.y, 1, true, m_pEngine->GetConfigManager()->GetEnvSoundType());
								if (pEffect)
								{
									SingleEffectNotify* pNotify = new SingleEffectNotify();
									pNotify->triggerKey = key;
									m_SignleEffectMap[key.id] = pNotify;
									pEffect->AddNotify(pNotify);
								}
								triggerInfoIter->effectTime = 0;
								triggerInfoIter->triggerTime = 0;
							} else {
								triggerInfoIter->effectTime += delta;
							}
							if (triggerInfoIter->triggerTime >= triggerInfoIter->triDelta)
							{
								triggerInfoIter->triggerTime -= triggerInfoIter->triDelta;
								f = static_cast<float>(rand()) / (RAND_MAX + 1);	//变成一个[0,1)的小数
								if (f < triggerInfoIter->frequency)
								{
									//终于触发了，感动啊～ - -b
									loc = pInfo->rect.Center();
									Effect* pEffect = PlayOnceEffect(triggerInfoIter->effname, triggerInfoIter->layer, loc.x, loc.y, 1, true, m_pEngine->GetConfigManager()->GetEnvSoundType());
									if (pEffect)
									{
										SingleEffectNotify* pNotify = new SingleEffectNotify();
										pNotify->triggerKey = key;
										m_SignleEffectMap[key.id] = pNotify;
										pEffect->AddNotify(pNotify);
									}
									triggerInfoIter->effectTime = 0;
								}
							}
						}
					} else {
						//非Sprite触发特效的触发概率和触发间隔是按照Tile来计算的
						tmp = Nuclear::PMap::TILE_S * triggerInfoIter->triDelta;
						effectCount = s * triggerInfoIter->triggerTime / tmp;
						if ((triggerInfoIter->maxDelta > 0) && (effectCount == 0) && (triggerInfoIter->effectTime > triggerInfoIter->maxDelta))
						{
							loc.x = rand() % (objRect.Width()) + objRect.left;
							loc.y = rand() % (objRect.Height()) + objRect.top;
							PlayEffect(triggerInfoIter->effname, triggerInfoIter->layer, loc.x, loc.y, 1, true, m_pEngine->GetConfigManager()->GetEnvSoundType());
							triggerInfoIter->effectTime = 0;
							triggerInfoIter->triggerTime = 0;
						} else {
							triggerInfoIter->effectTime += delta;
						}
						triggerInfoIter->triggerTime += delta;
						triggerInfoIter->triggerTime -= effectCount * tmp / s;
						if (effectCount > 0)
						{
							for (i=0;i<effectCount;++i)
							{
								f = static_cast<float>(rand()) / (RAND_MAX + 1);	//变成一个[0,1)的小数
								if (f < triggerInfoIter->frequency)
								{
									//终于触发了，感动啊～ - -b
									loc.x = rand() % (objRect.Width()) + objRect.left;
									loc.y = rand() % (objRect.Height()) + objRect.top;
									PlayEffect(triggerInfoIter->effname, triggerInfoIter->layer, loc.x, loc.y, 1, true, m_pEngine->GetConfigManager()->GetEnvSoundType());
									triggerInfoIter->effectTime = 0;
								}
							}
						}
					}
				}
				//sprite的触发
				if (actionFlags.size() > 0)
				{
					std::list<Sprite*>::iterator sprIter = m_sprites.begin();
					std::list<Sprite*>::iterator sprEnd = m_sprites.end();
					std::vector<std::vector<Location>> locs(actionFlags.size());
					std::vector<std::vector<Location>>::iterator locIt;
					afIe = actionFlags.end();
					for (;sprIter!=sprEnd;++sprIter)
					{
						loc = (*sprIter)->GetLocation();
						if (objRect.PtInRect(loc))
						{
							//判断动作是否符合
							afIt = actionFlags.begin();
							locIt = locs.begin();
							ActionTypeFlag atf = (*sprIter)->GetCurrentActionType();
							for (;afIt!=afIe;++afIt,++locIt)
							{
								//如果动作符合
								if (atf & (*afIt))
								{
									locIt->push_back(loc);
								}								
							}
						}
					}
					triggerInfoIter = m_pMap->m_TriggerInfos[pInfo->infoID].begin();
					locIt = locs.begin();
					for (;triggerInfoIter!=triggerInfoIE;++triggerInfoIter)
					{
						if (triggerInfoIter->triggerTypeFlag & Nuclear::SubMap::TRIGGER_SPRITE)
						{
							int count = static_cast<int>(locIt->size());
							if (count > 0)
							{
								//求出目前在rect里面的精灵的数量a，
								//每次update尝试触发[a*b/d]=n个特效(d为触发间隔)，
								//也就是产生n个随机数进一步决定是否触发，
								//之后b要加上本次更新的delta值，再b-=n*d/a
								effectCount = count * triggerInfoIter->triggerTime / triggerInfoIter->triDelta;
								if ((triggerInfoIter->maxDelta > 0) && (effectCount == 0) && (triggerInfoIter->effectTime > triggerInfoIter->maxDelta))
								{
									loc = (*locIt)[rand() % count];	//假设count比RAND_MAX小很多，事实上，一个屏幕1万人也是不可能的事情。。。
									PlayEffect(triggerInfoIter->effname, triggerInfoIter->layer, loc.x, loc.y, 1, true, m_pEngine->GetConfigManager()->GetEnvSoundType());
									triggerInfoIter->effectTime = 0;
									triggerInfoIter->triggerTime = 0;
								} else {
									triggerInfoIter->effectTime += delta;
								}
								triggerInfoIter->triggerTime += delta;
								triggerInfoIter->triggerTime -= effectCount * triggerInfoIter->triDelta / count;
								if (effectCount > 0)
								{
									for (i=0;i<effectCount;++i)
									{
										f = static_cast<float>(rand()) / (RAND_MAX + 1);	//变成一个[0,1)的小数
										if (f < triggerInfoIter->frequency)
										{
											//终于触发了，感动啊～ - -b
											loc = (*locIt)[rand() % count] + triggerInfoIter->excursion;
											PlayEffect(triggerInfoIter->effname, triggerInfoIter->layer, loc.x, loc.y, 1, true, m_pEngine->GetConfigManager()->GetEnvSoundType());
											triggerInfoIter->effectTime = 0;
										}
									}
								}
							}
							++locIt;
						}
					}
				}
			}
		}
	}

	void World::UpdateEffects(int delta)
	{
		for(std::vector<Map::RegionInfo*>::iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); 
			it != ie; ++it)
		{
			for (int i=0;i<MAP_LAYER_COUNT - 1;++i)
			{
				//地图特效
				for(std::vector<Region::EffectInfo>::iterator it2 = (*it)->m_effects[i].begin(), ie2 = (*it)->m_effects[i].end();
				it2 != ie2; ++it2)
				{
					it2->pEffect->Update(delta);
				}
			}
		}
	}

	int World::GetSurfaceCacheCountByView(const CRECT &viewrect)
	{
		return ((viewrect.Width()-1)/Map::REGION_WIDTH+4)*((viewrect.Height()-1)/Map::REGION_HEIGHT+4);
			
	}

	void World::UpdateBGMArea(const CPOINT &pt)
	{
		DirectSoundManager *pDSM = m_pEngine->GetSoundManager();
		if  (!m_pMap || m_bgsoundMode != XPBGSM_AUTO || !pDSM->IsSoundTypeEnable(m_pEngine->GetConfigManager()->GetBGMType()))
			return;
		Camera* v = &m_camera;
		
		
		bool lastIsDefault = false;
		float fVol = 1.0f;
		int regionID = (pt.y / Map::REGION_HEIGHT) * m_pMap->m_regioncol + (pt.x / Map::REGION_WIDTH);
		std::vector<XPBkgVolArea*> &volAreas = m_pMap->m_regions[regionID].m_BkgVolumeAreas;
		std::vector<XPBkgVolArea*>::iterator vit = volAreas.begin(), vie = volAreas.end();
		for (;vit!=vie;++vit)
		{
			float tmpVol = (*vit)->GetAreaVolume(pt);
			fVol = fVol<tmpVol?fVol:tmpVol;
		}
		if (m_pLastArea)
		{
			lastIsDefault = (m_pLastArea->GetType() == XPAREA_TYPE_NULL);
			if (!lastIsDefault)
			{
				if (m_pLastArea->TestArea(pt))
				{
					if (!m_bHasCreateBGM)
					{
						m_pLastArea->LeaveMeAndEnterNew(m_pLastArea);
						m_bHasCreateBGM = true;
					}
					m_pLastArea->SetVolume(fVol);
					return;
				}
			}
		}
		//来到这里，有三种可能：1 last是NULL；2 last是Default；3 已经离开了last了
		
		std::vector<XPBkgSoundArea*> &areas = m_pMap->m_regions[regionID].m_BkgSoundAreas;
		std::vector<XPBkgSoundArea*>::iterator it = areas.begin(), ie = areas.end();
		for (;it!=ie;++it)
		{
			if ((*it)->TestArea(pt))
			{
				if (m_pLastArea)
				{
					m_pLastArea->LeaveMeAndEnterNew((*it));
					m_pLastArea = (*it);
				} else {
					m_pLastArea = (*it);
					m_pLastArea->LeaveMeAndEnterNew(m_pLastArea);
				}
				m_bHasCreateBGM = true;
				m_pLastArea->SetVolume(fVol);
				return;
			}
		}

		//来到这里，last有三种可能：1 last是NULL；2 last是Default；3 已经离开了last了
		//但是现在不在任何一个区域里面，播放默认背景音乐
		//只要不是第2种情况，都需要播放默认背景音乐
		if ((!m_bHasCreateBGM) || (!lastIsDefault))
		{
			m_bHasCreateBGM = true;
			if (m_pLastArea)
			{
				m_pLastArea->LeaveMeAndEnterNew(m_pMap->m_pBackGroundDefaultArea);
				m_pLastArea = m_pMap->m_pBackGroundDefaultArea;
			} else {
				m_pLastArea = m_pMap->m_pBackGroundDefaultArea;
				if (m_pLastArea)
				{
					m_pLastArea->LeaveMeAndEnterNew(m_pLastArea);
				}
			}
		}
		if (m_pLastArea)
		{
			m_pLastArea->SetVolume(fVol);
		}
	}

	//XPBGSM_AUTO 》XPBGSM_CUSTOM、XPBGSM_BATTLE
	//XPBGSM_CUSTOM　》XPBGSM_AUTO、XPBGSM_BATTLE
	//XPBGSM_BATTLE　》XPBGSM_CUSTOM、XPBGSM_AUTO

	void World::OnChangeBGSoundMode(XPBGSoundMode mode)
	{
		if (mode == m_bgsoundMode)
			return;
		DirectSoundManager *pDSM = m_pEngine->GetSoundManager();
		ConfigManager *pCfgMan = m_pEngine->GetConfigManager();
		switch (m_bgsoundMode)
		{
		case XPBGSM_AUTO:
			if (mode == XPBGSM_CUSTOM)
			{
				ResetBGSound();
			} else { //mode == XPBGSM_BATTLE
				if (pDSM && pCfgMan)
				{
					pDSM->PauseTypeSound(pCfgMan->GetBGMType());
					pDSM->PauseTypeSound(pCfgMan->GetEnvSoundType());
				}
			}
			break;
		case XPBGSM_CUSTOM:
			ResetBGSound();
			if (mode == XPBGSM_BATTLE && pDSM && pCfgMan)
			{
				pDSM->PauseTypeSound(pCfgMan->GetEnvSoundType());
			}
			break;
		case XPBGSM_BATTLE:
			if (pDSM && pCfgMan)
			{
				pDSM->ResumeTypeSound(pCfgMan->GetEnvSoundType());
			}
			if (mode == XPBGSM_CUSTOM)
			{
				ResetBGSound();
			} else { //mode == XPBGSM_AUTO
				if (pDSM && pCfgMan)
				{
					pDSM->ResumeTypeSound(pCfgMan->GetBGMType());
				}
			}
			break;
		}
		m_bgsoundMode = mode;
	}

	void World::ResetBGSound()
	{
		m_bHasCreateBGM = false;
		if (m_pLastArea)
		{
			m_pLastArea->LeaveMeAndEnterNew(NULL);
			m_pLastArea = NULL;
		}
	}
	
	void World::UpdateBeforeRender(int delta)
	{
		if (delta > 200)
			delta = 200;
		if( !m_bDrawWorld )
			return;
		m_nGameTime = m_nGameTime + static_cast<DWORD>(delta * m_nGameTimeDeltaMul);
		m_nGameTime %= XP_A_DAY_TIME;

		if (m_worldMode == XPWM_SIMPLE_BATTLE || m_worldMode == XPWM_WORLD_BATTLE)
		{
			if (!m_pMap)
				return;
			EffectList::iterator it, ie;
			for (int layer=0;layer<XPEFFECT_WAR_LAYER_COUNT;++layer)
			{
				it = m_warEffects[layer].begin();
				ie = m_warEffects[layer].end();
				for(;it!=ie;)
				{
					if( ! ((*it)->Update(delta) ) )
					{
						delete (*it);
						it = m_warEffects[layer].erase(it);
					}
					else
						++it;
				}
			}
			if (m_worldMode == XPWM_SIMPLE_BATTLE) return;//简单战斗状态在这里就return掉
		} else {
			m_camera.Update();
		}
		

		if  (!m_pMap)
			return;
		

		Camera* v = &m_camera;
		if (v->m_pAttachedSprite) 
		{
			if (m_pEngine->GetSystemResourceManager()->IsSoundInited() && (m_bgsoundMode != XPBGSM_BATTLE))
			{
				CPOINT centerPos = v->m_pAttachedSprite->GetLocation();
				//Update 背景音乐
				UpdateBGMArea(centerPos);
				//Update 环境音乐
				
				int regionID = (centerPos.y / Map::REGION_HEIGHT) * m_pMap->m_regioncol + (centerPos.x / Map::REGION_WIDTH);
				std::vector<XPEnvSoundArea*> &areas = m_pMap->m_regions[regionID].m_EnvSoundAreas;
				m_activeEnvSoundArea.insert(areas.begin(), areas.end());
				std::set<XPEnvSoundArea*>::iterator it = m_activeEnvSoundArea.begin(), ie = m_activeEnvSoundArea.end();
				for (;it!=ie;)
				{
					if (!(*it)->Update(centerPos))
					{
						m_activeEnvSoundArea.erase(it++);
					} else {
						++it;
					}

				}
			}
		}
		
		// 视口有变化，重新计算 cache 的数据
		if( m_camera.m_bDirty) 
		{
			const CRECT &viewport = v->GetConstViewport();
			m_pEngine->GetRenderer()->GetParticleManager()->OnViewportUpdate(viewport);
			//XPTRACE(L"ditry = true, updatecache\n");
			bool picloading = false;
			// todo 计算当前区域，太难看
			////////////////////////////////////////////
			std::set<Map::RegionInfo*> setOldRegions;
			std::set<Map::RegionInfo*> setNewRegions;
			for(std::vector<Map::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end();
				it != ie; ++it)
			{
				if( (*it)->m_pSurfaceRenderTargetCache != NULL )
					setOldRegions.insert(*it);
			}
			m_pMap->m_curRegions.clear();
			int regioncol = m_pMap->m_regioncol;
			int regionrow = m_pMap->m_regionrow;

			int minregioncol = viewport.left / Map::REGION_WIDTH - 1;
			if( minregioncol < 0 ) minregioncol = 0;

			int maxregioncol = (viewport.right -1 )/ Map::REGION_WIDTH + 1;
			if( maxregioncol >= regioncol ) maxregioncol = regioncol - 1;

			int minregionrow = viewport.top / Map::REGION_HEIGHT -1;
			if( minregionrow < 0 ) minregionrow = 0;

			int maxregionrow = (viewport.bottom -1 )/ Map::REGION_HEIGHT + 1;
			if( maxregionrow >= regionrow ) maxregionrow = regionrow - 1;

			m_pMap->m_currentRegion.left = minregioncol * Map::REGION_WIDTH;
			m_pMap->m_currentRegion.top = minregionrow * Map::REGION_HEIGHT;
			m_pMap->m_currentRegion.right = (maxregioncol+1)* Map::REGION_WIDTH;
			m_pMap->m_currentRegion.bottom = (maxregionrow+1)* Map::REGION_HEIGHT;

			if( m_pMap->m_currentRegion.right < m_pMap->m_currentRegion.left) 
				m_pMap->m_currentRegion.right = m_pMap->m_currentRegion.left;
			if( m_pMap->m_currentRegion.bottom < m_pMap->m_currentRegion.top) 
				m_pMap->m_currentRegion.bottom = m_pMap->m_currentRegion.top;

			for(int row = minregionrow; row <= maxregionrow; ++row) for(int col = minregioncol; col <= maxregioncol; ++col)
			{				
				m_pMap->m_curRegions.push_back(&m_pMap->m_regions[col+row*regioncol]);
			}
			//m_poolSurfaceRenderTargets.SetSize(m_pMap->m_curRegions.size());
			for(std::vector<Map::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end();
				it != ie; ++it)
				setNewRegions.insert(*it);
			//
			std::vector<Map::RegionInfo*> vecAdd, vecDel;
			std::set_difference(setNewRegions.begin(), setNewRegions.end(),
				setOldRegions.begin(), setOldRegions.end(), std::back_inserter(vecAdd));
			std::set_difference(setOldRegions.begin(), setOldRegions.end(),
				setNewRegions.begin(), setNewRegions.end(), std::back_inserter(vecDel));
			for(std::vector<Map::RegionInfo*>::iterator it = vecDel.begin(), ie = vecDel.end(); it != ie; ++it)
				m_poolSurfaceRenderTargets.Release((*it)->m_pSurfaceRenderTargetCache);
			for(std::vector<Map::RegionInfo*>::iterator it = vecAdd.begin(), ie = vecAdd.end(); it != ie; ++it)
				(*it)->m_pSurfaceRenderTargetCache = m_poolSurfaceRenderTargets.Get();
			///////////////////////////////////////////////

			//水面特效
			int x, y, i, j, xAdj, yAdj, ci, cj;
			xAdj = viewport.left % SubMap::PGround::WATER_WIDTH;
			yAdj = viewport.top % SubMap::PGround::WATER_HEIGHT;
			x = static_cast<int>(viewport.right / SubMap::PGround::WATER_WIDTH);
			if (x >= m_pMap->m_widthForWater)
			{
				x = m_pMap->m_widthForWater - 1;
			}
			y = static_cast<int>(viewport.bottom  / SubMap::PGround::WATER_HEIGHT);
			if (y >= m_pMap->m_heightForWater)
			{
				y = m_pMap->m_heightForWater - 1;
			}
			ci = 0;
			cj = 0;
			Nuclear::FRECT tmpRect;
			for (i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
				m_pMap->m_waterEffects[i].ClearRects();
			SubMap::WaterTileType *pTmpWater;
			for (j=viewport.top / SubMap::PGround::WATER_HEIGHT; j<=y ; j++, cj++) 
			{
				ci = 0;
				i = viewport.left / SubMap::PGround::WATER_WIDTH;
				pTmpWater =  m_pMap->m_pWaterData + (j * m_pMap->m_widthForWater + i);
				for ( ; i<=x ; i++, ci++, pTmpWater++)
				{
					if (pTmpWater->tileRes > 0)
					{
						tmpRect.left = (ci*SubMap::PGround::WATER_WIDTH - xAdj) * v->GetScale();
						tmpRect.top = (cj*SubMap::PGround::WATER_HEIGHT - yAdj) * v->GetScale();
						tmpRect.right = ((ci+1)*SubMap::PGround::WATER_WIDTH - xAdj) * v->GetScale();
						tmpRect.bottom = ((cj+1)*SubMap::PGround::WATER_HEIGHT - yAdj) * v->GetScale();
						m_pMap->m_waterEffects[pTmpWater->tileRes-1].PushARect(tmpRect, pTmpWater->tileType);
					}
				}
			}

			// 用RenderTargetCache绘制地表层和物件低层
			int l = 0;
			if( m_pEngine->GetConfigManager()->IsUseSurfaceCache() )
				//如果是用RenderTarget来Cache的话，地表和天1就没有必要在这里cache了，
				//但是切换的时候要注意一下，如果静止的时候，从用RenderTarget切换到不用RenderTarget，
				//要重新Cache。最直接的办法就是在切换的时候将camera.dirty设为true，
				//不过camera目前没有暴露这个接口
			{
				l = PIC_CATCH_LAYER_COUNT;
			} else {
				for (; l < PIC_GROUND_LAYER_COUNT; ++l)
				{
					v->m_ground_picele_cache[l].clear();
					for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); 
						it != ie; ++it)
					{
						for(std::vector<Region::PicEle>::const_iterator it2 = (*it)->m_vPicEle[l].begin(),
							ie2 = (*it)->m_vPicEle[l].end(); it2 != ie2; ++it2)
						{
							const Region::PicEle &pic = *it2;
							if (pic.forCache)
								continue;
							PictureHandle handle = pic.pPicResrc->handle;
							if( handle == INVALID_PICTURE_HANDLE) // todo load?
							{
								//XPTRACE(L"invalid picture handle, path is %s\n", pic.pPicResrc->uri.c_str());
								picloading = true;
								continue;
							}
							GroundCachePic p;
							p.handle = handle;
							if (IsRectCross(viewport, pic.pos))
							{
								p.objRect.left = (pic.pos.left - viewport.left) * v->GetScale();
								p.objRect.top = (pic.pos.top - viewport.top) * v->GetScale();
								p.objRect.right = (pic.pos.right - viewport.left) * v->GetScale();
								p.objRect.bottom = (pic.pos.bottom - viewport.top) * v->GetScale();
								if (pic.useColorPos)
								{
									p.color[0].data = m_pMap->m_pGroundColor[pic.colorpos1];
									p.color[1].data = m_pMap->m_pGroundColor[pic.colorpos1 + 1];
									p.color[2].data = m_pMap->m_pGroundColor[pic.colorpos2 + 1];
									p.color[3].data = m_pMap->m_pGroundColor[pic.colorpos2];
								} else {
									p.color[0].data = pic.color;
									p.color[1].data = p.color[0].data;
									p.color[2].data = p.color[0].data;
									p.color[3].data = p.color[0].data;
								}
								v->m_ground_picele_cache[l].push_back(p);
							}
						}
					}
				}
			}

			for(; l < PIC_LAYER_COUNT; ++l)
			{
				if( l == PIC_MID_ELEMENT_LAYER_ID ) continue; // 物件中层忽略
				v->m_picele_cache[l].clear();
				for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); 
					it != ie; ++it)
				{
					for(std::vector<Region::PicEle>::const_iterator it2 = (*it)->m_vPicEle[l].begin(),
						ie2 = (*it)->m_vPicEle[l].end(); it2 != ie2; ++it2)
					{
						const Region::PicEle &pic = *it2;
						if (pic.forCache)
							continue;
						PictureHandle handle = pic.pPicResrc->handle;
						if( handle == INVALID_PICTURE_HANDLE) // todo load?
						{
							//XPTRACE(L"invalid picture handle, path is %s\n", pic.pPicResrc->uri.c_str());
							picloading = true;
							continue;
						}
						CachePic p;
						CRECT rect;
						p.handle = handle;
						CRECT wrect = pic.pos;
						if( wrect.Cut(viewport, rect, p.srcRect) )
						{
							p.color = pic.color;
							p.objRect.left = rect.left * v->GetScale();
							p.objRect.top = rect.top * v->GetScale();
							p.objRect.right = rect.right * v->GetScale();
							p.objRect.bottom = rect.bottom * v->GetScale();
							v->m_picele_cache[l].push_back(p);
						}
					}
				}

			}

			// 地图物件中层
			{
				//img图素
				v->m_mapmobj_cache.clear();
//				v->m_effect_update_cache.clear();
				std::set<LkoTree*> hasAdd;
				std::set<Distort*> hasAddDist;
				for(std::vector<Map::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); 
					it != ie; ++it)
				{
					for(std::vector<Region::PicEleWithBase>::const_iterator it2 = (*it)->m_vPicEleWithBase.begin(),
						ie2 = (*it)->m_vPicEleWithBase.end(); it2 != ie2; ++it2)
					{
						const Region::PicEleWithBase &pic = *it2;
						PictureHandle handle = pic.pPicResrc->handle;
						if( handle == INVALID_PICTURE_HANDLE) // todo load?
						{
							//XPTRACE(L"picture invalid, path is %s\n", pic.pPicResrc->uri.c_str());
							continue;
						}
						VPic vpic;
						CRECT rect;
						vpic.handle = handle;
						CRECT wrect = pic.pos;
						if( wrect.Cut(viewport, rect, vpic.srcrect) )
						{
							vpic.rect.left = rect.left * v->GetScale();
							vpic.rect.top = rect.top * v->GetScale();
							vpic.rect.right = rect.right * v->GetScale();
							vpic.rect.bottom = rect.bottom * v->GetScale();
							vpic.base = pic.pOrg->base;
							vpic.pRenderable = NULL;
							vpic.pObjPic = (void*)&pic;
							//pic.pOrg->trans = false;

							v->m_mapmobj_cache.push_back(vpic);
						}
					}

					//地图特效
					for(std::vector<Region::EffectInfo>::iterator it2 = (*it)->m_effects[MID_ELEMENT_LAYER_ID_FOR_ELEMENT].begin(), ie2 = (*it)->m_effects[MID_ELEMENT_LAYER_ID_FOR_ELEMENT].end();
						it2 != ie2; ++it2)
					{
						Region::EffectInfo &einfo = *it2;
						if (!einfo.validBase)
						{
							XPBase base;
							if( einfo.pEffect->GetBase(base) )
							{
								CPOINT tmpCPoint;
								TransToDiamondRadix((base.left.x+ einfo.pos.x),(base.left.y+ einfo.pos.y),tmpCPoint);
								einfo.base.left = tmpCPoint.x;
								einfo.base.bottom = tmpCPoint.y;
								TransToDiamondRadix((base.right.x+ einfo.pos.x),(base.right.y+ einfo.pos.y),tmpCPoint);
								einfo.base.right = tmpCPoint.x;
								einfo.base.top = tmpCPoint.y;
								einfo.validBase = true;
							}
						}
						if (IsRectCross(v->m_viewport, einfo.rect))
						{
							VPic vpic;
							vpic.base = einfo.base;
							vpic.pRenderable = einfo.pEffect;
							v->m_mapmobj_cache.push_back(vpic);
							//v->m_effect_update_cache.push_back(einfo.pEffect);
						}
					}
					//动态物件 && 飘动物件

					for (std::vector<LkoTree*>::const_iterator
						treeIter = (*it)->m_vpLkoTrees[MID_ELEMENT_LAYER_ID_FOR_ELEMENT].begin(),	//4是物件中层
						treeIE = (*it)->m_vpLkoTrees[MID_ELEMENT_LAYER_ID_FOR_ELEMENT].end();
					treeIter!=treeIE;++treeIter)
					{
						if (hasAdd.find(*treeIter) == hasAdd.end())
						{
							if (IsRectCross((*treeIter)->m_rect, v->m_viewport))
							{
								VPic vpic;
								vpic.base = (*treeIter)->base;
								vpic.pRenderable = *treeIter;
								v->m_mapmobj_cache.push_back(vpic);
							}
							hasAdd.insert(*treeIter);
						}
					}
					for (std::vector<Distort*>::const_iterator
						distIter = (*it)->m_vpDistorts[MID_ELEMENT_LAYER_ID_FOR_ELEMENT].begin(), 
						distIE = (*it)->m_vpDistorts[MID_ELEMENT_LAYER_ID_FOR_ELEMENT].end();
					distIter!=distIE;++distIter)
					{
						if (hasAddDist.find(*distIter) == hasAddDist.end())
						{
							if (IsRectCross((*distIter)->GetRect(), v->m_viewport))
							{
								VPic vpic;
								vpic.base = (*distIter)->base;
								vpic.pRenderable = *distIter;
								v->m_mapmobj_cache.push_back(vpic);
							}
							hasAddDist.insert(*distIter);
						}
					}
				}
			}
			if( !picloading )
				m_camera.m_bDirty = false;
		}
		UpdateDistorts(delta);
		UpdateTrees(delta);
		UpdateTriggers(delta);
		UpdateEffects(delta);

		g_timeuserlog.Push( XPTimeUseLog::TUL_ENGBEFORERENDER);
		for (int layer=0;layer<XPEFFECT_LAYER_COUNT;++layer)
		{

			for(EffectList::iterator it = m_effects[layer].begin(); 
				it != m_effects[layer].end();)
			{
				if( ! ((*it)->Update(delta) ) )
				{
					delete (*it);
					it = m_effects[layer].erase(it);
				}
				else
					++it;
			}
		}

		g_timeuserlog.Push( XPTimeUseLog::TUL_WORLD_UPDATEEFFECT);

		for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
			m_pMap->m_waterEffects[i].Update(delta);

		for(std::list<ImmovableObj*>::iterator it = m_immobjs.begin(), ie = m_immobjs.end();
			it != ie; ++it)
		{
			(*it)->Update(delta);
		}
	}

	
	void World::Render(int now, bool UseCavas)
	{	
		m_topSprEffect.clear();
		if( !m_bDrawWorld )
			return;
	
		Canvas *pCanvas = UseCavas?&m_canvas:NULL;
		Camera* v = &m_camera;
		// 排序地图中间层和精灵
		bool IsInBattle = m_worldMode == XPWM_SIMPLE_BATTLE || m_worldMode == XPWM_WORLD_BATTLE;
		std::vector<VPic> sortlist = v->m_mapmobj_cache;
		std::vector<VPic> battlesortlist;
		std::vector<TopologySortBase> battlesortlist2;
		std::list<Sprite*>::iterator sprIt, sprIe;
		EffectList::iterator effIt, effIe;
		if (IsInBattle)
		{
			sprIt = m_warSprites.begin();
			sprIe = m_warSprites.end();
			effIt = m_warEffects[XPEL_SPRITE].begin();
			effIe = m_warEffects[XPEL_SPRITE].end();
			for(;sprIt != sprIe; ++sprIt)
			{
				Sprite *pSprite = *sprIt;
				if (!pSprite->TestViewport(v->m_viewport))
					continue;
				VPic vpic;
				pSprite->GetBase(vpic.base);
				vpic.pRenderable = pSprite;
				battlesortlist.push_back(vpic);
			}

			//触发特效和别的特效（不在地图编辑器放的中层特效）

			XPBase base;
			Location effLoc;
			for(; effIt != effIe;++effIt)
			{
				Effect* pEffect = *effIt;
				if( pEffect->GetBase(base) )
				{
					effLoc = pEffect->GetLocation();
					VPic vpic;
					CPOINT tmpCPoint;
					TransToDiamondRadix((base.left.x+ effLoc.x),(base.left.y+ effLoc.y),tmpCPoint);
					vpic.base.left = tmpCPoint.x;
					vpic.base.bottom = tmpCPoint.y;
					TransToDiamondRadix((base.right.x+ effLoc.x),(base.right.y+ effLoc.y),tmpCPoint);
					vpic.base.right = tmpCPoint.x;
					vpic.base.top = tmpCPoint.y;
					vpic.pRenderable = pEffect;
					battlesortlist.push_back(vpic);
				}
			}

			int sortn = (int)battlesortlist.size();
			battlesortlist2.resize(sortn);

			for(int i=0; i<sortn; ++i)
			{
				battlesortlist2[i].data = &battlesortlist[i];
				battlesortlist2[i].rect = battlesortlist[i].GetRect();
				battlesortlist2[i].sortBaseID = battlesortlist[i].pRenderable->GetGUID();	
			}
			m_sortlist[XPSL_BATTLE].Dump(battlesortlist2);
		}
		else
		{
			sprIt = m_sprites.begin();
			sprIe = m_sprites.end();
			effIt = m_effects[XPEL_SPRITE].begin();
			effIe = m_effects[XPEL_SPRITE].end();
			for(;sprIt != sprIe; ++sprIt)
			{
				Sprite *pSprite = *sprIt;
				if (!pSprite->TestViewport(v->m_viewport))
					continue;
				VPic vpic;
				pSprite->GetBase(vpic.base);
				vpic.pRenderable = pSprite;
				sortlist.push_back(vpic);
			}

			//触发特效和别的特效（不在地图编辑器放的中层特效）

			XPBase base;
			Location effLoc;
			for(; effIt != effIe;++effIt)
			{
				Effect* pEffect = *effIt;
				if( pEffect->GetBase(base) )
				{
					effLoc = pEffect->GetLocation();
					VPic vpic;
					CPOINT tmpCPoint;
					TransToDiamondRadix((base.left.x+ effLoc.x),(base.left.y+ effLoc.y),tmpCPoint);
					vpic.base.left = tmpCPoint.x;
					vpic.base.bottom = tmpCPoint.y;
					TransToDiamondRadix((base.right.x+ effLoc.x),(base.right.y+ effLoc.y),tmpCPoint);
					vpic.base.right = tmpCPoint.x;
					vpic.base.top = tmpCPoint.y;
					vpic.pRenderable = pEffect;
					sortlist.push_back(vpic);
				}
			}
		}
		
		// 这个排序还是太费时，可能是内存分配问题，需要处理


		std::vector<TopologySortBase> sortlist2(sortlist.size());

		int sortn = (int)sortlist.size();
		static unsigned int testid = 1;
		for(int i=0; i<sortn; ++i)
		{
			sortlist2[i].data = &sortlist[i];
			sortlist2[i].rect = sortlist[i].GetRect();
			if( sortlist[i].pRenderable )
				sortlist2[i].sortBaseID = sortlist[i].pRenderable->GetGUID();
			else
				sortlist2[i].sortBaseID = ((DWORD)sortlist[i].pObjPic) | 0x8000000;
		}
		g_timeuserlog.Push(XPTimeUseLog::TUL_PERPSORT);
		static int sort_tick = 1;
		if( (++sort_tick % 8) == 0 || m_sortCache.empty() )
		{
			if( m_pEngine->GetConfigManager()->IsSortMapObjects() )
				m_sortlist[XPSL_MIDDLE].Dump(sortlist2);
			g_timeuserlog.Push(XPTimeUseLog::TUL_FULLSORT);

		}
		else
		{
			if( m_pEngine->GetConfigManager()->IsSortMapObjects() )
				LocalSort(sortlist2);
			g_timeuserlog.Push(XPTimeUseLog::TUL_LOCALSORT);
		}
		m_sortCache = sortlist2;

		int drawTick = m_pEngine->GetTick();
		static int lastDrawTick = drawTick;
		if (lastDrawTick == drawTick)
			--drawTick;
		lastDrawTick = drawTick;
		Renderer * renderer = m_pEngine->GetRenderer();

		//////////////////////////////////////////////////

		//画图//////////////////////////////////////////
		DrawPictureParam dpParam;
		/////////////////////////////////////////////////////////////
		// 夜晚效果
		//////////////////////////////////计算天黑效果的param
		XPColorBalanceParam timeEffectParam;
		timeEffectParam.fCoefr = 0.0f;
		timeEffectParam.fCoefg = 0.0f;
		timeEffectParam.fCoefb = 0.0f;
		timeEffectParam.fContrastk = 1.0f;
		timeEffectParam.fContrastb = 0.0f;
		timeEffectParam.fLightness = 0.0f;
		timeEffectParam.fCentx = -1000.0f;
		timeEffectParam.fCenty = -1000.0f;
		FPOINT timeEffectPos(-10000.0f, -10000.0f);
		if (v->m_pAttachedSprite && (!IsInBattle))
		{
			CPOINT centerPos = v->m_pAttachedSprite->GetLocation();
			timeEffectPos.x = (float)(centerPos.x - v->m_viewport.left);
			timeEffectPos.y = (float)(centerPos.y - v->m_viewport.top);
		}
		
		if (m_pMap)
		{
			if (m_pMap->m_TimeEffects.size() == 0)//以后可以优化直接画
			{
				timeEffectParam.fCoefr = 0.0f;
				timeEffectParam.fCoefg = 0.0f;
				timeEffectParam.fCoefb = 0.0f;
				timeEffectParam.fContrastk = 1.0f;
				timeEffectParam.fContrastb = 0.0f;
				timeEffectParam.fLightness = 0.0f;
			} else {
				SubMap::TimeEffectParamMap::iterator timeEffectiter = m_pMap->m_TimeEffects.begin();
				for (;timeEffectiter != m_pMap->m_TimeEffects.end();++timeEffectiter)
				{
					if (timeEffectiter->first>=GetGameTime())
						break;
				}
				SubMap::TimeEffectParamMap::iterator preIter = timeEffectiter;
				if (timeEffectiter == m_pMap->m_TimeEffects.end())
				{
					timeEffectiter = m_pMap->m_TimeEffects.begin();
				}
				if (preIter == m_pMap->m_TimeEffects.begin())
				{
					preIter = m_pMap->m_TimeEffects.end();
				}
				preIter--;
				int length = timeEffectiter->first - preIter->first;
				if ((length == 0) || (GetGameTime() == timeEffectiter->first))//只有一个关键帧或者就是当前帧
				{
					timeEffectParam = timeEffectiter->second;
				} else {
					if (length < 0)
						length += XP_A_DAY_TIME;
					int nPos = GetGameTime() - preIter->first;
					if (nPos < 0)
						nPos += XP_A_DAY_TIME;
					timeEffectParam.fCoefr = preIter->second.fCoefr + (timeEffectiter->second.fCoefr - preIter->second.fCoefr) * nPos / length;
					timeEffectParam.fCoefg = preIter->second.fCoefg + (timeEffectiter->second.fCoefg - preIter->second.fCoefg) * nPos / length;
					timeEffectParam.fCoefb = preIter->second.fCoefb + (timeEffectiter->second.fCoefb - preIter->second.fCoefb) * nPos / length;
					timeEffectParam.fContrastk = preIter->second.fContrastk + (timeEffectiter->second.fContrastk - preIter->second.fContrastk) * nPos / length;
					timeEffectParam.fContrastb = preIter->second.fContrastb + (timeEffectiter->second.fContrastb - preIter->second.fContrastb) * nPos / length;
					timeEffectParam.fLightness = preIter->second.fLightness + (timeEffectiter->second.fLightness - preIter->second.fLightness) * nPos / length;
				}	
			}
		}
		if( m_pEngine->GetConfigManager()->IsRenderNightEffectByShader())
		{
			timeEffectParam.fCentx = timeEffectPos.x;
			timeEffectParam.fCenty = timeEffectPos.y;
			if( m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget() )
			{
				if (m_hNightEffectRenderTarget == INVALID_PICTURE_HANDLE)
				{
					XDisplayMode mode = m_pEngine->GetRenderer()->GetDisplayMode();
					m_hNightEffectRenderTarget = renderer->CreateRenderTarget(mode.width,
					mode.height, mode.bitperpix==32?XPTEXFMT_A8R8G8B8:XPTEXFMT_A4R4G4B4);
				}
				renderer->PushRenderTarget(m_hNightEffectRenderTarget);
			}
			else
			{
				if (m_pMap)
				{
					if (m_pMap->m_nSign & PMap::MAP_SIGN_UNDERGROUND_PALACE)
					{
						renderer->SelectRenderEffect(XPRE_COLORBALANCE_2);
						renderer->SetShaderParam(XPRE_COLORBALANCE_2, &timeEffectParam);
					} else {
						renderer->SelectRenderEffect(XPRE_COLORBALANCE);
						renderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam);
					}
				}
			}
		}

		/////////////////////////////////////////////////////
		//std::set<LkoTree*> hasDrawed;
		//stdext::hash_set<LkoTree*> hasDrawed;
		if( m_pMap )
		{
			if (m_worldMode != XPWM_SIMPLE_BATTLE)//如果不是是在简单战斗状态下
			{
				//移动背景
				RegionMap::BackGoundInfoVet::iterator bgit = m_pMap->m_backGroundInfos.begin(), bgie = m_pMap->m_backGroundInfos.end();
				CPOINT viewportCenter = v->m_viewport.Center();
				DrawPictureParam dpp;
				FRECT bgRect;
				dpp.pRect = &bgRect;
				for (;bgit!=bgie;++bgit)
				{
					CRECT res;
					if (v->m_viewport.IsCross(bgit->activeRect, res))
					{
						CPOINT bgVet = viewportCenter - bgit->pos;
						bgVet.x = static_cast<int>(bgVet.x * bgit->relSpeed.x);
						bgVet.y = static_cast<int>(bgVet.y * bgit->relSpeed.y);
						int bgwidth = bgit->girdWidth * bgit->width;
						int bgheight = bgit->girdHeight * bgit->height;
						int renderwidth, renderheight;
						CPOINT pos = bgit->pos + bgVet;
						pos.x -= bgwidth / 2;
						pos.y -= bgheight / 2;
						CPOINT ltpos;
						if (bgit->wrapHorz)
						{
							ltpos.x = 0;
							renderwidth = m_pMap->m_width;
						} else {
							ltpos.x = pos.x;
							renderwidth = bgwidth;
						}
						if (bgit->wrapVert)
						{
							ltpos.y = 0;
							renderheight = m_pMap->m_height;
						} else {
							ltpos.y = pos.y;
							renderheight = bgheight;
						}
						CRECT rect(ltpos, renderwidth, renderheight);
						CRECT objRect;
						if (rect.IsCross(res, objRect))	//objRect是需要画的区域，世界坐标
						{
							//ltpos为要画的区域的左上角坐标，屏幕坐标
							ltpos.x = objRect.left - v->m_viewport.left;
							ltpos.y = objRect.top - v->m_viewport.top;
							objRect -= pos;  //变成移动背景坐标了

							if (objRect.left < 0)
							{
								XPASSERT(bgit->wrapHorz);	//如果不是水平缠绕，rect.left不会小于0
								int n = -(objRect.left + 1) / bgwidth + 1;
								objRect.left += n*bgwidth;
								objRect.right += n*bgwidth;
							}
							if (objRect.left >= bgwidth)
							{
								int n = objRect.left / bgwidth;
								objRect.left -= n*bgwidth;
								objRect.right -= n*bgwidth;
							}
							if (objRect.top < 0)
							{
								XPASSERT(bgit->wrapVert);	//如果不是垂直缠绕，rect.top不会小于0
								int n = -(objRect.top + 1) / bgheight + 1;
								objRect.top += n*bgheight;
								objRect.bottom += n*bgheight;
							}
							if (objRect.top >= bgheight)
							{
								int n = objRect.top / bgheight;
								objRect.top -= n*bgheight;
								objRect.bottom -= n*bgheight;
							}
							int x, y, w, h, tx, ty, bx;
							bx = objRect.left / bgit->girdWidth;
							w = (objRect.right + bgit->girdWidth - 1) / bgit->girdWidth;
							y = objRect. top / bgit->girdHeight;
							h = (objRect.bottom + bgit->girdHeight - 1) / bgit->girdHeight;
							ltpos.x -= objRect.left % bgit->girdWidth;
							ltpos.y -= objRect.top % bgit->girdHeight;
							bgRect.top = ltpos.y * v->GetScale();
							bgRect.bottom = bgRect.top + bgit->girdHeight * v->GetScale();
							for (ty=y;y<h;++y,++ty)
							{
								if (ty >= bgit->height) ty = 0;
								PicResrc **ppPicResrcs = bgit->ppPicResrcs + ty * bgit->width + bx;
								bgRect.left = ltpos.x * v->GetScale();
								bgRect.right = bgRect.left + bgit->girdWidth * v->GetScale();
								for (x=tx=bx;x<w;++x,++ppPicResrcs,++tx)
								{
									if (tx >= bgit->width)
									{
										tx = 0;
										ppPicResrcs = bgit->ppPicResrcs + ty * bgit->width;
									}
									if (*ppPicResrcs)
									{
										dpp.handle = (*ppPicResrcs)->handle;

										renderer->DrawPicture(dpp);
									}
									bgRect.left = bgRect.right;
									//可能会有累积误差，先看看
									bgRect.right += bgit->girdWidth * v->GetScale();
								}
								bgRect.top = bgRect.bottom;
								bgRect.bottom += bgit->girdHeight * v->GetScale();

							} 
						}
					}
				}
				g_timeuserlog.Push(XPTimeUseLog::TUL_MOVINGBACKGROUND);

				// 用RenderTargetCache绘制地表层和物件低层
				if( m_pEngine->GetConfigManager()->IsUseSurfaceCache() )
				{
					CRECT objrect;
					FRECT frect, srcrect;
					int iRows = m_pMap->m_currentRegion.Height()/Map::REGION_HEIGHT;
					int iCols = m_pMap->m_currentRegion.Width()/Map::REGION_WIDTH;
					for(int iRow = 0; iRow < iRows; ++iRow) for(int iCol = 0; iCol < iCols; ++iCol)
					{
						Map::RegionInfo *pRegion = m_pMap->m_curRegions[iCol + iRow * iCols];
						CRECT wrect(CPOINT(iCol*Map::REGION_WIDTH+m_pMap->m_currentRegion.left,
							iRow*Map::REGION_HEIGHT+m_pMap->m_currentRegion.top), Map::REGION_WIDTH, Map::REGION_HEIGHT);
						if( pRegion->m_pSurfaceRenderTargetCache == NULL ) 
							continue;
						if( !pRegion->m_pSurfaceRenderTargetCache->m_bRendered )
						{
							renderer->PushRenderTarget(pRegion->m_pSurfaceRenderTargetCache->m_hRenderTarget);
							renderer->Clear(0);
							XPRENDER_EFFECT oldEffect = renderer->GetRenderEffect();
							renderer->SelectRenderEffect(XPRE_NULL);
							bool bNotComplete = false;
							// render to region
							XPCOLOR color[4];
							dpParam.pColor = color;
							for(int iLayer = 0; iLayer<PIC_CATCH_LAYER_COUNT;++iLayer)
							{
								for(std::vector<Region::PicEle>::const_iterator it2 = pRegion->m_vPicEle[iLayer].begin(),
									ie2 = pRegion->m_vPicEle[iLayer].end(); it2 != ie2; ++it2)
								{
									const Region::PicEle &pic = *it2;
									if( pic.pPicResrc->handle == INVALID_PICTURE_HANDLE ) 
									{
										bNotComplete = true;
										continue;
									}
									FRECT rect(FPOINT((float)pic.pos.left-wrect.left, (float)pic.pos.top-wrect.top), (float)pic.pos.Width(), (float)pic.pos.Height());
									dpParam.handle = pic.pPicResrc->handle;
									dpParam.pRect = &rect;
									if (pic.useColorPos)
									{
										dpParam.colorCount = 4;
										color[0].data = m_pMap->m_pGroundColor[pic.colorpos1];
										color[1].data = m_pMap->m_pGroundColor[pic.colorpos1 + 1];
										color[2].data = m_pMap->m_pGroundColor[pic.colorpos2 + 1];
										color[3].data = m_pMap->m_pGroundColor[pic.colorpos2];
									} else {
										dpParam.colorCount = 1;
										color[0].data = pic.color;
									}

									//可以考虑cut一下wrect
									renderer->DrawPicture(dpParam);

								}
							}
							renderer->SelectRenderEffect(oldEffect);
							renderer->PopRenderTarget();
							if( !bNotComplete )
								pRegion->m_pSurfaceRenderTargetCache->m_bRendered = true;
						}
						// render region
						if( wrect.Cut(m_camera.GetViewport(), objrect, srcrect) )
						{
							DrawPictureParam dpParam2;
							frect.left = objrect.left * v->GetScale();
							frect.top = objrect.top * v->GetScale();
							frect.right = objrect.right * v->GetScale();
							frect.bottom = objrect.bottom * v->GetScale();
							dpParam2.handle = pRegion->m_pSurfaceRenderTargetCache->m_hRenderTarget;
							dpParam2.pRect = &frect;
							dpParam2.pSrcrect = &srcrect;
							dpParam2.pColor = NULL;
							dpParam2.blend = Nuclear::XPTEXBM_COPY;

							renderer->DrawPicture(dpParam2);
						}
					}
				}
				//
				else
				{
					// 传统画法画地表层
					dpParam.pSrcrect = NULL;
					int i = 0;
					for (; i < PIC_GROUND_LAYER_COUNT; ++i)
					{
						for(std::vector<GroundCachePic>::const_iterator it = v->m_ground_picele_cache[i].begin(),
							ie = v->m_ground_picele_cache[i].end(); it != ie; ++it)
						{
							dpParam.handle = it->handle;
							dpParam.pRect = &(it->objRect);
							dpParam.colorCount = 4;
							dpParam.pColor = it->color;
							renderer->DrawPicture(dpParam);
						}	
					}
					// 传统画法画水1 水2
					for(; i<PIC_CATCH_LAYER_COUNT; ++i)
					{
						for(std::vector<CachePic>::const_iterator it = v->m_picele_cache[i].begin(),
							ie = v->m_picele_cache[i].end(); it != ie; ++it)
						{
							dpParam.handle = it->handle;
							dpParam.pRect = &(it->objRect);
							dpParam.pSrcrect = &(it->srcRect);
							dpParam.colorCount = 1;
							dpParam.pColor = &(it->color);

							renderer->DrawPicture(dpParam);
						}				
					}
				}
				g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERCACHERENDERTARGET);
				//画水的在水2和低1之间
				for (int j=0;j<SubMap::PGround::WATER_EFFECT_COUNT;++j)
					m_pMap->m_waterEffects[j].Render();
				// 画 低1 低2 低3 低4 共4层

				for(int i=PIC_CATCH_LAYER_COUNT; i<PIC_MID_ELEMENT_LAYER_ID; ++i)
				{
					for(std::vector<CachePic>::const_iterator it = v->m_picele_cache[i].begin(),
						ie = v->m_picele_cache[i].end(); it != ie; ++it)
					{
						dpParam.handle = it->handle;
						dpParam.pRect = &(it->objRect);
						dpParam.pSrcrect = &(it->srcRect);
						dpParam.colorCount = 1;
						dpParam.pColor = &(it->color);

						renderer->DrawPicture(dpParam);
					}
					int nLayerID = i-PIC_FIRST_ELEMENT_ID;
					if ( true )
					{

						//不用cache effect、lko和distort文件，直接画
						for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); 
							it != ie; ++it)
						{
							for(std::vector<Region::EffectInfo>::iterator 
								it2 = (*it)->m_effects[nLayerID].begin(), 
								ie2 = (*it)->m_effects[nLayerID].end();
							it2 != ie2; ++it2)
							{
								Region::EffectInfo &einfo = *it2;
								if (IsRectCross(v->m_viewport, einfo.rect))
								{
									einfo.pEffect->Render(NULL);
								}

							}

							for (std::vector<LkoTree*>::const_iterator
								treeIter = (*it)->m_vpLkoTrees[nLayerID].begin(),
								treeIE = (*it)->m_vpLkoTrees[nLayerID].end();
							treeIter!=treeIE;++treeIter)
							{
								if ((*treeIter)->lastDrawTick != drawTick)
								{
									(*treeIter)->Render(NULL);
									(*treeIter)->lastDrawTick = drawTick;
								}
							}
							for (std::vector<Distort*>::const_iterator
								disIter = (*it)->m_vpDistorts[nLayerID].begin(), 
								disIE = (*it)->m_vpDistorts[nLayerID].end();
							disIter!=disIE;++disIter)
							{
								if ((*disIter)->lastDrawTick != drawTick)
								{
									(*disIter)->Render(NULL);
									(*disIter)->lastDrawTick = drawTick;
								}
							}
						}
					}
				}
			}//if (!m_bIsInWar) 
		}
		renderer->JustEnd();
		renderer->JustBegin();

		g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERLOWLAYER);
		// 画矿和掉落物品
		if (!IsInBattle)
		{
			for(std::list<ImmovableObj*>::iterator it = m_immobjs.begin(), ie = m_immobjs.end();
				it != ie; ++it)
			{
				(*it)->Render(pCanvas);
			}
			renderer->BatchDrawParticles();
		}
		if( m_pMap )
		{
			//画精灵之下地表之上特效
			EffectList::iterator eIt, eIe;
			if (!IsInBattle)
			{
				eIt = m_effects[XPEL_UNDER_SPRITE].begin();
				eIe = m_effects[XPEL_UNDER_SPRITE].end();
				for(; eIt != eIe; ++eIt)
				{
					(*eIt)->Render(NULL);
				}
			}
			g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERBEFOREMID);
			// 画物件中层和精灵
			DrawPictureParam spriteParam;
			FRECT spriteRect, spriteSrcRect;
			spriteParam.pRect = &spriteRect;
			spriteParam.pSrcrect = &spriteSrcRect;
			for(std::vector<TopologySortBase>::const_iterator it = sortlist2.begin(),ie = sortlist2.end(); it != ie; ++it)
			{
				VPic *p = (VPic*)it->data;
				if( p->pRenderable == NULL )
				{
					XPCOLOR color = ((Region::PicEleWithBase*)p->pObjPic)->color;
					color.a = 0xff;
					dpParam.handle = p->handle;
					dpParam.pRect = &(p->rect);
					dpParam.pSrcrect = &(p->srcrect);
					dpParam.pColor = &color;
					dpParam.colorCount = 1;
					renderer->DrawPicture(dpParam);
				}
				else
				{
					p->pRenderable->Render(pCanvas);
				}
			}
			g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERMID);
			if (m_pEngine->GetConfigManager()->IsRenderAlphaSprite() && (!IsInBattle))
			{
				//画最上面的半透明贴图
				FRECT rect(0.0f, 0.0f, (float)renderer->GetDisplayMode().width, (float)renderer->GetDisplayMode().height);

				renderer->DrawPicture(m_pEngine->GetAlphaSpritePictureHandle(), rect, 0x50FFFFFF);
			}

			renderer->JustEnd();
			renderer->JustBegin();
			// 画精灵之上特效
			if (!IsInBattle)
			{
				eIt = m_effects[XPEL_ABOVE_SPRITE].begin();
				eIe = m_effects[XPEL_ABOVE_SPRITE].end();
				for(; eIt != eIe; ++eIt)
				{
					(*eIt)->Render(NULL);
				}
			}
			
			if ( m_worldMode != XPWM_SIMPLE_BATTLE)
			{
				g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERAFTERMID);

				// 画 地图物件高层
				dpParam.pColor = NULL;
				dpParam.colorCount = 0;
				for(int i=PIC_MID_ELEMENT_LAYER_ID+1; i<PIC_LAYER_COUNT; ++i)
				{
					int nLayerID = i-PIC_FIRST_ELEMENT_ID;
					for(std::vector<CachePic>::const_iterator it = v->m_picele_cache[i].begin(),
						ie = v->m_picele_cache[i].end(); it != ie; ++it)
					{
						dpParam.handle = it->handle;
						dpParam.pRect = &(it->objRect);
						dpParam.pSrcrect = &(it->srcRect);
						dpParam.colorCount = 1;
						dpParam.pColor = &(it->color);

						renderer->DrawPicture(dpParam);
					}
					//不用cache lko文件，直接画
					for(std::vector<Map::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); 
						it != ie; ++it)
					{
						for(std::vector<Region::EffectInfo>::iterator 
							it2 = (*it)->m_effects[nLayerID].begin(), 
							ie2 = (*it)->m_effects[nLayerID].end();
						it2 != ie2; ++it2)
						{
							Region::EffectInfo &einfo = *it2;
							if (IsRectCross(v->m_viewport, einfo.rect))
							{
								einfo.pEffect->Render(NULL);
							}

						}

						for (std::vector<LkoTree*>::const_iterator
							treeIter = (*it)->m_vpLkoTrees[nLayerID].begin(),
							treeIE = (*it)->m_vpLkoTrees[nLayerID].end();
						treeIter!=treeIE;++treeIter)
						{
							if ((*treeIter)->lastDrawTick != drawTick)
							{
								(*treeIter)->Render(NULL);
								(*treeIter)->lastDrawTick = drawTick;
							}
						}
						for (std::vector<Distort*>::const_iterator
							disIter = (*it)->m_vpDistorts[nLayerID].begin(),
							disIE = (*it)->m_vpDistorts[nLayerID].end();
						disIter!=disIE;++disIter)
						{
							if ((*disIter)->lastDrawTick != drawTick)
							{
								(*disIter)->Render(NULL);
								(*disIter)->lastDrawTick = drawTick;
							}
						}
					}	
				}
			} //if ( m_worldMode != XPWM_SIMPLE_BATTLE)
			if (!IsInBattle)
			{
				// 画最上层特效
				for(EffectList::iterator it = m_effects[XPEL_TOP].begin(); 
					it != m_effects[XPEL_TOP].end();++it)
				{
					(*it)->Render(NULL);
				}
				g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERHIGHT);
			} else { //if (!IsInBattle)
				//画战斗背景
				if (m_warBackGroundPic != INVALID_PICTURE_HANDLE)
				{
					FPOINT lt((v->GetViewport().Width() - m_nWarBackGroundPicWidth) / 2.0f, 
						(v->GetViewport().Height() - m_nWarBackGroundPicHeight) / 2.0f);
					FRECT rect(lt, (float)m_nWarBackGroundPicWidth, (float)m_nWarBackGroundPicHeight);
					renderer->DrawPicture(m_warBackGroundPic, rect, 0xFFFFFFFF);
				}
				//画精灵之下地表之上特效
				eIt = m_warEffects[XPEL_UNDER_SPRITE].begin();
				eIe = m_warEffects[XPEL_UNDER_SPRITE].end();
				for(; eIt != eIe; ++eIt)
				{
					(*eIt)->Render(NULL);
				}
				//画战斗场景
				for(std::vector<TopologySortBase>::const_iterator it = battlesortlist2.begin(),ie = battlesortlist2.end(); it != ie; ++it)
				{
					VPic *p = (VPic*)it->data;
					if( p->pRenderable != NULL )
					{
						p->pRenderable->Render(pCanvas);
					}
				}
				// 画精灵之上特效
				eIt = m_warEffects[XPEL_ABOVE_SPRITE].begin();
				eIe = m_warEffects[XPEL_ABOVE_SPRITE].end();
				for(; eIt != eIe; ++eIt)
				{
					(*eIt)->Render(NULL);
				}

			}//if (!IsInBattle)

			//画精灵身上的最上层特效
			eIt = m_topSprEffect.begin();
			eIe = m_topSprEffect.end();
			for(; eIt != eIe; ++eIt)
			{
				(*eIt)->Render(NULL);
			}

			m_topSprEffect.clear();

			if( m_pEngine->GetConfigManager()->IsRenderNightEffectByShader())
			{
				//renderer->SelectRenderEffect(XPRE_NULL);///!!!!!!!

				/////////////////////////////////////////////////
				if( m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget() )
				{
					renderer->PopRenderTarget();
					//m_timeEffectiter
					DrawPictureParam dpParam;
					FRECT rect = GetViewport().ToFRECT();
					rect.bottom -= rect.top;
					rect.top = 0.0f;
					rect.right -= rect.left;
					rect.left = 0.0f;

					dpParam.handle = m_hNightEffectRenderTarget;
					dpParam.pRect = &rect;
					XPCOLOR color = 0xffffffff;
					dpParam.pColor = &color;
					dpParam.colorCount = 1;
					dpParam.blend = XPTEXBM_COPY;
					//timeEffectParam.nColorOpType = XPCOLOROP_SELECTARG1;

					if (m_pMap->m_nSign & PMap::MAP_SIGN_UNDERGROUND_PALACE)
					{
						renderer->SelectRenderEffect(XPRE_COLORBALANCE_2);
						renderer->SetShaderParam(XPRE_COLORBALANCE_2, &timeEffectParam);
					} else {
						renderer->SelectRenderEffect(XPRE_COLORBALANCE);
						renderer->SetShaderParam(XPRE_COLORBALANCE, &timeEffectParam);
					}
					
					renderer->DrawPicture(dpParam);//DrawPicture(m_timeEffectHandle, rect, 0xffffffff);
				}
				renderer->SelectRenderEffect(XPRE_NULL);
				//renderer->RenderColorBalance(m_timeEffectHandle, rect, param);
				//		int tickEnd = (int)GetMilliSeconds();
				//		XPTRACE(L"World::Render()--RenderColorBalance, use time %d ms\n", tickEnd- tickStart);
				//	XPTRACE(L"World::Render()--DrawPicture(m_timeEffectHandle), use time %d ms\n", tickEnd- tickStart);
			}

			/////////////////////////////////////////////////////////////
			// 叠图效果
			if (m_pEngine->GetConfigManager()->IsEnableMaskPic() && m_pMap)
			{
				//////////////////////////////////计算叠图效果的param
				if (m_pMap->m_maskMap.size() > 0)
				{
					XPMaskParam maskParam;
					maskParam.alpha = 0;
					maskParam.lastAlpha = 0;
					SubMap::MaskParamMap::iterator beginIter = m_pMap->m_maskMap.begin(),
						endIter = m_pMap->m_maskMap.end();
					SubMap::MaskParamMap::iterator maskIter = beginIter, imgIter = endIter;
					for (;maskIter!=endIter;++maskIter)
					{
						if (maskIter->first>=GetGameTime())
							break;
						if (maskIter->second.picuri.size() > 0)
							imgIter = maskIter;
					}
					SubMap::MaskParamMap::iterator preIter = maskIter;
					if (maskIter == endIter)
					{
						maskIter = beginIter;
					}
					if (preIter == beginIter)
					{
						preIter = endIter;
					}
					preIter--;
					int length = maskIter->first - preIter->first;
					if ((length == 0) || (GetGameTime() == maskIter->first))//只有一个关键帧或者就是当前帧
					{
						maskParam = maskIter->second;
						imgIter = maskIter;
					} else {
						if (length < 0)
							length += XP_A_DAY_TIME;
						int nPos = GetGameTime() - preIter->first;
						if (nPos < 0)
							nPos += XP_A_DAY_TIME;
						maskParam.alpha = preIter->second.alpha + (maskIter->second.lastAlpha - preIter->second.alpha) * nPos / length;
						maskParam.blendMode = preIter->second.blendMode;
					}
					if (imgIter == endIter)
					{
						imgIter = preIter;
						size_t i = m_pMap->m_maskMap.size();
						while (imgIter->second.picuri.size() == 0 && i > 0)
						{
							--i;
							if (imgIter == beginIter)
							{
								imgIter = endIter;
							}
							--imgIter;
						}
					}

					if (imgIter->second.picuri != m_maskPicUri)
					{
						m_maskPicUri = imgIter->second.picuri;
						if (m_maskPicHandle != INVALID_PICTURE_HANDLE)
						{
							renderer->FreePicture(m_maskPicHandle);
							m_maskPicHandle = INVALID_PICTURE_HANDLE;
						}
					}

					if (m_maskPicHandle == INVALID_PICTURE_HANDLE && m_maskPicUri.size() != 0)
					{
						m_maskPicHandle = renderer->LoadPicture(m_maskPicUri);
					}

					if (m_maskPicHandle != INVALID_PICTURE_HANDLE)
					{
						Nuclear::DrawPictureParam param;
						param.handle = m_maskPicHandle;
						Nuclear::FRECT rect(0.0f, 0.0f, (float)v->m_viewport.Width(), (float)v->m_viewport.Height());
						Nuclear::FRECT srcrect;
						srcrect.left = static_cast<float>(v->m_viewport.left) / m_pMap->m_width;
						srcrect.top = static_cast<float>(v->m_viewport.top) / m_pMap->m_height;
						srcrect.right = static_cast<float>(v->m_viewport.right) / m_pMap->m_width;
						srcrect.bottom = static_cast<float>(v->m_viewport.bottom) / m_pMap->m_height;
						Nuclear::XPCOLOR color;
						param.pRect = &rect;
						param.pSrcrect = &srcrect;
						param.pColor = &color;
						param.blend = maskParam.blendMode;
						switch (maskParam.blendMode)
						{
						case Nuclear::XPTEXBM_MODULATE:
						case Nuclear::XPTEXBM_ADDITIVE_ALPHA:
						case Nuclear::XPTEXBM_MODULATE2X:
							color.a = maskParam.alpha;
							break;
						case Nuclear::XPTEXBM_MUTIPLY_ALPHA:
							color.a = color.r = color.g = color.b = maskParam.alpha;
							break;
						}
						renderer->DrawPicture(param);
					}
				}
				g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERMASK);
			}
			
			/////////////////////////////////////////////////////

			// 画迷宫，调试输出。
			RenderMaze();

		}
		//m_bIsLostDevice = false;
		g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERWORLDEND);
	}

	/*
	 * 尝试性能优化的函数
	 * 使用m_sortMap
	 * 根据上轮排序结果排序本次待排序的数据
	 * 新插入的数据只处理和整理结果中底座相同的物件
	 */
	void World::LocalSort(std::vector<TopologySortBase> &v)
	{
		//初始化map 将待排序数据加入到map中
		int pos = 0, n = (int)v.size();
		for(int i=0; i<n; ++i){
			m_sortMap.insert(std::make_pair(v[i].sortBaseID, i));
		}

		int index=0;
		//stdext::hash_map<SortBaseID,int>::iterator ir;
		std::map<SortBaseId,int>::iterator ir;
		for(std::vector<TopologySortBase>::const_iterator it = m_sortCache.begin(), ie = m_sortCache.end(); it != ie; ++it)
		{			
			index = -1;
			ir = m_sortMap.find(it->sortBaseID);
			if( ir == m_sortMap.end()){				
			}else{
				index=ir->second;
			}

			if( index != -1 ){
				if( index!= pos ){
					//交换数据，更新sortMap中的索引信息
					std::swap(v[pos],v[index]);			
					m_sortMap[v[index].sortBaseID]=index;
				}
				++pos;
			}			
		}
		//XPTRACE(L"\t\terase %d of %d\n", n-pos,n);
		//清空map
		m_sortMap.clear();	
		 
		int n2=n;
		while(pos<n2)
		{
			int fj = -1;
			for(int j=0; j<pos; ++j)
			{
				if( v[j].rect == v[pos].rect )
				{
					fj = j;
					break;
				}
			}
			if( fj == -1 )
			{
				std::swap(v[pos], v[n2-1]);
				n2--;
			}
			else
			{
				TopologySortBase tmp = v[pos];
				for(int k=pos; k>fj;--k)
					v[k] = v[k-1];
				v[fj] = tmp;
				++pos;
			}
		}
		v.erase(v.begin()+pos,v.end());
		//XPTRACE(L"\terase %d of %d\n", n-pos,n);
	}

	void World::DrawInfo()
	{
		if( m_pMap == NULL ) return;
		const int fheight = 20;
		RECT trect;
		trect.left = 0;
		trect.top =400;
		trect.right =  500;
		trect.bottom = 400 + fheight;
		Renderer *pRenderer = m_pEngine->GetRenderer();
		wchar_t buffer[64];

		wsprintf(buffer, L"Sprite Count : %d", m_sprites.size());
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

		size_t effectSize = 0;
		for (int layer=0;layer<XPEFFECT_LAYER_COUNT;++layer)
		{
			effectSize += m_effects[layer].size();
		}
		for (int layer=0;layer<XPEFFECT_WAR_LAYER_COUNT;++layer)
		{
			effectSize += m_warEffects[layer].size();
		}
		wsprintf(buffer, L"Effect in world Count : %d", effectSize);
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

		wsprintf(buffer, L"Sorted Objects Count : %d", m_sortCache.size());
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

		wsprintf(buffer, L"Map Static Element Count : %d", m_picResrcs.size());
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

		wsprintf(buffer, L"Map Active Element Count : %d", m_activePicResrcs.size());
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

		wsprintf(buffer, L"Map Active Effect Count : %d", m_activeEffects.size());
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

		wsprintf(buffer, L"Viewport        : (%d,%d) (%d,%d,%d,%d)"
			,m_camera.GetViewport().Width(), m_camera.GetViewport().Height()
			,m_camera.GetViewport().left,m_camera.GetViewport().top
			,m_camera.GetViewport().right, m_camera.GetViewport().bottom			
			);
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

		wsprintf(buffer, L"Current  Region : (%d,%d) (%d,%d,%d,%d) (%d)"
			,m_pMap->m_currentRegion.Width(), m_pMap->m_currentRegion.Height()
			, m_pMap->m_currentRegion.left, m_pMap->m_currentRegion.top
			,m_pMap->m_currentRegion.right, m_pMap->m_currentRegion.bottom
			,m_pMap->m_curRegions.size());
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

		wsprintf(buffer, L"Loaded  Region  : (%d,%d) (%d,%d,%d,%d) (%d)"
			,m_pMap->m_loadingRegion.Width(), m_pMap->m_loadingRegion.Height()
			, m_pMap->m_loadingRegion.left, m_pMap->m_loadingRegion.top
			,m_pMap->m_loadingRegion.right, m_pMap->m_loadingRegion.bottom
			,m_pMap->m_nLoadingRegion);
		trect.top += fheight;
		trect.bottom += fheight;
		pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff80);

	}

	void World::OnRendererRestore()
	{
		m_poolSurfaceRenderTargets.Reset(GetSurfaceCacheCountByView(m_camera.GetViewport()),Map::REGION_WIDTH,Map::REGION_HEIGHT);
		if( m_pMap )
		{
			
			m_activePicResrcs.clear();
			for(PicResrcMap::iterator it= m_picResrcs.begin(), ie= m_picResrcs.end();
				it != ie; ++it)
			{
				if( it->second->pooltype == XPPOOL_VIDEOMEM )
					it->second->handle = INVALID_PICTURE_HANDLE;
			}
			/*std::set<PicResrc*>::iterator it = m_activePicResrcs.begin();
			while( it != m_activePicResrcs.end())
			{
				if( (*it)->handle == INVALID_PICTURE_HANDLE )
					it = m_activePicResrcs.erase(it);
				else
					++it;
			}*/
			for(std::vector<RegionMap::RegionInfo>::iterator it =m_pMap->m_regions.begin(), ie = m_pMap->m_regions.end(); it != ie;++it)
				(*it).m_pSurfaceRenderTargetCache = NULL;
			m_pMap->AssureRegionResource(m_camera.GetViewport(), false, false, false, 0);
			m_camera.m_bDirty = true;
		}

		m_hNightEffectRenderTarget = INVALID_PICTURE_HANDLE;
		if( m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget() )
		{
			const XDisplayMode& mode = m_pEngine->GetRenderer()->GetDisplayMode();
			m_hNightEffectRenderTarget = m_pEngine->GetRenderer()->CreateRenderTarget(mode.width,
				mode.height, mode.bitperpix==32?XPTEXFMT_A8R8G8B8:XPTEXFMT_A4R4G4B4);
		}
	}

	void World::SetViewport(const Location &loc/* 目的视窗中心，也就是要传送主角到的位置*/, bool syncRsrc/* 是否先加载资源*/)
	{
		if( m_pMap == NULL ) return;
		Location lt = m_camera.GetLTPosFromCenterPos(loc, m_pMap->m_width, m_pMap->m_height);

		m_camera.SetPosition(lt.x, lt.y);
		m_sortCache.clear();

		if( syncRsrc )
		{
			m_pMap->AssureRegionResource(m_camera.GetViewport(), false, false, false, 0);
		}

		if( m_camera.m_pAttachedSprite )
			m_camera.m_pAttachedSprite->SetLocation(loc);
	}

	void World::SetViewportLT(int left, int top) // 为编辑器添加，不绑精灵的时候用此函数移动视口；
	{
		if (m_pMap)
		{
			if (left < 0) left = 0;
			if (top < 0) top = 0;
			CRECT vp = m_camera.GetViewport();
			if (left + vp.Width() > m_pMap->m_width)
				left = m_pMap->m_width - vp.Width();
			if (top + vp.Height() > m_pMap->m_height)
				top = m_pMap->m_height - vp.Height();
		}
		m_camera.SetPosition(left, top);
		if( m_pMap )
			m_pMap->AssureRegionResource(m_camera.GetViewport(), false, false, false, 0);
	}

	//rect左闭右开上闭下开，格子坐标
	bool World::SetMazeMask(const unsigned char* pData, const CRECT &rect)
	{
		if( m_pMap == NULL )
			return false;
		return m_pMap->m_maze.setMask(pData, rect);
	}

	unsigned int World::GetMazeMask(int x, int y)
	{
		if( m_pMap == NULL ) 
			return 0xFFFFFFFF;	//所有的障碍位都设置上。
		return m_pMap->getMaze().getMask(x / astar::PathFinder::eGridSize, y / astar::PathFinder::eGridSize);
	}

	bool World::GetAStartPath(astar::Path& path, const Nuclear::Location& start, const Nuclear::Location& end)
	{
		astar::APath apath;
		astar::Point pointStart( static_cast<float>(start.x), static_cast<float>(start.y));
		if ( this->m_pEngine->GetPathFinder()->find( pointStart, end.x, end.y, 
			this->m_pEngine->GetConfigManager()->GetSpriteMoveSmoothLimit(), apath, path) )
		{
			path.insert( path.begin(), pointStart);
			return true;
		}
		return false;
	}

	void World::RenderMaze()
	{
		if( m_pMap == NULL ) return;

		// coordinate
		const float N = this->m_pEngine->GetConfigManager()->GetLogicToWorldScaleX();
		const float DX = astar::PathFinder::eGridSize * N;
		const float DY = astar::PathFinder::eGridSize;

		// drar grid
		if (this->m_pEngine->GetConfigManager()->IsShowMazeGrid())
		{
			Renderer * renderer = this->m_pEngine->GetRenderer();
			const CRECT  viewport = this->m_pEngine->GetViewport();
			const CPOINT view = viewport.GetPos();
			const DWORD  color = 0x3f00ffff;

			const float w = (float)viewport.Width();
			const float h = (float)viewport.Height();
			const float dx = DX - viewport.left % (int)DX;
			const float dy = DY - viewport.top % (int)DY;

			std::vector<FRECT> lines;
			for (float i = dx; i <= w; i += DX) { lines.push_back(FRECT(i, 0, i, h)); }
			for (float i = dy; i <= h; i += DY) { lines.push_back(FRECT(0, i, w, i)); }
			if (!lines.empty())
				renderer->DrawLine(&lines[0], int(lines.size()), color);
		}

		// draw Maze
		if (this->m_pEngine->GetConfigManager()->GetMazeMask() != 0)
		{
			Renderer * renderer = this->m_pEngine->GetRenderer();
			const CRECT  viewport = this->m_pEngine->GetViewport();
			const FPOINT view = viewport.GetPos().ToFPOINT();

			const CRECT mazeRect = CRECT(int(viewport.left/DX), int(viewport.top/DY), int(viewport.right/DX), int(viewport.bottom/DY));
			//std::vector<FRECT> rects_unmove_and_unattack;   // red   0x3 0x3fff0000
			//std::vector<FRECT> rects_unmove;                // green 0x1 0x3f04fd16
			std::vector<std::vector<FRECT>> mazerects;
			mazerects.resize(m_mazeColors.size());
			XPMazeColors::iterator cit, cie = m_mazeColors.end();
			int mazecolorid;
			for (int y = mazeRect.top - 1; y <= mazeRect.bottom; ++y)
			{
				for (int x = mazeRect.left - 1; x <= mazeRect.right; ++x) // x inner loop. performance with maze
				{
					unsigned int mask = m_pMap->getMaze().getMask(x, y);
					for (cit=m_mazeColors.begin(),mazecolorid=0;cit!=cie;++cit,++mazecolorid)
					{
						if ((mask & cit->mask) == cit->mask)
						{
							mazerects[mazecolorid].push_back(FRECT(FPOINT(x * DX, y * DY), DX, DY)-view);
							break;
						}
					}
				}
			}
			for (mazecolorid=0,cit=m_mazeColors.begin(),cie=m_mazeColors.end();cit!=cie;++cit,++mazecolorid)
			{
				if (!mazerects[mazecolorid].empty())
				{
					renderer->DrawBox(&mazerects[mazecolorid][0], (int)mazerects[mazecolorid].size(), cit->color);
				}
			}
		}
	}

	IImmovableObj* World::NewImmovableObj(const std::wstring &objname, int layer/*目前必须为0，只有地表面一层*/, XPCOLOR color, float freq) 
	{ 
		if( layer != 0 ) return NULL;

		ImmovableObj *tpObj =  new ImmovableObj(m_pEngine, objname, color, freq);
		m_immobjs.push_back(tpObj);

		return tpObj; 
	}

	void World::DeleteImmovableObj()
	{
		std::list<ImmovableObj*>::iterator it = m_immobjs.begin(), ie=m_immobjs.end();
		std::set<IImmovableObj*>::iterator sie = m_willBeDeletedImmovableObjs.end();
		for (;it!=ie;)
		{
			if (m_willBeDeletedImmovableObjs.find(*it) != sie)
			{
				delete (*it);
				it = m_immobjs.erase(it);
			} else {
				++it;
			}
		}
		m_willBeDeletedImmovableObjs.clear();
	}

	void World::DeleteImmovableObj(IImmovableObj* immobj) 
	{
		m_willBeDeletedImmovableObjs.insert(immobj);
	}

	void World::DeleteAllImmovableObj()
	{
		m_willBeDeletedImmovableObjs.insert(m_immobjs.begin(), m_immobjs.end());
	}

	unsigned int World::GetWaterDepth(const Location& pt) const
	{
		if (!m_pMap)
			return 0;
		int x = pt.x / Map::REGION_WIDTH;
		int y = pt.y / Map::REGION_HEIGHT;
		int regioncol = m_pMap->m_regioncol;
		std::vector<Region::WaterArea> &areas = m_pMap->m_regions[x + y * regioncol].m_vWaterArea;
		std::vector<Region::WaterArea>::iterator iter;
		int border;
		unsigned int result = 0;
		for (iter=areas.begin();iter!=areas.end();++iter)
		{
			if (result < iter->depth && iter->rect.PtInRect(pt))
			{
				border = iter->border;
				if (pt.x - iter->rect.left < border)
					border = pt.x - iter->rect.left;
				if (iter->rect.right - pt.x < border)
					border = iter->rect.right - pt.x;
				if (pt.y - iter->rect.top < border)
					border = pt.y - iter->rect.top;
				if (iter->rect.bottom - pt.y < border)
					border = iter->rect.bottom - pt.y;
				if (border == iter->border)
				{
					result = iter->depth;
				} else {
					unsigned int tmp = static_cast<unsigned int>(Lerpf(0.0f, 
						static_cast<float>(iter->depth), 
						static_cast<float>(border) / iter->border));
					if (result < tmp)
						result = tmp;
				}
			}
		}
		return result;
	}

	void World::OnTick(int now, int delta)
	{
		DeleteEffect();
		DeleteWarEffect();
		DeleteSprite();
		DeleteWarSprite();
		DeleteImmovableObj();
		//用于镜头跟随~ add by baiyu
		m_camera.tick(delta);
		//update 别的什么东西要写在这个的前面
		if (m_pEngine->GetConfigManager()->IsSmoothMove())
		{
			if (m_bReseted)
			{
				m_bReseted = false;
				return;
			}
			if (delta > 1500)
				return;
			if (m_sprites.size() == 0)
				return;
			//XPTRACE(L"====== acc: %d first: %d ", m_nAccDelta, delta);
			delta += m_nAccDelta;
			//XPTRACE(L" mid: %d ", delta);
			int dif = delta - m_nLastDelta;
			//XPTRACE(L" diff: %d ", dif);
			short maxDiffDelta = m_pEngine->GetConfigManager()->GetMaxDiffDelta();
			if (dif > maxDiffDelta)
			{
				m_nAccDelta = dif - maxDiffDelta;
				delta = m_nLastDelta + maxDiffDelta;
			} else if (dif < -maxDiffDelta) {
				m_nAccDelta = dif + maxDiffDelta;
				delta = m_nLastDelta - maxDiffDelta;
			} else {
				m_nAccDelta = 0;
			}

			int avgDelta;
			if (m_bDeltaVetFull)
			{
				avgDelta = m_totalDeltas / XPDELTA_COUNT;
			} else {
				if (m_nDeltaPoint == 0)
				{
					avgDelta = 20;
				} else {
					avgDelta = m_totalDeltas / m_nDeltaPoint;
				}
			}
			dif = delta - avgDelta;
			short maxDeltaDiffAvg = m_pEngine->GetConfigManager()->GetMaxDiffFromAvg();
			if (dif > maxDeltaDiffAvg)
			{
				m_nAccDelta += dif - maxDeltaDiffAvg;
				delta = avgDelta + maxDeltaDiffAvg;
			} else if (dif < -maxDeltaDiffAvg) {
				m_nAccDelta += dif + maxDeltaDiffAvg;
				delta = avgDelta - maxDeltaDiffAvg;
			}

			short minDelta = m_pEngine->GetConfigManager()->GetMinDelta();
			if (delta < minDelta)
			{
				m_nAccDelta += delta - minDelta;
				delta = minDelta;
			}
			m_totalDeltas += delta;
			m_vetDeltas[m_nDeltaPoint] = delta;
			++m_nDeltaPoint;
			m_nDeltaPoint %= XPDELTA_COUNT;
			if (m_bDeltaVetFull)
			{
				m_totalDeltas -= m_vetDeltas[m_nDeltaPoint];
			} else {
				if (m_nDeltaPoint == 0)
				{
					m_bDeltaVetFull = true;
				}
			}
			m_nLastDelta = delta;
			//XPTRACE(L" end: %d\n", delta);
		}
		for (std::list<Sprite*>::iterator it = m_sprites.begin(); it != m_sprites.end(); ++it)
			(*it)->tick(now, delta);
		for (std::list<Sprite*>::iterator it = m_warSprites.begin(); it != m_warSprites.end(); ++it)
			(*it)->tick(now, delta);
		m_canvas.Clear();
	}

	bool World::GetLightPos(CPOINT &pos)
	{
		return false;
	}

	void World::PrefetchMapNotify(int count)
	{
		if (count < XPRMTT_COUNT)
		{
			NotifyLoadmapProgress(3 * count);//这个3没啥意思，就是说大概4个资源都加载完了进度应该是12%
			return;
		}
		m_pPrefetchMap = new Map(this, m_pEngine, &m_picResrcs);
		m_bIsAsync = true;
		if (!m_pPrefetchMap->Load(m_pReadMapTask[XPRMTT_MAP]->GetData(), 
			m_pReadMapTask[XPRMTT_MAZE]->GetData(), m_pReadMapTask[XPRMTT_SOUND]->GetData(), 
			m_pReadMapTask[XPRMTT_STEP_SOUND]->GetData(), m_pPathMap, 12))
		{
			for (int i=0;i<XPRMTT_COUNT;++i)
			{
				delete m_pReadMapTask[i];
				m_pReadMapTask[i] = NULL;
			}
			delete m_pPrefetchMap;
			m_pPrefetchMap = NULL;
			m_strPrefetchMapName = L"";
			m_pEngine->GetApp()->LoadingMapFaildNotify();
			return;
		}
		CRECT vp(m_camera.GetLTPosFromCenterPos(m_PrefetchPt, 
			m_pPrefetchMap->m_width, m_pPrefetchMap->m_height), 
			m_camera.GetViewport().Width(), m_camera.GetViewport().Height());
		for (int i=0;i<XPRMTT_COUNT;++i)
		{
			delete m_pReadMapTask[i];
			m_pReadMapTask[i] = NULL;
		}
		m_nLoadingCount = 0;
		m_nTotalLoadingCount = 0;
		m_pPrefetchMap->AssureRegionResource(vp, true, true, true, 33);//这个33就是说，4个资源加载完了是12%，然后Load地图的框架（包括img文件，不包括图片资源）占20%，最后从33%开始
		if (m_nTotalLoadingCount == 0)
		{
			m_nTotalLoadingCount = 1;
			//一个资源都不需要load
			NotifyALoadItemFin(33, 96);
		}
	}

	bool World::PrefetchMapRes(const std::wstring &mapname, const std::wstring &mazename, const CPOINT &center, bool autoSwitch)
	{
		Map *pMap = NULL;
		if (mapname == L"" || mapname == m_strMapName)
		{
			if (m_pPrefetchMap)
			{
				CancelPrefetch();
			}
			pMap = m_pMap;
		} else {
			if ( mapname != m_strPrefetchMapName )
			{
				CancelPrefetch();
				m_strPrefetchMapName = mapname;
				m_PrefetchPt = center;
				m_bIsAutoSwitch = autoSwitch;
				CFileIOManager* pFileIO = m_pEngine->GetFileIOManager();
				m_pReadMapTask[XPRMTT_MAP] = new ReadMapTask(Map::GetMapResUrl(mapname), 
					mapname, pFileIO, this);
				m_pReadMapTask[XPRMTT_MAP]->Submit();
				m_pReadMapTask[XPRMTT_MAZE] = new ReadMapTask(L"/map/" + mazename, 
					mazename, pFileIO, this);
				m_pReadMapTask[XPRMTT_MAZE]->Submit();
				m_pReadMapTask[XPRMTT_SOUND] = new ReadMapTask(Map::GetSoundResUrl(mapname), 
					mapname, pFileIO, this);
				m_pReadMapTask[XPRMTT_SOUND]->Submit();
				m_pReadMapTask[XPRMTT_STEP_SOUND] = new ReadMapTask(Map::GetStepSoundResUrl(mapname), 
					mapname, pFileIO, this);
				m_pReadMapTask[XPRMTT_STEP_SOUND]->Submit();
				return true;
			}
			pMap = m_pPrefetchMap;
		}
		m_nLoadingCount = 0;
		m_nTotalLoadingCount = 0;
		if( pMap == NULL ) 
			return false;

		m_PrefetchPt = center;
		CRECT vp(m_camera.GetLTPosFromCenterPos(center, pMap->m_width, pMap->m_height), 
			m_camera.GetViewport().Width(), m_camera.GetViewport().Height());
		
		m_bIsAutoSwitch = autoSwitch;
		pMap->AssureRegionResource(vp, true, true, true, 0);
		return true;
	}

	void World::CancelPrefetch()
	{
		m_bIsAutoSwitch = false;
		m_bIsAsync = false;
		m_nLoadingCount = 0;
		m_nTotalLoadingCount = 0;
		m_strPrefetchMapName = L"";
		if (m_pPrefetchMap == NULL)
		{
			m_manualPrefetchPicResrcs.clear();
			m_manualPrefetchEffects.clear();
			for (int i=0;i<XPRMTT_COUNT;++i)
			{
				if (m_pReadMapTask[i])
				{
					if (m_pReadMapTask[i]->ready)
					{
						delete m_pReadMapTask[i];
					} else {
						m_pReadMapTask[i]->Cancel();
					}
					m_pReadMapTask[i] = NULL;
				}
			}
			return;
		}
		std::set<Effect*> tmpEffect;
		std::set_difference(m_activeEffects.begin(), m_activeEffects.end(), m_manualPrefetchEffects.begin(),
			m_manualPrefetchEffects.end(), std::insert_iterator<std::set<Effect*>>(tmpEffect, tmpEffect.begin()));
		m_activeEffects.swap(tmpEffect);
		m_manualPrefetchPicResrcs.clear();
		m_manualPrefetchEffects.clear();
		m_camera.m_bDirty = true;
		if (m_pPrefetchMap)
			delete m_pPrefetchMap;
		m_pPrefetchMap = NULL;
	}

	void World::OnPlayFrame(ISprite* pSprite, const std::wstring &actionName, int frame)
	{
		if (m_pMap && m_pMap->m_pStepSoundMap && m_pEngine->GetSystemResourceManager()->IsSoundInited())
		{
			StepSoundType *pSST = m_pEngine->GetStepSoundType();
			int soundCount;
			int groundType = m_pMap->m_pStepSoundMap->GetData(pSprite->GetLocation());
			const std::wstring &actor = pSprite->GetSoundActorName();
			if (!pSST->GetSoundCount(actor, groundType, soundCount))
				groundType = 0;	//用默认声音
			if (soundCount > 0)
			{
				wchar_t tmpstr[255];
				wsprintf(tmpstr, L"/sound/step/%s/%d-%d.wav", actor.c_str(), groundType, rand() % soundCount);
				m_pEngine->GetSoundManager()->XPPlaySound(tmpstr, 
					m_pEngine->GetConfigManager()->GetStepSoundPriority(), 
					m_pEngine->GetConfigManager()->GetStepSoundType(), 255, 128);
			}
		}
	}

	int World::GetAllSpriteCount() const
	{
		return (int)(m_sprites.size() + m_warSprites.size());
		
	}
	void World::GetAllSpriteDetail(std::map<std::wstring, int> &detail) const
	{
		std::list<Sprite*>::const_iterator it = m_sprites.begin(), ie = m_sprites.end();
		for (;it!=ie;++it)
		{
			detail[(*it)->GetModelName()] += 1;
		}

		it = m_warSprites.begin(), ie = m_warSprites.end();
		for (;it!=ie;++it)
		{
			detail[(*it)->GetModelName()] += 1;
		}
	}

	int World::GetAllEffectCount() const
	{
		int result = 0;
		int i;
		for (i = 0;i<4;++i)
		{
			result += (int)m_effects[i].size();
		}
		for (i = 0;i<3;++i)
		{
			result += (int)m_warEffects[i].size();
		}

		std::list<Sprite*>::const_iterator it = m_sprites.begin(), ie = m_sprites.end();
		for (;it!=ie;++it)
		{
			result += (*it)->GetAllEffectCount();
		}

		it = m_warSprites.begin(), ie = m_warSprites.end();
		for (;it!=ie;++it)
		{
			result += (*it)->GetAllEffectCount();
		}
		if (m_pMap)
		{
			std::vector<RegionMap::RegionInfo*>::const_iterator regIt = m_pMap->m_curRegions.begin(), 
				regIe = m_pMap->m_curRegions.end();
			for (;regIt!=regIe;++regIt)
			{
				for(i=0;i<PIC_ELEMENT_LAYER_COUNT;++i)
				{
					result += (int)((*regIt)->m_effects[i].size());
				}
			}
		}
		return result;
	}

	void World::GetAllEffectDetail(std::map<std::wstring, int> &detail) const
	{
		EffectList::const_iterator it, ie;
		int i;
		for (i = 0;i<4;++i)
		{
			it = m_effects[i].begin();
			ie = m_effects[i].end();
			for (;it!=ie;++it)
			{
				detail[(*it)->GetName()] += 1;
			}
		}
		for (i = 0;i<3;++i)
		{
			it = m_warEffects[i].begin();
			ie = m_warEffects[i].end();
			for (;it!=ie;++it)
			{
				detail[(*it)->GetName()] += 1;
			}
		}

		std::list<Sprite*>::const_iterator sit = m_sprites.begin(), sie = m_sprites.end();
		for (;sit!=sie;++sit)
		{
			(*sit)->GetAllEffectDetail(detail);
		}

		sit = m_warSprites.begin(), sie = m_warSprites.end();
		for (;sit!=sie;++sit)
		{
			(*sit)->GetAllEffectDetail(detail);
		}
		if (m_pMap)
		{
			std::vector<Region::EffectInfo>::const_iterator effIt, effIe;
			std::vector<RegionMap::RegionInfo*>::const_iterator regIt = m_pMap->m_curRegions.begin(), 
				regIe = m_pMap->m_curRegions.end();
			for (;regIt!=regIe;++regIt)
			{
				for(i=0;i<PIC_ELEMENT_LAYER_COUNT;++i)
				{
					effIt = (*regIt)->m_effects[i].begin();
					effIe = (*regIt)->m_effects[i].end();
					for (;effIt!=effIe;++effIt)
					{
						detail[effIt->pEffect->GetName()] += 1;
					}
				}
			}
		}
	}
}
