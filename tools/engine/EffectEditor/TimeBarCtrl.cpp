

#include "stdafx.h"
#include "resource.h"
#include "TimeBarCtrl.h"

#define IsKeyPressed(KEY) ((GetAsyncKeyState(KEY) & (0x8000)) != 0) 
#define IsCTRLPressed() IsKeyPressed(VK_CONTROL)

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CTimeBarCtrl, CDialog)



BEGIN_MESSAGE_MAP(CTimeBarCtrl, CDialog)
	ON_WM_PAINT()
	ON_WM_VSCROLL()
	ON_WM_HSCROLL()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_WM_SIZE()
	ON_WM_SIZING()
END_MESSAGE_MAP()

void CTimeBarCtrl::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);

	ResetScrollBar();
	Invalidate(FALSE);
}

void CTimeBarCtrl::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);

	ResetScrollBar();
	Invalidate(FALSE);
}

void CTimeBarCtrl::OnMouseMove(UINT nFlags, CPoint point)
{
	switch (m_mouseState)
	{
	case MOUSE_ON_MAX_FRAME_LINE:
	case MOUSE_NORMAL:
	case MOUSE_ON_SELECTED_OBJ_END:
	case MOUSE_ON_PLAYPOS:
		{
			bool ok = false;
			int mouseXPix = m_ptMousePos.x - LAYER_HEAD_WIDTH + GetScrollPos(SB_HORZ);
			int mouseX = (mouseXPix + FRAME_WIDTH / 2) / FRAME_WIDTH;
			int mouseY = (m_ptMousePos.y - HEAD_HEIGHT + GetScrollPos(SB_VERT));
			if (m_nSelectIDs.size() == 1)
			{
				ClipObjMap::iterator iter = m_mapClipObjs.find(*m_nSelectIDs.begin());
				if (iter != m_mapClipObjs.end() && !iter->second.isSpecObj)
				{
					if (mouseX == iter->second.endFrame)
					{
						int separatorPos = (m_nLayerSeparatorPos + 1) * LINE_HEIGHT;
						bool checkSparator = true;
						if (m_nLayerSeparatorPos >= -1)
						{
							if (separatorPos + SPAR_HEIGHT <= mouseY)
							{
								mouseY -= SPAR_HEIGHT;	
							} else if (separatorPos <= mouseY)	
							{
								checkSparator = false;
							}
						}
						if (checkSparator)
						{
							int objPosY = iter->second.layerID * LINE_HEIGHT;
							if (mouseY >= objPosY && mouseY <= objPosY + LINE_HEIGHT)
							{
								SetCursor(LoadCursor(NULL, IDC_SIZEWE));
								m_mouseState = MOUSE_ON_SELECTED_OBJ_END;
								ok = true;
							}
						}
					}
				}
			}

			if (!ok)
			{
				if (mouseXPix >= m_nPlayPos - mousePlayPosRange && mouseXPix <= m_nPlayPos + mousePlayPosRange && m_ptMousePos.y < HEAD_HEIGHT)
				{
					SetCursor(LoadCursor(NULL, IDC_HAND));
					m_mouseState = MOUSE_ON_PLAYPOS;
				} else if (mouseX == m_nMaxFrame)
				{
					SetCursor(LoadCursor(NULL, IDC_SIZEWE));
					m_mouseState = MOUSE_ON_MAX_FRAME_LINE;
				} else {
					m_mouseState = MOUSE_NORMAL;
				}
			}
		}
		break;
	case MOUSE_DOWN:
		if (abs(point.x - m_ptMouseDownPos.x) > 3 ||  abs(point.y - m_ptMouseDownPos.y) > 3)
		{
			if (m_nSelectIDs.size() == 1)
			{
				m_mouseState = MOUSE_DRAGING_OBJ;
				Invalidate(FALSE);
			} else {
				int separatorPos = (m_nLayerSeparatorPos + 1) * LINE_HEIGHT;
				int posY = m_ptMouseDownPos.y + GetScrollPos(SB_VERT) - HEAD_HEIGHT;
				if (posY >= separatorPos && posY < separatorPos + SPAR_HEIGHT)
				{
					m_mouseState = MOUSE_DRAGING_SPARE;
					Invalidate(FALSE);
				}
			}
		}
		break;
	case MOUSE_DRAGING_OBJ:
	case MOUSE_DRAGING_SPARE:
		Invalidate(FALSE);
		break;
	case MOUSE_DRAGING_MAX_FRAME:
	case MOUSE_DRAGING_SELECTED_OBJ_END:
		SetCursor(LoadCursor(NULL, IDC_SIZEWE));
		Invalidate(FALSE);
		break;
	case MOUSE_DRAGING_PLAYPOS:
		if (m_pNotify)
		{
			int frame = point.x - LAYER_HEAD_WIDTH;

			{

				m_pNotify->OnDragingPlayFrame(frame / FRAME_WIDTH);
				m_pNotify->OnDragingPlayPos(static_cast<float>(frame) / FRAME_WIDTH);
			}
		}
		SetCursor(LoadCursor(NULL, IDC_HAND));
		Invalidate(FALSE);
		break;
	default:
		break;
	}
	m_ptMousePos = point;
	CDialog::OnMouseMove(nFlags, point);
}

