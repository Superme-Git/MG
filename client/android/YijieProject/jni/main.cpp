#include "cocos2d.h"
#include "platform/android/jni/JniHelper.h"
#include <jni.h>
#include <android/log.h>


#include "../../../common/platform/android/SDJniHelper.h"
#include "../../../FireClient/Application/androidcommon/ChannelPlatformInterface.h"
#include "../../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/VideoPlayerEngine.h"


#define  LOG_TAG    "main"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

#include "client/linux/handler/exception_handler.h"
#include "client/linux/handler/minidump_descriptor.h"

extern void gSetReloadAllTexturesState(bool flag);
extern void ReloadAllTextures();

static google_breakpad::ExceptionHandler* exceptionHandler;
bool DumpCallback(const google_breakpad::MinidumpDescriptor& descriptor,
					void* context, bool succeeded) {
	printf("Dump path: %s\n", descriptor.path());
	return succeeded;
}

using namespace cocos2d;
using namespace cocos2d::extension;

extern bool gRunGameApplication();
extern void gReceiveMemoryWarning();

extern "C"
{

jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
	LOGD("game vm %p", vm);
    JniHelper::setJavaVM(vm);
	MT3::JniHelper::setJavaVM(vm);

    return JNI_VERSION_1_4;
}
void Java_com_locojoy_mini_mt3_GameApp_setUpBreakpad(JNIEnv* env, jobject obj, jstring filepath) {
	const char *path = env->GetStringUTFChars(filepath, 0);
	google_breakpad::MinidumpDescriptor descriptor(path);
	exceptionHandler = new google_breakpad::ExceptionHandler(descriptor, NULL, DumpCallback, NULL, true, -1);
	env->ReleaseStringUTFChars(filepath, path);
}

void Java_org_cocos2dx_lib_Cocos2dxRenderer_nativeNotifyMemoryWarning(JNIEnv* env) {
	gReceiveMemoryWarning();
}

void Java_org_cocos2dx_lib_Cocos2dxVideoHelper_nativeExecuteVideoCallback(JNIEnv* env, jobject obj, jint index, jint event){
	cocos2d::VideoPlayer::executeVideoCallback(index, event);
}

void Java_org_cocos2dx_lib_Cocos2dxRenderer_nativeInit(JNIEnv*  env, jobject thiz, jint w, jint h)
{
    if (!CCDirector::sharedDirector()->getOpenGLView())
    {
    
    	LOGD("CCDirector::sharedDirector()->getOpenGLView() Done");
    	
        CCEGLView *view = CCEGLView::sharedOpenGLView();
        view->setFrameSize(w, h);
		
		gRunGameApplication();
    }
    else
    {
		LOGD("java coco2dx native init");
		ccDrawInit();
		ccGLInvalidateStateCache();

		CCShaderCache::sharedShaderCache()->reloadDefaultShaders();
		//CCTextureCache::reloadAllTextures();
		gSetReloadAllTexturesState(true);
		ReloadAllTextures();
        CCNotificationCenter::sharedNotificationCenter()->postNotification(EVENT_COME_TO_FOREGROUND, NULL);
        CCDirector::sharedDirector()->setGLDefaultValues();
    }
}


void jStringToBuffer(JNIEnv* env, jstring jstr, char * buffer)
{
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0)
    {
        memcpy(buffer, ba, alen);
    }
    buffer[alen] = 0;
    env->ReleaseByteArrayElements(barr, ba, 0);
}

// imlementation of ChannelPlatformInterface

const char * const JavaClassName = "com/locojoy/mini/mt3/JniProxy";
const char * const JavaClassName1 = "com/locojoy/mini/mt3/GameResLoadErrorHandle"; //SystemUIUtil";  //

static void * CallbackSelf = 0;
static CallbackOnLogin callbackOnLogin = 0;
static CallbackOnLogin callbackOnGuestRegistered = 0;
static CallbackOnLogout callbackOnLogout = 0;



int ChannelPlatformInterface_IsLogined()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "isLogined", "()Z"))
    {
        jboolean result = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_IsLogined: Result %d", (int)result);
        
        return result;
    }
    else {
        LOGD("ChannelPlatformInterface_IsLogined couldn't be found.");
    }
    
    return 0;
}

int ChannelPlatformInterface_IsGuest()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "isGuest", "()Z"))
    {
        jboolean result = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_IsGuest: Result %d", (int)result);
        
        return result;
    }
    else {
        LOGD("ChannelPlatformInterface_IsGuest couldn't be found.");
    }
    
    return 0;
}

