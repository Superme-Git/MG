/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxTipsLinkComponent_h_
#define _CEGUIRichEditboxTipsLinkComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxTipsLinkComponent:public RichEditboxLinkTextComponent 
	{
	public:
		//! Destructor.
		virtual ~RichEditboxTipsLinkComponent();
		RichEditboxTipsLinkComponent(const String& text,const String& font_name,
			int64_t _roleID,int type,const String& key,int baseid, int64_t _shopID, 
			int _counterID,int _bind, int64_t _loseeffecttime);

		String GenerateParseText();

		void SetObjectType(int type) { d_ObjectType=type; }
		int GetObjectType() { return d_ObjectType; }

		void SetObjectKey(const String& key) { d_ObjectKey=key; }
		String GetObjectKey() { return d_ObjectKey; }
		bool onMouseButtonDown(MouseEventArgs& e);
		bool onMouseButtonUp(MouseEventArgs& e);

		void SetShopID(int64_t _shopID) { d_shopID=_shopID; }
		int64_t  GetShopID() { return d_shopID; }
		void SetCounterID( int _counterID ) { d_counterID=_counterID; }
		int  GetCounterID() { return d_counterID; }

		int64_t GetRoleID() { return d_RoleID; }
		void SetRoleID(int64_t _roleID) { d_RoleID=_roleID; } 

		int GetBind() { return d_Bind; }
		void SetBind(int _bind) { d_Bind=_bind; }

		int64_t GetLoseEffectTime() { return d_loseeffecttime; }
		void SetLoseEffectTime(int64_t _loseeffecttime) { d_loseeffecttime=_loseeffecttime; }

		//----------------------------------------------------------------------------//
		bool canSplit();
		RichEditboxTipsLinkComponent* split(float split_point, bool first_component);
		Point GetOffsetOfIndex(size_t index);
	private:
		int64_t d_RoleID;
		int d_ObjectType;
		String d_ObjectKey;
		int d_ObjBaseID;
		int64_t d_shopID;
		int d_counterID;
		int d_Bind; //是否绑定
		int64_t d_loseeffecttime; // 失效时间
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