void CTimeBarCtrl::OnLButtonUp(UINT nFlags, CPoint point)
{
	ReleaseCapture();
	switch (m_mouseState)
	{
	case MOUSE_DRAGING_OBJ:
		{
			CRect rect = m_rectClientRect;
			rect.left = LAYER_HEAD_WIDTH;
			rect.top = HEAD_HEIGHT;
			if (rect.PtInRect(point))
			{
				int scrY = GetScrollPos(SB_VERT);
				CPoint pos = m_ptSelectObjPos + point - m_ptMouseDownPos;
				pos.x -= LAYER_HEAD_WIDTH;
				pos.y -= HEAD_HEIGHT;
				pos.x += GetScrollPos(SB_HORZ) + FRAME_WIDTH / 2;
				pos.y += GetScrollPos(SB_VERT) + LINE_HEIGHT / 2;
				bool checkSepar = true;
				if (m_nLayerSeparatorPos >= -1)
				{
					int layerSepPos = (m_nLayerSeparatorPos + 1) * LINE_HEIGHT;
					if (layerSepPos <= pos.y)
					{

						pos.y -= SPAR_HEIGHT;
					} else if (layerSepPos + SPAR_HEIGHT >= pos.y) {

					} else {
						checkSepar = false;
					}
				}
				if (checkSepar)
				{
					pos.x /= FRAME_WIDTH;
					pos.y /= LINE_HEIGHT;
					ClipObjMap::iterator iter = m_mapClipObjs.find(*m_nSelectIDs.begin());
					ClipObjMap::iterator objIter = m_mapClipObjs.begin();
					if (iter != m_mapClipObjs.end())
					{
						int iterEndFrame = pos.x + iter->second.endFrame - iter->second.startFrame;
						if (pos.x >= 0 && iterEndFrame <= m_nMaxFrame && pos.y < m_nLayerCount && pos.y >= 0)
						{
							ClipObjMap::iterator objIter = m_mapClipObjs.begin();
							bool ok = true;
							if (!canCross)
							{
								for (;objIter!=m_mapClipObjs.end();++objIter)
								{
									if (objIter->first != *m_nSelectIDs.begin() && objIter->second.layerID == pos.y && 
										objIter->second.endFrame > pos.x && objIter->second.startFrame < iterEndFrame)
									{
										ok = false;
									}
								}
							}
							if (ok)
							{
								ClipObj oldObj = iter->second;
								iter->second.startFrame = pos.x;
								iter->second.endFrame = iterEndFrame;
								iter->second.layerID = pos.y;
								if (m_pNotify)
								{
									if (!m_pNotify->OnModify(iter->second))
									{
										iter->second = oldObj;
									}
								}
							}
						}
					}
				}
			}
			Invalidate(FALSE);
		}
		break;
	case MOUSE_DRAGING_SPARE:
		{
			int mouseY = m_ptMousePos.y - HEAD_HEIGHT + GetScrollPos(SB_VERT);
			int separatorPos = (m_nLayerSeparatorPos + 1) * LINE_HEIGHT;
			bool result = true;
			if (mouseY > separatorPos - LINE_HEIGHT / 2)
			{
				if (mouseY < separatorPos + SPAR_HEIGHT + LINE_HEIGHT / 2)
				{
					result = false;
				} else {
					mouseY -= SPAR_HEIGHT;
				}
			}
			if (result)
			{
				int newSeparatorPos = mouseY + LINE_HEIGHT / 2;
				if (newSeparatorPos % LINE_HEIGHT > 0)
				{
					newSeparatorPos /= LINE_HEIGHT;
					if (newSeparatorPos <= m_nLayerCount)
					{
						int oldPos = m_nLayerSeparatorPos;
						SetLayerSeparatorPos(newSeparatorPos - 1);
						if (m_pNotify)
						{
							if (!m_pNotify->OnModifySpare(m_nLayerSeparatorPos))
							{
								SetLayerSeparatorPos(oldPos);
							}
						}
					}
				}
			}
		}
		Invalidate(FALSE);
		break;
	case MOUSE_DRAGING_MAX_FRAME:
		{
			int oldMaxFrame = m_nMaxFrame;
			int mouseX = (m_ptMousePos.x - LAYER_HEAD_WIDTH + GetScrollPos(SB_HORZ) + FRAME_WIDTH / 2) / FRAME_WIDTH;
			if (mouseX > 0)
			{
				SetMaxFrame(mouseX);
				if (m_pNotify)
				{
					if (!m_pNotify->OnModifyMaxFrame(m_nMaxFrame))
					{
						SetMaxFrame(oldMaxFrame);
					}
				}
			}

		}
		Invalidate(FALSE);
		break;
	case MOUSE_DRAGING_SELECTED_OBJ_END:
		{			
			int mouseX = (m_ptMousePos.x - LAYER_HEAD_WIDTH + GetScrollPos(SB_HORZ) + FRAME_WIDTH / 2) / FRAME_WIDTH;
			ClipObjMap::iterator iter = m_mapClipObjs.find(*m_nSelectIDs.begin());
			if (iter != m_mapClipObjs.end())
			{
				if (mouseX <= iter->second.startFrame)
					mouseX = iter->second.startFrame + 1;
				if (mouseX > m_nMaxFrame)
					mouseX = m_nMaxFrame;
				ClipObj oldObj = iter->second;
				iter->second.endFrame = mouseX;
				if (m_pNotify)
				{
					if (!m_pNotify->OnModify(iter->second))
					{
						iter->second = oldObj;
					}
				}
			}
		}
		Invalidate(FALSE);
		break;
	case MOUSE_DOWN:
		if (IsCTRLPressed())
		{
			DWORD selectedID;
			switch (CheckSelection(point, selectedID))
			{
			case CLICK_A_COMMON_CLIP:
				if (m_pNotify)
				{
					if (m_pNotify->OnSelectedItem(selectedID))
					{
						m_nSelectIDs.insert(selectedID);
					}
				} else {
					m_nSelectIDs.insert(selectedID);
				}
				break;
			case CLICK_A_SELECTED_CLIP:
				if (m_pNotify)
				{
					if (m_pNotify->OnUnSelectedItem(selectedID))
					{
						m_nSelectIDs.erase(selectedID);
					}
				} else {
					m_nSelectIDs.erase(selectedID);
				}
				break;
			case CLICK_EMPTY_AREA:
				break;
			}
			Invalidate(FALSE);
		}
		break;
	default:
		break;
	}
	m_mouseState = MOUSE_NORMAL;
	CDialog::OnLButtonUp(nFlags, point);
}

