#include "stdafx.h"
#include "codeout.h"
#include "component.h"
#include "generatecode.h"
#include "codecomponent.h"
#include "util.h"
#include "gxwencoder.h"

std::string GetGXWFontEffectType( const TEXTSTATE& ts)
{
	std::string rs;
	if ( ts.bUnderline)
		rs = "GXWINDOW::FET_UNDERLINE";

	if ( ts.bShadow)
	{
		if ( !rs.empty())
			rs += " | ";
		rs += "GXWINDOW::FET_SHADOW";
	}
	else if ( ts.bOutline)
	{
		if ( !rs.empty())
			rs += " | ";
		rs += "GXWINDOW::FET_STROKE";
	}

	if ( rs.empty())
		rs = "GXWINDOW::FET_NONE";

	return rs;
}


CCodeComponent::CCodeComponent()
{

}

CCodeComponent::~CCodeComponent()
{

}

std::string CCodeComponent::CodeOutImageParam(const CComponent& component, const CComponent& parent, CCodeOut& initialize, const std::string& parentvariant, const std::string& strFuncName, CContext& context, const char* appandparms)
{
	CStringCodeOut tmpCodeOut;
	tmpCodeOut.Print( "\t\t%s.%s( GXWINDOW::CImageParams( %s, %s, %s, GXWINDOW::MakeColor( %s))%s);\n", parentvariant.c_str(), strFuncName.c_str(),
		context.QueryImageRes( component.QueryPropertyValue( "f")).c_str(), component.QueryPropertyValue( "x").c_str(), 
		component.QueryPropertyValue( "y").c_str(), ARGB2RGBA( parent.QueryPropertyValue( "b")).c_str(), NULL == appandparms ? "" : appandparms);

	return tmpCodeOut.GetString();
}

bool CCodeComponent::CodeOutDeclaration(const CComponent& component, CCodeOut& declaration, CContext& context)
{
	const char* tmp_type = g_CodeGeneratePropertyMap.GetType( component.GetType());
	if ( !tmp_type)
		return false;

	declaration.Print( "\t%s %s;%s\n", component.QueryPropertyValue( "class").empty() ? tmp_type : component.QueryPropertyValue( "class").c_str(), 
		context.QueryVariant( component).c_str(), component.QueryPropertyValue( "comment").empty() ? "" : ( std::string( " // ") + component.QueryPropertyValue( "comment")).c_str());

	return true;
}

bool CCodeComponent::CodeOutFreeTextDraw(const CComponent& component, CCodeOut& freetext, CContext& context)
{
	return true;
}

bool CCodeComponent::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( _stricmp( "Content", component.GetParentType()) != 0)
		return false;
	const char* tmp_type = g_CodeGeneratePropertyMap.GetType( component.GetType());
	if ( !tmp_type)
		return false;

	if ( !component.QueryPropertyValue( "x").empty() && !component.QueryPropertyValue( "y").empty())
		initialize.Print( "\t%s.SetPos( %s, %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "x").c_str(), component.QueryPropertyValue( "y").c_str());
	if ( !component.QueryPropertyValue( "w").empty() && !component.QueryPropertyValue( "h").empty())
	{
		if ( !component.QueryPropertyValue( "class").empty() && _stricmp( "GXWINDOW::CTree", component.QueryPropertyValue( "class").c_str()) == 0)
			initialize.Print( "\t%s.SetViewWidth( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "w").c_str());
		else if ( _stricmp( COMTYPE_ORITEXT, component.GetType()) == 0 || _stricmp( COMTYPE_ORIEDIT, component.GetType()) == 0)
			initialize.Print( "\t%s.SetWidth( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "w").c_str());
		else
			initialize.Print( "\t%s.SetSize( %s, %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "w").c_str(), component.QueryPropertyValue( "h").c_str());
	}
	// 目前不启用SetTransparent方法的自动生成
	//if ( _stricmp( "true", component.QueryPropertyValue( "transparent").c_str()) == 0)
	//	initialize.Print( "\t%s.SetTransparent();\n", context.QueryVariant( component.GetName()).c_str());

	initialize.Print( "\t%s.SetComponentName( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.GetName());
	
	if ( _stricmp( "true", component.QueryPropertyValue( "viewonly").c_str()) == 0)
		initialize.Print( "\t%s.SetViewOnly( true);\n", context.QueryVariant( component.GetName()).c_str());

	if ( !component.QueryPropertyValue( "tipinfo").empty())
	{
		std::string comment = component.GetName();
		comment.append( "_tip");
		int textResId = context.LoadTextResID( component.QueryPropertyValue( "tipinfo"), context.GetUIName(), comment.c_str());
		initialize.Print( "\t%s.SetTipText( %d); // %d-%s\n", context.QueryVariant( component.GetName()).c_str(), textResId, textResId, component.QueryPropertyValue( "t").c_str());
	}

	return true;
}

void CCodeComponent::CodeOutBackGround( const std::string& bgcomponentname, const CComponent& component, CCodeOut& CodeOut, CContext& context, 
									   const char* bgimagetypename, const char* bgcomponentnamedec, const char* clientWidth, const char* clientHeight, bool bSubScope)
{
	std::vector<int> clrs;
	std::string ImageAlphaColor = ARGB2RGBA( component.QueryPropertyValue( "b"), clrs);

	if ( !bgimagetypename)
		bgimagetypename = COMTYPE_BGIMAGE;
	const CComponent* bgimg = component.FindChild( bgimagetypename);
	if ( bgimg)	// 画背景图
	{
		std::string ImageVariantName = context.QueryImageRes( bgimg->QueryPropertyValue( "f"));

		int cw = atoi( NULL == clientWidth ? component.QueryPropertyValue( "w").c_str() : clientWidth);
		int ch = atoi( NULL == clientHeight ? component.QueryPropertyValue( "h").c_str() : clientHeight);
		int x = atoi( bgimg->QueryPropertyValue( "x").c_str());
		int y = atoi( bgimg->QueryPropertyValue( "y").c_str());
		int w = atoi( bgimg->QueryPropertyValue( "w").c_str());
		int h = atoi( bgimg->QueryPropertyValue( "h").c_str());
		int w0 = atoi( bgimg->QueryPropertyValue( "w0").c_str());
		int h0 = atoi( bgimg->QueryPropertyValue( "h0").c_str());
		int w1 = atoi( bgimg->QueryPropertyValue( "w1").c_str());
		int h1 = atoi( bgimg->QueryPropertyValue( "h1").c_str());
		int wc = w - w0 - w1;
		int hc = h - h0 - h1;

		if ( bSubScope)
			CodeOut.Print( "\t{\n");

		if ( bgcomponentnamedec)
			CodeOut.Print( "\t\t%s\n", bgcomponentnamedec);
		else
		{
			CodeOut.Print( "\t\tGXWINDOW::CImagesFill& bgf = %s.GetTilesBackGround();\n", bgcomponentname.c_str());
			CodeOut.Print( "\t\t%s.SetBackGroundStyle( GXWINDOW::BGS_IMAGE);\n", bgcomponentname.c_str());
		}

		//if ( !setstyle)	
		//	CodeOut.Print( "\t\t%s.SetBackGroundStyle( GXWINDOW::BGS_IMAGE);\n", bgcomponentname.c_str());
		//else
		//	CodeOut.Print( "\t\t%s\n", setstyle);

		if ( h0 > 0)
		{
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_TOPLEFT);\n", ImageVariantName.c_str(), x, y, ImageAlphaColor.c_str());
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_TOP);\n", ImageVariantName.c_str(), x + w0, y, ImageAlphaColor.c_str());
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_TOPRIGHT);\n", ImageVariantName.c_str(), x + w0 + wc, y, ImageAlphaColor.c_str());
		}
		if ( w0 > 0)
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_LEFT);\n", ImageVariantName.c_str(), x, y + h0, ImageAlphaColor.c_str());
		if ( wc > 0 && hc > 0)
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_CENTER);\n", ImageVariantName.c_str(), x + w0, y + h0, ImageAlphaColor.c_str());
		if ( w1 > 0)
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_RIGHT);\n", ImageVariantName.c_str(), x + w0 + wc,y  + h0, ImageAlphaColor.c_str());
		if ( h1 > 0)
		{
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_BOTTOMLEFT);\n", ImageVariantName.c_str(), x, y + h0 + hc, ImageAlphaColor.c_str());
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_BOTTOM);\n", ImageVariantName.c_str(), x + w0, y + h0 + hc, ImageAlphaColor.c_str());
			CodeOut.Print( "\t\tbgf.SetTileImage( GXWINDOW::CImageParams( %s, %d, %d, GXWINDOW::MakeColor( %s)), GXWINDOW::IFP_BOTTOMRIGHT);\n", ImageVariantName.c_str(), x + w0 + wc, y + h0 + hc, ImageAlphaColor.c_str());
		}
		CodeOut.Print( "\t\tbgf.SetTileSize( %d, %d);\n", wc, hc);
		if ( !( 0 == w0 && 0 == h0))
			CodeOut.Print( "\t\tbgf.SetGridSize( %d, %d, GXWINDOW::IFP_TOPLEFT);\n", w0, h0);
		if ( !( 0 == cw - w0 - w1 && 0 == ch - h0 - h1))
			CodeOut.Print( "\t\tbgf.SetGridSize( %d, %d, GXWINDOW::IFP_CENTER);\n", wc == 0 ? 0 : cw - w0 - w1, hc == 0 ? 0 : ch - h0 - h1);
		if ( !( 0 == w1 && 0 == h1))
			CodeOut.Print( "\t\tbgf.SetGridSize( %d, %d, GXWINDOW::IFP_BOTTOMRIGHT);\n", w1, h1);
		if ( !( _stricmp( "tile", bgimg->QueryPropertyValue( "mh").c_str()) == 0 && _stricmp( "tile", bgimg->QueryPropertyValue( "mw").c_str()) == 0))
			CodeOut.Print( "\t\tbgf.SetFillMode( %s, %s);\n", _stricmp( "tile", bgimg->QueryPropertyValue( "mh").c_str()) == 0 ? "true" : "false", 
														  _stricmp( "tile", bgimg->QueryPropertyValue( "mw").c_str()) == 0 ? "true" : "false");
		if ( bSubScope)
			CodeOut.Print( "\t}\n");
	}
	else if ( clrs[3] != 0)	// 画背景色
	{
		CodeOut.Print( "\t%s.SetBackGroundStyleColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ImageAlphaColor.c_str());
	}
}

bool CCodeComponent::CodeOutResponsedFuncDefine( const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context)
{
	return true;
}

bool CCodeComponent::CodeOutResponsedFuncDeclaration(const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp, CContext& context)
{
	return true;
}

void CCodeComponent::CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse)
{

}

void CCodeComponent::SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context)
{

}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeFrame::CCodeFrame()
{

}

CCodeFrame::~CCodeFrame()
{

}

bool CCodeFrame::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CodeOutBackGround( context.QueryVariant( component.GetName()).c_str(), component, initialize, context);
	std::string strTmp = component.QueryPropertyValue( "movetype");
	if ( _stricmp( "title", strTmp.c_str()) == 0)
		strTmp = "FMS_TITLE";
	else if ( _stricmp( "body", strTmp.c_str()) == 0)
		strTmp = "FMS_BODY";
	else if ( _stricmp( "all", strTmp.c_str()) == 0)
		strTmp = "FMS_ALL";
	else
		strTmp = "FMS_NONE";
	initialize.Print( "\t%s.SetMoveStyle( GXWINDOW::%s);\n", context.QueryVariant( component.GetName()).c_str(), strTmp.c_str());

	if ( !component.QueryPropertyValue( "titleheight").empty())
		initialize.Print( "\t%s.SetTitleHeight( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "titleheight").c_str());

	if ( _stricmp( "true", component.QueryPropertyValue( "autoclose").c_str()) == 0)
		initialize.Print( "\t%s.SetAutoClose( true);\n", context.QueryVariant( component.GetName()).c_str());

	if ( _stricmp( "true", component.QueryPropertyValue( "popup").c_str()) == 0)
		initialize.Print( "\t%s.SetViewAsPopup( true);\n", context.QueryVariant( component.GetName()).c_str());

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeButton::CCodeButton()
{

}

CCodeButton::~CCodeButton()
{

}

bool CCodeButton::CodeOutDeclaration(const CComponent& component, CCodeOut& declaration, CContext& context)
{
	if ( !CCodeComponent::CodeOutDeclaration( component, declaration, context))
		return false;
	if ( _stricmp( "true", component.QueryPropertyValue( "onclick").c_str()) == 0)
		context.PushNotifyVariant( COMTYPE_BUTTON, &component);
	return true;
}

bool CCodeButton::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( _stricmp( "true", component.QueryPropertyValue( "onclick").c_str()) == 0)
	{
		notifyadd.Print( "\t%s.ButtonClickNotifyAdd( *this);\n", context.QueryVariant( component.GetName()).c_str());
		release.Print( "\t%s.ButtonClickNotifyRemove( *this);\n", context.QueryVariant( component.GetName()).c_str());
	}

	return true;
}

