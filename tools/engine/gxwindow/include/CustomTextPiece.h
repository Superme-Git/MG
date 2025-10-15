#pragma once
namespace GXWINDOW { 
class GXWINDOW_EXPORTS_DLL CCustomTextPieceAbstractImpl
{
public:
	CCustomTextPieceAbstractImpl() {}
	virtual ~CCustomTextPieceAbstractImpl() {}
public:
	virtual void Draw(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch) = 0;
	virtual void Release() = 0;
};
class GXWINDOW_EXPORTS_DLL CCustomTextPiece : public CComponent
{

	INLINECLASS::CSimpleString*		m_textNotify;
	bool							m_bNotTransparent;
public:
	CCustomTextPiece();
	virtual  ~CCustomTextPiece();


public:
	virtual void SetNotifyString( const wchar_t* szText);
	virtual const wchar_t* GetNotifyString() const;

	virtual bool OnLeftButtonDown( int x, int y, int key);

	virtual void SetTransparent( bool b);
	virtual bool IsTransparent() const;
public:
	void SetAbstractImpl(CCustomTextPieceAbstractImpl* impl);
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
private:
	CCustomTextPieceAbstractImpl* m_impl;
};
}
