#ifndef __MIX_BASE_SMART_PRT_H_
#define __MIX_BASE_SMART_PRT_H_

#if _MSC_VER > 1000
#pragma once
#endif


namespace MIX
{

template <typename T> class HeapStorage;
template <typename T> class HeapArrayStorage;
template <typename P> class RefCounted;
template <typename P> class DestructiveCopy;
template
<
typename T,
template < class> class OwnershipPolicy = RefCounted,
template < class> class StoragePolicy = HeapStorage
>
class SmartPtr;   

class IRefCounter
{
protected:
	virtual void AddRef() const = 0;
	virtual int Release() const = 0;
	template<typename P> friend class RefCounted;
};

//! 引用读数器类,需要引用计数的类从这个类继承
class RefCounter
{
	template<typename P> friend class RefCounted;

public:
	RefCounter( )
		: m_refCount( 0)
	{}
	int GetRefCount() const
	{
		return m_refCount;
	}

protected:
	void AddRef() const
	{
		++m_refCount;
	}

	int Release() const
	{
		return --m_refCount;
	}

private:
	mutable int m_refCount;
};


//! 用于智能指针的堆存储策略
template <typename T>
class HeapStorage
{
	template
	<
		typename T,
		template < class> class OwnershipPolicy,
		template < class> class StoragePolicy
	>
	friend class SmartPtr;   
public:
	typedef T* PTR_TYPE;
	typedef T* INIT_TYPE;
	typedef T& REF_TYPE;
	typedef T* STORAGE_TYPE;
	typedef T  TYPE;

	HeapStorage() : m_data( 0)
	{}

	HeapStorage( STORAGE_TYPE& rhs)
		: m_data( rhs)
	{
	}
	
	HeapStorage( const STORAGE_TYPE& rhs)
		: m_data( rhs)
	{}

	PTR_TYPE operator->() const
	{
		return m_data;
	}

	REF_TYPE operator*()
	{
		return *m_data;
	}

protected:
	void Destroy()
	{
		if ( m_data)
		{
			delete m_data;
			m_data = 0;
		}
	}
	const STORAGE_TYPE& GetRef() const
	{
		return m_data;
	}
	STORAGE_TYPE& GetRef()
	{
		return m_data;
	}

	void Swap( HeapStorage& rhs)
	{
		std::swap( m_data, rhs.m_data);
	}
	
private:
	STORAGE_TYPE	m_data;
};

//! 用于智能指针的堆数组策略
template <typename T>
class HeapArrayStorage
{
	template
	<
		typename T,
		template < class> class OwnershipPolicy,
		template < class> class StoragePolicy
	>
	friend class SmartPtr;   
public:
	typedef T* PTR_TYPE;
	typedef T* INIT_TYPE;
	typedef T& REF_TYPE;
	typedef T* STORAGE_TYPE;
	typedef T  TYPE;

	HeapArrayStorage() : m_data( 0)
	{}

	HeapArrayStorage( STORAGE_TYPE& rhs)
		: m_data( rhs)
	{
	}

	HeapArrayStorage( const STORAGE_TYPE& rhs)
		: m_data( rhs)
	{}
	
	PTR_TYPE operator->() const
	{
		return m_data;
	}

	REF_TYPE operator*()
	{
		return *m_data;
	}

	template < class F>
	friend F* GetArrayPtr( HeapArrayStorage& sp);
	template < class F>
	friend const F* GetArrayPtr( const HeapArrayStorage& sp);

protected:
	void Destroy()
	{
		if ( m_data)
		{
			delete []m_data;
			m_data = 0;
		}
	}
	const STORAGE_TYPE& GetRef() const
	{
		return m_data;
	}
	STORAGE_TYPE& GetRef()
	{
		return m_data;
	}

	void Swap( HeapArrayStorage& rhs)
	{
		std::swap( m_data, rhs.m_data);
	}
private:
	STORAGE_TYPE	m_data;
};

template < class F>
F* GetArrayPtr( HeapArrayStorage<F>& sp)
{
	return sp.GetRef();
}
template < class F>
const F* GetArrayPtr( const HeapArrayStorage<F>& sp)
{
	return sp.GetRef();
}

template <typename P>
class RefCounted
{
public:
	RefCounted()
	{}

	RefCounted( const P& val)
	{
		val->AddRef();
	}

	P Clone( const P& val)
	{
		val->AddRef();
		return val;
	}

	bool Release( const P& val)
	{
		if ( val && val->Release())
			return false;
		else
			return true;
	}

	void Swap( RefCounted& rhs)
	{}

	enum { destructiveCopy = false};
};

template <typename P>
class COMRefCounted
{
public:
	COMRefCounted()
	{}

	COMRefCounted( const P&)
	{
	}

	template < class Q>
	COMRefCounted( const Q&)
	{
	}

	template < class U>
	COMRefCounted( const COMRefCounted<U>&)
	{}

	P Clone( const P& val)
	{
		if ( val)
			val->AddRef();
		return val;
	}

	bool Release( const P& val)
	{
		if ( val)
			val->Release();
		return false;
	}

	void Swap( COMRefCounted& rhs)
	{}

	enum { destructiveCopy = false};
};

template < typename P>
class DestructiveCopy
{
public:
	DestructiveCopy()
	{}

