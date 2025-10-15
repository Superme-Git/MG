#ifndef __BATTLEMANAGER_H
#define __BATTLEMANAGER_H

#include "BattleCommon.h"
#include "Singleton.hpp"

#include "../Event.h"
#include "fire/pb/battle/CSendAction.hpp"
#include "fire/pb/battle/SSendBattleStart.hpp"
#include "fire/pb/battle/SSendAddFighters.hpp"
#include "fire/pb/battle/SSendRoundScript.hpp"

const int c_nBattleOperateTime = 30;
const int c_nBattleSceneWidth = 800;
const int c_nBattleSceneHeight = 600;

#define SUMMON_NPC_ITEMTYPE	57

#include "RoleItem.h"

class Battler;
class BattleIllusion;
class AreaBlock;

typedef std::vector<Battler*>	BattlerList;
typedef std::vector<BattleIllusion*>	BattleIllusionList;
typedef std::vector<AreaBlock*>	AreaBlockList;

class BattleManager : public CSingleton<BattleManager>
{
    friend class BattleScriptPlayer;
private:
	typedef std::list<sBattleAIAction*>	AIActionList;
	typedef std::map<int, int>			BeforeOperateAI;

public:
	BattleManager();
	~BattleManager();

	bool m_bFirstShowQuickButton;
	bool m_bLastRound;

public:
	void SetFirstShowQuickButton(bool st){ m_bFirstShowQuickButton = st; }
	bool GetFirstShowQuickButton(){ return m_bFirstShowQuickButton; }

public:
	bool PickBattler(Battler* pBattler);
	bool PickArea(int battlerID);
	void CheckPickBattler();
	void CheckPickArea();

	int	 GetBattleType(){return m_BattleType;}
    void SetBattleType(int battletype) {m_BattleType = battletype;}
    int64_t	 GetBattleKey(){return m_BattleKey;}
    void SetBattleKey(int64_t battlekey) {m_BattleKey = battlekey;}
	bool IsEscapeForbiddenBattle();

	void SetFriendFormation(const int& i){m_iFriendFormation = i;}
	void SetEnemyFormation(const int& i){m_iEnemyFormation = i;}

	int	 GetFriendFormation(){return m_iFriendFormation;}
	int  GetEnemyFormation(){return m_iEnemyFormation;}
    
    void SetFriendFormationLvl(const int& i){m_iFriendFormationLvl = i;}
	void SetEnemyFormationLvl(const int& i){m_iEnemyFormationLvl = i;}
    
    int GetFriendFormationLvl(){return m_iFriendFormationLvl;}
    int GetEnemyFormationLvl(){return m_iEnemyFormationLvl;}
    
	Nuclear::PictureHandle GetJobPicHandle(int schoolID);
private:
	void FreeJobPic();
	std::map<int, Nuclear::PictureHandle>			m_mapJobPic;
public:
	CBroadcastEvent<NoParam>	EventBeginBattle;
	CBroadcastEvent<NoParam>	EventEndBattle;

	CEvent<int>					EventBattlerBuffChange;

public:
    void RefreshAllSpriteNameTexture();
	void ReleaseAllSpriteNameTexture();
	void ReloadAllSpriteNameTexture();
	
	void AddTempWatcherData(sBattlerData& data){m_listTempWatcherData.push_back(data);}
	void ClearTempWatcherData(){m_listTempWatcherData.clear();}

