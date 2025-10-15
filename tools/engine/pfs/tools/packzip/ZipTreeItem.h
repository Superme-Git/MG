#pragma once
#include <list>
#include <string>

class CZipTreeItem
{
public:
	enum ZIPITEM_STATE
	{
		ZS_PATH = 0x00000001,			// Ŀ¼
		ZS_CHECK = 0x00000002,			// ѡ��
		ZS_PACK = 0x00000004,			// pack
		ZS_SETCHECK = 0x00000008,		// ��Ϊ����ѡ��
		ZS_ZIPNODEF = 0x00000010,		// ziplevel����Ĭ��ѹ����ʽ
		ZS_ATTRNODEF = 0x00000020,		// attribute����Ĭ�Ϸ�ʽ
		ZS_FILENOEXIST = 0x00000040,	// �ļ�������
		ZS_NOINIT = 0x00000080,			// û�г�ʼ��
		ZS_HASCHILD = 0x00000100,		// �к��ӽڵ㣬����û�г�ʼ��������ʱ��������
		ZS_CHILDPACKAGE = 0x00000200,	// �����Ӱ�����
	};

	CZipTreeItem(): m_nZipLevel( -1), m_attributes( 0), m_state( ZS_CHECK), m_parent( NULL), m_level( 0) {}
	virtual ~CZipTreeItem() {}

	bool operator==( const CZipTreeItem& right)
	{
		return m_name == right.m_name;
	}
	// ���أ�������Ϊ�˽���Ŀ¼���͡��Ӱ�������뵽ǰ��
	friend bool operator<( const CZipTreeItem& left, const CZipTreeItem& right)
	{
		if( (left.GetIsPath() == right.GetIsPath()) && (left.GetIsChildPackage() == right.GetIsChildPackage()))
			return left.m_name < right.m_name;
		return left.GetIsPath() ? true : left.GetIsChildPackage();
	}
	CZipTreeItem( const CZipTreeItem& right) { *this = right; }
	CZipTreeItem& operator=( const CZipTreeItem& right)
	{
		if( this == &right)
			return *this;
		m_state = right.m_state;
		m_name = right.m_name;
		m_nZipLevel = right.m_nZipLevel;
		m_attributes = right.m_attributes;
		m_child = right.m_child;
		m_url = right.m_url;
		m_parent = right.m_parent;
		m_mount = right.m_mount;
		//m_relatename = right.m_relatename;
		m_level = right.m_level;
		return *this;
	}

	// ��Ϊlist,��ΪҪ����ʹ��insert,��vector���Ʋ���̫��
	typedef std::list<CZipTreeItem>	CZipTreeItemList;
	CZipTreeItemList& GetChild() { return m_child; }
	const CZipTreeItemList& GetChild() const { return m_child; }

	bool GetIsPath() const { return GetState( ZS_PATH); }
	void SetIsPath( bool bIsPath){ SetState( ZS_PATH, bIsPath); }

	bool GetIsCheck() const { return GetState( ZS_CHECK); }
	void SetIsCheck( bool bIsCheck) { SetState( ZS_CHECK, bIsCheck); }

	bool GetIsPack() const { return GetState( ZS_PACK); }
	void SetIsPack( bool bIsPack) { SetState( ZS_PACK, bIsPack); }

	bool GetIsSetCheck() const { return GetState( ZS_SETCHECK); }
	void SetIsSetCheck( bool bIsSetCheck) { SetState( ZS_SETCHECK, bIsSetCheck); }

	bool GetIsZipNoDef() const { return GetState( ZS_ZIPNODEF); }
	void SetIsZipNoDef( bool bIsZipNoDef) { SetState( ZS_ZIPNODEF, bIsZipNoDef); }

	bool GetIsAttrNoDef() const { return GetState( ZS_ATTRNODEF); }
	void SetIsAttrNoDef( bool bIsAttrNoDef) { SetState( ZS_ATTRNODEF, bIsAttrNoDef); }

	bool GetIsFileNoExist() const { return GetState( ZS_FILENOEXIST); }
	void SetIsFileNoExist( bool bIsFileNoExist) { SetState( ZS_FILENOEXIST, bIsFileNoExist); }

	bool GetIsNoInit() const { return GetState( ZS_NOINIT); }
	void SetIsNoInit( bool bIsNoInit) { SetState( ZS_NOINIT, bIsNoInit); }

	bool GetIsHasChild() const { return GetState( ZS_HASCHILD); }
	void SetIsHasChild( bool bIsHasChild) { SetState( ZS_HASCHILD, bIsHasChild); }

	bool GetIsChildPackage() const { return GetState( ZS_CHILDPACKAGE); }
	void SetIsChildPackage( bool bIsChildPackage) { SetState( ZS_CHILDPACKAGE, bIsChildPackage); }

	const std::wstring& GetName() const { return m_name; }
	void SetName( const std::wstring& name) { m_name = name; }

	int GetAttributes() const { return m_attributes; }
	void SetAttributes( int attributes) { m_attributes = attributes; }

	int GetZipLevel() const { return m_nZipLevel; }
	void SetZipLevel( int nLevel) { m_nZipLevel = nLevel; }

	const CZipTreeItem* GetParent() const { return m_parent; }
	CZipTreeItem* GetParent() { return m_parent; }
	void SetParent( CZipTreeItem* parent) { m_parent = parent; }

	const std::wstring& GetUrl() { return m_url; }
	void SetUrl( const std::wstring& url) { m_url = url; }

	const std::wstring& GetMount() { return m_mount; }
	void SetMount( const std::wstring& mount) { m_mount = mount; }

	//const std::wstring& GetRelateName() { return m_relatename; }
	//void SetRelateName( const std::wstring& relatename) { m_relatename = relatename; }

	int GetLevel() const { return m_level; }
	void SetLevel( int level) { m_level = level; }

	//std::wstring GetMountFileName()
	//{
	//	//std::wstring name( m_relatename);
	//	//name.erase( 0, name.find_first_of( L'/')+1);
	//	//return m_mount + L'/' + name;
	//	return m_mount + L"/" + m_relatename;
	//}

public:
	static void SetItemState( CZipTreeItem& item, ZIPITEM_STATE type, bool value)
	{
		switch( type)
		{
		case ZS_CHECK:
		case ZS_PACK:
		case ZS_SETCHECK:
			item.SetState( type, value);
			break;
		default:
			return;
		}
		CZipTreeItemList& child = item.GetChild();
		for( CZipTreeItemList::iterator itor = child.begin(); itor != child.end(); ++itor)
			SetItemState( *itor, type, value);
	}

private:
	bool GetState( ZIPITEM_STATE type) const { return !!(m_state & type); }
	void SetState( ZIPITEM_STATE type, bool value) { value ? ( m_state |= type) : ( m_state &= ~type); }

	std::wstring					m_name;
	UINT							m_state;
	int								m_nZipLevel;	// ѹ����ʽ
	int								m_attributes;
	std::wstring					m_url;			// download url
	std::wstring					m_mount;		
	//std::wstring					m_relatename;	// �����mount������
	int								m_level;		// ���Ĳ�������Ҫ��ʾ��

	CZipTreeItem*					m_parent;		// parent
	CZipTreeItemList				m_child;		// child
};
