#include "../common/nulog.h"
#include "nupground.h"
#include "nupmap.h"

namespace Nuclear
{
	union TILE_ORDER
	{
		unsigned char data;
		struct {
			unsigned char _1: 2;
			unsigned char _2: 2;
			unsigned char _3: 2;
			unsigned char _4: 2;
		};
	};

	namespace SubMap
	{
		const uBiggerTileType NuclearGround::BIGGER_TILE_NULL = 0;
		NuclearGround::NuclearGround(void)
		{
			int layer;
			for (layer=0;layer<XPTILES_SMALL_LAYER_COUNT;++layer)
			{
				m_pTiles[layer] = NULL;
			}
			for (layer=0;layer<XPTILES_MID_LAYER_COUNT;++layer)
			{
				m_pMidTiles[layer] = NULL;
			}
			m_pBigTiles = NULL;
			m_pGroundColor = NULL;
			m_pWaters = NULL;
			for (int i=0;i<WATER_EFFECT_COUNT;++i)
			{
				m_waterNames[i] = L"";
			}
			for (layer=0;layer<XPTILES_ALL_LAYER_COUNT;++layer)
			{
				m_TileOrder[layer] = layer;
			}
		}

		NuclearGround::~NuclearGround(void)
		{
			freeMaps();
		}

		NuclearOStream& NuclearGround::marshal(NuclearOStream &os) const
		{
			//水的信息，不压缩了
			for (int i=0;i<WATER_EFFECT_COUNT;++i)
			{
				os << m_waterNames[i];
			}
			int size = GetWaterSize();
			os.push_byte(m_pWaters, size);
			
			/////////////////////水End
			size = GetColorSize();
			os.push_byte(m_pGroundColor, size);
			//地表信息
			
			int layer;
			//地表顺序
			TILE_ORDER order;
			order._1 = m_TileOrder[0];
			order._2 = m_TileOrder[1];
			order._3 = m_TileOrder[2];
			order._4 = m_TileOrder[3];
			os << order.data;
			//小地表
			size = GetSmlTileSize();
			for (layer=0;layer<XPTILES_SMALL_LAYER_COUNT;++layer)
			{
				os.push_byte(m_pTiles[layer], size);
			}
			//中地表
			size = GetMidTileSize();
			for (layer=0;layer<XPTILES_MID_LAYER_COUNT;++layer)
			{
				os.push_byte(m_pMidTiles[layer], size);
			}
			//大地表
			size = GetBigTileSize();
			os.push_byte(m_pBigTiles, size);
			return os;
		}

		const INuclearStream& NuclearGround::unmarshal(const INuclearStream &os, unsigned int version, int colorGridWidth, int colorGridHeight)
		{
			//水的信息
			for (int i=0;i<WATER_EFFECT_COUNT;++i)
			{
				os >> m_waterNames[i];
			}
			int size = GetWaterSize();
			//不压缩了
			os.pop_byte(m_pWaters, size);
			/////////////////////水End
			if (version <= NuclearMap::MAP_OLD_GROUND_COLOR)
			{
				int smlWidth = GetWidthForSmlTiles();
				int smlHeight = GetHeightForSmlTiles();
				int oldColorWidth = (smlWidth + colorGridWidth - 1) / colorGridWidth;
				int oldColorHeight = (smlHeight + colorGridHeight - 1) / colorGridHeight;
				size = oldColorWidth * oldColorHeight * sizeof(unsigned int);
				unsigned int* pGroundColor = static_cast<unsigned int*>(XAlloc(size));
				unsigned int* pOldGroundColor = pGroundColor;
				unsigned int* pNewGroundColor = m_pGroundColor;
				os.pop_byte(pGroundColor, size);
				for (int oldy = 0; oldy < oldColorHeight; ++oldy)
				{
					for (int oldx = 0; oldx < oldColorWidth; ++oldx, ++pGroundColor)
					{
						if (*pGroundColor == 0xFFFFFFFF)
							continue;
						int newy = oldy * colorGridHeight;
						int newylimit = newy + colorGridHeight;
						newylimit = std::min(newylimit, smlHeight);
						int newx = oldx * colorGridWidth;
						int newxlimit = newx + colorGridWidth;
						newxlimit = std::min(newxlimit, smlWidth);
						//都可以等于
						for (; newy <= newylimit ; ++newy )
						{
							newx = oldx * colorGridWidth;
							pNewGroundColor = m_pGroundColor + newy * (smlWidth + 1) + newx;
							for (; newx <= newxlimit; ++ newx, ++pNewGroundColor)
							{
								(*pNewGroundColor) = (*pGroundColor);
							}
						}
					}
				}
				XFree(pOldGroundColor);
			} else {
				size = GetColorSize();
				os.pop_byte(m_pGroundColor, size);
			}
			//地表顺序
			TILE_ORDER order;
			os >> order.data;
			m_TileOrder[0] = order._1;
			m_TileOrder[1] = order._2;
			m_TileOrder[2] = order._3;
			m_TileOrder[3] = order._4;
			int layer;
			//小地表
			size = GetSmlTileSize();
			for (layer=0;layer<XPTILES_SMALL_LAYER_COUNT;++layer)
			{
				if (m_pTiles[layer] == NULL)
				{
					XPLOG_ERROR(L"m_pTiles[%d] == NULL\n", layer);
					throw NuclearMarshal::Exception();
				}
				os.pop_byte(m_pTiles[layer], size);
			}
			//中地表
			size = GetMidTileSize();
			for (layer=0;layer<XPTILES_MID_LAYER_COUNT;++layer)
			{
				if (m_pMidTiles[layer] == NULL)
				{
					XPLOG_ERROR(L"m_pMidTiles[%d] == NULL\n", layer);
					throw NuclearMarshal::Exception();
				}
				os.pop_byte(m_pMidTiles[layer], size);
			}
			//大地表
			size = GetBigTileSize();
			if (m_pBigTiles == NULL)
			{
				XPLOG_ERROR(L"m_pBigTiles == NULL\n");
				throw NuclearMarshal::Exception();
			}
			os.pop_byte(m_pBigTiles, size);
			
			return os;
		}

