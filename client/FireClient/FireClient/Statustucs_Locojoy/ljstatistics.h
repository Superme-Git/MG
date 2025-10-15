//
//  BossStat.h
//  DataUpdate

#import <Foundation/Foundation.h>

@interface LJStatistics : NSObject
+(LJStatistics*)getInstance;

-(void)initGameID:(NSString*)gameId channel:(NSString*)channel logKey:(NSString*)logKey;

-(void)onLogin:(NSString*)platformId roleId:(NSString *)roleId roleName:(NSString*)roleName serverId:(NSString *)serverId;
-(void)onRegister:(NSString *)platformId roleId:(NSString *)roleId roleName:(NSString*)roleName serverId:(NSString *)serverId;
-(void)onPay:(NSString *)platformId roleId:(NSString*)roleId roleName:(NSString*)roleName serverId:(NSString*)serverId payType:(int)payType appOrder:(NSString*)appOrder channelOrder:(NSString*)channelOrder productId:(NSString*)productId productPrice:(NSString*)productPrice;
-(void)willEnterForeground;

@end
