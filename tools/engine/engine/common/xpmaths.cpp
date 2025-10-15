#include "stdafx.h"
#include "xptypes.h"
#include "..\renderer\renderer.h"

namespace Nuclear
{
	const float XP_SIN[181] = 
	{
		sin(TORADIANS(0)), sin(TORADIANS(1)), sin(TORADIANS(2)), sin(TORADIANS(3)), sin(TORADIANS(4)), 
		sin(TORADIANS(5)), sin(TORADIANS(6)), sin(TORADIANS(7)), sin(TORADIANS(8)), sin(TORADIANS(9)), 
		sin(TORADIANS(10)), sin(TORADIANS(11)), sin(TORADIANS(12)), sin(TORADIANS(13)), sin(TORADIANS(14)), 
		sin(TORADIANS(15)), sin(TORADIANS(16)), sin(TORADIANS(17)), sin(TORADIANS(18)), sin(TORADIANS(19)), 
		sin(TORADIANS(20)), sin(TORADIANS(21)), sin(TORADIANS(22)), sin(TORADIANS(23)), sin(TORADIANS(24)), 
		sin(TORADIANS(25)), sin(TORADIANS(26)), sin(TORADIANS(27)), sin(TORADIANS(28)), sin(TORADIANS(29)), 
		sin(TORADIANS(30)), sin(TORADIANS(31)), sin(TORADIANS(32)), sin(TORADIANS(33)), sin(TORADIANS(34)), 
		sin(TORADIANS(35)), sin(TORADIANS(36)), sin(TORADIANS(37)), sin(TORADIANS(38)), sin(TORADIANS(39)), 
		sin(TORADIANS(40)), sin(TORADIANS(41)), sin(TORADIANS(42)), sin(TORADIANS(43)), sin(TORADIANS(44)), 
		sin(TORADIANS(45)), sin(TORADIANS(46)), sin(TORADIANS(47)), sin(TORADIANS(48)), sin(TORADIANS(49)), 
		sin(TORADIANS(50)), sin(TORADIANS(51)), sin(TORADIANS(52)), sin(TORADIANS(53)), sin(TORADIANS(54)), 
		sin(TORADIANS(55)), sin(TORADIANS(56)), sin(TORADIANS(57)), sin(TORADIANS(58)), sin(TORADIANS(59)), 
		sin(TORADIANS(60)), sin(TORADIANS(61)), sin(TORADIANS(62)), sin(TORADIANS(63)), sin(TORADIANS(64)), 
		sin(TORADIANS(65)), sin(TORADIANS(66)), sin(TORADIANS(67)), sin(TORADIANS(68)), sin(TORADIANS(69)), 
		sin(TORADIANS(70)), sin(TORADIANS(71)), sin(TORADIANS(72)), sin(TORADIANS(73)), sin(TORADIANS(74)), 
		sin(TORADIANS(75)), sin(TORADIANS(76)), sin(TORADIANS(77)), sin(TORADIANS(78)), sin(TORADIANS(79)), 
		sin(TORADIANS(80)), sin(TORADIANS(81)), sin(TORADIANS(82)), sin(TORADIANS(83)), sin(TORADIANS(84)), 
		sin(TORADIANS(85)), sin(TORADIANS(86)), sin(TORADIANS(87)), sin(TORADIANS(88)), sin(TORADIANS(89)), 
		sin(TORADIANS(90)), sin(TORADIANS(91)), sin(TORADIANS(92)), sin(TORADIANS(93)),	sin(TORADIANS(94)),
		sin(TORADIANS(95)), sin(TORADIANS(96 )),sin(TORADIANS(97 )),sin(TORADIANS(98 )),sin(TORADIANS(99 )),
		sin(TORADIANS(100)),sin(TORADIANS(101)),sin(TORADIANS(102)),sin(TORADIANS(103)),sin(TORADIANS(104)),
		sin(TORADIANS(105)),sin(TORADIANS(106)),sin(TORADIANS(107)),sin(TORADIANS(108)),sin(TORADIANS(109)),
		sin(TORADIANS(110)),sin(TORADIANS(111)),sin(TORADIANS(112)),sin(TORADIANS(113)),sin(TORADIANS(114)),
		sin(TORADIANS(115)),sin(TORADIANS(116)),sin(TORADIANS(117)),sin(TORADIANS(118)),sin(TORADIANS(119)),
		sin(TORADIANS(120)),sin(TORADIANS(121)),sin(TORADIANS(122)),sin(TORADIANS(123)),sin(TORADIANS(124)),
		sin(TORADIANS(125)),sin(TORADIANS(126)),sin(TORADIANS(127)),sin(TORADIANS(128)),sin(TORADIANS(129)),
		sin(TORADIANS(130)),sin(TORADIANS(131)),sin(TORADIANS(132)),sin(TORADIANS(133)),sin(TORADIANS(134)),
		sin(TORADIANS(135)),sin(TORADIANS(136)),sin(TORADIANS(137)),sin(TORADIANS(138)),sin(TORADIANS(139)),
		sin(TORADIANS(140)),sin(TORADIANS(141)),sin(TORADIANS(142)),sin(TORADIANS(143)),sin(TORADIANS(144)),
		sin(TORADIANS(145)),sin(TORADIANS(146)),sin(TORADIANS(147)),sin(TORADIANS(148)),sin(TORADIANS(149)),
		sin(TORADIANS(150)),sin(TORADIANS(151)),sin(TORADIANS(152)),sin(TORADIANS(153)),sin(TORADIANS(154)),
		sin(TORADIANS(155)),sin(TORADIANS(156)),sin(TORADIANS(157)),sin(TORADIANS(158)),sin(TORADIANS(159)),
		sin(TORADIANS(160)),sin(TORADIANS(161)),sin(TORADIANS(162)),sin(TORADIANS(163)),sin(TORADIANS(164)),
		sin(TORADIANS(165)),sin(TORADIANS(166)),sin(TORADIANS(167)),sin(TORADIANS(168)),sin(TORADIANS(169)),
		sin(TORADIANS(170)),sin(TORADIANS(171)),sin(TORADIANS(172)),sin(TORADIANS(173)),sin(TORADIANS(174)),
		sin(TORADIANS(175)),sin(TORADIANS(176)),sin(TORADIANS(177)),sin(TORADIANS(178)),sin(TORADIANS(179)),
		sin(TORADIANS(180))
	};
	
