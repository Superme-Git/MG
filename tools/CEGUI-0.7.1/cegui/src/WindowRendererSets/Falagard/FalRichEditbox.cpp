/***********************************************************************
    filename:   FalMultiLineEditbox.cpp
    created:    Thu Jul 7 2005
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
#include "FalRichEditbox.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUIWindowManager.h"
#include "elements/CEGUIScrollbar.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIGeometryBuffer.h"

// Start of CEGUI namespace section
namespace CEGUI
{
FalagardRichEditboxProperties::BlinkCaret FalagardRichEditbox::d_blinkCaretProperty;
FalagardRichEditboxProperties::BlinkCaretTimeout FalagardRichEditbox::d_blinkCaretTimeoutProperty;

const utf8 FalagardRichEditbox::TypeName[] = "Falagard/RichEditbox";
const String FalagardRichEditbox::UnselectedTextColourPropertyName( "NormalTextColour" );
const String FalagardRichEditbox::SelectedTextColourPropertyName( "SelectedTextColour" );
const String FalagardRichEditbox::ActiveSelectionColourPropertyName( "ActiveSelectionColour" );
const String FalagardRichEditbox::InactiveSelectionColourPropertyName( "InactiveSelectionColour" );
const float FalagardRichEditbox::DefaultCaretBlinkTimeout(0.50f);

FalagardRichEditbox::FalagardRichEditbox(const String& type) :
    RichEditboxWindowRenderer(type),
    d_blinkCaret(true),
    d_caretBlinkTimeout(DefaultCaretBlinkTimeout),
    d_caretBlinkElapsed(0.0f),
    d_showCaret(true),
	d_lastTextOffset(0.0f),
	d_FixStartDrawLineIdx(false),
	d_StartDrawLineIdx(0),
	d_StartDrawLineYoffset(0.0f),
    d_SelectBrushLeft(NULL),
    d_SelectBrushMiddle(NULL),
    d_SelectBrushRight(NULL)

{
    registerProperty(&d_blinkCaretProperty);
    registerProperty(&d_blinkCaretTimeoutProperty);
    
	// new add , 原来的皮肤为 common_sangongge3 npcdialogbackleft 及 component5 npcdialogpushedleft
    d_SelectBrushLeft=ImagesetManager::getSingleton().getImage("common", "common_buttonyellow60_l");
	d_SelectBrushMiddle = ImagesetManager::getSingleton().getImage("common", "common_buttonyellow60_c");
	d_SelectBrushRight = ImagesetManager::getSingleton().getImage("common", "common_buttonyellow60_r");
    
	d_PushedBrushLeft = ImagesetManager::getSingleton().getImage("common", "common_buttonlan60_l");
	d_PushedBrushMiddle = ImagesetManager::getSingleton().getImage("common", "common_buttonlan60_c");
	d_PushedBrushRight = ImagesetManager::getSingleton().getImage("common", "common_buttonlan60_r");

}

Rect FalagardRichEditbox::getTextRenderArea(void) const
{
    RichEditbox* w = (RichEditbox*)d_window;
    const WidgetLookFeel& wlf = getLookNFeel();
    bool v_visible = w->getVertScrollbar()->isVisible(true);
    bool h_visible = w->getHorzScrollbar()->isVisible(true);
	
	Rect textrenderrect;

    // if either of the scrollbars are visible, we might want to use another text rendering area
    if (v_visible || h_visible)
    {
        String area_name("TextArea");

       /* if (h_visible)
        {
            area_name += "H";
        }*/
        if (v_visible)
        {
            area_name += "V";
        }
        area_name += "Scroll";

        if (wlf.isNamedAreaDefined(area_name))
        {
            textrenderrect=wlf.getNamedArea(area_name).getArea().getPixelRect(*w);
			textrenderrect.d_bottom-=w->GetTextBottomEdge();
			return textrenderrect;
        }
    }

    // default to plain TextArea
     textrenderrect=wlf.getNamedArea("TextArea").getArea().getPixelRect(*w);
	 textrenderrect.d_bottom-=w->GetTextBottomEdge();
	  
	 return textrenderrect;
}

