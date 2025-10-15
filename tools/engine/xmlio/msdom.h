#pragma once

#import "msxml3.dll"


#if _MSC_FULL_VER >= 140050320
#define _XMLIO_DEPRECATE_TEXT(_Text) __declspec(deprecated(_Text))
#else
#define _XMLIO_DEPRECATE_TEXT(_Text) __declspec(deprecated)
#endif

#ifndef XMLIO_PERFORMANCE_DEPRECATED_TEXT
#define XMLIO_PERFORMANCE_DEPRECATED_TEXT(_Text) _XMLIO_DEPRECATE_TEXT(_Text)
#endif

#ifdef XMLIO_PERFORMANCE_NO_WARNINGS
#define XMLIO_PERFORMANCE_DEPRECATED(_Replacement)
#else
#define XMLIO_PERFORMANCE_DEPRECATED(_Replacement) XMLIO_PERFORMANCE_DEPRECATED_TEXT("This function has performance issues or not recomended to use, and would be removed in future. Consider using " #_Replacement " instead. To disable deprecation, use XMLIO_PERFORMANCE_NO_WARNINGS. Contact XMLIO developers for details.")
#endif


namespace MSXMLDOM
{

using PFS::fsize_t;
using PFS::foff_t;

const wchar_t UNICODE_WINDOWS_FILE_PREFIX = 0xFEFF;

enum
{
	EC_SUCCESS						= 0,
	EC_MSXMLDOM_CREATE_DOCUMENT		= -1,
	EC_MSXMLDOM_PARSE_XML_CONTEXT	= -2,
	EC_MSXMLDOM_XML_ENCODE			= -3,

	EC_MSXMLDOM_COM_ERROR			= -10,
};

class CMSComServerScoped
{
public:
	CMSComServerScoped() { CoInitialize( NULL); }
	~CMSComServerScoped() { CoUninitialize(); }
};
class CReadNode;
typedef std::vector<CReadNode> CReadNodeList;
typedef std::vector<std::wstring> CAttributesNames;

class CReadNode
{
	friend class CReader;
	CMSComServerScoped		m_scoped;

	// 允许子类访问以扩展该类的实现接口
	CComPtr<IXMLDOMNode>				m_Node;
	mutable CComPtr<IXMLDOMNodeList>	m_NodeList;

public:
	CReadNode(){}
	~CReadNode(){}
	CReadNode( const CReadNode& src)
		: m_Node( src.m_Node)
		, m_NodeList( src.m_NodeList) {}
	CReadNode& operator=( const CReadNode& src)
	{
		if( this == &src)
			return *this;
		 m_Node = src.m_Node;
		 m_NodeList = src.m_NodeList;
		return *this;
	}

	explicit CReadNode( CComPtr<IXMLDOMNode> Node)
	{
		m_Node = Node;
 	}

	CComPtr<IXMLDOMNode> GetInterface() const
	{
		return m_Node;
	}

private:
	void CheckNodeList() const
	{
		if( m_NodeList != NULL)
			return;
		if( m_Node == NULL)
			return;
		CComPtr<IXMLDOMElement>		EleChild;
		HRESULT hr = m_Node->QueryInterface( __uuidof(IXMLDOMElement), (void**)&EleChild);
		if( FAILED( hr) || EleChild == NULL)
			return;
		EleChild->get_childNodes( &m_NodeList);
	}

public:
	DOMNodeType GetType() const
	{
		DOMNodeType			domnt;
		if( m_Node == NULL)
			return NODE_INVALID;
		HRESULT hr = m_Node->get_nodeType( &domnt);
		if( FAILED( hr))
			return NODE_INVALID;
		else
			return domnt;
	}

	size_t GetChildrenCount() const
	{
		CheckNodeList();
		if( m_NodeList == NULL)
			return 0;
		long lCount = 0;
		HRESULT hr = m_NodeList->get_length( &lCount);
		if( FAILED( hr))
			lCount = 0;
		return lCount;
	}

	bool GetChildAt( size_t index, CReadNode& rChild) const
	{
		CheckNodeList();
		if( m_NodeList == NULL)
			return false;
		rChild.m_Node.Release();
		HRESULT hr = m_NodeList->get_item( (long)index, &rChild.m_Node);
		return SUCCEEDED(hr);
	}

	bool GetChildren(  CComPtr<IXMLDOMNodeList>& rChildren ) const
	{
		CheckNodeList();
		if( m_NodeList == NULL)
			return false;
		rChildren = m_NodeList;
		return true;
	}