CTimeBarCtrl::CLICK_STATE CTimeBarCtrl::CheckSelection(const CPoint &point, DWORD &selectedID)
{
	CRect rect = m_rectClientRect;
	rect.left = LAYER_HEAD_WIDTH;
	int width = rect.Width();
	int height = rect.Height() - HEAD_HEIGHT;
	int scrX = GetScrollPos(SB_HORZ);
	int scrY = GetScrollPos(SB_VERT);
	bool hasSepar = false;
	int separatorPos = (m_nLayerSeparatorPos + 1) * LINE_HEIGHT;
	if (m_nLayerSeparatorPos >= -1)
	{
		if (separatorPos + SPAR_HEIGHT <= scrY)
		{
			scrY -= SPAR_HEIGHT;	

		} else if (separatorPos >= scrY + height) {


		} else {
			hasSepar = true;
		}
	}
	int adjX = scrX % FRAME_WIDTH;
	int adjY = scrY % LINE_HEIGHT;

	int beginFrame = scrX / FRAME_WIDTH;
	int endFrame = beginFrame + width / FRAME_WIDTH;
	if (endFrame > m_nMaxFrame)
		endFrame = m_nMaxFrame;
	int beginLayer = scrY / LINE_HEIGHT;
	int endLayer = beginLayer + height / LINE_HEIGHT;
	if (endLayer > m_nLayerCount)
		endLayer = m_nLayerCount;
	ClipObjMap::iterator objIter = m_mapClipObjs.begin();
	for (;objIter!=m_mapClipObjs.end();++objIter)
	{
		if (objIter->second.endFrame > beginFrame && objIter->second.startFrame <= endFrame)
		{
			if (objIter->second.layerID >= beginLayer && objIter->second.layerID <= endLayer)
			{
				rect.left = (objIter->second.startFrame - beginFrame) * FRAME_WIDTH - adjX + LAYER_HEAD_WIDTH;
				rect.top = (objIter->second.layerID - beginLayer) * LINE_HEIGHT - adjY + 1 + HEAD_HEIGHT;
				if (hasSepar && objIter->second.layerID > m_nLayerSeparatorPos)
				{
					rect.top += SPAR_HEIGHT;
				}
				rect.right = rect.left + (objIter->second.endFrame - objIter->second.startFrame) * FRAME_WIDTH;
				rect.bottom = rect.top + LINE_HEIGHT - 1;
				if (rect.PtInRect(point))
				{
					m_ptSelectObjPos.x = rect.left;
					m_ptSelectObjPos.y = rect.top;
					selectedID = objIter->first;
					if (m_nSelectIDs.find(selectedID) == m_nSelectIDs.end())
					{
						return CLICK_A_COMMON_CLIP;
					} else {
						return CLICK_A_SELECTED_CLIP;
					}
				}

			}
		}
	}
	return CLICK_EMPTY_AREA;
}

