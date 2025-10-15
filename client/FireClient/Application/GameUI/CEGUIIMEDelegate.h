/****************************************************************************
Copyright (c) 2010 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/

#ifndef __CCEGUIIMEDelegate_H__
#define __CCEGUIIMEDelegate_H__

#include "text_input_node/CCIMEDelegate.h"
#include "touch_dispatcher/CCTouchDelegateProtocol.h"
#include <string>

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "CEGUIRect.h"
#endif

using namespace std;

class GameIMEDelegate : public cocos2d::CCIMEDelegate
{
public:
    GameIMEDelegate();
    virtual ~GameIMEDelegate();

    //Open keyboard and receive input text.
    virtual bool attachWithIME();

    //End text input  and close keyboard.
    virtual bool detachWithIME();

    //////////////////////////////////////////////////////////////////////////
    // properties
    //////////////////////////////////////////////////////////////////////////
    
    CC_SYNTHESIZE_READONLY(int, m_nCharCount, CharCount);

public:
    virtual void setString(const char *text);
    virtual const char* getString(void);
    void setInputText(const char* text);
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	void setInputRect(const CEGUI::Rect* rect);
#endif

protected:
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	CEGUI::Rect m_InputRect;
#endif
    std::string m_InputText;

public:
    virtual void setPlaceHolder(const char * text);
    virtual const char * getPlaceHolder(void);

protected:
    std::string  m_PlaceHolder;

protected:

    //////////////////////////////////////////////////////////////////////////
    // CCIMEDelegate interface
    //////////////////////////////////////////////////////////////////////////

    virtual bool canAttachWithIME();
    virtual bool canDetachWithIME();
    virtual void insertText(const char * text, int len);
    virtual void deleteBackward();
    virtual const char * getContentText();
    
    virtual void keyboardWillShow(cocos2d::CCIMEKeyboardNotificationInfo& info);
    virtual void keyboardWillHide(cocos2d::CCIMEKeyboardNotificationInfo& info);
    virtual void keyboardDidHide(cocos2d::CCIMEKeyboardNotificationInfo& info);
};



#endif