bool CCodeButton::CodeOutResponsedFuncDefine(const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onclick").c_str()) != 0)
		return false;

	if ( context.GetSplitedNotifyClass().empty())
		codeouticpp.Print( "void %s::On%sButton%sClick()\n{\n}\n\n", context.QueryVariant( "class").c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
	else
	{
		const std::vector<std::string>& splitedset = context.GetSplitedNotifyClass();
		codeoutucpp.Print( "void %s::On%sButton%sClick()\n{\n}\n\n", splitedset[splitedset.size() - 1].c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
	}
	return true;
}

bool CCodeButton::CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp,  CContext& context)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onclick").c_str()) != 0)
		return false;

	std::string tmp = "";
	CCodeOut* pCodeOut = &codeoutcpp;
	if ( context.GetSplitedNotifyClass().empty())
	{
		tmp = "\t";
		pCodeOut = &codeouth;
	}

	pCodeOut->Print( "%svoid On%sButton%sClick();\n", tmp.c_str(), context.QueryVariant( "framename").c_str(), component.GetName());

	return true;
}

void CCodeButton::CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onclick").c_str()) != 0)
		return;

	//CodeOut.Print( "\t%sif( %s.IsSameComponent( c) )\n\t\t%sOn%sButton%sClick();\n", bGenElse ? "else " : "", 
	//	context.QueryVariant( component.GetName()).c_str(), context.GetSplitedNotifyClass().empty() ? "" : "m_scene->", 
	//	context.QueryVariant( "framename").c_str(), component.GetName());
	CodeOut.Print( "\tif( %s.IsSameComponent( c) )\n\t{\n\t\t%sOn%sButton%sClick();\n\t\treturn;\n\t}\n", 
		context.QueryVariant( component.GetName()).c_str(), context.GetSplitedNotifyClass().empty() ? "" : "m_scene->", 
		context.QueryVariant( "framename").c_str(), component.GetName());
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeImageButton::CCodeImageButton()
{

}

CCodeImageButton::~CCodeImageButton()
{

}

bool CCodeImageButton::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeButton::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CStringCodeOut tmpCodeOut;
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGNORMAL))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetComponentImageNormal",  context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGMOUSEON))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetComponentImageMouseIn", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGDOWN))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetImageButtonDown", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGDISABLE))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetComponentImageDisable", context).c_str());

	initialize.Print( "\t{\n");
	initialize.Print( tmpCodeOut.GetString().c_str());
	initialize.Print( "\t}\n");

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTextButton::CCodeTextButton()
{

}

CCodeTextButton::~CCodeTextButton()
{

}

bool CCodeTextButton::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeButton::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CodeOutButtonImage( component, initialize, release, context);

	if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i"));
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
				initialize.Print( "\t%s.SetComponentFont( %d);\n", context.QueryVariant( component.GetName()).c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_DOWN);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextMouseDownColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_MOUSEON);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextMouseInColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_DISABLE);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextDisableColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}
	else
	{
		if ( !component.QueryPropertyValue( "i").empty())
			initialize.Print( "\t%s.SetComponentFont( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "i").c_str());
		if ( !component.QueryPropertyValue( "c0").empty())
			initialize.Print( "\t%s.SetComponentTextDisableColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c0")).c_str());
		if ( !component.QueryPropertyValue( "c1").empty())
			initialize.Print( "\t%s.SetComponentTextColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c1")).c_str());
		if ( !component.QueryPropertyValue( "c3").empty())
			initialize.Print( "\t%s.SetComponentTextMouseDownColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c3")).c_str());
		if ( !component.QueryPropertyValue( "c2").empty())
			initialize.Print( "\t%s.SetComponentTextMouseInColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c2")).c_str());
	}

	if ( !component.QueryPropertyValue( "t").empty())
	{
		if ( !context.IsUseTextRes()) // 没指定字符串资源文件, 直接将字符串生成到代码中
			initialize.Print( "\t%s.SetComponentText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
		else	// 使用字符串资源xml文件, 代码中只生成资源id
		{
			int textResId = context.LoadTextResID( component.QueryPropertyValue( "t"), context.GetUIName(), component.GetName());
			initialize.Print( "\t%s.SetComponentText( %d); // %d-%s\n", context.QueryVariant( component.GetName()).c_str(), textResId, textResId, component.QueryPropertyValue( "t").c_str());
		}
	}
	
	return true;
}

bool CCodeTextButton::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	if ( _stricmp( context.QueryVariant( component.GetName()) .c_str(), "") != 0)
		freetext.Print( "\t\t%s.FreeAllTextDraw();\n", context.QueryVariant( component.GetName()) .c_str());
	return true;
}

void CCodeTextButton::CodeOutButtonImage( const CComponent& component, CCodeOut& initialize, CCodeOut& release, CContext& context)
{
	if ( const CComponent* pimg = component.FindChild( COMTYPE_BGIMAGE))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageNormal();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_BGIMAGE, tmp.GetString().c_str());
	}

	if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGMOUSEON))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageMouseIn();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_IMGMOUSEON, tmp.GetString().c_str());
	}

	if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGDOWN))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageButtonDown();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_IMGDOWN, tmp.GetString().c_str());
	}

	if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGDISABLE))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageDisable();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_IMGDISABLE, tmp.GetString().c_str());
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeCheckButton::CCodeCheckButton()
{

}

CCodeCheckButton::~CCodeCheckButton()
{

}

bool CCodeCheckButton::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeButton::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CStringCodeOut tmpCodeOut;

	//if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGNORMAL))
	//{
	//	tmpCodeOut.Print( "GXWINDOW::CImageParams( %s, %s, %s)", context.QueryImageRes( pimg->QueryPropertyValue( "f"), initialize).c_str(), 
	//		pimg->QueryPropertyValue( "x").c_str(), pimg->QueryPropertyValue( "y").c_str());
	//}
	//if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGDOWN))
	//{
	//	tmpCodeOut.Print( "%sGXWINDOW::CImageParams( %s, %s, %s)", tmpCodeOut.GetString().empty() ? "" : ", ", context.QueryImageRes( pimg->QueryPropertyValue( "f"), initialize).c_str(), 
	//		pimg->QueryPropertyValue( "x").c_str(), pimg->QueryPropertyValue( "y").c_str());
	//}
	//if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGMOUSEON))
	//{
	//	tmpCodeOut.Print( "%sGXWINDOW::CImageParams( %s, %s, %s)", tmpCodeOut.GetString().empty() ? "" : ", ", context.QueryImageRes( pimg->QueryPropertyValue( "f"), initialize).c_str(), 
	//		pimg->QueryPropertyValue( "x").c_str(), pimg->QueryPropertyValue( "y").c_str());
	//}
	//if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGSELECTMOUSEON))
	//{
	//	tmpCodeOut.Print( "%sGXWINDOW::CImageParams( %s, %s, %s)", tmpCodeOut.GetString().empty() ? "" : ", ", context.QueryImageRes( pimg->QueryPropertyValue( "f"), initialize).c_str(), 
	//		pimg->QueryPropertyValue( "x").c_str(), pimg->QueryPropertyValue( "y").c_str());
	//}
	//if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGDISABLE))
	//{
	//	tmpCodeOut.Print( "%sGXWINDOW::CImageParams( %s, %s, %s)", tmpCodeOut.GetString().empty() ? "" : ", ", context.QueryImageRes( pimg->QueryPropertyValue( "f"), initialize).c_str(), 
	//		pimg->QueryPropertyValue( "x").c_str(), pimg->QueryPropertyValue( "y").c_str());
	//}

	//initialize.Print( "\t%s.SetButtonBoxStyleImage( %s);\n", context.QueryVariant( component.GetName()).c_str(), tmpCodeOut.GetString().c_str());

	CodeOutButtonTileImage( component, initialize, release, context);

	if ( !component.QueryPropertyValue( "boxwidth").empty())
		initialize.Print( "\t%s.SetBoxWidth( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "boxwidth").c_str());

	return true;
}

void CCodeCheckButton::CodeOutButtonTileImage( const CComponent& component, CCodeOut& initialize, CCodeOut& release, CContext& context)
{
	initialize.Print( "\t%s.SetButtonBoxStyle( GXWINDOW::BBS_TILE);\n", context.QueryVariant( component.GetName()).c_str());

	if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGNORMAL))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageNormal();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_IMGNORMAL, tmp.GetString().c_str());
	}

	if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGMOUSEON))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageMouseIn();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_IMGMOUSEON, tmp.GetString().c_str());
	}

	if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGDOWN))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageSelect();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_IMGDOWN, tmp.GetString().c_str(), component.QueryPropertyValue("boxwidth").c_str());
	}

	if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGSELECTMOUSEON))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageMouseInSelect();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_IMGSELECTMOUSEON, tmp.GetString().c_str(), component.QueryPropertyValue("boxwidth").c_str());
	}

	if ( const CComponent* pimg = component.FindChild( COMTYPE_IMGDISABLE))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageDisable();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_IMGDISABLE, tmp.GetString().c_str());
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTextCheckButton::CCodeTextCheckButton()
{

}

CCodeTextCheckButton::~CCodeTextCheckButton()
{

}

bool CCodeTextCheckButton::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeCheckButton::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i"));
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
			initialize.Print( "\t%s.SetComponentFont( %d);\n", context.QueryVariant( component.GetName()).c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_MOUSEON);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextMouseInColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_DISABLE);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextDisableColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}
	else
	{
		if ( !component.QueryPropertyValue( "i").empty())
			initialize.Print( "\t%s.SetComponentFont( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "i").c_str());
		if ( !component.QueryPropertyValue( "c0").empty())
			initialize.Print( "\t%s.SetComponentTextDisableColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c0")).c_str());
		if ( !component.QueryPropertyValue( "c1").empty())
			initialize.Print( "\t%s.SetComponentTextColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c1")).c_str());
		if ( !component.QueryPropertyValue( "c2").empty())
			initialize.Print( "\t%s.SetComponentTextMouseInColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c2")).c_str());
	}

	//if ( !component.QueryPropertyValue( "t").empty())
	//	initialize.Print( "\t%s.SetComponentText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
	if ( !component.QueryPropertyValue( "t").empty())
	{
		if ( !context.IsUseTextRes()) // 没指定字符串资源文件, 直接将字符串生成到代码中
			initialize.Print( "\t%s.SetComponentText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
		else	// 使用字符串资源xml文件, 代码中只生成资源id
		{
			int textResId = context.LoadTextResID( component.QueryPropertyValue( "t"), context.GetUIName(), component.GetName());
			initialize.Print( "\t%s.SetComponentText( %d); // %d-%s\n", context.QueryVariant( component.GetName()).c_str(), textResId, textResId, component.QueryPropertyValue( "t").c_str());
		}
	}

	return true;
}

bool CCodeTextCheckButton::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	freetext.Print( "\t\t%s.FreeAllTextDraw();\n", context.QueryVariant( component.GetName()) .c_str());
	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTextImageCheckButton::CCodeTextImageCheckButton()
{

}

CCodeTextImageCheckButton::~CCodeTextImageCheckButton()
{

}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeRadioButton::CCodeRadioButton()
{

}

CCodeRadioButton::~CCodeRadioButton()
{

}

