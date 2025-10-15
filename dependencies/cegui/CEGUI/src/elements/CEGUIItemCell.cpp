/***********************************************************************
	filename: 	CEGUIDragContainer.cpp
	created:	14/2/2005
	author:		Paul D Turner
*************************************************************************/

#include "elements/CEGUIItemCell.h"
#include "CEGUIImageset.h"
#include "CEGUICoordConverter.h"
#include "CEGUIRenderingContext.h"
#include "CEGUIFont.h"
#include "elements/CEGUIItemTable.h"
#include <math.h>

// Start of CEGUI namespace section
namespace CEGUI
{
    //////////////////////////////////////////////////////////////////////////
    // Window type string
    const String ItemCell::WidgetTypeName("CEGUI/ItemCell");
    // Event Strings
    const String ItemCell::EventNamespace("ItemCell");
    //
	const String ItemCell::EventCellClick( "TableClick" );
    const String ItemCell::EventCellLongPressBegan("LongPressBegan");
    const String ItemCell::EventCellLongPressMove("LongPressMove");
    const String ItemCell::EventCellLongPressEnded("LongPressEnd");
	const String ItemCell::EventLockCellClick( "LockCellClick");
    const String ItemCell::EventCellDoubleClick( "TableDoubleClick");
	const String ItemCell::EventCellMaskClick("MaskClick");

	ItemCellProperties::ItemCellStyle	ItemCell::d_ItemCellStyleProperty;

	//////////////////////////////////////////////////////////////////////////

    ItemCell::ItemCell(const String& type, const String& name) :
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
		d_ImageAutoScale(true),
		d_cornerimage(NULL),
		d_BtnVisible(true),
		d_iconPosOffset(0),
		d_iconPosOffsetScale(0.0957f), //mt3 need
		d_enableMaskClickEvent(false),
		m_bIsTreasure(false),
		d_backimage2(NULL),
		d_style(ItemCellStyle_IconInside)
    {
		SetDragMoveEnable(false);
		addItemCellProperties();
		setFont("simhei-12");
		d_wantsMultiClicks = true;
		for (int i = 0; i < 4; i++)
		{
			
			d_cornerimages[i] = NULL;
			d_cornerimageScales[i] = 1.0f;
			d_cornerimageOffset[i] = CEGUI::Point(0.0f, 0.0f);
		}
		for (int i = 0; i<5; ++i)
		{
			d_maskimage[i] = NULL;
		}

    }

