//  CoreLog.cpp

#include "CoreLog.h"

#include "../utils/StringUtil.h"
#include "../utils/StringCover.h"

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <windows.h>
#include <time.h>
#include <stdarg.h>
#else
#include "../../common/platform/android/SDJniHelper.h"
#include <jni.h>
#include <vector>
#include "StringCover.h"
#endif

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

namespace core
{
    /*************************************************************************
     Constructor
     *************************************************************************/
    Logger::Logger(void) :
    d_level(Errors),d_write2file(false)
    {
    }
    
    /*************************************************************************
     Destructor
     *************************************************************************/
    Logger::~Logger(void)
    {
        if (d_ostream.is_open())
        {
            d_ostream.close();
        }
		m_PassLevel.clear();
    }

	int Logger::AddPassLevel(int PassLevel, std::wstring PassLevelCaption)
	{
		if (m_PassLevel.find(PassLevel) != m_PassLevel.end())
		{
			return 1;
		}
		if (PassLevelCaption == L"")
		{
			PassLevelCaption = StringCover::intTowstring(PassLevel);
		}
		m_PassLevel[PassLevel] = PassLevelCaption;
		return 0;
	}
	std::wstring Logger::FindPassLevel(int PassLevel)
	{
		std::map<int, std::wstring>::iterator it = m_PassLevel.find(PassLevel);
		if (it != m_PassLevel.end())
		{
			return it->second;
		}
		return L"";
	}
    
