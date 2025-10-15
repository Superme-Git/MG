#ifndef __Nuclear_SPRITEMANAGER_H
#define __Nuclear_SPRITEMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\pobject.h"
#include "..\common\xptypes.h"
#include "..\common\xref.h"
#include "..\engine\animanager.h"
#include "..\isprite.h"


namespace Nuclear
{

class EngineBase;
class CFileIOManager;

typedef std::map<std::wstring, int> HorseHeightMap;
typedef std::set<std::wstring> ActionNameSet;

class HorseHeight
{
private:
	HorseHeightMap m_map;
public:
	bool Load(CFileIOManager* pFileIOMan, const std::wstring &modelname);
	bool Save(CFileIOManager* pFileIOMan, const std::wstring &modelname) const; //�༭��ר��
	int GetHeight(const std::wstring &horsename) { return m_map[horsename]; }
	void SetHeight(const std::wstring &horsename, int height) { m_map[horsename] = height; }
};

typedef std::map<std::wstring, HorseHeight> AllHorseHeightMap;

struct SpriteSoundParam
{
	std::wstring soundname;
	unsigned char vol;
	unsigned char freq;
	SpriteSoundParam() : vol(255), freq(128) {}
};

typedef std::vector<SpriteSoundParam> SpriteSoundParamVet;
typedef std::map<std::wstring, SpriteSoundParamVet> ActionSoundParamMap;//key�Ƕ�����
typedef ActionSoundParamMap* PActionSoundParamMap;

//key��װ������value��ָ����Ϊ�˿��԰�ָ�����Sprite���Ա���棬ʡ��ÿ�ζ�ȥ�����Map��
//��Ϊװ��һ����˵���ò�Ƶ��
typedef std::map<std::wstring, PActionSoundParamMap> ComponentSoundMap;

struct ModelSoundParam
{
	int keyComponentID;
	ComponentSoundMap soundmap;
	ModelSoundParam() : keyComponentID(-1) {}
	void ClearSoundMap() 
	{
		ComponentSoundMap::iterator it = soundmap.begin(), ie = soundmap.end();
		for (;it!=ie;++it)
		{
			delete it->second;
		}
		soundmap.clear();
	}
};

class ActionType
{
public:
	ActionType() {};
	virtual ~ActionType(void) {};
	void Init(CFileIOManager* pFileIOMan) { m_pFileIOMan = pFileIOMan; }
	bool Load();

	ActionTypeFlag GetActionType(const std::wstring &name) const
	{
		std::map<std::wstring, ActionTypeFlag>::const_iterator iter = m_actionTypeMap.find(name);
		if (iter == m_actionTypeMap.end())
			return 0;
		return iter->second;
	}
	std::wstring GetActionDes(unsigned char type) const
	{		
		std::map<unsigned char, std::wstring>::const_iterator iter = m_actionTypeDes.find(type);
		if (iter == m_actionTypeDes.end())
			return L"";
		return iter->second;
	}

	//���·����༭��ר�ã��Ȳ����ڱ༭���༭�ɣ�
	const std::map<unsigned char, std::wstring> &GetDesMap() const { return m_actionTypeDes; }
	//bool InsertActionType(TILE_SIZE_TYPE type, int key, const std::wstring &value);

	//����
private:
	//bool m_bChanged;
	std::map<std::wstring, ActionTypeFlag> m_actionTypeMap;
	std::map<unsigned char, std::wstring> m_actionTypeDes;
	CFileIOManager* m_pFileIOMan;
};

class Action : public PObject
{
	struct Frame
	{
		int m_time; // ���ʱ����أ�1~64
		float m_startTime; // ��һ������ʼ�Ĳ���ʱ�䣬����������������һ�벥�ţ���Ϊ0.5, �������ɱ༭������
		__int64 m_equipOrder[XPDIR_COUNT]; // �·���������������Ⱥ�˳��

		void SetOrder(int dir, const int *ec, int size);
		void GetOrder(int dir, int *ec, int size) const;
	};

