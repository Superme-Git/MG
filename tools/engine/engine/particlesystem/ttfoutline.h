#pragma once
namespace Nuclear
{

typedef struct CONTOUR
{
	std::vector<POINT> vectorPoint;
} Contour;

class CTTFOutline
{
public:
	CTTFOutline(void);
	~CTTFOutline(void);

public:
	BOOL Create( LPTTPOLYGONHEADER lpHeader, DWORD size );
//	void Destroy(){ m_vectorContour.clear(); }

	int GetContourCount() { return (int)m_vectorContour.size(); }
	Contour* GetContour( int nContourId ) { return &m_vectorContour[nContourId]; }
	std::vector<Contour> GetVectorContour() { return m_vectorContour; }

protected:
	std::vector<Contour> m_vectorContour;


private:
	int IntFromFixed(FIXED f);
	FIXED fxDiv2(FIXED fxVal1, FIXED fxVal2);
	UINT MakeBezierFromLine( POINT *pPts, POINT startpt, POINT endpt );
	UINT MakeBezierFromQBSpline( POINT *pPts, POINTFX *pSpline );
	UINT AppendPolyLineToBezier( LPPOINT pt, POINTFX start, LPTTPOLYCURVE lpCurve );
	UINT AppendQuadBSplineToBezier( LPPOINT pt, POINTFX start, LPTTPOLYCURVE lpCurve );
	UINT CloseContour( LPPOINT pt, UINT cTotal );
};


};