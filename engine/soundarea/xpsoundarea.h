#ifndef __Nuclear_XPSOUNDAREA_H
#define __Nuclear_XPSOUNDAREA_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <set>
#include "../../dependencies/LJXML/Include/LJXML.hpp"
#include "nuxpmaths.h"

namespace Nuclear
{
	enum XPSOUND_IO_FLAG
	{
		XPSOUND_IO_NORMAL			= 0x00000000,		//! 正常版本
		XPSOUND_IO_EDIT				= 0x00000001,		//! 编辑器版本
	};

	enum XPAREA_TYPE
	{
		XPAREA_TYPE_CIRCLE = 0,
		XPAREA_TYPE_POLYGON,
		XPAREA_TYPE_NULL
	};

	enum XPSA_SOUND_TYPE
	{
		XPSA_DAY = 1,
		XPSA_NIGHT = 2,
		XPSA_BOTH = 3,
	};

	class XPSoundArea
	{
	//自定义类型们
	public:
		typedef std::set<int> RegionSet;
	//成员们
	protected:
		RegionSet m_regionSet;

	//构造析构
	public:
		XPSoundArea(void);
		virtual ~XPSoundArea(void);
	//方法们
		static void LoadRegion(LJXML::LJXML_Node<>& node, RegionSet& regionSet);
		static void SaveRegion(LJXML::LJXML_Node<>& node, const RegionSet& regionSet);
		static void LoadPolygon(LJXML::LJXML_Node<>& node, NuclearPolygon& polygon);
		static void SavePolygon(LJXML::LJXML_Node<>& node, const NuclearPolygon& polygon);
		inline static void LoadCircle(LJXML::LJXML_Node<>& node, NuclearCircle& circle);
		inline static void SaveCircle(LJXML::LJXML_Node<>& node, const NuclearCircle& circle);
		const RegionSet& GetRegionSet() const { return m_regionSet; }
		virtual XPAREA_TYPE GetType() const = 0;
		virtual int Load(LJXML::LJXML_Node<>& node, int flag) = 0;
		virtual int Save(LJXML::LJXML_Node<>& node, int flag) = 0;
	};

	void XPSoundArea::LoadCircle(LJXML::LJXML_Node<>& node, NuclearCircle& circle)
	{
		circle.m_pos.x = node.GetAttributeInteger(L"centerx");
		circle.m_pos.y = node.GetAttributeInteger(L"centery");
		circle.m_nRadius = node.GetAttributeInteger(L"radius");
	}

	void XPSoundArea::SaveCircle(LJXML::LJXML_Node<>& node, const NuclearCircle& circle)
	{
		//node.SetAttribute(L"centerx", circle.m_pos.x);
		//node.SetAttribute(L"centery", circle.m_pos.y);
		//node.SetAttribute(L"radius", circle.m_nRadius);
	}

}

#endif //#ifndef __Nuclear_XPSOUNDAREA_H
