#ifndef __BATTLER_H__
#define __BATTLER_H__

#include "ComponentsObject.h"
#include "BattleCommon.h"

class CArtText;

const Nuclear::NuclearLocation c_nHitBackLocation(1,1);

const Nuclear::NuclearLocation c_nAttackCloseLocation(72,48);
const Nuclear::NuclearLocation c_nAttackLocation(96,64);
const Nuclear::NuclearLocation c_nAttackSideLocation(96,-64);

const Nuclear::NuclearLocation c_nProtectLocation(48,32);
const Nuclear::NuclearLocation c_nDodgeLocation(30,20);
const Nuclear::NuclearLocation c_nCriticalLocation(48,32);
//const Nuclear::NuclearLocation c_nFlyoutLocation(720,480);

const float c_fBattlerMoveSpeed = 1750.0f;

const Nuclear::NuclearLocation c_nDodgeSideLocation(30,-30);

struct stRoleItem;

struct sBattleBuff
{
	int buffid;
	int	leftround;
	std::list<std::wstring>	tipsparam;
	Nuclear::IEffect*		buffeffect;
	std::vector<int> SpecialShow;
	sBattleBuff():buffid(0),leftround(0),buffeffect(NULL)
	{
	}
};

class BattleIllusion;

class IActionListener
{
public:
    IActionListener(){}
    virtual ~IActionListener(){}
    
    virtual void OnUpdateAction(Nuclear::XPUSA_TYPE aType, eActionType aAction) = 0;
};

class AreaBlock
{
public:
	AreaBlock(int pos);
	virtual ~AreaBlock();

public:
	bool CanPick(int eCurOperatorType, int iCurSkillID, stRoleItem& pCurItem);
	void ShowPick(bool bShow);
	bool HandlePickBtnClicked(const CEGUI::EventArgs& e);
	void Release();
	CEGUI::Window* GetBattleWindow(){ return m_pPickBtn; }
	int GetPos(){ return m_iPosition; }
private:
	bool create();
	CEGUI::Window*				m_pPickBtn;
	int m_iPosition;
};

class Battler : public ComponentsObject
{
public:
	Battler();
	virtual ~Battler();

public:
	void SetMoveOffset(int X, int Y)
	{
		m_iMoveOffsetX = X;
		m_iMoveOffsetY = Y;
	}
	void SetTargetPos(int X, int Y)
	{
		m_iTargetPosX = X;
		m_iTargetPosY = Y;
	}
	void ResetMoveOffset();
	void ResetTargetPos();

	void BeginHitMove(int nTime);
	void StopHitMove();
	void StopTeleport();

	void RegistListener(IActionListener* aPListener);
    
	void UnregistListener(IActionListener* aPListener);

    void NewPlayAction(eActionType actionType,float freq,bool realmagic,eActionType eSpecialActionType,bool magicnotify,bool attackeffect);

	BattleIllusion* AddPhantom(int ID);
	void DelPhantom(int ID);
	BattleIllusion* FindPhantom(int ID);
	BattleIllusion* FindAndAddPhantom(int ID);
	void ClearPhantom();

	Nuclear::IEffect* CreateLightEffect(const std::wstring& effect_name, Battler* pTarget,int startPos, int appearTime, int disappearTime, int totalTime);

	std::list<sBattleBuff>	GetBattleBuffList(){return m_BattleBuffList;}
	void	AddBattleBuff(int buffid, int leftround = 0);

	void	RemoveBattleBuff(int buffid);
	void	RemoveAllBattleBuff();

	bool	IsSetBattleBuff(int buffid);

	bool	IsHaveBattleBuffSpecialShow(int SpecialShowType, int ExcludeBuffID);
	void	CheckBuffBeforeOperate(eBattleOperate	BattleOperate);

	void	GetBattleBuffInfoByID(int buffid,sBattleBuff& info);
	void	GetBattleBuffInfoByIndex(int Index, sBattleBuff& info);

	int		GetBattleBuffCount();
	int		GetBattleBuffIDByIndex(int Index);

	virtual int	GetShapeID(){return m_data.iShapeID;}

	unsigned char GetNormalAlpha(){return m_NormalAlpha;}
        
	void SetShake(bool bShake)
	{
		m_bShake = bShake;
		if (m_bShake)
		{
			if (GetActionType() == eActionBattleStand)
			{
				GetSprite()->StartShake();
			}
		}
		else
		{
			GetSprite()->StopShake();
		}
	}

	bool GetShake() { return m_bShake; }

	bool IsPetCanUse(int pKey);
	void SetPetUsed(int pKey);

