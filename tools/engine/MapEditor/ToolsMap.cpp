#include "StdAfx.h"
#include "MapEditor.h"
#include "ToolsMap.h"
#include "..\engine\common\util.h"
#include "..\engine\common\log.h"
#include "..\engine\common\xml.h"
#include "..\engine\map\plinkedobject.h"
#include "..\engine\map\pdistortionobject.h"

void CToolsMap::ModifyWaterAreaCanvas(const Nuclear::CRECT &oldMap, const Nuclear::CRECT &newMap)
{
	int xAdj = newMap.left - oldMap.left;
	int yAdj = newMap.top - oldMap.top;
	Nuclear::SubMap::WaterAreaInfoMap::iterator it = m_Water.m_mapWaters.begin(), 
		ie = m_Water.m_mapWaters.end();
	Nuclear::SubMap::WaterAreaMap::iterator it2, ie2;
	for (;it!=ie;++it)
	{
		Nuclear::SubMap::WaterAreaMap& areas = it->second.areas;
		for (it2=areas.begin(),ie2=areas.end();it2!=ie2;)
		{
			if (IsRectCross(oldMap, it2->second))
			{
				it2->second.left += xAdj;
				it2->second.right += xAdj;
				it2->second.top += yAdj;
				it2->second.bottom += yAdj;
				++it2;
			} else {
				areas.erase(it2++);
			}
		}
	}
}

void CToolsMap::ModifyTriggerCanvas(const Nuclear::CRECT &oldMap, const Nuclear::CRECT &newMap)
{
	int xAdj = newMap.left - oldMap.left;
	int yAdj = newMap.top - oldMap.top;
	Nuclear::SubMap::TriggerInfoMap::iterator it = m_triggerObjs.m_mapTriggers.begin(), 
		ie = m_triggerObjs.m_mapTriggers.end();
	Nuclear::SubMap::TriggerAreaMap::iterator it2, ie2;
	for (;it!=ie;++it)
	{
		Nuclear::SubMap::TriggerAreaMap& areas = it->second.areas;
		for (it2=areas.begin(),ie2=areas.end();it2!=ie2;)
		{
			if (IsRectCross(oldMap, it2->second))
			{
				it2->second.left += xAdj;
				it2->second.right += xAdj;
				it2->second.top += yAdj;
				it2->second.bottom += yAdj;
				++it2;
			} else {
				areas.erase(it2++);
			}
		}
	}
}

bool CToolsMap::SwapBackgroundInfo(int id1, int id2)
{
	if (static_cast<int>(m_backGround.m_backGroundInfos.size()) <= id1)
		return false;
	if (static_cast<int>(m_backGround.m_backGroundInfos.size()) <= id2)
		return false;
	Nuclear::SubMap::PBackGoundInfo &objinfo1 = m_backGround.m_backGroundInfos[id1];
	if (objinfo1.m_bDeleted)
		return false;
	Nuclear::SubMap::PBackGoundInfo &objinfo2 = m_backGround.m_backGroundInfos[id2];
	if (objinfo2.m_bDeleted)
		return false;
	std::swap(objinfo1, objinfo2);
	return true;
}

bool CToolsMap::DestoryLastBackgroundInfo()
{
	if (m_backGround.m_backGroundInfos.size() == 0)
		return false;
	Nuclear::SubMap::PBackGoundInfo &objinfo = *m_backGround.m_backGroundInfos.rbegin();
	if (objinfo.m_bDeleted)
		return false;
	m_backGround.m_backGroundInfos.pop_back();
	--m_backGround.m_backGroundInfoCount;
	return true;
}

bool CToolsMap::ReAddBackgroundInfo(int id)
{
	if (static_cast<int>(m_backGround.m_backGroundInfos.size()) <= id)
		return false;
	Nuclear::SubMap::PBackGoundInfo &objinfo = m_backGround.m_backGroundInfos[id];
	if (!objinfo.m_bDeleted)
		return false;
	objinfo.m_bDeleted = false;
	++m_backGround.m_backGroundInfoCount;
	return true;
}

bool CToolsMap::DelBackgroundInfo(int id)
{
	if (static_cast<int>(m_backGround.m_backGroundInfos.size()) <= id)
		return false;
	Nuclear::SubMap::PBackGoundInfo &objinfo = m_backGround.m_backGroundInfos[id];
	if (objinfo.m_bDeleted)
		return false;
	objinfo.m_bDeleted = true;
	--m_backGround.m_backGroundInfoCount;
	return true;
}

bool CToolsMap::ModifyBackgroundInfo(int id, const Nuclear::SubMap::PBackGoundInfo &info)
{
	if (info.m_bDeleted)
		return false;
	if (static_cast<int>(m_backGround.m_backGroundInfos.size()) <= id)
		return false;
	Nuclear::SubMap::PBackGoundInfo &objinfo = m_backGround.m_backGroundInfos[id];
	if (objinfo.m_bDeleted)
		return false;
	int oldBufferSize = objinfo.width * objinfo.height * sizeof(Nuclear::SubMap::ObjMapKey);
	int newBufferSize = info.width * info.height * sizeof(Nuclear::SubMap::ObjMapKey);
	if (oldBufferSize != newBufferSize)
	{
		objinfo = info;
		objinfo.pPics = static_cast<Nuclear::SubMap::ObjMapKey*>(Nuclear::XAlloc(newBufferSize));
		memset(objinfo.pPics, Nuclear::SubMap::INVALID_OBJ_MAP_KEY, newBufferSize);
	} else {
		Nuclear::SubMap::ObjMapKey* pOldKey = objinfo.pPics;
		objinfo = info;
		objinfo.pPics = pOldKey;
	}

	return true;
}