	bool FPOINT::isInTriangle(FPOINT p0, FPOINT p1, FPOINT p2) const
	{
		XPVECTOR2 V0 = p2-p0;
		XPVECTOR2 V1 = p1-p0;
		XPVECTOR2 V2 = *this-p0;

		float dot00 = DotProduct(V0, V0);
		float dot01 = DotProduct(V0, V1);
		float dot02 = DotProduct(V0, V2);
		float dot11 = DotProduct(V1, V1);
		float dot12 = DotProduct(V1, V2);
		float fcoef = 1.0f/(dot00 * dot11 - dot01 * dot01);

		float u = (dot11 * dot02 - dot01 * dot12) *fcoef;
		float v = (dot00 * dot12 - dot01 * dot02) *fcoef;

		return (u>0)&&(v>0)&&(u+v<1);
	}

	void XPCircle::Render(Renderer *pRenderer, const CRECT &viewport, XPCOLOR color, XPFILLMODE fillmode, int pointR) const
	{
		pRenderer->DrawCircle(static_cast<float>(m_pos.x-viewport.left), static_cast<float>(m_pos.y-viewport.top), 
			static_cast<float>(m_nRadius), color, fillmode);
		if (pointR)
		{
			Nuclear::FRECT rect;
			rect.left = static_cast<float>(m_pos.x + m_nRadius - pointR - viewport.left);
			rect.top = static_cast<float>(m_pos.y - pointR - viewport.top);
			rect.right = rect.left + pointR * 2;
			rect.bottom = rect.top + pointR * 2;
			pRenderer->DrawBox(rect, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);
		}
	}