bool CCodeRadioButton::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeCheckButton::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( !component.QueryPropertyValue( "groupid").empty())
		initialize.Print( "\t%s.SetGroupID( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "groupid").c_str());

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTextRadioButton::CCodeTextRadioButton()
{

}

CCodeTextRadioButton::~CCodeTextRadioButton()
{

}

bool CCodeTextRadioButton::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeTextCheckButton::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( !component.QueryPropertyValue( "groupid").empty())
		initialize.Print( "\t%s.SetGroupID( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "groupid").c_str());

	return true;
}

bool CCodeTextRadioButton::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	freetext.Print( "\t\t%s.FreeAllTextDraw();\n", context.QueryVariant( component.GetName()) .c_str());
	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTextImageRadioButton::CCodeTextImageRadioButton()
{

}

CCodeTextImageRadioButton::~CCodeTextImageRadioButton()
{

}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodePanel::CCodePanel()
{

}

CCodePanel::~CCodePanel()
{

}

bool CCodePanel::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	return true;
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeBKPanel::CCodeBKPanel()
{

}

CCodeBKPanel::~CCodeBKPanel()
{

}

bool CCodeBKPanel::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeBKBaseComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	return true;
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeScrollPanel::CCodeScrollPanel()
{

}

CCodeScrollPanel::~CCodeScrollPanel()
{

}

bool CCodeScrollPanel::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodePanel::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CCodeScrollBar scb;
	if ( const CComponent* pvscb = component.FindChild( COMTYPE_VSCROLLBAR))
	{
		CStringCodeOut tmpDec;
		tmpDec.Print( "\t\tGXWINDOW::CScrollBar& vscb = *( %s.GetVerticalScrollBar());\n", context.QueryVariant( component.GetName()).c_str());
		scb.CodeOutScrollBarSetup( "vscb", *pvscb, tmpDec.GetString(), initialize, context, NULL, component.QueryPropertyValue( "h").c_str());
	}
	if ( const CComponent* phscb = component.FindChild( COMTYPE_HSCROLLBAR))
	{
		CStringCodeOut tmpDec;
		tmpDec.Print( "\t\tGXWINDOW::CScrollBar& hscb = *( %s.GetHorizontalScrollBar());\n", context.QueryVariant( component.GetName()).c_str());
		scb.CodeOutScrollBarSetup( "hscb", *phscb, tmpDec.GetString(), initialize, context, component.QueryPropertyValue( "w").c_str(), NULL);
	}

	//if ( !component.QueryPropertyValue( "border").empty())
	//	initialize.Print( "\t%s.SetBorderWidth( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "border").c_str());

	return true;
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTextComponent::CCodeTextComponent()
{

}

CCodeTextComponent::~CCodeTextComponent()
{

}

bool CCodeTextComponent::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	//CodeOutBackGround( context.QueryVariant( component.GetName()).c_str(), component, initialize, context);

	if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i")); 
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
			initialize.Print( "\t%s.SetComponentFont( %d);\n", context.QueryVariant( component.GetName()).c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_MOUSEON);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextMouseInColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_DISABLE);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetComponentTextDisableColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}
	else
	{
		if ( !component.QueryPropertyValue( "i").empty())
			initialize.Print( "\t%s.SetComponentFont( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "i").c_str());
		if ( !component.QueryPropertyValue( "c1").empty())
			initialize.Print( "\t%s.SetComponentTextColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c1")).c_str());
		if ( !component.QueryPropertyValue( "c0").empty())
			initialize.Print( "\t%s.SetComponentTextDisableColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c0")).c_str());
		if ( !component.QueryPropertyValue( "c2").empty())
			initialize.Print( "\t%s.SetComponentTextMouseInColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "c2")).c_str());
	}

	//if ( !component.QueryPropertyValue( "t").empty())
	//	initialize.Print( "\t%s.SetComponentText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
	if ( !component.QueryPropertyValue( "t").empty())
	{
		if ( !context.IsUseTextRes()) // 没指定字符串资源文件, 直接将字符串生成到代码中
			initialize.Print( "\t%s.SetComponentText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
		else	// 使用字符串资源xml文件, 代码中只生成资源id
		{
			int textResId = context.LoadTextResID( component.QueryPropertyValue( "t"), context.GetUIName(), component.GetName());
			initialize.Print( "\t%s.SetComponentText( %d); // %d-%s\n", context.QueryVariant( component.GetName()).c_str(), textResId, textResId, component.QueryPropertyValue( "t").c_str());
		}
	}

	if ( !component.QueryPropertyValue( "center").empty() && _stricmp( component.QueryPropertyValue( "center").c_str(), "true") == 0)
		initialize.Print( "\t%s.SetTextAlign( GXWINDOW::TXA_CENTER | GXWINDOW::TXA_MIDDLE);\n", context.QueryVariant( component.GetName()).c_str());
	return true;
}

bool CCodeTextComponent::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	freetext.Print( "\t\t%s.FreeAllTextDraw();\n", context.QueryVariant( component.GetName()).c_str());
	return CCodeComponent::CodeOutFreeTextDraw( component, freetext, context);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeImageComponent::CCodeImageComponent()
{

}

CCodeImageComponent::~CCodeImageComponent()
{

}

