#pragma  once

#include <map>
#include <vector>
#include <windows.h>
#include "..\effect\xapbatch.h"
#include "..\engine\guobj.h"
#include "regionmap.h"

namespace Nuclear
{

	class ISprite;
	class Sprite;
	class World;
	class MapPicElementReadTask;
	class XPSoundArea;
	class XPBkgDefaultSoundArea;
	class PStepSoundMap;

	class Map : public RegionMap, public GUObject// 地图
	{
	public:
		enum { MAP_NAME_MAX_LENGTH = 32 };

		friend class World;
		friend class MapPicElementReadTask;

		Map(World *pWorld, EngineBase* pEB, PicResrcMap* pPicResrcMap) : RegionMap(pEB, pWorld, pPicResrcMap), m_pStepSoundMap(NULL),
			m_loading(false), m_pWaterData(NULL), m_hasWater(false), m_pBackGroundDefaultArea(NULL), m_nSign(0), m_pGroundColor(NULL) { }
		virtual ~Map();

		static std::wstring GetMapResUrl(const std::wstring &mapname) 
		{
			int a = 0;
			return mapname + L"\\map.rmp"; 
		}
		static std::wstring GetSoundResUrl(const std::wstring &mapname) { return L"/map/" + mapname + L"/sound.mps"; }
		static std::wstring GetStepSoundResUrl(const std::wstring &mapname) { return L"/map/" + mapname + L"/ssm.ssm"; }

		//下列参数分别为：视口，是否异步，是否回调，是否手动预读，回调的百分比起始值
		void AssureRegionResource(CRECT viewport, bool async, bool callback, bool isManualPrefetch, int pro);
		bool HasWater() const { return m_hasWater; }

		bool Load(const std::wstring & mapname, const std::wstring & mazename, const SubMap::PPathMap*);
		//其中maze为了节省一次memcpy，用了swap方法，所以会改变maze的Xbuffer
		bool Load(const XBuffer &pmap, XBuffer &maze, const XBuffer &soundXML, const XBuffer &stepsound, const SubMap::PPathMap*, int pro);
		void UseMapMaze();
		bool SetMapMaze(const void* mazeBuffer, size_t size);//设置通过图数据（编辑器专用）
		bool LoadSoundAreas(XMLIO::CINode &root, PStepSoundMap* pSSM);	//在Load会调用这个方法，但是编辑器也需要直接调用
		void FreeSoundMap();

		class Maze
		{
			XBuffer buffer;
			int width;
			int height;
			unsigned char * maze; // 指向buffer的内存。

			// nocopyable
			Maze(const Maze&);
			void operator=(const Maze&);
			
		public:
			Maze() { this->width = 0; this->height = 0; this->maze = NULL; }

			// 装载迷宫数据
			bool Load(XBuffer &buffer);//这个为了效率会修改XBuffer，Load之后这个buffer会无效
			bool Load(Map * map, const std::wstring & mazename);
			bool Load(const void* mazedata, size_t size);

			//配置PathFinder
			void SetPathFinderMaze(Map * map);

			// 设置成全部可走
			void reset(Map * map);

			// 调试时，用来画出迷宫。
			/*
			inline bool walkable(int x, int y, unsigned int mask = 0xffffffff) const
			{
				if (x < 0 || y < 0 || x >= this->width || y >= this->height)
					return false;
				return 0 == (maze[x + y * width] & mask);
			}
			*/
			inline unsigned int getMask(int x, int y) const
			{
				if (x < 0 || y < 0 || x >= this->width || y >= this->height)
					return 0xffffffff; // 越界时，所有的障碍位都设置上。
				return maze[x + y * width];
			}

			bool setMask(const unsigned char* pData, const CRECT &rect)
			{
				if (!pData)
					return false;
				int x = rect.left<0?0:rect.left;
				int y = rect.top<0?0:rect.top;
				int w = (rect.right>this->width?this->width:rect.right) - x;
				int sw = rect.Width();
				int dw = this->width - x;
				int y1 = rect.bottom>this->height?this->height:rect.bottom;
				if (w <= 0 || y >= y1)
					return false;
				unsigned char* pDist = maze + x + y * this->width;
				const unsigned char* pSrc = pData;
				for (;y<y1;++y, pDist += this->width, pSrc += sw)
				{
					memcpy_s(pDist, dw, pSrc, w);
				}
				return true;
			}
		};

		const Maze & getMaze() const { return m_maze; }
	private:
		void DeleteSoundMap();

	private:
		PStepSoundMap *m_pStepSoundMap;
		Maze m_maze;

		//
		bool m_loading;
		std::vector<RegionInfo*> m_curRegions;
		std::vector<XPSoundArea*> m_SoundAreas;	//声音区域
		XPBkgDefaultSoundArea* m_pBackGroundDefaultArea;
		SubMap::TransObjInfoMap m_TransObjs;
		bool m_hasWater;
		SubMap::TimeEffectParamMap m_TimeEffects;
		SubMap::MaskParamMap m_maskMap;
		unsigned int m_nSign;
		
		// lru
		
		
		int m_nLoadingRegion; // todo 调试用
		CRECT m_currentRegion; // todo 调试用
		CRECT m_loadingRegion; // todo 调试用
		
		std::vector<XapBatch> m_waterEffects;	//水面特效在这里
		SubMap::WaterTileType *m_pWaterData;	//水面特效的数据
		unsigned int *m_pGroundColor;//地表的颜色
				
		// 占坑
	private:
		Map(const Map&);
		Map& operator=(const Map&);
	};
}