void ChannelPlatformInterface_Login(CallbackOnLogin onLogin)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "login", "()V"))
    {
        callbackOnLogin = onLogin;
        
        t.env->CallStaticVoidMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_Login");
    }
    else {
        LOGD("ChannelPlatformInterface_Login couldn't be found.");
    }
}

void ChannelPlatformInterface_GetSessionId(char * session)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "getSessionId", "()Ljava/lang/String;"))
    {
        jstring jstr = (jstring)(t.env->CallStaticObjectMethod(t.classID, t.methodID));
        jStringToBuffer(t.env, jstr, session);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_GetSessionId: %s", session);
    }
    else {
        LOGD("ChannelPlatformInterface_GetSessionId couldn't be found.");
    }
}

void ChannelPlatformInterface_GuestRegister(CallbackOnLogin onGuestRegistered)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "guestRegister", "()V"))
    {
        callbackOnGuestRegistered = onGuestRegistered;
        
        t.env->CallStaticVoidMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_GuestRegister");
    }
    else {
        LOGD("ChannelPlatformInterface_GuestRegister couldn't be found.");
    }
}

void ChannelPlatformInterface_GetUserName(char * userName)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "getUserName", "()Ljava/lang/String;"))
    {
        jstring jstr = (jstring)(t.env->CallStaticObjectMethod(t.classID, t.methodID));
        jStringToBuffer(t.env, jstr, userName);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_GetUserName: %s", userName);
    }
    else {
        LOGD("ChannelPlatformInterface_GetUserName couldn't be found.");
    }
}

void ChannelPlatformInterface_GetUserID(char * userID)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "getUserID", "()Ljava/lang/String;"))
    {
        jstring jstr = (jstring)(t.env->CallStaticObjectMethod(t.classID, t.methodID));
        jStringToBuffer(t.env, jstr, userID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_GetUserID: %s", userID);
    }
    else {
        LOGD("ChannelPlatformInterface_GetUserID couldn't be found.");
    }
}

void ChannelPlatformInterface_ChangeAccount(CallbackOnLogin onLogin)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "changeAccount", "()V"))
    {
        callbackOnLogin = onLogin;
        
        t.env->CallStaticVoidMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_ChangeAccount");
    }
    else {
        LOGD("ChannelPlatformInterface_ChangeAccount couldn't be found.");
    }
}

void ChannelPlatformInterface_GetPlatformID(char * platformID)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "getPlatformID", "()Ljava/lang/String;"))
    {
        jstring jstr = (jstring)(t.env->CallStaticObjectMethod(t.classID, t.methodID));
        jStringToBuffer(t.env, jstr, platformID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_GetPlatformID");
    }
    else {
        LOGD("ChannelPlatformInterface_GetPlatformID couldn't be found.");
    }
}

void ChannelPlatformInterface_Purchase(CallbackOnLogin onPurchased,
                                       const char * orderNo,
                                       int itemId,
                                       const char * itemName,
                                       int itemNum,
                                       float price,
                                       int serviceID,
                                       const char * payType,
                                       const char * roleName,
                                       const char * roleID,
                                       const char * rate
                                       )
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "purchase", "(Ljava/lang/String;ILjava/lang/String;IFILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
    {
        //        callbackOnLogin = onLogin;
        
        LOGD("ChannelPlatformInterface_Purchase orderNo: %s itemId: %d itemName: %s itemNum: %d price: %f serviceID: %d",
             orderNo, itemId, itemName, itemNum, price, serviceID
             );
        
        jstring jOrderNo = t.env->NewStringUTF(orderNo);
        jstring jItemName = t.env->NewStringUTF(itemName);
        jstring jPayType = t.env->NewStringUTF(payType);
        jstring jRoleName = t.env->NewStringUTF(roleName);
        jstring jRoleID = t.env->NewStringUTF(roleID);
        jstring jRate = t.env->NewStringUTF(rate);
        
        t.env->CallStaticVoidMethod(t.classID, t.methodID, jOrderNo, itemId, jItemName, itemNum,
                                    price, serviceID, jPayType, jRoleName, jRoleID, jRate);
        t.env->DeleteLocalRef(t.classID);
        t.env->DeleteLocalRef(jOrderNo);
        t.env->DeleteLocalRef(jItemName);
        t.env->DeleteLocalRef(jPayType);
        t.env->DeleteLocalRef(jRoleName);
        t.env->DeleteLocalRef(jRoleID);
        t.env->DeleteLocalRef(jRate);
        
        LOGD("ChannelPlatformInterface_Purchase");
    }
    else {
        LOGD("ChannelPlatformInterface_Purchase couldn't be found.");
    }
}

