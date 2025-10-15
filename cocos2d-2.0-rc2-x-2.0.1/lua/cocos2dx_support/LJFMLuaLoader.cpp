//#include "stdafx.h"
#include "LJFMLuaLoader.h"
#include <string>
#include "cocos2d.h"
#include "ljfmext.h"
#include "utils/StringUtil.h"
#include "log/CoreLog.h"

#ifdef ANDROID
#include <fcntl.h>
#include <errno.h>
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

extern "C"
{
    int loader_LJFM(lua_State *L)
    {
        std::string filename(luaL_checkstring(L, 1));
        
        //replace "." with "/"
        std::string::size_type pos(0);
        std::string old_value = ".";
        while ((pos =filename.find(old_value)) != std::string::npos)
        {
            filename.replace(pos, old_value.length(), "/");
        }
        
        filename.append(".lua");

		LOGD("filename:%s", std::string(filename.begin(), filename.end()).c_str());
        
        std::wstring LJFMname = L"/script/" + SHARE_String2Wstring(filename);
#ifndef NoPack
        std::transform(LJFMname.begin(),LJFMname.end(),LJFMname.begin(),::tolower);
#else
#endif

        LJFM::LJFMF LJFMfile;
        if( !LJFMfile.Open(LJFMname, LJFM::FM_EXCL, LJFM::FA_RDONLY ))
        {
			std::string strOut = "can not get file data of ";
			strOut += filename.c_str();
			strOut += '\n';
#if (defined WIN32) && _DEBUG
			::OutputDebugStringA(strOut.c_str());
#endif
			SDLOG_ERR(L"can not get file data of %s", strOut.c_str());
			LOGD("can not get file data %s", strOut.c_str());
            return -1;
        }
            
        int file_size = (int)LJFMfile.GetSize();
        if( file_size == 0 )
        {
#if (defined WIN32) && _DEBUG
			std::string strOut = "can not get file data of ";
			strOut += filename.c_str();
			strOut += '\n';
			::OutputDebugStringA(strOut.c_str());
#endif
			LOGD("file size is zero!!!");
            return -1;
        }
        LJFM::LJFMID output = LJFMfile.GetImage();
        
        // load code into lua
        int loaderr = luaL_loadbuffer(L,
                                      reinterpret_cast<const char*>(output.GetData()),
                                      output.GetSize(), filename.c_str());
        
        int top = lua_gettop(L);
        if (loaderr)
        {
#if (defined WIN32) && _DEBUG
			std::string strOut = "[LUA ERROR] ";
			strOut += lua_tostring(L, -1);
			strOut += '\n';
			::OutputDebugStringA(strOut.c_str());
#endif
            lua_settop(L,top);
            return loaderr;
        }

        return 1;
    }
}
