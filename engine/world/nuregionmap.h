#ifndef __Nuclear_regionmap_H__
#define __Nuclear_regionmap_H__

#include "../common/nuxptypes.h"
#include "../common/nurecttopologylist.h"
#include "../common/nuxref.h"
#include "../common/nufileiomanager.h"
#include "../common/nulog.h"
#include "../map/nupmap.h"
#include "../map/nupimg.h"
#include "../map/nuplinkedobject.h"
#include "../map/nupdistortionobject.h"
#include "../engine/nuenginebase.h"
#include "nupicresrc.h"
#include <stdio.h>
#include "../soundarea/xpenvsoundarea.h"


namespace Nuclear
{
	struct LkoTreeNode;
	class LkoTree;
	struct RenderTargetCache;
	class Effect;
	class Distort;
	class World;

	namespace SubMap
	{
		class PPathMap;
	};

	namespace Region
	{
		struct EffectInfo
		{
			Effect *pEffect;
			NuclearRect rect;
			NuclearPoint pos;
			NuclearSortBaseRectType base;
			bool validBase;
			NuclearRect GetRect() const { return base; }
			EffectInfo() : pEffect(NULL), validBase(false) { }
		};

		struct PicEle
		{
			union
			{
				DWORD color;
				struct 
				{//这样子内存就不变了，一个PicEle可以用一个颜色，也可以四个点各一个颜色（小地表），下面两个pos就是在小地表的颜色表的下标，分别是左上和左下角的点。
					unsigned short colorpos1;
					unsigned short colorpos2;
				};
			};
			NuclearRect pos;
			PicResrc *pPicResrc;
			bool forCache;
			bool useColorPos;//是否四个点各一个颜色
			//大地表和物件低层，一张图片会分到不同的cache块上，所以现在同一张图片会绑在多个Region上，
			//绑在周围的Region上的PicEle的forCache为true，如果不cache的话，就不用画了
			PicEle() : pPicResrc(NULL), forCache(false), useColorPos(false) { }
		};

		struct LkoEle					//LkoTreeNodeList的子类会有一个这个结构体指针的vector，
		{									//顺序是绘画顺序，这个结构体会绑在区域里面
			PicResrc *pPicResrc;			//图片资源
			NuclearFRectt rect;						//以旋转中心为原点的图片位置
			LkoTreeNode *pTreeNode;		//运行时的变换数据
			LkoEle() : pPicResrc(NULL) { }
		};

		struct OrgPicEleWithBase
		{
			NuclearSortBaseRectType base;
		};

		struct PicEleWithBase
		{
			NuclearColor color;
			NuclearRect pos;
			OrgPicEleWithBase *pOrg;
			PicResrc *pPicResrc;
			PicEleWithBase() : pPicResrc(NULL) { }
		};

		struct BackGoundInfo {
			NuclearPoint pos;				//图片中心点位置，地图坐标
			NuclearFPoint relSpeed;		//速度比，[0~1]，当速度比->(1, 1)时，意味着几乎全地图可见，几乎永远跟着viewport跑，当速度比->(0, 0)时，就退化为普通背景了
			NuclearRect activeRect;		//激活区，地图坐标
			int girdWidth;			//格子的宽度
			int girdHeight;			//格子的高度
			int width;				//图片几个格子宽
			int height;				//图片几个格子高
			PicResrc **ppPicResrcs;	//pPicResrc数组
			bool wrapHorz;			//是否水平方向缠绕	//如果以后要做什么镜像之类的就用标志位来做吧
			bool wrapVert;			//是否竖直方向缠绕
			BackGoundInfo() : pos(0, 0), relSpeed(0.0f, 0.0f), activeRect(0, 0, 0, 0), girdWidth(0), 
				girdHeight(0), width(0), height(0), ppPicResrcs(NULL), wrapHorz(false), wrapVert(false) {}
		};