void ChannelPlatformInterface_EnterPlatformCenter()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "enterPlatformCenter", "()V"))
    {
        t.env->CallStaticVoidMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_EnterPlatformCenter");
    }
    else {
        LOGD("ChannelPlatformInterface_EnterPlatformCenter couldn't be found.");
    }
}

void ChannelPlatformInterface_EnterPlatformForum()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "enterPlatformForum", "()V"))
    {
        t.env->CallStaticVoidMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_EnterPlatformForum");
    }
    else {
        LOGD("ChannelPlatformInterface_EnterPlatformForum couldn't be found.");
    }
}

void ChannelPlatformInterface_EnterPlatformFeedback()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "enterPlatformFeedback", "()V"))
    {
        t.env->CallStaticVoidMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);

        LOGD("ChannelPlatformInterface_EnterPlatformFeedback");
    }
    else {
        LOGD("ChannelPlatformInterface_EnterPlatformFeedback couldn't be found.");
    }
}

int ChannelPlatformInterface_HasPlatformCenter()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "hasPlatformCenter", "()Z"))
    {
        jboolean result = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_HasPlatformCenter: Result %d", (int)result);
        
        return result;
    }
    else {
        LOGD("ChannelPlatformInterface_HasPlatformCenter couldn't be found.");
    }
    
    return 0;
}

int ChannelPlatformInterface_HasPlatformForum()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "hasPlatformForum", "()Z"))
    {
        jboolean result = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_HasPlatformForum: Result %d", (int)result);
        
        return result;
    }
    else {
        LOGD("ChannelPlatformInterface_HasPlatformForum couldn't be found.");
    }
    
    return 0;
}

int ChannelPlatformInterface_HasFeedback()
{
    JniMethodInfo t;

    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "hasFeedback", "()Z"))
    {
        jboolean result = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);

        LOGD("ChannelPlatformInterface_HasFeedback: Result %d", (int)result);

        return result;
    }
    else {
        LOGD("ChannelPlatformInterface_HasFeedback couldn't be found.");
    }

    return 0;
}

void ChannelPlatformInterface_GetPlatformCenterName(char * name)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "getPlatformCenterName", "()Ljava/lang/String;"))
    {
        jstring jstr = (jstring)(t.env->CallStaticObjectMethod(t.classID, t.methodID));
        jStringToBuffer(t.env, jstr, name);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_GetPlatformCenterName");
    }
    else {
        LOGD("ChannelPlatformInterface_GetPlatformCenterName couldn't be found.");
    }
}

void ChannelPlatformInterface_GetPlatformForumName(char * name)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "getPlatformForumName", "()Ljava/lang/String;"))
    {
        jstring jstr = (jstring)(t.env->CallStaticObjectMethod(t.classID, t.methodID));
        jStringToBuffer(t.env, jstr, name);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_GetPlatformForumName");
    }
    else {
        LOGD("ChannelPlatformInterface_GetPlatformForumName couldn't be found.");
    }
}

void ChannelPlatformInterface_GetPlatformFeedbackName(char * name)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "getPlatformFeedbackName", "()Ljava/lang/String;"))
    {
        jstring jstr = (jstring)(t.env->CallStaticObjectMethod(t.classID, t.methodID));
        jStringToBuffer(t.env, jstr, name);
        t.env->DeleteLocalRef(t.classID);

        LOGD("ChannelPlatformInterface_GetPlatformFeedbackName");
    }
    else {
        LOGD("ChannelPlatformInterface_GetPlatformFeedbackName couldn't be found.");
    }
}
    

void ChannelPlatformInterface_SetValues(const char * jsonValues)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "setValues", "(Ljava/lang/String;)V"))
    {
        jstring jValues = t.env->NewStringUTF(jsonValues);
        
        t.env->CallStaticVoidMethod(t.classID, t.methodID, jValues);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_SetValues");
    }
    else {
        LOGD("ChannelPlatformInterface_SetValues couldn't be found.");
    }
}

void ChannelPlatformInterface_SetOnLogout(CallbackOnLogout onLogout)
{
    callbackOnLogout = onLogout;
}

void ChannelPlatformInterface_LogoutPlatform()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "logoutPlatform", "()V"))
    {
        t.env->CallStaticVoidMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_logoutPlatform Called");
    }
    else {
        LOGD("ChannelPlatformInterface_logoutPlatform couldn't be found.");
    }
    
    return;
}

