/***********************************************************************
	filename: 	CEGUIItemTable.h
*************************************************************************/

#ifndef _CEGUIItemCell_h_
#define _CEGUIItemCell_h_

#include "../CEGUIWindow.h"
#include "../CEGUIWindowFactory.h"
#include "CEGUIItemCellProperties.h"


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


#define STANDARD_ITEMCELL_SIZE 94.0f

// Start of CEGUI namespace section
namespace CEGUI
{
    /*!
    \brief
        Generic drag & drop enabled window class
    */

	enum ItemCellTextType         //cell一共有
	{
		ItemCellTextType_LeftTop,  //文字在左上角
		ItemCellTextType_RightBottom,//文字在右下角
		ItemCellTextType_Center,
		ItemCellTextType_NUM
		
	};

	enum ItemCellStyle	//by liugeng
	{
		ItemCellStyle_IconInside,
		ItemCellStyle_IconExtend	//used for pet, assist hero, etc. the ear or hair will beyond the bound of itemcell
	};

	struct ItemCellTextUnit 
	{
		String Text;
		ItemCellTextType type;
		colour color;
		int offect;

		ItemCellTextUnit():
		Text(""),
		type(ItemCellTextType_RightBottom),
		color(0xFFFFFFFF),
		offect(0)
		{
		}
	};

	const colour DefaultItemCellTextColor=colour(1.0f,1.0f,1.0f,1.0);
	
    
	class CEGUIEXPORT ItemCell : public Window
    {
    public:
       
        static const String WidgetTypeName;     //!< Type name for DragContainer.
        static const String EventNamespace;     //!< Namespace for global events
		static const String EventCellClick;    //!< click events
        static const String EventCellLongPressBegan;   //!< long press events;
        static const String EventCellLongPressMove;   //!< long press events;
        static const String EventCellLongPressEnded;   //!< long press events;
		static const String EventLockCellClick;    //!< click events
        static const String EventCellDoubleClick;
		static const String EventCellMaskClick;

        ItemCell(const String& type, const String& name);
        virtual ~ItemCell(void);

		bool testClassName_impl(const String& class_name) const
		{
			if (class_name==(const utf8*)"ItemCell")
				return true;

			return Window::testClassName_impl(class_name);
		}
		size_t GetIndex() { return d_index; }
		void SetIndex(size_t index)  {  d_index=index; }
		void SetImage( const Image* image);
		const Image* GetImage() const { return d_image; } 
		void SetImagePosOffsetScale(float scale) { d_iconPosOffsetScale = scale; }	//by liugeng.按比例设置页边距
		float GetImagePosOffsetScale() { return d_iconPosOffsetScale; }
		void SetImagePosOffset(int offset) { d_iconPosOffset = offset; }	//by liugeng.设置图标的边距
		int GetImagePosOffset() { return d_iconPosOffset; }

		void SetImage(String ImagesetName, String ImageName);

		void SetMaskImage(int id,const Image* image);
		const Image* GetMaskImage(int id) const { if (id >= 0 && id < 5) return d_maskimage[id]; return NULL; }
		void SetMaskImage(int id,String Imageset, String ImageName);
		void ClearMaskImage(int id);
		void ClearAllMaskImage();

		bool isMouseOnThisCell() { return d_isMouseOnThisCell; }
		void setMouseOnThisCell(bool bMouseOn);// { d_isMouseOnThisCell = bMouseOn;}

		void SetTextUnit(CEGUI::String text, ItemCellTextType type = ItemCellTextType_RightBottom, CEGUI::colour color = DefaultItemCellTextColor, int yoffect = 0);
		void SetTextUnitText(CEGUI::String text, ItemCellTextType type = ItemCellTextType_RightBottom, int yoffect = 0);
		void SetTextUnitColor(CEGUI::colour color, ItemCellTextType type = ItemCellTextType_RightBottom, int yoffect = 0);
		ItemCellTextUnit* GetTextUnit(ItemCellTextType type=ItemCellTextType_RightBottom);
		CEGUI::Point GetTextUnitDrawPos(const Rect& rect,ItemCellTextType type=ItemCellTextType_RightBottom); //获得

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
		void OnSelectedChanged();

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

