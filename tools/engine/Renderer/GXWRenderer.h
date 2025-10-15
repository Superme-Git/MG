#pragma once

#undef  WIN32_LEAN_AND_MEAN
#define WIN32_LEAN_AND_MEAN

#include <windows.h>
#include <map>
#include "../gxwindow/gxw.h"
#include "../engine/renderer/renderer.h"
#include "../engine/particlesystem/particlemanager.h"

namespace GXWRENDER {

class CGXWRenderer : public GXWINDOW::CDraw, public GXWINDOW::CTextDrawAbstractImplement
{
	Nuclear::Renderer*								m_renderer;
	Nuclear::IFontManager*							m_fontmgr;
	std::map<int, Nuclear::PictureHandle>			m_picmap;
	Nuclear::PictureHandle							m_picdefault;	// ´íÎóÄ¬ÈÏÍ¼
	std::map<int, Nuclear::ParticleSystemHandle>	m_pshmap;

public:
	CGXWRenderer(Nuclear::Renderer* renderer);
	virtual ~CGXWRenderer();
	virtual void Line(int sx, int sy, int ex, int ey, GXWINDOW::COLOR color);
	virtual void Rect(int l, int t, int r, int b, GXWINDOW::COLOR color);
	virtual void FillRect(int l, int t, int r, int b, GXWINDOW::COLOR color);
	virtual void DrawPicture(const GXWINDOW::ImageParams& ip, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh);
	virtual void DrawPicture(const GXWINDOW::ImageParams& ip, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh, bool bxMirro, bool byMirro = false);

	virtual int NewTextDraw(const wchar_t* text, int fontid, GXWINDOW::COLOR colorr, GXWINDOW::COLOR clrGradual, GXWINDOW::COLOR bkcolor, GXWINDOW::FONTEFEECTFLAG effect = GXWINDOW::FET_SHADOW, GXWINDOW::COLOR effcolor = 0xc0000000);
	virtual const wchar_t* GetText(int n);
	virtual void DeleteTextDraw(int n);
	virtual void Draw(int n, int l, int t, int cx, int cy, int cw, int ch, GXWINDOW::COLOR color, GXWINDOW::COLOR clrGradual);
	virtual int GetCharPixelWidth(const wchar_t& wc, int fontid);
	virtual int GetStringPixelWidth(const wchar_t* ws, int fontid);
	virtual int GetStringPixelWidth(int n, int fontid);
	virtual int GetFontHeight(int fontid);
	virtual int GetFontHeightWithoutUnderline( int fontid);

	virtual int NewParticleEffect(const wchar_t* name);
	virtual void DrawParticleEffect(int n);
	virtual void DeleteParticleEffect(int n);
};

}
