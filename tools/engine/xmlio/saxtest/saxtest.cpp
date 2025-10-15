// saxtest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <locale.h>
#include <stdio.h>
#include <MsXml2.h>

#include <assert.h>
#include <vector>
#include "../src/octetsiobase.h"


#pragma comment( lib, "msxml2.lib")

#define __STR2WSTR(str)    L##str
#define _STR2WSTR(str)     __STR2WSTR(str)
#define __FILEW__          _STR2WSTR(__FILE__)
#define __FUNCTIONW__      _STR2WSTR(__FUNCTION__)

#ifdef __USE_MSXML2_NAMESPACE__
#define SAXNS MSXML2::
#else
#define SAXNS ::
#endif

class CComInitialize
{
public:
	CComInitialize()
	{
		HRESULT hr = CoInitializeEx( NULL, COINIT_APARTMENTTHREADED );
		if ( !SUCCEEDED(hr) )
		{
			return;
		}
	}

	~CComInitialize()
	{
		CoUninitialize();
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
	//IStreamOnDataBlock( const PFS::CDataBlock& rDataBlock )
	//	: m_pData(rDataBlock.GetData())
	//{
	//	m_size.QuadPart = rDataBlock.GetSize();
	//	m_offset.QuadPart = 0;
	//	m_lRef = 1;
	//}

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
		if( IsEqualGUID( riid, IID_IUnknown ) )
		{
			*ppvObject = (IUnknown*)this;
			AddRef();
			return S_OK;
		}
		else if( IsEqualGUID( riid, IID_IStream ) )
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

inline IStream* CreateIStreamOnDataBuffer( const void* pData, size_t size)
{
	IStreamOnDataBlock* pObj = new IStreamOnDataBlock( pData, size );
	return pObj;
}

class CSAXHandlerAgent 
	: virtual public SAXNS ISAXContentHandler
	, virtual public SAXNS ISAXLexicalHandler
	, virtual public SAXNS ISAXErrorHandler
{
protected:
	ULONG m_ulRefCounter;

	~CSAXHandlerAgent()
	{

	}

public:
	CSAXHandlerAgent()
		: m_ulRefCounter(1)
	{
	}

	virtual HRESULT STDMETHODCALLTYPE QueryInterface( 
		/* [in] */ REFIID riid,
		/* [iid_is][out] */ void __RPC_FAR *__RPC_FAR *ppvObject)
	{	
		// 注意，因为我们的类实现了多接口，下面的转换链都是必须的
		if (riid == IID_IUnknown )
		{
			*ppvObject = reinterpret_cast<void*>((IUnknown*)(ISAXContentHandler*)this);
		}
		else if (riid == IID_ISAXContentHandler )
		{
			*ppvObject = reinterpret_cast<void*>((ISAXContentHandler*)this);
		}
		else if (riid == IID_ISAXLexicalHandler )
		{
			*ppvObject = reinterpret_cast<void*>((ISAXLexicalHandler*)this);
		}
		else if ( riid == IID_ISAXErrorHandler )
		{
			*ppvObject = reinterpret_cast<void*>((ISAXErrorHandler*)this);
		}
		else
		{
			return E_NOINTERFACE;
		}

		AddRef();
		return S_OK;
	}

	virtual ULONG STDMETHODCALLTYPE AddRef( void)
	{
		return ++m_ulRefCounter;
	}

	virtual ULONG STDMETHODCALLTYPE Release( void) 
	{
		if ( --m_ulRefCounter == 0 )
		{
			delete this;
			return 0;
		}
		return m_ulRefCounter;
	}

public:
	virtual HRESULT STDMETHODCALLTYPE putDocumentLocator( 
		/* [in] */ ISAXLocator *pLocator)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE startDocument( void)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE endDocument( void)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE startPrefixMapping( 
		/* [in] */ const wchar_t *pwchPrefix,
		/* [in] */ int cchPrefix,
		/* [in] */ const wchar_t *pwchUri,
		/* [in] */ int cchUri)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE endPrefixMapping( 
		/* [in] */ const wchar_t *pwchPrefix,
		/* [in] */ int cchPrefix)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE startElement( 
		/* [in] */ const wchar_t *pwchNamespaceUri,
		/* [in] */ int cchNamespaceUri,
		/* [in] */ const wchar_t *pwchLocalName,
		/* [in] */ int cchLocalName,
		/* [in] */ const wchar_t *pwchQName,
		/* [in] */ int cchQName,
		/* [in] */ ISAXAttributes *pAttributes)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE endElement( 
		/* [in] */ const wchar_t *pwchNamespaceUri,
		/* [in] */ int cchNamespaceUri,
		/* [in] */ const wchar_t *pwchLocalName,
		/* [in] */ int cchLocalName,
		/* [in] */ const wchar_t *pwchQName,
		/* [in] */ int cchQName)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE characters( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE ignorableWhitespace( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE processingInstruction( 
		/* [in] */ const wchar_t *pwchTarget,
		/* [in] */ int cchTarget,
		/* [in] */ const wchar_t *pwchData,
		/* [in] */ int cchData)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE skippedEntity( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName)
	{
		return S_OK;
	}

public:
	virtual HRESULT STDMETHODCALLTYPE startDTD( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName,
		/* [in] */ const wchar_t *pwchPublicId,
		/* [in] */ int cchPublicId,
		/* [in] */ const wchar_t *pwchSystemId,
		/* [in] */ int cchSystemId)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE endDTD( void)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE startEntity( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName) 
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE endEntity( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName) 
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE startCDATA( void) 
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE endCDATA( void)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE comment( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars) 
	{
		return S_OK;
	}

public:
	virtual HRESULT STDMETHODCALLTYPE error( 
		/* [in] */ ISAXLocator *pLocator,
		/* [in] */ const wchar_t *pwchErrorMessage,
		/* [in] */ HRESULT hrErrorCode) 
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE fatalError( 
		/* [in] */ ISAXLocator *pLocator,
		/* [in] */ const wchar_t *pwchErrorMessage,
		/* [in] */ HRESULT hrErrorCode)
	{
		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE ignorableWarning( 
		/* [in] */ ISAXLocator *pLocator,
		/* [in] */ const wchar_t *pwchErrorMessage,
		/* [in] */ HRESULT hrErrorCode)
	{
		return S_OK;
	}
};

class IXmlAttributes
{
public:
	virtual int Length() = 0;

	virtual bool Name( 
		/* [in] */ int nIndex,
		/* [out] */ const wchar_t **ppwchName,
		/* [out] */ int *pcchName) = 0;

	virtual bool IndexFromName( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName,
		/* [retval][out] */ int *pnIndex) = 0;

	virtual bool Value( 
		/* [in] */ int nIndex,
		/* [out] */ const wchar_t **ppwchValue,
		/* [out] */ int *pcchValue) = 0;

	virtual bool ValueFromName( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName,
		/* [out] */ const wchar_t **ppwchValue,
		/* [out] */ int *pcchValue) = 0;
};

class _SAXAttributes : public IXmlAttributes
{
public:
	SAXNS ISAXAttributes* m_pAttributes;

	virtual int Length()
	{
		if ( m_pAttributes == NULL )
		{
			return 0;
		}

		int nLength = 0; 

		m_pAttributes->getLength( &nLength );

		return nLength;				 
	}

	virtual bool Name( 
		/* [in] */ int nIndex,
		/* [out] */ const wchar_t **ppwchName,
		/* [out] */ int *pcchName)
	{
		if ( m_pAttributes == NULL )
		{
			return false;
		}
		
		HRESULT res = m_pAttributes->getQName( nIndex, ppwchName, pcchName);
		return SUCCEEDED( res);
	}

	virtual bool IndexFromName( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName,
		/* [retval][out] */ int *pnIndex) 
	{
		if ( m_pAttributes == NULL )
		{
			return false;
		}

		HRESULT res = m_pAttributes->getIndexFromQName( pwchName, cchName, pnIndex);
		return SUCCEEDED( res);
	}


	virtual bool Value( 
		/* [in] */ int nIndex,
		/* [out] */ const wchar_t **ppwchValue,
		/* [out] */ int *pcchValue) 
	{
		if ( m_pAttributes == NULL )
		{
			return false;
		}

		HRESULT res = m_pAttributes->getValue( nIndex, ppwchValue, pcchValue);
		return SUCCEEDED( res);
	}


	virtual bool ValueFromName( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName,
		/* [out] */ const wchar_t **ppwchValue,
		/* [out] */ int *pcchValue) 
	{
		if ( m_pAttributes == NULL )
		{
			return false;
		}

		HRESULT res = m_pAttributes->getValueFromQName( pwchName, cchName, ppwchValue, pcchValue);
		return SUCCEEDED( res);
	}
};

class COctetsAttributes  : public IXmlAttributes
{
public:
	struct CAttributeItem 
	{
		XMLIO::OCTETSIO::CMemoryString	m_name;
		XMLIO::OCTETSIO::CMemoryString	m_value;
	};
	typedef std::vector<CAttributeItem> CAttributeVector;
	CAttributeVector	m_atts;

	virtual int Length()
	{
		return (int)m_atts.size();
	}

	virtual bool Name( 
		/* [in] */ int nIndex,
		/* [out] */ const wchar_t **ppwchName,
		/* [out] */ int *pcchName)
	{
		if( nIndex < 0 || nIndex >= Length())
		{
			return false;
		}

		CAttributeItem& item = m_atts[nIndex];
		*ppwchName = item.m_name.GetString();
		*pcchName = (int)item.m_name.GetLength();

		return true;
	}

	virtual bool IndexFromName( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName,
		/* [retval][out] */ int *pnIndex)
	{
		*pnIndex = 0;
		for( CAttributeVector::const_iterator i=m_atts.begin(), e=m_atts.end(); i!=e; ++i, ++*pnIndex)
		{
			const CAttributeItem& item = *i;
			if( wcsncmp( item.m_name.GetString(), pwchName, cchName) == 0)
			{
				return true;
			}
		}
		return false;
	}

	virtual bool Value( 
		/* [in] */ int nIndex,
		/* [out] */ const wchar_t **ppwchValue,
		/* [out] */ int *pcchValue)
	{
		if( nIndex < 0 || nIndex >= Length())
		{
			return false;
		}

		CAttributeItem& item = m_atts[nIndex];
		*ppwchValue = item.m_value.GetString();
		*pcchValue = (int)item.m_value.GetLength();

		return true;
	}

	virtual bool ValueFromName( 
		/* [in] */ const wchar_t *pwchName,
		/* [in] */ int cchName,
		/* [out] */ const wchar_t **ppwchValue,
		/* [out] */ int *pcchValue)
	{
		for( CAttributeVector::const_iterator i=m_atts.begin(), e=m_atts.end(); i!=e; ++i)
		{
			const CAttributeItem& item = *i;
			if( wcsncmp( item.m_name.GetString(), pwchName, cchName) == 0)
			{
				*ppwchValue = item.m_value.GetString();
				*pcchValue = (int)item.m_value.GetLength();
				return true;
			}
		}
		return false;
	}
};

class IXmlParseEventListener
{
public:
	virtual bool StartDocument() = 0;

	virtual bool EndDocument() = 0;

	virtual bool Characters( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars) = 0;

	virtual bool StartElement(
		/* [in] */ const wchar_t* pwchName, 
		/* [in] */ int cchChars, 
		/* [in] */ IXmlAttributes* pAttributes )  = 0;

	virtual bool EndElement(
		/* [in] */ const wchar_t* pwchName, 
		/* [in] */ int cchChars ) = 0;

	virtual bool StartCDATA() = 0;

	virtual bool EndCDATA() = 0;

	virtual bool Comment( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars) = 0;

	virtual bool Error( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwzErrorMessage,
		/* [in] */ HRESULT hrErrorCode) = 0;

	virtual bool FatalError( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwchErrorMessage,
		/* [in] */ HRESULT hrErrorCode) = 0;

	virtual bool Warning( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwchErrorMessage,
		/* [in] */ HRESULT hrErrorCode) = 0;

	virtual void Release() = 0;
};

class CXmlParseEventAdapter : public IXmlParseEventListener
{
public:
	virtual bool StartDocument(){ return true; }

	virtual bool EndDocument(){ return true; }

	virtual bool Characters( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars)
	{  return true; }

	virtual bool StartElement(
		/* [in] */ const wchar_t* pwchName, 
		/* [in] */ int cchChars, 
		/* [in] */ IXmlAttributes* pAttributes )
	{  return true; }

	virtual bool EndElement(
		/* [in] */ const wchar_t* pwchName, 
		/* [in] */ int cchChars )
	{  return true; }

	virtual bool StartCDATA()
	{  return true; }

	virtual bool EndCDATA()
	{  return true; }

	virtual bool Comment( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars)
	{  return true; }

	virtual bool Error( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwzErrorMessage,
		/* [in] */ HRESULT hrErrorCode)
	{  return true; }

	virtual bool FatalError( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwchErrorMessage,
		/* [in] */ HRESULT hrErrorCode)
	{  return true; }

	virtual bool Warning( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwchErrorMessage,
		/* [in] */ HRESULT hrErrorCode)
	{  return true; }

	virtual void Release() { }
};

class CXmlParser 
{
public:
	CXmlParser()
		: m_pAgent ( NULL )
	{
	}
	
	~CXmlParser()
	{
		if( m_pAgent != NULL )
		{
			m_pAgent->Close();
			m_pAgent->Release();
		}
	}

	bool SetHandler( IXmlParseEventListener* pHandler )
	{
		if( m_pAgent == NULL )
		{
			//m_pAgent = new CSAXTranslator();
			m_pAgent = new COctetsSAXTranslator();
		}

		if( !m_pAgent->SetReceiver( pHandler ) )
		{
			return false;
		}

		return m_pAgent->Init();
	}

	bool ParseData( const void* pvData, size_t cbSize )
	{
		if( m_pAgent == NULL )
		{
			return false;
		}

		return m_pAgent->ParseData( pvData, cbSize );
	}

	bool ParseNative( const wchar_t* pwzFileName )
	{
		if( m_pAgent == NULL )
		{
			return false;
		}

		return m_pAgent->ParseNative( pwzFileName );
	}

	void Close()
	{
		if( m_pAgent != NULL )
		{
			m_pAgent->Close();
		}
	}

protected:
	class CSAXTranslator : public CSAXHandlerAgent
	{
	protected:
		IXmlParseEventListener* m_pHandler;
		SAXNS ISAXXMLReader* m_pReader;

	public:
		CSAXTranslator()
			: m_pHandler(NULL)
			, m_pReader(NULL)
		{
		}

		bool SetReceiver( IXmlParseEventListener* pHandler )
		{
			if( m_pHandler != NULL )
				m_pHandler->Release();
			m_pHandler = pHandler;
			return m_pHandler != NULL;
		}

		// TODO: 确定Parser的类型
		bool Init()
		{
			CComPtr<SAXNS ISAXXMLReader> spISAXXMLReader = NULL;
			HRESULT hr = CoCreateInstance( CLSID_SAXXMLReader40,
				NULL, CLSCTX_INPROC_SERVER, IID_ISAXXMLReader, (void**)&spISAXXMLReader );
			if ( !SUCCEEDED( hr ) )
			{
				return false;
			}

			VARIANT variant;
			variant.vt = VT_UNKNOWN;
			variant.punkVal = (SAXNS ISAXLexicalHandler*)this; // 这里的类型转换是必须的，等价于下面的转换链
			//variant.punkVal = (IUnknown*)(SAXNS ISAXLexicalHandler*)m_pAgent;
			// 这里直接赋值的方式没有增加引用计数，也无需执行VariantClear()
			hr = spISAXXMLReader->putProperty( L"http://xml.org/sax/properties/lexical-handler", variant );
			if ( !SUCCEEDED( hr ) )
			{
				return false;
			}

			hr = spISAXXMLReader->putContentHandler( this );
			if ( !SUCCEEDED( hr ) )
			{
				return false;
			}

			hr = spISAXXMLReader->putErrorHandler( this );
			if ( !SUCCEEDED( hr ) )
			{
				return false;
			}

			if( m_pReader != NULL )
			{
				m_pReader->Release();
			}

			m_pReader = spISAXXMLReader.Detach();

			return true;
		}

		bool ParseData( const void* pvData, size_t cbSize )
		{
			if( m_pReader == NULL )
			{
				return false;
			}

			VARIANT varSource;
			VariantInit(&varSource);
			varSource.vt = VT_UNKNOWN;
			V_UNKNOWN( &varSource ) = CreateIStreamOnDataBuffer( pvData, cbSize );
			HRESULT hr = m_pReader->parse( varSource );
			VariantClear(&varSource); // 这里需要执行VariantClear，释放CreateIStreamOnDataBuffer()创建的对象
			if ( !SUCCEEDED( hr ) )
			{
				return false;
			}

			return true;
		}

		bool ParseNative(const wchar_t* pwzFileName )
		{
			if( m_pReader == NULL )
			{
				return false;
			}

			HRESULT hr = m_pReader->parseURL( pwzFileName );
			if ( !SUCCEEDED( hr ) )
			{
				return false;
			}

			return true;
		}

		void Close()
		{
			if( m_pReader != NULL )
			{
				m_pReader->Release();
				m_pReader = NULL;
			}

			if( m_pHandler != NULL )
			{
				m_pHandler->Release();
				m_pHandler = NULL;
			}
		}

	public:
		virtual HRESULT STDMETHODCALLTYPE startDocument( void)
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			if( m_pHandler->StartDocument() )
				return S_OK;
			else
				return S_FALSE;
		}

		virtual HRESULT STDMETHODCALLTYPE endDocument( void)
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			if( m_pHandler->EndDocument() )
				return S_OK;
			else
				return S_FALSE;
		}

		virtual HRESULT STDMETHODCALLTYPE startElement( 
			/* [in] */ const wchar_t *pwchNamespaceUri,
			/* [in] */ int cchNamespaceUri,
			/* [in] */ const wchar_t *pwchLocalName,
			/* [in] */ int cchLocalName,
			/* [in] */ const wchar_t *pwchQName,
			/* [in] */ int cchQName,
			/* [in] */ ISAXAttributes *pAttributes)
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			_SAXAttributes atts;
			atts.m_pAttributes = pAttributes;

			if( m_pHandler->StartElement( pwchLocalName, cchLocalName, &atts ) )
				return S_OK;
			else
				return S_FALSE;

			return S_OK;
		}

		virtual HRESULT STDMETHODCALLTYPE endElement( 
			/* [in] */ const wchar_t *pwchNamespaceUri,
			/* [in] */ int cchNamespaceUri,
			/* [in] */ const wchar_t *pwchLocalName,
			/* [in] */ int cchLocalName,
			/* [in] */ const wchar_t *pwchQName,
			/* [in] */ int cchQName)
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			if( m_pHandler->EndElement(
				pwchLocalName, cchLocalName ) )
				return S_OK;
			else
				return S_FALSE;

			return S_OK;
		}

		virtual HRESULT STDMETHODCALLTYPE characters( 
			/* [in] */ const wchar_t *pwchChars,
			/* [in] */ int cchChars)
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			if( m_pHandler->Characters(
				pwchChars, cchChars ) )
				return S_OK;
			else
				return S_FALSE;

			return S_OK;
		}

		virtual HRESULT STDMETHODCALLTYPE startCDATA( void) 
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			if( m_pHandler->StartCDATA() )
				return S_OK;
			else
				return S_FALSE;

			return S_OK;
		}