int CToolsMap::AddBackgroundInfo(const Nuclear::SubMap::PBackGoundInfo &info, Nuclear::SubMap::ObjMapKey* pPics)
{
	XPASSERT(!info.m_bDeleted);
	int result = static_cast<int>(m_backGround.m_backGroundInfos.size());
	m_backGround.m_backGroundInfos.push_back(info);
	if (pPics == NULL)
	{
		int bufferSize = info.width*info.height*sizeof(Nuclear::SubMap::ObjMapKey);
		pPics = static_cast<Nuclear::SubMap::ObjMapKey*>(Nuclear::XAlloc(bufferSize));
		memset(pPics, Nuclear::SubMap::INVALID_OBJ_MAP_KEY, bufferSize);
	}
	m_backGround.m_backGroundInfos.rbegin()->pPics = pPics;
	++m_backGround.m_backGroundInfoCount;
	return result;
}

void CToolsMap::SaveTilesToGround()
{

	unsigned int pos;
	unsigned int tileCount = GetSmlTileCount();
	TileLayerInfoList::const_iterator iter;
	unsigned char tmpTileType, nowTile;
	int layer;
	for (pos=0;pos<tileCount;pos++)
	{
		nowTile = Nuclear::SubMap::PGround::TILE_EMPTY;
		layer = Nuclear::TILES_SMALL_LAYER_COUNT;
		for (iter = m_GroundLayerInfoArray.begin();iter!=m_GroundLayerInfoArray.end();iter++)
		{
			if (!iter->isShow)
				continue;
			tmpTileType = Nuclear::SubMap::PGround::GetTileType(m_GroundLayersArray[iter->layerID][pos]);
			tmpTileType -= tmpTileType & nowTile;
			if (tmpTileType) 
			{	
				layer--;
				nowTile |= tmpTileType;	
				m_Ground.SetSmlTileData(layer, pos, m_GroundLayersArray[iter->layerID][pos]);
			}
			if (nowTile == Nuclear::SubMap::PGround::TILE_SOLID)	
			{
				break;
			}

		}
		while (layer>0)
		{
			m_Ground.SetSmlTileData(--layer, pos, Nuclear::SubMap::PGround::TILE_NULL);
		}
	}
}

void CToolsMap::SaveTilesFromGround()
{
	TileLayerInfo tmpInfo;
	Nuclear::SubMap::TileType *ptrTiles;
	int layer;
	unsigned int size, tileCount;
	size = GetSmlTileSize();
	tileCount = GetSmlTileCount();
	for(layer=0;layer<Nuclear::TILES_SMALL_LAYER_COUNT;layer++)
	{
		ptrTiles = new Nuclear::SubMap::TileType[tileCount];
		memcpy(ptrTiles, m_Ground.GetSmlTileData(layer), size);
		m_GroundLayersArray.push_back(ptrTiles);
		tmpInfo.isShow = true;
		tmpInfo.layerID = Nuclear::TILES_SMALL_LAYER_COUNT - layer - 1;
		tmpInfo.name.Format(_T("图层 %d"), layer+1);
		m_GroundLayerInfoArray.push_back(tmpInfo);
	}
}

void CToolsMap::MargeMap(CToolsMap& from)
{
	int tileSize = from.GetSmlTileSize();
	int tileCount = from.GetSmlTileCount();
	TileLayerInfoList::iterator infoIt, infoIe;
	infoIt = from.m_GroundLayerInfoArray.begin();
	infoIe = from.m_GroundLayerInfoArray.end();
	for (;infoIt!=infoIe;++infoIt)
	{
		m_GroundLayersArray.push_back(from.m_GroundLayersArray[infoIt->layerID]);
		infoIt->layerID = static_cast<int>(m_GroundLayersArray.size()) - 1;
		m_GroundLayerInfoArray.push_back(*infoIt);
	}
	from.m_GroundLayerInfoArray.clear();
	from.m_GroundLayersArray.clear();
	int colorCount = m_Ground.GetColorCount();
	if (colorCount == from.m_Ground.GetColorCount())
	{
		unsigned int *resultColorData = GetColorData();
		unsigned int *tmpColorData = from.GetColorData();
		for (int ii = 0;ii<colorCount; ++ii,++resultColorData, ++tmpColorData)
		{
			if ((*resultColorData != *tmpColorData) && (*resultColorData == 0xFFFFFFFF))
			{
				*resultColorData = *tmpColorData;
			}
		}
	}

	tileCount = from.GetMidTileCount();
	for (int i=0;i<Nuclear::TILES_MID_LAYER_COUNT;++i)
	{
		Nuclear::SubMap::BiggerTileType *pBiggerData = m_Ground.GetMidTileData(i);
		Nuclear::SubMap::BiggerTileType *pFromBiggerData = from.m_Ground.GetMidTileData(i);
		for (int j=0;j<tileCount;++j,++pBiggerData,++pFromBiggerData)
		{
			if (pBiggerData->data == Nuclear::SubMap::PGround::BIGGER_TILE_NULL.data)
			{
				pBiggerData->data = pFromBiggerData->data;
			}
		}
	}


	tileCount = from.GetBigTileCount();
	Nuclear::SubMap::BiggerTileType *pBiggerData = m_Ground.GetBigTileData();
	Nuclear::SubMap::BiggerTileType *pFromBiggerData = from.m_Ground.GetBigTileData();
	for (int j=0;j<tileCount;++j,++pBiggerData,++pFromBiggerData)
	{
		if (pBiggerData->data == Nuclear::SubMap::PGround::BIGGER_TILE_NULL.data)
		{
			pBiggerData->data = pFromBiggerData->data;
		}
	}

	unsigned char i, j, layerCount;
	layerCount = Nuclear::MAP_LAYER_COUNT - 1;
	PMap::MAP_OBJ_TYPE objType;
	Nuclear::SubMap::ObjInfoVector::iterator iter;
	for (j=0;j<PMap::MAP_OBJ_COUNT;++j)
	{
		objType = static_cast<PMap::MAP_OBJ_TYPE>(j);
		for (i=0;i<layerCount;i++)
		{
			Nuclear::SubMap::ObjInfoVector &objs = from.m_objs[j].GetObjInfos(i);
			for (iter=objs.begin();iter!=objs.end();++iter)
			{
				Nuclear::SubMap::ObjMapKey mapKey = GetObjMapKey(objType, from.GetObjFileName(objType, iter->m_MapKey));
				AddObj(objType, i, mapKey, iter->m_Rect, iter->m_Position, iter->m_AttachColor.data);
			}
		}				
	}
}

