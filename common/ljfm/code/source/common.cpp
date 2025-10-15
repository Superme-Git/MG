 #if defined(_OS_IOS)

#include "../include/common.h" 
    
namespace LJFM
{
        
#define __FUNCTIONW__
        
    bool CheckDirFileStringFormat( const std::wstring& dir)
    {
        if( dir.empty() || L'/' != dir[0] || 1 == dir.size() || L'/' == dir[dir.size() - 1])
        {
            LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
            return false;
        }
        else if( std::find_if( dir.begin(), dir.end(), ::iswupper) != dir.end())
        {
            LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
            return false;
        }
            
        if( std::find( dir.begin(), dir.end(), L'\\') != dir.end())
        {
            LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
            return false;
        }
            
        return true;
    }
        
    bool CheckDirFileStringFormatEx( const std::wstring& dir, std::wstring& dirNew )
    {
        if( dir.empty() || L'/' != dir[0] || 1 == dir.size() || L'/' == dir[dir.size() - 1])
        {
            LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
            return false;
        }
            
        dirNew.resize(dir.size());
#ifndef NoPack
		std::transform( dir.begin(), dir.end(), dirNew.begin(), ::towlower);
#else
		dirNew = dir;
#endif
            
        return true;
    }
        
    bool CheckDeviceStringFormat( const std::wstring& device)
    {
        if( device.empty() || L'.' == device[0] )
        {
            LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", device.c_str());
            return false;
        }
            
        if ( device[0] == L'/' )
        {
            if( std::find( device.begin(), device.end(), L'\\') != device.end())
            {
                LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", device.c_str());
                return false;
            }
        }
            
        return true;
    }        
        
	CMutex::CMutex()
    {
        pthread_mutex_init(&mutex, NULL);
    }
	CMutex::~CMutex()
    {
        pthread_mutex_destroy(&mutex);
    }
            
    void CMutex::Lock()
    {
        pthread_mutex_lock(&mutex);
    }
    void CMutex::UNLock()
    {
        pthread_mutex_unlock(&mutex);
    }
            
}
#endif