void FalagardRichEditbox::cacheEditboxBaseImagery()
{
    
	RichEditbox* w = (RichEditbox*)d_window;
	if (!w->IsBackGroundEnable())
	{
		return;
	}
	
    const StateImagery* imagery;

    // get WidgetLookFeel for the assigned look.
    const WidgetLookFeel& wlf = getLookNFeel();
    // try and get imagery for our current state
    imagery = &wlf.getStateImagery(w->isDisabled() ? "Disabled" : (w->isReadOnly() ? "ReadOnly" : "Enabled"));
    // peform the rendering operation.
    imagery->render(*w);
}

void FalagardRichEditbox::cacheCaratImagery(const Rect& textArea)
{
    RichEditbox* w = (RichEditbox*)d_window;
	if (NULL==w)
	{
		return;
	}
	
    Font* fnt = w->getFont();

    // require a font so that we can calculate carat position.
    if (fnt&&w->isInputFocus())
    {
        // get line that carat is in
        //size_t caratLine = w->getLineNumberFromIndex(w->getCaratIndex());

        //const RichEditbox::LineList& d_lines = w->getFormattedLines();

        // if carat line is valid.
       // if (caratLine < d_lines.size())
        {
            // calculate pixel offsets to where carat should be drawn
            //size_t caratLineIdx = w->getCaratIndex() - d_lines[caratLine].d_startIdx;
            float xpos = w->GetCaratDrawPos().d_x;
            float  ypos= w->GetCaratDrawPos().d_y;
//             // get base offset to target layer for cursor.
//             Renderer* renderer = System::getSingleton().getRenderer();
//             float baseZ = renderer->getZLayer(7) - renderer->getCurrentZ();

            // get WidgetLookFeel for the assigned look.
            const WidgetLookFeel& wlf = getLookNFeel();
            // get carat imagery
            const ImagerySection& caratImagery = wlf.getImagerySection("Carat");

            // calculate finat destination area for carat
            Rect caratArea;
            caratArea.d_left    = textArea.d_left + xpos+d_lastTextOffset;
			if (!w->isWordWrapped())
				caratArea.d_top = textArea.d_top + (textArea.getHeight() - fnt->getLineSpacing())*0.5;
			else
				caratArea.d_top = textArea.d_top + ypos;
            caratArea.setWidth(caratImagery.getBoundingRect(*w).getSize().d_width);
            caratArea.setHeight(fnt->getLineSpacing());
            caratArea.offset(Point(-w->getHorzScrollbar()->getScrollPosition(), -w->getVertScrollbar()->getScrollPosition()));

            // cache the carat image for rendering.
            ColourRect color=w->GetColourRect();
            color.setAlpha(w->getEffectiveAlpha());
            caratImagery.render(*w, caratArea, &color, &textArea);
        }
    }
}

void FalagardRichEditbox::render()
{
    RichEditbox* w = (RichEditbox*)d_window;
    // render general frame and stuff before we handle the text itself
    cacheEditboxBaseImagery();

    // Render edit box text
	Rect textarea(getTextRenderArea());
	ResetCaratDrawPos(textarea);
	cacheTextLines(textarea);

	// draw caret
	if ((w->hasInputFocus() && !w->isReadOnly()) &&
		(!d_blinkCaret || d_showCaret))
		cacheCaratImagery(textarea);

}

