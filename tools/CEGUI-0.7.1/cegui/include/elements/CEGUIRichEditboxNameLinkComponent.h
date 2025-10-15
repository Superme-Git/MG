/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxNameLinkComponent_h_
#define _CEGUIRichEditboxNameLinkComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxNameLinkComponent:public RichEditboxLinkTextComponent 
	{
	public:
		//! Destructor.
		virtual ~RichEditboxNameLinkComponent();

		RichEditboxNameLinkComponent(int64_t roleID,const String& roleName);
		
		 virtual String GenerateParseText();
		 String GeneratePureText(size_t startPos,size_t endPos);
		 bool onMouseButtonDown(MouseEventArgs& e);
		 bool onMouseButtonUp(MouseEventArgs& e);

		 int64_t GetRoleID() { return d_roleID; }
		 String GetRoleName() { return d_roleName; }

		 void SetRoleID(int64_t id) { d_roleID=id;  }
		 void SetRoleName(const String& name);
		 void SetNameColorType(int type);

		 bool canSplit(){ return true; }
		 RichEditboxNameLinkComponent* split(float split_point, bool first_component);

	private:
		int64_t d_roleID;
		String d_roleName;
		int d_NameColorType;  //名字颜色类型,0为默认,1为点击某个人出来的链接颜色
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
