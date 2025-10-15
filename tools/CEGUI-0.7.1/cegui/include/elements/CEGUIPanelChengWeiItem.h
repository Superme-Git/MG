/***********************************************************************
	filename: 	CEGUIPanelChengWeiItem.h
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
#ifndef _CEGUIPanelChengWeiItem_h_
#define _CEGUIPanelChengWeiItem_h_

#include "CEGUIPanelItem.h"
#include "../CEGUIBasicRenderedStringParser.h"

// Start of CEGUI namespace section
namespace CEGUI
{

class CEGUIEXPORT PanelChengWeiItem : public PanelItem
{
public:

	static const ColourRect NameColour;
	static const ColourRect IntroColor;
	static const ColourRect TipsColor;//

	static const colour     IntroBoardColor;
	static const colour     TipsBoardColor;
	
	/*************************************************************************
		Construction and Destruction
	*************************************************************************/
	/*!
	\brief
		base class constructor
	*/
	PanelChengWeiItem(const String& name,const String& intro,const String& tips, const String& finishstr,int item_id = 0,void* item_data = 0,bool bAutodelete = false);

	/*!
	\brief
		base class destructor
	*/
	virtual ~PanelChengWeiItem(void);

/*************************************************************************/
    void draw(GeometryBuffer* buffer, const Rect& targetRect, float alpha, const Rect* clipper);

protected:

	String d_FinishStr;//成就达成时的文字说明，“已达成：2011-5-11 21：23”
	
	virtual void toggleIsOpen();
};

} // End of  CEGUI namespace section


#endif	// end of guard _CEGUIListboxTextItem_h_
