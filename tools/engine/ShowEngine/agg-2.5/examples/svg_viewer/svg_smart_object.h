///=============================================================================
/// Product   : 
/// Project   : svg_viewer
/// Module    : svg_smart_object.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description : 定义如下的模板类
/// 	smart_object 
///		smart_object_factory
///		smart_ptr
/// 	smart_object_holder 
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-15 Created.
///=============================================================================
#ifndef SVG_SMART_OBJECT_H_INCLUDED
#define SVG_SMART_OBJECT_H_INCLUDED

#ifndef SVGASSERT
#define SVGASSERT(expr) _ASSERTE(expr)
#endif

#ifndef SVGENSURE_THROW
#define SVGENSURE_THROW(expr)          \
	do {                               \
	int __svg_condVal=!!(expr);        \
	SVGASSERT(__svg_condVal);         \
	if(!(__svg_condVal)) throw agg::svg::exception(#expr);\
	} while (0)
#endif // SVGENSURE_THROW

#ifndef SVGENSURE
#define SVGENSURE(expr) SVGENSURE_THROW(expr)
#endif // ATLENSURE

namespace svg
{
	template<class T>
	class smart_object_factory;

	template<class T>
	class smart_object : public T
	{
	public:
		typedef T					basetype;
		typedef smart_object<T>		selftype;
		typedef selftype&			reference;
		typedef const selftype&		const_reference;
		long add_ref()
		{
			m_ref ++;
			return m_ref;
		}

		void release()
		{
			if( --m_ref == 0 )
			{
				delete this;
			}
		}

	protected:
		smart_object()
		{
			m_ref = 1;
		}

		friend class smart_object_factory<T>;

	private:
		long m_ref;
	};

	template<class T>
	class smart_object_factory
	{
	public:
		static smart_object<T>* create_instance()
		{
			return new smart_object<T>();
		}
	};

	template<class T>
	class smart_ptr
	{
	public:
		typedef T					objtype;
		typedef smart_ptr<T>		selftype;
		typedef selftype&			reference;
		typedef const selftype&		const_reference;
		
		smart_ptr()
		{
			m_pobj = NULL;
		}

		~smart_ptr()
		{
			if( m_pobj != NULL )
			{
				m_pobj->release();
			}
		}

		smart_ptr(int nNull)
		{
			SVGASSERT(nNull==0);
			(void)nNull;
			m_pobj = NULL;
		}

		smart_ptr(T* other)
		{
			m_pobj = other;
			if( m_pobj != NULL )
			{
				m_pobj->add_ref();
			}
		}

		smart_ptr(const smart_ptr& other)
		{
			m_pobj = other.m_pobj;
			if( m_pobj != NULL )
			{
				m_pobj->add_ref();
			}
		}

		bool operator == ( const T* pOther )
		{
			return m_pobj == pOther;
		}

		bool operator != ( const T* pOther )
		{
			return !(*this == pOther);
		}

		bool operator == ( const smart_ptr<T>& pOther )
		{
			return m_pobj == pOther.m_pobj;
		}

		bool operator != ( const smart_ptr<T>& pOther )
		{
			return !(*this == pOther);
		}

		const smart_ptr& operator = (T* other)
		{
			if( m_pobj != other )
			{
				if( m_pobj != NULL ) 
					m_pobj->release();

				if( other != NULL )
					other->add_ref();

				m_pobj = other;
			}

			return *this;
		}

		const smart_ptr& operator = (smart_ptr& other)
		{
			if( m_pobj != other.m_pobj )
			{
				if( m_pobj != NULL ) 
					m_pobj->release();

				if( other.m_pobj != NULL )
					other.m_pobj->add_ref();

				m_pobj = other.m_pobj;
			}

			return *this;
		}

		T* operator->()
		{
			SVGASSERT( m_pobj != NULL );
			return m_pobj;
		}

		const T* operator->() const
		{
			SVGASSERT( m_pobj != NULL );
			return m_pobj;
		}

		T& operator*()
		{
			SVGENSURE( m_pobj != NULL );
			return *m_pobj;
		}

		const T& operator*() const
		{
			SVGENSURE( m_pobj != NULL );
			return *m_pobj;
		}

		T** operator&()
		{
			return &m_pobj;
		}

		const T** operator&() const
		{
			return &m_pobj;
		}

		bool operator!() const
		{
			return m_pobj == NULL;
		}

		bool operator == (const T* p) const
		{
			return m_pobj == p;
		}

		bool operator != (const T* p) const
		{
			return m_pobj != p;
		}

		bool operator < (const T* p) const
		{
			return m_pobj < p;
		}

		void release()
		{
			T* _pobj = m_pobj;
			if( _pobj != NULL )
			{
				m_pobj = NULL;
				_pobj->release();
			}
		}

	protected:
		T* m_pobj;
	};

	template<class T>
	class smart_object_holder : public smart_ptr<T>
	{
	public:
		smart_object_holder()
		{
			m_pobj = smart_object_factory<T>::create_instance();
		}
	};

}// namespace svg

#endif // SVG_SMART_OBJECT_H_INCLUDED