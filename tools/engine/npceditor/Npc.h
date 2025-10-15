#pragma once

#include "Sprite.h"

enum enumNpcType
{
	eNpcTypeNone = 0,
	eNpcTypeImportant,		//��Ҫ
	eNpcTypeTranslate,      //����
	eNpcTypeTrade,		//��ҵ
	eNpcTypeNormal,	//��ͨ
	eNpcTypeMax,
};

//npc������
struct stNpcBaseData
{
	int				BaseID;
	std::wstring	strNpcModel;		//Npc����
	std::wstring    strBodyResName;
	std::wstring    strHeadResName;
	std::wstring    strHairResName;
	int		eNpcType;		//Npc����
	std::wstring	strName;		//Npc����
	std::wstring	strTitle;		//Npc��ν
	int	chat1;	//Npc�л�1
	int	chat2;	//Npc�л�2
	int	chat3;	//Npc�л�3
	int dirType; //��������



	stNpcBaseData()
	{
		BaseID=-1;
		strNpcModel=L"";
		strBodyResName=L"";
		strHeadResName=L"";
		strHairResName=L"";
		eNpcType=0;
		strName=L"";
		strTitle=L"";
        chat1=0;
		chat2=0;
		chat3=0;
		dirType=4;


	}
	stNpcBaseData(const stNpcBaseData& rhs)
	{
		BaseID=rhs.BaseID;
		strNpcModel=rhs.strNpcModel;
		eNpcType=rhs.eNpcType;
		strName=rhs.strName;
		strTitle=rhs.strTitle;
		chat1=rhs.chat1;
		chat2=rhs.chat3;
		chat3=rhs.chat3;
		dirType=rhs.dirType;

	}

	stNpcBaseData& operator=(const stNpcBaseData& rhs)
	{
		BaseID=rhs.BaseID;
		strNpcModel=rhs.strNpcModel;
		eNpcType=rhs.eNpcType;
		strName=rhs.strName;
		strTitle=rhs.strTitle;
		chat1=rhs.chat1;
		chat2=rhs.chat3;
		chat3=rhs.chat3;
		dirType=rhs.dirType;
		
	}

};

class CNpc:public Sprite
{
public:
	CNpc(Nuclear::ISprite* piSprte);
	~CNpc(){};
	void SetId(int id){ m_nId=id; }
	int GetId() { return m_nId; }
	void SetName(const std::wstring& name) { m_strName=name; };
	std::wstring GetName()  { return m_strName; }
	const stNpcBaseData* m_pNpcBaseData;
	
protected:
private:
	int m_nId;
	std::wstring m_strName;
	
};