void CTimeBarCtrl::OnLButtonDown(UINT nFlags, CPoint point)
{
	SetCapture();
	m_ptMouseDownPos = point;
	CRect rect = m_rectClientRect;
	rect.left = LAYER_HEAD_WIDTH;
	if (rect.PtInRect(point))
	{
		switch (m_mouseState)
		{
		case MOUSE_ON_MAX_FRAME_LINE:
			m_mouseState = MOUSE_DRAGING_MAX_FRAME;
			return;
			break;
		case MOUSE_ON_SELECTED_OBJ_END:
			m_mouseState = MOUSE_DRAGING_SELECTED_OBJ_END;
			return;
			break;
		case MOUSE_ON_PLAYPOS:
			m_mouseState = MOUSE_DRAGING_PLAYPOS;
			return;
			break;
		default:
			if (!IsCTRLPressed())
			{
				DWORD selectedID;
				switch (CheckSelection(point, selectedID))
				{
				case CLICK_A_COMMON_CLIP:
					if (m_pNotify)
					{
						if (m_pNotify->OnUnSelectedAllItem())
						{
							m_nSelectIDs.clear();
							if (m_pNotify->OnSelectedItem(selectedID))
							{
								m_nSelectIDs.insert(selectedID);
							}
						}
					} else {
						m_nSelectIDs.clear();
						m_nSelectIDs.insert(selectedID);
					}
					break;
				case CLICK_A_SELECTED_CLIP:
					break;
				case CLICK_EMPTY_AREA:
					if (m_pNotify)
					{
						if (m_pNotify->OnUnSelectedAllItem())
						{
							m_nSelectIDs.clear();
						}
					} else {
						m_nSelectIDs.clear();
					}
					break;
				}
				Invalidate(FALSE);
			}
			break;
		}
	}
	m_mouseState = MOUSE_DOWN;

	CDialog::OnLButtonDown(nFlags, point);
}

void CTimeBarCtrl::DrawAnObj(const ClipObj &obj, CDC* pDC, int scroll)
{

	CRect rect;
	rect.left = 0;
	rect.top = 0;
	if (m_mouseState == MOUSE_DRAGING_SELECTED_OBJ_END && obj.objID == *m_nSelectIDs.begin())
	{
		int mouseX = (m_ptMousePos.x - LAYER_HEAD_WIDTH + GetScrollPos(SB_HORZ) + FRAME_WIDTH / 2) / FRAME_WIDTH;
		if (mouseX <= obj.startFrame)
			mouseX = obj.startFrame + 1;
		if (mouseX > m_nMaxFrame)
			mouseX = m_nMaxFrame;
		rect.right = (mouseX - obj.startFrame) * FRAME_WIDTH;
	} else {
		rect.right = (obj.endFrame - obj.startFrame) * FRAME_WIDTH;
	}
	rect.right += scroll;
	rect.bottom = LINE_HEIGHT - 1;
	pDC->Rectangle(&rect);
	rect.left = scroll + 1;
	rect.right -= scroll + 1;
	pDC->DrawText(obj.name.data(), &rect, DT_VCENTER|DT_LEFT|DT_SINGLELINE);
}

