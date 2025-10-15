#include <Windows.h>
#include <string>
#include <map>
#include <hash_map>
#include <vector>
#include <assert.h>
#include "geninclude.h"
#include "uiloader.h"
#include "xmlioloader.h"

namespace GXWADDIN
{


inline void SetGXWFontEffectType( TEXTSTATE& ts)
{
	ts.effect = GXWINDOW::FET_NONE;

	if ( ts.bUnderline)
		ts.effect = GXWINDOW::FET_UNDERLINE;

	if ( ts.bShadow)
		ts.effect |= GXWINDOW::FET_SHADOW;
	else if ( ts.bOutline)
		ts.effect |= GXWINDOW::FET_STROKE;
}


GXWINDOW::COLOR StrARGB2GXWColor( const wchar_t* argb, GXWINDOW::COLOR defaultvalue)
{
	if ( NULL == argb)
		return 0;

	bool bError = false;
	GXWINDOW::COLOR color = 0;
	static const int bytes = 8;
	int bits = sizeof( GXWINDOW::COLOR) * bytes;

	std::wstring tmp = std::wstring(argb) + L",";
	const wchar_t* p = tmp.c_str();
	std::wstring value;

	while ( *p)
	{
		if ( *p == L',')
		{
			int c = _wtoi( value.c_str());
			bits -= bytes;
			color |= ( ( c & 0xff) << bits);
			value.clear();
		}
		else
		{
			if ( bError = !isdigit( *p))
				break;
			value.push_back( *p);
		}

		++ p;
	}

	if ( bError)
		return defaultvalue;
	else
		return color;
}

void Load(XMLIO::CINode node, CComponentAttributes& ca, bool bReadImagePFSMode)
{
	if ( 0 == _wcsicmp( node.GetName().c_str(), L"Content") || 0 == _wcsicmp( node.GetName().c_str(), L"Desktop"))
	{
		XMLIO::CNodeList nl;
		node.GetChildren( nl);

		for ( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
			Load( *it, ca, bReadImagePFSMode);
	}
	else
	{
		std::wstring name;
		node.GetAttribute( L"n", name);
		if ( name.empty())
			name = node.GetName();
		CComponentAttributes& nca = ca.NewChildAttribute( name.c_str());

		XMLIO::CAttributesNames an;
		node.GetAttributeNames( an);
		if ( bReadImagePFSMode)
		{
			for ( XMLIO::CAttributesNames::const_iterator it = an.begin(); it != an.end(); ++ it)
			{
				const std::wstring& name = *it;
				std::wstring value;
				node.GetAttribute( name, value);
				if ( name == L"f")
				{
					//size_t pos = value.find_last_of( L"/\\");
					//if ( pos != std::wstring::npos)
					//	value = value.substr( pos + 1, value.length());
					for ( std::wstring::iterator it = value.begin(), ite = value.end(); it != ite; ++ it)
					{
						wchar_t& ch = *it;
						if ( ch == L'\\')
							ch = L'/';
					}
					value = L"/" + value;
				}
				nca.InsertAttribute( name.c_str(), value.c_str());
			}
		}
		else
		{
			for ( XMLIO::CAttributesNames::const_iterator it = an.begin(); it != an.end(); ++ it)
			{
				const std::wstring& name = *it;
				std::wstring value;
				node.GetAttribute( name, value);
				nca.InsertAttribute( name.c_str(), value.c_str());
			}
		}


		XMLIO::CNodeList nl;
		node.GetChildren( nl);

		for ( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode n = *it;
			if ( 0 == _wcsicmp( n.GetName().c_str(), L"Content"))
				Load( n, ca, bReadImagePFSMode);
			else
				Load( n, nca, bReadImagePFSMode);
		}
	}
}

void LoadTextFormat( XMLIO::CINode node, CTextFormatMap& tfm)
{
	XMLIO::CNodeList nl;
	node.GetChildren( nl);

	if ( 0 == _wcsicmp( node.GetName().c_str(), L"FontProfile"))
	{
		for ( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
		{
			XMLIO::CINode& profile = *it;
			TEXTFORMAT tf;
			tf.nFontType = profile.GetIntAttribute( L"fontid");

			XMLIO::CNodeList states;
			profile.GetChildren( states);
			for ( XMLIO::CNodeList::iterator it = states.begin(); it != states.end(); ++ it )
			{
				XMLIO::CINode& state = *it;
				TEXTSTATE ts;
				int id = state.GetIntAttribute( L"id");

				if ( id >= 0 && id <= TSF_STATE_MAXNUM)
				{
					std::wstring value;
					state.GetAttribute( L"color", value);
					ts.color = StrARGB2GXWColor( value.c_str());

					state.GetAttribute( L"color2", value);
					ts.color2 = StrARGB2GXWColor( value.c_str());

					state.GetAttribute( L"effcolor", value);
					ts.effectColor = StrARGB2GXWColor( value.c_str());

					state.GetAttribute( L"underline", value);
					ts.bUnderline = 0 == _wcsicmp( L"true", value.c_str());

					state.GetAttribute( L"shadow", value);
					ts.bShadow = 0 == _wcsicmp( L"true", value.c_str());

					state.GetAttribute( L"outline", value);
					ts.bOutline = 0 == _wcsicmp( L"true", value.c_str());
					ts.bGraudal = ts.color != ts.color2;

					SetGXWFontEffectType( ts);

					tf.textStateMap.insert( std::make_pair( id, ts));
				}
			}

			if ( !tf.textStateMap.empty())
			{
				std::wstring value;
				profile.GetAttribute( L"name", value);
				tfm.insert( std::make_pair( value, tf));
			}
		}
	}
	else
	{
		for ( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
			LoadTextFormat( *it, tfm);
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


CXMLIOGuiLoader::CXMLIOGuiLoader()
{

}

CXMLIOGuiLoader::~CXMLIOGuiLoader()
{

}

CComponentAttributes CXMLIOGuiLoader::LoadGuiFile( const wchar_t* filename, bool bReadImagePFSMode)
{
	assert( filename);

	CComponentAttributes ca;
	XMLIO::CFileReader fr;

#ifdef USE_NAIVEFILE
	if ( fr.OpenNativeFile( filename) == XMLIO::EC_SUCCESS)
#else
	if ( fr.OpenFile( filename) == XMLIO::EC_SUCCESS)
#endif
	{
		XMLIO::CINode root;
		fr.GetRootNode( root);
		Load( root, ca, bReadImagePFSMode);
	}

	return ca;
}

void CXMLIOGuiLoader::LoadTextFormatFile( const wchar_t* filename, CTextFormatMap& tfm)
{
	assert( filename);

	XMLIO::CFileReader fr;

#ifdef USE_NAIVEFILE
	if ( fr.OpenNativeFile( filename) == XMLIO::EC_SUCCESS)
#else
	if ( fr.OpenFile( filename) == XMLIO::EC_SUCCESS)
#endif
	{
		XMLIO::CINode root;
		fr.GetRootNode( root);
		LoadTextFormat( root, tfm);
	}

}



}