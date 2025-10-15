//
//  HttpRequest.m
//  test91
//
//  Created by locojoy on 14-10-9.
//  Copyright (c) 2014年 locojoy. All rights reserved.
//

#import "HttpRequest.h"

@implementation HttpRequest

-(void)start:(NSString *)url{
    
    NSLog(@"Start request, url= %@", url);
    buf = [[NSMutableData alloc] initWithLength:0];
    NSURLConnection *conn = [NSURLConnection connectionWithRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]] delegate:self];
    [conn start];
}

// connect to remote server
// will receive data
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    [buf setLength:0];
    /*
    NSHTTPURLResponse *res = (NSHTTPURLResponse *)response;
    
    if (res) {
        int code = [res statusCode];
        NSLog(@"Receive response, code = %d, name = %@, url = %@", code, _name, _url);
        if (code != 200) {
            if (_handler) {
                _handler(NO, nil);
                _handler = nil;
            }
        }
    }
     */
}

// receive data
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    [buf appendData:data];
}

// receive data end
- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    NSLog(@"Request success");
    if (_handler) {
        _handler(YES, buf,self);
        _handler = nil;
    }
}

// http error
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    NSLog(@"Request failed!");
    if (_handler) {
        _handler(NO, nil,self);
        _handler = nil;
    }
}

@end
