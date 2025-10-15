/***********************************************************************
filename: 	DroptItem.h
purpose:	µÙ¬‰ŒÔ
*************************************************************************/
#pragma once

#include "SceneObject.h"
#include "../ProtoDef/rpcgen/fire/pb/move/PickUpItem.hpp"

class DroptItem : public SceneObject
{
public:
	DroptItem();
	virtual ~DroptItem();

	virtual int64_t GetID(){return m_data.uniqueid;}
	int GetBaseID(){return m_data.baseid;}

	virtual std::wstring GetName(){return m_BaseData.name;}

	virtual bool CanPickedUp(){return true;}
	
	virtual bool OnSetCursor();

	bool SetAttribute(const fire::pb::move::PickUpItem& data);
	
private:
	fire::pb::move::PickUpItem	m_data;
	stNPCConfig		m_BaseData;
};
