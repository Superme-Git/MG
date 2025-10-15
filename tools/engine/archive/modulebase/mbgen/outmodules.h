#pragma once

namespace CODEOUT {

std::wstring& ToUpString( std::wstring& str)
{
	std::transform( str.begin(), str.end(), str.begin(), towupper);
	return str;
}

std::wstring& ToLastName( std::wstring& str)
{
	std::wstring::size_type s = str.rfind( L"::");
	if( std::wstring::npos == s)
		return str;
	str = str.substr( s + 2);
	return str;
}

void OutOneModuleDefineCPP( const DATA::CModule& module, const std::wstring& upname, CCodeOut& co)
{
	co.Print( L"#include \"../commondef.h\"\n");
	co.Print( L"#include \"%s.h\"\n#include \"protocolprocess.inl\"\n\n", module.GetName().c_str());
	co.Print( L"namespace %s {\n\n", upname.c_str());

	co.Print( L"CModule_%s*	CModule_%s::s_instance = NULL;\n\n", module.GetName().c_str(), module.GetName().c_str());

	if( module.IsMessage())
		co.Print( L"bool CModule_%s::OnWindowsMessage( UINT uMsg, WPARAM wParam, LPARAM lParam)\n{\n\treturn false;\n}\n\n", module.GetName().c_str());
	
	if( !module.GetProtocolList().empty())
	{
		const DATA::CWStringList& ps = module.GetProtocolList();
		for( DATA::CWStringList::const_iterator it = ps.begin(); it != ps.end(); ++ it)
		{
			const std::wstring& name = *it;
			std::wstring lastname = name;
			lastname = ToLastName( lastname);
			co.Print( L"void CModule_%s::OnProtocol%s( %s* p)\n{\n}\n\n", module.GetName().c_str(), lastname.c_str(), name.c_str());
		} // for( DATA::CWStringList::iterator it = ps.begin(); it != ps.end(); ++ it)
	} // if( !module.GetProtocolList().empty())

	if( module.IsRender())
		co.Print( L"void CModule_%s::OnRender()\n{\n}\n\n", module.GetName().c_str());
	if( module.IsState())
		co.Print( L"void CModule_%s::OnScenceStateChangle( MODULEBASE::ScenceStateID n, MODULEBASE::ScenceStateID o)\n{\n}\n\n", module.GetName().c_str());
	if( module.IsIdle())
		co.Print( L"void CModule_%s::OnIdle()\n{\n}\n\n", module.GetName().c_str());
	if( module.IsReset())
		co.Print( L"void CModule_%s::Reset()\n{\n}\n\n", module.GetName().c_str());

	if( !module.GetNotifyList().empty())
		co.Print( L"void CModule_%s::OnModuleNotify( MODULEBASE::ModuleID mid, const MODULEBASE::CNotifyData& nd)\n{\n}\n\n", module.GetName().c_str());

	co.Print( L"} // namespace %s\n\n", upname.c_str());
}

void OutOneModuleDefineInl( const DATA::CModule& module, const std::wstring& upname, CCodeOut& co)
{
	if( !module.GetProtocolList().empty())
	{
		co.Print( L"public:\n");
		const DATA::CWStringList& ps = module.GetProtocolList();
		for( DATA::CWStringList::const_iterator it = ps.begin(); it != ps.end(); ++ it)
		{
			const std::wstring& name = *it;
			std::wstring lastname = name;
			lastname = ToLastName( lastname);

			co.Print( L"\tvoid OnProtocol%s( %s* p);\n", lastname.c_str(), name.c_str());
		} // for( DATA::CWStringList::iterator it = ps.begin(); it != ps.end(); ++ it)
		co.Print( L"\n");
	}
	co.Print( L"protected:\n");
	if( module.IsMessage())
		co.Print( L"\tvirtual bool OnWindowsMessage( UINT uMsg, WPARAM wParam, LPARAM lParam);\n");
	if( !module.GetDataCoreList().empty())
		co.Print( L"\tvirtual bool OnDatacore( GNET::DataCore* dc);\n");
	if( module.IsRender())
		co.Print( L"\tvirtual void OnRender();\n");
	if( module.IsState())
		co.Print( L"\tvirtual void OnScenceStateChangle( MODULEBASE::ScenceStateID n, MODULEBASE::ScenceStateID o);\n");
	if( module.IsIdle())
		co.Print( L"\tvirtual void OnIdle();\n");
	if( module.IsReset())
		co.Print( L"\tvirtual void Reset();\n");

	if( !module.GetNotifyList().empty())
		co.Print( L"\tvirtual void OnModuleNotify( MODULEBASE::ModuleID mid, const MODULEBASE::CNotifyData& nd);\n");

	co.Print( L"private:\n");

	co.Print( L"\tvoid DoModuleInitialize()\n\t{\n");

	{
		const DATA::CWStringList& ps = module.GetProtocolList();
		for( DATA::CWStringList::const_iterator it = ps.begin(); it != ps.end(); ++ it)
		{
			const std::wstring& pname = *it;
			co.Print( L"\t\tRegisterProtocol( %s::PROTOCOL_TYPE);\n", pname.c_str());
		}
		if( !ps.empty())
			co.Print( L"\n");
	}

	{
		const DATA::CIDRangerList& ps = module.GetDataCoreList();
		for( DATA::CIDRangerList::const_iterator it = ps.begin(); it != ps.end(); ++ it)
		{
			const std::pair<int,int>& rng = *it;
			const int& s = rng.first;
			const int& e = rng.second;

			co.Print( L"\t\tRegisterDataCore( %d, %d);\n", s, e);
		}
		if( !ps.empty())
			co.Print( L"\n");
	}

	{
		const DATA::CWStringList& ps = module.GetNotifyList();
		for( DATA::CWStringList::const_iterator it = ps.begin(); it != ps.end(); ++ it)
		{
			const std::wstring& pname = *it;
			co.Print( L"\t\tMODULEBASE::CModuleManage::GetInstance().RegisterNotify( MODULEBASE::NDID_%s, *this);\n", pname.c_str());
		}
		if( !ps.empty())
			co.Print( L"\n");
	}

	{
		const DATA::CWStringList& ps = module.GetStateList();
		if( !ps.empty())
			co.Print( L"\t\t{\n\t\t\tunsigned int states[] = {");
		for( DATA::CWStringList::const_iterator it = ps.begin(); it != ps.end(); ++ it)
		{
			std::wstring pname = *it;
			co.Print( L" MODULEBASE::STATE_%s,", pname.c_str());
		}
		if( !ps.empty())
			co.Print( L" };\n\t\t\tSetScenceStateIDS( states, sizeof( states) / sizeof( unsigned int));\n\t\t}\n");
	}

	co.Print( L"\t} // DoModuleInitialize()\n");
}

void OutOneModuleDefineHPP( const DATA::CModule& module, const std::wstring& upname, CCodeOut& co)
{
	co.Print( L"#pragma once\n\n");
	co.Print( L"namespace %s {\n\n", upname.c_str());

	co.Print( L"class CModule_%s : public MODULEBASE::CModuleBase", module.GetName().c_str());

	if( !module.GetNotifyList().empty())
		co.Print( L", public MODULEBASE::CModuleNotify");

	co.Print( L"\n{\n");
	co.Print( L"\tstatic CModule_%s*\ts_instance;\n\n", module.GetName().c_str());
	co.Print( L"\tCModule_%s() : CModuleBase( MODULEBASE::MODULEID_%s)\n", module.GetName().c_str(), module.GetName().c_str()); 
	co.Print( L"\t{\n\t\tMODULEBASE::CModuleManage::GetInstance().RegisterModule( *this);\n\t\tDoModuleInitialize();\n\t}\n");
	co.Print( L"\tvirtual ~CModule_%s()\n\t{\n\t\tMODULEBASE::CModuleManage::GetInstance().UnregisterModule( *this);\n\t\ts_instance = NULL;\n\t}\n", module.GetName().c_str()); 

	co.Print( L"\n#include \"%s.inl\"\n\n", module.GetName().c_str());

	co.Print( L"public:\n\tstatic CModule_%s& GetInstance()\n\t{\n\t\tif( NULL == s_instance)\n\t\t\ts_instance = new CModule_%s;\n\t\treturn *s_instance;\n\t}\n", module.GetName().c_str(), module.GetName().c_str());

	co.Print( L"\n\n");

	co.Print( L"}; // class CModule_%s\n\n", module.GetName().c_str());
	co.Print( L"} // namespace %s \n\n", upname.c_str());
}


std::wstring& OutOneProtocolNamespace( std::wstring& name, CCodeOut& co)
{
	CStringCodeOut		sco;
	while( true) 
	{
		std::wstring::size_type s = name.find( L"::");
		if( std::wstring::npos == s)
			break;
		co.Print( L"namespace %s {\n", name.substr( 0, s).c_str());
		name = name.substr( s + 2);
		sco.Print( L"}\n");
	}
	co.Print( L"\n");
	name = sco.GetString();
	return name;
}
void OutOneModuleProtocolProcess( const DATA::CModule& module, const std::wstring& upname, CCodeOut& co)
{
	const DATA::CWStringList& ps = module.GetProtocolList();
	for( DATA::CWStringList::const_iterator it = ps.begin(); it != ps.end(); ++ it)
	{
		const std::wstring& name = *it;
		std::wstring lastname = name;
		ToLastName( lastname);
		std::wstring namespaceend = name;
		OutOneProtocolNamespace( namespaceend, co);

		co.Print( L"void %s::Process( GNET::Protocol::Manager* manager, GNET::Protocol::Manager::Session::ID sid)\n{\n", lastname.c_str());
		co.Print( L"\t%s::CModule_%s::GetInstance().OnProtocol%s( this);\n", upname.c_str(), module.GetName().c_str(), lastname.c_str());
		co.Print( L"}\n\n%s\n\n", namespaceend.c_str());
	} // for( DATA::CWStringList::iterator it = ps.begin(); it != ps.end(); ++ it)
}

void OutOneModuleDefine( const DATA::CModule& module, CODEOUT::CCodeOut& coins, CODEOUT::CCodeOut& coinc)
{
	std::wstring		upname = module.GetName();
	ToUpString( upname);

	CreateDirectoryW( module.GetName().c_str(), NULL);

	{
		std::wstring filename = module.GetName() + L"\\" + module.GetName() + L".h";
		DWORD dwAttributes = GetFileAttributesW( filename.c_str());
		if( -1 == dwAttributes)
		{
			CFileCodeOut		co( filename.c_str());
			OutOneModuleDefineHPP( module, upname, co);
		}
	}
	{
		std::wstring filename = module.GetName() + L"\\" + module.GetName() + L".inl";
		CFileCodeOut		co( filename.c_str());
		OutOneModuleDefineInl( module, upname, co);
	}
	{
		std::wstring filename = module.GetName() + L"\\" + module.GetName() + L".cpp";
		DWORD dwAttributes = GetFileAttributesW( filename.c_str());
		if( -1 == dwAttributes)
		{
			CFileCodeOut		co( filename.c_str());
			OutOneModuleDefineCPP( module, upname, co);
		}
	}

	{
		std::wstring filename = module.GetName() + L"\\protocolprocess.inl";
		CFileCodeOut		co( filename.c_str());
		OutOneModuleProtocolProcess( module, upname, co);
	}

	coins.Print( L"\t%s::CModule_%s::GetInstance();\n", upname.c_str(), module.GetName().c_str());
	coinc.Print( L"#include \"%s/%s.h\"\n", module.GetName().c_str(), module.GetName().c_str());
}

void OutModuleDefines( const DATA::CItemList& il)
{
	CODEOUT::CFileCodeOut		co( L"common\\moduleids.h");
	CODEOUT::CFileCodeOut		coins( L"moduleinstances.hpp");
	CODEOUT::CStringCodeOut		cosins;
	CODEOUT::CStringCodeOut		cosinc;

	co.Print( L"#pragma once\n\n");
	co.Print( L"namespace MODULEBASE {\n\nenum // 模块ID常量定义\n{\n\tMODULEID_NULL = 0,\n");

	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CModule* module = dynamic_cast<const DATA::CModule*>( ip.GetBase()))
		{
			co.Print( L"\tMODULEID_%s,", module->GetName().c_str());
			if( !module->GetNotes().empty())
				co.Print( L"\t// %s", module->GetNotes().c_str());
			co.Print( L"\n");
			OutOneModuleDefine( *module, cosins, cosinc);
		}
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)

	co.Print( L"}; // enum 模块ID常量定义\n\n} // namespace MODULEBASE\n\n");

	coins.Print( L"#pragma once\n\n");
	coins.Print( L"%s\n", cosinc.GetString().c_str());
	coins.Print( L"namespace MODULEBASE {\n\ninline void InitializeAllModules()\n{\n");
	coins.Print( L"%s\n", cosins.GetString().c_str());
	coins.Print( L"}\n\n} // namespace MODULEBASE\n\n");
}

} // namespace CODEOUT {