	void	SetCatchFailure(bool b){m_bCatchFailure = b;}
	void	AddCatchEffect();
	void	ClearCatchEffect();

protected:
	Nuclear::TextBlockHandle	m_TitleTextureHandle;  //称谓纹理句柄
	Nuclear::ITextBlock*		m_pTitleTextBlock; //称谓的TextBlock

	Nuclear::TextBlockHandle	m_FlagTextureHandle;  //标记纹理句柄
	Nuclear::ITextBlock*		m_pFlagTextBlock; //标记的TextBlock

	bool				m_bShowHPBarBig;
	Nuclear::PictureHandle		m_pFlagPic;			//标记底图

public:
	int				GetAttackDistance(){return m_iAttackDistance;}
	virtual void    ChangeSpriteModel(const int& modelid, int effectID = 11045);
	virtual bool	IsCanUseComponentsAfterChangeModel();

	bool			IsDeath() { return m_bDeath; }
	bool			IsDeathEx() { return m_bDeath || m_bGhost || m_bDieVanish || m_bFlyOut; }
	bool			IsDeathOnGround(){return m_bDeathOnGround;}
	sBattlerData	GetBattlerData(){return m_data;}
	bool			IsGeneralDie();	//是否要死了，包括死亡倒地，被击飞，鬼魂状态
	bool			IsFlyOut();		//是否被击飞了
	bool			IsDieVanish();	//是否倒地消失
	virtual			std::wstring GetName(){return m_strName;}
	bool			IsDisappear(){return m_bDisappear;}	//判断是否应该删除
	void			SetDisappear(bool b){m_bDisappear = b;}
	int				GetBattleID() const {return m_data.iBattleID;}
	eBattlerType	GetBattlerType() const {return m_data.BattlerType;}

	//是否在战场右下角
	bool IsFriendSide()	 { return m_data.iBattleID <= FriendMaxID && m_data.iBattleID >= FriendMinID; }
	bool IsEnemySide() { return m_data.iBattleID <= EnemyMaxID && m_data.iBattleID >= EnemyMinID; }
	bool IsWatcher() { return m_data.iBattleID <= WatchMaxID && m_data.iBattleID >= WatchMinID; }
	bool IsWatcherTopRight() { return m_data.iBattleID <= 35 && m_data.iBattleID >= WatchMinID; }
	bool IsWatcherBottomLeft() { return m_data.iBattleID > 35 && m_data.iBattleID <= WatchMaxID; }

	bool SetCharacter(int64_t characterID,const int&shapeID,const std::map<char,int>& components);
	bool SetCharacter(int64_t characterID,const int&shapeID, const std::map<char,int>& components, std::wstring modelName, std::wstring hair, std::wstring head, std::wstring body);	//设置角色,指定模型名字(编辑器专用)
	bool SetPet(int64_t petID,int shapeID);
	bool SetPet(int64_t petID,int shapeID, std::wstring modelName, std::wstring hair, std::wstring head, std::wstring body);		//设置宠物,指定模型名字(编辑器专用)
	bool SetCreeps(int64_t creepID,int shapeID);
	virtual void SetNormalAlpha(unsigned char a, bool immediately);
	virtual void SetAlphaCtrl(float fA);
	void SetCurrentAlpha(unsigned char a);

	virtual void SetTitle(const std::wstring &title){ if(title != m_data.strTitle) m_data.strTitle = title;}
	virtual std::wstring GetTitle() const { return m_data.strTitle; }
	void SetTitleID(const int &);
	int GetTitleID() const;
	void UpdataTitleTexture();

	int64_t	GetBattleDataID(){return m_data.BattlerDataID;}

	void SetDefaultAction(eActionType actionType,bool holdlastframe=false,float freq=g_defaultActionFrequence);
	void ResetSpeed();
	//动作相关
	void SetMoveToParam(Nuclear::NuclearLocation loc,int time);
	void MoveTo();	//广义上的移动，不需要做某个动作
	void RunTo(Nuclear::NuclearLocation ptMoveTarget);	//这里的ptMoveTarget是像素坐标
	void RunBack();
	void FlyOut();
	void RunRollAction(int delta);

	void ActionRunaway();
	void PlayAction(eActionType actionType,float freq=g_defaultActionFrequence,bool playmagiceffect=false,eActionType eSpecialActionType = eActionNull,bool magicnotify = false,bool attackeffect = true);
	//受击声音音效
	std::wstring GetActionEffectSoundName();
	void ChangeRollDirection();
	//坐标相关
	bool IsOnBattlePosition();
	
	void SetBattleDirection();
	void SetBattleLocation();
	Nuclear::NuclearLocation GetAttackerPositionBehind();		//被击攻击者的坐标
	Nuclear::NuclearLocation GetAttackerClosePosition();		//被击攻击者更近些的坐标
	Nuclear::NuclearLocation GetAttackerPosition(int attackdistance=96);//得到被击时攻击者的坐标
	Nuclear::NuclearLocation GetAttackerFarPosition();