bool CToolsMap::DeleteGroundLayer(int layer)
{
	const TileLayerInfo& layerGroundInfo = GetGroundLayerInfo(layer);
	int layerID = layerGroundInfo.layerID;
	delete (m_GroundLayersArray[layerID]);
	m_GroundLayersArray.erase(remove(m_GroundLayersArray.begin(), m_GroundLayersArray.end(), m_GroundLayersArray[layerGroundInfo.layerID]));
	m_GroundLayerInfoArray.erase(remove(m_GroundLayerInfoArray.begin(), m_GroundLayerInfoArray.end(), layerGroundInfo));
	TileLayerInfoList::iterator iter = m_GroundLayerInfoArray.begin();
	for(;iter!=m_GroundLayerInfoArray.end();iter++)
	{
		if (iter->layerID > layerID)
		{
			iter->layerID--;
		}
	}
	return true;
}

bool CToolsMap::NewGroundLayer(int count, int size)
{
	if (count == 0)
		count = GetSmlTileCount();
	if (size == 0)
		size = GetSmlTileSize();
	Nuclear::SubMap::TileType *ptrTiles = new Nuclear::SubMap::TileType[count];
	if (!ptrTiles)
		return false;
	ZeroMemory(ptrTiles, size);

	m_GroundLayersArray.push_back(ptrTiles);
	TileLayerInfo tmpInfo;
	tmpInfo.isShow = true;
	tmpInfo.layerID = 0;
	m_GroundLayerInfoArray.push_back(tmpInfo);
	TileLayerInfoList::reverse_iterator iter = m_GroundLayerInfoArray.rbegin();
	for(;;iter++)
	{
		if (iter+1==m_GroundLayerInfoArray.rend())
			break;
		iter->isShow = (iter+1)->isShow;
		iter->layerID = (iter+1)->layerID;
		iter->name = (iter+1)->name;
	}
	iter->isShow = true;
	iter->layerID = static_cast<int>(m_GroundLayersArray.size() - 1);
	iter->name.Format(_T("图层 %d"), iter->layerID+1);
	return true;
}

void CToolsMap::ResetMapNamesToObjMapKeys()
{
	Nuclear::SubMap::ObjNameMap::const_iterator myIterator, iterEnd;
	for (int i=0;i<PMap::MAP_OBJ_COUNT;++i)
	{
		const Nuclear::SubMap::ObjNameMap &mapObjNames = m_objs[i].GetConstObjFileNameMap();
		myIterator = mapObjNames.begin();
		iterEnd = mapObjNames.end();
		m_nMaxObjMapKey[i] = static_cast<Nuclear::SubMap::ObjMapKey>(mapObjNames.size() - 1);
		for (; myIterator!=iterEnd; ++myIterator)
		{
			m_mapNamesToObjMapKey[i][myIterator->second] = myIterator->first;
			if (myIterator->first > m_nMaxObjMapKey[i])
			{
				m_nMaxObjMapKey[i] = myIterator->first;
			}
		}

	}
}

const Nuclear::XIStream& CToolsMap::unmarshal(const Nuclear::XIStream &os)
{
	if (m_bIsExportImport)
		return PMap::unmarshal(os);
	int toolsVersion = 0;
	if (*((char*)os.begin()) != 'R')
	{
		char fileHead[4];
		os.pop_byte(fileHead, sizeof(char) * 4);
		if (fileHead[0] != 'M' || fileHead[1] != 'R' || fileHead[2] != 'M' || fileHead[3] != 'P' )
			throw Exception();
		os >> toolsVersion;
	}
	PMap::unmarshal(os);
	wchar_t *layerName;
	unsigned int layerNameSize;
	int layerCount;
	int layer;
	unsigned char isShow;
	os >> layerCount;
	int TileSize = GetSmlTileSize();
	for (layer=0;layer<layerCount;layer++)
	{
		NewGroundLayer();
		os >> isShow;
		m_GroundLayerInfoArray[0].isShow = (isShow==1)?true:false;
		os.pop_byte(reinterpret_cast<char*>(m_GroundLayersArray[m_GroundLayerInfoArray[0].layerID]), TileSize);
		os >> layerNameSize;
		layerName = reinterpret_cast<wchar_t*>(Nuclear::XAlloc(layerNameSize + 2));
		memset(layerName, 0, layerNameSize + 2);
		os.pop_byte(reinterpret_cast<char*>(layerName), layerNameSize);
		m_GroundLayerInfoArray[0].name = layerName;
		Nuclear::XFree(layerName);
	}
	return os;
}

