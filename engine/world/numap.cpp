#include "../common/numapconst.h"
#include "../common/nulog.h"
#include "../common/nuxptimeuselog.h"
#include "../renderer/nurenderer.h"
#include "../effect/nueffect.h"
#include "../engine/nuconfigmanager.h"
#include "../common/nuljfmutil.h"
#include "nuworld.h"
#include "numap.h"

#include "../../dependencies/LJXML//Include/LJXML.hpp"
#include "../soundarea/xpenvsoundcirclearea.h"
#include "../soundarea/xpenvsoundpolygonarea.h"
#include "../map/nupstepsoundmap.h"

namespace Nuclear
{
	extern XPTimeUseLog g_timeuserlog;

	class MapPicElementReadTask : public NuclearFileIOManager::AsyncReadTask
	{
		World *m_pWorld;
		EngineBase *m_pEB;
		PicResrc *m_pPicResrc;
		unsigned int m_mapguid;
		int64_t m_tick;
		bool m_bCallBack;
		unsigned char m_beginPro;
		unsigned char m_endPro;
		cocos2d::CCImage* m_pImage;
	public:
		MapPicElementReadTask(const std::wstring filename, NuclearFileIOManager *pFileIOMan, World *pWorld, Map *pMap,
			PicResrc *pPicResrc, bool callback, unsigned char beginPro, unsigned char endPro, bool bLookupSegmpak)
			: NuclearFileIOManager::AsyncReadTask(pFileIOMan, filename, 3, bLookupSegmpak), m_pWorld(pWorld), m_pEB(pMap->m_pEB),
			m_pPicResrc(pPicResrc), m_bCallBack(callback), m_beginPro(beginPro), m_endPro(endPro), m_pImage(NULL)
		{
			m_mapguid = pMap->GetGUID();
			m_tick = GetMilliSeconds();
		}

		virtual void Execute()
		{
			NuclearFileIOManager::AsyncReadTask::Execute();

			loadCCImage();
		}

		void loadCCImage()
		{
			if (m_data.size() > 0)
			{
				m_pImage = new cocos2d::CCImage;
				NuclearTextureFormat textureFormat = XPTEXFMT_DEFAULT;
				if (!m_pEB->GetRenderer()->LoadCCImageFromMem(m_pImage, textureFormat, m_data.constbegin(), m_data.size()))
				{
					CC_SAFE_RELEASE_NULL(m_pImage);
				}
			}
		}

		virtual void OnReady()
		{
			int64_t tick = GetMilliSeconds();
			if (m_data.size() == 0)
			{
				XPLOG_ERROR(L"load imgfile failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"load imgfile failed!, path is %s\n", m_filename.c_str());
			}
			else
			{
				m_pFileIOMan->OnReady(int(tick - m_tick), m_data.size());
				//上面这段放在检查环境上面，load错了的东西也统计好了

				if (m_pWorld->CheckMapGUID(m_mapguid) &&
					m_pPicResrc->handle == INVALID_PICTURE_HANDLE) // 检查环境
				{
					//下面这段为啥不在else里面是为了在 Renderer 中加入错误替代图片方便调试，load不到就可以显示一个Error
					//XPTRACE(L"load imgfile ok, path is %s\n", filename.c_str());
					NuclearPictureInfo picinfo;
					picinfo.fileuri = m_pPicResrc->uri;
					PictureHandle handle = INVALID_PICTURE_HANDLE;
					if (m_pImage)
					{
						handle = m_pEB->GetRenderer()->LoadPictureFromCCImage(m_pImage, XPTEXFMT_DEFAULT, &picinfo, m_pEB->GetConfigManager()->GetCreateTexturePoolType(), m_pEB->GetRenderer()->CacheInMemory());
						CC_SAFE_RELEASE_NULL(m_pImage);
					}
					else
					{
						handle = m_pEB->GetRenderer()->LoadPictureFromMem(m_data.constbegin(), m_data.size(), &picinfo, XPTEXFMT_DEFAULT,
							m_pEB->GetConfigManager()->GetCreateTexturePoolType(), m_pEB->GetRenderer()->CacheInMemory());
					}
					if (handle != INVALID_PICTURE_HANDLE)
					{
						if (m_pEB->GetRenderer()->CacheInMemory() == false)
							m_pEB->GetRenderer()->CachePicture(handle, m_filename);
						m_pPicResrc->handle = handle;
						m_pPicResrc->pooltype = picinfo.m_pooltype;
					}
				}
				if (m_bCallBack)
				{
					m_pWorld->NotifyALoadItemFin(m_beginPro, m_endPro);
				}
			}
			delete this;
		}
		virtual void OnDiscard()
		{
			delete this;
		}
		virtual ~MapPicElementReadTask()
		{
			CC_SAFE_RELEASE_NULL(m_pImage);
		}
	};