void FalagardRichEditbox::cacheTextLines(const Rect& dest_area)
{
    RichEditbox* w = (RichEditbox*)d_window;
    //// text is already formatted, we just grab the lines and render them with the required alignment.
	Rect drawArea(dest_area);		
	
	float vertScrollPos = w->getVertScrollbar()->getScrollPosition();


	drawArea.offset(Point(-w->getHorzScrollbar()->getScrollPosition(), -vertScrollPos));

	if (w->isHoriAutoCenter())
	{
		float YOffset=(drawArea.getHeight()-w->GetExtendSize().d_height)/2.0f;
		drawArea.offset(Point(0.0f,YOffset));
	}

	

	if (w->isVertAutoCenter())
	{
		float XOffset=(drawArea.getWidth()-w->GetExtendSize().d_width)/2.0f;
		drawArea.offset(Point(XOffset,0.0f));
	}
	
   
	bool bTextBorder=w->isTextBorder();

	drawArea.offset(Point(d_lastTextOffset,0.0f));

     const RichEditbox::LineList& d_lines = w->getFormattedLines();
	 RichEditbox::ComponentList& FormatComponents=w->getFormattedComponents();

	 size_t linecount=d_lines.size();

	 size_t componentCount=FormatComponents.size();

	 if (componentCount<=0||linecount<=0)
	 {
		 return;
	 }

	 bool bRefreshUpdateCpnList=false; //是否要更新每帧都更新的列表

	 float DrawHeight=0.0f;
	 size_t startLine=0;
	 if (w->isReadOnly())
	 {
		 if (d_FixStartDrawLineIdx)
		 {
			 DrawHeight=d_StartDrawLineYoffset;
			 startLine=d_StartDrawLineIdx;
		 }else
		 {
			 bRefreshUpdateCpnList=true; //要更新每帧都更新的列表

			 float maxLineHeight=w->GetMaxLineHeight();
			 if (maxLineHeight>0.0f)
			 {
				 float fLineIdx=vertScrollPos/maxLineHeight;
				 if (fLineIdx>0.0f)
				 {
					 startLine=(size_t)fLineIdx;
					 if (startLine<linecount)
					 {
						 DrawHeight=d_lines[startLine].d_YOffset;
					 }else
					 {
						 startLine=0;
					 }
					 
				 }
				 
			 }
			 

		 }
	 }
    
	 for (size_t j=startLine;j<linecount;j++)
	 {
		 size_t startindex=d_lines[j].d_startComponentIdx;
		 size_t count=d_lines[j].d_ComponentCount;

		 float DrawWidth=0.0f;
		 
		 Size curlinesize=w->GetLineSize(j);

		 if (startindex<0||startindex>(componentCount-1)||(startindex+count)>componentCount)
		 {
			 continue;
		 }

		 
		 float nextLineDrawHeigh=DrawHeight+curlinesize.d_height;
		 if (w->isReadOnly()&&!d_FixStartDrawLineIdx)
		 {

			 if (nextLineDrawHeigh>vertScrollPos+dest_area.d_top-1.0f)
			 {
				 d_FixStartDrawLineIdx=true;
				 d_StartDrawLineIdx=j;
				 d_StartDrawLineYoffset=DrawHeight;
			 }else
			 {
				 DrawHeight=nextLineDrawHeigh;
				 continue;
			 }

		 }

		 
                 
         bool bIsDrawFlag=false;

         if (w->isClickSelectLine()&&!bIsDrawFlag)
         {
             Point flagDrawPos(drawArea.d_left+DrawWidth,drawArea.d_top+DrawHeight);
             
             Rect TargetRect(dest_area);
             TargetRect.d_top=dest_area.d_top+flagDrawPos.d_y+2.0f;
             
             TargetRect.d_bottom=TargetRect.d_top+curlinesize.d_height-2.0f;
             TargetRect.d_left-=7.0f;
             TargetRect.d_right-=1.0f;
			 TargetRect.d_top += w->GetComponentHeightOffset();
			 TargetRect.d_bottom -= w->GetComponentHeightOffset();
             Rect clipRect(dest_area);
             clipRect.d_left-=7.0f;

             if (w->isPushedLine(j))
             {
                 drawPushedBrush(w->getGeometryBuffer(),TargetRect,&clipRect,ColourRect(0xFFFFFFFF));
             }else
             {
                 drawLineBrush(w->getGeometryBuffer(),TargetRect,&clipRect,ColourRect(0xFFFFFFFF));
                 
             }
             
             bIsDrawFlag=true;
         }

        
		 const Image* pSelectImage=System::getSingleton().GetTextBrushImage();
		 for(size_t i=startindex;i<startindex+count;i++ )
		 {
			 Size componentsize=FormatComponents[i]->getPixelSize();/*d_FormatComponents[i].getPixelSize()*/;

			 ColourRect final_cols(0xFFFFFFFF);
			 final_cols.modulateAlpha(w->getEffectiveAlpha());

			 Point pos(drawArea.d_left+DrawWidth,drawArea.d_top+DrawHeight);
			 float vertOffset=curlinesize.d_height-componentsize.d_height;

			 //如果是编辑模式以及不换行，则从底部往上画，解决表情过高时，文字看不到的bug
			 if ( !w->isReadOnly() && !w->isWordWrapped() )
			 {
				 //pos.d_y=drawArea.d_bottom-componentsize.d_height;  //change by liugeng
				 pos.d_y += (drawArea.getHeight() - componentsize.d_height)*0.5;
				 vertOffset=0.0f;
			 }
			 

			 //画选择标志 非编辑模式不画选择标志
			 if (!w->isReadOnly())
			 {
				 size_t startDrawOffset=0;
				 size_t EndDrawOffset=0;
				 if (pSelectImage&&w->GetSelectDrawPos(i,startDrawOffset,EndDrawOffset)&&!w->isReadOnly())
				 {
					 FormatComponents[i]->DrawSelectFlag(w->getGeometryBuffer(),pos,
						 &final_cols, &dest_area,pSelectImage,startDrawOffset, EndDrawOffset);
				 }
			 }

			 //画mouse在其上的标志
			/* if (FormatComponents[i]->IsMouseOn())
			 {
				 FormatComponents[i]->DrawMouseOnFlag(w->getGeometryBuffer(),pos,&final_cols, &dest_area);
			 }*/
			 
			if (FormatComponents[i]->isTextType())
			{
				float textYOffset=w->GetTextYOffset();
				pos.d_y+=textYOffset;
			}
             
            
            
             if (FormatComponents[i]->GetType()==RichEditboxComponentType_LinkText)
             {
                 float drawPixHeight=FormatComponents[i]->getDrawPixelSize().d_height;
                 pos.d_y+=(curlinesize.d_height-drawPixHeight)/2.0f;
				 pos.d_y += 2.0f;//因为BUTTON加了2个像素。
             }
             
             if (FormatComponents[i]->GetType()==RichEditboxComponentType_Image)
             {
                 float drawPixHeight=componentsize.d_height;
                 float linSpace=FormatComponents[i]->GetLineSpace();
                 drawPixHeight-=linSpace;
                 pos.d_y+=(curlinesize.d_height-drawPixHeight)/2.0f;
                 vertOffset = 0.0f;
             }
             
             //文字是否居中
			 if (w->getTextLineVertCenter()){
				 pos.d_x += ((dest_area.d_right - dest_area.d_left) - w->GetExtendSize().d_width) / 2;
			 }
			
			 FormatComponents[i]->draw(w->getGeometryBuffer(),
				 pos,
				 &final_cols, &dest_area,vertOffset,0.0f,bTextBorder);
			
			 DrawWidth+=componentsize.d_width;
		 }

		 DrawHeight+=curlinesize.d_height;

		 if (DrawHeight>vertScrollPos+dest_area.d_bottom+1.0f)
		 {
			 if (bRefreshUpdateCpnList&&d_FixStartDrawLineIdx)
			 {
				 if (d_StartDrawLineIdx<linecount)
				 {
					 size_t startUpdateCpnIdx=d_lines[d_StartDrawLineIdx].d_startComponentIdx;
					 
					
					 size_t endUpdateCpnIdx=startindex+count-1;
					 w->RefreshUpdateCpnList(startUpdateCpnIdx,endUpdateCpnIdx);
				 }
				 
			 }
			 
			 break;
		 }
	 }

	 if (bRefreshUpdateCpnList&&d_FixStartDrawLineIdx)
	 {
		 if (d_StartDrawLineIdx<linecount)
		 {
			 size_t startUpdateCpnIdx=d_lines[d_StartDrawLineIdx].d_startComponentIdx;
			 w->RefreshUpdateCpnList(startUpdateCpnIdx,componentCount-1);
		 }

	 }
	 
}

