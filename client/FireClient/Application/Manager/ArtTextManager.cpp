#include "stdafx.h"
#include "ArtTextManager.h"
#include "SceneObject.h"
#include "Battler.h"
#include "engine/nuengine.h"
#include "effect/nueffectmanager.h"
#include "GameTable/common/CCommon.h"
#include "GameTable/npc/CMonsterConfig.h"

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "Utils2.h"
#endif

const Nuclear::NuclearFRectt RECT_ROUNDDI(Nuclear::NuclearFPoint(315, 20), Nuclear::NuclearFPoint(347, 60));
const Nuclear::NuclearFRectt RECT_ROUNDNUM(Nuclear::NuclearFPoint(345, 20), Nuclear::NuclearFPoint(382, 55));
const Nuclear::NuclearFRectt RECT_ROUNDHUIHE(Nuclear::NuclearFPoint(382, 20), Nuclear::NuclearFPoint(447, 58));

namespace
{
	const int diWidth = RECT_ROUNDDI.Width();
	const int diHeight = RECT_ROUNDDI.Height();

	const int numWidth = RECT_ROUNDNUM.Width();
	const int numHeight = RECT_ROUNDNUM.Height();

	const int huiheWidth = RECT_ROUNDHUIHE.Width();
	const int huiheHeight = RECT_ROUNDHUIHE.Height();
}

const Nuclear::NuclearFRectt RECT_NUM1(Nuclear::NuclearFPoint(-40, 100), Nuclear::NuclearFPoint(0, 140));
const Nuclear::NuclearFRectt RECT_NUM2(Nuclear::NuclearFPoint(0, 100), Nuclear::NuclearFPoint(40, 140));
const Nuclear::NuclearFRectt WAIT_MIDDLE(Nuclear::NuclearFPoint(-20, 100), Nuclear::NuclearFPoint(20, 145));

const Nuclear::NuclearFRectt BOAT_STATE(Nuclear::NuclearFPoint(380, 119), Nuclear::NuclearFPoint(502, 162));

const Nuclear::NuclearFRectt RECT_MINUTE1(Nuclear::NuclearFPoint(410, 130), Nuclear::NuclearFPoint(430, 150));
const Nuclear::NuclearFRectt RECT_MINUTE2(Nuclear::NuclearFPoint(430, 130), Nuclear::NuclearFPoint(450, 150));
const Nuclear::NuclearFRectt RECT_COLON(Nuclear::NuclearFPoint(450, 130), Nuclear::NuclearFPoint(470, 150));//冒号
const Nuclear::NuclearFRectt RECT_SECOND1(Nuclear::NuclearFPoint(470, 130), Nuclear::NuclearFPoint(490, 150));
const Nuclear::NuclearFRectt RECT_SECOND2(Nuclear::NuclearFPoint(490, 130), Nuclear::NuclearFPoint(510, 150));

const std::wstring HpMpPath[eHPMPMAX] =
{
	L"addhp/",
	L"countsubhp/",
	L"addmp/",
	L"submp/",
	L"baojiaddhp/",
	L"baojisubhp/",
	L"addexp/",
	L"countsubhp/",
};
int HpMpWidth[eHPMPMAX] =
{
	20,
	20,
	20,
	20,
	25,
	25,
	30,
	20,
};

void NumberEffectNotify::OnEnd(Nuclear::IEffect *pEffect)
{
	if (pEffect)
	{
		pEffect->RemoveNotify(this);
		m_pArtText->RemoveEffect(pEffect);
		delete this;
	}
}

void NumberEffectNotify::OnDelete(Nuclear::IEffect *pEffect)
{
	if (pEffect)
	{
		pEffect->RemoveNotify(this);
		m_pArtText->RemoveEffect(pEffect);
		delete this;
	}
}