		struct sTriggerInfo
		{
			std::wstring effname;	//触发器的名字
			unsigned short triggerTypeFlag;		//eTRIGGER_TYPE;
			ActionTypeFlag actionsFlag;			//包含的动作类型
			DWORD beginTime;
			DWORD endTime;
			float frequency;
			DWORD triDelta;
			DWORD maxDelta;
			NuclearPoint excursion;
			Nuclear_EffectLayer layer;
			/////////////////////以下是运行时参数
			DWORD triggerTime;	//距离上次尝试去“触发”的毫秒数
			DWORD effectTime;	//距离上次触发特效成功的毫秒数
			sTriggerInfo& operator =(const SubMap::sTriggerInfo &x)
			{
				triggerTypeFlag = x.triggerTypeFlag;
				actionsFlag = x.actionsFlag;
				beginTime = x.beginTime;
				endTime = x.endTime;
				frequency = x.frequency;
				triDelta = x.triDelta;
				maxDelta = x.maxDelta;
				excursion = x.excursion;
				layer = x.layer;
				triggerTime = 0;
				effectTime = 0;
				return *this;
			}
			sTriggerInfo(const SubMap::sTriggerInfo &x) : triggerTypeFlag(x.triggerTypeFlag), actionsFlag(x.actionsFlag), 
				beginTime(x.beginTime), endTime(x.endTime), frequency(x.frequency), triDelta(x.triDelta), 
				maxDelta(x.maxDelta), excursion(x.excursion), layer(x.layer), triggerTime(0), effectTime(0)
			{}
		};

		struct sTriggerObjInfo
		{
			int infoID;
			NuclearRect rect;
			__int64 updateTick;
		};

		struct WaterArea
		{
			unsigned int depth;
			int border;
			NuclearRect rect;
		};

	};

	class RegionMap
	{
	public:

		struct LoadingPicResrc
		{
			NuclearImg imgfile;
			std::vector<PicResrc*> vPicResrcs;
		};

		typedef std::map<SubMap::ObjMapKey, LoadingPicResrc> LoadingPicResrcMap;
		typedef std::map<std::wstring, SubMap::ObjMapKey> ElementNames;
		typedef std::map<SubMap::ObjMapKey, NuclearLinkedObject> LoadingLinkedObjMap;
		typedef std::map<SubMap::ObjMapKey, NuclearDistortionObject> LoadingDistortObjMap;
		typedef std::vector<Region::BackGoundInfo> BackGoundInfoVet;
		

		enum { REGION_WIDTH = 256, REGION_HEIGHT = 256 };
		//enum { REGION_WIDTH = 128, REGION_HEIGHT = 128 };

		
		typedef std::vector<Region::sTriggerInfo> TriggerInfoVet;
		typedef std::vector<TriggerInfoVet> TriggerInfos;		

		struct RegionInfo
		{
			std::vector<Region::PicEle> m_vPicEle[XPPIC_LAYER_COUNT]; // 两层物件中层都是空的
			std::vector<Region::PicEleWithBase> m_vPicEleWithBase[XPSORTED_LAYER_COUNT];  // 两层物件中层
			std::vector<LkoTree*> m_vpLkoTrees[XPPIC_ELEMENT_LAYER_COUNT];//有重复
			std::vector<const Region::LkoEle*> m_vLkoEle[XPPIC_ELEMENT_LAYER_COUNT];//这个Vector只用来load资源，以后可以不用数组
			std::vector<Region::sTriggerObjInfo*> m_vpTriggerObjInfo;//有重复，只在update用
			std::vector<Region::WaterArea> m_vWaterArea;//水面区域，不同区域会有重复，同一个水面覆盖不同区域的话，每个区域都会保留一个
			std::vector<Distort*> m_vpDistorts[XPPIC_ELEMENT_LAYER_COUNT];//有重复
			std::vector<PicResrc*> m_vpDistortsPic;//用来load资源
			std::vector<Region::EffectInfo> m_effects[XPPIC_ELEMENT_LAYER_COUNT]; // 物件层动画，这里的特效不能超过512*512

			std::vector<XPEnvSoundArea*> m_EnvSoundAreas;	//环境音区域

			RenderTargetCache* m_pSurfaceRenderTargetCache;
			RegionInfo() : m_pSurfaceRenderTargetCache(NULL) { }
		};

	protected:
		EngineBase *m_pEB;
		World *m_pWorld;
		int m_width;
		int m_height;
		int m_widthForWater;
		int m_heightForWater;
		int m_regioncol;
		int m_regionrow;
		TriggerInfos m_TriggerInfos;
		std::vector<RegionInfo> m_regions;
		PicResrcMap *m_pAllPicResrcs;
		PicResrcVet m_picResrcs;
		std::vector<NuclearHardRef<Region::OrgPicEleWithBase> > m_orgPics;//这个是用来引用计数防止内存泄漏的
		BackGoundInfoVet m_BackGroundInfos;	