colour FalagardRichEditbox::getOptionalPropertyColour(const String& propertyName) const
{
    if (d_window->isPropertyPresent(propertyName))
        return PropertyHelper::stringToColour(d_window->getProperty(propertyName));
    else
        return colour(0,0,0);
}

colour FalagardRichEditbox::getUnselectedTextColour() const
{
    return getOptionalPropertyColour(UnselectedTextColourPropertyName);
}

colour FalagardRichEditbox::getSelectedTextColour() const
{
    return getOptionalPropertyColour(SelectedTextColourPropertyName);
}

colour FalagardRichEditbox::getActiveSelectionColour() const
{
    return getOptionalPropertyColour(ActiveSelectionColourPropertyName);
}

colour FalagardRichEditbox::getInactiveSelectionColour() const
{
    return getOptionalPropertyColour(InactiveSelectionColourPropertyName);
}



//----------------------------------------------------------------------------//
void FalagardRichEditbox::update(float elapsed)
{
    // do base class stuff
    WindowRenderer::update(elapsed);

    // only do the update if we absolutely have to
    if (d_blinkCaret &&
        !static_cast<RichEditbox*>(d_window)->isReadOnly() &&
        static_cast<RichEditbox*>(d_window)->hasInputFocus())
    {
        d_caretBlinkElapsed += elapsed;

        if (d_caretBlinkElapsed > d_caretBlinkTimeout)
        {
            d_caretBlinkElapsed = 0.0f;
            d_showCaret ^= true;
            // state changed, so need a redraw
            d_window->invalidate();
        }
    }
}

