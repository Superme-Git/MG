/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxComponent_h_
#define _CEGUIRichEditboxComponent_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "../CEGUIFont.h"
#include "CEGUIRichEditboxProperties.h"


#include <vector>


//#if defined(_MSC_VER)
//#	pragma warning(push)
//#	pragma warning(disable : 4251)
//#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	enum RichEditboxComponentType
	{
		RichEditboxComponentType_None,
		RichEditboxComponentType_Text,
		RichEditboxComponentType_Image,
		RichEditboxComponentType_Break,   //换行
		RichEditboxComponentType_LinkText,   //超文本链接
		RichEditboxComponentType_GoToText,   //自动寻路链接
		RichEditboxComponentType_Emotion,       //表情
		RichEditboxComponentType_Tips,         //物品，技能，修炼等链接
		RichEditboxComponentType_NameLink,         //玩家名字链接
		RichEditboxComponentType_ButtonImage,         //按钮图片
		RichEditboxComponentType_FamilyRecruitLink,         //家族招募链接
		RichEditboxComponentType_HttpAddress,   //网页链接
		RichEditboxComponentType_JoinTeam,	//申请入队
		RichEditboxComponentType_RequestTeam,	//请求队伍信息 by yq 2016-03-25
		RichEditboxComponentType_AnswerQuestion,	//答题
		RichEditboxComponentType_OpenDialog,  //打开dialog窗口
		RichEditboxComponentType_CommonLink	//通用链接，参数拼成字符串

	};
	class CEGUIEXPORT RichEditboxComponent:  public EventSet 
	{
	public:
		//! Destructor.
		
		static const String EventNamespace;
		static const String EventMouseButtonDown;			//!< The read-only mode for the edit box has been changed.

		static const String EventMouseEnter; //mouse进入事件
		static const String EventMouseLeave; //mouse离开事件

		static const String EventGuideEnd; //引导结束事件



		RichEditboxComponent();

		virtual ~RichEditboxComponent(){}

		//! Copy constructor.
		 RichEditboxComponent(const RichEditboxComponent& other);
		//! Assignment.
		 RichEditboxComponent& operator=(const RichEditboxComponent& rhs);

		//! draw the component.
		virtual void draw(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect,
			const float vertical_space,
			const float space_extra,
			bool bTextBorder) {}  //btextborder为文字是否描边 

		virtual void DrawSelectFlag(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect,
			const Image* pSeleImage=NULL,
			size_t selectStartIdx=0,size_t selectEndIdx=0);

		virtual void DrawMouseOnFlag(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect);

		

		//! return the pixel size of the rendered component.
		virtual Size getPixelSize() { return Size(0.0f,d_LineSpace); }
        
        virtual Size getDrawPixelSize() { return getPixelSize(); }
		//! return whether the component can be split
		virtual bool canSplit() {return false;}

		virtual RichEditboxComponent* split(float split_point,
			bool first_component) {return NULL;}

		//! clone this component.
		virtual RichEditboxComponent* clone()  {return NULL;}

		//! return the total number of spacing characters in the string.
		virtual size_t getSpaceCount() {return 0;}

		RichEditboxComponentType GetType() const{ return d_Type; }

		virtual Point GetOffsetOfIndex(size_t index);
		virtual size_t GetMaxIndex(){return 1;}
		virtual size_t GetIndexByWidth( float width);
		virtual bool HandleBackSpace(size_t offset){ return true; }  //返回true需则删除当前的component
		virtual  RichEditboxComponent* HandleChar(size_t offset,const String& InsetText,const ColourRect& ColorRect=ColourRect()){ return NULL; }   //返回true则需生成新的textcomponent
		virtual  RichEditboxComponent* InsertComponent(size_t offset,RichEditboxComponent* pComponent){ return NULL; }   //返回true则需生成新的component


		virtual String GenerateParseText(){ return String("");}
		virtual String GeneratePureText(size_t startPos,size_t endPos){ return ""; }
		bool IsMouseOn() { return d_bIsMouseOn; }

		//获得该控件拆分后新的光标位置，curidx::当前位置 newcaratIdx新的光标位置 返回true则表明新光标是在拆分后的新控件里
		virtual bool GetNewCaratPos(float width,size_t curIdx,size_t& newCaratIdx);
        
        virtual bool isPushed() { return false; }

		

		
    protected:
		//! Protected constructor.
		Rect d_padding;
		bool d_bIsMouseOn;
		RichEditboxComponentType d_Type;
	public:
		virtual	bool    onMouseButtonDown(MouseEventArgs& e); 
		virtual bool	onMouseButtonUp(MouseEventArgs& e){ return false; } //返回true说明子控件状态有变化，需要redraw
		virtual	void	onMouseDoubleClicked(MouseEventArgs& e){ return; }
		virtual void	onMouseMove(MouseEventArgs& e){  return; }
		virtual void    onMouseEnter();
		virtual void    onMouseLeave();

		virtual bool  updata(float fElapse) { return false; }

		void* GetUserData() { return d_UserData; }
		void  SetUserData( void* pData )
		{
			d_UserData=pData;
		}

		void SetUserID(int64_t id) { d_UserID = id;}
		int64_t GetUserID(){ return d_UserID;}

		void SetLineSpace(float fLineSpace) { d_LineSpace=fLineSpace;  }
		float GetLineSpace() { return d_LineSpace; }

		virtual int GetCharCount() { return 1; } //获得字符个数

		virtual size_t GetCharNumByIndex(size_t index) { return 1; }

		virtual bool isOnlySpace() { return false; } //只否只有空字符

		void SetHasGuide(bool bGuide) { d_HasGuide=bGuide; }

		virtual bool isTextType() { return false; } //是否是文字类型的子控件

		static void InitSpecialChar(); //初始化特殊xml转义字符
		static void ReplaceSpecialChar(String& text);  //替换特殊xml转义字符

		static std::map<String,String> d_mapSpecialChar; //特殊xml转义字符

	private:
		
		void* d_UserData;

		int64_t d_UserID;//
		float d_LineSpace;  //行间距
		bool d_HasGuide;  //是否有引导


	};
}

//#if defined (_MSC_VER)
//#	pragma warning(pop)
//#endif

#endif // end of guard _CEGUICentredRenderedString_h_