	void AddTempBattlerData(sBattlerData& data){m_listTempBattlerData.push_back(data);}
	void ClearTempBattlerData(){m_listTempBattlerData.clear();}
	BattlerList&	GetBattlerList(){ return m_listBattler; }
	AreaBlockList&	GetAreaBlockList(){ return m_listAreaBlock; }
	Battler*		GetMouseOverObjects(){return m_pMouseOverBattler;}
	void			ClearMouseOverObjects(){m_pMouseOverBattler = NULL;}
	void SetSummonID(int id){m_iSummonID = id;}
	void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type);
	void BeginBattleScene(int battletype, int roundnum, int battleid, int groundid, int musicid, bool bIsWatch);
	void EndBattleScene();
	void BeginWatchScene(int leftcount,int battletype,int roundnum,int groundid,int musicid, int64_t battlekey);		
	bool IsOnBattleWatch()  {  return m_bWatchModel; }
	
	void BeginReplayScene(int leftcount, int battletype, int roundnum, int groundid, int musicid, int64_t battlekey);		//进入观战
	bool isOnBattleReplay() { return m_bReplayModel; }//是否正在观战

	void InitAreaBlock();

	void ClearBattler();
	void ClearBattleData();
	void ClearAreaBlock();
	void ClearIllusionAll();

	bool AddBattler(sBattlerData* pData);
	void AddNotAddFighterList();
	bool RemoveBattler(int iBattleID);

	int	GetMainBattleCharID();
	int	GetMainBattlePetID();
	int	GetFirstEnemyID();
	int GetFirstFriendID();

	int	GetDefaultTargetID(eBattleOperate operatetype);
	Battler*	GetMainBattleChar(){return m_pMainBattler[eOperateBattler_Char];}
	Battler*	GetMainBattlePet(){return m_pMainBattler[eOperateBattler_Pet];}

	void		ClearMainBattleChar(){m_pMainBattler[eOperateBattler_Char] = NULL;}
	void		ClearMainBattlePet();
	Battler*	FindBattlerByID(int iBattleID);
	Battler*	FindBattlerCharacterByRoleID(int64_t roleID);

	void SendBattleCommand(int TargetID,eBattleOperate eOperate,bool usingdefault = false);
	void SendAutoCommand(bool autosend = true);//非玩家操作时发的消息，自动，或者是读秒倒计时到了的时�?

	std::wstring GetCurrentAutoOperateCommand();	
	std::wstring GetAutoCommandDescribe(eOperateBattlerType e);

	int GetAutoCommandOperateType(int RoleType);
	int GetAutoCommandOperateID(int RoleType);

	void SetAutoCommandOperateType(int RoleType, int Value);
	void SetAutoCommandOperateID(int RoleType, int Value);

	bool	IsEnemy(int BattleID);
	bool	IsFriend(int BattleID);
	bool	IsMySelf(int BattleID);

	//战斗里状态相�?
	void SetBattleState(eBattleState state);
	eBattleState GetBattleState(){return m_eBattleState;}

	bool IsInBattle();
    bool IsInBattleOperate();
	bool IsInBattleDemoOperate(){return m_eBattleState == eBattleState_DemoOperate;}
	bool IsInBattleDemo(){return m_eBattleState == eBattleState_Demo || m_eBattleState == eBattleState_DemoOperate;}
	bool IsMainCharOperate(){return GetBattleState() == eBattleState_OperateChar;}
	bool IsInBattleWait(){return GetBattleState() == eBattleState_Wait;}
	bool IsMainPetOperate(){return m_pMainBattler[eOperateBattler_Pet] && GetBattleState() == eBattleState_OperatePet;}

	Battler* FindBattlerBySprite(Nuclear::ISprite * sprite);
	bool OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam );
	bool UpdateMouseOverBattler(const Nuclear::NuclearLocation& aLocation);

	int GetOperateMainBatterID();
	eOperateBattlerType GetOperateMainBatterType();

	bool CanSaveToDefaultOperate(eBattleOperate e,int targetid);
	void CancelCursorForbid();

	stRoleItem& GetCurSelectedItem() { return m_CurSelectedItem; }
	void SetCurSelectedItem(stRoleItem& item){ m_CurSelectedItem = item; }
	int	 GetCurSelectedSkillID() const {return m_iCurSelectedSkillID;}
	void SetCurSelectedSkillID(int skillid){m_iCurSelectedSkillID = skillid;}

	void SetDefaultSkillID(int SkillID);
	int  GetDefaultSkillID();
	int  GetCharacterDefaultSkillID();
	void ClearCharacterDefaultSkillID(){m_pDefaultSkillID[eOperateBattler_Char] = 0;}
	void ClearPetDefaultSkillID(){m_pDefaultSkillID[eOperateBattler_Pet] = 0;}
	void CloseAllBattleDialog();
	void CloseCharacterOperateDlg();
	void ClosePetOperateDlg();
	void CloseOtherOperateDlg();	

	void SetBattlerOperateStateNull();

	void SetShowBattlerDemo(bool state);
	void RefreshBattlerDemo();
	void SetDelayDemoShow(int delayTm){ m_iDemoShowDelayTime = delayTm; };
	void BeginBattleOperate(eOperateBattlerType eMainBattler);
	void FinishBattleOperate(eOperateBattlerType eMainBattler);
	void FinishBattleOperate();

	void ClearAllFlag();
