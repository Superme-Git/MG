#pragma once

namespace CODEOUT {

void OutEnumDefineObject( const DATA::CEnumType& et, CCodeOut& co)
{
	if( !et.GetNotes().empty())
		co.Print( L"//! %s\n", et.GetNotes().c_str());
	co.Print( L"enum %s\n{\n", et.GetName().c_str());

	const DATA::CEnumItemList& eil = et.GetItemList();
	for( DATA::CEnumItemList::const_iterator it = eil.begin(); it != eil.end(); ++ it)
	{
		const DATA::EnumItem& ei = *it;
		co.Print( L"\t%s", ei.strName.c_str());
		if( !ei.strValue.empty())
			co.Print( L" = %s,", ei.strValue.c_str());
		else
			co.Print( L",");
		if( !ei.strNotes.empty())
			co.Print( L"\t//! %s", ei.strNotes.c_str());
		co.Print( L"\n");
	} // for( DATA::CEnumItemList::const_iterator it = eil.begin(); it != eil.end(); ++ it)

	co.Print( L"}; // enum %s\n\n", et.GetName().c_str());
}

class CVariantCode
{
	const DATA::Variant& m_var;
public:
	CVariantCode( const DATA::Variant& var) : m_var( var) {}
	~CVariantCode() {}
public:
	std::wstring GetType()
	{
		if( 0 == _wcsicmp( m_var.strType.c_str(), L"string"))
			return L"std::wstring";
		else if( 0 == _wcsicmp( m_var.strType.c_str(), L"list"))
		{
			std::wstringstream		wss;
			wss << L"std::list<" << m_var.strValue << L">";
			return wss.str();
		}
		else if( 0 == _wcsicmp( m_var.strType.c_str(), L"vector"))
		{
			std::wstringstream		wss;
			wss << L"std::vector<" << m_var.strValue << L">";
			return wss.str();
		}
		else if( 0 == _wcsicmp( m_var.strType.c_str(), L"map"))
		{
			std::wstringstream		wss;
			wss << L"std::map<" << m_var.strKey << L"," << m_var.strValue << L">";
			return wss.str();
		}
		else
			return m_var.strType;
	}
	bool IsContainer() { return DATA::CBaseTypeDefine::GetInstance().IsContainerType( m_var.strType); }
	std::wstring GetConstRefType()
	{
		if( DATA::CBaseTypeDefine::GetInstance().IsRawType( m_var.strType))
			return m_var.strType;
		else if( DATA::CBaseTypeDefine::GetInstance().IsClassType( m_var.strType))
		{
			std::wstringstream		wss;
			wss << L"const " << GetType() << L"&";
			return wss.str();
		}
		else
		{
			std::wstringstream		wss;
			wss << L"const " << m_var.strType << L"&";
			return wss.str();
		}
	}
	std::wstring GetDefaultValue()
	{
		if( 0 == _wcsicmp( m_var.strType.c_str(), L"string"))
		{
			std::wstringstream		wss;
			wss << L"L\"" << m_var.strDefault << L"\"";
			return wss.str();
		}
		else if( 0 == _wcsicmp( m_var.strType.c_str(), L"list"))
		{
			assert( false);
			return L"";
		}
		else if( 0 == _wcsicmp( m_var.strType.c_str(), L"vector"))
		{
			assert( false);
			return L"";
		}
		else if( 0 == _wcsicmp( m_var.strType.c_str(), L"map"))
		{
			assert( false);
			return L"";
		}
		else
			return m_var.strDefault;
	}
};

void OutVariantDefineObject( const DATA::Variant& var, CCodeOut& co)
{
	CVariantCode		vc( var);
	std::wstring	strType = vc.GetType();
	co.Print( L"\t%s %s;", strType.c_str(), var.strName.c_str());
	if( !var.strNotes.empty())
		co.Print( L"\t//! %s", var.strNotes.c_str());
	co.Print( L"\n");
}

void OutStructDefineObject( const DATA::CClass& cl, CCodeOut& co)
{
	if( !cl.GetNotes().empty())
		co.Print( L"//! %s\n", cl.GetNotes().c_str());
	co.Print( L"struct %s", cl.GetName().c_str());

	assert( !cl.IsNotify());
	if( cl.GetParent().empty())
		co.Print( L"\n{\n");
	else
		co.Print( L" : public %s\n{\n", cl.GetParent().c_str());

	bool	bHasDefault = false;
	const DATA::CVariantList& vl = cl.GetVariantList();
	for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	{
		const DATA::Variant& var = *it;
		OutVariantDefineObject( var, co);
		if( !var.strDefault.empty())
			bHasDefault = true;
	} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)

	if( bHasDefault)
	{
		co.Print( L"\t%s()", cl.GetName().c_str());
		bool bFirst = true;
		for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
		{
			const DATA::Variant& var = *it;
			if( var.strDefault.empty())
				continue;
			CVariantCode		vc( var);

			if( bFirst)
				co.Print( L" : ");
			else
				co.Print( L", ");
			bFirst = false;
			co.Print( L"%s( %s)", var.strName.c_str(), vc.GetDefaultValue().c_str());
		} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
		co.Print( L" {}\n");
	}

