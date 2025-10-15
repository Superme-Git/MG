#pragma once
#include "..\common\recttopologylist.h"
#include "..\map\pimg.h"
#include "distortbase.h"

namespace Nuclear
{
	class Distort :
		public DistortBase
	{
	private:
		CPOINT basePoint[2];
	public:
		int lastUpdateTick;
		int lastDrawTick;
		SortBaseRectType base;
		
	public:
		Distort(EngineBase* pEB, const DistortionEffectParam& effectParam, const PImg& img) : 
		  DistortBase(pEB, effectParam), lastUpdateTick(0), lastDrawTick(0) 
		 {
			 basePoint[0] = img.pos[enum_left];
			 basePoint[1] = img.pos[enum_right];
		 }
		virtual ~Distort(void) {}

		virtual void setPos(const CRECT &rct)
		{
			CPOINT tmpCPoint;
			TransToDiamondRadix(basePoint[0].x + rct.left, basePoint[0].y + rct.top, tmpCPoint);
			base.left = tmpCPoint.x;
			base.bottom = tmpCPoint.y;
			TransToDiamondRadix(basePoint[1].x + rct.left, basePoint[1].y + rct.top, tmpCPoint);
			base.right = tmpCPoint.x;
			base.top = tmpCPoint.y;
			DistortBase::setPos(rct);
		}
		const CRECT& GetRect() const { return m_rct; }
	};

}