bool CCodeImageComponent::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CStringCodeOut tmpCodeOut;

	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGNORMAL))
		tmpCodeOut.Print( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetComponentImageNormal",  context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGMOUSEON))
		tmpCodeOut.Print( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetComponentImageMouseIn", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGDISABLE))
		tmpCodeOut.Print( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetComponentImageDisable", context).c_str());

	initialize.Print( "\t{\n");
	initialize.Print( tmpCodeOut.GetString().c_str());
	initialize.Print( "\t}\n");

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeBaseComponent::CCodeBaseComponent()
{

}

CCodeBaseComponent::~CCodeBaseComponent()
{

}

bool CCodeBaseComponent::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	//if ( _stricmp( "true", component.QueryPropertyValue( "transparent").c_str()) == 0)
	//	initialize.Print( "\t%s.SetTransparent();\n", context.QueryVariant( component.GetName()).c_str());

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeBKBaseComponent::CCodeBKBaseComponent()
{

}

CCodeBKBaseComponent::~CCodeBKBaseComponent()
{

}

bool CCodeBKBaseComponent::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeBaseComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( component.FindChild( COMTYPE_BGIMAGE) )
		CodeOutBackGround( context.QueryVariant( component.GetName()).c_str(), component, initialize, context);
	else if ( !component.QueryPropertyValue( "b").empty())
		initialize.Print( "\t%s.SetBackGroundStyleColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "b")).c_str());

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTextArea::CCodeTextArea()
{

}

CCodeTextArea::~CCodeTextArea()
{

}

bool CCodeTextArea::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	//CodeOutBackGround( context.QueryVariant( component.GetName()) + ".GetScrollPanel()", component, initialize, context);

	CCodeScrollBar scb;
	if ( const CComponent* pscb = component.FindChild( COMTYPE_SCROLLBAR))
	{
		CStringCodeOut tmpDec;
		tmpDec.Print( "\t\tGXWINDOW::CScrollBar& scb = *( %s.GetScrollPanel().GetVerticalScrollBar());\n", context.QueryVariant( component.GetName()).c_str());
		scb.CodeOutScrollBarSetup( "scb", *pscb, tmpDec.GetString(), initialize, context, NULL, component.QueryPropertyValue( "h").c_str());
	}


	if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i"));
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
			initialize.Print( "\t%s.GetTextArea().SetDefaultFont( %d);\n", context.QueryVariant( component.GetName()).c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.GetTextArea().SetDefaultTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}
	else
	{
		if ( !component.QueryPropertyValue( "i").empty())
			initialize.Print( "\t%s.GetTextArea().SetDefaultFont( %s);\n", context.QueryVariant( component.GetName()) .c_str(), component.QueryPropertyValue( "i").c_str());
		if ( !component.QueryPropertyValue( "c").empty())
			initialize.Print( "\t%s.GetTextArea().SetDefaultTextColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()) .c_str(), ARGB2RGBA( component.QueryPropertyValue( "c")).c_str());
	}

	//if ( !component.QueryPropertyValue( "t").empty())
	//	initialize.Print( "\t%s.AppendText( L\"%s\");\n", context.QueryVariant( component.GetName()) .c_str(), component.QueryPropertyValue( "t").c_str());
	if ( !component.QueryPropertyValue( "t").empty())
	{
		if ( !context.IsUseTextRes()) // 没指定字符串资源文件, 直接将字符串生成到代码中
			initialize.Print( "\t%s.GetTextArea().AppendText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
		else	// 使用字符串资源xml文件, 代码中只生成资源id
		{
			int textResId = context.LoadTextResID( component.QueryPropertyValue( "t"), context.GetUIName(), component.GetName());
			initialize.Print( "\t%s.GetTextArea().AppendText( %d); // %d-%s\n", context.QueryVariant( component.GetName()).c_str(), textResId, textResId, component.QueryPropertyValue( "t").c_str());
		}
	}
	if ( _stricmp( "false", component.QueryPropertyValue( "multiline").c_str()) == 0)
		initialize.Print( "\t%s.GetTextArea().SetSingleLine();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "select").c_str()) == 0)
		initialize.Print( "\t%s.GetTextArea().SetSelectAllow();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "password").c_str()) == 0)
		initialize.Print( "\t%s.GetTextArea().SetPassword();\n", context.QueryVariant( component.GetName()) .c_str());

	return true;
}

bool CCodeTextArea::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	freetext.Print( "\t\t%s.GetTextArea().EraseAll();\n", context.QueryVariant( component.GetName()) .c_str());
	return true;
}

void CCodeTextArea::SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	CodeOut.Print( "\tvoid Set%sText( const wchar_t* szText)\n\t{\n", component.GetName());
	CodeOut.Print( "\t\t%s.GetTextArea().EraseAll();\n", context.QueryVariant( component.GetName()).c_str());
	CodeOut.Print( "\t\t%s.GetTextArea().AppendText( szText);\n\t}\n", context.QueryVariant( component.GetName()).c_str());
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeOriTextArea::CCodeOriTextArea()
{

}

CCodeOriTextArea::~CCodeOriTextArea()
{

}

bool CCodeOriTextArea::CodeOutInitializeAndRelease(const CComponent &component, CCodeOut &initialize, CCodeOut &notifyadd, CCodeOut &release, CContext &context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i"));
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
			initialize.Print( "\t%s.SetDefaultFont( %d);\n", context.QueryVariant( component.GetName()).c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetDefaultTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}
	else
	{
		if ( !component.QueryPropertyValue( "i").empty())
			initialize.Print( "\t%s.SetDefaultFont( %s);\n", context.QueryVariant( component.GetName()) .c_str(), component.QueryPropertyValue( "i").c_str());
		if ( !component.QueryPropertyValue( "c").empty())
			initialize.Print( "\t%s.SetDefaultTextColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()) .c_str(), ARGB2RGBA( component.QueryPropertyValue( "c")).c_str());
	}

	if ( !component.QueryPropertyValue( "t").empty())
	{
		if ( !context.IsUseTextRes()) // 没指定字符串资源文件, 直接将字符串生成到代码中
			initialize.Print( "\t%s.AppendText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
		else	// 使用字符串资源xml文件, 代码中只生成资源id
		{
			int textResId = context.LoadTextResID( component.QueryPropertyValue( "t"), context.GetUIName(), component.GetName());
			initialize.Print( "\t%s.AppendText( %d); // %d-%s\n", context.QueryVariant( component.GetName()).c_str(), textResId, textResId, component.QueryPropertyValue( "t").c_str());
		}
	}
	if ( _stricmp( "false", component.QueryPropertyValue( "multiline").c_str()) == 0)
		initialize.Print( "\t%s.SetSingleLine();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "select").c_str()) == 0)
		initialize.Print( "\t%s.SetSelectAllow();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "password").c_str()) == 0)
		initialize.Print( "\t%s.SetPassword();\n", context.QueryVariant( component.GetName()) .c_str());

	return true;
}

bool CCodeOriTextArea::CodeOutFreeTextDraw(const CComponent& component, CCodeOut& freetext, CContext& context)
{
	freetext.Print( "\t\t%s.EraseAll();\n", context.QueryVariant( component.GetName()) .c_str());
	return true;
}

void CCodeOriTextArea::SetAndGetProperty(const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	CodeOut.Print( "\tvoid Set%sText( const wchar_t* szText)\n\t{\n", component.GetName());
	CodeOut.Print( "\t\t%s.EraseAll();\n", context.QueryVariant( component.GetName()).c_str());
	CodeOut.Print( "\t\t%s.AppendText( szText);\n\t}\n", context.QueryVariant( component.GetName()).c_str());
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeOriEidtArea::CCodeOriEidtArea()
{

}

CCodeOriEidtArea::~CCodeOriEidtArea()
{

}

bool CCodeOriEidtArea::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i")); 
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
			initialize.Print( "\t%s.SetDefaultFont( %d);\n", context.QueryVariant( component.GetName()).c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.SetDefaultTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}
	else
	{
		if ( !component.QueryPropertyValue( "i").empty())
			initialize.Print( "\t%s.SetDefaultFont( %s);\n", context.QueryVariant( component.GetName()) .c_str(), component.QueryPropertyValue( "i").c_str());
		if ( !component.QueryPropertyValue( "c").empty())
			initialize.Print( "\t%s.SetDefaultTextColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()) .c_str(), ARGB2RGBA( component.QueryPropertyValue( "c")).c_str());
	}

	if ( !component.QueryPropertyValue( "t").empty())
	{
		if ( !context.IsUseTextRes()) // 没指定字符串资源文件, 直接将字符串生成到代码中
			initialize.Print( "\t%s.AppendText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
		else	// 使用字符串资源xml文件, 代码中只生成资源id
		{
			int textResId = context.LoadTextResID( component.QueryPropertyValue( "t"), context.GetUIName(), component.GetName());
			initialize.Print( "\t%s.AppendText( %d); // %d-%s\n", context.QueryVariant( component.GetName()).c_str(), textResId, textResId, component.QueryPropertyValue( "t").c_str());
		}
	}
	if ( _stricmp( "false", component.QueryPropertyValue( "multiline").c_str()) == 0)
		initialize.Print( "\t%s.SetSingleLine();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "select").c_str()) == 0)
		initialize.Print( "\t%s.SetSelectAllow();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "password").c_str()) == 0)
		initialize.Print( "\t%s.SetPassword();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( !component.QueryPropertyValue( "limit").empty())
		initialize.Print( "\t%s.SetLimit( %s);\n", context.QueryVariant( component.GetName()) .c_str(), component.QueryPropertyValue( "limit").c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "number").c_str()) == 0)
		initialize.Print( "\t%s.SetNumber();\n", context.QueryVariant( component.GetName()) .c_str());

	return true;
}

bool CCodeOriEidtArea::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	freetext.Print( "\t\t%s.EraseAll();\n", context.QueryVariant( component.GetName()) .c_str());
	return true;
}

void CCodeOriEidtArea::SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	CodeOut.Print( "\tstd::wstring Get%sText()\n\t{\n", component.GetName());
	CodeOut.Print( "\t\tGXWINDOW::CFormatTextEncoder ftd;\n");
	CodeOut.Print( "\t\t%s.Encode( ftd);\n", context.QueryVariant( component.GetName()).c_str());
	CodeOut.Print( "\t\treturn ftd.GetResult();\n\t}\n");
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeSingleEidtArea::CCodeSingleEidtArea()
{

}

CCodeSingleEidtArea::~CCodeSingleEidtArea()
{

}

bool CCodeSingleEidtArea::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	//CodeOutBackGround( context.QueryVariant( component.GetName()) + ".GetScrollPanel()", component, initialize, context);

	if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i")); 
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
			initialize.Print( "\t%s.GetEditArea().SetDefaultFont( %d);\n", context.QueryVariant( component.GetName()).c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				initialize.Print( "\t%s.GetEditArea().SetDefaultTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}
	else
	{
		if ( !component.QueryPropertyValue( "i").empty())
			initialize.Print( "\t%s.GetEditArea().SetDefaultFont( %s);\n", context.QueryVariant( component.GetName()) .c_str(), component.QueryPropertyValue( "i").c_str());
		if ( !component.QueryPropertyValue( "c").empty())
			initialize.Print( "\t%s.GetEditArea().SetDefaultTextColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()) .c_str(), ARGB2RGBA( component.QueryPropertyValue( "c")).c_str());
	}

	//if ( !component.QueryPropertyValue( "t").empty())
	//	initialize.Print( "\t%s.AppendText( L\"%s\");\n", context.QueryVariant( component.GetName()) .c_str(), component.QueryPropertyValue( "t").c_str());
	if ( !component.QueryPropertyValue( "t").empty())
	{
		if ( !context.IsUseTextRes()) // 没指定字符串资源文件, 直接将字符串生成到代码中
			initialize.Print( "\t%s.GetEditArea().AppendText( L\"%s\");\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "t").c_str());
		else	// 使用字符串资源xml文件, 代码中只生成资源id
		{
			int textResId = context.LoadTextResID( component.QueryPropertyValue( "t"), context.GetUIName(), component.GetName());
			initialize.Print( "\t%s.GetEditArea().AppendText( %d); // %d-%s\n", context.QueryVariant( component.GetName()).c_str(), textResId, textResId, component.QueryPropertyValue( "t").c_str());
		}
	}
	if ( _stricmp( "true", component.QueryPropertyValue( "select").c_str()) == 0)
		initialize.Print( "\t%s.GetEditArea().SetSelectAllow();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "password").c_str()) == 0)
		initialize.Print( "\t%s.GetEditArea().SetPassword();\n", context.QueryVariant( component.GetName()) .c_str());
	if ( !component.QueryPropertyValue( "limit").empty())
		initialize.Print( "\t%s.GetEditArea().SetLimit( %s);\n", context.QueryVariant( component.GetName()) .c_str(), component.QueryPropertyValue( "limit").c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "number").c_str()) == 0)
		initialize.Print( "\t%s.GetEditArea().SetNumber();\n", context.QueryVariant( component.GetName()) .c_str());

	if( _stricmp( context.QueryVariant( COMTYPE_HOOKEDIT).c_str(), "true") == 0)
		context.PushNotifyVariant( COMTYPE_HOOKEDIT, &component);

	return true;
}

bool CCodeSingleEidtArea::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	freetext.Print( "\t\t%s.GetEditArea().EraseAll();\n", context.QueryVariant( component.GetName()) .c_str());
	return true;
}

void CCodeSingleEidtArea::SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	CodeOut.Print( "\tstd::wstring Get%sText()\n\t{\n", component.GetName());
	CodeOut.Print( "\t\tGXWINDOW::CFormatTextEncoder ftd;\n");
	CodeOut.Print( "\t\t%s.GetEditArea().Encode( ftd);\n", context.QueryVariant( component.GetName()).c_str());
	CodeOut.Print( "\t\treturn ftd.GetResult();\n\t}\n");
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeMultiLineEidtArea::CCodeMultiLineEidtArea()
{

}

CCodeMultiLineEidtArea::~CCodeMultiLineEidtArea()
{

}

bool CCodeMultiLineEidtArea::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeSingleEidtArea::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;


	CCodeScrollBar scb;
	if ( const CComponent* pscb = component.FindChild( COMTYPE_SCROLLBAR))
	{
		CStringCodeOut tmpDec;
		tmpDec.Print( "\t\tGXWINDOW::CScrollBar& scb = *( %s.GetScrollPanel().GetVerticalScrollBar());\n", context.QueryVariant( component.GetName()).c_str());
		scb.CodeOutScrollBarSetup( "scb", *pscb, tmpDec.GetString(), initialize, context, NULL, component.QueryPropertyValue( "h").c_str());
	}

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeListBox::CCodeListBox() {}
CCodeListBox::~CCodeListBox() {}

bool CCodeListBox::CodeOutDeclaration( const CComponent& component, CCodeOut& declaration, CContext& context)
{
	if ( !CCodeBKBaseComponent::CodeOutDeclaration( component, declaration, context))
		return false;

	DeclarationListDataModule( component, declaration, context);

	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
		context.PushNotifyVariant( COMTYPE_LISTBOX, &component);

	return true;
}

void CCodeListBox::DeclarationListDataModule( const CComponent& component, CCodeOut& declaration, CContext& context)
{
	std::string ldmtype = component.QueryPropertyValue( "datatype");
	std::string ldmvariant = "m_tldm";
	ldmvariant += component.GetName();

	if ( _stricmp( COMTYPE_LDT_TEXT, component.QueryPropertyValue( "datatype").c_str()) == 0)
	{
		ldmtype = "GXWINDOW::CTextListDataModule";
		ldmvariant = "m_tldm";
		ldmvariant += component.GetName();
	}
	else if ( _stricmp( COMTYPE_LDT_CLRTEXT, component.QueryPropertyValue( "datatype").c_str()) == 0)
	{
		ldmtype = "GXWINDOW::CColorTextListDataModule";
		ldmvariant = "m_ctldm";
		ldmvariant += component.GetName();
	}
	else if ( _stricmp( COMTYPE_LDT_IMG, component.QueryPropertyValue( "datatype").c_str()) == 0)
	{
		ldmtype = "GXWINDOW::CImageListDataModlue";
		ldmvariant = "m_ildm";
		ldmvariant += component.GetName();
	}

	declaration.Print( "\t%s %s;\n", ldmtype.c_str(), ldmvariant.c_str());

	context.PushVariant( std::string( component.GetName()) + "_ldm", ldmvariant);
	context.PushVariant( std::string( component.GetName()) + "_ldmtype", ldmtype);
}

bool CCodeListBox::CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeBKBaseComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	SetupListDataModule( component, initialize, context);

	std::string ldmvariant = context.QueryVariant( std::string( component.GetName()) + "_ldm");

	if ( !component.QueryPropertyValue( "lineheight").empty() && _stricmp( "0", component.QueryPropertyValue( "lineheight").c_str()) != 0)
		initialize.Print( "\t%s.SetClientComponentHeight( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "lineheight").c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "multiselect").c_str()) == 0)
		initialize.Print( "\t%s.SetMultiSelect();\n", context.QueryVariant( component.GetName()).c_str());
	initialize.Print( "\t%s.SetListDataModule( %s);\n", context.QueryVariant( component.GetName()).c_str(), ldmvariant.c_str());

	if ( const CComponent* pc = component.FindChild( COMTYPE_LISTBOX_LINE))
	{
		CStringCodeOut dec;
		dec.Print( "GXWINDOW::CImagesFill& bgf = %s.GetRowBackGround();", context.QueryVariant( std::string( component.GetName()) + "_ldm").c_str());
		CodeOutBackGround( "headerimg", component, initialize, context, COMTYPE_LISTBOX_LINE, dec.GetString().c_str(), pc->QueryPropertyValue( "w").c_str(), pc->QueryPropertyValue( "h").c_str());
	}

	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
	{
		notifyadd.Print( "\t%s.ListBoxSelectChangeNotifyAdd( *this);\n", context.QueryVariant( component.GetName()).c_str());
		release.Print( "\t%s.ListBoxSelectChangeNotifyAdd( *this);\n", context.QueryVariant( component.GetName()).c_str());
	}

	return true;
}

void CCodeListBox::SetupListDataModule(const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	std::string ldmvariant = context.QueryVariant( std::string( component.GetName()) + "_ldm");

	if ( _stricmp( "image", component.QueryPropertyValue( "datatype").c_str()) != 0 && !component.QueryPropertyValue( "i").empty())
	{
		if ( context.IsTextFormat())
		{
			const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i"));
			if ( ptf)
			{
				const TEXTFORMAT& tf = *ptf;
				CodeOut.Print( "\t%s.SetTextFont( %d);\n", ldmvariant.c_str(), tf.nFontType);
			}
		}
		else
			CodeOut.Print( "\t%s.SetTextFont( %s);\n", ldmvariant.c_str(), component.QueryPropertyValue( "i").c_str());
	}

	if ( _stricmp( "image", component.QueryPropertyValue( "datatype").c_str()) != 0)
	{
		if ( !component.QueryPropertyValue( "b1").empty())
			CodeOut.Print( "\t%s.SetBackGroundMouseOnColor( GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "b1")).c_str());
		if ( !component.QueryPropertyValue( "b3").empty())
			CodeOut.Print( "\t%s.SetBackGroundSelectedColor( GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "b3")).c_str());
	}
	else
	{
		if ( !component.QueryPropertyValue( "b1").empty())
			CodeOut.Print( "\t%s.SetImageMouseOnColor( GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "b1")).c_str());
		if ( !component.QueryPropertyValue( "b3").empty())
			CodeOut.Print( "\t%s.SetImageSelectedColor( GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "b3")).c_str());
	}

	if ( _stricmp( COMTYPE_LDT_CLRTEXT, component.QueryPropertyValue( "datatype").c_str()) != 0 && _stricmp( COMTYPE_LDT_IMG, component.QueryPropertyValue( "datatype").c_str()) != 0 )
	{

		if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
		{
			const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i")); 
			if ( ptf)
			{
				const TEXTFORMAT& tf = *ptf;

				CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
				if ( it != tf.textStateMap.end())
					CodeOut.Print( "\t%s.SetTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

				it = tf.textStateMap.find( TSF_SELECT);
				if ( it != tf.textStateMap.end())
					CodeOut.Print( "\t%s.SetTextSelectedColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

				it = tf.textStateMap.find( TSF_MOUSEON);
				if ( it != tf.textStateMap.end())
					CodeOut.Print( "\t%s.SetTextMouseOnColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
			}
		}
		else
		{
			if ( !component.QueryPropertyValue( "c1").empty())
				CodeOut.Print( "\t%s.SetTextColor( GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "c1")).c_str());
			if ( !component.QueryPropertyValue( "c2").empty())
				CodeOut.Print( "\t%s.SetTextMouseOnColor( GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "c2")).c_str());
			if ( !component.QueryPropertyValue( "c3").empty())
				CodeOut.Print( "\t%s.SetTextSelectedColor( GXWINDOW::MakeColor( %s));\n", ldmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "c3")).c_str());
		}
	}
}

bool CCodeListBox::CodeOutResponsedFuncDefine( const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) != 0)
		return false;

	if ( context.GetSplitedNotifyClass().empty())
		codeouticpp.Print( "void %s::On%sListBox%sSelectChange( int n)\n{\n}\n\n", context.QueryVariant( "class").c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
	else
	{
		const std::vector<std::string>& splitedset = context.GetSplitedNotifyClass();
		codeoutucpp.Print( "void %s::On%sListBox%sSelectChange( int n)\n{\n}\n\n", splitedset[splitedset.size() - 1].c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
	}

	return true;
}

bool CCodeListBox::CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp, CContext& context)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) != 0)
		return false;

	std::string tmp = "";
	CCodeOut* pCodeOut = &codeoutcpp;
	if ( context.GetSplitedNotifyClass().empty())
	{
		tmp = "\t";
		pCodeOut = &codeouth;
	}

	pCodeOut->Print( "%svoid On%sListBox%sSelectChange( int n);\n", tmp.c_str(), context.QueryVariant( "framename").c_str(), component.GetName());

	return true;
}

void CCodeListBox::CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) != 0)
		return;

	CodeOut.Print( "\t%sif( %s.IsSameComponent( lb) )\n\t\t%sOn%sListBox%sSelectChange( n);\n", bGenElse ? "else " : "", 
		context.QueryVariant( component.GetName()).c_str(), context.GetSplitedNotifyClass().empty() ? "" : "m_scene->", 
		context.QueryVariant( "framename").c_str(), component.GetName());
}

bool CCodeListBox::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	if ( _stricmp( COMTYPE_LDT_IMG, component.QueryPropertyValue( "datatype").c_str()) != 0)
	{
		freetext.Print( "\t\t%s.FreeAllTextDraw();\n", context.QueryVariant( std::string( component.GetName()) + "_ldm").c_str());
	}
	
	return true;
}

void CCodeListBox::SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	CodeOut.Print( "\t%s& Get%sListDataModule()\n\t{\n", context.QueryVariant( std::string( component.GetName()) + "_ldmtype").c_str(), context.QueryVariant( component.GetName()).c_str());
	CodeOut.Print( "\t\treturn %s;\n\t}\n", context.QueryVariant( std::string( component.GetName()) + "_ldm").c_str());
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeComboBox::CCodeComboBox()
{

}

CCodeComboBox::~CCodeComboBox()
{

}

bool CCodeComboBox::CodeOutDeclaration(const CComponent& component, CCodeOut& declaration, CContext& context)
{
	if ( !CCodeBaseComponent::CodeOutDeclaration( component, declaration, context))
		return false;

	CCodeListBox listbox;
	listbox.DeclarationListDataModule( component, declaration, context);

	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
		context.PushNotifyVariant( COMTYPE_COMBOBOX, &component);
	return true;
}

bool CCodeComboBox::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeBaseComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	// 组合框背景
	if ( component.FindChild( COMTYPE_COMBOBOX_IMGBOX) )
		CodeOutBackGround( context.QueryVariant( component.GetName()).c_str(), component, initialize, context, COMTYPE_COMBOBOX_IMGBOX);
	else if ( !component.QueryPropertyValue( "b").empty())
		initialize.Print( "\t%s.SetBackGroundStyleColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "b")).c_str());

	// 下拉按钮图片
	CStringCodeOut tmpCodeOut;
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGNORMAL))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, "combobtn", "SetComponentImageNormal",  context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGMOUSEON))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, "combobtn", "SetComponentImageMouseIn", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGDOWN))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, "combobtn", "SetImageButtonDown", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGDISABLE))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, "combobtn", "SetComponentImageDisable", context).c_str());

	initialize.Print( "\t{\n");
	initialize.Print( "\t\tGXWINDOW::CImageButton& combobtn = *( %s.GetButton());\n", context.QueryVariant( component.GetName()).c_str());
	initialize.Print( tmpCodeOut.GetString().c_str());
	initialize.Print( "\t}\n");

	// 下拉框Frame背景
	if ( component.FindChild( COMTYPE_COMBOBOX_IMGLIST))
	{
		CStringCodeOut tmp;
		tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetDrawListBackGround();", context.QueryVariant( component.GetName()).c_str());
		CodeOutBackGround( "scp", component, initialize, context, COMTYPE_COMBOBOX_IMGLIST, tmp.GetString().c_str(), NULL, component.QueryPropertyValue( "listboxh").c_str());
	}
	else if ( !component.QueryPropertyValue( "b").empty())
		initialize.Print( "\t%.SetDrawListBackGroundColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "b")).c_str());


	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
	{
		notifyadd.Print( "\t%s.SelectChangeNotifyAdd( *this);\n", context.QueryVariant( component.GetName()).c_str());
		release.Print( "\t%s.SelectChangeNotifyRemove( *this);\n", context.QueryVariant( component.GetName()).c_str());
	}

	// 下拉框滚动条图片
	CCodeScrollBar scb;
	if ( const CComponent* pvscb = component.FindChild( COMTYPE_VSCROLLBAR))
	{
		CStringCodeOut tmpDec;
		tmpDec.Print( "\t\tGXWINDOW::CScrollBar& vscb = *( %s.GetScrollPanel()->GetVerticalScrollBar());\n", context.QueryVariant( component.GetName()).c_str());
		//tmpDec.Print( "\t\tvscb.SetSize( %s, %s);\n", pvscb->QueryPropertyValue( "w").c_str(), component.QueryPropertyValue( "listboxh").c_str());
		//scb.CodeOutScrollBarSetup( "vscb", *pvscb, tmpDec.GetString(), initialize, context, NULL, component.QueryPropertyValue( "h").c_str());
		scb.CodeOutScrollBarSetup( "vscb", *pvscb, tmpDec.GetString(), initialize, context, NULL, component.QueryPropertyValue( "listboxh").c_str());
	}

	if ( const CComponent* phscb = component.FindChild( COMTYPE_HSCROLLBAR))
	{
		CStringCodeOut tmpDec;
		tmpDec.Print( "\t\tGXWINDOW::CScrollBar& hscb = *( %s.GetScrollPanel()->GetHorizontalScrollBar());\n", context.QueryVariant( component.GetName()).c_str());
		//tmpDec.Print( "\t\thscb.SetSize( %s, %s);\n", component.QueryPropertyValue( "listboxw").c_str(), phscb->QueryPropertyValue( "h").c_str());
		//scb.CodeOutScrollBarSetup( "hscb", *phscb, tmpDec.GetString(), initialize, context, component.QueryPropertyValue( "w").c_str(), NULL);
		scb.CodeOutScrollBarSetup( "hscb", *phscb, tmpDec.GetString(), initialize, context, component.QueryPropertyValue( "listboxw").c_str(), NULL);
	}

	// 其他组合框属性
	CCodeListBox listbox;
	listbox.SetupListDataModule( component, initialize, context);
	initialize.Print( "\t%s.SetListDataModule( %s);\n", context.QueryVariant( component.GetName()).c_str(), context.QueryVariant( std::string( component.GetName()) + "_ldm").c_str());
	if ( !component.QueryPropertyValue( "lineheight").empty() && _stricmp( "0", component.QueryPropertyValue( "lineheight").c_str()) != 0)
		initialize.Print( "\t%s.SetClientComponentHeight( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "lineheight").c_str());
	if ( !component.QueryPropertyValue( "btnwidth").empty())
		initialize.Print( "\t%s.SetButtonWidth( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "btnwidth").c_str());
	if ( !component.QueryPropertyValue( "listboxw").empty() && !component.QueryPropertyValue( "listboxh").empty())
		initialize.Print( "\t%s.SetListBoxSize( %s, %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "listboxw").c_str(), component.QueryPropertyValue( "listboxh").c_str());
	if ( !component.QueryPropertyValue( "border").empty())
		initialize.Print( "\t%s.SetBorderWidth( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "border").c_str());

	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
	{
		notifyadd.Print( "\t%s.SelectChangeNotifyAdd( *this);\n", context.QueryVariant( component.GetName()).c_str());
		release.Print( "\t%s.SelectChangeNotifyRemove( *this);\n", context.QueryVariant( component.GetName()).c_str());
	}

	return true;
}

bool CCodeComboBox::CodeOutResponsedFuncDefine(const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) != 0)
		return false;

	if ( context.GetSplitedNotifyClass().empty())
		codeouticpp.Print( "void %s::On%sComboBox%sSelectChange( int n)\n{\n}\n\n", context.QueryVariant( "class").c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
	else
	{
		const std::vector<std::string>& splitedset = context.GetSplitedNotifyClass();
		codeoutucpp.Print( "void %s::On%sComboBox%sSelectChange( int n)\n{\n}\n\n", splitedset[splitedset.size() - 1].c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
	}
	return true;
}

bool CCodeComboBox::CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp,  CContext& context)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) != 0)
		return false;

	std::string tmp = "";
	CCodeOut* pCodeOut = &codeoutcpp;
	if ( context.GetSplitedNotifyClass().empty())
	{
		tmp = "\t";
		pCodeOut = &codeouth;
	}

	pCodeOut->Print( "%svoid On%sComboBox%sSelectChange( int n);\n", tmp.c_str(), context.QueryVariant( "framename").c_str(), component.GetName());

	return true;
}

void CCodeComboBox::CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) != 0)
		return;

	CodeOut.Print( "\t%sif( %s.IsSameComponent( lb) )\n\t\t%sOn%sComboBox%sSelectChange( n);\n", bGenElse ? "else " : "", 
		context.QueryVariant( component.GetName()).c_str(), context.GetSplitedNotifyClass().empty() ? "" : "m_scene->", 
		context.QueryVariant( "framename").c_str(), component.GetName());
}