	ItemCell::ItemCell(const String& type) :
		Window(type)
	{
	}
	const ItemCell& ItemCell::operator=(const ItemCell& t)
	{
		Window::operator=(t);
		d_index = t.d_index;
		d_isMouseOnThisCell = t.d_isMouseOnThisCell;
		d_BackGroundEnable = t.d_BackGroundEnable;
		d_ashy = t.d_ashy;
		d_selected = t.d_selected;
		d_haveSelectedState = t.d_haveSelectedState;
		d_lock = t.d_lock;
		d_enableMaskClickEvent = t.d_enableMaskClickEvent;
		d_image = t.d_image;
		d_backimage = t.d_backimage;
		d_colorimage = t.d_colorimage;
		d_cornerimage = t.d_cornerimage;
		for (int i = 0; i < 5; i++)
		{
			d_maskimage[i] = t.d_maskimage[i];
		}
		for (int i = 0; i < 4; i++)
		{
			d_cornerimages[i] = t.d_cornerimages[i];
			d_cornerimageScales[i] = t.d_cornerimageScales[i];
			d_cornerimageOffset[i] = t.d_cornerimageOffset[i];
		}
		d_iconPosOffsetScale = t.d_iconPosOffsetScale;
		d_iconPosOffset = t.d_iconPosOffset;
		m_bIsTreasure = t.m_bIsTreasure;
		for (int i = 0; i < ItemCellTextType_NUM; i++)
		{
			m_TextUnits[i] = t.m_TextUnits[i];
		}
		d_CellTypeMask = t.d_CellTypeMask;
		d_HasFloodLight = t.d_HasFloodLight;
		d_ImageAutoScale = t.d_ImageAutoScale;
		d_BtnVisible = t.d_BtnVisible;
		d_style = t.d_style;
		d_backimage2 = t.d_backimage2;
		setFont("simhei-12");
		return *this;
	}
	Window* ItemCell::clone(Window* wnd)
	{
		ItemCell* retWnd = (ItemCell*)wnd;
		if (retWnd == NULL)
			retWnd = new ItemCell(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == ItemCell::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

    ItemCell::~ItemCell(void)
    {
    }

	void ItemCell::Clear()
	{
		setUserData(NULL);
		SetAshy(false);
		SetSelected(false);
		SetImage(NULL);
		SetTextUnitText("");
		SetTextUnitText("",ItemCellTextType_LeftTop);
		setID(0);
		SetBackGroundImage(NULL);
		d_cornerimage = NULL;
		d_backimage2 = NULL;
		for (int i = 0; i < 4; i++)
		{
			d_cornerimages[i] = NULL;
			d_cornerimageScales[i] = 1.0f;
			d_cornerimageOffset[i] = CEGUI::Point(0.0f, 0.0f);
		}
		for (int i = 0; i < 5; i++)
		{
			d_maskimage[i] = NULL;
		}
	}

	void ItemCell::SetBackGroundEnable(bool bEnable)
	{
		if (bEnable!=d_BackGroundEnable)
		{
			d_BackGroundEnable=bEnable;
			invalidate(false);
		}
		
	}

	void ItemCell::addItemCellProperties(void)
	{
		addProperty(&d_ItemCellStyleProperty);
	}

	void ItemCell::SetImage( const Image* image) 
	{ 
		if (d_image!=image)
		{
			d_image=image;
			invalidate(false);
		}
		
	}

	void ItemCell::SetImage(CEGUI::String ImagesetName, CEGUI::String ImageName)
	{
		const CEGUI::Image* pImage=ImagesetManager::getSingleton().getImage(ImagesetName, ImageName);
		if (pImage)
		{
			SetImage(pImage);
		}
	}

	void ItemCell::SetMaskImage(int id, const Image* image)
	{
		if (id >= 0 && id < 5)
		{
			if (d_maskimage[id] != image)
			{
				d_maskimage[id] = image;
				invalidate(false);
			}
		}
	}

	void ItemCell::SetMaskImage(int id,CEGUI::String ImagesetName, CEGUI::String ImageName)
	{
		const CEGUI::Image* pImage = ImagesetManager::getSingleton().getImage(ImagesetName, ImageName);
		if (pImage)
		{
			SetMaskImage(id,pImage);
		}
	}

	void ItemCell::ClearMaskImage(int id)
	{
		if (id >= 0 && id < 5)
		{
			d_maskimage[id] = NULL;
		}
	}

	void ItemCell::ClearAllMaskImage()
	{
		for (int i = 0; i < 5; i++)
		{
			d_maskimage[i] = NULL;
		}
	}

	void ItemCell::SetAshy(bool bAshy)
	{
		if (bAshy != d_ashy)
		{
			d_ashy = bAshy;
			invalidate();
		}
	}

	void ItemCell::SetLockState(bool block)
	{
		if (block != d_lock)
		{
			d_lock = block;
			if(d_lock)
			{
				//如果格子被锁定，设置锁定的图片
				SetBackGroundImage("skillui","lock2");
			}
			else
			{
				SetBackGroundImage(0);
			}
			invalidate();
		}
	}

	void ItemCell::OnSelectedChanged()
	{
		AddSelectEffectToItemCell* func = System::getSingleton().GetAddSelectEffectToItemCell();
		if (func)
		{
			func(this, d_selected);
		}
	}

	void ItemCell::SetSelected(bool select)
	{
		if (select != d_selected)
		{
			d_selected = select;
			OnSelectedChanged();
			invalidate();
			// if new state is 'selected', we must de-select any selected cell within our table.
			if (d_selected)
			{
				deselectOtherCellInTable();
			}
		}
	}

	void ItemCell::SetMutiSelected(bool select)
	{
		if (select != d_selected)
		{
			d_selected = select;
			OnSelectedChanged();
			invalidate();
		}
	}

	void ItemCell::deselectOtherCellInTable()
	{
		ItemTable* pTable=dynamic_cast<ItemTable*>(getParent());
		if (pTable && !pTable->GetMulitySelect())
		{
			size_t child_count = d_parent->getChildCount();

			for (size_t child = 0; child < child_count; ++child)
			{			
				ItemCell* cell = (ItemCell*)pTable->GetCell(child);

				if (cell !=NULL && cell->isSelected() && (cell != this))
				{
					cell->SetSelected(false);
					break;
				}
			}
		}
	}

	void ItemCell::setMouseOnThisCell(bool bMouseOn)
	{
		if(d_isMouseOnThisCell != bMouseOn)
		{
			d_isMouseOnThisCell = bMouseOn;
			invalidate();
		}
	}

	void ItemCell::onMouseEnters(MouseEventArgs& e)
	{
		if(d_isMouseOnThisCell == true)
			return;
        
		Window::onMouseEnters(e);
		d_isMouseOnThisCell=true;
		invalidate(false);
	}

	void ItemCell::onMouseLeaves(MouseEventArgs& e)
	{
		if(d_isMouseOnThisCell == false)
			return;
	
		Window::onMouseLeaves(e);
		d_isMouseOnThisCell=false;
		invalidate(false);
	}
    
    bool ItemCell::onLongPress(Gesture::CEGUIGestureRecognizer* pRecognizer)
    {
        Gesture::GestureRecognizerState state = pRecognizer->GetState();
        MouseEventArgs* e = (MouseEventArgs*)pRecognizer->GetEvent();
		if (!e) return false;

        if (state == Gesture::GestureRecognizerStateBegan) {
            if(d_lock)
            {
                fireEvent(EventLockCellClick, *e, EventNamespace);
                ++e->handled;
                return true;
            }
            
            
            if(d_haveSelectedState && !d_selected && getUserData())
            {
                d_selected = true;
				OnSelectedChanged();
            }
            invalidate();
            deselectOtherCellInTable();
        }
        if (state == Gesture::GestureRecognizerStateBegan)
            fireEvent(EventCellLongPressBegan, *e, EventNamespace);
        else if (state == Gesture::GestureRecognizerStateChanged)
            fireEvent(EventCellLongPressMove, *e, EventNamespace);
        else if (state == Gesture::GestureRecognizerStateEnded)
            fireEvent(EventCellLongPressEnded, *e, EventNamespace);
        ItemTable* pTable=dynamic_cast<ItemTable*>(getParent());
        if (NULL!=pTable)
        {
//            MouseEventArgs newArgs(this);
//            newArgs.button=e.button;
//            newArgs.position=e.position;
//            newArgs.clickCount=e.clickCount;
//            pTable->onTableLongPress(newArgs);
			++e->handled;
        }else{
            
			++e->handled;
        }
        Window::CheckGuideEnd(e->button);
        return true;
    }
    
    void ItemCell::onMouseDoubleClicked(MouseEventArgs& e) {
        if (e.clickCount == 1) {
            if(d_lock)
            {
                fireEvent(EventLockCellClick, e, EventNamespace);
                ++e.handled;
                return;
            }
            
            
            if(d_haveSelectedState && !d_selected && getUserData())
            {
                d_selected = true;
				OnSelectedChanged();
            }
            invalidate();
            deselectOtherCellInTable();
        }
        ItemTable* pTable=dynamic_cast<ItemTable*>(getParent());
        if (NULL!=pTable)
        {
            MouseEventArgs newArgs(this);
            newArgs.button=e.button;
            newArgs.position=e.position;
            newArgs.clickCount=e.clickCount;
            pTable->onTableDoubleClick(newArgs);
            ++e.handled;
        }else{
            fireEvent(EventCellDoubleClick, e, EventNamespace);
            ++e.handled;
        }
        Window::CheckGuideEnd(e.button);
    }

	void ItemCell::onMouseButtonDown(MouseEventArgs& e)
	{
        Window::onMouseButtonDown(e);
		
	}
    
	void ItemCell::onMouseClicked(MouseEventArgs& e)
    {
        if(d_lock)
		{
			fireEvent(EventLockCellClick, e, EventNamespace);
			//Window::onMouseButtonDown(e);
			Window::onMouseClicked(e);
			++e.handled;
			return;
		}
        
		if(d_haveSelectedState && !d_selected)
		{
			d_selected = true;
			OnSelectedChanged();
		}
		invalidate();
		deselectOtherCellInTable();
		
		ItemTable* pTable=dynamic_cast<ItemTable*>(getParent()); // 把staitc_cast 改了，用了一个不用ItemTable包装的ItemCell
		if (NULL!=pTable)
		{
			MouseEventArgs newArgs(this);
			newArgs.button=e.button;
			newArgs.position=e.position;
			pTable->onTableClick(newArgs);
			++e.handled;
		}else
		{
			//只检查左上角
			bool bSuccess = false;
			if (GetMaskImage(0) && d_enableMaskClickEvent)
			{
				Size imageSize = GetMaskImage(0)->getSize();
				CEGUI::Point pos=e.position-GetScreenPos();
				if (pos.d_x < imageSize.d_width&&pos.d_y < imageSize.d_height)
				{
					fireEvent(EventCellMaskClick, e, EventNamespace);
					bSuccess = true;
				}
			}
			if (bSuccess == false)
			{
				fireEvent(EventCellClick, e, EventNamespace);
			}
			++e.handled;
		}
        //Window::onMouseButtonUp(e);
		Window::CheckGuideEnd(e.button);
		Window::onMouseClicked(e);
    }

	ItemCellTextUnit* ItemCell::GetTextUnit(ItemCellTextType type)
	{
		if (type>=0&&type<ItemCellTextType_NUM)
		{
			return &m_TextUnits[type];
		}

		return NULL;
	}

	void ItemCell::SetTextUnit(CEGUI::String text, ItemCellTextType type/* =ItemCellTextType_RightBottom */, CEGUI::colour color, int yoffect)
	{
		ItemCellTextUnit* pUnit=GetTextUnit(type);
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

			pUnit->offect = yoffect;

			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}
	}

	void ItemCell::SetTextUnitText(CEGUI::String text, ItemCellTextType type, int yoffect)
	{
		ItemCellTextUnit* pUnit=GetTextUnit(type);
		if (NULL!=pUnit)
		{
			pUnit->type = type;
			bool isNeedRedraw=false;
			if (text!=pUnit->Text)
			{
				pUnit->Text=text;
				isNeedRedraw=true;
			}
			
			pUnit->offect = yoffect;

			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}

	}
	void ItemCell::SetTextUnitColor(CEGUI::colour color, ItemCellTextType type, int yoffect)
	{
		ItemCellTextUnit* pUnit=GetTextUnit(type);
		if (NULL!=pUnit)
		{
			bool isNeedRedraw=false;
			if (color!=pUnit->color)
			{
				pUnit->color=color;
				isNeedRedraw=true;
			}
			pUnit->offect = yoffect;
			if (isNeedRedraw)
			{
				invalidate(false);
			}
		}
	}
	
	CEGUI::Point ItemCell:: GetTextUnitDrawPos(const Rect& rect,ItemCellTextType type)
	{
		Point offset(0.0f,0.0f);
		ItemCellTextUnit* pUnit=GetTextUnit(type);
		Font* pFont=getFont(true);
		
		float TextWide=pFont->getTextExtent(pUnit->Text);
		float TextHight=pFont->getFontHeight();
		
		if (NULL!=pUnit)
		{
			switch (type)
			{
			case ItemCellTextType_RightBottom:
				{
					offset.d_x=rect.d_right-TextWide-10.0f;
					offset.d_y=rect.d_bottom-TextHight-8.0f;
				}
				break;
			case ItemCellTextType_LeftTop:
				{
					offset.d_x=rect.d_left+4.0f;
					offset.d_y=rect.d_top+4.0f;
				}
				break;
			case ItemCellTextType_Center:
				{
					offset.d_x = (rect.d_left + rect.d_right)/2 - TextWide / 2;
					offset.d_y = rect.d_bottom - TextHight - 8.0f;
				}
				break;
			}
			
			
		}
		
		return offset;
		
	}

	void ItemCell::SetBackGroundImage(const CEGUI::Image *image)
	{
		if (d_backimage!=image)
		{
			d_backimage=image;
			invalidate(false);
		}
	}

	void ItemCell::SetBackGroundImage(CEGUI::String Imageset, CEGUI::String ImageName)
	{
		const CEGUI::Image* pImage=ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetBackGroundImage(pImage);
		}
	}

