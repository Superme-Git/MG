#include "StdAfx.h"
#include "..\engine\world\regionmap.h"
#include "ToolsSoundArea.h"

int CToolsSoundArea::LoadPolygonToolsData(XMLIO::CINode& node, CToolsSoundArea::CutPairCont& toolsData)
{
	toolsData.clear();
	XMLIO::CNodeList cpnl;
	node.GetChildren(cpnl);
	XMLIO::CNodeList::iterator cpnlIt = cpnl.begin(), cpnlIe = cpnl.end();
	Nuclear::CPOINT tmpCp;
	for (;cpnlIt!=cpnlIe;++cpnlIt)
	{
		XMLIO::CINode& cpNode = *cpnlIt;
		if (cpNode.GetType() != XMLIO::NT_ELEMENT)
			continue;
		XPASSERT(cpNode.GetName()==L"cut");
		tmpCp.x = cpNode.GetAttributeInteger(L"x");
		tmpCp.y = cpNode.GetAttributeInteger(L"y");
		toolsData.push_back(tmpCp);
	}
	return 0;
}

int CToolsSoundArea::SavePolygonToolsData(XMLIO::CONode& node, const CToolsSoundArea::CutPairCont& toolsData)
{
	CutPairCont::const_iterator cpIt = toolsData.begin(), cpIe = toolsData.end();
	for (;cpIt!=cpIe;++cpIt)
	{
		XMLIO::CONode cpNode;
		node.CreateSubElement(L"cut", cpNode);
		cpNode.SetAttribute(L"x", cpIt->x);
		cpNode.SetAttribute(L"y", cpIt->y);
	}
	return 0;
}

void ResetCircleAreaRegion(const POINT &mapSize, const Nuclear::XPCircle& circle, int nTrans, Nuclear::XPSoundArea::RegionSet &regionSet)
{
	int nRadius = circle.m_nRadius + nTrans;
	int left = circle.m_pos.x - nRadius;
	int right = circle.m_pos.x + nRadius;
	int top = circle.m_pos.y - nRadius;
	int bottom = circle.m_pos.y + nRadius;
	left = max(0, left);
	left = min(left, mapSize.x);
	right = max(0, right);
	right = min(right, mapSize.x);
	top = max(0, top);
	top = min(top, mapSize.y);
	bottom = max(0, bottom);
	bottom = min(bottom, mapSize.y);
	regionSet.clear();

	left /= Nuclear::RegionMap::REGION_WIDTH;
	right /= Nuclear::RegionMap::REGION_WIDTH;
	top /= Nuclear::RegionMap::REGION_HEIGHT;
	bottom /= Nuclear::RegionMap::REGION_HEIGHT;
	int col = mapSize.x / Nuclear::RegionMap::REGION_WIDTH + 1;
	int i;
	for (int y=top;y<=bottom;++y)
	{
		i = y * col + left;
		for (int x=left;x<=right;++x, ++i)
		{
			regionSet.insert(i);
		}
	}
}

int CheckCircleClickPoint(const Nuclear::XPCircle& circle, const POINT &pt)
{
	CRect rect;
	rect.left = circle.m_pos.x + circle.m_nRadius - CToolsSoundArea::CHECK_CLICK_RADIUS;
	rect.right = rect.left + 2 * CToolsSoundArea::CHECK_CLICK_RADIUS;
	rect.top = circle.m_pos.y - CToolsSoundArea::CHECK_CLICK_RADIUS;
	rect.bottom = circle.m_pos.y + CToolsSoundArea::CHECK_CLICK_RADIUS;
	if (rect.PtInRect(pt))
		return 0;
	return -1;
}

#define GET_ID(x, y) ((y)/Nuclear::RegionMap::REGION_HEIGHT)*col+((x)/Nuclear::RegionMap::REGION_WIDTH)

