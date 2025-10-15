#include "platform_types.h"
#include "../utils/StringUtil.h"
#include <cassert>
#include <fstream>
#include <cstdio>

#ifdef _OS_IOS
int _wtoi(const wchar_t* str)
{
    int ret = 0;
    std::string str_s = ws2s(str);
    sscanf(str_s.c_str(), "%d", &ret);
    return ret;
}

float _wtof(const wchar_t* str)
{
    float ret = 0.0f;
    std::string str_s = ws2s(str);
    sscanf(str_s.c_str(), "%f", &ret);
    return ret;
}

int64 _wtoi64(const wchar_t* str)
{
    int64 ret = 0;
    std::string str_s = ws2s(str);
    sscanf(str_s.c_str(), "%lld", &ret);
    return ret;
}

long _wtol(const wchar_t* str)
{
    long ret = 0;
    std::string str_s = ws2s(str);
    sscanf(str_s.c_str(), "%ld", &ret);
    return ret;
}

unsigned int _wtou(const wchar_t* str)
{
    long ret = 0;
    std::string str_s = ws2s(str);
    sscanf(str_s.c_str(), "%lu", &ret);
    return ret;
}
#elif (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
unsigned int _wtou(const wchar_t* str)
{
    long ret = 0;
    std::string str_s = ws2s(str);
    sscanf(str_s.c_str(), "%lu", &ret);
    return ret;
}
#endif
