/********************************************************************
	file base:	notify
	purpose:	一个通告模板，从GXWindow里面扒出来的
*********************************************************************/

#pragma once

namespace fire {

	/** 通告集合
	*/
	template<class NOTIFY> class NotifySet
	{
		NotifySet(const NotifySet&) {}
		NotifySet& operator=( const NotifySet&) { return *this; }
	private:
		typedef std::list<NOTIFY*> CNotifyList;	
		CNotifyList* m_notifylist;	//! 为了让在没有通告对像的时候，少占一些内存，这儿使用了指针；在第一次出现通告对像时，再构造容器
	public:
		NotifySet() : m_notifylist( NULL) {}
		~NotifySet() {
			if (m_notifylist)
			{
				delete m_notifylist;
			}
		}
	private:
		//得到容器，如果容器没有被构造，就构造容器
		CNotifyList& GetSafeList()
		{
			if (NULL == m_notifylist)
				m_notifylist = new CNotifyList;
			return *m_notifylist;
		}
		//判断容器是否为空，考虑到容器没有被构造的情况
		bool IsSafeEmpty()
		{
			if( NULL == m_notifylist)
				return true;
			else
				return m_notifylist->empty();
		}
	public:
		//添加一个通告对像，如果该对像在容器中已经存在，返回false
		bool InsertNotify( NOTIFY& n)
		{
			CNotifyList& nl = NotifySet::GetSafeList();
			typename CNotifyList::iterator it = std::find( nl.begin(), nl.end(), &n);
			if( it != nl.end())
				return false;
			nl.push_back( &n);
			return true;
		}
		//清除所有通告对像，这里没有删除容器对像。这里是否需要删除容器，值得考虑！！！
		void Clear()
		{
			if (NULL == m_notifylist)
				return;
			else
				m_notifylist->clear();
		}
		//删除一个通告对像，如果该对像在容器中不存在，返回false
		bool RemoveNotify( NOTIFY& n)
		{
			if( IsSafeEmpty())
				return false;

			CNotifyList& nl = NotifySet::GetSafeList();
			typename CNotifyList::iterator it = std::find( nl.begin(), nl.end(), &n);
			if( it == nl.end())
				return false;
			nl.erase( it);
			return true;
		}
	public:
		//触发通告，如果有某个通告返回true了，就不再通告其他对像了，通告函数有1个参数
		template<class F, class PARAM> bool FireNotifyCareBooleanReturn( F f, PARAM& p)
		{
			if( IsSafeEmpty())
				return false;
			CNotifyList& nl = GetSafeList();
			for( typename CNotifyList::iterator it = nl.begin(), ite = nl.end(); it != ite; ++ it)
			{
				NOTIFY*& n = *it;
				if( (n->*f)( p))
					return true;
			}
			return false;
		}
		//触发通告
		template<class F, class DATA> void FireNotify(F f, DATA& data)
		{
			if (IsSafeEmpty())
				return;
			CNotifyList& nl = GetSafeList();
			for (typename CNotifyList::iterator it = nl.begin(), ite = nl.end(); it != ite; ++it)
			{
				NOTIFY*& n = *it;
				(n->*f)(data);
			}
		}
	};

}
