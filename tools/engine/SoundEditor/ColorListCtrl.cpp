// ColorListCtrl.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "ColorListCtrl.h"


// CColorListCtrl

IMPLEMENT_DYNAMIC(CColorListCtrl, CListCtrl)

CColorListCtrl::CColorListCtrl()
{

}

CColorListCtrl::~CColorListCtrl()
{
}


BEGIN_MESSAGE_MAP(CColorListCtrl, CListCtrl)
	ON_NOTIFY_REFLECT(NM_CUSTOMDRAW, &CColorListCtrl::OnNMCustomdraw)
END_MESSAGE_MAP()



// CColorListCtrl message handlers


void CColorListCtrl::OnNMCustomdraw(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVCUSTOMDRAW *lpLvdr = reinterpret_cast<NMLVCUSTOMDRAW*>(pNMHDR);
	//LPNMCUSTOMDRAW pNMCD = lpLvdr->nmcd;
	*pResult = CDRF_DODEFAULT;
	switch (lpLvdr->nmcd.dwDrawStage)
	{
	case CDDS_PREPAINT:
		*pResult = CDRF_NOTIFYITEMDRAW;
		break;
	case CDDS_ITEMPREPAINT:
		*pResult = CDRF_NOTIFYSUBITEMDRAW;
		break;
	case CDDS_ITEMPREPAINT | CDDS_SUBITEM:
		if (lpLvdr->iSubItem == 1)
		{
			lpLvdr->clrTextBk = m_colors[lpLvdr->nmcd.lItemlParam];
		}
		break;
	}
}
