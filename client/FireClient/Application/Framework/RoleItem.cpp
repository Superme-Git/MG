#include "stdafx.h"
#include "RoleItem.h"

stRoleItem::stRoleItem()
{
	m_Battleuse = -1;
	m_ThisID = -1;
}

stRoleItem::stRoleItem(int battleuse, int thisID)
{
	m_Battleuse = battleuse;
	m_ThisID = thisID;
}

stRoleItem::~stRoleItem()
{

}
