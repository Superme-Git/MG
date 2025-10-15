// BezierControl.cpp : implementation file
//
#include "stdafx.h"
#include "resource.h"
#include "BezierControl.h"
#include <math.h>

#ifndef Round
#define Round(x) ( ((x) > 0.0) ? ( (int)((x) + 0.5) ) : ( -(int)(-(x) + 0.5)))
#endif

#define IsKeyPressed(KEY) ((GetAsyncKeyState(KEY) & (0x8000)) != 0) 
#define IsCTRLPressed() IsKeyPressed(VK_CONTROL)

// CBezierControl dialog
namespace bezier {

	IMPLEMENT_DYNAMIC(CBezierControl, CDialog)

	CBezierControl::CBezierControl(CWnd* pParent /*=NULL*/)
		: CDialog(CBezierControl::IDD, pParent)
	{
		m_pNotify = NULL;
		m_ptSelectPos.x = 0.0f;
		m_ptSelectPos.y = 0.0f;
		m_fMinX = 0.0f;
		m_fMaxX = 100.0f;
		unitX = 10.0f;
		m_fMinY = 0.0f;
		m_fMaxY = 50.0f;
		unitY = 5.0f;
		xMustInt = false;
		m_NearRadius.x = NEAR_RADIUS;
		m_NearRadius.y = NEAR_RADIUS;
		bgColor = RGB(212, 208, 200);
		axisColor = RGB(0, 0, 0);
		vectorCurveColor.clear();
		controlPointColor = RGB(0, 0, 0);
		tangentPointColor = RGB(0, 0, 0);
		selectedPointColor = RGB(0, 255, 0);
		sampleLineColor = RGB(255, 0, 0);
		selectRectColor = RGB(0, 0, 0);
		m_ToolsState = MOUSE_STATE_ARROW;
		m_WorkMode = WORK_MODE_CURVE;
		m_TPCTangentPoints.clear();
		m_selectedPoints.clear();
		m_nSampleCount = 0;
		m_SPCSamplePoints.clear();
		m_selectedSamplePoints.clear();
		m_bIsSamplePointWillBeDelete = false;
		m_bIsSamplesReady = false;
		m_bIsLButtonDown = false;
		m_editMode = EDIT_MODE_NORMAL;
		
		SetLineCount(1);
		
	}

	CBezierControl::~CBezierControl()
	{
	}

	void CBezierControl::DoDataExchange(CDataExchange* pDX)
	{
		CDialog::DoDataExchange(pDX);
		DDX_Text(pDX, IDC_BZ_EDIT_X, m_ptSelectPos.x);
		DDX_Text(pDX, IDC_BZ_EDIT_Y, m_ptSelectPos.y);
		DDX_Control(pDX, IDC_BZ_EDIT_X, m_EditX);
		DDX_Control(pDX, IDC_BZ_EDIT_Y, m_EditY);
		DDX_Control(pDX, IDC_BZ_X, m_StaticX);
		DDX_Control(pDX, IDC_BZ_Y, m_StaticY);
		DDX_Control(pDX, IDC_BZ_STATIC_FILTER, m_StaticFilter);
		DDX_Control(pDX, IDC_BZ_COMBO_FILTER, m_ComboFilter);
	}

	void CBezierControl::SetEditCtrlFromControlPoint()
	{
		if (m_selectedPoints.size() == 1)
		{
			m_ptSelectPos = m_selectedPoints.begin()->iter->point;
			m_EditX.EnableWindow(true);
			m_EditY.EnableWindow(true);
		} else {
			m_ptSelectPos.x = 0.0f;
			m_ptSelectPos.y = 0.0f;
			m_EditX.EnableWindow(false);
			m_EditY.EnableWindow(false);
		}
		UpdateData(FALSE);
	}

	void CBezierControl::SetControlPointFromEditCtrl()
	{
		if (m_selectedPoints.size() == 1)
		{
			UpdateData();
			FPoint adj = m_ptSelectPos - m_selectedPoints.begin()->oldState.point;
			MoveSelectedPoint(adj, true);
			ClearSamplePoints();
		}
	}

	void CBezierControl::SetSamplePointFromEditCtrl()
	{
		if (m_selectedSamplePoints.size() > 0)
		{
			UpdateData();
			AdjSelectedSamplePoint(m_ptSelectPos.x);
		}
	}

	void CBezierControl::SetEditCtrlFromSamplePoint()
	{
		if (m_selectedSamplePoints.size() > 0)
		{
			m_ptSelectPos = (*m_selectedSamplePoints.begin())->point;
			m_EditX.EnableWindow(true);
		} else {
			m_ptSelectPos.x = 0.0f;
			m_EditX.EnableWindow(false);
		}
		UpdateData(FALSE);
	}

	void CBezierControl::ResetSize()
	{
		if (m_wndToolbar.m_hWnd)
		{
			CRect rcClientTool;
			RepositionBars(AFX_IDW_CONTROLBAR_FIRST, AFX_IDW_CONTROLBAR_LAST, 0, reposQuery, &m_rectClient);
			rcClientTool.left = 0;
			rcClientTool.right = m_rectClient.right;
			rcClientTool.top = 0;
			rcClientTool.bottom = m_rectClient.top;
			m_wndToolbar.MoveWindow(&rcClientTool);
			m_rectClient.left += 2;
			m_rectClient.right -= 2;
			m_nClientWidth = m_rectClient.Width();
			m_nClientHeight = m_rectClient.Height();
			CRect rect;
			rect.top = 3;
			rect.bottom = m_rectClient.top - 1;
			rect.left = m_rectClient.right - 50;
			rect.right = m_rectClient.right;
			m_EditY.MoveWindow(&rect);
			rect.right = rect.left;
			rect.left -= 20;
			rect.top += 2;
			m_StaticY.MoveWindow(&rect);
			rect.right = rect.left;
			rect.left -= 50;
			rect.top -= 2;
			m_EditX.MoveWindow(&rect);
			rect.right = rect.left;
			rect.left -= 20;
			rect.top += 2;
			m_StaticX.MoveWindow(&rect);
			rect.right = rect.left;
			rect.left -= 60;
			rect.top -= 2;
			m_ComboFilter.MoveWindow(&rect);
			rect.right = rect.left;
			rect.left -= 50;
			rect.top += 2;
			m_StaticFilter.MoveWindow(&rect);

			InvalidateRect(&rcClientTool);
			InvalidateRect(&m_rectClient, FALSE);

			ResetNearRadius();
		}
	}
	
	void CBezierControl::ResetNearRadius()
	{
		m_NearRadius.x = NEAR_RADIUS;
		m_NearRadius.y = -NEAR_RADIUS;
		TransToLogicVector(m_NearRadius, m_NearRadius);
	}

	FPoint CBezierControl::Computer3BezierCurvePoint(float u, const BezierParam &p)
	{
		float coef0 = powf(1.0f-u, 3.0f);
		float coef1 = 3.0f * u * powf(1.0f-u, 2);
		float coef2 = 3 * powf(u, 2.0f) * (1-u);
		float coef3 = powf(u, 3.0f);

		FPoint pt;
		pt.x = coef0*p.p0->x + coef1*p.p1->x + coef2*p.p2->x + coef3*p.p3->x;
		pt.y = coef0*p.p0->y + coef1*p.p1->y + coef2*p.p2->y + coef3*p.p3->y;
		return pt;
	}

	bool CBezierControl::ComputerNear3BezierCurvePoint(float x, const BezierParam &p, float u1, float u2, float &u)
	{
		u = (u1 + u2) / 2;
		FPoint middle = Computer3BezierCurvePoint(u, p);
		if (fabs(middle.x - x) < 0.01)
			return true;
		if (fabs(u1 - u2) < EPSILON)
			return false;
		if (middle.x > x)
		{
			return ComputerNear3BezierCurvePoint(x, p, u1, u, u);
		} else {
			return ComputerNear3BezierCurvePoint(x, p, u, u2, u);
		}
	}

	bool CBezierControl::FindCtrlPoint(LineID lineID, int pointId, TangentPointContanter::iterator& outIter)
	{
		if (lineID >= m_TPCTangentPoints.size())
			return false;
		int iterId = 0;
		outIter = m_TPCTangentPoints[lineID].begin();
		TangentPointContanter::iterator iterEnd = m_TPCTangentPoints[lineID].end();
		while (iterId < pointId && outIter != iterEnd)
		{
			++iterId;
			++outIter;
		}
		if (outIter == iterEnd)
			return false;
		return true;
	}

