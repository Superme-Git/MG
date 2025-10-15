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

	class Map : public RegionMap, public GUObject// ��ͼ
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

		//���в����ֱ�Ϊ���ӿڣ��Ƿ��첽���Ƿ�ص����Ƿ��ֶ�Ԥ�����ص��İٷֱ���ʼֵ
		void AssureRegionResource(CRECT viewport, bool async, bool callback, bool isManualPrefetch, int pro);
		bool HasWater() const { return m_hasWater; }

		bool Load(const std::wstring & mapname, const std::wstring & mazename, const SubMap::PPathMap*);
		//����mazeΪ�˽�ʡһ��memcpy������swap���������Ի�ı�maze��Xbuffer
		bool Load(const XBuffer &pmap, XBuffer &maze, const XBuffer &soundXML, const XBuffer &stepsound, const SubMap::PPathMap*, int pro);
		void UseMapMaze();
		bool SetMapMaze(const void* mazeBuffer, size_t size);//����ͨ��ͼ���ݣ��༭��ר�ã�
		bool LoadSoundAreas(XMLIO::CINode &root, PStepSoundMap* pSSM);	//��Load�����������������Ǳ༭��Ҳ��Ҫֱ�ӵ���
		void FreeSoundMap();

		class Maze
		{
			XBuffer buffer;
			int width;
			int height;
			unsigned char * maze; // ָ��buffer���ڴ档

			// nocopyable
			Maze(const Maze&);
			void operator=(const Maze&);
			
		public:
			Maze() { this->width = 0; this->height = 0; this->maze = NULL; }

			// װ���Թ�����
			bool Load(XBuffer &buffer);//���Ϊ��Ч�ʻ��޸�XBuffer��Load֮�����buffer����Ч
			bool Load(Map * map, const std::wstring & mazename);
			bool Load(const void* mazedata, size_t size);

			//����PathFinder
			void SetPathFinderMaze(Map * map);

			// ���ó�ȫ������
			void reset(Map * map);

			// ����ʱ�����������Թ���
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
					return 0xffffffff; // Խ��ʱ�����е��ϰ�λ�������ϡ�
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
		std::vector<XPSoundArea*> m_SoundAreas;	//��������
		XPBkgDefaultSoundArea* m_pBackGroundDefaultArea;
		SubMap::TransObjInfoMap m_TransObjs;
		bool m_hasWater;
		SubMap::TimeEffectParamMap m_TimeEffects;
		SubMap::MaskParamMap m_maskMap;
		unsigned int m_nSign;
		
		// lru
		
		
		int m_nLoadingRegion; // todo ������
		CRECT m_currentRegion; // todo ������
		CRECT m_loadingRegion; // todo ������
		
		std::vector<XapBatch> m_waterEffects;	//ˮ����Ч������
		SubMap::WaterTileType *m_pWaterData;	//ˮ����Ч������
		unsigned int *m_pGroundColor;//�ر����ɫ
				
		// ռ��
	private:
		Map(const Map&);
		Map& operator=(const Map&);
	};
}