#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { class CTextDrawItemMultiIndex; class CTextDrawItems; };

typedef unsigned int FONTEFEECTFLAG;
enum FONTEFFECT_TYPE
{
	FET_NONE = 0,
	FET_UNDERLINE = 1, // 下划线
	FET_SHADOW = 2, // 阴影
	FET_STROKE = 4, // 描边
};


class GXWINDOW_EXPORTS_DLL CTextDrawAbstractImplement : public CUncopyableObject
{
public:
	CTextDrawAbstractImplement();
	virtual ~CTextDrawAbstractImplement();
protected:
	static void Register( CTextDrawAbstractImplement& tdai);
public:
	static CTextDrawAbstractImplement* GetPriorImplement();
public:
	virtual int GetPriority();
	virtual int NewTextDraw( const wchar_t* szText, int nFont, COLOR clr, COLOR clrGradual, COLOR bk, FONTEFEECTFLAG effect = FET_NONE, COLOR clrEffect = 0) = 0;
	virtual const wchar_t* GetText( int n) = 0;
	virtual void DeleteTextDraw( int n) = 0;
	virtual void Draw( int n, int l, int t, int cx, int cy, int cw, int ch, COLOR clr = MakeColor( 0, 0, 0), COLOR clrGradual = MakeColor( 0, 0, 0)) = 0; 
	virtual int GetCharPixelWidth( const wchar_t& wt , int font) = 0;
	virtual int GetStringPixelWidth( const wchar_t* sz, int nFont) = 0;
	virtual int GetStringPixelWidth( int n, int nFont) = 0;
	virtual int GetFontHeight( int nFont) = 0; 
	virtual int GetFontHeightWithoutUnderline( int nFont) = 0;

	virtual void SetUnderline( int n, bool bUnderline) {};
	virtual void SetShadow( int n, COLOR clr) {};
	virtual void SetStroke( int n, COLOR clr) {};
};

class GXWINDOW_EXPORTS_DLL CTextDrawManager : public CUncopyableObject
{
	INLINECLASS::CTextDrawItemMultiIndex*	m_tdimi;
	INLINECLASS::CTextDrawItems*			m_tdis;

	CTextDrawAbstractImplement*		m_Implement;
	int			m_nid;

	static CTextDrawManager*		m_pTextDrawManager;
public:
	virtual ~CTextDrawManager();
public:
	static CTextDrawManager& GetInstance();
	static void ReleaseInstance();
private:
	int GenerateTextDrawIndex();
public:
	// clrGradual为渐变色
	int NewTextDraw( const wchar_t* szText, int nFont, COLOR clr, COLOR clrGradual, COLOR bk = 0, FONTEFEECTFLAG effect = FET_NONE, COLOR clrEffect = 0);
	int AddRef( int n);
	int FreeTextDraw( int n);
	void Draw( int n, int l, int t, int cx, int cy, int cw, int ch);

	int GetCharPixelWidth( const wchar_t& wt , int font);
	int GetStringPixelWidth( const wchar_t* sz, int nFont);
	int GetStringPixelWidth( int n, int nFont);
	int GetFontHeight( int nFont); 
	int GetFontHeightWithoutUnderline( int nFont);

	void SetShadow( int n, COLOR clr);
	void SetStroke( int n, COLOR clr);

	void FreeAll();
private:
	CTextDrawManager();
};

} // namespace GXWINDOW { 