#include "stdafx.h"
#include "..\common\mapconst.h"
#include "..\map\ppathmap.h"
#include "..\effect\effect.h"
#include "..\effect\effectmanager.h"
#include "..\renderer\renderer.h"
#include "..\engine\enginebase.h"
#include "world.h"
#include "regionmap.h"
#include "distort.h"
#include "lkotree.h"

namespace Nuclear
{

	RegionMap::RegionMap(EngineBase* pEB, World* pWorld, PicResrcMap* pPicResrcMap) : m_pEB(pEB), m_pWorld(pWorld), m_pAllPicResrcs(pPicResrcMap)
	{
	}

	RegionMap::~RegionMap(void)
	{
		std::vector<RegionInfo>::iterator iter = m_regions.begin();
		std::vector<LkoTree*>::iterator dtreeIter;
		std::vector<Region::TriggerObjInfo*>::iterator dtriggerIter;
		std::vector<Distort*>::iterator distIter;
		std::vector<Region::EffectInfo>::iterator effIter;
		std::set<LkoTree*> hasDelete;
		std::set<Region::TriggerObjInfo*> hasDelTrigger;
		std::set<Distort*> hasDelDistort;
		BackGoundInfoVet::iterator bgIt = m_backGroundInfos.begin(), bgIe = m_backGroundInfos.end();
		for (;bgIt!=bgIe;++bgIt)
		{
			XFree(bgIt->ppPicResrcs);
		}
		for(;iter!=m_regions.end();++iter)
		{
			iter->m_BkgSoundAreas.clear();
			iter->m_EnvSoundAreas.clear();
			for (int i=0;i<MAP_LAYER_COUNT-1;++i)
			{
				dtreeIter=iter->m_vpLkoTrees[i].begin();
				for (;dtreeIter!=iter->m_vpLkoTrees[i].end();++dtreeIter)
				{
					if (hasDelete.find(*dtreeIter) == hasDelete.end())
					{
						hasDelete.insert(*dtreeIter);
						delete(*dtreeIter);
					}

				}
				distIter=iter->m_vpDistorts[i].begin();
				for(;distIter!=iter->m_vpDistorts[i].end();++distIter)
				{
					if (hasDelDistort.find(*distIter) == hasDelDistort.end())
					{
						hasDelDistort.insert(*distIter);
						delete (*distIter);
					}
				}
				effIter=iter->m_effects[i].begin();
				for(;effIter!=iter->m_effects[i].end();++effIter)
				{
					if( effIter->pEffect )
						delete effIter->pEffect;
				}
			}
			dtriggerIter=iter->m_vpTriggerObjInfo.begin();
			for(;dtriggerIter!=iter->m_vpTriggerObjInfo.end();++dtriggerIter)
			{
				if (hasDelTrigger.find(*dtriggerIter) == hasDelTrigger.end())
				{
					hasDelTrigger.insert(*dtriggerIter);
					XFree (*dtriggerIter);
				}
			}
		}

		
		for(PicResrcVet::iterator it= m_picResrcs.begin(), ie= m_picResrcs.end(); it != ie; ++it)
		{
			--((*it)->mapRef);
		}

		for(PicResrcMap::iterator it= m_pAllPicResrcs->begin(), ie= m_pAllPicResrcs->end(); it != ie;)
		{
			if (it->second->mapRef == 0)
			{
				if (m_pWorld)
				{
					m_pWorld->m_activePicResrcs.erase(it->second);
				}
				if( it->second->handle != INVALID_PICTURE_HANDLE )
					m_pEB->GetRenderer()->FreePicture(it->second->handle);
				delete it->second;
				m_pAllPicResrcs->erase(it++);
			} else {
				++it;
			}
		}
	}

	bool RegionMap::LoadSmlGround(const PMap& pmap, const SubMap::PPathMap* pathMap, int layerID)
	{
		if (m_regions.size() == 0)
			return false;
		// 小地表层
		CRECT sizeRect(0, 0, PMap::TILE_WIDTH, PMap::TILE_HEIGHT);
		std::map<SubMap::TileType, PicResrc*> tmpTileMap;
		unsigned int *pColorData = pmap.GetColorData();
		unsigned short colorpos1;
		unsigned short colorpos2;
		bool useColorPos = false;
		
		int colorWidth = pmap.GetWidthForColor();

		int nRows = pmap.GetHeightForSmlTiles();
		int nCols = pmap.GetWidthForSmlTiles();

		for(int l=0; l < TILES_SMALL_LAYER_COUNT; ++l)
		{
			SubMap::TileType *tiles = pmap.GetTileData(l);

			for(int row = 0; row < nRows; ++row)
			{
				colorpos1 = row * colorWidth;
				colorpos2 = colorpos1 + colorWidth;
				for(int col = 0; col < nCols; ++col, ++colorpos1, ++colorpos2)
				{
					useColorPos = ( pColorData[colorpos1] != pColorData[colorpos2] || pColorData[colorpos1] != pColorData[colorpos1 + 1]
					|| pColorData[colorpos2] != pColorData[colorpos2 + 1]);//四个角颜色都一样
					SubMap::TileType tile = tiles[col+row*nCols];
					if (SubMap::PGround::GetGroundType(tile) == 0) continue;
					PicResrc *pPicResrc = NULL;
					std::map<SubMap::TileType, PicResrc*>::iterator itt = tmpTileMap.find(tile);
					if( itt != tmpTileMap.end() )
					{
						pPicResrc = itt->second;
					}
					else
					{
						std::wstring tilepath = pathMap->GetTileFileName(tile);
						if( tilepath.size() == 0 )
						{
							XPTRACE(L"load tilefile failed, tile = %d\n", tile);
							XPLOG_ERROR(L"load tilefile failed, tile = %d\n", tile);
						}
						else
						{
							PicResrcMap::iterator picIt = m_pAllPicResrcs->find(tilepath);
							if (picIt == m_pAllPicResrcs->end())
							{
								pPicResrc = new PicResrc(tilepath);
								(*m_pAllPicResrcs)[tilepath] = pPicResrc;
							} else {
								pPicResrc = picIt->second;
							}
							++(pPicResrc->mapRef);
							m_picResrcs.push_back(pPicResrc);
							tmpTileMap[tile] = pPicResrc;	
							AddAreaSize(pPicResrc, sizeRect);
						}
					}

					if( NULL != pPicResrc )
					{
						CRECT rect;
						rect.left = col * PMap::TILE_WIDTH;
						rect.right = rect.left + PMap::TILE_WIDTH;
						rect.top = row * PMap::TILE_HEIGHT;
						rect.bottom = rect.top + PMap::TILE_HEIGHT;
						CPOINT center = rect.Center();

						int rcol = center.x/ REGION_WIDTH;
						int rrow = center.y/ REGION_HEIGHT;
						if( rcol < 0 ) rcol = 0; if( rcol >= m_regioncol ) rcol = m_regioncol-1;
						if( rrow < 0 ) rrow = 0; if( rrow >= m_regionrow ) rrow = m_regionrow-1;
						RegionInfo &reg = m_regions[rcol + rrow * m_regioncol];

						Region::PicEle picele;
						picele.pos = rect;
						picele.pPicResrc = pPicResrc;
						if (useColorPos)
						{
							picele.useColorPos = useColorPos;//picele.useColorPos默认false
							picele.colorpos1 = colorpos1;
							picele.colorpos2 = colorpos2;
						} else {
							picele.color = pColorData[colorpos1];//四个角都一样
						}
						reg.m_vPicEle[l+layerID].push_back(picele);
					}
				}
			}
		}
		return true;
	}

