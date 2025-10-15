


#pragma once


class CChildFrame : public CMDIChildWnd
{
	DECLARE_DYNCREATE(CChildFrame)
public:
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif
	virtual ~CChildFrame();
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

	CChildFrame();

protected:
	DECLARE_MESSAGE_MAP()
};
