#include "datadef.h"
#include "xmlloader.h"
#include "../../iolib/common/marshal.h"
#include <assert.h>
#include "../../pfs/pfs.h"
#include "../../xmlio/xmlio.h"

namespace LOADER {

class CLoader
{
	static const std::wstring s_strMountRoot;

	typedef std::list<std::wstring> CFileNameList;

	std::wstring		m_xmlfile;
	DATA::CItemList&	m_itemlist;
	DATA::CGroupList&	m_grouplist;
public:
	CLoader( const std::wstring& xmlfile, DATA::CItemList& il, DATA::CGroupList& gl) : m_xmlfile( xmlfile), m_itemlist( il), m_grouplist( gl) {}
	~CLoader() {}
private:
	int MountFileSytem( std::wstring& filename) const
	{
		std::wstring::size_type pos = m_xmlfile.rfind(  L'\\');
		if( std::wstring::npos == pos)
			pos = m_xmlfile.rfind(  L'/');
		if( std::wstring::npos == pos)
			return -1;
		filename = s_strMountRoot + L"/" + m_xmlfile.substr( pos + 1);
		if( PFS::CEnv::Mount( s_strMountRoot, m_xmlfile.substr( 0, pos), PFS::FST_NATIVE))
			return PFS::EC_SUCCESS;
		else
			return PFS::CEnv::GetLastError();
	}
	void ReadItemList( XMLIO::CINode& root, CFileNameList& fnlist)
	{
		const std::wstring	wstrEnumName = L"enum";
		const std::wstring	wstrObjectName = L"object";
		const std::wstring	wstrGroupName = L"group";
		const std::wstring	wstrIncludeName = L"include";

		XMLIO::CNodeList	nodes = root.GetChildren();
		for( XMLIO::CNodeList::iterator it = nodes.begin(); it != nodes.end(); ++ it)
		{
			XMLIO::CINode& onenode = *it;
			std::wstring name = onenode.GetName();
			if( 0 == _wcsicmp( name.c_str(), wstrEnumName.c_str()))
				ReadEnumItem( onenode);
			else if( 0 == _wcsicmp( name.c_str(), wstrObjectName.c_str()))
				ReadObjectItem( onenode);
			else if( 0 == _wcsicmp( name.c_str(), wstrGroupName.c_str()))
				ReadGroupItem( onenode);
			else if( 0 == _wcsicmp( name.c_str(), wstrIncludeName.c_str()))
				fnlist.push_back( onenode.GetAttribute( L"name"));
		} // for( XMLIO::CNodeList::iterator it = nodes.begin(); it != nodes.end(); + it)
	}

	void ReadGroupItem( XMLIO::CINode& node)
	{
		const std::wstring	wstrObjectName = L"object";
		const std::wstring	wstrGroupName = L"group";

		DATA::GroupSingleDef		gsd;
		gsd.name = node.GetAttribute( L"name");
		gsd.notes = node.GetAttribute( L"notes");
		XMLIO::CNodeList	items = node.GetChildren();

		for( XMLIO::CNodeList::iterator it = items.begin(); it != items.end(); ++ it)
		{
			XMLIO::CINode&		oneitem = *it;

			std::wstring		name = oneitem.GetName();
			if( 0 == _wcsicmp( name.c_str(), wstrObjectName.c_str()))
				gsd.objects.push_back( oneitem.GetAttribute( L"name"));
			else if( 0 == _wcsicmp( name.c_str(), wstrGroupName.c_str()))
				gsd.groups.push_back( oneitem.GetAttribute( L"name"));
		}
		m_grouplist.push_back( gsd);
	}

	void ReadObjectItem( XMLIO::CINode& node)
	{
		const std::wstring	wstrVariantName = L"variant";

		DATA::CClass		classobject;
		XMLIO::CNodeList	items = node.GetChildren();

		classobject.SetName( node.GetAttribute( L"name"));
		classobject.SetNotes( node.GetAttribute( L"notes"));
		classobject.SetParent( node.GetAttribute( L"parent"));
		classobject.SetNotify( 0 == _wcsicmp( node.GetAttribute( L"notify").c_str(), L"true"));

		for( XMLIO::CNodeList::iterator it = items.begin(); it != items.end(); ++ it)
		{
			XMLIO::CINode&		oneitem = *it;
			DATA::Variant		var;

			std::wstring		name = oneitem.GetName();
			if( 0 != _wcsicmp( name.c_str(), wstrVariantName.c_str()))
				continue;

			var.strName = oneitem.GetAttribute( L"name");
			var.strType = oneitem.GetAttribute( L"type");
			var.strKey = oneitem.GetAttribute( L"key");
			var.strValue = oneitem.GetAttribute( L"value");
			var.strNotes = oneitem.GetAttribute( L"notes");
			var.strDefault = oneitem.GetAttribute( L"default");
			classobject.GetVariantList().push_back( var);
		}
		m_itemlist.Insert( classobject);
	}

	void ReadEnumItem( XMLIO::CINode& node)
	{
		DATA::CEnumType		enumtype;
		XMLIO::CNodeList	items = node.GetChildren();

		enumtype.SetName( node.GetAttribute( L"name"));
		enumtype.SetNotes( node.GetAttribute( L"notes"));

		for( XMLIO::CNodeList::iterator it = items.begin(); it != items.end(); ++ it)
		{
			XMLIO::CINode&		oneitem = *it;
			DATA::EnumItem		ei;
			ei.strName = oneitem.GetName();
			ei.strValue = oneitem.GetAttribute( L"value");
			ei.strNotes = oneitem.GetAttribute( L"notes");
			enumtype.GetItemList().push_back( ei);
		}
		m_itemlist.Insert( enumtype);
	}

	void TryReadXMLFile( std::wstring& filename, CFileNameList& fnlist)
	{
		XMLIO::CFileReader		xmlfr;
		int nError = xmlfr.OpenFile( filename);
		if( XMLIO::EC_SUCCESS != nError)
			throw nError;
		ReadItemList( xmlfr.GetRootNode(), fnlist);
		xmlfr.CloseFile();
	}
public:
	int	DoLoad()
	{
		try
		{
			std::wstring	strFilename;
			int nError = MountFileSytem( strFilename);
			if( PFS::EC_SUCCESS != nError)
				throw nError;

			CFileNameList		fnlist;
			TryReadXMLFile( strFilename, fnlist);

			while( !fnlist.empty())
			{
				std::wstring& subname = *fnlist.begin();
				strFilename = s_strMountRoot + L"/" + subname;
				TryReadXMLFile( strFilename, fnlist);
				fnlist.pop_front();
			} // while( !fnlist.empty())

			throw 0;
		}
		catch( int nError)
		{
			PFS::CEnv::Unmount( s_strMountRoot);
			return nError;
		}
	}
};

const std::wstring CLoader::s_strMountRoot = L"/datapool/dpgen/xml";

int Load( const std::wstring& xmlfile, DATA::CItemList& il, DATA::CGroupList& gl)
{
	CLoader		lder( xmlfile, il, gl);
	return lder.DoLoad();
}

} // namespace LOADER {
