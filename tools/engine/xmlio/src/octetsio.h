#pragma once

namespace XMLIO {
namespace OCTETSIO {

	using PFS::fsize_t;

class COctetsINode : public CAbstractINode
{
	const CMemoryNode*			m_node;
	const CStringDictionary*	m_dict;
public:
	COctetsINode( const CMemoryNode& src, const CStringDictionary& dict)
		: m_node( &src), m_dict( &dict) {}
	virtual ~COctetsINode() {}
public:
	virtual CAbstractINode* Clone() const
	{
		return new COctetsINode( *m_node, *m_dict);
	}
public:
	virtual NodeType GetType() const
	{
		return (NodeType)m_node->GetNodeType();
	}

	virtual size_t GetChildrenCount() const
	{
		return m_node->GetChildren().GetCount();
	}

	virtual bool GetChildAt( size_t index, CINode& rNode) const
	{
		return rNode.Construct( new COctetsINode( m_node->GetChildren().GetAt( index), *m_dict) );
	}

	virtual void GetAttributeNames( CAttributesNames& names) const
	{
		CStringDictionary		sd;
		m_node->GetAttributesMap().GetNames( sd);

		size_t count = sd.GetCount();
		names.clear();
		names.reserve( count);
		for( size_t i = 0; i < count; i ++)
		{
			const CMemoryString& str = sd.GetString( i);
			names.push_back( std::wstring( str.GetString(), str.GetLength()));
		}
	}

	virtual bool GetAttribute( const std::wstring& name, std::wstring& value) const
	{
		size_t	index = m_node->GetAttributesMap().GetAttribute( CMemoryString( name.c_str(), name.length()));
		const CMemoryString* pStr = m_dict->TryGetString( index);
		if ( pStr == NULL )
		{
			// 按理说,这里返回false时不应该修改value的值,但是现在既有的代码多数没有检测此返回值,
			// 所以, 这里执行clear()动作等同于空属性值作为缺省属性.
			value.clear();
			return false;
		}
		value.assign( pStr->GetString(), pStr->GetLength() );
		return true;
	}

	virtual void GetText( std::wstring& value) const
	{
		const CMemoryString& text = m_dict->GetString( m_node->GetText());
		value.assign( text.GetString(), text.GetLength());
	}

	virtual void GetSubText( std::wstring& value) const
	{
		const CMemoryNodeList& mnl = m_node->GetChildren();
		size_t count = mnl.GetCount();
		for( size_t i = 0; i < count; i ++)
		{
			const CMemoryNode& mn =	mnl.GetAt( i);
			if( NT_TEXT == mn.GetNodeType())
			{
				const CMemoryString& text = m_dict->GetString( mn.GetText());
				value.assign( text.GetString(), text.GetLength());
				return;
			}
		}
		// 没有找到,返回空字符串
		value.clear();
	}

	virtual void GetName( std::wstring& value) const
	{
		const CMemoryString& name = m_dict->GetString( m_node->GetName());
		value.assign( name.GetString(), name.GetLength());
	}
};

class COctetsReader : public CAbstractReader
{
	PFS::CImageData			m_source;
	CMemoryObject			m_data;
	CMemoryNode				m_root;
	CStringDictionary		m_dict;
	enum { PRETYPE_SIZE = 4, };
public:
	COctetsReader() {}
	virtual ~COctetsReader() { Close(); }
private:
	int MakeRoot()
	{
		assert( m_source.GetSize() >= PRETYPE_SIZE);
		fsize_t size = m_source.GetSize() - PRETYPE_SIZE;
		if( 0 == size || size > MAXDWORD )
			return EC_PARSE_XML_CONTEXT;
		{
			unsigned int type = *((unsigned int*)m_source.GetData());
			if(  OCTETS_IO_PREFIX != type)
				return EC_PARSE_XML_CONTEXT;
		}
		m_data.Initialize( (const unsigned char*)m_source.GetData() + PRETYPE_SIZE, (size_t)size);
		if( 0 == m_data.MakeRootNode( m_root, m_dict))
			return EC_SUCCESS;
		else
			return EC_PARSE_XML_CONTEXT;

	}
public:
	virtual int OpenData( const PFS::CImageData& data)
	{
		Close();
		m_source = data;
		return MakeRoot();		
	}
	virtual int OpenData( const PFS::CDataBlock& data)
	{
		return OpenData( PFS::DataBlockToImageData( data));
	}
	virtual int OpenNativeFile( const std::wstring& filename)
	{
		PFSX::CNativeFile		file;

		if( !file.Open( filename, PFS::FM_EXCL, PFS::FA_RDONLY))
			return PFS::CEnv::GetLastError();
		return OpenFile( file);
	}
	virtual int OpenFile( PFS::CBaseFile& bf)
	{
		return OpenData( bf.GetImage());
	}
	virtual void Close() 
	{
		if( PFS::CAbstactImageImplement* aii = m_source.Detach())
			aii->Release();
		
		m_root.Clear();
		m_dict.Clear();
	}

	virtual bool GetRootNode(CINode& rNode) const
	{
		return rNode.Construct( new COctetsINode( m_root, m_dict ) );
	}

public:
	// 还原XML文本
	virtual bool EncodeToXML( std::wstring& str );

	// 转换为二进制数据块
	virtual bool EncodeToBinary( PFS::CDataBlock& data)
	{
		// 直接Copy，提高效率
		if( m_source.GetSize() > 0 )
		{
			data.Assign( m_source.GetData(), (size_t)m_source.GetSize() );
			return true;
		}
		else
		{
			return false;
		}
	}
};

} // namespace OCTETSIO {
} // namespace XMLIO {