void ResetPolygonAreaRegion(const POINT &mapSize, const Nuclear::XPPolygon& polygon, Nuclear::XPSoundArea::RegionSet &regionSet)
{
	regionSet.clear();
	if (polygon.m_points.size() == 0)
		return;
	Nuclear::XPSoundArea::RegionSet tmpSet;
	int col = mapSize.x / Nuclear::RegionMap::REGION_WIDTH + 1;
	std::vector<Nuclear::CPOINT>::const_iterator pIt = polygon.m_points.begin(), pIe = polygon.m_points.end();
	int left = pIt->x;
	int right = pIt->x;
	int top = pIt->y;
	int bottom = pIt->y;
	++pIt;
	for (;pIt!=pIe;++pIt)
	{
		if (left > pIt->x) left = pIt->x;
		if (right < pIt->x)	right = pIt->x;
		if (top > pIt->y) top = pIt->y;
		if (bottom < pIt->y) bottom = pIt->y;
	}

	left = max(0, left);
	left = min(left, mapSize.x);
	right = max(0, right);
	right = min(right, mapSize.x);
	top = max(0, top);
	top = min(top, mapSize.y);
	bottom = max(0, bottom);
	bottom = min(bottom, mapSize.y);


	left /= Nuclear::RegionMap::REGION_WIDTH;
	right /= Nuclear::RegionMap::REGION_WIDTH;
	top /= Nuclear::RegionMap::REGION_HEIGHT;
	bottom /= Nuclear::RegionMap::REGION_HEIGHT;

	int x, y, i, px, py;
	for (y=top;y<=bottom;++y)
	{
		i = y * col + left;
		for (x=left;x<=right;++x, ++i)
		{
			tmpSet.insert(i);
		}
	}

	//多边形的顶点所在的区域
	pIt = polygon.m_points.begin();
	pIe = polygon.m_points.end();
	for (;pIt!=pIe;++pIt)
	{
		if (pIt->x >= 0 && pIt->x <= mapSize.x && pIt->y >= 0 && pIt->y <= mapSize.y)
		{
			i = GET_ID(pIt->x, pIt->y);
			if (tmpSet.erase(i) > 0)
				regionSet.insert(i);
		}
	}

	//区域的顶点如果在多边形的内部
	py = Nuclear::RegionMap::REGION_HEIGHT * top;
	for (y=top;y<bottom;++y)
	{
		py+=Nuclear::RegionMap::REGION_HEIGHT;
		px = Nuclear::RegionMap::REGION_WIDTH * left;
		i = y * col + left;
		for (x=left;x<right;++x,++i)
		{
			px+=Nuclear::RegionMap::REGION_WIDTH;
			if (polygon.IsPointIn(px, py))
			{
				//i,i+1,i+col,i+col+1;
				if (tmpSet.erase(i) > 0)
					regionSet.insert(i);
				if (tmpSet.erase(i+1) > 0)
					regionSet.insert(i+1);
				if (tmpSet.erase(i+col) > 0)
					regionSet.insert(i+col);
				if (tmpSet.erase(i+col+1) > 0)
					regionSet.insert(i+col+1);
			}
		}
	}

	//区域的对角线是否和多边形相交
	size_t lastpt = polygon.m_points.size() - 1;
	Nuclear::XPSoundArea::RegionSet::iterator it = tmpSet.begin(), ie = tmpSet.end();
	bool ok;
	for (;it!=ie;++it)
	{
		ok = false;
		i = *it;
		y = i / col;
		x = i % col;
		Nuclear::CPOINT pt1(x * Nuclear::RegionMap::REGION_WIDTH, y * Nuclear::RegionMap::REGION_HEIGHT);
		Nuclear::CPOINT pt3(pt1.x + Nuclear::RegionMap::REGION_WIDTH, pt1.y + Nuclear::RegionMap::REGION_HEIGHT);
		Nuclear::CPOINT ptl1 = polygon.m_points[lastpt];
		pIt = polygon.m_points.begin();
		pIe = polygon.m_points.end();
		for (;pIt!=pIe;++pIt)
		{
			const Nuclear::CPOINT &ptl2 = *pIt;
			if (IsCross(pt1, pt3, ptl1, ptl2))
			{
				regionSet.insert(i);
				ok = true;
				break;
			}
			ptl1 = ptl2;
		}
		if (ok)
			continue;

		Nuclear::CPOINT pt2(pt3.x, pt1.y);
		Nuclear::CPOINT pt4(pt1.x, pt3.y);
		pIt = polygon.m_points.begin();
		pIe = polygon.m_points.end();
		for (;pIt!=pIe;++pIt)
		{
			const Nuclear::CPOINT &ptl2 = *pIt;
			if (IsCross(pt2, pt4, ptl1, ptl2))
			{
				regionSet.insert(i);
				break;
			}
			ptl1 = ptl2;
		}
	}

}

