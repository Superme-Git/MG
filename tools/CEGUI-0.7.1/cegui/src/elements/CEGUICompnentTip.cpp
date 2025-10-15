/***********************************************************************
filename:   CEGUIMessageTip.cpp
*************************************************************************/
#include "elements/CEGUICompnentTip.h"
//#include "elements/CEGUIRichEditbox.h"
#include "CEGUIFont.h"
#include "CEGUIImage.h"
#include "CEGUIWindowManager.h"
// Start of CEGUI namespace section
namespace CEGUI
{
	const String CompnentTip::WidgetTypeName("CEGUI/CompnentTip");

	//////////////////////////////////////////////////////////////////////////
	// Event name constants
	const String CompnentTip::EventNamespace("CompnentTip");
	//////////////////////////////////////////////////////////////////////////

	/*************************************************************************
	TooltipWindowRenderer
	*************************************************************************/
	CompnentTipWindowRenderer::CompnentTipWindowRenderer(const String& name) :
	WindowRenderer(name, CompnentTip::EventNamespace)
	{
	}

	//////////////////////////////////////////////////////////////////////////
	CompnentTip::CompnentTip(const String& type, const String& name) :
	Window(type, name),
	d_targetCompnent(0),
	d_InChatOutWin(false)
	{
		d_elapsed = 0.0f;
		d_displayTime   = 10.0f;
		d_fadeTime      = 0.2f;
		setMousePassThroughEnabled(true);
	}

	CompnentTip::CompnentTip(const String& type) :
		Window(type)
	{
	}
	const CompnentTip& CompnentTip::operator=(const CompnentTip& t)
	{
		Window::operator=(t);
		assert(t.d_targetCompnent == NULL);
		d_targetCompnent = NULL;
		d_elapsed = t.d_elapsed;
		d_displayTime = t.d_displayTime;
		d_fadeTime = t.d_fadeTime;
		d_InChatOutWin = t.d_InChatOutWin;
		return *this;
	}
	Window* CompnentTip::clone(Window* wnd)
	{
		CompnentTip* retWnd = (CompnentTip*)wnd;
		if (retWnd == NULL)
			retWnd = new CompnentTip(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == CompnentTip::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

	CompnentTip::~CompnentTip(void)
	{

	}

	void CompnentTip::SetTipsText(const String& tip)
	{
		setText(tip);
	}

	void CompnentTip::SetTargetCompnent(const RichEditboxComponent* pCompnent,bool bInChatOutWin)
	{
		d_InChatOutWin = bInChatOutWin;
		if(pCompnent == 0)
		{
			d_targetCompnent = 0;
			setAlpha(0.0f);
			d_elapsed = 0;

			if (d_parent)
				d_parent->removeChildWindow(this);

			setText(L"");
			hide();
			return;
		}
		else if (d_targetCompnent != pCompnent)
		{
			if (bInChatOutWin)
			{
				if (System::getSingleton().GetChatOutRootWnd())
				{
					SetInChatOutWnd(true);
					System::getSingleton().GetChatOutRootWnd()->addChildWindow(this);
				}

			}else
			{
				SetInChatOutWnd(false);
				System::getSingleton().getGUISheet()->addChildWindow(this);

			}
			show();
			moveToFront();
			d_targetCompnent = pCompnent;
		}

		
		// set size and potition of the tooltip window.
		sizeSelf();
		positionSelf();
	    d_elapsed = 0;
	}

	void CompnentTip::sizeSelf()
	{
		Size textSize(getTextSize());

		setSize(UVector2(cegui_absdim(textSize.d_width),cegui_absdim(textSize.d_height)));
	}

	void CompnentTip::updateSelf(float elapsed)
	{
		// base class processing.
		Window::updateSelf(elapsed);
 		if ((d_displayTime > 0) && ((d_elapsed += elapsed) < d_displayTime))
 		{
 			setAlpha((1.0f / d_fadeTime) * d_elapsed);
 		}
 		else
 		{
 			setAlpha(0.0f);
 			d_elapsed = 0;
			d_targetCompnent = 0;
			setText(L"");
  			if (d_parent)
  				d_parent->removeChildWindow(this);
 
 			hide();
 		}	
	}

	void CompnentTip::positionSelf(void)
	{
		MouseCursor& cursor = MouseCursor::getSingleton();
		Rect screen(Vector2(0, 0), System::getSingleton().getRenderer()->getDisplaySize());
		if (d_InChatOutWin)
		{
			Window* pChatOutRootWnd=System::getSingleton().GetChatOutRootWnd();
			if (NULL!=pChatOutRootWnd)
			{
				screen.setSize(pChatOutRootWnd->getPixelSize());
			}
		}

		Rect tipRect(getUnclippedOuterRect());
		//const Image* mouseImage = cursor.getImage();

		Point mousePos(cursor.getPosition());
		//Size mouseSz(0,0);

		/*if (mouseImage)
		{
			mouseSz = mouseImage->getSize();
		}*/

		Size selfSize=getPixelSize();

		Point tmpPos(mousePos.d_x, mousePos.d_y-selfSize.d_height);

		if (!d_InChatOutWin)
		{
			tmpPos.d_y=mousePos.d_y+32.0f;
		}
		

		tipRect.setPosition(tmpPos);
		tipRect.setSize(selfSize);
		
		if (tipRect.d_right>screen.d_right)
		{
			tmpPos.d_x=screen.d_right-selfSize.d_width;
			
		}
		

		// if tooltip would be off the right of the screen,
		// reposition to the other side of the mouse cursor.
		/*if (screen.d_right < tipRect.d_right)
		{
			tmpPos.d_x = mousePos.d_x - tipRect.getWidth() - 5;
		}*/

		// if tooltip would be off the bottom of the screen,
		// reposition to the other side of the mouse cursor.
		/*if (screen.d_bottom < tipRect.d_bottom)
		{
			tmpPos.d_y = mousePos.d_y - tipRect.getHeight() - 5;
		}*/

		if (tmpPos.d_y<0.0f)
		{
			tmpPos.d_y=mousePos.d_y+32.0f;
		}

		if (tmpPos.d_x<0.0f)
		{
			tmpPos.d_x=0.0f;
		}
		
		

		

		// set final position of tooltip window.
		setPosition(
			UVector2(cegui_absdim(tmpPos.d_x),
			cegui_absdim(tmpPos.d_y)));
	}

	Size CompnentTip::getTextSize() const
	{
		if (d_windowRenderer != 0)
		{
			CompnentTipWindowRenderer* wr = (CompnentTipWindowRenderer*)d_windowRenderer;
			return wr->getTextSize();
		}
		else
		{
			return getTextSize_impl();
		}
	}

	Size CompnentTip::getTextSize_impl() const
	{
		const RenderedString& rs(getRenderedString());
		Size sz(0.0f, 0.0f);

		for (size_t i = 0; i < rs.getLineCount(); ++i)
		{
			const Size line_sz(rs.getPixelSize(i));
			sz.d_height += line_sz.d_height;

			if (line_sz.d_width > sz.d_width)
				sz.d_width = line_sz.d_width;
		}

		return sz;
	}

} // End of  CEGUI namespace section


