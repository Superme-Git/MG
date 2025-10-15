#pragma once

#include "nuiengine.h"
#include "Npc.h"

#define EFFECT_OFFSETY 25	//头顶特效偏移
#define EFF_OFFSETY_TO_TITLE 32 //cheng wei effect offset
#include <queue>
const int c_iJumpActionTime		= 1500;	//定义跳跃动作时间恒定为1000毫秒
const int c_iJump2ActionTime	= 1500;	//定义2段跳跃动作时间恒定为1000毫秒

const int c_iHighJumpYOffset = 180;

#define OPEN_CHARACTER_TITLE_EFF 1

#if OPEN_CHARACTER_TITLE_EFF
#endif

class PopoEffectNotify : public Nuclear::IEffectNotify
{
public:
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

struct sOffsetEffect
{
    sOffsetEffect(Nuclear::IEffect* pEffect) : d_pEffect(pEffect) , d_offset(0)   
	{
        if (d_pEffect) {
            d_pNotify = new PopoEffectNotify;
            d_pEffect->AddNotify(d_pNotify);
        }
    }
    virtual ~sOffsetEffect()
	{
        if (d_pEffect) {
            d_pEffect->RemoveNotify(d_pNotify);
            delete d_pNotify;
            Nuclear::GetEngine()->ReleaseEffect(d_pEffect);
            d_pEffect = NULL;
        }
    }
    Nuclear::IEffect* d_pEffect;
    PopoEffectNotify* d_pNotify;
    int d_offset;
};

class PopoEffect
{
    typedef std::list<sOffsetEffect*> NumEffects;
public:
    PopoEffect(int effectid, int value, int valuecolor);
    ~PopoEffect();
    void Show(unsigned int now, float c_fx, float c_fy);
	void InitPopo(unsigned int now, float c_fx, float c_fy);
	bool IsStop(unsigned int now);

private:
    int getNumberEffectid(int num, int color);

	void numToEffects(int number, int color);
	void calEffectsOffset(long c_fx, long c_fy);
    int getEffectWidth(sOffsetEffect& effect);
    void playEffect(sOffsetEffect& effect, long c_fx, long c_fy);

public:
    const int d_effectid;
    int d_value;
    int d_color;

	NumEffects d_numEffects;

private:
    sOffsetEffect* d_pPropertyEffect;
    sOffsetEffect* d_pAddEffect;

	static const float s_fStartVelocity;

	unsigned int d_endtime;
};
typedef std::list<PopoEffect*> PopoEffectList;
typedef std::list<PopoEffect*> PopoEffectQueue;
typedef std::map<int,  PopoEffectQueue> MsgMap;

class CCharacterInfoManager : public MsgMap
{
public:
    CCharacterInfoManager();
    virtual ~CCharacterInfoManager();
	void OnUpdate(unsigned int now, float c_fx, float c_fy);

private:
    static const int s_popoInterval;
	unsigned int d_AllowPopo;
    PopoEffectList d_ShowString;
};

struct SCacheDefaultAction
{
	eActionType	actiontype;
	bool		holdlastframe;
	float		freq;
	SCacheDefaultAction(eActionType actt, bool holdlf, float f) : actiontype(actt), holdlastframe(holdlf), freq(f) {}
};

struct SCacheComponent
{
	eSpriteComponentType type;
	int					 baseid;
	bool				 forcechange;
	SCacheComponent(eSpriteComponentType t, int bid, bool b) : type(t), baseid(bid), forcechange(b) {}
};

struct SCacheCharacterAttribute
{
	stCharacterData characterData;
	float           speed;//运行速度
	SCacheDefaultAction*	pDefaultAct;
	SCacheDefaultAction*	pDefaultActWithOut;
	SCacheComponent*		pComponent;
	Nuclear::NuclearLocation*	pLocation;
	Nuclear::NuclearDirection*	pDir;
	SCacheCharacterAttribute() : speed(0.0f), pDefaultAct(NULL), pDefaultActWithOut(NULL), pComponent(NULL), pLocation(NULL), pDir(NULL) { }
	~SCacheCharacterAttribute()
	{
		if (pDefaultAct) delete pDefaultAct;
		if (pDefaultActWithOut) delete pDefaultActWithOut;
		if (pComponent) delete pComponent;
		if (pLocation) delete pLocation;
		if (pDir) delete pDir;
	}
};

class Character : public ComponentsObject
{
public:
	Character(); //lua
	virtual ~Character(); //lua