	void CBezierControl::DrawCtrl(CDC* pDC)
	{
		DrawAxis(pDC);
		DrawCurve(pDC);
		if (m_WorkMode == WORK_MODE_CURVE)
		{
			DrawCtrlPoint(pDC);
			if (m_ToolsState == MOUSE_STATE_ARROW)
			{
				DrawSelectRect(pDC);
			}
		} else {
			DrawSamplePoint(pDC);
		}
	}

	void CBezierControl::DrawAxis(CDC* pDC)
	{
		CPen axisPen(PS_SOLID, 1, axisColor);
		FPoint ptInLogic, ptInClient;
		if (m_fMinX * m_fMaxX <= 0)
		{
			ptInLogic.x = 0;
		} else {
			ptInLogic.x = m_fMinX;
		}

		if (m_fMinY * m_fMaxY <= 0)
		{
			ptInLogic.y = 0;
		} else {
			ptInLogic.y = m_fMinY;
		}
		TransToClient(ptInLogic, ptInClient);
		pDC->SelectObject(&axisPen);
		pDC->MoveTo(static_cast<int>(ptInClient.x), 0);
		pDC->LineTo(static_cast<int>(ptInClient.x), m_nClientHeight);
		pDC->MoveTo(0, static_cast<int>(ptInClient.y));
		pDC->LineTo(m_nClientWidth, static_cast<int>(ptInClient.y));
		int i = 0;
		int obj;
		CString text;
		pDC->SetTextAlign(TA_BOTTOM);
		i = static_cast<int>(m_fMinX / unitX);
		ptInLogic.x = unitX * i;
		while (ptInLogic.x <= m_fMaxX)
		{
			TransToClient(ptInLogic, ptInClient);
			pDC->MoveTo(static_cast<int>(ptInClient.x), static_cast<int>(ptInClient.y));
			if ((i % 10) == 0)
			{
				obj = static_cast<int>(ptInClient.y) - 7;
				if (i != 0)
				{
					text.Format(L"%0.2f", ptInLogic.x);
					pDC->TextOut(static_cast<int>(ptInClient.x), obj + 3, text);
				}
			} else if ((i % 5) == 0)
			{
				obj = static_cast<int>(ptInClient.y) - 5;
			} else {
				obj = static_cast<int>(ptInClient.y) - 3;
			}
			pDC->LineTo(static_cast<int>(ptInClient.x), obj);
			ptInLogic.x += unitX;
			++i;
		}
		i = 0;
		if (m_fMinX * m_fMaxX <= 0)
		{
			ptInLogic.x = 0;
		} else {
			ptInLogic.x = m_fMinX;
		}
		ptInLogic.y = m_fMinY;
		i = static_cast<int>(m_fMinY / unitY);
		ptInLogic.y = unitY * i;
		while (ptInLogic.y <= m_fMaxY)
		{
			TransToClient(ptInLogic, ptInClient);
			pDC->MoveTo(static_cast<int>(ptInClient.x), static_cast<int>(ptInClient.y));
			if ((i % 10) == 0)
			{
				obj = static_cast<int>(ptInClient.x) + 7;
				if (i != 0)
				{
					text.Format(L"%0.2f", ptInLogic.y);
					pDC->TextOut(obj - 3, static_cast<int>(ptInClient.y), text);
				}
			} else if ((i % 5) == 0)
			{
				obj = static_cast<int>(ptInClient.x) + 5;
			} else {
				obj = static_cast<int>(ptInClient.x) + 3;
			}
			pDC->LineTo(obj, static_cast<int>(ptInClient.y));
			ptInLogic.y += unitY;
			++i;
		}
	}

	void CBezierControl::DrawCurve(CDC* pDC)
	{
		FPoint pt, point[4];
		COLORREF curveColor;
		TangentPointContanter::iterator iter;
		TangentPointContanter::iterator nextIter;
		TangentPointContanter::iterator last;
		float u, x;
		BezierParam param;
		TangentPointContanters::iterator lineIter = m_TPCTangentPoints.begin();
		size_t count = 0;
		for (;lineIter!=m_TPCTangentPoints.end();++lineIter,++count)
		{
			if (vectorCurveColor.size() > count)
			{
				curveColor = vectorCurveColor[count];
			} else {
				curveColor = RGB(0, 0, 0);
			}
			CPen pen(PS_SOLID, 1, curveColor);
			pDC->SelectObject(&pen);
			iter = lineIter->begin();
			nextIter = iter;
			++nextIter;
			last = lineIter->end();
			TransToClient(iter->point, point[0]);
			pDC->MoveTo(static_cast<int>(point[0].x), static_cast<int>(point[0].y));

			param.p0 = point;
			param.p1 = point + 1;
			param.p2 = point + 2;
			param.p3 = point + 3;
			for (;nextIter!=last;++iter,++nextIter)
			{
				TransToClient(iter->point, point[0]);
				TransToClient(iter->controlPt[CONTROL_TYPE_NEXT], point[1]);
				TransToClient(nextIter->controlPt[CONTROL_TYPE_PREV], point[2]);
				TransToClient(nextIter->point, point[3]);
				if ((point[0] == point[1]) && (point[2] == point[3]))
				{
					pDC->LineTo(static_cast<int>(point[3].x), static_cast<int>(point[3].y));
				} else {
					for (x=0;x<=100;x+=1)
					{
						u = x / 100;
						pt = Computer3BezierCurvePoint(u, param);
						pDC->LineTo(static_cast<int>(pt.x), static_cast<int>(pt.y));
					}
				}
			}
		}
	}

