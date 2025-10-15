#include "../common/nulog.h"
#include "../common/numapconst.h"
#include "numapobjs.h"
#include "nupmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		NuclearMapObjs::NuclearMapObjs(void) : m_nSign(0)
		{
			m_vectorLayers.reserve(XPPIC_ELEMENT_LAYER_COUNT);
		}

		NuclearMapObjs::~NuclearMapObjs(void){}

		bool NuclearMapObjs::Init() 
		{
			m_mapObjNames.clear();
			m_vectorLayers.clear();
			ObjLayer layer;
			layer.ObjCount = 0;
			int i;
			for (i=0;i<XPPIC_ELEMENT_LAYER_COUNT;++i)
			{
				m_vectorLayers.push_back(layer);
			}
			return true; 
		}

		NuclearOStream& NuclearMapObjs::marshal(NuclearOStream &os) const
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
				os<<m_vectorLayers[i].ObjCount;
				for (; oIt != oIe; ++oIt)
				{
					if (!oIt->deleted)
					{
						os<<oIt->mapKey;
						os<<oIt->rect;
						if ((m_nSign & SIGN_NO_POS) == 0)
						{
							os<<oIt->pos;
						}
						os<<oIt->attachColor.data;
						if ((m_nSign & SIGN_NO_SCALE) == 0)
						{
							os<<oIt->scale;
						}
						if ((m_nSign & SIGN_NO_ROTATION) == 0)
						{
							os<<oIt->fRotationRadian;
						}
					}
				}
			}
			return os;
		}
		const INuclearStream& NuclearMapObjs::unmarshal(const INuclearStream &os, unsigned int version)
		{
			m_mapObjNames.clear();
			m_vectorLayers.clear();
			ObjLayer layer;
			unsigned int num,i;
			ObjMapKey key;
			ObjInfo tmpInfo;
			unsigned char layerCount;
			std::map<std::wstring, ObjMapKey> tmpMap;
			std::map<ObjMapKey, ObjMapKey> key2key;
			std::map<std::wstring, ObjMapKey>::iterator tmpMapIter;
			std::map<ObjMapKey, ObjMapKey>::iterator key2keyIter;
			bool needDelete = false;
			//文件名表
			os >> num;
			for (i=0;i<num;++i)
			{
				os >> key;
				os >> m_mapObjNames[key];
				if (version <= NuclearMap::MAP_OLD_MOUNT)
				{
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
				if (version <= NuclearMap::MAP_MOUNT_ELEMENT)
				{
					std::wstring& url = m_mapObjNames[key];
					if (url.substr(0, 4) == L"/ele")
					{
						url = L"/map" + url;
					}
				}
			}

			//物件列表
			layerCount = XPPIC_ELEMENT_LAYER_COUNT;
			int j;
			for (j=0;j<layerCount;++j)
			{
				m_vectorLayers.push_back(layer);
				if (version <= NuclearMap::MAP_OLD_LAYER_COUNT_AND_NO_MASK)
				{
					if (version <= NuclearMap::MAP_OLD_LAYER_COUNT)
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
				if (version <= NuclearMap::MAP_NO_MID_2)
				{
					if (j == 9)
						continue;//又加了一层
				}
				os >> num;
				m_vectorLayers[j].ObjCount = num;
				ObjInfoVector &vetorObjs = m_vectorLayers[j].vectorObjs;
				for (i=0;i<num;++i)
				{

					os >> tmpInfo.mapKey;
					os >> tmpInfo.rect;
					if ((m_nSign & SIGN_NO_POS) == 0)
					{
						os >> tmpInfo.pos;
					}
					//虽然这个是Color 在版本46的时候只有特效没有color，
					//到了版本47，就所有的东西都有color了
					if ((m_nSign & SIGN_NO_ROTATION) == 0) //目前(版本47）只有特效有SIGN_NO_ROTATION标志
					{
						if (version > NuclearMap::MAP_NO_OBJ_ROUTE) os >> tmpInfo.attachColor.data;
					} else {
						os >> tmpInfo.attachColor.data;
					}
					if ((m_nSign & SIGN_NO_SCALE) == 0 && version > NuclearMap::MAP_NO_OBJ_SCALE)
					{
						os >> tmpInfo.scale;
					}
					if ((m_nSign & SIGN_NO_ROTATION) == 0 && version > NuclearMap::MAP_NO_OBJ_ROUTE)
					{
						os >> tmpInfo.fRotationRadian;
					}
					tmpInfo.objID = i;
					vetorObjs.push_back(tmpInfo);
				}
			}

			return os;
		}

		const std::wstring& NuclearMapObjs::GetObjFileName(unsigned char layerID, ObjID objID) const
		{
			if ( layerID >= GetLayerCount())
				throw GetObjFileNameException();
			if ( objID >= GetObjCount(layerID) )
				throw GetObjFileNameException();
			ObjMapKey mapKey;
			mapKey = m_vectorLayers[layerID].vectorObjs[objID].mapKey;
			return GetObjFileName(mapKey);
		}

		const std::wstring& NuclearMapObjs::GetObjFileName(ObjMapKey key) const
		{
			ObjNameMap::const_iterator iter = m_mapObjNames.find(key);
			if (iter==m_mapObjNames.end()) 
			{
				throw GetObjFileNameException();
			}
			return iter->second;
		}

		ObjID NuclearMapObjs::AddObj(int layerID, ObjMapKey key, const NuclearRect& rect, const NuclearPoint& pos, unsigned int attachColor)
		{
			ObjInfo tmpInfo;
			tmpInfo.mapKey = key;
			tmpInfo.attachColor.data = attachColor;
			tmpInfo.rect = rect;
			tmpInfo.pos = pos;
			tmpInfo.objID = static_cast<ObjID>(m_vectorLayers[layerID].vectorObjs.size());
			m_vectorLayers[layerID].vectorObjs.push_back(tmpInfo);
			m_vectorLayers[layerID].ObjCount++;
			return tmpInfo.objID;
		}

		void NuclearMapObjs::DeleteObj(int layerID, ObjID objID)
		{
			if (m_vectorLayers[layerID].vectorObjs[objID].deleted)
				return;
			m_vectorLayers[layerID].vectorObjs[objID].deleted = true;
			m_vectorLayers[layerID].ObjCount--;
		}
		void NuclearMapObjs::ReAddObj(int layerId, ObjID objID)
		{
			if (!m_vectorLayers[layerId].vectorObjs[objID].deleted)
				return;
			m_vectorLayers[layerId].vectorObjs[objID].deleted = false;
			m_vectorLayers[layerId].ObjCount++;
		}

		void NuclearMapObjs::DestoryLastObj(int layerID)
		{
			m_vectorLayers[layerID].vectorObjs.pop_back();
			m_vectorLayers[layerID].ObjCount--;
		}
	}
}