	float XPPolygon::DistanceTo(int x, int y) const
	{
		const std::vector<CPOINT>::size_type n = m_points.size();
		CPOINT q(x, y);
		switch (n)
		{
		case 0:
			return 0.0f;
			break;
		case 1:
			return sqrtf(static_cast<float>(m_points[0].Dis2(q)));
			break;
		default:
			break;
		}

		float result = 3.4e38f;
		CPOINT a = m_points[n - 1];
		float t, tmpr;
		for(std::vector<CPOINT>::size_type i = 0; i < n; ++i)
		{
			const CPOINT &b = m_points[i];
			CPOINT d(b.x - a.x, b.y - a.y);
			t = (d.x * (x - a.x) + d.y * (y - a.y)) / static_cast<float>(d.x * d.x + d.y * d.y);
			if (t <= 0)	//垂足不在线段上，距离a点更近
			{
				tmpr = sqrtf(static_cast<float>(a.Dis2(q)));
			} else if (t >= 1) {//垂足不在线段上，距离b点更近
				tmpr = sqrtf(static_cast<float>(b.Dis2(q)));
			} else {	//垂足在线段上
				FPOINT q1(d.x * t + a.x, d.y * t + a.y);	//垂足
				tmpr = sqrtf(q1.Dis2(q));
			}
			if (result > tmpr)
				result = tmpr;
			a = b;
		}
		return result;
	}

	void XPPolygon::Render(Renderer *pRenderer, const CRECT &viewport, XPCOLOR color, XPFILLMODE fillmode, int pointR) const
	{
		std::vector<CPOINT> pts(m_points.size());
		std::vector<CPOINT>::iterator fit = pts.begin();
		std::vector<CPOINT>::const_iterator cit = m_points.begin(), cie = m_points.end();
		for (;cit!=cie;++cit,++fit)
		{
			fit->x = cit->x - viewport.left;
			fit->y = cit->y - viewport.top;
		}

		pRenderer->DrawPolygon(pts, color, fillmode);

	//	pRenderer->DrawPolygon(m_points, color, fillmode);

		if (pointR)
		{
			int i = 0;
			cit = m_points.begin();
			cie = m_points.end();
			fit = pts.begin();
			int twoPointR = pointR * 2;
			FRECT* fRects = new FRECT[m_points.size()];
			for (;cit!=cie;++cit,++i,++fit)
			{
				fRects[i].left = (float)(fit->x - pointR);
				fRects[i].top = (float)(fit->y - pointR);
				fRects[i].right = fRects[i].left + twoPointR;
				fRects[i].bottom = fRects[i].top + twoPointR;
				if (i == 0)
				{
					pRenderer->DrawBox(fRects, 1, 0xFFFF0000, Nuclear::XPFM_WIREFRAME);
				}
			}
			pRenderer->DrawBox(fRects+1, i-1, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);
			delete [] fRects;
		}
	}

