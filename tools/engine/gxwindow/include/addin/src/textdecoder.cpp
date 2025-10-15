#include "gxwaddindef.h"
#include "DecoderHelp.h"

namespace GXWADDIN
{


void CDomXMLTextDecoder::Decode( const wchar_t* fulltext, GXWINDOW::CDecoderTextPieceList& tpl)
{
	INLINECLASS::CComInitScope		scope;

	try
	{
		CComPtr<IXMLDOMDocument>	Document;
		CComPtr<IXMLDOMElement>		EleRoot;

		HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL, CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&Document);
		if( FAILED( hr))
			throw hr;

		{
			VARIANT_BOOL		bResult;
			hr = Document->loadXML( (BSTR)fulltext, &bResult);
			if( FAILED( hr))
				throw hr;
			if( !bResult)
				throw 0;
		}

		{
			hr = Document->get_documentElement( &EleRoot);
			if ( FAILED( hr))
				throw hr;
			if( EleRoot == NULL)
				throw 0;
		}

		CComPtr<IXMLDOMNodeList>	NodeList;

		hr = EleRoot->get_childNodes( &NodeList);
		if( FAILED( hr))
			throw hr;
		if( NodeList == NULL)
			throw 0;

		long		lCount;
		hr = NodeList->get_length( &lCount);
		if( FAILED( hr))
			lCount = 0;
		for( long i = 0; i < lCount; i ++)
		{
			CComPtr<IXMLDOMNode>		DomNode;

			hr = NodeList->get_item( i, &DomNode);
			if( FAILED( hr) || DomNode == NULL)
				continue;
			DOMNodeType		nt;

			hr = DomNode->get_nodeType( &nt);
			if( FAILED( hr))
				continue;
			switch( nt)
			{
			case NODE_TEXT:
				{
					CComBSTR	bstrName;
					hr = DomNode->get_text( &bstrName);
					if( FAILED( hr))
						continue;
					tpl.Insert( GXWINDOW::CDecoderTextPiece( bstrName));
				}
				break;
			case NODE_ELEMENT:
				{
					CComPtr<IXMLDOMElement>		EleChild;
					hr = DomNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&EleChild);
					if( FAILED( hr) || EleChild == NULL)
						continue;
					DomDecodeElement( EleChild, tpl);
				}
				break;
			default:
				continue;
			}
		} // for( long i = 0; i < lCount; i ++)
	}
	catch( ...)
	{
	}
}

}