bool CCodeComboBox::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	CCodeListBox listbox;
	return listbox.CodeOutFreeTextDraw( component, freetext, context);
}

void CCodeComboBox::SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	CCodeListBox listbox;
	listbox.SetAndGetProperty( component, CodeOut, context);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTable::CCodeTable() {}
CCodeTable::~CCodeTable() {}

bool CCodeTable::CodeOutDeclaration( const CComponent& component, CCodeOut& declaration, CContext& context)
{
	if ( !CCodeBaseComponent::CodeOutDeclaration( component, declaration, context))
		return false;

	DeclarationTableDataModule( component, declaration, context);
	
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
		context.PushNotifyVariant( COMTYPE_TABLE, &component);

	if ( _stricmp( "true", component.QueryPropertyValue( "onheadclick").c_str()) == 0)
		context.PushNotifyVariant( COMTYPE_TABLEHEADER, &component);

	return true;
}

void CCodeTable::DeclarationTableDataModule( const CComponent& component, CCodeOut& declaration, CContext& context)
{
	std::string ldmtype = component.QueryPropertyValue( "datatype");
	std::string ldmvariant = "m_tdm";
	ldmvariant += component.GetName();

	if ( _stricmp( COMTYPE_LDT_TEXT, ldmtype.c_str()) == 0 || ldmtype.empty())
	{
		ldmtype = "GXWINDOW::CTextTableDataModule";
		ldmvariant = "m_ttdm";
		ldmvariant += component.GetName();
	}
	else if ( _stricmp( COMTYPE_LDT_IMG, ldmtype.c_str()) == 0)
	{
		ldmtype = "GXWINDOW::CImageTextTableDataModule";
		ldmvariant = "m_ittdm";
		ldmvariant += component.GetName();
	}

	declaration.Print( "\t%s %s;\n", ldmtype.c_str(), ldmvariant.c_str());

	context.PushVariant( std::string( component.GetName()) + "_tdm", ldmvariant);
	context.PushVariant( std::string( component.GetName()) + "_tdmtype", ldmtype);
}

