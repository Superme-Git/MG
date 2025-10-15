#pragma once

#include "xmliocommon.h"

namespace XMLIO {

class CAbstractINode;
class CAbstractReader;

class CINode : public CNoTrackData
{
	friend class CFileReader;
	friend class CAbstractINode;

	struct	Data;
	Data*	m_data;

public:
	CINode();
	CINode( const CINode& src);
	virtual ~CINode();

	// construct from CAbstractINode
	CINode( CAbstractINode* pNode );
	bool Construct( CAbstractINode* pNode );

public:
	CINode& operator=( const CINode& src);

	NodeType GetType() const;
	void GetChildren( CNodeList& nl) const;

	XMLIO_PERFORMANCE_DEPRECATED(void GetChildren( CNodeList& nl))
	const CNodeList GetChildren() const;

	size_t GetChildrenCount() const;

	XMLIO_PERFORMANCE_DEPRECATED(bool GetChildAt( size_t index, CINode& rNode))
	const CINode GetChildAt( size_t index) const;

	bool GetChildAt( size_t index, CINode& rNode) const;

	void GetAttributeNames( CAttributesNames& names) const;

	XMLIO_PERFORMANCE_DEPRECATED(void GetAttributeNames( CAttributesNames& names))
	const CAttributesNames GetAttributeNames() const;

	XMLIO_PERFORMANCE_DEPRECATED(bool GetAttribute( const std::wstring& name, std::wstring& rstrValue ) const)
	std::wstring GetAttribute( const std::wstring& name) const;

	bool GetAttribute( const std::wstring& name, std::wstring& rstrValue ) const;

	int GetAttributeInteger( const std::wstring& name, int defaultvalue = 0) const;
	
	int GetIntAttribute( const std::wstring& name, int defaultvalue = 0) const
	{ return GetAttributeInteger( name, defaultvalue); }

	__int64 GetAttributeInt64( const std::wstring& name, __int64 defaultvalue = 0) const;

	__int64 GetInt64Attribute( const std::wstring& name, __int64 defaultvalue = 0) const
	{ return GetAttributeInt64( name, defaultvalue); }

	unsigned int GetAttributeUnsignedInteger( const std::wstring& name, unsigned int defaultvalue = 0) const;

	unsigned int GetUIntAttribute( const std::wstring& name, unsigned int defaultvalue = 0) const
	{ return GetAttributeUnsignedInteger( name, defaultvalue); }

	double GetAttributeFloat( const std::wstring& name, double defaultvalue = 0.0) const;

	double GetFloatAttribute( const std::wstring& name, double defaultvalue = 0.0) const
	{ return GetAttributeFloat( name, defaultvalue); }

	std::wstring GetText() const;
	std::wstring GetSubText() const; // 用与获取第一个子节点的文本
	std::wstring GetName() const;
};

class CFileReader : public CUncopyableObject
{
	friend class CFileWriter;
	struct Data;
	Data*		m_data;

public:
	CFileReader();
	virtual ~CFileReader();
public:
	// 1.解析XML文件，文件数据需要有完整的BOM前缀
	// 1.1 对于UTF16LE编码的XML数据，需要有0xFF 0xFE前缀(0xFEFF)
	// 1.2 对于UTF16BE编码的XML数据，需要有0xFE 0xFF前缀(0xFFFE)
	// 1.3 对于UTF8编码的XML数据，需要有0xEF 0xBB 0xBF
	// 2.解析X2O格式的二进制文件(文件前缀0xEFCDAB89)

	// 读取并解析PFS系统或Native系统中的XML文件
	// filename可以是PFS路径名，也可以是文件系统路径名
	int OpenFile( const std::wstring& filename);
	
	// 读取并解析Native系统中的XML文件
	// filename只能是文件系统路径名
	int OpenNativeFile( const std::wstring& filename);

	 // 目前和 OpenFile 功能完全一样
	int OpenSyncFile( const std::wstring& filename);
	
	// 读取并解析CBaseFile对象提供的XML文件
	int OpenFile( PFS::CBaseFile& bf );

public:
	// 1.解析XML数据，数据需要有完整的BOM前缀
	// 1.1 对于UTF16LE编码的XML数据，需要有0xFF 0xFE前缀(0xFEFF)
	// 1.2 对于UTF16BE编码的XML数据，需要有0xFE 0xFF前缀(0xFFFE)
	// 1.3 对于UTF8编码的XML数据，需要有0xEF 0xBB 0xBF
	// 2.解析X2O格式的二进制数据（二进制XML文件数据）(前缀0xEFCDAB89)
	int OpenData( const PFS::CDataBlock& data );
	