	bool RegionMap::LoadBiggerGround(SubMap::GroundLayers layer, const PMap& pmap, const SubMap::PPathMap* pathMap, int layerID, std::map<unsigned short, PicResrc*> *tileMap)
	{
		if (m_regions.size() == 0)
			return false;
		//大中地表层
		int tileWidth, tileHeight;
		SubMap::PtrBiggerTileType pTile;
		TILE_SIZE_TYPE tileSize;
		switch (layer)
		{
		case SubMap::GL_SMALL:
			return false;
			break;
		case SubMap::GL_MID1:
			tileSize = TST_MIDDLE;
			tileWidth = SubMap::PGround::TILE_MID_WIDTH;
			tileHeight = SubMap::PGround::TILE_MID_HEIGHT;
			pTile = pmap.GetMidTileData(0);
			break;
		case SubMap::GL_MID2:
			tileSize = TST_MIDDLE;
			tileWidth = SubMap::PGround::TILE_MID_WIDTH;
			tileHeight = SubMap::PGround::TILE_MID_HEIGHT;
			pTile = pmap.GetMidTileData(1);
			break;
		case SubMap::GL_BIG:
			tileSize = TST_BIG;
			tileWidth = SubMap::PGround::TILE_BIG_WIDTH;
			tileHeight = SubMap::PGround::TILE_BIG_HEIGHT;
			pTile = pmap.GetBigTileData();
			break;
		}
		CRECT sizeRect(0, 0, tileWidth, tileHeight);
		int nRows = (m_height + tileHeight - 1) / tileHeight;
		int nCols = (m_width + tileWidth - 1) / tileWidth;
		for(int row = 0; row < nRows; ++row)
		{
			for(int col = 0; col < nCols; ++col)
			{
				SubMap::BiggerTileType tile = pTile[col+row*nCols];
				if( tile.tileType == 0 )
					continue;
				PicResrc *pPicResrc = NULL;
				std::map<unsigned short, PicResrc*>::iterator itt = tileMap->find(tile.data);
				if( itt != tileMap->end() )
				{
					pPicResrc = itt->second;
				}
				else
				{
					std::wstring tilepath = pathMap->GetTileFileName(tileSize, tile);
					if( tilepath.size() == 0 )
					{
						XPTRACE(L"load tilefile failed, tile = %d\n", tile);
						XPLOG_ERROR(L"load tilefile failed, tile = %d\n", tile);
					}
					else
					{
						PicResrcMap::iterator picIt = m_pAllPicResrcs->find(tilepath);
						if (picIt == m_pAllPicResrcs->end())
						{
							pPicResrc = new PicResrc(tilepath);
							(*m_pAllPicResrcs)[tilepath] = pPicResrc;
						} else {
							pPicResrc = picIt->second;
						}
						m_picResrcs.push_back(pPicResrc);
						++(pPicResrc->mapRef);
						(*tileMap)[tile.data] = pPicResrc;	
						AddAreaSize(pPicResrc, sizeRect);
					}
				}
				if( NULL != pPicResrc )
				{
					CRECT rect;
					rect.left = col * tileWidth;
					rect.right = rect.left + tileWidth;
					rect.top = row * tileHeight;
					rect.bottom = rect.top + tileHeight;
					AddCacheLagerPic(rect, pPicResrc, 0xFFFFFFFF, layerID);
				}
			}
		}
		return true;
	}