		virtual HRESULT STDMETHODCALLTYPE endCDATA( void)
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			if( m_pHandler->EndCDATA() )
				return S_OK;
			else
				return S_FALSE;

			return S_OK;
		}

		virtual HRESULT STDMETHODCALLTYPE comment( 
			/* [in] */ const wchar_t *pwchChars,
			/* [in] */ int cchChars) 
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			if( m_pHandler->Comment(pwchChars, cchChars) )
				return S_OK;
			else
				return S_FALSE;

			return S_OK;
		}

		virtual HRESULT STDMETHODCALLTYPE error( 
			/* [in] */ ISAXLocator *pLocator,
			/* [in] */ const wchar_t *pwzErrorMessage,
			/* [in] */ HRESULT hrErrorCode) 
		{
			if( m_pHandler == NULL )
			{
				return E_FAIL;
			}

			int nColumn = 0, nLine = 0;
			pLocator->getLineNumber( &nLine );
			pLocator->getColumnNumber( &nColumn );

			if( m_pHandler->Error(nLine, nColumn, pwzErrorMessage, hrErrorCode) )
				return S_OK;
			else
				return S_FALSE;
		}

		virtual HRESULT STDMETHODCALLTYPE fatalError( 
			/* [in] */ ISAXLocator *pLocator,
			/* [in] */ const wchar_t *pwzErrorMessage,
			/* [in] */ HRESULT hrErrorCode)
		{
			int nColumn = 0, nLine = 0;
			pLocator->getLineNumber( &nLine );
			pLocator->getColumnNumber( &nColumn );

			if( m_pHandler->FatalError(nLine, nColumn, pwzErrorMessage, hrErrorCode) )
				return S_OK;
			else
				return S_FALSE;
		}

		virtual HRESULT STDMETHODCALLTYPE ignorableWarning( 
			/* [in] */ ISAXLocator *pLocator,
			/* [in] */ const wchar_t *pwzErrorMessage,
			/* [in] */ HRESULT hrErrorCode)
		{
			int nColumn = 0, nLine = 0;
			pLocator->getLineNumber( &nLine );
			pLocator->getColumnNumber( &nColumn );

			if( m_pHandler->Warning(nLine, nColumn, pwzErrorMessage, hrErrorCode) )
				return S_OK;
			else
				return S_FALSE;
		}
	};


	// 二进制xml的SAX解析
	class COctetsSAXTranslator
	{
	protected:
		IXmlParseEventListener* m_pHandler;
		XMLIO::OCTETSIO::CMemoryObject			m_data;
		XMLIO::OCTETSIO::CStringDictionary		m_dict;
		enum { PRETYPE_SIZE = 4, };

	public:
		COctetsSAXTranslator()
			: m_pHandler(NULL)
		{
		}

		bool SetReceiver( IXmlParseEventListener* pHandler )
		{
			if( m_pHandler != NULL )
				m_pHandler->Release();
			m_pHandler = pHandler;
			return m_pHandler != NULL;
		}

		bool ParseData( const void* pvData, size_t cbSize )
		{
			assert( cbSize >= PRETYPE_SIZE);
			size_t size = cbSize - PRETYPE_SIZE;
			if( 0 == size || size > MAXDWORD )
				return false;
			{
				unsigned int type = *((unsigned int*)pvData);
				if( 0xEFCDAB89 != type)
					return false;
			}
			m_data.Initialize( (const unsigned char*)pvData + PRETYPE_SIZE, size);

			try
			{
				m_dict.Make( m_data);

				// startDocument
				m_pHandler->StartDocument();

				ParseNode( m_data, m_dict);

				// endDocument
				m_pHandler->EndDocument();
			}
			catch( ...)
			{
				m_pHandler->Error( 0, 0, L"data error", 0);
				return false;
			}

			return true;
		}

		bool Init() { return true; }
		bool ParseNative(const wchar_t* pwzFileName ) 
		{
			return true;
		}
		void Close()
		{
			m_dict.Clear();
		}
		void Release()
		{
			delete this;
		}

	protected:
		void ParseNode( const XMLIO::OCTETSIO::CMemoryObject& mo, 
			const XMLIO::OCTETSIO::CStringDictionary& dict)
		{
			unsigned int type = mo.GetNextUnsignedInteger();	// node type
			size_t name = mo.GetNextSize();						// node name
			size_t text = mo.GetNextSize();						// node text
			
			COctetsAttributes atts;
			ParseAttribute( mo, dict, atts);

			// startElement
			const XMLIO::OCTETSIO::CMemoryString& nameStr = dict.GetString( name);
			if( wcsncmp( nameStr.GetString(), L"#cdata", 6) == 0)
			{
				m_pHandler->StartCDATA();
			}
			else if( wcsncmp( nameStr.GetString(), L"#comment", 8) == 0)
			{
				m_pHandler->Comment( nameStr.GetString(), (int)nameStr.GetLength());
			}
			else
			{
				m_pHandler->StartElement( nameStr.GetString(), (int)nameStr.GetLength(), &atts);
			}
			
			ParseChildren( mo, dict);

			// endElement
			if( wcsncmp( nameStr.GetString(), L"#cdata", 6) == 0)
			{
				m_pHandler->EndCDATA();
			}
			else if( wcsncmp( nameStr.GetString(), L"#comment", 8) == 0)
			{

			}
			else
			{
				m_pHandler->EndElement( nameStr.GetString(), (int)nameStr.GetLength());
			}
		}

		void ParseAttribute( const XMLIO::OCTETSIO::CMemoryObject& mo, 
			const XMLIO::OCTETSIO::CStringDictionary& dict,
			COctetsAttributes& atts)
		{
			size_t scount =	mo.GetNextSize();
			for( size_t i = 0; i < scount; i ++)
			{
				size_t name = mo.GetNextSize();			// attribute name
				size_t value = mo.GetNextSize();		// attribute value
				COctetsAttributes::CAttributeItem item;
				item.m_name = dict.GetString( name);
				item.m_value = dict.GetString( value);
				atts.m_atts.push_back( item);
			}
		}

		void ParseChildren( const XMLIO::OCTETSIO::CMemoryObject& mo, 
			const XMLIO::OCTETSIO::CStringDictionary& dict)
		{
			size_t scount = mo.GetNextSize();
			for( size_t i = 0; i < scount; i++)
			{
				ParseNode( mo, dict);
			}
		}
	};

