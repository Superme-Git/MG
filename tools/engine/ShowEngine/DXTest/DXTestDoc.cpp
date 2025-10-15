// DXTestDoc.cpp : implementation of the CDXTestDoc class
//

#include "stdafx.h"
#include "DXTest.h"

#include "DXTestDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CDXTestDoc

IMPLEMENT_DYNCREATE(CDXTestDoc, CDocument)

BEGIN_MESSAGE_MAP(CDXTestDoc, CDocument)
END_MESSAGE_MAP()


// CDXTestDoc construction/destruction

CDXTestDoc::CDXTestDoc()
{
	// TODO: add one-time construction code here

}

CDXTestDoc::~CDXTestDoc()
{
}

BOOL CDXTestDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}




// CDXTestDoc serialization

void CDXTestDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}


// CDXTestDoc diagnostics

#ifdef _DEBUG
void CDXTestDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CDXTestDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG


// CDXTestDoc commands