	struct Link
	{
		std::wstring name;
		int frame;
	};

	int m_nTime; // ms ����������Ҫ��ʱ�䣬��λ����
	int m_damagepoint; // �˺�������֡
	int m_effectpoint; // Ч������֡

	int m_maskDir; // (1 << XPDIR_UP) ...
	int m_maskEquip; // (1 << XPEC_BODY) ...
	int m_stride;

	std::vector<Frame> m_frames;

	enum { AL_NULL = 0,
		AL_L1 = 1 };

	int m_linkType;
	std::vector<Link> m_links;
	CRECT m_rects[8];

public:

	Action() : m_nTime(0), m_maskDir(0), m_maskEquip(0), m_stride(0), m_linkType(AL_NULL) { }

	std::wstring GetLinkName(int seq) const { return m_linkType ? m_links[seq].name : L""; }
	int GetLineFrame(int seq) const { return m_linkType ? m_links[seq].frame : seq; }

	void SetDir(int dir) { m_maskDir |= ( 1 << dir); }
	void ClearDir(int dir) { m_maskDir &= (~( 1 << dir)); }
	bool TestDir(int dir) const { return 0 != (m_maskDir & ( 1 << dir)); }
	int GetDirMask() const { return m_maskDir; }

	void SetEquip(int ec) 
	{ 
		m_maskEquip |= ( 1 << ec); 
	}
	void ClearEquip(int ec) { m_maskEquip &= (~( 1 << ec)); }
	bool TestEquip(int ec) const { return 0 != (m_maskEquip & ( 1 << ec));  }

	int GetEquipMask() const { return m_maskEquip; }
	void SetEquipMask(int m) { m_maskEquip = m; }

	bool IsInd() const { return m_linkType == AL_L1; }
	void SetInd(bool f) { m_linkType = f ? AL_L1 : AL_NULL; }

	void SetFrameLink(int seq, const std::wstring &n, int f);

	void SetFrameOrder(int seq, int dir, const int *ec, int size);
	void GetFrameOrder(int seq, int dir, int *ec, int size) const;
	float GetFrameStartTime(int seq) const { return m_frames[seq].m_startTime; }
	void SetFrameStartTime(int seq, float t) { m_frames[seq].m_startTime = t; }
	void SetFrameTime(int seq, int t) { m_frames[seq].m_time = t; }
	int GetFrameTime(int seq) const { return m_frames[seq].m_time; }

	int GetFrameCount() const { return (int)m_frames.size(); }
	int GetStride () const { return m_stride; }
	int GetTime() const { return m_nTime; }
	void SetTime(int nTime) { m_nTime = nTime; }
	int GetEffectPoint() const { return m_effectpoint; }
	int GetDamagePoint() const { return m_damagepoint; }
	void SetDamagePoint(int p) { m_damagepoint = p; }
	void SetEffectPoint(int p) { m_effectpoint = p; }
	void SetStride(int stride) { m_stride = stride; }
	void SetFrameCount(int n );

	const CRECT& GetBoundingBox(int dir) const { return m_rects[dir]; }
	void SetBoundingBox(int dir, const CRECT& rect) { m_rects[dir] = rect; }

	bool IsValid() const { return 0 != m_nTime && 0 != m_maskDir && 0 != m_maskEquip && !m_frames.empty(); }

	//
	virtual XOStream& marshal(XOStream &) const;
	virtual const XIStream& unmarshal(const XIStream &);

	// ʹ��Ĭ�Ͽ�������͸�ֵ����
	
};

class SpriteManager;

class Component: public AniLoadingNotify
{
	//Ⱦɫ��Ϣ
	//struct DyeInfo
	//{
	//	int projIndex;
	//	int iH;
	//	int iS;
	//	int iL;
	//	DyeInfo() { projIndex = 0; iH = 0; iS = 0; iL = 0; }
	//	DyeInfo(int i, int h, int s, int l) : projIndex(i), iH(h), iS(s), iL(l) { }
	//};

