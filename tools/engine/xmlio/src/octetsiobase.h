#pragma once

#ifdef OCTETSIO_EXPORTS
#define OCTETSIO_EXPORTS_DLL __declspec( dllexport)
#else
#define OCTETSIO_EXPORTS_DLL
#endif

namespace XMLIO {
namespace OCTETSIO {

class CMemoryNode;
class CStringDictionary;
class CMemoryAttributesMap;

class OCTETSIO_EXPORTS_DLL CMemoryObject
{
	const unsigned char*	m_data;
	size_t					m_size;
	mutable size_t			m_pos;
private:
	CMemoryObject( const CMemoryObject&) {}
	CMemoryObject& operator=( const CMemoryObject&) { return *this; }
public:
	CMemoryObject();
	CMemoryObject( const unsigned char* data, size_t size);
	~CMemoryObject();

	void Initialize( const unsigned char* data, size_t size);
public:
	const unsigned char* Data() const;
	size_t Size() const;
	size_t Pos() const;
private:
	void CheckSize( size_t s) const;
	template<typename T> void PopByte(T &t) const;
public:
	static inline unsigned __int16 byteorder_16(unsigned __int16 x)
	{
		__asm ror x, 8
		return x;
	}
	static inline unsigned __int32 byteorder_32(unsigned __int32 x)
	{
		__asm mov eax, x
		__asm bswap eax
		__asm mov x, eax
		return x;
	}
public:
	unsigned int PopByte32() const;
	unsigned short PopByte16() const;
	unsigned char PopByte8() const;
	unsigned int PopCompactUint32() const;
public:
	size_t GetNextSize() const;
	unsigned int GetNextUnsignedInteger() const;
	const unsigned char* GetNextString( size_t size) const;
public:
	int MakeRootNode( CMemoryNode& node, CStringDictionary& dict);
};

class OCTETSIO_EXPORTS_DLL CMemoryString
{
	const wchar_t*		m_data;
	size_t				m_length;
public:
	CMemoryString();
	CMemoryString( const CMemoryObject& mo);
	CMemoryString( const wchar_t* str, size_t size);
	~CMemoryString();
public:
	CMemoryString( const CMemoryString& src);
	CMemoryString& operator=( const CMemoryString& src);
public:
	void Make( const CMemoryObject& mo);
	void Clear();
public:
	const wchar_t* GetString() const;
	size_t GetLength() const;
	bool operator<( const CMemoryString& dst) const;
};

class OCTETSIO_EXPORTS_DLL CStringDictionary
{
	friend CMemoryAttributesMap;

	class CStringVector;

	CStringVector*		m_data;
	CMemoryString		m_null;
private:
	CStringDictionary( const CStringDictionary&) {}
	CStringDictionary& operator=( const CStringDictionary&) { return *this; }
public:
	CStringDictionary();
	CStringDictionary( const CMemoryObject& mo);
	~CStringDictionary();
public:
	void Make( const CMemoryObject& mo);
	void Clear();
public:
	const CMemoryString& GetString( size_t index) const;

	const CMemoryString* TryGetString( size_t index) const;

	size_t GetCount() const;
};

class OCTETSIO_EXPORTS_DLL CMemoryAttributesMap
{
	class CMemoryStringMap;
	CMemoryStringMap*			m_map;
public:
	CMemoryAttributesMap();
	CMemoryAttributesMap( const CMemoryObject& mo, const CStringDictionary& dict);
	~CMemoryAttributesMap();
public:
	void Make( const CMemoryObject& mo, const CStringDictionary& dict);
	void Clear();
public:
	CMemoryAttributesMap( const CMemoryAttributesMap& src);
	CMemoryAttributesMap& operator=( const CMemoryAttributesMap& src);
public:
	size_t GetAttribute( const CMemoryString& name) const;
	void GetNames( CStringDictionary& names) const;
};

class OCTETSIO_EXPORTS_DLL CMemoryNodeList
{
	class CNodeList;
	CNodeList*		m_list;
public:
	CMemoryNodeList();
	CMemoryNodeList( const CMemoryObject& mo, const CStringDictionary& dict);
	~CMemoryNodeList();
public:
	CMemoryNodeList( const CMemoryNodeList& src);
	CMemoryNodeList& operator=( const CMemoryNodeList& src);
public:
	void Make( const CMemoryObject& mo, const CStringDictionary& dict);
	void Clear();
public:
	size_t GetCount() const;
	const CMemoryNode& GetAt( size_t n) const;
};

class OCTETSIO_EXPORTS_DLL CMemoryNode
{
	unsigned int				m_type;
	size_t						m_name;
	size_t						m_text;
	CMemoryAttributesMap		m_attributes;
	CMemoryNodeList				m_children;
public:
	CMemoryNode();
	CMemoryNode( const CMemoryObject& mo, const CStringDictionary& dict);
	~CMemoryNode();
public:
	void Make( const CMemoryObject& mo, const CStringDictionary& dict);
	void Clear();
public:
	CMemoryNode( const CMemoryNode& src) ;
	CMemoryNode& operator=( const CMemoryNode& src);
public:
	unsigned int GetNodeType() const;
	size_t GetName() const;
	size_t GetText() const;
	const CMemoryAttributesMap& GetAttributesMap() const;
	const CMemoryNodeList& GetChildren() const;
};

} // namespace OCTETSIO {
} // namespace XMLIO {
