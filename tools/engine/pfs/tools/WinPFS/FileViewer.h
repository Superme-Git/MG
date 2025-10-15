#pragma once

class CFileViewer : public CCmdTarget
{
public:

	virtual bool Create( CWnd* parent) = 0;
	virtual bool SetData( const void* pData, size_t len ) = 0;
	virtual void Show( bool bShow) = 0;
	virtual void Move() = 0;

public:
	// for translating Windows messages in main message pump
	// virtual BOOL PreTranslateMessage(MSG* pMsg) { return FALSE; }
};