Nuclear::XOStream& CToolsMap::marshal(Nuclear::XOStream &os) const
{
	if (m_bIsExportImport)
		return PMap::marshal(os);
	const char fileHead[4] = {'M','R','M','P'};
	os.push_byte(fileHead, sizeof(char) * 4);
	os << TOOLS_VERSION;
	PMap::marshal(os);
	CString layerName;
	unsigned int GroundLayerNameSize;
	wchar_t *pLayerName;
	os << static_cast<int>(m_GroundLayerInfoArray.size());
	TileLayerInfoList::const_reverse_iterator iter = m_GroundLayerInfoArray.rbegin();
	int TileSize = GetSmlTileSize();
	for(;iter!=m_GroundLayerInfoArray.rend();iter++)
	{
		bool a = iter->isShow;
		int b = iter->layerID;
		os << static_cast<unsigned char>((iter->isShow)?1:0);
		os.push_byte(reinterpret_cast<char*>(m_GroundLayersArray[iter->layerID]), TileSize);
		layerName = iter->name;
		GroundLayerNameSize = (layerName.GetLength()) << 1;
		pLayerName = layerName.GetBuffer(GroundLayerNameSize);
		os << GroundLayerNameSize;
		os.push_byte(reinterpret_cast<char*>(pLayerName), GroundLayerNameSize);
		layerName.ReleaseBuffer();
	}
	return os;
}

bool CToolsMap::CleanMap(MapFileState &res)
{
	std::wstring filename;

	CleanGround(res.groundFile);
	CleanElement(res.elementFile);
	CleanEffect(res.effectFile);
	CleanLinkedObjects(res.linkedObjectFile, res.elementFile);
	CleanDistortObjects(res.disFile, res.elementFile);
	return true;	

}

void CToolsMap::CleanDistortObjects(FileStates &res, FileStates &eleres)
{
	Nuclear::SubMap::ObjNameMap &names = m_objs[MAP_OBJ_DISTORT].GetObjFileNameMap();
	std::set<Nuclear::SubMap::ObjMapKey> usedObjs;
	std::set<Nuclear::SubMap::ObjMapKey> missObjs;
	Nuclear::SubMap::ObjInfoVector::iterator it, ie;
	int layerCount = Nuclear::MAP_LAYER_COUNT-1;
	bool ok = true;
	for (int i=0;i<layerCount;++i)
	{
		Nuclear::SubMap::ObjInfoVector& vet = m_objs[MAP_OBJ_DISTORT].GetObjInfos(i);
		it = vet.begin();
		ie = vet.end();
		for (;it!=ie;++it)
		{
			if (it->m_bDeleted)
				continue;
			if (usedObjs.find(it->m_MapKey) == usedObjs.end())
			{
				if (missObjs.find(it->m_MapKey) == missObjs.end())
				{
					const std::wstring &filename = names[it->m_MapKey];
					ok = true;
					if (PFS::CFile::IsFileExisting(filename))
					{
						Nuclear::PDistortionObject obj;
						if (obj.Load(filename.c_str()))
						{
							std::wstring picName = obj.m_distortionObj.strImgFileName;
							picName.erase(picName.length() - 4, 4);	
							if (eleres.usedFiles.find(picName) == eleres.usedFiles.end())
							{
								if (eleres.missFiles.find(picName) == eleres.missFiles.end())
								{
									if (PFS::CFile::IsFileExisting(obj.m_distortionObj.strImgFileName))
									{
										eleres.usedFiles.insert(picName);
									} else {
										eleres.missFiles.insert(picName);
										ok = false;
									}
								} else {
									ok = false;
								}
							}
						} else {
							ok = false;
						}
					} else {
						ok = false;
					}
					if (ok)
					{
						usedObjs.insert(it->m_MapKey);
						res.usedFiles.insert(filename);
					} else {
						missObjs.insert(it->m_MapKey);
						res.missFiles.insert(filename);
						m_objs[MAP_OBJ_DISTORT].DeleteObj(i, it->m_ObjectId);
					}
				} else {
					m_objs[MAP_OBJ_DISTORT].DeleteObj(i, it->m_ObjectId);
				}
			}
		}
	}
	Nuclear::SubMap::ObjNameMap::iterator nit = names.begin(), nie = names.end();
	std::set<Nuclear::SubMap::ObjMapKey>::iterator uie = usedObjs.end();
	for (;nit!=nie;)
	{
		if (usedObjs.find(nit->first) == uie)
		{
			nit = names.erase(nit);
		} else {
			++nit;
		}
	}
}

