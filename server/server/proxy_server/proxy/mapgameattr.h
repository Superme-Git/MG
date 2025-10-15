#pragma once
#include <utility>
#include "rpcgen.hpp"

namespace gdeliverd
{
	enum
	{
		//Ҫ֧�ֶ౶���ԵĻ���iwebҲ�øĸģ���204��ʾN������(Ŀǰ204��iwebӦ����˫������`)
		ATTR_EXP		= 204,	//˫������
		ATTR_LAMBDA		= 205,	//����������
		ATTR_ACTIVITYMANAGER	= 206,	//�����Ա
		ATTR_NOTRADE		= 207,	//��ֹ��Ҽ佻��
		ATTR_NOAUCTION		= 208,	//��ֹ��������
		ATTR_NOMAIL		= 209,	//��ֹ�ʼ�����
		ATTR_NOFACTION		= 210,	//��ֹ�������
		ATTR_MONEY		= 211,	//˫����Ǯ
		ATTR_OBJECT		= 212,	//˫������
		ATTR_SP			= 213,	//˫�����ܵ�
		ATTR_NOSELLPOINT	= 214,	//��ֹ���۵㿨
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
