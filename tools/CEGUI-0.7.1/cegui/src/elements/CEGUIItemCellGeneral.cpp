/***********************************************************************
	filename: 	CEGUIDragContainer.cpp
	created:	14/2/2005
	author:		Paul D Turner
*************************************************************************/

#include "elements/CEGUIItemCellGeneral.h"
#include "CEGUIImageset.h"
#include "CEGUICoordConverter.h"
#include "CEGUIRenderingContext.h"
#include "CEGUIFont.h"
#include "elements/CEGUIItemTable.h"
#include "CEGUIPropertyHelper.h"
#include <math.h>

// Start of CEGUI namespace section
namespace CEGUI
{
    //////////////////////////////////////////////////////////////////////////
    // Window type string
    const String ItemCellGeneral::WidgetTypeName("CEGUI/ItemCellGeneral");
    // Event Strings
    const String ItemCellGeneral::EventNamespace("ItemCellGeneral");
    //
	const String ItemCellGeneral::EventCellClick( "TableClick" );
	const String ItemCellGeneral::EventLockCellClick( "LockCellClick");

    //////////////////////////////////////////////////////////////////////////

    ItemCellGeneral::ItemCellGeneral(const String& type, const String& name) :
        Window(type, name),
	    d_index(-1),
		d_image(NULL),
		d_backimage(NULL),
		d_colorimage(NULL),
		d_isMouseOnThisCell(false),
		d_BackGroundEnable(true),
		d_ashy(false),
		d_lock(false),
		d_selected(false),
		d_haveSelectedState(false),
		d_CellTypeMask(0),
		d_HasFloodLight(false),
		d_ImageAutoScale(false),
		d_cornerimage(NULL),
		d_generalID(0),
		d_skillDueDate(0),
		d_pickupinbattle(false),
		sourceBoxName(L"")
    {
		SetDragMoveEnable(false);
		addItemCellGeneralProperties();
		//d_wantsMultiClicks = false;
#ifdef FORCEGUIEDITOR
		//setProperty("UnifiedMaxSize", PropertyHelper::uvector2ToString(UVector2(UDim(0.f, static_cast<float>(ItemCellGeneralSize)),
		//	UDim(0.f,static_cast<float>(ItemCellGeneralSize)))));
		//setProperty("UnifiedMinSize",PropertyHelper::uvector2ToString(UVector2(UDim(0.f,static_cast<float>(ItemCellGeneralSize)),
		//	UDim(0.f,static_cast<float>(ItemCellGeneralSize)))));
#else
		setProperty("UnifiedMaxSize", PropertyHelper::uvector2ToString(UVector2(UDim(0.f, static_cast<float>(ItemCellGeneralSize)),
			UDim(0.f, static_cast<float>(ItemCellGeneralSize)))));
		setProperty("UnifiedMinSize", PropertyHelper::uvector2ToString(UVector2(UDim(0.f, static_cast<float>(ItemCellGeneralSize)),
			UDim(0.f, static_cast<float>(ItemCellGeneralSize)))));
#endif
		
    }

