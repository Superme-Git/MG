#include "DeviceData_iOS.h"
#import <UIKit/UIDevice.h>
#include <ifaddrs.h>
#include <string>
#include <sys/socket.h>
#include "Reachability.h"

float gGetBatteryRatio_IOS()
{
    if (![UIDevice currentDevice].batteryMonitoringEnabled) {
        [UIDevice currentDevice].batteryMonitoringEnabled = YES;
    }
    return [UIDevice currentDevice].batteryLevel;
}

bool gIsBatteryCharging_IOS()
{
    if (![UIDevice currentDevice].batteryMonitoringEnabled)
    {
        [UIDevice currentDevice].batteryMonitoringEnabled = YES;
    }
    return [UIDevice currentDevice].batteryState == UIDeviceBatteryStateCharging;
}

int gGetNetworkType_IOS()
{
    struct sockaddr_storage zeroAddress;
    
    bzero(&zeroAddress, sizeof(zeroAddress));
    zeroAddress.ss_len = sizeof(zeroAddress);
    zeroAddress.ss_family = AF_INET;
    
    SCNetworkReachabilityRef defaultRouteReachability = SCNetworkReachabilityCreateWithAddress(NULL, (struct sockaddr*)&zeroAddress);
    SCNetworkReachabilityFlags flags;
    
    BOOL didRetrieveFlags = SCNetworkReachabilityGetFlags(defaultRouteReachability, &flags);
    CFRelease(defaultRouteReachability);
    if (!didRetrieveFlags) {
        return 0;
    }

    int ret = 0;
    
    if ((flags & kSCNetworkReachabilityFlagsConnectionRequired) == 0) {
        ret = 1;
    }
    
    if (((flags & kSCNetworkReachabilityFlagsConnectionOnDemand) != 0 ||
         (flags & kSCNetworkReachabilityFlagsConnectionOnTraffic) != 0) &&
        (flags & kSCNetworkReachabilityFlagsInterventionRequired) == 0) {
        ret = 1;
    }
    
    if ((flags & kSCNetworkReachabilityFlagsIsWWAN) == kSCNetworkReachabilityFlagsIsWWAN) {
        ret = 0;
    }
    
    return ret; //0.other  1.wifi
}

bool gIsNetworkConnected_IOS()
{
	struct sockaddr_in zeroAddress;
    bzero(&zeroAddress, sizeof(zeroAddress));
    zeroAddress.sin_len = sizeof(zeroAddress);
    zeroAddress.sin_family = AF_INET;
    
    SCNetworkReachabilityRef defaultRouteReachability = SCNetworkReachabilityCreateWithAddress(NULL, (struct sockaddr*)&zeroAddress);
    SCNetworkReachabilityFlags flags;
    
    BOOL didRetrieveFlags = SCNetworkReachabilityGetFlags(defaultRouteReachability, &flags);
    CFRelease(defaultRouteReachability);
    
    if (!didRetrieveFlags) {
        return false;
    }
    BOOL isReachable = flags & kSCNetworkFlagsReachable;
    BOOL needsConnection = flags & kSCNetworkFlagsConnectionRequired;
    return (isReachable && !needsConnection) ? true: false;
}