	void GetAttributeNames( CAttributesNames& names) const
	{
		if( m_Node == NULL)
			return;
		CComPtr<IXMLDOMNamedNodeMap>		NodeMap;
		HRESULT hr = m_Node->get_attributes( &NodeMap);
		if( FAILED( hr) || NodeMap == NULL)
			return;

		long	lCount = 0;
		hr = NodeMap->get_length( &lCount);
		if( FAILED( hr) || 0 == lCount)
			return;
		names.reserve( lCount);
		CComPtr<IXMLDOMNode> Node;
		for( long i = 0; i < lCount; i ++)
		{
			Node.Release();
			hr = NodeMap->get_item( i, &Node);
			if( FAILED( hr) || Node == NULL)
				continue;
			DOMNodeType		domnt;
			hr = Node->get_nodeType( &domnt);
			if( FAILED( hr) || NODE_ATTRIBUTE != domnt)
				continue;
			CComBSTR	bstrName;
			hr = Node->get_nodeName( &bstrName);
			if( SUCCEEDED( hr))
				names.push_back( std::wstring( bstrName.m_str));
		}
	}

	bool GetAttribute( const std::wstring& name, std::wstring& value) const
	{
		return GetAttribute( name.c_str(), value);
	}

	bool GetAttribute( const wchar_t* pszName, std::wstring& value) const
	{
		assert ( pszName != NULL );
		if( m_Node == NULL)
			return false;
		CComPtr<IXMLDOMElement>		EleChild;
		HRESULT hr = m_Node->QueryInterface( __uuidof(IXMLDOMElement), (void**)&EleChild);
		if( FAILED( hr) || EleChild == NULL)
			return false;
		CComVariant		variant;
		hr = EleChild->getAttribute( CComBSTR(pszName), &variant);
		if( FAILED( hr) || VT_BSTR != variant.vt)
			return false;
		value = variant.bstrVal;
		return true;
	}

	XMLIO_PERFORMANCE_DEPRECATED(bool GetAttribute( const wchar_t* pszName, std::wstring& value) const)
	std::wstring GetAttribute( const std::wstring& name) const
	{
		std::wstring	value;
		GetAttribute( name, value);
		return value;
	}

	bool GetText( std::wstring& value) const
	{
		if( m_Node == NULL)
			return false;
		CComVariant		variant;
		DOMNodeType nt = GetType();

		// 只有下面几个XML节点支持nodeValue属性
		if( nt == NODE_ATTRIBUTE ||
			nt == NODE_CDATA_SECTION ||
			nt == NODE_TEXT || 
			nt == NODE_PROCESSING_INSTRUCTION || 
			nt == NODE_COMMENT )
		{
			HRESULT hr = m_Node->get_nodeValue( &variant);
			if( FAILED( hr) || VT_BSTR != variant.vt)
				return false;
			value = variant.bstrVal;
			return true;
		}
		return false;
	}

	XMLIO_PERFORMANCE_DEPRECATED(bool GetText( std::wstring& value) const)
	std::wstring GetText() const
	{
		std::wstring value;
		GetText( value);
		return value;
	}

	bool GetName( std::wstring& value) const
	{
		if( m_Node == NULL)
			return false;
		CComBSTR		name;
		HRESULT hr = m_Node->get_nodeName( &name);
		if( FAILED( hr))
			return false;
		value = name.m_str;
		return true;
	}

	XMLIO_PERFORMANCE_DEPRECATED(bool GetName( std::wstring& value) const)
	std::wstring GetName() const
	{
		std::wstring value;
		GetName( value);
		return value;
	}

public:
	int SelectNodes(const std::wstring& queryString, MSXMLDOM::CReadNodeList& nl)
	{
		CComPtr<IXMLDOMNodeList> nodeList;
		HRESULT hr = m_Node->selectNodes( (BSTR)queryString.c_str(), &nodeList ) ;
		if( SUCCEEDED( hr ) )
		{
			long listLength = 0;
			nodeList->get_length( &listLength);
			nl.resize(listLength);
			for ( int index = 0; index < listLength; index ++ )
			{
				nl[index].m_Node.Release();
				nodeList->get_item( index, &nl[index].m_Node);
			}
		}

		if ( hr == 0 )
			return EC_SUCCESS;
		else
			return EC_MSXMLDOM_COM_ERROR;
	}

