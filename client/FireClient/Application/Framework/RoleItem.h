#ifndef __ROLEITEM_H
#define __ROLEITEM_H

#if _MSC_VER > 1000
#pragma once
#endif

struct stRoleItem
{
public:
	stRoleItem();
	stRoleItem(int battleuse, int thisID);
	~stRoleItem();

	int m_Battleuse;
	int m_ThisID;
};

#endif