	virtual void SetInHighLevel(bool bhighlevel);
	virtual bool IsInHighLevel(){return m_data.bhighlevel;}
			
	virtual void InitPosition(Nuclear::NuclearLocation loc,bool bhighlevel = false);

	virtual	void SetAttribute(stCharacterData* pData);
	static void ClearPicPool() { picpool.clear(); }

	BYTE GetDefaultAction(){return m_data.actiondefault;}
	virtual void SetDefaultAction(eActionType actiontype,bool holdlastframe = false,float freq=g_defaultActionFrequence);

	void SetTeamLeaderEffectVisible(bool b); //lua
	virtual std::wstring GetName(){return m_data.strName;} //lua
	virtual void SetName(const std::wstring &name);
	virtual void SetTitle(const int& id, const std::wstring &title); //lua
    int GetTitleID(); //lua
	virtual std::wstring GetTitle(); //lua
    std::wstring GetTitleForClassCharacter(); //lua
    
	virtual void SetDirection(int tx, int ty);
	virtual void SetDirection(Nuclear::NuclearDirection dir);
	virtual void SetVisible(bool v);
	virtual bool PlayAction(eActionType actionType,float freq=g_defaultActionFrequence);
	virtual int GetShapeID() { return m_data.shape;} //lua

	virtual void SetShapeID(int shapeid); //lua

	virtual void Run(int now,int delta);
	virtual void StopMove();
	virtual bool MoveTo(Nuclear::NuclearLocation ptMoveTarget);
	virtual void UpdateAction(Nuclear::XPUSA_TYPE type);
	virtual void Draw(int now);

	virtual int GetLevel(){ return m_data.level;} //lua
	void SetLevel(const int level){m_data.level = level;} //lua

	virtual eSexType GetSex() { return m_data.eSex; } //lua
	virtual eSchoolType GetSchool() { return m_data.eSchool; } //lua
    virtual int GetCamp() {return m_data.camp;} //lua
    void SetCamp(const int camp){m_data.camp = camp;} //lua

	virtual bool OnSetCursor(); //lua

	virtual bool CanBeAttack(); //lua
	virtual bool CanBeGiven(); //lua
	virtual bool CanJoinTeam(); //lua
	virtual bool CanTradeWith(); //lua
	virtual bool CanGiveFlower(); //lua
	virtual bool CanGiveRose(); //lua
	virtual bool CanBeUseSkill(int skillid); //lua

	void				InitCharacterState();
	void				UpdateCharacterState(int state);
	void				AddCharacterState(eCharacterState e);
	void				RemoveCharacterState(eCharacterState e);
	bool				IsSetCharacterState(eCharacterState e);

	//是否是在战斗状态下
	bool				IsInBattle(){ return (m_data.characterstate & (int)eCharacterStateInBattle) != 0x00;} //lua
	bool				IsWatchBattle(){ return (m_data.characterstate & (int)eCharacterStateWatchBattle) != 0x00;} //lua
	int					GetActuallyShape() { return m_data.actuallyshape; } //lua

	Character*			GetTeamLeader(); //lua
	Character*			GetTeamFollow(){return m_pTeamFollower;} //lua
	bool				IsOnTeamFollow(){return NULL != m_pTeamFollower;}//是否在队伍跟随状态下
	virtual void		ClearTeamFollower(){m_pTeamFollower = NULL;} //lua
	void				RunTeamFollow();
	int64_t				GetTeamID(){return m_data.teamID;} //lua
	int					GetTeamIndex(); //lua
	eTeamMemberState	GetTeamState(); //lua
	void				SetTeamInfoOutOfDate(bool b){m_TeamInfoOutofDate = b;}
	bool				IsTeamInfoOutOfDate(){return m_TeamInfoOutofDate;}
	bool				IsOnTeam(){return m_data.teamID != 0;} //lua
	bool				IsTeamLeader(){return  m_data.teamID != 0 && m_data.teamindex == 1;} //lua
	virtual void		SetTeamInfo(int64_t teamid, int teamindex, int teamstate);
	virtual void		SetTeamFollow(); //lua
	void				UpdateTeamLeaderEffect();
	