	Nuclear::NuclearLocation GetAttackerSidePosition(bool bleft);

	Nuclear::NuclearLocation GetProtecterPosition();			//得到被击时，保护者的坐标
	Nuclear::NuclearLocation GetDodgePosition();				//得到被击时，闪避退后的坐标
	
	Nuclear::NuclearLocation GetBattleLocation(){return m_ptBattlePosition;}

	virtual void UpdateAction(Nuclear::XPUSA_TYPE type);

	virtual void Run(int now,int delta);
	virtual void Draw(int now);
	virtual bool OnSetCursor();

	virtual	bool SetAttribute(sBattlerData* pData);
	bool SetAttribute(sBattlerData* pData, std::wstring modelName, std::wstring hair, std::wstring head, std::wstring body);	//设置属性,指定模型名字(编辑器专用)
	void ResetState();
	bool CanUseTo(int usetype);					//是否可作用函数

	bool AffectByAttack();						//是否能够被攻击

	bool AffectedBySkill(int pSkillID);			//是否可被选中技能影响
	bool AffectedByItem(int itemuse);			//是否可被选中物品影响
	bool AffectedByCatch();						//是否可被捕捉

	bool AffectByProtect();						//能否被保护

	bool CanBeCommand();						//能否被指挥

public://战斗Demo表现相关
	bool ProcessAttackAction(sBattleDemoAttack* pAttack);			//攻击执行
	bool ProcessResultAction(int eResult, int FlagType, int aOnhitDelay, int aOnhitDir, int HPChange, int MPChange, int uplimitHPChange = 0, int SPChange = 0, bool bReturnhurt = false, bool battackaction = false, int HPChangeGodBless = 0, bool bPlayEffect = true);//攻击结果
	void ProcessBuffChange(const sBattleBuffResult& buffdatalist);
	void ProcessSpecialAction();//执行后续结果，击飞，死亡等等

	void OnHitNumber(int num, eBattleResult eHitType, int FlagType, bool critic = false);//头上掉冒血的数字

	void OnHpChangeOutSideBattle(int curhp);

public:
	void ResetBattlerLocation();
		
	//朝向某个单位
	void TurnTo(SceneObject* pSceneObj);

//战斗单位头顶血槽
private:
	CEGUI::ProgressBarTwoValue* m_pHpBar;
	CEGUI::Window*              m_pLevelText;
	CEGUI::Window*				m_pPickBtn;
	CEGUI::Window*				m_pSkillNameRoot;
	CEGUI::Window*				m_pSkillNameImage;
	CEGUI::Window*				m_pSkillNameText;
	CEGUI::AnimationInstance*	m_pSkillNameAni;
	CEGUI::AnimationInstance*	m_pSkillNameAni2;
	std::vector<CEGUI::Window*>	m_pEpImages;

	Nuclear::PictureHandle		m_pJobPic;

public:
	CEGUI::Window* m_DownPickBtn;
	int m_TipTime;
	bool m_TipShow;
	bool HandlePickBtnClicked(const CEGUI::EventArgs& e);
	bool HandlePickBtnUp(const CEGUI::EventArgs& e);
	bool HandlePickBtnDown(const CEGUI::EventArgs& e);
	void RunPick(int now, int delta);
	void ShowBattlerTip(bool bShow);

	void ShowPickBtn(bool bShow);
	bool CanPick(int eCurOperatorType, int iCurSkillID, stRoleItem& pCurItem);

	bool HandleSkillNameStopped(const CEGUI::EventArgs& e);
	void ShowSkillName(bool bShow, int iSkillID = 0);
	//void StartSkillName

	bool m_bShowHPBar;
	void AddHpBar();			//添加血条
	void RemoveHpBar();			//删除血条
	void UpdateHpBarLocation();	//更新血条位置
	void RefreshHpBar();

	void ShowHpBar(bool b);

	void SetEPValue(int vl);
	int GetEPValue();
	void SetHPValue(int vl);
	int GetHPValue();

	void AddLevelText(int level);

	bool AddBabyTips();

	CEGUI::Window* GetBattleWindow(){ return m_pPickBtn; }

	void SetOperateState(eOperateState state);

	void ClientSetOperateState(eOperateState state);//每回合操作开始前，客户端自设所有人状态为“准备中”，demo开始前设为“NULL”
	void PopEffectText(int type);
public:
	void OnBeforeRender(int now);
	
	void SetSelected(bool b){m_bSelected = b;}

	void SetGlobalAnimFreq(float freq){m_fGlobalAnimFreq = freq;}	//设置全局播放动画速度倍率
	float GetGlobalAnimFreq(){ return m_fGlobalAnimFreq; }

