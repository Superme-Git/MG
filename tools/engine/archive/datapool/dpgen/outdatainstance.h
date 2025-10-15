#pragma once

namespace CODEOUT {

void OutGetNotifyInstance( const DATA::CClass& cl, CCodeOut& co)
{
	const std::wstring& strname = cl.GetName();
	co.Print( L"\tconst const_%s& get_const_%s();\n", strname.c_str(), strname.c_str());
	co.Print( L"\t%s& get_%s();\n\n", strname.c_str(), strname.c_str());
}

void OutNotifyGetInstanceHPP( const DATA::CItemList& il, CCodeOut& co, const wchar_t* wszData, const wchar_t* wszInterface)
{
	co.Print( L"#pragma once\n\n");
	co.Print( L"#include \"%s\"\n", wszData);
	co.Print( L"#include \"%s\"\n\n", wszInterface);
	co.Print( L"namespace DATAPOOL {\n\n");

	co.Print( L"class CObjManager\n{\n\tstatic CObjManager s_instance;\n\n\tCObjManager() {}\n\t~CObjManager() {}\npublic:\n\tstatic CObjManager& GetInstance() { return s_instance; }\npublic:\n");

	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CClass* cl = dynamic_cast<const DATA::CClass*>( ip.GetBase()))
			OutGetNotifyInstance( *cl, co);
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	co.Print( L"}; // CObjManager\n\n");
	co.Print( L"} // namespace DATAPOOL \n\n");
}

void OutDataInstanceClassImpl( const DATA::CClass& cl, CCodeOut& co)
{
	if( !cl.GetNotes().empty())
		co.Print( L"//! %s\n", cl.GetNotes().c_str());
	co.Print( L"class %s_impl : public %s\n{\n", cl.GetName().c_str(), cl.GetName().c_str());
	co.Print( L"\tCNotifyNamedData m_Object_Notify_;\n\n");

	const DATA::CVariantList& vl = cl.GetVariantList();
	for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	{
		const DATA::Variant& var = *it;
		OutVariantDefineObject( var, co);
	} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)

	co.Print( L"public:\n\t%s_impl() : m_Object_Notify_( NDID_%s, *this)",cl.GetName().c_str(), cl.GetName().c_str());

	for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	{
		const DATA::Variant& var = *it;
		if( var.strDefault.empty())
			continue;
		CVariantCode		vc( var);
		co.Print( L", %s( %s)", var.strName.c_str(), vc.GetDefaultValue().c_str());
	} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	co.Print( L" { }\n");
	co.Print( L"\tvirtual ~%s_impl() { }\n", cl.GetName().c_str());
	co.Print( L"public:\n");

	for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	{
		const DATA::Variant& var = *it;
		CVariantCode		vc( var);
		co.Print( L"\tvirtual %s get_%s() const { return %s; }\n", vc.GetConstRefType().c_str(), var.strName.c_str(), var.strName.c_str());

		if( vc.IsContainer())
		{
			std::wstring type = vc.GetType();

			co.Print( L"\tvirtual CNotifyContainer<%s> get_container_%s()\n\t{\n", type.c_str(), var.strName.c_str());
			co.Print( L"\t\treturn CNotifyContainer<%s>( %s, m_Object_Notify_);\n\t}\n", type.c_str(), var.strName.c_str());
		}
		else
		{
			co.Print( L"\tvirtual void set_%s( %s _src_)\n\t{\n", var.strName.c_str(), vc.GetConstRefType().c_str());
			co.Print( L"\t\t%s = _src_;\n", var.strName.c_str());
			co.Print( L"\t\tm_Object_Notify_.FireDataChange();\n\t}\n");
		}
	} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)

	co.Print( L"public:\n\tCNotifyBase& GetNotifyDataBase() { return m_Object_Notify_; }\n");

	co.Print( L"}; // class %s_impl\n\n", cl.GetName().c_str());
}

void OutDataInstanceImpl( const DATA::CItemList& il, CCodeOut& co)
{
	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CClass* cl = dynamic_cast<const DATA::CClass*>( ip.GetBase()))
			OutDataInstanceClassImpl( *cl, co);
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
}