	void CBezierControl::DrawSamplePoint(CDC* pDC)
	{
		FPoint pt;
		CPen pen(PS_SOLID, 1, sampleLineColor);
		pDC->SelectObject(&pen);
		TangentPointContanters::iterator lineIter = m_TPCTangentPoints.begin();
		SelectedSamplePoints::iterator lineSelSpIter = m_selectedSamplePoints.begin();
		TangentPointContanter::iterator iter;
		TangentPointContanter::iterator nextIter;
		TangentPointContanter::iterator last;
		SamplePointContanter::iterator spIter;
		SamplePointContanter::iterator spLast;
		SamplePointContanter::iterator spLastOne;
		SamplePointContanter tmpSp;
		SamplePoint sp;
		CBrush selectBrush(selectedPointColor);
		CBrush normalBrush(bgColor);
		int id = 0;
		BezierParam param;
		bool addSample = false;
		LineID lineID = 0;
		float u;
		for (;lineIter!=m_TPCTangentPoints.end();++lineIter,++lineID)
		{
			if (m_SPCSamplePoints.size() > lineID)	//已经初始化了
			{
				spIter = m_SPCSamplePoints[lineID].begin();
				spLast = m_SPCSamplePoints[lineID].end();
				addSample = false;
			} else {
				spIter = m_SPCSamplePoints[0].begin();
				spLast = m_SPCSamplePoints[0].end();
				addSample = true;
				tmpSp.clear();
				u = 0.0f;
			}
			spLastOne = spLast;
			--spLastOne;
			iter = lineIter->begin();
			last = lineIter->end();
			nextIter = iter;
			++nextIter;
			id = 0;
			sp.id = id;
			TransToClient(iter->point, pt);
			pDC->SelectObject(&normalBrush);
			pDC->MoveTo(static_cast<int>(pt.x), static_cast<int>(pt.y));
			for (;spIter!=spLast;++spIter)
			{
				if (m_selectedSamplePoints.size() > 0)
				{
					if (spIter==(*lineSelSpIter) && m_bIsSamplePointWillBeDelete && 
						spIter != spLastOne && spIter != m_SPCSamplePoints[lineID].begin())
					{
						continue;
					}
				}
				if (addSample)
				{
					while (spIter->point.x > nextIter->point.x)
					{
						++iter;
						++nextIter;
						++id;
						sp.id = id;
						u = 0.0f;
					}
					param.p0 = &iter->point;
					param.p1 = &iter->controlPt[CONTROL_TYPE_NEXT];
					param.p2 = &nextIter->controlPt[CONTROL_TYPE_PREV];
					param.p3 = &nextIter->point;
					ComputerNear3BezierCurvePoint(spIter->point.x, param, u, 1.0f, u);
					sp.u = u;
					sp.point = Computer3BezierCurvePoint(u, param);
					sp.inited = true;
					TransToClient(sp.point, pt);
					tmpSp.push_back(sp);
				} else {
					while (spIter->id > id)
					{
						++iter;
						++nextIter;
						++id;
						///////////////////////
						//TransToClient(iter->point, pt);
						//pDC->Rectangle(static_cast<int>(pt.x) - 1, static_cast<int>(pt.y) - 1, 
						//	static_cast<int>(pt.x) + 2, static_cast<int>(pt.y) + 2);
						///////////////////////
					}
					if (!spIter->inited)
					{
						param.p0 = &iter->point;
						param.p1 = &iter->controlPt[CONTROL_TYPE_NEXT];
						param.p2 = &nextIter->controlPt[CONTROL_TYPE_PREV];
						param.p3 = &nextIter->point;
						spIter->point = Computer3BezierCurvePoint(spIter->u, param);
						if (xMustInt)
						{
							int x = Round(spIter->point.x);
							if (m_SPCSamplePoints.size() < m_TPCTangentPoints.size() && m_setHasSamplePoint.find(x) != m_setHasSamplePoint.end())
							{
								spIter = m_SPCSamplePoints[0].erase(spIter);
								--spIter;
								--m_nSampleCount;
								if (m_pNotify)
								{
									m_pNotify->OnSampleCount(m_nSampleCount);
								}
								continue;
							}
							m_setHasSamplePoint.insert(x);
							ComputerNear3BezierCurvePoint((float)x, param, 0.0f, 1.0f, spIter->u);
							spIter->point = Computer3BezierCurvePoint(spIter->u, param);
						}
						spIter->inited = true;
						SetEditCtrlFromSamplePoint();
					}
					TransToClient(spIter->point, pt);
				}
				pDC->LineTo(static_cast<int>(pt.x), static_cast<int>(pt.y));
			}
			if (addSample)
			{
				m_SPCSamplePoints.push_back(tmpSp);
				spLast = m_SPCSamplePoints[lineID].end();
				spLastOne = spLast;
				--spLastOne;
			}
			spIter = m_SPCSamplePoints[lineID].begin();
			for (;spIter!=spLast;++spIter)
			{
				TransToClient(spIter->point, pt);
				if ((m_selectedSamplePoints.size() > 0) && (spIter==(*lineSelSpIter)))
				{
					if (!m_bIsSamplePointWillBeDelete || spIter == spLastOne || spIter == m_SPCSamplePoints[lineID].begin())
					{
						pDC->SelectObject(&selectBrush);
						pDC->Ellipse(static_cast<int>(pt.x) - 2, static_cast<int>(pt.y) - 2, 
							static_cast<int>(pt.x) + 3, static_cast<int>(pt.y) + 3);
						pDC->SelectObject(&normalBrush);
					}
				} else {
					pDC->Ellipse(static_cast<int>(pt.x) - 2, static_cast<int>(pt.y) - 2, 
						static_cast<int>(pt.x) + 3, static_cast<int>(pt.y) + 3);
				}
			}
			if (m_selectedSamplePoints.size() > 0)
			{
				++lineSelSpIter;
			}
		}
		if (!m_bIsSamplesReady)
		{
			m_bIsSamplesReady = true;
			if (m_pNotify)
			{
				m_pNotify->OnSamplesReady(m_bIsSamplesReady);
			}
		}
	}

	void CBezierControl::DrawSelectRect(CDC* pDC)
	{
		if (!m_bIsLButtonDown || m_editMode != EDIT_MODE_DRAG_RECT)
			return;
		CRect rect;
		rect.left = m_ptMouseDownPoint.x - m_rectClient.left;
		rect.top = m_ptMouseDownPoint.y - m_rectClient.top;
		rect.right = m_ptMousePoint.x - m_rectClient.left;
		rect.bottom = m_ptMousePoint.y - m_rectClient.top;
		pDC->SelectStockObject(NULL_BRUSH);
		CPen pen(PS_DOT, 1, selectRectColor);
		pDC->SelectObject(&pen);
		pDC->Rectangle(&rect);
	}

	void CBezierControl::DrawCtrlPoint(CDC* pDC)
	{
		FPoint pt, point[4];
		CPen pen(PS_SOLID, 1, tangentPointColor);
		TangentPointContanter::iterator iter;
		TangentPointContanter::iterator nextIter;
		TangentPointContanter::iterator last;
		TangentPointContanters::iterator lineIter = m_TPCTangentPoints.begin();
		CBrush selectBrush(selectedPointColor);
		CBrush normalBrush(bgColor);
		bool bIsSelected = false;
		SelectedPoint sp;
		sp.lineID = 0;
		for (;lineIter!=m_TPCTangentPoints.end();++lineIter,++sp.lineID)
		{
			iter = lineIter->begin();
			nextIter = iter;
			++nextIter;
			last = lineIter->end();
			pDC->SelectObject(&pen);
			pDC->SelectObject(&normalBrush);
			sp.iter = iter;
			
			if (m_selectedPoints.find(sp) != m_selectedPoints.end())
			{
				bIsSelected = true;
				pDC->SelectObject(&selectBrush);
			}
			for (;nextIter!=last;++iter,++nextIter)
			{
				TransToClient(iter->point, point[0]);
				TransToClient(iter->controlPt[CONTROL_TYPE_NEXT], point[1]);
				TransToClient(nextIter->controlPt[CONTROL_TYPE_PREV], point[2]);
				TransToClient(nextIter->point, point[3]);
				if (bIsSelected)
				{
					pDC->MoveTo(static_cast<int>(point[0].x), static_cast<int>(point[0].y));
					pDC->LineTo(static_cast<int>(point[1].x), static_cast<int>(point[1].y));
					pDC->Ellipse(static_cast<int>(point[1].x) - 3, static_cast<int>(point[1].y) - 3, 
						static_cast<int>(point[1].x) + 4, static_cast<int>(point[1].y) + 4);
				}
				pDC->Rectangle(static_cast<int>(point[0].x) - 3, static_cast<int>(point[0].y) - 3, 
					static_cast<int>(point[0].x) + 4, static_cast<int>(point[0].y) + 4);
				sp.iter = nextIter;
				if (bIsSelected)
				{
					pDC->SelectObject(&normalBrush);
					bIsSelected = false;
				}
				if (m_selectedPoints.find(sp) != m_selectedPoints.end()) 
				{
					bIsSelected = true;
					pDC->MoveTo(static_cast<int>(point[2].x), static_cast<int>(point[2].y));
					pDC->LineTo(static_cast<int>(point[3].x), static_cast<int>(point[3].y));
					pDC->SelectObject(&selectBrush);
					pDC->Ellipse(static_cast<int>(point[2].x) - 3, static_cast<int>(point[2].y) - 3, 
						static_cast<int>(point[2].x) + 4, static_cast<int>(point[2].y) + 4);
				}
			}
			pDC->Rectangle(static_cast<int>(point[3].x) - 3, static_cast<int>(point[3].y) - 3, 
				static_cast<int>(point[3].x) + 4, static_cast<int>(point[3].y) + 4);
			if (bIsSelected)
			{
				pDC->SelectObject(&normalBrush);
			}

		}

	}

	void CBezierControl::ModifyStartPoint(LineID line, float y, const FPoint &nextCtrlPt)
	{
		if (line >= m_TPCTangentPoints.size())
			return;
		TangentPointContanter::iterator iter = m_TPCTangentPoints[line].begin();
		iter->point.y = y;
		iter->controlPt[CONTROL_TYPE_NEXT] = nextCtrlPt;
		iter->controlPt[CONTROL_TYPE_PREV] = iter->point;
	}

	void CBezierControl::ModifyEndPoint(LineID line, float y, const FPoint &prevCtrlPt)
	{
		if (line >= m_TPCTangentPoints.size())
			return;
		TangentPointContanter::iterator iter = m_TPCTangentPoints[line].end();
		--iter;
		iter->point.y = y;
		iter->controlPt[CONTROL_TYPE_PREV] = prevCtrlPt;
		iter->controlPt[CONTROL_TYPE_NEXT] = iter->point;
	}

