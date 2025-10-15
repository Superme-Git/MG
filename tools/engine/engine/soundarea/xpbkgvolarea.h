#ifndef NUCLEAR_BKG_VOLUME_AREA
#define NUCLEAR_BKG_VOLUME_AREA

#pragma once
#include "xpsoundarea.h"

class CSoundEditorDoc;
class SoundApp;

namespace Nuclear
{
	class XPBkgVolArea :
		public XPSoundArea
	{
	protected:
		float m_fVol;//[0, 1]
		int m_nTransition;		//扩展区（过渡区）宽度

	public:
		XPBkgVolArea(void) : m_fVol(1.0f), m_nTransition(0) {}
		virtual ~XPBkgVolArea(void) {}
		void LoadAttrib(XMLIO::CINode& node);
		void SaveAttrib(XMLIO::CONode& node);

		float GetVol() const { return m_fVol; }
		int GetTransition() const { return m_nTransition; }

		virtual float GetAreaVolume(const CPOINT& pos) const = 0;
	};

	class XPBkgVolCircleArea :
		public XPBkgVolArea
	{
		friend class ::CSoundEditorDoc;
		friend class ::SoundApp;
		//属性们
	protected:
		XPCircle m_circle;
		//方法们
		//构造析构
	public:
		XPBkgVolCircleArea() {}
		virtual ~XPBkgVolCircleArea(void) {}

		//实现
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_CIRCLE; }
		virtual int Load(XMLIO::CINode& node, int flag);
		virtual int Save(XMLIO::CONode& node, int flag);
		virtual float GetAreaVolume(const CPOINT& pos) const;
	};

	class XPBkgVolPolygonArea :
		public XPBkgVolArea
	{
		//自定义类型
	public:
		friend class ::CSoundEditorDoc;
		friend class ::SoundApp;

		//属性们
	protected:
		XPPolygon m_innerPolygon;
		XPPolygon m_outterPolygon;

		//方法们
	protected:
		virtual int LoadToolsData(XMLIO::CINode& node) { return 0; }
		virtual int SaveToolsData(XMLIO::CONode& node) const { return 0; }
	public:
		//构造析构
		XPBkgVolPolygonArea() {}
		virtual ~XPBkgVolPolygonArea(void) {}

		//实现
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_POLYGON; }
		virtual int Load(XMLIO::CINode& node, int flag);
		virtual int Save(XMLIO::CONode& node, int flag);
		virtual float GetAreaVolume(const CPOINT& pos) const;
	};
}

#endif //#ifndef NUCLEAR_BKG_VOLUME_AREA