	XPIShape* XPPolygon::Extension(int l) const
	{
		const std::vector<CPOINT>::size_type n = m_points.size();
		if (n <=2 )
			return NULL;
		CPOINT a = m_points[n - 1];
		std::vector<XPVECTOR2> nvets(n);	//各线段的单位化向量单
		float mod;
		for(std::vector<CPOINT>::size_type i = 0; i < n; ++i)
		{
			const CPOINT &b = m_points[i];
			nvets[i] = b - a;
			mod = sqrtf(nvets[i].x * nvets[i].x + nvets[i].y * nvets[i].y);
			nvets[i].x /= mod;
			nvets[i].y /= mod;
			a = b;
		}
		XPPolygon* pPolygon = new XPPolygon();
		pPolygon->m_points.resize(n);
		float sinAlpha;
		XPVECTOR2 nva = nvets[n - 1];
		for(std::vector<XPVECTOR2>::size_type i = 0; i < n; ++i)
		{
			const XPVECTOR2 &nvb = nvets[i];
			sinAlpha = nva.x * nvb.y - nva.y * nvb.x;
			if (i == 0)
			{
				pPolygon->m_points[n-1] = m_points[n-1] + (nvb - nva) * (l / sinAlpha);
				CPOINT tmpPt = m_points[n-1] + (nvb - nva) / sinAlpha;
				if (IsPointIn(tmpPt.x, tmpPt.y))
				{
					l *= -1;
					pPolygon->m_points[n-1] = m_points[n-1] + (nvb - nva) * (l / sinAlpha);
				}
			} else {
				pPolygon->m_points[i-1] = m_points[i-1] + (nvb - nva) * (l / sinAlpha);
			}
			nva = nvb;
		}
		return pPolygon;
	}

	bool RectClipPolygon::ClipPolygon(CRECT clipWindow, const std::vector<FPOINT>& polygon, std::vector<FPOINT>& polygonResult)
	{
		std::vector<FPOINT> polygonTemp = polygon;

		LineClipPolygon(clipWindow, LEFTLINE, polygonTemp, polygonResult);
		LineClipPolygon(clipWindow, TOPLINE, polygonTemp, polygonResult);
		LineClipPolygon(clipWindow, RIGHTLINE, polygonTemp, polygonResult);
		LineClipPolygon(clipWindow, BOTTOMLINE, polygonTemp, polygonResult);

		return true;
	}

	bool RectClipPolygon::LineClipPolygon(CRECT clipWindow, RectBorderType lineType, std::vector<FPOINT>& polygon, std::vector<FPOINT>& polygonResult)
	{
		polygonResult.clear();

		int cnt = (int)(polygon.size())-1;
		FPOINT pt,pt1,pt2;
		for(int i=0; i<=cnt; i++)
		{
			pt1 = polygon[i];
			if(i != cnt)
				pt2 = polygon[i+1];
			else
				pt2 = polygon[0];

			switch(lineType)
			{
			case LEFTLINE:
				{
					if(pt1.x >= clipWindow.left)
						polygonResult.push_back(pt1);
					if((pt1.x < clipWindow.left && pt2.x > clipWindow.left) || (pt2.x < clipWindow.left && pt1.x > clipWindow.left))
					{
						if(pt1.y == pt2.y)
						{
							pt.x = (float)(clipWindow.left);
							pt.y = pt1.y;
						}
						else
							pt = ComputeCrossPoint(clipWindow, lineType, pt1, pt2);
						polygonResult.push_back(pt);
					}
				}
				break;

			case TOPLINE:
				{
					if(pt1.y >= clipWindow.top)
						polygonResult.push_back(pt1);
					if((pt1.y < clipWindow.top && pt2.y > clipWindow.top) || (pt2.y < clipWindow.top && pt1.y > clipWindow.top))
					{
						if(pt1.x == pt2.x)
						{
							pt.x = pt1.x;
							pt.y = (float)(clipWindow.top);
						}
						else
							pt = ComputeCrossPoint(clipWindow, lineType, pt1, pt2);
						polygonResult.push_back(pt);
					}
				}
				break;

			case RIGHTLINE:
				{
					if(pt1.x <= clipWindow.right)
						polygonResult.push_back(pt1);
					if((pt1.x < clipWindow.right && pt2.x > clipWindow.right) || (pt2.x < clipWindow.right && pt1.x > clipWindow.right))
					{
						if(pt1.y == pt2.y)
						{
							pt.x = (float)(clipWindow.right);
							pt.y = pt1.y;
						}
						else
							pt = ComputeCrossPoint(clipWindow, lineType, pt1, pt2);
						polygonResult.push_back(pt);
					}
				}
				break;

			case BOTTOMLINE:
				{
					if(pt1.y <= clipWindow.bottom)
						polygonResult.push_back(pt1);
					if((pt1.y < clipWindow.bottom && pt2.y > clipWindow.bottom) || (pt2.y < clipWindow.bottom && pt1.y > clipWindow.bottom))
					{
						if(pt1.x == pt2.x)
						{
							pt.x = pt1.x;
							pt.y = (float)(clipWindow.bottom);
						}
						else
							pt = ComputeCrossPoint(clipWindow, lineType, pt1, pt2);
						polygonResult.push_back(pt);
					}
				}
				break;
			default:
				break;
			}
		}

		polygon.clear();
		polygon = polygonResult;

		return true;
	}

