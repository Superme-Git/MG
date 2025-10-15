#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { class CStateImageMap; class CStateTileImageMap; }

class GXWINDOW_EXPORTS_DLL CImageable : public CUncopyableObject
{
public:
	enum STATE_FLAG {	
		NORMAL =			0x00000001, 
		MOUSEON =			0x00000002,
		MOUSEDOWN =			0x00000004,
		DISABLE =			0x00000008,
		SELECTED_NORMAL =	0x00000010,
		SELECTED_MOUSEON =	0x00000020,
		USER_CUSTOM1 =		0x00000040,
		USER_CUSTOM2 =		0x00000080,
		USER_CUSTOM3 =		0x00000100,
		USER_CUSTOM4 =		0x00000200,
	};
public:
	CImageable();
	CImageable( unsigned int flag);
	virtual ~CImageable();
protected:
	INLINECLASS::CStateImageMap*		m_pStateImages;
public:
	virtual const ImageParams* GetImage( unsigned int flag) const;
	virtual bool IsImageXMirro( unsigned int flag) const;
	virtual bool IsImageYMirro( unsigned int flag) const;
	virtual void SetImage( const ImageParams& ip, unsigned int flag, bool bXMirro = false, bool bYMirro = false);
	virtual void RemoveImage( unsigned int flag);
	virtual void Clear();
	virtual bool IsValid() const;

	// 绘制图片前, 调用该方法将图片纹理id添加到管理器中, 一般在控件的OnComponentShow()方法中对各图片资源对象进行调用
	virtual void RegisterImage( unsigned int flag);
	virtual void RegisterImages();

	virtual void FreeImage( unsigned int flag); // 释放指定图片纹理, 但不破坏纹理id
	virtual void FreeImages();					// 释放所有图片纹理, 但不破坏纹理id
public:
	virtual bool DrawImage( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, unsigned int flag) const;
private:
	void RegisterImageByBitmapID( int nBitmap);
	void FreeImageByBitmapID( int nBitmap);
};


enum TILEPOSITION {	IFP_TOPLEFT = 0, IFP_TOP, IFP_TOPRIGHT,
	IFP_LEFT, IFP_CENTER, IFP_RIGHT,
	IFP_BOTTOMLEFT, IFP_BOTTOM, IFP_BOTTOMRIGHT};

class GXWINDOW_EXPORTS_DLL CImagesFill : public CUncopyableObject
{
	// BACKGROUND_TILES_DISTRIBUTION
	/*
	nw0  nw1 nw2
	+---+---+---+
	| 0 | 1 | 2 | nh0
	+---+---+---+
	| 3 | 4 | 5 | nh1
	+---+---+---+
	| 6 | 7 | 8 | nh2
	+---+---+---+
	*/
	CImageable		m_tiles;			// 九宫格图片
	int				m_nGridW[3];		// 要填充的区域宽度, nw0,nw2与填充图同宽
	int				m_nGridH[3];		// 要填充的区域高度, nh0,nh2与填充图同高
	int				m_nTileW;			// 水平平铺时的填充图宽度
	int				m_nTileH;			// 垂直平铺时的填充图高度
	bool			m_bVTile;			// 为true则垂直方向采用平铺方式填充, 为false则垂直方向采用拉伸方式填充
	bool			m_bHTile;			// 为true则水平方向采用平铺方式填充, 为false则水平方向采用拉伸方式填充
public:
	CImagesFill();
	virtual ~CImagesFill();
public:
	virtual void SetTileImage( const ImageParams& ip, TILEPOSITION position, bool bXMirro = false, bool bYMirro = false);
	virtual const ImageParams& GetTileImage( TILEPOSITION position) const;

	virtual void SetTileSize( int w, int h);
	virtual void SetGridSize( int w, int h, TILEPOSITION position);		// 只设置[0][4][8]区块便可
	virtual int	 GetGridWidth( TILEPOSITION position) const;
	virtual int  GetGridHeight( TILEPOSITION position) const;
	virtual int  GetTileWidth() const;
	virtual int  GetTileHeight() const;
	virtual bool IsValid() const;

	// 设置水平、垂直两个方向的填充方式: 平铺或拉伸
	virtual void SetFillMode( bool bVTile = true, bool bHTile = true);
	virtual bool IsVerticalTile() const;
	virtual bool IsHorizontalTile() const;
	virtual bool GetVerticalFillMode() const;
	virtual bool GetHorizontalFillMode() const;

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	// 从一个输入对象中复制九宫格填充数据
	virtual void CopyFrom( const CImagesFill& other);

	// 绘制图片前, 调用该方法将图片纹理id添加到管理器中, 一般在控件的OnComponentShow()方法中对各图片资源对象进行调用
	virtual void RegisterImage( TILEPOSITION position);
	virtual void RegisterImages();

	// 释放m_tiles里的纹理资源, 但不破坏纹理id
	virtual void FreeImage( TILEPOSITION position);
	virtual void FreeImages();
private:
	inline bool FillTiles( CDraw& draw, const ImageParams& ip, int l, int t, int cw, int ch, int cx, int cy, int TileW, int TileH, bool bHTile, bool bVTile, bool bXMirro, bool bYMirro);
};

class GXWINDOW_EXPORTS_DLL CTileImageAble : public CUncopyableObject
{
public:
	enum STATE_FLAG {	
		NORMAL =			0x00000001, 
		MOUSEON =			0x00000002,
		MOUSEDOWN =			0x00000004,
		DISABLE =			0x00000008,
		SELECTED_NORMAL =	0x00000010,
		SELECTED_MOUSEON =	0x00000020,
		USER_CUSTOM1 =		0x00000040,
		USER_CUSTOM2 =		0x00000080,
		USER_CUSTOM3 =		0x00000100,
		USER_CUSTOM4 =		0x00000200,
	};
public:
	CTileImageAble();
	virtual ~CTileImageAble();
protected:
	INLINECLASS::CStateTileImageMap*		m_pStateTileImages;
public:
	virtual bool IsContainImage( unsigned int flag) const;
	virtual CImagesFill& GetTileImage( unsigned int flag);
	virtual void RemoveImage( unsigned int flag);
	virtual void Clear();

	virtual void RegisterImage( unsigned int flag);
	virtual void RegisterImages();

	virtual void FreeImage( unsigned int flag);
	virtual void FreeImages();
public:
	virtual bool DrawImage( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, unsigned int flag) const;
};


} // namespace GXWINDOW { 
