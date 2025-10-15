#pragma once
#include "TextFileViewer.h"

class CXmlFileViewer : public CTextFileViewer
{

public:
	CXmlFileViewer(void);
	~CXmlFileViewer(void);

	virtual bool SetData( const void* pData, size_t len );
	
};
