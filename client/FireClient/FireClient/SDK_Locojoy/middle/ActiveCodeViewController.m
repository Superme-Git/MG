//
//  ActiveCodeViewController.m
//  LJSDKDemo
//
//  Created by locojoy on 16/5/12.
//  Copyright © 2016年 leneqi. All rights reserved.
//

#import "ActiveCodeViewController.h"
#import "HttpRequest.h"
#import "PlatformSDK.h"
#import "SDKUtils.h"

@interface ActiveCodeViewController ()<UIGestureRecognizerDelegate>
{
}
@property (weak, nonatomic) IBOutlet UITextField *testFieldActiveCode;

@property (weak, nonatomic) IBOutlet UIView *viewCenterView;
@property (weak, nonatomic) IBOutlet UIButton *butActive;
@property (weak, nonatomic) IBOutlet UILabel *labelTip;
@end

@implementation ActiveCodeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    /*键盘出现和消失通知添加*/
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(keyboardWillBeShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillBeHidden:) name:UIKeyboardWillHideNotification object:nil];
    /*点击背景手势添加*/
    UITapGestureRecognizer * hideKeyBoard = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(hideKeyBoardClick)];
    hideKeyBoard.delegate = self ;
    [self.view  addGestureRecognizer:hideKeyBoard];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)hideKeyBoardClick{
    [self.view endEditing:NO];
}
- (void)keyboardWillBeShow:(NSNotification*)notification
{

    
    NSArray* constrains2 = self.view.constraints;;
    for (NSLayoutConstraint* constraint in constrains2) {
        if (constraint.secondItem == self.view && constraint.firstItem == self.viewCenterView) {
            //据底部0
            if (constraint.firstAttribute == NSLayoutAttributeCenterY ) {
                constraint.constant = -100.0;
                break;
            }
        }
    }


}
- (IBAction)backUp:(id)sender {
    
    
    //[self dismissViewControllerAnimated:YES completion:nil];
    [self dismissViewControllerAnimated:YES completion:^{
        [self.platformsdk login:NO];
    }];
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{

    return  YES;
}

- (void)keyboardWillBeHidden:(NSNotification*)notification
{
    NSArray* constrains2 = self.view.constraints;;
    for (NSLayoutConstraint* constraint in constrains2) {
        if (constraint.secondItem == self.view && constraint.firstItem == self.viewCenterView) {
            //据底部0
            if (constraint.firstAttribute == NSLayoutAttributeCenterY ) {
                constraint.constant = 0;
                break;
            }
        }
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)ActiveCode:(id)sender {
    NSString*activeCode = self.testFieldActiveCode.text;
    
    NSString * regex = @"^[A-Za-z0-9]{5,20}$";
    NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    BOOL isMatch = [pred evaluateWithObject:activeCode];
    if(!isMatch)
    {
        self.labelTip.text = @"*激活码格式不对";
        return;
    }
    else
    {
        self.labelTip.text = @"";
    }
    
    HttpRequest*req = [[HttpRequest alloc] init];
    
    NSTimeInterval time = [[NSDate date] timeIntervalSince1970];
    long long int timestamp = (long long int)time;
    NSString*version = @"1";
    
    NSString* gameId  = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"GAMEID"];
    NSString* gameKey = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"GAMEKEY"];
    NSString* data = [NSString stringWithFormat:@"{\"PlatformId\":\"%@\",\"Channel\":\"%@\",\"Time\":\"%lld\",\"ActiveCode\":\"%@\"}",self.accountId,self.channelId,timestamp,activeCode];
    NSString* strToBeSign = [NSString stringWithFormat:@"%@%@%@",gameKey,version,data];
    NSString* sign = [SDKUtils encryptMD5String:strToBeSign];
    data = [SDKUtils UrlEncode:data];
    NSString*url = @"";
    if([[[[NSBundle mainBundle]infoDictionary]objectForKey:@"SDK_Debug"] boolValue])
    {
        NSString*baseurl = @"http://testot.locojoy.com/api/ActiveCode/Active.ashx";
        url = [NSString stringWithFormat:@"%@?Version=%@&GameId=%@&Data=%@&Sign=%@",baseurl,version,gameId,data,sign];
    }
    else{
        NSString*baseurl = @"http://ot.locojoy.com/api/ActiveCode/Active.ashx";
        url = [NSString stringWithFormat:@"%@?Version=%@&GameId=%@&Data=%@&Sign=%@",baseurl,version,gameId,data,sign];
    }
    __weak ActiveCodeViewController*weakSelf = self;
    req.handler= ^(BOOL bSuccess, NSData *data,HttpRequest*req)
    {
        if(bSuccess)
        {
            NSError *error;
            NSDictionary *dict = [NSJSONSerialization
                                  JSONObjectWithData:data
                                  options:NSJSONReadingMutableLeaves error:&error];
            NSString *code = [dict objectForKey:@"Code"];
            //激活码验证成功
            if([code isEqualToString:@"1"])
            {
                [weakSelf dismissViewControllerAnimated:YES completion:^{
                    [weakSelf.platformsdk _doLoginVerify:self.accountId    withSession:self.token];
                }];
            }
            else
            {
                weakSelf.labelTip.text = [NSString stringWithFormat:@"*%@", [dict objectForKey:@"Message"]];
            }
        }
        else{
            //发生http错误了 重新连接
            [req start:url];
        }
        
    };
    [req start:url];
    
}


@end
