//  CoreLog.h

#ifndef __CORE_LOG__
#define __CORE_LOG__


#define SDLOG_ERR(__FORMAT__, ...) core::Logger::GetInstance()->logEvent(core::Errors, __FORMAT__, ##__VA_ARGS__)
#define SDLOG_WARN(__FORMAT__, ...) core::Logger::GetInstance()->logEvent(core::Warnings, __FORMAT__, ##__VA_ARGS__)
#define SDLOG_STD(__FORMAT__, ...) core::Logger::GetInstance()->logEvent(core::Standard, __FORMAT__, ##__VA_ARGS__)
#define SDLOG_INFO(__FORMAT__, ...) core::Logger::GetInstance()->logEvent(core::Informative, __FORMAT__, ##__VA_ARGS__)
#define SDLOG_INSANE(__FORMAT__, ...) core::Logger::GetInstance()->logEvent(core::Insane, __FORMAT__, ##__VA_ARGS__) 

#define SDLOG_CUSTOM(Level, __FORMAT__, ...) core::Logger::GetInstance()->logEventInt(Level, __FORMAT__, ##__VA_ARGS__)
#define SDLOG_PASS(Level, __FORMAT__, ...) core::Logger::GetInstance()->AddPassLevel(Level, L""); core::Logger::GetInstance()->logEventInt(Level, __FORMAT__, ##__VA_ARGS__)
#define SDLOG_ADDPASSLEVEL(Level, LevelCaption) core::Logger::GetInstance()->AddPassLevel(Level)

//#ifdef	_DEBUG
//#define	LOG_OutLog_Info(...)
//#endif

#include <fstream>
#include <sstream>
#include "../Singleton.hpp"
#include <map>

namespace core
{  

	/*!
	 \brief
	 Enumeration of logging levels
	 */
	enum LoggingLevel
	{
		Errors,			//!< Only actual error conditions will be logged.
		Warnings,       //!< Warnings will be logged as well.
		Standard,		//!< Basic events will be logged (default level).
		Informative,	//!< Useful tracing (object creations etc) information will be logged.
		Insane			//!< Mostly everything gets logged (use for heavy tracing only, log WILL be big).
	};

	/*!
	 \brief
	 Abstract class that defines the interface of a logger object for the GUI system.
	 The default implementation of this interface is the DefaultLogger class; if you
	 want to perform special logging, derive your own class from Logger and initialize
	 a object of that type before you create the CEGUI::System singleton.
	 */
	class  Logger : public CSingleton <Logger>
	{
	public:

		std::map<int, std::wstring> m_PassLevel;
		int AddPassLevel(int PassLevel, std::wstring PassLevelCaption);
		std::wstring FindPassLevel(int PassLevel);
 
	public:
		/*!
		 \brief
		 Constructor for Logger object.
		 */
		Logger(void);
    
		/*!
		 \brief Destructor for Logger object.
		 */
		virtual ~Logger(void);
   
		/*!
		 \brief
		 Set the level of logging information that will get out to the log file
     
		 \param level
		 One of the LoggingLevel enumerated values that specified the level of logging information required.
     
		 \return
		 Nothing
		 */
		void	setLoggingLevel(LoggingLevel level)		{d_level = level;}

		void    setLogFilename(const std::string& filename, bool append);
		static  bool isfirstInstall;
		static void flurryEvent(std::wstring s , bool remarkFirst = false);
		static void flurryEvent(std::wstring s , std::wstring key , std::wstring value,  bool remarkFirst = false);
		static void flurryError(std::wstring s , std::wstring key , std::wstring value, bool notRepeatSameError=true);
    
    
		/*!
		 \brief
		 return the current logging level setting
     
		 \return
		 One of the LoggingLevel enumerated values specifying the current level of logging
		 */
		LoggingLevel	getLoggingLevel(void) const		{return d_level;}
    
    
		/*!
		 \brief
		 Add an event to the log.
     
		 \param message
		 String object containing the message to be added to the event log.
     
		 \param level
		 LoggingLevel for this message.  If \a level is greater than the current set logging level, the message is not logged.
     
		 \return
		 Nothing
		 */
		virtual void logEvent( LoggingLevel level, const wchar_t* format, ...);
    
		virtual void logLuaEvent( LoggingLevel level, std::wstring message);//only for lua

		virtual void logEventInt(int level, const wchar_t* format, ...) { logEvent((LoggingLevel)level, format); }
		
		virtual void logLuaEventInt(int level, std::wstring message) { logLuaEvent((LoggingLevel)level, message); }//only for lua
    
	private:
  
		std::wstring getLevelString(LoggingLevel level);
    
	protected:
		//for file
		std::ofstream	d_ostream;		//!< Stream used to implement the logger
		std::ostringstream d_workstream;//!< Used to build log entry strings.

		LoggingLevel	d_level;		//!< Holds current logging level
    
		bool            d_write2file;        
    };
    
    inline Logger* GetCoreLogger()
    {
        return Logger::GetInstance();
    }
}//end of namespace mt3



#endif /* defined(__MT3_LOG__) */
