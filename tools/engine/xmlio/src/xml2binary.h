///=============================================================================
/// Product   : Nuclear
/// Project   : xmliolib
/// Module    : xml2binary.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	XML 到 OctetsXML 的转换
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-7-17 Created.
///=============================================================================
#include <msxml.h>
#include <atlcomcli.h>
#include <map>
#include "../../pfs/pfs2.h"
#include "XMLIOHeapManager.h"

#ifndef _FARQ	/* specify standard memory model */
#define _FARQ
#define _PDFT	ptrdiff_t
#define _SIZT	size_t
#endif /* _FARQ */

namespace std
{
	// TEMPLATE FUNCTION _Allocate2
	template<typename _Ty> inline
	_Ty _FARQ *_Allocate2(_SIZT _Count, _Ty _FARQ *)
	{	// check for integer overflow
		if (_Count <= 0)
			_Count = 0;
		else if (((_SIZT)(-1) / _Count) < sizeof (_Ty))
			throw std::bad_alloc();

		// allocate storage for _Count elements of type _Ty
		return ((_Ty _FARQ *)CXMLIOHeapManager::GetInstance().
			Alloc(_Count * sizeof (_Ty)));
	}

	// TEMPLATE CLASS _Allocator
	template<typename _Ty>
	class _Allocator
		: public _Allocator_base<_Ty>
	{	// generic _Allocator for objects of class _Ty
	public:
		typedef _Ty nodetype;
		typedef _Allocator_base<nodetype> _Mybase;
		typedef typename _Mybase::value_type value_type;
		typedef value_type _FARQ *pointer;
		typedef value_type _FARQ& reference;
		typedef const value_type _FARQ *const_pointer;
		typedef const value_type _FARQ& const_reference;

		typedef _SIZT size_type;
		typedef _PDFT difference_type;

		template<class _Other>
		struct rebind
		{	// convert an _Allocator<_Ty> to an _Allocator <_Other>
			typedef _Allocator<_Other> other;
		};

		pointer address(reference _Val) const
		{	// return address of mutable _Val
			return (&_Val);
		}

		const_pointer address(const_reference _Val) const
		{	// return address of nonmutable _Val
			return (&_Val);
		}

		_Allocator() _THROW0()
		{	// construct default _Allocator (do nothing)
		}

		_Allocator(const _Allocator<nodetype>&) _THROW0()
		{	// construct by copying (do nothing)
		}

		template<class _Other>
		_Allocator(const _Allocator<_Other>&) _THROW0()
		{	// construct from a related _Allocator (do nothing)
		}

		template<class _Other>
		_Allocator<nodetype>& operator=(const _Allocator<_Other>&)
		{	// assign from a related _Allocator (do nothing)
			return (*this);
		}

		void deallocate(pointer _Ptr, size_type)
		{
			// deallocate object at _Ptr, ignore size

			//::operator delete(_Ptr);

			// 使用下面的方法代替::operator delete
			CXMLIOHeapManager::GetInstance().Free( _Ptr );
		}

		pointer allocate(size_type _Count)
		{	// allocate array of _Count elements

			// 用下面的方法代替_Allocate()
			return (_Allocate2(_Count, (pointer)0));
		}

		pointer allocate(size_type _Count, const void _FARQ *)
		{	// allocate array of _Count elements, ignore hint
			return (allocate(_Count));
		}

		void construct(pointer _Ptr, const nodetype& _Val)
		{	// construct object at _Ptr with value _Val
			_Construct(_Ptr, _Val);
		}

		void destroy(pointer _Ptr)
		{	// destroy object at _Ptr
			_Destroy(_Ptr);
		}

		_SIZT max_size() const _THROW0()
		{	// estimate maximum array size
			_SIZT _Count = (_SIZT)(-1) / sizeof (nodetype);
			return (0 < _Count ? _Count : 1);
		}
	};

} // namespace std;

class CXML2Binary
{
private:
	struct BSTR_Less : public std::binary_function<BSTR, BSTR, bool>
	{
	public:
		bool operator()(const BSTR& _Left, const BSTR& _Right) const
		{	// apply operator< to operands
			return wcscmp( _Left, _Right ) < 0;
		}
	};

	typedef std::pair<const BSTR, size_t> _PairCBUI;
	typedef std::_Allocator<_PairCBUI> myallocator;

	class CMemoryOut;

	class CStringMap : public std::map<BSTR, size_t, BSTR_Less, myallocator >
	{
	public:
		void CalcOrdinal();
		void DumpItems( CMemoryOut& mo ) const;
		size_t ItemIndex( BSTR bstrKey ) const;
		~CStringMap();
	};
	CStringMap		m_map;

public:
	CXML2Binary()
	{
	}

	~CXML2Binary()
	{
	}

private:
	static DOMNodeType GetNodeType( IXMLDOMNode* node );
	static BSTR GetText(IXMLDOMNode* node);	

private:
	void InsertToStringMap( BSTR bstr);
	void MakeNodeAttributesStringMap( IXMLDOMNode* node);
	void MakeNodeChildrenStringMap( IXMLDOMNode* node);
	void MakeNodeStringMap( IXMLDOMNode* node);
	void MakeStringOrdinal()
	{
		// 计算字典序号
		m_map.CalcOrdinal();
	}
	size_t GetStringIndex( BSTR bstr )
	{
		return m_map.ItemIndex( bstr );
	}

private:
	void OutputDictionary( CMemoryOut& mo);
	void OutputNode( IXMLDOMNode* pNode, CMemoryOut& mo);
	void OutputAttributes( IXMLDOMNode* pNode, CMemoryOut& mo);
	void OutputChildNodes( IXMLDOMNode* pNode, CMemoryOut& mo);

public:
	bool Translate(CComPtr<IXMLDOMDocument> fr, PFS::CDataBlock& data);
};
