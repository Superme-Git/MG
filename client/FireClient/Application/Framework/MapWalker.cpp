#include "stdafx.h"
#include "MapWalker.h"
#include "MainCharacter.h"
#include "GameScene.h"
#include "MainRoleDataManager.h"
#include "GameApplication.h"
#include "nuiengine.h"
#include "Common/nufileiomanager.h"
#include "../../../../dependencies/LJXML/Include/LJXML.hpp"
#include "GameUIManager.h"

MapWalker::MapWalker()
: m_xPos(-1)
, m_yPos(-1)
, m_targetMapID(0)
, m_bOnAstar(false)
, m_iThroughChefu(0)
, m_targetNpcID(0)
, m_pCurrentMapTarget(Nuclear::NuclearLocation(-1,-1))
, m_iFactionTargetMapID(0)
, m_bStopThenPace(false)
, m_bLinkFindPath(false)
{
	BuildMapPath();
}

bool MapWalker::CheckTarget(const int& x, const int &y, const int& mapid)
{
	std::multimap<int, Nuclear::NuclearLocation>::const_iterator it = m_MapJumpNode.lower_bound(mapid);
	std::multimap<int, Nuclear::NuclearLocation>::const_iterator itend = m_MapJumpNode.upper_bound(mapid);

	while (it != itend)
	{
		//如果是一个跳转点
		if (abs(it->second.x - x / g_logicGrid) < s_JumpToS && abs(it->second.y - y / g_logicGrid) < s_JumpToS)
		{
			return true;
		}
		++it;
	}
	return false;
}

Nuclear::NuclearLocation MapWalker::GetTranspotLoc()
{
	int index;
	index = gGetScene()->GetMapID() * 10000 + m_targetMapID;
	Nuclear::NuclearLocation loc = m_MapTransport[index];

	loc.x *= g_logicGrid;
	loc.y *= g_logicGrid;

	return loc;
}

void MapWalker::BuildMapPath()
{
	return;//不需要读取跳转点信息，不在xml文件中，现在是填在表格中。
	std::wstring xmlpath = L"/config/paths.xml";
	char* fileBuf = NULL;
	std::streamsize ss = 0;
	if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
	{
		return;
	}

	std::wstring fileData;
	LJXMLStringHelper::EncodeLJ codeType;
	if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
	{
		delete[] fileBuf;
		return;
	}

	delete[] fileBuf;

	LJXML::LJXML_Doc<LJXML::Char> doc;
	LJXML::Char* charData = doc.LoadFromString(fileData);
	if (!doc.first_node())
	{
		return;
	}
	LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
	LJXML::LJXML_NodeList typenl;
	root->GetSubNodeList(typenl);
	for (size_t i = 0; i < typenl.size(); i++)
	{
		int sourceMapID = _wtoi(typenl[i]->first_attribute(L"sourceMapID")->value());
		int destMapID = _wtoi(typenl[i]->first_attribute(L"destMapID")->value());
		int xPos = _wtoi(typenl[i]->first_attribute(L"xPos")->value());
		int yPos = _wtoi(typenl[i]->first_attribute(L"yPos")->value());
		m_MapTransport[sourceMapID * 10000 + destMapID] = Nuclear::NuclearLocation(xPos, yPos);
		m_MapJumpNode.insert(std::make_pair(sourceMapID, Nuclear::NuclearLocation(xPos, yPos)));
	}
}

Nuclear::NuclearLocation MapWalker::GetFactionMapTranspotLoc()
{
	int index;
	index = gGetScene()->GetMapID() * 10000 + 1007;
	Nuclear::NuclearLocation loc = m_MapTransport[index];

	loc.x *= g_logicGrid;
	loc.y *= g_logicGrid;

	return loc;
}

//清除跨地图寻路信息
void MapWalker::ClearGoTo(bool bfinishgoto, bool clearTreasureInfo)
{
	if (bfinishgoto)
	{
		RunPacing();
		if (gGetGameUIManager()->getCurrentItemId() != -1)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("Taskuseitemdialog.getInstanceAndLoadData", gGetGameUIManager()->getCurrentItemId(), GetMainCharacter()->GetCurItemkey());
		}
	}
	if (!clearTreasureInfo)
	{
		gGetGameUIManager()->setCurrentItemId(-1);
		gGetGameUIManager()->setTreasureMapId(-1);
	}
	GetMainCharacter()->RemoveAutoWalkingEffect();
	m_iThroughChefu = 0;
	m_MapWalkerType = eMapWalkerType_Normal;
	m_iFactionTargetMapID = 0;
	m_xPos = -1;
	m_yPos = -1;
	m_targetMapID = 0;
	m_targetNpcID = 0;
	m_bOnAstar = false;
	m_pCurrentMapTarget = Nuclear::NuclearLocation(-1, -1);
}

