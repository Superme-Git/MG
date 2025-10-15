#ifndef __Nuclear_distort_H__
#define __Nuclear_distort_H__
#include "../common/nurecttopologylist.h"
#include "../map/nupimg.h"
#include "nudistortbase.h"

namespace Nuclear
{
	class Distort : public DistortBase
	{
	private:
		NuclearPoint basePoint[2];
	public:
		int lastUpdateTick;
		int lastDrawTick;
		NuclearSortBaseRectType base;
		
	public:
		Distort(EngineBase* pEB, const DistortionEffectParam& effectParam, const NuclearImg& img) : 
		  DistortBase(pEB, effectParam), lastUpdateTick(0), lastDrawTick(0) 
		 {
			 basePoint[0] = img.pos[enum_left];
			 basePoint[1] = img.pos[enum_right];
		 }
		virtual ~Distort(void) {}

		virtual void setPos(const NuclearRect &rct)
		{
			NuclearPoint tmpCPoint;
			TransToDiamondRadix(basePoint[0].x + rct.left, basePoint[0].y + rct.top, tmpCPoint);
			base.left = tmpCPoint.x;
			base.bottom = tmpCPoint.y;
			TransToDiamondRadix(basePoint[1].x + rct.left, basePoint[1].y + rct.top, tmpCPoint);
			base.right = tmpCPoint.x;
			base.top = tmpCPoint.y;
			base.right = tmpCPoint.x;
			base.top = tmpCPoint.y;
			base.srcRect = rct.Widen(50);
			base.y = (basePoint[0].y + basePoint[1].y) / 2 + rct.top;
			DistortBase::setPos(rct);
		}
		const NuclearRect& GetRect() const { return m_rct; }
	};
}

#endif