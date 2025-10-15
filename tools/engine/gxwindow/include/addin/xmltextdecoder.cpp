#include "comxml.h"
#include "xmltextdecoder.h"

#ifdef USE_NAIVEFILE

namespace GXWADDIN {

namespace INLINECLASS {

void DecodeElementEmotion( CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	int w = Ele.ReadInteger( L"W", L"w", 32);
	int h = Ele.ReadInteger( L"H", L"h", 32);
	int u = Ele.ReadInteger( L"U", L"u", 32);
	int v = Ele.ReadInteger( L"V", L"v", 32);
	int p = Ele.ReadInteger( L"P", L"p", 0);
	int i = Ele.ReadInteger( L"I", L"i", 0);
	CComBSTR	strMouseNotify;
	Ele.ReadString( L"M", L"n", strMouseNotify);

	tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetTipInfoText().c_str(), p, i, w, h, u, v, strMouseNotify));
}

void DecodeElementAnimate( CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	int w = Ele.ReadInteger( L"W", L"w", 32);
	int h = Ele.ReadInteger( L"H", L"h", 32);
	int i = Ele.ReadInteger( L"I", L"i", 500);
	int s = Ele.ReadInteger( L"S", L"s", 0);
	int e = Ele.ReadInteger( L"E", L"e", 0);
	CComBSTR	strMouseNotify;
	Ele.ReadString( L"M", L"n", strMouseNotify);

	tpl.Insert( GXWINDOW::CDecoderTextPiece( s, e, w, h, i, Ele.GetTipInfoText().c_str(), strMouseNotify));
}

void DecodeElementImage( CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	int w = Ele.ReadInteger( L"W", L"w", 32);
	int h = Ele.ReadInteger( L"H", L"h", 32);
	int i = Ele.ReadInteger( L"I", L"i", 0);
	CComBSTR	strMouseNotify;
	Ele.ReadString( L"M", L"n", strMouseNotify);
	tpl.Insert( GXWINDOW::CDecoderTextPiece( i, w, h, Ele.GetTipInfoText().c_str(), strMouseNotify));
}

void DecodeElementText( CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl, GXWINDOW::DTP_TYPE dtpt)
{
	int f = Ele.ReadInteger( L"F", L"f", 0);
	CComBSTR	strNotify;
	Ele.ReadString( L"I", L"i", strNotify);
	TextFormat		tf;
	bool b = CTextFormatManager::GetInstance().GetTextFormat( f, tf);
	assert( b);
	tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetText().c_str(), tf.m_nFont, f, tf.m_clrNormal, tf.m_clrNormalGradual, tf.m_clrSelect, tf.m_clrSelectGradual, tf.m_clrBackground, strNotify, 
		GXWINDOW::DTPT_COMPLEX == dtpt, Ele.GetTipInfoText().c_str(), tf.m_nNormalEffectType, tf.m_clrNormalEffect, tf.m_nSelectEffectType, tf.m_clrSelectEffect));
}

void DecodeElementButton( CDomElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	int f = Ele.ReadInteger( L"F", L"f", 0);
	TextFormat		tf;
	bool b = CTextFormatManager::GetInstance().GetTextFormat( f, tf);
	assert( b);
	CComBSTR	strNotify;
	Ele.ReadString( L"N", L"n", strNotify);
	int nImage = Ele.ReadInteger( L"I", L"i", -1);
	int nImageMouseOn = Ele.ReadInteger( L"O", L"o", -1);
	int nImageDown = Ele.ReadInteger( L"D", L"d", -1);
	int nImageWidth = Ele.ReadInteger( L"W", L"w", nImage < 0 ? 0 : GXWINDOW::EBE_DEFAULT_BUTTON_WIDTH);
	int nImageHeight = Ele.ReadInteger( L"H", L"h", nImage < 0 ? 0 : GXWINDOW::EBE_DEFAULT_BUTTON_HEIGHT);
	CComBSTR	strMouseNotify;
	Ele.ReadString( L"M", L"n", strMouseNotify);

	tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetText().c_str(), tf.m_nFont, f, tf.m_clrNormal, tf.m_clrNormalGradual, tf.m_clrMouseOn, tf.m_clrMouseOnGradual, nImage, nImageMouseOn, nImageDown, strNotify, 
		nImageWidth, nImageHeight, Ele.GetTipInfoText().c_str(), strMouseNotify, tf.m_nNormalEffectType, tf.m_clrNormalEffect, tf.m_nSelectEffectType, tf.m_clrSelectEffect));
}

