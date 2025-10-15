

#include "stdafx.h"
#include "resource.h"
#include "ColorBarCtrl.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


namespace colorbarctrl
{

	IMPLEMENT_DYNAMIC(CColorBarCtrl, CDialog)


		BEGIN_MESSAGE_MAP(CColorBarCtrl, CDialog)
			ON_WM_SIZING()
			ON_WM_SIZE()
			ON_WM_PAINT()
			ON_WM_MOUSEMOVE()
			ON_WM_LBUTTONUP()
			ON_WM_LBUTTONDOWN()
		END_MESSAGE_MAP()



		void CColorBarCtrl::OnLButtonDown(UINT nFlags, CPoint point)
	{
		SetCapture();
		m_bIsMouseDown = true;
		m_ptMouseDownPoint = point;
		int tmpX = point.x - m_nSilderWidth / 2 - 1;

		m_nSelectedId = -1;
		if (m_rectControlButtonArea.PtInRect(point))
		{
			Colors::iterator iter = m_Colors.begin();
			int x;
			for (;iter!=m_Colors.end();++iter)
			{
				x = m_nWidth * iter->first / m_nMaxX;
				if (point.x > x && point.x <= x + m_nSilderWidth)
				{
					m_nSelectedId = iter->first;
					break;
				}
			}
		}
		if (m_nSelectedId == -1 && tmpX >= 0 && tmpX < m_nWidth)
		{
			int pos = tmpX * m_nMaxX / m_nWidth;
			if (m_Colors.find(pos) == m_Colors.end())
			{
				Colors::iterator iter = m_Colors.begin();
				if (iter != m_Colors.end())
				{
					COLORREF begin = iter->second;
					COLORREF end = begin;
					int left = iter->first;
					int right = m_nMaxX;
					++iter;
					for (;iter!=m_Colors.end();++iter)
					{
						right = iter->first;
						end = iter->second;
						if (iter->first > pos)
							break;
						left = iter->first;
						begin = iter->second;
					}
					if (iter == m_Colors.end())
					{
						m_Colors[pos] = begin;
					} else {
						m_Colors[pos] = InterpolationColor(begin, end, pos - left, right - left);
					}
					Invalidate(FALSE);
				}
			}
		}

		CDialog::OnLButtonDown(nFlags, point);
	}

	void CColorBarCtrl::OnLButtonUp(UINT nFlags, CPoint point)
	{
		ReleaseCapture();
		if (m_bIsMouseDown)
		{
			m_bIsMouseDown = false;
			if (m_nSelectedId != -1)
			{
				if (point == m_ptMouseDownPoint)
				{
					Colors::iterator iter = m_Colors.find(m_nSelectedId);
					if (iter != m_Colors.end())
					{
						CColorDialog dlg(iter->second);
						if (dlg.DoModal() == IDOK)
						{
							iter->second = dlg.GetColor();
							Invalidate(FALSE);
						}
					}					
				} else if (m_nSelectedId > 0)
				{
					m_bSelectedWillBeDelete = false;
					int tmpX = point.x - m_nSilderWidth / 2 - 1;
					if (!m_rectControlButtonArea.PtInRect(point))
					{
						m_Colors.erase(m_nSelectedId);
						m_nSelectedId = -1;
						Invalidate(FALSE);
					} else {
						int pos = tmpX * m_nMaxX / m_nWidth;
						if (pos < 0)
							pos = 0;
						if (pos >= m_nMaxX)
							pos = m_nMaxX - 1;
						Colors::iterator iter = m_Colors.find(m_nSelectedId);
						if (iter != m_Colors.end() && m_Colors.find(pos) == m_Colors.end())
						{
							COLORREF color = iter->second;
							m_Colors.erase(iter);
							m_Colors[pos] = color;
							m_nSelectedId = pos;
							Invalidate(FALSE);
						}
					}
				}
			}
		}
		CDialog::OnLButtonUp(nFlags, point);
	}

