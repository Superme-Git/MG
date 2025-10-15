///=============================================================================
/// Product   : Nuclear
/// Project   : xmliolib
/// Module    : msxmlio.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	msxmlio相关实现
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-7-17 Created.
///=============================================================================
#include "../../pfs/pfs2.h"
#include "abstractio.h"
#include <atlcomcli.h>
#include "../msdom.h"
#include "msxmlio.h"
#include "xml2binary.h"
#include "../xmlio.h"

namespace XMLIO
{
	namespace MSXMLIO
	{
		bool CMSXMLINode::GetChildAt( size_t index, CINode& rNode) const
		{
			CMSXMLINode* pNode = new CMSXMLINode;
			if( pNode == NULL )
			{
				return false;
			}

			if( !m_node.GetChildAt( index, pNode->m_node) )
			{
				delete pNode;
				return false;
			}

			return rNode.Construct( pNode );
		}

		bool CCMSXMLReader::GetRootNode(CINode& rNode) const
		{
			CMSXMLINode* pRoot = new CMSXMLINode;
			if( pRoot == NULL ) return false;
			if( !m_Document.GetRootNode( pRoot->m_node ) )
			{
				delete pRoot;
				return false;
			}

			return rNode.Construct( pRoot );
		}

		// 还原XML文本
		bool CCMSXMLReader::EncodeToXML( std::wstring& str)
		{
			CComPtr<IXMLDOMDocument> spDocument = m_Document.GetInterface();
			CComBSTR bstr;
			HRESULT hr = spDocument->get_xml( &bstr);
			if( FAILED(hr) )
			{
				return FALSE;
			}
			str.assign( bstr );
			return TRUE;
		}

		// 转换为二进制数据块
		bool CCMSXMLReader::EncodeToBinary( PFS::CDataBlock& data)
		{
			CComPtr<IXMLDOMDocument> spDocument = m_Document.GetInterface();
			
			CXML2Binary _Translator;
			return _Translator.Translate( spDocument, data );
		}

	} // namespace MSXMLIO
} // namespace XMLIO