	void CBezierControl::SetLineCount(size_t count)
	{
		if (count < 1)
			return;
		m_lineNames.clear();
		CString str;
		for (size_t i=0;i<count;++i)
		{
			str.Format(L"Line %d",i);
			m_lineNames.push_back(str);
		}
		TangentPointContanter cont;
		TangentPoint tp;
		tp.point.x = m_fMinX;
		tp.point.y = m_fMinY;
		tp.controlPt[0].x = m_fMinX;
		tp.controlPt[0].y = m_fMinY;
		tp.controlPt[1].x = m_fMinX;
		tp.controlPt[1].y = m_fMinY;
		cont.push_back(tp);
		tp.point.x = m_fMaxX;
		tp.controlPt[0].x = m_fMaxX;
		tp.controlPt[1].x = m_fMaxX;
		cont.push_back(tp);
		while (m_TPCTangentPoints.size() < count)
		{
			m_TPCTangentPoints.push_back(cont);
		}
		if (m_TPCTangentPoints.size() > count)
		{
			TangentPointContanters::iterator iter = m_TPCTangentPoints.end();
			size_t removeCount = m_TPCTangentPoints.size() - count;
			for (size_t i=0;i<removeCount;++i,--iter) {}
			m_TPCTangentPoints.erase(iter, m_TPCTangentPoints.end());
		}
		if (m_wndToolbar.m_hWnd)
		{
			ResetFilter();
			SetWorkMode(WORK_MODE_CURVE);
		}
	}

	void CBezierControl::SetMinX(float minX)
	{
		CleanAllLinePoints();
		m_fMinX = minX;
		TangentPointContanters::iterator lineIter = m_TPCTangentPoints.begin();
		TangentPointContanter::iterator iter;
		for (;lineIter!=m_TPCTangentPoints.end();++lineIter)
		{
			iter = lineIter->begin();
			iter->point.x = minX;
			iter->controlPt[0] = iter->point;
			iter->controlPt[1] = iter->point;
		}
		ResetNearRadius();
	}

	void CBezierControl::SetMaxX(float maxX)
	{
		CleanAllLinePoints();
		m_fMaxX = maxX;
		TangentPointContanters::iterator lineIter = m_TPCTangentPoints.begin();
		TangentPointContanter::iterator iter;
		for (;lineIter!=m_TPCTangentPoints.end();++lineIter)
		{
			iter = lineIter->end();
			--iter;
			iter->point.x = maxX;
			iter->controlPt[0] = iter->point;
			iter->controlPt[1] = iter->point;
		}
		ResetNearRadius();
	}

	void CBezierControl::SetMaxY(float maxY)
	{
		m_fMaxY = maxY;
		ResetNearRadius();
	}

	void CBezierControl::SetMinY(float minY)
	{
		m_fMinY = minY;
		ResetNearRadius();
	}

	void CBezierControl::CleanAllLinePoints()
	{
		size_t lineCount = m_TPCTangentPoints.size();
		for (size_t i=0;i<lineCount;++i)
		{
			CleanPoints(i);
		}
	}

	void CBezierControl::CleanPoints(LineID line)
	{
		if (line >= m_TPCTangentPoints.size())
			return;
		TangentPointContanter::iterator iter = m_TPCTangentPoints[line].begin();
		++iter;
		TangentPointContanter::iterator iterEnd = m_TPCTangentPoints[line].end();
		--iterEnd;
		m_TPCTangentPoints[line].erase(iter, iterEnd);
	}

	bool CBezierControl::AddPoint(LineID line, const TangentPoint& tp)
	{
		if (line >= m_TPCTangentPoints.size())
			return false;
		TangentPointContanter::iterator iter = m_TPCTangentPoints[line].begin();
		if (tp.point.x <= iter->point.x)
		{
			return false;
		}
		++iter;
		for (;iter!=m_TPCTangentPoints[line].end();++iter)
		{
			if (tp.point.x < iter->point.x)
			{
				m_TPCTangentPoints[line].insert(iter, tp);
				return true;
			}
		}
		return false;
	}

	void CBezierControl::MoveSelectedPoint(FPoint logicVector, bool changeOldState)
	{
		if (m_selectedPoints.size() > 0)
		{
			SelectedPoints::iterator selIter = m_selectedPoints.begin();
			TangentPointContanter::iterator leftIter;
			TangentPointContanter::iterator rightIter;
			float left, right;
			for (;selIter!=m_selectedPoints.end();++selIter)
			{
				left = m_fMinX;
				right = m_fMaxX;
				leftIter = selIter->iter;
				if (leftIter != m_TPCTangentPoints[selIter->lineID].begin())
				{
					--leftIter;
					left = leftIter->point.x;
				} else {
					logicVector.x = 0;
				}
				rightIter = selIter->iter;
				++rightIter;
				if (rightIter != m_TPCTangentPoints[selIter->lineID].end())
				{
					right = rightIter->point.x;
				} else {
					logicVector.x = 0;
				}
				if (selIter->oldState.point.x + logicVector.x < left)
				{
					logicVector.x = left - selIter->oldState.point.x;
				}
				if (selIter->oldState.point.x + logicVector.x > right)
				{
					logicVector.x = right - selIter->oldState.point.x;
				}
			}
			selIter = m_selectedPoints.begin();
			for (;selIter!=m_selectedPoints.end();++selIter)
			{
				selIter->iter->point = selIter->oldState.point + logicVector;
				selIter->iter->controlPt[CONTROL_TYPE_PREV] = 
					selIter->oldState.controlPt[CONTROL_TYPE_PREV] + logicVector;
				selIter->iter->controlPt[CONTROL_TYPE_NEXT] = 
					selIter->oldState.controlPt[CONTROL_TYPE_NEXT] + logicVector;
				if (changeOldState)
				{
					selIter->oldState = (*selIter->iter);
					if (selIter->oldState.point.y > m_fMaxY)
					{
						SetMaxY(selIter->oldState.point.y);
					}
					if (selIter->oldState.point.y < m_fMinY)
					{
						SetMinY(selIter->oldState.point.y);
					}
				}
			}
			if (m_selectedPoints.size() == 1)
			{
				m_ptSelectPos = m_selectedPoints.begin()->iter->point;
				UpdateData(FALSE);
			}		
		}
	}

	void CBezierControl::AdjustControlPoint(FPoint logicVector, ControlPointType type, bool changeOldState)
	{
		if (m_selectedPoints.size() == 1) { 
			ControlPointType otherType;
			TangentPointContanter::iterator selectedPoint = m_selectedPoints.begin()->iter;
			const TangentPoint &oldState = m_selectedPoints.begin()->oldState;
			switch (type)
			{
			case CONTROL_TYPE_PREV:
				otherType = CONTROL_TYPE_NEXT;
				if (logicVector.x + oldState.controlPt[CONTROL_TYPE_PREV].x > oldState.point.x)
				{
					logicVector.x = oldState.point.x - oldState.controlPt[CONTROL_TYPE_PREV].x;
				}
				break;
			case CONTROL_TYPE_NEXT:
				otherType = CONTROL_TYPE_PREV;
				if (logicVector.x + oldState.controlPt[CONTROL_TYPE_NEXT].x < oldState.point.x)
				{
					logicVector.x = oldState.point.x - oldState.controlPt[CONTROL_TYPE_NEXT].x;
				}
				break;
			default:
				return;
				break;
			}
			selectedPoint->controlPt[type] = oldState.controlPt[type] + logicVector;
			if (selectedPoint->type == POINT_TYPE_SMOOTH)
			{
				float thisLength = selectedPoint->point.ToLength(selectedPoint->controlPt[type]);
				float otherLength = selectedPoint->point.ToLength(oldState.controlPt[otherType]);
				if (m_ToolsState == MOUSE_STATE_CONVERT)
				{
					otherLength = thisLength;
				}
				if (thisLength > 0)
				{
					FPoint add = selectedPoint->point - selectedPoint->controlPt[type];
					add.x = add.x * otherLength / thisLength;
					add.y = add.y * otherLength / thisLength;
					selectedPoint->controlPt[otherType] = selectedPoint->point + add;
				}
			}
			if (changeOldState)
			{
				m_selectedPoints.begin()->oldState = *selectedPoint;
			}
			Invalidate(FALSE);
		}
	}

