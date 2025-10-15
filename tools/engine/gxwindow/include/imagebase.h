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

	// ����ͼƬǰ, ���ø÷�����ͼƬ����id��ӵ���������, һ���ڿؼ���OnComponentShow()�����жԸ�ͼƬ��Դ������е���
	virtual void RegisterImage( unsigned int flag);
	virtual void RegisterImages();

	virtual void FreeImage( unsigned int flag); // �ͷ�ָ��ͼƬ����, �����ƻ�����id
	virtual void FreeImages();					// �ͷ�����ͼƬ����, �����ƻ�����id
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
	CImageable		m_tiles;			// �Ź���ͼƬ
	int				m_nGridW[3];		// Ҫ����������, nw0,nw2�����ͼͬ��
	int				m_nGridH[3];		// Ҫ��������߶�, nh0,nh2�����ͼͬ��
	int				m_nTileW;			// ˮƽƽ��ʱ�����ͼ���
	int				m_nTileH;			// ��ֱƽ��ʱ�����ͼ�߶�
	bool			m_bVTile;			// Ϊtrue��ֱ�������ƽ�̷�ʽ���, Ϊfalse��ֱ����������췽ʽ���
	bool			m_bHTile;			// Ϊtrue��ˮƽ�������ƽ�̷�ʽ���, Ϊfalse��ˮƽ����������췽ʽ���
public:
	CImagesFill();
	virtual ~CImagesFill();
public:
	virtual void SetTileImage( const ImageParams& ip, TILEPOSITION position, bool bXMirro = false, bool bYMirro = false);
	virtual const ImageParams& GetTileImage( TILEPOSITION position) const;

	virtual void SetTileSize( int w, int h);
	virtual void SetGridSize( int w, int h, TILEPOSITION position);		// ֻ����[0][4][8]������
	virtual int	 GetGridWidth( TILEPOSITION position) const;
	virtual int  GetGridHeight( TILEPOSITION position) const;
	virtual int  GetTileWidth() const;
	virtual int  GetTileHeight() const;
	virtual bool IsValid() const;

	// ����ˮƽ����ֱ�����������䷽ʽ: ƽ�̻�����
	virtual void SetFillMode( bool bVTile = true, bool bHTile = true);
	virtual bool IsVerticalTile() const;
	virtual bool IsHorizontalTile() const;
	virtual bool GetVerticalFillMode() const;
	virtual bool GetHorizontalFillMode() const;

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	// ��һ����������и��ƾŹ����������
	virtual void CopyFrom( const CImagesFill& other);

	// ����ͼƬǰ, ���ø÷�����ͼƬ����id��ӵ���������, һ���ڿؼ���OnComponentShow()�����жԸ�ͼƬ��Դ������е���
	virtual void RegisterImage( TILEPOSITION position);
	virtual void RegisterImages();

	// �ͷ�m_tiles���������Դ, �����ƻ�����id
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
