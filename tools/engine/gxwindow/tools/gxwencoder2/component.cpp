#include "stdafx.h"
#include "codeout.h"
#include "component.h"
#include "generatecode.h"
#include "codecomponent.h"
#include "gxwencoder.h"
#include "util.h"

#include <algorithm>


class CCoInitScope
{
public:
	CCoInitScope()
	{
		CoInitialize( NULL);
	}
	~CCoInitScope()
	{
		CoUninitialize();
	}
};



CComponent::CComponent()
{

}

CComponent::~CComponent()
{

}

void CComponent::SetType(const std::string& type)
{
	m_type = type;
}

void CComponent::SetName(const std::string& name)
{
	m_name = name;
}

void CComponent::SetParentType(const std::string& parent)
{
	m_parenttype = parent;
}

const char* CComponent::GetType() const
{
	return m_type.c_str();
}

const char* CComponent::GetName() const
{
	return m_name.c_str();
}

const char* CComponent::GetParentType() const
{
	return m_parenttype.c_str();
}

CPropertyMap& CComponent::GetPropertyMap()
{
	return m_properties;
}

ComponentList& CComponent::GetChilderList()
{
	return m_children;
}

std::string CComponent::QueryPropertyValue(const std::string& strProperty) const
{
	assert( !strProperty.empty());

	return m_properties.Find( strProperty);
}

void CComponent::Clear()
{
	m_properties.clear();
	m_children.clear();
	m_name.erase();
	m_type.erase();
}

const CComponent* CComponent::FindComponent(const std::string& strName) const
{
	if( 0 == strcmp( strName.c_str(), m_name.c_str()))
		return this;

	for( ComponentList::const_iterator it = m_children.begin(); it != m_children.end(); ++ it)
	{
		const CComponent* pc = (*it).FindComponent( strName);
		if( pc)
			return pc;
	}
	return NULL;
}

void CComponent::FindComponent( const std::string& strType, ComponentPointerList& components) const
{
	if( 0 == strcmp( strType.c_str(), m_type.c_str()))
		components.push_back( this);

	for( ComponentList::const_iterator it = m_children.begin(); it != m_children.end(); ++ it)
	{
		(*it).FindComponent( strType, components);
	}
}

void CComponent::FindChildren( const std::string& strType, ComponentPointerList& children) const
{
	for( ComponentList::const_iterator it = m_children.begin(); it != m_children.end(); ++ it)
	{
		if( strType.empty() || 0 == strcmp( strType.c_str(), (*it).GetType()))
			children.push_back( &(*it));
	}
}

const CComponent* CComponent::FindComponentByType( const std::string& strType) const
{
	if( 0 == strcmp( strType.c_str(), m_type.c_str()))
		return this;

	for( ComponentList::const_iterator it = m_children.begin(); it != m_children.end(); ++ it)
	{
		const CComponent* pc = (*it).FindComponentByType( strType);
		if( pc)
			return pc;
	}
	return NULL;
}

bool CComponent::HasType(const std::string& strType) const
{
	if( 0 == strcmp( strType.c_str(), m_type.c_str()))
		return true;
	
	for( ComponentList::const_iterator it = m_children.begin(); it != m_children.end(); ++ it)
	{
		if ( (*it).HasType( strType))
			return true;
	}

	return false;
}

const CComponent* CComponent::FindChild(const std::string& strType) const
{
	for( ComponentList::const_iterator it = m_children.begin(); it != m_children.end(); ++ it)
	{
		if ( _stricmp( strType.c_str(), (*it).GetType()) == 0 )
			return &(*it);
	}
	return NULL;
}

CComponent* CComponent::FindChild( const std::string& strType)
{
	for( ComponentList::iterator it = m_children.begin(); it != m_children.end(); ++ it)
	{
		if ( _stricmp( strType.c_str(), (*it).GetType()) == 0 )
			return &(*it);
	}
	return NULL;
}

