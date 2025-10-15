//  IOSDeviceInfo.mm
//  FireClient

#import "IOSDeviceInfo.h"
#import <sys/sysctl.h>
#import <mach/mach.h>
#include "../Application/Framework/DeviceInfo.h"

@implementation IOSDeviceInfo

#pragma mark sysctl utils

+ (NSString *) getSysInfoByName:(const char *)typeSpecifier
{
    size_t size;
    sysctlbyname(typeSpecifier, NULL, &size, NULL, 0);
    char *answer = (char*)malloc(size);
    sysctlbyname(typeSpecifier, answer, &size, NULL, 0);
    NSString *results = [NSString stringWithCString:answer encoding:NSUTF8StringEncoding];
    free(answer);
    return results;
}

+ (NSUInteger) getSysInfo: (uint) typeSpecifier
{
    size_t size = sizeof(int);
    int results;
    int mib[2] = {CTL_HW, typeSpecifier};
    sysctl(mib, 2, &results, &size, NULL, 0);
    return (NSUInteger)results;
}

+ (NSUInteger) getCPUFrequency
{
    return [IOSDeviceInfo getSysInfo:HW_CPU_FREQ];
}

+ (NSUInteger) getCPUCount
{
    return [IOSDeviceInfo getSysInfo:HW_NCPU];
}

+ (NSUInteger) getTotalMemorySize
{
    return [IOSDeviceInfo getSysInfo:HW_PHYSMEM];
}

+ (float) getProcessCpuRate
{
	kern_return_t kr;
	task_info_data_t tinfo;
	mach_msg_type_number_t task_info_count;

	task_info_count = TASK_INFO_MAX;
	
	kr = task_info(mach_task_self(), TASK_BASIC_INFO, (task_info_t)tinfo, &task_info_count);
    if (kr != KERN_SUCCESS) {
        return -1;
    }

	task_basic_info_t      basic_info;
	thread_array_t         thread_list;
	mach_msg_type_number_t thread_count;

    thread_info_data_t     thinfo;
    mach_msg_type_number_t thread_info_count;

    thread_basic_info_t basic_info_th;

    uint32_t stat_thread = 0; // Mach threads

    basic_info = (task_basic_info_t)tinfo;

    // get threads in the task
    kr = task_threads(mach_task_self(), &thread_list, &thread_count);
    if (kr != KERN_SUCCESS) {
        return -1;
    }

    if (thread_count > 0) {
        stat_thread += thread_count;
	}

    long tot_sec = 0;
    long tot_usec = 0;
    float tot_cpu = 0;

	int j;
	for (j = 0; j < thread_count; j++)
	{
        thread_info_count = THREAD_INFO_MAX;

        kr = thread_info(thread_list[j], THREAD_BASIC_INFO, (thread_info_t)thinfo, &thread_info_count);
		if (kr != KERN_SUCCESS) {
            return -1;
        }

        basic_info_th = (thread_basic_info_t)thinfo;
        if (!(basic_info_th->flags & TH_FLAGS_IDLE)) {
            tot_sec = tot_sec + basic_info_th->user_time.seconds + basic_info_th->system_time.seconds;
            tot_usec = tot_usec + basic_info_th->system_time.microseconds + basic_info_th->system_time.microseconds;
            tot_cpu = tot_cpu + basic_info_th->cpu_usage / (float)TH_USAGE_SCALE * 100.0;
        }
    } // for each thread

    kr = vm_deallocate(mach_task_self(), (vm_offset_t)thread_list, thread_count * sizeof(thread_t));
    assert(kr == KERN_SUCCESS);

    return tot_cpu;
}

@end

//const char* CDeviceInfo::GetIOSDevInfoFromGivenName(const char *typeSpecifier)
//{
//    NSString *strResult = [IOSDeviceInfo getSysInfoByName:typeSpecifier];
//    return [strResult UTF8String];
//}

