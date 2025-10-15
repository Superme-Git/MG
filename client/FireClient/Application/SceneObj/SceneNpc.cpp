#include "stdafx.h"
#include "SceneNpc.h"
#include "GameScene.h"
#include "GameApplication.h"
#include "GameTable/message/CMessageTip.h"
#include "SceneMovieManager.h"
#include <time.h>

#define GATHER_DISTANCE 12

SceneNpc::SceneNpc()
	: m_iTalkInterval(-1)
	, m_bTalkNearby(false)
	, m_bTalkCharacterGather(false)
	, m_iTalkCaseTime(3000)
	, m_iAppearTime(0)
	, m_iDisappearTime(0)
{
	m_SceneNpcBase.name = L"";
	m_eSceneType = eSceneObjClientNpc;
}
SceneNpc::~SceneNpc()
{

}

std::wstring SceneNpc::GetName()
{
	return m_SceneNpcBase.name;
}

void SceneNpc::CharacterGatherTalkRun()
{
	if (m_SceneNpcConfig.talkcharnum != -1)
	{
		//如果还未说话
		if (!m_bTalkCharacterGather)
		{
			if (gGetScene()->GetNearByCharacterNum(GetLogicLocation(), GATHER_DISTANCE*g_logicGrid) >= m_SceneNpcConfig.talkcharnum)
			{
				m_bTalkCharacterGather = true;
				randomTalk(m_TalkCharacterGatherList);
			}
		}
		else
		{
			if (gGetScene()->GetNearByCharacterNum(GetLogicLocation(), GATHER_DISTANCE*g_logicGrid) < m_SceneNpcConfig.talkcharnum)
			{
				m_bTalkCharacterGather = false;
			}
		}
	}
}

