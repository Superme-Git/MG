#ifndef __Nuclear_XPENVSOUNDCIRCLEAREA_H
#define __Nuclear_XPENVSOUNDCIRCLEAREA_H

#if _MSC_VER > 1000 
#pragma once 
#endif // _MSC_VER > 1000 
#include "xpenvsoundarea.h"
class CSoundEditorDoc;
class SoundApp;
namespace Nuclear
{

	class XPEnvSoundCircleArea :
		public XPEnvSoundArea
	{
		friend class ::CSoundEditorDoc;
		friend class ::SoundApp;
	//������
	protected:
		NuclearCircle m_circle;
	//������
	//��������
	public:
		XPEnvSoundCircleArea(EngineBase *pEB);
		virtual ~XPEnvSoundCircleArea(void);

	//ʵ��
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_CIRCLE; }
		virtual int Load(LJXML::LJXML_Node<>& node, int flag);
		virtual int Save(LJXML::LJXML_Node<>& node, int flag);
		virtual float GetAreaVolume(const NuclearPoint& pos) const;
	};
}
#endif	//__Nuclear_XPENVSOUNDCIRCLEAREA_H
