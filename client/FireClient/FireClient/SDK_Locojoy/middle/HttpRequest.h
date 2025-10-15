#import <Foundation/Foundation.h>
@class HttpRequest;

typedef void (^HttpRequestCompletionHandler)(BOOL bSuccess, NSData *data,HttpRequest*req);

@interface HttpRequest : NSObject<NSURLConnectionDataDelegate>
{
    NSMutableData *buf;
}
@property(nonatomic,strong)HttpRequestCompletionHandler handler;

- (void) start:(NSString *)url;

@end