void CTimeBarCtrl::DrawObjs(CDC* pDC)
{
	int width = m_rectClientRect.Width() - LAYER_HEAD_WIDTH;
	int height = m_rectClientRect.Height() - HEAD_HEIGHT;
	int scrX = GetScrollPos(SB_HORZ);
	int scrY = GetScrollPos(SB_VERT);
	bool hasSepar = false;
	if (m_nLayerSeparatorPos >= -1)
	{
		if ((m_nLayerSeparatorPos + 1) * LINE_HEIGHT + SPAR_HEIGHT <= scrY)
		{
			scrY -= SPAR_HEIGHT;	

		} else if ((m_nLayerSeparatorPos + 1) * LINE_HEIGHT >= scrY + height) {


		} else {
			hasSepar = true;
		}
	}
	int adjX = scrX % FRAME_WIDTH;
	int adjY = scrY % LINE_HEIGHT;

	int beginFrame = scrX / FRAME_WIDTH;
	int endFrame = beginFrame + width / FRAME_WIDTH;
	if (endFrame > m_nMaxFrame)
		endFrame = m_nMaxFrame;
	int beginLayer = scrY / LINE_HEIGHT;
	int endLayer = beginLayer + height / LINE_HEIGHT;
	if (endLayer > m_nLayerCount)
		endLayer = m_nLayerCount;
	pDC->SetViewportOrg(LAYER_HEAD_WIDTH, HEAD_HEIGHT);


	CBitmap	objBitmap;
	objBitmap.CreateCompatibleBitmap(pDC, width + LAYER_HEAD_WIDTH, LINE_HEIGHT - 1);
	if(!objBitmap.m_hObject)
		return;

	CDC objDC;
	objDC.CreateCompatibleDC(pDC);
	if(!objDC.m_hDC)
		return;
	objDC.SelectObject(&objBitmap);
	objDC.SetTextColor(textColor);
	CPen pen(PS_INSIDEFRAME, 1, lineColor);
	CBrush brush(frontColor);
	CBrush selectBrush(selectColor);
	CBrush specBrush(specObjColor);
	objDC.SelectObject(&pen);
	objDC.SelectObject(&brush);
	objDC.SetBkMode(TRANSPARENT);
	ClipObjMap::iterator objIter = m_mapClipObjs.begin();
	int x, y, objW, objH, ry;
	for (;objIter!=m_mapClipObjs.end();++objIter)
	{
		if (objIter->second.endFrame > beginFrame && objIter->second.startFrame <= endFrame)
		{
			if (objIter->second.layerID >= beginLayer && objIter->second.layerID <= endLayer)
			{
				if (m_nSelectIDs.find(objIter->first) != m_nSelectIDs.end())
				{
					if (m_mouseState == MOUSE_DRAGING_OBJ)
						continue;
					objDC.SelectObject(&selectBrush);
				} else if (objIter->second.isSpecObj) {
					objDC.SelectObject(&specBrush);
				}
				x = (objIter->second.startFrame - beginFrame) * FRAME_WIDTH - adjX;
				y = (objIter->second.layerID - beginLayer) * LINE_HEIGHT - adjY + 1;
				DrawAnObj(objIter->second, &objDC, x>=0?0:x);
				if (m_nSelectIDs.find(objIter->first) != m_nSelectIDs.end() || objIter->second.isSpecObj)
				{
					objDC.SelectObject(&brush);
				}

				if (hasSepar && objIter->second.layerID > m_nLayerSeparatorPos)
				{
					y += SPAR_HEIGHT;
				}
				if (m_mouseState == MOUSE_DRAGING_SELECTED_OBJ_END && m_nSelectIDs.find(objIter->first) != m_nSelectIDs.end())
				{
					int mouseX = (m_ptMousePos.x - LAYER_HEAD_WIDTH + GetScrollPos(SB_HORZ) + FRAME_WIDTH / 2) / FRAME_WIDTH;
					if (mouseX <= objIter->second.startFrame)
						mouseX = objIter->second.startFrame + 1;
					if (mouseX > m_nMaxFrame)
						mouseX = m_nMaxFrame;
					objW = (mouseX - objIter->second.startFrame) * FRAME_WIDTH;
				} else {
					objW = (objIter->second.endFrame - objIter->second.startFrame) * FRAME_WIDTH;
				}
				objH = LINE_HEIGHT - 1;
				if (x < 0)
				{
					objW += x;
					x = 0;
				}
				if (objW > width)
				{
					objW = width;
				}
				ry = 0;
				if (y < 0)
				{
					objH += y - 1;
					ry -= y - 1;
					y = 1;
				}
				if (y + objH > height)
				{
					objH = height - y;
				}
				pDC->BitBlt(x, y, objW, objH, &objDC, 0, ry, SRCCOPY);
			}
		}
	}
	pDC->SetViewportOrg(0, 0);
	if (m_mouseState == MOUSE_DRAGING_OBJ)
	{
		objIter = m_mapClipObjs.find(*m_nSelectIDs.begin());
		if (objIter != m_mapClipObjs.end())
		{
			int x = m_ptMousePos.x - m_ptMouseDownPos.x + m_ptSelectObjPos.x;
			int objWidth = (objIter->second.endFrame - objIter->second.startFrame) * FRAME_WIDTH;
			objDC.SelectObject(&selectBrush);
			DrawAnObj(objIter->second, &objDC, x>=0?0:x);
			objDC.SelectObject(&brush);
			if (x < 0)
			{
				objWidth += x;
				x = 0;
			}
			pDC->BitBlt(x, m_ptMousePos.y - m_ptMouseDownPos.y + m_ptSelectObjPos.y, objWidth, LINE_HEIGHT - 1, &objDC, 0, 0, SRCCOPY);
		}
	}
	objDC.SelectStockObject(NULL_PEN);
	objDC.SelectStockObject(NULL_BRUSH);
}

