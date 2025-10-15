#include "CEGUIIMEDelegate.h"
#include "CCDirector.h"
#include "CCEGLView.h"
#include "GameUIManager.h"
#if !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
#include "../../FireClient/IViewCtrl.h"
#endif
using namespace cocos2d;

static int _calcCharCount(const char * pszText)
{
    int n = 0;
    char ch = 0;
    while ((ch = *pszText))
    {
        CC_BREAK_IF(! ch);
        
        if (0x80 != (0xC0 & ch))
        {
            ++n;
        }
        ++pszText;
    }
    return n;
}

//////////////////////////////////////////////////////////////////////////
// constructor and destructor
//////////////////////////////////////////////////////////////////////////

GameIMEDelegate::GameIMEDelegate()
:m_InputText("")
, m_PlaceHolder("")
{ }

GameIMEDelegate::~GameIMEDelegate()
{ }


//////////////////////////////////////////////////////////////////////////
// CCIMEDelegate
//////////////////////////////////////////////////////////////////////////

const char * GameIMEDelegate::getContentText()
{
	return m_InputText.c_str();
}

void GameIMEDelegate::deleteBackward()
{
	if (gGetGameUIManager()) {
		gGetGameUIManager()->OnBackSpace();
	}
}

void GameIMEDelegate::insertText(const char * text, int len)
{
	std::string sInsert(text, len);

#ifdef ANDROID
	// insert \n means input end
	int nPos = sInsert.find('\n');
	if ((int)sInsert.npos != nPos)
	{
		len = nPos;
		sInsert.erase(nPos);
	}
#endif

	if (sInsert.length() == 1 && sInsert == "\n") {
		detachWithIME();
		return;
	}

	if (gGetGameUIManager()) {
		gGetGameUIManager()->InjectChar(sInsert.c_str());
	}
}

bool GameIMEDelegate::canDetachWithIME()
{
	return true;
}

bool GameIMEDelegate::canAttachWithIME()
{
	return true;
}

bool GameIMEDelegate::detachWithIME()
{
	bool bRet = CCIMEDelegate::detachWithIME();
	if (bRet)
	{
		// close keyboard
		CCEGLView * pGlView = CCDirector::sharedDirector()->getOpenGLView();
		if (pGlView)
		{
			pGlView->setIMEKeyboardState(false);
		}

	}
	return bRet;
}

bool GameIMEDelegate::attachWithIME()
{
    bool bRet = CCIMEDelegate::attachWithIME();
    if (bRet)
    {
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		CCEGLView * pGlView = CCDirector::sharedDirector()->getOpenGLView();
		if (pGlView)
		{
			Platform::String^ text = ref new Platform::String(s2ws(m_InputText).c_str());
			SHORT left = m_InputRect.d_left;
			SHORT top = m_InputRect.d_top;
			SHORT right = m_InputRect.d_right;
			SHORT bottom = m_InputRect.d_bottom;
			pGlView->ShowWP8Keyboard(text, left, top, right, bottom);
		}
#else

		// open keyboard
        CCEGLView * pGlView = CCDirector::sharedDirector()->getOpenGLView();
        if (pGlView)
        {
            pGlView->setIMEKeyboardState(true);
        }
#endif       
    }

    return bRet;
}



//////////////////////////////////////////////////////////////////////////
// properties
//////////////////////////////////////////////////////////////////////////

void GameIMEDelegate::keyboardDidHide(cocos2d::CCIMEKeyboardNotificationInfo& info)
{
#ifndef ANDROID
#if !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
	gGetGameApplication()->setDetachingIME(false);
#endif
#endif
}

void GameIMEDelegate::keyboardWillHide(cocos2d::CCIMEKeyboardNotificationInfo& info)
{
#ifndef ANDROID
#if !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
	CEditViewControl::hide(info.duration);
	gGetGameUIManager()->SetDetachIMEState();
	gGetGameApplication()->setDetachingIME(true);
#endif
#endif
}

void GameIMEDelegate::keyboardWillShow(cocos2d::CCIMEKeyboardNotificationInfo& info)
{
	float bottom = gGetGameUIManager()->GetCurEditBoxBottom();
#ifndef ANDROID
#if !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
	if (bottom > 0.0f) {
		float height = MIN(info.end.size.width, info.end.size.height);
		CEditViewControl::show(bottom, height, info.duration);
	}
#endif
#endif

}

const char * GameIMEDelegate::getPlaceHolder(void)
{
	return m_PlaceHolder.c_str();
}

void GameIMEDelegate::setPlaceHolder(const char * text)
{
	m_PlaceHolder = "";
	if (text != NULL) {
		m_PlaceHolder = text;
	}
}

const char* GameIMEDelegate::getString(void)
{
	return m_InputText.c_str();
}

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
void GameIMEDelegate::setInputRect(const CEGUI::Rect* rect)
{
	if (rect)
	{
		m_InputRect.d_left = rect->d_left;
		m_InputRect.d_bottom = rect->d_bottom;
		m_InputRect.d_right = rect->d_right;
		m_InputRect.d_top = rect->d_top;
	}
	else {
		m_InputRect.d_left = 0;
		m_InputRect.d_bottom = 0;
		m_InputRect.d_right = 0;
		m_InputRect.d_top = 0;
	}
}
#endif

void GameIMEDelegate::setInputText(const char* text)
{
	if (text == NULL)
	{
		m_InputText.clear();
	}
	else
	{
		m_InputText = text;
	}
}

void GameIMEDelegate::setString(const char *text)
{
    m_InputText="";
    if (text!=NULL) {
        m_InputText=text;
        
    }
#ifdef ANDROID
    // if there is no input text, display placeholder instead
    if (! m_InputText.length())
    {
        if (gGetGameUIManager()) {
            gGetGameUIManager()->InjectString(m_PlaceHolder.c_str());
        }
        
    }
    else
    {
        //CCLabelTTF::setString(m_pInputText->c_str());
        if (gGetGameUIManager()) {
            gGetGameUIManager()->InjectString(m_InputText.c_str());
        }
    }
#else
    // if there is no input text, display placeholder instead
    if (! m_InputText.length())
    {
        if (gGetGameUIManager()) {
            gGetGameUIManager()->InjectChar(m_PlaceHolder.c_str());
        }
        
    }
    else
    {
        if (gGetGameUIManager()) {
            gGetGameUIManager()->InjectChar(m_InputText.c_str());
        }
    }
#endif
    m_nCharCount = _calcCharCount(m_InputText.c_str());    //to do
}