	void CColorBarCtrl::OnMouseMove(UINT nFlags, CPoint point)
	{
		if (m_bIsMouseDown && m_nSelectedId > 0)
		{
			m_bSelectedWillBeDelete = false;
			int tmpX = point.x - m_nSilderWidth / 2 - 1;
			if (!m_rectControlButtonArea.PtInRect(point))
			{
				m_bSelectedWillBeDelete = true;
				Invalidate(FALSE);
			} else {
				int pos = tmpX * m_nMaxX / m_nWidth;
				if (pos < 0)
					pos = 0;
				if (pos >= m_nMaxX)
					pos = m_nMaxX - 1;
				Colors::iterator iter = m_Colors.find(m_nSelectedId);
				if ((iter != m_Colors.end() && m_Colors.find(pos) == m_Colors.end()) || pos == m_nSelectedId)
				{					
					COLORREF color = iter->second;
					m_Colors.erase(iter);
					m_Colors[pos] = color;
					m_nSelectedId = pos;
					Invalidate(FALSE);
				}
			}
		}
		CDialog::OnMouseMove(nFlags, point);
	}

	void CColorBarCtrl::OnPaint()
	{
		CPaintDC dc(this); 
		int width = m_rect.Width();
		int height = m_rect.Height();
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

	void CColorBarCtrl::OnSize(UINT nType, int cx, int cy)
	{
		CDialog::OnSize(nType, cx, cy);
		ResetSize();
	}

	void CColorBarCtrl::OnSizing(UINT fwSide, LPRECT pRect)
	{
		CDialog::OnSizing(fwSide, pRect);
		ResetSize();
	}

	void CColorBarCtrl::DrawCtrl(CDC* pDC)
	{
		CBrush brush(bgColor);
		pDC->FillRect(&m_rect, &brush);
		pDC->SetViewportOrg(m_nSilderWidth / 2, 0);
		DrawColorBar(pDC);
		pDC->SetViewportOrg(m_nSilderWidth / 2 + 1, m_rectColorBar.bottom - 1);
		DrawControlButton(pDC);
		pDC->SetViewportOrg(0, 0);

	}

	void CColorBarCtrl::DrawControlButton(CDC* pDC)
	{
		CPen darkPen(PS_SOLID, 1, darkLineColor);
		CPen lightPen(PS_SOLID, 1, lightLineColor);
		CPen linePen(PS_SOLID, 1, bgColor);
		CBrush brush;
		Colors::iterator iter = m_Colors.begin();
		int x;
		CRect rect;
		for (;iter!=m_Colors.end();++iter)
		{
			if (iter->first == m_nSelectedId && m_bSelectedWillBeDelete)
				continue;
			x = m_nWidth * iter->first / m_nMaxX;
			pDC->MoveTo(x, 0);
			pDC->SelectObject(&lightPen);
			pDC->LineTo(x-m_nSilderWidth / 2 - 1, m_nSilderWidth / 2 + 1);
			pDC->LineTo(x-m_nSilderWidth / 2 - 1, m_nSilderHeight);
			pDC->SelectObject(&darkPen);
			pDC->LineTo(x+m_nSilderWidth / 2 + 1, m_nSilderHeight);
			pDC->LineTo(x+m_nSilderWidth / 2 + 1, m_nSilderWidth / 2 + 1);
			pDC->LineTo(x, 0);
			pDC->SelectObject(&linePen);
			brush.DeleteObject();
			brush.CreateSolidBrush(iter->second);
			pDC->SelectObject(&brush);
			rect.left = x-m_nSilderWidth / 2;
			rect.right = x+m_nSilderWidth / 2 + 1;
			rect.top = m_nSilderWidth / 2 + 1;
			rect.bottom = m_nSilderHeight;
			pDC->Rectangle(&rect);

		}
	}

	void CColorBarCtrl::DrawColorBar(CDC* pDC)
	{
		CPen pen(PS_SOLID, 1, lineColor);
		CBrush brush(bgColor);
		int left, right, now, width;
		pDC->SelectObject(&pen);
		pDC->SelectStockObject(NULL_BRUSH);
		CRect rect = m_rectColorBar;
		rect.top += 20;
		int i, y;
		CString str;
		pDC->SetTextAlign(TA_RIGHT|TA_TOP);
		for (i=0;i<m_nMaxX;i+=unitX)
		{
			left = m_nWidth * i / m_nMaxX + 1;
			pDC->MoveTo(left, rect.top);
			switch (i % 10)
			{
			case 0:
				y = rect.top - 7;
				str.Format(L"%d", i);
				if (i > 0)
				{
					pDC->TextOut(left - 1, y - 10, str);
				}
				break;
			case 5:
				y = rect.top - 5;
				break;
			default:
				y = rect.top - 3;
				break;
			}
			pDC->LineTo(left, y);
		}
		pDC->Rectangle(&rect);
		rect.top += 1;
		rect.bottom -= 1;
		COLORREF begin, end;
		Colors::iterator iter = m_Colors.begin();
		if (iter != m_Colors.end())
		{
			left = iter->first;
			begin = iter->second;
			rect.left = m_nWidth * left / m_nMaxX + 1;
			end = begin;
			++iter;
			for (;iter!=m_Colors.end();++iter)
			{
				if (iter->first == m_nSelectedId && m_bSelectedWillBeDelete)
					continue;
				right = iter->first;
				end = iter->second;
				width = right - left;
				for (now=0;now<width;++now)
				{
					brush.DeleteObject();
					brush.CreateSolidBrush(InterpolationColor(begin,end,now,width));
					rect.right = m_nWidth * (left + now + 1) / m_nMaxX + 1;
					if (rect.right > rect.left)
					{
						pDC->FillRect(&rect, &brush);
						rect.left = rect.right;
					}
				}
				left = right;
				begin = end;
				rect.left=rect.right;
			}
			brush.DeleteObject();
			brush.CreateSolidBrush(end);
			rect.right = m_rectColorBar.right - 1;
			pDC->FillRect(&rect, &brush);
		}
	}

	COLORREF CColorBarCtrl::InterpolationColor(COLORREF begin, COLORREF end, int now, int width)
	{

		return RGB((GetRValue(end) - GetRValue(begin)) * now / width + GetRValue(begin),
			(GetGValue(end) - GetGValue(begin)) * now / width + GetGValue(begin),
			(GetBValue(end) - GetBValue(begin)) * now / width + GetBValue(begin));
	}

	void CColorBarCtrl::CheckColor()
	{
		Colors::iterator iter = m_Colors.begin();
		if (iter == m_Colors.end())
		{
			m_Colors[0] = RGB(255, 255, 255);
		} else {
			bool first = true;
			for (;iter!=m_Colors.end();)
			{
				if (first)
				{
					if (iter->first > 0) { m_Colors[0] = iter->second; }
					first = false;
				}
				if (iter->first >= m_nMaxX)
				{
					m_Colors.erase(iter++);
				} else {
					++iter;
				}
			}
		}
	}

	void CColorBarCtrl::SetMaxX(int maxX)
	{
		m_nMaxX = maxX;
		CheckColor();
	}

	void CColorBarCtrl::SetColors(const Colors &colors)
	{
		m_Colors = colors;
		CheckColor();
	}

	void CColorBarCtrl::ResetSize()
	{
		GetClientRect(&m_rect);
		m_nWidth = m_rect.Width() - m_nSilderWidth;
		m_rectColorBar.left = 0;
		m_rectColorBar.right = m_nWidth;
		m_nWidth -= 2;
		m_rectColorBar.top = 0;
		m_rectColorBar.bottom = m_rect.bottom - m_nSilderHeight - 1;
		m_rectControlButtonArea.left = 0;
		m_rectControlButtonArea.right = m_rect.right;
		m_rectControlButtonArea.top = m_rectColorBar.bottom + m_nSilderWidth / 2;
		m_rectControlButtonArea.bottom = m_rectColorBar.bottom + m_nSilderHeight;
	}

	void CColorBarCtrl::DoDataExchange(CDataExchange* pDX)
	{
		CDialog::DoDataExchange(pDX);
	}

	CColorBarCtrl::~CColorBarCtrl()
	{
	}

	CColorBarCtrl::CColorBarCtrl(CWnd* pParent /*=NULL*/)
		: CDialog(CColorBarCtrl::IDD, pParent)
	{
		m_nMaxX = 0;
		m_nSilderHeight = 13;
		m_nSilderWidth = 7;
		m_nSelectedId = -1;
		lineColor = RGB(0, 0, 0);
		lightLineColor = RGB(255, 255, 255);
		darkLineColor = RGB(128, 128, 128);
		bgColor = RGB(212, 208, 200);
		m_bIsMouseDown = false;
		m_bSelectedWillBeDelete = false;
		m_Colors[0] = RGB(255, 255, 255);
		unitX = 1;
	}


}


