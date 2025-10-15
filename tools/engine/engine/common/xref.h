#ifndef __Nuclear_XREF_H
#define __Nuclear_XREF_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
// hard, weak均非线程安全
template<class Y> class XHardRef;
template<class Y> class XWeakRef;

class XRefBase
{
	unsigned short m_iHardRefCount;
	unsigned short m_iWeakRefCount;
	int m_tick;				//最后一次使用的时间戳
public:
	XRefBase() : m_iHardRefCount(0), m_iWeakRefCount(0), m_tick(0) { }
	int GetHardRefCount() const { return m_iHardRefCount; }
	int GetWeakRefCount() const { return m_iWeakRefCount; }
	void HardRef() { ++m_iHardRefCount; ++m_iWeakRefCount; }
	void WeakRef() { ++m_iWeakRefCount; }
	void SetTick(int tick) { m_tick = tick; }
	int GetTick() const { return m_tick; }

	
	bool HardRelease()
	{
		bool r =  --m_iHardRefCount == 0;
		if( --m_iWeakRefCount == 0 )
			delete this;
		return r;
	}

	void WeakRelease()
	{
		if( --m_iWeakRefCount == 0 )
			delete this;
	}
private:
	XRefBase(const XRefBase &rhs);
	XRefBase& operator=(const XRefBase &rhs);
};

// hardref
template<class T>
class XHardRef
{
	friend class XWeakRef<T>;
	template<class Y> friend class XHardRef;
	template<class Y> friend class XWeakRef;
	T *m_pObj;
	XRefBase *m_pBase;
public:
	XHardRef() : m_pObj(NULL), m_pBase(NULL) { }

	const T* operator->() const
	{
		return m_pObj;
	}
	const T& operator*() const
	{
		return *m_pObj;
	}

	T* operator->()
	{
		return m_pObj;
	}
	T& operator*()
	{
		return *m_pObj;
	}

	bool IsValid() const
	{
		return m_pBase != NULL;
	}

	void SetTick(int tick) { m_pBase->SetTick(tick); }
	int GetTick() const { return m_pBase->GetTick(); }

	int GetHardRefCount() const
	{
		return m_pBase->GetHardRefCount();
	}

	int GetWeakRefCount() const
	{
		return m_pBase->GetWeakRefCount();
	}

	template<class Y>
	XHardRef(Y *pObj) : m_pObj(pObj), m_pBase(NULL) 
	{ 
		if (pObj)
		{
			m_pBase = new XRefBase();
			m_pBase->HardRef();
		}
		
	}
private:
	XHardRef(T *pObj, XRefBase *pBase) : m_pObj(pObj), m_pBase(pBase) { }
public:
	
