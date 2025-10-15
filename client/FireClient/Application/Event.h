#ifndef __EVENT_HPP
#define __EVENT_HPP

#include "script_support/CCScriptSupport.h"
#include <vector>
#include <list>

extern "C" {
#include "lua.h"
#include "lauxlib.h"
}

using std::vector;
using std::list;

class EmptyType {};

template<typename EventFunPtr>

class EventObject
{
public:
	EventObject():ObjectPtr(NULL), EventFunPointer(NULL){}
public:
	EmptyType* ObjectPtr;			//??μ??‘??÷∏’?
	EventFunPtr* EventFunPointer;	//??μ?≥…‘±∫???÷∏’?
};

#ifndef InvokeEvent
#define InvokeEvent ((iter->ObjectPtr)->*(*iter->EventFunPointer))
#endif


template<class Targs>
struct invoker
{
	typedef void (*method)(Targs args);
    
	static void invoke(Targs args)
	{
		m_pcallback(args);
	}
    
	static void bind(method pcallback)
	{
		m_pcallback = pcallback;
	}
    
private:
	static method m_pcallback;
};

template<class Targs>
typename invoker<Targs>::method invoker<Targs>::m_pcallback = NULL;

template<class tobject, class Targs>
struct memberinvoker
{
	typedef void (tobject::*membermethod)(Targs);
    
	static void invoke(Targs args)
	{
		(m_pobject->*m_pcallback)(args);
	}
    
	static void bind(tobject* pobject,membermethod pcallback)
	{
		m_pobject = pobject;
		m_pcallback = pcallback;
	}
    
private:
	static tobject*  m_pobject;
	static membermethod m_pcallback;
};

template<class tfunctor, class Targs>
struct functorinvoker
{
	typedef tfunctor* functormethod;
    
	static void invoke(Targs args)
	{
		m_pcallback(args);
	}
    
	static void bind(functormethod pcallback)
	{
		m_pcallback = pcallback;
	}
    
private:
	static functormethod m_pcallback;
};

template<class Targs>
class delegate  
{
public:
	delegate(){}
	virtual ~delegate(){}
public:

	typedef void (*invokertype)(Targs args);

	// for global or static methods
	delegate(void (*pcallback)(Targs))
		:m_pinvoker(NULL)
	{
		invoker<Targs>::bind(pcallback);
		m_pinvoker = invoker<Targs>::invoke;
	} 

	// for object member methods
	template<class tobject>
	delegate(tobject* pobject, void (tobject::*pcallback)(Targs))
		:m_pinvoker(NULL)
	{
		memberinvoker<tobject,Targs>::bind(pobject,pcallback);
		m_pinvoker = memberinvoker<tobject,Targs>::invoke;  
	}

	// for functor methods
	template<class tfunctor>
	delegate(tfunctor* pcallback)
		:m_pinvoker(NULL)
	{
		functorinvoker<tfunctor,Targs>::bind(pcallback);
		m_pinvoker = functorinvoker<tfunctor,Targs>::invoke; 
	}


	void operator() (Targs args)
	{
		m_pinvoker(args);
	}

	invokertype GetInvoker(){return m_pinvoker;}
	//bool operator==(const delegate<Targs>& dele) const {return GetInvoker() == dele<Targs>.GetInvoker();}
private:
	invokertype m_pinvoker;
};





template<class tobject, class Targs>
tobject* memberinvoker<tobject, Targs>::m_pobject = NULL;

template<class tobject, class Targs>
typename memberinvoker<tobject, Targs>::membermethod memberinvoker<tobject,/*treturn,*/Targs>::m_pcallback = NULL;



template<class tfunctor, class Targs>
typename functorinvoker<tfunctor, Targs>::functormethod functorinvoker<tfunctor, Targs>::m_pcallback = NULL;

template<class Targs>
class CEvent
{
public:

	typedef Targs eventargs; 
	typedef delegate<Targs> eventhandler;

public:
	CEvent(){}
	virtual ~CEvent(){
        
        for(std::list<int>::iterator iter=m_VecScriptHandler.begin(); iter != m_VecScriptHandler.end(); iter++)
        {
            luaL_unref(cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->getLuaState(), LUA_REGISTRYINDEX, *iter);
        }
        m_VecScriptHandler.clear();
    }


public:
	void call(int args)
	{
		for (typename std::list<eventhandler>::iterator i = m_handler.begin(); i != m_handler.end(); ++i)
		{
			(*i)(args);
		}
		for (std::list<int>::iterator iter = m_VecScriptHandler.begin(); iter != m_VecScriptHandler.end(); iter++)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeFunctionWithIntegerData(*iter, args);
		}
	}

	void operator() (eventargs/* const& */args)
	{
		for(typename std::list<eventhandler>::iterator i = m_handler.begin(); i != m_handler.end(); ++i)
		{
			(*i)(args);
		}
        //只支持int参数，其他参数不允许用了
        for(std::list<int>::iterator iter=m_VecScriptHandler.begin(); iter != m_VecScriptHandler.end(); iter++)
        {
            cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeFunctionWithIntegerData(*iter, (int)args);
        }
	}

	CEvent& operator+= ( eventhandler deleobj )
	{
		m_handler.push_back(deleobj);

		return *this;
	}

	CEvent& operator-= ( eventhandler deleobj )
	{
		for(typename std::list<eventhandler>::iterator i = m_handler.begin(); i != m_handler.end(); ++i)
		{
			if (i->GetInvoker() == deleobj.GetInvoker())
			{
				m_handler.erase(i);
				break;
			}
		}

		return *this;
	}
    
    virtual int InsertScriptFunctor(int handler)
    {
        m_VecScriptHandler.push_back(handler);
        return handler;
    }
    virtual void RemoveScriptFunctor(int handler)
    {
        for(std::list<int>::iterator iter=m_VecScriptHandler.begin(); iter != m_VecScriptHandler.end();)
        {
            if ((*iter) == handler)
            {
                luaL_unref(cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->getLuaState(), LUA_REGISTRYINDEX, handler);
                m_VecScriptHandler.erase(iter++);
            }
			else
			{
				++iter;
			}
        }
    }