	void RegionMap::AddCacheLagerPic(const CRECT &rect, PicResrc *pPicResrc, unsigned int color, int layerID)
	{
		CPOINT center = rect.Center();

		int rcol = center.x / REGION_WIDTH;
		int rrow = center.y / REGION_HEIGHT;
		if( rcol < 0 ) rcol = 0; if( rcol >= m_regioncol ) rcol = m_regioncol-1;
		if( rrow < 0 ) rrow = 0; if( rrow >= m_regionrow ) rrow = m_regionrow-1;
		RegionInfo &reg = m_regions[rcol + rrow * m_regioncol];

		Region::PicEle picele;
		picele.pos = rect;
		picele.pPicResrc = pPicResrc;
		picele.color = color;
		reg.m_vPicEle[layerID].push_back(picele);

		static const CPOINT ADJ_RECT_ARRAY[8] = {
			CPOINT(-REGION_WIDTH, -REGION_HEIGHT),		//从中间到左上
			CPOINT(REGION_WIDTH, 0),					//从左上到中上
			CPOINT(REGION_WIDTH, 0),					//从中上到右上
			CPOINT(-REGION_WIDTH * 2, REGION_HEIGHT),	//从右上到左中
			CPOINT(REGION_WIDTH * 2, 0),				//从左中到右中
			CPOINT(-REGION_WIDTH * 2, REGION_HEIGHT),	//从右中到左下
			CPOINT(REGION_WIDTH, 0),					//从左下到中下
			CPOINT(REGION_WIDTH, 0),					//从中下到右下
		};

		static const CPOINT ADJ_REGION_ARRAY[8] = {
			CPOINT(-1, -1),		//从中间到左上
			CPOINT(1, 0),		//从左上到中上
			CPOINT(1, 0),		//从中上到右上
			CPOINT(-1 * 2, 1),	//从右上到左中
			CPOINT(1 * 2, 0),	//从左中到右中
			CPOINT(-1 * 2, 1),	//从右中到左下
			CPOINT(1, 0),		//从左下到中下
			CPOINT(1, 0),		//从中下到右下
		};

		CRECT regionRect(CPOINT(rcol * REGION_WIDTH, rrow * REGION_HEIGHT), REGION_WIDTH, REGION_HEIGHT);
		picele.forCache = true;
		for (int i=0;i<8;++i)
		{
			regionRect += ADJ_RECT_ARRAY[i];
			//在地图范围之内
			rcol += ADJ_REGION_ARRAY[i].x;
			rrow += ADJ_REGION_ARRAY[i].y;
			if( rcol < 0 || rcol >= m_regioncol || rrow < 0 || rrow >= m_regionrow)	//出了地图了
				continue;
			RegionInfo &reg = m_regions[rcol + rrow * m_regioncol];
			reg.m_vPicEle[layerID].push_back(picele);
		}

	}

	bool RegionMap::LoadBaseInfo(const PMap& pmap)
	{
		m_width = pmap.GetWidth();
		m_height = pmap.GetHeight();
		//
		m_regioncol = m_width / REGION_WIDTH + 1;
		m_regionrow = m_height / REGION_HEIGHT + 1;

		m_widthForWater = pmap.GetWidthForWater();
		m_heightForWater = pmap.GetHeightForWater();

		m_regions.resize(m_regioncol * m_regionrow);
		return true;
	}

	bool RegionMap::LoadGround(const PMap& pmap, const SubMap::PPathMap* pathMap)
	{
		SubMap::GroundLayers layer;
		int layerID = 0;
		bool result = true;
		std::map<unsigned short, PicResrc*> midTileMap;
		std::map<unsigned short, PicResrc*> bigTileMap;
		for (int i=TILES_ALL_LAYER_COUNT-1;i>=0;--i)
		{
			if (pmap.GetGroundOrders(i, layer))
			{
				if (layer == SubMap::GL_SMALL)
				{
					result = LoadSmlGround(pmap, pathMap, layerID) && result;
					layerID += TILES_SMALL_LAYER_COUNT;
				} else {
					result = LoadBiggerGround(layer, pmap, pathMap, layerID++, 
						(layer==SubMap::GL_BIG)?(&bigTileMap):(&midTileMap)) && result;
				}
			}
		}
		return result;
	}

	bool RegionMap::LoadElementPicMap(const PMap& pmap, LoadingPicResrcMap& picMap, LoadingLinkedObjMap& lkoMap, LoadingDistortObjMap& disMap, ElementNames& names)
	{
		// 物件层图片
		const SubMap::ObjNameMap &ele2path = pmap.m_objs[PMap::MAP_OBJ_ELEMENT].GetConstObjFileNameMap();
		SubMap::ObjMapKey keySeed = 0;
		SubMap::ObjMapKey tmpKeySeed;

		for(SubMap::ObjNameMap::const_iterator it = ele2path.begin(), ie = ele2path.end(); it != ie; ++it)
		{
			std::wstring imgpath = it->second + ImageSet_ExtName;
			tmpKeySeed = it->first - 1;
			if (it->first > keySeed)
				keySeed = it->first;
			LoadImg(imgpath, picMap, names, tmpKeySeed);
		}

		//LinkObjs.............

		const SubMap::ObjNameMap &dyn2path = pmap.m_objs[PMap::MAP_OBJ_LINKEDOBJ].GetConstObjFileNameMap();

		PLinkedObject::VectorLinkedObjectInfo::iterator nodeIter;
		//int n=sizeof(CRECT);
		for(SubMap::ObjNameMap::const_iterator it = dyn2path.begin(), ie = dyn2path.end(); it != ie; ++it)
		{
			XBuffer objfiledata;
			if( !m_pEB->GetFileIOManager()->GetFileImage(it->second, objfiledata))
			{
				XPTRACE(L"load linked obj file failed, file is %s\n", it->second.c_str());
				XPLOG_ERROR(L"load linked obj file failed, file is %s\n", it->second.c_str());
				continue;
			}
			PLinkedObject &tmp = lkoMap[it->first];
			if (tmp.LoadFromMem(objfiledata))
			{
				nodeIter = tmp.m_vectorLinkedObjectInfo.begin();
				for (;nodeIter!=tmp.m_vectorLinkedObjectInfo.end();++nodeIter)
				{
					LoadImg(nodeIter->m_strPicName, picMap, names, keySeed);
				}
			} else {
				lkoMap.erase(it->first);
			}
		}

		//DistortionObjs........
		const SubMap::ObjNameMap &dis2path = pmap.m_objs[PMap::MAP_OBJ_DISTORT].GetConstObjFileNameMap();
		for(SubMap::ObjNameMap::const_iterator it = dis2path.begin(), ie = dis2path.end(); it != ie; ++it)
		{
			XBuffer objfiledata;
			if( !m_pEB->GetFileIOManager()->GetFileImage(it->second, objfiledata))
			{
				XPTRACE(L"load distort obj file failed, file is %s\n", it->second.c_str());
				XPLOG_ERROR(L"load distort obj file failed, file is %s\n", it->second.c_str());
				continue;
			}
			PDistortionObject &tmp = disMap[it->first];
			if (tmp.LoadFromMem(objfiledata))
			{
				if (LoadImg(tmp.m_distortionObj.strImgFileName, picMap, names, keySeed))
				{
					LoadingPicResrc &img = picMap[keySeed];
					if (tmp.m_bLightFlag)
					{
						if (LoadImg(tmp.m_distortionObj.strLightFile, picMap, names, keySeed))
						{
							LoadingPicResrc &lig = picMap[keySeed];
							if (img.imgfile.m_nFileHeight != lig.imgfile.m_nFileHeight ||
								img.imgfile.m_nFileWidth != lig.imgfile.m_nFileWidth)
							{
								XPTRACE(L"distort obj img and light not match, file is %s\n", it->second.c_str());
								XPLOG_ERROR(L"distort obj img and light not match, file is %s\n", it->second.c_str());
								disMap.erase(it->first);
							}
						}
					}
				}
			} else {
				disMap.erase(it->first);
			}
		}

		//////////加载lko，dis，和img（不包括真正的图片）完成
		return true;
	}

