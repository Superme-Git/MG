#include "stdafx.h"
#include "GameScene.h"
#include "MainCharacter.h"
#include "MainRoleDataManager.h"
#include "GameUIManager.h"
#include "GameTable/effect/Ccoloreffect.h"
#include "GameTable/effect/CEffectConfig.h"
#include "GameTable/move/CAutoMovePath.h"
#include "GameTable/move/CAutoMovePathPoint.h"
#include "../world/nuworld.h"

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

const int CCharacterInfoManager::s_popoInterval = 800;

void PopoEffectNotify::OnEnd(Nuclear::IEffect *pEffect) {
	pEffect->Stop();
}
void PopoEffectNotify::OnDelete(Nuclear::IEffect *pEffect) {}

PopoEffect::PopoEffect(int effectid, int value, int valuecolor) :
d_effectid(effectid)
, d_value(value)
, d_color(valuecolor)
, d_pPropertyEffect(0)
, d_pAddEffect(0) {
}
PopoEffect::~PopoEffect() {
	delete d_pPropertyEffect;
	delete d_pAddEffect;
	for (NumEffects::iterator iter = d_numEffects.begin(); iter != d_numEffects.end(); iter++) {
		delete *iter;
	}
	d_numEffects.clear();
}

int PopoEffect::getNumberEffectid(int num, int color) {
	const GameTable::effect::Ccoloreffect* pEffectConfig;
	if (num == 0)
		pEffectConfig = &GameTable::effect::GetCcoloreffectTableInstance().getRecorder(10);
	else
		pEffectConfig = &GameTable::effect::GetCcoloreffectTableInstance().getRecorder(num);
	switch (color) {
	case 1:
		return pEffectConfig->red;
	case 2:
		return pEffectConfig->yellow;
	case 3:
		return pEffectConfig->blue;
	case 4:
		return pEffectConfig->green;
	default:
		break;
	}
	return 0;
}
int PopoEffect::getEffectWidth(sOffsetEffect& effect) {
	if (effect.d_pEffect == NULL)
		return 0;
	if (&effect == d_pPropertyEffect) {
		const GameTable::effect::CEffectConfig& conf = GameTable::effect::GetCEffectConfigTableInstance().getRecorder(d_effectid);
		if (conf.id == -1)
			return 70;
		return conf.width;
	}
	if (&effect == d_pAddEffect)
		return 15;
	else
		return 15;
	const Nuclear::NuclearRect& rect = effect.d_pEffect->GetRelBouningBox();

	return (int)(rect.right - rect.left);
}
void PopoEffect::numToEffects(int number, int color) {
	int i = 100;
	while (number > 0 && i > 0) {
		int t = number % 10;
		int effectid = getNumberEffectid(t, color);
		sOffsetEffect* effect = new sOffsetEffect(Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(effectid), false));
		d_numEffects.push_front(effect);
		number /= 10;
		i--;
	}
}
void PopoEffect::calEffectsOffset(long c_fx, long c_fy) {
	Nuclear::NuclearLocation orignalLoc = Nuclear::NuclearLocation(c_fx, c_fy);
	int propEffectWidth = getEffectWidth(*d_pPropertyEffect);
	int addEffectWidth = getEffectWidth(*d_pAddEffect);
	int totalWidth = propEffectWidth + addEffectWidth;
	for (NumEffects::iterator iter = d_numEffects.begin(); iter != d_numEffects.end(); iter++) {
		totalWidth += getEffectWidth(**iter);
	}
	int startpoint = -totalWidth / 2;
	Nuclear::NuclearLocation loc(orignalLoc);
	loc.x += startpoint;
	if (d_pPropertyEffect->d_pEffect)
		d_pPropertyEffect->d_pEffect->SetLocation(loc);
	startpoint += propEffectWidth;
	loc = orignalLoc;
	loc.x += startpoint;
	if (d_pAddEffect->d_pEffect)
		d_pAddEffect->d_pEffect->SetLocation(loc);
	startpoint += addEffectWidth;
	for (NumEffects::iterator iter = d_numEffects.begin(); iter != d_numEffects.end(); iter++) {
		loc = orignalLoc;
		loc.x += startpoint;
		if ((*iter)->d_pEffect)
			(*iter)->d_pEffect->SetLocation(loc);
		startpoint += getEffectWidth(**iter);
	}
}
void PopoEffect::playEffect(sOffsetEffect &effect, long c_fx, long c_fy) {
	if (effect.d_pEffect == NULL) {
		printf("warning effect not found\n");
		return;
	}
	Nuclear::GetEngine()->DrawEffect(effect.d_pEffect);
}

bool PopoEffect::IsStop(unsigned int now) {
	return now > d_endtime;
}
void PopoEffect::InitPopo(unsigned int now, float c_fx, float c_fy) {
	d_pPropertyEffect = new sOffsetEffect(Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(d_effectid), false));

	if (d_value >= 0)
	{
		d_pAddEffect = new sOffsetEffect(Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(getNumberEffectid(100, d_color)), false));
	}
	else
	{
		d_pAddEffect = new sOffsetEffect(Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(getNumberEffectid(200, d_color)), false));
	}

	d_numEffects.clear();
	numToEffects(abs(d_value), d_color);
	calEffectsOffset(c_fx, c_fy);

	d_endtime = now + 1000.0;
}
void PopoEffect::Show(unsigned int now, float c_fx, float c_fy) {

	for (NumEffects::iterator iter = d_numEffects.begin(); iter != d_numEffects.end(); iter++) {
		playEffect(**iter, c_fx, c_fy);
	}
	playEffect(*d_pPropertyEffect, c_fx, c_fy);
	playEffect(*d_pAddEffect, c_fx, c_fy);

}

CCharacterInfoManager::CCharacterInfoManager() :
d_AllowPopo(0)
{
	clear();
}
CCharacterInfoManager::~CCharacterInfoManager() {
	for (MsgMap::iterator iter = begin(); iter != end(); iter++) {
		PopoEffectQueue& strings = iter->second;
		if (strings.empty())
			continue;
		PopoEffectQueue& queue = iter->second;
		while (!queue.empty()) {
			PopoEffect* effect = queue.front();
			delete effect;
			queue.pop_front();
		}
	}
	while (!d_ShowString.empty()) {
		PopoEffect* string = d_ShowString.front();
		delete string;
		d_ShowString.pop_front();
	}
}

