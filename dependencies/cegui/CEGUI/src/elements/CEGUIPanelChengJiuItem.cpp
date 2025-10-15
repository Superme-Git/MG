/***********************************************************************
	filename: 	CEGUIPanelChengJiuItem.cpp
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
#include "elements/CEGUIPanelChengJiuItem.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUIWindow.h"
#include "CEGUIImage.h"
#include "CEGUIImagesetManager.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const ColourRect PanelChengJiuItem::NameColour = ColourRect(0xFFFFEF42,0xFFFFEF42,0xFFFFBB05,0xFFFFBB05);
	const ColourRect PanelChengJiuItem::IntroColor = ColourRect(0xFF00B3FE);
	const ColourRect PanelChengJiuItem::TipsColor = ColourRect(0xFF0DB900);
	const ColourRect PanelChengJiuItem::NoFinishColor = ColourRect(0xFF7A7A7A);

	const colour PanelChengJiuItem::IntroBoardColor = 0xFF082143;
	const colour PanelChengJiuItem::TipsBoardColor = 0xFF00103C;
	const colour PanelChengJiuItem::NoFinishBoardColor = 0xFF131A21;

/*************************************************************************
	Constructor,
*************************************************************************/
PanelChengJiuItem::PanelChengJiuItem(const String& name,const String& intro,const String& tips,const String& finishstr,bool bShowProgress,int curGrade,int totalGrade,
		int64_t finishtime,int item_id,void* item_data, bool bAutodelete,bool bHide):
	PanelItem(name, intro,tips,item_id,bAutodelete,item_data,bHide),
	d_FinishTime(finishtime),
	d_FinishStr(finishstr),
	d_totalGrade(totalGrade),
	d_bShowProgressBar(bShowProgress),
	d_RightIcon(NULL)
{
	if(finishtime == 0)
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementDisableSmall");
	}
	else
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementNormalSmall");
	}

	d_NotFInishIcon = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementDisable");
	d_HoverIcon = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementHoverSmall");
	
}

PanelChengJiuItem::~PanelChengJiuItem(void)
{
}

