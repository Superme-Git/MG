/***********************************************************************
	filename: 	CEGUIPanelChengJiuItem.h
	purpose:	Interface for list box text items
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
#ifndef _CEGUIPanelChengJiuItem_h_
#define _CEGUIPanelChengJiuItem_h_

#include "CEGUIPanelItem.h"
#include "../CEGUIBasicRenderedStringParser.h"

// Start of CEGUI namespace section
namespace CEGUI
{

class CEGUIEXPORT PanelChengJiuItem : public PanelItem
{
public:

	//! Default text colour.
	static const ColourRect NameColour;
	static const ColourRect IntroColor;
	static const ColourRect TipsColor;//
	static const ColourRect NoFinishColor;

	static const colour     IntroBoardColor;
	static const colour     TipsBoardColor;
	static const colour     NoFinishBoardColor;
	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		base class constructor
	*/
	PanelChengJiuItem(const String& name,const String& intro,const String& tips, const String& finishstr, bool bShowProgress,
					  int curGrade, int totalGrade,int64_t finishtime = 0,int item_id = 0,void* item_data = 0,bool bAutodelete = false,bool bHide = false);

	/*!
	\brief
		base class destructor
	*/
	virtual ~PanelChengJiuItem(void);

/*************************************************************************/
   
	int GetCurGrade(){ return d_curGrade;}
	int GetTotalGrade() { return d_totalGrade;}

	void SetCurGrade(int grade) { d_curGrade = grade;}

	//是否达成成就
	bool IsFinish(){ return d_FinishTime == 0? false:true;}
	bool IsShowProgress() { return d_bShowProgressBar;}

	void SetRightIconImage(const Image* icon) { d_RightIcon = icon;}
	void SetFinishStr(const String& str) { d_FinishStr = str;}
	void SetFinishTime(int64_t time);// { d_FinishTime = time;}
	

    void draw(GeometryBuffer* buffer, const Rect& targetRect, float alpha, const Rect* clipper);

protected:

	int64_t d_FinishTime;//成就达成的时间

	String d_FinishStr;//成就达成时的文字说明，“已达成：2011-5-11 21：23”
	int d_curGrade;//当前成就点数
	int d_totalGrade;//成就总点数
	bool d_bShowProgressBar;//是否显示进度条

	const Image* d_RightIcon;
	const Image* d_NotFInishIcon;//未达成图标
	const Image* d_HoverIcon;//悬浮图标

	 virtual void toggleIsOpen();

	 virtual bool    operator<(const PanelItem& rhs) const;//     {return d_ItemID < rhs.d_ItemID;}
	 virtual bool    operator>(const PanelChengJiuItem& rhs) const;//     {return d_ItemID > rhs.d_ItemID;}

};

} // End of  CEGUI namespace section


#endif	// end of guard _CEGUIListboxTextItem_h_