void CComponent::RemoveChild( const std::string& name)
{
	for( ComponentList::iterator it = m_children.begin(); it != m_children.end(); ++ it)
	{
		if ( _stricmp( name.c_str(), (*it).GetName()) == 0 )
		{
			m_children.erase( it);
			return;
		}
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CContext::CContext() : m_bPakMode( false), m_bUseTextFormat( false), m_genmode( CGxWEncoder::eGenMode_Code)
{

}

CContext::~CContext()
{
	Clear();
}

void CContext::Clear()
{
	m_variant.clear();
	m_imageres.clear();
}

void CContext::PushVariant(const std::string& componentname, const std::string& variantname)
{
	m_variant[componentname] = variantname;
}

std::string CContext::QueryVariant(const std::string& componentname)
{
	CPropertyMap::const_iterator it = m_variant.find( componentname);
	if( it == m_variant.end())
	{
		return "";
	}
	return it->second;
}

std::string CContext::QueryVariant( const CComponent& component)
{
	CPropertyMap::const_iterator it = m_variant.find( component.GetName());
	if( it == m_variant.end())
	{
		CStringCodeOut tmp;
		// 由于已有部分代码投入使用, 所以这里强制修改两个类型的命名
		if ( 0 == _stricmp( component.GetType(), COMTYPE_TEXTIMGRADIOBUTTON))
			tmp.Print( "m_%s%s", COMTYPE_TEXTBUTTONRADIO, component.GetName());
		else if ( 0 == _stricmp( component.GetType(), COMTYPE_TEXTIMGCHECKBUTTON))
			tmp.Print( "m_%s%s", COMTYPE_TEXTBUTTONCHECK, component.GetName());
		else
			tmp.Print( "m_%s%s", component.GetType(), component.GetName());
		m_variant[ component.GetName()] = tmp.GetString();
		return tmp.GetString();
	}
	return it->second;
}

std::string CContext::QueryImageRes(const std::string& imagefilename)
{
	CPropertyMap::const_iterator it = m_imageres.find( imagefilename);
	if( it == m_imageres.end())
	{
		int count = ( int)m_imageres.size();
		CStringCodeOut tmp;
		tmp.Print( "nImageFileIndex%d", count + 1);
		m_imageres[imagefilename] = tmp.GetString();

		//regex::rpattern rep( std::wstring( L"\\"), std::wstring( L"/"), regex::NOCASE | regex::GLOBAL );
		//regex::subst_results rs;
		//std::wstring wtmp = str2wstr( imagefilename);
		//rep.substitute( wtmp, rs);
		std::string replaced = Replace( imagefilename, "\\", "/");

		if ( m_bPakMode)
		{
			size_t pos = replaced.find_last_of( '/');
			replaced = replaced.substr( pos + 1);
		}

		m_imagereg.Print( "\tint %s = GXWINDOW::CFileNameImages::GetInstance().RegisterFileName( L\"%s\");\n", tmp.GetString().c_str(), replaced.c_str());

		return tmp.GetString();
	}
	return it->second;
}

void CContext::PushNotifyVariant(const std::string& type, const CComponent* component)
{
	m_notifymap[type].push_back( component);
}

const ComponentPointerList& CContext::GetNotifyVariants( const std::string& type)
{
	NotifyVariantMap::const_iterator it = m_notifymap.find( type);
	if ( it == m_notifymap.end())
	{
		std::pair<NotifyVariantMap::const_iterator, bool> insert_it = m_notifymap.insert( std::make_pair( type, ComponentPointerList()));
		return insert_it.first->second;
	}
	return it->second;
}

const std::vector<std::string>& CContext::SplitNotifyClass( const std::string& notifyclass)
{
	regex::match_results results;
	regex::rpattern pat( std::wstring(L"::"));
	regex::split_results sr;
	pat.split( str2wstr( notifyclass), sr);

	m_notifyclassSplit.resize( sr.size());
	int i = 0;
	for ( regex::split_results::iterator it = sr.begin(); it != sr.end(); ++ it, ++ i)
	{
		m_notifyclassSplit[i] = wstr2str( sr[i]);
	}

	return m_notifyclassSplit;
}

const std::vector<std::string>& CContext::GetSplitedNotifyClass() const
{
	return m_notifyclassSplit;
}

void CContext::LoadTextFormat( const char* szTextFormat)
{
	if ( !szTextFormat)
		return;

	CCoInitScope		coinut;
	try
	{
		CComPtr<IXMLDOMDocument> spXMLDOM;
		HRESULT hr = spXMLDOM.CoCreateInstance(	__uuidof(DOMDocument));
		if ( FAILED(hr) ) 
			throw "Unable to create XML parser object";
		if ( spXMLDOM.p == NULL ) 
			throw "Unable to create XML parser object";

		VARIANT_BOOL bSuccess = false;
		std::wstring wstrTemp;
		str2wstr( szTextFormat, wstrTemp);
		hr = spXMLDOM->load( CComVariant( wstrTemp.c_str()),
			&bSuccess);
		if ( FAILED(hr) ) 
			throw "Unable to load XML document into the parser";
		if ( !bSuccess ) 
			throw "Unable to load XML document into the parser";

		CComBSTR bstrSS( L"Config/FontProfile");
		CComPtr<IXMLDOMNode> spXMLNode;
		hr = spXMLDOM->selectSingleNode(bstrSS,&spXMLNode);
		if ( FAILED(hr) || spXMLNode == NULL) 
			throw "Unable to locate FontProfile node";

		CComPtr<IXMLDOMElement> spXMLElem;
		hr = spXMLNode->QueryInterface( IID_IXMLDOMElement,   (void**)&spXMLElem);
		if ( FAILED( hr) || spXMLElem == NULL)
			throw "Unable to locate FontProfile node";


		CComPtr<IXMLDOMNodeList> spNodeList;  
		hr = spXMLElem->get_childNodes( &spNodeList); 
		if ( FAILED(hr) ) 
			throw "无法加载子节点";

		long lCount;                               
		hr = spNodeList->get_length( &lCount); 
		if ( FAILED(hr) ) 
			throw "无法获取子节点数";

		for ( long i = 0; i < lCount; ++ i)
		{
			CComPtr<IXMLDOMNode> spNode;
			DOMNodeType NodeType;

			hr = spNodeList->get_item( i, &spNode);    //获取结点
			hr = spNode->get_nodeType( &NodeType);     //获取结点信息的类型
			if ( NODE_ELEMENT == NodeType)
			{
				std::string strValue;
				CComBSTR value;
				hr = spNode->get_nodeName( &value);                //获取结点的名字

				wstr2str( value.m_str, strValue);

				if ( SUCCEEDED( hr) && _stricmp( strValue.c_str(), "Profile") == 0)
				{
					TEXTFORMAT tf;

					CComVariant variant;
					CComPtr<IXMLDOMElement>		spEle;
					hr = spNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&spEle);
					if ( FAILED( hr) || spEle == NULL)
						throw "无法加载字体样式";

					if ( S_FALSE == spEle->getAttribute( L"fontid", &variant))
						throw "无法加载字体号";

					tf.nFontType = _wtoi( variant.bstrVal);

					if ( S_FALSE == spEle->getAttribute( L"name", &variant))
						throw "无法加载字体样式名";

					std::string profilename;
					wstr2str( variant.bstrVal, profilename);
					CTextFormatMap::iterator it = m_textformatMap.find( profilename);
					if ( it != m_textformatMap.end())
						throw "字体样式名重复";


					CComPtr<IXMLDOMNodeList> spSubList;  
					hr = spEle->get_childNodes( &spSubList); 
					if ( FAILED(hr) ) 
						throw "无法加载State子节点";

					long lStateCount;                               
					hr = spSubList->get_length( &lStateCount); 
					if ( FAILED(hr) ) 
						throw "无法获取State子节点数";

					for ( long j = 0; j < lStateCount; ++ j)
					{
						CComPtr<IXMLDOMNode> spStateNode;
						DOMNodeType StateNodeType;

						hr = spSubList->get_item( j, &spStateNode);    //获取结点
						hr = spStateNode->get_nodeType( &StateNodeType);     //获取结点信息的类型
						if ( NODE_ELEMENT == StateNodeType)
						{
							int			id = -1;
							TEXTSTATE	ts;

							CComPtr<IXMLDOMElement>		spEleState;
							hr = spStateNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&spEleState);
							if ( FAILED( hr) || spEleState == NULL)
								throw "无法加载字体样式状态";

							if ( S_FALSE != spEleState->getAttribute( L"id", &variant))
								id = _wtoi( variant.bstrVal);
							
							if ( S_FALSE != spEleState->getAttribute( L"color", &variant))
								wstr2str( variant.bstrVal, ts.szColor);
							ts.szColor2 = ts.szColor;
							ts.szColor = ARGB2RGBA( ts.szColor);

							if ( S_FALSE != spEleState->getAttribute( L"color2", &variant))
								wstr2str( variant.bstrVal, ts.szColor2);
							ts.szColor2 = ARGB2RGBA( ts.szColor2);

							if ( S_FALSE != spEleState->getAttribute( L"effcolor", &variant))
								wstr2str( variant.bstrVal, ts.szEffectColor);
							ts.szEffectColor = ARGB2RGBA( ts.szEffectColor);

							if ( S_FALSE != spEleState->getAttribute( L"underline", &variant))
							{
								wstr2str( variant.bstrVal, strValue);
								if ( _stricmp( strValue.c_str(), "true") == 0)
									ts.bUnderline = true;
								else
									ts.bUnderline = false;
							}

							if ( S_FALSE != spEleState->getAttribute( L"shadow", &variant))
							{
								wstr2str( variant.bstrVal, strValue);
								if ( _stricmp( strValue.c_str(), "true") == 0)
									ts.bShadow = true;
								else
									ts.bShadow = false;
							}

							if ( S_FALSE != spEleState->getAttribute( L"outline", &variant))
							{
								wstr2str( variant.bstrVal, strValue);
								if ( _stricmp( strValue.c_str(), "true") == 0)
									ts.bOutline = true;
								else
									ts.bOutline = false;
							}

							ts.bGraudal = 0 != _stricmp( ts.szColor.c_str(), ts.szColor2.c_str());

							if ( id >= 0 && id <= TSF_STATE_MAXNUM)
								tf.textStateMap[ id] = ts;

						}
					}

					m_textformatMap[ profilename] = tf;
				}
			}
		}
	}
	catch (...)
	{
		return;
	}

	if ( !m_textformatMap.empty())
		m_bUseTextFormat = true;
}