	bool CBezierControl::CheckSelectSamplePoint(const FPoint &logicPoint)
	{
		SelectedSamplePoints::iterator lineSPIter = m_selectedSamplePoints.begin();
		for (;lineSPIter!=m_selectedSamplePoints.end();++lineSPIter)
		{
			if ((*lineSPIter)->point.Near(logicPoint, m_NearRadius))
				return true;
		}
		SamplePointContanters::iterator lineIter = m_SPCSamplePoints.begin();
		SamplePointContanter::iterator iter;
		SamplePointContanter::iterator last;
		m_selectedSamplePoints.clear();
		int pos = 0;
		bool findOut = false;
		for (;lineIter!=m_SPCSamplePoints.end();++lineIter)
		{
			pos = 0;
			iter = lineIter->begin();
			last = lineIter->end();
			for (;iter!=last;++iter,++pos)
			{
				if (iter->point.Near(logicPoint, m_NearRadius))
				{
					findOut = true;
					break;
				}
			}
			if (findOut)
				break;
		}
		if (findOut)
		{
			int tmpPos = 0;
			lineIter = m_SPCSamplePoints.begin();
			for (;lineIter!=m_SPCSamplePoints.end();++lineIter)
			{
				tmpPos = 0;
				iter = lineIter->begin();
				for (;tmpPos<pos;++iter,++tmpPos) {}
				m_selectedSamplePoints.push_back(iter);
			}
		}
		SetEditCtrlFromSamplePoint();
		return findOut;
	}

	void CBezierControl::SelectPoints(FPoint logicTopLeft, FPoint logicBottomRight)
	{
		float tmp;
		if (logicTopLeft.x > logicBottomRight.x)
		{
			tmp = logicTopLeft.x;
			logicTopLeft.x = logicBottomRight.x;
			logicBottomRight.x = tmp;
		}
		if (logicTopLeft.y > logicBottomRight.y)
		{
			tmp = logicTopLeft.y;
			logicTopLeft.y = logicBottomRight.y;
			logicBottomRight.y = tmp;
		}
		m_selectedControlPoint = CONTROL_TYPE_NONE;
		if (!IsCTRLPressed())
		{
			m_selectedPoints.clear();
		}
		TangentPointContanters::iterator lineIter = m_TPCTangentPoints.begin();
		TangentPointContanter::iterator iter;
		TangentPointContanter::iterator last;
		SelectedPoint sp;
		SelectedPoints::iterator spIter;
		LineID id = 0;
		for (;lineIter!=m_TPCTangentPoints.end();++lineIter,++id)
		{
			if (m_filter < m_TPCTangentPoints.size() && m_filter != id)
				continue;
			iter = lineIter->begin();
			last = lineIter->end();
			sp.lineID = id;
			for (;iter!=last;++iter)
			{
				if (iter->point.x >= logicTopLeft.x && iter->point.x <= logicBottomRight.x && 
					iter->point.y >= logicTopLeft.y && iter->point.y <= logicBottomRight.y)
				{
					sp.iter = iter;
					if (IsCTRLPressed())
					{
						spIter = m_selectedPoints.find(sp);
						if (spIter == m_selectedPoints.end())
						{
							sp.oldState = *iter;
							m_selectedPoints.insert(sp);
						} else {
							m_selectedPoints.erase(spIter);
						}
					} else {
						sp.oldState = *iter;
						m_selectedPoints.insert(sp);
					}
				}
			}
		}
		SetEditCtrlFromControlPoint();
	}

	CBezierControl::CLICK_STATE CBezierControl::CheckSelectPoint(const FPoint &logicPoint, 
		SelectedPoint& outInfo, ControlPointType& type)
	{
		type = CONTROL_TYPE_NONE;
		if (m_selectedPoints.size() > 0)
		{
			SelectedPoints::iterator selIter = m_selectedPoints.begin();
			for (;selIter!=m_selectedPoints.end();++selIter)
			{
				if (selIter->iter->point.Near(logicPoint, m_NearRadius))
				{
					selIter->oldState = *(selIter->iter);
					outInfo = *selIter;
					return CLICK_A_SELECTED_POINT;
				} else if (selIter->iter->controlPt[CONTROL_TYPE_PREV].Near(logicPoint, m_NearRadius))
				{
					selIter->oldState = *(selIter->iter);
					outInfo = *selIter;
					type = CONTROL_TYPE_PREV;
					return CLICK_A_CONTROL_POINT;
				} else if (selIter->iter->controlPt[CONTROL_TYPE_NEXT].Near(logicPoint, m_NearRadius))
				{
					selIter->oldState = *(selIter->iter);
					outInfo = *selIter;
					type = CONTROL_TYPE_NEXT;
					return CLICK_A_CONTROL_POINT;
				}
			}
		}
		TangentPointContanters::iterator lineIter = m_TPCTangentPoints.begin();
		TangentPointContanter::iterator iter;
		TangentPointContanter::iterator last;
		LineID id = 0;
		for (;lineIter!=m_TPCTangentPoints.end();++lineIter,++id)
		{
			if (m_filter < m_TPCTangentPoints.size() && id != m_filter)
				continue;
			iter = lineIter->begin();
			last = lineIter->end();
			for (;iter!=last;++iter)
			{
				if (iter->point.Near(logicPoint, m_NearRadius))
				{
					outInfo.lineID = id;
					outInfo.iter = iter;
					outInfo.oldState = *iter;
					return CLICK_A_COMMON_POINT;
				}
			}
		}
		return CLICK_EMPTY_AREA;
	}

	void CBezierControl::ConvertControlPoint(const FPoint &logicPoint, bool changeOldState)
	{
		if (m_selectedPoints.size() == 1)
		{
			if (m_selectedControlPoint == CONTROL_TYPE_NONE)
			{	//选中的是切点（在曲线上的点）
				if (!logicPoint.Near(m_selectedPoints.begin()->iter->point, m_NearRadius))
				{
					TangentPointContanter::iterator iter = m_TPCTangentPoints[m_selectedPoints.begin()->lineID].begin();
					TangentPointContanter::iterator last = m_TPCTangentPoints[m_selectedPoints.begin()->lineID].end();
					--last;
					if (m_selectedPoints.begin()->iter == iter) {
						m_selectedControlPoint = CONTROL_TYPE_NEXT;
					} else if (m_selectedPoints.begin()->iter == last) {
						m_selectedControlPoint = CONTROL_TYPE_PREV;
					} else {
						//不能将两个端点转换为平滑点
						m_selectedPoints.begin()->iter->type = POINT_TYPE_SMOOTH;
						m_selectedControlPoint = logicPoint.x>m_selectedPoints.begin()->iter->point.x?CONTROL_TYPE_NEXT:CONTROL_TYPE_PREV;
					}
				}
			} else {
				FPoint clientPoint = m_ptMouseDownPoint;
				clientPoint.y -= m_rectClient.top;
				FPoint mouseLogicPoint;
				TransToLogic(clientPoint, mouseLogicPoint);
				AdjustControlPoint(logicPoint - mouseLogicPoint, m_selectedControlPoint, changeOldState);
			}
		}
	}

	bool CBezierControl::SetSamples(const SamplePointContanter &spc)
	{
		if (spc.size() < 2)
			return false;
		ClearSamplePoints();
		m_nSampleCount = static_cast<int>(spc.size());
		if (m_pNotify)
		{
			m_pNotify->OnSampleCount(m_nSampleCount);
		}
		m_SPCSamplePoints.push_back(spc);
		SamplePointContanter::iterator spcIter = m_SPCSamplePoints.begin()->begin();
		for (;spcIter!=m_SPCSamplePoints.begin()->end();++spcIter)
		{
			spcIter->inited = false;
		}
		m_selectedSamplePoints.clear();
		return true;
	}