void RecutToolsPolygon(const CToolsSoundArea::CutPairCont &cutpairs, const Nuclear::XPPolygon& integrityPoly, CToolsSoundArea::PolygonCont& polys)
{
	CToolsSoundArea::PolygonCont::iterator iIt = polys.begin(), iIe = polys.end(), iTemp;
	polys.clear();
	polys.push_back(integrityPoly);
	Nuclear::CPOINT pt1, pt2;
	std::vector<Nuclear::CPOINT>::iterator pIt, pIe;
	int first, second, i;
	Nuclear::XPPolygon tmpPoly;

	CToolsSoundArea::CutPairCont::const_iterator it = cutpairs.begin(), ie = cutpairs.end();
	for (;it!=ie;++it)
	{
		iIt = polys.begin();
		iIe = polys.end();
		pt1 = integrityPoly.m_points[it->x];
		pt2 = integrityPoly.m_points[it->y];
		for (;iIt!=iIe;++iIt)
		{
			std::vector<Nuclear::CPOINT> &points = iIt->m_points;
			pIt = points.begin();
			pIe = points.end();
			first = second = -1;
			for (i=0;pIt!=pIe;++pIt,++i)
			{
				if (*pIt == pt1 || *pIt == pt2)
				{
					if (first == -1)
						first = i;
					else
						second = i;
				}
			}
			if (second > -1 && second - first > 1 && (first != 0 || second != i - 1))
			{
				pIt = points.begin() + first;
				pIe = points.begin() + second;
				iTemp = polys.insert(++iIt, tmpPoly);//在当前位置之后插入一个新的多边形;
				iTemp->m_points.insert(iTemp->m_points.begin(), pIt, pIe + 1);
				points.erase(pIt+1, pIe);//删除前面那个多边形分出来的那堆点（拆分点是需要保留的）
				iIt = iTemp;
			}
		}
	}
}

int CheckPolygonClickPoint(const POINT &pt, const Nuclear::XPPolygon &polygon)
{
	CRect rect(pt.x - CToolsSoundArea::CHECK_CLICK_RADIUS, pt.y - CToolsSoundArea::CHECK_CLICK_RADIUS,
		pt.x + CToolsSoundArea::CHECK_CLICK_RADIUS, pt.y + CToolsSoundArea::CHECK_CLICK_RADIUS);
	//从后往前找
	std::vector<Nuclear::CPOINT>::const_reverse_iterator pIt = polygon.m_points.rbegin(), 
		pIe = polygon.m_points.rend();
	int i = static_cast<int>(polygon.m_points.size() - 1);
	for (;pIt!=pIe;++pIt, --i)
	{
		if (rect.PtInRect(*pIt))
			return i;
	}
	return -1;
}

int CheckPolygonClickLine(const POINT &pt, const Nuclear::XPPolygon &polygon)
{
	CRect rect(pt.x - CToolsSoundArea::CHECK_CLICK_RADIUS, pt.y - CToolsSoundArea::CHECK_CLICK_RADIUS,
		pt.x + CToolsSoundArea::CHECK_CLICK_RADIUS, pt.y + CToolsSoundArea::CHECK_CLICK_RADIUS);
	std::vector<Nuclear::CPOINT>::const_iterator pIt = polygon.m_points.begin(), 
		pIe = polygon.m_points.end();
	int i = static_cast<int>(polygon.m_points.size()) - 1;
	if (i < 0)
		return -1;
	Nuclear::CPOINT pt1 = polygon.m_points[i];
	i = 0;
	for (;pIt!=pIe;++pIt, ++i)
	{
		if (Nuclear::IsCross(pt1, *pIt, rect))
			return i;
		pt1 = *pIt;
	}
	return -1;

}

//====================================================背景音音量区域===================================================
void CToolsBkgVolCircleArea::ResetShape()
{
	if (m_pShape)
		delete m_pShape;
	m_pShape = dynamic_cast<Nuclear::XPCircle*>(m_circle.Extension(m_nTransition));
}

void CToolsBkgVolCircleArea::ResetAreaRegion(const POINT &mapSize)
{
	ResetCircleAreaRegion(mapSize, m_circle, m_nTransition, m_regionSet);
}

void CToolsBkgVolCircleArea::Move(const POINT& pt)
{
	m_circle.m_pos += pt;
	ResetShape();
}

void CToolsBkgVolCircleArea::SetTransition(int nTransition)
{
	m_nTransition = nTransition;
	ResetShape();
}

bool CToolsBkgVolCircleArea::SetShape(const Nuclear::XPIShape *pShape)
{
	const Nuclear::XPCircle *pCircle = dynamic_cast<const Nuclear::XPCircle*>(pShape);
	if (!pCircle)
		return false;
	m_circle = *pCircle;
	ResetShape();
	return true;
}

void CToolsBkgVolCircleArea::DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const
{
	m_pShape->Render(pRenderer, vp, isSelected?SELECTED_OUTTER_COLOR:NON_SELECTED_OUTTER_COLOR, Nuclear::XPFM_SOLIDFILL, 0);
	m_circle.Render(pRenderer, vp, isSelected?SELECTED_INNER_COLOR:NON_SELECTED_INNER_COLOR, Nuclear::XPFM_SOLIDFILL, 
		isSelected?CHECK_CLICK_RADIUS:0);
}

