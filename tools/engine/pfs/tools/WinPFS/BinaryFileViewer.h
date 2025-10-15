#pragma once
#include "TextFileViewer.h"

class CBinaryFileViewer : public CTextFileViewer
{
	
public:
	CBinaryFileViewer(void);
	~CBinaryFileViewer(void);

	virtual bool SetData( const void* pData, size_t len );

};
