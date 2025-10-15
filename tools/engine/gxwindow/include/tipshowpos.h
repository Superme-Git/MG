#pragma once

namespace GXWINDOW {

class GXWINDOW_EXPORTS_DLL CTipShowPosLayout : public CUncopyableObject
{
	friend class CDesktop;
public:
	CTipShowPosLayout();
	virtual ~CTipShowPosLayout();
protected:
	virtual void LayOutTipPos( CComponent& base, CFrame& tip, int w, int h, int mx, int my, int& x, int& y) = 0;
};

} // namespace GXWINDOW {