	typedef std::map<std::wstring, XWeakRef<XAni> > AniMap;
	typedef std::list<XHardRef<XAni>> AniList;
	typedef std::pair<int, AniList> AniInfo;//first������
	typedef std::map<std::wstring, AniInfo> ComponentAni;	//key�Ƕ�����(������û��ani��)
	typedef std::pair<std::wstring, ActionNameSet> LoadingActions;//first��linked action, second��action_name�ǣ�������û��ani��
	typedef std::map<std::wstring, LoadingActions> LoadingUrls;	//key��url
	//typedef std::vector<DyeInfo> DyeInfoVec;//Ⱦɫ�����б���


	int m_layer;					// ������
	std::wstring m_name;			// װ������
	std::wstring m_linkname;		// ����װ�����ƣ�������ӵ��ani��Դ��װ�� Ĭ��Ϊ�մ���û����������װ��
	AniMap m_anis;					// ��������ani��ӳ��map
	EngineBase *m_pEB;
	LoadingUrls m_loadingUrls;
	ComponentAni m_holdAni;
	//DyeInfoVec	m_DyeInfos;			//Ⱦɫ��������
	//int			m_CurDyeIndex;

public:
	
	Component(EngineBase *pEB) : m_layer(0), m_pEB(pEB)/*, m_CurDyeIndex(0)*/ { }
	Component(EngineBase *pEB, int layer, const std::wstring &name) : m_layer(layer), m_name(name), m_pEB(pEB)/*, m_CurDyeIndex(0)*/ { }
	Component(EngineBase *pEB, int layer, const std::wstring &name, const std::wstring &linkname) : m_layer(layer), m_name(name), m_linkname(linkname), m_pEB(pEB)/*, m_CurDyeIndex(0) */{ }
	~Component();

	XWeakRef<XAni> GetAni(const std::wstring &modelname, const std::wstring &actname, bool async);	
	void Release();

	int GetLayer() const { return m_layer; }
	std::wstring GetName() const { return m_name; }
	const std::wstring& GetConstName() const { return m_name; }
	std::wstring GetLinkName() const { return m_linkname; }

	virtual void OnLoadAniReady(const std::wstring &filename);
	virtual void OnLoadAniBaseReady(const std::wstring &filename);

	// Ԥȡ�þ���Ķ�����Դ
	XP_PREFETCH_RESULT PrefetchAction(const std::wstring &modelname, const std::wstring &action_name, const ActionNameSet &linked_action_names);

	// ��ס�þ���Ķ�����Դ�����������ReleaseAction����ô����Զ���ͷţ�ֱ�����黻ģ�ͻ��߱�����
	void HoldAction(const std::wstring &modelname, const std::wstring &action_name, const ActionNameSet &linked_action_names);
	// ֻ��Holdס��Action����ҪRelease
	void ReleaseAction(const std::wstring &action_name);

	void SetName(const std::wstring &name); // �༭��ʹ��
	void SetLinkName(const std::wstring &linkname); // �༭��ʹ��

	void DeleteAction(const std::wstring &modelname,const std::wstring &name); // �༭��ʹ��
	void RenameAction(const std::wstring &modelname,const std::wstring &oldname, const std::wstring &newname, bool renamexap = true); // �༭��ʹ��

	//void AddDyeInfo(int index, int h, int s, int l); // �༭��ʹ��
	//void DelDyeInfo(int index);// �༭��ʹ��
	//void SetCurDyeIndex(int curIndex);
	//DyeInfo* GetDyeInfo(int index);
	//int  GetDyeInfoCount() const { return (int)m_DyeInfos.size(); }

	// ʹ��Ĭ�Ͽ�������͸�ֵ����
};

class SpriteManager
{
public:
	enum eDyeType
	{
        e_unknown = 0,
        e_DyeHSL,
        e_GrayPic
	};

