#pragma once
#include <list>
#include "..\engine\effect\geffect.h"
#include "..\engine\effect\xapeffect.h"

class CEffectEditorDoc;

typedef std::set<Nuclear::AbstractEffectClip*> EffectClipSet;
typedef std::map<int, Nuclear::FPOINT> DoubleKeyMap;
typedef std::map<int, float> SingleKeyMap;
typedef std::map<int, Nuclear::XPCOLOR> ColorKeyMap;

class CAction
{	

public:
	enum
	{
		BEFORE_ACTION = 0,
		AFTER_ACTION = 1
	};
	enum eActionType
	{
		AT_MODIFY_START_END_FRAME,
		AT_MODIFY_LAYER,
		AT_MODIFY_CLIP_ANI,
		AT_SET_DIVIDE_LAYER,
		AT_SET_TOTAL_FRAME,
		AT_SET_FPS,
		AT_SET_BIND_TYPE,
		AT_SET_BOUNDS_RECT,
		AT_DEL_ADD_CLIP,
		AT_SET_CLIP_POS,
		AT_SET_CLIP_FPS,
		AT_SET_CLIP_VERTEX_COLOR,
		AT_SET_CLIP_PLAY_MODE_TIME,
		AT_SET_CLIPS_DOUBLE_KEY,
		AT_SET_CLIP_SINGLE_KEY,
		AT_SET_CLIP_COLOR_KEY,

	};

public:
	virtual eActionType GetType() = 0;
	virtual ~CAction();
	CAction();
};

class CActionSetClipColorKeys : public CAction
{
public:
	Nuclear::AbstractEffectClip* m_pClip;
	ColorKeyMap m_KeyMaps[2];
	virtual eActionType GetType() { return AT_SET_CLIP_COLOR_KEY; }
};

class CActionSetClipsDoubleKeys : public CAction
{
public:
	Nuclear::AbstractEffectClip::KeyType m_KeyType;
	std::vector<Nuclear::AbstractEffectClip*> m_pClips;
	std::vector<Nuclear::AbstractEffectClip::KP_VECTOR> m_KeyPoints[2];
	std::vector<Nuclear::AbstractEffectClip::SP_VECTOR> m_SamplePoints[2];
	std::vector<DoubleKeyMap> m_KeyMaps[2];
	virtual eActionType GetType() { return AT_SET_CLIPS_DOUBLE_KEY; }
	void AddAnContaints(int type, Nuclear::AbstractEffectClip* pClip, int &firstFrame, int &lastFrame)
	{
		if (type == CAction::BEFORE_ACTION)
		{
			m_pClips.push_back(pClip);
		}
		m_KeyPoints[type].push_back(pClip->GetToolsKeyPoints(m_KeyType));
		m_SamplePoints[type].push_back(pClip->GetToolsSamplePoints(m_KeyType));
		DoubleKeyMap tmpKeyMap;
		m_KeyMaps[type].push_back(tmpKeyMap);
		DoubleKeyMap& keyMap = m_KeyMaps[type].back();
		int frame;
		Nuclear::FPOINT v;
		firstFrame = INT_MAX;
		lastFrame = -1;
		for (unsigned int i=0;i<pClip->GetKeyCount(m_KeyType);++i)
		{
			if (m_KeyType == Nuclear::AbstractEffectClip::KT_POS)
			{
				pClip->GetPosKeyValue(i, frame, v);
			} else {
				pClip->GetScaleKeyValue(i, frame, v);
			}
			keyMap[frame] = v;
			if (i == 0)
			{
				firstFrame = frame;
			}
			lastFrame = frame;
		}

	}
};

class CActionSetClipSingleKeys : public CAction
{
public:
	Nuclear::AbstractEffectClip* m_pClip;
	Nuclear::AbstractEffectClip::KeyType m_KeyType;
	Nuclear::AbstractEffectClip::KP_VECTOR m_KeyPoints[2];
	Nuclear::AbstractEffectClip::SP_VECTOR m_SamplePoints[2];
	SingleKeyMap m_KeyMaps[2];
	virtual eActionType GetType() { return AT_SET_CLIP_SINGLE_KEY; }
};

