/***********************************************************************
	filename: 	CEGUIPanelChengWeiItem.cpp
	purpose:	Implementation of List box text items
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
#include "elements/CEGUIPanelChengWeiItem.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUIWindow.h"
#include "CEGUIImage.h"
#include "CEGUIImagesetManager.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const ColourRect PanelChengWeiItem::NameColour = ColourRect(0xFFFFEF42,0xFFFFEF42,0xFFFFBB05,0xFFFFBB05);
	const ColourRect PanelChengWeiItem::IntroColor = ColourRect(0xFF00B3FE);
	const ColourRect PanelChengWeiItem::TipsColor = ColourRect(0xFF0DB900);

	const colour PanelChengWeiItem::IntroBoardColor = 0xFF082143;
	const colour PanelChengWeiItem::TipsBoardColor = 0xFF00103C;


/*************************************************************************
	Constructor,
*************************************************************************/
PanelChengWeiItem::PanelChengWeiItem(const String& name,const String& intro,const String& tips,const String& finishstr,int item_id, void* item_data,bool bAutodelete):
	PanelItem(name, intro,tips,item_id,bAutodelete,item_data),
	d_FinishStr(finishstr)
{
	d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementNormalSmall");
}

PanelChengWeiItem::~PanelChengWeiItem(void)
{
}
/*************************************************************************
	Draw the list box item in its current state.
*************************************************************************/
void PanelChengWeiItem::draw(GeometryBuffer* buffer, const Rect& targetRect,
                           float alpha, const Rect* clipper)
{
    
	ColourRect final_colours;
	final_colours = getModulateAlphaColourRect(ColourRect(0xFFFFFFFF),alpha);
	
	//背景图
	if(d_BackImage)
	{
		d_BackImage->draw(buffer,targetRect,clipper,final_colours);
	}

	Rect LeftIconRect(targetRect);
	LeftIconRect.d_left+= 10;
	LeftIconRect.d_top+= 2;
	//左边图标的绘制
	if(d_LeftIcon)
	{
		Size imagesize = d_LeftIcon->getSize();
		LeftIconRect.setWidth(imagesize.d_width);
		LeftIconRect.setHeight(imagesize.d_height);
		d_LeftIcon->draw(buffer,LeftIconRect,clipper,final_colours);
	}
	
	Font* fnt = System::getSingleton().getDefaultFont();
	Vector2 draw_pos(targetRect.getPosition());
	
	//称谓名称
	float   nameWidth = fnt->getTextExtent(d_ItemName);
	draw_pos.d_y+= 5;
	draw_pos.d_x+=(targetRect.getWidth()- nameWidth)/2.0f;
	fnt->drawText(buffer, d_ItemName, draw_pos, clipper,NameColour,0.0f,1.0f,1.0f,false,true,ColourRect(0xFF5F4100));
	
	//称谓具体说明
	draw_pos = targetRect.getPosition();
	draw_pos.d_y += 25.0f;
	float   introWidth = fnt->getTextExtent(d_ItemIntro);
	draw_pos.d_x+=(targetRect.getWidth()- introWidth)/2.0f;
	fnt->drawText(buffer, d_ItemIntro, draw_pos, clipper,IntroColor,0.0f,1.0f,1.0f,false,true,ColourRect(IntroBoardColor));
	
	//展开情况下
	if(isOpen())
	{
		//到期日期
		draw_pos = targetRect.getPosition();
		draw_pos.d_y += 45.0f;
		
		float timeWidth = fnt->getTextExtent(d_FinishStr);
		draw_pos.d_x+=(targetRect.getWidth()- timeWidth)/2.0f;
		fnt->drawText(buffer,d_FinishStr, draw_pos, clipper,TipsColor,0.0f,1.0f,1.0f,false,true,ColourRect(TipsBoardColor));
		

		if(d_Itemtips.empty() == false)
		{
			draw_pos = targetRect.getPosition();	
			draw_pos.d_y += 65.0f;
	
			float strWidth = fnt->getTextExtent(d_Itemtips);
			draw_pos.d_x+=(targetRect.getWidth()- strWidth)/2.0f;
			fnt->drawText(buffer,d_Itemtips, draw_pos, clipper,TipsColor,0.0f,1.0f,1.0f,false,true,ColourRect(TipsBoardColor));
		}
	}

}

void PanelChengWeiItem::toggleIsOpen()
{
	d_isOpen = !d_isOpen;
	if(d_isOpen)
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementNormalBig");
	}
	if(d_isOpen == false)
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementNormalSmall");
	}
}

} // End of  CEGUI namespace section