void PanelChengJiuItem::SetFinishTime(int64_t time)
{
	d_FinishTime = time;
	if(d_FinishTime == 0)
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementDisableSmall");
	}
	else
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementNormalSmall");
	}
}
/*************************************************************************
	Draw the list box item in its current state.
*************************************************************************/
void PanelChengJiuItem::draw(GeometryBuffer* buffer, const Rect& targetRect,
                           float alpha, const Rect* clipper)
{
    
	ColourRect final_colours;
	final_colours = getModulateAlphaColourRect(ColourRect(0xFFFFFFFF),alpha);

	
	//����ͼ
	if(d_BackImage)
	{
		d_BackImage->draw(buffer,targetRect,clipper,final_colours);
	}

	//����
	if(d_MouseOn && d_HoverIcon)
	{
		d_HoverIcon->draw(buffer,targetRect,clipper,final_colours);
	}

	Rect LeftIconRect(targetRect);
	LeftIconRect.d_left+= 10;
	LeftIconRect.d_top+= 2;

	Rect RightIconRect(targetRect);
	RightIconRect.d_top+= 2;
	//���ͼ��Ļ���
	if(d_LeftIcon)
	{
		Size imagesize = d_LeftIcon->getSize();
		LeftIconRect.setWidth(imagesize.d_width);
		LeftIconRect.setHeight(imagesize.d_height);
		d_LeftIcon->draw(buffer,LeftIconRect,clipper,final_colours);

		if(IsFinish() == false)
		{
			LeftIconRect.d_left-= 3;
			LeftIconRect.setWidth(d_NotFInishIcon->getSize().d_width);
			LeftIconRect.setHeight(d_NotFInishIcon->getSize().d_height);

			d_NotFInishIcon->draw(buffer,LeftIconRect,clipper,final_colours);
		}
	}
	
	//�ұ�ͼ��Ļ���
	if(d_RightIcon)
	{
		Size imagesize = d_LeftIcon->getSize();
		RightIconRect.d_left = RightIconRect.d_right - imagesize.d_width -5;
		RightIconRect.setWidth(imagesize.d_width);
		RightIconRect.setHeight(imagesize.d_height);
		d_RightIcon->draw(buffer,RightIconRect,clipper,final_colours);
	}
	
	Font* fnt = System::getSingleton().getDefaultFont();
	Vector2 draw_pos(targetRect.getPosition());
	
	if(IsFinish())
	{
		//�ɾ�����
		float   nameWidth = fnt->getTextExtent(d_ItemName);
		draw_pos.d_y+= 5;
		draw_pos.d_x =(targetRect.getWidth()- nameWidth)/2.0f;
		fnt->drawText(buffer, d_ItemName, draw_pos, clipper,NameColour,0.0f,1.0f,1.0f,false,true,ColourRect(0xFF5F4100));
		
		//�ɾ;���˵��
		draw_pos = targetRect.getPosition();
		draw_pos.d_y += 25.0f;
		float   introWidth = fnt->getTextExtent(d_ItemIntro);
		draw_pos.d_x=(targetRect.getWidth()- introWidth)/2.0f;
		fnt->drawText(buffer, d_ItemIntro, draw_pos, clipper,IntroColor,0.0f,1.0f,1.0f,false,true,ColourRect(IntroBoardColor));
		
		//չ�������
		if(isOpen())
		{
			//�Ѵ�ɣ����д��ʱ��	
			draw_pos = targetRect.getPosition();
			draw_pos.d_y += 50.0f;

			float timeWidth = fnt->getTextExtent(d_FinishStr);
			draw_pos.d_x =(targetRect.getWidth()- timeWidth)/2.0f;
			fnt->drawText(buffer,d_FinishStr, draw_pos, clipper,TipsColor,0.0f,1.0f,1.0f,false,true,ColourRect(TipsBoardColor));
		
			if(d_Itemtips.empty() == false)
			{
				draw_pos = targetRect.getPosition();
				draw_pos.d_y += 70.0f;
				float strWidth = fnt->getTextExtent(d_Itemtips);
				draw_pos.d_x+=(targetRect.getWidth()- strWidth)/2.0f;
				fnt->drawText(buffer,d_Itemtips, draw_pos, clipper,TipsColor,0.0f,1.0f,1.0f,false,true,ColourRect(TipsBoardColor));
			}
		}
	}
	//δ���
	else
	{	
		//�ɾ�����
		float   nameWidth = fnt->getTextExtent(d_ItemName);
		draw_pos.d_y+= 5;
		draw_pos.d_x+=(targetRect.getWidth()- nameWidth)/2.0f;
		fnt->drawText(buffer, d_ItemName, draw_pos, clipper,NoFinishColor,0.0f,1.0f,1.0f,false,true,ColourRect(NoFinishBoardColor));

		//�ɾ;���˵��
		draw_pos = targetRect.getPosition();
		draw_pos.d_y += 25.0f;
		float   introWidth = fnt->getTextExtent(d_ItemIntro);
		draw_pos.d_x+=(targetRect.getWidth()- introWidth)/2.0f;
		fnt->drawText(buffer, d_ItemIntro, draw_pos, clipper,NoFinishColor,0.0f,1.0f,1.0f,false,true,ColourRect(NoFinishBoardColor));

		//չ�������
		if(isOpen())
		{
			if(d_Itemtips.empty() == false)
			{
				draw_pos = targetRect.getPosition();
				if(d_bShowProgressBar == false)
				{
					draw_pos.d_y += 50.0f;
				}
				else
				{
					draw_pos.d_y += 70.0f;
				}

				float strWidth = fnt->getTextExtent(d_Itemtips);
				draw_pos.d_x+=(targetRect.getWidth()- strWidth)/2.0f;
				fnt->drawText(buffer,d_Itemtips, draw_pos, clipper,NoFinishColor,0.0f,1.0f,1.0f,false,true,ColourRect(NoFinishBoardColor));
			}
		}
	}

}

bool PanelChengJiuItem::operator<(const PanelItem& rhs) const
{
	const PanelChengJiuItem& rhss = static_cast<const PanelChengJiuItem&>(rhs);
	if(d_FinishTime > 0 && rhss.d_FinishTime > 0)
	{
		return d_FinishTime > rhss.d_FinishTime;
	}
	else if(d_FinishTime > 0 && rhss.d_FinishTime == 0)
	{
		return true;
	}
	else if(d_FinishTime == 0 && rhss.d_FinishTime > 0)
	{
		return false;
	}
	else
	{
		return d_ItemID < rhss.d_ItemID;
	}
}

bool PanelChengJiuItem::operator >(const PanelChengJiuItem& rhs) const
{
	if(d_FinishTime > 0 && rhs.d_FinishTime > 0)
	{
		return d_FinishTime < rhs.d_FinishTime;
	}
	else if(d_FinishTime > 0 && rhs.d_FinishTime == 0)
	{
		return true;
	}
	else if(d_FinishTime == 0 && rhs.d_FinishTime > 0)
	{
		return false;
	}
	else
	{
		return d_SortID > rhs.d_SortID;
	}
}

void PanelChengJiuItem::toggleIsOpen()
{
	d_isOpen = !d_isOpen;
	if(d_isOpen && IsFinish())
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementNormalBig");
		d_HoverIcon = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementHoverBig");
	}
	else if(d_isOpen && IsFinish() == false)
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementDisableBig");
		d_HoverIcon = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementHoverBig");
	}
	if(d_isOpen == false && IsFinish())
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementNormalSmall");
		d_HoverIcon = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementHoverSmall");
	}
	else if(d_isOpen == false && IsFinish() == false)
	{
		d_BackImage = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementDisableSmall");
		d_HoverIcon = ImagesetManager::getSingleton().getImage("MainControl15", "AchievementHoverSmall");
	}
}

} // End of  CEGUI namespace section
