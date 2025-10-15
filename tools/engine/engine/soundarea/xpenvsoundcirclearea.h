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
		XPCircle m_circle;
	//������
	//��������
	public:
		XPEnvSoundCircleArea(EngineBase *pEB);
		virtual ~XPEnvSoundCircleArea(void);

	//ʵ��
		virtual XPAREA_TYPE GetType() const { return XPAREA_TYPE_CIRCLE; }
		virtual int Load(XMLIO::CINode& node, int flag);
		virtual int Save(XMLIO::CONode& node, int flag);
		virtual float GetAreaVolume(const CPOINT& pos) const;
	};
}
#endif	//__Nuclear_XPENVSOUNDCIRCLEAREA_H
