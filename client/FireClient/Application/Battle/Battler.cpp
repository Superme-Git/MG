#include "stdafx.h"
#include "Battler.h"
#include "BattleIllusion.h"
#include "GameScene.h"
#include "GameOperateState.h"
#include "BattleManager.h"
#include "BattleMagicControl.h"
#include "ArtTextManager.h"
#include "MainRoleDataManager.h"
#include "GameUIManager.h"
#include "GameTable/npc/CMonsterConfig.h"
#include "GameApplication.h"
#include "GameTable/buff/CBuffConfig.h"
#include "BattleScriptPlayer.h"
#include "effect/nulightingEffect.h"
#include "RoleItem.h"
#include "GameTable/skill/CSkillTypeConfig.h"

#include "CallLuaUtil.h"
#define AttackDistance 96
const float c_fMinimizeSpeed = 0.0002f;

AreaBlock::AreaBlock(int pos) :m_pPickBtn(NULL), m_iPosition(pos)
{
}
AreaBlock::~AreaBlock()
{
	Release();
}
bool AreaBlock::create()
{
	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
	if (m_pPickBtn == NULL)
	{
		CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
		m_pPickBtn = static_cast <CEGUI::Window*>(winMgr.createWindow("TaharezLook/StaticImage", OnlyName));
	}
	if (m_pPickBtn)
	{
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow != NULL)
		{
			Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
			int yoffset = 0;
			m_pPickBtn->setAlwaysOnBottom(true);
			pRootWindow->addChildWindow(m_pPickBtn);
			m_pPickBtn->SetOnShiedUIState(false);
			Nuclear::NuclearLocation loc = GetBattleManager()->GetBattleUILocation(m_iPosition) + Nuclear::NuclearLocation(-58, -120);
			m_pPickBtn->setPosition(CEGUI::UVector2(cegui_absdim((float)loc.x), cegui_absdim((float)loc.y)));
			m_pPickBtn->setSize(CEGUI::UVector2(cegui_absdim(115.0f), cegui_absdim(116.0f)));
			m_pPickBtn->subscribeEvent(CEGUI::Window::EventMouseClick, CEGUI::Event::Subscriber(&AreaBlock::HandlePickBtnClicked, this));
		}
		m_pPickBtn->setVisible(true);
	}
	return true;
}
bool AreaBlock::CanPick(int eCurOperatorType, int iCurSkillID, stRoleItem& pCurItem)
{
	switch ((eGameCursorType)eCurOperatorType)
	{
	case eGameCursorType_BattleSkill:	//技能
	{
		int skilluse = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("RoleSkillManager.GetSkillUseType",iCurSkillID);
		return (skilluse & BattleUseFriendArea) != 0x00;
	}
	break;
	case eGameCursorType_BattleItem:		//道具
	{
		int itemuse = pCurItem.m_Battleuse;
		return (itemuse & BattleUseFriendArea) != 0x00;
	}
	break;
	default:
	{
		return false;
	}
	break;
	}
	return false;
}
void AreaBlock::ShowPick(bool bShow)
{
	if (bShow == true && m_pPickBtn == NULL)
	{
		create();
	}
	if (m_pPickBtn == NULL)	return;
	if (bShow)
	{
		const char* pImage = "set:fightui image:pitch_on";
		m_pPickBtn->setProperty("Image", pImage);
	}
	else
	{
		m_pPickBtn->setProperty("Image", "");
	}
}
bool AreaBlock::HandlePickBtnClicked(const CEGUI::EventArgs& e)
{
	if (!GetBattleManager()->IsInBattleOperate() || GetBattleManager()->IsAutoOperate())
	{
		return true;
	}
	GetBattleManager()->PickArea(m_iPosition);
	return false;
}
void AreaBlock::Release()
{
	if (m_pPickBtn)
	{
		m_pPickBtn->destroy();
		m_pPickBtn = NULL;
	}
}

