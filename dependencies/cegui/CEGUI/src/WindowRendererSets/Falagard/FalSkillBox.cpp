/***********************************************************************
filename:   FalSkillBox.cpp
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
#include "FalSkillBox.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUIWindowManager.h"
#include "elements/CEGUIScrollbar.h"
#include "CEGUIImage.h"
#include "CEGUIFont.h"
#include "CEGUIGeometryBuffer.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardSkillBox::TypeName[] = "Falagard/SkillBox";


    FalagardSkillBox::FalagardSkillBox(const String& type, const String&) :
        WindowRenderer(type)
    {
    }

    FalagardSkillBox::FalagardSkillBox(const String& type) :
        WindowRenderer(type)
    {
    }

    FalagardSkillBox::~FalagardSkillBox()
    {
    }

    Rect FalagardSkillBox::getSkillBoxRenderArea(bool bFilled) const
    {
        // get WidgetLookFeel for the assigned look.
        const WidgetLookFeel& wlf = getLookNFeel();
		SkillBox* cell = (SkillBox*)d_window;
        // default to plain ItemRenderingArea
		if (bFilled)
			return wlf.getNamedArea("SkillBoxRenderingArea").getArea().getPixelRect(*cell);
		else
			return wlf.getNamedArea("SkillBoxEmptyRenderingArea").getArea().getPixelRect(*cell);

    }

	void FalagardSkillBox::render()
	{
		SkillBox* cell = (SkillBox*)d_window;
		if (NULL==cell)
		{
			return;
		}
		
		//Set the render area for this.
        //tree->setItemRenderArea(rect);
		const WidgetLookFeel& wlf = getLookNFeel();


		const StateImagery* imagery;
		bool bMouseOn = cell->isMouseOn();
		ColourRect colorrect(colour(0));
		//Get the Falagard imagery to render
		String state;
		if (!cell->isBackGroundEnable())
		{
			Rect rect = getSkillBoxRenderArea(true);
			state="BackGroundDisable";	
			renderCellImage(rect);
		}
		else if (cell->GetBackIamge() != NULL)		
		{
			const Image* pImage=cell->GetImage();
            if (cell->GetBackgroundDynamic()) {
                Rect rect = getSkillBoxRenderArea(false);
				const Image* pBackImage=cell->GetBackIamge();
				colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
                colorrect.modulateAlpha(cell->getEffectiveAlpha());
				if (!cell->GetBackGroupOnTop()){
					pBackImage->draw(cell->getGeometryBuffer(), rect.getPosition(), rect.getSize(), &rect, colorrect);
				}
                
                
                if (NULL != pImage)
                {
                    Rect rect = getSkillBoxRenderArea(true);
                    
                    
                    if(cell->isDisabled() || cell->IsAshy())
                    {
                        colorrect = ColourRect(0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F);
                        colorrect.modulateAlpha(cell->getEffectiveAlpha());
                        pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
                    }
                    else
                    {
                        colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
                        colorrect.modulateAlpha(cell->getEffectiveAlpha());
                        pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
                    }
                }
				if (cell->GetBackGroupOnTop()){
					pBackImage->draw(cell->getGeometryBuffer(), rect.getPosition(), rect.getSize(), &rect, colorrect);
				}
            }else if (NULL!=pImage)
			{
				//技能快捷栏特殊处理，背景图为透明的格子，但是如果有技能，还是原来的实体格子
				state ="Enabled";
				imagery = &wlf.getStateImagery(state);
				imagery->render(*cell);
				
				Rect rect = getSkillBoxRenderArea(true);


				if(cell->isDisabled() || cell->IsAshy())
				{
					colorrect = ColourRect(0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F);
                    colorrect.modulateAlpha(cell->getEffectiveAlpha());
					pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
				}
				else
				{
					colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
                    colorrect.modulateAlpha(cell->getEffectiveAlpha());
                    pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
				}

			}
			else
			{
				Rect rect = getSkillBoxRenderArea(false);
				const Image* pBackImage=cell->GetBackIamge();
				colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);
                colorrect.modulateAlpha(cell->getEffectiveAlpha());
                pBackImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
			}
		}
		else
		{
			Rect rect = getSkillBoxRenderArea(true);

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

		Rect rect = getSkillBoxRenderArea(false);

		for (int i = 0; i < SkillBoxTextType_NUM; i++)
		{
			SkillBoxTextUnit* pUnit = cell->GetTextUnit((SkillBoxTextType)i);
			if (pUnit&&!pUnit->Text.empty())
			{
				Font* pFont = cell->getFont(true);
				Point pt = cell->GetTextUnitDrawPos(rect, pUnit->type);
				// 				if(cell->isDisabled() == false && cell->IsAshy() == false)
				// 				{
				// 					colorrect = ColourRect(0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF);//pUnit->color;
				// 				}
				// 				else
				//				{
				colorrect = ColourRect(0xFF488298, 0xFF488298, 0xFF488298, 0xFF488298);//pUnit->color;	
				//				}
				colorrect.modulateAlpha(cell->getEffectiveAlpha());
				pFont->drawText(cell->getGeometryBuffer(), pUnit->Text, pt, &rect, colorrect, 0.0f, 1.0f, 1.0f, false, true);
			}
		}

		//corner image at top left
		if (cell->GetCornerImage())
		{
			const Image* pCornerImage = cell->GetCornerImage();

			Rect cornerRect(rect);
			Size imageSize = pCornerImage->getSize();
			cornerRect.d_left = 0;
			cornerRect.d_top = 0;
			cornerRect.setSize(pCornerImage->getSize());
			colorrect = ColourRect(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
			colorrect.modulateAlpha(cell->getEffectiveAlpha());
			pCornerImage->draw(cell->getGeometryBuffer(), cornerRect.getPosition(), cornerRect.getSize(), &rect, colorrect);
		}
	}

	void FalagardSkillBox::renderCellImage(Rect& rect)
	{
		SkillBox* cell = (SkillBox*)d_window;
		if (NULL==cell)
		{
			return;
		}
		ColourRect colorrect(colour(0xFFFFFFFF));

		const Image* pImage=cell->GetImage();
		if (NULL!=pImage)
		{
			if(cell->isDisabled() || cell->IsAshy())
			{
				colorrect = ColourRect(0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F,0xFF7F7F7F);
                colorrect.modulateAlpha(cell->getEffectiveAlpha());
				pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
			}
			else
			{
				colorrect.modulateAlpha(cell->getEffectiveAlpha());
                pImage->draw(cell->getGeometryBuffer(),rect.getPosition(),rect.getSize(),&rect,colorrect);
			}
		}
	}
} // End of  CEGUI namespace section