void CTimeBarCtrl::DrawGird(CDC* pDC)
{
	int width = m_rectClientRect.Width();
	int height = m_rectClientRect.Height();

	CBrush brush(bgColor);
	pDC->FillRect(&m_rectClientRect, &brush);

	CRect rect;
	rect.left = rect.top = 0;
	rect.right = width;
	rect.bottom = HEAD_HEIGHT;

	brush.DeleteObject();
	brush.CreateSolidBrush(frontColor);
	pDC->FillRect(&rect, &brush);
	CPen pen(PS_SOLID, 1, lineColor);
	pDC->SelectObject(&pen);
	pDC->MoveTo(0, 0);
	pDC->LineTo(width, 0);
	pDC->MoveTo(0, HEAD_HEIGHT);
	pDC->LineTo(width, HEAD_HEIGHT);
	pDC->MoveTo(0,0);
	pDC->LineTo(0, height);

	pDC->MoveTo(LAYER_HEAD_WIDTH, 0);
	pDC->LineTo(LAYER_HEAD_WIDTH, height);

	pDC->SetViewportOrg(0, HEAD_HEIGHT);
	height -= HEAD_HEIGHT;
	int scrollPosY = GetScrollPos(SB_VERT);
	int i;
	int y = -scrollPosY;
	CString text;

	CBitmap	TextBitmap;
	TextBitmap.CreateCompatibleBitmap(pDC, LAYER_HEAD_WIDTH * 2, LINE_HEIGHT);
	if(!TextBitmap.m_hObject)
		return;

	CDC textDC;
	textDC.CreateCompatibleDC(pDC);
	if(!textDC.m_hDC)
		return;
	textDC.SelectObject(&TextBitmap);
	textDC.SetBkMode(TRANSPARENT);
	textDC.SetTextColor(textColor);
	brush.DeleteObject();
	brush.CreateSolidBrush(bgColor);

	if (m_mouseState == MOUSE_DRAGING_SPARE)
	{
		if ((y + HEAD_HEIGHT - LINE_HEIGHT / 2 < m_ptMousePos.y) && (y + HEAD_HEIGHT + LINE_HEIGHT / 2 > m_ptMousePos.y))
		{
			CPen tmpPen(PS_DOT, 3, spareColor);
			pDC->SelectObject(&tmpPen);
			pDC->MoveTo(0, y);
			pDC->LineTo(width, y);
			pDC->SelectObject(&pen);
		}
	}

	for (i=0;i<=m_nLayerCount;++i)
	{
		if (m_nLayerSeparatorPos == i-1)
		{
			rect.right = width;
			rect.top = y + 1;
			rect.left = 1;
			y+= SPAR_HEIGHT;
			rect.bottom = y;
			if (y > 0)
			{
				if (rect.top <= 0)
				{
					rect.top = 1;
				}
				brush.DeleteObject();
				if (m_mouseState == MOUSE_DRAGING_SPARE)
				{
					brush.CreateSolidBrush(RGB(255,255,255) - spareColor);
				} else {
					brush.CreateSolidBrush(spareColor);
				}
				pDC->FillRect(&rect, &brush);
				pDC->MoveTo(0, y);
				pDC->LineTo(width, y);
				brush.DeleteObject();
				brush.CreateSolidBrush(bgColor);
			}
		}
		y += LINE_HEIGHT;
		if (y > 0 && i < m_nLayerCount)
		{
			text.Format(_T("%d"), i);
			rect.left = 0;
			rect.right = LAYER_HEAD_WIDTH;
			rect.top = 0;
			rect.bottom = LINE_HEIGHT;
			textDC.FillRect(&rect, &brush);
			textDC.DrawText(text, &rect, DT_VCENTER|DT_CENTER|DT_SINGLELINE);
			pDC->TransparentBlt(0, y-LINE_HEIGHT>0?y-LINE_HEIGHT:1, LAYER_HEAD_WIDTH, y-LINE_HEIGHT>0?LINE_HEIGHT:y-1, 
				&textDC, 0, y-LINE_HEIGHT>0?0:1+LINE_HEIGHT-y, LAYER_HEAD_WIDTH, y-LINE_HEIGHT>0?LINE_HEIGHT:y-1, bgColor);
			pDC->MoveTo(0, y);
			pDC->LineTo(width, y);
			if (m_mouseState == MOUSE_DRAGING_SPARE)
			{
				if ((y + HEAD_HEIGHT - LINE_HEIGHT / 2 < m_ptMousePos.y) && (y + HEAD_HEIGHT + LINE_HEIGHT / 2 > m_ptMousePos.y))
				{
					CPen tmpPen(PS_DOT, 3, spareColor);
					pDC->SelectObject(&tmpPen);
					pDC->LineTo(0, y);
					pDC->SelectObject(&pen);
				}
			}
		}
	}
	pDC->SetViewportOrg(LAYER_HEAD_WIDTH, 0);
	int scrollPosX = GetScrollPos(SB_HORZ);
	int adjX = scrollPosX % FRAME_WIDTH;
	i = scrollPosX / FRAME_WIDTH + 1;
	int x = i % 10;
	width -= LAYER_HEAD_WIDTH;
	rect.left = 0;
	rect.right = LAYER_HEAD_WIDTH * 2 ;
	rect.top = 0;
	rect.bottom = HEAD_HEIGHT;
	if (x > 0 && i - x >= 0)
	{
		text.Format(_T("%d"), i - x);
		textDC.FillRect(&rect, &brush);
		textDC.DrawText(text, &rect, DT_VCENTER|DT_LEFT|DT_SINGLELINE);
		x = - adjX - (x - 1) * FRAME_WIDTH;
		if (LAYER_HEAD_WIDTH * 2 + x > 0)
			pDC->TransparentBlt(1, 0, LAYER_HEAD_WIDTH * 2 + x, LINE_HEIGHT, &textDC, -x, 0, LAYER_HEAD_WIDTH * 2 + x, LINE_HEIGHT, bgColor);
	}
	x = FRAME_WIDTH - adjX;
	int mouseX = (m_ptMousePos.x - LAYER_HEAD_WIDTH + GetScrollPos(SB_HORZ) + FRAME_WIDTH / 2) / FRAME_WIDTH;
	while (x <= width)
	{
		if (i % 5 == 0)
		{
			y = HEAD_HEIGHT - 4;
			if (i % 10 == 0)
			{
				text.Format(_T("%d"), i);
				textDC.FillRect(&rect, &brush);
				textDC.DrawText(text, &rect, DT_VCENTER|DT_LEFT|DT_SINGLELINE);
				pDC->TransparentBlt(x+1, 0, LAYER_HEAD_WIDTH * 2, LINE_HEIGHT, &textDC, 0, 0, LAYER_HEAD_WIDTH * 2, LINE_HEIGHT, bgColor);
				y = HEAD_HEIGHT - 6;
			}
		} else {
			y = HEAD_HEIGHT - 2;
		}
		pDC->MoveTo(x, y);
		pDC->LineTo(x, HEAD_HEIGHT);
		if (i == m_nMaxFrame)
		{
			pen.DeleteObject();
			if (m_mouseState == MOUSE_DRAGING_MAX_FRAME)
			{
				pen.CreatePen(PS_SOLID, 1, RGB(255,255,255) - spareColor);
			} else {
				pen.CreatePen(PS_SOLID, 1, spareColor);
			}
			pDC->SelectObject(&pen);
			pDC->MoveTo(x, 0);
			pDC->LineTo(x, height + HEAD_HEIGHT);
			pen.DeleteObject();
			pen.CreatePen(PS_SOLID, 1, lineColor);
			pDC->SelectObject(&pen);
		}
		if (m_mouseState == MOUSE_DRAGING_MAX_FRAME)
		{
			if (mouseX == i)
			{
				CPen tmpPen(PS_DOT, 2, spareColor);
				pDC->SelectObject(&tmpPen);
				pDC->MoveTo(x, 0);
				pDC->LineTo(x, height + HEAD_HEIGHT);
				pDC->SelectObject(&pen);
			}
		}

		x += FRAME_WIDTH;
		++i;
	}
	pDC->SetViewportOrg(0, 0);
	textDC.DeleteDC( );
	TextBitmap.DeleteObject();
}

