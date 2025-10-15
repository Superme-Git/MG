
#include <string>
#include "IOS_Utils.h"
#include "netdb.h"
#include "arpa/inet.h"
#include "Utils.h"
#include "StringCover.h"
#import <UIKit/UIKit.h>
@interface SDAlertView : NSObject <UIAlertViewDelegate>
+(SDAlertView*) getInstance;
-(void) GameResLoadError;
@end

@implementation SDAlertView
+(SDAlertView *)getInstance
{
    __strong static SDAlertView * instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

-(void)GameResLoadError
{
	NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
	UIAlertView* newalert = [[[UIAlertView alloc] initWithTitle:[data valueForKey:@"str15"] message:[data valueForKey:@"str16"] delegate:self cancelButtonTitle:[data valueForKey:@"str17"] otherButtonTitles:nil] autorelease];
	[newalert setTag:1];
	[newalert show];
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if ([alertView tag] == 1) {
		std::wstring vmFile = MHSD_UTILS::GetRunDir()+L"/.version.meta";
		std::remove(SHARE_Wstring2String(vmFile).c_str());
		exit(0);
	}
}
@end

namespace IOS_MHSD_UTILS
{
    void OpenURL(const std::string& urlstring)
    {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithUTF8String:urlstring.c_str()]]];
    }
    
    std::string getIPAddressForHost(const std::string& urlstring)
    {
        NSString *theHost = [NSString stringWithUTF8String:urlstring.c_str()];
        if ([theHost rangeOfString:@"http://"].location != NSNotFound)
        {
            theHost=[theHost substringFromIndex:7];
        }
        NSArray *array = [theHost componentsSeparatedByString:@"/"];
        theHost = [array objectAtIndex:0];
        
        struct hostent *host = gethostbyname([theHost UTF8String]);
        if (!host) {return ""; }
        struct in6_addr **list = (struct in6_addr **)host->h_addr_list;
        
        char dstIP[256];
        inet_ntop(AF_INET, (void*)*host->h_addr_list[0], dstIP, sizeof(dstIP));
        NSString *addressString = [NSString stringWithCString:dstIP encoding:NSUTF8StringEncoding];
        
        return addressString.UTF8String;
    }
	
	void GameResLoadError()
	{
		SDAlertView *av = [SDAlertView getInstance];
		[av GameResLoadError];
	}
	
	float GetCurBrightness()
	{
		return [UIScreen mainScreen].brightness;
	}
	
	void SetCurBrightness(float brightness)
	{
		[[UIScreen mainScreen] setBrightness:brightness];
	}

}//namepace IOS_MHSD_UTILS


