#ifndef __FireClient__ChannlePlatformUtil__
#define __FireClient__ChannlePlatformUtil__

#include "./ChannelPlatformInterface.h"

#include <stdarg.h>
#include <string>

template<typename F>
inline std::string channelPlatformGetString(F f)
{
    char * buffer = new char[JNI_STRING_MAX_SIZE];
    f(buffer);
    std::string result(buffer);
    delete buffer;
    
    return result;
}

inline std::wstring channelPlatformGetUserName()
{
    std::string s = channelPlatformGetString(GetChannelPlatformInterface()->GetUserName);
    return StringCover::to_wstring(s);
}

inline std::wstring channelPlatformGetUserID()
{
    std::string s = channelPlatformGetString(GetChannelPlatformInterface()->GetUserID);
    return StringCover::to_wstring(s);
}

inline std::wstring channelPlatformGetSessionId()
{
    std::string s = channelPlatformGetString(GetChannelPlatformInterface()->GetSessionId);
    return StringCover::to_wstring(s);
}

inline std::wstring channelPlatformGetPlatformID()
{
    std::string s = channelPlatformGetString(GetChannelPlatformInterface()->GetPlatformID);
    return StringCover::to_wstring(s);
}

inline std::wstring channelPlatformGetPlatformCenterName()
{
    std::string s = channelPlatformGetString(GetChannelPlatformInterface()->GetPlatformCenterName);
    return StringCover::to_wstring(s);
}

inline std::wstring channelPlatformGetPlatformForumName()
{
    std::string s = channelPlatformGetString(GetChannelPlatformInterface()->GetPlatformForumName);
    return StringCover::to_wstring(s);
}

inline std::wstring ChannelPlatformGetPlatformFeedbackName()
{
    std::string s = channelPlatformGetString(GetChannelPlatformInterface()->GetPlatformFeedbackName);
    return StringCover::to_wstring(s);
}

// TODO: should put this function definition to .cpp file
inline std::string makeSimpleJson(int paramCount, ...)
{
    assert(paramCount % 2 == 0);
    
    va_list argPointer;
    va_start(argPointer, paramCount);
    std::string result;
    for(int i = 0; i < paramCount; i += 2) {
        char * key = va_arg(argPointer, char *);
        char * value = va_arg(argPointer, char *);
        if(result.size() > 0) {
            result = result + ",";
        }
        result = result + "\"" + std::string(key) + "\":\"" + std::string(value) + "\"";
    }
    va_end(argPointer);
    return std::string("{") + result + std::string("}");
}


#endif