	FPOINT RectClipPolygon::ComputeCrossPoint(CRECT clipWindow, RectBorderType lineType, FPOINT polygonPt0, FPOINT polygonPt1)
	{
		//线段与直线的交点..
		FPOINT pt;

		float k = (polygonPt1.y-polygonPt0.y)/(polygonPt1.x-polygonPt0.x);
		float b = (polygonPt1.x*polygonPt0.y-polygonPt0.x*polygonPt1.y)/(polygonPt1.x-polygonPt0.x);

		switch(lineType)
		{
		case LEFTLINE:
			{
				pt.x = (float)(clipWindow.left);
				pt.y = k*pt.x+b;
			}
			break;
		case TOPLINE:
			{
				pt.y = (float)(clipWindow.top);
				pt.x = (pt.y-b)/k;
			}
			break;
		case RIGHTLINE:
			{
				pt.x = (float)(clipWindow.right);
				pt.y = k*pt.x+b;
			}
			break;
		case BOTTOMLINE:
			{
				pt.y = (float)(clipWindow.bottom);
				pt.x = (pt.y-b)/k;
			}
			break;
		default:
			break;
		}

		return pt;
	}

	bool PolygonToTriangles::DecomPolygon(const std::vector<FPOINT>& vextexs, std::vector<TRIANGLE>& triangles)
	{
		int cnt = (int)(vextexs.size());
		if(cnt>V_MAX || cnt<3)
			return false;

		triangles.clear();

		int prev, cur, next;
		int vp[V_MAX];
		int count;
		int min_vert;
		int i;
		float dist;
		double min_dist;
		ORIENTATIONTYPE poly_orientation;

		poly_orientation = orientation(vextexs);

		for(i=0; i<cnt; i++)
			vp[i] = i;

		count = cnt;
		while(count > 3)
		{
			min_dist = BIG; 
			min_vert = 0; 
			for(cur=0; cur<count; cur++)
			{
				prev = cur-1;
				next = cur+1;
				if(cur == 0)
					prev = count-1;
				else if(cur == count-1)
					next = 0;

				if( (determinant(vp[prev], vp[cur], vp[next], vextexs) == poly_orientation)
					&& interior( vp[prev], vp[cur], vp[next], vextexs, vp, count, poly_orientation )
					&& ((dist = distance2( vextexs[vp[prev]].x, vextexs[vp[prev]].y,
					vextexs[vp[next]].x, vextexs[vp[next]].y )) < min_dist) )
				{
					min_dist = dist;
					min_vert = cur;
				}
			}

			if(min_dist == BIG)
			{
				return false;
			}

			prev = min_vert-1;
			next = min_vert+1;
			if(min_vert == 0)
				prev = count-1;
			else if(min_vert == count-1)
				next = 0;

			TRIANGLE triangle;
			triangle.pt0.x = vextexs[vp[prev]].x;
			triangle.pt0.y = vextexs[vp[prev]].y;
			triangle.pt1.x = vextexs[vp[min_vert]].x;
			triangle.pt1.y = vextexs[vp[min_vert]].y;
			triangle.pt2.x = vextexs[vp[next]].x;
			triangle.pt2.y = vextexs[vp[next]].y;
			triangles.push_back(triangle);

			count -= 1;
			for(i=min_vert; i<count; i++)
				vp[i] = vp[i+1];
		}

		TRIANGLE triangle;
		triangle.pt0.x = vextexs[vp[0]].x;
		triangle.pt0.y = vextexs[vp[0]].y;
		triangle.pt1.x = vextexs[vp[1]].x;
		triangle.pt1.y = vextexs[vp[1]].y;
		triangle.pt2.x = vextexs[vp[2]].x;
		triangle.pt2.y = vextexs[vp[2]].y;
		triangles.push_back(triangle);

		return true;
	}