	co.Print( L"}; // struct %s\n\n", cl.GetName().c_str());
}

void OutDataDefineHPP( const DATA::CItemList& il, CCodeOut& co)
{
	co.Print( L"#pragma once\n\n");
	co.Print( L"namespace DATAPOOL {\n\n");

	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CEnumType* et = dynamic_cast<const DATA::CEnumType*>( ip.GetBase()))
			OutEnumDefineObject( *et, co);
		else if( const DATA::CClass* cl = dynamic_cast<const DATA::CClass*>( ip.GetBase()))
			OutStructDefineObject( *cl, co);
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)

	co.Print( L"} // namespace DATAPOOL \n\n");
}

void OutInterfaceDefineVariantGet( const DATA::Variant& var, CCodeOut& co)
{
	CVariantCode	vc( var);
	co.Print( L"\tvirtual %s get_%s() const = 0;", vc.GetConstRefType().c_str(), var.strName.c_str());
	if( var.strNotes.empty())
		co.Print( L"\n");
	else
		co.Print( L"\t// get %s\n", var.strNotes.c_str());
}

void OutInterfaceDefineVariantSet( const DATA::Variant& var, CCodeOut& co)
{
	CVariantCode	vc( var);

	if( vc.IsContainer())
		co.Print( L"\tvirtual CNotifyContainer<%s> get_container_%s() = 0;", vc.GetType().c_str(), var.strName.c_str());
	else
		co.Print( L"\tvirtual void set_%s( %s) = 0;", var.strName.c_str(), vc.GetConstRefType().c_str());

	if( var.strNotes.empty())
		co.Print( L"\n");
	else
		co.Print( L"\t// set %s\n", var.strNotes.c_str());
}

void OutConstInterfaceDefineObject( const DATA::CClass& cl, CCodeOut& co)
{
	if( !cl.GetNotes().empty())
		co.Print( L"//! 常量 %s 接口\n", cl.GetNotes().c_str());
	co.Print( L"class const_%s : public CNotifyableData\n{\npublic:\n", cl.GetName().c_str());
	co.Print( L"\tconst_%s() { }\n", cl.GetName().c_str());
	co.Print( L"\tvirtual ~const_%s() { }\npublic:\n", cl.GetName().c_str());

	assert( cl.IsNotify());
	assert( cl.GetParent().empty());

	const DATA::CVariantList& vl = cl.GetVariantList();
	for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	{
		const DATA::Variant& var = *it;
		OutInterfaceDefineVariantGet( var, co);
	} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	co.Print( L"}; // class const_%s\n\n", cl.GetName().c_str());
}

void OutWritableInterfaceDefineObject( const DATA::CClass& cl, CCodeOut& co)
{
	if( !cl.GetNotes().empty())
		co.Print( L"//! 可写 %s 接口\n", cl.GetNotes().c_str());
	co.Print( L"class %s : public const_%s\n{\npublic:\n", cl.GetName().c_str(), cl.GetName().c_str());
	co.Print( L"\t%s() { }\n", cl.GetName().c_str());
	co.Print( L"\tvirtual ~%s() { }\npublic:\n", cl.GetName().c_str());

	assert( cl.IsNotify());
	assert( cl.GetParent().empty());

	const DATA::CVariantList& vl = cl.GetVariantList();
	for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	{
		const DATA::Variant& var = *it;
		OutInterfaceDefineVariantSet( var, co);
	} // for( DATA::CVariantList::const_iterator it = vl.begin(); it != vl.end(); ++ it)
	co.Print( L"}; // class %s\n\n", cl.GetName().c_str());
}

void OutInterfaceDefineObject( const DATA::CClass& cl, CCodeOut& co)
{
	OutConstInterfaceDefineObject( cl, co);
	OutWritableInterfaceDefineObject( cl, co);
}

void OutNotifyDataIDDefine( const DATA::CItemList& il, CCodeOut& co)
{
	co.Print( L"enum // 通告数据常量定义\n{\n\tNDID_NULL = 0,\n");
	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CClass* cl = dynamic_cast<const DATA::CClass*>( ip.GetBase()))
			co.Print( L"\tNDID_%s,\n", cl->GetName().c_str());
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	co.Print( L"}; // enum 通告数据常量定义\n\n");
}

void OutNotifyDefineHPP( const DATA::CItemList& il, CCodeOut& co)
{
	co.Print( L"#pragma once\n\n");
	co.Print( L"namespace DATAPOOL {\n\n");

	OutNotifyDataIDDefine( il, co);

	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CClass* cl = dynamic_cast<const DATA::CClass*>( ip.GetBase()))
			OutInterfaceDefineObject( *cl, co);
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	co.Print( L"} // namespace DATAPOOL \n\n");
}

} // namespace CODEOUT {