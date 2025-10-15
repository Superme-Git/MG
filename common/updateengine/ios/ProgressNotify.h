//  ProgressNotify.h
//  FireClient

#ifndef FireClient_ProgressNotify_h
#define FireClient_ProgressNotify_h

#import <Foundation/Foundation.h>

@protocol ProgressNotify <NSObject>

@required

-(void) onNotifyMsg: (NSString*)msg;

-(void) onNotifyStart : (NSString*)msg;

-(void) onNotifyEnd;

-(void) onNotifyFail;

-(void) onNotifyStep: (NSNumber*) step;

-(void) onNotifyLocalVersion: (NSString*)localVersion;

-(void) onNotifyLatestVersion: (NSString*)newVersion;

-(void) onNotifyDownLoadSize: (NSString*)sizeString;

-(void) onNotifyDownLoadEnd;

-(void) onNotifyDownLoadSizeTooLarge:(NSNumber*) total;

-(void) onNotifyAppUpdate:(NSDictionary*) dic;

-(void) onNotifyShowForm:(NSDictionary *) dic;

@end

#endif
