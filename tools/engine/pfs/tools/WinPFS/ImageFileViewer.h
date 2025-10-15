#pragma once
#include "FileViewer.h"

class CImageFileViewer : public CFileViewer
{
public:
	CImageFileViewer(void);
	~CImageFileViewer(void);

	virtual bool Create( CWnd* parent) { return true; }
	virtual bool SetData( const void* pData, size_t len ) { return true; }
	virtual void Show( bool bShow){}
	virtual void Move(){}
};