public:
	Nuclear::NuclearLocation GetWatcherLocation(int iBattleID);				
	Nuclear::NuclearLocation GetBattleLocation(int iBattleID);				
	Nuclear::NuclearLocation GetBattleUILocation(int iBattleID);					
	Nuclear::NuclearLocation BattleGridToPixelGrid(Nuclear::NuclearLocation ptGrid);
	Nuclear::NuclearLocation PixelGridToBattleGrid(Nuclear::NuclearLocation ptPixel);

	Nuclear::NuclearLocation GetBattleCenter();	

	void Run(int now, int delta);
	void BattleOperateRun(int now, int delta);

	void DrawUnderUI(int now);
	void Draw(int now);

	void ResetOperateTime(int time = c_nBattleOperateTime);

    int GetPetAutoSkillID(){return (m_LastBattleOperateCmd[eOperateBattler_Pet].BattleOperate == eOperate_Skill)?m_LastBattleOperateCmd[eOperateBattler_Pet].OperatorID:-1;}
    void SetPetAutoSkillID(int skillID)
	{
        m_LastBattleOperateCmd[eOperateBattler_Pet].BattleOperate = eOperate_Skill;
        m_LastBattleOperateCmd[eOperateBattler_Pet].OperatorID = skillID;
    }

	void ResetPetAutoOperateCommand();

	void SetMainRoleAttributeRoundEnd(std::map<int,int> attrchange)
	{
		m_MainRoleAttributeRoundEnd.clear();
		m_MainRoleAttributeRoundEnd = attrchange;
	}
	void SetMainPetAttributeRoundEnd(std::map<int,int> attrchange)
	{
		m_MainPetAttributeRoundEnd.clear();
		m_MainPetAttributeRoundEnd = attrchange;
	}
	void SetFighterHPRoundEnd(std::map<int, int> hpmap)
	{
		m_fighterfinallyhps.clear();
		for (std::map<int, int>::iterator itor = hpmap.begin(); itor != hpmap.end(); itor++)
		{
			int k = itor->first;
			int v = itor->second;
			SCBattleIDChange(k);
			m_fighterfinallyhps[k] = v;
		}
	}

	void SetDealBeforeBattleEnd(int iDealBeforeBattleEnd);
	int GetDealBeforeBattleEnd();

	int DealAIActionMap(BeforeOperateAI aiaction);
	void DealAIActionBeforeBattleEnd(BeforeOperateAI aiaction);	

	void PlayAIEffect(std::wstring effectpath,Battler* pExecutor);

	int	 DealAIBeforeDemoExecute() { return 0; };
	int  DealAIAfterDemoExecute() { return 0; };

	void AddAIActionScript(sBattleAIAction* pAIAction){m_listAIAction.push_back(pAIAction);}

	void ProcessRoundEndScript(std::vector<sBattleDemoResult>& list);

	void ClearRoundAIAction();

