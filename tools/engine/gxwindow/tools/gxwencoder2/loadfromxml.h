#pragma once

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

inline bool DecodeComponentFromXMLElememnt( CComPtr<IXMLDOMElement>& Ele, CComponent& component)
{
	HRESULT							hr;

	// 读取控件本身属性
	try
	{
		CComPtr<IXMLDOMNamedNodeMap>	Attributes;

		hr = Ele->get_attributes( &Attributes);
		if( FAILED( hr) || Attributes == NULL)
			throw hr;

		long lCount;

		hr = Attributes->get_length( &lCount);
		if( FAILED( hr))
			throw hr;
		for( long i = 0; i < lCount; i ++ )
		{
			CComPtr<IXMLDOMNode>		DomNode;
			DOMNodeType					DomNodeType;

			hr = Attributes->get_item( i, &DomNode);
			if( FAILED( hr))
				continue;
			hr = DomNode->get_nodeType( &DomNodeType);
			if( FAILED( hr))
				continue;
			if( NODE_ATTRIBUTE != DomNodeType)
				continue;

			CComBSTR		name;
			CComVariant		value;

			hr = DomNode->get_nodeName( &name);
			if( FAILED( hr))
				continue;
			hr = DomNode->get_nodeValue( &value);
			if( FAILED( hr))
				continue;
			component.GetPropertyMap().Push( ConvertBStrToTStr( name), ConvertVariantToString( value) );
		} // for( long i = 0; i < lCount; i ++ )

		component.SetName( component.QueryPropertyValue( "n"));
		{
			CComBSTR		classname;
			hr = Ele->get_nodeName( &classname);
			if ( !FAILED( hr))
				component.SetType( ConvertBStrToTStr( classname));
		}

		if ( _stricmp( COMTYPE_PANEL, component.GetType()) == 0 && component.QueryPropertyValue( "b").c_str()[0] != '0')
		{
				component.SetType( COMTYPE_BKPANEL);
		}

		if( !component.GetName() || ( !component.GetType() && _stricmp( "Content", component.GetType()) != 0))
			throw 0;
	}
	catch( ...) 
	{
		component.Clear();
		return false;
	}
	//if( component.GetPropertyMap().empty() && _stricmp( "Content", component.GetType()) != 0)
	//	return false;

	// 读取控件本身的子控件或用户插入的控件
	try
	{
			CComPtr<IXMLDOMNodeList>	NodeList;

			hr = Ele->get_childNodes( &NodeList);
			if( FAILED( hr) && NodeList == NULL)
				throw hr;

			long		lCount;

			hr = NodeList->get_length( &lCount);
			if ( FAILED( hr))
				lCount = 0;

			for( long i = 0; i < lCount; i ++)
			{
				CComPtr<IXMLDOMNode>		DomNode;
				CComPtr<IXMLDOMElement>		EleChild;
				CComponent				ChildComponent;

				hr = NodeList->get_item( i, &DomNode);
				if( FAILED( hr) || DomNode == NULL)
					continue;
				hr = DomNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&EleChild);
				if( FAILED( hr) || EleChild == NULL)
					continue;
				if( DecodeComponentFromXMLElememnt( EleChild, ChildComponent))
				{
					ChildComponent.SetParentType( component.GetType());

					// 根据一些属性调整控件类型
					if ( _stricmp( COMTYPE_EDIT, ChildComponent.GetType()) == 0)
					{
						if ( _stricmp( "true", ChildComponent.QueryPropertyValue( "rawarea").c_str()) == 0)
							ChildComponent.SetType( COMTYPE_ORIEDIT);
						else if ( _stricmp( "true", ChildComponent.QueryPropertyValue( COMTYPE_MULTILINE).c_str()) == 0)
							ChildComponent.SetType( COMTYPE_MULTIEDIT);
					}
					else if ( _stricmp( COMTYPE_TEXT, ChildComponent.GetType()) == 0)
					{
						if  ( _stricmp( "true", ChildComponent.QueryPropertyValue( "rawarea").c_str()) == 0)
							ChildComponent.SetType( COMTYPE_ORITEXT);
					}
					else if ( _stricmp( COMTYPE_BUTTONCHECK, ChildComponent.GetType()) == 0 && !ChildComponent.QueryPropertyValue( "t").empty())
					{
						if ( _stricmp( "true", ChildComponent.QueryPropertyValue( "button").c_str()) == 0)
							ChildComponent.SetType( COMTYPE_TEXTIMGCHECKBUTTON);
						else
							ChildComponent.SetType( COMTYPE_TEXTBUTTONCHECK);
					}
					else if ( _stricmp( COMTYPE_BUTTONRADIO, ChildComponent.GetType()) == 0 && !ChildComponent.QueryPropertyValue( "t").empty())
					{
						if ( _stricmp( "true", ChildComponent.QueryPropertyValue( "button").c_str()) == 0)
							ChildComponent.SetType( COMTYPE_TEXTIMGRADIOBUTTON);
						else
							ChildComponent.SetType( COMTYPE_TEXTBUTTONRADIO);
					}
					else if ( _stricmp( COMTYPE_BASE, ChildComponent.GetType()) == 0 && !ChildComponent.QueryPropertyValue( "b").empty() && ChildComponent.QueryPropertyValue( "b").c_str()[0] != '0')
						ChildComponent.SetType( COMTYPE_BKBASE);

					if ( _stricmp( COMTYPE_BASE, component.GetType()) == 0)
					{
						if ( _stricmp( COMTYPE_BGIMAGE, ChildComponent.GetType()) == 0)
							component.SetType( COMTYPE_BKBASE);
					}
					
					if ( _stricmp( COMTYPE_PANEL, component.GetType()) == 0)
					{
						if ( component.QueryPropertyValue( "b").c_str()[0] != '0' || _stricmp( COMTYPE_BGIMAGE, ChildComponent.GetType()) == 0)
							component.SetType( COMTYPE_BKPANEL);
					}
					
					if ( ( _stricmp( COMTYPE_PANEL, component.GetType()) == 0 || _stricmp( COMTYPE_BKPANEL, component.GetType()) == 0) && _stricmp( COMTYPE_SCROLLBAR, ChildComponent.GetType()) == 0)
						component.SetType( COMTYPE_SCROLLPANEL);
					
					if ( ( _stricmp( COMTYPE_SCROLLPANEL, component.GetType()) == 0 || _stricmp( COMTYPE_COMBOBOX, component.GetType()) == 0) && _stricmp( COMTYPE_SCROLLBAR, ChildComponent.GetType()) == 0)
					{
						if ( _stricmp( "true", ChildComponent.QueryPropertyValue( "vertical").c_str()) == 0)
							ChildComponent.SetType( COMTYPE_VSCROLLBAR);
						else
							ChildComponent.SetType( COMTYPE_HSCROLLBAR);
					}

					component.GetChilderList().push_back( ChildComponent);
				}
			} //for( i = 0; i < lCount; i ++)
	}
	catch (...)
	{
		component.Clear();
		return false;
	}

	return true;
}

