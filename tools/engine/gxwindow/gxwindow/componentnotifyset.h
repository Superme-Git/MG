#pragma once

namespace GXWINDOW { 

namespace INLINECLASS {  

template<typename T>
class CNotifySet
{
	typedef std::list<T> CMapType;

	CMapType		m_map;
public:
	CNotifySet()
	{
	}
	virtual ~CNotifySet()
	{
	}
public:
	void Insert( T t)
	{
		if( NULL == t)
			return;
		for( CMapType::iterator it = m_map.begin(), ite = m_map.end(); it != ite; ++ it)
		{
			T& st = *it;
			if( st->IsSameNotify( t))
				return;
		}
		m_map.push_back( t);
	}
	void Remove( T t)
	{
		if( NULL == t)
			return;
		for( CMapType::iterator it = m_map.begin(), ite = m_map.end(); it != ite; ++ it)
		{
			T& st = *it;
			if( st->IsSameNotify( t))
			{
				m_map.erase( it);
				return;
			}
		}
	}
	void Clear()
	{
		m_map.clear();
	}
public:
	template<class C, typename F>
		void NotifyAll( C& c, F f)
	{
		CMapType	temp = m_map;
		for( CMapType::iterator it = temp.begin(), ite = temp.end(); it != ite; ++ it)
		{
			T& t = *it;
			(t->*f)( c);
		}
	}
	template<class C, typename F>
		void NotifyAll( C& c, F f, const wchar_t* p)
	{
		CMapType	temp = m_map;
		for( CMapType::iterator it = temp.begin(), ite = temp.end(); it != ite; ++ it)
		{
			T& t = *it;
			(t->*f)( c, p);
		}
	}

	template<class C, typename F, class P>
		void NotifyAll( C& c, F f, P& p)
	{
		CMapType	temp = m_map;

		for( CMapType::iterator it = temp.begin(), ite = temp.end(); it != ite; ++ it)
		{
			T& t = *it;
			(t->*f)( c, p);
		}
	}
	template<class C, typename F, class P, class L>
		void NotifyAll( C& c, F f, P& p, L& l)
	{
		CMapType	temp = m_map;

		for( CMapType::iterator it = temp.begin(), ite = temp.end(); it != ite; ++ it)
		{
			T& t = *it;
			(t->*f)( c, p, l);
		}
	}
	template<class C, typename F, class P0, class P1, class P2>
		void NotifyAll( C& c, F f, P0& p0, P1& p1, P2& p2)
	{
		CMapType	temp = m_map;

		for( CMapType::iterator it = temp.begin(), ite = temp.end(); it != ite; ++ it)
		{
			T& t = *it;
			(t->*f)( c, p0, p1, p2);
		}
	}
	template<class C, typename F, class P0, class P1, class P2, class P3>
		void NotifyAll( C& c, F f, P0& p0, P1& p1, P2& p2, P3& p3)
	{
		CMapType	temp = m_map;

		for( CMapType::iterator it = temp.begin(), ite = temp.end(); it != ite; ++ it)
		{
			T& t = *it;
			(t->*f)( c, p0, p1, p2, p3);
		}
	}

	template<typename F, class P0, class P1, class P2, class P3>
		bool NotifyHasReturn( F f, P0 p0, P1 p1, P2 p2, P3 p3)
	{
		CMapType	temp = m_map;

		for( CMapType::iterator it = temp.begin(), ite = temp.end(); it != ite; ++ it)
		{
			T& t = *it;
			if( (t->*f)( p0, p1, p2, p3))
				return true;
		}
		return false;
	}
};
	
	
} // namespace INLINECLASS { 

} // namespace GXWINDOW { 