void CToolsMap::CleanLinkedObjects(FileStates &res, FileStates &eleres)
{
	Nuclear::SubMap::ObjNameMap &names = m_objs[MAP_OBJ_LINKEDOBJ].GetObjFileNameMap();
	std::set<Nuclear::SubMap::ObjMapKey> usedObjs;
	std::set<Nuclear::SubMap::ObjMapKey> missObjs;
	Nuclear::SubMap::ObjInfoVector::iterator it, ie;
	int layerCount = Nuclear::MAP_LAYER_COUNT-1;
	bool ok = true;
	for (int i=0;i<layerCount;++i)
	{
		Nuclear::SubMap::ObjInfoVector& vet = m_objs[MAP_OBJ_LINKEDOBJ].GetObjInfos(i);
		it = vet.begin();
		ie = vet.end();
		for (;it!=ie;++it)
		{
			if (it->m_bDeleted)
				continue;
			if (usedObjs.find(it->m_MapKey) == usedObjs.end())
			{
				if (missObjs.find(it->m_MapKey) == missObjs.end())
				{
					const std::wstring &filename = names[it->m_MapKey];
					ok = true;
					if (PFS::CFile::IsFileExisting(filename))
					{
						Nuclear::PLinkedObject  obj;
						if (obj.Load(filename.c_str()))
						{
							Nuclear::PLinkedObject::VectorLinkedObjectInfo::iterator eIt, eIe;
							eIt = obj.m_vectorLinkedObjectInfo.begin();
							eIe = obj.m_vectorLinkedObjectInfo.end();
							for (;eIt!=eIe;++eIt)
							{
								std::wstring picName = eIt->m_strPicName;
								picName.erase(picName.length() - 4, 4);	
								if (eleres.usedFiles.find(picName) == eleres.usedFiles.end())
								{
									if (eleres.missFiles.find(picName) == eleres.missFiles.end())
									{
										if (PFS::CFile::IsFileExisting(eIt->m_strPicName))
										{
											eleres.usedFiles.insert(picName);
										} else {
											eleres.missFiles.insert(picName);
											ok = false;
										}
									} else {
										ok = false;
									}
								}
							}
						} else {
							ok = false;
						}
					} else {
						ok = false;
					}
					if (ok)
					{
						usedObjs.insert(it->m_MapKey);
						res.usedFiles.insert(filename);
					} else {
						missObjs.insert(it->m_MapKey);
						res.missFiles.insert(filename);
						m_objs[MAP_OBJ_LINKEDOBJ].DeleteObj(i, it->m_ObjectId);
					}
				} else {
					m_objs[MAP_OBJ_LINKEDOBJ].DeleteObj(i, it->m_ObjectId);
				}
			}
		}
	}
	Nuclear::SubMap::ObjNameMap::iterator nit = names.begin(), nie = names.end();
	std::set<Nuclear::SubMap::ObjMapKey>::iterator uie = usedObjs.end();
	for (;nit!=nie;)
	{
		if (usedObjs.find(nit->first) == uie)
		{
			nit = names.erase(nit);
		} else {
			++nit;
		}
	}
}

void CToolsMap::CleanEffect(FileStates &res)
{
	Nuclear::SubMap::ObjNameMap &names = m_objs[MAP_OBJ_EFFECT].GetObjFileNameMap();
	std::set<Nuclear::SubMap::ObjMapKey> usedObjs;
	std::set<Nuclear::SubMap::ObjMapKey> missObjs;
	Nuclear::SubMap::ObjInfoVector::iterator it, ie;
	int layerCount = Nuclear::MAP_LAYER_COUNT-1;
	for (int i=0;i<layerCount;++i)
	{
		Nuclear::SubMap::ObjInfoVector& vet = m_objs[MAP_OBJ_EFFECT].GetObjInfos(i);
		it = vet.begin();
		ie = vet.end();
		for (;it!=ie;++it)
		{
			if (it->m_bDeleted)
				continue;
			if (usedObjs.find(it->m_MapKey) == usedObjs.end())
			{
				if (missObjs.find(it->m_MapKey) == missObjs.end())
				{
					if (CheckEffectRefCount(names[it->m_MapKey], res))
					{
						usedObjs.insert(it->m_MapKey);
					} else {
						missObjs.insert(it->m_MapKey);
						m_objs[MAP_OBJ_EFFECT].DeleteObj(i, it->m_ObjectId);
					}
				} else {
					m_objs[MAP_OBJ_EFFECT].DeleteObj(i, it->m_ObjectId);
				}
			}
		}
	}

	Nuclear::SubMap::TriggerInfoMap &tinfoMap = m_triggerObjs.m_mapTriggers;
	Nuclear::SubMap::TriggerInfoMap::iterator tit = tinfoMap.begin(), tie = tinfoMap.end();
	Nuclear::SubMap::TriggerInfoVet::iterator civit, civie;
	bool ok = true;
	for (;tit!=tie;++tit)
	{
		ok = true;
		civit = tit->second.triggerInfos.begin();
		civie = tit->second.triggerInfos.end();
		for (;civit!=civie;++civit)
		{
			if (civit->triggerTypeFlag & Nuclear::SubMap::TRIGGER_DELETE)
				continue;
			if (usedObjs.find(civit->effectMapKey) == usedObjs.end())
			{
				if (missObjs.find(civit->effectMapKey) == missObjs.end())
				{
					if (CheckEffectRefCount(names[civit->effectMapKey], res))
					{
						usedObjs.insert(civit->effectMapKey);
					} else {
						missObjs.insert(civit->effectMapKey);
						civit->triggerTypeFlag |= Nuclear::SubMap::TRIGGER_DELETE;
					}
				} else {
					civit->triggerTypeFlag |= Nuclear::SubMap::TRIGGER_DELETE;
				}
			}
		}
	}

	Nuclear::SubMap::ObjNameMap::iterator nit = names.begin(), nie = names.end();
	std::set<Nuclear::SubMap::ObjMapKey>::iterator uie = usedObjs.end();
	for (;nit!=nie;)
	{
		if (usedObjs.find(nit->first) == uie)
		{
			nit = names.erase(nit);
		} else {
			++nit;
		}
	}
}

