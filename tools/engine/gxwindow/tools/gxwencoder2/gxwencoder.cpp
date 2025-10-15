// gxwencoder2.cpp : 定义 DLL 应用程序的入口点。
//

#include "stdafx.h"
#include "codeout.h"
#include "component.h"
#include "generatecode.h"
#include "codecomponent.h"
#include "loadfromxml.h"
#include "gxwencoder.h"
#include "util.h"

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
	case DLL_THREAD_ATTACH:
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
		break;
	}
    return TRUE;
}

CGxWEncoder::CGxWEncoder()
{

}

CGxWEncoder::~CGxWEncoder()
{

}

void CGxWEncoder::SetXmlFileName( const std::string& xmlfilename)
{
	m_param.m_xmlfilename = xmlfilename;
}

void CGxWEncoder::SetOutputPath( const std::string& outputpath)
{
	m_param.m_outputpath = outputpath;
}

void CGxWEncoder::SetIncludeFiles( const std::string& includefiles)
{
	m_param.m_includefile = includefiles;
}

void CGxWEncoder::SetCppAppandCode( const std::string& cppappand)
{
	m_param.m_cppappand = cppappand;
}

void CGxWEncoder::SetFileNamePrefix( const std::string& filenameprefix)
{
	m_param.m_filenameprefix = filenameprefix;
}

void CGxWEncoder::SetTextFormatFile( const std::string& textformatfile)
{
	m_param.m_textformatfile = textformatfile;
}

void CGxWEncoder::SetNamespaces( const std::string& namespaces)
{
	m_param.m_namespaces = namespaces;
}

void CGxWEncoder::SetTextResXmlFile( const std::string& textresxmlfile)
{
	m_param.m_textresxmlfile = textresxmlfile;
}

void CGxWEncoder::SetInlFilePath( const std::string& inlfilepath)
{
	m_param.m_inlfilepath = inlfilepath;
}

void CGxWEncoder::SetCreateAutoPtr( bool bCreate)
{
	m_param.m_bCrateAutoptr = bCreate;
}

void CGxWEncoder::SetPackMode( bool bPackMode)
{
	m_param.m_bPakMode = bPackMode;
}

void CGxWEncoder::SetGenMode( int mode)
{
	m_param.m_nGenMode = mode;
}

std::string CGxWEncoder::GetXmlFileName() const
{
	return m_param.m_xmlfilename;
}

std::string CGxWEncoder::GetOutputPath() const
{
	return m_param.m_outputpath;
}

std::string CGxWEncoder::GetIncludeFiles() const
{
	return m_param.m_includefile;
}

std::string CGxWEncoder::GetCppAppandCode() const
{
	return m_param.m_cppappand;
}

std::string CGxWEncoder::GetFileNamePrefix() const
{
	return m_param.m_filenameprefix;
}

std::string CGxWEncoder::GetTextFormatFile() const
{
	return m_param.m_textformatfile;
}

std::string CGxWEncoder::GetNamespaces() const
{
	return m_param.m_namespaces;
}

std::string CGxWEncoder::GetTextResXmlFile() const
{
	return m_param.m_textresxmlfile;
}

std::string CGxWEncoder::GetInlFilePath() const
{
	return m_param.m_inlfilepath;
}

bool CGxWEncoder::GetCreateAutoPtr() const
{
	return m_param.m_bCrateAutoptr;
}

bool CGxWEncoder::GetPackMode() const
{
	return m_param.m_bPakMode;
}

int CGxWEncoder::GetGenMode() const
{
	return m_param.m_nGenMode;
}

int CGxWEncoder::Encode()
{
	CComponent	RootComponent;
	if( !LoadRootComponentFromXML( m_param.m_xmlfilename.c_str(), RootComponent))
		return -1;
	if( strcmp( RootComponent.GetType(), "Frame"))
		return -2;

	if ( !m_param.m_includefile.empty())
	{
		RootComponent.GetPropertyMap().Push( "gxinclude", m_param.m_includefile.c_str());
	}

	if ( !m_param.m_cppappand.empty())
	{
		regex::rpattern rep( std::wstring( L";"), std::wstring( L"\n"), regex::NOCASE | regex::GLOBAL );
		regex::subst_results rs;
		std::wstring wtmp = str2wstr( m_param.m_cppappand);
		rep.substitute( wtmp, rs);

		RootComponent.GetPropertyMap().Push( "gxacpp", wstr2str( wtmp));
	}

	if ( m_param.m_bCrateAutoptr)
		RootComponent.GetPropertyMap().Push( "gxautoptr", "true");

	if ( m_param.m_inlfilepath.empty())
		m_param.m_inlfilepath = m_param.m_outputpath;

	CGenerateCode generater;
	generater.SetPakMode( m_param.m_bPakMode);
	generater.LoadTextFormat( m_param.m_textformatfile.c_str());
	generater.SetNamespace( m_param.m_namespaces.c_str());
	generater.LoadTextResource( m_param.m_textresxmlfile.c_str());
	generater.SetUIName( m_param.m_xmlfilename.c_str());
	generater.SetGenMode( m_param.m_nGenMode);
	if ( !generater.GenerateCode( m_param.m_outputpath, m_param.m_inlfilepath, RootComponent, m_param.m_filenameprefix.empty() ? "" : m_param.m_filenameprefix))
		return -3;

	return 0;
}
