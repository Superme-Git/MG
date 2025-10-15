#include "stdafx.h"
#include "codeout.h"
#include "component.h"
#include "generatecode.h"
#include "codecomponent.h"
#include "gxwencoder.h"
#include "util.h"

//// 该宏用于控制是否生成OnCustomButtonClick()函数
//#define  CREATE_CUSTOM_ONBUTTONCLICK
//#define  AUTOPTR_AUTOCREATE_POINTER
//#define  CREATE_AUTOPTR_GETFRAMENAME


const char* g_hfile_custombegin = "// User custom code begin";
const char* g_hfile_customend = "// User custom code end";

CGenerateCode::CGenerateCode()
{

}

CGenerateCode::~CGenerateCode()
{

}

bool CGenerateCode::GenerateCode(const std::string& cppOutDir, const std::string& inlOutDir, CComponent& RootComponent, const std::string& strFilePrefix)
{
	if ( -1  == GetFileAttributes( cppOutDir.c_str()))
		return false;

	std::string		strHFileTemp, strInlFileTemp, strInitCppFileTemp, strUserCppFileTemp, strMngUICppFileTemp, cppOutDirTemp;

	cppOutDirTemp = cppOutDir;
	const std::vector<std::string>& namespaces = m_context.GetNameSpace();

	// 如果命令行参数中使用了 -namespace 参数, 并且xml里不指定namespace信息, 则用-namespace参数来设置输出路径, 每级namespace一层目录
	if ( RootComponent.QueryPropertyValue( "outdir").empty() && !namespaces.empty() && RootComponent.QueryPropertyValue( "namespace").empty())
	{
		for ( std::vector<std::string>::const_iterator it = namespaces.begin(); it != namespaces.end(); ++ it)
		{
			cppOutDirTemp += "\\" + *it;
			CreateDirectory( cppOutDirTemp.c_str(), NULL);
		}
	}
	else
	{
		CreateDirectories( cppOutDirTemp, RootComponent.QueryPropertyValue("outdir"));
		cppOutDirTemp += "\\" + RootComponent.QueryPropertyValue("outdir");
	}
	
	// 处理名字空间, 优先处理xml里提供的namespace信息
	if ( !RootComponent.QueryPropertyValue( "namespace").empty())
	{
		m_context.SetNameSpace( RootComponent.QueryPropertyValue( "namespace"));
	}

	std::string t_szFilePrefix = strFilePrefix;
	if ( t_szFilePrefix.empty())
		t_szFilePrefix = RootComponent.GetName();

	m_context.PushVariant( "headerfile", t_szFilePrefix + ".h");
	m_context.PushVariant( "inlfile", t_szFilePrefix + ".inl");
	m_context.PushVariant( "initfile", t_szFilePrefix + "_init.inl");

	strHFileTemp = cppOutDirTemp;
	strHFileTemp += "\\";
	strHFileTemp += m_context.QueryVariant( "headerfile");

	if ( stricmp( cppOutDir.c_str(), inlOutDir.c_str()) == 0) // 一致
	{
		strInlFileTemp = cppOutDirTemp;
		strInlFileTemp += "\\";
		strInlFileTemp += m_context.QueryVariant( "inlfile");

		strInitCppFileTemp = cppOutDirTemp;
		strInitCppFileTemp += "\\";
		strInitCppFileTemp += m_context.QueryVariant( "initfile");
	}
	else
	{
		strInlFileTemp = inlOutDir;
		CreateDirectory( strInlFileTemp.c_str(), NULL);

		strInlFileTemp += "\\";

		strInitCppFileTemp = strInlFileTemp + m_context.QueryVariant( "initfile");
		strInlFileTemp += m_context.QueryVariant( "inlfile");

		//m_context.PushVariant( "inloutpath", inlOutDir);
	}

	strUserCppFileTemp = cppOutDirTemp;
	strUserCppFileTemp += "\\";
	strUserCppFileTemp += t_szFilePrefix;
	strUserCppFileTemp += ".cpp";

	m_context.PushVariant( "framename", RootComponent.QueryPropertyValue( "n"));
	if ( !RootComponent.QueryPropertyValue( "notifyclass").empty())
		m_context.PushVariant( "notifyclass", RootComponent.QueryPropertyValue( "notifyclass"));
	if ( _stricmp( "true", RootComponent.QueryPropertyValue( COMTYPE_HOOKEDIT).c_str()) == 0)
		m_context.PushVariant( COMTYPE_HOOKEDIT, "true");

	bool bSuc = true;
	bSuc = bSuc && GenerateHeaderFile( strHFileTemp, strInlFileTemp, RootComponent);
	bSuc = bSuc && GenerateInitializeCppFile( strInitCppFileTemp, RootComponent);
	bSuc = bSuc && GenerateUserCppFile( strUserCppFileTemp, RootComponent);
	if ( !bSuc )
		 return false;

	if ( !m_context.QueryVariant( "notifyclass").empty())
	{
		std::string tmp;
		if ( !RootComponent.QueryPropertyValue( "include").empty())
		{
			std::string split = RootComponent.QueryPropertyValue( "include");
			size_t pos1 = split.find_last_of( "/\\");
			size_t pos2 = split.find_last_of( ".");
			tmp = split.substr( pos1 + 1, pos2 - pos1 - 1);
		}
		if ( tmp.empty())
			tmp = m_context.GetSplitedNotifyClass()[m_context.GetSplitedNotifyClass().size() - 1];

		strMngUICppFileTemp = cppOutDirTemp;
		strMngUICppFileTemp += "\\";
		strMngUICppFileTemp += tmp + "_";
		strMngUICppFileTemp += RootComponent.GetName();
		strMngUICppFileTemp += "_ui.cpp";
		
		if ( !GenerateMngClassCppFile( strMngUICppFileTemp, RootComponent))
			return false;
	}

	m_context.SaveTextResXmlFile();

	return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 写头文件

bool CGenerateCode::GenerateHeaderFile(const std::string& strH, const std::string& strHinl, CComponent& RootComponent)
{
	CStringCodeOut hfile_variant;
	CStringCodeOut hfile_protectedfunc;

	m_context.PushVariant( "class", ( RootComponent.QueryPropertyValue( "class").empty() ? "CFrame" : RootComponent.QueryPropertyValue( "class")) + RootComponent.QueryPropertyValue( "n") + "Container");
	GenerateHFileVariantDeclaration( hfile_variant, RootComponent);

	
	CStringCodeOut autoCodeOut;

	autoCodeOut.Print( "#pragma once\n\n");
	if ( RootComponent.QueryPropertyValue( "gxinclude").empty())
	//	autoCodeOut.Print( "#include \"../../ui/gxwinclude.h\"\n");
	//else
		autoCodeOut.Print( "#include \"%s\"\n", RootComponent.QueryPropertyValue( "gxinclude").c_str());
	if ( RootComponent.FindComponent( COMTYPE_EDIT) != NULL || RootComponent.FindComponent( COMTYPE_MULTIEDIT) != NULL)
		autoCodeOut.Print( "#include <string>\n");
	autoCodeOut.Print( "\n");
	GenerateHFileNamespaceScope( autoCodeOut, RootComponent);

	// 输出名字空间
	//if ( !RootComponent.QueryPropertyValue( "namespace").empty())
	//	autoCodeOut.Print( "namespace %s\n{\n", RootComponent.QueryPropertyValue( "namespace").c_str());
	GenerateSelfNamespacesBegin( autoCodeOut);

	autoCodeOut.Print( "class %s \n\t: virtual public GXWINDOW::CFrameClosedNotify\n", m_context.QueryVariant( "class").c_str());
	hfile_protectedfunc.Print( "protected:\n\tvoid DoFrameGenerate();\n\tvoid DoFrameRelease();\n\tvirtual void OnFrameInitialize();\n\tvirtual void OnFrameClosed( GXWINDOW::CFrame& c);\n");
	GenerateHFileInheritList( autoCodeOut, hfile_protectedfunc, RootComponent);

	autoCodeOut.Print("{\npublic:\n\t%s(%s);\n\tvirtual ~%s();\n", m_context.QueryVariant( "class").c_str(), 
		RootComponent.QueryPropertyValue( "notifyclass").empty() ? "" : std::string( RootComponent.QueryPropertyValue( "notifyclass") + "* pscene").c_str(),
		m_context.QueryVariant( "class").c_str());

	const char* inlout = m_context.QueryVariant( "inloutpath").c_str();
	autoCodeOut.Print( "\t#include \"%s%s\"\n", inlout ? inlout : "", m_context.QueryVariant( "inlfile").c_str());
	autoCodeOut.Print( "private:\n\tvoid CustomFreeTextDraw()\n\t{\n\t}\n");
#ifdef CREATE_CUSTOM_ONBUTTONCLICK
	autoCodeOut.Print( "\n\tvoid OnCustomButtonClick( GXWINDOW::CAbstractButton& c)\n\t{\n\t}\n");
#endif
	autoCodeOut.Print( "};\n");

	// 生成指针封装类
	if ( _stricmp( "true", RootComponent.QueryPropertyValue( "gxautoptr").c_str()) == 0)
	{
		autoCodeOut.Print( "\n");
		autoCodeOut.Print( "class %sPtr\n{\n",  m_context.QueryVariant( "class").c_str());
		autoCodeOut.Print( "\t%s* m_ptr;\n",  m_context.QueryVariant( "class").c_str());
		autoCodeOut.Print( "public:\n\t%sPtr() : m_ptr( NULL) {}\n",  m_context.QueryVariant( "class").c_str());
		autoCodeOut.Print( "\tvirtual ~%sPtr() { Delete(); }\n\n",  m_context.QueryVariant( "class").c_str());
		autoCodeOut.Print( "\tvoid Create(%s)\n\t{\n\t\tif ( !m_ptr)\n\t\t\tm_ptr = new %s(%s);\n\t}\n\n", RootComponent.QueryPropertyValue( "notifyclass").empty() ? "" : std::string( RootComponent.QueryPropertyValue( "notifyclass") + "* pscene").c_str(),
							m_context.QueryVariant( "class").c_str(), RootComponent.QueryPropertyValue( "notifyclass").empty() ? "" : " pscene");
#ifdef CREATE_AUTOPTR_GETFRAMENAME
		autoCodeOut.Print( "\tconst wchar_t* GetFrameName() const\n\t{\n\t\treturn L\"%s\";\n\t}\n\n", RootComponent.GetName());
#endif

#ifdef AUTOPTR_AUTOCREATE_POINTER // 临时使用
		autoCodeOut.Print( "\t%s* operator->()\n\t{\n\t\tif ( !m_ptr)\n\t\t\tm_ptr = new %s(%s);\n\t\treturn m_ptr;\n\t}\n\n", m_context.QueryVariant( "class").c_str(), RootComponent.QueryPropertyValue( "notifyclass").empty() ? "" : std::string( RootComponent.QueryPropertyValue( "notifyclass") + "* pscene").c_str(),
			m_context.QueryVariant( "class").c_str(), RootComponent.QueryPropertyValue( "notifyclass").empty() ? "" : " pscene");
		autoCodeOut.Print( "\tbool IsCreated() const\n\t{\n\t\treturn NULL != m_ptr;\n\t}\n\n");
#else
		autoCodeOut.Print( "\t%s* operator->()\n\t{\n\t\tassert( m_ptr);\n\t\treturn m_ptr;\n\t}\n\n", m_context.QueryVariant( "class").c_str());
#endif
		autoCodeOut.Print( "\tvoid Delete()\n\t{\n\t\tif ( m_ptr)\n\t\t{\n\t\t\tm_ptr->HideFrame();\n\t\t\tm_ptr->FreeTextDraw();\n\t\t\tdelete m_ptr;\n\t\t\tm_ptr = NULL;\n\t\t}\n\t}\n\n};\n");
	}

	//if ( !RootComponent.QueryPropertyValue( "namespace").empty())
	//	autoCodeOut.Print( "};\n");
	GenerateSelfNamespacesEnd( autoCodeOut);

	if ( -1  == GetFileAttributes( strH.c_str()))
	{
		CFileCodeOut hfileCodeOut( strH.c_str());
		hfileCodeOut.Print( autoCodeOut.GetString().c_str());
	}

	CFileCodeOut inlCodeOut( strHinl.c_str());

	// xxx.inl
	GenerateHFilePrivatePart( inlCodeOut, RootComponent);
	inlCodeOut.Print( hfile_variant.GetString().c_str());	// 对象声明
	//if ( m_context.GetGenMode() != CGxWEncoder::eGenMode_Code)
	//{
	//	std::string strCGen = "\tGXWINDOW::CComponentGenerator m_cgen;\n";
	//	if ( m_context.GetGenMode() == CGxWEncoder::eGenMode_Both)
	//		inlCodeOut.Print( "#ifdef GXWE_GENERATEMODE_FRAME\n%s\n#endif\n", strCGen.c_str());
	//	else
	//		inlCodeOut.Print( strCGen.c_str());
	//}	
	GenerateHFileInlineFunc( inlCodeOut, RootComponent);
	inlCodeOut.Print( hfile_protectedfunc.GetString().c_str());
	GenerateHRespondDeclaration( inlCodeOut, CStringCodeOut(), RootComponent);
	inlCodeOut.Print( "public:\n");
	SetAndGetProperty( inlCodeOut, RootComponent);

	return true;
}

void CGenerateCode::LoadHeadFileSuffix(CCodeOut& suffix, const std::string& strH)
{
	FILE* pFile = fopen( strH.c_str(), "r");
	if( NULL == pFile)
		return;

	const int nBufferSize = 2048;
	char		szBuffer[nBufferSize];

	const int nGenerateLineStartCount = (int)strlen( g_hfile_custombegin);
	const int nGenerateLineEndCount = (int)strlen( g_hfile_customend);
		
	while( fgets( szBuffer, nBufferSize, pFile))
	{
		if( 0 == strncmp( szBuffer, g_hfile_custombegin, nGenerateLineStartCount))
			break;
	}

	{
		while( fgets( szBuffer, nBufferSize, pFile))
		{
			if( 0 == strncmp( szBuffer, g_hfile_customend, nGenerateLineEndCount))
				break;
			suffix.Print( szBuffer);
		}
	}

	fclose( pFile);
}

void CGenerateCode::GenerateHFileNamespaceScope( CCodeOut& CodeOut, CComponent& RootComponent)
{
	const std::vector<std::string>& splitedset = m_context.SplitNotifyClass( RootComponent.QueryPropertyValue( "notifyclass"));
	if ( splitedset.empty())
		return;

	std::string tab_tmp;
	for ( int i = 0; i < (int)splitedset.size(); i ++)
		tab_tmp += "\t";

	int i = 0;
	for ( ; i < (int)splitedset.size() - 1; i ++)
	{
		CodeOut.Print( tab_tmp.substr( 0, i).c_str());
		CodeOut.Print( "namespace %s \n%s{\n", splitedset[i].c_str(), tab_tmp.substr( 0, i).c_str());
	}

	CodeOut.Print( tab_tmp.substr( 0, i).c_str());
	CodeOut.Print( "class %s;\n", splitedset[i].c_str());
	for ( ; i > 0; i --)
	{
		CodeOut.Print( tab_tmp.substr( 0, i - 1).c_str());
		CodeOut.Print( "}\n");
	}
	CodeOut.Print( "\n");
}

void CGenerateCode::GenerateHFileInheritList( CCodeOut& InheritList, CCodeOut& OverrideFunc, CComponent& RootComponent)
{
	const ComponentPointerList* notifycomponents = &m_context.GetNotifyVariants( COMTYPE_BUTTON);
	if ( !notifycomponents->empty())
	{
		InheritList.Print( "\t, virtual public GXWINDOW::CButtonClickNotify\n");
		OverrideFunc.Print( "\tvirtual void OnButtonClick( GXWINDOW::CAbstractButton& c);\n");
	}

	notifycomponents = &m_context.GetNotifyVariants( COMTYPE_LISTBOX);
	if ( !notifycomponents->empty())
	{
		InheritList.Print( "\t, virtual public GXWINDOW::CListBoxSelectChangeNotify\n");
		OverrideFunc.Print( "\tvirtual void OnListBoxSelectChange( GXWINDOW::CListBox& lb, int n);\n");
	}

	notifycomponents = &m_context.GetNotifyVariants( COMTYPE_COMBOBOX);
	if ( !notifycomponents->empty())
	{
		InheritList.Print( "\t, virtual public GXWINDOW::CComboBoxSelectChangeNotify\n");
		OverrideFunc.Print( "\tvirtual void OnComboBoxSelectChange( GXWINDOW::CComboBox& lb, int n);\n");
	}

	notifycomponents = &m_context.GetNotifyVariants( COMTYPE_TABLE);
	if ( !notifycomponents->empty())
	{
		InheritList.Print( "\t, virtual public GXWINDOW::CTableSelectChangeNotify\n");
		OverrideFunc.Print( "\tvirtual void OnTableSelectChangeNotify( GXWINDOW::CTable& t, int n, int o);\n");
	}

	notifycomponents = &m_context.GetNotifyVariants( COMTYPE_TABLEHEADER);
	if ( !notifycomponents->empty())
	{
		InheritList.Print( "\t, virtual public GXWINDOW::CComponentMouseEventNotify\n");
		OverrideFunc.Print( "\tvirtual void OnLeftButtonUp( GXWINDOW::CComponent& c, int x, int y, int key);\n");
	}

	if ( _stricmp( m_context.QueryVariant( COMTYPE_HOOKEDIT).c_str(), "true") == 0 )
	{
		InheritList.Print( "\t, public GXWADDIN::CHookEditKeyFrame\n");
		OverrideFunc.Print( "\tvirtual bool OnEditKeyPress( GXWINDOW::CEditArea& ea, int key);\n");
		OverrideFunc.Print( "\tvirtual bool OnKeyDown( GXWINDOW::CEditArea& ea, WPARAM wParam, LPARAM lParam);\n");
	}
}

void CGenerateCode::GenerateHFilePrivatePart(CCodeOut& CodeOut, CComponent& RootComponent)
{
	CStringCodeOut tmpCodeOut;
	if ( !RootComponent.QueryPropertyValue( "notifyclass").empty())
		tmpCodeOut.Print( "\t%s* m_scene;\n", RootComponent.QueryPropertyValue( "notifyclass").c_str());

	if ( _stricmp( m_context.QueryVariant( COMTYPE_HOOKEDIT).c_str(), "true") == 0 )
		tmpCodeOut.Print( "\tGXWADDIN::CHookEditKeyFrame::CHEKSEditAllocator m_editalloc;\n");

	if ( !tmpCodeOut.GetString().empty())
		CodeOut.Print( "private:\n%s", tmpCodeOut.GetString().c_str());
}

void CGenerateCode::GenerateHFileVariantDeclaration(CCodeOut& CodeOut, CComponent& RootComponent)
{
	CStringCodeOut tmpCodeOut;
	VariantDeclaration( tmpCodeOut, RootComponent);

	if ( !tmpCodeOut.GetString().empty())
		CodeOut.Print( "public:\n%s", tmpCodeOut.GetString().c_str());
}

void CGenerateCode::VariantDeclaration(CCodeOut& CodeOut, CComponent& component)
{
	if ( _stricmp( "Content", component.GetParentType()) == 0)
	{
		if ( CCodeComponent* cc = g_CodeGeneratePropertyMap.Query( component.GetType()))
		{
			if ( _stricmp( COMTYPE_APPLEMENU, component.GetType()) == 0)
			{
				CCodeAppleMenu* pam = dynamic_cast<CCodeAppleMenu*>( cc);
				if ( pam)
					pam->AnalyseAppleMenu( component);
			}
			cc->CodeOutDeclaration( component, CodeOut, m_context);
		}
	}


	for ( ComponentList::iterator it = component.GetChilderList().begin(); it != component.GetChilderList().end(); ++ it)
	{
		VariantDeclaration( CodeOut, *it);
	}
}

void CGenerateCode::GenerateHFileInlineFunc(CCodeOut& CodeOut, CComponent& RootComponent)
{
	if ( !RootComponent.QueryPropertyValue( "defaultfocus").empty())
		CodeOut.Print( "public:\n\tvirtual void ShowFrame()\n\t{\n\t\tGXWINDOW::CKeyInput::GetInstance().SetFocus( %s.GetEditArea());\n\t\t%s.Show();\n\t}\n", m_context.QueryVariant( RootComponent.QueryPropertyValue( "defaultfocus")).c_str(), m_context.QueryVariant( RootComponent.GetName()).c_str());
	else
		CodeOut.Print( "public:\n\tvirtual void ShowFrame()\n\t{\n\t\t%s.Show();\n\t}\n", m_context.QueryVariant( RootComponent.GetName()).c_str());
	if ( !RootComponent.QueryPropertyValue( "defaultfocus").empty())
		CodeOut.Print( "\tvirtual void HideFrame()\n\t{\n\t\tGXWINDOW::CKeyInput::GetInstance().ReleaseFocus( %s.GetEditArea());\n\t\t%s.Hide();\n\t}\n", m_context.QueryVariant( RootComponent.QueryPropertyValue( "defaultfocus")).c_str(), m_context.QueryVariant( RootComponent.GetName()).c_str());
	else
		CodeOut.Print( "\tvirtual void HideFrame()\n\t{\n\t\t%s.Hide();\n\t}\n", m_context.QueryVariant( RootComponent.GetName()).c_str());
	CodeOut.Print( "\tvirtual GXWINDOW::CFrame& GetFrame()\n\t{\n\t\treturn %s;\n\t}\n", m_context.QueryVariant( RootComponent.GetName()).c_str());
	//CodeOut.Print( "\tvirtual bool IsVisible() const\n\t{\n\t\treturn %s.IsVisible();\n\t}\n", m_context.QueryVariant( RootComponent.GetName()).c_str());
	CodeOut.Print( "\tvirtual void FreeTextDraw()\n\t{\n");
	FreeTextDraw( CodeOut, RootComponent);
	CodeOut.Print( "\t\tCustomFreeTextDraw();\n");
	CodeOut.Print( "\t}\n");
}

void CGenerateCode::FreeTextDraw(CCodeOut& CodeOut, CComponent& component)
{
	if ( CCodeComponent* cc = g_CodeGeneratePropertyMap.Query( component.GetType()))
		cc->CodeOutFreeTextDraw( component, CodeOut, m_context);

	for ( ComponentList::iterator it = component.GetChilderList().begin(); it != component.GetChilderList().end(); ++ it)
	{
		FreeTextDraw( CodeOut, *it);
	}
}

void CGenerateCode::SetAndGetProperty( CCodeOut& CodeOut, CComponent& component)
{
	if ( CCodeComponent* cc = g_CodeGeneratePropertyMap.Query( component.GetType()))
		cc->SetAndGetProperty( component, CodeOut, m_context);

	for ( ComponentList::iterator it = component.GetChilderList().begin(); it != component.GetChilderList().end(); ++ it)
	{
		SetAndGetProperty( CodeOut, *it);
	}
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 生成inline文件, 存在不可覆盖

bool CGenerateCode::GenerateInlineFile( const std::string& strInl, CComponent& RootCoomponent)
{
	if ( -1  != GetFileAttributes( strInl.c_str())) // 已存在，不能覆盖
		return true;

	CFileCodeOut inl( strInl.c_str());
	inl.Print( "\t// Add custom code here\n");

	return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 写初始化界面的inl文件, 所有内容自动生成, 可以覆盖

bool CGenerateCode::GenerateInitializeCppFile(const std::string& strICpp, CComponent& RootComponent)
{
	CFileCodeOut icpp( strICpp.c_str());

	//icpp.Print( "#include <windows.h>\n#include \"%s.h\"\n", RootComponent.GetName());
	//icpp.Print( "#include \"../../txcommon.h\"\n#include \"%s.h\"\n", RootComponent.GetName());
	//if ( !RootComponent.QueryPropertyValue( "gxacpp").empty())
	//	icpp.Print( "%s\n", RootComponent.QueryPropertyValue( "gxacpp").c_str());
	//icpp.Print( "#include \"%s\"\n\n", m_context.QueryVariant( "headerfile").c_str());
	//if ( !RootComponent.QueryPropertyValue( "include").empty())
	//	icpp.Print( "#include \"%s\"\n", RootComponent.QueryPropertyValue( "include").c_str());
	//icpp.Print( "\n");

	GenerateICppDoFrameGenerateAndRelease( icpp, RootComponent);
	GenerateICppOverrideFunc( icpp, RootComponent);

	return true;
}

void CGenerateCode::GenerateICppDoFrameGenerateAndRelease( CCodeOut& CodeOut, CComponent& RootComponent)
{
	CStringCodeOut generate, notifyadd, release, generateframemode;

	notifyadd.Print( "\t%s.FrameClosedNotifyAdd( *this);\n", m_context.QueryVariant( RootComponent.GetName()).c_str());

	if ( m_context.GetGenMode() != CGxWEncoder::eGenMode_Code)
	{
		// 暂时使用GXWADDIN::CGuiGenerator, 以后有需要再加命令行参数进行开关控制
		std::string xmlfile = m_context.GetUIName();
		size_t pos = xmlfile.find_last_of( "\\/");
		xmlfile = xmlfile.substr( pos + 1, xmlfile.length());
		//generateframemode.Print( "\tm_cgen.SetGeneratorImplement( GXWADDIN::CGuiGenerator::GetGenerator( L\"%s\"));\n\n", xmlfile.c_str());
		generateframemode.Print( "\tGXWADDIN::CGuiGenerator& cgen = GXWADDIN::CGuiGenerator::GetGenerator( L\"%s\");\n\n", xmlfile.c_str());

		DoFrameModeGenerate( generateframemode, RootComponent);
		generateframemode.Print( "\n");
	}

	DoFrameGenerateAndRelease( generate, generateframemode, notifyadd, release, RootComponent);
	if ( m_context.GetGenMode() != CGxWEncoder::eGenMode_Code)
	{
		if ( m_context.GetGenMode() == CGxWEncoder::eGenMode_Both)
		{
			CodeOut.Print( "#ifdef GXWE_GENERATEMODE_FRAME\n");
			CodeOut.Print( "void %s::DoFrameGenerate()\n{\n%s\n%s}\n\n", m_context.QueryVariant( "class").c_str(), generateframemode.GetString().c_str(), notifyadd.GetString().c_str());
			CodeOut.Print( "#else\n");
			CodeOut.Print( "void %s::DoFrameGenerate()\n{\n%s\n%s\n%s}\n\n", m_context.QueryVariant( "class").c_str(), m_context.GetImageReg().c_str(), generate.GetString().c_str(), notifyadd.GetString().c_str());
			CodeOut.Print( "#endif\n\n");
		}
		else
			CodeOut.Print( "void %s::DoFrameGenerate()\n{\n%s\n%s}\n\n", m_context.QueryVariant( "class").c_str(), generateframemode.GetString().c_str(), notifyadd.GetString().c_str());
	}
	else
		CodeOut.Print( "void %s::DoFrameGenerate()\n{\n%s\n%s\n%s}\n\n", m_context.QueryVariant( "class").c_str(), m_context.GetImageReg().c_str(), generate.GetString().c_str(), notifyadd.GetString().c_str());
	CodeOut.Print( "void %s::DoFrameRelease()\n{\n%s\t%s.Hide();\n\t%s.FrameClosedNotifyRemove( *this);\n}\n\n", m_context.QueryVariant( "class").c_str(), release.GetString().c_str(), m_context.QueryVariant( RootComponent.GetName()).c_str(), m_context.QueryVariant( RootComponent.GetName()).c_str());
}

void CGenerateCode::DoFrameGenerateAndRelease( CCodeOut& generate, CCodeOut& generateframemode, CCodeOut& notifyadd, CCodeOut& release, CComponent& component, CComponent* parent)
{
	if ( _stricmp( COMTYPE_APPLEMENU, component.GetType()) == 0)
	{
		for ( ComponentList::iterator it = component.GetChilderList().begin(); it != component.GetChilderList().end(); ++ it)
		{
			DoFrameGenerateAndRelease( generate, generateframemode, notifyadd, release, *it, _stricmp( COMTYPE_CONTENT, component.GetType()) == 0 ? parent : &component);
		}
	}

	if ( CCodeComponent* cc = g_CodeGeneratePropertyMap.Query( component.GetType()))
	{
		if ( cc->CodeOutInitializeAndRelease( component, generate, notifyadd, release, m_context) )
		{
			if ( NULL != parent && _stricmp( "Content", component.GetParentType()) == 0)
			{
				if ( _stricmp( COMTYPE_FRAME, parent->GetType()) == 0 || _stricmp( COMTYPE_PANEL, parent->GetType()) == 0 || _stricmp( COMTYPE_BKPANEL, parent->GetType()) == 0 )					
				{
					generate.Print( "\t%s.InsertComponent( &%s);\n", m_context.QueryVariant( parent->GetName()).c_str(), m_context.QueryVariant( component.GetName()).c_str());
					if ( m_context.GetGenMode() != CGxWEncoder::eGenMode_Code)
					{
						if ( 0 == _stricmp( component.GetType(), COMTYPE_TAB)) // tab的结构比较特殊, 只能先在这里处理一下, 否则动态配置时会出问题
						{
							ComponentPointerList tabpanels;
							if ( const CComponent* pc = component.FindChild( COMTYPE_CONTENT) )
								pc->FindChildren( "", tabpanels);

							if ( !tabpanels.empty())
							{
								for ( ComponentPointerList::iterator it = tabpanels.begin(), ite = tabpanels.end(); it != ite; ++ it)
								{
									DoFrameModeGenerate( generateframemode, *(*it));
									generateframemode.Print( "\t%s.InsetComponent( %s);\n", m_context.QueryVariant( component.GetName()).c_str(), m_context.QueryVariant( (*it)->GetName()).c_str());
								}
							}
						}
						DoFrameModeGenerate( generateframemode, component);
						generateframemode.Print( "\t%s.InsertComponent( &%s);\n\n", m_context.QueryVariant( parent->GetName()).c_str(), m_context.QueryVariant( component.GetName()).c_str());
					}
				}
				else if ( _stricmp( COMTYPE_SCROLLPANEL, parent->GetType()) == 0)
				{
					generate.Print( "\t%s.SetComponent( %s);\n", m_context.QueryVariant( parent->GetName()).c_str(), m_context.QueryVariant( component.GetName()).c_str());
					if ( m_context.GetGenMode() != CGxWEncoder::eGenMode_Code)
					{
						DoFrameModeGenerate( generateframemode, component);
						generateframemode.Print( "\t%s.SetComponent( %s);\n\n", m_context.QueryVariant( parent->GetName()).c_str(), m_context.QueryVariant( component.GetName()).c_str());
					}
				}
				else if ( _stricmp( COMTYPE_APPLEMENU, parent->GetType()) == 0)
				{
					if ( component.QueryPropertyValue( "ParentButton").empty())
					{
						generate.Print( "\t%s.InsertAppleButton( %s);\n", m_context.QueryVariant( parent->GetName()).c_str(), m_context.QueryVariant( component.GetName()).c_str());
						if ( m_context.GetGenMode() != CGxWEncoder::eGenMode_Code)
						{
							DoFrameModeGenerate( generateframemode, component);
							generateframemode.Print( "\t%s.InsertAppleButton( %s);\n\n", m_context.QueryVariant( parent->GetName()).c_str(), m_context.QueryVariant( component.GetName()).c_str());
						}

					}
					else
					{
						generate.Print( "\t%s.InsertAppleButton( %s, %s);\n", m_context.QueryVariant( parent->GetName()).c_str(), m_context.QueryVariant( component.QueryPropertyValue( "ParentButton")).c_str(), m_context.QueryVariant( component.GetName()).c_str());
						if ( m_context.GetGenMode() != CGxWEncoder::eGenMode_Code)
						{
							DoFrameModeGenerate( generateframemode, component);
							generateframemode.Print( "\t%s.InsertAppleButton( %s, %s);\n\n", m_context.QueryVariant( parent->GetName()).c_str(), m_context.QueryVariant( component.QueryPropertyValue( "ParentButton")).c_str(), m_context.QueryVariant( component.GetName()).c_str());
						}
					}
				}
			}
			
			generate.Print( "\n");
		}
	}

	if ( _stricmp( COMTYPE_APPLEMENU, component.GetType()) != 0)
	{
		for ( ComponentList::iterator it = component.GetChilderList().begin(); it != component.GetChilderList().end(); ++ it)
		{
			DoFrameGenerateAndRelease( generate, generateframemode, notifyadd, release, *it, _stricmp( COMTYPE_CONTENT, component.GetType()) == 0 ? parent : &component);
		}
	}
}

void CGenerateCode::DoFrameModeGenerate( CCodeOut& generatefamemode, const CComponent& component)
{
	const char* genfunc = g_CodeGeneratePropertyMap.GetGenFuncName( component.GetType());	

	if ( 0 == _stricmp( component.GetType(), COMTYPE_LISTBOX))
		generatefamemode.Print( "\tcgen.%s( %s, %s, L\"%s\");\n", genfunc, m_context.QueryVariant( component.GetName()).c_str(), m_context.QueryVariant( std::string( component.GetName()) + "_ldm").c_str(), component.GetName());
	else if ( 0 == _stricmp( component.GetType(), COMTYPE_TABLE))
		generatefamemode.Print( "\tcgen.%s( %s, %s, L\"%s\");\n", genfunc, m_context.QueryVariant( component.GetName()).c_str(), m_context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str(), component.GetName());
	else if ( 0 == _stricmp( component.GetType(), COMTYPE_COMBOBOX))
		generatefamemode.Print( "\tcgen.%s( %s, %s, L\"%s\");\n", genfunc, m_context.QueryVariant( component.GetName()).c_str(), m_context.QueryVariant( std::string( component.GetName()) + "_ldm").c_str(), component.GetName());
	else
		generatefamemode.Print( "\tcgen.%s( %s, L\"%s\");\n", genfunc, m_context.QueryVariant( component.GetName()).c_str(), component.GetName());
}

void CGenerateCode::GenerateICppOverrideFunc( CCodeOut& CodeOut, CComponent& RootComponent)
{
	// 生成OnButtonClick函数
	const ComponentPointerList* notifycomponents = &m_context.GetNotifyVariants( COMTYPE_BUTTON);
	if ( !notifycomponents->empty())
	{
		CodeOut.Print( "void %s::OnButtonClick( GXWINDOW::CAbstractButton& c)\n{\n", m_context.QueryVariant( "class").c_str());
		ICppOverrideFuncContent( CodeOut, *notifycomponents);
		CodeOut.Print( "}\n\n");
	}

	// 生成OnListBoxSelectChange函数
	notifycomponents = &m_context.GetNotifyVariants( COMTYPE_LISTBOX);
	if ( !notifycomponents->empty())
	{
		CodeOut.Print( "void %s::OnListBoxSelectChange( GXWINDOW::CListBox& lb, int n)\n{\n", m_context.QueryVariant( "class").c_str());
		ICppOverrideFuncContent( CodeOut, *notifycomponents, "onselchange");
		CodeOut.Print( "}\n\n");
	}

	// 生成OnComboBoxSelectChange函数
	notifycomponents = &m_context.GetNotifyVariants( COMTYPE_COMBOBOX);
	if ( !notifycomponents->empty())
	{
		CodeOut.Print( "void %s::OnComboBoxSelectChange( GXWINDOW::CComboBox& lb, int n)\n{\n", m_context.QueryVariant( "class").c_str());
		ICppOverrideFuncContent( CodeOut, *notifycomponents, "onselchange");
		CodeOut.Print( "}\n\n");
	}

	// 生成OnTableSelectChangeNotify函数
	notifycomponents = &m_context.GetNotifyVariants( COMTYPE_TABLE);
	if ( !notifycomponents->empty())
	{
		CodeOut.Print( "void %s::OnTableSelectChangeNotify( GXWINDOW::CTable& t, int n, int o)\n{\n", m_context.QueryVariant( "class").c_str());
		ICppOverrideFuncContent( CodeOut, *notifycomponents, "onselchange");
		CodeOut.Print( "}\n\n");
	}

	// 生成OnLeftButtonUp函数 for 响应表头点击
	notifycomponents = &m_context.GetNotifyVariants( COMTYPE_TABLEHEADER);
	if ( !notifycomponents->empty())
	{
		CodeOut.Print( "void %s::OnLeftButtonUp( GXWINDOW::CComponent& c, int x, int y, int key)\n{\n", m_context.QueryVariant( "class").c_str());
		bool bGenElse = false;
		for ( ComponentPointerList::const_iterator it = notifycomponents->begin(); it != notifycomponents->end(); ++ it)
		{
			if ( CCodeTable *ct = dynamic_cast<CCodeTable *>(g_CodeGeneratePropertyMap.Query( (*it)->GetType())))
				ct->CodeOutCallOnHeaderClickFunc( *(*it), CodeOut, m_context, bGenElse);
			if ( !bGenElse && _stricmp( "true", (*it)->QueryPropertyValue( "onheadclick").c_str()) == 0)
				bGenElse = true;
		}
		CodeOut.Print( "}\n\n");
	}
}

void CGenerateCode::ICppOverrideFuncContent( CCodeOut& CodeOut, const ComponentPointerList& cpl, const char* evevt)
{
//	bool bGenElse = false;
//	for ( ComponentPointerList::const_iterator it = cpl.begin(); it != cpl.end(); ++ it)
//	{
//		if ( CCodeComponent* cc = g_CodeGeneratePropertyMap.Query( (*it)->GetType()))
//			cc->CodeOutCallOnResponsedFunc( *(*it), CodeOut, m_context, bGenElse);
//		if ( !bGenElse && _stricmp( "true", (*it)->QueryPropertyValue( NULL == evevt ? "onclick" : evevt).c_str()) == 0)
//			bGenElse = true;
//	}
//#ifdef CREATE_CUSTOM_ONBUTTONCLICK
//	if ( !evevt)
//	{
//		if ( bGenElse)
//			CodeOut.Print( "\telse\n\t\tOnCustomButtonClick( c);\n");
//		else
//			CodeOut.Print( "\tOnCustomButtonClick( c);\n");
//	}
//#endif
	for ( ComponentPointerList::const_iterator it = cpl.begin(); it != cpl.end(); ++ it)
	{
		if ( CCodeComponent* cc = g_CodeGeneratePropertyMap.Query( (*it)->GetType()))
			cc->CodeOutCallOnResponsedFunc( *(*it), CodeOut, m_context, false);
	}
#ifdef CREATE_CUSTOM_ONBUTTONCLICK
	if ( !evevt)
	{
		CodeOut.Print( "\tOnCustomButtonClick( c);\n");
	}
#endif
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 写用户可修改的Cpp文件

bool CGenerateCode::GenerateUserCppFile(const std::string& strUCpp, CComponent& RootComponent)
{
	if ( -1  != GetFileAttributes( strUCpp.c_str())) // 已存在，不能覆盖
		return true;

	CFileCodeOut ucpp( strUCpp.c_str());
	CStringCodeOut tmpCodeOut;

	// ucpp.Print( "#include <windows.h>\n#include \"%s.h\"\n\n", RootComponent.GetName());
	//ucpp.Print( "#include \"../../TxCommon.h\"\n#include \"%s.h\"\n\n", RootComponent.GetName());
	if ( !RootComponent.QueryPropertyValue( "gxacpp").empty())
		ucpp.Print( "%s\n", RootComponent.QueryPropertyValue( "gxacpp").c_str());
	ucpp.Print( "#include \"%s\"\n", m_context.QueryVariant( "headerfile").c_str());

	if ( !RootComponent.QueryPropertyValue( "include").empty())
		ucpp.Print( "#include \"%s\"\n", RootComponent.QueryPropertyValue( "include").c_str());

	if ( _stricmp( m_context.QueryVariant( COMTYPE_HOOKEDIT).c_str(), "true") == 0 )
		ucpp.Print( "#pragma warning( disable: 4355)\n");

	ucpp.Print( "\n");

	//if ( !RootComponent.QueryPropertyValue( "namespace").empty())
	//	ucpp.Print( "namespace %s\n{\n", RootComponent.QueryPropertyValue( "namespace").c_str());
	GenerateSelfNamespacesBegin( ucpp);
	ucpp.Print( "#include \"%s\"\n\n", m_context.QueryVariant( "initfile").c_str());

	// 生成构造函数
	if ( !RootComponent.QueryPropertyValue( "notifyclass").empty())
		tmpCodeOut.Print( " %s* pscene", RootComponent.QueryPropertyValue( "notifyclass").c_str());

	CStringCodeOut initlistCodeOut;
	if ( !RootComponent.QueryPropertyValue( "notifyclass").empty() || _stricmp( m_context.QueryVariant( COMTYPE_HOOKEDIT).c_str(), "true") == 0)
	{
		if ( !RootComponent.QueryPropertyValue( "notifyclass").empty())
			initlistCodeOut.Print( ": m_scene( pscene)");
		if ( _stricmp( m_context.QueryVariant( COMTYPE_HOOKEDIT).c_str(), "true") == 0 )
		{
			initlistCodeOut.Print( "%s m_editalloc( *this)", initlistCodeOut.GetString().empty() ? ":" : ",");
			const ComponentPointerList* notifycomponents = &m_context.GetNotifyVariants( COMTYPE_HOOKEDIT);
			if ( !notifycomponents->empty())
			{
				for ( ComponentPointerList::const_iterator it = notifycomponents->begin(); it != notifycomponents->end(); ++ it)
				{
					initlistCodeOut.Print( "%s %s( m_editalloc)", initlistCodeOut.GetString().empty() ? ":" : ",", m_context.QueryVariant( (*it)->GetName()).c_str());
				}
			}
		}
	}
	ucpp.Print( "%s::%s(%s)%s%s\n{\n\tDoFrameGenerate();\n\tOnFrameInitialize();\n}\n\n", m_context.QueryVariant( "class").c_str(), 
		m_context.QueryVariant( "class").c_str(), tmpCodeOut.GetString().c_str(), initlistCodeOut.GetString().empty() ? "" : "\n\t", initlistCodeOut.GetString().c_str());

	// 生成析构函数
	ucpp.Print( "%s::~%s()\n{\n\tDoFrameRelease();\n}\n\n", m_context.QueryVariant( "class").c_str(), m_context.QueryVariant( "class").c_str());

	ucpp.Print( "void %s::OnFrameInitialize()\n{\n}\n\n", m_context.QueryVariant( "class").c_str());
	ucpp.Print( "void %s::OnFrameClosed( GXWINDOW::CFrame& c)\n{\n}\n\n", m_context.QueryVariant( "class").c_str());

	// 控件响应事件函数
	GenerateCppRespondDefine( ucpp, CStringCodeOut(), RootComponent);

	// 生成OnEditKeyPress函数
	if ( _stricmp( m_context.QueryVariant( COMTYPE_HOOKEDIT).c_str(), "true") == 0 )
	{
		ucpp.Print( "bool %s::OnEditKeyPress( GXWINDOW::CEditArea& ea, int key)\n{\n", m_context.QueryVariant( "class").c_str());
		ucpp.Print( "\treturn false;\n}\n\n");
		ucpp.Print( "bool %s::OnKeyDown( GXWINDOW::CEditArea& ea, WPARAM wParam, LPARAM lParam)\n{\n", m_context.QueryVariant( "class").c_str());
		ucpp.Print( "\treturn false;\n}\n\n");
	}

	//if ( !RootComponent.QueryPropertyValue( "namespace").empty())
	//	ucpp.Print( "}\n");
	GenerateSelfNamespacesEnd( ucpp);

	return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 写与管理类(notifyclass)连接部分的Cpp文件

bool CGenerateCode::GenerateMngClassCppFile(const std::string& strUiCpp, CComponent& RootComponent)
{
	if ( -1  != GetFileAttributes( strUiCpp.c_str())) // 已存在，不能覆盖
		return true;

	CStringCodeOut prefix, suffix, commentdeclaration;
	CFileCodeOut mcpp( strUiCpp.c_str());

	GenerateUCppNamespaceScope( prefix, suffix);

	if ( !RootComponent.QueryPropertyValue( "gxacpp").empty())
		mcpp.Print( "%s\n", RootComponent.QueryPropertyValue( "gxacpp").c_str());

	if ( !RootComponent.QueryPropertyValue( "include").empty())
		mcpp.Print( "#include \"%s\"\n", RootComponent.QueryPropertyValue( "include").c_str());
	mcpp.Print( "\n");
	mcpp.Print( prefix.GetString().c_str());

	mcpp.Print( "/*\n");
	GenerateHRespondDeclaration( CStringCodeOut(), mcpp, RootComponent);
	mcpp.Print( "*/\n\n");

	GenerateCppRespondDefine( CStringCodeOut(), mcpp, RootComponent);

	mcpp.Print( suffix.GetString().c_str());

	return true;
}

void CGenerateCode::GenerateUCppNamespaceScope(CCodeOut& prefix, CCodeOut& suffix)
{
	const std::vector<std::string>& splitedset = m_context.GetSplitedNotifyClass();
	if ( splitedset.empty())
		return;

	for ( int i = 0; i < ( int)splitedset.size() - 1; ++ i)
	{
		prefix.Print( "namespace %s {\n", splitedset[i].c_str());
	}
	prefix.Print( "\n");
	for ( int i = 0; i < ( int)splitedset.size() - 1; ++ i)
	{
		suffix.Print( "}\n");
	}
	suffix.Print( "\n");
}


void CGenerateCode::GenerateCppRespondDefine( CCodeOut& defineucpp, CCodeOut& definemcpp, CComponent& component)
{
	if ( CCodeComponent* cc = g_CodeGeneratePropertyMap.Query( component.GetType()))
		cc->CodeOutResponsedFuncDefine( component, defineucpp, definemcpp, m_context);

	for ( ComponentList::iterator it = component.GetChilderList().begin(); it != component.GetChilderList().end(); ++ it)
	{
		GenerateCppRespondDefine( defineucpp, definemcpp, (*it));
	}
}

void CGenerateCode::GenerateHRespondDeclaration( CCodeOut& declarationhfile, CCodeOut& declarationmngcpp, CComponent& component)
{
	if ( CCodeComponent* cc = g_CodeGeneratePropertyMap.Query( component.GetType()))
		cc->CodeOutResponsedFuncDeclaration( component, declarationhfile, declarationmngcpp, m_context);

	for ( ComponentList::iterator it = component.GetChilderList().begin(); it != component.GetChilderList().end(); ++ it)
	{
		GenerateHRespondDeclaration( declarationhfile, declarationmngcpp, (*it));
	}
}


//////////////////////////////////////////////////////////////////////////

void CGenerateCode::GenerateSelfNamespacesBegin( CCodeOut& CodeOut)
{
	const std::vector<std::string>& namespaces = m_context.GetNameSpace();
	if ( namespaces.empty())
		return;

	for ( std::vector<std::string>::const_iterator it = namespaces.begin(); it != namespaces.end(); ++ it)
	{
		CodeOut.Print( "namespace %s\n{\n", (*it).c_str());
	}

	CodeOut.Print( "\n\n");
}

void CGenerateCode::GenerateSelfNamespacesEnd( CCodeOut& CodeOut)
{
	const std::vector<std::string>& namespaces = m_context.GetNameSpace();
	if ( namespaces.empty())
		return;

	CodeOut.Print( "\n\n");

	for ( std::vector<std::string>::const_iterator it = namespaces.begin(); it != namespaces.end(); ++ it)
	{
		CodeOut.Print( "}\n");
	}
}