bool CToolsMap::CheckEffectRefCount(const std::wstring &name, FileStates &res)
{
	if( 0 == name.find(L"ani") )
	{

		if (PFS::CFile::IsFileExisting(L"/effect/" + name + SFI_ExtName))
		{
			res.usedFiles.insert(name);
			return true;
		} else {
			res.missFiles.insert(name);
			return false;
		}
	}
	else if( 0 == name.find(L"par") )
	{

		if (PFS::CFile::IsFileExisting(L"/effect/" + name + L"." + FNExt_ParticleWS))
		{
			res.usedFiles.insert(name);
			return true;
		} else {
			res.missFiles.insert(name);
			return false;
		}
	}
	else if( 0 == name.find(L"spine") )
	{

		if (PFS::CFile::IsFileExisting(L"/effect/" + name + SPINE_ExtName))
		{
			res.usedFiles.insert(name);
			return true;
		} else {
			res.missFiles.insert(name);
			return false;
		}
	}
	else if ( 0 == name.find(L"geffect") )
	{

		std::wstring uri = L"/effect/" + name + Effect_ExtName;
		Nuclear::XBuffer xmlfiledata;
		if( !theApp.GetFileIOManager()->GetFileImage(uri, xmlfiledata))
		{
			res.missFiles.insert(name);
			return false;
		}
		XMLIO::CFileReader fr;
		int nError = fr.OpenData(xmlfiledata.constbegin(), xmlfiledata.size());
		if( XMLIO::EC_SUCCESS != nError)
		{
			fr.CloseFile();
			res.missFiles.insert(name);
			return false;
		}

		XMLIO::CINode root;
		if (!fr.GetRootNode(root))
		{
			fr.CloseFile();
			res.missFiles.insert(name);
			return false;
		}

		XMLIO::CNodeList clipsNodeList;
		root.GetChildren( clipsNodeList);

		for ( XMLIO::CNodeList::iterator i = clipsNodeList.begin(), e = clipsNodeList.end(); i != e; ++i)
		{
			XMLIO::CINode& node = *i;
			if (node.GetType() != XMLIO::NT_ELEMENT)
				continue;
			if (node.GetName() != L"clip")	
			{
				std::wstring strRes;
				if (! node.GetAttribute(L"r_f", strRes) )
				{
					fr.CloseFile();
					res.missFiles.insert(name);
					return false;
				}
				if (!CheckEffectRefCount(strRes, res))
				{
					fr.CloseFile();
					res.missFiles.insert(name);
					return false;
				}
			}
		}
		fr.CloseFile();
		res.usedFiles.insert(name);
		return true;
	}

	res.missFiles.insert(name);
	return false;
}

void CToolsMap::CleanElement(FileStates &res)
{
	Nuclear::SubMap::ObjNameMap &names = m_objs[MAP_OBJ_ELEMENT].GetObjFileNameMap();
	std::set<Nuclear::SubMap::ObjMapKey> usedObjs;
	std::set<Nuclear::SubMap::ObjMapKey> missObjs;
	Nuclear::SubMap::ObjInfoVector::iterator it, ie;
	int layerCount = Nuclear::MAP_LAYER_COUNT-1;
	for (int i=0;i<layerCount;++i)
	{
		Nuclear::SubMap::ObjInfoVector& vet = m_objs[MAP_OBJ_ELEMENT].GetObjInfos(i);
		it = vet.begin();
		ie = vet.end();
		for (;it!=ie;++it)
		{
			if (it->m_bDeleted)
				continue;
			if (usedObjs.find(it->m_MapKey) == usedObjs.end())
			{
				if (missObjs.find(it->m_MapKey) == missObjs.end())
				{
					const std::wstring &filename = names[it->m_MapKey];
					XPASSERT(filename != L"" && L"物件名字表里面找不到记录");
					if (PFS::CFile::IsFileExisting(filename + ImageSet_ExtName))
					{
						usedObjs.insert(it->m_MapKey);
						res.usedFiles.insert(filename);
					} else {
						missObjs.insert(it->m_MapKey);
						res.missFiles.insert(filename);
						m_objs[MAP_OBJ_ELEMENT].DeleteObj(i, it->m_ObjectId);
					}
				} else {
					m_objs[MAP_OBJ_ELEMENT].DeleteObj(i, it->m_ObjectId);
				}
			}
		}
	}

	Nuclear::SubMap::PBackGoundInfoVector::iterator git = m_backGround.m_backGroundInfos.begin(), gie = m_backGround.m_backGroundInfos.end();
	int picCount;
	Nuclear::SubMap::ObjMapKey *pKeys;
	for (;git!=gie;++git)
	{
		picCount = git->width * git->height;
		pKeys = git->pPics;
		for (int i=0;i<picCount;++i,++pKeys)
		{
			if (*pKeys != Nuclear::SubMap::INVALID_OBJ_MAP_KEY)
			{
				if (usedObjs.find(*pKeys) == usedObjs.end())
				{
					if (missObjs.find(*pKeys) == missObjs.end())
					{
						const std::wstring &filename = names[*pKeys];
						XPASSERT(filename != L"" && L"物件名字表里面找不到移动背景记录");
						if (PFS::CFile::IsFileExisting(filename + ImageSet_ExtName))
						{
							usedObjs.insert(*pKeys);
							res.usedFiles.insert(filename);
						} else {
							missObjs.insert(*pKeys);
							res.missFiles.insert(filename);
							*pKeys = Nuclear::SubMap::INVALID_OBJ_MAP_KEY;
						}
					} else {
						*pKeys = Nuclear::SubMap::INVALID_OBJ_MAP_KEY;
					}
				}
			}
		}
	}
	Nuclear::SubMap::ObjNameMap::iterator nit = names.begin(), nie = names.end();
	std::set<Nuclear::SubMap::ObjMapKey>::iterator uie = usedObjs.end();
	for (;nit!=nie;)
	{
		if (usedObjs.find(nit->first) == uie)
		{
			nit = names.erase(nit);
		} else {
			++nit;
		}
	}
}

