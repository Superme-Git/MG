#include "xmlioxml.h"
#include "xmltextdecoder.h"

namespace GXWADDIN {

namespace INLINECLASS {

void DecodeElementEmotion( CElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	int w = Ele.ReadInteger( L"W", L"w", 32);
	int h = Ele.ReadInteger( L"H", L"h", 32);
	int u = Ele.ReadInteger( L"U", L"u", 32);
	int v = Ele.ReadInteger( L"V", L"v", 32);
	int p = Ele.ReadInteger( L"P", L"p", 0);
	int i = Ele.ReadInteger( L"I", L"i", 0);
	std::wstring	strMouseNotify;
	Ele.ReadString( L"M", L"n", strMouseNotify);

	tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetTipInfoText().c_str(), p, i, w, h, u, v, strMouseNotify.c_str()));
}

void DecodeElementAnimate( CElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	int w = Ele.ReadInteger( L"W", L"w", 32);
	int h = Ele.ReadInteger( L"H", L"h", 32);
	int i = Ele.ReadInteger( L"I", L"i", 500);
	int s = Ele.ReadInteger( L"S", L"s", 0);
	int e = Ele.ReadInteger( L"E", L"e", 0);
	std::wstring	strMouseNotify;
	Ele.ReadString( L"M", L"n", strMouseNotify);

	tpl.Insert( GXWINDOW::CDecoderTextPiece( s, e, w, h, i, Ele.GetTipInfoText().c_str(), strMouseNotify.c_str()));
}

void DecodeElementImage( CElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	int w = Ele.ReadInteger( L"W", L"w", 32);
	int h = Ele.ReadInteger( L"H", L"h", 32);
	int i = Ele.ReadInteger( L"I", L"i", 0);
	std::wstring	strMouseNotify;
	Ele.ReadString( L"M", L"n", strMouseNotify);
	tpl.Insert( GXWINDOW::CDecoderTextPiece( i, w, h, Ele.GetTipInfoText().c_str(), strMouseNotify.c_str()));
}

void DecodeElementText( CElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl, GXWINDOW::DTP_TYPE dtpt)
{
	int f = Ele.ReadInteger( L"F", L"f", 0);
	std::wstring	strNotify;
	Ele.ReadString( L"I", L"i", strNotify);
	TextFormat		tf;
	bool b = CTextFormatManager::GetInstance().GetTextFormat( f, tf);
	assert( b);
	tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetText().c_str(), tf.m_nFont, f, tf.m_clrNormal, tf.m_clrNormalGradual, tf.m_clrSelect, tf.m_clrSelectGradual, tf.m_clrBackground, strNotify.c_str(), 
			GXWINDOW::DTPT_COMPLEX == dtpt, Ele.GetTipInfoText().c_str(), tf.m_nNormalEffectType, tf.m_clrNormalEffect, tf.m_nSelectEffectType, tf.m_clrSelectEffect));
}

void DecodeElementButton( CElement& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	int f = Ele.ReadInteger( L"F", L"f", 0);
	TextFormat		tf;
	bool b = CTextFormatManager::GetInstance().GetTextFormat( f, tf);
	assert( b);
	std::wstring	strNotify;
	Ele.ReadString( L"N", L"n", strNotify);
	int nImage = Ele.ReadInteger( L"I", L"i", -1);
	int nImageMouseOn = Ele.ReadInteger( L"O", L"o", -1);
	int nImageDown = Ele.ReadInteger( L"D", L"d", -1);
	int nImageWidth = Ele.ReadInteger( L"W", L"w", nImage < 0 ? 0 : GXWINDOW::EBE_DEFAULT_BUTTON_WIDTH);
	int nImageHeight = Ele.ReadInteger( L"H", L"h", nImage < 0 ? 0 : GXWINDOW::EBE_DEFAULT_BUTTON_HEIGHT);
	std::wstring	strMouseNotify;
	Ele.ReadString( L"M", L"n", strMouseNotify);

	tpl.Insert( GXWINDOW::CDecoderTextPiece( Ele.GetText().c_str(), tf.m_nFont, f, tf.m_clrNormal, tf.m_clrNormalGradual, tf.m_clrMouseOn, tf.m_clrMouseOnGradual, nImage, nImageMouseOn, nImageDown, strNotify.c_str(), 
			nImageWidth, nImageHeight, Ele.GetTipInfoText().c_str(), strMouseNotify.c_str(), tf.m_nNormalEffectType, tf.m_clrNormalEffect, tf.m_nSelectEffectType, tf.m_clrSelectEffect));
}

