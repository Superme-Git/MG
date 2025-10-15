#include "xpenvsoundcirclearea.h"
#include "nulog.h"

namespace Nuclear
{
	XPEnvSoundCircleArea::XPEnvSoundCircleArea(EngineBase *pEB) : XPEnvSoundArea(pEB)
	{
	}

	XPEnvSoundCircleArea::~XPEnvSoundCircleArea(void)
	{
	}

	int XPEnvSoundCircleArea::Load(LJXML::LJXML_Node<>& node, int flag)
	{
		LoadCircle(node, m_circle);
		m_nTransition = node.GetAttributeInteger(L"l");
		LJXML::LJXML_NodeList sNodeList;
		node.GetChildren(sNodeList);
		LJXML::LJXML_NodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			LJXML::LJXML_Node<>& snode = **nlIt;
			if (snode.GetType() != rapidxml::node_element)
				continue;
			if (snode.GetName()==L"sounds")
			{
				LoadSound(snode);
			} else if (snode.GetName()==L"regions") {
				LoadRegion(snode, m_regionSet);
			} else {
				XPASSERT(false);
			}
		}
		return 0;
	}

	int XPEnvSoundCircleArea::Save(LJXML::LJXML_Node<>& node, int flag)
	{
		return 0;
	}

	float XPEnvSoundCircleArea::GetAreaVolume(const NuclearPoint& pos) const
	{
		float dis = m_circle.DistanceTo(pos.x, pos.y);
		if (dis <= 0)
			return 1.0f;
		if (dis >= m_nTransition)
			return 0.0f;
		return (m_nTransition - dis) / m_nTransition;
	}

}