//unsigned long long CDeviceInfo::GetIOSDevInfoFromGivenType(unsigned int typeSpecifier)
//{
//    unsigned long long valueInfo = (unsigned long long)[IOSDeviceInfo getSysInfo:typeSpecifier];
//    return valueInfo;
//}

void DeviceInfo::sInitDeviceInfo()
{
    int deviceType = DeviceInfo::sGetDeviceType();
    
    switch (deviceType) {
        
        case 0: //simulator
        {
            // best performance
            DeviceInfo::s_nCpuFrequency = 2048; //MHZ
            DeviceInfo::s_nCpuCount = 4;
            DeviceInfo::s_nTotalMemSize = 32768; //MBytes, 32GB
        }
        break;
            
        case 1: //ios device
        {
            uint64_t lReg = (uint64_t)[IOSDeviceInfo getCPUFrequency];
            DeviceInfo::s_nCpuFrequency = (int)(lReg/(1000*1000));
            lReg = (uint64_t)[IOSDeviceInfo getTotalMemorySize];
            DeviceInfo::s_nTotalMemSize = (int)(lReg/(8*1024*1024));
            DeviceInfo::s_nCpuCount = (int)[IOSDeviceInfo getCPUCount];
        }
        break;
            
        default: //android
        {
            assert(deviceType==2);
            
            // according to iphone 4s standard
            DeviceInfo::s_nCpuFrequency = 800; //MHZ
            DeviceInfo::s_nCpuCount = 2;
            DeviceInfo::s_nTotalMemSize = 16384; //MBytes, 16GB
        }
        break;
    }
}

float DeviceInfo::sGetAppCpuRate()
{
	return [IOSDeviceInfo getProcessCpuRate];
}

int DeviceInfo::sGetCpuFrequency()
{
    if(DeviceInfo::s_nCpuFrequency == 0)
    {
        int deviceType = DeviceInfo::sGetDeviceType();
        switch (deviceType) {
                
            case 0: //simulator
            {
                // best performance
                DeviceInfo::s_nCpuFrequency = 2048; //MHZ
            }
            break;
                
            case 1: //ios device
            {
                uint64_t lReg = (uint64_t)[IOSDeviceInfo getCPUFrequency];
                DeviceInfo::s_nCpuFrequency = (int)(lReg/(1000*1000));
            }
            break;
                
            default: //android
            {
                assert(deviceType==2);
                
                // according to iphone 4s standard
                DeviceInfo::s_nCpuFrequency = 800; //MHZ
            }
            break;
        }
    }
    
    return DeviceInfo::s_nCpuFrequency;
}

int DeviceInfo::sGetCpuCount()
{
    if(DeviceInfo::s_nCpuCount == 0)
    {
        int deviceType = DeviceInfo::sGetDeviceType();
        
        switch (deviceType) {
                
            case 0: //simulator
            {
                // best performance
                DeviceInfo::s_nCpuCount = 4;
            }
            break;
                
            case 1: //ios device
            {
                DeviceInfo::s_nCpuCount = (int)[IOSDeviceInfo getCPUCount];
            }
            break;
                
            default: //android
            {
                assert(deviceType==2);
                
                // according to iphone 4s standard
                DeviceInfo::s_nCpuCount = 2;
            }
            break;
        }
    }
    
    return DeviceInfo::s_nCpuCount;
}

int DeviceInfo::sGetTotalMemSize()
{
    if(DeviceInfo::s_nTotalMemSize == 0)
    {
        int deviceType = DeviceInfo::sGetDeviceType();
        
        switch (deviceType) {
                
            case 0: //simulator
            {
                // best performance
                DeviceInfo::s_nTotalMemSize = 32768; //MBytes, 32GB
            }
            break;
                
            case 1: //ios device
            {
                uint64_t lReg = (uint64_t)[IOSDeviceInfo getTotalMemorySize];
                DeviceInfo::s_nTotalMemSize = (int)(lReg/(8*1024*1024));
            }
            break;
                
            default: //android
            {
                assert(deviceType==2);
                
                // according to iphone 4s standard
                DeviceInfo::s_nTotalMemSize = 16384; //MBytes, 16GB
            }
            break;
        }
    }
    
    return DeviceInfo::s_nTotalMemSize;
}

