
#if _MSC_VER == 1310
#define _PTRDIFF_T_DEFINED
typedef int ptrdiff_t;
#define _itow_s( v, s, l, f) _itow( v, s, f)
#define _i64tow_s( v, s, l, f) _i64tow( v, s, f)
#define _ui64tow_s( v, s, l, f) _ui64tow( v, s, f)
#endif

#include "../../pfs/pfs2.h"
#include <atlbase.h>
#include "../xmlio.h"
#include "xmlfunc.h"
#include "abstractio.h"

#include "../msdom.h"
#include "msxmlio.h"
#include "octetsiobase.h"
#include "octetsio.h"
#include "xml2binary.h"

//
// 说明：USE_DLL_OCTETS_IO仅在xmliolibfulllib DEBUG工程中被定义。其它工程不定义。
// 未定义此宏的工程(缺省配置)将直接包含octetsiobase.cpp，使得使用本工程的程序不需
// 再单独加载octetsio库。
// 换言之，对于定义了USE_DLL_OCTETS_IO宏的xmliofulllib DEBUG工程，使用它的DLL或EXE
// 必须同时导入octetsio.lib，运行时必须加载octetsio.dll。
//
// xmliofulllib被用在了GameClient.exe中，为了提高Debug版GameClient的运行速度，
// xmliofulllib使用了Release版的octetsio.dll，因此，它不再需要octetsiobase.cpp。
// 
#ifndef USE_DLL_OCTETS_IO
#include "octetsiobase.cpp"
#endif

namespace MSXMLDOM
{
	static CMSComServerScoped g_dummy_com_inatance;
}// namespace MSXMLDOM