void CTimeBarCtrl::DrawPlayPos(CDC* pDC)
{
	int scrX = GetScrollPos(SB_HORZ);
	int height = m_rectClientRect.Height();
	int x = m_nPlayPos - GetScrollPos(SB_HORZ) + LAYER_HEAD_WIDTH;
	if (x >= LAYER_HEAD_WIDTH)
	{
		CPen pen(PS_SOLID, 1, playLineColor);
		pDC->SelectObject(&pen);
		pDC->MoveTo(x, 1);
		pDC->LineTo(x, height + 1);
		pDC->SelectStockObject(NULL_PEN);
	}
}

void CTimeBarCtrl::UpdateScrollPos()
{
	if (!playPosMustInScreen)
		return;
	int scrX = GetScrollPos(SB_HORZ);
	CRect rect;
	int width = m_rectClientRect.Width();
	int endPos;
	if (scrX > m_nPlayPos)
	{
		SetScrollPos(SB_HORZ, m_nPlayPos);
	} else {
		endPos = scrX + (width * 3 / 4);
		if (m_nPlayPos > endPos)
		{
			scrX += width / 2;
			SetScrollPos(SB_HORZ, scrX);
		}
	}

}

void CTimeBarCtrl::DrawCtrl(CDC* pDC)
{
	UpdateScrollPos();
	DrawGird(pDC);
	DrawObjs(pDC);
	DrawPlayPos(pDC);
}

void CTimeBarCtrl::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	TransScrollPos(SB_HORZ, nSBCode, nPos, FRAME_WIDTH);
	Invalidate(FALSE);
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CTimeBarCtrl::OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	TransScrollPos(SB_VERT, nSBCode, nPos, LINE_HEIGHT);
	Invalidate(FALSE);
	CDialog::OnVScroll(nSBCode, nPos, pScrollBar);
}

