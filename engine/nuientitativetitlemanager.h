#ifndef __Nuclear_IENTITATIVE_TITLE_MANAGER_H__
#define __Nuclear_IENTITATIVE_TITLE_MANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "common/nuxptypes.h"


namespace Nuclear
{
	class IEntitativeTitleManager
	{

	public:
		IEntitativeTitleManager(void) {}
		virtual ~IEntitativeTitleManager(void) {}

		//在绘制实体（Sprite、Immovableobj）的时候会调用这个方法。
		//逻辑那边可以在调用这个的时候就马上绘制，这个时候就在中间层，
		//也可以在这里仅仅做收集，之后在绘制完引擎，但是在所有UI之前绘制
		//pos为屏幕像素坐标
		virtual void RenderTitle(EntitativeTitleHandle title, const NuclearPoint &pos) = 0;
		
		//这个NuclearLocation是世界像素坐标，在拾取检测的时候，当发现检测点落在了Title的范围内的时候，
		//就会调用这个方法进一步检测。当然，不是必须精确到像素
		virtual bool TestTitleAlpha(EntitativeTitleHandle title, const NuclearLocation &testLoc) = 0;
	};
}

#endif