bool MapWalker::GetAStarState()
{
	return m_bOnAstar;
}

void MapWalker::SetAStarState(bool bOn)
{
	m_bOnAstar = bOn;
}

int MapWalker::GetTargetMapID()
{
	return m_targetMapID;
}

eMapWalkerType MapWalker::GetMapWalkerType()
{
	return m_MapWalkerType;
}

bool MapWalker::IsThroughChefu()
{
	return m_iThroughChefu == 1;
}

bool MapWalker::IsThroughLinanTrans()
{
	return m_iThroughChefu == 2;
}

bool MapWalker::IsThroughFamilyManager()
{
	return m_iThroughChefu == 3;
}

Nuclear::NuclearLocation MapWalker::GetTargetPos()
{
	return Nuclear::NuclearLocation(m_xPos, m_yPos);
}

Nuclear::NuclearLocation MapWalker::GetCurrentMapTarget()
{
	return m_pCurrentMapTarget;
}

int MapWalker::GetFactionTargetMapID()
{
	return m_iFactionTargetMapID;
}

//寻路后开始踱步
void MapWalker::RunPacing()
{
	if (m_bStopThenPace)
	{
		//这里填踱步的代码
		GetMainCharacter()->SetRandomPacing();
		m_bStopThenPace = false;
	}
}

bool MapWalker::CanGoTo(const int& mapid)
{
	if (gGetScene()->GetMapID() == mapid)
		return true;
	int index = gGetScene()->GetMapID() * 10000 + mapid;
	return m_MapTransport.count(index) > 0;
}

void MapWalker::SetTarget(int x,int y,int mapId,int npcID, int chefu,bool returnteam,bool blinkfindpath,int64_t npckey)
{
	m_bLinkFindPath = blinkfindpath;
	//判断是否能行走
	if (!GetMainCharacter()->CanActiveMoveTo())
	{
		return;
	}
	//alt+R情况下点击NPC超链接，则先清除踱步状态
	if (npcID != 0 && GetMainCharacter()->IsReadyToPacing())
	{
		GetMainCharacter()->ClearReadyToPacing();
	}
	
	m_bStopThenPace = false;
	

	//如果已经是自动寻路归队，并且发起一次新的自动寻路归队
	if (returnteam && eMapWalkerType_ReturnTeam == m_MapWalkerType)
	{
		//自己跟队长并非一张地图
		if (gGetScene()->GetMapID() != m_targetMapID)
		{
			//且队长地图没改变，则不用重新寻路
			if (mapId == m_targetMapID)
				return;
		}
		//如果已经是一张地图，并且已经靠近了，就发送归队消息
		else if (Nuclear::distance(Nuclear::NuclearLocation(x,y),GetMainCharacter()->GetLogicLocation())  <= 64
			  && gGetScene()->IsTheSameArea(Nuclear::NuclearLocation(x,y),GetMainCharacter()->GetLogicLocation(),GetMainCharacter()->IsInHighLevel()))
		{
			Character* pTeamLeader = GetMainCharacter()->GetTeamLeader();
			if (pTeamLeader)
			{
				if (pTeamLeader->IsInHighLevel() == GetMainCharacter()->IsInHighLevel())
				{
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("TeamManager.SendAbsentReturnTeam", 0);
					return;
				}
				else
				{
                    if (GetTipsManager())
                        GetTipsManager()->AddMsgTips(143867);
					return;
				}
			}
		}
	}

	if (!CanGoTo(mapId))
	{
		ClearGoTo();
		return;
	}
	
	GetMainCharacter()->AddAutoWalkingEffect();
	//如果是寻至跳转点
	if (returnteam)
	{
		m_MapWalkerType = eMapWalkerType_ReturnTeam;
	}
	else if (CheckTarget(x,y,mapId))
	{
		m_MapWalkerType = eMapWalkerType_JumpPoint;
	}
	//如果是寻至访问NPC
	else if (npcID != 0 )
	{
		m_MapWalkerType = eMapWalkerType_VisiteNpc;
	}
	//普通寻路
	else
	{
		m_MapWalkerType = eMapWalkerType_Normal;
	}

	m_iThroughChefu = 0;
	
	//如果是访问NPC，则需要重新计算随机位置
	if (npcID != 0)
	{
		int nParam1 = 0;
		int nParam2 = 0;
		int nParam3 = 0;
		int nParam4 = 0;
		int randomradius = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("getVisitNpcDis", nParam1, nParam2, nParam3, nParam4);

		Nuclear::NuclearLocation randomlocation =	gGetScene()->GetNearByPosPreferY(Nuclear::NuclearLocation(x,y),randomradius);

		m_xPos = x;
		m_yPos = y;
	}
	else
	{
		m_xPos = x;
		m_yPos = y;
	}
	m_targetNpcID = npcID;
    m_targetNpcKey = npckey;
	m_targetMapID = mapId;
	
	m_bOnAstar = true;
	CrossGoTo();
}

