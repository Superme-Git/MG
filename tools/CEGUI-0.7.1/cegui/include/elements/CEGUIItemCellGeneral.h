/***********************************************************************
	filename: 	CEGUIItemCellGeneralGeneral.h
*************************************************************************/

#ifndef _CEGUIItemCellGeneralGeneral_h_
#define _CEGUIItemCellGeneralGeneral_h_

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

	enum ItemCellGeneralTextType         //cell一共有
	{
		ItemCellGeneralTextType_LeftTop,  //文字在左上角
		ItemCellGeneralTextType_LeftBottom,  //文字在左下角
		ItemCellGeneralTextType_RightBottom,//文字在右下角
		ItemCellGeneralTextType_Center,//居中 by wangjianfeng
		ItemCellGeneralTextType_NUM
	};

	struct ItemCellGeneralTextUnit 
	{
		String Text;
		ItemCellGeneralTextType type;
		colour color;

		ItemCellGeneralTextUnit():
		Text(""),
		type(ItemCellGeneralTextType_RightBottom),
		color(0xFFFFFFFF)
		{
		}
	};

	const colour DefaultItemCellGeneralTextColor=colour(1.0f,1.0f,1.0f,1.0);
	
    
	class CEGUIEXPORT ItemCellGeneral : public Window
    {
    public:
		//定义通用类型枚举
		enum GeneralType
		{
			GT_NONE = 0,
			GT_ITEM = 1,
			GT_SKILL = 2,
			GT_NUM = 3
		};
       
        static const String WidgetTypeName;     //!< Type name for DragContainer.
        static const String EventNamespace;     //!< Namespace for global events
		static const String EventCellClick;    //!< click events
		static const String EventLockCellClick;    //!< click events
		static const int ItemCellGeneralSize = 77;	//skillbox部分合入

        ItemCellGeneral(const String& type, const String& name);
        virtual ~ItemCellGeneral(void);

		bool testClassName_impl(const String& class_name) const
		{
			if (class_name==(const utf8*)"ItemCellGeneral")
				return true;

			return Window::testClassName_impl(class_name);
		}
		size_t GetIndex() { return d_index; }
		void SetIndex(size_t index)  {  d_index=index; }
		void SetImage( const Image* image);
		const Image* GetImage() const { return d_image; } 

		void SetImage(String Imageset, String ImageName);

		bool isMouseOnThisCell() { return d_isMouseOnThisCell; }
		void setMouseOnThisCell(bool bMouseOn);// { d_isMouseOnThisCell = bMouseOn;}

		void SetTextUnit(CEGUI::String text,ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom,CEGUI::colour color=DefaultItemCellGeneralTextColor );
		void SetTextUnitText(CEGUI::String text,ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom);
		void SetTextUnitColor(CEGUI::colour color,ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom);
		ItemCellGeneralTextUnit* GetTextUnit(ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom);
		CEGUI::Point GetTextUnitDrawPos(const Rect& rect,ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom); //获得

		bool isBackGroundEnable()  { return d_BackGroundEnable; }
		void SetBackGroundEnable(bool bEnable);
		int GetCellTypeMask() { return d_CellTypeMask; }
		void SetCellTypeMask(int mask) { d_CellTypeMask=mask; }

		bool IsAshy()  { return d_ashy;}
		void SetAshy(bool bAshy);// { d_ashy = bAshy; invalidate();}

		bool IsLock()  { return d_lock;}
		void SetLockState(bool block);// { d_ashy = bAshy; invalidate();}

		bool isSelected() { return d_selected;}
		void SetSelected(bool bSelect);
		void SetMutiSelected(bool bSelect);

		void SetHaveSelectedState(bool bHaveSelected) { d_haveSelectedState = bHaveSelected;}

		void SetBackGroundImage(const Image* image);
		void SetBackGroundImage(String Imageset, String ImageName);
		bool IsHaveBackGroundImage() { return d_backimage != NULL;}
		const Image* GetBackIamge() const { return d_backimage; } 
		
		void SetEquipColorImage(const Image* image);
		void SetEquipColorImage(String Imageset, String ImageName);
		const Image* GetColorImage() const { return d_colorimage; } 

		void SetCornerImage(const Image* image);
		void SetCornerImage(String Imageset, String ImageName);
		const Image* GetCornerImage() const { return d_cornerimage; } 

		void Clear();

		bool HasFloodLight(){ return d_HasFloodLight; } //是否有流光
		void SetFloodLight(bool bHasFloodLight) { d_HasFloodLight=bHasFloodLight; }

		bool isImageAutoScale() { return d_ImageAutoScale; }
		void SetImageAutoScale(bool bAutoScale);


		//<<==============================================
		//将skillbox类方法部分合入此类
		void SetGeneralID(int id);
		int  GetGeneralID() const;

		void SetSkillDueDate(int64_t time) { d_skillDueDate = time;}
		int64_t  GetSkillDueDate() const { return d_skillDueDate;}

		void SetSourceBox(const String& pbox);
		String GetSourceBox() const;

		void SetCanPickedInBattle(){d_pickupinbattle = true;}
		bool IsCanPickedInBattle(){return d_pickupinbattle;}
		//>>================================================

		void SetGeneralType(GeneralType type){d_generalType = type;}
		GeneralType GetGeneralType(){return d_generalType;}

		void SetKeyID(int keyID){d_keyID = keyID;}
		int GetKeyID(){return d_keyID;}

	public:
		ItemCellGeneral(const String& type);
		const ItemCellGeneral& operator=(const ItemCellGeneral& t);
		virtual Window* clone(Window* wnd);
		virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

	private:
        void addItemCellGeneralProperties(void);
		void deselectOtherCellInTable(void); //将table上的其他格子的选中状态置为false
		size_t d_index;              //格子的位置标志第几行第几列就决定了index
		bool d_isMouseOnThisCell;    //mouse是否是在这个格子上
		bool d_BackGroundEnable;     //格子背景是否显示
		bool d_ashy;				 //格子是否為灰色
		bool d_selected;				 //格子是否為选中状态
		bool d_haveSelectedState;    //格子是否可以被选中
		bool d_lock;				//格子是否被锁定
		const Image* d_image;		//格子上的图片
		const Image* d_backimage;		//格子上的背景图
		const Image* d_colorimage;		//装备的颜色图
		const Image* d_cornerimage;		//左下角的标志图
		
		void onMouseEnters(MouseEventArgs& e);
		void onMouseLeaves(MouseEventArgs& e);
		void onMouseButtonDown(MouseEventArgs& e);

		ItemCellGeneralTextUnit m_TextUnits[ItemCellGeneralTextType_NUM];
		int d_CellTypeMask;    //格子类型的判断掩码
		bool d_HasFloodLight;  //是否有流光效果
		bool d_ImageAutoScale; //图片是否自动缩放

		//void onMouseButtonUp(MouseEventArgs& e);
		//void onMouseButtonDown(MouseEventArgs& e);

		//<<==============================================
		//将skillbox类成员部分合入此类
		String	 sourceBoxName;
		int d_generalID;
		int d_keyID;				//道具keyid
		int64_t d_skillDueDate;		//技能的到期时间
		bool d_pickupinbattle;		//战斗内能否被拾取
		//>>================================================

		GeneralType d_generalType;	//类型说明(供tips显示用)
    };

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	