	// 参数要求同 int OpenData( const PFS::CDataBlock& data );
	int OpenData( const void *buf, size_t size );

public:
	// 解析UTF16LE编码的XML数据，不能有任何BOM前缀
	int OpenString( const std::wstring& xml );

	// 解析UTF16LE编码的XML数据，不能有任何BOM前缀，字符串需要以L'\0'结尾
	int OpenString( const wchar_t* xml);

public:
	void CloseFile();

	XMLIO_PERFORMANCE_DEPRECATED(bool GetRootNode(CINode& rNode) const)
	const CINode GetRootNode() const;
	
	bool GetRootNode(CINode& rNode) const;

public:
	//
	// 数据格式转换接口
	//

	// 如果为XML，转换为二进制XML对象
	// 如果为二进制XML对象，直接还原二进制表示形式
	void SaveAsOctets( PFS::CDataBlock& data);
	
	// 对于XML文本，将原始的XML文本还原出来。
	// 对于二进制XML对象，转换成逻辑等价的XML字符串。
	void SaveAsString( std::wstring& str);

public:
	bool GetAsWriter( CFileWriter& fw ); // 只有MSXMLReader支持CFileWriter
};

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CONode : public CNoTrackData
{
	friend class CFileWriter;

	struct	Data;
	Data*	m_data;
	
public:
	CONode();
	CONode( const CONode& src);
	virtual ~CONode();

public:
	CONode& operator=( const CONode& src);
	bool SetAttribute( const wchar_t* pszName, const wchar_t* pszValue);
	bool SetAttribute( const std::wstring& name, const std::wstring& value);
	bool SetAttribute( const std::wstring& name, int value);
	bool SetAttribute( const std::wstring& name, __int64 value);
	bool SetAttribute( const std::wstring& name, double value);
	bool SetAttribute( const std::wstring& name, unsigned int value);

	XMLIO_PERFORMANCE_DEPRECATED(bool CreateSubElement( const wchar_t* pszName, CONode& rNode ))
	CONode CreateSubElement( const std::wstring& name);
	bool CreateSubElement( const wchar_t* pszName, CONode& rNode );

	bool InsertText( const std::wstring& text);
	bool InsertText( const wchar_t* pszText);
	bool InsertCData( const std::wstring& data);
	bool InsertCData( const wchar_t* pszCData);
};

class CFileWriter : public CUncopyableObject
{
	friend class CFileReader;
	friend class CONode;
	struct Data;
	Data*	m_data;
public:
	CFileWriter();
	virtual ~CFileWriter();
public:
	XMLIO_PERFORMANCE_DEPRECATED(bool GetRootNode(CONode& rNode))
	CONode GetRootNode();
	bool GetRootNode(CONode& rNode);

	XMLIO_PERFORMANCE_DEPRECATED(bool CreateElement( CONode& parent, const wchar_t* pszName, CONode& rNode))
	CONode CreateElement( CONode& parent, const std::wstring& name);
	bool CreateElement( CONode& parent, const std::wstring& name, CONode& rNode);
	bool CreateElement( CONode& parent, const wchar_t* pszName, CONode& rNode);

	bool InsertText( CONode& node, const std::wstring& text);
	bool InsertText( CONode& node, const wchar_t* pszText);
	bool InsertCData( CONode& node, const std::wstring& data);
	bool InsertCData( CONode& node, const wchar_t* pszCData);

	// 保存UTF16LE格式(BSTR)的XML数据到文件，有BOM前缀，没有L'\0'结尾
	// filename 既可以是pfs系统路径的文件，也可以是Native系统文件
	int Save( const std::wstring& filename, int iAction = 0);

	// 保存UTF16LE格式(BSTR)的XML数据到Native系统文件，有BOM前缀，没有L'\0'结尾
	int SaveNative( const std::wstring& filename);

	// 保存UTF16LE格式(BSTR)的XML数据到文件对象CBaseFile，有BOM前缀，没有L'\0'结尾
	int Save( PFS::CBaseFile& bf);

	// 保存UTF16LE格式(BSTR)的XML数据，没有BOM前缀，没有L'\0'结尾
	int SaveData( PFS::CDataBlock& data );
	
	// 保存UTF16LE格式(BSTR)的XML数据，有BOM前缀，没有L'\0'结尾
	int SaveDataWithBOM( PFS::CDataBlock& data );

	// 保存UTF16LE格式(BSTR)的XML字符串，没有BOM前缀，有L'\0'结尾
	int SaveAsString( std::wstring& rXMLString );

	// 保存为二进制XML数据（转换器接口）
	int SaveAsOctets( PFS::CDataBlock& rOctetsData );

public:
	bool GetAsReader( CFileReader& fr );

private:
	int TranslateErrorCode( int code);
};

} // namespace XMLIO {