//----------------------------------------------------------------------------//
bool FalagardRichEditbox::isCaretBlinkEnabled() const
{
    return d_blinkCaret;
}

//----------------------------------------------------------------------------//
float FalagardRichEditbox::getCaretBlinkTimeout() const
{
    return d_caretBlinkTimeout;
}

//----------------------------------------------------------------------------//
void FalagardRichEditbox::setCaretBlinkEnabled(bool enable)
{
    d_blinkCaret = enable;
}

//----------------------------------------------------------------------------//
void FalagardRichEditbox::setCaretBlinkTimeout(float seconds)
{
    d_caretBlinkTimeout = seconds;
}

//重置光标及整个文本的绘制横向偏移，主要是针对单行可编辑,解决输入字过多后面的文字不画的问题
void FalagardRichEditbox::ResetCaratDrawPos(const Rect& dest_area)
{
	RichEditbox* w = (RichEditbox*)d_window;
	//// text is already formatted, we just grab the lines and render them with the required alignment.
	Rect drawArea(dest_area);
	float vertScrollPos = w->getVertScrollbar()->getScrollPosition();
	drawArea.offset(Point(-w->getHorzScrollbar()->getScrollPosition(), -vertScrollPos));

	if (w->isHoriAutoCenter())
	{
		float YOffset=(drawArea.getHeight()-w->GetExtendSize().d_height)/2.0f;
		drawArea.offset(Point(0.0f,YOffset));
	}

	float textOffset=0.0f;
	if (!w->isReadOnly()&&!w->isWordWrapped())
	{
		float extentToCarat = w->GetCaratDrawPos().d_x;
		// get carat imagery
		const WidgetLookFeel& wlf = getLookNFeel();
		// get carat imagery
		const ImagerySection& caratImagery = wlf.getImagerySection("Carat");

	
		// store carat width
		float caratWidth = caratImagery.getBoundingRect(*w, drawArea).getWidth();

	
		if (!w->isInputFocus())
		{
			textOffset = d_lastTextOffset;
		}else if ((d_lastTextOffset + extentToCarat) < 0)
			textOffset = -extentToCarat;
		// if carat is off to the right.
		else if ((d_lastTextOffset + extentToCarat) >= (drawArea.getWidth() - caratWidth))
			textOffset = drawArea.getWidth() - extentToCarat - caratWidth;
		// else carat is already within the box
		else
			textOffset = d_lastTextOffset;
		//todo 会有浮点数精度问题
		if (w->isCaratOnEnd())
		{
			if (extentToCarat<drawArea.getWidth())
			{
				textOffset=0.0f;
			}else
			{
				textOffset=drawArea.getWidth()-extentToCarat-caratWidth;
			}
			
		}
		

	}
	d_lastTextOffset=textOffset;


}