	void ItemCell::SetEquipColorImage(const CEGUI::Image *image)
	{
		if (d_colorimage!=image)
		{
			d_colorimage=image;
			invalidate(false);
		}
	}

	void ItemCell::SetEquipColorImage(CEGUI::String Imageset, CEGUI::String ImageName)
	{
		if(ImageName.empty())
			return;

		const CEGUI::Image* pImage=ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetEquipColorImage(pImage);
		}
	}

	void ItemCell::SetImageAutoScale(bool bAutoScale)
	{
		if (d_ImageAutoScale!=bAutoScale)
		{
			d_ImageAutoScale=bAutoScale;
			invalidate();
		}

	}

	void ItemCell::SetCornerImage(const CEGUI::Image *image)
	{
		if (d_cornerimage!=image)
		{
			d_cornerimage=image;
			invalidate(false);
		}
	}

	void ItemCell::SetCornerImage(CEGUI::String Imageset, CEGUI::String ImageName)
	{
		if(ImageName.empty())
			return;

		const CEGUI::Image* pImage=ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetCornerImage(pImage);
		}
	}

	void ItemCell::SetCornerImageAtPos(const Image* image, int pos, float scale)
	{
		d_cornerimages[pos] = image;
		d_cornerimageScales[pos] = scale;
		invalidate(false);
	}

	void ItemCell::SetCornerImageAtPos(String Imageset, String ImageName, int pos, float scale, float xoffset, float yoffset)
	{
		if (ImageName.empty())
			return;

		const CEGUI::Image* pImage = ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage)
		{
			SetCornerImageAtPos(pImage, pos, scale);
			d_cornerimageOffset[pos] = CEGUI::Point(xoffset, yoffset);
		}
	}
	void ItemCell::ClearCornerImage(int pos){
		d_cornerimages[pos] = NULL;
		d_cornerimageScales[pos] = 1.0f;
		d_cornerimageOffset[pos] = CEGUI::Point(0.0f, 0.0f);
		invalidate(false);
	}
	void ItemCell::ClearBackUpImage(){
		d_backimage2 = NULL;
	}
	void ItemCell::SetBackUpImage(String Imageset, String ImageName){
		if (ImageName.empty())
			return;

		const CEGUI::Image* pImage = ImagesetManager::getSingleton().getImage(Imageset, ImageName);
		if (pImage){
			SetBackUpImage(pImage);
		}
	}
	/*void ItemCell::onMouseButtonUp(MouseEventArgs& e)
	{
		Window::onMouseButtonUp(e);
		if (e.button==LeftButton)
		{
			const Image* pCusorImage=MouseCursor::getSingleton().getImage();
			MouseCursor::getSingleton().setImage(d_image);
			SetImage(pCusorImage);
		}
		
	}*/

	/*void ItemCell::onMouseButtonDown(MouseEventArgs& e)
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
