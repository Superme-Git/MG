//  BattleScriptPlayer.h
//  FireClient

#ifndef FireClient_BattleScriptPlayer_h
#define FireClient_BattleScriptPlayer_h

#include <vector>
#include <list>
#include "Battler.h"
#include "BattleIllusion.h"
#include "Skill.h"
#include "fire/pb/battle/SSendRoundScript.hpp"

typedef std::vector<Skill*>            SkillList;
typedef std::deque<sBattleDemoScript>	ListBattleDemoScript;
typedef std::vector<Nuclear::IEffect*> EffectList;
typedef std::list<SkillStage*>         StageList;

typedef std::list<sBattleAIAction*>	AIActionList;
typedef std::map<int,int>				BeforeOperateAI;	//回合操作开始前的AI

class BattleScriptPlayer
{
private:
	EffectList  m_GroundEffects;
	EffectList  m_PlayEffects;
    void processDataChangeOnly(sBattleDemoScript& aScript);
public:
    BattleScriptPlayer();
    ~BattleScriptPlayer();

	std::vector<fire::pb::battle::SSendRoundScript> m_RoundArr;
	int m_RoundIndex;
	int PlayReplayRound(int RoundIndex);
    
    void AddPersistStage(SkillStage* aPStage);

    void DoBattleClear();
    void DoRoundClear();
    void PushDemoScript(sBattleDemoScript& aScript)
	{
		m_listBattleDemoScript.push_back(aScript);
		m_listBattleDemoScriptBackup.push_back(aScript);
	}
	Skill* GetCurrentSkill();
    void Play();
    void Run(int now, int delta);    
    
    void OnDemoBegin();
	void OnDemoEnd(sBattleDemoScript& script);
    void OnScriptFinished();
	ListBattleDemoScript	m_listBattleDemoScript;     //本回合战斗脚本
	ListBattleDemoScript	m_listBattleDemoScriptBackup;     //本回合战斗脚本

	bool IsSkillsOver();
	bool IsSkillsZero();
	void ClearSkills();
	std::vector<Skill*> m_pCurSkills;            //当前技能数组
    StageList       m_listPersistStage;     //持续stage，用于击飞
    bool            m_bFinished;
    int             m_iLastTick;            //
	int				m_actionStarttime;      //接收到战斗协议时间
    std::list<int>  m_actiontime;           //回合出手时间列表
    int             m_iBeforeAITime;        //出手前ai延时
    int             m_iAfterAITime;         //出手后ai延时
    int             m_iDealBeforeOperAI;    //操作前ai延时
    int             m_iRoundDemoIndex;      //回合出手数

	int				m_iRoundOverDelayTime;	//回合表演结束之后上报服务器结束之前的延迟时间
    
    void AddGroundEffect(Nuclear::IEffect* aPEffect);
    void RemoveGroundEffect(Nuclear::IEffect* aPEffect);
    void ClearGroundEffect();
	void AddPlayEffect(Nuclear::IEffect* aPEffect);
	void ClearPlayEffect();

public:
    //////////*AI*//////////
	AIActionList		m_listAIAction;
	BeforeOperateAI		m_AIActionBeforeOperate;
    
    void    AddAI(sBattleAIAction* aPAI);
    
    void    DealAIAction(sBattleAIAction* pAction);
    void    DealAIActionWhileBattlerDie(int battleID);
    int     DealAIBeforeDemoExecute();
    int     DealAIAfterDemoExecute();
    void    DealAIActionBeforeOpearte(BeforeOperateAI aiaction);
    void    DealAIActionBeforeOpearte();
};

extern BattleScriptPlayer g_BattleScriptPlayer;

inline BattleScriptPlayer GetBattleScriptPlayer()
{
	return g_BattleScriptPlayer;
}

#endif
