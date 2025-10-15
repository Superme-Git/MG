//
//  UIViewController_Orientation.m
//  GrubbyWorm
//
//  Created by ian luo on 16/3/14.
//  Copyright © 2016年 GAME-CHINA.ORG. All rights reserved.
//

#import "UIViewController+OrientationFix.h"

@implementation UIViewController(OrientationFix)

- (BOOL)shouldAutorotate {
    return YES;
}

- (NSUInteger)supportedInterfaceOrientations {
    return UIInterfaceOrientationMaskLandscape;
}

@end


@implementation UIImagePickerController(OrientationFix)

- (BOOL)shouldAutorotate {
    return YES;
}

- (NSUInteger)supportedInterfaceOrientations {
    return UIInterfaceOrientationMaskLandscape;
}

@end