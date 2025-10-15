#ifndef __Nuclear_SUBMAP_MAPOBJS_H__
#define __Nuclear_SUBMAP_MAPOBJS_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxptypes.h"
#include "../common/nuxmarshal.h"

namespace Nuclear
{
	class RegionMap;
	namespace SubMap
	{
		class GetObjFileNameException {};
		typedef unsigned int ObjMapKey;
		typedef unsigned int ObjID;
		const ObjMapKey INVALID_OBJ_MAP_KEY = 0;
		struct ObjInfo
		{
			ObjMapKey mapKey;
			ObjID objID;
			NuclearColor attachColor;
			NuclearRect rect;	//这个框是缩放过的
			NuclearPoint pos;
			NuclearFPoint scale;
			float fRotationRadian;
			bool deleted;
			ObjInfo() : attachColor(0xFFFFFFFF), scale(1.0f, 1.0f), fRotationRadian(0.0f), deleted(false) {};
		};

		typedef std::vector<ObjInfo> ObjInfoVector;

		struct ObjLayer
		{
			ObjInfoVector vectorObjs;
			unsigned int ObjCount;
			ObjLayer():ObjCount(0) {
				vectorObjs.reserve(100);
			};
		};

		typedef std::map<ObjMapKey, std::wstring> ObjNameMap;
		typedef std::vector<ObjLayer> ObjLayerVector;

		class NuclearMapObjs
		{
		public:

			static const unsigned int SIGN_NO_POS = 1 << 0;
			static const unsigned int SIGN_NO_SCALE = 1 << 1;
			static const unsigned int SIGN_NO_ROTATION = 1 << 2;
		
			ObjNameMap m_mapObjNames;
			ObjLayerVector m_vectorLayers;
			unsigned int m_nSign;

		public:
			NuclearMapObjs(void);
			~NuclearMapObjs(void);
			virtual bool Init();
			void SetNoPos() { m_nSign |= SIGN_NO_POS; }
			void SetNoScale() { m_nSign |= SIGN_NO_SCALE; }
			void SetNoRotation() { m_nSign |= SIGN_NO_ROTATION; }

			///////////////////////////////输入输出///////////////////////////////
			NuclearOStream& marshal(NuclearOStream &os) const;
			const INuclearStream& unmarshal(const INuclearStream &os, unsigned int version);

			const std::wstring& GetObjFileName(unsigned char layerID, ObjID objID) const;
			const std::wstring& GetObjFileName(ObjMapKey key) const;

			const ObjNameMap& GetConstObjFileNameMap() const { return m_mapObjNames; }
			ObjNameMap& GetObjFileNameMap()	{ return m_mapObjNames; }
			void SetObjFileName(ObjMapKey key, const std::wstring& name) { m_mapObjNames[key] = name; }
			ObjInfo& GetObjInfoByID(unsigned char layerID, ObjID objID)	{ return m_vectorLayers[layerID].vectorObjs[objID];	}
			const ObjInfo& GetObjInfoByID (unsigned char layerID, ObjID objID) const { return m_vectorLayers[layerID].vectorObjs[objID];	}
			const ObjInfoVector& GetObjInfos(unsigned char layerID) const { return m_vectorLayers[layerID].vectorObjs; }
			ObjInfoVector& GetObjInfos(unsigned char layerID) {	return m_vectorLayers[layerID].vectorObjs; }
			unsigned int GetObjCount(unsigned char layerID)	const { return m_vectorLayers[layerID].ObjCount; }
			size_t GetObjSize(unsigned char layerID) { return m_vectorLayers[layerID].vectorObjs.size(); }
			const unsigned char GetLayerCount() const { return static_cast<unsigned char>(m_vectorLayers.size()); }
			ObjID AddObj(int layerID, ObjMapKey key, const NuclearRect& rect, const NuclearPoint& pos, unsigned int attachColor);
			void DeleteObj(int layerID, ObjID objID);
			void ReAddObj(int layerId, ObjID objID);
			void DestoryLastObj(int layerID);
		};
	}

}

#endif