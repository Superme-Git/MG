#pragma once

namespace CODEOUT {

void OutNotifyDefineObject( const DATA::CNotify& cl, CCodeOut& co)
{
	assert( cl.GetParent().empty());

	if( !cl.GetNotes().empty())
		co.Print( L"//! %s\n", cl.GetNotes().c_str());
	co.Print( L"class %s : public CNotifyData\n{\npublic:\n", cl.GetName().c_str());

	bool	bHasDefault = false;
	const DATA::CVariantList& vl = cl.GetVariantList();
	for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	{
		const DATA::Variant& var = *it;
		OutVariantDefineObject( var, co);
		if( !var.strDefault.empty())
			bHasDefault = true;
	} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)

	co.Print( L"public:\n");
	co.Print( L"\t%s() : CNotifyData( NDID_%s)", cl.GetName().c_str(), cl.GetName().c_str());

	if( bHasDefault)
	{
		for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
		{
			const DATA::Variant& var = *it;
			if( var.strDefault.empty())
				continue;
			CVariantCode		vc( var);
			co.Print( L", %s( %s)", var.strName.c_str(), vc.GetDefaultValue().c_str());
		} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	}
	co.Print( L" {}\n");
	co.Print( L"\tvirtual ~%s() {}\n", cl.GetName().c_str());
	co.Print( L"}; // class %s\n\n", cl.GetName().c_str());
}

void OutNotifyDataIDDefine( const DATA::CItemList& il, CCodeOut& co)
{
	co.Print( L"enum // 通告数据常量定义\n{\n\tNDID_NULL = 0,\n");
	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CNotify* cl = dynamic_cast<const DATA::CNotify*>( ip.GetBase()))
			co.Print( L"\tNDID_%s,\n", cl->GetName().c_str());
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	co.Print( L"}; // enum 通告数据常量定义\n\n");
}

void OutNotifyDefineHPP( const DATA::CItemList& il, CCodeOut& co)
{
	co.Print( L"#pragma once\n\n");
	co.Print( L"namespace MODULEBASE {\n\n");

	OutNotifyDataIDDefine( il, co);

	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CNotify* cl = dynamic_cast<const DATA::CNotify*>( ip.GetBase()))
			OutNotifyDefineObject( *cl, co);
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	co.Print( L"} // namespace MODULEBASE \n\n");
}

} // namespace CODEOUT {