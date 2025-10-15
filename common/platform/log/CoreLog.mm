//  CoreLog.cpp
//  platform

#include "CoreLog.h"

#include "StringUtil.h"
#include "StringCover.h"
#import "Flurry.h"
#include <vector>
#include <map>

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
    
    /*************************************************************************
     Logs an event
     *************************************************************************/
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
        localtime_r(&now,&t);

        va_list ap;
        va_start(ap,format);
        wchar_t buff[4096+1];
        memset(buff, 0, 4096+1);
        vsnwprintf_x(buff, 4096, format, ap);
        std::wstring LevelString = getLevelString(level);
        wchar_t prefix_buff[4096+128+1];
        SprintfW(prefix_buff, 4096+128, L"%4d-%02d-%02d %02d:%02d:%02d:%ls\t%ls", t.tm_year+1900, t.tm_mon+1,
                 t.tm_mday, t.tm_hour, t.tm_min, t.tm_sec, LevelString.c_str(),buff);        
        
        NSString* nsformat = [NSString stringWithUTF8String:SHARE_Wstring2String(prefix_buff).c_str()];
//        [Crittercism leaveBreadcrumb:nsformat];
        NSLog(nsformat);
        va_end(ap);
        NSRange rang = [nsformat rangeOfString:@"[LUA ERROR]"];
        if (rang.location != NSNotFound) {
            std::wstring wsw = buff;
            flurryError(L"LUA_ERROR",L"Message",wsw,true);
        }
        
        if(d_write2file)
        {
            d_workstream.str("");
            d_workstream << SHARE_Wstring2String(prefix_buff) << std::endl;
            
            // write message
            d_ostream << d_workstream.str();
            // ensure new event is written to the file, rather than just being buffered.
            d_ostream.flush();
        }

        
    }
    
    /*************************************************************************
     Logs an string event
     *************************************************************************/
    void Logger::logLuaEvent( LoggingLevel level, std::wstring message)
    {
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
        localtime_r(&now,&t);      
        std::wstring LevelString = getLevelString(level);
        wchar_t prefix_buff[4096+128+1];
        SprintfW(prefix_buff, 4096+128, L"%4d-%02d-%02d %02d:%02d:%02d:%ls\t%ls", t.tm_year+1900, t.tm_mon+1,
                 t.tm_mday, t.tm_hour, t.tm_min, t.tm_sec, LevelString.c_str(),message.c_str());
        
        NSString* nsformat = [NSString stringWithUTF8String:SHARE_Wstring2String(prefix_buff).c_str()];
//        [Crittercism leaveBreadcrumb:nsformat];
        NSLog(nsformat);
        if(d_write2file)
        {
            d_workstream.str("");
            d_workstream << SHARE_Wstring2String(prefix_buff) << std::endl;
            
            // write message
            d_ostream << d_workstream.str();
            // ensure new event is written to the file, rather than just being buffered.
            d_ostream.flush();
        }
    }


    std::wstring Logger::getLevelString(LoggingLevel level)
    {
        // write event type code
        
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
        // close current log file (if any)
        if (d_ostream.is_open())
        {
            d_ostream.close();
        }
        
        d_ostream.open(filename.c_str(), std::ios_base::out | (append ? std::ios_base::app : std::ios_base::trunc));
        
        if (!d_ostream)
        {
            return;
        }
        
        // initialise width for date & time alignment.
        d_ostream.width(2);
        
        d_write2file = true;
    }
    bool Logger::isfirstInstall = false;
    void    Logger::flurryEvent(std::wstring s,bool once)
    {
        /*
        if (s.empty()){
            return;
        }
        static std::vector<std::string> logOnce;
        std::string str = SHARE_Wstring2String(s);
        if(once){
            std::vector<std::string>::iterator result = std::find(logOnce.begin(), logOnce.end(), str);
            if (result == logOnce.end()) {
                logOnce.push_back(str);
                s = L"logFirst_" + s;
            }
        }
        if (isfirstInstall) {
            s = L"firstInstall_" + s;
        }
        NSString *event = [[NSString alloc] initWithBytes:s.data() length:s.length()*sizeof(wchar_t) encoding:NSUTF32LittleEndianStringEncoding];

          [Flurry logEvent:event];
        */
    }
    
    
    void    Logger::flurryEvent(std::wstring s,std::wstring k , std::wstring v ,bool once)
    {
        /*
        if (s.empty() || k.empty() || v.empty() ){
            return;
        }
        static std::vector<std::string> logOnce;
        std::string str = SHARE_Wstring2String(s);
        if(once){
            std::vector<std::string>::iterator result = std::find(logOnce.begin(), logOnce.end(), str);
            if (result == logOnce.end()) {
                logOnce.push_back(str);
                s = L"logFirst_" + s;
            }
        }
        if (isfirstInstall) {
            s = L"firstInstall_" + s;
        }
        NSString* key = [[NSString alloc] initWithBytes:k.data() length:k.length()*sizeof(wchar_t) encoding:NSUTF32LittleEndianStringEncoding];
        NSString* value = [[NSString alloc] initWithBytes:v.data() length:v.length()*sizeof(wchar_t) encoding:NSUTF32LittleEndianStringEncoding];
        NSDictionary *dict = [NSDictionary dictionaryWithObjectsAndKeys:value,key, nil];
        
        NSString *event = [[NSString alloc] initWithBytes:s.data() length:s.length()*sizeof(wchar_t) encoding:NSUTF32LittleEndianStringEncoding];
        [Flurry logEvent:event withParameters:dict];
      */
        
    }
    
    void Logger::flurryError(std::wstring s , std::wstring key , std::wstring value, bool notRepeatSameError)
    {
        /*
        if (s.empty() || key.empty() || value.empty())return;
        static std::vector<std::string> repeat;
      
        std::string str = SHARE_Wstring2String(value);
        std::string substr = str.substr(11,100);
        if (notRepeatSameError){
            str  = SHARE_Wstring2String(value);
            std::vector<std::string>::iterator result = std::find(repeat.begin(), repeat.end(), substr);
            if (result != repeat.end())return;
            repeat.push_back(substr);
        }
        flurryEvent(s,key ,value);
    */
    
    }
    
    
    
    
    
}