void CToolsBkgVolCircleArea::DrawArea(CDC* pDC, const CPoint& lt, float ratio) const
{
	CRect rect(m_circle.m_pos.x - m_circle.m_nRadius, m_circle.m_pos.y - m_circle.m_nRadius, 
		m_circle.m_pos.x + m_circle.m_nRadius,m_circle.m_pos.y + m_circle.m_nRadius);
	rect.left = static_cast<int>(rect.left * ratio - lt.x);
	rect.right = static_cast<int>(rect.right * ratio - lt.x);
	rect.top = static_cast<int>(rect.top * ratio - lt.y);
	rect.bottom = static_cast<int>(rect.bottom * ratio - lt.y);
	pDC->Ellipse(&rect);
}


int CToolsBkgVolCircleArea::CheckClickPoint(const POINT &pt) const
{
	return CheckCircleClickPoint(m_circle, pt);
}

void CToolsBkgVolCircleArea::GetShape(Nuclear::XPIShape* pShape) const
{
	*dynamic_cast<Nuclear::XPCircle*>(pShape) = m_circle;
}

CToolsBkgVolumeArea* CToolsBkgVolCircleArea::Clone() const
{
	CToolsBkgVolCircleArea *pArea = new CToolsBkgVolCircleArea();
	*pArea = *this;
	pArea->m_pShape = NULL;
	pArea->ResetShape();
	return pArea;
}

void CToolsBkgVolPolygonArea::ClearSPolygons()
{
	m_ToolsInnerPolygons.clear();
	m_ToolsOutterPolygons.clear();
	m_ToolsCutPairs.clear();
}

void CToolsBkgVolPolygonArea::ResetAreaRegion(const POINT &mapSize)
{
	ResetPolygonAreaRegion(mapSize, m_outterPolygon, m_regionSet);
}

void CToolsBkgVolPolygonArea::DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const
{
	Nuclear::FPOINT pos;
	PolygonCont::const_iterator vIt = m_ToolsOutterPolygons.begin(), vIe = m_ToolsOutterPolygons.end();
	Nuclear::XPCOLOR color = isSelected?SELECTED_OUTTER_COLOR:NON_SELECTED_OUTTER_COLOR;
	for (;vIt!=vIe;++vIt)
	{
		vIt->Render(pRenderer, vp, color, Nuclear::XPFM_SOLIDFILL, 0);
	}
	vIt = m_ToolsInnerPolygons.begin();
	vIe = m_ToolsInnerPolygons.end();
	color = isSelected?SELECTED_INNER_COLOR:NON_SELECTED_INNER_COLOR;
	int r = isSelected?CHECK_CLICK_RADIUS:0;
	for (;vIt!=vIe;++vIt)
	{
		vIt->Render(pRenderer, vp, color, Nuclear::XPFM_SOLIDFILL, 0);
		if (isSelected)
			vIt->Render(pRenderer, vp, SELECTED_DIV_COLOR, Nuclear::XPFM_WIREFRAME, 0);
	}
	m_innerPolygon.Render(pRenderer, vp, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME, r);
}

void CToolsBkgVolPolygonArea::DrawArea(CDC* pDC, const CPoint& lt, float ratio) const
{
	std::vector<Nuclear::CPOINT>::size_type n;
	/*PolygonCont::const_iterator vIt = m_ToolsOutterPolygons.begin(), vIe = m_ToolsOutterPolygons.end();
	for (;vIt!=vIe;++vIt)
	{*/
	const Nuclear::XPPolygon& poly = m_innerPolygon;
	n = poly.m_points.size();
	if (n > 2)
	{
		Nuclear::CPOINT a = poly.m_points[n - 1];
		a.x = static_cast<int>(a.x * ratio - lt.x);
		a.y = static_cast<int>(a.y * ratio - lt.y);
		pDC->MoveTo(a);
		for(std::vector<Nuclear::CPOINT>::size_type i = 0; i < n; ++i)
		{
			a = poly.m_points[i];
			a.x = static_cast<int>(a.x * ratio - lt.x);
			a.y = static_cast<int>(a.y * ratio - lt.y);
			pDC->LineTo(a);
		}
	}
	//}
}

Nuclear::CPOINT CToolsBkgVolPolygonArea::GetPos() const
{
	if (m_innerPolygon.m_points.size() > 0)
	{
		return m_innerPolygon.m_points.front();
	}
	return Nuclear::CPOINT(0, 0);
}

void CToolsBkgVolPolygonArea::Move(const POINT& pt)
{
	std::vector<Nuclear::CPOINT>::iterator pIt = m_innerPolygon.m_points.begin(), pIe = m_innerPolygon.m_points.end();
	for (;pIt!=pIe;++pIt)
	{
		*pIt += pt;
	}
	pIt = m_outterPolygon.m_points.begin();
	pIe = m_outterPolygon.m_points.end();
	for (;pIt!=pIe;++pIt)
	{
		*pIt += pt;
	}

	PolygonCont::iterator vIt = m_ToolsInnerPolygons.begin(), vIe = m_ToolsInnerPolygons.end();
	for (;vIt!=vIe;++vIt)
	{
		pIt = vIt->m_points.begin();
		pIe = vIt->m_points.end();
		for (;pIt!=pIe;++pIt)
		{
			*pIt += pt;
		}
	}
	vIt = m_ToolsOutterPolygons.begin();
	vIe = m_ToolsOutterPolygons.end();
	for (;vIt!=vIe;++vIt)
	{
		pIt = vIt->m_points.begin();
		pIe = vIt->m_points.end();
		for (;pIt!=pIe;++pIt)
		{
			*pIt += pt;
		}
	}
}

