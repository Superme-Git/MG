/***********************************************************************
	filename: 	CEGUIDragContainer.cpp
	created:	14/2/2005
	author:		Paul D Turner
*************************************************************************/

#include "elements/CEGUIItemTable.h"
#include "elements/CEGUIItemCell.h"
#include "CEGUIImageset.h"
#include "CEGUICoordConverter.h"
#include "CEGUIRenderingContext.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIWindowManager.h"
#include "gesture/CEGUILongPressGestureRecognizer.h"
#include <math.h>
#include "CEGUIGeometryBuffer.h"
#include "elements/CEGUIScrollablePane.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    //////////////////////////////////////////////////////////////////////////
    // Window type string
    const String ItemTable::WidgetTypeName("CEGUI/ItemTable");
    // Event Strings
    const String ItemTable::EventNamespace("ItemTable");

	const String ItemTable::EventTableClick( "TableClick" );
//    const String ItemTable::EventTableLongPressBegan("TableLongPressBegan");
//    const String ItemTable::EventTableLongPressMove("TableLongPressMove");
//    const String ItemTable::EventTableLongPressEnd("TableLongPressEnd");
    const String ItemTable::EventTableDoubleClick("TableDoubleClick");

	ItemTableProperties::ColCount        ItemTable::d_ColCountProperty;
	ItemTableProperties::RowCount        ItemTable::d_RowCountProperty;
	ItemTableProperties::CellWidth		 ItemTable::d_CellWidthProperty;
	ItemTableProperties::CellHeight		 ItemTable::d_CellHeightProperty;
	ItemTableProperties::SpaceX			 ItemTable::d_SpaceXProperty;
	ItemTableProperties::SpaceY			 ItemTable::d_SpaceYProperty;
	ItemTableProperties::StartX			 ItemTable::d_StartXProperty;
	ItemTableProperties::StartY			 ItemTable::d_StartYProperty;
	ItemTableProperties::ItemCellStyle	 ItemTable::d_ItemCellStyleProperty;

    //////////////////////////////////////////////////////////////////////////

    ItemTable::ItemTable(const String& type, const String& name) :
        Window(type, name),
		d_CellHeight(77.0f),
		d_CellWide(77.0f),
		d_ColCount(0),
		d_RowCount(0),
		d_TableType(0),
		d_SpaceX(0),
		d_SpaceY(0),
		d_StartX(0),
		d_StartY(0),
		b_MulitySelect(false),
		d_ItemCellStyle(ItemCellStyle_IconInside)
      
    {
		d_GridWidth = d_CellWide + d_SpaceX;
		d_GridHeight = d_CellHeight + d_SpaceY;
		SetDragMoveEnable(false);
		addItemTableProperties();
		d_ListCells.clear();
		d_ListCells.resize(0);
    }

	ItemTable::ItemTable(const String& type) :
		Window(type)
	{
	}

	bool ItemTable::onRenameTemplatePrefix(const String& sPrefix)
	{
		if (false == Window::onRenameTemplatePrefix(sPrefix))
			return false;
		
		ItemCellList tempList = d_ListCells;
		d_ListCells.clear();

		for (size_t i = 0; i < tempList.size(); ++i)
		{
			ItemCell* itemCell = dynamic_cast<ItemCell*>(getCloneWindowFromTemplate(tempList[i], ""));
			d_ListCells.push_back(itemCell);
		}

		return true;
	}

	const ItemTable& ItemTable::operator=(const ItemTable& t)
	{
		Window::operator=(t);
		d_ListCells = t.d_ListCells;
		d_RowCount = t.d_RowCount;
		d_ColCount = t.d_ColCount;
		d_CellWide = t.d_CellWide;
		d_CellHeight = t.d_CellHeight;
		d_GridWidth = t.d_GridWidth;
		d_GridHeight = t.d_GridHeight;
		d_SpaceX = t.d_SpaceX;
		d_SpaceY = t.d_SpaceY;
		d_StartX = t.d_StartX;
		d_StartY = t.d_StartY;
		d_TableType = t.d_TableType;
		b_MulitySelect = t.b_MulitySelect;
		d_ItemCellStyle = t.d_ItemCellStyle;
		return *this;
	}

	Window* ItemTable::clone(Window* wnd)
	{
		ItemTable* retWnd = (ItemTable*)wnd;
		if (retWnd == NULL)
			retWnd = new ItemTable(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == ItemTable::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

    ItemTable::~ItemTable(void)
    {
		//DestroyAllCell();
    }

	void ItemTable::doTableRender()
	{


	}

	void ItemTable::SetType(int type)
	{
		d_TableType = type;
		ItemCellListIterator it=d_ListCells.begin();
		for (;it!=d_ListCells.end();it++)
		{
			(*it)->SetCellTypeMask(type);
		}

	}

	void ItemTable::SetItemCellStyle(int style)
	{
		d_ItemCellStyle = (ItemCellStyle)style;
		ItemCellListIterator it = d_ListCells.begin();
		for (; it != d_ListCells.end(); it++)
		{
			(*it)->SetStyle(style);
		}
	}

	void ItemTable::SetRowCount(size_t RowCount)  
	{ 
		if (RowCount!=d_RowCount)
		{
			d_RowCount=RowCount; 
			initialiseComponents(true);
			invalidate(true);
		}
	}
	void ItemTable::SetColCount(size_t ColCount)  
	{ 
		if (ColCount!=d_ColCount)
		{
			d_ColCount=ColCount; 
			initialiseComponents(true);
			invalidate(true);
		}
	}

	
	void ItemTable::initialiseComponents(bool bClone)
	 {
		DestroyAllCell();
		if (d_ColCount > 0 && d_RowCount > 0)
		{
			for (size_t row = 0; row < d_RowCount; row++)
			{
				for (size_t col = 0; col < d_ColCount; col++)
				{
					AddCell(row*d_ColCount + col);
				}
			}
		}
	 }

	ItemCell* ItemTable::GetCell(size_t index) 
	{ 
		if (d_ListCells.size()==0)
		{
			return NULL;
		}
		
		if (index>d_ListCells.size())
		{
			return NULL;
		}

		ItemCellListIterator it=d_ListCells.begin();
		for (;it!=d_ListCells.end();it++)
		{
			if ((*it)->GetIndex()==index)
			{
				return *it;
				break;
			}

		}

		return NULL;
	}

	ItemCell* ItemTable::GetCellAtPoint(CEGUI::Point pt)
	{
		return static_cast<ItemCell*>(getChildAtPosition(pt));
	}

	void ItemTable::RefreshCellPos(bool bFilterHideCell)
	{
		int j = 0;
		for (size_t i = 0; i < d_ListCells.size(); i++)
		{
			ItemCell* pCell = d_ListCells[i];
			if ((bFilterHideCell&&pCell->isVisible()) || !bFilterHideCell)
			{
				// ycl 计算 Cell 位置的代码统一放在 SetCellPosByIndex 函数里
				SetCellPosByIndex(pCell, j);
				j++;
			}
		}
	}

	// ycl 刷新所有 Cell 的位置和尺寸
	void ItemTable::RefreshCellPosAndSize()
	{
		for (size_t index = 0; index < d_ListCells.size(); ++index)
		{
			ItemCell* pCell = d_ListCells[index];
			if (pCell)
			{
				pCell->setSize(UVector2(UDim(0.0f, d_CellWide), UDim(0.0f, d_CellHeight)));
				SetCellPosByIndex(pCell, index);
			}
		}
	}

	// ycl 计算并返回一个 Cell 的位置
	void ItemTable::CalCellPosByIndex(size_t index, float* pX, float* pY) const
	{
		if (pX)
		{
			float xOffset = (float)(index%d_ColCount)*d_GridWidth;
			if (xOffset > 1)
			{
				xOffset -= index%d_ColCount;
			}

			*pX = xOffset;
		}

		if (pY)
		{
			float yOffset = (float)(index / d_ColCount)*d_GridHeight;

			if (yOffset > 1)
			{
				yOffset -= index / d_ColCount;
			}

			*pY = yOffset;
		}
	}

	// ycl 设置 Cell 的位置
	void ItemTable::SetCellPosByIndex(ItemCell* pCell, size_t index)
	{
		float xOffset, yOffset;
		CalCellPosByIndex(index, &xOffset, &yOffset);
		
		pCell->setXPosition(UDim(0.0, d_StartX + xOffset));
		pCell->setYPosition(UDim(0.0, d_StartY + yOffset));
	}
	
	ItemCell* ItemTable::AddCell(size_t index)
	{
		if (NULL != GetCell(index))
		{
			DestroyCell(index);
		}

		String name(getName());
		name += "_ItemCell_";
		name += PropertyHelper::intToString((int)index);
		
		ItemCell* pCell = static_cast<ItemCell*>(WindowManager::getSingleton().createWindow("TaharezLook/ItemCell", name));
		if (NULL != pCell)
		{
			pCell->SetStyle(d_ItemCellStyle);
			pCell->SetIndex(index);

			pCell->setSize(UVector2(UDim(0.0f, d_CellWide), UDim(0.0f, d_CellHeight)));
			// ycl 计算 Cell 位置的代码统一放在 SetCellPosByIndex 函数里
			SetCellPosByIndex(pCell, index);
			addChildWindow(pCell);
			d_ListCells.push_back(pCell);

			//        pCell->EnableLongPress_imp(true, Event::Subscriber(&ItemTable::onTableLongPress, this));
		}

		return pCell;
	}

	void ItemTable::DestroyCell(size_t index)
	{
		ItemCell* pCell=GetCell(index);
		if (NULL==pCell)
		{
			return;
		}
		
		WindowManager::getSingleton().destroyWindow(pCell);
		ItemCellListIterator it=d_ListCells.begin();
		for (;it!=d_ListCells.end();it++)
		{
			if (*it==pCell)
			{
				d_ListCells.erase(it);
				break;
			}
			
		}
		delete pCell;
	}

	void ItemTable::DestroyAllCell()
	{
		ItemCellListIterator it=d_ListCells.begin();
		for (;it!=d_ListCells.end();it++)
		{
			ItemCell* pCell=(*it);
			if (NULL==pCell)
			{
				continue;
			}

			WindowManager::getSingleton().destroyWindow(pCell);
			//delete pCell;
			//DestroyCell((*it)->GetIndex());
			//d_ListCells.erase(it);
		}
		d_ListCells.clear();
		d_ListCells.resize(0);
	}

	void ItemTable::addItemTableProperties()
	{
		addProperty(&d_RowCountProperty);
		addProperty(&d_ColCountProperty);
		addProperty(&d_CellWidthProperty);
		addProperty(&d_CellHeightProperty);
		addProperty(&d_SpaceXProperty);
		addProperty(&d_SpaceYProperty);
		addProperty(&d_StartXProperty);
		addProperty(&d_StartYProperty);
		addProperty(&d_ItemCellStyleProperty);
	}

	void ItemTable::onTableClick(CEGUI::MouseEventArgs &e)
	{
		fireEvent(EventTableClick, e, EventNamespace);
	}
    
    void ItemTable::onTableDoubleClick(CEGUI::MouseEventArgs& e) {
        fireEvent(EventTableDoubleClick, e);
    }

	ItemCell* ItemTable::GetFirstSelectCell()
	{
		ItemCellListIterator it=d_ListCells.begin();
		for (;it!=d_ListCells.end();it++)
		{
			if ((*it)->isSelected())
			{
				return *it;
				break;
			}
		}
		return NULL;
	}

	void ItemTable::SetCellWidth(int width)
	{
		// ycl 只在 Cell 尺寸发生变化时刷新所有 Cell 的位置和尺寸
		if (d_CellWide != width)
		{
			d_CellWide = width;
			d_GridWidth = d_CellWide + d_SpaceX;
			RefreshCellPosAndSize();
			invalidate(true);
		}
	}

	void ItemTable::SetCellHeight(int height)
	{
		// ycl 只在 Cell 尺寸发生变化时刷新所有 Cell 的位置和尺寸
		if (d_CellHeight != height)
		{
			d_CellHeight = height;
			d_GridHeight = d_CellHeight + d_SpaceY;
			RefreshCellPosAndSize();
			invalidate(true);
		}
	}

	void ItemTable::SetSpaceX(int x)
	{
		// ycl 只在 d_SpaceX 发生变化时刷新所有 Cell 的位置和尺寸
		if (d_SpaceX != x)
		{
			d_SpaceX = x;
			d_GridWidth = d_CellWide + d_SpaceX;
			RefreshCellPosAndSize();
			invalidate(true);
		}
	}

	void ItemTable::SetSpaceY(int y)
	{
		// ycl 只在 d_SpaceY 发生变化时刷新所有 Cell 的位置和尺寸
		if (d_SpaceY != y)
		{
			d_SpaceY = y;
			d_GridHeight = d_CellHeight + d_SpaceY;
			RefreshCellPosAndSize();
			invalidate(true);
		}
	}

	void ItemTable::SetStartX(int x)
	{
		// ycl 只在 d_StartX 发生变化时刷新所有 Cell 的位置和尺寸
		if (d_StartX != x)
		{
			d_StartX = x;
			RefreshCellPosAndSize();
			invalidate(true);
		}
	}
	void ItemTable::SetStartY(int y)
	{
		// ycl 只在 d_StartY 发生变化时刷新所有 Cell 的位置和尺寸
		if (d_StartY != y)
		{
			d_StartY = y;
			RefreshCellPosAndSize();
			invalidate(true);
		}
	}

  
//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