void CTimeBarCtrl::ResetScrollBar()
{
	if (!m_bInited)
		return;
	GetClientRect(&m_rectClientRect);
	SCROLLINFO info;
	int pos;
	info.cbSize = sizeof(SCROLLINFO);
	info.fMask = SIF_ALL;
	info.nPage = m_rectClientRect.Height() - HEAD_HEIGHT;
	info.nMax = LINE_HEIGHT * m_nLayerCount;
	if (m_nLayerSeparatorPos >= -1 && m_nLayerSeparatorPos <= m_nLayerCount + 1)
	{
		info.nMax += SPAR_HEIGHT;
	}
	info.nMin = 0;
	pos = GetScrollPos(SB_VERT);
	if (pos > info.nMax)
		pos = info.nMax;
	info.nPos = pos;
	SetScrollInfo(SB_VERT, &info);


	info.nMax = FRAME_WIDTH * m_nMaxFrame;
	info.nPage = m_rectClientRect.Width() - LAYER_HEAD_WIDTH;
	pos = GetScrollPos(SB_HORZ);

	if (pos > info.nMax)
		pos = info.nMax;
	info.nPos = pos;
	SetScrollInfo(SB_HORZ, &info);
	Invalidate(FALSE);
}
void CTimeBarCtrl::TransScrollPos(int nBar, UINT nSBCode, UINT nPos, int step)
{
	SCROLLINFO info;
	GetScrollInfo(nBar, &info, SIF_ALL);  
	switch(nSBCode)  
	{  
	case   SB_PAGEUP:  

		info.nPos -= info.nPage;
		break;  
	case   SB_PAGEDOWN:  

		info.nPos += info.nPage;
		break;  
	case   SB_BOTTOM:  

		info.nPos = info.nMax;
		break;  
	case   SB_LINEDOWN:  

		info.nPos += step;
		break;  
	case   SB_LINEUP:  

		info.nPos -= step;
		break;  
	case   SB_TOP: 
		info.nPos = info.nMin;

		break;  
	case   SB_THUMBTRACK:  
		info.nPos = info.nTrackPos;
		break;
	case   SB_THUMBPOSITION:  

		info.nPos = nPos;  
		break;  
	}  
	info.fMask = SIF_POS;
	SetScrollInfo(nBar, &info);
}

void CTimeBarCtrl::OnPaint()
{
	CPaintDC dc(this); 

	if (!m_bInited)
	{
		m_bInited = true;
		ResetScrollBar();
	}
	int width = m_rectClientRect.Width();
	int height = m_rectClientRect.Height();
	CBitmap	ShowBitmap;
	ShowBitmap.CreateCompatibleBitmap(&dc, width, height);
	if(!ShowBitmap.m_hObject)
		return;

	CDC memDC;
	memDC.CreateCompatibleDC(&dc);
	if(!memDC.m_hDC)
		return;
	memDC.SelectObject(&ShowBitmap);
	memDC.SetBkMode(TRANSPARENT);
	DrawCtrl(&memDC);

	dc.BitBlt(0, 0, width, height, &memDC, 0, 0, SRCCOPY);
	memDC.DeleteDC( );
	ShowBitmap.DeleteObject();
}

bool CTimeBarCtrl::DeleteAnObj(DWORD objId)
{
	if (m_mapClipObjs.erase(objId) > 0)
	{
		Invalidate(FALSE);
		return true;
	} else {
		return false;
	}
}




bool CTimeBarCtrl::ModifyAnObj(const ClipObj& obj)
{
	ClipObjMap::iterator iter = m_mapClipObjs.find(obj.objID);
	if (iter == m_mapClipObjs.end())
		return false;
	iter->second = obj;
	return true;
}

bool CTimeBarCtrl::AddAnObj(const ClipObj& obj)
{
	if (m_mapClipObjs.find(obj.objID) != m_mapClipObjs.end())
		return false;
	m_mapClipObjs[obj.objID] = obj;
	if (m_bInited)
		Invalidate(FALSE);
	return true;
}

bool CTimeBarCtrl::RemoveAll()
{
	m_mapClipObjs.clear();
	return true;
}

void CTimeBarCtrl::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

CTimeBarCtrl::~CTimeBarCtrl()
{
}

CTimeBarCtrl::CTimeBarCtrl(CWnd* pParent /*=NULL*/)
: CDialog(CTimeBarCtrl::IDD, pParent), m_nLayerCount(10), m_nLayerSeparatorPos(4), bgColor(RGB(255,255,255)), 
lineColor(RGB(0,0,0)), frontColor(RGB(212,208,200)), textColor(RGB(0,0,0)), selectColor(RGB(28, 128, 28)), spareColor(RGB(255,0,0)), 
specObjColor(RGB(0, 252, 228)), playLineColor(RGB(0, 255, 0)), m_bInited(false),m_mouseState(MOUSE_NORMAL), m_nMaxFrame(200),
m_pNotify(NULL), canCross(true), playPosMustInScreen(false), mousePlayPosRange(4)
{
	m_mapClipObjs.clear();
}

