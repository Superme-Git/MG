#pragma once
#include <list>

#include "..\engine\soundarea\xpsoundarea.h"
#include "..\engine\soundarea\xpenvsoundarea.h"
#include "..\engine\soundarea\xpbkgsoundarea.h"
#include "..\engine\map\pstepsoundmap.h"
#include "ToolsSoundArea.h"

class CSoundEditorDoc;
class CToolsEnvSoundArea;
class CToolsBkgSoundArea;

typedef std::map<DWORD_PTR, UINT> ALL_SOUND_LIST_ID_MAP;

struct CAction
{	
	//枚举们 自定义类型们
public:
	enum
	{
		BEFORE_ACTION = 0,
		AFTER_ACTION = 1
	};
	enum eActionType
	{
		ADD_DEL_ENV_AREA = 0,
		ADD_DEL_BKG_AREA,
		ADD_DEL_BKG_VOL_AREA,
		ENV_SOUND_PROP,
		ENV_SOUND_TIME,
		ENV_SOUND_LOOPING,
		ENV_SOUND_INTERVAL,
		ENV_SOUND_PRIORITY,
		BKG_SOUND_VOLUME,
		BKG_SOUND_TYPE,
		BKG_SOUND_INTERVAL,
		BKG_SOUND_FADETIME,
		BKG_SOUND_WAIT_PREV_AREA,
		BKG_SOUND_PLAY_MODE,
		ADD_DEL_ENV_SOUND,
		ADD_DEL_BKG_SOUND,
		ADD_DEL_ENV_TYPE,
		MOV_AREA,
		ENV_AREA_TRANSITION,
		BKV_AREA_TRANSITION,
		POLYGON_MODIFY,	//偷个懒，将整个多边形记录下来，就不管是添加、删除还是移动点了
		CIRCLE_MODIFY,	//
		STEP_SOUND_AREA,
		BKV_AREA_VOL,
	};

public:
	CAction();
	virtual ~CAction();

	virtual eActionType GetType() const = 0;
	virtual EDIT_STATE GetTabEditType() const = 0;
	virtual int GetTypeID(int type) const { return  -1; }
	virtual int GetSoundID(int type) const { return -1; }
};

