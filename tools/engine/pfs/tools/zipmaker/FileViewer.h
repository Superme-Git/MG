#pragma once
#include <vector>
#include <string>
#include "ZipTreeItem.h"
#include <assert.h>
#include <algorithm>

class CFileViewer
{
public:
	CFileViewer(void){}
	virtual ~CFileViewer(void){}

	virtual bool Create( CWnd* parent) 
	{ 
		assert( parent); 
		m_parent = parent;
		return true;
	}
	virtual bool SetTreeItem( CZipTreeItem& item) = 0;
	bool IsBelong( const std::wstring& sExt)
	{
		return m_exts.empty() ? true : std::find( m_exts.begin(), m_exts.end(), sExt) != m_exts.end();
	}
	virtual void Show( bool bShow) = 0;
	virtual void Move() = 0;

protected:
	std::vector<std::wstring>	m_exts;
	CWnd*						m_parent;
};