protected:
	//CSAXTranslator* m_pAgent;
	COctetsSAXTranslator* m_pAgent;
};

class CXmlParseTracer : public CXmlParseEventAdapter
{
protected:
	void outputtext( const wchar_t *pwchChars,
		/* [in] */ int cchChars )
	{
		if ( cchChars > 0 )
		{
			static wchar_t buffer[1025];
			if ( cchChars < 1025 )
			{
				wcsncpy_s( buffer, sizeof(buffer), pwchChars, cchChars );
				wprintf(L"%s", buffer );
			}
			else
			{
				wchar_t last = pwchChars[cchChars-1];
				if ( cchChars > 1 )
				{
					((wchar_t *)pwchChars)[cchChars-1] = 0;
					wprintf(L"%s", pwchChars );
					wcsncpy_s( buffer, sizeof(buffer), &last, 1 );
					wprintf(L"%s", buffer );
					((wchar_t *)pwchChars)[cchChars-1] = last;
				}
				else
				{
					wcsncpy_s( buffer,sizeof(buffer),  &last, 1 );
					wprintf(L"%s", buffer );
				}
			}

			//_putws(L"");
		}
	}

	void outputtextln( const wchar_t *pwchChars,
		/* [in] */ int cchChars )
	{
		if ( cchChars > 0 )
		{
			outputtext( pwchChars, cchChars );
			_putws(L"");
		}
	}

public:
	virtual bool StartDocument()
	{
		wprintf(__FUNCTIONW__ L"\n\n");
		return true;
	}