	struct Layer
	{
		std::wstring name;
		std::wstring des;
		bool	bRide;		//�Ƿ���㣨������ĸ߶ȵ�ʱ��ƫ�ƣ�
		bool	bEffect;	//�Ƿ���Ч��������һ��һ����ǿ����Ч��
		Layer() : bRide(false), bEffect(false) { }
		Layer(const std::wstring &n, const std::wstring &d, bool r, bool e) : name(n), des(d), bRide(r), bEffect(e) { }
	};
	ActionType actionType;
	typedef std::map<std::wstring, unsigned char> ActionSoundTypeMap;//key�Ƕ�������value������������

	// ��������
public:
	SpriteManager(EngineBase *pEB);
	~SpriteManager();

	/////////////////////////////////
public:
	std::wstring GetModelDes(const std::wstring &modelname) const;
	void DumpModelNames(std::vector<std::wstring> &modellist) const;

	bool AddModel(const std::wstring &newmodelname, const std::wstring &newmodeldes, const std::vector<Layer> &layers);
	bool ModifyModel(const std::wstring &modelname, const std::wstring &newmodeldes, const std::vector<Layer> &layers);	//�༭��ʹ��
	bool DelModel(const std::wstring &modelname);

public:
	int GetLayerCount(const std::wstring &modelname) const;
	std::wstring GetLayerName(const std::wstring &modelname, int i) const;
	std::wstring GetLayerDes(const std::wstring &modelname, int i) const;
	bool GetLayerRideEnable(const std::wstring &modelname, int i) const;
	bool GetLayerEffectEnable(const std::wstring &modelname, int i) const;
	int GetTitlePos(const std::wstring &modelname) const;
	const CPOINT &GetEffectPos(const std::wstring &modelname) const;
	WATER_SURFACE_TYPE GetWaterSurfaceType(const std::wstring &modelname) const;
	int GetHorseHeight(const std::wstring &modelname, const std::wstring &horsename);
	const XPBase &GetBase(const std::wstring &modelname) const;
	unsigned char GetActionSoundType(const std::wstring &action_name) { return m_actionSoundMap[action_name]; }//������ģ��
	void SetActionSoundType(const std::wstring &action_name, unsigned char type) { m_actionSoundMap[action_name] = type; } //������ģ��
	int GetDyeEnable(const std::wstring &modelname) const;		//�༭��ʹ��
	void SetDyeEnable(const std::wstring &modelname,int i);		//�༭��ʹ��

public:

	const std::wstring& GetActionRef(const std::wstring &modelname, const std::wstring &actname) const;
	
	bool SetBase(const std::wstring &modelname, const XPBase &base, bool savefile);
	bool SaveSoundParam(const std::wstring &modelname); //  �༭��ʹ��
	bool SaveBase(const std::wstring &modelname); //  �༭��ʹ��
	bool SetTitlePos(const std::wstring &modelname, int pos); //  �༭��ʹ��
	bool SetEffectPos(const std::wstring &modelname, const CPOINT& pos); //  �༭��ʹ��
	bool SetHorseHeight(const std::wstring &modelname, const std::wstring &horsename, int pos); //  �༭��ʹ��
	bool SetWaterSurfaceType(const std::wstring &modelname, WATER_SURFACE_TYPE type); //  �༭��ʹ��
	void DeleteAction2(const std::wstring &modelname, const std::wstring &name); // �༭��ʹ��
	void RenameAction2(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname, bool renamexap = true); // �༭��ʹ��
	bool AddComponent(const std::wstring &modelname, const Component &equip); // �༭��ʹ��
	bool DeleteComponent(const std::wstring &modelname, const Component &equip); // �༭��ʹ��
	Component* GetComponent(const std::wstring &modelname, int ectype, const std::wstring &name);

	void DumpComponent(const std::wstring &modelname, int ectype, std::vector<std::wstring> &equiplist) const; // �༭��ʹ�� 
	void RenameComponent(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname); // �༭��ʹ��

	ModelSoundParam &GetSoundParam(const std::wstring &modelname);  //  �༭��ʹ��
	const ModelSoundParam &GetSoundParam(const std::wstring &modelname) const;  //  �༭��ʹ��

	void LoadDyeInfo(const std::wstring &modelname);
	void SaveDyeInfo(const std::wstring &modelname);

	/////////////////////////////////////
public:

	const Action* GetAction(const std::wstring &modelname, const std::wstring &actName) const;
	void SetAction(const std::wstring &modelname, const std::wstring &actName, const Action& act); // �༭��ʹ��

	bool SaveActions(const std::wstring &modelname) const; // �༭��ʹ��
	bool AddAction(const std::wstring &modelname, const std::wstring &name, const Action &act); // �༭��ʹ��
	bool DeleteAction(const std::wstring &modelname, const std::wstring &name); // �༭��ʹ��
	bool RenameAction(const std::wstring &modelname, const std::wstring &oldname, const std::wstring &newname); // �༭��ʹ��
	void DumpAction(const std::wstring &modelname, std::vector<std::wstring> &actlist) const; // �༭��ʹ�� 
	void DumpBaseAction(const std::wstring &modelname, std::vector<std::pair<std::wstring,int> > &baseactlist) const; // �༭��ʹ��
	void DumpActionRefMap(const std::wstring &modelname, std::map<std::wstring,std::wstring> &actrefmap) const; // �༭��ʹ��
	void SetActionRefMap(const std::wstring &modelname, const std::map<std::wstring,std::wstring> &actrefmap); // �༭��ʹ��

	std::wstring GetModelPath(const std::wstring& modelname) const;
	//std::wstring GetModelBodyPath(const std::wstring& modelname) const;

public:
	//����0����û�����⣬����ÿλ���������ĺ���
	unsigned __int64 Init(size_t id, size_t batch);
	bool PreInit();
	bool PostInit();
	bool SaveModels();
	size_t GetInitCount() 
	{
		if (!m_pInitingModelVet)
			return 0;
		return m_pInitingModelVet->size();
	}

	//2015 Yao
	void AddCompon(const std::wstring &modelname, const std::wstring &componentname, int num);

private:
	void RemoveALayer(int layerIDMap[], int layerID, const std::wstring &modelname, const std::wstring &layername);	// �༭��ʹ��

public:
	struct XModel
	{
		//Ⱦɫ��Ϣ
		struct DyeInfo
		{
			int iType;
			int iH;
			int iS;
			int iL;
			int iR;
			int iG;
			int iB;
			int iBright;
			DyeInfo() { iType = 0; iH = 0; iS = 0; iL = 0; iR = 2; iG = 3; iB = 4;iBright=0;}
		};
		typedef std::vector<DyeInfo> PartInfo;
		static const int VERSION = 0;

		XModel(EngineBase *pEB) :m_pEB(pEB), m_nType(0), m_nTitlePos(0), m_effectPos(0, 0), bLoadedAction(false), bLoadedBase(false), bLoadLayerConfig(false),m_dyeType(SpriteManager::e_DyeHSL),m_ndyeEnable(0), m_fScale(1) { }
		~XModel();
		EngineBase *m_pEB;
		std::wstring m_name;
		std::wstring m_des;
		int  m_nType;	//����(����SprineҲ������ϵ��֡)
		int m_nTitlePos;
		CPOINT m_effectPos;
		XPBase m_base;
		WATER_SURFACE_TYPE m_surfaceType;
		ModelSoundParam m_soundParam;
		int m_ndyeEnable;
		SpriteManager::eDyeType	m_dyeType;
		std::map<std::wstring, Action*> m_actions;
		std::map<std::wstring, std::wstring> m_actionrefs; // �����洢�����
		std::vector<Layer> m_layers;
		std::vector<std::map<std::wstring, Component*> > m_Components;
		std::vector<PartInfo> m_DyeInfos;			//Ⱦɫ��������
		int			m_CurDyeIndex;

		std::wstring m_ModelPath;

		bool bLoadedAction, bLoadedBase, bLoadLayerConfig;
		float m_fScale;

		bool LoadDyeInfo();
		bool SaveDyeInfo();