void CToolsBkgVolPolygonArea::ResetOutter()
{
	Nuclear::XPIShape *pShape = m_innerPolygon.Extension(m_nTransition);
	XPASSERT(pShape);
	m_outterPolygon = *dynamic_cast<Nuclear::XPPolygon*>(pShape);
	delete pShape;
	ResetToolsPolygon(m_outterPolygon, m_ToolsOutterPolygons);
}

void CToolsBkgVolPolygonArea::ResetToolsPolygon()
{
	ResetToolsPolygon(m_innerPolygon, m_ToolsInnerPolygons);
	ResetToolsPolygon(m_outterPolygon, m_ToolsOutterPolygons);
}

void CToolsBkgVolPolygonArea::ResetToolsPolygon(const Nuclear::XPPolygon& integrityPoly, PolygonCont& polys)
{
	RecutToolsPolygon(m_ToolsCutPairs,integrityPoly, polys);
}

void CToolsBkgVolPolygonArea::SetTransition(int nTransition)
{
	m_nTransition = nTransition;
	ResetOutter();
}

bool CToolsBkgVolPolygonArea::SetShape(const Nuclear::XPIShape *pShape)
{
	m_innerPolygon = *dynamic_cast<const Nuclear::XPPolygon*>(pShape);
	ResetOutter();
	ClearSPolygons();
	ResetToolsPolygon();
	return true;
}

int CToolsBkgVolPolygonArea::CheckClickPoint(const POINT &pt) const
{
	return CheckPolygonClickPoint(pt, m_innerPolygon);
}

int CToolsBkgVolPolygonArea::CheckClickLinePoint(const POINT &pt) const
{
	return CheckPolygonClickLine(pt, m_innerPolygon);
}

void CToolsBkgVolPolygonArea::GetShape(Nuclear::XPIShape* pShape) const
{
	*dynamic_cast<Nuclear::XPPolygon*>(pShape) = m_innerPolygon;
}

CToolsBkgVolumeArea* CToolsBkgVolPolygonArea::Clone() const
{
	CToolsBkgVolumeArea *pArea = new CToolsBkgVolPolygonArea();
	*pArea = *this;
	return pArea;
}

//====================================================背景音乐区域===================================================
void CToolsBkgSoundCircleArea::ResetAreaRegion(const POINT &mapSize)
{
	ResetCircleAreaRegion(mapSize, m_circle, 0, m_regionSet);
}

void CToolsBkgSoundCircleArea::Move(const POINT& pt)
{
	m_circle.m_pos += pt;
}

bool CToolsBkgSoundCircleArea::SetShape(const Nuclear::XPIShape *pShape)
{
	const Nuclear::XPCircle *pCircle = dynamic_cast<const Nuclear::XPCircle*>(pShape);
	if (!pCircle)
		return false;
	m_circle = *pCircle;
	return true;
}

void CToolsBkgSoundCircleArea::DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const
{
	m_circle.Render(pRenderer, vp, isSelected?SELECTED_INNER_COLOR:NON_SELECTED_INNER_COLOR, Nuclear::XPFM_SOLIDFILL, 0);
	m_circle.Render(pRenderer, vp, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME, isSelected?CHECK_CLICK_RADIUS:0);
}

void CToolsBkgSoundCircleArea::DrawArea(CDC* pDC, const CPoint& lt, float ratio) const
{
	CRect rect(m_circle.m_pos.x - m_circle.m_nRadius, m_circle.m_pos.y - m_circle.m_nRadius, 
		m_circle.m_pos.x + m_circle.m_nRadius, m_circle.m_pos.y + m_circle.m_nRadius);
	rect.left = static_cast<int>(rect.left * ratio - lt.x);
	rect.right = static_cast<int>(rect.right * ratio - lt.x);
	rect.top = static_cast<int>(rect.top * ratio - lt.y);
	rect.bottom = static_cast<int>(rect.bottom * ratio - lt.y);
	pDC->Ellipse(&rect);
}

int CToolsBkgSoundCircleArea::CheckClickPoint(const POINT &pt) const
{
	return CheckCircleClickPoint(m_circle, pt);
}

