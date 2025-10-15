/***********************************************************************
	filename: 	CEGUIRichEditbox.h
*************************************************************************/

#ifndef _CEGUIRichEditboxAnswerQuestionLinkComponent_h_
#define _CEGUIRichEditboxAnswerQuestionLinkComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxAnswerQuestionLinkComponent:public RichEditboxLinkTextComponent 
	{
	public:
		//! Destructor.
		virtual ~RichEditboxAnswerQuestionLinkComponent() {}
		RichEditboxAnswerQuestionLinkComponent(const String& text, int question, const String& name, int roleid, int type);
		bool onMouseButtonUp(MouseEventArgs& e);

		bool canSplit(){ return true; }
		RichEditboxAnswerQuestionLinkComponent* split(float split_point, bool first_component);
	private:
		int _questionid;
		String _name;
		int _roleid;
		int _type;
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUIRichEditboxAnswerQuestionLinkComponent_h_
