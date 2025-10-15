#ifndef FireClient_AndroidLoginDialog_h
#define FireClient_AndroidLoginDialog_h

#include "../../GameUI/dialog.h"
#include "../../GameUI/SingletonDialog.h"

#include <jni.h>

extern "C" {
    void androidOnLogin(void * self, int status);
    JNIEXPORT jstring JNICALL Java_com_locojoy_mini_mt3_GameApp_getHttpNoticeUrl(JNIEnv* env, jclass obj);
    JNIEXPORT jstring JNICALL Java_com_locojoy_mini_mt3_GameApp_getHttpShareUrl(JNIEnv* env, jclass obj);
}

class CAndroidLoginDialog: public SingletonDialog<CAndroidLoginDialog>,public Dialog
{
public:
    CAndroidLoginDialog();
    ~CAndroidLoginDialog();
    
public:
    static void ShowLogin(bool allowAutoLogin, bool showLogin = true);

	CEGUI::String GetLayoutFileName();
    void OnCreate();
    void DestroyDialog();
    void OnClose();
    
    void Login();
    
    
    bool HandleLogin(const CEGUI::EventArgs& e);
    
    
private:
    CEGUI::PushButton*      m_pLogin;
    bool allowAutoLogin;
};


#endif