	virtual bool EndDocument()
	{
		wprintf(L"\n" __FUNCTIONW__ L"\n");
		return true;
	}

	virtual bool Characters( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars)
	{
		outputtext( pwchChars, cchChars );
		return true;
	}

	virtual bool StartElement(
		/* [in] */ const wchar_t* pwchName, 
		/* [in] */ int cchChars, 
		/* [in] */ IXmlAttributes* pAttributes )
	{
		wprintf(__FUNCTIONW__ L": ");
		outputtext( pwchName, cchChars );

		if( pAttributes)
		{
			int count = pAttributes->Length();
			for( int i=0; i<count; i++)
			{
				const wchar_t* name = NULL;
				int len = 0;
				pAttributes->Name( i, &name, &len);
				wprintf( L" ");
				outputtext( name, len );
				wprintf( L" = \"");
				pAttributes->Value( i, &name, &len);
				outputtext( name, len );
				wprintf( L"\"");
			}
		}

		wprintf( L"\n");
		return true;
	}

	virtual bool EndElement(
		/* [in] */ const wchar_t* pwchName, 
		/* [in] */ int cchChars )
	{
		wprintf(__FUNCTIONW__ L": ");
		outputtext( pwchName, cchChars );
		wprintf( L"\n");
		return true;
	}