	ItemCellGeneral::ItemCellGeneral(const String& type) :
		Window(type)
	{
	}
	const ItemCellGeneral& ItemCellGeneral::operator=(const ItemCellGeneral& t)
	{
		Window::operator=(t);
		d_index = t.d_index;
		d_isMouseOnThisCell = t.d_isMouseOnThisCell;
		d_BackGroundEnable = t.d_BackGroundEnable;
		d_ashy = t.d_ashy;
		d_selected = t.d_selected;
		d_haveSelectedState = t.d_haveSelectedState;
		d_lock = t.d_lock;
		d_image = t.d_image;
		d_backimage = t.d_backimage;
		d_colorimage = t.d_colorimage;
		d_cornerimage = t.d_cornerimage;
		for (int i = 0; i <ItemCellGeneralTextType_NUM; i++)
		{
			m_TextUnits[i] = t.m_TextUnits[i];
		}
		d_CellTypeMask = t.d_CellTypeMask;
		d_HasFloodLight = t.d_HasFloodLight;
		d_ImageAutoScale = t.d_ImageAutoScale;
		sourceBoxName = t.sourceBoxName;
		d_generalID = t.d_generalID;
		d_keyID = t.d_keyID;
		d_skillDueDate = t.d_skillDueDate;
		d_pickupinbattle = t.d_pickupinbattle;
		d_generalType = t.d_generalType;
		return *this;
	}
	Window* ItemCellGeneral::clone(Window* wnd)
	{
		ItemCellGeneral* retWnd = (ItemCellGeneral*)wnd;
		if (retWnd == NULL)
			retWnd = new ItemCellGeneral(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == ItemCellGeneral::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

    ItemCellGeneral::~ItemCellGeneral(void)
    {
    }

	void ItemCellGeneral::Clear()
	{
		/*setUserData(NULL);
		SetAshy(false);
		SetSelected(false);
		SetImage(NULL);
		SetTextUnitText("");
		SetTextUnitText("",ItemCellGeneralTextType_LeftTop);
		setID(0);
		d_cornerimage = NULL;*/
		setUserData(NULL);
		SetImage(NULL);
		SetTextUnitText("");
		SetTextUnitText("",ItemCellGeneralTextType_LeftTop);
		setTooltipText("");
		d_generalID = 0;
		d_skillDueDate = 0;
		sourceBoxName = L"";
	}

	void ItemCellGeneral::SetBackGroundEnable(bool bEnable)
	{
		if (bEnable!=d_BackGroundEnable)
		{
			d_BackGroundEnable=bEnable;
			invalidate(false);
		}
		
	}

	void ItemCellGeneral::addItemCellGeneralProperties(void)
	{

	}

	void ItemCellGeneral::SetImage( const Image* image) 
	{ 
		if (d_image!=image)
		{
			d_image=image;
			invalidate(false);
		}
		
	}

	void ItemCellGeneral::SetImage(String Imageset, String ImageName)
	{
		const CEGUI::Image* pImage=ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetImage(pImage);
		}
	}

	void ItemCellGeneral::SetAshy(bool bAshy)
	{
		if (bAshy != d_ashy)
		{
			d_ashy = bAshy;
			invalidate();
		}
	}

	void ItemCellGeneral::SetLockState(bool block)
	{
		if (block != d_lock)
		{
			d_lock = block;
			if(d_lock)
			{
				//如果格子被锁定，设置锁定的图片
				//start by wjf 2/17/2016 --delete image
				//SetBackGroundImage("BaseControl","ItemLock");
				SetBackGroundImage("common", "common_biaoshi_cc");
				//end by wjf 2/17/2016 --delete image
			}
			else
			{
				SetBackGroundImage(0);
			}
			invalidate();
		}
	}

	void ItemCellGeneral::SetSelected(bool select)
	{
		//if (select != d_selected)
		//{
		//	d_selected = select;
		//	invalidate();
		//	// if new state is 'selected', we must de-select any selected cell within our table.
		//	if (d_selected)
		//	{
		//		deselectOtherCellInTable();
		//	}
		//}
		if (select != d_selected)
		{
			d_selected = select;
			invalidate();
		}
	}

	void ItemCellGeneral::SetMutiSelected(bool select)
	{
		if (select != d_selected)
		{
			d_selected = select;
			invalidate();
		}
	}

	void ItemCellGeneral::deselectOtherCellInTable()
	{
		ItemTable* pTable=dynamic_cast<ItemTable*>(getParent());
		if (pTable && !pTable->GetMulitySelect())
		{
			size_t child_count = d_parent->getChildCount();

			for (size_t child = 0; child < child_count; ++child)
			{			
				ItemCellGeneral* cell = (ItemCellGeneral*)pTable->GetCell(child);

				if (cell !=NULL && cell->isSelected() && (cell != this))
				{
					cell->SetSelected(false);
					break;
				}
			}
		}
	}

	void ItemCellGeneral::setMouseOnThisCell(bool bMouseOn)
	{
		if(d_isMouseOnThisCell != bMouseOn)
		{
			d_isMouseOnThisCell = bMouseOn;
			invalidate();
		}
	}

	void ItemCellGeneral::onMouseEnters(MouseEventArgs& e)
	{
		if(d_isMouseOnThisCell == true)
			return;

		Window::onMouseEnters(e);
		d_isMouseOnThisCell=true;
		invalidate(false);
	}

	void ItemCellGeneral::onMouseLeaves(MouseEventArgs& e)
	{
		if(d_isMouseOnThisCell == false)
			return;
	
		Window::onMouseLeaves(e);
		d_isMouseOnThisCell=false;
		invalidate(false);
	}

	void ItemCellGeneral::onMouseButtonDown(MouseEventArgs& e)
	{
		////锁定状态下，不响应点击事件
		//if(d_lock)
		//{
		//	fireEvent(EventLockCellClick, e, EventNamespace);
		//	++e.handled;
		//	return;
		//}
		//	

		//if(d_haveSelectedState && getUserData())
		//{
		//	d_selected = true;
		//}
		//invalidate();
		//deselectOtherCellInTable();
		//
		//ItemTable* pTable=dynamic_cast<ItemTable*>(getParent()); // 把staitc_cast 改了，用了一个不用ItemTable包装的ItemCellGeneral
		//if (NULL!=pTable)
		//{
		//	MouseEventArgs newArgs(this);
		//	newArgs.button=e.button;
		//	newArgs.position=e.position;
		//	pTable->onTableClick(newArgs);
		//	++e.handled;
		//}else{
		//	fireEvent(EventCellClick, e, EventNamespace);
		//	++e.handled;
		//}
		//Window::CheckGuideEnd(e.button);

		CheckGuideEnd(e.button);
		if(d_haveSelectedState)
		{
			SetSelected(true);
		}
		fireEvent(EventCellClick, e, EventNamespace);
		++e.handled;

		
		
	}

	ItemCellGeneralTextUnit* ItemCellGeneral::GetTextUnit(ItemCellGeneralTextType type)
	{
		if (type>=0&&type<ItemCellGeneralTextType_NUM)
		{
			return &m_TextUnits[type];
		}

		return NULL;
	}

	void ItemCellGeneral::SetTextUnit(CEGUI::String text,ItemCellGeneralTextType type/* =ItemCellGeneralTextType_RightBottom */,CEGUI::colour color)
	{
		ItemCellGeneralTextUnit* pUnit=GetTextUnit(type);
		pUnit->type=type;
		if (NULL!=pUnit)
		{
			bool isNeedRedraw=false;
			if (text!=pUnit->Text)
			{
				pUnit->Text=text;
				isNeedRedraw=true;
			}
			if (color!=pUnit->color)
			{
				pUnit->color=color;
				isNeedRedraw=true;
			}
			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}
	}

	void ItemCellGeneral::SetTextUnitText(CEGUI::String text,ItemCellGeneralTextType type)
	{
		ItemCellGeneralTextUnit* pUnit=GetTextUnit(type);
		if (NULL!=pUnit)
		{
			bool isNeedRedraw=false;
			if (text!=pUnit->Text)
			{
				pUnit->Text=text;
				isNeedRedraw=true;
			}
			
			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}

	}
	void ItemCellGeneral::SetTextUnitColor(CEGUI::colour color,ItemCellGeneralTextType type)
	{
		ItemCellGeneralTextUnit* pUnit=GetTextUnit(type);
		if (NULL!=pUnit)
		{
			bool isNeedRedraw=false;
			if (color!=pUnit->color)
			{
				pUnit->color=color;
				isNeedRedraw=true;
			}

			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}
	}
	
	CEGUI::Point ItemCellGeneral:: GetTextUnitDrawPos(const Rect& rect,ItemCellGeneralTextType type)
	{
		Point offset(0.0f,0.0f);
		ItemCellGeneralTextUnit* pUnit=GetTextUnit(type);
		Font* pFont=getFont(true);
		
		float TextWide=pFont->getTextExtent(pUnit->Text);
		float TextHight=pFont->getFontHeight();
		
		if (NULL!=pUnit)
		{
			switch (type)
			{
			case ItemCellGeneralTextType_RightBottom:
				{
					offset.d_x=rect.d_right-TextWide-4.0f;
					offset.d_y=rect.d_bottom-TextHight-4.0f;
				}
				break;
			case ItemCellGeneralTextType_LeftTop:
				{
					offset.d_x=rect.d_left+4.0f;
					offset.d_y=rect.d_top+4.0f;
				}
				break;
			case ItemCellGeneralTextType_LeftBottom:
				{
					offset.d_x=rect.d_left+4.0f;
					offset.d_y=rect.d_bottom-TextHight-4.0f;
				}
				break;
			case ItemCellGeneralTextType_Center:
				{
					offset.d_x = rect.d_left  + TextWide / 2;
					offset.d_y = rect.d_bottom  + TextHight / 2;
				}
				break;
			}
			
			
		}
		
		return offset;
		
	}

	void ItemCellGeneral::SetBackGroundImage(const CEGUI::Image *image)
	{
		if (d_backimage!=image)
		{
			d_backimage=image;
			invalidate(false);
		}
	}

	void ItemCellGeneral::SetBackGroundImage(CEGUI::String Imageset, CEGUI::String ImageName)
	{
		const CEGUI::Image* pImage = ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetBackGroundImage(pImage);
		}
	}