void Character::AddPopoMsg(int effectid, int value, int colorid, int order) {
	if (value < 0)
		return;
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	return;
#endif
	MsgMap::iterator iter = m_MsgManager.find(order);
	if (iter == m_MsgManager.end()) {
		PopoEffectQueue queue;
		m_MsgManager[order] = queue;
	}
	PopoEffectQueue& strings = m_MsgManager[order];

	strings.push_back(new PopoEffect(effectid, value, colorid));
}
void Character::AddPopoMsgReduce(int effectid, int value, int colorid, int order) {

#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	return;
#endif
	MsgMap::iterator iter = m_MsgManager.find(order);
	if (iter == m_MsgManager.end()) {
		PopoEffectQueue queue;
		m_MsgManager[order] = queue;
	}
	PopoEffectQueue& strings = m_MsgManager[order];

	strings.push_back(new PopoEffect(effectid, value, colorid));
}
void CCharacterInfoManager::OnUpdate(unsigned int now, float c_fx, float c_fy) {
	for (PopoEffectList::iterator iter = d_ShowString.begin(); iter != d_ShowString.end(); iter++) {
		(*iter)->Show(now, c_fx, c_fy);
	}
	while (!d_ShowString.empty()) {
		PopoEffect* string = d_ShowString.front();
		if (string->IsStop(now)) {
			d_ShowString.pop_front();
			delete string;
		}
		else break;
	}
	if (d_AllowPopo != 0 && now < d_AllowPopo) {
		return;
	}

	MsgMap::reverse_iterator iter = rbegin();
	bool bFind = false;
	for (; iter != rend(); iter++)
	{
		for (std::list<PopoEffect*>::iterator iterpop = iter->second.begin();
			iterpop != iter->second.end();
			iterpop++)
		{
			(*iterpop)->InitPopo(now, c_fx, c_fy);
			d_ShowString.push_back(*iterpop);
			iter->second.remove(*iterpop);
			bFind = true;
			break;
		}
		if (bFind)
		{
			break;
		}
	}

	d_AllowPopo = now + s_popoInterval;

}

Character::PICMAP Character::picpool;
Character::Character()
	: m_pTeamFollower(NULL)
	, m_pTeamLeaderEffect(NULL)
	, m_pInBattleEffect(NULL)
	, m_pWatchBattleEffect(NULL)
	, m_pFlowerEffect(NULL)
	, m_pInPlayCGEffect(NULL)
	, m_pFactionBattleEffect(NULL)
	, m_pTitleEffect(NULL)
	, m_TeamInfoOutofDate(false)
	, m_TitleTextureHandle(Nuclear::INVALID_TEXTBLOCK_HANDLE)
	, m_bGM(false)
	, m_pTitleTextBlock(NULL)
	, m_iFollowCheckTime(0)
	, m_pCacheCharacterAttribute(NULL)
	, m_dwTitleColor(0xffffffff)
	, m_bShowTeamNum(false)
	, m_iTeamNumHeight(0)
	, m_iTeamNumHeightHorse(0)
	, m_TeamNumTextureHandle(Nuclear::INVALID_TEXTBLOCK_HANDLE)
	, m_pTeamTextBlock(NULL)
{
	m_eSceneType = eSceneObjCharacter;
	SetNameColour(0xff00ff42);
	SetNameShadowColor(0xff011620);
	SetMouseOverNameColor(0xff36baff);
	SetMouseOverShadowColor(0xff020012);
}
Character::~Character()
{
	if (m_pTitleEffect)
	{
		RemoveDurativeEffect(m_pTitleEffect);
		m_pTitleEffect = NULL;
	}
	m_pTeamFollower = NULL;

	gGetScene()->ClearTeamFollowWhileCharacterDelete(this);

	if (m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_TitleTextureHandle);
		}
	}

	if (m_TeamNumTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_TeamNumTextureHandle);
		}
	}
	if (m_pCacheCharacterAttribute)
	{
		delete m_pCacheCharacterAttribute;
		m_pCacheCharacterAttribute = NULL;
	}

	m_pTitleTextBlock = NULL;
	m_pTeamTextBlock = NULL;
}

std::wstring Character::GetAutoMoveRideName(int AutoMovePathID, int AutoMovePathID2)
{
	const GameTable::move::CAutoMovePath& AMP = GameTable::move::GetCAutoMovePathTableInstance().getRecorder(AutoMovePathID);
	std::vector<std::wstring> delimiters;
	delimiters.push_back(L",");
	std::vector<std::wstring> wsPaths;
	StringCover::split_string(AMP.paths, delimiters, wsPaths);
	for (unsigned int i = 0; i < wsPaths.size(); i++)
	{
		int CurPathID = StringCover::WStrToNum<int>(wsPaths[i]);
		if (CurPathID == AutoMovePathID2)
		{
			const GameTable::move::CAutoMovePathPoint& AMPP = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(CurPathID);
			return AMPP.ridename;
		}
	}
	return L"";
}
float Character::GetAutoMoveSpeed(int AutoMovePathID, int AutoMovePathID2)
{
	const GameTable::move::CAutoMovePath& AMP = GameTable::move::GetCAutoMovePathTableInstance().getRecorder(AutoMovePathID);
	std::vector<std::wstring> delimiters;
	delimiters.push_back(L",");
	std::vector<std::wstring> wsPaths;
	StringCover::split_string(AMP.paths, delimiters, wsPaths);
	for (unsigned int i = 0; i < wsPaths.size(); i++)
	{
		int CurPathID = StringCover::WStrToNum<int>(wsPaths[i]);
		if (CurPathID == AutoMovePathID2)
		{
			const GameTable::move::CAutoMovePathPoint& AMPP = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(CurPathID);
			return AMPP.speed * 1.0f / 1000 * g_moveSpeed / 1000;
		}
	}
	return g_moveSpeed / 1000;
}
int Character::GetHeadEffectNum()
{
	int num = 0;
	//队长标志，战斗标志和观战标志只会存在一个，特产特效也是
	if (m_pTeamLeaderEffect || m_pWatchBattleEffect || m_pInBattleEffect)
	{
		num++;
	}
	//“梦-境”标志以及工会战标志只会存在一个
	if (m_pInPlayCGEffect || m_pFactionBattleEffect)
	{
		num++;
	}
	return num;
}
int Character::GetTitleID()
{
	return m_data.TitleID;
}
std::wstring Character::GetTitle()
{
#if OPEN_CHARACTER_TITLE_EFF
	if (m_data.strTitle.empty())
	{
		int titleid = GetTableNumberFromLua("title.ctitleconfig", m_data.TitleID, "id");
		if (titleid != -1)
			return GetTableWStringFromLua("title.ctitleconfig", m_data.TitleID, "titlename");
	}
#endif
	return m_data.strTitle;
}
std::wstring Character::GetTitleForClassCharacter()
{
	if (m_data.strTitle.empty())
	{
		int titleid = GetTableNumberFromLua("title.ctitleconfig", m_data.TitleID, "id");
		if (titleid != -1)
			return GetTableWStringFromLua("title.ctitleconfig", m_data.TitleID, "titlename");
	}
	return m_data.strTitle;
}
Character*	Character::GetTeamLeader()
{
	if (IsOnTeam())
	{
		return gGetScene()->FindCharacterByTeamIDandIndex(m_data.teamID, 1);
	}
	else
	{
		return NULL;
	}
}
int Character::GetTeamIndex()
{
	if (m_data.teamID == 0)
		return 0;
	else
		return m_data.teamindex;
}
eTeamMemberState Character::GetTeamState()
{
	if (m_data.teamID != 0)
		return eTeamMemberState(m_data.teamstate);
	else
		return eTeamMemberNULL;
}

