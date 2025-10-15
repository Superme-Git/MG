/***********************************************************************
	filename: 	CEGUIItemTable.h
	created:	26/11/2010
	author:		Amen
*************************************************************************/

#ifndef _CEGUISKILLBOX_H
#define _CEGUISKILLBOX_H

#include "../CEGUIWindow.h"
#include "../CEGUIWindowFactory.h"


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
    /*!
    \brief
        Generic drag & drop enabled window class
    */

	enum SkillBoxTextType         //cell一共有
	{
		SkillBoxTextType_LeftTop,  //文字在左上角
		SkillBoxTextType_LeftBottom,  //文字在左下角
		SkillBoxTextType_RightBottom,//文字在右下角
		SkillBoxTextType_NUM
	};

	struct SkillBoxTextUnit 
	{
		String Text;
		SkillBoxTextType type;
		colour color;

		SkillBoxTextUnit():
		Text(""),
		type(SkillBoxTextType_LeftBottom)
		{
		}
	};

	const colour DefaultSkillBoxTextColor=colour(1.0f,1.0f,1.0f,1.0);
	
    
	class CEGUIEXPORT SkillBox : public Window
    {
    public:
       
        static const String WidgetTypeName;     //!< Type name for DragContainer.
        static const String EventNamespace;     //!< Namespace for global events
		static const String EventOnClick;    //!< click events
		static const int SkillBoxSize = 96;
		//static const String EventUpdateSkillInfo;

        SkillBox(const String& type, const String& name);
        virtual ~SkillBox(void);

		bool testClassName_impl(const String& class_name) const
		{
			if (class_name==(const utf8*)"SkillBox")
				return true;

			return Window::testClassName_impl(class_name);
		}
		void SetImage( const Image* image);
		const Image* GetImage() const { return d_image; }
        
        void SetBackgroundDynamic(bool bDyn){ m_bBackgroundDynamic = bDyn; }
        bool GetBackgroundDynamic(){ return m_bBackgroundDynamic; }
        bool m_bBackgroundDynamic;

		void SetImage(String Imageset, String ImageName);
		bool isMouseOn() { return d_isMouseOn; }
		void SetTextUnit(CEGUI::String text,SkillBoxTextType type=SkillBoxTextType_LeftBottom,CEGUI::colour color=DefaultSkillBoxTextColor );
		void SetTextUnitText(CEGUI::String text,SkillBoxTextType type=SkillBoxTextType_RightBottom);
		void SetTextUnitColor(CEGUI::colour color,SkillBoxTextType type=SkillBoxTextType_RightBottom);
		SkillBoxTextUnit* GetTextUnit(SkillBoxTextType type=SkillBoxTextType_RightBottom);
		CEGUI::Point GetTextUnitDrawPos(const Rect& rect,SkillBoxTextType type=SkillBoxTextType_RightBottom); //获得

		bool isBackGroundEnable()  { return d_BackGroundEnable; }
		void SetBackGroundEnable(bool bEnable);

		bool IsAshy()  { return d_ashy;}
		void SetAshy(bool bAshy) { d_ashy = bAshy; invalidate();}

		bool isSelected() { return d_selected;}
		void SetSelected(bool bSelect);

		void SetHaveSelectedState(bool bHaveSelected) { d_haveSelectedState = bHaveSelected;}

		void SetBackGroundImage(const Image* image);
		void SetBackGroundImage(String Imageset, String ImageName);
		bool IsHaveBackGroundImage() { return d_backimage != NULL;}
		const Image* GetBackIamge() const { return d_backimage; } 

		//add by liugeng
		void SetCornerImage(const Image* image);
		void SetCornerImage(String Imageset, String ImageName);
		const Image* GetCornerImage() const { return d_tlCornerImg; }
		
		void Clear();

		void SetSkillID(int id);
		int  GetSkillID() const;

		void SetSkillDueDate(int64_t time) { d_skillDueDate = time;}
		int64_t  GetSkillDueDate() const { return d_skillDueDate;}

		void SetSourceBox(const String& pbox);
		String GetSourceBox() const;

		void SetCanPickedInBattle(){d_pickupinbattle = true;}
		bool IsCanPickedInBattle(){return d_pickupinbattle;}

		void SetBackGroupOnTop(bool bln){ d_blnBackGroupOnTop = bln;}
		bool GetBackGroupOnTop(){ return d_blnBackGroupOnTop;}

	public:
		SkillBox(const String& type);
		const SkillBox& operator=(const SkillBox& t);
		virtual Window* clone(Window* wnd);
		virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

	private:
        void addSkillBoxProperties(void);
		bool d_isMouseOn;    //mouse是否是在这个格子上
		bool d_BackGroundEnable;     //格子背景是否显示
		bool d_ashy;				 //格子是否為灰色
		bool d_selected;				 //格子是否為选中状态
		bool d_haveSelectedState;    //格子是否可以被选中
		const Image* d_image;		//格子上的图片
		const Image* d_backimage;		//格子上的背景图
		const Image* d_tlCornerImg;		//左上角的标志图
		String	 sourceBoxName;
		int d_skillID;
		int64_t d_skillDueDate;//技能的到期时间
		bool d_blnBackGroupOnTop;
		void onMouseEnters(MouseEventArgs& e);
		void onMouseLeaves(MouseEventArgs& e);
		//void onMouseButtonUp(MouseEventArgs& e);
		void onMouseButtonDown(MouseEventArgs& e);

		SkillBoxTextUnit m_TextUnits[SkillBoxTextType_NUM];

		bool d_pickupinbattle;		//战斗内能否被拾取

    };

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	