	int SelectSingleNode(const std::wstring& queryString, CReadNode& node )
	{
		node.m_Node.Release();
		HRESULT hr = m_Node->selectSingleNode( (BSTR) queryString.c_str(), &node.m_Node );
		if ( hr == 0 )
			return EC_SUCCESS;
		else
			return EC_MSXMLDOM_COM_ERROR;
	}
};

class IStreamOnDataBlock : public IStream
{
private:
	const void*		m_pData;
	LARGE_INTEGER	m_size;
	LARGE_INTEGER	m_offset;
	volatile LONG	m_lRef;

public:
	IStreamOnDataBlock( const PFS::CDataBlock& rDataBlock )
		: m_pData(rDataBlock.GetData())
	{
		m_size.QuadPart = rDataBlock.GetSize();
		m_offset.QuadPart = 0;
		m_lRef = 1;
	}

	IStreamOnDataBlock( const void* pData, size_t size )
		: m_pData(pData)
	{
		m_size.QuadPart = size;
		m_offset.QuadPart = 0;
		m_lRef = 1;
	}

public:
	virtual HRESULT STDMETHODCALLTYPE QueryInterface( 
		/* [in] */ REFIID riid,
		/* [iid_is][out] */ void __RPC_FAR *__RPC_FAR *ppvObject)
	{
		if( IsEqualGUID( riid ,IID_IUnknown ) )
		{
			*ppvObject = (IUnknown*)this;
			AddRef();
			return S_OK;
		}
		else if( IsEqualGUID( riid , IID_IStream ) )
		{
			*ppvObject = (IStream*)this;
			AddRef();
			return S_OK;
		}
		
		return E_NOINTERFACE;
	}

	virtual ULONG STDMETHODCALLTYPE AddRef( void)
	{
		return InterlockedIncrement(&m_lRef);
	}

	virtual ULONG STDMETHODCALLTYPE Release( void)
	{	
		if ( InterlockedDecrement(&m_lRef) == 0 )
		{
			delete this;
			return 0;
		}

		return m_lRef;
	}

public:
	// ISequentialStream methods
	virtual /* [local] */ HRESULT STDMETHODCALLTYPE Read( 
		/* [length_is][size_is][out] */ void *pv,
		/* [in] */ ULONG cb,
		/* [out] */ ULONG *pcbRead)
	{
		if ( pv == NULL )
		{
			return E_INVALIDARG;
		}

		if( cb == 0 )
		{
			return S_FALSE;
		}

		ULONG cbCanRead = (ULONG)(m_size.QuadPart - m_offset.QuadPart);
		if ( cbCanRead > cb )
		{
			cbCanRead = cb;
		}

		memcpy( pv, (unsigned char*)m_pData + m_offset.QuadPart, cbCanRead);
		if ( pcbRead != NULL )
		{
			*pcbRead = cbCanRead;
		}
		
		m_offset.QuadPart += cbCanRead;

		return cbCanRead ? S_OK : S_FALSE;
	}

