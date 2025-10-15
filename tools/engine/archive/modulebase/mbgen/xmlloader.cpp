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
public:
	CLoader( const std::wstring& xmlfile, DATA::CItemList& il) : m_xmlfile( xmlfile), m_itemlist( il) {}
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
		const std::wstring	wstrNotifyName = L"notify";
		const std::wstring	wstrStateName = L"state";
		const std::wstring	wstrModuleName = L"module";
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
			else if( 0 == _wcsicmp( name.c_str(), wstrNotifyName.c_str()))
				ReadNotifyItem( onenode);
			else if( 0 == _wcsicmp( name.c_str(), wstrStateName.c_str()))
				ReadStateItem( onenode);
			else if( 0 == _wcsicmp( name.c_str(), wstrModuleName.c_str()))
				ReadModuleItem( onenode);
			else if( 0 == _wcsicmp( name.c_str(), wstrIncludeName.c_str()))
				fnlist.push_back( onenode.GetAttribute( L"name"));
		} // for( XMLIO::CNodeList::iterator it = nodes.begin(); it != nodes.end(); + it)
	}
	void ReadModuleItem( XMLIO::CINode& node)
	{
		class CAttributeDefault 
		{
			XMLIO::CINode& m_node;
		public:
			CAttributeDefault( XMLIO::CINode& node) : m_node( node) {}
			~CAttributeDefault() {}
		public:
			bool GetAttribute( const std::wstring& name, bool bDefault)
			{
				const std::wstring value = m_node.GetAttribute( name);
				if( value.empty())
					return bDefault;
				if( 0 == _wcsicmp( L"true", value.c_str()))
					return true;
				if( 0 == _wcsicmp( L"false", value.c_str()))
					return false;
				return bDefault;
			}
		};

		DATA::CModule		module;

		module.SetName( node.GetAttribute( L"name"));
		module.SetNotes( node.GetAttribute( L"notes"));

		CAttributeDefault		ad( node);
		module.SetMessage( ad.GetAttribute( L"message", false));
		module.SetRender( ad.GetAttribute( L"render", false));
		module.SetState( ad.GetAttribute( L"state", true));
		module.SetIdle( ad.GetAttribute( L"idle", false));
		module.SetReset( ad.GetAttribute( L"reset", true));

		const std::wstring	wstrStateName = L"state";
		const std::wstring	wstrNotifyName = L"notify";
		const std::wstring	wstrProtocolName = L"protocol";
		const std::wstring	wstrDatacoreName = L"datacore";
		const std::wstring	wstrNameName = L"name";
		
		XMLIO::CNodeList	items = node.GetChildren();

		for( XMLIO::CNodeList::iterator it = items.begin(); it != items.end(); ++ it)
		{
			XMLIO::CINode&		oneitem = *it;

			std::wstring		name = oneitem.GetName();
			if( 0 == _wcsicmp( name.c_str(), wstrStateName.c_str()))
				module.GetStateList().push_back( oneitem.GetAttribute( wstrNameName));
			else if( 0 == _wcsicmp( name.c_str(), wstrNotifyName.c_str()))
				module.GetNotifyList().push_back( oneitem.GetAttribute( wstrNameName));
			else if( 0 == _wcsicmp( name.c_str(), wstrProtocolName.c_str()))
				module.GetProtocolList().push_back( oneitem.GetAttribute( wstrNameName));
			else if( 0 == _wcsicmp( name.c_str(), wstrDatacoreName.c_str()))
			{
				std::wstring strid = oneitem.GetAttribute( L"id");
				if( !strid.empty())
					module.GetDataCoreList().push_back( std::make_pair( _wtol( strid.c_str()), _wtol( strid.c_str() ) ) );
				else
					module.GetDataCoreList().push_back( std::make_pair( _wtol( oneitem.GetAttribute( L"from").c_str()), _wtol( oneitem.GetAttribute( L"to").c_str() ) ) );
			}
		}

		m_itemlist.Insert( module);
	}
	void ReadStateItem( XMLIO::CINode& node)
	{
		DATA::CState		state;
		state.SetName( node.GetAttribute( L"name"));
		state.SetNotes( node.GetAttribute( L"notes"));
		m_itemlist.Insert( state);
	}
	void ReadNotifyItem( XMLIO::CINode& node)
	{
		const std::wstring	wstrVariantName = L"variant";

		DATA::CNotify		classobject;
		XMLIO::CNodeList	items = node.GetChildren();

		classobject.SetName( node.GetAttribute( L"name"));
		classobject.SetNotes( node.GetAttribute( L"notes"));
		classobject.SetParent( node.GetAttribute( L"parent"));

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

	void ReadObjectItem( XMLIO::CINode& node)
	{
		const std::wstring	wstrVariantName = L"variant";

		DATA::CData		classobject;
		XMLIO::CNodeList	items = node.GetChildren();

		classobject.SetName( node.GetAttribute( L"name"));
		classobject.SetNotes( node.GetAttribute( L"notes"));
		classobject.SetParent( node.GetAttribute( L"parent"));

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

const std::wstring CLoader::s_strMountRoot = L"/datapool/mbgen/xml";

int Load( const std::wstring& xmlfile, DATA::CItemList& il)
{
	CLoader		lder( xmlfile, il);
	return lder.DoLoad();
}

} // namespace LOADER {
