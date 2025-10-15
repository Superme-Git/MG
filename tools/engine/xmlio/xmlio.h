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
	std::wstring GetSubText() const; // �����ȡ��һ���ӽڵ���ı�
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
	// 1.����XML�ļ����ļ�������Ҫ��������BOMǰ׺
	// 1.1 ����UTF16LE�����XML���ݣ���Ҫ��0xFF 0xFEǰ׺(0xFEFF)
	// 1.2 ����UTF16BE�����XML���ݣ���Ҫ��0xFE 0xFFǰ׺(0xFFFE)
	// 1.3 ����UTF8�����XML���ݣ���Ҫ��0xEF 0xBB 0xBF
	// 2.����X2O��ʽ�Ķ������ļ�(�ļ�ǰ׺0xEFCDAB89)

	// ��ȡ������PFSϵͳ��Nativeϵͳ�е�XML�ļ�
	// filename������PFS·������Ҳ�������ļ�ϵͳ·����
	int OpenFile( const std::wstring& filename);
	
	// ��ȡ������Nativeϵͳ�е�XML�ļ�
	// filenameֻ�����ļ�ϵͳ·����
	int OpenNativeFile( const std::wstring& filename);

	 // Ŀǰ�� OpenFile ������ȫһ��
	int OpenSyncFile( const std::wstring& filename);
	
	// ��ȡ������CBaseFile�����ṩ��XML�ļ�
	int OpenFile( PFS::CBaseFile& bf );

public:
	// 1.����XML���ݣ�������Ҫ��������BOMǰ׺
	// 1.1 ����UTF16LE�����XML���ݣ���Ҫ��0xFF 0xFEǰ׺(0xFEFF)
	// 1.2 ����UTF16BE�����XML���ݣ���Ҫ��0xFE 0xFFǰ׺(0xFFFE)
	// 1.3 ����UTF8�����XML���ݣ���Ҫ��0xEF 0xBB 0xBF
	// 2.����X2O��ʽ�Ķ��������ݣ�������XML�ļ����ݣ�(ǰ׺0xEFCDAB89)
	int OpenData( const PFS::CDataBlock& data );
	
	// ����Ҫ��ͬ int OpenData( const PFS::CDataBlock& data );
	int OpenData( const void *buf, size_t size );

public:
	// ����UTF16LE�����XML���ݣ��������κ�BOMǰ׺
	int OpenString( const std::wstring& xml );

	// ����UTF16LE�����XML���ݣ��������κ�BOMǰ׺���ַ�����Ҫ��L'\0'��β
	int OpenString( const wchar_t* xml);

public:
	void CloseFile();

	XMLIO_PERFORMANCE_DEPRECATED(bool GetRootNode(CINode& rNode) const)
	const CINode GetRootNode() const;
	
	bool GetRootNode(CINode& rNode) const;

public:
	//
	// ���ݸ�ʽת���ӿ�
	//

	// ���ΪXML��ת��Ϊ������XML����
	// ���Ϊ������XML����ֱ�ӻ�ԭ�����Ʊ�ʾ��ʽ
	void SaveAsOctets( PFS::CDataBlock& data);
	
	// ����XML�ı�����ԭʼ��XML�ı���ԭ������
	// ���ڶ�����XML����ת�����߼��ȼ۵�XML�ַ�����
	void SaveAsString( std::wstring& str);

public:
	bool GetAsWriter( CFileWriter& fw ); // ֻ��MSXMLReader֧��CFileWriter
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

	// ����UTF16LE��ʽ(BSTR)��XML���ݵ��ļ�����BOMǰ׺��û��L'\0'��β
	// filename �ȿ�����pfsϵͳ·�����ļ���Ҳ������Nativeϵͳ�ļ�
	int Save( const std::wstring& filename, int iAction = 0);

	// ����UTF16LE��ʽ(BSTR)��XML���ݵ�Nativeϵͳ�ļ�����BOMǰ׺��û��L'\0'��β
	int SaveNative( const std::wstring& filename);

	// ����UTF16LE��ʽ(BSTR)��XML���ݵ��ļ�����CBaseFile����BOMǰ׺��û��L'\0'��β
	int Save( PFS::CBaseFile& bf);

	// ����UTF16LE��ʽ(BSTR)��XML���ݣ�û��BOMǰ׺��û��L'\0'��β
	int SaveData( PFS::CDataBlock& data );
	
	// ����UTF16LE��ʽ(BSTR)��XML���ݣ���BOMǰ׺��û��L'\0'��β
	int SaveDataWithBOM( PFS::CDataBlock& data );

	// ����UTF16LE��ʽ(BSTR)��XML�ַ�����û��BOMǰ׺����L'\0'��β
	int SaveAsString( std::wstring& rXMLString );

	// ����Ϊ������XML���ݣ�ת�����ӿڣ�
	int SaveAsOctets( PFS::CDataBlock& rOctetsData );

public:
	bool GetAsReader( CFileReader& fr );

private:
	int TranslateErrorCode( int code);
};

} // namespace XMLIO {