	DestructiveCopy( const P& val)
	{}
	
	P Clone( P& val)
	{
		P result(val);
		val = P();
		return result;
	}

	bool Release( const P&)
	{
		return true;
	}

	static void Swap( DestructiveCopy& )
	{}

	enum { destructiveCopy = true,};
};


#if defined(_MSC_VER)
#pragma warning( push)
#pragma warning( disable : 4521)
#endif


//! 智能指针
template
<
	typename T, 
	template <class> class OwnershipPolicy,
	template <class> class StoragePolicy
>
class SmartPtr
	:public StoragePolicy<T>,
	public OwnershipPolicy< typename StoragePolicy<T>::INIT_TYPE>
{
	typedef StoragePolicy<T> SP;
	typedef OwnershipPolicy<typename StoragePolicy<T>::INIT_TYPE> OP;

public:
	typedef typename SP::PTR_TYPE	PTR_TYPE;
	typedef typename SP::REF_TYPE	REF_TYPE;
	typedef typename SP::TYPE		TYPE;

	SmartPtr() {}

	~SmartPtr()
	{
		if ( Release( GetRef()))
			SP::Destroy();
	}

	explicit SmartPtr( typename SP::STORAGE_TYPE& val)
		: SP( val), OP( val)
	{}

	SmartPtr( const typename SP::STORAGE_TYPE& val)
		: SP( val), OP( val)
	{}

	SmartPtr( const SmartPtr& rhs) 
		: SP( rhs) 
	{
		if ( rhs.GetRef())
			GetRef() = Clone( rhs.GetRef());
	}

	SmartPtr( SmartPtr& rhs) 
		: SP( rhs) 
	{
		if ( rhs.GetRef())
			GetRef() = Clone( rhs.GetRef());
	}

	template< class T1, template <class> class OP1, template <class> class SP1>
	SmartPtr( const SmartPtr<T1, OP1, SP1>& rhs)
	{
		if ( rhs.GetRef())
			GetRef() = Clone( rhs.GetRef());
	}

	template< class T1, template <class> class OP1, template <class> class SP1>
	SmartPtr( SmartPtr<T1, OP1, SP1>& rhs)
	{
		if ( rhs.GetRef())
			GetRef() = Clone( rhs.GetRef());
	}

	const SmartPtr& operator= ( const typename SP::STORAGE_TYPE& val)
	{
		SmartPtr temp( val);
		Swap( temp);
		return *this;
	}

	const SmartPtr& operator= ( const SmartPtr& rhs)
	{
		SmartPtr temp( rhs);
		Swap( temp);
		return *this;
	}

	template< class T1, template <class> class OP1, template <class> class SP1>
	const SmartPtr& operator= ( const SmartPtr<T1, OP1, SP1>& rhs)
	{
		SmartPtr temp( rhs);
		Swap( temp);
		return *this;
	}

	void Swap( SmartPtr& rhs)
	{
		OP::Swap( rhs);
		SP::Swap( rhs);
	}

	void Reset()
	{
		OP::Swap( SmartPtr());
		SP::Swap( SmartPtr());
	}

	PTR_TYPE operator->()
	{
		return SP::operator->();
	}
	
	const PTR_TYPE operator->() const
	{
		return SP::operator->();
	}

	REF_TYPE operator*()
	{
		return SP::operator*();
	}

	const T& operator*() const
	{
		return SP::operator*();
	}

	bool operator!() const
	{
		return GetRef() == 0;
	}

	const T* GetPtr() const
	{
		return GetRef();
	}

	T* GetPtr()
	{
		return GetRef();
	}

	bool operator== ( const typename SP::STORAGE_TYPE& val) const
	{
		return GetRef() == val;
	}

	bool operator!=( const typename SP::STORAGE_TYPE& val) const
	{
		return GetRef() == val;
	}

	template< class T1, template <class> class OP1, template <class> class SP1>
	bool operator== ( const SmartPtr<T1, OP1, SP1>& rhs) const
	{
		return GetRef() == rhs.GetRef();
	}

	template< class T1, template <class> class OP1, template <class> class SP1>
	bool operator!= ( const SmartPtr<T1, OP1, SP1>& rhs) const
	{
		return GetRef() != rhs.GetRef();
	}

	template< class T1, template <class> class OP1, template <class> class SP1>
	bool operator< ( const SmartPtr<T1, OP1, SP1>& rhs) const
	{
		return GetRef() < rhs.GetRef();
	}

	bool IsNULL() const
	{
		return GetRef() == 0;
	}
};

#if defined(_MSC_VER)
#pragma warning( pop)
#endif


#define MIX_DEFINE_REFCOUNTER(C) private: class _MYCOUNTER : public RefCounter \
	{ public: void AddRef() const { RefCounter::AddRef();} int Release() const { return RefCounter::Release();}};\
	mutable _MYCOUNTER _refCounter_; \
	public: void AddRef() const { _refCounter_.AddRef();} int Release() const { return _refCounter_.Release();} \
	template<typename P> friend class RefCounted; friend class C;

#define MIX_DECLARE_WEAK_PTR(C) 
#define MIX_DEFINE_WEAK_MANAGER(C) 
#define MIX_WEAK_MANAGER_CTOR



}	// namesapce MIX




#endif	//__MIX_BASE_SMART_PRT_H_
