#ifndef __Nuclear_REGIONMAP_PGROUND_H__
#define __Nuclear_REGIONMAP_PGROUND_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxptypes.h"
#include "../common/nuxmarshal.h"
#include "../common/numapconst.h"

namespace Nuclear
{
	namespace SubMap
	{
		typedef unsigned short TileType;
		typedef TileType* PtrTileType;
		typedef std::vector<PtrTileType> PtrTileTypeList;

		enum eGroundLayers
		{
			GL_BIG = 0,
			GL_MID1,
			GL_MID2,
			GL_SMALL,
		};

		union uBiggerTileType
		{
			unsigned short data;
			struct 
			{
				unsigned short tileID : 5;
				unsigned short tileType : 11;
			};
			uBiggerTileType(unsigned short _d = 0) : data(_d) {}
		};
		typedef uBiggerTileType* PtrBiggerTileType;
		union uWaterTileType
		{
			unsigned char data;
			struct 
			{
				unsigned char tileType : 4;
				unsigned char tileRes : 4;
			};
		};

		class NuclearGround
		{
		public:
			int static const TILE_HEIGHT = 64;
			int static const TILE_WIDTH = 128;
			int static const TILE_MID_HEIGHT = 256;
			int static const TILE_MID_WIDTH = 256;
			int static const TILE_BIG_HEIGHT = 512;
			int static const TILE_BIG_WIDTH = 512;
			int static const WATER_WIDTH = 128;
			int static const WATER_HEIGHT = 128;
			int static const WATER_EFFECT_COUNT = 3;
			
			TileType static const TILE_NULL = 0;
			uBiggerTileType static const BIGGER_TILE_NULL;
			enum
			{	//Tile的种类			  ABCD	 本类型  	  空白Alpha		   A--------B
				TILE_EMPTY = 0,				//0000	  			    ABCD	       |        |
				TILE_LEFT_BOTTOM_S = 1,		//0001	  D	    	    ABC			   D--------C
				TILE_RIGHT_BOTTOM_S = 2,	//0010    C			    ABD
				TILE_BOTTOM = 3,			//0011	  CD            AB
				TILE_RIGHT_TOP_S = 4,		//0100	  B			    ACD
				TILE_SLASH = 5,				//0101	  BD		    AC
				TILE_RIGHT = 6,				//0110	  BC		    AD
				TILE_RIGHT_BOTTOM_L = 7,	//0111    BCD			A
				TILE_LEFT_TOP_S = 8,		//1000	  A				BCD
				TILE_LEFT = 9,				//1001	  AD			BC
				TILE_BACKSLASH = 10,		//1010	  AC			BD
				TILE_LEFT_BOTTOM_L = 11,	//1011	  ACD			B
				TILE_TOP = 12,				//1100	  AB			CD
				TILE_LEFT_TOP_L = 13,		//1101	  ABD			C
				TILE_RIGHT_TOP_L = 14,		//1110	  ABC			D
				TILE_SOLID = 15				//1111	  ABCD
			};
			enum
			{	//Masks....
				TILE_MSK_GROUND_TYPE = 0xFF00,
				TILE_MSK_TILE_TYPE = 0x00F0,
				TILE_MSK_TILE_NUMBER = 0x000F
			};

			enum
			{	//Adjust....
				TILE_ADJ_GROUND_TYPE = 8,
				TILE_ADJ_TILE_TYPE = 4,
			};

		public:
			NuclearGround(void);
			virtual ~NuclearGround(void);

			virtual bool Init(int width, int height);

			NuclearGround& operator= (const NuclearGround &x);

			void SetColorNULL() { m_pGroundColor = NULL; }

			///////////////////////////////输入输出///////////////////////////////
			NuclearOStream& marshal(NuclearOStream &os) const;
			const INuclearStream& unmarshal(const INuclearStream &os, unsigned int version, int colorWidth, int colorHeight);

