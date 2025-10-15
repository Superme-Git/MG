#pragma once
#include "LeftView.h"

class CLeftViewListener
{
public:
	// �����ļ�����
	virtual void SetZipTreeItem( CZipTreeItem* item) = 0;
	virtual void SetIsResource( bool bIsResource) {}

	// ����LeftView
	void SetLeftView( CLeftView* leftview) { m_leftview = leftview; }

protected:
	CLeftView*				m_leftview;
};