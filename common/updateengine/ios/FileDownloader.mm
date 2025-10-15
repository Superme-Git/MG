#include "FileDownloader.h"
#import "ASIHTTPRequest.h"
#include "StringCover.h"
#import "GlobalNotification.h"

bool FileDownloader::SynDownloadOneFile(const std::wstring& urlstring, const std::wstring& destfile, bool notify)
{
    std::string utf8urlstring = StringCover::SUNWstring2String(urlstring);
    std::string utf8filename = StringCover::SUNWstring2String(destfile);
    NSString *destnsfile = [NSString stringWithUTF8String:utf8filename.c_str()];
    
    
     //获取所在目录, 如果目录不存在，则递归创建
    NSString* destnspath = [[[NSString alloc] init] autorelease];
   
    NSArray* deststringarry = [destnsfile pathComponents];
    NSRange range = NSMakeRange(1, [deststringarry count]-2);
    NSArray* destpatharray = [deststringarry subarrayWithRange:range];
    for (NSString* str in destpatharray)
    {
        destnspath = [destnspath stringByAppendingString: @"/"];
        destnspath = [destnspath stringByAppendingString: str];
    }
    
    
    NSFileManager* fileManager = [NSFileManager defaultManager];
    NSError* error;
    if(![fileManager fileExistsAtPath:destnspath])
    {
        //create dir
        [fileManager createDirectoryAtPath:destnspath withIntermediateDirectories:YES attributes:nil error:&error];
    }
    
    [fileManager removeItemAtPath:destnsfile error:&error];
    
    
    NSURL* url = [NSURL URLWithString:[NSString stringWithUTF8String:utf8urlstring.c_str()]];
    ASIHTTPRequest* request = [ASIHTTPRequest requestWithURL:url];
    [request setDownloadDestinationPath:destnsfile];
    [request setTimeOutSeconds:30];
    [request setNumberOfTimesToRetryOnTimeout:0];
    [request setDownloadProgressDelegate:GlobalNotification::GetInstance().getWatcher()];
    [request setTemporaryFileDownloadPath:[destnsfile stringByAppendingString:@".download"]];
    [request setAllowResumeForFileDownloads:YES];
    [request addRequestHeader:@"Content-type" value:@"application/octet-stream"];
    [request startSynchronous];
    
    return ([request error] == nil && [request responseStatusCode] == 200);
}