	bool RegionMap::LoadElements(const PMap& pmap, LoadingPicResrcMap& picMap)
	{
		for(int i=0; i<PIC_ELEMENT_LAYER_COUNT; ++i)
		{
			const SubMap::ObjInfoVector &pics = pmap.m_objs[PMap::MAP_OBJ_ELEMENT].m_vectorLayers[i].vectorObjs;	
			for(SubMap::ObjInfoVector::const_iterator it = pics.begin(), ie = pics.end(); it != ie; ++it)
			{
				const SubMap::sObjInfo &pic = *it;
				if (pic.m_bDeleted)
					continue;
				LoadingPicResrcMap::iterator itppe = picMap.find(pic.m_MapKey);
				if( itppe == picMap.end() ) continue;

				LoadingPicResrc &tmp = itppe->second;
				const CRECT &maprect = pic.m_Rect;
				float hr = maprect.Width()/(float)tmp.imgfile.m_nFileWidth;
				float vr = maprect.Height()/(float)tmp.imgfile.m_nFileHeight;

				Region::OrgPicEleWithBase *pOrg = NULL;

				if( i == MID_ELEMENT_LAYER_ID_FOR_ELEMENT )
				{
					pOrg = new Region::OrgPicEleWithBase();
					m_orgPics.push_back(pOrg);
					SortBaseRectType tmpbase;
					CPOINT tmpCPoint;
					TransToDiamondRadix(static_cast<int>(tmp.imgfile.pos[0].x*hr+maprect.left),
						static_cast<int>(tmp.imgfile.pos[0].y*vr+maprect.top), tmpCPoint);
					tmpbase.left = tmpCPoint.x;
					tmpbase.bottom = tmpCPoint.y;
					TransToDiamondRadix(static_cast<int>(tmp.imgfile.pos[2].x*hr+maprect.left),
						static_cast<int>(tmp.imgfile.pos[2].y*vr+maprect.top), tmpCPoint);
					tmpbase.right = tmpCPoint.x;
					tmpbase.top = tmpCPoint.y;						
					pOrg->base = tmpbase;
					//for(std::vector<CPOINT>::const_iterator it = tmp.imgfile.m_mask.begin(), ie = tmp.imgfile.m_mask.end();
					//	it != ie; ++it)
					//{
					//	CPOINT maskpt;
					//	maskpt.x = maprect.left + (int)(hr * it->x);
					//	maskpt.y = maprect.top + (int)(vr * it->y);

					//	pOrg->mask.push_back( maskpt.ToFPOINT() );
					//}
				}
				int n = (int)tmp.imgfile.m_vectorRct.size();
				for(int j=0; j<n ; ++j)
				{
					const CRECT &srcrect = tmp.imgfile.m_vectorRct[j];
					CRECT rect;
					rect.left = maprect.left + (int)(srcrect.left * hr);
					rect.right = maprect.left + (int)(srcrect.right * hr);
					rect.top = maprect.top + (int)(srcrect.top * vr);
					rect.bottom = maprect.top + (int)(srcrect.bottom * vr);
					CPOINT center = rect.Center();

					int col = center.x/ REGION_WIDTH;
					int row = center.y/ REGION_HEIGHT;
					if( col < 0 ) col = 0; if( col >= m_regioncol ) col = m_regioncol-1;
					if( row < 0 ) row = 0; if( row >= m_regionrow ) row = m_regionrow-1;
					RegionInfo &reg = m_regions[col + row * m_regioncol];

					//
					switch (i)
					{
					case 0:	//水1
					case 1:	//水2
						AddCacheLagerPic(rect, tmp.vPicResrcs[j], pic.m_AttachColor.data, PIC_FIRST_ELEMENT_ID);
						break;
					case MID_ELEMENT_LAYER_ID_FOR_ELEMENT:	//物件中层
						{
							Region::PicEleWithBase picele;
							picele.pos = rect;
							picele.pPicResrc = tmp.vPicResrcs[j];
							picele.pOrg = pOrg;
							picele.color = pic.m_AttachColor;
							reg.m_vPicEleWithBase.push_back(picele);
						}
						break;
					default:
						{
							Region::PicEle picele;
							picele.pos = rect;
							picele.pPicResrc = tmp.vPicResrcs[j];
							picele.color = pic.m_AttachColor.data;
							reg.m_vPicEle[PIC_FIRST_ELEMENT_ID+i].push_back(picele);
						}
						break;
					}
				}

			}
		}
		return true;
	}

