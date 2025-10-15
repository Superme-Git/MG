// SoundEditorDoc.h : CSoundEditorDoc 类的接口
//
#pragma once
#include "..\engine\common\pfsutil.h"
#include "..\engine\soundarea\xpenvsoundarea.h"
#include <set>
#include "ToolsSoundArea.h"

const int SE_EDITOR_TIME_A_DAY = GameTime2EditorTime(Nuclear::XP_A_DAY_TIME);

enum SELECT_STATE
{
	SS_SELECTED = 0,		//已经被选中了的
	SS_SELECTED_AREA_POINT,	//已经选中了的区域的一个点
	SS_SELECTED_AREA_LINE,	//已经选中了的区域的一条线
	SS_NEWSELECTED,			//选中了个新的
	SS_ENPTY,				//选了个空地方
};

enum POLYGON_MOUSE_STATE
{
	PMS_NORMAL = 0,
	PMS_DRAGGING,
	PMS_MOVING_POINT,
	PMS_CUTTING
};

enum UPDATE_VIEW_SIGN
{
	UVS_LOADMAP = 1,						//					 	  1
	UVS_TAB = 2,							//					 	 10
	UVS_VIEWPORT = 4,						//					    100
	UVS_ENV_SOUND_AREA_SELECTED = 8,		//					   1000
	UVS_ENV_SOUND_AREA_SHAPE = 16,			//					 1 0000
	UVS_ENV_SOUND_AREA_ADD_DELETED = 32,	//				    10 0000
	UVS_BKG_SOUND_AREA_SELECTED = 64,		//				   100 0000
	UVS_BKG_SOUND_AREA_SHAPE = 128,			//			 	  1000 0000
	UVS_BKG_SOUND_AREA_ADD_DELETED = 256,	//			    1 0000 0000
	UVS_TEST_SET_POS = 512,					//			   10 0000 0000
	UVS_TEST_SET_DARK = 1024,				//			  100 0000 0000
	UVS_TEST_SET_GAMETIME_CYCLE = 2048,		//			 1000 0000 0000
	UVS_TEST_SET_GAMETIME = 4096,			//		   1 0000 0000 0000
	UVS_TEST_SET_VOLUME = 8192,				//		  10 0000 0000 0000
	UVS_TEST_SET_SPRITE = 16384,			//		 100 0000 0000 0000
	UVS_BKV_SOUND_AREA_SELECTED = 32768,	//		1000 0000 0000 0000
	UVS_BKV_SOUND_AREA_SHAPE = 65536,		//	  1 0000 0000 0000 0000
	UVS_BKV_SOUND_AREA_ADD_DELETED = 131072,//	 10 0000 0000 0000 0000
	UVS_ALL = 0xFFFFFFFF,
};

enum UPDATE_SOURCE
{
	US_DEFAULT = -1,
	US_ENGINE = 0,
	US_PANEAL,
	US_UNDOREDO,
};

typedef std::set<CToolsEnvSoundArea*> EnvAreaCont;
typedef std::set<CToolsBkgSoundArea*> BkgAreaCont;
typedef std::set<CToolsBkgVolumeArea*> BkgVolAreaCont;
typedef std::map<DWORD_PTR, CToolsEnvSoundArea*> EnvSoundMap;
typedef std::map<DWORD_PTR, CToolsBkgSoundArea*> BkgSoundMap;