	void    Logger::flurryEvent(std::wstring s, bool once)
	{
#ifdef ANDROID
#ifdef _3RD_PLATFORM

		if (s.empty()){
			return;
		}
		static std::vector<std::string> logOnce;
		std::string str = SHARE_Wstring2String(s);
		if (once){
			std::vector<std::string>::iterator result = std::find(logOnce.begin(), logOnce.end(), str);
			if (result == logOnce.end()) {
				logOnce.push_back(str);
				s = L"logFirst_" + s;
			}
		}
		core::JniMethodInfo t;
		if (core::JniHelper::getStaticMethodInfo(t,
			"com/locojoy/mini/mt3/StatisticUtil",
			"flurryEvent",
			"(Ljava/lang/String;)V"))
		{
			jstring stringArg = t.env->NewStringUTF(StringCover::SUNWstring2String(s).c_str());
			t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg);
			t.env->DeleteLocalRef(t.classID);
			t.env->DeleteLocalRef(stringArg);
		}
#endif
#endif
	}

	void    Logger::flurryEvent(std::wstring s, std::wstring k, std::wstring v, bool once)
	{
#ifdef ANDROID
#ifdef _3RD_PLATFORM
		if (s.empty() || k.empty() || v.empty()){
			return;
		}
		static std::vector<std::string> logOnce;
		std::string str = SHARE_Wstring2String(s);
		if (once){
			std::vector<std::string>::iterator result = std::find(logOnce.begin(), logOnce.end(), str);
			if (result == logOnce.end()) {
				logOnce.push_back(str);
				s = L"logFirst_" + s;
			}
		}


		core::JniMethodInfo t;
		if (core::JniHelper::getStaticMethodInfo(t,
			"com/locojoy/mini/mt3/StatisticUtil",
			"flurryEvent",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V"))
		{
			jstring stringArg = t.env->NewStringUTF(StringCover::SUNWstring2String(s).c_str());
			jstring stringArg1 = t.env->NewStringUTF(StringCover::SUNWstring2String(k).c_str());
			jstring stringArg2 = t.env->NewStringUTF(StringCover::SUNWstring2String(v).c_str());

			t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg, stringArg1, stringArg2, false);
			t.env->DeleteLocalRef(t.classID);
			t.env->DeleteLocalRef(stringArg);
			t.env->DeleteLocalRef(stringArg1);
			t.env->DeleteLocalRef(stringArg2);
		}
#endif
#endif
	}

	void Logger::flurryError(std::wstring s, std::wstring key, std::wstring value, bool notRepeatSameError)
	{
#ifdef ANDROID
#ifdef _3RD_PLATFORM
		if (s.empty() || key.empty() || value.empty())return;
		static std::vector<std::string> repeat;

		std::string str = SHARE_Wstring2String(value);
		std::string substr = str.substr(11, 100);
		if (notRepeatSameError){
			str = SHARE_Wstring2String(value);
			std::vector<std::string>::iterator result = std::find(repeat.begin(), repeat.end(), substr);
			if (result != repeat.end())return;
			repeat.push_back(substr);
		}
		flurryEvent(s, key, value);
#endif
#endif
	}

    /*************************************************************************
     Logs an event
     *************************************************************************/
	static std::string s_StrLastInfo = "";
    void Logger::logEvent( LoggingLevel level, const wchar_t* format, ...)
    {
		level = (LoggingLevel)(0xFFFF & level);
		if (level > d_level)
		{
			if (FindPassLevel(level) == L"")
			{
				return;
			}
		}
		if (!format)
		{
			return;
		}
		
        time_t now = time(NULL);
        struct tm t;
#if (defined WIN7_32) || ((defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
		localtime_s(&t, &now);
#else
        localtime_r(&now,&t);
#endif
        va_list ap;
        va_start(ap,format);
        wchar_t buff[4096+1];
        memset(buff, 0, 4096+1);
        vsnwprintf_x(buff, 4096, format, ap);
		std::wstring LevelString = getLevelString(level);
        wchar_t prefix_buff[4096+128+1];
        SprintfW(prefix_buff, 4096+128, L"%4d-%02d-%02d %02d:%02d:%02d:%ls\t%ls", t.tm_year+1900, t.tm_mon+1,
			t.tm_mday, t.tm_hour, t.tm_min, t.tm_sec, LevelString.c_str(), buff);

		//LOGD(SHARE_Wstring2String(prefix_buff).c_str() );//shield by yyy
        
        va_end(ap);

        
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
        std::wstring wsw = buff;
        std::size_t pos = wsw.find(L"[LUA ERROR]");
        if(pos != std::string::npos){
             flurryError(L"LUA_ERROR",L"Message",wsw,true);
        }
#endif        
        
		std::string strInfo = SHARE_Wstring2String(prefix_buff);
		if (s_StrLastInfo.compare(strInfo) != 0) {
			if (d_write2file)
			{
				d_workstream.str("");
				d_workstream << strInfo << std::endl;

				d_ostream << d_workstream.str();
				d_ostream.flush();
			}
			s_StrLastInfo = strInfo;
		}
    }
    
    /*************************************************************************
     Logs an string event
     *************************************************************************/
    void Logger::logLuaEvent( LoggingLevel level, std::wstring message)
    {
#ifdef ANDROID
		LOGE("Logger::logLuaEvent level=%d, message=%s", level, ws2s(message.c_str()).c_str());
#endif
		level = (LoggingLevel)(0xFFFF & level);
		if (level > d_level)
		{
			if (FindPassLevel(level) == L"")
			{
				return;
			}
		}
        
        time_t now = time(NULL);
        struct tm t;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		localtime_s(&t, &now);
#else
        localtime_r(&now,&t);
#endif
		std::wstring LevelString = getLevelString(level);
        wchar_t prefix_buff[4096+128+1];
        SprintfW(prefix_buff, 4096+128, L"LUA:%4d-%02d-%02d %02d:%02d:%02d:%ls\t%ls", t.tm_year+1900, t.tm_mon+1,
			t.tm_mday, t.tm_hour, t.tm_min, t.tm_sec, LevelString.c_str(), message.c_str());

        if(d_write2file)
        {
            d_workstream.str("");
            d_workstream << SHARE_Wstring2String(prefix_buff) << std::endl;
            
            d_ostream << d_workstream.str();
            d_ostream.flush();
        }
    }

	std::wstring Logger::getLevelString(LoggingLevel level)
    {
		std::wstring LevelCaption = FindPassLevel(level);
		if (LevelCaption != L"")
		{
			return LevelCaption;
		}

        switch(level)
        {
            case Errors:
                return L"(Error)";
                
            case Warnings:
                return L"(Warn)";
                
            case Standard:
                return  L"(Std)";
                
            case Informative:
                return L"(Info)";
                
            case Insane:
                return L"(Insan)";
                
			default:
                return L"(Unkwn)";
        }
    }
    
    void Logger::setLogFilename(const std::string& filename, bool append)
    {
        if (d_ostream.is_open())
        {
            d_ostream.close();
        }
        
        d_ostream.open(filename.c_str(), std::ios_base::out | (append ? std::ios_base::app : std::ios_base::trunc));
        
        if (!d_ostream)
        {
            return;
        }
        
        d_ostream.width(2);
        
        d_write2file = true;
    }

}
