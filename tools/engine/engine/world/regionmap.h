#pragma once

#include "..\common\xptypes.h"
#include "..\common\recttopologylist.h"
#include "..\common\xref.h"
#include "..\common\fileiomanager.h"
#include "..\common\log.h"
#include "..\map\pmap.h"
#include "..\map\pimg.h"
#include "..\map\plinkedobject.h"
#include "..\map\pdistortionobject.h"
#include "..\engine\enginebase.h"
#include "picresrc.h"

namespace Nuclear
{
	struct LkoTreeNode;
	class LkoTree;
	struct RenderTargetCache;
	class Effect;
	class Distort;
	class XPEnvSoundArea;
	class XPBkgSoundArea;
	class XPBkgVolArea;
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
			CRECT rect;
			CPOINT pos;
			SortBaseRectType base;
			bool validBase;
			CRECT GetRect() const { return base; }
			EffectInfo() : pEffect(NULL), validBase(false) { }
		};

		struct PicEle
		{
			union
			{
				DWORD color;
				struct {//�������ڴ�Ͳ����ˣ�һ��PicEle������һ����ɫ��Ҳ�����ĸ����һ����ɫ��С�ر���
					//��������pos������С�ر����ɫ����±꣬�ֱ������Ϻ����½ǵĵ㡣
					unsigned short colorpos1;
					unsigned short colorpos2;
				};
			};
			CRECT pos;
			PicResrc *pPicResrc;
			bool forCache;
			bool useColorPos;//�Ƿ��ĸ����һ����ɫ
			//��ر������Ͳ㣬һ��ͼƬ��ֵ���ͬ��cache���ϣ���������ͬһ��ͼƬ����ڶ��Region�ϣ�
			//������Χ��Region�ϵ�PicEle��forCacheΪtrue�������cache�Ļ����Ͳ��û���
			PicEle() : pPicResrc(NULL), forCache(false), useColorPos(false) { }
		};

		struct LkoEle					//LkoTreeNodeList���������һ������ṹ��ָ���vector��
		{									//˳���ǻ滭˳������ṹ��������������
			PicResrc *pPicResrc;			//ͼƬ��Դ
			FRECT rect;						//����ת����Ϊԭ���ͼƬλ��
			LkoTreeNode *pTreeNode;		//����ʱ�ı任����
			LkoEle() : pPicResrc(NULL) { }
		};

		struct OrgPicEleWithBase
		{
			SortBaseRectType base;
		};

		struct PicEleWithBase
		{
			XPCOLOR color;
			CRECT pos;
			OrgPicEleWithBase *pOrg;
			PicResrc *pPicResrc;
			PicEleWithBase() : pPicResrc(NULL) { }
		};

		struct BackGoundInfo {
			CPOINT pos;				//ͼƬ���ĵ�λ�ã���ͼ����
			FPOINT relSpeed;		//�ٶȱȣ�[0~1]�����ٶȱ�->(1, 1)ʱ����ζ�ż���ȫ��ͼ�ɼ���������Զ����viewport�ܣ����ٶȱ�->(0, 0)ʱ�����˻�Ϊ��ͨ������
			CRECT activeRect;		//����������ͼ����
			int girdWidth;			//���ӵĿ��
			int girdHeight;			//���ӵĸ߶�
			int width;				//ͼƬ�������ӿ�
			int height;				//ͼƬ�������Ӹ�
			PicResrc **ppPicResrcs;	//pPicResrc����
			bool wrapHorz;			//�Ƿ�ˮƽ�������	//����Ժ�Ҫ��ʲô����֮��ľ��ñ�־λ������
			bool wrapVert;			//�Ƿ���ֱ�������
			BackGoundInfo() : pos(0, 0), relSpeed(0.0f, 0.0f), activeRect(0, 0, 0, 0), girdWidth(0), 
				girdHeight(0), width(0), height(0), ppPicResrcs(NULL), wrapHorz(false), wrapVert(false) {}
		};

		struct TriggerInfo
		{
			std::wstring effname;	//������������
			unsigned short triggerTypeFlag;		//TRIGGER_TYPE;
			ActionTypeFlag actionsFlag;			//�����Ķ�������
			DWORD beginTime;
			DWORD endTime;
			float frequency;
			DWORD triDelta;
			DWORD maxDelta;
			CPOINT excursion;
			XPEFFECT_LAYER layer;
			/////////////////////����������ʱ����
			DWORD triggerTime;	//�����ϴγ���ȥ���������ĺ�����
			DWORD effectTime;	//�����ϴδ�����Ч�ɹ��ĺ�����
			TriggerInfo& operator =(const SubMap::TriggerInfo &x)
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
			TriggerInfo(const SubMap::TriggerInfo &x) : triggerTypeFlag(x.triggerTypeFlag), actionsFlag(x.actionsFlag), 
				beginTime(x.beginTime), endTime(x.endTime), frequency(x.frequency), triDelta(x.triDelta), 
				maxDelta(x.maxDelta), excursion(x.excursion), layer(x.layer), triggerTime(0), effectTime(0)
			{}
		};

		struct TriggerObjInfo
		{
			int infoID;
			CRECT rect;
			__int64 updateTick;
		};

		struct WaterArea
		{
			unsigned int depth;
			int border;
			CRECT rect;
		};

	};

	class RegionMap
	{
	public:

		struct LoadingPicResrc
		{
			PImg imgfile;
			std::vector<PicResrc*> vPicResrcs;
		};

		typedef std::map<SubMap::ObjMapKey, LoadingPicResrc> LoadingPicResrcMap;
		typedef std::map<std::wstring, SubMap::ObjMapKey> ElementNames;
		typedef std::map<SubMap::ObjMapKey, PLinkedObject> LoadingLinkedObjMap;
		typedef std::map<SubMap::ObjMapKey, PDistortionObject> LoadingDistortObjMap;
		typedef std::vector<Region::BackGoundInfo> BackGoundInfoVet;
		

		enum { REGION_WIDTH = 256, REGION_HEIGHT = 256 };
		//enum { REGION_WIDTH = 128, REGION_HEIGHT = 128 };

		
		typedef std::vector<Region::TriggerInfo> TriggerInfoVet;
		typedef std::vector<TriggerInfoVet> TriggerInfos;		

		struct RegionInfo
		{
			std::vector<Region::PicEle> m_vPicEle[PIC_LAYER_COUNT]; // ��ȥ���ղ�, ����в��ǿյ�
			std::vector<Region::PicEleWithBase> m_vPicEleWithBase;				  // ����в�
			std::vector<LkoTree*> m_vpLkoTrees[PIC_ELEMENT_LAYER_COUNT];//���ظ�
			std::vector<const Region::LkoEle*> m_vLkoEle[PIC_ELEMENT_LAYER_COUNT];//���Vectorֻ����load��Դ���Ժ���Բ�������
			std::vector<Region::TriggerObjInfo*> m_vpTriggerObjInfo;//���ظ���ֻ��update��
			std::vector<Region::WaterArea> m_vWaterArea;//ˮ�����򣬲�ͬ��������ظ���ͬһ��ˮ�渲�ǲ�ͬ����Ļ���ÿ�����򶼻ᱣ��һ��
			std::vector<Distort*> m_vpDistorts[PIC_ELEMENT_LAYER_COUNT];//���ظ�
			std::vector<PicResrc*> m_vpDistortsPic;//����load��Դ
			std::vector<Region::EffectInfo> m_effects[PIC_ELEMENT_LAYER_COUNT]; // ����㶯�����������Ч���ܳ���512*512
			std::vector<XPEnvSoundArea*> m_EnvSoundAreas;	//����������
			std::vector<XPBkgSoundArea*> m_BkgSoundAreas;	//����������
			std::vector<XPBkgVolArea*> m_BkgVolumeAreas;	//������������

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
		std::vector<XHardRef<Region::OrgPicEleWithBase> > m_orgPics;
		BackGoundInfoVet m_backGroundInfos;
		

	private:
		bool LoadSmlGround(const PMap& pmap, const SubMap::PPathMap* pathMap, int layerID);
		bool LoadBiggerGround(SubMap::GroundLayers layer, const PMap& pmap, const SubMap::PPathMap* pathMap, int layerID, std::map<unsigned short, PicResrc*> *tileMap);
		void AddCacheLagerPic(const CRECT &rect, PicResrc *pPicResrc, unsigned int color, int layerID);
		inline bool RegionMap::LoadImg(const std::wstring& imgname, LoadingPicResrcMap& picMap, ElementNames& names, SubMap::ObjMapKey& keySeed)
		{
			if (names.find(imgname) == names.end())
			{
				std::wstring mappath = imgname;
				mappath.erase(imgname.length() - 4, 4);
				names[imgname] = ++keySeed;
				XBuffer imgfiledata;
				if( !m_pEB->GetFileIOManager()->GetFileImage(imgname, imgfiledata))
				{
					XPTRACE(L"load imgfile failed, file is %s\n", imgname.c_str());
					XPLOG_ERROR(L"load imgfile failed, file is %s\n", imgname.c_str());
					return false;
				}
				//else
				//	XPTRACE(L"%s\n", imgpath.c_str());


				LoadingPicResrc &tmp = picMap[keySeed];
				tmp.imgfile.LoadFromMem(imgfiledata);
				int index = 0;
				for(std::vector<CRECT>::const_iterator it2 = tmp.imgfile.m_vectorRct.begin(), ie2 = tmp.imgfile.m_vectorRct.end(); it2 != ie2; ++it2)
				{
					wchar_t fnbuf[MAX_PATH];
					wsprintf(fnbuf,L"_%d",index);
					std::wstring uri = mappath + fnbuf;
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
		//����
		bool LoadBaseInfo(const PMap& pmap);
		bool LoadGround(const PMap& pmap, const SubMap::PPathMap* pathMap);
		bool LoadElementPicMap(const PMap& pmap, LoadingPicResrcMap& picMap, LoadingLinkedObjMap& lkoMap, LoadingDistortObjMap& disMap, ElementNames& names);
		bool LoadElements(const PMap& pmap, LoadingPicResrcMap& picMap);
		bool LoadEffects(const PMap& pmap);
		bool LoadWaterArea(const PMap& pmap);
		bool LoadLinkedObjs(const PMap& pmap, const LoadingPicResrcMap& picMap, const LoadingLinkedObjMap& lkoMap, const ElementNames& names);
		bool LoadDistortObjs(const PMap& pmap, const LoadingPicResrcMap& picMap, const LoadingDistortObjMap& disMap, const ElementNames& names);
		bool LoadBackGround(const PMap& pmap, const LoadingPicResrcMap& picMap);

		virtual void AddAreaSize(PicResrc* pic, const CRECT& rect) {}
	public:
		//��������
		RegionMap(EngineBase* pEB, World* pWorld, PicResrcMap* pPicResrcMap);
		virtual ~RegionMap(void);

	};

}