void OutDataGetInstanceClassImpl( const DATA::CClass& cl, CCodeOut& co)
{
	const std::wstring& strname = cl.GetName();

	co.Print( L"namespace IMPL { %s_impl g_%s_instance; }\n", strname.c_str(), strname.c_str());

	co.Print( L"const const_%s& CObjManager::get_const_%s() { return IMPL::g_%s_instance; }\n", strname.c_str(), strname.c_str(), strname.c_str());
	co.Print( L"%s& CObjManager::get_%s() { return IMPL::g_%s_instance;}\n\n", strname.c_str(), strname.c_str(), strname.c_str());
}

void OutDataGetInstanceImpl( const DATA::CItemList& il, CCodeOut& co)
{
	co.Print( L"CObjManager CObjManager::s_instance;\n\n");

	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CClass* cl = dynamic_cast<const DATA::CClass*>( ip.GetBase()))
			OutDataGetInstanceClassImpl( *cl, co);
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
}

void OutCDataModifyManagerRegisterAllDataObjectImpl( const DATA::CItemList& il, CCodeOut& co)
{
	co.Print( L"void CDataModifyManager::RegisterAllDataObject()\n{\n");

	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CClass* cl = dynamic_cast<const DATA::CClass*>( ip.GetBase()))
			co.Print( L"\tRegisterDataObject( IMPL::g_%s_instance.GetNotifyDataBase());\n", cl->GetName().c_str());
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	co.Print( L"}\n\n");
}

void OutCNotifyGroupManagerRegisterAllDefineGroupGroupSingleDef( const DATA::GroupSingleDef& gsd, CCodeOut& co)
{
	co.Print( L"\t{");
	if( gsd.notes.empty())
		co.Print( L"\n");
	else
		co.Print( L" // %s\n", gsd.notes.c_str());
	co.Print( L"\t\tconst std::wstring gname = L\"%s\";\n", gsd.name.c_str());

	if( !gsd.objects.empty())
	{
		co.Print( L"\t\tCNotifyDataIDSet	ndidset;\n");
		for( DATA::CWStringList::const_iterator it = gsd.objects.begin(); it != gsd.objects.end(); ++ it)
		{
			const std::wstring& name = *it;
			co.Print( L"\t\tndidset.Insert( NDID_%s);\n", name.c_str());
		} // for( DATA::CWStringList::const_iterator it = gsd.objects.begin(); it != gsd.objects.end(); ++ it)
		co.Print( L"\t\tRegisterIDSetToGroup( gname, ndidset);\n\n");
	}

	for( DATA::CWStringList::const_iterator it = gsd.groups.begin(); it != gsd.groups.end(); ++ it)
	{
		const std::wstring& name = *it;
		co.Print( L"\t\tRegisterGroupToGroup( gname, L\"%s\");\n", name.c_str());
	} // for( DATA::CWStringList::const_iterator it = gsd.objects.begin(); it != gsd.objects.end(); ++ it)
	co.Print( L"\t}\n\n");
}

void OutCNotifyGroupManagerRegisterAllDefineGroupImpl( const DATA::CGroupList& gl, CCodeOut& co)
{
	co.Print( L"void CNotifyGroupManager::RegisterAllDefineGroup()\n{\n");
	
	for( DATA::CGroupList::const_iterator it = gl.begin(); it != gl.end(); ++ it)
	{
		const DATA::GroupSingleDef& gsd = *it;
		OutCNotifyGroupManagerRegisterAllDefineGroupGroupSingleDef( gsd, co);
	}
	co.Print( L"}\n\n");
}

void OutDataInstanceHPP( const DATA::CItemList& il, const DATA::CGroupList& gl, CCodeOut& co)
{
	co.Print( L"#pragma once\n\n#pragma warning( push)\n#pragma warning( disable : 4355)\n\n");
	co.Print( L"namespace DATAPOOL {\n\n");

	co.Print( L"namespace IMPL {\n\n");
	OutDataInstanceImpl( il, co);
	co.Print( L"} // namespace IMPL \n\n");

	OutDataGetInstanceImpl( il, co);
	OutCDataModifyManagerRegisterAllDataObjectImpl( il, co);
	OutCNotifyGroupManagerRegisterAllDefineGroupImpl( gl, co);

	co.Print( L"} // namespace DATAPOOL \n\n#pragma warning( pop)\n\n");
}


} // namespace CODEOUT {