#pragma once

namespace GXWINDOW {  
namespace INLINECLASS {

class CDefaultTipShowLayout : public CTipShowPosLayout
{
public:
	CDefaultTipShowLayout() {}
	virtual ~CDefaultTipShowLayout() {}
public:
	virtual void LayOutTipPos( CComponent& base, CFrame& tip, int w, int h, int mx, int my, int& x, int& y);
};

#ifdef _GXW_UPDATE_CHECK
#define _GXW_UPDATE_CHECK_ASSERT do { CDesktop::GetDefault().UPDATE_CHECK_ASSERT(); } while( 0)
#else
#define _GXW_UPDATE_CHECK_ASSERT
#endif

} // namespace INLINECLASS {
} // namespace GXWINDOW { 