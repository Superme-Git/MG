//
//  PhotoPicker.mm
//  cocos2dx
//
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AVFoundation/AVMediaFormat.h>
#import <AVFoundation/AVCaptureDevice.h>
#import <Photos/Photos.h>

#include "PhotoPicker.h"
#include "CCImage.h"

#define CAMERA_PHOTO_QUALITY 0.8

// class LandscapeUIImagePickerController

@interface LandscapeUIImagePickerController : UIImagePickerController
@end

@implementation LandscapeUIImagePickerController

-(UIInterfaceOrientation)preferredInterfaceOrientationForPresentation {
    return UIInterfaceOrientationPortrait;
}

- (NSUInteger)supportedInterfaceOrientations{
    return UIInterfaceOrientationMaskAll;
}

- (BOOL)shouldAutorotate {
    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
    if(UIInterfaceOrientationIsLandscape(deviceOrientation)) {
        return NO;
    } else {
        return YES;
    }}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    if(UIInterfaceOrientationIsLandscape(interfaceOrientation)) {
        return NO;
    } else {
        return YES;
    }
}

@end

////////////////////////////////////////////////////////////////////////////////////////////////

@interface PhotoPickerImplementor : NSObject<UINavigationControllerDelegate, UIImagePickerControllerDelegate>
{
    cocos2d::PhotoPicker* mpPicker;
}
- (void) setPhotoPicker: (cocos2d::PhotoPicker*)photoPicker;
- (bool) openCamera;
- (bool) openAlbum;
@end

@implementation PhotoPickerImplementor

- (void) setPhotoPicker: (cocos2d::PhotoPicker*)photoPicker
{
    mpPicker = photoPicker;
}

- (bool) openCamera
{
    NSString* mediaType = AVMediaTypeVideo;
    AVAuthorizationStatus authStatus = [AVCaptureDevice authorizationStatusForMediaType:mediaType];
    if(authStatus == AVAuthorizationStatusRestricted || authStatus == AVAuthorizationStatusDenied)
    {
        cocos2d::CCMessageBox("你的相机功能未打开,请设置", "错误");
        return false;
    }
    
    UIImagePickerControllerSourceType sourceType = UIImagePickerControllerSourceTypeCamera;
    if ([UIImagePickerController isSourceTypeAvailable: UIImagePickerControllerSourceTypeCamera])
    {
        UIImagePickerController *picker = [[LandscapeUIImagePickerController alloc] init];
        
        picker.delegate = self;
        //设置拍照后的图片可被编辑
        picker.allowsEditing = YES;
        picker.sourceType = sourceType;
        [[UIApplication sharedApplication].keyWindow.rootViewController presentModalViewController:picker animated:YES];
        [picker release];
        return true;
    }
    else
    {
        NSLog(@"相机不可用");
        return false;
    }
}

- (bool) openAlbum
{
    PHAuthorizationStatus authStatus = [PHPhotoLibrary authorizationStatus];
    if(authStatus == PHAuthorizationStatusDenied || authStatus == PHAuthorizationStatusRestricted)
    {
        cocos2d::CCMessageBox("你的照片功能未打开,请设置", "错误");
        return false;
    }
    
    UIImagePickerController *picker = [[UIImagePickerController alloc] init];
    
    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    picker.delegate = self;
    //设置选择后的图片可被编辑
    picker.allowsEditing = YES;
    [[UIApplication sharedApplication].keyWindow.rootViewController presentModalViewController:picker animated:YES];
    [picker release];
    
    return true;
}

-(void)imagePickerController:(UIImagePickerController*)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    NSString *type = [info objectForKey:UIImagePickerControllerMediaType];
    
    //当选择的类型是图片
    if ([type isEqualToString:@"public.image"])
    {
        //先把图片转成NSData
        UIImage* image = [info objectForKey:UIImagePickerControllerEditedImage];
        if(image == nil)
        {
            image = [info objectForKey:UIImagePickerControllerOriginalImage];
        }
        
        NSData *data = UIImageJPEGRepresentation(image, CAMERA_PHOTO_QUALITY);
        if (data == nil)
        {
            data = UIImagePNGRepresentation(image);
        }
        
        if(data != nil)
        {
            cocos2d::CCImage* img = new cocos2d::CCImage;
            img->initWithImageData(const_cast<void*>(data.bytes), data.length);
            img->autorelease();
            mpPicker->addSelectedPhoto(img);
        }
    }
    
    //关闭相册界面
    [picker dismissModalViewControllerAnimated:YES];

    mpPicker->onSelected();
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    NSLog(@"您取消了选择图片");
    [picker dismissModalViewControllerAnimated:YES];

    mpPicker->onCancel();
}

-(NSUInteger)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(UIWindow *)window
{
    NSString* strSubClass = [NSString stringWithUTF8String:object_getClassName(window.rootViewController.presentedViewController)];
    if ([@"ImgTakeViewController" isEqualToString:strSubClass]) {
        return UIInterfaceOrientationMaskAll;
    }
    return [application supportedInterfaceOrientationsForWindow:window];
}

@end

////////////////////////////////////////////////////////////////////////////////////////////////

NS_CC_BEGIN

bool PhotoPicker::openCamera()
{
    releaseSelectedPhotos();
    
    PhotoPickerImplementor* pImp = [PhotoPickerImplementor alloc];
    [pImp setPhotoPicker: this];
    [pImp openCamera];
    return true;
}

bool PhotoPicker::openAlbum()
{
    releaseSelectedPhotos();
    
    PhotoPickerImplementor* pImp = [PhotoPickerImplementor alloc];
    [pImp setPhotoPicker: this];
    [pImp openAlbum];
    return true;
}

NS_CC_END