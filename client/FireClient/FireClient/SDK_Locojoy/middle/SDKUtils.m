//
//  Created by locojoy on 14-3-4.
//  Copyright (c) 2014年 locojoy. All rights reserved.
//
/*
#include <stdio.h>
#include <stdarg.h>
#include <net/if.h>
#include <net/if_dl.h>
#include <sys/sysctl.h>
#include <sys/socket.h>
#include <mach/mach.h>
#include <ifaddrs.h>
#include <arpa/inet.h>*/



#import "GTMBase64.h"
#import <CommonCrypto/CommonDigest.h>
#import <AdSupport/AdSupport.h>
#import <UIKit/UIKit.h>

#include "SDKUtils.h"

#define kTag_MainScreenView        (2002)
#define kTag_ActivityIndicatoriew  (2001)
#define kTag_PromptView            (2000)
#define ToHex(X) ((X) > 9 ? (X) + 55 : (X) + 48)

@implementation SDKUtils

+ (NSString*)encodeBase64String:(NSString * )input {
    NSData *data = [input dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
    data = [GTMBase64 encodeData:data];
    NSString *base64String = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    return base64String;
}

+ (NSString*)decodeBase64String:(NSString * )input {
    NSData *data = [input dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
    data = [GTMBase64 decodeData:data];
    NSString *base64String = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    return base64String;
} 

+ (NSString*)encryptMD5String:(NSString*)string {
    const char *cStr = [string UTF8String];
    unsigned char result[16];
    CC_MD5( cStr, (CC_LONG)strlen(cStr), result );
    return [NSString stringWithFormat:@"%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x",
            result[0], result[1], result[2], result[3],
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]
            ];

}


+ (NSString*)getMac {
    
    if ([[[UIDevice currentDevice] systemVersion] compare:@"6.0" options:NSNumericSearch] != NSOrderedAscending)
    {
        // iOS6版本以上用IDFA
        NSUUID* adID =  [[ASIdentifierManager sharedManager] advertisingIdentifier];
        return [adID UUIDString];
    }
    
    return @"";
}

+ (NSString*)getBundleId {
    return [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)kCFBundleIdentifierKey];
}

+ (NSString*)UrlEncode:(NSString*)nstring
{
    
    NSString* outputStr = (__bridge NSString *)CFURLCreateStringByAddingPercentEscapes
    (kCFAllocatorDefault,
     (__bridge CFStringRef)nstring,
     NULL,
     (CFStringRef)@"!*'();:@&=+$,/?%#[]~",
     kCFStringEncodingUTF8);
    
    return outputStr;
    
}
/*
 std::string strTemp = "";
 const char *str = [nstring UTF8String];
 size_t length = strlen(str);
 
 for (size_t i = 0; i < length; i++)
 {
 if (isalnum((unsigned char)str[i])
 || (str[i] == '-')
 || (str[i] == '_')
 || (str[i] == '.')
 || (s
 == '~')
 )
 {
 strTemp += str[i];
 }
 else if (str[i] == ' ')
 {
 strTemp += "+";
 }
 else
 {
 strTemp += '%';
 strTemp += ToHex((unsigned char)str[i] >> 4);
 strTemp += ToHex((unsigned char)str[i] % 16);
 }
 }
 
 return [NSString stringWithUTF8String:strTemp.c_str()];*/
@end


