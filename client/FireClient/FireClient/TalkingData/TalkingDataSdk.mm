//  TalkingDataSdk.mm
//  FireClient


#import "TalkingDataSdk.h"
#import "TalkingDataAppCpa.h"

void TalkingDataSdk::init(const char * channelid)
{
    NSString * strChannel = [NSString stringWithUTF8String:channelid];
    if([[[[NSBundle mainBundle]infoDictionary]objectForKey:@"IS_CARD_SERVER"] boolValue])
    {
        [TalkingDataAppCpa init:@"10df7d7e82ed46899c44e8cb24138569" withChannelId:strChannel];
    }
    else
    {
        [TalkingDataAppCpa init:@"8b7ce2b84eb6471bba65b3a9ccb0fa96" withChannelId:strChannel];
    }
}