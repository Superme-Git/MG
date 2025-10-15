#include "xpenvsoundpolygonarea.h"
#include "nulog.h"

namespace Nuclear
{
	XPEnvSoundPolygonArea::XPEnvSoundPolygonArea(EngineBase *pEB) : XPEnvSoundArea(pEB)
	{
	}

	XPEnvSoundPolygonArea::~XPEnvSoundPolygonArea(void)
	{
	}

	int XPEnvSoundPolygonArea::Load(LJXML::LJXML_Node<>& node, int flag)
	{
		m_nTransition = node.GetAttributeInteger(L"l");
		LJXML::LJXML_NodeList sNodeList;
		node.GetChildren(sNodeList);
		LJXML::LJXML_NodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			LJXML::LJXML_Node<>& snode = **nlIt;
			if (snode.GetType() != rapidxml::node_element)
				continue;
			if (snode.GetName()==L"inner") 
			{
				LoadPolygon(snode, m_innerPolygon);
			} else if (snode.GetName()==L"outter")
			{
				LoadPolygon(snode, m_outterPolygon);
			} else if (snode.GetName()==L"regions")
			{
				LoadRegion(snode, m_regionSet);
			} else if (snode.GetName()==L"sounds")
			{
				LoadSound(snode);
			} else if (snode.GetName()==L"TOOLS_DATA")
			{
				if (flag & XPSOUND_IO_EDIT)	//切割信息
				{
					LoadToolsData(snode);
				}
			} else
				XPASSERT(false);
		}
		return 0;
	}

	int XPEnvSoundPolygonArea::Save(LJXML::LJXML_Node<>& node, int flag)
	{
		return 0;
	}

	float XPEnvSoundPolygonArea::GetAreaVolume(const NuclearPoint& pos) const
	{
		if (m_innerPolygon.IsPointIn(pos.x, pos.y))	//在小区域里面
			return 1.0f;

		if (m_outterPolygon.IsPointIn(pos.x, pos.y))	//在大区域里面
			return (m_nTransition - m_innerPolygon.DistanceTo(pos.x, pos.y)) / m_nTransition;

		return 0.0f;
	}

}
