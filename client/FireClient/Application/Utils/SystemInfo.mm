#include "SystemInfo.h"

#import <AdSupport/ASIdentifierManager.h>
#import "CoreTelephony/CTTelephonyNetworkInfo.h"
#import "CoreTelephony/CTCarrier.h"

#include "Reachability.h"
#include <string>
#include "GameApplication.h"
#include "log/CoreLog.h"
#include "../ProtoDef/fire/pb/CNotifyDeviceInfo.hpp"
#include <sys/socket.h>
#include <sys/sysctl.h>
#include <net/if.h>
#include <net/if_dl.h>
#include <sys/utsname.h>
#include "OpenUDID.h"

std::map<std::wstring, std::wstring> MT3SystemInfo::m_MapSystemInfo = std::map<std::wstring, std::wstring>();

static std::wstring NSStringToWstring(NSString* src)
{
    std::wstring dst = (wchar_t*)[src cStringUsingEncoding:NSUTF32StringEncoding];
    return dst;
}

static NSString* getIDFA()
{
    if([[UIDevice currentDevice].systemVersion floatValue] >= 6.0)
    {
        NSString* ret = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
        return ret;
    }
	return nil;
}

static NSString* getMacAddress()
{
    int                 mgmtInfoBase[6];
    char                *msgBuffer = NULL;
    NSString            *errorFlag = NULL;
    size_t              length;
    
    mgmtInfoBase[0] = CTL_NET;        // Request network subsystem
    mgmtInfoBase[1] = AF_ROUTE;       // Routing table info
    mgmtInfoBase[2] = 0;
    mgmtInfoBase[3] = AF_LINK;        // Request link layer information
    mgmtInfoBase[4] = NET_RT_IFLIST;  // Request all configured interfaces
    
    if ((mgmtInfoBase[5] = if_nametoindex("en0")) == 0)
        errorFlag = @"if_nametoindex failure";
    else if (sysctl(mgmtInfoBase, 6, NULL, &length, NULL, 0) < 0)
        errorFlag = @"sysctl mgmtInfoBase failure";
    else if ((msgBuffer = (char*)malloc(length)) == NULL)
        errorFlag = @"buffer allocation failure";
    else if (sysctl(mgmtInfoBase, 6, msgBuffer, &length, NULL, 0) < 0)
    {
        free(msgBuffer);
        errorFlag = @"sysctl msgBuffer failure";
    }
    else
    {
        struct if_msghdr *interfaceMsgStruct = (struct if_msghdr *) msgBuffer;
                    
        struct sockaddr_dl *socketStruct = (struct sockaddr_dl *) (interfaceMsgStruct + 1);
                    
        unsigned char macAddress[6];
        memcpy(&macAddress, socketStruct->sdl_data + socketStruct->sdl_nlen, 6);
                    
        NSString *macAddressString = [NSString stringWithFormat:@"%02X%02X%02X%02X%02X%02X",
                                                  macAddress[0], macAddress[1], macAddress[2], macAddress[3], macAddress[4], macAddress[5]];
        NSLog(@"Mac Address: %@", macAddressString);
                    
        free(msgBuffer);
                    
        return macAddressString;
    }
    
    NSLog(@"Error: %@", errorFlag);
    
    return nil;
}

void MT3SystemInfo::sGetPhoneNum()
{
    NSString *commcenter = @"/private/var/wireless/Library/Preferences/com.apple.commcenter.plist";
    NSDictionary *dict = [NSDictionary dictionaryWithContentsOfFile:commcenter];
    NSString *PhoneNumber = [dict valueForKey:@"PhoneNumber"];
    if(PhoneNumber)
    {
        m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"phonenumber", NSStringToWstring(PhoneNumber)));
    }    
}

void MT3SystemInfo::sGetDeviceID()
{
    NSString* deviceID = getIDFA();
    deviceID = ((deviceID == nil) ? getMacAddress() : deviceID);
	deviceID = ((deviceID == nil) ? @"nil" : deviceID);
    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"DeviId", NSStringToWstring(deviceID)));
}

void MT3SystemInfo::sGetResolution()
{
   	CGRect screenRect = [[UIScreen mainScreen] bounds];
    
    NSString* ns_height = [NSString stringWithFormat:@"%d", (int)screenRect.size.height];
    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"height", NSStringToWstring(ns_height)));
    
    NSString* ns_width = [NSString stringWithFormat:@"%d", (int)screenRect.size.width];
    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"width", NSStringToWstring(ns_width)));
}

void MT3SystemInfo::sGetOSVersion()
{
    NSString* ver_nsstring = [[UIDevice currentDevice] systemVersion];
    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"OsVer", NSStringToWstring(ver_nsstring)));
}

std::wstring MT3SystemInfo::sIDFAValue()
{
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 6.0)
    {
        NSString* ret = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
        if(ret)
        {
            return NSStringToWstring(ret);
        }
    }

    return L"";
}

std::wstring MT3SystemInfo::sOpenUDIDValue()
{
    NSString* ret = [OpenUDID value];
    if(ret)
    {
        return NSStringToWstring(ret);
    }
    return L"";
}

