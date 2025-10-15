#include "AndroidLoginDialog.h"

#include "stdafx.h"
#include "LoginManager.h"
#include "IniManager.h"
#include "GameApplication.h"
#include "GameUIManager.h"

#include "ChannelPlatformInterface.h"
#include "ChannelPlatformUtil.h"

#include "GameTable/message/CMessageTip.h"

#include "utils/StringCover.h"

#include <android/log.h>
#define  LOG_TAG    "main"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

#include "ConnectGetServerInfo.h"

jstring Java_com_locojoy_mini_mt3_GameApp_getHttpNoticeUrl(JNIEnv* env, jclass obj) {
	std::string strHttpNoticeUrl = GetServerInfo()->getHttpAdressByEnum(eHttpNoticeUrl) + "?r=x" + StringCover::intToString(rand());
	return env->NewStringUTF(strHttpNoticeUrl.c_str());
}

jstring Java_com_locojoy_mini_mt3_GameApp_getHttpShareUrl(JNIEnv* env, jclass obj) {
	std::string strHttpShareUrl = GetServerInfo()->getHttpAdressByEnum(eHttpShareUrl);
	return env->NewStringUTF(strHttpShareUrl.c_str());
}

CAndroidLoginDialog::CAndroidLoginDialog()
{
    m_eDialogType = eDialogType_Null;
}

CAndroidLoginDialog::~CAndroidLoginDialog()
{
}

void CAndroidLoginDialog::OnCreate()
{
    Dialog::OnCreate();

    allowAutoLogin = true;
    
	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
    
	m_pLogin = static_cast<CEGUI::PushButton*>(winMgr.getWindow("loginquick/LoginBtn1"));

	m_pLogin->subscribeEvent(CEGUI::PushButton::EventClicked, CEGUI::Event::Subscriber(&CAndroidLoginDialog::HandleLogin, this));

    if (GetChannelPlatformInterface()->GetPlatformName() == 14 && GetChannelPlatformInterface()->IsLogined() == 0) {
         m_pLogin->setVisible(false);
    }
}

void CAndroidLoginDialog::DestroyDialog()
{
    CloseDialog();
}

void CAndroidLoginDialog::OnClose()
{
    Dialog::OnClose();
}

CEGUI::String CAndroidLoginDialog::GetLayoutFileName()
{
	return "loginquickdialog.layout";
}

bool CAndroidLoginDialog::HandleLogin(const CEGUI::EventArgs &e)
{
    LOGD("CAndroidLoginDialog::HandleLogin");

    Login();
    
    
    return true;
}

void onLogined()
{
    LOGD("CAndroidLoginDialog::onLogined 1");

    LoginManager * loginManager = gGetLoginManager();
    loginManager->SetAccountInfo(channelPlatformGetUserID());
    loginManager->SetSession(channelPlatformGetSessionId());
    loginManager->SetPassword(channelPlatformGetSessionId());

    LOGD("CAndroidLoginDialog::onLogined 2");
    
//    if(GetLoginManager()->isAutoLogin() && statusAutoLogin == AUTO_LOGIN)
//        loginManager->LoginIn();
    
    gGetLoginManager()->ToServerChoose(gGetLoginManager()->GetSelectArea(),gGetLoginManager()->GetSelectServer());

    LOGD("CAndroidLoginDialog::onLogined 3");

    CAndroidLoginDialog::OnExit();

    LOGD("CAndroidLoginDialog::onLogined 4");
}

extern "C" {
    
    void androidOnLogin(void * self, int status)
    {
        int statusLogin = status & 0x00FF;
        int statusAutoLogin = status & 0xFF00;
        if(status == LOGIN_STATUS_SUCCESS || status == LOGIN_STATUS_SUCCESS)
        {
            onLogined();
            
        }
        else {
        }
    }
    
}

void CAndroidLoginDialog::Login()
{
    LOGD("CAndroidLoginDialog::Login 1");

    gGetLoginManager();

    LOGD("CAndroidLoginDialog::Login 2");

    if(! GetChannelPlatformInterface()->IsLogined()) {
        LOGD("CAndroidLoginDialog::Login 3");

        GetChannelPlatformInterface()->Login(androidOnLogin);
        return;
    }
    else {
        onLogined();
    }
}

void CAndroidLoginDialog::ShowLogin(bool allowAutoLogin, bool showLogin)
{
    CAndroidLoginDialog * dialog = GetSingletonDialog();
    dialog->allowAutoLogin = allowAutoLogin;
    dialog->SetVisible(true);
    if(allowAutoLogin) {
        dialog->Login();
    }
    else {
	if(showLogin) {
            GetChannelPlatformInterface()->Login(androidOnLogin);
	}
    }
}

