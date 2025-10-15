#pragma once
#include <map>


namespace colorbarctrl
{
	class CColorBarCtrl : public CDialog
	{
		DECLARE_DYNAMIC(CColorBarCtrl)

	private:
	public:

		enum { IDD = IDD_COLOR_BAR };
		typedef std::map<int, COLORREF> Colors;



	private:
		CRect m_rect;
		CRect m_rectColorBar;
		CRect m_rectControlButtonArea;
		Colors m_Colors;
		int m_nMaxX;
		int m_nSilderHeight;
		int m_nSilderWidth;
		int m_nWidth;
		int m_nSelectedId;
		bool m_bIsMouseDown;
		bool m_bSelectedWillBeDelete;
		CPoint m_ptMouseDownPoint;

	public:
		COLORREF lineColor;
		COLORREF darkLineColor;
		COLORREF lightLineColor;
		COLORREF bgColor;
		int unitX;


	private:
		void DrawControlButton(CDC* pDC);
		void DrawColorBar(CDC* pDC);
		void DrawCtrl(CDC* pDC);
		void CheckColor();
		void ResetSize();
		inline COLORREF InterpolationColor(COLORREF begin, COLORREF end, int now, int width);

	public:
		int GetSilderHeight() const { return m_nSilderHeight; }
		void SetSilderHeight(int height) { m_nSilderHeight = height; }
		int GetSilderWidth() const { return m_nSilderWidth; }
		void SetSilderWidth(int width) { m_nSilderWidth = width; }
		int GetMaxX() const { return m_nMaxX; }
		void SetMaxX(int maxX);
		const Colors& GetColors() const { return m_Colors; }
		void SetColors(const Colors &colors);
		virtual ~CColorBarCtrl();

		CColorBarCtrl(CWnd* pParent = NULL);   

	protected:
		virtual void DoDataExchange(CDataExchange* pDX);    

		DECLARE_MESSAGE_MAP()
	public:
		afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
		afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
		afx_msg void OnMouseMove(UINT nFlags, CPoint point);
		afx_msg void OnSize(UINT nType, int cx, int cy);
		afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
		afx_msg void OnPaint();

	};

}
