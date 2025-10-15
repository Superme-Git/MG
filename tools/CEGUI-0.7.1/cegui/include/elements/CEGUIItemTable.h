/***********************************************************************
	filename: 	CEGUIItemTable.h
*************************************************************************/

#ifndef _CEGUIItemTable_h_
#define _CEGUIItemTable_h_

#include "../CEGUIWindow.h"
#include "../CEGUIWindowFactory.h"
#include "CEGUIItemTableProperties.h"
#include "elements/CEGUIItemCell.h"
#include "gesture/CEGUIGestureRecognizer.h"

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
	class CEGUIEXPORT ItemTable : public Window
	{
		friend class ItemCell;
	public:

		static const String WidgetTypeName;     //!< Type name for DragContainer.
		static const String EventNamespace;     //!< Namespace for global events
		static const String EventTableClick;    //!< click events
		//		static const String EventTableLongPressBegan;
		//        static const String EventTableLongPressMove;
		//        static const String EventTableLongPressEnd;
		static const String EventTableDoubleClick;
		typedef	std::vector<ItemCell*>	ItemCellList;
		typedef ItemCellList::iterator ItemCellListIterator;

		ItemTable(const String& type, const String& name);
		virtual ~ItemTable(void);
		void doTableRender();
		void initialiseComponents(bool bClone);
		ItemCell* AddCell(size_t index);
		void DestroyCell(size_t index);
		size_t GetCellCount() { return d_ListCells.size(); }
		ItemCell* GetCell(size_t index);
		ItemCell* GetCellAtPoint(CEGUI::Point pt);
		void SetRowCount(size_t RowCount);
		void SetColCount(size_t ColCount);
		void DestroyAllCell();
		void RefreshCellPos(bool bFilterHideCell);
		void RefreshCellPosAndSize();  // ycl 刷新所有 Cell 的位置和尺寸
		void CalCellPosByIndex(size_t index, float* pX, float* pY) const;  // 计算并返回一个 Cell 的位置
		void SetCellPosByIndex(ItemCell* pCell, size_t index);  // ycl  设置一个 Cell 的位置

		ItemCell* GetFirstSelectCell();
		ItemCellList& GetAllCell() { return d_ListCells; }
		size_t GetRowCount()const  { return d_RowCount; }
		size_t GetColCount()const  { return d_ColCount; }

		void SetCellWidth(int width);
		void SetCellHeight(int height);
		void SetSpaceX(int x);
		void SetSpaceY(int y);
		void SetStartX(int x);
		void SetStartY(int y);


		int GetCellWidth()const { return d_CellWide; }
		int GetCellHeight()const { return d_CellHeight; }
		int GetSpaceX()const { return d_SpaceX; }
		int GetSpaceY()const { return d_SpaceY; }
		int GetStartX()const { return d_StartX; }
		int GetStartY()const { return d_StartY; }

		void	SetType(int type);// { d_TableType = type;}
		int		GetType() { return d_TableType;}

		void SetItemCellStyle(int style);
		int GetItemCellStyle()const { return d_ItemCellStyle; }

		bool testClassName_impl(const String& class_name) const
		{
			if (class_name==(const utf8*)"ItemTable")
				return true;
			return Window::testClassName_impl(class_name);
		}

		void onTableClick(MouseEventArgs& e);

	public:
		ItemTable(const String& type);
		const ItemTable& operator=(const ItemTable& t);
		virtual Window* clone(Window* wnd);
		virtual bool onRenameTemplatePrefix(const String& sPrefix);
    private:
        bool onTableLongPress(const EventArgs& e);
    public:
//        bool onTableLongPressBegan(const EventArgs& e);
//        bool onTableLongPressMove(const EventArgs& e);
//        void onTableLongPressEnd(const EventArgs& e);
        void onTableDoubleClick(MouseEventArgs& e);
		virtual int writeChildWindowsXML(XMLSerializer& xml_stream)const
		{
			return 0;
		}


		void SetMulitySelect(bool b)
		{
			if (b != b_MulitySelect)
				b_MulitySelect = b;
		}

		bool GetMulitySelect()
		{
			return b_MulitySelect;
		}

		void DeSelectAll()
		{
			size_t child_count = getChildCount();

			for (size_t child = 0; child < child_count; ++child)
			{			
				ItemCell* cell = (ItemCell*)GetCell(child);

				if (cell !=NULL && cell->isSelected() )
				{
					cell->SetSelected(false);
				}
			}
		}
	private:
        void addItemTableProperties(void);
		ItemCellList d_ListCells;
		size_t d_RowCount;   //有多少行
		size_t d_ColCount;   //有多少列
		int d_CellWide;   //格子的宽
		int d_CellHeight;  //格子的高
		int d_GridWidth;
		int d_GridHeight;
		int d_SpaceX;	//格子间的间隔
		int d_SpaceY;
		int d_StartX;
		int d_StartY;
		ItemCellStyle d_ItemCellStyle;
		int	   d_TableType;

		bool   b_MulitySelect;


		static ItemTableProperties::ColCount d_ColCountProperty;
		static ItemTableProperties::RowCount d_RowCountProperty;
		static ItemTableProperties::CellWidth d_CellWidthProperty;
		static ItemTableProperties::CellHeight d_CellHeightProperty;
		static ItemTableProperties::SpaceX d_SpaceXProperty;
		static ItemTableProperties::SpaceY d_SpaceYProperty;
		static ItemTableProperties::StartX d_StartXProperty;
		static ItemTableProperties::StartY d_StartYProperty;
		static ItemTableProperties::ItemCellStyle d_ItemCellStyleProperty;
    };

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	
