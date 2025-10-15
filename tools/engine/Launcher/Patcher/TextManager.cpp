#include "StdAfx.h"
#include "TextManager.h"
#include "../../pfs/pfs2.h"
#include "../../xmlio/xmlio.h"

const size_t LEN = 1024;
std::wstring CTextLoader::m_null;

class CXMLTextLoader : public CTextLoader
{
	typedef std::map< int, std::wstring>	CTextMap;

	CTextMap		m_texts;

public:
	virtual bool Load( const std::wstring& file)
	{
		XMLIO::CFileReader reader;
		if( XMLIO::EC_SUCCESS != reader.OpenFile( file))
		{
			return false;
		}

		XMLIO::CINode root;
		reader.GetRootNode( root);
		XMLIO::CINode child;

		size_t count = root.GetChildrenCount();
		for( size_t i=0; i<count; i++)
		{
			if( !root.GetChildAt( i, child))
			{
				continue;
			}

			if( child.GetType() != XMLIO::NT_ELEMENT )
			{
				// not a child element
				continue;
			}

			int id = child.GetAttributeInteger( L"id", 0);
			if( 0 == id)
			{
				id = child.GetAttributeInteger( L"id", -1);
				if( -1 == id)
				{
					return false;
				}
			}

			std::wstring& value = m_texts[id];
			child.GetAttribute( L"name", value);
			std::wstring::size_type off = 0;
			while( true)
			{
				// 将字符串中的 \n 串替换为真正的换行字符
				std::wstring::size_type pos = value.find( L"\\n", off);
				if( std::wstring::npos == pos)
				{
					break;
				}
				value.replace( pos, 2, L"\n");
				off = pos + 1;
			}
		}

		return true;
	}

	virtual const std::wstring& GetText( int id)
	{
		CTextMap::const_iterator res = m_texts.find( id);
		if( res != m_texts.end())
		{
			return res->second;
		}

		return m_null;
	}
};

class CDLLTextLoader : public CTextLoader
{
	std::wstring	m_buffer;
	HMODULE			m_module;

public:

	CDLLTextLoader() : m_module( NULL) 
	{
	}

	~CDLLTextLoader()
	{
		if( m_module)
		{
			::FreeLibrary( m_module);
			m_module = NULL;
		}
	}

	virtual bool Load( const std::wstring& file)
	{
		m_module = ::LoadLibrary( file.c_str());
		return m_module != NULL;
	}

	virtual const std::wstring& GetText( int id)
	{
		if( m_module)
		{
			m_buffer.resize( LEN);
			int res = ::LoadString( m_module, id, (LPTSTR)m_buffer.c_str(), LEN);
			if( res > 0 )
			{
				m_buffer.resize( res);
				return m_buffer;
			}
		}
		return m_null;
	}
};

class CTXTTextLoader : public CTextLoader
{
public:
	virtual bool Load( const std::wstring& file)
	{
		// todo
		return false;
	}

	virtual const std::wstring& GetText( int id)
	{
		// todo
		return m_null;
	}
};

CTextManager::CTextManager( )
: m_loader( NULL)
{
}

CTextManager::~CTextManager(void)
{
	if( m_loader)
	{
		delete m_loader;
		m_loader = NULL;
	}
}

bool CTextManager::Init( FILE_TYPE type, const std::wstring& file)
{
	switch( type)
	{
	case FILE_XML:
		m_loader = new CXMLTextLoader;
		break;
	case FILE_DLL:
		m_loader = new CDLLTextLoader;
		break;
	default:
		m_loader = new CTXTTextLoader;
		break;
	}

	return m_loader->Load( file);
}

const std::wstring& CTextManager::GetText( int id)
{
	if( m_loader)
	{
		return m_loader->GetText( id);
	}

	return CTextLoader::m_null;
}

const std::wstring& CTextManager::FormatText( const wchar_t* pszFormat, va_list* vl)
{
	m_buffer.resize( LEN);
	DWORD res = ::FormatMessage( FORMAT_MESSAGE_FROM_STRING, pszFormat, 0, 0, (LPTSTR)m_buffer.c_str(), LEN, vl);
	if( res > 0)
	{
		m_buffer.resize( res);
		return m_buffer;
	}

	return CTextLoader::m_null;
}

std::wstring CTextManager::GetText2( int id)
{
	return GetText( id);
}

const std::wstring& GlobalGetText( int id)
{
	return CTextManager::GetInstance().GetText( id);
}

std::wstring GlobalGetText2( int id)
{
	return CTextManager::GetInstance().GetText2( id);
}

const std::wstring& GlobalFormatText( int id, ...)
{
	const std::wstring& str = GlobalGetText( id);
	if( str.empty())
	{
		return str;
	}
	
	va_list vl;
	va_start( vl, id);
	const std::wstring& res = CTextManager::GetInstance().FormatText( str.c_str(), &vl);
	va_end( vl);

	return res;
}