public:
    bool m_bIsAutoBattle;
	bool m_bIsAutoBattleQuick;
    
	void SetMaxAutoRound(int maxnum){m_iAutoMaxRound = maxnum;}

	void BeginAutoOperate();
	void EndAutoOperate();
	bool IsAutoOperate();

	int SCBattleIDChange(int& id);
	void SetBattleIDChange(bool b){m_bBattleIDChange = b;}

	bool HandleRefusePK(const CEGUI::EventArgs& e);
	bool HandleAcceptPK(const CEGUI::EventArgs& e);

public:
	void RemoveExitReplayBtn();
	int BeginBattleReplay(std::wstring FileName);
	int BeginPlayBattleReplay(std::wstring FileName);
	int LoadBattleReplay(std::wstring FileName, fire::pb::battle::SSendBattleStart& SSBS, fire::pb::battle::SSendAddFighters& SSAF, std::vector<fire::pb::battle::SSendRoundScript>& RoundArr);
	int SSendBattleStart_Process(fire::pb::battle::SSendBattleStart& SSBS);
	int SSendAddFighters_Process(fire::pb::battle::SSendAddFighters& SSAF);
	int SSendRoundScript_Process(fire::pb::battle::SSendRoundScript& SSRS);

	bool	IsInWatchModel(){return m_eBattleState != eBattleState_Null && m_bWatchModel;}
	bool	IsInPVPBattle();
	bool	IsInReplayModel() { return m_bReplayModel; }//当前是否在回放状态

	void	UpdateItemRemove(int itemthisid);
	void	RequestLeaveBattle();
	void	RequestLeaveBattleReplay();//向服务器请求离开战斗(回放情况下用)

	//战斗场景里的一些无关npc
	BattleIllusion* AddBattleIllusion(sBattlerData data,float scale = 1.0f);
	BattleIllusion* AddBattleIllusion(int shapeid,float scale = 1.0f);
	BattleIllusion* FindBattleIllusionBySprite(Nuclear::ISprite * sprite);

	bool OnBeforeRender(int now);

	void SetSummonDemoPetKey(int key){m_SummonDemoKey = key;}
	int	 GetSummonDemoPetKey(){return m_SummonDemoKey;}

	int GetSummonCount() { return m_SummonCount; }
	void SetSummonCount(int Value) { m_SummonCount = Value; }

	void SetRecallDemoPet(bool b){m_bRecallDemo = b;}
	bool GetRecallDemoPet(){return m_bRecallDemo;}

	int GetUseItemCount(int Index) { return m_UseItemCount[Index]; }
	void SetUseItemCount(int Index, int Value) { m_UseItemCount[Index] = Value; }

	int GetRunawayConfirmCount(int PlayerOrPet) { return m_RunawayConfirmCount[PlayerOrPet]; }
	void SetRunawayConfirmCount(int PlayerOrPet, int Value) { m_RunawayConfirmCount[PlayerOrPet] = Value; }

	int GetRoundCount(){return m_iRoundCount;}
	void AddRoundCount(){ m_iRoundCount++; }

	int GetBattleGroundID(){return m_iBattleGroundid;}
	int GetBattleID(){return m_BattleID;}
	void ChangeBattleGround(const int& groundid);
	void ResetBattleGround(bool changedisplaymode = false);		

	void PlayBattleSound(int musicid = 0);
	void PlayAISpeak(std::wstring soundpath,Battler* pExecutor);

	void SetAlreadyPackTips(){m_bAlreadyPackTips = true;}
	bool GetAlreadyPackTips(){return m_bAlreadyPackTips;}

	Nuclear::NuclearFRectt GetBattlerRect(int battlerId);
	CEGUI::Window* GetBattlerWindow(int battlerId);

	bool HaveAlreadyUseSummonNpcItem();
	void SetAlreadyUseItemList(std::map<int, int> list);
	void AddAlreadyUseItemList(int itembaseid);
	void RefreshTwoItem();

	void ClearAlreadyUseItemList();
	void InsertAlreadyUseItemList(int nKey, int nValue);

	void SetEffectComponentNum(int num){m_iEffectComponentNum = num;}

	void SetBattleDemoSpeedUp(float rate);

	void ResetBattlerDefaultAction();
	void ResetBattlerHpBar();

