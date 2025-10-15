/***********************************************************************
filename:   CEGUIMessageTip.cpp
*************************************************************************/
#include "elements/CEGUIMessageTip.h"
//#include "elements/CEGUIRichEditbox.h"
#include "CEGUIFont.h"
#include "CEGUIImage.h"
#include "CEGUIWindowManager.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	const String MessageTip::WidgetTypeName("CEGUI/MessageTip");

	//////////////////////////////////////////////////////////////////////////
	// Event name constants
	const String MessageTip::EventNamespace("MessageTip");
	const String MessageTip::RichEditboxNameSuffix("__auto_richeditbox__");
	const String MessageTip::EventStartFade("StartFade");
	const float MessageTip::d_ConstWidth = 400.0f;
	const float MessageTip::d_ConstHeight = 20.0f;
	const float MessageTip::d_SysTipHeight = 40.0f;
	//////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////
	MessageTip::MessageTip(const String& type, const String& name) :
		Window(type, name)
	{
		d_destYPos = 0.5f;
		d_decHeight = 0.0f;
		d_elapsed = 0.0f;
		d_displayTime   = 5.0f; //yanji 20150921 2.0f->3.0f
		d_fadeTime      = 0.2f;
		d_tipType = eMsgTip;
		d_startFade = false;
		//yanji 20150921
		d_startYPos = 0.5f;
		d_fHeightScale = 0.0f;
		//setAlwaysOnTop(true);
	}

	MessageTip::MessageTip(const String& type) :
		Window(type)
	{
	}
	const MessageTip& MessageTip::operator=(const MessageTip& t)
	{
		Window::operator=(t);
		d_destYPos = t.d_destYPos;
		d_decHeight = t.d_decHeight;
		d_elapsed = t.d_elapsed;
		d_displayTime = t.d_displayTime;
		d_fadeTime = t.d_fadeTime;
		d_tipType = t.d_tipType;
		d_startFade = t.d_startFade;
		d_startYPos = t.d_startYPos;
		d_fHeightScale = t.d_fHeightScale;
		return *this;
	}
	Window* MessageTip::clone(Window* wnd)
	{
		MessageTip* retWnd = (MessageTip*)wnd;
		if (retWnd == NULL)
			retWnd = new MessageTip(d_type);
#ifdef _DEBUG
		const char* type = d_type.c_str();
		assert(d_type == MessageTip::WidgetTypeName);
#endif
		*retWnd = *this;
		return retWnd;
	}

	MessageTip::~MessageTip(void)
	{

	}
	
	/*************************************************************************
	Initialise the Window based object ready for use.
	*************************************************************************/
	void MessageTip::initialiseComponents(bool bClone)
	{
		RichEditbox* editbox = getRichEditbox();
		if (!bClone)
		{
			editbox->SetBackGroundEnable(false);
			editbox->setReadOnly(true);
			editbox->setShowVertScrollbar(false);
			editbox->SetForceHideVerscroll(true);
			//	editbox->setAlwaysOnTop(true);
		}
		// internal event wiring
		editbox->subscribeEvent(Window::EventMouseButtonDown, Event::Subscriber(&CEGUI::MessageTip::editbox_MouseDownHandler, this));
		// put components in their initial positions
		if (!bClone)
			performChildWindowLayout();
	}

	RichEditbox* MessageTip::getRichEditbox()const
	{
		return static_cast<RichEditbox*>(WindowManager::getSingleton().getWindow(
			getName() + RichEditboxNameSuffix));	
	}
	
	void MessageTip::SetTipsType(TipType type)
	{
		d_tipType = type;
		RichEditbox* editbox = getRichEditbox();
		if(type == eMsgTip)
		{	
			editbox->setFont("simhei-14");
			setSize(UVector2(cegui_absdim(d_ConstWidth),cegui_absdim(d_ConstHeight)));
		}
		else if(type == eSystemTip)
		{
			d_displayTime   = 4.0f;
			setEnabled(false);
			editbox->setFont("simhei-14");
			setSize(UVector2(cegui_absdim(getParentPixelWidth()-200),cegui_absdim(d_SysTipHeight)));
		}
	}

	void MessageTip::sizeSelf()
	{
		Size textSize(getTextSize());

		setSize(UVector2(cegui_absdim(textSize.d_width),cegui_absdim(textSize.d_height)));
	}

	bool MessageTip::GetTextureIsLoading()
	{
		return g_bIsTextLoading;
	}
    
	void MessageTip::updateSelf(float elapsed)
	{
		// base class processing.
		Window::updateSelf(elapsed);

		if (!isVisible())
		{
			return;
		}

		d_elapsed += elapsed;
        
        if (g_bIsTextLoading)
        {
            sizeSelf();
        }
        
		if ((d_displayTime - d_elapsed)<= d_fadeTime && d_startFade == false)
		{
			WindowEventArgs args(this);
			onStartFade(args);
		}

		if (d_displayTime == 0.0f || d_elapsed >= d_displayTime)
		{
			destroy();
		}
		else if (d_startFade && d_fadeTime > 0 && d_displayTime > 0 && getAlpha() > 0)
		{
			float alpha = ceguimax((d_displayTime - d_elapsed) / d_fadeTime, 0.1f);
			setAlpha(alpha);
		}

		if (this->getYPosition().d_scale < 0) {
			if (g_bIsTextLoading)
			{
				invalidate();
			}
			return;
		}

		//if(d_tipType == eSystemTip)
		//{
		//	if((0.2f - d_decHeight) > d_destYPos)
		//	{
		//		d_decHeight += 0.1f*0.07f;

		//		setYPosition(cegui_reldim(0.2f - d_decHeight));			
		//	}
		//	else
		//	{
		//		setYPosition(cegui_reldim(d_destYPos));		
		//	}
		//	
  //          if (g_bIsTextLoading)
  //          {
  //              invalidate();
  //          }
  //          
  //          return;
		//}
		//else
		//{
		//	//if((0.41f - d_decHeight) > d_destYPos) yanji 20150921
		//	if ((d_startYPos - d_decHeight) > d_destYPos)
		//	{
	
		//			//yanji 20150921
		//			//d_fHeightScale = (this->getHeight().d_offset) / this->getParentPixelHeight();
		//			d_decHeight += 0.1f*d_fHeightScale*0.4;
	
		//		setYPosition(cegui_reldim(d_startYPos - d_decHeight));
		//	}
		//	else
		//	{
		//		if (d_destYPos < d_startYPos - d_decHeight) {
		//			setYPosition(cegui_reldim(d_destYPos));
		//		}
		//		else {
		//			setYPosition(cegui_reldim(d_startYPos - d_decHeight));
		//		}
		//	}
		//	
  //          if (g_bIsTextLoading)
  //          {
  //              invalidate();
  //          }
  //          return;
		//}
        
        
	}

	

	void MessageTip::onMouseClicked(MouseEventArgs& e)
	{
		if(d_tipType == eMsgTip)
		{
			destroy();
		}
	}

	void MessageTip::onTextChanged(WindowEventArgs& e)
	{
		RichEditbox* editbox = getRichEditbox();

		// update ourselves only if needed (prevents perpetual event loop & stack overflow)
		if (editbox->getText() != getText())
		{
			editbox->Clear();
			editbox->AppendParseText(getText());
			editbox->Refresh();
		}
		// set size and potition of the tooltip window to consider new text
		sizeSelf();
		// about text changes too.
	}

	Size MessageTip::getTextSize() const
	{
		Size sz(0.0f, 0.0f);
		sz = getRichEditbox()->GetExtendSize();
		
		if(d_tipType == eMsgTip)
		{
			if(isDisabled() == true)//无后面的背景框
			{
				sz.d_width += 14.0f;
				sz.d_height += 7.0f;
			}
			else
			{
				sz.d_width = d_ConstWidth;
				sz.d_height += 10.0f;
			}
			
		}
		else
		{
			sz.d_width += 14.0f;
			sz.d_height += 14.0f;
		}
		return sz;
	}

	bool MessageTip::editbox_MouseDownHandler(const EventArgs& e)
	{
		if(d_tipType == eMsgTip)
		{
			destroy();
		}
		return true;
		
	}

	//----------------------------------------------------------------------------//
	void MessageTip::onStartFade(WindowEventArgs& e)
	{
		d_startFade = true;
		fireEvent(EventStartFade, e, EventNamespace);
	}

	void MessageTip::InitSysMsgParamter()
	{
		d_destYPos = 0.5f;
		d_decHeight = 0.0f;
		d_elapsed = 0.0f;
		d_displayTime   = 2.0f; 
		d_fadeTime      = 0.2f;
		d_startFade = false;
		setText("");
		setFont(CEGUI::String("simhei-20"));
	}


} // End of  CEGUI namespace section


