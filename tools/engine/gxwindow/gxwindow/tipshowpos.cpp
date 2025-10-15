#include "gxwindowdef.h"

namespace GXWINDOW {

CTipShowPosLayout::CTipShowPosLayout()
{
}

CTipShowPosLayout::~CTipShowPosLayout()
{
}

void INLINECLASS::CDefaultTipShowLayout::LayOutTipPos( CComponent& base, CFrame& tip, int w, int h, int mx, int my, int& x, int& y)
{
	const int nMinIcon = 64;
	int basew = base.GetWidth();
	int baseh = base.GetHeight();

	CDesktop& desktop = CDesktop::GetDefault();
 	const CPanel& desktoppanel = desktop.GetDesktopPanel();

	if( basew < nMinIcon && baseh < nMinIcon)
	{
		{
			int basel = base.GetAbsPosX();
			int baser = basel + basew;
			int tipwidth = tip.GetWidth();
			if( (baser + tipwidth) > w) //不能放在右边
				x = basel - tipwidth;
			else						// 可以放在右边
				x = baser;
		}

		{
			int baset = base.GetAbsPosY();
			int baseb = baset + baseh;
			int tipheight = tip.GetHeight();
			y = baseb - tipheight;
			if((my + tipheight + 6) > desktoppanel.GetHeight())
				y = desktoppanel.GetHeight() - tipheight - 6;
			else
				y = my;
			//if( y < 6)
			//	y = 6;
			//else if( (y + tipheight) > h)
			//	y = h - tipheight;
			
		}
	}
	else
	{
		{
			int tipw = tip.GetWidth();
			if( (mx + tipw) > w)
				x = mx - tipw;
			else
				x = mx;
			if( x < 0)
				x = w - tipw;
		}
		{
			int tiph = tip.GetHeight();
			y = my - tiph;
			if( y < 0)
				y = 0;

			if((my + tiph + 6) > desktoppanel.GetHeight())
				y = desktoppanel.GetHeight() - tiph - 6;
			else
				y = my;
		}
	}
}

} // namespace GXWINDOW {