	virtual /* [local] */ HRESULT STDMETHODCALLTYPE Write( 
		/* [size_is][in] */ const void *pv,
		/* [in] */ ULONG cb,
		/* [out] */ ULONG *pcbWritten)
	{
		return E_NOTIMPL;
	}

public:
	virtual /* [local] */ HRESULT STDMETHODCALLTYPE Seek( 
		/* [in] */ LARGE_INTEGER dlibMove,
		/* [in] */ DWORD dwOrigin,
		/* [out] */ ULARGE_INTEGER *plibNewPosition)
	{
		LARGE_INTEGER _current = m_offset;
		switch( dwOrigin )
		{
		case STREAM_SEEK_SET:
			_current = dlibMove;
			break;
		case STREAM_SEEK_CUR:
			_current.QuadPart += dlibMove.QuadPart;
			break;
		case STREAM_SEEK_END:
			_current.QuadPart = m_size.QuadPart - dlibMove.QuadPart;
			break;
		default:
			return STG_E_INVALIDFUNCTION;
		}
		
		if( dlibMove.QuadPart < 0 )
		{
			return HRESULT_FROM_WIN32(ERROR_NEGATIVE_SEEK);
		}
		else if ( dlibMove.QuadPart > m_size.QuadPart )
		{
			return STG_E_SEEKERROR;
		}
		else
		{
			m_offset = _current;
		}

		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE SetSize( 
		/* [in] */ ULARGE_INTEGER libNewSize)
	{
		return E_NOTIMPL;
	}

	virtual /* [local] */ HRESULT STDMETHODCALLTYPE CopyTo( 
		/* [unique][in] */ IStream *pstm,
		/* [in] */ ULARGE_INTEGER cb,
		/* [out] */ ULARGE_INTEGER *pcbRead,
		/* [out] */ ULARGE_INTEGER *pcbWritten)
	{
		return E_NOTIMPL;
	}

	virtual HRESULT STDMETHODCALLTYPE Commit( 
		/* [in] */ DWORD grfCommitFlags)
	{
		return E_NOTIMPL;
	}

	virtual HRESULT STDMETHODCALLTYPE Revert( void)
	{
		return E_NOTIMPL;
	}

	virtual HRESULT STDMETHODCALLTYPE LockRegion( 
		/* [in] */ ULARGE_INTEGER libOffset,
		/* [in] */ ULARGE_INTEGER cb,
		/* [in] */ DWORD dwLockType)
	{
		return E_NOTIMPL;
	}

	virtual HRESULT STDMETHODCALLTYPE UnlockRegion( 
		/* [in] */ ULARGE_INTEGER libOffset,
		/* [in] */ ULARGE_INTEGER cb,
		/* [in] */ DWORD dwLockType)
	{
		return E_NOTIMPL;
	}

	virtual HRESULT STDMETHODCALLTYPE Stat( 
		/* [out] */ STATSTG *pstatstg,
		/* [in] */ DWORD grfStatFlag)
	{
		return E_NOTIMPL;
	}

	virtual HRESULT STDMETHODCALLTYPE Clone( 
		/* [out] */ IStream **ppstm)
	{
		return E_NOTIMPL;
	}
};

inline IStream* CreateIStreamOnDataBlock( const PFS::CDataBlock& rDataBlock )
{
	IStreamOnDataBlock* pObj = new IStreamOnDataBlock( rDataBlock );
	return pObj;
}

inline IStream* CreateIStreamOnDataBuffer( const void* pData, size_t size)
{
	IStreamOnDataBlock* pObj = new IStreamOnDataBlock( pData, size );
	return pObj;
}

class CReader
{
	CMSComServerScoped			m_scoped;
	CComPtr<IXMLDOMDocument>	m_Document;
public:
	CReader() {}
	~CReader()
	{
		Close();
	}
	CReader( const CReader& src)
		: m_Document( src.m_Document)
	{}
	CReader& operator = ( const CReader& src)
	{
		if( this == &src)
			return *this;
		m_Document = src.m_Document;
		return *this;
	}

	explicit CReader( CComPtr<IXMLDOMDocument> doc)
	{
		m_Document = doc;
	}

	CComPtr<IXMLDOMDocument> GetInterface() const
	{
		return m_Document;
	}
private:
	static BSTR TryReadUniCodeStringFromMemory( PFS::CDataBlock& buffer) 
	{
		BSTR bstrText = (BSTR)(const char*)buffer.GetData();
		if( UNICODE_WINDOWS_FILE_PREFIX == bstrText[0])
		{
			bstrText ++;
			return bstrText;
		}
		else
		{
			const char*	szSrc = (const char*)buffer.GetData();// 使用const转换可避免不必要的内存拷贝!
			int nResult = MultiByteToWideChar( CP_ACP, 0, szSrc, -1, NULL, NULL);
			if( 0 == nResult)
				return NULL;

			PFS::CDataBlock	temp;
			temp.Resize( sizeof(wchar_t) * ( nResult + 1) );
			wchar_t* wsBuffer= (wchar_t*)temp.GetData();
			nResult = MultiByteToWideChar( CP_ACP, 0, szSrc, -1, wsBuffer, nResult);
			if( 0 == nResult)
				return NULL;
			wsBuffer[nResult] = 0;

			buffer.Swap( temp);
			return (BSTR)buffer.GetData();
		}
	}

private:
	long _TraceXMLDOMError()
	{
		IXMLDOMParseErrorPtr parseError;
		HRESULT hr = m_Document->get_parseError( &parseError );
		if ( SUCCEEDED(hr) )
		{
			BSTR bstrReason;
			BSTR bstrSrcText;
			long errorCode;
			long line, linepos, filePos;
			parseError->get_reason( &bstrReason );
			parseError->get_errorCode( &errorCode );
			parseError->get_line( &line );
			parseError->get_linepos( &linepos );
			parseError->get_filepos( &filePos );
			parseError->get_srcText( &bstrSrcText );

			OutputDebugStringW( bstrReason ); // reason 中带有换行符
			if( bstrSrcText != NULL && bstrSrcText[0] != NULL )
			{
				OutputDebugStringW( bstrSrcText );
				OutputDebugStringW( L"\r\n" ); // SrcText没有回车换行符,这里补充之
			}

			SysFreeString( bstrReason );
			SysFreeString( bstrSrcText );

			return errorCode;
		}

		return hr;
	}