void ChannelPlatformInterface_LoginGameCallback()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "loginGameCallback", "()V"))
    {
        t.env->CallStaticVoidMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_loginGameCallback Called");
    }
    else {
        LOGD("ChannelPlatformInterface_loginGameCallback couldn't be found.");
    }
    
    return;
}

void ChannelPlatformInterface_SetCallbackSelf(void * self)
{
    CallbackSelf = self;
}

int ChannelPlatformInterface_SupportFeature(int feature)
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "supportFeature", "(I)Z"))
    {
        jboolean result = t.env->CallStaticBooleanMethod(t.classID, t.methodID, feature);
        t.env->DeleteLocalRef(t.classID);
        
        LOGD("ChannelPlatformInterface_SupportFeature: Result %d", (int)result);
        
        return result;
    }
    else {
        LOGD("ChannelPlatformInterface_SupportFeature couldn't be found.");
    }
    
    return 0;
}

int ChannelPlatformInterface_GetPlatformName()
{
    JniMethodInfo t;
    
    if(JniHelper::getStaticMethodInfo(t, JavaClassName, "getPlatformName", "()I"))
    {
        jint result = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
        t.env->DeleteLocalRef(t.classID);
        

        LOGD("ChannelPlatformInterface_GetPlatformName: Result %d", (int)result);
        
        return result;
    }
    else {
        LOGD("ChannelPlatformInterface_GetPlatformName couldn't be found.");
    }
    
    return 0;
}

void ChannelPlatformInterface_GameResLoadError()
{
	JniMethodInfo t;
	if(JniHelper::getStaticMethodInfo(t, JavaClassName1, "showMessageBox","()V"))
    {
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
		LOGD("ChannelPlatformInterface_GameResLoadError success \n");
	}
	else
	{
		LOGD("ChannelPlatformInterface_GameResLoadError couldn't be found\n");
	}
}

ChannelPlatformInterface * GetChannelPlatformInterface()
{
    static ChannelPlatformInterface channelPlatformInterface = {
        &ChannelPlatformInterface_IsLogined,
        &ChannelPlatformInterface_IsGuest,
        &ChannelPlatformInterface_Login,
        &ChannelPlatformInterface_GetSessionId,
        &ChannelPlatformInterface_GuestRegister,
        &ChannelPlatformInterface_GetUserName,
        &ChannelPlatformInterface_GetUserID,
        &ChannelPlatformInterface_ChangeAccount,
        &ChannelPlatformInterface_GetPlatformID,
        &ChannelPlatformInterface_SetOnLogout,
        &ChannelPlatformInterface_SetCallbackSelf,
        &ChannelPlatformInterface_Purchase,
        
        &ChannelPlatformInterface_EnterPlatformCenter,
        &ChannelPlatformInterface_EnterPlatformForum,
        &ChannelPlatformInterface_EnterPlatformFeedback,
        &ChannelPlatformInterface_HasPlatformCenter,
        &ChannelPlatformInterface_HasPlatformForum,
        &ChannelPlatformInterface_HasFeedback,
        &ChannelPlatformInterface_GetPlatformCenterName,
        &ChannelPlatformInterface_GetPlatformForumName,
        &ChannelPlatformInterface_GetPlatformFeedbackName,
        
        &ChannelPlatformInterface_SetValues,
        &ChannelPlatformInterface_LogoutPlatform,
        &ChannelPlatformInterface_LoginGameCallback,

		&ChannelPlatformInterface_SupportFeature,
		&ChannelPlatformInterface_GetPlatformName,
		&ChannelPlatformInterface_GameResLoadError,
    };
    
    return &channelPlatformInterface;
}


//void Java_com_locojoy_mini_mt3_JniProxy_nativeOnLogined(JNIEnv*  env, jobject thiz, jint status)
//{
//    if(callbackOnLogin) {
//        callbackOnLogin(CallbackSelf, status);
//    }
//}
//
//void Java_com_locojoy_mini_mt3_JniProxy_nativeOnGuestRegistered(JNIEnv*  env, jobject thiz, jint status)
//{
//    if(callbackOnGuestRegistered) {
//        callbackOnGuestRegistered(CallbackSelf, status);
//    }
//}
//
//void Java_com_locojoy_mini_mt3_JniProxy_nativeOnLogout(JNIEnv*  env, jobject thiz)
//{
//    if(callbackOnLogout) {
//        callbackOnLogout(CallbackSelf);
//    }
//}

}
