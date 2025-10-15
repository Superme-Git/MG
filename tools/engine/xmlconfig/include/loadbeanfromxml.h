#pragma once

namespace XMLCONFIG {

	template<class BeanType, class PrecType> inline int LoadBeanFromXMLFileForOneFile( const std::wstring& filename, PrecType prec, std::wstring& nextfilename)
	{
		XMLIO::CFileReader		m_reader;

		int code = m_reader.OpenFile( filename);
		if( XMLIO::EC_SUCCESS != code)
			return code;

		XMLIO::CINode		root;
		if( !m_reader.GetRootNode( root))
			return XMLIO::EC_PARSE_XML_CONTEXT;

		nextfilename.clear();
		root.GetAttribute( L"nextfile", nextfilename);

		size_t count = root.GetChildrenCount();
		for( size_t i = 0; i < count; i ++)
		{
			BeanType		bean;
			XMLIO::CINode	subnode;
			if( !root.GetChildAt( i, subnode))
				continue;
			BeanFromXML( bean, subnode);
			prec( bean);
		}
		return XMLIO::EC_SUCCESS;
	}

	template<class BeanType, class PrecType> inline int LoadBeanFromXMLFile( const std::wstring& path, const std::wstring& filename, PrecType prec)
	{
		std::wstring	nextfilename;
		
		int code = LoadBeanFromXMLFileForOneFile<BeanType,PrecType>( path + L"/" + filename, prec, nextfilename);
		if( XMLIO::EC_SUCCESS != code)
			return code;

		while( !nextfilename.empty())
			LoadBeanFromXMLFileForOneFile<BeanType,PrecType>( path + L"/" + nextfilename, prec, nextfilename);
		return XMLIO::EC_SUCCESS;
	}

} // namespace XMLCONFIG {

namespace XMLCONFIG {

	template<class ValueType> inline const wchar_t* GetValueTypeName()
	{
		return L"unknowname";
	}
	template<> inline const wchar_t* GetValueTypeName<int>()
	{
		return L"int";
	}
	template<> inline const wchar_t* GetValueTypeName<long long>()
	{
		return L"long";
	}
	template<> inline const wchar_t* GetValueTypeName<double>()
	{
		return L"double";
	}
	template<> inline const wchar_t* GetValueTypeName<bool>()
	{
		return L"bool";
	}
	template<> inline const wchar_t* GetValueTypeName<std::wstring>()
	{
		return L"string";
	}

} // namespace XMLCONFIG {

namespace XMLCONFIG {

	inline void LoadAttributeValue( const XMLIO::CINode& node, const std::wstring& name, int& value)
	{
		value = node.GetAttributeInteger( name);
	}
	inline void LoadAttributeValue( const XMLIO::CINode& node, const std::wstring& name, long long& value)
	{
		value = node.GetAttributeInt64( name);
	}
	inline void LoadAttributeValue( const XMLIO::CINode& node, const std::wstring& name, bool& value)
	{
		std::wstring	temp;
		node.GetAttribute( name, temp);
		value = ( 0 == _wcsicmp( temp.c_str(), L"true"));
	}
	inline void LoadAttributeValue( const XMLIO::CINode& node, const std::wstring& name, double& value)
	{
		value = node.GetAttributeFloat( name);
	}
	inline void LoadAttributeValue( const XMLIO::CINode& node, const std::wstring& name, std::wstring& value)
	{
		value.clear();
		node.GetAttribute( name, value);
	}

	template<class Container> inline void ContainerBeanFromXML( Container& bean, const XMLIO::CINode& node)
	{
		typedef typename Container::value_type ValueType;
		ValueType		value;
		std::wstring	nodename;

		bean.clear();
		size_t count = node.GetChildrenCount();
		for( size_t i = 0; i < count; i ++)
		{
			XMLIO::CINode	subnode;
			if( !node.GetChildAt( i, subnode))
				continue;

			if( subnode.GetName() != GetValueTypeName<ValueType>())
				continue;

			BeanFromXML( value, subnode);
			bean.insert( bean.end(), value);
		}
	}
	template<class ValueType> inline void BeanFromXML( std::vector<ValueType>& bean, const XMLIO::CINode& node)
	{
		ContainerBeanFromXML( bean, node);
	}
	template<class ValueType> inline void BeanFromXML( std::set<ValueType>& bean, const XMLIO::CINode& node)
	{
		ContainerBeanFromXML( bean, node);
	}

	inline void BeanFromXML( int& bean, const XMLIO::CINode& node)
	{
		bean = _wtoi( node.GetSubText().c_str());
	}

	inline void BeanFromXML( long long& bean, const XMLIO::CINode& node)
	{
		bean = _wtoi64( node.GetSubText().c_str());
	}

	inline void BeanFromXML( bool& bean, const XMLIO::CINode& node)
	{
		bean = ( 0 == _wcsicmp( node.GetSubText().c_str(), L"true"));
	}

	inline void BeanFromXML( double& bean, const XMLIO::CINode& node)
	{
		bean = _wtof( node.GetSubText().c_str());
	}

	inline void BeanFromXML( std::wstring& bean, const XMLIO::CINode& node)
	{
		bean = node.GetSubText();
	}

} // namespace XMLCONFIG {
