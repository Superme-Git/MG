#pragma once
#include "afxwin.h"

//��ǰ����ϵͳ����Ҫ������ʾ..
//ʵʱ��ʾ����������
//��Ⱦ֡�ʵ���Ϣ

class CParticleSysInfoView : public CView
{
	DECLARE_DYNCREATE(CParticleSysInfoView)

protected:
	CParticleSysInfoView();           // protected constructor used by dynamic creation
	virtual ~CParticleSysInfoView();

public:
	virtual void OnDraw(CDC* pDC);      // overridden to draw this view

private:
	bool m_bStop;

protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);

	afx_msg void OnOpenPsl();
	afx_msg void OnSavePsl();
	afx_msg void OnSaveasPsl();
};
