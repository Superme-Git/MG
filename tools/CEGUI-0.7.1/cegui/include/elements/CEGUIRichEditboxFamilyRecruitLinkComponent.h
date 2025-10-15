/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxFamilyRecruitLinkComponent_h_
#define _CEGUIRichEditboxFamilyRecruitLinkComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxFamilyRecruitLinkComponent:public RichEditboxLinkTextComponent 
	{
	public:
		//! Destructor.
		virtual ~RichEditboxFamilyRecruitLinkComponent();

		RichEditboxFamilyRecruitLinkComponent(int familyID,const String& familyName,
			int64_t leaderID,const String& leader,int rank,int level,const String& aim);
		
		 virtual String GenerateParseText();
		 String GeneratePureText(size_t startPos,size_t endPos);
		 bool onMouseButtonDown(MouseEventArgs& e);
		 bool onMouseButtonUp(MouseEventArgs& e);

		 int	GetFamilyID() { return d_FamilyID; }
		 String GetFamilyName() { return d_FamilyName; }
		 String GetLeaderName() { return d_LeaderName; }
		 int GetFamilyRank() { return d_FamilyRank; }
		 int GetFamilyLevel() { return d_FamilyLevel; }
		 String GetFamilyAim() { return d_FamilyAim; }

		 void SetFamilyID(int id) { d_FamilyID=id;  }
		 void SetFamilyName(const String& name) { d_FamilyName=name;  }
		 void SetLeaderName(const String& name) { d_LeaderName=name; }
		 void SetFamilyLevel(int level) { d_FamilyLevel=level; }
		 void SetFamilyRank(int rank) { d_FamilyRank=rank; }
		 void SetFamilyAim(const String& aim) { d_FamilyAim = aim; }

		 bool canSplit(){ return true; }
		 RichEditboxFamilyRecruitLinkComponent* split(float split_point, bool first_component);

	private:
		int	   d_FamilyID;
		String d_FamilyName;
		int64_t d_LeaderID;
		String d_LeaderName;
		int     d_FamilyRank; //ÅÅÃû
		int    d_FamilyLevel;
		String d_FamilyAim; //×ÚÖ¼
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
