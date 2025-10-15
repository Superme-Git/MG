#pragma once

namespace CODEOUT {

void OutStateDefineHPP( const DATA::CItemList& il, CCodeOut& co)
{
	co.Print( L"#pragma once\n\n");
	co.Print( L"namespace MODULEBASE {\n\n");

	co.Print( L"enum // 状态常量定义\n{\n\tSTATE_NULL = 0,\n");
	for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	{
		const DATA::CItemPointer& ip = *it;
		if( const DATA::CState* s = dynamic_cast<const DATA::CState*>( ip.GetBase()))
		{
			co.Print( L"\tSTATE_%s,", s->GetName().c_str());
			if( !s->GetNotes().empty())
				co.Print( L"\t// %s", s->GetNotes().c_str());
			co.Print( L"\n");
		}
		else
			assert( false);
	} // for( DATA::CItemList::const_iterator it = il.begin(); it != il.end(); ++ it)
	co.Print( L"}; // enum 状态常量定义\n\n");
	co.Print( L"} // namespace MODULEBASE \n\n");
}

} // namespace CODEOUT {