void CCodeTable::SetupTableDataModule( const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	std::string tdmvariant = context.QueryVariant( std::string( component.GetName()) + "_tdm");

	

	if ( !component.QueryPropertyValue( "b1").empty())
		CodeOut.Print( "\t%s.SetBackMouseOnColor( GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "b1")).c_str());
	if ( !component.QueryPropertyValue( "b3").empty())
		CodeOut.Print( "\t%s.SetBackSelectedColor( GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "b3")).c_str());


	if ( context.IsTextFormat() && !component.QueryPropertyValue( "i").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "i")); 
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
			CodeOut.Print( "\t%s.SetTextFont( %d);\n", tdmvariant.c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				CodeOut.Print( "\t%s.SetTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_SELECT);
			if ( it != tf.textStateMap.end())
				CodeOut.Print( "\t%s.SetTextSelectedColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			it = tf.textStateMap.find( TSF_MOUSEON);
			if ( it != tf.textStateMap.end())
				CodeOut.Print( "\t%s.SetTextMouseOnColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}
	else
	{
		if ( !component.QueryPropertyValue( "i").empty())
			CodeOut.Print( "\t%s.SetTextFont( %s);\n", tdmvariant.c_str(), component.QueryPropertyValue( "i").c_str());
		if ( !component.QueryPropertyValue( "c1").empty())
			CodeOut.Print( "\t%s.SetTextColor( GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "c1")).c_str());
		if ( !component.QueryPropertyValue( "c2").empty())
			CodeOut.Print( "\t%s.SetTextMouseOnColor( GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "c2")).c_str());
		if ( !component.QueryPropertyValue( "c3").empty())
			CodeOut.Print( "\t%s.SetTextSelectedColor( GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), ARGB2RGBA( component.QueryPropertyValue( "c3")).c_str());
	}

	if ( context.IsTextFormat() && !component.QueryPropertyValue( "headfont").empty())
	{
		const TEXTFORMAT* ptf = context.GetTextFormatMap( component.QueryPropertyValue( "headfont")); 
		if ( ptf)
		{
			const TEXTFORMAT& tf = *ptf;
			CodeOut.Print( "\t%s.SetTableHeaderFont( %d);\n", tdmvariant.c_str(), tf.nFontType);

			CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
			if ( it != tf.textStateMap.end())
				CodeOut.Print( "\t%s.SetTableHeaderColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", tdmvariant.c_str(), it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());
		}
	}

	ComponentPointerList columns;
	CStringCodeOut tmpCodeOut;
	component.FindComponent( COMTYPE_TABLECOLUMN, columns);
	int i = 0;
	bool bDragWidth = _stricmp( "true", component.QueryPropertyValue( "dragwidth").c_str()) == 0;
	for ( ComponentPointerList::iterator it = columns.begin(); it != columns.end(); ++ it, ++ i)
	{
		const CComponent& col = *(*it);
		if ( const CComponent* pimg = col.FindChild( COMTYPE_BGIMAGE))
		{
			CodeOut.Print( "\t%s.InsertColumn( L\"\");\n", tdmvariant.c_str());
			tmpCodeOut.Print( "\t%s.GetTableHeader().SetHeaderCellImage( %d, GXWINDOW::CImageParams( %s, %s, %s, GXWINDOW::MakeColor( %s)), %s, %s);\n", 
							context.QueryVariant( component.GetName()).c_str(), i, context.QueryImageRes( pimg->QueryPropertyValue( "f")).c_str(),
							pimg->QueryPropertyValue( "x").c_str(), pimg->QueryPropertyValue( "y").c_str(), ARGB2RGBA( col.QueryPropertyValue( "b")).c_str(),
							pimg->QueryPropertyValue( "w").c_str(), pimg->QueryPropertyValue( "h").c_str());
		}
		else
			CodeOut.Print( "\t%s.InsertColumn( L\"%s\");\n", tdmvariant.c_str(), col.QueryPropertyValue( "t").c_str());
		if ( !bDragWidth)
			tmpCodeOut.Print( "\t%s.GetTableHeader().GetHeaderColumn( %d).bSizable = false;\n", context.QueryVariant( component.GetName()).c_str(), i);
		if ( !col.QueryPropertyValue( "w").empty())
			tmpCodeOut.Print( "\t%s.GetTableHeader().GetHeaderColumn( %d).nWidth = %s;\n", context.QueryVariant( component.GetName()).c_str(), i, col.QueryPropertyValue( "w").c_str());
	}
	CodeOut.Print( "\t%s.FireDataChanged();\n", tdmvariant.c_str());
	CodeOut.Print( tmpCodeOut.GetString().c_str());
	CodeOut.Print( "\t%s.GetTableHeader().FireTableHeaderColumnSizeChanged();\n", context.QueryVariant( component.GetName()).c_str());
}

bool CCodeTable::CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( _stricmp( "Content", component.GetParentType()) != 0)
		return false;

	const char* tmp_type = g_CodeGeneratePropertyMap.GetType( component.GetType());
	if ( !tmp_type)
		return false;

	initialize.Print( "\t%s.SetPos( %s, %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "x").c_str(), component.QueryPropertyValue( "y").c_str());

	if ( component.FindChild( COMTYPE_BGIMAGE) )
		CodeOutBackGround( context.QueryVariant( component.GetName()).c_str(), component, initialize, context);
	else if ( !component.QueryPropertyValue( "b").empty())
		initialize.Print( "\t%s.SetBackGroundStyleColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "b")).c_str());

	initialize.Print( "\t%s.SetTableDataModule( %s);\n", context.QueryVariant( component.GetName()).c_str(), context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str());
	SetupTableDataModule( component, initialize, context);

	if ( !component.QueryPropertyValue( "headheight").empty())
	{
		std::string hh = "1";
		if ( 0 != _stricmp( "0", component.QueryPropertyValue( "headheight").c_str()))
			hh = component.QueryPropertyValue( "headheight");
		initialize.Print( "\t%s.SetTableHeaderHeight( %s);\n", context.QueryVariant( component.GetName()).c_str(), hh.c_str());
	}
	if ( !component.QueryPropertyValue( "lineheight").empty())
		initialize.Print( "\t%s.SetRowHeight( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "lineheight").c_str());

	bool bUseImgGrid = false;
	if ( const CComponent* pc = component.FindChild( COMTYPE_TABLE_HEADER_CELL))
	{
		bUseImgGrid = true;
		CStringCodeOut dec;
		dec.Print( "GXWINDOW::CImagesFill& bgf = %s.GetHeaderGridImagesFill();", context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str());
		dec.Print( "\n\t\t%s.SetGridStyle( GXWINDOW::CTableDataModule::GRID_IMAGE);", context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str());
		CodeOutBackGround( "headerimg", component, initialize, context, COMTYPE_TABLE_HEADER_CELL, dec.GetString().c_str(), pc->QueryPropertyValue( "w").c_str(), pc->QueryPropertyValue( "h").c_str());
	}
	if ( const CComponent* pc = component.FindChild( COMTYPE_TABLE_CELL))
	{
		bUseImgGrid = true;
		CStringCodeOut dec;
		dec.Print( "GXWINDOW::CImagesFill& bgf = %s.GetCellGridImagesFill();", context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str());
		dec.Print( "\n\t\t%s.SetGridStyle( GXWINDOW::CTableDataModule::GRID_IMAGE);", context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str());
		CodeOutBackGround( "headerimg", component, initialize, context, COMTYPE_TABLE_CELL, dec.GetString().c_str(), pc->QueryPropertyValue( "w").c_str(), pc->QueryPropertyValue( "h").c_str());
	}

	if ( !bUseImgGrid)
	{
		initialize.Print( "\t%s.SetGridStyle( GXWINDOW::CTableDataModule::GRID_LINE);\n", context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str());
		initialize.Print( "\t%s.SetGridColor( GXWINDOW::MakeColor( %s));\n",context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str(), ARGB2RGBA( component.QueryPropertyValue( "gridcolor")).c_str());
	}

	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
	{
		notifyadd.Print( "\t%s.TableSelectChangeNotifyAdd( *this);\n", context.QueryVariant( component.GetName()).c_str());
		release.Print( "\t%s.TableSelectChangeNotifyRemove( *this);\n", context.QueryVariant( component.GetName()).c_str());
	}
	if ( _stricmp( "true", component.QueryPropertyValue( "onheadclick").c_str()) == 0)
	{
		notifyadd.Print( "\t%s.GetTableHeader().ComponentMouseEventNotifyAdd( *this);\n", context.QueryVariant( component.GetName()).c_str());
		release.Print( "\t%s.GetTableHeader().ComponentMouseEventNotifyRemove( *this);\n", context.QueryVariant( component.GetName()).c_str());
	}
	return true;
}

bool CCodeTable::CodeOutResponsedFuncDefine( const CComponent& component, CCodeOut& codeouticpp, CCodeOut& codeoutucpp, CContext& context)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
	{
		if ( context.GetSplitedNotifyClass().empty())
			codeouticpp.Print( "void %s::On%sTable%sSelectChange( int n, int o)\n{\n}\n\n", context.QueryVariant( "class").c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
		else
		{
			const std::vector<std::string>& splitedset = context.GetSplitedNotifyClass();
			codeoutucpp.Print( "void %s::On%sTable%sSelectChange( int n, int o)\n{\n}\n\n", splitedset[splitedset.size() - 1].c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
		}
	}

	if ( _stricmp( "true", component.QueryPropertyValue( "onheadclick").c_str()) == 0)
	{
		if ( context.GetSplitedNotifyClass().empty())
			codeouticpp.Print( "void %s::On%sTable%sHeaderClick( int col)\n{\n}\n\n", context.QueryVariant( "class").c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
		else
		{
			const std::vector<std::string>& splitedset = context.GetSplitedNotifyClass();
			codeoutucpp.Print( "void %s::On%sTable%sHeaderClick( int col)\n{\n}\n\n", splitedset[splitedset.size() - 1].c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
		}
	}

	return true;
}

bool CCodeTable::CodeOutResponsedFuncDeclaration( const CComponent& component, CCodeOut& codeouth, CCodeOut& codeoutcpp, CContext& context)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
	{
		std::string tmp = "";
		CCodeOut* pCodeOut = &codeoutcpp;
		if ( context.GetSplitedNotifyClass().empty())
		{
			tmp = "\t";
			pCodeOut = &codeouth;
		}

		pCodeOut->Print( "%svoid On%sTable%sSelectChange( int n, int o);\n", tmp.c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
	}

	if ( _stricmp( "true", component.QueryPropertyValue( "onheadclick").c_str()) == 0)
	{
		std::string tmp = "";
		CCodeOut* pCodeOut = &codeoutcpp;
		if ( context.GetSplitedNotifyClass().empty())
		{
			tmp = "\t";
			pCodeOut = &codeouth;
		}

		pCodeOut->Print( "%svoid On%sTable%sHeaderClick( int col);\n", tmp.c_str(), context.QueryVariant( "framename").c_str(), component.GetName());
	}

	return true;
}

void CCodeTable::CodeOutCallOnResponsedFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onselchange").c_str()) == 0)
	{
		CodeOut.Print( "\t%sif( %s.IsSameComponent( t) )\n\t\t%sOn%sTable%sSelectChange( n, o);\n", bGenElse ? "else " : "", 
			context.QueryVariant( component.GetName()).c_str(), context.GetSplitedNotifyClass().empty() ? "" : "m_scene->", 
			context.QueryVariant( "framename").c_str(), component.GetName());
	}
}

void CCodeTable::CodeOutCallOnHeaderClickFunc( const CComponent& component, CCodeOut& CodeOut, CContext& context, bool bGenElse)
{
	if ( _stricmp( "true", component.QueryPropertyValue( "onheadclick").c_str()) == 0)
	{
		CodeOut.Print( "\t%sif ( %s.GetTableHeader().IsSameComponent( &c))\n", bGenElse ? "else " : "", context.QueryVariant( component.GetName()).c_str());
		CodeOut.Print( "\t\t%sOn%sTable%sHeaderClick( %s.GetTableHeader().ColumnIndexFromPoint( x, y));\n", context.GetSplitedNotifyClass().empty() ? "" : "m_scene->",
			context.QueryVariant( "framename").c_str(), component.GetName(), context.QueryVariant( component.GetName()).c_str());
	}
}

bool CCodeTable::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	freetext.Print( "\t\t%s.FreeAllTextDraw();\n", context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str());

	return true;
}

void CCodeTable::SetAndGetProperty( const CComponent& component, CCodeOut& CodeOut, CContext& context)
{
	CodeOut.Print( "\t%s& Get%sTableDataModule()\n\t{\n", context.QueryVariant( std::string( component.GetName()) + "_tdmtype").c_str(), context.QueryVariant( component.GetName()).c_str());
	CodeOut.Print( "\t\treturn %s;\n\t}\n", context.QueryVariant( std::string( component.GetName()) + "_tdm").c_str());
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeSliderBar::CCodeSliderBar() {}
CCodeSliderBar::~CCodeSliderBar() {}

bool CCodeSliderBar::CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CCodeScrollBar scb;
	scb.CodeOutScrollBarSetup( context.QueryVariant( component.GetName()), component, "", initialize, context);

	bool bVertical = _stricmp( "true", component.QueryPropertyValue( "vertical").c_str()) == 0;
	if ( bVertical)
		initialize.Print( "\t%s.SetVertical();\n", context.QueryVariant( component.GetName()).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_SCBRUBNORMAL))
		initialize.Print( "\t%s.SetRubSize( %s);\n", context.QueryVariant( component.GetName()).c_str(), bVertical ? pc->QueryPropertyValue( "h").c_str() : pc->QueryPropertyValue( "w").c_str());

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeScrollBar::CCodeScrollBar() {}
CCodeScrollBar::~CCodeScrollBar() {}

bool CCodeScrollBar::CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CodeOutScrollBarSetup( context.QueryVariant( component.GetName()), component, "", initialize, context);

	return true;
}

void CCodeScrollBar::CodeOutScrollBarSetup( const std::string& scbvariant, const CComponent& scbcomponent, const std::string& decinscope, CCodeOut& CodeOut, CContext& context, const char* scbwidth, const char* scbheight)
{
	CStringCodeOut tmpCodeOut;

	tmpCodeOut.Print( "\t\t%s.SetSize( %s, %s);\n", scbvariant.c_str(), NULL == scbwidth ? scbcomponent.QueryPropertyValue( "w").c_str() : scbwidth, NULL == scbheight ? scbcomponent.QueryPropertyValue( "h").c_str() : scbheight);
	tmpCodeOut.Print( "\t\t%s.SetAdaptMode( %s);\n", scbvariant.c_str(), _stricmp( "true", scbcomponent.QueryPropertyValue( "autoblock").c_str()) == 0 ? "true" : "false");
	bool bVertical = _stricmp( "true", scbcomponent.QueryPropertyValue( "vertical").c_str()) == 0;
	if ( bVertical)
		tmpCodeOut.Print( "\t\t%s.SetVertical();\n", scbvariant.c_str());
	if ( const CComponent* pc = scbcomponent.FindChild( COMTYPE_SCBUPNORMAL))
		tmpCodeOut.Print( "\t\t%s.SetButtonSize( %s);\n", scbvariant.c_str(), bVertical ? pc->QueryPropertyValue( "h").c_str() : pc->QueryPropertyValue( "w").c_str());
	if ( const CComponent* pc = scbcomponent.FindChild( COMTYPE_SCBRUBNORMAL))
		tmpCodeOut.Print( "\t\t%s.SetRubSize( %s);\n", scbvariant.c_str(), bVertical ? pc->QueryPropertyValue( "h").c_str() : pc->QueryPropertyValue( "w").c_str());


	{	// 上/左按钮
		const CComponent* btn1 = scbcomponent.FindChild( COMTYPE_SCBUPNORMAL);
		const CComponent* btn2 = scbcomponent.FindChild( COMTYPE_SCBUPON);
		const CComponent* btn3 = scbcomponent.FindChild( COMTYPE_SCBUPDOWN);
		const CComponent* btn4 = scbcomponent.FindChild( COMTYPE_SCBUPDISABLE);

		CStringCodeOut normal, mouseon, mousedown, disable;
		if ( btn1)
			normal.Print( "%s, %s, %s", context.QueryImageRes( btn1->QueryPropertyValue( "f")).c_str(), btn1->QueryPropertyValue( "x").c_str(), btn1->QueryPropertyValue( "y").c_str());
		if ( btn2)
			mouseon.Print( "%s, %s, %s", context.QueryImageRes( btn2->QueryPropertyValue( "f")).c_str(), btn2->QueryPropertyValue( "x").c_str(), btn2->QueryPropertyValue( "y").c_str());
		if ( btn3)
			mousedown.Print( "%s, %s, %s", context.QueryImageRes( btn3->QueryPropertyValue( "f")).c_str(), btn3->QueryPropertyValue( "x").c_str(), btn3->QueryPropertyValue( "y").c_str());
		if ( btn4)
			disable.Print( "%s, %s, %s", context.QueryImageRes( btn4->QueryPropertyValue( "f")).c_str(), btn4->QueryPropertyValue( "x").c_str(), btn4->QueryPropertyValue( "y").c_str());

		if ( btn4)
			tmpCodeOut.Print( "\t\t%s.SetTopLeftButtonImage( GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s));\n", scbvariant.c_str(), normal.GetString().c_str(), mouseon.GetString().c_str(), mousedown.GetString().c_str(), disable.GetString().c_str());
		else if ( btn1 || btn2 || btn3)
			tmpCodeOut.Print( "\t\t%s.SetTopLeftButtonImage( GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s));\n", scbvariant.c_str(), normal.GetString().c_str(), mouseon.GetString().c_str(), mousedown.GetString().c_str());
	}


	{	// 下/右按钮
		const CComponent* btn1 = scbcomponent.FindChild( COMTYPE_SCBDOWNNORMAL);
		const CComponent* btn2 = scbcomponent.FindChild( COMTYPE_SCBDOWNON);
		const CComponent* btn3 = scbcomponent.FindChild( COMTYPE_SCBDOWNDOWN);
		const CComponent* btn4 = scbcomponent.FindChild( COMTYPE_SCBDOWNDISABLE);

		CStringCodeOut normal, mouseon, mousedown, disable;
		if ( btn1)
			normal.Print( "%s, %s, %s", context.QueryImageRes( btn1->QueryPropertyValue( "f")).c_str(), btn1->QueryPropertyValue( "x").c_str(), btn1->QueryPropertyValue( "y").c_str());
		if ( btn2)
			mouseon.Print( "%s, %s, %s", context.QueryImageRes( btn2->QueryPropertyValue( "f")).c_str(), btn2->QueryPropertyValue( "x").c_str(), btn2->QueryPropertyValue( "y").c_str());
		if ( btn3)
			mousedown.Print( "%s, %s, %s", context.QueryImageRes( btn3->QueryPropertyValue( "f")).c_str(), btn3->QueryPropertyValue( "x").c_str(), btn3->QueryPropertyValue( "y").c_str());
		if ( btn4)
			disable.Print( "%s, %s, %s", context.QueryImageRes( btn4->QueryPropertyValue( "f")).c_str(), btn4->QueryPropertyValue( "x").c_str(), btn4->QueryPropertyValue( "y").c_str());

		if ( btn4)
			tmpCodeOut.Print( "\t\t%s.SetRightBottomButtonImage( GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s));\n", scbvariant.c_str(), normal.GetString().c_str(), mouseon.GetString().c_str(), mousedown.GetString().c_str(), disable.GetString().c_str());
		else if ( btn1 || btn2 || btn3)
			tmpCodeOut.Print( "\t\t%s.SetRightBottomButtonImage( GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s), GXWINDOW::CImageParams( %s));\n", scbvariant.c_str(), normal.GetString().c_str(), mouseon.GetString().c_str(), mousedown.GetString().c_str());
	}

	{	// 滚动槽
		if ( const CComponent* pc = scbcomponent.FindChild( COMTYPE_SCBSLOT))
			CodeOutBackGround( scbvariant.c_str(), scbcomponent, tmpCodeOut, context, COMTYPE_SCBSLOT, NULL, scbwidth, scbheight, false);
		else if ( !scbcomponent.QueryPropertyValue( "b").empty())
			tmpCodeOut.Print( "\t%s.SetBackGroundStyleColor( GXWINDOW::MakeColor( %s));\n", scbvariant.c_str(), ARGB2RGBA( scbcomponent.QueryPropertyValue( "b")).c_str());
		//tmpCodeOut.Print ( CodeOutImageParam( *pc, scbcomponent, CodeOut, scbvariant.c_str(), "SetSliderScaleStyleImage",  context).c_str());
	}

	{	// 滚动块

		//const CComponent* prub1 = scbcomponent.FindChild( COMTYPE_SCBRUBNORMAL);
		//const CComponent* prub2 = scbcomponent.FindChild( COMTYPE_SCBRUBON);
		//const CComponent* prub3 = scbcomponent.FindChild( COMTYPE_SCBRUBDOWN);
		//if ( NULL != prub1 && NULL != prub2 && NULL != prub3)
		//{
		//	tmpCodeOut.Print( "\t\t%s.SetSliderRubStyleImage( GXWINDOW::CImageParams( %s, %s, %s), GXWINDOW::CImageParams( %s, %s, %s), GXWINDOW::CImageParams( %s, %s, %s));\n",
		//		scbvariant.c_str(), context.QueryImageRes( prub1->QueryPropertyValue( "f"), CodeOut).c_str(), prub1->QueryPropertyValue( "x").c_str(), prub1->QueryPropertyValue( "y").c_str(),
		//		context.QueryImageRes( prub2->QueryPropertyValue( "f"), CodeOut).c_str(), prub2->QueryPropertyValue( "x").c_str(), prub2->QueryPropertyValue( "y").c_str(),
		//		context.QueryImageRes( prub3->QueryPropertyValue( "f"), CodeOut).c_str(), prub3->QueryPropertyValue( "x").c_str(), prub3->QueryPropertyValue( "y").c_str());
		//}
		tmpCodeOut.Print( "\t\t%s.SetSliderRubStyle( GXWINDOW::SRS_IMAGE);\n", scbvariant.c_str());
		if ( const CComponent* prub = scbcomponent.FindChild( COMTYPE_SCBRUBNORMAL))
		{
			CStringCodeOut tmp;
			tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetRubTilesNormal();", scbvariant.c_str());
			CodeOutBackGround( scbvariant.c_str(), scbcomponent, tmpCodeOut, context, COMTYPE_SCBRUBNORMAL, tmp.GetString().c_str(), prub->QueryPropertyValue( "w").c_str(), prub->QueryPropertyValue( "h").c_str(), true);
		}
		if ( const CComponent* prub = scbcomponent.FindChild( COMTYPE_SCBRUBON))
		{
			CStringCodeOut tmp;
			tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetRubTilesMouseOn();", scbvariant.c_str());
			CodeOutBackGround( scbvariant.c_str(), scbcomponent, tmpCodeOut, context, COMTYPE_SCBRUBON, tmp.GetString().c_str(), prub->QueryPropertyValue( "w").c_str(), prub->QueryPropertyValue( "h").c_str(), true);
		}
		if ( const CComponent* prub = scbcomponent.FindChild( COMTYPE_SCBRUBDOWN))
		{
			CStringCodeOut tmp;
			tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetRubTilesMoving();", scbvariant.c_str());
			CodeOutBackGround( scbvariant.c_str(), scbcomponent, tmpCodeOut, context, COMTYPE_SCBRUBDOWN, tmp.GetString().c_str(), prub->QueryPropertyValue( "w").c_str(), prub->QueryPropertyValue( "h").c_str(), true);
		}
	}

	CodeOut.Print( "\t{\n");
	CodeOut.Print( decinscope.c_str());
	CodeOut.Print( tmpCodeOut.GetString().c_str());
	CodeOut.Print( "\t}\n");
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeProgressBar::CCodeProgressBar() {}
CCodeProgressBar::~CCodeProgressBar() {}

bool CCodeProgressBar::CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( const CComponent* pc = component.FindChild( COMTYPE_BGIMAGE))
	{
		//if ( _stricmp( "0", pc->QueryPropertyValue( "w0").c_str()) == 0)
		//	initialize.Print ( "\t{\n%s\t}\n", CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetProgressBarStyleImage",  context).c_str());
		//else
		//{
		//	CStringCodeOut tmp;
		//	tmp.Print( ", %s, %s", pc->QueryPropertyValue( "w").c_str(), pc->QueryPropertyValue( "h").c_str());
		//	initialize.Print ( "\t{\n%s\t}\n", CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetProgressBarStyleSimpleTile",  context, tmp.GetString().c_str()).c_str());
		//}
		
		
		if ( _stricmp( "0", pc->QueryPropertyValue( "w0").c_str()) == 0 && _stricmp( "0", pc->QueryPropertyValue( "w1").c_str()) == 0 && _stricmp( "0", pc->QueryPropertyValue( "h0").c_str()) == 0 && _stricmp( "0", pc->QueryPropertyValue( "h1").c_str()) == 0)
			initialize.Print ( "\t{\n%s\t}\n", CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetProgressBarStyleImage",  context).c_str());
		else if ( _stricmp( "0", pc->QueryPropertyValue( "w0").c_str()) == 0 && _stricmp( "0", pc->QueryPropertyValue( "w1").c_str()) == 0)
		{
			CStringCodeOut tmp;
			tmp.Print( ", %s, %s", pc->QueryPropertyValue( "w").c_str(), pc->QueryPropertyValue( "h").c_str());
			initialize.Print ( "\t{\n%s\t}\n", CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetProgressBarStyleSimpleTile",  context, tmp.GetString().c_str()).c_str());
		}
		else // 支持九宫格
		{
			CStringCodeOut tmp;
			tmp.Print( "%s.SetProgressBarStyle( GXWINDOW::PBS_TILE);\n\t\tGXWINDOW::CImagesFill& bgf = %s.GetProgressBarImageFill();", context.QueryVariant( component.GetName()).c_str(), context.QueryVariant( component.GetName()).c_str());
			CodeOutBackGround( component.GetName(), component, initialize, context, COMTYPE_BGIMAGE, tmp.GetString().c_str(), component.QueryPropertyValue( "w").c_str(), component.QueryPropertyValue( "h").c_str(), true);

		}
	}
	else
		initialize.Print( "\t%s.SetProgressBarStyleColor( GXWINDOW::MakeColor( %s));\n", context.QueryVariant( component.GetName()).c_str(), ARGB2RGBA( component.QueryPropertyValue( "b")).c_str());

	return true;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeTab::CCodeTab() {}
CCodeTab::~CCodeTab() {}

bool CCodeTab::CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeBKBaseComponent::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	ComponentPointerList tabbtns, tabpanels;
	component.FindChildren( COMTYPE_BUTTONTEXT, tabbtns);
	if ( const CComponent* pc = component.FindChild( COMTYPE_CONTENT) )
		pc->FindChildren( "", tabpanels);

	if ( tabbtns.size() != tabpanels.size())
		return false;

	ComponentPointerList::iterator it_btn = tabbtns.begin();
	ComponentPointerList::iterator it_panel = tabpanels.begin();
	CStringCodeOut CodeOut;
	int nIndex = 0;
	for ( ; it_btn != tabbtns.end() && it_panel != tabpanels.end(); ++ it_btn, ++ it_panel)
	{
		const CComponent* pbtn = *it_btn;
		CStringCodeOut t_getbtn;
		t_getbtn.Print( "( *%s.GetRadioButton( %d))", context.QueryVariant( component.GetName()).c_str(), nIndex);

		CodeOut.Print( "\t%s.InsetComponent( %s);\n", context.QueryVariant( component.GetName()).c_str(), context.QueryVariant( (*it_panel)->GetName()).c_str());
		//CodeOut.Print( "\t%sprbtn = %s.GetRadioButton( %d);\n", nIndex == 0 ? "GXWINDOW::CAbstractRadioButton* " : "", context.QueryVariant( component.GetName()).c_str(), nIndex);
		CodeOut.Print( "\t%s.SetButtonBoxStyle( GXWINDOW::BBS_TILE);\n", t_getbtn.GetString().c_str());
		if ( const CComponent* pimg = pbtn->FindChild( COMTYPE_IMGNORMAL))
		{
			CStringCodeOut tmp;
			tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageNormal();", t_getbtn.GetString().c_str());
			CodeOutBackGround( t_getbtn.GetString().c_str(), *pbtn, CodeOut, context, COMTYPE_IMGNORMAL, tmp.GetString().c_str(), component.QueryPropertyValue( "btnwidth").c_str(), component.QueryPropertyValue( "btnheight").c_str());
		}

		if ( const CComponent* pimg = pbtn->FindChild( COMTYPE_IMGMOUSEON))
		{
			CStringCodeOut tmp;
			tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageMouseIn();", t_getbtn.GetString().c_str());
			CodeOutBackGround( t_getbtn.GetString().c_str(), *pbtn, CodeOut, context, COMTYPE_IMGMOUSEON, tmp.GetString().c_str(), component.QueryPropertyValue( "btnwidth").c_str(), component.QueryPropertyValue( "btnheight").c_str());
		}

		if ( const CComponent* pimg = pbtn->FindChild( COMTYPE_IMGDOWN))
		{
			CStringCodeOut tmp;
			tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageSelect();", t_getbtn.GetString().c_str());
			CodeOutBackGround( t_getbtn.GetString().c_str(), *pbtn, CodeOut, context, COMTYPE_IMGDOWN, tmp.GetString().c_str(), component.QueryPropertyValue( "btnwidth").c_str(), component.QueryPropertyValue( "btnheight").c_str());
		}

		if ( const CComponent* pimg = pbtn->FindChild( COMTYPE_IMGSELECTMOUSEON))
		{
			CStringCodeOut tmp;
			tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageMouseInSelect();", t_getbtn.GetString().c_str());
			CodeOutBackGround( t_getbtn.GetString().c_str(), *pbtn, CodeOut, context, COMTYPE_IMGSELECTMOUSEON, tmp.GetString().c_str(), component.QueryPropertyValue( "btnwidth").c_str(), component.QueryPropertyValue( "btnheight").c_str());
		}

		if ( const CComponent* pimg = pbtn->FindChild( COMTYPE_IMGDISABLE))
		{
			CStringCodeOut tmp;
			tmp.Print( "GXWINDOW::CImagesFill& bgf = %s.GetTileImageDisable();", t_getbtn.GetString().c_str());
			CodeOutBackGround( t_getbtn.GetString().c_str(), *pbtn, CodeOut, context, COMTYPE_IMGDISABLE, tmp.GetString().c_str(), component.QueryPropertyValue( "btnwidth").c_str(), component.QueryPropertyValue( "btnheight").c_str());
		}

		if ( !pbtn->QueryPropertyValue( "n").empty() && !pbtn->QueryPropertyValue( "i").empty() && context.IsTextFormat())
		{
			CodeOut.Print( "\t{\n");
			CodeOut.Print( "\t\tGXWINDOW::CTextImageRadioButton* prbtn = dynamic_cast<GXWINDOW::CTextImageRadioButton*>( %s.GetRadioButton( %d));\n", context.QueryVariant( component.GetName()).c_str(), nIndex);
			CodeOut.Print( "\t\tif ( prbtn)\n\t\t{\n");

			//if ( !pbtn->QueryPropertyValue( "n").empty())
			//	CodeOut.Print( "\t\t\tprbtn->SetComponentText( L\"%s\");\n", pbtn->QueryPropertyValue( "n").c_str());
			if ( !pbtn->QueryPropertyValue( "n").empty())
			{
				if ( !context.IsUseTextRes()) // 没指定字符串资源文件, 直接将字符串生成到代码中
					CodeOut.Print( "\t\t\tprbtn->SetComponentText( L\"%s\");\n", pbtn->QueryPropertyValue( "n").c_str());
				else	// 使用字符串资源xml文件, 代码中只生成资源id
				{
					char buf[64];
					sprintf( buf, "%s_btn%d", component.GetName(), nIndex);
					int textResId = context.LoadTextResID( pbtn->QueryPropertyValue( "n").c_str(), context.GetUIName(), buf);
					CodeOut.Print( "\t\t\tprbtn->SetComponentText( %d); // %d-%s\n", textResId, textResId, component.QueryPropertyValue( "t").c_str());
				}
			}

			const TEXTFORMAT* ptf = context.GetTextFormatMap( pbtn->QueryPropertyValue( "i"));
			if ( ptf)
			{
				const TEXTFORMAT& tf = *ptf;
				CodeOut.Print( "\t\t\tprbtn->SetComponentFont( %d);\n", tf.nFontType);

				CTextStateMap::const_iterator it = tf.textStateMap.find( TSF_NORMAL);
				if ( it != tf.textStateMap.end())
					CodeOut.Print( "\t\t\tprbtn->SetComponentTextColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

				it = tf.textStateMap.find( TSF_MOUSEON);
				if ( it != tf.textStateMap.end())
					CodeOut.Print( "\t\t\tprbtn->SetComponentTextMouseInColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

				it = tf.textStateMap.find( TSF_SELECT);
				if ( it != tf.textStateMap.end())
					CodeOut.Print( "\t\t\tprbtn->SetComponentTextSelectedColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

				it = tf.textStateMap.find( TSF_SELECT_MOUSEON);
				if ( it != tf.textStateMap.end())
					CodeOut.Print( "\t\t\tprbtn->SetComponentTextSelectedMouseInColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

				it = tf.textStateMap.find( TSF_DISABLE);
				if ( it != tf.textStateMap.end())
					CodeOut.Print( "\t\t\tprbtn->SetComponentTextDisableColor( GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s), %s, GXWINDOW::MakeColor( %s));\n", it->second.szColor.c_str(), GetGXWFontEffectType( it->second).c_str(), it->second.szEffectColor.c_str(), it->second.bGraudal ? "true" : "false", it->second.szColor2.c_str());

			}

			CodeOut.Print( "\t\t}\n");
			CodeOut.Print( "\t}\n");
		}

		nIndex++;
		
	}

	//initialize.Print( "\t{\n");
	if ( !component.QueryPropertyValue( "btnwidth").empty())
		initialize.Print( "\t%s.SetButtonWidth( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "btnwidth").c_str());
	if ( !component.QueryPropertyValue( "btnheight").empty())
		initialize.Print( "\t%s.SetButtonHeight( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "btnheight").c_str());
	if ( !component.QueryPropertyValue( "headsize").empty())
		initialize.Print( "\t%s.SetTitleSize( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "headsize").c_str());
	if ( !component.QueryPropertyValue( "btnborder").empty())
		initialize.Print( "\t%s.SetTitleOffset( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "btnborder").c_str());
	initialize.Print( "\n");
	initialize.Print( CodeOut.GetString().c_str());
	initialize.Print( "\n");
	if ( _stricmp( "true", component.QueryPropertyValue( "vertical").c_str()) == 0)
		initialize.Print( "\t%s.SetTitleVertical( true);\n", context.QueryVariant( component.GetName()).c_str());
	if ( _stricmp( "true", component.QueryPropertyValue( "reverse").c_str()) == 0)
		initialize.Print( "\t%s.SetTitleRightBottom( true);\n", context.QueryVariant( component.GetName()).c_str());
	//initialize.Print( "\t}\n");

	return true;
}

bool CCodeTab::CodeOutFreeTextDraw( const CComponent& component, CCodeOut& freetext, CContext& context)
{
	if ( _stricmp( context.QueryVariant( component.GetName()) .c_str(), "") != 0)
		freetext.Print( "\t\t%s.FreeAllTextDraw();\n", context.QueryVariant( component.GetName()) .c_str());
	return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCodeAppleButton::CCodeAppleButton()
{

}

CCodeAppleButton::~CCodeAppleButton()
{

}

bool CCodeAppleButton::CodeOutInitializeAndRelease(const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodeButton::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	CStringCodeOut tmpCodeOut;
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGNORMAL))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetSmallImageNormal",  context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGMOUSEON))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetSmallImageMouseOn", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGDOWN))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetSmallImageDown", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_IMGDISABLE))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetSmallImageDisable", context).c_str());

	if ( const CComponent* pc = component.FindChild( COMTYPE_APPLEBUTTON_BIGNORMAL))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetBigImageNormal",  context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_APPLEBUTTON_BIGMOUSEON))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetBigImageMouseOn", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_APPLEBUTTON_BIGDOWN))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetBigImageDown", context).c_str());
	if ( const CComponent* pc = component.FindChild( COMTYPE_APPLEBUTTON_BIGDISABLE))
		tmpCodeOut.Print ( CodeOutImageParam( *pc, component, initialize, context.QueryVariant( component.GetName()), "SetBigImageDisable", context).c_str());

	initialize.Print( "\t{\n");
	initialize.Print( tmpCodeOut.GetString().c_str());
	initialize.Print( "\t}\n");

	return true;
}

CCodeAppleMenu::CCodeAppleMenu()
{

}

CCodeAppleMenu::~CCodeAppleMenu()
{

}

bool CCodeAppleMenu::CodeOutInitializeAndRelease( const CComponent& component, CCodeOut& initialize, CCodeOut& notifyadd, CCodeOut& release, CContext& context)
{
	if ( !CCodePanel::CodeOutInitializeAndRelease( component, initialize, notifyadd, release, context))
		return false;

	if ( !component.QueryPropertyValue( "radius0").empty())
		initialize.Print( "\t%s.SetBaseRadius( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "radius0").c_str());

	if ( !component.QueryPropertyValue( "radius1").empty())
		initialize.Print( "\t%s.SetLargeRadius( %s);\n", context.QueryVariant( component.GetName()).c_str(), component.QueryPropertyValue( "radius1").c_str());

	initialize.Print( "\t%s.ResetButtonPosition();\n", context.QueryVariant( component.GetName()).c_str());

	return true;
}

void CCodeAppleMenu::AnalyseAppleMenu( CComponent& component)
{
	CComponent* pc = component.FindChild( COMTYPE_CONTENT);
	pc->SetType( "TempContent");
	pc->SetName( "TempContent");

	CComponent content;
	content.SetType( COMTYPE_CONTENT);
	component.GetChilderList().push_back( content);

	AnalyseAppleMenu( component, component, NULL);

	component.RemoveChild( "TempContent");
}

void CCodeAppleMenu::AnalyseAppleMenu( CComponent& menu, const CComponent& submenu, const char* parent)
{
	ComponentPointerList applebtns, subsubmenus;
	submenu.FindChildren( COMTYPE_BUTTONIMAGE, applebtns);
	if ( const CComponent* pc = submenu.FindChild( menu.IsSameComponent( submenu) ? "TempContent" : COMTYPE_CONTENT))
		pc->FindChildren( "", subsubmenus);

	if ( applebtns.size() != subsubmenus.size())
		return;

	ComponentPointerList::iterator it_btn = applebtns.begin();
	ComponentPointerList::iterator it_subsubmenu = subsubmenus.begin();
	int index = 0;
	for ( ; it_btn != applebtns.end() && it_subsubmenu != subsubmenus.end(); ++ it_btn, ++ it_subsubmenu)
	{
		CComponent btn;
		btn.Clone( *(*it_btn));
		btn.GetPropertyMap().Push( "ParentButton", NULL == parent ? "" : parent);
		char buffer[128];
		sprintf_s( buffer, "%sButton%d", submenu.GetName(), index);
		btn.SetName( buffer);
		btn.SetParentType( COMTYPE_CONTENT);
		btn.SetType( COMTYPE_APPLEBUTTON);
		btn.GetPropertyMap().Push( "onclick", "true");
		
		CComponent* pcontent = menu.FindChild( COMTYPE_CONTENT);
		pcontent->GetChilderList().push_back( btn);

		const CComponent* psubsubmenucontent = ( *it_subsubmenu)->FindChild( COMTYPE_CONTENT);
		if ( psubsubmenucontent)
		{
			if ( const CComponent* psubsubmenu = psubsubmenucontent->FindChild( COMTYPE_APPLEMENU))
				AnalyseAppleMenu( menu, *psubsubmenu, btn.GetName());
		}

		++ index;
	}
}