	bool RegionMap::LoadEffects(const PMap& pmap)
	{
		// 物件层动画(特效)
		const SubMap::ObjNameMap &effect2path = pmap.m_objs[PMap::MAP_OBJ_EFFECT].GetConstObjFileNameMap();
		
		for(int i=0; i< PIC_ELEMENT_LAYER_COUNT; ++i)
		{
			const SubMap::ObjLayer &l = pmap.m_objs[PMap::MAP_OBJ_EFFECT].m_vectorLayers[i];
			const SubMap::ObjInfoVector & effs = l.vectorObjs;
			for(SubMap::ObjInfoVector::const_iterator it = effs.begin(), ie = effs.end(); it != ie; ++it)
			{
				const SubMap::sObjInfo &eff = *it;
				if (eff.m_bDeleted)
					continue;
				const SubMap::ObjNameMap::const_iterator itn = effect2path.find(eff.m_MapKey);
				if( itn == effect2path.end() ) continue;
				const std::wstring ename = itn->second;
				//XPTRACE(L"load sceneeffect, eleid=%d, lvl=%d, path is %s\n", eff.mapKey, i, ename.c_str());
				Effect *pEffect = m_pEB->GetEffectManager()->CreateEffect(ename, true);
				if( pEffect == NULL )
					continue;
				//pEffect->AssureResource();
				pEffect->SetDirection(Nuclear::XPDIR_TOP);
				pEffect->SetLocation(eff.m_Position);
				pEffect->SetScaleType(XPEST_ALLSCALE);
				//pEffect->Play();
				Region::EffectInfo effinfo;
				effinfo.pEffect = pEffect;
				effinfo.rect = eff.m_Rect;
				effinfo.pos = eff.m_Position;
				//

				CPOINT center = effinfo.rect.Center();

				int col = center.x/ REGION_WIDTH;
				int row = center.y/ REGION_HEIGHT;
				if( col < 0 ) col = 0; if( col >= m_regioncol ) col = m_regioncol-1;
				if( row < 0 ) row = 0; if( row >= m_regionrow ) row = m_regionrow-1;
				RegionInfo &reg = m_regions[col + row * m_regioncol];

				reg.m_effects[i].push_back(effinfo);


/*				XBase base;
				if( pEffect->GetBase(base) )
				{
					CPOINT tmpCPoint;
					TransToDiamondRadix((base.offset[0].x+ pEffect->GetLocation().x),(base.offset[0].y+ pEffect->GetLocation().y),tmpCPoint);
					effinfo.base.left = tmpCPoint.x;
					effinfo.base.bottom = tmpCPoint.y;
					TransToDiamondRadix((base.offset[2].x+ pEffect->GetLocation().x),(base.offset[2].y+ pEffect->GetLocation().y),tmpCPoint);
					effinfo.base.right = tmpCPoint.x;
					effinfo.base.top = tmpCPoint.y;
				}*/

				//m_effects[i].push_back(effinfo);

			}
		}
		//触发特效...
		SubMap::TriggerInfoMap::const_iterator triggerIter;
		const SubMap::TriggerInfoMap &triggers = pmap.m_triggerObjs.GetTriggerInfos();
		SubMap::TriggerInfoVet::const_iterator triggerInfoIt;
		SubMap::TriggerAreaMap::const_iterator areaIter;
		triggerIter = triggers.begin();
		Region::TriggerObjInfo* pInfo;
		int col, row, colMin, colMax, rowMin, rowMax, triggerID, i;
		TriggerInfoVet tmpInfoVet;
		m_TriggerInfos.clear();
		for (triggerID=0;triggerIter!=triggers.end();++triggerIter,++triggerID)
		{
			m_TriggerInfos.push_back(tmpInfoVet);
			TriggerInfoVet& infoVet = m_TriggerInfos[triggerID];
			const SubMap::TriggerInfoVet &triggerinfo = triggerIter->second.triggerInfos;
			for (i=0, triggerInfoIt=triggerinfo.begin();triggerInfoIt!=triggerinfo.end();++triggerInfoIt, ++i)
			{
				SubMap::ObjNameMap::const_iterator itn = effect2path.find(triggerInfoIt->effectMapKey);
				if( itn == effect2path.end() ) continue;
				infoVet.push_back(*triggerInfoIt);
				infoVet[i].effname = itn->second;
			}
			const SubMap::TriggerAreaMap &areas = triggerIter->second.areas;
			//加进区域里面
			for (areaIter=areas.begin();areaIter!=areas.end();++areaIter)
			{
				pInfo = (Region::TriggerObjInfo*)XAlloc(sizeof(Region::TriggerObjInfo));
				pInfo->rect = areaIter->second;
				pInfo->infoID = triggerID;
				pInfo->updateTick = 0;

				colMin = pInfo->rect.left / REGION_WIDTH;
				colMax = pInfo->rect.right / REGION_WIDTH;
				rowMin = pInfo->rect.top / REGION_HEIGHT;
				rowMax = pInfo->rect.bottom / REGION_HEIGHT;
				if( colMin < 0 ) colMin = 0; if( colMin >= m_regioncol ) colMin = m_regioncol-1;
				if( colMax < 0 ) colMax = 0; if( colMax >= m_regioncol ) colMax = m_regioncol-1;
				if( rowMin < 0 ) rowMin = 0; if( rowMin >= m_regionrow ) rowMin = m_regionrow-1;
				if( rowMax < 0 ) rowMax = 0; if( rowMax >= m_regionrow ) rowMax = m_regionrow-1;
				for (col=colMin;col<=colMax;++col)
				{
					for (row=rowMin;row<=rowMax;++row)
					{
						m_regions[col + row * m_regioncol].m_vpTriggerObjInfo.push_back(pInfo);
					}
				}
			}
		}
		return true;
	}