		bool LoadActions();
		bool LoadBase();
		bool LoadLayerConfig();
		bool SaveLayerConfig();
		bool LoadSoundParam();

		int GetLayerCount() const { return (int)m_layers.size(); }
		std::wstring GetLayerName(int i) const;
		std::wstring GetLayerDes(int i) const;
		bool GetLayerRideEnable(int i) const;
		bool GetLayerEffectEnable(int i) const;

		int GetTitlePos() const { return m_nTitlePos; }
		const CPOINT& GetEffectPos() const { return m_effectPos; }
		WATER_SURFACE_TYPE GetSurfaceType() const { return m_surfaceType; }

		ModelSoundParam &GetSoundParam() { return m_soundParam; }
		const ModelSoundParam &GetSoundParam() const { return m_soundParam; }

		const std::wstring &GetActionRef(const std::wstring &actname) const;
		const XPBase &GetBase() const { return m_base; }
		void SetBase(const XPBase& base) { m_base = base; }
		void SetTitlePos(int pos) { m_nTitlePos = pos; }//  �༭��ʹ��
		void SetEffectPos(const CPOINT& pos) { m_effectPos = pos; }//  �༭��ʹ��
		void SetSurfaceType(WATER_SURFACE_TYPE type) { m_surfaceType = type; } //  �༭��ʹ��
		bool SaveBase(); //  �༭��ʹ��
		bool SaveSoundParam(); // �༭��ʹ��

		void DeleteAction2(const std::wstring &name); // �༭��ʹ��
		void RenameAction2(const std::wstring &oldname, const std::wstring &newname, bool renamexap = true); // �༭��ʹ��
		bool AddComponent(const Component &equip); // �༭��ʹ��
		bool DeleteComponent(const Component &equip); // �༭��ʹ��
		Component* GetComponent(int ectype, const std::wstring &name);

		void DumpComponent(int ectype, std::vector<std::wstring> &equiplist) const; // �༭��ʹ�� 
		void RenameComponent(const std::wstring &oldname, const std::wstring &newname); // �༭��ʹ��

		const Action* GetAction(const std::wstring &actName) const;
		void SetAction(const std::wstring &actName, const Action& act); // �༭��ʹ��
		bool SaveActions() const; // �༭��ʹ��
		bool AddAction(const std::wstring &name, const Action &act); // �༭��ʹ��
		bool DeleteAction(const std::wstring &name); // �༭��ʹ��
		bool RenameAction(const std::wstring &oldname, const std::wstring &newname); // �༭��ʹ��
		void DumpAction(std::vector<std::wstring> &actlist) const; // �༭��ʹ�� 
		void DumpBaseAction(std::vector<std::pair<std::wstring,int> > &baseactlist) const; // �༭��ʹ��
		void DumpActionRefMap(std::map<std::wstring,std::wstring> &actrefmap) const; // �༭��ʹ��
		void SetActionRefMap(const std::map<std::wstring,std::wstring> &actrefmap); // �༭��ʹ��

		void AddDyePart(); // �༭��ʹ��
		void DelDyePart(int part); // �༭��ʹ��
		void AddDyeProj(int part); // �༭��ʹ��
		void DelDyeProj(int part,int proj); // �༭��ʹ��
		DyeInfo* GetDyeInfo(int part,int proj);
		int getDyePartCount();	// �༭��ʹ��
		int getDyeProjCount(int part);	// �༭��ʹ��
	};

	XModel* GetXModel(const std::wstring &modelname) const;

private:
	EngineBase *m_pEB;
	typedef std::vector<XModel*> XModelVet;
	typedef std::map<std::wstring, XModel*> ModelMap;
	ModelMap m_models;
	AllHorseHeightMap m_horseHeights;
	ActionSoundTypeMap m_actionSoundMap;
	XModelVet* m_pInitingModelVet;

	// ռ��
private:
	SpriteManager(const SpriteManager&);
	SpriteManager& operator=(const SpriteManager&);

};

}

#endif