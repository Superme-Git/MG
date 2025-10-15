// viewxmlDoc.cpp : implementation of the CviewxmlDoc class
//

#include "stdafx.h"
#include "viewxml.h"
#include "viewxmlDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

// CviewxmlDoc

IMPLEMENT_DYNCREATE(CviewxmlDoc, CDocument)

BEGIN_MESSAGE_MAP(CviewxmlDoc, CDocument)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVE, &CviewxmlDoc::OnUpdateFileSave)
END_MESSAGE_MAP()

// CviewxmlDoc construction/destruction

CviewxmlDoc::CviewxmlDoc()
: m_bOpen( FALSE)
{
}

CviewxmlDoc::~CviewxmlDoc()
{
}

BOOL CviewxmlDoc::OnNewDocument()
{
	m_reader.CloseFile();
	m_bOpen = FALSE;

	if (!CDocument::OnNewDocument())
		return FALSE;

	return TRUE;
}

// CviewxmlDoc commands

BOOL CviewxmlDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	//if (!CDocument::OnOpenDocument(lpszPathName))
	//	return FALSE;

	m_reader.CloseFile();
	m_bOpen = (XMLIO::EC_SUCCESS == m_reader.OpenNativeFile( lpszPathName));

	return m_bOpen;
}

BOOL CviewxmlDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	// TODO: Add your specialized code here and/or call the base class
	if( !m_bOpen)
	{
		return FALSE;
	}

	XMLIO::CINode rootSrc;
	if( !m_reader.GetRootNode( rootSrc))
	{
		return FALSE;
	}
	XMLIO::CFileWriter writer;
	XMLIO::CONode rootDes;
	if( !writer.GetRootNode( rootDes))
	{
		return FALSE;
	}

	// 二进制xml转文本xml
	WriteNode( rootSrc, rootDes);
	return (XMLIO::EC_SUCCESS == writer.SaveNative( lpszPathName));
	//return TRUE;
}

void CviewxmlDoc::WriteNode( const XMLIO::CINode& nodeSrc, XMLIO::CONode& nodeDes)
{
	// text
	std::wstring value = nodeSrc.GetText( );
	nodeDes.InsertText( value);

	// attribute
	XMLIO::CAttributesNames atts;
	nodeSrc.GetAttributeNames( atts);
	for( XMLIO::CAttributesNames::const_iterator i=atts.begin(), e=atts.end(); i != e; ++i)
	{
		const std::wstring& attName = *i;
		nodeSrc.GetAttribute( attName, value);
		nodeDes.SetAttribute( attName, value);
	}

	// children
	XMLIO::CINode childSrc;
	XMLIO::CONode childDes;
	size_t count = nodeSrc.GetChildrenCount();
	for( size_t i=0; i<count; i++)
	{
		if( nodeSrc.GetChildAt( i, childSrc))
		{
			if( nodeDes.CreateSubElement( childSrc.GetName().c_str(), childDes))
			{
				WriteNode( childSrc, childDes);
			}
		}
	}
}

void CviewxmlDoc::OnUpdateFileSave(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bModified);
}