	bool RegionMap::LoadWaterArea(const PMap& pmap)
	{
		int colMin, colMax, rowMin, rowMax, col, row;
		const SubMap::WaterAreaInfoMap& waterAreas = pmap.GetWaterAreaInfos();
		SubMap::WaterAreaInfoMap::const_iterator waterIter;
		SubMap::WaterAreaMap::const_iterator rectIter;
		waterIter = waterAreas.begin();
		Region::WaterArea area;
		for (;waterIter!=waterAreas.end();++waterIter)
		{
			const SubMap::WaterAreaMap &rects = waterIter->second.areas;
			area.border = static_cast<int>(waterIter->second.border);
			area.depth = waterIter->second.depth;
			for (rectIter=rects.begin();rectIter!=rects.end();++rectIter)
			{
				//加进区域里面
				area.rect = rectIter->second;
				colMin = area.rect.left / REGION_WIDTH;
				colMax = area.rect.right / REGION_WIDTH;
				rowMin = area.rect.top / REGION_HEIGHT;
				rowMax = area.rect.bottom / REGION_HEIGHT;
				if( colMin < 0 ) colMin = 0; if( colMin >= m_regioncol ) colMin = m_regioncol-1;
				if( colMax < 0 ) colMax = 0; if( colMax >= m_regioncol ) colMax = m_regioncol-1;
				if( rowMin < 0 ) rowMin = 0; if( rowMin >= m_regionrow ) rowMin = m_regionrow-1;
				if( rowMax < 0 ) rowMax = 0; if( rowMax >= m_regionrow ) rowMax = m_regionrow-1;
				for (col=colMin;col<=colMax;++col)
				{
					for (row=rowMin;row<=rowMax;++row)
					{
						m_regions[col + row * m_regioncol].m_vWaterArea.push_back(area);
					}
				}
			}
		}
		return true;
	}

	bool RegionMap::LoadDistortObjs(const PMap& pmap, const LoadingPicResrcMap& picMap, const LoadingDistortObjMap& disMap, const ElementNames& names)
	{
		Distort* pDistort;
		std::vector<PicResrc*>::iterator picIter;
		SubMap::ObjInfoVector::const_iterator it, ie;
		LoadingDistortObjMap::const_iterator disEnd = disMap.end();
		LoadingDistortObjMap::const_iterator disIt;
		LoadingPicResrcMap::const_iterator lprEnd = picMap.end();
		LoadingPicResrcMap::const_iterator lprIt, lprLightIt;
		ElementNames::const_iterator nameMapIter, nameMapLightIter;
		DistortionEffectParam effectParam;
		int col, row, colMin, colMax, rowMin, rowMax;

		for(int i=0; i< PIC_ELEMENT_LAYER_COUNT; ++i)
		{
			const SubMap::ObjInfoVector &objs = pmap.m_objs[PMap::MAP_OBJ_DISTORT].m_vectorLayers[i].vectorObjs;
			it = objs.begin();
			ie = objs.end();
			for (;it!=ie;++it)
			{
				const SubMap::sObjInfo &obj = *it;
				if (obj.m_bDeleted)
					continue;
				disIt = disMap.find(obj.m_MapKey);
				if (disIt == disEnd)
					continue;
				const PDistortionObject &tmpObj = disIt->second;
				nameMapIter = names.find(tmpObj.m_distortionObj.strImgFileName);
				if (nameMapIter == names.end())
				{
					continue;
				}
				lprIt = picMap.find(nameMapIter->second);
				if (lprIt == lprEnd)
					continue;
				if (tmpObj.m_bLightFlag)
				{
					nameMapLightIter = names.find(tmpObj.m_distortionObj.strLightFile);
					if (nameMapLightIter == names.end())
						continue;
					lprLightIt = picMap.find(nameMapLightIter->second);
					if (lprLightIt == lprEnd)
						continue;
				}
				const LoadingPicResrc &tmpPicResrc = lprIt->second;
				effectParam.fAmplitudes = tmpObj.m_distortionObj.fAmplitudes;
				effectParam.fCycNum = tmpObj.m_distortionObj.fCycNum;
				effectParam.fPos = 0;
				effectParam.fSpeed = tmpObj.m_distortionObj.fSpeed;
				effectParam.nType = tmpObj.m_distortionObj.nType;
				pDistort = new Distort(m_pEB, effectParam, tmpPicResrc.imgfile);
				pDistort->setZoom(1.0f);
				pDistort->setColor(obj.m_AttachColor.data);
				pDistort->setPos(obj.m_Rect);
				int n = (int)tmpPicResrc.imgfile.m_vectorRct.size();
				for(int j=0; j<n ; ++j)
				{
					const CRECT &tmpRect = tmpPicResrc.imgfile.m_vectorRct[j];
					pDistort->PushAnPic(tmpPicResrc.vPicResrcs[j], tmpRect);
					col = (obj.m_Rect.left + (tmpRect.left + tmpRect.right) / 2) / REGION_WIDTH;
					row = (obj.m_Rect.top + (tmpRect.top + tmpRect.right) / 2) / REGION_HEIGHT;
					if( col < 0 ) col = 0; if( col >= m_regioncol ) col = m_regioncol-1;
					if( row < 0 ) row = 0; if( row >= m_regionrow ) row = m_regionrow-1;
					//加进区域里面Load资源
					m_regions[col + row * m_regioncol].m_vpDistortsPic.push_back(tmpPicResrc.vPicResrcs[j]);

					if (tmpObj.m_bLightFlag)
					{
						const LoadingPicResrc &tmpLightPicResrc = lprLightIt->second;
						pDistort->PushLightPic(tmpLightPicResrc.vPicResrcs[j], tmpRect);
						m_regions[col + row * m_regioncol].m_vpDistortsPic.push_back(tmpLightPicResrc.vPicResrcs[j]);
					}
				}
				//加进区域里面
				
				colMin = obj.m_Rect.left / REGION_WIDTH;
				colMax = obj.m_Rect.right / REGION_WIDTH;
				rowMin = obj.m_Rect.top / REGION_HEIGHT;
				rowMax = obj.m_Rect.bottom / REGION_HEIGHT;
				if( colMin < 0 ) colMin = 0; if( colMin >= m_regioncol ) colMin = m_regioncol-1;
				if( colMax < 0 ) colMax = 0; if( colMax >= m_regioncol ) colMax = m_regioncol-1;
				if( rowMin < 0 ) rowMin = 0; if( rowMin >= m_regionrow ) rowMin = m_regionrow-1;
				if( rowMax < 0 ) rowMax = 0; if( rowMax >= m_regionrow ) rowMax = m_regionrow-1;
				for (col=colMin;col<=colMax;++col)
				{
					for (row=rowMin;row<=rowMax;++row)
					{
						m_regions[col + row * m_regioncol].m_vpDistorts[i].push_back(pDistort);
					}
				}

			}

		}
		return true;
	}

