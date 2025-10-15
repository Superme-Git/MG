/***********************************************************************
filename:   FalItemCellGeneral.cpp
created:	2010-11-26
author:		Amen
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
#include "FalItemCellGeneral.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUIWindowManager.h"
#include "elements/CEGUIScrollbar.h"
#include "CEGUIImage.h"
#include "CEGUIFont.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardItemCellGeneral::TypeName[] = "Falagard/ItemCellGeneral";


    FalagardItemCellGeneral::FalagardItemCellGeneral(const String& type, const String&) :
        WindowRenderer(type)
    {
    }

    FalagardItemCellGeneral::FalagardItemCellGeneral(const String& type) :
        WindowRenderer(type)
    {
    }

    FalagardItemCellGeneral::~FalagardItemCellGeneral()
    {
    }

    Rect FalagardItemCellGeneral::getItemCellGeneralRenderArea(bool bFilled) const
    {
		// get WidgetLookFeel for the assigned look.
		const WidgetLookFeel& wlf = getLookNFeel();
		ItemCellGeneral* cell = (ItemCellGeneral*)d_window;
		// default to plain ItemRenderingArea
		if (bFilled)
			return wlf.getNamedArea("SkillBoxRenderingArea").getArea().getPixelRect(*cell);
		else
			return wlf.getNamedArea("SkillBoxEmptyRenderingArea").getArea().getPixelRect(*cell);
    }

	void FalagardItemCellGeneral::render()
	{
		ItemCellGeneral* cell = (ItemCellGeneral*)d_window;
		if (NULL==cell)
		{
			return;
		}
		
		//Set the render area for this.
        //tree->setItemRenderArea(rect);
		const WidgetLookFeel& wlf = getLookNFeel();

		const StateImagery* imagery;
		bool bMouseOn = cell->isMouseOnThisCell();
		ColourRect colorrect(colour(0));
		ColourRect colorrectBorder(colour(0));
		//Get the Falagard imagery to render
		String state;
		if (!cell->isBackGroundEnable())
		{
			Rect rect = getItemCellGeneralRenderArea(true);
			state="BackGroundDisable";	
			renderCellImage(rect);
		}
		else if (cell->GetBackIamge() != NULL)		
		{
			const Image* pImage=cell->GetImage();
			if (NULL!=pImage)
			{
				//技能快捷栏特殊处理，背景图为透明的格子，但是如果有技能，还是原来的实体格子
				state ="Enabled";
				imagery = &wlf.getStateImagery(state);
				imagery->render(*cell);
				Rect rect = getItemCellGeneralRenderArea(true);

				if(cell->isDisabled() || cell->IsAshy())
				{
					colorrect = ColourRect(0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F);
					pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
				}
				else
				{
					pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect);
				}

			}
			else
			{
				Rect rect = getItemCellGeneralRenderArea(false);

				const Image* pBackImage=cell->GetBackIamge();
				pBackImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect);
			}
		}
		else
		{
			Rect rect = getItemCellGeneralRenderArea(true);

			if (cell->isDisabled())
			{
				state ="Disabled";
			}
			else
			{
				state ="Enabled";
			}
			imagery = &wlf.getStateImagery(state);
			imagery->render(*cell);
			renderCellImage(rect);	
		}
	
		if (bMouseOn)		
		{
			state = "MouseOnBackGround";
			imagery = &wlf.getStateImagery(state);
			imagery->render(*cell);
		}

		Rect rect = getItemCellGeneralRenderArea(false);


		for (int i=0;i<ItemCellGeneralTextType_NUM;i++)
		{
			ItemCellGeneralTextUnit* pUnit=cell->GetTextUnit((ItemCellGeneralTextType)i);
			if (pUnit&&!pUnit->Text.empty())
			{
				Font* pFont=cell->getFont(true);
                Point pt=cell->GetTextUnitDrawPos(rect,pUnit->type);
// 				if(cell->isDisabled() == false && cell->IsAshy() == false)
// 				{
// 					colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);//pUnit->color;
// 				}
// 				else
//				{
				if (i == ItemCellGeneralTextType_LeftTop)	
				{
					colorrect = ColourRect(0xFF6EE6FF,0xFF6EE6FF,0xFF6EE6FF,0xFF6EE6FF);//pUnit->color;	
					colorrectBorder = ColourRect(0xFF003679,0xFF003679,0xFF003679,0xFF003679);	
					pFont->drawText(cell->getGeometryBuffer(),pUnit->Text,pt,&rect,colorrect,
						0.0f,1.0f,1.0f,false,true,colorrectBorder);
				}
				else
				{
					colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);//pUnit->color;	
					pFont->drawText(cell->getGeometryBuffer(),pUnit->Text,pt,&rect,colorrect,0.0f,1.0f,1.0f,false,true);
				}
				
//				}
				
				          				
			}
			
		}
	}

	void FalagardItemCellGeneral::renderCellImage(Rect& rect)
	{
		ItemCellGeneral* cell = (ItemCellGeneral*)d_window;
		if (NULL==cell)
		{
			return;
		}
		ColourRect colorrect(colour(0));

		const Image* pImage=cell->GetImage();
		if (NULL!=pImage)
		{
			if(cell->isDisabled() || cell->IsAshy())
			{
				colorrect = ColourRect(0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F);
				pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
			}
			else
			{
				pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect);
			}
		}
	}
} // End of  CEGUI namespace section
