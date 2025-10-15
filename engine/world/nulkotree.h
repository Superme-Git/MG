#ifndef __Nuclear_immovableobj_H__
#define __Nuclear_immovableobj_H__

#include "../common/nurecttopologylist.h"
#include "../engine/nurenderable.h"
#include "nulkotreenodelist.h"

namespace Nuclear
{
	namespace Region
	{
		struct LkoEle;
	};

	class LkoTree : public LkoTreeNodeList, public Renderable
	{
	public:
		typedef std::vector<Region::LkoEle*> DEList;

		NuclearSortBaseRectType base;
		NuclearRect m_rect;
		int lastUpdateTick;
		int lastDrawTick;

	protected:
		bool CheckNeedDraw(const NuclearFRectt &rect, const NuclearMatrix3 &m, const NuclearRect& viewport);

		void AddANode(LkoTreeNode *node);
		bool render(Canvas* canvas, bool isAlpha, bool realRender);

	public:
		LkoTree(EngineBase* pEB) : Renderable(pEB), lastUpdateTick(0), lastDrawTick(0) { m_color = XPCOLOR_FULL; };
		virtual ~LkoTree(void);

		const DEList& GetLkoEleList() const { return m_drawList; }
		void SetColor(NuclearColor color) { m_color = color; }
		void AddANode(Region::LkoEle *node, bool bIsRoot);
		void AddEnded() { m_updateListset.clear(); }
		virtual bool RenderBottom(Canvas* canvas) { return true; }
		virtual bool RenderMid(Canvas* canvas, bool realRender) { return render(canvas, true, realRender); }
		virtual void RenderBeforeMid(bool realRender) { render(NULL, false, realRender); }
		virtual bool Render(Canvas* canvas, bool realRender) { return render(canvas, false, realRender); }

	protected:
		NuclearColor m_color;
		DEList m_drawList;
		std::set<LkoTreeNode*> m_updateListset;
	};

}

#endif
