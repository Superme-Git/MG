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
		NuclearPolygon m_innerPolygon;
		NuclearPolygon m_outterPolygon;

	//������
	protected:
		virtual int LoadToolsData(LJXML::LJXML_Node<>& node) { return 0; }
		virtual int SaveToolsData(LJXML::LJXML_Node<>& node) const { return 0; }
	public:
		//��������
		XPEnvSoundPolygonArea(EngineBase *pEB);
		virtual ~XPEnvSoundPolygonArea(void);

		//ʵ��
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_POLYGON; }
		virtual int Load(LJXML::LJXML_Node<>& node, int flag);
		virtual int Save(LJXML::LJXML_Node<>& node, int flag);
		virtual float GetAreaVolume(const NuclearPoint& pos) const;
	};

}
#endif //__Nuclear_XPENVSOUNDPOLYGONAREA_H