CArtText::CArtText()
{
	m_PlayTime = 0;
	m_DelayTime = 0;
}
CArtText::~CArtText()
{
	for (std::list<EffectNode>::iterator it = m_listEffect.begin(), ie = m_listEffect.end(); it != ie; ++it)
	{
		it->m_pEffect->RemoveNotify(it->m_pNotify);
		delete it->m_pNotify;
	}
	m_listEffect.clear();
}
void CArtText::SetOwner(SceneObject* pOwner)
{
	m_pOwner = pOwner;
}
SceneObject* CArtText::GetOwner()
{
	return m_pOwner;
}
void CArtText::SetDelayTime(int iDelay)
{
	m_DelayTime = iDelay;
}
int CArtText::GetDelayTime()
{
	return m_DelayTime;
}
void CArtText::AddHPMPNumber(enumHPMPChangeType eType, int iNum, int iFlagType, bool bClearAll)
{
	NumberData ND;
	ND.m_eType = eType;
	ND.m_iNum = iNum;
	ND.m_iFlagType = iFlagType;
	if (bClearAll)
	{
		m_listNumberData.clear();
	}
	m_listNumberData.push_back(ND);
}
void CArtText::MakeNewText(NumberData& ND)
{
	std::vector<NumberTextOne> NumberText;
	int iNumber = abs(ND.m_iNum);

	while (iNumber > 0)
	{
		NumberTextOne NTO;
		NTO.m_EffectFileName = L"geffect/number/" + HpMpPath[ND.m_eType] + StringCover::intTowstring(iNumber % 10);
		iNumber /= 10;
		NumberText.push_back(NTO);
	}

	int iNumberCount = NumberText.size();
	//if (ND.m_eType == eExpAdd)
	//{
	//	iNumberCount = 11;
	//}
	int iNumberWidth = iNumberCount * HpMpWidth[ND.m_eType];

	int iNumberX = iNumberWidth / 2;

	float ModelScale = 1.0f;
	if (((Battler*)m_pOwner)->GetBattlerData().BattlerType == eBattlerMonster 
		|| ((Battler*)m_pOwner)->GetBattlerData().BattlerType == eBattlerNPC
		|| ((Battler*)m_pOwner)->GetBattlerData().BattlerType == eBattlerNaKaMa)
	{
		const GameTable::npc::CMonsterConfig& MonsterData = GameTable::npc::GetCMonsterConfigTableInstance().getRecorder(static_cast<int>(((Battler*)m_pOwner)->GetBattlerData().BattlerDataID));
		if (MonsterData.id != -1)
		{
			ModelScale = MonsterData.bodytype;
		}
	}
	int iNumberY = m_pOwner->GetSprite()->GetBubbleItemOffset() == 0 ? 105 : m_pOwner->GetSprite()->GetBubbleItemOffset();
	iNumberY += 36;
	iNumberY = iNumberY * ModelScale;
	iNumberY -= 22;

	if (ND.m_eType == eHPAddBang || ND.m_eType == eHPSubBang)
	{
		//iNumberWidth += 64;
		//iNumberX = iNumberWidth / 2;
		iNumberY -= 6;
		std::wstring strEffect = L"";
		if (ND.m_iFlagType == 1)
		{
			strEffect = L"geffect/number/baoji";
		}
		else if (ND.m_iFlagType == 2)
		{
			strEffect = L"geffect/number/baoji2";
		}
		else if (ND.m_iFlagType == 3)
		{
			strEffect = L"geffect/number/baoji3";
		}
		if (((Battler*)m_pOwner)->IsFriendSide())
		{
			Nuclear::IEffect* pEffect = m_pOwner->PlayEffect(strEffect, 0/*(iNumberX - HpMpWidth[ND.m_eType] / 2) - HpMpWidth[ND.m_eType] * iNumberCount - 20*/, -iNumberY + 35, 1, false, true, false, 1);
			AddEffect(pEffect);
		}
		else
		{
			Nuclear::IEffect* pEffect = m_pOwner->PlayEffect(strEffect, 0/*(iNumberX - HpMpWidth[ND.m_eType] / 2) - HpMpWidth[ND.m_eType] * iNumberCount - 20*/, -iNumberY + 35, 1, false, true, false, 1);
			AddEffect(pEffect);
		}
	}
	std::wstring strPlayEffect = L"";
	switch (ND.m_eType)
	{
	case eFanZhen:	//反震
		strPlayEffect = MHSD_UTILS::GetEffectPath(11052);
		break;
	case eFangYu:	//防御特效
		strPlayEffect = MHSD_UTILS::GetEffectPath(11015);
		break;
	}
	if (strPlayEffect != L"")
	{
		Nuclear::IEffect* pEffect = m_pOwner->PlayEffect(strPlayEffect);
		AddEffect(pEffect);
	}
	for (int i = 0; i < iNumberCount; i++)
	{
		Nuclear::IEffect* pEffect = m_pOwner->PlayEffect(NumberText[i].m_EffectFileName, (iNumberX - HpMpWidth[ND.m_eType] / 2) - HpMpWidth[ND.m_eType] * i, -iNumberY, 1, false, true, false, 1);//(iNumberCount - i) * HpMpWidth[ND.m_eType] - iNumberX
		AddEffect(pEffect);
	}
}
void CArtText::AddEffect(Nuclear::IEffect* pEffect)
{
	if (pEffect == NULL)	return;
	NumberEffectNotify* pNotify = new NumberEffectNotify();
	pNotify->m_pArtText = this;
	pEffect->AddNotify(pNotify);
	EffectNode enode;
	enode.m_pEffect = pEffect;
	enode.m_pNotify = pNotify;
	m_listEffect.push_back(enode);
}
void CArtText::RemoveEffect(Nuclear::IEffect* pEffect)
{
	for (std::list<EffectNode>::iterator it = m_listEffect.begin(), ie = m_listEffect.end(); it != ie; ++it)
	{
		if(it->m_pEffect == pEffect)
		{
			it = m_listEffect.erase(it);
			break;
		}
	}
}
void CArtText::Run(int delta)
{
	if (m_PlayTime >= m_DelayTime)
	{
		m_PlayTime = 0;
	}
	if (m_PlayTime == 0)
	{
		if (m_listNumberData.size() > 0)
		{
			for (std::list<EffectNode>::iterator it = m_listEffect.begin(), ie = m_listEffect.end(); it != ie; ++it)
			{
				it->m_pEffect->RemoveNotify(it->m_pNotify);
				it->m_pEffect->Stop();
				delete it->m_pNotify;
			}
			m_listEffect.clear();

			MakeNewText(m_listNumberData.front());
			m_listNumberData.pop_front();
			m_PlayTime = 1;
			return;
		}
	}
	if (m_PlayTime > 0)
	{
		m_PlayTime += delta;
	}
}

void CArtText::InitArtText()
{
	Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());
	for (int i = 0; i <= 9; i++)
	{
		pEngine->GetEffectManager()->CreateEffect(L"geffect/number/" + HpMpPath[eHPCount] + StringCover::intTowstring(i), false, true);
		pEngine->GetEffectManager()->CreateEffect(L"geffect/number/" + HpMpPath[eHPCounhtSub] + StringCover::intTowstring(i), false, true);
	}
	for (int i = 0; i < eHPMPMAX; i++)
	{
		HpMpWidth[i] = StringCover::WStrToNum<float>(GameTable::common::GetCCommonTableInstance().getRecorder(190 + i).value);
	}
}