public:
	int m_lastBattleId;
	int GetLastBattleId();

private:
	bool HandleExitWatch(const CEGUI::EventArgs& e);
	void AddExitWatchBtn(int iBattleID);
	void RemoveExitWatchBtn();
	bool HandleExitReplay(const CEGUI::EventArgs& e);
	void AddExitReplayBtn(int iBattleID);

private:
	bool m_bReplayModel;//回放状态
	bool m_bWatchModel;
	CEGUI::PushButton* m_pExitWatchBtn;
	CEGUI::PushButton* m_pExitReplayBtn;

	int	 m_SummonDemoKey;
	int  m_SummonCount;
	bool m_bRecallDemo;
	int  m_UseItemCount[2];
	bool m_bAlreadyPackTips;

	int m_RunawayConfirmCount[2];

private:
	int		m_iAutoRound;
	int		m_iAutoMaxRound;

	int		m_iEnemyFormation;
	int		m_iFriendFormation;

	int     m_iEnemyFormationLvl;
	int     m_iFriendFormationLvl;

	int		m_BattleType;
	int64_t	m_BattleKey;

	bool	m_bShowHpBar;
	bool    m_bFirstEnter;

	float	m_fDemoSpeedRate;
	int		m_iEffectComponentNum;

	Nuclear::IEffect* m_pOperateStateEffectChar;
	Nuclear::IEffect* m_pOperateStateEffectPet;

	eBattleOperate	m_eCurDemoOperate;
	int				m_iOperateTime;

	BattleIllusionList	m_listBattleIllusion;

	int				m_iDemoShowDelayTime;	//显示其他玩家播放中 延迟时间
private://自动相关
	sBattleOperateCommand m_LastBattleOperateCmd[eOperateBattlerMax];

private:
	eBattleState	m_eBattleState;
	Battler*		m_pMainBattler[eOperateBattlerMax];
	int				m_iSummonID;
	std::list<sBattlerData>	m_listTempBattlerData;

	std::list<sBattlerData>	m_listTempWatcherData;
	BattlerList		m_listBattler;
	AreaBlockList	m_listAreaBlock;

	Battler*		m_pMouseOverBattler;

	int		m_BattleID;
	int		m_iBattleGroundid;
	int		m_iBattleMusicid;
	int		m_iRoundCount;

	bool	m_bBattleIDChange;

	Nuclear::WarBackgroundHandle	m_hCurrentBattleGround;

	std::map<int, int>		m_MainRoleAttribute;
	std::map<int, int>		m_MainPetAttribute;

	std::map<int, int>		m_MainRoleAttributeRoundEnd;
	std::map<int, int>		m_MainPetAttributeRoundEnd;

	std::map<int, int>		m_fighterfinallyhps;

	std::map<int, int>	m_AlreadyUseItemList;

private:	//战斗操作相关
	fire::pb::battle::CSendAction m_SendAction[eOperateBattlerMax];

	stRoleItem		m_CurSelectedItem;
	int				m_pDefaultSkillID[eOperateBattlerMax];
	int				m_iCurSelectedSkillID;

	int				m_iDefaultTargetID[eOperateBattlerMax];
	
private://ai
	AIActionList			m_listAIAction;

	BeforeOperateAI			m_AIActionBeforeOperate;

	int		m_iDealBeforeBattleEnd;
	bool	m_bShowDemoing;
};

inline BattleManager* GetBattleManager()
{
	return BattleManager::GetInstance();
}
#endif
