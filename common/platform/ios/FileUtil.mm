//  FileUtil.cpp
//  share

#include "FileUtil.h"
#import <UIKit/UIKit.h>

//获取当前设备可用内存及所占内存的头文件
#import <sys/sysctl.h>
#import <mach/mach.h>

#include "Utils.h"

std::string CFileUtil::GetRootDir()
{
	NSString* appPath = [[NSBundle mainBundle] resourcePath];
    return [appPath UTF8String];
}

std::string CFileUtil::GetDocDir()
{
    NSString* documentPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    return [documentPath UTF8String];
}

std::string CFileUtil::MakePath(const char* strPre, const char* strSub)
{
    std::string ret = strPre;
    if (ret.empty()) {
        return ret;
    }
    char* sub = (char*)strSub;
    if (strSub[0] == '/') {
        sub++;
    }
    
    if (ret[ret.length() - 1] == '/') {
        ret += sub;
    }else
    {
        ret = ret + '/' + sub;
    }
    
    return ret;
}

std::string CFileUtil::GetCacheDir()
{
    NSString* cachePath = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    return [cachePath UTF8String];
}

std::string CFileUtil::GetTempDir()
{
    NSString* temPath = NSTemporaryDirectory();
    return [temPath UTF8String];
}

std::string CFileUtil::GetLogDir()
{
	return GetDocDir();
}

bool CFileUtil::IsCurLanguageSimpleCh()
{
    NSArray* languages = [NSLocale preferredLanguages];
    NSString* curLanguages = [languages objectAtIndex:0];
    if([curLanguages isEqualToString:@"zh-Hant"])
    {
        return false;
    }
    return true;
}

// 获取当前设备总内存(单位：MB）
double CFileUtil::GetTotalMemory()
{
  double dResult1 = [NSProcessInfo processInfo].physicalMemory / 1024.0 / 1024.0;
  
  //double dResult2 = NSRealMemoryAvailable() / 1024.0 / 1024.0;
  
  return dResult1;
    
  vm_statistics_data_t vmStats;

  mach_msg_type_number_t infoCount = HOST_VM_INFO_COUNT;

  kern_return_t kernReturn = host_statistics(mach_host_self(), 

                                             HOST_VM_INFO, 

                                             (host_info_t)&vmStats, 

                                             &infoCount); 

  if (kernReturn != KERN_SUCCESS)
  {
    return NSNotFound;
  } 

    
  double dResult3 = (vm_page_size * (vmStats.wire_count + vmStats.active_count + vmStats.inactive_count + vmStats.free_count) / 1024.0) / 1024.0;
  return dResult3;
}


// 获取当前设备可用内存(单位：MB）
double CFileUtil::GetAvailableMemory()
{
  vm_statistics_data_t vmStats;

  mach_msg_type_number_t infoCount = HOST_VM_INFO_COUNT;

  kern_return_t kernReturn = host_statistics(mach_host_self(), 

                                             HOST_VM_INFO, 

                                             (host_info_t)&vmStats, 

                                             &infoCount); 

  if (kernReturn != KERN_SUCCESS)
  {
    return NSNotFound;
  } 

  return ((vm_page_size * vmStats.free_count) / 1024.0) / 1024.0;
}

// 获取当前任务所占用的内存（单位：MB）
double CFileUtil::GetUsedMemory()
{
  task_basic_info_data_t taskInfo;

  mach_msg_type_number_t infoCount = TASK_BASIC_INFO_COUNT;

  kern_return_t kernReturn = task_info(mach_task_self(), 

                                       TASK_BASIC_INFO, 

                                       (task_info_t)&taskInfo, 

                                       &infoCount);

  if (kernReturn != KERN_SUCCESS)
  {
    return NSNotFound;
  }  

  return taskInfo.resident_size / 1024.0 / 1024.0;
}

int CFileUtil::GetFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop, std::vector<std::wstring>& FileArr)
{
    NSString* dirString = [NSString stringWithUTF8String:ws2s(wsBasePath).c_str()];
    NSFileManager* fileMgr = [NSFileManager defaultManager];
    NSArray* tempArray = [fileMgr contentsOfDirectoryAtPath:dirString error:nil];
    for (NSString* fileName in tempArray)
    {
        BOOL flag = YES;
        NSString* fullPath = [dirString stringByAppendingPathComponent:fileName];
        if ([fileMgr fileExistsAtPath:fullPath isDirectory:&flag])
        {
            std::string sCurPath = fullPath.UTF8String;
            std::wstring wsCurPath = s2ws(sCurPath);
            if (!flag)
            {
                if (wsFileType == L"")
                {
                    FileArr.push_back(wsCurPath);
                }
                else
                {
                		if (wsCurPath.size() >= wsFileType.size())
              			{
              					if (wsCurPath.substr(wsCurPath.size() - wsFileType.size()) == wsFileType)
              					{
              							FileArr.push_back(wsCurPath);			
              					}
              			}
                }
            }
            else
            {
                if (bLoop)
                {
                    GetFileArrOfPath(wsCurPath, wsFileType, bLoop, FileArr);
                }
            }
        }
    }
    return FileArr.size();
}

int CFileUtil::DelFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop)
{
	std::vector<std::wstring> FileArr;
	int iResult = GetFileArrOfPath(wsBasePath, wsFileType, bLoop, FileArr);
	
	for (int i = 0; i < iResult; i++)
	{
			MHSD_UTILS::RemoveFile(FileArr[i]);
	}
	
	return iResult;
}