	void CBezierControl::SetSampleCount(int count)
	{
		if (m_SPCSamplePoints.size() > 0 && m_SPCSamplePoints.begin()->size() == count && m_nSampleCount == count)
			return;
		m_bIsSamplesReady = true;//强制clear
		ClearSamplePoints();
		if (count < 2)
		{
			count = 2;
		}
		m_nSampleCount = count--;
		if (m_pNotify)
		{
			m_pNotify->OnSampleCount(m_nSampleCount);
		}
		int tangentPtcount = static_cast<int>(m_TPCTangentPoints[0].size()) - 1;
		int remainder = count % tangentPtcount;
		int countInTanges = count / tangentPtcount;
		int counts = countInTanges + (remainder>0?1:0);
		int j = 0;
		int id = 0;
		--remainder;
		SamplePointContanter spc;
		spc.clear();
		SamplePoint sp;
		for (int i=0;i<count;++i,++j)
		{
			if (j >= counts)
			{
				if (remainder > 0)
				{
					--remainder;
				} else {
					counts = countInTanges;
				}
				j = 0;
				++id;
			}
			sp.id = id;
			sp.u = static_cast<float>(j) / counts;
			spc.push_back(sp);
		}
		sp.id = tangentPtcount - 1;
		sp.u = 1.0f;
		spc.push_back(sp);
		m_SPCSamplePoints.push_back(spc);
		m_selectedSamplePoints.clear();
	}

	void CBezierControl::AdjSelectedSamplePoint(float x)
	{
		SelectedSamplePoints::iterator selIter = m_selectedSamplePoints.begin();
		SamplePointContanter::iterator iterLeft;
		SamplePointContanter::iterator iterRight;
		TangentPointContanter::iterator tpIter;
		TangentPointContanter::iterator tpIterRight;
		TangentPointContanter::iterator tpIterLeft;
		BezierParam param;
		FPoint result;
		float u, u1, u2;
		int newID;
		LineID lineID = 0;
		bool ok = true;
		if (xMustInt)
		{
			x = (float)Round(x);
		}
		for (;selIter!=m_selectedSamplePoints.end();++selIter,++lineID)
		{
			iterLeft = *selIter;
			iterRight = iterLeft;
			if (*selIter != m_SPCSamplePoints[lineID].begin())
			{
				--iterLeft;
			} else {
				ok = false;
				break;
			}
			if (x < iterLeft->point.x)
			{
				x = iterLeft->point.x;
			}
			++iterRight;
			if (iterRight == m_SPCSamplePoints[lineID].end())
			{
				ok = false;
				break;
			}
			if (x > iterRight->point.x)
			{
				x = iterRight->point.x;
			}
			if (xMustInt)
			{
				if (m_setHasSamplePoint.find(Round(x)) != m_setHasSamplePoint.end())
				{
					ok = false;
					break;
				}	
			}
			if (!FindCtrlPoint(lineID, (*selIter)->id, tpIter))
			{
				ok = false;
				break;
			}
		}
		if (ok)
		{
			lineID = 0;
			selIter = m_selectedSamplePoints.begin();
			if (xMustInt)
			{
				m_setHasSamplePoint.erase(Round((*selIter)->point.x));
				m_setHasSamplePoint.insert(Round(x));
			}
			for (;selIter!=m_selectedSamplePoints.end();++selIter,++lineID)
			{
				iterLeft = *selIter;
				iterRight = iterLeft;
				--iterLeft;
				++iterRight;
				FindCtrlPoint(lineID, (*selIter)->id, tpIter);
				tpIterRight = tpIter;
				++tpIterRight;
				if (tpIterRight == m_TPCTangentPoints[lineID].end())
				{
					--tpIterRight;
				}
				if (iterLeft->id != (*selIter)->id || iterRight->id != (*selIter)->id)
				{
					tpIterLeft = tpIter;
					if (tpIter != m_TPCTangentPoints[lineID].begin())
					{
						--tpIterLeft;
					}
					if (tpIter->point.x < x && x <= tpIterRight->point.x)
					{
						//点在当前曲线上
						param.p0 = &tpIter->point;
						param.p1 = &tpIter->controlPt[CONTROL_TYPE_NEXT];
						param.p2 = &tpIterRight->controlPt[CONTROL_TYPE_PREV];
						param.p3 = &tpIterRight->point;
						u1 = (iterLeft->id != (*selIter)->id)?0.0f:iterLeft->u;
						u2 = (iterRight->id != (*selIter)->id)?1.0f:iterRight->u;
						newID = (*selIter)->id;
					} else if (x <= tpIter->point.x)
					{
						//点在前一段曲线上
						param.p0 = &tpIterLeft->point;
						param.p1 = &tpIterLeft->controlPt[CONTROL_TYPE_NEXT];
						param.p2 = &tpIter->controlPt[CONTROL_TYPE_PREV];
						param.p3 = &tpIter->point;
						if (iterLeft->id != (*selIter)->id - 1)
						{
							u1 =0.0f;
						} else {
							u1 = iterLeft->u;
						}
						u2 = 1.0f;
						newID = (*selIter)->id - 1;
					} else {
						//点在后一段曲线上
						param.p0 = &tpIterRight->point;
						param.p1 = &tpIterRight->controlPt[CONTROL_TYPE_NEXT];
						++tpIterRight;
						param.p2 = &tpIterRight->controlPt[CONTROL_TYPE_PREV];
						param.p3 = &tpIterRight->point;
						u1 = 0.0f;
						if (iterRight->id != (*selIter)->id + 1)
						{
							u2 = 1.0f;
						} else {
							u2 = iterRight->u;
						}
						newID = (*selIter)->id + 1;
					}
				} else {
					param.p0 = &tpIter->point;
					param.p1 = &tpIter->controlPt[CONTROL_TYPE_NEXT];
					param.p2 = &tpIterRight->controlPt[CONTROL_TYPE_PREV];
					param.p3 = &tpIterRight->point;
					u1 = (iterLeft->id != (*selIter)->id)?0.0f:iterLeft->u;
					u2 = (iterRight->id != (*selIter)->id)?1.0f:iterRight->u;
					newID = (*selIter)->id;
				}
				ComputerNear3BezierCurvePoint(x, param, u1, u2, u);
				(*selIter)->id = newID;
				(*selIter)->u = u;
				(*selIter)->inited = false;
			}
		}
	}

	BEGIN_MESSAGE_MAP(CBezierControl, CDialog)
		ON_WM_LBUTTONDOWN()
		ON_WM_PAINT()
		ON_WM_SIZE()
		ON_WM_SIZING()
		ON_COMMAND_RANGE(ID_BZ_TOOL_ARROW, ID_BZ_TOOL_CONVERT, &CBezierControl::OnToolBar)
		ON_WM_LBUTTONUP()
		ON_WM_MOUSEMOVE()
		ON_EN_KILLFOCUS(IDC_BZ_EDIT_X, &CBezierControl::OnEnKillfocusBzEditX)
		ON_EN_KILLFOCUS(IDC_BZ_EDIT_Y, &CBezierControl::OnEnKillfocusBzEditY)
		ON_CBN_SELCHANGE(IDC_BZ_COMBO_FILTER, &CBezierControl::OnCbnSelchangeBzComboFilter)
	END_MESSAGE_MAP()
	// CBezierControl message handlers

	void CBezierControl::OnToolBar(UINT nID)
	{
		m_ToolsState = static_cast<TOOLS_STATE>(nID);
		m_editMode = EDIT_MODE_NORMAL;
	}

	void CBezierControl::OnPaint()
	{
		CPaintDC dc(this); 

		CBitmap ShowBitmap;
		ShowBitmap.CreateCompatibleBitmap(&dc, m_nClientWidth + 4, m_nClientHeight);
		if (!ShowBitmap.m_hObject)
			return;
		CDC memDC;
		memDC.CreateCompatibleDC(&dc);
		if (!memDC.m_hDC)
			return;
		memDC.SelectObject(&ShowBitmap);
		memDC.SetBkMode(TRANSPARENT);

		CBrush bgBrush(bgColor);
		CRect rect;
		rect.top = 0;
		rect.bottom = m_nClientHeight;
		rect.left = 0;
		rect.right = m_nClientWidth + 4;
		memDC.FillRect(&rect, &bgBrush);
		DrawCtrl(&memDC);

		dc.BitBlt(0, m_rectClient.top, m_nClientWidth + 4, m_nClientHeight, &memDC, 0, 0, SRCCOPY);
		memDC.DeleteDC();
		ShowBitmap.DeleteObject();
	}

	void CBezierControl::OnSize(UINT nType, int cx, int cy)
	{
		CDialog::OnSize(nType, cx, cy);
		ResetSize();
	}

	void CBezierControl::OnSizing(UINT fwSide, LPRECT pRect)
	{
		CDialog::OnSizing(fwSide, pRect);
		ResetSize();
	}