	bool RegionMap::LoadBackGround(const PMap& pmap, const LoadingPicResrcMap& picMap)
	{
		SubMap::PBackGoundInfoVector::const_iterator it = pmap.m_backGround.m_backGroundInfos.begin();
		SubMap::PBackGoundInfoVector::const_iterator ie = pmap.m_backGround.m_backGroundInfos.end();
		std::vector<PicResrc*>::const_iterator picSrcIter;
		std::vector<CRECT>::const_iterator rectIt, rectIe;
		Region::BackGoundInfo tmpInfo;
		LoadingPicResrcMap::const_iterator picIter, picIe = picMap.end();
		for (;it!=ie;++it)
		{
			if (it->m_bDeleted)
				continue;
			m_backGroundInfos.push_back(tmpInfo);
			Region::BackGoundInfo& info = *(m_backGroundInfos.rbegin());
			info.activeRect = it->activeRect;
			info.girdHeight = it->girdHeight;
			info.girdWidth = it->girdWidth;
			info.height = it->height;
			info.pos = it->pos;
			info.relSpeed = it->relSpeed;
			info.width = it->width;
			info.wrapHorz = it->wrapHorz;
			info.wrapVert = it->wrapVert;
			info.ppPicResrcs = NULL;
			int picPicecWidth = 0, picPicecHeight = 0, picRow = 0, picCol = 0, realW = 0, realH = 0, x = 0, y = 0;
			SubMap::ObjMapKey* pKey = it->pPics;
			for (int j=0;j<info.height;++j)
			{
				for (int i=0;i<info.width;++i, ++pKey)
				{
					if (*pKey != SubMap::INVALID_OBJ_MAP_KEY)
					{
						picIter = picMap.find(*pKey);
						if (picIter == picIe)
							continue;
						if (info.ppPicResrcs == NULL)
						{
							picCol = picIter->second.imgfile.m_nCol;
							picRow = picIter->second.imgfile.m_nRow;
							realW = info.width * picCol;
							realH = info.height * picRow;
							const CRECT &rect = *(picIter->second.imgfile.m_vectorRct.begin());
							picPicecHeight = rect.Height();
							picPicecWidth = rect.Width();
							info.ppPicResrcs = static_cast<PicResrc**>(XAlloc(realW * realH * sizeof(PicResrc*)));
							memset(info.ppPicResrcs, NULL, realW * realH * sizeof(PicResrc*));
						}
						rectIt = picIter->second.imgfile.m_vectorRct.begin();
						rectIe = picIter->second.imgfile.m_vectorRct.end();
						y = j * picRow - 1;
						picSrcIter = picIter->second.vPicResrcs.begin();
						for (;rectIt!=rectIe;++rectIt,++x, ++picSrcIter)
						{
							if (rectIt->left == 0)
							{
								++y;
								x = i * picCol;
							}
							info.ppPicResrcs[y * realW + x] = *picSrcIter;
						}
					}
				}
			}
			if (realH == 0 || realW == 0)
			{
				m_backGroundInfos.pop_back();
				continue;
			}
			info.height = realH;
			info.width = realW;
			info.girdHeight = picPicecHeight;
			info.girdWidth = picPicecWidth;
		}
		return true;
	}


