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

	class Map : public RegionMap, public GUObject//��ͼ
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

		//���в����ֱ�Ϊ���ӿڣ��Ƿ��첽���Ƿ�ص����Ƿ��ֶ�Ԥ�����ص��İٷֱ���ʼֵ
		void AssureRegionResource(NuclearRect viewport, bool async, bool callback, bool isManualPrefetch, int pro);
		bool HasWater() const { return m_hasWater; }

		bool Load(const std::wstring & mapname, const std::wstring & mazename, const SubMap::PPathMap*);
		//����mazeΪ�˽�ʡһ��memcpy������swap���������Ի�ı�maze��Xbuffer
		bool Load(const NuclearBuffer &pmap, NuclearBuffer &maze, const NuclearBuffer &soundXML, const NuclearBuffer &stepsound, const SubMap::PPathMap*, int pro);
		void UseMapMaze();
		bool SetMapMaze(const void* mazeBuffer, size_t size);//����ͨ��ͼ���ݣ��༭��ר�ã�
		bool LoadSoundAreas(LJXML::LJXML_Node<> &root, NuclearStepSoundMap* pSSM);	//��Load�����������������Ǳ༭��Ҳ��Ҫֱ�ӵ���
		void FreeSoundMap();
		bool IsFull() const { return m_bIsFull; }

		class Maze
		{
			NuclearBuffer buffer;
			int width;
			int height;
			unsigned char * maze; // ָ��buffer���ڴ档

			Maze(const Maze&);
			void operator=(const Maze&);
			
		public:
			Maze() { this->width = 0; this->height = 0; this->maze = NULL; }

			// װ���Թ�����
			bool Load(NuclearBuffer &buffer);//���Ϊ��Ч�ʻ��޸�NuclearBuffer��Load֮�����buffer����Ч
			bool Load(Map * map, const std::wstring & mazename);
			bool Load(const void* mazedata, size_t size);

			//����PathFinder
			void SetPathFinderMaze(Map * map);

			// ���ó�ȫ������
			void reset(Map * map);
			
			inline unsigned int getMask(int x, int y) const
			{
				if (x < 0 || y < 0 || x >= this->width || y >= this->height)
					return 0xffffffff; // Խ��ʱ�����е��ϰ�λ�������ϡ�
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
		std::vector<XPSoundArea*> m_SoundAreas;	//��������
		SubMap::TransObjInfoMap m_TransObjects;
		bool m_loading;
		bool m_hasWater;
		bool m_bIsFull;		//��Դ�Ƿ�ȫ��
		bool m_bLookupSegmpak;	//�Ƿ���Ӱ�
		SubMap::TimeEffectParamMap m_TimeEffects;
		SubMap::MaskParamMap m_maskMap;
		unsigned int m_nSign;
		std::vector<XapBatch> m_waterEffects;	//ˮ����Ч������
		SubMap::uWaterTileType *m_pWaterData;	//ˮ����Ч������
		unsigned int *m_pGroundColor;//�ر����ɫ
				
#if defined(WIN32) && defined(DEBUG)// ������
		int m_nLoadingRegion; 
		NuclearRect m_CurRegion; 
		NuclearRect m_LoadingRegionRect;
#endif		
	};

}

#endif