/***********************************************************************
	filename: 	CEGUIPanelQiYuanItem.h
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
#ifndef _CEGUIPanelQiYuanItem_h_
#define _CEGUIPanelQiYuanItem_h_

#include "CEGUIPanelItem.h"
#include "../CEGUIBasicRenderedStringParser.h"

// Start of CEGUI namespace section
namespace CEGUI
{

class CEGUIEXPORT PanelQiYuanItem : public PanelItem
{
public:

	//! Default text colour.
	static const ColourRect NameColour;
	static const ColourRect IntroColor;
	static const ColourRect TipsColor;//
	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		base class constructor
	*/
	PanelQiYuanItem(const String& name,const String& intro,const String& tips, const String& awardstr, int wishstate,int item_id = 0,void* item_data = 0);

	/*!
	\brief
		base class destructor
	*/
	virtual ~PanelQiYuanItem(void);

/*************************************************************************/
	//是否达成成就
	int  getWishState(){ return d_WishState;}
	void SetFinishState( int wishstate){d_WishState = wishstate;}

	void SetAwardStr(const String& awardstr) { d_AwardstrStr = awardstr;}
	void SetTips(const String& tips) {d_Itemtips = tips;}

    void draw(GeometryBuffer* buffer, const Rect& targetRect, float alpha, const Rect* clipper);

protected:

	int d_WishState;//愿望状态，0为未祈愿，1为祈愿了，但是没还愿，2为可还愿

	String d_AwardstrStr;//愿望奖励
	
	virtual void toggleIsOpen();

	 //virtual bool    operator<(const PanelQiYuanItem& rhs) const;//     {return d_ItemID < rhs.d_ItemID;}
	 //virtual bool    operator>(const PanelQiYuanItem& rhs) const;//     {return d_ItemID > rhs.d_ItemID;}

};

} // End of  CEGUI namespace section


#endif	// end of guard _CEGUIListboxTextItem_h_
