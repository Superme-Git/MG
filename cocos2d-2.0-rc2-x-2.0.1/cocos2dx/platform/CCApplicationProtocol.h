#ifndef __CC_APPLICATION_PROTOCOL_H__
#define __CC_APPLICATION_PROTOCOL_H__

#include "ccTypes.h"

NS_CC_BEGIN
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
enum TargetPlatform
{
    kTargetWindows,
    kTargetLinux,
    kTargetMacOS,
    kTargetAndroid,
    kTargetIphone,
    kTargetIpad,
    kTargetBlackBerry,
    kTargetNaCl,
    kTargetEmscripten,
    kTargetTizen,
    kTargetWinRT,
    kTargetWP8
};
#endif
class CCRect;

/**
 * @addtogroup platform
 * @{
 */

class CC_DLL CCApplicationProtocol
{
public:

    virtual ~CCApplicationProtocol() {}

    /**
    @brief    Implement CCDirector and CCScene init code here.
    @return true    Initialize success, app continue.
    @return false   Initialize failed, app terminate.
    */
    virtual bool applicationDidFinishLaunching() = 0;

    /**
    @brief  The function be called when the application enter background
    @param  the pointer of the application
    */
    virtual void applicationDidEnterBackground() = 0;

    /**
    @brief  The function be called when the application enter foreground
    @param  the pointer of the application
    */
    virtual void applicationWillEnterForeground() = 0;

    /**
    @brief    Callback by CCDirector for limit FPS.
    @interval       The time, which expressed in second in second, between current frame and next. 
    */
    virtual void setAnimationInterval(double interval) = 0;

    /**
    @brief Get current language config
    @return Current language config
    */
    virtual ccLanguageType getCurrentLanguage() = 0;
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)    
	/**
     @brief Get target platform
     */
    virtual TargetPlatform getTargetPlatform() = 0;
#endif
    virtual bool isIpad() { return false; }

};

// end of platform group
/// @}

NS_CC_END

#endif    // __CC_APPLICATION_PROTOCOL_H__