void FalagardRichEditbox::drawLineBrush(GeometryBuffer* buffer, const Rect& targetRect, const Rect* clipper, const ColourRect& colorRect) const
{
    if (d_SelectBrushLeft&&d_SelectBrushMiddle&&d_SelectBrushRight)
    {
        float leftWidth=d_SelectBrushLeft->getWidth();
        float RightWidth=d_SelectBrushRight->getWidth();
        float middleDrawWidth=targetRect.getWidth()-leftWidth-RightWidth;
        
        Rect LeftDrawRect(targetRect);
        LeftDrawRect.d_right=LeftDrawRect.d_left+leftWidth;
        d_SelectBrushLeft->draw(buffer,LeftDrawRect,clipper,colorRect);
        
        Rect MiddleDrawRect(targetRect);
        MiddleDrawRect.d_left=LeftDrawRect.d_right;
        MiddleDrawRect.d_right=MiddleDrawRect.d_left+middleDrawWidth;
        d_SelectBrushMiddle->draw(buffer,MiddleDrawRect,clipper,colorRect);
        
        Rect RightDrawRect(targetRect);
        RightDrawRect.d_left=targetRect.d_right-RightWidth;
        RightDrawRect.d_right=targetRect.d_right;
        d_SelectBrushRight->draw(buffer,RightDrawRect,clipper,colorRect);
    }
    
}
    
    void FalagardRichEditbox::drawPushedBrush(GeometryBuffer* buffer, const Rect& targetRect, const Rect* clipper, const ColourRect& colorRect) const
    {
        if (d_PushedBrushLeft&&d_PushedBrushMiddle&&d_PushedBrushRight)
        {
            float leftWidth=d_SelectBrushLeft->getWidth();
            float RightWidth=d_SelectBrushRight->getWidth();
            float middleDrawWidth=targetRect.getWidth()-leftWidth-RightWidth;
            
            Rect LeftDrawRect(targetRect);
            LeftDrawRect.d_right=LeftDrawRect.d_left+leftWidth;
            d_PushedBrushLeft->draw(buffer,LeftDrawRect,clipper,colorRect);
            
            Rect MiddleDrawRect(targetRect);
            MiddleDrawRect.d_left=LeftDrawRect.d_right;
            MiddleDrawRect.d_right=MiddleDrawRect.d_left+middleDrawWidth;
            d_PushedBrushMiddle->draw(buffer,MiddleDrawRect,clipper,colorRect);
            
            Rect RightDrawRect(targetRect);
            RightDrawRect.d_left=targetRect.d_right-RightWidth;
            RightDrawRect.d_right=targetRect.d_right;
            d_PushedBrushRight->draw(buffer,RightDrawRect,clipper,colorRect);
        }
        
    }

//----------------------------------------------------------------------------//


} // End of  CEGUI namespace section