	bool RegionMap::LoadLinkedObjs(const PMap& pmap, const LoadingPicResrcMap& picMap, const LoadingLinkedObjMap& lkoMap, const ElementNames& names)
	{
		Region::LkoEle* pLkoEle;
		LkoTreeNode *pTmpNode, *pRootNode;
		LkoTree* pTree;
		DWORD rootID;
		ElementNames::const_iterator nameMapIter;
		ElementNames::const_iterator nameMapEnd = names.end();
		std::vector<PicResrc*>::iterator picIter;
		PLinkedObject::VectorLinkedObjectInfo::const_iterator nodeIter;
		SubMap::ObjInfoVector::const_iterator it, ie;
		LoadingLinkedObjMap::const_iterator lkoEnd = lkoMap.end();		
		LoadingLinkedObjMap::const_iterator lkoIt;
		LoadingPicResrcMap::const_iterator lprEnd = picMap.end();
		LoadingPicResrcMap::const_iterator lprIt;
		for(int i=0; i< PIC_ELEMENT_LAYER_COUNT; ++i)
		{
			const SubMap::ObjInfoVector &objs = pmap.m_objs[PMap::MAP_OBJ_LINKEDOBJ].m_vectorLayers[i].vectorObjs;
			it = objs.begin();
			ie = objs.end();
			for (;it!=ie;++it)
			{
				const SubMap::sObjInfo &obj = *it;
				if (obj.m_bDeleted)
					continue;
				lkoIt = lkoMap.find(obj.m_MapKey);
				if (lkoIt == lkoEnd)
					continue;
				const PLinkedObject &tmpPLObj = lkoIt->second;
				rootID = tmpPLObj.GetMainNodeID();
				nodeIter = tmpPLObj.m_vectorLinkedObjectInfo.begin() + rootID;
				if (nodeIter == tmpPLObj.m_vectorLinkedObjectInfo.end())
					continue;
				pTmpNode = new LkoTreeNode();
				pTmpNode->pParent = NULL;
				pRootNode = pTmpNode;
				CPOINT rootRotateCent = nodeIter->m_rotateCent;
				int noodID = 0;
				pTree = new LkoTree(m_pEB);
				pTree->SetStaticFlag(tmpPLObj.m_bStatic);
				for (nodeIter=tmpPLObj.m_vectorLinkedObjectInfo.begin();nodeIter!=tmpPLObj.m_vectorLinkedObjectInfo.end();++nodeIter,++noodID)
				{
					nameMapIter = names.find(nodeIter->m_strPicName);
					if (nameMapIter == nameMapEnd)
					{
						if (noodID == rootID)
						{
							delete pTree;
							pTree = NULL;
							break;
						} else  {
							continue;
						}
					}
					if (noodID == rootID) 
					{
						pTmpNode = pRootNode;
					} else {
						pTmpNode = new Nuclear::LkoTreeNode();
						pTmpNode->pParent = pRootNode;
					}
					pTmpNode->rotateCent = nodeIter->m_rotateCent;
					pTmpNode->rotateCent.x += nodeIter->m_nWidth / 2;
					pTmpNode->rotateCent.y += nodeIter->m_nHeight / 2;
					if (noodID == rootID)
					{
						pTmpNode->fromMainRotateCent = (obj.m_Position - tmpPLObj.m_ptCentPos + pTmpNode->rotateCent).ToFPOINT();
					} else {
						pTmpNode->fromMainRotateCent = (nodeIter->m_rotateCent_2 - rootRotateCent).ToFPOINT();
					}
					pTmpNode->pRoot = pRootNode;
					pTmpNode->LinkedObjectMoveParam.m_nMoveFollowType = nodeIter->m_LinkedObjectMoveParam.m_nMoveFollowType;
					pTmpNode->height = nodeIter->m_nHeight;
					pTmpNode->width = nodeIter->m_nWidth;
					pTmpNode->LinkedObjectMoveParam = nodeIter->m_LinkedObjectMoveParam;
					pTmpNode->fAngle = pTmpNode->fSkewx = 0.0f;
					lprIt = picMap.find(nameMapIter->second);
					if (lprIt == lprEnd)
					{
						if (noodID == rootID)
						{
							delete pTree;
							pTree = NULL;
							delete pTmpNode;
							break;
						}
						delete pTmpNode;
						continue;
					}
					const LoadingPicResrc &tmpPicResrc = lprIt->second;
					int n = (int)tmpPicResrc.imgfile.m_vectorRct.size();
					for(int j=0; j<n ; ++j)
					{
						pLkoEle = new Region::LkoEle();
						pLkoEle->pPicResrc = tmpPicResrc.vPicResrcs[j];
						pLkoEle->pTreeNode = pTmpNode;
						pLkoEle->rect = tmpPicResrc.imgfile.m_vectorRct[j].ToFRECT();
						pTree->AddANode(pLkoEle, noodID==rootID);
					}
				}
				if (!pTree)
				{
					continue;
				}
				pTree->SetColor(obj.m_AttachColor);
				pTree->RandomBrightTime();
				pTree->AddEnded();
				//加进区域里面
				int col, row, colMin, colMax, rowMin, rowMax;
				CPOINT tmpCPoint;
				CPOINT adj = obj.m_Position - tmpPLObj.m_ptCentPos;
				TransToDiamondRadix(tmpPLObj.m_ptBasePos[Nuclear::enum_left].x+adj.x,
					tmpPLObj.m_ptBasePos[Nuclear::enum_left].y+adj.y, tmpCPoint);
				pTree->base.left = tmpCPoint.x;
				pTree->base.bottom = tmpCPoint.y;
				TransToDiamondRadix(tmpPLObj.m_ptBasePos[Nuclear::enum_right].x+adj.x,
					tmpPLObj.m_ptBasePos[Nuclear::enum_right].y+adj.y, tmpCPoint);
				pTree->base.right = tmpCPoint.x;
				pTree->base.top = tmpCPoint.y;
				pTree->m_rect = obj.m_Rect;
				colMin = obj.m_Rect.left / REGION_WIDTH;
				colMax = obj.m_Rect.right / REGION_WIDTH;
				rowMin = obj.m_Rect.top / REGION_HEIGHT;
				rowMax = obj.m_Rect.bottom / REGION_HEIGHT;
				if( colMin < 0 ) colMin = 0; if( colMin >= m_regioncol ) colMin = m_regioncol-1;
				if( colMax < 0 ) colMax = 0; if( colMax >= m_regioncol ) colMax = m_regioncol-1;
				if( rowMin < 0 ) rowMin = 0; if( rowMin >= m_regionrow ) rowMin = m_regionrow-1;
				if( rowMax < 0 ) rowMax = 0; if( rowMax >= m_regionrow ) rowMax = m_regionrow-1;
				for (col=colMin;col<=colMax;++col)
				{
					for (row=rowMin;row<=rowMax;++row)
					{
						m_regions[col + row * m_regioncol].m_vpLkoTrees[i].push_back(pTree);
					}
				}
				
				//绑图片进区域
				pTree->Update(0, 0, 0, 1.0f);
				const LkoTree::DEList &list = pTree->GetLkoEleList();
				LkoTree::DEList::const_iterator DEIter = list.begin();
				XPVECTOR2 vec, vecOut, vecSum;
				for (;DEIter!=list.end();++DEIter)
				{
					vec.x = (*DEIter)->rect.left;
					vec.y = (*DEIter)->rect.top;
					XPVec3TransformCoord(&vecOut, &vec, &(*DEIter)->pTreeNode->mTransResult);
					vecSum = vecOut;
					vec.y = (*DEIter)->rect.bottom;
					vec.x = (*DEIter)->rect.right;
					XPVec3TransformCoord(&vecOut, &vec, &(*DEIter)->pTreeNode->mTransResult);
					vecSum += vecOut;
					col = static_cast<int>(vecSum.x / 2 / REGION_WIDTH);
					row = static_cast<int>(vecSum.y / 2 / REGION_HEIGHT);
					if( col < 0 ) col = 0; if( col >= m_regioncol ) col = m_regioncol-1;
					if( row < 0 ) row = 0; if( row >= m_regionrow ) row = m_regionrow-1;
					m_regions[col + row * m_regioncol].m_vLkoEle[i].push_back(*DEIter);
				}
			}
		}
		return true;
	}
}