typedef struct _SoundInfo
{
	UINT nID;
	DWORD_PTR handle;
} SoundInfo;
class CActionList;
struct AddDelEnvType;
struct CAction;
namespace Nuclear 
{
	class EngineBase;
	class PStepSoundMap;
}
class CSoundEditorDoc : public CDocument
{
protected: // 仅从序列化创建
	CSoundEditorDoc();
	DECLARE_DYNCREATE(CSoundEditorDoc)
//自定义类型们
public:
	friend class CActionList;

// 属性
private:
	EDIT_STATE m_editState;
	EnvAreaCont m_envAreas;
	BkgAreaCont m_bkgAreas;
	BkgVolAreaCont m_bkgVolAreas;
	Nuclear::CPOINT m_mapSize;
	UPDATE_SOURCE m_us;
	int m_nGameTimeCycle;	//天黑效果的周期（毫秒）
	float m_fGameTime;		//天黑效果的时间（0~1)
	DWORD_PTR m_nHandleSeed;	//没有必要分开Env和Bkg，用不完
	int m_nEnvOrdersSeed;
	int m_nEnvSoundCount;
	int m_nBkgOrdersSeed;
	int m_nBkgSoundCount;
	EnvSoundMap m_envSoundMap;
	BkgSoundMap m_bkgSoundMap;	
	CToolsSoundArea::HandleVet m_handleVet;
	CActionList *m_pOperatorList;
	Nuclear::PStepSoundMap *m_pSSM;
	bool m_bEnableEnv;
	bool m_bEnableBkg;
	unsigned char m_nEnvVolume;
	unsigned char m_nBkgVolume;
	Nuclear::EngineBase *m_pEB;
	std::wstring m_modelname;
	std::wstring m_component[8];
	std::wstring m_actRunLeft;
	std::wstring m_actRunRight;
	std::wstring m_actStand;
	std::wstring m_rideName;
	bool m_bSSMModified;	//脚步文件有没有被修改
	std::set<int> m_bStepHiddent;
	CToolsEnvSoundArea* m_pCopiedEnvArea;
	CToolsBkgSoundArea* m_pCopiedBkgArea;
	CToolsBkgVolumeArea* m_pCopiedBkgVolArea;

public:
	std::wstring m_mazeName;
	std::wstring m_mapName;
	CString m_strMiniMapURL;
	CString m_strSSMURL;
	CString m_lastEnvOpenUrl;
	CString m_lastBkgOpenUrl;
	Nuclear::XPIShape *m_pSelectedShape;
	CToolsEnvSoundArea* m_pSelectedEnvArea;
	CToolsBkgSoundArea* m_pSelectedBkgArea;
	CToolsBkgVolumeArea* m_pSelectedBkgVolArea;
	CToolsBkgDefaultSoundArea m_defaultBkgArea;
	POINT m_movingSelectArea;
	int m_nSelectedPt;
	int m_nPolygonEditState;//0 none 1 base 2 cut
	POLYGON_MOUSE_STATE m_PolygonMouseState;
	RECT m_CutingPolygonLine;
	bool m_bTestingEnableDark;		//是否在测试的时候开启天黑效果
	BOOL m_bTestingDrawEnvArea;
	BOOL m_bTestingDrawBkgArea;
	BOOL m_bTestingDrawBkgVolArea;
	BOOL m_bTestingDrawStep;
	BOOL m_bDrawMaze;
	unsigned int m_SpriteMazeMask;
	int m_nUpdateViewSign;
	CPoint m_viewportlt;

	CAction* m_pCurOperator;
	int m_nCurOperatorType;//是redo 还是 undo
	int m_nStepBrush;
	int m_nBrushSize;


// 操作
private:
	void FreeAll();
	void LegitimateViewportLt(POINT &pt);
	bool SaveSSM();
	bool LoadSSM();
	bool CanCopy();
	
public:
	static void SetShape(CToolsSoundArea* pArea, const Nuclear::XPIShape* pShape);
	
	void Reload();
	void ResetSetMazeName();
	void SetPEB(Nuclear::EngineBase* pEB);
	Nuclear::EngineBase* GetEngineBase() const { return m_pEB; }
	Nuclear::PStepSoundMap *CloneSSM() const;
	const Nuclear::PStepSoundMap *GetSSM() const { return m_pSSM; }
	void SetStepVisable(int id, bool visable) 
	{ 
		if (visable)
			m_bStepHiddent.erase(id);
		else
			m_bStepHiddent.insert(id);
	}
	bool IsStepVisable(int id) const { return m_bStepHiddent.find(id) == m_bStepHiddent.end(); }