void CToolsMap::CleanGround(FileStates &res)
{
	std::wstring filename;
	TILT_TYPE_ID tileTypeId;
	int jjj = sizeof(TILT_TYPE_ID);
	const std::map<unsigned int, bool> &tileTypeMap = theApp.GetTileType();
	std::map<unsigned int, bool>::const_iterator tileIter;
	Nuclear::SubMap::PPathMap* pPathMap = theApp.GetPathMap();

	{
		tileTypeId.type = 0;
		std::set<Nuclear::SubMap::TileType> usedTile;
		std::set<Nuclear::SubMap::TileType> missTile;
		Nuclear::SubMap::TileType *pTiles;
		int tileSize = m_Ground.GetSmlTileCount();
		int j;
		usedTile.insert(Nuclear::SubMap::PGround::TILE_NULL);

		for (int i=0;i<4;++i)
		{
			pTiles = m_Ground.GetSmlTileData(i);
			for (j=0;j<tileSize;++j,++pTiles)
			{
				if (usedTile.find(*pTiles) == usedTile.end())
				{
					if (missTile.find(*pTiles) == missTile.end())
					{
						tileTypeId.tileType = Nuclear::SubMap::PGround::GetGroundType(*pTiles);
						tileIter = tileTypeMap.find(tileTypeId.id);
						if (tileIter == tileTypeMap.end())
						{
							if (tileTypeId.tileType != 0)
							{
								CString tmpFile;
								tmpFile.Format(L"小地表：地表号 %d, 地表形状 %d, 第 %d 块", 
									tileTypeId.tileType, Nuclear::SubMap::PGround::GetTileType(*pTiles),
									Nuclear::SubMap::PGround::GetTileNumber(*pTiles));
								res.missFiles.insert(tmpFile.GetBuffer());
							}
							missTile.insert(*pTiles);
							*pTiles = Nuclear::SubMap::PGround::TILE_NULL;
						} else {
							if ((Nuclear::SubMap::PGround::GetTileType(*pTiles) != 15)	&&
								(!tileIter->second))	
							{

								missTile.insert(*pTiles);
								*pTiles = Nuclear::SubMap::PGround::TILE_NULL;
							} else {
								filename = pPathMap->GetTileFileName(*pTiles);
								if (filename == L"")
								{
									XPASSERT(false && L"pPathMap->GetTileFileName(*pTiles)为空");
								} else {
									if (PFS::CFile::IsFileExisting(filename))
									{
										usedTile.insert(*pTiles);
										res.usedFiles.insert(filename);
									} else {
										missTile.insert(*pTiles);
										res.missFiles.insert(filename);
										*pTiles = Nuclear::SubMap::PGround::TILE_NULL;
									}
								}
							}
						}
					} else {
						*pTiles = Nuclear::SubMap::PGround::TILE_NULL;
					}
				}
			}
		}
	}

	{
		tileTypeId.type = 1;
		std::set<unsigned short> usedTile;
		std::set<unsigned short> missTile;
		Nuclear::SubMap::PtrBiggerTileType pTiles;
		int tileSize = m_Ground.GetMidTileCount();
		int j;
		for (int i=0;i<2;++i)
		{
			pTiles = m_Ground.GetMidTileData(i);
			for (j=0;j<tileSize;++j,++pTiles)
			{
				if (pTiles->tileType == 0)
					continue;
				if (usedTile.find(pTiles->data) == usedTile.end())
				{
					if (missTile.find(pTiles->data) == missTile.end())
					{
						tileTypeId.tileType = pTiles->tileType;
						tileIter = tileTypeMap.find(tileTypeId.id);
						if (tileIter == tileTypeMap.end())
						{
							if (pTiles->tileType != 0)
							{
								CString tmpFile;
								tmpFile.Format(L"中地表：地表号 %d, 第 %d 块", pTiles->tileType, pTiles->tileID);
								res.missFiles.insert(tmpFile.GetBuffer());
							}
							missTile.insert(pTiles->data);
							*pTiles = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
						} else {
							if ((pTiles->tileID < 15) &&
								(!tileIter->second))	
							{

								missTile.insert(pTiles->data);
								*pTiles = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
							} else {
								filename = pPathMap->GetTileFileName(Nuclear::TST_MIDDLE, *pTiles);
								if (filename == L"")
								{
									XPASSERT(false && L"pPathMap->GetTileFileName(Nuclear::TST_MIDDLE, *pTiles)为空");
								} else {
									if (PFS::CFile::IsFileExisting(filename))
									{
										usedTile.insert(pTiles->data);
										res.usedFiles.insert(filename);
									} else {
										missTile.insert(pTiles->data);
										res.missFiles.insert(filename);
										*pTiles = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
									}
								}
							}
						}
					} else {
						*pTiles = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
					}
				}
			}
		}
	}


	{
		tileTypeId.type = 2;
		std::set<unsigned short> usedTile;
		std::set<unsigned short> missTile;
		Nuclear::SubMap::PtrBiggerTileType pTiles;
		int tileSize = m_Ground.GetBigTileCount();
		int j;
		pTiles = m_Ground.GetBigTileData();
		for (j=0;j<tileSize;++j,++pTiles)
		{
			if (pTiles->tileType == 0)
				continue;
			if (usedTile.find(pTiles->data) == usedTile.end())
			{
				if (missTile.find(pTiles->data) == missTile.end())
				{
					tileTypeId.tileType = pTiles->tileType;
					tileIter = tileTypeMap.find(tileTypeId.id);
					if (tileIter == tileTypeMap.end())
					{
						if (pTiles->tileType != 0)
						{
							CString tmpFile;
							tmpFile.Format(L"大地表：地表号 %d, 第 %d 块", pTiles->tileType, pTiles->tileID);
							res.missFiles.insert(tmpFile.GetBuffer());
						}
						missTile.insert(pTiles->data);
						*pTiles = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
					} else {
						if ((pTiles->tileID < 15) &&
							(!tileIter->second))	
						{

							missTile.insert(pTiles->data);
							*pTiles = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
						} else {
							filename = pPathMap->GetTileFileName(Nuclear::TST_BIG, *pTiles);
							if (filename == L"")
							{
								XPASSERT(false && L"pPathMap->GetTileFileName(Nuclear::TST_BIG, *pTiles)为空");
							} else {
								if (PFS::CFile::IsFileExisting(filename))
								{
									usedTile.insert(pTiles->data);
									res.usedFiles.insert(filename);
								} else {
									missTile.insert(pTiles->data);
									res.missFiles.insert(filename);
									*pTiles = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
								}
							}
						}
					}
				} else {
					*pTiles = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
				}
			}
		}
	}
}