			///////////////////////////////Tile相关///////////////////////////////
			const std::wstring& GetWaterName(int i) const { return m_waterNames[i]; }
			std::wstring GetWaterName(int i) { return m_waterNames[i]; }
			void SetWaterName(int i, const std::wstring& name) { m_waterNames[i] = name; }
			int GetSmlTilePos(int x, int y) const
			{
				if (!IsPointInMapForSmlTiles(x, y))
					return -1;
				return y * GetWidthForSmlTiles() + x;
			}
			int GetMidTilePos(int x, int y) const
			{
				if (!IsPointInMapForMidTiles(x, y))
					return -1;
				return y * GetWidthForMidTiles() + x;
			}
			int GetBigTilePos(int x, int y) const
			{
				if (!IsPointInMapForBigTiles(x, y))
					return -1;
				return y * GetWidthForBigTiles() + x;
			}
			int GetColorPos(int x, int y) const
			{
				if (!IsPointInMapForColor(x, y))
					return -1;
				return y * GetWidthForColor() + x;
			}
			int GetWaterPos(int x, int y) const
			{
				if (!IsPointInMapForWater(x, y))
					return -1;
				return y * GetWidthForWater() + x;
			}
			int GetSmlTileSize() const { return GetSmlTileCount() * sizeof(TileType); }
			int GetMidTileSize() const { return GetMidTileCount() * sizeof(uBiggerTileType); }
			int GetBigTileSize() const { return GetBigTileCount() * sizeof(uBiggerTileType); }
			int GetSmlTileCount() const { return GetWidthForSmlTiles() * GetHeightForSmlTiles(); }
			int GetMidTileCount() const { return GetWidthForMidTiles() * GetHeightForMidTiles(); }
			int GetBigTileCount() const { return GetWidthForBigTiles() * GetHeightForBigTiles(); }
			int GetColorSize() const { return GetColorCount() * sizeof(unsigned int); }
			int GetColorCount() const { return GetWidthForColor() * GetHeightForColor(); }
			int GetWaterSize() const { return GetWaterCount(); }
			int GetWaterCount() const { return GetWidthForWater() * GetHeightForWater(); }
			static TileType SetGroundType(TileType tile, unsigned short groundType)
			{
				tile &= ~TILE_MSK_GROUND_TYPE;
				tile |= groundType << TILE_ADJ_GROUND_TYPE;
				return tile;
			}

			static unsigned char GetGroundType(TileType tile) { return (tile & TILE_MSK_GROUND_TYPE) >> TILE_ADJ_GROUND_TYPE; }

			static TileType SetTileType(TileType tile, unsigned char type){	tile &= ~TILE_MSK_TILE_TYPE; tile |= type << TILE_ADJ_TILE_TYPE; return tile; }
			static unsigned char GetTileType(TileType tile) { return (tile & TILE_MSK_TILE_TYPE) >> TILE_ADJ_TILE_TYPE; }

			static TileType SetTileNumber(TileType tile, unsigned char number) { tile &= ~TILE_MSK_TILE_NUMBER; tile |= number; return tile; }
			static unsigned char GetTileNumber(TileType tile) { return tile & TILE_MSK_TILE_NUMBER; }

			bool IsPointInMapForSmlTiles(int x, int y) const //以小Tile为单位，判断Tile是否在地图里面
			{
				if (x >= GetWidthForSmlTiles() || y >= GetHeightForSmlTiles()
					|| x < 0 || y < 0)
					return false;
				else
					return true;
			}

			bool IsPointInMapForMidTiles(int x, int y) const //以中Tile为单位，判断Tile是否在地图里面
			{
				if (x >= GetWidthForMidTiles() || y >= GetHeightForMidTiles()
					|| x < 0 || y < 0)
					return false;
				else
					return true;
			}

			bool IsPointInMapForBigTiles(int x, int y) const //以大Tile为单位，判断Tile是否在地图里面
			{
				if (x >= GetWidthForBigTiles() || y >= GetHeightForBigTiles()
					|| x < 0 || y < 0)
					return false;
				else
					return true;
			}

			bool IsPointInMapForColor(int x, int y) const //以颜色格子为单位，判断点是否在地图里面
			{
				if (x >= GetWidthForColor() || y >= GetHeightForColor()
					|| x < 0 || y < 0)
					return false;
				else
					return true;
			}

			bool IsPointInMapForWater(int x, int y) const //以水面特效的格子为单位，判断点是否在地图里面
			{
				if (x >= GetWidthForWater() || y >= GetHeightForWater()
					|| x < 0 || y < 0)
					return false;
				else
					return true;
			}

			unsigned int *GetColorData() const { return m_pGroundColor; }
			uWaterTileType *GetWaterData() const { return m_pWaters; }

			TileType *GetSmlTileData(int layer) const 
			{ 
				if (layer<0 || layer >= XPTILES_SMALL_LAYER_COUNT)
					return NULL;
				return m_pTiles[layer]; 
			}
			TileType GetSmlTileData(int layer, int x, int y) const
			{
				if (!IsPointInMapForSmlTiles(x,y))
					return TILE_NULL;
				if (layer<0 || layer >= XPTILES_SMALL_LAYER_COUNT)
					return TILE_NULL;
				return m_pTiles[layer][GetSmlTilePos(x, y)];
			}

			bool SetSmlTileData(int layer, int x, int y, TileType data)
			{
				if (!IsPointInMapForSmlTiles(x,y))
					return false;
				if (layer<0 || layer >= XPTILES_SMALL_LAYER_COUNT)
					return false;
				m_pTiles[layer][GetSmlTilePos(x, y)] = data;
				return true;
			}