	//精灵相关
	const std::wstring& GetModelName() const { return m_modelname; }
	void SetModel(const std::wstring &name);
	//ec >= 0 && ec < 8
	const std::wstring& GetComponent(int ec) const;
	void SetComponent(int ec, const std::wstring &name);
	const std::wstring& GetRunLeftAct() const { return m_actRunLeft; }
	const std::wstring& GetRunRightAct() const { return m_actRunRight; }
	const std::wstring& GetStandAct() const { return m_actStand; }
	void SetRunLeftAct(const std::wstring &name);
	void SetRunRightAct(const std::wstring &name);
	void SetStandAct(const std::wstring &name);
	const std::wstring& GetRideName() const { return m_rideName; }
	void SetRidename(const std::wstring &name);
	int GetCurSpriteLayerCount() const;
	void LoadSpriteCfg();
	void SaveSpriteCfg();
	//精灵相关结束

	const CToolsSoundArea::HandleVet& GetDeletedHandleVet() const { return m_handleVet; }
	void SetEnvVolume(unsigned char v, UPDATE_SOURCE s);//[0, 255]
	unsigned char GetEnvVolume() const { return m_nEnvVolume; }//[0, 255]
	void EnableEnv(bool b, UPDATE_SOURCE s);
	bool IsEnvEnable() const { return m_bEnableEnv; }
	void SetBkgVolume(unsigned char v, UPDATE_SOURCE s);//[0, 255]
	unsigned char GetBkgVolume() const { return m_nBkgVolume; }//[0, 255]
	void EnableBkg(bool b, UPDATE_SOURCE s);
	bool IsBkgEnable() const { return m_bEnableBkg; }
	void SetGameTime(float time, UPDATE_SOURCE s);
	float GetGameTime() const { return m_fGameTime; }
	void SetGameTimeCycle(int cycle);
	int GetGameTimeCycle() const { return m_nGameTimeCycle; }
	UPDATE_SOURCE GetUpdateSource() const { return m_us; }
	void UpdateViewport(POINT pt, UPDATE_SOURCE m_us);
	CPoint GetViewport() const { return m_viewportlt; }
	void SetMapSize(const Nuclear::CPOINT &size);
	int GetMapWidth() const { return m_mapSize.x; }
	int GetMapHeight() const { return m_mapSize.y; }
	EDIT_STATE GetEditState() { return m_editState; }
	void SetEditState(EDIT_STATE state) { m_editState = state; }

	const EnvAreaCont& GetEnvArea() const { return m_envAreas; }
	const BkgAreaCont& GetBkgArea() const { return m_bkgAreas; }
	const BkgVolAreaCont& GetBkgVolArea() const { return m_bkgVolAreas; }
	int GetEnvSoundCount() const { return m_nEnvSoundCount; }
	int GetBkgSoundCount() const { return m_nBkgSoundCount; }
	void SaveAsXML(XMLIO::CONode &root, int flag);
	void ResetAreaRegion(CToolsSoundArea* pArea);
	SELECT_STATE CheckSelectEnvArea(const CPoint &pt);//世界坐标
	SELECT_STATE CheckSelectBkgArea(const CPoint &pt);//世界坐标
	SELECT_STATE CheckSelectBkgVolArea(const CPoint &pt);//世界坐标
	void ModifyEnvAreaSoundOrders(int order, int value);
	void ModifyBkgAreaSoundOrders(int order, int value);

	void SelectAndMoveToAnArea(CToolsEnvSoundArea* pArea);
	void SelectAndMoveToAnEnvArea(DWORD_PTR handle);
	void SelectAndMoveToAnArea(CToolsBkgSoundArea* pArea);
	void SelectAndMoveToAnBkgArea(DWORD_PTR handle);