	Map::~Map()
	{
		if (m_pWaterData)
			XFree(m_pWaterData);
		if (m_pGroundColor)
			XFree(m_pGroundColor);
		DeleteSoundMap();
	}

	void Map::DeleteSoundMap()
	{
		std::vector<XPSoundArea*>::iterator it = m_SoundAreas.begin(), ie = m_SoundAreas.end();
		for (; it != ie; ++it)
		{
			delete (*it);
		}
		m_SoundAreas.clear();
		//if (m_pBackGroundDefaultArea)
		//	delete m_pBackGroundDefaultArea;
		//m_pBackGroundDefaultArea = NULL;
		if (m_NuclearStepSoundMap)
			delete m_NuclearStepSoundMap;
		m_NuclearStepSoundMap = NULL;
	}

	void Map::FreeSoundMap()
	{
		DeleteSoundMap();
		std::vector<RegionInfo>::iterator it = m_regions.begin(), ie = m_regions.end();
		for (; it != ie; ++it)
		{
			it->m_EnvSoundAreas.clear();
			//it->m_BkgSoundAreas.clear();
			//it->m_BkgVolumeAreas.clear();
		}

	}

	struct MMHeader
	{
		enum { eMinSize = 12 };
		unsigned int hsize;
		unsigned int width;
		unsigned int height;
		MMHeader() { hsize = eMinSize; }
	};

	void Map::Maze::reset(Map * map)
	{
		this->width = map->m_width / astar::PathFinder::eGridSize;
		this->height = map->m_height / astar::PathFinder::eGridSize;
		this->buffer.resize(this->width * this->height);
		::memset(this->buffer.normalbegin(), 0, this->buffer.size());
		this->maze   = (unsigned char *)this->buffer.normalbegin();
	}

	void Map::Maze::SetPathFinderMaze(Map * map)
	{
		if (astar::PathFinder * finder = map->m_pEB->GetPathFinder())
			finder->setAMap(astar::AMap(width, height, 0, this->maze));
	}

	bool Map::Maze::Load(NuclearBuffer &buffer)
	{
		if (buffer.size() < MMHeader::eMinSize)
			return false;

		const MMHeader * header = (const MMHeader*)buffer.constbegin();
		if (header->hsize < MMHeader::eMinSize)
			return false;

		if ((int)buffer.size() != header->width * header->height + header->hsize)
			return false;

		// 保存数据并配置 PathFinder。
		this->buffer.resize(buffer.size());
		memcpy(this->buffer.normalbegin(), buffer.constbegin(), buffer.size());
		this->width  = header->width;
		this->height = header->height;
		this->maze   = (unsigned char *)this->buffer.normalbegin() + header->hsize;

		return true;
	}

	bool Map::Maze::Load(const void* mazedata, size_t size)
	{
		NuclearBuffer buffer(mazedata, size);
		return Load(buffer);
	}

	bool Map::Maze::Load(Map * map, const std::wstring & mazename)
	{
		NuclearBuffer buffer;

		if (false == map->m_pEB->GetFileIOManager()->GetFileImage(mazename, buffer))
			return false;
		return Load(buffer);
	}


	bool Map::SetMapMaze(const void* mazeBuffer, size_t size)
	{
		if (m_maze.Load(mazeBuffer, size))
		{
			UseMapMaze();
			return true;
		}
		return false;
	}

	void Map::UseMapMaze()
	{
		m_maze.SetPathFinderMaze(this);
	}