	int Open( BSTR bstrText )
	{
		assert( NULL != bstrText );
		HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL, 
			CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&m_Document);
		if( FAILED( hr))
			return EC_MSXMLDOM_CREATE_DOCUMENT;
		VARIANT_BOOL bResult;
		hr = m_Document->loadXML( bstrText, &bResult);
		if( FAILED( hr) )
			return EC_MSXMLDOM_PARSE_XML_CONTEXT;
		if( bResult == VARIANT_FALSE )
		{
			return _TraceXMLDOMError();
		}

		return EC_SUCCESS;
	}

	// 基于IStream打开XML文件, 这种方式可以处理非UTF16编码的文件
	// 注: IStream的Read()方法不要在数据尾部补充结尾的0,否则会导致XML顶部元素无效的错误.
	// XML_E_INVALIDATROOTLEVEL : 0xC00CE556
	// Invalid at the top level of the document.
	// [5/19/2009 yanghaibo]
	int Open( IStream* pStream )
	{
		assert( NULL != pStream );
		HRESULT hr = CoCreateInstance(CLSID_DOMDocument, NULL,
			CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&m_Document);
		if( FAILED( hr))
			return EC_MSXMLDOM_CREATE_DOCUMENT;
		VARIANT_BOOL bResult;
		CComVariant varSource( pStream );
		//m_Document->put_async( VARIANT_TRUE);
		m_Document->put_async( VARIANT_FALSE);
		m_Document->put_resolveExternals( VARIANT_FALSE );
		m_Document->put_validateOnParse( VARIANT_FALSE );
		hr = m_Document->load(varSource, &bResult);
		if( FAILED( hr) )
			return EC_MSXMLDOM_PARSE_XML_CONTEXT;
		if( bResult == VARIANT_FALSE )
		{
			return _TraceXMLDOMError();
		}

		return EC_SUCCESS;
	}

	// 调用方需保证data是以0为结尾的字符串
	int Open( PFS::CDataBlock& data)
	{	
		BSTR bstrText = TryReadUniCodeStringFromMemory( data);
		if( NULL == bstrText)
			return ::GetLastError();
		return Open(bstrText);
	}

public:
	// 解析UTF16LE编码的XML数据，不能有任何BOM前缀
	int OpenString( const wchar_t* xml )
	{
		return Open( CComBSTR(xml) );
	}

	// 解析XML数据，数据需要有完整的BOM前缀
	// 对于UTF16LE编码的XML数据，需要有0xFF 0xFE前缀(0xFEFF)
	// 对于UTF16BE编码的XML数据，需要有0xFE 0xFF前缀(0xFFFE)
	// 对于UTF8编码的XML数据，需要有0xEF 0xBB 0xBF
	// data 不能以0x0000结尾
	int OpenData( const void* data, size_t size)
	{
		if( size <= 2 )
		{
			// treat it as error
			return EC_MSXMLDOM_PARSE_XML_CONTEXT;
		}
		else
		{
			// 以IStream方式读取data数据时，data 不能以0x0000结尾
			IStream* pStream = CreateIStreamOnDataBuffer( data, size );
			if ( pStream != NULL )
			{
				int iRet = Open( pStream );
				pStream->Release();
				return iRet;
			}

			// data 可能以0x0000结尾，也可能不以0x0000结尾
			const byte* pBuffer = (const byte*)data;
			if( pBuffer[size-2] == 0 && pBuffer[size-1] == 0 )
			{
				BSTR bstrText = (BSTR)pBuffer;
				if( UNICODE_WINDOWS_FILE_PREFIX == bstrText[0] )
				{
					return Open(bstrText + 1);
				}
				else
				{
					PFS::CDataBlock buffer( size );
					memcpy( buffer.GetData(), data, size);
					return Open( buffer);
				}
			}
			else
			{
				//  如果 data 不以0x0000结尾,需拷贝输入并追加结尾0
				PFS::CDataBlock buffer( size + sizeof(wchar_t));
				memcpy( buffer.GetData(), data, size);
				{
					char* temp = (char*)buffer.GetData();
					temp[size] = 0;
					temp[size + 1] = 0;
				}
				return Open( buffer);
			}
		}
	}