bool CToolsMap::ImportFromFile(LPCTSTR fn)
{
	FILE *f = Nuclear::FOpen(fn, L"rb");
	if( f == NULL )
		return false;
	m_bIsExportImport = true;
	bool r = Load(f);
	m_bIsExportImport = false;
	fclose(f);
	SaveTilesFromGround();
	ResetMapNamesToObjMapKeys();
	return r;
}

bool CToolsMap::ExportToFile(LPCTSTR fn, MapFileState &res)
{
	CToolsMap tmpMap;
	tmpMap = *this;
	tmpMap.SaveTilesToGround();
	tmpMap.CleanMap(res);
	FILE *f = Nuclear::FOpen(fn, L"wb");
	if( f == NULL )
		return false;
	tmpMap.m_bIsExportImport = true;
	bool r = tmpMap.Save(f);
	tmpMap.m_bIsExportImport = false;
	fclose(f);
	return r;
}

void OutFileName(FILE *f, const CToolsMap::FileSet& file)
{
	CToolsMap::FileSet::const_iterator it = file.begin(), ie = file.end();
	for (;it!=ie;++it)
	{
		fwprintf(f, L"  %s\n", it->c_str());
	}
}

bool CToolsMap::ExpRes(LPCTSTR fn, const MapFileState &res)
{
	FILE *f = Nuclear::FOpen(fn, L"w");
	if( f == NULL )
		return false;
	bool r = false;
	if (res.groundFile.missFiles.size() > 0)
	{
		fwprintf(f, L"下列地表文件丢失：\n");
		OutFileName(f, res.groundFile.missFiles);
		r = true;
	}
	if (res.elementFile.missFiles.size() > 0)
	{
		fwprintf(f, L"下列物件文件丢失：\n");
		OutFileName(f, res.elementFile.missFiles);
		r = true;
	}	
	if (res.effectFile.missFiles.size() > 0)
	{
		fwprintf(f, L"下列特效文件丢失或损坏：\n");
		OutFileName(f, res.effectFile.missFiles);
		r = true;
	}
	if (res.linkedObjectFile.missFiles.size() > 0)
	{
		fwprintf(f, L"下列链接物件文件丢失或损坏：\n");
		OutFileName(f, res.linkedObjectFile.missFiles);
		r = true;
	}
	if (res.disFile.missFiles.size() > 0)
	{
		fwprintf(f, L"下列飘动物件文件丢失或损坏：\n");
		OutFileName(f, res.disFile.missFiles);
		r = true;
	}
	fclose(f);
	return r;
}

CToolsMap& CToolsMap::operator=(const CToolsMap& o)
{
	if (&o == this)
		return *this;
	PMap::operator=(o);
	Nuclear::SubMap::PtrTileTypeList::iterator iter = m_GroundLayersArray.begin();
	for (;iter!=m_GroundLayersArray.end();++iter)
	{
		delete (*iter);
	}
	m_GroundLayersArray.clear();
	m_GroundLayerInfoArray = o.m_GroundLayerInfoArray;

	unsigned int size, count;
	Nuclear::SubMap::PtrTileType pTile;
	size = o.GetSmlTileSize();
	count = o.GetSmlTileCount();
	Nuclear::SubMap::PtrTileTypeList::const_iterator newIter = o.m_GroundLayersArray.begin();
	for (;newIter!=o.m_GroundLayersArray.end();++newIter)
	{
		pTile = new Nuclear::SubMap::TileType[count];
		memcpy(pTile, *newIter, size);
		m_GroundLayersArray.push_back(pTile);
	}
	return (*this);
}

CToolsMap::~CToolsMap(void)
{
	Nuclear::SubMap::PtrTileTypeList::iterator iter = m_GroundLayersArray.begin();
	for (;iter!=m_GroundLayersArray.end();++iter)
	{
		delete [] (*iter);
	}
}

CToolsMap::CToolsMap(void): m_bIsExportImport(false)
{
	for (int i=0;i<PMap::MAP_OBJ_COUNT;++i)
	{
		m_nMaxObjMapKey[i] = 0;
	}
}