	void CBezierControl::ResetFilter()
	{
		while (m_ComboFilter.GetCount() > 0)
		{
			m_ComboFilter.DeleteString(0);
		}
		for (size_t i=0;i<m_TPCTangentPoints.size();++i)
		{
			m_ComboFilter.InsertString(static_cast<int>(i), m_lineNames[i]);
		}
		m_ComboFilter.InsertString(static_cast<int>(m_TPCTangentPoints.size()), L"无");
		m_ComboFilter.SetCurSel(0);
						//static_cast<int>(m_TPCTangentPoints.size())
		m_filter = m_TPCTangentPoints.size();
	}

	BOOL CBezierControl::OnInitDialog()
	{
		CDialog::OnInitDialog();
		GetClientRect(&m_rectClient);
		if (!m_wndToolbar.CreateEx(this, TBSTYLE_CHECKGROUP, WS_CHILD|WS_VISIBLE|CBRS_TOP|CBRS_TOOLTIPS|CBRS_FLYBY|CBRS_SIZE_DYNAMIC) ||
			!m_wndToolbar.LoadToolBar(IDR_BEZIER_TOOLS))
		{
			return FALSE;
		}
		int count = m_wndToolbar.GetToolBarCtrl().GetButtonCount();
		for (int i=0;i<count;++i)
		{
			if(m_wndToolbar.GetButtonStyle(i) != TBBS_SEPARATOR)
			{
				m_wndToolbar.SetButtonStyle(i, TBBS_CHECKGROUP);
			}
		}
		m_wndToolbar.SetButtonStyle(0, TBBS_CHECKGROUP | TBBS_CHECKED);
		m_StaticX.SetWindowText(L"X:");
		m_StaticX.ModifyStyle(0, ES_RIGHT);
		m_StaticY.SetWindowText(L"Y:");
		m_StaticY.ModifyStyle(0, ES_RIGHT);
		m_StaticFilter.SetWindowText(L"过滤器:");
		m_StaticFilter.ModifyStyle(0, ES_RIGHT);
		ResetFilter();
		m_EditX.EnableWindow(false);
		m_EditY.EnableWindow(false);
		ResetSize();
		return FALSE;
	}

	
	void CBezierControl::OnLButtonDown(UINT nFlags, CPoint point)
	{
		SetCapture();
		m_ptMouseDownPoint = point;
		m_bIsLButtonDown = true;
		FPoint clientPoint = point;
		clientPoint.y -= m_rectClient.top;
		FPoint logicPoint;
		TransToLogic(clientPoint, logicPoint);
		SelectedPoint outInfo;
		ControlPointType type;
		switch (m_WorkMode)
		{
		case WORK_MODE_CURVE:
			switch (m_ToolsState)
			{
			case MOUSE_STATE_ARROW:
				if (m_editMode == EDIT_MODE_NORMAL)
				{
					switch (CheckSelectPoint(logicPoint, outInfo, type))
					{
					case CLICK_EMPTY_AREA:
						if (!IsCTRLPressed())
						{
							m_selectedPoints.clear();
						}
						m_editMode = EDIT_MODE_DRAG_RECT;
						break;
					case CLICK_A_SELECTED_POINT:
						break;
					case CLICK_A_COMMON_POINT:
						if (!IsCTRLPressed())
						{
							m_selectedPoints.clear();
							m_selectedPoints.insert(outInfo);
						}
						break;
					case CLICK_A_CONTROL_POINT:
						m_selectedPoints.clear();
						m_selectedPoints.insert(outInfo);
						m_selectedControlPoint = type;
						m_editMode = EDIT_MODE_ADJUST_CONTROL;
						break;
					}
					Invalidate(FALSE);
				}
				break;
			case MOUSE_STATE_ADD:
			case MOUSE_STATE_REMOVE:
				break;
			case MOUSE_STATE_CONVERT:
				switch (CheckSelectPoint(logicPoint, outInfo, type))
				{
				case CLICK_EMPTY_AREA:
					m_selectedPoints.clear();
					break;
				case CLICK_A_SELECTED_POINT:
				case CLICK_A_COMMON_POINT:
					outInfo.iter->controlPt[0] = outInfo.iter->point;
					outInfo.iter->controlPt[1] = outInfo.iter->point;
					//不需要break;
				case CLICK_A_CONTROL_POINT:
					outInfo.iter->type = POINT_TYPE_CORNER;
					outInfo.oldState = *outInfo.iter;
					m_selectedPoints.clear();
					m_selectedPoints.insert(outInfo);
					m_selectedControlPoint = type;
					ClearSamplePoints();
					break;
				}
				Invalidate(FALSE);
				break;
			}
			SetEditCtrlFromControlPoint();
			break;
		case WORK_MODE_SAMPLEING:
			if (!CheckSelectSamplePoint(logicPoint))
			{
				TangentPointContanters::iterator lineIter = m_TPCTangentPoints.begin();
				TangentPointContanter::iterator iter;// = m_TangentPoints.begin();
				m_selectedSamplePoints.clear();
				BezierParam param;
				float u;
				bool ok = false;
				FPoint pt;
				for (;lineIter!=m_TPCTangentPoints.end();++lineIter)
				{
					iter = lineIter->begin();
					for (;iter!=lineIter->end();++iter)
					{
						if (iter->point.x > logicPoint.x)
							break;
					}
					if (iter != lineIter->end())
					{
						param.p2 = &iter->controlPt[CONTROL_TYPE_PREV];
						param.p3 = &iter->point;
						--iter;
						param.p0 = &iter->point;
						param.p1 = &iter->controlPt[CONTROL_TYPE_NEXT];
						ComputerNear3BezierCurvePoint(logicPoint.x, param, 0.0f, 1.0f, u);
						pt = Computer3BezierCurvePoint(u, param);
						if (pt.Near(logicPoint, m_NearRadius))
						{
							ok = true;
							//点在某条线上了
						}
					}
				}
				if (ok && xMustInt)
				{
					int tmpX = Round(logicPoint.x);
					if (m_setHasSamplePoint.find(Round(tmpX)) == m_setHasSamplePoint.end())
					{
						m_setHasSamplePoint.insert(tmpX);
						logicPoint.x = (float)tmpX;
					} else {
						ok = false;
					}
				}
				if (ok)
				{
					
					SamplePoint sp;
					SamplePointContanters::iterator spLineIter = m_SPCSamplePoints.begin();
					SamplePointContanter::iterator spIter;
					lineIter = m_TPCTangentPoints.begin();
					int id;
					for (;spLineIter!=m_SPCSamplePoints.end();++spLineIter,++lineIter)
					{
						iter = lineIter->begin();
						id = 0;
						for (;iter!=lineIter->end();++iter,++id)
						{
							if (iter->point.x > logicPoint.x)
								break;
						}
						param.p2 = &iter->controlPt[CONTROL_TYPE_PREV];
						param.p3 = &iter->point;
						--iter;
						param.p0 = &iter->point;
						param.p1 = &iter->controlPt[CONTROL_TYPE_NEXT];
						ComputerNear3BezierCurvePoint(logicPoint.x, param, 0.0f, 1.0f, u);
						pt = Computer3BezierCurvePoint(u, param);
						spIter = spLineIter->begin();
						for (;spIter!=spLineIter->end();++spIter)
						{
							if (spIter->point.x > logicPoint.x)
								break;
						}
						sp.id = id - 1;
						sp.inited = true;
						sp.u = u;
						sp.point = pt;
						spLineIter->insert(spIter, sp);
					}
					++m_nSampleCount;
					if (m_pNotify)
					{
						m_pNotify->OnSampleCount(m_nSampleCount);
					}
				}
			}
			Invalidate(FALSE);
			break;
		}
		
		CDialog::OnLButtonDown(nFlags, point);
	}

