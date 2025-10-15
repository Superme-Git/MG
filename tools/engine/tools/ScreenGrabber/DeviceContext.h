#pragma once

class CDeviceContext
{
public:
	CDeviceContext(void);
	~CDeviceContext(void);

	CDeviceContext(HDC hDC);

	BOOL CreateCompatibleDC(HDC hDC);

	BOOL CreateCompatibleDC(CDeviceContext* pDC);

	// 关联逻辑设备句柄
	BOOL Attach(HDC hDC);

	// 解除关联的逻辑设备
	HDC Detach(void);

	// 删除逻辑设备
	BOOL DeleteDC();

public:
	// 关联的逻辑设备上下文句柄
	HDC m_hDC;
	operator HDC() const;
	HDC GetSafeHdc() const; // Always returns the Output DC
	HWND GetWindow() const;

public:
	int SaveDC();

	BOOL RestoreDC(int nSavedDC);

	// 将位图对象选进绘制及显示设备
	HBITMAP SelectBitmap(HBITMAP hBitmap);

	HGDIOBJ SelectStockObject(int nIndex);

	COLORREF SetBkColor(COLORREF crColor);

	int SetBkMode(int nBkMode);
	
	COLORREF SetTextColor(COLORREF crColor);

	int GetClipBox(LPRECT lpRect) const;

	int GetStretchBltMode() const;
	COLORREF GetTextColor() const;

	BOOL BitBlt(int x, int y, int nWidth, int nHeight, HDC hSrcDC,
		int xSrc, int ySrc, DWORD dwRop);

	BOOL BitBlt(int x, int y, int nWidth, int nHeight, CDeviceContext* pSrcDC,
		int xSrc, int ySrc, DWORD dwRop);

	BOOL StretchBlt(int x, int y, int nWidth, int nHeight, HDC hSrcDC,
		int xSrc, int ySrc, int nSrcWidth, int nSrcHeight, DWORD dwRop);

	BOOL StretchBlt(int x, int y, int nWidth, int nHeight, CDeviceContext* pSrcDC,
		int xSrc, int ySrc, int nSrcWidth, int nSrcHeight, DWORD dwRop);

	BOOL Rectangle(int left, int top, int right, int bottom);

	BOOL MoveTo( int x, int y );
	BOOL LineTo( int x, int y);
};

class CClientDeviceContext :
	public CDeviceContext
{
public:
	CClientDeviceContext(void);
	~CClientDeviceContext(void);

	// Attributes
protected:
	HWND m_hWnd;
};

class CWindowDeviceContext :
	public CDeviceContext
{
public:
	CWindowDeviceContext(void);
	~CWindowDeviceContext(void);

	// Attributes
protected:
	HWND m_hWnd;

};

class CScreenDeviceContext :
	public CDeviceContext
{
public:
	CScreenDeviceContext(void);
	~CScreenDeviceContext(void);

	BOOL CreateDC();

	HBITMAP CaptureToBitmap( );

	BOOL CaptureToDC( HDC hDC );
};

// CDeviceContext inline functions

inline CDeviceContext::operator HDC() const
{ return this == NULL ? NULL : m_hDC; }

inline HDC CDeviceContext::GetSafeHdc() const
{ return this == NULL ? NULL : m_hDC; }

inline HWND CDeviceContext::GetWindow() const
{ _ASSERT(m_hDC != NULL); return ::WindowFromDC(m_hDC); }

inline int CDeviceContext::GetStretchBltMode() const
{ _ASSERT(m_hDC != NULL); return ::GetStretchBltMode(m_hDC); }

inline COLORREF CDeviceContext::GetTextColor() const
{ _ASSERT(m_hDC != NULL); return ::GetTextColor(m_hDC); }

inline BOOL CDeviceContext::CreateCompatibleDC(HDC hDC)
{ return Attach(::CreateCompatibleDC(hDC)); }

inline BOOL CDeviceContext::CreateCompatibleDC(CDeviceContext* pDC)
{ return Attach(::CreateCompatibleDC(pDC->GetSafeHdc())); }