	virtual	bool		IsInChallengeArea();	//是否在PK台上

	//角色换装相关接口
	virtual void ChangeSpriteModel(const int& shapeid); //lua
	virtual void OnCloseStall(bool isRemoveChar); //结束摆摊时造型换回来
	void UpdataTitleTexture();
	Nuclear::TextBlockHandle GetTitleTexHandle() { return m_TitleTextureHandle; }
	Nuclear::ITextBlock* GetTitleTextBlock() { return m_pTitleTextBlock; }

    void AddPopoMsg(int effectid, int value, int colorid, int order); //lua
	void AddPopoMsgReduce(int effectid, int value, int colorid, int order);
    virtual void UpdatNameTexture(bool bReleaseOld=false); //bReleaseOld是否释放旧纹理

	void DrawMsgEffect(int now);

	std::wstring GetAutoMoveRideName(int AutoMovePathID, int AutoMovePathID2);

	float GetAutoMoveSpeed(int AutoMovePathID, int AutoMovePathID2);

	int GetRideModeID();
	int GetRideSpeedByModelID(int iModelID);

	int GetOffsetYStep(int WayID, int PathID1, int PointID1);
	int GetOffsetYCur(int WayID, int PathID1, int PointID1);
	int GetOffsetYNext(int WayID, int PathID1, int PointID1);

	void SetFlyScaleTgt(int iFlyScaleTgt);
	void SetFlyScaleCur(int iFlyScaleCur);
	int GetFlyScaleCur();
	void SetFlyScaleStep(int iFlyScaleStep);
	int GetFlyScaleStep();
	void SetFlyOffsetYTgt(int iFlyOffsetYTgt);
	void SetFlyOffsetYCur(int iFlyOffsetYCur);
	int GetFlyOffsetYCur();
	void SetFlyOffsetYStep(int iFlyOffsetYStep);
	int GetFlyOffsetYStep();
	
	void UpdateSpeed();

	virtual void SetMoveSpeed(float speed);
	virtual void SetDefaultActionWithoutLogic(eActionType actiontype, bool holdlastframe = false, float freq = g_defaultActionFrequence);
	virtual void SetSpriteComponent(eSpriteComponentType type, int baseid, bool forcechange = false);
	virtual void SetLocation(const Nuclear::NuclearLocation& location);
	
	void SetTeamNumVisible(bool b);
	void SetTeamNum(std::wstring numstr);
	void SetTeamNumHeight(int height,int horseheight);
protected:
	int GetHeadEffectNum();

protected:
	typedef std::map<std::wstring, Nuclear::PictureHandle> PICMAP;
	static PICMAP	picpool;
	stCharacterData		m_data;
	Nuclear::IEffect*  m_pTitleEffect;

	Character*			m_pTeamFollower;
	int					m_iFollowCheckTime;

private:
	bool				m_TeamInfoOutofDate;
	Nuclear::TextBlockHandle       m_TitleTextureHandle;  //称谓纹理句柄
	Nuclear::ITextBlock* m_pTitleTextBlock; //称谓的TextBlock	

	Nuclear::TextBlockHandle       m_TeamNumTextureHandle;  //队伍人数纹理句柄
	Nuclear::ITextBlock*			m_pTeamTextBlock; //队伍人数的TextBlock	
	bool							m_bShowTeamNum;
	int						m_iTeamNumHeight;
	int						m_iTeamNumHeightHorse;

	CCharacterInfoManager m_MsgManager;
	bool                  m_bGM;

	Nuclear::IEffect*  m_pTeamLeaderEffect;
	Nuclear::IEffect*	m_pWatchBattleEffect;
	Nuclear::IEffect*	m_pInBattleEffect;
	Nuclear::IEffect*	m_pInPlayCGEffect;
	Nuclear::IEffect*	m_pFactionBattleEffect;		//公会大战特效
	Nuclear::IEffect*	m_pFlowerEffect;

	SCacheCharacterAttribute*    m_pCacheCharacterAttribute;
	DWORD				m_dwTitleColor;
};
