#include "stdafx.h"
#include "ttfoutline.h"

//#include <assert.h>


namespace Nuclear
{


CTTFOutline::CTTFOutline(void)
{
}

CTTFOutline::~CTTFOutline(void)
{
//	assert( m_vectorContour.empty() );
	m_vectorContour.clear();
}

int CTTFOutline::IntFromFixed(FIXED f)
{
	if (f.fract >= 0x8000)
		return(f.value + 1);
	else
		return(f.value);
}

FIXED CTTFOutline::fxDiv2(FIXED fxVal1, FIXED fxVal2)
{
	long l;
	l = (*((long*)&(fxVal1)) + *((long*)&(fxVal2)))/2;
	return(*(FIXED *)&l);
}

UINT CTTFOutline::MakeBezierFromLine( POINT *pPts, POINT startpt, POINT endpt )
{
	UINT cTotal = 0; // starting point of Bezier
	pPts[cTotal] = startpt;
	cTotal++; // 1rst Control, pt == endpoint makes Bezier a line
	//pPts[cTotal].x = endpt.x;
	//pPts[cTotal].y = endpt.y;
	pPts[cTotal] = endpt;
	cTotal++; // 2nd Control, pt == startpoint makes Bezier a line
	//pPts[cTotal].x = startpt.x;
	//pPts[cTotal].y = startpt.y;
	pPts[cTotal] = startpt;
	cTotal++; // ending point of Bezier
	pPts[cTotal] = endpt;
	cTotal++;

	return cTotal;
}

UINT CTTFOutline::MakeBezierFromQBSpline( POINT *pPts, POINTFX *pSpline )
{
	POINT P0, // Quadratic on curve start point
		P1, // Quadratic control point
		P2; // Quadratic on curve end point
	UINT cTotal = 0; // Convert the Quadratic points to integer
	P0.x = IntFromFixed( pSpline[0].x );
	P0.y = IntFromFixed( pSpline[0].y );
	P1.x = IntFromFixed( pSpline[1].x );
	P1.y = IntFromFixed( pSpline[1].y );
	P2.x = IntFromFixed( pSpline[2].x );
	P2.y = IntFromFixed( pSpline[2].y ); // conversion of a quadratic to a cubic // Cubic P0 is the on curve start point
	pPts[cTotal] = P0;
	cTotal++;

	// Cubic P1 in terms of Quadratic P0 and P1
	pPts[cTotal].x = P0.x + 2*(P1.x - P0.x)/3;
	pPts[cTotal].y = P0.y + 2*(P1.y - P0.y)/3;
	cTotal++; // Cubic P2 in terms of Qudartic P1 and P2
	pPts[cTotal].x = P1.x + 1*(P2.x - P1.x)/3;
	pPts[cTotal].y = P1.y + 1*(P2.y - P1.y)/3;
	cTotal++; // Cubic P3 is the on curve end point
	pPts[cTotal] = P2;
	cTotal++; 
	return cTotal;
}

UINT CTTFOutline::AppendPolyLineToBezier( LPPOINT pt, POINTFX start, LPTTPOLYCURVE lpCurve )
{
	int i;
	UINT cTotal = 0;
	POINT endpt;
	POINT startpt;
	POINT bezier[4]; 
	endpt.x = IntFromFixed(start.x);
	endpt.y = IntFromFixed(start.y); 
	for (i = 0; i < lpCurve->cpfx; i++)
	{
		// define the line segment
		startpt = endpt;
		endpt.x = IntFromFixed(lpCurve->apfx[i].x);
		endpt.y = IntFromFixed(lpCurve->apfx[i].y); // convert a line to a bezier representation
		MakeBezierFromLine( bezier, startpt, endpt ); // append the Bezier to the existing ones
		// Point 0 is Point 3 of previous.
		pt[cTotal++] = bezier[1]; // Point 1
		pt[cTotal++] = bezier[2]; // Point 2
		pt[cTotal++] = bezier[3]; // Point 3 
	} 
	return cTotal;
}

UINT CTTFOutline::AppendQuadBSplineToBezier( LPPOINT pt, POINTFX start, LPTTPOLYCURVE lpCurve )
{
	WORD i;
	UINT cTotal = 0;
	POINTFX spline[3]; // a Quadratic is defined by 3 points
	POINT bezier[4]; // a Cubic by 4 // The initial A point is on the curve.
	spline[0] = start;
	for (i = 0; i < lpCurve->cpfx;)
	{
		// The B point.
		spline[1] = lpCurve->apfx[i++]; // Calculate the C point.
		if (i == (lpCurve->cpfx - 1))
		{
			// The last C point is described explicitly
			// i.e. it is on the curve.
			spline[2] = lpCurve->apfx[i++];
		} 
		else
		{
			// C is midpoint between B and next B point
			// because that is the on curve point of 
			// a Quadratic B-Spline.
			spline[2].x = fxDiv2(
				lpCurve->apfx[i-1].x,
				lpCurve->apfx[i].x
				);
			spline[2].y = fxDiv2(
				lpCurve->apfx[i-1].y,
				lpCurve->apfx[i].y
				);
		} // convert the Q Spline to a Bezier
		MakeBezierFromQBSpline( bezier, spline );

		// append the Bezier to the existing ones
		// Point 0 is Point 3 of previous.
		pt[cTotal++] = bezier[1]; // Point 1
		pt[cTotal++] = bezier[2]; // Point 2
		pt[cTotal++] = bezier[3]; // Point 3 // New A point for next slice of spline is the 
		// on curve C point of this B-Spline
		spline[0] = spline[2];
	}
	return cTotal;
}

UINT CTTFOutline::CloseContour( LPPOINT pt, UINT cTotal )
{
	POINT endpt, 
		startpt; // definition of a line
	POINT bezier[4]; // connect the first and last points by a line segment
	startpt = pt[cTotal-1];
	endpt = pt[0]; // convert a line to a bezier representation
	MakeBezierFromLine( bezier, startpt, endpt ); // append the Bezier to the existing ones
	// Point 0 is Point 3 of previous.
	pt[cTotal++] = bezier[1]; // Point 1
	pt[cTotal++] = bezier[2]; // Point 2
	pt[cTotal++] = bezier[3]; // Point 3 
	return 3;
}

BOOL CTTFOutline::Create( LPTTPOLYGONHEADER lpHeader, DWORD size ) 
{
	WORD i;
	UINT cTotal = 0; // Total points in a contour.
	LPTTPOLYGONHEADER lpStart; // the start of the buffer
	LPTTPOLYCURVE lpCurve; // the current curve of a contour
	LPPOINT pt; // the bezier buffer
	POINTFX ptStart; // The starting point of a curve
	DWORD dwMaxPts = size/sizeof(POINTFX); // max possible pts.
	DWORD dwBuffSize; 
	dwBuffSize = dwMaxPts * // Maximum possible # of contour points.
		sizeof(POINT) * // sizeof buffer element
		3; // Worst case multiplier of one additional point
	// of line expanding to three points of a bezier 
	lpStart = lpHeader;
	//pt = (LPPOINT)malloc( dwBuffSize ); // Loop until we have processed the entire buffer of contours.
	pt = (LPPOINT)new BYTE[dwBuffSize];
	// The buffer may contain one or more contours that begin with
	// a TTPOLYGONHEADER. We have them all when we the end of the buffer.

	while ((DWORD)lpHeader < (DWORD)(((LPSTR)lpStart) + size) && pt != NULL)
	{
		if (lpHeader->dwType == TT_POLYGON_TYPE)
			// Draw each coutour, currently this is the only valid
			// type of contour.
		{
			// Convert the starting point. It is an on curve point.
			// All other points are continuous from the "last" 
			// point of the contour. Thus the start point the next
			// bezier is always pt[cTotal-1] - the last point of the 
			// previous bezier. See PolyBezier.
			cTotal = 1;
			pt[0].x = IntFromFixed(lpHeader->pfxStart.x);
			pt[0].y = IntFromFixed(lpHeader->pfxStart.y); // Get to first curve of contour - 
			// it starts at the next byte beyond header
			lpCurve = (LPTTPOLYCURVE) (lpHeader + 1); // Walk this contour and process each curve( or line ) segment 
			// and add it to the Beziers
			while ((DWORD)lpCurve < (DWORD)(((LPSTR)lpHeader) + lpHeader->cb))
			{
				//**********************************************
				// Format assumption:
				// The bytes immediately preceding a POLYCURVE
				// structure contain a valid POINTFX.
				// 
				// If this is first curve, this points to the 
				// pfxStart of the POLYGONHEADER.
				// Otherwise, this points to the last point of
				// the previous POLYCURVE.
				// 
				// In either case, this is representative of the
				// previous curve's last point.
				//********************************************** 
				ptStart = *(LPPOINTFX)((LPSTR)lpCurve - sizeof(POINTFX));
				if (lpCurve->wType == TT_PRIM_LINE)
				{
					// convert the line segments to Bezier segments
					cTotal += AppendPolyLineToBezier( &pt[cTotal], ptStart, lpCurve );
					i = lpCurve->cpfx;
				}
				else if (lpCurve->wType == TT_PRIM_QSPLINE)
				{
					// Decode each Quadratic B-Spline segment, convert to bezier,
					// and append to the Bezier segments
					cTotal += AppendQuadBSplineToBezier( &pt[cTotal], ptStart, lpCurve );
					i = lpCurve->cpfx;
				}
				else
					// Oops! A POLYCURVE format we don't understand.
					; // error, error, error // Move on to next curve in the contour.
				lpCurve = (LPTTPOLYCURVE)&(lpCurve->apfx[i]);
			} // Add points to close the contour.

			// All contours are implied closed by TrueType definition.
			// Depending on the specific font and glyph being used, these
			// may not always be needed.
			if ( pt[cTotal-1].x != pt[0].x || pt[cTotal-1].y != pt[0].y )
			{
				cTotal += CloseContour( pt, cTotal );
			} // flip coordinates to get glyph right side up (Windows coordinates)
			// TT native coordiantes are zero originate at lower-left.
			// Windows MM_TEXT are zero originate at upper-left.
			for (i = 0; i < cTotal; i++)
			{
				pt[i].y = 0 - pt[i].y; // Draw the contour
			}
			// RealRender added
			Contour contour;
			for(int i=0; i<(int)cTotal; i++)
			{
				contour.vectorPoint.push_back( pt[i] );
			}
			m_vectorContour.push_back( contour );
		}
		else
			// Bad, bail, must have a bogus buffer.
			break; // error, error, error // Move on to next Contour.
		// Its header starts immediate after this contour
		lpHeader = (LPTTPOLYGONHEADER)(((LPSTR)lpHeader) + lpHeader->cb);
	} 

	delete []pt;
	return TRUE;
}

};