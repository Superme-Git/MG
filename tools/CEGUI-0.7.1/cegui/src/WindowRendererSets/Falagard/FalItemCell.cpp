/***********************************************************************
filename:   FalTree.cpp
created:	5-13-07
author:		Jonathan Welch (Based on Code by David Durant)
*************************************************************************/
/***************************************************************************
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
#include "FalItemCell.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUIWindowManager.h"
#include "elements/CEGUIScrollbar.h"
#include "CEGUIImage.h"
#include "CEGUIFont.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardItemCell::TypeName[] = "Falagard/ItemCell";


    FalagardItemCell::FalagardItemCell(const String& type, const String&) :
        WindowRenderer(type)
    {
    }

    FalagardItemCell::FalagardItemCell(const String& type) :
        WindowRenderer(type)
    {
    }

    FalagardItemCell::~FalagardItemCell()
    {
    }

    Rect FalagardItemCell::getItemCellRenderArea(void) const
    {
        // get WidgetLookFeel for the assigned look.
        const WidgetLookFeel& wlf = getLookNFeel();
		ItemCell* cell = (ItemCell*)d_window;

        // default to plain ItemRenderingArea
        return wlf.getNamedArea("ItemCellRenderingArea").getArea().getPixelRect(*cell);
    }

	void FalagardItemCell::render()
	{
		ItemCell* cell = (ItemCell*)d_window;
		if (NULL==cell)
		{
			return;
		}
		
		//Set the render area for this.
        Rect rect = getItemCellRenderArea();

		Rect mouseOnRect(rect);

		/*if (cell->getType()!="TaharezLook/BufferCell")
		{
			mouseOnRect.d_left -=1;
			mouseOnRect.d_top -=2;
			mouseOnRect.d_right +=1;
			mouseOnRect.d_bottom;
		}else
		{
			mouseOnRect.d_left-=3;
			mouseOnRect.d_top-=2;
			mouseOnRect.d_right+=2;
			mouseOnRect.d_bottom;

		}*/
		
        //tree->setItemRenderArea(rect);
		const WidgetLookFeel& wlf = getLookNFeel();

		const StateImagery* imagery;
		//Get the Falagard imagery to render
		String state;
		//bool bSelect = cell->isSelected();
		//bool bMouseOnCell = cell->isMouseOnThisCell();
		ColourRect colorrect(colour(0));
		if(!cell->isBackGroundEnable())
		{
			state = "BackGroundDisable";
			imagery = &wlf.getStateImagery(state);
			imagery->render(*cell);
			if(cell->GetImage())
			{
				renderCellImage(cell,rect);
			}	
		}
		
		else if(cell->GetBackIamge() != NULL)
		{
			const Image* pBackImage=cell->GetBackIamge();
            colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
            colorrect.modulateAlpha(cell->getEffectiveAlpha());
			pBackImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
			/*if (bSelect &&bMouseOnCell)
			{
				state = "SelectedMouseBackGround";
				imagery = &wlf.getStateImagery(state);
				imagery->render(*cell, mouseOnRect);
			}
			else if (bSelect)
			{
				state = "SelectedBackGround";
				imagery = &wlf.getStateImagery(state);
				imagery->render(*cell, mouseOnRect);
			}*/
			if(cell->GetImage())
			{
				renderCellImage(cell,rect);
			}
			if(cell->GetColorImage())
			{
				colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
                colorrect.modulateAlpha(cell->getEffectiveAlpha());
                cell->GetColorImage()->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect);
			}
			
			// shield by liugeng
			/*else if (bMouseOnCell)		
			{
				state = "MouseOnBackGround";
				imagery = &wlf.getStateImagery(state);
				imagery->render(*cell,mouseOnRect);
			}*/
			
		}
		else
		{
			if(cell->GetImage())
			{
				state ="ItemInCell";
			}
			else
			{
				state ="Enabled";
			}
			
			imagery = &wlf.getStateImagery(state);
			imagery->render(*cell);

			/*if (bSelect &&bMouseOnCell)
			{
				state = "SelectedMouseEnabled";
				imagery = &wlf.getStateImagery(state);
				imagery->render(*cell, mouseOnRect);
			}
			else if (bSelect)
			{
				state = "SelectedEnabled";
				imagery = &wlf.getStateImagery(state);
				imagery->render(*cell, mouseOnRect);
			}*/

			if(cell->GetImage())
			{
				renderCellImage(cell,rect);
			}
			if(cell->GetColorImage())
			{
				cell->GetColorImage()->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect);	
			}
			
			//shield by liugeng
			/*else if (bMouseOnCell)		
			{
				state = "MouseOnEnabled";
				imagery = &wlf.getStateImagery(state);
				imagery->render(*cell, mouseOnRect);
			}*/
		}

		for (int i = 0; i < ItemCellTextType_NUM; i++)
		{
			ItemCellTextUnit* pUnit = cell->GetTextUnit((ItemCellTextType)i);
			if (pUnit&&!pUnit->Text.empty())
			{
				Font* pFont = cell->getFont(true);
				Point pt = cell->GetTextUnitDrawPos(rect, pUnit->type);
				pt.d_y += pUnit->offect;
				if ((cell->isDisabled() == false && cell->IsAshy() == false) || pUnit->type == ItemCellTextType_LeftTop)
				{
					colorrect.setColours(pUnit->color);//pUnit->color;
				}
				else
				{
					colorrect.setColours(0xFF606060);
				}
				colorrect.modulateAlpha(cell->getEffectiveAlpha());
				if (pUnit->type == ItemCellTextType_Center){
					pFont->drawText(cell->getGeometryBuffer(), pUnit->Text, pt, &rect, colorrect, 0.0f, 1.0f, 1.0f, false, false);
				}
				else{
					pFont->drawText(cell->getGeometryBuffer(), pUnit->Text, pt, &rect, colorrect, 0.0f, 1.0f, 1.0f, false, true);
				}

			}

		}

		//by liugeng. 四个角的图片
		for (int i = 0; i < 4; i++)
		{
			const Image *img = cell->GetCornerImageAtPos(i);
			if (img)
			{
				float scale = cell->GetCornerImageScaleAtPos(i);
				Rect cornerRect(rect);
				Size imageSize = img->getSize();
				imageSize.d_width *= scale;
				imageSize.d_height *= scale;
				Point offset = cell->GetConerImageOffsetAtPos(i);
				switch (i)
				{
				case 0:
					cornerRect.d_left = offset.d_x;
					cornerRect.d_top = offset.d_y;
					break;
				case 1:
					cornerRect.d_left = rect.getWidth() - imageSize.d_width - offset.d_x;
					cornerRect.d_top = offset.d_y;
					break;
				case 2:
					cornerRect.d_left = rect.getWidth() - imageSize.d_width - offset.d_x;
					cornerRect.d_top = rect.getHeight() - imageSize.d_height - offset.d_y;
					break;
				case 3:
					cornerRect.d_left = offset.d_x;
					cornerRect.d_top = rect.getHeight() - imageSize.d_height - offset.d_y;
					break;
				default:
					break;
				}
				cornerRect.setSize(imageSize);
				colorrect = ColourRect(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
				colorrect.modulateAlpha(cell->getEffectiveAlpha());
				img->draw(cell->getGeometryBuffer(), cornerRect.getPosition(), cornerRect.getSize(), &cornerRect, colorrect);
			}
		}
	}

	void FalagardItemCell::renderCellImage(ItemCell* pCell, Rect& rect)
	{
		ColourRect colorrect(colour(0));

		const Image* pImage=pCell->GetImage();
		if (NULL!=pImage)
		{
			Rect imageRect(rect);
			if (!pCell->isImageAutoScale())
			{
				Size imageSize=pImage->getSize();
				float xOffset=(imageRect.getWidth()-imageSize.d_width)/2.0f;
				float yOffset=(imageRect.getHeight()-imageSize.d_height)/2.0f;
				imageRect.d_left+=xOffset;
				imageRect.d_top+=yOffset;
				imageRect.setSize(pImage->getSize());
			}

			Rect drawImgRect(imageRect);
			int offset = pCell->GetImagePosOffset();
			if (offset == 0 && pCell->GetImagePosOffsetScale() > 0)
			{
				offset = drawImgRect.getWidth()*pCell->GetImagePosOffsetScale();
				pCell->SetImagePosOffset(offset);
			}
			if (offset)
			{
				switch (pCell->GetStyle())
				{
				case ItemCellStyle_IconInside:
					{
						drawImgRect.d_left += offset;
						drawImgRect.d_right -= offset;
						drawImgRect.d_top += offset;
						drawImgRect.d_bottom -= offset;
					}
					break;
				case ItemCellStyle_IconExtend:
					{
						Size imageSize = pImage->getSize();
						if (imageSize.d_height > 100) //avoid icon size is too big
						{
							imageSize.d_width *= 100 / imageSize.d_height;
							imageSize.d_height = 100;
						}
						float scale = 1.0f;
						float h = imageRect.getHeight();
						if (h != STANDARD_ITEMCELL_SIZE)
						{
							scale = h / STANDARD_ITEMCELL_SIZE;
						}
						drawImgRect.d_bottom -= offset;
						drawImgRect.d_top = drawImgRect.d_bottom - imageSize.d_height*scale;
						drawImgRect.d_left -= (imageSize.d_width*scale - rect.getWidth())*0.5;
						drawImgRect.d_right += (imageSize.d_width*scale - rect.getWidth())*0.5;
						imageRect.d_left = std::min(drawImgRect.d_left, rect.d_left);
						imageRect.d_right = std::max(drawImgRect.d_right, rect.d_right);
						imageRect.d_top = std::min(drawImgRect.d_top, rect.d_top);
					}
					break;
				default:
					break;
				}
			}

			if (pCell->GetBackUpImage()){
				Rect backRect(rect);
				colorrect = ColourRect(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
				colorrect.modulateAlpha(pCell->getEffectiveAlpha());
				pCell->GetBackUpImage()->draw(pCell->getGeometryBuffer(), backRect.getPosition(), backRect.getSize(), &rect, colorrect);
			}

			if(pCell->isDisabled() || pCell->IsAshy())
			{
				colorrect = ColourRect(0xFF606060,0xFF606060,0xFF606060,0xFF606060);
                colorrect.modulateAlpha(pCell->getEffectiveAlpha());
				pImage->draw(pCell->getGeometryBuffer(), drawImgRect.getPosition(), drawImgRect.getSize(), &imageRect, colorrect);
			}
			else
			{
				colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
                colorrect.modulateAlpha(pCell->getEffectiveAlpha());
				pImage->draw(pCell->getGeometryBuffer(), drawImgRect.getPosition(), drawImgRect.getSize(), &imageRect, colorrect);
			}

			for (int i = 0; i < 5; i++)
			{
				if (pCell->GetMaskImage(i))
				{
					Rect maskRect(rect);
					Size imageSize = pCell->GetMaskImage(i)->getSize();
					if (i == 0)//左上角
					{
						maskRect.d_right = maskRect.d_left + imageSize.d_width;
						maskRect.d_bottom = maskRect.d_top + imageSize.d_height;
					}
					else if (i == 1)//右上角
					{
						//maskRect.d_left += imageRect.getWidth() - maskRect.getWidth();
						//maskRect.d_bottom = imageRect.d_top + maskRect.getHeight();
					}
					else if (i == 2)//右下角
					{
						//maskRect.d_left += imageRect.getWidth() - maskRect.getWidth();
						//maskRect.d_top += imageRect.getHeight() - maskRect.getHeight();
					}
					else if (i == 3)//左下角
					{
						maskRect.d_right = imageRect.d_left + imageRect.getWidth();
						maskRect.d_top += imageRect.getHeight() - maskRect.getHeight();
					}
					else if (i == 4)
					{
						maskRect.d_left = maskRect.getWidth() / 2.f- imageSize.d_width / 2.f ;
						maskRect.d_top = maskRect.getHeight() / 2.f- imageSize.d_height / 2.f ;
						maskRect.d_right = maskRect.d_left + imageSize.d_width;
						maskRect.d_bottom = maskRect.d_top + imageSize.d_height;
					}
					
					colorrect = ColourRect(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
					colorrect.modulateAlpha(pCell->getEffectiveAlpha());
					pCell->GetMaskImage(i)->draw(pCell->getGeometryBuffer(), maskRect.getPosition(), maskRect.getSize(), &rect, colorrect);
				}
			}


			//左下角的图片
			if(pCell->GetCornerImage())
			{
				const Image* pCornerImage=pCell->GetCornerImage();

				Rect cornerRect(rect);
				Size imageSize=pCornerImage->getSize();
				float yOffset=imageRect.getHeight()-imageSize.d_height;
				cornerRect.d_top+=yOffset;
				cornerRect.setSize(pCornerImage->getSize());
                colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
                colorrect.modulateAlpha(pCell->getEffectiveAlpha());
				pCornerImage->draw(pCell->getGeometryBuffer(),cornerRect.getPosition(),cornerRect.getSize(),&rect,colorrect);
			}

		}
	}
} // End of  CEGUI namespace section