	// 解析XML数据，数据需要有完整的BOM前缀
	// 对于UTF16LE编码的XML数据，需要有0xFF 0xFE前缀(0xFEFF)
	// 对于UTF16BE编码的XML数据，需要有0xFE 0xFF前缀(0xFFFE)
	// 对于UTF8编码的XML数据，需要有0xEF 0xBB 0xBF
	// data 不能以0x0000结尾
	int OpenData( const PFS::CDataBlock& data )
	{
		size_t size = data.GetSize();
		// 无效数据
		if( size <= 2 ) 
		{
			return EC_MSXMLDOM_PARSE_XML_CONTEXT;
		}
		else
		{
			// 首先尝试以IStream方式打开
			// 以IStream方式读取data数据时，data 不能以0x0000结尾
			IStream* pStream = CreateIStreamOnDataBlock( data );
			if( pStream != NULL )
			{
				int iRet = Open( pStream );
				pStream->Release();
				return iRet;
			}

			// data 可能以0x0000结尾，也可能不以0x0000结尾
			const byte* pBuffer = (const byte*)data.GetData();
			if( pBuffer[size-2] == 0 && pBuffer[size-1] == 0 )
			{
				// 如果 data 以0x0000结尾，无需拷贝输入并追加结尾0
				// 如下的拷贝只增加引用计数
				PFS::CDataBlock buffer( data );
				return Open( buffer);
			}
			else
			{
				//  如果 data 不以0x0000结尾,需拷贝输入并追加结尾0
				PFS::CDataBlock buffer( data );
				// 这里的Resize内部重新分配内存并执行拷贝
				buffer.Resize( size + 2); 
				{
					char* temp = (char*)buffer.GetData();
					temp[size] = 0;
					temp[size + 1] = 0;
				}

				return Open( buffer);
			}
		}
	}

	int OpenFile( PFS::CBaseFile& bf )
	{
		fsize_t fullsize = bf.GetSize();
		if( fullsize == 0 || fullsize > MAXDWORD )
		{
			// treat it as error
			return EC_MSXMLDOM_PARSE_XML_CONTEXT;
		}
		else
		{
			PFS::CImageData imageData = bf.GetImage();
			// 使用IStream方式ParseXML
			return OpenData( imageData.GetData(), (size_t)imageData.GetSize() );
		}
	}

	// XMLIO_PERFORMANCE_DEPRECATED
	int OpenNativeFile( const std::wstring& filename )
	{	
		return OpenNativeFile( filename.c_str() );
	}

	int OpenNativeFile( const wchar_t* pszFileName )
	{
		HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL,
			CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&m_Document);
		if( FAILED( hr))
			return EC_MSXMLDOM_CREATE_DOCUMENT;

		VARIANT_BOOL bResult;
		hr = m_Document->load( CComVariant( pszFileName ), &bResult);
		if( FAILED( hr) || !bResult)
			return EC_MSXMLDOM_PARSE_XML_CONTEXT;
		return EC_SUCCESS;
	}

	void Close()
	{
		m_Document.Release();
	}

	bool GetRootNode( CReadNode& node) const
	{
		if( m_Document == NULL)
			return false;
		CComPtr<IXMLDOMElement>	RootEle;
		HRESULT hr = m_Document->get_documentElement( &RootEle);
		if( FAILED( hr) || RootEle == NULL)
			return false;
		hr = RootEle->QueryInterface( IID_IXMLDOMNode, (void**)&(node.m_Node));
		return ( SUCCEEDED( hr) && node.m_Node != NULL);
	}

	XMLIO_PERFORMANCE_DEPRECATED(bool GetRootNode( CReadNode& node) const)
	CReadNode GetRootNode() const
	{
		CReadNode		node;
		GetRootNode( node);
		return node;
	}
};

class CWriter;
class CWriterNode
{
	friend class CWriter;
	CWriter*					m_Writer;
	int							m_level;
	CComPtr<IXMLDOMElement>		m_node;
public:
	CWriterNode() : m_Writer( NULL), m_level( 0)  {}
	CWriterNode( CWriter* w) : m_Writer( w), m_level( 0)  {}
	~CWriterNode(){}
	CWriterNode( const CWriterNode& src)
		: m_Writer( src.m_Writer), m_level( src.m_level), m_node( src.m_node) {}
	CWriterNode& operator = ( const CWriterNode& src)
	{
		if( this == &src)
			return *this;
		assert( NULL == m_Writer || src.m_Writer == m_Writer);
		m_Writer = src.m_Writer;
		m_node = src.m_node;
		m_level = src.m_level;
		return *this;
	}