	virtual bool StartCDATA()
	{
		wprintf(__FUNCTIONW__ L"\n");
		return true;
	}

	virtual bool EndCDATA()
	{
		wprintf(__FUNCTIONW__ L"\n");
		return true;
	}

	virtual bool Comment( 
		/* [in] */ const wchar_t *pwchChars,
		/* [in] */ int cchChars)
	{
		wprintf(__FUNCTIONW__ L": ");
		outputtext( pwchChars, cchChars );
		wprintf( L"\n");
		return true;
	}

	virtual bool Error( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwzErrorMessage,
		/* [in] */ HRESULT hrErrorCode)
	{
		wprintf(L"Error: line: %d, column, %d, message: %s, code: %#08x\n", 
			nLine, nColumn, pwzErrorMessage, hrErrorCode );
		return true;
	}

	virtual bool FatalError( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwzErrorMessage,
		/* [in] */ HRESULT hrErrorCode)
	{
		wprintf(L"FatalError: line: %d, column, %d, message: %s, code: %#08x\n",
			nLine, nColumn, pwzErrorMessage, hrErrorCode );
		return true;
	}

	virtual bool Warning( 
		/* [in] */ int nLine,
		/* [in] */ int nColumn,
		/* [in] */ const wchar_t *pwzErrorMessage,
		/* [in] */ HRESULT hrErrorCode) 
	{
		wprintf(L"Warning: line: %d, column, %d, message: %s, code: %#08x\n",
			nLine, nColumn, pwzErrorMessage, hrErrorCode );
		return true;
	}