void Character::SetShapeID(int shapeid)
{
	m_data.shape = shapeid;
	ChangeSpriteModel(shapeid);
}
void Character::SetAttribute(stCharacterData* pData)
{
	if (!m_pCacheCharacterAttribute && gGetGameApplication()->GetFirstTimeEnterGame() <= 3)
	{
		m_pCacheCharacterAttribute = new SCacheCharacterAttribute;
		m_pCacheCharacterAttribute->characterData = *pData;
		return;
	}

	m_data = *pData;
	SetID(m_data.dwID);
	SetLevel(m_data.level);
	UpdatNameTexture();

	if (NULL == GetSprite())
	{
		//优先用角色实际造型，而不是角色1-10那个初始造型
		if (pData->actuallyshape != 0)
		{
			NewSpriteModel(pData->actuallyshape, m_data.bhighlevel);
		}
		else
		{
			NewSpriteModel(pData->shape, m_data.bhighlevel);
		}
	}
	else
	{
		//优先用角色实际造型，而不是角色1-10那个初始造型
		if (pData->actuallyshape != 0)
		{
			ChangeSpriteModel(pData->actuallyshape);
		}
		else
		{
			ChangeSpriteModel(pData->shape);
		}

		SetInHighLevel(m_data.bhighlevel);
	}
	if (m_data.weaponbaseid != 0)
	{
		SetSpriteComponent(eSprite_Horse, m_data.weaponbaseid);
	}
	m_data.eSex = (eSexType)cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetRoleSex", m_data.shape % 10);
	//跨地图寻路时，经常断开就是因为这里主角SetAttribute重设了location
	if (this != GetMainCharacter())
	{
		if (GetAutoMove() == 1)
		{
		}
		else
		{
			SetLogicLocation(m_data.ptPos);
			SetDirection(m_data.dir);
		}
	}

	if (IsOnTeam() && !IsTeamLeader() && GetTeamState() == eTeamMemberNormal)
	{
		m_pTeamFollower = gGetScene()->FindCharacterByTeamIDandIndex(m_data.teamID, m_data.teamindex - 1);
		RunTeamFollow();
	}
	else
	{
		m_pTeamFollower = NULL;
		//不是主角的话才需要调用moveto
		if (this != GetMainCharacter())
		{
			if (m_data.ptTargetPos != m_data.ptPos && m_data.ptTargetPos != Nuclear::NuclearLocation(-1, -1))
				MoveTo(m_data.ptTargetPos);
			else
				PlayAction(eActionStand);
		}
	}
	//队长标志更新
	UpdateTeamLeaderEffect();

	InitCharacterState();

	SetTitle(pData->TitleID, pData->strTitle);

	if (GetSprite())
	{
		GetSprite()->SetUseWorldScale(1);
	}
}
void Character::SetTeamLeaderEffectVisible(bool b)
{
	if (m_pTeamLeaderEffect && !b)
	{
		RemoveDurativeEffect(m_pTeamLeaderEffect);
		m_pTeamLeaderEffect = NULL;
	}
	else if (!m_pTeamLeaderEffect && b)
	{
		UpdateTeamLeaderEffect();
	}
}
void Character::SetVisible(bool v)
{
	SceneObject::SetVisible(v);
}
void Character::SetDirection(int tx, int ty)
{
	if (GetSprite())
	{
		SceneObject::SetDirection(tx, ty);
	}
	else
	{
		if (m_pCacheCharacterAttribute)
		{
			//m_pCacheCharacterAttribute->pDir = new Nuclear::NuclearDirection(tx, ty);
		}
	}
}
void Character::SetDirection(Nuclear::NuclearDirection dir)
{
	if (GetSprite())
	{
		SceneObject::SetDirection(dir);
	}
	else
	{
		if (m_pCacheCharacterAttribute)
		{
			if (m_pCacheCharacterAttribute->pDir)
				delete m_pCacheCharacterAttribute->pDir;
			m_pCacheCharacterAttribute->pDir = new Nuclear::NuclearDirection(dir);
		}
	}
}
void Character::SetTeamInfo(int64_t teamid, int teamindex, int teamstate)
{
	m_data.teamID = teamid;
	m_data.teamindex = teamindex;
	m_data.teamstate = (eTeamMemberState)teamstate;

	if (GetMainCharacter())
	{
		if (teamid != 0 && teamid == GetMainCharacter()->GetTeamID())
		{
			if (GetSprite())
			{
				GetSprite()->SetUpdateEveryFrame(true);
			}
		}
	}
	if (IsOnTeam() && !IsTeamLeader() && m_data.teamstate == eTeamMemberNormal)
	{
		m_pTeamFollower = gGetScene()->FindCharacterByTeamIDandIndex(m_data.teamID, m_data.teamindex - 1);
		RunTeamFollow();

	}
	else
	{
		m_pTeamFollower = NULL;
	}
	UpdateTeamLeaderEffect();
	UpdateSpeed();
}
void Character::SetTeamFollow()
{
	if (IsOnTeam() && !IsTeamLeader() && m_data.teamstate == eTeamMemberNormal)
	{
		m_pTeamFollower = gGetScene()->FindCharacterByTeamIDandIndex(m_data.teamID, m_data.teamindex - 1);
		RunTeamFollow();
	}
	else
	{
		m_pTeamFollower = NULL;
	}
	UpdateTeamLeaderEffect();
	UpdateSpeed();
}

bool Character::IsSetCharacterState(eCharacterState state)
{
	return (m_data.characterstate & state) != 0x00;
}
bool Character::CanBeAttack()
{
	if (this == GetMainCharacter())
		return false;
	return true;
}
bool Character::CanBeGiven()
{
	return true;
}
bool Character::CanJoinTeam()
{
	if (IsOnTeam())
		return true;
	return false;
}
bool Character::CanTradeWith()
{
	return true;
}
bool Character::CanGiveFlower()
{
	return true;
}
bool Character::CanGiveRose()
{
	return GetSex() == eSexFemale;
}
bool Character::CanBeUseSkill(int skillid)
{
	if (this == GetMainCharacter())
	{
		return true;
	}
	return false;
}
bool Character::IsInChallengeArea()
{
	if (gGetScene())
	{
		return gGetScene()->IsInChallengeArea(GetLogicLocation());
	}
	else
		return false;
}