void SceneNpc::NearbyTalkRun()
{
	if (m_SceneNpcConfig.talkdistance != -1)
	{
		//如果还未说话
		if (!m_bTalkNearby)
		{
			if (Nuclear::distance(GetLogicLocation(), GetMainCharacter()->GetLogicLocation()) <= m_SceneNpcConfig.talkdistance*g_logicGrid)
			{
				m_bTalkNearby = true;
				randomTalk(m_TalkNearbyList);
			}
		}
		else
		{
			if (Nuclear::distance(GetLogicLocation(), GetMainCharacter()->GetLogicLocation()) > m_SceneNpcConfig.talkdistance*g_logicGrid)
			{
				m_bTalkNearby = false;
			}
		}
	}
}
void SceneNpc::IntervalTalkRun(int delta)
{
	if (m_SceneNpcConfig.talkinterval != -1)
	{
		m_iTalkInterval -= delta;
		if (m_iTalkInterval <= 0)
		{
			m_iTalkInterval = m_SceneNpcConfig.talkinterval;
			//2次间隔触发一次吧
			if (rand() % 2)
				randomTalk(m_TalkIntervalList);
		}
	}
}
void SceneNpc::randomTalk(std::vector<int>& talklist)
{
	if (!talklist.empty())
	{
		size_t num = talklist.size();
		size_t talkindex = rand() % num;
		const GameTable::message::CMessageTip& tips = GameTable::message::GetCMessageTipTableInstance().getRecorder(talklist[talkindex]);
		if (tips.id != -1)
		{
			AddTalk(tips.msg);
		}
	}
}
void SceneNpc::SetArea1Color(int colorID)
{
	if (GetSprite())
	{
		DWORD color = GetNpcColorByColorID(GetSprite()->GetModelName(), colorID);
		SetComponent(g_npcAreaColorID, g_npcAreaColorComponentName, color);
	}
}
void SceneNpc::Run(int now, int delta)
{
	SceneObject::Run(now, delta);
	IntervalTalkRun(delta);

	//区域说话判断
	m_iTalkCaseTime -= delta;
	if (m_iTalkCaseTime <= 0)
	{
		m_iTalkCaseTime = 3000;
		NearbyTalkRun();
		CharacterGatherTalkRun();
	}
}
void SceneNpc::Draw(int now)
{
	if (IsVisiable() == false)//如果隐藏，不画名字
		return;
	SceneObject::Draw(now);
}
bool SceneNpc::OnSetCursor()
{
	if (SceneObject::OnSetCursor())
		return true;

	return false;
}
bool SceneNpc::CanBeVisited()
{
	return !m_SceneNpcBase.chatidlist.empty();
}
void SceneNpc::RandomWalk()
{
	int xRandom = (rand() % 9 - 4)*g_logicGrid;
	int yRandom = (rand() % 9 - 4)*g_logicGrid;
	Nuclear::NuclearLocation randomtarget = m_ptInitLocation + Nuclear::NuclearLocation(xRandom, yRandom);
	if (!gGetScene()->IsMoveBlock(randomtarget, GetMoveMask()))
	{
		MoveTo(randomtarget);
	}
}
bool SceneNpc::SetAttribute(int npcid)
{
	m_SceneNpcID = npcid;

	m_SceneNpcConfig = GameTable::npc::GetCSceneNPCConfigTableInstance().getRecorder(npcid);

	if (m_SceneNpcConfig.talkinterval != -1)
	{
		m_iTalkInterval = m_SceneNpcConfig.talkinterval;
		m_TalkIntervalList.clear();
		std::wstring talklist = m_SceneNpcConfig.talkintervalsays;
		std::vector<std::wstring>	delimiters;
		delimiters.push_back(L";");
		std::vector<std::wstring>	outputstrings;
		StringCover::split_string(talklist, delimiters, outputstrings);
		for (std::vector<std::wstring>::iterator it = outputstrings.begin(); it != outputstrings.end(); it++)
		{
			m_TalkIntervalList.push_back(StringCover::StrToNum<int>(StringCover::to_string(*it)));
		}
	}
	if (m_SceneNpcConfig.talkdistance != -1)
	{
		m_TalkNearbyList.clear();
		std::wstring talklist = m_SceneNpcConfig.talkdistancesays;
		std::vector<std::wstring>	delimiters;
		delimiters.push_back(L";");
		std::vector<std::wstring>	outputstrings;
		StringCover::split_string(talklist, delimiters, outputstrings);
		for (std::vector<std::wstring>::iterator it = outputstrings.begin(); it != outputstrings.end(); it++)
		{
			m_TalkNearbyList.push_back(StringCover::StrToNum<int>(StringCover::to_string(*it)));
		}
	}
	if (m_SceneNpcConfig.talkcharnum != -1)
	{
		m_TalkCharacterGatherList.clear();
		std::wstring talklist = m_SceneNpcConfig.talkcharnumsays;
		std::vector<std::wstring>	delimiters;
		delimiters.push_back(L";");
		std::vector<std::wstring>	outputstrings;
		StringCover::split_string(talklist, delimiters, outputstrings);
		for (std::vector<std::wstring>::iterator it = outputstrings.begin(); it != outputstrings.end(); it++)
		{
			m_TalkCharacterGatherList.push_back(StringCover::StrToNum<int>(StringCover::to_string(*it)));
		}
	}

	if (m_SceneNpcConfig.id != -1)
	{
		m_SceneNpcBase = GameTable::npc::GetCSceneNPCBaseTableInstance().getRecorder(m_SceneNpcConfig.baseid);

		UpdatNameTexture();

		if (NULL == GetSprite())	//设置角色造型
		{
			int npcShape_dir = GetTableNumberFromLua("npc.cnpcshape", m_SceneNpcBase.shapeid, "dir");
			NewSpriteModel(m_SceneNpcBase.shapeid, m_SceneNpcConfig.mask != 0);
			if (npcShape_dir == 4)
			{
				GetSprite()->SetTurnDir(4);
				if (m_SceneNpcConfig.dir != 0)
				{
					SetDirection((Nuclear::NuclearDirection)m_SceneNpcConfig.dir);
				}
				else
				{
					if (rand() % 2 == 0)
						SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
					else
						SetDirection(Nuclear::XPDIR_BOTTOMLEFT);
				}
			}
			else if (npcShape_dir == 2)
			{
				GetSprite()->SetTurnDir(2);
				if (m_SceneNpcConfig.dir != 0)
				{
					if (m_SceneNpcConfig.dir <= 4)
						SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
					else
						SetDirection(Nuclear::XPDIR_TOPLEFT);
				}
				else
				{
					if (rand() % 2 == 0)
						SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
					else
						SetDirection(Nuclear::XPDIR_TOPLEFT);
				}
			}
			else
			{
				SetDirection(Nuclear::XPDIR_BOTTOM);
			}

			if (GetSprite())
			{
				GetSprite()->SetSolidMask(m_SceneNpcConfig.transparent == 1);
				GetSprite()->SetDyePartIndex(0, m_SceneNpcBase.area1colour);
			}
			if (m_SceneNpcBase.type == 52)
			{
				SetHighlightEnable(false);
			}
			//Npc缩放
			switch (m_SceneNpcBase.scale)
			{
			case 1:
				SetScale(c_fPetScale0);
				break;
			case 2:
				SetScale(c_fPetScale1);
				break;
			case 3:
				SetScale(c_fPetScale2);
				break;
			case 4:
				SetScale(c_fPetScale3);
				break;
			case 5:
				SetScale(c_fPetScale4);
				break;
			default:
				SetScale(c_fPetScale4);
				break;
			}

			m_ptInitLocation = Nuclear::NuclearLocation(m_SceneNpcConfig.posx, m_SceneNpcConfig.posy);
			SetLogicLocation(m_ptInitLocation);

			if (GetSprite() && m_SceneNpcConfig.movespeed != 0)
			{
				GetSprite()->SetMoveSpeed(m_SceneNpcConfig.movespeed / 1000.0f);
			}

			if (m_SceneNpcBase.npcappear != L"")
			{
				int year, month, day, hour, minite, second;
#ifdef ANDROID
				std::string str = SHARE_Wstring2String(m_SceneNpcBase.npcappear);
				sscanf(str.c_str(),"%d-%d-%d %d:%d:%d",&year,&month,&day,&hour,&minite,&second);
#else
				std::swscanf(m_SceneNpcBase.npcappear.c_str(), L"%d-%d-%d %d:%d:%d", &year, &month, &day, &hour, &minite, &second);
#endif
				tm time;
				time.tm_year = year - 1900;
				time.tm_mon = month - 1;
				time.tm_mday = day;
				time.tm_hour = hour;
				time.tm_min = minite;
				time.tm_sec = second;
				time.tm_isdst = 0;
				m_iAppearTime = 1000 * (int64_t)mktime(&time);

				if (gGetServerTime() >= m_iAppearTime)
				{
					SetAlpha(255);
				}
				else
				{
					SetAlpha(0);
				}
			}
			if (m_SceneNpcBase.npcdisappear != L"")
			{
				int year, month, day, hour, minite, second;
#ifdef ANDROID
				std::string str = SHARE_Wstring2String(m_SceneNpcBase.npcdisappear);
				sscanf(str.c_str(),"%d-%d-%d %d:%d:%d",&year,&month,&day,&hour,&minite,&second);
#else
				std::swscanf(m_SceneNpcBase.npcdisappear.c_str(), L"%d-%d-%d %d:%d:%d", &year, &month, &day, &hour, &minite, &second);
#endif
				tm time;
				time.tm_year = year - 1900;
				time.tm_mon = month - 1;
				time.tm_mday = day;
				time.tm_hour = hour;
				time.tm_min = minite;
				time.tm_sec = second;
				time.tm_isdst = 0;
				m_iDisappearTime = 1000 * (int64_t)mktime(&time);
				if (gGetServerTime() > m_iDisappearTime)
				{
					SetAlpha(0);
				}
			}
		}
		return true;
	}
	else
		return false;
}
void SceneNpc::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	if (type == Nuclear::XPUSAT_MOVEINFLEXION)
	{
		if (m_SceneNpcConfig.pacing != 0)
		{
			if (rand() % 4 == 0)
			{
				RandomWalk();
			}
		}
	}
	else if (type == Nuclear::XPUSAT_PLAYACTIONFINISH)
	{
		if (m_actionType == eActionStandRandom)
		{
			if (m_SceneNpcConfig.pacing != 0)
			{
				if (rand() % 2 == 0)
				{
					RandomWalk();
				}
			}
		}
	}
	SceneObject::UpdateAction(type);
}

