#ifndef __Nuclear_REGIONMAP_PPATHMAP_H__
#define __Nuclear_REGIONMAP_PPATHMAP_H__

#if _MSC_VER > 1000
#pragma once
#endif

#ifndef TILE_TYPE
#define TILE_TYPE (L".png")
#endif

#include "nupground.h"
#include "../common/nufileiomanager.h"

namespace Nuclear
{
	enum eTILE_SIZE_TYPE
	{
		TST_SMALL = 0,
		TST_MIDDLE,
		TST_BIG
	};

	enum eTILE_SIZE_TYPE_COUNT
	{
		TST_COUNT = TST_BIG + 1,
	};

	namespace SubMap
	{
		class PPathMap
		{
		public:
			PPathMap(NuclearFileIOManager* pFileIOMan);
			virtual ~PPathMap(void);
			bool Load();
			bool Save() const;
				
			std::wstring GetTileFileName(SubMap::TileType tileData) const;
			std::wstring GetTileFileName(eTILE_SIZE_TYPE type, SubMap::uBiggerTileType tileData) const;
			void Insert(eTILE_SIZE_TYPE type, int key, const std::wstring &value);

		private:
			bool m_bChanged;
			std::map<int, std::wstring> m_Paths[TST_COUNT];
			NuclearFileIOManager* m_pFileIOMan;
		};
	}
}

#endif