	// Special construct
	bool Construct( CWriter* writer, CComPtr<IXMLDOMElement>& rNode, int level)
	{
		assert( m_Writer == NULL || m_Writer == writer );
		assert( writer != NULL );
		m_Writer = writer;
		m_node = rNode;
		m_level = level;
		return m_Writer != NULL;
	}

public:
	bool SetAttribute( const wchar_t* pszName, const wchar_t* pszValue)
	{
		assert( pszName != NULL );
		assert( pszValue != NULL );
		if( m_node == NULL)
			return false;
		HRESULT hr = m_node->setAttribute( CComBSTR(pszName), CComVariant(pszValue));
		return SUCCEEDED( hr);
	}

	bool CreateSubElement( const wchar_t* pszName, CWriterNode& rNode);

	bool InsertText( const wchar_t* pszText);

	bool InsertCData( const wchar_t* pszCData);

public:
	CWriter* GetWriter() { return m_Writer; }
};

class CWriter
{
	CMSComServerScoped			m_scoped;
	CComPtr<IXMLDOMDocument>	m_Document;
	CComPtr<IXMLDOMElement>		m_RootElement;
public:
	CWriter() {}
	~CWriter() { }
	CWriter( const CWriter& src)
		: m_Document( src.m_Document)
		, m_RootElement( src.m_RootElement) {}
	CWriter& operator=( const CWriter& src)
	{
		if( this == &src)
			return *this;
		m_Document = src.m_Document;
		m_RootElement = src.m_RootElement;
		return *this;
	}
	CComPtr<IXMLDOMDocument> GetInterface() const
	{
		return m_Document;
	}

	// 从已有的XMLDocument接口构造对象
	bool Construct( CComPtr<IXMLDOMDocument>& spXMLDocument )
	{
		if( spXMLDocument == NULL )
		{
			return false;
		}

		m_RootElement = NULL;

		HRESULT hr = spXMLDocument->get_documentElement( &m_RootElement);
		if( FAILED( hr) || m_RootElement == NULL)
			return false;
		
		m_Document = spXMLDocument;

		return true;
	}

private:
	void CheckDocument()
	{
		if( m_RootElement != NULL)
			return;
		if( m_Document == NULL)
		{
			HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL,
				CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&m_Document);
			if( FAILED( hr) || m_Document == NULL)
				return;
		}
		HRESULT hr = m_Document->createElement( CComBSTR( OLESTR("data") ), &m_RootElement);
		if( FAILED( hr) || m_RootElement == NULL)
			return;
		m_Document->putref_documentElement( m_RootElement);
	}

	void OutXMLLine( CWriterNode& node, int level)
	{
		std::wstring strtext = L"\r\n";

		for( int i = 0; i < level; i ++)
			strtext += L"\t";

		node.InsertText( strtext.c_str() );
	}