void MapWalker::CrossGoTo()
{
	if (!m_bOnAstar)
	{
		return;
	}

	//已经到最后的地图了
	if ( gGetScene()->GetMapID() == m_targetMapID && m_iThroughChefu == 0)//if in same map 
	{
		//超链接寻路至某个孤岛，则需要给提示
		if (m_bLinkFindPath)
			GetMainCharacter()->SetNeedToIslandTipsFlag(true);
		
		GetMainCharacter()->ActiveMoveTo(Nuclear::NuclearLocation(m_xPos,m_yPos),m_targetNpcID, false,m_targetNpcKey);
		GetMainCharacter()->SetNeedToIslandTipsFlag(false);
		m_pCurrentMapTarget = Nuclear::NuclearLocation(m_xPos,m_yPos);
	}
	else
	{
		if (m_iThroughChefu == 1)
		{
			if (gGetScene()->IsInMySchoolMap())
			{
				Nuclear::NuclearLocation loc;
				int chefuid = gGetScene()->GetMySchoolChefuPosition(loc);
				if (chefuid != 0)
				{
					GetMainCharacter()->ActiveMoveTo(loc,chefuid);
					m_pCurrentMapTarget = loc;
				}
			}
			//如果是在内景地图，则先寻路到外景地图
			else if (gGetScene()->IsInMySchoolInsideMap())
			{
				Nuclear::NuclearLocation transpotloc = GetTranspotLoc();
				GetMainCharacter()->ActiveMoveTo(transpotloc);
				m_pCurrentMapTarget = transpotloc;
			}
			else if (gGetScene()->GetMapID() == m_targetMapID)
			{
				m_iThroughChefu = 0;
				GetMainCharacter()->ActiveMoveTo(Nuclear::NuclearLocation(m_xPos,m_yPos),m_targetNpcID);
				m_pCurrentMapTarget = Nuclear::NuclearLocation(m_xPos,m_yPos);
			}
			else
			{
				Nuclear::NuclearLocation transpotloc = GetTranspotLoc();
				GetMainCharacter()->ActiveMoveTo(transpotloc);
				m_pCurrentMapTarget = transpotloc;
				m_iThroughChefu = 0;
			}
		}
		else if (m_iThroughChefu == 2)
		{
			if (gGetScene()->IsInLinanMap())
			{
				Nuclear::NuclearLocation loc;
				int linanchefu = gGetScene()->GetLinanChefuPosition(loc);
				if (linanchefu != 0)
				{
					GetMainCharacter()->ActiveMoveTo(loc,linanchefu);
					m_pCurrentMapTarget = loc;
				}
			}
			else if (gGetScene()->IsInLinanNeijingMap())
			{
				Nuclear::NuclearLocation transpotloc = GetTranspotLoc();
				GetMainCharacter()->ActiveMoveTo(transpotloc);
				m_pCurrentMapTarget = transpotloc;
			}
			else
			{
				Nuclear::NuclearLocation transpotloc = GetTranspotLoc();
				GetMainCharacter()->ActiveMoveTo(transpotloc);
				m_pCurrentMapTarget = transpotloc;
				m_iThroughChefu = 0;
			}
		}
		else if (m_iThroughChefu == 3)
		{
			if (gGetScene()->IsInLinanMap())
			{
				Nuclear::NuclearLocation loc;
				int linanchefu = gGetScene()->GetFamilyManagerPosition(loc);
				if (linanchefu != 0)
				{
					GetMainCharacter()->ActiveMoveTo(loc,linanchefu);
					m_pCurrentMapTarget = loc;
				}
			}
			else
			{
				Nuclear::NuclearLocation transpotloc = GetTranspotLoc();
				GetMainCharacter()->ActiveMoveTo(transpotloc);
				m_pCurrentMapTarget = transpotloc;
			}
		}
		else
		{
			Nuclear::NuclearLocation transpotloc = GetTranspotLoc();
			GetMainCharacter()->ActiveMoveTo(transpotloc);
			m_pCurrentMapTarget = transpotloc;
			if (m_iFactionTargetMapID == 0)
			{
				m_iThroughChefu = 0;
			}
		}
	}
}

