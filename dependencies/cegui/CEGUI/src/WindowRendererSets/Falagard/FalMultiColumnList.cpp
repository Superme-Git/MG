/***********************************************************************
    filename:   FalMultiColumnList.cpp
    created:    Wed Jul 6 2005
    author:     Paul D Turner <paul@cegui.org.uk>
*************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2006 Paul D Turner & The CEGUI Development Team
 *
 *   Permission is hereby granted, free of charge, to any person obtaining
 *   a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including
 *   without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to
 *   permit persons to whom the Software is furnished to do so, subject to
 *   the following conditions:
 *
 *   The above copyright notice and this permission notice shall be
 *   included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *   OTHER DEALINGS IN THE SOFTWARE.
 ***************************************************************************/
#include "FalMultiColumnList.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUIWindowManager.h"
#include "elements/CEGUIScrollbar.h"
#include "elements/CEGUIListHeader.h"
#include "elements/CEGUIListboxItem.h"

#include "CEGUIImage.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIGeometryBuffer.h"
#include "CEGUIPropertyHelper.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardMultiColumnList::TypeName[] = "Falagard/MultiColumnList";


    FalagardMultiColumnList::FalagardMultiColumnList(const String& type) :
        MultiColumnListWindowRenderer(type),
		d_SelectBrushLeft(NULL),
		d_SelectBrushMiddle(NULL),
		d_SelectBrushRight(NULL)
    {
		d_SelectBrushLeft = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg_dwn_l");
		d_SelectBrushMiddle = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg_dwn_c");
		d_SelectBrushRight = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg_dwn_r");
        
		d_NormalBrushLeft[0] = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg_l");
		d_NormalBrushMiddle[0] = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg_c");
		d_NormalBrushRight[0] = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg_r");

		d_NormalBrushLeft[1] = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg2_l");
		d_NormalBrushMiddle[1] = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg2_c");
		d_NormalBrushRight[1] = ImagesetManager::getSingleton().getImage("common", "common_list_3textbg2_r");

		m_bNeedTwoImage = true;
    }

    Rect FalagardMultiColumnList::getListRenderArea(void) const
    {
        MultiColumnList* w = (MultiColumnList*)d_window;
        // get WidgetLookFeel for the assigned look.
        const WidgetLookFeel& wlf = getLookNFeel();
        bool v_visible = w->getVertScrollbar()->isVisible(true);
        bool h_visible = w->getHorzScrollbar()->isVisible(true);

        // if either of the scrollbars are visible, we might want to use another item rendering area
        if (v_visible || h_visible)
        {
            String area_name("ItemRenderingArea");

            if (h_visible)
            {
                area_name += "H";
            }
            if (v_visible)
            {
                area_name += "V";
            }
            area_name += "Scroll";

            if (wlf.isNamedAreaDefined(area_name))
            {
                return wlf.getNamedArea(area_name).getArea().getPixelRect(*w);
            }
        }

        // default to plain ItemRenderingArea
        return wlf.getNamedArea("ItemRenderingArea").getArea().getPixelRect(*w);
    }

    void FalagardMultiColumnList::render()
    {
        MultiColumnList* w = (MultiColumnList*)d_window;
        const ListHeader* header = w->getListHeader();
        const Scrollbar* vertScrollbar = w->getVertScrollbar();
        const Scrollbar* horzScrollbar = w->getHorzScrollbar();

        // render general stuff before we handle the items
        cacheListboxBaseImagery();

        //
        // Render list items
        //
        Vector3 itemPos;
        Size    itemSize;
        Rect    itemClipper, itemRect;

        // calculate position of area we have to render into
        Rect itemsArea(getListRenderArea());

        // set up initial positional details for items
        itemPos.d_y = itemsArea.d_top - vertScrollbar->getScrollPosition();
        itemPos.d_z = 0.0f;

        float alpha = w->getEffectiveAlpha();

        // loop through the items
		bool bDrawSelect=false;
//		bool bDrawMouseOn=false;
        for (uint i = 0; i < w->getRowCount(); ++i)
        {
            // set initial x position for this row.
            itemPos.d_x = itemsArea.d_left - horzScrollbar->getScrollPosition();

            // calculate height for this row.
			const PropertyInitialiser* const propinit = getLookNFeel().findPropertyInitialiser("ListItemHeight");
			itemSize.d_height = PropertyHelper::stringToFloat(propinit->getInitialiserValue());

            // loop through the columns in this row
            bool bIsSelectLine=false;
            for (uint k = 0; k < w->getColumnCount(); ++k)
            {
                ListboxItem* pItem = w->getItemAtGridReference(MCLGridRef(i,k));
                
                // is the item for this column set?
                if (pItem&&pItem->isSelected())
                {
                    bIsSelectLine=true;
                    break;
                }
                
            }
            
            if (!bIsSelectLine)
            {
                Rect LineTargetRect(itemsArea);
                LineTargetRect.d_top=itemPos.d_y;
                LineTargetRect.d_bottom=LineTargetRect.d_top+ itemSize.d_height;
                LineTargetRect.d_left+=1.0f;
                LineTargetRect.d_right-=1.0f;
                
                ColourRect color(0xFFFFFFFF);
                color.setAlpha(alpha);
                drawSelectBrush(w->getGeometryBuffer(),false,LineTargetRect,&itemsArea,color,i);
            }
           

            for (uint j = 0; j < w->getColumnCount(); ++j)
            {
                // allow item to use full width of the column
                itemSize.d_width = header->getColumnWidth(j).asAbsolute(header->getPixelSize().d_width);

                ListboxItem* item = w->getItemAtGridReference(MCLGridRef(i,j));

				// is the item for this column set?
				if (item)
				{
					// calculate destination area for this item.
					itemRect.d_left = itemPos.d_x;
					itemRect.d_top  = itemPos.d_y;
					itemRect.setSize(itemSize);
					itemClipper = itemRect.getIntersection(itemsArea);

					// skip this item if totally clipped
					if (itemClipper.getWidth() == 0)
					{
						itemPos.d_x += itemSize.d_width;
						continue;
					}
					
//					bool bDrawFlage=false;
					if (item->isSelected()&&!bDrawSelect)
					{
//						bDrawFlage=true;
						Rect TargetRect(itemsArea);
						TargetRect.d_top=itemRect.d_top;
						TargetRect.d_bottom=TargetRect.d_top + itemSize.d_height;
						TargetRect.d_left+=1.0f;
						TargetRect.d_right-=1.0f;
						drawSelectBrush(w->getGeometryBuffer(),true,TargetRect,&itemsArea,item->getModulateAlphaColourRect(ColourRect(0xFFFFFFFF), alpha),j);
						bDrawSelect=true;
					}
					// draw this item
					item->draw(w->getGeometryBuffer(), itemRect, alpha, &itemClipper,false);
				}
                // update position for next column.
                itemPos.d_x += itemSize.d_width;
            }

            // update position ready for next row
            itemPos.d_y += itemSize.d_height;
        }
    }

    void FalagardMultiColumnList::cacheListboxBaseImagery()
    {
        const StateImagery* imagery;

        // get WidgetLookFeel for the assigned look.
        const WidgetLookFeel& wlf = getLookNFeel();
        // try and get imagery for our current state
        imagery = &wlf.getStateImagery(d_window->isDisabled() ? "Disabled" : "Enabled");
        // peform the rendering operation.
        imagery->render(*d_window);
    }

	void FalagardMultiColumnList::drawSelectBrush(GeometryBuffer* buffer,bool bSelect, const Rect& targetRect, const Rect* clipper, const ColourRect& colorRect, int index) const
	{
		if (m_bNeedTwoImage)
		{
			index = index % 2;
		}
		else
		{
			index = 0;
		}

		const Image* pLeftImage=d_NormalBrushLeft[index];
        const Image* pMiddleImage=d_NormalBrushMiddle[index];
        const Image* pRightImage=d_NormalBrushRight[index];
        if (bSelect)
        {
            pLeftImage=d_SelectBrushLeft;
            pMiddleImage=d_SelectBrushMiddle;
            pRightImage=d_SelectBrushRight;
            
        }
        
        if (pLeftImage&&pMiddleImage&&pRightImage)
		{
			float leftWidth=pLeftImage->getWidth();
			float RightWidth=pRightImage->getWidth();
			float middleDrawWidth=targetRect.getWidth()-leftWidth-RightWidth - 4;

			Rect LeftDrawRect(targetRect);
			LeftDrawRect.d_right=LeftDrawRect.d_left+leftWidth;
			LeftDrawRect.d_left = LeftDrawRect.d_left;
			pLeftImage->draw(buffer,LeftDrawRect,clipper,colorRect);

			Rect MiddleDrawRect(targetRect);
			MiddleDrawRect.d_left=LeftDrawRect.d_right;
			MiddleDrawRect.d_right=MiddleDrawRect.d_left+middleDrawWidth;
			pMiddleImage->draw(buffer,MiddleDrawRect,clipper,colorRect);

			Rect RightDrawRect(targetRect);
			RightDrawRect.d_left = targetRect.d_right - RightWidth - 4;
			RightDrawRect.d_right = targetRect.d_right - 4;
			pRightImage->draw(buffer,RightDrawRect,clipper,colorRect);
		}

	}


} // End of  CEGUI namespace section
