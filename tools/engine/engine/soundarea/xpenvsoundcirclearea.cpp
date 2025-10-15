#include "stdafx.h"
#include "..\common\log.h"
#include "xpenvsoundcirclearea.h"


namespace Nuclear
{
	XPEnvSoundCircleArea::XPEnvSoundCircleArea(EngineBase *pEB) : XPEnvSoundArea(pEB)
	{
	}

	XPEnvSoundCircleArea::~XPEnvSoundCircleArea(void)
	{
	}

	int XPEnvSoundCircleArea::Load(XMLIO::CINode& node, int flag)
	{
		LoadCircle(node, m_circle);
		m_nTransition = node.GetAttributeInteger(L"l");
		XMLIO::CNodeList sNodeList;
		node.GetChildren(sNodeList);
		XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			XMLIO::CINode& snode = *nlIt;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
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

	int XPEnvSoundCircleArea::Save(XMLIO::CONode& node, int flag)
	{
		SaveCircle(node, m_circle);
		node.SetAttribute(L"l", m_nTransition);
		XMLIO::CONode soundnode;
		node.CreateSubElement(L"sounds", soundnode);
		SaveSound(soundnode);
		XMLIO::CONode regionnode;
		node.CreateSubElement(L"regions", regionnode);
		SaveRegion(regionnode, m_regionSet);
		return 0;
	}

	float XPEnvSoundCircleArea::GetAreaVolume(const CPOINT& pos) const
	{
		float dis = m_circle.DistanceTo(pos.x, pos.y);
		if (dis <= 0)
			return 1.0f;
		if (dis >= m_nTransition)
			return 0.0f;
		return (m_nTransition - dis) / m_nTransition;
	}

}
