//  Skill.h
//  FireClient

#ifndef FireClient_Skill_h
#define FireClient_Skill_h

#include <vector>
#include <list>
#include "BattleCommon.h"

class Battler;
class BattleIllusion;

typedef std::vector<sBattleDemoResult*>	BattleResultList;

//一次性stage ，靠谱不？
class SkillStage
{
public:    
    int         m_iBeginTick;       //开始时刻
    int         m_iDelayTicks;
    int         m_iExecuteTicks;	//总共执行时间,包含等待时间
	int         m_iRealExecuteTicks;//除去等待时间之外的真实执行时间
    int         m_iLifeTicks;       //生命周期,生命周期结束，转至下个stage
    int         m_iLastTicks;       //持续时间,持续时间结束，析构本stage， 0 表示非持续型
    int         m_iTriggerTicks;    //触发等待时间      0，表示非触发型
    bool        m_bIsStageComplete;
    bool        m_bIsTriggerStage;
    bool        m_bIsTriggered;     //是否被触发
    
public:    
    SkillStage(int aLife):
    m_iBeginTick(0)
    ,m_iDelayTicks(0)
    ,m_iExecuteTicks(0)
	,m_iRealExecuteTicks(0)
    ,m_iLifeTicks(aLife)
    ,m_bIsStageComplete(false)
    ,m_bIsTriggerStage(false)
    ,m_bIsTriggered(true){}
    
    virtual ~SkillStage(){}
    
    virtual void OnStageBegin(){};
    virtual void OnStageEnd(){};
    virtual void OnStageTick(int now, int delta) { m_iRealExecuteTicks += delta; };

	virtual void SetDelayTicks(int iDelayTicks) { m_iDelayTicks = iDelayTicks; }
    
    void Begin(int now)
    {
		if (m_iBeginTick == 0)
		{
			m_iBeginTick = now;
		}
    }
    
	virtual bool Run(int now, int delta)
    {
        if(m_iBeginTick == 0)
        {
			Begin(now-delta);
			//delta = 0;
            //return false;
        }
        m_iExecuteTicks += delta;
        
        if(m_iDelayTicks >= 0)
        {
            if(m_iDelayTicks > 0)
            {
                if(m_iDelayTicks < m_iExecuteTicks)
                {
                    OnStageBegin();
                    m_iDelayTicks = -1;
                    //return;
                }
				else
				{
					return false;
				}
            }
			else
            {
                OnStageBegin();
                m_iDelayTicks = -1;
            }
        }
        
        
        if (!m_bIsStageComplete)
        {
            if (now > m_iBeginTick)
            {
                if (m_bIsTriggerStage && m_bIsTriggered)
				{
                    m_bIsStageComplete = true;
                    OnStageEnd();
					return true;
                }
                
                if(now - m_iBeginTick < m_iLifeTicks)
				{
                    OnStageTick(now, delta);
                }
				else
				{
                    m_bIsStageComplete = true;
                    OnStageEnd();
                }
            }
        }
		return true;
    }
    
private:
    SkillStage();
};

class ParallelSkillStage : public SkillStage
{
    std::vector<SkillStage*>   m_vStages;
    ParallelSkillStage();
public:
    ParallelSkillStage(int aTicks):SkillStage(aTicks)
    {
        m_bIsTriggerStage = true;
        m_bIsTriggered = false;
    }
	~ParallelSkillStage()
	{
		for (unsigned int i = 0; i < m_vStages.size(); i++)
		{
			delete m_vStages[i];
		}
		m_vStages.resize(0);
		m_vStages.clear();
	}
    
    void AddStage(SkillStage* aStage) { m_vStages.push_back(aStage); }
    
    virtual void OnStageBegin() { };
    virtual void OnStageEnd() { };

	virtual void SetDelayTicks(int iDelayTicks) { SkillStage::SetDelayTicks(iDelayTicks); }

	virtual bool Run(int now, int delta)
	{
        bool isComplete = true;
        for (unsigned int i = 0; i < m_vStages.size(); i++)
		{
            m_vStages[i]->Run(now, delta);//如果执行过快,这里可以放到下面的IF下面,先执行,下帧再判断是否完成
            if(!m_vStages[i]->m_bIsStageComplete)
            {
                isComplete = false;
            }
        }
        m_bIsTriggered = isComplete;
		return SkillStage::Run(now, delta);
    };
};


#define final
final class Skill
{
public:
    int m_iLimitTime;
	
	int m_BeginMode;
	int m_DelayTime;

public:
    bool m_bDanti;
    typedef std::list<SkillStage*> StageList;
    
	Skill(sBattleDemoScript& aScript, int SubSkillIndex);
    virtual ~Skill();
    
    void Begin();
    void Run(int now, int delta);
    
    bool IsComplete(){return m_lStages.empty();}
    void SetLimitTime(int aMs);
    void PushStageBack(SkillStage* aPStage) { m_lStages.push_back(aPStage); }
    
    sBattleDemoScript GetScript(){return m_Script;}
	stBattleDemoResultArr GetResultList(){ return m_Script.listResults[m_SubSkillIndex].listResults;}

public:
    StageList           m_lStages;
    sBattleDemoScript&  m_Script;
	int					m_SubSkillIndex;
    
};
#undef final

Skill* gBuildSkill(sBattleDemoScript& aScript, int SubSkillIndex);

#endif