void DecodeElement( XMLIO::CINode& Ele, GXWINDOW::CDecoderTextPieceList& tpl)
{
	std::wstring name = Ele.GetName();
	if ( name.empty())
		return;

	switch( name.c_str()[0])
	{
	case L'A':
	case L'a':
		DecodeElementAnimate( CElement( Ele), tpl);
		break;
	case L'I':
	case L'i':
		DecodeElementImage( CElement( Ele), tpl);
		break;
	case L'T':
	case L't':
		DecodeElementText( CElement( Ele), tpl, GXWINDOW::DTPT_FORMAT_TEXT);
		break;
	case L'C':
	case L'c':
		DecodeElementText( CElement( Ele), tpl, GXWINDOW::DTPT_COMPLEX);
		break;
	case L'R':
	case L'r':
		tpl.Insert( GXWINDOW::CDecoderTextPiece());
		break;
	case L'B':
	case L'b':
		DecodeElementButton( CElement( Ele), tpl);
		break;
	case L'E':
	case L'e':
		DecodeElementEmotion( CElement( Ele), tpl);
		break;
	default:
		break;
	}
}

std::string WStringToString( const std::wstring& wstr)
{
	int nCount = WideCharToMultiByte( CP_ACP, 0, wstr.c_str(), -1, NULL, NULL, NULL, NULL);
	assert( nCount);
	char* szBuffer = new char[nCount];

	WideCharToMultiByte( CP_ACP, 0, wstr.c_str(), -1, szBuffer, nCount, NULL, NULL);
	std::string str( szBuffer, nCount - 1);
	delete[] szBuffer;
	return str;
};

} // namespace INLINECLASS {

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextFormatManager		CTextFormatManager::s_Instance;

void CTextFormatManager::Load( const wchar_t* szFile, bool bLoadFromMem)
{
	Clear();

	XMLIO::CFileReader fr;

	if ( XMLIO::EC_SUCCESS == fr.OpenSyncFile( szFile))
	{
		XMLIO::CNodeList NodeList = fr.GetRootNode().GetChildren();
		for ( XMLIO::CNodeList::iterator it = NodeList.begin(); it != NodeList.end(); ++ it)
		{
			XMLIO::CINode& Node = *it;
			if ( _wcsicmp( L"TextFormat", Node.GetName().c_str()))
				continue;

			XMLIO::CNodeList TFEles = Node.GetChildren();
			for ( XMLIO::CNodeList::iterator tfit = TFEles.begin(); tfit != TFEles.end(); ++ tfit)
			{
				INLINECLASS::CElement		element( *tfit);
				TextFormat		tf;
				std::wstring	name;

				int nid = element.ReadInteger( L"id", L"ID", -1);

				if( !element.ReadString( L"name", L"Name", name))
				{
					std::wstringstream		wss;
					wss << L"tf-" << nid;
					name = wss.str();
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

					std::wstring		strUnderline;
					element.ReadString( L"normalunderline", L"Normalunderline", strUnderline);
					if ( 0 == _wcsicmp( L"true", strUnderline.c_str()))
						tf.m_nNormalEffectType = tf.m_nNormalEffectType | GXWINDOW::FET_UNDERLINE;

					effect_type = element.ReadInteger( L"selecteffecttype", L"Selecteffecttype");
					if ( 1 == effect_type)
						tf.m_nSelectEffectType = tf.m_nSelectEffectType | GXWINDOW::FET_SHADOW;
					else if ( 2 == effect_type)
						tf.m_nSelectEffectType = tf.m_nSelectEffectType | GXWINDOW::FET_STROKE;

					element.ReadString( L"selectunderline", L"Selectunderline", strUnderline);
					if ( 0 == _wcsicmp( L"true", strUnderline.c_str()))
						tf.m_nSelectEffectType = tf.m_nSelectEffectType | GXWINDOW::FET_UNDERLINE;
				}

				m_tfmap[nid] = tf;
				m_name2id[name] = nid;
				m_id2name[nid] = name;

				if( m_nIdGenerator <= nid)
					m_nIdGenerator = nid + 1;
			}
		}
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
	if ( NULL == fulltext)
		return;

	XMLIO::CFileReader fr;
	std::string str = INLINECLASS::WStringToString( fulltext);
	if ( XMLIO::EC_SUCCESS == fr.OpenData( str.c_str(), str.length()))
	{
		XMLIO::CNodeList nodes = fr.GetRootNode().GetChildren();
		for ( XMLIO::CNodeList::iterator it = nodes.begin(); it != nodes.end(); ++ it)
		{
			XMLIO::CINode& EleChild = *it;
			switch( EleChild.GetType())
			{
			case XMLIO::NT_TEXT:
				{
					std::wstring text = EleChild.GetText();
					if ( text.empty())
						continue;
					tpl.Insert( GXWINDOW::CDecoderTextPiece( text.c_str()));
				}
				break;
			case XMLIO::NT_ELEMENT:
				INLINECLASS::DecodeElement( EleChild, tpl);
			    break;
			default:
			    continue;
			}
		}
	}
}

void CXMLTextEncoder::AddTextPiece( const GXWINDOW::CDecoderTextPiece& tp)
{
	INLINECLASS::CComInitScope		scope;
}

} // namespace GXWADDIN {