	public:
		RegionMap(EngineBase* pEB, World* pWorld, PicResrcMap* pPicResrcMap);
		virtual ~RegionMap(void);

	private:
		bool LoadSmlGround(const NuclearMap& pmap, const SubMap::PPathMap* pathMap, int layerID);
		bool LoadBiggerGround(SubMap::eGroundLayers layer, const NuclearMap& pmap, const SubMap::PPathMap* pathMap, int layerID, std::map<unsigned short, PicResrc*> *tileMap);
		void AddCacheLagerPic(const NuclearRect &rect, PicResrc *pPicResrc, unsigned int color, int layerID);
		inline bool LoadImg(const std::wstring& imgname, LoadingPicResrcMap& picMap, ElementNames& names, SubMap::ObjMapKey& keySeed)
		{
			if (names.find(imgname) == names.end())
			{
				std::wstring mappath = imgname;
				mappath.erase(imgname.length() - 4, 4);
				names[imgname] = ++keySeed;
				NuclearBuffer imgfiledata;
				if( !m_pEB->GetFileIOManager()->GetFileImage(imgname, imgfiledata))
				{
					XPTRACE(L"load imgfile failed, file is %s\n", imgname.c_str());
					XPLOG_ERROR(L"load imgfile failed, file is %s\n", imgname.c_str());
					return false;
				}

				LoadingPicResrc &tmp = picMap[keySeed];
				tmp.imgfile.LoadFromMem(imgfiledata);
				int index = 0;
				for(std::vector<NuclearRect>::const_iterator it2 = tmp.imgfile.m_vectorRct.begin(), ie2 = tmp.imgfile.m_vectorRct.end(); it2 != ie2; ++it2)
				{
#ifndef ANDROID
					wchar_t fnbuf[MAX_PATH] = {0};
                    swprintf(fnbuf, MAX_PATH, L"_%d", index);
                    std::wstring uri = mappath + fnbuf;
#else
                    char fnbuf[MAX_PATH] = {0};
                    snprintf(fnbuf, MAX_PATH, "_%d", index);
                    std::wstring uri = mappath + s2ws(fnbuf);
#endif
					PicResrcMap::iterator picIt = m_pAllPicResrcs->find(uri);
					PicResrc *pPicResrc = NULL;
					if (picIt == m_pAllPicResrcs->end())
					{
						pPicResrc = new PicResrc(uri);
						(*m_pAllPicResrcs)[uri] = pPicResrc;
					} else {
						pPicResrc = picIt->second;
					}
					tmp.vPicResrcs.push_back(pPicResrc);
					m_picResrcs.push_back(pPicResrc);
					++index;
					++(pPicResrc->mapRef);
					AddAreaSize(pPicResrc, *it2);
				}
			}
			return true;
		}

	protected:
		bool LoadBaseInfo(const NuclearMap& pmap);
		bool LoadGround(const NuclearMap& pmap, const SubMap::PPathMap* pathMap);
		bool LoadElementPicMap(const NuclearMap& pmap, LoadingPicResrcMap& picMap, LoadingLinkedObjMap& lkoMap, LoadingDistortObjMap& disMap, ElementNames& names, bool bIsSegmpack);
		bool LoadElements(const NuclearMap& pmap, LoadingPicResrcMap& picMap);
		bool LoadEffects(const NuclearMap& pmap);
		bool LoadWaterArea(const NuclearMap& pmap);
		bool LoadLinkedObjs(const NuclearMap& pmap, const LoadingPicResrcMap& picMap, const LoadingLinkedObjMap& lkoMap, const ElementNames& names);
		bool LoadDistortObjs(const NuclearMap& pmap, const LoadingPicResrcMap& picMap, const LoadingDistortObjMap& disMap, const ElementNames& names);
		bool LoadBackGround(const NuclearMap& pmap, const LoadingPicResrcMap& picMap);

		virtual void AddAreaSize(PicResrc* pic, const NuclearRect& rect) {}
	};

}

#endif