#ifndef __ROLEITEMMANAGER_H
#define __ROLEITEMMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "Singleton.hpp"
#include "../Event.h"
#include "../../../../dependencies/cegui/CEGUI/include/elements/CEGUIItemTable.h"
#include "../ProtoDef/rpcgen/fire/pb/item/BagTypes.hpp"
#include "../ProtoDef/rpcgen/fire/pb/item/IDType.hpp"
#include "baseobject.h"
#include "notify.h"
#include "../Framework/ITickTimer.h"

#define COALBIND_BASEID		36031
#define COAL_BASEID			36189

class RoleItem;

class ItemIDParameter
{
public:
	int itemkey;
	int itembaseid;
	int bagid;

	ItemIDParameter();

	ItemIDParameter(int key, int id, int bagtype);
};

class ItemNumChangeNotify : virtual public fire::UnCopyObject
{
	friend class RoleItemManager;
public:
	ItemNumChangeNotify();
	virtual ~ItemNumChangeNotify();

protected:
	virtual void OnItemNumChangeNotify(const ItemIDParameter &) = 0;
};

class LuaItemNumChangeNotify : public ItemNumChangeNotify {
	std::vector<int> m_handlers;
public:
	virtual ~LuaItemNumChangeNotify();
	int InsertItemNumChangeNotify(int handler);
	void RemoveItemNumChangeNotify(int handler);
protected:
	virtual void OnItemNumChangeNotify(const ItemIDParameter &data);
};

class RoleItemManager : public CSingleton<RoleItemManager>
{
public:
	RoleItemManager();
	~RoleItemManager();

public:
	int64_t  GetPackMoney();
	int64_t  GetReserveMoney();
	bool IsBagFull(int badid = fire::pb::item::BagTypes::BAG);
	CEGUI::ItemCell* GetItemCellAtBag(int baseID);
	CEGUI::ItemCell* GetItemCellAtQuestBag(int baseID);
	bool DestroyItem(); // 摧毁悬浮在鼠标上的道具,参数bDestroy为是否真正要摧毁，右键点击时改值为false
	void ClearBag(int bagid); //删除对应包裹的所有物品
	void CheckEquipEffect();

public:
	// 物品数量变化
	void FireItemNumChange(const ItemIDParameter &data);
	bool InsertItemNumChangeNotify(ItemNumChangeNotify& notiy);
	bool RemoveItemNumChangeNotify(ItemNumChangeNotify& notiy);
	void ClearItemNumChangeNotify();
    int InsertLuaItemNumChangeNotify(int handler);
	void RemoveLuaItemNumChangeNotify(int handler);

public:
	fire::NotifySet <ItemNumChangeNotify>	m_ItemNumChangeNotifySet;
    LuaItemNumChangeNotify*		m_pLuaItemNumChangeNotify;

	CBroadcastEvent<NoParam>	m_EventPackMoneyChange;
	CBroadcastEvent<NoParam>	m_EventDeportMoneyChange;
	CBroadcastEvent<NoParam>	m_EventReserveMoneyChange;
	CBroadcastEvent<NoParam>	m_EventPackGoldChange;
	CBroadcastEvent<NoParam>	m_EventRemoveItemList;			// 主要是用于提炼功能
	CBroadcastEvent<NoParam>	m_EventPackItemLocationChange;	// 包裹内的道具位置交换移动

	CEvent<int>					m_EventTypeMoneyChange;
	CEvent<int>					m_EventAddPackItem;				// 带参数的事件多播实例
	CEvent<int>					m_EventAddEquipItem;			// 带参数的事件多播实例
	CEvent<int>					m_EventDelBagItem;	
	CEvent<int>					m_EventDelEquipItem;	
	CEvent<int>					m_EventAddQuestItem;
	CEvent<int>					m_EventDelQuestItem;

};

inline RoleItemManager* gGetRoleItemManager()
{
	return RoleItemManager::GetInstance();
}

#endif