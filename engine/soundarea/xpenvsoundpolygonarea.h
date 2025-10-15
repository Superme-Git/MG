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
		NuclearPolygon m_innerPolygon;
		NuclearPolygon m_outterPolygon;

	//方法们
	protected:
		virtual int LoadToolsData(LJXML::LJXML_Node<>& node) { return 0; }
		virtual int SaveToolsData(LJXML::LJXML_Node<>& node) const { return 0; }
	public:
		//构造析构
		XPEnvSoundPolygonArea(EngineBase *pEB);
		virtual ~XPEnvSoundPolygonArea(void);

		//实现
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_POLYGON; }
		virtual int Load(LJXML::LJXML_Node<>& node, int flag);
		virtual int Save(LJXML::LJXML_Node<>& node, int flag);
		virtual float GetAreaVolume(const NuclearPoint& pos) const;
	};

}
#endif //__Nuclear_XPENVSOUNDPOLYGONAREA_H