private:

	std::list<eventhandler> m_handler;
    std::list<int> m_VecScriptHandler;
};



///////////////////////////////////////////////////////////////////
template<typename EventFunPtr>
class CBroadcastEvent
{
public:
	typedef list< EventObject<EventFunPtr> > EventList;
	typedef typename list< EventObject<EventFunPtr> >::iterator EventIterator;

public:
	//??o???≤?∞???
	template<typename ObjectType,typename MemeberFunctionPtrType>
	static EventObject<EventFunPtr> EventBind(ObjectType* pObj, MemeberFunctionPtrType memberFunctionPtr)
	{
		MemeberFunctionPtrType* pf = new MemeberFunctionPtrType;
		*pf = memberFunctionPtr;
		EventObject<EventFunPtr> eventObj;
		eventObj.ObjectPtr = (EmptyType*)pObj;
		eventObj.EventFunPointer = (EventFunPtr*)pf;
		return eventObj;
	}
public:
	CBroadcastEvent()
	{
	}
	virtual ~CBroadcastEvent()
	{
		EventIterator iter;

		for( iter = m_EventList.begin();iter != m_EventList.end();++iter )
		{
			if( iter->EventFunPointer != NULL )
				delete iter->EventFunPointer;
		}
        
        for(std::list<int>::iterator iter=m_VecScriptHandler.begin(); iter != m_VecScriptHandler.end(); iter++)
        {
            luaL_unref(cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->getLuaState(), LUA_REGISTRYINDEX, *iter);
        }
        m_VecScriptHandler.clear();
	}

	virtual void operator+=(const EventObject<EventFunPtr>& eventObj)
	{
		bool AlreadyHaveEvent = false;
		for(EventIterator iter = m_EventList.begin();iter != m_EventList.end();++iter)
		{
			if( iter->ObjectPtr == eventObj.ObjectPtr
			 && *(iter->EventFunPointer) == (*eventObj.EventFunPointer) )
			{
				AlreadyHaveEvent = true;
				if( eventObj.EventFunPointer != NULL )
					delete eventObj.EventFunPointer;
				break;
			}
		}
		if( !AlreadyHaveEvent )
			m_EventList.push_back(eventObj);
	}

	virtual void operator-=(const EventObject<EventFunPtr>& eventObj )
	{
		for(EventIterator iter = m_EventList.begin();iter != m_EventList.end();++iter)
		{
			if( iter->ObjectPtr == eventObj.ObjectPtr
			 && *(iter->EventFunPointer) == *(eventObj.EventFunPointer) )
			{
				if( iter->EventFunPointer != NULL )
					delete iter->EventFunPointer;
				m_EventList.erase(iter);
				break;
			}
		}
		delete eventObj.EventFunPointer;
	}
    virtual int InsertScriptFunctor(int handler)
    {
        m_VecScriptHandler.push_back(handler);
        return handler;
    }
    virtual void RemoveScriptFunctor(int handler)
    {
        for(std::list<int>::iterator iter=m_VecScriptHandler.begin(); iter != m_VecScriptHandler.end();)
        {
            if ((*iter) == handler)
            {
                luaL_unref(cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->getLuaState(), LUA_REGISTRYINDEX, handler);
                m_VecScriptHandler.erase(iter++);
            }
			else
			{
				++iter;
			}
        }
    }
	virtual void Bingo()	//￥?∑￠??o???≤???”?
	{
		EventList tmplist = m_EventList;
		EventIterator iter;
		for( iter = tmplist.begin();iter != tmplist.end();++iter )
		{
			InvokeEvent();
		}
        //只支持null参数，其他参数不允许用了
        for(std::list<int>::iterator iter=m_VecScriptHandler.begin(); iter != m_VecScriptHandler.end(); iter++)
        {
            cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeFunctionByHandler(*iter);
        }
	}
public:
	EventList m_EventList;
    std::list<int> m_VecScriptHandler;
};

typedef EmptyType EventDelegater;
typedef void(EventDelegater::*NoParam) (void);
typedef void(EventDelegater::*intParam) (int);
#endif
