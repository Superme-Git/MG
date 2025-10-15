#include "stdafx.h"
#include "DroptItem.h"
#include "GameScene.h"

DroptItem::DroptItem()
{
	m_eSceneType = eSceneObjItem;
}

DroptItem::~DroptItem()
{
}

bool DroptItem::SetAttribute(const fire::pb::move::PickUpItem& data)
{
	m_data = data;
	stNPCConfig config;
	m_BaseData = config; //目前先用thisID找，以后再换baseID；
	m_BaseData.id = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "id");
	m_BaseData.bodytype = GetTableFloatFromLua("npc.cnpcconfig", data.baseid, "bodytype");
	m_BaseData.npctype = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "npctype");
	m_BaseData.modelID = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "modelID");
	m_BaseData.name = GetTableWStringFromLua("npc.cnpcconfig", data.baseid, "name");
	m_BaseData.foottitle = GetTableWStringFromLua("npc.cnpcconfig", data.baseid, "foottitle");
	m_BaseData.headtitle = GetTableWStringFromLua("npc.cnpcconfig", data.baseid, "headtitle");
	m_BaseData.title = GetTableWStringFromLua("npc.cnpcconfig", data.baseid, "title");
	m_BaseData.area1colour = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "area1colour");
	m_BaseData.area2colour = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "area2colour");
	m_BaseData.mapid = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "mapid");
	m_BaseData.xPos = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "xPos");
	m_BaseData.yPos = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "yPos");
	m_BaseData.hide = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "hide");
	m_BaseData.ndir = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "ndir");
	m_BaseData.nstate = GetTableNumberFromLua("npc.cnpcconfig", data.baseid, "nstate");

	if(m_BaseData.id == -1)
		return false;

	SetID(m_data.uniqueid);

 	if ( NULL == GetSprite())	//设置角色造型
 		NewSpriteModel(m_BaseData.modelID);
 
	if(NULL == GetSprite())
		return false;

	int ShapeConfig_dir = GetTableNumberFromLua("npc.cnpcshape", m_BaseData.modelID, "dir");
	if (ShapeConfig_dir == 4)
	{
		if (rand()%2 == 0)
		{
			SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
		}
		else
		{
			SetDirection(Nuclear::XPDIR_BOTTOMLEFT);
		}
	}
	else
	{
		SetDirection(Nuclear::XPDIR_BOTTOM);
	}
	SetLogicLocation(Nuclear::NuclearLocation(m_data.pos.x,m_data.pos.y));
	
	return true;
}

bool DroptItem::OnSetCursor()
{
	if (SceneObject::OnSetCursor())
		return true;
	return false;
}
