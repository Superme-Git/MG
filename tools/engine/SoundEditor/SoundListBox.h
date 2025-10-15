#pragma once
//#include "DragCheckListBox.h"

// CSoundListBox

class CSoundListBox : public CCheckListBox
{
	DECLARE_DYNAMIC(CSoundListBox)
private:
	enum {
		CSLB_CHECK_BOX_WIDTH = 13,
		CSLB_CHECK_BOX_HEIGHT = 13,
	};
public:
	CSoundListBox();
	virtual ~CSoundListBox();

protected:
	DECLARE_MESSAGE_MAP()
	void PreDrawSoundItem(LPDRAWITEMSTRUCT lpDrawItemStruct);
	virtual bool PreDrawSoundItem(CDC* pDC, DRAWITEMSTRUCT &drawItem, int nCheck, int cyItem);
public:
	virtual BOOL OnChildNotify(UINT message, WPARAM wParam, LPARAM lParam, LRESULT* pLResult);
};


