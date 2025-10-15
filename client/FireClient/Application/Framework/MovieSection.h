/***********************************************************************
filename: 	CSceneMovieNpc.h
author:		eagle
purpose:	场景动画的一个片段
*************************************************************************/
#pragma once

class MovieSection 
{
public:
	MovieSection();
	virtual ~MovieSection();

	virtual void Start();
	virtual void End();
	virtual void Update(int elapse);
	virtual void OnMouseButtonDown();

	virtual void SetTotalTime(int time);
	int	GetTotalTime();
	int GetDurationTime();
	bool IsEnd();
	
private:
	int		m_iDurateTime;	//片段已经持续时间
	int		m_iTotalTime;	//总时间m_iTotalTime
    bool	m_bEnd;			//是否结束
};

class SetMapSection : public MovieSection
{
public:
	SetMapSection(int mapID,int xPos,int yPos);
	~SetMapSection();

	void Start();

private:
	int m_mapID;
	int m_PosX;
	int m_PosY;
};

class ReturnSection : public MovieSection
{
public:
	enum TYPE
	{
		TYPE_DIRECTLY_RETURN,  // 直接返回
		TYPE_RETURN_TO_SPECIFIED_MAP,  // 返回到指定地图
		TYPE_RETURN_TO_SCHOOL,  // 返回到门派
		TYPE_RETURN_TO_FIGHT,  // 返回后战斗
	};

public:
	ReturnSection(int time, TYPE type, int mapId, int posX, int posY);
	~ReturnSection();
	void Start();

private:
	TYPE mType;
	int mMapId;
	int mPosX;
	int mPosY;
};

class StartSection : public MovieSection
{
public:
	StartSection(int TotalTime);
	~StartSection();
    void Start();
	void Update(int elapse);
	void End();
};

class EndSection : public MovieSection
{
public:
	EndSection(int TotalTime);
	~EndSection();

	void Start();
	void Update(int elapse);
	void End();
};

class SetCamSection : public MovieSection
{
public:
	SetCamSection(int xPos,int yPos);
	~SetCamSection();

	void Start();
private:
	int m_PosX;
	int m_PosY;
};

class MoveCamSection : public MovieSection
{
public:
	MoveCamSection(int xPos,int yPos,int time);
	~MoveCamSection();

	void Start();
	void Update(int elapse);

private:
	int m_TargetX;
	int m_TargetY;
	
	Nuclear::NuclearCameraUpdateType mOldCamUpdateType;
};

class ZoomInSection : public MovieSection
{
public:
	ZoomInSection(int rate,int lastTime,int endTime);
	~ZoomInSection();

	void Start();
	void Update(int elapse);
};


class ZoomOutSection : public MovieSection
{
public:
	ZoomOutSection(int rate,int lastTime,int endTime);
	~ZoomOutSection();

	void Start();
	void Update(int elapse);
};

class RotateSection : public MovieSection
{
public:
	RotateSection(int angel);
	~RotateSection();

	void Start();
	void Update(int elapse);
private:
	int m_iAngle;
};

class ShakeSection : public MovieSection
{
public:
	ShakeSection(int id, int range,int time);
	~ShakeSection();

	void Start();
private:
	int m_id;
	int m_range;
	int m_time;
};

class CreateNpcSection : public MovieSection
{
public:
	CreateNpcSection(int id, const std::wstring& identityName, int pos_X, int pos_Y, int modelID, const std::wstring& strNpcName, int title, int dtaction, float scale, int dir = 0, int _colorTemplate = 1, 
		const std::wstring& effectName = L"", int reapeat = 1, int ex = 0, int ey = 0, const std::wstring& chengwei = L"", const std::wstring& cwColor = L"", int mountID=0, int colorA=0, int colorB=0);
	~CreateNpcSection();

	void Start();
	
private:
	int m_id;
	std::wstring m_identityName;
	int m_posX;
	int m_pos_Y;
	int m_modelID;
	std::wstring m_strNpcName;
	int m_title;
	int m_dir;
	int m_colorTemplate;
	int m_defaultAction;
	float m_scale;
	std::wstring m_effectName;
	std::wstring m_chengwei;
	std::wstring m_cwColor;
	int m_eftRepeat;
	int m_effectX;
	int m_effectY;
	int m_mountID;
	int m_colorA;
	int m_colorB;
};

