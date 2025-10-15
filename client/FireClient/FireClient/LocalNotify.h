//  #import <UIKit/UIKit.h>.h
//  FireClient

@interface  LocalNotify : NSObject

+(NSDate*) getNSDate:(NSString*)timestr;
+(void) addLocalNotification:(NSDate*)pushdate alertbody:(NSString*)alertbody;
+(void) clearNotification;

+(void) addNotificationForServerOpen;

+(void)AddLocalNotify;
@end
