#pragma once
#include <sstream>

// CShowXmlView view

class CShowXmlView : public CEditView
{
	DECLARE_DYNCREATE(CShowXmlView)

protected:
	CShowXmlView();           // protected constructor used by dynamic creation
	virtual ~CShowXmlView();

	// Attributes
public:
	CviewxmlDoc* GetDocument() const
	{ return reinterpret_cast<CviewxmlDoc*>(m_pDocument); }

	virtual void OnInitialUpdate();

protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
};