class MoveNpcSection : public MovieSection
{
public:
	MoveNpcSection(const std::wstring& varIdentityName,int target_X,int target_Y,int TotalTime);
	~MoveNpcSection();
   
	void Start();
	
private:
	int m_TargetX;
	int m_TargetY; 
	std::wstring m_identityName;
	int m_time;
};

class PlayActionSection : public MovieSection
{
public:
	PlayActionSection(const std::wstring& identityName,int ActionId, int endAction,int dir);
	~PlayActionSection();

	void Start();
	void End();
private:
	std::wstring m_identityName;
	int m_ActionID;
	int m_endAction;
	int m_dir;
};


class NpcSayPopSection : public MovieSection
{
public:
	NpcSayPopSection(const std::wstring& identityName,const std::wstring& text);
	~NpcSayPopSection();

	void Start();
	
private:
	std::wstring m_identityName;
	std::wstring m_text;
};

class NpcSpeakWndSection : public MovieSection
{
public:
	NpcSpeakWndSection(int headID,const std::wstring& name,const std::wstring& text, int time, bool isPause=false);
	~NpcSpeakWndSection();

	void Start();
	void Update(int elapse);
	void OnMouseButtonDown();
	void ShowNextText();
private:
	bool m_isPause;
	int m_HeadID;
	int m_TotalTime;
	int m_DurateTime;
	std::wstring m_NpcName;
	std::wstring m_text;
	std::vector<std::wstring> m_vecText;
};

class NpcComponentSection : public MovieSection
{
public:
	NpcComponentSection(const std::wstring& varIdentityName, int type, int baseid);
	~NpcComponentSection();

	void Start();

private:
	int m_Type;
	int m_BaseID;
	std::wstring m_identityName;
};

class DeleteNpcSection : public MovieSection
{
public:
	DeleteNpcSection(const std::wstring& identityName);
	~DeleteNpcSection();

	void Start();
	
private:
	std::wstring m_identityName;
};

class StartSpeakSection : public MovieSection
{
public:
	StartSpeakSection(int time);
	~StartSpeakSection();

	void Start();
};

class WaitSection : public MovieSection
{
public:
	WaitSection(int time);
	~WaitSection();

	void Start();

private:
	int m_WaitTime;
};

class SetNpcDirSection : public MovieSection
{
public:
	SetNpcDirSection(const std::wstring& name,int dir);
	~SetNpcDirSection();

	void Start();

private:
	std::wstring m_identityName;
	int m_dir;
};

class SetEffectSection : public MovieSection
{
public:
	SetEffectSection(const std::wstring& name, int x, int y, int effectID, int cycle, int type = 2); //cycle为零则无限次播放
	~SetEffectSection();

	void Start();

private:
	std::wstring m_name;
	int m_x;
	int m_y;
	int m_effectID;
	int m_cycle;
	int m_type;
};

class PlaySoundSection : public MovieSection
{
public:
	PlaySoundSection(int id,const std::wstring& soundName,int repeat,int _fadeTime);
	~PlaySoundSection();

	void Start();

private:
	std::wstring m_SoundName;
	int m_Id;
	int m_Repeat;
	int m_FadeTime;
};

class PlaySDEffectSection : public MovieSection
{
public:
	PlaySDEffectSection(const std::wstring& eftName);
	~PlaySDEffectSection();

	void Start();
private:
	std::wstring m_SoundEffectName;
};

class StopSoundSection : public MovieSection
{
public:
	StopSoundSection(int id, int _fadeTime);
	~StopSoundSection();

	void Start();

private:
	int m_Id;
	int m_FadeTime;

};

class ClearSceneSection : public MovieSection
{
public:
	ClearSceneSection(const std::wstring& type);
	~ClearSceneSection();

	void Start();

private:
	std::wstring m_TypeName;
};

class ForbiddenAutoPlaySection : public MovieSection
{
public:
	ForbiddenAutoPlaySection();
	~ForbiddenAutoPlaySection();

	void Start();
};