struct AddDelEnvArea : public CAction	//add 已经做了的和非add没有做的不用delete，add没有做的和非add已经做了的要delete
{
	CToolsEnvSoundArea *pArea;
	ALL_SOUND_LIST_ID_MAP allSoundListIDMap;
	bool isAdd;
	AddDelEnvArea(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	bool IsCurrentAdd(int type) const { return (type==CAction::BEFORE_ACTION?(!isAdd):isAdd);}
	virtual eActionType GetType() const { return ADD_DEL_ENV_AREA; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
};

struct AddDelBkgArea : public CAction	//add 已经做了的和非add没有做的不用delete，add没有做的和非add已经做了的要delete
{
	CToolsBkgSoundArea *pArea;
	ALL_SOUND_LIST_ID_MAP allSoundListIDMap;
	bool isAdd;
	AddDelBkgArea(CToolsBkgSoundArea *_pArea) : pArea(_pArea) {}
	bool IsCurrentAdd(int type) const { return (type==CAction::BEFORE_ACTION?(!isAdd):isAdd);}
	virtual eActionType GetType() const { return ADD_DEL_BKG_AREA; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_MUSIC; }
};

struct AddDelBkgVolArea : public CAction	//add 已经做了的和非add没有做的不用delete，add没有做的和非add已经做了的要delete
{
	CToolsBkgVolumeArea *pArea;
	bool isAdd;
	AddDelBkgVolArea(CToolsBkgVolumeArea *_pArea) : pArea(_pArea) {}
	bool IsCurrentAdd(int type) const { return (type==CAction::BEFORE_ACTION?(!isAdd):isAdd);}
	virtual eActionType GetType() const { return ADD_DEL_BKG_VOL_AREA; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_VOL; }
};

struct EnvSoundProp : public CAction
{
	enum PROP_TYPE
	{
		MAX_VOL,
		MIN_VOL,
		MAX_FREQ,
		MIN_FREQ,
		SOUND_TYPE,
	};
	CToolsEnvSoundArea *pArea;
	int typeID;
	int soundID;
	PROP_TYPE type;
	unsigned char data[2];
	EnvSoundProp(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return ENV_SOUND_PROP; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
	virtual int GetTypeID(int type) const { return  typeID; }
	virtual int GetSoundID(int type) const { return soundID; }
};

struct EnvSoundTime : public CAction
{
	CToolsEnvSoundArea *pArea;
	int typeID;
	int soundID;
	int type;
	int time[2];
	EnvSoundTime(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return ENV_SOUND_TIME; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
	virtual int GetTypeID(int type) const { return  typeID; }
	virtual int GetSoundID(int type) const { return soundID; }
};

struct EnvSoundLoop : public CAction
{
	CToolsEnvSoundArea *pArea;
	int typeID;
	int soundID;
	bool loop[2];
	EnvSoundLoop(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return ENV_SOUND_LOOPING; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
	virtual int GetTypeID(int type) const { return  typeID; }
	virtual int GetSoundID(int type) const { return soundID; }
};

struct EnvSoundInterval : public CAction
{
	enum INTERVAL_TYPE
	{
		MAX,
		MIN,
	};
	CToolsEnvSoundArea *pArea;
	int typeID;
	INTERVAL_TYPE type;
	int interval[2];
	EnvSoundInterval(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return ENV_SOUND_INTERVAL; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
	virtual int GetTypeID(int type) const { return  typeID; }
};

struct EnvSoundPriority : public CAction
{
	CToolsEnvSoundArea *pArea;
	int typeID;
	short priority[2];
	EnvSoundPriority(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return ENV_SOUND_PRIORITY; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
	virtual int GetTypeID(int type) const { return  typeID; }
};

struct BkgSoundVolume : public CAction
{
	CToolsBkgSoundArea *pArea;
	int soundID;
	int vol[2];
	BkgSoundVolume(CToolsBkgSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return BKG_SOUND_VOLUME; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_MUSIC; }
	virtual int GetSoundID(int type) const { return soundID; }
};

struct BkgSoundType : public CAction
{
	CToolsBkgSoundArea *pArea;
	int soundID;
	Nuclear::XPSA_SOUND_TYPE type[2];
	BkgSoundType(CToolsBkgSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return BKG_SOUND_TYPE; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_MUSIC; }
	virtual int GetSoundID(int type) const { return soundID; }
};

struct BkgSoundInterval : public CAction
{
	enum INTERVAL_TYPE
	{
		MAX,
		MIN,
	};
	CToolsBkgSoundArea *pArea;
	INTERVAL_TYPE type;
	int interval[2];
	BkgSoundInterval(CToolsBkgSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return BKG_SOUND_INTERVAL; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_MUSIC; }
};

struct BkgSoundFadeTime : public CAction
{
	enum FADE_TYPE
	{
		T_IN,
		T_OUT,
	};
	CToolsBkgSoundArea *pArea;
	FADE_TYPE type;
	int fadeTime[2];
	BkgSoundFadeTime(CToolsBkgSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return BKG_SOUND_FADETIME; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_MUSIC; }
};

struct BkgSoundWaitPrevArea : public CAction
{
	CToolsBkgSoundArea *pArea;
	bool wait[2];
	BkgSoundWaitPrevArea(CToolsBkgSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return BKG_SOUND_WAIT_PREV_AREA; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_MUSIC; }
};

struct BkgSoundPlayMode : public CAction
{
	CToolsBkgSoundArea *pArea;
	Nuclear::XPBGM_PLAY_TYPE type[2];
	BkgSoundPlayMode(CToolsBkgSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return BKG_SOUND_PLAY_MODE; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_MUSIC; }
};

struct AddDelEnvSound : public CAction
{
	CToolsEnvSoundArea *pArea;
	int typeID;
	int soundID;
	UINT nAllSoundListID;
	Nuclear::XPEnvSoundArea::SoundParam param;
	DWORD_PTR handle;
	bool isAdd;
	AddDelEnvSound(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	bool IsCurrentAdd(int type) const { return (type==CAction::BEFORE_ACTION?(!isAdd):isAdd);}
	virtual eActionType GetType() const { return ADD_DEL_ENV_SOUND; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
	virtual int GetTypeID(int type) const { return  typeID; }
	virtual int GetSoundID(int type) const { return IsCurrentAdd(type)?soundID:-1;}
};

struct AddDelBkgSound : public CAction
{
	CToolsBkgSoundArea *pArea;
	int soundID;
	UINT nAllSoundListID;
	Nuclear::XPBkgSoundArea::SoundParam param;
	DWORD_PTR handle;
	bool isAdd;
	AddDelBkgSound(CToolsBkgSoundArea *_pArea) : pArea(_pArea) {}
	bool IsCurrentAdd(int type) const { return (type==CAction::BEFORE_ACTION?(!isAdd):isAdd);}
	virtual eActionType GetType() const { return ADD_DEL_BKG_SOUND; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_MUSIC; }
	virtual int GetSoundID(int type) const { return IsCurrentAdd(type)?soundID:-1;}
};

struct AddDelEnvType : public CAction
{
	CToolsEnvSoundArea *pArea;
	int typeID;
	ALL_SOUND_LIST_ID_MAP allSoundListIDMap;
	CToolsSoundArea::HandleVet soundHandles;
	bool isAdd;
	Nuclear::XPEnvSoundArea::SoundTypeParam param;
	AddDelEnvType(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	bool IsCurrentAdd(int type) const { return (type==CAction::BEFORE_ACTION?(!isAdd):isAdd);}
	virtual eActionType GetType() const { return ADD_DEL_ENV_TYPE; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
};

struct MovArea : public CAction
{
	CToolsSoundArea *pArea;
	POINT vet;
	EDIT_STATE state;
	MovArea(CToolsSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return MOV_AREA; }
	virtual EDIT_STATE GetTabEditType() const { return state; }
};

struct EnvAreaTransition : public CAction
{
	CToolsEnvSoundArea *pArea;
	int trans[2];
	EnvAreaTransition(CToolsEnvSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return ENV_AREA_TRANSITION; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_ENV_SOUND; }
};

struct BkvAreaTransition : public CAction
{
	CToolsBkgVolumeArea *pArea;
	int trans[2];
	BkvAreaTransition(CToolsBkgVolumeArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return BKV_AREA_TRANSITION; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_VOL; }
};

struct PolygonModify : public CAction
{
	CToolsSoundArea *pArea;
	Nuclear::XPPolygon polygon[2];
	CToolsSoundArea::CutPairCont  cutpair[2];
	EDIT_STATE state;
	PolygonModify(CToolsSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return POLYGON_MODIFY; }
	virtual EDIT_STATE GetTabEditType() const { return state; }
};

struct CircleModify : public CAction
{
	CToolsSoundArea *pArea;
	int radius[2];
	EDIT_STATE state;
	CircleModify(CToolsSoundArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return CIRCLE_MODIFY; }
	virtual EDIT_STATE GetTabEditType() const { return state; }
};

struct StepSoundArea : public CAction
{
	Nuclear::StepSoundGT newdata;
	std::vector<std::pair<int, Nuclear::StepSoundGT>> olddata;
	StepSoundArea() {}
	virtual eActionType GetType() const { return STEP_SOUND_AREA; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_STEP; }
};

struct BkvAreaVolume : public CAction
{
	CToolsBkgVolumeArea *pArea;
	float vol[2];
	BkvAreaVolume(CToolsBkgVolumeArea *_pArea) : pArea(_pArea) {}
	virtual eActionType GetType() const { return BKV_AREA_VOL; }
	virtual EDIT_STATE GetTabEditType() const { return EDIT_STATE_BKG_VOL; }
};

class CActionList
{
	//数据们
private:
	std::list<CAction*> m_ActionList;
	std::list<CAction*>::iterator m_Iter;
	CSoundEditorDoc* m_pDoc;
public:
	static int const MAX_LENGTH = 50;

	//方法们
private:
	bool SetAddDelEnvArea(int type);
	bool SetAddDelBkgArea(int type);
	bool SetAddDelBkgVolArea(int type);
	bool SetEnvSoundProp(int type);
	bool SetEnvSoundTime(int type);
	bool SetEnvSoundLoop(int type);
	bool SetEnvSoundInterval(int type);
	bool SetEnvSoundPriority(int type);
	bool SetBkgSoundVolume(int type);
	bool SetBkgSoundType(int type);
	bool SetBkgSoundInterval(int type);
	bool SetBkgSoundFadeTime(int type);
	bool SetBkgSoundWaitPrevArea(int type);
	bool SetBkgSoundPlayMode(int type);
	bool SetAddDelEnvSound(int type);
	bool SetAddDelBkgSound(int type);
	bool SetAddDelEnvType(int type);
	bool SetMovArea(int type);
	bool SetEnvAreaTransition(int type);
	bool SetBkvAreaTransition(int type);
	bool SetPolygonModify(int type);
	bool SetCircleModify(int type);
	bool SetStepSoundArea(int type);
	bool SetBkvAreaVolume(int type);

	bool TodoAction(int type);
	void SetDocSelectedArea(CToolsBkgSoundArea *pArea);
public:
	CActionList(CSoundEditorDoc* pDoc);
	virtual ~CActionList();
	void DoOneAction(CAction* pOper);
	bool UndoOneAction();
	bool RedoOneAction();
	void CleanAll();
	bool CanUndo()
	{
		if (m_Iter == m_ActionList.begin())
		{
			return false;
		} else {
			return true;
		}
	}
	bool CanRedo()
	{
		if (m_Iter == m_ActionList.end())
		{
			return false;
		} else {
			return true;
		}
	}
};