	bool PolygonToTriangles::DecomPolygon(const std::vector<FPOINT>& vextexs, const XPCOLOR& cr, std::vector<TCVERTEX>& triangles)
	{
		triangles.clear();

		int prev, cur, next;
		int vp[V_MAX];
		int count;
		int min_vert;
		int i;
		float dist;
		double min_dist;
		ORIENTATIONTYPE poly_orientation;

		int cnt = (int)(vextexs.size());

		if(cnt>V_MAX || cnt<3)
		{
			return false;
		}

		poly_orientation = orientation(vextexs);

		for(i=0; i<cnt; i++)
			vp[i] = i;

		count = cnt;
		while(count > 3)
		{
			min_dist = BIG; 
			min_vert = 0; 
			for(cur=0; cur<count; cur++)
			{
				prev = cur-1;
				next = cur+1;
				if(cur == 0)
					prev = count-1;
				else if(cur == count-1)
					next = 0;

				if( (determinant(vp[prev], vp[cur], vp[next], vextexs) == poly_orientation)
					&& interior( vp[prev], vp[cur], vp[next], vextexs, vp, count, poly_orientation )
					&& ((dist = distance2( vextexs[vp[prev]].x, vextexs[vp[prev]].y,
					vextexs[vp[next]].x, vextexs[vp[next]].y )) < min_dist) )
				{
					min_dist = dist;
					min_vert = cur;
				}
			}

			if(min_dist == BIG)
			{
				return false;
			}

			prev = min_vert-1;
			next = min_vert+1;
			if(min_vert == 0)
				prev = count-1;
			else if(min_vert == count-1)
				next = 0;

			TCVERTEX vex;
			vex.x = vextexs[vp[prev]].x;
			vex.y = vextexs[vp[prev]].y;
			vex.c = cr.data;
			triangles.push_back(vex);

			vex.x = vextexs[vp[min_vert]].x;
			vex.y = vextexs[vp[min_vert]].y;
			vex.c = cr.data;
			triangles.push_back(vex);

			vex.x = vextexs[vp[next]].x;
			vex.y = vextexs[vp[next]].y;
			vex.c = cr.data;
			triangles.push_back(vex);

			count -= 1;
			for(i=min_vert; i<count; i++)
				vp[i] = vp[i+1];
		}

		TCVERTEX vex;
		vex.x = vextexs[vp[0]].x;
		vex.y = vextexs[vp[0]].y;
		vex.c = cr.data;
		triangles.push_back(vex);

		vex.x = vextexs[vp[1]].x;
		vex.y = vextexs[vp[1]].y;
		vex.c = cr.data;
		triangles.push_back(vex);

		vex.x = vextexs[vp[2]].x;
		vex.y = vextexs[vp[2]].y;
		vex.c = cr.data;
		triangles.push_back(vex);

		return true;
	}

	PolygonToTriangles::ORIENTATIONTYPE PolygonToTriangles::orientation(const std::vector<FPOINT>& vextexs)
	{
		float area;
		int i;
		int cnt = vextexs.size();
		area = vextexs[cnt-1].x * vextexs[0].y - vextexs[0].x * vextexs[cnt-1].y;
		for(i=0; i<cnt-1; i++)
			area += vextexs[i].x * vextexs[i+1].y - vextexs[i+1].x * vextexs[i].y;

		if (area >= 0.0)
			return COUNTER_CLOCKWISE;
		else
			return CLOCKWISE;
	}