	//要Undo Redo
	void BeginSetStepSound();
	void SetStepSound(RECT rect, int gid);//像素坐标
	void EndSetStepSound();
	bool ClearPolygonCuts(CToolsSoundArea* pArea);
	void AddAnEnvArea(CToolsEnvSoundArea* area);
	void AddAnBkgArea(CToolsBkgSoundArea* area);
	void AddAnBkgVolArea(CToolsBkgVolumeArea* area);
	void SetSelectedAreaMaxVol(int typeID, int soundID, unsigned char v);	//环境音
	void SetSelectedAreaMinVol(int typeID, int soundID, unsigned char v);	//环境音
	void SetSelectedAreaMaxFreq(int typeID, int soundID, unsigned char f);	//环境音
	void SetSelectedAreaMinFreq(int typeID, int soundID, unsigned char f);	//环境音
	void SetSelectedAreaTime(int typeID, int soundID, int type, int time);	//环境音
	void SetSelectedAreaLooping(int typeID, int soundID, bool loop);		//环境音
	void SetSelectedAreaSoundType(int typeID, int soundID, unsigned char type);	//环境音
	void SetSelectedAreaMinInterval(int typeID, int interval);				//环境音
	void SetSelectedAreaMaxInterval(int typeID, int interval);				//环境音
	void SetSelectedAreaPriority(int typeID, short priority);				//环境音
	void SetSelectedAreaVol(int soundID, int v);	//背景音
	void SetSelectedAreaSoundType(int soundID,  Nuclear::XPSA_SOUND_TYPE type);	//背景音
	int AddSound2SelectedArea(int typeID, const std::wstring &name, __out SoundInfo &info);	//环境音
	void DeleteSelectedAreaSound(int typeID, int soundID, UINT nAllSoundListID);	//环境音
	int AddSound2SelectedArea(const std::wstring &name, __out SoundInfo &info);	//背景音
	void DeleteSelectedAreaSound(int soundID, UINT nAllSoundListID);	//背景音
	void SetSelectedAreaMinInterval(int interval);				//背景音
	void SetSelectedAreaMaxInterval(int interval);				//背景音
	void SetSelectedAreaFadeIn(int f);							//背景音
	void SetSelectedAreaFadeOut(int f);							//背景音
	void SetSelectedAreaWaitPrevArea(bool b);					//背景音
	void SetSelectedAreaPlayMode(Nuclear::XPBGM_PLAY_TYPE mode);//背景音
	void DeleteSelectedArea();
	int AddSoundType2SelectedArea();
	void DeleteSoundTypeFromSelectedArea(int typeID, AddDelEnvType* &pOper);
	void MoveSelectedArea();
	void SetSelectedAreaTransition(int nTransition);
	void DeleteSelectedPolygonAreaPoint(int pt);
	void MoveSelectedPolygonAreaPointTo(const POINT &pt);
	void AddSelectedEnvPolygonPoint(const POINT &pt);
	void AddSelectedBkgPolygonPoint(const POINT &pt);
	void AddSelectedBkgVolPolygonPoint(const POINT &pt);
	void CutSelectedPolygon(int pt1, int pt2);
	void SetSelectedCircleAreaRadius(int r);
	void SetSelectedBkgVolAreaVol(float v);


// 重写
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

protected:
	virtual BOOL SaveModified();

// 实现
public:
	virtual ~CSoundEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	afx_msg void OnEditRedo();
	afx_msg void OnUpdateEditRedo(CCmdUI *pCmdUI);
	afx_msg void OnEditUndo();
	afx_msg void OnUpdateEditUndo(CCmdUI *pCmdUI);
	afx_msg void OnEditCopy();
	afx_msg void OnUpdateEditCopy(CCmdUI *pCmdUI);
	afx_msg void OnEditCut();
	afx_msg void OnUpdateEditCut(CCmdUI *pCmdUI);
	afx_msg void OnEditPaste();
	afx_msg void OnUpdateEditPaste(CCmdUI *pCmdUI);
};