int DeviceInfo::sGetFreeMemSize()
{
    //later fullfill this function
    return 1;
}

int DeviceInfo::sGetMaxCpuFreq()
{
    //later fullfill this function
    return DeviceInfo::s_nMaxCpuFreq;
}

const char* DeviceInfo::sGetCpuName()
{
    //later fullfill this function
    return DeviceInfo::s_strCpuName.c_str();
}

const char* DeviceInfo::sGetOSVersion()
{
    //later fullfill this function
    return DeviceInfo::s_strOSVersion.c_str();
}

const char* DeviceInfo::sGetMacAddress()
{
    /*
     static NSString* getMacAddress()
     {
     int                 mgmtInfoBase[6];
     char                *msgBuffer = NULL;
     NSString            *errorFlag = NULL;
     size_t              length;
     
     // Setup the management Information Base (mib)
     mgmtInfoBase[0] = CTL_NET;        // Request network subsystem
     mgmtInfoBase[1] = AF_ROUTE;       // Routing table info
     mgmtInfoBase[2] = 0;
     mgmtInfoBase[3] = AF_LINK;        // Request link layer information
     mgmtInfoBase[4] = NET_RT_IFLIST;  // Request all configured interfaces
     
     // With all configured interfaces requested, get handle index
     if ((mgmtInfoBase[5] = if_nametoindex("en0")) == 0)
     errorFlag = @"if_nametoindex failure";
     // Get the size of the data available (store in len)
     else if (sysctl(mgmtInfoBase, 6, NULL, &length, NULL, 0) < 0)
     errorFlag = @"sysctl mgmtInfoBase failure";
     // Alloc memory based on above call
     else if ((msgBuffer = (char*)malloc(length)) == NULL)
     errorFlag = @"buffer allocation failure";
     // Get system information, store in buffer
     else if (sysctl(mgmtInfoBase, 6, msgBuffer, &length, NULL, 0) < 0)
     {
     free(msgBuffer);
     errorFlag = @"sysctl msgBuffer failure";
     }
     else
     {
     // Map msgbuffer to interface message structure
     struct if_msghdr *interfaceMsgStruct = (struct if_msghdr *) msgBuffer;
     
     // Map to link-level socket structure
     struct sockaddr_dl *socketStruct = (struct sockaddr_dl *) (interfaceMsgStruct + 1);
     
     // Copy link layer address data in socket structure to an array
     unsigned char macAddress[6];
     memcpy(&macAddress, socketStruct->sdl_data + socketStruct->sdl_nlen, 6);
     
     // Read from char array into a string object, into traditional Mac address format
     // NSString *macAddressString = [NSString stringWithFormat:@"%02X:%02X:%02X:%02X:%02X:%02X",
     NSString *macAddressString = [NSString stringWithFormat:@"%02X%02X%02X%02X%02X%02X",
     macAddress[0], macAddress[1], macAddress[2], macAddress[3], macAddress[4], macAddress[5]];
     NSLog(@"Mac Address: %@", macAddressString);
     
     // Release the buffer memory
     free(msgBuffer);
     
     return macAddressString;
     }
     
     // Error...
     NSLog(@"Error: %@", errorFlag);
     
     return nil;
     }
     //////////////////////////////////////////////////////////////////////////////////////
     //////////////////////////////////////////////////////////////////////////////////////
     
     */
    return "4a8e5b6a";
}


const char* DeviceInfo::sGetProductModel()
{
    return "";
}

const char* DeviceInfo::sGetNetEnvir()
{
    return "";
}

const char* DeviceInfo::sGetScreenWidthInfo()
{
    return "";
}

const char* DeviceInfo::sGetScreenHeightInfo()
{
    return "";
}

const char* DeviceInfo::sGetDeviceID()
{
    return "";
}

const char* DeviceInfo::sGetProvider()
{
    return "";
}