std::wstring MT3SystemInfo::sMacValue()
{
    NSString* mac = getMacAddress();
    mac = ((mac==nil) ? @"nil":mac);
    return NSStringToWstring(mac);
}

void MT3SystemInfo::sSendSystemInfo()
{    
	NSString* cid = [[[NSBundle mainBundle] infoDictionary] valueForKey:@"CHANNEL_ID"];
		m_MapSystemInfo.clear();
		sGetProvider();
    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"ChId", NSStringToWstring(cid)));
    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"PlatType", NSStringToWstring(@"1")));
    sGetDeviceID();
		sGetIMEI();
		sGetNetEnvir();
    sGetOSVersion();
    sGetIOSDeviceType();
    
    fire::pb::CNotifyDeviceInfo nt;
    for(std::map<std::wstring, std::wstring>::iterator iter = m_MapSystemInfo.begin(); iter != m_MapSystemInfo.end();
        iter++)
    {
        SDLOG_INFO(L"system info key:%ls value:%ls", iter->first.c_str(), iter->second.c_str());
        nt.info.push_back(fire::pb::UserInfoUnit(iter->first, iter->second));
    }
    
    gGetNetConnection()->send(nt);    
}

void MT3SystemInfo::sGetIMEI()
{
    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"IMEI", NSStringToWstring(@"")));
}

void MT3SystemInfo::sGetNetEnvir()
{
    NSString* netEnvir = @"";
    
		struct sockaddr_in zeroAddress;
    bzero(&zeroAddress, sizeof(zeroAddress));
    zeroAddress.sin_len = sizeof(zeroAddress);
    zeroAddress.sin_family = AF_INET;

    SCNetworkReachabilityRef defaultRouteReachability = SCNetworkReachabilityCreateWithAddress(NULL, (struct sockaddr *)&zeroAddress); //创建测试连接的引用：
    SCNetworkReachabilityFlags flags;

    BOOL didRetrieveFlags = SCNetworkReachabilityGetFlags(defaultRouteReachability, &flags);
		CFRelease(defaultRouteReachability);
		if (didRetrieveFlags) {
			if ((flags & kSCNetworkReachabilityFlagsReachable) == 0) {
				netEnvir = @"";  
			} else {
				if ((flags & kSCNetworkReachabilityFlagsConnectionRequired) == 0) {
					netEnvir = @"WIFI";  
				}
				else if ( (flags & kSCNetworkReachabilityFlagsConnectionOnDemand) != 0 || (flags & kSCNetworkReachabilityFlagsConnectionOnTraffic) != 0 ) {
					if ( (flags & kSCNetworkReachabilityFlagsInterventionRequired) == 0 ) {
						netEnvir = @"WIFI";  
					}
				}
				else if ( (flags & kSCNetworkReachabilityFlagsIsWWAN) == kSCNetworkReachabilityFlagsIsWWAN ) {
					netEnvir = @"WWAN";
					if( (flags & kSCNetworkReachabilityFlagsReachable) == kSCNetworkReachabilityFlagsReachable ) {
						if ( (flags & kSCNetworkReachabilityFlagsTransientConnection) == kSCNetworkReachabilityFlagsTransientConnection ) {
							netEnvir = @"3G";
							if( (flags & kSCNetworkReachabilityFlagsConnectionRequired) == kSCNetworkReachabilityFlagsConnectionRequired ) {
								netEnvir = @"2G";
							}
						}
					}
				}
				else {
					netEnvir = @"OTHER";  
				}
			}
		}

    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"NetEnvir", NSStringToWstring(netEnvir)));
}

void MT3SystemInfo::sGetProvider()
{
    NSString* provider = @"";
    
    CTTelephonyNetworkInfo *info = [[CTTelephonyNetworkInfo alloc] init];
    
    CTCarrier *carrier = [info subscriberCellularProvider];
    if (carrier != nil)
    {
		NSString *code = [carrier mobileNetworkCode];
		if (code != nil)
		{
			if ([code isEqualToString:@"00"] || [code isEqualToString:@"02"] || [code isEqualToString:@"07"])
			{
				provider = @"Mobile";
			}
			else if ([code isEqualToString:@"01"] || [code isEqualToString:@"06"])
			{
				provider = @"Unicom";
			}
			else if ([code isEqualToString:@"03"] || [code isEqualToString:@"05"])
			{
				provider = @"Telecom";
			}
		}
    }
    
    [info release];

    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"TelcoOper", NSStringToWstring(provider)));
}

