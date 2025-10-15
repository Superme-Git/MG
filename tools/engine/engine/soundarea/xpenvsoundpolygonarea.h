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
	//�Զ�������
	public:
		friend class ::CSoundEditorDoc;
		friend class ::SoundApp;

	//������
	protected:
		XPPolygon m_innerPolygon;
		XPPolygon m_outterPolygon;

	//������
	protected:
		virtual int LoadToolsData(XMLIO::CINode& node) { return 0; }
		virtual int SaveToolsData(XMLIO::CONode& node) const { return 0; }
	public:
		//��������
		XPEnvSoundPolygonArea(EngineBase *pEB);
		virtual ~XPEnvSoundPolygonArea(void);

		//ʵ��
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_POLYGON; }
		virtual int Load(XMLIO::CINode& node, int flag);
		virtual int Save(XMLIO::CONode& node, int flag);
		virtual float GetAreaVolume(const CPOINT& pos) const;
	};

}
#endif //__Nuclear_XPENVSOUNDPOLYGONAREA_H
