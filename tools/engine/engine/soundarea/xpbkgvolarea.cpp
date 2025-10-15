#include "stdafx.h"
#include "..\common\log.h"
#include "xpbkgvolarea.h"

namespace Nuclear
{
	void XPBkgVolArea::LoadAttrib(XMLIO::CINode& node)
	{
		m_fVol = (float)node.GetAttributeFloat(L"vol", 1.0f);
		m_nTransition = node.GetAttributeInteger(L"l");
	}

	void XPBkgVolArea::SaveAttrib(XMLIO::CONode& node)
	{
		node.SetAttribute(L"vol", m_fVol);
		node.SetAttribute(L"l", m_nTransition);
	}

	int XPBkgVolCircleArea::Load(XMLIO::CINode& node, int flag)
	{
		LoadAttrib(node);
		LoadCircle(node, m_circle);
		XMLIO::CNodeList sNodeList;
		node.GetChildren(sNodeList);
		XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			XMLIO::CINode& snode = *nlIt;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			if (snode.GetName()==L"regions") 
			{
				LoadRegion(snode, m_regionSet);
			} else {
				XPASSERT(false);
			}
		}
		return 0;
	}

	int XPBkgVolCircleArea::Save(XMLIO::CONode& node, int flag)
	{
		SaveAttrib(node);
		SaveCircle(node, m_circle);
		XMLIO::CONode regionnode;
		node.CreateSubElement(L"regions", regionnode);
		SaveRegion(regionnode, m_regionSet);
		return 0;
	}

	float XPBkgVolCircleArea::GetAreaVolume(const CPOINT& pos) const
	{
		float dis = m_circle.DistanceTo(pos.x, pos.y);
		if (dis <= 0)
			return m_fVol;
		if (dis >= m_nTransition)
			return 1.0f;
		return dis / m_nTransition * (1 - m_fVol) + m_fVol;
	}

	int XPBkgVolPolygonArea::Load(XMLIO::CINode& node, int flag)
	{
		LoadAttrib(node);
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

	int XPBkgVolPolygonArea::Save(XMLIO::CONode& node, int flag)
	{
		SaveAttrib(node);
		XPASSERT(m_innerPolygon.m_points.size() == m_outterPolygon.m_points.size());
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

	float XPBkgVolPolygonArea::GetAreaVolume(const CPOINT& pos) const
	{
		if (m_innerPolygon.IsPointIn(pos.x, pos.y))	//在小区域里面
			return m_fVol;

		if (m_outterPolygon.IsPointIn(pos.x, pos.y))	//在大区域里面
			return m_innerPolygon.DistanceTo(pos.x, pos.y) / m_nTransition * (1 - m_fVol) + m_fVol;

		return 1.0f;
	}
}
