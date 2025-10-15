#pragma once
#include <utility>
#include "rpcgen.hpp"

namespace gdeliverd
{
	enum
	{
		//要支持多倍属性的话，iweb也得改改，即204表示N倍经验(目前204在iweb应该是双倍经验`)
		ATTR_EXP		= 204,	//双倍经验
		ATTR_LAMBDA		= 205,	//并发连接数
		ATTR_ACTIVITYMANAGER	= 206,	//活动管理员
		ATTR_NOTRADE		= 207,	//禁止玩家间交易
		ATTR_NOAUCTION		= 208,	//禁止拍卖操作
		ATTR_NOMAIL		= 209,	//禁止邮件操作
		ATTR_NOFACTION		= 210,	//禁止公会操作
		ATTR_MONEY		= 211,	//双倍掉钱
		ATTR_OBJECT		= 212,	//双倍掉物
		ATTR_SP			= 213,	//双倍技能点
		ATTR_NOSELLPOINT	= 214,	//禁止销售点卡
	};
	class GameAttrMap
	{
	public:	
		enum {
			DISABLE = 0,
			ENABLE = 1,
		};
	private:	
	   	typedef std::map<int,int> AttrMap;
	   	typedef AttrMap::iterator Iterator;
		AttrMap attrmap;
		GameAttrMap() 
		{
			attrmap[ATTR_EXP] = DISABLE;
			attrmap[ATTR_LAMBDA] = DISABLE;
			attrmap[ATTR_ACTIVITYMANAGER] = DISABLE;
			attrmap[ATTR_NOTRADE] = DISABLE;
			attrmap[ATTR_NOAUCTION] = DISABLE;
			attrmap[ATTR_NOMAIL] = DISABLE;
			attrmap[ATTR_NOFACTION] = DISABLE;
			attrmap[ATTR_MONEY] = DISABLE;
			attrmap[ATTR_OBJECT] = DISABLE;
			attrmap[ATTR_SP] = DISABLE;
			attrmap[ATTR_NOSELLPOINT] = DISABLE;
		}
		static GameAttrMap * GetInstance() 
		{
			static GameAttrMap instance;
			return &instance;
		}	
	public:
		~GameAttrMap() { }
		static bool PutAttr(int attr,int value) 
		{
			Iterator it = GetInstance()->attrmap.find(attr);
			if ( it != GetInstance()->attrmap.end() )
			{
				(*it).second = value;
				return true;
			}
			return false;
		}
		static int GetAttr(int attr) 
		{
			Iterator it = GetInstance()->attrmap.find(attr);
			return (it == GetInstance()->attrmap.end()) ? 0 : (*it).second;
		}
	/*	static void GetAttrs( std::vector<gnet::GameAttr>& attrs ) 
		{
			AttrMap::iterator it=_instance().attrmap.begin(),ite=_instance().attrmap.end();
			for ( ;it!=ite;++it )
				attrs.push_back( gnet::GameAttr((*it).first,(*it).second) );
		}
	*/
		static void GetAttrs( std::map<int,int>& attrs ) 
		{
			AttrMap::iterator begin = GetInstance()->attrmap.begin();
			AttrMap::iterator end = GetInstance()->attrmap.end();
			for (AttrMap::iterator it = begin ;it != end;++it )
				attrs.insert( std::make_pair((*it).first,(*it).second) );
		}

	};
}