	void SetBattlerDemoSpeedRate(float rate);

	void SetDemoShowEnd(bool b);
	void RrfreshDemoShow(bool b);

	void AddDemoShowNotEndEffect(bool);

	const std::wstring& GetHitEffect(){return m_RoleHitEffect;}

public:
	void SetHitBack();

	void ClearHitBack();

	void ContinueHitBack();

	void ReadyBattleDemo();

	void SetBattleAttackFinish(bool b);

	void SetFlag(std::wstring flag);
	std::wstring GetFlag(){ return m_strflag; };
public:

	int  m_iHitBackDelay;

	int  m_iHitBackDelayTime;

	bool m_bBattleAttackFinish;//自己的出手结束
	bool m_bShowName;

	//操作状态相关
private:
	Nuclear::IEffect*  m_pOperateStateEffect;

private:
	//战斗者bufflist
	std::list<sBattleBuff>	m_BattleBuffList;

	std::map<int, BattleIllusion*> m_Phantoms;

	std::wstring	m_AttackEffectName;
	static Nuclear::PictureHandle m_GMpic;
	std::vector<Nuclear::NuclearLocation>	m_lightingPos;

	std::wstring	m_strflag;
private:
	bool				m_bShake;
	unsigned char		m_NormalAlpha;		//最终的alpha值，即战斗单位应该有的alpha值，隐身状态的话就是半透明
	float				m_CurrentAlpha;		//最终的alpha值，即战斗单位应该有的alpha值，隐身状态的话就是半透明
	float				m_fAlphaCtrl;		//控制透明度参数
	bool				m_bVanishAlphaChange;	//false为变小，true为变大
	int					m_iFlashTime;			//闪烁几次消失 暂定为2
	bool				m_bDisappear;
	sBattlerData		m_data;
	std::wstring		m_strName;
	Nuclear::NuclearLocation	m_ptBattlePosition;	//像素坐标

	Nuclear::NuclearLocation	m_ptFlyOutPosition;	//被击飞时要飞去的坐标
	bool				m_bDeath;		//死亡倒地
	bool				m_bDeathOnGround;//倒地动作做完
	bool				m_bDieVanish;	//死亡闪烁消失
	bool				m_bBeginVanish;	//倒地时才应该开始闪烁消失

	bool				m_bFlyOut;		//被击飞
	bool				m_bGhost;		//鬼魂状态

	bool				m_bCatchFailure;//捕捉失败

private:
	float				m_fInitScale;		//初始大小，应该是1.0f的
	float				m_fMinimizeSpeed;	//缩放速度
	Nuclear::IEffect*	m_pCatchEffect;	//捕捉特效
	int					m_iRiveDelay;	//原地复活延迟时间

	bool				m_bCanBeCatch;		//是否能被捕捉
	bool				m_bCreepsShowHpBar;	//是否显示血条（对于怪物来说）

	int					m_iAttackDistance;	//模型攻击距离的比例，默认96

	Nuclear::IEffect*  m_pFootprintEffect;

private:
	int		m_FlySpeedX;
	int		m_FlySpeedY;
	int		m_FlyHitTime;
	float	m_fFlyMoveX;
	float	m_fFlyMoveY;

	bool	m_bJumpRun;	//跳斩

	struct sMoveToInfo
	{
		Nuclear::NuclearLocation desloc;
		int time;
		sMoveToInfo() :desloc(0, 0), time(0)
		{}
	};
	sMoveToInfo m_stMoveInfo;

private:

	bool m_bDemoShowEnd;	//录像是否播放完毕

	Nuclear::IEffect* m_pDemoShowNotEndEffect;

	std::wstring					m_RoleHitEffect;	//该单位打中对方后，对方的受击特效

	float	m_fGlobalAnimFreq;		//全局播放动画速度设置
	float	m_fDemoSpeedRate;

	bool	m_bSelected;

	int		m_HPChangeGodBless;

	CArtText* m_pArtText;
	CArtText* m_pEffectText;
	std::vector<IActionListener*>   m_Listeners;
	int     m_iOnhitTick;
	int     m_iOnhitFrame;
	int     m_iOnhitDelay;
	int		m_iOnhitDir;//上8,下2,左4,右6,//这里为了策划填表方便所以如果填写的是2,则人物实际上是向6的方向移动,其它方向依此类推
	int		m_iMoveOffsetX;
	int		m_iMoveOffsetY;
	int     m_bHitMoving;

	int		m_iTargetPosX;
	int		m_iTargetPosY;

private:
	bool m_bFirstTickVisible;
	int	 m_iSetVisibleDelay;

	bool m_bEquipEffectOn;
};

#endif