void DecodeElement( CComPtr<IXMLDOMElement>& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	CComBSTR	bstrName;

	HRESULT	hr = Ele->get_tagName( &bstrName);
	if( FAILED( hr))
		return;
	if( 0 == bstrName.m_str[0])
		return;
	if( bstrName.m_str[1])
		return;
	switch( bstrName.m_str[0]) 
	{
	case L'A':
	case L'a':
		DecodeElementAnimate( CDomElement( Ele), tpl);
		break;
	case L'I':
	case L'i':
		DecodeElementImage( CDomElement( Ele), tpl);
		break;
	case L'T':
	case L't':
		DecodeElementText( CDomElement( Ele), tpl, GXWINDOW::DTPT_FORMAT_TEXT);
		break;
	case L'C':
	case L'c':
		DecodeElementText( CDomElement( Ele), tpl, GXWINDOW::DTPT_COMPLEX);
		break;
	case L'R':
	case L'r':
		tpl.Insert( GXWINDOW::CDecoderTextPiece());
		break;
	case L'B':
	case L'b':
		DecodeElementButton( CDomElement( Ele), tpl);
		break;
	case L'E':
	case L'e':
		DecodeElementEmotion( CDomElement( Ele), tpl);
		break;
	default:
		break;
	}
}

} // namespace INLINECLASS {

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextFormatManager		CTextFormatManager::s_Instance;

void CTextFormatManager::Load( const wchar_t* szFile, bool bLoadFromMem)
{
	INLINECLASS::CComInitScope		scope;

	Clear();

	try
	{
		CComPtr<IXMLDOMDocument>	Document;
		CComPtr<IXMLDOMNode>		NodeRoot;
		CComPtr<IXMLDOMElement>		EleRoot;

		HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL, CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&Document);
		if( FAILED( hr))
			throw hr;
		{
			VARIANT_BOOL		vb;
			if ( bLoadFromMem)
				hr = Document->loadXML( _bstr_t( szFile), &vb);
			else
				hr = Document->load( CComVariant( szFile), &vb);
			if( FAILED( hr) || !vb)
				throw hr;
		}
		//hr = Document->get_documentElement( &EleRoot);
		hr = Document->selectSingleNode( L"data/TextFormat", &NodeRoot);
		if( FAILED( hr) || NodeRoot == NULL)
			throw hr;

		hr = NodeRoot->QueryInterface( __uuidof(IXMLDOMElement), (void**)&EleRoot);
		if( FAILED( hr) || EleRoot == NULL)
			throw hr;

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
			if( NODE_ELEMENT != nt)
				continue;

			CComPtr<IXMLDOMElement>		EleChild;
			hr = DomNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&EleChild);
			if( FAILED( hr) || EleChild == NULL)
				continue;

			INLINECLASS::CDomElement		element( EleChild);

			TextFormat		tf;
			std::wstring	name;

			int nid = element.ReadInteger( L"id", L"ID", -1);

			{
				CComBSTR		strName;
				if( element.ReadString( L"name", L"Name", strName))
					name.assign( strName, strName.Length());
				else
				{
					std::wstringstream		wss;
					wss << L"tf-" << nid;
					name = wss.str();
				}
			}
			tf.m_nFont = element.ReadInteger( L"font", L"Font");
			tf.m_clrNormal = element.ReadColor( L"normal", L"Normal");
			tf.m_clrNormalGradual = element.ReadColor( L"normalgradual", L"NoramlGradual", tf.m_clrNormal);
			tf.m_clrSelect = element.ReadColor( L"select", L"Select");
			tf.m_clrSelectGradual = element.ReadColor( L"selectgradual", L"SelectGradual", tf.m_clrSelect);
			tf.m_clrBackground = element.ReadColor( L"back", L"Back");
			tf.m_clrNormalEffect = element.ReadColor( L"normaleffect", L"Normaleffect");
			tf.m_clrSelectEffect = element.ReadColor( L"selecteffect", L"selecteffect");

			tf.m_nNormalEffectType = GXWINDOW::FET_NONE;
			tf.m_nSelectEffectType = GXWINDOW::FET_NONE;

			{
				int effect_type = element.ReadInteger( L"normaleffecttype", L"Normaleffecttype");
				if ( 1 == effect_type)
					tf.m_nNormalEffectType = tf.m_nNormalEffectType | GXWINDOW::FET_SHADOW;
				else if ( 2 == effect_type)
					tf.m_nNormalEffectType = tf.m_nNormalEffectType | GXWINDOW::FET_STROKE;

				CComBSTR		strUnderline;
				element.ReadString( L"normalunderline", L"Normalunderline", strUnderline);
				if ( 0 == _wcsicmp( L"true", strUnderline.m_str))
					tf.m_nNormalEffectType = tf.m_nNormalEffectType | GXWINDOW::FET_UNDERLINE;

				effect_type = element.ReadInteger( L"selecteffecttype", L"Selecteffecttype");
				if ( 1 == effect_type)
					tf.m_nSelectEffectType = tf.m_nSelectEffectType | GXWINDOW::FET_SHADOW;
				else if ( 2 == effect_type)
					tf.m_nSelectEffectType = tf.m_nSelectEffectType | GXWINDOW::FET_STROKE;

				element.ReadString( L"selectunderline", L"Selectunderline", strUnderline);
				if ( 0 == _wcsicmp( L"true", strUnderline.m_str))
					tf.m_nSelectEffectType = tf.m_nSelectEffectType | GXWINDOW::FET_UNDERLINE;
			}

			
			m_tfmap[nid] = tf;
			m_name2id[name] = nid;
			m_id2name[nid] = name;

			if( m_nIdGenerator <= nid)
				m_nIdGenerator = nid + 1;
		} // for( long i = 0; i < lCount; i ++)
	}
	catch( ...) 
	{
	}
}

