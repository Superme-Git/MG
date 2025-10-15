#ifndef __Nuclear_SUBMAP_MAPOBJS_H
#define __Nuclear_SUBMAP_MAPOBJS_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"
#include "..\common\xmarshal.h"

class CObjectResourceDlg;
class CAllObjectResourceDlg;
class CActionList;
class CReplaceObjs;

namespace Nuclear
{
	class RegionMap;
	namespace SubMap
	{

		class GetObjFileNameException {};
		typedef unsigned int ObjMapKey;
		typedef unsigned int ObjectId;
		const ObjMapKey INVALID_OBJ_MAP_KEY = 0;
		struct sObjInfo
		{
			ObjMapKey m_MapKey;
			ObjectId m_ObjectId;
			XPCOLOR m_AttachColor;
			CRECT m_Rect;
			CPOINT m_Position;
			bool m_bDeleted;
			sObjInfo() : m_bDeleted(false), m_AttachColor(0xFFFFFFFF) {};
		};

		typedef std::vector<sObjInfo> ObjInfoVector;

		struct ObjLayer
		{
			ObjInfoVector vectorObjs;
			unsigned int m_ObjectCount;
			ObjLayer():m_ObjectCount(0) {
				vectorObjs.reserve(100);
			};
		};

		typedef std::map<ObjMapKey, std::wstring> ObjNameMap;
		typedef std::vector<ObjLayer> ObjLayerVector;

		class MapObjs
		{
		public:
			friend class RegionMap;
			friend class ::CObjectResourceDlg;
			friend class ::CActionList;
			friend class ::CAllObjectResourceDlg;
			friend class ::CReplaceObjs;

		private:
			static const unsigned int SIGN_NO_COLOR = 1;
			static const unsigned int SIGN_NO_POS = 2;
			//成员数据们
		private:
			ObjNameMap m_mapObjNames;
			ObjLayerVector m_vectorLayers;
			unsigned int m_nSign;

			//成员方法们
		private:

		public:
			///////////////////////////////初始化、构造与析构
			MapObjs(void);
			~MapObjs(void);
			virtual bool Init();
			void SetNoColor() { m_nSign |= SIGN_NO_COLOR; }
			void SetNoPos() { m_nSign |= SIGN_NO_POS; }

			///////////////////////////////输入输出
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////公共方法们
			const std::wstring& GetObjFileName(unsigned char layerID, ObjectId objID) const;
			const std::wstring& GetObjFileName(ObjMapKey key) const;

			const ObjNameMap& GetConstObjFileNameMap() const { return m_mapObjNames; }
			ObjNameMap& GetObjFileNameMap()	{ return m_mapObjNames; }
			void SetObjFileName(ObjMapKey key, const std::wstring& name) 
			{ 
				m_mapObjNames[key] = name; 
			}
			sObjInfo& GetObjInfoByID(unsigned char layerID, ObjectId objID)	{ return m_vectorLayers[layerID].vectorObjs[objID];	}
			const sObjInfo& GetObjInfoByID (unsigned char layerID, ObjectId objID) const { return m_vectorLayers[layerID].vectorObjs[objID];	}
			const ObjInfoVector& GetObjInfos(unsigned char layerID) const { return m_vectorLayers[layerID].vectorObjs; }
			ObjInfoVector& GetObjInfos(unsigned char layerID) {	return m_vectorLayers[layerID].vectorObjs; }
			unsigned int GetObjCount(unsigned char layerID)	const { return m_vectorLayers[layerID].m_ObjectCount; }
			size_t GetObjSize(unsigned char layerID) { return m_vectorLayers[layerID].vectorObjs.size(); }
			const unsigned char GetLayerCount() const { return static_cast<unsigned char>(m_vectorLayers.size()); }
			ObjectId AddObj(int layerID, ObjMapKey key, const CRECT& rect, const CPOINT& pos, unsigned int attachColor);
			void DeleteObj(int layerID, ObjectId objID);
			void ReAddObj(int layerId, ObjectId objID);
			void DestoryLastObj(int layerID);
		};

	}
}

#endif