	virtual void Release()
	{
		wprintf(__FUNCTIONW__ L"\n");
	}
};

INT ReadDataFromFile( LPCWSTR lpszFileName, LPVOID lpvBuffer, INT bufferSize )
{
	HANDLE fh = CreateFile (lpszFileName, 
		GENERIC_READ, 
		FILE_SHARE_READ,
		NULL, OPEN_EXISTING, 0, NULL); 

	if (fh != INVALID_HANDLE_VALUE)
	{
		DWORD len = GetFileSize(fh, NULL);
		if ( lpvBuffer == NULL || bufferSize == 0 )
		{
			CloseHandle (fh);
			return len;
		}
		
		DWORD numRead = 0;
		
		if (len && len != INVALID_FILE_SIZE)
		{
			ReadFile (fh, lpvBuffer, bufferSize, &numRead, NULL);
		}

		CloseHandle (fh);

		return numRead;
	}

	return INVALID_FILE_SIZE;
}

int _tmain(int argc, _TCHAR* argv[])
{
	setlocale( LC_ALL, ".936");

	CComInitialize _ComInit;

	CXmlParseTracer handler;
	CXmlParser parser;
	parser.SetHandler( &handler );
	//LPCWSTR lpszFileName = L"D:\\TestInstall\\uitext.xml";
	LPCWSTR lpszFileName = L"D:\\TestInstall\\version.xml";
	INT len = ReadDataFromFile(lpszFileName, NULL, 0 ); // 获取文件大小
	// len --; 
	// 测试某一错误
	if( len > 0 )
	{
		char* data = new char[ len ];
		ReadDataFromFile(lpszFileName, data, len);
		parser.ParseData(data, len);
		delete[] data;
	}

	//parser.ParseNative( L"D:\\saxtest.xml" );

	return 0;
}

