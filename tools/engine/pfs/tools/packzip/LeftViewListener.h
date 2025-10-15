#pragma once
#include "LeftView.h"

class CLeftViewListener
{
public:
	// 设置文件数据
	virtual void SetZipTreeItem( CZipTreeItem* item) = 0;
	virtual void SetIsResource( bool bIsResource) {}

	// 设置LeftView
	void SetLeftView( CLeftView* leftview) { m_leftview = leftview; }

protected:
	CLeftView*				m_leftview;
};