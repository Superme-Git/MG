#ifndef __Nuclear_IFONTMANAGER_H
#define __Nuclear_IFONTMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{

	// TextBlock接口
	class ITextBlock
	{
	public:
		ITextBlock() {}
		virtual ~ITextBlock() {}

		virtual bool SetText( LPCTSTR text) = 0; // 如超出分配纹理的长度，则截断文字
		virtual LPCTSTR GetText() = 0;
		virtual int	 GetFontType() = 0;
		virtual DWORD GetFontColor() = 0;
		virtual DWORD GetGradualColor() = 0;
		virtual DWORD GetBkColor() = 0;

		virtual bool SetFontColor( DWORD fontColor) = 0;
		virtual bool SetFontColor( DWORD fontColor, DWORD gradualColor) = 0;
		virtual bool SetBkColor( DWORD bkColor) = 0;

		virtual int	GetTextWidth() = 0;
		virtual int GetTextHeight() = 0;

		virtual void CancelFontEffect() = 0;

		virtual void SetShadowColor( DWORD shadowColor) = 0;
		virtual DWORD GetShadowColor() = 0;

		virtual void SetStrokeColor( DWORD strokeColor) = 0;
		virtual DWORD GetStrokeColor() = 0;

		virtual bool SetUnderline( bool bUnderline) = 0;
		virtual bool IsUnderline() = 0;

		virtual void Render( Renderer& renderer, float x, float y, const FRECT* src) = 0;
		virtual void Render( Renderer* renderer, const FPOINT& textPos, const FPOINT& fScale, const FRECT* src) = 0;
	};

	class IFontManager
	{

	public:
		IFontManager() { }
		virtual ~IFontManager() { }

		// 接口
		virtual bool AddFontType( int fontType, LPCTSTR fontFile, int fontSize) = 0;
		virtual void Release() = 0;

		virtual TextBlockHandle CreateTextBlock( int fontType, LPCTSTR szText) = 0;
		virtual TextBlockHandle CreateTextBlock( int fontType, int maxTextWidth) = 0;
		virtual ITextBlock* GetTextBlock( TextBlockHandle htb) = 0;
		virtual void ReleaseTextBlock( TextBlockHandle tb) = 0;

		virtual int GetCharWidth( const TCHAR ch, int fontType) = 0;
		virtual int GetStringWidth( LPCTSTR szText, int fontType) = 0;
		virtual int GetFontTypeHeight( int fontType) = 0;
		virtual int GetFontTypeUnderlinePosition( int fontType) = 0;
		virtual int GetFontTypeUnderlineThickness( int fontType) = 0;


		virtual TextBlockHandle NewText( LPCTSTR text, int fonttype, DWORD color, DWORD gradualcolor, DWORD bkColor = 0x00000000, bool bUnderline = false) = 0;
		virtual bool DrawText( TextBlockHandle htb, float x1, float y1, const FRECT* srcrect) = 0;

		// 占坑
	private:
		IFontManager(const IFontManager&);
		IFontManager& operator=(const IFontManager&);
	};



};

#endif