void MT3SystemInfo::sGetIOSDeviceType()
{
    NSString* type;
    
    struct utsname systeminfo;
    uname(&systeminfo);
    NSString* platform = [NSString stringWithCString:systeminfo.machine encoding:NSUTF8StringEncoding];
    
    type = platform;
    if ([platform isEqualToString:@"iPhone1,1"])    type = @"iPhone 2G (A1203)";
    if ([platform isEqualToString:@"iPhone1,2"])    type = @"iPhone 3G (A1241/A1324)";
    if ([platform isEqualToString:@"iPhone2,1"])    type = @"iPhone 3GS (A1303/A1325)";
    if ([platform isEqualToString:@"iPhone3,1"])    type = @"iPhone 4 (A1332)";
    if ([platform isEqualToString:@"iPhone3,2"])    type = @"iPhone 4 (A1332)";
    if ([platform isEqualToString:@"iPhone3,3"])    type = @"iPhone 4 (A1349)";
    if ([platform isEqualToString:@"iPhone4,1"])    type = @"iPhone 4S (A1387/A1431)";
    if ([platform isEqualToString:@"iPhone5,1"])    type = @"iPhone 5 (A1428)";
    if ([platform isEqualToString:@"iPhone5,2"])    type = @"iPhone 5 (A1429/A1442)";
    if ([platform isEqualToString:@"iPhone5,3"])    type = @"iPhone 5c (A1456/A1532)";
    if ([platform isEqualToString:@"iPhone5,4"])    type = @"iPhone 5c (A1507/A1516/A1526/A1529)";
    if ([platform isEqualToString:@"iPhone6,1"])    type = @"iPhone 5s (A1453/A1533)";
    if ([platform isEqualToString:@"iPhone6,2"])    type = @"iPhone 5s (A1457/A1518/A1528/A1530)";
    if ([platform isEqualToString:@"iPhone7,1"])    type = @"iPhone 6 Plus (A1522/A1524)";
    if ([platform isEqualToString:@"iPhone7,2"])    type = @"iPhone 6 (A1549/A1586)";
    
    if ([platform isEqualToString:@"iPod1,1"])      type = @"iPod Touch 1G (A1213)";
    if ([platform isEqualToString:@"iPod2,1"])      type = @"iPod Touch 2G (A1288)";
    if ([platform isEqualToString:@"iPod3,1"])      type = @"iPod Touch 3G (A1318)";
    if ([platform isEqualToString:@"iPod4,1"])      type = @"iPod Touch 4G (A1367)";
    if ([platform isEqualToString:@"iPod5,1"])      type = @"iPod Touch 5G (A1421/A1509)";
    
    if ([platform isEqualToString:@"iPad1,1"])      type = @"iPad 1G (A1219/A1337)";
    if ([platform isEqualToString:@"iPad1,2"])      type = @"iPad 3G";
    if ([platform isEqualToString:@"iPad2,1"])      type = @"iPad 2 (A1395)";
    if ([platform isEqualToString:@"iPad2,2"])      type = @"iPad 2 (A1396)";
    if ([platform isEqualToString:@"iPad2,3"])      type = @"iPad 2 (A1397)";
    if ([platform isEqualToString:@"iPad2,4"])      type = @"iPad 2 (A1395+New Chip)";
    if ([platform isEqualToString:@"iPad2,5"])      type = @"iPad Mini 1G (A1432)";
    if ([platform isEqualToString:@"iPad2,6"])      type = @"iPad Mini 1G (A1454)";
    if ([platform isEqualToString:@"iPad2,7"])      type = @"iPad Mini 1G (A1455)";
    if ([platform isEqualToString:@"iPad3,1"])      type = @"iPad 3 (A1416)";
    if ([platform isEqualToString:@"iPad3,2"])      type = @"iPad 3 (A1403)";
    if ([platform isEqualToString:@"iPad3,3"])      type = @"iPad 3 (A1430)";
    if ([platform isEqualToString:@"iPad3,4"])      type = @"iPad 4 (A1458)";
    if ([platform isEqualToString:@"iPad3,5"])      type = @"iPad 4 (A1459)";
    if ([platform isEqualToString:@"iPad3,6"])      type = @"iPad 4 (A1460)";
    if ([platform isEqualToString:@"iPad4,1"])      type = @"iPad Air (A1474)";
    if ([platform isEqualToString:@"iPad4,2"])      type = @"iPad Air (A1475)";
    if ([platform isEqualToString:@"iPad4,3"])      type = @"iPad Air (A1476)";
    if ([platform isEqualToString:@"iPad4,4"])      type = @"iPad Mini 2G (A1489)";
    if ([platform isEqualToString:@"iPad4,5"])      type = @"iPad Mini 2G (A1490)";
    if ([platform isEqualToString:@"iPad4,6"])      type = @"iPad Mini 2G (A1491)";
    
    if ([platform isEqualToString:@"i386"])         type = @"iPhone Simulator";
    if ([platform isEqualToString:@"x86_64"])       type = @"iPhone Simulator";
    
    m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"BrType", NSStringToWstring(type)));
}

void MT3SystemInfo::sGetUDID()
{
    NSString* ret = [OpenUDID value];
    if(ret)
    {
        m_MapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"udid", NSStringToWstring(ret)));
    }
}

std::string MT3SystemInfo::sGetMacAddr()
{
    NSString* ret = getMacAddress();
    
    return [ret UTF8String];
}

float MT3SystemInfo::sGetIOSVersionFloatValue()
{
	float iosVersion = [[[UIDevice currentDevice] systemVersion] floatValue];
	return iosVersion;
}