	XHardRef(const XHardRef &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
	{ 		
		if( m_pBase != NULL )
			m_pBase->HardRef(); 
	}

	template<class Y>
	XHardRef(const XHardRef<Y> &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
	{ 
		if( m_pBase != NULL )
			m_pBase->HardRef(); 
	}

	XHardRef& operator=(const XHardRef &rhs) 
	{ 
		if( m_pBase == rhs.m_pBase ) 
			return *this;
		if( m_pBase != NULL && m_pBase->HardRelease() )
			delete m_pObj;
		m_pBase = rhs.m_pBase;
		if( m_pBase != NULL )
		{
			m_pBase->HardRef();
			m_pObj = rhs.m_pObj;
		}
		return *this;
	}

	template<class Y>
	XHardRef& operator=(const XHardRef<Y> &rhs) 
	{ 
		if( m_pBase == rhs.m_pBase ) 
			return *this;
		if( m_pBase != NULL && m_pBase->HardRelease() )
			delete m_pObj;
		m_pBase = rhs.m_pBase;
		if( m_pBase != NULL )
		{
			m_pBase->HardRef();
			m_pObj = rhs.m_pObj;
		}
		return *this;
	}

	template<class Y>
	XHardRef(const XWeakRef<Y> &rhs) : m_pObj(NULL), m_pBase(NULL)
	{		
		if( rhs.m_pBase != NULL && rhs.m_pBase->GetHardRefCount() > 0 )
		{
			m_pObj = rhs.m_pObj;
			m_pBase = rhs.m_pBase;
			m_pBase->HardRef();
		}
	}

	template<class Y>
	XHardRef& operator=(const XWeakRef<Y> &rhs)
	{		
		if( m_pBase == rhs.m_pBase ) 
			return *this;
		if( m_pBase != NULL && m_pBase->HardRelease() )
			delete m_pObj;
		m_pBase = NULL;
		if( rhs.m_pBase != NULL && rhs.m_pBase->GetHardRefCount() > 0 )
		{
			m_pObj = rhs.m_pObj;
			m_pBase = rhs.m_pBase;
			m_pBase->HardRef();
		}
		return *this;
	}

	template<class Y>
	XHardRef<Y> DynamicCast()
	{
		if( m_pBase != NULL )
		{
			Y *pObj = dynamic_cast<Y*>(m_pObj);
			if( pObj != NULL )
			{
				m_pBase->HardRef();
				return XHardRef<Y>(pObj, m_pBase);
			}
		}
		return XHardRef<Y>();
	}

	~XHardRef() 
	{ 
		if( m_pBase != NULL && m_pBase->HardRelease() )
			delete m_pObj;
	}
};

// weakref
template<class T>
class XWeakRef
{
	template<class Y> friend class XHardRef;
	template<class Y> friend class XWeakRef;
	T *m_pObj;
	XRefBase *m_pBase;
public:
	XWeakRef() : m_pObj(NULL), m_pBase(NULL)  { }

	template<class Y>
	XWeakRef(const XHardRef<Y> &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
	{ 
		if( m_pBase != NULL )
			m_pBase->WeakRef(); 
	}

	XWeakRef(const XWeakRef &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
	{ 
		if( m_pBase != NULL )
			m_pBase->WeakRef(); 	
	}

	template<class Y>
	XWeakRef(const XWeakRef<Y> &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
	{ 
		if( m_pBase != NULL )
			m_pBase->WeakRef(); 
	}

	XWeakRef& operator=(const XWeakRef &rhs) 
	{ 
		if( m_pBase == rhs.m_pBase ) 
			return *this;
		if( m_pBase != NULL )
			m_pBase->WeakRelease();
		m_pBase = rhs.m_pBase;
		if( m_pBase != NULL )
		{
			m_pBase->WeakRef();
			m_pObj = rhs.m_pObj;
		}
		return *this;
	}

	template<class Y>
	XWeakRef& operator=(const XWeakRef<Y> &rhs) 
	{ 
		if( m_pBase == rhs.m_pBase ) 
			return *this;
		if( m_pBase != NULL )
			m_pBase->WeakRelease();
		m_pBase = rhs.m_pBase;
		if( m_pBase != NULL )
		{
			m_pBase->WeakRef();
			m_pObj = rhs.m_pObj;
		}
		return *this;
	}

	template<class Y>
	XWeakRef& operator=(const XHardRef<Y> &rhs) 
	{ 
		if( m_pBase == rhs.m_pBase ) 
			return *this;
		if( m_pBase != NULL )
			m_pBase->WeakRelease();
		m_pBase = rhs.m_pBase;
		if( m_pBase != NULL )
		{
			m_pObj = rhs.m_pObj;
			m_pBase->WeakRef();
		}
		return *this;
	}

	template<class Y>
	XHardRef<Y> DynamicCast()
	{
		if( m_pBase != NULL && m_pBase->GetHardRefCount() > 0)
		{
			Y *pObj = dynamic_cast<Y*>(m_pObj);
			if( pObj != NULL )
			{
				m_pBase->HardRef();
				return XHardRef<Y>(pObj, m_pBase);
			}
		}
		return XHardRef<Y>();
	}

	void Release()
	{
		if( m_pBase != NULL )
		{
			m_pBase->WeakRelease();
			m_pBase = NULL;
		}
	}

	bool IsValid() const
	{
		return m_pBase != NULL;
	}
	
	~XWeakRef() 
	{
		if( m_pBase != NULL )
			m_pBase->WeakRelease(); 
	}
};


};

#endif