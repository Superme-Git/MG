#pragma once

namespace GXWINDOW { 

namespace INLINECLASS{ class CFileNameImagesParam; class CImageRefCountMap; }

typedef unsigned int COLOR;

inline COLOR MakeColor( int r, int g, int b, int a = 255)
{
	return (((a) & 0xff) << 24) | (((r) & 0xff) << 16) | (((g) & 0xff) << 8) | (((b) & 0xff));
}

inline int GetColorRed( COLOR clr)
{
	return (0x00ff0000 & clr) >> 16;
}

inline int GetColorGreen( COLOR clr)
{
	return (0x0000ff00 & clr) >> 8;
}

inline int GetColorBlue( COLOR clr)
{
	return (0x000000ff & clr);
}

inline int GetColorAlpha( COLOR clr)
{
	return (0xff000000 & clr) >> 24;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CFileImages : public CUncopyableObject
{
public:
	CFileImages();
	virtual ~CFileImages();
public:
	virtual const wchar_t* QueryFileName( int n) = 0;
};

class GXWINDOW_EXPORTS_DLL CFileNameImages : public CFileImages 
{
	INLINECLASS::CFileNameImagesParam*		m_fnip;
	int				m_nID;
public:
	CFileNameImages();
	virtual ~CFileNameImages();
public:
	virtual const wchar_t* QueryFileName( int n);
private:
	int GetNewID();
public:
	virtual int RegisterFileName( const wchar_t* szName);
public:
	static CFileNameImages& GetInstance();
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////

struct GXWINDOW_EXPORTS_DLL ImageParams
{
	int			nBitmap;
	int			x;
	int			y;
	COLOR		color;
};

class GXWINDOW_EXPORTS_DLL CImageParams : public ImageParams
{
public:
	CImageParams();
	CImageParams( int bmp, int x, int y, COLOR cl = MakeColor( 255, 255, 255));
	virtual ~CImageParams();
};

class GXWINDOW_EXPORTS_DLL CImageTextureManager : public CUncopyableObject
{
	INLINECLASS::CImageRefCountMap* m_pircm;
public:
	CImageTextureManager();
	virtual ~CImageTextureManager();

	virtual void InsertImage( int nBitmap);
	virtual void RemoveImage( int nBitmap);
public:
	static CImageTextureManager& GetInstance();
};

class GXWINDOW_EXPORTS_DLL CDraw : public CUncopyableObject
{
public:
	CDraw();
	virtual ~CDraw();
public:
	virtual void Line( int sx, int sy, int ex, int ey, COLOR clr) = 0;
	virtual void Rect( int l, int t, int r, int b, COLOR clr) = 0;
	virtual void FillRect( int l, int t, int r, int b, COLOR clr) = 0;
	//virtual void DrawPicture( const ImageParams& ip, int dx, int dy, int dw, int dh, int sx, int sy, float fxScale = 1.0f, float fyScale = 1.0f) = 0;
	//virtual void DrawPicture( const ImageParams& ip, int dx, int dy, int dw, int dh, int sx, int xy, float fxScale, float fyScale, bool bxMirro, bool byMirro) = 0;
	virtual void DrawPicture( const ImageParams& ip, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh) = 0;
	virtual void DrawPicture( const ImageParams& ip, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh, bool bxMirro, bool byMirro = false) = 0;
	virtual void FreePicture( int nBitmap) {};

#ifdef GXWINDOW_ENABLE_BATCH_RENDER
	virtual void BatchDraw() = 0; // 该接口中实现批量渲染, 具体实现方式得结合引擎考虑
	virtual bool IsNeedRedraw() const;
#endif
};



} // namespace GXWINDOW { 