//////////////////////////////////////////////////////////////////////////

SceneNpcManager::SceneNpcManager()
{

}
SceneNpcManager::~SceneNpcManager()
{
	ClearSceneNpc();
}

SceneObject* SceneNpcManager::FindSceneObjectBySprite(Nuclear::ISprite * sprite)
{
	for (std::map<int, SceneNpc*>::iterator it = m_SceneNpcMap.begin(); it != m_SceneNpcMap.end(); it++)
	{
		if (it->second && it->second->GetSprite() == sprite)
		{
			return it->second;
		}
	}
	return NULL;
}
SceneNpc* SceneNpcManager::FindSceneNpcByID(int64_t npcid)
{
	std::map<int, SceneNpc*>::iterator it = m_SceneNpcMap.find(static_cast<int>(npcid));
	if (it != m_SceneNpcMap.end())
	{
		return it->second;
	}
	else
	{
		return NULL;
	}
}
void SceneNpcManager::AddSceneNpc(int npcid)
{
	if (m_SceneNpcMap.find(npcid) == m_SceneNpcMap.end())
	{
		SceneNpc* pSceneNpc = new SceneNpc;
		pSceneNpc->SetAttribute(npcid);
		m_SceneNpcMap[npcid] = pSceneNpc;
		if (gGetSceneMovieManager() && gGetSceneMovieManager()->isOnSceneMovie())
		{
			pSceneNpc->SetVisible(false);
		}
	}
}
void SceneNpcManager::RemoveSceneNpc(int npcid)
{
	std::map<int, SceneNpc*>::iterator it = m_SceneNpcMap.find(npcid);
	if (it != m_SceneNpcMap.end())
	{
		delete it->second;
		it->second = NULL;
		m_SceneNpcMap.erase(it);
	}
}