inline bool LoadRootComponentFromXML( const char* szXML, CComponent& RootComponent)
{
	CCoInitScope		coinut;

	try
	{
		CComPtr<IXMLDOMDocument>	Document;
		CComPtr<IXMLDOMElement>		EleRoot;
		HRESULT						hr;

		hr = CoCreateInstance( CLSID_DOMDocument, NULL, CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&Document);
		if( FAILED( hr))
			throw hr;

		{
			VARIANT_BOOL		bResult;
			hr = Document->load( CComVariant( szXML), &bResult);
			if( FAILED( hr))
				throw hr;
			if( !bResult)
				throw -1;
		}

		{
			hr = Document->get_documentElement( &EleRoot);
			if ( FAILED( hr))
				throw hr;
			if( EleRoot == NULL)
				throw 0;
		}

		CComPtr<IXMLDOMNode> ContentNode;
		EleRoot->get_firstChild( &ContentNode);

		CComPtr<IXMLDOMNodeList>	NodeList;

		hr = ContentNode->get_childNodes( &NodeList);
		if( SUCCEEDED( hr) && NodeList != NULL)
		{	
			long		i, lCount;

			hr = NodeList->get_length( &lCount);
			if ( FAILED( hr))
				lCount = 0;

			for( i = 0; i < lCount; i ++)
			{
				CComPtr<IXMLDOMNode>		DomNode;
				CComPtr<IXMLDOMElement>		EleChild;
				CComBSTR					name;


				hr = NodeList->get_item( i, &DomNode);
				if( FAILED( hr) || DomNode == NULL)
					continue;

				hr = DomNode->get_nodeName( &name);
				if ( FAILED( hr) || strcmp( "Frame", ConvertBStrToTStr( name).c_str()) != 0)
					continue;

				hr = DomNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&EleChild);
				if( FAILED( hr) || EleChild == NULL)
					continue;
				RootComponent.SetParentType( "Content");
				if( DecodeComponentFromXMLElememnt( EleChild, RootComponent))
					break;
			} //for( i = 0; i < lCount; i ++)
		} // if( SUCCEEDED( hr) && NodeList != NULL)
	}
	catch( ...) 
	{
		return false;
	}
	return true;
}