	PolygonToTriangles::ORIENTATIONTYPE PolygonToTriangles::determinant(int p1, int p2, int p3, const std::vector<FPOINT>& vextexs)
	{
		float x1, x2, x3, y1, y2, y3;
		float determ;

		x1 = vextexs[p1].x;
		y1 = vextexs[p1].y;
		x2 = vextexs[p2].x;
		y2 = vextexs[p2].y;
		x3 = vextexs[p3].x;
		y3 = vextexs[p3].y;

		determ = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
		if (determ >= 0.0f)
			return COUNTER_CLOCKWISE;
		else
			return CLOCKWISE;
	}

	float PolygonToTriangles::distance2(float x1, float y1, float x2, float y2 )
	{
		float xd, yd;
		float dist2; 

		xd = x1-x2;
		yd = y1-y2;
		dist2 = xd*xd + yd*yd;

		return dist2;
	}

	int PolygonToTriangles::interior(int p1, int p2, int p3, const std::vector<FPOINT>& vextexs, int vp[], int cnt, int poly_or)
	{
		int i;
		int p;

		for(i=0; i<cnt; i++)
		{
			p = vp[i];
			if((p == p1) || (p == p2) || (p == p3))
				continue;
			if ( (determinant( p2, p1, p, vextexs ) == poly_or)
				|| (determinant( p1, p3, p, vextexs ) == poly_or)
				|| (determinant( p3, p2, p, vextexs ) == poly_or) ) 
			{
				continue;
			}
			else
			{
				return 0;
			}
		}
		return 1;
	}

	RECT GetMaxRect(const RECT &r, float s /* height/width */)
	{
		RECT rt = r;
		LONG height = r.bottom - r.top;
		LONG width = r.right - r.left;

		float ls = height/(float)width;

		if( ls > s )
		{
			float aheight = width*s;
			rt.top = (LONG)((float)height/2 + r.top - aheight/2);
			rt.bottom = (LONG)((float)height/2 + r.top + aheight/2);
		}
		else
		{
			float awidth = height/s;
			rt.left = (LONG)((float)width/2 + r.left - awidth/2);
			rt.right = (LONG)((float)width/2 + r.left + awidth/2);			
		}
		return rt;
	}

	bool IsPower2(int i)
	{
		return i>0 && !(i&(i-1));
	}

	//就在本文件使用
	static void pstp2(int length, std::vector<int>& pts)
	{
		if( length <= 0 )
			return;

		if( length > 512 )
		{
			pts.push_back(512);
			pstp2(length - 512, pts);
			return;
		}

		int np2 = 1;
		while( np2 < length) np2 *=2;

		if( np2 == length || np2 - length <= 64 )
		{
			pts.push_back(length);
		}
		else
		{
			pts.push_back(np2/2);
			pstp2(length - np2/2, pts);
		}
	}

	bool PartitionRectToPower2(const CRECT &src, std::vector<CRECT> &dst)
	{
		dst.clear();
		std::vector<int> pts_w;
		std::vector<int> pts_h;
		pstp2( src.Width(), pts_w);
		pstp2( src.Height(), pts_h);
		int top = 0;
		for(int r=0; r < (int)pts_h.size(); ++r)
		{
			int left = 0;
			int bottom = top + pts_h[r];
			for(int c=0; c < (int)pts_w.size(); ++c)
			{
				int right = pts_w[c] + left;
				//
				dst.push_back(CRECT(left, top, right, bottom));
				//
				left = right;
			}
			top = bottom;
		}
		if( dst.empty() )
			return false;
		return true;
	}

	void TransToDiamondRadix(int x, int y, CPOINT& pointInDiamonRadix)
	{
		y = static_cast<int>(static_cast<float>(y) / COS_58);
		pointInDiamonRadix.x = static_cast<int>((y + x) / 2);
		pointInDiamonRadix.y = static_cast<int>((y - x) / 2);
	}
};
