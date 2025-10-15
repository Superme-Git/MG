#ifndef _UTILS_H_
#define _UTILS_H_

#import <Foundation/Foundation.h>

@interface SDKUtils : NSObject

+ (NSString*)encodeBase64String:(NSString *)input;
+ (NSString*)decodeBase64String:(NSString *)input;
+ (NSString*)encryptMD5String:(NSString*)string;
+ (NSString*)getMac;
+ (NSString*)getBundleId;
+ (NSString*)UrlEncode:(NSString*)str;

@end

#endif
