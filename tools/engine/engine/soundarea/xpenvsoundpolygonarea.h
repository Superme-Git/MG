#ifndef __Nuclear_XPENVSOUNDPOLYGONAREA_H
#define __Nuclear_XPENVSOUNDPOLYGONAREA_H

#if _MSC_VER > 1000 
#pragma once 
#endif // _MSC_VER > 1000 
#include "xpenvsoundarea.h"
class CSoundEditorDoc;
class SoundApp;
namespace Nuclear
{
	class XPEnvSoundPolygonArea :
		public XPEnvSoundArea
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
		XPEnvSoundPolygonArea(EngineBase *pEB);
		virtual ~XPEnvSoundPolygonArea(void);

		//实现
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_POLYGON; }
		virtual int Load(XMLIO::CINode& node, int flag);
		virtual int Save(XMLIO::CONode& node, int flag);
		virtual float GetAreaVolume(const CPOINT& pos) const;
	};

}
#endif //__Nuclear_XPENVSOUNDPOLYGONAREA_H
