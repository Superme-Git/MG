#include "../common/nuxptypes.h"
#include "../common/nulog.h"
#include "../common/nurecttopologylist.h"
#include "../common/numapconst.h"
#include "../common/nuxptimeuselog.h"
#include "../particlesystem/nuiparticlemanager.h"
#include "../effect/nueffectmanager.h"
#include "../effect/nuparticleeffect.h"
#include "../map/nuppathmap.h"
#include "../map/nupstepsoundmap.h"
#include "../sprite/nusprite.h"
#include "../sprite/nuspinesprite.h"
#include "../sprite/nuspritemanager.h"
#include "../engine/nuconfigmanager.h"
#include "../engine/nuengine.h"
#include "nulkotree.h"
#include "nudistort.h"
#include "numap.h"
#include "nuworld.h"

#include "log/CoreLog.h"

namespace Nuclear
{
	extern XPTimeUseLog g_timeuserlog;
	class SingleEffectNotify : public IEffectNotify
	{
	public:
		TriggerKey triggerKey;
		static World* pWorld;
		virtual void OnStop(IEffect *pEffect)
		{
			pWorld->m_SignleEffectMap.erase(triggerKey.id);
			((Effect*)pEffect)->RemoveNotify(this);
			delete this;
		}
		virtual void OnDelete(IEffect *pEffect)
		{
			pWorld->m_SignleEffectMap.erase(triggerKey.id);
			//pEffect->RemoveNotify(this);��Ҫɾ���ˣ��Ͳ�����
			delete this;
		}
	};

	World* SingleEffectNotify::pWorld = NULL;

	class WorldEffectnotify : public IEffectNotify, public EffectGC
	{
	public:
		Nuclear_EffectLayer layer;
		static World* pWorld;
		virtual void OnDelete(IEffect* pEffect)
		{
			//pEffect->RemoveNotify(this);��Ҫɾ���ˣ��Ͳ�����
			if (pWorld->m_loadingEffects.erase(pEffect) == 0 && (!pWorld->m_bIsInDestructor))
			{
				pWorld->m_removedLoadingEffects.erase(pEffect);
			}
			delete this;
		}
		virtual void OnAsyncLoaded(IEffect* pEffect, bool succeeded)
		{
			IEffectSet::iterator it = pWorld->m_loadingEffects.find(pEffect);
			Effect *tpEffect = (Effect*)pEffect;
			tpEffect->RemoveNotify(this);
			if (it == pWorld->m_loadingEffects.end())
			{
				//������֮������ʵ�Ѿ�����Ҫ�ˡ�������
				RemoveEffect(pEffect);
				pWorld->m_removedLoadingEffects.erase(pEffect);
			} else {
				if (succeeded)
				{
					tpEffect->Play();
					pWorld->m_effects[layer].push_back(tpEffect);
				} else {
					if (pWorld->m_liveDurEffectSet.find(pEffect) == pWorld->m_liveDurEffectSet.end())//�ǲ���һ�ε���Ч�����治����effect��ָ��
						RemoveEffect(pEffect);
				}
				pWorld->m_loadingEffects.erase(it);
			}
			delete this;
		}

	};

	World* WorldEffectnotify::pWorld = NULL;
	class ReadMapTask : public NuclearFileIOManager::AsyncReadTask
	{
		World *m_pWorld;
		int64_t m_tick;
		std::wstring m_name;	//����ǵ�ͼ������mapname�������maze������mazename
		bool m_bIsFull;			//��Դ�Ƿ�ȫ
	public:
		bool ready;
		const std::wstring &GetName() const {  return m_name; }
		NuclearBuffer &GetData() { return m_data; }
		ReadMapTask(const std::wstring &filename, const std::wstring &name, 
			NuclearFileIOManager *pFileIOMan, World *pWorld, bool bLookupSegmpak, bool bIsFull)
			//��ͼ��ReadMapTask������4���ļ���������Ҫ��Segmpak�ֵ�ģ��������bLookupSegmpak�Ǹ���ͼ�µ���Դ�õ�
			: NuclearFileIOManager::AsyncReadTask(pFileIOMan, filename, 0, bLookupSegmpak), m_pWorld(pWorld), m_name(name),
			ready(false), m_bIsFull(bIsFull)
		{
			m_tick = GetMilliSeconds();
		}

		bool IsFull() const { return m_bIsFull; }
		bool NeedLookupSegmpak() const { return m_bLookupSegmPak; }

		virtual void OnReady()
		{
			int64_t tick = GetMilliSeconds();
			if( m_data.size() == 0 )
			{
				XPLOG_ERROR(L"load map failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load map failed!, path is %s\n", m_filename.c_str());
			}
			else
				m_pFileIOMan->OnReady(int(tick-m_tick), m_data.size());
			ready = true;
			int count = 0;
			for (int i=0;i<World::XPRMTT_COUNT;++i)
			{
				if (m_pWorld->m_pReadMapTask[i]->ready)
				{
					++count;
				}
			}
			m_pWorld->PrefetchMapNotify(count);//��Notify����ɾ��ReadMapTask
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
		m_bHasCreateBGM(false),
		m_worldMode(XPWM_NORMAL),
		m_bIsInDestructor(false),
		m_iFlyOffsetYCur(0),//����ж������Y��ƫ��(ʵ������ģ��Z��)��ǰֵ
		m_iFlyOffsetYTgt(0),//����ж������Y��ƫ��(ʵ������ģ��Z��)Ŀ��ֵ
		m_iFlyOffsetYStep(10000)//����ж������Y��ƫ��(ʵ������ģ��Z��)����ֵ
	{
		m_fBattleBackgroundEdge = 0.0f;
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
		m_hNightEffectRenderTarget = INVALID_PICTURE_HANDLE;
		SingleEffectNotify::pWorld = this;
		WorldEffectnotify::pWorld = this;
		for (int i=0;i<XPRMTT_COUNT;++i)
		{
			m_pReadMapTask[i] = NULL;
		}
		//Ĭ���ϰ�����ɫ
		NuclearMazeColor mc;
		mc.mask = 0x3;
		mc.color.data = 0x3fff0000;
		m_mazeColors.push_back(mc);
		mc.mask = 0x1;
		mc.color.data = 0x3f04fd16;
		m_mazeColors.push_back(mc);

		//Ĭ���ɰ����ɫ
		m_MaskBoxColor = 0xffffffff;

		m_CurveMoveHeight = 8.0f;
		m_bShowEffect = true;
	}

	World::~World()
	{
		m_bIsInDestructor = true;
		FreeAllWarBackground();
		for(std::list<ImmovableObj*>::iterator it = m_immobjs.begin(), ie = m_immobjs.end(); it != ie; ++it)
			delete *it;
		for (int layer=0;layer<XPEL_COUNT;++layer)
			for(EffectList::iterator it = m_effects[layer].begin(), ie = m_effects[layer].end(); it != ie; ++it)
				delete *it;
		for (int layer=0;layer<XPSL_COUNT;++layer)
			for(std::list<Sprite*>::iterator it = m_sprites[layer].begin(), ie = m_sprites[layer].end(); it != ie; ++it)
				delete *it;
		
		for (std::map<ULONG64, SingleEffectNotify*>::iterator it =m_SignleEffectMap.begin(), ie = m_SignleEffectMap.end(); it != ie; ++it)
			delete it->second;

		for (IEffectSet::iterator it = m_removedLoadingEffects.begin(), ie = m_removedLoadingEffects.end(); it != ie; ++it)
		{
			delete *it;
		}
		for (IEffectSet::iterator it = m_loadingEffects.begin(), ie = m_loadingEffects.end(); it != ie; ++it)
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
		NuclearPictureInfo pinfo;
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

	bool World::SetWarBackground(WarBackgroundHandle handle, NuclearWarBackgroundType type)
	{
		WarBackgroundMap::iterator it = m_wbm.find(handle);
		if (it == m_wbm.end())
		{
			m_warBackGroundPic = INVALID_PICTURE_HANDLE;
			m_nWarBackGroundPicWidth = 0;
			m_nWarBackGroundPicHeight = 0;
			return false;
		}
		m_warBackGroundType = type;
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

	ISprite* World::NewSprite(NuclearSpriteLayer layer, const std::wstring &modelname)
	{
		Sprite *pSprite = NULL;
		SpriteManager* pSM = m_pEngine->GetSpriteManager();
		int nType = pSM->GetModelTypeByModelName(modelname);
		if (nType == 1)//Spine����
		{
			pSprite = new SpineSprite(m_pEngine, modelname);
		}
		else//ϵ��֡����
		{
			pSprite = new Sprite(m_pEngine, modelname);
		}

		if (pSprite)
		{
			pSprite->SetModel(modelname, true);
			m_sprites[layer].push_back(pSprite);
		}
		return pSprite;
	}

	bool World::AttachSprite(ISprite *pHostSprite, ISprite *pClientSprite, const std::wstring &hostSocket, const std::wstring &clientSocket, const NuclearVector3 &relpos, unsigned int sign)
	{
		Sprite *pSprHost = dynamic_cast<Sprite*>(pHostSprite);
		Sprite *pSprClient = dynamic_cast<Sprite*>(pClientSprite);
		if (!pSprHost || !pSprClient)
			return false;
		if (pSprHost->GetType() != XPSPRITETYPE_3D || pSprClient->GetType() != XPSPRITETYPE_3D)
			return false;
		m_willBeRemoveSprites[pClientSprite] = 1;
		return true;
	}

	bool World::DetachSprite(NuclearSpriteLayer layer, ISprite *pHostSprite, ISprite *pClientSprite)
	{
		Sprite *pSprHost = dynamic_cast<Sprite*>(pHostSprite);
		Sprite *pSprClient = dynamic_cast<Sprite*>(pClientSprite);
		if (!pSprHost || !pSprClient)
			return false;
		if (pSprHost->GetType() != XPSPRITETYPE_3D || pSprClient->GetType() != XPSPRITETYPE_3D)
			return false;
		std::map<ISprite*, int>::iterator iter = m_willBeRemoveSprites.find(pClientSprite);
		if (iter != m_willBeRemoveSprites.end())
		{
			XPASSERT(iter->second == 1);
			m_willBeRemoveSprites.erase(iter);
		} else {
			m_sprites[layer].push_back(pSprClient);
		}
		return true;
	}

	bool World::SelectObjs(const NuclearLocation &selloc, std::vector<ISelectableObj*> &ettobjs)
	{
		ettobjs.clear();
		std::set<EntitativeObj*> tset;
		for(std::vector<DrawElement>::const_reverse_iterator it = m_canvas.m_elements.rbegin(),ie = m_canvas.m_elements.rend(); it != ie; it++)
		{
			const DrawElement& drawElement = *it;
			if( ! drawElement.m_pos.PtInRect(NuclearPoint(selloc.x,selloc.y)) ) continue;
			EntitativeObj* pEtobj = dynamic_cast<EntitativeObj*>(drawElement.m_pr);
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

	//��ͨ��Ч
	//������Ч��
	IEffect* World::SetLinkedEffect(const std::wstring &name, Nuclear_EffectLayer layer, const NuclearPoint &pt1, const NuclearPoint &pt2, float time, bool async)
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
				tpEffect->SetDirection(XPDIR_TOP);//����Դ�޹�
				tpEffect->SetScaleType(XPEST_ALLSCALE);
			}
		}
		return tpEffect;
	}

	IEffect* World::SetEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, bool async)
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
			tpEffect->SetDirection(XPDIR_BOTTOM);//����Դ�޹�
			tpEffect->SetLocation(NuclearLocation(x, y));//����Դ�޹�
			tpEffect->SetScaleType(XPEST_ALLSCALE);
			IParticleEffect *pPEffect = tpEffect->TryConvertToParticle();
			if (pPEffect)
			{
				pPEffect->SetCycleMode(XPPCM_PULSE_EMISSION);
			}
		}
		return tpEffect;
	}