		bool NuclearGround::initial()
		{
			freeMaps();
			return allocMaps();
		}

		bool NuclearGround::Init(int width, int height)
		{
			if (width == 0 || height == 0)
				return false;
			if (width % TILE_WIDTH != 0)
				return false;
			if (height % TILE_HEIGHT != 0)
				return false;
			m_nWidth = width;
			m_nHeight = height;
			return initial();
		}

		void NuclearGround::freeMaps()
		{
			int layer;
			for (layer=0;layer<XPTILES_SMALL_LAYER_COUNT;++layer)
			{
				if (m_pTiles[layer] != NULL)
					XFree(m_pTiles[layer]);
			}
			for (layer=0;layer<XPTILES_MID_LAYER_COUNT;++layer)
			{
				if (m_pMidTiles[layer] != NULL)
					XFree(m_pMidTiles[layer]);
			}
			if (m_pBigTiles != NULL)
				XFree(m_pBigTiles);

			if (m_pGroundColor != NULL)
				XFree(m_pGroundColor);
			if (m_pWaters != NULL)
				XFree(m_pWaters);
		}

		bool NuclearGround::allocMaps()
		{
			int layer;
			int size = GetSmlTileSize();
			for (layer=0;layer<XPTILES_SMALL_LAYER_COUNT;++layer)
			{
				m_pTiles[layer] = static_cast<PtrTileType>(XAlloc(size));
				if (m_pTiles[layer] == NULL)
					return false;
				memset(m_pTiles[layer], TILE_NULL, size);
			}
			size = GetMidTileSize();
			for (layer=0;layer<XPTILES_MID_LAYER_COUNT;++layer)
			{
				m_pMidTiles[layer] = static_cast<PtrBiggerTileType>(XAlloc(size));
				if (m_pMidTiles[layer] == NULL)
					return false;
				memset(m_pMidTiles[layer], BIGGER_TILE_NULL.data, size);
			}
			size = GetBigTileSize();
			m_pBigTiles = static_cast<PtrBiggerTileType>(XAlloc(size));
			if (m_pBigTiles == NULL)
				return false;
			memset(m_pBigTiles, BIGGER_TILE_NULL.data, size);
			size = GetColorSize();
			m_pGroundColor = static_cast<unsigned int*>(XAlloc(size));
			if (m_pGroundColor == NULL)
				return false;
			memset(m_pGroundColor, 0xFF, size);
			size = GetWaterSize();
			m_pWaters = static_cast<uWaterTileType*>(XAlloc(size));
			if (m_pWaters == NULL)
				return false;
			memset(m_pWaters, 0, size);
			return true;
		}

		NuclearGround& NuclearGround::operator= (const NuclearGround &x)
		{
			if (&x == this)
				return *this;
			m_waterNames[0] = x.m_waterNames[0];
			m_waterNames[1] = x.m_waterNames[1];
			m_waterNames[2] = x.m_waterNames[2];
			m_nWidth = x.m_nWidth;
			m_nHeight = x.m_nHeight;
			memcpy(m_TileOrder, x.m_TileOrder, XPTILES_ALL_LAYER_COUNT);
			int layer;
			int size = GetSmlTileSize();
			freeMaps();
			for (layer=0;layer<XPTILES_SMALL_LAYER_COUNT;++layer)
			{
				m_pTiles[layer] = static_cast<PtrTileType>(XAlloc(size));
				memcpy(m_pTiles[layer], x.m_pTiles[layer], size);
			}
			size = GetMidTileSize();
			for (layer=0;layer<XPTILES_MID_LAYER_COUNT;++layer)
			{
				m_pMidTiles[layer] = static_cast<PtrBiggerTileType>(XAlloc(size));
				memcpy(m_pMidTiles[layer], x.m_pMidTiles[layer], size);
			}
			size = GetBigTileSize();
			m_pBigTiles = static_cast<PtrBiggerTileType>(XAlloc(size));
			memcpy(m_pBigTiles, x.m_pBigTiles, size);
			size = GetColorSize();
			m_pGroundColor = static_cast<unsigned int*>(XAlloc(size));
			memcpy(m_pGroundColor, x.m_pGroundColor, size);
			size = GetWaterSize();
			m_pWaters = static_cast<uWaterTileType*>(XAlloc(size));
			memcpy(m_pWaters, x.m_pWaters, size);
			return *this;
		}
	}
}
