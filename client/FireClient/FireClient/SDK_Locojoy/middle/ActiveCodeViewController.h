//
//  ActiveCodeViewController.h
//  LJSDKDemo
//
//  Created by locojoy on 16/5/12.
//  Copyright © 2016年 leneqi. All rights reserved.
//

#import <UIKit/UIKit.h>
@class PlatformSDK;


@interface ActiveCodeViewController : UIViewController
@property(nonatomic,weak) PlatformSDK*platformsdk;
@property(nonatomic,strong) NSString* token;
@property(nonatomic,strong) NSString* accountId;
@property(nonatomic,strong) NSString* channelId;

@end