void CContext::SetNameSpace( std::string szNamespace)
{
	m_namespace.clear();
	m_namespace = SplitString( szNamespace, "\\.");
	if ( m_namespace.size() == 1)
		m_namespace = SplitString( szNamespace, "\\:");
}

const std::vector<std::string>& CContext::GetNameSpace() const
{
	return m_namespace;
}

void CContext::LoadTextResXmlFile(const std::string file)
{
	m_textResMap.clear();
	m_textResIDs.clear();

	m_textResXmlFile = file;

	if ( !m_textResXmlFile.empty())
	{
		CCoInitScope		coinut;
		try
		{
			CComPtr<IXMLDOMDocument> spXMLDOM;
			HRESULT hr = spXMLDOM.CoCreateInstance(	__uuidof(DOMDocument));
			if ( FAILED(hr) ) 
				throw "Unable to create XML parser object";
			if ( spXMLDOM.p == NULL ) 
				throw "Unable to create XML parser object";

			VARIANT_BOOL bSuccess = false;
			std::wstring wstrTemp;
			str2wstr( m_textResXmlFile.c_str(), wstrTemp);
			hr = spXMLDOM->load( CComVariant( wstrTemp.c_str()),
				&bSuccess);
			if ( FAILED(hr) ) 
				throw "Unable to load XML document into the parser";
			if ( !bSuccess ) 
				throw "Unable to load XML document into the parser";

			CComBSTR bstrSS( L"TextRes");
			CComPtr<IXMLDOMNode> spXMLNode;
			hr = spXMLDOM->selectSingleNode(bstrSS,&spXMLNode);
			if ( FAILED(hr) || spXMLNode == NULL) 
				throw "Unable to locate TextRes node";

			CComPtr<IXMLDOMElement> spXMLElem;
			hr = spXMLNode->QueryInterface( IID_IXMLDOMElement,   (void**)&spXMLElem);
			if ( FAILED( hr) || spXMLElem == NULL)
				throw "Unable to locate TextRes node";


			CComPtr<IXMLDOMNodeList> spNodeList;  
			hr = spXMLElem->get_childNodes( &spNodeList); 
			if ( FAILED(hr) ) 
				throw "无法加载子节点";

			long lCount;                               
			hr = spNodeList->get_length( &lCount); 
			if ( FAILED(hr) ) 
				throw "无法获取子节点数";

			for ( long i = 0; i < lCount; ++ i)
			{
				CComPtr<IXMLDOMNode> spNode;
				DOMNodeType NodeType;

				hr = spNodeList->get_item( i, &spNode);    //获取结点
				hr = spNode->get_nodeType( &NodeType);     //获取结点信息的类型
				if ( NODE_ELEMENT == NodeType)
				{
					std::string strValue;
					CComBSTR value;
					hr = spNode->get_nodeName( &value);                //获取结点的名字

					wstr2str( value.m_str, strValue);

					if ( SUCCEEDED( hr) && _stricmp( strValue.c_str(), "res") == 0)
					{
						std::string text;
						int	id;
						CTextResRefVector refs;

						CComVariant variant;
						CComPtr<IXMLDOMElement>		spEle;
						hr = spNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&spEle);
						if ( FAILED( hr) || spEle == NULL)
							throw "加载文本资源出错";

						if ( S_FALSE == spEle->getAttribute( L"text", &variant))
							throw "无法加载文本串";
						wstr2str( variant.bstrVal, text);

						if ( S_FALSE == spEle->getAttribute( L"id", &variant))
							throw "无法加载文本id";
						id = _wtoi( variant.bstrVal);

						// 读取子节点
						CComPtr<IXMLDOMNodeList> spSubList;  
						hr = spEle->get_childNodes( &spSubList); 
						if ( FAILED(hr) ) 
							continue;

						long lRefCount;                               
						hr = spSubList->get_length( &lRefCount); 
						if ( FAILED(hr) ) 
							continue;

						for ( long j = 0; j < lRefCount; ++ j)
						{
							CComPtr<IXMLDOMNode> spRefNode;
							DOMNodeType RefNodeType;

							hr = spSubList->get_item( j, &spRefNode);    //获取结点
							hr = spRefNode->get_nodeType( &RefNodeType);     //获取结点信息的类型
							if ( NODE_ELEMENT == RefNodeType)
							{
								TEXTRESREF textresRef;

								CComPtr<IXMLDOMElement>		spEleRef;
								hr = spRefNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&spEleRef);
								if ( FAILED( hr) || spEleRef == NULL)
									continue;

								if ( S_FALSE != spEleRef->getAttribute( L"refui", &variant))
									wstr2str( variant.bstrVal, textresRef.referenceUI);

								if ( S_FALSE != spEleRef->getAttribute( L"refcomponent", &variant))
									wstr2str( variant.bstrVal, textresRef.referenceComponent);

								refs.push_back( textresRef);

							}
						}

						//CTextResMap::iterator it = m_textResMap.find( text);
						//if ( it != m_textResMap.end())
						//{
						//	if ( it->second.id < eMinTextResID)
						//		continue;
						//}
						m_textResMap[ text].id = id;
						m_textResMap[ text].refs = refs;

						TEXTRESKEY key;
						key.id = id;
						key.text = text;
						m_textResIDs.push_back( key);
					}
				}
			}
		}
		catch (...)
		{
			return;
		}

		std::sort( m_textResIDs.begin(), m_textResIDs.end());
	}
}

