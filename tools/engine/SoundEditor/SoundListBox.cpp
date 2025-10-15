// SoundListBox.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "SoundListBox.h"


// CSoundListBox

IMPLEMENT_DYNAMIC(CSoundListBox, CCheckListBox)

CSoundListBox::CSoundListBox()
{

}

CSoundListBox::~CSoundListBox()
{
}


BEGIN_MESSAGE_MAP(CSoundListBox, CCheckListBox)
END_MESSAGE_MAP()

// CSoundListBox message handlers

bool CSoundListBox::PreDrawSoundItem(CDC* pDC, DRAWITEMSTRUCT &drawItem, int nCheck, int cyItem)
{
	//_AFX_CHECKLIST_STATE* pChecklistState = _afxChecklistState;	
	CDC bitmapDC;
	if (bitmapDC.CreateCompatibleDC(pDC))
	{

		// Change Compatible DC to LTR since the bitmap is LTR
		DWORD dwLayoutBitmapDC = bitmapDC.GetLayout();
		bitmapDC.SetLayout(0);

		CRect rectCheck = drawItem.rcItem;
		rectCheck.left += 1;
		rectCheck.top += 1 + max(0, (cyItem - CSLB_CHECK_BOX_HEIGHT) / 2);
		rectCheck.right = rectCheck.left + CSLB_CHECK_BOX_WIDTH;
		rectCheck.bottom = rectCheck.top + CSLB_CHECK_BOX_HEIGHT;

		CRect rectItem = drawItem.rcItem;
		rectItem.right = rectItem.left + CSLB_CHECK_BOX_WIDTH + 3;
		CRect rectCheckBox = OnGetCheckPosition(rectItem, rectCheck);

		XPASSERT(rectCheck.IntersectRect(rectItem, rectCheckBox));
		if (!((rectCheck == rectCheckBox) && (rectCheckBox.Width() == CSLB_CHECK_BOX_WIDTH) && (rectCheckBox.Height() == CSLB_CHECK_BOX_HEIGHT)))
		{
			AfxMessageBox(L"出问题了，去问程序这怎么回事……");
		}

		COLORREF newBkColor = GetSysColor(COLOR_WINDOW);

		BOOL fDisabled = !IsWindowEnabled() || !IsEnabled(drawItem.itemID);
		if ((drawItem.itemState & ODS_SELECTED) && !fDisabled)
			newBkColor = GetSysColor(COLOR_HIGHLIGHT);

		CBrush brush(newBkColor);
		pDC->FillRect(rectItem, &brush);

		DWORD dwLayoutDC = pDC->GetLayout();

		// Change destination DC layout to preserve bitmap orientation
		pDC->SetLayout(dwLayoutDC | LAYOUT_BITMAPORIENTATIONPRESERVED);

		CGdiObject *pOldBrush = pDC->SelectStockObject(NULL_BRUSH);
		switch (nCheck)
		{
		case 0://没有选，在声音代表不区分白天黑夜
			pDC->SelectStockObject(WHITE_BRUSH);
			pDC->Rectangle(&rectCheckBox);
			pDC->SelectStockObject(DKGRAY_BRUSH);
			pDC->Rectangle(rectCheckBox.left + CSLB_CHECK_BOX_WIDTH / 2, rectCheckBox.top, rectCheckBox.right, rectCheckBox.bottom);
			break;
		case 1://选上，白天
			pDC->SelectStockObject(WHITE_BRUSH);
			pDC->Rectangle(&rectCheckBox);
			break;
		case 2://灰的，晚上
			pDC->SelectStockObject(DKGRAY_BRUSH);
			pDC->Rectangle(&rectCheckBox);
			break;
		}
		pDC->SelectObject(pOldBrush);
		drawItem.rcItem.left = drawItem.rcItem.left + CSLB_CHECK_BOX_WIDTH + 3;
	}
	return true;
}

void CSoundListBox::PreDrawSoundItem(LPDRAWITEMSTRUCT lpDrawItemStruct)
{
	DRAWITEMSTRUCT drawItem = *lpDrawItemStruct;

	if ((((LONG)drawItem.itemID) >= 0) &&
		((drawItem.itemAction & (ODA_DRAWENTIRE | ODA_SELECT)) != 0))
	{
		int cyItem = GetItemHeight(drawItem.itemID);

		CDC* pDC = CDC::FromHandle(drawItem.hDC);

		COLORREF newBkColor = GetSysColor(COLOR_WINDOW);

		BOOL fDisabled = !IsWindowEnabled() || !IsEnabled(drawItem.itemID);
		if ((drawItem.itemState & ODS_SELECTED) && !fDisabled)
			newBkColor = GetSysColor(COLOR_HIGHLIGHT);

		COLORREF oldBkColor = pDC->SetBkColor(newBkColor);

		int nCheck = GetCheck(drawItem.itemID);
		bool bThemed = false;

		PreDrawSoundItem(pDC, drawItem, nCheck, cyItem);

		pDC->SetBkColor(oldBkColor);
	}
	PreDrawItemHelper(&drawItem);
}

BOOL CSoundListBox::OnChildNotify(UINT message, WPARAM wParam, LPARAM lParam, LRESULT* pLResult)
{
	if (message == WM_DRAWITEM)
	{
		XPASSERT(pLResult == NULL);       // no return value expected
		PreDrawSoundItem((LPDRAWITEMSTRUCT)lParam);
		return TRUE;
	}

	return CCheckListBox::OnChildNotify(message, wParam, lParam, pLResult);
}
