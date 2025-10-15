#ifndef __Nuclear_REGIONMAP_PPATHMAP_H
#define __Nuclear_REGIONMAP_PPATHMAP_H

#if _MSC_VER > 1000
#pragma once
#endif

#ifndef TILE_TYPE
#define TILE_TYPE (L".png")
#endif

#include "pground.h"
#include "..\common\fileiomanager.h"

namespace Nuclear
{
	enum TILE_SIZE_TYPE
	{
		TST_SMALL = 0,
		TST_MIDDLE,
		TST_BIG
	};

	enum TILE_SIZE_TYPE_COUNT
	{
		TST_COUNT = TST_BIG + 1,
	};

	namespace SubMap
	{
		class PPathMap
		{
		public:
			PPathMap(CFileIOManager* pFileIOMan);
			virtual ~PPathMap(void);
			bool Load();
			bool Save() const;
				
			std::wstring GetTileFileName(SubMap::TileType tileData) const;
			std::wstring GetTileFileName(TILE_SIZE_TYPE type, SubMap::BiggerTileType tileData) const;
			void Insert(TILE_SIZE_TYPE type, int key, const std::wstring &value);

			//Êý¾Ý
		private:
			bool m_bChanged;
			std::map<int, std::wstring> m_mapPaths[TST_COUNT];
			CFileIOManager* m_pFileIOMan;
		};
	}
}

#endif