void CContext::SaveTextResXmlFile()
{
	if ( !m_textResMap.empty())
	{
		CCoInitScope		coinut;
		try
		{
			CComPtr<IXMLDOMDocument> spXMLDOM;
			HRESULT hr = spXMLDOM.CoCreateInstance(	__uuidof(DOMDocument));

			if ( FAILED(hr) || spXMLDOM.p == NULL) 
				throw "Unable to create XML parser object";

			VARIANT_BOOL bSuccess = false;
			IXMLDOMProcessingInstruction *ppi = NULL;
			hr = spXMLDOM->createProcessingInstruction( L"xml" ,L"version=\"1.0\" encoding=\"UTF-16\"" ,&ppi);
			if ( FAILED( hr) || !ppi)
				throw "无法新建xml";

			spXMLDOM->appendChild( ppi,NULL);
			ppi->Release();

			CComPtr<IXMLDOMNode> spXMLRootNode;
			hr = spXMLDOM->createNode( CComVariant(NODE_ELEMENT), L"TextRes", NULL, &spXMLRootNode);
			if ( FAILED( hr))
				throw "创建TextRes节点失败";

			for ( CTextResKeyVector::iterator it_key = m_textResIDs.begin(); it_key != m_textResIDs.end(); ++ it_key)
			{
				CTextResMap::iterator it = m_textResMap.find( (*it_key).text);
				if ( it == m_textResMap.end())
					continue;

				TEXTRES& res = it->second;

				CComPtr<IXMLDOMNode> spXMLNode;
				hr = spXMLDOM->createNode( CComVariant(NODE_ELEMENT), L"res", NULL, &spXMLNode);
				if ( FAILED( hr))
					throw "创建res节点失败";

				CComQIPtr< IXMLDOMElement> spXMLEle = spXMLNode;
				//spXMLEle->setAttribute( L"id", CComVariant( (long)it->second.id));
				spXMLEle->setAttribute( L"id", CComVariant( (long)(*it_key).id));
				spXMLEle->setAttribute( L"text", CComVariant( str2wstr( it->first.c_str()).c_str()));


				for ( CTextResRefVector::iterator it_ref = res.refs.begin(); it_ref != res.refs.end(); ++ it_ref)
				{
					// 创建ref子节点
					CComPtr<IXMLDOMNode> spXMLRefNode;
					hr = spXMLDOM->createNode( CComVariant(NODE_ELEMENT), L"ref", NULL, &spXMLRefNode);
					if ( FAILED( hr))
						continue;

					TEXTRESREF& ref = *it_ref;
					if ( ref.referenceUI.empty() || ref.referenceComponent.empty())
						continue;

					CComQIPtr< IXMLDOMElement> spXMLEle = spXMLRefNode;

					spXMLEle->setAttribute( L"refui", CComVariant( str2wstr( ref.referenceUI).c_str()));
					spXMLEle->setAttribute( L"refcomponent", CComVariant( str2wstr( ref.referenceComponent).c_str()));

					CComQIPtr< IXMLDOMText> spXMLTextNode;
					spXMLDOM->createTextNode( L"\n\t\t", &spXMLTextNode);
					spXMLNode->appendChild( spXMLTextNode, NULL);

					spXMLNode->appendChild( spXMLRefNode, NULL);
				}

				if ( !it->second.refs.empty())
				{
					CComQIPtr< IXMLDOMText> spXMLTextNode;
					spXMLDOM->createTextNode( L"\n\t", &spXMLTextNode);
					spXMLNode->appendChild( spXMLTextNode, NULL);
				}

				CComQIPtr< IXMLDOMText> spXMLTextNode;
				spXMLDOM->createTextNode( L"\n\t", &spXMLTextNode);
				spXMLRootNode->appendChild( spXMLTextNode, NULL);
				spXMLRootNode->appendChild( spXMLNode, NULL);
			}

			CComQIPtr< IXMLDOMText> spXMLTextNode;
			spXMLDOM->createTextNode( L"\n", &spXMLTextNode);
			spXMLRootNode->appendChild( spXMLTextNode, NULL);

			spXMLDOM->appendChild( spXMLRootNode, NULL);

			hr = spXMLDOM->save( CComVariant( str2wstr( m_textResXmlFile).c_str()));
			if ( FAILED( hr))
				throw "无法保存XML文件";
		}
		catch( ...)
		{
			return;
		}

	}
}