	IEffect* World::SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, Nuclear_EffectLayer layer, int x, int y, bool async)
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
			tpEffect->SetLocation(NuclearLocation(x, y));
			tpEffect->SetScaleType(XPEST_ALLSCALE);
		}
		return tpEffect;
	}

	void World::DeleteEffect()
	{
		EffectList::iterator it, ie;
		IEffectSet::iterator sie = m_willBeDeletedEffects.end();
		for (int layer=0;layer<XPEL_COUNT;++layer)
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

	IEffect* World::PlayEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, int times, bool async, unsigned char soundtype)
	{
		return (IEffect*)PlayOnceEffect(name, layer, x, y, times, async, soundtype);
	}

	void World::RemoveEffectEx(IEffect* pEffect)
	{
		IEffectSet::iterator it = m_liveDurEffectSet.find(pEffect);
		if (it != m_liveDurEffectSet.end())
			m_liveDurEffectSet.erase(it);
		it = m_loadingEffects.find(pEffect);
		if (it == m_loadingEffects.end())
		{
			m_willBeDeletedEffects.insert(pEffect);
		}
		else {
			m_loadingEffects.erase(pEffect);
			m_removedLoadingEffects.insert(pEffect);
		}
	}

	//һ����Ч��, times�ǲ��Ŵ���, һ����������һ�����˺����߷�Χ�˺���
	Effect* World::PlayOnceEffect(const std::wstring &name, Nuclear_EffectLayer layer, int x, int y, int times, bool async, unsigned char soundtype)
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
			tpEffect->SetLoopNum(times);//ֻ��������Ч���Լ���ʵ�֣���������Ч�������������Դ�޹أ����ӵ�psl����ͬ���ģ�ֻ��ͼƬ��Դ���첽
			tpEffect->SetDirection(XPDIR_TOP);//����Դ�޹�
			tpEffect->SetLocation(NuclearLocation(x, y));//����Դ�޹�
		}
		return tpEffect;
	}

	//��ͨ����
	void World::DeleteSprite()
	{
		std::list<Sprite*>::iterator it, ie;
		std::map<ISprite*, int>::iterator sit, sie = m_willBeRemoveSprites.end();
		std::map<ISprite*, NuclearSpriteLayer>::iterator mit, mie;
		if (m_willBeRemoveSprites.find(m_camera.m_pAttachedSprite) != sie)
		{
			m_camera.Attach(NULL);
		}
		bool bCanChangeLayer = true;//((m_nSort_tick + 1) % FIRE_SORT_FREQUENCE == 0);
		
		for (int layer=XPSL_MIDDLE1;layer<XPSL_COUNT;++layer)
		{
			mie = m_willBeMoveLayer[layer].end();
			mit = mie;
			it = m_sprites[layer].begin();
			ie = m_sprites[layer].end();
			for (;it!=ie;)
			{
				sit = m_willBeRemoveSprites.find(*it);
				if (sit != sie)
				{
					if (sit->second == 0)
						delete (*it);
					it = m_sprites[layer].erase(it);
				} else {
					if (bCanChangeLayer)
					{
						mit = m_willBeMoveLayer[layer].find(*it);
					}
					if (mit != mie)
					{
						m_sprites[mit->second].push_back(*it);
						it = m_sprites[layer].erase(it);
					} else {
						++it;
					}
				}
			}
			if (bCanChangeLayer)
			{
				m_willBeMoveLayer[layer].clear();
			}
		}
		m_willBeRemoveSprites.clear();
	}

	void World::DeleteSprite(ISprite* sprite)
	{
		if (m_willBeRemoveSprites.find(sprite) == m_willBeRemoveSprites.end())
			m_willBeRemoveSprites[sprite] = 0;
	}
	
	void World::DeleteAllSprite(NuclearSpriteLayer layer, bool keepAttached)
	{
		std::list<Sprite*>::iterator it = m_sprites[layer].begin(), ie=m_sprites[layer].end();
		for (;it!=ie;++it)
		{
			if (m_willBeRemoveSprites.find(*it) == m_willBeRemoveSprites.end())
				m_willBeRemoveSprites[*it] = 0;
		}
		if (keepAttached)
			m_willBeRemoveSprites.erase(m_camera.m_pAttachedSprite);
	}

	void World::MoveSpriteLayer(ISprite *pSprite, NuclearSpriteLayer fromlayer, NuclearSpriteLayer tolayer)
	{
		if (fromlayer == tolayer)
			return;
		m_willBeMoveLayer[fromlayer][pSprite] = tolayer;
	}

	void World::OnTimer(int iPeriod)
	{
		if( m_pMap  && m_pEngine->GetConfigManager()->IsDynamicMapLoading() )
		{
			m_pMap->AssureRegionResource(m_camera.GetConstViewport(), true, false, false, 0);
		}
	}

	bool World::NotifyLoadmapProgress(int pro)
	{
		if (m_pEngine->GetApp()->LoadingMapNotify(pro))
			;
		else 
			return false;
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

	bool World::GetMapSize(NuclearPoint &size) const
	{
		if (!m_pMap)
			return false;
		size.x = m_pMap->m_width;
		size.y = m_pMap->m_height;
		return true;
	}

	bool World::IsMapFull() const
	{
		if (!m_pMap)
			return false;
		return m_pMap->IsFull();
	}

	void World::ImmediatelyAssureRegionResource(const Nuclear::NuclearLocation &loc)
	{
		m_camera.Reset(loc, m_pMap->m_width, m_pMap->m_height);
		m_pMap->AssureRegionResource(m_camera.GetConstViewport(), false, false, false, 0);
	}

	bool World::LoadMap( const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, int pro, bool async)	// ���ص�ͼ
	{
		m_bReseted = true;
		if (param == NULL)
			return false;
		XPLOG_INFO(L"  ��ʼ���ص�ͼ: %s\n", mapname.c_str());
		bool switchPrefectchMap = ( mapname == m_strPrefetchMapName && m_pPrefetchMap);
		m_bDrawWorld = false;
		m_bIsAsync = async;
		if (!switchPrefectchMap)
		{
			CancelPrefetch();
			if (async)
			{
				XPLOG_INFO(L"  �첽���ص�ͼ: %s\n", mapname.c_str());
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

			NuclearFileIOManager* pFileIO = m_pEngine->GetFileIOManager();
			std::wstring shortname(mapname);

			if(!m_pMap->Load(shortname, L"/map/" + mazename, m_pPathMap))
			{
				delete m_pMap;
				m_pMap = NULL;
				m_bDrawWorld = true;
				return false;
			}
			XPLOG_INFO(L"  ����Ĭ�ϵ�ͼ: %s\n", shortname.c_str());
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
			//��ˮ
			if (m_waterAlphaMask == INVALID_PICTURE_HANDLE)
			{
				m_waterAlphaMask = m_pEngine->GetRenderer()->LoadPicture(L"/map/tiles/tilemask.png");
			}
			for (int i=0;i<SubMap::NuclearGround::WATER_EFFECT_COUNT;++i)
			{
				m_pMap->m_waterEffects[i].SetAlphaMask(m_waterAlphaMask);
			}
		} else {
			//û��ˮ
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
		m_pMap->AssureRegionResource(m_camera.GetConstViewport(), async, true, false, 22 * (100 - pro) / 100 + pro);

		NotifyLoadmapProgress(99 * (100 - pro) / 100 + pro);
		
		m_strMapName = mapname;
		m_bHasCreateBGM = false;
		XPLOG_INFO(L"  ���ص�ͼ�ɹ�: %s\n", mapname.c_str());
		NotifyLoadmapProgress(100);
		m_bDrawWorld = true;

		return true;
	}

	bool World::LoadMap(const std::wstring& mapname, const std::wstring &mazename, const XPLoadmapParam* param, bool async)
	{
		return LoadMap(mapname, mazename, param, 0, async);
	}

	bool World::UnloadMap()			// ж�ص�ͼ
	{
		if (!m_pMap)
			return true;
		m_strMapName = L"";

		//����Ѿ�����Remove���ռ����ˣ�����������ɾ���Ķ���
		DeleteSprite();
		DeleteEffect();
		DeleteImmovableObj();
		m_activeEnvSoundArea.clear();
		m_removedLoadingEffects.insert(m_loadingEffects.begin(), m_loadingEffects.end());
		m_loadingEffects.clear();
		//���ʣ�µ�Effect
		EffectList::iterator eit, eie;
		for (int layer=0;layer<XPEL_COUNT;++layer)
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
		//���ʣ�µ�ImmovableObj
		std::list<ImmovableObj*>::iterator iit = m_immobjs.begin(), iie=m_immobjs.end();
		for (;iit!=iie;++iit)
		{
			delete (*iit);
		}
		m_immobjs.clear();
		//�����Sprite

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
		for (int l=0;l<XPSL_COUNT;++l)
		{
			m_sortCache[l].clear();
			m_vpiclist[l].clear();
		}
		m_poolSurfaceRenderTargets.ReleaseAll();
//		m_pLastArea = NULL;
		return true;
	}
		
	bool World::Init()
	{
		m_poolSurfaceRenderTargets.Reset(GetSurfaceCacheCountByView(m_camera.GetConstViewport()),Map::REGION_WIDTH,Map::REGION_HEIGHT);
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
			for (int layer = XPSL_MIDDLE1; layer<XPSL_COUNT; ++layer)
			{
				std::list<Sprite*>::iterator iter = m_sprites[layer].begin();
				for(;iter!=m_sprites[layer].end();iter++)
				{
					if (*iter == sprite)
					{
						m_camera.Attach(*iter);
						return;
					}
				}
			}
			
		}
		
	}

	const NuclearRect& World::GetViewport() const
	{
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
		//����Distorts
		const NuclearRect &vp = m_camera.GetConstViewport();
		for (int i=0;i<XPPIC_ELEMENT_LAYER_COUNT;++i)
		{
			for(std::vector<Map::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for (std::vector<Distort*>::const_iterator disIter = (*it)->m_vpDistorts[i].begin(), disIE = (*it)->m_vpDistorts[i].end(); disIter!=disIE;++disIter)
				{
					Distort* pDistort = *disIter;
					if (pDistort->lastUpdateTick != updateTime)
					{
						pDistort->setZoom(m_camera.GetScale());
						pDistort->Update(fDelta);
						pDistort->lastUpdateTick = updateTime;
					}
				}
			}
		}
	}

	void World::SimpleUpdateTrees(int delta, int updatetime, bool randomlight)
	{
		const NuclearRect &vp = m_camera.GetConstViewport();
		float scale = m_pEngine->GetWorldScale();
		for (int i=0;i<XPPIC_ELEMENT_LAYER_COUNT;++i)
		{
			for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for (std::vector<LkoTree*>::const_iterator treeIter = (*it)->m_vpLkoTrees[i].begin(), treeIE = (*it)->m_vpLkoTrees[i].end(); treeIter!=treeIE;++treeIter)
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
		const NuclearRect &vp = m_camera.GetConstViewport();
		float scale = GetScale();
		for (int i=0;i<XPPIC_ELEMENT_LAYER_COUNT;++i)
		{
			for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for (std::vector<LkoTree*>::const_iterator treeIter = (*it)->m_vpLkoTrees[i].begin(), treeIE = (*it)->m_vpLkoTrees[i].end(); treeIter!=treeIE;++treeIter)
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
		//���¶�̬���
		DWORD nightTime = FIRE_NIGHT_START_TIME - FIRE_LIGNT_RANGE;
		DWORD dayTime = FIRE_DAY_START_TIME + FIRE_LIGNT_RANGE;
		if (nightTime < 0)
			nightTime += FIRE_A_DAY_TIME;
		if (dayTime > FIRE_A_DAY_TIME)
			dayTime -= FIRE_A_DAY_TIME;
		bool updateNewLightTime = false;
		if (!(m_pMap->m_nSign & NuclearMap::MAP_SIGN_ALWAYS_DAYTIME) && !(m_pMap->m_nSign & NuclearMap::MAP_SIGN_ALWAYS_NIGHT))
		{
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
		}
		m_pEngine->GetConfigManager()->IsEnableLinkedObjs()?
			NormalUpdateTrees(delta, updateTime, updateNewLightTime):SimpleUpdateTrees(delta, updateTime, updateNewLightTime);
	}

	void World::UpdateTriggers(int delta)
	{

	}

	void World::UpdateEffects(int delta)
	{
		for(std::vector<Map::RegionInfo*>::iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
		{
			for (int i=0;i<XPPIC_ELEMENT_LAYER_COUNT;++i)
			{
				if (!m_bShowEffect && i <= XPEL_NORMAL_SPRITE_2)
					continue;

				//��ͼ��Ч
				for(std::vector<Region::EffectInfo>::iterator it2 = (*it)->m_effects[i].begin(), ie2 = (*it)->m_effects[i].end(); it2 != ie2; ++it2)
				{
					it2->pEffect->Update(delta);
				}
			}
		}
	}

	void World::pauseSceneEffects()
	{
		if (!m_pMap) return;
		for (std::vector<Map::RegionInfo*>::iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
		{
			for (int i = 0; i < XPPIC_ELEMENT_LAYER_COUNT; ++i)
			{
				//��ͼ��Ч
				for (std::vector<Region::EffectInfo>::iterator it2 = (*it)->m_effects[i].begin(), ie2 = (*it)->m_effects[i].end();
					it2 != ie2; ++it2)
				{
					it2->pEffect->Stop();
				}
			}
		}
	}

	void World::resumeSceneEffects()
	{
		if (!m_pMap) return;
		for (std::vector<Map::RegionInfo*>::iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
		{
			for (int i = 0; i < XPPIC_ELEMENT_LAYER_COUNT; ++i)
			{
				//��ͼ��Ч
				for (std::vector<Region::EffectInfo>::iterator it2 = (*it)->m_effects[i].begin(), ie2 = (*it)->m_effects[i].end();
					it2 != ie2; ++it2)
				{
					it2->pEffect->AssureResource(true);
					it2->pEffect->Play();
				}
			}
		}
	}

	int World::GetSurfaceCacheCountByView(const NuclearRect &viewrect)
	{
		return ((viewrect.Width()-1)/Map::REGION_WIDTH+4)*((viewrect.Height()-1)/Map::REGION_HEIGHT+4);			
	}

	void World::UpdateBGMArea(const NuclearPoint &pt)
	{
//        assert(false);
//		DirectSoundManager *pDSM = m_pEngine->GetSoundManager();
//		if  (!m_pMap || m_bgsoundMode != XPBGSM_AUTO || !pDSM->IsSoundTypeEnable(m_pEngine->GetConfigManager()->GetBGMType()))
//			return;
//		Camera* v = &m_camera;
//		
//		
//		bool lastIsDefault = false;
//		float fVol = 1.0f;
//		int regionID = (pt.y / Map::REGION_HEIGHT) * m_pMap->m_regioncol + (pt.x / Map::REGION_WIDTH);
//		std::vector<XPBkgVolArea*> &volAreas = m_pMap->m_regions[regionID].m_BkgVolumeAreas;
//		std::vector<XPBkgVolArea*>::iterator vit = volAreas.begin(), vie = volAreas.end();
//		for (;vit!=vie;++vit)
//		{
//			float tmpVol = (*vit)->GetAreaVolume(pt);
//			fVol = fVol<tmpVol?fVol:tmpVol;
//		}
//		if (m_pLastArea)
//		{
//			lastIsDefault = (m_pLastArea->GetType() == XPAREA_TYPE_NULL);
//			if (!lastIsDefault)
//			{
//				if (m_pLastArea->TestArea(pt))
//				{
//					if (!m_bHasCreateBGM)
//					{
//						m_pLastArea->LeaveMeAndEnterNew(m_pLastArea);
//						m_bHasCreateBGM = true;
//					}
//					m_pLastArea->SetVolume(fVol);
//					return;
//				}
//			}
//		}
//		//������������ֿ��ܣ�1 last��NULL��2 last��Default��3 �Ѿ��뿪��last��
//		
//		std::vector<XPBkgSoundArea*> &areas = m_pMap->m_regions[regionID].m_BkgSoundAreas;
//		std::vector<XPBkgSoundArea*>::iterator it = areas.begin(), ie = areas.end();
//		for (;it!=ie;++it)
//		{
//			if ((*it)->TestArea(pt))
//			{
//				if (m_pLastArea)
//				{
//					m_pLastArea->LeaveMeAndEnterNew((*it));
//					m_pLastArea = (*it);
//				} else {
//					m_pLastArea = (*it);
//					m_pLastArea->LeaveMeAndEnterNew(m_pLastArea);
//				}
//				m_bHasCreateBGM = true;
//				m_pLastArea->SetVolume(fVol);
//				return;
//			}
//		}
//
//		//�������last�����ֿ��ܣ�1 last��NULL��2 last��Default��3 �Ѿ��뿪��last��
//		//�������ڲ����κ�һ���������棬����Ĭ�ϱ�������
//		//ֻҪ���ǵ�2�����������Ҫ����Ĭ�ϱ�������
//		if ((!m_bHasCreateBGM) || (!lastIsDefault))
//		{
//			m_bHasCreateBGM = true;
//			if (m_pLastArea)
//			{
//				m_pLastArea->LeaveMeAndEnterNew(m_pMap->m_pBackGroundDefaultArea);
//				m_pLastArea = m_pMap->m_pBackGroundDefaultArea;
//			} else {
//				m_pLastArea = m_pMap->m_pBackGroundDefaultArea;
//				if (m_pLastArea)
//				{
//					m_pLastArea->LeaveMeAndEnterNew(m_pLastArea);
//				}
//			}
//		}
//		if (m_pLastArea)
//		{
//			m_pLastArea->SetVolume(fVol);
//		}
	}

	//XPBGSM_AUTO ��XPBGSM_CUSTOM��XPBGSM_BATTLE
	//XPBGSM_CUSTOM����XPBGSM_AUTO��XPBGSM_BATTLE
	//XPBGSM_BATTLE����XPBGSM_CUSTOM��XPBGSM_AUTO

	void World::OnChangeBGSoundMode(NuclearBGSoundMode mode)
	{
        assert(false);
//		if (mode == m_bgsoundMode)
//			return;
//		DirectSoundManager *pDSM = m_pEngine->GetSoundManager();
//		ConfigManager *pCfgMan = m_pEngine->GetConfigManager();
//		switch (m_bgsoundMode)
//		{
//		case XPBGSM_AUTO:
//			if (mode == XPBGSM_CUSTOM)
//			{
//				ResetBGSound();
//			} else { //mode == XPBGSM_BATTLE
//				if (pDSM && pCfgMan)
//				{
//					pDSM->PauseTypeSound(pCfgMan->GetBGMType());
//					pDSM->PauseTypeSound(pCfgMan->GetEnvSoundType());
//				}
//			}
//			break;
//		case XPBGSM_CUSTOM:
//			ResetBGSound();
//			if (mode == XPBGSM_BATTLE && pDSM && pCfgMan)
//			{
//				pDSM->PauseTypeSound(pCfgMan->GetEnvSoundType());
//			}
//			break;
//		case XPBGSM_BATTLE:
//			if (pDSM && pCfgMan)
//			{
//				pDSM->ResumeTypeSound(pCfgMan->GetEnvSoundType());
//			}
//			if (mode == XPBGSM_CUSTOM)
//			{
//				ResetBGSound();
//			} else { //mode == XPBGSM_AUTO
//				if (pDSM && pCfgMan)
//				{
//					pDSM->ResumeTypeSound(pCfgMan->GetBGMType());
//				}
//			}
//			break;
//		}
//		m_bgsoundMode = mode;
	}

	void World::ResetBGSound()
	{
//		m_bHasCreateBGM = false;
//		if (m_pLastArea)
//		{
//			m_pLastArea->LeaveMeAndEnterNew(NULL);
//			m_pLastArea = NULL;
//		}
	}

	void World::UpdateWaterRenderArea()
	{
		Camera* v = &m_camera;
		const NuclearRect &viewport = v->GetConstViewport();
		//ˮ����Ч
		int x, y, i, j, xAdj, yAdj, ci, cj;
		xAdj = viewport.left % SubMap::NuclearGround::WATER_WIDTH;
		yAdj = viewport.top % SubMap::NuclearGround::WATER_HEIGHT;
		x = static_cast<int>(viewport.right / SubMap::NuclearGround::WATER_WIDTH);
		if (x >= m_pMap->m_widthForWater)
		{
			x = m_pMap->m_widthForWater - 1;
		}
		y = static_cast<int>(viewport.bottom  / SubMap::NuclearGround::WATER_HEIGHT);
		if (y >= m_pMap->m_heightForWater)
		{
			y = m_pMap->m_heightForWater - 1;
		}
		ci = 0;
		cj = 0;
		Nuclear::NuclearFRectt tmpRect;
		for (i=0;i<Nuclear::SubMap::NuclearGround::WATER_EFFECT_COUNT;++i)
			m_pMap->m_waterEffects[i].ClearRects();
		SubMap::uWaterTileType *pTmpWater;
		j = viewport.top / SubMap::NuclearGround::WATER_HEIGHT;
		if (j < 0)
		{
			cj = -j;
			j = 0;
		}
		for (; j<=y ; j++, cj++) 
		{
			ci = 0;
			i = viewport.left / SubMap::NuclearGround::WATER_WIDTH;
			if (i < 0)
			{
				ci = -i;
				i = 0;
			}
			pTmpWater =  m_pMap->m_pWaterData + (j * m_pMap->m_widthForWater + i);
			for ( ; i<=x ; i++, ci++, pTmpWater++)
			{
				if (pTmpWater->tileRes > 0)
				{
					tmpRect.left = (ci*SubMap::NuclearGround::WATER_WIDTH - xAdj) * v->GetScale();
					tmpRect.top = (cj*SubMap::NuclearGround::WATER_HEIGHT - yAdj) * v->GetScale();
					tmpRect.right = ((ci+1)*SubMap::NuclearGround::WATER_WIDTH - xAdj) * v->GetScale();
					tmpRect.bottom = ((cj+1)*SubMap::NuclearGround::WATER_HEIGHT - yAdj) * v->GetScale();
					m_pMap->m_waterEffects[pTmpWater->tileRes-1].PushARect(tmpRect, pTmpWater->tileType);
				}
			}
		}
	}

	void World::UpdateSurfaceCacheLayersWhenNotUseSurfaceCache(bool &picloading)
	{
		Camera* v = &m_camera;
		const NuclearRect &viewport = v->GetConstViewport();
		for (int l = 0; l < XPPIC_GROUND_LAYER_COUNT; ++l)
		{
			v->m_ground_picele_cache[l].clear();
			for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for(std::vector<Region::PicEle>::const_iterator it2 = (*it)->m_vPicEle[l].begin(), ie2 = (*it)->m_vPicEle[l].end(); it2 != ie2; ++it2)
				{
					const Region::PicEle &pic = *it2;
					if (pic.forCache)
						continue;
					PictureHandle handle = pic.pPicResrc->handle;
					if( handle == INVALID_PICTURE_HANDLE) // todo load?
					{
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

	void World::UpdateStaticLayers(int startlayer, bool &picloading)
	{
		Camera* v = &m_camera;
		const NuclearRect &viewport = v->GetConstViewport();
		for(int l = startlayer; l < XPPIC_LAYER_COUNT; ++l)
		{
			if( l == XPPIC_MID1_ELEMENT_LAYER_ID ) continue; // ����в����
			v->m_picele_cache[l].clear();
			for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for(std::vector<Region::PicEle>::const_iterator it2 = (*it)->m_vPicEle[l].begin(), ie2 = (*it)->m_vPicEle[l].end(); it2 != ie2; ++it2)
				{
					const Region::PicEle &pic = *it2;
					if (pic.forCache)
						continue;
					PictureHandle handle = pic.pPicResrc->handle;
					if( handle == INVALID_PICTURE_HANDLE)
					{
						//��û�м�������
						picloading = true;
						continue;
					}
					CachePic p;
					NuclearRect rect;
					p.handle = handle;
					NuclearRect wrect = pic.pos;
					if( wrect.Cut(viewport, rect, p.srcRect) )
					{
						p.color = pic.color;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
						p.srcRect.left += (0.5 / 256);
						p.srcRect.top += (0.5 / 256);
						p.srcRect.right -= (0.5 / 256);
						p.srcRect.bottom -= (0.5 / 256);
#endif
						p.objRect.left = rect.left * v->GetScale();
						p.objRect.top = rect.top * v->GetScale();
						p.objRect.right = rect.right * v->GetScale();
						p.objRect.bottom = rect.bottom * v->GetScale();
						v->m_picele_cache[l].push_back(p);
					}
				}
			}
		}
	}

	void World::UpdateDynamicLayers(bool &picloading)
	{
		Camera* v = &m_camera;
		const NuclearRect &viewport = v->GetConstViewport();
		//imgͼ��
		int elementLayerID;
		for (int l=0;l<XPSORTED_LAYER_COUNT;++l)
		{
			elementLayerID = l + XPMID1_ELEMENT_LAYER_ID_FOR_ELEMENT;
			//l����0��ʱ����XPMID1_ELEMENT_LAYER_ID_FOR_ELEMENT��
			//l����1��ʱ����XPMID2_ELEMENT_LAYER_ID_FOR_ELEMENT��
			v->m_mapmobj_cache[l].clear();
			std::set<LkoTree*> hasAdd;
			std::set<Distort*> hasAddDist;
			for(std::vector<Map::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for(std::vector<Region::PicEleWithBase>::const_iterator it2 = (*it)->m_vPicEleWithBase[l].begin(), ie2 = (*it)->m_vPicEleWithBase[l].end(); it2 != ie2; ++it2)
				{
					const Region::PicEleWithBase &pic = *it2;
					PictureHandle handle = pic.pPicResrc->handle;
					if( handle == INVALID_PICTURE_HANDLE)
					{
						picloading = true;
						continue;
					}
					VPic vpic;
					NuclearRect rect;
					vpic.handle = handle;
					NuclearRect wrect = pic.pos;
					if( wrect.Cut(viewport, rect, vpic.srcrect) )
					{
						vpic.rect.left = rect.left * v->GetScale();
						vpic.rect.top = rect.top * v->GetScale();
						vpic.rect.right = rect.right * v->GetScale();
						vpic.rect.bottom = rect.bottom * v->GetScale();
						vpic.base = pic.pOrg->base;
						vpic.pRenderable = NULL;
						vpic.pObjPic = (void*)&pic;
						v->m_mapmobj_cache[l].push_back(vpic);
					}
				}

				//��ͼ��Ч
				for(std::vector<Region::EffectInfo>::iterator it2 = (*it)->m_effects[elementLayerID].begin(), ie2 = (*it)->m_effects[elementLayerID].end(); it2 != ie2; ++it2)
				{
					Region::EffectInfo &einfo = *it2;
					if (!einfo.validBase)
					{
						NuclearBase base;
						if( einfo.pEffect->GetBase(base) )
						{
							NuclearPoint tmpCPoint;
							TransToDiamondRadix((base.left.x+ einfo.pos.x),(base.left.y+ einfo.pos.y),tmpCPoint);
							einfo.base.left = tmpCPoint.x;
							einfo.base.bottom = tmpCPoint.y;
							TransToDiamondRadix((base.right.x+ einfo.pos.x),(base.right.y+ einfo.pos.y),tmpCPoint);
							einfo.base.right = tmpCPoint.x;
							einfo.base.top = tmpCPoint.y;
							einfo.base.srcRect = einfo.pEffect->GetRelBouningBox().Widen(20) + einfo.pos;
							einfo.base.y = (base.right.y+ base.left.y)/2 + einfo.pos.y;
							einfo.validBase = true;
						}
					}
					if (IsRectCross(v->m_viewport, einfo.rect))
					{
						VPic vpic;
						vpic.base = einfo.base;
						vpic.pRenderable = einfo.pEffect;
						v->m_mapmobj_cache[l].push_back(vpic);
					}
				}
				//��̬���
				for (std::vector<LkoTree*>::const_iterator treeIter = (*it)->m_vpLkoTrees[elementLayerID].begin(), treeIE = (*it)->m_vpLkoTrees[elementLayerID].end(); treeIter!=treeIE;++treeIter)
				{
					if (hasAdd.find(*treeIter) == hasAdd.end())
					{
						if (IsRectCross((*treeIter)->m_rect, v->m_viewport))
						{
							VPic vpic;
							vpic.base = (*treeIter)->base;
							vpic.pRenderable = *treeIter;
							v->m_mapmobj_cache[l].push_back(vpic);
						}
						hasAdd.insert(*treeIter);
					}
				}
				//Ʈ�����
				for (std::vector<Distort*>::const_iterator distIter = (*it)->m_vpDistorts[elementLayerID].begin(), distIE = (*it)->m_vpDistorts[elementLayerID].end(); distIter!=distIE;++distIter)
				{
					if (hasAddDist.find(*distIter) == hasAddDist.end())
					{
						if (IsRectCross((*distIter)->GetRect(), v->m_viewport))
						{
							VPic vpic;
							vpic.base = (*distIter)->base;
							vpic.pRenderable = *distIter;
							v->m_mapmobj_cache[l].push_back(vpic);
						}
						hasAddDist.insert(*distIter);
					}
				}
			}
		}
	}
	void World::UpdateBeforeRender(int delta)
	{
		if( !m_bDrawWorld )
			return;

		m_nGameTime = m_nGameTime + static_cast<DWORD>(delta * m_nGameTimeDeltaMul);
		m_nGameTime %= FIRE_A_DAY_TIME;

		if (m_worldMode == XPWM_SIMPLE_BATTLE || m_worldMode == XPWM_WORLD_BATTLE)
		{
			if (!m_pMap) return;
			EffectList::iterator it, ie;
			for (int layer=XPEL_BATTLE_LOW;layer<=XPEL_BATTLE_HEIGHT;++layer)
			{
				it = m_effects[layer].begin();
				ie = m_effects[layer].end();
				for(;it!=ie;)
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
			if (m_worldMode == XPWM_SIMPLE_BATTLE) return;//��ս��״̬�������return��
		}

		if  (!m_pMap) return;

		Camera* v = &m_camera;
		if (v->m_pAttachedSprite)
		{
			if (m_pEngine->GetSystemResourceManager()->IsSoundInited() && (m_bgsoundMode != XPBGSM_BATTLE))
			{
				NuclearPoint centerPos = v->m_pAttachedSprite->GetLocation();
				//Update ��������
				UpdateBGMArea(centerPos);
				//Update ��������

				int regionID = (centerPos.y / Map::REGION_HEIGHT) * m_pMap->m_regioncol + (centerPos.x / Map::REGION_WIDTH);
				std::vector<XPEnvSoundArea*> &areas = m_pMap->m_regions[regionID].m_EnvSoundAreas;
				m_activeEnvSoundArea.insert(areas.begin(), areas.end());
				std::set<XPEnvSoundArea*>::iterator it = m_activeEnvSoundArea.begin(), ie = m_activeEnvSoundArea.end();
				for (; it != ie;)
				{
					if (!(*it)->Update(centerPos))
					{
						m_activeEnvSoundArea.erase(it++);
					}
					else {
						++it;
					}
				}
			}
		}
		
		// �ӿ��б仯�����¼��� cache ������
		if( m_camera.m_bDirty) 
		{
			const NuclearRect &viewport = v->GetConstViewport();
			m_pEngine->GetRenderer()->GetParticleManager()->OnViewportUpdate(viewport);
			//XPTRACE(L"ditry = true, updatecache\n");
			bool picloading = false;
			// todo ���㵱ǰ����̫�ѿ�
			////////////////////////////////////////////
			std::set<Map::RegionInfo*> setOldRegions;
			std::set<Map::RegionInfo*> setNewRegions;
			for(std::vector<Map::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
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

#if defined(WIN32) && defined(DEBUG)
			m_pMap->m_CurRegion.left = minregioncol * Map::REGION_WIDTH;
			m_pMap->m_CurRegion.top = minregionrow * Map::REGION_HEIGHT;
			m_pMap->m_CurRegion.right = (maxregioncol+1)* Map::REGION_WIDTH;
			m_pMap->m_CurRegion.bottom = (maxregionrow+1)* Map::REGION_HEIGHT;

			if( m_pMap->m_CurRegion.right < m_pMap->m_CurRegion.left) 
				m_pMap->m_CurRegion.right = m_pMap->m_CurRegion.left;
			if( m_pMap->m_CurRegion.bottom < m_pMap->m_CurRegion.top) 
				m_pMap->m_CurRegion.bottom = m_pMap->m_CurRegion.top;
#endif
			for(int row = minregionrow; row <= maxregionrow; ++row) for(int col = minregioncol; col <= maxregioncol; ++col)
			{				
				m_pMap->m_curRegions.push_back(&m_pMap->m_regions[col+row*regioncol]);
			}
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

			UpdateWaterRenderArea();

			// ��RenderTargetCache���Ƶر�������Ͳ�
			int startlayer = 0;
			if( m_pEngine->GetConfigManager()->IsUseSurfaceCache() )
				//�������RenderTarget��Cache�Ļ����ر����1��û�б�Ҫ������cache�ˣ�
				//�����л���ʱ��Ҫע��һ�£������ֹ��ʱ�򣬴���RenderTarget�л�������RenderTarget��
				//Ҫ����Cache����ֱ�ӵİ취�������л���ʱ��camera.dirty��Ϊtrue��
				//����cameraĿǰû�б�¶����ӿ�
			{
				UpdateStaticLayers(XPPIC_CATCH_LAYER_COUNT, picloading);
			} else {
				UpdateSurfaceCacheLayersWhenNotUseSurfaceCache(picloading);
				UpdateStaticLayers(0, picloading);
			}

			// ��ͼ����в�
			UpdateDynamicLayers(picloading);
			if( !picloading )
				m_camera.m_bDirty = false;
		}

		UpdateDistorts(delta);
		UpdateTrees(delta);
		UpdateTriggers(delta);
		UpdateEffects(delta);

		for (int layer=0;layer<XPEL_WORLD_COUNT;++layer)
		{
			for(EffectList::iterator it = m_effects[layer].begin(); it != m_effects[layer].end();)
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

		for (int i=0;i<Nuclear::SubMap::NuclearGround::WATER_EFFECT_COUNT;++i)
			m_pMap->m_waterEffects[i].Update(delta);

		for(std::list<ImmovableObj*>::iterator it = m_immobjs.begin(), ie = m_immobjs.end(); it != ie; ++it)
		{
			(*it)->Update(delta);
		}
	}

	void World::SortMid()
	{	
		// �����ͼ�м��;���
		bool bIsInBattle = m_worldMode == XPWM_SIMPLE_BATTLE || m_worldMode == XPWM_WORLD_BATTLE;
		std::list<Sprite*>::iterator sprIt, sprIe;
		EffectList::iterator effIt, effIe;
		Camera *v = &m_camera;
		for (int l = 0; l < XPSL_COUNT; ++l)
		{
			if (bIsInBattle && l != XPSL_BATTLE)
				continue;

			switch(l)
			{
			case XPSL_MIDDLE1:
				m_vpiclist[XPSL_MIDDLE1] = v->m_mapmobj_cache[XPSL_MIDDLE1];
				sprIt = m_sprites[XPSL_MIDDLE1].begin();
				sprIe = m_sprites[XPSL_MIDDLE1].end();
				effIt = m_effects[XPEL_NORMAL_SPRITE_1].begin();
				effIe = m_effects[XPEL_NORMAL_SPRITE_1].end();
				break;
			case XPSL_MIDDLE2:
				m_vpiclist[XPSL_MIDDLE2] = v->m_mapmobj_cache[XPSL_MIDDLE2];
				sprIt = m_sprites[XPSL_MIDDLE2].begin();
				sprIe = m_sprites[XPSL_MIDDLE2].end();
				effIt = m_effects[XPEL_NORMAL_SPRITE_2].begin();
				effIe = m_effects[XPEL_NORMAL_SPRITE_2].end();
				break;
			case XPSL_FLY:
				m_vpiclist[XPSL_FLY].clear();
				sprIt = m_sprites[XPSL_FLY].begin();
				sprIe = m_sprites[XPSL_FLY].end();
				//Ŀǰû�������Ч�㡭�����Ծ��������end��
				effIt = m_effects[XPEL_NORMAL_SPRITE_1].end();
				effIe = m_effects[XPEL_NORMAL_SPRITE_1].end();
				break;
			case XPSL_BATTLE:
				m_vpiclist[XPSL_BATTLE].clear();
				sprIt = m_sprites[XPSL_BATTLE].begin();
				sprIe = m_sprites[XPSL_BATTLE].end();
				effIt = m_effects[XPEL_BATTLE_MID].begin();
				effIe = m_effects[XPEL_BATTLE_MID].end();
				break;
			default:
				XPASSERT(false && L"SortMid");
				break;
			}
			for(;sprIt != sprIe; ++sprIt)
			{
				Sprite *pSprite = *sprIt;
				if (!pSprite->TestViewport(v->m_viewport))
					continue;
				VPic vpic;
				pSprite->GetBase(vpic.base);
				vpic.pRenderable = pSprite;
				m_vpiclist[l].push_back(vpic);
			}

			//������Ч�ͱ����Ч�����ڵ�ͼ�༭���ŵ��в���Ч��
			NuclearBase base;
			NuclearLocation effLoc;
			for(; effIt != effIe;++effIt)
			{
				Effect* pEffect = *effIt;
				if( pEffect->GetBase(base) )
				{
					effLoc = pEffect->GetLocation();
					VPic vpic;
					NuclearPoint tmpCPoint;
					TransToDiamondRadix((base.left.x+ effLoc.x),(base.left.y+ effLoc.y),tmpCPoint);
					vpic.base.left = tmpCPoint.x;
					vpic.base.bottom = tmpCPoint.y;
					TransToDiamondRadix((base.right.x+ effLoc.x),(base.right.y+ effLoc.y),tmpCPoint);
					vpic.base.right = tmpCPoint.x;
					vpic.base.top = tmpCPoint.y;
					vpic.base.srcRect = pEffect->GetRelBouningBox().Widen(20) + effLoc.y;
					vpic.base.y = (base.right.y + base.left.y) / 2 + effLoc.y;
					vpic.pRenderable = pEffect;
					m_vpiclist[l].push_back(vpic);
				}
			}
			int sortn = (int)m_vpiclist[l].size();
			std::vector<TopologySortBase> sortlist(sortn);

			// ���������̫��ʱ���������ڴ�������⣬��Ҫ����
			for(int i=0; i<sortn; ++i)
			{
				sortlist[i].data = &m_vpiclist[l][i];
				sortlist[i].rect = m_vpiclist[l][i].GetRect();
				if( m_vpiclist[l][i].pRenderable )
					sortlist[i].sortBaseID = m_vpiclist[l][i].pRenderable->GetGUID();
				else
					sortlist[i].sortBaseID = ((ULONG64)m_vpiclist[l][i].pObjPic) | 0x8000000;
			}

			if (m_pEngine->GetConfigManager()->IsSortMapObjects())
			{
				//if (!bIsInBattle)//��ս������
				//{
				//	m_sortlist[l].Dump(sortlist, false);
				//}
				//else//ս������
				{
					//��y���꽵������
					m_sortlist[l].Dump(sortlist, false);
					std::sort(sortlist.begin(), sortlist.end(), TopologySortBase::CompareByY);
				}
			}

			m_sortCache[l].swap(sortlist);
		}
	}

	void World::PrefetchMapNotify(int count)
	{
		if (count < XPRMTT_COUNT)
		{
			NotifyLoadmapProgress(3 * count);//���3ûɶ��˼������˵���4����Դ���������˽���Ӧ����12%
			return;
		}
		m_pPrefetchMap = new Map(this, m_pEngine, &m_picResrcs);
		m_pPrefetchMap->m_bIsFull = m_pReadMapTask[0]->IsFull();
		m_pPrefetchMap->m_bLookupSegmpak = m_pReadMapTask[0]->NeedLookupSegmpak();
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
		NuclearRect vp(m_camera.GetLTPosFromCenterPos(m_PrefetchPt, 
			m_pPrefetchMap->m_width, m_pPrefetchMap->m_height), 
			m_camera.GetConstViewport().Width(), m_camera.GetConstViewport().Height());
		for (int i=0;i<XPRMTT_COUNT;++i)
		{
			delete m_pReadMapTask[i];
			m_pReadMapTask[i] = NULL;
		}
		m_nLoadingCount = 0;
		m_nTotalLoadingCount = 0;
		m_pPrefetchMap->AssureRegionResource(vp, true, true, true, 33);//���33����˵��4����Դ����������12%��Ȼ��Load��ͼ�Ŀ�ܣ�����img�ļ���������ͼƬ��Դ��ռ20%������33%��ʼ
		if (m_nTotalLoadingCount == 0)
		{
			m_nTotalLoadingCount = 1;
			//һ����Դ������Ҫload
			NotifyALoadItemFin(33, 96);
		}
	}

	bool World::PrefetchMapRes(const std::wstring &mapname, const std::wstring &mazename, const NuclearPoint &center, bool autoSwitch)
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
				NuclearFileIOManager* pFileIO = m_pEngine->GetFileIOManager();
				bool bIsSegmpak = false;
				bool bIsFull = true;
				m_pReadMapTask[XPRMTT_MAP] = new ReadMapTask(Map::GetMapResUrl(mapname), 
					mapname, pFileIO, this, bIsSegmpak, bIsFull);
				if (!m_pReadMapTask[XPRMTT_MAP]->Submit())
				{
					delete m_pReadMapTask[XPRMTT_MAP];
					m_pReadMapTask[XPRMTT_MAP] = NULL;
				}
				m_pReadMapTask[XPRMTT_MAZE] = new ReadMapTask(L"/map/" + mazename, 
					mazename, pFileIO, this, bIsSegmpak, bIsFull);
				if (!m_pReadMapTask[XPRMTT_MAZE]->Submit())
				{
					delete m_pReadMapTask[XPRMTT_MAZE];
					m_pReadMapTask[XPRMTT_MAZE] = NULL;
				}
				m_pReadMapTask[XPRMTT_SOUND] = new ReadMapTask(Map::GetSoundResUrl(mapname), 
					mapname, pFileIO, this, bIsSegmpak, bIsFull);
				if (!m_pReadMapTask[XPRMTT_SOUND]->Submit())
				{
					delete m_pReadMapTask[XPRMTT_SOUND];
					m_pReadMapTask[XPRMTT_SOUND] = NULL;
				}
				m_pReadMapTask[XPRMTT_STEP_SOUND] = new ReadMapTask(Map::GetStepSoundResUrl(mapname), 
					mapname, pFileIO, this, bIsSegmpak, bIsFull);
				if (!m_pReadMapTask[XPRMTT_STEP_SOUND]->Submit())
				{
					delete m_pReadMapTask[XPRMTT_STEP_SOUND];
					m_pReadMapTask[XPRMTT_STEP_SOUND] = NULL;
				}
				return true;
			}
			pMap = m_pPrefetchMap;
		}
		m_nLoadingCount = 0;
		m_nTotalLoadingCount = 0;
		if( pMap == NULL ) 
			return false;

		m_PrefetchPt = center;
		NuclearRect vp(m_camera.GetLTPosFromCenterPos(center, pMap->m_width, pMap->m_height), 
			m_camera.GetConstViewport().Width(), m_camera.GetConstViewport().Height());

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
			m_manualPrefetchEffects.end(), std::insert_iterator<std::set<Effect*> >(tmpEffect, tmpEffect.begin()));
		m_activeEffects.swap(tmpEffect);
		m_manualPrefetchPicResrcs.clear();
		m_manualPrefetchEffects.clear();
		m_camera.m_bDirty = true;
		if (m_pPrefetchMap)
			delete m_pPrefetchMap;
		m_pPrefetchMap = NULL;
	}

	void World::FreeAllPicReses()
	{
		UnloadMap();
		m_poolSurfaceRenderTargets.Destroy();
		if (m_hNightEffectRenderTarget != INVALID_PICTURE_HANDLE)
		{
			m_pEngine->GetRenderer()->FreeRenderTarget(m_hNightEffectRenderTarget);
			m_hNightEffectRenderTarget = INVALID_PICTURE_HANDLE;
		}
		Renderer* pRenderer = m_pEngine->GetRenderer();
		WarBackgroundMap::iterator wIt = m_wbm.begin(), wIe = m_wbm.end();
		for (;wIt!=wIe;++wIt)
		{
			pRenderer->FreePicture(wIt->second.picHandle);
		}
		m_wbm.clear();
		m_wbhm.clear();
		m_warBackGroundPic = INVALID_PICTURE_HANDLE;
		m_nWarBackGroundPicWidth = 0;
		m_nWarBackGroundPicHeight = 0;
		if (m_maskPicHandle != INVALID_PICTURE_HANDLE)
		{
			pRenderer->FreePicture(m_maskPicHandle);
			m_maskPicHandle = INVALID_PICTURE_HANDLE;
		}
		if (m_waterAlphaMask != INVALID_PICTURE_HANDLE)
		{
			pRenderer->FreePicture(m_waterAlphaMask);
			m_waterAlphaMask = INVALID_PICTURE_HANDLE;
		}
	}

	void World::OnRendererRestore()
	{
		m_poolSurfaceRenderTargets.Reset(GetSurfaceCacheCountByView(m_camera.GetConstViewport()),Map::REGION_WIDTH,Map::REGION_HEIGHT);
		if( m_pMap )
		{

			m_activePicResrcs.clear();
			for(PicResrcMap::iterator it= m_picResrcs.begin(), ie= m_picResrcs.end(); it != ie; ++it)
			{
				if( it->second->pooltype == XPPOOL_VIDEOMEM )
					it->second->handle = INVALID_PICTURE_HANDLE;
			}
			for(std::vector<RegionMap::RegionInfo>::iterator it =m_pMap->m_regions.begin(), ie = m_pMap->m_regions.end(); it != ie;++it)
				(*it).m_pSurfaceRenderTargetCache = NULL;
			m_pMap->AssureRegionResource(m_camera.GetConstViewport(), false, false, false, 0);
			m_camera.m_bDirty = true;
		}

		m_hNightEffectRenderTarget = INVALID_PICTURE_HANDLE;
	}

	void World::SetViewport(const NuclearLocation &loc/* Ŀ���Ӵ����ģ�Ҳ����Ҫ�������ǵ���λ��*/, bool syncRsrc/* �Ƿ��ȼ�����Դ*/)
	{
		if( m_pMap == NULL ) return;
		NuclearLocation lt = m_camera.GetLTPosFromCenterPos(loc, m_pMap->m_width, m_pMap->m_height);

		m_camera.SetPosition(lt.x, lt.y);
		for (int l=0;l<XPSL_COUNT;++l)
			m_sortCache[l].clear();

		if( syncRsrc )
		{
			m_pMap->AssureRegionResource(m_camera.GetConstViewport(), true, false, false, 0);
		}

		if( m_camera.m_pAttachedSprite )
			m_camera.m_pAttachedSprite->SetLocation(loc);
	}

	void World::SetViewportLT(int left, int top) // Ϊ�༭����ӣ��������ʱ���ô˺����ƶ��ӿڣ�
	{
		if (m_pMap)
		{
			if (left < 0) left = 0;
			if (top < 0) top = 0;
			const NuclearRect &vp = m_camera.GetConstViewport();
			if (left + vp.Width() > m_pMap->m_width)
				left = m_pMap->m_width - vp.Width();
			if (top + vp.Height() > m_pMap->m_height)
				top = m_pMap->m_height - vp.Height();
		}
		m_camera.SetPosition(left, top);
		if( m_pMap )
			m_pMap->AssureRegionResource(m_camera.GetConstViewport(), true, false, false, 0);
	}

	//rect����ҿ��ϱ��¿�����������
	bool World::SetMazeMask(const unsigned char* pData, const NuclearRect &rect)
	{
		if( m_pMap == NULL )
			return false;
		return m_pMap->m_maze.setMask(pData, rect);
	}

	unsigned int World::GetMazeMask(int x, int y)
	{
		if( m_pMap == NULL ) 
			return 0xFFFFFFFF;	//���е��ϰ�λ�������ϡ�
		return m_pMap->getMaze().getMask(x / astar::PathFinder::eGridSize, y / astar::PathFinder::eGridSize);
	}

	bool World::GetAStartPath(astar::Path& path, const Nuclear::NuclearLocation& start, const Nuclear::NuclearLocation& end)
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

	void World::GetSpriteShadowParam(float &shearX, float &scalingY) const
	{
		shearX = m_fSpriteShadowShearX;
		scalingY = m_fSpriteShadowScalingY;
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
			const NuclearRect &viewport = m_camera.GetConstViewport();
			const NuclearPoint view = viewport.GetPos();
			const DWORD  color = 0x3f00ffff;

			const float w = (float)viewport.Width();
			const float h = (float)viewport.Height();
			const float dx = DX - viewport.left % (int)DX;
			const float dy = DY - viewport.top % (int)DY;

			std::vector<NuclearFRectt> lines;
			for (float i = dx; i <= w; i += DX) { lines.push_back(NuclearFRectt(i, 0, i, h)); }
			for (float i = dy; i <= h; i += DY) { lines.push_back(NuclearFRectt(0, i, w, i)); }
			if (!lines.empty())
				renderer->DrawLine(&lines[0], int(lines.size()), color);
		}

		// draw Maze
		if (this->m_pEngine->GetConfigManager()->GetMazeMask() != 0)
		{
			Renderer * renderer = this->m_pEngine->GetRenderer();
			const NuclearRect &viewport = m_camera.GetConstViewport();
			const NuclearFPoint view = viewport.GetPos().ToFPOINT();

			const NuclearRect mazeRect = NuclearRect(int(viewport.left/DX), int(viewport.top/DY), int(viewport.right/DX), int(viewport.bottom/DY));
			//std::vector<NuclearFRectt> rects_unmove_and_unattack;   // red   0x3 0x3fff0000
			//std::vector<NuclearFRectt> rects_unmove;                // green 0x1 0x3f04fd16
			std::vector<std::vector<NuclearFRectt> > mazerects;
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
						//if ((mask & cit->mask) == cit->mask)
						{
							mazerects[mazecolorid].push_back(NuclearFRectt(NuclearFPoint(x * DX, y * DY), DX, DY)-view);
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

	void World::RenderMaskBox()
	{
		//����һ����ɫ�ɰ��
		Renderer * renderer = m_pEngine->GetRenderer();
		if (renderer && m_MaskBoxColor.data != 0xffffffff)
		{
			Nuclear::NuclearFRectt screenRect(0.0f, 0.0f, (float)m_camera.m_viewport.Width(), (float)m_camera.m_viewport.Height());
			renderer->DrawBox(screenRect, m_MaskBoxColor);
		}
	}

	IImmovableObj* World::NewImmovableObj(const std::wstring &objname, int layer/*Ŀǰ����Ϊ0��ֻ�еر���һ��*/, NuclearColor color, float freq) 
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

	bool World::GetWaterDepth(const NuclearLocation& pt, unsigned int &nDepth) const
	{
		nDepth = 0;
		if (!m_pMap)
			return true;
		if (pt.x < 0 || pt.y < 0)
			return false;
		if (pt.x >= m_pMap->m_width || pt.y >= m_pMap->m_height)
			return false;
		int x = pt.x / Map::REGION_WIDTH;
		int y = pt.y / Map::REGION_HEIGHT;
		int regioncol = m_pMap->m_regioncol;
		std::vector<Region::WaterArea> &areas = m_pMap->m_regions[x + y * regioncol].m_vWaterArea;
		std::vector<Region::WaterArea>::iterator iter;
		int border;
		for (iter=areas.begin();iter!=areas.end();++iter)
		{
			if (nDepth < iter->depth && iter->rect.PtInRect(pt))
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
					nDepth = iter->depth;
				} else {
					unsigned int tmp = static_cast<unsigned int>(Lerpf(0.0f, 
						static_cast<float>(iter->depth), 
						static_cast<float>(border) / iter->border));
					if (nDepth < tmp)
						nDepth = tmp;
				}
			}
		}
		return true;
	}
	//static float gs_ScaleDir = 1.0f;
	void World::OnTick(unsigned int now, unsigned int delta, unsigned int realDelta)
	{
		m_canvas.Clear();
		DeleteEffect();
		DeleteSprite();
		DeleteImmovableObj();

		//�������Ҫ�ھ�ͷ���µ�ǰ�氡������
		for (int layer=0;layer<XPSL_COUNT;++layer)
		{
			for (std::list<Sprite*>::iterator it = m_sprites[layer].begin(); it != m_sprites[layer].end(); ++it)
				(*it)->tick(now, delta);
		}

		long long TT = delta;

		if (m_iFlyOffsetYCur != m_iFlyOffsetYTgt)
		{
			if (m_iFlyOffsetYCur < m_iFlyOffsetYTgt)
			{
				m_iFlyOffsetYCur += m_iFlyOffsetYStep * TT / 1000.0f;
				if (m_iFlyOffsetYCur > m_iFlyOffsetYTgt)
				{
					m_iFlyOffsetYCur = m_iFlyOffsetYTgt;
				}
			}
			if (m_iFlyOffsetYCur > m_iFlyOffsetYTgt)
			{
				m_iFlyOffsetYCur -= m_iFlyOffsetYStep * TT / 1000.0f;
				if (m_iFlyOffsetYCur < m_iFlyOffsetYTgt)
				{
					m_iFlyOffsetYCur = m_iFlyOffsetYTgt;
				}
			}
			//SetUseSmooth(false);
			SetScale(10000.0f / (m_iFlyOffsetYCur + 10000));
		}
		else
		{
			//SetUseSmooth(true);
		}

		//if (GetScale() > 1.0f)
		//{
		//	gs_ScaleDir = -1.0f;
		//}
		//else if (GetScale() < 0.5f)
		//{
		//	gs_ScaleDir = 1.0f;
		//}
		//SetScale(GetScale() + 0.01f * gs_ScaleDir);

		m_camera.Update(now, realDelta, m_worldMode == XPWM_SIMPLE_BATTLE || m_worldMode == XPWM_WORLD_BATTLE);

		if (m_pMap){
			m_pMap->AssureRegionResource(m_camera.GetConstViewport(), true, false, false, 0);
		}
	}

	void World::UpdateSpriteAction(DWORD delta)
	{
		for (int layer=0;layer<XPSL_COUNT;++layer)
		{
			for (std::list<Sprite*>::iterator it = m_sprites[layer].begin(); it != m_sprites[layer].end(); ++it)
				(*it)->UpdateAction(delta);
		}
	}

	int World::GetAllSpriteCount() const
	{
		return (int)(m_sprites[0].size() + m_sprites[1].size() + m_sprites[2].size() + m_sprites[3].size());

	}
	void World::GetAllSpriteDetail(std::map<std::wstring, int> &detail) const
	{
		for (int layer=0;layer<XPSL_COUNT;++layer)
		{
			std::list<Sprite*>::const_iterator it = m_sprites[layer].begin(), ie = m_sprites[layer].end();
			for (;it!=ie;++it)
			{
				detail[(*it)->GetModelName()] += 1;
			}
		}
		
	}

	int World::GetAllEffectCount() const
	{
		int result = 0;
		int i;
		for (i = 0;i<XPEL_COUNT;++i)
		{
			result += (int)m_effects[i].size();
		}
		for (i = 0;i<XPSL_COUNT;++i)
		{
			std::list<Sprite*>::const_iterator it = m_sprites[i].begin(), ie = m_sprites[i].end();
			for (;it!=ie;++it)
			{
				result += (*it)->GetAllEffectCount();
			}
		}
		if (m_pMap)
		{
			std::vector<RegionMap::RegionInfo*>::const_iterator regIt = m_pMap->m_curRegions.begin(), 
				regIe = m_pMap->m_curRegions.end();
			for (;regIt!=regIe;++regIt)
			{
				for(i=0;i<XPPIC_ELEMENT_LAYER_COUNT;++i)
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
		for (i = 0;i<XPEL_COUNT;++i)
		{
			it = m_effects[i].begin();
			ie = m_effects[i].end();
			for (;it!=ie;++it)
			{
				detail[(*it)->GetName()] += 1;
			}
		}

		for (i = 0;i<XPSL_COUNT;++i)
		{
			std::list<Sprite*>::const_iterator sit = m_sprites[i].begin(), sie = m_sprites[i].end();
			for (;sit!=sie;++sit)
			{
				(*sit)->GetAllEffectDetail(detail);
			}
		}
		if (m_pMap)
		{
			std::vector<Region::EffectInfo>::const_iterator effIt, effIe;
			std::vector<RegionMap::RegionInfo*>::const_iterator regIt = m_pMap->m_curRegions.begin(), 
				regIe = m_pMap->m_curRegions.end();
			for (;regIt!=regIe;++regIt)
			{
				for(i=0;i<XPPIC_ELEMENT_LAYER_COUNT;++i)
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

	void World::SetNightEffect(bool bIsInBattle)
	{
		Camera* v = &m_camera;
		Renderer * renderer = m_pEngine->GetRenderer();
		/////////////////////////////////////////////////////////////
		// ҹ��Ч��
		//////////////////////////////////�������Ч����param
		sNuclearColorBalanceParam timeEffectParam;
		timeEffectParam.fCoefr = 0.0f;
		timeEffectParam.fCoefg = 0.0f;
		timeEffectParam.fCoefb = 0.0f;
		timeEffectParam.fContrastk = 1.0f;
		timeEffectParam.fContrastb = 0.0f;
		timeEffectParam.fLightness = 0.0f;
		timeEffectParam.fCentx = -1000.0f;
		timeEffectParam.fCenty = -1000.0f;
		NuclearFPoint timeEffectPos(-10000.0f, -10000.0f);
		if (v->m_pAttachedSprite && (!bIsInBattle))
		{
			NuclearPoint centerPos = v->m_pAttachedSprite->GetLocation();
			timeEffectPos.x = (float)(centerPos.x - v->m_viewport.left);
			timeEffectPos.y = (float)(centerPos.y - v->m_viewport.top);
		}

		if (m_pMap)
		{
			if (m_pMap->m_TimeEffects.size() == 0)//�Ժ�����Ż�ֱ�ӻ�
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
					if (timeEffectiter->first>=m_nGameTime)
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
				if ((length == 0) || (m_nGameTime == timeEffectiter->first))//ֻ��һ���ؼ�֡���߾��ǵ�ǰ֡
				{
					timeEffectParam = timeEffectiter->second;
				} else {
					if (length < 0)
						length += FIRE_A_DAY_TIME;
					int nPos = m_nGameTime - preIter->first;
					if (nPos < 0)
						nPos += FIRE_A_DAY_TIME;
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
				renderer->PushRenderTarget(m_hNightEffectRenderTarget);
			}
			else
			{
				if (m_pMap)
				{
					if (m_pMap->m_nSign & NuclearMap::MAP_SIGN_UNDERGROUND_PALACE)
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
	}

	void World::RenderMovingBackground()
	{
		Camera* v = &m_camera;
		Renderer * renderer = m_pEngine->GetRenderer();

		//�ƶ�����
		RegionMap::BackGoundInfoVet::iterator bgit = m_pMap->m_BackGroundInfos.begin(), bgie = m_pMap->m_BackGroundInfos.end();
		NuclearPoint viewportCenter = v->m_viewport.Center();
		DrawPictureParam dpp;
		NuclearFRectt bgRect;
		dpp.pRect = &bgRect;
		for (;bgit!=bgie;++bgit)
		{
			NuclearRect res;
			if (v->m_viewport.IsCross(bgit->activeRect, res))
			{
				NuclearPoint bgVet = viewportCenter - bgit->pos;
				bgVet.x = static_cast<int>(bgVet.x * bgit->relSpeed.x);
				bgVet.y = static_cast<int>(bgVet.y * bgit->relSpeed.y);
				int bgwidth = bgit->girdWidth * bgit->width;
				int bgheight = bgit->girdHeight * bgit->height;
				int renderwidth, renderheight;
				NuclearPoint pos = bgit->pos + bgVet;
				pos.x -= bgwidth / 2;
				pos.y -= bgheight / 2;
				NuclearPoint ltpos;
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
				NuclearRect rect(ltpos, renderwidth, renderheight);
				NuclearRect objRect;
				if (rect.IsCross(res, objRect))	//objRect����Ҫ����������������
				{
					//ltposΪҪ������������Ͻ����꣬��Ļ����
					ltpos.x = objRect.left - v->m_viewport.left;
					ltpos.y = objRect.top - v->m_viewport.top;
					objRect -= pos;  //����ƶ�����������

					if (objRect.left < 0)
					{
						XPASSERT(bgit->wrapHorz);	//�������ˮƽ���ƣ�rect.left����С��0
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
						XPASSERT(bgit->wrapVert);	//������Ǵ�ֱ���ƣ�rect.top����С��0
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
							//���ܻ����ۻ����ȿ���
							bgRect.right += bgit->girdWidth * v->GetScale();
						}
						bgRect.top = bgRect.bottom;
						bgRect.bottom += bgit->girdHeight * v->GetScale();

					} 
				}
			}
		}
	}

	void World::RenderRenderTargetCache()
	{
#if defined(WIN32) && defined(DEBUG)
		Renderer * renderer = m_pEngine->GetRenderer();
		float fScale = m_camera.GetScale();
		DrawBatchPictureParam dbpParam;
		NuclearRect objrect;
		NuclearFRectt frect, srcrect;
		int iRows = m_pMap->m_CurRegion.Height() / Map::REGION_HEIGHT;
		int iCols = m_pMap->m_CurRegion.Width()/Map::REGION_WIDTH;
		dbpParam.rectCount = 1;
		for (int iRow = 0; iRow < iRows; ++iRow)
		{
			for (int iCol = 0; iCol < iCols; ++iCol)
			{
				Map::RegionInfo *pRegion = m_pMap->m_curRegions[iCol + iRow * iCols];
				NuclearRect wrect(NuclearPoint(iCol*Map::REGION_WIDTH + m_pMap->m_CurRegion.left, iRow*Map::REGION_HEIGHT + m_pMap->m_CurRegion.top), Map::REGION_WIDTH, Map::REGION_HEIGHT);
				if (pRegion->m_pSurfaceRenderTargetCache == NULL)
					continue;

				if (wrect.Cut(m_camera.GetConstViewport(), objrect, srcrect))
				{
					DrawBatchPictureParam param;
					frect.left = objrect.left * fScale;
					frect.top = objrect.top * fScale;
					frect.right = objrect.right * fScale;
					frect.bottom = objrect.bottom * fScale;
					param.rectCount = 1;
					param.handle = pRegion->m_pSurfaceRenderTargetCache->m_hRenderTarget;
					param.pRect = &frect;
					param.pSrcrect = &srcrect;
					param.pColor = NULL;
					param.blend = Nuclear::XPTEXBM_COPY;
					renderer->DrawBatchPicture(param);
				}
			}
		}
#endif
	}
	void World::RenderStaticLayer(int layer, int drawTick, bool realRender)
	{
		DrawBatchPictureParam param;
		Camera *v = &m_camera;
		Renderer *renderer = m_pEngine->GetRenderer();
		param.rectCount = 1;

		if (realRender)
		{
			for(std::vector<CachePic>::const_iterator it = v->m_picele_cache[layer].begin(),ie = v->m_picele_cache[layer].end(); it != ie; ++it)
			{
				param.handle = it->handle;
				param.pRect = &(it->objRect);
				param.pSrcrect = &(it->srcRect);
				param.colorCount = 1;
				param.pColor = &(it->color);
				//renderer->DrawBatchPicture(param);
			}
			layer -= XPPIC_FIRST_ELEMENT_ID;
			//����cache effect��lko��distort�ļ���ֱ�ӻ�
			for(std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for(std::vector<Region::EffectInfo>::iterator it2 = (*it)->m_effects[layer].begin(), ie2 = (*it)->m_effects[layer].end(); it2 != ie2; ++it2)
				{
					if (!m_bShowEffect && layer <= XPEL_NORMAL_SPRITE_2)
						continue;

					Region::EffectInfo &einfo = *it2;
					if (IsRectCross(v->m_viewport, einfo.rect))
					{
						einfo.pEffect->Render(NULL, realRender);
					}
				}

				for (std::vector<LkoTree*>::const_iterator treeIter = (*it)->m_vpLkoTrees[layer].begin(),treeIE = (*it)->m_vpLkoTrees[layer].end();treeIter!=treeIE;++treeIter)
				{
					if ((*treeIter)->lastDrawTick != drawTick)
					{
						(*treeIter)->Render(NULL, realRender);
						(*treeIter)->lastDrawTick = drawTick;
					}
				}
				for (std::vector<Distort*>::const_iterator disIter = (*it)->m_vpDistorts[layer].begin(), disIE = (*it)->m_vpDistorts[layer].end(); disIter!=disIE;++disIter)
				{
					if ((*disIter)->lastDrawTick != drawTick)
					{
						(*disIter)->Render(NULL, realRender);
						(*disIter)->lastDrawTick = drawTick;
					}
				}
			}
		}
		else
		{
			layer -= XPPIC_FIRST_ELEMENT_ID;
			for (std::vector<RegionMap::RegionInfo*>::const_iterator it = m_pMap->m_curRegions.begin(), ie = m_pMap->m_curRegions.end(); it != ie; ++it)
			{
				for (std::vector<Region::EffectInfo>::iterator it2 = (*it)->m_effects[layer].begin(), ie2 = (*it)->m_effects[layer].end(); it2 != ie2; ++it2)
				{
					if (!m_bShowEffect && layer <= XPEL_NORMAL_SPRITE_2)
						continue;

					Region::EffectInfo &einfo = *it2;
					if (IsRectCross(v->m_viewport, einfo.rect))
					{
						einfo.pEffect->Render(NULL, realRender);
					}
				}
			}
		}
	}

	//����;��鶼��͸��
	void World::RenderSortedLayerWithoutAlpha(int layer, Canvas *pCanvas, bool realRender)
	{
		Renderer *renderer = m_pEngine->GetRenderer();
		DrawBatchPictureParam dbpParam;
		dbpParam.rectCount = 1;
		for(std::vector<TopologySortBase>::const_iterator it = m_sortCache[layer].begin(),ie = m_sortCache[layer].end(); it != ie; ++it)
		{
			VPic *p = (VPic*)it->data;
			if (p && p->pRenderable == NULL)
			{
				if (realRender)
				{
					dbpParam.handle = p->handle;
					dbpParam.pRect = &(p->rect);
					dbpParam.pSrcrect = &(p->srcrect);
					dbpParam.pColor = &((Region::PicEleWithBase*)p->pObjPic)->color;
					dbpParam.colorCount = 1;
					renderer->DrawBatchPicture(dbpParam);
				}
			}
			else if (p && p->pRenderable)
			{
				p->pRenderable->Render(pCanvas, realRender);
			}
		}
	}

	//ֻ���ƾ��飨����������������ľ��飩
	void World::RenderSortedLayerAlphaSpriteWithoutElement(int layer, Canvas *pCanvas, bool realRender)
	{
		Renderer *renderer = m_pEngine->GetRenderer();
		DrawBatchPictureParam dbpParam;
		dbpParam.rectCount = 1;
		ConfigManager *pCfgMan = m_pEngine->GetConfigManager();
		XPASSERT(pCfgMan && L"RenderSortedLayerNonAlphaSpriteWithAlphaElement");
		float fMaskAlpha = pCfgMan->GetMaskAlpha();
		for(std::vector<TopologySortBase>::const_iterator it = m_sortCache[layer].begin(),ie = m_sortCache[layer].end(); it != ie; ++it)
		{
			VPic *p = (VPic*)it->data;
			if(p->pRenderable != NULL)
			{
				p->pRenderable->RenderSpriteOnly(realRender);
			}
		}
	}

	//���ƾ��飬�Լ���͸���������������͸���ĳ���������ľ��飩
	void World::RenderSortedLayerNonAlphaSpriteWithAlphaElement(int layer, Canvas *pCanvas, bool realRender)
	{
		static void* testMember[3] = { 0, 0, 0 };
		testMember[0] = this;
		testMember[1] = m_pEngine;
		testMember[2] = &m_sortCache[layer];
		CCLOGERROR("member1=%x,member2=%x\n member3=%x\n", testMember[0], testMember[1], testMember[2]);

		Renderer *renderer = m_pEngine->GetRenderer();
		DrawBatchPictureParam dbpParam;
		dbpParam.rectCount = 1;
		ConfigManager *pCfgMan = m_pEngine->GetConfigManager();
		XPASSERT(pCfgMan && L"RenderSortedLayerNonAlphaSpriteWithAlphaElement");
		float fMaskAlpha = pCfgMan->GetMaskAlpha();
		for (std::vector<TopologySortBase>::const_iterator it = m_sortCache[layer].begin(), ie = m_sortCache[layer].end(); it != ie; ++it)
		{
			VPic *p = (VPic*)it->data;
			if (p && p->pRenderable == NULL)
			{
				if (realRender)
				{
					NuclearColor color = ((Region::PicEleWithBase*)p->pObjPic)->color;
					color.a = static_cast<unsigned char>(fMaskAlpha * color.a);
					dbpParam.handle = p->handle;
					dbpParam.pRect = &(p->rect);
					dbpParam.pSrcrect = &(p->srcrect);
					dbpParam.pColor = &color;
					dbpParam.colorCount = 1;
					renderer->DrawBatchPicture(dbpParam);
				}
			}
			else if (p && p->pRenderable)
			{
				p->pRenderable->RenderMid(pCanvas, realRender);
			}
		}
	}

	//ֻ�����������������������ľ��飩
	void World::RenderSortedLayerElementWithoutSprite(int layer, Canvas *pCanvas, bool realRender)
	{
		Renderer *renderer = m_pEngine->GetRenderer();
		DrawBatchPictureParam dbpParam;
		dbpParam.rectCount = 1;
		ConfigManager *pCfgMan = m_pEngine->GetConfigManager();
		XPASSERT(pCfgMan && L"RenderSortedLayerElementWithoutSprite");
		bool bRenderSoildMask = pCfgMan->IsRenderSolidMask();
		for (std::vector<TopologySortBase>::const_iterator it = m_sortCache[layer].begin(), ie = m_sortCache[layer].end(); it != ie; ++it)
		{
			VPic *p = (VPic*)it->data;
			if (p && p->pRenderable == NULL)
			{
				if (realRender && bRenderSoildMask)
				{
					dbpParam.handle = p->handle;
					dbpParam.pRect = &(p->rect);
					dbpParam.pSrcrect = &(p->srcrect);
					dbpParam.pColor = &((Region::PicEleWithBase*)p->pObjPic)->color;
					dbpParam.colorCount = 1;
					renderer->DrawBatchPicture(dbpParam);
				}
			}
			else if (p && p->pRenderable)
			{
				p->pRenderable->RenderBeforeMid(realRender);
			}
		}
	}

	void World::RenderSortedLayer(int layer, Canvas *pCanvas, bool realRender)
	{
		ConfigManager *pCfgMan = m_pEngine->GetConfigManager();
		XPASSERT(pCfgMan && L"RenderSortedLayer");
		Renderer *renderer = m_pEngine->GetRenderer();
		switch (pCfgMan->GetSpriteTranslucentType())
		{
		case XPSTT_NONE:
			RenderSortedLayerWithoutAlpha(layer, pCanvas, realRender);
			break;
		case XPSTT_ELEMENT_TRANSLUCENT:
			//RenderSortedLayerElementWithoutSprite(layer, pCanvas, realRender);
			RenderSortedLayerNonAlphaSpriteWithAlphaElement(layer, pCanvas, realRender);
			break;
		case XPSTT_SPRITE_TRANSLUCENT:
			{
				DrawBatchPictureParam dbpParam;
				RenderSortedLayerWithoutAlpha(layer, pCanvas, realRender);
				dbpParam.handle = m_pEngine->GetAlphaSpritePictureHandle();
				if (dbpParam.handle != INVALID_PICTURE_HANDLE)
				{
					dbpParam.rectCount = 1;
					if (realRender)
						renderer->PushRenderTarget(dbpParam.handle);
					RenderSortedLayerAlphaSpriteWithoutElement(layer, pCanvas, realRender);
					if (realRender)
					{
						renderer->PopRenderTarget();
						NuclearColor color;
						color.a = static_cast<unsigned char>(color.a * pCfgMan->GetMaskAlpha());
						dbpParam.pColor = &color;
						dbpParam.colorCount = 1;
					}
				}
			}
			break;
		}
	}

	void World::RenderDynamicEffect(Nuclear_EffectLayer layer, bool realRender)
	{
		for(EffectList::iterator it = m_effects[layer].begin(); it != m_effects[layer].end();++it)
		{
			//if (!m_bShowEffect && layer <= XPEL_NORMAL_SPRITE_2)
			//	continue;

			(*it)->Render(NULL, realRender);
		}
	}

	void World::Render(int now, bool UseCavas, bool realRender)
	{
		m_topSprEffect.clear();
		m_bottomSprEffect.clear();
		if (!m_bDrawWorld)
			return;

		Canvas *pCanvas = UseCavas ? &m_canvas : NULL;
		Camera* v = &m_camera;

		int drawTick = m_pEngine->GetTick();
		static int lastDrawTick = drawTick;
		if (lastDrawTick == drawTick)
			--drawTick;
		lastDrawTick = drawTick;
		Renderer * renderer = m_pEngine->GetRenderer();

		bool bIsInBattle = m_worldMode == XPWM_SIMPLE_BATTLE || m_worldMode == XPWM_WORLD_BATTLE;
		
		cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColor);
		cocos2d::ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords | cocos2d::kCCVertexAttribFlag_Color);

		if (realRender)
		{
			//////////////////////////////////////////////////
			SetNightEffect(bIsInBattle);

			//��ͼ//////////////////////////////////////////
			DrawPictureParam dpParam;

			if (m_pMap)
			{
				if (m_worldMode != XPWM_SIMPLE_BATTLE)//����������ڼ�ս��״̬��
				{
					RenderMovingBackground();
					g_timeuserlog.Push(XPTimeUseLog::TUL_MOVINGBACKGROUND);

					// ��RenderTargetCache���Ƶر�������Ͳ�
					if (m_pEngine->GetConfigManager()->IsUseSurfaceCache())
					{
						RenderRenderTargetCache();
					}
					else
					{
						// ��ͳ�������ر��
						dpParam.pSrcrect = NULL;
						int i = 0;
						for (; i < XPPIC_GROUND_LAYER_COUNT; ++i)
						{
							for (std::vector<GroundCachePic>::const_iterator it = v->m_ground_picele_cache[i].begin(), ie = v->m_ground_picele_cache[i].end(); it != ie; ++it)
							{
								dpParam.handle = it->handle;
								dpParam.pRect = &(it->objRect);
								dpParam.colorCount = 4;
								dpParam.pColor = it->color;
								renderer->DrawPicture(dpParam);
							}
						}
						// ��ͳ������ˮ1 ˮ2
						for (; i < XPPIC_CATCH_LAYER_COUNT; ++i)
						{
							for (std::vector<CachePic>::const_iterator it = v->m_picele_cache[i].begin(), ie = v->m_picele_cache[i].end(); it != ie; ++it)
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
					//��ˮ����ˮ2�͵�1֮��
					for (int j = 0; j < SubMap::NuclearGround::WATER_EFFECT_COUNT; ++j)
						m_pMap->m_waterEffects[j].Render(realRender);
					// �� ��1 ��2 ��3 ��4 ��4��
					for (int i = XPPIC_CATCH_LAYER_COUNT; i < XPPIC_MID1_ELEMENT_LAYER_ID; ++i)
					{
						RenderStaticLayer(i, drawTick, realRender);
					}
				}//if (m_worldMode != XPWM_SIMPLE_BATTLE)
			}

			g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERLOWLAYER);
			// ����͵�����Ʒ
			if (!bIsInBattle)
			{
				for (std::list<ImmovableObj*>::iterator it = m_immobjs.begin(), ie = m_immobjs.end(); it != ie; ++it)
				{
					(*it)->Render(pCanvas, realRender);
				}
				renderer->BatchDrawParticles();
			}
			if (m_pMap)
			{
				if (!bIsInBattle)
				{
					//������֮�µر�֮����Ч
					RenderDynamicEffect(XPEL_UNDER_SPRITE, realRender);

					//���������ϵ����²���Ч
					EffectList::iterator eIt = m_bottomSprEffect.begin(), eIe = m_bottomSprEffect.end();
					for (; eIt != eIe; ++eIt)
					{
						(*eIt)->Render(NULL, realRender);
					}
					m_bottomSprEffect.clear();

					g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERBEFOREMID);
					//����1��
					RenderSortedLayer(0, pCanvas, realRender);
					g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERMID);
					// ������֮����Ч
					RenderDynamicEffect(XPEL_NORMAL_ABOVE_SPRITE, realRender);
					//����2��
					RenderSortedLayer(1, pCanvas, realRender);
				}

				if (m_worldMode != XPWM_SIMPLE_BATTLE)
				{
					g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERAFTERMID);

					// �� ��ͼ����߲�
					dpParam.pColor = NULL;
					dpParam.colorCount = 0;
					for (int i = XPPIC_MID1_ELEMENT_LAYER_ID + 1; i < XPPIC_LAYER_COUNT; ++i)
					{
						RenderStaticLayer(i, drawTick, realRender);
					}
				} //if ( m_worldMode != XPWM_SIMPLE_BATTLE)
				if (!bIsInBattle)
				{
					// �����ϲ���Ч
					RenderDynamicEffect(XPEL_NORMAL_TOP, realRender);
					g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERHIGHT);
				}
				else
				{ //if (!bIsInBattle)
					//��ս������
					if (m_warBackGroundPic != INVALID_PICTURE_HANDLE)
					{
						NuclearDisplayMode mode = m_pEngine->GetRenderer()->GetDisplayMode();
						NuclearFRectt rect;
						switch (m_warBackGroundType)
						{
						case XPWBT_CENTER:
						{
							rect.left = (mode.width - m_nWarBackGroundPicWidth) / 2.0f;
							rect.top = (mode.height - m_nWarBackGroundPicHeight) / 2.0f;
							rect.right = rect.left + m_nWarBackGroundPicWidth;
							rect.bottom = rect.top + m_nWarBackGroundPicHeight;
						}
						break;
						case XPWBT_SCALE:
						{
							float height = mode.height + m_fBattleBackgroundEdge * 2;
							float width = mode.width + m_fBattleBackgroundEdge * 2;
							if (m_nWarBackGroundPicWidth * height > m_nWarBackGroundPicHeight * width)
							{//ս��������߱ȴ�����Ļ��߱ȣ����ˣ�
								rect.top = -m_fBattleBackgroundEdge;
								rect.bottom = height - m_fBattleBackgroundEdge;
								rect.right = height * m_nWarBackGroundPicWidth / m_nWarBackGroundPicHeight;
								rect.left = (width - rect.right) / 2.0f;
								rect.right += rect.left;
							}
							else {//ս��������߱�С����Ļ��߱ȣ�խ�ˣ����ߵ��ڣ�
								rect.left = -m_fBattleBackgroundEdge;
								rect.right = width - m_fBattleBackgroundEdge;
								rect.bottom = width * m_nWarBackGroundPicHeight / m_nWarBackGroundPicWidth;
								rect.top = (height - rect.bottom) / 2.0f;
								rect.bottom += rect.top;
							}
						}
						break;
						default:
							XPASSERT(false && "switch warbackgroundtype");
						}
						const NuclearPoint &adj = v->GetShakeViewportAdj();
						rect -= adj;
						renderer->DrawPicture(m_warBackGroundPic, rect, 0xFFFFFFFF);
					}
					//������֮�µر�֮����Ч
					RenderDynamicEffect(XPEL_BATTLE_LOW, realRender);

					//���������ϵ����²���Ч��ӵ�������
					for (std::vector<TopologySortBase>::const_iterator it = m_sortCache[XPSL_BATTLE].begin(), ie = m_sortCache[XPSL_BATTLE].end(); it != ie; ++it)
					{
						VPic *p = (VPic*)it->data;
						if (p->pRenderable != NULL)
						{
							p->pRenderable->RenderBottom(pCanvas);
						}
					}
					//���������ϵ����²���Ч
					EffectList::iterator eIt = m_bottomSprEffect.begin(), eIe = m_bottomSprEffect.end();
					for (; eIt != eIe; ++eIt)
					{
						(*eIt)->Render(NULL, realRender);
					}
					m_bottomSprEffect.clear();

					//��ս������
					for (std::vector<TopologySortBase>::const_iterator it = m_sortCache[XPSL_BATTLE].begin(), ie = m_sortCache[XPSL_BATTLE].end(); it != ie; ++it)
					{
						VPic *p = (VPic*)it->data;
						if (p->pRenderable != NULL)
						{
							p->pRenderable->RenderMid(pCanvas, realRender);
						}
					}
					// ������֮����Ч
					RenderDynamicEffect(XPEL_BATTLE_HEIGHT, realRender);

				}//if (!bIsInBattle)

				//���������ϵ����ϲ���Ч
				EffectList::iterator eIt = m_topSprEffect.begin(), eIe = m_topSprEffect.end();
				for (; eIt != eIe; ++eIt)
				{
					(*eIt)->Render(NULL, realRender);
				}

				m_topSprEffect.clear();

				if (m_pEngine->GetConfigManager()->IsRenderNightEffectByShader())
				{
					/////////////////////////////////////////////////
					if (m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget())
					{
						renderer->PopRenderTarget();
						//m_timeEffectiter
						DrawPictureParam dpParam;
						NuclearFRectt rect = v->GetConstViewport().ToFRECT();
						rect.bottom -= rect.top;
						rect.top = 0.0f;
						rect.right -= rect.left;
						rect.left = 0.0f;

						dpParam.handle = m_hNightEffectRenderTarget;
						dpParam.pRect = &rect;
						NuclearColor color = 0xffffffff;
						dpParam.pColor = &color;
						dpParam.colorCount = 1;
						dpParam.blend = XPTEXBM_COPY;
						//timeEffectParam.nColorOpType = XPCOLOROP_SELECTARG1;

						int todoShader;
						if (m_pMap->m_nSign & NuclearMap::MAP_SIGN_UNDERGROUND_PALACE)
						{
							renderer->SelectRenderEffect(XPRE_COLORBALANCE_2);
						}
						else {
							renderer->SelectRenderEffect(XPRE_COLORBALANCE);
						}

						renderer->DrawPicture(dpParam);
					}
					renderer->SelectRenderEffect(XPRE_NULL);
				}

				/////////////////////////////////////////////////////////////
				// ��ͼЧ��
				if (m_pEngine->GetConfigManager()->IsEnableMaskPic() && m_pMap)
				{
					//////////////////////////////////�����ͼЧ����param
					if (m_pMap->m_maskMap.size() > 0)
					{
						sNuclearMaskParam maskParam;
						maskParam.alpha = 0;
						maskParam.lastAlpha = 0;
						SubMap::MaskParamMap::iterator beginIter = m_pMap->m_maskMap.begin(),
							endIter = m_pMap->m_maskMap.end();
						SubMap::MaskParamMap::iterator maskIter = beginIter, imgIter = endIter;
						for (; maskIter != endIter; ++maskIter)
						{
							if (maskIter->first >= m_nGameTime)
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
						if ((length == 0) || (m_nGameTime == maskIter->first))//ֻ��һ���ؼ�֡���߾��ǵ�ǰ֡
						{
							maskParam = maskIter->second;
							imgIter = maskIter;
						}
						else {
							if (length < 0)
								length += FIRE_A_DAY_TIME;
							int nPos = m_nGameTime - preIter->first;
							if (nPos < 0)
								nPos += FIRE_A_DAY_TIME;
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
							Nuclear::NuclearFRectt rect(0.0f, 0.0f, (float)v->m_viewport.Width(), (float)v->m_viewport.Height());
							Nuclear::NuclearFRectt srcrect;
							srcrect.left = static_cast<float>(v->m_viewport.left) / m_pMap->m_width;
							srcrect.top = static_cast<float>(v->m_viewport.top) / m_pMap->m_height;
							srcrect.right = static_cast<float>(v->m_viewport.right) / m_pMap->m_width;
							srcrect.bottom = static_cast<float>(v->m_viewport.bottom) / m_pMap->m_height;
							Nuclear::NuclearColor color;
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

				//���Թ������������
				RenderMaze();

				//����һ����ɫ�ɰ��
				RenderMaskBox();
			}

			//m_bIsLostDevice = false;
			g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERWORLDEND);
		}
		else
		{//����ʵ��Ⱦ
			if (m_pMap)
			{
				if (m_worldMode != XPWM_SIMPLE_BATTLE)//����������ڼ�ս��״̬��
				{
					//��ˮ����ˮ2�͵�1֮��
					for (int j = 0; j < SubMap::NuclearGround::WATER_EFFECT_COUNT; ++j)
						m_pMap->m_waterEffects[j].Render(realRender);
					// �� ��1 ��2 ��3 ��4 ��4��
					for (int i = XPPIC_CATCH_LAYER_COUNT; i < XPPIC_MID1_ELEMENT_LAYER_ID; ++i)
					{
						RenderStaticLayer(i, drawTick, realRender);
					}
				}//if (m_worldMode != XPWM_SIMPLE_BATTLE)
			}

			// ����͵�����Ʒ
			if (!bIsInBattle)
			{
				for (std::list<ImmovableObj*>::iterator it = m_immobjs.begin(), ie = m_immobjs.end(); it != ie; ++it)
				{
					(*it)->Render(pCanvas, realRender);
				}
			}
			if (m_pMap)
			{
				if (!bIsInBattle)
				{
					//������֮�µر�֮����Ч
					RenderDynamicEffect(XPEL_UNDER_SPRITE, realRender);

					//���������ϵ����²���Ч
					EffectList::iterator eIt = m_bottomSprEffect.begin(), eIe = m_bottomSprEffect.end();
					for (; eIt != eIe; ++eIt)
					{
						(*eIt)->Render(NULL, realRender);
					}
					m_bottomSprEffect.clear();

					//����1��
					RenderSortedLayer(0, pCanvas, realRender);
					// ������֮����Ч
					RenderDynamicEffect(XPEL_NORMAL_ABOVE_SPRITE, realRender);
					//����2��
					RenderSortedLayer(1, pCanvas, realRender);
				}

				if (m_worldMode != XPWM_SIMPLE_BATTLE)
				{
					for (int i = XPPIC_MID1_ELEMENT_LAYER_ID + 1; i < XPPIC_LAYER_COUNT; ++i)
					{
						RenderStaticLayer(i, drawTick, realRender);
					}
				} //if ( m_worldMode != XPWM_SIMPLE_BATTLE)
				if (!bIsInBattle)
				{
					// �����ϲ���Ч
					RenderDynamicEffect(XPEL_NORMAL_TOP, realRender);
				}
				else
				{
					//������֮�µر�֮����Ч
					RenderDynamicEffect(XPEL_BATTLE_LOW, realRender);

					//���������ϵ����²���Ч
					EffectList::iterator eIt = m_bottomSprEffect.begin(), eIe = m_bottomSprEffect.end();
					for (; eIt != eIe; ++eIt)
					{
						(*eIt)->Render(NULL, realRender);
					}
					m_bottomSprEffect.clear();

					//��ս������
					for (std::vector<TopologySortBase>::const_iterator it = m_sortCache[XPSL_BATTLE].begin(), ie = m_sortCache[XPSL_BATTLE].end(); it != ie; ++it)
					{
						VPic *p = (VPic*)it->data;
						if (p->pRenderable != NULL)
						{
							p->pRenderable->RenderMid(pCanvas, realRender);
						}
					}
					// ������֮����Ч
					RenderDynamicEffect(XPEL_BATTLE_HEIGHT, realRender);

				}//if (!bIsInBattle)

				//���������ϵ����ϲ���Ч
				EffectList::iterator eIt = m_topSprEffect.begin(), eIe = m_topSprEffect.end();
				for (; eIt != eIe; ++eIt)
				{
					(*eIt)->Render(NULL, realRender);
				}

				m_topSprEffect.clear();
			}
		}
		cocos2d::CCShaderCache::sharedShaderCache()->popShader();
	}
    
    void World::RenderCaptureWorld()
    {
		if( !m_bDrawWorld )
			return;

		Camera* v = &m_camera;

		Renderer * renderer = m_pEngine->GetRenderer();

		cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColor);

		DrawPictureParam dpParam;
        
		if( m_pMap )
		{
            dpParam.pSrcrect = NULL;
            int i = 0;
            for (; i < XPPIC_GROUND_LAYER_COUNT; ++i)
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

            for(; i<XPPIC_CATCH_LAYER_COUNT; ++i)
            {
                for(std::vector<CachePic>::const_iterator it = v->m_picele_cache[i].begin(), ie = v->m_picele_cache[i].end(); it != ie; ++it)
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

		cocos2d::CCShaderCache::sharedShaderCache()->popShader();
	}

	/*
	* ���������Ż��ĺ���
	* ʹ��m_sortMap
	* �����������������򱾴δ����������
	* �²��������ֻ������������е�����ͬ�����
	*/
	void World::LocalSort(std::vector<TopologySortBase> &v, int l)
	{
		//��ʼ��map �����������ݼ��뵽map��
		std::map<SortBaseID,int> sortMap;
		int pos = 0, n = (int)v.size();
		for(int i=0; i<n; ++i){
			sortMap.insert(std::make_pair(v[i].sortBaseID, i));
		}

		int index=0;
		std::map<SortBaseID,int>::iterator ir;
		for(std::vector<TopologySortBase>::const_iterator it = m_sortCache[l].begin(), ie = m_sortCache[l].end(); it != ie; ++it)
		{			
			index = -1;
			ir = sortMap.find(it->sortBaseID);
			if( ir == sortMap.end()){				
			}else{
				index=ir->second;
			}

			if( index != -1 ){
				if( index!= pos ){
					//�������ݣ�����sortMap�е�������Ϣ
					std::swap(v[pos],v[index]);			
					sortMap[v[index].sortBaseID]=index;
				}
				++pos;
			}			
		}

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
	}

	void World::SetSpriteShadowParam(const float &shearX, const float &scalingY)
	{
		m_fSpriteShadowShearX = shearX;
		m_fSpriteShadowScalingY = scalingY;
	}

	void World::SetFlyOffsetYTgt(int iFlyOffsetYTgt)
	{
		m_iFlyOffsetYTgt = iFlyOffsetYTgt;
	}
	void World::SetFlyOffsetYCur(int iFlyOffsetYCur)
	{
		m_iFlyOffsetYCur = iFlyOffsetYCur;
	}
	int World::GetFlyOffsetYCur()
	{
		return m_iFlyOffsetYCur;
	}
	void World::SetFlyOffsetYStep(int iFlyOffsetYStep)
	{
		m_iFlyOffsetYStep = iFlyOffsetYStep;
		if (m_iFlyOffsetYStep < 0)
		{
			m_iFlyOffsetYStep *= -1;
		}
	}
	int World::GetFlyOffsetYStep()
	{
		return m_iFlyOffsetYStep;
	}

	void World::SetScale(float scale) 
	{ 
		m_camera.SetScale(scale); 
		m_poolSurfaceRenderTargets.SetSize(GetSurfaceCacheCountByView(m_camera.GetConstViewport()));
	}

	void World::SetUseSmooth(bool bSmooth)
	{
		m_camera.SetUseSmooth(bSmooth);
	}

	int World::GetGameTime() const { 
		if (m_pMap->m_nSign & NuclearMap::MAP_SIGN_ALWAYS_DAYTIME)
			return FIRE_MID_DAY;
		if (m_pMap->m_nSign & NuclearMap::MAP_SIGN_ALWAYS_NIGHT)
			return FIRE_MID_NIGHT;
		return m_nGameTime; 
	}

	void World::SetWorldMode(eNuclearWorldMode m)
	{
		m_worldMode = m;
		std::list<Sprite*>::iterator it, ie;
		bool bActive = false;
		switch (m_worldMode)
		{
		case XPWM_NORMAL:
		case XPWM_FLY:
			//����ͨ������ɼ����,��ս����ʱ�������Ϊս������Ӧ�ö�Ҫ���٣����ԾͲ��ý�ս��������Ϊ�Ǽ�����
			bActive = true;
			break;
		case XPWM_SIMPLE_BATTLE:
		case XPWM_WORLD_BATTLE:
			//����ͨ������ɲ������
			bActive = false;
			break;
		}
		for (int layer=0;layer<XPSL_WORLD_SPRITE_COUNT;++layer)
		{
			it = m_sprites[layer].begin();
			ie = m_sprites[layer].end();
			for (;it!=ie;++it)
			{
				(*it)->SetActive(bActive);
			}
		}
	}

}
