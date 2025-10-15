#pragma once

#include "ComponentsObject.h"

class Npc : public ComponentsObject
{
public:
	Npc(); //lua
	virtual ~Npc(); //lua

	Nuclear::TextBlockHandle    m_TitleTextureHandle;  //称谓纹理句柄
	Nuclear::ITextBlock*		m_pTitleTextBlock; //称谓的TextBlock

	virtual bool IsInHighLevel();

	void SetTitle(const std::wstring &title){ if(title != m_NpcBaseData.title) m_NpcBaseData.title = title;} //lua
	std::wstring GetTitle() const { return m_NpcBaseData.title; } //lua
	void InitNameAndTitleColor();

	virtual std::wstring GetName() //lua
	{
		if (m_data.strName != L"")
			return m_data.strName;
		else
			return m_NpcBaseData.name;
	}
	
	virtual int GetShapeID() //lua
	{
		if (m_data.ShapeID != 0)
			return m_data.ShapeID;
		else
			return m_NpcBaseData.modelID;
	}

	int	GetTotalDir(){return m_totaldir;} //lua
	int GetNpcBaseID(){return m_NpcBaseData.id;} //lua
	int GetNpcTypeID(){return m_NpcBaseData.npctype;} //lua
	void SetShapeID(int shapeID) //lua
	{
		if (shapeID >= 0)
		{
			m_data.ShapeID = shapeID;
			m_NpcBaseData.modelID = shapeID;
		}		
	}

	bool IsDefaultHide() {return m_NpcBaseData.hide!=0;}

	void BeginLeaveScene();	//npc离场表现

	//设置任务状态
	void SetQuestState(eNpcMissionState queststate, bool bHideImmediate = true);
	eNpcMissionState GetQuestState() const{ return m_data.dQuestState;}
	eNpcMissionState GetQuestState() { return m_data.dQuestState;}

	void RemoveVisitEffect(); //lua
    void AddVisitEffect(); //lua

	virtual void UpdateAction(Nuclear::XPUSA_TYPE type);

	virtual void Run(int now,int delta);
	virtual void Draw(int now);

	void		 RandomWalk();	//随机走路
	virtual bool OnSetCursor();

	virtual	bool SetAttribute(stNpcData* pData);

	virtual void SetArea1Color(int color);
	virtual void SetArea2Color(int color);
	
	//将npc设置为玩家的造型
	void SetShouXiNewShapeAndName(int modelid,std::wstring& strName,const std::map<char, int>& components); //lua

	stNPCConfig& GetNpcConfig() { return m_NpcBaseData; }

	void UpdateNpcState(int scenestate);

	void	AddNpcState(eNpcState e);
	void	RemoveNpcState(eNpcState e);

public://鼠标移向该单位时处理相关
	virtual bool CanBeAttack();
	virtual bool CanBeGiven(){return false;}
	virtual bool CanFossick(){return false;}
	virtual bool CanPickedUp();
	virtual int CanBeTriggered();

	virtual bool CanBeVisited();

	virtual bool CanFBAttack();	//是否能被工会战攻城物资使用
	virtual bool CanFBRepair();	//是否能被工会战维修物资使用	

	Nuclear::NuclearLocation	GetRandomVisitPosition();	//得到随机访问的逻辑坐标 //lua

	void SetFadeOut(); //lua
	bool IsDisappear(){return m_bDisappear;} //lua

	Nuclear::NuclearLocation GetInitLocation();
	
private:
	//宝箱的淡出相关
	bool				m_bDisappear;
	bool				m_bFadeOut;

	Nuclear::IEffect*	m_pTaskStateEffect;
	Nuclear::IEffect*	m_pTitleEffect;//称谓图片

	stNpcData			m_data;
	stNPCConfig		m_NpcBaseData;
	int					m_totaldir;			//总共的方向数
	unsigned int		m_iLastChatTime;	//上次说话的时间
	Nuclear::NuclearLocation	m_ptInitLocation;	//npc出生位置，因为明雷怪会随机走动

	Nuclear::IEffect* m_pVisitEffect;

	Nuclear::IEffect*	m_pInBattleEffect;
private:
	DWORD m_TitleColor;
	DWORD m_TitleShadowColor;
	DWORD m_MouseOverTitleColor;
	DWORD m_MouseOverTitleShadowColor;

	int	 m_iLeavingScene;

};