int CContext::LoadTextResID( const std::string& text, const std::string& refui, const std::string& refcomponent)
{
	CTextResMap::iterator it = m_textResMap.find( text);
	if ( it != m_textResMap.end())
	{
		if ( it->second.id >= eMinTextResID)
		{
			bool bRefFound = false;
			CTextResRefVector& refs = it->second.refs;
			for ( CTextResRefVector::iterator i = refs.begin(); i != refs.end(); ++ i)
			{
				if ( _stricmp( (*i).referenceUI.c_str(), refui.c_str()) == 0 && _stricmp( (*i).referenceComponent.c_str(), refcomponent.c_str()) == 0)
				{
					bRefFound = true;
					break;
				}
			}
			if ( !bRefFound)
			{
				TEXTRESREF ref;
				ref.referenceUI = refui;
				ref.referenceComponent = refcomponent;
				it->second.refs.push_back( ref);
			}
		}

		return it->second.id;
	}

	TEXTRESKEY key;
	key.id = eMinTextResID;
	for ( CTextResKeyVector::const_iterator it_id = std::find( m_textResIDs.begin(), m_textResIDs.end(), key); it_id != m_textResIDs.end(); ++ it_id, ++ key.id)
	{
		if ( (*it_id).id != key.id)
			break;
	}
	TEXTRES textRes;
	TEXTRESREF ref;
	ref.referenceUI = refui;
	ref.referenceComponent = refcomponent;

	textRes.id = key.id;
	textRes.refs.push_back( ref);

	key.text = text;
	m_textResIDs.push_back( key);
	m_textResMap.insert( std::make_pair( text, textRes));

	return key.id;
}
