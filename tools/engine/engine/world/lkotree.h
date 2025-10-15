#pragma once

#include "..\common\recttopologylist.h"
#include "..\engine\renderable.h"
#include "lkotreenodelist.h"

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
	protected:
		XPCOLOR m_color;
		DEList m_drawList;
		std::set<LkoTreeNode*> m_updateListset;
	public:
		SortBaseRectType base;
		CRECT m_rect;
		int lastUpdateTick;
		int lastDrawTick;
	protected:
		//这个CheckNeedDraw，以后可以考虑提为公共方法扔到xpmaths里面
		bool CheckNeedDraw(const FRECT &rect, const XPMATRIX3 &m, const CRECT& viewport);

		void AddANode(LkoTreeNode *node);
	public:
		LkoTree(EngineBase* pEB) : Renderable(pEB), lastUpdateTick(0), lastDrawTick(0) { m_color = XPCOLOR_FULL; };
		virtual ~LkoTree(void);

		const DEList& GetLkoEleList() const { return m_drawList; }
		void SetColor(XPCOLOR color) { m_color = color; }
		void AddANode(Region::LkoEle *node, bool bIsRoot);
		void AddEnded() { m_updateListset.clear(); }
		virtual bool Render(Canvas* canvas);
	};

}