class CActionSetClipPlayModeAndTime : public CAction
{
public:
	Nuclear::Effect* m_pEffect;
	Nuclear::XPPLAY_MODE m_Types[2];
	int m_nTimes[2];
	virtual eActionType GetType() { return AT_SET_CLIP_PLAY_MODE_TIME; }
};

class CActionSetClipVertexColor : public CAction
{
public:
	Nuclear::Effect* m_pEffect;
	Nuclear::XPCOLOR m_Colors[2];
	virtual eActionType GetType() { return AT_SET_CLIP_VERTEX_COLOR; }
};

class CActionSetClipFps : public CAction
{
public:
	Nuclear::XapEffect* m_pEffect;
	float m_fFps[2];
	virtual eActionType GetType() { return AT_SET_CLIP_FPS; }
};

class CActionSetClipPos : public CAction
{
public:
	typedef std::map<Nuclear::AbstractEffectClip*, Nuclear::CPOINT> ClipPos;
	ClipPos m_ClipPositions[2];
	virtual eActionType GetType() { return AT_SET_CLIP_POS; }
};

class CActionDelAddClip : public CAction
{
public:
	Nuclear::AbstractEffectClip* m_pClips[2];
	virtual eActionType GetType() { return AT_DEL_ADD_CLIP; }
};

class CActionSetBoundsRect : public CAction
{
public:
	Nuclear::CRECT m_Rects[2];
	virtual eActionType GetType() { return AT_SET_BOUNDS_RECT; }
};

class CActionSetBindType : public CAction
{
public:
	Nuclear::XPEFFECT_BIND_TYPE m_BindTypes[2];
	virtual eActionType GetType() { return AT_SET_BIND_TYPE; }
};

class CActionSetFps : public CAction
{
public:
	float m_Fps[2];
	virtual eActionType GetType() { return AT_SET_FPS; }
};

class CActionSetTotalFrame : public CAction
{
public:
	int m_Frames[2];
	virtual eActionType GetType() { return AT_SET_TOTAL_FRAME; }
};

class CActionSetDivideLayer : public CAction
{
public:
	int m_Layers[2];
	virtual eActionType GetType() { return AT_SET_DIVIDE_LAYER; }
};

class CActionModifyClipAni : public CAction
{
public:
	Nuclear::AbstractEffectClip* m_pClip;
	Nuclear::Effect* m_pEffects[2];
	virtual eActionType GetType() { return AT_MODIFY_CLIP_ANI; }
};

class CActionModifyLayer : public CAction
{
public:
	Nuclear::AbstractEffectClip* m_pClip;
	int m_Layers[2];
	virtual eActionType GetType() { return AT_MODIFY_LAYER; }
};

class CActionModifyStartEndFrame : public CAction
{
public:
	Nuclear::AbstractEffectClip* m_pClip;
	int m_StartFrames[2];
	int m_EndFrames[2];
	virtual eActionType GetType() { return AT_MODIFY_START_END_FRAME; }
};

class CActionList
{

private:
	std::list<CAction*> m_ActionList;
	std::list<CAction*>::iterator m_Iter;
	CEffectEditorDoc* m_pDoc;
public:
	static int const MAX_LENGTH = 50;


private:
	bool TodoAction(int type);
	bool SetClipColorKeys(int type);
	bool SetClipOneKeys(int type);
	bool SetClipsTwoKeys(int type);
	bool SetClipPlayModeAndTime(int type);
	bool SetClipVertexColor(int type);
	bool SetClipFps(int type);
	bool SetClipPosition(int type);
	bool DelAddClip(int type);
	bool SetBoundsRect(int type);
	bool SetBindType(int type);
	bool SetFps(int type);
	bool SetTotalFrame(int type);
	bool SetDivideLayer(int type);
	bool SetModifyClipAnimation(int type);
	bool SetModifyLayer(int type);
	bool SetModifyStartEndFrame(int type);

public:
	bool CanRedo()
	{
		if (m_Iter == m_ActionList.end())
		{
			return false;
		} else {
			return true;
		}
	}
	bool CanUndo()
	{
		if (m_Iter == m_ActionList.begin())
		{
			return false;
		} else {
			return true;
		}
	}
	void CleanAll();
	bool RedoOneAction();
	bool UndoOneAction();
	void DoOneAction(CAction* pOper);
	virtual ~CActionList();
	CActionList(CEffectEditorDoc* pDoc);
};