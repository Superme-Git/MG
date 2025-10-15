#include "stdafx.h"
#include "..\common\log.h"
#include "xpenvsoundpolygonarea.h"


namespace Nuclear
{
	XPEnvSoundPolygonArea::XPEnvSoundPolygonArea(EngineBase *pEB) : XPEnvSoundArea(pEB)
	{
	}

	XPEnvSoundPolygonArea::~XPEnvSoundPolygonArea(void)
	{
	}

	int XPEnvSoundPolygonArea::Load(XMLIO::CINode& node, int flag)
	{
		m_nTransition = node.GetAttributeInteger(L"l");
		XMLIO::CNodeList sNodeList;
		node.GetChildren(sNodeList);
		XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			XMLIO::CINode& snode = *nlIt;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
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

	int XPEnvSoundPolygonArea::Save(XMLIO::CONode& node, int flag)
	{
		node.SetAttribute(L"l", m_nTransition);
		XPASSERT(m_innerPolygon.m_points.size() == m_outterPolygon.m_points.size());
		XMLIO::CONode sounds;
		node.CreateSubElement(L"sounds", sounds);
		SaveSound(sounds);
		XMLIO::CONode inner;
		node.CreateSubElement(L"inner", inner);
		SavePolygon(inner, m_innerPolygon);
		XMLIO::CONode outter;
		node.CreateSubElement(L"outter", outter);
		SavePolygon(outter, m_outterPolygon);
		XMLIO::CONode regions;
		node.CreateSubElement(L"regions", regions);
		SaveRegion(regions, m_regionSet);
		if (flag & XPSOUND_IO_EDIT)
		{
			XMLIO::CONode tools;
			node.CreateSubElement(L"TOOLS_DATA", tools);
			SaveToolsData(tools);
		}
		return 0;
	}

	float XPEnvSoundPolygonArea::GetAreaVolume(const CPOINT& pos) const
	{
		if (m_innerPolygon.IsPointIn(pos.x, pos.y))	//在小区域里面
			return 1.0f;

		if (m_outterPolygon.IsPointIn(pos.x, pos.y))	//在大区域里面
			return (m_nTransition - m_innerPolygon.DistanceTo(pos.x, pos.y)) / m_nTransition;

		return 0.0f;
	}

}
