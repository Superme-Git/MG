#pragma once

class CPropertyMap;



class CGenerateCode
{
	CContext m_context;
public:
	CGenerateCode();
	virtual ~CGenerateCode();
protected:
	virtual bool GenerateHeaderFile( const std::string& strH, const std::string& strHinl, CComponent& RootComponent);	// 生成头文件
	virtual bool GenerateInlineFile( const std::string& strInl, CComponent& RootCoomponent);	// 生成inline文件, 存在不可覆盖
	virtual bool GenerateInitializeCppFile( const std::string& strICpp, CComponent& RootComponent);	// 生成初始化界面部分为一个cpp
	virtual bool GenerateUserCppFile( const std::string& strUCpp, CComponent& RootComponent);			// 生成其余用户可能手动修改的部分为一个cpp
	virtual bool GenerateMngClassCppFile( const std::string& strUiCpp, CComponent& RootComponent);		// 根据指定的notifyclass生成UI与管理类的连接部分
private:
	void LoadHeadFileSuffix( CCodeOut& suffix, const std::string& strH);
	void GenerateHFileNamespaceScope( CCodeOut& CodeOut, CComponent& RootComponent);
	void GenerateHFileInheritList( CCodeOut& InheritList, CCodeOut& OverrideFunc, CComponent& RootComponent);
	void GenerateHFilePrivatePart( CCodeOut& CodeOut, CComponent& RootComponent);
	void GenerateHFileVariantDeclaration( CCodeOut& CodeOut, CComponent& RootComponent);
	void VariantDeclaration( CCodeOut& CodeOut, CComponent& component);
	void GenerateHFileInlineFunc( CCodeOut& CodeOut, CComponent& RootComponent);
	void FreeTextDraw( CCodeOut& CodeOut, CComponent& component);
	void SetAndGetProperty( CCodeOut& CodeOut, CComponent& component);

	void GenerateICppDoFrameGenerateAndRelease( CCodeOut& CodeOut, CComponent& RootComponent);
	void DoFrameGenerateAndRelease( CCodeOut& generate, CCodeOut& generateframemode, CCodeOut& notifyadd, CCodeOut& release, CComponent& component, CComponent* parent = NULL);
	void DoFrameModeGenerate( CCodeOut& generatefamemode, const CComponent& component);
	void GenerateICppOverrideFunc( CCodeOut& CodeOut, CComponent& RootComponent);
	void ICppOverrideFuncContent( CCodeOut& CodeOut, const ComponentPointerList& cpl, const char* evevt = NULL);

	void GenerateUCppNamespaceScope( CCodeOut& prefix, CCodeOut& suffix);
	
	void GenerateCppRespondDefine( CCodeOut& defineucpp, CCodeOut& definemcpp, CComponent& component);
	void GenerateHRespondDeclaration( CCodeOut& declarationhfile, CCodeOut& declarationmngcpp, CComponent& component);

	void GenerateSelfNamespacesBegin( CCodeOut& CodeOut);
	void GenerateSelfNamespacesEnd( CCodeOut& CodeOut);
public:
	bool GenerateCode( const std::string& cppOutDir, const std::string& inlOutDir, CComponent& RootComponent, const std::string& strFilePrefix);
	void SetPakMode( bool bPak = true)
	{
		m_context.SetPakMode( bPak);
	}

	void LoadTextFormat( const char* szTextFormat)
	{
		if ( szTextFormat)
			m_context.LoadTextFormat( szTextFormat);
	}

	void SetNamespace( const char* szNamespace)
	{
		if ( szNamespace)
			m_context.SetNameSpace( szNamespace);
	}

	void LoadTextResource( const char* szTextResXmlFile)
	{
		if ( szTextResXmlFile)
			m_context.LoadTextResXmlFile( szTextResXmlFile);
	}

	void SetUIName( const char* szName)
	{
		if ( szName)
			m_context.SetUIName( szName);
	}

	void SetGenMode( int mode)
	{
		m_context.SetGenMode( mode);
	}
};