	bool Map::Load(const NuclearBuffer &pmapbuffer, NuclearBuffer &mazebuffer, const NuclearBuffer &soundXMLbuffer,
		const NuclearBuffer &stepsound, const SubMap::PPathMap* pathMap, int pro)
	{
		if (!m_pWorld->NotifyLoadmapProgress(6 + pro)) return false;
		if (pmapbuffer.size() == 0)
			return false;
		NuclearMap pmap;
		if (!pmap.LoadFromMem(pmapbuffer))
			return false;

		m_nSign = pmap.GetSign();

		if (!LoadBaseInfo(pmap))//这个方法目前永远return true
			return false;

		if (!LoadGround(pmap, pathMap))
			return false;

		//把记录地表颜色的内存块的所有权从pmap转移给Map
		m_pGroundColor = pmap.GetColorData();
		pmap.m_Ground.SetColorNULL();
		//
		Renderer *pr = m_pEB->GetRenderer();
		if (!m_pWorld->NotifyLoadmapProgress(8 + pro)) return false;
		//水面层
		m_hasWater = false;
		XapBatch tmpBatch(m_pEB);
		for (int i=0;i<SubMap::NuclearGround::WATER_EFFECT_COUNT;++i)
		{
			m_waterEffects.push_back(tmpBatch);
			if (m_bIsFull)
			{
				m_waterEffects[i].SetSegmPak(m_bLookupSegmpak);
				m_waterEffects[i].SetAni(pmap.GetWaterName(i));//这里是同步加载的
				if (pmap.GetWaterName(i) != L"")
				{
					m_hasWater = true;
				}
			}
			
		}
		int tmpsize = pmap.m_Ground.GetWaterSize();
		m_pWaterData = static_cast<SubMap::uWaterTileType*>(XAlloc(tmpsize));
		memcpy(m_pWaterData, pmap.GetWaterData(), tmpsize);
		m_widthForWater = pmap.GetWidthForWater();
		m_heightForWater = pmap.GetHeightForWater();


		if (!m_pWorld->NotifyLoadmapProgress(10 + pro)) return false;

		LoadingPicResrcMap picMap;
		LoadingLinkedObjMap lkoMap;
		LoadingDistortObjMap disMap;
		ElementNames names;
		if (m_bIsFull)
		{
			if (!LoadElementPicMap(pmap, picMap, lkoMap, disMap, names, m_bLookupSegmpak))
				return false;
		}

		if (!m_pWorld->NotifyLoadmapProgress(14 + pro)) return false;

		if (m_bIsFull)
		{
			if (!LoadElements(pmap, picMap))
				return false;
		}

		if (!LoadEffects(pmap))
			return false;
		

		//水面区域...
		if (!LoadWaterArea(pmap))
			return false;

		if (!m_pWorld->NotifyLoadmapProgress(16 + pro)) return false;

		if (m_bIsFull)
		{
			//链接对象
			if (!LoadLinkedObjs(pmap, picMap, lkoMap, names))
				return false;

			//飘动效果
			if (!LoadDistortObjs(pmap, picMap, disMap, names))
				return false;
		}

		//传送对象们
		m_TransObjects = pmap.m_TransObjects.GetTransInfos();

		//昼夜特效
		m_TimeEffects = pmap.GetTimeEffectParams();

		//Mask叠图
		m_maskMap = pmap.GetMaskParams();

		if (m_bIsFull)
		{
			//移动背景
			if (!LoadBackGround(pmap, picMap))
				return false;
		}

		if (!m_pWorld->NotifyLoadmapProgress(18 + pro)) return false;

		//迷宫
		if (false == m_maze.Load(mazebuffer))
			m_maze.reset(this);

		//声音描述
		if (soundXMLbuffer.size() == 0)
			return true;

		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)soundXMLbuffer.constbegin(), soundXMLbuffer.size(), fileData, codeType))
		{
			XPLOG_ERROR(L"解析声音描述文件错误\n");
			return true;
		}

		LJXML::LJXML_Doc<LJXML::Char> doc;
		LJXML::Char* charData = doc.LoadFromString(fileData);
		LJXML::LJXML_Node<>* root = doc.GetRootNode();
		if (!root)
		{
			XPLOG_ERROR(L"获得声音描述文件根错误\n");
			return true;
		}

		NuclearStepSoundMap *pSSM = new NuclearStepSoundMap();
		if (!pSSM)
		{
			XPLOG_ERROR(L"申请脚步声音空间错误，内存不够\n");
			return false;
		}

		if( !pSSM->LoadFromMem(stepsound) )
		{
			delete pSSM;
			pSSM = NULL;
		}

		LoadSoundAreas(*root, pSSM);

		if (!m_pWorld->NotifyLoadmapProgress(20 + pro)) return false;
		return true;
	}

	bool Map::Load(const std::wstring & mapname, const std::wstring & mazename, const SubMap::PPathMap* pathMap)
	{
		NuclearBuffer pmap;
		NuclearBuffer maze;
		NuclearBuffer xmlData;
		NuclearBuffer ssm;
		NuclearFileIOManager *pFIO = m_pEB->GetFileIOManager();
		if (!pFIO->GetFileImage(GetMapResUrl(mapname), pmap))
			return false;
		
		//迷宫
		pFIO->GetFileImage(mazename, maze);

		//声音描述
		if (LJFM::LJFMOpen::IsFileExisting(GetSoundResUrl(mapname)))
		{
			pFIO->GetFileImage(GetSoundResUrl(mapname), xmlData);
		}
		else
		{
			XPLOG_ERROR(L"找不到声音描述文件 %s \n", mapname.c_str());
		}

		if (LJFM::LJFMOpen::IsFileExisting(GetStepSoundResUrl(mapname)))
		{
			pFIO->GetFileImage(GetStepSoundResUrl(mapname), ssm);
		}

		return Load(pmap, maze, xmlData, ssm, pathMap, 0);
	}

	bool Map::LoadSoundAreas(LJXML::LJXML_Node<> &root, NuclearStepSoundMap* pSSM)
	{
		m_NuclearStepSoundMap = pSSM;
		LJXML::LJXML_NodeList nl;
		root.GetChildren(nl);
		LJXML::LJXML_NodeList::iterator it = nl.begin(), ie = nl.end();
		for (; it != ie; ++it)
		{
			if ((*it)->GetType() != rapidxml::node_element)
				continue;
			if ((*it)->GetName() == L"env")
			{//环境音
				LJXML::LJXML_NodeList envnl;
				XPEnvSoundArea *pEsa;
				(*it)->GetChildren(envnl);
				LJXML::LJXML_NodeList::iterator eIt = envnl.begin(), eIe = envnl.end();
				for (; eIt != eIe; ++eIt)
				{
					if ((*eIt)->GetType() != rapidxml::node_element)
						continue;
					if ((*eIt)->GetName() == L"circle")
						pEsa = new XPEnvSoundCircleArea(m_pEB);
					else if ((*eIt)->GetName() == L"polygon")
						pEsa = new XPEnvSoundPolygonArea(m_pEB);
					else
						XPASSERT(false);
					pEsa->Load(**eIt, XPSOUND_IO_NORMAL);
					const XPEnvSoundArea::RegionSet &rset = pEsa->GetRegionSet();
					XPEnvSoundArea::RegionSet::const_iterator rIt = rset.begin(), rIe = rset.end();
					for (; rIt != rIe; ++rIt)
					{
						m_regions[*rIt].m_EnvSoundAreas.push_back(pEsa);
					}
					m_SoundAreas.push_back(pEsa);
				}
			}
			else if ((*it)->GetName() == L"bgs")
			{//背景音乐
				//LJXML::LJXML_NodeList bkgnl;
				//XPBkgSoundArea *pBsa;
				//(*it)->GetChildren(bkgnl);
				//LJXML::LJXML_NodeList::iterator bIt = bkgnl.begin(), bIe = bkgnl.end();
				//for (; bIt != bIe; ++bIt)
				//{
				//	if (bIt->GetType() != rapidxml::node_element)
				//		continue;
				//	if (bIt->GetName() == L"circle")
				//		pBsa = new XPBkgSoundCircleArea(m_pEB);
				//	else if (bIt->GetName() == L"polygon")
				//		pBsa = new XPBkgSoundPolygonArea(m_pEB);
				//	else if (bIt->GetName() == L"default")
				//	{
				//		m_pBackGroundDefaultArea = new XPBkgDefaultSoundArea(m_pEB);
				//		m_pBackGroundDefaultArea->Load(*bIt, XPSOUND_IO_NORMAL);
				//		continue;
				//	}
				//	else
				//		XPASSERT(false);
				//	pBsa->Load(*bIt, XPSOUND_IO_NORMAL);
				//	const XPBkgSoundArea::RegionSet &rset = pBsa->GetRegionSet();
				//	XPEnvSoundArea::RegionSet::const_iterator rIt = rset.begin(), rIe = rset.end();
				//	for (; rIt != rIe; ++rIt)
				//	{
				//		m_regions[*rIt].m_BkgSoundAreas.push_back(pBsa);
				//	}
				//	m_SoundAreas.push_back(pBsa);
				//}
			}
			else if ((*it)->GetName() == L"bgv")
			{//背景音乐音量
				//LJXML::LJXML_NodeList bkgnl;
				//XPBkgVolArea *pBsv;
				//(*it)->GetChildren(bkgnl);
				//LJXML::LJXML_NodeList::iterator bIt = bkgnl.begin(), bIe = bkgnl.end();
				//for (; bIt != bIe; ++bIt)
				//{
				//	if (bIt->GetType() != rapidxml::node_element)
				//		continue;
				//	if (bIt->GetName() == L"circle")
				//		pBsv = new XPBkgVolCircleArea();
				//	else if (bIt->GetName() == L"polygon")
				//		pBsv = new XPBkgVolPolygonArea();
				//	else
				//		XPASSERT(false);
				//	pBsv->Load(*bIt, XPSOUND_IO_NORMAL);
				//	const XPBkgSoundArea::RegionSet &rset = pBsv->GetRegionSet();
				//	XPEnvSoundArea::RegionSet::const_iterator rIt = rset.begin(), rIe = rset.end();
				//	for (; rIt != rIe; ++rIt)
				//	{
				//		m_regions[*rIt].m_BkgVolumeAreas.push_back(pBsv);
				//	}
				//	m_SoundAreas.push_back(pBsv);
				//}
			}
			else {
				XPASSERT(false);
			}
		}
		return true;
	}

	void Map::AssureRegionResource(NuclearRect viewport, bool async, bool callback, bool isManualPrefetch, int pro)
	{
		if (m_loading && !isManualPrefetch) //loading会是true么？单线程的呀。。。。
			return;

		int64_t tick = GetMilliSeconds();
		m_loading = true;
		async = async && m_pEB->IsAsyncRead();
		bool asynccallback = callback && async;
		bool synccallback = callback && (!async);

		int extraregion = 512;
		int minregioncol = viewport.left / REGION_WIDTH - extraregion / REGION_WIDTH;
		if (minregioncol < 0) minregioncol = 0;

		int maxregioncol = (viewport.right - 1) / REGION_WIDTH + extraregion / REGION_WIDTH;
		if (maxregioncol >= m_regioncol) maxregioncol = m_regioncol - 1;

		int minregionrow = viewport.top / REGION_HEIGHT - extraregion / REGION_HEIGHT;
		if (minregionrow < 0) minregionrow = 0;
		int maxregionrow = (viewport.bottom - 1) / REGION_HEIGHT + extraregion / REGION_HEIGHT;
		if (maxregionrow >= m_regionrow) maxregionrow = m_regionrow - 1;

		std::set<PicResrc*> newActivePicResrcs;
		std::set<PicResrc*> newSyncActivePicResrcs;
		std::vector<PicResrc*> syncAddset, addset, delset;

		std::set<Effect*> newActiveEffects;
		std::vector<Effect*> effectAddset, effectDelset;

#if defined(WIN32) && defined(DEBUG)
		m_nLoadingRegion = 0;
		m_LoadingRegionRect.left = minregioncol * REGION_WIDTH;
		m_LoadingRegionRect.top = minregionrow * REGION_HEIGHT;
		m_LoadingRegionRect.right = (maxregioncol + 1)* REGION_WIDTH;
		m_LoadingRegionRect.bottom = (maxregionrow + 1)* REGION_HEIGHT;

		if (m_LoadingRegionRect.right < m_LoadingRegionRect.left) m_LoadingRegionRect.right = m_LoadingRegionRect.left;
		if (m_LoadingRegionRect.bottom < m_LoadingRegionRect.top) m_LoadingRegionRect.bottom = m_LoadingRegionRect.top;
#endif
		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_REGION);

		//移动背景
		BackGoundInfoVet::iterator bgit = m_BackGroundInfos.begin(), bgie = m_BackGroundInfos.end();
		NuclearRect bigVp = viewport.Widen(REGION_WIDTH);
		for (; bgit != bgie; ++bgit)
		{
			NuclearRect res;
			if (bigVp.IsCross(bgit->activeRect, res))
			{
				NuclearPoint bgVet = viewport.Center() - bgit->pos;
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
					renderwidth = m_width;
				}
				else {
					ltpos.x = pos.x;
					renderwidth = bgwidth;
				}
				if (bgit->wrapVert)
				{
					ltpos.y = 0;
					renderheight = m_height;
				}
				else {
					ltpos.y = pos.y;
					renderheight = bgheight;
				}
				NuclearRect rect(ltpos, renderwidth, renderheight);
				NuclearRect objRect;
				if (rect.IsCross(res, objRect))
				{
					//objRect是需要画的区域，世界坐标
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
					if (objRect.right - objRect.left >= bgwidth)
					{
						bx = 0;
						w = bgit->width - 1;
					}
					else {
						bx = objRect.left / bgit->girdWidth;
						w = (objRect.right + bgit->girdWidth - 1) / bgit->girdWidth;
					}
					if (objRect.bottom - objRect.top >= bgheight)
					{
						y = 0;
						h = bgit->height - 1;
					}
					else {
						y = objRect.top / bgit->girdHeight;
						h = (objRect.bottom + bgit->girdHeight - 1) / bgit->girdHeight + 1;
					}
					for (ty = y; y <= h; ++y, ++ty)
					{
						if (ty >= bgit->height)
						{
							ty = 0;
						}
						PicResrc **ppPicResrcs = bgit->ppPicResrcs + ty * bgit->width + bx;
						for (x = tx = bx; x <= w; ++x, ++ppPicResrcs, ++tx)
						{
							if (tx >= bgit->width)
							{
								tx = 0;
								ppPicResrcs = bgit->ppPicResrcs + ty * bgit->width;
							}
							if (*ppPicResrcs)
							{
								newActivePicResrcs.insert(*ppPicResrcs);
							}
						}
					}
				}
			}
		}
		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_BACKGROUND);

		int i;
		for (int col = minregioncol; col <= maxregioncol; ++col)
		{
			for (int row = minregionrow; row <= maxregionrow; ++row)
			{
#if defined(WIN32) && defined(DEBUG)
				++m_nLoadingRegion;
#endif
				RegionInfo &regionInfo = m_regions[col + row*m_regioncol];
				for (i = 0; i < XPPIC_CATCH_LAYER_COUNT; ++i)//地表+水1+水2层（需要rendertarget cache的）
				{
					std::vector<Region::PicEle> &v = regionInfo.m_vPicEle[i];
					for (std::vector<Region::PicEle>::const_iterator it = v.begin(), ie = v.end(); it != ie; ++it)
						newSyncActivePicResrcs.insert(it->pPicResrc);
				}

				for (i = XPPIC_CATCH_LAYER_COUNT; i < XPPIC_LAYER_COUNT; ++i)//从低1层开始
				{
					switch (i)
					{
					case XPPIC_MID1_ELEMENT_LAYER_ID:
					case XPPIC_MID2_ELEMENT_LAYER_ID:
					{
						std::vector<Region::PicEleWithBase> &v = regionInfo.m_vPicEleWithBase[i - XPPIC_MID1_ELEMENT_LAYER_ID];
						for (std::vector<Region::PicEleWithBase>::const_iterator it = v.begin(), ie = v.end(); it != ie; ++it)
							newActivePicResrcs.insert(it->pPicResrc);
					}
						break;
					default:
					{
						std::vector<Region::PicEle> &v = regionInfo.m_vPicEle[i];
						for (std::vector<Region::PicEle>::const_iterator it = v.begin(), ie = v.end(); it != ie; ++it)
							newActivePicResrcs.insert(it->pPicResrc);
					}
						break;
					}
					XPASSERT(i >= XPPIC_FIRST_ELEMENT_ID);
					std::vector<const Region::LkoEle*> &v = regionInfo.m_vLkoEle[i - XPPIC_FIRST_ELEMENT_ID];
					for (std::vector<const Region::LkoEle*>::const_iterator it = v.begin(), ie = v.end(); it != ie; ++it)
						newActivePicResrcs.insert((*it)->pPicResrc);

					std::vector<Region::EffectInfo> &effInfo = regionInfo.m_effects[i - XPPIC_FIRST_ELEMENT_ID];
					for (std::vector<Region::EffectInfo>::const_iterator efit = effInfo.begin(), efie = effInfo.end(); efit != efie; ++efit)
						newActiveEffects.insert(efit->pEffect);
				}
				newActivePicResrcs.insert(regionInfo.m_vpDistortsPic.begin(), regionInfo.m_vpDistortsPic.end());
			}
		}
		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_MAPOBJS);
		//XPTRACE(L"new size is %d, old size is %d\n", newActivePicResrcs.size(), m_activePicResrcs.size());
		//
		//是否“地表+水1+水2用同步，剩下的用异步。”如果全部同步这个也是false，手动预读也异步
		bool picCache = async && (m_pEB->GetConfigManager()->IsUseSurfaceCache() || m_pEB->GetConfigManager()->IsSyncBeforeWater()) && (!isManualPrefetch);
		if (!picCache)
		{
			newActivePicResrcs.insert(newSyncActivePicResrcs.begin(), newSyncActivePicResrcs.end());
			newSyncActivePicResrcs.clear();
		}

		//需要Load的资源
		std::set_difference(newActivePicResrcs.begin(), newActivePicResrcs.end(),
			m_pWorld->m_activePicResrcs.begin(), m_pWorld->m_activePicResrcs.end(), std::back_inserter(addset));

		//if (m_pWorld->isShowSceneEffects())
		{
			std::set_difference(newActiveEffects.begin(), newActiveEffects.end(),
				m_pWorld->m_activeEffects.begin(), m_pWorld->m_activeEffects.end(), std::back_inserter(effectAddset));
		}

		//addset里面包括部分需要同步加载的资源
		if (picCache)
		{
			newActivePicResrcs.insert(newSyncActivePicResrcs.begin(), newSyncActivePicResrcs.end());
		}

		if (!isManualPrefetch)
		{
			newActivePicResrcs.insert(m_pWorld->m_manualPrefetchPicResrcs.begin(), m_pWorld->m_manualPrefetchPicResrcs.end());
			newActiveEffects.insert(m_pWorld->m_manualPrefetchEffects.begin(), m_pWorld->m_manualPrefetchEffects.end());
			//需要释放的资源
			std::set_difference(m_pWorld->m_activePicResrcs.begin(), m_pWorld->m_activePicResrcs.end(),
				newActivePicResrcs.begin(), newActivePicResrcs.end(), std::back_inserter(delset));
			std::set_difference(m_pWorld->m_activeEffects.begin(), m_pWorld->m_activeEffects.end(),
				newActiveEffects.begin(), newActiveEffects.end(), std::back_inserter(effectDelset));
		}

		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_DIFF);
		
		if( synccallback ) m_pWorld->NotifyLoadmapProgress(pro);
		int nadd = static_cast<int>(addset.size() + effectAddset.size() + newSyncActivePicResrcs.size());
		int iadd = 1;
		int rtime = 0;
		float d = static_cast<float>(nadd)/64;
		std::set<PicResrc*>::iterator syncPicEnd = newSyncActivePicResrcs.end();

		// 需要删除(img)
		for(std::vector<PicResrc*>::iterator it = delset.begin(), ie = delset.end();
			it != ie; ++it)
		{
			PicResrc *pPicResrc = *it;
			if( pPicResrc->handle != INVALID_PICTURE_HANDLE )
				m_pEB->GetRenderer()->FreePicture(pPicResrc->handle);
			pPicResrc->handle = INVALID_PICTURE_HANDLE;
		}
		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_DEL_ELE);
		//effect
		for(std::vector<Effect*>::iterator it = effectDelset.begin(), ie = effectDelset.end();
			it != ie; ++it)
		{
			(*it)->Stop();
			(*it)->Release();
		}
		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_DEL_EFCT);

		// 需要新增(img)
		for (std::set<PicResrc*>::iterator it = newSyncActivePicResrcs.begin();it!=syncPicEnd;++it)
		{
			PicResrc *pPicResrc = *it;
			if (pPicResrc->handle != INVALID_PICTURE_HANDLE)
			{
				++iadd;
				continue;
			}
			NuclearBuffer data;
			if( m_pEB->GetFileIOManager()->GetFileImage(pPicResrc->uri, data))
			{
				if( data.size())
				{
					NuclearPictureInfo picinfo;
					picinfo.fileuri = pPicResrc->uri;
					PictureHandle handle = m_pEB->GetRenderer()->LoadPictureFromMem(data.constbegin(), data.size(), &picinfo, XPTEXFMT_DEFAULT, 
						m_pEB->GetConfigManager()->GetCreateTexturePoolType(), m_pEB->GetRenderer()->CacheInMemory());
                    
					if( handle != INVALID_PICTURE_HANDLE )
					{
                        if (m_pEB->GetRenderer()->CacheInMemory() == false)
                        m_pEB->GetRenderer()->CachePicture(handle, pPicResrc->uri);
						pPicResrc->handle = handle;
						pPicResrc->pooltype = picinfo.m_pooltype;
					} else {
						XPLOG_ERROR(L"create tex error: %s\n", pPicResrc->uri.c_str());
					}
				} else {
					XPLOG_ERROR(L"load file size is 0: %s\n", pPicResrc->uri.c_str());
				}
			} else {
				XPLOG_ERROR(L"load file error: %s\n", pPicResrc->uri.c_str());
			}

			if( synccallback && d > 0.001 && ( iadd / d ) > (rtime + 1) )
			{
				rtime = static_cast<int>(iadd/d);
				m_pWorld->NotifyLoadmapProgress(pro + rtime);
			}
			++iadd;
		}
		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_SYNCLOAD);

		for(std::vector<PicResrc*>::iterator it = addset.begin(), ie = addset.end(); it != ie; ++it)
		{
			PicResrc *pPicResrc = *it;
			if (pPicResrc->handle != INVALID_PICTURE_HANDLE)
				continue;
			if( async )
			{
				if (picCache)
				{
					if (newSyncActivePicResrcs.find(pPicResrc) != syncPicEnd) //应该同步读，不异步了，
					{
						++iadd;
						continue;
					}
				}
				MapPicElementReadTask *pTask = new MapPicElementReadTask(pPicResrc->uri, m_pEB->GetFileIOManager(), m_pWorld, this, pPicResrc, asynccallback, pro, 96, m_bLookupSegmpak);
				if (!pTask->Submit())
				{
					delete pTask;
				}
				if (asynccallback)
				{
					m_pWorld->AddTotalLoadingCount();
				}
			}
			else
			{
				NuclearBuffer data;
				if( m_pEB->GetFileIOManager()->GetFileImage(pPicResrc->uri, data, m_bLookupSegmpak))
				{
					if( data.size())
					{
						NuclearPictureInfo picinfo;
						picinfo.fileuri = pPicResrc->uri;
						PictureHandle handle = m_pEB->GetRenderer()->LoadPictureFromMem(data.constbegin(), data.size(), &picinfo, XPTEXFMT_DEFAULT, 
							 m_pEB->GetConfigManager()->GetCreateTexturePoolType(), m_pEB->GetRenderer()->CacheInMemory());
						if( handle != INVALID_PICTURE_HANDLE )
						{
                            if (m_pEB->GetRenderer()->CacheInMemory() == false)
                            m_pEB->GetRenderer()->CachePicture(handle, pPicResrc->uri);
							pPicResrc->handle = handle;
							pPicResrc->pooltype = picinfo.m_pooltype;
						} else {
							XPLOG_ERROR(L"create tex error: %s\n", pPicResrc->uri.c_str());
						}
					} else {
						XPLOG_ERROR(L"load file size is 0: %s\n", pPicResrc->uri.c_str());
					}
				} else {
					XPLOG_ERROR(L"load file error: %s\n", pPicResrc->uri.c_str());
				}
			}
			if( synccallback && d > 0.001 && ( iadd / d ) > (rtime + 1) )
			{
				rtime = static_cast<int>(iadd/d);
				m_pWorld->NotifyLoadmapProgress(pro + rtime);
			}
			++iadd;
		}


		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_ELE_ASYNCLOAD);
		for(std::vector<Effect*>::iterator it = effectAddset.begin(), ie = effectAddset.end(); it != ie; ++it)
		{

			(*it)->AssureResource(async);

			if( synccallback && d > 0.001 && ( iadd / d ) > (rtime + 1) )
			{
				rtime = static_cast<int>(iadd/d);
				m_pWorld->NotifyLoadmapProgress(pro + rtime);
			}
			++iadd;
		}
		g_timeuserlog.Push(XPTimeUseLog::TUL_ASURE_EFFECT_ASYNCLOAD);

		if( synccallback ) m_pWorld->NotifyLoadmapProgress(96);

		if (isManualPrefetch)
		{
			m_pWorld->m_activePicResrcs.insert(newActivePicResrcs.begin(), newActivePicResrcs.end());
			m_pWorld->m_activeEffects.insert(newActiveEffects.begin(), newActiveEffects.end());
			m_pWorld->m_manualPrefetchPicResrcs.swap(newActivePicResrcs);
			m_pWorld->m_manualPrefetchEffects.swap(newActiveEffects);
		} else {
			m_pWorld->m_activePicResrcs.swap(newActivePicResrcs);
			m_pWorld->m_activeEffects.swap(newActiveEffects);
		}
		
		m_loading = false;
	}

}