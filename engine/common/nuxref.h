#ifndef __Nuclear_XREF_H__
#define __Nuclear_XREF_H__

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	// hard, weak均非线程安全
	template<class Y> class NuclearHardRef;
	template<class Y> class NuclearWeakRef;

	class NuclearRefBase
	{
	public:
		NuclearRefBase() : m_iHardRefCount(0), m_iWeakRefCount(0), m_tick(0) { }
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
		NuclearRefBase(const NuclearRefBase &rhs);
		NuclearRefBase& operator=(const NuclearRefBase &rhs);

	private:
		unsigned short	m_iHardRefCount;
		unsigned short	m_iWeakRefCount;
		int				m_tick;	//最后一次使用的时间戳
	};

	template<class T>
	class NuclearHardRef
	{
		friend class NuclearWeakRef<T>;
		template<class Y> friend class NuclearHardRef;
		template<class Y> friend class NuclearWeakRef;
		T *m_pObj;
		NuclearRefBase *m_pBase;
	public:
		NuclearHardRef() : m_pObj(NULL), m_pBase(NULL) { }

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
		T* get() const
		{
			return m_pObj;
		}
		explicit operator bool() const
		{
			return (m_pObj != 0);
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
		NuclearHardRef(Y *pObj) : m_pObj(pObj), m_pBase(NULL) 
		{ 
			if (pObj)
			{
				m_pBase = new NuclearRefBase();
				m_pBase->HardRef();
			}
		
		}

	public:
	
		NuclearHardRef(const NuclearHardRef &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
		{ 		
			if( m_pBase != NULL )
				m_pBase->HardRef(); 
		}

		template<class Y>
		NuclearHardRef(const NuclearHardRef<Y> &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
		{ 
			if( m_pBase != NULL )
				m_pBase->HardRef(); 
		}

		NuclearHardRef& operator=(const NuclearHardRef &rhs) 
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
		NuclearHardRef& operator=(const NuclearHardRef<Y> &rhs) 
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
		NuclearHardRef(const NuclearWeakRef<Y> &rhs) : m_pObj(NULL), m_pBase(NULL)
		{		
			if( rhs.m_pBase != NULL && rhs.m_pBase->GetHardRefCount() > 0 )
			{
				m_pObj = rhs.m_pObj;
				m_pBase = rhs.m_pBase;
				m_pBase->HardRef();
			}
		}

		template<class Y>
		NuclearHardRef& operator=(const NuclearWeakRef<Y> &rhs)
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
		NuclearHardRef<Y> DynamicCast()
		{
			if( m_pBase != NULL )
			{
				Y *pObj = dynamic_cast<Y*>(m_pObj);
				if( pObj != NULL )
				{
					m_pBase->HardRef();
					return NuclearHardRef<Y>(pObj, m_pBase);
				}
			}
			return NuclearHardRef<Y>();
		}

		~NuclearHardRef() 
		{ 
			if( m_pBase != NULL && m_pBase->HardRelease() )
				delete m_pObj;
		}

	private:
		NuclearHardRef(T *pObj, NuclearRefBase *pBase) : m_pObj(pObj), m_pBase(pBase) { }
	};

	template<class T>
	class NuclearWeakRef
	{
		template<class Y> friend class NuclearHardRef;
		template<class Y> friend class NuclearWeakRef;
		T *m_pObj;
		NuclearRefBase *m_pBase;
	public:
		NuclearWeakRef() : m_pObj(NULL), m_pBase(NULL)  { }

		template<class Y>
		NuclearWeakRef(const NuclearHardRef<Y> &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
		{ 
			if( m_pBase != NULL )
				m_pBase->WeakRef(); 
		}

		NuclearWeakRef(const NuclearWeakRef &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
		{ 
			if( m_pBase != NULL )
				m_pBase->WeakRef(); 	
		}

		template<class Y>
		NuclearWeakRef(const NuclearWeakRef<Y> &rhs) : m_pObj(rhs.m_pObj), m_pBase(rhs.m_pBase)
		{ 
			if( m_pBase != NULL )
				m_pBase->WeakRef(); 
		}

		NuclearWeakRef& operator=(const NuclearWeakRef &rhs) 
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
		NuclearWeakRef& operator=(const NuclearWeakRef<Y> &rhs) 
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
		NuclearWeakRef& operator=(const NuclearHardRef<Y> &rhs) 
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
		NuclearHardRef<Y> DynamicCast()
		{
			if( m_pBase != NULL && m_pBase->GetHardRefCount() > 0)
			{
				Y *pObj = dynamic_cast<Y*>(m_pObj);
				if( pObj != NULL )
				{
					m_pBase->HardRef();
					return NuclearHardRef<Y>(pObj, m_pBase);
				}
			}
			return NuclearHardRef<Y>();
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
	
		~NuclearWeakRef() 
		{
			if( m_pBase != NULL )
				m_pBase->WeakRelease(); 
		}
	};

}

#endif