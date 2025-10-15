#include "xpsoundarea.h"
#include "nulog.h"

namespace Nuclear
{

	XPSoundArea::XPSoundArea(void)
	{
	}

	XPSoundArea::~XPSoundArea(void)
	{
	}

	void XPSoundArea::LoadRegion(LJXML::LJXML_Node<>& node, RegionSet& regionSet)
	{
		regionSet.clear();
		LJXML::LJXML_NodeList sNodeList;
		node.GetChildren(sNodeList);
		LJXML::LJXML_NodeList::iterator nlIt = sNodeList.begin(), nlIe = sNodeList.end();
		for (;nlIt!=nlIe;++nlIt)
		{
			LJXML::LJXML_Node<>& snode = **nlIt;
			if (snode.GetType() != rapidxml::node_element)
				continue;
			XPASSERT(snode.GetName()==L"region");
			regionSet.insert(snode.GetAttributeInteger(L"id"));
		}
	}

	void XPSoundArea::SaveRegion(LJXML::LJXML_Node<>& node, const RegionSet& regionSet)
	{
	}

	void XPSoundArea::LoadPolygon(LJXML::LJXML_Node<>& node, NuclearPolygon& polygon)
	{
		polygon.m_points.clear();
		LJXML::LJXML_NodeList nl;
		node.GetChildren(nl);
		LJXML::LJXML_NodeList::iterator it = nl.begin(), ie = nl.end();
		NuclearPoint pt;
		for (;it!=ie;++it)
		{
			LJXML::LJXML_Node<>& snode = **it;
			if (snode.GetType() != rapidxml::node_element)
				continue;
			XPASSERT(snode.GetName()==L"point");
			pt.x = snode.GetAttributeInteger(L"x");
			pt.y = snode.GetAttributeInteger(L"y");
			polygon.m_points.push_back(pt);
		}
	}

	void XPSoundArea::SavePolygon(LJXML::LJXML_Node<>& node, const NuclearPolygon& polygon)
	{
	}
}