void CToolsBkgSoundCircleArea::GetShape(Nuclear::XPIShape* pShape) const
{
	*dynamic_cast<Nuclear::XPCircle*>(pShape) = m_circle;
}

void CToolsBkgSoundPolygonArea::ClearSPolygons()
{
	m_ToolsPolygons.clear();
	m_ToolsCutPairs.clear();
}

void CToolsBkgSoundPolygonArea::ResetAreaRegion(const POINT &mapSize)
{
	ResetPolygonAreaRegion(mapSize, m_polygon, m_regionSet);
}
void CToolsBkgSoundPolygonArea::DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const
{
	Nuclear::FPOINT pos;
	PolygonCont::const_iterator vIt = m_ToolsPolygons.begin(), vIe = m_ToolsPolygons.end();
	Nuclear::XPCOLOR color = isSelected?SELECTED_INNER_COLOR:NON_SELECTED_INNER_COLOR;
	int r = isSelected?CHECK_CLICK_RADIUS:0;
	for (;vIt!=vIe;++vIt)
	{
		vIt->Render(pRenderer, vp, color, Nuclear::XPFM_SOLIDFILL, 0);
		if (isSelected)
			vIt->Render(pRenderer, vp, SELECTED_DIV_COLOR, Nuclear::XPFM_WIREFRAME, 0);
	}
	m_polygon.Render(pRenderer, vp, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME, r);
}
void CToolsBkgSoundPolygonArea::DrawArea(CDC* pDC, const CPoint& lt, float ratio) const
{
	//PolygonCont::const_iterator vIt = m_ToolsPolygons.begin(), vIe = m_ToolsPolygons.end();
	std::vector<Nuclear::CPOINT>::size_type n;
	//for (;vIt!=vIe;++vIt)
	{
		//const Nuclear::XPPolygon& poly = *vIt;
		const Nuclear::XPPolygon& poly = m_polygon;
		n = poly.m_points.size();
		if (n > 2)
		{
			Nuclear::CPOINT a = poly.m_points[n - 1];
			a.x = static_cast<int>(a.x * ratio - lt.x);
			a.y = static_cast<int>(a.y * ratio - lt.y);
			pDC->MoveTo(a);
			for(std::vector<Nuclear::CPOINT>::size_type i = 0; i < n; ++i)
			{
				a = poly.m_points[i];
				a.x = static_cast<int>(a.x * ratio - lt.x);
				a.y = static_cast<int>(a.y * ratio - lt.y);
				pDC->LineTo(a);
			}
		}
	}
}

Nuclear::CPOINT CToolsBkgSoundPolygonArea::GetPos() const
{
	if (m_polygon.m_points.size() > 0)
	{
		return m_polygon.m_points.front();
	}
	return Nuclear::CPOINT(0, 0);
}

void CToolsBkgSoundPolygonArea::Move(const POINT& pt)
{
	std::vector<Nuclear::CPOINT>::iterator pIt = m_polygon.m_points.begin(), pIe = m_polygon.m_points.end();
	for (;pIt!=pIe;++pIt)
	{
		*pIt += pt;
	}
	PolygonCont::iterator vIt = m_ToolsPolygons.begin(), vIe = m_ToolsPolygons.end();
	for (;vIt!=vIe;++vIt)
	{
		pIt = vIt->m_points.begin();
		pIe = vIt->m_points.end();
		for (;pIt!=pIe;++pIt)
		{
			*pIt += pt;
		}
	}
}

void CToolsBkgSoundPolygonArea::ResetToolsPolygon()
{
	ResetToolsPolygon(m_polygon, m_ToolsPolygons);
}

void CToolsBkgSoundPolygonArea::ResetToolsPolygon(const Nuclear::XPPolygon& integrityPoly, PolygonCont& polys)
{
	RecutToolsPolygon(m_ToolsCutPairs,integrityPoly, polys);
}

bool CToolsBkgSoundPolygonArea::SetShape(const Nuclear::XPIShape *pShape)
{
	m_polygon = *dynamic_cast<const Nuclear::XPPolygon*>(pShape);
	ClearSPolygons();
	ResetToolsPolygon();
	return true;
}

int CToolsBkgSoundPolygonArea::CheckClickPoint(const POINT &pt) const
{
	return CheckPolygonClickPoint(pt, m_polygon);
}

int CToolsBkgSoundPolygonArea::CheckClickLinePoint(const POINT &pt) const
{
	return CheckPolygonClickLine(pt, m_polygon);
}

void CToolsBkgSoundPolygonArea::GetShape(Nuclear::XPIShape* pShape) const
{
	*dynamic_cast<Nuclear::XPPolygon*>(pShape) = m_polygon;
}