		void SetCornerImageAtPos(const Image* image, int pos, float scale);
		void SetCornerImageAtPos(String Imageset, String ImageName, int pos, float scale, float xoffset=0.0f, float yoffset=0.0f); //四个角的图标，0左上，1右上，2右下，3左下
		const Image* GetCornerImageAtPos(int pos) { if (pos >= 0 && pos < 4) return d_cornerimages[pos]; return NULL; }
		float GetCornerImageScaleAtPos(int pos) { if (pos >= 0 && pos < 4) return d_cornerimageScales[pos]; return 1.0f; }
		CEGUI::Point GetConerImageOffsetAtPos(int pos) { if (pos >= 0 && pos < 4) return d_cornerimageOffset[pos]; return CEGUI::Point(0.0f, 0.0f); };
		void ClearCornerImage(int pos);
		void Clear();

		bool HasFloodLight(){ return d_HasFloodLight; } //是否有流光
		void SetFloodLight(bool bHasFloodLight) { d_HasFloodLight=bHasFloodLight; }

		bool isImageAutoScale() { return d_ImageAutoScale; }
		void SetImageAutoScale(bool bAutoScale);
        
        bool isBtnVisible() { return d_BtnVisible;}
        void SetBtnVisible(bool visible) { d_BtnVisible = visible; }

		bool isEnableMaskClick(){ return d_enableMaskClickEvent;  }
		void SetEnableMaskClick(bool enable){ d_enableMaskClickEvent = enable;  }

		void SetStyle(int style) { d_style = (ItemCellStyle)style; }
		int GetStyle()const { return d_style; }
		void SetBackUpImage(const Image* image){ d_backimage2 = image; };
		void SetBackUpImage(String Imageset, String ImageName);
		void ClearBackUpImage();
		const Image* GetBackUpImage(){ return d_backimage2; }
	public:
		ItemCell(const String& type);
		const ItemCell& operator=(const ItemCell& t);
		virtual Window* clone(Window* wnd);
		virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

	private:
        void addItemCellProperties(void);
		void deselectOtherCellInTable(void); //将table上的其他格子的选中状态置为false
		size_t d_index;              //格子的位置标志第几行第几列就决定了index
		bool d_isMouseOnThisCell;    //mouse是否是在这个格子上
		bool d_BackGroundEnable;     //格子背景是否显示
		bool d_ashy;				 //格子是否為灰色
		bool d_selected;				 //格子是否為选中状态
		bool d_haveSelectedState;    //格子是否可以被选中
		bool d_lock;				//格子是否被锁定
		bool d_enableMaskClickEvent;//有效掩码点击事件
		const Image* d_image;		//格子上的图片
		const Image* d_backimage;		//格子上的背景图
		const Image* d_colorimage;		//装备的颜色图
		const Image* d_cornerimage;		//左下角的标志图
		const Image* d_maskimage[5];    //遮罩图 
		const Image* d_cornerimages[4]; //四个角的图标，0左上，1右上，2右下，3左下
		float		 d_cornerimageScales[4];//by liugeng.四个角的图标的缩放比例
		CEGUI::Point d_cornerimageOffset[4];//角标的坐标偏移，朝向格子中心的为正值，反之为负值
		float	d_iconPosOffsetScale;	//by liugeng.按比例设置页边距
		int		d_iconPosOffset;		//by liugeng.图标离边缘的距离
		bool m_bIsTreasure;

		const Image* d_backimage2;      //添加一种在背景之上其他图片之下的图
		
		virtual void onMouseEnters(MouseEventArgs& e);
		virtual void onMouseLeaves(MouseEventArgs& e);
		virtual void onMouseButtonDown(MouseEventArgs& e);
        virtual bool onLongPress(Gesture::CEGUIGestureRecognizer* pRecognizer);
		virtual void onMouseClicked(MouseEventArgs& e);
        virtual void onMouseDoubleClicked(MouseEventArgs& e);
        
		ItemCellTextUnit m_TextUnits[ItemCellTextType_NUM];
		int d_CellTypeMask;    //格子类型的判断掩码
		bool d_HasFloodLight;  //是否有流光效果
		bool d_ImageAutoScale; //图片是否自动缩放
        bool d_BtnVisible;
		//void onMouseButtonUp(MouseEventArgs& e);
		//void onMouseButtonDown(MouseEventArgs& e);

		ItemCellStyle d_style;

		static ItemCellProperties::ItemCellStyle d_ItemCellStyleProperty;
    };

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	