public:
	bool GetRootNode( CWriterNode& node)
	{
		CheckDocument();
		node.m_Writer = this;
		node.m_node = m_RootElement;
		return m_RootElement != NULL;
	}

	bool CreateElement( CWriterNode& parent, const wchar_t* pszName, CWriterNode& rNode)
	{
		assert( this == parent.m_Writer);
		
		OutXMLLine( parent, parent.m_level + 1);

		if( parent.m_node == NULL)
			return false;

		CComPtr<IXMLDOMElement> Element;
		HRESULT hr = m_Document->createElement( CComBSTR(pszName) , &Element);
		if( FAILED( hr) || Element == NULL)
			return false;
		hr = parent.m_node->appendChild( Element, NULL);
		if( FAILED( hr))
			return false;
		
		if( !rNode.Construct( this, Element, parent.m_level + 1) )
		{
			return false;
		}

		OutXMLLine( parent, parent.m_level);

		return true;
	}

	bool InsertText( CWriterNode& node, const wchar_t* pszText)
	{
		assert( pszText != NULL );
		if( node.m_node == NULL)
			return false;
		CComPtr<IXMLDOMNode>		TextNode;
		HRESULT hr = m_Document->createNode( CComVariant( NODE_TEXT), NULL, NULL, &TextNode);
		if( FAILED( hr) || TextNode == NULL)
			return false;
		hr = TextNode->put_nodeValue( CComVariant( pszText));
		if( FAILED( hr))
			return false;
		hr = node.m_node->appendChild( TextNode, NULL);
		if( FAILED( hr))
			return false;
		return true;
	}

	bool InsertCData( CWriterNode& node, const wchar_t* pszCData)
	{
		assert( pszCData != NULL );
		if( node.m_node == NULL)
			return false;
		CComPtr<IXMLDOMNode>		DataNode;
		HRESULT hr = m_Document->createNode( CComVariant( NODE_CDATA_SECTION), NULL, NULL, &DataNode);
		if( FAILED( hr) || DataNode == NULL)
			return false;
		hr = DataNode->put_nodeValue( CComVariant( pszCData));
		if( FAILED( hr))
			return false;
		hr = node.m_node->appendChild( DataNode, NULL);
		if( FAILED( hr))
			return false;
		return true;
	}

	int Save( const std::wstring& filename, int iAction = 0 )
	{
		PFS::CFile		file;
		if( file.Open( filename, PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY))
			return Save( file, iAction);
		else
			return PFS::CEnv::GetLastError();
	}

	int SaveNative( const std::wstring& filename )
	{
		PFSX::CNativeFile		file;
		if( file.Open( filename, PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY))
			return Save( file);
		else
			return PFS::CEnv::GetLastError();
	}

	int Save( PFS::CBaseFile& bf, int iAction = 0 )
	{
		if( PFS::FILE_SIZE_ERROR == bf.Write( &UNICODE_WINDOWS_FILE_PREFIX, sizeof(wchar_t)))
			return PFS::CEnv::GetLastError();
		CComBSTR		text;
		HRESULT hr = m_Document->get_xml( &text);
		if( FAILED( hr))
			return EC_MSXMLDOM_XML_ENCODE;
		if (iAction == 1)
		{
			::SysFreeString(text.m_str);
			LPCOLESTR strTemp = L"<data>\n</data>";
			text.m_str = ::SysAllocString(strTemp);
		}
		else if(iAction == 2)
		{
			::SysFreeString(text.m_str);
			LPCOLESTR strTemp = L"<data>\n</data>";
			//LPCOLESTR strTemp = L"<data>\n	<body name=\"bodyonly\"/>\n </data>";
			text.m_str = ::SysAllocString(strTemp);
		}

		if( PFS::FILE_SIZE_ERROR == bf.Write( text.m_str, text.ByteLength()))
			return PFS::CEnv::GetLastError();
		return EC_SUCCESS;
	}

	// 保存UTF16LE格式(BSTR)的XML数据，没有BOM前缀，没有L'\0'结尾
	int SaveData( PFS::CDataBlock& data )
	{
		CComBSTR		text;
		HRESULT hr = m_Document->get_xml( &text);
		if( FAILED( hr))
			return EC_MSXMLDOM_XML_ENCODE;
		data.Assign( text.m_str, text.ByteLength());
		return EC_SUCCESS;
	}

	// 保存UTF16LE格式(BSTR)的XML数据，有BOM前缀，没有L'\0'结尾
	int SaveDataWithBOM( PFS::CDataBlock& data )
	{
		CComBSTR		text;
		HRESULT hr = m_Document->get_xml( &text );
		if( FAILED( hr))
			return EC_MSXMLDOM_XML_ENCODE;
		data.Clear();
		data.Append( &UNICODE_WINDOWS_FILE_PREFIX, sizeof(wchar_t) );
		data.Append( text.m_str, text.ByteLength() );
		return EC_SUCCESS;
	}

	// 保存UTF16LE格式(BSTR)的XML字符串，没有BOM前缀，有L'\0'结尾
	int SaveAsString( std::wstring& rXMLString )
	{
		CComBSTR		text;
		HRESULT hr = m_Document->get_xml( &text);
		if( FAILED( hr))
			return EC_MSXMLDOM_XML_ENCODE;
		// 注意：BSTR和wstring都是双字节字符串，所以，用Length()获取结果，而不用ByteLength().
		rXMLString.assign( text.m_str, text.Length() );
		return EC_SUCCESS;
	}
};

inline bool CWriterNode::CreateSubElement( const wchar_t* pszName, CWriterNode& rNode)
{
	assert( m_Writer);
	return m_Writer->CreateElement( *this, pszName, rNode );
}

inline bool CWriterNode::InsertText( const wchar_t* pszText)
{
	assert( m_Writer);
	assert( pszText != NULL );
	return m_Writer->InsertText( *this, pszText);
}

inline bool CWriterNode::InsertCData( const wchar_t* pszCData)
{
	assert( m_Writer);
	assert( pszCData != NULL );
	return m_Writer->InsertCData( *this, pszCData);
}

} // namespace MSXMLDOM {