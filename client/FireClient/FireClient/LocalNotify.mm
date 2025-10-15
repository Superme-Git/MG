//  MyClass.m
//  FireClient

#import "LocalNotify.h"
#include "cocos2d.h"


@implementation LocalNotify

//通过类似："2013-04-03 11:00:00"的字符串获取时间
+(NSDate*) getNSDate:(NSString*)timestr
{
    //NSString* timestr = @"2013-04-03 11:00:00";
    NSDateFormatter *formatter = [[[NSDateFormatter alloc] init] autorelease];
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    //NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/Beijing"];
    [formatter setTimeZone:[NSTimeZone defaultTimeZone]];
    NSDate* pushdate = [formatter dateFromString:timestr];
    return pushdate;
}

+(void) addLocalNotification:(NSDate*)pushdate alertbody:(NSString*)alertbody repeatInterval:(NSCalendarUnit)repeatInterval
{
    Class cls = NSClassFromString(@"UILocalNotification");
    if (cls != nil) {
        
        UILocalNotification *notif = [[cls alloc] init];
        notif.fireDate = pushdate;
        notif.timeZone = [NSTimeZone defaultTimeZone];
        
        notif.alertBody = alertbody;
        notif.soundName = UILocalNotificationDefaultSoundName;
        notif.applicationIconBadgeNumber = 1;
        notif.repeatInterval = repeatInterval;
        
        
        [[UIApplication sharedApplication] scheduleLocalNotification:notif];
        [notif release];
    }
}

+(void) clearNotification
{
    [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
    [[UIApplication sharedApplication] cancelAllLocalNotifications];
}

int GetNotifyEnable(int id){
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(id);
	int ret = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SystemsettingTuiSongDlg.LoadConfigById", 1);
	return ret;
}

//开服通知3
+(void) addNotificationForServerOpen
{
    NSString* Times[10];
    NSString* Notify[99];
    
    NSString* rootPath = [[NSBundle mainBundle] resourcePath];
    NSString* cachePath = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSString* plistPath = [cachePath stringByAppendingString:@"/res/506811695"];

    //先判断cache下面
    NSMutableDictionary* data = [[[NSMutableDictionary alloc] initWithContentsOfFile:plistPath] autorelease];
    
    if (data == nil) {
        plistPath = [rootPath stringByAppendingString:@"/res/506811695"];
        //在判断原始resource下面
        data = [[[NSMutableDictionary alloc] initWithContentsOfFile:plistPath] autorelease];
        
        if (data == nil) {
            return;
        }
    }
	
	int AlarmCount = [[data valueForKey:@"alarmcount"] intValue];
	int NotifyCount = [[data valueForKey:@"notifycount"] intValue];
    
    for (int i = 0; i < AlarmCount; i++) {
        Times[i] = [data valueForKey:[NSString stringWithFormat:@"alarm%d", i + 1]];
        Times[i] = [@" " stringByAppendingString:Times[i]];
    }
    
    for (int i = 0; i < NotifyCount; i++) {
        Notify[i] = [data valueForKey:[NSString stringWithFormat:@"notify%d", i + 1]];
    }
    
    NSCalendar* calendar=[[NSCalendar alloc]initWithCalendarIdentifier:NSGregorianCalendar];
    NSDateComponents* comps=[[NSDateComponents alloc]init];
    NSDate* now = [NSDate date];
    comps = [calendar components:NSWeekdayCalendarUnit fromDate:now];
    long curday = [comps weekday];//默认周日是1
    
    int dayoffset = 1;//手动设置系统周1和实际周1的偏移量
    
    for (int i = 0; i < NotifyCount; i++) {
		if(GetNotifyEnable(i + 1) == 0){
			continue;
		}
	
        NSString* Set = [data valueForKey:[NSString stringWithFormat:@"set%d", i + 1]];
        NSArray* arr = [Set componentsSeparatedByString:@","];
        for (int j = 1; j < [arr count]; j++) {
            int timeIdx = [arr[0] intValue];//第一位是用第几条时间，后面数字全是第几天
            int setday = [arr[j] intValue] + dayoffset;//得到周几
            
            NSDate* date = [NSDate date];
            NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
            [dateFormatter setDateFormat:@"yyyy-MM-dd"];
            NSString* dateString = [dateFormatter stringFromDate:date];
            NSString* timestring = [dateString stringByAppendingString: Times[timeIdx - 1]];
            NSDate* pushdate = [self getNSDate:timestring];
            pushdate = [pushdate dateByAddingTimeInterval:(setday - curday)*24*3600];
            
            [self addLocalNotification:pushdate alertbody:Notify[i] repeatInterval:NSWeekCalendarUnit];
            
        }
    }
}

+(void)AddLocalNotify
{
    [LocalNotify clearNotification];
    [LocalNotify addNotificationForServerOpen];
}
@end

