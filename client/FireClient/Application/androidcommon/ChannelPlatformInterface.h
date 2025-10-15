#ifndef __FireClient__ChannlePlatformInterface__
#define __FireClient__ChannlePlatformInterface__

#define JNI_STRING_MAX_SIZE 1024

#define LOGIN_STATUS_SUCCESS 0
#define LOGIN_STATUS_GUEST_SUCCESS 1
#define LOGIN_STATUS_ERROR 2

#define AUTO_LOGIN 0x0100

// now we only have one feature -- switch account
// in the future we should move "platform center", "forum", "feedback" to features.
const int FEATURE_SWITCH_ACCOUNT = 1;

typedef void (*CallbackOnLogin)(void * self, int status);
typedef void (*CallbackOnLogout)(void * self);

extern "C" {
    // implemented by JNI
    struct ChannelPlatformInterface {
        int (*IsLogined)(); // 1-true, 0-false
        int (*IsGuest)(); // 1-true, 0-false

        void (*Login)(CallbackOnLogin onLogin);
        void (*GetSessionId)(char * session); // session is at least JNI_STRING_MAX_SIZE length
        void (*GuestRegister)(CallbackOnLogin onGuestRegistered);
        void (*GetUserName)(char * userName); // userName is at least JNI_STRING_MAX_SIZE length
        void (*GetUserID)(char * userID); // userID is at least JNI_STRING_MAX_SIZE length
        void (*ChangeAccount)(CallbackOnLogin onLogin);
        void (*GetPlatformID)(char * platformID); // platformID is at least JNI_STRING_MAX_SIZE length

        void (*SetOnLogout)(CallbackOnLogout onLogout);
        void (*SetCallbackSelf)(void * self);

        void (*Purchase)(CallbackOnLogin onPurchased,
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
                                               );
        
        void (*EnterPlatformCenter)();
        void (*EnterPlatformForum)();
        void (*EnterPlatformFeedback)();
        int (*HasPlatformCenter)(); // 1-true, 0-false
        int (*HasPlatformForum)(); // 1-true, 0-false
        int (*HasFeedback)();//1-true, 0-false
        void (*GetPlatformCenterName)(char * name); // name is at least JNI_STRING_MAX_SIZE length
        void (*GetPlatformForumName)(char * name); // name is at least JNI_STRING_MAX_SIZE length
        void (*GetPlatformFeedbackName)(char * name);//name is at least JNI_STRING_MAX_SIZE length
        
        void (*SetValues)(const char * jsonContent);
        void (*LogoutPlatform)();
        void (*LoginGameCallback)();

        int (*SupportFeature)(int feature); // 1-true, 0-false
        int (*GetPlatformName)();
        void (*GameResLoadError)();
    };

    // implemented by JNI
    ChannelPlatformInterface * GetChannelPlatformInterface();

}



#endif /* defined(__FireClient__channelplatform__) */