void SceneNpcManager::InitSceneNpc(int mapid)
{
	m_MapSceneNpcIDList.clear();
	std::vector<int> scenenpclist;
	GameTable::npc::GetCSceneNPCConfigTableInstance().getAllID(scenenpclist);

	for (std::vector<int>::iterator it = scenenpclist.begin(); it != scenenpclist.end(); it++)
	{
		const GameTable::npc::CSceneNPCConfig& scenenpc = GameTable::npc::GetCSceneNPCConfigTableInstance().getRecorder(*it);
		if (scenenpc.id != -1 && scenenpc.mapid == mapid)
		{
			m_MapSceneNpcIDList.push_back(*it);
		}
	}
}
void SceneNpcManager::Draw(int now)
{
	for (std::map<int, SceneNpc*>::iterator it = m_SceneNpcMap.begin(); it != m_SceneNpcMap.end(); it++)
	{
		it->second->Draw(now);
	}
}
void SceneNpcManager::Run(int now, int delta)
{
	for (std::map<int, SceneNpc*>::iterator it = m_SceneNpcMap.begin(); it != m_SceneNpcMap.end(); it++)
	{
		it->second->Run(now, delta);
	}
}
void SceneNpcManager::ClearSceneNpc()
{
	m_MapSceneNpcIDList.clear();
	for (std::map<int, SceneNpc*>::iterator it = m_SceneNpcMap.begin(); it != m_SceneNpcMap.end(); it++)
	{
		delete it->second;
		it->second = NULL;
	}
	m_SceneNpcMap.clear();
}
void SceneNpcManager::HideOrShowSceneNpc(bool bVis)
{
	for (std::map<int, SceneNpc*>::iterator it = m_SceneNpcMap.begin(); it != m_SceneNpcMap.end(); it++)
	{
		SceneNpc* pNpc = it->second;
		if (pNpc)
		{
			pNpc->SetVisible(bVis);
		}
	}

}
void SceneNpcManager::UpdateSceneNpc(const Nuclear::NuclearLocation& maincharLoc)
{
	for (std::list<int>::iterator it = m_MapSceneNpcIDList.begin(); it != m_MapSceneNpcIDList.end(); it++)
	{
		const GameTable::npc::CSceneNPCConfig& scenenpc = GameTable::npc::GetCSceneNPCConfigTableInstance().getRecorder(*it);
		if (scenenpc.id != -1)
		{	//如果不在m_SceneNpcMap内并且距离主角在一屏幕以内，则添加
			if (m_SceneNpcMap.find(scenenpc.id) == m_SceneNpcMap.end()
				&& gNpcInScreen(Nuclear::NuclearLocation(scenenpc.posx, scenenpc.posy), maincharLoc))
			{
				AddSceneNpc(*it);
			}
			//如果在m_SceneNpcMap内并且距离主角在一屏幕以外，则删除
			else if (m_SceneNpcMap.find(scenenpc.id) != m_SceneNpcMap.end()
				&& !gNpcInScreen(Nuclear::NuclearLocation(scenenpc.posx, scenenpc.posy), maincharLoc))
			{
				RemoveSceneNpc(*it);
			}
		}
	}
}