Nuclear::PictureHandle Battler::m_GMpic = Nuclear::INVALID_PICTURE_HANDLE;
static int s_RollDirFrame = 0;
Battler::Battler()
: m_iOnhitTick(0)
, m_iOnhitFrame(0)
, m_iOnhitDelay(0)
, m_iOnhitDir(2)
, m_iMoveOffsetX(0)
, m_iMoveOffsetY(0)
, m_bHitMoving(false)
, m_iTargetPosX(0)
, m_iTargetPosY(0)
, m_ptBattlePosition(-1,-1)
, m_strName(L"")
, m_bDisappear(false)
, m_bDeath(false)
, m_bFlyOut(false)
, m_bDieVanish(false)
, m_bBeginVanish(false)
, m_bDeathOnGround(false)
, m_bGhost(false)
, m_bShowHPBar(true)
, m_pHpBar(NULL)
, m_pJobPic(Nuclear::INVALID_PICTURE_HANDLE)
, m_pFlagPic(Nuclear::INVALID_PICTURE_HANDLE)
, m_pPickBtn(NULL)
, m_pSkillNameRoot(NULL)
, m_pSkillNameImage(NULL)
, m_pSkillNameText(NULL)
, m_pSkillNameAni(NULL)
, m_pSkillNameAni2(NULL)
, m_DownPickBtn(NULL)
, m_TipTime(0)
, m_TipShow(false)
, m_pLevelText(NULL)
, m_pOperateStateEffect(NULL)
, m_bCanBeCatch(false)
, m_bShowHPBarBig(false)
, m_ptFlyOutPosition(Nuclear::NuclearLocation(0,0))
, m_bCreepsShowHpBar(false)
, m_bSelected(false)
, m_iRiveDelay(-1)
, m_NormalAlpha(255)
, m_CurrentAlpha(255)
, m_fAlphaCtrl(1.0)
, m_bShake(false)
, m_FlySpeedX(0)
, m_FlySpeedY(0)
, m_FlyHitTime(1)
, m_bVanishAlphaChange(true)
, m_iFlashTime(3)
, m_bCatchFailure(true)
, m_fMinimizeSpeed(c_fMinimizeSpeed)
, m_pCatchEffect(NULL)
, m_fInitScale(1.0f)
, m_bJumpRun(false)
, m_fGlobalAnimFreq(1.0f)
, m_fDemoSpeedRate(1.0f)
, m_iAttackDistance(AttackDistance)
, m_bDemoShowEnd(true)
, m_pDemoShowNotEndEffect(NULL)
, m_bFirstTickVisible(true)
, m_iSetVisibleDelay(100)
, m_iHitBackDelay(-1)
, m_iHitBackDelayTime(0)
, m_RoleHitEffect(L"")
, m_bBattleAttackFinish(false)
, m_TitleTextureHandle(Nuclear::INVALID_TEXTBLOCK_HANDLE)
, m_pTitleTextBlock(NULL)
, m_FlagTextureHandle(Nuclear::INVALID_TEXTBLOCK_HANDLE)
, m_pFlagTextBlock(NULL)
, m_pArtText(NULL)
, m_pEffectText(NULL)
, m_HPChangeGodBless(0)
, m_bShowName(true)
, m_bEquipEffectOn(false)
, m_fFlyMoveX(0.0f)
, m_fFlyMoveY(0.0f)
{
	m_eSceneType = eSceneObjBattler;
	m_pArtText = new CArtText;
	m_pArtText->SetOwner(this);
	m_pArtText->SetDelayTime(500);
	m_pEffectText = new CArtText;
	m_pEffectText->SetOwner(this);
	m_pEffectText->SetDelayTime(500);	
}
Battler::~Battler()
{
	delete m_pArtText;
	delete m_pEffectText;

	if (GetBattleManager() && GetBattleManager()->GetMouseOverObjects() == this)
	{
		GetBattleManager()->ClearMouseOverObjects();
	}
	
	if (GetBattleManager())
	{
		if (GetBattleManager()->GetMainBattleChar() == this)
		{
			GetBattleManager()->ClearMainBattleChar();
		}
		else if (GetBattleManager()->GetMainBattlePet() == this)
		{
			GetBattleManager()->ClearMainBattlePet();
		}
	}
	RemoveHpBar();					//删除bar

	if (m_pDemoShowNotEndEffect)
	{
		RemoveDurativeEffect(m_pDemoShowNotEndEffect);
		m_pDemoShowNotEndEffect = NULL;
	}

	if (m_pCatchEffect)
	{
		RemoveDurativeEffect(m_pCatchEffect);
		m_pCatchEffect = NULL;
	}
    
	if (m_pOperateStateEffect)
	{
		RemoveDurativeEffect(m_pOperateStateEffect);
		m_pOperateStateEffect = NULL;
	}
	for (std::list<sBattleBuff>::iterator it = m_BattleBuffList.begin(); it != m_BattleBuffList.end(); it++)
	{
		if (it->buffeffect)
		{
			RemoveDurativeEffect(it->buffeffect);
			it->buffeffect = NULL;
		}
	}
	m_BattleBuffList.clear();

	if (m_TitleTextureHandle!=Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_TitleTextureHandle);
		}
		m_TitleTextureHandle=Nuclear::INVALID_TEXTBLOCK_HANDLE;
		m_pTitleTextBlock=NULL;
	}
	if (m_FlagTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_FlagTextureHandle);
		}
		m_FlagTextureHandle = Nuclear::INVALID_TEXTBLOCK_HANDLE;
		m_pFlagTextBlock = NULL;
	}
	m_lightingPos.clear();
}
void Battler::RunRollAction(int delta)
{
	if (m_iOnhitDelay > 0)
	{
		return;
	}

	if (s_RollDirFrame >= 100)
	{
		Nuclear::NuclearDirection CurDir = GetDirection();
		Nuclear::NuclearDirection NewDir = CurDir;
		if (CurDir == Nuclear::XPDIR_TOPLEFT)
		{
			NewDir = Nuclear::XPDIR_TOPRIGHT;
		}
		else if (CurDir == Nuclear::XPDIR_TOPRIGHT)
		{
			NewDir = Nuclear::XPDIR_BOTTOMRIGHT;
		}
		else if (CurDir == Nuclear::XPDIR_BOTTOMRIGHT)
		{
			NewDir = Nuclear::XPDIR_BOTTOMLEFT;
		}
		else if (CurDir == Nuclear::XPDIR_BOTTOMLEFT)
		{
			NewDir = Nuclear::XPDIR_TOPLEFT;
		}
		SetDirection(NewDir);
		s_RollDirFrame = 0;
	}
	s_RollDirFrame += delta;
    
	float fx = m_FlySpeedX*delta * 0.001f;
	float fy = m_FlySpeedY*delta * 0.001f;

	m_fFlyMoveX += fx;
	m_fFlyMoveY += fy;

	int x = m_fFlyMoveX;
	int y = m_fFlyMoveY;
	m_fFlyMoveX -= x;
	m_fFlyMoveY -= y;

	int posx = GetSprite()->GetLocation().x;
	int posy = GetSprite()->GetLocation().y;
	Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
	if (posx + x >= cp.right || posx + x <= cp.left)
	{
		m_FlySpeedX = -m_FlySpeedX;
		m_FlyHitTime--;
	}
	else
	{
		posx += x;
	}
	if (posy + y >= cp.bottom || posy + y <= cp.top)
	{
		m_FlySpeedY = -m_FlySpeedY;
		m_FlyHitTime--;
	}
	else
	{
		posy += y;
	}
	//撞够了就消失吧
	if (m_FlyHitTime <= 0)
	{
		m_bDisappear = true;
	}
	else
	{
		GetSprite()->SetLocation(Nuclear::NuclearLocation(posx,posy));
	}
}
void Battler::SetHitBack()
{
	if (m_iHitBackDelayTime == 0)
	{
		m_iHitBackDelayTime = 4;
		SetLogicLocation(GetLogicLocation() + (IsFriendSide()?3*c_nHitBackLocation:-3*c_nHitBackLocation));
	}
	else
	{
		m_iHitBackDelayTime++;
		SetLogicLocation(GetLogicLocation() + (IsFriendSide()?2*c_nHitBackLocation:-2*c_nHitBackLocation));
	}
	m_iHitBackDelay = 150 - m_iHitBackDelayTime*20;
}
void Battler::ContinueHitBack()
{
	m_iHitBackDelayTime--;
	m_iHitBackDelay = 150 - m_iHitBackDelayTime*20;
	int backdisrate = m_iHitBackDelayTime/2;
	if (m_iHitBackDelayTime > 0)
	{
		SetLogicLocation(GetLogicLocation() + (IsFriendSide()?backdisrate*c_nHitBackLocation:-backdisrate*c_nHitBackLocation));
		UpdateHpBarLocation();
	}
	else
	{
		ClearHitBack();
	}
}
void Battler::ClearHitBack()
{
	m_iHitBackDelay = -1;
	m_iHitBackDelayTime = 0;
	if (!m_bFlyOut)
	{
		SetLocation(m_ptBattlePosition);
		UpdateHpBarLocation();
	}
}
void Battler::BeginHitMove(int nTime)
{
	m_iOnhitTick = nTime;
	m_bHitMoving = true;
}
void Battler::StopHitMove()
{
	m_iOnhitTick = 0;
	m_bHitMoving = false;
	m_iOnhitFrame = 0;
	m_iOnhitDelay = -100001;
	m_iOnhitDir = 2;
	SetLocation(Nuclear::NuclearLocation(m_iTargetPosX, m_iTargetPosY));
}
bool Battler::ProcessResultAction(int eResult, int FlagType, int aOnhitDelay, int aOnhitDir, int HPChange, int MPChange, int uplimitHPChange, int SPChange, bool bReturnhurt, bool battackaction, int HPChangeGodBless, bool bPlayEffect)
{
	m_iOnhitDelay = aOnhitDelay;

	Nuclear::NuclearDirection _dir = GetSprite()->GetDirection();
	bool _side = IsFriendSide();
	switch (_dir)
	{
	case Nuclear::XPDIR_BOTTOMRIGHT:
		aOnhitDir = _side ? 8 : 2;
		break;
	case Nuclear::XPDIR_TOPLEFT:
		aOnhitDir = _side ? 2 : 8;
		break;
	}
	m_iOnhitDir = aOnhitDir;
	if (m_iOnhitDir != 2 && m_iOnhitDir != 4 && m_iOnhitDir != 6 && m_iOnhitDir != 8)
	{
		m_iOnhitDir = 2;
	}

	if(HPChangeGodBless != 0)
		m_HPChangeGodBless = HPChangeGodBless;

	bool dealULHP = false;	//是否已经处理了血上限变化
	int result = (int)eBattleResult_HPChange;
	while (result < eBattleResultMax)
	{
		if (result & eResult)
		{
			switch((eBattleResult)result)
			{
                case eBattleResult_HPChange:	//血量变化
				{
					if (HPChange != 0)
					{
						if (eResult & eBattleResult_ULHPChange)
						{
							dealULHP = true;
							if (uplimitHPChange > 0)
							{
								m_data.UpLimitHp += uplimitHPChange;
								if (m_data.UpLimitHp < 0)
									m_data.UpLimitHp = 0;
								if (m_data.UpLimitHp > m_data.MaxHp)
									m_data.UpLimitHp = m_data.MaxHp;
								if (GetBattleManager() && GetBattleManager()->GetMainBattleChar() == this)
								{
									cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainRoleData_UpdateHpChange", 0, uplimitHPChange);
								}
                                
								if (eResult & eBattleResult_Critic)	
									OnHitNumber(HPChange, eBattleResult_HPChange, FlagType, true);
								else
									OnHitNumber(HPChange, eBattleResult_HPChange, FlagType);
							}
							else
							{
								if (eResult & eBattleResult_Critic)
									OnHitNumber(HPChange, eBattleResult_HPChange, FlagType, true);
								else
									OnHitNumber(HPChange, eBattleResult_HPChange, FlagType);
                                
								m_data.UpLimitHp += uplimitHPChange;
								if (m_data.UpLimitHp < 0)
									m_data.UpLimitHp = 0;
								if (m_data.UpLimitHp > m_data.MaxHp)
									m_data.UpLimitHp = m_data.MaxHp;
								if (GetBattleManager() && GetBattleManager()->GetMainBattleChar() == this)
								{
									cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainRoleData_UpdateHpChange", 0, uplimitHPChange);
								}
							}
						}
						else
						{                            
							if (eResult & eBattleResult_Critic)	
								OnHitNumber(HPChange, eBattleResult_HPChange, FlagType, true);
							else
								OnHitNumber(HPChange, eBattleResult_HPChange, FlagType);
						}
					}
				}
                    break;
                case eBattleResult_MPChange:	
				{
					if (MPChange != 0)
						OnHitNumber(MPChange, eBattleResult_MPChange, FlagType);
				}
                    break;
                case eBattleResult_SPChange:	
				{
					if (SPChange != 0 && GetBattleManager() && GetBattleManager()->GetMainBattleChar() == this)
					{
						cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainRoleData_UpdateSpChange", SPChange);
					}
				}
                    break;
				case eBattleResult_Hit:
				{
					if (!(eResult & eBattleResult_Defence) && !(eResult & eBattleResult_Parry))
					{
						PlayEffect(L"geffect/skill/mt3_beiji/mt3_beiji");
						int hitmove = GetTableNumberFromLua("npc.cnpcshape", GetShapeID(), "hitmove");
						if (hitmove != 0)
						{
							SetDefaultAction(eActionOnHit1, true);
							NewPlayAction(eActionOnHit1, 1.0f, true, eActionNull, true, true);
							BeginHitMove(90);
						}
						else
						{
							NewPlayAction(eActionOnHit1, 1.0f, true, eActionNull, true, true);
						}
					}
				}
				break;
                case eBattleResult_Parry:	
				{
					PlayEffect(MHSD_UTILS::GetEffectPath(11014));
					SetDefaultAction(eActionDefence);
					NewPlayAction(eActionDefence, 1.0f, true, eActionNull, true, true);
					m_iOnhitDelay *= 1.75;
					BeginHitMove(65);
				}
                    break;
                case eBattleResult_Defence:	//目标防御
				{
					if (bPlayEffect)
						PopEffectText(eFangYu);
					SetDefaultAction(eActionDefence);
					NewPlayAction(eActionDefence, 1.0f, true, eActionNull, true, true);
					m_iOnhitDelay *= 1.75;
					BeginHitMove(65);
				}
                    break;
                case eBattleResult_Dodge://目标闪避
				{
					float time = 150.0f / m_fDemoSpeedRate;
					float time1 = 50.0f / m_fDemoSpeedRate;

					GetSprite()->TeleportWithBlur(GetDodgePosition(), static_cast<int>(time), static_cast<int>(time1));
					PlayAction(eActionDodge);
				}
                    break;
                case eBattleResult_Death:
				{
					m_bDeath = true;
					SetHPValue(0);
				}
                    break;
                case eBattleResult_Summonback:
				{	//目标消失
					if (GetBattlerType() != eBattlerCharacter)
						m_bDisappear = true;
				}
                    break;
                case eBattleResult_Runaway:
				{
					m_bDisappear = true;
					if (GetBattlerType() == eBattlerCharacter && GetBattleMagicControl()->FindBattlerByID(GetBattleID() + 5))
					{
						GetBattleMagicControl()->FindBattlerByID(GetBattleID() + 5)->SetDisappear(true);
					}
					else if (GetBattleManager() && this == GetBattleManager()->GetMainBattlePet())
					{
						cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainPetDataManager_SetBattlePetState", ePetState_AlreadyFight);
						cocos2d::gGetScriptEngine()->executeGlobalFunction("MainRoleData_PostBattlePetStateChangeEvent");
					}
				}
                    break;
                case eBattleResult_Seized:
				{
					if (GetBattlerType() != eBattlerCharacter)
						m_bDisappear = true;
				}
                    break;
                case eBattleResult_Relive:
				{
					m_bDeath = false;
					m_bGhost = false;
					m_bDeathOnGround = false;
					SetDefaultAction(eActionBattleStand);
					SetLocation(m_ptBattlePosition);
					if (IsFriendSide())
					{
						SetDirection(Nuclear::XPDIR_TOPLEFT);
					}
					else if (IsEnemySide())
					{
						SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
					}
				}
                    break;
                case eBattleResult_Summon:
				{
                    printf("show new battler\n");
					GetBattleMagicControl()->ShowNewBattler();
				}
                    break;
                case eBattleResult_Rest://目标休息
				{
                    
				}
                    break;
                case eBattleResult_ULHPChange://目标当前血上限变化
				{
					if (!dealULHP)
					{
						m_data.UpLimitHp += uplimitHPChange;
						if (m_data.UpLimitHp < 0)
							m_data.UpLimitHp = 0;
						if (m_data.UpLimitHp > m_data.MaxHp)
							m_data.UpLimitHp = m_data.MaxHp;
						if (GetBattleManager() && GetBattleManager()->GetMainBattleChar() == this)
						{
							cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainRoleData_UpdateHpChange", 0, uplimitHPChange);
						}
						if (uplimitHPChange != 0)
						{
							if (uplimitHPChange > 0)
							{
								PlayEffect(MHSD_UTILS::GetEffectPath(10155));
							}
						}
					}
				}
                    break;
                case eBattleResult_FlyOut:
				{
					if (m_data.iShapeID < 1000 || m_data.iShapeID == 6100 || m_data.iShapeID == 6126)
					{
						m_bDieVanish = true;
					}
					else
					{
						m_bFlyOut = true;
                        FlyOut();
					}
					if (GetBattleManager() && this == GetBattleManager()->GetMainBattlePet())
					{
						GetBattleManager()->ClearMainBattlePet();
						cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainPetDataManager_SetBattlePetState", ePetState_Flyout);
						cocos2d::gGetScriptEngine()->executeGlobalFunction("MainRoleData_PostBattlePetStateChangeEvent");
					}
					GetSprite()->SetComponentAniReleaseFlag(Nuclear::XPANI_RELEASE_IMMEDIATE_GC);
					SetHPValue(0);
				}
                    break;
                case eBattleResult_Ghost:
				{
					m_bGhost = true;
					SetHPValue(0);
				}
                    break;
				case eBattleResult_NotDefence:
				{
					if (FlagType == 1)
					{
						PlayEffect(MHSD_UTILS::GetEffectPath(11009));
					} 
					else if (FlagType == 2)
					{
						PlayEffect(MHSD_UTILS::GetEffectPath(11010));
					}
					break;
				}
				case eBattleResult_Absorb:
				{
					PlayEffect(MHSD_UTILS::GetEffectPath(11007));
					if (IsFriendSide())
					{
						PlayEffect(MHSD_UTILS::GetEffectPath(11008), -50, 50);
					}
					else
					{
						PlayEffect(MHSD_UTILS::GetEffectPath(11008), -50, 50);
					}
					break;
				}
				case eBattleResult_DestroyMP:
				{
					PlayEffect(MHSD_UTILS::GetEffectPath(11006));
					break;
				}

                break;
			}
		}
		result = result<<1;
	}
	//处理逃跑
	if (m_actionType == eActionRunaway && !(eResult & eBattleResult_Runaway))
	{
		if (GetBattlerType() == eBattlerCharacter)
		{
			Battler* pPet = GetBattleMagicControl()->FindBattlerByID(GetBattleID() + 5);
			if (pPet && !pPet->IsGeneralDie())
			{
				pPet->PlayAction(eActionBattleStand);
				pPet->SetBattleDirection();
			}
		}
		PlayAction(eActionBattleStand);
		SetBattleDirection();
	}
	return true;
}
void Battler::ProcessSpecialAction()
{
	if (m_bDeath || m_bGhost || m_bDieVanish)
	{
		TurnTo(this);
		m_actionType = eActionDeath;
		SetDefaultAction(eActionDeath, true);

		g_BattleScriptPlayer.DealAIActionWhileBattlerDie(m_data.iBattleID);
	}
	else if (m_bFlyOut)		
	{
		g_BattleScriptPlayer.DealAIActionWhileBattlerDie(m_data.iBattleID);
		FlyOut();
	}
}
bool Battler::ProcessAttackAction(sBattleDemoAttack* pAttack)
{
	if (pAttack)
	{
		if (pAttack->HPConsume != 0)
		{
			m_data.Hp += pAttack->HPConsume;
			if (m_data.Hp < 0)
				m_data.Hp = 0;
			if (m_data.Hp > m_data.UpLimitHp)
				m_data.Hp = m_data.UpLimitHp;
		}
        
		if (GetBattleManager())
		{
			if (GetBattleManager()->GetMainBattleChar() == this)
			{
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainRoleData_UpdateHpChange", pAttack->HPConsume, 0);
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainRoleData_UpdateMpChange", pAttack->MPConsume, 0);
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainRoleData_UpdateSpChange", pAttack->SPConsume);
			}
			else if (GetBattleManager()->GetMainBattlePet() == this)
			{
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainPetDataManager_UpdateBattlePetHpChange", pAttack->HPConsume, 0);
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainPetDataManager_UpdateBattlePetMpChange", pAttack->MPConsume, 0);
			}
		}
		return true;
	}
	
	return false;
}
void Battler::RunTo(Nuclear::NuclearLocation ptMoveTarget)
{
	if (m_actionType != eActionBattleRun)
	{
		PlayAction(eActionBattleRun);
	}
	GetSprite()->MoveTo(ptMoveTarget,0,NULL);
	SetTargetPos(ptMoveTarget.x, ptMoveTarget.y);
}
void Battler::SetMoveToParam(Nuclear::NuclearLocation loc,int time)
{
	m_stMoveInfo.desloc = loc;
	m_stMoveInfo.time = time;
}
void Battler::MoveTo()
{
	if (m_stMoveInfo.time != 0)
	{
		double jumpdistance = sqrt((double)GetLogicLocation().Dis2(Nuclear::NuclearWorldLogicCoord::world2logic(m_stMoveInfo.desloc)));
		GetSprite()->SetMoveSpeed((float)jumpdistance*1.1f*m_fDemoSpeedRate/m_stMoveInfo.time);
		GetSprite()->MoveTo(m_stMoveInfo.desloc,0,NULL);

		m_stMoveInfo.time = 0;
		m_stMoveInfo.desloc.x = m_stMoveInfo.desloc.y = 0;
	}
}
void Battler::FlyOut()
{
	PlayAction(eActionRoll);
	int roundnum = GetBattleMagicControl()->GetRoundNum();
	
	m_FlyHitTime = (m_data.iShapeID + roundnum) % 2 + 3;	//撞击4-5次
	int speedrand = (roundnum + m_data.iBattleID+m_data.MaxHp)%3 + 1;
	if (IsEnemySide())
	{
		m_FlySpeedX = -600 + speedrand*-180;
		m_FlySpeedY = -420 + speedrand*-120;
	}
	else
	{
		m_FlySpeedX = 600 + speedrand*180;
		m_FlySpeedY = 420 + speedrand*120;
	}
	m_FlySpeedX *= 1.5;
	m_FlySpeedY *= 1.5;
}
void Battler::RunBack()
{
	Nuclear::NuclearLocation ptCur = GetLocation();
	if (m_ptBattlePosition.Dis2(ptCur) < 10)
	{
		SetLocation(m_ptBattlePosition);
		if (IsFriendSide())
		{
			SetDirection(Nuclear::XPDIR_TOPLEFT);
		}
		else if (IsEnemySide())
		{
			SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
		}
		return;
	}

	RunTo(m_ptBattlePosition);
	if (IsFriendSide())
	{
		SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
	}
	else if (IsEnemySide())
	{
		SetDirection(Nuclear::XPDIR_TOPLEFT);
	}
}
void Battler::ActionRunaway()
{
	Nuclear::NuclearLocation pt = GetDodgePosition();
    if (IsFriendSide())
    {
        SetDirection(Nuclear::XPDIR_TOPLEFT);
    }
    else if (IsEnemySide())
    {
        SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
    }
	PlayAction(eActionRunaway);
	if (GetBattlerType() == eBattlerCharacter)
	{
		Battler* pBattlerPet = GetBattleMagicControl()->FindBattlerByID(GetBattleID() + 5);
		if (pBattlerPet && !pBattlerPet->IsGeneralDie())
		{
			pBattlerPet->ActionRunaway();
		}
	}
}
bool Battler::IsPetCanUse(int pKey)
{
	for (std::list<int>::iterator it = m_data.petkeys.begin(); it != m_data.petkeys.end(); it++)
	{
		if (pKey == (*it))
			return false;
	}
	return true;
}
void Battler::SetPetUsed(int pKey)
{
	for (std::list<int>::iterator it = m_data.petkeys.begin(); it != m_data.petkeys.end(); it++)
	{
		if (pKey == (*it))
			return;
	}
	m_data.petkeys.push_back(pKey);
}
bool Battler::IsOnBattlePosition()
{
	return Nuclear::distance(m_ptBattlePosition, GetLocation()) <= 2;
}
void Battler::AddCatchEffect()
{
	if (m_pCatchEffect)
	{
		RemoveDurativeEffect(m_pCatchEffect);
		m_pCatchEffect = NULL;
	}
	if (NULL == m_pCatchEffect)
	{
		PlayEffect(MHSD_UTILS::GetEffectPath(10187));
	}
}
void Battler::ClearCatchEffect()
{
	if (m_pCatchEffect)
	{
		RemoveDurativeEffect(m_pCatchEffect);
		m_pCatchEffect = NULL;
	}
}
void Battler::ReadyBattleDemo()
{
}
void Battler::SetBattleAttackFinish(bool b)
{
	m_bBattleAttackFinish = b;
	if (IsCharacterModel() && !IsGeneralDie() && !m_bDeathOnGround)
	{
		if (m_bBattleAttackFinish)
		{
			SetDefaultAction(eActionBattleStand);
		}
	}
}
void Battler::SetNormalAlpha(unsigned char a, bool immediately)
{
	m_NormalAlpha = a;
	if (immediately == true)
	{
		m_CurrentAlpha = m_NormalAlpha;
		ComponentsObject::SetAlpha(m_NormalAlpha*m_fAlphaCtrl);
	}
}
void Battler::SetCurrentAlpha(unsigned char a)
{
	m_CurrentAlpha = a;
	ComponentsObject::SetAlpha(m_CurrentAlpha*m_fAlphaCtrl);
}
void Battler::SetAlphaCtrl(float fA)
{
	m_fAlphaCtrl = fA;
	SetAlpha(m_CurrentAlpha*m_fAlphaCtrl);
}
void Battler::Run(int now,int delta)
{
	m_pArtText->Run(delta);
	m_pEffectText->Run(delta);
	RunPick(now, delta);
	bool bIsOnHit = false;
	if ( (m_bHitMoving && m_actionType == eActionOnHit1) || m_actionType == eActionDying || m_actionType == eActionRoll || m_actionType == eActionDefence || m_actionType == eActionDodge)
    {
        m_iOnhitTick -= delta;

		int iOnhitTick = 90;
		int iOnhitTickHalf = 45;
		int iOnhitDelay = 80;
		int iOnhitDist = 80;
		int iOnhitDistHalf = 40;

		if (m_actionType == eActionDefence)
		{
			iOnhitTick = 65;
			iOnhitTickHalf = 33;
			iOnhitDelay = 80;
			iOnhitDist = 20;
			iOnhitDistHalf = 10;
		}

		int OnhitDistance = 6;
		if (m_iOnhitTick > iOnhitTickHalf && m_iOnhitTick <= iOnhitTick)
		{
			OnhitDistance = iOnhitDist * (1 - (m_iOnhitTick - iOnhitTickHalf) * 1.0f / iOnhitTickHalf);
		}
		else if (m_iOnhitTick >= 0 && m_iOnhitTick <= iOnhitTickHalf)
		{
			OnhitDistance = iOnhitDist - iOnhitDistHalf * (1 - m_iOnhitTick * 1.0f / iOnhitTickHalf);
		}

		Nuclear::NuclearLocation loc = Nuclear::NuclearLocation(m_iTargetPosX, m_iTargetPosY);
		
		Nuclear::NuclearDirection OnhitDir;

		int OffsetX = 1;
		int OffsetY = 1;

		if (IsFriendSide())
		{
			if (m_iOnhitDir == 2)
			{
				OffsetX = 1;
				OffsetY = 1;
				OnhitDir = Nuclear::XPDIR_TOPLEFT;
			}
			else if (m_iOnhitDir == 4)
			{
				OffsetX = -1;
				OffsetY = 1;
				OnhitDir = Nuclear::XPDIR_TOPRIGHT;
			}
			else if (m_iOnhitDir == 6)
			{
				OffsetX = 1;
				OffsetY = -1;
				OnhitDir = Nuclear::XPDIR_BOTTOMLEFT;
			}
			else if (m_iOnhitDir == 8)
			{
				OffsetX = -1;
				OffsetY = -1;
				OnhitDir = Nuclear::XPDIR_BOTTOMRIGHT;
			}
		}
		else
		{
			if (m_iOnhitDir == 2)
			{
				OffsetX = -1;
				OffsetY = -1;
				OnhitDir = Nuclear::XPDIR_BOTTOMRIGHT;
			}
			else if (m_iOnhitDir == 4)
			{
				OffsetX = 1;
				OffsetY = -1;
				OnhitDir = Nuclear::XPDIR_BOTTOMLEFT;
			}
			else if (m_iOnhitDir == 6)
			{
				OffsetX = -1;
				OffsetY = 1;
				OnhitDir = Nuclear::XPDIR_TOPRIGHT;
			}
			else if (m_iOnhitDir == 8)
			{
				OffsetX = 1;
				OffsetY = 1;
				OnhitDir = Nuclear::XPDIR_TOPLEFT;
			}
		}
		if (m_iOnhitTick > 0)
		{
			loc.x += OnhitDistance * OffsetX;
			loc.y += OnhitDistance * OffsetY;
			SetLocation(loc);
			SetDirection(OnhitDir);
			bIsOnHit = true;
		}
		else
		{
			if (m_iOnhitDelay > 0)
			{
				m_iOnhitDelay -= delta;
				if (m_iOnhitDelay <= 0)
				{
					if (m_actionType == eActionRoll)
					{
						m_iOnhitDelay = -iOnhitDelay - 1;
					}
					else
					{
						if (m_iOnhitDelay == 0)
						{
							m_iOnhitDelay = -1;
						}
					}
				}
				bIsOnHit = true;
			}
			else if (m_iOnhitDelay == 0)
			{
				if (m_actionType == eActionRoll)
				{
					m_iOnhitDelay = -iOnhitDelay - 1;
				}
				else
				{
					m_iOnhitDelay = -1;
				}
				bIsOnHit = true;
			}
			else if (m_iOnhitDelay >= -iOnhitDelay && m_iOnhitDelay < 0)
			{
				m_iOnhitDelay -= delta;
				loc = Nuclear::NuclearLocation(m_iTargetPosX, m_iTargetPosY);
				OnhitDistance = iOnhitDistHalf + iOnhitDistHalf * m_iOnhitDelay * 1.0f / iOnhitDelay;
				loc.x += OnhitDistance * OffsetX;
				loc.y += OnhitDistance * OffsetY;
				SetLocation(loc);
				SetDirection(OnhitDir);
				bIsOnHit = true;
			}
			else if (m_iOnhitDelay >= -100000 && m_iOnhitDelay < -iOnhitDelay)
			{
				m_iOnhitTick = 0;
				m_iOnhitFrame = 0;
				m_iOnhitDelay = -100001;
				m_iOnhitDir = 2;
				if (!m_bDeath && !m_bGhost && !m_bDieVanish && !m_bFlyOut)
				{
					loc = Nuclear::NuclearLocation(m_iTargetPosX, m_iTargetPosY);
					SetLocation(loc);
						SetDefaultAction(eActionBattleStand);
						NewPlayAction(eActionBattleStand, 1.0f, true, eActionNull, true, true);
					if (m_HPChangeGodBless > 0)
					{
						OnHitNumber(m_HPChangeGodBless, eBattleResult_HPChange, 3);
						PlayEffect(MHSD_UTILS::GetEffectPath(11012));//这里需要一个正确的特效ID
						m_HPChangeGodBless = 0;
					}
				}
				else if (m_bDeath || m_bGhost || m_bDieVanish)
				{
					loc = Nuclear::NuclearLocation(m_iTargetPosX, m_iTargetPosY);
					SetLocation(loc);
					m_actionType = eActionDeath;
					SetDefaultAction(eActionDeath, true);
					if (IsFriendSide())
						SetDirection(Nuclear::XPDIR_TOPLEFT);
					else if (IsEnemySide())
						SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
				}
				else if (m_actionType == eActionRoll)
				{

				}
				bIsOnHit = true;
			}
			else
			{
				if (m_actionType == eActionOnHit1)
				{
					bIsOnHit = false;
				}
			}
		}
		if (m_actionType == eActionOnHit1 && bIsOnHit == false)
		{
			m_bHitMoving = false;
			SetDefaultAction(eActionBattleStand);
			NewPlayAction(eActionBattleStand, 1.0f, true, eActionNull, true, true);
		}
        UpdateHpBarLocation();
    }
	if (m_iMoveOffsetX != 0 || m_iMoveOffsetY != 0)
	{
		Nuclear::NuclearLocation loc = GetSprite()->GetLocation();
		loc.x += m_iMoveOffsetX * delta * 0.001;// 1000.0f;
		loc.y += m_iMoveOffsetY * delta * 0.001;// / 1000.0f;
		GetSprite()->SetLocation(loc);
		UpdateHpBarLocation();
	}

	delta = static_cast<int>(m_fDemoSpeedRate*delta);
	if (m_iHitBackDelay >= 0)
	{
		m_iHitBackDelay -= delta;
		if (m_iHitBackDelay < 0)
		{
			ContinueHitBack();
		}
	}
	if (m_bFirstTickVisible)
	{
		m_iSetVisibleDelay -= delta;
		if (m_iSetVisibleDelay < 0)
		{
			m_bFirstTickVisible = false;
			m_iSetVisibleDelay =  0;
			SetVisible(true);
		}
	}
	SceneObject::Run(now,delta);
	if (GetBattleManager() && GetBattleManager()->GetMouseOverObjects() == this)
	{
        SetHighlight(true);
	}
	else
		SetHighlight(false);

	if (m_actionType == eActionRoll || (m_FlySpeedX != 0 && m_FlySpeedY != 0))
	{
		RunRollAction(delta);
	}
	if (m_bDieVanish && m_bBeginVanish &&m_actionType == eActionDeath)
	{
		float alpha = m_CurrentAlpha;
		if (m_bVanishAlphaChange)
		{
			alpha -= 0.25f * delta;
			if (alpha <= 0)
			{
				alpha = 0;
				m_bVanishAlphaChange = false;
				m_iFlashTime--;
				if (m_iFlashTime <= 0)
					m_bDisappear = true;
			}
			m_CurrentAlpha = alpha;
		}
		else
		{
			alpha += 0.25f * delta;
			if (alpha >= 255)
			{
				alpha = 255;
				m_bVanishAlphaChange = true;
				m_iFlashTime--;
				if (m_iFlashTime <= 0)
					m_bDisappear = true;
			}
			m_CurrentAlpha = alpha;
		}
		SetAlpha(m_CurrentAlpha*m_fAlphaCtrl);
	}
	else
	{
		if (m_CurrentAlpha != m_NormalAlpha)
		{
			if (m_NormalAlpha > m_CurrentAlpha)
			{
				if (m_NormalAlpha - m_CurrentAlpha > 0.25f * delta)
					m_CurrentAlpha = m_CurrentAlpha +  0.25f * delta;
				else
					m_CurrentAlpha = m_NormalAlpha;	
			}
			else
			{
				if (m_CurrentAlpha > 0.25f * delta +  m_NormalAlpha)
					m_CurrentAlpha = m_CurrentAlpha - 0.25f * delta;
				else
					m_CurrentAlpha = m_NormalAlpha;
			}
			SetAlpha(m_CurrentAlpha*m_fAlphaCtrl);
		}
	}
	if (m_bShowName == false)
	{
		if (m_NormalAlpha > 0 && m_CurrentAlpha == m_NormalAlpha)
			m_bShowName = true;
	}
		
	RefreshHpBar();
	if (IsMoving() || m_actionType == eActionDodge || m_actionType == eActionRoll)
	{
		UpdateHpBarLocation();
	}
}
void Battler::Draw(int now)
{
	if(IsVisiable() == false || m_bShowName == false)//如果隐藏，不画名字
		return;

	Nuclear::NuclearLocation loc = GetLocation();

	Nuclear::TextBlockHandle handle = GetNameTextureHandle();
	Nuclear::ITextBlock* pBlock = GetNameBlock();
	Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
	if (pFontMan && handle != Nuclear::INVALID_TEXTBLOCK_HANDLE && pBlock)
	{
		DWORD colour = GetNameColour();
		pBlock->SetFontColor(colour,colour);
		pBlock->SetShadowColor(0xFF000000);

		int height=pBlock->GetTextHeight();
		int width=pBlock->GetTextWidth();

		if (m_data.bGM)
		{
			if (m_GMpic == Nuclear::INVALID_PICTURE_HANDLE)
			{
				//m_GMpic = Nuclear::GetEngine()->GetRenderer()->LoadPicture(L"/image/title/GM.tga");
			}

			if (m_GMpic == Nuclear::INVALID_PICTURE_HANDLE)
			{
				int left = loc.x - Nuclear::GetEngine()->GetWorld()->GetViewport().left - width / 2;
				int top = loc.y - Nuclear::GetEngine()->GetWorld()->GetViewport().top + 15 * m_titleIndex++ + 6;
				pFontMan->DrawText(handle, (float)left, (float)top, NULL);
			}
			else
			{
				Nuclear::NuclearPictureInfo picinfo;
				bool bgetinfo = Nuclear::GetEngine()->GetRenderer()->GetPictureInfo(m_GMpic, picinfo);

				Nuclear::NuclearFRectt rct;
				rct.left = (float)(loc.x - gGetScene()->GetViewport().left - (picinfo.m_nPicWidth + width) / 2);
				rct.top = (float)(loc.y - Nuclear::GetEngine()->GetWorld()->GetViewport().top + DRAW_NAME_OFFSET + 15 * m_titleIndex++ + 6);
				rct.bottom = (float)(rct.top + picinfo.m_nPicHeight);
				rct.right = (float)(rct.left + picinfo.m_nPicWidth);
				Nuclear::GetEngine()->GetRenderer()->DrawPicture(m_GMpic, rct, 0xFFFFFFFF);

				pFontMan->DrawText(m_NameTextureHandle, (float)rct.right, (float)rct.top, NULL);
			}
		}
		else 
		{
			int left = loc.x - Nuclear::GetEngine()->GetWorld()->GetViewport().left - width / 2;
			int top = loc.y - Nuclear::GetEngine()->GetWorld()->GetViewport().top + 15 * m_titleIndex++ + 6;

			pFontMan->DrawText(handle, (float)left, (float)top, NULL);

			if (m_pJobPic == Nuclear::INVALID_PICTURE_HANDLE && m_data.school > 0)
			{				
				m_pJobPic = GetBattleManager()->GetJobPicHandle(m_data.school);
			}
			if (m_pJobPic != Nuclear::INVALID_PICTURE_HANDLE)
			{
				Nuclear::NuclearFRectt rct;
				rct.left = (float)(loc.x - Nuclear::GetEngine()->GetWorld()->GetViewport().left - width / 2 + width);
				rct.top = (float)(loc.y - Nuclear::GetEngine()->GetWorld()->GetViewport().top + 15 * (m_titleIndex - 1) + 6);
				rct.bottom = rct.top + 26.0f;
				rct.right = rct.left + 26.0f;
				Nuclear::GetEngine()->GetRenderer()->DrawPicture(m_pJobPic, rct, 0xFFFFFFFF);
			}

			if (m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE&&m_pTitleTextBlock)
			{
				int titlewidth = m_pTitleTextBlock->GetTextWidth();
				int titleleft = loc.x - Nuclear::GetEngine()->GetWorld()->GetViewport().left - titlewidth / 2;
				int titletop = loc.y - gGetScene()->GetViewport().top + DRAW_NAME_OFFSET + 15 * m_titleIndex++ + 6;
				pFontMan->DrawText(m_TitleTextureHandle, (float)titleleft, (float)titletop, NULL);
			}
			if (m_FlagTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE&&m_pFlagTextBlock)
			{
				if (m_pFlagPic == Nuclear::INVALID_PICTURE_HANDLE)
				{
					m_pFlagPic = Nuclear::GetEngine()->GetRenderer()->LoadPicture(L"/ui/zhandou/di.png");
				}
				int titlewidth = m_pFlagTextBlock->GetTextWidth();
				int titleleft = loc.x - Nuclear::GetEngine()->GetWorld()->GetViewport().left - titlewidth / 2;
				int yoffset = GetSprite()->GetBubbleItemOffset() == 0 ? DEFAULT_HPBAR_HEIGHT : GetSprite()->GetBubbleItemOffset();
				int titletop = loc.y - gGetScene()->GetViewport().top - yoffset + 40;

				if (m_pFlagPic != Nuclear::INVALID_PICTURE_HANDLE)
				{
					Nuclear::NuclearFRectt rct;
					rct.left = titleleft - 4;
					rct.right = rct.left + titlewidth + 8;
					rct.top = (float)titletop-4;
					rct.bottom = rct.top + 32.0f;
					Nuclear::GetEngine()->GetRenderer()->DrawPicture(m_pFlagPic, rct, 0xFFFFFFFF);
				}

				pFontMan->DrawText(m_FlagTextureHandle, (float)titleleft, (float)titletop, NULL);
			}
		}
	}
	m_titleIndex = 0;
}
bool Battler::IsGeneralDie()
{
	return m_bDeath || m_bFlyOut || m_bGhost || m_bDieVanish;
}
bool Battler::IsFlyOut()
{
	return m_bFlyOut;
}
bool Battler::IsDieVanish()
{
	return m_bDieVanish;
}
bool Battler::CanUseTo(int usetype)
{
	switch(m_data.BattlerType)
	{
	case eBattlerCharacter:
		{
			if (IsFriendSide())
			{
				if (GetBattleManager() && GetBattleManager()->GetMainBattleCharID() == m_data.iBattleID)
					return (usetype & BattleUseMainCharacter) != 0x00;
				else														
					return (usetype & BattleUseFriendCharacter) != 0x00;
			}
			else if (IsEnemySide())												
				return (usetype & BattleUseEnemyCharacter) != 0x00;
			return false;
		}
		break;
	case eBattlerPet:	
		{
			if (IsFriendSide())
			{
				if (GetBattleManager() && GetBattleManager()->GetMainBattlePetID() == m_data.iBattleID)	
					return (usetype & BattleUseMainPet) != 0x00;
				else															
					return (usetype & BattleUseFriendPet) != 0x00;
			}
			else if (IsEnemySide())												
				return (usetype & BattleUseEnemyPet) != 0x00;
			return false;
		}
		break;
	case eBattlerNaKaMa:
	case eBattlerMonster:
	case eBattlerNPC:
	case eBattlerNaKaMaSys:
		{
			if (IsEnemySide())														
			{
				return (usetype & BattleUseEnemyCreep) != 0x00;
			}
			else if (IsFriendSide())
			{
				return (usetype & BattleUseFriendNPC) != 0x00;						
			}
			return false;
		}
		break;
	}
	return false;
}
bool Battler::AffectByAttack()
{
	if (GetBattleManager())
	{
		return GetBattleManager()->IsEnemy(this->m_data.iBattleID);
	}
	else
		return false;
}
bool Battler::CanBeCommand()
{
	return m_data.iBattleID <= EnemyMaxID;
}
bool Battler::AffectedBySkill(int skillID)
{
	int skilluse = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("RoleSkillManager.GetSkillUseType", skillID);
	if (skilluse == 0)
	{
		return false;
	}
	else
	{
		bool bR = CanUseTo(skilluse);
		return bR;
	}
		
}
bool Battler::AffectedByItem(int itemuse)
{	
	if (itemuse == 0)
	{
		return false;
	}
	else
	{
		return CanUseTo(itemuse);
	}
}
bool Battler::AffectedByCatch()
{
	if (GetBattleManager())
	{
		bool bResult = GetBattleManager()->IsEnemy(m_data.iBattleID) && 
			(m_data.BattlerType == eBattlerNaKaMa || m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC || m_data.BattlerType == eBattlerNaKaMaSys) &&
			m_bCanBeCatch;
		return bResult;
	}
	else
	{
		return false;
	}
}
bool Battler::AffectByProtect()
{
	if (GetBattleManager())
	{
		return GetBattleManager()->IsFriend(m_data.iBattleID);
	}
	else
		return false;
}
bool Battler::OnSetCursor()
{
	if (NULL == GetBattleManager())
		return true;
	if (!GetBattleManager()->IsInBattleOperate())
		return false;
	if (gGetGameOperateState()->GetGameCursorType() == eGameCursorType_BattleNormal
	 && AffectByAttack())
	{
		gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_BattleAttack);
		return true;
	}
	return false;
}
void Battler::ResetState()
{
	m_actionType = eActionStand;
	m_FlySpeedX = 0;
	m_FlySpeedY = 0;
	m_bDisappear = false;
	SetFlag(L"");
}
bool Battler::SetAttribute(sBattlerData* pData)
{
	ResetState();
	m_data.iBattleID = pData->iBattleID;
	m_data.strName = pData->strName;
	m_data.subtype = pData->subtype;
	m_data.school = pData->subtype;
	if (pData->BattlerType == eBattlerNaKaMa || pData->BattlerType == eBattlerNaKaMaSys)
	{
		int HeroBaseInfo_school = GetTableNumberFromLua("npc.cherobaseinfo", static_cast<int>(pData->BattlerDataID), "school");
		m_data.school = HeroBaseInfo_school;
	}
	if (pData->BattlerType == eBattlerMonster || pData->BattlerType == eBattlerNPC)
	{
		const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(static_cast<int>(pData->BattlerDataID));
		m_data.school = MonsterData.school;
		if (MonsterData.showhpbarbig > 0)
		{
			m_bShowHPBar = false;
			m_bShowHPBarBig = true;
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("bossdaxuetiao.CSetVisible", true);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("bossdaxuetiao.CSetMonsterID",pData->iBattleID, pData->BattlerDataID);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("bossdaxuetiao.CSetHPMaxHP", pData->iBattleID, pData->Hp, pData->MaxHp);
		}
	}
    m_data.colorInx= pData->colorInx;
	m_data.strTitle = pData->strTitle;
	m_data.iTitleID = pData->iTitleID;
	m_data.BattlerType = pData->BattlerType;
	m_data.bGM = pData->bGM;
	m_data.BattlerDataID = pData->BattlerDataID;
	m_data.MaxHp = pData->MaxHp;
	m_data.Hp = pData->Hp;
	m_data.iShapeID = pData->iShapeID;
	m_data.Ep = pData->Ep;
	m_data.eQuipEffect = pData->eQuipEffect;
	m_data.petkeys = pData->petkeys;
    
	if(pData->iShapeID <=10 && pData->iShapeID > 0)
		m_data.iShapeID = 1010100+pData->iShapeID;
	else if(pData->iShapeID <300 && pData->iShapeID > 0)
		m_data.iShapeID = 2010100+pData->iShapeID%100;

	m_data.componentsMap = pData->componentsMap;
	m_bDeath = m_bGhost = m_bFlyOut = m_bDieVanish = m_bDeathOnGround = false;
	RemoveAllBattleBuff();
	m_data.BattleBuff.clear();
	for (BattlerBuffMap::iterator it = pData->BattleBuff.begin();it!=pData->BattleBuff.end();it++)
	{
		m_data.BattleBuff[it->first] = it->second;
	}
	switch(m_data.BattlerType)
	{
	case eBattlerCharacter:
		{
			m_data.UpLimitHp = pData->UpLimitHp;
			SetNameColour(0xff00ff42);
			if (!SetCharacter(m_data.BattlerDataID, m_data.iShapeID, m_data.componentsMap))
			{
				return false;
			}
		}
		break;
	case eBattlerPet:
		{	
			m_data.UpLimitHp = pData->MaxHp;
            union uclr{
                struct sclr {
                    unsigned char r,g,b,a;
                } s;
                DWORD c;
                
                uclr(DWORD aC)
                {
                    c = aC;
                    unsigned char r = s.r;
                    s.r = s.b;
                    s.b = r;
                }
                operator DWORD()
                {
                    return c;
                }
            };
            
			SetNameColour(uclr(GetPetNameColor(m_data.BattlerDataID)));
			if (!SetPet(m_data.BattlerDataID,m_data.iShapeID))
				return false;
		}
		break;
	case eBattlerNaKaMa:
	case eBattlerMonster:
	case eBattlerNPC:
	case eBattlerNaKaMaSys:
		{
			m_data.UpLimitHp = pData->MaxHp;
			m_data.level = pData->UpLimitHp;
            if (m_data.colorInx == 0) {
                SetNameColour(0xffffb100);
            }else
            {
                union uclr{
                    struct sclr {
                        unsigned char r,g,b,a;
                    } s;
                    DWORD c;
                    
                    uclr(DWORD aC)
                    {
                        c = aC;
                        unsigned char r = s.r;
                        s.r = s.b;
                        s.b = r;
                    }
                    operator DWORD()
                    {
                        return c;
                    }
                };
                
                SetNameColour(uclr(GetPetColour(ePetColour(m_data.colorInx))));
            }
			if (IsFriendSide())
			{
				SetNameColour(0xffff00f0);
			}
			if (!SetCreeps(m_data.BattlerDataID,m_data.iShapeID))
				return false;
		}
		break;
	default:
		return false;
		break;
	}

	UpdateSpriteComponent(m_data.componentsMap);

	m_NormalAlpha = 255;
	if (!pData->BattleBuff.empty())
	{
		for (BattlerBuffMap::iterator it = pData->BattleBuff.begin();it!=pData->BattleBuff.end();it++)
		{
			AddBattleBuff(it->first,it->second);
		}

		if (GetBattleManager())
		{
			GetBattleManager()->EventBattlerBuffChange(m_data.iBattleID);
		}
	}
	
	if (m_data.strName != L"")
		m_strName = m_data.strName;
	UpdatNameTexture(true);
	UpdataTitleTexture();
    
	if (GetSprite())
	{
		m_fInitScale = GetScale();

		GetSprite()->SetMoveSpeed(c_fBattlerMoveSpeed * m_fDemoSpeedRate / 1000);

        GetSprite()->SetMouseTestMode(Nuclear::XPTM_POLYGON);
		GetSprite()->SetMoveMask(eMoveMaskNull);
		if (IsWatcher())
			SetDefaultAction(eActionStand);
		else
			SetDefaultAction(eActionBattleStand);

		BattlerBuffMap::iterator it = m_data.BattleBuff.find((int)eBuffBattleDeath);
		BattlerBuffMap::iterator ie = m_data.BattleBuff.find((int)eBuffBattleGhost);
		if (it != m_data.BattleBuff.end() || ie != m_data.BattleBuff.end())
		{
			if (it != m_data.BattleBuff.end())
			{
				m_bDeath = true;
			}
			if (ie != m_data.BattleBuff.end())
			{
				m_bGhost = true;
			}
			SetDefaultAction(eActionDeath,true);
		}
		SetBattleLocation();
		SetBattleDirection();
	}
	if ( 
		((m_data.BattlerType == eBattlerNaKaMa || m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC || m_data.BattlerType == eBattlerNaKaMaSys) &&
		!m_bCreepsShowHpBar))
	{
		m_bShowHPBar = false;
	}
	if (GetBattleManager() && GetBattleManager()->IsInPVPBattle() && IsEnemySide())
	{
		m_bShowHPBar = false;
	}
	if (!IsWatcher())
	{
		AddHpBar();
	}
	if (IsWatcher())
	{
		Nuclear::NuclearLocation loc = GetBattleManager()->GetWatcherLocation(m_data.iBattleID);
		SetTargetPos(loc.x, loc.y);
	}
	else
	{
		Nuclear::NuclearLocation loc = GetBattleManager()->GetBattleLocation(m_data.iBattleID);
		SetTargetPos(loc.x, loc.y);
	}

	GetSprite()->SetCurveMove(true);

	return true;
}
bool Battler::IsCanUseComponentsAfterChangeModel() 
{ 
	switch (m_data.BattlerType)
	{
	case eBattlerCharacter:return false;
	case eBattlerPet:return false;
	case eBattlerNaKaMa:return true;	//伙伴（玩家自带助战）
	case eBattlerMonster:return true;	//暗雷野怪
	case eBattlerNPC:return true;	//战斗npc（24000以上）
	case eBattlerNaKaMaSys:return true;	//系统安排的助战
	default:
		break;
	}
	return ComponentsObject::IsCanUseComponentsAfterChangeModel(); 
}
void Battler::ResetMoveOffset()
{
	m_iMoveOffsetX = 0;
	m_iMoveOffsetY = 0;
}
void Battler::ResetTargetPos()
{
	if (IsWatcher())
	{
		Nuclear::NuclearLocation loc = GetBattleManager()->GetWatcherLocation(m_data.iBattleID);
		SetTargetPos(loc.x, loc.y);
	}
	else
	{
		Nuclear::NuclearLocation loc = GetBattleManager()->GetBattleLocation(m_data.iBattleID);
		SetTargetPos(loc.x, loc.y);
	}
}
bool Battler::SetAttribute(sBattlerData* pData, std::wstring modelName,
							std::wstring hair, std::wstring head, std::wstring body)
{
	ResetState();
	m_data.iBattleID = pData->iBattleID;
	m_data.strName = pData->strName;
	m_data.subtype = pData->subtype;
	m_data.strTitle = pData->strTitle;
	m_data.iTitleID = pData->iTitleID;
	m_data.BattlerType = pData->BattlerType;
	m_data.bGM = pData->bGM;
	m_data.BattlerDataID = pData->BattlerDataID;
	m_data.MaxHp = pData->MaxHp;
	m_data.Hp = pData->Hp;
	m_data.iShapeID = pData->iShapeID;
	m_data.componentsMap = pData->componentsMap;

	RemoveAllBattleBuff();
	for (BattlerBuffMap::iterator it = pData->BattleBuff.begin();it!=pData->BattleBuff.end();it++)
	{
		m_data.BattleBuff[it->first] = it->second;
	}
	switch(m_data.BattlerType)
	{
	case eBattlerCharacter:
		{
			m_data.UpLimitHp = pData->UpLimitHp;
			SetNameColour(0xff00ff42);
			if (!SetCharacter(m_data.BattlerDataID,m_data.iShapeID,m_data.componentsMap, modelName,
				hair, head, body))
				return false;
		}
		break;
	case eBattlerPet: 
		{	
			m_data.UpLimitHp = pData->MaxHp;
            union uclr{
                struct sclr {
                    unsigned char a,r,g,b;
                } s;
                DWORD c;
            };
            
            uclr clr;
			clr.c = GetPetNameColor(m_data.BattlerDataID);
            
            unsigned char r = clr.s.b;
            clr.s.b = clr.s.r;
            clr.s.r = r;
            
			SetNameColour(clr.c);	
			if (!SetPet(m_data.BattlerDataID,m_data.iShapeID, modelName,
				hair, head, body))
				return false;
		}
		break;
	case eBattlerMonster:
		{
			m_data.UpLimitHp = pData->MaxHp;
			m_data.level = pData->UpLimitHp;
            if (m_data.colorInx == 0) {
                SetNameColour(0xffffb100);
            }else
            {
                union uclr{
                    struct sclr {
                        unsigned char a,r,g,b;
                    } s;
                    DWORD c;
                };
                
                uclr clr;
                clr.c = GetPetColour(ePetColour(m_data.subtype));
                
                unsigned char r = clr.s.b;
                clr.s.b = clr.s.r;
                clr.s.r = r;
                
                SetNameColour(clr.c);
            }
			if (!SetCreeps(m_data.BattlerDataID,m_data.iShapeID))
				return false;
		}
		break;
	default:
		return false;
		break;
	}
	UpdateSpriteComponent(m_data.componentsMap);

	m_NormalAlpha = 255;
	if (!pData->BattleBuff.empty())
	{
		for (BattlerBuffMap::iterator it = pData->BattleBuff.begin();it!=pData->BattleBuff.end();it++)
		{
			AddBattleBuff(it->first,it->second);
		}

		if (GetBattleManager())
		{
			GetBattleManager()->EventBattlerBuffChange(m_data.iBattleID);
		}
	}
	
	if (m_data.strName != L"")
		m_strName = m_data.strName;
	UpdatNameTexture(true);
	UpdataTitleTexture();

	if (GetSprite())
	{
		if (gGetDisPlayMode() == eDisplayMode_1024768)
		{
			GetSprite()->SetMoveSpeed(c_fBattlerMoveSpeed*1.23f*m_fDemoSpeedRate/1000);
		}
		else
		{
			GetSprite()->SetMoveSpeed(c_fBattlerMoveSpeed*m_fDemoSpeedRate/1000);
		}
		
		GetSprite()->SetMoveMask(eMoveMaskNull);
		if (IsWatcher())
			SetDefaultAction(eActionStand);
		else
			SetDefaultAction(eActionBattleStand);

		BattlerBuffMap::iterator it=m_data.BattleBuff.find((int)eBuffBattleDeath);
		BattlerBuffMap::iterator ie=m_data.BattleBuff.find((int)eBuffBattleGhost);
		if (it != m_data.BattleBuff.end() || ie != m_data.BattleBuff.end())
		{
			SetDefaultAction(eActionDeath,true);
		}
		SetBattleLocation();
		SetBattleDirection();
		if (GetBattleManager() && GetBattleManager()->IsInPVPBattle() && IsEnemySide())
		{
			m_bShowHPBar = false;
		}
		if (
			((m_data.BattlerType == eBattlerNaKaMa || m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC || m_data.BattlerType == eBattlerNaKaMaSys) &&
			!m_bCreepsShowHpBar))
		{
			m_bShowHPBar = false;
		}
		AddHpBar();
	}
	return true;
}
bool Battler::AddBabyTips()
{
	//当类型是怪物时，subtype为3代表宝宝
	//当出现宝宝时，给出一个特效并且给出提示
	if (m_data.BattlerType == eBattlerMonster && m_data.subtype == 3)
	{
		PlayEffect(MHSD_UTILS::GetEffectPath(10208));
		return true;
	}
	else
		return false;
}
void Battler::ShowHpBar(bool bshow)
{
	if (m_pHpBar)
	{
		m_pHpBar->setVisible(bshow);
		for (size_t i = 0; i < m_pEpImages.size(); i++)
			m_pEpImages[i]->setVisible(bshow);
	}

	if(m_pLevelText)
	{
		m_pLevelText->setVisible(bshow);
	}
}
bool Battler::HandlePickBtnClicked(const CEGUI::EventArgs& e)
{
	m_DownPickBtn = NULL;
	const CEGUI::MouseEventArgs& MouseArgs = static_cast<const CEGUI::MouseEventArgs&>(e);
	if (m_TipShow == false)
	{
		if (!GetBattleManager()->IsInBattleOperate() || GetBattleManager()->IsAutoOperate())
		{
			return true;
		}
		return GetBattleManager()->PickBattler(this);
	}
	return true;

}
bool Battler::HandlePickBtnUp(const CEGUI::EventArgs& e)
{
	m_DownPickBtn = NULL;
	return true;
}
bool Battler::HandlePickBtnDown(const CEGUI::EventArgs& e)
{
	ShowBattlerTip(false);
	const CEGUI::MouseEventArgs& MouseArgs = static_cast<const CEGUI::MouseEventArgs&>(e);
	m_DownPickBtn = m_pPickBtn;
	return true;
}
void Battler::RunPick(int now, int delta)
{
	CEGUI::Window* pTargetWnd = GameUImanager::GetInstance()->GetCursorWindow();
	if (pTargetWnd != NULL)
	{
		if (m_TipShow == false)
		{
			if (m_DownPickBtn != NULL)
			{
				if (m_DownPickBtn == pTargetWnd || pTargetWnd->isAncestor(m_DownPickBtn))
				{
					m_TipTime = m_TipTime + delta;
					if (m_TipTime >= 500)
					{
						ShowBattlerTip(true);
						m_TipShow = true;
					}
				}
				else
				{
					if (pTargetWnd == NULL)
					{
						m_DownPickBtn = NULL;
					}
				}
			}
		}
	}
	else
	{
		m_DownPickBtn = NULL;
	}
}
void Battler::ShowBattlerTip(bool bShow)
{
	if (bShow)
	{
		int PlayerOrPetOrNPC;
		if (GetBattleID() == GetBattleManager()->GetMainBattleCharID())
		{
			PlayerOrPetOrNPC = 0;
		}
		else if (GetBattleID() == GetBattleManager()->GetMainBattlePetID())
		{
			PlayerOrPetOrNPC = 1;
		}
		else
		{
			PlayerOrPetOrNPC = 2;
		}
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("BattleTips.CSetBattleID", GetBattleID(), PlayerOrPetOrNPC);
	} 
	else
	{
		m_DownPickBtn = NULL;
		m_TipTime = 0;
		m_TipShow = false;
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("BattleTips.CSetBattleID", 0, 0);
	}
}
void Battler::ShowPickBtn(bool bShow)
{
	if (m_pPickBtn == NULL)
	{
		return;
	}
	if (bShow)
	{
		const char* pImage = "set:fightui image:pitch_on";
		m_pPickBtn->setProperty("Image", pImage);
	}
	else
	{
		m_pPickBtn->setProperty("Image", "");
	}
}
bool Battler::CanPick(int eCurOperatorType, int iCurSkillID, stRoleItem& pCurItem)
{	
	switch ((eGameCursorType)eCurOperatorType)
	{
	case eGameCursorType_BattleNormal:	
	case eGameCursorType_BattleAttack:	
	{
		return AffectByAttack();
	}
	break;
	case eGameCursorType_BattleSkill:
	{
		return AffectedBySkill(iCurSkillID);
	}
	break;
	case eGameCursorType_BattleItem:	
	{
		return AffectedByItem(pCurItem.m_Battleuse);
	}
	break;
	case eGameCursorType_BattleProtect:
	{
		return AffectByProtect();
	}
	break;
	case eGameCursorType_BattleCatch:
	{
		return AffectedByCatch();
	}
	break;
	default:
	{
		return false;
	}
	break;
	}
}
bool Battler::HandleSkillNameStopped(const CEGUI::EventArgs& e)
{
	ShowSkillName(false);
	return true;
}
void Battler::ShowSkillName(bool bShow, int iSkillID)
{
	if (m_pSkillNameRoot)
	{
		if (bShow)
		{
			if (m_pSkillNameAni)
			{
				m_pSkillNameAni->start();
			}
			if (m_pSkillNameAni2)
			{
				m_pSkillNameAni2->start();
			}
			const GameTable::skill::CSkillTypeConfig& skilltypecfg = GameTable::skill::GetCSkillTypeConfigTableInstance().getRecorder(iSkillID);
			if (skilltypecfg.id > 0)
			{
				m_pSkillNameText->setText(skilltypecfg.skillname);
			}
			else
			{
				m_pSkillNameText->setText(L"");
			}
		}
		m_pSkillNameRoot->setVisible(bShow);
	}
}
void Battler::AddHpBar()
{
	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
	if (m_bShowHPBar)
	{
		if (m_pHpBar == NULL)
		{
			CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
			if ((m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC))
			{
				if (m_data.subtype == 2)
				{
					m_pHpBar = static_cast <CEGUI::ProgressBarTwoValue*>(winMgr.createWindow("TaharezLook/BattleProgressBar3", OnlyName));
				}
				else if (m_data.subtype == 4)
				{
					m_pHpBar = static_cast <CEGUI::ProgressBarTwoValue*>(winMgr.createWindow("TaharezLook/BattleProgressBar2", OnlyName));
				}
				else
				{
					m_pHpBar = static_cast <CEGUI::ProgressBarTwoValue*>(winMgr.createWindow("TaharezLook/BattleProgressBar", OnlyName));
				}
			}
			else
			{
				m_pHpBar = static_cast <CEGUI::ProgressBarTwoValue*>(winMgr.createWindow("TaharezLook/BattleProgressBar", OnlyName));
			}
		}
		if (m_pHpBar)
		{
			CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
			if (pRootWindow != NULL)
			{
				Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
				int yoffset = DEFAULT_HPBAR_HEIGHT;
				if (GetSprite())
				{
					yoffset = GetSprite()->GetBubbleItemOffset() == 0 ? DEFAULT_HPBAR_HEIGHT : GetSprite()->GetBubbleItemOffset();
				}
				float ModelScale = 1.0f;
				if ((m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC || m_data.BattlerType == eBattlerNaKaMa))
				{
					const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(static_cast<int>(m_data.BattlerDataID));
					if (MonsterData.id != -1)
					{
						ModelScale = MonsterData.bodytype;
					}
				}
				Nuclear::NuclearLocation ptHpBar = GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top) + Nuclear::NuclearLocation(-30, (-yoffset - 36) * ModelScale);
				m_pHpBar->setAlwaysOnBottom(true);
				pRootWindow->addChildWindow(m_pHpBar);
				m_pHpBar->SetOnShiedUIState(false);
				m_pHpBar->setPosition(CEGUI::UVector2(cegui_absdim((float)ptHpBar.x), cegui_absdim((float)ptHpBar.y)));
				m_pHpBar->setSize(CEGUI::UVector2(cegui_absdim(64.0f), cegui_absdim(10.0f)));

				m_pHpBar->setReverseProgress((float)(m_data.MaxHp - m_data.UpLimitHp) / m_data.MaxHp);
				m_pHpBar->setProgress((float)m_data.Hp / m_data.MaxHp);
				m_pHpBar->setMousePassThroughEnabled(true);
				m_pHpBar->SetFrameEnable(true);
			}
			m_pHpBar->setVisible(false);
		}
		if (m_data.school == eSchoolDaoZei)
		{
			if (IsFriendSide() || GetBattleManager()->IsInPVPBattle() == false)
			{
				if (m_pEpImages.size() == 0)
				{
					m_pEpImages.resize(5);
					CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
					for (int i = 0; i < 5; i++)
					{
						CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
						m_pEpImages[i] = static_cast <CEGUI::Window*>(winMgr.createWindow("TaharezLook/StaticImage", OnlyName));

						if (m_pEpImages[i] == NULL) continue;
						if (pRootWindow != NULL)
						{
							Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
							int yoffset = GetSprite()->GetBubbleItemOffset() == 0 ? DEFAULT_HPBAR_HEIGHT : GetSprite()->GetBubbleItemOffset();
							float ModelScale = 1.0f;
							if ((m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC))
							{
								const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(static_cast<int>(m_data.BattlerDataID));
								if (MonsterData.id != -1)
								{
									ModelScale = MonsterData.bodytype;
								}
							}
							Nuclear::NuclearLocation ptEpImage = GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top) + Nuclear::NuclearLocation(-31, (-yoffset - 26) * ModelScale);
							m_pEpImages[i]->setPosition(CEGUI::UVector2(cegui_absdim((float)ptEpImage.x + i * 12), cegui_absdim((float)ptEpImage.y)));
							m_pEpImages[i]->setAlwaysOnBottom(true);
							m_pEpImages[i]->setMousePassThroughEnabled(true);
							pRootWindow->addChildWindow(m_pEpImages[i]);
							m_pEpImages[i]->SetOnShiedUIState(false);
							m_pEpImages[i]->setSize(CEGUI::UVector2(cegui_absdim(17.0f), cegui_absdim(17.0f)));
							m_pEpImages[i]->setClippedByParent(false);
							if (i < m_data.Ep)
							{
								m_pEpImages[i]->setUserData((void*)2);
								m_pEpImages[i]->setProperty("Image", "set:huobanui image:lianji2");
							}
							else
							{
								m_pEpImages[i]->setUserData((void*)1);
								m_pEpImages[i]->setProperty("Image", "set:huobanui image:lianji1");
							}
						}
						m_pEpImages[i]->setVisible(false);
					}
				}
			}
		}
	}
	if (m_pPickBtn == NULL)
	{
		CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
		m_pPickBtn = static_cast <CEGUI::Window*>(winMgr.createWindow("TaharezLook/StaticImage", OnlyName));
	}
	if (m_pPickBtn)
	{
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow != NULL)
		{
			Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
			int yoffset = 0;
			Nuclear::NuclearLocation ptPickBtn = GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top) + Nuclear::NuclearLocation(-58, -yoffset - 15);
			m_pPickBtn->setAlwaysOnBottom(true);
			pRootWindow->addChildWindow(m_pPickBtn);
			m_pPickBtn->SetOnShiedUIState(false);
			m_pPickBtn->setPosition(CEGUI::UVector2(cegui_absdim((float)ptPickBtn.x), cegui_absdim((float)ptPickBtn.y)));
			m_pPickBtn->setSize(CEGUI::UVector2(cegui_absdim(115.0f), cegui_absdim(116.0f)));
			m_pPickBtn->cleanupAllEvent();
			m_pPickBtn->subscribeEvent(CEGUI::Window::EventMouseClick, CEGUI::Event::Subscriber(&Battler::HandlePickBtnClicked, this));
			m_pPickBtn->subscribeEvent(CEGUI::Window::EventMouseButtonUp, CEGUI::Event::Subscriber(&Battler::HandlePickBtnUp, this));
			m_pPickBtn->subscribeEvent(CEGUI::Window::EventMouseButtonDown, CEGUI::Event::Subscriber(&Battler::HandlePickBtnDown, this));
		}
		m_pPickBtn->setVisible(true);
	}
	if (m_pSkillNameRoot == NULL)
	{
		CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
		m_pSkillNameRoot = static_cast <CEGUI::Window*>(winMgr.createWindow("DefaultWindow", OnlyName));
	}
	if (m_pSkillNameRoot)
	{
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow != NULL)
		{
			Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
			int yoffset = 0;
			float ModelScale = 1.0f;
			if ((m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC))
			{
				const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(static_cast<int>(m_data.BattlerDataID));
				if (MonsterData.id != -1)
				{
					ModelScale = MonsterData.bodytype;
				}
			}
			Nuclear::NuclearLocation ptPos = GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top) + Nuclear::NuclearLocation(-92, (-yoffset - 80) * ModelScale);
			m_pSkillNameRoot->setAlwaysOnBottom(true);
			m_pSkillNameRoot->setMousePassThroughEnabled(true);
			pRootWindow->addChildWindow(m_pSkillNameRoot);
			m_pSkillNameRoot->SetOnShiedUIState(false);
			m_pSkillNameRoot->setPosition(CEGUI::UVector2(cegui_absdim((float)ptPos.x), cegui_absdim((float)ptPos.y)));
			m_pSkillNameRoot->setSize(CEGUI::UVector2(cegui_absdim(184.0f), cegui_absdim(40.0f)));
			m_pSkillNameRoot->subscribeEvent(CEGUI::AnimationInstance::EventAnimationStopped, CEGUI::Event::Subscriber(&Battler::HandleSkillNameStopped, this));
		}
		m_pSkillNameRoot->setVisible(false);
	}
	if (m_pSkillNameImage == NULL)
	{
		CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
		m_pSkillNameImage = static_cast <CEGUI::Window*>(winMgr.createWindow("TaharezLook/StaticImage", OnlyName));
	}
	if (m_pSkillNameImage)
	{
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow != NULL)
		{
			Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
			int yoffset = 0;
			Nuclear::NuclearLocation ptPos = Nuclear::NuclearLocation(0, 0);
			m_pSkillNameImage->setAlwaysOnBottom(true);
			m_pSkillNameImage->setMousePassThroughEnabled(true);
			m_pSkillNameRoot->addChildWindow(m_pSkillNameImage);
			m_pSkillNameImage->SetOnShiedUIState(false);
			m_pSkillNameImage->setPosition(CEGUI::UVector2(cegui_absdim((float)ptPos.x), cegui_absdim((float)ptPos.y)));
			m_pSkillNameImage->setSize(CEGUI::UVector2(cegui_absdim(184.0f), cegui_absdim(40.0f)));
			const char* pImage = "set:fightui image:skillname_bottom";
			m_pSkillNameImage->setProperty("Image", pImage);
			m_pSkillNameImage->setClippedByParent(false);
		}
		m_pSkillNameImage->setVisible(true);
	}
	if (m_pSkillNameText == NULL)
	{
		CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
		m_pSkillNameText = static_cast <CEGUI::Window*>(winMgr.createWindow("TaharezLook/StaticText", OnlyName));
	}
	if (m_pSkillNameText)
	{
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow != NULL)
		{
			Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
			int yoffset = 0;
			Nuclear::NuclearLocation ptPos = Nuclear::NuclearLocation(0, 0);
			m_pSkillNameText->setAlwaysOnBottom(true);
			m_pSkillNameText->setMousePassThroughEnabled(true);
			m_pSkillNameRoot->addChildWindow(m_pSkillNameText);
			m_pSkillNameText->SetOnShiedUIState(false);
			m_pSkillNameText->setPosition(CEGUI::UVector2(cegui_absdim((float)ptPos.x), cegui_absdim((float)ptPos.y)));
			m_pSkillNameText->setSize(CEGUI::UVector2(cegui_absdim(184.0f), cegui_absdim(40.0f)));
			m_pSkillNameText->setText(L"123");
			m_pSkillNameText->setFont(L"simhei-16");
			m_pSkillNameText->setClippedByParent(false);
			CEGUI::FalagardStaticText* wr = static_cast<CEGUI::FalagardStaticText*>(m_pSkillNameText->getWindowRenderer());
			wr->setHorizontalFormatting(CEGUI::HTF_CENTRE_ALIGNED);
		}
		m_pSkillNameText->setVisible(true);
	}
	if (m_pSkillNameAni == NULL)
	{
		CEGUI::AnimationManager* pAniMgr = CEGUI::AnimationManager::getSingletonPtr();
		m_pSkillNameAni = pAniMgr->instantiateAnimation(L"CombatEffect3");
		if (m_pSkillNameAni != NULL)
		{
			m_pSkillNameAni->setTargetWindow(m_pSkillNameImage);
			m_pSkillNameAni->setEventReceiver(m_pSkillNameRoot);
		}
		m_pSkillNameAni2 = pAniMgr->instantiateAnimation(L"CombatEffect3");
		if (m_pSkillNameAni2 != NULL)
		{
			m_pSkillNameAni2->setTargetWindow(m_pSkillNameText);
		}
	}
	UpdateHpBarLocation();
}
void Battler::RemoveHpBar()
{
	if (m_pHpBar)
	{
		m_pHpBar->destroy();
		m_pHpBar = NULL;
	}
	if(m_pLevelText)
	{
		m_pLevelText->destroy();
		m_pLevelText = NULL;
	}
	if (m_pPickBtn)
	{
		m_pPickBtn->destroy();
		m_pPickBtn = NULL;
	}
	if (m_pSkillNameAni)
	{
		CEGUI::AnimationManager* pAniMgr = CEGUI::AnimationManager::getSingletonPtr();
		pAniMgr->destroyAnimationInstance(m_pSkillNameAni);
		m_pSkillNameAni = NULL;
	}
	if (m_pSkillNameAni2)
	{
		CEGUI::AnimationManager* pAniMgr = CEGUI::AnimationManager::getSingletonPtr();
		pAniMgr->destroyAnimationInstance(m_pSkillNameAni2);
		m_pSkillNameAni2 = NULL;
	}
	if (m_pSkillNameText)
	{
		m_pSkillNameText->destroy();
		m_pSkillNameText = NULL;
	}
	if (m_pSkillNameImage)
	{
		m_pSkillNameImage->destroy();
		m_pSkillNameImage = NULL;
	}
	for (size_t i = 0; i < m_pEpImages.size(); i++)
	{
		m_pEpImages[i]->destroy();
		m_pEpImages[i] = NULL;
	}
	m_pEpImages.clear();
	if (m_pSkillNameRoot)
	{
		m_pSkillNameRoot->destroy();
		m_pSkillNameRoot = NULL;
	}
}
void Battler::UpdateHpBarLocation()
{
	float ModelScale = 1.0f;
	if ((m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC || m_data.BattlerType == eBattlerNaKaMa))
	{
		const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(static_cast<int>(m_data.BattlerDataID));
		if (MonsterData.id != -1)
		{
			ModelScale = MonsterData.bodytype;
		}
	}
	Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
	int yoffset = GetSprite()->GetBubbleItemOffset() == 0 ? DEFAULT_HPBAR_HEIGHT : GetSprite()->GetBubbleItemOffset();
	if (m_pHpBar)
	{
		Nuclear::NuclearLocation ptHpBar = GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top) + Nuclear::NuclearLocation(-30, (-yoffset - 36) * ModelScale);
		m_pHpBar->setPosition(CEGUI::UVector2(cegui_absdim((float)ptHpBar.x), cegui_absdim((float)ptHpBar.y)));
		if(m_pLevelText)
		{
			m_pLevelText->setPosition(CEGUI::UVector2(cegui_absdim((float)ptHpBar.x - 25.0f), cegui_absdim((float)ptHpBar.y)));
		}
	}
	if (m_pPickBtn)
	{
		Nuclear::NuclearLocation ptPickBtn = GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top) + Nuclear::NuclearLocation(-58, -120);
		m_pPickBtn->setPosition(CEGUI::UVector2(cegui_absdim((float)ptPickBtn.x), cegui_absdim((float)ptPickBtn.y)));
	}
	if (m_pSkillNameRoot)
	{
		Nuclear::NuclearLocation ptPos = GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top) + Nuclear::NuclearLocation(-92, (-yoffset - 80) * ModelScale);
		m_pSkillNameRoot->setPosition(CEGUI::UVector2(cegui_absdim((float)ptPos.x), cegui_absdim((float)ptPos.y)));
	}

	for (size_t i = 0; i < m_pEpImages.size(); i++)
	{
		Nuclear::NuclearLocation ptEpImage = GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top) + Nuclear::NuclearLocation(-31, (-yoffset - 26) * ModelScale);
		m_pEpImages[i]->setPosition(CEGUI::UVector2(cegui_absdim((float)ptEpImage.x + i * 12), cegui_absdim((float)ptEpImage.y)));
	}
}
void Battler::RefreshHpBar()
{
	if (m_pHpBar)
	{
		float desprogress = 0;
		if(m_data.MaxHp >= 1)
			desprogress = (float)m_data.Hp / m_data.MaxHp;
		m_pHpBar->setProgress(desprogress);
		m_pHpBar->setReverseProgress(0);
	}
	
	SetEPValue(m_data.Ep);

	if (m_bShowHPBarBig)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("bossdaxuetiao.CSetHPMaxHP", m_data.iBattleID, m_data.Hp, m_data.MaxHp);
	}
}
void Battler::AddLevelText(int level)
{
	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();

	CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
	if (m_pLevelText == NULL)
		m_pLevelText = static_cast <CEGUI::Window*>(winMgr.createWindow("TaharezLook/StaticText",OnlyName));//
	if (m_pLevelText)
	{
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow!=NULL)
		{
			Nuclear::NuclearRect cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
			int yoffset = DEFAULT_HPBAR_HEIGHT;
			if (GetSprite())
			{
				yoffset = GetSprite()->GetBubbleItemOffset() == 0?DEFAULT_HPBAR_HEIGHT:GetSprite()->GetBubbleItemOffset();
			}

			Nuclear::NuclearLocation ptHpBar = GetLocation() - Nuclear::NuclearLocation(cp.left,cp.top) + Nuclear::NuclearLocation(-30,-yoffset);
			m_pLevelText->setAlwaysOnBottom(true);
			pRootWindow->addChildWindow(m_pLevelText);
			m_pLevelText->SetOnShiedUIState(false);
			m_pLevelText->setPosition(CEGUI::UVector2(cegui_absdim((float)ptHpBar.x - 25.0f), cegui_absdim((float)ptHpBar.y)));
			m_pLevelText->setSize(CEGUI::UVector2(cegui_absdim(25.0f),cegui_absdim(15.0f)));

			m_pLevelText->setMousePassThroughEnabled(true);
			m_pLevelText->setProperty("FrameEnabled","false");
			m_pLevelText->setProperty("BackgroundEnabled","false");
			m_pLevelText->setProperty("HorzFormatting","RightAligned");
			m_pLevelText->setProperty("DefaultBorderEnable","true");
			m_pLevelText->setProperty("BorderColour","FF351051");
			m_pLevelText->setProperty("TextColours","FFEAFF00");
			m_pLevelText->setText(CEGUI::PropertyHelper::intToString(level));
		}

		m_pLevelText->setVisible(false);
	}
}
void Battler::SetEPValue(int vl)
{
	m_data.Ep = vl;
	for (size_t i = 0; i < m_pEpImages.size(); i++)
	{
		const char* pImage = "set:huobanui image:lianji1";
		const char* pImage2 = "set:huobanui image:lianji2";
		if ((int)i < m_data.Ep)
		{
			if ((long)m_pEpImages[i]->getUserData() != 2)
			{
				m_pEpImages[i]->setUserData((void*)2);
				m_pEpImages[i]->setProperty("Image", pImage2);					
				gGetGameUIManager()->AddUIEffect(m_pEpImages[i], MHSD_UTILS::GetEffectPath(11050), false, 0, 0, true);
			}
		}
		else
		{
			if ((long)m_pEpImages[i]->getUserData() != 1)
			{
				m_pEpImages[i]->setUserData((void*)1);
				m_pEpImages[i]->setProperty("Image", pImage);
			}
		}
	}
}
int Battler::GetEPValue()
{
	return m_data.Ep;
}
void Battler::SetHPValue(int vl)
{
	m_data.Hp = vl;
}
int Battler::GetHPValue()
{
	return m_data.Hp;
}
bool Battler::SetCharacter(int64_t characterID,const int&shapeID,const std::map<char,int>& components)
{
	if (NULL == GetSprite())
	{
		NewSpriteModel(shapeID);
		m_iAttackDistance = AttackDistance;
	}
	if (m_data.eQuipEffect != 0)
	{
		int effectId = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("EquipEffect_GetID",m_data.eQuipEffect);
		if (effectId != 0 && m_bEquipEffectOn == false)
		{
			m_bEquipEffectOn = true;
			PlayEffect(MHSD_UTILS::GetEffectPath(effectId), false, true, false, -1, 0);
		}
	}
	return true;
}
bool Battler::SetCharacter(int64_t characterID,const int&shapeID,
							const std::map<char,int>& components, std::wstring modelName, 
							std::wstring hair, std::wstring head, std::wstring body)
{
	if (NULL == GetSprite())
	{
		NewSpriteModel(shapeID, modelName,hair, head, body);
	}
	return true;
}
bool Battler::SetPet(int64_t petID, int shapeID)
{
	int petData_id = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "id");
	if (petData_id != -1)
	{
		int petData_modelid = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "modelid");
		std::wstring petData_name = GetTableWStringFromLua("pet.cpetattr", static_cast<int>(petID), "name");
		int petData_area1colour = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "area1colour");
		int petData_area2colour = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "area2colour");

		if (NULL != GetSprite())
			DeleteSpriteModel();
		m_data.iShapeID = shapeID != 0 ? shapeID : petData_modelid;
		NewSpriteModel(m_data.iShapeID);

		m_iAttackDistance = AttackDistance;
		m_strName = petData_name;

		SetSpriteComponent(eSprite_DyePartA, petData_area1colour, true);
		SetSpriteComponent(eSprite_DyePartB, petData_area2colour, true);
	}
	return true;
}
bool Battler::SetPet(int64_t petID,int shapeID, std::wstring modelName, std::wstring hair, std::wstring head, std::wstring body)	
{
	int petData_id = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "id");
	if (petData_id != -1)
	{
		int petData_modelid = GetTableNumberFromLua("pet.cpetattr", static_cast<int>(petID), "modelid");
		std::wstring petData_name = GetTableWStringFromLua("pet.cpetattr", static_cast<int>(petID), "name");

		if (NULL != GetSprite())
			DeleteSpriteModel();
		m_data.iShapeID = shapeID != 0 ? shapeID : petData_modelid;
		NewSpriteModel(m_data.iShapeID, modelName, hair, head, body);		
		m_strName = petData_name;
	}
	return true;
}
bool Battler::SetCreeps(int64_t creepID,int shapeID)
{
	if ( NULL != GetSprite())
		DeleteSpriteModel();
	const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(static_cast<int>(creepID));
	if (MonsterData.id != -1)
	{
		m_bCreepsShowHpBar = MonsterData.showhpbar != 0;	

		m_strName = MonsterData.name;
		m_bCanBeCatch = MonsterData.becatch != 0;
		//m_data.iShapeID = shapeID != 0 ? shapeID : MonsterData.modelID;
		m_data.iShapeID = MonsterData.modelID == 0 ? shapeID : MonsterData.modelID;

	}
	if (shapeID == 0)
		shapeID = m_data.iShapeID;
	NewSpriteModel(shapeID);
	GetSprite()->SetScale(MonsterData.bodytype);
	m_iAttackDistance = AttackDistance;
	
	if ((m_data.BattlerType == eBattlerMonster || m_data.BattlerType == eBattlerNPC) && MonsterData.showlevel != 0 && m_bCreepsShowHpBar)
	{
		AddLevelText(m_data.level);
	}
	if (GetSprite() != NULL)
	{
		SetSpriteComponent(eSprite_DyePartA, MonsterData.area1colour);
		SetSpriteComponent(eSprite_DyePartB, MonsterData.area2colour);
	}
	return true;
}
void Battler::SetTitleID(const int &titleid)
{
	m_data.iTitleID = titleid;
}
int Battler::GetTitleID() const
{
	return m_data.iTitleID;
}
void Battler::UpdataTitleTexture()
{
	int titleid = GetTableNumberFromLua("title.ctitleconfig", m_data.iTitleID, "id");
	if (titleid != -1) {
		if (m_TitleTextureHandle!=Nuclear::INVALID_TEXTBLOCK_HANDLE)
		{
			Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
			if (pFontMan)
			{
				pFontMan->ReleaseTextBlock(m_TitleTextureHandle);
			}
			m_TitleTextureHandle=Nuclear::INVALID_TEXTBLOCK_HANDLE;
			m_pTitleTextBlock=NULL;
		}
		std::wstring titlename;
		if (m_data.strTitle.empty()) {
			GetTableWStringFromLua("title.ctitleconfig", m_data.iTitleID, "titlename");
		} else {
			titlename = m_data.strTitle;
		}
		if (!titlename.empty()) {
			DWORD color = GetABGRColorByStringNew(GetTableWStringFromLua("title.ctitleconfig", m_data.iTitleID, "fontcolor"));
			Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
			if (pFontMan)
			{
				m_TitleTextureHandle=pFontMan->NewText(titlename.c_str(),0,color,color);
				
				m_pTitleTextBlock=pFontMan->GetTextBlock(m_TitleTextureHandle);
			}
		}
	}
	if (m_FlagTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_FlagTextureHandle);
		}
		m_FlagTextureHandle = Nuclear::INVALID_TEXTBLOCK_HANDLE;
		m_pFlagTextBlock = NULL;
	}
	if (!m_strflag.empty())
	{
		DWORD color = 0xff00ffff;
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			m_FlagTextureHandle = pFontMan->NewText(m_strflag.c_str(), 0, color, color);
			m_pFlagTextBlock = pFontMan->GetTextBlock(m_FlagTextureHandle);
		}
	}
}
void Battler::SetBattleLocation()
{		
	if (IsWatcher())	//观战位
		m_ptBattlePosition = GetBattleManager()->GetWatcherLocation(m_data.iBattleID);
	else
		m_ptBattlePosition = GetBattleManager()->GetBattleLocation(m_data.iBattleID);
	
	if (m_ptBattlePosition == Nuclear::NuclearLocation(0,0))
	{
		SetVisible(false);
	}
	SetLocation(m_ptBattlePosition);
}
void Battler::ChangeSpriteModel(const int& modelid, int effectID)
{
	int mid = modelid;
	if (mid <= 10 && mid > 0)
		mid = 1010100 + mid;
	else if (mid < 300 && mid > 0)
		mid = 2010100 + mid % 100;
	if (mid == 0)
		mid = m_data.iShapeID;
	ComponentsObject::ChangeSpriteModel(mid);
	m_iAttackDistance = AttackDistance;
	if (effectID != 0)
		PlayEffect(MHSD_UTILS::GetEffectPath(effectID));
}
Nuclear::NuclearLocation Battler::GetAttackerPositionBehind()
{
	Nuclear::NuclearLocation ptBehind;
	if (IsFriendSide())
		ptBehind = m_ptBattlePosition + c_nAttackLocation;
	else if (IsEnemySide())
		ptBehind = m_ptBattlePosition - c_nAttackLocation;
	else
		return Nuclear::NuclearLocation(-1,-1);

	Nuclear::NuclearLocation mapsize;
	Nuclear::GetEngine()->GetWorld()->GetMapSize(mapsize);
	if (ptBehind.x <= 0)
		ptBehind.x = 1;
	if (ptBehind.x >= mapsize.x)
		ptBehind.x = mapsize.x - 1;
	if (ptBehind.y <= 0)
		ptBehind.y = 1;
	if (ptBehind.y >= mapsize.y)
		ptBehind.y = mapsize.y - 1;
	return ptBehind;
}
Nuclear::NuclearLocation Battler::GetAttackerClosePosition()
{
	if (IsFriendSide())
		return	m_ptBattlePosition - c_nAttackCloseLocation;
	else if (IsEnemySide())
		return	m_ptBattlePosition + c_nAttackCloseLocation;
	else
		return Nuclear::NuclearLocation(-1,-1);
}
Nuclear::NuclearLocation Battler::GetAttackerPosition(int attackdistance)
{
	Nuclear::NuclearLocation attackloc(attackdistance,attackdistance*2/3);
	if (IsFriendSide())
		return	m_ptBattlePosition - attackloc;
	else if (IsEnemySide())
		return	m_ptBattlePosition + attackloc;
	else
		return Nuclear::NuclearLocation(-1,-1);
}
Nuclear::NuclearLocation Battler::GetAttackerSidePosition(bool bleft)
{
	if (IsFriendSide())
		return	m_ptBattlePosition - (bleft?c_nAttackSideLocation:-c_nAttackSideLocation);
	else if (IsEnemySide())
		return	m_ptBattlePosition + (bleft?c_nAttackSideLocation:-c_nAttackSideLocation);
	else
		return Nuclear::NuclearLocation(-1,-1);
}
Nuclear::NuclearLocation Battler::GetAttackerFarPosition()
{
	if (IsFriendSide())
		return	m_ptBattlePosition - c_nAttackLocation - c_nAttackLocation;
	else if (IsEnemySide())
		return	m_ptBattlePosition + c_nAttackLocation + c_nAttackLocation;
	else
		return Nuclear::NuclearLocation(-1,-1);
}
Nuclear::NuclearLocation Battler::GetProtecterPosition()
{
	if (IsFriendSide())
		return	m_ptBattlePosition - c_nProtectLocation;
	else if (IsEnemySide())
		return	m_ptBattlePosition + c_nProtectLocation;
	else
		return Nuclear::NuclearLocation(-1,-1);
}
Nuclear::NuclearLocation Battler::GetDodgePosition()
{
	if (IsFriendSide())
		return	m_ptBattlePosition + c_nDodgeLocation;
	else if (IsEnemySide())
		return	m_ptBattlePosition - c_nDodgeLocation;
	else
		return Nuclear::NuclearLocation(-1,-1);
}
void Battler::SetBattleDirection()
{
	if (GetSprite())
	{
		Nuclear::NuclearLocation pt = GetAttackerPosition();
		if (IsFriendSide())
        {
            SetDirection(Nuclear::XPDIR_TOPLEFT);
        }			
		else if (IsEnemySide())
        {
			SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
        }
		else
		{
			SetDirection(Nuclear::XPDIR_BOTTOMLEFT);
		}
	}
}
void Battler::OnHpChangeOutSideBattle(int curhp)
{
	m_data.Hp = curhp;
}
void Battler::OnHitNumber(int num, eBattleResult eHitType, int FlagType, bool critic)
{
	if ( eHitType == eBattleResult_HPChange )
	{
		if (num > 0 && num > m_data.UpLimitHp - m_data.Hp)
			num = m_data.UpLimitHp - m_data.Hp;
		m_data.Hp += num;	

		if (m_data.Hp > m_data.UpLimitHp)
		{
			m_data.Hp = m_data.UpLimitHp;
		}
		if (m_data.Hp < 0)
		{
			m_data.Hp = 0;
		}

		if (GetBattleManager())
		{
			if (GetBattleManager()->GetMainBattleChar() == this)
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainRoleData_UpdateHpChange", num, 0);
			else if (GetBattleManager()->GetMainBattlePet() == this)
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainPetDataManager_UpdateBattlePetHpChange", num, 0);
		}

		if (num > 0)
		{
			PlayEffect(MHSD_UTILS::GetEffectPath(10155));
			if (critic)
			{
				m_pArtText->AddHPMPNumber(eHPAddBang, num, FlagType);
			}
			else
			{
				m_pArtText->AddHPMPNumber(eHPAdd, num, FlagType);
			}
		}
		else if (num < 0)
		{
			if (critic)
			{
				m_pArtText->AddHPMPNumber(eHPSubBang, -num, FlagType);
			}
			else
			{
				m_pArtText->AddHPMPNumber(eHPCount, -num, FlagType);
			}
		}
		
	}
	else if(eHitType == eBattleResult_MPChange)
	{
		if (GetBattleManager())
		{
			if (GetBattleManager()->GetMainBattleChar() == this)
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainRoleData_UpdateMpChange", num, 0);
			else if (GetBattleManager()->GetMainBattlePet() == this)
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWith2Int("MainPetDataManager_UpdateBattlePetMpChange", num, 0);
		}

		if (num > 0)
		{
			PlayEffect(MHSD_UTILS::GetEffectPath(10156));
			m_pArtText->AddHPMPNumber(eMPAdd, num, FlagType);
		}
		else if (num < 0)
		{
			m_pArtText->AddHPMPNumber(eMPSub, -num, FlagType);
		}
	}
}
void Battler::PopEffectText(int type)
{
	m_pEffectText->AddHPMPNumber((enumHPMPChangeType)type, 0, 0);
}
void Battler::ProcessBuffChange(const sBattleBuffResult& buff)
{
	//-1代表删除
	if (buff.buffdata == -1)
	{
		RemoveBattleBuff(buff.buffid);
	}
	else
	{
		AddBattleBuff(buff.buffid,buff.buffdata);
	}

	if (GetBattleManager())
	{
		GetBattleManager()->EventBattlerBuffChange(m_data.iBattleID);
	}
}
void Battler::SetDefaultAction(eActionType actionType,bool holdlastframe,float freq)
{
	if (m_bShake)
	{
		if (actionType == eActionBattleStand)
		{
			GetSprite()->StartShake();
		}
		else
		{
			GetSprite()->StopShake();
		}
	}

	freq = freq/m_fDemoSpeedRate;
	if ((GetBattleManager() && (GetBattleManager()->IsInBattleDemo() || m_data.OperateState == eOperateState_Wait))
	 && !m_bBattleAttackFinish
	 && actionType == eActionBattleStand
	 && !IsWatcher())
	{
		if(m_actionType != eActionRoll)
		{
			SceneObject::SetDefaultAction(eActionBattleStand, holdlastframe, freq);
			m_actionType = eActionBattleStand;
		}
	}
	else
	{
		SceneObject::SetDefaultAction(actionType,holdlastframe,freq);
	}
}
void Battler::SetBattlerDemoSpeedRate(float rate)
{
	m_fDemoSpeedRate = rate;
	if (GetSprite())
	{
		float movespeed = GetSprite()->GetMoveSpeed();
		GetSprite()->SetMoveSpeed(movespeed*rate);
	}
}
void Battler::RegistListener(IActionListener* aPListener)
{
	for (unsigned int i = 0; i < m_Listeners.size(); i++)
	{
		if (m_Listeners[i] == aPListener)
		{
			return;
		}
	}
	m_Listeners.push_back(aPListener);
}
void Battler::UnregistListener(IActionListener* aPListener)
{
	for (unsigned int i = 0; i < m_Listeners.size(); i++)
	{
		if (m_Listeners[i] == aPListener)
		{
			m_Listeners[i] = m_Listeners[m_Listeners.size() - 1];
			m_Listeners.pop_back();
			return;
		}
	}
}
void Battler::NewPlayAction(eActionType actionType,float freq,bool realmagic,eActionType eSpecialActionType,bool magicnotify,bool attackeffect)
{
	if (m_bShake)
	{
		if (actionType == eActionBattleStand)
		{
			GetSprite()->StartShake();
		}
		else
		{
			GetSprite()->StopShake();
		}
	}
    m_actionType = actionType;
    if(!ComponentsObject::PlayAction(actionType,freq))
    {
        if(m_actionType == eActionAttack)
        {
			ComponentsObject::PlayAction(eActionAttack, freq);
        }
		else if(m_actionType == eActionMagic1)
        {
			ComponentsObject::PlayAction(eActionMagic1, freq); 
        }
    }
    
}
void Battler::PlayAction(eActionType actionType,float freq,bool realmagic,eActionType eSpecialActionType,bool magicnotify,bool attackeffect)
{
	if (m_bShake)
	{
		if (actionType == eActionBattleStand)
		{
			GetSprite()->StartShake();
		}
		else
		{
			GetSprite()->StopShake();
		}
	}
	freq = freq/m_fDemoSpeedRate;
	if (!IsWatcher() && !m_bBattleAttackFinish && (GetBattleManager() && (GetBattleManager()->IsInBattleDemo() || m_data.OperateState == eOperateState_Wait)))
	{
		if (actionType == eActionBattleStand)
		{
			eSpecialActionType = eActionBattleStandReady;
		}
	}
	std::wstring actionname = L"";
	if (GetSprite())
	{
		actionname = GetSprite()->GetCurActionName();
	}
	if ((actionname == GetActionName(actionType) || actionname == GetActionName(eSpecialActionType))
	 && actionType != eActionBattleStand && !actionname.empty())
	{
		ComponentsObject::PlayAction(eActionBattleStand,g_defaultActionFrequence);
	}
	
	if (actionType == eActionOnHit1 || eSpecialActionType == eActionOnHit1)
	{
		freq = 1.5f;
	}
	else if (actionType == eActionRoll)
	{
		freq = 0.25f;
	}
	else if (actionType == eActionRunaway)
	{
		freq = 0.5f;
	}

	freq *= m_fGlobalAnimFreq;

	if (eSpecialActionType != eActionNull)
	{
		ComponentsObject::PlayAction(eSpecialActionType,freq);
		m_actionType = actionType;
	}
	else
	{
		ComponentsObject::PlayAction(actionType,freq);
	}
	
	if (actionType == eActionRoll)
	{
		ChangeRollDirection();
	}
}
std::wstring Battler::GetActionEffectSoundName()
{
    return L"";
}
void Battler::ChangeRollDirection()
{
    if (IsFriendSide())
    {
        SetDirection(Nuclear::XPDIR_TOPLEFT);
    }
    else if (IsEnemySide())
    {
        SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
    }
}
void Battler::StopTeleport()
{
	GetSprite()->StopTeleport(m_ptBattlePosition);
}
void Battler::UpdateAction(Nuclear::XPUSA_TYPE type)
{
    if(type == Nuclear::XPUSAT_TELEPORTEND)
    {
        if (IsOnBattlePosition())	
        {
			if(GetSprite())
				GetSprite()->SetVisible(true);
            
			if (m_bHitMoving == false && m_actionType == eActionDodge)		
				PlayAction(eActionBattleStand);
            SetLocation(m_ptBattlePosition);
        }
        else
        {
			float time = 150.0f / m_fDemoSpeedRate;
			float time1 = 50.0f / m_fDemoSpeedRate;
			if (GetSprite())
				GetSprite()->TeleportWithBlur(m_ptBattlePosition, static_cast<int>(time), static_cast<int>(time1));
        }
    }
        
    for(unsigned int i = 0; i < m_Listeners.size(); i++)
    {
        m_Listeners[i]->OnUpdateAction(type, m_actionType);
    }

	switch (type)
	{
		case Nuclear::XPUSAT_DEFAULTACTIONFINISH:
		case Nuclear::XPUSAT_PLAYACTIONFINISH:
		{
			if (m_actionType == eActionDying)
			{
			}
			if (m_actionType == eActionDefence)
			{
				if (m_bDeath || m_bGhost || m_bDieVanish)	//受击动作结束之后倒地
				{
					TurnTo(this);
					m_actionType = eActionDeath;
					SetDefaultAction(eActionDeath, true);
				}
			}
		}
	}

    return;    
}
void Battler::OnBeforeRender(int now)
{
}
void Battler::ResetSpeed()
{
	if (gGetDisPlayMode() == eDisplayMode_1024768)
	{
		GetSprite()->SetMoveSpeed(c_fBattlerMoveSpeed*1.23f*m_fDemoSpeedRate/1000);
	}
	else
	{
		GetSprite()->SetMoveSpeed(c_fBattlerMoveSpeed*m_fDemoSpeedRate/1000);
	}
}
void Battler::SetOperateState(eOperateState state)
{
	if (IsWatcher())
	{
		return;
	}
	int yoffset = GetSprite()->GetBubbleItemOffset() == 0?DEFAULT_HPBAR_HEIGHT:GetSprite()->GetBubbleItemOffset();

	m_data.OperateState = state;
	//只有人物显示这些
	if (m_data.BattlerType == eBattlerCharacter || m_data.BattlerType == eBattlerPet)
	{
		if (m_pOperateStateEffect)
		{
			RemoveDurativeEffect(m_pOperateStateEffect);
			m_pOperateStateEffect = NULL;
		}
		switch(m_data.OperateState)
		{
		case eOperateState_Null:
			{
				//人物掉线后回来还在demo状态的时候，要发eOperateState_Null状态并置于宠物
				if (m_data.BattlerType == eBattlerCharacter)
				{
					Battler* pPet = GetBattleMagicControl()->FindBattlerByID(m_data.iBattleID+5);
					if (pPet)
					{
						pPet->SetOperateState(eOperateState_Null);
					}
				}
			}
			break;
		case eOperateState_Prepare:
			{
				m_pOperateStateEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10161), 0, 0, DEFAULT_HPBAR_HEIGHT - yoffset - 13 - 30, false, false, true);
			}
			break;
		case eOperateState_Wait:	
			{
				ReadyBattleDemo();
			}
			break;
		case eOperateState_FallLine:	
			{
				m_pOperateStateEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10160), 0, 0, DEFAULT_HPBAR_HEIGHT - yoffset - 13 - 30, false, false, true);
				if (m_data.BattlerType == eBattlerCharacter)
				{
					Battler* pPet = GetBattleMagicControl()->FindBattlerByID(m_data.iBattleID+5);
					if (pPet)
					{
						pPet->SetOperateState(eOperateState_FallLine);
					}
				}
			}
			break;
		default:
			break;
		}
	}

}
void Battler::ClientSetOperateState(eOperateState state)
{
	if (m_data.OperateState == eOperateState_FallLine)
	{
		return;
	}
	SetOperateState(state);
}
BattleIllusion* Battler::AddPhantom(int ID)
{
	std::map<int, BattleIllusion*>::iterator it;
	it = m_Phantoms.find(ID);
	if (it != m_Phantoms.end())
	{
		return it->second;
	}
	else
	{
		sBattlerData stData = m_data;
		stData.iShapeID = GetModelID();
		BattleIllusion* Result = GetBattleManager()->AddBattleIllusion(stData, GetScale());
		Result->SetLocation(GetLocation());
		Result->SetDirection(GetDirection());
		Result->SetAlpha(127);
		m_Phantoms[ID] = Result;
		return Result;
	}
}
void Battler::DelPhantom(int ID)
{
	if (ID <= 0)
	{
		return;
	}
	std::map<int, BattleIllusion*>::iterator it;
	it = m_Phantoms.find(ID);
	if (it != m_Phantoms.end())
	{
		it->second->SetDisappear();
		m_Phantoms.erase(it);
	}
}
BattleIllusion* Battler::FindPhantom(int ID)
{
	std::map<int, BattleIllusion*>::iterator it;
	it = m_Phantoms.find(ID);
	if (it != m_Phantoms.end())
	{
		return it->second;
	}
	return NULL;
}
BattleIllusion* Battler::FindAndAddPhantom(int ID)
{
	std::map<int, BattleIllusion*>::iterator it;
	it = m_Phantoms.find(ID);
	if (it != m_Phantoms.end())
	{
		return it->second;
	}
	else
	{
		return AddPhantom(ID);
	}
}
void Battler::ClearPhantom()
{
	for (std::map<int, BattleIllusion*>::iterator it = m_Phantoms.begin(); it != m_Phantoms.end(); it++)
	{
		it->second->SetDisappear();
	}
	m_Phantoms.clear();
}
Nuclear::IEffect* Battler::CreateLightEffect(const std::wstring& effect_name, Battler* pTarget, int startPos, int appearTime, int disappearTime, int totalTime)
{
	if (startPos < 1)	return NULL;
	Nuclear::NuclearLocation sPos;
	if (startPos == 1)	
	{
		m_lightingPos.clear();
		m_lightingPos.push_back(GetLocation()+GetSprite()->GetEffectPos());
	}
	if (m_lightingPos.size() <= 0)
		return NULL;
	size_t posID = startPos - 1;
	if (posID >= m_lightingPos.size())
		posID = m_lightingPos.size() - 1;
	sPos = m_lightingPos[posID];
	Nuclear::lightingEffect* pTemp = dynamic_cast<Nuclear::lightingEffect*>(Nuclear::GetEngine()->GetWorld()->SetEffect(effect_name, Nuclear::XPEL_BATTLE_HEIGHT, sPos.x, sPos.y - 50, false));
	if (pTemp)
	{
		Nuclear::NuclearLocation lpos = pTarget->GetLocation() + pTarget->GetSprite()->GetEffectPos();
		pTemp->SetTargetPos(lpos.x, lpos.y - 50);
		pTemp->SetLightTime(appearTime, disappearTime, totalTime);
		m_lightingPos.push_back(lpos);
	}
	return pTemp;
}
void Battler::AddBattleBuff(int buffid, int leftround)
{
	bool havebuff = false;
	for (std::list<sBattleBuff>::iterator it = m_BattleBuffList.begin(); it != m_BattleBuffList.end(); it++)
	{
		if (it->buffid == buffid)
		{
			havebuff = true;
			it->leftround = leftround;
			it->tipsparam.clear();
			if (GetBattleManager())
			{
			}
			for (size_t i = 0; i < it->SpecialShow.size(); i++)
			{
				if (it->SpecialShow[i] == 1)
					m_NormalAlpha = 128;
			}
			break;
		}
	}
	if (!havebuff)
	{
		sBattleBuff newBattleBuff;
		newBattleBuff.buffid = buffid;
		const GameTable::buff::CBuffConfig& buffconfig = GameTable::buff::GetCBuffConfigTableInstance().getRecorder(buffid);
		if (buffconfig.id != -1 && buffconfig.effect != L"" && NULL == newBattleBuff.buffeffect)
		{
			bool alwayontop = false;
			bool underSprite = false;
			if (buffconfig.effectsort > 0)
			{
				alwayontop = true;
			}
			else if (buffconfig.effectsort < 0)
			{
				underSprite = true;
			}
			newBattleBuff.buffeffect = SetDurativeEffect(buffconfig.effect, 0, 0, 0, false, false, false, alwayontop, underSprite);
		}
		newBattleBuff.leftround = leftround;

		for (size_t i = 0; i < buffconfig.specialshow.size(); i++)
		{
			if (buffconfig.specialshow[i] == 1)
			{
				m_NormalAlpha = 128;
			}
			else if (buffconfig.specialshow[i] == 2)
			{
				SetShake(true);
			}
			newBattleBuff.SpecialShow.push_back(buffconfig.specialshow[i]);
		}

		m_BattleBuffList.push_back(newBattleBuff);
	}
}
void Battler::RemoveBattleBuff(int buffid)
{
	for (std::list<sBattleBuff>::iterator it = m_BattleBuffList.begin(); it != m_BattleBuffList.end(); it++)
	{
		if (it->buffid == buffid)
		{
			for (size_t i = 0; i < it->SpecialShow.size(); i++)
			{
				if (it->SpecialShow[i] == 1)
				{
					if (!IsHaveBattleBuffSpecialShow(1, buffid))
					{
						m_NormalAlpha = 255;
					}
				}
				else if (it->SpecialShow[i] == 2)
				{
					if (!IsHaveBattleBuffSpecialShow(2, buffid))
					{
						SetShake(false);
					}
				}
			}
			if (it->buffeffect)
			{
				RemoveDurativeEffect(it->buffeffect);
			}
			it = m_BattleBuffList.erase(it);
			break;
		}
	}
}
void Battler::RemoveAllBattleBuff()
{
	for (std::list<sBattleBuff>::iterator it = m_BattleBuffList.begin(); it != m_BattleBuffList.end(); it++)
	{
		for (size_t i = 0; i < it->SpecialShow.size(); i++)
		{
			if (it->SpecialShow[i] == 1)
			{
				if (!IsHaveBattleBuffSpecialShow(1, it->buffid))
				{
					m_NormalAlpha = 255;
				}
			}
			else if (it->SpecialShow[i] == 2)
			{
				if (!IsHaveBattleBuffSpecialShow(2, it->buffid))
				{
					SetShake(false);
				}
			}
		}
		if (it->buffeffect)
		{
			RemoveDurativeEffect(it->buffeffect);
		}
	}
	m_BattleBuffList.clear();
}
bool Battler::IsSetBattleBuff(int buffid)
{
	for (std::list<sBattleBuff>::iterator it=m_BattleBuffList.begin();it!=m_BattleBuffList.end();it++)
	{
		if (it->buffid == buffid)
		{
			return true;
			break;
		}
	}
	return false;
}
bool Battler::IsHaveBattleBuffSpecialShow(int SpecialShowType, int ExcludeBuffID)
{
	for (std::list<sBattleBuff>::iterator it = m_BattleBuffList.begin(); it != m_BattleBuffList.end(); it++)
	{
		if (it->buffid == ExcludeBuffID)
		{
			continue;
		}
		for (size_t i = 0; i < it->SpecialShow.size(); i++)
		{
			if (it->SpecialShow[i] == SpecialShowType)
			{
				return true;
			}
		}
	}
	return false;
}
void Battler::CheckBuffBeforeOperate(eBattleOperate	BattleOperate)
{
	for (std::list<sBattleBuff>::iterator it = m_BattleBuffList.begin(); it != m_BattleBuffList.end(); it++)
	{
		int buffid = it->buffid;
		
		const GameTable::buff::CBuffConfig& buffconfig = GameTable::buff::GetCBuffConfigTableInstance().getRecorder(buffid);

		if (buffconfig.cleartype == 4 || buffconfig.cleartype == 5)
		{
			for (size_t i = 0; i < buffconfig.specialshow.size(); i++)
			{
				if (buffconfig.specialshow[i] == 1)
				{
					if (BattleOperate == eOperate_Skill 
							|| BattleOperate == eOperate_Attack && buffconfig.cleartype == 5)
					{
						SetNormalAlpha(255, false);
						return;
					}
				}
			}
		}
	}
}
void Battler::GetBattleBuffInfoByID(int buffid,sBattleBuff& info)
{
	for (std::list<sBattleBuff>::iterator it=m_BattleBuffList.begin();it!=m_BattleBuffList.end();it++)
	{
		if (it->buffid == buffid)
		{
			info = (*it);
			break;
		}
	}
}
void Battler::GetBattleBuffInfoByIndex(int Index, sBattleBuff& info)
{
	int i = 0;
	for (std::list<sBattleBuff>::iterator it = m_BattleBuffList.begin(); it != m_BattleBuffList.end(); it++)
	{
		if (i == Index)
		{
			info.buffid = (*it).buffid;
			info.leftround = (*it).leftround;
			return;
		}
		i++;
	}
}
int	Battler::GetBattleBuffCount()
{
	return m_BattleBuffList.size();
}
int	Battler::GetBattleBuffIDByIndex(int Index)
{
	int i = 0;
	for (std::list<sBattleBuff>::iterator it = m_BattleBuffList.begin(); it != m_BattleBuffList.end(); it++)
	{
		if (i = Index)
		{
			return it->buffid;
		}
		i++;
	}
	return 0;
}
void Battler::ResetBattlerLocation()
{
	if (!IsGeneralDie() && !IsOnBattlePosition())
	{
		SetLocation(m_ptBattlePosition);
		SetBattleDirection();
	}
	UpdateHpBarLocation();
	m_stMoveInfo.time = 0;
}
void Battler::TurnTo(SceneObject* pSceneObj)
{	
	if (pSceneObj)
	{
        if (IsFriendSide())
        {
            SetDirection(Nuclear::XPDIR_TOPLEFT);
        }
		else if (IsEnemySide())
        {
            SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
        }
	}
}
void Battler::SetDemoShowEnd(bool b)
{
	if (m_data.BattlerType == eBattlerCharacter || m_data.BattlerType == eBattlerPet)
	{
		m_bDemoShowEnd = b;
		if (m_bDemoShowEnd)
		{
			GetSprite()->SetActive(true);
		}
	}
}
void Battler::RrfreshDemoShow(bool b)
{
	if (!m_bDemoShowEnd&&b)
	{
		GetSprite()->SetActive(false);
	}
	else
	{
		GetSprite()->SetActive(true);
	}
}
void Battler::AddDemoShowNotEndEffect(bool add)
{
	if (m_data.BattlerType == eBattlerCharacter && !IsWatcher())
	{
		if (add && !m_bDemoShowEnd)
		{
			if (NULL == m_pDemoShowNotEndEffect)
			{
				int yoffset = GetSprite()->GetBubbleItemOffset() == 0?DEFAULT_HPBAR_HEIGHT:GetSprite()->GetBubbleItemOffset();
				m_pDemoShowNotEndEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10024), 0, 0, DEFAULT_HPBAR_HEIGHT - yoffset - 13, false, false, true);	//
			}
		}
		else
		{
			if (m_pDemoShowNotEndEffect)
			{
				RemoveDurativeEffect(m_pDemoShowNotEndEffect);
				m_pDemoShowNotEndEffect = NULL;
			}
		}
	}
}

void Battler::SetFlag(std::wstring flag)
{
	m_strflag = flag;
	UpdataTitleTexture();
};