	void CBezierControl::OnLButtonUp(UINT nFlags, CPoint point)
	{
		ReleaseCapture();
		if (!m_bIsLButtonDown)
			return;
		m_bIsLButtonDown = false;
		FPoint clientPoint = point;
		clientPoint.y -= m_rectClient.top;
		FPoint logicPoint;
		TransToLogic(clientPoint, logicPoint);
		FPoint logicVector;
		clientPoint.x = static_cast<float>(point.x - m_ptMouseDownPoint.x);
		clientPoint.y = static_cast<float>(point.y - m_ptMouseDownPoint.y);
		TransToLogicVector(clientPoint, logicVector);
		SelectedPoint outInfo;
		ControlPointType type;
		switch (m_WorkMode)
		{
		case WORK_MODE_CURVE:
			switch (m_ToolsState)
			{
			case MOUSE_STATE_ARROW:
				switch (m_editMode)
				{
				case EDIT_MODE_NORMAL:
					if (IsCTRLPressed())
					{
						switch (CheckSelectPoint(logicPoint, outInfo, type))
						{
						case CLICK_EMPTY_AREA:
						case CLICK_A_CONTROL_POINT:
							break;
						case CLICK_A_SELECTED_POINT:
							m_selectedPoints.erase(outInfo);
							SetEditCtrlFromControlPoint();
							break;
						case CLICK_A_COMMON_POINT:
							m_selectedPoints.insert(outInfo);
							SetEditCtrlFromControlPoint();
							break;
						}
					}
					break;
				case EDIT_MODE_DRAG_POINT:
					MoveSelectedPoint(logicVector, true);
					m_editMode = EDIT_MODE_NORMAL;
					ClearSamplePoints();
					break;
				case EDIT_MODE_ADJUST_CONTROL:
					AdjustControlPoint(logicVector, m_selectedControlPoint, true);
					m_editMode = EDIT_MODE_NORMAL;
					ClearSamplePoints();
					break;
				case EDIT_MODE_DRAG_RECT:
					SelectPoints(logicPoint - logicVector, logicPoint);
					m_editMode = EDIT_MODE_NORMAL;
					break;
				}
				Invalidate(FALSE);
				break;
			case MOUSE_STATE_ADD:
				switch (CheckSelectPoint(logicPoint, outInfo, type))
				{
				case CLICK_EMPTY_AREA:
				case CLICK_A_CONTROL_POINT:
					if (m_selectedPoints.size() == 1)
					{
						AddPoint(m_selectedPoints.begin()->lineID, logicPoint);
						Invalidate(FALSE);
						ClearSamplePoints();
					} else {
						if (m_filter < m_TPCTangentPoints.size())
						{
							AddPoint(m_filter, logicPoint);
							Invalidate(FALSE);
							ClearSamplePoints();
						}
					}
					break;
				case CLICK_A_SELECTED_POINT:
				case CLICK_A_COMMON_POINT:
					m_selectedPoints.clear();
					m_selectedPoints.insert(outInfo);
					SetEditCtrlFromControlPoint();
					Invalidate(FALSE);
					break;
				}
				break;
			case MOUSE_STATE_REMOVE:
				{
					switch (CheckSelectPoint(logicPoint, outInfo, type))
					{
					case CLICK_EMPTY_AREA:
					case CLICK_A_CONTROL_POINT:
						break;
					case CLICK_A_SELECTED_POINT:
					case CLICK_A_COMMON_POINT:
						m_selectedPoints.clear();
						TangentPointContanter &line = m_TPCTangentPoints[outInfo.lineID];
						if (line.size() > 2)
						{
							TangentPointContanter::iterator last = line.end();
							--last;
							if (outInfo.iter != last && outInfo.iter != line.begin())
							{
								line.erase(outInfo.iter);
								ClearSamplePoints();
							}
						}
						SetEditCtrlFromControlPoint();
						Invalidate(FALSE);
						break;
					}
				}
				break;
			case MOUSE_STATE_CONVERT:
				ConvertControlPoint(logicPoint, true);
				ClearSamplePoints();
				Invalidate(FALSE);
				break;
			}
			break;
		case WORK_MODE_SAMPLEING:
			if (m_selectedSamplePoints.size() > 0)
			{
				if (logicPoint.y < m_fMinY || logicPoint.y > m_fMaxY)
				{
					//删除点
					SamplePointContanters::iterator iterLine = m_SPCSamplePoints.begin();
					SelectedSamplePoints::iterator selIter = m_selectedSamplePoints.begin();
					SamplePointContanter::iterator iter = iterLine->begin();
					if (*selIter == iter)
						break;
					iter = iterLine->end();
					--iter;
					if (*selIter == iter)
						break;
					if (xMustInt)
					{
						m_setHasSamplePoint.erase(Round((*selIter)->point.x));
					}
					for (;iterLine!=m_SPCSamplePoints.end();++iterLine,++selIter)
					{
						iterLine->erase(*selIter);
					}
					--m_nSampleCount;
					if (m_pNotify)
					{
						m_pNotify->OnSampleCount(m_nSampleCount);
					}
					m_selectedSamplePoints.clear();
					SetEditCtrlFromSamplePoint();
				} else {
					AdjSelectedSamplePoint(logicPoint.x);
				}
			}
			break;
		}
		CDialog::OnLButtonUp(nFlags, point);
	}

	void CBezierControl::OnMouseMove(UINT nFlags, CPoint point)
	{
		m_ptMousePoint = point;
		FPoint clientPoint = point;
		clientPoint.y -= m_rectClient.top;
		FPoint logicPoint;
		TransToLogic(clientPoint, logicPoint);
		switch (m_WorkMode)
		{
		case WORK_MODE_CURVE:
			switch (m_ToolsState)
			{
			case MOUSE_STATE_ARROW:
				if (m_bIsLButtonDown)
				{
					switch (m_editMode)
					{
					case EDIT_MODE_NORMAL:
					case EDIT_MODE_DRAG_POINT:
						if (m_selectedPoints.size() > 0)
						{
							FPoint logicVector;
							clientPoint.x = static_cast<float>(point.x - m_ptMouseDownPoint.x);
							clientPoint.y = static_cast<float>(point.y - m_ptMouseDownPoint.y);
							TransToLogicVector(clientPoint, logicVector);
							MoveSelectedPoint(logicVector, false);
							m_editMode = EDIT_MODE_DRAG_POINT;
						}
						break;
					case EDIT_MODE_DRAG_RECT:
						break;
					case EDIT_MODE_ADJUST_CONTROL:
						FPoint logicVector;
						clientPoint.x = static_cast<float>(point.x - m_ptMouseDownPoint.x);
						clientPoint.y = static_cast<float>(point.y - m_ptMouseDownPoint.y);
						TransToLogicVector(clientPoint, logicVector);
						AdjustControlPoint(logicVector, m_selectedControlPoint, false);
						break;
					}
					Invalidate(FALSE);
				}
				break;
			case MOUSE_STATE_ADD:
			case MOUSE_STATE_REMOVE:
				break;
			case MOUSE_STATE_CONVERT:
				if (m_bIsLButtonDown)
				{
					ConvertControlPoint(logicPoint, false);
				}
				break;
			}
			break;
		case WORK_MODE_SAMPLEING:
			m_bIsSamplePointWillBeDelete = false;
			if (m_selectedSamplePoints.size() > 0 && m_bIsLButtonDown)
			{
				if (logicPoint.y < m_fMinY || logicPoint.y > m_fMaxY)
				{//删除点
					m_bIsSamplePointWillBeDelete = true;
				} else {
					AdjSelectedSamplePoint(logicPoint.x);
				}
			}
			Invalidate(FALSE);
			break;
		}
		CDialog::OnMouseMove(nFlags, point);
	}

	void CBezierControl::OnEnKillfocusBzEditX()
	{
		if (m_WorkMode == WORK_MODE_CURVE)
		{
			SetControlPointFromEditCtrl();
		} else {
			SetSamplePointFromEditCtrl();
		}
		Invalidate(FALSE);
	}

	void CBezierControl::OnEnKillfocusBzEditY()
	{
		SetControlPointFromEditCtrl();
		Invalidate(FALSE);
	}

	BOOL CBezierControl::PreTranslateMessage(MSG* pMsg)
	{
		if (pMsg->message == WM_LBUTTONDOWN)
		{
			if (m_WorkMode == WORK_MODE_CURVE)
			{
				SetControlPointFromEditCtrl();
			} else {
				SetSamplePointFromEditCtrl();
			}
		}
		return CDialog::PreTranslateMessage(pMsg);
	}

	void CBezierControl::OnCbnSelchangeBzComboFilter()
	{
		m_filter = m_ComboFilter.GetCurSel();
		m_selectedPoints.clear();
		Invalidate(FALSE);
	}
}

