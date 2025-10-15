#include "stdafx.h"
#include "..\common\log.h"
//#include "..\common\pfsutil.h"
#include "..\common\mapconst.h"
#include "mapobjs.h"
#include "pmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		MapObjs::MapObjs(void) : m_nSign(0)
		{
			m_vectorLayers.reserve(MAP_LAYER_COUNT - 1);
		}

		MapObjs::~MapObjs(void){}

		bool MapObjs::Init() 
		{
			m_mapObjNames.clear();
			m_vectorLayers.clear();
			ObjLayer layer;
			layer.m_ObjectCount = 0;
			int i;
			for (i=0;i<MAP_LAYER_COUNT - 1;++i)
			{
				m_vectorLayers.push_back(layer);
			}
			return true; 
		}

		XOStream& MapObjs::marshal(XOStream &os) const
		{
			//文件名表
			ObjNameMap::const_iterator myIterator = m_mapObjNames.begin();
			int num = static_cast<int>(m_mapObjNames.size());
			os<<num;
			for ( ; myIterator != m_mapObjNames.end(); ++myIterator)
			{
				os<<myIterator->first;
				std::wstring name = myIterator->second;
				std::transform(name.begin(), name.end(), name.begin(), ::towlower);
				os<<name;
			}

			//物件列表
			unsigned char layerCount;
			layerCount = static_cast<unsigned char>(m_vectorLayers.size());
			ObjInfoVector::const_iterator oIt, oIe;
			int i;
			for (i=0;i<layerCount;++i)
			{
				const ObjInfoVector &vetorObjs = m_vectorLayers[i].vectorObjs;
				oIt = vetorObjs.begin();
				oIe = vetorObjs.end();
				os<<m_vectorLayers[i].m_ObjectCount;
				for (; oIt != oIe; ++oIt)
				{
					if (!oIt->m_bDeleted)
					{
						os<<oIt->m_MapKey;
						os<<oIt->m_Rect;
						if ((m_nSign & SIGN_NO_POS) == 0)
						{
							os<<oIt->m_Position;
						}
						if ((m_nSign & SIGN_NO_COLOR) == 0)
						{
							os<<oIt->m_AttachColor.data;
						}
					}
				}
			}
			return os;
		}
		const XIStream& MapObjs::unmarshal(const XIStream &os, unsigned int version)
		{
			m_mapObjNames.clear();
			m_vectorLayers.clear();
			ObjLayer layer;
			unsigned int num,i;
			ObjMapKey key;
			sObjInfo tmpInfo;
			unsigned char layerCount;
			std::map<std::wstring, ObjMapKey> tmpMap;
			std::map<ObjMapKey, ObjMapKey> key2key;
			std::map<std::wstring, ObjMapKey>::iterator tmpMapIter;
			std::map<ObjMapKey, ObjMapKey>::iterator key2keyIter;
			bool isEffect = false;
			bool needDelete = false;
			//文件名表
			os >> num;
			for (i=0;i<num;++i)
			{
				os >> key;
				os >> m_mapObjNames[key];
				if (version <= PMap::MAP_OLD_MOUNT)
				{
					//删除L"/bin/res"
					std::wstring& url = m_mapObjNames[key];
					std::transform(url.begin(), url.end(), url.begin(), ::towlower);
					if (i == 0)
					{
						if (url.substr(0, 4) == L"/bin")
							needDelete = true;
					}
					if (needDelete)
					{
						url = url.substr(11);
					}
				}
			}

			//物件列表
			layerCount = MAP_LAYER_COUNT-1;
			int j;
			for (j=0;j<layerCount;++j)
			{
				m_vectorLayers.push_back(layer);
				if (version <= PMap::MAP_OLD_LAYER_COUNT_AND_NO_MASK)
				{
					if (version <= PMap::MAP_OLD_LAYER_COUNT)
					{
						if (j == 1 || j == 5)
						{
							continue;//加了俩层
						}
					}
					if (j == 6 || j == 7)
					{
						continue;//又加了俩
					}
				}
				os >> num;
				m_vectorLayers[j].m_ObjectCount = num;
				ObjInfoVector &vetorObjs = m_vectorLayers[j].vectorObjs;
				for (i=0;i<num;++i)
				{

					os >> tmpInfo.m_MapKey;
					if (isEffect)
					{
						key2keyIter = key2key.find(tmpInfo.m_MapKey);
						if (key2keyIter != key2key.end())
						{
							tmpInfo.m_MapKey = key2keyIter->second;
						}
					}
					os >> tmpInfo.m_Rect;
					if ((m_nSign & SIGN_NO_POS) == 0)
					{
						os >> tmpInfo.m_Position;
					}
					if ((m_nSign & SIGN_NO_COLOR) == 0)
					{
						os >> tmpInfo.m_AttachColor.data;
					}
					tmpInfo.m_ObjectId = i;
					vetorObjs.push_back(tmpInfo);
				}
			}

			return os;
		}

		const std::wstring& MapObjs::GetObjFileName(unsigned char layerID, ObjectId objID) const
		{
			if ( layerID >= GetLayerCount())
				throw GetObjFileNameException();
			if ( objID >= GetObjCount(layerID) )
				throw GetObjFileNameException();
			ObjMapKey mapKey;
			mapKey = m_vectorLayers[layerID].vectorObjs[objID].m_MapKey;
			return GetObjFileName(mapKey);
		}

		const std::wstring& MapObjs::GetObjFileName(ObjMapKey key) const
		{
			ObjNameMap::const_iterator iter = m_mapObjNames.find(key);
			if (iter==m_mapObjNames.end()) 
			{
				throw GetObjFileNameException();
			}
			return iter->second;
		}

		ObjectId MapObjs::AddObj(int layerID, ObjMapKey key, const CRECT& rect, const CPOINT& pos, unsigned int attachColor)
		{
			sObjInfo tmpInfo;
			tmpInfo.m_MapKey = key;
			tmpInfo.m_AttachColor.data = attachColor;
			tmpInfo.m_Rect = rect;
			tmpInfo.m_Position = pos;
			tmpInfo.m_ObjectId = static_cast<ObjectId>(m_vectorLayers[layerID].vectorObjs.size());
			m_vectorLayers[layerID].vectorObjs.push_back(tmpInfo);
			m_vectorLayers[layerID].m_ObjectCount++;
			return tmpInfo.m_ObjectId;
		}

		void MapObjs::DeleteObj(int layerID, ObjectId objID)
		{
			if (m_vectorLayers[layerID].vectorObjs[objID].m_bDeleted)
				return;
			m_vectorLayers[layerID].vectorObjs[objID].m_bDeleted = true;
			m_vectorLayers[layerID].m_ObjectCount--;
		}
		void MapObjs::ReAddObj(int layerId, ObjectId objID)
		{
			if (!m_vectorLayers[layerId].vectorObjs[objID].m_bDeleted)
				return;
			m_vectorLayers[layerId].vectorObjs[objID].m_bDeleted = false;
			m_vectorLayers[layerId].m_ObjectCount++;
		}

		void MapObjs::DestoryLastObj(int layerID)
		{
			m_vectorLayers[layerID].vectorObjs.pop_back();
			m_vectorLayers[layerID].m_ObjectCount--;
		}
	}
}