			bool SetSmlTileData(int layer, int pos, TileType data)
			{
				if (layer<0 || layer >= XPTILES_SMALL_LAYER_COUNT)
					return false;
				if (pos < 0 || pos >= GetSmlTileCount())
					return false;
				m_pTiles[layer][pos] = data;
				return true;
			}

			uBiggerTileType *GetMidTileData(int layer) const 
			{ 
				if (layer<0 || layer >= XPTILES_MID_LAYER_COUNT)
					return NULL;
				return m_pMidTiles[layer]; 
			}
			uBiggerTileType GetMidTileData(int layer, int x, int y) const
			{
				if (!IsPointInMapForMidTiles(x,y))
					return BIGGER_TILE_NULL;
				if (layer<0 || layer >= XPTILES_MID_LAYER_COUNT)
					return BIGGER_TILE_NULL;
				return m_pMidTiles[layer][GetMidTilePos(x, y)];
			}

			bool SetMidTileData(int layer, int x, int y, uBiggerTileType data)
			{
				if (!IsPointInMapForMidTiles(x,y))
					return false;
				if (layer<0 || layer >= XPTILES_MID_LAYER_COUNT)
					return false;
				m_pMidTiles[layer][GetMidTilePos(x, y)] = data;
				return true;
			}

			bool SetMidTileData(int layer, int pos, uBiggerTileType data)
			{
				if (layer<0 || layer >= XPTILES_MID_LAYER_COUNT)
					return false;
				if (pos < 0 || pos >= GetMidTileCount())
					return false;
				m_pMidTiles[layer][pos] = data;
				return true;
			}

			uBiggerTileType *GetBigTileData() const { return m_pBigTiles; }
			uBiggerTileType GetBigTileData(int x, int y) const
			{
				if (!IsPointInMapForBigTiles(x,y))
					return BIGGER_TILE_NULL;
				return m_pBigTiles[GetBigTilePos(x, y)];
			}

			bool SetBigTileData(int x, int y, uBiggerTileType data)
			{
				if (!IsPointInMapForBigTiles(x,y))
					return false;
				m_pBigTiles[GetBigTilePos(x, y)] = data;
				return true;
			}

			bool SetBigTileData(int pos, uBiggerTileType data)
			{
				if (pos < 0 || pos >= GetBigTileCount())
					return false;
				m_pBigTiles[pos] = data;
				return true;
			}

			int GetWidthForSmlTiles(void) const { return m_nWidth / TILE_WIDTH;}
			int GetHeightForSmlTiles(void)	const { return m_nHeight / TILE_HEIGHT;}

			int GetWidthForMidTiles() const { return (m_nWidth + TILE_MID_WIDTH - 1) / TILE_MID_WIDTH; }
			int GetHeightForMidTiles() const { return (m_nHeight + TILE_MID_HEIGHT - 1) / TILE_MID_HEIGHT; }

			int GetWidthForBigTiles() const	{ return (m_nWidth + TILE_BIG_WIDTH - 1) / TILE_BIG_WIDTH; }
			int GetHeightForBigTiles() const { return (m_nHeight + TILE_BIG_HEIGHT - 1) / TILE_BIG_HEIGHT; }
			
			int GetWidthForColor() const { return GetWidthForSmlTiles() + 1; }
			int GetHeightForColor() const { return GetHeightForSmlTiles() + 1; }

			int GetWidthForWater() const { return (m_nWidth + WATER_WIDTH - 1) / WATER_WIDTH; }
			int GetHeightForWater() const { return (m_nHeight + WATER_HEIGHT - 1) / WATER_HEIGHT; }
			
			bool GetOrders(int i, eGroundLayers &layer) const 
			{ 
				if (i>=XPTILES_ALL_LAYER_COUNT || i<0)
					return false;
				layer = static_cast<eGroundLayers>(m_TileOrder[i]);
				return true;
			}
			bool SetOrders(int i, eGroundLayers layer)
			{
				if (i>=XPTILES_ALL_LAYER_COUNT || i<0)
					return false;
				m_TileOrder[i] = layer;
				return true;
			}

		private:
			virtual bool initial();
			void freeMaps();
			bool allocMaps();

		private:
			std::wstring m_waterNames[WATER_EFFECT_COUNT];
			int m_nWidth;
			int m_nHeight;
			PtrTileType m_pTiles[XPTILES_SMALL_LAYER_COUNT];
			//每两个字节表示一格，其中，第一个字节(MSK:FF 00)为地表类型，
			//第二个字节的高4位(MSK:00 F0)为Tile的种类，第二个字节的低4位(MSK:00 0F)为具体的TILE号
			unsigned int* m_pGroundColor;
			uWaterTileType* m_pWaters;
			PtrBiggerTileType m_pMidTiles[XPTILES_MID_LAYER_COUNT];
			PtrBiggerTileType m_pBigTiles;
			unsigned char m_TileOrder[XPTILES_ALL_LAYER_COUNT];
		};
	}

}
#endif