namespace XMLIO {

CINode::Data::~Data() { if( Node) Node->Delete(); }

CINode::CINode()
	: m_data( new Data)
{
}

CINode::CINode( const CINode& src)
	: m_data( new Data)
{
	if( src.m_data->Node != NULL )
		m_data->Node = src.m_data->Node->Clone();
	else
		m_data->Node = NULL;
}

CINode::CINode( CAbstractINode* pNode )
	: m_data( new Data)
{
	assert( pNode != NULL );
	m_data->Node = pNode;
}

bool CINode::Construct( CAbstractINode* pNode )
{
	assert( m_data != NULL );
	assert( pNode != NULL );
	if( m_data->Node != NULL ) 
	{
		m_data->Node->Delete();
	}
	m_data->Node = pNode;
	return pNode != NULL ;
}

CINode::~CINode()
{
	delete m_data;
}

CINode& CINode::operator =( const CINode& src)
{
	if( this == &src)
		return *this;

	// 注意：下面的操作很有必要，否则会导致内存泄露
	if( m_data->Node != NULL )
		m_data->Node->Delete();

	if( src.m_data->Node != NULL )
		m_data->Node = src.m_data->Node->Clone();
	else
		m_data->Node = NULL;

	return *this;
}

NodeType CINode::GetType() const
{
	if( NULL == m_data->Node)
	{
		assert( m_data->Node);
		return NT_UNKNOW;
	}
	return m_data->Node->GetType();
}

size_t CINode::GetChildrenCount() const
{
	if( NULL == m_data->Node)
	{
		assert( m_data->Node);
		return 0;
	}
	else
		return m_data->Node->GetChildrenCount();
}

const CINode CINode::GetChildAt( size_t index) const
{
	CINode temp;
	if( NULL != m_data->Node)
		m_data->Node->GetChildAt( index, temp);
	else
		assert( m_data->Node);
	return temp;
}

bool CINode::GetChildAt( size_t index, CINode& rNode ) const
{
	if( NULL == m_data->Node)
	{
		assert( m_data->Node);
		return false;
	}
	else
		return m_data->Node->GetChildAt( index, rNode);
}

void CINode::GetChildren( CNodeList& nl) const
{
	if( NULL == m_data->Node)
	{
		assert( m_data->Node);
		return;
	}

	size_t scount = m_data->Node->GetChildrenCount();
	nl.resize( scount);
	size_t j = 0;
	for( size_t i = 0; i < scount; i ++)
	{
		CINode& rNode = nl[j];
		if( m_data->Node->GetChildAt( i, rNode) )
		{
			j ++;
		}
		else
		{
			assert( false );
		}
	}
	if( j != scount )
		nl.resize(j);
}

const CNodeList CINode::GetChildren() const
{
	CNodeList nl;
	GetChildren( nl);
	return nl;
}

void CINode::GetAttributeNames( CAttributesNames& names) const
{
	if( m_data->Node != NULL ) 
		m_data->Node->GetAttributeNames( names);
	else
		assert( m_data->Node);
}

const CAttributesNames CINode::GetAttributeNames() const
{
	CAttributesNames	names;
	GetAttributeNames( names);
	return names;
}

std::wstring CINode::GetAttribute( const std::wstring& name) const
{
	std::wstring value;
	if( m_data->Node != NULL ) 
		m_data->Node->GetAttribute( name, value);
	else
		assert( m_data->Node);
	return value;
}

bool CINode::GetAttribute( const std::wstring& name, std::wstring& rstrValue ) const
{
	if( m_data->Node == NULL )
	{
		assert( m_data->Node);
		return false;
	}
	return m_data->Node->GetAttribute( name, rstrValue);
}

int CINode::GetAttributeInteger( const std::wstring& name, int defaultvalue) const
{
	if( m_data->Node == NULL )
	{
		assert( m_data->Node);
		return defaultvalue;
	}

	std::wstring value;
	m_data->Node->GetAttribute( name, value);
	if( value.empty())
		return defaultvalue;
	else
		return _wtoi( value.c_str());
}

__int64 CINode::GetAttributeInt64( const std::wstring& name, __int64 defaultvalue) const
{
	if( m_data->Node == NULL )
	{
		assert( m_data->Node);
		return defaultvalue;
	}

	std::wstring value;
	m_data->Node->GetAttribute( name, value);
	if( value.empty())
		return defaultvalue;
	else
		return _wtoi64( value.c_str());
}

unsigned int CINode::GetAttributeUnsignedInteger( const std::wstring& name, unsigned int defaultvalue) const
{
	if( m_data->Node == NULL )
	{
		assert( m_data->Node);
		return defaultvalue;
	}
	std::wstring value;
	m_data->Node->GetAttribute( name, value);
	if( value.empty())
		return defaultvalue;
	else
		return wcstoul( value.c_str(), NULL, 10);
}

double CINode::GetAttributeFloat( const std::wstring& name, double defaultvalue) const
{
	if( m_data->Node == NULL )
	{
		assert( m_data->Node);
		return defaultvalue;
	}
	std::wstring value;
	m_data->Node->GetAttribute( name, value);
	if( value.empty())
		return defaultvalue;
	else
		return _wtof( value.c_str());
}	

std::wstring CINode::GetText() const
{
	std::wstring value;
	if( m_data->Node != NULL )
		m_data->Node->GetText( value);
	else
		assert( m_data->Node);
	return value;
}

std::wstring CINode::GetSubText() const
{
	std::wstring value;
	if( m_data->Node != NULL )
		m_data->Node->GetSubText( value);
	else
		assert( m_data->Node);
	return value;
}

std::wstring CINode::GetName() const
{
	assert( m_data->Node);
	std::wstring value;
	if( m_data->Node != NULL )
		m_data->Node->GetName( value);
	else
		assert( m_data->Node);
	return value;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFileReader::Data::~Data() { if( Reader) delete Reader; }
void CFileReader::Data::CreateReader( unsigned int type)
{
	if( OCTETS_IO_PREFIX == type)
		Reader = new OCTETSIO::COctetsReader;
	else
		Reader = new MSXMLIO::CCMSXMLReader;
}

void CFileReader::Data::DeleteReader()
{
	if( Reader)
	{
		delete Reader;
		Reader = NULL;
	}
}

CFileReader::CFileReader() : m_data( new Data) {}
CFileReader::~CFileReader() { delete m_data; }

int CFileReader::OpenFile( const std::wstring& filename)
{
	PFSX::CSyncFile  file;
	if( file.Open( filename))
		return OpenFile( file);
	return PFS::CEnv::GetLastError();
}

int CFileReader::OpenNativeFile( const std::wstring& filename)
{
	m_data->Reader = new MSXMLIO::CCMSXMLReader;
	if( EC_SUCCESS != m_data->Reader->OpenNativeFile( filename))
	{
		delete m_data->Reader;
		m_data->Reader = new OCTETSIO::COctetsReader;
		int code = m_data->Reader->OpenNativeFile( filename);
		if( EC_SUCCESS != code)
			return code;
	}
	return EC_SUCCESS;
}

int CFileReader::OpenSyncFile( const std::wstring& filename)
{
	return OpenFile( filename);
}

int CFileReader::OpenFile( PFS::CBaseFile& bf)
{
	assert( NULL == m_data->Reader);

	unsigned int prefix = 0;
	size_t readsize = sizeof(unsigned int);
	readsize = bf.Read( &prefix, readsize);
	if( readsize == PFS::FILE_SIZE_ERROR)
		return PFS::CEnv::GetLastError();
	if( sizeof(unsigned int) != readsize)
		return EC_FILE_SIZE_LACK;
	bf.Seek( 0, PFS::FSM_SET);
	m_data->CreateReader( prefix);
	return m_data->Reader->OpenFile( bf);
}

int CFileReader::OpenData( const PFS::CDataBlock& data)
{
	assert( NULL == m_data->Reader);
	if( data.GetSize() < sizeof( unsigned int))
		return EC_FILE_SIZE_LACK;
	unsigned int prefix = *((unsigned int*)data.GetData());
	m_data->CreateReader( prefix);
	return m_data->Reader->OpenData( data);
}

int CFileReader::OpenData(const void *buf, size_t size)
{
	assert( NULL == m_data->Reader);
	return OpenData( PFS::CDataBlock( buf,size));
}

int CFileReader::OpenString( const wchar_t* xml)
{
	assert( NULL == m_data->Reader);
	if( NULL == xml || 0 == xml[0])
		return EC_PARSE_XML_CONTEXT;

	MSXMLIO::CCMSXMLReader*	reader = new MSXMLIO::CCMSXMLReader;

	int error = reader->OpenString( xml);
	if( EC_SUCCESS == error)
		m_data->Reader = reader;
	else
		delete reader;
	return error;
}

int CFileReader::OpenString( const std::wstring& xml)
{
	return OpenString( xml.c_str());
}

void CFileReader::CloseFile()
{
	m_data->DeleteReader();
}

const CINode CFileReader::GetRootNode() const
{
	CINode rNode;
	if( m_data->Reader )
		m_data->Reader->GetRootNode(rNode);
	else
		assert( m_data->Reader);
	return rNode;
}

bool CFileReader::GetRootNode(CINode& rNode) const
{
	if( m_data->Reader )
		return m_data->Reader->GetRootNode(rNode);
	else
	{
		assert( m_data->Reader);
		return false;
	}
}

void CFileReader::SaveAsOctets( PFS::CDataBlock& data)
{
	if( m_data->Reader )
	{
		// 使用Reader提供的编码接口，提高转换效率
		if( m_data->Reader->EncodeToBinary(data) )
		{
			return;
		}
	}
	else
	{
		assert( m_data->Reader);
		return;
	}

	class CSave
	{
		CFileReader& m_fr;
		PFS::CDataBlock& m_data;

		typedef std::map<std::wstring,size_t>	CStringMap;
		typedef std::vector<std::wstring>	CStringDict;

		CStringMap		m_map;
		CStringDict		m_dict;
	public:
		CSave( CFileReader& fr, PFS::CDataBlock& data) : m_fr( fr), m_data( data) {}
		~CSave() {}
	private:
		inline void InsertToStringMap( const std::wstring& str)
		{
			// 不再检测空串,我们为空串也建立字典
			m_map.insert( std::make_pair( str, 0));
		}

		void MakeNodeAttributesStringMap( const CINode& node)
		{
			CAttributesNames		names;
			node.GetAttributeNames( names);

			for( CAttributesNames::const_iterator it = names.begin(); it != names.end(); ++ it)
			{
				const std::wstring& name = *it;
				InsertToStringMap( name);

				std::wstring strValue;
				node.GetAttribute( name, strValue );

				InsertToStringMap( strValue );
			}
		}

		void MakeNodeChildrenStringMap( const CINode& node)
		{
			CNodeList		nl;
			node.GetChildren( nl);

			for( CNodeList::const_iterator it = nl.begin(); it != nl.end(); ++ it)
			{
				const CINode& sub = *it;
				MakeNodeStringMap( sub);
			}
		}

		void MakeNodeStringMap( const CINode& node)
		{
			InsertToStringMap( node.GetName());
			InsertToStringMap( node.GetText());

			MakeNodeAttributesStringMap( node);
			MakeNodeChildrenStringMap( node);
		}

		void MakeStringVector()
		{
			size_t  size = m_map.size();
			m_dict.reserve( size);

			size = 0;
			for( CStringMap::iterator it = m_map.begin(); it != m_map.end(); ++ it, size ++)
			{
				const std::wstring& str = it->first;
				m_dict.push_back( str);
				size_t& index = it->second;
				index = size;
			}
		}
		size_t GetNameIndex( const std::wstring& str)
		{
			CStringMap::const_iterator it = m_map.find( str);
			if( it == m_map.end())
				return -1;
			else
				return it->second;
		}

		class CMemoryOut
		{
			PFS::CDataBlock& m_data;
		public:
			CMemoryOut( PFS::CDataBlock& data) : m_data( data) {}
			~CMemoryOut() {}
		public:
			void push_byte( unsigned char t)
			{
				m_data.Append( &t, sizeof(t));
			}
			void push_byte( unsigned short t)
			{
				m_data.Append(  &t, sizeof(t));
			}
			void push_byte( unsigned int t)
			{
				m_data.Append( &t, sizeof(t));
			}

			void PushCompactUint32(unsigned int x)
			{
				if( x < 0x80) return push_byte( (unsigned char)x);
				else if( x < 0x4000) return push_byte(  OCTETSIO::CMemoryObject::byteorder_16( x | 0x8000));
				else if( x < 0x20000000) return push_byte( OCTETSIO::CMemoryObject::byteorder_32( x| 0xc0000000));
				push_byte( (unsigned char)0xe0);
				return push_byte( OCTETSIO::CMemoryObject::byteorder_32( x));
			}
			void PushSize( size_t size)
			{
				PushCompactUint32( (unsigned int)size);
			}
		public:
			void Push( NodeType type)
			{
				PushCompactUint32( ( unsigned int)type);
			}
			void Push( size_t size)
			{
				PushSize( size);
			}
			void Push( const std::wstring& str)
			{
				size_t size = (str.size() + 1)* sizeof(wchar_t);
				Push( size);
				m_data.Append( str.c_str(), size);
			}
		};
		void OutputStringVector( CMemoryOut& mo)
		{
			mo.Push( m_dict.size());
			for( CStringDict::const_iterator it = m_dict.begin(); it != m_dict.end(); ++ it)
			{
				const std::wstring& str = *it;
				mo.Push( str);
			}
		}
		void OutputNode( const CINode& node, CMemoryOut& mo)
		{
			mo.Push( node.GetType());
			mo.Push( GetNameIndex( node.GetName()));
			mo.Push( GetNameIndex( node.GetText()));

			{
				CAttributesNames		names;
				node.GetAttributeNames( names);
				mo.Push( names.size());
				for( CAttributesNames::const_iterator it = names.begin(); it != names.end(); ++ it)
				{
					const std::wstring& name = *it;
					mo.Push( GetNameIndex( name));
					std::wstring strValue;
					node.GetAttribute( name, strValue );
					mo.Push( GetNameIndex( strValue ) );
				}
			}

			{
				CNodeList		nl;
				node.GetChildren( nl);
				mo.Push( nl.size());
				for( CNodeList::const_iterator it = nl.begin(); it != nl.end(); ++ it)
				{
					const CINode& sub = *it;
					OutputNode( sub, mo);
				}
			}
		}
	public:
		void Do()
		{
			CINode root;
			if( !m_fr.GetRootNode(root) )
			{
				return ;
			}

			MakeNodeStringMap( root);
			MakeStringVector();

			CMemoryOut		mo( m_data);
			mo.push_byte( OCTETS_IO_PREFIX);
			OutputStringVector( mo);
			OutputNode( root, mo);
		}
	};

	CSave	save( *this, data);
	save.Do();
}

void CFileReader::SaveAsString( std::wstring& str)
{
	if( m_data->Reader == NULL )
	{
		assert( m_data->Reader );
		return ;
	}
	
	m_data->Reader->EncodeToXML( str );
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

struct CONode::Data : public CNoTrackData
{
	Data() {}
	Data( const MSXMLDOM::CWriterNode& src) : node( src) {}
	MSXMLDOM::CWriterNode		node;
};

CONode::~CONode() { delete m_data; }

CONode::CONode() : m_data( new Data) {}
CONode::CONode( const CONode& src) : m_data( new Data( src.m_data->node)) {}

CONode& CONode::operator=( const CONode& src)
{
	if( this == &src)
		return *this;
	m_data->node = src.m_data->node;
	return *this;
}

bool CONode::SetAttribute( const std::wstring& name, const std::wstring& value)
{
	return m_data->node.SetAttribute( name.c_str(), value.c_str());
}

bool CONode::SetAttribute( const wchar_t* pszName, const wchar_t* pszValue)
{
	return m_data->node.SetAttribute( pszName, pszValue);
}

bool CONode::SetAttribute( const std::wstring& name, int value)
{
	const size_t	size = 16;
	wchar_t		str[size];
	_itow_s( value, str, size, 10);
	return SetAttribute( name, std::wstring( str));
}

bool CONode::SetAttribute( const std::wstring& name, __int64 value)
{
	const size_t	size = 32;
	wchar_t		str[size];
	_i64tow_s( value, str, size, 10);
	return SetAttribute( name, std::wstring( str));
}

bool CONode::SetAttribute( const std::wstring& name, unsigned int value)
{
	const size_t	size = 32;
	wchar_t		str[size];
	_ui64tow_s( value, str, size, 10);
	return SetAttribute( name, std::wstring( str));
}

bool CONode::SetAttribute( const std::wstring& name, double value)
{
	const size_t	size = 32;
	wchar_t		str[size];
#if _MSC_VER == 1310
	swprintf( str, L"%f", value);
#else
	swprintf_s( str, size, L"%f", value);
#endif
	return SetAttribute( name, std::wstring( str));
}

CONode CONode::CreateSubElement( const std::wstring& name)
{
	CONode temp;
	m_data->node.CreateSubElement( name.c_str(), temp.m_data->node ); 
	return temp;
}

bool CONode::CreateSubElement( const wchar_t* pszName, CONode& rNode )
{
	return m_data->node.CreateSubElement( pszName, rNode.m_data->node ); 
}

bool CONode::InsertText( const std::wstring& text)
{
	return m_data->node.InsertText( text.c_str() );
}

bool CONode::InsertCData( const std::wstring& data)
{
	return m_data->node.InsertCData( data.c_str() );
}

bool CONode::InsertText( const wchar_t* pszText)
{
	return m_data->node.InsertText( pszText );
}

bool CONode::InsertCData( const wchar_t* pszCData)
{
	return m_data->node.InsertCData( pszCData );
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

struct CFileWriter::Data : public CNoTrackData
{
	MSXMLDOM::CWriter	writer;
};

CFileWriter::CFileWriter() : m_data( new Data)
{
}

CFileWriter::~CFileWriter() 
{
	delete m_data;
}

CONode CFileWriter::GetRootNode()
{
	CONode node;
	m_data->writer.GetRootNode(node.m_data->node);
	return node;
}

bool CFileWriter::GetRootNode(CONode& rNode)
{
	return m_data->writer.GetRootNode(rNode.m_data->node);
}

CONode CFileWriter::CreateElement( CONode& parent, const std::wstring& name)
{
	CONode temp;
	parent.CreateSubElement( name.c_str(), temp );
	return temp;
}

bool CFileWriter::CreateElement( CONode& parent, const wchar_t* pszName, CONode& rNode)
{
	return parent.CreateSubElement( pszName, rNode);
}

bool CFileWriter::CreateElement( CONode& parent, const std::wstring& name, CONode& rNode)
{
	return parent.CreateSubElement( name.c_str(), rNode);
}

bool CFileWriter::InsertText( CONode& node, const std::wstring& text)
{
	return node.InsertText( text);
}

bool CFileWriter::InsertCData( CONode& node, const std::wstring& data)
{
	return node.InsertCData( data);
}

bool CFileWriter::InsertText( CONode& node, const wchar_t* pszText)
{
	return node.InsertText( pszText );
}

bool CFileWriter::InsertCData( CONode& node, const wchar_t* pszCData)
{
	return node.InsertCData( pszCData );
}

int CFileWriter::Save( const std::wstring& filename, int iAction)
{
	return TranslateErrorCode( m_data->writer.Save( filename, iAction));
}

int CFileWriter::SaveNative( const std::wstring& filename)
{
	return TranslateErrorCode( m_data->writer.SaveNative( filename));
}

int CFileWriter::Save( PFS::CBaseFile& bf)
{
	return TranslateErrorCode( m_data->writer.Save( bf));
}

int CFileWriter::SaveData( PFS::CDataBlock& data)
{
	return TranslateErrorCode( m_data->writer.SaveData( data));
}

int CFileWriter::SaveDataWithBOM( PFS::CDataBlock& data)
{
	return TranslateErrorCode( m_data->writer.SaveDataWithBOM( data));
}

int CFileWriter::SaveAsString( std::wstring& rXMLString )
{
	return TranslateErrorCode( m_data->writer.SaveAsString( rXMLString ));
}

// 保存为二进制XML数据（转换器接口）
int CFileWriter::SaveAsOctets( PFS::CDataBlock& rOctetsData )
{
	CComPtr<IXMLDOMDocument> spDocument = m_data->writer.GetInterface();
	
	// 使用XML到二进制的转换器完成转换过程
	CXML2Binary _Translator;
	bool bRet = _Translator.Translate( spDocument, rOctetsData );
	return bRet ? EC_SUCCESS : EC_X2O_TRANSLATE_FAILED;
}

int CFileWriter::TranslateErrorCode( int code)
{
	switch( code)
	{
	case MSXMLDOM::EC_MSXMLDOM_XML_ENCODE:
		return EC_XML_ENCODE;
	default:
		return code;
	}
}

bool CFileWriter::GetAsReader( CFileReader& fr)
{
	fr.m_data->DeleteReader();
	MSXMLDOM::CReader reader( m_data->writer.GetInterface() );
	fr.m_data->Reader = new MSXMLIO::CCMSXMLReader( reader);
	return true;
}

// 只有MSXMLReader支持Writer
bool CFileReader::GetAsWriter( CFileWriter& fw )
{
	MSXMLIO::CCMSXMLReader* msxml = dynamic_cast<MSXMLIO::CCMSXMLReader*>( m_data->Reader);
	if( NULL == msxml ) 
	{
		PFS::CEnv::SetLastError( XMLIO::EC_NOT_SUPPORT_WRITER );
		return false;
	}
	
	return fw.m_data->writer.Construct( msxml->GetInterface() );
}


} // namespace XMLIO {

