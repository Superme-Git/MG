#include <map>
#include <list>
#include <vector>
#include <string>
#include <assert.h>
#include "octetsiobase.h"

namespace XMLIO {
namespace OCTETSIO {

class CMemoryNode;
class CMemoryException {};

CMemoryObject::CMemoryObject() : m_data( NULL), m_size(0), m_pos( 0) {}
CMemoryObject::CMemoryObject( const unsigned char* data, size_t size) : m_data( data), m_size( size), m_pos( 0) {}
CMemoryObject::~CMemoryObject() { assert( 0 == m_pos || m_size == m_pos); }

void CMemoryObject::Initialize( const unsigned char* data, size_t size)
{
	m_data = data;
	m_size = size;
	m_pos = 0;
}

int CMemoryObject::MakeRootNode( CMemoryNode& node, CStringDictionary& dict)
{
	try
	{
		dict.Make( *this);
		node.Make( *this, dict);
	}
	catch( CMemoryException&)
	{
		return -1;
	}
	return 0;
}

const unsigned char* CMemoryObject::Data() const { return m_data; }
size_t CMemoryObject::Size() const { return m_size; }
size_t CMemoryObject::Pos() const { return m_pos; }

void CMemoryObject::CheckSize( size_t s) const
{
	if( m_pos + s > m_size)
	{
		assert( false);
		throw CMemoryException();
	}
}

template<typename T> void CMemoryObject::PopByte(T &t) const
{
	CheckSize( sizeof(t));
	t = *((T*)(m_data + m_pos));
	m_pos += sizeof(t);
}

unsigned int CMemoryObject::PopByte32() const
{
	unsigned int	data;
	PopByte( data);
	return byteorder_32( data);
}

unsigned short CMemoryObject::PopByte16() const
{
	unsigned short data;
	PopByte( data);
	return byteorder_16( data);
}

unsigned char CMemoryObject::PopByte8() const
{
	unsigned char		data;
	PopByte( data);
	return data;
}

unsigned int CMemoryObject::PopCompactUint32() const
{
	CheckSize( sizeof(unsigned char));
	switch ( m_data[m_pos] & 0xe0)
	{
	case 0xe0:
		PopByte8();
		return PopByte32();
	case 0xc0:
		return PopByte32() & ~0xc0000000;
	case 0xa0:
	case 0x80:
		return PopByte16() & ~0x8000;
	default:
		return PopByte8();
	}
}

size_t CMemoryObject::GetNextSize() const
{
	return (size_t)PopCompactUint32();
}

unsigned int CMemoryObject::GetNextUnsignedInteger() const
{
	return PopCompactUint32();
}

const unsigned char* CMemoryObject::GetNextString( size_t size) const
{
	const unsigned char* result = m_data + m_pos;
	m_pos += size;
	return result;
}

CMemoryString::CMemoryString() : m_data( L""), m_length( 0) {}
CMemoryString::CMemoryString( const CMemoryObject& mo) { Make( mo);}
CMemoryString::CMemoryString( const wchar_t* str, size_t size) : m_data( str), m_length( size) {}
CMemoryString::~CMemoryString() {}

CMemoryString::CMemoryString( const CMemoryString& src) : m_data( src.m_data), m_length( src.m_length) {}
CMemoryString& CMemoryString::operator=( const CMemoryString& src)
{
	if( this == &src)
		return *this;
	m_data = src.m_data;
	m_length = src.m_length;
	return *this;
}

void CMemoryString::Make( const CMemoryObject& mo)
{
	m_length = mo.GetNextSize();
	m_data = (const wchar_t*)mo.GetNextString( m_length);
	m_length /= sizeof(wchar_t);
	m_length --;
}

void CMemoryString::Clear()
{
	m_data = L"";
	m_length = 0;
}

const wchar_t* CMemoryString::GetString() const { return m_data; }
size_t CMemoryString::GetLength() const { return m_length; }
bool CMemoryString::operator<( const CMemoryString& dst) const
{
	return wcscmp( GetString(), dst.GetString()) < 0;
}

class CStringDictionary::CStringVector : public std::vector<CMemoryString>
{
public:
	CStringVector() {}
	~CStringVector() {}
};

CStringDictionary::CStringDictionary() : m_data( new CStringVector) {}
CStringDictionary::CStringDictionary( const CMemoryObject& mo) : m_data( new CStringVector)
{
	Make( mo);
}

CStringDictionary::~CStringDictionary() { delete m_data; }

void CStringDictionary::Make( const CMemoryObject& mo)
{
	size_t count = mo.GetNextSize();
	m_data->reserve( count);
	for( size_t i = 0; i < count; i ++)
		m_data->push_back( CMemoryString( mo));
}

void CStringDictionary::Clear()
{
	m_data->clear();
}

const CMemoryString& CStringDictionary::GetString( size_t index) const
{
	if( index >= m_data->size())
		return m_null;
	else
		return m_data->at( index);
}

const CMemoryString* CStringDictionary::TryGetString( size_t index) const
{
	if( index >= m_data->size())
		return NULL;
	else
		return &m_data->at( index);
}

size_t CStringDictionary::GetCount() const
{
	return m_data->size();
}

class CMemoryAttributesMap::CMemoryStringMap : public std::map<CMemoryString,size_t> 
{
public:
	CMemoryStringMap() {}
	~CMemoryStringMap() {}
};

CMemoryAttributesMap::CMemoryAttributesMap() : m_map( new CMemoryStringMap) {}
CMemoryAttributesMap::CMemoryAttributesMap( const CMemoryObject& mo, const CStringDictionary& dict) : m_map( new CMemoryStringMap)
{
	Make( mo, dict);
}
CMemoryAttributesMap::~CMemoryAttributesMap() { delete m_map; }

void CMemoryAttributesMap::Make( const CMemoryObject& mo, const CStringDictionary& dict)
{
	size_t scount =	mo.GetNextSize();
	for( size_t i = 0; i < scount; i ++)
	{
		size_t name = mo.GetNextSize();
		size_t value = mo.GetNextSize();
		m_map->insert( std::make_pair( dict.GetString( name), value));
	}
}
void CMemoryAttributesMap::Clear()
{
	m_map->clear();
}

CMemoryAttributesMap::CMemoryAttributesMap( const CMemoryAttributesMap& src) : m_map( new CMemoryStringMap( *src.m_map)) {}
CMemoryAttributesMap& CMemoryAttributesMap::operator=( const CMemoryAttributesMap& src)
{
	if( this == &src)
		return *this;
	*m_map = *src.m_map;
	return *this;
}

size_t CMemoryAttributesMap::GetAttribute( const CMemoryString& name) const
{
	CMemoryStringMap::const_iterator it = m_map->find( name);
	if( it == m_map->end())
		return -1;
	const size_t& ms = it->second;
	return ms;
}

void CMemoryAttributesMap::GetNames( CStringDictionary& names) const
{
	names.m_data->reserve( m_map->size());
	for( CMemoryStringMap::const_iterator it = m_map->begin(); it != m_map->end(); ++ it)
	{
		const CMemoryString& name = it->first;
		names.m_data->push_back( name);
	}
}

class CMemoryNodeList::CNodeList : public std::vector<CMemoryNode>
{
public:
	CNodeList() {}
	~CNodeList() {}
};

CMemoryNodeList::CMemoryNodeList() : m_list( new CNodeList){}
CMemoryNodeList::CMemoryNodeList( const CMemoryObject& mo, const CStringDictionary& dict) : m_list( new CNodeList)
{
	Make( mo, dict);
}
CMemoryNodeList::~CMemoryNodeList() { delete m_list; }

CMemoryNodeList::CMemoryNodeList( const CMemoryNodeList& src) : m_list( new CNodeList( *src.m_list)) {}
CMemoryNodeList& CMemoryNodeList::operator=( const CMemoryNodeList& src) 
{
	if( this == &src)
		return *this;
	*m_list = *src.m_list;
	return *this;
}

void CMemoryNodeList::Clear()
{
	m_list->clear();
}

size_t CMemoryNodeList::GetCount() const { return m_list->size(); }
const CMemoryNode& CMemoryNodeList::GetAt( size_t n) const { return m_list->at( n); }

void CMemoryNodeList::Make( const CMemoryObject& mo, const CStringDictionary& dict) 
{
	size_t scount = mo.GetNextSize();
	m_list->resize( scount);
	for( size_t i = 0; i < scount; i ++)
		(*m_list)[i].Make( mo, dict);
}

CMemoryNode::CMemoryNode() {}

CMemoryNode::CMemoryNode( const CMemoryObject& mo, const CStringDictionary& dict) 
{
	Make( mo, dict);
}

CMemoryNode::~CMemoryNode() {}

void CMemoryNode::Make( const CMemoryObject& mo, const CStringDictionary& dict)
{
	m_type = mo.GetNextUnsignedInteger();
	m_name = mo.GetNextSize();
	m_text = mo.GetNextSize();

	m_attributes.Make( mo, dict);
	m_children.Make( mo, dict);
}

void CMemoryNode::Clear()
{
	m_attributes.Clear();
	m_children.Clear();
}

CMemoryNode::CMemoryNode( const CMemoryNode& src) 
	:  m_type( src.m_type), m_name( src.m_name), m_text( src.m_text), m_attributes( src.m_attributes), m_children( src.m_children)
{}

CMemoryNode& CMemoryNode::operator=( const CMemoryNode& src) 
{
	if( this == &src)
		return *this;
	m_type = src.m_type;
	m_name = src.m_name;
	m_text = src.m_text;
	m_attributes = src.m_attributes;
	m_children = src.m_children;
	return *this;
}

unsigned int CMemoryNode::GetNodeType() const { return m_type; }				
size_t CMemoryNode::GetName() const { return m_name; }
size_t CMemoryNode::GetText() const { return m_text; }
const CMemoryAttributesMap& CMemoryNode::GetAttributesMap() const { return m_attributes; }
const CMemoryNodeList& CMemoryNode::GetChildren() const { return m_children; }


} // namespace OCTETSIO {
} // namespace XMLIO {
