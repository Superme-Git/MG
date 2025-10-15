#include "../../pfs/pfs2.h"
#include "../xmlio.h"
#include "abstractio.h"
#include "octetsiobase.h"
#include "xmlfunc.h"
#include "octetsio.h"
#include <sstream>
#include "StringHelper.h"

namespace XMLIO
{
	namespace OCTETSIO
	{
		// 二进制XML到文本XML的转换函数
		static bool _DoTranslateRecursively( std::wostream& os,
			const CMemoryNode& node, CStringDictionary& dict, int level );

		// 还原XML文本
		bool COctetsReader::EncodeToXML( std::wstring& str )
		{
			std::wostringstream os;
			if( !_DoTranslateRecursively( os, m_root, m_dict, 0 ) )
			{
				return false;
			}
			str = os.str();
			return true;
		}

		static const size_t INVALID_INDEX = -1; // 无效索引

		// 递归转换二进制XML对象
		static bool _DoTranslateRecursively(std::wostream& os, 
			const CMemoryNode& node, CStringDictionary& dict, int level )
		{
			for( int i=0; i<level; i++)
			{
				os << L"\t";
			}

			//----------------------------------------------------------
			// node type
			//----------------------------------------------------------
			unsigned int type = node.GetNodeType();

			//----------------------------------------------------------
			// node name
			//----------------------------------------------------------
			size_t nameIndex = node.GetName( );
			const CMemoryString* pName = nameIndex != INVALID_INDEX ? dict.TryGetString(nameIndex) : NULL;
			std::wstring name;
			if( pName != NULL )
				name.assign( pName->GetString(), pName->GetLength() );
			
			//----------------------------------------------------------
			// node text, maybe none
			//----------------------------------------------------------
			size_t textIndex = node.GetText(); 
			const CMemoryString* pString = textIndex != INVALID_INDEX ? dict.TryGetString(textIndex) : NULL;
			std::wstring value;
			
			if (type != NT_ELEMENT )
			{
				//----------------------------------------------------------
				// non-element node
				//----------------------------------------------------------

				if ( type == NT_CDATA_SECTION /*|| name == L"#cdata" */)
				{
					if( pString != NULL) 
					{
						os << L"<![CDATA["<< pString->GetString() << L"]]>\r\n";
					}
					else
					{
						os << L"<![CDATA[]]>\r\n";
					}
					return true;
				}
				else if ( type == NT_TEXT /*|| name == L"#text"*/ )
				{
					if( pString != NULL) 
					{
						// 对文本结点进行escape处理
						StringHelper::EscapeXml(pString->GetString(), value);
					}
					os << value << L"\r\n";
					return true;
				}
				else if( type == NT_UNKNOWN )
				{
					if( name == L"#comment" )
					{
						if( pString != NULL) 
						{
							os << L"<!--"<< pString->GetString() << L"-->\r\n";
						}
						else
						{
							os << L"<!---->\r\n";
						}
						return true;
					}					
				}
				return false;
			}
	
			//----------------------------------------------------------
			// process element node
			//----------------------------------------------------------

			// name
			os << L"<" << name;

			// attributes
			const CMemoryAttributesMap& atts = node.GetAttributesMap();
			CStringDictionary attrNames;
			atts.GetNames( attrNames );
			size_t attCount = attrNames.GetCount();
			for( size_t i=0; i < attCount; ++i)
			{
				const CMemoryString& attName = attrNames.GetString( i );
				size_t iValueIndex = atts.GetAttribute( attName );
				const CMemoryString& attValue = dict.GetString( iValueIndex );

				// 对属性值进行escape处理
				std::wstring strValue;
				StringHelper::EscapeXml(attValue.GetString(), strValue);

				os << L" " << attName.GetString() << L"=\"" << strValue << L"\"";
			}

			// children
			const CMemoryNodeList& rChildren = node.GetChildren();
			size_t count = rChildren.GetCount();

			bool bRet = true;

			if( pString != NULL) 
			{
				// 对元素值进行escape处理（应该不会遇到）
				StringHelper::EscapeXml(pString->GetString(), value);
			}
			if( value.empty() && 0 == count)
			{
				// close the element
				os << L"/>\r\n";
			}
			else
			{
				os << L">" ; // close the element start tag
				
				if ( count > 0 )
				{
					os << L"\r\n" ;

					// 有子结点 - children
					for( size_t i=0; i<count; i++)
					{
						const CMemoryNode& rNode = rChildren.GetAt( i );
						bRet = _DoTranslateRecursively( os, rNode, dict, level+1 );
					}

					if( !value.empty() )
					{
						for( int i=0; i<level; i++)
						{
							os << L"\t";
						}
						os << value << L"\r\n";
					}

					for( int i=0; i<level; i++)
					{
						os << L"\t";
					}
				}
				else
				{
					os << value;
				}

				os << L"</" << name << L">\r\n";
			}

			return bRet;
		}
	}
}