//====================================================环境音效区域===================================================
void CToolsEnvSoundCircleArea::ResetShape()
{
	if (m_pShape)
		delete m_pShape;
	m_pShape = dynamic_cast<Nuclear::XPCircle*>(m_circle.Extension(m_nTransition));
}

void CToolsEnvSoundCircleArea::ResetAreaRegion(const POINT &mapSize)
{
	ResetCircleAreaRegion(mapSize, m_circle, m_nTransition, m_regionSet);
}

void CToolsEnvSoundCircleArea::Move(const POINT& pt)
{
	m_circle.m_pos += pt;
	ResetShape();
}

void CToolsEnvSoundCircleArea::SetTransition(int nTransition)
{
	m_nTransition = nTransition;
	ResetShape();
}

bool CToolsEnvSoundCircleArea::SetShape(const Nuclear::XPIShape *pShape)
{
	const Nuclear::XPCircle *pCircle = dynamic_cast<const Nuclear::XPCircle*>(pShape);
	if (!pCircle)
		return false;
	m_circle = *pCircle;
	ResetShape();
	return true;
}

void CToolsEnvSoundCircleArea::DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const
{
	m_pShape->Render(pRenderer, vp, isSelected?SELECTED_OUTTER_COLOR:NON_SELECTED_OUTTER_COLOR, Nuclear::XPFM_SOLIDFILL, 0);
	m_circle.Render(pRenderer, vp, isSelected?SELECTED_INNER_COLOR:NON_SELECTED_INNER_COLOR, Nuclear::XPFM_SOLIDFILL, 
		isSelected?CHECK_CLICK_RADIUS:0);
}

void CToolsEnvSoundCircleArea::DrawArea(CDC* pDC, const CPoint& lt, float ratio) const
{
	CRect rect(m_circle.m_pos.x - m_circle.m_nRadius, m_circle.m_pos.y - m_circle.m_nRadius, 
		m_circle.m_pos.x + m_circle.m_nRadius,m_circle.m_pos.y + m_circle.m_nRadius);
	rect.left = static_cast<int>(rect.left * ratio - lt.x);
	rect.right = static_cast<int>(rect.right * ratio - lt.x);
	rect.top = static_cast<int>(rect.top * ratio - lt.y);
	rect.bottom = static_cast<int>(rect.bottom * ratio - lt.y);
	pDC->Ellipse(&rect);
}


int CToolsEnvSoundCircleArea::CheckClickPoint(const POINT &pt) const
{
	return CheckCircleClickPoint(m_circle, pt);
}

void CToolsEnvSoundCircleArea::GetShape(Nuclear::XPIShape* pShape) const
{
	*dynamic_cast<Nuclear::XPCircle*>(pShape) = m_circle;
}

CToolsEnvSoundArea* CToolsEnvSoundCircleArea::Clone() const
{
	CToolsEnvSoundCircleArea *pArea = new CToolsEnvSoundCircleArea();
	*pArea = *this;
	pArea->m_pShape = NULL;
	pArea->ResetShape();
	return pArea;
}

void CToolsEnvSoundPolygonArea::ClearSPolygons()
{
	m_ToolsInnerPolygons.clear();
	m_ToolsOutterPolygons.clear();
	m_ToolsCutPairs.clear();
}

void CToolsEnvSoundPolygonArea::ResetAreaRegion(const POINT &mapSize)
{
	ResetPolygonAreaRegion(mapSize, m_outterPolygon, m_regionSet);
}

void CToolsEnvSoundPolygonArea::DrawArea(const Nuclear::CRECT& vp, Nuclear::Renderer* pRenderer, bool isSelected) const
{
	Nuclear::FPOINT pos;
	PolygonCont::const_iterator vIt = m_ToolsOutterPolygons.begin(), vIe = m_ToolsOutterPolygons.end();
	Nuclear::XPCOLOR color = isSelected?SELECTED_OUTTER_COLOR:NON_SELECTED_OUTTER_COLOR;
	for (;vIt!=vIe;++vIt)
	{
		vIt->Render(pRenderer, vp, color, Nuclear::XPFM_SOLIDFILL, 0);
	}
	vIt = m_ToolsInnerPolygons.begin();
	vIe = m_ToolsInnerPolygons.end();
	color = isSelected?SELECTED_INNER_COLOR:NON_SELECTED_INNER_COLOR;
	int r = isSelected?CHECK_CLICK_RADIUS:0;
	for (;vIt!=vIe;++vIt)
	{
		vIt->Render(pRenderer, vp, color, Nuclear::XPFM_SOLIDFILL, 0);
		if (isSelected)
			vIt->Render(pRenderer, vp, SELECTED_DIV_COLOR, Nuclear::XPFM_WIREFRAME, 0);
	}
	m_innerPolygon.Render(pRenderer, vp, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME, r);
}

