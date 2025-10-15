//  FileDownloader.h
//  FireClient


#ifndef __FireClient__ProgressIndicator__
#define __FireClient__ProgressIndicator__

#include <string>

#import "ASIProgressDelegate.h"

@interface ProgressIndicator:NSObject<ASIProgressDelegate>
{
    
}

- (void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes;
    
@end

#endif /* defined(__FireClient__ProgressIndicator__) */