	//<<==============================
	//将skillbox类方法部分合入此类并采用通用名称
	void ItemCellGeneral::SetGeneralID(int id)
	{
		d_generalID = id;
	}

	int ItemCellGeneral::GetGeneralID() const
	{
		return d_generalID;
	}

	void ItemCellGeneral::SetSourceBox(const String& pbox)
	{
		sourceBoxName = pbox;
	}

	String ItemCellGeneral::GetSourceBox() const
	{
		return sourceBoxName;
	}
	//>>==============================

	void ItemCellGeneral::SetEquipColorImage(const CEGUI::Image *image)
	{
		if (d_colorimage!=image)
		{
			d_colorimage=image;
			invalidate(false);
		}
	}

	void ItemCellGeneral::SetEquipColorImage(CEGUI::String Imageset, CEGUI::String ImageName)
	{
		if(ImageName.empty())
			return;

		const CEGUI::Image* pImage = ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetEquipColorImage(pImage);
		}
	}

	void ItemCellGeneral::SetImageAutoScale(bool bAutoScale)
	{
		if (d_ImageAutoScale!=bAutoScale)
		{
			d_ImageAutoScale=bAutoScale;
			invalidate();
		}

	}

	void ItemCellGeneral::SetCornerImage(const CEGUI::Image *image)
	{
		if (d_cornerimage!=image)
		{
			d_cornerimage=image;
			invalidate(false);
		}
	}

	void ItemCellGeneral::SetCornerImage(CEGUI::String Imageset, CEGUI::String ImageName)
	{
		if(ImageName.empty())
			return;

		const CEGUI::Image* pImage=ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetCornerImage(pImage);
		}
	}

	/*void ItemCellGeneral::onMouseButtonUp(MouseEventArgs& e)
	{
		Window::onMouseButtonUp(e);
		if (e.button==LeftButton)
		{
			const Image* pCusorImage=MouseCursor::getSingleton().getImage();
			MouseCursor::getSingleton().setImage(d_image);
			SetImage(pCusorImage);
		}
		
	}*/

	/*void ItemCellGeneral::onMouseButtonDown(MouseEventArgs& e)
	{
		Window::onMouseButtonDown(e);
		if (e.button==LeftButton)
		{
				const Image* pCusorImage=MouseCursor::getSingleton().getImage();
				MouseCursor::getSingleton().setImage(d_image);
			

		}

	}*/


  
//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