void CTextFormatManager::Save( const wchar_t* szFile) const
{
	INLINECLASS::CComInitScope		scope;

	try
	{
		CComPtr<IXMLDOMDocument>	Document;
		CComPtr<IXMLDOMElement>		EleRoot;

		HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL, CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&Document);
		if( FAILED( hr))
			throw hr;
		hr = Document->createElement( L"TextFormat", &EleRoot);
		if( FAILED( hr) || EleRoot == NULL)
			throw hr;
		hr = Document->putref_documentElement( EleRoot);
		if( FAILED( hr))
			throw hr;

		for( CTextFormatMap::const_iterator it = m_tfmap.begin(); it != m_tfmap.end(); ++ it)
		{
			const int& nID = it->first;
			const TextFormat& tf = it->second;
			CComPtr<IXMLDOMElement>		Ele;

			hr = Document->createElement( L"tf", &Ele);
			if( FAILED( hr))
				continue;
            Ele->setAttribute( L"id", CComVariant( nID));
			Ele->setAttribute( L"font", CComVariant( tf.m_nFont));
			Ele->setAttribute( L"normal", CComVariant( INLINECLASS::ColorToSting( tf.m_clrNormal).c_str()) );
			Ele->setAttribute( L"normalgradual", CComVariant( INLINECLASS::ColorToSting( tf.m_clrNormalGradual).c_str()) );
			Ele->setAttribute( L"select", CComVariant( INLINECLASS::ColorToSting( tf.m_clrSelect).c_str()) );
			Ele->setAttribute( L"selectgradual", CComVariant( INLINECLASS::ColorToSting( tf.m_clrSelectGradual).c_str()) );
			Ele->setAttribute( L"back", CComVariant( INLINECLASS::ColorToSting( tf.m_clrBackground).c_str()) );
            
			std::wstring	strName;
			if( !GetName( nID, strName))
			{
				std::wstringstream	wss;
				wss << L"tf-" << nID;
				strName = wss.str();
			}
			Ele->setAttribute( L"name", CComVariant( strName.c_str()) );
		
			CComPtr<IXMLDOMNode> node;
			EleRoot->appendChild( Ele, &node);
		} //for( CTextFormatMap::const_iterator it = m_tfmap.begin(); it != m_tfmap.end(); ++ it)

		Document->save( CComVariant( szFile));
	}
	catch( ...)
	{
	}
}

int CTextFormatManager::NewName( const std::wstring& name)
{
	CTFName2IDMap::const_iterator itName2id = m_name2id.find( name);
	if( itName2id != m_name2id.end())
	{
		const int& oldid = itName2id->second;
		return oldid;
	}
	
	int nNewID = m_nIdGenerator ++;
	m_name2id[name] = nNewID;
	m_id2name[nNewID] = name;
	return nNewID;
}

void CTextFormatManager::SetTextFormat( int nID, const TextFormat& tf, const wchar_t *name)
{
	m_tfmap[nID] = tf;
	if (!name) // add "name" by llq
	{
		m_name2id[name] = nID;
		m_id2name[nID] = name;
	}
}

bool CTextFormatManager::GetName( int nID, std::wstring& name) const
{
	CTFID2NameMap::const_iterator it = m_id2name.find( nID);
	if( it == m_id2name.end())
		return false;
	const std::wstring& strName = it->second;
	name = strName;
	return true;
}

bool CTextFormatManager::GetID( const std::wstring& name, int& nid) const
{
	CTFName2IDMap::const_iterator itName2id = m_name2id.find( name);
	if( itName2id == m_name2id.end())
		return false;
	const int& id = itName2id->second;
	nid = id;
	return true;
}

bool CTextFormatManager::GetTextFormat( int nID, TextFormat& tf) const
{
	CTextFormatMap::const_iterator it = m_tfmap.find( nID);
	if( it == m_tfmap.end())
		return false;
	const TextFormat& tfValue = it->second;
	tf = tfValue;
	return true;
}

bool CTextFormatManager::GetTextFormat( const std::wstring& name, TextFormat& tf) const
{
	int nid = -1;
	if( GetID( name, nid))
		return GetTextFormat( nid, tf);
	else
		return false;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void CXMLTextDecoder::Decode( const wchar_t* fulltext, GXWINDOW::CDecoderTextPieceList& tpl)
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
					INLINECLASS::DecodeElement( EleChild, tpl);
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

void CXMLTextEncoder::AddTextPiece( const GXWINDOW::CDecoderTextPiece& tp)
{
	INLINECLASS::CComInitScope		scope;
}

} // namespace GXWADDIN {

#endif