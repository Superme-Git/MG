// SoundEditorDoc.h : CSoundEditorDoc ��Ľӿ�
//
#pragma once
#include "..\engine\common\pfsutil.h"
#include "..\engine\soundarea\xpenvsoundarea.h"
#include <set>
#include "ToolsSoundArea.h"

const int SE_EDITOR_TIME_A_DAY = GameTime2EditorTime(Nuclear::XP_A_DAY_TIME);

enum SELECT_STATE
{
	SS_SELECTED = 0,		//�Ѿ���ѡ���˵�
	SS_SELECTED_AREA_POINT,	//�Ѿ�ѡ���˵������һ����
	SS_SELECTED_AREA_LINE,	//�Ѿ�ѡ���˵������һ����
	SS_NEWSELECTED,			//ѡ���˸��µ�
	SS_ENPTY,				//ѡ�˸��յط�
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
protected: // �������л�����
	CSoundEditorDoc();
	DECLARE_DYNCREATE(CSoundEditorDoc)
//�Զ���������
public:
	friend class CActionList;

// ����
private:
	EDIT_STATE m_editState;
	EnvAreaCont m_envAreas;
	BkgAreaCont m_bkgAreas;
	BkgVolAreaCont m_bkgVolAreas;
	Nuclear::CPOINT m_mapSize;
	UPDATE_SOURCE m_us;
	int m_nGameTimeCycle;	//���Ч�������ڣ����룩
	float m_fGameTime;		//���Ч����ʱ�䣨0~1)
	DWORD_PTR m_nHandleSeed;	//û�б�Ҫ�ֿ�Env��Bkg���ò���
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
	bool m_bSSMModified;	//�Ų��ļ���û�б��޸�
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
	bool m_bTestingEnableDark;		//�Ƿ��ڲ��Ե�ʱ�������Ч��
	BOOL m_bTestingDrawEnvArea;
	BOOL m_bTestingDrawBkgArea;
	BOOL m_bTestingDrawBkgVolArea;
	BOOL m_bTestingDrawStep;
	BOOL m_bDrawMaze;
	unsigned int m_SpriteMazeMask;
	int m_nUpdateViewSign;
	CPoint m_viewportlt;

	CAction* m_pCurOperator;
	int m_nCurOperatorType;//��redo ���� undo
	int m_nStepBrush;
	int m_nBrushSize;


// ����
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

	//�������
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
	//������ؽ���

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
	SELECT_STATE CheckSelectEnvArea(const CPoint &pt);//��������
	SELECT_STATE CheckSelectBkgArea(const CPoint &pt);//��������
	SELECT_STATE CheckSelectBkgVolArea(const CPoint &pt);//��������
	void ModifyEnvAreaSoundOrders(int order, int value);
	void ModifyBkgAreaSoundOrders(int order, int value);

	void SelectAndMoveToAnArea(CToolsEnvSoundArea* pArea);
	void SelectAndMoveToAnEnvArea(DWORD_PTR handle);
	void SelectAndMoveToAnArea(CToolsBkgSoundArea* pArea);
	void SelectAndMoveToAnBkgArea(DWORD_PTR handle);

	//ҪUndo Redo
	void BeginSetStepSound();
	void SetStepSound(RECT rect, int gid);//��������
	void EndSetStepSound();
	bool ClearPolygonCuts(CToolsSoundArea* pArea);
	void AddAnEnvArea(CToolsEnvSoundArea* area);
	void AddAnBkgArea(CToolsBkgSoundArea* area);
	void AddAnBkgVolArea(CToolsBkgVolumeArea* area);
	void SetSelectedAreaMaxVol(int typeID, int soundID, unsigned char v);	//������
	void SetSelectedAreaMinVol(int typeID, int soundID, unsigned char v);	//������
	void SetSelectedAreaMaxFreq(int typeID, int soundID, unsigned char f);	//������
	void SetSelectedAreaMinFreq(int typeID, int soundID, unsigned char f);	//������
	void SetSelectedAreaTime(int typeID, int soundID, int type, int time);	//������
	void SetSelectedAreaLooping(int typeID, int soundID, bool loop);		//������
	void SetSelectedAreaSoundType(int typeID, int soundID, unsigned char type);	//������
	void SetSelectedAreaMinInterval(int typeID, int interval);				//������
	void SetSelectedAreaMaxInterval(int typeID, int interval);				//������
	void SetSelectedAreaPriority(int typeID, short priority);				//������
	void SetSelectedAreaVol(int soundID, int v);	//������
	void SetSelectedAreaSoundType(int soundID,  Nuclear::XPSA_SOUND_TYPE type);	//������
	int AddSound2SelectedArea(int typeID, const std::wstring &name, __out SoundInfo &info);	//������
	void DeleteSelectedAreaSound(int typeID, int soundID, UINT nAllSoundListID);	//������
	int AddSound2SelectedArea(const std::wstring &name, __out SoundInfo &info);	//������
	void DeleteSelectedAreaSound(int soundID, UINT nAllSoundListID);	//������
	void SetSelectedAreaMinInterval(int interval);				//������
	void SetSelectedAreaMaxInterval(int interval);				//������
	void SetSelectedAreaFadeIn(int f);							//������
	void SetSelectedAreaFadeOut(int f);							//������
	void SetSelectedAreaWaitPrevArea(bool b);					//������
	void SetSelectedAreaPlayMode(Nuclear::XPBGM_PLAY_TYPE mode);//������
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


// ��д
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

protected:
	virtual BOOL SaveModified();

// ʵ��
public:
	virtual ~CSoundEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ���ɵ���Ϣӳ�亯��
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