void Character::InitCharacterState()
{
	int estate = eCharacterStateInBattle;
	while (estate < eCharacterStateMax)
	{
		if ((m_data.characterstate & estate) != 0x00)
		{
			AddCharacterState(eCharacterState(estate));
		}
		estate = estate << 1;
	}
}
void Character::UpdateCharacterState(int state)
{
	int estate = eCharacterStateInBattle;
	while (estate < eCharacterStateMax)
	{
		//说明是添加了某个状态
		if ((m_data.characterstate & estate) == 0x00
			&& (state & estate) != 0x00)
		{
			AddCharacterState(eCharacterState(estate));
		}
		//说明是删除了某个状态
		else if ((m_data.characterstate & estate) != 0x00
			&& (state & estate) == 0x00)
		{
			RemoveCharacterState(eCharacterState(estate));
		}
		estate = estate << 1;
	}
	m_data.characterstate = state;
}
void Character::AddCharacterState(eCharacterState e)
{
	int effectnum = GetHeadEffectNum();
	switch (e)
	{
	case eCharacterStateGM: //GM
	{
		m_bGM = true;
	}
	break;
	case eCharacterStateInBattle:	//战斗状态
	{
		//如果是自己进战斗，就不用显示头顶图标了，反正自己也看不到自己
		if (GetMainCharacter() != this)
		{
			//需要停止移动，因为现在服务器不发stopmove的协议了
			StopMove();
			if (NULL == m_pInBattleEffect)
			{
				m_pInBattleEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10164), 0, 0, 0, false, false, false, true);
			}
			//进战斗时显示战斗标志，暂时关闭队长标志
			if (IsOnTeam() && IsTeamLeader() && m_pTeamLeaderEffect)
			{
				RemoveDurativeEffect(m_pTeamLeaderEffect);
				m_pTeamLeaderEffect = NULL;
			}
			else
			{
				if (m_pInPlayCGEffect)
				{
					GetSprite()->ChangeEffectOffset(m_pInPlayCGEffect, Nuclear::NuclearLocation(0, -EFFECT_OFFSETY));
				}
				if (m_pFactionBattleEffect)
				{
					GetSprite()->ChangeEffectOffset(m_pFactionBattleEffect, Nuclear::NuclearLocation(0, -EFFECT_OFFSETY));
				}
			}
		}
	}
	break;
	case eCharacterStateWatchBattle://观战状态
	{
		if (this != GetMainCharacter())
		{
			//需要停止移动，因为现在服务器不发stopmove的协议了
			StopMove();
		}
		if (NULL == m_pWatchBattleEffect)
			m_pWatchBattleEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10169), 0, 0, 0, false, false, false, true);
		//进战斗时显示战斗标志，暂时关闭队长标志
		if (IsOnTeam() && IsTeamLeader() && m_pTeamLeaderEffect)
		{
			RemoveDurativeEffect(m_pTeamLeaderEffect);
			m_pTeamLeaderEffect = NULL;
		}
		else
		{
			if (m_pInPlayCGEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pInPlayCGEffect, Nuclear::NuclearLocation(0, -EFFECT_OFFSETY));
			}
			if (m_pFactionBattleEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pFactionBattleEffect, Nuclear::NuclearLocation(0, -EFFECT_OFFSETY));
			}
		}
	}
	break;
	case eCharacterStatePlayCG://动画播放状态
	{
		if (this != GetMainCharacter())
		{
			//需要停止移动，因为现在服务器不发stopmove的协议了
			StopMove();
		}
		if (m_pInPlayCGEffect == NULL)
		{
			int effectalreadyhavenum = GetHeadEffectNum();
			m_pInPlayCGEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10259), 0, 0, -EFFECT_OFFSETY*effectalreadyhavenum, false, true, false, true);
		}
	}
	break;
	case eCharacterStateFB_ATTACK1://15:工会战高级攻城
	case eCharacterStateFB_ATTACK2://16:工会战中级攻城
	case eCharacterStateFB_ATTACK3://17:工会战初级攻城
	case eCharacterStateFB_REPAIR1://18:工会战高级修理
	case eCharacterStateFB_REPAIR2://19:工会战中级修理
	case eCharacterStateFB_REPAIR3://20:工会战初级修理
	{
		std::wstring effectpath = L"";
		if (e == eCharacterStateFB_REPAIR1)
			effectpath = MHSD_UTILS::GetEffectPath(10270);
		else if (e == eCharacterStateFB_REPAIR2)
			effectpath = MHSD_UTILS::GetEffectPath(10269);
		else if (e == eCharacterStateFB_REPAIR3)
			effectpath = MHSD_UTILS::GetEffectPath(10268);
		else if (e == eCharacterStateFB_ATTACK1)
			effectpath = MHSD_UTILS::GetEffectPath(10267);
		else if (e == eCharacterStateFB_ATTACK2)
			effectpath = MHSD_UTILS::GetEffectPath(10266);
		else if (e == eCharacterStateFB_ATTACK3)
			effectpath = MHSD_UTILS::GetEffectPath(10265);
		if (NULL == m_pFactionBattleEffect)
		{
			int effectalreadyhavenum = GetHeadEffectNum();
			m_pFactionBattleEffect = SetDurativeEffect(effectpath, 0, 0, -EFFECT_OFFSETY*effectalreadyhavenum, false, false, false, true);
		}
		else
		{
			RemoveDurativeEffect(m_pFactionBattleEffect);
			m_pFactionBattleEffect = NULL;
			int effectalreadyhavenum = GetHeadEffectNum();
			m_pFactionBattleEffect = SetDurativeEffect(effectpath, 0, 0, -EFFECT_OFFSETY*effectalreadyhavenum, false, false, false, true);
		}
	}
	break;
	default:
		break;
	}
	int effectnumnow = GetHeadEffectNum();
	if (effectnum != effectnumnow)
	{

	}
}
void Character::RemoveCharacterState(eCharacterState e)
{
	int effectnum = GetHeadEffectNum();
	switch (e)
	{
	case eCharacterStateInBattle:	//战斗状态
	{
		RemoveDurativeEffect(m_pInBattleEffect);
		m_pInBattleEffect = NULL;
		//重新添加队长特效
		if (IsOnTeam() && IsTeamLeader() && m_pTeamLeaderEffect == NULL)
		{
			m_pTeamLeaderEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10165), 0, 0, 0, true, true, false, true);
		}
		else
		{
			if (m_pInPlayCGEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pInPlayCGEffect, Nuclear::NuclearLocation(0, 0));
			}
			if (m_pFactionBattleEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pFactionBattleEffect, Nuclear::NuclearLocation(0, 0));
			}
		}
	}
	break;
	case eCharacterStateWatchBattle://观战状态
	{
		RemoveDurativeEffect(m_pWatchBattleEffect);
		m_pWatchBattleEffect = NULL;
		if (IsOnTeam() && IsTeamLeader() && m_pTeamLeaderEffect == NULL)
		{
			m_pTeamLeaderEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10165), 0, 0, 0, true, true, false, true);
		}
		else
		{
			if (m_pInPlayCGEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pInPlayCGEffect, Nuclear::NuclearLocation(0, 0));
			}
			if (m_pFactionBattleEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pFactionBattleEffect, Nuclear::NuclearLocation(0, 0));
			}
		}
	}
	break;
	case eCharacterStatePlayCG://动画播放状态
	{
		RemoveDurativeEffect(m_pInPlayCGEffect);
		m_pInPlayCGEffect = NULL;
	}
	break;
	case eCharacterStateFB_ATTACK1://15:工会战高级攻城
	case eCharacterStateFB_ATTACK2://16:工会战中级攻城
	case eCharacterStateFB_ATTACK3://17:工会战初级攻城
	case eCharacterStateFB_REPAIR1://18:工会战高级修理
	case eCharacterStateFB_REPAIR2://19:工会战中级修理
	case eCharacterStateFB_REPAIR3://20:工会战初级修理
	{
		if (m_pFactionBattleEffect)
		{
			RemoveDurativeEffect(m_pFactionBattleEffect);
			m_pFactionBattleEffect = NULL;
		}
	}
	break;
	default:
		break;
	}
}
void Character::Run(int now, int delta)
{
	if (m_pCacheCharacterAttribute && gGetGameApplication()->GetFirstTimeEnterGame() > 3)
	{
		SetAttribute(&m_pCacheCharacterAttribute->characterData);
		if (GetSprite())
		{
			if (m_pCacheCharacterAttribute->speed != 0.0f)
				GetSprite()->SetMoveSpeed(m_pCacheCharacterAttribute->speed);
			if (m_pCacheCharacterAttribute->pDefaultAct)
				SetDefaultAction(m_pCacheCharacterAttribute->pDefaultAct->actiontype, m_pCacheCharacterAttribute->pDefaultAct->holdlastframe, m_pCacheCharacterAttribute->pDefaultAct->freq);
			if (m_pCacheCharacterAttribute->pDefaultActWithOut)
				SceneObject::SetDefaultActionWithoutLogic(m_pCacheCharacterAttribute->pDefaultActWithOut->actiontype, m_pCacheCharacterAttribute->pDefaultActWithOut->holdlastframe, m_pCacheCharacterAttribute->pDefaultActWithOut->freq);
			if (m_pCacheCharacterAttribute->pComponent)
				ComponentsObject::SetSpriteComponent(m_pCacheCharacterAttribute->pComponent->type, m_pCacheCharacterAttribute->pComponent->baseid, m_pCacheCharacterAttribute->pComponent->forcechange);
			if (m_pCacheCharacterAttribute->pLocation)
				SceneObject::SetLocation(*(m_pCacheCharacterAttribute->pLocation));
			if (m_pCacheCharacterAttribute->pDir)
				SceneObject::SetDirection(*(m_pCacheCharacterAttribute->pDir));
		}
		SceneObject::ReloadNameTexture();
		delete m_pCacheCharacterAttribute;
		m_pCacheCharacterAttribute = NULL;
	}

	RunTeamFollow();

	SceneObject::Run(now, delta);
}
bool Character::PlayAction(eActionType actionType, float freq)
{
	if (ComponentsObject::PlayAction(actionType, freq))
	{
		return true;
	}
	return false;
}
bool Character::OnSetCursor()
{
	return SceneObject::OnSetCursor();
}
void Character::Draw(int now)
{
	Nuclear::World* world = static_cast<Nuclear::World*>(Nuclear::GetEngine()->GetWorld());
	float fWorldScale = world->GetScale();

	if (IsVisiable())
	{
		Nuclear::NuclearLocation loc = GetLocation();
		if (m_data.TitleID > 0 && IsVisiable())
		{
#if OPEN_CHARACTER_TITLE_EFF
			int titleid = GetTableNumberFromLua("title.ctitleconfig", m_data.TitleID, "id");
			if (titleid != -1)
			{
				DWORD shadowColor = 0xff000000;

				std::wstring titlename = GetTitleForClassCharacter();
				if (!titlename.empty())
				{
					Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();


					if (pFontMan&&m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE&&m_pTitleTextBlock)
					{

						m_pTitleTextBlock->SetFontColor(m_dwTitleColor, m_dwTitleColor);
						m_pTitleTextBlock->SetShadowColor(shadowColor);

						int height = m_pTitleTextBlock->GetTextHeight();
						int width = m_pTitleTextBlock->GetTextWidth();

						int left = loc.x * fWorldScale - gGetScene()->GetViewport().left * fWorldScale - width / 2;
						int top = loc.y * fWorldScale - gGetScene()->GetViewport().top * fWorldScale + DRAW_NAME_OFFSET + 24 * m_titleIndex++;

						pFontMan->DrawText(m_TitleTextureHandle, (float)left, (float)top, NULL);

					}
				}
			}
#endif
		}

		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan&&m_NameTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE&&m_pNameTextBlock != NULL)
		{
			DWORD colour = m_NameColour;
			DWORD shadowColor = m_NameShadowColor;
			m_pNameTextBlock->SetFontColor(colour, colour);

			m_pNameTextBlock->SetShadowColor(shadowColor);
			int height = m_pNameTextBlock->GetTextHeight();
			int width = m_pNameTextBlock->GetTextWidth();

			if (m_bGM)
			{
				std::wstring strGM(L"GM");
				std::map<std::wstring, Nuclear::PictureHandle>::iterator pospic = picpool.find(strGM);
				Nuclear::PictureHandle pic = Nuclear::INVALID_PICTURE_HANDLE;

				if (pospic != picpool.end())
				{
					pic = pospic->second;
				}
				else{
					//pic = Nuclear::GetEngine()->GetRenderer()->LoadPicture(L"/image/title/GM.tga");
					if (pic)
					{
						picpool.insert(make_pair(strGM, pic));
					}

				}

				Nuclear::NuclearPictureInfo picinfo;
				bool bgetinfo = Nuclear::GetEngine()->GetRenderer()->GetPictureInfo(pic, picinfo);
				if (pic && bgetinfo)
				{
					Nuclear::NuclearFRectt rct;
					rct.left = (float)(loc.x * fWorldScale - gGetScene()->GetViewport().left * fWorldScale - (picinfo.m_nPicWidth + width) / 2);
					rct.top = loc.y * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().top * fWorldScale + DRAW_NAME_OFFSET + 24 * m_titleIndex++;
					rct.bottom = (float)(rct.top + picinfo.m_nPicHeight);
					rct.right = (float)(rct.left + picinfo.m_nPicWidth);
					Nuclear::GetEngine()->GetRenderer()->DrawPicture(pic, rct, 0xFFFFFFFF);

					pFontMan->DrawText(m_NameTextureHandle, (float)rct.right, (float)rct.top, NULL);
				}
				else
				{
					int left = loc.x * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().left * fWorldScale - width / 2;
					int top = loc.y * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().top * fWorldScale + DRAW_NAME_OFFSET + 24 * m_titleIndex++;

					pFontMan->DrawText(m_NameTextureHandle, (float)left, (float)top, NULL);
				}

			}
			else
			{
				if (m_data.camp == 1 || m_data.camp == 2)
				{
					std::wstring strPic(L"");
					if (m_data.camp == 1)
					{
						strPic = L"campred";
					}
					else if (m_data.camp == 2)
					{
						strPic = L"campblue";
					}

					std::map<std::wstring, Nuclear::PictureHandle>::iterator pospic = picpool.find(strPic);
					Nuclear::PictureHandle pic;

					if (pospic != picpool.end())
					{
						pic = pospic->second;
					}
					else{
						pic = Nuclear::GetEngine()->GetRenderer()->LoadPicture(L"/image/role/" + strPic + L".tga");
						if (pic)
						{
							picpool.insert(make_pair(strPic, pic));
						}
					}

					Nuclear::NuclearPictureInfo picinfo;
					bool bgetinfo = Nuclear::GetEngine()->GetRenderer()->GetPictureInfo(pic, picinfo);
					if (pic && bgetinfo)
					{
						Nuclear::NuclearFRectt rct;
						rct.left = (float)(loc.x * fWorldScale - gGetScene()->GetViewport().left * fWorldScale - width / 2 - picinfo.m_nPicWidth);
						rct.top = loc.y * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().top * fWorldScale + DRAW_NAME_OFFSET + 24 * m_titleIndex++;
						rct.bottom = (float)(rct.top + picinfo.m_nPicHeight);
						rct.right = (float)(rct.left + picinfo.m_nPicWidth);
						Nuclear::GetEngine()->GetRenderer()->DrawPicture(pic, rct, 0xFFFFFFFF);

						pFontMan->DrawText(m_NameTextureHandle, (float)rct.right, (float)rct.top, NULL);
					}
					else
					{
						int left = loc.x * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().left * fWorldScale - width / 2;
						int top = loc.y * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().top * fWorldScale + DRAW_NAME_OFFSET + 24 * m_titleIndex++;

						pFontMan->DrawText(m_NameTextureHandle, (float)left, (float)top, NULL);
					}
				}
				else
				{
					int left = loc.x * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().left * fWorldScale - width / 2;
					int top = loc.y * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().top * fWorldScale + DRAW_NAME_OFFSET + 24 * m_titleIndex++;

					pFontMan->DrawText(m_NameTextureHandle, (float)left, (float)top, NULL);
				}

			}
		}

		if (m_bShowTeamNum && pFontMan&&m_TeamNumTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE&&m_pTeamTextBlock != NULL)
		{
			DWORD colour = 0xff00ff0c;
			DWORD shadowColor = m_NameShadowColor;
			m_pTeamTextBlock->SetFontColor(colour, colour);

			m_pTeamTextBlock->SetShadowColor(shadowColor);
			int height = m_pTeamTextBlock->GetTextHeight();
			int width = m_pTeamTextBlock->GetTextWidth();
			int detpos = m_iTeamNumHeight;
			if (GetRideModeID()!=0)
				detpos = m_iTeamNumHeightHorse;

			int left = loc.x * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().left * fWorldScale - width / 2;
			int top = loc.y * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().top * fWorldScale - detpos;

			pFontMan->DrawText(m_TeamNumTextureHandle, (float)left, (float)top, NULL);
		}

		m_titleIndex = 0;
	}
}
void Character::UpdateTeamLeaderEffect()
{
	int effectnum = GetHeadEffectNum();
	if (IsOnTeam() && IsTeamLeader() && !IsInBattle())
	{
		if (m_pTeamLeaderEffect == NULL)
		{
			m_pTeamLeaderEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10165), 0, 0, 0, true, true, false, true);
			if (m_pInPlayCGEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pInPlayCGEffect, Nuclear::NuclearLocation(0, -EFFECT_OFFSETY));
			}
			if (m_pFactionBattleEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pFactionBattleEffect, Nuclear::NuclearLocation(0, -EFFECT_OFFSETY));
			}
		}
	}
	else
	{
		if (m_pTeamLeaderEffect)
		{
			RemoveDurativeEffect(m_pTeamLeaderEffect);
			m_pTeamLeaderEffect = NULL;
			if (m_pInPlayCGEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pInPlayCGEffect, Nuclear::NuclearLocation(0, 0));
			}
			if (m_pFactionBattleEffect)
			{
				GetSprite()->ChangeEffectOffset(m_pFactionBattleEffect, Nuclear::NuclearLocation(0, 0));
			}
		}
	}
	int effectnumnow = GetHeadEffectNum();
	if (effectnum != effectnumnow)
	{
	}
}
void Character::OnCloseStall(bool isRemoveChar) //结束摆摊时造型换回来
{
	if (isRemoveChar)
	{
		return;
	}
	if (m_data.actuallyshape != 0)
	{
		ChangeSpriteModel(m_data.actuallyshape);
	}
	else
	{
		ChangeSpriteModel(m_data.shape);
	}

	if (gGetScene())
	{
		gGetScene()->AddNotTransparentManNum();
	}

	SetDirection(Nuclear::XPDIR_BOTTOM);
}
void Character::SetTitle(const int& id, const std::wstring &title)
{
	bool bTitleChange = (title != m_data.strTitle || id != m_data.TitleID) ? true : false;

	m_data.TitleID = id;
	m_data.strTitle = title;
	m_dwTitleColor = GetABGRColorByStringNew(GetTableWStringFromLua("title.ctitleconfig", id, "fontcolor"));
	if (bTitleChange
		|| (m_TitleTextureHandle == Nuclear::INVALID_TEXTBLOCK_HANDLE && id >= 0)
		|| (m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE && id < 0))
	{
		UpdataTitleTexture();
		if (m_pTitleEffect)
		{
			RemoveDurativeEffect(m_pTitleEffect);
			m_pTitleEffect = NULL;
		}
	}
}

