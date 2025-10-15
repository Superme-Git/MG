#include "stdafx.h"
#include "..\common\log.h"
#include "xpsoundarea.h"


namespace Nuclear
{

	XPSoundArea::XPSoundArea(void)
	{
	}

	XPSoundArea::~XPSoundArea(void)
	{
	}

	void XPSoundArea::LoadRegion(XMLIO::CINode& node, RegionSet& regionSet)
	{
		regionSet.clear();
		XMLIO::CNodeList sNodeList;
		node.GetChildren(sNodeList);
		XMLIO::CNodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			XMLIO::CINode& snode = *nlIt;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			XPASSERT(snode.GetName()==L"region");
			regionSet.insert(snode.GetAttributeInteger(L"id"));
		}
	}

	void XPSoundArea::SaveRegion(XMLIO::CONode& node, const RegionSet& regionSet)
	{
		RegionSet::const_iterator it = regionSet.begin(), ie = regionSet.end();
		for (;it!=ie;++it)
		{
			XMLIO::CONode snode;
			node.CreateSubElement(L"region", snode);
			snode.SetAttribute(L"id", *it);
		}
	}

	void XPSoundArea::LoadPolygon(XMLIO::CINode& node, XPPolygon& polygon)
	{
		polygon.m_points.clear();
		XMLIO::CNodeList nl;
		node.GetChildren(nl);
		XMLIO::CNodeList::iterator it = nl.begin(), ie = nl.end();
		CPOINT pt;
		for (;it!=ie;++it)
		{
			XMLIO::CINode& snode = *it;
			if (snode.GetType() != XMLIO::NT_ELEMENT)
				continue;
			XPASSERT(snode.GetName()==L"point");
			pt.x = snode.GetAttributeInteger(L"x");
			pt.y = snode.GetAttributeInteger(L"y");
			polygon.m_points.push_back(pt);
		}
	}

	void XPSoundArea::SavePolygon(XMLIO::CONode& node, const XPPolygon& polygon)
	{
		std::vector<CPOINT>::const_iterator it = polygon.m_points.begin(), ie = polygon.m_points.end();
		for (;it!=ie;++it)
		{
			XMLIO::CONode snode;
			node.CreateSubElement(L"point", snode);
			snode.SetAttribute(L"x", it->x);
			snode.SetAttribute(L"y", it->y);
		}
	}
}