void CToolsEnvSoundPolygonArea::DrawArea(CDC* pDC, const CPoint& lt, float ratio) const
{
	std::vector<Nuclear::CPOINT>::size_type n;
	/*PolygonCont::const_iterator vIt = m_ToolsOutterPolygons.begin(), vIe = m_ToolsOutterPolygons.end();
	for (;vIt!=vIe;++vIt)
	{*/
		const Nuclear::XPPolygon& poly = m_innerPolygon;
		n = poly.m_points.size();
		if (n > 2)
		{
			Nuclear::CPOINT a = poly.m_points[n - 1];
			a.x = static_cast<int>(a.x * ratio - lt.x);
			a.y = static_cast<int>(a.y * ratio - lt.y);
			pDC->MoveTo(a);
			for(std::vector<Nuclear::CPOINT>::size_type i = 0; i < n; ++i)
			{
				a = poly.m_points[i];
				a.x = static_cast<int>(a.x * ratio - lt.x);
				a.y = static_cast<int>(a.y * ratio - lt.y);
				pDC->LineTo(a);
			}
		}
	//}
}

Nuclear::CPOINT CToolsEnvSoundPolygonArea::GetPos() const
{
	if (m_innerPolygon.m_points.size() > 0)
	{
		return m_innerPolygon.m_points.front();
	}
	return Nuclear::CPOINT(0, 0);
}

void CToolsEnvSoundPolygonArea::Move(const POINT& pt)
{
	std::vector<Nuclear::CPOINT>::iterator pIt = m_innerPolygon.m_points.begin(), pIe = m_innerPolygon.m_points.end();
	for (;pIt!=pIe;++pIt)
	{
		*pIt += pt;
	}
	pIt = m_outterPolygon.m_points.begin();
	pIe = m_outterPolygon.m_points.end();
	for (;pIt!=pIe;++pIt)
	{
		*pIt += pt;
	}

	PolygonCont::iterator vIt = m_ToolsInnerPolygons.begin(), vIe = m_ToolsInnerPolygons.end();
	for (;vIt!=vIe;++vIt)
	{
		pIt = vIt->m_points.begin();
		pIe = vIt->m_points.end();
		for (;pIt!=pIe;++pIt)
		{
			*pIt += pt;
		}
	}
	vIt = m_ToolsOutterPolygons.begin();
	vIe = m_ToolsOutterPolygons.end();
	for (;vIt!=vIe;++vIt)
	{
		pIt = vIt->m_points.begin();
		pIe = vIt->m_points.end();
		for (;pIt!=pIe;++pIt)
		{
			*pIt += pt;
		}
	}
}

void CToolsEnvSoundPolygonArea::ResetOutter()
{
	Nuclear::XPIShape *pShape = m_innerPolygon.Extension(m_nTransition);
	XPASSERT(pShape);
	m_outterPolygon = *dynamic_cast<Nuclear::XPPolygon*>(pShape);
	delete pShape;
	ResetToolsPolygon(m_outterPolygon, m_ToolsOutterPolygons);
}

void CToolsEnvSoundPolygonArea::ResetToolsPolygon()
{
	ResetToolsPolygon(m_innerPolygon, m_ToolsInnerPolygons);
	ResetToolsPolygon(m_outterPolygon, m_ToolsOutterPolygons);
}

void CToolsEnvSoundPolygonArea::ResetToolsPolygon(const Nuclear::XPPolygon& integrityPoly, PolygonCont& polys)
{
	RecutToolsPolygon(m_ToolsCutPairs,integrityPoly, polys);
}

void CToolsEnvSoundPolygonArea::SetTransition(int nTransition)
{
	m_nTransition = nTransition;
	ResetOutter();
}

bool CToolsEnvSoundPolygonArea::SetShape(const Nuclear::XPIShape *pShape)
{
	m_innerPolygon = *dynamic_cast<const Nuclear::XPPolygon*>(pShape);
	ResetOutter();
	ClearSPolygons();
	ResetToolsPolygon();
	return true;
}

int CToolsEnvSoundPolygonArea::CheckClickPoint(const POINT &pt) const
{
	return CheckPolygonClickPoint(pt, m_innerPolygon);
}

int CToolsEnvSoundPolygonArea::CheckClickLinePoint(const POINT &pt) const
{
	return CheckPolygonClickLine(pt, m_innerPolygon);
}

void CToolsEnvSoundPolygonArea::GetShape(Nuclear::XPIShape* pShape) const
{
	*dynamic_cast<Nuclear::XPPolygon*>(pShape) = m_innerPolygon;
}

CToolsEnvSoundArea* CToolsEnvSoundPolygonArea::Clone() const
{
	CToolsEnvSoundPolygonArea *pArea = new CToolsEnvSoundPolygonArea();
	*pArea = *this;
	return pArea;
}