void Character::SetName(const std::wstring &name)
{
	m_data.strName = name;
}

void Character::UpdataTitleTexture()
{
	if (m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_TitleTextureHandle);
		}
		m_TitleTextureHandle = Nuclear::INVALID_TEXTBLOCK_HANDLE;
	}
	m_pTitleTextBlock = NULL;

#if OPEN_CHARACTER_TITLE_EFF
	std::wstring strTitle = GetTitleForClassCharacter();
#else    
	std::wstring strTitle = GetTitle();
#endif
	if (!strTitle.empty())
	{

		DWORD color = 0xFFFFFFFF;
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			m_TitleTextureHandle = pFontMan->NewText(strTitle.c_str(), 0, color, color);

			m_pTitleTextBlock = pFontMan->GetTextBlock(m_TitleTextureHandle);
		}
	}
}
void Character::SetInHighLevel(bool bhighlevel)
{
	if (m_data.bhighlevel != bhighlevel)
	{
		m_data.bhighlevel = bhighlevel;
	}

	SceneObject::SetInHighLevel(bhighlevel);
}
void Character::InitPosition(Nuclear::NuclearLocation loc, bool bhighlevel)
{
	SceneObject::StopMove();

	SetLogicLocation(loc);
	SetInHighLevel(bhighlevel);

	m_data.ptPos = loc;
	m_data.ptTargetPos = loc;
}
void Character::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	SceneObject::UpdateAction(type);
	if (type == Nuclear::XPUSAT_MOVEINFLEXION)
	{
		astar::Path path;
		GetPath(path);
		RunTeamFollow();
	}
}
void Character::RunTeamFollow()
{
	if (m_pTeamFollower != NULL)
	{
		Nuclear::NuclearLocation ptFollow = m_pTeamFollower->GetLogicLocation();
		int LogicDistance = Nuclear::distance(GetLogicLocation(), ptFollow);

		if (LogicDistance >= MaxFollowDistance)
		{
			if (IsMoving() == false || m_iFollowCheckTime < 0)
			{
				MoveTo(ptFollow);
				if (GetMainCharacter() == this)
				{
					GetMainCharacter()->DealWhileMainCharacterMove();
					GetMainCharacter()->EventMainCharacterMove.Bingo();
				}
				m_iFollowCheckTime = 30;
			}
			else
			{
				m_iFollowCheckTime--;
			}
		}
		else if (LogicDistance <= MinFollowDistance && IsMoving() == true)
		{
			StopMove();
		}
	}
}
bool Character::MoveTo(Nuclear::NuclearLocation ptMoveTarget)
{
	m_data.ptTargetPos = ptMoveTarget;
	return SceneObject::MoveTo(m_data.ptTargetPos, m_data.automovepathid > 0);
}
void Character::StopMove()
{
	if (!IsMoving())
		return;
	if (m_pTeamFollower && m_pTeamFollower->IsMoving())
	{
		Nuclear::NuclearLocation ptFollow = m_pTeamFollower->GetLogicLocation();
		int LogicDistance = Nuclear::distance(GetLogicLocation(), ptFollow);

		if (LogicDistance >= MaxFollowDistance)
		{
			MoveTo(ptFollow);
			if (GetMainCharacter() == this)
			{
				GetMainCharacter()->DealWhileMainCharacterMove();
				GetMainCharacter()->EventMainCharacterMove.Bingo();
			}
			return;
		}
	}
	SceneObject::StopMove();
}
void Character::SetDefaultAction(eActionType actiontype, bool holdlastframe, float freq)
{
	if (GetSprite())
	{
		if (actiontype == eActionStand)
		{
			switch (GetDefaultAction())
			{
			case 0:
			{
				if (GetSprite()) GetSprite()->SetDefaultAction(GetActionName(eActionStand), Nuclear::XPSALT_ASYNC, freq, holdlastframe);
			}
			break;
			default:
			{
				if (GetSprite()) GetSprite()->SetDefaultAction(GetActionName(eActionStand), Nuclear::XPSALT_ASYNC, freq, holdlastframe);
			}
			break;
			}
		}
		else
		{
			SceneObject::SetDefaultAction(actiontype, holdlastframe, freq);
		}
	}
	else
	{
		if (m_pCacheCharacterAttribute)
		{
			if (m_pCacheCharacterAttribute->pDefaultAct)
				delete m_pCacheCharacterAttribute->pDefaultAct;
			m_pCacheCharacterAttribute->pDefaultAct = new SCacheDefaultAction(actiontype, holdlastframe, freq);
		}
	}
}
void Character::ChangeSpriteModel(const int& modelid)
{
	ComponentsObject::ChangeSpriteModel(modelid);

	SetDefaultAction(eActionStand);//这里必须重设DefaultAction
}
void Character::UpdatNameTexture(bool bReleaseOld)
{
	SceneObject::UpdatNameTexture(bReleaseOld);

	if (bReleaseOld)
	{
		LOGD("UpdatNameTexture release old");
		if (m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
		{
			UpdataTitleTexture();
		}
	}
	else if (m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		LOGD("UpdatNameTexture not release old");
		return;
	}
}
void Character::DrawMsgEffect(int now)
{
	Nuclear::NuclearRect r = gGetScene()->GetViewport();
	m_MsgManager.OnUpdate(now, r.Width()*0.5, r.Height()*0.5);
}

int Character::GetRideModeID()
{
	int Result = GetSpriteComponent(eSprite_Horse);
	return Result;
}

int Character::GetRideSpeedByModelID(int iModelID)
{
	return cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetRideSpeedByModelID", iModelID);
}

int Character::GetOffsetYStep(int WayID, int PathID1, int PointID1)
{
	int Result = 0;
	int PathID2 = PathID1;
	int PointID2 = PointID1 + 1;

	const GameTable::move::CAutoMovePathPoint& AMPP1 = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(PathID1);
	if (AMPP1.id == -1)
	{
		return 0;
	}
	std::vector<std::wstring> delimiters;
	delimiters.push_back(L",");
	std::wstring wsPoint1 = AMPP1.points[PointID1];
	std::vector<std::wstring> wsPoints1;
	StringCover::split_string(wsPoint1, delimiters, wsPoints1);
	int PointX1 = StringCover::WStrToNum<int>(wsPoints1[0]) * 16;
	int PointY1 = StringCover::WStrToNum<int>(wsPoints1[1]) * 16;
	int PointZ1 = StringCover::WStrToNum<int>(wsPoints1[2]);
	Nuclear::NuclearLocation PointXY1(PointX1, PointY1);

	if (PointID1 == AMPP1.pointcount - 1)
	{
		PointID2 = 0;
		const GameTable::move::CAutoMovePath& AMP = GameTable::move::GetCAutoMovePathTableInstance().getRecorder(WayID);
		if (AMP.id == -1)
		{
			return 0;
		}
		std::vector<std::wstring> wsPaths;
		StringCover::split_string(AMP.paths, delimiters, wsPaths);
		for (int i = 0; i < wsPaths.size(); i++)
		{
			int CurPathID = StringCover::WStrToNum<int>(wsPaths[i]);
			if (PathID1 == CurPathID)
			{
				if (i < wsPaths.size() - 1)
				{
					int NextPathID = StringCover::WStrToNum<int>(wsPaths[i + 1]);
					PathID2 = NextPathID;
					const GameTable::move::CAutoMovePathPoint& AMPPTmp = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(PathID2);
					if (AMPPTmp.sceneid != AMPP1.sceneid)
					{
						return 0;
					}
				}
				else
				{
					return 0;
				}
				break;
			}
		}
	}

	const GameTable::move::CAutoMovePathPoint& AMPP2 = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(PathID2);
	std::wstring wsPoint2 = AMPP2.points[PointID2];
	std::vector<std::wstring> wsPoints2;
	StringCover::split_string(wsPoint2, delimiters, wsPoints2);
	int PointX2 = StringCover::WStrToNum<int>(wsPoints2[0]) * 16;
	int PointY2 = StringCover::WStrToNum<int>(wsPoints2[1]) * 16;
	int PointZ2 = StringCover::WStrToNum<int>(wsPoints2[2]);
	Nuclear::NuclearLocation PointXY2(PointX2, PointY2);

	float fLength = sqrtf(PointXY2.Dis2(PointXY1));
	float fSpeed = GetAutoMoveSpeed(WayID, PathID2) * 1000;
	float fTime = fLength / fSpeed;
	Result = (PointZ2 - PointZ1) / fTime;
	return Result;
}

int Character::GetOffsetYCur(int WayID, int PathID1, int PointID1)
{
	int Result = 0;

	const GameTable::move::CAutoMovePathPoint& AMPP1 = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(PathID1);
	if (AMPP1.id == -1)
	{
		return 0;
	}
	std::vector<std::wstring> delimiters;
	delimiters.push_back(L",");
	std::wstring wsPoint1 = AMPP1.points[PointID1];
	std::vector<std::wstring> wsPoints1;
	StringCover::split_string(wsPoint1, delimiters, wsPoints1);
	int PointZ1 = StringCover::WStrToNum<int>(wsPoints1[2]);
	Result = PointZ1;
	return Result;
}

int Character::GetOffsetYNext(int WayID, int PathID1, int PointID1)
{
	int Result = 0;
	int PathID2 = PathID1;
	int PointID2 = PointID1 + 1;

	const GameTable::move::CAutoMovePathPoint& AMPP1 = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(PathID1);
	if (AMPP1.id == -1)
	{
		return 0;
	}
	std::vector<std::wstring> delimiters;
	delimiters.push_back(L",");
	std::wstring wsPoint1 = AMPP1.points[PointID1];
	std::vector<std::wstring> wsPoints1;
	StringCover::split_string(wsPoint1, delimiters, wsPoints1);
	int PointZ1 = StringCover::WStrToNum<int>(wsPoints1[2]);

	if (PointID1 == AMPP1.pointcount - 1)
	{
		PointID2 = 0;
		const GameTable::move::CAutoMovePath& AMP = GameTable::move::GetCAutoMovePathTableInstance().getRecorder(WayID);
		if (AMP.id == -1)
		{
			return 0;
		}
		std::vector<std::wstring> wsPaths;
		StringCover::split_string(AMP.paths, delimiters, wsPaths);
		for (int i = 0; i < wsPaths.size(); i++)
		{
			int CurPathID = StringCover::WStrToNum<int>(wsPaths[i]);
			if (PathID1 == CurPathID)
			{
				if (i < wsPaths.size() - 1)
				{
					int NextPathID = StringCover::WStrToNum<int>(wsPaths[i + 1]);
					PathID2 = NextPathID;
					const GameTable::move::CAutoMovePathPoint& AMPPTmp = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(PathID2);
					if (AMPPTmp.sceneid != AMPP1.sceneid)
					{
						return PointZ1;
					}
				}
				else
				{
					return 0;
				}
				break;
			}
		}
	}

	const GameTable::move::CAutoMovePathPoint& AMPP2 = GameTable::move::GetCAutoMovePathPointTableInstance().getRecorder(PathID2);
	std::wstring wsPoint2 = AMPP2.points[PointID2];
	std::vector<std::wstring> wsPoints2;
	StringCover::split_string(wsPoint2, delimiters, wsPoints2);
	int PointZ2 = StringCover::WStrToNum<int>(wsPoints2[2]);
	Result = PointZ2;
	return Result;
}

void Character::SetFlyScaleTgt(int iFlyScaleTgt)
{
	GetSprite()->SetFlyScaleTgt(iFlyScaleTgt);
}
void Character::SetFlyScaleCur(int iFlyScaleCur)
{
	GetSprite()->SetFlyScaleCur(iFlyScaleCur);
}
int Character::GetFlyScaleCur()
{
	return GetSprite()->GetFlyScaleCur();
}
void Character::SetFlyScaleStep(int iFlyScaleStep)
{
	GetSprite()->SetFlyScaleStep(iFlyScaleStep);
}
int Character::GetFlyScaleStep()
{
	return GetSprite()->GetFlyScaleStep();
}
void Character::SetFlyOffsetYTgt(int iFlyOffsetYTgt)
{
	GetSprite()->SetFlyOffsetYTgt(iFlyOffsetYTgt);
}
void Character::SetFlyOffsetYCur(int iFlyOffsetYCur)
{
	GetSprite()->SetFlyOffsetYCur(iFlyOffsetYCur);
}
int Character::GetFlyOffsetYCur()
{
	return GetSprite()->GetFlyOffsetYCur();
}
void Character::SetFlyOffsetYStep(int iFlyOffsetYStep)
{
	GetSprite()->SetFlyOffsetYStep(iFlyOffsetYStep);
}
int Character::GetFlyOffsetYStep()
{
	return GetSprite()->GetFlyOffsetYStep();
}

void Character::UpdateSpeed()
{
	int RideModeID = GetRideModeID();
	int CurSpeed = GetRideSpeedByModelID(GetRideModeID());
	if (IsOnTeam())
	{
		if (!IsTeamLeader())
		{
			if (GetTeamState() == eTeamMemberNormal)
			{
				Character* pLeader = GetTeamLeader();
				if (pLeader)
				{
					CurSpeed = pLeader->GetRideSpeedByModelID(pLeader->GetRideModeID());
				}
			}
		}
	}

	if (GetSprite())
	{
		GetSprite()->SetMoveSpeed(CurSpeed * 1.0f / 1000 * g_moveSpeed / 1000);
	}
}

void Character::SetMoveSpeed(float speed)
{
	if (GetSprite())
	{
		GetSprite()->SetMoveSpeed(speed);
	}
	else
	{
		if (m_pCacheCharacterAttribute)
		{
			m_pCacheCharacterAttribute->speed = speed;
		}
	}
}

void Character::SetDefaultActionWithoutLogic(eActionType actiontype, bool holdlastframe, float freq)
{
	if (GetSprite())
	{
		SceneObject::SetDefaultActionWithoutLogic(actiontype, holdlastframe, freq);
	}
	else
	{
		if (m_pCacheCharacterAttribute)
		{
			if (m_pCacheCharacterAttribute->pDefaultActWithOut)
				delete m_pCacheCharacterAttribute->pDefaultActWithOut;
			m_pCacheCharacterAttribute->pDefaultActWithOut = new SCacheDefaultAction(actiontype, holdlastframe, freq);
		}
	}
}

void Character::SetSpriteComponent(eSpriteComponentType type, int baseid, bool forcechange)
{
	if (GetSprite())
	{
		ComponentsObject::SetSpriteComponent(type, baseid, forcechange);
	}
	else
	{
		if (m_pCacheCharacterAttribute)
		{
			if (m_pCacheCharacterAttribute->pComponent)
				delete m_pCacheCharacterAttribute->pComponent;
			m_pCacheCharacterAttribute->pComponent = new SCacheComponent(type, baseid, forcechange);
		}
	}
}

void Character::SetLocation(const Nuclear::NuclearLocation& location)
{
	if (GetSprite())
	{
		SceneObject::SetLocation(location);
	}
	else
	{
		if (m_pCacheCharacterAttribute)
		{
			if (m_pCacheCharacterAttribute->pLocation)
				delete m_pCacheCharacterAttribute->pLocation;
			m_pCacheCharacterAttribute->pLocation = new Nuclear::NuclearLocation(location);
		}
	}
}

void Character::SetTeamNumVisible(bool b)
{
	m_bShowTeamNum = b;
}

void Character::SetTeamNumHeight(int height, int horseheight)
{
	m_iTeamNumHeight = height;
	m_iTeamNumHeightHorse = horseheight;
}

void Character::SetTeamNum(std::wstring numstr)
{
	if (m_TeamNumTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_TeamNumTextureHandle);
		}
		m_TeamNumTextureHandle = Nuclear::INVALID_TEXTBLOCK_HANDLE;
	}
	m_pTeamTextBlock = NULL;

	if (!numstr.empty())
	{
		DWORD color = 0xFFFFFFFF;
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			m_TeamNumTextureHandle = pFontMan->NewText(numstr.c_str(), 0, color, color);

			m_pTeamTextBlock = pFontMan->GetTextBlock(m_TeamNumTextureHandle);
		}
	}
}