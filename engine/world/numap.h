#ifndef __Nuclear_map_H__
#define __Nuclear_map_H__

#include <map>
#include <vector>
#include "../effect/nuxapbatch.h"
#include "../engine/nuguobj.h"
#include "nuregionmap.h"

namespace Nuclear
{
	class ISprite;
	class Sprite;
	class World;
	class MapPicElementReadTask;
	class NuclearStepSoundMap;

	class Map : public RegionMap, public GUObject//地图
	{
	public:
		enum { MAP_NAME_MAX_LENGTH = 32 };

		friend class World;
		friend class MapPicElementReadTask;

		Map(World *pWorld, EngineBase* pEB, PicResrcMap* pPicResrcMap) : RegionMap(pEB, pWorld, pPicResrcMap), m_NuclearStepSoundMap(NULL),
			m_loading(false), m_pWaterData(NULL), m_hasWater(false), m_bIsFull(true), m_bLookupSegmpak(false), 
            m_nSign(0), 
            m_pGroundColor(NULL) { }
		virtual ~Map();

		static std::wstring GetMapResUrl(const std::wstring &mapname) { return L"/map/" + mapname + L"/map.rmp"; }
		static std::wstring GetSoundResUrl(const std::wstring &mapname) { return L"/map/" + mapname + L"/sound.mps"; }
		static std::wstring GetStepSoundResUrl(const std::wstring &mapname) { return L"/map/" + mapname + L"/ssm.ssm"; }

		//下列参数分别为：视口，是否异步，是否回调，是否手动预读，回调的百分比起始值
		void AssureRegionResource(NuclearRect viewport, bool async, bool callback, bool isManualPrefetch, int pro);
		bool HasWater() const { return m_hasWater; }

		bool Load(const std::wstring & mapname, const std::wstring & mazename, const SubMap::PPathMap*);
		//其中maze为了节省一次memcpy，用了swap方法，所以会改变maze的Xbuffer
		bool Load(const NuclearBuffer &pmap, NuclearBuffer &maze, const NuclearBuffer &soundXML, const NuclearBuffer &stepsound, const SubMap::PPathMap*, int pro);
		void UseMapMaze();
		bool SetMapMaze(const void* mazeBuffer, size_t size);//设置通过图数据（编辑器专用）
		bool LoadSoundAreas(LJXML::LJXML_Node<> &root, NuclearStepSoundMap* pSSM);	//在Load会调用这个方法，但是编辑器也需要直接调用
		void FreeSoundMap();
		bool IsFull() const { return m_bIsFull; }

		class Maze
		{
			NuclearBuffer buffer;
			int width;
			int height;
			unsigned char * maze; // 指向buffer的内存。

			Maze(const Maze&);
			void operator=(const Maze&);
			
		public:
			Maze() { this->width = 0; this->height = 0; this->maze = NULL; }

			// 装载迷宫数据
			bool Load(NuclearBuffer &buffer);//这个为了效率会修改NuclearBuffer，Load之后这个buffer会无效
			bool Load(Map * map, const std::wstring & mazename);
			bool Load(const void* mazedata, size_t size);

			//配置PathFinder
			void SetPathFinderMaze(Map * map);

			// 设置成全部可走
			void reset(Map * map);
			
			inline unsigned int getMask(int x, int y) const
			{
				if (x < 0 || y < 0 || x >= this->width || y >= this->height)
					return 0xffffffff; // 越界时，所有的障碍位都设置上。
				return maze[x + y * width];
			}

			bool setMask(const unsigned char* pData, const NuclearRect &rect)
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
					memcpy(pDist, pSrc, w);
				}
				return true;
			}
		};

		const Maze & getMaze() const { return m_maze; }

	private:
		Map(const Map&);
		Map& operator=(const Map&);
		void DeleteSoundMap();

	private:
		NuclearStepSoundMap *m_NuclearStepSoundMap;
		Maze m_maze;

		std::vector<RegionInfo*> m_curRegions;
		std::vector<XPSoundArea*> m_SoundAreas;	//声音区域
		SubMap::TransObjInfoMap m_TransObjects;
		bool m_loading;
		bool m_hasWater;
		bool m_bIsFull;		//资源是否全的
		bool m_bLookupSegmpak;	//是否查子包
		SubMap::TimeEffectParamMap m_TimeEffects;
		SubMap::MaskParamMap m_maskMap;
		unsigned int m_nSign;
		std::vector<XapBatch> m_waterEffects;	//水面特效在这里
		SubMap::uWaterTileType *m_pWaterData;	//水面特效的数据
		unsigned int *m_pGroundColor;//地表的颜色
				
#if defined(WIN32) && defined(DEBUG)// 调试用
		int m_nLoadingRegion; 
		NuclearRect m_CurRegion; 
		NuclearRect m_LoadingRegionRect;
#endif		
	};

}

#endif